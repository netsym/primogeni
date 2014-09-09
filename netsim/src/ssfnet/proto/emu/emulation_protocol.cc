/**
 * \file emulation_protocol.cc
 * \brief Definition file for the EmulationProtocol class.
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

#include "os/logger.h"
#include "os/emu/emulation_device.h"
#include "proto/emu/emulation_protocol.h"
#include "net/interface.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(EmulationProtocol);

EmulationProtocol::EmulationProtocol(): emuDevice(0) {}

EmulationProtocol::~EmulationProtocol() {}

void EmulationProtocol::init() {
#ifdef SSFNET_EMULATION
	if(!emuDevice) {
		LOG_ERROR(getUName()<<"  never got their emulation device!"<<endl)
	}
#endif
}

void EmulationProtocol::wrapup() {
//no op
}

void EmulationProtocol::transmit(Packet* pkt, VirtualTime deficit) {
	if(deficit != 0) {
		//XXX dont handle deficit yet
		LOG_WARN("dont handle deficit yet"<<endl);
		deficit=0;
	}
	//getInterface()->transmit(pkt,deficit);
	getInterface()->enqueueEmuPkt(pkt, deficit);
}

bool EmulationProtocol::receive(Packet* pkt, bool pkt_was_targeted_at_iface) {
#ifdef SSFNET_EMULATION
	LOG_DEBUG(getUName()<<" emu proto pkt_was_targeted_at_iface="<<(pkt_was_targeted_at_iface?"true":"false")<<
			", ipForwardingEnabled()="<<(ipForwardingEnabled()?"true":"false")<<", isActive()="<<(isActive()?"true":"false")<<endl);
	if(isActive()) {
		//XXX pkt_was_targeted_at_iface not working!
		//if(pkt_was_targeted_at_iface || ipForwardingEnabled()) {
			LOG_DEBUG("exporting packet from iface "<<getInterface()->getUName()<<endl);
			//LOG_DEBUG("pkt->size()="<<pkt->size()<<endl);
			emuDevice->exportPacket(getInterface(),pkt);
			return true;
		//}
	}
	//XXX
	LOG_DEBUG(getUName()<<" emu proto did not take packet! "<<endl);
#endif
	return false;
}

#ifdef SSFNET_EMULATION
void EmulationProtocol::setEmulationDevice(EmulationDevice* device) {
#else
void EmulationProtocol::setEmulationDevice(void* device) {
#endif
	this->emuDevice=device;
}

bool EmulationProtocol::isActive() {
#ifdef SSFNET_EMULATION
	if(emuDevice) {
		return emuDevice->isActive();
	}
#endif
	return false;
}

Interface* EmulationProtocol::getInterface() {
	return (Interface*)getParent();
}


bool EmulationProtocol::ipForwardingEnabled() {
	return shared.ip_forwarding.read();
}

int EmulationProtocol::getEmulationDeviceType() {
	return -1;
}


} // namespace ssfnet
} // namespace prime

