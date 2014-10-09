/**
 * \file swing_tcp_traffic.cc
 * \brief Source file for the SwingTCPTraffic class.
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
#include<string.h>
#include "os/logger.h"
#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "proto/tcp/swing_tcp_traffic/swing_tcp_traffic.h"
#include "os/protocol_session.h"
#include "os/community.h"
#include "os/partition.h"

namespace prime {
namespace ssfnet {
#define LINK_PARAM 3
LOGGING_COMPONENT(SwingTCPTraffic)

SwingStartTrafficEvent::SwingStartTrafficEvent():
		StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_SWING_START_EVT), session_id(1), rre_id(1), req_size(0), rsp_size(0),
        dst_port(0), flow_type(-1)  {
}
SwingStartTrafficEvent::SwingStartTrafficEvent(UID_t traffic_type_uid, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_SWING_START_EVT, traffic_type_uid, id, h_uid, d_uid, d_ip, st), session_id(1), rre_id(1), req_size(0), rsp_size(0),
        dst_port(0), flow_type(-1) {
}
SwingStartTrafficEvent::SwingStartTrafficEvent(TrafficType* traffic_type, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_SWING_START_EVT, traffic_type, id, h_uid, d_uid, d_ip, st), session_id(1), rre_id(1), req_size(0), rsp_size(0),
        dst_port(0), flow_type(-1)  {
}
SwingStartTrafficEvent::SwingStartTrafficEvent(SwingStartTrafficEvent* evt) :
        StartTrafficEvent(evt), session_id(evt->session_id), rre_id(evt->rre_id), req_size(evt->req_size),
        rsp_size(evt->rsp_size), dst_port(evt->dst_port), flow_type(evt->flow_type) {
}
SwingStartTrafficEvent::SwingStartTrafficEvent(const SwingStartTrafficEvent& evt) :
		StartTrafficEvent(evt), session_id(evt.session_id), rre_id(evt.rre_id), req_size(evt.req_size),
		rsp_size(evt.rsp_size), dst_port(evt.dst_port), flow_type(evt.flow_type){
}
SwingStartTrafficEvent::~SwingStartTrafficEvent(){

}
prime::ssf::ssf_compact* SwingStartTrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = StartTrafficEvent::pack();
	dp->add_unsigned_int(session_id);
	dp->add_unsigned_int(rre_id);
	dp->add_int(req_size);
	dp->add_int(rsp_size);
	dp->add_int(dst_port);
	dp->add_int(flow_type);
	return dp;
}

void SwingStartTrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	StartTrafficEvent::unpack(dp);
	dp->get_unsigned_int(&session_id,1);
	dp->get_unsigned_int(&rre_id,1);
	dp->get_int(&req_size,1);
	dp->get_int(&rsp_size,1);
	dp->get_int(&dst_port,1);
	dp->get_int(&flow_type,1);
}

void SwingStartTrafficEvent::setSessionID(uint32_t s_id){
        session_id=s_id;
}

void SwingStartTrafficEvent::setRreID(uint32_t r_id){
        rre_id=r_id;
}

uint32_t SwingStartTrafficEvent::getSessionID(){
        return session_id;
}

uint32_t SwingStartTrafficEvent::getRreID(){
        return rre_id;
}

void SwingStartTrafficEvent::setRequestSize(int req){
        req_size=req;
}

void SwingStartTrafficEvent::setResponseSize(int rsp){
        rsp_size=rsp;
}

int SwingStartTrafficEvent::getRequestSize(){
        return req_size;
}

int SwingStartTrafficEvent::getResponseSize(){
        return rsp_size;
}

void SwingStartTrafficEvent::setDstPort(int port){
        dst_port = port;
}

int SwingStartTrafficEvent::getDstPort(){
        return dst_port;
}

void SwingStartTrafficEvent::setFlowType(int type){
        flow_type = type;
}

int SwingStartTrafficEvent::getFlowType(){
        return flow_type;
}

prime::ssf::Event* SwingStartTrafficEvent::createSwingStartTrafficEvent(prime::ssf::ssf_compact* dp){
	SwingStartTrafficEvent* t_evt = new SwingStartTrafficEvent();
    t_evt->unpack(dp);
    return t_evt;
}

SwingFinishedTrafficEvent::SwingFinishedTrafficEvent():
		FinishedTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_SWING_FINISH_EVT){
}

SwingFinishedTrafficEvent::SwingFinishedTrafficEvent(UID_t trafficTypeUID, int target_community_id, uint32_t id, uint32_t s_id, uint32_t r_id, float tm) :
        FinishedTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_SWING_FINISH_EVT, trafficTypeUID, target_community_id, id), session_id(s_id), rre_id(r_id), timeout(tm) {
}

SwingFinishedTrafficEvent::SwingFinishedTrafficEvent(TrafficType* trafficType, int target_community_id, uint32_t id, uint32_t s_id, uint32_t r_id, float tm) :
        FinishedTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_SWING_FINISH_EVT, trafficType, target_community_id, id), session_id(s_id), rre_id(r_id), timeout(tm) {
}

SwingFinishedTrafficEvent::SwingFinishedTrafficEvent(SwingFinishedTrafficEvent* evt) :
        FinishedTrafficEvent(evt), session_id(evt->session_id), rre_id(evt->rre_id), timeout(evt->timeout) {
}

SwingFinishedTrafficEvent::SwingFinishedTrafficEvent(const SwingFinishedTrafficEvent& evt) :
        FinishedTrafficEvent(evt), session_id(evt.session_id), rre_id(evt.rre_id), timeout(evt.timeout) {
}

SwingFinishedTrafficEvent::~SwingFinishedTrafficEvent(){

}

prime::ssf::ssf_compact* SwingFinishedTrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = FinishedTrafficEvent::pack();
	dp->add_unsigned_int(session_id);
	dp->add_unsigned_int(rre_id);
	dp->add_float(timeout);
	return dp;
}

void SwingFinishedTrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	FinishedTrafficEvent::unpack(dp);
	dp->get_unsigned_int(&session_id,1);
	dp->get_unsigned_int(&rre_id,1);
	dp->get_float(&timeout);
}

void SwingFinishedTrafficEvent::setSessionID(uint32_t s_id){
	session_id=s_id;
}

void SwingFinishedTrafficEvent::setRreID(uint32_t r_id){
	rre_id=r_id;
}

uint32_t SwingFinishedTrafficEvent::getSessionID(){
    return session_id;
}

uint32_t SwingFinishedTrafficEvent::getRreID(){
    return rre_id;
}

float SwingFinishedTrafficEvent::getTimeout(){
    return timeout;
}

prime::ssf::Event* SwingFinishedTrafficEvent::createSwingFinishedTrafficEvent(prime::ssf::ssf_compact* dp){
	SwingFinishedTrafficEvent* t_evt = new SwingFinishedTrafficEvent();
    t_evt->unpack(dp);
    return t_evt;
}

ConfigType* SwingTCPTraffic::getProtocolType() { return SwingClient::getClassConfigType(); }

SwingTCPTraffic::SwingTCPTraffic() : traffic_id(0), session_id(1), rre_id(1), heap_actions(IntvActionComparison(true)) {
}

SwingTCPTraffic::~SwingTCPTraffic() {
	for (int i = 0; i < TRAFFIC_TYPE; i++) {
		for (int j = 0; j < TRAFFIC_DIRECTION; j++) {
			delete tpf[i][j];
		}
	}
	/*
	if (file_summary) delete[] file_summary;
	*/
}

bool SwingTCPTraffic::shouldBeIncludedInCommunity(Community* com) {
	return CentralizedTrafficType::shouldBeIncludedInCommunity(com);
}


