/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file routing.h
 * \brief Header file for the Routing and RouteTable classes.
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

#ifndef __ROUTING_H__
#define __ROUTING_H__

#define USE_CACHE 1

#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/rtree.h"
#include "os/nix_vector.h"
#include "ssf.h"

namespace prime {
namespace ssfnet {

class ModelBuilder;
class Net;
class Interface;
class Host;
class Packet;
class RoutingSphere;
class RouteTable;
class RouteEntry;
class RouteEntryWithBus;
class Community;
class LocalDstCache;
class NixVectorCache;
class GhostRoutingSphereRegistrationResponse;
/**
 * \brief An generic api used by hosts (or ip sessions) to forward packets
 *
 * XXX
 */
class ForwardingEngine {
public:
	ForwardingEngine() {
	}
	virtual ~ForwardingEngine() {
	}

	/**
	 * NEED TO UPDATE THE COMMENT HERE!
	 * Given the requesting host and the packet, find the interface to send the packet out on
	 *
	 * \param host is used for two purposes: 1) once we have the outbound interface idx,
	 * 	we need to get the iface from the host and 2) sice caches are associated with
	 *  communities we need to find the community the host is a member of so we can use the
	 *  cache.
	 *  \param outbound_iface this will be side-effected with the iface of the host to send the
	 *  packet out on. If there is an error outbound_iface is set to NULL
	 *
	 *  \return true is returned if the outbound_iface was successfully determined. false is returned otherwise.
	 *
	 *
	 * If this is not a RoutingSphere one must set the pkt destination MAC.
	 */
	virtual bool getOutboundIface(Host* host, Packet* pkt,
			Interface*& outbound_iface) = 0;

	/**
	 * UPDATE THE COMMENT HERE!
	 * If this engine is managed locally (i.e. by a host) then this
	 * returns true. Otherwise its a static/shared table and this return
	 * false.
	 */
	virtual bool isLocal()=0;
};

/**
 * \brief A EdgeInterface
 *
 * Used to store interfaces which are connected to links
 * which cross to a routing sphere which is not a sub-routing sphere
 * (i.e a parent or sibling routing sphere).
 */
class EdgeInterface {
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, EdgeInterface& u);
public:
	EdgeInterface(UID_t _local_rank);
	EdgeInterface(const EdgeInterface& o);
	EdgeInterface(PRIME_ISTREAM &is);
	~EdgeInterface();
	UID_t getLocalRank();
	/*
	 * Every node has an offset and size, offset represents the sum of the offset and size of its left sibling,
	 * and the size represents the sum of its children's sizes plus 1 (itself). Using this information, we are
	 * able to calculate the node's rank in terms of its parentSphere based on its rank in terms of the localSphere
	 * and vice versa.
	 */
	UID_t getParentRank(RoutingSphere* localSphere, RoutingSphere* parentSphere);
private:
	UID_t local_rank;
};

class SubEdgeInterface {
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, SubEdgeInterface& u);
public:
	SubEdgeInterface(UID_t _local_rank, UID_t _host_rank);
	SubEdgeInterface(const SubEdgeInterface& o);
	SubEdgeInterface(PRIME_ISTREAM &is);
	~SubEdgeInterface();
	UID_t getLocalRank();
	UID_t getHostLocalRank();
private:
	UID_t local_rank;
	UID_t host_rank;
};

typedef SSFNET_LIST(EdgeInterface) EdgeList;
typedef SSFNET_LIST(SubEdgeInterface) SubEdgeList;
class EdgeInterfaceList;
class EdgeInterfaceListWrapper;

class EdgeInterfaceListState {
public:
	EdgeInterfaceListState();
	~EdgeInterfaceListState();
	volatile int reference_count;
	bool ready_to_free;
	EdgeList* edges;
};


class EdgeInterfaceList {
public:
	EdgeInterfaceList();
	~EdgeInterfaceList();
	EdgeInterfaceList operator=(const EdgeInterfaceList& x);
	EdgeInterfaceListWrapper reference();
	EdgeInterfaceListState* getState();
private:
	EdgeInterfaceListState* state;
};

class EdgeInterfaceListWrapper {
public:
	EdgeInterfaceListWrapper(EdgeInterfaceListState* _state);
	~EdgeInterfaceListWrapper();
	EdgeList& getEdgesInterfaces();
	uint size();
	EdgeList::iterator begin();
	EdgeList::iterator end();
private:
	EdgeInterfaceListState* state;
};

