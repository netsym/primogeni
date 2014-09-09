/**
 * \file traffic.cc
 * \brief Source file for the Traffic class.
 * \author Nathanael Van Vorst
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

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <iostream>
#include <iterator>

#include "os/config_vars.h"
#include "os/logger.h"
#include "os/ssfnet_exception.h"
#include "os/community.h"
#include "os/timer_queue.h"
#include "os/partition.h"
#include "os/protocol_session.h"
#include "net/net.h"
#include "os/traffic.h"
#include "proto/routing_protocol.h"
#include "proto/ipv4/ipv4_session.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(Traffic)

ProbeHandling::ProbeHandling(){}

ProbeHandling::~ProbeHandling(){}

void ProbeHandling::handleProbe(ProbeMessage* pmsg){
		ssfnet_throw_exception(SSFNetException::must_implement_exception, " handleProbe(ProbeMessage* pmsg)");
};

TrafficEvent::TrafficEvent(SSFNetEvent::Type evt_type) : SSFNetEvent(evt_type), traffic_type(0) {}

TrafficEvent::TrafficEvent(UID_t t_id, SSFNetEvent::Type evt_type, int target_community_id):
		SSFNetEvent(evt_type, target_community_id), traffic_type(0), traffic_type_id(t_id) {}

TrafficEvent::TrafficEvent(TrafficType* t_type, SSFNetEvent::Type evt_type, int target_community_id):
		SSFNetEvent(evt_type, target_community_id), traffic_type(t_type), traffic_type_id(t_type->getUID()) {}

TrafficEvent::TrafficEvent(TrafficEvent* evt):
		SSFNetEvent(*evt), traffic_type(evt->traffic_type), traffic_type_id(evt->traffic_type_id)  {
}
TrafficEvent::TrafficEvent(const TrafficEvent& evt):
		SSFNetEvent(evt), traffic_type(evt.traffic_type), traffic_type_id(evt.traffic_type_id)  {

}
TrafficEvent::~TrafficEvent(){

}
void TrafficEvent::setTrafficTypeUID(UID_t uid){
	traffic_type_id=uid;
	if(traffic_type && traffic_type->getUID() != uid)
		traffic_type_id=0;
}

void TrafficEvent::setTrafficType(TrafficType* tt){
	traffic_type=tt;
	traffic_type_id=tt->getUID();
}

TrafficType* TrafficEvent::getTrafficType(){
    return traffic_type;
}

UID_t TrafficEvent::getTrafficTypeUID(){
    return traffic_type_id;
}
prime::ssf::ssf_compact* TrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_long_long(traffic_type_id);
	return dp;
}

void TrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	SSFNetEvent::unpack(dp);
	traffic_type=NULL;
	dp->get_unsigned_long_long(&traffic_type_id,1);
}

StartTrafficEvent::StartTrafficEvent():
		TrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_START_EVT),
		traffic_id(0), host_uid(0), dst_uid(0), dst_ip(0), start_time(0), src_community_id(-1) {
}

StartTrafficEvent::StartTrafficEvent(SSFNetEvent::Type evt_type):
				TrafficEvent(evt_type),traffic_id(0), host_uid(0), dst_uid(0), dst_ip(0), start_time(0), src_community_id(-1) {
}

StartTrafficEvent::StartTrafficEvent(SSFNetEvent::Type evt_type, UID_t trafficTypeUID, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        TrafficEvent(trafficTypeUID, evt_type, -1), traffic_id(id),
                        host_uid(h_uid), dst_uid(d_uid), dst_ip(d_ip),start_time(st), src_community_id(-1) {
}
StartTrafficEvent::StartTrafficEvent(SSFNetEvent::Type evt_type, TrafficType* trafficType, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        TrafficEvent(trafficType, evt_type, -1), traffic_id(id),
                        host_uid(h_uid), dst_uid(d_uid), dst_ip(d_ip), start_time(st), src_community_id(-1) {
}
StartTrafficEvent::StartTrafficEvent(StartTrafficEvent* evt) :
        TrafficEvent(evt), traffic_id(evt->traffic_id), host_uid(evt->host_uid), dst_uid(evt->dst_uid), dst_ip(evt->dst_ip),
        start_time(evt->start_time), src_community_id(evt->src_community_id){
}
StartTrafficEvent::StartTrafficEvent(const StartTrafficEvent& evt) :
		TrafficEvent(evt), traffic_id(evt.traffic_id), host_uid(evt.host_uid), dst_uid(evt.dst_uid), dst_ip(evt.dst_ip),
		start_time(evt.start_time), src_community_id(evt.src_community_id){
}
StartTrafficEvent::~StartTrafficEvent(){
}
prime::ssf::ssf_compact* StartTrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = TrafficEvent::pack();
	dp->add_unsigned_long_long(traffic_id);
	dp->add_unsigned_long_long(host_uid);
	dp->add_unsigned_long_long(dst_uid);
	dp->add_unsigned_int(dst_ip);
	dp->add_double(start_time.nanosecond());
	dp->add_int(src_community_id);
	return dp;
}

void StartTrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	TrafficEvent::unpack(dp);
	dp->get_unsigned_long_long(&traffic_id,1);
	dp->get_unsigned_long_long(&host_uid,1);
	dp->get_unsigned_long_long(&dst_uid,1);
	dp->get_unsigned_int(&dst_ip,1);
	double t;
	dp->get_double(&t,1);
	start_time=VirtualTime(t,VirtualTime::NANOSECOND);
	dp->get_int(&src_community_id,1);
}

void StartTrafficEvent::setTrafficId(uint64_t t_id){
        traffic_id=t_id;
}

void StartTrafficEvent::setHostUID(UID_t h_uid){
        host_uid=h_uid;
}

void StartTrafficEvent::setDstUID(UID_t d_uid){
        dst_uid=d_uid;
}

void StartTrafficEvent::setDstIP(uint32_t d_ip){
        dst_ip=d_ip;
}

void StartTrafficEvent::setStartTime(VirtualTime st){
        start_time=st;
}

void StartTrafficEvent::setSrcCommunityID(int src_comm_id){
        src_community_id=src_comm_id;
}

uint64_t StartTrafficEvent::getTrafficId(){
        return traffic_id;
}

UID_t StartTrafficEvent::getHostUID(){
        return host_uid;
}

UID_t StartTrafficEvent::getDstUID(){
        return dst_uid;
}

uint32_t StartTrafficEvent::getDstIP(){
        return dst_ip;
}

VirtualTime StartTrafficEvent::getStartTime(){
        return start_time;
}

int StartTrafficEvent::getSrcCommunityID(){
        return src_community_id;
}

prime::ssf::Event* StartTrafficEvent::createStartTrafficEvent(prime::ssf::ssf_compact* dp){
	StartTrafficEvent* t_evt = new StartTrafficEvent();
    t_evt->unpack(dp);
    return t_evt;
}


FinishedTrafficEvent::FinishedTrafficEvent():
		TrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_FINISH_EVT){
}

FinishedTrafficEvent::FinishedTrafficEvent(SSFNetEvent::Type evt_type):
		TrafficEvent(evt_type){
}

FinishedTrafficEvent::FinishedTrafficEvent(SSFNetEvent::Type evt_type, UID_t trafficTypeUID, int target_community_id, uint32_t id) :
        TrafficEvent(trafficTypeUID, evt_type, target_community_id), traffic_id(id) {
}

FinishedTrafficEvent::FinishedTrafficEvent(SSFNetEvent::Type evt_type, TrafficType* trafficType, int target_community_id, uint32_t id) :
        TrafficEvent(trafficType, evt_type, target_community_id), traffic_id(id) {
}

FinishedTrafficEvent::FinishedTrafficEvent(FinishedTrafficEvent* evt) :
        TrafficEvent(evt),traffic_id(evt->traffic_id) {
}

FinishedTrafficEvent::FinishedTrafficEvent(const FinishedTrafficEvent& evt) :
        TrafficEvent(evt),traffic_id(evt.traffic_id) {
}

FinishedTrafficEvent::~FinishedTrafficEvent(){

}

prime::ssf::ssf_compact* FinishedTrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = TrafficEvent::pack();
	dp->add_unsigned_long_long(traffic_id);
	return dp;
}

void FinishedTrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	TrafficEvent::unpack(dp);
	dp->get_unsigned_long_long(&traffic_id,1);
}

uint64_t FinishedTrafficEvent::getTrafficId(){
        return traffic_id;
}

prime::ssf::Event* FinishedTrafficEvent::createFinishedTrafficEvent(prime::ssf::ssf_compact* dp){
	FinishedTrafficEvent* t_evt = new FinishedTrafficEvent();
    t_evt->unpack(dp);
    return t_evt;
}


UpdateTrafficTypeEvent::UpdateTrafficTypeEvent():
		TrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_UPDATE_EVT){
	populateMap();
}
UpdateTrafficTypeEvent::UpdateTrafficTypeEvent(SSFNetEvent::Type evt_type, TrafficType* trafficType, int target_community_id) :
        TrafficEvent(trafficType,evt_type, target_community_id) {
	populateMap();
}
UpdateTrafficTypeEvent::UpdateTrafficTypeEvent(UpdateTrafficTypeEvent* evt) :
        TrafficEvent(evt) {
	populateMap();
}
UpdateTrafficTypeEvent::UpdateTrafficTypeEvent(const UpdateTrafficTypeEvent& evt) :
        TrafficEvent(evt) {
	populateMap();
}

UpdateTrafficTypeEvent::~UpdateTrafficTypeEvent(){
}

void UpdateTrafficTypeEvent::populateMap() {
	BaseConfigVar::AttrMap& bm = this->getTrafficType()->getStateMap()->getBackingMap();
	SSFNET_STRING temp;
	for(BaseConfigVar::AttrMap::iterator i = bm.begin();i!=bm.end();i++) {
		temp.clear();
		i->second->ext_read(temp);
		values.insert(SSFNET_MAKE_PAIR(i->first,temp));
	}
}


prime::ssf::ssf_compact* UpdateTrafficTypeEvent::pack(){
	prime::ssf::ssf_compact* dp = TrafficEvent::pack();
	dp->add_int(values.size());
	for(VarIdValueMap::iterator i =values.begin();
			i!= values.end();i++) {
		dp->add_int(i->first);
		dp->add_int(i->second.length());
		dp->add_char_array(i->second.length(),const_cast<char*>(i->second.c_str()));
	}
	return dp;
}

void UpdateTrafficTypeEvent::unpack(prime::ssf::ssf_compact* dp){
	TrafficEvent::unpack(dp);
	int num_vars=0;
	dp->get_int(&num_vars);
	for(int i=0;i<num_vars;i++) {
		int var_id;
		int len;
		char* str;
		dp->get_int(&var_id);
		dp->get_int(&len);
		str=new char[len+1];
		dp->get_char(str,len);
		str[len]='\0';
		values.insert(SSFNET_MAKE_PAIR(var_id,SSFNET_STRING(str)));
		delete str;
	}
}

prime::ssf::Event* UpdateTrafficTypeEvent::createUpdateTrafficTypeEvent(prime::ssf::ssf_compact* dp){
	UpdateTrafficTypeEvent* t_evt = new UpdateTrafficTypeEvent();
    t_evt->unpack(dp);
    return t_evt;
}

template<> bool rw_config_var<SSFNET_STRING, TrafficType::CommunityIDList> (
                SSFNET_STRING& dst, TrafficType::CommunityIDList& src) {
        PRIME_STRING_STREAM s;
        s << "[";
        for (TrafficType::CommunityIDList::iterator i = src.begin(); i != src.end(); i++) {
                s << *i;
        }
        s << "]";
        dst.append(s.str().c_str());
        return false;
}

template<> bool rw_config_var<TrafficType::CommunityIDList, SSFNET_STRING> (
                TrafficType::CommunityIDList& dst, SSFNET_STRING& src) {
        if (src.length() <= 1) {
                LOG_ERROR("Invalid CommunityIDList "<<src<<endl)
        }
        int ll;
        PRIME_STRING_STREAM s(src.c_str());

        if (s.get() != '[') {
                LOG_ERROR("Invalid CommunityIDList '"<<src<<"'"<<endl)
        }
        while (s.good()) {
                switch (s.peek()) {
                case ']':
                        return false;
                case ',':
                        s.get();
                default:
                        s >> ll;
                        dst.push_back(ll);
                }
        }
        LOG_ERROR("Invalid CommunityIDList "<<src<<endl)
        return true;
}

TrafficType::TrafficType() {
}

TrafficType::~TrafficType() {
	if (unshared.rng)
		delete unshared.rng;
}

void TrafficType::init() {
	// traffic_type_seed is zero if all traffic types share the same unshared.rng;
	// otherwise, each traffic type has its own unshared.rng (with a seed calculated
	// from the traffic_type_seed initiated in the following).
	int seed;
	seed = (int) getUID();
	unshared.rng_seed = shared.traffic_type_seed.read();
	if (seed)
		unshared.rng_seed += 13* (17 +seed);
		// seed the random stream by the default ip address
	//std::cout<<"traffic_type_seed="<<shared.traffic_type_seed.read()<<", unshared.rng_seed="<<unshared.rng_seed<<endl;
	unshared.rng=new prime::rng::Random(prime::rng::Random::RNG_DASSF_LEHMER, unshared.rng_seed);
    // we must scramble the random numbers!!!
    unshared.rng->uniform();
}

void TrafficType::wrapup() {
}

bool TrafficType::shouldBeIncludedInCommunity(Community* comm) {
	ssfnet_throw_exception(SSFNetException::must_implement_exception, " shouldBeIncludedInCommunity()");
	return false;
}

void TrafficType::getNextEvent(StartTrafficEvent*& traffics_to_start,
								UpdateTrafficTypeEvent*& updateEvent,
								bool& wrap_up,
								VirtualTime& recall_at) {
	ssfnet_throw_exception(SSFNetException::must_implement_exception, " getNextEvent()");
}

ConfigType* TrafficType::getProtocolType() {
	ssfnet_throw_exception(SSFNetException::must_implement_exception, " getProtocolType()");
	return NULL;
}

UID_t& TrafficType::selectUIDFromList(UIDVec& list) {
	//pick one from the list uniformly
	//LOG_DEBUG("list size is: "<<list.size()<<endl)
	int i=(int)(getRandom()->uniform(0,1)*list.size());
	//LOG_DEBUG("selected uid is: "<<list[i]<<endl)
	return list[i];
}

Partition* TrafficType::getPartition(){
	return Partition::getInstance();
}

TrafficType::CommunityIDList& TrafficType::getCommunityIDList(){
	return unshared.community_ids.read();
}

SSFNET_VECTOR(SSFNET_STRING) tokenize_str(const SSFNET_STRING & str, const SSFNET_STRING & delims = ", \t[]") {
	//http://rosettacode.org/wiki/Tokenize_a_string#C.2B.2B
	// Skip delims at beginning, find start of first token
	SSFNET_STRING::size_type lastPos = str.find_first_not_of(delims, 0);
	// Find next delimiter @ end of token
	SSFNET_STRING::size_type pos = str.find_first_of(delims, lastPos);

	// output vector
	SSFNET_VECTOR(SSFNET_STRING) tokens;

	while (SSFNET_STRING::npos != pos || SSFNET_STRING::npos != lastPos) {
		// Found a token, add it to the vector.
		tokens.push_back(str.substr(lastPos, pos - lastPos));
		// Skip delims.  Note the "not_of". this is beginning of token
		lastPos = str.find_first_not_of(delims, pos);
		// Find next delimiter at end of token.
		pos = str.find_first_of(delims, lastPos);
	}

	return tokens;
}

template<> bool rw_config_var<SSFNET_STRING, DstIPVec>(SSFNET_STRING& dst,
		DstIPVec& src) {
	PRIME_STRING_STREAM s;
        bool first = true;
        for (DstIPVec::iterator i = src.begin(); i != src.end(); i++) {
    		if (!first){
    			s << ",";
    		}
    		first=false;
            s << *i;
        }
        dst.append(s.str().c_str());
        return false;
}

template<> bool rw_config_var<DstIPVec, SSFNET_STRING> (DstIPVec& dst, SSFNET_STRING& src) {
        if (src.length() > 1) {
            PRIME_STRING_STREAM s(src.c_str());
            vector<string> v(tokenize_str(src));
            for (int i  = 0; i < (int)v.size(); i++){
            	dst.push_back(v[i]);
            }
        }
        return false;
}

StaticTrafficType::StaticTrafficType(){

}
StaticTrafficType::~StaticTrafficType(){

}
void StaticTrafficType::init(){
	TrafficType::init();
	if(!shared.srcs.read().isCompiled()) {
		shared.srcs.read().evaluate(getParent()->getParent());
	}
	if(!shared.dsts.read().isCompiled()) {
		shared.dsts.read().evaluate(getParent()->getParent());
	}
	if(shared.dst_ips.read().size()==0){
		unshared.traffic_flows=getTrafficFlowList();
	}else{
		if(shared.dsts.read().getCompiledRI()->getUIDVec().size()>0){
			LOG_WARN("Dst_ips has been set, dst uids will be ignored. "<<endl);
		}
		unshared.hybrid_traffic_flows=getHybridTrafficFlowList();
	}
	for (DstIPVec::iterator i =shared.dst_ips.read().begin(); i!=shared.dst_ips.read().end(); i++) {
		std::cout<<"Dst ip is "<<*i<<endl;
	}
}

int StaticTrafficType::getOwningCommunityId(){
	CommunityIDList& coms = this->getCommunityIDList();
	int idx=-1,t=0,rv=-1;
	switch(coms.size()) {
	case 0:
		LOG_ERROR("How did this happen?"<<endl);
		break;
	case 1:
		idx=0;
		break;
	default:
		idx=coms.size()/2;
		break;
	}
	SSFNET_STRING ack;
	char ack1[100];
	for(CommunityIDList::iterator i = coms.begin();i!=coms.end();i++) {
		if(ack.length()>0)ack.append(", ");
		sprintf(ack1,"%d",*i);
		ack.append(ack1);
		if(idx == t)
			rv=*i;
		t++;
	}
	if(rv==-1)
		LOG_ERROR("how did this happen?\n");
	LOG_INFO(getUName()<<" assigning to com "<<rv<<", coms=["<<ack<<"]"<<endl);
	return rv;
}

bool StaticTrafficType::shouldBeIncludedInCommunity(Community* comm){
		LOG_DEBUG("this community id="<<comm->getCommunityId()<<"owning community id="<<getOwningCommunityId()<<endl)
        if(comm->getCommunityId()==getOwningCommunityId())
                return true;
        return false;
}


void StaticTrafficType::getNextEvent(StartTrafficEvent*& traffics_to_start,
						bool& wrap_up,
                        VirtualTime& recall_at){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " getNextEvent()");
}

ConfigType* StaticTrafficType::getProtocolType(){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " getProtocolType()");
        return NULL;
}

float StaticTrafficType::getStartTime(){
	return shared.start_time.read();
}

StaticTrafficType::TrafficFlowList* StaticTrafficType::getTrafficFlowList(){
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
	UIDVec& src_rids=shared.srcs.read().getCompiledRI()->getUIDVec();
	UIDVec& dst_rids=shared.dsts.read().getCompiledRI()->getUIDVec();
	BaseEntity* anchor=getParent()->getParent();
	TrafficFlowList* flow_list=new TrafficFlowList;
	switch (unshared.mapping) {
		case ALL2ALL:
		{
			for (UIDVec::iterator i =src_rids.begin(); i!=src_rids.end(); i++) {
				//LOG_DEBUG("src is: "<<(*i)<<endl)
				for (UIDVec::iterator j =dst_rids.begin(); j!=dst_rids.end(); j++) {
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

			for (UIDVec::iterator i =src_rids.begin(); i!=src_rids.end(); i++) {
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
			for (UIDVec::iterator i =dst_rids.begin(); i!=dst_rids.end(); i++) {
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
			for (UIDVec::iterator i =src_rids.begin(); i!=src_rids.end(); i++) {
				//LOG_DEBUG("src is: "<<(*i)<<endl)
				j=(int)(getRandom()->uniform(0,1)*dst_rids.size());
				//LOG_DEBUG("picked dst is: "<<p[j]<<endl)
				while(*i==dst_rids[j]){
					j=(int)(getRandom()->uniform(0,1)*dst_rids.size());
				}
				//LOG_DEBUG("src="<<*i<<",dst="<<dst_rids[j]<<endl)
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

StaticTrafficType::HybridTrafficFlowList* StaticTrafficType::getHybridTrafficFlowList(){
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

	UIDVec& src_rids=shared.srcs.read().getCompiledRI()->getUIDVec();
	DstIPVec& dst_ips=shared.dst_ips.read();
	BaseEntity* anchor=getParent()->getParent();
	HybridTrafficFlowList* flow_list=new HybridTrafficFlowList;
	switch (unshared.mapping) {
		case ALL2ALL:
		{
			for (UIDVec::iterator i =src_rids.begin(); i!=src_rids.end(); i++) {
				for (DstIPVec::iterator j =dst_ips.begin(); j!=dst_ips.end(); j++) {
						LOG_DEBUG("src="<<*i<<",dst="<<*j<<endl)
						flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(*i,anchor),*j));
				}
			}
			break;
		}
		case ONE2MANY:
		{
			if(src_rids.size()>dst_ips.size()){
				LOG_ERROR("The number of srcs should be less than the number of dsts for one2many mapping!"<<endl)
			}
			int many=(int)dst_ips.size()/src_rids.size();
			int base=0;
			int m=0;

			for (UIDVec::iterator i =src_rids.begin(); i!=src_rids.end(); i++) {
				for(m=0; m<many; m++){
					if(base+m<(int)dst_ips.size()) {
						flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(*i,anchor),dst_ips[base+m]));
					}
				}
				base+=m;
			}
			break;
		}
		case MANY2ONE:
		{
			if(src_rids.size()<dst_ips.size()){
				LOG_ERROR("The number of dsts should be less than the number of srcs for many2one mapping!"<<endl)
			}
			int many=(int)src_rids.size()/dst_ips.size();
			int base=0;
			int m=0;
			for (DstIPVec::iterator i =dst_ips.begin(); i!=dst_ips.end(); i++) {
				for(m=0; m<many; m++){
					if(base+m<(int)src_rids.size()) {
						flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(src_rids[base+m],anchor),*i));
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
			for (UIDVec::iterator i =src_rids.begin(); i!=src_rids.end(); i++) {
				j=(int)(getRandom()->uniform(0,1)*dst_ips.size());
				//LOG_DEBUG("src="<<*i<<",dst="<<dst_rids[j]<<endl)
				flow_list->push_back(SSFNET_MAKE_PAIR(CompiledRID::getUID(*i,anchor),dst_ips[j]));
			}
			break;
		}
		default: {
			LOG_ERROR("Mapping: "<<unshared.mapping<<" unrecognized."<<endl);
		}
	}
	return flow_list;
}

DynamicTrafficType::DynamicTrafficType(){

}
DynamicTrafficType::~DynamicTrafficType(){

}
void DynamicTrafficType::init(){

}

void DynamicTrafficType::getNextEvent(StartTrafficEvent*& traffics_to_start,
						UpdateTrafficTypeEvent*& update_event,
                        bool& wrap_up,
                        VirtualTime& recall_at
                        ){
    ssfnet_throw_exception(SSFNetException::must_implement_exception, " getNextEvent()");
}

void DynamicTrafficType::processUpdateEvent(UpdateTrafficTypeEvent* update_evt){
    ssfnet_throw_exception(SSFNetException::must_implement_exception, " processUpdateEvent()");
}

void DynamicTrafficType::processFinishedEvent(FinishedTrafficEvent* finished_evt){
	ssfnet_throw_exception(SSFNetException::must_implement_exception, " processFinishedEvent()");
}

void DynamicTrafficType::finishTraffic(uint64_t traffic_id){
	 ssfnet_throw_exception(SSFNetException::must_implement_exception, " finishTraffic");
}

ConfigType* DynamicTrafficType::getProtocolType(){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " getProtocolType()");
        return NULL;
}

int DynamicTrafficType::getOwningCommunityId() {
	 ssfnet_throw_exception(SSFNetException::must_implement_exception, " getOwningCommunityId()");
	return 0;
}

CentralizedTrafficType::CentralizedTrafficType(){

}

CentralizedTrafficType::~CentralizedTrafficType(){

}
void CentralizedTrafficType::init(){
	TrafficType::init();
	if(!shared.srcs.read().isCompiled()) {
		shared.srcs.read().evaluate(getParent()->getParent());
	}
	if(!shared.dsts.read().isCompiled()) {
		shared.dsts.read().evaluate(getParent()->getParent());
	}
}

bool CentralizedTrafficType::shouldBeIncludedInCommunity(Community* comm){
		LOG_DEBUG("this community id="<<comm->getCommunityId()<<"owning community id="<<getOwningCommunityId()<<endl)
        if(comm->getCommunityId()==getOwningCommunityId())
                return true;
        return false;
}

void CentralizedTrafficType::getNextEvent(StartTrafficEvent*& traffics_to_start,
		UpdateTrafficTypeEvent*& update_vent,
        bool& wrap_up,
        VirtualTime& recall_at){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " getNextEvent()");
}

void CentralizedTrafficType::processUpdateEvent(UpdateTrafficTypeEvent* update_evt){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " processFinishedEvent()");
}

void CentralizedTrafficType::processFinishedEvent(FinishedTrafficEvent* finished_evt){
	uint64_t traffic_id = finished_evt->getTrafficId();
	this->finishTraffic(traffic_id);
	UpdateTrafficTypeEvent* update_evt = NULL;
	if(update_evt){
		int cur_community_id = finished_evt->getTargetCommunity();
		Community* cur_community = Partition::getInstance()->getCommunity(cur_community_id);
		if(cur_community){
			cur_community->getTrafficManager()->handleUpdateEvent(update_evt);
		}
	}
}

void CentralizedTrafficType::finishTraffic(uint64_t traffic_id){
	//xxx, what to do?
	//used to monitor the active traffic flows?
	//if there is update, create update event and broadcast.
}

ConfigType* CentralizedTrafficType::getProtocolType(){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " getProtocolType()");
        return NULL;
}

int CentralizedTrafficType::getOwningCommunityId() {
	//XXX, now always assign the community with id 0 to be the one that runs the traffic.
	return 0;
}

DistributedTrafficType::DistributedTrafficType(){

}

DistributedTrafficType::~DistributedTrafficType(){

}

void DistributedTrafficType::init(){

}

bool DistributedTrafficType::shouldBeIncludedInCommunity(Community* com){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " shouldBeIncludedInCommunity()");
        return false;
}

void DistributedTrafficType::getNextEvent(StartTrafficEvent*& traffics_to_start,
                        UpdateTrafficTypeEvent*& updateEvent,
                        bool& wrap_up,
                        VirtualTime& recall_at
                        ){
    ssfnet_throw_exception(SSFNetException::must_implement_exception, " getNextEvent()");
}

void DistributedTrafficType::processUpdateEvent(UpdateTrafficTypeEvent* update_evt){
    ssfnet_throw_exception(SSFNetException::must_implement_exception, " processFinishedEvent()");
}

void DistributedTrafficType::processFinishedEvent(FinishedTrafficEvent* finished_evt){
	ssfnet_throw_exception(SSFNetException::must_implement_exception, " processFinishedEvent()");
}

void DistributedTrafficType::finishTraffic(uint64_t traffic_id){
	 ssfnet_throw_exception(SSFNetException::must_implement_exception, " finishTraffic");
	 //if there is update, create update event and broadcast.
}

ConfigType* DistributedTrafficType::getProtocolType(){
        ssfnet_throw_exception(SSFNetException::must_implement_exception, " getProtocolType()");
        return NULL;
}

int DistributedTrafficType::getOwningCommunityId() {
	//XXX, now always assign the community with id 0 to be the one that runs the traffic.
	return 0;
}

Traffic::Traffic() {
}

Traffic::~Traffic() {
}

void Traffic::init() {
	//We initialize the traffic types in traffic manager.
}

void Traffic::getTrafficTypesForCommunity(Community* com, TrafficType::Vector& types) {
		ChildIterator<TrafficType*> si = traffic_types();
		while (si.hasMoreElements()) {
			TrafficType* t = si.nextElement();
			//LOG_DEBUG("there is a traffic type="<<t<<endl)
			if(t->shouldBeIncludedInCommunity(com)) {
				LOG_DEBUG("This traffic type="<<t<<" is in this community"<<endl)
				types.push_back(t);
				//t->setCommunity(com);
				if (FluidTraffic::getClassConfigType()->isSubtype(t->getConfigType())) {
					SSFNET_DYNAMIC_CAST(FluidTraffic*,t)->traffic_mgr=com->getTrafficManager();
				}
			}
			//else{
			//	LOG_DEBUG("This traffic type="<<t<<" is NOT in this community"<<endl)
			//}
		}
}

// ssf requires this macro to register an event class.
SSF_REGISTER_EVENT(StartTrafficEvent, StartTrafficEvent::createStartTrafficEvent);
SSF_REGISTER_EVENT(FinishedTrafficEvent, FinishedTrafficEvent::createFinishedTrafficEvent);
SSF_REGISTER_EVENT(UpdateTrafficTypeEvent, UpdateTrafficTypeEvent::createUpdateTrafficTypeEvent);

} // namespace ssfnet
} // namespace prime