void SwingTCPTraffic::init(){
	CentralizedTrafficType::init();
	LOG_DEBUG("trace description file is: "<<shared.trace_description.read()<<endl)
	//LOG_DEBUG("trace description file is: "<<shared.trace_description.read()<<endl)
	readTrafficSummary(shared.trace_description.read());
	loadTrafficSummary();
	//create swing flow for each direction and type combination
	for (int traffic_type = 0; traffic_type < TRAFFIC_TYPE; traffic_type++) {
		for (int traffic_direction = 0; traffic_direction < TRAFFIC_DIRECTION; traffic_direction++) {
			//create swing flow for each combination of type and direction
			sf[traffic_type][traffic_direction] = new SwingFlow(this, traffic_type, traffic_direction);
		}
	}
	LOG_DEBUG("go through the client nets"<<endl)
	UIDVec& src_rids=shared.srcs.read().getCompiledRI()->getUIDVec();
	//go through the client nets, for every host, check the connected link's delay, bw and tcp's mss
	for (UIDVec::iterator i = src_rids.begin(); i!= src_rids.end(); i++) {
		Partition* pt=getPartition();
		Net* net=SSFNET_DYNAMIC_CAST(Net*,pt->getCommunityWithLowestId()->getObject(CompiledRID::getUID(*i,getParent()->getParent()),true)); // nets are passive objects so we need to specifically as for them to be returned
		if(!net) {
			LOG_ERROR("the net was unexpectedly null!"<<endl)
		}
		LOG_DEBUG("net id="<<(*i)<<", net is: "<<net->getUName()<<endl)
		ChildIterator<Host*> hosts = net->getHosts();
	    while(hosts.hasMoreElements()) {
	    	Host* h=hosts.nextElement();
	    	if(h->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP)){
	    		ChildIterator<Interface*> ifaces=h->getInterfaces();
	    		Interface* iface=ifaces.nextElement();
	    		double delay=iface->getLink()->getDelay().second();
	    		float bw=iface->getLink()->getBandwidth();
	    		int psize=(int)SSFNET_DYNAMIC_CAST(TCPMaster*,h->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP))->getMSS();
	    		//LOG_DEBUG("----java, delay="<<delay<<", bw="<<bw<<", psize="<<psize<<endl)
	    		//for each flow, create the type uid map for clients
	    		for (int traffic_type = 0; traffic_type < TRAFFIC_TYPE; traffic_type++) {
	    			for (int traffic_direction = 0; traffic_direction < TRAFFIC_DIRECTION; traffic_direction++) {
	    				FlowDescription::TypeParamMap::iterator iter;
	    				for(iter = sf[traffic_type][traffic_direction]->tpf_sf->typeParamClient.begin();
							iter!= sf[traffic_type][traffic_direction]->tpf_sf->typeParamClient.end(); iter++){
	    					//LOG_DEBUG("-----c, delay="<<iter->second.at(0)<<", bw="<<iter->second.at(1)
	    							//<<", psize="<<iter->second.at(2)<<endl)
	    					if(((float)delay-iter->second.at(0)<=0.0000001) && ((float)delay-iter->second.at(0)>=-0.0000001)
	    							&& (bw-iter->second.at(1)<=0.01) && (bw-iter->second.at(1)>=-0.001)
	    							&& (psize-iter->second.at(2)<=0.1) && (psize-iter->second.at(2)>=-0.01)){
	    						int type=iter->first;
	    						if(sf[traffic_type][traffic_direction]->typeClientUID.find(type)==sf[traffic_type][traffic_direction]->typeClientUID.end()){
	    							UIDVec clients;
	    							clients.push_back(h->getUID());
	    							LOG_DEBUG("1 insert type="<<type<<", client id="<<h->getUID()<<", traffic_type="<<traffic_type<<", direction="<<traffic_direction<<endl)
	    							sf[traffic_type][traffic_direction]->typeClientUID.insert(SSFNET_MAKE_PAIR(type, clients));
	    						}else{
	    							LOG_DEBUG("2 insert type="<<type<<", client id="<<h->getUID()<<", traffic_type="<<traffic_type<<", direction="<<traffic_direction<<endl)
	    							sf[traffic_type][traffic_direction]->typeClientUID.find(type)->second.push_back(h->getUID());
	    						}
	    					}
	    				}
	    				/*SwingFlow::TypeUIDMap::iterator it;
	    				for(it=sf[traffic_type][traffic_direction]->typeClientUID.begin();
	    					it!=sf[traffic_type][traffic_direction]->typeClientUID.end();it++){
	    					   LOG_DEBUG("type="<<it->first<<", uid size="<<it->second.size()<<endl)
	    				}*/
	    			}
	    		}
	    	}
		}
	}
	LOG_DEBUG("go through the server nets"<<endl)
	//go through the server nets, for every host, check the connected link's delay, bw and tcp's mss
	UIDVec& dst_rids=shared.dsts.read().getCompiledRI()->getUIDVec();
	for (UIDVec::iterator i = dst_rids.begin(); i!= dst_rids.end(); i++) {
		Partition* pt = getPartition();
		Net* net=SSFNET_DYNAMIC_CAST(Net*,pt->getCommunityWithLowestId()->getObject(CompiledRID::getUID(*i,getParent()->getParent()),true)); // nets are passive objects so we need to specifically as for them to be returned
		if(!net) {
			LOG_ERROR("the net was unexpectedly null!"<<endl)
		}
		//LOG_DEBUG("net is: "<<net->getUName()<<endl)
		ChildIterator<Host*> hosts = net->getHosts();
		while (hosts.hasMoreElements()) {
			Host* h = hosts.nextElement();
			//LOG_DEBUG("host uid="<<h->getUID()<<", TCP TYPE="<<h->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP)<<endl)
			if (h->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP)) {
				ChildIterator<Interface*> ifaces = h->getInterfaces();
				Interface* iface = ifaces.nextElement();
				double delay=iface->getLink()->getDelay().second();
				float bw = iface->getLink()->getBandwidth();
				int psize = (int)SSFNET_DYNAMIC_CAST(TCPMaster*,h->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP))->getMSS();
				//for each flow, create the typeuid map for the servers
				for (int traffic_type = 0; traffic_type < TRAFFIC_TYPE; traffic_type++) {
					for (int traffic_direction = 0; traffic_direction < TRAFFIC_DIRECTION; traffic_direction++) {
						//LOG_DEBUG("the first loop"<<endl)
						FlowDescription::TypeParamMap::iterator iter;
						for (iter= sf[traffic_type][traffic_direction]->tpf_sf->typeParamServer.begin(); iter
								!= sf[traffic_type][traffic_direction]->tpf_sf->typeParamServer.end(); iter++) {
							//LOG_DEBUG("the second loop"<<endl)
							//LOG_DEBUG("delay="<<delay<<", bw="<<bw<<", psize="<<psize<<endl)
							//LOG_DEBUG("iter->second.at(0)="<<iter->second.at(0)<<", iter->second.at(1)="<<iter->second.at(1)
									//<<",iter->second.at(2)"<<iter->second.at(2)<<endl)
							if(((float)delay-iter->second.at(0)<=0.0000001) && ((float)delay-iter->second.at(0)>=-0.0000001)
								&& (bw-iter->second.at(1)<=0.01) && (bw-iter->second.at(1)>=-0.001)
								&& (psize-iter->second.at(2)<=0.1) && (psize-iter->second.at(2)>=-0.01)){
								int type = iter->first;
								if (sf[traffic_type][traffic_direction]->typeServerUID.find(type)==sf[traffic_type][traffic_direction]->typeServerUID.end()) {
									UIDVec servers;
									servers.push_back(h->getUID());
									LOG_DEBUG("type="<<type<<", server="<<h->getUID()<<endl)
									sf[traffic_type][traffic_direction]->typeServerUID.insert(SSFNET_MAKE_PAIR(type, servers));
								} else {
									(sf[traffic_type][traffic_direction]->typeServerUID.find(type))->second.push_back(h->getUID());
									LOG_DEBUG("type="<<type<<", server="<<h->getUID()<<endl)
								}
							}
						}
					}
				}
			}
		}
	}
}

