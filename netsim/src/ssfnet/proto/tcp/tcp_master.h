/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file tcp_master.h
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

#ifndef __TCP_MASTER_H__
#define __TCP_MASTER_H__

#include "os/ssfnet.h"
#include "os/protocol_session.h"
#include "os/routing.h"
#include "agent/tcp_session.h"
#include "tcp_init.h"
#include "proto/transport_session.h"
#include "os/virtual_time.h"

#define COARSE_GRAINED_TIMERS //enable this to make the master run the timers

namespace prime {
namespace ssfnet {

class SimpleSocket;

//#define NEW_TCP_PROTOCOL_CLASSNAME "SSF.OS.NEW_TCP"

class TCPSession;
class TCPSinkSession;
class TCPMasterTimer;

class IPPushOption {
 public:
	IPPushOption(IPADDR _src_ip, IPADDR _dst_ip, uint8 _p_id, uint8 _ttl) :
	    src_ip(_src_ip), dst_ip(_dst_ip), prot_id(_p_id), ttl(_ttl) {}

  	/** The source IP address. */
	IPAddress src_ip;
	/** The destination IP address. */
	IPAddress dst_ip;
	/** The protocol ID. */
	uint8 prot_id;
	/** Time-to-live. */
	uint8 ttl;
};

// To identify a TCP connection
class TCPConnection {
public:
	TCPConnection(int srcPort_, int dstPort_, IPAddress srcIP_, IPAddress dstIP_):
		srcPort(srcPort_), dstPort(dstPort_), srcIP(srcIP_), dstIP(dstIP_){}
	int srcPort;
	int dstPort;
	IPAddress srcIP;
	IPAddress dstIP;
};

class TCPMaster : public ConfigurableEntity<TCPMaster,ProtocolSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_TCP)>, public TransportMaster {

#ifdef COARSE_GRAINED_TIMERS
  friend class TCPMasterTimer;
#endif

public:
	TCPMaster();

	// the destructor
	~TCPMaster();

	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(SSFNET_STRING, tcpCA)
		SSFNET_CONFIG_PROPERTY_DECL(int, mss)
		SSFNET_CONFIG_PROPERTY_DECL(int, sndWndSize)
		SSFNET_CONFIG_PROPERTY_DECL(int, sndBufSize)
		SSFNET_CONFIG_PROPERTY_DECL(int, rcvWndSize)
		SSFNET_CONFIG_PROPERTY_DECL(float, samplingInterval)
		SSFNET_CONFIG_PROPERTY_DECL(int, iss)
	)
	SSFNET_STATEMAP_DECL(
	)
	SSFNET_ENTITY_SETUP( )
;

	// Called after config() to initialize this protocol session
	void init();

    // Called by the protocol session above to push a protocol message down the protocol stack
	int push(ProtocolMessage* msg, ProtocolSession* hi_sess,
			void* extinfo = 0, size_t extinfo_size = 0);

    // This method is called when an ip message is popped up by a local
	//  network interface.
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

	//create active session
	virtual TransportSession* createActiveSession(SimpleSocket* sock_,
			int local_port,
			IPAddress remote_ip, int remote_port);

	//Create a listening session
	virtual TransportSession* createListeningSession(SimpleSocket* sock,
			IPAddress listen_ip, int listen_port);


	// Change this session to connected state
	void changeToConnected(TCPSession* sess);

	// Erase a TCP session and associated objects
	void eraseSession(TCPSession* sess);

	virtual void deleteSession(TransportSession* sess) { /* no for tcp since it erases sessions itself */}

	virtual int nextSourcePort() { return next_src_port++; }

#ifdef COARSE_GRAINED_TIMERS
	void enableFastTimeout(TCPSinkSession* session);
	void disableFastTimeout(TCPSinkSession* session);

	//fast timer
	typedef SSFNET_SET(TCPSinkSession*) TCP_RECEIVER_SET;
	TCP_RECEIVER_SET fast_timeout_sessions;

	TCPMasterTimer* master_fast_timer;
	//slow timer
	TCPMasterTimer* master_rtx_timer;

	void masterTimeout(int type);
	double getSlowTimeout(){return slow_timeout;}
	double getFastTimeout(){return fast_timeout;};
	double slow_timeout;
	double fast_timeout;
#endif

