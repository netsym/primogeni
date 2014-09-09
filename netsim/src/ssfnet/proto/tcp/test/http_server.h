/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file http_server
 * \author Miguel Erazo
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
#ifndef __HTTP_SERVER_APP_H__
#define __HTTP_SERVER_APP_H__

#include "proto/application_session.h"
#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "../agent/tcp_session.h"
#include "../tcp_master.h"
#include "proto/simple_socket.h"

namespace prime {
namespace ssfnet {

class SimpleSocket;

class HTTPServer : public ConfigurableEntity<HTTPServer,ApplicationSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_HTTP_SERVER)> {

	friend class SimpleSocket;

 public:

	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(uint32_t, listeningPort)
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(uint32_t, bytesReceived)
		SSFNET_CONFIG_STATE_DECL(uint32_t, requestsReceived)
	)
	SSFNET_ENTITY_SETUP( )
;

	HTTPServer(); 

	// The destructor
	virtual ~HTTPServer();

	// Called after config() to initialize this protocol session
	virtual void init();

	// Called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();

 protected:

	// Called by the protocol session above to push a protocol message down the protocol stack
	virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0);

	// Called by the protocol session below to pop a protocol message up the protocol stack
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

 private:
	// Create a socket and later a session for a given request
	void serveRequest();

	// This method verifies whether the request is from a simulated host or an emulated host
	bool isEmulationRequest(char* req, uint64_t& bytes_to_deliver);

	// The TCP layer below this protocol
	TCPMaster* tcpMaster;
 public:
	static char* global_buf;
	static int precalc_cksums[1500];
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__HTTP_SERVER_APP_H__*/