void SwingTCPTraffic::setStartTrafficEvent(StartTrafficEvent*& traffics_to_start, VirtualTime cur, SwingPair* sp){
	traffics_to_start->setStartTime(cur); //start the traffic immediately, because the recall_time has been set to be the interval
	int client_id = sp->getSwingConnection()->getSwingRRE()->getSwingSession()->client_id;
	int server_id = sp->getSwingConnection()->server_id;
	traffics_to_start->setHostUID(client_id);
	traffics_to_start->setDstUID(server_id);
	traffics_to_start->setTrafficId(traffic_id);
	traffic_id++;
	traffics_to_start->setTrafficType(this);
	traffics_to_start->setTrafficTypeUID(getUID());
	//set the additional parameters in swing start traffic event: session id, rre id, request size, response size
	SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,traffics_to_start)->
		setSessionID(sp->getSwingConnection()->getSwingRRE()->getSwingSession()->getSessionID());
	SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,traffics_to_start)->setRreID(sp->getSwingConnection()->getSwingRRE()->getRreID());
	SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,traffics_to_start)->setRequestSize(sp->getReqSize());
	SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,traffics_to_start)->setResponseSize(sp->getRspSize());
	//if pair is HTTP, dst_port_=80; otherwise, the other value
	int dst_port = 0;
	int flow_type = -1;
	if (sp->getSwingConnection()->getSwingRRE()->getSwingSession()->getSwingFlow()->t_type == SwingTCPTraffic::TRAFFIC_TYPE_HTTP) {
		dst_port = 80;
		if(sp->getSwingConnection()->getSwingRRE()->getSwingSession()->getSwingFlow()->t_direction == SwingTCPTraffic::TRAFFIC_DIRECTION_LEFT_RIGHT){
			flow_type = 0;
		}else{
			flow_type = 1;
		}
	} else {
		dst_port = 1024;
		if(sp->getSwingConnection()->getSwingRRE()->getSwingSession()->getSwingFlow()->t_direction == SwingTCPTraffic::TRAFFIC_DIRECTION_LEFT_RIGHT){
			flow_type = 2;
		}else{
			flow_type = 3;
		}
	}
	SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,traffics_to_start)->setDstPort(dst_port);
	SSFNET_DYNAMIC_CAST(SwingStartTrafficEvent*,traffics_to_start)->setFlowType(flow_type);
}


