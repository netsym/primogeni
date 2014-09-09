/**
 * \file traffic_mgr.h
 * \brief Header file for the TrafficManager class.
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

#ifndef __TRAFFIC_MGR_H__
#define __TRAFFIC_MGR_H__

#include "ssf.h"
#include "os/ssfnet_types.h"
#include "os/virtual_time.h"
#include "os/community.h"
#include "os/ssfnet.h"
#include "net/link.h"
#include "os/traffic.h"
#include "net/net.h"
#include "proto/fluid/fluid_hop.h"

#ifdef SSFNET_FLUID_VARYING
#include "os/timer_queue.h"
#endif

namespace prime {
namespace ssfnet {

class FluidTraffic;
class FluidClass;
class FluidQueue;
class FluidEvent;
class RungeKuttaTimer;

class TrafficManagerTimer;

//traffic type's recall time and the type itself.
typedef SSFNET_PAIR(VirtualTime, TrafficType*) RecallTypePair;

class RecallTypePairComparison{
public:
	RecallTypePairComparison(const bool& revparam=false);
	bool operator()(const RecallTypePair& lhs, const RecallTypePair& rhs) const;
private:
	bool reverse;
};

/**
 * This class provides management of traffic, it can schedule
 * both the start and the end of the traffic.
 * There is one such object for each community.
 * For distributed traffic type, each traffic manager only manages the traffic within its community.
 * For centralized traffic type, within a partition, only one traffic manager manages the traffic in all
 * the communities within this partition. And that traffic manager is chosen arbitrarily.
 */

class TrafficManager {
	friend class Community;

public:
	typedef SSFNET_PRIORITY_QUEUE(RecallTypePair,
			SSFNET_VECTOR(RecallTypePair),
	        RecallTypePairComparison) MinHeapOfPairs;
	typedef	SSFNET_MAP(FLUID_CLASSID, FluidClass*) FLUID_CLASSES;
	typedef SSFNET_SET(FluidQueue*) FLUID_QUEUES;

	/** The constructor. */
	TrafficManager(Community* p);

	/** The destructor. */
	virtual ~TrafficManager();

	/**
	 * This method is used to initialize the traffic manager once
	 * it has been created by model builder. It will process all traffic, traffic types.
	 */
	virtual void init();

	/**
	 * The method scans the traffic types need to be called and triggers
	 * the start traffic event on the corresponding host.
	 */
	void run();

	/**
	 * This function returns the community that owns the traffic manager,
	 * the traffic manager then uses the community information to figure
	 * out the traffics of its own (the traffics belong to this community)
	 */
	Community* getCommunity();

	/**
	 * This method handles the update event.
	 */
    void handleUpdateEvent(UpdateTrafficTypeEvent* evt);

    /**
     * This method calls the getNextEvent() on the traffic type immediately if the recall_at is 0,
     * and update the recall_at if it isn't 0.
     */
    void processTrafficType(TrafficType* t, VirtualTime& cur);

    /**
     * called by community to add traffics that are created on-demand (i.e. dynamically)
     */
	void addDynamicallyCreatedTraffic(TrafficType* traffic_type);

	/** xxx, Return the default step size, will change it to be configurable later. */
	float getStepSize();

	//The following methods are used for managing fluid traffic type.

	/** Add a fluid queue that belong to this community; the queue will be updated during each Runge-Kutta step. */
	void addFluidQueue(FluidQueue* queue);

	/** Remove a fluid queue from the set maintained by this
	 community. The queue will no longer be updated during the
	 Runge-Kutta step. */
	void deleteFluidQueue(FluidQueue* queue);

	/** Set the given fluid hop to receive the fluid event. */
	void setReceiving(FLUID_CLASSID class_id, UID_t peer_uid, FluidHop* hop);

	/** Remove the fluid hop from receivng the fluid event. */
	void resetReceiving(FLUID_CLASSID class_id, UID_t peer_uid);

	/** Ask the community to deliver the event to update of fluid variables maintained by another timeline. */
	void sendFluidEvent(FluidEvent* fevt);

	void deleteFluidTimer();

	RungeKuttaTimer* getFluidTimer();
#ifdef SSFNET_FLUID_VARYING
	void setFluidTimer(FluidClass* fclass, float& t);
#endif

