/**
 * \file packet.cc
 * \brief Source file for the Packet class.
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

#include <assert.h>
#include <string.h>
#include "ssf.h"
#include "os/logger.h"
#include "os/protocol_message.h"
#include "os/packet.h"
#include "os/nix_vector.h"
#include "proto/ipv4/ipv4_message.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(Packet);

Packet::Packet() :
		  refcnt(1), src(MACAddress::MACADDR_INVALID), dst(MACAddress::MACADDR_INVALID),
		  prototype(0), protomsg(0), rawbuf(0), rawbfsz(0), rawrsv(0), nixvector(0), dst_uid(0), prev_rs_uid(0), local_dst_uid(0) {
#if TEST_ROUTING == 1
  route_debug = new RouteDebug();
#endif

}

Packet::Packet(ProtocolMessage* msg,UID_t _dst_uid) :
		  refcnt(1), src(MACAddress::MACADDR_INVALID), dst(MACAddress::MACADDR_INVALID), protomsg(msg),
		  rawbuf(0), rawbfsz(0), rawrsv(0), nixvector(0), dst_uid(_dst_uid), prev_rs_uid(0), local_dst_uid(0)
{
	assert(protomsg);
	prototype = protomsg->archetype();
	ProtocolMessage* mymsg = protomsg;
	while(mymsg) {
		assert(!mymsg->pkt);
		mymsg->pkt = this;
		mymsg = mymsg->payload();
	}
#if TEST_ROUTING == 1
  route_debug = new RouteDebug();
#endif
}

Packet::Packet(MACAddress s, MACAddress d, ProtocolMessage* msg,UID_t _dst_uid) :
		  refcnt(1), src(s), dst(d), protomsg(msg),
		  rawbuf(0), rawbfsz(0), rawrsv(0), nixvector(0), dst_uid(_dst_uid), prev_rs_uid(0), local_dst_uid(0)
{
	assert(protomsg);
	prototype = protomsg->archetype();
	ProtocolMessage* mymsg = protomsg;
	while(mymsg) {
		assert(!mymsg->pkt);
		mymsg->pkt = this;
		mymsg = mymsg->payload();
	}
#if TEST_ROUTING == 1
  route_debug = new RouteDebug();
#endif
}

Packet::Packet(int t, byte* pktbuf, int pktsiz, int reserved,UID_t _dst_uid) :
		  refcnt(1), src(MACAddress::MACADDR_INVALID), dst(MACAddress::MACADDR_INVALID), prototype(t),
		  rawbuf(pktbuf), rawbfsz(pktsiz), rawrsv(reserved), nixvector(0), dst_uid(_dst_uid), prev_rs_uid(0), local_dst_uid(0)
{
	//LOG_DEBUG("Creating packet, rawbfsz="<<rawbfsz<<", rawrsv="<<rawrsv<<endl)
	assert(rawbfsz > rawrsv);
	assert(rawrsv >= 0);
	assert(rawbuf);
	protomsg = ProtocolMessage::newMessage(prototype);
	if(!protomsg) LOG_ERROR("protocol type (" << prototype << ") not found.");
	byte* b = &rawbuf[rawrsv];
	int n = rawbfsz-rawrsv;
	protomsg->fromRawBytes(this, b, n);
	if(n) LOG_ERROR("ill-formed packet raw content. There are "<<n<<" bytes left after parsing!"<<endl);
#if TEST_ROUTING == 1
  route_debug = new RouteDebug();
#endif
}

Packet::Packet(MACAddress s, MACAddress d, int t, byte* pktbuf, int pktsiz, int reserved,UID_t _dst_uid) :
		  refcnt(1), src(s), dst(d), prototype(t),
		  rawbuf(pktbuf), rawbfsz(pktsiz), rawrsv(reserved), nixvector(0), dst_uid(_dst_uid), prev_rs_uid(0), local_dst_uid(0)
{
	//LOG_DEBUG("Creating packet, rawbfsz="<<rawbfsz<<", rawrsv="<<rawrsv<<endl)
	assert(rawbfsz > rawrsv);
	assert(rawrsv >= 0);
	assert(rawbuf);
	protomsg = ProtocolMessage::newMessage(prototype);
	if(!protomsg) LOG_ERROR("protocol type (" << prototype << ") not found.");
	byte* b = &rawbuf[rawrsv];
	int n = rawbfsz-rawrsv;
	protomsg->fromRawBytes(this, b, n);
	if(n) LOG_ERROR("ill-formed packet raw content. There are "<<n<<" bytes left after parsing!"<<endl);
#if TEST_ROUTING == 1
  route_debug = new RouteDebug();
#endif
}

Packet::Packet(const Packet& pkt) :
		  refcnt(1), src(MACAddress::MACADDR_INVALID), dst(MACAddress::MACADDR_INVALID), prototype(pkt.prototype),
		  rawbfsz(pkt.rawbfsz), rawrsv(pkt.rawrsv), nixvector(0), dst_uid(pkt.dst_uid), prev_rs_uid(0), local_dst_uid(0)
{
	//LOG_DEBUG("Copying packet, rawbfsz="<<rawbfsz<<", rawrsv="<<rawrsv<<endl)
	if(pkt.rawbuf) { // this packet contains raw content in a contiguous buffer
		assert(rawbfsz > 0);
		rawbuf = new byte[rawbfsz];
		memcpy(rawbuf, pkt.rawbuf, rawbfsz);
		protomsg = ProtocolMessage::newMessage(prototype);
		if(!protomsg) LOG_ERROR("protocol type (" << prototype << ") not found.");
		byte* b = &rawbuf[rawrsv];
		int n = rawbfsz-rawrsv;
		protomsg->fromRawBytes(this, b, n);
		if(n) LOG_ERROR("ill-formed packet raw content.");
	} else { // this is simulation packet
		rawbuf=0;
		assert(!rawbfsz);
		assert(!rawrsv);
		assert(pkt.protomsg);
		protomsg = pkt.protomsg->clone();
		ProtocolMessage* mymsg = protomsg;
		while(mymsg) {
			assert(!mymsg->pkt);
			mymsg->pkt = this;
			mymsg = mymsg->payload();
		}
	}
#if TEST_ROUTING == 1
  route_debug = new RouteDebug();
#endif
}

Packet::~Packet()
{
	if(refcnt > 1)
		LOG_ERROR("attempt to reclaim a shared packet (refcnt=" << refcnt << ").");

	ProtocolMessage* msg = protomsg;
	while(msg) {
		ProtocolMessage* nxt = msg->payload();
		assert(msg->pkt == this);
		if(rawbuf && msg->rawptr) msg->rawptr = 0;
		msg->pkt=0;
		delete msg;
		msg = nxt;
	}
	if(rawbuf) delete[] rawbuf;
	if(nixvector)
		delete nixvector;
#if TEST_ROUTING == 1
	delete route_debug;
#endif
}

Packet* Packet::reuse()
{
	if(refcnt == 1) {
		//we need to reinit these vars
		setFinalDestinationUID(0);
		setLocalDestinationUID(0);
		setPrevRoutingSphereUID(0);
		setSrc(MACAddress::MACADDR_INVALID);
		setDst(MACAddress::MACADDR_INVALID);
		if(nixvector)
			delete dropNixVector(); //we need to know this is a new pkt being routed
		return this;
	}
	else {
		assert(refcnt > 1);
		refcnt--;
		return clone(); // this resets the nixvector and src/dst like above
	}
#if TEST_ROUTING == 1
	delete route_debug;
	route_debug = new RouteDebug();
#endif
}

void Packet::erase()
{
	refcnt--;
	if(refcnt <= 0) delete this;
}


/**
 *    Common packing header
 * ----------------
 * dst            | long long
 * ----------------
 * src            | long long
 * ----------------
 * dst_uid        | long long
 * ----------------
 * local_dst_uid  | long long
 * ----------------
 * prev_rs_uid    | long long
 * ----------------
 * have_nix       | short
 * ----------------
 * nix            | <variable>
 * ----------------
 * prototype      | int
 * ----------------
 * have_raw_buf   | short
 * ----------------
 * < have_raw_buf==0
 * 		then      A ,
 *      otherwise B >
 *
 *        A
 * ----------------
 * rawbfsz        | int
 * ----------------
 * rawrsv         | int
 * ----------------
 * raw_buf        | <variable>
 * ----------------
 *
 *        B
 * ----------------
 * pack size      | int
 * ----------------
 * first_proto_buf| <variable>
 * ----------------
 * next proto type| int
 * ----------------
 * next_proto_buf | <variable>
 * ----------------
 * next proto type| int
 * ----------------
 * next_proto_buf | <variable>
 * ----------------
 * ###############
 * ----------------
 * last proto type| int
 * ----------------
 * last_proto_buf | <variable>
 * ----------------
 */