void SwingTCPTraffic::getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
		UpdateTrafficTypeEvent*& update_vent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
		bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
		VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
) {
	VirtualTime cur = VirtualTime(prime::ssf::now());
	//if this is the first time this function being called, push the first session for each swing traffic flow
	if (traffic_id == 0) {
		LOG_DEBUG("traffic id is 0"<<endl)
		for (int traffic_type = 0; traffic_type < TRAFFIC_TYPE; traffic_type++) {
			for (int traffic_direction = 0; traffic_direction < TRAFFIC_DIRECTION; traffic_direction++) {
				//create swing flow for each combination of type and direction
				float intv = sf[traffic_type][traffic_direction]->getSessionInterval();
				//create a new session for each flow
				SwingSession* ss_start = new SwingSession(sf[traffic_type][traffic_direction]);
				ss_start->setSessionID(session_id);
				id_session.insert(SSFNET_MAKE_PAIR(session_id, ss_start));
				session_id++;
				ss_start->client_id=ss_start->getClient();
				LOG_DEBUG("start session client id="<<ss_start->client_id<<endl)
				ss_start->setNumRemainingRRE(ss_start->getNumRREs());
				LOG_DEBUG("---push session, session_intv="<<intv<<", time is: "<<(cur+VirtualTime(intv,VirtualTime::SECOND)).second()<<endl)
				heap_actions.push(SSFNET_MAKE_PAIR(cur+VirtualTime(intv,VirtualTime::SECOND),ss_start));
			}
		}
		traffic_id++;
	} else{ //traffic id is greater than 1
		//pop the actions from the heap if it's ancestor has already been deleted
		int stop=0;
		while(!heap_actions.empty() && stop==0){
			assert(heap_actions.top().second);
			if(heap_actions.top().second->getAction() == SwingAction::RRE &&
				SSFNET_DYNAMIC_CAST(SwingRRE*,heap_actions.top().second)->getSwingSession()==0){
				LOG_DEBUG("RRE, PARENT HAS BEEN DELETED!"<<endl)
				delete heap_actions.top().second;
				heap_actions.pop();
			}else if(heap_actions.top().second->getAction() == SwingAction::CONNECTION &&
				(SSFNET_DYNAMIC_CAST(SwingConnection*,heap_actions.top().second)->getSwingRRE()==0 ||
					SSFNET_DYNAMIC_CAST(SwingConnection*,heap_actions.top().second)->getSwingRRE()->getSwingSession()==0)	){
				LOG_DEBUG("CONN, PARENT HAS BEEN DELETED!"<<endl)
				delete heap_actions.top().second;
				heap_actions.pop();
			}else if (heap_actions.top().second->getAction() == SwingAction::PAIR &&
				(SSFNET_DYNAMIC_CAST(SwingPair*,heap_actions.top().second)->getSwingConnection()==0 ||
					SSFNET_DYNAMIC_CAST(SwingPair*,heap_actions.top().second)->getSwingConnection()->getSwingRRE()==0 ||
					SSFNET_DYNAMIC_CAST(SwingPair*,heap_actions.top().second)->getSwingConnection()->getSwingRRE()->getSwingSession()==0)	){
				LOG_DEBUG("PAIR, PARENT HAS BEEN DELETED!"<<endl)
				delete heap_actions.top().second;
				heap_actions.pop();
			}else{
				stop=1;
			}
		}

		if (!heap_actions.empty()){
			assert(heap_actions.top().second);
			if(heap_actions.top().second->getAction() == SwingAction::SESSION) { /*session intv is the minimum*/
				LOG_DEBUG("session, time of heap top is: "<<heap_actions.top().first.second()<<endl)
				SwingSession* ss = SSFNET_DYNAMIC_CAST(SwingSession*,heap_actions.top().second);
				heap_actions.pop();

				//create the first RRE for this session
				SwingRRE* sr_start = new SwingRRE(ss);
				ss->swing_rre_list.push_back(sr_start);
				ss->setNumRemainingRRE(ss->getNumRemainingRRE()-1);
				sr_start->setRreID(rre_id);
				id_rre.insert(SSFNET_MAKE_PAIR(rre_id, sr_start));
				rre_id++;
				sr_start->setNumRemainingConn(sr_start->getNumConnections());
				if(ss->getNumRemainingRRE()>0){
					//create a new rre for this session and push it into the heap
					SwingRRE* sr_new = new SwingRRE(ss);
					ss->swing_rre_list.push_back(sr_new);
					ss->setNumRemainingRRE(ss->getNumRemainingRRE()-1);
					sr_new->setRreID(rre_id);
					id_rre.insert(SSFNET_MAKE_PAIR(rre_id, sr_new));
					rre_id++;
					sr_new->setNumRemainingConn(sr_new->getNumConnections());
					float rre_intv = ss->getRREInterval();
					//push the rre of ths session in the heap
					//LOG_DEBUG("---push rre, rre_intv="<<rre_intv<<", time is: "<<(cur+VirtualTime(rre_intv,VirtualTime::SECOND)).second()<<endl)
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(rre_intv,VirtualTime::SECOND)),sr_new));
				}

				//Create the first connection for the first rre
				SwingConnection* sc_start = new SwingConnection(sr_start);
				sr_start->swing_conn_list.push_back(sc_start);
				sr_start->setNumRemainingConn(sr_start->getNumRemainingConn()-1);
				sc_start->server_id = sc_start->getServer();
				sc_start->setNumRemainingPair(sc_start->getNumPairs());
				if(sr_start->getNumRemainingConn()>0){
					//create a new conn for this rre and push it into the heap
					SwingConnection* sc_new = new SwingConnection(sr_start);
					sr_start->swing_conn_list.push_back(sc_new);
					sc_new->setNumRemainingPair(sc_new->getNumPairs());
					sc_new->server_id = sc_new->getServer();
					sr_start->setNumRemainingConn(sr_start->getNumRemainingConn()-1);
					float conn_intv = sr_start->getConnectionInterval();
					//LOG_DEBUG("---push connection, conn_intv="<<conn_intv<<", time is: "<<(cur+VirtualTime(conn_intv,VirtualTime::SECOND)).second()<<endl)
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(conn_intv,VirtualTime::SECOND)),sc_new));
				}

				//Create the first pair for the first connection of the first rre of this session
				SwingPair* sp_start = new SwingPair(sc_start);
				sc_start->swing_pair_list.push_back(sp_start);
				sc_start->setNumRemainingPair(sc_start->getNumRemainingPair()-1);
				if(sc_start->getNumRemainingPair()>0){
					//create a new pair for this connection and push it into the heap
					SwingPair* sp_new = new SwingPair(sc_start);
					sc_start->swing_pair_list.push_back(sp_new);
					sc_start->setNumRemainingPair(sc_start->getNumRemainingPair()-1);
					float think_time = sp_start->getThinkTime();;
					//LOG_DEBUG("---push pair, think time="<<think_time<<", time is: "<<(cur+VirtualTime(think_time,VirtualTime::SECOND)).second()<<endl)
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(think_time,VirtualTime::SECOND)),sp_new));\
				}

				//create a swing start traffic event
				traffics_to_start = new SwingStartTrafficEvent();
				//set the parameters in the start traffic event
				this->setStartTrafficEvent(traffics_to_start, cur, sp_start);

				//Create a new session of this flow and push it in the heap_actions
				float sess_intv = ss->getSwingFlow()->getSessionInterval();
				SwingSession* ss_new = new SwingSession(ss->getSwingFlow());
				ss_new->setSessionID(session_id);
				id_session.insert(SSFNET_MAKE_PAIR(session_id, ss_new));
				session_id++;
				ss_new->client_id = ss_new->getClient();
				ss_new->setNumRemainingRRE(ss_new->getNumRREs());
				//LOG_DEBUG("create a new session, sess_intv="<<sess_intv<<", client_id="<<ss_new->client_id<<endl)
				//LOG_DEBUG("---push session, session_intv="<<sess_intv<<", time is: "<<(cur+VirtualTime(sess_intv,VirtualTime::SECOND)).second()<<endl)
				heap_actions.push(SSFNET_MAKE_PAIR(cur+VirtualTime(sess_intv,VirtualTime::SECOND),ss_new));

			} else if (heap_actions.top().second->getAction() == SwingAction::RRE) { /*rre intv is the minimum*/
				LOG_DEBUG("rre, time of heap top is: "<<heap_actions.top().first.second()<<endl)
				SwingRRE* sr = SSFNET_DYNAMIC_CAST(SwingRRE*,heap_actions.top().second);
				heap_actions.pop();

				//create a new rre of its session if needed and push it into the heap
				if (sr->getSwingSession()->getNumRemainingRRE() > 0) {
					float rre_intv = sr->getSwingSession()->getRREInterval();
					SwingRRE* sr_new = new SwingRRE(sr->getSwingSession());
					sr_new->getSwingSession()->swing_rre_list.push_back(sr_new);
					sr->getSwingSession()->setNumRemainingRRE(sr->getSwingSession()->getNumRemainingRRE()-1);
					sr_new->setRreID(rre_id);
					id_rre.insert(SSFNET_MAKE_PAIR(rre_id, sr_new));
					rre_id++;
					sr_new->setNumRemainingConn(sr_new->getNumConnections());
					//LOG_DEBUG("---push rre, rre_intv="<<rre_intv<<", time is: "<<(cur+VirtualTime(rre_intv,VirtualTime::SECOND)).second()<<endl)
					heap_actions.push(SSFNET_MAKE_PAIR(cur+VirtualTime(rre_intv,VirtualTime::SECOND),sr_new));
				}

				//create the first connection of this rre
				SwingConnection* sc_start = new SwingConnection(sr);
				sr->swing_conn_list.push_back(sc_start);
				sr->setNumRemainingConn(sr->getNumRemainingConn()-1);
				sc_start->server_id = sc_start->getServer();
				sc_start->setNumRemainingPair(sc_start->getNumPairs());
				if(sr->getNumRemainingConn()>0){
					//create a new conn for this rre and push it into the heap
					SwingConnection* sc_new = new SwingConnection(sr);
					sr->swing_conn_list.push_back(sc_new);
					sc_new->setNumRemainingPair(sc_new->getNumPairs());
					sc_new->server_id = sc_new->getServer();
					sr->setNumRemainingConn(sr->getNumRemainingConn()-1);
					float conn_intv = sr->getConnectionInterval();
					//LOG_DEBUG("---push connection, conn_intv="<<conn_intv<<", time is: "<<(cur+VirtualTime(conn_intv,VirtualTime::SECOND)).second()<<endl)
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(conn_intv,VirtualTime::SECOND)),sc_new));
				}

				//Create the first pair for the first connection of this rre
				SwingPair* sp_start = new SwingPair(sc_start);
				sc_start->swing_pair_list.push_back(sp_start);
				sc_start->setNumRemainingPair(sc_start->getNumRemainingPair()-1);
				if(sc_start->getNumRemainingPair()>0){
					//create a new pair for this connection and push it into the heap
					SwingPair* sp_new = new SwingPair(sc_start);
					sc_start->swing_pair_list.push_back(sp_new);
					sc_start->setNumRemainingPair(sc_start->getNumRemainingPair()-1);
					float think_time = sp_start->getThinkTime();;
					//LOG_DEBUG("---push pair, think time="<<think_time<<", time is: "<<(cur+VirtualTime(think_time,VirtualTime::SECOND)).second()<<endl)
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(think_time,VirtualTime::SECOND)),sp_new));\
				}

				//create a swing start traffic event
				traffics_to_start = new SwingStartTrafficEvent();
				//set the parameters in the start traffic event
				this->setStartTrafficEvent(traffics_to_start, cur, sp_start);

			} else if (heap_actions.top().second->getAction()== SwingAction::CONNECTION) { /*connection intv is the minimum*/
				LOG_DEBUG("connection, time of heap top is: "<<heap_actions.top().first.second()<<endl)
				SwingConnection* sc = SSFNET_DYNAMIC_CAST(SwingConnection*,heap_actions.top().second);
				heap_actions.pop();

				//create a new connection of its rre if needed and push it into the heap
				if(sc->getSwingRRE()->getNumRemainingConn()>0){
					SwingConnection* sc_new = new SwingConnection(sc->getSwingRRE());
					sc_new->getSwingRRE()->swing_conn_list.push_back(sc_new);
					sc_new->setNumRemainingPair(sc_new->getNumPairs());
					sc_new->server_id = sc_new->getServer();
					sc->getSwingRRE()->setNumRemainingConn(sc->getSwingRRE()->getNumRemainingConn()-1);
					float conn_intv = sc->getSwingRRE()->getConnectionInterval();
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(conn_intv,VirtualTime::SECOND)),sc_new));
				}

				//Create the first pair for this connection
				SwingPair* sp_start = new SwingPair(sc);
				sc->swing_pair_list.push_back(sp_start);
				sc->setNumRemainingPair(sc->getNumRemainingPair()-1);
				if(sc->getNumRemainingPair()>0){
					//create a new pair for this connection and push it into the heap
					SwingPair* sp_new = new SwingPair(sc);
					sc->swing_pair_list.push_back(sp_new);
					sc->setNumRemainingPair(sc->getNumRemainingPair()-1);
					float think_time = sp_start->getThinkTime();;
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(think_time,VirtualTime::SECOND)),sp_new));
				}

				//create a swing start traffic event
				traffics_to_start = new SwingStartTrafficEvent();
				//set the parameters in the start traffic event
				this->setStartTrafficEvent(traffics_to_start, cur, sp_start);

			}else if (heap_actions.top().second->getAction()==SwingAction::PAIR) { /*pair intv is the minimum*/
				LOG_DEBUG("pair, time of heap top is: "<<heap_actions.top().first.second()<<endl)
				SwingPair* sp = SSFNET_DYNAMIC_CAST(SwingPair*,heap_actions.top().second);
				heap_actions.pop();

				//create a swing start traffic event
				traffics_to_start = new SwingStartTrafficEvent();
				//set the parameters in the start traffic event
				this->setStartTrafficEvent(traffics_to_start, cur, sp);
				//Create new pair of its connection if needed and push it to the heap
				if(sp->getSwingConnection()->getNumRemainingPair()>0) {
					SwingPair* sp_new = new SwingPair(sp->getSwingConnection());
					sp_new->getSwingConnection()->swing_pair_list.push_back(sp_new);
					sp_new->getSwingConnection()->setNumRemainingPair(sp->getSwingConnection()->getNumRemainingPair()-1);
					float thinktime = sp->getThinkTime();
					LOG_DEBUG("---push pair, pair_intv="<<thinktime<<", time is: "<<(cur+VirtualTime(thinktime,VirtualTime::SECOND)).second()<<endl)
					heap_actions.push(SSFNET_MAKE_PAIR((cur+VirtualTime(thinktime,VirtualTime::SECOND)),sp_new));
				}
			} else {
				LOG_ERROR("The action can not be recognized!"<<endl)
			}
		}
	}
	if(heap_actions.empty()){
		//Finish the traffic
		LOG_DEBUG("wrapping up traffic" << endl);
		wrap_up=true;
	} else {
		LOG_DEBUG("recalling at:" << (heap_actions.top().first-cur) << endl);
		recall_at = heap_actions.top().first-cur;
	}
}