	// return the configuration parameters
	uint32      getISS()            { return shared.iss.read(); }
	uint32      getRcvWndSize()     { return shared.rcvWndSize.read(); }
	uint32      getSndBufSize()     { return shared.sndBufSize.read(); }
	uint32      getSndWndSize()     { return shared.sndWndSize.read(); }
	float     getSamplingInterval() { return shared.samplingInterval.read(); }
	VirtualTime getMSL()            { return msl; }
	SSFNET_STRING getTcpCA() {return shared.tcpCA.read();}
	uint32 getMSS() { return shared.mss.read(); }

	/* Members included to sample variables */
	FILE* fp_tracefile;
	char* tracefile_name;
	char* myIP;
	IPAddress IP;

private:

	//double samplingInterval;

	// a listening tcp session is indexed by its src_port
	typedef SSFNET_MAP(int,TCPSession*) TCP_LISTEN_SESSIONS_MAP;

	// a connected tcp session is indexed by its <src_port, dst_ip, dst_port>
	typedef SSFNET_PAIR(IPADDR,int) TCPSESS_IP_PAIR;
	typedef SSFNET_PAIR(int,TCPSESS_IP_PAIR) TCPSESS_PIP_TRIPLE;
	typedef SSFNET_MAP(TCPSESS_PIP_TRIPLE, TCPSession*) TCP_CONN_SESSIONS_MAP;
	typedef SSFNET_MAP(TCPSESS_PIP_TRIPLE, TCPSinkSession*) TCP_SINK_MAP;

	// TCP sessions that are listening. i.e servers
	TCP_LISTEN_SESSIONS_MAP listeningSessions;
	// TCP sessions that are connected with peers
	TCP_CONN_SESSIONS_MAP connectedSessions;

	// The IP layer protocol session, located below this protocol
	//  session on the protocol stack
	ProtocolSession* ip_sess;

	// Maximum segment lifetime
	VirtualTime msl;

	TCPSession* bufferSession;
	int bufferListeningPort;
	int next_src_port;
};

/*
 * TIMERS
 */
#define TIMER_IDLE	      	  0
#define TIMER_PENDING	      1
#define FAST_TIMER            0
#define SLOW_TIMER            1
#define DEFAULT_SLOW_TIMER    0.5
#define DEFAULT_FAST_TIMER    0.2

#ifdef COARSE_GRAINED_TIMERS
class TCPMasterTimer : public Timer {
 public:
	TCPMasterTimer(int type, TCPMaster* master);

	void sched(double delay);
	void resched(double delay);
	int status();

	inline void force_cancel() { cancel();  }

 protected:
	virtual void callback();

	int n_; // timer type number
	TCPMaster* a_; // pointer to master
};
#endif

class TCPTimer : public Timer {
public:
	TCPTimer(int tno, TCPSession* agent);

	void sched(double delay);
	void resched(double delay);
	int status();

	inline void force_cancel() { cancel(); }

#ifdef COARSE_GRAINED_TIMERS
	bool isRunning();
	bool is_running;
	// For retransmission timer
	double rxmit_timer_count;
	void setCounter(double rtttimeout);
	void cancel();
	double getCounter();
#endif
protected:
  virtual void callback();

  int n_; // timer type number
  TCPSession* a_; // pointer to agent
};

class TCPSinkTimer : public Timer {
public:
	TCPSinkTimer(int tno, TCPSinkSession* agent);

	void sched(double delay);
	void resched(double delay);
	int status();

	inline void force_cancel() { cancel();  }

	//double delay_timer_count; //delay timer

	 #ifdef COARSE_GRAINED_TIMERS
	bool isRunning();
	bool is_running;

	double delay_timer_count; //delay timer

	void setCounter(double rtttimeout);
	void cancel();
	double getCounter();
#endif

protected:
	virtual void callback();

	int n_; // timer type number
	TCPSinkSession* a_; // pointer to agent
};

class TCPSamplingTimer : public Timer {
public:
	TCPSamplingTimer(TCPSession *agent);

	void resched(double delay);

protected:
	virtual void callback();
	TCPSession *a_;
};

}
}
#endif /*__TCP_MASTER_H__*/
