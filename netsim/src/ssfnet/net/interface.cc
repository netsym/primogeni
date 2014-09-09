/**
 * \file interface.cc
 * \brief Source file for the Interface class.
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

#include "os/logger.h"
#include "os/state_logger.h"
#include "os/config_factory.h"
#include "os/community.h"
#include "os/packet_event.h"
#include "os/protocol_session.h"
#include "os/partition.h"
#include "net/host.h"
#include "net/link.h"
#include "net/interface.h"
#include "proto/emu/emulation_protocol.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(Interface);

/**
 * Used to preform async lookups
 *
 */
class InterfaceNameResolutionCallBack: public NameResolutionCallBackWrapper {
public:
	InterfaceNameResolutionCallBack(Interface* _iface, Packet* _pkt, VirtualTime _delay):
		iface(_iface), pkt(_pkt), delay(_delay), now(iface->inHost()->getCommunity()->now())
	{
	}

	virtual ~InterfaceNameResolutionCallBack() {
	}

	/**
	 * called when a mac or ip is searched for
	 */
	virtual void call_back(UID_t uid) {
		VirtualTime n =iface->inHost()->getCommunity()->now()-now;
		if(n >= delay)
			iface->transmit(pkt, 0, uid);
		else
			iface->transmit(pkt, delay-n, uid);
	}

	/**
	 * called when a uid is searched for
	 */
	virtual void call_back(IPAddress ip, MACAddress mac) {
		LOG_ERROR("InterfaceNameResolutionCallBack::call_back(IPAddress ip, MACAddress mac) should never be called!"<<endl)
	}

	/*
	 * called when the uid/mac/ip can't be found
	 */
	virtual void invalid_query() {
		DEBUG_CODE(
			IPv4Message* ipmsg = SSFNET_DYNAMIC_CAST(IPv4Message*,pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4));
			LOG_WARN("Unable to route to ip "<<ipmsg->getDst()<<", the IP appears to be invalid!"<<endl);
		);
		pkt->erase();
		pkt=0;
		iface=0;
	}

private:
	Interface* iface;
	Packet* pkt;
	VirtualTime delay, now;
};


// ****************************************
// ************** BaseInterface ***********
// ****************************************

BaseInterface::BaseInterface() {
}

BaseInterface::~BaseInterface() {
}

void BaseInterface::initStateMap() {
	BaseEntity::initStateMap();
	unshared.attached_link=0;
}


/** Initialize this interface. */
void BaseInterface::init() {
	BaseEntity::init();
}

Link* BaseInterface::getLink() {
	return unshared.attached_link;
}

void BaseInterface::setLink(Link* l) {
	unshared.attached_link=l;
}

const IPAddress& BaseInterface::getIP() {
	return unshared.ip_address.read();
}

const MACAddress& BaseInterface::getMAC() {
	return unshared.mac_address.read();
}

bool BaseInterface::isBroadcastAddress(MACAddress mac) {
	return MACAddress::MACADDR_BROADCAST_11==mac;
}
bool BaseInterface::isIPBroadcastAddress(IPAddress dst_ip) {
	if (unshared.attached_link) {
		uint32 mask =  IPPrefix::getMask(
				unshared.attached_link->getIPPrefixLen());
		if((uint32(getIP())&mask)==(uint32(dst_ip)&mask)) {
			//they are in the same network
			DEBUG_CODE(
			if(((uint32(dst_ip) | ~mask) == uint32(dst_ip))) {
				LOG_WARN("isIPBroadcastAddress, nic_ip="<<getIP()
					<<", dst_ip="<<dst_ip
					<<", link="<<unshared.attached_link->getUName()
					<<", prefix="<<unshared.attached_link->getIPPrefix()
					<<", mask="<<mask
					<<", prefix len="<<((uint32)unshared.attached_link->getIPPrefixLen())
					<<", uint32(dst_ip)="<<uint32(dst_ip)
					<<", (uint32(dst_ip) | ~mask)="<<(uint32)(uint32(dst_ip) | ~mask)
					<<endl);
			});
			return ((uint32(dst_ip) | ~mask) == uint32(dst_ip));
		}
	}
	return false;
}

bool BaseInterface::isMulticastAddress(IPAddress ip) {
	return (uint32(ip) & 0xff000000) == 0xe0000000; // 224.0.0.0/4
}

