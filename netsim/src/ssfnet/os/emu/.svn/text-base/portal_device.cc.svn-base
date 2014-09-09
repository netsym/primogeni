/**
 * \file portal_device.cc
 * \brief XXX
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
 */


#define EMU_BUFSIZ 300000 // buffer up to 200 pkts of 1500 bytes

#include "os/emu/portal_device.h"
#include "os/io_mgr.h"
#include "os/logger.h"
#include "proto/emu/emulation_protocol.h"
#include "os/partition.h"

#ifdef SSFNET_EMULATION

#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <sys/select.h>
#include <sys/types.h>
#include <unistd.h>
#include <iomanip>
#include <errno.h>
#include <net/ethernet.h>
#include <sys/poll.h>

#ifdef PRIME_SSF_MACH_X86_DARWIN
#else
#include <linux/if_ether.h>
#include <linux/if_packet.h>
#endif

//#define LOG_WARN(X) std::cout<<"["<<__LINE__<<"]"<<X;
namespace prime {
namespace ssfnet {

#ifdef USE_TX_RING
int Portal::c_buffer_sz   = 1024*8;
int Portal::c_buffer_nb   = 1024;
int Portal::c_sndbuf_sz   = 0;
#endif
int Portal::c_mtu         = 0;
int Portal::mode_loss     = 1;
bool Portal::sconfiged = false;

LOGGING_COMPONENT(PortalDevice);
#define BUFFSIZE 300000 // buffer up to 200 pkts of 1500 bytes


void TrafficPortal::init() {
	EmulationProtocol::init();
	getInterface()->inHost()->registerTrafficPortal(this);
}

IPPrefix::List& TrafficPortal::getNetworks() {
	return unshared.networks.read();
}

void TrafficPortal::transmit(Packet* pkt, VirtualTime deficit) {
	getInterface()->receiveEmuPkt(pkt);
}

bool TrafficPortal::receive(Packet* pkt, bool pkt_was_targeted_at_iface) {
#ifdef SSFNET_EMULATION
	LOG_DEBUG(getUName()<<" emu proto pkt_was_targeted_at_iface="<<(pkt_was_targeted_at_iface?"true":"false")<<
			", ipForwardingEnabled()="<<(ipForwardingEnabled()?"true":"false")<<", isActive()="<<(isActive()?"true":"false")<<endl);
	if(isActive()) {
		LOG_DEBUG("exporting packet from iface "<<getInterface()->getUName()<<endl);
		//LOG_DEBUG("pkt->size()="<<pkt->size()<<endl);
		emuDevice->exportPacket(getInterface(),pkt);
		return true;
	}
	LOG_DEBUG(getUName()<<" emu proto did not take packet! "<<endl);
#endif
	return false;
}

PortalDevice::PortalDevice():maxfd(0) {
}

PortalDevice::~PortalDevice(){
	for(Portal::Map::iterator i = uid2portal.begin(); i!=uid2portal.end();i++) {
		delete i->second;
	}
}

void PortalDevice::init(){
	BlockingEmulationDevice::init();
	LOG_DEBUG("PortalDevice::init() input_channel="<<input_channel<<", output_channel="<<output_channel<<endl);
	LOG_DEBUG("Portal Table:\n"<<ip2portal<<endl);

	for(Portal::Map::iterator i = uid2portal.begin(); i!=uid2portal.end();i++) {
		i->second->init();
		if(maxfd < i->second->getRxFD()) {
			maxfd = i->second->getRxFD();
		}
		ip2mac.insert(SSFNET_MAKE_PAIR(i->second->getIP(),i->second->getMAC()));
	}
	sim_net=getCommunity()->getPartition()->getTopnet()->getIPPrefix();
	ip2portal.setupRoutes(sim_net);
	LOG_DEBUG("Simulated network prefix is "<<sim_net<<endl);
}

void PortalDevice::wrapup() {
	BlockingEmulationDevice::wrapup();
}

void PortalDevice::registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto) {
	ssfnet_throw_exception(SSFNetException::other_exception,"Cannot call registerProxiedEmulationProtocol() on an TAPDevice!");
}

void PortalDevice::registerEmulationProtocol(EmulationProtocol* emu_proto) {
	if(!TrafficPortal::getClassConfigType()->isSubtype(emu_proto->getTypeId())) {
		LOG_ERROR("The Portal device only supports emulation protocols of type TrafficPortal! Found "<<emu_proto->getTypeName()<<endl);
	}
	TrafficPortal* portal_proto = (TrafficPortal*) emu_proto;
	Portal* portal= new Portal(emu_proto, this);
	uid2portal.insert(SSFNET_MAKE_PAIR(emu_proto->getInterface()->getUID(),portal));
	for(IPPrefix::List::iterator i=portal_proto->getNetworks().begin(); i!=portal_proto->getNetworks().end(); i++) {
		ip2portal.addPortal(*i, portal, portal_proto);
	}
	emu_proto->setEmulationDevice(this);
}

void PortalDevice::exportPacket(Interface* iface, Packet* pkt) {
	//LOG_DEBUG("exporting packet from "<<iface->getUName()<<", ip="<<iface->getIP()<<endl);
	EmulationEvent* evt= new EmulationEvent(iface, pkt, false);
	output_channel->write(evt);
}

void PortalDevice::handleProxiedEvent(EmulationEvent* evt) {
	LOG_WARN("Portal devices cannot proxy evts!"<<endl);
	evt->free();
}

EmulationProtocol* PortalDevice::ip2EmulationProtocol(IPAddress& ip) {
	TrafficPortalPair * portal = ip2portal.getPortal((uint32)ip);

	if(!portal) {
		LOG_WARN("asked to handle the departure to an unknown network "<<ip);
		return 0;
	}

	return portal->trafficPortal;
}

void PortalDevice::reader_thread() {
	maxfd++; //it should be 1 greater than the max fd...
	LOG_DEBUG("Starting portal read thread"<<endl)
	//its assumed that all of the taps are opened and ready to go.... we just need to setup the FDs...
	Portal::Map::iterator portal_it;

	fd_set base_fds, fds;
	//setup base_fd set
	FD_ZERO(&base_fds);

	for(portal_it = uid2portal.begin();portal_it!=uid2portal.end();portal_it++) {
		FD_SET(portal_it->second->getRxFD(),&base_fds);
	}
	int max_loops=500;
	bool more_packets=false;

	for(;!stop;) {
		//FD_COPY isn't standard... a simple assignment should work though....
		//FD_ZERO(&fds);
		fds=base_fds;
		select(maxfd, &fds, NULL, NULL, NULL);
		LOG_DEBUG("Got something..."<<endl);

		max_loops=500;
		more_packets=false;
		do {
			for(portal_it = uid2portal.begin();portal_it!=uid2portal.end();portal_it++) {
				more_packets=more_packets || portal_it->second->process_pkts(100);
			}
			max_loops--;
		} while(more_packets && max_loops>0);
	}
}

void PortalDevice::writer_thread() {
	LOG_DEBUG("Starting portal write thread"<<endl)
	double delay;
	VirtualTime last_arp_check=getCommunity()->now();
	for(;!stop;) {
		// receive event through the out-channel from the simulator
		SSFNetEvent* evt_ = (SSFNetEvent*)output_channel->getRealEvent(delay);

		/* WHAT TO DO WITH THE DELAY? */
		VirtualTime now=this->getCommunity()->now();

		switch(evt_->getSSFNetEventType()) {
		case SSFNetEvent::SSFNET_EMU_NORMAL_EVT:
		{
			EmulationEvent* evt = (EmulationEvent*)evt_;

			if(evt->pkt) {
				LOG_DEBUG("Got a evt to write out! iface="<<evt->iface->getUName()<<", ip="<<evt->iface->getIP()<<endl);
				Portal::Map::iterator portal_it = uid2portal.find(evt->iface->getUID());

				if(portal_it == uid2portal.end()) {
					LOG_WARN("Tried to export an emulation event from an interface which ("<< evt->iface->getUName()<<
							") which is not registered traffic portal! Dropping packet."<<endl);
					// the event needs to be reclaimed
					evt->free();
				}
				else {
					IPAddress tgt = ((IPv4Message*)(evt->pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4)))->getDst();
					MACAddress* mac = lookupMAC(tgt);
					if(mac) {
						portal_it->second->sendPacket(evt,*mac);
						// the event needs to be reclaimed
						evt->free();
					}
					else {
						//XXX what about distributed?
						//not all portals will be on the
						//same compute node.....
						TrafficPortalPair* tp =ip2portal.getPortal(tgt);
						if(tp) {
							watingForArps.push_back(new WaitingArpEvt(portal_it->second,evt,this->getCommunity()->now(), tgt));
							tp->portal->sendArpRequest(tgt);
						}
						else {
							LOG_WARN("Tried to export an emulation event from an interface which ("<< evt->iface->getUName()<<
									") to and ip ("<<tgt<<") but I dont know where to arp for the mac!"<<endl);
							// the event needs to be reclaimed
							evt->free();
						}
					}
				}
			}
			//XXX the virtual time stuff is all f*** up......
			/*if(last_arp_check - now < ArpEvent::ArpEvent::MAX_ARP_LOOK_INTERVAL)
				break;*/
			evt_=0;
		}
		case SSFNetEvent::SSFNET_ARP_EVT:
			//see if any of the pkts waiting for arps are ready
			if(evt_) {
				ArpEvent* evt=(ArpEvent*)evt_;
				LOG_DEBUG("Got an arp response... "<<evt->ip<<" --> "<<evt->mac<<endl);
				ip2mac.insert(SSFNET_MAKE_PAIR(evt->ip,evt->mac));
				evt->free();
			}
			if(watingForArps.size()>0) {
					for(WaitingArpEvt::List::iterator arp_it=watingForArps.begin();arp_it!=watingForArps.end();) {
						LOG_DEBUG("*arp_it="<<(*arp_it)<<endl);

						MACAddress* mac = lookupMAC((*arp_it)->tgt);
						if(mac) {
							TrafficPortalPair* tp =ip2portal.getPortal((*arp_it)->tgt);
							if(tp) {
								LOG_DEBUG("Sending mac that was waiting for arp response...."<<endl);
								assert((*arp_it)->portal && (*arp_it)->evt);
								(*arp_it)->portal->sendPacket((*arp_it)->evt,*mac);
								// the event needs to be reclaimed
								delete (*arp_it);
								arp_it=watingForArps.erase(arp_it);
							}
							else {
								LOG_ERROR("How did this happen?");
							}
							continue;
						}
						else {
							LOG_DEBUG("can't find mac for "<<(*arp_it)->tgt<<endl);
							arp_it++;
						}
						//XXX the virtual time stuff is all f*** up......
						/*if( (now-(*arp_it).time) > ArpEvent::ArpEvent::MAX_ARP_TIME) {
							LOG_WARN("Waited too long ("<<((now-(*arp_it).time))<<", max="<<ArpEvent::ArpEvent::MAX_ARP_TIME<<") for the arp response... dropping pkt"<<endl);
							(*arp_it).evt->free();
							watingForArps.erase(arp_it);
						}*/
					}
				}
			break;
		default:
			LOG_ERROR("Unexpected evt type!"<<endl);
		}
	}
}


