/**
 * \file fluid_hop.cc
 * \brief Source file for the FluidHop class.
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
#include "os/logger.h"
#include "net/interface.h"
#include "fluid_hop.h"
#include "fluid_event.h"
#include "fluid_queue.h"
#include "fluid_traffic.h"
#include "os/traffic_mgr.h"

#ifdef SSFNET_FLUID_DAMPENING
//#define SSFNET_FLUID_DAMPENING_WEIGHT 0
#define SSFNET_FLUID_DAMPENING_THRESH 0.05
//#define SSFNET_FLUID_DAMPENING_AVG(x,y)
//  ((1.0-SSFNET_FLUID_DAMPENING_WEIGHT)*(x)+SSFNET_FLUID_DAMPENING_WEIGHT*(y))
#define SSFNET_FLUID_DAMPENING_XING(x,y) \
  ((x)>(1.0+SSFNET_FLUID_DAMPENING_THRESH)*(y) || \
   (y)>(1.0+SSFNET_FLUID_DAMPENING_THRESH)*(x))
#endif

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(FluidHop);

FluidSeries::FluidSeries(int t, float initvalue, long initstep) : type(t) {
	earliest = new FluidState(initvalue, initstep);
#ifdef SSFNET_FLUID_CACHING
	current = earliest;
#endif
	/*
	 if(initstep > 0) {
		 earliest = new FluidState(initvalue, 0);
		 earliest->next = current;
		 current->prev = earliest;
	 }
	 */
}

FluidSeries::~FluidSeries() {
	while (earliest) {
		FluidState* nxt = earliest->next;
		delete earliest;
		earliest = nxt;
	}
}

void FluidSeries::setType(int t) {
	assert(t == PIECE_WISE_LINEAR || t == PIECE_WISE_CONSTANT);
	type = t;
}

void FluidSeries::set(float value, long step) {
#ifndef SSFNET_FLUID_CACHING
	FluidState* current = earliest;
#endif
	assert(current);

	// find the state with time either immediately smaller than or equal
	// to the timestamp of the value to be inserted
	while (current && current->step > step)
		current = current->prev;
	while (current && current->next && current->next->step <= step)
		current = current->next;

	// insert the state
	if (!current) { // if the previous node has fell off the left edge
#if 0
		static bool early_warned = false;
		if(!early_warned) {
			LOG_WARN("set(value="<<value<<", step="<<step<<"): timestamp in the past: earliest="<<earliest->step<<endl);
			early_warned = true;
		}
#endif
		current = earliest;
		step = current->step;
	}

	if (current->step == step) {
		// same timestamp; averaging is needed if we want linear
		// intepolation; otherwise, simply replace it
		/*if(type == PIECE_WISE_LINEAR)
		 current->value = (current->value+value)/2.0;
		 else*/current->value = value;
	} else {
		// insert the node right after the current node
		assert(current->step < step);
		// for linear interpolation, averaging is needed
		/*if(current->next && type == PIECE_WISE_LINEAR) {
		 value = (value+(current->next->value-current->value)/
		 (current->next->step-current->step)*(step-current->step))/2.0;
		 }*/
		FluidState* state = new FluidState(value, step);
		state->next = current->next;
		state->prev = current;
		if (current->next)
			current->next->prev = state;
		current->next = state;
		current = state;
	}
}

float FluidSeries::get(long step) {
	// IMPORTANT: we assume that this function is called with
	// non-decreasing step value!]
	//LOG_DEBUG("step="<<step<<", earlist="<<earliest<<endl)
	//LOG_DEBUG("earliest->step="<<earliest->step<<endl)
	assert(step >= earliest->step);
	while (earliest->next && earliest->next->step <= step) {
		FluidState* state = earliest;
		earliest = earliest->next;
#ifdef SSFNET_FLUID_CACHING
		if (state == current)
			current = earliest;
#endif
		earliest->prev = 0;
		delete state;
	}

	if (earliest->step == step)
		return earliest->value;
	else {
		if (!earliest->next) {
#if 0
			static bool late_warned = false;
			if(step > earliest->step && !late_warned) {
				LOG_WARN("get("<<step<<"): data is not available beyond timestamp "<<earliest->step<<endl);
				late_warned = true;
			}
#endif
			return earliest->value;
		}

		// if piece-wise linear, we need interpolation; otherwise, simply
		// return the value of the earliest step
		/*if(type == PIECE_WISE_LINEAR) {
		 return earliest->value+(earliest->next->value-earliest->value)/
		 (earliest->next->step-earliest->step)*(step-earliest->step);
		 } else*/return earliest->value;
	}
}