/**
 * \brief A Routing Sphere.
 *
 * A RoutingSphere is used by a sub-network to do its
 * routing using user-defined policies.
 */
class RoutingSphere: public ConfigurableEntity<RoutingSphere, BaseEntity> ,
		public ForwardingEngine {
	friend class ModelBuilder;
	//friend class NixVectorCache;
public:
	typedef SSFNET_PAIR(UID_t, int) UIDCostPair;
	typedef SSFNET_VECTOR(UIDCostPair) UIDCostPairVector;
	typedef SSFNET_LIST(UID_t) SrcRankList;
	typedef SSFNET_PAIR(UID_t,SrcRankList) HostSrcRankListPair;
	typedef SSFNET_MAP(UID_t,HostSrcRankListPair*) HostSrcRankListPairMap;
	typedef SSFNET_MAP(UID_t,SrcRankList)SrcRankListPairMap;
	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(uint32_t, nix_vec_cache_size)
		SSFNET_CONFIG_PROPERTY_DECL(uint32_t, local_dst_cache_size)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(RouteTable, default_route_table)
	)
	SSFNET_STATEMAP_DECL(
		HostSrcRankListPairMap sub_edge_iface_map;
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(RoutingSphere,RouteTable,default_route_table)
	)
	SSFNET_CONFIG_CHILDREN_DECL(RouteTable, default_route_table)
	SSFNET_ENTITY_SETUP(&(unshared.default_route_table) )
;

	/** The constructor. */
	RoutingSphere();

	/** The destructor. */
	virtual ~RoutingSphere();

	/**
	 * The init method is used to initialize the routing sphere once it has
	 * been created and configured by the model builder.
	 */
	virtual void init();

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * used to collect statistical information of the simulation run.
	 */
	virtual void wrapup();

	/** Return the owning network; routing spheres are owned by networks. */
	Net* getOwningNet();

	/** Return the routing sphere of the owning network's parent network,
	 or NULL if the owning network is the top-level network. */
	RoutingSphere* getSuperRoutingSphere();

	/** Return the routing table for this routing sphere */
	virtual RouteTable* getRouteTable();

	/**
	 * Given the requesting host and the packet, find the interface to send the packet out on.
	 *
	 * \param host is used for two purposes: 1) once we have the outbound interface idx,
	 * 	we need to get the iface from the hostj=; and 2) since caches are associated with
	 *  communities we need to find the community the host is a member of so we can use the
	 *  cache.
	 *  \param pkt is the packet being forwarded.
	 *  \param outbound_iface is an output parameter, which is the outbound interface the packet
	 *  will be sent out on. If there is an error, it is set to NULL.
	 *
	 *  \return true is returned if the outbound_iface was successfully determined;
	 *  false is returned otherwise.
	 */
	virtual bool getOutboundIface(Host* host, Packet* pkt,
			Interface*& outbound_iface);

	/**
	 * This method returns the rank of the node specified by its uid in terms of the net who
	 * owns the given routing sphere.
	 *
	 * \param uid is the globally unique id of the node.
	 * \param rs is the routing sphere.
	 */
	UID_t getRank(UID_t uid, RoutingSphere* rs);

	/**
	 * This method returns the uid of the node specified by its rank in terms of the net who
	 * owns the given routing sphere.
	 *
	 * \param rank is the rank of the node in terms of the net who owns the given routing sphere..
	 * \param rs is the routing sphere.
	 */

	UID_t getUID(UID_t rank, RoutingSphere* rs);

	/**
	 * This method converts the rank of the node in terms of the this routing sphere's owning
	 * network into the rank in terms of the child routing sphere's owning network.
	 *
	 * \param rank is the rank of the node in the current routing sphere
	 * \param child_rs is the child routing sphere.
	 */
	UID_t getChildRank(UID_t rank, RoutingSphere* child_rs);

	/**
	 * This method returns true if this forwarding engine is bound to or
	 * managed by the host locally; otherwise it returns false, which
	 * means the forwarding engine is shared among hosts.
	 */
	virtual bool isLocal();

	/**
	 * This method returns the size of the nix vector cache.
	 */
	uint32_t getNixVectorCacheSize();


	/**
	 * This method returns the size of the local dst cache.
	 */
	uint32_t getLocalDstCacheSize();

	/**
	 * This method sets the size of the nix vector cache.
	 */
	void setNixVectorCacheSize(uint32_t s);

	/**
	 * This method sets the size of the local dst cache.
	 */
	void setLocalDstCacheSize(uint32_t s);

	/**
	 * Return the next hop (and other related information) along a path
	 * from the source node to the destination node in this routing
	 * sphere.
	 */
	virtual RouteEntry* getNextHop(UID_t src_rank, UID_t dst_rank);

	/**
	 * given the RID of two nodes (a host or routable router)
	 * find the number of hops between them
	 */
	int calcDist(UID_t src_rank, UID_t dst_rank);

	/**
	 * return whether this sphere has routes
	 */
	virtual bool hasRoutes();

	/**
	 * Get the edge interfaces of routing sphere
	 */
	virtual EdgeInterfaceListWrapper getEdgeInterfaces();

	/**
	 * This method is called by the community (or partition in bootstrapping) and is used to handle interface event.
	 */
	virtual void handleRoutingEvent(UID_t iface_id, bool is_available);

	/**
	 * This method is called by the community (or partition in bootstrapping) and is used to handle ghost registrations.
	 */
	virtual GhostRoutingSphereRegistrationResponse* handleGhostRegistration(int src_com);

	UID_t getUID(){  return BaseEntity::getUID(); }


