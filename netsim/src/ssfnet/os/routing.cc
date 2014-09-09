/**
 * \file routing.cc
 * \brief Source file for the Routing and RouteTable classes.
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
#include<math.h>
#include "os/logger.h"
#include "os/routing.h"
#include "net/net.h"
#include "proto/ipv4/ipv4_message.h"
#include "os/partition.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(RoutingSphere);

//#define LOG_WARN(X) std::cout<<"[routing.cc:"<<__LINE__<<"]"<<X;
//#define LOG_DEBUG(X) std::cout<<"[routing.cc:"<<__LINE__<<"]"<<X;

EdgeInterfaceListState::EdgeInterfaceListState()
:reference_count(0), ready_to_free(0), edges(new EdgeList()){
}

EdgeInterfaceListState::~EdgeInterfaceListState(){
	if(edges) delete edges;
}

EdgeInterfaceList::EdgeInterfaceList()
	:state(new EdgeInterfaceListState())  {
}
EdgeInterfaceList::~EdgeInterfaceList() {
	//state must be delete by EdgeInterfaceWrapper or manually
}

EdgeInterfaceList EdgeInterfaceList::operator=(const EdgeInterfaceList& x) {
	EdgeInterfaceListState* old=state;
	state=x.state;

	old->ready_to_free=1;
	if(old->reference_count<=0) {
		delete old;
	}
	//else someone else is using it and the EdgeInterfaceListWrapper will delete it.
	return *this;
}

EdgeInterfaceListWrapper EdgeInterfaceList::reference() {
	EdgeInterfaceListWrapper rv(state);
	return rv;
}

EdgeInterfaceListState* EdgeInterfaceList::getState(){
	return state;
}

EdgeInterfaceListWrapper::EdgeInterfaceListWrapper(EdgeInterfaceListState* _state): state(_state) {
	__sync_fetch_and_add( &(state->reference_count), 1 ); //atomic increment
}
EdgeInterfaceListWrapper::~EdgeInterfaceListWrapper() {
	if(state->ready_to_free && state->reference_count<=0) {
		delete state;
	}
	else {
		__sync_fetch_and_sub( &(state->reference_count), 1 ); //atomic decrement
	}
}
EdgeList& EdgeInterfaceListWrapper::getEdgesInterfaces() {
	return *(state->edges);
}

uint EdgeInterfaceListWrapper::size() {
	return state->edges->size();
}
EdgeList::iterator EdgeInterfaceListWrapper::begin() {
	return state->edges->begin();

}
EdgeList::iterator EdgeInterfaceListWrapper::end() {
	return state->edges->end();
}

RoutingSphere::RoutingSphere() :
	superSphere(0), rtIsCached(0), rtCache(0){
}

RoutingSphere::~RoutingSphere() {
	if(getRouteTable())
		delete getRouteTable();
}

void RoutingSphere::init() {
	if (getRouteTable()) {
		getRouteTable()->init();
	}
	//LOG_DEBUG("net name="<<this->getOwningNet()<<getUName()<<", uid="<<this->getOwningNet()->getUID()<<endl);
	SubEdgeList& sub_edge_list = getOwningNet()->getSubEdgeInterfaces();
	if(sub_edge_list.size() > 1){
		SrcRankListPairMap uid_srcs_map; //host rank, a list of ranks of the edge interfaces on this host
		for (SubEdgeList::iterator i = sub_edge_list.begin(); i != sub_edge_list.end(); i++) {
			UID_t host_rank = (*i).getHostLocalRank();
			UID_t src_rank = (*i).getLocalRank();
			if(uid_srcs_map.find(host_rank)!=uid_srcs_map.end()){
				(*uid_srcs_map.find(host_rank)).second.push_back(src_rank);
			}else{
				SrcRankList pair_list;
				pair_list.push_back(src_rank);
				uid_srcs_map.insert(SSFNET_MAKE_PAIR(host_rank, pair_list));
			}
		}

		for (SrcRankListPairMap::iterator i = uid_srcs_map.begin(); i != uid_srcs_map.end(); i++) {
			if((*i).second.size()<2){//if the number of edge interfaces on one host is less than 2
				continue;
			}
			HostSrcRankListPair* p = new HostSrcRankListPair();
			p->first  = (*i).first;
			p->second = (*i).second;
			for(SrcRankList::iterator j = (*i).second.begin(); j != (*i).second.end(); j++){
				unshared.sub_edge_iface_map.insert(SSFNET_MAKE_PAIR(*j,p));
			}
		}
	}
}

void RoutingSphere::wrapup() {
	if (getRouteTable())
		getRouteTable()->wrapup();
}

uint32_t RoutingSphere::getNixVectorCacheSize() {
	return shared.nix_vec_cache_size.read();
}

uint32_t RoutingSphere::getLocalDstCacheSize() {
	return shared.local_dst_cache_size.read();
}

void RoutingSphere::setNixVectorCacheSize(uint32_t s) {
	shared.nix_vec_cache_size.read()=s;
}

void RoutingSphere::setLocalDstCacheSize(uint32_t s) {
	shared.local_dst_cache_size.read()=s;
}


bool RoutingSphere::getOutboundIface(Host* host, Packet* pkt,
		Interface*& outbound_iface) {
#if TEST_ROUTING == 1
	uint64_t before=cur_timestamp();
#endif
	LOG_DEBUG(": RoutingSphere::getOutboundIface(src_mac="<<pkt->getSrc()
			<<", dst_mac="<<pkt->getDst()
			<<", final_dst="<<pkt->getFinalDestinationUID()
			<<", local_dst="<<pkt->getLocalDestinationUID()<<")"
			<<" host="<<host->getUName()<<endl)
	outbound_iface = NULL;
	Community* comm=host->getCommunity();
	if (!pkt->getNixVector()) {
		//LOG_DEBUG("Creating new nix vector"<<endl)
		//The nix vector needs to be built
		this->createNixVector(this->getRank(host->getUID(),this), pkt, comm);
	}else {
		//LOG_DEBUG("already have nix vector"<<endl);
	}

	if(!pkt->getNixVector()) {
		//the packet was not routable!
#if TEST_ROUTING == 1
		pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::TOTAL,getParent()->getUID(),cur_timestamp()-before));
#endif
		return false;
	}
	//else we already have a nix vector
	//LOG_DEBUG("nix vector="<<pkt->getNixVector()<<endl)
	if(pkt->getNixVector()->getRemainingBits() == 0) {
		//LOG_DEBUG("DEBUG NIX VEC, remaining bits is 0!"<<endl);
		if(host->containsUID(pkt->getLocalDestinationUID())) {
			//we have arrived at the edge
			//there are two possibilities
			//1) We are at the final dst
			//2) We are at the local dst (i.e. an edge node)
			if(host->containsUID(pkt->getFinalDestinationUID())) {
				//we are done
				LOG_WARN("The final destination asked us to route the pkt back to it!"<<endl)
				delete pkt->dropNixVector();
#if TEST_ROUTING == 1
				pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::TOTAL,getParent()->getUID(),cur_timestamp()-before));
#endif
				return false;
			}
			else {
				//We are crossing spheres.
				if (!getOwningNet()->containsUID(pkt->getFinalDestinationUID())) {
					if(getOwningNet()->containsUID(pkt->getPrevRoutingSphereUID())){
						//We are crossing from sub-sphere to super-sphere.
						LOG_DEBUG("we are in sub-sphere "<<this->getUName()<<", final_dst="<<pkt->getFinalDestinationUID()<<endl);
						getSuperRoutingSphere()->createNixVector(this->getRank(host->getUID(),this->getSuperRoutingSphere()),pkt, comm);
					}else{
						//We are going through a transit valley
						LOG_DEBUG("we are in a transit valley."<<endl)
						UIDCostPairVector srcs;
						for (EdgeList::iterator i = getEdgeInterfaces().begin(); i != getEdgeInterfaces().end(); i++) {
								LOG_DEBUG("using edge iface "<<(*i).getParentRank(this, getSuperRoutingSphere())<<endl);
								srcs.push_back(
									SSFNET_MAKE_PAIR((*i).getParentRank(this, getSuperRoutingSphere()),0));
								}
						getSuperRoutingSphere()->mapToLocalDsts(this,pkt->getFinalDestinationUID(), srcs);
						//pick the best new_dst.... and save it to dst_rank
						UID_t temp_rank;
						LOG_DEBUG("The current sphere is: "<<this->getUName()<<endl)
						UIDCostPair new_dst=SSFNET_MAKE_PAIR(0,0xFFFF);

						for (UIDCostPairVector::iterator i = srcs.begin(); i != srcs.end(); i++) {
							LOG_DEBUG("new_dst is "<<i->first<<", cumulative cost="<<i->second<<endl)
							//i->first is not uid, it is the rank in terms of the parent net.
							temp_rank = getChildRank(i->first, this);

							RouteEntry* next_hop = getNextHop(this->getRank(host->getUID(),this), temp_rank);
							if (next_hop && i->second != -1) {
								LOG_DEBUG("src_rank="<<this->getRank(host->getUID(),this)<<", dst_rank="<<temp_rank
										<<", cost="<<next_hop->getCost()<<", next_hop_uid="<<getUID(next_hop->getNextHopRank(),this)<<endl);
								if (new_dst.second >= (next_hop->getCost() + i->second)) {
										new_dst.first = temp_rank;
										new_dst.second = next_hop->getCost() + i->second;
								}
		     				//else we have a better choice already
							} else if (!next_hop && new_dst.first == 0) {
								//if the src and the mapped local dst are in one node, the next_hop is null.
								LOG_DEBUG("next hop is null, src_rank="<<this->getRank(host->getUID(),this)<<", dst_rank="<<temp_rank<<endl)
								new_dst.first = temp_rank;
								new_dst.second = i->second;
							}
						}
						UID_t uid_offset=getOwningNet()->getUID()-getOwningNet()->getSize();
						pkt->setLocalDestinationUID(new_dst.first + uid_offset );
						createNixVector(this->getRank(host->getUID(),this), pkt, comm);
					}
				} else {
					//We are crossing from super-sphere to sub-sphere.
					LOG_DEBUG("we are in super-sphere "<<this->getUName()<<endl)
					createNixVector(this->getRank(host->getUID(),this), pkt, comm);
				}
			}
		}
		else {
			//re-route the packet from here
			LOG_DEBUG("we are in transit sphere "<<this->getUName()<<endl)
			createNixVector(this->getRank(host->getUID(),this), pkt, comm);
		}
	}
	if(!pkt->getNixVector() || pkt->getNixVector()->getRemainingBits() <= 0) {
		IPv4Message* iph = SSFNET_DYNAMIC_CAST(IPv4Message*,pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4));
		assert(iph);
		LOG_WARN("The nix vector is invalid! src="<<iph->getSrc()
				<<", dst="<<iph->getDst()
				<<", pkt->getNixVector()="<<pkt->getNixVector()
				<<endl)
		delete pkt->dropNixVector();
#if TEST_ROUTING == 1
		pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::TOTAL,getParent()->getUID(),cur_timestamp()-before));
#endif
		return false;
	}
	//figure out iface_idx from nix vector
	int num_of_bits = (int)host->getSize();
	num_of_bits = pkt->getNixVector()->getNumOfBits(num_of_bits);
	//LOG_DEBUG("Number of ifaces="<<(int)num_of_bits<<endl)
	//figure out iface from host and iface_ids
	const int iface_rank = pkt->getNixVector()->getOutboundIfaceIdx(num_of_bits);
	//LOG_DEBUG("iface_rank="<<iface_rank<<"(uid="<<(iface_rank+(host->getUID()-host->getSize()))<< ")"<<endl);
	outbound_iface = SSFNET_DYNAMIC_CAST(Interface*,host->getCommunity()->getObject(iface_rank+(host->getUID()-host->getSize())));
	if (!outbound_iface) {
		//There was a failure in routing.....
		//if there is exactly 1 iface we will choose it if pkt->getSrc()==MACAddress::MACADDR_INVALID
		// or, if there are exactly 2 ifaces we will choose the iface  with the mac != pkt->getSrc()
		//as long as pkt->getSrc()!=MACAddress::MACADDR_INVALID
		LOG_WARN("There was a failure in routing...."<<endl)
		int count = 0;
		ChildIterator<Interface*> si = host->getInterfaces();
		while (si.hasMoreElements()) {
			Interface* nic = si.nextElement();
			if (outbound_iface == NULL || nic->getMAC() != pkt->getSrc()) {
				outbound_iface = nic;
			}
			count++;
		}
		if (count == 1 && pkt->getSrc() == MACAddress::MACADDR_INVALID) {
			//this is source of the packet
		} else if (count == 2 && pkt->getSrc() != MACAddress::MACADDR_INVALID) {
			//we simple forward the pkt
		} else {
			//whatever we found is probably incorrect
			outbound_iface = NULL;
		}
	}

	/*DEBUG_CODE(
			if(outbound_iface) {
				LOG_DEBUG("The outbound interface nix idx="<<iface_idx<<"outbound_iface="<<outbound_iface->getUName()<<endl)
			}
			else {
				LOG_DEBUG("outbound_iface="<<outbound_iface<<endl)
			})*/