// ****************************************
// ************** GhostInterface **********
// ****************************************

UID_t GhostInterface::getRemoteCommunityId() {
	return unshared.community_id.read();
}

UID_t GhostInterface::getUIDOfRealInterface() {
	return unshared.real_uid.read();
}


// ****************************************
// *************** Interface **************
// ****************************************


TxTimerQueueData::TxTimerQueueData(Packet* p, Interface* nic, VirtualTime time) :
			TimerQueueData(nic->inHost()->hostTimeToAbsoluteTime(time)), pkt(p),
			iface(nic) {
}

TxTimerQueueData::~TxTimerQueueData() {
	if (pkt)
		pkt->erase();
}

TxTimerQueue::TxTimerQueue(Community* comm) :
			TimerQueue(comm) {
}

TxTimerQueue::~TxTimerQueue() {

}

void TxTimerQueue::callback(TimerQueueData *timerData) {
	TxTimerQueueData* data = SSFNET_DYNAMIC_CAST(TxTimerQueueData*,timerData);
	assert(data);
	Packet* pkt = data->pkt;
	data->pkt = 0; // take the pkt off the data
	data->iface->tx_packet_callback(pkt);
	delete data;
}

Interface::Interface() {
}

Interface::~Interface() {
	// reclaim the nic queue
	if (unshared.tx_queue)
		delete unshared.tx_queue;

	// reclaim the transmission timer queue
	if (unshared.tx_timer_queue) {
		unshared.tx_timer_queue->clearQueue(true); // forcing it to release memory
		delete unshared.tx_timer_queue;
	}
}

void Interface::initStateMap() {
	BaseInterface::initStateMap();
	unshared.emu_protocol=0;
	unshared.tx_queue=0;
	unshared.tx_timer_queue=0;
	unshared.ip_sess=0;
}

void Interface::init() {
	BaseInterface::init();

	unshared.tx_timer_queue = new TxTimerQueue(inHost()->getCommunity());
	assert(unshared.tx_timer_queue);

	unshared.ip_sess = SSFNET_STATIC_CAST(IPv4Session*,inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_IPV4));
	if (!unshared.ip_sess)
		LOG_ERROR("missing ip protocol session.\n");

	unshared.mac_address.read()=MACAddress(getUID());

	ChildIterator<NicQueue*> r = nic_queue();
	if (r.hasMoreElements()) {
		unshared.tx_queue = r.nextElement();
	}
	else {
		unshared.tx_queue = ConfigurableFactory::createInstance<NicQueue*>(shared.queue_type.read().c_str());
		assert(unshared.tx_queue);
		addChild(unshared.tx_queue);
	}
	ChildIterator<EmulationProtocol*> e = emu_proto();
	if (e.hasMoreElements()) {
		unshared.emu_protocol = e.nextElement();
	}
	if(unshared.emu_protocol) {
		unshared.emu_protocol->init();
	}
	assert(unshared.tx_queue);
	unshared.tx_queue->init();

	unshared.num_in_bytes.write(0);
	unshared.num_out_bytes.write(0);
	unshared.num_in_packets.write(0);
	unshared.num_out_packets.write(0);
	unshared.packets_in_per_sec.write(0.0);
	unshared.packets_out_per_sec.write(0.0);
	unshared.bytes_in_per_sec.write(0.0);
	unshared.bytes_out_per_sec.write(0.0);
	unshared.queue_size.write(0);
}

void Interface::wrapup() {
	BaseInterface::wrapup();
	unshared.tx_queue->wrapup();
}

EmulationProtocol* Interface::getEmulationProtocol() {
	return unshared.emu_protocol;
}

Host* Interface::inHost() {
	return SSFNET_STATIC_CAST(Host*,getParent());
}

float Interface::getBitRate() {
	// get min between nic bit rate and link bandwidth
	float x = shared.bit_rate.read();
	if (unshared.attached_link) {
		float y = unshared.attached_link->getBandwidth();
		if (x < y ) {
			return x;
		}
		return y;
	}
	return x;
}

float Interface::getLatency() {
	return shared.latency.read();
}

float Interface::getJitterRange() {
	return shared.jitter_range.read();
}

float Interface::getDropProbability() {
	return unshared.drop_probability.read();
}

