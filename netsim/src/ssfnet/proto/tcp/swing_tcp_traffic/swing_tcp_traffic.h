/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file swing_tcp_traffic.h
 * \brief Header file for the SwingTCPTraffic class.
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

#ifndef __SWING_TCP_TRAFFIC_H__
#define __SWING_TCP_TRAFFIC_H__

#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "os/virtual_time.h"
#include "ssf.h"
#include "os/ssfnet_types.h"
#include "swing_client.h"

namespace prime {
namespace ssfnet {

#define TRAFFIC_TYPE 2
#define TRAFFIC_DIRECTION 2
#define PARAMS 11

#define SWING_TCP_TRAFFIC 4101 //some value that is unique....

class SwingFlow;
class SwingAction;
class SwingSession;
class SwingRRE;
class SwingConnection;
class SwingPair;
class FlowDescription;

typedef SSFNET_PAIR(SwingRRE*, int) RRETypeNum;
typedef SSFNET_PAIR(SwingConnection*, int) ConnectionTypeNum;
typedef SSFNET_PAIR(VirtualTime, SwingAction*) IntvAction;

class SwingStartTrafficEvent: public StartTrafficEvent {
public:
        /** The default constructor. */
        SwingStartTrafficEvent();

        /** The constructor. */
        SwingStartTrafficEvent(UID_t trafficTypeUID, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The constructor. */
        SwingStartTrafficEvent(TrafficType* trafficType, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The ssf clone constructor. */
        SwingStartTrafficEvent(SwingStartTrafficEvent* evt);

        /** The copy constructor. */
        SwingStartTrafficEvent(const SwingStartTrafficEvent& evt);

        /** The destructor. */
        virtual ~SwingStartTrafficEvent();

        virtual Event* clone() {
                return new SwingStartTrafficEvent(*this);
        }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        /* The unique session id, which is the traffic id of the first pair of this session. */
        void setSessionID(uint32_t session_id);

        /* The unique rre id, which is the traffic id of the first pair of this rre. */
        void setRreID(uint32_t rre_id);

        /* Called by the client to maintain the session socket map, which is used to close the socket when session timeout. */
        uint32_t getSessionID();

        /* Called by the client to maintain the rre socket map, which is used to close the socket when rre timeout. */
        uint32_t getRreID();

        /* The size of the request in bytes. */
        void setRequestSize(int req);

        /* The size of the response in bytes. */
        void setResponseSize(int rsp);

        /* Return the request size. */
        int getRequestSize();

        /* Return the response size.*/
        int getResponseSize();

        /* Set the port for dst. */
        void setDstPort(int port);

        /* Return the dst port. */
        int getDstPort();

        /* For monitor purpose, set the type/direction mark of the traffic flow. */
        void setFlowType(int type);

        /* Return the type/direction mark of the traffic flow. */
        int getFlowType();

private:
        uint32_t session_id; //The unique session id.
        uint32_t rre_id; //The unique rre id.
        int req_size; //request size
        int rsp_size; //response size
        int dst_port; //dst port
        int flow_type; //mark the type/direction combination of the traffic flow

public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createSwingStartTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(SwingStartTrafficEvent);
};

class SwingFinishedTrafficEvent: public FinishedTrafficEvent {
public:
        /** The default constructor. */
        SwingFinishedTrafficEvent();

        /** The constructor. */
        SwingFinishedTrafficEvent(UID_t trafficTypeUID, int target_community_id, uint32_t id, uint32_t s_id, uint32_t r_id, float tm);

        /** The constructor. */
        SwingFinishedTrafficEvent(TrafficType* trafficType, int target_community_id, uint32_t id, uint32_t s_id, uint32_t r_id, float tm);

        /** The ssf clone constructor. */
        SwingFinishedTrafficEvent(SwingFinishedTrafficEvent* evt);

        /** The copy constructor. */
        SwingFinishedTrafficEvent(const SwingFinishedTrafficEvent& evt);

        /** The destructor. */
        virtual ~SwingFinishedTrafficEvent();

        virtual Event* clone() {
                return new SwingFinishedTrafficEvent(*this);
        }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        void setSessionID(uint32_t s_id);

        void setRreID(uint32_t r_id);

        uint32_t getSessionID();

        uint32_t getRreID();

        float getTimeout();

private:
        uint32_t session_id;
        uint32_t rre_id;
        float timeout;
public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createSwingFinishedTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(SwingFinishedTrafficEvent);
};

class IntvActionComparison{
public:
	IntvActionComparison(const IntvActionComparison& o): reverse(o.reverse){}
	IntvActionComparison(const bool& revparam=false): reverse(revparam){};
	bool operator()(const IntvAction& lhs, const IntvAction& rhs) const {
		if (reverse)
			return (lhs.first > rhs.first);
		else
		    return (lhs.first < rhs.first);

	}
private:
	bool reverse;
};

class SwingTCPTraffic: public ConfigurableEntity<SwingTCPTraffic, CentralizedTrafficType, SWING_TCP_TRAFFIC> {
	friend class StartTrafficEvent;

public:
	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(SSFNET_STRING, trace_description)
		SSFNET_CONFIG_PROPERTY_DECL(bool, stretch)
		SSFNET_CONFIG_PROPERTY_DECL(float, session_timeout)
		SSFNET_CONFIG_PROPERTY_DECL(float, rre_timeout)
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
;

public:
	typedef SSFNET_PRIORITY_QUEUE(IntvAction,
				SSFNET_VECTOR(IntvAction),
		        IntvActionComparison) MinHeapOfActions;
	typedef SSFNET_MAP(uint32_t, SwingSession*) IDSessionMap;
	typedef SSFNET_MAP(uint32_t, SwingRRE*) IDRreMap;

	/** The constructor. */
	SwingTCPTraffic();

	/** The destructor. */
	virtual ~SwingTCPTraffic();

	enum TrafficType{
		TRAFFIC_TYPE_HTTP,
		TRAFFIC_TYPE_TCP
	};

	enum TrafficDirection{
		TRAFFIC_DIRECTION_LEFT_RIGHT,
		TRAFFIC_DIRECTION_RIGHT_LEFT
	};

	enum StretchType{
	    STRETCH_TYPE_RTT,
	    STRETCH_TYPE_LOSS_RATE,
	    STRETCH_TYPE_BANDWIDTH,
	    STRETCH_TYPE_PKT_SIZ,
	    STRETCH_TYPE_SESS_INTV,
	    STRETCH_TYPE_RRE_INTV,
	    STRETCH_TYPE_CONNECTION_INTV,
	    STRETCH_TYPE_REQSIZE,
	    STRETCH_TYPE_RSPSIZE,
	    STRETCH_TYPE_THINK_TIME
	};

	ConfigType* getProtocolType();

	/*
	 * The traffic manager will call this to find out if this traffic type should be included
	 */
	virtual bool shouldBeIncludedInCommunity(Community* com);

	/*
	 * Used by the tm get see if the traffic type needs to start events
	 */
	virtual void getNextEvent(StartTrafficEvent*& traffics_to_start, //the tm will send this to the correct host at the correcT time
			UpdateTrafficTypeEvent*& update_evt, //if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up, //if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at //when should the traffic type be recalled -- may need to update its current recall time. if its zero don't change it
			);
	/**
	 * XXX
	 * If there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
	 *
	 */
	virtual void processUpdateEvent(UpdateTrafficTypeEvent* update_evt);

    /**
     * The method is used to finish the traffic
     */
	virtual void processFinishedEvent(FinishedTrafficEvent* finished_evt);

    /*
     * called when the traffic type is initially created
     */
    virtual void init();

	void stretch(int stretch_type, int traffic_type, int traffic_direction, float factor);

	/*
	 * returns the session timeout time.
	 */
	float getSessionTimeout();

	/*
	 * returns the rre timeout time.
	 */
	float getRreTimeout();

    /*
     * Set the parameters in the start traffic event
     */
	void setStartTrafficEvent(StartTrafficEvent*& traffics_to_start, VirtualTime cur, SwingPair* sp);

	FlowDescription* tpf[TRAFFIC_TYPE][TRAFFIC_DIRECTION];

	SwingFlow* sf[TRAFFIC_TYPE][TRAFFIC_DIRECTION];

private:
	uint32_t traffic_id;
	uint32_t session_id;
	uint32_t rre_id;
	MinHeapOfActions heap_actions;
	char* file_summary[TRAFFIC_TYPE][TRAFFIC_DIRECTION][PARAMS];
	void readTrafficSummary(SSFNET_STRING file); //read the directory and name information of the files generated by swing
	void loadTrafficSummary(); //load data from file into data structure according to the traffic type and traffic direction
	IDSessionMap id_session;  //mapping from session id to session
	IDRreMap id_rre;  //mapping from rre id to rre
};

class SwingFlow {
public:
	typedef SSFNET_MAP(int,UIDVec) TypeUIDMap;
	SwingFlow(SwingTCPTraffic* st, int traffic_type, int traffic_direction);
	virtual ~SwingFlow();
	SwingTCPTraffic* getSwingTraffic(); //returns the swing traffic which the session belongs to
	float getSessionInterval(); // returns session inter-arrival time
	int t_type;
	int t_direction;
	FlowDescription* tpf_sf;

    TypeUIDMap typeClientUID;
    TypeUIDMap typeServerUID;

  private:
	SwingTCPTraffic* swing_traffic;
};

class SwingAction{
public:
	enum ActionType {
		SESSION=99,
		RRE,
		CONNECTION,
		PAIR
	};
	SwingAction(){}
	virtual ~SwingAction(){}
	virtual ActionType getAction()=0;
};

class SwingSession : public SwingAction {
  public:
	SwingSession(SwingFlow* sf);
	virtual ~SwingSession();
	SwingFlow* getSwingFlow(); //returns the swing flow which the session belongs to
	int getClient();  // returns the client id associated with the session
	int getNumRREs(); // returns the total number of RREs of this session
	void setNumRemainingRRE(int r); // set the # of remaining rres swing_session needs to generate by 1
	int getNumRemainingRRE(); //returns the number of the unscheduled RREs of this session
	float getRREInterval(); // returns the interval between the RREs
	virtual ActionType getAction(); //returns the action type
	uint32_t getSessionID(); //returns the session id
	void setSessionID(uint32_t s_id); //set the session id
	int client_id;
	SSFNET_LIST(SwingRRE*) swing_rre_list;
  private:
	SwingFlow* swing_flow;
	uint32_t session_id;
	int remaining_rre;
};

class SwingRRE : public SwingAction {
  public:
	SwingRRE(SwingSession* ss);
	virtual ~SwingRRE();
	SwingSession* getSwingSession(); //returns the swing session which the RRE belongs to
	void deleteSwingSession(); //set swing_session to be null
	int getNumConnections(); // returns the number of connections within the RRE
	void setNumRemainingConn(int c); // set the # of remaining connections this RRE needs to generate by 1
	int getNumRemainingConn(); //returns the number of the unscheduled connections of this RRE
	float getConnectionInterval(); // returns the interval between the connections
	virtual ActionType getAction(); // returns the action type
	uint32_t getRreID(); //returns the rre id
	void setRreID(uint32_t r_id); //set the rre id
	SSFNET_LIST(SwingConnection*) swing_conn_list;

  private:
	SwingSession* swing_session;
	int remaining_conn;
	uint32_t rre_id;
};

class SwingConnection : public SwingAction {
  public:
	SwingConnection(SwingRRE* sr);
	virtual ~SwingConnection();
	SwingRRE* getSwingRRE(); //returns the swing RRE which the connection belongs to
	void deleteSwingRRE(); //set swing_rre to be null
	int getServer(); // returns the server id associated with the connection
	int getNumPairs(); // returns the number of req/rsp pairs within the connection
	virtual ActionType getAction(); // returns the action type
	void setNumRemainingPair(int p); // set the # of remaining pairs this connection needs to generate
	int getNumRemainingPair(); // returns the number of the unscheduled pairs of this connection
	int server_id;
	SSFNET_LIST(SwingPair*) swing_pair_list;
  private:
	SwingRRE* swing_rre;
	int remaining_pair;
};

class SwingPair : public SwingAction {
  public:
	SwingPair(SwingConnection* sc);
	virtual ~SwingPair();
	SwingConnection* getSwingConnection(); //returns the swing connection which the pair belongs to
	void deleteSwingConnection(); //set swing_conn to be null
	int getReqSize(); //returns the number of bytes of the request
	int getRspSize(); //returns the number of bytes of the response
	float getThinkTime(); // returns the interval between the requests
	virtual ActionType getAction(); // returns the action type
  private:
	SwingConnection* swing_conn;
};

class FlowDescription {
public:
	typedef SSFNET_VECTOR(float) ParamVector;
	typedef SSFNET_MAP(int, ParamVector) TypeParamMap;
	typedef SSFNET_MAP(int,float) TypeProbMap;
	/** The constructor. */
	FlowDescription();
	/** The destructor. */
	virtual ~FlowDescription();

	char* traffic_summary[PARAMS];

	int client_size;
	int server_size;
	int session_interval_size;
	int num_rres_size;
	int rre_interval_size;
	int num_connections_size;
	int connection_interval_size;
	int num_pairs_size;
	int req_size;
	int rsp_size;
	int think_time_size;

    ParamVector params;
    TypeParamMap typeParamClient;
    TypeParamMap typeParamServer;
    TypeProbMap typeProbClient;
    TypeProbMap typeProbServer;

	float* session_interval_data;
	int* num_rres_data;
	float* rre_interval_data;
	int* num_connections_data;
	float* connection_interval_data;
	int* num_pairs_data;
	int* req_data;
	int* rsp_data;
	float* think_time_data;

	/** Functions to read the empirical data into data structures. */
 	void loadSessionInterval();
 	void loadClient();
 	void loadNumRREs();
 	void loadRREInterval();
 	void loadNumConnections();
 	void loadConnectionInterval();
 	void loadServer();
 	void loadNumPairs();
 	void loadReqSize();
 	void loadRspSize();
 	void loadThinkTime();
};

} // namespace ssfnet
} // namespace prime

#endif /*__SWING_TCP_TRAFFIC_H__*/
