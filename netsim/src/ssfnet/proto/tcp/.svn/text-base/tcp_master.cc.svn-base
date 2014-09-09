/**
 * \file tcp_master.cc
 * \author Miguel Erazo
 * 
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

#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "../ipv4/icmpv4_session.h"
#include "ssf.h"
#include "os/ssfnet_types.h"
#include "../../net/host.h"

#include "tcp_master.h"
#include "agent/tcp_message.h"
#include "agent/tcp_session.h"
#include "proto/simple_socket.h"

#include "os/logger.h"
//#define LOG_WARN(X) std::cout<<"["<<__LINE__<<"]"<<X;
//#define LOG_DEBUG(X) std::cout<<"["<<__LINE__<<"]"<<X;

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(TCPMaster);

TCPSinkTimer::TCPSinkTimer(int tno, TCPSinkSession* agent) :
		Timer(agent->tcp_master), n_(tno), a_(agent)
		//XXX NATE Timer(agent->tcp_master), is_running(false), delay_timer_count(-1), n_(tno), a_(agent)
{
#ifdef COARSE_GRAINED_TIMERS
	//initialize timer
	//rxmit_timer_count=0;
	//is_running=FALSE;
#endif
}

void TCPSinkTimer::sched(double delay)
{
	//printf("TIMERS TCPSinkTimer: set a timer with this delay!:%g\n", delay);
#ifdef LINUX_TCP_MASTER_DEBUG_TIMER
	//printf("\n in SCHED SINK : rtx_timer delay=%g", delay);
#endif
	if(!isRunning()) {
#ifndef COARSE_GRAINED_TIMERS
		VirtualTime delay_(delay, VirtualTime::SECOND);
		set(delay_);
#else
		is_running = true;
#endif
	}
}

void TCPSinkTimer::resched(double delay)
{
	//printf("TIMERS TCPSinkTimer: set a timer with this delay!:%g\n", delay);
#ifdef LINUX_TCP_MASTER_DEBUG_TIMER
	//printf("\n in RESCHED SINK : rtx_timer delay=%g", delay);
#endif
	if(isRunning()) cancel();
#ifndef COARSE_GRAINED_TIMERS
	VirtualTime delay_(delay, VirtualTime::SECOND);
	set(delay_);
#else
	is_running = true;
#endif
}

int TCPSinkTimer::status()
{
	if(isRunning()) return TIMER_PENDING;
	else return TIMER_IDLE;
}

void TCPSinkTimer::callback()
{
	a_->timeout(0);
}

#ifdef COARSE_GRAINED_TIMERS

void TCPSinkTimer::cancel(){
	delay_timer_count=0;
	is_running = false;
}

bool TCPSinkTimer::isRunning(){
	return is_running;
}

void TCPSinkTimer::setCounter(double rtttimeout){
	delay_timer_count = rtttimeout;
}

double TCPSinkTimer::getCounter(){
	return delay_timer_count;// timer_count;
}
#endif


TCPSamplingTimer::TCPSamplingTimer(TCPSession *agent) :
		Timer(agent->tcpMaster), a_(agent) {}

void TCPSamplingTimer::resched(double delay)
{
	//printf("TIMERS TCPSinkTimer: set a timer with this delay!:%g\n", delay);
#ifdef LINUX_TCP_MASTER_DEBUG_SAMPLE
  //printf("\n in RESCHED tcp master: samp timer resched at for %g", delay);
#endif
  if(isRunning())  cancel();
  VirtualTime delay_(delay, VirtualTime::SECOND);
  set(delay_);
}

void TCPSamplingTimer::callback()
{
	a_->sample();
}

/* Linux TCP Master timer's related code */
//#ifdef COARSE_GRAINED_TIMERS
TCPMasterTimer::TCPMasterTimer(int type, TCPMaster* master) :
		Timer(master), n_(type), a_(master) {
}

void TCPMasterTimer::sched(double delay)
{
	if(!isRunning()) {
		//LOG_DEBUG("[" << a_->getNow().second() << "] schedule a timer at master" << endl);
		LOG_DEBUG("[] schedule a timer at master type=" << n_ << " delay=" << delay << endl);
		VirtualTime delay_(delay, VirtualTime::SECOND);
		set(delay_);
	}
}

