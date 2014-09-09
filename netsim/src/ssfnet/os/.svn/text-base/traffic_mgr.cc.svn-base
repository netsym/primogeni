/**
 * \file traffic_mgr.cc
 * \brief Source file for the TrafficManager class.
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
#include <math.h>
#include <stdio.h>

#include "os/traffic_mgr.h"
#include "os/packet.h"
#include "os/packet_event.h"
#include "os/logger.h"
#include "os/community.h"
#include "os/partition.h"
#include "net/interface.h"
#include "net/link.h"
#include "net/host.h"
#include "os/timer_queue.h"
#include "proto/ipv4/ipv4_session.h"
#include "proto/fluid/fluid_queue.h"
#include "proto/fluid/fluid_traffic.h"
#include "proto/fluid/fluid_event.h"

#define DEFAULT_FLUID_STEP_SIZE 0.01

//interval to check for new traffic in MS
#define PROCESS_DYNAMIC_TRAFFIC_INTERVAL 100

//#define LOG_DEBUG(X) { std::cout<<"["<<"traffic_manager.cc"<<":"<<__LINE__<<"]"<<X; }

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(TrafficManager)

RecallTypePairComparison::RecallTypePairComparison(const bool& revparam) :
	reverse(revparam) {
}
bool RecallTypePairComparison::operator()(const RecallTypePair& lhs,
		const RecallTypePair& rhs) const {
	if (reverse)
		return (lhs.first > rhs.first);
	else
		return (lhs.first < rhs.first);
}

class TrafficManagerTimer: public prime::ssf::ssf_timer {
public:
	TrafficManagerTimer(prime::ssf::Entity* ent, TrafficManager* tm) :
		prime::ssf::ssf_timer(ent), traffic_mgr(tm) {
	}

	virtual void callback() {
		traffic_mgr->run();
	}

private:
	TrafficManager* traffic_mgr;
};

#ifdef SSFNET_FLUID_VARYING
class RungeKuttaTimerData : public TimerQueueData {
public:
	RungeKuttaTimerData(VirtualTime t, bool fclass, int handle) :
	TimerQueueData(t), is_fluid_class(fclass), timer_handle(handle) {}

	bool is_fluid_class;
	int timer_handle;
};

class RungeKuttaTimer : public TimerQueue {
public:
	RungeKuttaTimer(Community* comm, TrafficManager* manager) :
	TimerQueue(comm), traffic_mgr(manager) {}

	virtual void callback(TimerQueueData *tqd) {
		traffic_mgr->curstep = prime::ssf::now()/ VirtualTime(traffic_mgr->getStepSize(), VirtualTime::SECOND);
		//LOG_DEBUG("fluid timer callback, curstep="<<traffic_mgr->curstep<<endl)
		traffic_mgr->timerCallback(tqd);
	}

private:
	TrafficManager* traffic_mgr;
};
#else
class RungeKuttaTimer: public prime::ssf::ssf_timer {
public:
	RungeKuttaTimer(prime::ssf::Entity* ent, TrafficManager* manager) :
		prime::ssf::ssf_timer(ent), traffic_mgr(manager) {
		schedule(0); // starts immediately after zero delay
	}

	virtual void callback() {
		// make sure the current step count is always relative to time 0
		// with the prescribed step size
		//LOG_DEBUG("call back time="<<VirtualTime(prime::ssf::now()).second()<<endl)
		traffic_mgr->curstep = prime::ssf::now() / VirtualTime(
				traffic_mgr->getStepSize(), VirtualTime::SECOND);
		traffic_mgr->rungeKuttaStep();
		//LOG_DEBUG("set time="<<VirtualTime(prime::ssf::now()).second()<<endl)
		set();
	}

	void set() {
		//LOG_DEBUG("cur step="<<traffic_mgr->curstep<<", step size="<<VirtualTime(traffic_mgr->getStepSize(), VirtualTime::SECOND).second()<<endl)
		VirtualTime t = (traffic_mgr->curstep + 1) * VirtualTime(traffic_mgr->getStepSize(), VirtualTime::SECOND) - prime::ssf::now();
		//LOG_DEBUG("RungeKuttaTimer cur_time="<<VirtualTime(prime::ssf::now()).second()<<", t="<<t.second()<<endl)
		//LOG_DEBUG("Traffic manager="<<traffic_mgr->getCommunity()->getCommunityId()<<endl)
		schedule(t);
	}

private:
	TrafficManager* traffic_mgr;
};
#endif

#if 0
class FluidTrafficRecvProcess : public prime::ssf::Process {
public:
	FluidTrafficRecvProcess(TrafficManager* manager) :
	prime::ssf::Process(traffic->community, true),
	traffic_mgr(manager) {}

	void action(); //! SSF PROCEDURE SIMPLE

public:
	TrafficManager* traffic_mgr;
};

//! SSF PROCEDURE SIMPLE
void FluidTrafficRecvProcess::action()
{
	//! SSF CALL
	traffic_mgr->recv_fluid_events(this);
}
#endif

TrafficManager::TrafficManager(Community* comm) :
#ifdef SSFNET_FLUID_VARYING
			registered_handle(0), handle_during_update(0),
#endif
			tm_timer(0), community(comm), traffics(0),
			traffic_types(0), min_heap(RecallTypePairComparison(true)),
			curstep(0), fluid_timer(0)  {
	LOG_DEBUG("Created the TrafficManager for community "<<comm->getCommunityId()<<endl)
}

TrafficManager::~TrafficManager() {
	LOG_DEBUG("Delete the TrafficManager for community "<<community->getCommunityId()<<endl)
	if (tm_timer) {
		delete tm_timer;
		tm_timer = 0;
	}
	//For fluid traffic:
	for (FLUID_CLASSES::iterator cit = fluid_classes.begin(); cit
			!= fluid_classes.end(); cit++)
		delete (*cit).second;
	fluid_classes.clear();
	//fluid_classids.clear();

	// fluid queue are removed by the corresponding network interfaces,
	// which happens earlier
	assert(fluid_queues.empty());

	if (fluid_timer) {
#ifndef SSFNET_FLUID_VARYING
		fluid_timer->cancel(); // should have already been canceled
#endif
		//LOG_DEBUG("~~~~~~~~~~~~~~~~~deleting fluid timer"<<endl)
		delete fluid_timer;
	}

#ifdef SSFNET_FLUID_VARYING
	queue_timer_stepsize.clear();
	MAP_QUEUE_TIMER_GROUP::iterator qiter;
	for(qiter = queue_timer_group.begin(); qiter != queue_timer_group.end(); qiter++) {
		delete (*qiter).second;
	}
	queue_timer_group.clear();
	for(qiter = empty_queue_timer_group.begin(); qiter != empty_queue_timer_group.end(); qiter++) {
		delete (*qiter).second;
	}
	empty_queue_timer_group.clear();
	window_timer_stepsize.clear();
	MAP_WINDOW_TIMER_GROUP::iterator witer;
	for(witer = window_timer_group.begin(); witer != window_timer_group.end(); witer++) {
		delete (*witer).second;
	}
	window_timer_group.clear();
	for(witer = empty_window_timer_group.begin(); witer != empty_window_timer_group.end(); witer++) {
		delete (*witer).second;
	}
	empty_window_timer_group.clear();
	added_queues_during_update.clear();
	deleted_queues_during_update.clear();
	added_windows_during_update.clear();
	deleted_windows_during_update.clear();
#endif
}

void TrafficManager::init() {
	bool fluid_only = true;
	LOG_DEBUG("Init traffic manager"<<endl)
	LOG_DEBUG("traffics.size()="<<traffics.size()<<endl)
	//Go through all traffics to find the traffic types belong to this traffic manager.
	for (Traffic::Vector::iterator t = traffics.begin(); t != traffics.end(); t++) {
		LOG_DEBUG("get traffic types from '"<< (*t)->getName()<<"' for community: "<<this->getCommunity()<<endl)
		(*t)->getTrafficTypesForCommunity(this->getCommunity(), traffic_types);
	}
	LOG_DEBUG("Found "<<traffic_types.size()<<" traffic types for community: "<<this->getCommunity()<<endl)
	TrafficType::Vector::iterator tt;
	for(tt=traffic_types.begin(); tt<traffic_types.end(); tt++){
		LOG_DEBUG("There are existing traffic types for this traffic manager."<<endl)
		(*tt)->init(); //in init() of fluid traffic type, insert the fluid type to the fluid_classes of traffic_mgr.
		//check if the traffic type has other traffic types than fluid traffic, if so, set fluid_only to be false.
		if (!FluidTraffic::getClassConfigType()->isSubtype((*tt)->getConfigType())) {
			fluid_only=false;
		}
	}
#ifdef SSFNET_FLUID_VARYING
	if(fluid_timer==0){
		//LOG_DEBUG("~~~~~~~~~~creating a fluid timer."<<endl)
		fluid_timer = new RungeKuttaTimer(community, this);
		assert(fluid_timer);
	}
#endif
	if(!fluid_only){
		tm_timer = new TrafficManagerTimer(this->getCommunity(), this);
		run();
	}
	traffics.clear();
}

void TrafficManager::run() {
	const VirtualTime cur= VirtualTime(this->getCommunity()->now());
	VirtualTime recall_at= cur;
	VirtualTime min=VirtualTime(0);

	if (min_heap.empty()) {
		if(traffic_types.size()>0) {
			//This is the first scan, need to scan all the traffic types.
			//and setup the priority queue
			for (TrafficType::Vector::iterator t = traffic_types.begin(); t
					!= traffic_types.end(); t++) {
				LOG_DEBUG("The start time is "<<SSFNET_DYNAMIC_CAST(StaticTrafficType*,*t)->getStartTime()<<endl);
				if(!FluidTraffic::getClassConfigType()->isSubtype((*t)->getConfigType())){
					//set the recall_at to be the current time
					recall_at=cur;
					processTrafficType(*t, recall_at);
				}
			}
			traffic_types.clear();//we don't need this any more.
		}
		//else we are waiting for new traffic to show up....
	} else {
		TrafficType* cur_tt;
		while (!min_heap.empty()) {
			recall_at=cur;
			min=min_heap.top().first;//update min to be the smallest recall_at value in the current data structure after pop()
			if (min_heap.top().first <= cur) {
				//we need to run this traffic type
				cur_tt=min_heap.top().second;
				LOG_DEBUG("traffic_mgr="<<this<<", heap is not empty, traffic type on the top="
						<<cur_tt->getUID()<<", time="<<min_heap.top().first.second()<<endl);
				min_heap.pop();
				//can add additional events to min heap....
				processTrafficType(cur_tt, recall_at);
			} else {
				break;
			}
		}
	}
	if(min_heap.empty()) {
		//we are waiting for new traffic to show up....
		min = cur+VirtualTime(PROCESS_DYNAMIC_TRAFFIC_INTERVAL, VirtualTime::MILLISECOND);
	}
	else {
		min=min_heap.top().first;
	}
	//LOG_DEBUG("min="<<min.second()<<", cur="<<cur.second()<<", traffic type="<<min_heap.top().second->getUID()<<endl);
	if((min-cur).second()>PROCESS_DYNAMIC_TRAFFIC_INTERVAL){
		LOG_DEBUG("schedule min-cur, min="<<min.second()<<", cur="<<cur.second()<<", recall="<<PROCESS_DYNAMIC_TRAFFIC_INTERVAL<<endl)
		tm_timer->schedule(VirtualTime(PROCESS_DYNAMIC_TRAFFIC_INTERVAL, VirtualTime::MILLISECOND)); //min and cur are both absolute times, but timers what to be schedule by delays...
	}else{
		LOG_DEBUG("schedule min-cur, min="<<min.second()<<", cur="<<cur.second()<<", recall="<<(min-cur).second()<<endl)
		if((min-cur).second()>0){
			tm_timer->schedule(min-cur); //min and cur are both absolute times, but timers what to be schedule by delays..
		}else{
			tm_timer->schedule(0);
		}
	}
}

void TrafficManager::processTrafficType(TrafficType* t, VirtualTime& cur){
	//call getNextEvent() on the traffic type repeatedly while recal_at is 0 until
	//1. wrap_up: set recall_at to be 0 and break the loop
	//2. recall_at!=0: push this (cur+recall_at, traffic type) back to the data structure
	StartTrafficEvent* traffics_to_start = NULL;
	UpdateTrafficTypeEvent* update_event = NULL;
	bool wrap_up = false;
	VirtualTime recall_at= VirtualTime(0);
	while(!recall_at.second()) {
		t->getNextEvent(traffics_to_start, update_event, wrap_up, recall_at);
		if (traffics_to_start != NULL) {
			//set the target_community_id and src_community_id
			UID_t dst_uid = traffics_to_start->getHostUID();
			int target_community_id = Partition::getInstance()->uid2communityId(dst_uid);
			traffics_to_start->setTargetCommunity(target_community_id);
			traffics_to_start->setSrcCommunityID(getCommunity()->getCommunityId());
			LOG_DEBUG("DEBUGGING EVENT, set start evt="<<traffics_to_start
					<<", target comm id="<<traffics_to_start->getTargetCommunity() <<"(="<<target_community_id<<")"
					<<", src comm id="<<traffics_to_start->getSrcCommunityID()<<endl)
			if(getCommunity()->now() < traffics_to_start->getStartTime()) {
				traffics_to_start->setStartTime(getCommunity()->now()+VirtualTime(PROCESS_DYNAMIC_TRAFFIC_INTERVAL*2,VirtualTime::MILLISECOND));
			}
			getCommunity()->deliverEvent(traffics_to_start,0);
		}
		//If wrap_up is true, call wrapup() and remove this pair from MinHeapOfPairs
		if (wrap_up) {
			t->wrapup();
			LOG_DEBUG("Wrap up this traffic type."<<endl)
			recall_at=VirtualTime(0);
			break;
		}
		if (update_event) {
			handleUpdateEvent(update_event);
		}
	}
	if(recall_at.second()){
		LOG_DEBUG("push to min heap, time="<<(cur+recall_at).second()<<", traffic type="<<t->getUID()<<endl);
		min_heap.push(SSFNET_MAKE_PAIR(cur+recall_at,t));
	}
	cur=recall_at;
}

void TrafficManager::addDynamicallyCreatedTraffic(TrafficType* traffic_type) {
	LOG_DEBUG("add dynamically created traffic at time"<<VirtualTime(this->getCommunity()->now()).second()<<endl);
	if(StaticTrafficType::getClassConfigType()->isSubtype(traffic_type->getConfigType())){
		const VirtualTime cur= VirtualTime(this->getCommunity()->now());
		VirtualTime recall_at= cur;
		VirtualTime min=VirtualTime(0);
		LOG_DEBUG("recuall_at="<<recall_at.second()<<", min="<<min.second()<<endl)
		if(SSFNET_STATIC_CAST(StaticTrafficType*,traffic_type)->getOwningCommunityId() == getCommunity()->getCommunityId()) {
			processTrafficType(traffic_type, recall_at);
		}
		if(min_heap.empty()) {
			//we are waiting for new traffic to show up....
			min = cur+VirtualTime(PROCESS_DYNAMIC_TRAFFIC_INTERVAL, VirtualTime::MILLISECOND);
		}
		else {
			min=min_heap.top().first;
		}

		LOG_DEBUG("min="<<min.second()<<", cur="<<cur.second()<<", traffic type="<<min_heap.top().second->getUID()<<", tm_timer="<<tm_timer<<endl);
		if((min-cur).millisecond()>PROCESS_DYNAMIC_TRAFFIC_INTERVAL){
			if(!tm_timer)tm_timer = new TrafficManagerTimer(this->getCommunity(), this);
			LOG_DEBUG("min="<<min.second()<<", cur="<<cur.second()<<", recalling in"<<VirtualTime(PROCESS_DYNAMIC_TRAFFIC_INTERVAL, VirtualTime::MILLISECOND).second()<<endl)
			tm_timer->reschedule(VirtualTime(PROCESS_DYNAMIC_TRAFFIC_INTERVAL, VirtualTime::MILLISECOND)); //min and cur are both absolute times, but timers what to be schedule by delays...
		}else{
			if(!tm_timer)tm_timer = new TrafficManagerTimer(this->getCommunity(), this);
			LOG_DEBUG("schedule min-cur, min="<<min.second()<<", cur="<<cur.second()<<", recall="<<(min-cur).second()<<endl)
			if((min-cur).second()>PROCESS_DYNAMIC_TRAFFIC_INTERVAL){
				tm_timer->reschedule(min-cur); //min and cur are both absolute times, but timers what to be schedule by delays..
			}else{
				tm_timer->reschedule(VirtualTime(PROCESS_DYNAMIC_TRAFFIC_INTERVAL,VirtualTime::MILLISECOND));
			}
		}
	}
	else {
		LOG_ERROR("Can only dynamically add static traffic.\n");
	}
}

void TrafficManager::handleUpdateEvent(UpdateTrafficTypeEvent* evt) {
	//send the update event to all concerned communities
	TrafficType* tt = evt->getTrafficType();
	TrafficType::CommunityIDList community_ids = tt->getCommunityIDList();
	int cur_community_id = getCommunity()->getCommunityId();
	for (TrafficType::CommunityIDList::iterator i = community_ids.begin();
		i != community_ids.end(); i++) {
		if((*i)!=cur_community_id){
			UpdateTrafficTypeEvent* evt_ = new UpdateTrafficTypeEvent(evt);
			evt_->setTargetCommunity(*i);
			getCommunity()->deliverEvent(evt_);
		}
	}
}

float TrafficManager::getStepSize(){
	return DEFAULT_FLUID_STEP_SIZE;
}

Community* TrafficManager::getCommunity() {
	return community;
}

void TrafficManager::addFluidQueue(FluidQueue* queue) {
	//LOG_DEBUG("Inserting fluid queue, community id="<<community->getCommunityId()<<endl)
	fluid_queues.insert(queue);
	//LOG_DEBUG("fluid queue size="<<fluid_queues.size()<<", fluid_timer="<<fluid_timer<<endl)
#ifndef SSFNET_FLUID_VARYING
	if (fluid_queues.size() == 1) {
		if (!fluid_timer) {
			fluid_timer = new RungeKuttaTimer(community, this);
			//LOG_DEBUG("-----create a fluid_timer="<<fluid_timer<<endl)
			assert(fluid_timer);
		} else{
			fluid_timer->set();
		}
	}
#endif
}

void TrafficManager::deleteFluidQueue(FluidQueue* queue) {
	LOG_DEBUG("Deleting fluid queue"<<endl)
	fluid_queues.erase(queue);
#ifndef SSFNET_FLUID_VARYING
	if (fluid_queues.empty()) {
		if (fluid_timer)
			fluid_timer->cancel();
	}
#endif
}

void TrafficManager::setReceiving(FLUID_CLASSID class_id, UID_t peer_uid, FluidHop* hop) {
	fluid_receiving_nodes.insert(SSFNET_MAKE_PAIR(SSFNET_MAKE_PAIR(class_id, peer_uid), hop));
	hop->is_receiving = true;
}

void TrafficManager::resetReceiving(FLUID_CLASSID class_id, UID_t peer_uid) {
	fluid_receiving_nodes.erase(SSFNET_MAKE_PAIR(class_id, peer_uid));
}

void TrafficManager::sendFluidEvent(FluidEvent* fevt)
{
	// We add a tiny delay for fluid events unrelated to registration so
	// that normal events may not come before registration and
	// unregistration events may not come before normal events. THIS IS
	// NECESSARY BECAUSE SIMULTANEOUS EVENTS ACROSS TIMELINES MAY NOT BE
	// RECEIVED IN THE SAME ORDER AS THEY ARE SENT.
	ltime_t delta_delay;
#ifdef SSFNET_FLUID_ROUNDTRIP
	if(fevt->getSSFNetEventType() == SSFNetEvent::SSFNET_FLUID_REGISTER_EVT||fevt->getSSFNetEventType() == SSFNetEvent::SSFNET_FLUID_REGISTER_REVERSE_EVT)
		delta_delay = 0;
#else
	if(fevt->getSSFNetEventType() == SSFNetEvent::SSFNET_FLUID_REGISTER_EVT) delta_delay = 0;
#endif
	else if(fevt->getSSFNetEventType() == SSFNetEvent::SSFNET_FLUID_UNREGISTER_EVT) delta_delay = 2;
	else delta_delay = 1;
	//delta_delay = 0;
	community->deliverEvent(fevt, VirtualTime(delta_delay, VirtualTime::SECOND));
}

void TrafficManager::deleteFluidTimer(){
	LOG_DEBUG("---deleting fluid timer="<<fluid_timer<<", current community="<<community->getCommunityId()<<endl)
	if(fluid_timer) {
#ifndef SSFNET_FLUID_VARYING
		fluid_timer->cancel();
#else
		fluid_timer->clearQueue(false);
#endif
		delete fluid_timer;
		fluid_timer=0;
	}
}

#ifdef SSFNET_FLUID_VARYING
void TrafficManager::setFluidTimer(FluidClass* fclass, float& t){
	if(fluid_timer==0){
		LOG_DEBUG("~~~~~~~~~~creating a fluid timer."<<endl)
		fluid_timer = new RungeKuttaTimer(community, this);
		assert(fluid_timer);
	}
	int newhandle = ++registered_handle;
	SET_WINDOW_TIMER_GROUP* newgrp = new SET_WINDOW_TIMER_GROUP;
	newgrp->insert(fclass);
	window_timer_group.insert(SSFNET_MAKE_PAIR(newhandle, newgrp));
	window_timer_stepsize.insert(SSFNET_MAKE_PAIR(newhandle, t));
	RungeKuttaTimerData* rktd = new RungeKuttaTimerData
	(VirtualTime(t, VirtualTime::SECOND)+prime::ssf::now(), true, newhandle);
	fluid_timer->insert(rktd);
}
#endif

RungeKuttaTimer* TrafficManager::getFluidTimer(){
	return fluid_timer;
}

void TrafficManager::processFluidEvent(FluidEvent* fevt) {
	assert(fevt);
	//LOG_DEBUG("         process fluid event, target community="<<fevt->getTargetCommunity()<<", this community="
			//<<this->getCommunity()->getCommunityId()<<endl)
	//LOG_DEBUG("current time="<<VirtualTime(prime::ssf::now()).second()
			//<<", community="<<fevt->getTargetCommunity()
			//<<", fluid class: "<<fevt->class_id.first<<"=>"<<fevt->class_id.second
			//<<", peer_uid="<<fevt->peer_uid<<", type="<<fevt->getSSFNetEventType()<<", value="<<fevt->fluid_value
			//<<", step="<<fevt->fluid_step<<endl);
	FLUID_RECEIVING_NODES::iterator iter = fluid_receiving_nodes.find(SSFNET_MAKE_PAIR(fevt->class_id, fevt->peer_uid));
	//LOG_DEBUG("check receiving nodes="<<(iter != fluid_receiving_nodes.end())<<endl)
	if (iter != fluid_receiving_nodes.end()) {
		// the receiving node has been registered before
		FluidHop* firsthop = (*iter).second;
		assert(firsthop);

		switch (fevt->getSSFNetEventType()) {
		case SSFNetEvent::SSFNET_FLUID_REGISTER_EVT:
#ifdef SSFNET_FLUID_ROUNDTRIP
			LOG_WARN("Duplicate registration, fluid class: "<<fevt->class_id.first<<"=>"<<fevt->class_id.second<<", peer uid="<<fevt->peer_uid<<endl);
		case SSFNetEvent::SSFNET_FLUID_REGISTER_REVERSE_EVT:
#endif
			// if the receiving node has been registered, it must be the last hop
			if (firsthop->peer_uid != 0) {
				LOG_WARN("Duplicate registration, fluid class: "
						<<fevt->class_id.first<<"=>"<<fevt->class_id.second<<", peer uid="<<fevt->peer_uid<<endl);
			} else {
				// the peer uid used by the ghost node of the last hop must be the source uid
				assert(fevt->class_id.first == fevt->peer_uid);

				// the last hop has been registered during fluid class init;
				// we simply update its state
				long adv = fevt->fluid_step + long(ceil(fevt->fluid_value / getStepSize()));
				firsthop->setArrival(0, adv, true);
				//firsthop->setAccuDelay(fevt->fluid_value*2, 0);
				firsthop->setAccuDelay(fevt->fluid_value, adv, true);
				firsthop->setAccuLoss(0, adv, true);
			}
			break;
		case SSFNetEvent::SSFNET_FLUID_UNREGISTER_EVT:
			resetReceiving(fevt->class_id, fevt->peer_uid);
			delete firsthop;
			break;
		case SSFNetEvent::SSFNET_FLUID_ARRIVAL_EVT:
			firsthop->setArrival(fevt->fluid_value, fevt->fluid_step, true);
			break;
		case SSFNetEvent::SSFNET_FLUID_ACCU_DELAY_EVT:
			firsthop->setAccuDelay(fevt->fluid_value, fevt->fluid_step, true);
			break;
		case SSFNetEvent::SSFNET_FLUID_ACCU_LOSS_EVT:
			firsthop->setAccuLoss(fevt->fluid_value, fevt->fluid_step, true);
			break;
		default:
			LOG_DEBUG("The fluid event type is not recognized!"<<endl)
		}
	} else {
		// this is the first time we receive this type of fluid event
#ifdef SSFNET_FLUID_ROUNDTRIP
		if (fevt->getSSFNetEventType() != SSFNetEvent::SSFNET_FLUID_REGISTER_EVT && fevt->getSSFNetEventType()!= SSFNetEvent::SSFNET_FLUID_REGISTER_REVERSE_EVT)
#else
		if(fevt->getSSFNetEventType() != SSFNetEvent::SSFNET_FLUID_REGISTER_EVT)
#endif
		{
			LOG_ERROR("fluid event unregistered, fluid_event="<<fevt->getSSFNetEventType()<<", fluid class :"<<fevt->class_id.first<<"=>"<<fevt->class_id.second
					<<", peer uid="<<fevt->peer_uid<<endl);
		}
		//get the cur host from the event
		Interface* curnic = SSFNET_DYNAMIC_CAST(Interface*,community->getObject(fevt->peer_uid));
		assert(curnic);
		Host* curhost = SSFNET_DYNAMIC_CAST(Interface*,curnic)->inHost();

		UID_t src_uid = fevt->class_id.first;

		int src_comm_uid=community->getPartition()->uid2communityId(src_uid);
		UID_t dest_uid = fevt->class_id.second;
		LOG_DEBUG("curhost="<<curhost->getUID()<<", src_uid="<<src_uid<<", src comm id="<<src_comm_uid<<", dest_uid="<<dest_uid<<endl)

		float pathlen = fevt->fluid_value; // accumulative path latency
#ifdef SSFNET_FLUID_ROUNDTRIP
		bool forward_path = (fevt->getSSFNetEventType() == SSFNetEvent::SSFNET_FLUID_REGISTER_EVT);
#endif
		FluidHop* prehop = 0;
		for (;;) {
			// have we reached the destination?
			LOG_DEBUG("community="<<community->getCommunityId()<<", curhost="<<curhost->getUName()<<", dest_uid="<<dest_uid<<endl)
			bool isdest = curhost->getUID()==dest_uid;
			// if so, we find the route back to the source;
			// otherwise, we should be on our way to destination
#ifndef SSFNET_FLUID_ROUNDTRIP
			UID_t mydest_uid = isdest ? src_uid : dest_uid;
#else
			if (isdest) {
				forward_path = false;
			}
			UID_t mydest_uid = forward_path ? dest_uid : src_uid;
			LOG_DEBUG("isdest="<<isdest<<", forward_path="<<forward_path<<", mydest_uid="<<mydest_uid<<endl)

			bool issrc = curhost->getUID()==src_uid;
			if (issrc) {
				FLUID_CLASSES::iterator fcls=fluid_classes.find(fevt->class_id);
				setReceiving(fevt->class_id, fevt->peer_uid, fcls->second->last_hop);
				break; // we are done
			}
#endif
			// Get outbound interface
			int outbound_iface = 0;
			int16_t bus_idx = 0;
#ifdef SSFNET_FLUID_ROUNDTRIP
			if(forward_path){
#endif
				FluidHop::PathMap pm_forward = fevt->forward_nics;
				FluidHop::PathMap::iterator it=pm_forward.find(curhost->getUID());
				if (it != pm_forward.end()) {
					outbound_iface = it->second.first;
					bus_idx=it->second.second;
				}else{
#ifndef SSFNET_FLUID_ROUNDTRIP
					FluidHop::PathMap pm_reverse = fevt->reverse_nics;
					FluidHop::PathMap::iterator it=pm_reverse.find(curhost->getUID());
					if (it != pm_reverse.end()) {
						outbound_iface = it->second.first;
						bus_idx=it->second.second;
					}else{
						LOG_ERROR("No route to dst uid="<<mydest_uid<<" on host uid="<<curhost->getUID()<<endl)
					}
#else
					LOG_ERROR("No route to dst uid="<<mydest_uid<<" on host uid="<<curhost->getUID()<<endl)
#endif
				}
#ifdef SSFNET_FLUID_ROUNDTRIP
			}else{
				FluidHop::PathMap pm_reverse = fevt->reverse_nics;
				FluidHop::PathMap::iterator it=pm_reverse.find(curhost->getUID());
				if (it != pm_reverse.end()) {
					outbound_iface = it->second.first;
					bus_idx=it->second.second;
				}else{
					LOG_ERROR("No route to dst uid="<<mydest_uid<<" on host uid="<<curhost->getUID()<<endl)
				}
			}
#endif
			BaseInterface* curnic = SSFNET_DYNAMIC_CAST(Interface*,
					curhost->getCommunity()->getObject(outbound_iface+(curhost->getUID()-curhost->getSize())));

			// find the network queue in the outgoing network interface
			assert(curnic);
			FluidQueue* queue = (FluidQueue*) (SSFNET_DYNAMIC_CAST(Interface*,curnic)->getNicQueue());
			if (!queue || queue->type() != FLUID_QUEUE_TYPE) {
				LOG_ERROR("invalid NIC queue in host "<<curhost->getUName()<<" (fluid class: "
						<<fevt->class_id.first<<"=>"<<fevt->class_id.second<<")"<<endl)
			}
			// create the fluid hop
			FluidHop* hop = new FluidHop(queue, fevt->class_id, this);
			LOG_DEBUG("-----creating a fluid hop: fluid class :"<<fevt->class_id.first<<"=>"<<fevt->class_id.second
					<<", traffic_mgr="<<this->getCommunity()->getCommunityId()<<endl);
			if (prehop)
				prehop->next = hop;
			else
				setReceiving(fevt->class_id, fevt->peer_uid, hop);
			prehop = hop;

			long adv = fevt->fluid_step + long(ceil(pathlen / getStepSize()));
			hop->setArrival(0, adv, true);
			hop->setAccuDelay(pathlen * 2, 0, true); // offset the averaging at time zero
			hop->setAccuDelay(pathlen, adv, true);
			hop->setAccuLoss(0, adv, true);
#ifndef SSFNET_FLUID_ROUNDTRIP
			if(isdest) {
				// we are at the destination; we set the one-way path latency
				// to be the propagation delay and set its next hop to be the
				// last hop (or creating its ghost representation) since we
				// don't model the reverse path
				hop->prop_delay = pathlen;
				iter = fluid_receiving_nodes.find(SSFNET_MAKE_PAIR(fevt->class_id, src_uid));
				if(iter != fluid_receiving_nodes.end()) {
					// if the last hop is right here in this community, simply
					// link directly to the last hop and update its state
					hop->next = (*iter).second;

					float rtt = 2*pathlen;
					long adv = fevt->fluid_step+long(ceil(rtt/getStepSize()));
					hop->next->setArrival(0, adv, true);
					hop->next->setAccuDelay(rtt*2, 0, true); // offset the averaging at time 0
					hop->next->setAccuDelay(rtt, adv, true);
					hop->next->setAccuLoss(0, adv, true);
				} else {
					// if the last hop is somewhere else, we create the ghost
					// node and pass on the information
					FluidHop* newhop = new FluidHop(src_uid, src_comm_uid, fevt->class_id, this);
					LOG_DEBUG("creating a ghost of the last hop for fluid class :"<<fevt->class_id.first<<"=>"<<fevt->class_id.second<<endl);
					hop->next = newhop;
					hop->next_is_ghost = true;
					newhop->registerFluidClass(pathlen, fevt->fluid_step, fevt->forward_nics, fevt->reverse_nics);
				}

				break; // we are done
			}
			else {
#endif
			// we are not at the destination, keep going
			// accumulating the path latency (the mean packet size is hidden in the event)
			pathlen += hop->prop_delay; //+fevt->fluid_step/queue->bitrate;
			LOG_DEBUG("accumulating path length for fluid class: "<<fevt->class_id.first<<"=>"<<fevt->class_id.second
					<<", host="<<curhost->getUName()<<"=>"<<pathlen<<endl);

			// move to the next hop
			Link* link = curnic->getLink();
			Community* src_com=SSFNET_DYNAMIC_CAST(Interface*,curnic)->inHost()->getCommunity();
			LinkInfo* link_info=link->getLinkInfo(src_com);
			LinkInfo::RemoteIface** remotes=link_info->getRemoteInterfaces();
			BaseInterface* nextnic=NULL;
			int remote_idx=-1;
			link->getNextHop(curnic, NULL, src_com, nextnic, remote_idx, bus_idx);
			if (nextnic) { // the next hop is within the same community
				curnic=nextnic;
				curhost = SSFNET_DYNAMIC_CAST(Interface*,curnic)->inHost();
#ifdef SSFNET_FLUID_ROUNDTRIP
				bool issrc = curhost->getUID()==src_uid;
				if (issrc) {
					// we are at the source; we set the roundtrip path latency
					// to be the propogation delay and set its next hop to be the
					// last hop (or creating its ghost representation) .
					hop->prop_delay = pathlen;
					iter = fluid_receiving_nodes.find(SSFNET_MAKE_PAIR(fevt->class_id, src_uid));
					assert(iter != fluid_receiving_nodes.end());
					// the last hop is  in the same community as the source hop, simply
					// link directly to the last hop and update its state
					hop->next = (*iter).second;

					float rtt = 2* pathlen ;
					long adv = fevt->fluid_step + long(ceil(rtt / getStepSize()));
					hop->next->setArrival(0, adv, true);
					hop->next->setAccuDelay(rtt * 2, 0, true); // offset the averaging at time 0
					hop->next->setAccuDelay(rtt, adv, true);
					hop->next->setAccuLoss(0, adv, true);

					break; // we are done
				}
#endif
				// continue the for loop...
			} else { // the next hop is not in the current timeline
				// we need to create a ghost node for the next hop

				UID_t nextnic_uid = remotes[remote_idx]->iface->getUIDOfRealInterface(); //the uid of the next hop
				UID_t remote_comm_uid = remotes[remote_idx]->remote_com_id;
				hop = new FluidHop(nextnic_uid, remote_comm_uid, fevt->class_id, this);
				//hop = new FluidHop(nextnic_uid, remote_comm_uid, fevt->class_id, owning_tm);
				LOG_DEBUG("creating ghost hop: fluid class: "<<fevt->class_id.first<<"=>"<<fevt->class_id.second
						<<", host_uid="<<nextnic_uid<<endl);
				prehop->next = hop;
				prehop->next_is_ghost = true;
#ifdef SSFNET_FLUID_ROUNDTRIP
				hop->registerFluidClass(pathlen, fevt->fluid_step, forward_path, fevt->forward_nics, fevt->reverse_nics);
#else
				hop->registerFluidClass(pathlen, fevt->fluid_step, fevt->forward_nics,fevt->reverse_nics);
#endif

				// important: we need to sent back the new lower bound of
				// the path round trip time back to the source; otherwise,
				// the source may request information of the fluid series
				// beyond the lower bound set by itself
#ifdef SSFNET_FLUID_ROUNDTRIP
				sendFluidEvent(FluidEvent::new_register(fevt->class_id, src_uid, src_comm_uid, 2*pathlen,
						fevt->fluid_step, forward_path, fevt->forward_nics, fevt->reverse_nics));
#else
				sendFluidEvent(FluidEvent::new_register(fevt->class_id, src_uid, src_comm_uid, 2*pathlen,
						fevt->fluid_step, fevt->forward_nics, fevt->reverse_nics));
#endif
				break; // we are done
			}
#ifndef SSFNET_FLUID_ROUNDTRIP
		}
#endif
		} // for loop
	}
	// reclaiming the event is done automatically by ssf
}

#ifdef SSFNET_FLUID_VARYING
void TrafficManager::timerCallback(TimerQueueData* tqd)
{
	RungeKuttaTimerData* rktd = (RungeKuttaTimerData*)tqd;
	if(rktd->is_fluid_class) {
		MAP_WINDOW_TIMER_GROUP::iterator iter = window_timer_group.find(rktd->timer_handle);
		if(iter == window_timer_group.end()) {
			delete rktd;
			return;
		}
		SET_WINDOW_TIMER_GROUP* wgrp = (*iter).second;
		assert(wgrp && !wgrp->empty());
		handle_during_update = rktd->timer_handle;
		for(SET_WINDOW_TIMER_GROUP::iterator witer = wgrp->begin(); witer != wgrp->end(); witer++) {
			(*witer)->updateWindow();
		}
		handle_during_update = 0;
		if(!added_windows_during_update.empty()) {
			for(SET_WINDOW_TIMER_GROUP::iterator xiter = added_windows_during_update.begin();
					xiter != added_windows_during_update.end(); xiter++) {
				wgrp->insert(*xiter);
			}
			added_windows_during_update.clear();
		}
		if(!deleted_windows_during_update.empty()) {
			for(SET_WINDOW_TIMER_GROUP::iterator xiter = deleted_windows_during_update.begin();
					xiter != deleted_windows_during_update.end(); xiter++) {
				wgrp->erase(*xiter);
			}
			deleted_windows_during_update.clear();
		}
		if(!wgrp->empty()) {
			MAP_TIMER_STEPSIZE::iterator siter = window_timer_stepsize.find(rktd->timer_handle);
			assert(siter != window_timer_stepsize.end());
			rktd->addTime(VirtualTime((*siter).second, VirtualTime::SECOND));
			fluid_timer->insert(rktd);
		} else {
			window_timer_group.erase(iter);
			empty_window_timer_group.insert(SSFNET_MAKE_PAIR(rktd->timer_handle, wgrp));
			delete rktd;
		}
	} else {
		MAP_QUEUE_TIMER_GROUP::iterator iter = queue_timer_group.find(rktd->timer_handle);
		if(iter == queue_timer_group.end()) {
			delete rktd;
			return;
		}
		SET_QUEUE_TIMER_GROUP* qgrp = (*iter).second;
		assert(qgrp && !qgrp->empty());
		handle_during_update = rktd->timer_handle;
		for(SET_QUEUE_TIMER_GROUP::iterator qiter = qgrp->begin(); qiter != qgrp->end(); qiter++) {
			(*qiter)->updateQueue(this);
		}
		handle_during_update = 0;
		if(!added_queues_during_update.empty()) {
			for(SET_QUEUE_TIMER_GROUP::iterator xiter =added_queues_during_update.begin();
					xiter != added_queues_during_update.end();xiter++) {
				qgrp->insert(*xiter);
			}
			added_queues_during_update.clear();
		}
		if(!deleted_queues_during_update.empty()) {
			for(SET_QUEUE_TIMER_GROUP::iterator xiter = deleted_queues_during_update.begin();
					xiter != deleted_queues_during_update.end(); xiter++) {
				qgrp->erase(*xiter);
			}
			deleted_queues_during_update.clear();
		}
		if(!qgrp->empty()) {
			MAP_TIMER_STEPSIZE::iterator siter = queue_timer_stepsize.find(rktd->timer_handle);
			assert(siter != queue_timer_stepsize.end());
			rktd->addTime(VirtualTime((*siter).second, VirtualTime::SECOND));
			fluid_timer->insert(rktd);
		} else {
			queue_timer_group.erase(iter);
			empty_queue_timer_group.insert(SSFNET_MAKE_PAIR(rktd->timer_handle, qgrp));
			delete rktd;
		}
	}
}

int TrafficManager::schedQueueUpdate(FluidQueue* fqueue, float& t)
{
	// caution: this is a linear search; there's a better way!
	for(MAP_TIMER_STEPSIZE::iterator iter = queue_timer_stepsize.begin();
			iter != queue_timer_stepsize.end(); iter++) {
		if((*iter).second <= t && t < 2*(*iter).second) {
			t = (*iter).second;
			if(handle_during_update == (*iter).first) {
				SET_QUEUE_TIMER_GROUP::iterator myiter = deleted_queues_during_update.find(fqueue);
				if(myiter != deleted_queues_during_update.end()) {
					deleted_queues_during_update.erase(myiter);
				} else {
					added_queues_during_update.insert(fqueue);
				}
				//printf("sched queue: handle=%d (during upate), time=%g\n", (*iter).first, t*1e9);
			} else {
				MAP_QUEUE_TIMER_GROUP::iterator giter =
				queue_timer_group.find((*iter).first);
				if(giter != queue_timer_group.end()) {
					(*giter).second->insert(fqueue);
					//printf("sched queue: handle=%d, time=%g (%d)\n", (*iter).first, t*1e9, (int)(*giter).second->size());
				} else {
					giter = empty_queue_timer_group.find((*iter).first);
					assert(giter != empty_queue_timer_group.end());
					assert((*giter).second->empty());
					(*giter).second->insert(fqueue);
					//printf("sched queue: handle=%d, time=%g (%d)\n", (*iter).first, t*1e9, (int)(*giter).second->size());
					queue_timer_group.insert(SSFNET_MAKE_PAIR((*iter).first, (*giter).second));
					empty_queue_timer_group.erase(giter);
					RungeKuttaTimerData* rktd = new RungeKuttaTimerData
					(VirtualTime(t, VirtualTime::SECOND)+prime::ssf::now(),false, (*iter).first);
					fluid_timer->insert(rktd);
				}
			}
			return (*iter).first;
		}
	}
	int newhandle = ++registered_handle;
	SET_QUEUE_TIMER_GROUP* newgrp = new SET_QUEUE_TIMER_GROUP;
	newgrp->insert(fqueue);
	queue_timer_group.insert(SSFNET_MAKE_PAIR(newhandle, newgrp));
	queue_timer_stepsize.insert(SSFNET_MAKE_PAIR(newhandle, t));
	RungeKuttaTimerData* rktd = new RungeKuttaTimerData(VirtualTime(t, VirtualTime::SECOND)+prime::ssf::now(), false, newhandle);
	LOG_DEBUG("fluid_timer is: "<<fluid_timer<<endl)
	fluid_timer->insert(rktd);
	//printf("sched queue: handle=%d, time=%g (new)\n", newhandle, t*1e9);
	return newhandle;
}

void TrafficManager::unschedQueueUpdate(int handle, FluidQueue* fqueue)
{
	if(handle_during_update == handle) {
		SET_QUEUE_TIMER_GROUP::iterator myiter = added_queues_during_update.find(fqueue);
		if(myiter != added_queues_during_update.end()) {
			added_queues_during_update.erase(myiter);
		} else {
			deleted_queues_during_update.insert(fqueue);
		}
		//printf("unsched queue: handle=%d (during update)\n", handle);
	} else {
		MAP_QUEUE_TIMER_GROUP::iterator giter = queue_timer_group.find(handle);
		assert(giter != queue_timer_group.end());
		(*giter).second->erase(fqueue);
		//printf("unsched queue: handle=%d (%d left)\n", handle, (int)(*giter).second->size());
		if((*giter).second->empty()) {
			empty_queue_timer_group.insert(SSFNET_MAKE_PAIR(handle, (*giter).second));
			queue_timer_group.erase(giter);
		}
	}
}

int TrafficManager::schedWindowUpdate(FluidClass* fclass, float& t)
{
	// caution: this is a linear search; there's a better way!
	for(MAP_TIMER_STEPSIZE::iterator iter = window_timer_stepsize.begin(); iter != window_timer_stepsize.end(); iter++) {
		if((*iter).second <= t && t < 2*(*iter).second) {
			t = (*iter).second;
			if(handle_during_update == (*iter).first) {
				SET_WINDOW_TIMER_GROUP::iterator myiter = deleted_windows_during_update.find(fclass);
				if(myiter != deleted_windows_during_update.end()) {
					deleted_windows_during_update.erase(myiter);
				} else {
					added_windows_during_update.insert(fclass);
				}
			} else {
				MAP_WINDOW_TIMER_GROUP::iterator giter = window_timer_group.find((*iter).first);
				if(giter != window_timer_group.end()) {
					(*giter).second->insert(fclass);
				} else {
					giter = empty_window_timer_group.find((*iter).first);
					assert(giter != empty_window_timer_group.end());
					assert((*giter).second->empty());
					(*giter).second->insert(fclass);
					window_timer_group.insert(SSFNET_MAKE_PAIR((*iter).first, (*giter).second));
					empty_window_timer_group.erase(giter);
					RungeKuttaTimerData* rktd = new RungeKuttaTimerData
					(VirtualTime(t, VirtualTime::SECOND)+prime::ssf::now(), true, (*iter).first);
					fluid_timer->insert(rktd);
				}
				//printf("sched window: handle=%d, time=%g (%d)\n", (*iter).first, t*1e9, (*giter).second->size());
			}
			return (*iter).first;
		}
	}
	int newhandle = ++registered_handle;
	SET_WINDOW_TIMER_GROUP* newgrp = new SET_WINDOW_TIMER_GROUP;
	newgrp->insert(fclass);
	window_timer_group.insert(SSFNET_MAKE_PAIR(newhandle, newgrp));
	window_timer_stepsize.insert(SSFNET_MAKE_PAIR(newhandle, t));
	RungeKuttaTimerData* rktd = new RungeKuttaTimerData
	(VirtualTime(t, VirtualTime::SECOND)+prime::ssf::now(), true, newhandle);
	fluid_timer->insert(rktd);
	//printf("sched window: handle=%d, time=%g (new)\n", newhandle, t*1e9);
	return newhandle;
}

void TrafficManager::unschedWindowUpdate(int handle, FluidClass* fclass)
{
	if(handle_during_update == handle) {
		SET_WINDOW_TIMER_GROUP::iterator myiter = added_windows_during_update.find(fclass);
		if(myiter != added_windows_during_update.end()) {
			added_windows_during_update.erase(myiter);
		} else {
			deleted_windows_during_update.insert(fclass);
		}
	} else {
		MAP_WINDOW_TIMER_GROUP::iterator giter = window_timer_group.find(handle);
		assert(giter != window_timer_group.end());
		(*giter).second->erase(fclass);
		//printf("unsched window: handle=%d (%d left)\n", handle, (*giter).second->size());
		if((*giter).second->empty()) {
			empty_window_timer_group.insert(SSFNET_MAKE_PAIR(handle, (*giter).second));
			window_timer_group.erase(giter);
		}
	}
}
/*
 int TrafficManager::schedWindowUpdate(FluidClass* fclass, float& t)
 {
 for(MAP_TIMER_STEPSIZE::iterator iter = window_timer_stepsize.begin();
 iter != window_timer_stepsize.end(); iter++) {
 if((*iter).second <= t && t <= 2*(*iter).second) {
 if(handle_during_update == (*iter).first) {
 added_windows_during_update.insert(fclass);
 } else {
 MAP_WINDOW_TIMER_GROUP::iterator giter = window_timer_group.find((*iter).first);
 assert(giter != window_timer_group.end());
 (*giter).second->insert(fclass);
 t = (*iter).second;
 //printf("sched window: handle=%d, time=%g (%d)\n", (*iter).first, t*1e9, (*giter).second->size());
 }
 return (*iter).first;
 }
 }
 int newhandle = ++registered_handle;
 SET_WINDOW_TIMER_GROUP* newgrp = new SET_WINDOW_TIMER_GROUP;
 newgrp->insert(fclass);
 window_timer_group.insert(SSFNET_MAKE_PAIR(newhandle, newgrp));
 window_timer_stepsize.insert(SSFNET_MAKE_PAIR(newhandle, t));
 RungeKuttaTimerData* rktd = new RungeKuttaTimerData
 (VirtualTime(t, VirtualTime::SECOND)+prime::ssf::now(), true, newhandle);
 fluid_timer->insert(rktd);
 //printf("sched window: handle=%d, time=%g (new)\n", newhandle, t*1e9);
 return newhandle;
 }

 void TrafficManager::unschedWindowUpdate(int handle, FluidClass* fclass)
 {
 if(handle_during_update == handle) {
 deleted_windows_during_update.insert(fclass);
 } else {
 MAP_WINDOW_TIMER_GROUP::iterator giter = window_timer_group.find(handle);
 assert(giter != window_timer_group.end());
 (*giter).second->erase(fclass);
 //printf("unsched window: handle=%d (%d left)\n", handle, (*giter).second->size());
 if((*giter).second->empty()){
 window_timer_group.erase(giter);
 }
 }
 }
 */
