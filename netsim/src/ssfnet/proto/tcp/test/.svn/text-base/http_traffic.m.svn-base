/**
 * \file http_traffic.h
 * \author Miguel Erazo
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

#ifndef __HTTP_TRAFFIC_H__
#define __HTTP_TRAFFIC_H__

#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "os/virtual_time.h"
#include "proto/tcp/test/http_client.h"
#include "ssf.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

class HTTPLevel;
class HTTPSession;
class HTTPDownload;

// Some value that is unique
#define TCP_TRAFFIC 5001
typedef SSFNET_PAIR(VirtualTime, HTTPLevel*) Actions;
typedef SSFNET_MAP(UID_t, VirtualTime) BusyList;

class HttpStartTrafficEvent: public StartTrafficEvent {
public:
        /** The default constructor. */
        HttpStartTrafficEvent();

        /** The constructor. */
        HttpStartTrafficEvent(UID_t trafficTypeUID, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The constructor. */
        HttpStartTrafficEvent(TrafficType* trafficType, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st);

        /** The ssf clone constructor. */
        HttpStartTrafficEvent(HttpStartTrafficEvent* evt);

        /** The copy constructor. */
        HttpStartTrafficEvent(const HttpStartTrafficEvent& evt);

        /** The destructor. */
        virtual ~HttpStartTrafficEvent();

        virtual Event* clone() {
                return new HttpStartTrafficEvent(*this);
        }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        /* The unique session id, which is the traffic id of the first pair of this session. */
        void setSessionID(uint32_t session_id);

        /* Called by the client to maintain the session socket map, which is used to close the socket when session timeout. */
        uint32_t getSessionID();

        /* Set the file size. */
        void setFileSize(uint64_t size);

        /* Return the file size. */
        uint64_t getFileSize();

private:
        uint32_t session_id; //The unique session id.
        uint64_t file_size; //request size
        //int dst_port; //dst port

public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createHttpStartTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(HttpStartTrafficEvent);
};

class HttpFinishedTrafficEvent: public FinishedTrafficEvent {
public:
        /** The default constructor. */
        HttpFinishedTrafficEvent();

        /** The constructor. */
        HttpFinishedTrafficEvent(UID_t trafficTypeUID, int target_community_id, uint32_t id, uint32_t s_id, float tm);

        /** The constructor. */
        HttpFinishedTrafficEvent(TrafficType* trafficType, int target_community_id, uint32_t id, uint32_t s_id, float tm);

        /** The ssf clone constructor. */
        HttpFinishedTrafficEvent(HttpFinishedTrafficEvent* evt);

        /** The copy constructor. */
        HttpFinishedTrafficEvent(const HttpFinishedTrafficEvent& evt);

        /** The destructor. */
        virtual ~HttpFinishedTrafficEvent();

        virtual Event* clone() {
                return new HttpFinishedTrafficEvent(*this);
        }

        virtual prime::ssf::ssf_compact* pack();

        virtual void unpack(prime::ssf::ssf_compact* dp);

        void setSessionID(uint32_t s_id);

        void setTimeout(float tm);

        uint32_t getSessionID();

        float getTimeout();

private:
        uint32_t session_id;
        float timeout;
public:
        /**
         * This method is the factory method for deserializing the event
         * object. A new traffic event object is created.
         */
        static prime::ssf::Event* createHttpFinishedTrafficEvent(prime::ssf::ssf_compact* dp);

        /* This macros is used by SSF to declare an event class. */
        SSF_DECLARE_EVENT(HttpFinishedTrafficEvent);
};

class ActionsComparison {
public:
	ActionsComparison(const ActionsComparison& o): reverse(o.reverse){}
	ActionsComparison(const bool& revparam=false): reverse(revparam){};
	bool operator()(const Actions& lhs, const Actions& rhs) const {
		if (reverse)
			return (lhs.first > rhs.first);
		else
		    return (lhs.first < rhs.first);

	}
private:
	bool reverse;
};

