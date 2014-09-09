/**
 * \file util.cc
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

#include "os/emu/util.h"
#include "os/logger.h"
#include "proto/emu/emulation_protocol.h"

#include <stdio.h>
#include <netdb.h>
#include <sys/select.h>
#include <sys/types.h>
#include <unistd.h>
#include <iomanip>
#include <errno.h>
#include<net/ethernet.h>

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(EMUUtil);

/* pre 2.4.6 tun/tap compatibility */
#define OTUNSETNOCSUM  (('T'<< 8) | 200)
#define OTUNSETDEBUG	(('T'<< 8) | 201)
#define OTUNSETIFF	  (('T'<< 8) | 202)
#define OTUNSETPERSIST (('T'<< 8) | 203)
#define OTUNSETOWNER	(('T'<< 8) | 204)


VirtualTime ArpEvent::MAX_ARP_LOOK_INTERVAL=VirtualTime(100,VirtualTime::MILLISECOND);
VirtualTime ArpEvent::MAX_ARP_TIME=VirtualTime(10,VirtualTime::SECOND);


int tun_open_old(char *dev)
{
#ifdef PRIME_SSF_MACH_X86_DARWIN
	LOG_ERROR("Cannot use TAP Devices on DARWIN!");
#else
	char tunname[14];
	int i, fd;
	if( *dev ) {
		sprintf(tunname, "/dev/%s", dev);
		return open(tunname, O_RDWR);
	}
	for(i=0; i < 255; i++) {
		sprintf(tunname, "/dev/tun%d", i);
		/* Open device */
		if( (fd=open(tunname, O_RDWR)) > 0 ){
			sprintf(dev, "tun%d", i);
			return fd;
		}
	}
#endif
	return -1;
}


int tun_open(char *dev)
{
#ifdef PRIME_SSF_MACH_X86_DARWIN
	LOG_ERROR("Cannot use TAP Devices on DARWIN!");
#else
	struct ifreq ifr;
	int fd;
	if ((fd = open("/dev/net/tun", O_RDWR)) < 0) {
		return tun_open_old(dev);
	}

	memset(&ifr, 0, sizeof(ifr));
	ifr.ifr_flags = IFF_TAP | IFF_NO_PI;
	/*ifr.ifr_flags = IFF_TUN | IFF_NO_PI;*/
	if (*dev) {
		strncpy(ifr.ifr_name, dev, IFNAMSIZ);
	}

	if (ioctl(fd, TUNSETIFF, (void *) &ifr) < 0) {
		if (errno == EBADFD) {
			/* Try old ioctl */
			if (ioctl(fd, OTUNSETIFF, (void *) &ifr) < 0)
				goto failed;
		}
		else {
			goto failed;
		}
	}

	strcpy(dev, ifr.ifr_name);
	//printf("Created %s\n",dev);
	return fd;

	failed:
	close(fd);
	LOG_WARN("ACK!"<<endl);
#endif
	return -1;
}

void dump_buffer(char* b, int len)
{
	int idx=0;
	for(idx=0;idx<9;idx++){
		printf("|0%i",idx);
	}
	for(idx=10;idx<16;idx++){
		printf("|%i",idx);
	}
	printf("|(%p,%i)\n",b,len);
	for(idx=0;idx<45;idx++){
		printf("-");
	}
	printf("\n");
	for(idx=0;idx<len;idx++){
		if (idx % 15 == 0 && idx>0) {
			printf("|\n");
		}
		printf("|%02X", (unsigned char)b[idx]);
	}
	printf("\n");
}

SSFNET_STRING EthernetHeader::getTypeString(uint16_t t) {
	SSFNET_STRING rv;
	switch(t) {
	case ETH_P_8021Q:
		rv="vlan";
		break;
	case ETH_P_ARP:
		rv="arp";
		break;
	case ETH_P_IP:
		rv="ipv4";
		break;
	case ETH_P_IPV6:
		rv="ipv6";
		break;
	default:
		rv="UNKNOWN";
		break;
	}
	return rv;
}

PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, EthernetHeader const& hdr) {
	o <<"{Ethernet"
			<<"\n\tsrc_hw_addr=";
	o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[0];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[1];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[2];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[3];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[4];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[5];
	o <<"\n\ttgt_mac=";
	o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[0];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[1];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[2];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[3];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[4];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[5];
	return o <<"\n\tframe_type="<<hdr.getFrameType()
			<<"["<<EthernetHeader::getTypeString(hdr.getFrameType())<<"]"<<"}"<<std::dec;
}

PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, VLANHeader const& hdr) {
	return o <<"{VLAN"
			<<"\n\tframe_type="<<hdr.getFrameType()<<"["<<EthernetHeader::getTypeString(hdr.getFrameType())<<"]"
			<<"\n\tvid="<<hdr.getVID()
			<<"}"<<std::dec;
}

SSFNET_STRING ARPHeader::getOpcodeString() const {
	SSFNET_STRING rv;
	switch (getOpcode()){
	case ARP_REQUEST:
		rv="ARP_REQUEST";
		break;
	case ARP_REPLY:
		rv="ARP_REPLY";
		break;
	case RARP_REQUEST:
		rv="RARP_REQUEST";
		break;
	case RARP_REPLY:
		rv="RARP_REPLY";
		break;
	default:
		rv="BAD OPCODE";
		break;
	}
	return rv;
}

SSFNET_STRING ARPHeader::getHString() const {
	SSFNET_STRING rv;
	switch(getHType()) {
	case ARP_TYPE_ETH:
		rv="ETH";
		break;
	case ETH_P_IP:
		rv="IPv4";
		break;
	default:
		rv="UNKNWON";
	}
	return rv;
}

SSFNET_STRING ARPHeader::getPString() const {
	SSFNET_STRING rv;
	switch(getHType()) {
	case ARP_PROTO_IP:
		rv="IP";
		break;
	default:
		rv="UNKNWON";
	}
	return rv;
}

PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, ARPHeader const& hdr) {
	o <<"{ARP"
			<<"\n\thtype=0x"<<std::hex<<(uint16_t)hdr.getHType()<<"["<<hdr.getHString()<<std::dec<<"], size="<<(uint16_t)hdr.getHSize()
			<<"\n\tproto=0x"<<std::hex<<(uint16_t)hdr.getProto()<<"["<<hdr.getPString()<<std::dec<<"], size="<<(uint16_t)hdr.getPSize()
			<<"\n\topcode=0x"<<std::hex<<(uint16_t)hdr.getOpcode()<<"["<<hdr.getOpcodeString()<<"]"
			<<"\n\tsrc_mac=";
	o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[0];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[1];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[2];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[3];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[4];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[5]<<std::dec;
	o <<"\n\tsrc_ip="
			<<(uint16_t)hdr.src_ip[0]
			                       <<"."<<(uint16_t)hdr.src_ip[1]
			                                                   <<"."<<(uint16_t)hdr.src_ip[2]
			                                                                               <<"."<<(uint16_t)hdr.src_ip[3]
			                                                                                                           <<"\n\ttgt_mac=";
	o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[0];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[1];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[2];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[3];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[4];
	o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[5]<<std::dec;
	return o <<"\n\ttgt_ip="
			<<(uint16_t)hdr.tgt_ip[0]
			                       <<"."<<(uint16_t)hdr.tgt_ip[1]
			                                                   <<"."<<(uint16_t)hdr.tgt_ip[2]
			                                                                               <<"."<<(uint16_t)hdr.tgt_ip[3]
			                                                                                                           <<"}"<<std::dec;
}


PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, IPv6Header const& hdr) {
	return o <<"{IPv6 payload_length="<<hdr.getPayloadLength()<<"}";
}


prime::ssf::ssf_compact* ArpEvent::pack()
{
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_int((uint32)ip);
	unsigned char temp[6];
	mac.toRawMac(temp);
	dp->add_unsigned_char_array(6,temp);
	return dp;
}

void ArpEvent::unpack(prime::ssf::ssf_compact* dp)
{
	SSFNetEvent::unpack(dp);
	uint32_t ip_;
	dp->get_unsigned_int(&ip_);
	ip=IPAddress(ip_);
	unsigned char temp[6];
	dp->get_unsigned_char(temp,6);
	mac=MACAddress(temp);
}

prime::ssf::Event* ArpEvent::createArpEvent(prime::ssf::ssf_compact* dp)
{
	ArpEvent* e = new ArpEvent();
	e->unpack(dp);
	return e;
}

PRIME_OSTREAM& operator<< (PRIME_OSTREAM& os, WaitingArpEvt const& evt) {
        return os << "[evt:"<<(void*)evt.evt<<", portal:"<<(void*)evt.portal<<", tgt:"<<evt.tgt<<", time:"<<evt.time<<"]";
}


} //namespace ssfnet
} //namespace prime

#endif /*SSFNET_EMULATION*/
