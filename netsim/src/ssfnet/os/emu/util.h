/**
 * \file util.h
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

#ifndef __EMUUTIL_H__
#define	 __EMUUTIL_H__

#include "os/ssfnet_types.h"
#include "os/virtual_time.h"
#include "os/emu/emulation_device.h"
#include "proto/ipv4/ipv4_message.h"
#include <sys/socket.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <string.h>

#ifndef PRIME_SSF_MACH_X86_DARWIN
#include <linux/if_tun.h>
#include <linux/if_packet.h>
#include <arpa/inet.h>
#include <net/if.h>
#endif /*PRIME_SSF_ARCH_X86_DARWIN*/

namespace prime {
namespace ssfnet {

#define ETH_HW_ADDR_LEN 6

/* Ethernet frame types */
#define ETH_P_8021Q 0x8100
#define ETH_P_ARP 0x0806
#define ETH_P_IP 0x0800
#define ETH_P_IPV6 0x86DD

/* ARP protocol constants */
#define ARP_PROTO_IP      0x0800
#define ARP_TYPE_ETH      0x0001
/* ARP opcode constants */
#define ARP_REQUEST   0x0001
#define ARP_REPLY     0x0002
#define RARP_REQUEST  0x0003
#define RARP_REPLY    0x0004

/* ARP hardware ident */
#define ARPHRD_ETHER 1

class TAPDevice;
class VETH;
class TAP;

int tun_open(char *dev);
int tun_open_old(char *dev);
void dump_buffer(char* b, int len);


class Portal;
class VETH;
class EmulationEvent;

class WaitingArpEvt {
public:
	typedef SSFNET_LIST(WaitingArpEvt*) List;
	WaitingArpEvt(Portal* portal_, EmulationEvent* evt_, VirtualTime time_, IPAddress tgt_): portal(portal_), veth(0), evt(evt_), time(time_), tgt(tgt_){}
	WaitingArpEvt(VETH* veth_, EmulationEvent* evt_, VirtualTime time_, IPAddress tgt_): portal(0), veth(veth_), evt(evt_), time(time_), tgt(tgt_){}
	WaitingArpEvt(const WaitingArpEvt& o): portal(o.portal), veth(o.veth), evt(o.evt), time(o.time), tgt(o.tgt){}
	~WaitingArpEvt(){
		if(evt) evt->free();
	}
	Portal* portal;
	VETH* veth;
	EmulationEvent* evt;
	VirtualTime time;
	IPAddress tgt;
	friend PRIME_OSTREAM& operator<< (PRIME_OSTREAM& os, WaitingArpEvt const& evt);
};

/**
 * \brief ArpEvent
 *
 * This class is used to deliver an arp response to a writer thread ).
 */
class ArpEvent : public SSFNetEvent {
public:
	/** The default constructor. */
	ArpEvent():
		SSFNetEvent(SSFNetEvent::SSFNET_ARP_EVT),
		ip(IPAddress::IPADDR_INVALID),
		mac(MACAddress::MACADDR_INVALID){}

	/** The constructor with given parameters. */
	ArpEvent(IPAddress ip_, MACAddress mac_):
		SSFNetEvent(SSFNetEvent::SSFNET_ARP_EVT),
		ip(ip_),
		mac(mac_){}

	/** The destructor. */
	virtual ~ArpEvent(){}

	/** The copy constructor. */
	ArpEvent(const ArpEvent& o):
		SSFNetEvent(SSFNetEvent::SSFNET_ARP_EVT),
		ip(o.ip),
		mac(o.mac){}

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone() { return new ArpEvent(*this); }

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine. It is
	 * actually used to serialize the event object into a byte stream
	 * represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/**
	 * Unpack the emulation event, reverse process of the pack method.
	 */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * This method is the factor method for deserializing the event
	 * object. A new emulation event object is created.
	 */
	static prime::ssf::Event* createArpEvent(prime::ssf::ssf_compact* dp);

	IPAddress ip;
	MACAddress mac;

	/** Used by SSF to declare an event class. */
	SSF_DECLARE_EVENT(ArpEvent);

	static VirtualTime MAX_ARP_LOOK_INTERVAL;
	static VirtualTime MAX_ARP_TIME;

};


class EthernetHeader {
	friend class TAP;
	friend class VETH;
	friend class TAPDevice;
private:
	uint8_t tgt_hw_addr[ETH_HW_ADDR_LEN];
	uint8_t src_hw_addr[ETH_HW_ADDR_LEN];
	uint16_t frame_type;

public:
	uint8_t* getSrc() { return  src_hw_addr; }
	uint8_t* getDst() { return  tgt_hw_addr; }
	uint16_t getFrameType() const { return ntohs(frame_type); }
	void setFrameType(uint16_t ft) {frame_type = htons(ft);}
	void setSrc(uint8_t addr[]) {
		for(int i=0;i<ETH_HW_ADDR_LEN;i++)
			src_hw_addr[i]=addr[i];
	}
	void setDst(uint8_t addr[]) {
		for(int i=0;i<ETH_HW_ADDR_LEN;i++)
			tgt_hw_addr[i]=addr[i];
	}
	bool isVLAN() { return ETH_P_8021Q == ntohs(frame_type);}
	bool hasARP() { return ETH_P_ARP ==  ntohs(frame_type);}
	bool hasIPv4() { return ETH_P_IP ==  ntohs(frame_type);}
	bool hasIPv6() { return ETH_P_IPV6 ==  ntohs(frame_type);}

