/*
 * \file udp_session.cc
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

#include "udp_session.h"
#include "os/logger.h"
#include "proto/udp/udp_message.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(UDPSession);

UDPSession::UDPSession(UDPMaster* master_, SimpleSocket* sock_, UDPConnection* conn_) :
		udpMaster(master_), socket(sock_), conn(conn_), firsPacket(false) { }

UDPSession::~UDPSession() {
	if(socket)
		delete socket;
	if(conn)
		delete conn;
}

void UDPSession::send(uint32 length)
{
	LOG_DEBUG("Send "<< length << " (B) virtual data down the protocol stack, getMaxDatagramSize()=" <<
			udpMaster->getMaxDatagramSize() << endl);
	int offset = 0;
	while(offset < (int) length) { // if there's anything left to be sent
		int to_send = (int) mymin(udpMaster->getMaxDatagramSize(), (int)length-offset);
		offset += to_send;
		LOG_DEBUG("UDPSession::send to_send=" << to_send << endl);
		to_send += sizeof(RawUDPHeader); //udp length includes header
		UDPMessage* udp_msg = new UDPMessage(conn->srcPort, conn->dstPort, to_send);
		udp_msg->carryPayload(new DataMessage(to_send));

		LOG_DEBUG("UDPSession::send simu to_send:" << to_send <<
				" length=" << length << " offset=" << offset << " udp_msgsize=" <<
				udp_msg->size() << " payload=" << udp_msg->getLength() << endl);

		// No payload to carry here
		LOG_DEBUG("UDPSession: simu srcIP=" << conn->srcIP << " conn->dstIP" << conn->dstIP << endl);
		IPPushOption ops(conn->srcIP, conn->dstIP, SSFNET_PROTOCOL_TYPE_UDP, DEFAULT_IP_TIMETOLIVE);
		udpMaster->push(udp_msg, 0, (void*)&ops, sizeof(IPPushOption));
	}
}

void UDPSession::sendMsg(uint32 length, byte* msg, bool send_data_and_disconnect)
{
	LOG_DEBUG("UDPSession::send emu"<< length << " down the protocol stack" << endl);
	int offset = 0;
	while(offset < (int) length) { // if there's anything left to be sent
		int to_send = (int) mymin(udpMaster->getMaxDatagramSize(), (int)length-offset);
		DataMessage* dmsg;
		if(msg) dmsg = new DataMessage(to_send, &msg[offset]);
		else dmsg = new DataMessage(to_send);

		UDPMessage* udp_hdr = new UDPMessage(conn->srcPort, conn->dstPort, 8+to_send);
		udp_hdr->carryPayload(dmsg);
		LOG_DEBUG("emu srcIP=" << conn->srcIP << " conn->dstIP" << conn->dstIP << endl);
		IPPushOption ops(conn->srcIP, conn->dstIP, SSFNET_PROTOCOL_TYPE_UDP, DEFAULT_IP_TIMETOLIVE);
		udpMaster->push(udp_hdr, 0, (void*)&ops, sizeof(IPPushOption));

		offset += to_send;
	}
}

void UDPSession::receive(UDPMessage* udpmsg, void* extinfo)
{
	LOG_DEBUG("UDPSESSION: rx a pkt! with size=" << udpmsg->getLength() << endl);
	uint32 socket_status_mask_ = 0;

	if(firsPacket == false){
		firsPacket = true;
		socket->socketBusy();
	}

	DataMessage* dm = (DataMessage*)udpmsg->payload();
	if(!dm) {
		LOG_ERROR("what happened?\n");
	}
	if(dm->getRawData()) {
		socket_status_mask_ |= SimpleSocket::SOCKET_PSH_FLAG;
		socket_status_mask_ |= SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED;

		LOG_DEBUG("UDPSession::This packet DOES have a REAL payload, size=" <<
				udpmsg->getLength() << endl);
		socket->recv(udpmsg->getLength() - sizeof(RawUDPHeader), (char*)(dm->getRawData()),
				socket_status_mask_);
	}
	else {
		LOG_DEBUG("UDPSession::This packet does NOT have a REAL payload, size=" <<
				udpmsg->getLength()<< endl);
		socket_status_mask_ |= SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED;
		socket->recv(dm->size(), 0, socket_status_mask_);
	}
}

int UDPSession::getID() { return conn->dstPort*100000+conn->srcPort;}


}; // namespace ssfnet
}; // namespace prime
