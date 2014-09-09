/**
 * \file host.h
 * \brief Header file for the Host class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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

#ifndef __HOST_H__
#define __HOST_H__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/virtual_time.h"
#include "os/protocol_graph.h"
#include "net/interface.h"
#include "os/traffic_mgr.h"
#include "os/trie.h"

namespace prime {
namespace ssfnet {

class TrafficPortal;

class TrafficPortalInterface : public TrieData {
public:
	TrafficPortalInterface(IPPrefix prefix_, Interface* iface_):
		prefix(prefix_),iface(iface_){
	}
	TrafficPortalInterface(const TrafficPortalInterface& o):
		prefix(o.prefix), iface(o.iface){
	}
	virtual ~TrafficPortalInterface() {}
	IPPrefix prefix;
	Interface* iface;
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TrafficPortalInterface& x);
};

/**
 * \brief PortalIfaceTable
 *
 * This class maps IP addresses (in the form a.b.c.d/p) into to interfaces which are
 * traffic portals
 *
 */
class PortalIfaceTable : protected Trie {
public:
	/** The constructor. */
	PortalIfaceTable() {}

	/** The destructor. */
	virtual ~PortalIfaceTable() {}

	void addPortal(IPPrefix& network, Interface* iface, bool replace = true);

	TrafficPortalInterface* getPortal(uint32 ipaddr);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const PortalIfaceTable& x);

protected:
	/** The recursive helper to the dump method. */
	static void dump_helper(TrieNode* root, uint32 sofar,
			int n, PRIME_OSTREAM &os);
};


class Community;
class Net;
class HostTimerQueueData;
class HostTimerQueue;

/**
 * \brief A host or router.
 *
 * The class provides all necessary support for protocol sessions to
 * run inside a virtual machine, including time service and message
 * delivery service. Each host also maintains a unique random seed.
 */
class Host: public ConfigurableEntity<Host, ProtocolGraph> {
	friend class HostTimerQueue;
	friend class Community;
	friend class TrafficTimer;
public:
	typedef SSFNET_MAP(uint32, Interface*) IP2IFACE_MAP;
	typedef SSFNET_MAP(UID_t, Interface*) ID2IFACE_MAP;
	typedef SSFNET_VECTOR(Host*) Vector;
	state_configuration {
		shared configurable int host_seed {
			type=INT;
			default_value = "0" ;
			doc_string = "host's seed";
		};

		shared configurable float time_skew {
			type=FLOAT;
			default_value = 1.0;
			doc_string = "host's time skew (ratio of host time over absolute time)";
		};

		shared configurable VirtualTime time_offset {
			type= OBJECT; // could be set as default if not primitive type
			default_value= "0.0";
			doc_string= "host's time offset (relative to absolute time zero)";
		};

		configurable float traffic_intensity {
			type= FLOAT;
			default_value="0";
			doc_string="max  [ for each nic: (bits_out/bit_rate) ]";
			visualized=true;
			statistic=true;
		};

		/** A map from IP address to interface. */
		IP2IFACE_MAP ip2iface;

		/** Caching the default IP address of this host. */
		IPAddress default_ip = IPAddress::IPADDR_INVALID;

		/** Timer queue for managing all timers. */
		HostTimerQueue* tmr_queue = 0;

		/** Mapping from timer handle to timer data. */
		SSFNET_INT2PTR_MAP* tmr_map = 0;

		/** The timer handle that has been used last time. */
		int tmr_handle = 0;

		/**
		 * The random number generator, per host. Each host is expected to
		 * maintain a unique random stream, seeded initially as a function
		 * of the host's default ip address (or UID).
		 */
		prime::rng::Random* rng = 0;

		/**
		 * If it's not zero, this is the random seed used by protocol
		 * sessions to initialize their random streams; if it's zero, all
		 * protocol sessions will shared the same random stream (rng).
		 */
		int rng_seed = 0;

		PortalIfaceTable* portals = 0;
		
		child_type<Interface> nics {
			min_count = 0;
			max_count = 0; // infinity
			is_aliased = false; // by default it is false
			doc_string = "network interfaces on this host";
		};
	}
private:
	/** Caching a pointer to the community that owns this host (a
	 community is a self-aligned SSF entity). */
	Community* community;
public:
	/** The constructor. */
	Host();

	/** The destructor. */
	virtual ~Host();