#if TEST_ROUTING == 1
	pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::TOTAL,getParent()->getUID(),cur_timestamp()-before));
#endif
	return outbound_iface != NULL;
}

void RoutingSphere::mapToLocalDsts(RoutingSphere* source_sphere, UID_t dst_uid, UIDCostPairVector& srcs) {
	UIDCostPairVector new_dsts;
	RouteEntry* next_hop;
	uint16_t cost;
	bool i_own_dst = getOwningNet()->containsUID(dst_uid);
	//If any of the srcs can't get to the destination make sure to set the cost to -1
	/*LOG_DEBUG(": [start mapToLocalDsts]The current net is "<<getOwningNet()->getUName()
			<<", contains dst="<<i_own_dst
			<<", source sphere="<<source_sphere->getUName()
			<<", uid=["<< (source_sphere->getUID()-source_sphere->getSize()) <<"," <<source_sphere->getUID()<<"]"<<endl)
	*/
	if (i_own_dst) {
		//check if the current sphere is the dst's immediate sphere or not
		RoutingSphere* owning_sphere = getOwningNet()->findOwningSphere(dst_uid);
		//If the current sphere is the dst's immediate sphere, use the dst as the new destination with cost 0.
		if(owning_sphere == 0) {
			LOG_ERROR("Should never see this: In dst's owing net, no sphere owns the dst."<<endl)
		}
		else if (owning_sphere == this){
			LOG_DEBUG("owning sphere="<<owning_sphere->getUName()<<", this sphere="<<this->getUName()<<endl)
			new_dsts.push_back(SSFNET_MAKE_PAIR(getRank(dst_uid, owning_sphere),0));
		} else {
			/*
			 * If the current sphere is not the immediate routing sphere of the dst,
			 * use the edge interfaces of its owning sphere as the new destinations with cost 0.
			 */
			LOG_DEBUG("  The size of the edge interfaces is:"<<owning_sphere->getEdgeInterfaces().size()<<endl);
			LOG_DEBUG("owning sphere="<<owning_sphere->getUName()<<", this sphere="<<this->getUName()<<endl)
			for (EdgeList::iterator  i = owning_sphere->getEdgeInterfaces().begin(); i != owning_sphere->getEdgeInterfaces().end(); i++) {
				LOG_DEBUG("edge interface uid="<<(*i).getParentRank(owning_sphere, this)<<endl);
				new_dsts.push_back(SSFNET_MAKE_PAIR((*i).getParentRank(owning_sphere, this),0));
			}
		}
	} else {
		//set the edge interfaces as the new srcs
		UIDCostPairVector new_srcs;
		//LOG_DEBUG("net="<<getOwningNet()->getUName()<<", super sphere="<<getSuperRoutingSphere()<<endl)
		if (getSuperRoutingSphere() != NULL) {
			for (EdgeList::iterator i = getEdgeInterfaces().begin(); i != getEdgeInterfaces().end(); i++) {
				new_srcs.push_back(SSFNET_MAKE_PAIR((*i).getParentRank(this, getSuperRoutingSphere()), 0));
				new_dsts.push_back(SSFNET_MAKE_PAIR((*i).getLocalRank(), 0));
			}
			getSuperRoutingSphere()->mapToLocalDsts(this, dst_uid, new_srcs);
			//update the costs of the new dsts
			for(uint i=0;i<new_srcs.size();i++) {
				new_dsts[i].second=new_srcs[i].second;
				LOG_DEBUG(": Mapped edge iface to local dst uid: "<<getUID(new_dsts[i].first, this)<<", cost="<<new_dsts[i].second<<endl)
			}
		}
		else {
			LOG_ERROR("Should never see this: no sphere owns the dst. dst_uid="<<dst_uid<<endl)
		}
	}
	for (UIDCostPairVector::iterator src = srcs.begin(); src != srcs.end(); src++) {
		LOG_DEBUG("src rank = "<<src->first<<endl)
        cost = 0xFFFF;
        UID_t outbound_iface_rank=0;
		for (UIDCostPairVector::iterator dst = new_dsts.begin(); dst != new_dsts.end(); dst++) {
			if (dst->second != -1) {
				LOG_DEBUG("dst rank = "<<dst->first<<endl)
				next_hop=getNextHop(src->first, dst->first);
				if(next_hop==NULL) {
					//just ignore this this src/dst pair...
					LOG_DEBUG("\tInvalid path from "<<src->first<<" to "<<dst->first<<endl)
				} else {
					LOG_DEBUG(": get next_hop, src rank="<<src->first<<", dst rank="<<dst->first<<" -->"
						<< "next hop rank="<<next_hop->getNextHopRank()<<", cost="<<next_hop->getCost()<<endl);
					LOG_DEBUG(": getCost="<<next_hop->getCost()<<", source net="<<source_sphere->getOwningNet()->getUName()
							<<", source net contains nexthop="<<source_sphere->getOwningNet()->containsUID(getUID(next_hop->getNextHopRank(),this))<<endl);
					if(next_hop->getCost()>0 && source_sphere->getOwningNet()->containsUID(getUID(next_hop->getNextHopRank(),this))) {
						LOG_DEBUG(": Ignoring next hop since going back into the source sphere!"<<endl);
					}
					else {
						if(cost>(next_hop->getCost()+dst->second)) {
							cost=next_hop->getCost()+dst->second;
							src->second=cost;
							//get owning host's uid
							//LOG_DEBUG("Offset = "<<getOwningNet()->getUID()-getOwningNet()->getSize()<<endl)
							UID_t owning_host_uid = next_hop->getOwningHost()+getOwningNet()->getUID()-getOwningNet()->getSize();
							/*LOG_DEBUG("cur net="<<getOwningNet()->getUName()<<", Owning host uid = "<<owning_host_uid<<
									", getOwningHost="<<next_hop->getOwningHost()<<", host size="<<(int)(next_hop->getNumberOfBits())
									<<", outbound iface rank="<<next_hop->getOutboundIfaceIdx()<<endl);*/
							//get the outbound iface's uid
							outbound_iface_rank = next_hop->getOutboundIfaceIdx()+owning_host_uid-next_hop->getNumberOfBits(); //uid
							//get the outbound iface's rank in terms of the sphere
							outbound_iface_rank = outbound_iface_rank-(getOwningNet()->getUID()-getOwningNet()->getSize());
						}
					}
				}
			}
			//else its an invalid dst....
			if(cost == 0xFFFF) {
				src->second=-1;
				LOG_DEBUG("Found no valid paths from "<<src->first<<" to dst...."<<dst->first<<endl)
			}
		}
		LOG_DEBUG("outbound_iface rank = "<<outbound_iface_rank<<endl);
		//if src is on the same host with other srcs, check if src is outbound iface
		//if not, src->second=-1;

		if(src->second!=-1 && unshared.sub_edge_iface_map.find(src->first)!=unshared.sub_edge_iface_map.end()) {
			//the iface is on a host that has multiple edge ifaces...
			if(src->first != outbound_iface_rank){
				src->second = -1;
			}
		}
	}
	LOG_DEBUG(": [end mapToLocalDsts]The current net is "<<getOwningNet()->getUName()<<", contains dst="<<getOwningNet()->containsUID(dst_uid)<<endl)
}