void TCPMasterTimer::resched(double delay)
{
	//LOG_DEBUG("[" << a_->getNow().second() << "] reschedule a timer at master" << endl);
	LOG_DEBUG("[] reschedule a timer at master type=" << n_ << endl);
	if(isRunning()) cancel();
	VirtualTime delay_(delay, VirtualTime::SECOND);
	set(delay_);
//#endif
}

int TCPMasterTimer::status()
{
	if(isRunning()) return TIMER_PENDING;
	else return TIMER_IDLE;
}

void TCPMasterTimer::callback()
{
	LOG_DEBUG("[debug mastertimer] [" << a_->getUID() << "] [" << a_->getNow() << "] master callback called" << endl);
	a_->masterTimeout(n_);

	//schedule timer again
	if(n_ == SLOW_TIMER){
		sched(a_->getSlowTimeout());
	}
}

TCPMaster::TCPMaster() :
		ip_sess(0)
{
	fast_timeout = (double)DEFAULT_FAST_TIMER;
	slow_timeout = (double)DEFAULT_SLOW_TIMER;
	next_src_port = 2;
	msl            = VirtualTime(TCP_DEFAULT_MSL, VirtualTime::SECOND);

	master_fast_timer = 0;
}

TCPMaster::~TCPMaster(){}

void TCPMaster::init()
{
	// the same method at the parent class must be called
	ProtocolSession::init();

	bufferSession = NULL;
	bufferListeningPort = 0;

	LOG_DEBUG("---> master created IP=" << (uint32)inHost()->getDefaultIP() << "\n");

	if(!ip_sess) {
		ip_sess = inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_IPV4);
		//if(!ip_sess) LOG_ERROR("missing IP session.");
	}

	if(!ip_sess){
		LOG_ERROR("IP session is missing!"<<endl)
	}

	// Create and initialize slow master timer
	LOG_DEBUG("master_rtx_timer scheduled! slowtimeout=" << getSlowTimeout() << "\n");
	master_rtx_timer = new TCPMasterTimer(SLOW_TIMER, this);
	master_rtx_timer->sched(getSlowTimeout());
}

void TCPMaster::changeToConnected(TCPSession* sess){
	//Check for listening sessions
	TCPConnection* conn = sess->getTcpConn();

	TCP_LISTEN_SESSIONS_MAP::iterator listen_iter=
			listeningSessions.find(sess->getTcpConn()->dstPort);
	if(listen_iter != listeningSessions.end()) {
		listeningSessions.erase(listen_iter);
		//Insert this session into 'connectedSessions'
		connectedSessions.insert
			(SSFNET_MAKE_PAIR(SSFNET_MAKE_PAIR(conn->srcPort,
					SSFNET_MAKE_PAIR(conn->srcIP, conn->dstPort)), sess));

		//printf("inserting a conn with srcport=%d dstport=%d\n", conn->srcPort, conn->dstPort);

		//apply buffered session is exists
		if(bufferSession != NULL) {
			listeningSessions.insert(SSFNET_MAKE_PAIR(bufferListeningPort, bufferSession));
			//LOG_DEBUG("\t\t\t\tAPPLYING BUFFERED DATA...\n");
			for(TCP_LISTEN_SESSIONS_MAP::iterator listen_iter =	listeningSessions.begin();
					listen_iter != listeningSessions.end(); listen_iter++) {
				//LOG_DEBUG("\t\t\t\t\tLISTENING PORT 3 =" << listen_iter->first << "\n" << endl);
			}

			bufferSession = NULL;
			bufferListeningPort = 0;
		}
	} else LOG_DEBUG("Session not inserted properly in listening container!" << endl);
}

