/**
 * \file ipv4_session.cc
 * \brief Source file for the IPv4Session class.
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
#include <string.h>
#include "os/logger.h"
#include "os/community.h"
#include "net/host.h"
#include "proto/ipv4/ipv4_session.h"
#include "proto/ipv4/icmpv4_session.h"
#include "proto/fluid/probe_message.h"
#include "proto/stcp/stcp_message.h"

namespace prime {
namespace ssfnet {

//#define LOG_DEBUG(X) {std::cout<<"[ipv4_session.cc:"<<__LINE__<<"]"<<X<<endl;}
//#define LOG_WARN(X) {std::cout<<"[ipv4_session.cc:"<<__LINE__<<"]"<<X<<endl;}
LOGGING_COMPONENT(IPv4Session);

/**
 * Used to preform async lookups
 *
 */
class IPv4NameResolutionCallBack: public NameResolutionCallBackWrapper {
public:
	IPv4NameResolutionCallBack(bool _popping, IPv4Session* _ips, ProtocolSession* _other_session, IPv4MessageWrapper* msgwrap)
		: NameResolutionCallBackWrapper(), popping(_popping), ips(_ips), other_session(_other_session), msgwrap(msgwrap){
		//no op
	}

	virtual ~IPv4NameResolutionCallBack() {
	}

	/**
	 * called when a mac or ip is searched for
	 */
	virtual void call_back(UID_t uid) {
		if(popping) {
			ips->pop(msgwrap,other_session, uid);
		}
		else {
			ips->push(msgwrap,other_session, uid);
		}
		ips=0;
		other_session=0;
	}

	/**
	 * called when a uid is searched for
	 */
	virtual void call_back(IPAddress ip, MACAddress mac) {
		LOG_ERROR("IPv4NameResolutionCallBack::call_back(IPAddress ip, MACAddress mac) should never be called!"<<endl)
	}

	/*
	 * called when the uid/mac/ip can't be found
	 */
	virtual void invalid_query() {
		IPv4Message* ipmsg = msgwrap->getMsg();
		LOG_WARN("Unable to route to ip "<<ipmsg->getDst()<<", the IP appears to be invalid!"<<endl);
		delete ipmsg;
		delete msgwrap;
		ips=0;
		other_session=0;
	}

private:
	bool popping;
	IPv4Session* ips;
	ProtocolSession* other_session;
	IPv4MessageWrapper* msgwrap;
};



IPv4MessageWrapper::IPv4MessageWrapper(IPv4Message* msg, ProtocolSession* sess) :
    ipmsg(msg), hisess(sess), innic(0), outnic(0) {}
IPv4MessageWrapper::IPv4MessageWrapper(IPv4Message* msg, Interface* nic) :
    ipmsg(msg), hisess(0), innic(nic), outnic(0){}
IPv4MessageWrapper::~IPv4MessageWrapper() {}
bool IPv4MessageWrapper::isPushed() { return innic == 0; }
bool IPv4MessageWrapper::isOutRouteDetermined() { return outnic != 0; }
void IPv4MessageWrapper::setOutRoute(Interface* nic) {
    outnic = nic;
}
void IPv4MessageWrapper::resetOutRoute() { outnic = 0; }
IPv4Message* IPv4MessageWrapper::getMsg() { return ipmsg; }
Interface* IPv4MessageWrapper::getInNIC() { return innic; }
Interface* IPv4MessageWrapper::getOutNIC() { return outnic; }

IPv4Session::IPv4Session() :
	icmp(0), community(0), fr_engine(0), unique_iptables_handle(0) {
	iptables_chains = new IPTablesEntry*[IPTABLES_TOTAL_CHAINS];
	memset(iptables_chains, 0, IPTABLES_TOTAL_CHAINS * sizeof(IPTablesEntry*));
}

IPv4Session::~IPv4Session() {
	for (int i = 0; i < IPTABLES_TOTAL_CHAINS; i++) {
		IPTablesEntry* entry = iptables_chains[i];
		while (entry) {
			IPTablesEntry* next = entry->next;
			delete entry;
			entry = next;
		}
	}
	delete[] iptables_chains;
	if (fr_engine)
		delete fr_engine;
}

void IPv4Session::init() {
}

