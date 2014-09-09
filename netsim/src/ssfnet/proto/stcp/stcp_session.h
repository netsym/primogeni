/*
 * \file stcp_session.h
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
#ifndef __STCP_SESSION_H__
#define __STCP_SESSION_H__

#include "proto/transport_session.h"
#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/ssfnet_types.h"
#include "proto/simple_socket.h"
#include "stcp_master.h"
#include "stcp_message.h"

namespace prime {
namespace ssfnet {

class STCPMaster;
class STCPMessage;
class STCPConnection;
class STCPSessionSendDataTimer;

class STCPSession : public TransportSession {
public:
	enum State{
		START,
		UPDATE
	};
 public:
	// The constructor
	STCPSession(STCPMaster* master_, SimpleSocket* sock_, STCPConnection* conn_);

	// The destructor
	virtual ~STCPSession();

	// Send virtual data, called by socket
	virtual void send(uint32 length);

	// Send the bytes pointed by msg - disabled
	virtual void sendMsg(uint32 size, byte* msg=0, bool send_data_and_disconnect=false);

	// Send probe message
	void sendProbe(STCPMessage::Type type, uint32 bytes_to_send, int32 bytes_received_per_intv);

	// Send virtual data in a constant rate - call back by a timer
	void sendVirtualData(uint32 length);

	// Receive the message popped up from the ip session
	virtual void receive(STCPMessage* stcpmsg, void* extinfo);

	virtual int getID();

	STCPConnection* getConnection(){ return conn;};

	SimpleSocket* getSocket() { return socket; }

	STCPMaster* getMaster() {return stcp_master;}

	void setLastProbeSent(VirtualTime t);

	void addTotalBytesSent(uint32_t bytes_sent);

 private:
	// Point to the stcp master
	STCPMaster* stcp_master;

	// The socket id
	SimpleSocket* socket;

	// The connection data
	STCPConnection* conn;

	// To detect when the session has connected
	bool first_packet;

	// To calculate the loss probability in recent time interval
	STCPMessage::LossCalculationMap* loss_cal;

	// Bytes to send in total
	uint32_t bytes_to_send;

	// Bytes to send between two probes
	uint32_t bytes_to_send_per_update;

	// Bytes received in total
	int32_t bytes_received;

	// Bytes received between two probes
	int32_t bytes_received_per_update;

	// Total bytes sent by the sender
	uint32_t total_bytes_sent;

	// The state of the session
	State state;

	// A timer used to send data
	STCPSessionSendDataTimer* timer;

	// Whether it is in slow start phase
	bool is_slow_start;

	// Whether the end probe has been sent
	bool is_end;

	// When the last probe was sent
	VirtualTime last_probe_sent;
};

}
}
#endif