int TCPMaster::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
	//assert(packet);

	LOG_DEBUG("----------- in processack, [" << inHost()->getNow().second() << "] received in master=" << inHost()->getDefaultIP() <<
			" time=" << inHost()->getNow().second() << " has payload?" <<
			((TCPMessage*)msg)->hasPayload() << " srcport=" << ((TCPMessage*)msg)->getSrcPort() <<
			" dstport=" << ((TCPMessage*)msg)->getDstPort() << " seqno=" << ((TCPMessage*)msg)->getSeqno() <<
 			" ackno=" << ((TCPMessage*)msg)->getAckno() << endl);

	//assert(msg->getRawPointer());

	IPOptionToAbove* ops  = (IPOptionToAbove*)extra;
	TCPMessage* tcphdr = (TCPMessage*)msg;

	TCP_CONN_SESSIONS_MAP::iterator conn_iter =
			connectedSessions.find(SSFNET_MAKE_PAIR(tcphdr->getSrcPort(),
					SSFNET_MAKE_PAIR((uint32)ops->src_ip, tcphdr->getDstPort())));
	//LOG_DEBUG("---> src_port=" << tcphdr->src_port << " dst_port=" << tcphdr->dst_port << "\n");

	LOG_DEBUG("[" << inHost()->getNow().second() << "][debugemu]in master: ---> src_port=" << 
			tcphdr->getSrcPort() << " dst_port=" << tcphdr->getDstPort() <<
			" SRCIP=" << ops->src_ip << " seqno=" << tcphdr->getSeqno() << " ackno=" <<
			tcphdr->getAckno() << " sizeofmsg=" << tcphdr->size() << endl);

	DEBUG_CODE(
		char temp[1024];
        	snprintf(temp,1024,"---tcpmaster pop, seqno=, seqno=%u ackno=%u", tcphdr->getSeqno(), tcphdr->getAckno());
        	ACK_COUT(temp, this););

	if(conn_iter != connectedSessions.end()) {
		//printf("Master: new packet arrived for connected session in %s\n",
		//inHost()->nhi.toString());


	DEBUG_CODE(
		char temp4[1024];
                snprintf(temp4,1024,"---tcpmaster pop before TCP 3 seqno=%u ackno=%u size=%d", tcphdr->getSeqno(), tcphdr->getAckno(), (int)connectedSessions.size());
                ACK_COUT(temp4, this););


		(*conn_iter).second->receive(tcphdr, extra);
		return 0;
	} else {
		//printf("No connected session found!\n");
	}

	//for(TCP_LISTEN_SESSIONS_MAP::iterator listen_iter =	listeningSessions.begin();
	//		listen_iter != listeningSessions.end(); listen_iter++) {
		//LOG_DEBUG("LISTENING PORT=" << listen_iter->first << "\n" << endl);
	//}

	DEBUG_CODE(	
		char temp1[1024];
                snprintf(temp1,1024,"---tcpmaster pop before TCP 4, seqno=, seqno=%u ackno=%u", tcphdr->getSeqno(), tcphdr->getAckno());
                ACK_COUT(temp1, this););


	//Check for listening sessions
	 TCP_LISTEN_SESSIONS_MAP::iterator listen_iter=
			listeningSessions.find(tcphdr->getDstPort());
	//for(TCP_LISTEN_SESSIONS_MAP::iterator listen_iter =	listeningSessions.begin();
		//	listen_iter != listeningSessions.end(); listen_iter++){
		//LOG_DEBUG("listening port is: " << listen_iter->second->getTcpConn()->dstPort);
		if(listen_iter != listeningSessions.end()) {
			//Let's configure the session
			listen_iter->second->getTcpConn()->srcPort = tcphdr->getSrcPort();
			listen_iter->second->getTcpConn()->dstIP = (uint32)ops->dst_ip;
			listen_iter->second->getTcpConn()->srcIP = (uint32)ops->src_ip;

			DEBUG_CODE(
				char temp2[1024];
                        	snprintf(temp2,1024,"---tcpmaster pop before TCP 1, seqno=, seqno=%u ackno=%u", tcphdr->getSeqno(), tcphdr->getAckno());
                        	ACK_COUT(temp2, this););

			(*listen_iter).second->receive(tcphdr, extra);
		}
		else {
			if(tcphdr->getFlags() & TCPMessage::TCP_FLAG_SYN) {
				int app_type = ApplicationSession::getApplicationProtocolType(tcphdr->getDstPort());
				LOG_DEBUG("found no active connections or listening sessions! Automatically creating app_type="<<app_type<<", port="<<tcphdr->getDstPort()<<endl);
				ProtocolSession* sess = inHost()->sessionForNumber(app_type);
				if(sess) {
					TCP_LISTEN_SESSIONS_MAP::iterator listen_iter= listeningSessions.find(tcphdr->getDstPort());
					if(listen_iter != listeningSessions.end()) {
						//Let's configure the session
						listen_iter->second->getTcpConn()->srcPort = tcphdr->getSrcPort();
						listen_iter->second->getTcpConn()->dstIP = (uint32)ops->dst_ip;
						listen_iter->second->getTcpConn()->srcIP = (uint32)ops->src_ip;
						
						DEBUG_CODE(
							char temp3[1024];
        						snprintf(temp3,1024,"---tcpmaster pop before TCP 2 , seqno=, seqno=%u ackno=%u", tcphdr->getSeqno(), tcphdr->getAckno());
        						ACK_COUT(temp3, this););
						
						(*listen_iter).second->receive(tcphdr, extra);
					}
					else {
						LOG_WARN("Got a packet for an unknown application, e.g., iperf to a simulated host\n")
					}
				}
				else {
					LOG_ERROR("should never see this!\n");
				}
			}
			else {
				std::cout<<"Encountered stray packet from a closed session. pkt size="<<msg->getPacket()->size()<<"\n";
			}
		}
	return 0;
}



