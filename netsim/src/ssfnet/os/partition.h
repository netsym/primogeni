/**
 * \file partition.h
 * \brief Header file for the Partition class.
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

#ifndef __PARTITION_H__
#define __PARTITION_H__

#include "os/ssfnet_types.h"
#include "os/community.h"
#include "os/model_builder.h"
#include "os/emu/emulation_device.h"
#include <pthread.h>

namespace prime {
namespace ssfnet {

class Net;
class BaseEntity;
class Community;
class RoutingStatus;
class BootStrapMessage;

class CommunityForwardingEntry {
public:
	typedef SSFNET_VECTOR(CommunityForwardingEntry) Vector;
	typedef SSFNET_PAIR(CommunityForwardingEntry::Vector::iterator,CommunityForwardingEntry::Vector::iterator) Bounds;
	CommunityForwardingEntry(int _src, int _dst_min, int _dst_max, int _next_hop);
	CommunityForwardingEntry(CommunityForwardingEntry& o);
	CommunityForwardingEntry(const CommunityForwardingEntry& o);
	inline void operator=(const CommunityForwardingEntry o);
	inline bool operator<(const CommunityForwardingEntry rhs) const;
	inline bool operator>(const CommunityForwardingEntry rhs) const;
	int src, dst_min, dst_max, next_hop;
};

template<typename T>
class AlignmentRange {
public:
	AlignmentRange(T _lower, T _upper, int _com_id) :
		lower(_lower), upper(_upper), com_id(_com_id) {
	}
	AlignmentRange(AlignmentRange& o) :
		lower(o.lower), upper(o.upper), com_id(o.com_id) {
	}
	AlignmentRange(const AlignmentRange& o) :
		lower(o.lower), upper(o.upper), com_id(o.com_id) {
	}
	~AlignmentRange() { }
	inline void operator=(const AlignmentRange o) {
		this->lower=o.lower;
		this->upper=o.upper;
		this->com_id=o.com_id;
	}
	inline bool operator<(const AlignmentRange rhs) const {
		if(upper<rhs.lower)
			return true;
		return false;
	}
	inline bool operator>(const AlignmentRange rhs) const {
		if(upper>rhs.lower)
			return true;
		return false;
	}
	T lower, upper;
	int com_id;
};

typedef SSFNET_VECTOR(AlignmentRange<UID_t>) AlignmentUIDVec;
typedef SSFNET_PAIR(AlignmentUIDVec::iterator,AlignmentUIDVec::iterator) AlignmentUIDBounds;

typedef SSFNET_VECTOR(AlignmentRange<uint32>) AlignmentIPVector;
typedef SSFNET_PAIR(AlignmentIPVector::iterator,AlignmentIPVector::iterator) AlignmentIPBounds;

class Partition;
class Context;
class ContextMap {
public:
	ContextMap(uint32_t slabSize=1000, uint32_t numSlabs=10);
	ContextMap(const ContextMap& o);
	/**
	 * Get the context with uid context_id
	 */
	Context* get(UID_t context_id) const;

	/**
	 * Add the entity to the context map
	 */
	bool add(UID_t context_id, Context& c);

	uint32_t getSlabSize() const { return slabSize; }
	uint32_t getNumSlabs() const { return numSlabs; }
	void setPart(Partition* part_){ this->part=part_; };
private:
	void growSlabs(uint32_t newSize);
	uint32_t slabSize, numSlabs;
	Context** contexts;
	Partition* part;
};

class Context {
	friend class Community;
	friend class Partition;
	friend class ContextMap;
public :
	//typedef SSFNET_MAP(UID_t, Context) Map;
	typedef ContextMap Map;
	Context(BaseEntity* _obj=0, int _com_id=-1):
		obj(_obj), com_id(_com_id) {}
	Context(const Context& o):
		obj(o.obj), com_id(o.com_id) {}
	BaseEntity* getObj() const {return obj;}
	int getCommunityId() const {return com_id;}
	Context& operator= (Context const& o) {
		this->obj=o.obj;
		this->com_id=o.com_id;
		return *this;
	}

protected:
	void setCommunityId(int _com_id);
private:
	BaseEntity* obj;
	int com_id;
};

