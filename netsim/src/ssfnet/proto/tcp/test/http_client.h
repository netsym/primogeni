/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file http_client.h
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
#ifndef __HTTP_CLIENT_APP_H__
#define __HTTP_CLIENT_APP_H__

#include "proto/application_session.h"
#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "../agent/tcp_session.h"
#include "../tcp_master.h"
#include "proto/simple_socket.h"
#include "os/virtual_time.h"
#include "os/timer.h"

namespace prime {
namespace ssfnet {


class HTTPClientSessionTimer;
class HttpFinishedTrafficEvent;

class HTTPClient : public ConfigurableEntity<HTTPClient,ApplicationSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_HTTP_CLIENT)> {

friend class HTTPClientSessionTimer;

public:

	 typedef SSFNET_LIST(int) TCPSessionIDList;
	 typedef SSFNET_PAIR(uint32_t, UID_t) IDPair;
	 typedef SSFNET_PAIR(SimpleSocket*, uint64_t) SocketRemainingBytesPair;
	 typedef SSFNET_MAP(int, SocketRemainingBytesPair) SessionSocketBytesMap;

	 struct IDPairCMP {
	 	bool operator()(const IDPair & l, const IDPair & r) const {
	 		if (l.first != r.first) {
	 			return l.first < r.first;
	 		}
	 		return l.second < r.second;
	 	}
	 };

	 typedef SSFNET_MAP_CMP(IDPair, TCPSessionIDList, IDPairCMP) IDPairTCPSessionListMap;

	// The constructor
	HTTPClient();

	// The destructor
	virtual ~HTTPClient();

	// Configurable states
	
public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(uint32_t, active_sessions)
		SSFNET_CONFIG_STATE_DECL(uint32_t, bytes_received)
	)
	SSFNET_ENTITY_SETUP( )
;

	// Initializes this protocol session.
	virtual void init();

	// Called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();

	// This method initiates the request to the server by extracting data from the event passed
	void startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac);

 protected:

	// Called by the protocol session above to push a protocol message down the protocol stack
	virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0);

	// Called by the protocol session below to pop a protocol message up the protocol stack
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

 private:

 	void timeout(HTTPClientSessionTimer* timer, HttpFinishedTrafficEvent* finished_evt);

	TCPMaster* tcp_master; 	// The TCP layer below this protocol
 	HTTPClientSessionTimer* sess_timer; //session timeout timer

 	IDPairTCPSessionListMap session_map;    //<session id, traffic type id>, tcp session id list
 	SessionSocketBytesMap tcp_session_sock;  //tcp_session_id, <socket, remaining_bytes>
	uint64_t bytes_requested;  // Number of bytes received so far

	//VirtualTime last_log;
	//uint32_t last_bytes;
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__HTTP_CLIENT_APP_H__*/

