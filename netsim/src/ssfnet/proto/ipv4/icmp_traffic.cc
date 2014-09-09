/**
 * \file icmp_traffic.cc
 * \brief Source file for the ICMPTraffic class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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
#include "proto/ipv4/icmp_traffic.h"
#include "os/protocol_session.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(ICMPTraffic)


ConfigType* ICMPTraffic::getProtocolType() { return ICMPv4Session::getClassConfigType(); }

bool ICMPTraffic::shouldBeIncludedInCommunity(Community* com){ return StaticTrafficType::shouldBeIncludedInCommunity(com); };

void ICMPTraffic::getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
			UpdateTrafficTypeEvent*& update_evt, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
			) { StaticTrafficType::getNextEvent(traffics_to_start, update_evt, wrap_up, recall_at); };

PingTraffic::PingTraffic(): traffic_id(0){}
bool PingTraffic::shouldBeIncludedInCommunity(Community* com){
	return StaticTrafficType::shouldBeIncludedInCommunity(com);
}

void PingTraffic::getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
			UpdateTrafficTypeEvent*& update_evt, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
			){
	traffics_to_start=new StartTrafficEvent();
	if(traffic_id==0){
		if(shared.dst_ips.read().size()==0){
			if(unshared.traffic_flows->empty()){
				LOG_WARN("src and dst are set to be the same, no traffic flows, wrap_up, traffic_id="<<traffic_id<<endl)
				wrap_up = true;
				delete traffics_to_start;
				traffics_to_start= 0;
			}else{
				//LOG_DEBUG("start time="<<shared.start_time.read()<<", "<<VirtualTime(shared.start_time.read(), VirtualTime::SECOND).second()<<endl)
				traffics_to_start->setStartTime(VirtualTime(shared.start_time.read(), VirtualTime::SECOND));
			}
		}else{
			LOG_DEBUG("Traffic is between simulated node and emu/real nodes."<<endl);
			if(unshared.hybrid_traffic_flows->empty()){
				LOG_WARN("no hybrid traffic flows, wrap_up, traffic_id="<<traffic_id<<endl)
				wrap_up = true;
				delete traffics_to_start;
				traffics_to_start= 0;
			}else{
				//LOG_DEBUG("start time="<<shared.start_time.read()<<", "<<VirtualTime(shared.start_time.read(), VirtualTime::SECOND).second()<<endl)
				traffics_to_start->setStartTime(VirtualTime(shared.start_time.read(), VirtualTime::SECOND));
			}
		}
	}
	else{
		//this is the following traffic, this function is recalled after the interval time, so start the traffic immediately
		traffics_to_start->setStartTime(VirtualTime(prime::ssf::now()));
	}
	if(traffics_to_start){
		//LOG_DEBUG("traffic_id="<<traffic_id<<endl)
		//LOG_DEBUG("ICMP_TRAFFIC, src uid is: "<<unshared.traffic_flows->front().first<<endl)
		//LOG_DEBUG("ICMP_TRAFFIC, dst uid is: "<<unshared.traffic_flows->front().second<<endl)
		if(shared.dst_ips.read().size()==0){
			traffics_to_start->setHostUID(unshared.traffic_flows->front().first);
			traffics_to_start->setDstUID(unshared.traffic_flows->front().second);
			traffics_to_start->setDstIP(0);
		}else{
			traffics_to_start->setHostUID(unshared.hybrid_traffic_flows->front().first);
			IPAddress ipaddr;
			ipaddr.fromString(unshared.hybrid_traffic_flows->front().second);
			traffics_to_start->setDstIP((uint32_t)ipaddr);
			traffics_to_start->setDstUID(0);
		}
		traffics_to_start->setTrafficId(traffic_id++);
		traffics_to_start->setTrafficType(this);
		traffics_to_start->setTrafficTypeUID(getUID());
		//LOG_DEBUG("unshared.traffic_flows->empty()="<<unshared.traffic_flows->empty()<<endl)
		if((traffic_id)%shared.count.read()==0){
			//LOG_DEBUG("pop traffic_flows, traffic_id="<<traffic_id<<endl)
			if(shared.dst_ips.read().size()==0){
				unshared.traffic_flows->pop_front();
			}else{
				unshared.hybrid_traffic_flows->pop_front();
			}
		}
		if(shared.dst_ips.read().size()==0){
			if(unshared.traffic_flows->empty()){
				//LOG_DEBUG("wrap_up, traffic_id="<<traffic_id<<endl)
				wrap_up=true;
				//LOG_DEBUG("The flows have been processed, traffic_id="<<traffic_id<<endl)
			}else{
				//LOG_DEBUG("interval exponential is: "<<shared.interval_exponential.read()<<endl)
				if(shared.interval_exponential.read()){
					recall_at=VirtualTime(getRandom()->exponential(1.0/shared.interval.read()), VirtualTime::SECOND);
				}else {
					recall_at=VirtualTime(shared.interval.read(), VirtualTime::SECOND);
				}
				if(traffic_id==1){
					recall_at=VirtualTime(shared.start_time.read(), VirtualTime::SECOND)+recall_at;
				}
			}
		}else{
			if(unshared.hybrid_traffic_flows->empty()){
				//LOG_DEBUG("wrap_up, traffic_id="<<traffic_id<<endl)
				wrap_up=true;
				//LOG_DEBUG("The flows have been processed, traffic_id="<<traffic_id<<endl)
			}else{
				//LOG_DEBUG("interval exponential is: "<<shared.interval_exponential.read()<<endl)
				if(shared.interval_exponential.read()){
					recall_at=VirtualTime(getRandom()->exponential(1.0/shared.interval.read()), VirtualTime::SECOND);
				}else {
					recall_at=VirtualTime(shared.interval.read(), VirtualTime::SECOND);
				}
				if(traffic_id==1){
					recall_at=VirtualTime(shared.start_time.read(), VirtualTime::SECOND)+recall_at;
				}
			}
		}
	}
}

uint32_t PingTraffic::getPayloadSize(){
	return shared.payload_size.read();
}
} // namespace ssfnet
} // namespace prime