int Interface::getBufferSize() {
	return shared.buffer_size.read();
}

int Interface::getBytesSent() {
	return unshared.num_out_bytes.read();
}

int Interface::getBytesReceived() {
	return unshared.num_in_bytes.read();
}
int Interface::getMTU() {
	return shared.mtu.read();
}

NicQueue* Interface::getNicQueue(){
	return unshared.tx_queue;
}

bool Interface::isOn() {
	return unshared.is_on.read();
}

void Interface::switchOnOff(bool on_off) {
	for(SphereComList::iterator i = concerned_spheres.begin();i!=concerned_spheres.end();i++) {
		inHost()->getCommunity()->deliverEvent(
				new RoutingStatusResponse(
						0,
						inHost()->getCommunity()->getCommunityId(),
						inHost()->getCommunity()->getCommunityId(),
						getUID(),
						on_off)
		);
	}
	unshared.is_on.write(on_off);
}

IPv4Session* Interface::getIPv4Session() {
	return unshared.ip_sess;
}

bool Interface::send(IPv4Message* ipmsg, bool cannot_drop) {
	//LOG_DEBUG("["<<getUName()<<"]Interface (ip=" << getIP() << ", mac="<<getMAC()<<", uid="<<getUID()<<") sends a packet [ttl="<<(int)(ipmsg->getTTL())<<"]"<<endl);
	//LOG_DEBUG("The current time is "<<current_time()<<endl);
	if (!unshared.emu_protocol && (!isOn() || !unshared.attached_link)) {
		if(!isOn()) {
			LOG_WARN("Dropping pkt since iface is not on. link="<<getUName()<<endl);
		}
		else {
			LOG_WARN("Dropping pkt since iface is not connected to a link. link="<<getUName()<<endl);
		}
		ipmsg->erase();
		return true;
	}
	Packet* pkt = ipmsg->getPacket();
	if (pkt) {
		pkt->setSrc(getMAC());
	} else {
		pkt = new Packet(getMAC(), MACAddress::MACADDR_INVALID, ipmsg);
	}

#if TEST_ROUTING == 1
	pkt->route_debug->hops.push_back(RouteHop(true, inHost()->getUID(), inHost()->getCommunity()->now()));
#endif
	return !unshared.tx_queue->enqueue(pkt, this->getDropProbability(), cannot_drop);
}

