/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file routing_protocol.h
 * \brief Header file for the EmulationProtocol class.
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

#ifndef __EMULATION_PROTOCOL_H__
#define __EMULATION_PROTOCOL_H__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/packet.h"
#include "os/virtual_time.h"

namespace prime {
namespace ssfnet {

class Interface;
#ifdef SSFNET_EMULATION
class EmulationDevice;
#endif

/**
 * \brief A base class for all emulation protocols
 *
 * Each type of Emulation device must extend this class. No methods can or should be
 * overridden; the derived classes are used only to associate the type of emulation
 * device this emulation protocol should use.
 */
class EmulationProtocol: public ConfigurableEntity<EmulationProtocol,BaseEntity> {
public:

	/** The constructor. */
	EmulationProtocol();

	/** The destructor. */
	virtual ~EmulationProtocol();

	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(bool, ip_forwarding)
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
;

	/**
	 * This method is called to initialize the protocol session once the
	 * protocol session has been created and configured. This method will register
	 * this emulation protocol (and the associated interface/host) with the I/O
	 * manager. The I/O manager will setup the emulation device and deliver pkts
	 * to this protocol to be inserted in the nic as if they came from the link.
	 */
	virtual void init();

	/**
	 * no op
	 */
	void wrapup();

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

	/**
	 * Used by the I/O manager to register the specific emulation device this
	 * protocol should use to deliver the emulated message.
	 */
#ifdef SSFNET_EMULATION
	void setEmulationDevice(EmulationDevice* device);
#else
	void setEmulationDevice(void* device);
#endif

	/**
	 * If this protocol has an emulation device and the emulation
	 * device is active then this will return true. Otherwise false is returned.
	 */
	bool isActive();

	/**
	 * returns whether ip forwarding is enabled
	 */
	bool ipForwardingEnabled();

	/**
	 * returns the interface this is connected to
	 */
	Interface* getInterface();

	/**
	 * Used by the I/O manager to determine what kind of emulation device this emulation protocol needs.
	 *
	 * Derived classes must implement this.
	 */
	virtual int getEmulationDeviceType();

protected:
#ifdef SSFNET_EMULATION
	EmulationDevice* emuDevice;
#else
	void* emuDevice;
#endif
};

} // namespace ssfnet
} // namespace prime

#endif /*__EMULATION_PROTOCOL_H__*/
