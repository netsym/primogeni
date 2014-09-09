/**
 * \file cnf_session.cc
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

#include "os/protocol_session.h"
#include "os/logger.h"
#include "net/host.h"
#include "proto/cnf/cnf_session.h"
#include "proto/tcp/agent/tcp_session.h"
#include "proto/tcp/tcp_master.h"
#include "proto/cnf/test/cnf_traffic.h"
#include "proto/cnf/cnf_message.h"
#include "os/partition.h"
#include "net/net.h"

#define USE_TCP
//#define LOG_DEBUG(X) { std::cout<<"["<<"cnf_session.cc"<<":"<<__LINE__<<"]"<<X; }

//#define USE_CNF_CACHE 1
//#define USE_OPTIMAL_CACHE 1
//#define USE_DISK_CACHE 1
//#define STATELESS

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(CNFSession);

int RouterState::CACHE_SIZE = -1;
int RouterState::CONTENT_SIZE = -1;


template<> bool rw_config_var<SSFNET_STRING, CNFContentOwnerMap>(
		SSFNET_STRING& dst, CNFContentOwnerMap& src) {
	PRIME_STRING_STREAM s;
	s << "[";
	bool first = true;
	for (CNFContentOwnerMap::iterator i = src.begin(); i != src.end(); i++) {
		if (!first)
			s << ",";
		first = false;
		s << i->first << "," << i->second;
	}
	s << "]";
	dst.append(s.str().c_str());
	return false;
}

template<> bool rw_config_var<CNFContentOwnerMap, SSFNET_STRING>(
		CNFContentOwnerMap& dst, SSFNET_STRING& src) {
	if (src.length() <= 1) {
		LOG_ERROR("Invalid CNFContentOwnerMap "<<src<<endl)
	}
	int cid;
	unsigned long long uid;

	PRIME_STRING_STREAM s(src.c_str());

	if (s.get() != '[') {
		LOG_ERROR("Invalid CNFContentOwnerMap '"<<src<<"'"<<endl)
	}
	bool got_cid = false;
	while (s.good()) {
		switch (s.peek()) {
		case ']':
			return false;
		case ',':
			s.get();
		default:
			if (got_cid) {
				s >> uid;
				got_cid = false;
				dst.insert(SSFNET_MAKE_PAIR(cid,uid));
			} else {
				got_cid = true;
				s >> cid;
			}
		}
	}LOG_ERROR("Invalid CNFContentOwner"<<src<<endl)
	return true;
}

template<> bool rw_config_var<SSFNET_STRING, CNFContentMap>(SSFNET_STRING& dst,
		CNFContentMap& src) {
	PRIME_STRING_STREAM s;
	s << "[";
	bool first = true;
	for (CNFContentMap::iterator i = src.begin(); i != src.end(); i++) {
		if (!first)
			s << ",";
		s << i->first << "," << i->second;
		first = false;
	}
	s << "]";
	dst.append(s.str().c_str());
	return false;
}

template<> bool rw_config_var<CNFContentMap, SSFNET_STRING>(CNFContentMap& dst,
		SSFNET_STRING& src) {
	if (src.length() <= 1) {
		LOG_ERROR("Invalid CNFContentMap "<<src<<endl)
	}
	int cid;
	int size;
	PRIME_STRING_STREAM s(src.c_str());

	if (s.get() != '[') {
		LOG_ERROR("Invalid CNFContentMap '"<<src<<"'"<<endl)
	}
	bool got_cid = false;
	while (s.good()) {
		switch (s.peek()) {
		case ']':
			return false;
		case ',':
			s.get();
		default:
			if (got_cid) {
				s >> size;
				got_cid = false;
				dst.insert(SSFNET_MAKE_PAIR(cid,size));
			} else {
				got_cid = true;
				s >> cid;
			}
		}
	}LOG_ERROR("Invalid CNFContentMap"<<src<<endl)
	return true;
}

CNFApplication::CNFApplication() : cnf_trans(0) {
}

CNFApplication::~CNFApplication() {

}

void CNFApplication::init() {
	cnf_trans = getCNFTransport();

}

void CNFApplication::wrapup() {

}

void CNFApplication::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr,
		MACAddress mac) {
	LOG_DEBUG("starting CNF traffic "<<getUID()<<", at:"<<inHost()->getNow().second()<<", my IP="<<this->inHost()->getDefaultIP().toString()<< " dst IP="<<ipaddr<<", traffic type="<< evt->getTrafficType()<<endl);
	if (!evt->getTrafficType())
		LOG_ERROR("No traffic type in the start traffic event!"<<endl)
	switch (evt->getTrafficType()->getConfigType()->type_id) {
	case CNF_TRAFFIC: {
		Host* host = inHost();
		assert(host);
		UID_t dst_port = ((CNFStartTrafficEvent*) evt)->getDstPort();
		//get content id from the event
		int content_id = ((CNFStartTrafficEvent*) evt)->getContentID();
		CNFContentMap cid_size = unshared.cnf_content_sizes.read();
		if (cid_size.find(content_id) == cid_size.end()) {
			UID_t dst = cnf_trans->getControllerUID(); //For query packet, dst should be the uid of the controller.
			//create a CNF message and push it to CNFTransport, it will send the request to the controller.
			CNFMessage* cnfmsg = new CNFMessage(inHost()->getUID(), dst, dst_port, 0, content_id, 0);
			cnf_trans->push(cnfmsg, this);
		}
		else {
			//its my own content....
			LOG_WARN("Asked for my own content!\n");
		}
	}
		break;
	default:
		LOG_ERROR("Invalid traffic type...."<<endl);
		break;
	}
	evt->free();
}

int CNFApplication::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra) {
	LOG_ERROR(
			"ERROR: a message is pushed down to the CNFApplication session from protocol layer above; it's impossible\n");
	return 0;
}

int CNFApplication::pop(ProtocolMessage* msg, ProtocolSession* losess,
		void* extra) {
	assert(msg);
	// cnf message must be popped
	assert(losess);
	// there's lower layer
	assert(extra);
	// there's extra stuff
	SimpleSocket* sock = SSFNET_STATIC_CAST(SimpleSocket*,extra);
	DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);

	SocketState* state = cnf_trans->getCNFState(sock);
	if (state) {
		//it should be a response to a request, this is a src
		byte* data = dm->getRawData();
		if (data) { //the DM better have real data
			if (state->getTotalSize() == 0) {
				if (sizeof(CNFMessage) == dm->size()) {
					unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
					CNFMessage* cnf_data = (CNFMessage*) data;
					//update state -- check that the size is >0 (if its <=0 the final dst didn't have it....
					assert(cnf_data->getContentSize());
					state->setTotalSize(cnf_data->getContentSize());
				} else {
					LOG_ERROR("The size of CNF message is not correct!"<<endl);
				}
			} else {
				LOG_ERROR("The total size has been set to "<<state->getTotalSize()<<", this should not be a CNF message! "<<endl);
			}

		} else {
			//the DM better be virtual
			unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
			//update state with size of recv bytes
			state->setReceivedSize(state->getReceivedSize() + dm->size());
			//If finished remove socket from maps and close socket
			if (state->getReceivedSize() >= state->getTotalSize()) {
				cnf_trans->removeCNFState(sock);
				LOG_WARN("["<<inHost()->getUID()<<","<< inHost()->getNow()<< "]CNFDELAY: "<<(sock->getLastRecv()-sock->getFirstSend()).second()<<endl);
				//LOG_DEBUG("CNFDELAY: got "<<state->getReceivedSize()<<" expected "<< state->getTotalSize()<<"\n");
				sock->disconnect();
				DEBUG_CODE(
					if (state->getReceivedSize() > state->getTotalSize()) {
						LOG_WARN("CNFDELAY Got extra data....\n");
					}
				);
			}
		}
	} else {
		//it should be a request, this is a dst
		unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
		byte* data = dm->getRawData();
		if (data) {
			if (sizeof(CNFMessage) == dm->size()) {
				CNFMessage* cnf_data = (CNFMessage*) data;
				int cid = cnf_data->getContentID();
				int size = getContentSize(cid);
				if (size) {
					CNFMessage* response = new CNFMessage(inHost()->getUID(),
							cnf_data->getSrc(),
							cnf_data->getDstPort(),
							cnf_data->getCNFNode(),
							cid,
							size);
					sock->send(sizeof(CNFMessage), (byte*)response);
					sock->send(size);
					unshared.bytes_sent.write(unshared.bytes_sent.read() + size + sizeof(CNFMessage));
				}else{
					LOG_ERROR("The content with cid = "<< cid <<" is not stored in this node!"<<endl);
				}
			} else {
				LOG_ERROR("The size of CNF message is not correct!"<<endl);
			}
		} else {
			LOG_ERROR("This is a request, should be a CNF message! "<<endl);
		}
	}
	return 0;
}

CNFTransport* CNFApplication::getCNFTransport() {
	if (!cnf_trans) {
		cnf_trans = (CNFTransport*) inHost()->sessionForNumber(
				SSFNET_PROTOCOL_TYPE_CNF_TRANS);
		if (!cnf_trans)
			LOG_ERROR("missing CNFTransport session.");
	}
	return cnf_trans;
}

int CNFApplication::getContentSize(int cid) {
#if 0
	int size = 0;
	CNFContentMap cid_size = unshared.cnf_content_sizes.read();
	CNFContentMap::iterator i = cid_size.find(cid);
	if (i != cid_size.end()) {
		size = i->second;
		assert(size);
	} else {
		LOG_DEBUG("Can't find size information for cid "<<cid<<endl);
	}
	return size;
#endif
	//XXX hack
#ifdef USE_TCP
	return 1400*100;
#else
	return 1000000;
#endif
}

//CNFTransport::CacheDecisionMap* CNFTransport::optimal_cache_probs=0;
#include <sys/time.h>


CNFTransport* CNFTransport::master_controller=0; //XXX hack for opt

void CNFTransport::initOptimalCacheProbs() {
	//XXX comment to get reproducable results...
	srand(time(NULL));
#ifdef USE_OPTIMAL_CACHE //turn this to 0 to disable optimal init
	if(!master_controller) {
		master_controller=this;
		//if(optimal_cache_probs) return;
		optimal_cache_probs = new CNFTransport::CacheDecisionMap();
		//read int hard coded file...
		FILE* cnf_data_file;
		int cid;
		float prob;
		UID_t router_uid;
		char* t = getenv("DENSITY");
		if(t==0 || strlen(t)==0) {
			LOG_ERROR("must set env 'DENSITY' !\n")
		}
		char temp[strlen(t)+20];
		sprintf(temp,"cnf_data_%s",t);
		LOG_WARN("Opening optimal data file "<<SSFNET_STRING(temp)<<endl);
		cnf_data_file = fopen(temp,"r");
		if(!cnf_data_file) {
			LOG_ERROR("The cnf_data_file cannot be opened properly."<<endl);
		}
		while (fscanf(cnf_data_file, "%d %f %llu", &cid, &prob, &router_uid) != EOF) {
			//LOG_DEBUG("*******[cid:"<<cid<<" , prob:"<<prob<<", router_uid:"<<router_uid<<"]"<<endl);
			//if(std::find(cnf_router_uid.begin(), cnf_router_uid.end(), router_uid)!=cnf_router_uid.end()){
				//LOG_DEBUG("Found cnf router for this controller"<<endl);
				int cid_orig=cid; //HACK XXX
				prob = prob/5.0; //HACK XXX
				for(int q=0;q<5;q++) {
					cid=cid_orig+q*180;
					CacheDecisionMap::iterator it = optimal_cache_probs->find(cid);
					int sample_num = (int)(100000*prob)+1;
					if(it!= optimal_cache_probs->end()){
						for(int i=0; i<sample_num; i++){
							((*it)).second->push_back(router_uid);
						}
					}else{
						UIDVec* router_vec = new UIDVec();
						for(int i=0; i<sample_num; i++){
							router_vec->push_back(router_uid);
						}
						optimal_cache_probs->insert(SSFNET_MAKE_PAIR(cid, router_vec));
					}
				}//HACK XXX
			//}
		}
		/*for(int i=0; i<cnf_router_uid.size();i++){
			LOG_DEBUG("cnf router :"<<cnf_router_uid[i]<<endl);
		}*/

		/*for(CacheDecisionMap::iterator iter= optimal_cache_probs->begin();iter!=optimal_cache_probs->end();iter++){
			std::cout<<"cid="<<(*iter).first<<", vec size="<<(*iter).second->size()<<std::endl;
		}*/
		fclose(cnf_data_file);
		cnf_data_file = NULL;
		if(optimal_cache_probs->size()==0){
			LOG_WARN("no cnf router for this controller, controller uid is "<<getControllerUID()<<endl);
			no_cnf_router=true;
		}
	}