int IPv4Session::push(ProtocolMessage* msg, ProtocolSession* hisess,
		void* extra) {
	LOG_DEBUG("message is pushed; src="<<SSFNET_DYNAMIC_CAST(IPv4Message*,msg)->getSrc()<<
		    ", dst="<<SSFNET_DYNAMIC_CAST(IPv4Message*,msg)->getDst()<<endl);
	assert(msg && msg->archetype() == getProtocolNumber()); // ip message must be pushed
	assert(hisess); // upper layer must identify itself
	assert(!extra); // upper layer shall not pass extra stuff
	IPv4MessageWrapper* msgwrap = new IPv4MessageWrapper((IPv4Message*) msg,
			hisess);
	return this->push(msgwrap,hisess,0);
}

int IPv4Session::push(IPv4MessageWrapper* msgwrap, ProtocolSession* hisess, UID_t callback_uid) {
	ACK_COUT("ip push", this);
	msgwrap = conduct_routing(false, hisess, msgwrap,callback_uid);
	if (!msgwrap)
		return 0;
	if (msgwrap->isOutRouteDetermined()) { // if outgoing route has been determined
		msgwrap = iptables_output(msgwrap);
		if (!msgwrap)
			return 0;
		if (!msgwrap->isOutRouteDetermined()) { // if routing is needed (reset by the output chain)
			msgwrap = conduct_routing(false, hisess, msgwrap,callback_uid);
			if (!msgwrap)
				return 0;
		}
	}
	return iptables_input_or_postrouting(msgwrap);
}

int IPv4Session::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra) {
	assert(msg && msg->archetype() == getProtocolNumber()); // ip message must be popped
	//LOG_DEBUG("message is popped; src="<<SSFNET_DYNAMIC_CAST(IPv4Message*,msg)->getSrc()<<
	//		", dst="<<SSFNET_DYNAMIC_CAST(IPv4Message*,msg)->getDst()<<endl);
	assert(!losess); // there's no lower layer
	assert(extra); // the receiving interface is passed as the extra stuff

	//we could check if the ttl is zero here, but we
	//choose to do this after routing so Ting's
	//probe messages can get access to the in AND out nics.
	//This is also the reason we don't decrement ttl here

	IPv4MessageWrapper* msgwrap = new IPv4MessageWrapper((IPv4Message*) msg,
			(Interface*) extra);
	return this->pop(msgwrap, losess, 0);
}

