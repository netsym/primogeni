/**
 * \file swing_server.h
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

#ifndef __SWING_SERVER_H__
#define __SWING_SERVER_H__

#include "proto/application_session.h"
#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "proto/tcp/agent/tcp_session.h"
#include "proto/tcp/tcp_master.h"
#include "proto/simple_socket.h"

namespace prime {
namespace ssfnet {

class SimpleSocket;

class SwingServer : public ConfigurableEntity<SwingServer,ApplicationSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_SWING_SERVER)> {

	friend class SimpleSocket;

 public:
		state_configuration {
	 		shared configurable uint32_t listeningPort {
	 			type=INT;
	 			default_value="1024";
	 			doc_string="Listening port for incoming connections.";
	 		};
	 		configurable uint32_t bytesReceived {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of bytes received so far from all sessions";
	 		};
	 		configurable uint32_t requestsReceived {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of requests received so far from all sessions";
	 		};
	 	};

	//the constructor
	SwingServer();

	//the destructor
	virtual ~SwingServer();

	virtual void init();

	// called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();

	protected:

		// Called by the protocol session above to push a protocol message down the protocol stack
		virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0);

		// Called by the protocol session below to pop a protocol message up the protocol stack
		virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

private:
	// Create a socket and later a session for a given request
	void serveRequest();

private:
	TCPMaster* tcp_master;   // the TCP layer below this protocol

};

}; // namespace ssfnet
}; // namespace prime

#endif /*__SWING_SERVER_H__*/

