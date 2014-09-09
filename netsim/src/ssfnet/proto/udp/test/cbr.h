/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/*
 * \file cbr.h
 * \author Miguel Erazo
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
#ifndef __CBR_APP_H__
#define __CBR_APP_H__

#include "proto/application_session.h"
#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "proto/udp/udp_session.h"
#include "proto/udp/udp_master.h"
#include "proto/simple_socket.h"

namespace prime {
namespace ssfnet {

class CBRFlow;

class CBR : public ConfigurableEntity<CBR,ApplicationSession,
	convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_CBR)>
{
	friend class CBRFlow;

 public:
	// The constructor
	CBR();

	// The destructor
	virtual ~CBR();

	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(int, listening_port)
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(uint32_t, total_bytes_received)
		SSFNET_CONFIG_STATE_DECL(uint32_t, total_bytes_sent)
	)
	SSFNET_ENTITY_SETUP( )
;

	// This method is called to send bytes every each interval
	void startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac);

	// Called to initialize this protocol session
	virtual void init();

	// Called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();

 protected:

	// Called by the protocol session above to push a protocol message down the protocol stack
	virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra);

	// Called by the protocol session below to pop a protocol message up the protocol stack
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra);

	void startPassiveListening();

 private:

	void send(CBRFlow* flow_);

	// Pointers to other entities
	UDPMaster* udp_master;   	 // the TCP layer below this protocol
	//SimpleSocket* simpleSock;
	SimpleSocket* simple_passive_sock;

	// for statistics
	VirtualTime starting_time;
};

class CBRFlow : public Timer {
 public:

	//The constructor
	CBRFlow(CBR* cbr_, SimpleSocket* sock, uint32_t bytes_per_interval, float num_inter,
			float interval_dur, int dst_port = 5001, bool to_emulated = false);

	// The destructor
	virtual ~CBRFlow();

	void sched(double delay);
	void resched(double delay);
	void callback();
	void forceCancel();

	SimpleSocket* getSocket();
	uint32_t getBytesPerInterval();
	void setBytesPerInterval(uint32_t bytes_per_intv);
	float getNumOfIntervals();
	float getIntervalDuration();
	int getDstPort();
	bool getEmulated();
	void decreaseNumOfIntervals();

	int moreIntervals();

 private:
	CBR* cbr;
	SimpleSocket* socket;
	uint32_t bytes_per_interval;
	float num_of_intervals;
	float intv_duration;
	int dst_port;
	bool emulated;
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__CBR_APP_H__*/

