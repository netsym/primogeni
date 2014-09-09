/*
 * \file udp_message.h
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
#ifndef __UDP_MESSAGE_H__
#define __UDP_MESSAGE_H__

#include "os/ssfnet.h"
#include "os/protocol_message.h"
#include "os/data_message.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

class RawUDPHeader {
 private:
	// Source port number
	uint16 srcPort;
	// Destination port number
	uint16 dstPort;
	// Specifies the length in bytes of the entire datagram: header and data
	uint16 length;
	// Chechsum
	uint16 checksum;
 public:
	void setSrcPort(uint16 src_port_) { srcPort = myhtons(src_port_); }
	void setDstPort(uint16 dst_port_) { dstPort = myhtons(dst_port_); }
	void setLength(uint16 length_) { length = myhtons(length_); }
	void setCheckSum(uint16 checksum_) { checksum = checksum_; }

	const uint16 getSrcPort() const { return myntohs(srcPort); }
	const uint16 getDstPort() const { return myntohs(dstPort); }
	const uint16 getLength() const { return myntohs(length); }
	const uint16 getCheckSum() const { return checksum; }
};

class UDPMessage: public RawProtocolMessage {
 public:
	UDPMessage();

	UDPMessage(uint16 src_port_, uint16 dst_port_, uint16 length_ = 8,
			uint16 checksum_ = 0);

	UDPMessage(UDPMessage const & udpmsg_);

	//virtual methods
	virtual ProtocolMessage* clone();
	virtual int size() const;
	virtual void toRawBytes(byte*& b, int& n);
	virtual void fromRawBytes(Packet* p, byte*& b, int& n);
	virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_UDP; }

	virtual int packingSize() const;
	virtual void serialize(byte* buf, int& offset);
	virtual void deserialize(byte* buf, int& offset);

	const uint16 getSrcPort() const;
	const uint16 getDstPort() const;
	const uint32 getLength() const;
	const uint32 getCheckSum() const;

	void setSrcPort(uint16 src_port_);
	void setDstPort(uint16 dst_port_);
	void setLength(uint32 length_);
	void setCheckSum(uint32 checksum_);
};

} //namespace prime
} //namespace ssfnet
#endif
