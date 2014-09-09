/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file fluid_queue.h
 * \brief Header file for the FluidQueue class.
 * \author Ting Li
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

#ifndef __FLUID_QUEUE_H__
#define __FLUID_QUEUE_H__

#include <stdio.h>
#include "os/virtual_time.h"
#include "net/nic_queue.h"
#include "fluid_hop.h"

namespace prime {
namespace ssfnet {

#define FLUID_QUEUE_TYPE 3
#define FLUID_QUEUE_NAME "mixfluid"

#define FLUID_QUEUE_DEFAULT_WEIGHT 0.0001
#define FLUID_QUEUE_DEFAULT_PMAX 0.2
#define FLUID_QUEUE_DEFAULT_MEAN_PKTSIZ 500

class TrafficManager;
class FluidClass;

/**
 * \brief A queue (either RED or droptail) used inside a network
 * interface that conducts both fluid flows and packet flows.
 */

class FluidQueue: public ConfigurableEntity<FluidQueue, NicQueue> {
public:
	typedef SSFNET_MAP(FLUID_CLASSID, FluidHop*) FluidMap;
	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(SSFNET_STRING, queue_type)
		SSFNET_CONFIG_PROPERTY_DECL(bool, is_red)
		SSFNET_CONFIG_PROPERTY_DECL(float, weight)
		SSFNET_CONFIG_PROPERTY_DECL(float, qmin)
		SSFNET_CONFIG_PROPERTY_DECL(float, qmax)
		SSFNET_CONFIG_PROPERTY_DECL(float, pmax)
		SSFNET_CONFIG_PROPERTY_DECL(bool, wait_opt)
		SSFNET_CONFIG_PROPERTY_DECL(float, mean_pktsiz)
	)
	SSFNET_STATEMAP_DECL(
		float weight;
		float qmin;
		float qmax;
		float pmax;
		float mean_pktsiz;
		float fluid_weight;
		float prop_delay;
		float aggr_arrival;
		float pkt_arrival;
		float queue;
		float avgque;
		float loss;
		float shadow_queue;
		float shadow_avgque;
		float shadow_loss;
		bool crossing;
		float interdrop;
		VirtualTime last_update_time;
		VirtualTime last_send_time;
	)
	SSFNET_ENTITY_SETUP( )
;
public:
	  /** The constructor. */
	  FluidQueue();

	  /** The destructor. */
	  ~FluidQueue();

	  /** Initialize the nic queue. */
	  virtual void init();

	  /** Return the type of this queue. */
	  virtual int type() { return FLUID_QUEUE_TYPE; }

	  /** Add a fluid hop at this queue. */
	  void addFluidHop(TrafficManager* traffic_mgr, FLUID_CLASSID cid, FluidHop* hop);

	  /** Remove a fluid hop at this queue. */
	  void deleteFluidHop(TrafficManager* traffic_mgr, FLUID_CLASSID cid);

	  /** Enqueue the message at the tail of the buffer. If the NIC hasn't
	      yet used up all of its bandwidth, the msg is sent after the
	      appropriate buffer delay. If the queue is full or the message is
	      marked to be dropped according to RED policy, the message will
	      be reclaimed. */
	  bool enqueue(Packet* pkt, float drop_prob, bool cannot_drop=false);

	  /** Update the fluid queue state per Runge-Kutta step. */
	  void queueEvolution(float step_size);

	  /** Helper function that updates shadow variables (instant queue size (bits) and
	   *  avg queue size (bits) between RK step (from the last update time until the
	   *  current update time)); called by both calibrate() and queue_evolution() methods. */
	  void updateShadowVariables(VirtualTime now);

	  float getPropDelay();

	  float getPktArrival();

	  float getQueue();

	  float getLoss();

	  void setAggrArrival(float aggr_arrival);

	  void setPktArrival(float pkt_arrival);

	#ifdef SSFNET_FLUID_VARYING
	  int timer_handle;
	  float timer_stepsize;
	  void updateQueue(TrafficManager* traffic_mgr);
	#endif

	  FluidMap& getFluidMap();

private:

	friend class TrafficManager;
	friend class FluidClass;
	friend class FluidHop;

	/** Mapping from fluid class id to tcp flow per hop data. */
	FluidMap fluid_map;

protected:
	/**
	 * Called by the enqueue method. The queueing delay is calculated
	 * relative to the last recorded transmission. If we have moved
	 * forward in time, we need to recalibrate the accumulated delay so
	 * that it remains relative to the current time. We need to
	 * recalibrate before every transmission. If the jitter range is
	 * defined in the interface, this method also samples and returns a
	 * jitter for sending the message.
	 */
	VirtualTime calibrate(float length);

	/** Called by the queue monitor to record the state of the queue. */
	//virtual void quemonCallback();

};

}; // namespace ssfnet
}; // namespace prime

#endif /*__FLUID_QUEUE_H__*/
