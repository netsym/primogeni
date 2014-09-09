/**
 * \file swing_server.cc
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

#include <stdio.h>
#include <stdlib.h>

#include "swing_server.h"
#include "os/timer.h" // defines Timer class
#include "net/host.h" // defines Host class
#include "os/protocol_session.h"
#include "os/ssfnet.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(SwingServer);
SwingServer::SwingServer(): tcp_master(0){

}

SwingServer::~SwingServer()
{
	if(tcp_master)
		delete tcp_master;
}

void SwingServer::init()
{
	// Get transport protocol below
	if(tcp_master == NULL){
		tcp_master = (TCPMaster*) inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP);
	}
	if(!tcp_master) LOG_DEBUG("ERROR: can't find the Transport layer; impossible!");

	serveRequest();
}

void SwingServer::wrapup() {}

int SwingServer::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra)
{
	LOG_ERROR("A message is pushed down to the SwingServer session from protocol layer above; it's impossible"<<endl);
	return 0;
}

int SwingServer::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
	SimpleSocket* sock = SSFNET_STATIC_CAST(SimpleSocket*,extra);
	LOG_DEBUG("in SwingServer::pop status=" << sock->getStatus() << endl);
	uint32 status_ = sock->getStatus();

	if(status_ & SimpleSocket::SOCKET_CONNECTED) {
		// This event is used to start a new socket so that it always keep listening
		LOG_DEBUG("in SwingServer::pop SOCKET_BUSY" << endl);
		serveRequest();
		// Another passive socket has been created
	}
	if(status_ & SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED) {
		LOG_DEBUG("in SwingServer::pop SOCKET_UPDATE" << endl);
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		LOG_DEBUG("BYTES RECEIVED SO FAR IN SWING SERVER:" << dm->size() << endl);
		if(dm){
			unshared.requestsReceived.write(unshared.requestsReceived.read() + 1);
			// This event is to keep track of the bytes received so far by this applications
			unshared.bytesReceived.write(unshared.bytesReceived.read() + dm->size());
			LOG_DEBUG("bytes received so far:" << unshared.bytesReceived.read() << endl);
			LOG_DEBUG("number of requests received in server:" << unshared.requestsReceived.read() << endl);
		}
		// Already updated the received bytes
	}
	if(status_ & SimpleSocket::SOCKET_PSH_FLAG) {
		LOG_DEBUG("in SwingServer::pop SOCKET_PSH" << endl);
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		uint32_t bytes_to_deliver = 0;
		//if the message is not empty analyze it
		if(dm){
			//char* newreq = new char[sizeof(int)];
			char* req = (char*)(dm->getRawData());
			LOG_DEBUG("DEBUGGING SWINGSERVER, raw data:"<< dm->getRawData()<<"-----"<<endl);
			//memcpy(newreq, req, sizeof(int));
			if(req){
				bytes_to_deliver = (uint32_t)atol(const_cast<char*>(req));
				// Request from a simulated host
				LOG_DEBUG("DEBUGGING SWINGSERVER, sending from Swing server:" << bytes_to_deliver<<endl);
				LOG_DEBUG("FOR NOW, DEBUGGING SWINGSERVER, size="<<bytes_to_deliver<<endl)
				sock->send(bytes_to_deliver);
			}
		}else {
			LOG_ERROR("\t\tDM should not be NULL!" << "\n" << endl);
		}
	}
	return 0;
}

void SwingServer::serveRequest(){
	// create simple new tcp application interface (note that socket
	// should not be created in the constructor; socket creation
	// requires action from linux tcp master session, which may not be
	// there when this protocol session starts!!!)
    new SimpleSocket(tcp_master, this, shared.listeningPort.read());
}

//SSFNET_REGISTER_APPLICATION_SERVER(1026,SwingServer);
SSFNET_REGISTER_APPLICATION_SERVER(1024,SwingServer);

}; // namespace ssfnet
}; // namespace prime
