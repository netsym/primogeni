/**
 * \file emu_event.h
 * \brief The header file for the EmuEvent class.
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

#ifndef __EMU_EVENT_H__
#define __EMU_EVENT_H__

#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

class Interface;
class Packet;

/**
 * \brief External emulation event.
 *
 * This class is used to represent the the event used for
 * communicating with the external device (through the special
 * emulation input and output channel).
 */
class EmulationEvent : public SSFNetEvent {
public:
	/** The default constructor. */
	EmulationEvent();

	/** The constructor with given parameters. */
	EmulationEvent(Interface* iface, Packet* pkt, bool need_to_lookup_dst);

	/** The destructor. */
	virtual ~EmulationEvent();

	/** The copy constructor. */
	EmulationEvent(const EmulationEvent&);

	bool needToLookupDest() { return need_to_lookup_dst; }

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone();

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine. It is
	 * actually used to serialize the event object into a byte stream
	 * represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/**
	 * Unpack the emulation event, reverse process of the pack method.
	 */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * This method is the factor method for deserializing the event
	 * object. A new emulation event object is created.
	 */
	static prime::ssf::Event* createEmulationEvent(prime::ssf::ssf_compact* dp);

public:
	/** The pkt that was imported */
	Packet* pkt;

	/** The UID of the virtual interface that either exports or
      imports this event. */
	UID_t src_id;

	/**
	 * The interface that either should import or export the packet
	 *
	 * If the pointer is not valid we need to look up the interface from the uid
	 */
	Interface* iface;

	/**
	 * If the dst was not local we need to a asyn lookup....
	 */
	bool need_to_lookup_dst;

#ifdef SSFNET_EMU_TIMESYNC
	double clock_drift;
	double send_time;
#endif

	/** Used by SSF to declare an event class. */
	SSF_DECLARE_EVENT(EmulationEvent);
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__EMU_EVENT_H__*/

#endif /*SSFNET_EMULATION*/
