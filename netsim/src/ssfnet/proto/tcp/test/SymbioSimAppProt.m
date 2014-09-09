/**
 * \file SymbioSimAppProt.h
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
#ifndef __SYMBIOSIM_APP_PROT_H__
#define __SYMBIOSIM_APP_PROT_H__

#include "net/host.h"

#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "proto/transport_session.h"
#include "os/virtual_time.h"
#include "os/ssfnet_types.h"
#include "proto/application_session.h"
#include "proto/simple_socket.h"

namespace prime {
namespace ssfnet {

class SymbioSimTimer;
class SimpleSocket;

class SymbioSimAppProt : public ConfigurableEntity<SymbioSimAppProt, ProtocolSession, convert_protonum_to_typeid(SSFNET_PROTOCOL_SYMBIOSIM_APP)> {

 friend class SymbioSimTimer;
 	
 public:
	SymbioSimAppProt();
	
	~SymbioSimAppProt();
	
	// Called after config() to initialize this protocol session
	virtual void init();

	// Called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();
 
 private:
 	int32 checkBytesFromPhy(SymbioSimTimer* timer);
 	SymbioSimTimer* checkBytesTimer;	
 	void sendBytesToPeer(int bytes);
 	void listen();
 	
 protected:
 	TCPMaster* tcpMaster;
 	std::map<IPAddress, SimpleSocket*> ipToSocketMap; 
};

class SymbioSimTimer : public Timer {
 public:
	SymbioSimTimer(ProtocolSession* protsess) :
			Timer(protsess) {
	}

	inline void force_cancel() { cancel(); }

 protected:
	virtual void callback() {
		((SymbioSimAppProt*)getSession())->checkBytesFromPhy(this);
	}
};

}
}

#endif
