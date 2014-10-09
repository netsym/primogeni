/**
 * \file ipv4_session.h
 * \brief Header file for the IPv4Session class.
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

#ifndef __IPV4_SESSION_H__
#define __IPV4_SESSION_H__

#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "os/routing.h"
#include "net/interface.h"
#include "proto/ipv4/ipv4_message.h"

namespace prime {
namespace ssfnet {

/* Some details on the iptables implementation
   (from http://iptables-tutorial.frozentux.net/iptables-tutorial.html#TRAVERSINGOFTABLES)

Destination local host:

Step	Table	Chain		Comment
1				On the wire (e.g., Internet)
2				Comes in on the interface (e.g., eth0)
3	raw	PREROUTING	This chain is used to handle packets before the connection tracking takes place. It can be used to set a specific connection not to be handled by the connection tracking code for example.
4				This is when the connection tracking code takes place as discussed in the The state machine chapter.
5	mangle	PREROUTING	This chain is normally used for mangling packets, i.e., changing TOS and so on.
6	nat	PREROUTING	This chain is used for DNAT mainly. Avoid filtering in this chain since it will be bypassed in certain cases.
7				Routing decision, i.e., is the packet destined for our local host or to be forwarded and where.
8	mangle	INPUT		At this point, the mangle INPUT chain is hit. We use this chain to mangle packets, after they have been routed, but before they are actually sent to the process on the machine.
9	filter	INPUT		This is where we do filtering for all incoming traffic destined for our local host. Note that all incoming packets destined for this host pass through this chain, no matter what interface or in which direction they came from.
10				Local process or application (i.e., server or client program).

Source local host:

Step	Table	Chain		Comment
1				Local process/application (i.e., server/client program)
2				Routing decision. What source address to use, what outgoing interface to use, and other necessary information that needs to be gathered.
3	raw	OUTPUT		This is where you do work before the connection tracking has taken place for locally generated packets. You can mark connections so that they will not be tracked for example.
4				This is where the connection tracking takes place for locally generated packets, for example state changes et cetera. This is discussed in more detail in the The state machine chapter.
5	mangle	OUTPUT		This is where we mangle packets, it is suggested that you do not filter in this chain since it can have side effects.
6	nat	OUTPUT		This chain can be used to NAT outgoing packets from the firewall itself.
7				Routing decision, since the previous mangle and nat changes may have changed how the packet should be routed.
8	filter	OUTPUT		This is where we filter packets going out from the local host.
9	mangle	POSTROUTING	The POSTROUTING chain in the mangle table is mainly used when we want to do mangling on packets before they leave our host, but after the actual routing decisions. This chain will be hit by both packets just traversing the firewall, as well as packets created by the firewall itself.
10	nat	POSTROUTING	This is where we do SNAT as described earlier. It is suggested that you don't do filtering here since it can have side effects, and certain packets might slip through even though you set a default policy of DROP.
11				Goes out on some interface (e.g., eth0)
12				On the wire (e.g., Internet)

Forwarded packets:

Step	Table	Chain		Comment
1				On the wire (i.e., Internet)
2				Comes in on the interface (i.e., eth0)
3	raw	PREROUTING	Here you can set a connection to not be handled by the connection tracking system.
4				This is where the non-locally generated connection tracking takes place, and is also discussed more in detail in the The state machine chapter.
5	mangle	PREROUTING	This chain is normally used for mangling packets, i.e., changing TOS and so on.
6	nat	PREROUTING	This chain is used for DNAT mainly. SNAT is done further on. Avoid filtering in this chain since it will be bypassed in certain cases.
7				Routing decision, i.e., is the packet destined for our local host or to be forwarded and where.
8	mangle	FORWARD		The packet is then sent on to the FORWARD chain of the mangle table. This can be used for very specific needs, where we want to mangle the packets after the initial routing decision, but before the last routing decision made just before the packet is sent out.
9	filter	FORWARD		The packet gets routed onto the FORWARD chain. Only forwarded packets go through here, and here we do all the filtering. Note that all traffic that's forwarded goes through here (not only in one direction), so you need to think about it when writing your rule-set.
10	mangle	POSTROUTING	This chain is used for specific types of packet mangling that we wish to take place after all kinds of routing decisions have been done, but still on this machine.
11	nat	POSTROUTING	This chain should first and foremost be used for SNAT. Avoid doing filtering here, since certain packets might pass this chain without ever hitting it. This is also where Masquerading is done.
12				Goes out on the outgoing interface (i.e., eth1).
13				Out on the wire again (i.e., LAN).
*/

// all possible return value from iptables callback
enum IPTablesCallbackReturn {
  IPTABLES_CALLBACK_NOCHANGE = 0,
  IPTABLES_CALLBACK_MODIFIED = 1,
  IPTABLES_CALLBACK_REMOVED = 2
};

// iptables callback function prototype
typedef int (*IPTablesCallback)(ProtocolSession*, IPv4Message*);

/**
 * \brief A private class used by ipv4 session for maintaining iptables.
 */
struct IPTablesEntry {
  int handle; // unique handle (so that one can remove it from the iptables chain
  ProtocolSession* sess; // the protocol session that registers the callback
  IPTablesCallback* callback; // the call back function
  IPTablesEntry* next; // next callback entry in the iptables chain
};

class IPOptionToAbove {
 public:
	IPAddress src_ip;
	IPAddress dst_ip;
};

class IPv4Session;
class ICMPv4Session;
class Community;
class NameResolutionCallBackWrapper;
class IPv4NameResolutionCallBack;

/**
 * \brief Used by ipv4 session to wrap around an ipv4 message
 */
