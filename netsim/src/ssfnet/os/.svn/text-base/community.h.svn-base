/**
 * \file community.h
 * \brief Header file for the Community class.
 * \author Nathanael Van Vorst
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

#ifndef __COMMUNITY_H__
#define __COMMUNITY_H__

#include<iostream>
#include<fstream>
#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/model_builder.h"
#include "os/traffic.h"
#include "os/trie.h"

namespace prime {
namespace ssfnet {

class Net;
class Host;
class Interface;
class IOManager;
class TrafficManager;
class StateLogger;
class StateUpdateEvent;
class VizMonitor;
class MonitorTimer;

/**
 * Users who need to use the async name service must derive from
 * this class and then pass an instance of the derived class to
 * the async functions
 *
 * when the query is returned one of functions will be called, depending on which type of query was requested
 */
class NameResolutionCallBackWrapper {
public:
	typedef SSFNET_MAP(long,NameResolutionCallBackWrapper*) Map;

	NameResolutionCallBackWrapper(){}
	virtual~ NameResolutionCallBackWrapper() {}

	/**
	 * called when a mac or ip is searched for
	 */
	virtual void call_back(UID_t uid)=0;

	/**
	 * called when a uid is searched for
	 */
	virtual void call_back(IPAddress ip, MACAddress mac)=0;

	/*
	 * called when the uid/mac/ip can't be found
	 */
	virtual void invalid_query()=0;
};

class NameServiceEvent : public SSFNetEvent {
public:
	enum Action {
		LOOKUP_MIN,
		LOOKUP_UID_FROM_IP,
		LOOKUP_UID_FROM_MAC,
		LOOKUP_ADDR_FROM_UID,
		LOOKUP_RESPONSE,
		LOOKUP_MAX
	};
	/** The default constructor. */
	NameServiceEvent();

	/** The constructor. */
	NameServiceEvent(
			long serial_number_,
			Action action_,
			UID_t uid_,
			IPAddress ip_,
			MACAddress mac_,
			int src_com_,
			int target_com);
	/** The constructor. */
	NameServiceEvent(
			long serial_number_,
			Action action_,
			UID_t uid_,
			IPAddress ip_,
			MACAddress mac_,
			UID_t rec_uid_,
			IPAddress rec_ip_,
			MACAddress rec_mac_,
			int src_com_,
			int target_com);

	/** The copy constructor. */
	NameServiceEvent(const NameServiceEvent& evt);

	virtual ~NameServiceEvent();

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone(){return new NameServiceEvent(*this);}

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine across the
	 * memory boundaries. It is actually used to serialize the event
	 * object into a byte stream represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/** Unpack the traffic event, reverse process of the traffic method. */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	long getSerialNumber() const {return serial_number;}
	Action getAction() const {return action;}
	UID_t getUID() const { return uid;}
	IPAddress getIP() const { return ip; }
	MACAddress getMAC() const { return mac; }
	UID_t getRequestedUID() const { return requested_uid;}
	IPAddress getRequestedIP() const { return requested_ip; }
	MACAddress getRequestedMAC() const { return requested_mac; }
	int getSrcCommunity() const {return src_com; }

	/**
	 * This method is the factor method for deserializing the event
	 * object.
	 */
	static prime::ssf::Event* createNameServiceEvent(prime::ssf::ssf_compact* dp);
	SSF_DECLARE_EVENT(NameServiceEvent);
private:
	long serial_number;
	Action action;
	UID_t uid, requested_uid;
	IPAddress ip,requested_ip;
	MACAddress mac,requested_mac;
	int src_com;
};

/**
 * \brief External Network Table
 *
 * This class maps IP addresses (in the form a.b.c.d/p) into IPPrefixRoute
 * structures that specify the interface which is able to recevie and send
 * traffic to a specific network which is external to this one.
 * The forwarding table is implemented as a Trie so we can
 * quickly find the most specific prefix for any destination.
 *
 */
class ExternalNetworkTable : protected Trie {
public:
	enum ReturnValue {
		FT_ROUTE_SUCCESS = 0,
		FT_ROUTE_OVERWRITTEN = 1201,
		FT_ROUTE_NOT_REPLACED = 1202,
		FT_ROUTE_NOT_FOUND = 1203
	};
	/** The constructor. */
	ExternalNetworkTable() {}

	/** The destructor. */
	virtual ~ExternalNetworkTable() {}

	/**
	 * Add the preferred route to a given ip prefix. If there's already
	 * an entry for this address and if replace is true, the old route
	 * will be overwritten and the method returns
	 * FT_ROUTE_OVERWRITTEN. Also, the old route will be
	 * reclaimed. Otherwise, the method returns 0.
	 */
	ExternalNetworkTable::ReturnValue addRoute(IPPrefixRoute* route, bool replace = true);

