/**
 * \file host.cc
 * \brief Source file for the Host class.
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
#include <stdlib.h>

#include "os/partition.h"
#include "os/logger.h"
#include "os/state_logger.h"
#include "os/ssfnet_exception.h"
#include "os/community.h"
#include "os/timer_queue.h"
#include "os/protocol_session.h"
#include "os/emu/portal_emu_proto.h"
#include "net/net.h"
#include "net/host.h"
#include "proto/routing_protocol.h"
#include "proto/ipv4/ipv4_session.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(Host);

#define UPDATE_LOSS_INTV 0.01

void PortalIfaceTable::addPortal(IPPrefix& network, Interface* iface, bool replace) {
	TrafficPortalInterface* conflict = (TrafficPortalInterface*) insert((uint32)network.getAddress(), network.getLength(), new TrafficPortalInterface(network,iface), replace);
	if(conflict) {
		if(replace) {
			delete conflict; // this is the old route
		} else {
			delete conflict;  // this is the new route
		}
	}
}

TrafficPortalInterface* PortalIfaceTable::getPortal(uint32 ipaddr) {
	TrafficPortalInterface* rv = NULL;
	// find out the route for the given ip address
	lookup(ipaddr, (TrieData**)&rv);
	return rv;
}


PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TrafficPortalInterface& x) {
	return os <<"["<<x.prefix<<" --> "<<x.iface->getUName()<<"]";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const PortalIfaceTable& x) {
	PortalIfaceTable::dump_helper(x.root, 0, 0, os);
	return os;
}

void PortalIfaceTable::dump_helper(TrieNode* root, uint32 sofar,
		int n, PRIME_OSTREAM &os) {
	// Recurse on children, if any.
	for (int i=0; i < TRIE_KEY_SPAN; i++) {
		if(root->children[i]) {
			dump_helper(root->children[i], (sofar << 1) | i, n+1, os);
		}
	}
	if(root->data) {
		os << "\t" << *((TrafficPortalInterface*)root->data)<<"\n";
	}
}

class HostTimerQueueData: public TimerQueueData {
public:
	enum QueueDataType {
		START_TRAFFIC,
		GENERIC
	};

	HostTimerQueueData(ProtocolSession* s, int h, VirtualTime t, void* p) :
		TimerQueueData(s->inHost()->hostTimeToAbsoluteTime(t)), sess(s),
		handle(h), extra(p) {
	}

	HostTimerQueueData(int h, VirtualTime t, void* p) :
		TimerQueueData(t), sess(0), handle(h), extra(p) {
	}

	/** The destructor. */
	virtual ~HostTimerQueueData(){ }

	ProtocolSession* getSession() {
		return sess;
	}
	int getHandle() {
		return handle;
	}
	void* getExtra() {
		return extra;
	}

	virtual HostTimerQueueData::QueueDataType getQueueDataType() { return GENERIC; }

private:
	ProtocolSession* sess;
	int handle;
	void* extra;
};

class StartTrafficTimerData: public HostTimerQueueData {
public:
	StartTrafficTimerData(VirtualTime t, int handle) :
		HostTimerQueueData(handle,t,0){
	}
	virtual ~StartTrafficTimerData(){ }
	virtual HostTimerQueueData::QueueDataType getQueueDataType() { return START_TRAFFIC; }
};

class HostTimerQueue: public TimerQueue {
public:
	HostTimerQueue(Host* h) :
		TimerQueue(h->getCommunity()), host(h) {
	}

	virtual void callback(TimerQueueData* tqd) {
		switch(((HostTimerQueueData*)tqd)->getQueueDataType()) {
		case HostTimerQueueData::START_TRAFFIC:
			host->startTraffic();
			break;
		case HostTimerQueueData::GENERIC:
			host->tmr_callback((HostTimerQueueData*) tqd);
			break;
		default:
			LOG_ERROR("someone added a new host timer queue data type and forgot to update this switch!\n");
		}
	}

private:
	Host* host;
};

Host::Host(): start_traffics(StartTrafficEventComparison(true)) {
	this->community=0;
}

Host::~Host() {
	unshared.ip2iface.clear();

	if (unshared.tmr_queue) {
		delete unshared.tmr_queue;
		delete unshared.tmr_map;
	}

	if (unshared.rng)
		delete unshared.rng;

	// reclaim all interfaces
	ChildIterator<Interface*> si = nics();
	while (si.hasMoreElements())
		delete si.nextElement();

	// the base class (ProtocolGraph) will reclaim all protocol sessions
}

