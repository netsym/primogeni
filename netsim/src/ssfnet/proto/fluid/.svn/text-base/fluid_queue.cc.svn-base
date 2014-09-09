/**
 * \file fluid_queue.cc
 * \brief Source file for the FluidQueue class.
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
#include "os/logger.h"
#include "os/traffic_mgr.h"
#include "fluid_queue.h"
#include "fluid_hop.h"
//#include "net/quemon.h"
#include "net/net.h"
#include "net/interface.h"

#ifdef SSFNET_FLUID_VARYING
#define SSFNET_FLUID_VARYING_QSS 10.0
#ifdef SSFNET_FLUID_DYNAMIC
#define DELTA_QUEUE_THRESHOLD ((unshared.mean_pktsiz/getBitRate())*timer_stepsize)
#endif
#endif

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(FluidQueue)

// FIXME: we make wait_opt to be always true in the calculation for
// packet flows and false for fluid flows. For some reason, doing so
// results a better match between the two

FluidQueue::FluidQueue()
#ifdef SSFNET_FLUID_VARYING
: timer_handle(0), timer_stepsize(0)
#endif
{}

FluidQueue::~FluidQueue() {
	// it's possible that there are still fluid flowing through this
	// queue; these are static fluid classes; the destructor of the
	// fluid class is called later than this destructor
	//LOG_DEBUG("              ~fluid queue"<<endl)
	if (!fluid_map.empty()) {
		for (FluidMap::iterator iter = fluid_map.begin(); iter!= fluid_map.end(); iter = fluid_map.begin()) {
			// we must sever the link before reclaim the hop so that its
			// destructor will not follow the link
			FluidHop* nexthop = (*iter).second->next;
			(*iter).second->next = 0;
			if (nexthop && (*iter).second->next_is_ghost)
				delete nexthop;
			delete (*iter).second; // this changes fluid_map!!!
		}
		assert(fluid_map.empty());
	}
}

void FluidQueue::init() {
	NicQueue::init();

	unshared.mean_pktsiz=8.0*shared.mean_pktsiz.read();

	if(shared.weight.read() == 0) {
	    // FROM ns-2: if unshared.weight=0, set it to a reasonable value of
	    // 1-exp(-1/C), where C is the link bandwidth in mean packets
	    unshared.weight = 1.0 - exp(-1.0/(getBitRate()/unshared.mean_pktsiz));
	} else unshared.weight = shared.weight.read();

	// if the interface is connected, get the propagation delay
	if (!getInterface()->getLink())
		return;
	unshared.prop_delay = getInterface()->getLink()->getDelay().second() + getLatency();

	if (shared.is_red.read()) { // for RED queue
		if (shared.qmin.read() == 0)
			unshared.qmin = 0.05 * 8.0 * getBufferSize();
		else
			unshared.qmin = 8.0 * shared.qmin.read();
		if (shared.qmax.read() == 0)
			unshared.qmax = 0.5*8.0 * getBufferSize();
		else
			unshared.qmax = 8.0 * shared.qmax.read();

		if (unshared.qmin > unshared.qmax) {
			LOG_ERROR("qmin "<<unshared.qmin<<" (b) must be no larger than qmax "<<unshared.qmax<<" (b)."<<endl);
		}
		/*
		if(shared.qcap.read() == 0) {
			unshared.qcap = getBufferSize()*8.0;
			if(unshare.qcap > unshared.qmax*2.0) unshared.qcap = unshared.qmax*2.0;
		}else unshared.qcap = 8.0*shared.qcap.read();
		if(unshared.qmax > unshared.qcap) {
			LOG_ERROR("qmax "<<unshared.qmax<<" (b) must be no larger than qcap "<<unshared.qcap<<" (b)."<<endl);
		}
		*/