	/**
	 * Remove a route from the forwarding table to a given ip
	 * prefix. The removed route will be deleted. The method returns 0
	 * if the route has been found and reclaimed. Oherwise, the method
	 * returns FT_ROUTE_NOT_FOUND.
	 */
	ExternalNetworkTable::ReturnValue removeRoute(IPPrefixRoute* route);

	/**
	 * Find a route to a given ip address. The method returns NULL if no
	 * such route exists.
	 */
	IPPrefixRoute* getRoute(uint32 ipaddr);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const ExternalNetworkTable& x);

protected:
	/** The recursive helper to the dump method. */
	static void dump_helper(TrieNode* root, uint32 sofar,
			int n, PRIME_OSTREAM &os);
};

/**
 * \brief A container for network hosts and routers.
 *
 * Community is the class derived from the SSF entity and serves as
 * the container for hosts. The community class also provides time
 * advancement and message delivery services.
 *
 * Note that in SSFNet, there's only one SSF entity per
 * timeline. Therefore, community is also called an
 * alignment. Conceptually, a timeline in SSF is a set of entities
 * that keep a common simulation clock.
 *
 * In the network model networks have an alignment attribute. This
 * attribute is used  to assignments to a processor. Once these
 * alignments have been assigned they are compressed into a single
 * "alignment" which is then called a community.
 */
class Community: public prime::ssf::Entity {
	friend class Partition;
	friend class model_builder::PartitionWrapper;
	friend class model_builder::CommunityWrapper;
	friend class ModelBuilder;
	friend class IOManager;
	friend class TrafficManager;
	friend class StateLogger;
public:
	typedef SSFNET_MAP(int,Community*) Map;
	typedef SSFNET_MAP(IPAddress,UID_t) IP2UIDMap;
	typedef SSFNET_MAP(MACAddress,UID_t) MAC2UIDMap;
	typedef SSFNET_PAIR(IPAddress,MACAddress) IPMACPair;
	typedef SSFNET_MAP(UID_t,IPMACPair) UID2IPMACMap;
	typedef SSFNET_LIST(Community*) List;
	typedef SSFNET_SET(Community*) Set;
	typedef SSFNET_SET(MonitorTimer*) MonitorSet;
public:

	virtual ~Community();

	/**
	 * The init method is used to initialize the community. Since
	 * community is derived from SSF entity class, the method is called
	 * implicitly by the simulation engine when the simulation starts
	 * (i.e., after the Entity::startAll method is invoked).
	 */
	virtual void init();

	/** Finalize the community. The method will be called implicitly by
	 the simulation engine upon finishing the simulation run. */
	virtual void wrapup();

	/**
	 * Each community has a traffic manager which schedules traffic within its community
	 *
	 * This function returns this communty's traffic manager
	 */
	TrafficManager* getTrafficManager();

	/**
	 * returns whether init as been called on the commity
	 */
	bool init_called() { return inited; }

	/**
	 * returns whether wrapup as been called on the commity
	 */
	bool wrapup_called() { return wrapuped; }

	/**
	 * Convert ip to uid. If there is a iface in this community with the ip, its uid will be returned.
	 * Otherwise 0 is returned.
	 */
	UID_t synchronousNameResolution(IPAddress ip);

	/**
	 * Convert uid to ip . If there is a iface in this community with the uid, ipaddr will be set to it.
	 * Otherwise ipaddr will be set to IPAddress::IPADDR_INVALID
	 */
	void synchronousNameResolution(UID_t uid, IPAddress& ipaddr);

	/**
	 * Convert mac to uid. If there is a iface in this community with the mac, its uid will be returned.
	 * Otherwise 0 is returned.
	 */
	UID_t synchronousNameResolution(MACAddress mac);

	/**
	 * Convert uid to mac . If there is a iface in this community with the uid, macaddr will be set to it.
	 * Otherwise macaddr will be set to MACAddress::MACADDR_INVALID
	 */
	void synchronousNameResolution(UID_t uid, MACAddress& macaddr);

	/**
	 * this will call call_back_obj->call_back(UID_t) when the answer is known.
	 * If the query cannot be answered call_back_obj->invalid_query is called.
	 * It is guaranteed that the object will be called back, but there are no guarantees on the timeliness
	 */
	void asynchronousNameResolution(IPAddress ip, NameResolutionCallBackWrapper* call_back_obj);


	/**
	 * this will call call_back_obj->call_back(UID_t) when the answer is known.
	 * If the query cannot be answered call_back_obj->invalid_query is called.
	 * It is guaranteed that the object will be called back, but there are no guarantees on the timeliness
	 */
	void asynchronousNameResolution(MACAddress mac, NameResolutionCallBackWrapper* call_back_obj);

