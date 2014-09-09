/**
 * \file model_builder.cc
 * \brief Souce file for ModelBuilder, SymbolTable, SymbolTableEntry, TLV, ModelNode, ALignmentWrapper, CommunityWrapper, and PartitionWrapper
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

#include "os/model_builder.h"
#include "os/config_entity.h"
#include "os/logger.h"
#include "os/routing.h"
#include "net/net.h"
#include "net/link.h"
#include "os/alias.h"
#include "os/partition.h"
#include "os/community.h"
#include <iomanip>
using std::setfill;
using std::setw;

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(ModelBuilder)

#if TEST_ROUTING == 1
static long route_entry_count=0;
#endif

namespace model_builder {
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TLV* x) {
	if(x)
	return os << *x;
	return os <<"[NULL(UName)]";
}
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TLV& x) {
	os << "<" << (*getJPRIMETypeString(x.type));
	os << " val='";
	switch (x.type) {
	case TLV::OPAQUE:
	case TLV::STRING:
	case TLV::LONG:
	case TLV::FLOAT:
	case TLV::BOOL:
		os << *(x.value.as_str);
		break;
	case TLV::RESOURCE_IDENTIFIER:
	case TLV::LIST:
	default:
		for(TLV::List::iterator i = x.value.as_tlv_list->begin();i != x.value.as_tlv_list->end();i++) {
			os << **i;
		}
		break;
	};
	os << "'>";
	return os;
}

inline TLV* TLV::next(TLV::List* l) {
	TLV* rv = l->front();
	l->pop_front();
	return rv;
}

inline TLV::Type TLV::getType(int t) {
	return (TLV::Type) t;
}

char TLV::buf[BUF_SIZE];
TLV::TLV(PRIME_ISTREAM* file) {
	/* Tlvs are in the form <TYPE><LENGTH>,<VALUE>
	 * where <TYPE> is always four characters and the
	 * <LENGTH> is always followed by a newline.
	 * <VALUE> is exactly <LENGTH> characters.
	 */
	file->read(buf, 4);
	if (file->gcount() != 4)
		LOG_ERROR("Unable to extract two chars for the tlv type (file->gcount()="<<file->gcount()<<")"<<endl)
	buf[4] = '\0';
	SSFNET_STRING type_str(buf);
	this->type = getType(atoi(type_str.c_str()));
	//LOG_DEBUG("Parsing TLV of type "<<type_str<<endl)

	file->getline(buf, BUF_SIZE);
	if (file->gcount() == 0)
		LOG_ERROR("Unable to extract the length of the tlv"<<"[file->gcount()="<<file->gcount()<<"]!"<<endl)
	SSFNET_STRING len_str(buf);
	int len = atoi(len_str.c_str());
	//LOG_DEBUG("\tlen="<<len<<"("<<len_str<<")"<<"[file->gcount()="<<file->gcount()<<"]"<<endl)

	//parse the value
	char* buffer;
	bool free_buffer = false;
	if (len + 1 < BUF_SIZE) {
		buffer = buf;
	} else {
		buffer = new char[len + 1];
		free_buffer = true;
	}
	file->read(buffer, len);
	buffer[file->gcount()] = '\0';
	//LOG_DEBUG("\tvalue=\n"<<SSFNET_STRING(buffer)<<"\n"<<"[file->gcount()="<<file->gcount()<<"]"<<endl)

	switch (this->type) {
	case SYMBOL_TABLE_ENTRY:
	case OPAQUE:
	case STRING:
	case LONG:
	case FLOAT:
	case BOOL: {
		value.as_str = new SSFNET_STRING(buffer);
		switch (this->type) {
		case SYMBOL_TABLE_ENTRY:
		case OPAQUE:
		case STRING:
			break;
		case LONG:
			prim_value.as_long = atoll(buffer);
			break;
		case FLOAT:
			prim_value.as_float = atof(buffer);
			break;
		case BOOL: {
			int i = atoi(buffer);
			prim_value.as_bool = i != 0;
		}
			break;
		default:
			LOG_ERROR("Should never see this!"<<endl)
		}
	}
		break;
	case LIST:
	case PROPERTY:
	case SYMBOL:
	case SYMBOL_TABLE:
	case ROUTE_TABLE:
	case ROUTE_ENTRY:
	case PARITION:
	case COMMUNITY:
	case GHOST_NODE:
	case RESOURCE_IDENTIFIER:
	case NODE: {
		this->value.as_tlv_list = new TLV::List();
		if (len > 0) {
			PRIME_ISTRING_STREAM iss(buffer, PRIME_ISTRING_STREAM::in);
			while (iss.good()) {
				this->value.as_tlv_list->push_back(new TLV(
						SSFNET_DYNAMIC_CAST(PRIME_ISTREAM*,&iss)));
				iss.peek();//to see if there are more TLVS left....
			}
			if (!iss.eof())
				LOG_ERROR("MALFORMED TLV! The following characters are remaining :\""<< iss <<"\""<<endl)
		}
	}
		break;
	default:
		LOG_ERROR("Unknown TLV type "<<this->type<<"["<<type_str<<"] found during parse!"<<endl)
	}
	if (free_buffer)
		delete buffer;
}
TLV::~TLV() {
	switch (this->type) {
	case LONG:
	case FLOAT:
	case BOOL:
	case STRING:
	case OPAQUE:
	case SYMBOL_TABLE_ENTRY:
		delete this->value.as_str;
		break;
	case LIST:
	case PROPERTY:
	case SYMBOL:
	case SYMBOL_TABLE:
	case ROUTE_TABLE:
	case ROUTE_ENTRY:
	case PARITION:
	case COMMUNITY:
	case GHOST_NODE:
	case RESOURCE_IDENTIFIER:
	case NODE: {
		while (this->value.as_tlv_list->size() > 0) {
			delete TLV::next(this->value.as_tlv_list);
		}
		delete this->value.as_tlv_list;
	}
		break;
	default:
		LOG_ERROR("UNKNOWN TYPE "<<type<<endl)
	}
}