void Host::initStateMap() {
	ProtocolGraph::initStateMap();
	unshared.tmr_queue=0;
	unshared.rng=0;
}

void Host::init() {
	if(getCommunity() == 0) {
		LOG_ERROR("The community for name="<<getName()<<", uid="<<getUID()<<", uname="<<getUName()<<" was not set!"<<endl);
	}
	// the base class will call the init method of all protocol sessions
	ProtocolGraph::init();

	unshared.portals= new PortalIfaceTable();
	// call the init method of all interfaces and create mappings
	unshared.ip2iface.clear();
	ChildIterator<Interface*> si = nics();
	unshared.traffic_intensity.write(0.0);
	while (si.hasMoreElements()) {
		Interface* nic = si.nextElement();
		nic->init();

		IPAddress nicip = nic->getIP();
		if (nicip != IPAddress::IPADDR_INVALID) {
			unshared.ip2iface.insert(SSFNET_MAKE_PAIR((uint32)nicip, nic));
		}
	}

	// host_seed is zero if all sessions share the same unshared.rng; otherwise,
	// each session has its own unshared.rng (with a seed calculated from the
	// host_seed initiated in the following).
	int seed;
	IPAddress ipaddr = getDefaultIP();
	if (ipaddr == IPAddress::IPADDR_INVALID)
		seed = (int) getUID();
	else
		seed = int(ipaddr);
	unshared.rng_seed = shared.host_seed.read();
	if (unshared.rng_seed)
		unshared.rng_seed += 13 * (17 + seed);

	// seed the random stream by the default ip address
	unshared.rng = new prime::rng::Random(prime::rng::Random::USE_RNG_LEHMER, seed);
	unshared.rng->uniform(); // we must scramble the random numbers!!!

	IPv4Session* ip= NULL;
	ForwardingEngine* fwd_engine=NULL;
	ChildIterator<ProtocolSession*> sessions = cfg_sess();
	while (sessions.hasMoreElements()) {
		ProtocolSession* s= sessions.nextElement();
		if(RoutingProtocol::getClassConfigType()->isSubtype(s->getConfigType())) {
			if(fwd_engine) {
				LOG_WARN("Found multiple routing protocols that provide forwarding engines in host "<<getUName()<<endl)
			}
			else {
				fwd_engine=SSFNET_DYNAMIC_CAST(RoutingProtocol*,s)->getForwardingEngine();
				break;
			}
		}
		else if(IPv4Session::getClassConfigType()->isSubtype(s->getConfigType())) {
			ip=SSFNET_DYNAMIC_CAST(IPv4Session*,s);
		}
	}
	if(!fwd_engine) {
		//find the routing sphere that owns this host
		BaseEntity* cur=getParent();
		while(cur!=NULL) {
			if(cur->isRoutingSphere()) {
				fwd_engine=SSFNET_DYNAMIC_CAST(Net*,cur)->getRoutingSphere();
				break;
			}
			cur=cur->getParent();
		}
		if(!fwd_engine) {
			LOG_WARN("Unable to find a forwarding engine for "<<getUName()<<endl);
			DEBUG_CODE(
					cur=getParent();
			while(cur!=NULL) {
				Net * n = SSFNET_DYNAMIC_CAST(Net*,cur);
				RoutingSphere * rs = n->getRoutingSphere();
				LOG_WARN("\t"<<n->getUName()<<" is not a routing sphere! rs_type="<<rs->getConfigType()->getName()<<", has_routes="<<rs->hasRoutes()<<", rt="<<rs->getRouteTable()->getTypeName()<<endl)
				cur=cur->getParent();
			}
			);
		}
	}

	if(!ip) {
		ip = SSFNET_DYNAMIC_CAST(IPv4Session*,this->sessionForNumber(SSFNET_PROTOCOL_TYPE_IPV4));
		//ip = SSFNET_DYNAMIC_CAST(IPv4Session*,this->sessionForNumber(convert_typeid_to_protonum(SSFNET_PROTOCOL_TYPE_IPV4)));
	}
	if(!ip) {
		LOG_ERROR("Unable to find the IPv4 session for "<<getUName()<<endl)
	}
	else {
		ip->setForwardingEngine(fwd_engine);
	}
	startedStatCollected=false;
}