#ifdef USE_CACHE
	/**
	 * Get the local dest cache for comm
	 */
	virtual LocalDstCache* getLocalDstCache(Community * comm);

	/**
	 * Get the niz vector cache for comm
	 */
	virtual NixVectorCache* getNixCache(Community * comm);
#endif

private:
	/**
	 * This method is used to create the nix vector (for this routing sphere) in the following way:
	 * 1) Find out if the host with the destination uid stored in the packet is in this sphere;
	 * 		a) if so get its rank.
	 * 		b) if not, use mapToLocalDsts() to find all possible local destination nodes,
	 *       pick the closest one and get its rank
	 * 2) Use getNextHop() to find a path from the source rank (given as the parameter) to the
	 *    the destination rank (calculated in the previous step), and build the nix vector.
	 * 3) The nix vector is stored in the packet.
	 */
	void createNixVector(UID_t src_rank, Packet* pkt, Community* comm);

	/**
	 * Used by createNixVector() to find a list of possible destinations
	 * within this routing sphere given the uid of a destination not
	 * within this sphere.
	 */
	void mapToLocalDsts(RoutingSphere* source_sphere, UID_t dst_uid, UIDCostPairVector& srcs);

	/** A cache of the super routing sphere **/
	RoutingSphere* superSphere;

	/** cache to route table **/
	bool rtIsCached;
	RouteTable* rtCache;

};

/**
 * \brief A Routing Sphere.
 *
 * A RoutingSphere is used by a sub-network to do its
 * routing using user-defined policies.
 */
class GhostRoutingSphere: public ConfigurableEntity<GhostRoutingSphere, RoutingSphere>
{
public:
	
public:
	SSFNET_PROPMAP_DECL(
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(UID_t, community_id)
	)
	SSFNET_ENTITY_SETUP( )
;

	/** The constructor. */
	GhostRoutingSphere() { }

	/** The destructor. */
	virtual ~GhostRoutingSphere() { }

	/**
	 * The init method is used to initialize the routing sphere once it has
	 * been created and configured by the model builder.
	 */
	virtual void init();

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * used to collect statistical information of the simulation run.
	 */
	virtual void wrapup() { RoutingSphere::wrapup(); }

	/** Return the routing table for this routing sphere */
	virtual RouteTable* getRouteTable() { return 0; }

	/**
	 * no op for ghosts
	 */
	virtual bool getOutboundIface(Host* host, Packet* pkt,
			Interface*& outbound_iface) { return 0; }

	/**
	 * no op for ghosts
	 */
	virtual RouteEntry* getNextHop(UID_t src_rank, UID_t dst_rank) { return 0; }

	/**
	 * return whether this sphere has routes
	 */
	virtual bool hasRoutes();

	/**
	 * Get the edge interfaces of routing sphere
	 */
	virtual EdgeInterfaceListWrapper getEdgeInterfaces();

	/**
	 * This method is called by the community and is used to handle interface event.
	 */
	virtual void handleRoutingEvent(UID_t iface_id, bool is_available);

	/**
	 * This method is called by the community (or partition in bootstrapping) and is used to handle ghost registrations.
	 */
	virtual GhostRoutingSphereRegistrationResponse* handleGhostRegistration(int src_com);

