/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file protocol_session.h
 * \brief Header file for the ProtocolSession class.
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

#ifndef __PROTOCOL_SESSION_H__
#define __PROTOCOL_SESSION_H__

#include "rng.h"
#include "os/virtual_time.h"
#include "os/protocol_message.h"
#include "os/protocol_graph.h"
#include "os/config_type.h"
#include "os/config_entity.h"

namespace prime {
namespace ssfnet {

class Host;
class Interface;
class StartTrafficEvent;

/**
 * \brief A protocol layer on the protocol stack.
 *
 * The ProtocolSession class represents a protocol layer on the
 * ISO/OSI protocol stack. It's the base class for all protocol
 * implementations. The class specifies default mechanisms for how a
 * protocol session should behave. The data exchanged between protocol
 * layers are encapsulated in a ProtocolMessage object. The data path
 * is specified by two methods -- push() and pop() -- for receiving
 * data from the protocol session above and below. Subclasses most
 * likely would need to override these methods with specific protocol
 * behavior. Each derived protocol session class must register using
 * the SSFNET_REGISTER_PROTOCOL macro.
 */
class ProtocolSession : public ConfigurableEntity<ProtocolSession,BaseEntity> {
  friend class ProtocolGraph;
  friend class Host;
  friend class Interface;

 public:

  /**
   * This method is called to initialize the protocol session once the
   * protocol session has been created and configured. The default
   * behavior is doing nothing. The derived class may override this
   * method for protocol specific initialization. Note that the order
   * of initialization of the protocol sessions in a protocol graph is
   * unspecified. Don't rely on the ordering to help sort out the
   * relationships between protocols.
   */
  virtual void init() {}

  /**
   * This method is used to wrap up the protocol session. It is called
   * when the simulation finishes. It is designed to give user a
   * chance to report simulation statistics. The default behavior is
   * doing nothing. Subclass may override this method for protocol
   * specific dealings. Note that there is no specific order in which
   * wrapup is called for protocol sessions in a protocol graph.
   */
  virtual void wrapup() {}

  /**
   * This method is called when a message is pushed down to this
   * protocol session from the protocol session above. The protocol
   * session above calling this method will have a reference to itself
   * in the second argument. Extra information can be provided as the
   * third argument. The protocol message is given as the first
   * argument. Once this method is called, the message logically
   * belongs to this session until it's passed along, for example,
   * further down the protocol stack. That is, if the message is to be
   * dropped for any reason, this session must be responsible to
   * reclaim the message. Message exchange is implemented as method
   * calls for efficiency reasons. It consumes no simulation time
   * (even without context switch) unless the user coded it
   * explicitly. The extinfo parameter provides a means to pass any
   * auxiliary information that doesn't belong in the protocol message
   * with the call. This method returns zero if successful.
   */
  virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0) { return -1; }

  /**
   * This method is called when a message is popped up to this
   * protocol session from the protocol session below. The protocol
   * session below calling this method will have a reference to itself
   * in the second argument. Extra information can be provided as the
   * third argument. The protocol message is given as the first
   * argument.  The ownership of the the protocol message is
   * transfered when pop method is called, which means that the callee
   * (this protocol session) is responsible for the protocol message
   * and the packet. This method returns zero if successful.
   */
  virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0) { return -1; }

  /**
   * This is called by the owning host to start traffic of a particular type.
   * Protocol Sessions which expect to get start traffic events
   * must override this function.
   */
  virtual void startTraffic(StartTrafficEvent* evt);
  
    /**
   * The method returns the protocol graph in which this protocol
   * session resides.
   */
  ProtocolGraph* inGraph();

  /** Return a pointer to the host owning this protocol session. */
  virtual Host* inHost();

  /**
   * Each protocol session should have its unique protocol number. The
   * number will be used to retrieve the protocol session object using
   * the protocol graph's sessionForNumber() method. The derived class
   * should override this method and return the correct protocol
   * number. The protocol number should be the same as the type of the
   * corresponding protocol message, which the protocol uses to
   * interact with the protocol sessions above and below it. A
   * protocol session may use the protocol message's type to
   * demultiplex the protocol messages at the destination and send the
   * message to the correct protocol session.
   */
  int getProtocolNumber() { return convert_typeid_to_protonum(getTypeId()); }

  /**
   * Each protocol session should have a unique protocol name. The
   * name string will be used to retrieve the protocol session object
   * using the protocol graph's sessionForName() method. The derived
   * class should override this method and return the correct protocol
   * name.
   */
  const SSFNET_STRING& getProtocolName() { return getTypeName(); }

  /**
   * The method returns a pointer to the random number generator.
   */
  prime::rng::Random* getRandom();

  /**
   * The method returns the current simulation time. To be safe, one
   * should only call this method after initialization and before the
   * wrapup phase.
   */
  VirtualTime getNow();

  /**
   * Set a timer for a given duration. The method returns a handle
   * that one could use to either 1) later cancel the timer, or 2)
   * when timeout happens and the callback function (timerCallback) is
   * invoked, distinguish from other timeouts. User-defined data
   * associated with the timer can be passed along in the argument,
   * and will be provided to the callback method.
   */
  int setTimer(VirtualTime duration, void* extra = 0);

  /**
   * Same as the setTimer() method, except that we specify the exact
   * time (rather than the duration) that the timer will go off.
   */
  int setTimerUntil(VirtualTime time, void* extra = 0);

  /**
   * Cancel a timer with given handle. Return true if successful; the
   * extra data originally provided by user is also returned.
   */
  bool cancelTimer(int handle, void** extra = 0);

  /**
   * When a timer goes off, this method will be invoked. The handle of
   * the timer is provided as an argument. The user should override
   * this method in the derived class.
   */
  virtual void timerCallback(int handle, void* extra) {}

  /** The constructor. */
  ProtocolSession();

 protected:
  /** The destructor is protected; only ProtocolGraph reclaims it. */
  virtual ~ProtocolSession();

  /** Point to a random number generator. */
  prime::rng::Random* rng;

public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
};

} // namespace ssfnet
} // namespace prime

#endif /*__PROTOCOL_SESSION_H__*/