// rank=UID-(sum of the offsets of the nets), nets are all the parent nets of the uid who sphere as well
UID_t RoutingSphere::getRank(UID_t uid, RoutingSphere* rs) {
	return (uid-(rs->getOwningNet()->getUID()-rs->getOwningNet()->getSize()));
	/*
	 * the hard way:
	 *
	UID_t rank = uid;
	BaseEntity* cur=rs->getOwningNet();
	LOG_DEBUG("\trank="<<rank<<endl)
	while(cur) {
		rank -= cur->getOffset();
		LOG_DEBUG("\tsub "<<cur->getOffset()<<", rank="<<rank<<endl)
		cur=cur->getParent();
	}
	LOG_DEBUG("\treturn rank="<<rank<<endl)
	return rank;
	 */
}

UID_t RoutingSphere::getUID(UID_t rank, RoutingSphere* rs) {
	return (rank + (rs->getOwningNet()->getUID()-rs->getOwningNet()->getSize()));
}

UID_t RoutingSphere::getChildRank(UID_t rank, RoutingSphere* rs){
    rank=rank-rs->getOwningNet()->getOffset();
	BaseEntity* cur=rs->getOwningNet()->getParent();
	while(!cur->isRoutingSphere()) {
		rank -= cur->getOffset();
		cur=cur->getParent();
	}
	return rank;
}

void RoutingSphere::createNixVector(UID_t src_rank, Packet* pkt, Community* comm) {
	pkt->setPrevRoutingSphereUID(this->getUID());
	UID_t dst_uid = pkt->getFinalDestinationUID();
	NixVector* nix_vec = pkt->getNixVector();
	bool i_own_dst = getOwningNet()->containsUID(dst_uid);
	if (!dst_uid) {
		LOG_WARN("Cannot create a nix vector without a destination! The pkt's final destination uid was not set.(dst_uid="<<dst_uid<<")! Default routing will be attempted in lieu of the nix vector."<<endl)
		if (nix_vec) {
			delete nix_vec;
			nix_vec=pkt->setNixVector(NULL);
		}
		return;
	}
	if (!nix_vec) {
		nix_vec = pkt->setNixVector(new NixVector());
	}
	LOG_DEBUG(": RoutingSphere::createNixVector (src_rank="<<src_rank<<", src_uid="<<getUID(src_rank, this)<<", dst_uid="<<dst_uid<<")"<<endl)
	//LOG_DEBUG(": Owning net is "<<getOwningNet()->getUName()<<", cantainsUID(dst_uid)="<<getOwningNet()->containsUID(dst_uid)<<endl)
	RouteEntry* next_hop = NULL;
	UIDCostPair new_dst=SSFNET_MAKE_PAIR(0,0xFFFF);
#if TEST_ROUTING == 1
	uint64_t before_dst_lookup=cur_timestamp();
#endif
	if (i_own_dst) {
		//either this sphere (or a sub-sphere) owns the dst
		RoutingSphere* owning_sphere = getOwningNet()->findOwningSphere(dst_uid);
		LOG_DEBUG("Owning sphere is "<<getOwningNet()->findOwningSphere(dst_uid)->getUName()<<", this sphere="<<this->getUName()<<endl)
		//if the current sphere is the immediate sphere of dst, map the dst_uid to dst_rank in terms of this net
		if (owning_sphere == this) {
			//LOG_DEBUG("Owning sphere of dst uid is the current sphere"<<endl)
			//the local table can serve this request directly
			new_dst.first = getRank(dst_uid, this);
			new_dst.second=0;
			//LOG_DEBUG("dst_rank is "<<new_dst.first<<endl)
		} else {
			//if the current net is not the immediate routing sphere of the dst, use the edge interfaces of
			//its subnet who is a routing sphere and owns dst to be the local dsts
			UID_t temp_rank;
#ifdef USE_CACHE
			//if the size of the edge interfaces is greater than half of the size of the local dst cache,
			//we store the local dst in the cache and check the cache here.
			SrcDstPair src_dst = SSFNET_MAKE_PAIR(src_rank, pkt->getFinalDestinationUID());
			UID_t cached_dst;
			LocalDstCache* dst_cache = getLocalDstCache(comm);
			if (owning_sphere->getEdgeInterfaces().size()
					> (SSFNET_STATIC_CAST(RoutingSphere*,getParent())->getLocalDstCacheSize()/2)) {
#if TEST_ROUTING == 1
				uint64_t before_dst_find=cur_timestamp();
#endif
				//LOG_DEBUG("before dst find is: "<<before_dst_find<<endl)
				bool found=dst_cache->find(src_dst, cached_dst);
#if TEST_ROUTING == 1
				pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::DST_FIND,getParent()->getUID(),cur_timestamp()-before_dst_find));
#endif
				if (found) {
					//copy the cached dst to the new dst.
					//LOG_DEBUG("Cached local dst_cost is found."<<endl)
					new_dst.first = cached_dst;
				} else {
					for (EdgeList::iterator i = owning_sphere->getEdgeInterfaces().begin();
							i != owning_sphere->getEdgeInterfaces().end(); i++) {
						temp_rank = (*i).getParentRank(owning_sphere, this);
						next_hop = getNextHop(src_rank, temp_rank);
						if (next_hop) {
							if (new_dst.second > next_hop->getCost()) {
								new_dst.first = temp_rank;
								new_dst.second = next_hop->getCost();
							}
							//else have seen a short path to sub-sphere using different iface
						}
						//else no path to edge iface
					}
					//Store the local dst in the cache.
					//LOG_DEBUG("The local dst rank is: "<<new_dst.first<<", the cost is: "<<new_dst.second<<endl)
#if TEST_ROUTING == 1
					uint64_t before_dst_insert=cur_timestamp();
					//LOG_DEBUG("before dst insert is: "<<before_dst_insert<<endl)
#endif
					dst_cache->insert(src_dst, new_dst.first);
#if TEST_ROUTING == 1
					pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::DST_INSERT,getParent()->getUID(),cur_timestamp()-before_dst_insert));
#endif
				}
			}
			else{
#endif
				for (EdgeList::iterator i = owning_sphere->getEdgeInterfaces().begin();
					i!= owning_sphere->getEdgeInterfaces().end(); i++) {
					temp_rank = (*i).getParentRank(owning_sphere, this);
					next_hop = getNextHop(src_rank, temp_rank);
					//LOG_DEBUG("src_rank="<<src_rank<<", temp_rank="<<temp_rank<<endl)
					if (next_hop) {
						if (new_dst.second > next_hop->getCost()) {
							new_dst.first = temp_rank;
							new_dst.second = next_hop->getCost();
						}
						//else have seen a short path to sub-sphere using different iface
					}
					//else LOG_DEBUG("No path to edge iface!"<<endl)
				}
			}
#ifdef USE_CACHE
		}
#endif

	} else if (getSuperRoutingSphere() != NULL) {
#ifdef USE_CACHE
		//Check if the local dst is stored in the cache.
		//A pair of the source's rank in terms of this sphere and the final destination's uid,
		//is used to lookup the local dstination's rank within this sphere in the cache.
		SrcDstPair src_dst=SSFNET_MAKE_PAIR(src_rank, pkt->getFinalDestinationUID());
		UID_t cached_dst;
		LocalDstCache* dst_cache=getLocalDstCache(comm);
#if TEST_ROUTING == 1
		uint64_t before_dst_find=cur_timestamp();
		//LOG_DEBUG("before dst find is: "<<before_dst_find<<endl)
#endif
		bool found=dst_cache->find(src_dst, cached_dst);
#if TEST_ROUTING == 1
		pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::DST_FIND,getParent()->getUID(),cur_timestamp()-before_dst_find));
