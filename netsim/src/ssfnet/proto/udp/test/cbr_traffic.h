/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/*
 * \file cbr_traffic.h
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

#ifndef __CBR_TRAFFIC_H__
#define __CBR_TRAFFIC_H__

#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "os/virtual_time.h"
#include "ssf.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

// Some value that is unique....
#define UDP_TRAFFIC 5002

class UDPTraffic: public ConfigurableEntity<UDPTraffic, StaticTrafficType, UDP_TRAFFIC> {
	friend class StartTrafficEvent;
 public:
	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(uint32_t, dstPort)
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(uint32_t, bytesToSendEachInterval)
		SSFNET_CONFIG_STATE_DECL(uint32_t, count)
		SSFNET_CONFIG_STATE_DECL(bool, toEmulated)
	)
	SSFNET_ENTITY_SETUP( )
;

	// The constructor
	UDPTraffic();

	// The destructor
	virtual ~UDPTraffic() {
	};

	ConfigType* getProtocolType();

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
	int getDstPort();
	int getBytesToSendEachInterval();
	bool getToEmulated();
	int getCount();
	float getInterval();

 private:
	int traffic_id;
};

}
}

#endif /* __CBR_TRAFFIC_H__ */