CommunityWrapper::CommunityWrapper(TLV& tlv) :
	community(0) {
	if (tlv.type != TLV::COMMUNITY)
		LOG_ERROR("Expected a tlv of type "<<(*getJPRIMETypeString(TLV::COMMUNITY))<<" but found "<<tlv<<endl)
	/*
	 * The TLV encoder writes
	 *   [community id]
	 *   [part id]
	 *   [number of uids that follow]
	 */
	if (tlv.value.as_tlv_list->size() != 3) {
		LOG_ERROR("Found a malformed tlv:"<<tlv<<". It should contain at exactly 3 inner tlvs but it has "<<tlv.value.as_tlv_list->size()<<endl)
	}

	//com id
	TLV* tlv_com_id = TLV::next(tlv.value.as_tlv_list);
	if (tlv_com_id ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<tlv_com_id<<". Expected the id of the community to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(tlv_com_id->type))<<endl)
	this->com_id = tlv_com_id->prim_value.as_long;
	delete tlv_com_id;

	TLV* tlv_part_id = TLV::next(tlv.value.as_tlv_list);
	if (tlv_part_id ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<tlv_part_id<<". Expected the id of the partition to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(tlv_part_id->type))<<endl)
	this->part_id = tlv_part_id->prim_value.as_long;
	delete tlv_part_id;

	//num ranges
	tlv_com_id = TLV::next(tlv.value.as_tlv_list);
	if (tlv_com_id ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<tlv_com_id<<". Expected the number of uid_ranges to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(tlv_com_id->type))<<endl)
	this->num_uid_ranges = tlv_com_id->prim_value.as_long;
	delete tlv_com_id;

	//LOG_DEBUG("Created community with com_id="<<com_id<<" and expect "<<num_uid_ranges<<" uid ranges"<<endl)
}

CommunityWrapper::~CommunityWrapper() {
}

Community* CommunityWrapper::createCommunity(Partition* part) {
	if (NULL == community) {
		community = new Community(com_id, part);
	}
	return community;
}

PartitionWrapper::PartitionWrapper(TLV& tlv,bool useStatFile_, bool useStatStream_) :
	partition(0),useStatFile(useStatFile_),useStatStream(useStatStream_) {
	if (tlv.type != TLV::PARITION)
		LOG_ERROR("Expected a tlv of type "<<(*getJPRIMETypeString(TLV::PARITION))<<" but found "<<tlv<<endl)
		/**
		 * The TLV encoder writes
		 *     <start partition map>
		 *       [partition id]
		 *       [topnet uid]
		 *       [total # communities ]
		 *       [total number of community forwarding entries]
		 *       [total number of ip map entries]
		 *       [num communities in this part]
		 *          ...
		 *          ...
		 *     <end partition map>
		 *     <start communities> //this is not a tlv -- its just to help format this comment
		 *       <start community>
		 *         [community id]
		 *         [partition id]
		 *         [number of uids that follow]
		 *       <end community>
		 *       [low uid][high_uid]
		 *       [low uid][high_uid]
		 *       ...
		 *       ...
		 *       <start community>
		 *         [community id]
		 *         [partition id]
		 *         [number of uids that follow]
		 *       <end community>
		 *       [low uid][high_uid]
		 *       [low uid][high_uid]
		 *       ...
		 *       ...
		 *     <end communities> //this is not a tlv -- its just to help format this comment
		 *     <start forwarding entries> //this is not a tlv -- its just to help format this comment
		 *        [[src com_id][dst_min com_id][dst_max com_id][next_hop com_id]]
		 *        [[src com_id][dst_min com_id][dst_max com_id][next_hop com_id]]
		 *        ...
		 *        ...
		 *     <end forwarding entries> //this is not a tlv -- its just to help format this comment
		 *     <start ip map entries> //this is not a tlv -- its just to help format this comment
		 *        [[low ip][high ip][community]]
		 *        [[low ip][high ip][community]]
		 *        ...
		 *        ...
		 *     <end ip map  entries> //this is not a tlv -- its just to help format this comment
		 * @throws IOException
		 */
	if (tlv.value.as_tlv_list->size() < 6) {
		LOG_ERROR("Found a malformed tlv:"<<tlv<<". It should contain at least 6 inner tlvs but it has "<<tlv.value.as_tlv_list->size()<<endl)
	}


	//part id
 	TLV* tlv_part_id = TLV::next(tlv.value.as_tlv_list);
	if (tlv_part_id ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<tlv_part_id<<". Expected the id of the partition to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(tlv_part_id->type))<<endl)
	this->part_id = tlv_part_id->prim_value.as_long;
	delete tlv_part_id;

	//topnet uid
	TLV* tlv_uid = TLV::next(tlv.value.as_tlv_list);
	if (tlv_uid ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<tlv_uid<<". Expected the uid of the topnet to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(tlv_uid->type))<<endl)
	this->topnet_uid = tlv_uid->prim_value.as_long;
	delete tlv_uid;

	//total # coms
	TLV* total_coms = TLV::next(tlv.value.as_tlv_list);
	if (total_coms ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<total_coms<<". Expected the number of communities to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(total_coms->type))<<endl)
	this->total_num_comms = total_coms->prim_value.as_long;
	delete total_coms;

	//# fwd_entries
	TLV* num_fwd = TLV::next(tlv.value.as_tlv_list);
	if (num_fwd ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<num_fwd<<". Expected the number of forwarding entries to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(num_fwd->type))<<endl)
	this->num_fwd_entries = num_fwd->prim_value.as_long;
	delete num_fwd;

	//#num ip map entried that follow
	TLV* num_ip_map_entries = TLV::next(tlv.value.as_tlv_list);
	if (num_ip_map_entries ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<num_ip_map_entries<<". Expected the number of ip map entried to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(num_ip_map_entries->type))<<endl)
	this->num_ip_map_entries = num_ip_map_entries->prim_value.as_long;
	delete num_ip_map_entries;

	//#num comms that follow
	TLV* num_coms = TLV::next(tlv.value.as_tlv_list);
	if (num_coms ->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<num_coms<<". Expected the number of local communities to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(num_coms->type))<<endl)
	this->num_local_coms = num_coms->prim_value.as_long;
	delete num_coms;

	if(tlv.value.as_tlv_list->size()) {
		LOG_ERROR("Found a malformed partition tlv. There are "<<tlv.value.as_tlv_list->size()<<" extra TLVs at the end"<<endl)
	}
	//LOG_DEBUG("Created partition:: part_id="<<part_id<<", topnet uid="<<topnet_uid<<", communities="<<endl);
	//for(CommunityWrapper::Map::iterator it=communities.begin();it!=communities.end();it++) {
	//	LOG_DEBUG("\tcommunity="<<it->first<<endl);
	//}
}

PartitionWrapper::~PartitionWrapper() {
}

Partition* PartitionWrapper::createPartition(Net* topnet) {
	if (NULL == topnet) {
		LOG_ERROR("topnet cannot be null!"<<endl)
	}
	if (NULL == partition) {
		partition = Partition::createInstance(part_id, topnet,useStatFile,useStatStream);
		long local_coms=0;
		CommunityWrapper::Map::iterator i;
		for (i = communities.begin(); i != communities.end(); i++) {
			if(i->second->part_id == part_id) {
				Community* c = i->second->createCommunity(partition);
				c->makeIndependent();
				partition->addCommunity(c);
				local_coms++;
			}
			while(i->second->ranges.size()>0) {
				partition->addUIDAlignedRange(i->second->ranges.front().lower,i->second->ranges.front().upper,i->first);
				i->second->ranges.pop_front();
			}
			partition->addComPartPair(i->first,i->second->part_id);
			delete i->second;
			i->second=0;
		}
		communities.clear();
		while(fwd_entries.size()>0) {
			partition->addForwardingEntry(fwd_entries.front().src,fwd_entries.front().dst_min,fwd_entries.front().dst_max,fwd_entries.front().next_hop);
			fwd_entries.pop_front();
		}
		while(ipmap_entries.size()>0) {
			partition->addIPAlignedRange(ipmap_entries.front().low,ipmap_entries.front().high,(int)ipmap_entries.front().com_id);
			ipmap_entries.pop_front();
		}
		if(num_local_coms != local_coms) {
			LOG_ERROR("Found a malformed partition. The tlv said there would be "<<num_local_coms<<" but found "<<local_coms<<" coms!"<<endl)
		}
	} else {
		LOG_ERROR("Called PartitionWrapper::createPartition  twice on the same partition wrapper!"<<endl)
	}
	return partition;
}

void PartitionWrapper::findMembers(BaseEntity* node) {
	bool recurse=true;
	Community::Set l;
	this->partition->uid2localCommunities(node->getUID(), l);
	LOG_DEBUG("findMembers("<< node->getUName()<<") l.size="<<l.size()<<endl);
	if(Traffic::getClassConfigType()->isSubtype(node->getConfigType()) || RoutingSphere::getClassConfigType()->isSubtype(node->getConfigType())) {
		//we need to add traffic to all communities in this partition
		for(Community::Map::iterator it= this->partition->communities.begin();it!=this->partition->communities.end();it++) {
			it->second->addNode(node);
		}
	}
	else {
		for (Community::Set::iterator i = l.begin(); i != l.end(); i++) {
			Community* c = *i;
			if (NULL == c) {
				LOG_ERROR("Should never see this"<<endl)
			}
			//LOG_DEBUG( setfill('0')<<setw(5)<<node->getUID()<<" --> community "<<c->getCommunityId()<<endl)
			if(c->addNode(node)) {
				recurse=false;
			}
		}
	}
	if(recurse) {
		//LOG_DEBUG("Getting all children iterator"<<endl)
		ChildIterator<BaseEntity*> kids=node->getAllChildren();
		while(kids.hasMoreElements()) {
			//LOG_DEBUG("\tgetting next elem"<<endl)
			findMembers(kids.nextElement());
		}
	}
	//must be done afterward to ensure that aliases are resolved before the links attach themselves..
	if (Alias::getClassConfigType()->isSubtype(node->getConfigType())) {
		//force the aliases to be resolved
		//LOG_DEBUG("On Alias "<<node->getUName()<<", uid="<<node->getUID()<<endl)
		SSFNET_DYNAMIC_CAST(Alias*,node)->resolveAliasPath();
	}
	else if (Link::getClassConfigType()->isSubtype(node->getConfigType())) {
		//attach the links to the interfaces
		//LOG_DEBUG("On Link "<<node->getUName()<<", uid="<<node->getUID()<<endl)
		Link* l = SSFNET_DYNAMIC_CAST(Link*,node);
		ChildIterator<BaseInterface*> it = l->getAttachments();
		while (it.hasMoreElements()) {
			BaseInterface* iface = it.nextElement();
			//LOG_DEBUG("\tOn attachment "<<iface->getUName()<<", uid="<<iface->getUID()<<", type="<<iface->getTypeName()<<endl)
			if (iface->getLink() == NULL) {
				iface->setLink(l);
			} else {
				LOG_ERROR("attached_link was already set on interface "<<iface->getUName()<<endl)
			}
		}
	}
}

CommunityForwardingEntry::CommunityForwardingEntry(const CommunityForwardingEntry& o) {
	src=o.src;
	dst_min=o.dst_min;
	dst_max=o.dst_max;
	next_hop=o.next_hop;
}

CommunityForwardingEntry::CommunityForwardingEntry(TLV& tlv) {
	if (tlv.type != TLV::LIST)
		LOG_ERROR("Expected a tlv of type "<<(*getJPRIMETypeString(TLV::LIST))<<" but found "<<tlv<<endl);
	if (tlv.value.as_tlv_list->size() != 4)
		LOG_ERROR("Found a malformed tlv:"<<tlv<<". It should contain 4 inner tlvs but it has "<<tlv.value.as_tlv_list->size()<<endl);

	//src
	TLV* cur= TLV::next(tlv.value.as_tlv_list);
	if (cur->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected all tlv within a community forwarding entry to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(cur->type))<<endl);
	src=(int)cur->prim_value.as_long;
	delete cur;

	//dst min
	cur= TLV::next(tlv.value.as_tlv_list);
	if (cur->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected all tlv within a community forwarding entry to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(cur->type))<<endl);
	dst_min=(int)cur->prim_value.as_long;
	delete cur;

	//dst max
	cur= TLV::next(tlv.value.as_tlv_list);
	if (cur->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected all tlv within a community forwarding entry to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(cur->type))<<endl);
	dst_max=(int)cur->prim_value.as_long;
	delete cur;

	//next hope
	cur= TLV::next(tlv.value.as_tlv_list);
	if (cur->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected all tlv within a community forwarding entry to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(cur->type))<<endl);
	next_hop=(int)cur->prim_value.as_long;
	delete cur;
}

CommunityForwardingEntry::~CommunityForwardingEntry() {

};



IPAlignment::IPAlignment(TLV& tlv){
	if (tlv.type != TLV::LIST)
		LOG_ERROR("Expected a tlv of type "<<(*getJPRIMETypeString(TLV::LIST))<<" but found "<<tlv<<endl);
	if (tlv.value.as_tlv_list->size() != 3)
		LOG_ERROR("Found a malformed tlv:"<<tlv<<". It should contain 3 inner tlvs but it has "<<tlv.value.as_tlv_list->size()<<endl);

	//low
	TLV* cur= TLV::next(tlv.value.as_tlv_list);
	if (cur->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected all tlv within a ip an alignment entry to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(cur->type))<<endl);
	low=(uint32)cur->prim_value.as_long;
	delete cur;

	//high
	cur= TLV::next(tlv.value.as_tlv_list);
	if (cur->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected all tlv within a ip an alignment entry to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(cur->type))<<endl);
	high=(uint32)cur->prim_value.as_long;
	delete cur;

	//com id
	cur= TLV::next(tlv.value.as_tlv_list);
	if (cur->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected all tlv within a ip an alignment entry to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(cur->type))<<endl);
	com_id=(uint32)cur->prim_value.as_long;
	delete cur;
}
IPAlignment::IPAlignment(const IPAlignment& o):
	low(o.low),high(o.high),com_id(o.com_id){
}

IPAlignment::~IPAlignment() {

}

SymbolTable::SymbolTable(TLV& tlv, PRIME_ISTREAM* file, RuntimeVariables* runtime_vars) {
	if (tlv.type != TLV::SYMBOL_TABLE)
		LOG_ERROR("Expected a tlv of type "<<(*getJPRIMETypeString(TLV::SYMBOL_TABLE))<<" but found "<<tlv<<endl);
	TLV* count = TLV::next(tlv.value.as_tlv_list);
	if (count->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<count<<". Expected a the # of symbol entries to be of type "<<
					(*getJPRIMETypeString(TLV::LONG))<< " but found "<<count->type<<endl);
	int c = atoi(count->value.as_str->c_str());
	LOG_DEBUG("Unpacking "<<count->value.as_str<<"("<<c<<") symbol table entries"<<endl);
	while (c>0) {
		c--;
		TLV entry(file); //name then list of datatypes
		if (entry.type != TLV::SYMBOL_TABLE_ENTRY)
			LOG_ERROR("Found a malformed tlv:"<<entry<<". Expected a symbol table entry to be of type "<<
					(*getJPRIMETypeString(TLV::SYMBOL_TABLE_ENTRY))<< " but found "<<entry.type<<endl);

		SSFNET_STRING* v = runtime_vars->getSymbolValue(*(entry.value.as_str));
		symbolMap.insert(SSFNET_MAKE_PAIR(*(entry.value.as_str), v));
		DEBUG_CODE(
			if(v) {
				LOG_DEBUG("found symbol "<<*(entry.value.as_str)<<"='"<<v<<"'"<<endl);
			}
			else {
				LOG_DEBUG("found symbol "<<*(entry.value.as_str)<<"="<<NULL<<endl);
			}
		);
	}
	LOG_DEBUG("Created symbol table with "<<symbolMap.size()<<" symbols"<<endl)
	runtime_vars->issueSymbolWarnings();
}

SSFNET_STRING* SymbolTable::getValue(SSFNET_STRING& name) {
	SymbolMap::iterator i = symbolMap.find(name);
	if(i == symbolMap.end())
		return 0;
	return i->second;
}

SymbolTable::~SymbolTable() {
	for(SymbolMap::iterator i = symbolMap.begin(); i!= symbolMap.end();i++) {
		if(i->second) {
			delete i->second;
			i->second=0;
		}
	}
	this->symbolMap.clear();
}

ModelNode::ModelNode(TLV& tlv, SymbolTable* symbol_table) {
	if (tlv.type != TLV::NODE && tlv.type != TLV::ROUTE_TABLE && tlv.type != TLV::GHOST_NODE) {
		LOG_ERROR("Expected a tlv of type "<<(*getJPRIMETypeString(TLV::NODE))<<" or "<< (*getJPRIMETypeString(TLV::ROUTE_TABLE))<<" or "<< (*getJPRIMETypeString(TLV::GHOST_NODE))<<" but found "<<tlv<<endl)
	}
	bool is_route_table = tlv.type == TLV::ROUTE_TABLE;

	/**
	 * nodes are encoded:
	 *     <start modelnode>
	 *       [uid]
	 *       [parent uid]
	 *       [replica id]
	 *       [type]
	 *       [attributes (ModelNodeVariables)]
	 *       [# route entries that follow -- this is only there for route tables]
	 *     <end modelnode>
	 */
	TLV* tlv_id = TLV::next(tlv.value.as_tlv_list);
	if (tlv_id->type != TLV::LONG)
		LOG_ERROR("Expected a the uid to be of type "<<(*getJPRIMETypeString(TLV::LONG))<<" but found "<<(*getJPRIMETypeString(tlv_id->type))<<endl)

	TLV* tlv_parent_id = TLV::next(tlv.value.as_tlv_list);
	if (tlv_parent_id->type != TLV::LONG)
		LOG_ERROR("Expected a the parent uid to be of type "<<(*getJPRIMETypeString(TLV::LONG))<<" but found "<<(*getJPRIMETypeString(tlv_parent_id->type))<<endl)

	TLV* tlv_replica_id = TLV::next(tlv.value.as_tlv_list);
	if (tlv_replica_id->type != TLV::LONG)
		LOG_ERROR("Expected a the replica uid to be of type "<<(*getJPRIMETypeString(TLV::LONG))<<" but found "<<(*getJPRIMETypeString(tlv_replica_id->type))<<endl)

	TLV* tlv_type = TLV::next(tlv.value.as_tlv_list);
	if (tlv_type->type != TLV::LONG)
		LOG_ERROR("Expected a the node type to be of type "<<(*getJPRIMETypeString(TLV::LONG))<<" but found "<<(*getJPRIMETypeString(tlv_type->type))<<endl)

	this->parent_id = tlv_parent_id->prim_value.as_long;
	this->replica.id = tlv_replica_id->prim_value.as_long;
	this->expanded = this->replica.id == 0;//if its zero we dont need to expand it...

	//LOG_DEBUG("["<<*getJPRIMETypeString(tlv.type)<<"]Creating node (type="<<tlv_type->prim_value.as_long<<") uid="<<tlv_id->prim_value.as_long<< " parent_uid="<<parent_id<<" overlay_uid="<<replica.id<<endl)
	this->base_entity = ConfigurableFactory::createInstance_jprime<BaseEntity*>(
			tlv_type->prim_value.as_long);
	this->base_entity->setUID((UID_t)tlv_id->prim_value.as_long);
	//LOG_DEBUG("\t type="<<this->base_entity->getTypeName()<<endl);
	//whats left in tlv.value.as_tlv_list are the attrs...(the last one might be the # of routes if its a route_table)
	while (tlv.value.as_tlv_list->size() > is_route_table?1:0) {
		TLV* a = TLV::next(tlv.value.as_tlv_list);
		//LOG_DEBUG("\tattr="<<*a<<endl);
		this->attachAttr(a, symbol_table);
		delete a;
	}
	if(is_route_table) {
		TLV* num_entries = TLV::next(tlv.value.as_tlv_list);
		if (num_entries->type != TLV::LONG)
			LOG_ERROR("Expected a the # of routes to be of type "<<(*getJPRIMETypeString(TLV::LONG))<<" but found "<<(*getJPRIMETypeString(num_entries->type))<<endl)
		this->num_routes=num_entries->prim_value.as_long;
		delete num_entries;
	}
	else {
		this->num_routes=0;
	}
	delete tlv_type;
	delete tlv_id;
	delete tlv_parent_id;
	delete tlv_replica_id;
}

ModelNode::~ModelNode() {
}

void ModelNode::attachAttr(TLV* attr, SymbolTable* symbol_table) {
	/* ModelNode attributes are encoded as:
	 *   [property]
	 *       [db_name] (long)
	 *       [value]   (string or list)
	 */

	if (attr->type != TLV::PROPERTY)
		LOG_ERROR("Found a malformed tlv:"<<attr<<". Expected attr to be of type "<<
				(*getJPRIMETypeString(TLV::PROPERTY))<< " but found "<<(*getJPRIMETypeString(attr->type))<<endl);
	if (attr->value.as_tlv_list->size() != 2)
		LOG_ERROR("Found a malformed tlv:"<<attr<<". Expected 2 child tlvs but found "<<attr->value.as_tlv_list->size()<<endl);

	TLV* attr_name = TLV::next(attr->value.as_tlv_list);
	TLV* attr_val = TLV::next(attr->value.as_tlv_list);
	if (attr_name->type != TLV::LONG)
		LOG_ERROR("Found a malformed tlv:"<<attr<<". Expected attr_name to be of type "<<
				(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(attr_name->type))<<endl);
	SSFNET_STRING* name=NULL;
	switch (attr_val->type) {
	case TLV::PROPERTY: {
		LOG_ERROR("Found malformed TLV:"<<attr<<". The value is an embedded property, which is not valid!"<<endl);
		break;
	case TLV::SYMBOL:
	{
		if (symbol_table == NULL) {
			LOG_ERROR("Encountered a symbol before the symbol table!"<<endl)
		}
		name=getJPRIMEAttrName((uint32_t)attr_name->prim_value.as_long);
		if(name) {
			if (attr_val->value.as_tlv_list->size() != 1) {
				LOG_ERROR("Found a malformed tlv:"<<attr_val<<". Expected attr_val to have 1 children but found "<<attr_val->value.as_tlv_list->size()<<endl);
			}
			TLV* symbol_name = TLV::next(attr_val->value.as_tlv_list);
			if (symbol_name->type != TLV::STRING)
				LOG_ERROR("Found a malformed tlv:"<<symbol_name<<". Expected symbol_name to be of type "<<
						(*getJPRIMETypeString(TLV::STRING))<< " but found "<<(*getJPRIMETypeString(symbol_name->type))<<endl);
			SSFNET_STRING* value = symbol_table->getValue(*(symbol_name->value.as_str));
			if(value) {
				LOG_INFO("Found value "<<value<<" for symbol "<<symbol_name->value.as_str<<endl);
				if (base_entity->initVar(*name,*value)) {
					LOG_ERROR("Error setting attribute "<<*name<<" for node type "<<base_entity->getTypeName()<<endl);
				}
			}
			else {
				LOG_WARN("The symbol "<<symbol_name->value.as_str<<" was not set! Replacing reference with attributes default value.\n");
			}

			delete symbol_name;
		}
		else {
			LOG_ERROR("Unknown variable id "<<(uint32_t)attr_name->prim_value.as_long<<endl)
		}
	}
		break;
	case TLV::RESOURCE_IDENTIFIER:
	{
		/**
		 * form is
		 * [is_compiled](long)
		 *
		 * if(is_compiled==1):
		 * [number of paths] (long)
		 * [path] (list)
		 * [path] (list)
		 * ....
		 * [filter] (list)--optional
		 * filter has three members:
		 *    [op]
		 *    [left]
		 *    [right]
		 *    OR
		 *    [value] (which is a ResourceID)
		 * else(is_compiled==0):
		 * [whether the list is of uids or attr values]
		 *    if attrs:
		 *       [attr name id](long)
		 *       [value](string)
		 *       [value](string)
		 *       .....
		 *    else:
		 *       [model type](string)
		 *       [uid] (long)
		 *       [uid] (long)
		 *       ....
		 * else(is_compiled==2):
		 *   and empty list....
		 */
		BaseConfigVar* var = base_entity->findVar(attr_name->prim_value.as_long);
		if(!var) {
			LOG_ERROR("Unknown attribute "<<*getJPRIMEAttrName((uint32_t)attr_name->prim_value.as_long)<<" for node type "<<base_entity->getTypeName()<<endl);
		}
		ConfigVarType* cvt = var->getVarType();
		if(ConfigVarType::RESOURCE_ID != cvt->getDataType()) {
			LOG_ERROR("The attribute "<<*getJPRIMEAttrName((uint32_t)attr_name->prim_value.as_long)<<" is not a RESOURCE_ID!"<<endl);
		}

		TLV* is_compiled = TLV::next(attr_val->value.as_tlv_list);
		switch(is_compiled->prim_value.as_long) {
		case 0: {
			//its a raw RI expr
			//XXX
			LOG_ERROR("Don't handle raw resource identifiers yet. RI='"<<attr_val<<"'"<<endl);
		}
		break;
		case 1 : {
			//its been compiled into a set of relative ids
			TLV* is_attr_list = TLV::next(attr_val->value.as_tlv_list);
			switch(is_attr_list->prim_value.as_long) {
			case 0: {
				//its uids of nodes
				TLV* type_name = TLV::next(attr_val->value.as_tlv_list);
				ConfigType* ct = ConfigurableFactory::getConfigType(type_name->value.as_str->c_str());
				if(!ct) {
					LOG_ERROR("Unknown type '"<<type_name->value.as_str<<"'!")
				}
				CompiledRID* cid = new CompiledRID(ct);
				while(attr_val->value.as_tlv_list->size()>0) {
					TLV* temp = TLV::next(attr_val->value.as_tlv_list);
					cid->getUIDVec().push_back(temp->prim_value.as_long);
					delete temp;
				}
				delete type_name;
				ResourceIdentifier* ri = SSFNET_STATIC_CAST(ResourceIdentifier* ,var->getValuePtr());
				ri->init(cid);
				assert(cid == ri->getCompiledRI());
			}
			break;
			case 1: {
				//XXX
				LOG_ERROR("Don't (yet) support resource identifiers that are compiled to a set of attributes!")
			}
			break;
			case 2: {
				//empty RI
				CompiledRID* cid = new CompiledRID(0);
				ResourceIdentifier* ri = SSFNET_STATIC_CAST(ResourceIdentifier* ,var->getValuePtr());
				ri->init(cid);
				assert(cid == ri->getCompiledRI());
			}
			break;
			default:
				LOG_ERROR("malformed RI='"<<attr_val<<"'"<<endl);
			}
			delete is_attr_list;
		}
		break;
		default:
			LOG_ERROR("malformed RI='"<<attr_val<<"'"<<endl);
		}
		delete is_compiled;
	}
	break;

	case TLV::STRING:
	case TLV::OPAQUE:
	case TLV::LONG:
	case TLV::FLOAT:
	case TLV::BOOL:
		name=getJPRIMEAttrName((uint32_t)attr_name->prim_value.as_long);
		if(name) {
			//LOG_DEBUG("\tSettting "<<*name<<" to '"<<*attr_val->value.as_str<<"'"<<endl)
			if (base_entity->initVar(*name,
						*attr_val->value.as_str)) {
				LOG_ERROR("Error setting attribute "<<*name<<" for node type "<<base_entity->getTypeName()<<endl);
			}
			//LOG_DEBUG("\tDONE"<<endl)
		}
		else {
			LOG_ERROR("Unknown variable id "<<(uint32_t)attr_name->prim_value.as_long<<endl)
		}
		break;

	case TLV::LIST:
		//XXX
		name=getJPRIMEAttrName((uint32_t)attr_name->prim_value.as_long);
		if(name) {
			LOG_ERROR("Variable Lists are not yet supported! name="<<*name<<", value="<<attr_val<<endl)
		}
		else {
			LOG_ERROR("Unknown variable id "<<(uint32_t)attr_name->prim_value.as_long<<endl)
		}
	}
		break;
	default:
		LOG_ERROR("INVALID/UNKNOWN attribute type "<<(*getJPRIMETypeString(attr_val->type))<<endl)
	}
	delete attr_name;
	delete attr_val;
}


} // namespace model_builder
} // namespace ssfnet
} // namespace prime

