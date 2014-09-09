/**
 * \file io_mgr.cc
 * \brief Source file for the IOManager class.
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

#include "os/io_mgr.h"
#include "os/packet.h"
#include "os/packet_event.h"
#include "os/logger.h"
#include "os/community.h"
#include "os/partition.h"
#include "net/interface.h"
#include "net/link.h"
#include "net/host.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(IOManager)

IOManager::IOManager(Community* comm) :
	prime::ssf::Process(comm, true), ic(0),mycomm(comm),mypart(comm->getPartition()) {
	LOG_DEBUG("Created the IOManager for community "<<mycomm->getCommunityId()<<endl)
}

IOManager::~IOManager() {
	LOG_DEBUG("Delete the IOManager for community "<<mycomm->getCommunityId()<<endl)
}

void IOManager::setup_devices() {
#ifdef SSFNET_EMULATION
	LOG_DEBUG(this<<" -- have "<<emu_devs.size()<<" emu devs"<<endl)
	for(EmuDevMap::iterator it=emu_devs.begin();it!=emu_devs.end();it++) {
		LOG_DEBUG("initing dev id="<<it->first<<"("<<it->second->getDeviceId()<<", dev="<<it->second<<endl)
		it->second->init();
	}
#endif
}

void IOManager::wrapup() {
	ssf::Process::wrapup();
#ifdef SSFNET_EMULATION
	for(EmuDevMap::iterator it=emu_devs.begin();it!=emu_devs.end();it++) {
		it->second->wrapup();
	}
#endif
}


void IOManager::addInterface(BaseInterface* iface) {
	Link* l = iface->getLink();
	if (l) {
		LinkInfo* li= l->getLinkInfo(mycomm);
		//see if there is already a link info for this community
		if (NULL == li) {
			//we need to create one and register it with the link
			//this is used by the interface to send packets to the
			//other interfaces that are connected to the link
			li = createLinkInfo(l);
			l->registerLinkInfo(mycomm, li);
		}
	}
}


void IOManager::writeEvent(SSFNetEvent* evt, VirtualTime delay) {
	if(evt->getTargetCommunity()== mycomm->getCommunityId()) {
		LOG_WARN("Tried to write an event of type "<<evt->getSSFNetEventType()
				<<" to myself(id="<<mycomm->getCommunityId()<<"). Dropping evt!"<<endl);
		evt->release();
	}
	else if(evt->getTargetCommunity() == -1 ) {
		LOG_WARN("Tried to write an event of type "<<evt->getSSFNetEventType()
				<<" which did not have the target community set! Assuming it was targeted to this community (id="
				<<mycomm->getCommunityId()<<")!"<<endl);
		evt->setTargetCommunity(mycomm->getCommunityId());
		mycomm->deliverEvent(evt, delay);
	}
	else {
		int nh = evt->getTargetCommunity();

		//first lets see if we can directly send it
		ComId2ChannelDelayPairMap::iterator oc = out_channels.find(nh);
		if(oc==out_channels.end()) {
			//we need to route it
			nh = mypart->getNextCommunityInPath(mycomm->getCommunityId(),nh);
			if(nh == mycomm->getCommunityId()) {
				//the routes are wrong!
				LOG_ERROR("The routes are wrong! Routing says to go from "<<mycomm->getCommunityId()<<" to "<<evt->getTargetCommunity()<<" the next hop should be "<<nh<<"(this community)!"<<endl);
				evt->release();
				return;
			}
			else {
				oc = out_channels.find(nh);
				if(oc==out_channels.end()) {
					LOG_ERROR("The routes are wrong! Routing says to go from "<<mycomm->getCommunityId()<<" to "<<evt->getTargetCommunity()<<" the next hop should be "<<nh<<", however there is no out channel to "<<nh<<"!!"<<endl);
					evt->release();
					return;
				}
			}
		}
		//get the delay and channel
		//XXX can we use zero delay here?
		LOG_DEBUG("We are routing evt from this comm("<<mycomm->getCommunityId()<<") to "<<evt->getTargetCommunity()<<" via "<<nh<<" with a delay of "<<oc->second.second.second()<<" seconds."<<endl);
		//oc->second.first->write(evt,oc->second.second+delay);
		oc->second.first->write(evt,delay);
	}
}

void IOManager::createChannels() {
	LOG_INFO("[com:"<<mycomm->getCommunityId()<<"]Creating ssf channels\n");
	SSFNET_STRING name;
	name.clear();
	Community::createNameFromId(mycomm->getCommunityId(),name);
	name.append(".evt");

	LOG_INFO("[com:"<<mycomm->getCommunityId()<<"]Creating input channel "<<name<<endl)
	// create the input channel
	ic = new prime::ssf::inChannel(const_cast<char*> (name.c_str()), owner());

	waitsOn(ic);

	if (channel_info.size() == 0) {
		// we don't expect to send or recieve packets from other communities
		// so we don't need in/out channels

#ifdef SSFNET_EMULATION
		//XXX what should this be?
		//we need a delay between the channels of the co-located communities that
		//may proxy events between them.....
	        VirtualTime delay = VirtualTime(10,VirtualTime::MICROSECOND); // DESIGN FLAW HERE!!!

		for(EmuDevMap::iterator it=emu_devs.begin();it!=emu_devs.end();it++) {
			if(it->second->isEmulationDeviceProxy()) {
				EmulationDeviceProxy* e = (EmulationDeviceProxy*)it->second;
				if(e->getEmulationDevice()->getCommunity() != owner()) {
					name.clear();
					Community::createNameFromId(e->getEmulationDevice()->getCommunity()->getCommunityId(),name);
					name.append(".evt");

					//create the channel
					prime::ssf::outChannel* oc=new prime::ssf::outChannel(owner(),delay);

					//map the emu channel
					oc->mapto(const_cast<char*>(name.c_str()));
					out_channels.insert(SSFNET_MAKE_PAIR(e->getEmulationDevice()->getCommunity()->getCommunityId(),SSFNET_MAKE_PAIR(oc,delay)));
				}
				else {
					LOG_ERROR("should never see this!"<<endl)
				}
			}
		}
#endif
		return;
	}
	else {
		VirtualTime* min_min_delay=NULL;

		for(ComId2RemoteIfaceList::iterator i=channel_info.begin();i!=channel_info.end();i++) {
			LOG_INFO("\t[com:"<<mycomm->getCommunityId()<<"]Looking for smallest delay to community "<<i->first<<endl)
			//find the channel delay
			VirtualTime* min_delay=NULL;
			for(LinkInfo::RemoteIface::List::iterator j=i->second->begin();j!=i->second->end();j++) {
				LOG_INFO("\t\t[com:"<<mycomm->getCommunityId()<<"]remote iface, com_id="<<(*j)->remote_com_id
						<<", delay="<<(*j)->delay
						<<", iface="<<(*j)->iface->getUName()
						<<", link="<<(*j)->iface->getLink()->getUName()<<endl);
				if(!min_delay || (*j)->delay < *min_delay ) {
					min_delay=&((*j)->delay);
				}
			}
			if(!min_min_delay  || *min_min_delay < *min_delay ) {
				min_min_delay=min_delay;
			}
			LOG_INFO("\t[com:"<<mycomm->getCommunityId()<<"]Creating evt channel to com_id="<<i->first<<" with a delay of "<<min_delay->second()<<endl)

			//create the channel
			prime::ssf::outChannel* oc=new prime::ssf::outChannel(owner(),*min_delay);

			//map the channel
			name.clear();
			Community::createNameFromId(i->first,name);
			name.append(".evt");
			oc->mapto(const_cast<char*>(name.c_str()));

			//save a ptr to the channel in the remote ifaces...
			for(LinkInfo::RemoteIface::List::iterator j=i->second->begin();j!=i->second->end();j++) {
				(*j)->oc=oc;
				(*j)->delay -= *min_delay;
			}
			out_channels.insert(SSFNET_MAKE_PAIR(i->first,SSFNET_MAKE_PAIR(oc,*min_delay)));
		}
		channel_info.clear();

		LOG_INFO("\t[com:"<<mycomm->getCommunityId()<<"]Checking connectivity of collocated communities."<<endl);

		//we need links to all collocated communities
		Community::Map& coms = Partition::getInstance()->getCommunities();
		for(Community::Map::iterator cit = coms.begin(); cit!=coms.end();cit++) {
			if(cit->first == mycomm->getCommunityId()) continue;
			ComId2ChannelDelayPairMap::iterator oc = out_channels.find(cit->first);
			if(oc == out_channels.end()) {
				//we don't have a channel yet
				LOG_INFO("\t[com:"<<mycomm->getCommunityId()<<"]Creating collocated evt channel to com_id="<<cit->first<<" with a delay of "<<mycomm->getCollocatedCommunityDelay().second()<<endl);

				//create the channel
				prime::ssf::outChannel* oc=new prime::ssf::outChannel(owner(),mycomm->getCollocatedCommunityDelay());

				//map the channel
				name.clear();
				Community::createNameFromId(cit->first,name);
				name.append(".evt");
				oc->mapto(const_cast<char*>(name.c_str()));
				out_channels.insert(SSFNET_MAKE_PAIR(cit->first,SSFNET_MAKE_PAIR(oc,mycomm->getCollocatedCommunityDelay())));
			}
			else {
				LOG_INFO("\t[com:"<<mycomm->getCommunityId()<<"]Already have  an evt channel to com_id="<<cit->first<<endl);
			}
		}

#ifdef SSFNET_EMULATION
		for(EmuDevMap::iterator it=emu_devs.begin();it!=emu_devs.end();it++) {
			if(it->second->isEmulationDeviceProxy()) {
				EmulationDeviceProxy* e = (EmulationDeviceProxy*)it->second;
				if(e->getEmulationDevice()->getCommunity() != owner()) {
					//check that we don't already have a link to this community....
					ComId2ChannelDelayPairMap::iterator oc = out_channels.find(e->getEmulationDevice()->getCommunity()->getCommunityId());
					if(oc==out_channels.end()) {
						//we need to add an oc
						name.clear();
						Community::createNameFromId(e->getEmulationDevice()->getCommunity()->getCommunityId(),name);
						name.append(".evt");

						//create the channel
						prime::ssf::outChannel* emu_oc=new prime::ssf::outChannel(owner(),*min_min_delay);

						//map the emu channel

						emu_oc->mapto(const_cast<char*>(name.c_str()));
						out_channels.insert(SSFNET_MAKE_PAIR(e->getEmulationDevice()->getCommunity()->getCommunityId(),SSFNET_MAKE_PAIR(emu_oc,*min_min_delay)));
					}
				}
				else {
					LOG_ERROR("should never see this!"<<endl)
				}
			}
		}
#endif
	}
	DEBUG_CODE(
	SSFNET_STRING ack;
	for(ComId2ChannelDelayPairMap::iterator oc = out_channels.begin();oc!=out_channels.end();oc++) {
		if(ack.length()==0)ack.append("[");
		else ack.append(", ");
		char t[100];
		sprintf(t,"%d",oc->first);
		ack.append(t);
	}
	if(ack.length()==0)
		ack.append("[]");
	else
		ack.append("]");
	LOG_INFO("From community "<<mycomm->getCommunityId()<<"(part="<<mycomm->getPartition()->getPartitionId()<<") we have out channels to "<<ack<<endl);
	);

}

LinkInfo* IOManager::createLinkInfo(Link* link) {
	SSFNET_LIST(Interface*) local_ifaces;
	SSFNET_LIST(Interface*) remote_ifaces;
	SSFNET_LIST(GhostInterface*) ghost_ifaces;
	Partition* p = mycomm->getPartition();
	int idx=0, l_idx=0, r_idx=0;
	ChildIterator<BaseInterface*> it = link->getAttachments(&idx);

	LinkInfo* li = new LinkInfo();

	//setup the idx stuff
	li->iface_idx = new uint[idx];
	li->idx_type = new bool[idx];
	idx=0;
	while (it.hasMoreElements()) {
		BaseInterface* iface = it.nextElement();
		if (GhostInterface::getClassConfigType()->isSubtype(
				iface->getConfigType())) {
			ghost_ifaces.push_back(SSFNET_DYNAMIC_CAST(GhostInterface*,iface));
			li->iface_idx[idx]=r_idx;
			li->idx_type[idx]=true;
			idx++;
			r_idx++;
		} else {
			//we know its a link in this partition, lets see which community it belongs to
			Community::Set s;
			int my_com_id = mycomm->getCommunityId();
			int remote_com_id = p->uid2communityId(iface->getUID());
			if(remote_com_id>=0) {
				if (remote_com_id == my_com_id) {
					//its mine
					local_ifaces.push_back(SSFNET_DYNAMIC_CAST(Interface*,iface));
					li->iface_idx[idx]=l_idx;
					li->idx_type[idx]=false;
					idx++;
					l_idx++;
				} else {
					//its in a comm in a different community (in this partition)
					remote_ifaces.push_back(SSFNET_DYNAMIC_CAST(Interface*,iface));
					li->iface_idx[idx]=r_idx;
					li->idx_type[idx]=true;
					idx++;
					r_idx++;
				}
			} else {
				LOG_ERROR("Should never see this! couldn't find the community id of "
						<<iface->getUName()<<"["<<iface->getClassConfigType()->getName()<<","<<iface->getUID()<<"], remote_com_id="<<remote_com_id<<endl)
			}
		}
	}

	//setup the local interfaces
	li->local_iface_size = local_ifaces.size();
	assert(li->local_iface_size == (uint)l_idx);
	li->local_ifaces = new Interface*[li->local_iface_size];
	idx=0;
	for (SSFNET_LIST(Interface*)::iterator i=local_ifaces.begin();i!=local_ifaces.end();i++) {
		li->local_ifaces[idx]=*i;
		idx++;
	}

	//setup the remote interfaces;
	li->remote_iface_size=remote_ifaces.size()+ghost_ifaces.size();
	assert(li->remote_iface_size == (uint)r_idx);
	li->remote_ifaces=new LinkInfo::RemoteIface*[li->remote_iface_size];
	idx=0;
	ComId2RemoteIfaceList::iterator cinfo;
	for(SSFNET_LIST(Interface*)::iterator i=remote_ifaces.begin();i!=remote_ifaces.end();i++) {
		if(NULL==(*i)->inHost()){
			LOG_ERROR("The host is null for "<<(*i)->getUName()<<endl)
		}
		if(NULL==(*i)->inHost()->getCommunity()){
			LOG_ERROR("The community is null for "<<(*i)->inHost()->getUName()<<endl)
		}
		UID_t cid=(*i)->inHost()->getCommunity()->getCommunityId();
		li->remote_ifaces[idx]=
				new LinkInfo::RemoteIface(*i,cid,link->getDelay());
		cinfo=channel_info.find(cid);
		if(cinfo==channel_info.end()) {
			channel_info.insert(SSFNET_MAKE_PAIR(cid,new LinkInfo::RemoteIface::List()));
			cinfo=channel_info.find(cid);
		}
		cinfo->second->push_back(li->remote_ifaces[idx]);
		idx++;
	}
	//setup the ghosts
	for(SSFNET_LIST(GhostInterface*)::iterator i=ghost_ifaces.begin();i!=ghost_ifaces.end();i++) {
		li->remote_ifaces[idx]=
		new LinkInfo::RemoteIface(*i,(*i)->getRemoteCommunityId(),link->getDelay());
		cinfo=channel_info.find((*i)->getRemoteCommunityId());
		if(cinfo==channel_info.end()) {
			channel_info.insert(SSFNET_MAKE_PAIR((*i)->getRemoteCommunityId(),new LinkInfo::RemoteIface::List()));
			cinfo=channel_info.find((*i)->getRemoteCommunityId());
		}
		cinfo->second->push_back(li->remote_ifaces[idx]);
		idx++;
	}
	return li;
}

//! SSF PROCEDURE SIMPLE
void IOManager::action() {
	prime::ssf::Event** evts = ic?ic->activeEvents():0;
	for (int i = 0; evts && evts[i]; i++) {
		if(mycomm->getCommunityId() == ((SSFNetEvent*)evts[i])->getTargetCommunity()) {
			LOG_DEBUG("[handling]"<<mycomm->getCommunityId()<<" received evt with tgt com "<<((SSFNetEvent*)evts[i])->getTargetCommunity()<<endl);
			mycomm->deliverEvent((SSFNetEvent*) evts[i], 0);
		}
		else {
			LOG_DEBUG("[fwding  ]"<<mycomm->getCommunityId()<<" received evt with tgt com "<<((SSFNetEvent*)evts[i])->getTargetCommunity()<<endl);
			writeEvent((SSFNetEvent*) evts[i], 0);
		}
	}
	waitOn();
}

#ifdef SSFNET_EMULATION
void IOManager::registerEmulationDevice(EmulationDevice* dev) {
	if(!emu_devs.insert(SSFNET_MAKE_PAIR(dev->getDeviceId(),dev)).second) {
		EmulationDevice* cur_dev = emu_devs.find(dev->getDeviceId())->second;
		if(dev != cur_dev) {
			LOG_ERROR("duplicate emulation device id "<<dev->getDeviceId()<<" found"<<endl)
		}
	}
}
#else
void IOManager::registerEmulationDevice(void* dev) {
	LOG_ERROR("this function shouldn't be called"<<endl);
};
#endif

}// namespace ssfnet
}// namespace prime
