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
	state_configuration {
		shared configurable SSFNET_STRING queue_type {
			type=STRING;
			default_value= "red";
			doc_string= "The type of the queue, must be red or droptail";
		};
		shared configurable bool is_red {
			type= BOOL;
			default_value= "true";
			doc_string= "Whether this queue is a RED queue, rather than a drop-tail";
		};
		/** This is the weight applied FOR EACH PACKET ARRIVAL. */
		shared configurable float weight {
			type= FLOAT;
			default_value= "0.0001";
			doc_string= "The parameter used by RED queue to estimate the average queue length, must be between 0 and 1";
		};
		shared configurable float qmin {
			type=FLOAT;
			default_value= "0";
			doc_string= "the min threshold (in bytes) for calculating packet drop probability.";
		};
		shared configurable float qmax {
			type=FLOAT;
			default_value= "0";
			doc_string= "the max threshold (in bytes) for calculating packet drop probability.";
		};
		/*shared configurable float qcap {
			type=FLOAT;
			default_value= "0";
			doc_string= "Parameter to calculate packet drop probability, must be positive.";
		};*/
		shared configurable float pmax {
			type=FLOAT;
			default_value= "0.2";
			doc_string= "Parameter to calculate packet drop probability, must be in the range (0,1].";
		};
		shared configurable bool wait_opt {
			type= BOOL;
			default_value= "true";
			doc_string= "This RED option is used to avoid marking/dropping two packets in a row";
		};
		shared configurable float mean_pktsiz {
			type= FLOAT;
			default_value= "500";
			doc_string= "Mean packet size in bytes used by RED to compute the average queue length, must be positive.";
		};
		/** Weight used by the AQM policy to calculate average queue length ON EACH PACKET ARRIVAL. */
		float weight;

		/* Parameters to calculate packet drop probability based on the average queue length x(t):
		  P(x)=0, if 0 <= x < qmin;
		  P(x)=(x-qmin)/(qmax_qmin)*pmax, if qmin <= x < qmax;
		  P(x)=(x-qmax)/(bufsize-qmax)*(1-pmax)+pmax, if x > qmax.
		  Both qmin and qmax are in bits.
		*/
		float qmin; ///< Parameter to calculate packet drop probability.
		float qmax; ///< Parameter to calculate packet drop probability.
		//float qcap; ///< Parameter to calculate packet drop probability.
		float pmax; ///< Parameter to calculate packet drop probability.

		/** This option is to avoid dropping two packets in a row. */
		//bool wait_opt;

		/** Mean packet size in bits. */
		float mean_pktsiz;

		/** Weight used by the AQM policy to calculate average queue
		 *  length. We assume it is an exponentially weighed moving average
		 *  based on samples taken every delta seconds.
		 */
		float fluid_weight=0;

		/** The propagation delay of this queue. */
		float prop_delay=0;

		/** The aggregate arrival of fluid flows into this queue. */
		float aggr_arrival=0;

		/** The total number of bits arrived as packet flows since the last Runge-Kutta step. */
		float pkt_arrival=0;

		/** The instantaneous queue size. */
		float queue=0;

		/** The average queue size. */
		float avgque=0;

		/** The packet drop probability. */
		float loss=0;

		/** The instantaneous queue size inherited from the corresponding variable maintained
		 * solely for packet arrivals between Runge-Kutta steps.
		 */
		float shadow_queue=0;

		/** The average queue size inherited from the corresponding variable maintained solely
		 *  for packet arrivals between Runge-Kutta steps.
		 */
		float shadow_avgque=0;

		/** The packet drop probability inherited from the corresponding variable maintained
		 * solely for packet arrivals between Runge-Kutta steps.
		 */
		float shadow_loss=0;

		/** For RED packet model only. False if the average queue length is crossing the min
		 * threshold for the first time.
		 */
		bool crossing=false;

		/** The number of bits arrived between two consecutive packet drops (when the qmin
		 * threshold is crossed). Used for RED packet model only.
		 */
		float interdrop=0;

		/** The time of the last queue length update. */
		VirtualTime last_update_time=0;

		/** The time of the last packet sent. */
		VirtualTime last_send_time=0;

	};
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