TransportSession* TCPMaster::createActiveSession(SimpleSocket* sock_,
		int local_port,
		IPAddress remote_ip, int remote_port) {
	// Create a connection object (XXX have to change the name from TCPConnection to Connection)
	TCPConnection* conn = new TCPConnection(local_port,remote_port,inHost()->getDefaultIP(),remote_ip);
	/*std::cout<<"XXX NATE "<<__FILE__<<":"<<__LINE__<<" -- "<<
			" src_port="<<conn->srcPort<<
			", dst_port="<<conn->dstPort<<
			", src_ip="<<conn->srcIP<<
			", dst_ip="<<conn->dstIP<<endl;*/

	LOG_DEBUG("[debug sessionid]---> creating an active session IP=" << inHost()->getDefaultIP() << " srcport=" <<
			conn->srcPort << " dstPort=" << conn->dstPort << endl);

	std::cout << "---> creating an active session, IP=" << inHost()->getDefaultIP() << " srcport=" << conn->srcPort << 
		" dstport=" << conn->dstPort << endl;

	TCPSession* newSession = new TCPSession(this, sock_, conn);
	newSession->init();
	newSession->setSink(new TCPSinkSession(this, sock_, conn, newSession));

	TCP_CONN_SESSIONS_MAP::iterator connected_iter=
		connectedSessions.find(SSFNET_MAKE_PAIR(remote_port, SSFNET_MAKE_PAIR(remote_ip, conn->srcPort)));
	if(connected_iter != connectedSessions.end()){
		//This connection exists
		LOG_WARN("This connection already exists!" << endl);
	} else {
		//LOG_DEBUG("---> Inserting new session into the map \n");
		connectedSessions.insert(SSFNET_MAKE_PAIR(SSFNET_MAKE_PAIR(remote_port,
				SSFNET_MAKE_PAIR(remote_ip, conn->srcPort)) , newSession));
	}

	return newSession;
}

