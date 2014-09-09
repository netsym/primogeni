/**
 * \file community.cc
 * \brief source file for the Community class.
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

#include "os/community.h"
#include "os/io_mgr.h"
#include "os/traffic_mgr.h"
#include "os/logger.h"
#include "os/ssfnet_types.h"
#include "os/emu/emulation_device.h"
#include "os/partition.h"
#include "os/packet_event.h"
#include "os/state_logger.h"
#include "net/host.h"
#include "net/link.h"
#include "net/interface.h"
#include "proto/emu/emulation_protocol.h"
#include "os/monitor.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(Community);


bool Community::__is_throttled__ = false;
VirtualTime Community::__collocated_delay__=VirtualTime(1,VirtualTime::MILLISECOND);

void Community::setCollocatedCommunityDelay(VirtualTime t) {
	__collocated_delay__=t;
}

VirtualTime Community::getCollocatedCommunityDelay() {
	return __collocated_delay__;
}

bool Community::isThrottled() {
	return __is_throttled__;
}

void Community::setIsThrottled(bool b) {
	__is_throttled__=b;
}

Community::Community(int _community_id, Partition* _partition) :
				Entity(isThrottled()),
				inited(false), wrapuped(false),
				partition(_partition), community_id(_community_id), nm_serial(0), viz_export_rate(0), iomgr(0), traffic_mgr(0), ext_table(0), state_logger(0), viz_mon(0) {
	if (NULL == partition) {
		LOG_ERROR("the partition cannot be null!"<<endl)
	}

	viz_export_rate = Partition::getVisualiationExportInterval();
	std::cout<<"using intial VEI of "<< viz_export_rate<<endl;

	iomgr=new IOManager(this);
	traffic_mgr=new TrafficManager(this);

	if(partition->shouldUseStatFile()) {
		if(partition->shouldUseStatStream()) {
			LOG_ERROR("One can only log state to EITHER a file or stream!\n");
		}
		ofstream* f=new ofstream();
#ifdef PRIME_SSF_SYNC_MPI
		char foo[256];
		sprintf(foo,"state_part_%i.stats",prime::ssf::ssf_machine_index());
		f->open(foo);
#else
		f->open("state.stats");
#endif
		state_logger = new FileStateLogger(this,f);
	}
	else if(partition->shouldUseStatStream()) {
		if(partition->shouldUseStatFile()) {
			LOG_ERROR("One can only log state to EITHER a file or stream!\n");
		}
		state_logger = new TCPStateLogger(this);
	}
	else if(partition->shouldUseStatFile() && partition->shouldUseStatStream()) {
		LOG_ERROR("One can only log state to EITHER a file or stream!\n");
	}
	else {
		state_logger=0;
	}
	//LOG_DEBUG("Created community "<<this<<endl)
}

Community::~Community() {
	if(state_logger) delete state_logger;
}


void Community::updateVizExportRate(uint64 rate) {
	std::cout<<"updating viz export rate, old="<<viz_export_rate<<", new="<<rate<<endl;
	viz_export_rate=rate;
}


void Community::addToAreaOfInterest(UID_t uid) {
	assert(viz_mon);
	BaseEntity* e = getObject(uid,true);
	if(e) {
		LOG_WARN("Added "<<uid<<" to area of interest\n");
		viz_mon->add(e);
		if(viz_mon->isCancelled()) {
			viz_mon->set(VirtualTime((int64)viz_export_rate,VirtualTime::MILLISECOND));
		}
		//DEBUG_CODE(viz_mon->printDebug(););
		return;
	}
	DEBUG_CODE(
		LOG_WARN("Couldn't add "<<uid<<" to area of interest\n");
		viz_mon->printDebug();
	);
}

void Community::removeFromAreaOfInterest(UID_t uid) {
	assert(viz_mon);
	LOG_WARN("Removed "<<uid<<" to area of interest\n");
	viz_mon->remove(uid);
	if(viz_mon->size()==0) {
		viz_mon->cancel();
	}
	//DEBUG_CODE(viz_mon->printDebug(););
}

/** used to export state to slingshot **/
VizMonitor* viz_mon;


void Community::init() {
	LOG_WARN("Community "<<getCommunityId()<<" is on processor "<<prime::ssf::ssf_processor_index()<<endl);
	inited = true;
	IPPrefixRoute::List& rules = partition->getTopnet()->getExternalNetworkRoutes();
	if(rules.size()>0) {
		ext_table = new ExternalNetworkTable();
		for(IPPrefixRoute::List::iterator i = rules.begin(); i!= rules.end(); i++) {
			ext_table->addRoute(new IPPrefixRoute(*i), false);
		}
		LOG_DEBUG("The external network table:\n"<<*ext_table<<endl);
	}
	rules.clear();
	traffic_mgr->init();
	iomgr->setup_devices();
	if(state_logger) {
		state_logger->init();
		viz_mon = new VizMonitor(this);
	}
	else {
		viz_mon=0;
	}
	LOG_WARN("Community "<<getCommunityId()<<" has "<<monitors.size()<<" monitors"<<endl);
	for(MonitorSet::iterator i = monitors.begin(); i!=monitors.end(); i++) {
		LOG_WARN("init monitor!"<<endl);
		(*i)->set();
	}
	monitors.clear();
}

void Community::wrapup() {
	iomgr->wrapup();
	wrapuped = true;
	if(state_logger)
		state_logger->wrapup();
}

void Community::addMonitor(MonitorTimer* mon) {
	monitors.insert(mon);
}


int Community::getCommunityId() {
	return community_id;
}

void Community::exportState(StateUpdateEvent* update) {
	if(state_logger)state_logger->export_state(update);
	else delete update;
}

