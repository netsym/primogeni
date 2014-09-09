/*
 * \file udp_message.cc
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
#include "udp_message.h"
#include "os/logger.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(UDPMessage);

UDPMessage::UDPMessage(): RawProtocolMessage() {}

UDPMessage::UDPMessage(uint16 src_port_, uint16 dst_port_, uint16 length_,
		uint16 checksum_): RawProtocolMessage()
{
	LOG_DEBUG("creating a udp message with srcport=" << src_port_ << " dstport=" << dst_port_ <<
			" length=" << length_ << endl);

	//udp length is the header length + the payload length
	//we need to match sure everything jives!
	assert(length_>=sizeof(RawUDPHeader));

	rawptr=new byte[sizeof(RawUDPHeader)];
	setSrcPort(src_port_);
	setDstPort(dst_port_);
	setLength(length_);
	setCheckSum(checksum_);
}

UDPMessage::UDPMessage(UDPMessage const & udpmsg_)
		: RawProtocolMessage(udpmsg_)
{
}

ProtocolMessage* UDPMessage::clone()
{
	return new UDPMessage(*this);
}

int UDPMessage::size() const { return sizeof(RawUDPHeader); }

int UDPMessage::packingSize() const { return sizeof(RawUDPHeader); }

void UDPMessage::setSrcPort(uint16 src_port_){
	((RawUDPHeader*)rawptr)->setSrcPort(src_port_);
}

void UDPMessage::setDstPort(uint16 dst_port_){
	((RawUDPHeader*)rawptr)->setDstPort(dst_port_);
}

void UDPMessage::setLength(uint32 length_){
	((RawUDPHeader*)rawptr)->setLength(length_);
}

void UDPMessage::setCheckSum(uint32 checksum_){
	((RawUDPHeader*)rawptr)->setCheckSum(checksum_);
}

const uint16 UDPMessage::getSrcPort() const{
	return ((RawUDPHeader*)rawptr)->getSrcPort();
}

const uint16 UDPMessage::getDstPort() const{
	return ((RawUDPHeader*)rawptr)->getDstPort();
}

const uint32 UDPMessage::getLength() const{
	return ((RawUDPHeader*)rawptr)->getLength();
}

const uint32 UDPMessage::getCheckSum() const{
	return ((RawUDPHeader*)rawptr)->getCheckSum();
}

void UDPMessage::serialize(byte* buf, int& offset) {
	if(buf) {
		LOG_ERROR("THIS LOOKS WRONG!");
		if(rawptr) memcpy(&buf[offset], rawptr, getLength()-sizeof(RawUDPHeader));
		else LOG_ERROR("protocol message empty for serialization.\n");
		offset += getLength()-sizeof(RawUDPHeader);
	} else LOG_ERROR("empty serialization buffer.\n");
}

void UDPMessage::deserialize(byte* buf, int& offset) {
	if(buf) {
		LOG_ERROR("THIS LOOKS WRONG!");
		if(rawptr) LOG_ERROR("protocol message not empty for deserialization");
		rawptr = new byte[sizeof(RawUDPHeader)];
		memcpy(rawptr, &buf[offset], (getLength()-sizeof(RawUDPHeader)));
		offset +=  getLength()-sizeof(RawUDPHeader);
	} else LOG_ERROR("empty serialization buffer.\n");
}

void UDPMessage::toRawBytes(byte*& b, int& n){
	if(!pkt) LOG_ERROR("protocol message not packetized.");
	if(!rawptr) LOG_ERROR("protocol message empty.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	LOG_DEBUG("UDPMessage::toRawBytes n=" << n << "\n" << endl);

	RawProtocolMessage::toRawBytes(b, n); //will side effect b and n

	//Compute checksum
	//XXX
}

void UDPMessage::fromRawBytes(Packet* p, byte*& b, int& n){
	RawProtocolMessage::fromRawBytes(p, b, n);
	if(n > 0) {
		DataMessage* msg = new DataMessage();
		msg->fromRawBytes(pkt, b, n);
		link_payload(msg);
	}
}

SSFNET_REGISTER_MESSAGE(UDPMessage, SSFNET_PROTOCOL_TYPE_UDP);

} //namespace ssfnet
} //namespace prime