void Packet::pack(prime::ssf::ssf_compact* dp)
{
	LOG_DEBUG("Packing packet dst_uid="<<dst_uid<<", local_dst_uid="<<local_dst_uid<<", src="<<src<<", dst="<<dst<<endl);
#if TEST_ROUTING == 1
	route_debug->pack(dp);
#endif

	dp->add_unsigned_long_long((uint64)dst);
	dp->add_unsigned_long_long((uint64)src);
	dp->add_unsigned_long_long(dst_uid);
	dp->add_unsigned_long_long(local_dst_uid);
	dp->add_unsigned_long_long(prev_rs_uid);
	if(nixvector) {
		LOG_DEBUG("packing nix vector,"<<nixvector<<endl);
		dp->add_unsigned_short(1);
		nixvector->pack(dp);
	}
	else dp->add_unsigned_short(0);

	//LOG_DEBUG("prototype="<<prototype<<endl);
	dp->add_int(prototype);

	if(rawbuf) {
		//LOG_DEBUG("Packing rawbuf, size="<<rawbfsz<<endl);
		assert(rawbfsz > 0);
		dp->add_unsigned_short(1); //mark we serialized rawbuf
		dp->add_int(rawbfsz);
		dp->add_int(rawrsv);
		dp->add_unsigned_char_array(rawbfsz, rawbuf);
	} else {
		assert(protomsg);

		dp->add_unsigned_short(0); //mark we did not serialize rawbuf

		int packsz = 0;
		ProtocolMessage* mymsg = protomsg;
		while(mymsg) {
			packsz += mymsg->packingSize()+sizeof(int);
			mymsg = mymsg->payload();
		}
		packsz-=sizeof(int); //we encoded the proto type of the first proto above....
		assert(packsz > (int)sizeof(int));
		//LOG_DEBUG("Packing proto chain, size="<<packsz<<endl);
		dp->add_int(packsz);

		mymsg = protomsg;
		while(mymsg) {
			if(mymsg !=  protomsg) { // skip the first protocol message's implementation type
				int32 temp = (int32)mymsg->implementationType();
				dp->add_int(temp);
				packsz-=sizeof(int);
			}
			packsz-=mymsg->pack(dp);
			mymsg = mymsg->payload();
		}
		assert(packsz == 0);
	}
}