	/**
	 * This method is called by the community (or partition in bootstrapping) and is used to handle ghost registration response.
	 */
	void handleGhostRegistrationResponse(GhostRoutingSphereRegistrationResponse* response);



#ifdef USE_CACHE
	/**
	 * no op for ghosts
	 */
	virtual LocalDstCache* getLocalDstCache(Community * comm);

	/**
	 * no op for ghosts
	 */
	virtual NixVectorCache* getNixCache(Community * comm);
#endif
private:
	EdgeInterfaceList edge_ifaces;
};

/**
 * \brief A Route Entry
 *
 * A RouteEntry is an entry in the routing table.
 */
class RouteEntry {
public:
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, RouteEntry& u);
	typedef SSFNET_LIST(RouteEntry*) List;
	typedef SSFNET_LIST(RouteEntry*) Vector;
	RouteEntry(uint16_t _outbound_iface, UID_t _owning_host, uint16_t _num_of_bits, UID_t _next_hop,
			bool _edge_node, uint16_t _cost);
	virtual ~RouteEntry();
	const uint16_t getOutboundIfaceIdx();
	const UID_t getOwningHost();
	const uint16_t getNumberOfBits();
	const UID_t getNextHopRank();
	const bool isEdgeNode();
	const uint16_t getCost();
	virtual uint16_t getBusIdx();
	virtual uint16_t getNumberOfBitsBusIdx();
	virtual PRIME_OSTREAM& print(PRIME_OSTREAM &os);
private:
	const uint16_t outbound_iface; // rank of the outbound interface in terms of its host
	const UID_t owning_host; //rank of the owning host of the outbound interface in terms of its routing sphere
	const uint16_t num_of_bits; // number of bits to store the interface rank in the nix vector
	const UID_t next_hop; // rank of the next hop node in terms of its routing sphere
	const bool edge_node; // whether the next hop node is on the edge of its routing sphere
	const uint16_t cost; // cost of the path
};

class RouteEntryWithBus: public RouteEntry {
public:
	typedef SSFNET_LIST(RouteEntry*) List;
	RouteEntryWithBus(uint16_t _outbound_iface, UID_t _owning_host, uint16_t _num_of_bits,
			UID_t _next_hop, uint32_t _edge_iface, uint16_t bus_idx,
			uint16_t num_of_bits_bus, uint16_t _cost);
	virtual ~RouteEntryWithBus();
	virtual uint16_t getBusIdx();
	virtual uint16_t getNumberOfBitsBusIdx();
private:
	const uint16_t bus_idx; // the rank of the attachment point in terms of the link connecting to the next hop
	const uint16_t num_of_bits_bus; // number of bits to store the bus index
};

class UIDRect;
class RouteSearchResult;

typedef RTree<RouteEntry*, UID_t, 2, double> RTREE;

/**
 * \brief A src and dst rank range stored in a rtree
 *
 * This is the base structure used to store dst and src range pairs in the rtree structure.
 */
class UIDRect: public RTREE::Rect {
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, UIDRect& u);
public:
	UIDRect(UID_t src_min, UID_t dst_min, UID_t src_max, UID_t dst_max);
	~UIDRect();
};

/**
 * \brief RouteSearchResult
 *
 * Used by RTree to collect search results.
 */
class RouteSearchResult: public RTREE::SearchResult {
public:
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, RouteSearchResult& u);
	RouteSearchResult();
	virtual ~RouteSearchResult();
	virtual bool addResult(RouteEntry*& result);
	virtual PRIME_OSTREAM& print(PRIME_OSTREAM &os);
};

/**
 * \brief ExistenceSearchResult
 *
 * used to determine if t a query would return any results
 */
class ExistenceSearchResult: public RouteSearchResult {
public:
	ExistenceSearchResult();
	~ExistenceSearchResult();
	bool addResult(RouteEntry*& result);
	bool hasAtleastOneResult();
	virtual PRIME_OSTREAM& print(PRIME_OSTREAM &os);
private:
	bool rv;
};

/**
 * \brief SingleRouteSearchResult
 *
 * used to find the lowest cost route entry
 */
class SingleRouteSearchResult: public RouteSearchResult {
public:
	SingleRouteSearchResult();
	~SingleRouteSearchResult();
	bool addResult(RouteEntry*& result);
	RouteEntry* routeEntry();
	virtual PRIME_OSTREAM& print(PRIME_OSTREAM &os);
private:
	RouteEntry* rv;
};

