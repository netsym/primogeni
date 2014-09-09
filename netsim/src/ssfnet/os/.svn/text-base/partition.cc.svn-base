/**
 * \file partition.cc
 * \brief Source file for the Partition class
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

#include "os/partition.h"
#include "os/logger.h"
#include "net/net.h"
#include "os/traffic_mgr.h"
#include <algorithm>
#define MPI_BUF_SIZE 2048
namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(Partition);

#ifdef PRIME_SSF_DISTSIM
#ifdef PRIME_SSF_SYNC_MPI

class BootStrapMessage {
public:
	BootStrapMessage(SSFNetEvent* evt, int mpi_rank_):usrevt_ident(-1), usrevt_packed(0), buff(new char[MPI_BUF_SIZE]),pos(0),mpi_rank(mpi_rank_),need_to_wait(false){
		if(evt) {
			usrevt_packed = evt->pack();
			usrevt_ident = evt->_evt_evtcls_ident();
			usrevt_packed->add_int(usrevt_ident);
		} else {
			LOG_ERROR("evt was null!"<<endl)
		}
	}
	BootStrapMessage(int mpi_rank_):usrevt_ident(-1), usrevt_packed(0),buff(new char[MPI_BUF_SIZE]),pos(0),mpi_rank(mpi_rank_),need_to_wait(false){
	}
	~BootStrapMessage() {
		if(need_to_wait)
			LOG_ERROR("Tried to delete a bootstrap message that has not finished its IO!"<<endl)
			if(usrevt_packed) delete usrevt_packed;
		if(buff) delete[] buff;
	}
	void pack(MPI_Comm comm) {
		//LOG_INFO("1Packing evt, usrevt_ident="<<usrevt_ident<<", pos="<<pos<<", bufsiz="<<(MPI_BUF_SIZE-pos)<<endl);
		if(MPI_SUCCESS != MPI_Pack(&usrevt_ident, 1, MPI_INT, buff, MPI_BUF_SIZE-pos, &pos, comm)) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while packing event!"<<endl);
		}
		//LOG_INFO("2Packing evt, usrevt_ident="<<usrevt_ident<<", pos="<<pos<<", bufsiz="<<(MPI_BUF_SIZE-pos)<<endl);
		if(usrevt_packed && usrevt_packed->pack(comm, buff, pos, MPI_BUF_SIZE-pos)) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while packing event!"<<endl);
		}
		//LOG_INFO("3Packed evt, usrevt_ident="<<usrevt_ident<<", pos="<<pos<<", bufsiz="<<(MPI_BUF_SIZE-pos)<<endl);
	}
	void unpack(MPI_Comm comm) {
		//LOG_INFO("1Unpacking evt, usrevt_ident="<<usrevt_ident<<", pos="<<pos<<", bufsiz="<<(MPI_BUF_SIZE-pos)<<endl);
		if(MPI_SUCCESS != MPI_Unpack(buff, MPI_BUF_SIZE-pos, &pos, &usrevt_ident, 1, MPI_INT, comm)) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while unpacking event!"<<endl);
		}
		//LOG_INFO("2Unpacking evt, usrevt_ident="<<usrevt_ident<<", pos="<<pos<<", bufsiz="<<(MPI_BUF_SIZE-pos)<<endl);
		usrevt_packed = new ssf_compact;
		if(usrevt_packed->unpack(comm, buff, pos, MPI_BUF_SIZE-pos)) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while unpacking event!"<<endl);
		}
		//LOG_INFO("3Unpacked evt, usrevt_ident="<<usrevt_ident<<", pos="<<pos<<", bufsiz="<<(MPI_BUF_SIZE-pos)<<endl);
	}
	BootStrapMessage* IRecv() {
		if(need_to_wait) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while starting asynchronous receive!"<<endl);
		}
		if(MPI_SUCCESS != MPI_Irecv(buff, MPI_BUF_SIZE, MPI_CHAR, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &req)) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while starting asynchronous receive!"<<endl);
		}
		need_to_wait=true;
		return this;
	}
	BootStrapMessage* ISend(int dst_part) {
		if(need_to_wait) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while starting asynchronous send!"<<endl);
		}
		pack(MPI_COMM_WORLD);
		if(MPI_SUCCESS != MPI_Isend(buff, pos, MPI_CHAR, dst_part, 1, MPI_COMM_WORLD, &req)) {
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Error while starting asynchronous send!"<<endl);
		}
		need_to_wait=true;
		return this;
	}
	bool Test() {
		if(!need_to_wait) {
			LOG_WARN("[rank="<<(int)mpi_rank<<"] tries to wait for a send/recv to finish when nothing was started!"<<endl);
			while(true) {
				need_to_wait=(!need_to_wait);
			}
			//LOG_ERROR("[rank="<<(int)mpi_rank<<"] tries to wait for a send/recv to finish when nothing was started!"<<endl);
		}
		MPI_Status status;
		int flag;
		if(MPI_SUCCESS != MPI_Test(&req, &flag, &status)){
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] failed while testing an async send/recv!"<<endl);
		}
		if(flag == 1) {
			need_to_wait=false;
		}
		return !need_to_wait;
	}
	prime::ssf::int32 usrevt_ident;
	ssf_compact* usrevt_packed;
	char* buff;
	int pos;
	int mpi_rank;
	bool need_to_wait;
	MPI_Request req;
};
#endif
#endif


void Context::setCommunityId(int _com_id) {
	if(com_id == -1) {
		com_id=_com_id;
	}
	else {
		LOG_ERROR("tried to change ownership of "<<obj->getUID()<<" from "<<com_id<<" to "<<_com_id<<endl);
	}
}

CommunityForwardingEntry::CommunityForwardingEntry(int _src, int _dst_min, int _dst_max, int _next_hop) :
								src(_src), dst_min(_dst_min), dst_max(_dst_max), next_hop(_next_hop) {
}
CommunityForwardingEntry::CommunityForwardingEntry(CommunityForwardingEntry& o) :
								src(o.src), dst_min(o.dst_min), dst_max(o.dst_max), next_hop(o.next_hop) {
}
CommunityForwardingEntry::CommunityForwardingEntry(const CommunityForwardingEntry& o) :
								src(o.src), dst_min(o.dst_min), dst_max(o.dst_max), next_hop(o.next_hop) {
}
void CommunityForwardingEntry::operator=(const CommunityForwardingEntry o) {
	src=o.src;
	dst_min=o.dst_min;
	dst_max=o.dst_max;
	next_hop=o.next_hop;
}
bool CommunityForwardingEntry::operator<(const CommunityForwardingEntry rhs) const {
	if(src==rhs.src) {
		if(dst_max<rhs.dst_min)
			return true;
		return false;
	}
	if(src<rhs.src) return true;
	return false;
}
bool CommunityForwardingEntry::operator>(const CommunityForwardingEntry rhs) const {
	if(src==rhs.src) {
		if(dst_max>rhs.dst_min)
			return true;
		return false;
	}
	if(src>rhs.src) return true;
	return false;
}



Partition* Partition::__instance__=0;

Partition* Partition::createInstance(UID_t _partition_id, Net* _topnet, bool useStatFile, bool useStatStream) {
	if(__instance__) {
		ssfnet_throw_exception(SSFNetException::other_exception, "There can only be one partition per process!");
	}
	__instance__=new Partition(_partition_id, _topnet,useStatFile,useStatStream);
	return __instance__;
}

Partition* Partition::getInstance() {
	return __instance__;
}

int Partition::VEI = 500;

Partition::Partition(UID_t _partition_id, Net* _topnet, bool useStatFile_, bool useStatStream_) :
								topnet(_topnet),
								partition_id(_partition_id),
								com_with_low_id(0),
								bootstrapped(false),
								mutex(0),
								useStatFile(useStatFile_),
								useStatStream(useStatStream_) {
	if (NULL == topnet) {
		LOG_ERROR("the topnet cannot be null!"<<endl)
	}
	uids.setPart(this);
}

Partition::~Partition() {
	communities.clear();
	uid_alignment_ranges.clear();
	ip_alignment_ranges.clear();
}

void Partition::init() {
	ApplicationSession::init_applications();
	LOG_DEBUG("Creating i/o channels"<<endl)
							for(Community::Map::iterator it = communities.begin();it != communities.end();it++){
								it->second->createChannels();
							}
	LOG_DEBUG("initializing topnet"<<endl)
	topnet->init();
}

void Partition::wrapup() {
	LOG_DEBUG("wrapping up topnet"<<endl)
							topnet->wrapup();
}

Community* Partition::getCommunityWithLowestId() {
	if(!com_with_low_id) {
		LOG_ERROR("Called getCommunityWithLowestId() before it was set in the partition!"<<endl);
	}
	return com_with_low_id;
}


Community* Partition::getCommunity(int com_id) {
	Community::Map::iterator i = communities.find(com_id);
	if(i != communities.end()) {
		return i->second;
	}
	LOG_WARN("Asked for community "<<com_id<<" in partition "<<getPartitionId()<<" but its not here!"<<endl)
	return 0;
}


void Partition::internal_sort() {
	sort(uid_alignment_ranges.begin(), uid_alignment_ranges.end());
	sort(ip_alignment_ranges.begin(), ip_alignment_ranges.end());
	sort(community_forwarding_entries.begin(), community_forwarding_entries.end());
}


Net* Partition::getTopnet() {
	return topnet;
}

int Partition::getPartitionId() {
	return partition_id;
}

Community::Map& Partition::getCommunities() {
	return communities;
}

void Partition::addCommunity(Community* com) {
	Community::Map::iterator rv = communities.find(com->getCommunityId());
	if (rv == communities.end()) {
		if(com_with_low_id==NULL) {
			com_with_low_id=com;
		}
		else if(com_with_low_id->getCommunityId() > com->getCommunityId()){
			com_with_low_id=com;
		}
		communities.insert(SSFNET_MAKE_PAIR(com->getCommunityId(),com));
	}
	else if(rv->second != com) {
		LOG_ERROR("Duplicate community ids! "<<com<<"["<<com->getCommunityId()<<"] and "<<rv->second<<"["<<rv->second->getCommunityId()<<"]"<<endl);
	}
}


void Partition::addUIDAlignedRange(UID_t low, UID_t high, int com_id) {
	uid_alignment_ranges.push_back(AlignmentRange<UID_t>(low,high,com_id));
}

void Partition::addIPAlignedRange(uint32 low, uint32 high, int com_id) {
	ip_alignment_ranges.push_back(AlignmentRange<uint32>(low,high,com_id));
}

void Partition::addForwardingEntry(int src, int dst_min, int dst_max, int next_hop) {
	community_forwarding_entries.push_back(CommunityForwardingEntry(src,dst_min,dst_max,next_hop));
}

int Partition::uid2communityId(UID_t uid) {
	AlignmentRange<UID_t> to_find(uid,uid,-1);
	AlignmentUIDBounds bounds=equal_range(uid_alignment_ranges.begin(), uid_alignment_ranges.end(), to_find);
	if(bounds.second - bounds.first >1) {
		LOG_WARN("THE UID ="<<uid<<" IS IN MULTPLE COMS!"<<endl);
		for(AlignmentUIDVec::iterator i = bounds.first;i<bounds.second;i++) {
			LOG_WARN("\tMATCH ["<<i->lower<<","<<i->upper<<"] --> "<<i->com_id<<endl;);
		}
	}
	for(AlignmentUIDVec::iterator i = bounds.first;i<bounds.second;i++) {
		if(uid >= i->lower && uid<=i->upper) {
			//LOG_INFO("found uid "<<uid<<" in community "<<bounds.first->com_id<<endl);
			return bounds.first->com_id;
		}
	}
	LOG_WARN("COULD NOT FIND  THE COMMUNITY WHICH OWNS UID="<<uid<<endl);
	return -1;
}

int Partition::ip2communityId(IPAddress ip) {
	AlignmentRange<uint32> to_find((uint32)ip,(uint32)ip,-1);
	AlignmentIPBounds bounds=equal_range(ip_alignment_ranges.begin(), ip_alignment_ranges.end(), to_find);
	if(bounds.second - bounds.first >1) {
		LOG_WARN("THE IP ="<<ip<<" IS IN MULTPLE COMS!"<<endl);
		for(AlignmentIPVector::iterator i = bounds.first;i<bounds.second;i++) {
			LOG_WARN("\tMATCH ["<<IPAddress(i->lower)<<","<<IPAddress(i->upper)<<"] --> "<<i->com_id<<endl;);
		}
	}
	for(AlignmentIPVector::iterator i = bounds.first;i<bounds.second;i++) {
		if((uint32)ip >= i->lower && (uint32)ip<=i->upper) {
			return bounds.first->com_id;
		}
	}
	LOG_WARN("COULD NOT FIND  THE COMMUNITY WHICH OWNS IP="<<ip<<endl);
	return -1;
}

int Partition::mac2communityId(MACAddress mac) {
	//mac is just UID
	return uid2communityId((uint64)mac);
}


void Partition::uid2localCommunities(UID_t uid, Community::Set& coms) {
	AlignmentRange<UID_t> to_find(uid,uid,-1);
	Community::Map::iterator com;
	AlignmentUIDBounds bounds=equal_range(uid_alignment_ranges.begin(), uid_alignment_ranges.end(), to_find);
	for(AlignmentUIDVec::iterator i = bounds.first;i!=bounds.second;i++) {
		//LOG_DEBUG("\tCHECKING ["<<i->lower<<","<<i->upper<<"] --> "<<i->com_id<<endl;);
		if(uid >= i->lower && uid<=i->upper) {
			com=communities.find(i->com_id);
			if(com!=communities.end()) {
				coms.insert(com->second);
			}
		}
	}
	//LOG_DEBUG("\t FOUND "<<coms.size()<<" communiteis which own uid "<<uid<<endl);
}


int Partition::getNextCommunityInPath(int src, int dst) {
	//LOG_DEBUG("LOOKING FOR NEXT COMMUNITY IN PATH FROM "<<src<<" to "<<dst<<endl);
	CommunityForwardingEntry to_find(src,dst,dst,-1);
	CommunityForwardingEntry::Bounds bounds=equal_range(community_forwarding_entries.begin(), community_forwarding_entries.end(), to_find);

	for(CommunityForwardingEntry::Vector::iterator i = bounds.first;i<bounds.second;i++) {
		if(i->dst_min<=dst && i->dst_max>=dst) {
			return bounds.first->next_hop;
		}
	}
	//LOG_DEBUG("\tNO ENTRY, they must be directly connected!"<<endl);
	return dst;
}

#ifdef SSFNET_EMULATION
EmulationDevice::List* Partition::getEmulationDevices(int itype) {
	//LOG_DEBUG("Looking for emulation devices of type "<<itype<<endl)
	EmulationDevice::ListMap::iterator iter = emu_devices.find(itype);
	if(iter != emu_devices.end()) {
		//LOG_DEBUG("\tfound "<<iter->second->size()<<endl)
		return iter->second;
	}
	//LOG_DEBUG("\tfound none!"<<endl)
	return 0;
}

void Partition::registerEmulationDevice(EmulationDevice* dev) {
	EmulationDevice::List* l=NULL;
	EmulationDevice::ListMap::iterator iter = emu_devices.find(dev->getDeviceType());
	if(iter != emu_devices.end()) {
		l=iter->second;
	}
	else {
		l=new EmulationDevice::List();
		emu_devices.insert(SSFNET_MAKE_PAIR(dev->getDeviceType(),l));
	}
	//make sure its not already in there
	for(EmulationDevice::List::iterator it=l->begin();it!=l->end();it++) {
		if(*it == dev) {
			return;
		}
	}
	l->push_back(dev);
}

Partition::PORTAL2NIC* Partition::portal_map = 0;

void Partition::addTrafficPortalMappping(UID_t uid, SSFNET_STRING* str) {
	if(__instance__) {
		LOG_ERROR("tried to add a mapping after the partition was created!");
	}
	if(!portal_map) {
		portal_map = new PORTAL2NIC();
	}
	portal_map->insert(SSFNET_MAKE_PAIR(uid,str));
}

SSFNET_STRING* Partition::getPortalNic(UID_t uid) {
	if(!portal_map) {
		return 0;
	}
	PORTAL2NIC::iterator rv = portal_map->find(uid);
	if(rv == portal_map->end()) {
		return 0;
	}
	return rv->second;
}

#else
void* Partition::getEmulationDevices(int itype) {
	LOG_ERROR("this function shouldn't be called." << endl);
	return 0;
}

void Partition::registerEmulationDevice(void* dev) {
	LOG_ERROR("this function shouldn't be called." << endl);
}
void Partition::addTrafficPortalMappping(UID_t uid, SSFNET_STRING* str) {
	LOG_ERROR("this function shouldn't be called." << endl);
}
SSFNET_STRING* Partition::getPortalNic(UID_t uid) {
	LOG_ERROR("this function shouldn't be called." << endl);
	return 0;
}

#endif

void Partition::debug(int total_num_coms) {
	LOG_DEBUG("**************************************" << endl);
	LOG_DEBUG("*************START DEBUG**************" << endl);
	LOG_DEBUG("**************************************" << endl);
	LOG_DEBUG("printing uid alignment ranges"<<endl);
	LOG_DEBUG("**************************************" << endl);
	for(uint32 i=0;i<uid_alignment_ranges.size();i++) {
		LOG_DEBUG("\t["<<uid_alignment_ranges[i].lower<<","<<uid_alignment_ranges[i].upper<<"] --> "<<uid_alignment_ranges[i].com_id<<endl);
	}
	LOG_DEBUG("**************************************" << endl);
	LOG_DEBUG("printing ip alignment ranges"<<endl);
	LOG_DEBUG("**************************************" << endl);
	for(uint32 i=0;i<ip_alignment_ranges.size();i++) {
		LOG_DEBUG("\t["<<IPAddress(ip_alignment_ranges[i].lower)<<","<<IPAddress(ip_alignment_ranges[i].upper)<<"] --> "<<ip_alignment_ranges[i].com_id<<endl);
	}
	LOG_DEBUG("**************************************" << endl);
	LOG_DEBUG("printing fwding entries"<<endl);
	LOG_DEBUG("**************************************" << endl);
	for(uint32 i=0;i<community_forwarding_entries.size();i++) {
		LOG_DEBUG("\t"<<community_forwarding_entries[i].src
				<<" --> ["<<community_forwarding_entries[i].dst_min <<","
				<< community_forwarding_entries[i].dst_max
				<<"] : "<<community_forwarding_entries[i].next_hop<<endl);
	}
	LOG_DEBUG("**************************************" << endl);
	LOG_DEBUG("testing routing"<<endl);
	LOG_DEBUG("**************************************" << endl);
	for(int i=0;i<total_num_coms;i++) {
		for(int j=0;j<total_num_coms;j++) {
			if(i!=j) {
				DEBUG_CODE(
				int nh = getNextCommunityInPath(i,j);
				LOG_DEBUG("\t["<<i<<","<<j<<"] --> "<<nh<<endl);
				);
			}
		}
	}
	LOG_DEBUG("**************************************" << endl);
	LOG_DEBUG("**************END DEBUG***************" << endl);
	LOG_DEBUG("**************************************" << endl);
}

Context* Partition::lookupContext(UID_t context_id, bool throw_exp) {
	//LOG_DEBUG("lookup context, uid="<<context_id<<", contexts="<<uids.size()<<endl)
	Context* rv = uids.get(context_id);
	if(rv==NULL) {
		releaseStructuralModifcationLock();
		if(throw_exp){
			char t[50];
			snprintf(t,50," UID=%llu",context_id);
			ssfnet_throw_exception(SSFNetException::unknown_context_uid,t);
		}
		return NULL;
	}
	//LOG_DEBUG("\tmapped to "<<(*rv).second<<endl)
	return rv;
}

void Partition::getStructuralModifcationLock() {
	if(bootstrapped && mutex) {
		if(pthread_mutex_lock(mutex)) {
			LOG_ERROR("Failed to obtain lock!"<<endl);
		}
	}
}

//used to synchronize adding/deleting children
void Partition::releaseStructuralModifcationLock() {
	if(bootstrapped && mutex) {
		if(pthread_mutex_unlock(mutex)) {
			LOG_ERROR("Failed to release lock!"<<endl);
		}
	}
}


void Partition::addContext(BaseEntity* c) {
	assert(c);
	//LOG_DEBUG("Adding context "<<c->getUName()<<", uid="<<c->getUID()<<", contexts="<<uids.size()<<endl)
	Context* cur = uids.get(c->getUID());
	if(cur != NULL) {
		LOG_WARN("parent "<<c->getParent()->getUName()<<", uid="<<c->getParent()->getUID()<<", offset="<<c->getOffset()<<", type="<<c->getParent()->getTypeName()<<endl)
								LOG_WARN("The node "<<cur->getObj()->getUName()<<" has the uid "<<cur->getObj()->getUID()<<"("<<(void*)cur->getObj()<<
										"), so we cant add "<<c->getUName()<<", uid="<<c->getUID()<<"("<<(void*)c<<")"<<", type="<<c->getTypeName()<<endl)
										ssfnet_throw_exception(SSFNetException::other_exception);
	}
	else {
		Context newc(c);
		//set the community... if the parent is owned then so is the child.....
		if(c->getParent()) {
			Context* p = uids.get(c->getUID());
			if(p != NULL) {
				newc.setCommunityId(p->getCommunityId());
			}
		}
		uids.add(c->getUID(),newc);
		if(newc.com_id>=0) {
			LOG_DEBUG("\tAdded context "<<c->getUName()<<", uid="<<c->getUID()<<", com_id="<<newc.com_id<<endl);
		}
	}
	//LOG_DEBUG("\tAdded context "<<c->getUName()<<", uid="<<c->getUID()<<endl)
}

void Partition::addComPartPair(int com_id, int part_id) {
	Com2PartMap::iterator i = com2part_map.find(com_id);
	if(i == com2part_map.end()) {
		com2part_map.insert(SSFNET_MAKE_PAIR(com_id,part_id));
	}
}

int Partition::communityId2partitionId(int com_id) {
	Com2PartMap::iterator i = com2part_map.find(com_id);
	if(i == com2part_map.end()) {
		return -1;
	}
	return i->second;
}
int Partition::partitionId2rank(int part_id) {
	Part2RankMap::iterator i = part2rank_map.find(part_id);
	if(i == part2rank_map.end()) {
		return -1;
	}
	return i->second;
}

void Partition::setPartRank(int part_id, int rank) {
	Part2RankMap::iterator i = part2rank_map.find(part_id);
	if(i == part2rank_map.end()) {
		part2rank_map.insert(SSFNET_MAKE_PAIR(part_id,rank));
	}
}

long Partition::getMemUsage() {
	return sizeof(Context)*(uids.getSlabSize()+uids.getNumSlabs())+sizeof(Context*)*(uids.getNumSlabs()+1);
}

void Partition::addBootstrapEvent(RoutingStatus* rs_request){
	pre_init_evt_list.push_back(rs_request);
}

void Partition::handle_event(BootStrapMessage* bm, SSFNetEvent*& response, int& dst_part) {
//	LOG_DEBUG("Handling evt"<<endl);
#if PRIME_SSF_DISTSIM
#ifdef PRIME_SSF_SYNC_MPI
	response=NULL;
	bm->unpack(MPI_COMM_WORLD);
	SSFNetEvent* evt = SSFNET_DYNAMIC_CAST(SSFNetEvent*,
			Event::_evt_create_registered_event(bm->usrevt_ident, bm->usrevt_packed));
	switch(evt->getSSFNetEventType()) {
	case SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT:
	{
		//LOG_DEBUG("Handling SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT"<<endl);
		RoutingStatusResponse* evt_ = SSFNET_DYNAMIC_CAST(RoutingStatusResponse*,evt);
		RoutingSphere* rs = SSFNET_DYNAMIC_CAST(RoutingSphere*,getCommunityWithLowestId()->getObject(evt_->getRoutingSphereId()));
		if(rs!=NULL){
			rs->handleRoutingEvent(evt_->getIfaceId(), evt_->isAvailable());
		}else{
			LOG_ERROR("The community("<<getCommunityWithLowestId()->getCommunityId()<<") should own the routing sphere with id="<<evt_->getRoutingSphereId()<<endl);
		}
		evt->free();
	}
	break;
	case SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT:
	{
		//LOG_DEBUG("Handling SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT"<<endl);
		RoutingStatusRequest* evt_ = SSFNET_DYNAMIC_CAST(RoutingStatusRequest*,evt);
		Community::Map::iterator com = communities.find(evt_->getTargetCommunity());
		if(com == communities.end()) {
			LOG_ERROR("The partition("<<getPartitionId()<<") does not own community("<< evt_->getTargetCommunity() <<")"<<endl);
		}
		else {
			Interface* iface = SSFNET_DYNAMIC_CAST(Interface*,com->second->getObject(evt_->getIfaceId()));
			if(iface!=NULL){
				//check if the interface is available
				bool is_available = iface->getLink()&&iface->isOn();
				//send the response event
				response = new RoutingStatusResponse(*evt_, is_available);
				dst_part = partitionId2rank(communityId2partitionId(response->getTargetCommunity()));
			}else{
				LOG_ERROR("The community("<< evt_->getTargetCommunity() <<") should own the interface with uid="<<evt_->getIfaceId()<<endl);
			}
		}
		evt->free();
	}
	break;
	case SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT:
	{
		//LOG_DEBUG("Handling SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT"<<endl);
		GhostRoutingSphereRegistrationResponse* evt_ = SSFNET_DYNAMIC_CAST(GhostRoutingSphereRegistrationResponse*,evt);
		GhostRoutingSphere* rs = SSFNET_DYNAMIC_CAST(GhostRoutingSphere*,getCommunityWithLowestId()->getObject(evt_->getRoutingSphereId()));
		if(rs!=NULL){
			rs->handleGhostRegistrationResponse(evt_);
		}else{
			LOG_ERROR("The community("<<getCommunityWithLowestId()->getCommunityId()<<") should own the ghost outing sphere with id="<<evt_->getRoutingSphereId()<<endl);
		}
		evt->free();
	}
	break;
	case SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_EVT:
	{
		//LOG_DEBUG("Handling SSFNET_GHOST_ROUTING_SPHERE_REG_EVT"<<endl);
		GhostRoutingSphereRegistration* evt_ = SSFNET_DYNAMIC_CAST(GhostRoutingSphereRegistration*,evt);
		RoutingSphere* rs = SSFNET_DYNAMIC_CAST(RoutingSphere*,getCommunityWithLowestId()->getObject(evt_->getRoutingSphereId()));
		if(rs!=NULL){
			response = rs->handleGhostRegistration(evt_->getSrcCommunityId());
			dst_part = partitionId2rank(communityId2partitionId(response->getTargetCommunity()));
		}else{
			LOG_ERROR("The community("<<getCommunityWithLowestId()->getCommunityId()<<") should own the route sphere with id="<<evt_->getRoutingSphereId()<<endl);
		}
		evt->free();
	}
	break;
	default:
	{
		LOG_ERROR("The event type "<<evt->getSSFNetEventType()<<" is not a valid boot strap event!"<<endl);
	}
	break;
	}
#else
	LOG_ERROR("Something is terribly wrong. Partition::handle_event was called but we are not running with mpi!"<<endl);
#endif
#endif
}

void Partition::bootstrap() {
	//Setup the part_id to rank mapping
#if PRIME_SSF_DISTSIM
#ifdef PRIME_SSF_SYNC_MPI
	int mpi_nmachs=0, mpi_rank=0, expected_status_evt_count=0,
			expected_ghost_evt_count=0;
	PreInitEventList status_evts;
	PreInitEventList ghost_evts;
	SSFNET_LIST(BootStrapMessage*) outstanding_sends;
	SSFNET_LIST(BootStrapMessage*) outstanding_recvs;

	MPI_Comm_size(MPI_COMM_WORLD, &mpi_nmachs);
	MPI_Comm_rank(MPI_COMM_WORLD, &mpi_rank);

	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] Setup the part_id to rank mapping, mpi_nmachs="<<(int)mpi_nmachs<<", mpi_rank="<<(int)mpi_rank<<endl);

	int* pmap= new int[mpi_nmachs];
	int* counts= new int[2*mpi_nmachs];
	int* all_counts= new int[2*mpi_nmachs*mpi_nmachs];
	assert(pmap && counts && all_counts);

	bzero(pmap,sizeof(int)*mpi_nmachs);
	bzero(counts,sizeof(int)*mpi_nmachs*2);
	bzero(all_counts,sizeof(int)*2*mpi_nmachs*mpi_nmachs);

	assert(pmap && counts && all_counts);

	// setup ranks
	pmap[mpi_rank]=getPartitionId();
	if(MPI_Allgather( &partition_id, 1, MPI_INT, pmap, 1, MPI_INT, MPI_COMM_WORLD)) {
		LOG_ERROR("Failure while building partition to rank map"<<endl)
	}
	for(int i=0;i<mpi_nmachs;i++) {
		setPartRank(pmap[i],i);
		LOG_DEBUG("[rank="<<(int)mpi_rank<<"] rank="<<(int)i<<", part_id="<<(int)pmap[i]<<endl);
	}
	delete[] pmap;
	pmap=0;

	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] Sorting events"<<endl);

	//sort the pre-event list and setup counts
	while(pre_init_evt_list.size()>0) {
		SSFNetEvent* evt = pre_init_evt_list.front();
		pre_init_evt_list.pop_front();
		switch(evt->getSSFNetEventType()) {
		case SSFNetEvent::SSFNET_PKT_EVT:
		case SSFNetEvent::SSFNET_EMU_NORMAL_EVT:
		case SSFNetEvent::SSFNET_EMU_PROXY_EVT:
		case SSFNetEvent::SSFNET_TRAFFIC_START_EVT:
		case SSFNetEvent::SSFNET_TRAFFIC_UPDATE_EVT:
		case SSFNetEvent::SSFNET_TRAFFIC_FINISH_EVT:
		case SSFNetEvent::SSFNET_FLUID_REGISTER_EVT:
		case SSFNetEvent::SSFNET_FLUID_REGISTER_REVERSE_EVT:
		case SSFNetEvent::SSFNET_FLUID_UNREGISTER_EVT:
		case SSFNetEvent::SSFNET_FLUID_ARRIVAL_EVT:
		case SSFNetEvent::SSFNET_FLUID_ACCU_DELAY_EVT:
		case SSFNetEvent::SSFNET_FLUID_ACCU_LOSS_EVT:
		case SSFNetEvent::SSFNET_NAME_SRV_EVT:
		{
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] The event type "<<evt->getSSFNetEventType()<<" is not a valid boot strap event!"<<endl);
		}
		break;
		case SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT:
		case SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT:
		{
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Did not expect to see the response in the out bound event list! Event type "<<evt->getSSFNetEventType()<<endl);
		}
		break;
		case SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT:
		{
			status_evts.push_back(evt);
			int pid = communityId2partitionId(evt->getTargetCommunity());
			int rank = partitionId2rank(pid);
			assert(rank*2<2*mpi_nmachs);
			counts[rank*2]++;
		}
		break;
		case SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_EVT:
		{
			ghost_evts.push_back(evt);
			int pid = communityId2partitionId(evt->getTargetCommunity());
			int rank = partitionId2rank(pid);
			assert(rank*2+1<2*mpi_nmachs);
			counts[rank*2+1]++;
		}
		break;
		default:
			LOG_ERROR("[rank="<<(int)mpi_rank<<"] Unknown SSFNetEvent type "<<evt->getSSFNetEventType()<<", SSFNET_EVT_MIN="<< SSFNetEvent::SSFNET_EVT_MIN<<", SSFNET_EVT_MAX="<< SSFNetEvent::SSFNET_EVT_MAX<<endl);
			break;
		}
	}

	//gather all counts together...
	if(MPI_Allgather(counts, 2*mpi_nmachs, MPI_INT, all_counts, 2*mpi_nmachs, MPI_INT, MPI_COMM_WORLD)) {
		LOG_ERROR("Failure while exchanging counts"<<endl)
	}

	//sum how many events each rank intends to send
	for(int i=0;i<mpi_nmachs;i++) {
		int* t_counts=&(all_counts[i*(2*mpi_nmachs)]);
		counts[2*i]+=t_counts[2*mpi_rank];
		counts[2*i+1]+=t_counts[2*mpi_rank+1];
		expected_status_evt_count+=t_counts[2*mpi_rank];
		expected_ghost_evt_count+=t_counts[2*mpi_rank+1];
		LOG_DEBUG("[rank="<<(int)mpi_rank<<"] expect "<<(int)counts[2*i]<<" iface evts and "<<(int)counts[2*i+1]<<" ghost events from "<<i<<endl);
	}
	LOG_DEBUG("[rank="<<(mpi_rank)<<"] expected_status_evt_count="<<(int)expected_status_evt_count<<" expected_ghost_evt_count="<<(int)	expected_ghost_evt_count<<endl);


	//Send out all the status events and wait for responses
	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] sending status requests ("<<status_evts.size()<<")"<<endl);
	while(status_evts.size()>0) {
		int dst_part = partitionId2rank(communityId2partitionId(status_evts.front()->getTargetCommunity()));
		//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] need to send "<<status_evts.size()<<" status events, so lets start an asynchronous send to part "<<dst_part<<endl);
		outstanding_sends.push_back((new BootStrapMessage(status_evts.front(),mpi_rank))->ISend(dst_part));
		status_evts.front()->free();
		status_evts.pop_front();

		//for each request I expect a response....
		outstanding_recvs.push_back((new BootStrapMessage(mpi_rank))->IRecv());

	}
	for(int i=0;i<expected_status_evt_count;i++) {
		//i also expect a few requests
		outstanding_recvs.push_back((new BootStrapMessage(mpi_rank))->IRecv());
	}

	//Send out all the status events and wait for responses
	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] sending/receiving status responses ("<<outstanding_sends.size()<<","<<outstanding_recvs.size()<<")"<<endl);
	while(outstanding_sends.size()>0 || outstanding_recvs.size() > 0) {
		//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] (in the loop) sending/receiving status responses ("<<outstanding_sends.size()<<","<<outstanding_recvs.size()<<") expected_status_evt_count="<<expected_status_evt_count<<endl);

		BootStrapMessage *send = outstanding_sends.size()>0?outstanding_sends.front():0;
		BootStrapMessage *recv = outstanding_recvs.size()>0?outstanding_recvs.front():0;

		if(recv && recv->Test()) {
			//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished a recv, handling evt! "<<endl);
			SSFNetEvent* response=NULL;
			int dst_part;
			handle_event(recv,response,dst_part);
			if(response != NULL) {
				//this means it was a status request...
				outstanding_sends.push_back((new BootStrapMessage(response,mpi_rank))->ISend(dst_part));
				//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished handling evt; sending a response to "<<dst_part<<endl);
			}
			else {
				//it was a response to a status query!
				expected_status_evt_count--;
				//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished handling evt; NOT sending a response"<<endl);
			}
			outstanding_recvs.pop_front();
			delete recv;
		}
		if(send && send->Test()) {
			//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished a send!"<<endl);
			outstanding_sends.pop_front();
			delete send;
		}
	}

	//Send out all the ghost events and wait for responses
	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] sending ghost requests("<<ghost_evts.size()<<")"<<endl);
	while(ghost_evts.size()>0) {
		int dst_part = partitionId2rank(communityId2partitionId(ghost_evts.front()->getTargetCommunity()));
		//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] need to send "<<ghost_evts.size()<<" ghost events, so lets start an asynchronous send to part "<<dst_part<<endl);
		outstanding_sends.push_back((new BootStrapMessage(ghost_evts.front(),mpi_rank))->ISend(dst_part));
		ghost_evts.front()->free();
		ghost_evts.pop_front();
		//for each request I expect a response....
		outstanding_recvs.push_back((new BootStrapMessage(mpi_rank))->IRecv());

	}
	for(int i=0;i<expected_ghost_evt_count;i++) {
		//i also expect a few requests
		outstanding_recvs.push_back((new BootStrapMessage(mpi_rank))->IRecv());
	}

	//Send out all the status events and wait for responses
	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] sending/receiving ghost responses ("<<outstanding_sends.size()<<","<<outstanding_recvs.size()<<")"<<endl);
	while(outstanding_sends.size()>0 || outstanding_recvs.size() > 0 ) {
		//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] (in the loop) sending/receiving status responses ("<<outstanding_sends.size()<<","<<outstanding_recvs.size()<<") expected_ghost_evt_count="<<expected_ghost_evt_count<<endl);

		BootStrapMessage *send = outstanding_sends.size()>0?outstanding_sends.front():0;
		BootStrapMessage *recv = outstanding_recvs.size()>0?outstanding_recvs.front():0;

		if(recv && recv->Test()) {
			//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished a recv, handling evt! "<<endl);
			SSFNetEvent* response=NULL;
			int dst_part;
			handle_event(recv,response,dst_part);
			if(response != NULL) {
				//this means it was a status request...
				outstanding_sends.push_back((new BootStrapMessage(response,mpi_rank))->ISend(dst_part));
				//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished handling evt; sending a response to "<<dst_part<<endl);
			}
			else {
				//it was a response to a query!
				expected_ghost_evt_count--;
				//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished handling evt; NOT sending a response"<<endl);
			}
			outstanding_recvs.pop_front();
			delete recv;
		}
		if(send && send->Test()) {
			//LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished a send!"<<endl);
			outstanding_sends.pop_front();
			delete send;
		}
	}

	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] cleaning up!"<<endl);
	delete[] all_counts;
	delete[] counts;
	LOG_DEBUG("[rank="<<(int)mpi_rank<<"] finished sending/receiving all events!"<<endl);
#else
	if(pre_init_evt_list.size()!=0) {
		LOG_ERROR("Something is terribly wrong. We have bootstrap events but we only expected _1_ partition!"<<endl);
	}
#endif
#endif
	if(this->getCommunities().size()>1) {
		mutex = new pthread_mutex_t;
		if(pthread_mutex_init(mutex,NULL)) {
			LOG_ERROR("Unable to init mutex!\n");
		}
	}
	bootstrapped=true;
}





ContextMap::ContextMap(uint32_t slabSize_, uint32_t numSlabs_):
								slabSize(slabSize_), numSlabs(numSlabs_), contexts(0) {
	contexts = new Context*[numSlabs];
	bzero(contexts, sizeof(Context*)*numSlabs);
}

void ContextMap::growSlabs(uint32_t newSize) {
	part->getStructuralModifcationLock();
	Context** old = contexts;
	contexts = new Context*[newSize];
	for(uint32_t i=0;i<newSize;i++) {
		if(i<numSlabs) {
			contexts[i] = old[i];
		}
		else {
			contexts[i] = 0;
		}
	}
	delete[] old;
	numSlabs = newSize;
	part->releaseStructuralModifcationLock();
}


ContextMap::ContextMap(const ContextMap& o) {
	LOG_ERROR("DONT DO THIS!\n");
}
Context* ContextMap::get(UID_t context_id) const {
	const uint32_t slab = (uint32_t) (context_id / slabSize);
	const uint32_t offset = context_id % slabSize;
	if(slab < numSlabs) {
		if(contexts[slab] && contexts[slab][offset].obj) {
			return &(contexts[slab][offset]);
		}
	}
	return 0;
}

bool ContextMap::add(UID_t context_id, Context& c) {
	const uint32_t slab = (uint32_t) (context_id / slabSize);
	const uint32_t offset = context_id % slabSize;
	if(numSlabs <= slab) {
		growSlabs(slab+(int)(numSlabs*0.25));
	}
	if( 0 == contexts[slab] ) {
		contexts[slab] = new Context[slabSize];
		contexts[slab][offset] = c;
		return true;
	}
	else if( 0 == contexts[slab][offset].obj) {
		contexts[slab][offset] = c;
		return true;
	}
	return false;
}




} // namespace ssfnet
} // namespace prime