void SwingTCPTraffic::processUpdateEvent(UpdateTrafficTypeEvent* update_evt) {
	//if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
	//For centralized traffic type, there should not be update events
	if (update_evt) {
		LOG_ERROR("Swing TCP traffic is centralized traffic type, should not received the update event. "<<endl)
	}
}

void SwingTCPTraffic::processFinishedEvent(FinishedTrafficEvent* finished_evt) {
	CentralizedTrafficType::processFinishedEvent(finished_evt);
	uint32_t session_id = SSFNET_DYNAMIC_CAST(SwingFinishedTrafficEvent*,finished_evt)->getSessionID();
	if(session_id!=0){
		LOG_DEBUG("PROCESS FINISHED TRAFFIC EVENT, delete session! session id="<<session_id<<endl)
		if(id_session.find(session_id)!=id_session.end()){
			if((*id_session.find(session_id)).second){
				delete (*id_session.find(session_id)).second;
				(*id_session.find(session_id)).second=0;
			}
		}
	}
	uint32_t rre_id = SSFNET_DYNAMIC_CAST(SwingFinishedTrafficEvent*,finished_evt)->getRreID();
	if(rre_id!=0){
		LOG_DEBUG("PROCESS FINISHED TRAFFIC EVENT, delete rre! rre id="<<rre_id<<endl)
		if(id_rre.find(rre_id)!=id_rre.end()){
			if((*id_rre.find(rre_id)).second && (*id_rre.find(rre_id)).second->getSwingSession()){
				for(SSFNET_LIST(SwingRRE*)::iterator iter = (*id_rre.find(rre_id)).second->getSwingSession()->swing_rre_list.begin();
					iter!= (*id_rre.find(rre_id)).second->getSwingSession()->swing_rre_list.end(); iter++){
					if(*iter==(*id_rre.find(rre_id)).second){
						delete *iter;
						(*id_rre.find(rre_id)).second->getSwingSession()->swing_rre_list.erase(iter);
						break;
					}
				}
			}
		id_rre.erase(id_rre.find(rre_id));
		}
	}
}

void SwingTCPTraffic::readTrafficSummary(SSFNET_STRING file) {
	FILE* traffic_summary;
	char file_name_tmp[150];
	char* file_name[PARAMS];
	traffic_summary = fopen(file.c_str(),"r");
	if(!traffic_summary) {
		LOG_ERROR("The traffic summary file cannot be opened properly."<<endl);
	}
	for(int i=0; i<TRAFFIC_TYPE; i++){
		for(int j=0; j<TRAFFIC_DIRECTION; j++){
			for(int k=0; k<PARAMS; k++){
				if(fscanf(traffic_summary, "%s", file_name_tmp)!=EOF){
					file_name[k] = new char[150];
					strcpy(file_name[k], file_name_tmp);
					file_summary[i][j][k]= file_name[k];
					//printf("%s\n", file_summary[i][j][k]);
				}else{
					LOG_ERROR("The file names cannot be read properly!"<<endl);
				}
			}
		}
	}
	fclose(traffic_summary);
	traffic_summary = NULL;
}

void SwingTCPTraffic::loadTrafficSummary() {
	for (int i = 0; i < TRAFFIC_TYPE; i++) {
		for (int j = 0; j < TRAFFIC_DIRECTION; j++) {
			tpf[i][j] = new FlowDescription;
			for (int k = 0; k < PARAMS; k++) {
				tpf[i][j]->traffic_summary[k] = file_summary[i][j][k];
				//LOG_DEBUG(tpf[i][j]->traffic_summary[k])
				//printf("%s\n", tpf[i][j]->traffic_summary[k]);
			}
			tpf[i][j]->loadSessionInterval();
			tpf[i][j]->loadClient();
			tpf[i][j]->loadNumRREs();
			tpf[i][j]->loadRREInterval();
			tpf[i][j]->loadNumConnections();
			tpf[i][j]->loadConnectionInterval();
			tpf[i][j]->loadServer();
			tpf[i][j]->loadNumPairs();
			tpf[i][j]->loadReqSize();
			tpf[i][j]->loadRspSize();
			tpf[i][j]->loadThinkTime();
		}
	}
}

float SwingTCPTraffic::getSessionTimeout(){
	return shared.session_timeout.read();
}

float SwingTCPTraffic::getRreTimeout(){
	return shared.rre_timeout.read();
}

void SwingTCPTraffic::stretch(int stretch_type, int traffic_type,
		int traffic_direction, float factor) {
	int i;
	switch (stretch_type) {
	case STRETCH_TYPE_SESS_INTV:
		for (i = 0; i< tpf[traffic_type][traffic_direction]->session_interval_size; i++)
			tpf[traffic_type][traffic_direction]->session_interval_data[i] *= factor;
		break;
	case STRETCH_TYPE_RRE_INTV:
		for (i = 0; i < tpf[traffic_type][traffic_direction]->rre_interval_size; i++)
			tpf[traffic_type][traffic_direction]->rre_interval_data[i] *= factor;
		break;
	case STRETCH_TYPE_CONNECTION_INTV:
		for (i = 0; i< tpf[traffic_type][traffic_direction]->connection_interval_size; i++)
			tpf[traffic_type][traffic_direction]->connection_interval_data[i] *= factor;
		break;
	case STRETCH_TYPE_REQSIZE:
		for (i = 0; i < tpf[traffic_type][traffic_direction]->req_size; i++)
			tpf[traffic_type][traffic_direction]->req_data[i] *= factor;
		break;
	case STRETCH_TYPE_RSPSIZE:
		for (int i = 0; i < tpf[traffic_type][traffic_direction]->rsp_size; i++)
			tpf[traffic_type][traffic_direction]->rsp_data[i]
					= (int) tpf[traffic_type][traffic_direction]->rsp_data[i] * factor;
		break;
	case STRETCH_TYPE_THINK_TIME:
		for (int i = 0; i< tpf[traffic_type][traffic_direction]->think_time_size; i++)
			tpf[traffic_type][traffic_direction]->think_time_data[i] *= factor;
		break;
	default:
		LOG_ERROR(stretch_type<<" is invalid and cannot be stretched."<<endl );
	}
}

