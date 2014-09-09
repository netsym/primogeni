/**
 * \file icmpv4_session.h
 * \brief Header file for the ICMPv4Session class.
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

#ifndef __ICMPV4_SESSION_H__
#define __ICMPV4_SESSION_H__

#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "proto/application_session.h"
#include "proto/ipv4/icmpv4_message.h"
#include "proto/ipv4/icmp_traffic.h"

namespace prime {
namespace ssfnet {

class IPv4Session;
class ICMPTraffic;

/**
 * \brief Internet Control Message Protocol (ICMP) version 4.
 */
class ICMPv4Session: public ConfigurableEntity<ICMPv4Session,ApplicationSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_ICMPV4)> {
  typedef SSFNET_VECTOR(ProtocolSession*) PSESS_VECTOR;
  typedef SSFNET_PAIR(UID_t, ICMPTraffic*) TRAFFIC_ID_TYPE_PAIR;
  typedef SSFNET_MAP(uint32_t, TRAFFIC_ID_TYPE_PAIR) MSG_TRAFFIC_MAP;
  typedef SSFNET_MAP(uint16, VirtualTime) EchoMap;

 public:

  /** The constructor. */
  ICMPv4Session();

  /** The destructor. */
  virtual ~ICMPv4Session();

  /**
   * This method is called by the owning host to start traffic of a particular type.
   * Protocol Sessions which expect to get start traffic events
   * must override this function.
   *
   * This method is used for the icmpv4 session to start traffic particularly.
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

  /** Add a listener protocol session. */
  void addListener(ProtocolSession* sess);

  /** Send an ICMP message indicating destination is unreachable
      because of a missing protocol (above IP). */
  void sendDestUnreachProto(IPv4Message* ipmsg);

  /** Send an ICMP message indicating the host destination is
      unreachable (e.g., due to route failures). */
  void sendDestUnreachHost(IPv4Message* ipmsg);

  /**
   * Create and send an ICMP time exceeded message to the source of the msg
   */
  void sendTimeExceededMsg(IPAddress ifaceip, IPv4Message* ipmsg);

 private:
  /** Caching a pointer to the IP session. */
  IPv4Session *ip_sess;

  /** A list of ICMP listeners. */
  PSESS_VECTOR listener_list;

  /** A map of the icmp messange and the traffic type/id pair, used to keep track of the traffics*/
  MSG_TRAFFIC_MAP msg_traffic;

  EchoMap echo_requests;
  /** Sequence number for icmp message.*/
  uint16_t seq;
};

} // namespace ssfnet
} // namespace prime

#endif /*__ICMPV4_SESSION_H__*/