int IPv4Session::pop(IPv4MessageWrapper* msgwrap, ProtocolSession* losess, UID_t callback_uid) {
	ACK_COUT("ip pop", this);
	msgwrap = iptables_prerouting(msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = conduct_routing(true, losess, msgwrap, callback_uid);
	if (!msgwrap)
		return 0;
	if (msgwrap->isOutRouteDetermined()) { // if outgoing route has been determined
		msgwrap = iptables_forward(msgwrap);
		if (!msgwrap)
			return 0;
		if (!msgwrap->isOutRouteDetermined()) { // if routing is needed (reset by the forward chain)
			msgwrap = conduct_routing(true, losess, msgwrap, callback_uid);
			if (!msgwrap)
				return 0;
		}
	}
	return iptables_input_or_postrouting(msgwrap);
}

int IPv4Session::addIPTablesCallback(int chain_id, IPTablesCallback* callback,
		ProtocolSession* sess) {
	if (chain_id < 0 || chain_id >= IPTABLES_TOTAL_CHAINS) {
		LOG_INFO("chain_id (" << chain_id << ") out of range.");
		return 0;
	}
	IPTablesEntry* entry = new IPTablesEntry;
	entry->handle = ++unique_iptables_handle;
	entry->sess = sess;
	entry->callback = callback;
	entry->next = iptables_chains[chain_id];
	iptables_chains[chain_id] = entry;
	return entry->handle;
}

int IPv4Session::removeIPTablesCallback(int chain_id, int handle) {
	if (chain_id < 0 || chain_id >= IPTABLES_TOTAL_CHAINS) {
		LOG_INFO("chain_id (" << chain_id << ") out of range.");
		return 0;
	}
	IPTablesEntry** ptr = &iptables_chains[chain_id];
	while (*ptr) {
		IPTablesEntry* entry = *ptr;
		if (entry->handle == handle) {
			*ptr = entry->next;
			delete entry;
			return handle; // indicate that we have indeed removed it!
		} else
			ptr = &entry->next;
	}
	return 0;
}

bool IPv4Session::verifyLocalIP(IPAddress ipaddr) {
	//LOG_DEBUG("The host is: "<<inHost()->getUName()<<endl)
	if (ipaddr == IPAddress::IPADDR_LOOPBACK || inHost()->getInterfaceByIP(ipaddr))
		return true;
	else
		return false;
}

bool IPv4Session::verifyTargetIP(IPAddress ipaddr, Interface* nic) {
	if (inHost()->getInterfaceByIP(ipaddr) || nic->isIPBroadcastAddress(ipaddr)
			|| nic->isMulticastAddress(ipaddr))
		return true;
	else
		return false;
}

ICMPv4Session* IPv4Session::getICMPSession() {
	if (!icmp) {
		icmp = (ICMPv4Session*) inHost()->sessionForNumber(
				SSFNET_PROTOCOL_TYPE_ICMPV4);
		if (!icmp)
			LOG_ERROR("missing ICMP session.");
	}
	return icmp;
}

Community* IPv4Session::getCommunity() {
	if(!community)
		community=inHost()->getCommunity();
	return community;
}

IPv4MessageWrapper* IPv4Session::conduct_routing(bool popping, ProtocolSession* other_session, IPv4MessageWrapper* msgwrap, UID_t callback_uid) {
	IPv4Message* ipmsg = msgwrap->getMsg();
	LOG_DEBUG("ipmsg->getDst()="<<ipmsg->getDst()<<endl)
	//LOG_DEBUG("Is pushed: "<<msgwrap->isPushed()<<endl)
	if ((msgwrap->isPushed() && verifyLocalIP(ipmsg->getDst())) || // local host sends a message to itself
			(!msgwrap->isPushed() && verifyTargetIP(ipmsg->getDst(), msgwrap->getInNIC()))  // receives a remote message destined here
			) {
		//LOG_DEBUG("the pkt is meant for this machine"<<endl)
		msgwrap->resetOutRoute(); // no outgoing route meaning it's for local
	} else {
		if (fr_engine) {
			//LOG_DEBUG("we have a fwd_eng"<<endl)
			Packet* pkt = ipmsg->getPacket();
			if (!pkt)
				pkt = new Packet(ipmsg);
			if(!pkt->getFinalDestinationUID()) {
				//LOG_DEBUG("Setting final dest id\n");
				if(callback_uid) {
					//LOG_DEBUG("used callback_uid, "<<callback_uid<<"\n");
					pkt->setFinalDestinationUID(callback_uid);
				}
				else {
					pkt->setFinalDestinationUID(getCommunity()->synchronousNameResolution(ipmsg->getDst()));
					//LOG_DEBUG("looked up uid, "<<pkt->getFinalDestinationUID()<<"\n");
					if(!pkt->getFinalDestinationUID()) {
						//IPv4NameResolutionCallBack will either call push/pop back with the answer....
						getCommunity()->asynchronousNameResolution(ipmsg->getDst(), new IPv4NameResolutionCallBack(popping, this, other_session, msgwrap));
						return 0;
					}
				}
			}
			//LOG_DEBUG("ip before routing..., pkt="<<pkt<<", final_dst="<<pkt->getFinalDestinationUID()<<endl)
			Interface* nic=NULL;
			if (fr_engine->getOutboundIface(SSFNET_DYNAMIC_CAST(Host*,getParent()),
					pkt, nic)) {// forwarding table lookup
				assert(nic);
				//LOG_DEBUG("Setting outNic to "<<nic->getMAC()<<endl)
				msgwrap->setOutRoute(nic);
			}
			else {
				nic = inHost()->getTrafficPortal((uint32)msgwrap->getMsg()->getDst());
				if(nic) {
					msgwrap->setOutRoute(nic);
				}
				else {
					/* drop the packet */
	#if TEST_ROUTING == 1
					saveRouteDebug(ipmsg->getPacket(),this);
	#else
					LOG_WARN("Cant forward the pkt, sending icmp unreachable back and dropping pkt"<<endl)
					if (!msgwrap->isPushed()) { // generate icmp if received from elsewhere
						getICMPSession()->sendDestUnreachHost(ipmsg);
					}
	#endif
					delete msgwrap;
					ipmsg->erase();
					return 0;
				}
			}
			//LOG_DEBUG("ip after routing..., pkt="<<pkt<<", outbound nic="<<msgwrap->getOutNIC()<<", final_dst="<<pkt->getFinalDestinationUID()<<endl)
		} else {
			LOG_WARN("Somehow we don't have a forwarding engine!"<<endl)
			ipmsg->erase();
			delete msgwrap;
			return 0;
		}
	}
	return msgwrap;
}

int IPv4Session::iptables_input_or_postrouting(IPv4MessageWrapper* msgwrap) {
	LOG_DEBUG("in iptables_input or postrouting: default_ip="<<SSFNET_DYNAMIC_CAST(Host*,getParent())->getDefaultIP()<<endl);
	if (msgwrap->isOutRouteDetermined()) { // if outgoing route has been determined
		msgwrap = iptables_postrouting(msgwrap);
		if (!msgwrap)
			return 0;
		//check if the ttl is 0
		if(msgwrap->getMsg()->getTTL()==0) {
			ProbeMessage* probe = dynamic_cast<ProbeMessage*>(msgwrap->getMsg()->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_PROBE));
			LOG_DEBUG("TTL is 0"<<endl);
			if(probe) {
				//get output nic index (rank in terms of the host)
				UID_t outnic_rank = msgwrap->getOutNIC()->getRank(msgwrap->getOutNIC()->getParent());
				//add the output nic uid into the probe message
				LOG_DEBUG("Add out nic="<<outnic_rank<<" on node="<<inHost()<<endl);
				probe->addNics(outnic_rank);
				probe->addNodeUIDs(this->inHost()->getUID());
				//send this as a probe that skips queues and is not dropped
				msgwrap->getOutNIC()->sendProbe(msgwrap->getMsg());
			}
			else {
				STCPMessage* stcp_probe = dynamic_cast<STCPMessage*>(
							msgwrap->getMsg()->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_STCP)
							);
				if(stcp_probe) {
					Interface* iface = msgwrap->getOutNIC();
					if(stcp_probe->getType()!=STCPMessage::END){
						//send this as a probe that is not dropped
						//set the in/out bytes difference and the queue size of this interface for loss probability calculation
						int queue_size = iface->getNicQueue()->getQueueSize();
						stcp_probe->insertLossCalculationMap(iface->getUID(), iface->getBytesReceived(),iface->getBytesSent(), queue_size);

					    //XXX only when there is loss??
						float capacity = max(iface->getBitRate(),iface->getLink()->getBandwidth());
						float ts = (float)1440*8/capacity;

						if(stcp_probe->getMinTimestep()==0 || stcp_probe->getMinTimestep() > ts){
							stcp_probe->setMinTimestep(ts);
						}

						if(!stcp_probe->payload()){ //no carried data
							//set should_drop
							iface->send(msgwrap->getMsg(),true); //cannot drop
						}else{ //has data
							IPv4Message* new_msg=new IPv4Message(*msgwrap->getMsg());
							bool should_have_been_dropped=iface->send(msgwrap->getMsg(),false); //can drop
							if(should_have_been_dropped){
								//drop the payload in stcp message and resend it out without dropping it
								STCPMessage* new_stcp_probe = dynamic_cast<STCPMessage*>(new_msg->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_STCP));
								new_stcp_probe->dropPayload();
								iface->send(new_msg,true);
							}
						}
					}else{
						//This is END probe.
						//send this as a probe that skips queues and is not dropped
						iface->sendProbe(msgwrap->getMsg());
					}
				}
				else {
					LOG_WARN("Dropping pkt since the ttl was set to 0!"<<endl);
		#if TEST_ROUTING
					saveRouteDebug(msgwrap->getMsg()->getPacket(),this);
					msgwrap->getMsg()->erase();
		#else
					inHost()->sendTimeExceededMsg(msgwrap->getMsg(), msgwrap->getInNIC()->getIP());
		#endif
				}
			}
		}
		else {
			//decrement ttl
			msgwrap->getMsg()->decrementTTL();
			msgwrap->getOutNIC()->send(msgwrap->getMsg());
		}
		delete msgwrap;
		return 0;
	} else { // if it's going local
		msgwrap = iptables_input(msgwrap);
		if (!msgwrap)
			return 0;
		IPv4Message* ipmsg = msgwrap->getMsg();
		ProtocolSession* sess = inHost()->sessionForNumber(
				(int) ipmsg->getProto()); // some will be created by demand
		if (sess) {
			switch((int)ipmsg->getProto()) {
			case SSFNET_PROTOCOL_TYPE_UDP: {
				IPOptionToAbove ops;
				ops.src_ip = ipmsg->getSrc();
				ops.dst_ip = ipmsg->getDst();
				ACK_COUT("ip popping up udp", this);
				sess->pop(ipmsg->payload(), this, (void*)&ops);
				break;
			}
			case SSFNET_PROTOCOL_TYPE_TCP: {
				IPOptionToAbove ops;
				ops.src_ip = ipmsg->getSrc();
				ops.dst_ip = ipmsg->getDst();
				ACK_COUT("ip popping up tcp", this);
				sess->pop(ipmsg->payload(), this, (void*)&ops);
				break;
			}
			case SSFNET_PROTOCOL_TYPE_STCP: {
				IPOptionToAbove ops;
				ops.src_ip = ipmsg->getSrc();
				ops.dst_ip = ipmsg->getDst();
				ACK_COUT("ip popping up stcp", this);
				sess->pop(ipmsg->payload(), this, (void*)&ops);
				break;
			}
			default: {
				ACK_COUT("ip popping up default", this);
				sess->pop(ipmsg->payload(), this);
				break;
				}
			}
		}
		else {
			ACK_COUT("ip dropping pkt", this);
			LOG_WARN("Cant forward the pkt, dropping it! (src="<<ipmsg->getSrc()<<", dst="<<ipmsg->getDst()<<")"<<endl);
			if (!msgwrap->isPushed()) { // generate icmp if received from elsewhere
#ifndef TEST_ROUTING
				getICMPSession()->sendDestUnreachProto(ipmsg);
#endif
			}
			ipmsg->erase();
		}
		delete msgwrap;
		return 0;
	}
}

