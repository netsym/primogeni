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
	state_configuration {
		configurable IPAddress ip_address {
			type = OBJECT;
			default_value =  "0.0.0.0" ;
			unserialize_fct = str2ip;
			serialize_fct = ip2str;
			doc_string = "ip address";
			visualized=true;
		};
		configurable MACAddress mac_address {
			type = OBJECT;
			default_value =  "0:0:0:0:0:0" ;
			unserialize_fct = str2mac;
			serialize_fct = mac2str;
			doc_string = "mac address";
			visualized=false;
		};
		/** The link this interface is attached to. */
		Link* attached_link;
	}

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
	state_configuration {
		configurable UID_t real_uid {
			type = INT;
			default_value =  "0" ;
			doc_string = "the uid of the remote node";
			visualized=true;
		};
		configurable UID_t community_id {
			type = INT;
			default_value =  "0" ;
			doc_string = "the id of the remote community";
			visualized=true;
		};
	}
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
class Interface: public ConfigurableEntity<Interface, BaseInterface, 0, "Iface"> {
	friend class TxTimerQueue;
	friend class EmulationProtocol;
	friend class TrafficPortal;

	state_configuration {
		shared configurable float bit_rate {
			type = FLOAT;
			default_value =  "1e10" ;
			doc_string = "transmit speed";
			visualized=true;
		};
		shared configurable float latency {
			type = FLOAT;
			default_value =  "0";
			doc_string = "transmit latency";
			visualized=true;
		};
		shared configurable float jitter_range {
			type = FLOAT;
			default_value =  "0" ;
			doc_string = "jitter range";
			visualized=true;
		};
		
		configurable float drop_probability {
			type = FLOAT;
			default_value =  "0" ;
			doc_string = "drop probability";
			visualized=true;
			statistic=true;
		};
		
		shared configurable int buffer_size {
			type = INT;
			default_value =  "65536" ;
			doc_string = "send buffer size";
			visualized=true;
		};
		shared configurable int mtu {
			type = INT;
			default_value =  "1500" ;
			doc_string = "maximum transmission unit";
			visualized=true;
		};
		shared configurable SSFNET_STRING queue_type {
			type= STRING;
			default_value="DropTailQueue";
			doc_string="The class to use for the queue.";
			visualized=true;
		};
		configurable bool is_on {
			type= BOOL;
			default_value="true";
			doc_string="is the interface on?";
			visualized=true;
			statistic=true;
		};
		configurable int num_in_packets {
			type= INT;
			default_value="0";
			doc_string="number of packets received";
			visualized=true;
			statistic=true;
		};
		configurable int num_in_bytes {
			type= INT;
			default_value="0";
			doc_string="number of bytes received";
			visualized=true;
			statistic=true;
		};
		configurable float packets_in_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of packets per second";
			visualized=false;
			statistic=true;
		};
		configurable float bytes_in_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of bytes per second";
			visualized=false;
			statistic=true;
		};
		configurable int num_in_ucast_packets {
			type= INT;
			default_value="0";
			doc_string="number of unicast packets received";
		};
		configurable int num_in_ucast_bytes {
			type= INT;
			default_value="0";
			doc_string="number of unicast bytes received";
		};
		configurable int num_out_packets {
			type= INT;
			default_value="0";
			doc_string="number of packets sent";
			visualized=true;
			statistic=true;
		};
		configurable int num_out_bytes {
			type= INT;
			default_value="0";
			doc_string="number of bytes sent";
			visualized=true;
			statistic=true;
		};
		configurable int num_out_ucast_packets {
			type= INT;
			default_value="0";
			doc_string="number of unicast packets sent";
		};
		configurable int num_out_ucast_bytes {
			type= INT;
			default_value="0";
			doc_string="number of unicast bytes sent";
		};
		configurable float packets_out_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of packets per second";
			visualized=false;
			statistic=true;
		};	
		configurable float bytes_out_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of bytes per second";
			visualized=false;
			statistic=true;
		};

		configurable int queue_size {
			type= INT;
			default_value="0";
			doc_string="quesize";
			visualized=true;
			statistic=true;
		};

		child_type<NicQueue> nic_queue {
			min_count = 0;
			max_count = 1;
			is_aliased = false;
			doc_string = "The queue for this nic";
		};

		child_type<EmulationProtocol> emu_proto {
			min_count = 0;
			max_count = 1;
			is_aliased = false;
			doc_string = "If this interface is emulated then this protcol must be set";
		};

		/** a cache of the emu proto*/
		EmulationProtocol* emu_protocol;

		/** The send queue maintained by this network interface. */
		NicQueue* tx_queue;

		/** The timer for packet transmission. */
		TxTimerQueue* tx_timer_queue;

		/** Caching a pointer to the IP session that manages this interface. */
		IPv4Session* ip_sess;

	}

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