/**
 * \brief MultipleRouteSearchResult
 *
 * return all route entries that match the query
 */
class MultipleRouteSearchResult: public RouteSearchResult {
public:
	MultipleRouteSearchResult();
	~MultipleRouteSearchResult();
	bool addResult(RouteEntry*& result);
	RouteEntry::List& getRoutes();
	virtual PRIME_OSTREAM& print(PRIME_OSTREAM &os);
private:
	RouteEntry::List results;
};

/** Used to serialize an EdgeInterface::List into a string **/
template<> bool rw_config_var<SSFNET_STRING, EdgeInterfaceList> (
		SSFNET_STRING& dst, EdgeInterfaceList& src);

/** Used to parse a string into an EdgeInterface::List **/
template<> bool rw_config_var<EdgeInterfaceList, SSFNET_STRING> (
		EdgeInterfaceList& dst, SSFNET_STRING& src);

/** Used to serialize an SubEdgeList into a string **/
template<> bool rw_config_var<SSFNET_STRING, SubEdgeList> (
		SSFNET_STRING& dst, SubEdgeList& src);

/** Used to parse a string into an SubEdgeList **/
template<> bool rw_config_var<SubEdgeList, SSFNET_STRING> (
		SubEdgeList& dst, SSFNET_STRING& src);


typedef SSFNET_PAIR(UID_t, UID_t) SrcDstPair;

struct SrcDstPairCMP {
	bool operator()(const SrcDstPair* const& l, const SrcDstPair* const& r) const {
		if (l->first != r->first) {
			return l->first < r->first;
		}
		return l->second < r->second;
	}
};

class CacheEntry {
public:
	CacheEntry(SrcDstPair& _key) :
		key(_key), prev(0), next(0) {
	}
	void remove(CacheEntry*& head, CacheEntry*& tail); //remove from queue
	void append(CacheEntry*& head, CacheEntry*& tail); //remove from queue
	SrcDstPair key;
	CacheEntry* prev, *next;
};

class NixVectorCacheEntry: public CacheEntry {
public:
	//typedef SSFNET_MAP_CMP(SrcDstPair*, NixVectorCacheEntry*, bool(*)(SrcDstPair* const,SrcDstPair* const)) Map;
	typedef SSFNET_MAP_CMP(SrcDstPair* const, NixVectorCacheEntry*, SrcDstPairCMP) Map;
	NixVectorCacheEntry(SrcDstPair& _key, NixVector* _value) :
		CacheEntry(_key), value(_value) {
	}
	NixVector* value;
};

class LocalDstCacheEntry: public CacheEntry {
public:
	//	typedef SSFNET_MAP_CMP(SrcDstPair*, LocalDstCacheEntry*, bool(*)(SrcDstPair* const,SrcDstPair* const)) Map;
	typedef SSFNET_MAP_CMP(SrcDstPair* const, LocalDstCacheEntry*, SrcDstPairCMP) Map;
	LocalDstCacheEntry(SrcDstPair& _key, UID_t& _value) :
		CacheEntry(_key), value(_value) {
	}
	UID_t value;
};

class NixVectorCache {
public:
	/**
	 * Cache for nix vector with given size.
	 */
	NixVectorCache(uint32 size);
	/**
	 * This method returns true if found and update rv.
	 */
	bool find(SrcDstPair& tofind, NixVector*& rv);

	/**
	 * This method inserts a new entry to the nix vector cache.
	 */
	void insert(SrcDstPair& key, NixVector* value);

private:
	uint32_t cache_size;
	CacheEntry* head, *tail;
	NixVectorCacheEntry::Map map;

};

class LocalDstCache {
public:
	/**
	 * Cache for a pair of the local dst's rank and the cost from the local dst to the final dst with the given size.
	 */
	LocalDstCache(uint32 size);
	/**
	 * This method returns true if found and update rv.
	 */
	bool find(SrcDstPair& tofind, UID_t& rv);

	/**
	 * This method inserts a new entry to the local dst cache.
	 */
	void insert(SrcDstPair& key, UID_t& value);

private:
	uint32_t cache_size;
	CacheEntry* head, *tail;
	LocalDstCacheEntry::Map map;

};