ForwardingEngine* IPv4Session::getForwardingEngine() {
	return fr_engine;
}

void IPv4Session::setForwardingEngine(ForwardingEngine* fwdengine) {
	if (fr_engine != NULL && fr_engine->isLocal()) {
		delete fr_engine;
	}
	fr_engine = fwdengine;
}

IPv4MessageWrapper* IPv4Session::iptables_prerouting(
		IPv4MessageWrapper* msgwrap) {
	msgwrap = iptables_processing(IPTABLES_PREROUTING_RAW, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_PREROUTING_MANGLE, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_PREROUTING_NAT, msgwrap);
	if (!msgwrap)
		return 0;
	return msgwrap;
}

IPv4MessageWrapper* IPv4Session::iptables_input(IPv4MessageWrapper* msgwrap) {
	msgwrap = iptables_processing(IPTABLES_INPUT_MANGLE, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_INPUT_FILTER, msgwrap);
	if (!msgwrap)
		return 0;
	return msgwrap;
}

IPv4MessageWrapper* IPv4Session::iptables_forward(IPv4MessageWrapper* msgwrap) {
	msgwrap = iptables_processing(IPTABLES_FORWARD_MANGLE, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_FORWARD_FILTER, msgwrap);
	if (!msgwrap)
		return 0;
	return msgwrap;
}

