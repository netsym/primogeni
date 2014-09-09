/**
 * \file cnf_session.h
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
#ifndef __CNF_SESSION_H__
#define __CNF_SESSION_H__

#include "os/ssfnet.h"
#include "os/virtual_time.h"
#include "os/timer.h"
#include "proto/application_session.h"
#include "proto/simple_socket.h"

#define CNF_PORT 1000

namespace prime {
namespace ssfnet {

class CNFTransport;
class CNFMessage;
class IPv4Session;
class TCPMaster;

class SocketState{
public:
	typedef SSFNET_LIST(SimpleSocket*) ListenerSocketList;
	enum TYPE { 
		SRC_CONTROLLER, 
		SRC_CNFROUTER, 
		CNFROUTER_DST
	};
public:
	SocketState(int cid_, int t_size, int r_size, TYPE t): cid(cid_), total_size(t_size),received_size(r_size), type(t){}
	SocketState(const SocketState& o): cid(o.cid),total_size(o.total_size),received_size(o.received_size), type(o.type){}
	int getContentId(){return cid;}
	void setContentId(int c) {cid=c;}
	int getTotalSize(){ return total_size;}
	void setTotalSize(int size) {total_size=size; }
	int getReceivedSize(){ return received_size; }
	void setReceivedSize(int size) {received_size=size; }
	ListenerSocketList* getSocketList(){ return &socket_list; }
	TYPE getType(){return type;}
	void setType(TYPE t){type=t;}
private:
	int cid; //content id
	int total_size; //size of the content
	int received_size; //how many bytes have been received.
	TYPE type; //identified by the src and dst pair of the data message.
	ListenerSocketList socket_list; //a list of the listeners' sockets.
};

class RouterState{
public:
	static int CACHE_SIZE;
	static int CONTENT_SIZE;

	typedef SSFNET_MAP(int, int) CidPopularityMap;
public:
	RouterState(UID_t rid_): rid(rid_){}
	RouterState(const RouterState& o): rid(o.rid){}
	void updateCidPopularity(int cid){
		CidPopularityMap::iterator cp = cid_popularity.find(cid);
		if (cp != cid_popularity.end()){
			(*cp).second++;
		}else{
			cid_popularity.insert(SSFNET_MAKE_PAIR(cid,1));
		}
	}
	bool isFull(){
		return (int(cid_popularity.size()*CONTENT_SIZE) >= CACHE_SIZE);
	}
	
	CidPopularityMap::iterator getLeastPopularity(){
		CidPopularityMap::iterator least;
		int least_popularity = 0xFFFF;
		for (CidPopularityMap::iterator it= cid_popularity.begin(); it !=  cid_popularity.end(); it++){
			if((*it).second == 1){
				return it;
			}else if((*it).second < least_popularity){
				least_popularity = (*it).second;
				least = it;
			}
		}
		return least;
	}
	UID_t getRid(){return rid;}
	CidPopularityMap cid_popularity; // a map of cid and popularity
private:
	UID_t rid; //router id
};

typedef SSFNET_MAP(int, UID_t) CNFContentOwnerMap;
typedef SSFNET_MAP(int, int) CNFContentMap;

template<> bool rw_config_var<SSFNET_STRING, CNFContentOwnerMap> (SSFNET_STRING& dst, CNFContentOwnerMap& src);
template<> bool rw_config_var<CNFContentOwnerMap, SSFNET_STRING> (CNFContentOwnerMap& dst, SSFNET_STRING& src);

template<> bool rw_config_var<SSFNET_STRING, CNFContentMap> (SSFNET_STRING& dst, CNFContentMap& src);
template<> bool rw_config_var<CNFContentMap, SSFNET_STRING> (CNFContentMap& dst, SSFNET_STRING& src);


class CNFApplication : public ConfigurableEntity<CNFApplication,ApplicationSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_CNF_APP)> {

public:

	state_configuration {
	 		shared configurable uint32_t listening_port {
	 			type=INT;
	 			default_value="80";
	 			doc_string="Listening port for incoming connections.";
	 		};
	 		configurable uint32_t bytes_received {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of bytes received so far from all sessions";
	 		};
	 		configurable uint32_t bytes_sent {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of bytes sent so far from all sessions";
	 		};
	 		configurable uint32_t requests_received {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of requests received so far from all sessions";
	 		};
	 		configurable CNFContentMap cnf_content_sizes {
	 			type=OBJECT;
	 			default_value="[]";
				doc_string= "a list of content id and their sizes. i.e. [100,1,200,2] --> [cid:100, size:1],[cid:200, size:2]";
	 		};
 	};

	// The constructor
	CNFApplication();

	// The destructor
	virtual ~CNFApplication();

	// Configurable states

	// Initializes this protocol session.
	virtual void init();

	// Called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();

	// This method initiates the request to the controller, the controller will send a packet to the source indicating where the content locates.
	void startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac);

	// Return the CNF Transport layer session (create one if missing). */
	CNFTransport* getCNFTransport();

	// Called by the protocol session above to push a protocol message down the protocol stack
	virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0);

	// Called by the protocol session below to pop a protocol message up the protocol stack
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

	// Returns the size of the content given the content id.
	int getContentSize(int cid);