void Host::wrapup() {
	// the base class will call the wrapup method of all protocol sessions
	ProtocolGraph::wrapup();

	// call the wrapup method of all interfaces
	ChildIterator<Interface*> si = nics();
	while (si.hasMoreElements())
		si.nextElement()->wrapup();
}

void Host::registerTrafficPortal(TrafficPortal* portal) {
	for(IPPrefix::List::iterator i=portal->getNetworks().begin(); i!=portal->getNetworks().end(); i++) {
		unshared.portals->addPortal(*i, portal->getInterface());
	}
}

Interface* Host::getTrafficPortal(uint32 ip) {
	if(unshared.portals) {
		TrafficPortalInterface* rv=unshared.portals->getPortal(ip);
		if(rv)
			return rv->iface;
	}
	return 0;
}


Net* Host::inNet() {
	return SSFNET_STATIC_CAST(Net*,getParent());
}

prime::rng::Random* Host::getRandom() {
	return unshared.rng;
}

int Host::getHostSeed() {
	return unshared.rng_seed;
}

Community* Host::getCommunity() {
	return this->community;
}

void Host::setCommunity(Community* _community) {
	this->community = _community;
}

ChildIterator<Interface*> Host::getInterfaces() {
	return nics();
}

Interface* Host::getInterfaceByIP(IPAddress ip) {
	IP2IFACE_MAP::iterator iter = unshared.ip2iface.find((uint32) ip);
	if (iter != unshared.ip2iface.end()){
		//LOG_DEBUG("ip="<<ip<<", iface="<<iter->second->getUName()<<endl)
		return (*iter).second;
	}
	/*	else{
		LOG_DEBUG("host "<<getUName()<<" does not have ip "<<ip<<"\n\tvalid ips:"<<endl)
		for(iter=unshared.ip2iface.begin();iter!=unshared.ip2iface.end();iter++) {
			LOG_DEBUG("  ip="<<iter->second->getIP()<<endl)
		}
	}
	 */
	return 0;
}

IPAddress Host::getDefaultIP() {
	if (unshared.default_ip == IPAddress::IPADDR_INVALID) {
		ChildIterator<Interface*> si = nics();
		while (si.hasMoreElements()) {
			Interface* iface = si.nextElement();
			if (iface == NULL)
				LOG_ERROR("the iface was null -- should never see this!"<<endl)
				IPAddress nicip = iface->getIP();
			if (nicip != IPAddress::IPADDR_INVALID) {
				unshared.default_ip = nicip;
				break;
			}
		}
	}
	return unshared.default_ip;
}
MACAddress Host::getDefaultMAC() {
	if (unshared.default_ip == IPAddress::IPADDR_INVALID) {
		getDefaultIP();
	}
	ChildIterator<Interface*> si = nics();
	while (si.hasMoreElements()) {
		Interface* iface = si.nextElement();
		if (iface == NULL) {
			LOG_ERROR("the iface was null -- should never see this!"<<endl)
		}
		if(iface->getIP() == unshared.default_ip)
			return iface->getMAC();
	}
	return MACAddress::MACADDR_INVALID;
}

void Host::handleStartTrafficEvent(StartTrafficEvent* evt){
	//We need a priority queue of start traffic events, each event could technically be a different traffic type.
	//Make sure the timer is set for the minimum start time.
	VirtualTime old_min=VirtualTime(0);
	if(!start_traffics.empty()) {
		old_min=start_traffics.top()->getStartTime();
	}
	if(evt->getDstUID()==0 && evt->getDstIP()==0){
		//This is the cnf traffic evt, set controller to be the dst
		//Get controller uid for my network
		Net* owning_net = inNet();
		UID_t rid = owning_net->getControllerRid();
		while (rid == 0 && owning_net != NULL) {
			owning_net = owning_net->getSuperNet();
			rid = owning_net->getControllerRid();
		}
		UID_t controller_uid = rid + owning_net->getUID()-owning_net->getSize();
		evt->setDstUID(controller_uid);
	}
	start_traffics.push(evt);
	evt=start_traffics.top();
	//LOG_DEBUG("the event on the top of start_traffics is:"<<evt<<endl)
	//LOG_DEBUG("The start time is: "<<evt->getStartTime().second()<<", now="<<VirtualTime(prime::ssf::now())<<",oldmin="<<old_min.second() <<endl)
	if(old_min>evt->getStartTime().second()) {
		//LOG_DEBUG("the event is the new min"<<endl);
		//the timer was previously set to a time too far in the future....
		if(evt->getStartTime()<VirtualTime(prime::ssf::now())) {
			startTraffic();
		}
		else {
			scheduleTrafficStart(evt->getStartTime()-VirtualTime(prime::ssf::now()));
		}
	}
	else if(old_min.second()==0) {
		//the timer wasn't setup yet....
		VirtualTime t = evt->getStartTime()-VirtualTime(prime::ssf::now());
		if(t<0)t=0;
		//LOG_DEBUG("the event is being recalled at time "<<t<<endl);
		scheduleTrafficStart(t);
	}
	//else the evt we inserted was not the new min....the timer is okay
}

