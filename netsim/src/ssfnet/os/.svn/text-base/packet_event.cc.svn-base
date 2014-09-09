/**
 * \file packet_event.cc
 * \brief Source file for the PacketEvent class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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
#include <string.h>
#include "os/logger.h" 
#include "os/protocol_message.h"
#include "os/packet_event.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(PacketEvent);

PacketEvent::PacketEvent() : SSFNetEvent(SSFNetEvent::SSFNET_PKT_EVT), packet(0),tuid(0) {}

PacketEvent::PacketEvent(Packet* pkt, int dest_align_id, UID_t _tuid):
		SSFNetEvent(SSFNetEvent::SSFNET_PKT_EVT,dest_align_id), packet(pkt), tuid(_tuid) {}

PacketEvent::PacketEvent(const PacketEvent& pktevt) :
			SSFNetEvent(pktevt), tuid(pktevt.tuid)
{
	if(pktevt.packet) packet = pktevt.packet->clone();
	else packet = 0;
}

PacketEvent::~PacketEvent()
{
	if(packet) packet->erase();
}

Packet* PacketEvent::dropPacket()
{
	Packet* pkt = packet;
	packet = 0;
	return pkt;
}

prime::ssf::ssf_compact* PacketEvent::pack()
{
	if(!packet) LOG_ERROR("Packing an empty packet.");
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_long_long(tuid);
	//LOG_DEBUG("Packing PacketEvent, tuid="<<tuid<<endl);
	packet->pack(dp);
	return dp;
}

void PacketEvent::unpack(prime::ssf::ssf_compact* dp)
{
	SSFNetEvent::unpack(dp);
	dp->get_unsigned_long_long(&tuid);
	//LOG_DEBUG("Unpacking PacketEvent, tuid="<<tuid<<endl);
	packet = new Packet();
	packet->unpack(dp);
}

prime::ssf::Event* PacketEvent::createPacketEvent
(prime::ssf::ssf_compact* dp)
{
	PacketEvent* pktevt = new PacketEvent();
	pktevt->unpack(dp);
	return pktevt;
}

// ssf requires this macro to register an event class.
SSF_REGISTER_EVENT(PacketEvent, PacketEvent::createPacketEvent);

} // namespace ssfnet
} // namespace prime