UID_t Community::synchronousNameResolution(IPAddress ip)
{
	BaseInterface::IpMap::iterator rv = ip2iface.find(ip);
	if(rv == ip2iface.end()) {
		//the interface its not here, lets look at our cache
		IP2UIDMap::iterator rv1 = remoteIP2UIDCache.find(ip);
		if(rv1 == remoteIP2UIDCache.end()) {
			//no cache either.....
			//lets look at the external network
			if(ext_table) {
				IPPrefixRoute* prefix = ext_table->getRoute((uint32)ip);
				if(prefix) {
					return prefix->getPortalIfaceUID();
				}
			}
			return 0;
		}
		return rv1->second;
	}
	return rv->second->getUID();
}

UID_t Community::synchronousNameResolution(MACAddress mac)
{
	BaseInterface::MacMap::iterator rv = mac2iface.find(mac);
	if(rv == mac2iface.end()) {
		//the interface its not here, lets look at our cache
		MAC2UIDMap::iterator rv1 = remoteMAC2UIDCache.find(mac);
		if(rv1 == remoteMAC2UIDCache.end()) {
			//no cache either.....
			return 0;
		}
		return rv1->second;
	}
	return rv->second->getUID();
}

void Community::synchronousNameResolution(UID_t uid, IPAddress& ipaddr)
{
	BaseEntity* rv = getObject(uid);
	if(rv) {
		//we have it
		if(BaseInterface::getClassConfigType()->isSubtype(rv->getConfigType())) {
			//its an iface
			ipaddr=((BaseInterface*)rv)->getIP();
		}
		else if(Host::getClassConfigType()->isSubtype(rv->getConfigType())) {
			//its a host
			ipaddr=((Host*)rv)->getDefaultIP();
		}
		else {
			LOG_WARN("Asked for the IP of "<<rv->getUName()<<"["<<uid<<"], which is not an iface or host!"<<endl);
			ipaddr=IPAddress::IPADDR_INVALID;
		}
	}
	else {
		//its not here, lets check the cache
		UID2IPMACMap::iterator rv1 = remoteUID2IPMACCache.find(uid);
		if(rv1 != remoteUID2IPMACCache.end()) {
			//its here
			ipaddr = rv1->second.first;
		}
		else {
			ipaddr=IPAddress::IPADDR_INVALID;
		}
	}
	//LOG_DEBUG("mapped uid="<<uid<<" to "<<ipaddr<<endl);
}

void Community::synchronousNameResolution(UID_t uid, MACAddress& macaddr)
{
	BaseEntity* rv = getObject(uid);
	if(rv) {
		//we have it
		if(BaseInterface::getClassConfigType()->isSubtype(rv->getConfigType())) {
			//its an iface
			macaddr=((BaseInterface*)rv)->getMAC();
		}
		else if(Host::getClassConfigType()->isSubtype(rv->getConfigType())) {
			//its a host
			macaddr=((Host*)rv)->getDefaultMAC();
		}
		else {
			LOG_WARN("Asked for the MAC of "<<rv->getUName()<<"["<<uid<<"], which is not an iface or host!"<<endl);
			macaddr=MACAddress::MACADDR_INVALID;
		}
	}
	else {
		//its not here, lets check the cache
		UID2IPMACMap::iterator rv1 = remoteUID2IPMACCache.find(uid);
		if(rv1 != remoteUID2IPMACCache.end()) {
			//its here
			macaddr = rv1->second.second;
		}
		else {
			macaddr=MACAddress::MACADDR_INVALID;
		}
	}
	//LOG_DEBUG("mapped uid="<<uid<<" to "<<macaddr<<endl);
}

void Community::asynchronousNameResolution(IPAddress ip, NameResolutionCallBackWrapper* call_back_obj) {
	NameServiceEvent* evt = new NameServiceEvent(
			nm_serial++,
			NameServiceEvent::LOOKUP_UID_FROM_IP,
			0,
			ip,
			MACAddress::MACADDR_INVALID,
			getCommunityId(),
			getPartition()->ip2communityId(ip));
	name_evt_map.insert(SSFNET_MAKE_PAIR(evt->getSerialNumber(),call_back_obj));
	deliverEvent(evt,0);
}

void Community::asynchronousNameResolution(MACAddress mac, NameResolutionCallBackWrapper* call_back_obj) {
	NameServiceEvent* evt = new NameServiceEvent(
			nm_serial++,
			NameServiceEvent::LOOKUP_UID_FROM_MAC,
			0,
			IPAddress::IPADDR_INVALID,
			mac,
			getCommunityId(),
			getPartition()->mac2communityId(mac));
	name_evt_map.insert(SSFNET_MAKE_PAIR(evt->getSerialNumber(),call_back_obj));
	deliverEvent(evt,0);
}

void Community::asynchronousNameResolution(UID_t uid, NameResolutionCallBackWrapper* call_back_obj) {
	NameServiceEvent* evt = new NameServiceEvent(
			nm_serial++,
			NameServiceEvent::LOOKUP_ADDR_FROM_UID,
			uid,
			IPAddress::IPADDR_INVALID,
			MACAddress::MACADDR_INVALID,
			getCommunityId(),
			getPartition()->uid2communityId(uid));
	name_evt_map.insert(SSFNET_MAKE_PAIR(evt->getSerialNumber(),call_back_obj));
	deliverEvent(evt,0);
}