void PortalDevice::insertIntoSim(EthernetHeader* eth, IPv4RawHeader* ip, uint32_t len) {
	LOG_DEBUG("insertIntoSim, len="<<len<<endl)

	// we need to copy the message to a new buffer (the receive
	// buffer will be reused for next message)
	char* buf = new char[len];
	assert(buf);
	memcpy(buf, (char*)eth, len);
	eth = (EthernetHeader*)buf; // update the eth header pointer
	ip = (IPv4RawHeader*)(buf+sizeof(EthernetHeader)); // update the ip header pointer


	//find the iface in the sim which corresponds to the src mac
	TrafficPortalPair* p = ip2portal.getPortal(ip->getSrc());
	if(!p) {
		LOG_WARN("Tried to import a packet from an unknown network, ip=\n"<<*ip<<endl);
		return;
	}

	// create the event (the new buffer is handed off to the event
	// as the payload) and pass it subsequently to the simulator
	UID_t dst_uid=getCommunity()->synchronousNameResolution(IPAddress(ip->getDst()));
	Packet* pkt=new Packet(SSFNET_PROTOCOL_TYPE_IPV4, (prime::ssfnet::byte*)buf, len, sizeof(EthernetHeader), dst_uid);

	EmulationEvent* evt=0;
	if(!dst_uid) {
		evt = new EmulationEvent(p->trafficPortal->getInterface(), pkt,true);
		LOG_DEBUG("created emu evt with a need for async name res"<<endl)
	}
	else {
		evt = new EmulationEvent(p->trafficPortal->getInterface(), pkt,false);
		LOG_DEBUG("created emu evt withOUT a need for async name res"<<endl)
	}

	assert(evt);
	//std::cout << "**** get event at " << getCommunity()->now() << ", dst_uid=" << dst_uid << ", dest ip=" << IPAddress(ip->getDst()) << std::endl;
	input_channel->putRealEvent(evt);
	// no need to reclaim buf or evt!
}