#endif
}

PRIME_OFSTREAM* CNFTransport::hit_rate=0;

CNFTransport::CNFTransport() :
		ip_sess(0), tcp_master(0), cnf_app(0), cid_owner(0), no_cnf_router(false) {
	if(RouterState::CACHE_SIZE == -1) {
		char* t = getenv("STORAGE_SIZE");
		if(t==0 || strlen(t)==0) {
			LOG_ERROR("must set env 'STORAGE_SIZE' !\n")
		}
		RouterState::CONTENT_SIZE = 25;
		RouterState::CACHE_SIZE=atoll(t)*RouterState::CONTENT_SIZE;
		LOG_WARN("got CACHE_SIZE as " <<RouterState::CACHE_SIZE<<" and CONTENT_SIZE as "<<RouterState::CONTENT_SIZE<<endl);
	}
	if(!hit_rate) {
		hit_rate = new PRIME_OFSTREAM();
		hit_rate->open("hit_rate");
	}
}

CNFTransport::~CNFTransport() {

}

void CNFTransport::init() {
	//cache cnf app
	cnf_app = (CNFApplication*) inHost()->sessionForNumber(
			SSFNET_PROTOCOL_TYPE_CNF_APP);
	if (!cnf_app)
			LOG_ERROR("missing CNFApplication session.");
#ifdef USE_TCP
	tcp_master = (TCPMaster*) inHost()->sessionForNumber(
			SSFNET_PROTOCOL_TYPE_TCP);
	if (!tcp_master)
			LOG_ERROR("missing CNFApplication session.");
#else
	udp_master = (UDPMaster*)inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_UDP);
	if(!udp_master){
		LOG_ERROR("missing UDP session" << endl);
	}