TransportSession* TCPMaster::createListeningSession(SimpleSocket* sock,
		IPAddress listen_ip, int listen_port)
{
	//LOG_DEBUG("\t CREATING LISTENING SESSION port=" << listening_port << "\n" << endl);

	//Create an object containing data of the present connection
	TCPConnection* conn = new TCPConnection(-1,listen_port,IPADDR_INADDR_ANY,listen_ip);
	/*std::cout<<"XXX NATE "<<__FILE__<<":"<<__LINE__<<" -- "<<
			" src_port="<<conn->srcPort<<
			", dst_port="<<conn->dstPort<<
			", src_ip="<<conn->srcIP<<
			", dst_ip="<<conn->dstIP<<endl;*/

	assert(conn);

	TCPSession* newSession = new TCPSession(this, sock, conn);
	assert(newSession);
	LOG_DEBUG("---> creating a listening session IP=" << inHost()->getDefaultIP() << " srcport=" <<
				conn->srcPort << " dstPort=" << conn->dstPort << endl);
	newSession->init();
	newSession->setSink(new TCPSinkSession(this, sock, conn, newSession));
	assert(newSession->getSink());

	if(conn->srcIP == IPADDR_INADDR_ANY ) {
	    if(listeningSessions.find(listen_port) != listeningSessions.end()){
	    	//LOG_DEBUG("BUFFERING SESSION!\n");
	    	bufferSession = newSession;
	    	bufferListeningPort = listen_port;
	    } else {
	    	listeningSessions.insert(SSFNET_MAKE_PAIR(listen_port, newSession));
	    	//LOG_DEBUG("the number of listening sessions is" << listeningSessions);
	    }
	} else {
		// implement this case if necessary
		LOG_ERROR("miguel's code makes little sense....\n");
	}

	return newSession;
}

int TCPMaster::push(ProtocolMessage* msg, ProtocolSession* hi_sess,
		    void* extinfo, size_t extinfo_size)
{
	IPPushOption* inf = (IPPushOption*) extinfo;
	IPv4Message* iph;
	//printf("[%g][%llu]getting uids: master tx a pkt!\n", this->inHost()->getNow().second(), this->getUID());
	
	DEBUG_CODE(
		TCPMessage* tcphdr = (TCPMessage*)msg;

		LOG_DEBUG("[debug interactions] [" << inHost()->getNow().second() << "][debugemu]in master: ---> sending from master, seqno=" <<
                        tcphdr->getSeqno() << " sizeofmsg=" << tcphdr->size() << endl);

		char temp[1024];
        	snprintf(temp,1024,"tcpmaster push, seqno=, seqno=%u ackno=%u", tcphdr->getSeqno(), tcphdr->getAckno());
        	ACK_COUT(temp, this););


	if(inf && msg){
	//	iph = new IPv4Message(inf->src_ip, inf->dst_ip, SSFNET_PROTOCOL_TYPE_TCP);
		iph = new IPv4Message(inf->src_ip, inf->dst_ip, SSFNET_PROTOCOL_TYPE_TCP, DEFAULT_IPV4_HDRLEN, 0, 5);	
	
		/*
		//IPv4Message(IPAddress src, IPAddress dst, int proto, int hdrlen = DEFAULT_IPV4_HDRLEN,
	        //int tos = 0, int ident=0, int offset=0, int ttl = DEFAULT_IPV4_TTL);
		*/

		//LOG_DEBUG("****************	ACKNO SRC=" << inf->src_ip << " DST=" << inf->dst_ip << " seqno=" <<
		//	((TCPMessage*)msg)->getSeqno() << " ackno=" << ((TCPMessage*)msg)->getAckno() << endl);
		iph->carryPayload(msg);
		LOG_DEBUG("***debugip sending to ip..." << iph->getLen() << endl);
		ip_sess->push(iph, this);
	}
	return 1;
}

void TCPMaster::eraseSession(TCPSession* sess){
	//LOG_WARN("in Master a session will be erased! sess=" << sess << " at" << this->getNow().second() << endl);

	//traverse the listening sessions structure
	for(TCP_LISTEN_SESSIONS_MAP::iterator iter = listeningSessions.begin(); iter!=listeningSessions.end(); iter++){
		LOG_DEBUG("this sess=" << iter->second << endl);
		if(iter->second == sess){
			LOG_WARN("1 deleting session..." << sess << endl);
			listeningSessions.erase(iter->first);
			delete sess;
			return;
		}
	}

	//XXX chnage to find
	//traverse the connected sessions structure
	for(TCP_CONN_SESSIONS_MAP::iterator iter = connectedSessions.begin(); iter!=connectedSessions.end(); iter++){
		//LOG_DEBUG("port1=" << iter->first.first << " ip=" << iter->first.second.first << " port2=" << iter->first.second.second <<
			//" session=" << iter->second << " provided-session=" << sess << endl);
		if(iter->second == sess){ // we found the session
			LOG_DEBUG("this sess=" << iter->second << endl);
			LOG_DEBUG("2 deleting session..." << sess << endl);
			connectedSessions.erase(iter->first);
			delete sess;
			return;
		}
	}

	LOG_WARN("A session scheduled for deletion was not found: possibly because it was erased before because flow completed" << endl);
}