void Interface::sendProbe(IPv4Message* ipmsg) {
	LOG_DEBUG("["<<getUName()<<"]Interface (ip=" << getIP() << ", mac="<<getMAC()<<", uid="<<getUID()<<") sends a probe packet [ttl="<<(int)(ipmsg->getTTL())<<"]"<<endl);
	if (!isOn() || !unshared.attached_link) {
		if(!isOn()) {
			LOG_WARN("Dropping pkt since iface is not on. link="<<getUName()<<endl);
		}
		else {
			LOG_WARN("Dropping pkt since iface is not connected to a link. link="<<getUName()<<endl);
		}
		ipmsg->erase();
		return;
	}
	Packet* pkt = ipmsg->getPacket();
	if (pkt) {
		pkt->setSrc(getMAC());
	} else {
		LOG_ERROR("How did this happen?");
	}
#if TEST_ROUTING == 1
	pkt->route_debug->hops.push_back(RouteHop(true, inHost()->getUID(), inHost()->getCommunity()->now()));
#endif
	//we are being tricky here so this packet avoids the queue and cannot be dropped!

	{ // collect statistics
		uint x = unshared.num_out_packets.read();
		unshared.num_out_packets.write(x + 1);
		x = unshared.num_out_bytes.read();
		unshared.num_out_bytes.write(x + pkt->size());
	}
	ProbeMessage* probe = dynamic_cast<ProbeMessage*>(ipmsg->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_PROBE));
	LinkInfo* link_info=getLink()->getLinkInfo(inHost()->getCommunity());
	LinkInfo::RemoteIface** remotes=link_info->getRemoteInterfaces();
	if(pkt->getDst() == MACAddress::MACADDR_INVALID) {
		//LOG_DEBUG("\t["<< inHost()->getNow()<<"] find mac"<<endl)
		BaseInterface* nexthop=NULL;
		int remote_idx=-1;

		int16 bus_idx=getLink()->getNextHop(this, pkt,inHost()->getCommunity(),nexthop,remote_idx);
		if(!nexthop && remote_idx==-1) {
			//LOG_DEBUG("\tdoing routing...."<<endl)
			//lets try to route it.....
			if(!pkt->getFinalDestinationUID()) {
				LOG_ERROR("Should never see this!"<<endl);
			}
			Interface* nic=NULL;
			ForwardingEngine* fr_engine = getIPv4Session()->getForwardingEngine();
			if (fr_engine->getOutboundIface(SSFNET_DYNAMIC_CAST(Host*,getIPv4Session()->getParent()),
					pkt, nic)) {
				//XXX look at this logic later... seems sketchy to nate
				bus_idx = getLink()->getNextHop(this, pkt,inHost()->getCommunity(),nexthop,remote_idx);
			}
		}
		if(probe) {
			if(bus_idx>0){
				probe->addBusIdxes(bus_idx);
			}else{
				probe->addBusIdxes(0);
			}
		}
		if(nexthop) {
			//LOG_DEBUG("next hop is local"<<endl);
			pkt->setDst(nexthop->getMAC());
			DEBUG_CODE(
					Host* h = SSFNET_DYNAMIC_CAST(Host*,nexthop->deference()->getParent());
					if(inHost()->getCommunity() != h->getCommunity()) {
						LOG_ERROR("How did this happen?\n");
					}
			);

		}
		else if(remote_idx>=0){
			VirtualTime d = remotes[remote_idx]->delay;
			LOG_INFO("["<<getUName()<<"]Sending packet to remote community "
					<<remotes[remote_idx]->remote_com_id<<", part="
					<<Partition::getInstance()->communityId2partitionId(remotes[remote_idx]->remote_com_id)
					<<", uid="<<remotes[remote_idx]->iface->getUIDOfRealInterface()
					<<", delay="<<d
					<<", srcpart="<<inHost()->getCommunity()->getPartition()->getPartitionId()
					<<endl)
			remotes[remote_idx]->oc->write(new PacketEvent(pkt,remotes[remote_idx]->remote_com_id,remotes[remote_idx]->iface->getUIDOfRealInterface()),d);
			return;
		}
		else {
			LOG_ERROR("interface received a probe msg but was unable to find the next hop in the attached link!"<<endl);
		}
	}
	else {
		LOG_ERROR("Should never see this!"<<endl);
	}
	//must be local if it got here.....
	//lets do a sanity check!

	//LOG_DEBUG("Getting interface with mac "<<pkt->getDst()<<endl)
	BaseInterface* nic = unshared.attached_link->getInterfaceByMAC(pkt->getDst());
	if (!nic) {
		LOG_WARN("rogue routing: nexthop " << pkt->getDst() << " not found"<<endl);
		pkt->erase();
		return;
	}
	else if(nic==this) {
		LOG_WARN("rogue routing: nexthop (" << pkt->getDst() << ") is the same as the src iface("<<getMAC()<<")!"<<endl);
		pkt->erase();
		return;
	}
	DEBUG_CODE(
			Host* h = SSFNET_DYNAMIC_CAST(Host*,nic->deference()->getParent());
			if(inHost()->getCommunity() != h->getCommunity()) {
				LOG_ERROR("How did this happen?\n");
			}
	);

	tx_packet_callback(pkt);
}

void Interface::enqueueEmuPkt(Packet* pkt, VirtualTime deficit){
	IPv4Message* msg = SSFNET_DYNAMIC_CAST(IPv4Message*,pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4));
	if(msg) {
		msg->decrementTTL(1);
	}
	unshared.tx_queue->enqueue(pkt, this->getDropProbability());
}


void Interface::exportState(StateLogger* state_logger, double sampleInterval) {
	StateUpdateEvent * evt = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT,inHost()->getNow(),getUID(), false, false);
	evt->addVarUpdate(unshared.num_in_packets.getVarType()->getAttrNameId(),(int32_t)unshared.num_in_packets.read());
	evt->addVarUpdate(unshared.num_out_packets.getVarType()->getAttrNameId(),(int32_t)unshared.num_out_packets.read());
	evt->addVarUpdate(unshared.num_in_bytes.getVarType()->getAttrNameId(),(int32_t)unshared.num_in_bytes.read());
	evt->addVarUpdate(unshared.num_out_bytes.getVarType()->getAttrNameId(),(int32_t)unshared.num_out_bytes.read());
	state_logger->export_state(evt);
}
void Interface::exportVizState(StateLogger* state_logger, double sampleInterval, double& sent) {
	sent = (8.0*(unshared.num_out_bytes.read() - unshared.bytes_out_per_sec.read()))/sampleInterval;
	exportVizState(state_logger,sampleInterval);
}