#endif

	//Get controller uid for my network
	Net* owning_net = inHost()->inNet();
	UID_t rid = owning_net->getControllerRid();
	while (rid == 0 && owning_net != NULL) {
		owning_net = owning_net->getSuperNet();
		rid = owning_net->getControllerRid();
	}
	controller_uid = rid + owning_net->getUID()-owning_net->getSize();

	//if this is a controller, initialize cnf node and cached size map for its owning net
	if (inHost()->getUID() == getControllerUID()) {
		//Get <content id, owner_uid> map
		Net* topnet = inHost()->getCommunity()->getPartition()->getTopnet();
		cid_owner = topnet->getCNFContentOwnerMap();

		UIDVec cnf_routers = shared.cnf_routers.read();

		if (cnf_routers.size()!=0) {
			for (UIDVec::iterator cr = cnf_routers.begin(); cr != cnf_routers.end(); cr++) {
				//initialize cnf router list
				UID_t router_uid = (*cr) + owning_net->getUID() - owning_net->getSize();
				cnf_router_uid.push_back(router_uid);
				//initialize <rid, routerstate> map
				RouterState* rs = new RouterState(router_uid);
				rid_router.insert(SSFNET_MAKE_PAIR(router_uid, rs));
			}
		} else {
			no_cnf_router = true;
		}

		initOptimalCacheProbs();
#ifdef USE_OPTIMAL_CACHE
		if(this!=master_controller) {
			for(UidRouterStateMap::iterator foobar = rid_router.begin(); foobar!= rid_router.end();foobar++) {
				master_controller->rid_router.insert(SSFNET_MAKE_PAIR(foobar->first,foobar->second));
			}
		}
#endif

#ifdef USE_DISK_CACHE
		RoutingSphere* rs = owning_net->getRoutingSphere();
		SSFNET_VECTOR(UID_t) uids;
		if(owning_net == topnet) {
			//in topnet --- need to loop over hosts...
			ChildIterator<Host*> h = topnet->hosts();
			while(h.hasMoreElements()) {
				Host* hh =(Host*)h.nextElement();
				if(!Router::getClassConfigType()->isSubtype(hh->getConfigType())) {
					//In route entry, dest is interface, not host
					//So we push back interface uid to the list
					uids.push_back(hh->getInterfaceByIP(hh->getDefaultIP())->getUID());
				}
			}
		}
		else if(rs) {
			//in sub-sphere
			EdgeInterfaceListWrapper elw = rs->getEdgeInterfaces();
			for(EdgeList::iterator it = elw.begin(); it != elw.end();it++) {
				uids.push_back((*it).getLocalRank());
			}
		}
		else {
			LOG_ERROR("how did this happen?\n only support controllers in topnet or a routing sphere (not sub-nets of a RS).");
		}
		LOG_DEBUG("CALC AVG"<<endl);
		int total=0, count=0;
		//cal distance between all uids
		for(int i=0;i<(int)uids.size();i++) {
			for (int j = i + 1; j < (int)uids.size(); j++) {
				LOG_DEBUG("i["<<uids[i]<<"]="<<i<<", j["<<uids[j]<<"]="<<j<<", dist="<<rs->calcDist(uids[i], uids[j])<<endl);
				count++;
				total += rs->calcDist(uids[i], uids[j]);
			}
		}
		for(int i=0;i<(int)uids.size();i++) {
			for (int k =0; k<(int)cnf_routers.size(); k++) {
				LOG_DEBUG("i["<<uids[i]<<"]="<<i<<", k["<<cnf_routers[k]<<"]="<<k<<", dist="<<rs->calcDist(uids[i], cnf_routers[k])<<endl);
				count++;
				total += rs->calcDist(uids[i], cnf_routers[k]);
			}
		}
		int avg = (int) (ceil(((double) total) / ((double) count))/2);

		//cal the position of each cnf router in the network (disk)
		int dist=0;
		LOG_DEBUG("AVG DIST="<<avg<<endl);
		if (cnf_routers.size()!=0) {
			for (int k =0; k<(int)cnf_routers.size(); k++) {
				UID_t cr = cnf_routers[k];
				//cal the distance from each cnf router to all the edge interfaces
				for(int i=0;i<(int)uids.size();i++) {
					dist += (int)(pow((rs->calcDist(cr, uids[i])-avg),2));
				}
				DistCacheMap::iterator iter = dist_router.find(dist);
				if(iter!=dist_router.end()){
					iter->second->push_back(cnf_router_uid[k]);
				}else{
					UIDVec* router=new UIDVec();
					router->push_back(cnf_router_uid[k]);
					dist_router.insert(SSFNET_MAKE_PAIR(dist, router));
					LOG_DEBUG("INSERT DIST, dist="<<dist<<", router uid="<<cnf_router_uid[k]<<endl);
				}
				dist=0;
			}
		} else {
			no_cnf_router=true;
		}
		/*for(DistCacheMap::iterator iter = dist_router.begin(); iter!=dist_router.end();iter++){
			LOG_DEBUG("The dist is "<<(*iter).first);
			LOG_DEBUG("The size of the routers is "<<iter->second->size()<<endl);
		}*/
#endif
	}
	//create listener
