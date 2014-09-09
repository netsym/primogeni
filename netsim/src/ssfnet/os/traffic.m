/**
 * \file traffic.h
 * \brief Header file for the Traffic class.
 * \author Nathanael Van Vorst
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

#ifndef __TRAFFIC_H__
#define __TRAFFIC_H__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/virtual_time.h"
#include "os/protocol_graph.h"
#include "net/interface.h"
#include "os/resource_identifier.h"
#include "proto/fluid/probe_message.h"

namespace prime {
namespace ssfnet {

class Community;
class TrafficManager;
class TrafficType;
class Net;
class Host;

class TrafficType;

class ProbeHandling{
public:
	/** The constructor. */
	ProbeHandling();
	/** The destructor. */
	virtual ~ProbeHandling();

	/** The probe message is used to collect outbound interfaces along bi-path. */
	virtual void handleProbe(ProbeMessage* pmsg);

};

/**
 * Base class for all traffic events
 */
class TrafficEvent: public SSFNetEvent {
public:
        /** The default constructor. */
        TrafficEvent(SSFNetEvent::Type evt_type);

        /** The constructor. */
        TrafficEvent(UID_t traffic_type_id, SSFNetEvent::Type evt_type, int target_community_id);

        /** The constructor. */
        TrafficEvent(TrafficType* traffic_type, SSFNetEvent::Type evt_type, int target_community_id);

        /** The ssf clone constructor. */
        TrafficEvent(TrafficEvent* evt);

        /** The copy constructor. */
        TrafficEvent(const TrafficEvent& evt);

        /** The destructor. */
        virtual ~TrafficEvent();

        /**
         * The uid of the traffic type that initiated the traffic.
         */
        void setTrafficTypeUID(UID_t uid);

        /**
         * The traffic type that initiated the traffic
         */
        void setTrafficType(TrafficType* tt);

        /**
         * The uid of the traffic type that initiated the traffic
         */
        UID_t getTrafficTypeUID();

        /**
         * The traffic type that initiated the traffic
         */
        TrafficType* getTrafficType();

        /**
         * return whether this event is a start,finish, or update
         */
        virtual SSFNetEvent::Type getTrafficEventType() { return SSFNetEvent::SSFNET_TRAFFIC_MIN_TYPE; }

        /**
         * This method is required by SSF for any simulation event to clone
         * itself, which is used when the simulation kernel delivers the
         * event across processor boundaries.
         */
        virtual Event* clone(){return new TrafficEvent(*this);}

        /**
         * This method is required by SSF to pack the event object before
         * the event is about to be shipped to a remote machine across the
         * memory boundaries. It is actually used to serialize the event
         * object into a byte stream represented as an ssf_compact object.
         */
        virtual prime::ssf::ssf_compact* pack();

        /** Unpack the traffic event, reverse process of the traffic method. */
        virtual void unpack(prime::ssf::ssf_compact* dp);

private:

        /**
         * A traffic event wraps around a traffic type and along with its type tells the
         * protocol what to do. The traffic type is not serialized when the event
         * crosses timelines because each timeline will have its own copy of the traffic type.
         */
        TrafficType* traffic_type;

        /**
         * Each traffic type has a unique traffic_type_id, the traffic type is a type of traffic
         * defined by src, dst, etc in the model, multiple traffic types may have the same protocol.
         */
        UID_t traffic_type_id;

};

class StartTrafficEventComparison;

class StartTrafficEvent: public TrafficEvent {
public:
        friend class StartTrafficEventComparison;
        typedef SSFNET_VECTOR(StartTrafficEvent*) Vector;
        typedef SSFNET_PRIORITY_QUEUE(StartTrafficEvent*, StartTrafficEvent::Vector, StartTrafficEventComparison) PriorityQueue;

        /** The default constructor. */
        StartTrafficEvent();

        /** The constructor. */
        StartTrafficEvent(SSFNetEvent::Type evt_type);

