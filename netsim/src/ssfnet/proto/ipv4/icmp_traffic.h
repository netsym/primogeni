/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file icmp_traffic.h
 * \brief Header file for the ICMPTraffic class.
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

#ifndef __ICMP_TRAFFIC_H__
#define __ICMP_TRAFFIC_H__

#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "os/virtual_time.h"
#include "proto/ipv4/icmpv4_session.h"
#include "ssf.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

#define GENERIC_ICMPv4_TRAFFIC 4000 //some value that is unique....
#define ICMPv4_PING_TRAFFIC 4001 //some value that is unique....

class ICMPTraffic: public ConfigurableEntity<ICMPTraffic, StaticTrafficType, GENERIC_ICMPv4_TRAFFIC> {
	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(SSFNET_STRING, version)
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
;
public:

	/** The constructor. */
	ICMPTraffic() {}

	/** The destructor. */
	virtual ~ICMPTraffic() {}

	ConfigType* getProtocolType();

	virtual bool shouldBeIncludedInCommunity(Community* com);

	/*
	 * Used by the tm get see if the traffic type needs to start events
	 */
	virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
			UpdateTrafficTypeEvent*& update_evt, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
			);

};

class PingTraffic: public ConfigurableEntity<PingTraffic, ICMPTraffic, ICMPv4_PING_TRAFFIC> {
	friend class StartTrafficEvent;
public:
	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(uint32_t, payload_size)
		SSFNET_CONFIG_PROPERTY_DECL(uint64_t, count)
		SSFNET_CONFIG_PROPERTY_DECL(float, rtt)
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
;

	/** The constructor. */
	PingTraffic();

	/** The destructor. */
	virtual ~PingTraffic() {
	};

	/*
	 * the traffic manager will call this to find out if this traffic type should be included
	 */
	virtual bool shouldBeIncludedInCommunity(Community* com);

	/*
	 * Used by the tm get see if the traffic type needs to start events
	 */
	virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
			UpdateTrafficTypeEvent*& update_vent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
			);

	/**
	 * This method returns the size of the payload.
	 */
	uint32_t getPayloadSize();

private:
	uint64_t traffic_id;
};

} // namespace ssfnet
} // namespace prime

#endif /*__ICMP_TRAFFIC_H__*/
