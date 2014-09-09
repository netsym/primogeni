/*
 * \file stcp_master.cc
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
#include "stcp_master.h"
#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "ssf.h"
#include "os/ssfnet_types.h"
#include "net/host.h"
#include "os/protocol_session.h"
#include "stcp_session.h"
#include "stcp_message.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(STCPMaster);

//#define LOG_DEBUG(X) {std::cout<<"[stcp_master.cc:"<<__LINE__<<"]"<<X<<endl;}
//#define LOG_WARN(X) {std::cout<<"[stcp_master.cc:"<<__LINE__<<"]"<<X<<endl;}

bool STCPConnection::fncmp(STCPConnection* l, STCPConnection* r) {
		int rv;
		if(l->src_port == r->src_port) {
			if(l->dst_port == r->dst_port) {
				if((uint32)(l->src_ip) == (uint32)(r->src_ip)) {
					if((uint32)(l->dst_ip) == (uint32)(r->dst_ip)) {
						rv=0;
					}
					else if((uint32)(l->dst_ip) < (uint32)(r->dst_ip)){
						rv=-1;
					}
					else {
						rv=1;
					}
				}
				else if((uint32)(l->src_ip) < (uint32)(r->src_ip)){
					rv=-1;
				}
				else {
					rv=1;
				}
			}
			else if(l->dst_port < r->dst_port){
				rv=-1;
			}
			else {
				rv=1;
			}
		}
		else if(l->src_port < r->src_port){
			rv=-1;
		}
		else {
			rv=1;
		}
		return rv==-1;
}

STCPMaster::STCPMaster() :
	active_sessions(STCPConnection::fncmp),ip_sess(0), next_src_port(1)
{
	LOG_DEBUG("new STCP master session\n");
}

// the destructor
STCPMaster::~STCPMaster(){
	for(STCP_SESSION_MAP::iterator iter=active_sessions.begin(); iter!=active_sessions.end(); iter++){
		delete iter->second;
	}
	for(STCP_LISTEN_SESSION_MAP::iterator iter=listening_sessions.begin(); iter!=listening_sessions.end(); iter++){
		delete iter->second;
	}
}

// called after config() to initialize this protocol session
void STCPMaster::init(){
	// the same method at the parent class must be called
	ProtocolSession::init();

	if(!ip_sess) {
		ip_sess = inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_IPV4);
		if(!ip_sess) LOG_ERROR("missing IP session.");
	}
}

int STCPMaster::push(ProtocolMessage* msg, ProtocolSession* hi_sess,
			void* extinfo, size_t extinfo_size)
{
	IPPushOption* inf = (IPPushOption*) extinfo;
	IPv4Message* ipmsg = new IPv4Message(inf->src_ip, inf->dst_ip,
			SSFNET_PROTOCOL_TYPE_STCP);
	//Set TTL to be 0 for probe message.

	ipmsg->setTTL(0);

	LOG_DEBUG("STCPMASTER:push: uid="<<getUID()<<" SRC=" << inf->src_ip << " DST=" << inf->dst_ip <<
			" msg_>getLength=" << ((STCPMessage*)msg)->getLength() << endl);
	ipmsg->carryPayload(msg);
	ip_sess->push(ipmsg, this);
	return 0;
}

int STCPMaster::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra){
	IPOptionToAbove* ops  = (IPOptionToAbove*)extra;
	STCPMessage* stcphdr = (STCPMessage*)msg;

	LOG_DEBUG("pop: uid="<<getUID()<<"on host "<<inHost()->getUID()<<" a new packet has arrived with src_port=" << stcphdr->getSrcPort() <<
			" dst_port=" << stcphdr->getDstPort()<< " src_ip=" << ops->src_ip <<
			" size="<< stcphdr->getLength() << endl);

	// Search in the set of sessions
	// Packet sent as reply
	struct STCPConnection c;
	c.src_ip 	= ops->dst_ip;
	c.src_port  = stcphdr->getDstPort();
	c.dst_ip	= ops->src_ip;
	c.dst_port  = stcphdr->getSrcPort();


	STCP_SESSION_MAP::iterator f = active_sessions.find(&c);
	if(f !=active_sessions.end()){ //this is an active session
		(*f).second->receive(stcphdr, extra);
		return 0;
	}

	STCP_LISTEN_SESSION_MAP::iterator found = listening_sessions.find(c.src_port);

	if(found ==listening_sessions.end()){
		int app_type = ApplicationSession::getApplicationProtocolType(stcphdr->getDstPort());
		if(app_type) {
			//LOG_DEBUG("\tfound no active connections or listening sessions! Automatically creating app_type="<<app_type<<", port="<<stcphdr->getDstPort()<<endl);
			if(!inHost()->sessionForNumber(app_type)) {
				LOG_ERROR("should never see this!\n");
			}
			found = listening_sessions.find(c.src_port);
		}
		else return 0; //HACK XXX just ignore pkt
	}

	if(found !=listening_sessions.end()){ //this is a listening session
		//make new active session
		LOG_DEBUG("\tfound listening session! Automatically session, "<<c.dst_ip<<":"<<c.dst_port<<"\n");
		STCPSession* sess = (*found).second;
		STCPConnection* conn = sess->getConnection();
		listening_sessions.erase(found);
		conn->src_ip 	= inHost()->getDefaultIP();
		conn->src_port  = stcphdr->getDstPort();
		conn->dst_ip	= ops->src_ip;
		conn->dst_port  = stcphdr->getSrcPort();
		if(active_sessions.find(conn)!=active_sessions.end()) {
			LOG_WARN("duplicate active session! uid="<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
					" src_port=" << conn->src_port << " dst_port=" << conn->dst_port<<", current sessions:"<<endl)
			for(STCP_SESSION_MAP::iterator found=active_sessions.begin();found!=active_sessions.end();found++) {
				STCPConnection* conn1 =(*found).second->getConnection();
				LOG_WARN("\t[a]"<<getUID()<<", src_ip=" << conn1->src_ip << " dst_ip=" << conn1->dst_ip <<
						" src_port=" << conn1->src_port << " dst_port=" << conn1->dst_port << "\n");
				conn1 =(*found).first;
				LOG_WARN("\t[b]"<<getUID()<<", src_ip=" << conn1->src_ip << " dst_ip=" << conn1->dst_ip <<
						" src_port=" << conn1->src_port << " dst_port=" << conn1->dst_port << "\n\n");
			}
			conn=(*active_sessions.find(conn)).first;
			LOG_ERROR("duplicate active session! uid="<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
					" src_port=" << conn->src_port << " dst_port=" << conn->dst_port<<endl);
		}
		active_sessions.insert(SSFNET_MAKE_PAIR(conn, sess));
		LOG_DEBUG("\tnew active STCP session, uid="<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
				" src_port=" << conn->src_port << " dst_port=" << conn->dst_port<<endl);
		sess->receive(stcphdr, extra);
		return 0;
	}
	else {
		LOG_WARN("What happend?\n")
	}
	return 0;
}

TransportSession* STCPMaster::createActiveSession(SimpleSocket* sock_,
		int local_port,
		IPAddress remote_ip, int remote_port) {
	struct STCPConnection* conn = new STCPConnection();
	conn->src_ip 	= inHost()->getDefaultIP();
	conn->src_port 	= local_port;
	conn->dst_ip		= remote_ip;
	conn->dst_port 	= remote_port;
	STCPSession* sess = new STCPSession(this, sock_, conn);
	if(active_sessions.find(conn)!=active_sessions.end()) {
		LOG_WARN("duplicate active session! uid="<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
				" src_port=" << conn->src_port << " dst_port=" << conn->dst_port<<", current sessions:"<<endl)
		for(STCP_SESSION_MAP::iterator found=active_sessions.begin();found!=active_sessions.end();found++) {
			STCPConnection* conn1 =(*found).second->getConnection();
			LOG_WARN("\t[a]"<<getUID()<<", src_ip=" << conn1->src_ip << " dst_ip=" << conn1->dst_ip <<
					" src_port=" << conn1->src_port << " dst_port=" << conn1->dst_port << "\n");
			conn1 =(*found).first;
			LOG_WARN("\t[b]"<<getUID()<<", src_ip=" << conn1->src_ip << " dst_ip=" << conn1->dst_ip <<
					" src_port=" << conn1->src_port << " dst_port=" << conn1->dst_port << "\n\n");
		}
		conn=(*active_sessions.find(conn)).first;
		LOG_ERROR("duplicate active session! uid="<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
				" src_port=" << conn->src_port << " dst_port=" << conn->dst_port<<endl);
	}
	active_sessions.insert(SSFNET_MAKE_PAIR(conn, sess));

	LOG_DEBUG("\tnew active STCP session, uid="<<getUID()<<"on host "<<inHost()->getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
			" src_port=" << conn->src_port << " dst_port=" << conn->dst_port<<endl);

	return sess;
}

TransportSession* STCPMaster::createListeningSession(SimpleSocket* sock,
		IPAddress listen_ip, int listen_port)
{
	struct STCPConnection* conn = new STCPConnection();
	//Create an object containing data of the present connection
	conn->src_ip 	= IPADDR_INADDR_ANY;
	conn->src_port 	= -1;
	conn->dst_ip		= listen_ip;
	conn->dst_port 	= listen_port;

	STCPSession* sess = new STCPSession(this, sock, conn);
	listening_sessions.insert(SSFNET_MAKE_PAIR(listen_port, sess));

	//LOG_DEBUG("\tnew LISTENING STCP session, uid="<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
	//		" src_port=" << conn->src_port << " dst_port=" << conn->dst_port << "\n");

	return sess;
}

void STCPMaster::deleteSession(TransportSession* session){
	assert(session);
	STCPConnection* conn = ((STCPSession*)session)->getConnection();
	STCP_SESSION_MAP::iterator found = active_sessions.find(conn);
	LOG_DEBUG("tried to delete uid="<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
			" src_port=" << conn->src_port << " dst_port=" << conn->dst_port << "\n");
	if(found==active_sessions.end()){
		LOG_WARN("Try to erase an inactive session, current sessions:"<<endl)
		for(found=active_sessions.begin();found!=active_sessions.end();found++) {
			conn =(*found).second->getConnection();
			LOG_WARN("\t[a]"<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
					" src_port=" << conn->src_port << " dst_port=" << conn->dst_port << "\n");
			conn =(*found).first;
			LOG_WARN("\t[b]"<<getUID()<<", src_ip=" << conn->src_ip << " dst_ip=" << conn->dst_ip <<
					" src_port=" << conn->src_port << " dst_port=" << conn->dst_port << "\n\n");
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
