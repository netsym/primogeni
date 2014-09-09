/**
 * \file transport_session.cc
 * \brief Implementation file for the TransportSession class.
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
#include "proto/transport_session.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(TransportSession);

TransportSession* TransportMaster::createActiveSession(SimpleSocket* sock_,
		int srcPort_, IPAddress dst_ip, int dstPort_) {
	LOG_ERROR("Should have been overridden"<<endl);
	return NULL;
}

TransportSession* TransportMaster::createListeningSession(SimpleSocket* sock,
		IPAddress dstip, int listening_port) {
	LOG_ERROR("Should have been overridden"<<endl);
	return NULL;
}

int TransportMaster::nextSourcePort() {
	LOG_ERROR("Should have been overridden"<<endl);
	return 0;
}

void TransportMaster::deleteSession(TransportSession* session){
	LOG_ERROR("Should have been overridden"<<endl);
}



void TransportSession::send(uint64_t bytes_) {
	LOG_ERROR("Should have been overridden"<<endl)
}
void TransportSession::sendMsg(uint64_t size, byte* msg, bool send_data_and_disconnect) {
	LOG_ERROR("Should have been overridden"<<endl)
}
int TransportSession::getID() {
	LOG_ERROR("Should have been overridden"<<endl)
	return 0;
}

}
}