        /** The constructor. */
        StartTrafficEvent(SSFNetEvent::Type evt_type, UID_t trafficTypeUID, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The constructor. */
        StartTrafficEvent(SSFNetEvent::Type evt_type, TrafficType* trafficType, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The ssf clone constructor. */
        StartTrafficEvent(StartTrafficEvent* evt);

        /** The copy constructor. */
        StartTrafficEvent(const StartTrafficEvent& evt);

        /** The destructor. */
        virtual ~StartTrafficEvent();

        virtual Event* clone() {
                return new StartTrafficEvent(*this);
        }

        virtual SSFNetEvent::Type getTrafficEventType() { return SSFNetEvent::SSFNET_TRAFFIC_START_EVT; }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        /**
         * Each traffic is given an id by the traffic manager so the finish event can be recognized??
        */
        void setTrafficId(uint64_t t_id);

        /**
         * The uid of the host this event should be sent to.
         */
        void setHostUID(UID_t h_uid);

        /**
         * The uid of the destination of this traffic should be sent to.
         */
        void setDstUID(UID_t d_uid);
        
        /** 
         * The ip of the emu/real destination node.
         */
        void setDstIP(uint32_t d_ip);

        /**
         * When should the host see this start_evt
         */
        void setStartTime(VirtualTime st);

        /**
         * The community that starts this traffic.
         */
        void setSrcCommunityID(int src_community_id);

        /**
         * Each traffic is given an id by the traffic manager so the finish event can be recognized
         */
        uint64_t getTrafficId();

        /**
         * The uid of the host this event should be sent to.
         */
        UID_t getHostUID();

        /**
         * The uid of the destination of this traffic should be sent to.
         */
        UID_t getDstUID();
        
        /** 
         * The ip of the emu/real destination node.
         */
        uint32_t getDstIP();

        /**
         * When should the host see this start_evt
         */
        VirtualTime getStartTime();

        /**
         * The id of the community that creates the event.
         */
        int getSrcCommunityID();
private:
		/** traffic_id is unique for each start traffic event of a traffic type,
		 *  traffic_type_id and traffic_id are a pair to allow the finished event to
		 *  recognize the traffic start event.
		 */
        uint64_t traffic_id;
        UID_t host_uid;
        UID_t dst_uid;
        uint32 dst_ip;
        VirtualTime start_time;
        int src_community_id;
public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createStartTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(StartTrafficEvent);
};

class StartTrafficEventComparison{
public:
        StartTrafficEventComparison(const StartTrafficEventComparison& o): reverse(o.reverse){}
        StartTrafficEventComparison(const bool& revparam=false): reverse(revparam){}
        bool operator()(const StartTrafficEvent& lhs, const StartTrafficEvent& rhs) const {
                if (reverse)
                        return (lhs.start_time> rhs.start_time);
                else
                        return (lhs.start_time < rhs.start_time);
        }
private:
        bool reverse;
};

/*
 * The traffic type finished
 */
class FinishedTrafficEvent: public TrafficEvent {
public:
        /** The default constructor. */
        FinishedTrafficEvent();

        /** The constructor. */
        FinishedTrafficEvent(SSFNetEvent::Type evt_type);

        /** The constructor. */
        FinishedTrafficEvent(SSFNetEvent::Type evt_type, UID_t trafficTypeUID, int target_community_id, uint32_t id);

        /** The constructor. */
        FinishedTrafficEvent(SSFNetEvent::Type evt_type, TrafficType* trafficType, int target_community_id, uint32_t id);

        /** The ssf clone constructor. */
        FinishedTrafficEvent(FinishedTrafficEvent* evt);

        /** The copy constructor. */
        FinishedTrafficEvent(const FinishedTrafficEvent& evt);

        /** The destructor. */
        virtual ~FinishedTrafficEvent();

        virtual Event* clone() {
                return new FinishedTrafficEvent(*this);
        }

        virtual SSFNetEvent::Type getTrafficEventType() { return SSFNetEvent::SSFNET_TRAFFIC_FINISH_EVT; }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        /**
         * Each traffic is given an id by the traffic type so the finish event can be recognized
         */
        uint64_t getTrafficId();

private:
        uint64_t traffic_id;
public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createFinishedTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(FinishedTrafficEvent);
};

/**
 * users should add data that is to be updated....
 */
class UpdateTrafficTypeEvent: public TrafficEvent {
public:
	typedef SSFNET_MAP(int,SSFNET_STRING) VarIdValueMap;
        /** The default constructor. */
        UpdateTrafficTypeEvent();

        /** The constructor. */
        UpdateTrafficTypeEvent(SSFNetEvent::Type evt_type, TrafficType* trafficType, int target_community_id);

        /** The ssf clone constructor. */
        UpdateTrafficTypeEvent(UpdateTrafficTypeEvent* evt);

        /** The copy constructor. */
        UpdateTrafficTypeEvent(const UpdateTrafficTypeEvent& evt);

        /** The destructor. */
        virtual ~UpdateTrafficTypeEvent();

        virtual Event* clone(){
                return new UpdateTrafficTypeEvent(*this);
        }

        virtual SSFNetEvent::Type getTrafficEventType() { return SSFNetEvent::SSFNET_TRAFFIC_UPDATE_EVT; }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        /** get a map of the variable name id to the new value (stored as a string) **/
        VarIdValueMap& getVariableMap(){ return values; }

        /** populate the values map **/
        void populateMap();

        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createUpdateTrafficTypeEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(UpdateTrafficTypeEvent);

private:
        VarIdValueMap values;
};

class ConfigurableFactory;
class StaticTrafficType;
class DynamicTrafficType;
class CentralizedTrafficType;
class DistributedTrafficType;
class ConfigType;

/**
 * \brief A traffic type is a traffic specification.
 *
 */
class TrafficType: public ConfigurableEntity<TrafficType, BaseEntity> {
        friend class ConfigurableFactory;
        friend class StaticTrafficType;
        friend class DynamicTrafficType;
public:
        typedef SSFNET_VECTOR(TrafficType*) Vector;
        typedef SSFNET_LIST(int) CommunityIDList;

        state_configuration {
                shared configurable int traffic_type_seed {
                        type=INT;
                        default_value = "0" ;
                        doc_string = "traffic type's seed";
                };

         	   configurable CommunityIDList community_ids {
         	            type = OBJECT;
         	            default_value =  "[]" ;
         	            doc_string = "A comma separated list of the ids of the communities in this partition";
         	    };

                /**
                 * The random number generator, per traffic type. Each traffic type
                 * is expected to maintain a unique random stream, seeded initially
                 * as a function of the traffic type's UID.
                 */
                prime::rng::Random* rng = 0;

                /**
                 * If it's not zero, this is the random seed used by traffic
                 * types to initialize their random streams; if it's zero, all
                 * traffic types will shared the same random stream (rng).
                 */
                int rng_seed = 0;

        };

public:
        //so users must extend CentralizedTrafficType or DistributedTrafficType
        /** The constructor. */
        TrafficType();

        /** The destructor. */
        virtual ~TrafficType();

        /*
         * the traffic manager will call this to find out if this traffic type should be included
         */
        virtual bool shouldBeIncludedInCommunity(Community* com); //will throw exception if not overridden

        /*
         * Used by the tm get see if the traffic type needs to start events
         */
        virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correc time
                        UpdateTrafficTypeEvent*& updateEvent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
                        bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
                        VirtualTime& recall_at //when should the traffic type be recalled -- if its zero recall immediately
                        );//will throw exception if not overridden

        virtual ConfigType* getProtocolType(); //will throw exception if not overridden

        //@name Random Number Generator
        //@{

public:
        /**
         * Return the random number generator of this traffic type. The random
         * number generator for this traffic type is defined in the init method;
         * that is, one should not call this method before init.
         */
        prime::rng::Random* getRandom() {
                return unshared.rng;
        }

        /**
         * Return the random seed of this traffic type.
         */
        int getTrafficTypeSeed() {
             return unshared.rng_seed;
        }
        //@}

        /**
         * Randomly select a dst from the list
         */
        UID_t& selectUIDFromList(UIDVec& list);

        /**
         * Get the partition
         */
        Partition* getPartition();

        /**
         * Get the community id list
         */
        CommunityIDList& getCommunityIDList();

        /*
         * called when the traffic type is initially created
         */
        virtual void init();

        /*
         * called before the traffic type is destroyed
         */
        virtual void wrapup();

        /**
         * This method is called by the community to deal with the finished event.
         * It calls finishTrafficFlow() to finish the traffic flow,
         * how to differ centralized and distributed?
         */
        virtual void processFinishedEvent(FinishedTrafficEvent* finished_evt) {};
};

typedef SSFNET_VECTOR(SSFNET_STRING) DstIPVec;

template<> bool rw_config_var<SSFNET_STRING, DstIPVec> (SSFNET_STRING& dst, DstIPVec& src);
template<> bool rw_config_var<DstIPVec, SSFNET_STRING> (DstIPVec& dst, SSFNET_STRING& src);

/**
 * This class defines the traffic managed by only one traffic manager within one partition
 * (i.e one community out of all communities in the simulation).
 *
 * The static traffic type does not change any parameters during the run time, which means
 * they DO NOT process update or finish events.
 */
class StaticTrafficType: public ConfigurableEntity<StaticTrafficType, TrafficType> {
public:
		typedef SSFNET_PAIR(UID_t, UID_t) TrafficFlow; //simulated traffic flow, defined by a pair of src uid and dst uid
		typedef SSFNET_PAIR(UID_t, SSFNET_STRING) HybridTrafficFlow; //defined by a pair of src uid and dst ip
		typedef SSFNET_LIST(TrafficFlow) TrafficFlowList; //simulated traffic flow list
		typedef SSFNET_LIST(HybridTrafficFlow) HybridTrafficFlowList; //simulated traffic flow list
		