#endif
		if(found){
			//copy the cached dst to the new dst.
			//LOG_DEBUG("Cached local dst_cost is found."<<endl)
			new_dst.first=cached_dst;
		}else{
#endif
			//a parent sphere owns the dst
			UIDCostPairVector srcs;
			for (EdgeList::iterator i = getEdgeInterfaces().begin(); i != getEdgeInterfaces().end(); i++) {
				LOG_DEBUG("using edge iface "<<(*i).getParentRank(this, getSuperRoutingSphere())<<endl);
				srcs.push_back(
						SSFNET_MAKE_PAIR((*i).getParentRank(this, getSuperRoutingSphere()),0));
			}
			getSuperRoutingSphere()->mapToLocalDsts(this,dst_uid, srcs);
			//pick the best new_dst.... and save it to dst_rank
			UID_t temp_rank;
			LOG_DEBUG("The current sphere is: "<<this->getUName()<<endl)
			DEBUG_CODE(
					for (UIDCostPairVector::iterator i = srcs.begin(); i != srcs.end(); i++) {
						LOG_DEBUG(": possible new_dst uid= "<<getUID(getChildRank(i->first, this),this)<<", cumulative cost="<<i->second<<endl)
					}
			);
			for (UIDCostPairVector::iterator i = srcs.begin(); i != srcs.end(); i++) {
				LOG_DEBUG("new_dst is "<<i->first<<", cumulative cost="<<i->second<<endl)
				//i->first is not uid, it is the rank in terms of the parent net.
				temp_rank = getChildRank(i->first, this);

				next_hop = getNextHop(src_rank, temp_rank);
				if (next_hop && i->second != -1) {
					LOG_DEBUG("src_rank="<<src_rank<<", dst_rank="<<temp_rank
							<<", cost="<<next_hop->getCost()<<", next_hop_uid="<<getUID(next_hop->getNextHopRank(),this)<<endl);
					if (new_dst.second >= (next_hop->getCost() + i->second)) {
						new_dst.first = temp_rank;
						new_dst.second = next_hop->getCost() + i->second;
					}
					//else we have a better choice already
				} else if (!next_hop && new_dst.first == 0) {
					//if the src and the mapped local dst are in one node, the next_hop is null.
					LOG_DEBUG("next hop is null, src_rank="<<src_rank<<", dst_rank="<<temp_rank<<endl)
					new_dst.first = temp_rank;
					new_dst.second = i->second;
				}
			}
			DEBUG_CODE(if(new_dst.first==0 && new_dst.second==0xFFFF)
				LOG_WARN("No path from src to dst, either the edge iface isn't usable or there is no path to it"<<endl));

#ifdef USE_CACHE
			//Store the local dst in the cache.
			//LOG_DEBUG("The local dst rank is: "<<new_dst.first<<", the cost is: "<<new_dst.second<<endl)
#if TEST_ROUTING == 1
			uint64_t before_dst_insert=cur_timestamp();
#endif
			dst_cache->insert(src_dst, new_dst.first);
#if TEST_ROUTING == 1
			pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::DST_INSERT,getParent()->getUID(),cur_timestamp()-before_dst_insert));
#endif
		}
#endif
	}
	else {
		//I don't own it but I have no parent sphere....
		LOG_ERROR("Should never see this....."<<endl)
	}
	if(new_dst.first==0){
		LOG_WARN("Cant route the packet because no one claims to own the dst uid "<<dst_uid<<" my uid range=["<<getOwningNet()->getMinUID()<<","<<getOwningNet()->getUID()<<"]"<<endl)
		NixVector* ack = pkt->dropNixVector();
		if(ack) delete ack;
		return;
	} else {
		UID_t uid_offset=getOwningNet()->getUID()-getOwningNet()->getSize();
		//LOG_DEBUG("Local destination rank = "<<new_dst.first<<", Local destination uid = "
			//<<new_dst.first+uid_offset<<", cost="<<new_dst.second<<",uid_offset="<<uid_offset<<endl);
		pkt->setLocalDestinationUID(new_dst.first + uid_offset );
	}

#if TEST_ROUTING == 1
	pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::DST_LOOKUP,getParent()->getUID(),cur_timestamp()-before_dst_lookup));
	uint64_t before_nix_lookup=cur_timestamp();
#endif

#ifdef USE_CACHE
	//check if the nix vector is stored in the cache.
	//A pair of the ranks of the source and local destination within this sphere, is used to lookup the nix vector in the cache.
	SrcDstPair src_dst_pair = SSFNET_MAKE_PAIR(src_rank, getRank(pkt->getLocalDestinationUID(),this));
	NixVector* cached_nix;
	NixVectorCache* nix_cache=getNixCache(comm);
#if TEST_ROUTING == 1
	uint64_t before_nix_find=cur_timestamp();
	//LOG_DEBUG("before nix find is: "<<before_nix_find<<endl)
#endif
	bool found=nix_cache->find(src_dst_pair, cached_nix);
#if TEST_ROUTING == 1
	pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::NIX_FIND,getParent()->getUID(),cur_timestamp()-before_nix_find));
#endif
	if(found){
		*nix_vec=*cached_nix;
		LOG_DEBUG("Nix vector is found in cache."<<endl)
	}else{
#endif
		//If there is no nix vector in the cache, the nix vector needs to be built and stored.
		//LOG_DEBUG("Can't find nix vector in cache, create nix vector now."<<endl)
		//Create nix vector between src_rank and dst_rank using getNextHop
		RouteEntry* re = getNextHop(src_rank, new_dst.first);
		LOG_DEBUG("current net="<<getOwningNet()<<", src_rank="<<src_rank<<", new dst="<<new_dst.first<<endl);
		if(re==NULL) {LOG_DEBUG("first re is NULL!"<<endl);}
		else {LOG_DEBUG("first re:"<<*re<<endl);}
		while (re != NULL) { //we don't have entries to go back to oneself except for the edge interfaces
			LOG_DEBUG("re:"<<*re<<endl);
			if(re->getOwningHost()==0){
				nix_vec->addOutboundIfaceIdx(re->getOutboundIfaceIdx(),re->getNumberOfBits());
				//LOG_DEBUG("outbound interface="<<re->getOutboundIfaceIdx()<<", is edge node="<<(int)(uint32_t)re->isEdgeNode()<<", re->getNumberOfBits()="<<(int)(re->getNumberOfBits())<<endl);
			}else{
				const uint32_t num_of_bits=NixVector::getNumOfBits(re->getNumberOfBits());
				nix_vec->addOutboundIfaceIdx(re->getOutboundIfaceIdx(),num_of_bits);
				//LOG_DEBUG("outbound interface="<<re->getOutboundIfaceIdx()<<", is edge node="<<(int)(uint32_t)re->isEdgeNode()<<", num_of_bits="<<num_of_bits<<", re->getNumberOfBits()="<<(int)(re->getNumberOfBits())<<endl);
			}
			if (re->getNumberOfBitsBusIdx() != 0) {
				//LOG_DEBUG("\tadd bus "<<(re->getBusIdx())<<", size="<<(int)re->getNumberOfBitsBusIdx()<<endl)
				//LOG_DEBUG("\toutboundiface="<<(int)(re->getOutboundIfaceIdx())<<", numofbits="<<(int)re->getNumberOfBits()
				//		<<"nexthop="<<(int)(re->getNextHopRank())<<", cost="<<(int)re->getCost()<<endl)
				nix_vec->addOutboundIfaceIdx(re->getBusIdx(), re->getNumberOfBitsBusIdx());
			}
			//update the src_rank to be the rank of the next interface on the next hop
			if (src_rank == re->getNextHopRank()) {
				//we are at the next hop
				break;
			} else {
				src_rank = re->getNextHopRank();
			}
			if(re->isEdgeNode()) {
				//crossed into another sphere from this sphere
				LOG_DEBUG("crossed into another sphere from this sphere"<<endl)
				break;
			}
			//LOG_DEBUG("loop continues, src_rank="<<src_rank<<", new_dst="<<new_dst.first<<endl)
			re = getNextHop(src_rank, new_dst.first);
		}
#ifdef USE_CACHE
		//LOG_DEBUG("Insert nix vector to the cache."<<endl)
		//Insert the nix vector just built in the cache.
#if TEST_ROUTING == 1
		uint64_t before_nix_insert=cur_timestamp();
		//LOG_DEBUG("before nix insert is: "<<before_nix_insert<<endl)
#endif
		nix_cache->insert(src_dst_pair, nix_vec->copy());
#if TEST_ROUTING == 1
		pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::NIX_INSERT,getParent()->getUID(),cur_timestamp()-before_nix_insert));
#endif
	}
#endif
#if TEST_ROUTING == 1
	pkt->route_debug->lookups.push_back(RouteLookup(RouteLookup::NIX_LOOKUP,getParent()->getUID(),cur_timestamp()-before_nix_lookup));
#endif
}

bool RoutingSphere::isLocal() {
	return false;
}

RouteEntry* RoutingSphere::getNextHop(UID_t src_rank, UID_t dst_rank) {
	UIDRect tosearch(src_rank, dst_rank, src_rank, dst_rank);
	SingleRouteSearchResult rv;
	getRouteTable()->getRouteMap().Search(tosearch, rv);
	//DEBUG_CODE(if(rv.routeEntry())LOG_DEBUG("\t\trv.routeEntry()="<<*rv.routeEntry()<<endl))
	return rv.routeEntry();
}

