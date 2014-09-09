/**
 * \file vpn_device.cc
 * \brief Implemenation of OpenVPNDevice
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
 *
 */

#ifdef SSFNET_EMULATION

#define EMU_BUFSIZ 102400 // 100 KB

#include "os/emu/vpn_device.h"
#include "proto/emu/emulation_protocol.h"
#include "os/io_mgr.h"
#include "os/logger.h"
#include "proto/ipv4/ipv4_message.h"

#include <stdio.h>
#include <netdb.h>
#include <sys/select.h>
#include <sys/types.h>
#include <unistd.h>

//#define LOG_DEBUG(X) std::cout<<X<<endl;
//#define LOG_WARN(X) std::cout<<X<<endl;

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(OpenVPNDevice);

OpenVPNDevice::OpenVPNDevice():recv_buf(0),send_buf(0),recv_buf_size(0),send_buf_size(0) {
}

OpenVPNDevice::~OpenVPNDevice(){
	if(recv_buf)
		delete[] recv_buf;
	if(send_buf)
		delete[] send_buf;
}

void OpenVPNDevice::init(){
	BlockingEmulationDevice::init();
	if(static_gateways) {
		for(ServerPortPairList::iterator i=static_gateways->begin(); i!=static_gateways->end();i++) {
			gateways.push_back(new OpenVpnServer((*i).first,(*i).second));
		}
	}
	LOG_WARN("OpenVPNDevice::init() input_channel="<<input_channel<<", output_channel="<<output_channel<<endl);
}

void OpenVPNDevice::wrapup() {
	BlockingEmulationDevice::wrapup();
}

void OpenVPNDevice::registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto) {
	ssfnet_throw_exception(SSFNetException::other_exception,"Cannot call registerProxiedEmulationProtocol() on an OpenVPNDevice!");
}

void OpenVPNDevice::registerEmulationProtocol(EmulationProtocol* emu_proto) {
	if(!OpenVPNEmulation::getClassConfigType()->isSubtype(emu_proto->getTypeId())) {
		LOG_ERROR("the openvpndevice only supports emulation protocols of type OpenVPNEmulation! Found "<<emu_proto->getTypeName()<<endl);
	}
	LOG_DEBUG("registerEmulationProtocol "<<emu_proto->getUName()<<" with src ip "<<emu_proto->getInterface()->getIP()<<endl);
	if(!ip2emuproto.insert(SSFNET_MAKE_PAIR(emu_proto->getInterface()->getIP(),new EmuProtoVpnServerPair(emu_proto))).second) {
		LOG_ERROR("Duplicate ip_address '"<< emu_proto->getInterface()->getIP()<<"'detected"<<endl);
	}
	emu_proto->setEmulationDevice(this);
}

void OpenVPNDevice::exportPacket(Interface* iface, Packet* pkt) {
	//LOG_DEBUG("exporting packet from "<<iface->getUName()<<", ip="<<iface->getIP()<<endl);
	EmulationEvent* evt= new EmulationEvent(iface, pkt, false);
	output_channel->write(evt);
}

void OpenVPNDevice::handleProxiedEvent(EmulationEvent* evt) {
	LOG_WARN("OpenVPN devices should not be proxying evts!"<<endl);
	evt->free();
}

EmulationProtocol* OpenVPNDevice::ip2EmulationProtocol(IPAddress& ip) {
	EmuProtoVpnServerPairMap::iterator iter = ip2emuproto.find(ip);
	if(iter == ip2emuproto.end()) {
		LOG_WARN("asked to handle the departure of an unknown ip "<<ip);
		return 0;
	}
	return (*iter).second->proto;
}

int OpenVPNDevice::safe_send(int s, char* buf, unsigned int bufsiz)
{
	int sent_len = 0;
	while(sent_len < (int)bufsiz){
		int ret = send(s, buf+sent_len, bufsiz-sent_len, 0/*MSG_NOSIGNAL*/);
		if(ret <= 0) return -1;
		sent_len += ret;
	}
	return (int)sent_len;
}