bool PortalDevice::isActive() {
	return !stop;
}

bool PortalDevice::requiresSingleInstancePerHost() {
	return false;
}

int PortalDevice::getDeviceType() {
	return EMU_TRAFFIC_PORTAL;
}


void PortalDevice::handleArp(Portal* portal, EthernetHeader* eth, ARPHeader* arp) {
	LOG_DEBUG("Got arp\n"<<*eth<<"\n"<<*arp<<"\n");
	if(arp->isArpRequest()) {
		IPAddress ip=IPAddress(arp->getDstIP());
		LOG_DEBUG("Looking for mac of ip "<<ip<<endl);
		if(sim_net.contains(ip)) {
			LOG_DEBUG("It is in the simulation, send response!");
			portal->sendArpResponse(MACAddress(eth->getSrc()), IPAddress(arp->getSrcIP()), ip, portal->getMAC());
		}
		else {
			//ignore it
			LOG_DEBUG("Ignoring arp...\n");
		}
	}
	else {
		ArpEvent* evt = new ArpEvent(IPAddress(arp->getSrcIP()),MACAddress(arp->getSrcMac()));
		LOG_DEBUG("I think "<<evt->ip<<" is at "<<evt->mac<<endl);
		input_channel->putRealEvent(evt); //XXX not sure this is legal....
	}
}

MACAddress* PortalDevice::lookupMAC(IPAddress ip) {
	IP2MACMap::iterator rv = ip2mac.find(ip);
	if(rv == ip2mac.end())
		return 0;
	return &(rv->second);
}

SSFNET_REGISTER_EMU_DEVICE(PortalDevice, EMU_TRAFFIC_PORTAL);



#ifdef PRIME_SSF_MACH_X86_DARWIN
Portal::Portal(EmulationProtocol* emuproto_, PortalDevice* dev_)
{
	LOG_ERROR("Not supported on the mac!");
}
#else
Portal::Portal(EmulationProtocol* emuproto_, PortalDevice* dev_) :
					dev(dev_),
					emuproto(emuproto_),
					rx_fd(-1),
					tx_fd(-1),
					pcap_handle(0),
#ifdef USE_TX_RING
					tx_data_offset(0),
					tx_idx(0),
					tx_header_start(0)
#else
					sendbuf(new byte[BUFFSIZE])