SwingFlow::SwingFlow(SwingTCPTraffic* st, int traffic_type, int traffic_direction) {
	swing_traffic = st;
	t_type = traffic_type;
	t_direction = traffic_direction;
	//tpf_sf = new FlowDescription();
	tpf_sf = swing_traffic->tpf[t_type][t_direction];
}

SwingFlow::~SwingFlow() {
}

SwingTCPTraffic* SwingFlow::getSwingTraffic() {
	return swing_traffic;
}

float SwingFlow::getSessionInterval() {
	int index;
	float session_interval;
	//LOG_DEBUG("session interval size="<<tpf_sf->session_interval_size<<endl)
	index = (rand() % tpf_sf->session_interval_size);
	session_interval = tpf_sf->session_interval_data[index];
	return session_interval;
}

SwingSession::SwingSession(SwingFlow* sf) : swing_flow(sf), session_id(0), remaining_rre(0) {}

SwingSession::~SwingSession() {
	swing_flow=0;
	for(SSFNET_LIST(SwingRRE*)::iterator iter = swing_rre_list.begin(); iter!= swing_rre_list.end(); iter++){
		if(*iter){
			(*iter)->deleteSwingSession();
		}
	}
}

SwingFlow* SwingSession::getSwingFlow() {
	return swing_flow;
}

int SwingSession::getClient() {
	int client=0;
	int index;
	SwingFlow* sf;
	sf = getSwingFlow();
	//generate a random number between 0 to 1
	float prob = sf->getSwingTraffic()->getRandom()->uniform(0,1);
	int type=0;
	for (FlowDescription::TypeProbMap::iterator iter = sf->tpf_sf->typeProbClient.begin(); iter!= sf->tpf_sf->typeProbClient.end(); iter++) {
		if(prob<=iter->second){
			type=iter->first;
			break;
		}
	}
	UIDVec& clients=sf->typeClientUID.find(type)->second;
	LOG_DEBUG("DEBUGGING, type="<<type<<", -------------clients size="<<clients.size()<<"traffic_type="<<sf->t_type<<", traffic_dir="<<sf->t_direction<<endl)
	//LOG_DEBUG("rand()="<<rand()<<", client size="<<clients.size()<<endl)
	if(clients.size()==0){
		LOG_ERROR("There is no available client!"<<endl)
	}else if(clients.size()==1){
		client=(int) clients.at(0);
	}else{
		index = rand() % clients.size();
		client = (int) clients.at(index);
	}
	return client;
}

SwingAction::ActionType SwingSession::getAction(){
	return SwingAction::SESSION;
}

int SwingSession::getNumRREs() {
	int num_rre;
	int index;
	SwingFlow* sf;
	sf = this->getSwingFlow();
	//LOG_DEBUG("random="<<rand()<<endl)
	index = (int)(rand() % sf->tpf_sf->num_rres_size);
	//LOG_DEBUG("Index="<<index<<endl)
	num_rre = sf->tpf_sf->num_rres_data[index];
	return num_rre;
}

void SwingSession::setNumRemainingRRE(int r){
	remaining_rre = r;
}

int SwingSession::getNumRemainingRRE(){
	return remaining_rre;
}

float SwingSession::getRREInterval() {
	float inter_rre;
	int index;
	SwingFlow* sf = this->getSwingFlow();
	index = (rand() % sf->tpf_sf->rre_interval_size);
	inter_rre = sf->tpf_sf->rre_interval_data[index] / 1000.0;
	return inter_rre;
}

void SwingSession::setSessionID(uint32_t s_id){
	session_id = s_id;
}

uint32_t SwingSession::getSessionID(){
	return session_id;
}

SwingRRE::SwingRRE(SwingSession* ss) : swing_session(ss), remaining_conn(0), rre_id(0) {}

SwingRRE::~SwingRRE() {
	swing_session=0;
	//put heap_actions to the map
	for(SSFNET_LIST(SwingConnection*)::iterator iter = swing_conn_list.begin(); iter!= swing_conn_list.end(); iter++){
		if(*iter){
			(*iter)->deleteSwingRRE();
		}
	}
}

SwingSession* SwingRRE::getSwingSession() {
	return swing_session;
}

void SwingRRE::deleteSwingSession(){
	swing_session = NULL;
}

SwingAction::ActionType SwingRRE::getAction(){
	return SwingAction::RRE;
}

int SwingRRE::getNumConnections() {
	int num_conn;
	int index;
	SwingSession* ss;
	SwingFlow* sf;
	ss = this->getSwingSession();
	sf = ss->getSwingFlow();
	index = (rand() % sf->tpf_sf->num_connections_size);
	num_conn = sf->tpf_sf->num_connections_data[index];
	return num_conn;
}

void SwingRRE::setNumRemainingConn(int c){
	remaining_conn = c;
}

int SwingRRE::getNumRemainingConn(){
	return remaining_conn;
}

float SwingRRE::getConnectionInterval() {
	float inter_conn;
	int index;
	SwingSession* ss;
	SwingFlow* sf;
	ss = this->getSwingSession();
	sf = ss->getSwingFlow();
	index = (rand() % sf->tpf_sf->connection_interval_size);
	inter_conn = sf->tpf_sf->connection_interval_data[index] / 1000.0;
	return inter_conn;
}

void SwingRRE::setRreID(uint32_t r_id){
	rre_id=r_id;
}

uint32_t SwingRRE::getRreID(){
	return rre_id;
}

SwingConnection::SwingConnection(SwingRRE* sr): swing_rre(sr), remaining_pair(0) {}

SwingConnection::~SwingConnection() {
	swing_rre=0;
	for(SSFNET_LIST(SwingPair*)::iterator iter = swing_pair_list.begin(); iter!= swing_pair_list.end(); iter++){
		if(*iter){
			(*iter)->deleteSwingConnection();
		}
	}
}

SwingRRE* SwingConnection::getSwingRRE() {
	return swing_rre;
}

void SwingConnection::deleteSwingRRE(){
	swing_rre = NULL;
}

SwingAction::ActionType SwingConnection::getAction(){
	return SwingAction::CONNECTION;
}

int SwingConnection::getServer() {
	int server=0;
	int index;
	SwingFlow* sf = this->getSwingRRE()->getSwingSession()->getSwingFlow();
	LOG_DEBUG("Swing flow="<<sf<<endl)
	float prob = sf->getSwingTraffic()->getRandom()->uniform(0,1);
	LOG_DEBUG("prob="<<prob<<endl)
	int type=0;
	for (FlowDescription::TypeProbMap::iterator iter = sf->tpf_sf->typeProbServer.begin(); iter!= sf->tpf_sf->typeProbServer.end(); iter++) {
		if(prob<=iter->second){
			type=iter->first;
			break;
		}
	}
	LOG_DEBUG("type="<<sf->typeServerUID.find(type)->first<<endl)
	LOG_DEBUG("flow type="<<sf->t_type<<", flow direction="<<sf->t_direction<<endl)
	LOG_DEBUG("server="<<sf->typeServerUID.find(type)->second.at(0)<<endl)

	for(UIDVec::iterator it=sf->typeServerUID.find(type)->second.begin(); it<sf->typeServerUID.find(type)->second.end();it++){
		LOG_DEBUG("type="<<type<<", server id="<<*it<<endl)
	}
	UIDVec servers=sf->typeServerUID.find(type)->second;
	LOG_DEBUG("server size="<<servers.size()<<endl)
	if(servers.size()==0){
		LOG_ERROR("There is no available server!"<<endl)
	}else if(servers.size()==1){
		server=(int) servers.at(0);
	}else{
		index = rand() % servers.size();
		server = (int) servers.at(index);
	}
	return server;
}