void Community::deliverEvent(SSFNetEvent* evt, VirtualTime delay) {
	if(evt->getTargetCommunity() == getCommunityId()) {
		if(delay > VirtualTime(0)) {
			//XXX if the delay is non-zero and its for this community, what to do?
			LOG_WARN("Not sure what to do when an event is delivered to a local community with a delay....."<<endl)
		}
		switch(evt->getSSFNetEventType()) {
		case SSFNetEvent::SSFNET_PKT_EVT:
		{
			PacketEvent* pevt=(PacketEvent*)evt;
			LOG_DEBUG("["<<getCommunityId()<<"] receive packet for "<<pevt->target()<<" at time "<<VirtualTime(now()).second()<<endl);

			// find the iface
			BaseEntity *iface = getObject(pevt->target());
			if (NULL == iface) {
				LOG_WARN("Community "<<getCommunityId()
						<<" unable to decode incoming packet from "<<pevt->target()<<" at time "<<VirtualTime(now()).second()<<endl)
						pevt->dropPacket()->erase();
			}
			else {
				//the originating community will send this packet to the the interface it needs to be delivered to.
				SSFNET_DYNAMIC_CAST(Interface*,iface)->receive(pevt->dropPacket());
			}
			evt->free();
		}
		break;
		case SSFNetEvent::SSFNET_EMU_NORMAL_EVT:
		case SSFNetEvent::SSFNET_EMU_PROXY_EVT:
		{
			//EmulationEvent* eevt = (EmulationEvent*) evt;
			//XXX
			LOG_ERROR("NOT DONE"<<endl);
			evt->free();
		}
		break;

		case SSFNetEvent::SSFNET_FLUID_REGISTER_EVT:
		case SSFNetEvent::SSFNET_FLUID_REGISTER_REVERSE_EVT:
		case SSFNetEvent::SSFNET_FLUID_UNREGISTER_EVT:
		case SSFNetEvent::SSFNET_FLUID_ARRIVAL_EVT:
		case SSFNetEvent::SSFNET_FLUID_ACCU_DELAY_EVT:
		case SSFNetEvent::SSFNET_FLUID_ACCU_LOSS_EVT:
		{
			traffic_mgr->processFluidEvent((FluidEvent*)evt);
			evt->free();
		}
		break;
		case SSFNetEvent::SSFNET_NAME_SRV_EVT:
		{
			NameServiceEvent* evt_ = SSFNET_DYNAMIC_CAST(NameServiceEvent*,evt);
			LOG_DEBUG("["<<getCommunityId()<<"] receive a name service event from "<<evt_->getSrcCommunity()<<" at time "<<VirtualTime(now()).second()<<endl);
			switch(evt_->getAction()) {
			case NameServiceEvent::LOOKUP_ADDR_FROM_UID:
			{
				//LOG_DEBUG("Looking up ip of "<<evt_->getUID()<<"\n");
				BaseEntity* e = getObject(evt_->getUID());
				NameServiceEvent* resp=0;
				if(e && Interface::getClassConfigType()->isSubtype(e->getConfigType())) {
					//LOG_DEBUG("["<<getCommunityId()<<"](iface) look up ip/mac from uid "<<evt_->getUID()<<"-->"<<e->getUName()<<endl);
					resp = new NameServiceEvent(
							evt_->getSerialNumber(),
							NameServiceEvent::LOOKUP_RESPONSE,
							0,
							((Interface*)e)->getIP(),
							((Interface*)e)->getMAC(),
							e->getUID(),
							((Interface*)e)->getIP(),
							((Interface*)e)->getMAC(),
							getCommunityId(),
							evt_->getSrcCommunity());
				}
				else if(e && Host::getClassConfigType()->isSubtype(e->getConfigType())) {
					Interface* nic = ((Host*)e)->getInterfaceByIP(((Host*)e)->getDefaultIP());
					//LOG_DEBUG("["<<getCommunityId()<<"](host) look up ip/mac from uid "<<evt_->getUID()<<"-->"<<e->getUName()<<"{"<<nic->getUID()<<"}"<<endl);
					resp = new NameServiceEvent(
							evt_->getSerialNumber(),
							NameServiceEvent::LOOKUP_RESPONSE,
							0,
							nic->getIP(),
							nic->getMAC(),
							nic->getUID(),
							nic->getIP(),
							nic->getMAC(),
							getCommunityId(),
							evt_->getSrcCommunity());
				}
				else {
					if(e) {
						LOG_WARN("Asked for the IP/MAC of an object of type "<<e->getClassConfigType()->getName()<<"("<<e->getUName()<<")"<<endl);
					}
					else {
						LOG_WARN("Asked for the IP/MAC of an object that does not exist!"<<endl);
					}
					resp = new NameServiceEvent(
							evt_->getSerialNumber(),
							NameServiceEvent::LOOKUP_RESPONSE,
							0,
							IPAddress::IPADDR_INVALID,
							MACAddress::MACADDR_INVALID,
							evt_->getRequestedUID(),
							evt_->getRequestedIP(),
							evt_->getRequestedMAC(),
							getCommunityId(),
							evt_->getSrcCommunity());
				}
				deliverEvent(resp,0);
			}
			break;
			case NameServiceEvent::LOOKUP_UID_FROM_IP:
			case NameServiceEvent::LOOKUP_UID_FROM_MAC:
			{
				BaseInterface* e = 0;
				switch(evt_->getAction()) {
				case NameServiceEvent::LOOKUP_UID_FROM_MAC:
				{
					BaseInterface::MacMap::iterator i = mac2iface.find(evt_->getMAC());
					if(i!=mac2iface.end()) {
						e=i->second;
						//LOG_DEBUG("["<<getCommunityId()<<"] look up mac "<<evt_->getMAC()<<"-->"<<i->second->getUName()<<endl);
					}
					else {
						LOG_WARN("Asked for the UID of an unknown mac("<< evt_->getMAC()<<")!"<<endl);
					}
				}
				break;
				case NameServiceEvent::LOOKUP_UID_FROM_IP:
				{
					BaseInterface::IpMap::iterator i = ip2iface.find(evt_->getIP());
					if(i!=ip2iface.end()) {
						e=i->second;
						//LOG_DEBUG("["<<getCommunityId()<<"] look up ip "<<evt_->getIP()<<"-->"<<i->second->getUName()<<endl);
					}
					else {
						LOG_WARN("Asked for the UID of an unknown ip("<< evt_->getIP()<<")!"<<endl);
					}
				}
				break;
				default:
					//no op
					break;
				};
				if(e) {
					deliverEvent(
							new NameServiceEvent(
									evt_->getSerialNumber(),
									NameServiceEvent::LOOKUP_RESPONSE,
									e->getUID(),
									IPAddress::IPADDR_INVALID,
									MACAddress::MACADDR_INVALID,
									e->getUID(),
									evt_->getRequestedIP(),
									evt_->getRequestedMAC(),
									getCommunityId(),
									evt_->getSrcCommunity())
					,0);
				}
				else {
					deliverEvent(
							new NameServiceEvent(
									evt_->getSerialNumber(),
									NameServiceEvent::LOOKUP_RESPONSE,
									0,
									IPAddress::IPADDR_INVALID,
									MACAddress::MACADDR_INVALID,
									evt_->getRequestedUID(),
									evt_->getRequestedIP(),
									evt_->getRequestedMAC(),
									getCommunityId(),
									evt_->getSrcCommunity())
					,0);
				}
			}
			break;
			case NameServiceEvent::LOOKUP_RESPONSE:
			{
				//LOG_DEBUG("0received asyn name repsonse ip="<<evt_->getIP()<<", mac="<<evt_->getMAC()<<", uid="<<evt_->getUID()<<", req uid="<<evt_->getRequestedUID()<<endl)
				NameResolutionCallBackWrapper::Map::iterator i = name_evt_map.find(evt_->getSerialNumber());

				if(i == name_evt_map.end()) {
					LOG_WARN("Received a name service loop response which I was not expecting!"<<endl);
				}
				else {
					NameResolutionCallBackWrapper* cb=i->second;
					if(IPAddress::IPADDR_INVALID == evt_->getIP() && evt_->getUID()==0) {
						//LOG_DEBUG("1received asyn name repsonse ip="<<evt_->getIP()<<", mac="<<evt_->getMAC()<<", uid="<<evt_->getUID()<<", req uid="<<evt_->getRequestedUID()<<endl)
						cb->invalid_query();
					}
					else if(IPAddress::IPADDR_INVALID == evt_->getIP()) {
						//LOG_DEBUG("2received asyn name repsonse ip="<<evt_->getIP()<<", mac="<<evt_->getMAC()<<", uid="<<evt_->getUID()<<", req uid="<<evt_->getRequestedUID()<<endl)

						if(IPAddress::IPADDR_INVALID != evt_->getRequestedIP()) {
							//LOG_INFO("remoteIP2UIDCache["<<evt_->getRequestedIP()<<"]->"<<evt_->getUID()<<"\n");
							remoteIP2UIDCache.insert(SSFNET_MAKE_PAIR(evt_->getRequestedIP(),evt_->getUID()));
						}
						if(MACAddress::MACADDR_INVALID != evt_->getRequestedMAC()) {
							remoteMAC2UIDCache.insert(SSFNET_MAKE_PAIR(evt_->getRequestedMAC(),evt_->getUID()));
						}

						UID2IPMACMap::iterator t = remoteUID2IPMACCache.find(evt_->getUID());
						if(t == remoteUID2IPMACCache.end()) {
							remoteUID2IPMACCache.insert(SSFNET_MAKE_PAIR(evt_->getUID(),SSFNET_MAKE_PAIR(evt_->getRequestedIP(),evt_->getRequestedMAC())));
						}
						else {
							if(IPAddress::IPADDR_INVALID != evt_->getRequestedIP()) {
								t->second.first=evt_->getRequestedIP();
							}
							if(MACAddress::MACADDR_INVALID != evt_->getRequestedMAC()) {
								t->second.second=evt_->getRequestedMAC();
							}
						}
						cb->call_back(evt_->getUID());
					}
					else {
						//LOG_DEBUG("3received asyn name repsonse ip="<<evt_->getIP()<<", mac="<<evt_->getMAC()<<", uid="<<evt_->getUID()<<", req uid="<<evt_->getRequestedUID()<<endl)

						if(IPAddress::IPADDR_INVALID != evt_->getIP()) {
							//LOG_INFO("remoteIP2UIDCache["<<evt_->getIP()<<"]->"<<evt_->getRequestedUID()<<"\n");
							remoteIP2UIDCache.insert(SSFNET_MAKE_PAIR(evt_->getIP(),evt_->getRequestedUID()));
						}
						if(MACAddress::MACADDR_INVALID != evt_->getMAC()) {
							remoteMAC2UIDCache.insert(SSFNET_MAKE_PAIR(evt_->getMAC(),evt_->getRequestedUID()));
						}

						UID2IPMACMap::iterator t = remoteUID2IPMACCache.find(evt_->getRequestedUID());
						if(t == remoteUID2IPMACCache.end()) {
							remoteUID2IPMACCache.insert(SSFNET_MAKE_PAIR(evt_->getRequestedUID(),SSFNET_MAKE_PAIR(evt_->getIP(),evt_->getMAC())));
						}
						else {
							if(IPAddress::IPADDR_INVALID != evt_->getIP()) {
								t->second.first=evt_->getIP();
							}
							if(MACAddress::MACADDR_INVALID != evt_->getMAC()) {
								t->second.second=evt_->getMAC();
							}
						}
						cb->call_back(evt_->getIP(),evt_->getMAC());
					}
					name_evt_map.erase(i);
					delete cb;
				}
			}
			break;
			default:
				LOG_ERROR("Encountered a name service event with an unknown action type("
						<<evt_->getAction()<<") min="<<NameServiceEvent::LOOKUP_MIN<<", max="<<NameServiceEvent::LOOKUP_MAX<<"!"<<endl)
			}
			evt->free();
		}
		break;
		case SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT:
		{
			RoutingStatusRequest* evt_ = SSFNET_DYNAMIC_CAST(RoutingStatusRequest*,evt);
			LOG_DEBUG("["<<getCommunityId()<<"] receive a interface status request (routing) event from "<<evt_->getSrcCommunityId()<<" at time "<<VirtualTime(now()).second()<<endl);
			Interface* iface = SSFNET_DYNAMIC_CAST(Interface*,getObject(evt_->getIfaceId()));
			if(iface!=NULL){
				//check if the interface is available
				bool is_available = iface->getLink()&&iface->isOn();
				//send the response event
				deliverEvent(new RoutingStatusResponse(*evt_, is_available), 0);
				iface->registerConcernedSphere(evt_->getSrcCommunityId(), evt_->getRoutingSphereId());
			}else{
				LOG_WARN("The community("<< getCommunityId()<<") should own the interface with uid="<<evt_->getIfaceId()<<endl);
			}
			evt->free();
		}
		break;
		case SSFNetEvent::SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT:
		{
			RoutingStatusResponse* evt_ = SSFNET_DYNAMIC_CAST(RoutingStatusResponse*,evt);
			LOG_DEBUG("["<<getCommunityId()<<"] receive a interface status response (routing) event from "<<evt_->getSrcCommunityId()<<" at time "<<VirtualTime(now()).second()<<endl);
			RoutingSphere* rs = SSFNET_DYNAMIC_CAST(RoutingSphere*,getObject(evt_->getRoutingSphereId()));
			if(rs!=NULL){
				rs->handleRoutingEvent(evt_->getIfaceId(), evt_->isAvailable());
			}else{
				LOG_WARN("The community("<< getCommunityId()<<") should own the routing sphere with id="<<evt_->getRoutingSphereId()<<endl);
			}
			evt->free();
		}
		break;
		case SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_EVT:
		{
			LOG_WARN("SSFNET_GHOST_ROUTING_SPHERE_REG_EVT events should only be seen during boot strap! Encountered one during model execution!"<<endl);
			evt->free();
		}
		break;
		case SSFNetEvent::SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT:
		{
			LOG_WARN("SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT events should only be seen during boot strap! Encountered one during model execution!"<<endl);
			evt->free();
		}
		break;
		default:
		{//start default
			if(evt->getSSFNetEventType()>SSFNetEvent::SSFNET_TRAFFIC_MIN_TYPE && evt->getSSFNetEventType()<SSFNetEvent::SSFNET_TRAFFIC_MAX_TYPE) {
				switch(((TrafficEvent*)evt)->getTrafficEventType()) {
				case SSFNetEvent::SSFNET_TRAFFIC_START_EVT:
				{
					/**
					 * This method is called by traffic manager to handle the start traffic event,
					 * it calls the corresponding host in this community to start the traffic type.
					 */
					//figure out the host by host uid, and then call the start_traffic() on the host.
					//LOG_DEBUG("DEBUGGING EVENT, community delivers evt="<<evt<<", target comm id="<<evt->getTargetCommunity()<<endl)
					//LOG_WARN("we are cloning the start traffic evt because it gets deleted while the async name lookup is done! this is hack... we don't understand why its needed....."<<endl);
					StartTrafficEvent* evt_ = SSFNET_DYNAMIC_CAST(StartTrafficEvent*,evt->clone());
					//LOG_DEBUG("DEBUGGING EVENT, community delivers start traffic evt="<<evt_<<", target comm id="<<evt_->getTargetCommunity()<<endl)
					Host* h=SSFNET_DYNAMIC_CAST(Host*,getObject(evt_->getHostUID()));
					LOG_DEBUG("The host's uid is: "<<((StartTrafficEvent*)evt_)->getHostUID()<<endl)
					LOG_DEBUG("The start time="<<((StartTrafficEvent*)evt_)->getStartTime().second()<<endl)
					if(evt_->getTrafficType() == NULL) {
						//need to look it up
						Context* ct = getPartition()->lookupContext(evt_->getTrafficTypeUID(),false);
						if(ct == NULL) {
							LOG_WARN("Cannot find traffic type with uid "<<evt_->getTrafficTypeUID()<<"\n");
							return;
						}
						else if(ct->obj == NULL) {
							LOG_ERROR("ACK -- the context map is hosed!"<<endl)
						}
						else {
							evt_->setTrafficType((TrafficType*)ct->obj);
						}
					}
					if(h){
						LOG_DEBUG("The host is: "<<h->getUName()<<", its uid is:"<<h->getUID()<<endl);
						LOG_DEBUG("event is:"<<evt_<<endl);
						h->handleStartTrafficEvent(evt_);
					}
					else{
						if(StaticTrafficType::getClassConfigType()->isSubtype(evt_->getTrafficType()->getConfigType())) {
							//find the which community owns the host and send this evt to that community.
							LOG_DEBUG("ack:"<<getPartition()->uid2communityId(evt_->getHostUID()) <<endl);
							evt_->setTargetCommunity(getPartition()->uid2communityId(evt_->getHostUID()));
							iomgr->writeEvent(evt_, delay);
						}
						else {
							//distributed traffic types should only send start evts to hosts within their community;
							//to do otherwise in an error!
							LOG_WARN("Unable to find host "<<((StartTrafficEvent*)evt)->getHostUID()<<" in community "<<getCommunityId()<<endl);
							evt->free();
						}
					}
				}
				break;

				case SSFNetEvent::SSFNET_TRAFFIC_UPDATE_EVT:
				{
					/**
					 * This event is delivered by the community who owns the protocol session.
					 * It will send the update event to all concerned communities which will call the
					 * specific traffic type specified in the event to process the event.
					 *
					 * "concerned communities" means the communities in the community_id_list excluding
					 * the community that delivers the initial event. The information of concerned
					 * communities for a specific traffic type will be generated during the partitioning phase.
					 *
					 */
					UpdateTrafficTypeEvent* evt_ = SSFNET_DYNAMIC_CAST(UpdateTrafficTypeEvent*,evt);
					if(evt_->getTrafficType() == NULL) {
						//need to look it up
						Context* ct = getPartition()->lookupContext(evt_->getTrafficTypeUID(),false);
						if(ct == NULL) {
							LOG_ERROR("Cannot find traffic type with uid "<<evt_->getTrafficTypeUID()<<"\n")
						}
						else {
							evt_->setTrafficType((TrafficType*)ct->obj);
						}
					}

					DynamicTrafficType* tt=SSFNET_DYNAMIC_CAST(DynamicTrafficType*,evt_->getTrafficType());
					tt->processUpdateEvent(evt_);
					evt->free();
				}
				break;

				case SSFNetEvent::SSFNET_TRAFFIC_FINISH_EVT:
				{
					/**
					 * This event is delivered by the community who owns the protocol session.
					 * The targe_community_id of this event is set to be the src_community_id in start traffic event
					 * before it is sent in the protocol session. Only the src community will process the event.
					 */
					FinishedTrafficEvent* evt_ = SSFNET_DYNAMIC_CAST(FinishedTrafficEvent*,evt);
					if(evt_->getTrafficType() == NULL) {
						//need to look it up
						Context* ct = getPartition()->lookupContext(evt_->getTrafficTypeUID(),false);
						if(ct == NULL) {
							LOG_ERROR("Cannot find traffic type with uid "<<evt_->getTrafficTypeUID()<<"\n");
						}
						else {
							evt_->setTrafficType((TrafficType*)ct->obj);
						}
					}
					evt_->getTrafficType()->processFinishedEvent(evt_);
					evt->free();
				}
				break;
				default:
				{
					LOG_ERROR("Invalid SSFNetEvent traffic type "<<((TrafficEvent*)evt)->getTrafficEventType()
							<<", SSFNET_TRAFFIC_START_EVT="<< SSFNetEvent::SSFNET_TRAFFIC_START_EVT
							<<", SSFNET_TRAFFIC_FINISH_EVT="<< SSFNetEvent::SSFNET_TRAFFIC_FINISH_EVT
							<<", SSFNET_TRAFFIC_UPDATE_EVT="<< SSFNetEvent::SSFNET_TRAFFIC_UPDATE_EVT << endl);
				}
				break;
				};
			}
			else {
				LOG_ERROR("Invalid SSFNetEvent type "<<evt->getSSFNetEventType()<<", SSFNET_EVT_MIN="<< SSFNetEvent::SSFNET_EVT_MIN<<", SSFNET_EVT_MAX="<< SSFNetEvent::SSFNET_EVT_MAX<<endl);
			}
		}//end default
		break;
		};
	}
	else if(evt->getTargetCommunity()==-1){
		LOG_WARN("Found an event of type "<<evt->getSSFNetEventType()<<" which did not have the target community set! Assuming it was targeted to this community "<<getCommunityId()<<"!"<<endl);
		evt->setTargetCommunity(getCommunityId());
		deliverEvent(evt, delay);
	}
	else {
		LOG_DEBUG("asked to handle an evt which needed routing!"<<endl);
		//We need to route this packet
		iomgr->writeEvent(evt, delay);
	}
}