int RoutingSphere::calcDist(UID_t src_rank, UID_t dst_rank) {
	UIDRect tosearch(src_rank, dst_rank, src_rank, dst_rank);
	SingleRouteSearchResult hop;
	getRouteTable()->getRouteMap().Search(tosearch, hop);
	//i think this i host rid -- but not sure if all route entries get it set....
	if(hop.routeEntry()) {
		return hop.routeEntry()->getCost();
	}
	return 2; //if there is no route entry it must be 1 hope from the router...see Ting for details :)
}


Net* RoutingSphere::getOwningNet() {
	return SSFNET_DYNAMIC_CAST(Net*,getParent());
}

RoutingSphere* RoutingSphere::getSuperRoutingSphere() {
	if (superSphere == NULL) {
		Net* p = SSFNET_DYNAMIC_CAST(Net*,getParent())->getSuperNet();
		//LOG_DEBUG("Parent net="<<p->getUName()<<endl)
		while (p != NULL) {
			//LOG_DEBUG("p->isRoutingSphere()="<<p->isRoutingSphere()<<endl)
			if (p->isRoutingSphere()) {
				superSphere = p->getRoutingSphere();
				break;
			}
			p = p->getSuperNet();
		}
	}
	return superSphere;
}

long RouteTable::getMemUsage_object_properties() {
	long t = BaseEntity::getMemUsage_object_properties();
	return shared.routemap.XXX_total_memory+shared.routemap.Count()*sizeof(RouteEntry)+t;
}

long RouteTable::getMemUsage_class() {
	return sizeof(*this)+sizeof(shared.routemap)+getPropertyMap()->getMemUsage_class()+getStateMap()->getMemUsage_class();
}


RouteTable* RoutingSphere::getRouteTable() {
	if(!rtIsCached) {
		if (default_route_tableSize() == 0)
			rtCache = NULL;
		else
			rtCache = default_route_table().nextElement();
		rtIsCached = true;
	}
	return rtCache;
}

EdgeInterfaceListWrapper RoutingSphere::getEdgeInterfaces() {
	if(!getRouteTable()) {
		LOG_ERROR("called getEdgeInterfaces() on a routing sphere without a routing table!"<<endl)
	}
	return getRouteTable()->getEdgeInterfaces();
}

bool RoutingSphere::hasRoutes() {
	return getRouteTable()?(getRouteTable()->size()>0):0;
}

void RoutingSphere::handleRoutingEvent(UID_t iface_id, bool is_available) {
	if(!getRouteTable()) {
		LOG_ERROR("called handleRoutingEvent() on a routing sphere without a routing table!"<<endl)
	}
	getRouteTable()->handleRoutingEvent(iface_id, is_available);
}

GhostRoutingSphereRegistrationResponse* RoutingSphere::handleGhostRegistration(int src_com) {
	if(!getRouteTable()) {
		LOG_ERROR("called handleRoutingEvent() on a routing sphere without a routing table!"<<endl)
	}
	return getRouteTable()->handleGhostRegistration(src_com);
}


#ifdef USE_CACHE

LocalDstCache* RoutingSphere::getLocalDstCache(Community * comm) {
	if(!getRouteTable()) {
		LOG_ERROR("called getLocalDstCache() on a routing sphere without a routing table!"<<endl)
	}
	return getRouteTable()->getLocalDstCache(comm);
}

NixVectorCache* RoutingSphere::getNixCache(Community * comm) {
	if(!getRouteTable()) {
		LOG_ERROR("called getNixCache() on a routing sphere without a routing table!"<<endl)
	}
	return getRouteTable()->getNixCache(comm);
}
NixVectorCache* GhostRoutingSphere::getNixCache(Community * comm) {
	LOG_ERROR("called getNixCache() on a ghost routing sphere !"<<endl);
	return 0;
}
LocalDstCache* GhostRoutingSphere::getLocalDstCache(Community * comm) {
	LOG_ERROR("called getLocalDstCache() on a ghost routing sphere !"<<endl);
	return 0;
}
#endif


void GhostRoutingSphere::init() {
	Partition* p = Partition::getInstance();
	Community* comm=p->getCommunityWithLowestId();
	//get the edge interface list from the real sphere
	p->addBootstrapEvent(new GhostRoutingSphereRegistration(
			getUID(),
			comm->getCommunityId(),
			unshared.community_id.read()));
}

EdgeInterfaceListWrapper GhostRoutingSphere::getEdgeInterfaces() {
	return edge_ifaces.reference();
}

bool GhostRoutingSphere::hasRoutes() {
	return 1;
}

void GhostRoutingSphere::handleRoutingEvent(UID_t iface_id, bool is_available) {
	UID_t rank=getRank(iface_id, this);
	if(is_available) {
		//add it
		edge_ifaces.getState()->edges->push_back(EdgeInterface(rank));//iface id or iface
	}
	else {
		//remove it
		EdgeInterfaceList newlist;
		//copy unshared.my_edge_ifaces.getState()->edges to newlist.getState()->edges except for iface_id
		for(EdgeList::iterator i = edge_ifaces.getState()->edges->begin();
				i!=edge_ifaces.getState()->edges->end(); i++) {
			if((*i).getLocalRank()!=rank){
				newlist.getState()->edges->push_back(*i);
			}
		}
		edge_ifaces = newlist;
	}
}

GhostRoutingSphereRegistrationResponse* GhostRoutingSphere::handleGhostRegistration(int src_com) {
	LOG_ERROR("The ghost sphere "<<getUID()<<" in partition "<<
			Partition::getInstance()->getPartitionId()<<" received a ghost registration from com "<< src_com<<endl)
	return 0;
}

void GhostRoutingSphere::handleGhostRegistrationResponse(GhostRoutingSphereRegistrationResponse* response) {
	for(EdgeList::iterator i = response->getEdgeIfaces().begin();i!=response->getEdgeIfaces().end();i++) {
		edge_ifaces.getState()->edges->push_back(*i);
	}
}

RouteTable::RouteTable() {
	rtsize=-1;
}

RouteTable::~RouteTable() {

}

void RouteTable::handleRoutingEvent(UID_t iface_id, bool is_available) {
	//make iface_id relative
	UID_t rank=getRoutingSphere()->getRank(iface_id, getRoutingSphere());
	if(is_available) {
		//add it
		unshared.my_edge_ifaces.getState()->edges->push_back(EdgeInterface(rank));//iface id or iface
	}
	else {
		//remove it
		EdgeInterfaceList newlist;
		//copy unshared.my_edge_ifaces.getState()->edges to newlist.getState()->edges except for iface_id
		for(EdgeList::iterator i = unshared.my_edge_ifaces.getState()->edges->begin();
				i!=unshared.my_edge_ifaces.getState()->edges->end(); i++) {
			if((*i).getLocalRank()!=rank){
				newlist.getState()->edges->push_back(*i);
			}
		}
		unshared.my_edge_ifaces = newlist;
	}
	//send the update to all ghosts of this table...
	Community* src_com = Partition::getInstance()->getCommunityWithLowestId();
	for(UIDSet::iterator i=ghost_coms.begin(); i!=ghost_coms.end();i++) {
		src_com->deliverEvent(
				new RoutingStatusResponse(
						getParent()->getUID(),
						src_com->getCommunityId(),
						*i,
						iface_id,
						is_available
						)
		);
	}
}

GhostRoutingSphereRegistrationResponse* RouteTable::handleGhostRegistration(int src_com) {
	ghost_coms.insert(src_com);
	Partition* p=Partition::getInstance();
	return new GhostRoutingSphereRegistrationResponse(
				getParent()->getUID(),
				p->getCommunityWithLowestId()->getCommunityId(),
				src_com,
				*(unshared.my_edge_ifaces.getState()->edges)
				);
}

RoutingSphere* RouteTable::getRoutingSphere() {
	return SSFNET_DYNAMIC_CAST(RoutingSphere*,getParent());
}

void RouteTable::init() {
	Partition* p = Partition::getInstance();
	//copy edge_ifaces to unshared my_edge_ifaces
	for(EdgeList::iterator i = shared.edge_ifaces.read().getState()->edges->begin();
		i!=shared.edge_ifaces.read().getState()->edges->end();i++) {
		//convert rank to uid
		UID_t iface_uid=getRoutingSphere()->getUID((*i).getLocalRank(),getRoutingSphere());
		//pick a community with community id 0
		int cid = p->uid2communityId(iface_uid);
		if(cid >=0 ) {
			Community::Map::iterator match = p->getCommunities().find(cid);
			if(match != p->getCommunities().end()) {
				Interface* iface=SSFNET_DYNAMIC_CAST(Interface*,match->second->getObject(iface_uid));
				//LOG_DEBUG("iface="<<iface->getUName()<<", iface uid="<<iface->getUID()<<", iface getLink="<<iface->getLink()<<", is on="<<iface->isOn()<<endl)
				if(iface!=NULL){
					//check if it is connected and active
					if(iface->getLink()&&iface->isOn()){
						unshared.my_edge_ifaces.getState()->edges->push_back(*i);
					}
					iface->registerConcernedSphere(p->getCommunityWithLowestId()->getCommunityId(),getParent()->getUID());
				}
				else {
					LOG_ERROR("Someone we think the iface with uid "<<
							iface_uid<<" is in comm "<<cid<<" but the community "<<cid<<
							" doesnt know about the iface"<<endl)
				}
			}
			else {
				Community* comm=p->getCommunityWithLowestId();
				//create request event
				int target_com_id = p->uid2communityId(iface_uid);
				//add a request event to the preinit event list in the community
				p->addBootstrapEvent(new RoutingStatusRequest(
							this->getParent()->getUID(),
							comm->getCommunityId(),
							target_com_id,
							iface_uid));
			}
		}
		else{
			LOG_ERROR("We think the iface with uid "<<
					iface_uid<<" is and edge iface, "<<cid<<" but the partition doesnt know about it"<<cid<<endl)
		}
	}
#ifdef USE_CACHE
	for	(Community::Map::iterator i=p->getCommunities().begin();
			i!=p->getCommunities().end();i++){
		NixVectorCache* nv = new NixVectorCache(SSFNET_STATIC_CAST(RoutingSphere*,getParent())->getNixVectorCacheSize());
		LocalDstCache* ld=new LocalDstCache(SSFNET_STATIC_CAST(RoutingSphere*,getParent())->getLocalDstCacheSize());
		CachePair cp=SSFNET_MAKE_PAIR(nv,ld);
		//insert into map
		comm_cache.insert(SSFNET_MAKE_PAIR(i->second,cp));
	}
#endif
}

