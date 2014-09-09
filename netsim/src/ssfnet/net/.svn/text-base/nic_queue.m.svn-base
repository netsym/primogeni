/**
 * \file nic_queue.h
 * \brief Header file for the NicQueue class.
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

#ifndef __NIC_QUEUE_H__
#define __NIC_QUEUE_H__

#include "os/ssfnet.h"
#include "os/config_entity.h"
#include "os/packet.h"

namespace prime {
namespace ssfnet {

class Host;
class Interface;

/**
 * \brief A base class for implementation of queues in an NIC.
 *
 * All derived queues must implement the enqueue member function,
 * which takes charge of buffering packets or flow events.
 */
class NicQueue : public ConfigurableEntity<NicQueue, BaseEntity> {
 public:

  /** The constructor. */
  NicQueue() {}

  /** The destructor.  */
  virtual ~NicQueue() {}

  /** Initialize the queue. */
  virtual void init() {}

  /** The wrapup method is called when the simulation finishes. */
  virtual void wrapup() {}

  /** Return the type of this queue. */
  virtual int type();

  /**
   * Insert a packet into the queue before it can be sent. This method
   * must be overridden in the derived class.
   */
  virtual bool enqueue(Packet* pkt, float drop_prob, bool cannot_drop=false);
    
  /** Return the network interface managing this queue. */
  Interface* getInterface();

  /** Return the host that contains this nic queue. */
  Host* inHost();

  /** Get the transmission rate in bits per second. */
  float getBitRate();

  /** Get the transmission latency. */
  float getLatency();

  /** Get the jitter range (from 0 to 1). */
  float getJitterRange();

  /** Get the send-side buffer size in MB. */
  int getBufferSize();

  /** Get the send-side queue size in bits */
  virtual int getQueueSize();

};

} // namespace ssfnet
} // namespace prime

#endif /*__NIC_QUEUE_H__*/
