/**
 * \file SymbioSimAppProt.cc
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
#include <iostream>
#include <fstream>
#include <string>

#include <stdio.h>
#include <stdlib.h>
#include "SymbioSimAppProt.h"

#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(SymbioSimAppProt);

SSFNET_REGISTER_APPLICATION_SERVER(9999, SymbioSimAppProt);

SymbioSimAppProt::SymbioSimAppProt():
		tcpMaster(NULL){
	printf("A symbiosim app protocol was created!\n");
	//LOG_WARN("A symbiosim app protocol was created!\n");
}

SymbioSimAppProt::~SymbioSimAppProt(){

}

// Called after config() to initialize this protocol session
void SymbioSimAppProt::init(){
	checkBytesTimer = new SymbioSimTimer(this);
	checkBytesTimer->set(VirtualTime(0.001, VirtualTime::SECOND));
	if(!tcpMaster) {
		// Get a pointer to the protocol session below
		tcpMaster = (TCPMaster*)inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_TCP);
		if(!tcpMaster) LOG_ERROR("missing TCP session.");
	}

	//Create a passive socket
	listen();
}

// Called before the protocol session is reclaimed upon the end of simulation
void SymbioSimAppProt::wrapup(){}

int32 SymbioSimAppProt::checkBytesFromPhy(SymbioSimTimer* timer){
	//printf("time to check the file for bytes at %s\n", this->inHost()->getDefaultIP());
	LOG_WARN("time to check the file for bytes at " << this->inHost()->getDefaultIP() << endl);
	int32 bytes = 0;
	string line;
	string my_ip = this->inHost()->getDefaultIP().toString();
	FILE* bytes_file = NULL;

	//XXX this is static
	if(strcmp(my_ip.c_str(), "192.1.0.3")){
		bytes_file = fopen("/usr/local/primo/vz/emuhostdata/192.1.0.38", "r");
		if(bytes_file == NULL) {
			//No more data to send, schedule the timer again
			printf("no more data to send\n");
			checkBytesTimer->set(VirtualTime(1, VirtualTime::SECOND));
			return 0;
		}
		char mystring [100];
		if (bytes_file == NULL){  LOG_ERROR ("Error opening file");}
		else {
			while(fgets (mystring , 100 , bytes_file) != NULL){
				bytes += atoi(mystring);
				printf("\t***CURSEQ we read %d from file\n", bytes);
			}
			printf("\t\t***CURSEQ we read total bytes from %d from file\n", bytes);
			fclose (bytes_file);
		}

		//send the bytes to peer
		sendBytesToPeer(bytes);

		//Remove file now
		FILE* fp = popen("rm /usr/local/primo/vz/emuhostdata/192.1.0.38", "r");
		if (fp == 0) {
			// Could not open file
			fprintf(stderr, "Could not erase file\n");
			return 1;
		} else {
			printf("file erase successfully\n");
		}

		//Schedule the timer again
		checkBytesTimer->set(VirtualTime(1, VirtualTime::SECOND));
	}
	return 0;
}

void SymbioSimAppProt::sendBytesToPeer(int bytes){
	//XXX this must be conveyed from the Physical System
	string my_ip = this->inHost()->getDefaultIP().toString();
	IPAddress destination;

	if(!strcmp(my_ip.c_str(), "192.1.0.3")){
		//IPAddress dest = "192.1.0.25";
		destination = "192.1.0.25";
		printf("\t*** my ip=%s my destination:%s bytes=%d\n", this->inHost()->getDefaultIP().toString().c_str(),
				destination.toString().c_str(), bytes);
	} else if(!strcmp(my_ip.c_str(), "192.1.0.25")){
		destination = "192.1.0.3";
		printf("\t*** my destination:%s bytes=%d\n", destination.toString().c_str(), bytes);
	}

	//Check that we have a pointer to the TCP layer below
	assert(tcpMaster);

	//Knowing the destination, create a new socket
	//XXX 9999 is used here as the port for symbiotic simulation protocol
	std::map<IPAddress, SimpleSocket*>::iterator iter = ipToSocketMap.find(destination);
	if(iter != ipToSocketMap.end()){
		iter->second->send(bytes);
	} else {
		SimpleSocket* simple_sock = new SimpleSocket(tcpMaster, this, destination, 9999);
		ipToSocketMap.insert(std::pair<IPAddress, SimpleSocket*>(destination, simple_sock));
		simple_sock->send(bytes);
	}
}

void SymbioSimAppProt::listen(){
	//Create a socket that will listen for incoming connections
	printf("creating a new socket because the previous is used!\n");
	new SimpleSocket(tcpMaster, this, 9999);
}

}
}