void RouteTable::wrapup() {
}

void RouteTable::addRoute(long src_min, long src_max, long dst_min,
		long dst_max, long outbound_iface, long owning_host, long num_of_bits,
		long next_hop, long edge_iface, long bus_idx,
		long num_of_bits_bus, long cost) {
	UIDRect t(src_min, dst_min, src_max, dst_max);
	if (num_of_bits_bus == 0) {
		/*LOG_DEBUG("Adding RouteEntry to "<<
				getName()<<
				"["<<src_min<<", "<<dst_min<<"]"<<
				"x["<<src_max<<", "<<dst_max<<"]"<<
				"->("<<outbound_iface<<", "<<owning_host<<
				", "<<num_of_bits<<", "<<next_hop<<", "<<
				edge_iface<<", "<<cost<<")"<<endl);*/
		shared.routemap.Insert(t, new RouteEntry(outbound_iface, owning_host, num_of_bits, next_hop, edge_iface, cost));
	} else {
		/*LOG_DEBUG("Adding RouteEntryWithBus to "<<
				getName()<<"["<<src_min<<", "<<dst_min<<"]"<<
				"x["<<src_max<<", "<<dst_max<<"]"<<
				"->("<<outbound_iface<<", "<<owning_host<<
				", "<<num_of_bits<<", "<<next_hop<<","<<
				edge_iface<<","<<bus_idx<<", "<<num_of_bits_bus<<", "<<cost<<")"<<endl);*/
		shared.routemap.Insert(t, new RouteEntryWithBus(outbound_iface, owning_host,
				num_of_bits, next_hop, edge_iface, bus_idx, num_of_bits_bus, cost));
	}
}

int RouteTable::size() {
	if(rtsize == -1) rtsize = shared.routemap.Count();
	return rtsize;
}

EdgeInterfaceListWrapper RouteTable::getEdgeInterfaces() {
	return unshared.my_edge_ifaces.reference();
}

void RouteTable::getNextHop(RouteSearchResult& result, UIDRect& query) {
	shared.routemap.Search(query, result);
}

RouteEntry::RouteEntry(uint16_t _outbound_iface, UID_t _owning_host, uint16_t _num_of_bits, UID_t _next_hop, bool _edge_node, uint16_t _cost) :
	outbound_iface(_outbound_iface), owning_host(_owning_host), num_of_bits(_num_of_bits), next_hop(_next_hop), edge_node(_edge_node), cost(_cost) {
}

RouteEntry::~RouteEntry() {
}

const uint16_t RouteEntry::getOutboundIfaceIdx() {
	return outbound_iface;
}
const UID_t RouteEntry::getOwningHost() {
	return owning_host;
}
const uint16_t RouteEntry::getNumberOfBits() {
	return num_of_bits;
}
const UID_t RouteEntry::getNextHopRank() {
	return next_hop;
}
const uint16_t RouteEntry::getCost() {
	return cost;
}
uint16_t RouteEntry::getBusIdx() {
	return 0;
}

const bool RouteEntry::isEdgeNode() {
	return edge_node;
}

uint16_t RouteEntry::getNumberOfBitsBusIdx() {
	return 0;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, RouteEntry& u) {
	return u.print(os);
}
PRIME_OSTREAM& RouteEntry::print(PRIME_OSTREAM &os) {
	os << "[out_iface_idx=" << getOutboundIfaceIdx();
	os << ", owning_host=" << getOwningHost();
	os << ", #bits=" << (int) getNumberOfBits();
	os << ", next_hop=" << getNextHopRank();
	os << ", is_edge_node=" << isEdgeNode();
	os << ", cost=" << getCost();
	os << ", bus_idx=" << getBusIdx();
	return os << ", #bits_bus=" << (int) getNumberOfBitsBusIdx() << "]";
}

RouteEntryWithBus::RouteEntryWithBus(uint16_t _outbound_iface, UID_t _owning_host,
		uint16_t _num_of_bits, UID_t _next_hop, uint32_t _edge_iface,
		uint16_t _bus_idx, uint16_t _num_of_bits_bus, uint16_t _cost) :
	RouteEntry(_outbound_iface, _owning_host, _num_of_bits, _next_hop, _edge_iface,
			_cost), bus_idx(_bus_idx), num_of_bits_bus(_num_of_bits_bus) {
}

RouteEntryWithBus::~RouteEntryWithBus() {
}

uint16_t RouteEntryWithBus::getBusIdx() {
	return bus_idx;
}

uint16_t RouteEntryWithBus::getNumberOfBitsBusIdx() {
	return num_of_bits_bus;
}

EdgeInterface::EdgeInterface(UID_t _local_rank) :
	local_rank(_local_rank) {
}

EdgeInterface::EdgeInterface(PRIME_ISTREAM &is) {
	is >> local_rank;
}

EdgeInterface::EdgeInterface(const EdgeInterface& o) :
	local_rank(o.local_rank) {
}

EdgeInterface::~EdgeInterface() {

}

UID_t EdgeInterface::getLocalRank() {
	return local_rank;
}

UID_t EdgeInterface::getParentRank(RoutingSphere* localSphere,
		RoutingSphere* parentSphere) {
	if (parentSphere == NULL)
		return local_rank;
	UID_t prank = local_rank;
	BaseEntity* pnet = SSFNET_DYNAMIC_CAST(BaseEntity*,parentSphere->getOwningNet());
	BaseEntity* cur = SSFNET_DYNAMIC_CAST(BaseEntity*,localSphere->getOwningNet());
	while (cur != NULL && cur != pnet) {
		prank += cur->getOffset();
		cur = cur->getParent();
	}
	return prank;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, EdgeInterface& u) {
	return os << u.getLocalRank();
}

template<> bool rw_config_var<SSFNET_STRING, EdgeInterfaceList> (
		SSFNET_STRING& dst, EdgeInterfaceList& src) {
	PRIME_STRING_STREAM s;
	s << "[";
	for (EdgeList::iterator i = src.getState()->edges->begin(); i != src.getState()->edges->end(); i++) {
		s << *i;
	}
	s << "]";
	dst.append(s.str().c_str());
	return false;
}

template<> bool rw_config_var<EdgeInterfaceList, SSFNET_STRING> (
		EdgeInterfaceList& dst, SSFNET_STRING& src) {
	if (src.length() <= 1) {
		LOG_ERROR("Invalid EdgeInterface::List "<<src<<endl)
	}
	PRIME_STRING_STREAM s;
	s << src;
	if (s.get() != '[') {
		LOG_ERROR("Invalid EdgeInterface::List '"<<src<<"'"<<endl)
	}
	while (s.good()) {
		switch (s.peek()) {
		case ']':
			return false;
		case ',':
			s.get();
		default:
			dst.getState()->edges->push_back(EdgeInterface(s));
		}
	}
	LOG_ERROR("Invalid EdgeInterface::List "<<src<<endl)
	return true;
}

SubEdgeInterface::SubEdgeInterface(UID_t _local_rank, UID_t _host_rank) :
	local_rank(_local_rank), host_rank(host_rank) {
}

SubEdgeInterface::SubEdgeInterface(PRIME_ISTREAM &is) {
	if ((!is.good()) || (is.get() != '[')) {
		LOG_ERROR("1, Invalid SubEdgeInterface '"<<is<<"'"<<endl)
	}
	is >> local_rank;
	if ((!is.good()) || (is.get() != ',')) {
		LOG_ERROR("2, Invalid SubEdgeInterface '"<<is<<"'"<<endl)
	}
	is.get();
	is>>host_rank;
	if ((!is.good()) || (is.get() != ']')) {
		LOG_ERROR("3, Invalid SubEdgeInterface '"<<is<<"'"<<endl)
	}
}

SubEdgeInterface::SubEdgeInterface(const SubEdgeInterface& o) :
	local_rank(o.local_rank), host_rank(o.host_rank) {
}

SubEdgeInterface::~SubEdgeInterface() {

}

UID_t SubEdgeInterface::getLocalRank() {
	return local_rank;
}

UID_t SubEdgeInterface::getHostLocalRank() {
	return host_rank;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, SubEdgeInterface& u) {
	return os << "["<<u.getLocalRank()<<","<<u.getHostLocalRank()<<"]";
}

