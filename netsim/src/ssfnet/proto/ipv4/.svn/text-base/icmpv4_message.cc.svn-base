/**
 * \file icmpv4_message.cc
 * \brief Source file for the ICMPv4Message class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
 * \author Miguel Erazo
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
#include "proto/ipv4/icmpv4_message.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT_REF(ICMPv4Message);

ICMPv4Message::ICMPv4Message() {
}

ICMPv4Message::ICMPv4Message(const ICMPv4Message& icmpmsg) :
		  RawProtocolMessage(icmpmsg), msglen(icmpmsg.msglen) {
}

ICMPv4Message::ICMPv4Message(uint16 id, uint16 seq, int datalen, byte* data) :
		  msglen(8+datalen)
{
	rawptr = new byte[msglen];
	setType(ICMP_TYPE_ECHO);
	setCode(0);
	setEchoMsgID(id);
	setEchoMsgSeq(seq);
	if(data) setEchoMsgData(data, datalen);
}

ICMPv4Message::ICMPv4Message(uint8 type, uint8 code, IPv4Message* iphdr)
{
	if((int)type == ICMP_TYPE_TIME_EXCEEDED) {
		((IPv4RawHeader*)(iphdr->getRawData()))->setTTL(2);

		LOG_DEBUG("[debug traceroute] inside icmp, src=" << (IPAddress)((IPv4RawHeader*)(iphdr->getRawData()))->getSrc() << " dst=" <<
				(IPAddress)((IPv4RawHeader*)(iphdr->getRawData()))->getDst() << endl);

		int accu = 0;
		uint16* addr = (uint16*)(iphdr->getRawData());
		int len = 20;
		register int sum = 0;
		uint16 answer = 0;
		register uint16 *w = addr;
		register int nleft = len;

		uint16 aux = *w;
		uint16 first = (*w << 8);
		uint16 second = (*w >> 8);
		uint16 total = first + second;
		LOG_DEBUG("[debug checksum] first=" << hex << first << " second=" << hex << second << " total=" << hex << total <<
										" aux=" << hex << aux << endl);

		while (nleft > 1) {
			uint16 aux = *w;
			uint16 first = (*w << 8);
			uint16 second = (*w >> 8);
			uint16 total = first + second;

			if(nleft==10){
				total = 0;
			}

			LOG_DEBUG("\t[debug checksum] before summing, *w=" << hex << *w << " total=" << hex << total << endl);
			sum += total; w++;
			nleft -= 2;
			LOG_DEBUG("[debug checksum] sum=" << hex << sum << " nleft=" << dec << nleft << endl);
		}

		// mop up an odd byte, if necessary
		if (nleft == 1) {
			*(byte*) (&answer) = *(byte*) w;
			sum += answer;
		}

		// add back carry outs from top 16 bits to low 16 bits
		sum = (sum >> 16) + (sum & 0xffff); // add hi 16 to low 16
		if (accu)
						sum += (~accu) & 0xffff; // add accumulative result
		sum += (sum >> 16); // add carry
		answer = ~sum; // truncate to 16 bits

		uint16 first_ = (answer << 8);// + ( (aux << 4) & (0x0010));
		uint16 second_ = (answer >> 8);
		uint16 total_ = first_ + second_;
		((IPv4RawHeader*)(iphdr->getRawData()))->setCheckSum(total_);
	} else {
		// do nothing
	}

	if((int)type == ICMP_TYPE_TIME_EXCEEDED) {
		msglen = 4 + iphdr->size() + 8 + 4;//icmp(4) + unused(4) + ip(8) + 4(icmp echo)
	} else {
		msglen = iphdr->size()+8;
		if(msglen > iphdr->getLen()) msglen = iphdr->getLen();
	}

	LOG_DEBUG("msglen for ip-icmp=" << msglen << " iphdr->getLen()=" << iphdr->getLen() << endl);
	//if(msglen > iphdr->getLen()) msglen = iphdr->getLen();
	rawptr = new byte[msglen];
	setType(type);
	setCode(code);
	if(!iphdr->isStandalone()) {
		if((int)type == ICMP_TYPE_TIME_EXCEEDED) {
			LOG_DEBUG("executing ICMP_TYPE_TIME_EXCEEDED code (2)" << endl);
			memcpy(rawptr+8, iphdr->getRawData(), 28);
		} else {
			setDataMsgData(iphdr->getRawData(), msglen);
		}
	} else {
		LOG_DEBUG(" standalone message!" << endl);
		setDataMsgData(iphdr->getRawData(), iphdr->size());
		int r = msglen - iphdr->size();
		int o = iphdr->size();
		ProtocolMessage* m = iphdr->payload();
		while(r > 0) {
			byte* b = m->getRawData();
			if(b) setDataMsgData(b, r, o);
			else { /* ignore the data */ }
			r -= m->size(); o += m->size();
			m = m->payload();
		}
	}
}

