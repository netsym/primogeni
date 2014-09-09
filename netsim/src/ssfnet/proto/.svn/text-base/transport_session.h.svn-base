/**
 * \file transport_session.h
 * \brief Header file for the TransportSession class.
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

#ifndef __TRANSPORT_SESSION_H__
#define __TRANSPORT_SESSION_H__

#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

class SimpleSocket;

/**
 * \brief A base class for tcp/udp like sessions
 */
class TransportSession {
 public:

  /** The constructor. */
	TransportSession() { }

  /** The destructor. */
  virtual ~TransportSession() { }

  virtual bool activeOpen() { return false; }
  virtual bool passiveOpen() { return false; }
  virtual void send(uint64_t bytes_);
  virtual void sendMsg(uint64_t size, byte* msg, bool send_data_and_disconnect=false);
  virtual int getID();
};


/**
 * \brief A base class for transport protocols like tcp/udp
 */
class TransportMaster {
public:
	/** The constructor. */
	TransportMaster() { }
	/** The destructor. */
	virtual ~TransportMaster() { }

	//Create a listening session
	virtual TransportSession* createActiveSession(SimpleSocket* sock_,
			int local_port,
			IPAddress remote_ip, int remote_port);

	//Create a listening session
	virtual TransportSession* createListeningSession(SimpleSocket* sock,
			IPAddress listen_ip, int listen_port);

	// Reclaim a session
	virtual void deleteSession(TransportSession* session);


	//get the next autoassigned source port
	virtual int nextSourcePort();
};



} // namespace ssfnet
} // namespace prime

#endif /*__TRANSPORT_SESSION_H__*/

