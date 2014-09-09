/*
 * \file cbr_traffic.cc
 * \author Miguel Erazo
 * 
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
#include "proto/udp/test/cbr_traffic.h"
#include "proto/udp/test/cbr.h"
#include "os/protocol_session.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(UDPTraffic)

UDPTraffic::UDPTraffic() : traffic_id(0) {}
bool UDPTraffic::shouldBeIncludedInCommunity(Community* com){
	return StaticTrafficType::shouldBeIncludedInCommunity(com);
}

ConfigType* UDPTraffic::getProtocolType() { return CBR::getClassConfigType(); }

void UDPTraffic::getNextEvent(StartTrafficEvent*& traffics_to_start,
			//the tm will send this to the correct host at the correcT time
			UpdateTrafficTypeEvent*& update_vent,
			//if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up,
			//if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at
			//when should the traffic type be recalled
			//-- may need to update its current recall time. if its zero don't change it
			)
{
	VirtualTime now = prime::ssf::now();
	VirtualTime next;

	if(traffic_id == 0){
		// Schedule all flows to start at start_time
		next = VirtualTime(shared.start_time.read(), VirtualTime::SECOND);
		traffic_id++;
	} else {
		if(shared.dst_ips.read().size()==0){
			if(!unshared.traffic_flows->empty()) {
				traffics_to_start = new StartTrafficEvent();
				traffics_to_start->setStartTime(VirtualTime(prime::ssf::now()));
				LOG_DEBUG("simulated CBR_TRAFFIC, src uid is: " << unshared.traffic_flows->front().first << endl)
				LOG_DEBUG("simulated CBR_TRAFFIC, dst uid is: " << unshared.traffic_flows->front().second << endl)
				traffics_to_start->setHostUID(unshared.traffic_flows->front().first);
				traffics_to_start->setDstUID(unshared.traffic_flows->front().second);
				traffics_to_start->setDstIP(0);
				traffics_to_start->setTrafficId(traffic_id++);
				traffics_to_start->setTrafficType(this);
				traffics_to_start->setTrafficTypeUID(getUID());
				unshared.traffic_flows->pop_front();
				if(shared.interval_exponential.read()){
					next = VirtualTime(getRandom()->exponential(1.0/shared.interval.read()), VirtualTime::SECOND);
				}else{
					next = VirtualTime(shared.interval.read(), VirtualTime::SECOND);
				}
			}
		}else{
			if(!unshared.hybrid_traffic_flows->empty()) {
				traffics_to_start = new StartTrafficEvent();
				traffics_to_start->setStartTime(VirtualTime(prime::ssf::now()));
				LOG_DEBUG("hybrid CBR_TRAFFIC, src uid is: " << unshared.hybrid_traffic_flows->front().first << endl)
				LOG_DEBUG("hybrid CBR_TRAFFIC, dst ip is: " << unshared.hybrid_traffic_flows->front().second << endl)
				traffics_to_start->setHostUID(unshared.hybrid_traffic_flows->front().first);
				IPAddress ipaddr;
				ipaddr.fromString(unshared.hybrid_traffic_flows->front().second);
				traffics_to_start->setDstIP((uint32_t)ipaddr);
				traffics_to_start->setDstUID(0);
				traffics_to_start->setTrafficId(traffic_id++);
				traffics_to_start->setTrafficType(this);
				traffics_to_start->setTrafficTypeUID(getUID());
				unshared.hybrid_traffic_flows->pop_front();
				if(shared.interval_exponential.read()){
					next = VirtualTime(getRandom()->exponential(1.0/shared.interval.read()), VirtualTime::SECOND);
				}else{
					next = VirtualTime(shared.interval.read(), VirtualTime::SECOND);
				}
			}
		}
	}
	if(shared.dst_ips.read().size()==0){
		if(unshared.traffic_flows->empty()){
			//Finish traffic
			LOG_DEBUG("CBR_TRAFFIC: wrapping up traffic" << endl);
			wrap_up=true;
		} else {
			LOG_DEBUG("CBR_TRAFFIC: recalling at " << next << endl);
			recall_at = next;
		}
	}else{
		if(unshared.hybrid_traffic_flows->empty()){
			//Finish traffic
			LOG_DEBUG("CBR_TRAFFIC: wrapping up traffic" << endl);
			wrap_up=true;
		} else {
			LOG_DEBUG("CBR_TRAFFIC: recalling at " << next << endl);
			recall_at = next;
		}
	}
}

int UDPTraffic::getDstPort(){
	return shared.dstPort.read();
}

int UDPTraffic::getBytesToSendEachInterval(){
	return unshared.bytesToSendEachInterval.read();
}

bool UDPTraffic::getToEmulated(){
	return unshared.toEmulated.read();
}

int UDPTraffic::getCount(){
	return unshared.count.read();
}

float UDPTraffic::getInterval(){
	return shared.interval.read();
}

} // namespace ssfnet
} // namespace prime