	/**
	 * The init method is used to initialize the host once it has been
	 * created and configured by model builder. The init method will
	 * initialize the interfaces and protocol sessions.
	 */
	virtual void init();

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * used to collect statistical information of the simulation run.
	 * The wrapup method will call the wrapup method of the interfaces
	 * and protocol sessions.
	 */
	virtual void wrapup();

	/** Return the network containing this host. */
	Net* inNet();

	/** Return the community this host belongs to. */
	Community* getCommunity();

	/**
	 * Return a list (an enumeration) of network interfaces installed on
	 * this host.
	 */
	ChildIterator<Interface*> getInterfaces();

	/**
	 * Return the network interface on this host that has the given IP
	 * address. The method returns NULL if the interface doesn't exist.
	 */
	Interface* getInterfaceByIP(IPAddress ip);

	/** Return the IP address of the first (connected) interface. */
	IPAddress getDefaultIP();

	/** Return the MAC address of the first (connected) interface. */
	MACAddress getDefaultMAC();

	/**
	 * This method is called by the community, used to handle the start traffic event.
	 */
	void handleStartTrafficEvent(StartTrafficEvent* evt);

	/**
	 * This method is called by a protocol session to send the finished evt to the correspongding
	 * community to indicate a particular traffic is finished.
	 */
	void sentFinishedTrafficEvent(FinishedTrafficEvent* evt);

	virtual void exportState(StateLogger* state_logger, double sampleInterval);
	virtual void exportVizState(StateLogger* state_logger, double sampleInterval);

	//@name Timer Services
	//@{

public:
	/** Return the current simulation time as perceived by this host;
	 each host may offset and skew the clock. */
	VirtualTime getNow();

	/** Convert from absolute time to host's time. */
	VirtualTime absoluteTimeToHostTime(VirtualTime x);

	/** Convert from host's time to absolute time. */
	VirtualTime hostTimeToAbsoluteTime(VirtualTime x);

	/**
	 * Set a timer for a given duration (in host's time). The method
	 * returns a handle that one could use to either 1) later cancel the
	 * timer, or 2) when timeout happens and the callback function
	 * (timerCallback) is invoked, distinguish from other
	 * timeouts. User-defined data associated with the timer can be
	 * passed along in the argument, and will be provided to the
	 * callback method.
	 */
	int setTimer(ProtocolSession* sess, VirtualTime duration, void* extra);

	/**
	 * Same as the setTimer() method, except that we specify the exact
	 * time (rather than the duration) that the timer will go off. Note
	 * that this method uses host's time.
	 */
	int setTimerUntil(ProtocolSession* sess, VirtualTime time, void* extra);

	/**
	 * Cancel a timer with given handle. Return true if successful; the
	 * extra data originally provided by user is also returned.
	 */
	bool cancelTimer(ProtocolSession* sess, int handle, void** extra);

	/** Used by timer queue to handle timeout. */
	void tmr_callback(HostTimerQueueData* tqd);

	/** Used to store the start traffic events **/
	StartTrafficEvent::PriorityQueue start_traffics;

	/** child portals should register themselves **/
	void registerTrafficPortal(TrafficPortal* portal);

	/** if this host has portals this will return the iface which handles the desired network **/
	Interface* getTrafficPortal(uint32 ip);

private:
	void scheduleTrafficStart(VirtualTime time);
	bool startedStatCollected;

	//@}

	//@name Random Number Generator
	//@{

public:
	/**
	 * Return the random number generator of this host. The random
	 * number generator for this host is defined in the init method;
	 * that is, one should not call this method before init.
	 */
	prime::rng::Random* getRandom();

	/**
	 * Return the random seed of this host. If it's zero, the protocol
	 * sessions on this host will share the same random stream.
	 * Otherwise, protocol sessions will use this host seed to
	 * initialize their own random streams.
	 */
	int getHostSeed();
	//@}

	/**
	 * Create and send an ICMP time exceeded message to the source of the msg
	 */
	void sendTimeExceededMsg(IPv4Message* ipmsg, IPAddress ipaddr);

protected:
	/** The model builder will set the community */
	void setCommunity(Community* _community);

	/**
	 * This method is called by the traffic timer to start traffic.
	 */
    void startTraffic();
};

class Router: public ConfigurableEntity<Router, Host> {
public:
	/** The constructor. */
	Router();

	/** The destructor. */
	virtual ~Router();
};


} // namespace ssfnet
} // namespace prime

#endif /*__HOST_H__*/