template<> bool rw_config_var<SSFNET_STRING, SubEdgeList> (
		SSFNET_STRING& dst, SubEdgeList& src) {
	PRIME_STRING_STREAM s;
	s << "[";
	for (SubEdgeList::iterator i = src.begin(); i != src.end(); i++) {
		s << *i;
	}
	s << "]";
	dst.append(s.str().c_str());
	return false;
}

template<> bool rw_config_var<SubEdgeList, SSFNET_STRING> (
		SubEdgeList& dst, SSFNET_STRING& src) {
	if (src.length() <= 1) {
		LOG_ERROR("Invalid SubEdgeList::List "<<src<<endl)
	}
	PRIME_STRING_STREAM s;
	s << src;
	if (s.get() != '[') {
		LOG_ERROR("Invalid SubEdgeListEdgeInterface::List '"<<src<<"'"<<endl)
	}
	while (s.good()) {
		switch (s.peek()) {
		case ']':
			return false;
		case ',':
			s.get();
			s.get();
		default:
			dst.push_back(SubEdgeInterface(s));
		}
	}
	LOG_ERROR("Invalid EdgeInterface::List "<<src<<endl)
	return true;
}


PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, UIDRect& u) {
	return os << "[[" << u.m_min[0] << "," << u.m_min[1] << "],[" << u.m_max[0]
			<< "," << u.m_max[1] << "]]";
}

UIDRect::UIDRect(UID_t src_min, UID_t dst_min, UID_t src_max, UID_t dst_max) {
	m_min[0] = src_min;
	m_min[1] = dst_min;
	m_max[0] = src_max;
	m_max[1] = dst_max;
}

UIDRect::~UIDRect() {

}

RouteSearchResult::RouteSearchResult() {

}
RouteSearchResult::~RouteSearchResult() {

}
bool RouteSearchResult::addResult(RouteEntry*& result) {
	return false;
}
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, RouteSearchResult& u) {
	return u.print(os);
}
PRIME_OSTREAM& RouteSearchResult::print(PRIME_OSTREAM &os) {
	return os << "[RouteSearchResult]";
}

PRIME_OSTREAM& SingleRouteSearchResult::print(PRIME_OSTREAM &os) {
	return os << "[SingleRouteSearchResult] routeEntry()=" << routeEntry();
}
PRIME_OSTREAM& MultipleRouteSearchResult::print(PRIME_OSTREAM &os) {
	return os << "[MultipleRouteSearchResult] getRoutes().size()="
			<< getRoutes().size();
}
PRIME_OSTREAM& ExistenceSearchResult::print(PRIME_OSTREAM &os) {
	return os << "[ExistenceSearchResult] hasAtleastOneResult()="
			<< hasAtleastOneResult();
}

SingleRouteSearchResult::SingleRouteSearchResult() :
	rv(0) {

}
SingleRouteSearchResult::~SingleRouteSearchResult() {

}
bool SingleRouteSearchResult::addResult(RouteEntry*& result) {
	if (rv == NULL) {
		rv = result;
	}
	else {
		if(result->getOwningHost()==0 && rv->getOwningHost()==0) {
			if(rv->getCost() > result->getCost()) rv = result;
		}
		else if(result->getOwningHost()>0 && rv->getOwningHost()>0) {
			if(rv->getCost() > result->getCost()) rv = result;
		}
		else if(result->getOwningHost()>0 && rv->getOwningHost()==0 ) {
			rv = result;
		}
		else if(result->getOwningHost()==0 && rv->getOwningHost()>0 ) {
			//skip it, keep searching
		}
	}
	return true;
}

RouteEntry* SingleRouteSearchResult::routeEntry() {
	return rv;
}

MultipleRouteSearchResult::MultipleRouteSearchResult() {
}

MultipleRouteSearchResult::~MultipleRouteSearchResult() {
}

bool MultipleRouteSearchResult::addResult(RouteEntry*& result) {
	results.push_back(result);
	return true;
}

RouteEntry::List& MultipleRouteSearchResult::getRoutes() {
	return results;
}

ExistenceSearchResult::ExistenceSearchResult() :
	rv(false) {
}
ExistenceSearchResult::~ExistenceSearchResult() {
}
bool ExistenceSearchResult::addResult(RouteEntry*& result) {
	rv = true;
	return false;
}
bool ExistenceSearchResult::hasAtleastOneResult() {
	return rv;
}

#ifdef USE_CACHE
NixVectorCache::NixVectorCache(uint32 size) {
	cache_size = size;
	head = 0;
	tail = 0;
}

bool NixVectorCache::find(SrcDstPair& tofind, NixVector*& rv) {
	//LOG_DEBUG("We are looking for nix vector for src "<<tofind.first<<" to dst "<<tofind.second<<endl)
	NixVectorCacheEntry::Map::iterator entry = map.find(&tofind);
	if (entry == map.end()) {
		//we don't have the entry
		rv = NULL;
		return false;
	}
	rv = entry->second->value;
	//we need to update that we access this
	entry->second->remove(head, tail);
	entry->second->append(head, tail);
	return true; //we found it
}
void NixVectorCache::insert(SrcDstPair& key, NixVector* value) {
	//LOG_DEBUG("We insert nix vector "<<value<<endl)
	NixVectorCacheEntry* cur = 0;
	if (map.size() == cache_size) {
		//The cache is full, delete the item at queue tail from the queue first
		cur = SSFNET_STATIC_CAST(NixVectorCacheEntry*,head);
		head->remove(head, tail);
		map.erase(&(cur->key));
		cur->key = key;
		cur->value = value;
	} else {
		cur = new NixVectorCacheEntry(key, value);
	}
	//append the new item to the end of the queue
	cur->append(head, tail);
	//add it to the map
	map.insert(SSFNET_MAKE_PAIR(&(cur->key),cur));
}

LocalDstCache::LocalDstCache(uint32 size) {
	cache_size = size;
	head = 0;
	tail = 0;
}

bool LocalDstCache::find(SrcDstPair& tofind, UID_t& rv) {
	LocalDstCacheEntry::Map::iterator entry = map.find(&tofind);
	if (entry == map.end()) {
		//we don't have the entry
		return false;
	}
	rv = entry->second->value;
	//we need to update that we access this
	entry->second->remove(head, tail);
	entry->second->append(head, tail);
	return true; //we found it
}

void LocalDstCache::insert(SrcDstPair& key, UID_t& value) {
	LocalDstCacheEntry* cur = 0;
	if (map.size() == cache_size) {
		//The cache is full, delete the item at queue tail from the queue first
		cur = SSFNET_STATIC_CAST(LocalDstCacheEntry*,head);
		head->remove(head, tail);
		map.erase(&(cur->key));
		cur->key = key;
		cur->value = value;
	} else {
		cur = new LocalDstCacheEntry(key, value);
	}
	//append the new item to the end of the queue
	cur->append(head, tail);
	//add it to the map
	map.insert(SSFNET_MAKE_PAIR(&(cur->key),cur));
}

void CacheEntry::remove(CacheEntry*& head, CacheEntry*& tail) {
	if (!head) {
		LOG_WARN("tried to remove cache entry when head/tail were null!")
		return;
	} else if (this == head) {
		if (this == tail) {
			//there is only one entry
			head = 0;
			tail = 0;
		} else {
			//this is the head
			head = this->next;
			head->prev = 0;
		}
	} else if (this == tail) {
		tail = this->prev;
		tail->next = 0;
	} else {
		//we are in the middle of the list
		this->prev->next = this->next;
		this->next->prev = this->prev;
	}
	this->next = 0;
	this->prev = 0;
}

void CacheEntry::append(CacheEntry*& head, CacheEntry*& tail) {
	if (!head) {
		//LOG_DEBUG("There are no entries in this cache."<<endl)
		//there are no entries
		head = this;
		tail = this;
	} else {
		//LOG_DEBUG("There are existing entries in this cache."<<endl)
		//just append this node
		tail->next = this;
		this->prev = tail;
		tail = this;
	}
}

LocalDstCache* RouteTable::getLocalDstCache(Community* comm) {
	LocalDstCache* rv=0;
	//check if there is a cache for this community
	Comm2CacheMap::iterator it=comm_cache.find(comm);
	//if so store it in rv;
	if (it != comm_cache.end()) {
		//we have the entry
		//LOG_DEBUG("We have the dst cache entry"<<endl)
		rv = it->second.second;
	}
	if(!rv) {
		//the cache entry for this community is not found
		LOG_ERROR("We don't have the entry, should never see this!"<<endl)
	}
	return rv;
}

NixVectorCache* RouteTable::getNixCache(Community* comm) {
	NixVectorCache* rv=0;
	//check if there is a cache for this community
	Comm2CacheMap::iterator it=comm_cache.find(comm);
	//if so store it in rv;
	if (it != comm_cache.end()) {
		//we have the entry
		//LOG_DEBUG("We have the nix vector cache entry"<<endl)
		rv = it->second.first;
	}
	if(!rv) {
		//the cache entry for this community is not found
		LOG_ERROR("We don't have the entry, should never see this!"<<endl)
	}
	return rv;
}
#endif