	FLUID_CLASSES& getFluidClasses();
	FLUID_QUEUES& getFluidQueues();


protected:
	friend class FluidHop;
	friend class FluidTraffic;
	friend class FluidClass;
	friend class FluidQueue;
	friend class RungeKuttaTimer;
	friend class FluidTrafficRecvProcess;

#ifdef SSFNET_FLUID_VARYING
	typedef SSFNET_MAP(int, float) MAP_TIMER_STEPSIZE;
	typedef SSFNET_SET(FluidQueue*) SET_QUEUE_TIMER_GROUP;
	typedef SSFNET_MAP(int, SET_QUEUE_TIMER_GROUP*) MAP_QUEUE_TIMER_GROUP;
	typedef SSFNET_SET(FluidClass*) SET_WINDOW_TIMER_GROUP;
	typedef SSFNET_MAP(int, SET_WINDOW_TIMER_GROUP*) MAP_WINDOW_TIMER_GROUP;
	int registered_handle;
	int handle_during_update;
	MAP_TIMER_STEPSIZE queue_timer_stepsize;
	MAP_QUEUE_TIMER_GROUP queue_timer_group;
	MAP_QUEUE_TIMER_GROUP empty_queue_timer_group;
	MAP_TIMER_STEPSIZE window_timer_stepsize;
	MAP_WINDOW_TIMER_GROUP window_timer_group;
	MAP_WINDOW_TIMER_GROUP empty_window_timer_group;
	SET_QUEUE_TIMER_GROUP added_queues_during_update;
	SET_QUEUE_TIMER_GROUP deleted_queues_during_update;
	SET_WINDOW_TIMER_GROUP added_windows_during_update;
	SET_WINDOW_TIMER_GROUP deleted_windows_during_update;

	void timerCallback(TimerQueueData* tqd);
	int schedQueueUpdate(FluidQueue* fqueue, float& t);
	void unschedQueueUpdate(int handle, FluidQueue* fqueue);
	int schedWindowUpdate(FluidClass* fclass, float& t);
	void unschedWindowUpdate(int handle, FluidClass* fclass);
#else
	/** Called to solve the fluid model for each step. */
	void rungeKuttaStep();

	/** Update the fluid flows per Runge-Kutta step. */
	void stateEvolution();
#endif

	/** Process the receiving fluid event. */
	void processFluidEvent(FluidEvent* fevt);

private:
	/** Used to run traffic manager */
	TrafficManagerTimer* tm_timer;

	/**
	 * The community that owns this traffic manager.
	 */
	Community* community;
	/**
	 * Traffics in this community
	 **/
	Traffic::Vector traffics;

	/**
	 * Traffic types in this community
	 **/
	TrafficType::Vector traffic_types;

	/**
	 * Used to store the recall time and traffic type pairs
	 * and schedule the time to call getNextEvent() on
	 * the corresponding traffic type.
	 */
	MinHeapOfPairs min_heap;

	//The following variables are used for managing fluid traffic type.

	/** Enumeration of the current Runge-Kutta step. */
	long curstep;

	typedef SSFNET_PAIR(FLUID_CLASSID, UID_t) FLUID_INCOMING;
	typedef SSFNET_MAP(FLUID_INCOMING, FluidHop*) FLUID_RECEIVING_NODES;

	/** A set of fluid classes initiated from this community. */
	FLUID_CLASSES fluid_classes;

	/** A set of fluid queues in the system that need to be updated
	 during the Runge-Kutta step. */
	FLUID_QUEUES fluid_queues;

	/** A set of fluid class ids used internally to prevent duplicate
	 fluid classes (with the same source and destination uids). */
	//FLUID_CLASSIDS fluid_classids;

	/** The timer is used to carry out each Runge-Kutta step. */
	RungeKuttaTimer* fluid_timer;

	/** Map from fluid class id and uid to fluid hop at the
	 network queue that corresponds to the uid which is
	 maintained by the current community. This data structure is used
	 for receiving fluid events from remote timelines. */
	FLUID_RECEIVING_NODES fluid_receiving_nodes;

};


}// namespace ssfnet
}// namespace prime

#endif /*__TRAFFIC_MGR_H__*/
