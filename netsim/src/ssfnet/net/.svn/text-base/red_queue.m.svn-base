/**
 * \file red_queue.h
 * \brief Header file for the RedQueue class.
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

#ifndef __RED_QUEUE_H__
#define __RED_QUEUE_H__

#include "os/virtual_time.h"
#include "net/nic_queue.h"

namespace prime {
namespace ssfnet {

#define RED_QUEUE_TYPE 2
#define RED_QUEUE_NAME "red"

/**
 * \brief A simple RED queue used inside a network interface.
 *
 * The RED queue maintains a buffer that holds up to a fixed number of
 * bits. Bits go out one by one at the NIC's bit-rate. If attempting
 * to enqueue a message whose size is larger than the currently
 * available free buffer space, the packet is dropped. If there's
 * available buffer space, RED policy is enforced in terms of treating
 * newly arrived packets. A packet could be dropped according to a
 * probability calculated from the following function:
 *   - P(x)=0, if 0 <= x < qmin;
 *   - P(x)=(x-qmin)/(qmax_qmin)*pmax, if qmin <= x < qmax;
 *   - P(x)=(x-qmax)/(qcap-qmax)*(1-pmax)+pmax, if qmax <= x < qcap,
 *   - P(x)=1, otherwise
 * where x is the average queue length. The average queue length is
 * calculated at each packet arrival using an exponentially weighed
 * moving average (EWMA) method. That is, avgque =
 * (1-w)*avgque+w*queue, where w is a configurable parameter.
 */
class RedQueue: public ConfigurableEntity<RedQueue, NicQueue> {
 public:

	state_configuration {

		shared configurable float weight {
			type=FLOAT;
			default_value = "0";
			doc_string = "weight used by the AQM policy to calculate average queue length for each packet arrival";
			visualized=true;
		};
		shared configurable float qmin {
			type=FLOAT;
			default_value = "0";
			doc_string = "the min threshold (in bytes) for calculating packet drop probability";
			visualized=true;
		};
		shared configurable float qmax {
			type=FLOAT;
			default_value = "0";
			doc_string = "the max threshold (in bytes) for calculating packet drop probability";
			visualized=true;
		};
		shared configurable float qcap {
			type=FLOAT;
			default_value = "0";
			doc_string = "qcap for calculating packet drop probability";
			visualized=true;
		};
		shared configurable float pmax {
			type=FLOAT;
			default_value = "0.2";
			doc_string = "pmax for calculating packet drop probability";
			visualized=true;
		};
		shared configurable bool wait_opt {
			type=BOOL;
			default_value = "true";
			doc_string = "an option to avoid marking/dropping two packets in a row";
			visualized=true;
		};
		shared configurable int mean_pktsiz {
			type=INT;
			default_value = "500";
			doc_string = "mean packet size in bytes";
			visualized=true;
		};
		  /** Weight used by the AQM policy to calculate average queue length
		      ON EACH PACKET ARRIVAL. */
		  float weight;

		  /* Parameters to calculate packet drop probability based on the
		     average queue length x(t):
		       P(x)=0, if 0 <= x < qmin;
		       P(x)=(x-qmin)/(qmax_qmin)*pmax, if qmin <= x < qmax;
		       P(x)=(x-qmax)/(bufsize-qmax)*(1-pmax)+pmax, if x > qmax.
		     Both qmin and qmax are in bits.
		   */
		  float qmin; ///< Parameter to calculate packet drop probability.
		  float qmax; ///< Parameter to calculate packet drop probability.
		  float qcap; ///< Parameter to calculate packet drop probability.
		  float pmax; ///< Parameter to calculate packet drop probability.

		  /** This option is to avoid dropping two packets in a row. */
		  //bool wait_opt;

		  /** Mean packet size in bits. */
		  float mean_pktsiz;

		  /** The instantaneous queue size (in bits). */
		  float queue;

		  /** The average queue size (in bits). */
		  float avgque;

		  /** The loss probability calcuated from avg queue length. */
		  float loss;

		  /** False if the average queue length is crossing the min threshold
		      for the first time. */
		  bool crossing;

		  /** The number of bits arrived between two consecutive packet drops
		      (when the qmin threshold is crossed). */
		  float interdrop;

		  /** The time of the last queue update. */
		  VirtualTime last_update_time;

		  /** The time when the queue will be emptied. */
		  VirtualTime vacate_time;
	};

  /** The constructor. */
  RedQueue();

  /** The destructor. */
  virtual ~RedQueue();

  /** Initialize the queue. */
  virtual void init();

  /** Return the type of this queue. */
  virtual int type();

  /**
   * XXX initStateMap()
   */
  virtual void initStateMap();

  /**
   * Enqueue the message at the tail of the buffer. If the NIC hasn't
   * yet used up all of its bandwidth, the msg is sent after the
   * appropriate buffer delay. If the queue is full or the message is
   * marked to be dropped according to RED policy, the message will
   * be dropped.
   */
  virtual bool enqueue(Packet* pkt, float drop_prob, bool cannot_drop=false);

  /** Return the instantaneous queue length (in bytes). */
  int getInstantLength() { return int(unshared.queue/8); }

  /** Return the average queue length (in bytes). */
  int getAverageLength() { return int(unshared.avgque/8); }

  /** Return the packet drop probability. */
  float getLossRate() { return unshared.loss; }

  /** Get the send-side queue size in bits */
  virtual int getQueueSize() { return int(unshared.queue); }

 protected:
  /**
   * Called by the enqueue method. The queuing delay is calculated
   * relative to the last recorded transmission. If we have moved
   * forward in time, we need to recalibrate the accumulated delay so
   * that it remains relative to the current time. We need to
   * recalibrate before every transmission. If the jitter range is
   * defined in the interface, this method also samples and returns a
   * jitter for sending the message.
   */
  VirtualTime calibrate(float length);
};

} // namespace ssfnet
} // namespace prime

#endif /*__RED_QUEUE_H__*/
