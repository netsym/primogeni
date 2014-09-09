/**
 * \file droptail_queue.h
 * \brief Header file for the DropTailQueue class.
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

#ifndef __DROPTAIL_QUEUE_H__
#define __DROPTAIL_QUEUE_H__

#include "os/virtual_time.h"
#include "net/nic_queue.h"

namespace prime {
namespace ssfnet {

#define DROPTAIL_QUEUE_TYPE 1
#define DROPTAIL_QUEUE_NAME "droptail"

/**
 * \brief A FIFO queue used inside a network interface.
 *
 * The drop-tail queue maintains a buffer that holds up to a fixed
 * number of bits. Bits go out one by one at the NIC's bit-rate. If
 * attempting to enqueue a message whose size is larger than the
 * currently available free buffer space, the packet is dropped.  This
 * is accomplished by the following scheme. Each interface tracks its
 * "queuing delay," which accumulates as packets are written
 * out. Each packet contributes its transmission time (computed as the
 * size of the packet in bits divided by the bit rate of the NIC) to
 * the queuing delay of future packets.  This guarantees that the
 * interface will never exceed the local NIC's set bit rate .
 */
class DropTailQueue: public ConfigurableEntity<DropTailQueue, NicQueue> {
	state_configuration {
		/** The time of the last transmission. */
		VirtualTime last_xmit_time;

		/** Queuing delay since the last transmission time. */
		VirtualTime queue_delay;

		/** Maximum queuing delay is bounded by the queue length. */
		VirtualTime max_queue_delay;
	}

public:
	/** The constructor. */
	DropTailQueue() {
	}

	/** The destructor. */
	virtual ~DropTailQueue() {
	}

	/** Initialize the queue. */
	virtual void init();

	/** Return the type of this queue. */
	virtual int type();

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/**
	 * Enqueue the packet at the tail of the queue. If the queue still
	 * has space for this packet, the packet will be sent after the
	 * appropriate queuing delay, in which case this method returns
	 * TRUE. Otherwise, if the queue is full, the packet will be dropped
	 * and the method returns FALSE.
	 */
	virtual bool enqueue(Packet* pkt, float drop_prob, bool cannot_drop=false);
	
	/** Return the instantaneous queue length (in bytes). */
	int getInstantLength();

	/** Get the send-side queue size in bits */
	virtual int getQueueSize();

protected:
	/**
	 * Called by the enqueue method. The queuing delay is calculated
	 * relative to the last recorded transmission. If we have moved
	 * forward in time, we need to re-calibrate the accumulated delay so
	 * that it remains relative to the current time. We need to
	 * re-calibrate before every transmission. If the jitter range is
	 * defined in the interface, this method also samples and returns a
	 * jitter for sending the message.
	 */
	VirtualTime calibrate(int pktlen);
};

} // namespace ssfnet
} // namespace prime

#endif /*__DROPTAIL_QUEUE_H__*/