// *******************
RoutingStatus::RoutingStatus(SSFNetEvent::Type t):
	SSFNetEvent(t), routing_sphere_id(0), src_com_id(0) {
}
RoutingStatus::RoutingStatus(SSFNetEvent::Type t, UID_t _src_routing_sphere_id, int _src_com_id, int dst_com_id):
		SSFNetEvent(t,dst_com_id),
		routing_sphere_id(_src_routing_sphere_id),
		src_com_id(_src_com_id) { }
RoutingStatus::RoutingStatus(const RoutingStatus& evt):
		SSFNetEvent(evt.getSSFNetEventType(),evt.getTargetCommunity()),
		routing_sphere_id(evt.getRoutingSphereId()),
		src_com_id(evt.getSrcCommunityId()){
}
RoutingStatus::~RoutingStatus() {
}
prime::ssf::ssf_compact* RoutingStatus::pack() {
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_long_long(routing_sphere_id);
	dp->add_int(src_com_id);
	return dp;
}
void RoutingStatus::unpack(prime::ssf::ssf_compact* dp) {
	SSFNetEvent::unpack(dp);
	dp->get_unsigned_long_long(&routing_sphere_id);
	dp->get_int(&src_com_id);
}
// *******************
RoutingStatusRequest::RoutingStatusRequest():
	RoutingStatus(SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT), iface_id(0) {
}
RoutingStatusRequest::RoutingStatusRequest(UID_t src_sphere_id, int src_com_id, int dst_com_id, UID_t _iface_id):
	RoutingStatus(SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT, src_sphere_id,src_com_id,dst_com_id),
	iface_id(_iface_id) {
}
RoutingStatusRequest::RoutingStatusRequest(const RoutingStatusRequest& evt):
	RoutingStatus(SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT,
			evt.getRoutingSphereId(),
			evt.getSrcCommunityId(),
			evt.getTargetCommunity()),
	iface_id(evt.getIfaceId()) {
}
RoutingStatusRequest::~RoutingStatusRequest() {
}
prime::ssf::ssf_compact* RoutingStatusRequest::pack() {
	prime::ssf::ssf_compact* dp = RoutingStatus::pack();
	dp->add_unsigned_long_long(iface_id);
	return dp;
}
void RoutingStatusRequest::unpack(prime::ssf::ssf_compact* dp) {
	RoutingStatus::unpack(dp);
	dp->get_unsigned_long_long(&iface_id);
}
// *******************

RoutingStatusResponse::RoutingStatusResponse():
	RoutingStatus(SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT),iface_id(0),is_available(0) {
}
RoutingStatusResponse::RoutingStatusResponse(UID_t src_sphere_id, int src_com_id, int dst_com_id, UID_t _iface_id, bool _is_available):
	RoutingStatus(SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT, src_sphere_id,src_com_id,dst_com_id),
	iface_id(_iface_id),is_available(_is_available) {
}
RoutingStatusResponse::RoutingStatusResponse(const RoutingStatusResponse& evt):
	RoutingStatus(SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT,
			evt.getRoutingSphereId(),
			evt.getSrcCommunityId(),
			evt.getTargetCommunity()),
	iface_id(evt.getIfaceId()),
	is_available(evt.isAvailable()){
}

RoutingStatusResponse::RoutingStatusResponse(const RoutingStatusRequest& evt, bool _is_available):
			RoutingStatus(SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT,
					evt.getRoutingSphereId(),
					evt.getTargetCommunity(),
					evt.getSrcCommunityId()),
			iface_id(evt.getIfaceId()),
			is_available(_is_available){
}
RoutingStatusResponse::~RoutingStatusResponse() {
}
prime::ssf::ssf_compact* RoutingStatusResponse::pack() {
	prime::ssf::ssf_compact* dp = RoutingStatus::pack();
	dp->add_unsigned_long_long(iface_id);
	dp->add_unsigned_short(is_available?1:0);
	return dp;
}
void RoutingStatusResponse::unpack(prime::ssf::ssf_compact* dp) {
	RoutingStatus::unpack(dp);
	dp->get_unsigned_long_long(&iface_id);
	unsigned short s;
	dp->get_unsigned_short(&s);
	is_available=s?1:0;
}
// *******************

GhostRoutingSphereRegistration::GhostRoutingSphereRegistration():
	RoutingStatus(SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_EVT) {
}
GhostRoutingSphereRegistration::GhostRoutingSphereRegistration(UID_t src_sphere_id, int src_com_id, int dst_com_id):
	RoutingStatus(SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_EVT, src_sphere_id,src_com_id,dst_com_id) {
}
GhostRoutingSphereRegistration::GhostRoutingSphereRegistration(const GhostRoutingSphereRegistration& evt):
	RoutingStatus(SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_EVT,
			evt.getRoutingSphereId(),
			evt.getSrcCommunityId(),
			evt.getTargetCommunity()) {
}
GhostRoutingSphereRegistration::~GhostRoutingSphereRegistration() {
}
prime::ssf::ssf_compact* GhostRoutingSphereRegistration::pack() {
	prime::ssf::ssf_compact* dp = RoutingStatus::pack();
	return dp;
}
void GhostRoutingSphereRegistration::unpack(prime::ssf::ssf_compact* dp) {
	RoutingStatus::unpack(dp);
}
// *******************

GhostRoutingSphereRegistrationResponse::GhostRoutingSphereRegistrationResponse():
	RoutingStatus(SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT) {
}
GhostRoutingSphereRegistrationResponse::GhostRoutingSphereRegistrationResponse(UID_t src_sphere_id, int src_com_id, int dst_com_id, EdgeList& edges):
	RoutingStatus(SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT, src_sphere_id,src_com_id,dst_com_id) {
	for (EdgeList::iterator  i = edges.begin(); i !=  edges.end(); i++) {
		edge_ifaces.push_back(*i);
	}
}
GhostRoutingSphereRegistrationResponse::GhostRoutingSphereRegistrationResponse(const GhostRoutingSphereRegistrationResponse& evt):
	RoutingStatus(SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT,
			evt.getRoutingSphereId(),
			evt.getSrcCommunityId(),
			evt.getTargetCommunity()) {
	for (EdgeList::iterator  i = const_cast<GhostRoutingSphereRegistrationResponse&>(evt).getEdgeIfaces().begin();
			i !=  const_cast<GhostRoutingSphereRegistrationResponse&>(evt).getEdgeIfaces().end(); i++) {
		edge_ifaces.push_back(*i);
	}
}

GhostRoutingSphereRegistrationResponse::GhostRoutingSphereRegistrationResponse(const GhostRoutingSphereRegistration& evt, EdgeList& egdes):
	RoutingStatus(SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT,
			evt.getRoutingSphereId(),
			evt.getTargetCommunity(),
			evt.getSrcCommunityId()) {
	for (EdgeList::iterator  i = egdes.begin(); i !=  egdes.end(); i++) {
		edge_ifaces.push_back(*i);
	}
}

GhostRoutingSphereRegistrationResponse::~GhostRoutingSphereRegistrationResponse() {
}
prime::ssf::ssf_compact* GhostRoutingSphereRegistrationResponse::pack() {
	prime::ssf::ssf_compact* dp = RoutingStatus::pack();
	dp->add_int(edge_ifaces.size());
	for (EdgeList::iterator  i = edge_ifaces.begin(); i !=  edge_ifaces.end(); i++) {
		dp->add_unsigned_long_long(i->getLocalRank());
	}
	return dp;
}
void GhostRoutingSphereRegistrationResponse::unpack(prime::ssf::ssf_compact* dp) {
	RoutingStatus::unpack(dp);
	int sz=0;
	UID_t t;
	dp->get_int(&sz);
	for(int i=0;i<sz;i++) {
		dp->get_unsigned_long_long(&t);
		edge_ifaces.push_back(EdgeInterface(t));
	}
}

prime::ssf::Event* RoutingStatusRequest::createRoutingStatusRequest(prime::ssf::ssf_compact* dp)
{
	RoutingStatusRequest* e = new RoutingStatusRequest();
	e->unpack(dp);
	return e;
}
prime::ssf::Event* RoutingStatusResponse::createRoutingStatusResponse(prime::ssf::ssf_compact* dp)
{
	RoutingStatusResponse* e = new RoutingStatusResponse();
	e->unpack(dp);
	return e;
}
prime::ssf::Event* GhostRoutingSphereRegistration::createGhostRoutingSphereRegistration(prime::ssf::ssf_compact* dp)
{
	GhostRoutingSphereRegistration* e = new GhostRoutingSphereRegistration();
	e->unpack(dp);
	return e;
}
prime::ssf::Event* GhostRoutingSphereRegistrationResponse::createGhostRoutingSphereRegistrationResponse(prime::ssf::ssf_compact* dp)
{
	GhostRoutingSphereRegistrationResponse* e = new GhostRoutingSphereRegistrationResponse();
	e->unpack(dp);
	return e;
}

SSF_REGISTER_EVENT(RoutingStatusRequest, RoutingStatusRequest::createRoutingStatusRequest);
SSF_REGISTER_EVENT(RoutingStatusResponse, RoutingStatusResponse::createRoutingStatusResponse);
SSF_REGISTER_EVENT(GhostRoutingSphereRegistration, GhostRoutingSphereRegistration::createGhostRoutingSphereRegistration);
SSF_REGISTER_EVENT(GhostRoutingSphereRegistrationResponse, GhostRoutingSphereRegistrationResponse::createGhostRoutingSphereRegistrationResponse);



} // namespace ssfnet
} // namespace prime
