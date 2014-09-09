/**
 * \file cnf_traffic.cc
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
#include "os/logger.h"
#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "proto/cnf/test/cnf_traffic.h"
#include "os/protocol_session.h"
#include "proto/cnf/cnf_session.h"
#include "os/partition.h"

#define REQUEST_INTERVAL 0.1

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(CNFTraffic);

PRIME_OFSTREAM* CNFTraffic::prob_file=0;


CNFStartTrafficEvent::CNFStartTrafficEvent() :
		StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_CNF_START_EVT), content_id(0), dst_port(0) {
}
CNFStartTrafficEvent::CNFStartTrafficEvent(UID_t traffic_type_uid, uint32_t id,
		UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
		StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_CNF_START_EVT,
				traffic_type_uid, id, h_uid, d_uid, d_ip, st), content_id(0), dst_port(0) {
}
CNFStartTrafficEvent::CNFStartTrafficEvent(TrafficType* traffic_type,
		uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
		StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_CNF_START_EVT,
				traffic_type, id, h_uid, d_uid, d_ip, st), content_id(0), dst_port(0) {
}
CNFStartTrafficEvent::CNFStartTrafficEvent(CNFStartTrafficEvent* evt) :
		StartTrafficEvent(evt), content_id(evt->content_id), dst_port(
				evt->dst_port) {
}
CNFStartTrafficEvent::CNFStartTrafficEvent(const CNFStartTrafficEvent& evt) :
		StartTrafficEvent(evt), content_id(evt.content_id), dst_port(
				evt.dst_port) {
}
CNFStartTrafficEvent::~CNFStartTrafficEvent() {

}
prime::ssf::ssf_compact* CNFStartTrafficEvent::pack() {
	prime::ssf::ssf_compact* dp = StartTrafficEvent::pack();
	dp->add_unsigned_long_long(content_id);
	dp->add_unsigned_long_long(dst_port);
	return dp;
}

void CNFStartTrafficEvent::unpack(prime::ssf::ssf_compact* dp) {
	StartTrafficEvent::unpack(dp);
	dp->get_int(&content_id, 1);
	dp->get_unsigned_long_long(&dst_port, 1);
}

void CNFStartTrafficEvent::setContentID(int c_id) {
	content_id = c_id;
}

int CNFStartTrafficEvent::getContentID() {
	return content_id;
}

void CNFStartTrafficEvent::setDstPort(int p) {
	dst_port = p;
}

UID_t CNFStartTrafficEvent::getDstPort() {
	return dst_port;
}

prime::ssf::Event* CNFStartTrafficEvent::createCNFStartTrafficEvent(
		prime::ssf::ssf_compact* dp) {
	CNFStartTrafficEvent* t_evt = new CNFStartTrafficEvent();
	t_evt->unpack(dp);
	return t_evt;
}

ConfigType* CNFTraffic::getProtocolType() {
	return CNFApplication::getClassConfigType();
}

CNFTraffic::CNFTraffic() :
		traffic_id(0) {
	int seed = (int) rand();
	ack_rng=new prime::rng::Random(prime::rng::Random::RNG_DASSF_LEHMER, seed);
	ack_rng->uniform();

	if(!prob_file) {
		prob_file = new PRIME_OFSTREAM();
		prob_file->open("prob");
	}
}

CNFTraffic::~CNFTraffic(){}

bool CNFTraffic::shouldBeIncludedInCommunity(Community* com) {
	return StaticTrafficType::shouldBeIncludedInCommunity(com);
}

void CNFTraffic::getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
		UpdateTrafficTypeEvent*& update_evt, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
		bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
		VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
		) {
	traffics_to_start = new CNFStartTrafficEvent();
	if (traffic_id == 0) {
		if (traffics_to_start) {
			//LOG_DEBUG("traffic_id="<<traffic_id<<endl)
			UIDVec& src_rids = shared.srcs.read().getCompiledRI()->getUIDVec();
			LOG_DEBUG("The size of the srcs vector ="<<src_rids.size()<<endl);
			int num_of_contents = getNumberOfContents();
			int num_of_requests = getNumberOfRequests()*3;
			int content_id = getContentID();
			UIDVec src_uids;
			BaseEntity* anchor = getParent()->getParent();
			for (UIDVec::iterator it = src_rids.begin(); it < src_rids.end(); it++) {
				src_uids.push_back(*it+(anchor->getUID()-anchor->getSize()));
			}
			LOG_DEBUG("The size of the srcs uids ="<<src_uids.size()<<endl);
			//calculate the popularity of the content
			//select a number of srcs_to_use from src_rids according to the popularity
			//push srcs to a list, every time pop one src to send request
			//while the list is empty, wrap up the traffic.
			//HACK, for each content, calculate its popularity
			float popularity = this->calPopularity(content_id,num_of_contents);
			content_id += 180*(rand()%5);//XXX HACK
			int requests_to_send = (int)num_of_requests*popularity;
			if(requests_to_send==0){
				requests_to_send = 1;
			}
			LOG_DEBUG("The number of requests to send ="<<requests_to_send<<endl);
			for(int i=0; i<requests_to_send; i++){
				int index = rand() % src_uids.size();
				srcs_to_use.push_back(src_uids[index]);
			}
			LOG_DEBUG("The size of the srcs to use ="<<srcs_to_use.size()<<endl);
			UID_t src_uid = srcs_to_use.front();
			srcs_to_use.pop_front();
			LOG_DEBUG("CNF_TRAFFIC, src uid is: "<<src_uid<<endl)
			traffics_to_start->setStartTime(VirtualTime(shared.start_time.read(), VirtualTime::SECOND));
			traffics_to_start->setHostUID(src_uid);
			traffics_to_start->setDstUID(0);
			traffics_to_start->setTrafficId(traffic_id++);
			traffics_to_start->setTrafficType(this);
			traffics_to_start->setTrafficTypeUID(getUID());

			//set the additional parameters in CNF start traffic event: content id, dst port
			SSFNET_DYNAMIC_CAST(CNFStartTrafficEvent*,traffics_to_start)->setContentID(
					content_id);
			SSFNET_DYNAMIC_CAST(CNFStartTrafficEvent*,traffics_to_start)->setDstPort(
					getDstPort());

			if(prob_file)
				(*prob_file)<<"CNFPROBsrc=="<<src_uid<<"==content=="<<content_id<<endl;

			if (srcs_to_use.empty()) {
				wrap_up = true;
			} else {
				if(shared.interval_exponential.read()){
					recall_at = VirtualTime(shared.start_time.read(), VirtualTime::SECOND)
							+ VirtualTime(ack_rng->exponential(1.0/shared.interval.read()), VirtualTime::SECOND);
				}else{
					recall_at = VirtualTime(shared.start_time.read(), VirtualTime::SECOND)
							+ VirtualTime(shared.interval.read(), VirtualTime::SECOND);
				}
			}
		}
	} else {
		//int content_id = getContentID();
		int content_id = getContentID()+180*(rand()%5);//XXX HACK
		UID_t src_uid = srcs_to_use.front();
		srcs_to_use.pop_front();
		traffics_to_start->setStartTime(VirtualTime(prime::ssf::now()));
		traffics_to_start->setHostUID(src_uid);
		traffics_to_start->setDstUID(0);
		traffics_to_start->setTrafficId(traffic_id++);
		traffics_to_start->setTrafficType(this);
		traffics_to_start->setTrafficTypeUID(getUID());

		//set the additional parameters in CNF start traffic event: content id, dst port
		SSFNET_DYNAMIC_CAST(CNFStartTrafficEvent*,traffics_to_start)->setContentID(
				content_id);
		SSFNET_DYNAMIC_CAST(CNFStartTrafficEvent*,traffics_to_start)->setDstPort(
				getDstPort());
		if(prob_file)
			(*prob_file)<<"CNFPROBsrc=="<<src_uid<<"==content=="<<getContentID()<<endl;

		if (srcs_to_use.empty()) {
			wrap_up = true;
		} else {
			if(shared.interval_exponential.read()){
				recall_at = VirtualTime(ack_rng->exponential(1.0/shared.interval.read()), VirtualTime::SECOND);
			}else{
				recall_at = VirtualTime(shared.interval.read(), VirtualTime::SECOND);
			}
		}
	}
}

int CNFTraffic::getContentID() {
	return shared.content_id.read();
}

UID_t CNFTraffic::getDstPort() {
	return shared.dst_port.read();
}

float CNFTraffic::calPopularity(int cid, int num_of_contents){
	PopularityVec p = Zipf(0.5, num_of_contents);
	return p[cid-1];
}

int CNFTraffic::getNumberOfContents() {
	return shared.number_of_contents.read();
}

int CNFTraffic::getNumberOfRequests() {
	return shared.number_of_requests.read();
}

CNFTraffic::PopularityVec CNFTraffic::Zipf(float theta, int N){
	/*
	 * zipfian - p(i) = c / i ^^ (1 - theta)
	 * At theta = 1, uniform *
	 * at theta = 0, pure zipfian
	 */
    float sum=0.0;
    float c=0.0;
    float expo;
    int i;
    PopularityVec zdist(N);
    expo = 1 - theta;

    for (i = 1; i <= N; i++) {
        sum += 1.0 /(float) pow((double) i, (double) (expo));
    }
    c = 1.0 / sum;

    for (i = 0; i < N; i++) {
         zdist[i] = c / (float) pow((double) (i + 1), (double) (expo));
    }

    return zdist;
}

// ssf requires this macro to register an event class.
SSF_REGISTER_EVENT(CNFStartTrafficEvent,
		CNFStartTrafficEvent::createCNFStartTrafficEvent);

} // namespace ssfnet
} // namespace prime

