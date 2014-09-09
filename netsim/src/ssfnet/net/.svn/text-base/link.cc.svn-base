/**
 * \file link.cc
 * \brief Source file for the Link and LinkInfo classes.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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

#include <assert.h>
#include <stdio.h>
#include<list>
#include <math.h>

#include "os/logger.h"
#include "os/community.h"
#include "net/net.h"
#include "net/link.h"
#include "os/alias.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(Link)

Link::Link() {
}

Link::~Link() {
	unshared.ip2iface.clear();
	unshared.mac2iface.clear();

	// delete only ghost interfaces
	ChildIterator<BaseInterface*> si = attachments();
	while (si.hasMoreElements()) {
		BaseInterface* nic = si.nextElement();
		if (nic->getTypeId() == GhostInterface::getClassConfigTypeId())
			delete nic; // only delete the ghost interfaces; others are done through host
	}
}
void Link::initStateMap() {
	BaseEntity::initStateMap();
}


void Link::init() {
	LOG_DEBUG("Link init for "<<getUName()<<endl)
	ChildIterator<BaseInterface*> si = attachments();
	while (si.hasMoreElements()) {
		BaseInterface* nic = si.nextElement();
		LOG_DEBUG("\tattachment="<<nic->getUName()<<endl)
		nic->setLink(this);
		if (nic->getTypeId() == GhostInterface::getClassConfigTypeId()) {
			nic->init(); // only init ghost interfaces, others are done through host
		}
		if(nic->getMAC()!=MACAddress::MACADDR_INVALID) {
			unshared.mac2iface.insert(SSFNET_MAKE_PAIR(nic->getMAC(), nic));
			LOG_DEBUG("\t\tadded mac ("<<nic->getMAC()<<")\n")
		}
		else {
			LOG_DEBUG("\t\tinvalid mac ("<<nic->getMAC()<<")\n")
		}
		if (nic->getIP() != IPAddress::IPADDR_INVALID) {
			unshared.ip2iface.insert(SSFNET_MAKE_PAIR((uint32)nic->getIP(), nic));
			LOG_DEBUG("\t\tadded ip ("<<nic->getIP()<<")\n")
		}
		else {
			LOG_DEBUG("\t\tinvalid ip ("<<nic->getIP()<<")\n")
		}
	}
}

void Link::wrapup() {
	ChildIterator<BaseInterface*> si = attachments();
	while (si.hasMoreElements()) {
		BaseInterface* nic = si.nextElement();
		if (nic->getTypeId() == GhostInterface::getClassConfigTypeId())
			nic->wrapup(); // only wrapup ghost interfaces, others are done through host
	}
}

Net* Link::inNet() {
	return SSFNET_STATIC_CAST(Net*,getParent());
}

ChildIterator<BaseInterface*> Link::getAttachments(int* nattached) {
	if (nattached)
		*nattached = attachmentsSize();
	return attachments();
}
BaseInterface* Link::getInterfaceByIP(IPAddress ip) {
	IP2IFACE_MAP::iterator iter = unshared.ip2iface.find((uint32) ip);
	if (iter != unshared.ip2iface.end())
		return (*iter).second;
	else
		return 0;
}

BaseInterface* Link::getInterfaceByMAC(MACAddress mac) {
	MAC2IFACE_MAP::iterator iter = unshared.mac2iface.find(mac);
	if (iter != unshared.mac2iface.end())
		return (*iter).second;
	else {
		LOG_DEBUG("ACK -- couldn't find "<<mac<<", name="<<getUName()<<", mac2iface.size()="<<unshared.mac2iface.size()<<endl)
		for(iter=unshared.mac2iface.begin();iter!=unshared.mac2iface.end();iter++) {
			LOG_DEBUG("\t"<<(*iter).second->getUName()<<", mac="<<(*iter).second->getMAC()<<endl);
		}
		return 0;
	}
}


LinkInfo* Link::getLinkInfo(Community* com) {
	LinkInfo::Map::iterator it = unshared.com_id2link_info.find(com->getCommunityId());
	if (it != unshared.com_id2link_info.end()) {
		return it->second;
	}
	return NULL;
}

void Link::registerLinkInfo(Community* com, LinkInfo* link_info) {
	LinkInfo::Map::iterator it = unshared.com_id2link_info.find(com->getCommunityId());
	if (it != unshared.com_id2link_info.end()) {
		LOG_ERROR("tried to register a LinkInfo for a community who already had one, com_id="<<com->getCommunityId()<<endl)
	}
	unshared.com_id2link_info.insert(SSFNET_MAKE_PAIR(com->getCommunityId(),link_info));
}

IPPrefix  Link::getIPPrefix() {
	return unshared.ip_prefix.read();
}

uint8_t  Link::getIPPrefixLen() {
	return shared.ip_prefix_len.read();
}

VirtualTime Link::getDelay() {
	return VirtualTime(shared.delay.read(), VirtualTime::SECOND);
}

float Link::getBandwidth() {
	return shared.bandwidth.read();
}

int16_t Link::getNextHop(BaseInterface* src_iface,
			Packet* pkt,
			Community* src_com,
			BaseInterface*& next_hop_iface,
			int& remote_idx,
			int16_t _bus_idx) {
	LinkInfo* link_info=getLinkInfo(src_com);

	if(link_info->getLocalInterfaceCount()==2) {
		//they are both here
		if(link_info->getLocalInterfaces()[0]==src_iface) {
			next_hop_iface=link_info->getLocalInterfaces()[1];
		}
		else {
			next_hop_iface=link_info->getLocalInterfaces()[0];
		}
		remote_idx=-1;
	}
	else if(link_info->getLocalInterfaceCount()+link_info->getRemoteInterfaceCount()==2) {
		//one is here the other is remote...
		if(link_info->getLocalInterfaces()[0]==src_iface) {
			next_hop_iface=NULL;
			remote_idx=0;
		}
		else {
			next_hop_iface=link_info->getLocalInterfaces()[0];
			remote_idx=-1;
		}
	}
	else {
		//LOG_DEBUG("this link has "<<attachmentsSize()<<" attachments, so we are looking for the nix vector...."<<endl)
		//get nix-vector from pkt, if nix-vector does not exist, raise error
		if(pkt) {
			NixVector* nix_vec=pkt->getNixVector();
			if(!nix_vec){
				//LOG_DEBUG("There was no nix vector."<<endl)
				next_hop_iface=NULL;
				remote_idx=-1;
			}
			else{
				//otherwise get the num_of_bits_bus figure out the bus_idx to determine which attachment to use.
				uint8_t num_of_bits_bus=(int)ceil(log(attachmentsSize())/log(2));
				uint32_t bus_idx=nix_vec->getOutboundIfaceIdx(num_of_bits_bus);

				//LOG_DEBUG("Fetching bus idx "<<bus_idx<<endl)
				if(bus_idx < link_info->getLocalInterfaceCount()+link_info->getRemoteInterfaceCount()) {
					if(link_info->getIfaceIdxListType()[bus_idx]) {
						next_hop_iface=NULL;
						remote_idx=link_info->getIfaceIdxList()[bus_idx];
					}
					else {
						next_hop_iface=link_info->getLocalInterfaces()[link_info->getIfaceIdxList()[bus_idx]];
						remote_idx=-1;
					}
				}
				return bus_idx;
			}
		}
		else {
			if(_bus_idx<0) {
				LOG_ERROR("must eitehr pass in the pkt or the _bus_idx!"<<endl);
			}
			if(_bus_idx < (int16)(link_info->getLocalInterfaceCount()+link_info->getRemoteInterfaceCount())) {
				if(link_info->getIfaceIdxListType()[_bus_idx]) {
					next_hop_iface=NULL;
					remote_idx=link_info->getIfaceIdxList()[_bus_idx];
				}
				else {
					next_hop_iface=link_info->getLocalInterfaces()[link_info->getIfaceIdxList()[_bus_idx]];
					remote_idx=-1;
				}
			}
		}
	}
	return _bus_idx;
}

long Link::getMemUsage_object_properties() {
	long t = BaseEntity::getMemUsage_object_properties();
	LinkInfo::Map::pointer p = unshared.com_id2link_info.get_allocator().allocate(1);
	t+=(sizeof(p)+sizeof(LinkInfo))*unshared.com_id2link_info.size();
	unshared.com_id2link_info.get_allocator().deallocate(p,1);
	return t;
}
long Link::getMemUsage_object_states() {
	long t= unshared.getMemUsage_object();
	IP2IFACE_MAP::pointer p1 = unshared.ip2iface.get_allocator().allocate(1);
	MAC2IFACE_MAP::pointer p2 = unshared.mac2iface.get_allocator().allocate(1);
	t+= (sizeof(p1)+sizeof(BaseInterface*))*unshared.ip2iface.size();
	t+= (sizeof(p2)+sizeof(BaseInterface*))*unshared.mac2iface.size();
	unshared.ip2iface.get_allocator().deallocate(p1,1);
	unshared.mac2iface.get_allocator().deallocate(p2,1);
	return t;
}

long Link::getMemUsage_class() {
	return sizeof(*this)+getPropertyMap()->getMemUsage_class()+getStateMap()->getMemUsage_class();
}


LinkInfo::LinkInfo() :
	remote_iface_size(0), local_iface_size(0) {
}
LinkInfo::~LinkInfo() {
	delete remote_ifaces;
	delete local_ifaces;
	delete iface_idx;
	delete idx_type;
}


uint32_t LinkInfo::getRemoteInterfaceCount() {
	return remote_iface_size;
}
uint32_t LinkInfo::getLocalInterfaceCount() {
	return local_iface_size;
}
LinkInfo::RemoteIface** LinkInfo::getRemoteInterfaces() {
	return remote_ifaces;
}
Interface** LinkInfo::getLocalInterfaces() {
	return local_ifaces;
}
uint* LinkInfo::getIfaceIdxList(){return iface_idx;}

bool* LinkInfo::getIfaceIdxListType() { return idx_type; }




} // namespace ssfnet
} // namespace prime
