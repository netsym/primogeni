/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/*
 * \file ppbp_traffic.h
 * \author Ting Li
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

#ifndef __PPBP_TRAFFIC_H__
#define __PPBP_TRAFFIC_H__

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
#define PPBP_TRAFFIC 5003

class PPBPStartTrafficEvent: public StartTrafficEvent {
public:
        /** The default constructor. */
	    PPBPStartTrafficEvent();

        /** The constructor. */
	    PPBPStartTrafficEvent(UID_t trafficTypeUID, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The constructor. */
	    PPBPStartTrafficEvent(TrafficType* trafficType, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The ssf clone constructor. */
	    PPBPStartTrafficEvent(PPBPStartTrafficEvent* evt);

        /** The copy constructor. */
	    PPBPStartTrafficEvent(const PPBPStartTrafficEvent& evt);

        /** The destructor. */
        virtual ~PPBPStartTrafficEvent();

        virtual Event* clone() {
                return new PPBPStartTrafficEvent(*this);
        }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        /* The number of bytes to send per interval. */
        void setBytesToSendPerIntv(uint32_t bytes_per_intv);

        /* Called by the client to get the bytes to send per interval. */
        uint32_t getBytesToSendPerIntv();

        /* Set the number of intervals, which is equal to duration/interval. Duration is Pareto distributed. */
        void setNumOfIntervals(float num_of_intv);

        /* Return the number of intervals. */
        float getNumOfIntervals();

        void setInterval(float interval);
        float getInterval();

private:
	    uint32_t bytes_to_send_per_interval; //The unique session id.
		float num_of_intervals; //session's duration/interval
		float interval;
public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createPPBPStartTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(PPBPStartTrafficEvent);
};


class PPBPTraffic: public ConfigurableEntity<PPBPTraffic, StaticTrafficType, PPBP_TRAFFIC> {
	friend class StartTrafficEvent;
 public:
	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(uint32_t, dst_port)
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(uint32_t, bytes_to_send_per_interval)
		SSFNET_CONFIG_STATE_DECL(uint32_t, avg_sessions)
		SSFNET_CONFIG_STATE_DECL(float, hurst)
		SSFNET_CONFIG_STATE_DECL(float, stop)
	)
	SSFNET_ENTITY_SETUP( )
;

	// The constructor
	PPBPTraffic();

	// The destructor
	virtual ~PPBPTraffic() {
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

 private:
	 uint32_t traffic_id;
};

}
}

#endif /* __PPBP_TRAFFIC_H__ */