void Community::createChannels() {
	for(BaseInterface::List::iterator i = temp_ifaces.begin(); i!= temp_ifaces.end();i++) {
		iomgr->addInterface(*i);
	}
	temp_ifaces.clear();
	iomgr->createChannels();
}


bool Community::addNode(BaseEntity* node) {
	//LOG_DEBUG("[community "<<getCommunityId()<<"]::addNode("<<node->getUName()<<"["<< node->getUID()<<","<< node->getConfigType()->getName()<<"])"<<endl)
	if(Host::getClassConfigType()->isSubtype(node->getConfigType())) {
		if(SSFNET_DYNAMIC_CAST(Host*,node)->getCommunity()!=NULL) {
			LOG_ERROR("Host name='"<<node->getUName()<<"', uid='"<<node->getUID()<<" is part of more than one community!["<<
					SSFNET_DYNAMIC_CAST(Host*,node)->getCommunity()->getCommunityId()<<","<<getCommunityId()<<"]"<<endl)
		}
		SSFNET_DYNAMIC_CAST(Host*,node)->setCommunity(this);
		//note in the context map that this community owns this obj
		//this information should be propagated to all children of a host
		BaseEntity::List temp;
		temp.push_back(node);
		while(temp.size()>0) {
			node=temp.front();
			temp.pop_front();
			Context* context = getPartition()->lookupContext(node->getUID(),true);
			if(context->getCommunityId() != -1) {
				LOG_ERROR("How did this happen?\n");
			}
			context->setCommunityId(getCommunityId());
			ChildIterator<BaseEntity*> kids = node->getAllChildren();
			while(kids.hasMoreElements()) {
				temp.push_back(kids.nextElement());
			}
			if(BaseInterface::getClassConfigType()->isSubtype(node->getConfigType())) {
				BaseInterface* iface=SSFNET_DYNAMIC_CAST(BaseInterface*,node);
				ip2iface.insert(SSFNET_MAKE_PAIR(iface->getIP(),iface));
				mac2iface.insert(SSFNET_MAKE_PAIR(iface->getMAC(),iface));
				temp_ifaces.push_back(iface);
			}
			else if(EmulationProtocol::getClassConfigType()->isSubtype(node->getTypeId())) {
#ifdef SSFNET_EMULATION
				EmulationProtocol* ep = SSFNET_DYNAMIC_CAST(EmulationProtocol*,node);
				EmulationDevice::List* l = getPartition()->getEmulationDevices(ep->getEmulationDeviceType());
				EmulationDevice* dev = NULL;
				if(l && l->size()) {
					//LOG_DEBUG("Found "<<l->size()<<" devices of type "<<ep->getEmulationDeviceType()<<endl)
					EmulationDevice* first_dev = NULL;
					//we need to see if this emu device type exists for this community
					for(EmulationDevice::List::iterator i=l->begin();i!=l->end();i++) {
						if(!first_dev) {
							first_dev=*i;
						}
						//LOG_DEBUG("this="<<this<<", com for device="<<(*i)->getCommunity()<<endl)
						if((*i)->getCommunity()==this) {
							dev=*i;
						}
					}
					if(!first_dev) {
						//there are none == create one
						//LOG_DEBUG("did not find one in this community"<<endl)
						dev = EmulationDevice::createInstance(ep->getEmulationDeviceType());
						dev->setCommunity(this);
					}
					else {
						LOG_DEBUG("found one in this community"<<endl);
						if(first_dev->requiresSingleInstancePerHost()) {
							//LOG_DEBUG("it requires 1 instance per host, lets make a proxy"<<endl)
							dev = first_dev->createProxyDevice(this);
							dev->setCommunity(this);
						}
						else if(!dev){
							//LOG_DEBUG("using existing dev"<<endl)
							//this community does not have a device of this type yet, lets create it
							dev = first_dev;
						}
					}
				}
				else {
					//this is the first instance of this dev type
					dev = EmulationDevice::createInstance(ep->getEmulationDeviceType());
					dev->setCommunity(this);
				}
				assert(dev);
				getPartition()->registerEmulationDevice(dev);
				iomgr->registerEmulationDevice(dev);
				dev->registerEmulationProtocol(ep);
#else
				LOG_WARN("found emulation protocol under node " << node->getUName() << " when emulation is disabled." << endl);
#endif
			}
			else if (Alias::getClassConfigType()->isSubtype(node->getConfigType())) {
				//force the aliases to be resolved
				//LOG_DEBUG("On Alias "<<node->getUName()<<", uid="<<node->getUID()<<endl)
				SSFNET_DYNAMIC_CAST(Alias*,node)->resolveAliasPath();
			}
		}
		return true;
	}
	else if(RoutingSphere::getClassConfigType()->isSubtype(node->getConfigType())) {
		if(getPartition()->getCommunityWithLowestId()==this) {
			Context* context = getPartition()->lookupContext(node->getUID(),true);
			if(context->getCommunityId() != -1) {
				LOG_ERROR("How did this happen?\n");
			}
			context->setCommunityId(getCommunityId());
		}
	}
	else if(Traffic::getClassConfigType()->isSubtype(node->getConfigType())) {
		//add all the traffics to traffic manager
		Traffic* t=SSFNET_DYNAMIC_CAST(Traffic*,node);
		traffic_mgr->traffics.push_back(t);
		//traffics are not owned by one community so we don't return true like above!
	}
	//else we don't care about it
	return false;
}