#ifdef USE_TCP
	new SimpleSocket(tcp_master, this, CNF_PORT);
#else
	new SimpleSocket(udp_master, this, CNF_PORT);
#endif
}

void CNFTransport::wrapup() {

}

void CNFTransport::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr,
		MACAddress mac) {
	LOG_ERROR("startTraffic has never been called by probe session."<<endl);
}

int CNFTransport::push(ProtocolMessage* msg, ProtocolSession* hisess,
		void* extra) {
	//being called by the CNFApplication on src to send query to the controller
	IPAddress ipaddr;
	CNFMessage* cnfmsg = SSFNET_STATIC_CAST(CNFMessage*,msg);
	UID_t controller_uid = cnfmsg->getDst();
	inHost()->getCommunity()->synchronousNameResolution(controller_uid, ipaddr);
	if (IPAddress::IPADDR_INVALID == ipaddr) {
		LOG_ERROR("The controller with uid="<<controller_uid<<" does not exist in this network!"<<endl)
	} else {
		UID_t dst_port = cnfmsg->getDstPort();
		// Create a new socket to send query to the controller
		LOG_DEBUG("CNF create new sock"<<endl)
#ifdef USE_TCP
		SimpleSocket* sock = new SimpleSocket(tcp_master, this, ipaddr, dst_port);
#else
		SimpleSocket* sock = new SimpleSocket(udp_master, this, ipaddr, dst_port);
#endif

		sock->send(sizeof(CNFMessage), (byte*) cnfmsg);

		//insert the entry for the new created sock to <sock state> map
		SocketState* state = new SocketState(cnfmsg->getContentID(), 0, 0, SocketState::SRC_CONTROLLER);
		SSFNET_PAIR(SocketStateMap::iterator,bool) ret  =
				sock_state.insert(SSFNET_MAKE_PAIR(sock, state));
		if(!(ret.second)) {
			LOG_ERROR("Already had a session with addr "<<((void*)sock)<<endl);
		}
	}
	return 0;
}