class IPv4MessageWrapper {
 public:
  // the constructor using the ip message pushed from above
  IPv4MessageWrapper(IPv4Message* msg, ProtocolSession* sess);

  // the constructor using the ip message popped from below
  IPv4MessageWrapper(IPv4Message* msg, Interface* nic);

  // the destructor
  ~IPv4MessageWrapper();

  // return true if the ip message is sent out by this host
  bool isPushed();

  // return true if the outgoing route has been determined
  bool isOutRouteDetermined();

  // set the outgoing route
  void setOutRoute(Interface* nic);

  // reset the outgoing route
  void resetOutRoute();

  // return the ip message
  IPv4Message* getMsg();

  // return the incoming network interface
  Interface* getInNIC();

  // return the outgoing network interface
  Interface* getOutNIC();

 private:
  IPv4Message* ipmsg; // points to the ip message being processed
  ProtocolSession* hisess; // the upper layer protocol sending the ip message
  Interface* innic; // the network interface originally receiving the ip message
  Interface* outnic; // if null, it's either local or routing is needed; otherwise, outgoing route has been determined
};

/**
 * \brief Internet Protocol (IP) version 4
 */
class IPv4Session: public ConfigurableEntity<IPv4Session,ProtocolSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_IPV4)> {
	friend class Interface;
	friend class IPv4NameResolutionCallBack;
public:

  /** The constructor. */
  IPv4Session();

  /** The destructor. */
  virtual ~IPv4Session();

  /**
   * Initialize the IP session. This function will be called after the
   * entire network has been built and configured, and all the links
   * have established connections, but before the simulation clock
   * starts ticking.
   */
  virtual void init();

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

  /** Used to identify all different chains for iptables. */
  enum IPTablesChainID {
    IPTABLES_PREROUTING_RAW	= 0,
    IPTABLES_PREROUTING_MANGLE	= 1,
    IPTABLES_PREROUTING_NAT	= 2,
    IPTABLES_INPUT_MANGLE	= 3,
    IPTABLES_INPUT_FILTER	= 4,
    IPTABLES_FORWARD_MANGLE	= 5,
    IPTABLES_FORWARD_FILTER	= 6,
    IPTABLES_OUTPUT_RAW		= 7,
    IPTABLES_OUTPUT_MANGLE	= 8,
    IPTABLES_OUTPUT_NAT		= 9,
    IPTABLES_OUTPUT_FILTER	= 10,
    IPTABLES_POSTROUTING_MANGLE = 12,
    IPTABLES_POSTROUTING_FILTER = 13,
    IPTABLES_TOTAL_CHAINS	= 14
  };

  /** Return a handle of a newly created iptables entry (zero means error). */
  int addIPTablesCallback(int chain_id, IPTablesCallback* callback, ProtocolSession* sess);

  /** Remove the iptables entry using the handle (return zero means error). */
  int removeIPTablesCallback(int chain_id, int handle);

  /**
   * Return true if the address matches with one of the interfaces, or
   * it's a loopback address.
   */
  bool verifyLocalIP(IPAddress ipaddr);

  /**
   * Return true if the address matches with one of the interfaces, or
   * it's a broadcast or multicast address.
   */
  bool verifyTargetIP(IPAddress ipaddr, Interface* nic);

  /** Return the icmp session (create one if missing). */
  ICMPv4Session* getICMPSession();

  /** Return the current forwarding engine */
  ForwardingEngine* getForwardingEngine();

  /** Set the current forwarding engine */
  void setForwardingEngine(ForwardingEngine* fwdengine);

  /** Return the NameServer */
  Community* getCommunity();

 private:
  ICMPv4Session* icmp; // fast link to the icmp session
  Community* community; // fast link to the community
  ForwardingEngine* fr_engine; // the mechanism for forwarding and routing
  IPTablesEntry** iptables_chains; // all callback chains for iptables
  int unique_iptables_handle; // increment by one for each new iptables entry

  // make the routing (forwarding) decision for the given ip packet
  //callback_uid will != 0 if this being called back by the async name service....
  IPv4MessageWrapper* conduct_routing(bool popping, ProtocolSession* other_session, IPv4MessageWrapper* msgwrap, UID_t callback_uid);

  // called by push with callback_uid set to zero
  // called by async callback with callback_uid != 0
  int push(IPv4MessageWrapper* msgwrap, ProtocolSession* hisess, UID_t callback_uid);

  // called by pop with callback_uid set to zero
  // called by async callback with callback_uid != 0
  int pop(IPv4MessageWrapper* msgwrap, ProtocolSession* losess, UID_t callback_uid);


  // depending on whether outgoing route has been determined or not,
  // go through iptables input or postrouting chains before actually
  // popping up or pushing down the ip packet
  int iptables_input_or_postrouting(IPv4MessageWrapper* msgwrap);

  // handle respective iptables processing
  IPv4MessageWrapper* iptables_prerouting(IPv4MessageWrapper* msgwrap);
  IPv4MessageWrapper* iptables_input(IPv4MessageWrapper* msgwrap);
  IPv4MessageWrapper* iptables_forward(IPv4MessageWrapper* msgwrap);
  IPv4MessageWrapper* iptables_output(IPv4MessageWrapper* msgwrap);
  IPv4MessageWrapper* iptables_postrouting(IPv4MessageWrapper* msgwrap);
  IPv4MessageWrapper* iptables_processing(int chain_id, IPv4MessageWrapper* msgwrap);

};



} // namespace ssfnet
} // namespace prime

#endif /*__IPV4_SESSION_H__*/