int SwingConnection::getNumPairs() {
	int num_pairs;
	int index;
	SwingRRE* sr=0;
	SwingSession* ss=0;
	SwingFlow* sf=0;
	sr = this->getSwingRRE();
	if(sr){
		ss=sr->getSwingSession();
	}else{
		return 0;
	}
	if(ss){
		LOG_DEBUG("ss"<<endl)
		sf = ss->getSwingFlow();
		LOG_DEBUG("sf:"<<sf<<endl)
		LOG_DEBUG("sf->tpf_sf:"<<sf->tpf_sf<<endl)
		index = (rand() % sf->tpf_sf->num_pairs_size);
		num_pairs = sf->tpf_sf->num_pairs_data[index];
		return num_pairs;
	}else{
		return 0;
	}
}

void SwingConnection::setNumRemainingPair(int p){
	remaining_pair = p;
}

int SwingConnection::getNumRemainingPair(){
	return remaining_pair;
}

SwingPair::SwingPair(SwingConnection* sc) : swing_conn(sc){}

SwingPair::~SwingPair() {
	swing_conn=0;
}

SwingConnection* SwingPair::getSwingConnection() {
	return swing_conn;
}

void SwingPair::deleteSwingConnection(){
	swing_conn = NULL;
}

SwingAction::ActionType SwingPair::getAction(){
	return SwingAction::PAIR;
}

int SwingPair::getReqSize() {
	int reqsize;
	int index;
	SwingConnection* sc;
	SwingRRE* sr;
	SwingFlow* sf;
	sc = this->getSwingConnection();
	sr = sc->getSwingRRE();

	sf = sr->getSwingSession()->getSwingFlow();
	index = (rand() % sf->tpf_sf->req_size);
	reqsize = sf->tpf_sf->req_data[index];
	return reqsize;

}

int SwingPair::getRspSize() {
	int rspsize;
	int index;
	SwingConnection* sc;
	SwingRRE* sr;
	SwingFlow* sf;
	sc = this->getSwingConnection();
	sr = sc->getSwingRRE();
	sf = sr->getSwingSession()->getSwingFlow();
	index = (rand() % sf->tpf_sf->rsp_size);
	rspsize = sf->tpf_sf->rsp_data[index];
	return rspsize;
}

float SwingPair::getThinkTime() {
	float think_time;
	int index;
	SwingConnection* sc;
	SwingRRE* sr;
	SwingFlow* sf;
	sc = this->getSwingConnection();
	sr = sc->getSwingRRE();
	sf = sr->getSwingSession()->getSwingFlow();
	index = (rand() % sf->tpf_sf->think_time_size);
	think_time = sf->tpf_sf->think_time_data[index] / 1000.0;
	return think_time;
}

FlowDescription::FlowDescription() {
}

FlowDescription::~FlowDescription() {
	delete[] session_interval_data;
	delete[] num_rres_data;
	delete[] rre_interval_data;
	delete[] num_connections_data;
	delete[] connection_interval_data;
	delete[] num_pairs_data;
	delete[] req_data;
	delete[] rsp_data;
	delete[] think_time_data;
}

void FlowDescription::loadSessionInterval() {
	char* session_interval_fn;
	FILE* session_interval;
	float tmp;
	session_interval_fn = traffic_summary[2];
	session_interval = fopen(session_interval_fn, "r");
	if (fscanf(session_interval, "%d", &session_interval_size) != EOF) {
		//printf("session_interval_size is %d\n", session_interval_size);
		session_interval_data = new float[session_interval_size];
	} else {
		LOG_ERROR("session_interval size cannot be read properly!"<<endl);
	}
	for (int j = 0; j < session_interval_size; j++) {
		if (fscanf(session_interval, "%f", &tmp) != EOF) {
			session_interval_data[j] = tmp;
			//printf("session interval is %f\n", session_interval_data[j]);
		} else {
			LOG_ERROR("session_interval cannot be read properly!"<<endl);
		}
	}
	fclose(session_interval);
	session_interval = NULL;
}

void FlowDescription::loadClient() {
	char* client_fn;
	FILE* client;
	float prob;
	float tmp;
	int psize;
	int type;
	client_fn = traffic_summary[0];
	client = fopen(client_fn, "r");
	if (fscanf(client, "%d", &client_size) != EOF) {
		LOG_DEBUG("client_size="<<client_size<<endl)
	} else {
		LOG_ERROR("client size cannot be read properly!"<<endl);
	}
	for (int i = 0; i < client_size; i++) {
		params.clear();
		if (fscanf(client, "%d", &type) != EOF) {
			LOG_DEBUG("client type="<<type<<endl)
		} else {
			LOG_ERROR("client type cannot be read properly!"<<endl);
		}
		if (fscanf(client, "%f", &prob) != EOF) {
			LOG_DEBUG("type prob="<<prob<<endl)
		} else {
			LOG_ERROR("type prob cannot be read properly!"<<endl);
		}
		typeProbClient.insert(SSFNET_MAKE_PAIR(type,prob));

	    if (fscanf(client, "%f", &tmp) != EOF) {
			params.push_back(tmp/1000);
			LOG_DEBUG("the link param is: "<<tmp<<endl);
		} else {
			LOG_ERROR("delay cannot be read properly!"<<endl);
		}
		if (fscanf(client, "%f", &tmp) != EOF) {
			params.push_back(tmp*1000000);
			LOG_DEBUG("the link param is: "<<tmp<<endl);
		} else {
			LOG_ERROR("bandwidth cannot be read properly!"<<endl);
		}
		if (fscanf(client, "%d", &psize) != EOF) {
			params.push_back(psize);
			LOG_DEBUG("the pkt size is: "<<psize<<endl);
		} else {
			LOG_ERROR("pkt size cannot be read properly!"<<endl);
		}
		typeParamClient.insert(SSFNET_MAKE_PAIR(type,params));;
	}
	fclose(client);
	client = NULL;
}

void FlowDescription::loadNumRREs() {
	char* num_rres_fn;
	FILE* num_rres;
	int tmp;

	num_rres_fn = traffic_summary[3];
	num_rres = fopen(num_rres_fn, "r");
	if (fscanf(num_rres, "%d", &num_rres_size) != EOF) {
		//printf("num_rres_size is %d\n", num_rres_size);
		num_rres_data = new int[num_rres_size];
	} else {
		LOG_ERROR("The size of the number of rres cannot be read properly!"<<endl);
	}
	for (int j = 0; j < num_rres_size; j++) {
		if (fscanf(num_rres, "%d", &tmp) != EOF) {
			num_rres_data[j] = tmp;
			//printf("rre data is %d\n", num_rres_data[j]);
		} else {
			LOG_ERROR("The number of rres cannot be read properly!"<<endl);
		}
	}
	fclose(num_rres);
	num_rres = NULL;
}

void FlowDescription::loadRREInterval() {
	char* rre_interval_fn;
	FILE* rre_interval;
	float tmp;

	rre_interval_fn = traffic_summary[4];
	rre_interval = fopen(rre_interval_fn, "r");
	if (fscanf(rre_interval, "%d", &rre_interval_size) != EOF) {
		//printf("rre interval size is %d\n", rre_interval_size);
		rre_interval_data = new float[rre_interval_size];
	} else {
		LOG_ERROR("The size of rre intervals cannot be read properly!"<<endl);
	}
	for (int j = 0; j < rre_interval_size; j++) {
		if (fscanf(rre_interval, "%f", &tmp) != EOF) {
			rre_interval_data[j] = tmp;
			//printf("rre interval is %f\n", rre_interval_data[j]);
		} else {
			LOG_ERROR("The rre intervals cannot be read properly!"<<endl);
		}
	}
	fclose(rre_interval);
	rre_interval = NULL;
}

