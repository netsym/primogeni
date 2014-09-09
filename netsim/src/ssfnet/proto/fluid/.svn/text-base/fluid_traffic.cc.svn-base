/**
 * \file fluid_traffic.cc
 * \brief Source file for the FluidTraffic class.
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

#include <math.h>
#include <limits.h>
#include <string.h>
#include "os/logger.h"
#include "os/traffic_mgr.h"
#include "net/interface.h"
#include "proto/ipv4/ipv4_session.h"
#include "fluid_traffic.h"
#include "fluid_queue.h"
#include "os/partition.h"

#ifdef SSFNET_FLUID_VARYING
#define SSFNET_FLUID_VARYING_WSS 10.0
#ifdef SSFNET_FLUID_DYNAMIC
#define DELTA_WINDOW_THRESHOLD ((shared.pktsiz.read()/unshared.rtt)*timer_stepsize)
#endif
#endif

//#define PRINT_SESS_COUNT
//#define PRINT_PKT_COUNT

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(FluidTraffic)

class FluidStartTimer: public prime::ssf::ssf_timer {
public:
	FluidStartTimer(prime::ssf::Entity* ent, FluidClass* cls) :
		prime::ssf::ssf_timer(ent), fluid_class(cls) {
	}
	virtual void callback() {
		fluid_class->run();
	}
private:
	FluidClass* fluid_class;
};

class FluidStopTimer: public prime::ssf::ssf_timer {
public:
	FluidStopTimer(prime::ssf::Entity* ent, FluidClass* cls) :
		prime::ssf::ssf_timer(ent), fluid_class(cls) {
	}
	virtual void callback() {
		LOG_DEBUG("fluid stop, cur time="<<VirtualTime(prime::ssf::now()).second()<<", class id="<<fluid_class->class_id.first<<"=>"<<fluid_class->class_id.second<<endl)
		TrafficManager* t_mgr=fluid_class->community->getTrafficManager();
		if(t_mgr->getFluidClasses().find(fluid_class->class_id)!=t_mgr->getFluidClasses().end()){
			t_mgr->getFluidClasses().erase(t_mgr->getFluidClasses().find(fluid_class->class_id));
		}
		for (TrafficManager::FLUID_QUEUES::iterator qit = t_mgr->getFluidQueues().begin(); qit!= t_mgr->getFluidQueues().end(); qit++) {
			if((*qit)->getFluidMap().find(fluid_class->class_id)!=(*qit)->getFluidMap().end()){
				(*qit)->getFluidMap().erase((*qit)->getFluidMap().find(fluid_class->class_id));
			}
		}
		delete fluid_class;
#ifdef SSFNET_FLUID_VARYING
		//FIX IT LATER
		if(t_mgr->getFluidClasses().empty()){
			LOG_DEBUG("~~~~deleting timer, there is no fluid classes for this traffic manager."<<endl)
			t_mgr->deleteFluidTimer();
	    }
#endif
	}
private:
	FluidClass* fluid_class;
};

#ifdef SSFNET_FLUID_STOCHASTIC
class SessionStartTimer: public prime::ssf::ssf_timer {
public:
	SessionStartTimer(prime::ssf::Entity* ent, FluidClass* cls) :
		prime::ssf::ssf_timer(ent), fluid_class(cls) {
	}

	virtual void callback() {
		fluid_class->nsessions++;
		LOG_DEBUG("class_id="<<fluid_class->class_id.first<<"=>"<<fluid_class->class_id.second<<", start a new session:"<<fluid_class->nsessions<<endl);
#ifdef PRINT_SESS_COUNT
		printf("uid=%lld %g %d\n", fluid_class->class_id.first, VirtualTime(prime::ssf::now()).second(), fluid_class->nsessions);
#endif
		set();
		fluid_class->sessionStop();
	}

	void set() {
		//LOG_DEBUG("SessionStartTimer::set()"<<endl);
		//double delta = fluid_class->fluid_traffic->getNFlows()*fluid_class->fluid_traffic->get0ffTime()*(2-2*fluid_class->fluid_traffic->getHurst())/(3-2*fluid_class->fluid_traffic->getHurst());
		double lamda = 1 / fluid_class->fluid_traffic->getOffTime();
		VirtualTime t = VirtualTime(fluid_class->rng->exponential(lamda),VirtualTime::SECOND);
		schedule(t);
	}

private:
	FluidClass* fluid_class;
};

class SessionStopTimer: public prime::ssf::ssf_timer {
public:
	SessionStopTimer(prime::ssf::Entity* ent, FluidClass* cls) :
		prime::ssf::ssf_timer(ent), fluid_class(cls) {
	}

	virtual void callback() {
		fluid_class->nsessions--;
		LOG_DEBUG("stop a session: "<<fluid_class->nsessions<<", fluid class: "<<fluid_class->class_id.first<<"=>"<<fluid_class->class_id.second<<endl);
#ifdef PRINT_SESS_COUNT
		printf("uid=%lld %g %d\n", fluid_class->class_id.first, VirtualTime(prime::ssf::now()).second(), fluid_class->nsessions);
#endif
	}

private:
	FluidClass* fluid_class;
};
#endif

FluidProbeHandling::FluidProbeHandling(){}

FluidProbeHandling::~FluidProbeHandling(){}

void FluidProbeHandling::setFluidClass(FluidClass* fs){
	f_class = fs;
}

void FluidProbeHandling::handleProbe(ProbeMessage* pmsg){
	UIDVec nl = pmsg->getNics();
	UIDVec ul = pmsg->getNodeUIDs();
	UIDVec bl = pmsg->getBusIdxes();
	assert(nl.size()==ul.size()); //each outbound interface corresponds to a node
	assert(nl.size()==bl.size());
	for(int i=0; i<(int)ul.size()/2; i++){
		LOG_DEBUG("time="<<VirtualTime(prime::ssf::now()).second()<<", insert forward path for node="<<ul[i]<<", out nic="<<nl[i]<<", bus idx="<<bl[i]<<endl);
		FluidClass::NextHopPair next = SSFNET_MAKE_PAIR(nl[i],bl[i]);
		f_class->forward_nics.insert(SSFNET_MAKE_PAIR(ul[i],next));
	}
	for(int i=ul.size()/2; i<(int)ul.size(); i++){
		LOG_DEBUG("time="<<VirtualTime(prime::ssf::now()).second()<<", insert reverse path for node="<<ul[i]<<", out nic="<<nl[i]<<", bus idx="<<bl[i]<<endl);
		FluidClass::NextHopPair next = SSFNET_MAKE_PAIR(nl[i],bl[i]);
		f_class->reverse_nics.insert(SSFNET_MAKE_PAIR(ul[i],next));
	}
	f_class->startTraffic();
}

FluidTraffic::FluidTraffic(){

}
FluidTraffic::~FluidTraffic(){

}

const char* FluidTraffic::class_type() {
	switch (unshared.protocol_type) {
	case FLUID_TYPE_UDP:
		return "udp";
	case FLUID_TYPE_TCP_RENO:
		return "tcp_reno";
	case FLUID_TYPE_TCP_NEWRENO:
		return "tcp_newreno";
	case FLUID_TYPE_TCP_SACK:
		return "tcp_sack";
	default:
		return "unknown";
	}
}

void FluidTraffic::init() {
	TrafficType::init();
	if(!shared.srcs.read().isCompiled()) {
		shared.srcs.read().evaluate(getParent()->getParent());
	}
	if(!shared.dsts.read().isCompiled()) {
		shared.dsts.read().evaluate(getParent()->getParent());
	}
	unshared.traffic_flows=getTrafficFlowList();
	//check the configurations
	if (strcmp(shared.protocol_type.read().c_str(), "udp") == 0)
		unshared.protocol_type = FLUID_TYPE_UDP;
	else if (strcmp(shared.protocol_type.read().c_str(), "tcp_reno") == 0)
		unshared.protocol_type = FLUID_TYPE_TCP_RENO;
	else if (strcmp(shared.protocol_type.read().c_str(), "tcp_newreno") == 0)
		unshared.protocol_type = FLUID_TYPE_TCP_NEWRENO;
	else if (strcmp(shared.protocol_type.read().c_str(), "tcp_sack") == 0)
		unshared.protocol_type = FLUID_TYPE_TCP_SACK;
	else
		LOG_ERROR("TYPE: "<<shared.protocol_type.read()<<" unrecognized."<<endl);

	if (shared.nflows.read() <= 0)
		LOG_ERROR("NFLOWS: "<<shared.nflows.read()<<" is non-positive."<<endl)

	if (shared.pktsiz.read() <= 0)
		LOG_ERROR("PKTSIZ: "<<shared.pktsiz.read()<<" is non-positive."<<endl)
	else
		unshared.pktsiz = 8.0 * shared.pktsiz.read();

#ifdef SSFNET_FLUID_STOCHASTIC
	if ((shared.hurst.read() <= 0.5) || (shared.hurst.read() >= 1))
		LOG_ERROR("HURST: "<<shared.hurst.read()<<" between 0.5 to 1."<<endl)
#endif

	if (unshared.protocol_type == FLUID_TYPE_UDP) {
		unshared.sendrate = 8.0 * shared.sendrate.read();
		unshared.window = unshared.sendrate;
		if (unshared.window <= 0)
			LOG_ERROR("SENDRATE: "<<shared.sendrate.read()<<" is non-positive."<<endl)
	} else { //tcp version
		if (shared.md.read() <= 0)
			LOG_ERROR("MD: "<<shared.md.read()<<" is non-positive."<<endl)
		unshared.wndmax = unshared.pktsiz * shared.wndmax.read(); //in bits
		if (shared.wndmax.read() <= 0)
			LOG_ERROR("WNDMAX"<<shared.wndmax.read()<<" is non-positive."<<endl)
	}
	if (unshared.monres.read() <= 0)
		LOG_ERROR("MONRES"<<unshared.monres.read()<<" is non-positive."<<endl)

	LOG_DEBUG("Init fluid traffic, "<<"the community id="<<traffic_mgr->community->getCommunityId()<<endl)
	while(!unshared.traffic_flows->empty()){
		if(traffic_mgr->community->getObject(unshared.traffic_flows->front().first)!=NULL){
			FluidClass* fclass=new FluidClass(this, traffic_mgr->community, unshared.traffic_flows->front());
			LOG_DEBUG("Init fluid traffic, community id="<<traffic_mgr->community->getCommunityId()<<", class id: "<<fclass->class_id.first<<"=>"<<fclass->class_id.second<<endl)
			fclass->init();
		}
		unshared.traffic_flows->pop_front();
	}
}

int FluidTraffic::getFluidProtoType(){
	return unshared.protocol_type;
}
int FluidTraffic::getNFlows(){
	return shared.nflows.read();
}
float FluidTraffic::getPktsiz(){
	return unshared.pktsiz;
}
float FluidTraffic::getHurst(){
	return shared.hurst.read();
}
float FluidTraffic::getSendrate(){
	return unshared.sendrate;
}
float FluidTraffic::getWindow(){
	return unshared.window;
}
float FluidTraffic::getMd(){
	return shared.md.read();
}
float FluidTraffic::getWndmax(){
	return unshared.wndmax;
}
float FluidTraffic::getStart(){
	return shared.start.read();
}
float FluidTraffic::getStop(){
	return shared.stop.read();
}
float FluidTraffic::getOffTime(){
	return shared.off_time.read();
}
SSFNET_STRING FluidTraffic::getMonfn(){
	return unshared.monfn.read();
}
float FluidTraffic::getMonres(){
	return unshared.monres.read();
}

FluidTraffic::TrafficFlowList* FluidTraffic::getTrafficFlowList(){
	if (strcmp(shared.mapping.read().c_str(), "all2all") == 0)
		unshared.mapping = ALL2ALL;
	else if (strcmp(shared.mapping.read().c_str(), "one2many") == 0)
		unshared.mapping = ONE2MANY;
	else if (strcmp(shared.mapping.read().c_str(), "many2one") == 0)
		unshared.mapping = MANY2ONE;
	else if (strcmp(shared.mapping.read().c_str(), "one2one") == 0)
		unshared.mapping = ONE2ONE;
	else
		LOG_ERROR("Mapping: "<<shared.mapping.read()<<" unrecognized."<<endl);
	TrafficFlowList* flow_list=new TrafficFlowList;
	UIDVec& src_rids=shared.srcs.read().getCompiledRI()->getUIDVec();
	UIDVec& dst_rids=shared.dsts.read().getCompiledRI()->getUIDVec();
	BaseEntity* anchor=getParent()->getParent();
	switch (unshared.mapping) {
		case ALL2ALL:
		{
			for (UIDVec::iterator i = src_rids.begin(); i!= src_rids.end(); i++) {
				//LOG_DEBUG("src is: "<<(*i)<<endl)
				for (UIDVec::iterator j = dst_rids.begin(); j!= dst_rids.end(); j++) {
					if(*i!=*j){
						//LOG_DEBUG("dst is: "<<(*j)<<endl)
						LOG_DEBUG("src="<<*i<<",dst="<<*j<<endl)
						flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(*i,anchor),CompiledRID::getUID(*j,anchor)));
					}
				}
			}
			break;
		}
		case ONE2MANY:
		{
			if(src_rids.size()>dst_rids.size()){
				LOG_ERROR("The number of srcs should be less than the number of dsts for one2many mapping!"<<endl)
			}
			int many=(int)dst_rids.size()/src_rids.size();
			int base=0;
			int m=0;

			for (UIDVec::iterator i = src_rids.begin(); i!= src_rids.end(); i++) {
				for(m=0; m<many; m++){
					if(base+m<(int)dst_rids.size()) {
						if(*i!=dst_rids[base+m]){
							flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(*i,anchor),CompiledRID::getUID(dst_rids[base+m],anchor)));
						}
					}
				}
				base+=m;
			}
			break;
		}
		case MANY2ONE:
		{
			if(src_rids.size()<dst_rids.size()){
				LOG_ERROR("The number of dsts should be less than the number of srcs for many2one mapping!"<<endl)
			}
			int many=(int)src_rids.size()/dst_rids.size();
			int base=0;
			int m=0;
			for (UIDVec::iterator i = dst_rids.begin(); i!= dst_rids.end(); i++) {
				for(m=0; m<many; m++){
					if(base+m<(int)src_rids.size()) {
						if(*i!=src_rids[base+m]){
							flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(src_rids[base+m],anchor),CompiledRID::getUID(*i,anchor)));
						}
					}
				}
				base=base+m;
			}
			break;
		}
		case ONE2ONE:
		{
			int j=0;
			//for each src, randomly pick a dst from the dst array
			for (UIDVec::iterator i = src_rids.begin(); i!= src_rids.end(); i++) {
				//LOG_DEBUG("src is: "<<(*i)<<endl)
				j=(int)(getRandom()->uniform(0,1)*dst_rids.size());
				//LOG_DEBUG("picked dst is: "<<p[j]<<endl)
				while(*i==dst_rids[j]){
					j=(int)(getRandom()->uniform(0,1)*dst_rids.size());
				}
				LOG_DEBUG("src="<<*i<<",dst="<<dst_rids[j]<<endl)
				flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(*i,anchor),CompiledRID::getUID(dst_rids[j],anchor)));
			}
			break;
		}
		default: {
			LOG_ERROR("Mapping: "<<unshared.mapping<<" unrecognized."<<endl);
		}
	}
	return flow_list;
}

FluidClass::FluidClass(FluidTraffic* ft, Community* comm, FLUID_CLASSID cls_id) :
	fluid_traffic(ft), community(comm), class_id(cls_id), first_hop(0), last_hop(0),
	start_timer(0), stop_timer(0),
#ifdef SSFNET_FLUID_VARYING
	timer_handle(0), timer_stepsize(0),
#endif
#ifdef SSFNET_FLUID_STOCHASTIC
    nsessions(1), rng(0), session_start_timer(0), session_stop_timer(0), //nsessionfd(0)
#endif
	is_on(false), earliest_activity(0), current_activity(0),
    src_uid(0), dst_uid(0), protocol_type(0), nflows(0), pktsiz(0), hurst(0),
    md(0), sendrate(0), wndmax(0), start(0), stop(0), monres(0), moncnt(0),
    rtt(2.0), loss(0), window(0), monfd(0)
	{
}

FluidClass::~FluidClass() {
	if (start_timer) {
		start_timer->cancel();
		delete start_timer;
		start_timer = 0;
	} else {
		// note that the fluid classes are reclaimed later than the fluid
		// queues; in this case, we let the queues to reclaim the hops!
		// assert(first_hop); delete first_hop;

		// it's possible that last_hop is NULL; this happens when
		// FluidClass::config returns non-zero for a remote fluid class
		if (last_hop)
			delete last_hop;
	}

#ifdef SSFNET_FLUID_STOCHASTIC
	if (session_start_timer){
		session_start_timer->cancel();
		delete session_start_timer;
		session_start_timer=0;
	}
	if (session_stop_timer){
		session_stop_timer->cancel();
		delete session_stop_timer;
	}
	if (rng)
		delete rng;
	//if(nsessionfd) fclose(nsessionfd);
#endif

	if (monfd) fclose(monfd);

	while (earliest_activity) {
		current_activity = earliest_activity->next;
		delete earliest_activity;
		earliest_activity = current_activity;
	}
	if(stop_timer){
		stop_timer->cancel();
		delete stop_timer;
	}
	LOG_DEBUG("~FluidClass(), fluid class: "<<class_id.first<<"=>"<<class_id.second<<endl);
}

void FluidClass::init(){
	LOG_DEBUG("init fluid class, the community id="<<fluid_traffic->traffic_mgr->community->getCommunityId()<<endl)
	nflows=fluid_traffic->getNFlows();
	pktsiz=fluid_traffic->getPktsiz();
	protocol_type=fluid_traffic->getFluidProtoType();
#ifdef SSFNET_FLUID_STOCHASTIC
	hurst=fluid_traffic->getHurst();
#endif
	if (fluid_traffic->getFluidProtoType() == FluidTraffic::FLUID_TYPE_UDP) {
		sendrate=fluid_traffic->getSendrate();
		window=fluid_traffic->getWindow();
	} else { //tcp version
		md=fluid_traffic->getMd();
		wndmax=fluid_traffic->getWndmax();
	}

	monres=fluid_traffic->getMonres();

	LOG_DEBUG("start timer, the community id="<<community->getCommunityId()<<endl)

	//Send a probe to get the nics vector of the path from src to dst.
	src_uid = class_id.first;
	dst_uid = class_id.second;
	Host* srchost = SSFNET_DYNAMIC_CAST(Host*,community->getObject(src_uid));
	FluidProbeHandling* ph = new FluidProbeHandling();
	ph->setFluidClass(this);
	dynamic_cast<ProbeSession*>(srchost->sessionForNumber(SSFNET_PROTOCOL_TYPE_PROBE))->startProbe(ph, dst_uid);
}

void FluidClass::startTraffic(){
	start = fluid_traffic->getStart();
	if(start<VirtualTime(prime::ssf::now()).second()) { //wait for the probe ready, XXX
		start=VirtualTime(prime::ssf::now()).second();
	}
	ActivityNode* start_node = new ActivityNode(VirtualTime(start, VirtualTime::SECOND), true);
	insert_activity(start_node);
	stop = fluid_traffic->getStop();
	ActivityNode* stop_node = new ActivityNode(VirtualTime(stop,VirtualTime::SECOND), false);
	insert_activity(stop_node);
	VirtualTime t = check_activities();

	start_timer = new FluidStartTimer(community, this);
	LOG_DEBUG("start_timer->schedule(t)"<<endl);
	start_timer->schedule(t);
	LOG_DEBUG("cur time="<<VirtualTime(prime::ssf::now()).second()<<" stop time is: "<<VirtualTime(stop, VirtualTime::SECOND).second()<<endl)
	stop_timer=new FluidStopTimer(community, this);
	stop_timer->schedule(VirtualTime(stop, VirtualTime::SECOND));
}

void FluidClass::run() {
	LOG_DEBUG("src_uid="<<class_id.first<<", dst="<<class_id.second<<", community id="<<community->getCommunityId()<<endl)
	assert(start_timer);
#ifndef SSFNET_FLUID_VARYING
	delete start_timer;
	start_timer = 0;
#endif
	long curtick = prime::ssf::now() / VirtualTime(community->getTrafficManager()->getStepSize(), VirtualTime::SECOND);

	std::ostringstream osstream1;
	osstream1 << src_uid;
	std::string src = osstream1.str();
	std::ostringstream osstream2;
	osstream2 << dst_uid;
	std::string dst = osstream2.str();
	std::string uidcat = src + dst;
#ifdef SSFNET_FLUID_STOCHASTIC
	int seed = atoi(uidcat.c_str());
	seed=seed+fluid_traffic->getTrafficTypeSeed();
	LOG_DEBUG("src_uid="<<src_uid<<", dst_uid="<<dst_uid<<", seed="<<seed<<endl);
#endif

	if (!community->getTrafficManager()->fluid_classes.insert(SSFNET_MAKE_PAIR(class_id,this)).second) {
		LOG_ERROR("Found duplicate fluid class "<<class_id.first<<"=>"<<class_id.second<<endl)
	}

	LOG_DEBUG("src_uid="<<src_uid<<", community id="<<community->getCommunityId()<<endl)

	Host* srchost = SSFNET_DYNAMIC_CAST(Host*,community->getObject(src_uid));
	//Host* dsthost = SSFNET_DYNAMIC_CAST(Host*,community->getObject(dst_uid));// could be NULL

	assert(srchost);

	// create the monitor file if needed
	if (fluid_traffic->getMonfn() != "") {
		SSFNET_STRING monfn=fluid_traffic->getMonfn()+uidcat;
		monfd = fopen(monfn.c_str(), "w");
		if (monfd < 0) {
			LOG_ERROR("FLUID_CLASS.MONITOR ("<<fluid_traffic->getMonfn()<<") cannot open file (fluid class: "<<class_id.first<<"=>"<<class_id.second<<endl)
		}
		fprintf(monfd, "# AUTOMATICALLY GENERATED, DO NOT HANGE!\n"
			"# FORMAT: TIME CWND RTT LOSS\n");
		fflush(monfd);
	}

	float pathlen = 0; // accumulative path latency
	FluidHop* prehop = 0;
	Host* curhost = srchost;
#ifdef SSFNET_FLUID_ROUNDTRIP
	bool forward_path = true; // we are on the forwarding path
#endif
	for (;;) {
		// if we're at the destination, we find the route back to the
		// source; otherwise, it's the route to destination
#ifdef SSFNET_FLUID_ROUNDTRIP
		if (curhost->getUID() == dst_uid)
			forward_path = false;
		UID_t mydest_uid = forward_path ? dst_uid : src_uid;
#else
		UID_t mydest_uid = (curhost->getUID() == dst_uid) ? src_uid : dst_uid;
#endif
		//find the outbound interface
		int outbound_iface = 0;
		int16_t bus_idx = 0;
#ifdef SSFNET_FLUID_ROUNDTRIP
		if(forward_path){
#endif
			PathMap::iterator it=forward_nics.find(curhost->getUID());
			if (it != forward_nics.end()) {
				LOG_DEBUG("forward nics first="<<it->first<<"second.first="<<it->second.first<<", second.second="<<it->second.second<<endl);
				outbound_iface = it->second.first;
				bus_idx = it->second.second;
			}else{
#ifndef SSFNET_FLUID_ROUNDTRIP
				PathMap::iterator it=reverse_nics.find(curhost->getUID());
				LOG_DEBUG("the size of the reverse nics is "<<reverse_nics.size()<<endl);
				if (it != reverse_nics.end()) {
					LOG_DEBUG("forward nics first="<<it->first<<"second.first="<<it->second.first<<", second.second="<<it->second.second<<endl);
					outbound_iface = it->second.first;
					bus_idx = it->second.second;
				}else{
					LOG_ERROR("No route to dst uid="<<mydest_uid<<" on host uid="
							<<curhost->getUID()<<" (fluid class: "<<class_id.first<<"=>"<<class_id.second<<endl)
				}
#else
				LOG_ERROR("No route to dst uid="<<mydest_uid<<" on host uid="
					<<curhost->getUID()<<" (fluid class: "<<class_id.first<<"=>"<<class_id.second<<endl)
#endif
			}
#ifdef SSFNET_FLUID_ROUNDTRIP
		}else{
			PathMap::iterator it=reverse_nics.find(curhost->getUID());
			if (it != reverse_nics.end()) {
				LOG_DEBUG("forward nics first="<<it->first<<"second.first="<<it->second.first<<", second.second="<<it->second.second<<endl);
				outbound_iface = it->second.first;
				bus_idx = it->second.second;
			}else{
				LOG_ERROR("No route to dst uid="<<mydest_uid<<" on host uid="
					<<curhost->getUID()<<" (fluid class: "<<class_id.first<<"=>"<<class_id.second<<endl)
			}
		}
#endif
		BaseInterface* curnic = NULL;
		curnic = SSFNET_DYNAMIC_CAST(Interface*,curhost->getCommunity()->getObject(outbound_iface+(curhost->getUID()-curhost->getSize())));
		// find the network queue in the outgoing network interface
		assert(curnic);
		FluidQueue* queue = (FluidQueue*) (SSFNET_DYNAMIC_CAST(Interface*,curnic)->getNicQueue());
		LOG_DEBUG("ACK:queue="<<queue<<endl)
		LOG_DEBUG("QUEUE TYPE:"<<queue->type()<<endl)

		if (!queue || queue->type() != FLUID_QUEUE_TYPE) {
			LOG_ERROR("invalid NIC queue in host "<<curhost->getUName()<<" (fluid class: "
					<<class_id.first<<"=>"<<class_id.second<<endl)
		}
		// create the fluid hop
		FluidHop* hop = new FluidHop(queue, class_id, community->getTrafficManager());
		LOG_DEBUG("fluid queue="<<queue<<endl)
		LOG_DEBUG("creating a fluid hop for fluid class "<<class_id.first<<"=>"<<class_id.second
				<<", nic="<<curnic->getUName()<<endl);
		if (curhost == srchost) { // if this is the first hop
			assert(!prehop);
			hop->setFirstHop(); // piece-wise constant time series for the first hop
			first_hop = hop;
			first_hop->setAccuDelay(0, 0/*LONG_MAX*/, true);
			//first_hop->setAccuLoss(0, 0/*LONG_MAX*/, true);

			// no need to set the attributes of the first hop; default will do

			// create the last hop to represent the reverse path; the last
			// hop isn't a ghost node, is the last hop; it uses 0 as its uid.
			UID_t dst_comm_uid=community->getPartition()->uid2communityId(dst_uid);
			last_hop = new FluidHop((UID_t) 0, dst_comm_uid, class_id, community->getTrafficManager());
			LOG_DEBUG("creating the last fluid hop for fluid class "<<class_id.first<<"=>"<<class_id.second<<endl)
			// setting the attributes of the last hop is postponed
		} else {
			assert(prehop);
			prehop->next = hop;

			long adv = curtick + long(ceil(pathlen / community->getTrafficManager()->getStepSize()));
			hop->setArrival(0, adv, true);
			hop->setAccuDelay(pathlen * 2, 0, true); // offset the averaging at time zero
			hop->setAccuDelay(pathlen, adv, true);
			hop->setAccuLoss(0, adv, true);

#ifndef SSFNET_FLUID_ROUNDTRIP
			if(curhost->getUID() == dst_uid) {
				// we are at the destination; we set the one-way path latency
				// to be the propogation delay and set its next hop to be the
				// last hop since we don't model the reverse path
				hop->prop_delay = pathlen;
				hop->next = last_hop;

				// we have the round-trip time
				rtt = 2.0*pathlen;
				long adv = curtick+long(ceil(rtt/community->getTrafficManager()->getStepSize()));
				last_hop->setArrival(0, adv, true);
				last_hop->setAccuDelay(rtt*2, 0, true); // offset the averaging at time zero
				last_hop->setAccuDelay(rtt, adv, true);
				last_hop->setAccuLoss(0, adv, true);

				break; // we are done
			}
#endif
		}
		prehop = hop;
		// accumulating the path latency
		pathlen += hop->prop_delay; //+pktsiz/queue->bitrate;