ICMPv4Message::ICMPv4Message(uint16 id, uint16 seq) :
		  msglen(20)
{
	rawptr = new byte[msglen];
	setType(ICMP_TYPE_TIMESTAMP);
	setCode(0);
	setTimeMsgID(id);
	setTimeMsgSeq(seq);
}

ProtocolMessage* ICMPv4Message::clone() { return new ICMPv4Message(*this); }

void ICMPv4Message::serialize(byte* buf, int& offset)
{
	if(buf) {
		prime::ssf::ssf_compact::serialize((int32)msglen, &buf[offset]);
		offset += sizeof(int32);
		if(rawptr) memcpy(&buf[offset], rawptr, msglen);
		else LOG_ERROR("protocol message empty for serialization.");
		offset += msglen;
		LOG_DEBUG("serialize raw: size="<<packingSize()<<", first byte=0x"<<std::hex<<(uint32)*rawptr<<endl);
	} else LOG_ERROR("empty serialization buffer.");
}
int ICMPv4Message::pack(prime::ssf::ssf_compact* dp) {
	dp->add_int(msglen);
	if(msglen>0){
		dp->add_char_array(msglen,(char*)rawptr);
		return msglen+sizeof(int);
	}
	else {
		dp->add_int(0);
		return sizeof(int);
	}
}

void ICMPv4Message::deserialize(byte* buf, int& offset)
{
	if(buf) {
		int32 t; prime::ssf::ssf_compact::deserialize(t, &buf[offset]); msglen = (int)t;
		offset += sizeof(int32);
		if(rawptr) LOG_ERROR("protocol message not empty for deserialization");
		rawptr = new byte[msglen];
		memcpy(rawptr, &buf[offset], msglen);
		offset += msglen;
		LOG_DEBUG("deserialize raw: size="<<packingSize()<<", first byte=0x"<<std::hex<<(uint32)*rawptr<<endl);
	} else LOG_ERROR("empty serialization buffer.");
}



int ICMPv4Message::unpack(prime::ssf::ssf_compact* dp) {
	dp->get_int(&msglen);
	if(msglen>0) {
		rawptr = new byte[msglen];
		dp->get_char((char*)rawptr,msglen);
		return msglen+sizeof(int);
	}
	else {
		rawptr=0;
		return sizeof(int);
	}
}

void ICMPv4Message::fromRawBytes(Packet* p, byte*& b, int& n)
{
	//assume the size is the whole thing....
	msglen=n;
	RawProtocolMessage::fromRawBytes(p, b, n);
}

void ICMPv4Message::toRawBytes(byte*& b, int& n)
{
	if(n != msglen) LOG_ERROR("ill-formed real packet content.");
	byte* bb=b;
	int nn=n;
	RawProtocolMessage::toRawBytes(b, n); //will side effect b and n

	//LOG_DEBUG("cksum before "<<std::hex<<((RawICMPv4CommonHeader*)bb)->getCheckSum()<<endl)
#if !PRIME_SSFNET_IPV4_OFFLOAD_CKSUM
	((RawICMPv4CommonHeader*)bb)->setCheckSum(0);
	((RawICMPv4CommonHeader*)bb)->setCheckSum(cksum((uint16*)bb, nn));
#endif
	//LOG_DEBUG("cksum after "<<std::hex<<((RawICMPv4CommonHeader*)bb)->getCheckSum()<<endl)
}

uint8 ICMPv4Message::getType() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4CommonHeader*)rawptr)->getType();
}

uint8 ICMPv4Message::getCode() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4CommonHeader*)rawptr)->getCode();
}

uint16 ICMPv4Message::getCheckSum() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4CommonHeader*)rawptr)->getCheckSum();
}

byte* ICMPv4Message::getDataMsgData() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4IPDataHeader*)rawptr)->getData();
}

uint16 ICMPv4Message::getEchoMsgID() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4EchoHeader*)rawptr)->getID();
}

uint16 ICMPv4Message::getEchoMsgSeq() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4EchoHeader*)rawptr)->getSeq();
}

byte* ICMPv4Message::getEchoMsgData() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4EchoHeader*)rawptr)->getData();
}

uint16 ICMPv4Message::getTimeMsgID() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4TimestampHeader*)rawptr)->getID();
}

uint16 ICMPv4Message::getTimeMsgSeq() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4TimestampHeader*)rawptr)->getSeq();
}

uint32 ICMPv4Message::getTimeMsgOriginateTimestamp() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4TimestampHeader*)rawptr)->getOriginateTimestamp();
}