void Community::createNameFromId(UID_t uid, SSFNET_STRING& str) {
	str.clear();
	char t[64];
	sprintf(t,"com_%llu",uid);
	str.append(t);
}

Partition* Community::getPartition() {
	return partition;
}

BaseEntity* Community::getObject(UID_t uid, bool return_passive) {
	//LOG_DEBUG("Looking for uid "<<uid<<endl)
	Context* rv =partition->lookupContext(uid,false);
	if(rv) {
		if(rv->getCommunityId() == getCommunityId()) {
			//LOG_DEBUG("\treturning "<<rv->getObj()->second->getUName()<<endl)
			return rv->getObj();
		}
		if(return_passive && rv->getCommunityId() == -1) {
			//LOG_DEBUG("\treturning "<<rv->getObj()->second->getUName()<<endl)
			return rv->getObj();
		}
		else if(return_passive) LOG_WARN(" not returning object cause the com_id="<<rv->getCommunityId()<<endl);
		//else LOG_DEBUG("\t Found the obj, but it was passive and passive objects were not requested!!"<<endl)
	}
	LOG_WARN("unknown uid="<<uid<<endl);
	return NULL;
}

TrafficManager* Community::getTrafficManager(){
	return traffic_mgr;
}

NameServiceEvent::NameServiceEvent() :
				SSFNetEvent(SSFNET_NAME_SRV_EVT,0), serial_number(0),
				action(LOOKUP_MAX),
				uid(0),
				requested_uid(0),
				ip(IPAddress::IPADDR_INVALID),
				requested_ip(IPAddress::IPADDR_INVALID),
				mac(MACAddress::MACADDR_INVALID),
				requested_mac(MACAddress::MACADDR_INVALID),
				src_com(-1)
{
}



