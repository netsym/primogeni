/**
 * \file icmpv4_session.cc
 * \brief Source file for the ICMPv4Session class.
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

#include "os/logger.h"
#include "net/host.h"
#include "proto/ipv4/ipv4_message.h"
#include "proto/ipv4/ipv4_session.h"
#include "proto/ipv4/icmpv4_session.h"
#include "proto/ipv4/icmp_traffic.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(ICMPv4Session);


ICMPv4Session::ICMPv4Session() : ip_sess(0), seq(0) {

}

ICMPv4Session::~ICMPv4Session() {

}

int ICMPv4Session::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra)
{
	LOG_ERROR("push method is disabled.");
	return 0;
}

int ICMPv4Session::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
	LOG_DEBUG("["<<inHost()->getUID()<<","<<getUID()<<"] icmp pop "<<getUName()<<endl)
	assert(msg && msg->archetype() == getProtocolNumber()); // icmp message must be popped
	assert(losess); // there's lower layer
	assert(!extra); // no extra stuff

	ICMPv4Message* icmph = (ICMPv4Message*)msg;
	//XXX, this is correct to get seq?
	uint32_t key=(this->getUID()<<16)|icmph->getEchoMsgSeq();
	if(msg_traffic.find(key)!=msg_traffic.end()){
		TRAFFIC_ID_TYPE_PAIR id_type=msg_traffic.find(key)->second;
	}
	LOG_DEBUG("The icmp type is: "<<(int)icmph->getType()<<endl)
	if(ICMPv4Message::ICMP_TYPE_ECHO == icmph->getType()) {
		IPv4Message* iph = (IPv4Message*)icmph->carrier();
		IPAddress dst = iph->getDst();
		IPAddress src = iph->getSrc();
		LOG_DEBUG("in echo src="<<src<<", dst="<<dst<<endl)
#if TEST_ROUTING == 1
		saveRouteDebug(msg->getPacket(),NULL);
#endif
		icmph = ICMPv4Message::makeEchoReplyMsg((ICMPv4Message*)icmph->reuse());
		iph->setDst(src);
		iph->setSrc(dst);
		iph->setTTL(DEFAULT_IPV4_TTL);
		//LOG_DEBUG("["<<inHost()->getUID()<<","<<getUID()<<"] sending echo reply src="<<iph->getSrc()<<", dst="<<iph->getDst()<<endl);
		return getIPSession()->push(iph, this);
	}
	else if(ICMPv4Message::ICMP_TYPE_ECHO_REPLY == icmph->getType()) {
		LOG_DEBUG("in echo reply"<<endl);
		IPv4Message* iph = (IPv4Message*)icmph->carrier();
		IPAddress dst = iph->getDst();
		IPAddress src = iph->getSrc();
		EchoMap::iterator p = echo_requests.find(icmph->getEchoMsgSeq());
		if(p == echo_requests.end()) {
			LOG_WARN("received echo response that I did not expect! src="<<src<<", dst="<<dst<<endl);
		}
		else {
			//XXX send some event to TM saying this ping completed....
			//LOG_INFO("in echo reply"
			std::cout<<"[echo reply]"
					<<" request sent at "<<p->second.second()<<"s"
					<<", now="<<inHost()->getNow().second()<<"s"
					<<", rtt="<<VirtualTime(inHost()->getNow()-p->second).second()
					<<", src="<<src<<", dst="<<dst
					<<endl;
			echo_requests.erase(p);
		}

		msg->getPacket()->erase();
		return 0;
	}
	else if(ICMPv4Message::ICMP_TYPE_TIMESTAMP == icmph->getType()) {
		LOG_DEBUG("in timestamp"<<endl)
		icmph = ICMPv4Message::makeTimeReplyMsg((ICMPv4Message*)icmph->reuse());
		IPv4Message* iph = (IPv4Message*)icmph->carrier();
		IPAddress dst = iph->getDst();
		iph->setDst(iph->getSrc());
		iph->setSrc(dst);
		iph->setTTL(DEFAULT_IPV4_TTL);
		return getIPSession()->push(iph, this);
	}
	LOG_DEBUG("unknown"<<endl)
	// for all other ICMP messages, get it to the listeners
	unsigned size = listener_list.size();
	if(size == 0) icmph->erase();
	else {
		for(unsigned i=0; i<size-1; i++)
			listener_list[i]->pop(icmph, this);
		listener_list[size-1]->pop(icmph, this);
	}
	return 0;
}

void ICMPv4Session::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac) {
	LOG_DEBUG("startTraffic now="<<inHost()->getNow().second()<<", host="<<inHost()->getUID()
			<<", tt=["<<evt->getTrafficTypeUID()<<","<<evt->getTrafficType()->getUID()<<"] to "
			<<evt->getDstUID()<<endl)

	switch(evt->getTrafficType()->getConfigType()->type_id) {
	case GENERIC_ICMPv4_TRAFFIC:
	{
		LOG_ERROR("Should never see this!"<<endl)
	}
	break;
	case ICMPv4_PING_TRAFFIC:
	{
		PingTraffic* tt=SSFNET_STATIC_CAST(PingTraffic*,evt->getTrafficType());
		//get traffic id
		UID_t traffic_id=evt->getTrafficId();
		uint32_t payload_size=tt->getPayloadSize();
		TRAFFIC_ID_TYPE_PAIR type_id_pair=SSFNET_MAKE_PAIR(traffic_id, tt);

		if(ipaddr==IPAddress::IPADDR_INVALID) {
			LOG_ERROR("Did not expect to get IPAddress::IPADDR_INVALID!"<<endl)
		}

		//create a packet
		//the first parameter is the icmp id, the second parameter is the sequence #
		//we use these two parameters as the key of the msg to traffic map
		uint32_t key=(this->getUID()<<16)|seq;
		msg_traffic.insert(SSFNET_MAKE_PAIR(key, type_id_pair));
		ICMPv4Message* icmpmsg = new ICMPv4Message(this->getUID(),seq,payload_size);
		echo_requests.insert(SSFNET_MAKE_PAIR(seq,inHost()->getNow()));
		IPv4Message* ipmsg = new IPv4Message(inHost()->getDefaultIP(),ipaddr, SSFNET_PROTOCOL_TYPE_ICMPV4);
		ipmsg->carryPayload(icmpmsg);
		//send a ping pkt.....
		getIPSession()->push(ipmsg, this);
		seq++;
	}
	break;
	default:
		LOG_ERROR("Invalid traffic type...."<<endl)
	}
	evt->free();
}

void ICMPv4Session::sendTimeExceededMsg(IPAddress ifaceip, IPv4Message* ipmsg)
{
	LOG_DEBUG("[debug traceroute]\t sending icmp message from ICMPv4Session src=" << ipmsg->getSrc() <<
			" dst=" << ipmsg->getDst() << " iface=" << ifaceip << endl);

	ICMPv4Message* icmph = ICMPv4Message::makeTimeExceededMsg
					(ICMPv4Message::ICMP_CODE_EXC_TTL, ipmsg);
	IPv4Message* iph = new IPv4Message(ifaceip, ipmsg->getSrc(),
					SSFNET_PROTOCOL_TYPE_ICMPV4);

	//to force the code later to reclaculate the checksum
	((IPv4RawHeader*)(iph->getRawData()))->setCheckSum(1);

	iph->carryPayload(icmph);
	getIPSession()->push(iph, this);
}

IPv4Session* ICMPv4Session::getIPSession()
{
	if(!ip_sess) {
		ip_sess = (IPv4Session*)inHost()->sessionForNumber
				(SSFNET_PROTOCOL_TYPE_IPV4);
		if(!ip_sess) LOG_ERROR("missing IP session.");
	}
	return ip_sess;
}

void ICMPv4Session::addListener(ProtocolSession* sess)
{
	listener_list.push_back(sess);
}

void ICMPv4Session::sendDestUnreachProto(IPv4Message* ipmsg)
{
	ICMPv4Message* icmph = ICMPv4Message::makeDestUnreachMsg
			(ICMPv4Message::ICMP_CODE_PROT_UNREACH, ipmsg);
	IPv4Message* iph = new IPv4Message(ipmsg->getDst(), ipmsg->getSrc(),
			SSFNET_PROTOCOL_TYPE_ICMPV4);
	iph->carryPayload(icmph);
	getIPSession()->push(iph, this);
}

void ICMPv4Session::sendDestUnreachHost(IPv4Message* ipmsg)
{
	LOG_WARN("ACK!!! ICMPv4Session::sendDestUnreachHost causes a seg fault!.. no op for now."<<endl)
	return;
	IPv4Message* iph = new IPv4Message(ipmsg->getDst(), ipmsg->getSrc(),
			SSFNET_PROTOCOL_TYPE_ICMPV4);
	ICMPv4Message* icmph = ICMPv4Message::makeDestUnreachMsg
			(ICMPv4Message::ICMP_CODE_HOST_UNREACH, iph);
	iph->carryPayload(icmph);
	getIPSession()->push(iph, this);
}

} // namespace ssfnet
} // namespace prime