void Interface::exportVizState(StateLogger* state_logger, double sampleInterval){
	//std::cout<<"\t\t\tstaart export viz state for "<<getUName()<<", uid="<<getUID()<<", logger="<<((void*)state_logger)<<endl;
	StateUpdateEvent * evt = 0;
	if(state_logger) {
		evt = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT,inHost()->getNow(),getUID(),true, false);
		evt->addVarUpdate(unshared.num_in_packets.getVarType()->getAttrNameId(),(int32_t)unshared.num_in_packets.read());
		evt->addVarUpdate(unshared.num_out_packets.getVarType()->getAttrNameId(),(int32_t)unshared.num_out_packets.read());
		evt->addVarUpdate(unshared.num_in_bytes.getVarType()->getAttrNameId(),(int32_t)unshared.num_in_bytes.read());
		evt->addVarUpdate(unshared.num_out_bytes.getVarType()->getAttrNameId(),(int32_t)unshared.num_out_bytes.read());

		evt->addVarUpdate(unshared.packets_out_per_sec.getVarType()->getAttrNameId(),
				((double)unshared.num_out_packets.read()-unshared.packets_out_per_sec.read())/sampleInterval);

		evt->addVarUpdate(unshared.bytes_out_per_sec.getVarType()->getAttrNameId(),
				((double)unshared.num_out_bytes.read()-unshared.bytes_out_per_sec.read())/sampleInterval);

		evt->addVarUpdate(unshared.packets_in_per_sec.getVarType()->getAttrNameId(),
				((double)unshared.num_in_packets.read()-unshared.packets_in_per_sec.read())/sampleInterval);

		evt->addVarUpdate(unshared.bytes_in_per_sec.getVarType()->getAttrNameId(),
				((double)unshared.num_in_bytes.read()-unshared.bytes_in_per_sec.read())/sampleInterval);
	}
	unshared.packets_out_per_sec.write(unshared.num_out_packets.read());
	unshared.bytes_out_per_sec.write(unshared.num_out_bytes.read());
	unshared.packets_in_per_sec.write(unshared.num_in_packets.read());
	unshared.bytes_in_per_sec.write(unshared.num_in_bytes.read());
	if(state_logger) {
		state_logger->export_state(evt);
	}
	//std::cout<<"\t\t\tend export viz state for "<<getUName()<<", uid="<<getUID()<<", logger="<<((void*)state_logger)<<endl;
};


void Interface::receiveEmuPkt(Packet* pkt) {
	if (!isOn()) {
		LOG_DEBUG("Dropping pkt since iface is not on."<<endl);
#if TEST_ROUTING == 1
		saveRouteDebug(pkt,this);
#endif
		pkt->erase();
		return;
	}

	{ // collect statistics
		uint x = unshared.num_in_packets.read();
		unshared.num_in_packets.write(x + 1);
		x = unshared.num_in_bytes.read();
		unshared.num_in_bytes.write(x + pkt->size());
		if (pkt->getDst() == getMAC()) {
			x = unshared.num_in_ucast_packets.read();
			unshared.num_in_ucast_packets.write(x + 1);
			x = unshared.num_in_ucast_bytes.read();
			unshared.num_in_ucast_bytes.write(x + pkt->size());
		}
	}

	pkt->setSrc(getMAC());
	pkt->setDst(MACAddress::MACADDR_INVALID);
#if TEST_ROUTING == 1
		pkt->route_debug->hops.push_back(RouteHop(false, inHost()->getUID(), inHost()->getCommunity()->now()));
#endif
	// we pass the packet directly to the ip session
	unshared.ip_sess->pop(pkt->getMessage(), 0, this);
}