/**
 * \brief A Route Table.
 *
 * A RouteTable stores route entries, (src_min,src_max)x(dst_min,dst_max)->[next_hop,cost], in an Rtree.
 * Each Routing Sphere has one RouteTable.
 */
class RouteTable: public ConfigurableEntity<RouteTable, BaseEntity> {
	friend class ModelBuilder;
	friend class RoutingSphere;
public:
#ifdef USE_CACHE
	typedef SSFNET_PAIR(NixVectorCache*, LocalDstCache*) CachePair;
	typedef SSFNET_MAP(Community*, CachePair) Comm2CacheMap;
#endif
	typedef SSFNET_SET(UID_t) UIDSet;


	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(EdgeInterfaceList, edge_ifaces)
	RTREE routemap;
	)
	SSFNET_STATEMAP_DECL(
		EdgeInterfaceList my_edge_ifaces;
	)
	SSFNET_ENTITY_SETUP( )
;

	/** The constructor. */
	RouteTable();

	/** The destructor. */
	virtual ~RouteTable();

	/**
	 * The init method is used to initialize the route table.
	 */
	virtual void init();

	/**
	 * This method returns the routing sphere who owns the route table.
	 */
	RoutingSphere* getRoutingSphere();

	/**
	 * This method is called by the community and is used to handle interface event.
	 */
	void handleRoutingEvent(UID_t iface_id, bool is_available);

	/**
	 * This method is called by the community (or partition in bootstrapping) and is used to handle ghost registrations.
	 */
	virtual GhostRoutingSphereRegistrationResponse* handleGhostRegistration(int src_com);

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * a no-op currently.
	 */
	virtual void wrapup();

	//virtual void initPropertyMap(ConfigVarMap* map = NULL);

	//virtual void initStateMap();

	/**
	 * Find the routing entries given the source/destination range pair
	 */
	void getNextHop(RouteSearchResult& result, UIDRect& query);

	/**
	 * Get the edge interfaces of this forwarding table
	 */
	EdgeInterfaceListWrapper getEdgeInterfaces();

	/** get the number of routes **/
	int size();

	RTREE& getRouteMap() {
		return shared.routemap;
	}
#ifdef USE_CACHE
	/**
	 * This method returns the nix vector cache in the given community.
	 */
	NixVectorCache* getNixCache(Community* comm);

	/**
	 * This method returns the local dst cache in the given community.
	 */
	LocalDstCache* getLocalDstCache(Community* comm);
#endif

protected:
	/**
	 * Used by the model builder to add routes to the routing table.
	 */
	void addRoute(long src_min, long src_max, long dst_min, long dst_max,
			long outbound_iface, long owning_host, long num_of_bits, long next_hop,
			long edge_iface, long bus_idx, long num_of_bits_bus, long cost);

	/** a list of ghost spheres **/
	UIDSet ghost_coms;

public:
	virtual long getMemUsage_object_properties();
	virtual long getMemUsage_class();
#ifdef USE_CACHE
private:
	Comm2CacheMap comm_cache;
#endif
	long rtsize;
};

/**
 * Base Class for all routing status events
 */
class RoutingStatus : public SSFNetEvent {
public:
	/** The default constructor. */
	RoutingStatus(SSFNetEvent::Type t);
	/** The constructor. */
	RoutingStatus(
			SSFNetEvent::Type t,
			UID_t _src_routing_sphere_id,
			int _src_com_id,
			int dst_com_id);
	/** The copy constructor. */
	RoutingStatus(const RoutingStatus& evt);
	virtual ~RoutingStatus();
	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone(){return new RoutingStatus(*this);}
	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine across the
	 * memory boundaries. It is actually used to serialize the event
	 * object into a byte stream represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/** Unpack the traffic event, reverse process of the traffic method. */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	UID_t getRoutingSphereId() const { return routing_sphere_id;}
	int getSrcCommunityId() const { return src_com_id; }

private:
	UID_t routing_sphere_id;
	int src_com_id;
};


class RoutingStatusRequest: public RoutingStatus {
public:
	/** The default constructor. */
	RoutingStatusRequest();

	/** The constructor. */
	RoutingStatusRequest(
			UID_t src_sphere_id,
			int src_com_id,
			int dst_com_id,
			UID_t _iface_id);

	/** The copy constructor. */
	RoutingStatusRequest(const RoutingStatusRequest& evt);