#endif					
					{
#ifdef USE_TX_RING
	memset(&s_packet_req, 0, sizeof(struct tpacket_req));
#endif										

	if(!sconfiged) {
		sconfiged=true;
		char* env_var=0;
#ifdef USE_TX_RING
		env_var = getenv("c_buffer_sz");
		if(env_var && strlen(env_var)>0) {
			c_buffer_sz   = (int)atol(env_var);
		}
		env_var = getenv("c_buffer_nb");
		if(env_var && strlen(env_var)>0) {
			c_buffer_nb   = (int)atol(env_var);
		}
		env_var = getenv("c_sndbuf_sz");
		if(env_var && strlen(env_var)>0) {
			c_sndbuf_sz   = (int)atol(env_var);
		}
#endif
		env_var = getenv("c_mtu");
		if(env_var && strlen(env_var)>0) {
			c_mtu   = (int)atol(env_var);
		}
		env_var = getenv("mode_loss");
		if(env_var && strlen(env_var)>0) {
			mode_loss   = (int)atol(env_var);
		}
	}
}
#endif

Portal::~Portal() {
	/* cleanup */
#ifndef PRIME_SSF_MACH_X86_DARWIN
	if(pcap_handle) {
		pcap_close(pcap_handle);
		pcap_handle=0;
	}
#endif
	if(tx_fd!=-1)
		close(tx_fd);
#ifndef PRIME_SSF_MACH_X86_DARWIN
#ifndef USE_TX_RING
	delete sendbuf;
#endif
#endif
}

#ifdef PRIME_SSF_MACH_X86_DARWIN
void Portal::init() {
	LOG_ERROR("Not supported on the mac!");
}
#else
void Portal::init() {

	//setup the pcap rx channel
	bpf_u_int32 mask;
	bpf_u_int32 net;
	char errbuf[PCAP_ERRBUF_SIZE];
	SSFNET_STRING* nic=Partition::getPortalNic(emuproto->getInterface()->getUID());
	if(!nic) {
		LOG_ERROR("Unable to find the network interface attached to the traffic portal '"<<emuproto->getInterface()->getUName()<<"'"<<endl);
	}
	iface_name.clear();
	iface_name.append(nic->c_str());
	char dev_str[nic->length() + 1];
	sprintf(dev_str,"%s", nic->c_str());

	LOG_DEBUG("Creating pcap handle for "<<nic<<"\n");
	if (pcap_lookupnet(dev_str, &net, &mask, errbuf) == -1) {
		LOG_ERROR("Couldn't get netmask for device "<<nic<<", err:"<<errbuf<<endl);
	}
	pcap_handle = pcap_open_live(dev_str, 65535, 0, -1, errbuf); //dev name, max len, promiscuous, timeout, error buffer
	if(!pcap_handle) {
		LOG_ERROR("Couldn't open device "<<nic<<", err:"<<errbuf<<endl);
	}
	if (pcap_setnonblock(pcap_handle,1,errbuf) == -1) {
		LOG_ERROR("Couldn't change "<<nic<<" to non-blocking! err:"<<errbuf<<endl);
	}
	if (pcap_getnonblock(pcap_handle,errbuf)==0) {
		LOG_ERROR("Couldn't change "<<nic<<" to non-blocking! err:"<<errbuf<<endl);
	}
	rx_fd = pcap_get_selectable_fd(pcap_handle);
	if(rx_fd == -1) {
		LOG_ERROR("Libpcap couldn't get a selectable FD for "<<nic<<endl);
	}
	if(pcap_setdirection(pcap_handle,PCAP_D_IN)==-1) {
		LOG_ERROR("Libpcap couldn't set the capture direction for "<<nic<<endl);
	}

	struct ifreq tx_ifr; /* points to one interface returned from ioctl */

	//setup the tx channel
	tx_fd = socket(PF_PACKET, SOCK_RAW, htons(ETH_P_ALL));
	if(tx_fd == -1) {
		LOG_ERROR("Unable to open (tx) raw socket for "<<nic<<"!"<<endl);
	}

	/* initialize interface struct */
	strncpy (tx_ifr.ifr_name, dev_str, sizeof(tx_ifr.ifr_name));

	/*get the mac */
	if(ioctl(tx_fd, SIOCGIFHWADDR, &tx_ifr) == -1) {
		perror("iotcl");
		LOG_ERROR("Unable to ioctl the tx socket for "<<nic<<"!"<<endl);
	}
	mac=MACAddress((uint8_t*)(&tx_ifr.ifr_hwaddr.sa_data));
	LOG_DEBUG("MAC for  "<<nic<<":"<<mac<<endl);

	/* get the ip*/
	if(ioctl(tx_fd, SIOCGIFADDR, &tx_ifr) == -1) {
		perror("iotcl");
		LOG_ERROR("Unable to ioctl the tx socket for "<<nic<<"!"<<endl);
	}
	{
		struct sockaddr_in sin;
		memcpy(&sin, &tx_ifr.ifr_addr, sizeof(struct sockaddr));
		ip=IPAddress(ntohl((uint32)sin.sin_addr.s_addr));
	}
	LOG_DEBUG("IP for  "<<nic<<":"<<ip<<endl);

	/* set mtu */
	if(c_mtu) {
		/* update the mtu through ioctl */
		tx_ifr.ifr_mtu = c_mtu;
		if(ioctl(tx_fd, SIOCSIFMTU, &tx_ifr) == -1)
		{
			perror("iotcl");
			LOG_ERROR("Unable to ioctl the tx socket for "<<nic<<"!"<<endl);
		}
	}

	/* Get the iface index */
	if(ioctl(tx_fd, SIOCGIFINDEX, &tx_ifr) == -1) {
		perror("iotcl");
		LOG_ERROR("Unable to ioctl the tx socket for "<<nic<<"!"<<endl);
	}

	/* set sockaddr info */
	memset(&my_addr, 0, sizeof(struct sockaddr_ll));
	my_addr.sll_family = AF_PACKET;
	my_addr.sll_protocol = ETH_P_IP;
	//my_addr.sll_protocol = ETH_P_ALL;
	my_addr.sll_ifindex = tx_ifr.ifr_ifindex;

	/* bind port */
	if (bind(tx_fd, (struct sockaddr *)&my_addr,
			sizeof(struct sockaddr_ll)) == -1) {
		perror("iotcl");
		LOG_ERROR("Unable to bind the tx socket for "<<nic<<"!"<<endl);
	}

#ifdef USE_TX_RING
	/* set packet loss option */
	int tmp = mode_loss;
	if (setsockopt(tx_fd,
			SOL_PACKET,
			PACKET_LOSS,
			(char *)&tmp,
			sizeof(tmp))<0) {
		perror("iotcl");
		LOG_ERROR("Unable to set the (tx) packet loss socket option for "<<nic<<"!"<<endl);
	}

	/* prepare TX ring request */
	s_packet_req.tp_block_size = c_buffer_sz;
	s_packet_req.tp_frame_size = c_buffer_sz;
	s_packet_req.tp_block_nr = c_buffer_nb;
	s_packet_req.tp_frame_nr = c_buffer_nb;

	/* calculate memory to mmap in the kernel */
	uint32_t size = s_packet_req.tp_block_size * s_packet_req.tp_block_nr;

	/* send TX ring request */
	if (setsockopt(tx_fd,
			SOL_PACKET,
			PACKET_TX_RING,
			(char *)&s_packet_req,
			sizeof(s_packet_req))<0) {
		perror("iotcl");
		LOG_ERROR("Unable to set the packet tx_ring option for "<<nic<<"!"<<endl);
	}
	/* change send buffer size */
	if(c_sndbuf_sz) {
		LOG_DEBUG("Changing send buffer size to "<<c_sndbuf_sz<<endl);
		if (setsockopt(tx_fd, SOL_SOCKET, SO_SNDBUF, &c_sndbuf_sz,
				sizeof(c_sndbuf_sz))< 0) {
			perror("iotcl");
			LOG_ERROR("Unable to set the send buffer size for "<<nic<<"!"<<endl);
		}
	}

	/* get data offset */
	tx_data_offset = TPACKET_HDRLEN - sizeof(struct sockaddr_ll);
	LOG_DEBUG("tx data_offset = "<<tx_data_offset<<endl);

	/* mmap TX ring buffers memory */
	tx_header_start = (tpacket_hdr*)
			mmap(
					0,
					size,
					PROT_READ|PROT_WRITE,
					MAP_SHARED,
					tx_fd, 0);
	if (tx_header_start == (void*)-1)
	{
		perror("iotcl");
		LOG_ERROR("Unable memory map tx_ring/socket for "<<nic<<"!"<<endl);
	}

#endif

	LOG_DEBUG("Ready to send/recv on "<<nic<<"!"<<endl);
}