int CNFTransport::pop(ProtocolMessage* msg, ProtocolSession* losess,void* extra) {
	assert(losess);
	//LOG_WARN("in" <<getUID()<<"->CNFTransport::pop"<<endl);
	// there's lower layer
	assert(extra);
	// there's extra stuff
	IPAddress ipaddr;
	UID_t my_uid = inHost()->getUID();
	UID_t controller = getControllerUID();

	SimpleSocket* sock = SSFNET_STATIC_CAST(SimpleSocket*,extra);
	uint32 status = sock->getStatus();
	if (status & SimpleSocket::SOCKET_CONNECTED) {
		// This event is used by this server to start a new socket so that it always keep listening
		LOG_DEBUG("in CNFTransport::pop SOCKET_BUSY" << endl);
		// Another passive socket has been created
#ifdef USE_TCP
		new SimpleSocket(tcp_master, this, CNF_PORT);
#else
		new SimpleSocket(udp_master, this, CNF_PORT);
#endif
		return 0;
	}
	if ((status & SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED)
								|| (status & SimpleSocket::SOCKET_PSH_FLAG)) {
		assert(msg);
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		LOG_DEBUG("dm size = "<<dm->size()<<endl);
		//check socket state
		SocketState* state = getCNFState(sock);
		if (state) { //response, DM could be CNF message or data
			LOG_DEBUG("This is response." << endl);
			//check the type of the socket
			SocketState::TYPE type = state->getType();
			byte* data = dm->getRawData();
			if (data) { //the DM better have real data, CNF message
				if (state->getTotalSize() == 0) {
					if (sizeof(CNFMessage) == dm->size()) {
						CNFMessage* cnfmsg = (CNFMessage*) data;
						UID_t dst = cnfmsg->getDst();
						UID_t dst_port = cnfmsg->getDstPort();
						UID_t cnf_router = cnfmsg->getCNFNode();
						int evict = cnfmsg->getEvict();
						int cid = cnfmsg->getContentID();
						int content_size = cnfmsg->getContentSize();
						unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
						if (type == SocketState::SRC_CONTROLLER) { //this is a response from controller to src
							LOG_DEBUG("This is response from controller to src." << endl);
							// remove this socket from the socket state map
							removeCNFState(sock);
							sock->disconnect();

							// Create a new socket to send query to the cnf router
							inHost()->getCommunity()->synchronousNameResolution(cnf_router, ipaddr);

							if (IPAddress::IPADDR_INVALID == ipaddr) {
								LOG_ERROR("The cnf router with uid=" << cnf_router << " does not exist in this network!"<<endl)
							} else {
#ifdef USE_TCP
								SimpleSocket* new_sock = new SimpleSocket(tcp_master, this, ipaddr, dst_port);
#else
								SimpleSocket* new_sock = new SimpleSocket(udp_master, this, ipaddr, dst_port);
#endif

								CNFMessage* request_to_router = new CNFMessage(
														my_uid,
														dst,
														dst_port,
														cnf_router,
														cid,
														content_size,
														evict);
								new_sock->send(sizeof(CNFMessage), (byte*) request_to_router);
								//insert the entry for the new created sock to <sock state> map
#ifndef USE_CNF_CACHE
								SocketState* state = new SocketState(cid, 0, 0, SocketState::SRC_CNFROUTER);
#else
								SocketState* state = new SocketState(cid, 0, 0, SocketState::SRC_CNFROUTER);
#endif
								SSFNET_PAIR(SocketStateMap::iterator,bool) ret  =
										sock_state.insert(SSFNET_MAKE_PAIR(new_sock, state));
								if(!(ret.second)) {
									LOG_ERROR("Already had a session with addr "<<((void*)sock)<<endl);
								}
							}
						} else if (type == SocketState::SRC_CNFROUTER) { //this is a response from cnf router to src
							LOG_DEBUG("This is response from cnf router to src." << endl);
							//pop this cnf message to app
							cnf_app->pop(msg, this, extra);
						} else if (type == SocketState::CNFROUTER_DST) { //this is a response from dst to cnf router
							LOG_DEBUG("This is response from dst to cnf router." << endl);
							//send cnf message to all the listeners
							SocketStateMap::iterator ss = sock_state.find(sock);
							if (ss != sock_state.end()) {
								SocketState* state = (*ss).second;
								assert(!state->getTotalSize());
								state->setTotalSize(cnfmsg->getContentSize());
								SocketState::ListenerSocketList* listeners = state->getSocketList();

								CNFMessage* response_to_src = new CNFMessage(my_uid,
										dst, dst_port, cnf_router, cid, content_size);

								for (SocketState::ListenerSocketList::iterator ls =
										listeners->begin(); ls != listeners->end();
										ls++) {
									(*ls)->send(sizeof(CNFMessage),(byte*) response_to_src);
								}
							} else {
								LOG_ERROR(
										"There is no listeners for this content with cid = "<< endl);
							}
						} else {
							LOG_ERROR(
									"The type of the socket can not be recognized!"<<endl);
						}
					} else {
						LOG_ERROR("The size of CNF message is not correct! sizeof(CNFMessage)["<<
								sizeof(CNFMessage)<<"] != dm->size()["
								<<dm->size()<<"]"<<endl);
					}
				} else {
					CNFMessage* cnfmsg = (CNFMessage*) data;
					UID_t dst = cnfmsg->getDst();
					UID_t dst_port = cnfmsg->getDstPort();
					UID_t cnf_router = cnfmsg->getCNFNode();
					int cid = cnfmsg->getContentID();
					int content_size = cnfmsg->getContentSize();

					LOG_ERROR(
							"The total size has been set, this should not be a CNF message!"
							<<" state->getTotalSize()= "<<state->getTotalSize()
							<<", state->getContentId()= "<<state->getContentId()
							<<", state->getReceivedSize()= "<<state->getReceivedSize()
							<<", state->state->getSocketList()->size()= "<<state->getSocketList()->size()
							<<", state->getType()= "<<state->getType()
							<<", response: dst =" <<dst
							<<", dst_port =" <<dst_port
							<<", cnf_router =" <<cnf_router
							<<", cid =" <<cid
							<<", content_size =" <<content_size
							<<endl);
				}
			} else { //the DM better be virtual data
				unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
				if (type == SocketState::SRC_CNFROUTER) { //this is response from cnf router to src
					LOG_DEBUG("This is response with virtual data from cnf router to src." << endl);
					//pop this cnf message to app
					cnf_app->pop(msg, this, extra);
				} else if (type == SocketState::CNFROUTER_DST) { //this is a response from dst to cnf router
					LOG_DEBUG("This is response with virtual data from dst to cnf router." << endl);
					//send virtual to all the listeners
					SocketStateMap::iterator ss = sock_state.find(sock);
					LOG_DEBUG("sock session id="<<sock->getSessionId()<<" sock="<<((void*)sock)<<endl);
					if (ss != sock_state.end()) {
						SocketState* state = (*ss).second;
						SocketState::ListenerSocketList* listeners = state->getSocketList();
						if(listeners->empty())
							LOG_DEBUG("There is no listener for sock "<<sock->getSessionId()
									<<", type="<<state->getType()<<" ."<<endl)
						for (SocketState::ListenerSocketList::iterator ls =
								listeners->begin(); ls != listeners->end(); ls++) {
							LOG_DEBUG("Send size = "<<dm->size()<<" from cnf router to src."<<endl);
							LOG_DEBUG("The socket type is "<<state->getType()<<" ."<<endl)
							(*ls)->send(dm->size());
						}
						//update state with size of recv bytes
						state->setReceivedSize(state->getReceivedSize() + dm->size());

						if (state->getReceivedSize() == state->getTotalSize()) {
							//the content is received, insert entry to cid_size map
							cid_size.insert(SSFNET_MAKE_PAIR(state->getContentId(), state->getTotalSize()));
							removeCNFState(sock);
							//remove it from cid_state map
							ContentIDStateMap::iterator cs = cid_state.find(state->getContentId());
							if (cs != cid_state.end())
								cid_state.erase(cs);
							sock->disconnect();
						}
						//else it is still in progress
					} else {
						LOG_ERROR("The socket is not found in the sock_map for this response! "<< endl);
					}
				} else {
					LOG_ERROR("The type of the socket is not correct!"<<endl);
				}
			}
		} else {
			//request, DM must be CNF message
			byte* data = dm->getRawData();
			if(dm->size()==0){
				return 0;
			}
			if (data) { //the DM better have real data, CNF message
				if (sizeof(CNFMessage) == dm->size()) {
					CNFMessage* cnfmsg = (CNFMessage*) data;
					UID_t dst = cnfmsg->getDst();
					UID_t dst_port = cnfmsg->getDstPort();
					UID_t cnf_router = cnfmsg->getCNFNode();
					int evicted=0;
					int cid = cnfmsg->getContentID();
					int content_size = cnfmsg->getContentSize();
					unshared.bytes_received.write(unshared.bytes_received.read() + dm->size());
					unshared.requests_received.write(unshared.requests_received.read() + 1);
					if (my_uid == controller) { //this is a request from src to controller
#ifdef USE_OPTIMAL_CACHE
	if(this != master_controller) {
		return master_controller->pop(msg,losess, extra);
	}
#endif
#ifndef USE_CNF_CACHE
						dst = getFinalDst(cid);
						CNFMessage* response = new CNFMessage(my_uid,
									dst,
									dst_port,
									dst,
									cid,
									content_size,
									evicted);
						sock->send(sizeof(CNFMessage), (byte*) response);
						unshared.bytes_sent.write(unshared.bytes_sent.read() + sizeof(CNFMessage));
						return 0;
#endif
						LOG_DEBUG("This is request from src to controller." << endl);
						//send response CNFMessage to src
						//check <cid,router> map
						CidRouterStateMap::iterator cr = cid_router.find(cid);
						if (cr != cid_router.end()){
							//this content has been cached, popularity++
							for(RouterStateList::iterator rs=(*cr).second->begin(); rs!=(*cr).second->end(); rs++){
								(*rs)->updateCidPopularity(cid);
							}
							if(!pickNewRouter()){
								//randomly choose a router in the list in turn
								cnf_router = (*((*cr).second)->begin())->getRid();
								((*cr).second)->push_back(*((*cr).second)->begin());
								((*cr).second)->pop_front();
							}else{
								//choose a new router
								cnf_router = getCNFNode(cid, &evicted);
								if(cnf_router!=0){
									//get router state by <rid, router> map
									UidRouterStateMap::iterator ur = rid_router.find(cnf_router);
									if (ur != rid_router.end()){
										//update router state
										(*ur).second->updateCidPopularity(cid);
										//if the cached size on this router is equal to the total cache size,
										//remove this router from cnf_router_uid vector
										if((*ur).second->isFull()){
											for (int i=0; i<(int)cnf_router_uid.size(); i++){
												if(cnf_router_uid[i]==cnf_router){
													cnf_router_uid.erase(cnf_router_uid.begin()+i);
												}
											}
	#ifdef USE_DISK_CACHE
											if(!dist_router.empty()) {
												UIDVec* routers = (*dist_router.begin()).second;
												for(int q = 0;q<(int)routers->size();q++) {
													if((*routers)[q] == cnf_router) {
														routers->erase(routers->begin()+q);
														break;
													}
												}
												if(routers->size()==0) {
													dist_router.erase(dist_router.begin());
												}
											}
	#endif
										}
										//update <cid, RouterStateList> map
										((*cr).second)->push_back((*ur).second);
									}else{
										LOG_ERROR("The router state of router's uid "<< cnf_router
												<< " cannot be found in the rid_router map!"<<endl);
									}
								}else cnf_router=getFinalDst(cid);
							}
						}else{
							//this content has not been cached in any cnf router
							cnf_router = getCNFNode(cid, &evicted);
							if(cnf_router!=0){
								//get router state by <rid, router> map
								UidRouterStateMap::iterator ur = rid_router.find(cnf_router);
								if (ur != rid_router.end()){
									//update router state
									(*ur).second->updateCidPopularity(cid);
									//if the cached size on this router is equal to the total cache size,
									//remove this router from cnf_router_uid vector
									if((*ur).second->isFull()){
										for (int i=0; i<(int)cnf_router_uid.size(); i++){
											if(cnf_router_uid[i]==cnf_router){
												cnf_router_uid.erase(
														cnf_router_uid.begin() + i);
											}
										}
	#ifdef USE_DISK_CACHE
										if(!dist_router.empty()) {
											UIDVec* routers = (*dist_router.begin()).second;
											for(int q = 0;q<(int)routers->size();q++) {
												if((*routers)[q] == cnf_router) {
													routers->erase(routers->begin()+q);
													break;
												}
											}
											if(routers->size()==0) {
												dist_router.erase(dist_router.begin());
											}
										}
	#endif
									}
									//update <cid, RouterStateList> map
									RouterStateList* rs_list =
											new RouterStateList();
									rs_list->push_back((*ur).second);
									cid_router.insert(SSFNET_MAKE_PAIR(cid, rs_list));
								}else{
									LOG_ERROR("The router state of router's uid "<< cnf_router
											<< " cannot be found in the rid_router map!"<<endl);
								}
							}else cnf_router=getFinalDst(cid);
						}
						dst = getFinalDst(cid);
						if (cnf_router && dst) {
							CNFMessage* response = new CNFMessage(my_uid,
										dst,
										dst_port,
										cnf_router,
										cid,
										content_size,
										evicted);
							sock->send(sizeof(CNFMessage), (byte*) response);
							unshared.bytes_sent.write(unshared.bytes_sent.read() + sizeof(CNFMessage));
							LOG_DEBUG("my_uid="<<my_uid<<" send response for cid "<<cid<<endl);
						} else {
							LOG_ERROR("No cnf node is chosen or no dst owns the content! "<<endl)
						}
					} else if (my_uid == cnf_router && my_uid != dst) { //this is a request from src to cnf router
						//send a cnf message back to the src
						LOG_DEBUG("This is request from src to cnf router." << endl);
						//check if it has the content in the cache
						CidSizeMap::iterator cs = cid_size.find(cid);

						//check if there were evictions
						if(cnfmsg->getEvict()>0) {
							if(hit_rate)
								(*hit_rate)<<"["<<my_uid<<", "<<cid<<", -1],"<<endl;
							//we ignore the case where the victim is in progress.....
							cid_size.erase(cnfmsg->getEvict());
						}

						//if yes, send the cnf message and the content back to all the listeners
						if (cs != cid_size.end()) { //the router has all the content, send the content to the src
							//hit
							if(hit_rate)
								(*hit_rate)<<"["<<my_uid<<", "<<cid<<", 1],"<<endl;
							CNFMessage* response = new CNFMessage(my_uid, dst,
															dst_port, cnf_router, cid,
															(*cs).second);
							sock->send(sizeof(CNFMessage), (byte*) response);
							sock->send((*cs).second);
							unshared.bytes_sent.write(unshared.bytes_sent.read()+ sizeof(CNFMessage) + (*cs).second);
						} else {
							ContentIDStateMap::iterator c = cid_state.find(cid);
							if (c != cid_state.end()) { //the content is partially in the cache
								//hit
								if(hit_rate)
									(*hit_rate)<<"["<<my_uid<<", "<<cid<<", 1],"<<endl;
								((*c).second)->getSocketList()->push_back(sock);
								LOG_DEBUG("push sock "<<sock->getSessionId()<<" to socket list in state for sock type "<<((*c).second)->getType()<<endl);
								int total_size = ((*c).second)->getTotalSize();
								int received_size = ((*c).second)->getReceivedSize();
								if(total_size>0) {
									//we know how big it is, so lets send the response
									CNFMessage* response = new CNFMessage(my_uid, dst,
																	dst_port, cnf_router, cid,
																	total_size);
									sock->send(sizeof(CNFMessage), (byte*) response);
									if(received_size>0) {
										//send the received_size to the src
										sock->send(received_size);
										unshared.bytes_sent.write(
														unshared.bytes_sent.read()
														+ sizeof(CNFMessage)
														+ received_size);
										//add this sock to the listener list
									}// else we haven't see any data yet
								}//else -- we are waiting from the response from the content owner
							} else { // The content is not in this router, create a new dst_sock to forward the request to dst
								//miss
								if(hit_rate)
									(*hit_rate)<<"["<<my_uid<<", "<<cid<<", 0],"<<endl;
								LOG_DEBUG("The content is not in this router."<<endl);
								inHost()->getCommunity()->synchronousNameResolution(dst, ipaddr);
								if (IPAddress::IPADDR_INVALID == ipaddr) {
									LOG_ERROR("The dst with uid="<<dst<<" does not exist in this network! --- we dont work in distributed yet...."<<endl)
								} else {
#ifdef USE_TCP
									SimpleSocket* new_sock = new SimpleSocket(
											tcp_master, this, ipaddr, dst_port);
#else
									SimpleSocket* new_sock = new SimpleSocket(
											udp_master, this, ipaddr, dst_port);
#endif
									CNFMessage* request = new CNFMessage(
											my_uid, dst, dst_port, cnf_router,
											cid, 0);
									new_sock->send(sizeof(CNFMessage),(byte*) request);
									SocketState* state = new SocketState(cid, 0, 0, SocketState::CNFROUTER_DST);
									state->getSocketList()->push_back(sock);
									LOG_DEBUG("new sock id="<<new_sock->getSessionId()<<endl);
									LOG_DEBUG("push sock "<<sock->getSessionId()<<" to socket list in state for sock type "<<state->getType()<<endl);
									{
										SSFNET_PAIR(SocketStateMap::iterator,bool) ret  =
												sock_state.insert(SSFNET_MAKE_PAIR(new_sock, state));
										if(!(ret.second)) {
											LOG_ERROR("Already had a session addr "<<((void*)sock)<<endl);
										}
									}
									{
										SSFNET_PAIR(ContentIDStateMap::iterator,bool) ret  =
												cid_state.insert(SSFNET_MAKE_PAIR(state->getContentId(), state));
										if(!(ret.second)) {
											LOG_ERROR("Already had a cid entry for cid "<<state->getContentId()<<endl);
										}
									}
								}
							}
						}
					} else if (my_uid == dst) { //this is a request from cnf router to dst
						LOG_DEBUG("This is request from cnf router to dst." << endl);
						//pop this cnf message to app
						cnf_app->pop(msg, this, extra);
					} else {
						LOG_ERROR("This should never happen!"<<endl);
					}
				} else {
					LOG_ERROR("The size of CNF message is not correct! "<<endl);
				}
			}else {
				LOG_ERROR( "This should be a CNF message, the DM must be real data! "<<endl);
			}
		}
	}
	return 0;
}


