/**
 * \file simple_socket.cc
 * \brief Implementation file for the SimpleSocket class.
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
#include <stdio.h>
#include <stdlib.h>

#include "proto/simple_socket.h"
#include "proto/tcp/tcp_master.h"
#include "proto/tcp/agent/tcp_session.h"
#include "os/logger.h"
#include "proto/tcp/test/http_server.h"
#include "proto/transport_session.h"
#include "net/host.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(SimpleSocket);

// Active open
SimpleSocket::SimpleSocket(TransportMaster* master_, ProtocolSession* application_,
		IPAddress destIP_, int dstPort_) :
		master(master_),
		session(0),
		app(application_),
		status(0),
		firstRecv(0),
		firstSend(0),
		lastRecv(0),
		lastSent(0),
		totalBytesSent(0),
		totalBytesRecv(0)
{
	//make sure master and app were assigned correct values
	assert(master);
	assert(app);

	// The TCP layer below will create a new session
	session = master->createActiveSession(this, master_->nextSourcePort(), destIP_, dstPort_);
	assert(session);
	LOG_DEBUG("creating a new active SOCKET " << " dst=" << destIP_ <<
			" dstPort" << dstPort_ << endl);
	//LOG_DEBUG("master="<<((ProtocolSession*)master)->getUID()<<endl);
	//Initialize this socket
	session->activeOpen();
}

// Passive open
SimpleSocket::SimpleSocket(TransportMaster* master_, ProtocolSession* application_,
		int listeningPort_) : master(master_),
		session(0),
		app(application_),
		status(0),
		firstRecv(0),
		firstSend(0),
		lastRecv(0),
		lastSent(0),
		totalBytesSent(0),
		totalBytesRecv(0)
{
	//make sure master and app were assigned correct values
	assert(master);
	assert(app);

	// The TCP layer below will create a new session
	session = master->createListeningSession(this, IPADDR_INADDR_ANY, listeningPort_);
	assert(session);
	LOG_DEBUG("creating a new passive SOCKET listeningPort_" << listeningPort_ << endl);
	//LOG_DEBUG("master="<<((ProtocolSession*)master)->getUID()<<endl);
	//Initialize this socket
	session->passiveOpen();
}

SimpleSocket::~SimpleSocket() {}

uint32_t SimpleSocket::getInstantaneousRecvThroughput() {
	if(lastRecv != 0)
		return totalBytesRecv/(lastRecv.second()-firstRecv.second());
	return 0;
}

uint32_t SimpleSocket::getInstantaneousSendThroughput() {
	if(lastSent != 0)
		return totalBytesSent/(lastSent.second()-firstSend.second());
	return 0;
}
void SimpleSocket::disconnect() {
	/*fprintf(app->inHost()->getCommunity()->dump_file_for_pkt,
			//"%llu %f %i\n",
			"%f tcp session %d disconnect\n",
			//this->getInterface()->getUID(),
			app->inHost()->getNow().second(),
			this->getSessionId());*/
	session->sendMsg(0, 0);
	master->deleteSession(session);
}

void SimpleSocket::disconnect(uint32_t size, byte* msg){
	session->sendMsg(size, msg, true);
}

void SimpleSocket::send(uint64_t size, byte* msg)
{
ACK_COUT("simple socket send", ((TCPMaster*)master));
	if(firstSend == 0) firstSend = app->inHost()->getNow();
	lastSent=app->inHost()->getNow();
	totalBytesSent+=size;
	LOG_DEBUG("DEBUGGING SWING, SimpleSocket::size= " << size << ", msg="<<msg<<endl);

	if(size>0) {
		if(msg) {
			//send this message down the protocol stack
			session->sendMsg(size, msg);
		}
		else {
			LOG_DEBUG("FOR NOW, session send, size="<<size<<endl)
			session->send(size);
		}
	}
	else {
		LOG_ERROR("you must send more than 0 bytes!\n");
	}
}


void SimpleSocket::recv(uint32_t size, char* appData, uint32 status_){
ACK_COUT("simple socket recv", ((TCPMaster*)master));
	if(firstRecv == 0) firstRecv = app->inHost()->getNow();
	if(size>0)lastRecv=app->inHost()->getNow();
	totalBytesRecv+=size;
	//Send this data up to the App layer
	status = status_;
	if(appData != NULL) {
		DataMessage* data = new DataMessage(size, (byte*)appData);
		LOG_DEBUG("RCVWND: UPDATING THE APP with data not null size:" << size << endl);
		app->pop(data, ((ProtocolSession*)master), (void*)this);
	} else {
		DataMessage* data = new DataMessage(size, 0);
		LOG_DEBUG("RCVWND: UPDATING THE APP with NULL data size:" << size << endl);
		app->pop(data, ((ProtocolSession*)master), (void*)this);
	}
	return;
}

void SimpleSocket::socketBusy()
{
	//signal application that this socket is now busy with a session so it can initiate another
	status = SOCKET_CONNECTED;
	LOG_DEBUG("calling pop for socketbusy" << endl);
	app->pop(NULL, ((ProtocolSession*)master), (void*)this);
}

void SimpleSocket::allBytesReceived()
{
	status = SOCKET_ALL_BYTES_RECEIVED;
	LOG_DEBUG("calling pop for allbytesreceived" << endl);
	app->pop(NULL, ((ProtocolSession*)master), (void*)this);
}

}; // namespace ssfnet
}; // namespace prime
