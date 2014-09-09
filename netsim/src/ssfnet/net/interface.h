/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file interface.h
 * \brief Header file for the Interface class.
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

#ifndef __INTERFACE_H__
#define __INTERFACE_H__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/virtual_time.h"
#include "os/protocol_graph.h"
#include "os/packet.h"
#include "net/nic_queue.h"
#include "proto/ipv4/ipv4_message.h"
#include "os/timer_queue.h"

namespace prime {
namespace ssfnet {

class Host;
class Link;
class EmulationProtocol;
class IPv4Session;

namespace model_builder {
class PartitionWrapper;
}
/**
 * \brief A common network interface API, which is implemented by
 * either Interface of GhostInterface.
 */
class BaseInterface: public ConfigurableEntity<BaseInterface, BaseEntity> {
	friend class Link;
	friend class model_builder::PartitionWrapper;
public:
	typedef SSFNET_VECTOR(BaseInterface*) Vector;
	typedef SSFNET_LIST(BaseInterface*) List;
	typedef SSFNET_MAP(IPAddress,BaseInterface*) IpMap;
	typedef SSFNET_MAP(MACAddress,BaseInterface*) MacMap;
	
public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(IPAddress, ip_address)
		SSFNET_CONFIG_STATE_DECL(MACAddress, mac_address)
		Link * attached_link;
	)
	SSFNET_ENTITY_SETUP( )


public:
	/** The default constructor. */
	BaseInterface();

	/** The destructor. */
	~BaseInterface();

	/** Get the IP address of this network interface. */
	const IPAddress& getIP();

	/** Get the MAC address of this network interface. */
	const MACAddress& getMAC();

	/** Return true if the give MAC address is a broadcast address. */
	bool isBroadcastAddress(MACAddress mac);

	/** Return true if the give ip address is a broadcast address. */
	bool isIPBroadcastAddress(IPAddress ip);

	/** Return true if the give ip address is a multicast address. */
	bool isMulticastAddress(IPAddress ip);

	/** Return the link the interface is attached to. */
	Link* getLink();

	/** Return the link the interface is attached to. */
	void setLink(Link* l);

	/** Initialize this interface. */
	virtual void init();

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/** Wrapup this interface. */
	virtual void wrapup() {
	}

	virtual UID_t getUIDOfRealInterface() { return getUID(); }

	virtual EmulationProtocol* getEmulationProtocol() { return NULL;}
};

/**
 * \brief A proxy to a network interface in another community.
 */
class GhostInterface: public ConfigurableEntity<GhostInterface, BaseInterface> {
public:
	typedef SSFNET_LIST(GhostInterface*) GhostIfaceList;
	
public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(UID_t, real_uid)
		SSFNET_CONFIG_STATE_DECL(UID_t, community_id)
	)
	SSFNET_ENTITY_SETUP( )

	/** The constructor. */
	GhostInterface() {
	}

	/** The destructor. */
	virtual ~GhostInterface() {
	}

	/** Return the name of if of the remote community. */
	UID_t getRemoteCommunityId();

	/** Return the uid of the real interface. */
	virtual UID_t getUIDOfRealInterface();
};


class TxTimerQueueData: public TimerQueueData {
public:
	TxTimerQueueData(Packet* p, Interface* nic, VirtualTime time);
	virtual ~TxTimerQueueData();
	Packet* pkt; // the packet to be transmitted
	Interface* iface; // through which the packet is to be transmitted
};

class TxTimerQueue: public TimerQueue {
public:
	TxTimerQueue(Community* comm);
	virtual ~TxTimerQueue();
	virtual void callback(TimerQueueData *timerData);
};

/**
 * \brief A network interface.
 *
 * This class represents a network interface card (NIC) inside a host
 * or a router. An interface is also a protocol graph consisting of
 * protocol sessions (say, MAC and PHY). The simplest configuration
 * does not have protocols defined.
 */