void FlowDescription::loadNumConnections() {
	char* num_connections_fn;
	FILE* num_connections;
	int tmp;

	num_connections_fn = traffic_summary[5];
	num_connections = fopen(num_connections_fn, "r");
	if (fscanf(num_connections, "%d", &num_connections_size) != EOF) {
		//printf("number of connections is %d\n", num_connections_size);
		num_connections_data = new int[num_connections_size];
	} else {
		LOG_ERROR("The size of the number of connections cannot be read properly!"<<endl);
	}
	for (int j = 0; j < num_connections_size; j++) {
		if (fscanf(num_connections, "%d", &tmp) != EOF) {
			num_connections_data[j] = tmp;
			//printf("num_connections_data is %d\n", num_connections_data[j]);
		} else {
			LOG_ERROR("The number of connections cannot be read properly!"<<endl);
		}
	}
	fclose(num_connections);
	num_connections = NULL;
}

void FlowDescription::loadConnectionInterval() {
	char* connection_interval_fn;
	FILE* connection_interval;
	float tmp;

	connection_interval_fn = traffic_summary[6];
	connection_interval = fopen(connection_interval_fn, "r");
	if (fscanf(connection_interval, "%d", &connection_interval_size) != EOF) {
		//printf("connection interval size is %d\n", connection_interval_size);
		connection_interval_data = new float[connection_interval_size];
	} else {
		LOG_ERROR("The size of connection intervals cannot be read properly!"<<endl);
	}
	for (int j = 0; j < connection_interval_size; j++) {
		if (fscanf(connection_interval, "%f", &tmp) != EOF) {
			connection_interval_data[j] = tmp;
			//printf("connection interval is %f\n", connection_interval_data[j]);
		} else {
			LOG_ERROR("The connection intervals cannot be read properly!"<<endl);
		}
	}
	fclose(connection_interval);
	connection_interval = NULL;
}

void FlowDescription::loadServer() {
	LOG_DEBUG("load server"<<endl)
	char* server_fn;
	FILE* server;
	float prob;
	float tmp;
	int psize;
	int type;;
	server_fn = traffic_summary[1];
	LOG_DEBUG("server_fn="<<server_fn<<endl)

	server = fopen(server_fn, "r");

	if (fscanf(server, "%d", &server_size) == EOF) {
		LOG_ERROR("The server size cannot be read properly!"<<endl);
	}

	for (int i = 0; i < server_size; i++) {
		params.clear();
		if (fscanf(server, "%d", &type) == EOF) {
			LOG_ERROR("server type cannot be read properly!"<<endl);
		}
		if (fscanf(server, "%f", &prob) == EOF) {
			LOG_ERROR("type prob cannot be read properly!"<<endl);
		}
		typeProbServer.insert(SSFNET_MAKE_PAIR(type,prob));
		if (fscanf(server, "%f", &tmp) != EOF) {
			params.push_back(tmp/1000);
			//LOG_DEBUG("the link param is: "<<tmp<<endl);
		} else {
			LOG_ERROR("delay cannot be read properly!"<<endl);
		}
		if (fscanf(server, "%f", &tmp) != EOF) {
			params.push_back(tmp*1000000);
			//LOG_DEBUG("the link param is: "<<tmp<<endl);
		} else {
			LOG_ERROR("bandwidth cannot be read properly!"<<endl);
		}
		if (fscanf(server, "%d", &psize) != EOF) {
			params.push_back(psize);
			//LOG_DEBUG("the pkt size is: "<<psize<<endl);
		} else {
			LOG_ERROR("pkt size cannot be read properly!"<<endl);
		}
		typeParamServer.insert(SSFNET_MAKE_PAIR(type,params));
	}

	fclose(server);
	server = NULL;
}

void FlowDescription::loadNumPairs() {
	char* num_pairs_fn;
	FILE* num_pairs;
	int tmp;

	num_pairs_fn = traffic_summary[7];
	num_pairs = fopen(num_pairs_fn, "r");
	if (fscanf(num_pairs, "%d", &num_pairs_size) != EOF) {
		//printf("number of pairs is %d\n", num_pairs_size);
		num_pairs_data = new int[num_pairs_size];
	} else {
		LOG_ERROR("The size of the number of pairs cannot be read properly!"<<endl);
	}
	for (int j = 0; j < num_pairs_size; j++) {
		if (fscanf(num_pairs, "%d", &tmp) != EOF) {
			num_pairs_data[j] = tmp;
			//printf("num_pairs data is %d\n", num_pairs_data[j]);
		} else {
			LOG_ERROR("The number of pairs cannot be read properly!"<<endl);
		}
	}
	fclose(num_pairs);
	num_pairs = NULL;
}

void FlowDescription::loadReqSize() {
	char* req_fn;
	FILE* req;
	int tmp;

	req_fn = traffic_summary[8];
	req = fopen(req_fn, "r");
	if (fscanf(req, "%d", &req_size) != EOF) {
		//printf("number of requests is %d\n", req_size);
		req_data = new int[req_size];
	} else {
		LOG_ERROR("The size of requests cannot be read properly!"<<endl);
	}
	for (int j = 0; j < req_size; j++) {
		if (fscanf(req, "%d", &tmp) != EOF) {
			req_data[j] = tmp;
			//printf("size of request is %d\n", req_data[j]);
		} else {
			LOG_ERROR("Request data cannot be read properly!"<<endl);
		}
	}
	fclose(req);
	req = NULL;
}

void FlowDescription::loadRspSize() {
	char* rsp_fn;
	FILE* rsp;
	int tmp;

	rsp_fn = traffic_summary[9];
	rsp = fopen(rsp_fn, "r");
	if (fscanf(rsp, "%d", &rsp_size) != EOF) {
		//printf("number of rsponses is %d\n", rsp_size);
		rsp_data = new int[rsp_size];
	} else {
		LOG_ERROR("The size of responses cannot be read properly!"<<endl);
	}
	for (int j = 0; j < rsp_size; j++) {
		if (fscanf(rsp, "%d", &tmp) != EOF) {
			rsp_data[j] = tmp;
			//printf("size of response is %d\n", rsp_data[j]);
		} else {
			LOG_ERROR("Responses data cannot be read properly!"<<endl);
		}
	}
	fclose(rsp);
	rsp = NULL;
}

void FlowDescription::loadThinkTime() {
	char* think_time_fn;
	FILE* think_time;
	float tmp;

	think_time_fn = traffic_summary[10];
	think_time = fopen(think_time_fn, "r");
	if (fscanf(think_time, "%d", &think_time_size) != EOF) {
		//printf("size of think time is %d\n", think_time_size);
		think_time_data = new float[think_time_size];
	} else {
		LOG_ERROR("The size of think times cannot be read properly!"<<endl);
	}
	for (int j = 0; j < think_time_size; j++) {
		if (fscanf(think_time, "%f", &tmp) != EOF) {
			think_time_data[j] = tmp;
			//printf("think_time_data is %f\n", think_time_data[j]);
		} else {
			LOG_ERROR("Think times cannot be read properly!"<<endl);
		}
	}
	fclose(think_time);
	think_time = NULL;
}

// ssf requires this macro to register an event class.
SSF_REGISTER_EVENT(SwingStartTrafficEvent, SwingStartTrafficEvent::createSwingStartTrafficEvent);
SSF_REGISTER_EVENT(SwingFinishedTrafficEvent, SwingFinishedTrafficEvent::createSwingFinishedTrafficEvent);

} // namespace ssfnet
} // namespace prime

