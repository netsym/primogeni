/**
 * \file protocol_message.cc
 * \brief Source file for the ProtocolMessage class.
 * \author Nathanael Van Vorst
 * 
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

#include <string.h>
#include <assert.h>
#include "os/logger.h"
#include "os/protocol_message.h"
#include "os/packet.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(ProtocolMessage);

SSFNET_INT2PTR_MAP* ProtocolMessage::registered_messages = 0;

ProtocolMessage::ProtocolMessage() :
		  prev(0), next(0), pkt(0), rawptr(0) {}

ProtocolMessage::ProtocolMessage(const ProtocolMessage& pmsg) :
		  prev(0), pkt(0)
{
	if(pmsg.rawptr) { // makes a stand-alone message
		int sz = pmsg.size();
		rawptr = new byte[sz];
		memcpy(rawptr, pmsg.rawptr, sz);
	} else rawptr = 0;
	if(pmsg.next) { next = pmsg.next->clone(); next->prev = this; }
	else next = 0;
}

ProtocolMessage* ProtocolMessage::reuse()
{
	if(!pkt) return this;
	Packet* newpkt = pkt->reuse();
	if(pkt == newpkt) return this;
	else return newpkt->getMessageByImplementationType(implementationType());
}

void ProtocolMessage::erase()
{
	if(pkt) pkt->erase();
	else {
		if(next) next->erase();
		delete this;
	}
}

ProtocolMessage::~ProtocolMessage()
{
	assert(!pkt);
	if(rawptr) delete[] rawptr;
}

bool ProtocolMessage::isStandalone()
{
	return !(pkt && pkt->getRawBuffer());
}

ProtocolMessage* ProtocolMessage::getMessageByArchetype(int t)
{
	ProtocolMessage* mymsg = this;
	while(mymsg) {
		if(mymsg->archetype() == t) return mymsg;
		mymsg = mymsg->next;
	}
	return 0;
}

ProtocolMessage* ProtocolMessage::getMessageByImplementationType(int t)
{
	ProtocolMessage* mymsg = this;
	while(mymsg) {
		if(mymsg->implementationType() == t) return mymsg;
		mymsg = mymsg->next;
	}
	return 0;
}

void ProtocolMessage::carryPayload(ProtocolMessage* msg)
{
	assert(msg);
	if(!isStandalone())
		LOG_ERROR("only standalone protocol message can carry payload.");
	if(msg->pkt)
		LOG_ERROR("only non-packetized protocol message can be used as payload.");
	if(next) LOG_ERROR("existing protocol message payload.");
	if(msg->prev) LOG_ERROR("existing protocol message carrier.");
	link_payload(msg);
	if(pkt) {
		while(msg) {
			assert(!msg->pkt);
			msg->pkt = pkt;
			msg = msg->next;
		}
	}
}

void ProtocolMessage::link_payload(ProtocolMessage* msg)
{
	next = msg; msg->prev = this;
}

ProtocolMessage* ProtocolMessage::dropPayload()
{
	if(!isStandalone())
		LOG_ERROR("only standalone protocol message can drop payload.");
	ProtocolMessage* msg = next;
	if(pkt) {
		while(msg) {
			assert(msg->pkt);
			msg->pkt = 0;
			msg = msg->next;
		}
	}
	return unlink_payload();
}

ProtocolMessage* ProtocolMessage::unlink_payload()
{
	ProtocolMessage* msg = next;
	next = 0;
	msg->prev = 0;
	return msg;
}

int ProtocolMessage::registerMessage(ProtocolMessage* (*fct)(), int itype)
{
	// allocate the global data structure if it has not been
	if(!registered_messages) registered_messages = new SSFNET_INT2PTR_MAP;

	// insert the mapping; check if duplicated
	if(!registered_messages->insert(SSFNET_MAKE_PAIR(itype, (void*)fct)).second)
		LOG_ERROR("duplicate protocol message (impl_type=" << itype << ").");

	// we need to return something so that SSFNET_REGISTER_MESSAGE works
	return 0;
}

ProtocolMessage* ProtocolMessage::newMessage(int itype)
{
	// find the constructor of the given implementation type; create and
	// return a new instance
	if(!registered_messages) return 0;
	SSFNET_INT2PTR_MAP::iterator iter = registered_messages->find(itype);
	if(iter != registered_messages->end()) {
		void* fct = (*iter).second;
		return (*(ProtocolMessage*(*)())fct)(); // call factory method
	} else return 0;
}

void RawProtocolMessage::serialize(byte* buf, int& offset)
{
	if(buf) {
		int sz = size();
		if(rawptr) memcpy(&buf[offset], rawptr, sz);
		else LOG_ERROR("protocol message empty for serialization.");
		//LOG_DEBUG("serialize raw: size="<<sz<<", first byte=0x"<<std::hex<<(uint32)*rawptr<<endl);
		offset += sz;
	} else LOG_ERROR("empty serialization buffer.");
}

int RawProtocolMessage::pack(prime::ssf::ssf_compact* dp) {
	if(rawptr){
		int sz = size();
		dp->add_int(sz);
		dp->add_char_array(sz,(char*)rawptr);
		return sz+sizeof(int);
	}
	else {
		dp->add_int(0);
		return sizeof(int);
	}
}


void RawProtocolMessage::deserialize(byte* buf, int& offset)
{
	if(buf) {
		if(rawptr) LOG_ERROR("protocol message not empty for deserialization");
		rawptr = &buf[offset]; // temporary so that size() can return meaningful value
		int sz = size();
		rawptr = new byte[sz];
		memcpy(rawptr, &buf[offset], sz);
		offset += sz;
		//LOG_DEBUG("deserialize raw: size="<<sz<<", first byte=0x"<<std::hex<<(uint32)*rawptr<<endl);
	} else LOG_ERROR("empty serialization buffer.");
}

int RawProtocolMessage::unpack(prime::ssf::ssf_compact* dp) {
	int sz;
	dp->get_int(&sz);
	if(sz>0) {
		rawptr = new byte[sz];
		dp->get_char((char*)rawptr,sz);
		return sz+sizeof(int);
	}
	else {
		rawptr=0;
		return sizeof(int);
	}
}


void RawProtocolMessage::fromRawBytes(Packet* p, byte*& b, int& n)
{
	if(pkt) LOG_ERROR("protocol message already packetized.");
	if(rawptr) LOG_ERROR("protocol message not empty.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	pkt = p;
	if(!isStandalone()) {
		rawptr = b; // no copying here!!!
		int sz = size();
		b += sz;
		n -= sz;
	} else {
		rawptr = b; // temporary so that size() can return meaningful value
		int sz = size();
		rawptr = new byte[sz];
		memcpy(rawptr, b, sz);
		b += sz;
		n -= sz;
	}
	if(n < 0) LOG_ERROR("ill-formed real packet content. [n<"<<n<<"<0]\n");
}

void RawProtocolMessage::toRawBytes(byte*& b, int& n)
{
	if(!pkt) LOG_ERROR("protocol message not packetized.");
	if(!rawptr) LOG_ERROR("protocol message empty.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	int sz = size();
	//need to check that there is room before we do the copy. if we copy without room we will cause a memory corruption!
	if(n<sz) LOG_ERROR("There is no room in the buffer to store this protocol message. #bytes="<<n<<" require "<<sz);

	if(isStandalone() || b != rawptr) {
		//LOG_DEBUG("is standalone! i.e copying!"<<endl)
		memcpy(b, rawptr, sz);
	}

	b += sz;
	n -= sz;
	if(next) next->toRawBytes(b, n);
}

} // namespace ssfnet
} // namespace prime
