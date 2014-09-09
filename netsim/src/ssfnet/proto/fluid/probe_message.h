/**
 * \file probe_message.h
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
#ifndef __PROBE_MESSAGE_H__
#define __PROBE_MESSAGE_H__

#include "os/ssfnet.h"
#include "os/protocol_message.h"
#include "os/data_message.h"
#include "os/ssfnet_types.h"
#include "net/interface.h"

namespace prime {
namespace ssfnet {

class ProbeMessage: public ProtocolMessage {
public:
	ProbeMessage(): ProtocolMessage(),src(0),dst(0){}

	ProbeMessage(UID_t src, UID_t dst);

	ProbeMessage(ProbeMessage const & probemsg_);

	//virtual methods
	virtual ProtocolMessage* clone();
	virtual int size() const;
	virtual void toRawBytes(byte*& b, int& n);
	virtual void fromRawBytes(Packet* p, byte*& b, int& n);
	virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_PROBE; }

	virtual int packingSize() const;
	virtual void serialize(byte* buf, int& offset);
	virtual void deserialize(byte* buf, int& offset);
	virtual int pack(prime::ssf::ssf_compact* dp);
	virtual int unpack(prime::ssf::ssf_compact* dp);


	void setSrc(UID_t src_) { src=src_; }
	void setDst(UID_t dst_) { dst=dst_; }
	void addNics(UID_t nic) { nics.push_back(nic);}
	void addNodeUIDs(UID_t uid) {node_uids.push_back(uid);}
	void addBusIdxes(UID_t idx) {bus_idxes.push_back(idx);}

	const UID_t getSrc() const { return src; }
	const UID_t getDst() const { return dst; }
	const UIDVec getNics() const { return nics; }
	const UIDVec getNodeUIDs() const { return node_uids; }
	const UIDVec getBusIdxes() const { return bus_idxes; }

private:
	// Orignal source
	UID_t src;
	// Final destination
	UID_t dst;
	// Outbound interfaces list
	UIDVec nics;
	// The uid list of the nodes on the path
	UIDVec node_uids;
	// The bus index
	UIDVec bus_idxes;
};

} //namespace prime
} //namespace ssfnet
#endif