#endif

#ifndef PRIME_SSF_MACH_X86_DARWIN
#ifdef USE_TX_RING
struct tpacket_hdr * Portal::nextTxFrame() {
	struct tpacket_hdr * ps_header=0;
	int idx=0;
	/* get free pkt space */
	for(int i=tx_idx; i<c_buffer_nb && !ps_header; i++)
	{
		ps_header = ((struct tpacket_hdr *)((u_char *)tx_header_start + (c_buffer_sz*i)));
		switch((volatile uint32_t)ps_header->tp_status)
		{
		case TP_STATUS_SEND_REQUEST:
		case TP_STATUS_SENDING:
			//not available
			ps_header=0;
			break;
		case TP_STATUS_AVAILABLE:
			//case TP_STATUS_LOSING: //same as TP_STATUS_WRONG_FORMAT?
		case TP_STATUS_WRONG_FORMAT:
			//available
			idx=i;
			break;
		default:
			LOG_WARN("Unexpected state in tx ring!"<<endl)
			ps_header=0;
			break;
		}
	}
	if(ps_header == 0) {
		for(int i=0; i<tx_idx && !ps_header; i++)
		{
			ps_header = ((struct tpacket_hdr *)((u_char *)tx_header_start + (c_buffer_sz*i)));
			switch((volatile uint32_t)ps_header->tp_status)
			{
			case TP_STATUS_SEND_REQUEST:
				//not available
				ps_header=0;
				break;
			case TP_STATUS_AVAILABLE:
				//case TP_STATUS_LOSING: //same as TP_STATUS_WRONG_FORMAT?
			case TP_STATUS_WRONG_FORMAT:
				//available
				idx=i;
				break;
			default:
				LOG_WARN("Unexpected state in tx ring!"<<endl)
				ps_header=0;
				break;
			}
		}
	}

	if(ps_header)
		tx_idx=idx+1;
		if(tx_idx>=c_buffer_nb)
			tx_idx=0;

