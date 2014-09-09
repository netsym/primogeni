/**
 * \file red_queue.cc
 * \brief Source file for the RedQueue class.
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

#include <math.h>
#include "os/logger.h"
#include "proto/ipv4/icmpv4_message.h"
#include "net/red_queue.h"
#include "net/net.h"
#include "net/interface.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(RedQueue);


RedQueue::RedQueue()
{
}

RedQueue::~RedQueue() {}

void RedQueue::initStateMap() {
	NicQueue::initStateMap();
	unshared.queue=0;
	unshared.avgque=0;
	unshared.loss=0;
	unshared.crossing=false;
	unshared.interdrop=0;
	unshared.last_update_time=0;
	unshared.vacate_time=0;
}

void RedQueue::init()
{
  NicQueue::init();

  unshared.mean_pktsiz = 8.0*shared.mean_pktsiz.read();

  if(shared.weight.read() == 0) {
    // FROM ns-2: if unshared.weight=0, set it to a reasonable value of
    // 1-exp(-1/C), where C is the link bandwidth in mean packets
    unshared.weight = 1.0 - exp(-1.0/(getBitRate()/unshared.mean_pktsiz));
  } else unshared.weight = shared.weight.read();

  if(shared.qmin.read() == 0) unshared.qmin = 8*0.05*getBufferSize();
  else unshared.qmin = 8.0*shared.qmin.read();
  if(shared.qmax.read() == 0) unshared.qmax = 8*0.5*getBufferSize();
  else unshared.qmax = 8.0*shared.qmax.read();
  if(unshared.qmin > unshared.qmax)
    LOG_ERROR("unshared.qmin=" << unshared.qmin/8 << "B must be larger than unshared.qmax=" << unshared.qmax/8 << "B");

  if(shared.qcap.read() == 0) {
    unshared.qcap = getBufferSize()*8.0;
    if(unshared.qcap > unshared.qmax*2.0) unshared.qcap = unshared.qmax*2.0;
  } else unshared.qcap = 8.0*shared.qcap.read();
  if(unshared.qmax > unshared.qcap)
    LOG_ERROR("unshared.qmax=" << unshared.qmax/8 << "B must be no larger than unshared.qcap=" << unshared.qcap/8 << "B");
}

int RedQueue::type(){
	return RED_QUEUE_TYPE;
}

bool RedQueue::enqueue(Packet* pkt, float drop_prob, bool cannot_drop)
{
	LOG_DEBUG("pkt is enqueue; src="<<pkt->getSrc()<<
			", dst="<<pkt->getDst()<<endl);
  float len = 8.0*pkt->size();
  VirtualTime jitter = calibrate(len);

  bool should_drop = unshared.queue+len > 8.0*getBufferSize() || unshared.loss == 1.0 ||
		     ((unshared.loss > 0) && (inHost()->getRandom()->uniform() < unshared.loss));

  if((!cannot_drop)&&(unshared.queue+len > 8.0*getBufferSize() || unshared.loss == 1.0 ||
     ((unshared.loss > 0) && (inHost()->getRandom()->uniform() < unshared.loss)))) {
    // if the queue is full, or if the packet is chosen to be dropped
    // according to RED policy, just drop the entire packet
    LOG_INFO("interface (ip=" << getInterface()->getIP() << ") drop packet due to RED policy or queue overflow");
#if TEST_ROUTING == 1
		saveRouteDebug(pkt,this);
#endif

	pkt->erase();
	unshared.interdrop = 0; // reset the count of packet arrivals since the last packet drop
	return false;
  }

  double prob=inHost()->getRandom()->uniform(0.0,1.0);
  if((!cannot_drop) && (prob<(double) drop_prob)){
  	// drop the entire packet if the queue full.
  	LOG_INFO("interface (ip=" << getInterface()->getIP() << ") drops packet due to drop probability. "<<endl);
  	pkt->erase();
  	return false;
  }else if(cannot_drop && (prob<(double) drop_prob)){
		should_drop=true;
  }

  // all we need is to insert this packet into the queue
  unshared.queue += len;

  // calculate timing
  VirtualTime t = VirtualTime(unshared.queue/getBitRate()+getLatency(),
			      VirtualTime::SECOND)+jitter;
  unshared.vacate_time = inHost()->getNow()+t;

  // timestamp icmp timestamp packet
  ICMPv4Message* icmph = (ICMPv4Message*)pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_ICMPV4);
  if(icmph) {
    if(icmph->getType() == ICMPv4Message::ICMP_TYPE_TIMESTAMP) {
      icmph->setTimeMsgOriginateTimestamp((uint32)inHost()->getNow().millisecond());
      icmph->setTimeMsgReceiveTimestamp((uint32)unshared.vacate_time.millisecond());
    } else if(icmph->getType() == ICMPv4Message::ICMP_TYPE_TIMESTAMP_REPLY) {
      icmph->setTimeMsgTransmitTimestamp((uint32)inHost()->getNow().millisecond());
    }
  }

  // call the interface to send the packet
  getInterface()->transmit(pkt, t);
  return !should_drop;
}

VirtualTime RedQueue::calibrate(float len)
{
  VirtualTime jitter(0);
  VirtualTime now = inHost()->getNow();

  unshared.interdrop += len;

  if(getJitterRange() > 0) { // if jitter is enabled
    jitter = VirtualTime((inHost()->getRandom()->uniform(-1, 1)*
			  getJitterRange()*len)/getBitRate(), VirtualTime::SECOND);
    now += jitter;
  }

  unshared.queue -= getBitRate()*(now-unshared.last_update_time).second();
  if(unshared.queue < 0) unshared.queue = 0;
  else if(unshared.queue > 8.0*getBufferSize()) unshared.queue = 8.0*getBufferSize();
  unshared.last_update_time = now;

  int m = 0;
  if(now > unshared.vacate_time) // queue is idle?
    m = int((now-unshared.vacate_time).second()*getBitRate()/unshared.mean_pktsiz);
  unshared.avgque *= pow(1-unshared.weight, m+1);
  unshared.avgque += unshared.weight*unshared.queue;

  if(unshared.queue == 0 || unshared.avgque < unshared.qmin) {
	  unshared.crossing = false; // next time is the first time to across the threshold
	  unshared.loss = 0;
  } else if(!unshared.crossing) { // first time crossing the threshold
	  unshared.crossing = true; // no more the first time
	  unshared.loss = 0;
	  unshared.interdrop = 0;
  } else if(unshared.avgque < unshared.qmax) {
	  unshared.loss = (unshared.avgque-unshared.qmin)/(unshared.qmax-unshared.qmin)*unshared.pmax;
  } else {
#ifdef REDMIX_EXPERIMENTAL
    if(avgque < unshared.qcap) {
    	unshared.loss = unshared.avgque*(1-unshared.pmax)/unshared.qmax+(2*unshared.pmax-1);
      if(unshared.loss > 1) loss = 1;
    } else unshared.loss = 1;
#else
    if(unshared.avgque < unshared.qcap)
    	unshared.loss = (unshared.avgque-unshared.qmax)/(unshared.qcap-unshared.qmax)*(1-unshared.pmax)+unshared.pmax;
    else unshared.loss = 1;
#endif
  }

//#ifdef REDMIX_EXPERIMENTAL
  // make uniform instead of geometric inter-drop periods (ns-2)
  int cnt = int(unshared.interdrop/unshared.mean_pktsiz);
  if(shared.wait_opt.read()) {
    if(cnt*unshared.loss < 1.0) unshared.loss = 0;
    else if(cnt*unshared.loss < 2.0) unshared.loss /= (2.0-cnt*unshared.loss);
    else unshared.loss = 1.0;
  } else {
    if(cnt*unshared.loss < 1.0) unshared.loss /= (1.0-cnt*unshared.loss);
    else unshared.loss = 1.0;
  }
  if(unshared.loss < 1.0) unshared.loss *= len/unshared.mean_pktsiz;
  if(unshared.loss > 1.0) unshared.loss = 1.0;
//#endif

  return jitter;
}

} // namespace ssfnet
} // namespace prime
