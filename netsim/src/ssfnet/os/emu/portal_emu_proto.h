/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file portal_emu_proto.h
 * \brief Header file for the PortalEmulation class.
 * \author Nathanael Van Vorst
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

#ifndef __PORTAL_EMU_PROTO__H__
#define __PORTAL_EMU_PROTO__H__


#include "proto/emu/emulation_protocol.h"
#ifdef SSFNET_EMULATION
#include "os/emu/emulation_device.h"
#endif

namespace prime {
namespace ssfnet {

/**
 */
class TrafficPortal: public ConfigurableEntity<TrafficPortal,EmulationProtocol> {
public:
	
public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(IPPrefix::List, networks)
	)
	SSFNET_ENTITY_SETUP( )
;

	/** The constructor. */
	TrafficPortal() { }

	/** The destructor. */
	virtual ~TrafficPortal() { }

	/**
	 * This method is called to initialize the protocol session once the
	 * protocol session has been created and configured. This method will register
	 * this emulation protocol (and the associated interface/host) with the I/O
	 * manager. The I/O manager will setup the emulation device and deliver pkts
	 * to this protocol to be inserted in the nic as if they came from the link.
	 */
	virtual void init();

	/** Called by the emulation device to send the packet as if it
	 * came from the interface this protocol is attached to.
	 */
	virtual void transmit(Packet* pkt, VirtualTime deficit=0);

	/** Called by the interface when it is being emulated so that.
	 * This function should package the packet as necessary and send it to the I/O
	 * manager to deliver the packet to the real interface.
	 *
	 * If the emulation protocol is not active (i.e. communicate with the real interface)
	 * this function should return false so it may act as pass through. When
	 * active as a pass-through the interface is effectively _not_ emulated.
	 */
	virtual bool receive(Packet* pkt, bool pkt_was_targeted_at_iface);

	virtual int getEmulationDeviceType() {
#ifdef SSFNET_EMULATION
		return EMU_TRAFFIC_PORTAL;
#else
		return 0;
#endif
	}

	IPPrefix::List& getNetworks(); //defined in portal_device.cc
};

} // namespace ssfnet
} // namespace prime

#endif /*__PORTAL_EMU_PROTO__H__*/
