/**
 * \file nic_queue.cc
 * \brief Source file for the NicQueue class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
 * 
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

#include "net/nic_queue.h"
#include "net/host.h"
#include "net/interface.h"
#include "os/logger.h"
namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(NicQueue)

int NicQueue::type(){
	return 0;
}
bool NicQueue::enqueue(Packet* pkt, float drop_prob, bool cannot_drop) {
	double prob=inHost()->getRandom()->uniform(0.0,1.0);
	double should_drop=prob<(double) drop_prob;
	if((!cannot_drop) && (prob<(double) drop_prob)){
		pkt->erase();
		return false;
	}
	return !should_drop;
}

Interface* NicQueue::getInterface() {
	return SSFNET_STATIC_CAST(Interface*,getParent());
}

Host* NicQueue::inHost() {
	return getInterface()->inHost();
}

float NicQueue::getBitRate() {
	return getInterface()->getBitRate();
}

float NicQueue::getLatency() {
	return getInterface()->getLatency();
}

float NicQueue::getJitterRange() {
	return getInterface()->getJitterRange();
}

int NicQueue::getBufferSize() {
	return getInterface()->getBufferSize();
}

int NicQueue::getQueueSize() {
	return 0;
}

} // namespace ssfnet
} // namespace prime
