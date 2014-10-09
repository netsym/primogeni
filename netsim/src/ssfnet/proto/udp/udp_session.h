/*
 * \file udp_session.h
 * \author Miguel Erazo
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
#ifndef __UDP_SESSION_H__
#define __UDP_SESSION_H__

#include "proto/transport_session.h"
#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/ssfnet_types.h"
#include "proto/simple_socket.h"

namespace prime {
namespace ssfnet {

class UDPMaster;
class UDPMessage;
class UDPConnection;

class UDPSession : public TransportSession {
 public:
	// The constructor
	UDPSession(UDPMaster* master_, SimpleSocket* sock_, UDPConnection* conn_);

	// The destructor
	virtual ~UDPSession();

	// Send this number of bytes - simulation
	virtual void send(uint64 length);

	// Send the bytes pointed by msg - emulation
	virtual void sendMsg(uint64 size, byte* msg=0, bool send_data_and_disconnect=false);

	// Receive the message popped up from the ip session
	virtual void receive(UDPMessage* udpmsg, void* extinfo);

	virtual int getID();

	UDPConnection* getConnection(){ return conn;};

	SimpleSocket* getSocket() { return socket; }
 private:
	// Point to the udp master
	UDPMaster* udpMaster;

	// The socket id
	SimpleSocket* socket;

	// The connection data
	UDPConnection* conn;		//src ip, src port, dest ip, dest port

	// To detect when the session has connected
	bool firsPacket;
};

}
}
#endif
