/*
 * \file stcp_master.h
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
#ifndef __STCP_MASTER_H__
#define __STCP_MASTER_H__

#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "os/routing.h"
#include "proto/stcp/stcp_session.h"
#include "proto/transport_session.h"

namespace prime {
namespace ssfnet {

class SimpleSocket;
class STCPSession;

class STCPConnection {
 public:
	int src_port;
	int dst_port;
	IPAddress src_ip;
	IPAddress dst_ip;
	static bool fncmp(STCPConnection* l, STCPConnection* r);
};


#define DEFAULT_IP_TIMETOLIVE 64
#ifndef IPADDR
typedef prime::ssf::uint32 IPADDR;
#endif

class STCPMaster : public ConfigurableEntity<STCPMaster,ProtocolSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_STCP)>, public TransportMaster
{

 public:
	 typedef SSFNET_MAP_CMP(STCPConnection*, STCPSession*, bool (*)(STCPConnection *,STCPConnection *)) STCP_SESSION_MAP;
	 typedef SSFNET_MAP(int, STCPSession*) STCP_LISTEN_SESSION_MAP;
  	// The constructor
    STCPMaster();

    // The destructor
	~STCPMaster();

	// Called after config() to initialize this protocol session
	void init();

	// Called by the protocol session above to push a protocol message down the protocol stack
	virtual int push(ProtocolMessage* msg, ProtocolSession* hi_sess,
			void* extinfo = 0, size_t extinfo_size = 0);

	// This method is called when an ip message is popped up by a local
	//  network interface.
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

	//create active session
	virtual TransportSession* createActiveSession(SimpleSocket* sock_,
			int local_port,
			IPAddress remote_ip, int remote_port);

	//Create a listening session
	virtual TransportSession* createListeningSession(SimpleSocket* sock,
			IPAddress listen_ip, int listen_port);

	// Reclaim a STCP session
	void deleteSession(TransportSession* session);

	// Change this session to connected state
	void changeToConnected(STCPSession* sess);

	state_configuration {
		shared configurable int max_datagram_size {
			type=INT;
			default_value="1470";
			doc_string="maximum datagram size.";
		};
	};
	// Return the configuration parameters
	int getMaxDatagramSize() { return shared.max_datagram_size.read(); }

	virtual int nextSourcePort() { return next_src_port++; }

private:

	// a map for active sessions searching
	STCP_SESSION_MAP active_sessions;

	// a map for listening sessions searching
	STCP_LISTEN_SESSION_MAP listening_sessions;

	// The IP layer protocol session, located below this protocol
	//session on the protocol stack
	ProtocolSession* ip_sess;

	int next_src_port;
};

}
}
#endif //__STCP_MASTER_H__