void Packet::unpack(prime::ssf::ssf_compact* dp)
{
#if TEST_ROUTING == 1
	route_debug = new RouteDebug(dp);
#endif
	uint64 tl;
	dp->get_unsigned_long_long(&tl);
	dst=MACAddress(tl);
	dp->get_unsigned_long_long(&tl);
	src=MACAddress(tl);
	dp->get_unsigned_long_long(&dst_uid);
	dp->get_unsigned_long_long(&local_dst_uid);
	dp->get_unsigned_long_long(&prev_rs_uid);
	//LOG_DEBUG("Unpacking packet dst_uid="<<dst_uid<<", local_dst_uid="<<local_dst_uid<<", src="<<src<<", dst="<<dst<<endl);

	unsigned short have_nix=10, have_raw_buf=10;

	//do we have a nix?
	dp->get_unsigned_short(&have_nix);
	if(have_nix==1) {
		nixvector=new NixVector();
		nixvector->unpack(dp);
		//LOG_DEBUG("Unpacked nix vector,"<<nixvector<<endl);
	}
	else if(have_nix==0){
		nixvector=NULL;
	}
	else {
		LOG_ERROR("ACK! have_nix="<<have_nix<<endl);
	}

	dp->get_int(&prototype);
	//LOG_DEBUG("prototype="<<prototype<<endl);

	//did this have real bytes?
	dp->get_unsigned_short(&have_raw_buf);

	if(have_raw_buf==1) {
		// if this packet is an emulation packet, simply unpack the real
		// packet content
		dp->get_int(&rawbfsz);
		LOG_DEBUG("Unpacking rawbuf, size="<<rawbfsz<<endl);
		assert(rawbfsz > 0);
		rawbuf = new byte[rawbfsz];
		dp->get_int(&rawrsv);
		dp->get_unsigned_char(rawbuf, rawbfsz);

		protomsg = ProtocolMessage::newMessage(prototype);
		if(!protomsg) {
			LOG_ERROR("protocol type (" << prototype << ") not found.");
		}

		byte* b = &rawbuf[rawrsv];
		int n = rawbfsz-rawrsv;
		protomsg->fromRawBytes(this, b, n);
		if(n) {
			LOG_ERROR("ill-formed packet raw content.");
		}
	}
	else if(have_raw_buf==0){
		rawbuf = 0;

		// scan through the byte array and instantiate protocol messages
		// according to their implementation types
		ProtocolMessage* leadmsg = 0;
		ProtocolMessage* prevmsg = 0;
		int itype=prototype, packsz=0;

		// if this packet is a simulation packet, deserialize into the
		// list of protocol messages
		dp->get_int(&packsz);
		//LOG_DEBUG("Unpacking proto chain, size="<<packsz<<endl);
		assert(packsz>0);

		while(packsz>0) {
			if(leadmsg) { // skip the first protocol message's implementation type
				dp->get_int(&itype);
				packsz-=sizeof(int);
			}
			ProtocolMessage* msg = ProtocolMessage::newMessage(itype);
			if(!msg) LOG_ERROR("protocol implementation type " << itype << " not found."<<endl);
			packsz-=msg->unpack(dp);
			if(leadmsg) {
				prevmsg->carryPayload(msg);
				prevmsg = msg;
			}
			else{
				leadmsg = prevmsg = msg;
				leadmsg->pkt=this;
			}
		}
		assert(packsz==0);

		assert(leadmsg);
		protomsg = leadmsg;
	}
	else {
		LOG_ERROR("ACK! have_raw_buf=" << have_raw_buf <<endl);
	}
}