#ifdef DEBUG
		LOG_DEBUG("accumulating path length for fluid class "<<class_id.first<<"=>"<<class_id.second
				<<": host="<<curhost->getUName()<<" => "<<pathlen<<endl);
#endif
		// move to the next hop
		Link* link = curnic->getLink();
		Community* src_com=SSFNET_DYNAMIC_CAST(Interface*,curnic)->inHost()->getCommunity();
		LinkInfo* link_info=link->getLinkInfo(src_com);
		LinkInfo::RemoteIface** remotes=link_info->getRemoteInterfaces();
		BaseInterface* nextnic=NULL;
		int remote_idx=-1;
		link->getNextHop(curnic, NULL, src_com, nextnic, remote_idx, bus_idx);
		if (nextnic) { // the next hop is within the same community (timeline)
			curnic=nextnic;
			curhost = SSFNET_DYNAMIC_CAST(Interface*,curnic)->inHost();
#ifdef SSFNET_FLUID_ROUNDTRIP
			if (curhost == srchost) {
				hop->next = last_hop;
				// we have the round-trip time
				rtt = pathlen;
				long adv = curtick + long(ceil(rtt
						/ community->getTrafficManager()->getStepSize()));
				last_hop->setArrival(0, adv, true);
				last_hop->setAccuDelay(rtt * 2, 0, true); // offset the averaging at time zero
				last_hop->setAccuDelay(rtt, adv, true);
				last_hop->setAccuLoss(0, adv, true);
				break;
			}
#endif
			// continue the for loop...
		} else {// the next hop is not in the current timeline
			// we need to create a ghost node for the next hop
			// get the uid of the next interface from the remote_idx
			UID_t nextnic_uid = remotes[remote_idx]->iface->getUIDOfRealInterface(); //the uid of the next hop
			UID_t remote_comm_uid = remotes[remote_idx]->remote_com_id;
			LOG_DEBUG("remote_com_id="<<remotes[remote_idx]->remote_com_id<<endl)
			hop = new FluidHop(nextnic_uid, remote_comm_uid, class_id, community->getTrafficManager());
			LOG_DEBUG("creating ghost hop: fluid class "<<class_id.first<<"=>"<<class_id.second<<", host="<<nextnic_uid<<endl);
			prehop->next = hop;
			prehop->next_is_ghost = true;
#ifdef SSFNET_FLUID_ROUNDTRIP
			hop->registerFluidClass(pathlen, curtick, forward_path, forward_nics, reverse_nics);//we need send forward_path value to another alignment
#else
			hop->registerFluidClass(pathlen, curtick, forward_nics, reverse_nics);
#endif
			// more likely than not, a ghost node will be used to represent
			// the last hop at a remote processor; we need to prepare
			// receiving the fluid events targeting the last hop
			last_hop->prepareReceiving();

			// we don't have the round-trip time, but we have a lower bound
			rtt = 2.0 * pathlen;
			long adv = curtick + long(ceil(rtt / community->getTrafficManager()->getStepSize()));
			last_hop->setArrival(0, adv, true);
			last_hop->setAccuDelay(rtt * 2, 0, true); // offset the averaging at time zero
			last_hop->setAccuDelay(rtt, adv, true);
			last_hop->setAccuLoss(0, adv, true);

			break; // we are done
		}
	} // for loop

	if (protocol_type == FluidTraffic::FLUID_TYPE_UDP) {
		LOG_DEBUG("fluid class "<<class_id.first<<"=>"<<class_id.second<<": protocol_type="<<fluid_traffic->class_type()
				<<" nflows="<<nflows<<", pktsiz="<<pktsiz<<" (b) sendrate="<<sendrate<<" (bps)\n"
				<<" rtt="<<rtt<<" (s), loss="<<loss<<", window="<<window<<" (b)."<<endl)
	} else {
		LOG_DEBUG("fluid class "<<class_id.first<<"=>"<<class_id.second<<": protocol_type="<<fluid_traffic->class_type()
				<<" nflows="<<nflows<<", pktsiz="<<pktsiz<<" (b) md="<<md<<", wndmax="<<wndmax
				<<" rtt="<<rtt<<" (s), loss="<<loss<<", window="<<window<<" (b)."<<endl)
	}

