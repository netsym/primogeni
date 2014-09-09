/**
 * \file tap_device.cc
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

#ifdef SSFNET_EMULATION

#define EMU_BUFSIZ 300000 // buffer up to 200 pkts of 1500 bytes

#include "os/emu/tap_device.h"
#include "os/io_mgr.h"
#include "os/logger.h"
#include "proto/emu/emulation_protocol.h"
#include "os/partition.h"

#include <stdio.h>
#include <netdb.h>
#include <sys/select.h>
#include <sys/types.h>
#include <unistd.h>
#include <iomanip>
#include <errno.h>
#include <net/ethernet.h>

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(TAPDevice);
#define BUFFSIZE 300000 // buffer up to 200 pkts of 1500 bytes


#ifdef USE_TX_RING
int VETH::c_buffer_sz   = 1024*8;
int VETH::c_buffer_nb   = 1024;
int VETH::c_sndbuf_sz   = 0;
#endif
int VETH::c_mtu         = 0;
int VETH::mode_loss     = 1;
bool VETH::sconfiged = false;


TAPDevice::TAPDevice():maxfd(0) {
}

TAPDevice::~TAPDevice(){
	for(VETH::UIDMap::iterator i = uid2veth.begin(); i!=uid2veth.end();i++) {
		delete i->second;
	}
}

void TAPDevice::init(){
	BlockingEmulationDevice::init();
	LOG_WARN("TAPDevice::init() input_channel="<<input_channel<<", output_channel="<<output_channel<<endl);
	for(VETH::UIDMap::iterator i = uid2veth.begin(); i!=uid2veth.end();i++) {
		i->second->init();
		if(maxfd < i->second->getRxFD()) {
			maxfd = i->second->getRxFD();
		}
		ip2mac.insert(SSFNET_MAKE_PAIR(i->second->getIP(),i->second->getMAC()));
   		ip2veth.insert(SSFNET_MAKE_PAIR(i->second->getIP(),i->second));
   		mac2veth.insert(SSFNET_MAKE_PAIR(i->second->getMAC(),i->second));
	}
	sim_net=getCommunity()->getPartition()->getTopnet()->getIPPrefix();
	mymac=MACAddress(getCommunity()->getPartition()->getTopnet()->getUID());
	LOG_DEBUG("Simulated network prefix is "<<sim_net<<", mac="<<mymac<<endl);
	LOG_WARN("MAC/VETH MAP"<<endl);
	for(VETH::MACMap::iterator it = mac2veth.begin(); it != mac2veth.end();it++) {
		LOG_WARN("\t"<<it->first<<" -- "<<it->second<<endl);
	}
}

void TAPDevice::wrapup() {
	BlockingEmulationDevice::wrapup();
}

void TAPDevice::registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto) {
	ssfnet_throw_exception(SSFNetException::other_exception,"Cannot call registerProxiedEmulationProtocol() on an TAPDevice!");
}

void TAPDevice::registerEmulationProtocol(EmulationProtocol* emu_proto) {
	if(!TAPEmulation::getClassConfigType()->isSubtype(emu_proto->getTypeId())) {
		LOG_ERROR("the TAPEmulation device only supports emulation protocols of type TAPEmulation! Found "<<emu_proto->getTypeName()<<endl);
	}
	UID_t host = emu_proto->getParent()->getParent()->getUID();
	MACAddress mac = MACAddress(emu_proto->getInterface()->getUID());
	LOG_DEBUG("registerEmulationProtocol "<<emu_proto->getUName()
			<<"(host="<<host<<", iface="<<emu_proto->getParent()->getUID()<<") with src ip "<<emu_proto->getInterface()->getIP()<<", mac="<<mac<<endl);


	VETH* veth= new VETH(emu_proto, this);
	uid2veth.insert(SSFNET_MAKE_PAIR(emu_proto->getInterface()->getUID(),veth));
	emu_proto->setEmulationDevice(this);

}

void TAPDevice::exportPacket(Interface* iface, Packet* pkt) {
	//LOG_DEBUG("exporting packet from "<<iface->getUName()<<", ip="<<iface->getIP()<<endl);
	EmulationEvent* evt= new EmulationEvent(iface, pkt, false);
	output_channel->write(evt);
}

void TAPDevice::handleProxiedEvent(EmulationEvent* evt) {
	LOG_WARN("TapVPN devices cannot _yet_ proxy evts!"<<endl);
	evt->free();
}

EmulationProtocol* TAPDevice::ip2EmulationProtocol(IPAddress& ip) {
	VETH::IPMap::iterator iter = ip2veth.find(ip);
	if(iter == ip2veth.end()) {
		LOG_WARN("asked to handle the departure of an unknown ip "<<ip);
		return 0;
	}
	return (*iter).second->getEmuProto();
}

void TAPDevice::reader_thread() {
	maxfd++; //it should be 1 greater than the max fd...
	LOG_DEBUG("Starting tap read thread"<<endl)
	//its assumed that all of the taps are opened and ready to go.... we just need to setup the FDs...
	VETH::UIDMap::iterator veth_it;

	fd_set base_fds, fds;
	//setup base_fd set
	FD_ZERO(&base_fds);

	for(veth_it = uid2veth.begin();veth_it!=uid2veth.end();veth_it++) {
		FD_SET(veth_it->second->getRxFD(),&base_fds);
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
			for(veth_it = uid2veth.begin();veth_it!=uid2veth.end();veth_it++) {
				more_packets=more_packets || veth_it->second->process_pkts(100);
			}
			max_loops--;
		} while(more_packets && max_loops>0);
	}
}

void TAPDevice::writer_thread() {
	LOG_DEBUG("Starting tap read thread"<<endl);
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
				VETH::UIDMap::iterator veth_it = uid2veth.find(evt->iface->getUID());

				if(veth_it == uid2veth.end()) {
					LOG_WARN("Tried to export an emulation event from an interface which ("<< evt->iface->getUName()<<
							") which is not registered veth! Dropping packet."<<endl);
					// the event needs to be reclaimed
					evt->free();
				}
				else {
					IPAddress tgt = ((IPv4Message*)(evt->pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4)))->getDst();
					MACAddress* mac = lookupMAC(tgt);
					if(mac) {
						VETH::MACMap::iterator veth_it1 = mac2veth.find(*mac);
						if(veth_it1 == mac2veth.end()) {
							LOG_WARN("invalid mac ip="<<tgt<<", mac="<< *mac<<endl);
						}
						else {
							veth_it1->second->sendPacket(evt,*mac);
						}
						// the event needs to be reclaimed
						evt->free();
					}
					else {
						//XXX how to handle this?
						//we need to look for traffic portals.....
						LOG_ERROR("XXX --- need to finish this"<<endl)
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
						LOG_DEBUG("Sending mac that was waiting for arp response...."<<endl);
						assert((*arp_it)->veth && (*arp_it)->evt);
						(*arp_it)->veth->sendPacket((*arp_it)->evt,*mac);
						// the event needs to be reclaimed
						delete (*arp_it);
						arp_it=watingForArps.erase(arp_it);
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


void TAPDevice::insertIntoSim(EthernetHeader* eth, IPv4RawHeader* ip, uint32_t len) {
	// we need to copy the message to a new buffer (the receive
	// buffer will be reused for next message)
	LOG_DEBUG("insertIntoSim, len="<<len<<endl)
			char* buf = new char[len];
	assert(buf);
	memcpy(buf, (char*)eth, len);
	eth = (EthernetHeader*)buf; // update the eth header pointer
	ip = (IPv4RawHeader*)(buf+sizeof(EthernetHeader)); // update the ip header pointer


	//find the iface in the sim which corresponds to the src mac
	MACAddress mac(eth->getSrc());
	VETH::MACMap::iterator it = mac2veth.find(mac);
	if(it == mac2veth.end()) {
		for(it = mac2veth.begin(); it != mac2veth.end();it++) {
			LOG_WARN(it->first<<" -- "<<it->second<<endl);
		}
		LOG_ERROR("Tried to import a packet from an unknown iface, mac = "<<mac<<", eth=\n"<<*eth<<endl)
	}

	// create the event (the new buffer is handed off to the event
	// as the payload) and pass it subsequently to the simulator
	UID_t dst_uid=getCommunity()->synchronousNameResolution(IPAddress(ip->getDst()));
	Packet* pkt=new Packet(SSFNET_PROTOCOL_TYPE_IPV4, (prime::ssfnet::byte*)buf, len, sizeof(EthernetHeader), dst_uid);

	EmulationEvent* evt=0;
	if(!dst_uid) {
		evt = new EmulationEvent(it->second->getEmuProto()->getInterface(), pkt,true);
		LOG_DEBUG("created emu evt with a need for async name res"<<endl)
	}
	else {
		evt = new EmulationEvent(it->second->getEmuProto()->getInterface(), pkt,false);
		LOG_DEBUG("created emu evt withOUT a need for async name res"<<endl)
	}

	assert(evt);
	//std::cout << "**** get event at " << getCommunity()->now() << ", dst_uid=" << dst_uid << ", dest ip=" << IPAddress(ip->getDst()) << std::endl;
	input_channel->putRealEvent(evt);
	// no need to reclaim buf or evt!
}

bool TAPDevice::isActive() {
	return !stop;
}

bool TAPDevice::requiresSingleInstancePerHost() {
	return false;
}

int TAPDevice::getDeviceType() {
	return EMU_TAP_DEVICE;
}

void TAPDevice::handleArp(VETH* veth, EthernetHeader* eth, ARPHeader* arp) {
	LOG_DEBUG("Got arp\n"<<*eth<<"\n"<<*arp<<"\n");
	if(arp->isArpRequest()) {
		IPAddress ip=IPAddress(arp->getDstIP());
		LOG_DEBUG("Looking for mac of ip "<<ip<<endl);
		if(sim_net.contains(ip)) {
			LOG_DEBUG("It is in the simulation, send response! src="<<IPAddress(arp->getSrcIP())<<", dst="<<ip<<endl);
			//veth->sendArpResponse(MACAddress::MACADDR_BROADCAST_FF, ip, IPAddress(arp->getSrcIP()), mymac);
			//veth->sendArpResponse(MACAddress::MACADDR_BROADCAST_FF, IPAddress(arp->getSrcIP()), ip, mymac);
			veth->sendArpResponse(MACAddress(eth->getSrc()), IPAddress(arp->getSrcIP()), ip, mymac);
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

MACAddress* TAPDevice::lookupMAC(IPAddress ip) {
	IP2MACMap::iterator rv = ip2mac.find(ip);
	if(rv != ip2mac.end())
		return &(rv->second);
	if(sim_net.contains(ip)) {
		return &mymac;
	}
	return 0;
}


SSFNET_REGISTER_EMU_DEVICE(TAPDevice, EMU_TAP_DEVICE);



#ifdef PRIME_SSF_MACH_X86_DARWIN
VETH::VETH(EmulationProtocol* emuproto_, TAPDevice* dev_)
{
	LOG_ERROR("Not supported on the mac!");
}
#else
VETH::VETH(EmulationProtocol* emuproto_, TAPDevice* dev_) :
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
	ip = emuproto->getInterface()->getIP();
}
#endif

VETH::~VETH() {
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
void VETH::init() {
	LOG_ERROR("Not supported on the mac!");
}
#else
void VETH::init() {

	char dev_str[ emuproto->getInterface()->getName()->length() + 1];
	sprintf(dev_str,"veth%llu", emuproto->getInterface()->getUID());

	//setup the pcap rx channel
	char errbuf[PCAP_ERRBUF_SIZE];

	LOG_DEBUG("Creating pcap handle for "<<emuproto->getInterface()->getName()<<" (veth"<<emuproto->getInterface()->getUID()<<")\n");
	pcap_handle = pcap_open_live(dev_str, 65535, 0, -1, errbuf); //dev name, max len, promiscuous, timeout, error buffer
	if(!pcap_handle) {
		LOG_ERROR("Couldn't open device "<<emuproto->getInterface()->getName()
				<< "(" << SSFNET_STRING(dev_str) << ") on host " << emuproto->getParent()->getUName() << ", err:"<<errbuf<<endl);
	}
	if (pcap_setnonblock(pcap_handle,1,errbuf) == -1) {
		LOG_ERROR("Couldn't change "<<emuproto->getInterface()->getName()
				<< "(" << SSFNET_STRING(dev_str) << ") on host " << emuproto->getParent()->getUName() << " to non-blocking! err:"<<errbuf<<endl);
	}
	if (pcap_getnonblock(pcap_handle,errbuf)==0) {
		LOG_ERROR("Couldn't change "<<emuproto->getInterface()->getName()
				<< "(" << SSFNET_STRING(dev_str) << ") on host " << emuproto->getParent()->getUName() << " to non-blocking! err:"<<errbuf<<endl);
	}
	rx_fd = pcap_get_selectable_fd(pcap_handle);
	if(rx_fd == -1) {
		LOG_ERROR("Libpcap couldn't get a selectable FD for "<<emuproto->getInterface()->getName()
				<< "(" << SSFNET_STRING(dev_str) << ") on host " << emuproto->getParent()->getUName() <<endl);
	}
	if(pcap_setdirection(pcap_handle,PCAP_D_IN)==-1) {
		LOG_ERROR("Libpcap couldn't set the capture direction for "<<emuproto->getInterface()->getName()
				<< "(" << SSFNET_STRING(dev_str) << ") on host " << emuproto->getParent()->getUName() << endl);
	}

	struct ifreq tx_ifr; /* points to one interface returned from ioctl */

	//setup the tx channel
	tx_fd = socket(PF_PACKET, SOCK_RAW, htons(ETH_P_ALL));
	if(tx_fd == -1) {
		LOG_ERROR("Unable to open (tx) raw socket for "<<emuproto->getInterface()->getName()
				<< "(" << SSFNET_STRING(dev_str) << ") on host " << emuproto->getParent()->getUName() << "!"<<endl);
	}

	/* initialize interface struct */
	strncpy (tx_ifr.ifr_name, dev_str, sizeof(tx_ifr.ifr_name));

	/*get the mac 
	if(ioctl(tx_fd, SIOCGIFHWADDR, &tx_ifr) == -1) {
		perror("iotcl");
		LOG_ERROR("Unable to ioctl the tx socket for "<<emuproto->getInterface()->getName()<<"!"<<endl);
	}
	mac=MACAddress((uint8_t*)(&tx_ifr.ifr_hwaddr.sa_data));*/
	mac=emuproto->getInterface()->getMAC();
	LOG_DEBUG("MAC for  "<<emuproto->getInterface()->getName()<<":"<<mac<<endl);

	/* set mtu */
	if(c_mtu) {
		/* update the mtu through ioctl */
		tx_ifr.ifr_mtu = c_mtu;
		if(ioctl(tx_fd, SIOCSIFMTU, &tx_ifr) == -1)
		{
			perror("iotcl");
			LOG_ERROR("Unable to ioctl the tx socket for "<<emuproto->getInterface()->getName()<<"!"<<endl);
		}
	}

	/* Get the iface index */
	if(ioctl(tx_fd, SIOCGIFINDEX, &tx_ifr) == -1) {
		perror("iotcl");
		LOG_ERROR("Unable to ioctl the tx socket for "<<emuproto->getInterface()->getName()<<"!"<<endl);
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
		LOG_ERROR("Unable to bind the tx socket for "<<emuproto->getInterface()->getName()<<"!"<<endl);
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
		LOG_ERROR("Unable to set the (tx) packet loss socket option for "<<emuproto->getInterface()->getName()<<"!"<<endl);
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
		LOG_ERROR("Unable to set the packet tx_ring option for "<<emuproto->getInterface()->getName()<<"!"<<endl);
	}
	/* change send buffer size */
	if(c_sndbuf_sz) {
		LOG_DEBUG("Changing send buffer size to "<<c_sndbuf_sz<<endl);
		if (setsockopt(tx_fd, SOL_SOCKET, SO_SNDBUF, &c_sndbuf_sz,
				sizeof(c_sndbuf_sz))< 0) {
			perror("iotcl");
			LOG_ERROR("Unable to set the send buffer size for "<<emuproto->getInterface()->getName()<<"!"<<endl);
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
		LOG_ERROR("Unable memory map tx_ring/socket for "<<emuproto->getInterface()->getName()<<"!"<<endl);
	}

#endif

	LOG_DEBUG("Ready to send/recv on "<<emuproto->getInterface()->getName()<<"!"<<endl);
}
#endif

