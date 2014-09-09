/**
 * \file data_message.h
 * \brief Header file for the DataMessage class.
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

#ifndef __DATA_MESSAGE_H__
#define __DATA_MESSAGE_H__

#include "os/ssfnet.h"
#include "os/protocol_message.h"
#include <stdio.h>
#include <string.h>

namespace prime {
namespace ssfnet {

/**
 * \brief A generic protocol message carrying an opaque payload.
 *
 * The payload data can be represented as a continuous byte stream or byte
 * array. If this is true, the payload pointer should be of type byte*
 * that points to a byte array or simply NULL. The latter case is when
 * the data is intentionally not included in simulation for better
 * efficiency. Also, the real_length should be the number of real
 * bytes of this payload in the target system (rather than in the
 * simulator).
 */
class DataMessage : public ProtocolMessage {
public:
	/**
	 * The default constructor (without any argument).
	 */
	DataMessage();

	/**
	 * The copy constructor.
	 */
	DataMessage(const DataMessage& msg);

	/** The constructor with specific fields.
	 * If this is a simulation packet the data should be null
	 */
	DataMessage(int real_length, byte* payload=0);

	/**
	 * The destructor.
	 */
	virtual ~DataMessage();

	/**
	 * Clone the data message.
	 */
	virtual ProtocolMessage* clone();

	/**
	 * Return the protocol's archetype.
	 */
	virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_OPAQUE_DATA; }

	/**
	 * Return the number of bytes in this data message.
	 */
	virtual int size() const;

	/**
	 * Return the number of bytes needed to packet this protocol message
	 */
	virtual int packingSize() const { return sizeof(int)*2+(rawptr?real_length:0); }

	/**
	 * Serialize data into a byte array.
	 */
	virtual void serialize(byte* buf, int& offset);
	virtual int pack(prime::ssf::ssf_compact* dp);

	/**
	 *  Deserialize data from a byte array.
	 */
	virtual void deserialize(byte* buf, int& offset);
	virtual int unpack(prime::ssf::ssf_compact* dp);

	/**
	 * Convert from real bytes.
	 */
	virtual void fromRawBytes(Packet* rawpkt, byte*& rawbytes, int& nbytes);

	/**
	 * Convert to real types.
	 */
	virtual void toRawBytes(byte*& rawbytes, int& nbytes);

private:
	/** The real payload size, i.e., in the real target system rather
      than in the simulator. */
	int real_length;

};

}; // namespace ssfnet
}; // namespace prime

#endif /*__DATA_MESSAGE_H__*/