int OpenVPNDevice::safe_receive(int s, char* buf, unsigned int bufsiz)
{
	int rcvd_len = 0;
	while(rcvd_len < (int)bufsiz){
#if defined(PRIME_SSF_MACH_X86_CYGWIN) || defined(PRIME_SSF_MACH_X86_64_CYGWIN)
		// windows does not support MSG_WAITALL
		int ret = recv(s, buf+rcvd_len, bufsiz-rcvd_len, 0);
#else
		int ret = recv(s, buf+rcvd_len, bufsiz-rcvd_len, MSG_WAITALL);
#endif
		if(ret <= 0) return -1;
		rcvd_len += ret;
	}
	return rcvd_len;
}


int OpenVPNDevice::send_ip_addresses(int insock)
{
	int nnics=0;
	uint32 myipaddr;
	for(EmuProtoVpnServerPairMap::iterator i = ip2emuproto.begin();
			i!= ip2emuproto.end(); i++) {
		Host* host = SSFNET_DYNAMIC_CAST(Host*,(*i).second->proto->getParent()->getParent());
		ChildIterator<Interface*> si = host->getInterfaces();
		while (si.hasMoreElements()) {
			Interface* nic = si.nextElement();
			nic->init();
			IPAddress nicip = nic->getIP();
			if (nicip != IPAddress::IPADDR_INVALID) {
				// send the interface addresses in network byte order
				myipaddr = htonl((uint32)nicip);
				LOG_DEBUG("send ip "<<nicip<<" to the simulation gateway."<<endl);
				if(safe_send(insock, (char*)&myipaddr, sizeof(uint32)) != sizeof(uint32))
					return 1;
				nnics++;
			}
		}
	}

	// terminate with a zero
	myipaddr = 0;
	if(safe_send(insock, (char*)&myipaddr, sizeof(uint32)) != sizeof(uint32)) {
		return 2;
	}
	LOG_DEBUG("Sent "<<nnics<<" ip addresses to the simulation gateway."<<endl);
	return 0;
}

