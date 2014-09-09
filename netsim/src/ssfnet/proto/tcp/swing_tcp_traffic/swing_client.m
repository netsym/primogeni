/**
 * \file swing_client.h
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
#ifndef __SWING_CLIENT_H__
#define __SWING_CLIENT_H__

#include "proto/application_session.h"
#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "../agent/tcp_session.h"
#include "../tcp_master.h"
#include "proto/simple_socket.h"
#include "swing_tcp_traffic.h"

namespace prime {
namespace ssfnet {

class TimeoutTimer;
class SwingFinishedTrafficEvent;

class SwingClient : public ConfigurableEntity<SwingClient,ApplicationSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_SWING_CLIENT)> {
	friend class TimeoutTimer;
public:
	typedef SSFNET_LIST(SimpleSocket*) SocketList;
	typedef SSFNET_PAIR(uint32_t, UID_t) IDPair;

	struct IDPairCMP {
		bool operator()(const IDPair & l, const IDPair & r) const {
			if (l.first != r.first) {
				return l.first < r.first;
			}
			return l.second < r.second;
		}
	};

	typedef SSFNET_MAP_CMP(IDPair, SocketList, IDPairCMP) IDPairSocketListMap;

	// the constructor
	SwingClient();

	// the destructor
	virtual ~SwingClient();

	//configurable state
	state_configuration {
		configurable uint32_t active_sessions {
			type=INT;
			default_value="0";
			doc_string="This is the number of active downloading sessions";
		};
		configurable uint32_t bytes_received {
			type=INT;
			default_value="0";
			doc_string="Number of bytes received from all sessions";
		};
	};

	// This method initiates the request to the server
	void startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac);

	virtual void init();

	// called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();

	void recv(ProtocolMessage* msg, Packet* packet, ProtocolSession* lo_sess,void* extinfo, size_t extinfo_size);

 protected:

	// Called by the protocol session above to push a protocol message down the protocol stack
	virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0);

	// Called by the protocol session below to pop a protocol message up the protocol stack
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

 private:
	virtual void send(int req_size, int rsp_size);          // send bytes down the protocol stack
	void timeout(TimeoutTimer* t_timer, SwingFinishedTrafficEvent* finished_evt);    //close all the sockets of the session or rre by the given action_id

	TCPMaster* tcp_master;   // the TCP layer below this protocol
	SimpleSocket* simple_sock;
	TimeoutTimer* timeout_timer;   //session or rre timeout timer

	IDPairSocketListMap session_socket;    //session id to socket list map
	IDPairSocketListMap rre_socket;   //rre id to socket list map

	FILE* monitor_file; //dump traffic info for monitor purpose
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__SWING_CLIENT_H__*/