void Interface::receive(Packet* pkt) {
	assert(unshared.attached_link);
	//LOG_DEBUG("["<<getUName()<<"]Interface (ip=" << getIP() << ", mac="<<getMAC()<<", uid="<<getUID()<<") receives a packet"
		//<<", have emu_proto="<<((unshared.emu_protocol)?"true":"false")<<", pkt->getDst()="<<pkt->getDst()<<", macs match? "
		//<< (pkt->getDst() == getMAC()?"yes":"no") <<endl);
	//LOG_DEBUG("The current time is "<<current_time()<<endl);
	if (!isOn()) {
		LOG_DEBUG("Dropping pkt since iface is not on."<<endl);
#if TEST_ROUTING == 1
		saveRouteDebug(pkt,this);
#endif
		pkt->erase();
		return;
	}

	{ // collect statistics
		uint x = unshared.num_in_packets.read();
		unshared.num_in_packets.write(x + 1);
		x = unshared.num_in_bytes.read();
		unshared.num_in_bytes.write(x + pkt->size());
		if (pkt->getDst() == getMAC()) {
			x = unshared.num_in_ucast_packets.read();
			unshared.num_in_ucast_packets.write(x + 1);
			x = unshared.num_in_ucast_bytes.read();
			unshared.num_in_ucast_bytes.write(x + pkt->size());
		}
	}

	if(!unshared.emu_protocol || !unshared.emu_protocol->receive(pkt, pkt->getDst() == getMAC())) {
		//the emulation protocol did _not_ take the packet.
		//routing using the iface the pkt entered on
		//and forwarding uses the dst mac -- if the dst mac
		//is not set, the link will try and set it.
		//LOG_DEBUG("emuproto did not take packet..."<<endl)
		pkt->setSrc(getMAC());
		pkt->setDst(MACAddress::MACADDR_INVALID);
#if TEST_ROUTING == 1
		pkt->route_debug->hops.push_back(RouteHop(false, inHost()->getUID(), inHost()->getCommunity()->now()));
#endif
		// we pass the packet directly to the ip session
		unshared.ip_sess->pop(pkt->getMessage(), 0, this);
	}
	//else {
	//	LOG_DEBUG("interface (ip=" << getIP() << ", mac="<<getMAC()<<", uid="<<getUID()<<") emu proto took packet.."<<endl)
	//}
}

