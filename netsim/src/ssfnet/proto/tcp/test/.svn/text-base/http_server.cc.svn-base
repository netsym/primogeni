/**
 * \file http_server.cc
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
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <cstring>

#include "http_server.h"
#include "net/host.h"
#include "os/protocol_session.h"
#include "os/ssfnet.h"
#include "os/logger.h"

//#define LOG_WARN(X) std::cout<<"["<<__LINE__<<"]"<<X;
//#define LOG_DEBUG(X) std::cout<<"["<<__LINE__<<"]"<<X;

#define CHUNK_SIZE 1*1024*1024 /*1M*/
//#define CHUNK_SIZE 10*1024*1024 /*10M (slow)*/

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(HTTPServer);

char* HTTPServer::global_buf = 0;
int HTTPServer::precalc_cksums[1500];

HTTPServer::HTTPServer(): tcpMaster(0){
}

HTTPServer::~HTTPServer()
{
	if(tcpMaster)
		delete tcpMaster;
}

void HTTPServer::init()
{
	// Get transport protocol below
	if(tcpMaster == NULL)
		tcpMaster = (TCPMaster*) inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP);
	if(!tcpMaster) LOG_DEBUG("ERROR: can't find the Transport layer; impossible!");

	serveRequest();
	if(!global_buf) {
		global_buf = new char[CHUNK_SIZE];
		global_buf[0]='\0';
		memset(global_buf,'p',CHUNK_SIZE);
		precalc_cksums[0]=0;
		for(int i=1;i<1500;i++) {
			precalc_cksums[i]=fast_cksum_a((uint16*)global_buf, i);
		}
	}
}

void HTTPServer::wrapup() {}

int HTTPServer::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra)
{
	LOG_ERROR("ERROR: a message is pushed down to the HTTPServer session"
				" from protocol layer above; it's impossible\n");
	return 0;
}