uint32 ICMPv4Message::getTimeMsgReceiveTimestamp() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4TimestampHeader*)rawptr)->getReceiveTimestamp();
}

uint32 ICMPv4Message::getTimeMsgTransmitTimestamp() const
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	return ((RawICMPv4TimestampHeader*)rawptr)->getTransmitTimestamp();
}

void ICMPv4Message::setType(uint8 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4CommonHeader*)rawptr)->setType(x);
}

void ICMPv4Message::setCode(uint8 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4CommonHeader*)rawptr)->setCode(x);
}

void ICMPv4Message::setCheckSum(uint16 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4CommonHeader*)rawptr)->setCheckSum(x);
}

void ICMPv4Message::setDataMsgData(byte* bf, int bfsz, int offset)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	//LOG_DEBUG("setting " << " bytes << bfsz << " of the icmp message" << endl);
	((RawICMPv4IPDataHeader*)rawptr)->setData(bf, bfsz, offset);
}

void ICMPv4Message::setEchoMsgID(uint16 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4EchoHeader*)rawptr)->setID(x);
}

void ICMPv4Message::setEchoMsgSeq(uint16 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4EchoHeader*)rawptr)->setSeq(x);
}

void ICMPv4Message::setEchoMsgData(byte* bf, int bfsz, int offset)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4EchoHeader*)rawptr)->setData(bf, bfsz, offset);
}

void ICMPv4Message::setTimeMsgID(uint16 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4TimestampHeader*)rawptr)->setID(x);
}

void ICMPv4Message::setTimeMsgSeq(uint16 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4TimestampHeader*)rawptr)->setSeq(x);
}

void ICMPv4Message::setTimeMsgOriginateTimestamp(uint32 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4TimestampHeader*)rawptr)->setOriginateTimestamp(x);
}

void ICMPv4Message::setTimeMsgReceiveTimestamp(uint32 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4TimestampHeader*)rawptr)->setReceiveTimestamp(x);
}

void ICMPv4Message::setTimeMsgTransmitTimestamp(uint32 x)
{
	if(!rawptr) LOG_ERROR("empty protocol message.");
	((RawICMPv4TimestampHeader*)rawptr)->setTransmitTimestamp(x);
}

ICMPv4Message* ICMPv4Message::makeEchoRequestMsg(uint16 id, uint16 seq, int datalen, byte* data)
{
	return new ICMPv4Message(id, seq, datalen, data);
}

ICMPv4Message* ICMPv4Message::makeEchoReplyMsg(ICMPv4Message* req)
{
	assert(req && ICMP_TYPE_ECHO == req->getType());
	req->setType(ICMP_TYPE_ECHO_REPLY);
	return req;
}

ICMPv4Message* ICMPv4Message::makeDestUnreachMsg(uint8 code, IPv4Message* iphdr)
{
	return new ICMPv4Message(ICMP_TYPE_DEST_UNREACH, code, iphdr);
}

ICMPv4Message* ICMPv4Message::makeSourceQuenchMsg(IPv4Message* iphdr)
{
	return new ICMPv4Message(ICMP_TYPE_SOURCE_QUENCH, 0, iphdr);
}

ICMPv4Message* ICMPv4Message::makeTimeExceededMsg(uint8 code, IPv4Message* iphdr)
{
	LOG_DEBUG("[debug checksum] in makeTimeExceededMsg" << endl);
	return new ICMPv4Message(ICMP_TYPE_TIME_EXCEEDED, code, iphdr);
}

ICMPv4Message* ICMPv4Message::makeRedirectMsg(uint8 code, IPv4Message* iphdr)
{
	return new ICMPv4Message(ICMP_TYPE_REDIRECT, code, iphdr);
}

ICMPv4Message* ICMPv4Message::makeParamProbMsg(uint8 code, IPv4Message* iphdr)
{
	return new ICMPv4Message(ICMP_TYPE_PARAMETER_PROB, code, iphdr);
}

ICMPv4Message* ICMPv4Message::makeTimeRequestMsg(uint16 id, uint16 seq)
{
	// the times will be filled in at the interface
	return new ICMPv4Message(id, seq);
}

ICMPv4Message* ICMPv4Message::makeTimeReplyMsg(ICMPv4Message* req)
{
	assert(req && ICMP_TYPE_TIMESTAMP == req->getType());
	req->setType(ICMP_TYPE_TIMESTAMP_REPLY);
	// the times will be filled in at the interface
	return req;
}

SSFNET_REGISTER_MESSAGE(ICMPv4Message, SSFNET_PROTOCOL_TYPE_ICMPV4);

} // namespace ssfnet
} // namespace prime

