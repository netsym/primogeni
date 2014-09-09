/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file tap_emu_proto.h
 * \brief Header file for the TAPEmulation class.
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

#ifndef __TAP_EMU_PROTO__H__
#define __TAP_EMU_PROTO__H__


#include "proto/emu/emulation_protocol.h"
#ifdef SSFNET_EMULATION
#include "os/emu/emulation_device.h"
#endif

namespace prime {
namespace ssfnet {

/**
 */
class TAPEmulation: public ConfigurableEntity<TAPEmulation,EmulationProtocol> {
public:

	/** The constructor. */
	TAPEmulation() { }

	/** The destructor. */
	virtual ~TAPEmulation() { }

	virtual int getEmulationDeviceType() {
#ifdef SSFNET_EMULATION
		return EMU_TAP_DEVICE;
#else
		return 0;
#endif
	}

public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
};

} // namespace ssfnet
} // namespace prime

#endif /*__TAP_EMU_PROTO__H__*/