#ifndef PRIME_SSF_MACH_X86_DARWIN
#ifdef USE_TX_RING
struct tpacket_hdr * VETH::nextTxFrame() {
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

void VETH::sendArpResponse(MACAddress requester_mac, IPAddress requester_ip, IPAddress requested_ip, MACAddress requested_mac) {
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
	requested_mac.toRawMac(eth->getSrc());
	requester_mac.toRawMac(eth->getDst());

	eth->setFrameType(ETH_P_ARP);

	requested_mac.toRawMac(arp->getSrcMac());
	requester_mac.toRawMac(arp->getDstMac());
	arp->setDstIP((uint32)requester_ip);
	arp->setSrcIP((uint32)requested_ip);
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


void VETH::sendArpRequest(uint32 req_ip) {
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

void VETH::sendPacket(EmulationEvent* evt, MACAddress& dstmac) {
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

bool VETH::process_pkts(int max_pkts) {
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

void VETH::process_pkt(u_char * veth_, const struct pcap_pkthdr * pkt_meta, const u_char * pkt) {
#ifdef PRIME_SSF_MACH_X86_DARWIN
LOG_ERROR("not supported on mac")
#else
	VETH* veth = (VETH*) veth_;
	EthernetHeader* eth=(EthernetHeader*)pkt;
	uint16_t frame_type;
	IPv4RawHeader* ip=0;
	if(eth->isVLAN()) {
		LOG_WARN("We don't handle vlans for veths!\n");
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
		veth->dev->handleArp(veth,eth,(ARPHeader*)(pkt+sizeof(EthernetHeader)));
	}
	else if(EthernetHeader::isIPv4(frame_type)) {
		LOG_DEBUG(" Got IPv4 pkt [size="<<(ip->getLen()-ip->getHdrLen())<<"]\n\t"<<*ip<<"\n");
		veth->dev->insertIntoSim(eth,ip,pkt_meta->caplen);
	}
	else if(EthernetHeader::isIPv6(frame_type)) {
		LOG_WARN("Received an IPv6 Message... dropping it."<<endl)
	}
	else {
		LOG_WARN("Unknown protocol following Ethernet header!"<<endl << *eth <<endl);
	}
#endif
}


PRIME_OSTREAM& operator<< (PRIME_OSTREAM& os, VETH const& veth) {
	return os << "["<<veth.emuproto->getInterface()->getName()<<" tx_fd="<<veth.tx_fd<<", rx_fd="<<veth.rx_fd<<"]";
}


} //namespace ssfnet
} //namespace prime

#endif /*SSFNET_EMULATION*/