IPv4MessageWrapper* IPv4Session::iptables_output(IPv4MessageWrapper* msgwrap) {
	msgwrap = iptables_processing(IPTABLES_OUTPUT_RAW, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_OUTPUT_MANGLE, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_OUTPUT_NAT, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_OUTPUT_FILTER, msgwrap);
	if (!msgwrap)
		return 0;
	return msgwrap;
}

IPv4MessageWrapper* IPv4Session::iptables_postrouting(
		IPv4MessageWrapper* msgwrap) {
	//LOG_DEBUG("In iptables_postrouting"<<endl)
	msgwrap = iptables_processing(IPTABLES_POSTROUTING_MANGLE, msgwrap);
	if (!msgwrap)
		return 0;
	msgwrap = iptables_processing(IPTABLES_POSTROUTING_FILTER, msgwrap);
	if (!msgwrap)
		return 0;
	return msgwrap;
}

IPv4MessageWrapper* IPv4Session::iptables_processing(int chain_id,
		IPv4MessageWrapper* msgwrap) {
	IPTablesEntry* entry = iptables_chains[chain_id];
	while (entry) {
		int ret = (*entry->callback)(entry->sess, msgwrap->getMsg());
		if (ret == IPTABLES_CALLBACK_MODIFIED) {
			msgwrap->resetOutRoute(); // reset outgoing route
		} else if (ret == IPTABLES_CALLBACK_REMOVED) {
			delete msgwrap; // the callback is responsible for deleting ipmsg
			return 0;
		}
		entry = entry->next;
	}
	return msgwrap;
}

} // namespace ssfnet
} // namespace prime