UID_t CNFTransport::getCNFNode(int cid, int* evicted_cid) {
	assert(evicted_cid);
	UID_t cnf_node = 0;
	if(no_cnf_router) {
#ifdef USE_OPTIMAL_CACHE

#else
		return cnf_node;
#endif
	}
	if (!cnf_router_uid.empty()) {
#ifndef USE_OPTIMAL_CACHE
#ifndef USE_DISK_CACHE
		cnf_node = cnf_router_uid[rand() % cnf_router_uid.size()];
#endif
#endif
#ifdef USE_OPTIMAL_CACHE
		CacheDecisionMap::iterator it=optimal_cache_probs->find(cid);
		if(it==optimal_cache_probs->end()){
			LOG_WARN("Cannot find the content id in cache decision map!"<<endl)
		}else{
			UIDVec* router_vec=(*it).second;
			cnf_node = router_vec->at(rand()%router_vec->size());
		}
#endif

#ifdef USE_DISK_CACHE
		{
			if(dist_router.empty()){
				LOG_ERROR("the router which was in the not full list was in fact full.... wtf?"<<endl)
			}

			float popularity =CNFTraffic::calPopularity(cid, 3600);
			int choose_from = cnf_router_uid.size()*(1-popularity);
			if(choose_from<1) {
				UIDVec* routers = (*dist_router.begin()).second;
				cnf_node = routers->at(rand()%routers->size());
			}
			else {
				UID_t temp[choose_from];
				int j=0;
				for(DistCacheMap::iterator it = dist_router.begin(); it != dist_router.end(); it++) {
					if(j>=choose_from)break;
					UIDVec* routers = (*it).second;
					for(UIDVec::iterator it1 = routers->begin();it1!=routers->end();it1++) {
						if(j>=choose_from)break;
						temp[j]=*it1;
						j++;
					}
				}
				cnf_node=temp[rand()%choose_from];
			}

		}
#endif
	} else { //all cnf routers are full, need to evict content with least popularity
		LOG_DEBUG("All cnf routers are full, need to evict content."<<endl);
		//Go through <rid, router_state> map, find the cid with list popularity
		int least_popularity = 0xFFFFFF;
		RouterState::CidPopularityMap::iterator cid_pop;
		for (UidRouterStateMap::iterator it = rid_router.begin();
				it != rid_router.end(); it++) {
			RouterState* rs = (*it).second;
			int popularity = (*(rs->getLeastPopularity())).second;
			if (popularity == 1) {
				least_popularity = popularity;
				cnf_node = (*it).first;
				cid_pop = rs->getLeastPopularity();
				break;
			} else if (least_popularity > popularity){
				least_popularity = popularity;
				cnf_node = (*it).first;
				cid_pop = rs->getLeastPopularity();
			}
		}
		(*evicted_cid)=(*cid_pop).first;

		//update <rid, router_state>
		UidRouterStateMap::iterator it = rid_router.find(cnf_node);
		if(it!=rid_router.end()){
			(*it).second->cid_popularity.erase((*cid_pop).first);
		}
		//update <cid, router_state_list>
		CidRouterStateMap::iterator cr = cid_router.find((*cid_pop).first);
		if(cr!=cid_router.end()){
			if(((*cr).second)->size()>1){
				RouterState* to_del= 0;
				//remove route state with rid == cnf_router
				for (RouterStateList::iterator r = ((*cr).second)->begin(); r != ((*cr).second)->end(); r++){
					LOG_DEBUG("*r is "<<*r<<endl);
					LOG_DEBUG("rid="<<(*r)->getRid()<<", cnf_node="<<cnf_node<<endl);
					if((*r)->getRid()==cnf_node){
						to_del=*r;
						break;
					}
				}
				if(to_del) {
					cr->second->remove(to_del);
				}
				else{
					LOG_ERROR("wtf"<<endl);
				}
			}else{
				cid_router.erase(cr);
			}
		}
	}
	return cnf_node;
}

