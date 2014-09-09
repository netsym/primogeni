/**
 * \file probe_message.cc
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
#include "probe_message.h"
#include "os/logger.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(ProbeMessage);

ProbeMessage::ProbeMessage(UID_t src_, UID_t dst_): ProtocolMessage()
{
	LOG_DEBUG("creating a probe message with src=" << src_ << ", dst=" << dst_ << endl);
	setSrc(src_);
	setDst(dst_);
}

ProbeMessage::ProbeMessage(ProbeMessage const & probemsg_) : ProtocolMessage(probemsg_){}

ProtocolMessage* ProbeMessage::clone()
{
	return new ProbeMessage(*this);
}

int ProbeMessage::size() const { return packingSize(); }

int ProbeMessage::packingSize() const {
	return sizeof(UID_t)*(5+nics.size()+node_uids.size()+bus_idxes.size());//src,dst,nic.size()+the nics
}

void ProbeMessage::serialize(byte* buf, int& offset) {
	//XXX
	//this does not support mixed 32/64 clusters!
	UID_t* t = new UID_t[size()];
	t[0]=src;
	t[1]=dst;
	t[2]=nics.size();
	int i=0;
	for(UIDVec::iterator it=nics.begin();it!=nics.end();it++) {
		t[i+3]=*it;
		i++;
	}
	t[i+3]=node_uids.size();
	i++;
	for(UIDVec::iterator it=node_uids.begin();it!=node_uids.end();it++) {
		t[i+3]=*it;
		i++;
	}
	t[i+3]=bus_idxes.size();
	i++;
	for(UIDVec::iterator it=bus_idxes.begin();it!=bus_idxes.end();it++) {
		t[i+3]=*it;
		i++;
	}
	memcpy(buf+offset,t,size());
	offset+=size();
	delete[] t;
}

void ProbeMessage::deserialize(byte* buf, int& offset) {
	//XXX
	//this does not support mixed 32/64 clusters!
	UID_t* t = (UID_t*)buf;
	src=t[0];
	dst=t[1];
	int i=0;
	for(i = 0; i<(int)t[2];i++) {
		nics.push_back(t[i+3]);
	}
	int j=0;
	for(j = 0; j<(int)t[i+3];j++) {
		node_uids.push_back(t[i+4+j]);
	}
	for(int k = 0; k<(int)t[i+j+5];k++) {
		node_uids.push_back(t[i+j+6+k]);
	}
	offset+=size();
}

int ProbeMessage::pack(prime::ssf::ssf_compact* dp) {
	//encode src,dst,nics.size(),then each nic
	dp->add_unsigned_long_long(src);
	dp->add_unsigned_long_long(dst);
	UID_t t = nics.size();
	dp->add_unsigned_long_long(t);
	for(UIDVec::iterator it=nics.begin();it!=nics.end();it++) {
		dp->add_unsigned_long_long(*it);
	}
	t = node_uids.size();
	dp->add_unsigned_long_long(t);
	for(UIDVec::iterator it=node_uids.begin();it!=node_uids.end();it++) {
		dp->add_unsigned_long_long(*it);
	}
	t = bus_idxes.size();
	dp->add_unsigned_long_long(t);
	for(UIDVec::iterator it=bus_idxes.begin();it!=bus_idxes.end();it++) {
		dp->add_unsigned_long_long(*it);
	}
	return sizeof(UID_t)*(5+nics.size()+node_uids.size()+bus_idxes.size());
}

int ProbeMessage::unpack(prime::ssf::ssf_compact* dp) {
	//do ~pack
	dp->get_unsigned_long_long(&src);
	dp->get_unsigned_long_long(&dst);
	UID_t s=0,t=0;
	dp->get_unsigned_long_long(&s);
	for(int i=0;i<(int)s;i++){
		dp->get_unsigned_long_long(&t);
		nics.push_back(t);
	}
	s=0,t=0;
	dp->get_unsigned_long_long(&s);
	for(int i=0;i<(int)s;i++){
		dp->get_unsigned_long_long(&t);
		node_uids.push_back(t);
	}
	s=0,t=0;
	dp->get_unsigned_long_long(&s);
	for(int i=0;i<(int)s;i++){
		dp->get_unsigned_long_long(&t);
		bus_idxes.push_back(t);
	}
	return sizeof(UID_t)*(5+nics.size()+node_uids.size()+bus_idxes.size());
}

void ProbeMessage::toRawBytes(byte*& b, int& n){
	if(!pkt) LOG_ERROR("protocol message not packetized.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	int sz = size();
	//need to check that there is room before we do the copy. if we copy without room we will cause a memory corruption!
	if(n<sz) LOG_ERROR("There is no room in the buffer to store this protocol message. #bytes="<<n<<" require "<<sz);

	if(isStandalone() || b != rawptr) {
		//LOG_DEBUG("is standalone! i.e copying!"<<endl)
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

void ProbeMessage::fromRawBytes(Packet* p, byte*& b, int& n){
	if(pkt) LOG_ERROR("protocol message already packetized.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	pkt = p;
	int offset=0;
	deserialize(b,offset);
	b += offset;
	n -= offset;
	if(n < 0) LOG_ERROR("ill-formed real packet content. [n<"<<n<<"<0]\n");
}

SSFNET_REGISTER_MESSAGE(ProbeMessage, SSFNET_PROTOCOL_TYPE_PROBE);

} //namespace ssfnet
} //namespace prime