/**
 * \brief A container for co-located communities.
 *
 * Each processor will run 1 community so shared-memory
 * multiprocessor machines may have more than one community
 * (1 for each processor). The partition is a central place
 * to store shared properties (read-only) and
 * state (lock-based read/write).
 */
class Partition {
	friend class model_builder::PartitionWrapper;
	friend class model_builder::CommunityWrapper;
	friend class ModelBuilder;
	friend class EmuDevice;
public:
	typedef SSFNET_MAP(UID_t, SSFNET_STRING*) PORTAL2NIC;
	typedef SSFNET_MAP(int,int) Com2PartMap;
	typedef SSFNET_MAP(int,int) Part2RankMap;
	typedef SSFNET_LIST(SSFNetEvent*) PreInitEventList;

	/**
	 * The de-constructor
	 */
	virtual ~Partition();

	/**
	 * The init method is called by the main class which will call
	 * init on the topnet (which will call init on its children).
	 *
	 * The community will have its init called by ssf (NOT the
	 * partition) after  the Entity::startAll method is invoked
	 */
	void init();


	/**
	 * The wrapup method is called by the main class which will call
	 * wrapup on the topnet (which will call wrapup on its children).
	 *
	 * The community will have its wrapup called by ssf (NOT the
	 * partition) after the Entity::joinAll method is invoked
	 */
	void wrapup();

	/**
	 * Given a UID find the community it belongs to;
	 *
	 * WARNING:
	 *
	 * If the uid is belongs to a passive entity (such as a network or a link) then
	 * this function returns the smallest community id it belongs to.
	 */
	int uid2communityId(UID_t uid);

	/**
	 * Given an ip addr find the community it belongs to;
	 */
	int ip2communityId(IPAddress ip);

	/**
	 * Given an ip addr find the community it belongs to;
	 */
	int mac2communityId(MACAddress mac);

	/**
	 * Get the next community in the path from src to dst. Not all communities are
	 * directly connected. As such we must route events between them.
	 */
	int getNextCommunityInPath(int src, int dst);

	/**
	 * Given a community id determine which partition it is in
	 */
	int communityId2partitionId(int com_id);

	/**
	 * Given a partition id, get its MPI rank
	 */
	int partitionId2rank(int part_id);

	/**
	 * Get the topnet
	 */
	Net* getTopnet();

	/**
	 * Get the all communities in this partition
	 */
	Community::Map& getCommunities();

	/**
	 * Get the community by community id.
	 */
	Community* getCommunity(int com_id);

	/**
	 * Get the context with uid context_id
	 */
	Context* lookupContext(UID_t context_id, bool throw_exp=true);

	/**
	 * Add the entity to the context map
	 */
	void addContext(BaseEntity* c);

	/**
	 * Get the id of this partition
	 */
	int getPartitionId();

#ifdef SSFNET_EMULATION
	/**
	 * Get all of the emulation devices of type itype
	 */
	EmulationDevice::List* getEmulationDevices(int itype);

	/**
	 * register the emulation device
	 */
	void registerEmulationDevice(EmulationDevice* dev);
#else
	void* getEmulationDevices(int itype);
	void registerEmulationDevice(void* dev);
#endif
	/**
	 * functions to register retrieve portal mapping info
	 */
	static void addTrafficPortalMappping(UID_t uid, SSFNET_STRING* str);
	static SSFNET_STRING* getPortalNic(UID_t uid);

	/**
	 * prints out the alignments/community info for uids from 0 to topnet
	 */
	void debug(int total_num_coms);

	/**
	 *
	 */
	static Partition* getInstance();

	/**
	 * Get the community with the lowest id
	 */
	Community* getCommunityWithLowestId();

	/**
	 * xxx
	 */
	long getMemUsage();