void Interface::transmit(Packet* pkt, const VirtualTime& delay, UID_t callback_uid) {
	if(!unshared.attached_link) {
		//this better be an emulated iface...
		if(unshared.emu_protocol) {
			if(!unshared.emu_protocol->receive(pkt,true)) {
				LOG_WARN("Dropping a pkt tath got to an un-attached that was emulated but the emu session didn't accept the packet."<<endl);
				pkt->erase();
			}
		}
		else {
			LOG_WARN("Somehow a pkt got to an un-attached interface which was not emulated!"<<endl);
			pkt->erase();
		}
		return;
	}
	//LOG_DEBUG("["<< inHost()->getNow()<<"]interface::transmit (ip=" << getIP() << ", mac="<<getMAC()<<") "
	//	"transmit a packet, dstmac="<<pkt->getDst()<<", delay="<<delay<<", attached link="<<unshared.attached_link->getUName()<<endl);
	if (!isOn()) {
		LOG_DEBUG("Dropping pkt since iface is not on."<<endl);
		pkt->erase();
		return;
	}

	{ // collect statistics
		uint x = unshared.num_out_packets.read();
		unshared.num_out_packets.write(x + 1);
		x = unshared.num_out_bytes.read();
		unshared.num_out_bytes.write(x + pkt->size());
	}
	LinkInfo* link_info=getLink()->getLinkInfo(inHost()->getCommunity());
	LinkInfo::RemoteIface** remotes=link_info->getRemoteInterfaces();
	if(pkt->getDst() == MACAddress::MACADDR_INVALID) {
		//LOG_DEBUG("\t["<< inHost()->getNow()<<"] find mac"<<endl)
		BaseInterface* nexthop=NULL;
		int remote_idx=-1;
		getLink()->getNextHop(this, pkt,inHost()->getCommunity(),nexthop,remote_idx);
		if(!nexthop && remote_idx==-1) {
			//LOG_DEBUG("\tdoing routing...."<<endl)
			//lets try to route it.....
			if(!pkt->getFinalDestinationUID()) {
				//need to look up the dst
				IPv4Message* ipm = SSFNET_DYNAMIC_CAST(IPv4Message*,pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4));
				pkt->setFinalDestinationUID(getIPv4Session()->getCommunity()->synchronousNameResolution(ipm->getDst()));
				if(!pkt->getFinalDestinationUID()) {
					inHost()->getCommunity()->asynchronousNameResolution(ipm->getDst(),new InterfaceNameResolutionCallBack(this,pkt,delay));
					return;
				}
			}
			Interface* nic=NULL;
			ForwardingEngine* fr_engine = getIPv4Session()->getForwardingEngine();
			if (fr_engine->getOutboundIface(SSFNET_DYNAMIC_CAST(Host*,getIPv4Session()->getParent()),
					pkt, nic)) {
				//XXX look at this logic later... seems sketchy to nate
				getLink()->getNextHop(this, pkt,inHost()->getCommunity(),nexthop,remote_idx);
			}
		}
		if(nexthop) {
			//LOG_DEBUG("next hop is local"<<endl);
			pkt->setDst(nexthop->getMAC());
			DEBUG_CODE(
					Host* h = SSFNET_DYNAMIC_CAST(Host*,nexthop->deference()->getParent());
					if(inHost()->getCommunity() != h->getCommunity()) {
						LOG_ERROR("How did this happen?\n");
					}
			);
		}
		else if(remote_idx>=0){
			VirtualTime d = delay + remotes[remote_idx]->delay;
			LOG_DEBUG("["<<getUName()<<"]Sending packet to remote community "
					<<remotes[remote_idx]->remote_com_id<<", src comm="<<inHost()->getCommunity()->getCommunityId()
					<<", part="<<Partition::getInstance()->communityId2partitionId(remotes[remote_idx]->remote_com_id)
					<<", uid="<<remotes[remote_idx]->iface->getUIDOfRealInterface() <<"("<<remotes[remote_idx]->iface->getUID()<<")"
					<<", delay="<<d
					<<", srcpart="<<inHost()->getCommunity()->getPartition()->getPartitionId()
					<<endl)
			remotes[remote_idx]->oc->write(new PacketEvent(pkt,remotes[remote_idx]->remote_com_id,remotes[remote_idx]->iface->getUIDOfRealInterface()),d);
			return;
		}
		else {
			LOG_WARN("interface received an ip msg but was unable to find the next hop in the attached link! Using broadcast to find next hop instead."<<endl);
			pkt->setDst(MACAddress::MACADDR_BROADCAST_11);
		}
	}
	else {
		LOG_ERROR("Should never see this!"<<endl);
	}
	if (isBroadcastAddress(pkt->getDst())) {// || isMulticastAddress(pkt->getDst())) {
		LOG_ERROR("It looks like broadcast is broken for threads/partitions......we need to examine this\n");
		{ // collect statistics
			int x = unshared.num_out_ucast_packets.read();
			unshared.num_out_ucast_packets.write(x + 1);
			x = unshared.num_out_ucast_bytes.read();
			unshared.num_out_ucast_bytes.write(x + pkt->size());
		}
		//send to remote interfaces
		for(uint idx=0;idx<link_info->getRemoteInterfaceCount();idx++) {
			VirtualTime d = shared.latency.read() + remotes[idx]->delay;
			//remotes[idx]->oc->write(new PacketEvent(pkt,remotes[idx]->remote_com_id,remotes[idx]->iface->getUIDOfRealInterface()),d);
		}
		if(link_info->getLocalInterfaceCount()>0) {
			// compensate deficits for emulation packets (only when it's not cross-timeline)
			// recompute_emupkt_delay(pkt, delay); FIXME XXX

			//LOG_DEBUG("Queueing pkt in txqueue"<<endl);
			//LOG_DEBUG("pkt->size()="<<pkt->size()<<endl);
			TxTimerQueueData* td = new TxTimerQueueData(pkt, this, delay
					+ unshared.attached_link->getDelay() + inHost()->getNow());
			unshared.tx_timer_queue->insert(td);
		}
	}
	else {
		//must be local if it got here.....
		//LOG_DEBUG("Getting interface with mac "<<pkt->getDst()<<endl)
		BaseInterface* nic = unshared.attached_link->getInterfaceByMAC(pkt->getDst());
		//LOG_DEBUG("attached link:"<<unshared.attached_link->getUName()<<endl);
		if (!nic) {
			DEBUG_CODE(
				IPv4Message* m = SSFNET_DYNAMIC_CAST(IPv4Message*,pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4));
				LOG_WARN("rogue routing: nexthop mac " << pkt->getDst() << " not found, src="<<m->getSrc()<<", dst="<<m->getDst()<<endl);
			);
			pkt->erase();
			return;
		}
		else if(nic==this) {
			LOG_WARN("rogue routing: nexthop (" << pkt->getDst() << ") is the same as the src iface("<<getMAC()<<")!"<<endl);
			pkt->erase();
			return;
		}
		// compensate deficits for emulation packets (only when it's not cross-timeline)
		// recompute_emupkt_delay(pkt, delay); FIXME XXX

		//LOG_DEBUG("Queueing pkt in txqueue"<<endl);
		//LOG_DEBUG("pkt->size()="<<pkt->size()<<endl);
		DEBUG_CODE(
				Host* h = SSFNET_DYNAMIC_CAST(Host*,nic->deference()->getParent());
				if(inHost()->getCommunity() != h->getCommunity()) {
					LOG_ERROR("How did this happen?\n");
				}
		);
		TxTimerQueueData* td = new TxTimerQueueData(pkt, this, delay
				+ unshared.attached_link->getDelay() + inHost()->getNow());
		unshared.tx_timer_queue->insert(td);
	}
}