#ifdef SSFNET_FLUID_STOCHASTIC
	rng = new prime::rng::Random(prime::rng::Random::USE_RNG_LEHMER, seed);
	rng->uniform();
	nsessions = (int) rng->poisson((double) nflows);
	LOG_DEBUG("nflow="<<nflows<<", nsessions="<<nsessions<<endl)
	/*
	 std::string filename =  "fsessions_";
	 char buf[50]; filename += disp_fluid_class(class_id, buf);
	 nsessionfd = fopen(filename.c_str(),"w");
	 */
	for (int i = 0; i < nsessions; i++)
		sessionStop();
	session_start_timer = new SessionStartTimer(community, this);
	VirtualTime t = VirtualTime(rng->exponential(1 / fluid_traffic->getOffTime()), VirtualTime::SECOND);
	LOG_DEBUG("session_start_timer->schedule(t)"<<endl);
	session_start_timer->schedule(t);
#endif

#ifdef SSFNET_FLUID_VARYING
	timer_stepsize = rtt/SSFNET_FLUID_VARYING_WSS;
	LOG_DEBUG("rtt="<<rtt<<", SSFNET_FLUID_VARYING_WSS="<<SSFNET_FLUID_VARYING_WSS<<",timer_stepsize="<<timer_stepsize
			<<"StepSize="<<community->getTrafficManager()->getStepSize()<<endl)
	if(timer_stepsize < community->getTrafficManager()->getStepSize())
		timer_stepsize = community->getTrafficManager()->getStepSize();
	if(community->getTrafficManager()->getFluidTimer()==0){
		community->getTrafficManager()->setFluidTimer(this, timer_stepsize);
	}else{
		timer_handle = community->getTrafficManager()->schedWindowUpdate(this, timer_stepsize);
	}
	/*
	 if(timer_stepsize <= 0) {
	 LOG_ERROR("invalid timer step size "<<timer_stepsize<<endl);
	 }
	 */
