/**
 * \file cnf_message.cc
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
#include "cnf_message.h"
#include "os/logger.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(CNFMessage);

CNFMessage::CNFMessage(UID_t src_, UID_t dst_, UID_t d_port_, UID_t cnf_node_, int cid_, int c_size_, int evict_): ProtocolMessage()
, src(src_),dst(dst_),dst_port(d_port_), cnf_node(cnf_node_),cid((UID_t)cid_), c_size((UID_t)c_size_),evict(evict_)
{
	LOG_DEBUG("creating a CNF message with src=" << src_ << ", dst=" << dst_ << endl);
}

CNFMessage::CNFMessage(CNFMessage const & cnfmsg_) : ProtocolMessage(cnfmsg_)
, src(cnfmsg_.src),dst(cnfmsg_.dst),dst_port(cnfmsg_.dst_port), cnf_node(cnfmsg_.cnf_node),cid(cnfmsg_.cid), c_size(cnfmsg_.c_size),evict(cnfmsg_.evict)
{}

ProtocolMessage* CNFMessage::clone()
{
	return new CNFMessage(*this);
}

int CNFMessage::size() const { return packingSize(); }

int CNFMessage::packingSize() const {
	return sizeof(UID_t)*7;
}

void CNFMessage::serialize(byte* buf, int& offset) {
	UID_t t[7];
	t[0]=src;
	t[1]=dst;
	t[2]=dst_port;
	t[3]=cnf_node;
	t[4]=cid;
	t[5]=c_size;
	t[6]=evict;
	memcpy(buf+offset,t,size());
	offset+=size();
}

void CNFMessage::deserialize(byte* buf, int& offset) {
	UID_t* t = (UID_t*)buf;
	src=t[0];
	dst=t[1];
	dst_port=t[2];
	cnf_node=t[3];
	cid=t[4];
	c_size=t[5];
	evict=t[6];
	offset+=size();
}

int CNFMessage::pack(prime::ssf::ssf_compact* dp) {
	dp->add_unsigned_long_long(src);
	dp->add_unsigned_long_long(dst);
	dp->add_unsigned_long_long(dst_port);
	dp->add_unsigned_long_long(cnf_node);
	dp->add_unsigned_long_long(cid);
	dp->add_unsigned_long_long(c_size);
	dp->add_unsigned_long_long(evict);
	return sizeof(UID_t)*6;
}

int CNFMessage::unpack(prime::ssf::ssf_compact* dp) {
	//do ~pack
	dp->get_unsigned_long_long(&src);
	dp->get_unsigned_long_long(&dst);
	dp->get_unsigned_long_long(&dst_port);
	dp->get_unsigned_long_long(&cnf_node);
	dp->get_unsigned_long_long(&cid);
	dp->get_unsigned_long_long(&c_size);
	dp->get_unsigned_long_long(&evict);
	return sizeof(UID_t)*6;
}

void CNFMessage::toRawBytes(byte*& b, int& n){
	if(!pkt) LOG_ERROR("protocol message not packetized.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	int sz = size();
	//need to check that there is room before we do the copy. if we copy without room we will cause a memory corruption!
	if(n<sz) LOG_ERROR("There is no room in the buffer to store this protocol message. #bytes="<<n<<" require "<<sz);

	if(isStandalone() || b != rawptr) {
		//LOG_DEBUG("is stand alone! i.e copying!"<<endl)
		int offset=0;
		serialize(b,offset);
		if(offset != size()) {
			LOG_ERROR("The amount copied did not match size()!\n");
		}
	}
	b += sz;
	n -= sz;
	if(next) next->toRawBytes(b, n);
}

void CNFMessage::fromRawBytes(Packet* p, byte*& b, int& n){
	if(pkt) LOG_ERROR("protocol message already packetized.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	pkt = p;
	int offset=0;
	deserialize(b,offset);
	b += offset;
	n -= offset;
	if(n < 0) LOG_ERROR("ill-formed real packet content. [n<"<<n<<"<0]\n");
}

SSFNET_REGISTER_MESSAGE(CNFMessage, SSFNET_PROTOCOL_TYPE_CNF_TRANS);

} //namespace ssfnet
} //namespace prime