NameServiceEvent::NameServiceEvent(long serial_number_, Action action_, UID_t uid_, IPAddress ip_, MACAddress mac_, int src_com_, int target_com):
																		SSFNetEvent(SSFNET_NAME_SRV_EVT,target_com), serial_number(serial_number_),
																		action(action_),
																		uid(uid_),
																		requested_uid(uid_),
																		ip(ip_),
																		requested_ip(ip_),
																		mac(mac_),
																		requested_mac(mac_),
																		src_com(src_com_)
{
	LOG_DEBUG("CREATED NMS["<<serial_number <<"] uid="<<uid<<", ip="<<ip<<", mac="<<mac
			<<", requested_uid="<<requested_uid<<", requested_ip="<<requested_ip<<",src_com="<<src_com<<endl)
}

NameServiceEvent::NameServiceEvent(
		long serial_number_,
		Action action_,
		UID_t uid_,
		IPAddress ip_,
		MACAddress mac_,
		UID_t rec_uid_,
		IPAddress rec_ip_,
		MACAddress rec_mac_,
		int src_com_,
		int target_com):
		SSFNetEvent(SSFNET_NAME_SRV_EVT,target_com), serial_number(serial_number_),
		action(action_),
		uid(uid_),
		requested_uid(rec_uid_),
		ip(ip_),
		requested_ip(rec_ip_),
		mac(mac_),
		requested_mac(rec_mac_),
		src_com(src_com_)
{
	//LOG_DEBUG("CREATED NMS["<<serial_number <<"] uid="<<uid<<", ip="<<ip<<", mac="<<mac
	//		<<", requested_uid="<<requested_uid<<", requested_ip="<<requested_ip<<",src_com="<<src_com<<endl)

}

