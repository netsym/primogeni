/*
 * \file ppbp_traffic.cc
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
#include "proto/udp/test/ppbp_traffic.h"
#include "proto/udp/test/cbr.h"
#include "os/protocol_session.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(PPBPTraffic)

PPBPStartTrafficEvent::PPBPStartTrafficEvent():
		StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_PPBP_START_EVT), bytes_to_send_per_interval(1000000), num_of_intervals(0),
        interval(0.01) {
}
PPBPStartTrafficEvent::PPBPStartTrafficEvent(UID_t traffic_type_uid, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_PPBP_START_EVT, traffic_type_uid, id, h_uid, d_uid, d_ip, st), bytes_to_send_per_interval(1000000), num_of_intervals(0),
        interval(0.01){
}
PPBPStartTrafficEvent::PPBPStartTrafficEvent(TrafficType* traffic_type, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_PPBP_START_EVT, traffic_type, id, h_uid, d_uid, d_ip, st), bytes_to_send_per_interval(1000000), num_of_intervals(0),
        interval(0.01) {
}
PPBPStartTrafficEvent::PPBPStartTrafficEvent(PPBPStartTrafficEvent* evt) :
        StartTrafficEvent(evt), bytes_to_send_per_interval(evt->bytes_to_send_per_interval),
        num_of_intervals(evt->num_of_intervals), interval(evt->interval){
}
PPBPStartTrafficEvent::PPBPStartTrafficEvent(const PPBPStartTrafficEvent& evt) :
		StartTrafficEvent(evt), bytes_to_send_per_interval(evt.bytes_to_send_per_interval),
		num_of_intervals(evt.num_of_intervals), interval(evt.interval){
}
PPBPStartTrafficEvent::~PPBPStartTrafficEvent(){

}
prime::ssf::ssf_compact* PPBPStartTrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = StartTrafficEvent::pack();
	dp->add_unsigned_int(bytes_to_send_per_interval);
	dp->add_float(num_of_intervals);
	dp->add_float(interval);
	return dp;
}

void PPBPStartTrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	StartTrafficEvent::unpack(dp);
	dp->get_unsigned_int(&bytes_to_send_per_interval,1);
	dp->get_float(&num_of_intervals,1);
	dp->get_float(&interval,1);
}

void PPBPStartTrafficEvent::setBytesToSendPerIntv(uint32_t bytes_per_intv){
	bytes_to_send_per_interval = bytes_per_intv;
}

uint32_t PPBPStartTrafficEvent::getBytesToSendPerIntv(){
    return bytes_to_send_per_interval;
}

void PPBPStartTrafficEvent::setNumOfIntervals(float num_of_intv){
	num_of_intervals = num_of_intv;
}

float PPBPStartTrafficEvent::getNumOfIntervals(){
    return num_of_intervals;
}

void PPBPStartTrafficEvent::setInterval(float intv){
	interval = intv;
}

float PPBPStartTrafficEvent::getInterval(){
    return interval;
}

prime::ssf::Event* PPBPStartTrafficEvent::createPPBPStartTrafficEvent(prime::ssf::ssf_compact* dp){
	PPBPStartTrafficEvent* t_evt = new PPBPStartTrafficEvent();
    t_evt->unpack(dp);
    return t_evt;
}

PPBPTraffic::PPBPTraffic() : traffic_id(0) {}
bool PPBPTraffic::shouldBeIncludedInCommunity(Community* com){
	return StaticTrafficType::shouldBeIncludedInCommunity(com);
}

ConfigType* PPBPTraffic::getProtocolType() { return CBR::getClassConfigType(); }

void PPBPTraffic::getNextEvent(StartTrafficEvent*& traffics_to_start,
			UpdateTrafficTypeEvent*& update_vent,
			bool& wrap_up,
			VirtualTime& recall_at
			){
	traffics_to_start=new PPBPStartTrafficEvent();
	if(shared.dst_ips.read().size()>0){
		LOG_ERROR("PPBPTraffic doesn't support hybrid traffic yet!"<<endl)
	}
	if(traffic_id==0){
		if(unshared.traffic_flows->empty()){
			LOG_WARN("src and dst are set to be the same, no traffic flows, wrap_up, traffic_id="<<traffic_id<<endl)
			wrap_up = true;
			delete traffics_to_start;
			traffics_to_start= 0;
		}else if (unshared.traffic_flows->size()>1){
			LOG_ERROR("Should only allow one src and one dst for PPBP traffic configuration! "<<endl)
		}else{
			//LOG_DEBUG("start time="<<shared.start_time.read()<<", "<<VirtualTime(shared.start_time.read(), VirtualTime::SECOND).second()<<endl)
			traffics_to_start->setStartTime(VirtualTime(shared.start_time.read(), VirtualTime::SECOND));
		}
	}else{
		//this is the following traffic, this function is recalled after the interval time, so start the traffic immediately
		traffics_to_start->setStartTime(VirtualTime(prime::ssf::now()));
	}
	if(traffics_to_start){
		traffics_to_start->setHostUID(unshared.traffic_flows->front().first);
		traffics_to_start->setDstUID(unshared.traffic_flows->front().second);
		traffics_to_start->setTrafficId(traffic_id++);
		traffics_to_start->setTrafficType(this);
		traffics_to_start->setTrafficTypeUID(getUID());

		//set the additional parameters in PPBP start traffic event: bytes_to_send_per_intv, num_of_intvs, interval
		SSFNET_DYNAMIC_CAST(PPBPStartTrafficEvent*, traffics_to_start)->setBytesToSendPerIntv(unshared.bytes_to_send_per_interval.read());
		float avg_duration = shared.interval.read()*unshared.avg_sessions.read(); //xxx, duration is Pareto distributed
		double gamma = 3-2*unshared.hurst.read();
		double delta = avg_duration*(gamma-1)/gamma;
		float session_duration = getRandom()->pareto(delta, gamma);

		float num_of_intvs = session_duration / shared.interval.read();

		SSFNET_DYNAMIC_CAST(PPBPStartTrafficEvent*, traffics_to_start)->setNumOfIntervals(num_of_intvs);
		SSFNET_DYNAMIC_CAST(PPBPStartTrafficEvent*, traffics_to_start)->setInterval(shared.interval.read());

        LOG_DEBUG("now="<<VirtualTime(prime::ssf::now())<<", traffic id="<<traffic_id<<", ave session="<<unshared.avg_sessions.read()<<endl);
		if(VirtualTime(prime::ssf::now()) > VirtualTime(unshared.stop.read(), VirtualTime::SECOND)){
			//LOG_DEBUG("wrap_up, traffic_id="<<traffic_id<<endl)
			wrap_up=true;
			LOG_DEBUG("now="<<VirtualTime(prime::ssf::now())<<", stop="<<unshared.stop.read()<<" stop starting new flows."<<endl)
		}else{
			//LOG_DEBUG("interval exponential is: "<<shared.interval_exponential.read()<<endl)
			if(shared.interval_exponential.read()){
				recall_at=VirtualTime(getRandom()->exponential(1.0/shared.interval.read()), VirtualTime::SECOND);
			} else {
				LOG_WARN("The interval_exponetial="<< shared.interval_exponential.read()<<", which should be set to be True for PPBP traffic."<<endl)
				recall_at=VirtualTime(shared.interval.read(), VirtualTime::SECOND);
			}
			if(traffic_id==1){
				recall_at=VirtualTime(shared.start_time.read(), VirtualTime::SECOND);
			} else if(traffic_id<=unshared.avg_sessions.read()){//start avg number of sessions at the start time
				recall_at=VirtualTime(0, VirtualTime::SECOND);
			}
		}
	}
}

int PPBPTraffic::getDstPort(){
	return shared.dst_port.read();
}

// ssf requires this macro to register an event class.
SSF_REGISTER_EVENT(PPBPStartTrafficEvent, PPBPStartTrafficEvent::createPPBPStartTrafficEvent);

} // namespace ssfnet
} // namespace prime
