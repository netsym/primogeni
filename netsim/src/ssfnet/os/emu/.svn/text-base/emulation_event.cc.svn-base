/**
 * \file emu_event.cc
 * \brief The header file for the EmulationEvent class.
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
#ifdef SSFNET_EMULATION

#include <assert.h>
#include "os/emu/emulation_event.h"
#include "os/packet.h"
#include "net/interface.h"

namespace prime {
namespace ssfnet {

EmulationEvent::EmulationEvent() :
		SSFNetEvent(SSFNetEvent::SSFNET_EMU_NORMAL_EVT), pkt(0), src_id(0), iface(0),need_to_lookup_dst(false)
#ifdef SSFNET_EMU_TIMESYNC
		clock_drift(0), send_time(0)
#endif
{
}

EmulationEvent::EmulationEvent(Interface* _iface, Packet* _pkt, bool need_to_lookup_dst_) :
		SSFNetEvent(SSFNetEvent::SSFNET_EMU_NORMAL_EVT), pkt(_pkt), src_id(_iface->getUID()), iface(_iface),need_to_lookup_dst(need_to_lookup_dst_)
#ifdef SSFNET_EMU_TIMESYNC
	clock_drift(0), send_time(0)
#endif
{
}

EmulationEvent::~EmulationEvent()
{
	if(pkt) pkt->erase();
}

EmulationEvent::EmulationEvent(const EmulationEvent& evt): SSFNetEvent(SSFNetEvent::SSFNET_EMU_NORMAL_EVT), pkt(0), src_id(0), iface(0),need_to_lookup_dst(false)
{
	ssfnet_throw_exception(SSFNetException::other_exception, "dont use the copy constructor, use clone!");
}

Event* EmulationEvent::clone() {
	/**
	 * The outchannel will clone this evt for thread safety. This is VERY ineffiecient....
	 * As such we will _not_ copy the packet. instead we will take it evt we are copying.
	 * I know this violates the const EmulationEvent& evt but ohh well.
	 */
	EmulationEvent* rv = new EmulationEvent(iface, pkt,need_to_lookup_dst);
	rv->setSSFNetEventType(this->getSSFNetEventType());
	iface=0;
	pkt=0;
#ifdef SSFNET_EMU_TIMESYNC
		rv->clock_drift = clock_drift;
		rv->send_time = send_time;
#endif
	return rv;
}

prime::ssf::ssf_compact* EmulationEvent::pack()
{
	if(getSSFNetEventType() == SSFNetEvent::SSFNET_EMU_PROXY_EVT) {
		ssfnet_throw_exception(SSFNetException::other_exception, "Can't pack emulation proxy events!");
	}
	if(!pkt) {
		ssfnet_throw_exception(SSFNetException::other_exception, "packing an empty packet.");
	}

	//XXX
	ssfnet_throw_exception(SSFNetException::other_exception, "ACK! im packing an emu evt");

	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_long_long(src_id);
	dp->add_unsigned_short(need_to_lookup_dst?1:0);
	#ifdef SSFNET_EMU_TIMESYNC
	dp->add_double(clock_drift);
	dp->add_double(send_time);
#endif
	pkt->pack(dp);
	return dp;
}

void EmulationEvent::unpack(prime::ssf::ssf_compact* dp)
{
	SSFNetEvent::unpack(dp);
	iface=0;
	dp->get_unsigned_long_long(&src_id);
	unsigned short temp;
	dp->get_unsigned_short(&temp);
	need_to_lookup_dst=temp==1?true:false;
#ifdef SSFNET_EMU_TIMESYNC
	dp->get_double(&clock_drift);
	dp->get_double(&send_time);
#endif
	pkt = new Packet();
	pkt->unpack(dp);
	//XXX should we reset the type?
	setSSFNetEventType(SSFNetEvent::SSFNET_EMU_NORMAL_EVT);
}

prime::ssf::Event* EmulationEvent::createEmulationEvent(prime::ssf::ssf_compact* dp)
{
	EmulationEvent* e = new EmulationEvent();
	e->unpack(dp);
	return e;
}



// used to register an event class.
SSF_REGISTER_EVENT(EmulationEvent, EmulationEvent::createEmulationEvent);

}; // namespace ssfnet
}; // namespace prime

#endif /*SSFNET_EMULATION*/