	/**
	 * this will call call_back_obj->call_back(IPAdress, MACAdress) when the answer is known.
	 * If the query cannot be answered call_back_obj->invalid_query is called.
	 * It is guaranteed that the object will be called back, but there are no guarantees on the timeliness
	 */
	void asynchronousNameResolution(UID_t uid, NameResolutionCallBackWrapper* call_back_obj);


	/**
	 * Figure out if the event is meant for a host/iface in this community. If its, deliver it using the correct function.
	 * If the event is meant for another community, this will route the event to the community for which it is meant.
	 */
	void deliverEvent(SSFNetEvent* evt, VirtualTime delay=0);

	/**
	 * Get the id of this community
	 */
	int getCommunityId();

	/**
	 * Get the partition this community belongs to
	 */
	Partition* getPartition();

	/**
	 * determine if state exporting is enabled
	 */
	bool isStateExportEnabled() { return state_logger != NULL; }

	/**
	 * Export state to a file or TCP stream
	 */
	void exportState(StateUpdateEvent* update);
	
	/**
	 * get the state logger for this community
	 */
	StateLogger* getStateLogger() { return state_logger; }

	/** add/remove nodes to the area of interest **/
	void addToAreaOfInterest(UID_t uid);
	void removeFromAreaOfInterest(UID_t uid);


	/** adjust the viz export rate **/
	void updateVizExportRate(uint64 rate);
	uint64 getVizExportRate() { return viz_export_rate; };

	/** add a monitor to be initialized **/
	void addMonitor(MonitorTimer* mon);

	/**
	 * Given a UID find the associated object.
	 *
	 * When return_passive is false this only returns objects which
	 * are exclusively owned by this community. Hosts, Interfaces,
	 * protocol sessions, routingSpheres, are all included here.
	 *
	 * When return_passive is true this returns any object returns objects which
	 * are owned by this community OR objects which are not owned by any community.
	 * Networks are the primary example type here.
	 *
	 * \param uid is the uid of object
	 * \param return_passive
	 */
	BaseEntity* getObject(UID_t uid, bool return_passive=false);

	/**
	 * construct a community name from a community id
	 */
	static void createNameFromId(UID_t uid, SSFNET_STRING& str);

protected:
	/**
	 * Constructor
	 */
	Community(int _community_id, Partition* _partition);

	/**
	 * Add nodes to this community. If the node is a host the community will recurse into the node.
	 *
	 * If the community owns the node, it will return true and false otherwise.
	 */
	bool addNode(BaseEntity* node);

	/**
	 * called by the partition->init() to setup the channels to the remote communities
	 */
	void createChannels();

public:
	/**
	 * return whether the user requested throttling
	 */
	static bool isThrottled();

	/**
	 * set whether the user requested throttling
	 */
	static  void setIsThrottled(bool b);

	/**
	 * set collocated community delay
	 */
	static  void setCollocatedCommunityDelay(VirtualTime t);

	/**
	 * get collocated community delay
	 */
	static VirtualTime getCollocatedCommunityDelay();

private:
	bool inited;
	bool wrapuped;

	/** a pointer to the owning partition **/
	Partition* partition;

	/**
	 * The id of this community
	 */
	int community_id;

	/** base name evt serial # **/
	long nm_serial;

	/** viz export rate **/
	uint64 viz_export_rate;

	/**
	 * The IOManager
	 */
	IOManager* iomgr;

	/**
	 * The TrafficManager
	 */
	TrafficManager* traffic_mgr;

	/** a trie of external ip prefixes of networks which are external to the simulation. */
	ExternalNetworkTable* ext_table;

	/** used to export state and revieve state set/fetch requests **/
	StateLogger* state_logger;

	/** used to export state to slingshot **/
	VizMonitor* viz_mon;

	/**
	 * A list of ifaces which is used to create the MPI channels
	 */
	BaseInterface::List temp_ifaces;

	/** a map from ip to iface **/
	BaseInterface::IpMap ip2iface;

	/** a map from mac to iface **/
	BaseInterface::MacMap mac2iface;

	/** a map from serial # to name service call back **/
	NameResolutionCallBackWrapper::Map name_evt_map;

	/** a map to cache remote ip -> uid **/
	IP2UIDMap remoteIP2UIDCache;

	/** a map to cache remote mac -> uid **/
	MAC2UIDMap remoteMAC2UIDCache;

	/** a map to cache remote uid to ip/mac **/
	UID2IPMACMap remoteUID2IPMACCache;

	/** a set of monitors that need to be initialized**/
	MonitorSet monitors;

	/*
	 * whether to throttle communities
	 */
	static bool __is_throttled__;

	/**
	 * This is the delay that is used to connect to communities on the same partition that are
	 * not directly connected by links. this is necessary to carry control events (like start traffic).
	 */
	static VirtualTime __collocated_delay__;

};

} // namespace ssfnet
} // namespace prime

#endif /*__COMMUNITY_H__*/
