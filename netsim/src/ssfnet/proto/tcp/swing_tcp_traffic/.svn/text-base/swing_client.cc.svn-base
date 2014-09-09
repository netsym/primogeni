/**
 * \file swing_client.cc
 * \author Ting Li
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

#include "os/timer.h"
#include "net/host.h"
#include "os/protocol_session.h"
#include "swing_client.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(SwingClient);
//#define DEBUG

class TimeoutTimer : public Timer {
 public:
	 TimeoutTimer(ProtocolSession* protsess,SwingFinishedTrafficEvent* finished_evt) :
			Timer(protsess),finished_traffic_evt(finished_evt) {
		 LOG_DEBUG("create timeout timer, finished evt="<<finished_evt<<", target_comm_id="<<finished_evt->getTargetCommunity()
				 <<", finished traffic evt="<<finished_traffic_evt<<", its target comm id="<<finished_traffic_evt->getTargetCommunity()<<endl)
	}

	inline void force_cancel() { cancel();  }

 protected:
	virtual void callback() {
		LOG_DEBUG("callback, finished evt="<<finished_traffic_evt<<", target_comm_id="<<finished_traffic_evt->getTargetCommunity()<<endl)
		((SwingClient*)getSession())->timeout(this, finished_traffic_evt);
	}

 private:
	SwingFinishedTrafficEvent* finished_traffic_evt;
};

SwingClient::SwingClient(): tcp_master(0), simple_sock(0), timeout_timer(0), monitor_file(0){
}

SwingClient::~SwingClient(){
	if(timeout_timer){
		timeout_timer->cancel();
		delete timeout_timer;
		timeout_timer=0;
	}
}

void SwingClient::init() {}

void SwingClient::wrapup(){
	for(IDPairSocketListMap::iterator s = session_socket.begin(); s!=session_socket.end(); s++) {
		SocketList& socket_list = (*s).second;
		for(SocketList::iterator sl = socket_list.begin(); sl!=socket_list.end(); sl++){
			std::cout<<"[uid="<<getUID()<<","<<getUName()<<"] session_id="<<(*s).first.first<<", traffic_id="<<(*s).first.second<<" throughput="<< ((double)(*sl)->getInstantaneousRecvThroughput())/((double)1e6) << " MB/s"
					<<" totol download time="<< (*sl)->getLastRecv().second()-(*sl)->getFirstRecv().second()
					<<", start="<<(*sl)->getFirstRecv().second()<<", end="<<(*sl)->getLastRecv().second()
					<<", total bytes recv="<< ((double)(*sl)->getBytesRecv())/((double)1e6)<<" MB\n";
		}
	}
}

void SwingClient::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac) {
	LOG_DEBUG("Swing START TRAFFIC"<<endl)
	LOG_DEBUG("src ip="<<inHost()->getDefaultIP()<<", dst ip="<<ipaddr<<endl)
	LOG_DEBUG("traffic type id="<<evt->getTrafficType()->getConfigType()->type_id<<endl)
	if (evt->getTrafficType()->getConfigType()->type_id == SWING_TCP_TRAFFIC) {
		Host* host = inHost();
		assert(host);

		SwingTCPTraffic* tt = SSFNET_STATIC_CAST(SwingTCPTraffic*,evt->getTrafficType());
		int target_comm_id = evt->getSrcCommunityID();
		uint32_t traffic_id = evt->getTrafficId();
		//LOG_DEBUG("DEBUGGING EVENT, target comm id="<<target_comm_id<<", from start evt="<<evt<<endl)
		UID_t traffic_type_uid = evt->getTrafficTypeUID();

		int dst_port_= SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,evt)->getDstPort();
		LOG_DEBUG("DST PORT="<<dst_port_<<endl)
		if (!tcp_master) {
			tcp_master = (TCPMaster*) host->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP);
			if (!tcp_master) {
				LOG_ERROR("missing TCP session.");
			}
		}
		LOG_DEBUG("---> CREATING A NEW SOCKET" << endl);
		simple_sock = new SimpleSocket(tcp_master, this, ipaddr, dst_port_);
		//session id and rre id
		uint32_t session_id = SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,evt)->getSessionID();
		IDPair sess_id_pair = SSFNET_MAKE_PAIR(session_id, traffic_type_uid);

		uint32_t rre_id = SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,evt)->getRreID();
		IDPair rre_id_pair = SSFNET_MAKE_PAIR(rre_id, traffic_type_uid);

		//add entries to the corresponding action socket map, or add the socket to the existing entries
		if(session_socket.find(sess_id_pair)!=session_socket.end()){ //this session of the traffic type with traffic_type_uid has already been in the map
			(*session_socket.find(sess_id_pair)).second.push_back(simple_sock);
		}else{ //this is the first pair of this session
			//create a map for session socket list
			SocketList sess_socket_list;
			sess_socket_list.push_back(simple_sock);
			session_socket.insert(SSFNET_MAKE_PAIR(sess_id_pair, sess_socket_list));

			//create swing finished traffic event
			SwingFinishedTrafficEvent* finished_evt = new SwingFinishedTrafficEvent(tt, target_comm_id, traffic_id, session_id, 0, tt->getSessionTimeout());
			finished_evt->setTrafficTypeUID(traffic_type_uid);

			//Start a timer to later erase all sockets related to this session
			//timeout_timer = new TimeoutTimer(this, finished_evt);
			//timeout_timer->set(VirtualTime(tt->getSessionTimeout(), VirtualTime::SECOND));
			//LOG_DEBUG("DEBUGGING EVENT, session create finished evt="<<finished_evt<<", target comm id="<<target_comm_id<<endl)
		}
		if(rre_socket.find(rre_id_pair)!=rre_socket.end()){ //this session has already been in the map
			(*rre_socket.find(rre_id_pair)).second.push_back(simple_sock);
		}else{ //this is the first pair of this session
			//create a map for rre socket list
			SocketList rre_socket_list;
			rre_socket_list.push_back(simple_sock);
			rre_socket.insert(SSFNET_MAKE_PAIR(rre_id_pair, rre_socket_list));

			//create swing finished traffic event
			SwingFinishedTrafficEvent* finished_evt = new SwingFinishedTrafficEvent(tt, target_comm_id, traffic_id, 0, rre_id, tt->getRreTimeout());
			finished_evt->setTrafficTypeUID(traffic_type_uid);

			//Start a timer to later erase all sockets related to this rre
			//timeout_timer = new TimeoutTimer(this, finished_evt);
			//timeout_timer->set(VirtualTime(tt->getRreTimeout(), VirtualTime::SECOND));
			//LOG_DEBUG("DEBUGGING EVENT, rre create finished evt="<<finished_evt<<", target comm id="<<target_comm_id<<endl)
		}
		int req_size = SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,evt)->getRequestSize();
		int rsp_size = SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,evt)->getResponseSize();
		LOG_DEBUG("** req="<<req_size<<", rsp="<<rsp_size<<endl)
		send(req_size, rsp_size);

#ifdef DEBUG
		//Four monitor files for four flows (type/direction)
		//We can monitor: session arrival, rre arrival, # of rre, pair think time, req_size, rsp_size
		//if needed, we can monitor connection level by adding connection id later.
		int flow_type = SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,evt)->getFlowType();

		char absoluteName[160] = "";
		//char parameter[20] = "TCPTester/";
		char filename[20] = "monitor_";
		char type[10];

		strcat(absoluteName, filename);
		sprintf(type, "%d.out", flow_type);
		strcat(absoluteName, type);

		monitor_file = fopen(absoluteName, "a+");
		//write the session id and the download arrival time to the file
		LOG_DEBUG("WRITE TO "<<absoluteName<<", session id="<<session_id<<", rre id="<<rre_id<<", time="<<inHost()->getNow().second()<<endl)
		fprintf(monitor_file, "%d %d %f %d %d\n", session_id, rre_id, inHost()->getNow().second(), req_size, rsp_size);
		fclose(monitor_file);
#endif

	} else {
		LOG_ERROR("Invalid traffic type...."<<endl)
	}
	evt->free();
}

void SwingClient::send(int req_size, int rsp_size)
{
	char* buffer;
	buffer = new char[20];
	sprintf(buffer, "%i", rsp_size);
	LOG_DEBUG("DEBUG MSG="<<(byte*)buffer<<endl)
	simple_sock->send(strlen(buffer), (byte*)buffer);
	/*if(req_size-strlen(buffer)>0){
		simple_sock->send(req_size-strlen(buffer));
	}*/
}

