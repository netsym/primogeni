/**
 * \file data_message.cc
 * \brief Implementation file for the DataMessage (and DataChunk) class.
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

#include "os/data_message.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(DataMessage);

DataMessage::DataMessage():ProtocolMessage(),real_length(0) {
}

DataMessage::DataMessage(const DataMessage& msg): ProtocolMessage(msg), real_length(msg.real_length)  {
}

DataMessage::DataMessage(int _real_length, byte* payload):ProtocolMessage(),real_length(_real_length) {
	rawptr = payload;
}

DataMessage::~DataMessage() {
}

ProtocolMessage* DataMessage::clone() {
	return new DataMessage(*this);
}

int DataMessage::size() const {
	return real_length;
}

void DataMessage::serialize(byte* buf, int& offset) {
	//whether this is real or not
	int real=(rawptr?1:0);
	memcpy(buf+offset, &real_length, sizeof(int));
	offset+=sizeof(int);

	//the length
	memcpy(buf+offset, &real_length, sizeof(int));
	offset+=sizeof(int);

	//the data
	if(rawptr) {
		memcpy(buf+offset, rawptr, real_length);
		offset+=real_length;
	}
}
int DataMessage::pack(prime::ssf::ssf_compact* dp) {
	dp->add_int(real_length);
	if(rawptr) {
		dp->add_int(1);
		dp->add_char_array(real_length,(char*)rawptr);
		return sizeof(int)*2+real_length;
	}
	else {
		dp->add_int(0);
		return sizeof(int)*2;
	}
}

void DataMessage::deserialize(byte* buf, int& offset) {
	int isReal = ((int*)(buf+offset))[0];
	buf+=sizeof(int);

	real_length = ((int*)(buf+offset))[0];
	buf+=sizeof(int);

	if(isReal) {
		rawptr = new byte[real_length];
		memcpy(rawptr, buf, real_length);
	}
}

int DataMessage::unpack(prime::ssf::ssf_compact* dp) {
	dp->get_int(&real_length);
	int temp = 10;
	dp->get_int(&temp);
	if(temp == 1) {
		rawptr = new byte[real_length];
		dp->get_char((char*)rawptr,real_length);
		return sizeof(int)*2+real_length;
	}
	else if(temp ==0 ) {
		rawptr=0;
	}
	else {
		LOG_ERROR("ACK!");
	}
	return sizeof(int)*2;
}


void DataMessage::fromRawBytes(Packet* rawpkt, byte*& rawbytes, int& nbytes) {
	if(pkt) LOG_ERROR("protocol message already packetized.");
	if(rawptr) LOG_ERROR("protocol message not empty.");
	if(!rawbytes) LOG_ERROR("empty raw content buffer.");

	pkt=rawpkt;
	real_length=nbytes;
	if(!isStandalone()) {
		//LOG_DEBUG("NO copying, real_length="<< size()<<endl);
		rawptr = rawbytes; // no copying here!!!
	} else {
		//LOG_DEBUG("Copying, real_length="<< size()<<endl);
		rawptr = new byte[real_length];
		memcpy(rawptr, rawbytes, real_length);
	}
	nbytes-=real_length;
	//LOG_DEBUG("nbytes="<< nbytes<<endl);
}

void DataMessage::toRawBytes(byte*& rawbytes, int& nbytes) {
	if(!pkt) LOG_ERROR("protocol message not packetized.");
	if(!rawbytes) LOG_ERROR("empty raw content buffer.");

	//need to check that there is room before we do the copy. if we copy without room we will cause a memory corruption!
	if(nbytes<real_length)
		LOG_ERROR("There is no room in the buffer to store this protocol message. #bytes="<<nbytes<<" require "<<real_length);
	if(isStandalone() || rawbytes != rawptr) {
		if(rawptr) {
			memcpy(rawbytes, rawptr, real_length);
		}
		else {
			memset(rawbytes, 1, real_length);
		}
	}
	rawbytes+=real_length;
	nbytes-=real_length;
	if(next) next->toRawBytes(rawbytes, nbytes);
}

SSFNET_REGISTER_MESSAGE(DataMessage,SSFNET_PROTOCOL_TYPE_OPAQUE_DATA);
}; // namespace ssfnet
}; // namespace prime
