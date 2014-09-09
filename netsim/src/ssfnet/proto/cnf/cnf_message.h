/**
 * \file cnf_message.h
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
#ifndef __CNF_MESSAGE_H__
#define __CNF_MESSAGE_H__

#include "os/ssfnet.h"
#include "os/protocol_message.h"
#include "os/data_message.h"
#include "os/ssfnet_types.h"
#include "net/interface.h"

namespace prime {
namespace ssfnet {

class CNFMessage: public ProtocolMessage {
public:
	CNFMessage(): ProtocolMessage(), src(0),dst(0),dst_port(0),cnf_node(0),cid(0), c_size(0), evict(0){}

	CNFMessage(UID_t src, UID_t dst, UID_t dst_port, UID_t cnf_node, int cid, int c_size, int evict=0);

	CNFMessage(CNFMessage const & cnfmsg_);

	//virtual methods
	virtual ProtocolMessage* clone();
	virtual int size() const;
	virtual void toRawBytes(byte*& b, int& n);
	virtual void fromRawBytes(Packet* p, byte*& b, int& n);
	virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_CNF_TRANS; }

	virtual int packingSize() const;
	virtual void serialize(byte* buf, int& offset);
	virtual void deserialize(byte* buf, int& offset);
	virtual int pack(prime::ssf::ssf_compact* dp);
	virtual int unpack(prime::ssf::ssf_compact* dp);

	void setSrc(UID_t src_) { src=src_; }
	void setDst(UID_t dst_) { dst=dst_; }
	void setDstPort(UID_t d_port_) {dst_port=d_port_;}
	void setCNFNode(UID_t cnf_node_) {cnf_node=cnf_node_;}
	void setContentID(int cid_) {cid=(UID_t)cid_;}
	void setContentSize(int c_size_) {c_size=(UID_t)c_size_;}

	const UID_t getSrc() const { return src; }
	const UID_t getDst() const { return dst; }
	const UID_t getDstPort() const { return dst_port;}
	const UID_t getCNFNode() const {return cnf_node;}
	const int getContentID() const {return (int)cid;}
	const int getContentSize() const {return (int)c_size;}
	const int getEvict() const { return (int)evict;}
private:
	// Orignal source
	UID_t src;
	// Final destination
	UID_t dst;
	// Dst port
	UID_t dst_port;
	// CNF node UID
	UID_t cnf_node;
	// Content ID
	UID_t cid;
	// Content size
	UID_t c_size;
	//if there is to be an eviction
	UID_t evict;
};

} //namespace prime
} //namespace ssfnet
#endif