int Packet::packingSize() const
{
	int sum = 0;
	//dst_uid and local_dst_uid and src and dst and prev_routing_sphere uid
	sum+=sizeof(long long unsigned int)*5;
	//whether there is a nix vector and if there is a raw buf
	sum+=sizeof(unsigned short)*2;
	if(nixvector) {
		//the size of the nix-vector
		sum+=nixvector->packingSize();
	}
	//the extra int is used to store the protocol message's
	sum+=sizeof(int);
	if(rawbuf) {
		assert(rawbfsz > 0);
		sum+=sizeof(int)*2;
		sum+=rawbfsz;
	}
	else {
		assert(protomsg);
		ProtocolMessage* mymsg = protomsg;
		while(mymsg) {
			sum += mymsg->packingSize()+sizeof(int);
			mymsg = mymsg->payload();
		}
		sum-=sizeof(int); //we don't encode the first one's type
	}
	//LOG_DEBUG("pkt="<<((void*)this)<<", PACKING SIZE="<<sum<<endl);
#if TEST_ROUTING == 1
	return sum+route_debug->packingSize();
#else
	return sum;
#endif
}


int Packet::size() const
{
	assert(protomsg);
	int sum = 0;
	ProtocolMessage* mymsg = protomsg;
	while(mymsg) {
		assert(mymsg->pkt==this);
		sum += mymsg->size();
		mymsg = mymsg->payload();
	}
	return sum;
}

ProtocolMessage* Packet::getMessageByArchetype(int atype)
{
	if(protomsg) return protomsg->getMessageByArchetype(atype);
	else return 0;
}

ProtocolMessage* Packet::getMessageByImplementationType(int itype)
{
	if(protomsg) return protomsg->getMessageByImplementationType(itype);
	else return 0;
}

NixVector* Packet::getNixVector() {
	return nixvector;
}

NixVector* Packet::setNixVector(NixVector* nix) {
	if(nixvector) {
		ssfnet_throw_exception(SSFNetException::duplicate_nix_vector);
	}
	nixvector=nix;
	return nix;
}

NixVector* Packet::dropNixVector() {
	NixVector* nix=nixvector;
	nixvector=NULL;
	return nix;
}

UID_t Packet::getFinalDestinationUID() {
	return dst_uid;
}

void Packet::setFinalDestinationUID(UID_t new_dst_uid) {
	dst_uid=new_dst_uid;
}

UID_t Packet::getPrevRoutingSphereUID() {
	return prev_rs_uid;
}

void Packet::setPrevRoutingSphereUID(UID_t rs_uid) {
	prev_rs_uid=rs_uid;
}

UID_t Packet::getLocalDestinationUID() {
	return local_dst_uid;

}

void Packet::setLocalDestinationUID(UID_t new_dst_rank) {
	local_dst_uid=new_dst_rank;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, Packet& pkt) {
	os<<"[src="<<pkt.getSrc();
	os<<",dst="<<pkt.getDst();
	os<<",fdst="<<pkt.getFinalDestinationUID();
	return os<<",lrank="<<pkt.getLocalDestinationUID()<<"]";
}
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, Packet* pkt) {
	if(pkt)
		return os<<*pkt;
	return os<<"NULL";
}



} // namespace ssfnet
} // namespace prime