//Timers code
TCPTimer::TCPTimer(int tno, TCPSession* agent) :
		Timer(agent->getMaster()), n_(tno), a_(agent)
{
    //initialize timer
	rxmit_timer_count=0;
	is_running = false;
}

void TCPTimer::sched(double delay)
{
	if(!isRunning()) {
		VirtualTime delay_(delay, VirtualTime::SECOND);
		set(delay_);
		is_running = true;
	}
}

void TCPTimer::resched(double delay)
{
	if(isRunning()) cancel();
#ifndef COARSE_GRAINED_TIMERS
	VirtualTime delay_(delay, VirtualTime::SECOND);
	set(delay_);
#else
	if(n_ == TCP_TIMER_CONNTERM){
		VirtualTime delay_(delay, VirtualTime::SECOND);
		set(delay_);
	}
#endif
	is_running = true;
}

int TCPTimer::status()
{
	if(isRunning()) return TIMER_PENDING;
	else return TIMER_IDLE;
}

void TCPTimer::callback()
{
	if(n_ == TCP_TIMER_CONNTERM){
		LOG_DEBUG("[" << a_->sessionNumber << "] at [" << a_->getMaster()->getNow().second() << "] timeout at master" << endl);
		LOG_DEBUG("timeout at master\n");
		a_->timeout(n_);
	}
#ifndef COARSE_GRAINED_TIMERS
	a_->timeout(n_);
#endif
}

#ifdef COARSE_GRAINED_TIMERS

void TCPTimer::cancel(){
	rxmit_timer_count=0;
	is_running = false;
}

bool TCPTimer::isRunning(){
	return is_running;
}

void TCPTimer::setCounter(double rtttimeout){
	if(a_->sessionNumber == 9 && n_ == TCP_TIMER_RTX)
		LOG_DEBUG("\t\t\t***THIS*** setting to "<<rtttimeout<<endl);
	rxmit_timer_count = rtttimeout;
}

double TCPTimer::getCounter(){
	return rxmit_timer_count;
}
#endif

#ifdef COARSE_GRAINED_TIMERS
void TCPMaster::masterTimeout(int type)
{
	LOG_DEBUG("master timeout at " << this->getNow() << " type=" << type << endl);
	if(type == SLOW_TIMER) {
		// SLOW TIMER
		// Go through all sender connections
		for(TCP_CONN_SESSIONS_MAP::iterator master_iter = connectedSessions.begin();
				master_iter!=connectedSessions.end(); master_iter++){
			(*master_iter).second->slowTimeout();
		}
	} else{
		// FAST TIMER
		// Go through all receiver sessions that need a fast timer.
		// By default all our session use delayed acknowledgments so we
		// just traverse the connectedSessions data structure

		LOG_DEBUG("[debug mastertimer] [" << getUID() << "] fast timeout at " << this->getNow() << " type=" << type << " schedule with " <<
				getFastTimeout() << endl);

		for(TCP_CONN_SESSIONS_MAP::iterator master_iter = connectedSessions.begin();
				master_iter!=connectedSessions.end(); master_iter++){
			(*master_iter).second->slowTimeout();
		}
		master_fast_timer->resched(getFastTimeout());
	}
}

void TCPMaster::enableFastTimeout(TCPSinkSession* session){
	if(!master_fast_timer){
		//create and schedule a new timer
		master_fast_timer = new TCPMasterTimer(FAST_TIMER, this);
		LOG_DEBUG("[debug mastertimer] [ " << this->inHost()->getNow().second() << " ] enable fast timeout, "
				" getFastTimeout=" << getFastTimeout() << endl);
		master_fast_timer->sched(getFastTimeout());
	}
	fast_timeout_sessions.insert(session);
}

void TCPMaster::disableFastTimeout(TCPSinkSession* session){
	LOG_DEBUG("calling disableFastTimeout" << endl);
	fast_timeout_sessions.erase(session);
}

#endif

};
};