	return ps_header;
}
#endif
#endif

void Portal::sendArpResponse(MACAddress requester_mac, IPAddress requester_ip, IPAddress requested_ip, MACAddress requested_mac) {
#ifdef PRIME_SSF_MACH_X86_DARWIN
LOG_ERROR("not supported on mac")
#else
	//setup the ethernet header
#ifdef USE_TX_RING
	struct tpacket_hdr * ps_header = nextTxFrame();
	if(ps_header == 0) {
		LOG_WARN("No space in tx_buffer!"<<endl);
		return;
	}
	EthernetHeader* eth = (EthernetHeader*)(((byte*) ps_header) + tx_data_offset);
	ARPHeader *arp = (ARPHeader*)(((byte*) ps_header) + tx_data_offset+sizeof(EthernetHeader));
#else
	EthernetHeader* eth = (EthernetHeader*)(sendbuf);
	ARPHeader *arp = (ARPHeader*)(sendbuf+sizeof(EthernetHeader));
#endif
	mac.toRawMac(eth->getSrc());
	requester_mac.toRawMac(eth->getDst());

	eth->setFrameType(ETH_P_ARP);

	mac.toRawMac(arp->getSrcMac());
	requested_mac.toRawMac(arp->getDstMac());
	arp->setDstIP((uint32)requested_ip);
	arp->setSrcIP((uint32)ip);
	arp->setHType(ARP_TYPE_ETH);
	arp->setHSize(6);
	arp->setProto(ARP_PROTO_IP);
	arp->setPSize(4);
	arp->setOpcode(ARP_REPLY);


	LOG_DEBUG("Sending arp reply:\n"<<*eth<<"\n"<<*arp<<"\n");
#ifdef USE_TX_RING
	ps_header->tp_len = sizeof(EthernetHeader)+sizeof(ARPHeader);
	ps_header->tp_status = TP_STATUS_SEND_REQUEST;
	if( send(tx_fd,
			NULL,
			0,
			MSG_WAITALL)<0) {
		perror("Error while sending arp!");
		LOG_ERROR("HERE!"<<endl);
	}
#else
	if( send(tx_fd,
			sendbuf,
			sizeof(EthernetHeader)+sizeof(ARPHeader),
			MSG_WAITALL)<0) {
		perror("Error while sending arp!");
		LOG_ERROR("HERE!"<<endl);
	}
#endif
#endif
}


void Portal::sendArpRequest(uint32 req_ip) {
#ifdef PRIME_SSF_MACH_X86_DARWIN
LOG_ERROR("not supported on mac")
#else
	//setup the ethernet header
	//setup the ethernet header
#ifdef USE_TX_RING
	struct tpacket_hdr * ps_header = nextTxFrame();
	if(ps_header == 0) {
		LOG_WARN("No space in tx_buffer!"<<endl);
		return;
	}
	EthernetHeader* eth = (EthernetHeader*)(((byte*) ps_header) + tx_data_offset);
	ARPHeader *arp = (ARPHeader*)(((byte*) ps_header) + tx_data_offset+sizeof(EthernetHeader));
#else
	EthernetHeader* eth = (EthernetHeader*)(sendbuf);
	ARPHeader *arp = (ARPHeader*)(sendbuf+sizeof(EthernetHeader));
#endif
	mac.toRawMac(eth->getSrc());
	MACAddress::MACADDR_BROADCAST_FF.toRawMac(eth->getDst());
	eth->setFrameType(ETH_P_ARP);

	mac.toRawMac(arp->getSrcMac());
	MACAddress::MACADDR_INVALID.toRawMac(arp->getDstMac());
	arp->setDstIP(req_ip);
	arp->setSrcIP(ip);
	arp->setHType(ARP_TYPE_ETH);
	arp->setHSize(6);
	arp->setProto(ARP_PROTO_IP);
	arp->setPSize(4);
	arp->setOpcode(ARP_REQUEST);

	LOG_DEBUG("Sending arp request for "<<mac<<":\n"<<*eth<<"\n"<<*arp<<"\n");

#ifdef USE_TX_RING
	ps_header->tp_len = sizeof(EthernetHeader)+sizeof(ARPHeader);
	ps_header->tp_status = TP_STATUS_SEND_REQUEST;
	if( send(tx_fd,
			NULL,
			0,
			MSG_WAITALL)<0) {
		perror("Error while sending arp!");
		LOG_ERROR("HERE!"<<endl);
	}
#else
	if( send(tx_fd,
			sendbuf,
			sizeof(EthernetHeader)+sizeof(ARPHeader),
			MSG_WAITALL)<0) {
		perror("Error while sending arp!");
		LOG_ERROR("HERE!"<<endl);
	}
#endif
#endif
}

