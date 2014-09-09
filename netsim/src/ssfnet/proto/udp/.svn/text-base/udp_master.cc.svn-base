/*
 * \file udp_master.cc
 * \author Miguel Erazo
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
#include "udp_master.h"
#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "proto/ipv4/icmpv4_session.h"
#include "ssf.h"
#include "os/ssfnet_types.h"
#include "../../net/host.h"
#include "os/protocol_session.h"
#include "udp_session.h"
#include "udp_message.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(UDPMaster);

bool UDPConnection::fncmp(UDPConnection* l, UDPConnection* r) {
		int rv;
		if(l->srcPort == r->srcPort) {
			if(l->dstPort == r->dstPort) {
				if((uint32)(l->srcIP) == (uint32)(r->srcIP)) {
					if((uint32)(l->dstIP) == (uint32)(r->dstIP)) {
						rv=0;
					}
					else if((uint32)(l->dstIP) < (uint32)(r->dstIP)){
						rv=-1;
					}
					else {
						rv=1;
					}
				}
				else if((uint32)(l->srcIP) < (uint32)(r->srcIP)){
					rv=-1;
				}
				else {
					rv=1;
				}
			}
			else if(l->dstPort < r->dstPort){
				rv=-1;
			}
			else {
				rv=1;
			}
		}
		else if(l->srcPort < r->srcPort){
			rv=-1;
		}
		else {
			rv=1;
		}
		return rv==-1;
}

UDPMaster::UDPMaster() :
	active_sessions(UDPConnection::fncmp),ip_sess(0), next_src_port(1)
{
	LOG_DEBUG("new udp master session\n");
}

// the destructor
UDPMaster::~UDPMaster(){
	for(UDP_SESSION_MAP::iterator iter=active_sessions.begin(); iter!=active_sessions.end(); iter++){
		delete iter->second;
	}
	for(UDP_LISTEN_SESSION_MAP::iterator iter=listening_sessions.begin(); iter!=listening_sessions.end(); iter++){
		delete iter->second;
	}
}

// called after config() to initialize this protocol session
void UDPMaster::init(){
	// the same method at the parent class must be called
	ProtocolSession::init();

	if(!ip_sess) {
		ip_sess = inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_IPV4);
		if(!ip_sess) LOG_ERROR("missing IP session.");
	}
}

int UDPMaster::push(ProtocolMessage* msg, ProtocolSession* hi_sess,
			void* extinfo, size_t extinfo_size)
{
	IPPushOption* inf = (IPPushOption*) extinfo;
	IPv4Message* iph = new IPv4Message(inf->src_ip, inf->dst_ip,
			SSFNET_PROTOCOL_TYPE_UDP);
	//LOG_DEBUG("UDPMASTER:push: uid="<<getUID()<<" SRC=" << inf->src_ip << " DST=" << inf->dst_ip <<
	//		" msg_>getLength=" << ((UDPMessage*)msg)->getLength() << endl);
	iph->carryPayload(msg);
	ip_sess->push(iph, this);
	return 0;
}

int UDPMaster::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra){
	IPOptionToAbove* ops  = (IPOptionToAbove*)extra;
	UDPMessage* udphdr = (UDPMessage*)msg;

	//LOG_DEBUG("\n\nUDPMaster pop: uid="<<getUID()<<" a new packet has arrived with srcport=" << udphdr->getSrcPort() <<
	//		" dstport=" << udphdr->getDstPort() << " srcip=" << ops->src_ip <<
	//		" size="<< udphdr->getLength() << endl);

	// Search in the set of sessions
	// Packet sent as reply
	struct UDPConnection c;
	c.srcIP 	= ops->dst_ip;
	c.srcPort   = udphdr->getDstPort();
	c.dstIP	    = ops->src_ip;
	c.dstPort   = udphdr->getSrcPort();


	UDP_SESSION_MAP::iterator founda = active_sessions.find(&c);
	if(founda !=active_sessions.end()){
		//LOG_DEBUG("\tfound active session!\n");
		(*founda).second->receive(udphdr, extra);
		return 0;
	}

	UDP_LISTEN_SESSION_MAP::iterator found = listening_sessions.find(c.srcPort);

	if(found ==listening_sessions.end()){
		int app_type = ApplicationSession::getApplicationProtocolType(udphdr->getDstPort());
		//LOG_DEBUG("\tfound no active connections or listening sessions! Automatically creating app_type="<<app_type<<", port="<<udphdr->getDstPort()<<endl);
	
		if(app_type == -1){
			// do nothing because there is no app for this dst port. Possibly an imcp message.
			return 1;
		}

		if(!inHost()->sessionForNumber(app_type)) {
			LOG_ERROR("should never see this!\n");
		}
		found = listening_sessions.find(c.srcPort);
	}

	if(found !=listening_sessions.end()){
		//make new active session
		//LOG_DEBUG("\tfound listening session! Automatically session, "<<c.dstIP<<":"<<c.dstPort<<"\n");
		UDPSession* sess = (*found).second;
		UDPConnection* conn = sess->getConnection();
		listening_sessions.erase(found);
		conn->srcIP 	= inHost()->getDefaultIP();
		conn->srcPort   = udphdr->getDstPort();
		conn->dstIP	    = ops->src_ip;
		conn->dstPort   = udphdr->getSrcPort();
		if(active_sessions.find(conn)!=active_sessions.end()) {
			LOG_WARN("duplicate active session! uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
					" srcport=" << conn->srcPort << " dstport=" << conn->dstPort<<", current sessions:"<<endl)
			for(UDP_SESSION_MAP::iterator found=active_sessions.begin();found!=active_sessions.end();found++) {
				UDPConnection* conn1 =(*found).second->getConnection();
				LOG_WARN("\t[a]"<<getUID()<<", srcIP=" << conn1->srcIP << " dstIP=" << conn1->dstIP <<
						" srcport=" << conn1->srcPort << " dstport=" << conn1->dstPort << "\n");
				conn1 =(*found).first;
				LOG_WARN("\t[b]"<<getUID()<<", srcIP=" << conn1->srcIP << " dstIP=" << conn1->dstIP <<
						" srcport=" << conn1->srcPort << " dstport=" << conn1->dstPort << "\n\n");
			}
			conn=(*active_sessions.find(conn)).first;
			LOG_ERROR("duplicate active session! uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
					" srcport=" << conn->srcPort << " dstport=" << conn->dstPort<<endl);
		}
		active_sessions.insert(SSFNET_MAKE_PAIR(conn, sess));
		//LOG_DEBUG("\tnew active UDP session, uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
		//		" srcport=" << conn->srcPort << " dstport=" << conn->dstPort<<endl);
		sess->receive(udphdr, extra);
		return 0;
	}
	else {
		LOG_ERROR("What happend?\n")
	}
	return 0;
}


TransportSession* UDPMaster::createActiveSession(SimpleSocket* sock_,
		int local_port,
		IPAddress remote_ip, int remote_port) {
	struct UDPConnection* conn = new UDPConnection();
	conn->srcIP 	= inHost()->getDefaultIP();
	conn->srcPort 	= local_port;
	conn->dstIP		= remote_ip;
	conn->dstPort 	= remote_port;
	UDPSession* sess = new UDPSession(this, sock_, conn);
	if(active_sessions.find(conn)!=active_sessions.end()) {
		LOG_WARN("duplicate active session! uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
				" srcport=" << conn->srcPort << " dstport=" << conn->dstPort<<", current sessions:"<<endl)
		for(UDP_SESSION_MAP::iterator found=active_sessions.begin();found!=active_sessions.end();found++) {
			UDPConnection* conn1 =(*found).second->getConnection();
			LOG_WARN("\t[a]"<<getUID()<<", srcIP=" << conn1->srcIP << " dstIP=" << conn1->dstIP <<
					" srcport=" << conn1->srcPort << " dstport=" << conn1->dstPort << "\n");
			conn1 =(*found).first;
			LOG_WARN("\t[b]"<<getUID()<<", srcIP=" << conn1->srcIP << " dstIP=" << conn1->dstIP <<
					" srcport=" << conn1->srcPort << " dstport=" << conn1->dstPort << "\n\n");
		}
		conn=(*active_sessions.find(conn)).first;
		LOG_ERROR("duplicate active session! uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
				" srcport=" << conn->srcPort << " dstport=" << conn->dstPort<<endl);
	}
	active_sessions.insert(SSFNET_MAKE_PAIR(conn, sess));

	//LOG_DEBUG("\tnew active UDP session, uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
	//		" srcport=" << conn->srcPort << " dstport=" << conn->dstPort<<endl);

	return sess;
}

TransportSession* UDPMaster::createListeningSession(SimpleSocket* sock,
		IPAddress listen_ip, int listen_port)
{
	struct UDPConnection* conn = new UDPConnection();
	//Create an object containing data of the present connection
	conn->srcIP 	= IPADDR_INADDR_ANY;
	conn->srcPort 	= -1;
	conn->dstIP		= listen_ip;
	conn->dstPort 	= listen_port;

	UDPSession* sess = new UDPSession(this, sock, conn);
	listening_sessions.insert(SSFNET_MAKE_PAIR(listen_port, sess));

	//LOG_DEBUG("\tnew LISTENING UDP session, uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
	//		" srcport=" << conn->srcPort << " dstport=" << conn->dstPort << "\n");

	return sess;
}

void UDPMaster::deleteSession(TransportSession* session){
	assert(session);
	UDPConnection* conn = ((UDPSession*)session)->getConnection();
	UDP_SESSION_MAP::iterator found = active_sessions.find(conn);
	LOG_DEBUG("tried to delete uid="<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
			" srcport=" << conn->srcPort << " dstport=" << conn->dstPort << "\n");
	if(found==active_sessions.end()){
		LOG_WARN("Try to erase an inactive session, current sessions:"<<endl)
		for(found=active_sessions.begin();found!=active_sessions.end();found++) {
			conn =(*found).second->getConnection();
			LOG_WARN("\t[a]"<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
					" srcport=" << conn->srcPort << " dstport=" << conn->dstPort << "\n");
			conn =(*found).first;
			LOG_WARN("\t[b]"<<getUID()<<", srcIP=" << conn->srcIP << " dstIP=" << conn->dstIP <<
					" srcport=" << conn->srcPort << " dstport=" << conn->dstPort << "\n\n");
		}
		LOG_ERROR("see above!"<<endl)
	}
	else{
		active_sessions.erase(found);
		delete session;
	}
}

}
}
