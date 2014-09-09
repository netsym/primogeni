/**
 * \file simple_socket.h
 * \brief Header file for the SimpleSocket class.
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

#ifndef __SIMPLE_SOCKET_H__
#define __SIMPLE_SOCKET_H__

#include "os/ssfnet.h"
#include "proto/tcp/tcp_master.h"
#include "proto/udp/udp_master.h"

namespace prime {
namespace ssfnet {

class TCPMaster;
class UDPMaster;
class ProtocolSession;
class ProtocolMessage;
class Packet;
class UDPSession;

class SimpleSocket {
 public:
	/*
	 * When a socket calls pop on the application, this checks the status of the socket
	 *  to decide what to do with the data conveyed
	 */

	enum SocketStat {
		SOCKET_PSH_FLAG 		     = 0x00000001,
		SOCKET_UPDATE_BYTES_RECEIVED = 0x00000010,
		SOCKET_LISTENING             = 0x00000100,
		SOCKET_CONNECTED             = 0x00001000,
		SOCKET_FIN_FLAG_RECEIVED     = 0x00010000,
		SOCKET_ALL_BYTES_RECEIVED    = 0x00100000
	};

	// Constructor used for active openings - clients
	SimpleSocket(TransportMaster* master_, ProtocolSession* application_,
			IPAddress destIP_, int dstPort_);

	// Constructor used for passive openings - servers
	SimpleSocket(TransportMaster* master_, ProtocolSession* application_,
			int listeningPort_);

	// The destructor
	virtual ~SimpleSocket();

	// Used by applications to get the status of the socket
	uint32 getStatus() const { return status; }

	// Send data down the protocol stack - emulation
	void disconnect();

	// Send data and initiate disconnection
	void disconnect(uint32_t size, byte* msg);

	// Send data down the protocol stack - emulation
	void send(uint64_t size, byte* msg=0);

	// Used to signal the application that some bytes were received in TCP
	void recv(uint32_t size, char* appData, uint32 status);

	// Used to signal the application that this socket is busy now
	void socketBusy();

	//Used to signal the application that all data sent so far has been received
	void allBytesReceived();

	//How many bytes have been recved per second
	uint32_t getInstantaneousRecvThroughput();

	//How many bytes have been sent per second
	uint32_t getInstantaneousSendThroughput();

	//How many bytes have been recved
	uint32_t getBytesRecv() { return totalBytesRecv; }

	//How many bytes have been sent
	uint32_t getBytesSent() { return totalBytesSent; }

	//when was the first send
	VirtualTime getFirstSend() { return firstSend; }

	//when was the first recv
	VirtualTime getFirstRecv() { return firstRecv; }

	//when was the last recv
	VirtualTime getLastRecv() { return lastRecv; }

	//when was the last send
	VirtualTime getLastSent() { return lastSent; }

	int getSessionId() { return session->getID(); }

	ProtocolSession* getApp() { return app; }

	TransportSession* getSession() { return session; }


 private:

	// Pointer to application below this socket
	TransportMaster* master;
	// Pointer to session
	TransportSession* session;
	// Pointer to application
	ProtocolSession* app;
	// My Status
	uint32 status;

	//used to calculate instantaneous throughput
	VirtualTime firstRecv,firstSend,lastRecv, lastSent;
	uint32_t totalBytesSent, totalBytesRecv;
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__SIMPLE_SOCKET_H__*/