void Host::startTraffic(){
	//This method will process all traffic events with the start time less than or equal to the current time.
	//then reschedule the timer for the next minimum start time.
	StartTrafficEvent* evt=NULL;
	VirtualTime cur=this->getNow();
	//LOG_DEBUG("Start traffic..."<<endl)
	while(!start_traffics.empty()) {
		//LOG_DEBUG("cur time="<<cur.second()<<endl)
		evt=start_traffics.top();
		//LOG_DEBUG("host start traffic evt="<<evt<<" at start time="<<evt->getStartTime().second()
		//<<", target community id="<<evt->getTargetCommunity()<<", src comm id="<<evt->getSrcCommunityID()<<", evt="<<evt<<endl)
		if(cur>=evt->getStartTime()) {
			start_traffics.pop();
			//find the correct protocol session....
			//LOG_DEBUG("finding the correct protocol session"<<endl)
			ProtocolSession* psess = this->sessionForNumber(convert_typeid_to_protonum(evt->getTrafficType()->getProtocolType()->type_id));
			if(!psess) {
				LOG_WARN("Unable to find or create protocol session of type "<<evt->getTrafficType()->getProtocolType()->getName()<<endl);
				delete evt;
			}
			else {
				psess->startTraffic(evt);
			}
		}
		else {
			break;
		}
	}
	if(start_traffics.size()>0) {
		evt=start_traffics.top();
		//LOG_DEBUG("event is:"<<evt<<endl)
		//LOG_DEBUG("The start time is: "<<evt->getStartTime().second()<<endl)
		if(evt->getStartTime()<getCommunity()->now()) {
			LOG_ERROR("DIDNT EXPECT THIS!!!"<<endl);
		}
		else {
			scheduleTrafficStart(evt->getStartTime()-VirtualTime(prime::ssf::now()));
		}
	}
}

void Host::sentFinishedTrafficEvent(FinishedTrafficEvent* evt){
	//This method is called by a protocol session to indicate a particular traffic is finished.
	getCommunity()->deliverEvent(evt);
}

VirtualTime Host::getNow() {
	if(!getCommunity()->init_called() || getCommunity()->wrapup_called())
		ssfnet_throw_exception(SSFNetException::runtime_getnow);
	return absoluteTimeToHostTime(getCommunity()->now());
}

VirtualTime Host::absoluteTimeToHostTime(VirtualTime x) {
	return shared.time_skew.read() * x + shared.time_offset.read();
}

VirtualTime Host::hostTimeToAbsoluteTime(VirtualTime x) {
	return (x - shared.time_offset.read()) / shared.time_skew.read();
}

int Host::setTimer(ProtocolSession* sess, VirtualTime duration, void* extra) {
	if (duration < 0)
		LOG_ERROR("set timer with negative duration: " <<
				duration.second() << ".");
	return setTimerUntil(sess, getNow() + duration, extra);
}

void Host::scheduleTrafficStart(VirtualTime time) {
	VirtualTime now=getNow();
	time+=now;
	if (!unshared.tmr_queue) {
		unshared.tmr_queue = new HostTimerQueue(this);
		unshared.tmr_map = new SSFNET_INT2PTR_MAP;
		unshared.tmr_handle = 0;
	}
	int handle = ++(unshared.tmr_handle);
	HostTimerQueueData* tqd = new StartTrafficTimerData(hostTimeToAbsoluteTime(time),handle);
	unshared.tmr_queue->insert(tqd);
}

