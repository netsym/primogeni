/**
 * \file model_builder.h
 * \brief Header file for ModelBuilder, SymbolTable, SymbolTableEntry, TLV, ModelNode, ALignmentWrapper, CommunityWrapper, and PartitionWrapper
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

#ifndef __MODEL_BUILDER_H__
#define __MODEL_BUILDER_H__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/configurable_types.h"

namespace prime {
namespace ssfnet {

namespace model_builder {

static const int ack = JPRIME_STRING_VAR_ID;
/**
 * \brief A TLV is the base unit in which the model is transcribed from python.
 *
 * Tlvs are in the form <TYPE><LENGTH><VALUE>
 * where <TYPE> is always two characters and the
 * <LENGTH> is always followed by a newline.
 * <VALUE> is exactly <LENGTH> characters.
 */
class TLV {
public:
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TLV* x);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TLV& x);
	typedef SSFNET_LIST(TLV*) List;
	enum Type {
		STRING = JPRIME_STRING_VAR_ID,
		LONG   = JPRIME_LONG_VAR_ID,
		FLOAT  = JPRIME_FLOAT_VAR_ID,
		BOOL   = JPRIME_BOOL_VAR_ID,
		LIST   = JPRIME_LIST_VAR_ID,
		OPAQUE = JPRIME_OPAQUE_VAR_ID,
		RESOURCE_IDENTIFIER= JPRIME_RESOURCE_VAR_ID,
		SYMBOL = JPRIME_SYMBOL_VAR_ID,
		SYMBOL_TABLE = JPRIME_SYMBOL_TABLE,
		SYMBOL_TABLE_ENTRY = JPRIME_SYMBOL_TABLE_ENTRY,
		PARITION = JPRIME_PARTITION,
		COMMUNITY=JPRIME_COMMUNITY,
		ROUTE_TABLE = JPRIME_ROUTE_TABLE,
		ROUTE_ENTRY = JPRIME_ROUTE_ENTRY,
		PROPERTY = JPRIME_PROPERTY,
		NODE = JPRIME_GENERIC_NODE,
		GHOST_NODE = JPRIME_GHOST_NODE
	};
	TLV(PRIME_ISTREAM* file);
	~TLV();
	static TLV* next(List* l);
	static TLV::Type getType(int t);
	TLV::Type type;
	struct {
		long as_long;
		float as_float;
		bool as_bool;
	} prim_value;
	struct {
		SSFNET_STRING* as_str;
		TLV::List* as_tlv_list;
	} value;
private:
	static const int BUF_SIZE = 100;
	static char buf[BUF_SIZE];
};

/**
 * \brief a class to store meta info used to constructed a Community.
 */
class CommunityWrapper {
public:
	typedef SSFNET_MAP(long,CommunityWrapper*) Map;
	CommunityWrapper(TLV& tlv);
	~CommunityWrapper();
	Community* createCommunity(Partition* part);
	long com_id, num_uid_ranges,part_id;
	UIDRange::List ranges;
private:
	Community* community;
};

/**
 * \brief a forwarding entry between communities
 */
class CommunityForwardingEntry {
public:
	typedef SSFNET_LIST(CommunityForwardingEntry) List;
	CommunityForwardingEntry(TLV& tlv);
	CommunityForwardingEntry(const CommunityForwardingEntry& o);
	~CommunityForwardingEntry();
	int src,dst_min,dst_max,next_hop;
};

/**
 * \brief an ip range to community id
 */
class IPAlignment{
public:
	typedef SSFNET_LIST(IPAlignment) List;
	IPAlignment(TLV& tlv);
	IPAlignment(const IPAlignment& o);
	~IPAlignment();
	uint32 low,high,com_id;
};

/**
 * \brief a class to store meta info used to constructed a Partition.
 */
class PartitionWrapper {
	friend class ModelBuilder;
public:
	PartitionWrapper(TLV& tlv,bool useStatFile, bool useStatStream);
	~PartitionWrapper();
	CommunityWrapper::Map communities;
	CommunityForwardingEntry::List fwd_entries;
	IPAlignment::List ipmap_entries;
	Partition* createPartition(Net* topnet);
	void findMembers(BaseEntity* node);
	UID_t topnet_uid;
	long part_id,total_num_comms, num_local_coms, num_fwd_entries, num_ip_map_entries;
private:
	Partition* partition;
	bool useStatFile, useStatStream;
};

/**
 * \brief a class which is used to resolve a symbol name to a value.
 */
class SymbolTable {
public:
	typedef SSFNET_MAP(SSFNET_STRING,SSFNET_STRING*) SymbolMap;
	SymbolTable(TLV& tlv, PRIME_ISTREAM* file, RuntimeVariables* runtime_vars);
	~SymbolTable();
	SSFNET_STRING* getValue(SSFNET_STRING& name);
private:
	SymbolTable::SymbolMap symbolMap;
};

/**
 * \brief a generic node which store meta information to create specific instances of BaseEntity.
 */
class ModelNode {
public:
	typedef SSFNET_LIST(ModelNode*) List;
	typedef SSFNET_MAP(uint,  ModelNode::List* ) ReplicaMap;
	typedef SSFNET_MAP(UID_t, ModelNode*) Map;

	ModelNode(TLV& tlv, SymbolTable* symbol_table);
	~ModelNode();
	void attachAttr(TLV* attr, SymbolTable* symbol_table);
	UID_t parent_id;
	union {
		UID_t id;
		ModelNode* ptr;
	} replica;
	long num_routes;
	BaseEntity* base_entity;
	bool expanded;
};

} //end namespace model_builder

/**
 * \brief a class that reads a TLV stream and outputs a 'model'.
 *
 * This class reads a TLV stream and outputs
 * 1) 1 partition
 * 2) The communities within the partition
 * 3) A model rooted at 'topnet' with all of the sub-models
 *    included in any of the communities in this partition.
 *
 */
class ModelBuilder {
public:
	ModelBuilder(char* fileName, bool useStatFile, bool useStatStream, RuntimeVariables* runtime_vars);
	ModelBuilder(PRIME_ISTREAM* istream, bool useStatFile, bool useStatStream, RuntimeVariables* runtime_vars);
	~ModelBuilder();
	Partition* buildModel();
	void printNodes();
private:

	void addModelNode(model_builder::ModelNode* mn);
	model_builder::ModelNode* buildRouteTable(model_builder::ModelNode* route_table, PRIME_ISTREAM* file);
	void linkNodes();
	void expandReplicatedNodes();
	PRIME_ISTREAM* file;
	model_builder::ModelNode::Map* node_map;
	model_builder::ModelNode::ReplicaMap* replicas;
	Net* topnet;
	model_builder::SymbolTable* symbol_table;
	model_builder::PartitionWrapper* partitionWrapper;
	bool do_close;
	bool useStatFile, useStatStream;
	RuntimeVariables* runtime_vars;
};

} // namespace ssfnet
} // namespace prime

#endif /*__MODEL_BUILDER_H__*/
