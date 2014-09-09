/**
 * \file fluid_traffic.h
 * \brief Header file for the FluidTraffic class.
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

#ifndef __FLUID_TRAFFIC_H__
#define __FLUID_TRAFFIC_H__

#include <stdio.h>
#include "ssf.h"
#include "os/traffic.h"
#include "net/net.h"
#include "os/community.h"
#include "fluid_hop.h"
#include "fluid_event.h"

namespace prime {
namespace ssfnet {

#define FLUID_TRAFFIC 4003 //some value for fluid traffic that is unique....

#define DEFAULT_FLUID_TYPE FLUID_TYPE_TCP_RENO
#define DEFAULT_TCP_MULTIPLICATIVE_DECREASE 1.5
#define DEFAULT_TCP_PACKET_SIZE 1000.0 // in Bytes
#define DEFAULT_TCP_SENDWND_MAX 128 //in packets
#define DEFAULT_HURST 0.75
#define DEFAULT_MAX_PATH_LATENCY 1.0

class FluidClass;
class FluidStartTimer;
class FluidStopTimer;

#ifdef SSFNET_FLUID_STOCHASTIC
class SessionStartTimer;
class SessionStopTimer;
#endif

class FluidProbeHandling: public ProbeHandling{
public:
	/** The constructor. */
	FluidProbeHandling();
	/** The destructor. */
	virtual ~FluidProbeHandling();
	
	void setFluidClass(FluidClass* fc);

	/** The probe message is used to collect outbound interfaces along bi-path. */
	virtual void handleProbe(ProbeMessage* pmsg);
private:
	FluidClass* f_class;
};

/**
 * \brief This is fluid traffic type that defines a set of fluid flows with a common source
 * and destination via the same predetermined path.
 */
class FluidTraffic : public ConfigurableEntity<FluidTraffic, TrafficType, FLUID_TRAFFIC> {
public:
  /** Type of the fluid class. */
  enum FluidTrafficType {
    FLUID_TYPE_UDP, ///< udp flows are not responsive to congestions
    FLUID_TYPE_TCP_RENO, ///< tcp flows that behave like reno (default)
    FLUID_TYPE_TCP_NEWRENO, ///< tcp flows that behave like newreno
    FLUID_TYPE_TCP_SACK ///< tcp flows that behave like sack
  };
  typedef SSFNET_LIST(FluidClass*) FluidClassList;

  typedef SSFNET_PAIR(UID_t, UID_t) TrafficFlow; //defined by a pair of src and dst
  typedef SSFNET_LIST(TrafficFlow) TrafficFlowList;

	/** Type of the source/destination mapping methods. */
	enum SrcDstMappingType {
		ALL2ALL, ///< for every src in the src list, use all dst from the dst list as its destinations (default)
		ONE2MANY, ///< for every src in the src list, use partial dsts in the dst list as its destinations.
		MANY2ONE, ///< for every dst in the dst list, use partial srcs in the src list as its sources.
		ONE2ONE ///< for every src in the src list, randomly pick dst from the dst list
	};

  state_configuration{
	  //The sending rate will behave differently depending on the type of the fluid traffic.
	  shared configurable SSFNET_STRING protocol_type {
			type=STRING;
			default_value="tcp_reno";
			doc_string="The type of this fluid class.";
	  };
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
       shared configurable float off_time {
			type=FLOAT;
			default_value="0.01";
			doc_string="1/lamda, which is the expected value of the exponential distribution.";
       };
       shared configurable SSFNET_STRING mapping {
			type=STRING;
			default_value="all2all";
			doc_string="The method to map the source and destination based on the src and dst lists";
       };

	  shared configurable int nflows {
			type=INT;
			default_value="10";
			doc_string="The average number of homogeneous sessions in this fluid flow.";
	  };
	  shared configurable float pktsiz {
			type=FLOAT;
			default_value="1000.0";
			doc_string="Mean packet size in Bytes.";
	  };
//#ifdef SSFNET_FLUID_STOCHASTIC
	  shared configurable float hurst {
			type=FLOAT;
			default_value="0.75";
			doc_string="The hurst parameter indicates the presence of LRD (Long-Range Dependence).";
	  };
//#endif
	  shared configurable float md {
	  		type=FLOAT;
	  		default_value="1.5";
	  		doc_string="TCP congestion window multiplicative decrease factor.";
	  };
	  shared configurable float wndmax {
	  		type=FLOAT;
	  		default_value="128";
	  		doc_string="TCP send window maximum size in packets";
	  };
	  shared configurable float sendrate {
	  	    type=FLOAT;
	  		default_value="0";
	  		doc_string="UDP send rate in Byte per second.";
	  };
	  //XXX, change the type of start, stop to string for insert_activity()
	  shared configurable float start{
			type=FLOAT;
			default_value="0";
			doc_string="The start time of the flow";
	  };
	  shared configurable float stop{
	  		type=FLOAT;
	  		default_value="1000";
	  		doc_string="The stop time of the flow";
	  };
	  configurable SSFNET_STRING monfn {
	  		type=STRING;
	  		default_value=" ";
	  		doc_string="The name of monitor file.";
	  };
	  configurable float monres {
	  		type=FLOAT;
	  		default_value="0.001";
	  		doc_string="Recording interval";
	  };

	  int protocol_type;
	  float pktsiz; //in bits
	  float sendrate; //in bits per seconde
	  float wndmax;
	  float window;
      int mapping;
      TrafficFlowList* traffic_flows;

  };

public:
  /** The constructor. */
  FluidTraffic();