		/** Type of the source/destination mapping methods. */
		enum SrcDstMappingType {
			ALL2ALL, ///< for every src in the src list, use all dst from the dst list as its destinations (default)
			ONE2MANY, ///< for every src in the src list, use partial dsts in the dst list as its destinations.
			MANY2ONE, ///< for every dst in the dst list, use partial srcs in the src list as its sources.
			ONE2ONE ///< for every src in the src list, randomly pick dst from the dst list
		};
        state_configuration {
        	   shared configurable ResourceIdentifier srcs {
        	            type = RESOURCE_ID;
        	            default_value =  "" ;
        	            doc_string = "XXX";
        	    };
                shared configurable ResourceIdentifier dsts {
                        type = RESOURCE_ID;
                        default_value = "" ;
                        doc_string = "XXX";
                };
                shared configurable DstIPVec dst_ips {
	 					type=OBJECT;
	 					default_value="[]";
						doc_string= "a list of emu/real nodes ips";
	 			};
                shared configurable float start_time {
                        type=FLOAT;
                        default_value="0";
                        doc_string="Time before the traffic starts (default: 0).";
                };
                shared configurable float interval {
                        type=FLOAT;
                        default_value="0.1";
                        doc_string="The time between sending each traffic (default: 0.1). ";
                };
                shared configurable bool interval_exponential {
                        type=BOOL;
                        default_value="false";
                        doc_string="Whether to use exponential interval (default: false).";
                };
                shared configurable SSFNET_STRING mapping {
						type=STRING;
						default_value="all2all";
						doc_string="The method to map the source and destination based on the src and dst lists";
                };

                int mapping;
                TrafficFlowList* traffic_flows;
                HybridTrafficFlowList* hybrid_traffic_flows;
        };
public:

        /** The constructor. */
        StaticTrafficType();

        /** The destructor. */
        virtual ~StaticTrafficType();

        /*
         * Called when the traffic type is initially created
         */
        virtual void init();

        /*
         * the traffic manager will call this to find out if this traffic type should be included
         */
        bool shouldBeIncludedInCommunity(Community* com);

        /*
         * Used by the tm get see if the traffic type needs to start events
         */
        virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correc time
                        UpdateTrafficTypeEvent*& updateEvent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
                        bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
                        VirtualTime& recall_at //when should the traffic type be recalled -- if its zero recall immediately
                        ) {
                updateEvent=0;
                getNextEvent(traffics_to_start,wrap_up,recall_at);
        }

        /*
         * Used by the tm get see if the traffic type needs to start events
         */
        virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correc time
                        bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
                        VirtualTime& recall_at //when should the traffic type be recalled -- if its zero recall immediately
                        );//will throw exception if not overridden

        virtual ConfigType* getProtocolType(); //will throw exception if not overridden
        
        float getStartTime(); //return start time

        TrafficFlowList* getTrafficFlowList();
        
        HybridTrafficFlowList* getHybridTrafficFlowList();

        /**
         * 	determine which community will run traffic...
         * 	read the middle src uid and get the id of the community who owns it...
         */
        virtual int getOwningCommunityId();

        virtual void processFinishedEvent(FinishedTrafficEvent* finished_evt){};
};

/**
 * The dynamic traffic type may change the configurable parameters during the run time.
 */
class DynamicTrafficType: public ConfigurableEntity<DynamicTrafficType, TrafficType> {

public:

        /** The constructor. */
        DynamicTrafficType();

        /** The destructor. */
        virtual ~DynamicTrafficType();

        virtual void init();

        /*
         * Used by the tm get see if the traffic type needs to start events
         */
        virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correc time
                        UpdateTrafficTypeEvent*& updateEvent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
                        bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
                        VirtualTime& recall_at //when should the traffic type be recalled -- if its zero recall immediately
                        );//will throw exception if not overridden

        /**
         * This method is called by the community to process update event.
         * The vars are parsed here. virtual or not?
         */
        virtual void processUpdateEvent(UpdateTrafficTypeEvent* update_evt);

        virtual ConfigType* getProtocolType(); //will throw exception if not overridden

        /**
         * 	determine which community will run traffic...
         */
        virtual int getOwningCommunityId();

        virtual void processFinishedEvent(FinishedTrafficEvent* finished_evt);

protected:
        /**
         * The method is used to finish the traffic.
         */
        virtual void finishTraffic(uint64_t traffic_id); //will throw exception if not overridden
};