void Portal::sendPacket(EmulationEvent* evt, MACAddress& dstmac) {
#ifdef PRIME_SSF_MACH_X86_DARWIN
LOG_ERROR("not supported on mac")
#else
#ifdef USE_TX_RING
	int psize=evt->pkt->size()+sizeof(EthernetHeader);
	if(c_buffer_sz < psize) {// || psize>1514) {
		LOG_WARN("The packet is too big to send via the TX_RING! psize="<<psize<<endl);
		return;
	}

	struct tpacket_hdr * ps_header = nextTxFrame();
	byte * data;

	if(ps_header == 0) {
		LOG_WARN("No space in tx_buffer!"<<endl);
		return;
	}

	EthernetHeader* eth_h = (EthernetHeader*)(((byte*) ps_header) + tx_data_offset);
	data = ((byte*) ps_header) + tx_data_offset+sizeof(EthernetHeader);
	int temp_size = psize-sizeof(EthernetHeader);
	evt->pkt->getMessage()->toRawBytes(data, temp_size);
	DEBUG_CODE(
		data = ((byte*) ps_header) + tx_data_offset+sizeof(EthernetHeader);
		LOG_DEBUG("Put the following in the tx buffer for "<<this->getEmuProto()->getInterface()->getName()<<":\n"<<*((IPv4RawHeader*)data)<<endl);
	);
	
#else
	//prepare the buffer to put the real bytes in
	int buffer_size, reserved_size;
	byte* pbuf = evt->pkt->getRawBuffer(&buffer_size, &reserved_size);
	int temp_size = buffer_size - reserved_size;
	byte* temp_buf = pbuf+reserved_size;
	if(!pbuf) {
		//the packet does not have a buffer... we need to serialize into a local buf
		if(evt->pkt->size() > (int)(BUFFSIZE-sizeof(EthernetHeader))) {
			LOG_ERROR("Do not support pkts over "<< (BUFFSIZE-sizeof(EthernetHeader))<<" bytes!"<<endl);
		}
		temp_size=evt->pkt->size();
		pbuf=(byte*)sendbuf;
		reserved_size=sizeof(EthernetHeader);
		temp_buf=(byte*)(sendbuf+reserved_size);
		buffer_size=reserved_size+temp_size;
	}
	else {
		if(reserved_size < (int)sizeof(EthernetHeader)) {
			//we don't have enough space for the ethernet header...
			LOG_DEBUG("Found a packet with a raw buffer but not enough reserved space!"<<endl);
			temp_size=evt->pkt->size();
			pbuf=(byte*)sendbuf;
			reserved_size=sizeof(EthernetHeader);
			temp_buf=(byte*)(sendbuf+reserved_size);
			buffer_size=reserved_size+temp_size;
		}
		else if(reserved_size>(int)sizeof(EthernetHeader)) {
			//we need the ethernet header to be contigous with the ip payload...
			pbuf+=(reserved_size-sizeof(EthernetHeader));
			buffer_size-=(reserved_size-sizeof(EthernetHeader));
			reserved_size-=(reserved_size-sizeof(EthernetHeader));
		}
		//else its the perfect size!
	}
	assert(pbuf);
	if(evt->pkt->size() != temp_size) {
		LOG_ERROR("ACK! evt->pkt->size()("<< evt->pkt->size()<<") != temp_size("<<temp_size<<")"<<endl);
	}	
	//XXX assert(evt->pkt->size() == temp_size);
	EthernetHeader* eth_h = (EthernetHeader*)pbuf;

	//make the pkt real and put it in temp_buf
	evt->pkt->getMessage()->toRawBytes(temp_buf, temp_size);
	assert(temp_size==0);
#endif

	//setup the ethernet header.....
	mac.toRawMac(eth_h->getSrc());
	dstmac.toRawMac(eth_h->getDst());
	eth_h->setFrameType(ETH_P_IP);


#ifdef USE_TX_RING
	ps_header->tp_len = psize;
	ps_header->tp_status = TP_STATUS_SEND_REQUEST;
	if( send(tx_fd,
			NULL,
			0,
			MSG_DONTWAIT)<0) {
		perror("Error while sending pkts!");
		LOG_ERROR("HERE! psize="<<psize<<endl);
	}
#else
	if( send(tx_fd,
			pbuf,
			buffer_size,
			MSG_DONTWAIT)<0) {
		perror("Error while sending pkts!");
		LOG_ERROR("HERE! buffer_size="<<buffer_size<<endl);
	}
#endif
#endif
}

bool Portal::process_pkts(int max_pkts) {
#ifdef PRIME_SSF_MACH_X86_DARWIN
LOG_ERROR("not supported on mac")
#else
	int rv=0;
	while(max_pkts>0) {
		rv=pcap_dispatch(pcap_handle, -1, process_pkt, (u_char*)this);
		if(rv <= 0)
			return false;
		max_pkts-=rv;
	}
#endif
	return true;
}