	/**
	 * The routing spheres within this partition need to be initialized.
	 * This process requires interacting with remote partitions if the
	 * model is run distributed. This takes care of communicating the
	 * bootstrap events.
	 */
	void bootstrap();


	/**
	 * This method add the routing interface status request event to the preinit event list.
	 */
	void addBootstrapEvent(RoutingStatus* rs_request);

	//used to synchronize adding/deleting children
	void getStructuralModifcationLock();

	//used to synchronize adding/deleting children
	void releaseStructuralModifcationLock();

	bool shouldUseStatFile(){return useStatFile;}
	bool shouldUseStatStream(){return useStatStream;}

	static void setVisualiationExportInterval(int vei) { VEI=vei; }
	static int getVisualiationExportInterval() { return VEI; }

protected:
	/**
	 * The constructor
	 */
	static Partition* createInstance(UID_t _partition_id, Net* _topnet, bool useStatFile, bool useStatStream);

	/**
	 * Adds the uid ranges, and the community to this partition and
	 * associates them so we can later retrieve this information.
	 */
	void addCommunity(Community* com);

	/**
	 * Add a new aligned node range
	 */
	void addUIDAlignedRange(UID_t low, UID_t high, int com_id);

	/**
	 * Add a new aligned ip range
	 */
	void addIPAlignedRange(uint32 low, uint32 high, int com_id);

	/**
	 * Add a new community forwarding entry
	 */
	void addForwardingEntry(int src, int dst_min, int dst_max, int next_hop);

	/**
	 * find all local communities this uid belongs to
	 */
	void uid2localCommunities(UID_t uid, Community::Set& coms);

	/** sort the aligned ranges and community forwarding entries **/
	void internal_sort();

	/** add a community_id to partition_id entry. */
	void addComPartPair(int com_id, int part_id);

	/** set the rank of the partition id **/
	void setPartRank(int part_id, int rank);

private:


	/**
	 * The constructor
	 */
	Partition(UID_t _partition_id, Net* _topnet, bool useStatFile, bool useStatStream);

	/**
	 * handle an event that is received from a remote partition and return true if it was a response....
	 */
	void handle_event(BootStrapMessage* bm, SSFNetEvent*& response, int& dst_part);

	/**
	 * a list of uid ranges  and community ids
	 */
	AlignmentUIDVec uid_alignment_ranges;

	/**
	 * a list of ip ranges  and community ids
	 */
	AlignmentIPVector ip_alignment_ranges;

	/**
	 * fowarding entries so we can route between communities
	 */
	CommunityForwardingEntry::Vector community_forwarding_entries;

	/**
	 * All communities in this partition
	 */
	Community::Map communities;

	/**
	 * the root of the network
	 */
	Net* topnet;

	/**
	 * The id of this partition
	 */
	int partition_id;

	/**
	 * The community with the lowest id
	 */
	Community* com_with_low_id;

	/**
	 * A map from UID to contexts for all of the model nodes in this partition
	 */
	Context::Map uids;

	/**
	 * A map which stores community id to partition id
	 */
	Com2PartMap com2part_map;

	/**
	 * A map which stores partition ids to rank
	 */
	Part2RankMap part2rank_map;

	/** the pre init event list */
	PreInitEventList pre_init_evt_list;

#ifdef SSFNET_EMULATION
	/**
	 * A map of all of the emulation devices that have been created, by type
	 */
	EmulationDevice::ListMap emu_devices;

	/**
	 * map of portal interface uid's to nic names
	 */
	static PORTAL2NIC* portal_map;
#endif

	/** whether this has been boot strapped**/
	bool bootstrapped;

	/** used to protect access to the the context map during execution **/
	pthread_mutex_t* mutex;

	/** what, if any, type of state logger to use **/
	bool useStatFile, useStatStream;

	/**
	 * The instance of the partition of this process
	 */
	static Partition* __instance__;

	/** viz export interval **/
	static int VEI;

};

} // namespace ssfnet
} // namespace prime

#endif /*__PARTITION_H__*/
