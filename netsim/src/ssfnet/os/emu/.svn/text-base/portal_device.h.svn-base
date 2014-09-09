/**
 * \file portal_device.h
 * \brief XXX
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

#ifdef SSFNET_EMULATION

#ifndef __EMUPORTALDEVICE_H__
#define	 __EMUPORTALDEVICE_H__

//#define USE_TX_RING //if not defined we use a regular raw socket

#include "os/emu/util.h"
#include "os/emu/emulation_device.h"
#include "os/trie.h"
#include "os/emu/portal_emu_proto.h"
#include "proto/ipv4/ipv4_message.h"
#ifndef PRIME_SSF_MACH_X86_DARWIN
#include <pcap.h>
#endif

#include <sys/socket.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/select.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <string.h>

#ifndef PRIME_SSF_MACH_X86_DARWIN
#include <linux/if_tun.h>
#include <linux/if_ether.h>
#include <linux/if_packet.h>
#endif /*PRIME_SSF_ARCH_X86_DARWIN*/


namespace prime {
namespace ssfnet {

class PortalDevice;
class Portal;

class TrafficPortalPair : public TrieData {
public:
	TrafficPortalPair(IPPrefix prefix_, TrafficPortal* trafficPortal_, Portal* portal_):
		prefix(prefix_),trafficPortal(trafficPortal_), portal(portal_){
	}
	TrafficPortalPair(const TrafficPortalPair& o):
		prefix(o.prefix), trafficPortal(o.trafficPortal), portal(o.portal){
	}
	virtual ~TrafficPortalPair() {}
	IPPrefix prefix;
	TrafficPortal* trafficPortal;
	Portal* portal;
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TrafficPortalPair& x);
};

/**
 * \brief PortalTable
 *
 * This class maps IP addresses (in the form a.b.c.d/p) into to interfaces which are
 * traffic portals
 *
 */
class PortalTable : protected Trie {
public:
	/** The constructor. */
	PortalTable() {}

	/** The destructor. */
	virtual ~PortalTable() {}

	void addPortal(IPPrefix& network, Portal* portal, TrafficPortal* proto, bool replace = true);

	TrafficPortalPair* getPortal(uint32 ipaddr);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const PortalTable& x);

	void setupRoutes(IPPrefix sim_net);
protected:
	/** The recursive helper to the setup routes cmd. */
	void setupRoutes(TrieNode* root, uint32 sofar, int n);
	/** The recursive helper to the dump method. */
	static void dump_helper(TrieNode* root, uint32 sofar,
			int n, PRIME_OSTREAM &os);
};

class Portal {
public:
	typedef SSFNET_MAP(UID_t,Portal*) Map;
	Portal(EmulationProtocol* emuproto, PortalDevice* dev);
	~Portal();
	void init();
	bool process_pkts(int max_pkts);
	void sendPacket(EmulationEvent* evt, MACAddress& dstmac);
	inline EmulationProtocol* getEmuProto(){ return emuproto; }
#ifndef PRIME_SSF_MACH_X86_DARWIN
	inline pcap_t* getPcapHandle(){ return pcap_handle; }
#endif
	inline int getRxFD() { return rx_fd; }
	inline int getTxFD() { return tx_fd; }
	MACAddress& getMAC() { return mac; }
	IPAddress& getIP() { return ip; }
	SSFNET_STRING& getIfaceName() { return iface_name; }
	void sendArpRequest(uint32 ip);
	void sendArpResponse(MACAddress requester_mac, IPAddress requester_ip, IPAddress requested_ip, MACAddress requested_mac);

	friend PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, Portal const& portal);

private:
#ifndef PRIME_SSF_MACH_X86_DARWIN
#ifdef USE_TX_RING
	struct tpacket_hdr * nextTxFrame();
#endif
#endif
	static void process_pkt(u_char * portal, const struct pcap_pkthdr * pkt_meta, const u_char * pkt);

	PortalDevice* dev;
	EmulationProtocol* emuproto;
	int rx_fd, tx_fd;

#ifndef PRIME_SSF_MACH_X86_DARWIN
	/** for receiving */
	pcap_t * pcap_handle;
	/** for sending */
#ifdef USE_TX_RING
	volatile int tx_data_offset;
	int tx_idx;
	volatile struct tpacket_hdr * tx_header_start;
#else
	byte* sendbuf;
#endif
	struct sockaddr_ll my_addr;
#ifdef USE_TX_RING
	struct tpacket_req s_packet_req;
#endif
#endif
	/** local state */
	MACAddress mac;
	IPAddress ip;
	SSFNET_STRING iface_name;

	/** static vars configured via env variables**/
#ifdef USE_TX_RING
	static int c_buffer_sz; //default 1024*8
	static int c_buffer_nb; //default 1024
	static int c_sndbuf_sz; //default 0
#endif
	static int c_mtu; //default 0
	static int mode_loss; //default 1
	static bool sconfiged;
};

class PortalDevice : public BlockingEmulationDevice {
 public:
	typedef SSFNET_MAP(IPAddress,MACAddress) IP2MACMap;
	 friend class Portal;
	 enum ThreadType {
		 READER=0,
		 WRITER=1
	 };
	 /**
	  * the constructor
	  */
	 PortalDevice();

	 /**
	  * the deconstructor
	  */
	 virtual ~PortalDevice();

	 /** Initialize the device.
	  * derived classes must implement this
	  */
	 virtual void init();

	 /**
	  * close the emulation device
	  */
	 virtual void wrapup();

	 /** Called by the I/O manager to register an interface/protocol with this device.
	  * Once the device is setup it should call back to the EmulationProtocol with itself as
	  * the emulation device.
	  */
	 virtual void registerEmulationProtocol(EmulationProtocol* emu_proto);

	 /**
	  *
	  */
	 virtual void registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto);

	 /** called by EmulationProtocols to export messages */
	 virtual void exportPacket(Interface* iface, Packet* pkt);

	 /**
	  * called by the i/o manager to handle proxied emulation evts
	  */
	 virtual void handleProxiedEvent(EmulationEvent* evt);

	 /**
	  * returns whether this emulation device is ready
	  */
	 virtual bool isActive();

	 /**
	  * If this type of protocol requires that only one instance exist on a host
	  * then only _1_ I/O manager can create one of these devices. All other
	  * I/O managers must proxy access to the owning I/OManager.
	  * then
	  */
	 virtual bool requiresSingleInstancePerHost();

	 /**
	  * Get the type of emulation device
	  */
	 virtual int getDeviceType();

	 /**
	  * return the emulation protocol that corresponds to the ip
	  */
	 virtual EmulationProtocol* ip2EmulationProtocol(IPAddress& ip);

	 /**
	  * Called by read to implement the reader thread
	  */
	 virtual void reader_thread();

	 /**
	  * Called by write to implement the writer thread
	  */
	 virtual void writer_thread();

 protected:

	 void handleArp(Portal* portal, EthernetHeader* eth, ARPHeader* request);
	 MACAddress* lookupMAC(IPAddress ip);
	 void insertIntoSim(EthernetHeader* eth, IPv4RawHeader* ip, uint32_t len);

	 IP2MACMap ip2mac;
	 PortalTable ip2portal;
	 Portal::Map uid2portal;
	 WaitingArpEvt::List watingForArps;
	 int maxfd;
	 IPPrefix sim_net;
};


} //namespace ssfnet
} //namespace prime
#endif /*__EMUPORTALDEVICE_H__*/
#endif /*SSFNET_EMULATION*/