  /** The destructor. */
  virtual ~FluidTraffic();

  /** Initialize the fluid traffic. Initialize all the fluid classes of this fluid traffic. */
  virtual void init();

  int getFluidProtoType();
  int getNFlows();
  float getPktsiz();
  float getHurst();
  float getSendrate();
  float getWindow();
  float getMd();
  float getWndmax();
  float getStart();
  float getStop();
  float getOffTime();
  SSFNET_STRING getMonfn();
  float getMonres();

  TrafficFlowList* getTrafficFlowList();

  /*
   * the traffic manager will call this to find out if this traffic type should be included
   */
  virtual bool shouldBeIncludedInCommunity(Community* com);


public:
  /** Point to the traffic manager object that maintains this fluid traffic. */
  TrafficManager* traffic_mgr;
  const char* class_type();

};

class FluidClass{
public:
typedef SSFNET_PAIR(UID_t, UID_t) NextHopPair;
typedef SSFNET_MAP(UID_t,NextHopPair) PathMap;

	/** The constructor. */
	FluidClass(FluidTraffic* ft, Community* comm, FLUID_CLASSID cls_id);
	/** The destructor. */
	virtual ~FluidClass();

	/** Try best to resolve the path of this fluid class. Defer that if one can't at this moment. */
	virtual void init();
	
	void startTraffic();

	void run();

	/** Point to the traffic traffic object that maintains this fluid class. */
	FluidTraffic* fluid_traffic;

	Community* community;

	/** The id of this fluid flow class consists of the source and the destination ip addresses. */
	FLUID_CLASSID class_id;

	/** Point to the first fluid hop traversed by this fluid class
		      (maintained by the source node). */
	FluidHop* first_hop;

	/** Point to last fluid hop traversed by this fluid class (also
		maintained at the source node). Note that this hop is used to
		represent the reverse path. */
	FluidHop* last_hop;
		
	/** The nics for the bi-path between src and dst */
	PathMap forward_nics;
	
	PathMap reverse_nics; 

	/** Used to delay the start of the fluid flows. */
	FluidStartTimer* start_timer;

	/** Used to stop the fluid flows. */
	FluidStopTimer* stop_timer;

	int getFluidProtoType();
	int getNFlows();
	float getPktsiz();
	float getWindow();
	float getRtt();
	float getLoss();
	
	/** Update the send window size per Runge-Kutta step. */
	void windowEvolution(float step_size);

	void setRtt(float rtt);
	void setLoss(float loss);


#ifdef SSFNET_FLUID_VARYING
  int timer_handle;
  float timer_stepsize;
  void updateWindow();
#endif

 #ifdef SSFNET_FLUID_STOCHASTIC
   /** Number of homogeneous sessions in this fluid flow. */
   int nsessions;
   void sessionStop();
   prime::rng::Random* rng;
   /** Used to trigger the start of the fluid sessions. */
   SessionStartTimer* session_start_timer;
   SessionStopTimer* session_stop_timer;
   //FILE* nsessionfd;
 #endif


protected:
   class ActivityNode {
   public:
     VirtualTime time;
     bool is_start;
     ActivityNode* next;
   public:
     ActivityNode(VirtualTime t, bool s) :
       time(t), is_start(s), next(0) {}
   };

   bool is_on;
   ActivityNode* earliest_activity;
   ActivityNode* current_activity;

   void insert_activity(ActivityNode* node);
   VirtualTime check_activities(); // returns the start time
   bool activity_on();
   
 private:
 	/** The uid of the source host of this fluid class.  */
    UID_t src_uid;
    /** The uid of the destination host of this fluid class.  */
	UID_t dst_uid;
	
 	int protocol_type;
 	int nflows;
	float pktsiz; //in bits
	float hurst;
	float md;
	float sendrate; //in bits per seconde
	float wndmax;
	float start;
	float stop;
	float monres;	

	/** Number of records so far. */
	long moncnt;

	/** Measured round-trip time over time. */
	float rtt; //xxx, DEFAULT_MAX_PATH_LATENCY*2 doesn't work

	/** Measured packet loss rate over time. */
	float loss;

	/** The send window size. */
	float window;

	/** Record the fluctuation of window size over time. */
	FILE* monfd;

};

}; // namespace ssfnet
}; // namespace prime

#endif /*__FLUID_TRAFFIC_H__*/
