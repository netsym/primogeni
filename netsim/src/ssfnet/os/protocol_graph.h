/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file protocol_graph.h
 * \brief Header file for the ProtocolGraph class.
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

#ifndef __PROTOCOL_GRAPH_H__
#define __PROTOCOL_GRAPH_H__

#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

class ProtocolSession;

/**
 * \brief A stack of protocol sessions.
 *
 * The ProtocolGraph class represents a protocol graph, which is an
 * abstraction of the protocol stack in a host or router, in a fashion
 * similar to the ISO/OSI stack model. A protocol graph maintains a
 * list of protocol sessions, representing protocols running at
 * different layers. It serves as a container for the protocol
 * sessions.
 */
class ProtocolGraph: public ConfigurableEntity<ProtocolGraph, BaseEntity> {
	friend class ProtocolSession;

	typedef SSFNET_MAP(SSFNET_STRING, ProtocolSession*) PROTONAME_MAP;
	typedef SSFNET_MAP(int, ProtocolSession*) PROTONUM_MAP;
	typedef SSFNET_VECTOR(ProtocolSession*) PSESS_VECTOR;

	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(bool, automatic_protocol_session_creation)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(ProtocolSession, cfg_sess)
	)
	SSFNET_STATEMAP_DECL(
		bool initialized;
		PROTONAME_MAP pname_map;
		PROTONUM_MAP pno_map;
		PSESS_VECTOR protocol_list;
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(ProtocolGraph,ProtocolSession,cfg_sess)
	)
	SSFNET_CONFIG_CHILDREN_DECL(ProtocolSession, cfg_sess)
	SSFNET_ENTITY_SETUP(&(unshared.cfg_sess) )

public:
	/** The constructor. */
	ProtocolGraph();

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/**
	 * The init method is used to initialize the protocol graph once it
	 * has been configured. The init method will call init methods of
	 * all protocol sessions currently defined in the protocol
	 * graph. The order in which protocol sessions are initialized
	 * should NOT be significant. The derived class can override this
	 * method, in which it needs to call this method at the base class
	 * explicitly.
	 */
	virtual void init();

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * used to collect statistics of the simulation run.  The
	 * wrapup method in turn calls the wrapup methods of all
	 * protocol sessions defined in the protocol graph.  The order
	 * in which protocol sessions wrap up should NOT be
	 * significant. The derived class can override this method, in
	 * which it needs to call this method at the base class
	 * explicitly.
	 */
	virtual void wrapup();

	/**
	 * If a protocol session has been created and configured under the
	 * given name, the method returns the protocol session. Otherwise,
	 * the method creates and returns a new protocol registered under
	 * the given name. If the protocol name is not registered, a NULL is
	 * returned.
	 */
	ProtocolSession* sessionForName(const SSFNET_STRING& pname);

	/**
	 * If a protocol session has been created and configured under the
	 * given protocol number, the method returns the protocol session.
	 * Otherwise, the method creates and returns a new protocol
	 * registered under the given protocol number. If the protocol
	 * number is not registered, a NULL is returned.
	 */
	ProtocolSession* sessionForNumber(int pno, bool create=true);

	/**
	 * whether protocol sessions should be created on demand
	 */
	bool automaticallyCreateProtocolSessions();

protected:
	/** The destructor. */
	virtual ~ProtocolGraph();

	/** Insert a protocol session to this protocol graph. */
	void insert_session(ProtocolSession* psess);
};

} // namespace ssfnet
} // namespace prime

#endif /*__PROTOCOL_GRAPH_H__*/