#if 0
void Interface::recompute_emu_pkt_delay(Packet* pkt, VirtualTime& delay)
{
	assert(unshared.attached_link);
	EmuMessage* emsg = (EmuMessage*)pkt->
			getProtocolMessageByType(SSFNET_PROTOCOL_TYPE_EMULATION);
	if(emsg && emsg->deficit > 0) {
		VirtualTime total_delay = delay + unshared.attached_link->getDelay();
		IFACE_DUMP(printf("[nhi=\"%s\", ip=\"%s\"] recompute_emu_pkt_delay(), delay=%.9lf, deficit=%.9lf.\n",
				nhi.toString(), IPPrefix::ip2txt(ip_addr),
				total_delay.second(), emsg->deficit.second()));
		if(emsg->deficit <= total_delay) {
			delay -= emsg->deficit;
			emsg->deficit = 0;
		} else {
			delay -= total_delay;
			emsg->deficit -= total_delay;
		}
		IFACE_DUMP(printf("delay will be %.9lf, deficit will be %.9lf.\n",
				(delay+unshared.attached_link->getDelay()).second(),
				emsg->deficit.second()));
	}
}
#endif

void Interface::tx_packet_callback(Packet* pkt) {
	if(pkt->getDst()==MACAddress::MACADDR_INVALID) {
		LOG_WARN("A packet with an invalid mac made it to tx_packet_callback. Dropping it."<<endl);
		pkt->erase();
		return;
	}
	if (isBroadcastAddress(pkt->getDst())){// || isMulticastAddress(pkt->getDst())) {
		LOG_ERROR("ACK!");
		LinkInfo* link_info=getLink()->getLinkInfo(inHost()->getCommunity());
		Interface** local_ifaces = link_info->getLocalInterfaces();
		int c=link_info->getLocalInterfaceCount();
		for(uint i=0;i<link_info->getLocalInterfaceCount();i++) {
			if(local_ifaces[i] != this) {
				if(c>1) {
					//we need to clone it since we will be sending more copies....
					local_ifaces[i]->receive(pkt->clone());
					c--;
				}
				else {
					//each link has at least two ifaces -- one of which is the source.
					//so if c==1 then we know this is the last pkt.
					local_ifaces[i]->receive(pkt);
				}
			}
			c--;
		}
	}
	else {
		BaseInterface* nic = unshared.attached_link->getInterfaceByMAC(pkt->getDst());
		//LOG_DEBUG("nic type="<<nic->getConfigType()->getName()<<endl)
		assert(nic && nic->getTypeId() != GhostInterface::getClassConfigTypeId());
		(SSFNET_STATIC_CAST(Interface*,nic))->receive(pkt);
	}
}

void Interface::registerConcernedSphere(int com_id, UID_t sphere_id) {
	concerned_spheres.push_back(SSFNET_MAKE_PAIR(sphere_id,com_id));
}

} // namespace ssfnet
} // namespace prime
