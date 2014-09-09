/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file placeholder.h
 * \brief Header file for the placeholder class.
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

#ifndef __PLACEHOLDER__
#define __PLACEHOLDER__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"

namespace prime {
namespace ssfnet {

/**
 * \brief used to hold the place of hosts/subnets that are in another partition....
 *
 * XXX
 */
class PlaceHolder: public ConfigurableEntity<PlaceHolder, BaseEntity> {
public:
	/** The constructor. */
	PlaceHolder() { }

	/** The destructor. */
	virtual ~PlaceHolder() { }

	/**
	 * The init method is used to initialize the alias once it has been
	 * created and configured by model builder.
	 */
	virtual void init() { }

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * used to collect statistical information of the simulation run.
	 */
	virtual void wrapup() { }

	/**
	 * Get the node which this alias is a shortcut to.
	 */
	virtual BaseEntity* deference() { return this; }


public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
};

} // namespace ssfnet
} // namespace prime

#endif /*__PLACEHOLDER__*/
