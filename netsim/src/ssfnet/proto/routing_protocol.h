/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file routing_protocol.h
 * \brief Header file for the RoutingProtocol class.
 * \author Nathanael Van Vorst
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

#ifndef __ROUTING_PROTOCOL_H__
#define __ROUTING_PROTOCOL_H__

#include "os/ssfnet_types.h"
#include "os/protocol_session.h"
#include "os/routing.h"

namespace prime {
namespace ssfnet {

/**
 * \brief A base class for all routing protocols
 */
class RoutingProtocol: public ConfigurableEntity<RoutingProtocol,ProtocolSession> {
 public:

  /** The constructor. */
  RoutingProtocol() {}

  /** The destructor. */
  virtual ~RoutingProtocol(){}

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
   * Return the fowarding engine for this protocol
   *
   * This should be overridden by the dervived routing protocol
   */
  virtual ForwardingEngine* getForwardingEngine(){return 0;}


public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
};

} // namespace ssfnet
} // namespace prime

#endif /*__ROUTING_PROTOCOL_H__*/