private:

	CNFTransport* cnf_trans; 	// The CNF Transport layer below this protocol
};

struct DistCMP {
	bool operator()(const int & l, const int & r) const {
			return l < r;
	}
};

class CNFTransport : public ConfigurableEntity<CNFTransport,ProtocolSession,convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_CNF_TRANS)> {
public:
	typedef SSFNET_MAP(int, int) CidSizeMap;
	typedef SSFNET_MAP(SimpleSocket*, SocketState*) SocketStateMap;
	typedef SSFNET_MAP(int, SocketState*) ContentIDStateMap;
	typedef SSFNET_LIST(RouterState*) RouterStateList;
	typedef SSFNET_MAP(int, RouterStateList*) CidRouterStateMap;
	typedef SSFNET_MAP(UID_t, RouterState*) UidRouterStateMap;
	typedef SSFNET_MAP(int, UIDVec*) CacheDecisionMap;
	typedef SSFNET_MAP_CMP(int const, UIDVec*, DistCMP) DistCacheMap;

	state_configuration {
 		configurable uint32_t bytes_received {
 			type=INT;
 			default_value="0";
 			doc_string="Number of bytes received so far from all sessions";
 		};
 		configurable uint32_t requests_received {
 			type=INT;
 			default_value="0";
 			doc_string="Number of requests received so far from all sessions";
 		};
 		configurable uint32_t bytes_sent {
	 		type=INT;
	 		default_value="0";
	 		doc_string="Number of bytes sent so far from all sessions";
	 	};
 		shared configurable UIDVec cnf_routers {
 			type=OBJECT;
 			default_value="[]";
 			doc_string="a list of RIDS of other routers with cnf transports installed";
 		};
 		shared configurable bool is_controller {
 			type=BOOL;
 			default_value="false";
 			doc_string="Whether this is a controller";
 		};
 	};

	// The constructor
	CNFTransport();

	// The destructor
	virtual ~CNFTransport();

	// Configurable states

	// Initializes this protocol session.
	virtual void init();

	// Called before the protocol session is reclaimed upon the end of simulation
	virtual void wrapup();

	// This method initiates the request to the controller, the controller will send a packet to the source indicating where the content locates.
	void startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac);

	// Called by the protocol session above to push a protocol message down the protocol stack
	virtual int push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra = 0);

	// Called by the protocol session below to pop a protocol message up the protocol stack
	virtual int pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra = 0);

	SimpleSocket* sendRequest(CNFMessage* cnfmsg, IPAddress ipaddr);

	IPv4Session* getIPSession();

	TCPMaster* getTCPMaster();

	CNFApplication* getCNFApplication();

	UID_t getCNFNode(int cid, int* evicted_cid);

    CNFContentOwnerMap* getCNFContentOwnerMap();
    
    //Returns the final dst who owns the content with cid
    UID_t getFinalDst(int cid);

	// Returns the uid of the controller.
	UID_t getControllerUID();
	
	SocketState* getCNFState(SimpleSocket* sock);
	
	void removeCNFState(SimpleSocket* sock);
	
	bool pickNewRouter();

 private:

	IPv4Session* ip_sess; // Caching a pointer to the IP session.

	TCPMaster* tcp_master; 	// The TCP layer below this protocol.
	
	UDPMaster* udp_master; //The UDP layer below this protocol.

	CNFApplication* cnf_app; 	// The CNF Application layer above this protocol

	CidSizeMap cid_size; //The content id and its size map, the CNF node keeps this map for cache.

    CNFContentOwnerMap* cid_owner; //The content id and finial dst map, the src is supposed to know the dst given the content id.

	UID_t controller_uid; //The uid of the controller.
	
	SocketStateMap sock_state; // The socket and state map.
	
	ContentIDStateMap cid_state; // The cid and state map.	
	
	UIDVec cnf_router_uid; //The vector of the uids of the cnf routers. 
	
	CidRouterStateMap cid_router; //The map of cid and router state.
	
	UidRouterStateMap rid_router; //The map of the router's uid and its state.
	
	//static CacheDecisionMap* optimal_cache_probs; //The map used to decide which cnf router is used to cache the given content
	CacheDecisionMap* optimal_cache_probs; //The map used to decide which cnf router is used to cache the given content

	//static void initOptimalCacheProbs(); //Initialize the map
	void initOptimalCacheProbs(); //Initialize the map
	
	DistCacheMap dist_router; //A sorted map for disk cache policy
	
	bool no_cnf_router; //there is no cnf router in this network
	
	static PRIME_OFSTREAM* hit_rate; //file for dumping hit rate
	
	static CNFTransport* master_controller; //XXX hack for opt
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__CNF_SESSION_H__*/