class TCPTraffic: public ConfigurableEntity<TCPTraffic, StaticTrafficType, TCP_TRAFFIC> {
	friend class StartTrafficEvent;
 public:
	state_configuration {
		shared configurable uint32_t dst_port {
			type=INT;
			default_value="80";
			doc_string="The destination port for an HTTP connection.";
		};
		shared configurable uint64_t file_size {
			type=INT;
			default_value="1024000";
			doc_string="The size to request to the server.";
		};
		shared configurable bool file_pareto{
			type=BOOL;
			default_value="false";
			doc_string="Whether the file size are modeled by a concatenation of bounded Weibull and Pareto distributions";
		};
		shared configurable bool to_emulated {
			type=BOOL;
			default_value="false";
			doc_string="Whether this host will request packets from a simulated or emulated entity.";
		};
		shared configurable uint32_t number_of_sessions{
			type=INT;
			default_value="1";
			doc_string="Number of session.";
		};
		shared configurable uint32_t connections_per_session{
			type=INT;
			default_value="1";
			doc_string="Number of connections within a session.";
		};
		shared configurable float session_timeout{
			type=INT;
			default_value="30.0";
			doc_string="Session timeout in seconds.";
		};
	};

	// The constructor
	TCPTraffic();

	// The deconstructor
	virtual ~TCPTraffic() {};

	typedef SSFNET_PRIORITY_QUEUE(Actions,
			SSFNET_VECTOR(Actions),
			ActionsComparison) PriorityQueueActions;

	typedef SSFNET_MAP(uint32_t, HTTPSession*) IDSessionMap;
	typedef SSFNET_VECTOR(uint32_t) IPVector;

	BusyList busyList;
	UIDVec srcUIDs;
	UIDVec dstUIDs;
	IPVector dstIPs;

	ConfigType* getProtocolType();

	/*
	 * the traffic manager will call this to find out if this traffic type should be included
	 */
	virtual bool shouldBeIncludedInCommunity(Community* com);

    /*
     * called when the traffic type is initially created
     */
    virtual void init();

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
	virtual void processFinishedEvent(FinishedTrafficEvent* finished_evt);

	/*
	 * Return the destination port to connect to
	 */
	int getDstPort();

	/*
	 * Return the file size to request from server
	 */
	uint64_t getFileSize();

	/*
	 * Return whether this traffic is requesting real payload of fake payload the default is fake payload
	 * - for simulation
	 */
	bool getToEmulated();

	/*
	 * returns the session timeout time.
	 */
	float getSessionTimeout();

 private:
	double getFlowInterArrival();
	uint32_t traffic_id;
	uint32_t session_id;
	PriorityQueueActions queue;
	IDSessionMap id_session;  //mapping from session id to session
};

class HTTPLevel{
public:
	enum Level {
		SESSION=11,
		DOWNLOAD
	};
	HTTPLevel(){}
	virtual ~HTTPLevel(){}
	virtual Level getLevel()=0;
};

// Implements the concept of Session in Swing
class HTTPSession : public HTTPLevel{
public:
	HTTPSession(int conns_per_session);
	virtual ~HTTPSession();
	int getRemainingNumConnection() const;
	void decreaseRemainingNumConnection();
	virtual Level getLevel();
	double getNextSessionTime();
	UID_t getSrcUID();
	void setSrcUID(UID_t src_uid);
	uint32_t getSessionID(); //returns the session id
	void setSessionID(uint32_t s_id); //set the session id
	SSFNET_LIST(HTTPDownload*) download_list;
private:
	int remaining_connections;
	UID_t src_uid;
	uint32_t session_id;
};

// Implements the concept of connection in Swing
class HTTPDownload : public HTTPLevel{
public:
	HTTPDownload(HTTPSession* sess);
	virtual ~HTTPDownload();
	HTTPSession* getSession();
	void deleteSession();
private:
	virtual Level getLevel();
	// The HTTP session this connection belong to
	HTTPSession* http_session;
};

}
}

#endif /* __HTTP_TRAFFIC_H__ */