#else
void TrafficManager::rungeKuttaStep() {

	//LOG_DEBUG("current time is: "<<VirtualTime(prime::ssf::now()).second()
			//<<", runge_kutta_step() on community: "<<getCommunity()->getCommunityId()
			//<<", curstep="<<curstep<<endl);
	for (FLUID_CLASSES::iterator cit = fluid_classes.begin(); cit!= fluid_classes.end(); cit++){
		//LOG_DEBUG("in runge kutta step, fluid class="<<(*cit).first.first<<"=>"<<(*cit).first.second<<endl)
		(*cit).second->windowEvolution(getStepSize());
	}
	for (FLUID_QUEUES::iterator qit = fluid_queues.begin(); qit!= fluid_queues.end(); qit++)
		(*qit)->queueEvolution(getStepSize());
	stateEvolution();
}

void TrafficManager::stateEvolution() {
	// suppose that fluid updates have arrived from other timelines...

	// update arrival rate at the first queue of each fluid class
	for (FLUID_CLASSES::iterator cit = fluid_classes.begin(); cit!= fluid_classes.end(); cit++) {
		// it's possible that the flows haven't got started
		if (!(*cit).second->first_hop)
			continue;
		if ((*cit).second->getFluidProtoType() == FluidTraffic::FLUID_TYPE_UDP) {
			// udp uses window as send rate
			(*cit).second->first_hop->setArrival((*cit).second->getWindow(), curstep);
		} else {
#ifdef SSFNET_FLUID_STOCHASTIC
			(*cit).second->first_hop->setArrival((*cit).second->getWindow()
					* (*cit).second->nsessions / (*cit).second->getRtt(),
					curstep);
#else
			(*cit).second->first_hop->setArrival
			((*cit).second->getWindow()*(*cit).second->getNFlows()/(*cit).second->getRtt(), curstep);
#endif
		}
	}

	// update tcp state variables at each queue
	for (FLUID_QUEUES::iterator qit = fluid_queues.begin(); qit!= fluid_queues.end(); qit++) {
		// get the aggregate arrival rate at the queue
		SSFNET_MAP(FLUID_CLASSID, FluidHop*)::iterator fit;
		float arrival = 0; // aggregate arrival for both fluid and packet
		for (fit = (*qit)->fluid_map.begin(); fit != (*qit)->fluid_map.end(); fit++)
			arrival += (*fit).second->getArrival(curstep);
		(*qit)->setAggrArrival(arrival); // fluid only here
		if ((*qit)->getPktArrival() > 0) {
			arrival += (*qit)->getPktArrival() / getStepSize();
			(*qit)->setPktArrival(0);
		}

		float qdelay = (*qit)->getQueue() / (*qit)->getBitRate(); // queuing time
		long adv = long(ceil(qdelay / getStepSize()));

		// update state of each fluid traversing this queue
		for (fit = (*qit)->fluid_map.begin(); fit != (*qit)->fluid_map.end(); fit++) {
			// update departure rate for each fluid from the queue

			// important: we don't differentiate whether it's gonna take
			// time or not for the rates to propagate through the queue
			if (arrival * (1 - (*qit)->getLoss()) <= (*qit)->getBitRate()) {
				// if the aggregate arrival is less than the bandwidth, all
				// arrival rates (minus loss) pass through instantly
				(*fit).second->setDeparture((*fit).second->getArrival(curstep)
						* (1 - (*qit)->getLoss()), curstep + adv);
			} else {
				// if the aggregate arrival is larger than the bandwidth,
				// the arrival rates are proportioned
				(*fit).second->setDeparture((*fit).second->getArrival(curstep)
						* (*qit)->getBitRate() / arrival, curstep + adv); // BUG FIX: NOT CURSTEP!!!
			}

			// set the flow values at the next queue (should always be
			// there); note that the prop delay of the last leg is stretched
			// to include the path latency (on the returning trip)
			FluidHop* nexthop = (*fit).second->next;
			assert(nexthop);

			// update arrival rate
			static bool prop_warned = false;
			if ((*fit).second->prop_delay < getStepSize() && !prop_warned) {
				LOG_WARN("propagation delay "<<(*fit).second->prop_delay<<" less than R-K step size "
						<<getStepSize()<<" for fluid class "<<(*fit).first.first<<"=>"<<(*fit).first.second
						<<" on "<<(*qit)->getInterface()->getUName()<<endl);
				prop_warned = true;
			}
			long newpos = curstep + long((*fit).second->prop_delay / getStepSize() + 0.5);
			nexthop->setArrival((*fit).second->getDeparture(curstep), newpos);

			// update accumulated delay
			float newvalue = qdelay + (*fit).second->prop_delay;
			newvalue += (*fit).second->getAccuDelay(curstep);
			newpos = curstep + long((qdelay + (*fit).second->prop_delay) / getStepSize() + 0.5);
			nexthop->setAccuDelay(newvalue, newpos);

			// update accumulated loss rate
			newvalue = (*qit)->getLoss() * (*fit).second->getArrival(curstep);
			newvalue += (*fit).second->getAccuLoss(curstep);
			nexthop->setAccuLoss(newvalue, newpos);
		}
	}

	// update tcp's rtt and loss rate
	for (FLUID_CLASSES::iterator cit = fluid_classes.begin(); cit
			!= fluid_classes.end(); cit++) {
		// it's possible that the flows haven't got started
		if (!(*cit).second->first_hop)
			continue;
		FluidHop* lasthop = (*cit).second->last_hop;
		assert(lasthop && !lasthop->fluid_queue);
		(*cit).second->setRtt(lasthop->getAccuDelay(curstep));
		//printf("===> %g, %d\n", lasthop->getAccuLoss(curstep), (*cit).second->nsessions);
#ifdef SSFNET_FLUID_STOCHASTIC
		if ((*cit).second->nsessions > 0)
			(*cit).second->setLoss(lasthop->getAccuLoss(curstep) / (*cit).second->nsessions / (*cit).second->getPktsiz());
		else
			(*cit).second->setLoss(0);
#else
		(*cit).second->setLoss(lasthop->getAccuLoss(curstep)/(*cit).second->getNFlows()/(*cit).second->getPktsiz());
#endif
		if ((*cit).second->getFluidProtoType() == FluidTraffic::FLUID_TYPE_TCP_NEWRENO
				|| (*cit).second->getFluidProtoType() == FluidTraffic::FLUID_TYPE_TCP_SACK) {
			// we need to compute the effective loss rate for newreno and sack
#ifdef SSFNET_FLUID_STOCHASTIC
			if ((*cit).second->nsessions > 0) {
				float a = ((*cit).second->last_hop->getArrival(curstep)
						+ (*cit).second->last_hop->getAccuLoss(curstep))
						/ (*cit).second->nsessions / (*cit).second->getPktsiz();
				(*cit).second->setLoss((1 - pow(1 - (*cit).second->getLoss()
						/ a, (*cit).second->getRtt() * a))
						/ (*cit).second->getRtt());
			}
#else
			float a = ((*cit).second->last_hop->getArrival(curstep)+
					(*cit).second->last_hop->getAccuLoss(curstep))/(*cit).second->getNFlows()/(*cit).second->getPktsiz();
			(*cit).second->setLoss((1-pow(1-(*cit).second->getLoss()/a, (*cit).second->getRtt()*a))/(*cit).second->getRtt());
#endif
		}
#ifdef DEBUG
		LOG_DEBUG("current time: "<<VirtualTime(prime::ssf::now()).second()<<", state_evolution for community "
				<<community->getCommunityId()<<": fluid class="<<(*cit).first.first<<"=>"<<(*cit).first.second
				<<", rtt="<<(*cit).second->getRtt()<<", loss="<<(*cit).second->getLoss()<<endl);
#endif
	}
}
#endif

TrafficManager::FLUID_CLASSES& TrafficManager::getFluidClasses(){
	return fluid_classes;
}

TrafficManager::FLUID_QUEUES& TrafficManager::getFluidQueues(){
	return fluid_queues;
}
 /*SSFNET_FLUID_VARYING*/

}// namespace ssfnet
}// namespace prime