FluidHop::FluidHop(FluidQueue* fq, FLUID_CLASSID id, TrafficManager* t_mgr) :
	fluid_queue(fq), class_id(id), traffic_mgr(t_mgr), next(0),
			next_is_ghost(false), is_receiving(false) {
	assert(fluid_queue);
#ifdef DEBUG
	LOG_DEBUG("[interface="<<(fluid_queue->getInterface()->getUName())
			<<", uid="<<fluid_queue->getInterface()->getUID()
			<<"] new fluid hop for class: "<<class_id.first<<"=>"<<class_id.second<<endl)
#endif
	fluid_queue->addFluidHop(traffic_mgr, class_id, this);
	peer_uid = fluid_queue->getInterface()->getUID();
	comm_uid = fluid_queue->getInterface()->inHost()->getCommunity()->getCommunityId();
	prop_delay = fluid_queue->getPropDelay();
}

FluidHop::FluidHop(UID_t peer_id, UID_t comm_id, FLUID_CLASSID cls_id, TrafficManager* traffic_mgr) :
	fluid_queue(0), peer_uid(peer_id), comm_uid(comm_id), class_id(cls_id), traffic_mgr(traffic_mgr),
			next(0), next_is_ghost(false), is_receiving(false) {
	if(peer_uid==0){
		LOG_DEBUG("[peer uid="<<peer_uid<<"] new "<<"last"<<" fluid hop for class: "
				<<class_id.first<<"=>"<<class_id.second<<endl);
	}else {
		LOG_DEBUG("[peer uid="<<peer_uid<<"] new "<<"ghost"<<" fluid hop for class: "
				<<class_id.first<<"=>"<<class_id.second<<endl);
	}
	prop_delay = 0;
}

FluidHop::~FluidHop() {
	if (is_receiving)
		traffic_mgr->resetReceiving(class_id, peer_uid);
	if (fluid_queue) {
		fluid_queue->deleteFluidHop(traffic_mgr, class_id);
		// if the next hop exists
		if (next) {
			// if the next hop is not a ghost hop, we follow the chain;
			// otherwise, we need to additionally inform the downstream
			if (!next->isGhost())
				delete next;
			else {
				next->unregisterFluidClass();
				delete next;
			}
		}
	}
}

void FluidHop::setFirstHop() {
	arrival.setType(FluidSeries::PIECE_WISE_CONSTANT);
	accu_delay.setType(FluidSeries::PIECE_WISE_CONSTANT);
	accu_loss.setType(FluidSeries::PIECE_WISE_CONSTANT);
}

bool FluidHop::isGhost() {
	return !fluid_queue && peer_uid != 0;
}

float FluidHop::getArrival(long step) {
	assert(!isGhost());
	float ret = arrival.get(step);
#ifdef SSFNET_FLUID_ROUNDTRIP
	Interface* curnic = ((Interface*) fluid_queue->getInterface());
	assert(curnic);
	Host* curhost = curnic->inHost();
	UID_t destuid = class_id.second;
	bool isdest = (curhost->getUID() == destuid);
	if (isdest) {
		TrafficManager::FLUID_CLASSES::iterator iter = traffic_mgr->fluid_classes.find(class_id);
		if (iter != traffic_mgr->fluid_classes.end())
			ret /= (*iter).second->getPktsiz() / SSFNET_FLUID_ACK_PKTSIZ;
		else
			ret = 0; // the entry will not be inserted until flow starts
	}
#endif
#ifdef DEBUG
	LOG_DEBUG("[uid="<<peer_uid<<"] "<< VirtualTime(prime::ssf::now()).second()
			<<": getArrival(step="<<step<<")->"<<ret<<endl);
#endif
	return ret;
}

void FluidHop::setArrival(float my_arrival, long step, boolean init) {
#ifdef SSFNET_FLUID_DAMPENING
	if(!init) {
		if(!SSFNET_FLUID_DAMPENING_XING(d_arrival, my_arrival)) {
			//d_arrival = SSFNET_FLUID_DAMPENING_AVG(d_arrival, my_arrival);
			return;
		}
	}
	d_arrival = my_arrival;
#endif
	set_arrival(my_arrival, step);
}

void FluidHop::set_arrival(float my_arrival, long step) {
	if (isGhost()) {
#ifdef DEBUG
		LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
				<<": setArrival(step="<<step<<")<-"<<my_arrival<<" (remote)."<<endl)
#endif
		traffic_mgr->sendFluidEvent(FluidEvent::new_arrival(class_id,
				peer_uid, comm_uid, my_arrival, step));
	} else {
#ifdef DEBUG
		LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
						<<": setArrival(step="<<step<<")->"<<my_arrival<<endl)
#endif
		arrival.set(my_arrival, step);
	}
}

float FluidHop::getDeparture(long step) {
	assert(fluid_queue);
	float ret = departure.get(step);
#ifdef DEBUG
	LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
			<<": getDeparture(step="<<step<<")->"<<ret<<endl)
#endif
	return ret;
}