#endif
}

bool FluidTraffic::shouldBeIncludedInCommunity(Community* com) {
	LOG_DEBUG("check community."<<endl)
	if(!shared.srcs.read().isCompiled()) {
		shared.srcs.read().evaluate(getParent()->getParent());
	}
	UIDVec& src_rids=shared.srcs.read().getCompiledRI()->getUIDVec();

	for (UIDVec::iterator i = src_rids.begin(); i!= src_rids.end(); i++) {
		LOG_DEBUG("src="<<(*i)<<endl<<", com="<<com->getCommunityId()<<endl)
		LOG_DEBUG("src is in this comm="<<com->getObject(*i)<<endl)
		if(com->getObject(*i)!=NULL){
			return true;
		}
	}
	return false;
}

#ifdef SSFNET_FLUID_VARYING
void FluidClass::updateWindow()
{
#if 0 /*def SSFNET_FLUID_DYNAMIC*/
	float mywindow = unshared.window;
#endif
	windowEvolution(timer_stepsize);

	// FIXME: this shall be merged with the timer scheduling; also UDP
	// needs not further window update!
	if(!first_hop) return;

	if(protocol_type == FluidTraffic::FLUID_TYPE_UDP) {
		// udp uses window as send rate
		first_hop->setArrival(window, community->getTrafficManager()->curstep);
	} else {
#ifdef SSFNET_FLUID_STOCHASTIC
		first_hop->setArrival(window*nsessions/rtt, community->getTrafficManager()->curstep);

#else
		first_hop->setArrival(window*nflows/rtt, community->getTrafficManager()->curstep);
#endif
	}

	FluidHop* lasthop = last_hop;
	assert(lasthop && !lasthop->fluid_queue);
	rtt = lasthop->getAccuDelay(community->getTrafficManager()->curstep);
#ifdef SSFNET_FLUID_STOCHASTIC
	loss = lasthop->getAccuLoss(community->getTrafficManager()->curstep)/nsessions/pktsiz;
#else
	loss = lasthop->getAccuLoss(community->getTrafficManager()->curstep)/nflows/pktsiz;
#endif

	if(protocol_type == FluidTraffic::FLUID_TYPE_TCP_NEWRENO ||
			protocol_type == FluidTraffic::FLUID_TYPE_TCP_SACK) {
		// we need to compute the effective loss rate for newreno and sack
		float a = (last_hop->getArrival(community->getTrafficManager()->curstep)+
				last_hop->getAccuLoss(community->getTrafficManager()->curstep))/
#ifdef SSFNET_FLUID_STOCHASTIC
		nsessions/pktsiz;
#else
		nflows/pktsiz;
#endif
		loss = (1-pow(1-loss/a, rtt*a))/rtt;
	}

	LOG_DEBUG("Current time: "<<VirtualTime(prime::ssf::now()).second()
			<<", fluid class "<<class_id.first<<"=>"<<class_id.second
			<<", rtt="<<rtt<<", loss="<<loss<<endl)

#if 0 /*def SSFNET_FLUID_DYNAMIC*/
	float dw = window-mywindow; if(dw<0) dw = -dw;
	if(dw > 2*DELTA_WINDOW_THRESHOLD) {
		if(timer_stepsize > community->getTrafficManager()->getStepSize()) {
			community->getTrafficManager()->unsched_window_update(timer_handle, this);
			timer_stepsize *= (DELTA_WINDOW_THRESHOLD/dw);
			if(timer_stepsize < community->getTrafficManager()->getStepSize();
					timer_stepsize = community->getTrafficManager()->getStepSize();
					timer_handle = community->getTrafficManager()->sched_window_update(this, timer_stepsize);
				}
			} else if(dw < 0.5*DELTA_WINDOW_THRESHOLD) {
				if(timer_stepsize < rtt/10) {
					community->getTrafficManager()->unsched_window_update(timer_handle, this);
					timer_stepsize *= 2;
					timer_handle = community->getTrafficManager()->sched_window_update(this, timer_stepsize);
				}
			}
#endif
			/*
			 if(rtt > 2*timer_stepsize*SSFNET_FLUID_VARYING_WSS ||
			     rtt < 0.5*timer_stepsize*SSFNET_FLUID_VARYING_WSS) {
				 timer_stepsize = rtt/SSFNET_FLUID_VARYING_WSS;
				 timer_handle = community->getTrafficManager()->sched_window_update(this, timer_stepsize);
				 if(timer_stepsize <= 0) {
					LOG_ERROR("invalid timer step size "<<timer_stepsize<<endl);
				 }
			 } else community->getTrafficManager()->sched_window_update(this, timer_handle);
			 */
		}
#endif

void FluidClass::windowEvolution(float step_size) {
	// it's possible this method is called before the start timer expires
	assert(start_timer || (first_hop && last_hop));
	if (activity_on()) {
		// udp is unresponsive traffic; we don't change send rate
		if (protocol_type == FluidTraffic::FLUID_TYPE_UDP) {
			window = sendrate;
			return;
		}

		/* dW_i(t)/dt = 1(W_i(t)<M_i)/R_i(t)-W_i(t)/MD*lambda_i(t) */
#if 0 //REDMIX_EXPERIMENTAL
		float x = 1.0-1.0/pow(tcp.md, loss);
		float dw0 = pktsiz/rtt-window*x;
		float dw1 = pktsiz/rtt-(window+step_size*dw0/2)*x;
		float dw2 = pktsiz/rtt-(window+step_size*dw1/2)*x;
		float dw3 = pktsiz/rtt-(window+step_size*dw2)*x;
#else
		float dw0 = pktsiz / rtt - window * loss / md;
		float dw1 = pktsiz / rtt - (window + step_size * dw0 / 2) * loss / md;
		float dw2 = pktsiz / rtt - (window + step_size * dw1 / 2) * loss / md;
		float dw3 = pktsiz / rtt - (window + step_size * dw2) * loss / md;
#endif
		window = window + (dw0 + 2* dw1 + 2* dw2 + dw3) * step_size / 6;
		if (window < 0)
			window = 0;
		else if (window > wndmax)
			window = wndmax;
	} else
		window = 0;
#ifdef DEBUG
	LOG_DEBUG("cur time: "<<VirtualTime(prime::ssf::now()).second()<<", (fluid class "
			<<class_id.first<<"=>"<<class_id.second<<"), wnd="<<window
			<<" (rtt="<<rtt<<", loss="<<loss<<")."<<endl)
#endif
#ifdef PRINT_PKT_COUNT
#ifdef SSFNET_FLUID_STOCHASTIC
	printf("uid=%lld %g %g %d %g\n", class_id.first,
			VirtualTime(prime::ssf::now()).second(), window, nsessions, rtt);
#endif
#endif

	if (monfd && long(VirtualTime(prime::ssf::now()).second()/ monres + 0.5) >= moncnt) {
		moncnt++;
		if (activity_on()) {
			fprintf(monfd, "%g %g %g %g\n", VirtualTime(
					prime::ssf::now()).second(), window, rtt,
					loss);
			fflush(monfd);
		}
	}
}

int FluidClass::getFluidProtoType() {
	return protocol_type;
}

void FluidClass::setRtt(float r) {
	rtt = r;
}

void FluidClass::setLoss(float l) {
	loss = l;
}

int FluidClass::getNFlows() {
	return nflows;
}

float FluidClass::getPktsiz() {
	return pktsiz;
}

float FluidClass::getWindow() {
	return window;
}

float FluidClass::getRtt() {
	return rtt;
}

float FluidClass::getLoss() {
	return loss;
}

void FluidClass::insert_activity(ActivityNode* node) {
	ActivityNode* pre = 0;
	ActivityNode* cur = earliest_activity;
	//LOG_DEBUG(node->time<<endl)
	/*if(cur!=0){
		LOG_DEBUG("cur="<<cur<<", cur time="<<cur->time.second()<<", node time="<<node->time.second()<<endl)
	}*/
	//LOG_DEBUG("earliest_activity="<<earliest_activity<<",cur="<<cur<<endl)
	while (cur && cur->time <= node->time) {
		if (cur->time == node->time)
			LOG_ERROR("ERROR: duplicate START or STOP time.\n");
		pre = cur;
		cur = cur->next;
	}
	//LOG_DEBUG("pre="<<pre<<", cur="<<cur<<", node="<<node<<endl)
	if (pre) {
		node->next = cur;
		pre->next = node;
	} else {
		node->next = cur;
		if (pre)
			pre->next = node;
		else
			earliest_activity = node;
	}
	//LOG_DEBUG("node next="<<node->next<<", earliest="<<earliest_activity<<endl)
}

VirtualTime FluidClass::check_activities() {
	if (!earliest_activity)
		LOG_ERROR("missing FLUID_TRAFFIC.START."<<endl);
	bool now_on = false;
	ActivityNode* p = earliest_activity;
	while (p) {
		if ((now_on && p->is_start) || (!now_on && !p->is_start)) {
			LOG_ERROR("invalid FLUID_TRAFFIC.START and STOP sequence."<<endl);
		} else {
			now_on = p->is_start;
		}
		p = p->next;
	}
	//LOG_DEBUG(" earliest time="<<earliest_activity->time.second()<<"earliest next="<<earliest_activity->next<<endl)
	return earliest_activity->time;
}

bool FluidClass::activity_on() {
	VirtualTime now(prime::ssf::now());
	ActivityNode* cur = current_activity ? current_activity->next : earliest_activity;
	while (cur && cur->time <= now) {
		current_activity = cur;
		cur = cur->next;
		is_on = !is_on;
	}
	return is_on;
}

#ifdef SSFNET_FLUID_STOCHASTIC
void FluidClass::sessionStop() {
	LOG_DEBUG("class_id="<<class_id.first<<"=>"<<class_id.second<<", stop a session."<<endl);
	session_stop_timer = new SessionStopTimer(community, this);
	double delta = nflows * fluid_traffic->getOffTime() * (2 - 2*hurst) / (3 - 2* hurst);
	VirtualTime t = VirtualTime(rng->pareto(delta, (double) (3 - 2*hurst)), VirtualTime::SECOND);
	session_stop_timer->schedule(t);
}
#endif

}
; // namespace ssfnet
}
; // namespace prime