void OpenVPNDevice::reader_thread() {
	//connect to gateways
	for(OpenVpnServerVector::iterator i=gateways.begin(); i!=gateways.end();i++) {
		LOG_DEBUG("[reader]Connect to gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
		(*i)->in=socket(AF_INET, SOCK_STREAM, 0);
		if((*i)->in == -1) {
			LOG_WARN("Reader thread cannot create socket to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			continue;
		}

		struct hostent* hent = gethostbyname((*i)->host.c_str());
		if(!hent) {
			LOG_WARN("Reader thread cannot get the host info of simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			close((*i)->in);
			(*i)->in=-1;
			continue;
		}

		sockaddr_in addr;
		addr.sin_family = AF_INET;
		addr.sin_port = htons((*i)->port);
		addr.sin_addr = *(struct in_addr*)*hent->h_addr_list;
		LOG_DEBUG("fd is " << (*i)->in << " dest=" << (*i)->host.c_str() << " port=" << (*i)->port << " dest2=" <<
                         endl);
		if(connect((*i)->in, (struct sockaddr*)&addr, sizeof(addr)) != 0) {
			LOG_WARN("Reader thread cannot connect to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			close((*i)->in);
			(*i)->in=-1;
			continue;
		}

		// register this thread as a reader
		uint8_t type = OpenVPNDevice::READER;
		if(safe_send((*i)->in, (char*)&type, sizeof(type)) != sizeof(type)) {
			LOG_WARN("Reader thread cannot send connection type to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			close((*i)->in);
			(*i)->in=-1;
			continue;
		}

		// send the ip addresses of hosts accepting emulation packets
		if(send_ip_addresses((*i)->in)) {
			LOG_WARN("Reader thread cannot send ip list to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			close((*i)->in);
			(*i)->in=-1;
			continue;
		}
		LOG_DEBUG("[reader]Established connection to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
	}

	if(!recv_buf) {
		recv_buf = new char[EMU_BUFSIZ];
		assert(recv_buf);
		recv_buf_size=EMU_BUFSIZ;
	}
	int idx;
	EmuProtoVpnServerPair* emuProtoVpnServerPair;

	// keep receiving emulation packets forever
	for (;!stop;) {
		fd_set fdset;
		FD_ZERO(&fdset);

		int max_fd = -1;
		for(idx=0; idx<(int)gateways.size(); idx++) {
			if(gateways[idx]->in >= 0) {
				FD_SET(gateways[idx]->in, &fdset);
				if(max_fd < gateways[idx]->in)
					max_fd = gateways[idx]->in;
			}
		}
		//no valid sockets!
		if(max_fd < 0)  {
			LOG_WARN("The reader thread has no valid sockets!"<<endl);
			break;
		}
		LOG_DEBUG("Waiting for pkts to import"<<endl)
		int ret = select(max_fd+1, &fdset, NULL, NULL, NULL);
		if(ret <= 0){
			//all the sockets in the descriptor set were closed!
			break;
		}

		for(idx=0; idx<(int)gateways.size() && ret>0; idx++) {
			if(gateways[idx]->in >= 0 && FD_ISSET(gateways[idx]->in, &fdset)) {
				//the socket is ready
				ret--;

				// get the leading ip address of the virtual host
				uint32 ipaddr;
				if(safe_receive(gateways[idx]->in, (char*)&ipaddr, sizeof(uint32)) != sizeof(uint32)) {
					LOG_DEBUG("Reader thread cannot get leading ip address from simulation gateway: "<<gateways[idx]->host<<":"<<gateways[idx]->port<<endl);
					close(gateways[idx]->in);
					gateways[idx]->in = -1;
					continue;
				}
				ipaddr = ntohl(ipaddr);

				if(safe_receive(gateways[idx]->in, recv_buf, sizeof(IPv4RawHeader)) != sizeof(IPv4RawHeader)) {
					LOG_DEBUG("Reader thread cannot get ip header from simulation gateway: "<<gateways[idx]->host<<":"<<gateways[idx]->port<<endl);
					close(gateways[idx]->in);
					gateways[idx]->in = -1;
					continue;
				}
				IPv4RawHeader* iph = (IPv4RawHeader*)recv_buf;
				uint16_t size = iph->getLen();
				LOG_DEBUG("[reader]incomming packet size="<<size<<endl);
#ifdef SSFNET_EMU_TIMESYNC
				// the send time is appended to the packet, but the size is not
				// reflected accordingly
				size += sizeof(int)*2;
#endif
				if(safe_receive(gateways[idx]->in, &recv_buf[sizeof(IPv4RawHeader)],
						size-sizeof(IPv4RawHeader)) != (int)(size-sizeof(IPv4RawHeader))) {
					LOG_DEBUG("Reader thread cannot get ip payload from simulation gateway: "<<gateways[idx]->host<<":"<<gateways[idx]->port<<endl);
					close(gateways[idx]->in);
					gateways[idx]->in = -1;
					continue;
				}

				// update the ip to socket table
				EmuProtoVpnServerPairMap::iterator i2i = ip2emuproto.find(ipaddr);
				if(i2i != ip2emuproto.end()) {
					emuProtoVpnServerPair=i2i->second;
					//LOG_DEBUG("updated ip->server map "<<emuProtoVpnServerPair->proto->getInterface()->getIP()<<"-->"<<gateways[idx]<<endl);
					emuProtoVpnServerPair->server=gateways[idx];
				}
				else {
					LOG_WARN("Received a packet destined for a host("<< IPAddress(ipaddr)<<
							") which is not registered as emulated!Dropping packet."<<endl);
					continue;
				}

				if(iph->getTOS() == 1 && iph->getProto() == 255) {
					LOG_DEBUG("Received connection signal for client "<<IPAddress(ipaddr)<<endl);
					continue;
				} else if(iph->getTOS() == 2 && iph->getProto() == 255) {
					LOG_DEBUG("Received disconnection signal for client "<<IPAddress(ipaddr)<<endl);
					emuProtoVpnServerPair->server=0;
					continue;
				}

				// we need to copy the message to a new buffer (the receive
				// buffer will be reused for next message)
				char* buf = new char[size];
				assert(buf);
				memcpy(buf, recv_buf, size);
				iph = (IPv4RawHeader*)buf; // update the ip header pointer

#ifdef SSFNET_EMU_TIMESYNC
				size -= sizeof(int)*2;
#endif

#ifdef SSFNET_EMU_TIMESYNC
				//XXX old code which is not ported!
				LOG_ERROR("Need to finish porting SSFNET_EMU_TIMESYNC"<<endl);
				if(iph->get_proto() == 254) {
					struct timeval t3;
					gettimeofday(&t3, 0);
					int* tv = (int*)&buf[iph->header_length()];
					int t1s = ntohl(tv[0]);
					int t1u = ntohl(tv[1]);
					tv = (int*)&buf[iph->header_length()+EMU_TIMESYNC_MSGLEN];
					int t2s = ntohl(tv[0]);
					int t2u = ntohl(tv[1]);
					double drift = (EMU_TIMESYNC_DIFF(t2s, t2u, t1s, t1u)-
							EMU_TIMESYNC_DIFF(t3.tv_sec, t3.tv_usec, t2s, t2u))/2;
					/*if(iothreads->clock_drift[idx] != 0)
		    iothreads->clock_drift[idx] = (iothreads->clock_drift[idx]+drift)/2;
		    else*/
					iothreads->clock_drift[idx] = drift;
					delete[] buf;
					/*EMU_DUMP*/(printf("IOThreads::read_message(): clock synchronization: drift=%lf sec\n",
							iothreads->clock_drift[idx]));
					continue;
				}
#endif

				LOG_DEBUG("import pkt, leading ip="<<IPAddress(ipaddr)<<
						", src="<<IPAddress(iph->getSrc())<<", dst="<<IPAddress(iph->getDst())<<endl);

				// create the event (the new buffer is handed off to the event
				// as the payload) and pass it subsequently to the simulator
				UID_t dst_uid=getCommunity()->synchronousNameResolution(IPAddress(iph->getDst()));
				Packet* pkt=new Packet(SSFNET_PROTOCOL_TYPE_IPV4,(prime::ssfnet::byte*)buf,size,0,dst_uid);
				EmulationEvent* evt = 0;
				if(!dst_uid) {
					evt = new EmulationEvent(SSFNET_DYNAMIC_CAST(Interface*,emuProtoVpnServerPair->proto->getParent()), pkt, true);
				}
				else {
					evt = new EmulationEvent(SSFNET_DYNAMIC_CAST(Interface*,emuProtoVpnServerPair->proto->getParent()), pkt, false);
				}
				assert(evt);
#ifdef SSFNET_EMU_TIMESYNC
				//XXX old code which is not ported!
				LOG_ERROR("Need to finish porting SSFNET_EMU_TIMESYNC"<<endl);
				int* tv = (int*)&recv_buf[size];
				evt->clock_drift = iothreads->clock_drift[idx];
				evt->send_time = ntohl(tv[0])+ntohl(tv[1])*1e-6;
#endif

				input_channel->putRealEvent(evt);
				// no need to reclaim buf or evt!
			}
		}
	} // end of forever loop

	for(idx=0;idx<(int)gateways.size();idx++) {
		LOG_DEBUG("[reader]closing connection to simulation gateway: "<<gateways[idx]->host<<":"<<gateways[idx]->port<<endl);
		close(gateways[idx]->in);
		gateways[idx]->in=-1;
	}
}

void OpenVPNDevice::writer_thread() {
	//connect to gateways
	for(OpenVpnServerVector::iterator i=gateways.begin(); i!=gateways.end();i++) {
		LOG_DEBUG("[writer] Connect to gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
		(*i)->out = socket(AF_INET, SOCK_STREAM, 0);

		// open a tcp connection with the simulation gateway
		if((*i)->out == -1) {
			LOG_WARN("Writer thread cannot create socket to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			continue;
		}

		struct hostent* hent = gethostbyname((*i)->host.c_str());
		if(!hent) {
			LOG_WARN("Writer thread cannot get the host info of simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			close((*i)->out);
			(*i)->out=-1;
			continue;
		}

		sockaddr_in addr;
		addr.sin_family = AF_INET;
		addr.sin_port = htons((*i)->port);
		addr.sin_addr = *(struct in_addr*)*hent->h_addr_list;
		if(connect((*i)->out, (struct sockaddr*)&addr, sizeof(addr)) != 0) {
			LOG_WARN("Writer thread cannot connect to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			close((*i)->out);
			(*i)->out=-1;
			continue;
		}

		// register this thread as writer
		uint8_t type = OpenVPNDevice::WRITER;
		if(safe_send((*i)->out, (char*)&type, sizeof(type)) != sizeof(type)) {
			LOG_WARN("Writer thread cannot send connection type to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
			close((*i)->out);
			(*i)->out=-1;
			continue;
		}

#ifdef SSFNET_EMU_TIMESYNC
		//XXX old code which is not ported!
		LOG_ERROR("Need to finish porting SSFNET_EMU_TIMESYNC"<<endl);
		// send packets in order to estimate the clock shift
		if(iothreads->send_timesync_packet(iothreads->proxy_outsocks[idx])) {
			fprintf(stderr, "Writer thread cannot send time synchronization packet to simulation gateway: %s:%d.\n",
					iothreads->proxy_ips[idx].c_str(), iothreads->proxy_ports[idx]);
			close(iothreads->proxy_outsocks[idx]);
			iothreads->proxy_outsocks[idx] = -1;
			continue;
		}
#endif
		LOG_DEBUG("[writer]Established connection to simulation gateway: "<<(*i)->host<<":"<<(*i)->port<<endl);
	}

	OpenVpnServer* svr;
	double delay;
	int buffer_size, reserved_size;
	for(;!stop;) {
#ifdef SSFNET_EMU_TIMESYNC
		//XXX old code which is not ported!
		LOG_ERROR("Need to finish porting SSFNET_EMU_TIMESYNC"<<endl);
		if(EMU_TIMESYNC_PERIOD > 0 && EMU_TIMESYNC_PERIOD == i) {
			// send packets in order to estimate the clock shift
			for(idx=0; idx<(int)iothreads->proxy_ips.size(); idx++) {
				if(iothreads->proxy_outsocks[idx] == -1) continue;
				if(iothreads->send_timesync_packet(iothreads->proxy_outsocks[idx])) {
					fprintf(stderr, "Writer thread cannot send time synchronization packet to simulation gateway: %s:%d.\n",
							iothreads->proxy_ips[idx].c_str(), iothreads->proxy_ports[idx]);
					close(iothreads->proxy_outsocks[idx]);
					iothreads->proxy_outsocks[idx] = -1;
					continue;
				}
			}
			i = 0;
		}
#endif

		// receive event through the out-channel from the simulator
		EmulationEvent* evt = (EmulationEvent*)output_channel->getRealEvent(delay);
		/* WHAT TO DO WITH THE DELAY? */

		//LOG_DEBUG("Got a evt to write out! iface="<<evt->iface->getUName()<<", ip="<<evt->iface->getIP()<<endl)
		// update the ip to socket table
		EmuProtoVpnServerPairMap::iterator i2i = ip2emuproto.find((int)(evt->iface->getIP()));
		if(i2i != ip2emuproto.end()) {
			if(i2i->second->server) {
				svr=i2i->second->server;
				if(svr->out==-1)  {
					//The out thread is not connected
					LOG_WARN("The writer thread which connects tp "<<svr->host<<":"<<svr->port<<" has disconnected! Dropping packet"<<endl);
					evt->free();
					continue;
				}
			}
			else {
				//its not connected yet
				LOG_WARN("The remote(real) host has not connected to the ssfgwd yet! Dropping packet."<<endl);
				evt->free();
				continue;
			}
		}
		else {
			LOG_WARN("Tried to export an emulation event from an ip which ("<< evt->iface->getIP()<<
					") which is not registered as emulated! Dropping packet."<<endl);
			evt->free();
			continue;
		}

		assert(svr);

		//LOG_DEBUG("Exporting packet to server "<<svr->host<<":"<<svr->port<<endl);

		//prepare the buffer to put the real bytes in
		byte* pbuf = evt->pkt->getRawBuffer(&buffer_size, &reserved_size);
		int temp_size = buffer_size - reserved_size;
		byte* temp_buf = pbuf+reserved_size;
		if(!pbuf) {
			//the packet does not have a buffer... we need to serialize into a local buf
			if(!send_buf) {
				send_buf_size=evt->pkt->size();
				if(send_buf_size < EMU_BUFSIZ)
					send_buf_size=EMU_BUFSIZ;
				send_buf = new char[send_buf_size];
				assert(send_buf);
			}
			temp_size=evt->pkt->size();
			if(temp_size > send_buf_size) {
				LOG_INFO("Growing send buffer from "<<send_buf_size<<" to "<<temp_size<<endl)
				delete[] send_buf;
				send_buf_size=temp_size;
				send_buf = new char[send_buf_size];
				assert(send_buf);
			}
			pbuf=(byte*)send_buf;
			temp_buf=(byte*)send_buf;
			buffer_size=temp_size;
			reserved_size=0;
		}
		assert(pbuf);
		assert(evt->pkt->size() <= temp_size);

		//make the pkt real and put it in temp_buf
		evt->pkt->getMessage()->toRawBytes(temp_buf, temp_size);
		assert(temp_size==0);

		// send the message to the simulation gateway
		uint32 ip_n = htonl((int)(evt->iface->getIP()));
		if(safe_send(svr->out,(char*)&ip_n, sizeof(uint32)) != sizeof(uint32)) {
			LOG_WARN("Writer thread cannot send leading ip address to simulation gateway: "<<svr->host<<":"<<svr->port<<". Dropping pkt and closing writer socket!"<<endl);
			close(svr->out);
			svr->out=-1;
			evt->free();
			continue;
		}
		if(safe_send(svr->out,(char*)(pbuf+reserved_size), buffer_size-reserved_size) != buffer_size-reserved_size) {
			LOG_WARN("Writer thread cannot send packet to simulation gateway: "<<svr->host<<":"<<svr->port<<". Dropping pkt and closing writer socket!"<<endl);
			close(svr->out);
			svr->out=-1;
			evt->free();
			continue;
		}

		// the event needs to be reclaimed
		evt->free();
	}

	//cleanup
	for(int idx=0;idx<(int)gateways.size();idx++) {
		close(gateways[idx]->out);
		gateways[idx]->out=-1;
	}
}

bool OpenVPNDevice::isActive() {
	return !stop && gateways.size()>0;
}

bool OpenVPNDevice::requiresSingleInstancePerHost() {
	return false;
}

int OpenVPNDevice::getDeviceType() {
	return EMU_OPEN_VPN_DEVICE;
}


//XXX start hack for geni demo
OpenVPNDevice::ServerPortPairList* OpenVPNDevice::static_gateways = 0;

void OpenVPNDevice::addGateway(SSFNET_STRING& server, int port) {
	LOG_DEBUG("Adding gateway, server="<<server<<", port="<<port<<endl);
	if(!static_gateways) {
		static_gateways = new OpenVPNDevice::ServerPortPairList();
	}
	static_gateways->push_back(OpenVPNDevice::ServerPortPair(server,port));
}

//XXX end hack

SSFNET_REGISTER_EMU_DEVICE(OpenVPNDevice, EMU_OPEN_VPN_DEVICE);

} //namespace ssfnet
} //namespace prime

#endif /*SSFNET_EMULATION*/
