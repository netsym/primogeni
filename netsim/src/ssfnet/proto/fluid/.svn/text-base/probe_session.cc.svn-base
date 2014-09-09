/**
 * \file probe_session.cc
 * \brief Source file for the ProbeSession class.
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

#include "os/logger.h"
#include "net/host.h"
#include "proto/ipv4/ipv4_message.h"
#include "proto/ipv4/ipv4_session.h"
#include "proto/fluid/probe_session.h"
#include "proto/ipv4/icmp_traffic.h"
#include "proto/fluid/probe_message.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(ProbeSession);

/**
 * Used to perform async lookups
 */
class ProbeIPNameResolutionCallBack: public NameResolutionCallBackWrapper {
public:
	ProbeIPNameResolutionCallBack(ProbeSession* session_, UID_t dst_)
	: session(session_), dst(dst_)  {}

	virtual ~ProbeIPNameResolutionCallBack() {}

	/**
	 * called when a mac or ip is searched for
	 */
	virtual void call_back(UID_t uid) {
		LOG_ERROR("ProbeIPNameResolutionCallBack::call_back(UID_t uid) should never be called!"<<endl)
	}

	/**
	 * called when a uid is searched for
	 */
	virtual void call_back(IPAddress ip, MACAddress mac) {
		LOG_WARN("cur time="<<VirtualTime(prime::ssf::now()).second()<<", found ip/mac "<<ip<<","<<mac<<endl)
		session->startProbe(dst, ip);
	}

	/*
	 * called when the uid/mac/ip can't be found
	 */
	virtual void invalid_query() {
		LOG_WARN("The UID does not map to a valid IP!"<<endl)
	}

private:
	ProbeSession* session;
	UID_t dst;
};


ProbeSession::ProbeSession() : ip_sess(0) {

}

ProbeSession::~ProbeSession() {

}

void ProbeSession::startProbe(ProbeHandling* ph, UID_t final_dst_uid){
	LOG_DEBUG("["<<inHost()->getUID()<<","<<getUID()<<"] start probe "<<endl)
	if(probe_map.find(final_dst_uid)!=probe_map.end()){ //this dst uid has already been in the map
			(*probe_map.find(final_dst_uid)).second.push_back(ph);
	}else{ //this is the first time
		//create a list for probe handling objects
		ProbeHandlingList probe_list;
		probe_list.push_back(ph);
		probe_map.insert(SSFNET_MAKE_PAIR(final_dst_uid, probe_list));
	}
	IPAddress ipaddr;
	inHost()->getCommunity()->synchronousNameResolution(final_dst_uid, ipaddr);
	if(IPAddress::IPADDR_INVALID == ipaddr) {
		//do async lookup
		LOG_DEBUG("DO ASYNC LOOKUP for dst uid "<<final_dst_uid<<endl)
		inHost()->getCommunity()->asynchronousNameResolution(final_dst_uid, new ProbeIPNameResolutionCallBack(this, final_dst_uid));
	}else{
		LOG_DEBUG("7"<<endl)
		this->startProbe(final_dst_uid, ipaddr);
	}
}

void ProbeSession::startProbe(UID_t dst, IPAddress ipaddr){
	LOG_DEBUG("start probe, uid="<<dst<<", ip="<<ipaddr<<endl);
	//create a packet
	ProbeMessage* probemsg = new ProbeMessage();
	probemsg->setSrc(inHost()->getUID());
	probemsg->setDst(dst);

	IPv4Message* ipmsg = new IPv4Message(inHost()->getDefaultIP(),ipaddr, SSFNET_PROTOCOL_TYPE_PROBE);
	//set TTL to be 0
	ipmsg->setTTL(0);
	ipmsg->carryPayload(probemsg);

	//send a probe pkt.....
	getIPSession()->push(ipmsg, this);
}

int ProbeSession::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra)
{
	LOG_ERROR("push method is disabled."<<endl);
	return 0;
}

int ProbeSession::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
	//if dst, reset the src and dst, keep sending the packet
	//if src, call handleProbe()
	LOG_DEBUG("["<<inHost()->getUID()<<","<<getUID()<<"] probe message pop "<<getUName()<<endl)
	assert(msg && msg->archetype() == getProtocolNumber()); // probe message must be popped
	assert(losess); // there's lower layer
	assert(!extra); // no extra stuff

	ProbeMessage* probemsg = SSFNET_STATIC_CAST(ProbeMessage*,msg);

	if(inHost()->getUID()==probemsg->getDst()){ //we are at the destination
		IPv4Message* iph = (IPv4Message*)probemsg->carrier();
		IPAddress dst = iph->getDst();
		IPAddress src = iph->getSrc();

		LOG_DEBUG("we are at the destination, src="<<src<<", dst="<<dst<<endl);
		probemsg->reuse();
		//reset the src and dst
		iph->setDst(src);
		iph->setSrc(dst);
		//LOG_DEBUG("["<<inHost()->getUID()<<","<<getUID()<<"] sending echo probe src="<<iph->getSrc()<<", dst="<<iph->getDst()<<endl);
		return getIPSession()->push(iph, this);
	}
	else if(inHost()->getUID()==probemsg->getSrc()){ //we are at the source
		UID_t dst=probemsg->getDst();
		if(probe_map.find(dst)!=probe_map.end()){ //this dst uid has already been in the map
			ProbeHandling* ph=(*probe_map.find(dst)).second.front();
			assert(ph);
			ph->handleProbe(probemsg);
			(*probe_map.find(dst)).second.pop_front();
			if((*probe_map.find(dst)).second.empty()){ //all probeHandlings have already handled the probe packet.
				probe_map.erase(probe_map.find(dst));
			}
		}else{
			LOG_ERROR("The dst uid="<<dst<<" cannot be found in the probe map!"<<endl)
		}
	}
	return 0;
}

void ProbeSession::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac) {
	LOG_ERROR("startTraffic has never been called by probe session."<<endl);
}


IPv4Session* ProbeSession::getIPSession()
{
	if(!ip_sess) {
		ip_sess = (IPv4Session*)inHost()->sessionForNumber
				(SSFNET_PROTOCOL_TYPE_IPV4);
		if(!ip_sess) LOG_ERROR("missing IP session.");
	}
	return ip_sess;
}


} // namespace ssfnet
} // namespace prime
