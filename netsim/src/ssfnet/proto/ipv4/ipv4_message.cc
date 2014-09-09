/**
 * \file ipv4_message.cc
 * \brief Source file for the IPv4Message class.
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

#include "os/logger.h"
#include "proto/ipv4/ipv4_message.h"
#include "os/data_message.h"

#include "netinet/in.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT_REF(IPv4Session);

IPv4Message::IPv4Message(): RawProtocolMessage() {
}

IPv4Message::IPv4Message(const IPv4Message& ipmsg) : RawProtocolMessage(ipmsg) {
}

IPv4Message::IPv4Message(IPAddress src, IPAddress dst, int proto, int hdrlen, 
		int tos, int ident, int offset, int ttl)
{
	//LOG_DEBUG("Created IPv4Message "<<(void*)this<<endl)
	assert(hdrlen >= DEFAULT_IPV4_HDRLEN);
	rawptr = new byte[hdrlen];
	setHdrLen(hdrlen);
	setTOS(tos);
	setIdent(ident);
	setOffset(offset);
	setTTL(ttl);
	setProto(proto);
	setSrc(src);
	setDst(dst);
}

ProtocolMessage* IPv4Message::clone() { return new IPv4Message(*this); }

void IPv4Message::fromRawBytes(Packet* p, byte*& b, int& n)
{
	RawProtocolMessage::fromRawBytes(p, b, n);
	if(n > 0) { // continue to decode for the payload
		int proto = (int)getProto();
		ProtocolMessage* msg = newMessage(proto);
		if(!msg) {
			msg = new DataMessage();
		}
		msg->fromRawBytes(pkt, b, n);
		link_payload(msg);
	}
}

void IPv4Message::toRawBytes(byte*& b, int& n)
{
	byte* bb=b;
	int nn=n;
	RawProtocolMessage::toRawBytes(b, n); //will side effect b and n

	//LOG_DEBUG("cksum before "<<std::hex<<((IPv4RawHeader*)bb)->getCheckSum()<<endl)
	((IPv4RawHeader*)bb)->setLen(nn);
#if !PRIME_SSFNET_IPV4_OFFLOAD_CKSUM
	if(((IPv4RawHeader*)bb)->getCheckSum()!=0) {
		((IPv4RawHeader*)bb)->setCheckSum(0);
		((IPv4RawHeader*)bb)->setCheckSum(cksum((uint16*)bb, ((IPv4RawHeader*)bb)->getHdrLen()));
	}
#endif
	//LOG_DEBUG("cksum after "<<std::hex<<((IPv4RawHeader*)bb)->getCheckSum()<<endl)
}

uint8 IPv4Message::getHdrLen() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((IPv4RawHeader*)rawptr)->getHdrLen();
}

uint8 IPv4Message::getTOS() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((IPv4RawHeader*)rawptr)->getTOS();
}

uint16 IPv4Message::getLen() const
{ 
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((IPv4RawHeader*)rawptr)->getLen();
}

uint16 IPv4Message::getIdent() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((IPv4RawHeader*)rawptr)->getIdent();
}

uint16 IPv4Message::getOffset() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((IPv4RawHeader*)rawptr)->getOffset();
}

uint8 IPv4Message::getTTL() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((IPv4RawHeader*)rawptr)->getTTL();
}

uint8 IPv4Message::getProto() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((IPv4RawHeader*)rawptr)->getProto();
}

IPAddress IPv4Message::getSrc() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return IPAddress(((IPv4RawHeader*)rawptr)->getSrc());
}

IPAddress IPv4Message::getDst() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return IPAddress(((IPv4RawHeader*)rawptr)->getDst());
}

void IPv4Message::setHdrLen(uint8 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setHdrLen(x);
}

void IPv4Message::setTOS(uint8 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setTOS(x);
}

void IPv4Message::setLen(uint16 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setLen(x);
}

void IPv4Message::setIdent(uint16 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setIdent(x);
}

void IPv4Message::setOffset(uint16 x)
{ 
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setOffset(x);
}

void IPv4Message::setTTL(uint8 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setTTL(x);
}

void IPv4Message::decrementTTL(uint8 x) {
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->decrementTTL(x);
}

void IPv4Message::setProto(uint8 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setProto(x);
}

void IPv4Message::setSrc(IPAddress x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setSrc((uint32)x);
}

void IPv4Message::setDst(IPAddress x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((IPv4RawHeader*)rawptr)->setDst((uint32)x);
}


PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, IPv4RawHeader const& hdr) {
  uint8_t* src=(uint8_t*)&(hdr.ip_src);
  uint8_t* dst=(uint8_t*)&(hdr.ip_dst);
  return o <<"{IPv4"
	   <<"\n\tsrc="
	   <<(uint16_t)src[0]
	   <<"."<<(uint16_t)src[1]
	   <<"."<<(uint16_t)src[2]
	   <<"."<<(uint16_t)src[3]
	   <<"\n\tdst="
	   <<(uint16_t)dst[0]
	   <<"."<<(uint16_t)dst[1]
	   <<"."<<(uint16_t)dst[2]
	   <<"."<<(uint16_t)dst[3]
	   <<"\n\thdr_len="<<(uint16_t)hdr.getHdrLen()
	   <<"\n\ttos="<<(uint16_t)hdr.getTOS()
	   <<"\n\ttoal_len="<<(uint16_t)hdr.getLen()
	   <<"\n\tident="<<(uint16_t)hdr.getIdent()
	   <<"\n\toffset="<<(uint16_t)hdr.getOffset()
	   <<"\n\tttl="<<(uint16_t)hdr.getTTL()
	   <<"\n\tproto="<<(uint16_t)hdr.getProto()<<"["<<hdr.getProtoString()<<"]"
	   <<"\n\tchecksum="<<hdr.getCheckSum()
	   <<"}\n";
}

SSFNET_STRING IPv4RawHeader::getProtoString() const {
 SSFNET_STRING rv;
  switch(getProto()) {
  case IPPROTO_ICMP:
    rv="icmp";
    break;
  case IPPROTO_TCP:
    rv="tcp";
    break;
  case IPPROTO_UDP:
    rv="udp";
    break;
  case IPPROTO_IPIP: /*IP-IP tunneling*/
    rv="ip";
    break;
  default:
    rv="UNKNOWN";
  }
  return rv;
}

SSFNET_REGISTER_MESSAGE(IPv4Message, SSFNET_PROTOCOL_TYPE_IPV4);

} // namespace ssfnet
} // namespace prime
