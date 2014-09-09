
/*
 * \file stcp_message.h
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
#ifndef __STCP_MESSAGE_H__
#define __STCP_MESSAGE_H__

#include "os/ssfnet.h"
#include "os/protocol_message.h"
#include "os/data_message.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

class RawSTCPHeader;

class STCPMessage: public RawProtocolMessage {
 public:
	typedef SSFNET_MAP(UID_t, SSFNET_VECTOR(int)) LossCalculationMap;
	enum Type {
		START,
		UPDATE,
		END
	};
	STCPMessage();

	STCPMessage(uint16 src_port_, uint16 dst_port_, uint32 length_ = 8,
				STCPMessage::Type type_=START, double send_time_=0,
				uint32 bytes_to_send_=0, int32 bytes_received=0,
				STCPMessage::LossCalculationMap* m=0, float min_ts_=0, int seq_=0);

	STCPMessage(STCPMessage const & stcpmsg_);

	//virtual methods
	virtual ProtocolMessage* clone();
	virtual int size() const;
	virtual void toRawBytes(byte*& b, int& n);
	virtual void fromRawBytes(Packet* p, byte*& b, int& n);
	virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_STCP; }

	virtual int packingSize() const;
	virtual void serialize(byte* buf, int& offset);
	virtual void deserialize(byte* buf, int& offset);

	const uint16 getSrcPort() const;
	const uint16 getDstPort() const;
	const uint32 getLength() const;
	const STCPMessage::Type getType() const;
	const double getSendTime() const;
	const uint32 getBytesToSend() const;
	const int32 getBytesReceivedPerIntv() const;
	LossCalculationMap* getLossCalculationMap();
	const float getMinTimestep() const;
	const int getSeq() const;

	void setSrcPort(uint16 src_port_);
	void setDstPort(uint16 dst_port_);
	void setLength(uint32 length_);
	void setType(STCPMessage::Type type_);
	void setSendTime(double send_time_);
	void setBytesToSend(uint32 to_send_);
	void setBytesReceivedPerIntv(int32 received_);
	void setLossCalculationMap(STCPMessage::LossCalculationMap* m);
	void insertLossCalculationMap(UID_t uid, int in, int out, int queue_size);
	void initLossCalculationMap();
	void setMinTimestep(float min_ts);
	void setSeq(int seq);
};

class RawSTCPHeader {
 private:
	// Source port number
	uint16 srcPort;
	// Destination port number
	uint16 dstPort;
	// Specifies the length in bytes of the entire datagram: header and data
	uint32 length;
	// type
	STCPMessage::Type type;
	// the time probe is sent from the sender
	double send_time;
	// bytes to send in total
	uint32 bytes_to_send;
	// bytes received between two update probes
	int32 bytes_received_per_intv;
	// A map of the in/out bytes difference and queue size fo the interfaces for calculating the loss probability
	STCPMessage::LossCalculationMap* loss_cal;
	// minimum time step
	float min_timestep;
	// seq number
	int seq;

 public:
	void setSrcPort(uint16 src_port_) { srcPort = src_port_; }
	void setDstPort(uint16 dst_port_) { dstPort = dst_port_; }
	void setLength(uint32 length_) { length = length_; }
	void setType(STCPMessage::Type type_) { type=type_; }
	void setSendTime(double send_time_) { send_time=send_time_; }
	void setBytesToSend(uint32 to_send_) { bytes_to_send=to_send_; }
	void setBytesReceivedPerIntv(int32 received_) { bytes_received_per_intv=received_; }
	void setLossCalculationMap(STCPMessage::LossCalculationMap* m) { loss_cal=m; }
	void insertLossCalculationMap(UID_t iface, int in, int out, int queue) {
		SSFNET_VECTOR(int) v;
		v.push_back(in);
		v.push_back(out);
		v.push_back(queue);
		loss_cal->insert(SSFNET_MAKE_PAIR(iface, v));
	}
	void initLossCalculationMap() { loss_cal= new STCPMessage::LossCalculationMap(); }
	void setMinTimestep(float min_ts_) {min_timestep=min_ts_;}
	void setSeq(int s){seq=s;}

	const uint16 getSrcPort() const { return srcPort; }
	const uint16 getDstPort() const { return dstPort; }
	const uint32 getLength() const { return length; }
	const STCPMessage::Type getType() const {return type;}
	const double getSendTime() const {return send_time;}
	const uint32 getBytesToSend() const {return bytes_to_send;}
	const int32 getBytesReceivedPerIntv() const {return bytes_received_per_intv;}
    STCPMessage::LossCalculationMap* getLossCalculationMap() {return loss_cal; }
	const float getMinTimestep() const {return min_timestep;}
	const int getSeq() const {return seq;}
};

} //namespace prime
} //namespace ssfnet
#endif
