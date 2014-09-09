/**
 * \file droptail_queue.cc
 * \brief Source file for the DropTailQueue class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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
#include "proto/ipv4/icmpv4_message.h"
#include "net/droptail_queue.h"
#include "net/net.h"
#include "net/interface.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(DropTailQueue);

void DropTailQueue::initStateMap() {
	NicQueue::initStateMap();
	unshared.last_xmit_time=0;
	unshared.queue_delay=0;
}

void DropTailQueue::init()
{
	NicQueue::init();
	// convert the queuing delay in seconds to simulation ticks
	unshared.max_queue_delay = VirtualTime
			(8.0 * getBufferSize() / getBitRate(), VirtualTime::SECOND);
}

int DropTailQueue::type(){
	return DROPTAIL_QUEUE_TYPE;
}

int DropTailQueue::getInstantLength()
{
	// calculate/update the queuing delay
	VirtualTime now = inHost()->getNow();
	if(now != unshared.last_xmit_time) {
		unshared.queue_delay -= (now - unshared.last_xmit_time);
		if (unshared.queue_delay < 0) unshared.queue_delay = 0;
		unshared.last_xmit_time = now;
	}

	return int(unshared.queue_delay.second()*getBitRate()/8.0);
}

int DropTailQueue::getQueueSize() {
	return getInstantLength();
}

bool DropTailQueue::enqueue(Packet* pkt, float drop_prob, bool cannot_drop)
{
	LOG_DEBUG("enqueue, this interface is "<<this->getInterface()->getUID()<<endl);
	int pktlen = pkt->size();
	VirtualTime jitter = calibrate(pktlen);

	VirtualTime testqueue_delay = unshared.queue_delay + VirtualTime(8.0*pktlen/getBitRate(), VirtualTime::SECOND);
	bool should_drop = testqueue_delay > unshared.max_queue_delay;
	if(!cannot_drop && should_drop) {
		// drop the entire packet if the queue full.
		LOG_INFO("interface (ip=" << getInterface()->getIP() << ") queue over flown; drop packet\n");
#if TEST_ROUTING == 1
		saveRouteDebug(pkt,this);
#endif
		pkt->erase();
		return false;
	}

	double prob=inHost()->getRandom()->uniform(0.0,1.0);
	if((!cannot_drop) && (prob<(double) drop_prob)){
		// drop the entire packet if the queue full.
		LOG_DEBUG("interface (ip=" << getInterface()->getIP() << ") drops packet due to drop probability which is "
				<<drop_prob<<", sampled drop prob="<<prob<<endl);
		pkt->erase();
		return false;
	}else if(cannot_drop && (prob<(double) drop_prob)){
		should_drop=true;
	}

	// calculate queuing delay
	unshared.queue_delay = testqueue_delay+jitter+
			VirtualTime(getLatency(), VirtualTime::SECOND);

	// timestamp icmp timestamp packet
	ICMPv4Message* icmph = (ICMPv4Message*)pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_ICMPV4);
	if(icmph) {
		if(icmph->getType() == ICMPv4Message::ICMP_TYPE_TIMESTAMP) {
			icmph->setTimeMsgOriginateTimestamp((uint32)inHost()->getNow().millisecond());
			icmph->setTimeMsgReceiveTimestamp((uint32)(inHost()->getNow()+unshared.queue_delay).millisecond());
		} else if(icmph->getType() == ICMPv4Message::ICMP_TYPE_TIMESTAMP_REPLY) {
			icmph->setTimeMsgTransmitTimestamp((uint32)inHost()->getNow().millisecond());
		}
	}

	// call the interface to send the packet.
	getInterface()->transmit(pkt, unshared.queue_delay);
	return !should_drop;
}

VirtualTime DropTailQueue::calibrate(int pktlen)
{
	VirtualTime jitter(0);
	VirtualTime now = inHost()->getNow();
	if(getJitterRange() > 0) { // if jitter is enabled
		jitter = VirtualTime(inHost()->getRandom()->uniform(-1, 1) *
				8 * getJitterRange() * pktlen / getBitRate(),
				VirtualTime::SECOND);
		now += jitter;
	}
	if(now > unshared.last_xmit_time) { // it's possible jitter makes now < last_xmit_time
		unshared.queue_delay -= (now - unshared.last_xmit_time);
		if (unshared.queue_delay < 0) unshared.queue_delay = 0;
		unshared.last_xmit_time = now;
	}
	return jitter;
}

} // namespace ssfnet
} // namespace prime