using namespace prime::ssfnet::model_builder;

namespace prime {
namespace ssfnet {

ModelBuilder::ModelBuilder(char* filename, bool useStatFile_, bool useStatStream_, RuntimeVariables* runtime_vars_) :
	file(0), node_map(0), replicas(0), topnet(0), symbol_table(
			0), partitionWrapper(0), do_close(true),
			useStatFile(useStatFile_),useStatStream(useStatStream_), runtime_vars(runtime_vars_) {
	PRIME_IFSTREAM* f = new PRIME_IFSTREAM(filename, ios_base::in);
	if (!f->is_open() || !f->good())
		LOG_ERROR("Error opening file "<<string(filename)<<endl);
	file = f;
	this->node_map = new ModelNode::Map();
	this->replicas = new ModelNode::ReplicaMap();
	this->runtime_vars->initDefaults();
}

ModelBuilder::ModelBuilder(PRIME_ISTREAM* stream, bool useStatFile_, bool useStatStream_, RuntimeVariables* runtime_vars_) :
	file(0), node_map(0), replicas(0), topnet(0), symbol_table(
			0), partitionWrapper(0), do_close(true),
			useStatFile(useStatFile_),useStatStream(useStatStream_), runtime_vars(runtime_vars_) {
	file = stream;
	if (!file->good())
		LOG_ERROR("Stream is not in a good state!"<<endl);
	this->node_map = new ModelNode::Map();
	this->replicas = new ModelNode::ReplicaMap();
	this->runtime_vars->initDefaults();
}

ModelBuilder::~ModelBuilder() {
	if (file) {
		if (this->do_close)
			SSFNET_DYNAMIC_CAST(PRIME_IFSTREAM*,file)->close();
		delete file;
	}
	if (node_map) {
		if (node_map->size() != 0) {
			LOG_WARN("The parent_children map still had elements in it when it was deleted!"<<endl)
		}
		delete node_map;
	}
	if (replicas) {
		if (replicas->size() != 0) {
			LOG_WARN("The replicas list still had elements in it when it was deleted!"<<endl)
		}
		delete replicas;
	}
	if(runtime_vars)
		delete runtime_vars;
}

Partition* ModelBuilder::buildModel() {
	while (this->file->good()) {
		TLV t(file);
		switch (t.type) {
		case TLV::OPAQUE:
		case TLV::STRING:
		case TLV::LONG:
		case TLV::FLOAT:
		case TLV::BOOL:
		case TLV::LIST:
		case TLV::ROUTE_ENTRY:
		case TLV::PROPERTY:
		case TLV::SYMBOL:
		case TLV::SYMBOL_TABLE_ENTRY:
		case TLV::RESOURCE_IDENTIFIER:
		case TLV::COMMUNITY:
			LOG_ERROR("Encountered a stray "<<*getJPRIMETypeString(t.type)<<"!"<<endl)
			break;
		case TLV::PARITION:
			//LOG_DEBUG("Found PARITION TLV"<<endl)
			if (NULL == partitionWrapper) {
				this->partitionWrapper = new PartitionWrapper(t,useStatFile,useStatStream);
			} else {
				LOG_ERROR("Found multiple partitions!"<<endl)
			}
			//read in the communities that follow
			for(int idx=0;idx<partitionWrapper->total_num_comms;idx++) {
				//read in a comm
				TLV com_tlv(file);
				if(com_tlv.type == TLV::COMMUNITY) {
					CommunityWrapper* c = new CommunityWrapper(com_tlv);
					partitionWrapper->communities.insert(SSFNET_MAKE_PAIR(c->com_id,c));
					//read in uid ranges
					while(c->num_uid_ranges>0) {
						TLV cur(file);
						if(cur.type == TLV::LIST) {
							if(cur.value.as_tlv_list->size()!=2) {
								LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected the uid range to have two inner TLVS but found "<<cur.value.as_tlv_list->size()<<endl);
							}
							TLV* tlv_low= TLV::next(cur.value.as_tlv_list);
							TLV* tlv_high= TLV::next(cur.value.as_tlv_list);
							if (tlv_low->type != TLV::LONG) {
								LOG_ERROR("Found a malformed tlv:"<<tlv_low<<". Expected the low id of an uid range to be of type "<<
										(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(tlv_low->type))<<endl)
							}
							if (tlv_high->type != TLV::LONG) {
								LOG_ERROR("Found a malformed tlv:"<<tlv_high<<". Expected the high id of an uid range to be of type "<<
										(*getJPRIMETypeString(TLV::LONG))<< " but found "<<(*getJPRIMETypeString(tlv_high->type))<<endl)
							}
							c->ranges.push_back(UIDRange((UID_t)tlv_low->prim_value.as_long, (UID_t)tlv_high->prim_value.as_long));
							delete tlv_high;
							delete tlv_low;
						}
						else {
							LOG_ERROR("Found a malformed tlv:"<<cur<<". Expected the uid range to be of type "<<
									(*getJPRIMETypeString(TLV::LIST))<< " but found "<<(*getJPRIMETypeString(cur.type))<<endl)
						}
						c->num_uid_ranges--;
					}
				}
				else {
					LOG_ERROR("Found a malformed tlv:"<<com_tlv<<". Expected a community (tlv type="<<
							(*getJPRIMETypeString(TLV::COMMUNITY))<< ") but found "<<(*getJPRIMETypeString(com_tlv.type))<<endl)
				}
			}

			//read in the forwarding entries
			while(partitionWrapper->num_fwd_entries>0) {
				//read in a comm
				TLV fwd_entry(file);
				if(fwd_entry.type == TLV::LIST) {
					partitionWrapper->fwd_entries.push_back(model_builder::CommunityForwardingEntry(fwd_entry));
				}
				else {
					LOG_ERROR("Found a malformed tlv:"<<fwd_entry<<". Expected all community forwarding entries to be of type "<<
							(*getJPRIMETypeString(TLV::LIST))<< " but found "<<(*getJPRIMETypeString(fwd_entry.type))<<endl)
				}
				partitionWrapper->num_fwd_entries--;
			}

			//read in the ip map entries
			while(partitionWrapper->num_ip_map_entries>0) {
				//read in a comm
				TLV ip_entry(file);
				if(ip_entry.type == TLV::LIST) {
					partitionWrapper->ipmap_entries.push_back(model_builder::IPAlignment(ip_entry));
				}
				else {
					LOG_ERROR("Found a malformed tlv:"<<ip_entry<<". Expected all ip map entries to be of type "<<
							(*getJPRIMETypeString(TLV::LIST))<< " but found "<<(*getJPRIMETypeString(ip_entry.type))<<endl)
				}
				partitionWrapper->num_ip_map_entries--;
			}

			break;
		case TLV::SYMBOL_TABLE:
			//LOG_DEBUG("Found SYMBOL_TABLE TLV"<<endl)
			if (NULL == symbol_table) {
				this->symbol_table = new SymbolTable(t, file, runtime_vars);
			} else {
				LOG_ERROR("Found multiple symbol tables!"<<endl)
			}
			break;
		case TLV::GHOST_NODE:
			//LOG_DEBUG("Found GHOST NODE TLV"<<endl)
			addModelNode(new ModelNode(t, symbol_table));
			break;
		case TLV::NODE:
			//LOG_DEBUG("Found NODE TLV"<<endl)
			addModelNode(new ModelNode(t, symbol_table));
			break;
		case TLV::ROUTE_TABLE:
			//LOG_DEBUG("Found NODE ROUTE_TABLE"<<endl)
			addModelNode(buildRouteTable(new ModelNode(t, symbol_table), file));
			break;
		default:
			LOG_ERROR("Unknown TLV (type="<<t.type<<") found during parse!"<<endl)
		}
		file->peek();//to see if there are more TLVS left....if there are not it will set the EOF bit
	}
	if (this->topnet == NULL) {
			LOG_ERROR("Did not find the topnet (uid="<<this->partitionWrapper->topnet_uid<<")!"<<endl)
	}
	Partition * rv = this->partitionWrapper->createPartition(this->topnet);
	linkNodes();
	expandReplicatedNodes();
	//clean up the node map
	ModelNode::Map::iterator p_it;
	for (p_it = node_map->begin(); p_it
			!= node_map->end(); p_it++) {
		p_it->second->base_entity=0;
		delete p_it->second;
		p_it->second=0;
	}
	this->node_map->clear();
	rv->internal_sort(); //needed here so the findMembers will work
	//rv->debug(partitionWrapper->total_num_comms);
	//printNodes();

	//LOG_DEBUG("add hosts/ifaces to communities"<<endl)
	partitionWrapper->findMembers(topnet);

	//LOG_DEBUG("Created partition"<<endl)
#if TEST_ROUTING == 1
	std::cout<<"Found "<<route_entry_count<<" route entries"<<endl;
#endif

	return rv;
}

void ModelBuilder::addModelNode(ModelNode* mn) {
	//add the node to the map
	if(!node_map->insert(SSFNET_MAKE_PAIR(mn->base_entity->getUID(),mn)).second) {
		LOG_ERROR("duplicate UID "<<mn->base_entity->getUID()<<" found!"<<endl);
	}
	if (this->partitionWrapper->topnet_uid== mn->base_entity->getUID()) {
		this->topnet = SSFNET_DYNAMIC_CAST(Net*,mn->base_entity);
	}
	//ModelNode::Map::iterator it = node_map->find(mn->base_entity->getUID());
	//LOG_DEBUG("Parsed modelnode with uid "<<mn->base_entity->getUID()<<"["<<it->second->base_entity->getUID()<<"]parent uid="<<mn->parent_id<<", name="<<mn->base_entity->getName()<<endl)
}

ModelNode* ModelBuilder::buildRouteTable(model_builder::ModelNode* rv, PRIME_ISTREAM* file) {
	RouteTable* routeTable = SSFNET_DYNAMIC_CAST(RouteTable*,rv->base_entity);
#if TEST_ROUTING == 1
	route_entry_count+=rv->num_routes;
#endif

	for (int i = 0; i < rv->num_routes; i++) {
		TLV re(file);
		if (re.type != TLV::ROUTE_ENTRY) {
			LOG_ERROR("Expected a tlv of type "<<(*getJPRIMETypeString(TLV::ROUTE_ENTRY))<<" but found "<<re<<endl)
		}
		if (re.value.as_tlv_list->size()!= 12) {
			LOG_ERROR("TLVs of type "<<(*getJPRIMETypeString(TLV::ROUTE_ENTRY))<<" must have 12 children but found "<<re.value.as_tlv_list->size()<<endl)
		}
		TLV* src_min = TLV::next(re.value.as_tlv_list);
		TLV* src_max = TLV::next(re.value.as_tlv_list);
		TLV* dst_min = TLV::next(re.value.as_tlv_list);
		TLV* dst_max = TLV::next(re.value.as_tlv_list);
		TLV* outbound_iface = TLV::next(re.value.as_tlv_list);
		TLV* owning_host = TLV::next(re.value.as_tlv_list);
		TLV* num_of_bits = TLV::next(re.value.as_tlv_list);
		TLV* next_hop = TLV::next(re.value.as_tlv_list);
		TLV* edge_iface = TLV::next(re.value.as_tlv_list);
		TLV* bus_idx = TLV::next(re.value.as_tlv_list);
		TLV* num_of_bits_bus = TLV::next(re.value.as_tlv_list);
		TLV* cost = TLV::next(re.value.as_tlv_list);
		if (src_min->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child src_min; should be type "<<TLV::LONG<<" but found "<<src_min->type<<endl)
		}
		if (src_max->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child src_max; should be type "<<TLV::LONG<<" but found "<<src_max->type<<endl)
		}
		if (dst_min->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child dst_min; should be type "<<TLV::LONG<<" but found "<<dst_min->type<<endl)
		}
		if (dst_max->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child dst_max; should be type "<<TLV::LONG<<" but found "<<dst_max->type<<endl)
		}
		if (outbound_iface->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child outbound_iface; should be type "<<TLV::LONG<<" but found "<<outbound_iface->type<<endl)
		}
		if (owning_host->type != TLV::LONG) {
				LOG_ERROR("Encountered a malformed child owning_host; should be type "<<TLV::LONG<<" but found "<<owning_host->type<<endl)
			}
		if (num_of_bits->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child num_of_bits; should be type "<<TLV::LONG<<" but found "<<num_of_bits->type<<endl)
		}
		if (next_hop->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child next_hop; should be type "<<TLV::LONG<<" but found "<<next_hop->type<<endl)
		}
		if (edge_iface->type != TLV::BOOL) {
			LOG_ERROR("Encountered a malformed child edge_ifave; should be type "<<TLV::BOOL<<" but found "<<edge_iface->type<<endl)
		}
		if (bus_idx->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child bus_idx; should be type "<<TLV::LONG<<" but found "<<bus_idx->type<<endl)
		}
		if (num_of_bits_bus->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child num_of_bits_bus; should be type "<<TLV::LONG<<" but found "<<num_of_bits_bus->type<<endl)
		}
		if (cost->type != TLV::LONG) {
			LOG_ERROR("Encountered a malformed child cost; should be type "<<TLV::LONG<<" but found "<<cost->type<<endl)
		}

		routeTable->addRoute(src_min->prim_value.as_long,
				src_max->prim_value.as_long, dst_min->prim_value.as_long,
				dst_max->prim_value.as_long, outbound_iface->prim_value.as_long,
				owning_host->prim_value.as_long,
				num_of_bits->prim_value.as_long,next_hop->prim_value.as_long,
				edge_iface->prim_value.as_bool, bus_idx->prim_value.as_long,
				num_of_bits_bus->prim_value.as_long, cost->prim_value.as_long);
		/*
		LOG_DEBUG("src_min="<<src_min->prim_value.as_long
				<<", src_max="<<src_max->prim_value.as_long
				<<", dst_min="<<dst_min->prim_value.as_long
				<<", dst_max="<<dst_max->prim_value.as_long
				<<", outbound_iface="<<outbound_iface->prim_value.as_long
				<<", owning_host="<<owning_host->prim_value.as_long
				<<", num_of_bits="<<num_of_bits->prim_value.as_long
				<<", next_hop="<<next_hop->prim_value.as_long
				<<", edge_iface="<<edge_iface->prim_value.as_bool
				<<", bus_idx="<<bus_idx->prim_value.as_long
				<<", num_of_bits_bus="<<num_of_bits_bus->prim_value.as_long
				<<", cost="<<cost->prim_value.as_long <<endl);
		 */

		delete src_min;
		delete src_max;
		delete dst_min;
		delete dst_max;
		delete outbound_iface;
		delete owning_host;
		delete num_of_bits;
		delete next_hop;
		delete edge_iface;
		delete bus_idx;
		delete num_of_bits_bus;
		delete cost;
	}
	//LOG_DEBUG("Created a route table"<<endl)
	return rv;
}

void ModelBuilder::linkNodes() {
	ModelNode::Map::iterator cur;
	ModelNode::Map::iterator found;
	Partition* part = Partition::getInstance();
	for(cur=node_map->begin();cur!=node_map->end();cur++) {
		//LOG_DEBUG("On node "<<cur->first<<", parent="<<cur->second->parent_id<<endl)
		found = node_map->find(cur->second->parent_id);
		if(found == node_map->end()) {
			//LOG_DEBUG("\tdid not find parent!"<<endl)
			if(cur->second->base_entity!=this->topnet) {
				LOG_ERROR("Unable to find the "<<cur->second->base_entity->getUID()<<"'s parent parent_id="<<cur->second->parent_id);
			}
		}
		else {
			if(found->second->replica.id) {
				//LOG_DEBUG("\tfound parent 1! found->second->replica.id="<<found->second->replica.id<<endl)
				//because of the structural sharing we don't add children to replicas ....
				cur->second->base_entity->setParent(found->second->base_entity);
				part->addContext(cur->second->base_entity);
			}
			else {
				//LOG_DEBUG("\tfound parent 2!"<<endl)
				//LOG_DEBUG("Adding "<<cur->second->base_entity->getUName()<<"["<<cur->second->base_entity->getUID()
						//<<"] to "<<found->second->base_entity->getUName()<<"["<<found->second->base_entity->getUID()<<"]"<<endl);
				found->second->base_entity->addChild(cur->second->base_entity);
			}
		}
		if(cur->second->replica.id) {
			found = node_map->find(cur->second->replica.id);
			if(found == node_map->end()) {
				LOG_ERROR("Unable to find the node which "<<cur->second->base_entity->getUID()<<" replicates! replica_id ="<<cur->second->replica.id);
			}
			else {
				cur->second->replica.ptr=found->second;
			}
		}
		else {
			cur->second->replica.ptr=0;
		}
	}
}

void ModelBuilder::expandReplicatedNodes() {
	//the replica depth is the number of replica that are followed before
	//finding the initial real node which starts the replica chain.
	//
	//we need to process the replica with the smallest depth first.
	//to do that we will sort them on the depth.

	ModelNode::Map::iterator found;
	ModelNode* cur;
	int depth;
	for(ModelNode::Map::iterator  mit=node_map->begin();mit!=node_map->end();mit++) {
		cur=mit->second->replica.ptr;
		if(cur) {
			depth=0;
			while(cur) {
				depth++;
				cur=cur->replica.ptr;
			}
			ModelNode::ReplicaMap::iterator ret = replicas->find(depth);
			ModelNode::List* list;
			if(ret==replicas->end()) {
				list = new ModelNode::List();
				replicas->insert(SSFNET_MAKE_PAIR(depth,list));
			}
			else {
				list=ret->second;
			}
			list->push_back(mit->second);
		}
	}
	for(ModelNode::ReplicaMap::iterator it=replicas->begin();it!=replicas->end();it++) {
		for(ModelNode::List::reverse_iterator lit=it->second->rbegin();lit!=it->second->rend();lit++) {
			(*lit)->base_entity->copy_init((*lit)->replica.ptr->base_entity);
			if((*lit)->base_entity->getPropertyMap() != (*lit)->replica.ptr->base_entity->getPropertyMap()) {
				LOG_DEBUG("Making "<<(*lit)->base_entity->getUName()<<"("<<(*lit)->base_entity->getUID()
						<<") share with \n\t"<<(*lit)->replica.ptr->base_entity->getUName()<<"("
						<<(*lit)->replica.ptr->base_entity->getUID()<<") ["
						<<SSFNET_STATIC_CAST(BaseEntity::__state__*,(*lit)->base_entity->getStateMap())->name<<","
						<<SSFNET_STATIC_CAST(BaseEntity::__state__*,(*lit)->replica.ptr->base_entity->getStateMap())->name<<"]"<<endl);
				(*lit)->base_entity->initPropertyMap((*lit)->replica.ptr->base_entity->getPropertyMap());
			}
			/*
			found = node_map->find((*lit)->parent_id);
			if(found == node_map->end()) {
				LOG_ERROR("Unable to find the "<<(*lit)->base_entity->getUID()<<"'s parent parent_id="<<(*lit)->parent_id);
			}
			else {
				found->second->base_entity->addChild((*lit)->base_entity);
			}*/

		}
		it->second->clear();
		delete it->second;
		it->second=0;
	}
	replicas->clear();
}

void ModelBuilder::printNodes() {
	std::cout<<"\n\nPrinting all nodes in model....."<<endl;
	SSFNET_VECTOR(BaseEntity*) nodes;
	nodes.push_back(SSFNET_DYNAMIC_CAST(BaseEntity*,topnet));
	while(nodes.size()>0) {
		BaseEntity* cur = nodes.back();
		nodes.pop_back();
		std::cout<<cur->getUName()<<"["<<cur->getUID()<<"]"<<endl;
		ChildIterator<BaseEntity*> kids = cur->getAllChildren();
		while(kids.hasMoreElements()) {
			nodes.push_back(kids.nextElement());
		}
	}
	std::cout<<"\n\nDONE Printing all nodes in model"<<endl;
}


} // namespace ssfnet
}
// namespace prime