void FluidHop::setDeparture(float my_departure, long step, boolean init) {
	assert(fluid_queue);
#ifdef SSFNET_FLUID_DAMPENING
	if(!init) {
		if(!SSFNET_FLUID_DAMPENING_XING(d_departure, my_departure)) {
			//d_departure = SSFNET_FLUID_DAMPENING_AVG(d_departure, my_departure);
			return;
		}
	}
	d_departure = my_departure;
#endif
	set_departure(my_departure, step);
}

void FluidHop::set_departure(float my_departure, long step) {
	//XXX, check isGhost()?
#ifdef DEBUG
	LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
			<<": setDeparture(step="<<step<<")<-"<<my_departure<<endl);
#endif
	departure.set(my_departure, step);
}

float FluidHop::getAccuDelay(long step) {
	assert(!isGhost());
	float ret = accu_delay.get(step);
#ifdef DEBUG
	LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
			<<": getAccuDelay(step="<<step<<")->"<<ret<<endl)
#endif
	return ret;
}

void FluidHop::setAccuDelay(float my_accudelay, long step, boolean init) {
#ifdef SSFNET_FLUID_DAMPENING
	if(!init) {
		if(!SSFNET_FLUID_DAMPENING_XING(d_accudelay, my_accudelay)) {
			//d_accudelay = SSFNET_FLUID_DAMPENING_AVG(d_accudelay, my_accudelay);
			return;
		}
	}
	d_accudelay = my_accudelay;
#endif
	set_accudelay(my_accudelay, step);
}

void FluidHop::set_accudelay(float my_accudelay, long step) {
	if (isGhost()) {
#ifdef DEBUG
		LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
				<<": setAccuDelay(step="<<step<<")<-"<<my_accudelay<<" (remote)."<<endl)
#endif
		traffic_mgr->sendFluidEvent(FluidEvent::new_accu_delay(class_id, peer_uid, comm_uid, my_accudelay, step));
	} else {
#ifdef DEBUG
		LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
				<<": setAccuDelay(step="<<step<<")<-"<<my_accudelay<<endl)
#endif
		accu_delay.set(my_accudelay, step);
	}
}

float FluidHop::getAccuLoss(long step) {
	assert(!isGhost());
	float ret = accu_loss.get(step);
#ifdef DEBUG
	LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
			<<": getAccuLoss(step="<<step<<")->"<<ret<<endl)
#endif
	return ret;
}

void FluidHop::setAccuLoss(float my_acculoss, long step, boolean init) {
#ifdef SSFNET_FLUID_DAMPENING
	if(!init) {
		if(!SSFNET_FLUID_DAMPENING_XING(d_acculoss, my_acculoss)) {
			//d_acculoss = SSFNET_FLUID_DAMPENING_AVG(d_acculoss, my_acculoss);
			return;
		}
	}
	d_acculoss = my_acculoss;
#endif
	set_acculoss(my_acculoss, step);
}

void FluidHop::set_acculoss(float my_acculoss, long step) {
	if (isGhost()) {
#ifdef DEBUG
		LOG_DEBUG("[[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
				<<": setAccuLoss(step="<<step<<")<-"<<my_acculoss<<" (remote)."<<endl)
#endif
		traffic_mgr->sendFluidEvent(FluidEvent::new_accu_loss(class_id, peer_uid, comm_uid, my_acculoss, step));
	} else {
#ifdef DEBUG
		LOG_DEBUG("[uid="<<peer_uid<<"] "<<VirtualTime(prime::ssf::now()).second()
				<<": setAccuLoss(step="<<step<<")<-"<<my_acculoss<<endl)
#endif
		accu_loss.set(my_acculoss, step);
	}
}
#ifdef SSFNET_FLUID_ROUNDTRIP
void FluidHop::registerFluidClass(float accu_delay, long pktsiz, bool forward_path, PathMap f_nics, PathMap r_nics) {
	assert(isGhost());
	traffic_mgr->sendFluidEvent(FluidEvent::new_register(class_id, peer_uid, comm_uid,
			accu_delay, pktsiz, forward_path, f_nics, r_nics));
}
#else
void FluidHop::registerFluidClass(float accu_delay, long pktsiz, PathMap f_nics, PathMap r_nics)
{
	assert(isGhost());
	traffic_mgr->sendFluidEvent(FluidEvent::new_register(class_id, peer_uid, comm_uid, accu_delay, pktsiz,
			f_nics, r_nics));
}
#endif

void FluidHop::unregisterFluidClass() {
	assert(isGhost());
	traffic_mgr->sendFluidEvent(FluidEvent::new_unregister(class_id,peer_uid,comm_uid));
}

void FluidHop::prepareReceiving() {
	assert(!isGhost());
	// for the last node, we use the source ip address; that is, the ghost node
	// for the last hop uses the source ip address as its peer ip.
	if (peer_uid == 0)
		traffic_mgr->setReceiving(class_id, class_id.first, this);
	else
		traffic_mgr->setReceiving(class_id, peer_uid, this);
}

}
; // namespace ssfnet
}
; // namespace prime