int SwingClient::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra)
{
	LOG_ERROR("ERROR: a message is pushed down to the SwingClient session from protocol layer above; it's impossible\n");
	return 0;
}

int SwingClient::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
	SimpleSocket* sock = SSFNET_STATIC_CAST(SimpleSocket*,extra);
	uint32 status_ = sock->getStatus();

	LOG_DEBUG("in swing pop status=" << status_ << endl);

	if(status_ & SimpleSocket::SOCKET_CONNECTED) {
		// This event is not implemented here because this is not a server application
		LOG_ERROR("Got a SOCKET_BUSY signal in SwingClient!"<<endl)
	}
	if(status_ & SimpleSocket::SOCKET_PSH_FLAG) {
		// We do not implement here this case
	}
	if(status_ & SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED) {
		// We do not implement here this case
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		// This event is to keep track of the bytes received so far by this applications
		//LOG_DEBUG("bytes received in client=" << dm->size() << "\n" << endl);
		unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
		LOG_DEBUG("BYTES RECEIVED SO FAR IN SWING CLIENT:" << dm->size() << endl);
	}

	return 0;
}

void SwingClient::timeout(TimeoutTimer* timer, SwingFinishedTrafficEvent* finished_evt){
	UID_t traffic_type_uid = finished_evt->getTrafficTypeUID();
	uint32_t session_id = finished_evt->getSessionID();
	uint32_t rre_id = finished_evt->getRreID();
	LOG_DEBUG("timeout, finished evt="<<finished_evt<<", target comm id="<<finished_evt->getTargetCommunity()<<endl)

	VirtualTime now = inHost()->getNow();
	VirtualTime earliest = now; //earliest send or receive time among all sockets
	VirtualTime latest; //latest send or receive time of one socket

	SocketList* socket_list = NULL;

	if(session_id!=0){
		IDPair sess_id_pair = SSFNET_MAKE_PAIR(session_id, traffic_type_uid);
		IDPairSocketListMap::iterator sl = session_socket.find(sess_id_pair);
		if(session_socket.find(sess_id_pair)==session_socket.end()){
			LOG_ERROR("Invalid session and traffic type id pair in finished traffic event!"<<endl)
		}
		socket_list = &((*sl).second);
	}else if(rre_id!=0){
		IDPair rre_id_pair = SSFNET_MAKE_PAIR(rre_id, traffic_type_uid);
		IDPairSocketListMap::iterator sl = rre_socket.find(rre_id_pair);
		if(rre_socket.find(rre_id_pair)==rre_socket.end()){
			LOG_ERROR("Invalid rre and traffic type id pair in finished traffic event!"<<endl)
		}
		socket_list = &((*sl).second);
	}else{
		LOG_ERROR("Either session id or rre id should be non-zero!"<<endl)
	}

	if(!socket_list->empty()){
		for(SocketList::iterator iter = socket_list->begin(); iter!=socket_list->end(); iter++){
			if(*iter){ //this socket has not been erased.
				//for every socket, check if there is more than 30 seconds has past from the last received time or last send time
				//if so, erase the socket
				latest = ((*iter)->getLastRecv() > (*iter)->getLastSent()) ? (*iter)->getLastRecv() : (*iter)->getLastSent();
				if(now-latest >= VirtualTime(finished_evt->getTimeout(), VirtualTime::SECOND)){
					LOG_DEBUG("the socket="<<*iter<<" is erased at " << this->getNow().second() <<endl);
					std::cout<<"[uid="<<getUID()<<","<<getUName()<<"] session="<<session_id<<", rre="<<rre_id<<", throughput="<< ((double)(*iter)->getInstantaneousRecvThroughput())/((double)1e6) << " MB/s"
							<<" total download time="<< (*iter)->getLastRecv().second()-(*iter)->getFirstRecv().second()
							<<", start="<<(*iter)->getFirstRecv().second()<<", end="<<(*iter)->getLastRecv().second()
							<<", total bytes recv="<< ((double)(*iter)->getBytesRecv())/((double)1e6)<<" MB\n";
					(*iter)->disconnect();
					iter = socket_list->erase(iter);
					iter--;
				}else if(earliest > latest){
					earliest = latest;
				}
			}
		}
		//LOG_DEBUG("now="<<now.second()<<", earliest="<<earliest.second()<<", latest="<<latest.second()<<endl);
	}else{
		LOG_DEBUG("The socket list for session id="<<session_id<<" or rre id="<<rre_id<<" is empty!"<<endl)
	}

	//if all the sockets in the socket list has been deleted, send deliver event to delete the session
	//otherwise, reset the timer
	if(socket_list->empty()){
		inHost()->getCommunity()->deliverEvent(finished_evt, 0);
	}else{
		VirtualTime timeout = VirtualTime(finished_evt->getTimeout(), VirtualTime::SECOND);
		LOG_DEBUG("timeout="<<timeout.second()<<", earliest="<<earliest.second()<<", set delay="<<(timeout-now+earliest).second()<<endl);
		timer->set(timeout-(now-earliest));
	}
}

}; // namespace ssfnet
}; // namespace prime