	static bool isVLAN(uint16_t frame_type) { return ETH_P_8021Q == frame_type; }
	static bool isARP(uint16_t frame_type) { return ETH_P_ARP ==  frame_type; }
	static bool isIPv4(uint16_t frame_type) { return ETH_P_IP ==  frame_type; }
	static bool isIPv6(uint16_t frame_type) { return ETH_P_IPV6 ==  frame_type; }

	static SSFNET_STRING getTypeString(uint16_t t);

	friend PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, EthernetHeader const& hdr);
};

class VLANHeader {
private:
	uint16_t vid;//lower 12 bits
	uint16_t frame_type;
public:
	uint16_t getFrameType() const { return ntohs(frame_type); }
	uint16_t getVID() const { return ntohs(vid)*0x0FFF; }
	void setFrameType(uint16_t ft) { frame_type = htons(ft); }
	bool isVLAN() { return ETH_P_8021Q == ntohs(frame_type); }
	bool hasARP() { return ETH_P_ARP ==  ntohs(frame_type); }
	bool hasIPv4() { return ETH_P_IP ==  ntohs(frame_type); }
	bool hasIPv6() { return ETH_P_IPV6 ==  ntohs(frame_type); }

	friend PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, VLANHeader const& hdr);
};

class ARPHeader {
	friend class TAP;
private:
	uint16_t htype;
	uint16_t proto;
	uint8_t  hsize;
	uint8_t  psize;
	uint16_t opcode;
	uint8_t  src_mac[ETH_HW_ADDR_LEN];
	uint8_t  src_ip[4];
	uint8_t  tgt_mac[ETH_HW_ADDR_LEN];
	uint8_t  tgt_ip[4];

public:

	uint8_t* getSrcMac() { return  src_mac; }
	uint8_t* getDstMac() { return  tgt_mac; }
	uint16_t getHType()  const { return ntohs(htype); }
	uint16_t getProto()  const { return ntohs(proto); }
	uint8_t  getHSize()  const { return hsize; }
	uint8_t  getPSize()  const { return psize; }
	uint16_t getOpcode() const { return ntohs(opcode); }
	uint32_t getSrcIP() const { return ntohl(*((uint32_t*)src_ip)); }
	uint32_t getDstIP() const { return ntohl(*((uint32_t*)tgt_ip)); }

	SSFNET_STRING getOpcodeString() const;
	SSFNET_STRING getPString() const;
	SSFNET_STRING getHString() const;

	void setHType (uint16_t t) {htype = htons(t);}
	void setProto (uint16_t t) {proto = htons(t);}
	void setHSize (uint8_t  t) {hsize=t;}
	void setPSize (uint8_t  t) {psize=t;}
	void setOpcode(uint16_t t) {opcode = htons(t);}

	void setSrcMac(uint8_t addr[]) {
		for(int i=0;i<ETH_HW_ADDR_LEN;i++)
			src_mac[i]=addr[i];
	}
	void setDstMac(uint8_t addr[]) {
		for(int i=0;i<ETH_HW_ADDR_LEN;i++)
			tgt_mac[i]=addr[i];
	}
	void setSrcIP(uint32_t addr) {
		*((uint32_t*)src_ip)=htonl(addr);
	}
	void setDstIP(uint32_t addr) {
		*((uint32_t*)tgt_ip)=htonl(addr);
	}


	bool isArpRequest() { return ntohs(opcode) == ARP_REQUEST;}
	bool isArpReply() { return ntohs(opcode) == ARP_REPLY;}
	bool isRArpRequest() { return ntohs(opcode) == RARP_REQUEST;}
	bool isRArpReplt() { return ntohs(opcode) == RARP_REPLY;}

	friend PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, ARPHeader const& hdr);
};

class IPv6Header {
 private:
  uint8_t ver_cls_label[4];
  uint16_t payload_len;
  uint8_t  next_hdr;
  uint8_t  hop_limit;
  uint8_t  src_addr[16];
  uint8_t  dst_addr[16];
 public:

  uint8_t* getSrc() { return  src_addr; }
  uint8_t* getDst() { return  dst_addr; }
  uint16_t getPayloadLength() const { return ntohs(payload_len); }
  /*  uint8_t  getNextHdr() const { return ntohs(next_hdr); }
  uint8_t  getHopLimit() const { return ntohs(hop_limit); }
  uint8_t  getVersion() const { return (ver_cls_label&0xF0000000)>>28; }
  uint8_t  getTrafficClass() const { return (ver_cls_label&0x0FF000000)>>20; }
  uint8_t  getFlowLabel() const { return (ver_cls_label&0x000FFFFFF); }

  void setVersion(uint8_t t) {  ver_cls_label=((t&0x0f)<<28) & (ver_cls_label&0x0FFFFFFFF); }
  void setTrafficClass(uint8_t t) {  ver_cls_label=(t<<20) & (ver_cls_label&0x000FFFFFF); }
  void setFlowLabel(uint32_t t) { ver_cls_label=(ver_cls_label&0xFFF000000)&(t*0x000FFFFFF);}
  */
  void setPayloadLength(uint16_t t) {payload_len = htons(t);}
  /*
  void setNextHdr(uint8_t t) {next_hdr = t;}
  void setHopLimit(uint8_t t) {hop_limit = t;}
  void setSrc(uint8_t addr[]) {
    for(int i=0;i<16;i++)
      src_addr[i]=addr[i];
  }
  void setDst(uint8_t addr[]) {
    for(int i=0;i<16;i++)
      dst_addr[i]=addr[i];
  }
  */
  friend PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, IPv6Header const& hdr);
};


} //namespace ssfnet
} //namespace prime
#endif /*__EMUUTIL_H__*/
#endif /*SSFNET_EMULATION*/
