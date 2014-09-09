/**
 * \file http_client.cc
 * \author Miguel Erazo
 * 
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */
#include <stdio.h>
#include <stdlib.h>

#include "http_client.h"
#include "net/host.h"
#include "os/protocol_session.h"
#include "proto/tcp/test/http_client.h"
#include "proto/tcp/test/http_traffic.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(HTTPClient);

class HTTPClientSessionTimer : public Timer {
 public:
	HTTPClientSessionTimer(ProtocolSession* protsess, HttpFinishedTrafficEvent* finished_evt) :
			Timer(protsess), finished_traffic_evt(finished_evt) {
	}

	inline void force_cancel() { cancel();  }

 protected:
	virtual void callback() {
		((HTTPClient*)getSession())->timeout(this, finished_traffic_evt);
	}

 private:
	HttpFinishedTrafficEvent* finished_traffic_evt;
};

HTTPClient::HTTPClient(): tcp_master(0), sess_timer(0), bytes_requested(0) {
}

HTTPClient::~HTTPClient() {
	if(sess_timer){
		sess_timer->cancel();
		delete sess_timer;
		sess_timer=0;
	}
}


void HTTPClient::init() {
	//last_log=0;
	//last_bytes=0;
}

void HTTPClient::wrapup(){
	for(SessionSocketBytesMap::iterator i = tcp_session_sock.begin(); i!=tcp_session_sock.end(); i++) {
		std::cout<<"wrapup[uid="<<getUID()<<","<<getUName()<<"] session_id="<<(*i).first
			<<" throughput="<< ((double)(*i).second.first->getInstantaneousRecvThroughput())/((double)1e6) << " MB/s"
			<<" totol download time="<< (*i).second.first->getLastRecv().second()-(*i).second.first->getFirstRecv().second()
			<<", start="<<(*i).second.first->getFirstRecv().second()<<", end="<<(*i).second.first->getLastRecv().second()
			<<", total bytes recv="<< ((double)(*i).second.first->getBytesRecv())/((double)1e6)<<" MB\n";
	}
}