void Portal::process_pkt(u_char * portal_, const struct pcap_pkthdr * pkt_meta, const u_char * pkt) {
#ifdef PRIME_SSF_MACH_X86_DARWIN
LOG_ERROR("not supported on mac")
#else
	Portal* portal = (Portal*) portal_;
	EthernetHeader* eth=(EthernetHeader*)pkt;
	uint16_t frame_type;
	IPv4RawHeader* ip=0;
	if(eth->isVLAN()) {
		LOG_WARN("We don't handle vlans for portals!\n");
		VLANHeader* vlan=(VLANHeader*)(pkt+sizeof(EthernetHeader));
		frame_type=vlan->getFrameType();
		ip=(IPv4RawHeader*)(pkt+sizeof(EthernetHeader)+sizeof(VLANHeader));
	}
	else {
		frame_type=eth->getFrameType();
		ip=(IPv4RawHeader*)(pkt+sizeof(EthernetHeader));
	}

	if(pkt_meta->caplen != pkt_meta->len) {
		LOG_WARN("Unable to import packet because the capture length was less the than packet length["<<pkt_meta->caplen<<"!="<<pkt_meta->len<<"]!\n"<<*eth<<"\n"<<*ip<<endl);
		return;
	}
	/*else if(pkt_meta->caplen >1514) {
		LOG_WARN("The caplen was too large! pkt_meta->caplen="<<pkt_meta->caplen<<"\n"<<*eth<<"\n"<<*ip<<endl);
		return;
	}*/

	if(EthernetHeader::isARP(frame_type)) {
		portal->dev->handleArp(portal,eth,(ARPHeader*)(pkt+sizeof(EthernetHeader)));
	}
	else if(EthernetHeader::isIPv4(frame_type)) {
		LOG_DEBUG(" Got IPv4 pkt [size="<<(ip->getLen()-ip->getHdrLen())<<"]\n\t"<<*ip<<"\n");
		portal->dev->insertIntoSim(eth,ip,pkt_meta->caplen);
	}
	else if(EthernetHeader::isIPv6(frame_type)) {
		LOG_WARN("Received an IPv6 Message... dropping it."<<endl)
	}
	else {
		LOG_WARN("Unknown protocol following Ethernet header!"<<endl << *eth <<endl);
	}
#endif
}


PRIME_OSTREAM& operator<< (PRIME_OSTREAM& os, Portal const& portal) {
	return os << "["<<portal.emuproto->getInterface()->getName()<<" tx_fd="<<portal.tx_fd<<", rx_fd="<<portal.rx_fd<<"]";
}

void PortalTable::addPortal(IPPrefix& network, Portal* portal, TrafficPortal* proto, bool replace) {
	TrafficPortalPair* conflict = (TrafficPortalPair*) insert((uint32)network.getAddress(), network.getLength(), new TrafficPortalPair(network,proto,portal), replace);
	if(conflict) {
		if(replace) {
			delete conflict; // this is the old route
		} else {
			delete conflict;  // this is the new route
		}
	}
}

TrafficPortalPair* PortalTable::getPortal(uint32 ipaddr) {
	TrafficPortalPair* rv = NULL;
	// find out the route for the given ip address
	lookup(ipaddr, (TrieData**)&rv);
	return rv;
}


PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TrafficPortalPair& x) {
	return os <<"["<<x.prefix<<" --> "<<x.trafficPortal->getInterface()->getUName()<<","<< *(x.portal) <<"]";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const PortalTable& x) {
	PortalTable::dump_helper(x.root, 0, 0, os);
	return os;
}

void PortalTable::dump_helper(TrieNode* root, uint32 sofar,
		int n, PRIME_OSTREAM &os) {
	// Recurse on children, if any.
	for (int i=0; i < TRIE_KEY_SPAN; i++) {
		if(root->children[i]) {
			dump_helper(root->children[i], (sofar << 1) | i, n+1, os);
		}
	}
	if(root->data) {
		os << "\t" << *((TrafficPortalPair*)root->data)<<"\n";
	}
}

void PortalTable::setupRoutes(IPPrefix sim_net) {
	setupRoutes(root,0,0);
}

void PortalTable::setupRoutes(TrieNode* root, uint32 sofar, int n) {
	// Recurse on children, if any.
	for (int i=0; i < TRIE_KEY_SPAN; i++) {
		if(root->children[i]) {
			setupRoutes(root->children[i], (sofar << 1) | i, n+1);
		}
	}
	if(root->data) {
		TrafficPortalPair* d = (TrafficPortalPair*)root->data;
		d->portal->getIfaceName();
		LOG_WARN("SETUP ROUTE FOR nic=["<<d->portal->getIfaceName()<<","<< d->portal->getIP()<<"] prefix="<<d->prefix<<endl);
		char temp[1024];
		//clean bad routes
		sprintf(temp, "/sbin/ip route | /bin/grep \"%s\" | /bin/awk '{print \"/sbin/ip route del \"$1\" \"$2\" \"$3\";\"}' | /bin/sh", d->portal->getIfaceName().c_str());
		LOG_WARN("Cleaning old routes, '"<<SSFNET_STRING(temp)<<"'\n");
		system(temp);
		sprintf(temp, "ip route add %s via %s dev %s",d->prefix.toString().c_str(), d->portal->getIP().toString().c_str(), d->portal->getIfaceName().c_str());
		LOG_WARN("Adding route, '"<<SSFNET_STRING(temp)<<"'\n");
		system(temp);
	}
}

// used to register an event class.
SSF_REGISTER_EVENT(ArpEvent, ArpEvent::createArpEvent);
#else

namespace prime {
namespace ssfnet {


void TrafficPortal::init() {
	EmulationProtocol::init();
	getInterface()->inHost()->registerTrafficPortal(this);
}

IPPrefix::List& TrafficPortal::getNetworks() {
	return unshared.networks.read();
}

void TrafficPortal::transmit(Packet* pkt, VirtualTime deficit) {
	getInterface()->receiveEmuPkt(pkt);
}

bool TrafficPortal::receive(Packet* pkt, bool pkt_was_targeted_at_iface) {
	return false;
}

#endif /*SSFNET_EMULATION*/

} //namespace ssfnet
} //namespace prime

