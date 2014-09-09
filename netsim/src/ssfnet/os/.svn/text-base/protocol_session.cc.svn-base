/**
 * \file protocol_session.cc
 * \brief Source file for the ProtocolSession class.
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

#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include "os/logger.h"
#include "os/protocol_session.h"
#include "os/traffic.h"
#include "net/host.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(ProtocolSession);

ProtocolGraph* ProtocolSession::inGraph() {
	return SSFNET_STATIC_CAST(ProtocolGraph*,getParent());
}

Host* ProtocolSession::inHost() {
	BaseEntity* p = getParent();
	while(p!=NULL) {
		if (Host::getClassConfigType()->isSubtype(p->getConfigType())) {
			return SSFNET_STATIC_CAST(Host*,p);
		}
		p=p->getParent();
	}
	return NULL;
}

prime::rng::Random* ProtocolSession::getRandom() {
	if (rng)
		return rng;
	else {
		int seed = inHost()->getHostSeed();
		if (!seed)
			return inHost()->getRandom();
		else {
			assert(seed != -1);
			seed = int(inHost()->getRandom()->uniform() * 54321) + 1391;
			rng = new prime::rng::Random(prime::rng::Random::USE_RNG_LEHMER,
					seed);
			return rng;
		}
	}
}

VirtualTime ProtocolSession::getNow() {
	return inHost()->getNow();
}

void ProtocolSession::startTraffic(StartTrafficEvent* evt){ delete evt; }

int ProtocolSession::setTimer(VirtualTime duration, void* extra) {
	return inHost()->setTimer(this, duration, extra);
}

int ProtocolSession::setTimerUntil(VirtualTime time, void* extra) {
	return inHost()->setTimerUntil(this, time, extra);
}

bool ProtocolSession::cancelTimer(int handle, void** extra) {
	return inHost()->cancelTimer(this, handle, extra);
}

ProtocolSession::ProtocolSession() :
	rng(0) {
}

ProtocolSession::~ProtocolSession() {
	if (rng)
		delete rng;
}

} // namespace ssfnet
} // namespace prime