NameServiceEvent::NameServiceEvent(const NameServiceEvent& evt)
:
		SSFNetEvent(SSFNET_NAME_SRV_EVT,evt.getTargetCommunity()),
		serial_number(evt.serial_number),
		action(evt.action),
		uid(evt.uid),
		requested_uid(evt.requested_uid),
		ip(evt.ip),
		requested_ip(evt.requested_ip),
		mac(evt.mac),
		requested_mac(evt.requested_mac),
		src_com(evt.src_com)
{
	//LOG_DEBUG("CLONED NMS["<<serial_number <<"] uid="<<uid<<", ip="<<ip<<", mac="<<mac
	//		<<", requested_uid="<<requested_uid<<", requested_ip="<<requested_ip<<",src_com="<<src_com<<endl)

}

NameServiceEvent::~NameServiceEvent() {
}

prime::ssf::ssf_compact* NameServiceEvent::pack() {
	prime::ssf::ssf_compact* rv = SSFNetEvent::pack();
	rv->add_long(serial_number);
	rv->add_int(action);
	rv->add_unsigned_long_long(uid);
	rv->add_unsigned_int((uint32)ip);
	rv->add_unsigned_long_long((uint64)mac);
	rv->add_unsigned_long_long(requested_uid);
	rv->add_unsigned_int((uint32)requested_ip);
	rv->add_unsigned_long_long((uint64)requested_mac);
	rv->add_int(src_com);

	//LOG_DEBUG("PACKED NMS["<<serial_number <<"] uid="<<uid<<", ip="<<ip<<", mac="<<mac
	//		<<", requested_uid="<<requested_uid<<", requested_ip="<<requested_ip<<",src_com="<<src_com<<endl)

	return rv;
}

