/*
 * \file cbr.cc
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
#include <stdio.h>
#include <stdlib.h>

#include "os/timer.h"
#include "net/host.h"
#include "os/protocol_session.h"
#include "proto/udp/test/cbr.h"
#include "proto/udp/test/cbr_traffic.h"
#include "os/logger.h"
#include "os/virtual_time.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(CBR);

CBR::CBR() : udp_master(0), simple_passive_sock(0), starting_time(0) {}

CBR::~CBR(){
}

void CBR::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac)
{
	//getTrafficType: CBR
	//getConfigType: ConfigType for
	//this entity; A ConfigType describes the properties/state/structure of entities of this type

	LOG_DEBUG("CBR TRAFFIC started" << endl);

	if(starting_time == 0) {
		starting_time = inHost()->getNow().second();
		LOG_DEBUG("Starting time set to " << starting_time.second());
	}

	switch(evt->getTrafficType()->getConfigType()->type_id)
	{
	case UDP_TRAFFIC:
	{
		LOG_DEBUG("starting cbr!\n");
		Host* host = inHost();
		assert(host);
		int dst_port_;
		bool emulated_ = false;
		uint32_t bytes_to_send_ = 0;
		SimpleSocket* sock_ = 0;
		float count_ = 0;
		float interval_ = 0;
		CBRFlow* flow_ = 0;

		if(!udp_master) {
			udp_master = (UDPMaster*)inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_UDP);
			if(!udp_master) LOG_ERROR("missing UDP session.");
		}

		UDPTraffic* tt = SSFNET_STATIC_CAST(UDPTraffic*,evt->getTrafficType());

		dst_port_ = tt->getDstPort();
		IPAddress myIP = inHost()->getDefaultIP();
		emulated_ = tt->getToEmulated();
		bytes_to_send_ = tt->getBytesToSendEachInterval();
		count_ = tt->getCount();
		interval_ = tt->getInterval();

		LOG_DEBUG("dst_port_=" << dst_port_ << " myIP=" << myIP << " emulated_=" <<
				emulated_ << " bytes_to_send_=" << bytes_to_send_ << " count_=" << count_ <<
				" interval_=" << interval_ << endl);

		if(ipaddr == IPAddress::IPADDR_INVALID) {
			LOG_ERROR("Did not expect to get IPAddress::IPADDR_INVALID!"<<endl)
		}

		LOG_DEBUG("---> CREATING A NEW SOCKET FOR CBR APP [" << inHost()->getDefaultIP() << "]" << endl);
		sock_ = new SimpleSocket(udp_master, this, ipaddr, dst_port_);
		assert(sock_);

		flow_ = new CBRFlow(this, sock_, bytes_to_send_, count_, interval_, dst_port_, emulated_);

		send(flow_);

		LOG_DEBUG("emulated=" << emulated_ << endl);
	}
	break;
	case PPBP_TRAFFIC:
		{
			LOG_DEBUG("starting ppbp!\n");
			Host* host = inHost();
			assert(host);
			int dst_port_;
			uint32_t bytes_to_send_ = 0;
			SimpleSocket* sock_ = 0;
			float num_of_intervals_ = 0;
			float interval_ = 0;
			CBRFlow* flow_ = 0;

			if(!udp_master) {
				udp_master = (UDPMaster*)inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_UDP);
				if(!udp_master) LOG_ERROR("missing UDP session.");
			}

			PPBPTraffic* tt = SSFNET_STATIC_CAST(PPBPTraffic*,evt->getTrafficType());

			dst_port_ = tt->getDstPort();
			IPAddress myIP = inHost()->getDefaultIP();
			//get the following values from startPpbpTrafficEvent
			bytes_to_send_ = SSFNET_DYNAMIC_CAST(PPBPStartTrafficEvent*,evt)->getBytesToSendPerIntv();

			num_of_intervals_ = SSFNET_DYNAMIC_CAST(PPBPStartTrafficEvent*,evt)->getNumOfIntervals();
			interval_ = SSFNET_DYNAMIC_CAST(PPBPStartTrafficEvent*,evt)->getInterval();

			if(ipaddr == IPAddress::IPADDR_INVALID) {
				LOG_ERROR("Did not expect to get IPAddress::IPADDR_INVALID!"<<endl)
			}

			LOG_DEBUG("---> CREATING A NEW SOCKET FOR CBR APP [" << inHost()->getDefaultIP() <<"]" << endl);
			sock_ = new SimpleSocket(udp_master, this, ipaddr, dst_port_);
			assert(sock_);

			flow_ = new CBRFlow(this, sock_, bytes_to_send_, num_of_intervals_, interval_, dst_port_, false);

			send(flow_);
		}
		break;
	default:
		LOG_ERROR("Invalid traffic type...." << endl)
	}
	evt->free();
}

void CBR::send(CBRFlow* flow_)
{
	LOG_DEBUG("CBR: send called! emulated" << flow_->getEmulated() << endl);
	uint32_t bytes_to_send = flow_->getBytesPerInterval();
	if(flow_->getEmulated()) {
		char* buffer;
		buffer = new char[flow_->getBytesPerInterval()];
		for(int i=0; i<(int)bytes_to_send; i++){
			buffer[i] = 'U';
		}
		unshared.total_bytes_sent.write(unshared.total_bytes_sent.read() + bytes_to_send);
		LOG_DEBUG("CBR: sending emu " << bytes_to_send << " bytes" << " from " <<
				inHost()->getDefaultIP() << " sent already " << unshared.total_bytes_sent.read() <<
				" at" << inHost()->getNow().second() << endl);
		flow_->getSocket()->send(strlen(buffer), (byte*) buffer);
	} else {
		unshared.total_bytes_sent.write(unshared.total_bytes_sent.read() + bytes_to_send);
		LOG_DEBUG("CBR: sending simu " << bytes_to_send << " bytes" << " from " <<
				inHost()->getDefaultIP() << " sent already " << unshared.total_bytes_sent.read() <<
				" at" << inHost()->getNow().second() << endl);
		flow_->getSocket()->send(bytes_to_send);
	}

	// Schedule a timer now
	flow_->decreaseNumOfIntervals();
	float remaining_intervals = flow_->getNumOfIntervals();
	LOG_DEBUG("CBR: remaining intervals " << remaining_intervals << endl);
	if(remaining_intervals){
		LOG_DEBUG("CBR: schedule a timer in " << flow_->getIntervalDuration() << endl)
		flow_->sched(flow_->getIntervalDuration());
		if(remaining_intervals < 1){
			flow_->setBytesPerInterval(flow_->getBytesPerInterval()*remaining_intervals);
			if(flow_->getBytesPerInterval()<=0){
				flow_->cancel();
				delete flow_;
			}
		}
	}
	else {
		LOG_DEBUG("time="<<this->inHost()->getNow().second()<<", CBR: No more data to send, lets close the socket and the session"<<endl);
		//float sess_duration = flow_->getIntervalDuration() + this->inHost()->getNow().second();
		flow_->cancel();
		delete flow_;
	}
}

void CBR::init()
{
	// Start listening from the very beginning
	startPassiveListening();
}

void CBR::startPassiveListening(){
	if(!udp_master) {
		udp_master = (UDPMaster*)inHost()->sessionForNumber(SSFNET_PROTOCOL_TYPE_UDP);
		if(!udp_master){
			LOG_ERROR("missing UDP session" << endl);
		}
	}
	LOG_DEBUG("udp_master is configured" << endl);
	//Create s new socket to begin listening in the specified port
	simple_passive_sock = new SimpleSocket(udp_master, this, shared.listening_port.read());
}

void CBR::wrapup(){
	if(simple_passive_sock)
		delete simple_passive_sock;
}

int CBR::push(ProtocolMessage* msg, ProtocolSession* hisess, void* extra)
{
	LOG_ERROR("ERROR: a message is pushed down to the CBR session"
			" from protocol layer above; it's impossible" << endl);
	return 0;
}

int CBR::pop(ProtocolMessage* msg, ProtocolSession* losess, void* extra)
{
	SimpleSocket* sock = SSFNET_STATIC_CAST(SimpleSocket*,extra);
	uint32 status_ = sock->getStatus();

	if(status_ & SimpleSocket::SOCKET_CONNECTED) {
		startPassiveListening();
	}
	if(status_ & SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED) {
		DataMessage* dm = SSFNET_DYNAMIC_CAST(DataMessage*,msg);
		// This event is to keep track of the bytes received so far by this applications
		//LOG_DEBUG("bytes received in client=" << dm->size() << "\n" << endl);
		unshared.total_bytes_received.write(unshared.total_bytes_received.read() + dm->size());
		LOG_DEBUG("BYTES RECEIVED SO FAR IN CBR CLIENT [" << inHost()->getDefaultIP() << "]" <<
				unshared.total_bytes_received.read() << endl);
	}
	return 0;
}

CBRFlow::CBRFlow(CBR* cbr_, SimpleSocket* sock, uint32_t bytes_per_intv, float num_of_intv,
			float intv_dur, int d_port, bool to_emulated) : Timer(cbr_)
{
	LOG_DEBUG("CBR: a new flow has been created" << endl);
	cbr = cbr_;
	socket = sock;
	bytes_per_interval = bytes_per_intv;
	num_of_intervals = num_of_intv;
	intv_duration = intv_dur;
	dst_port = d_port;
	emulated = to_emulated;
}

CBRFlow::~CBRFlow(){
	this->getSocket()->disconnect();
}

void CBRFlow::sched(double delay){
	LOG_DEBUG("CBR: schedule a time with " << delay << endl);
	VirtualTime delay_(delay, VirtualTime::SECOND);
	set(delay_);
}

void CBRFlow::resched(double delay){
	LOG_DEBUG("CBR: schedule a time with " << delay << endl);
	VirtualTime delay_(delay, VirtualTime::SECOND);
	set(delay_);
}

void CBRFlow::callback(){
	LOG_DEBUG("CBR: the timer has expired " << delay << endl);
	cbr->send(this);
}

SimpleSocket* CBRFlow::getSocket(){
	return socket;
}

uint32_t CBRFlow::getBytesPerInterval(){
	return bytes_per_interval;
}

void CBRFlow::setBytesPerInterval(uint32_t bytes_per_intv){
	bytes_per_interval = bytes_per_intv;
}

float CBRFlow::getNumOfIntervals() {
	return num_of_intervals;
}

float CBRFlow::getIntervalDuration(){
	return intv_duration;
}

int CBRFlow::getDstPort() {
	return dst_port;
}

bool CBRFlow::getEmulated() {
	return emulated;
}

void CBRFlow::decreaseNumOfIntervals() {
	if(num_of_intervals > 1){
		num_of_intervals = num_of_intervals - 1;
	}else{
		num_of_intervals = 0;
	}
}

SSFNET_REGISTER_APPLICATION_SERVER(5001,CBR);

}; // namespace ssfnet
}; // namespace prime
