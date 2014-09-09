/**
 * \file probe_session.h
 * \brief Header file for the ProbeSession class.
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

#ifndef __PROBE_SESSION_H__
#define __PROBE_SESSION_H__

#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "proto/application_session.h"

namespace prime {
namespace ssfnet {

class IPv4Session;
class ProbeHandling;

class ProbeSession: public ConfigurableEntity<ProbeSession,ProtocolSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_PROBE)> {
	typedef SSFNET_LIST(ProbeHandling*) ProbeHandlingList;
	typedef SSFNET_MAP(UID_t,ProbeHandlingList) ProbeHandlingMap;
 public:

  /** The constructor. */
  ProbeSession();

  /** The destructor. */
  virtual ~ProbeSession();

  /**
   * This method is called by the owning host, which should be the src of a fluid flow,
   * to create the probe packet and sent it out to the destination.
   */

  void startProbe(ProbeHandling* ph, UID_t dst);

  void startProbe(UID_t dst, IPAddress ipaddr);

  /**
   * This method is called by the owning host to start traffic of a particular type.
   * Protocol Sessions which expect to get start traffic events
   * must override this function.
   *
   * This method is used for the probe session to start traffic particularly.
   */
  virtual void startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac);

  /**
   * This method is called when an ip message is pushed down from a
   * protocol above at the local host.
   */
  virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0);

  /**
   * This method is called when an ip message is popped up by a local
   * network interface.
   */
  virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

  /** Return the ip session (create one if missing). */
  IPv4Session* getIPSession();



 private:
  /** Caching a pointer to the IP session. */
  IPv4Session* ip_sess;

  /** used to figure out who asked for the probe and deliver response **/
  ProbeHandlingMap probe_map;

};

} // namespace ssfnet
} // namespace prime

#endif /*__PROBE_SESSION_H__*/