#ifdef DEBUG
		LOG_DEBUG("[interface="<<getInterface()->getUName()
				<<", ip="<<getInterface()->getIP().toString()<<"]"<<endl)
		LOG_DEBUG(" bitrate="<<getBitRate()<<" (bps), latency="<<getLatency()<<" (sec), jitter_range="<<getJitterRange()
				<<", bufsize="<<getBufferSize()<<" (B)"<<endl)
		LOG_DEBUG(" RED parameters: weight="<<unshared.weight<<", qmin="<<unshared.qmin<<" (b), qmax="<<unshared.qmax<<" (b), pmax="
				<<unshared.pmax<<", wait="<<shared.wait_opt.read()<<", mean_pktsiz="<<unshared.mean_pktsiz<<" (b)."<<endl)
#endif
	} else { // for DROPTAIL queue
#ifdef DEBUG
		LOG_DEBUG("[interface="<<getInterface()->getUName()
				<<", ip="<<getInterface()->getIP().toString()<<"]"<<endl)
		LOG_DEBUG(" bitrate="<<getBitRate()<<" (bps), latency="<<getLatency()<<" (sec), jitter_range="<<getJitterRange()
				<<", bufsize="<<getBufferSize()<<" (B)"<<endl)
#endif
	}

	//XXX nate should remove...
	LOG_WARN("Nate needs to fix the initialization of state that is not configurable...."<<endl);
	unshared.fluid_weight=0;
	unshared.aggr_arrival=0;
	unshared.pkt_arrival=0;
	unshared.queue=0;
	unshared.avgque=0;
	unshared.loss=0;
	unshared.shadow_queue=0;
	unshared.shadow_avgque=0;
	unshared.shadow_loss=0;
	unshared.crossing=false;
	unshared.interdrop=0;
	unshared.last_update_time=0;
	unshared.last_send_time=0;
}


void FluidQueue::addFluidHop(TrafficManager* traffic_mgr, FLUID_CLASSID cid, FluidHop* hop) {
	fluid_map.insert(SSFNET_MAKE_PAIR(cid, hop));
	if (fluid_map.size() == 1) {
		traffic_mgr->addFluidQueue(this);
#ifdef SSFNET_FLUID_VARYING
		timer_stepsize = unshared.prop_delay/SSFNET_FLUID_VARYING_QSS;
		if(timer_stepsize < traffic_mgr->getStepSize()){
			timer_stepsize = traffic_mgr->getStepSize();
		}
		timer_handle = traffic_mgr->schedQueueUpdate(this, timer_stepsize);
		/*
		 if(timer_stepsize <= 0) {
		 error_quit("ERROR: FluidQueue::add_fluid_hop(), "
		 "nic=\"%s\", ip=\"%s\": invalid timer step size (%g)\n",
		 getInterface()->getUName(), getInterface()->getIP()), timer_stepsize);
		 }
		 */
#endif
	}
}

void FluidQueue::deleteFluidHop(TrafficManager* traffic_mgr, FLUID_CLASSID cid) {
	fluid_map.erase(cid);
	if (fluid_map.size() == 0) {
		traffic_mgr->deleteFluidQueue(this);
#ifdef SSFNET_FLUID_VARYING
		traffic_mgr->unschedQueueUpdate(timer_handle, this);
		timer_handle = 0; timer_stepsize = 0;
#endif
	}
}