/**
 * This class defines the traffic managed by only one traffic manager within one partition
 * (i.e one community out of all communities in the simulation).
 *
 * This kind of traffic types may have update events to be processed.
 */
class CentralizedTrafficType: public ConfigurableEntity<CentralizedTrafficType, DynamicTrafficType> {

public:
        state_configuration {
        	   shared configurable ResourceIdentifier srcs {
        	            type = RESOURCE_ID;
        	            default_value =  "" ;
        	            doc_string = "XXX";
        	    };
                shared configurable ResourceIdentifier dsts {
                        type = RESOURCE_ID;
                        default_value = "" ;
                        doc_string = "XXX";
                };
        };
		/** The constructor. */
        CentralizedTrafficType();

        /** The destructor. */
        virtual ~CentralizedTrafficType();

        virtual void init();

        /*
         * the traffic manager will call this to find out if this traffic type should be included
         */
        bool shouldBeIncludedInCommunity(Community* com);

        /*
         * Used by the tm get see if the traffic type needs to start events
         */
        virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correc time
                        UpdateTrafficTypeEvent*& updateEvent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
                        bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
                        VirtualTime& recall_at //when should the traffic type be recalled -- if its zero recall immediately
                        ); //will throw exception if not overridden

        /**
         * This method is called by the community to process update event.
         */
        void processUpdateEvent(UpdateTrafficTypeEvent* update_evt);

        /**
         * This method is called by the community to deal with the finished event.
         */
        void processFinishedEvent(FinishedTrafficEvent* finished_evt);

        virtual ConfigType* getProtocolType(); //will throw exception if not overridden

        /**
         * 	determine which community will run traffic...
         */
        virtual int getOwningCommunityId();

protected:
        /**
         * The method is used to finish the traffic.
         */
        virtual void finishTraffic(uint64_t traffic_id); //will throw exception if not overridden

};

/**
 * This class defines the traffic managed by only one traffic manager per partition
 * (i.e many communites in the simulation but only one per partition). The traffic
 * manager that is in charge of the traffic for each partition is chosen arbitrarily.
 *
 * This kind of traffic types may have update events to be processed.
 */

class DistributedTrafficType: public ConfigurableEntity<DistributedTrafficType, DynamicTrafficType> {
public:
        /** The constructor. */
        DistributedTrafficType();

        /** The destructor. */
        virtual ~DistributedTrafficType();

        /*
         * the traffic manager will call this to find out if this traffic type should be included
         */
        virtual bool shouldBeIncludedInCommunity(Community* com);//will throw exception if not overridden

        /*
         * Used by the tm get see if the traffic type needs to start events
         */
        virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
                        UpdateTrafficTypeEvent*& update_vent, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
                        bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
                        VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
                        );//will throw exception if not overridden

        /**
         * This method is called by the community to process update event.
         */
        void processUpdateEvent(UpdateTrafficTypeEvent* update_evt);

        /**
         * This method is called by the community to deal with the finished event.
         */
        void processFinishedEvent(FinishedTrafficEvent* finished_evt); //will throw exception if not overridden

        virtual ConfigType* getProtocolType(); //will throw exception if not overridden

        virtual void init();

        /**
         * 	determine which community will run traffic...
         */
        virtual int getOwningCommunityId();

protected:
    /**
     * The method is used to finish the traffic
     */

    virtual void finishTraffic(uint64_t traffic_id);

};


/** Used to serialize a CommunityIDList into a string **/
template<> bool rw_config_var<SSFNET_STRING, TrafficType::CommunityIDList> (
                SSFNET_STRING& dst, TrafficType::CommunityIDList& src);

/** Used to parse a string into a CommunityIDList **/
template<> bool rw_config_var<TrafficType::CommunityIDList, SSFNET_STRING> (
                TrafficType::CommunityIDList& dst, SSFNET_STRING& src);

/**
 * Is a wrapper which holds traffic types for a network
 */
class Traffic: public ConfigurableEntity<Traffic, BaseEntity> {
public:
        typedef SSFNET_VECTOR(Traffic*) Vector;
        state_configuration {
                child_type<TrafficType> traffic_types {
                        min_count = 0;
                        max_count = 0;
                        is_aliased = false;
                        doc_string = "traffic types of the traffic";
                };
        };

        /** The constructor. */
        Traffic();

        /** The destructor. */
        virtual ~Traffic();

        virtual void init();

        virtual void wrapup() {}

        /** Get traffic types maintaind by the community com. */
        void getTrafficTypesForCommunity(Community* com, TrafficType::Vector& types);

};

} // namespace ssfnet
} // namespace prime

#endif /*__HOST_H__*/