int HTTPServer::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
ACK_COUT("http pop", this);
	SimpleSocket* sock = SSFNET_STATIC_CAST(SimpleSocket*,extra);
	LOG_DEBUG("in HTTPServer::pop status=" << sock->getStatus() << endl);
	uint32 status_ = sock->getStatus();

	if(status_ & SimpleSocket::SOCKET_CONNECTED) {
		// This event is used by this HTTP server to start a new socket so that it always keep listening
		LOG_DEBUG("in HTTPServer::pop SOCKET_BUSY" << endl);
		serveRequest();
		// Another passive socket has been created
	}
	if(status_ & SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED) {
		LOG_DEBUG("in HTTPServer::pop SOCKET_UPDATE" << endl);
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		if(dm){
			LOG_DEBUG("\t\tHTTPSERVER: DM IS NOT NULL\n" << endl);
			unshared.requestsReceived.write(unshared.requestsReceived.read() + 1);
			// This event is to keep track of the bytes received so far by this applications
			unshared.bytesReceived.write(unshared.bytesReceived.read() + dm->size());
			LOG_DEBUG("BYTES RECEIVED SO FAR IN HTTP SERVER:" << unshared.bytesReceived.read() << endl);
			LOG_DEBUG("number of requests received in this server:" << unshared.requestsReceived.read() << endl);
		}
		// Already updated the received bytes
	}
	if(status_ & SimpleSocket::SOCKET_ALL_BYTES_RECEIVED){
		LOG_DEBUG("all bytes received in http client, send a FIN" << endl);
		char* buffer = new char[50];
		sprintf(buffer, "HTTP/1.0 200 OK\r\n\r\n");
		sock->disconnect(strlen(buffer), (byte*)buffer);
	}
	if(status_ & SimpleSocket::SOCKET_PSH_FLAG) {
		LOG_DEBUG("in HTTPServer::pop SOCKET_PSH" << endl);
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		if(dm){
			// There exists a payload that must be conveyed to this application, let's get it
			char* message = (char*)(dm->getRawData());
			uint64_t bytes_to_deliver = 0;

			//std::cout<<"RECEIVED A REQUEST IN HTTP SERVER"<<"\n";

			if(message) {

				// Start sending the data
				if(isEmulationRequest(message, bytes_to_deliver)) {
					// Request from an emulated host so fill buffer and send it
					char* buffer = new char[1024];
					buffer[0]='\0';

					LOG_DEBUG("HTTPServer::pop: sending data... at " << inHost()->getNow().second() << endl);
					strcat(buffer, "HTTP/1.1 200\n");
					strcat(buffer, "Date: ");
					time_t t; time(&t);
					strcat(buffer, asctime(gmtime(&t)));
					strcat(buffer, "Server: PRIME SSFNET SimpleHTTPServer Version 1.0\n");
					strcat(buffer, "Last-Modified: ");
					strcat(buffer, asctime(gmtime(&t)));
					strcat(buffer, "ETag: \"00000-0000-00000000\"\n"
							"Accept-Ranges: bytes\n");
					sprintf(&buffer[strlen(buffer)], "Content-Length: %d\n", bytes_to_deliver);
					strcat(buffer, "Connection: close\n"
							 "Content-Type: text/html\n\n");
					sock->send(strlen(buffer), (byte*) buffer);
					buffer = new char[1024];

					strcat(buffer, "<HTML><HEAD>\n<TITLE>SSFNet Simple Web Server</TITLE>\n</HEAD><BODY>\n"
							 "<H1>SSFNet Simple Web Server</H1>\n"
							 "Welcome and Goodbye!<P>\n"
							 "<HR>\n");
					strcat(buffer, "<ADDRESS>Server at ");

					SSFNET_STRING myip = inHost()->getDefaultIP().toString();
					strcat(buffer, myip.c_str());
					strcat(buffer, " Port 80</ADDRESS>\n");
					bytes_to_deliver-=strlen(buffer);	
					sock->send(strlen(buffer), (byte*) buffer);

				   	bytes_to_deliver-=strlen("</BODY></HTML>\n");

					LOG_DEBUG("pagesize=" << bytes_to_deliver << endl);
					for(; bytes_to_deliver>0;) {
						uint j = CHUNK_SIZE;
						if(j>bytes_to_deliver) j=bytes_to_deliver;
						buffer = new char[j];
						memcpy(buffer,global_buf,j);
						bytes_to_deliver-=j;
						sock->send(j, (byte*) buffer);
					}
					buffer = new char[100];
					buffer[0]='\0';
					strcat(buffer, "</BODY></HTML>\n");
					sock->send(strlen(buffer), (byte*) buffer);
				}
				else {
					// Request from a simulated host
					LOG_DEBUG("sending from HTTP server for simulated:" << bytes_to_deliver);
					std::cout<<getUName()<<"["<<getParent()->getUID()<<"] is sending "<<bytes_to_deliver<<", time="<<inHost()->getNow().second()<<std::endl;
					sock->send(bytes_to_deliver);
				}
			}
		} else {
			LOG_ERROR("This should never happen!" << endl);
		}
	}
	return 0;
}

bool HTTPServer::isEmulationRequest(char* req, uint64_t& bytes_to_deliver)
{
	LOG_DEBUG("server request=" << req << " sizeof(req)=" << sizeof(req) <<
			" strlen=" << strlen(req) << " req:\n" << SSFNET_STRING(req) << "\n");

	if(req[0] == 'G' && req[1] == 'E' && req[2] == 'T') {
		char* pch = strtok (req," /."); //get
		pch = strtok (NULL," /.");
		bytes_to_deliver=(uint32_t)atol(const_cast<char*>(pch));
		bytes_to_deliver=1024*bytes_to_deliver;
		if(bytes_to_deliver<=0) bytes_to_deliver=1024*1024;
		LOG_DEBUG(" bytesToDeliver emulation=" << bytes_to_deliver << endl << "\n");
		return true;
	} else {
		bytes_to_deliver = (uint32_t)atol(const_cast<char*>(req));
		LOG_DEBUG(" bytesToDeliver simulation=" << bytes_to_deliver << endl << "\n");
		return false;
	}
	// Delete the buffer that contained the message
	delete req;
}

void HTTPServer::serveRequest(){
	//Create a socket that will listen for incoming connections
	LOG_DEBUG("creating a new socket because the previous is used!\n");
	LOG_DEBUG("tcpMaster="<<tcpMaster->getUName()<<endl)
	new SimpleSocket(tcpMaster, this, shared.listeningPort.read());
}

SSFNET_REGISTER_APPLICATION_SERVER(80,HTTPServer);

}; // namespace ssfnet
}; // namespace prime