void HTTPClient::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac)
{
	LOG_DEBUG("starting http traffic "<<getUID()<<", at:"<<inHost()->getNow().second()<<", myip="<<this->inHost()->getDefaultIP().toString()<<
			" dstip="<<ipaddr<<", traffictype="<< evt->getTrafficType()<<endl);
	if(!evt->getTrafficType())
		LOG_ERROR("SIGH!"<<endl)
	switch(evt->getTrafficType()->getConfigType()->type_id) {
		case TCP_TRAFFIC: {
			LOG_DEBUG("HTTP START TRAFFIC at " << inHost()->getNow().second() << endl);
			Host* host = inHost();
			assert(host);
			int dst_port_ = 0;
			uint64_t file_size_ = 0;
			//By default this client will ask for fake packets
			bool emulated_ = false;

			TCPTraffic* tt = SSFNET_STATIC_CAST(TCPTraffic*,evt->getTrafficType());
			int target_comm_id = evt->getSrcCommunityID();
			uint32_t traffic_id = evt->getTrafficId();
			UID_t traffic_type_uid = evt->getTrafficTypeUID();

			if(!tcp_master) {
				// Get a pointer to the protocol session below
				tcp_master = (TCPMaster*)inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP);
				if(!tcp_master) LOG_ERROR("missing TCP session.");
			}

			// Get the destination port to connect to
			dst_port_ = tt->getDstPort();
			//LOG_DEBUG("DEST PORT:" << dst_port_ << "\n" << endl);

			////cout << "---> creating a new socket for ip" << ipaddr << endl;
			// Create a new socket with the connection data which will later create a new TCP session
			SimpleSocket* simple_sock = new SimpleSocket(tcp_master, this, ipaddr, dst_port_);

			// Get the file size from the event
			file_size_ = SSFNET_DYNAMIC_CAST(HttpStartTrafficEvent*,evt)->getFileSize();
			std::cout<<getUName()<<"["<<getParent()->getUID()<<"] is downloading "<<file_size_<<" bytes from "<<evt->getDstUID()<<", time="<<inHost()->getNow().second()<<std::endl;

			int tcp_sess_id = simple_sock->getSessionId();
			if(tcp_session_sock.find(tcp_sess_id)==tcp_session_sock.end()){ //this tcp session id has already been in the map
				//create a map for session socket list
				tcp_session_sock.insert(SSFNET_MAKE_PAIR(tcp_sess_id, SSFNET_MAKE_PAIR(simple_sock, file_size_)));
			}

			//It is a bad idea to put changing item in the traffic type, which is a passive object.
			//If we do so, we need to consider synchronization.
			//The easier way is to embed what the client needs to the http start traffic evt and get them directly
			//on the client. Http start traffic event extends the basic start traffic event.

			//***************
			//session id, this is different from tcp session id.
			//tcp session id is unique, session id is not. one tcp session id is correspond to one socket (connection).
			//we use session id & traffic type uid pair to identify a list of tcp session ids (sockets) belong to the session.
			uint32_t session_id = SSFNET_DYNAMIC_CAST(HttpStartTrafficEvent*,evt)->getSessionID();
			IDPair id_pair = SSFNET_MAKE_PAIR(session_id, traffic_type_uid);

			//add a new entry to the map, or add the tcp session id to the existing entries
			if(session_map.find(id_pair)!=session_map.end()){ //this session id from the traffic type with traffic_type_uid has already been in the map
				(*session_map.find(id_pair)).second.push_back(tcp_sess_id);
			}else { //this is the first download of a new session
				//create a map for id_pair and tcp_session_id list
				TCPSessionIDList tcpsess_id_list;
				tcpsess_id_list.push_back(tcp_sess_id);
				session_map.insert(SSFNET_MAKE_PAIR(id_pair, tcpsess_id_list));

				//create HTTP finished traffic event
				HttpFinishedTrafficEvent* finished_evt = new HttpFinishedTrafficEvent(tt, target_comm_id, traffic_id, session_id, tt->getSessionTimeout());
				finished_evt->setTrafficTypeUID(traffic_type_uid);

				LOG_DEBUG("schedule timeout for session id="<<session_id<<
						", traffic type id="<<traffic_type_uid<<
						", at time="<<tt->getSessionTimeout()<<endl);

				// Start a timer to later erase all connection related to this session
				sess_timer = new HTTPClientSessionTimer(this, finished_evt);
				sess_timer->set(VirtualTime(tt->getSessionTimeout(), VirtualTime::SECOND));
			}
			//**************

			// Is this client going to request packets from an emulated host/simulated host acting as an emulated host?
			emulated_ = tt->getToEmulated();

			LOG_DEBUG("FILE SIZE FROM TRAFFIC["<<tt->getUID()<<"]("
				<<inHost()->getUID()<<","<<evt->getDstUID()<<"):" <<  file_size_ << " emulated=" << emulated_ <<  endl);
			//std::cout<<"SEND A REQUEST IN HTTP CLIENT"<<"\n";
			if(emulated_) {
				//this will send a request to an emulated host
                                char* buffer;
                                buffer = new char[100];
                                bytes_requested = file_size_;
                                sprintf(buffer, "GET /%llu.html HTTP/1.0\r\n", file_size_);
				strcat(buffer, "Host: ");                                
				strcat(buffer, (ipaddr.toString()).c_str()); 
				strcat(buffer, "\r\n");
				strcat(buffer, "\r\n");

                                //sprintf(buffer, "GET my content %llu", file_size_);
                                //LOG_DEBUG("REQUESTING: " << buffer << "\n" << endl);
                                simple_sock->send(strlen(buffer), (byte*) buffer);
			} else {
				//this will send a request to an simulated host
				char* buffer;
				buffer = new char[20];
				bytes_requested = file_size_;
				sprintf(buffer, "%llu", file_size_);
				//LOG_DEBUG("REQUESTING: " << buffer << "\n" << endl);
				simple_sock->send(strlen(buffer), (byte*) buffer);
			}

		}
		break;
		default:
			LOG_ERROR("Invalid traffic type...."<<endl)
	}
	evt->free();
}

int HTTPClient::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra)
{
	LOG_ERROR("ERROR: a message is pushed down to the HTTPClient session"
			" from protocol layer above; it's impossible\n");
	return 0;
}