class Interface: public ConfigurableEntity<Interface, BaseInterface, 0> {
	friend class TxTimerQueue;
	friend class EmulationProtocol;
	friend class TrafficPortal;

	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(float, bit_rate)
		SSFNET_CONFIG_PROPERTY_DECL(float, latency)
		SSFNET_CONFIG_PROPERTY_DECL(float, jitter_range)
		SSFNET_CONFIG_PROPERTY_DECL(int, buffer_size)
		SSFNET_CONFIG_PROPERTY_DECL(int, mtu)
		SSFNET_CONFIG_PROPERTY_DECL(SSFNET_STRING, queue_type)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(NicQueue, nic_queue)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(EmulationProtocol, emu_proto)
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(float, drop_probability)
		SSFNET_CONFIG_STATE_DECL(bool, is_on)
		SSFNET_CONFIG_STATE_DECL(int, num_in_packets)
		SSFNET_CONFIG_STATE_DECL(int, num_in_bytes)
		SSFNET_CONFIG_STATE_DECL(float, packets_in_per_sec)
		SSFNET_CONFIG_STATE_DECL(float, bytes_in_per_sec)
		SSFNET_CONFIG_STATE_DECL(int, num_in_ucast_packets)
		SSFNET_CONFIG_STATE_DECL(int, num_in_ucast_bytes)
		SSFNET_CONFIG_STATE_DECL(int, num_out_packets)
		SSFNET_CONFIG_STATE_DECL(int, num_out_bytes)
		SSFNET_CONFIG_STATE_DECL(int, num_out_ucast_packets)
		SSFNET_CONFIG_STATE_DECL(int, num_out_ucast_bytes)
		SSFNET_CONFIG_STATE_DECL(float, packets_out_per_sec)
		SSFNET_CONFIG_STATE_DECL(float, bytes_out_per_sec)
		SSFNET_CONFIG_STATE_DECL(int, queue_size)
		EmulationProtocol * emu_protocol;
		NicQueue * tx_queue;
		TxTimerQueue * tx_timer_queue;
		IPv4Session * ip_sess;
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Interface,NicQueue,nic_queue)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Interface,EmulationProtocol,emu_proto)
	)
	SSFNET_CONFIG_CHILDREN_DECL(NicQueue, nic_queue)
	SSFNET_CONFIG_CHILDREN_DECL(EmulationProtocol, emu_proto)
	SSFNET_ENTITY_SETUP(&(unshared.nic_queue), &(unshared.emu_proto) )


public:
	typedef SSFNET_PAIR(UID_t,int) SphereComPair;
	typedef SSFNET_LIST(SphereComPair) SphereComList;
	/** The constructor. */
	Interface();

	/** The destructor. */
	virtual ~Interface();

	/** Initialize this interface. */
	virtual void init();

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/** Wrapup this interface. */
	virtual void wrapup();

	/** Return the host that contains this interface. */
	Host* inHost();

	/** Get the effective transmission rate in bits per second; it's the
	 minimum of the bit rate of the interface and the bandwidth of
	 the attached link. */
	float getBitRate();

	/** Get the transmission latency. */
	float getLatency();

	/** Get the jitter range (from 0 to 1). */
	float getJitterRange();

	/** Get the drop probability (from 0 to 1). */
	float getDropProbability();

	/** Get the send-side buffer size in bytes. */
	int getBufferSize();

	/** Get # bytes sent */
	int getBytesSent();

	/** Get # bytes sent */
	int getBytesReceived();
	
	/** Get the maximum transmission unit in bytes. */
	int getMTU();

	/** Get the send queue maintained by this network interface. */
	NicQueue* getNicQueue();

	/** Return whether the interface is switched on. */
	bool isOn();

	/** Switch the interface on or off. */
	void switchOnOff(bool on_off);

	/** Called by IP session to send a packet (identified by the leading
	 IP message) from this interface. 
	 returns whether the pkt was (or should have been) dropped
	 */
	bool send(IPv4Message* ipmsg, bool cannot_drop=false);

	/** Called by IP session to send a probe packet */
	void sendProbe(IPv4Message* ipmsg);

	/** Send the packet onto wire after the given delay. */
	void transmit(Packet* pkt, const VirtualTime& delay, UID_t callback_uid=0);

	/** Called to receive a packet (identified by its first protocol
	 message) through this interface. */
	void receive(Packet* pkt);

	/**
	 * Get the emulation protocol
	 */
	virtual EmulationProtocol* getEmulationProtocol();

	/**
	 * Get the ip session this iface is communicates with
	 */
	IPv4Session* getIPv4Session();

	/** called by a sphere or the community to register a concerned sphere **/
	void registerConcernedSphere(int com_id, UID_t sphere_id);


	virtual void exportState(StateLogger* state_logger, double sampleInterval);
	virtual void exportVizState(StateLogger* state_logger, double sampleInterval);
	void exportVizState(StateLogger* state_logger, double sampleInterval, double& bytes_in);

protected:
	void enqueueEmuPkt(Packet* pkt, VirtualTime deficit);
	void receiveEmuPkt(Packet* pkt);

private:
	/** Invoked by the tx timer callback function to transmit a packet
	 all interfaces on the same timeline. */
	void tx_packet_callback(Packet* pkt);

	/** if this is an edge interface, the sphere will be registered here as being concerned about its state **/
	SphereComList concerned_spheres;

	/** Recompute the delay of an emulation packet to facilitate the
	 latency hiding scheme. */
	//void recompute_emu_pkt_delay(ProtocolMessage* msg, VirtualTime& delay);

};

} // namespace ssfnet
} // namespace prime

#endif /*__INTERFACE_H__*/