bool FluidQueue::enqueue(Packet* pkt, float drop_prob, bool cannot_drop) {
	// the mac layer header is not counted
	assert(pkt && pkt->getMessage());

	float len = 8.0 * (pkt->size() -pkt->getMessage()->size());

	/*fprintf(this->inHost()->getCommunity()->dump_file,"interface uid=%ull, time=%f, pkt_size=%f \n",
				this->getInterface()->getUID(),
				this->inHost()->getNow().second(),
				len);*/

	// update queue size and compute jitter
	VirtualTime now = inHost()->getNow();
	VirtualTime jitter = calibrate(len);
	//LOG_DEBUG("[shadow_queue="<<unshared.shadow_queue<<", len="<<len<<", buffer size="<<getBufferSize()<<"]"
			//<<", first check="<<(unshared.shadow_queue + len >= 8.0 * getBufferSize())<<endl)
	//LOG_DEBUG("[is_red="<<shared.is_red.read()<<", shadow_loss="<<unshared.shadow_loss<<"(" << (unshared.shadow_loss==1.0) <<")"
			//<<", random number="<<inHost()->getRandom()->uniform()<<"]"
			//<<", second check="<<(shared.is_red.read() && (unshared.shadow_loss == 1.0
					//|| (unshared.shadow_loss > 0 && inHost()->getRandom()->uniform() < unshared.shadow_loss)))<<endl)
	bool should_drop=(unshared.shadow_queue + len >= 8.0 * getBufferSize()) || (shared.is_red.read() && (unshared.shadow_loss == 1.0
			|| (unshared.shadow_loss > 0 && inHost()->getRandom()->uniform() < unshared.shadow_loss)));

	if ((!cannot_drop)&&((unshared.shadow_queue + len >= 8.0 * getBufferSize()) || (shared.is_red.read() && (unshared.shadow_loss == 1.0
			|| (unshared.shadow_loss > 0 && inHost()->getRandom()->uniform() < unshared.shadow_loss))))) {
		// if the queue is full, or if the packet is chosen to be dropped, just drop the entire packet
		LOG_DEBUG("[interface="<<getInterface()->getUName()
				<<", ip="<<getInterface()->getIP().toString()
				<<"] "<<now.second()<<": drop packet (intentionally or due to queue overflow)."<<endl);
#if TEST_ROUTING == 1
		saveRouteDebug(pkt,this);
#endif
		pkt->erase();
		// reset the count of packet arrivals since the last packet drop
		if (shared.is_red.read())
			unshared.interdrop = 0;
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
	unshared.shadow_queue += len;

	/* IMPORTANT: It is possible that the newly updated shadow_queue is
	 depleted faster than what was predicted originally using the
	 aggregate arrival rate, loss probability, and the bandwidth. This
	 is because the aggregate arrival rate and the loss probability
	 are updated at the Runge-Kutta step. In this case, the send time,
	 t, may be earlier than the previous send, which violates the
	 assumption of the FIFO queue at the interface. We need to
	 readjust the delay accordingly. */

	VirtualTime t = VirtualTime(unshared.shadow_queue / getBitRate() + getLatency(),
			VirtualTime::SECOND) + jitter;
	VirtualTime tt = now + t;
	if (tt < unshared.last_send_time) {

		/*printf("%g: [%s] fluid out of order: last=%g, this=%g\n", now.second(),
				getInterface()->getUName(),
				unshared.last_send_time.second(), tt.second());*/

		unshared.last_send_time += VirtualTime(len / getBitRate() + getLatency(),
				VirtualTime::SECOND) + jitter;
		t = unshared.last_send_time - now;
	} else
		unshared.last_send_time = tt;

	/*
	 printf("len=%d, qd=%.9lf, jitter=%.9lf => %.9lf\n", int(len/8),
	 VirtualTime(shadow_queue/getBitRate(), VirtualTime::SECOND).second(),
	 jitter.second(), t.second());
	 */

	// call the interface to send the packet
	getInterface()->transmit(pkt, t);
	return !should_drop;
}

void FluidQueue::updateShadowVariables(VirtualTime now) {
	// it's possible jitter makes now < last_update_time
	if (now <= unshared.last_update_time)
		return;

	int m = 0;
	float dq = unshared.aggr_arrival * (1 - unshared.loss) - getBitRate();
	unshared.shadow_queue += dq * (now - unshared.last_update_time).second();
	if (unshared.shadow_queue < 0) {
		if (shared.is_red.read()) {
			double t = unshared.shadow_queue / dq; // how long is the queue idle?
			m = int(t * getBitRate() / unshared.mean_pktsiz); // in # packets?
		}
		unshared.shadow_queue = 0;
	} else if (unshared.shadow_queue > 8.0 * getBufferSize())
		unshared.shadow_queue = 8.0 * getBufferSize();
	unshared.last_update_time = now;
	//printf("m=%d\n", m);

	if (shared.is_red.read()) {
		// this is important so that we use weight rather than fluid_weight
		unshared.shadow_avgque *= pow(1 - unshared.weight, m + 1);
		unshared.shadow_avgque += unshared.weight * unshared.shadow_queue;
	}

	// loss is treated differently between at each packet arrival and at
	// the queue R-K update time
}

float FluidQueue::getPropDelay() {
	return unshared.prop_delay;
}

float FluidQueue::getPktArrival() {
	return unshared.pkt_arrival;
}

float FluidQueue::getQueue() {
	return unshared.queue;
}

float FluidQueue::getLoss() {
	return unshared.loss;
}

void FluidQueue::setPktArrival(float pkt_arrival) {
	unshared.pkt_arrival=pkt_arrival;
}

void FluidQueue::setAggrArrival(float aggr_arrival) {
	unshared.aggr_arrival=aggr_arrival;
}

VirtualTime FluidQueue::calibrate(float len) {
	VirtualTime jitter(0);
	VirtualTime now = inHost()->getNow();

	// we record the number of bits arrived in the form of packet flows,
	// so that we can calculate aggregate packet arrival rate at the
	// Runge-Kutta step; for packet-only scenario, we also count
	// inter-drop packet arrivals in order to compute the loss
	// probability.
	unshared.pkt_arrival += len;
	unshared.interdrop += len;

	if (getJitterRange() > 0) { // if jitter is enabled
		jitter = VirtualTime((inHost()->getRandom()->uniform(-1, 1)
				* getJitterRange() * len) / getBitRate(), VirtualTime::SECOND);
		now += jitter;
	}

	updateShadowVariables(now);

	if (shared.is_red.read()) {
		if (unshared.shadow_avgque < unshared.qmin || unshared.shadow_avgque > unshared.qmax)
			unshared.crossing = false;
		else {
			if (!unshared.crossing) { // first time into the zone
				unshared.crossing = true;
				unshared.interdrop = 0;
			}
		}

		if (unshared.shadow_avgque < unshared.qmin)
			unshared.shadow_loss = 0;
		else if (unshared.shadow_avgque < unshared.qmax) {
			int cnt = int(unshared.interdrop / unshared.mean_pktsiz);
			unshared.shadow_loss = (unshared.shadow_avgque - unshared.qmin) / (unshared.qmax - unshared.qmin) * unshared.pmax;
			if (1) /*if(shared.wait_opt.read())*/{
				if (cnt * unshared.shadow_loss < 1.0)
					unshared.shadow_loss = 0;
				/* unshared.shadow_loss /= (1.0-cnt*unshared.shadow_loss); */
				else if (cnt * unshared.shadow_loss < 2.0)
					unshared.shadow_loss /= (2.0 - cnt * unshared.shadow_loss);
				else
					unshared.shadow_loss = 1.0;
			} else {
				if (cnt * unshared.shadow_loss < 1.0)
					unshared.shadow_loss /= (1.0 - cnt * unshared.shadow_loss);
				else
					unshared.shadow_loss = 1.0;
			}
			if (unshared.shadow_loss < 1.0)
				unshared.shadow_loss *= len / shared.mean_pktsiz.read();
			if (unshared.shadow_loss > 1.0)
				unshared.shadow_loss = 1.0;
		} else
			unshared.shadow_loss = 1.0;
	}

	/*printf("now=%g q=%g aq=%g l=%g\n", inHost()->getNow().nanosecond(),
	 unshared.shadow_queue, unshared.shadow_avgque, unshared.shadow_loss);*/
	return jitter;
}

#ifdef SSFNET_FLUID_VARYING
void FluidQueue::updateQueue(TrafficManager* traffic_mgr)
{
#ifdef SSFNET_FLUID_DYNAMIC
	float myqueue = unshared.queue;
#endif
	queueEvolution(timer_stepsize);

	float arrival = 0; // aggregate arrival for both fluid and packet
	SSFNET_MAP(FLUID_CLASSID, FluidHop*)::iterator fit;
	for(fit = fluid_map.begin(); fit != fluid_map.end(); fit++)
	arrival += (*fit).second->getArrival(traffic_mgr->curstep);
	unshared.aggr_arrival = arrival; // fluid only here
	if(unshared.pkt_arrival > 0) {
		arrival += unshared.pkt_arrival/timer_stepsize/*traffic_mgr->getStepSize()*/;
		unshared.pkt_arrival = 0;
	}

	float qdelay = unshared.queue/getBitRate(); // queuing time
	long adv = long(ceil(qdelay/traffic_mgr->getStepSize()));

	// update state of each fluid traversing this queue
	for(fit = fluid_map.begin(); fit != fluid_map.end(); fit++) {
		// update departure rate for each fluid from the queue

		// important: we don't differentiate whether it's gonna take
		// time or not for the rates to propagate through the queue
		if(arrival*(1-unshared.loss) <= getBitRate()) {
			// if the aggregate arrival is less than the bandwidth, all
			// arrival rates (minus loss) pass through instantly
			(*fit).second->setDeparture
			((*fit).second->getArrival(traffic_mgr->curstep)*(1-unshared.loss), traffic_mgr->curstep+adv);
		} else {
			// if the aggregate arrival is larger than the bandwidth,
			// the arrival rates are proportioned
			(*fit).second->setDeparture
			((*fit).second->getArrival(traffic_mgr->curstep)*getBitRate()/arrival,
					traffic_mgr->curstep+adv); // BUG FIX: NOT TRAFFIC_MGR->CURSTEP!!!
		}

		// set the flow values at the next queue (should always be
		// there); note that the prop delay of the last leg is stretched
		// to include the path latency (on the returning trip)
		FluidHop* nexthop = (*fit).second->next;
		assert(nexthop);

		// update arrival rate
		static bool prop_warned = false;
		if((*fit).second->prop_delay < timer_stepsize/*traffic_mgr->getStepSize()*/&& !prop_warned) {
			LOG_WARN("propagation delay "<<(*fit).second->prop_delay<<" less than R-K step size "<<timer_stepsize/*traffic_mgr->getStepSize()*/
					<<"for fluid class: "<<IPAddress((*fit).first.first)<<"=>"<<IPAddress((*fit).first.second)<<" on "<<getInterface()->getUName()<<endl);
			prop_warned = true;
		}
		long newpos = traffic_mgr->curstep+long((*fit).second->prop_delay/traffic_mgr->getStepSize()+0.5);
		nexthop->setArrival((*fit).second->getDeparture(traffic_mgr->curstep), newpos);

		// update accumulated delay
		float newvalue = qdelay+(*fit).second->prop_delay;
		newvalue += (*fit).second->getAccuDelay(traffic_mgr->curstep);
		newpos = traffic_mgr->curstep+long((qdelay+(*fit).second->prop_delay)/traffic_mgr->getStepSize()+0.5);
		nexthop->setAccuDelay(newvalue, newpos);

		// update accumulated loss rate
		newvalue = getLoss()*(*fit).second->getArrival(traffic_mgr->curstep);
		newvalue += (*fit).second->getAccuLoss(traffic_mgr->curstep);
		nexthop->setAccuLoss(newvalue, newpos);
	}

#ifdef SSFNET_FLUID_DYNAMIC
	float dq = unshared.queue-myqueue; if(dq<0) dq = -dq;
	if(dq > 2*DELTA_QUEUE_THRESHOLD) {
		if(timer_stepsize > traffic_mgr->getStepSize()) {
			//printf("decrease: %g > %g\n", dq, 2*DELTA_QUEUE_THRESHOLD);
			traffic_mgr->unschedQueueUpdate(timer_handle, this);
			timer_stepsize *= (DELTA_QUEUE_THRESHOLD/dq);
			if(timer_stepsize < traffic_mgr->getStepSize())
			timer_stepsize = traffic_mgr->getStepSize();
			timer_handle = traffic_mgr->schedQueueUpdate(this, timer_stepsize);
		}
	} else if(dq < 0.5*DELTA_QUEUE_THRESHOLD) {
		if(timer_stepsize < unshared.prop_delay) {
			//printf("increase: %g < %g\n", dq, 0.5*DELTA_QUEUE_THRESHOLD);
			traffic_mgr->unschedQueueUpdate(timer_handle, this);
			timer_stepsize *= 2;
			timer_handle = traffic_mgr->schedQueueUpdate(this, timer_stepsize);
		}
	}
#endif
}
#endif

void FluidQueue::queueEvolution(float step_size) {
	VirtualTime now = inHost()->getNow();

	if (shared.is_red.read() && unshared.fluid_weight == 0) {
		unshared.fluid_weight = -log(1 - unshared.weight) / step_size;
#ifdef DEBUG
		LOG_DEBUG("[interface="<<getInterface()->getUName()
				<<"]"<<now.second()<<" : weight="<<unshared.weight<<" => fluid_weight="<<unshared.fluid_weight<<endl);
#endif
	}

	/* Compute queue length, average queue length, and loss rate by
	 treating packet flows as fluid flows, i.e., by computing the
	 average from dividing the total packet arrivals by the RK step
	 size.

	 Alternatively, one can continue to update the shadow variables,
	 therefore obtaining more accurate values. We opt for the second
	 method. */

	updateShadowVariables(now);

	if (shared.is_red.read()) {
		if (unshared.shadow_avgque < unshared.qmin || unshared.shadow_avgque > unshared.qmax)
			unshared.crossing = false;
		else {
			if (!unshared.crossing) { // first time into the zone
				unshared.crossing = true;
				unshared.interdrop = 0;
			}
		}

		// shadow loss needs to be updated
		if (unshared.shadow_avgque < unshared.qmin)
			unshared.shadow_loss = 0;
		else if (unshared.shadow_avgque < shared.qmax.read()) {
			unshared.shadow_loss = (unshared.shadow_avgque - unshared.qmin) / (unshared.qmax - unshared.qmin) * unshared.pmax;
		} else
			unshared.shadow_loss = 1;
	}

	//last_update_time = now;
	unshared.queue = unshared.shadow_queue;

	if (shared.is_red.read()) {
		unshared.avgque = unshared.shadow_avgque;
		unshared.loss = unshared.shadow_loss;

		// the adjustment is described in the TOMACS paper by Liu, Presti,
		// Misra, Towsley, and Gu (2004); this adjustment applies to fluid
		// flows, not packet flows
		if (0) /*if(wait_opt)*/
			unshared.loss *= 0.6666667;
		else
			unshared.loss *= 2;
		if (unshared.loss > 1)
			unshared.loss = 1;
	} else {
		// droptail queue does not care for avgque, but the loss (which is
		// actually the packet drop probability) must be updated so that
		// we can calculate the end-to-end loss probability and readjust
		// the fluid tcp window size
		float y = unshared.aggr_arrival - getBitRate();
		if (y > 0) {
			float t = (8.0 * getBufferSize() - unshared.queue) / y;
			if (t > step_size)
				t = step_size;
			unshared.loss = 1 - t / step_size;
		} else
			unshared.loss = 0;
	}
#ifdef DEBUG
	LOG_DEBUG("[interface="<<getInterface()->getUName()
			<<"] "<<inHost()->getNow().second()<<": aggr_arrival="<<unshared.aggr_arrival
			<<", loss="<<unshared.loss<<", queue="<<unshared.queue<<", avgque="<<unshared.avgque<<endl)
#endif
}

FluidQueue::FluidMap& FluidQueue::getFluidMap(){
	return fluid_map;
}

/*void FluidQueue::quemon_callback() {
	assert(quemon);
	if (phy_sess->getNow() == 0)
		fprintf(quemon->monfd, "# time queue avgque loss aggr_arrival\n");
	fprintf(quemon->monfd, "%g %g %g %g 0\n", phy_sess->getNow().second(),
			shadow_queue, shadow_avgque, shadow_loss);
	fflush(quemon->monfd);
}*/

}; // namespace ssfnet
}; // namespace prime