int Host::setTimerUntil(ProtocolSession* sess, VirtualTime time, void* extra) {
	if (time < getNow())
		LOG_ERROR("set timer in the past: time=" << time.second() <<
				", now=" << getNow().second() << ".");

	if (!unshared.tmr_queue) {
		unshared.tmr_queue = new HostTimerQueue(this);
		unshared.tmr_map = new SSFNET_INT2PTR_MAP;
		unshared.tmr_handle = 0;
	}
	int handle = ++(unshared.tmr_handle);
	HostTimerQueueData* tqd = new HostTimerQueueData(sess, handle, time, extra);
	unshared.tmr_queue->insert(tqd);
	unshared.tmr_map->insert(SSFNET_MAKE_PAIR(handle, tqd));
	return handle;
}

bool Host::cancelTimer(ProtocolSession* sess, int handle, void** extra) {
	if (!unshared.tmr_queue)
		return false;
	SSFNET_INT2PTR_MAP::iterator iter = unshared.tmr_map->find(handle);
	if (iter == unshared.tmr_map->end())
		return false;
	HostTimerQueueData* tqd = (HostTimerQueueData*) iter->second;
	unshared.tmr_queue->remove(tqd);
	unshared.tmr_map->erase(iter);
	if (extra)
		*extra = tqd->getExtra();
	else if (tqd->getExtra())
		LOG_ERROR("Memory leak due to timer extra field.");
	delete tqd;
	return true;
}

void Host::tmr_callback(HostTimerQueueData* tqd) {
	assert(unshared.tmr_queue);
	SSFNET_INT2PTR_MAP::iterator iter = unshared.tmr_map->find(tqd->getHandle());
	assert(iter != unshared.tmr_map->end());
	unshared.tmr_map->erase(iter);
	tqd->getSession()->timerCallback(tqd->getHandle(), tqd->getExtra());
	delete tqd;
}


void Host::exportState(StateLogger* state_logger, double sampleInterval) {
	ChildIterator<Interface*> si = nics();
	Interface* nic=0;
	while (si.hasMoreElements()) {
		nic= si.nextElement();
		nic->exportState(state_logger,sampleInterval);
	}
}

void Host::exportVizState(StateLogger* state_logger, double sampleInterval){
	//std::cout<<"\t\tstaart export viz state for "<<getUName()<<", uid="<<getUID()<<", logger="<<((void*)state_logger)<<endl;
	double max_intensity=0,sent=0,temp_rate=0;

	ChildIterator<Interface*> si = nics();
	Interface* nic=0;
	while (si.hasMoreElements()) {
		nic= si.nextElement();
		nic->exportVizState(state_logger,sampleInterval,sent);
		temp_rate = sent/nic->getBitRate();
		if(temp_rate>max_intensity)max_intensity=temp_rate;
	}
	unshared.traffic_intensity.write(max_intensity>1.0?1.0:max_intensity);
	if(state_logger) {
		StateUpdateEvent * evt = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT,getNow(),getUID(),true, false);
		evt->addVarUpdate(unshared.traffic_intensity.getVarType()->getAttrNameId(),unshared.traffic_intensity.read());
		state_logger->export_state(evt);
	}
	//std::cout<<"\t\tend export viz state for "<<getUName()<<", uid="<<getUID()<<", logger="<<((void*)state_logger)<<endl;
};


void Host::sendTimeExceededMsg(IPv4Message* ipmsg, IPAddress ipaddr) {
	if(this->nicsSize()==1) {
		//end hosts do not need to send these back
		LOG_WARN("Dropping pkt since the ttl was set to 0!"<<endl);
	}
	else {
		LOG_DEBUG("[debug traceroute]\t before sendTimeExceededMsg in icmpv4 ipaddr=" << ipaddr << endl);
		LOG_DEBUG("SENDING makeTimeExceededMsg since the ttl was set to 0! ["<<ipmsg->getSrc()<<
				","<<ipmsg->getDst()<<"] iface="<<ipaddr<<endl);
		//ipmsg->setDst(ipaddr);
		SSFNET_DYNAMIC_CAST(ICMPv4Session*,
				this->sessionForNumber(SSFNET_PROTOCOL_TYPE_ICMPV4))->sendTimeExceededMsg(ipaddr, ipmsg);
	}
	ipmsg->erase();
}

Router::Router() {

}

Router::~Router() {
}

} // namespace ssfnet
} // namespace prime