UID_t CNFTransport::getFinalDst(int cid) {
	//XXX, HACK, pretend we have more contents
	int mapped_cid = 1+(cid-1)%180;
	CNFContentOwnerMap::iterator cc = cid_owner->find(mapped_cid);
	//CNFContentOwnerMap::iterator cc = cid_owner->find(cid);
	if (cc != cid_owner->end()) {
		return (*cc).second;
	} else {
		return 0;
	}
}

SimpleSocket* CNFTransport::sendRequest(CNFMessage* cnfmsg, IPAddress ipaddr) {
	// Get the destination port to connect to
	UID_t dst_port = cnfmsg->getDstPort();
	// Create a new socket with the connection data which will later create a new TCP session
#ifdef USE_TCP
	SimpleSocket* sock = new SimpleSocket(tcp_master, this, ipaddr,
			dst_port);
#else
	SimpleSocket* sock = new SimpleSocket(udp_master, this, ipaddr,
			dst_port);
#endif
	sock->send(sizeof(CNFMessage), (byte*) cnfmsg);
	return sock;
}

CNFContentOwnerMap* CNFTransport::getCNFContentOwnerMap() {
	return cid_owner;
}

UID_t CNFTransport::getControllerUID() {
	return controller_uid;
}

SocketState* CNFTransport::getCNFState(SimpleSocket* sock) {
	SocketStateMap::iterator cl = sock_state.find(sock);
	if (cl != sock_state.end()) {
		return (*cl).second;
	} else {
		return NULL;
	}
}

void CNFTransport::removeCNFState(SimpleSocket* sock) {
	SocketStateMap::iterator ss = sock_state.find(sock);
	if (ss != sock_state.end())
		sock_state.erase(ss);
}

SSFNET_REGISTER_APPLICATION_SERVER(CNF_PORT,CNFTransport);

bool CNFTransport::pickNewRouter() {
#ifdef STATELESS
	return true;
#else
	return false;
#endif
}

}
;
// namespace ssfnet
}
;
// namespace prime
