/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file application_session.h
 * \brief Header file for the ApplicationSession class.
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

#ifndef __APPLICATION_SESSION_H__
#define __APPLICATION_SESSION_H__

#include "os/protocol_session.h"
#include "os/ssfnet.h"

namespace prime {
namespace ssfnet {

#define SSFNET_REGISTER_APPLICATION_SERVER(port, application)\
	int __reg_port_##application = ApplicationSession::registerApplicationServer(port,application::getClassConfigType)

class RegisteredApp {
public:
	typedef SSFNET_LIST(RegisteredApp) AppList;
	int port;
	ConfigType* (*getClassConfigType)();
	RegisteredApp(const RegisteredApp& o): port(o.port), getClassConfigType(o.getClassConfigType) { }
	RegisteredApp(int port_, ConfigType* (*getClassConfigType_)()): port(port_), getClassConfigType(getClassConfigType_) { }
};

/**
 * \brief A wrapper for all application protocols
 */
class ApplicationSession: public ConfigurableEntity<ApplicationSession,ProtocolSession> {
 public:
	typedef SSFNET_MAP(int,ConfigType*) PortAppMap;
	friend class Partition;
  /** The constructor. */
  ApplicationSession();

  /** The destructor. */
  virtual ~ApplicationSession();

  /**
   * This method is called by the owning host to start traffic of a particular type.
   *
   * This method does NOT need to be overridden unless additional functionality is desired
   */
  virtual void startTraffic(StartTrafficEvent* evt);

  /**
   * This method is called by the startTraffic(StartTrafficEvent* evt) host to start traffic of a particular type.
   *
   * startTraffic(StartTrafficEvent* evt) takes care of asynchronous lookups
   *
   * Application sessions must override this function.
   *
   */
  virtual void startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac);

  /**
   * This method is called when an ip message is pushed down from a
   * protocol above at the local host.
   *
   * Application sessions must override this function.
   *
   */
  virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0)  { return -1; }

  /**
   * This method is called when an ip message is popped up by a local
   * network interface.
   *
   * Application sessions must override this function.
   *
   */
  virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0)  { return -1; }

  /**
   * used to register application types with port numbers so tcp/udp can create the correct app types
   */
  static int registerApplicationServer(int port, ConfigType* (*getClassConfigType)());

  /*
   * Given a port number return the config type id of the default application
   */
  static int getApplicationProtocolType(int port);

 private:

  static void init_applications();

  static PortAppMap* port_map;
  static RegisteredApp::AppList* apps_to_reg;

public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
};

} // namespace ssfnet
} // namespace prime

#endif /*__APPLICATION_SESSION_H__*/