void HTTPClient::timeout(HTTPClientSessionTimer* timer, HttpFinishedTrafficEvent* finished_evt)
{
	VirtualTime now = inHost()->getNow();
	VirtualTime earliest = now; //earliest send or receive time among all sockets
	VirtualTime latest; //latest send or receive time of one socket
	uint32_t session_id = finished_evt->getSessionID();
	UID_t traffic_type_uid = finished_evt->getTrafficTypeUID();
	IDPair id_pair = SSFNET_MAKE_PAIR(session_id, traffic_type_uid);
	LOG_DEBUG("Timer for session="<<session_id<<", traffic type="
			<<traffic_type_uid<<" expired at " << inHost()->getNow().second() << endl);
    IDPairTCPSessionListMap::iterator sl = session_map.find(id_pair);
	if(sl==session_map.end()){
		LOG_ERROR("Invalid session id in finished traffic event!"<<endl)
	}
	TCPSessionIDList& tcpsess_id_list = (*sl).second;
	if(!tcpsess_id_list.empty()){
		for(TCPSessionIDList::iterator iter = tcpsess_id_list.begin(); iter!=tcpsess_id_list.end(); iter++){
			//check if this socket has not been erased.
			SessionSocketBytesMap::iterator si = tcp_session_sock.find(*iter);
			if(si==tcp_session_sock.end()){
				LOG_DEBUG("This socket has been closed because the transmission has completed"<<endl);
				iter = tcpsess_id_list.erase(iter);
				iter--;
			}else{
				SimpleSocket* s=(*si).second.first;
				//check if there is more than 30 seconds has past from the last received time or last send time
				//if so, erase the socket
				latest = (s->getLastRecv() > s->getLastSent()) ? s->getLastRecv() : s->getLastSent();
				if(now-latest >= VirtualTime(finished_evt->getTimeout(), VirtualTime::SECOND)){
					LOG_DEBUG("the socket="<<s<<" is erased at " << this->getNow().second() <<endl);
					std::cout<<"timeout[uid="<<getUID()<<","<<getUName()<<"] tcp session="<<*iter<<" throughput="<< ((double)s->getInstantaneousRecvThroughput())/((double)1e6) << " MB/s"
						<<" total download time="<< s->getLastRecv().second()-s->getFirstRecv().second()
						<<", start="<<s->getFirstRecv().second()<<", end="<<s->getLastRecv().second()
						<<", total bytes recv="<< ((double)s->getBytesRecv())/((double)1e6)<<" MB\n";
					s->disconnect();
					//remove this socket from the tcp_session_sock map
					tcp_session_sock.erase(si);
					iter = tcpsess_id_list.erase(iter);
					iter--;
				}else if(earliest > latest){
					earliest = latest;
				}
			}
		}
		LOG_DEBUG("now="<<now.second()<<", earliest="<<earliest.second()<<", latest="<<latest.second()<<endl);
	}else{
		LOG_DEBUG("The socket list for session id="<<session_id<<" is empty!"<<endl)
	}
	//if all the sockets in the socket list has been deleted, send deliver event to delete the session
	//otherwise, reset the timer
	if(tcpsess_id_list.empty()){
		inHost()->getCommunity()->deliverEvent(finished_evt, 0);
	}else{
		VirtualTime timeout = VirtualTime(finished_evt->getTimeout(), VirtualTime::SECOND);
		LOG_DEBUG("timeout="<<timeout.second()<<", earliest="<<earliest.second()<<", set delay="<<(timeout-now+earliest).second()<<endl);
		timer->set(timeout-(now-earliest));
	}
}

//ofstream http_downloads;
//bool http_downloads_inited=false;

int HTTPClient::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
	SimpleSocket* sock = SSFNET_STATIC_CAST(SimpleSocket*,extra);
	uint32 status_ = sock->getStatus();
	//LOG_DEBUG("HTTPClient: pop status=" << status_ << endl);
	if(status_ & SimpleSocket::SOCKET_CONNECTED) {
		// This event is not implemented here because this is not a
		//server application
		LOG_ERROR("Got a SOCKET_BUSY signal in HTTPClient!"<<endl)
	}else if(status_ & SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED) {
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		// This event is to keep track of the bytes received so far by this applications
		//LOG_DEBUG("bytes received in client=" << dm->size() << "\n" << endl);
		unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
		//LOG_DEBUG("now="<<inHost()->getNow().second()<<", BYTES RECEIVED SO FAR IN HTTP CLIENT:" << unshared.bytes_received.read() <<endl);
		/*VirtualTime now = inHost()->getNow();
		VirtualTime d = inHost()->getNow()-last_log;
		if(last_log == 0 || d.second() >= 0.99) {
			if(!http_downloads_inited) {
				http_downloads.open("http.stats");
				http_downloads << "uid, time,  bytes in msg, total bytes\n";
				http_downloads_inited=true;
			}
			http_downloads << inHost()->getUID() << ", "<< now.second()<< ", "<<(unshared.bytes_received.read()-last_bytes)<<endl;
			last_log=now;
			last_bytes=unshared.bytes_received.read();
		}*/
		int tcp_session_id = sock->getSessionId();
		SessionSocketBytesMap::iterator sockpair = tcp_session_sock.find(tcp_session_id);
		if(sockpair==tcp_session_sock.end()){
			LOG_ERROR("Invalid tcp session id!"<<endl)
		}else{
			SocketRemainingBytesPair& p= (*tcp_session_sock.find(tcp_session_id)).second;
			p.second=p.second-dm->size();
			//LOG_DEBUG("session id="<<tcp_session_id<<", remaining bytes = "<<p.second<<endl);
			if(p.second<=0){
				LOG_DEBUG("close session="<<tcp_session_id<<", socket="<<p.first<<" at time="<<inHost()->getNow().second()<<endl);
				p.first->disconnect();
				std::cout<<"complete[uid="<<getUID()<<","<<getUName()<<"] tcp_session_id="<<tcp_session_id
					<<" throughput="<< ((double)p.first->getInstantaneousRecvThroughput())/((double)1e6) << " MB/s"
					<<" totol download time="<< p.first->getLastRecv().second()-p.first->getFirstRecv().second()
					<<", start="<<p.first->getFirstRecv().second()<<", end="<<p.first->getLastRecv().second()
					<<", total bytes recv="<< ((double)p.first->getBytesRecv())/((double)1e6)<<" MB\n";
				tcp_session_sock.erase(sockpair);
			}
		}
	}else if(status_ & SimpleSocket::SOCKET_PSH_FLAG) {
		// We do not implement here this case
	}

	return 0;
}

}; // namespace ssfnet
}; // namespace prime
