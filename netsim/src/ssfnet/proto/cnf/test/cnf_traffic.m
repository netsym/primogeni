/**
 * \file cnf_traffic.m
 * \author Ting Li
 * \author Rong Rong
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

#ifndef __CNF_TRAFFIC_H__
#define __CNF_TRAFFIC_H__

#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "os/virtual_time.h"
#include "ssf.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

// Some value that is unique
#define CNF_TRAFFIC 5008

class CNFStartTrafficEvent: public StartTrafficEvent {
public:
        /** The default constructor. */
        CNFStartTrafficEvent();

        /** The constructor. */
        CNFStartTrafficEvent(UID_t trafficTypeUID, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime tm);

        /** The constructor. */
        CNFStartTrafficEvent(TrafficType* trafficType, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime tm);

        /** The ssf clone constructor. */
        CNFStartTrafficEvent(CNFStartTrafficEvent* evt);

        /** The copy constructor. */
        CNFStartTrafficEvent(const CNFStartTrafficEvent& evt);

        /** The destructor. */
        virtual ~CNFStartTrafficEvent();

        virtual Event* clone() {
                return new CNFStartTrafficEvent(*this);
        }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        /* The content id. */
        void setContentID(int content_id);

        /* Called by the host.*/
        int getContentID();

        /* The dst port. */
        void setDstPort(int dst_port);

        /* Called by the host.*/
        UID_t getDstPort();

private:
		int content_id;
        UID_t dst_port; //dst port may be used to distinguish different packet /query, reply, regular
public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createCNFStartTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(CNFStartTrafficEvent);
};


class CNFTraffic: public ConfigurableEntity<CNFTraffic, StaticTrafficType, CNF_TRAFFIC> {
	friend class StartTrafficEvent;
public:
	typedef SSFNET_LIST(UID_t) UIDList;
	typedef SSFNET_VECTOR(float) PopularityVec;
 public:
	state_configuration {
		shared configurable UID_t dst_port {
			type=INT;
			default_value="1000";
			doc_string="The destination port for an CNF connection.";
		};
		shared configurable int content_id {
			type=INT;
			default_value="0";
			doc_string="The contenet id request to the controller.";
		};
		shared configurable int number_of_contents {
			type=INT;
			default_value="1";
			doc_string="The total number of contents.";
		};
		shared configurable int number_of_requests {
			type=INT;
			default_value="1";
			doc_string="The total number of requests.";
		};
	};

	// The constructor
	CNFTraffic();

	// The deconstructor
	virtual ~CNFTraffic();

	ConfigType* getProtocolType();

	/*
	 * the traffic manager will call this to find out if this traffic type should be included
	 */
	virtual bool shouldBeIncludedInCommunity(Community* com);

    /*
     * called when the traffic type is initially created
     */
    virtual void init(){}

	/*
	 * Used by the tm get see if the traffic type needs to start events
	 */
	virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
			UpdateTrafficTypeEvent*& update_vent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
			);

    /**
     * The method is used to finish the traffic
     */
	virtual void processFinishedEvent(FinishedTrafficEvent* finished_evt){}

	/*
	 * Return the destination port to connect to
	 */
	UID_t getDstPort();

	/*
	 * Return the file size to request from server
	 */
	int getContentID();
	
	/*
	 * Return the popularity of the content by its cid and the total number of the contents.
	 */
	static float calPopularity(int cid, int num_of_contents);
	
	/*
	 * Return the total number of contents.
	 */
	int getNumberOfContents();
	
	/*
	 * Return the total number of requests.
	 */
	int getNumberOfRequests();
	
	/*
	 * Return the popularity list.
	 */
	static PopularityVec Zipf(float theta, int N);

 private:
	uint32_t traffic_id;
	UIDList srcs_to_use;
	static PRIME_OFSTREAM* prob_file;
	prime::rng::Random * ack_rng;
};

}
}

#endif /* __CNF_TRAFFIC_H__ */