void NameServiceEvent::unpack(prime::ssf::ssf_compact* dp) {
	SSFNetEvent::unpack(dp);
	dp->get_long(&serial_number);
	int t;
	dp->get_int(&t);
	action=(Action)t;
	dp->get_unsigned_long_long(&uid);
	uint32 ip_;
	dp->get_unsigned_int(&ip_);
	ip=ip_;
	uint64 mac_;
	dp->get_unsigned_long_long(&mac_);
	mac=MACAddress(mac_);

	dp->get_unsigned_long_long(&requested_uid);
	dp->get_unsigned_int(&ip_);
	requested_ip=ip_;
	dp->get_unsigned_long_long(&mac_);
	requested_mac=MACAddress(mac_);

	dp->get_int(&src_com);

	//LOG_DEBUG("UNPACKED NMS["<<serial_number <<"] uid="<<uid<<", ip="<<ip<<", mac="<<mac
	//		<<", requested_uid="<<requested_uid<<", requested_ip="<<requested_ip<<",src_com="<<src_com<<endl)

}
prime::ssf::Event* NameServiceEvent::createNameServiceEvent(prime::ssf::ssf_compact* dp) {
	NameServiceEvent* e = new NameServiceEvent();
	e->unpack(dp);
	return e;
}

SSF_REGISTER_EVENT(NameServiceEvent, NameServiceEvent::createNameServiceEvent);



ExternalNetworkTable::ReturnValue ExternalNetworkTable::addRoute(IPPrefixRoute* route, bool replace) {
	// if returns a conflict route; it could be the original or the new
	// route depending on whether 'replace' is true or not
	IPPrefixRoute* conflict = (IPPrefixRoute*)insert((uint32)route->getAddress(), route->getLength(), route, replace);
	if(conflict) {
		if(replace) {
			delete conflict; // this is the old route
			return FT_ROUTE_OVERWRITTEN;
		} else {
			assert(conflict == route);
			delete conflict;  // this is the new route
			return FT_ROUTE_NOT_REPLACED;
		}
	}
	else {
		return FT_ROUTE_SUCCESS;
	}
}

ExternalNetworkTable::ReturnValue ExternalNetworkTable::removeRoute(IPPrefixRoute* route) {
	IPPrefixRoute* old = (IPPrefixRoute*)remove((uint32)route->getAddress(), route->getLength());
	if(old) {
		// we need to compare the existing route with the one we want to
		// remove; if they don't match, we reinstall the existing one;
		// otherwise, we remove it
		if(*route != *old) {
			addRoute(old);
		}
		else {
			delete old;
		}
		delete route;
		return FT_ROUTE_SUCCESS;
	} else {
		delete route;
		return FT_ROUTE_NOT_FOUND;
	}
}

IPPrefixRoute* ExternalNetworkTable::getRoute(uint32 ipaddr) {
	IPPrefixRoute* rv = NULL;
	// find out the route for the given ip address
	lookup(ipaddr, (TrieData**)&rv);
	return rv;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const ExternalNetworkTable& x) {
	ExternalNetworkTable::dump_helper(x.root, 0, 0, os);
	return os;
}

void ExternalNetworkTable::dump_helper(TrieNode* root, uint32 sofar,
		int n, PRIME_OSTREAM &os) {
	// Recurse on children, if any.
	for (int i=0; i < TRIE_KEY_SPAN; i++) {
		if(root->children[i]) {
			dump_helper(root->children[i], (sofar << 1) | i, n+1, os);
		}
	}
	if(root->data) {
		os << "\t" << *((IPPrefixRoute*)root->data)<<"\n";
	}
};


} // namespace ssfnet
} // namespace prime