	/** The destructor. */
	virtual ~RoutingStatusRequest();

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone(){return new RoutingStatusRequest(*this);}

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine across the
	 * memory boundaries. It is actually used to serialize the event
	 * object into a byte stream represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/** Unpack the traffic event, reverse process of the traffic method. */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	UID_t getIfaceId() const { return iface_id; }

	/**
	 * This method is the factor method for deserializing the event
	 * object.
	 */
	static prime::ssf::Event* createRoutingStatusRequest(prime::ssf::ssf_compact* dp);
	SSF_DECLARE_EVENT(RoutingStatusRequest);
private:
	UID_t iface_id;
};


class RoutingStatusResponse: public RoutingStatus {
public:
	/** The default constructor. */
	RoutingStatusResponse();
	/** The constructor. */
	RoutingStatusResponse(
			UID_t src_sphere_id,
			int src_com_id,
			int dst_com_id,
			UID_t _iface_id,
			bool _is_available);
	/** The copy constructor. */
	RoutingStatusResponse(const RoutingStatusResponse& evt);
	/** form response to request. */
	RoutingStatusResponse(const RoutingStatusRequest& evt, bool _is_available);
	/** The destructor. */
	virtual ~RoutingStatusResponse();
	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone(){return new RoutingStatusResponse(*this);}

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine across the
	 * memory boundaries. It is actually used to serialize the event
	 * object into a byte stream represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/** Unpack the traffic event, reverse process of the traffic method. */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	UID_t getIfaceId() const { return iface_id; }
	bool isAvailable() const { return is_available; }
	/**
	 * This method is the factor method for deserializing the event
	 * object.
	 */
	static prime::ssf::Event* createRoutingStatusResponse(prime::ssf::ssf_compact* dp);
	SSF_DECLARE_EVENT(RoutingStatusResponse);
private:
	UID_t iface_id;
	bool is_available;
};

class GhostRoutingSphereRegistration: public RoutingStatus {
public:
	/** The default constructor. */
	GhostRoutingSphereRegistration();
	/** The constructor. */
	GhostRoutingSphereRegistration(
			UID_t _src_routing_sphere_id,
			int _src_com_id,
			int dst_com_id);
	/** The copy constructor. */
	GhostRoutingSphereRegistration(const GhostRoutingSphereRegistration& evt);
	/** The destructor. */
	virtual ~GhostRoutingSphereRegistration();

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone(){return new GhostRoutingSphereRegistration(*this);}

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine across the
	 * memory boundaries. It is actually used to serialize the event
	 * object into a byte stream represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/** Unpack the traffic event, reverse process of the traffic method. */
	virtual void unpack(prime::ssf::ssf_compact* dp);
	/**
	 * This method is the factor method for deserializing the event
	 * object.
	 */
	static prime::ssf::Event* createGhostRoutingSphereRegistration(prime::ssf::ssf_compact* dp);
	SSF_DECLARE_EVENT(GhostRoutingSphereRegistration);
};

class GhostRoutingSphereRegistrationResponse: public RoutingStatus {
public:
	/** The default constructor. */
	GhostRoutingSphereRegistrationResponse();
	/** The constructor. */
	GhostRoutingSphereRegistrationResponse(
			UID_t _src_routing_sphere_id,
			int _src_com_id,
			int dst_com_id,
			EdgeList& edges);
	/** The copy constructor. */
	GhostRoutingSphereRegistrationResponse(const GhostRoutingSphereRegistrationResponse& evt);
	/** make a response from a registration */
	GhostRoutingSphereRegistrationResponse(const GhostRoutingSphereRegistration& evt, EdgeList& egdes);
	/** The destructor. */
	virtual ~GhostRoutingSphereRegistrationResponse();
	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone(){return new GhostRoutingSphereRegistrationResponse(*this);}

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine across the
	 * memory boundaries. It is actually used to serialize the event
	 * object into a byte stream represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/** Unpack the traffic event, reverse process of the traffic method. */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	EdgeList& getEdgeIfaces() { return edge_ifaces; }
	/**
	 * This method is the factor method for deserializing the event
	 * object.
	 */
	static prime::ssf::Event* createGhostRoutingSphereRegistrationResponse(prime::ssf::ssf_compact* dp);
	SSF_DECLARE_EVENT(GhostRoutingSphereRegistrationResponse);
private:
	EdgeList edge_ifaces;
};

} // namespace ssfnet
} // namespace prime

#endif /*__ROUTING_H__*/
