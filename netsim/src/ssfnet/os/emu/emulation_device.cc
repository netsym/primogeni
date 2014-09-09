/**
 * \file emulation_device.h
 * \brief This is the interface for IOThreads to generic
 * emulation devices (i.e. an ssfgateway or xen host)
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

#include "sim/composite.h"

#include "os/emu/emulation_device.h"
#include "proto/emu/emulation_protocol.h"
#include "os/community.h"
#include "os/logger.h"
#include "os/packet.h"
#include "time.h"

#include <sys/select.h>
#include <sys/fcntl.h>


//uncomment this line to by pass the simulator and directly write emulation packets back out
//#define PRIME_SSFNET_IOLOOP

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(EmulationDevice);


/**
 * Used to preform async lookups
 *
 */
class EmulationNameResolutionCallBack: public NameResolutionCallBackWrapper {
public:
	EmulationNameResolutionCallBack(IPAddress ip_, EmulationEvent* evt, double deficit_)
	:ip(ip_), deficit(deficit_), pkt(evt->pkt), src_id(evt->src_id), iface(evt->iface) {
		evt->pkt=0;
	}

	virtual ~EmulationNameResolutionCallBack() {
	}

	/**
	 * called when a uid is searched for
	 */
	virtual void call_back(UID_t uid) {
		pkt->setFinalDestinationUID(uid);
		iface->getEmulationProtocol()->transmit(pkt, VirtualTime(deficit, VirtualTime::SECOND));
	}
	/**
	 * called when a mac or ip is searched for
	 */
	virtual void call_back(IPAddress ip, MACAddress mac) {
		LOG_ERROR("TapNameResolutionCallBack::call_back(IPAddress ip, MACAddress mac) should never be called!"<<endl);
	}
	/*
	 * called when the uid/mac/ip can't be found
	 */
	virtual void invalid_query() {
		LOG_WARN("The IP "<<ip<<" does not map to a valid IP!"<<endl);
		pkt->erase();
		iface=0;
	}
private:
	/*
	 * the src ip
	 */
	IPAddress ip;

	/* emu deficit */
	double deficit;

	/**
	 * The packet
	 */
	Packet* pkt;

	/** The UID of the virtual interface that either exports or
      imports this event. */
	UID_t src_id;

	/**
	 * The interface that either should import or export the packet
	 *
	 * If the pointer is not valid we need to look up the interface from the uid
	 */
	Interface* iface;
};


SSFNET_INT2PTR_MAP* EmulationDevice::registered_devices = 0;
int EmulationDevice::total_devs = 0;


class EmulationArrivalProcess : public prime::ssf::Process {
public:
	BlockingEmulationDevice* dev;
	EmulationArrivalProcess(BlockingEmulationDevice* _dev) :
		prime::ssf::Process(_dev->getCommunity(), true),
		dev(_dev) {}
	void action(); //! SSF PROCEDURE SIMPLE
};


//! SSF PROCEDURE SIMPLE
void EmulationArrivalProcess::action()
{
	//! SSF CALL
	dev->arrival_process(this);

	LOG_DEBUG("EmulationArrivalProcess::action(): waitOn()\n");
	waitOn();
}

EmulationDevice::EmulationDevice():community(0), id(++total_devs) {

}

EmulationDevice::~EmulationDevice() {
}

void EmulationDevice::init() {
	if(!getCommunity()) {
		LOG_ERROR("the community was not set!"<<endl)
	}
}

void EmulationDevice::wrapup() {
}

EmulationDeviceProxy* EmulationDevice::createProxyDevice(Community* com) {
	EmulationDeviceProxy* rv = new EmulationDeviceProxy();
	rv->setCommunity(com);
	rv->setEmulationDevice(this);
	return rv;
}

int EmulationDevice::getDeviceId(){
	return id;
}

Community* EmulationDevice::getCommunity() {
	return community;

}

void EmulationDevice::setCommunity(Community* _community) {
	community=_community;
}

int EmulationDevice::registerEmulationDevice(EmulationDevice* (*fct)(), int itype) {
	// allocate the global data structure if it has not been
	if(!registered_devices) registered_devices = new SSFNET_INT2PTR_MAP;

	// insert the mapping; check if duplicated
	if(!registered_devices->insert(SSFNET_MAKE_PAIR(itype, (void*)fct)).second)
		LOG_ERROR("duplicate emulation device type (impl_type=" << itype << ").");

	// we need to return something so that SSFNET_REGISTER_EMULATION_DEVICE works
	return 0;
}

EmulationDevice* EmulationDevice::createInstance(int itype) {
	// find the constructor of the given implementation type; create and
	// return a new instance
	if(!registered_devices)
		return 0;
	SSFNET_INT2PTR_MAP::iterator iter = registered_devices->find(itype);
	if(iter != registered_devices->end()) {
		void* fct = (*iter).second;
		return (*(EmulationDevice*(*)())fct)(); // call factory method
	}
	return 0;
}

EmulationDeviceProxy::EmulationDeviceProxy(): dev(0) {
}

EmulationDeviceProxy::~EmulationDeviceProxy(){
}

EmulationDevice* EmulationDeviceProxy::getEmulationDevice() {
	return dev;
}

void EmulationDeviceProxy::setEmulationDevice(EmulationDevice* _dev) {
	dev=_dev;
}

void EmulationDeviceProxy::init(){
	EmulationDevice::init();
}

void EmulationDeviceProxy::wrapup() {
	//no op
}

void EmulationDeviceProxy::registerEmulationProtocol(EmulationProtocol* emu_proto) {
	dev->registerProxiedEmulationProtocol(this, emu_proto);
	emu_proto->setEmulationDevice(this);
	if(!protomap.insert(SSFNET_MAKE_PAIR(emu_proto->getInterface()->getIP(),emu_proto)).second) {
		LOG_ERROR("Duplicate ip_address '"<< emu_proto->getInterface()->getIP()<<"'detected"<<endl)
	}
}

void EmulationDeviceProxy::exportPacket(Interface* iface, Packet* pkt) {
	EmulationEvent* evt= new EmulationEvent(iface, pkt, false);
	evt->setSSFNetEventType(SSFNetEvent::SSFNET_EMU_PROXY_EVT);
	//XXX need to send this to the remote device somehow......
	LOG_ERROR("EmulationDeviceProxy::exportPacket NOT DONE"<<endl);
}

void EmulationDeviceProxy::handleProxiedEvent(EmulationEvent* evt) {
	LOG_WARN("Proxied an event to another emulation proxy device! Dropping packet."<<endl);
	evt->free();
}

bool EmulationDeviceProxy::isActive() {
	return dev->isActive();
}

bool EmulationDeviceProxy::requiresSingleInstancePerHost() {
	return false;
}

void EmulationDeviceProxy::registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto) {
	ssfnet_throw_exception(SSFNetException::other_exception,"Cannot call registerProxiedEmulationProtocol() on an EmulationDeviceProxy!");
}

int EmulationDeviceProxy::getDeviceType() {
	return EMU_PROXY_DEVICE;
}

SSFNET_REGISTER_EMU_DEVICE(EmulationDeviceProxy, EMU_PROXY_DEVICE);


BlockingEmulationDevice::BlockingEmulationDevice(): stop(false), input_channel(0), output_channel(0) {
}

BlockingEmulationDevice::~BlockingEmulationDevice() {
}

void BlockingEmulationDevice::init() {
	EmulationDevice::init();
	input_channel = new prime::ssf::inChannel
			(getCommunity(), BlockingEmulationDevice::read, this);
	assert(input_channel);
	EmulationArrivalProcess* reader = new EmulationArrivalProcess(this);
	assert(reader);
	reader->waitsOn(input_channel);

	output_channel = new prime::ssf::outChannel
			(getCommunity(), BlockingEmulationDevice::write, this);
	assert(output_channel);
	LOG_DEBUG("BlockingEmulationDevice::init(), getCommunity()="<<getCommunity()<<endl)
}

void BlockingEmulationDevice::wrapup() {
	EmulationDevice::wrapup();
	stop=true;
}

//! SSF PROCEDURE SIMPLE
void BlockingEmulationDevice::arrival_process(prime::ssf::Process* p) {
	SSFNetEvent** evts = (SSFNetEvent**)input_channel->activeEvents();
	/// HEREHERE
	//printf("%d: realnow=%Dlld: arrival_process() got event at time %lld\n", SSF_USE_SHARED_RO(whoami), ((prime::ssf::ssf_universe*)SSF_USE_PRIVATE(universe))->wallclock_to_ltime(), getCommunity()->now()); fflush(0);

	for(int i=0; evts && evts[i]; i++) {
#ifdef PRIME_SSFNET_IOLOOP
		switch(evts[i]->getSSFNetEventType())
		{
		case SSFNetEvent::SSFNET_ARP_EVT:
			output_channel->write(evts[i],1);
			break;
		case SSFNetEvent::SSFNET_EMU_NORMAL_EVT:
		{
			EmulationEvent* evt = (EmulationEvent*)evts[i];
			if(evt && evt->pkt) {
				IPv4Message* iphdr = (IPv4Message*)evt->pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4);
				IPAddress d = iphdr->getDst();
				EmulationProtocol* emu_proto = this->ip2EmulationProtocol(d);
				if(emu_proto) {
					LOG_DEBUG("Looping emu pkt\n");
					evt->iface = emu_proto->getInterface();
					evt->src_id = evts[i]->iface->getUID();
					output_channel->write(evt);
				}
				else {
					LOG_WARN("Can't loop the packet out because I don't know how to export to IP "<<iphdr->getDst()<<endl);
					evt->free();
				}
			}
		}
		break;
		case SSFNetEvent::SSFNET_EMU_PROXY_EVT:
			LOG_ERROR("Should never see emulation proxy events!"<<endl);
			break;
		default:
			LOG_ERROR("Unknown emulation event type!"<<endl);
			break;
		}
		continue;
#endif
		switch(evts[i]->getSSFNetEventType())
		{
		case SSFNetEvent::SSFNET_ARP_EVT:
			output_channel->write(evts[i],1);
			break;
		case SSFNetEvent::SSFNET_EMU_NORMAL_EVT:
		{
			// note that the constructor will directly take the payload from
			// the external event, which means we must make sure the event is
			// not aliased before we can alter the event!
			assert(!evts[i]->aliased());
			EmulationEvent* evt = (EmulationEvent*)evts[i];

#ifdef SSFNET_EMU_TIMESYNC
			// compute the deficit
			struct timeval tv;
			gettimeofday(&tv, 0);
			double now = tv.tv_sec+tv.tv_usec*1e-6;
			double deficit = now - evt->send_time;
			deficit += evt->clock_drift;
			if(deficit < 0) deficit = 0; // in case we have already over compensated
			LOG_DEBUG("TIMESYNC: now="<<now<<", sent="<<evt->send_time<<", drift="<<evt->clock_drift<<" (one-way) deficit="<<deficit<<endl);

			// [IMPORTANT] It is wrong to count the deficit twice; natural
			// thinging was that we should consider the latency between the
			// simulation gateway and the simulator in both ways; however, if
			// we double the deficit here, it would potentially cause the
			// arriving packets to be out of order in the time when they are
			// delivered to the destination and this will have a detrimental
			// effect on the transport protocols like tcp which depends on
			// in-order packet arrivals!!!
			/*deficit *= 2;*/
#else
			double deficit=0;
#endif
			if(!evt->iface) {
				LOG_ERROR("IFACE WAS NULL!"<<endl);
			}

			if(evt->needToLookupDest()) {
				ACK_COUT("import packet (arrival process) aysn name", ((BaseEntity*)NULL) )
								IPv4Message* msg = SSFNET_DYNAMIC_CAST(IPv4Message*,evt->pkt->getMessageByArchetype(SSFNET_PROTOCOL_TYPE_IPV4));
				if(!msg) {
					LOG_ERROR("Something went wrong! the pkt does not seem to have an ip proto!\n");
				}
				IPAddress ipaddr = msg->getDst();
				getCommunity()->asynchronousNameResolution(ipaddr,new EmulationNameResolutionCallBack(ipaddr, evt, deficit));
			}
			else {
				ACK_COUT("import packet (arrival process) transmit", ((BaseEntity*)NULL) )
								Packet* pkt = evt->pkt;
				evt->pkt=0;
				evt->iface->getEmulationProtocol()->transmit(pkt, VirtualTime(deficit, VirtualTime::SECOND));
			}
		}
		break;
		case SSFNetEvent::SSFNET_EMU_PROXY_EVT:
			LOG_ERROR("Should never see emulation proxy events!"<<endl);
			break;
		default:
			LOG_ERROR("Unknown emulation event type!"<<endl);
			break;
		}


	}
}


void BlockingEmulationDevice::read(prime::ssf::inChannel* ic, void* context) {
	LOG_DEBUG("BlockingEmulationDevice::read("<<ic<<","<<context<<")"<<endl);
	((BlockingEmulationDevice*)context)->reader_thread();
}

void BlockingEmulationDevice::write(prime::ssf::outChannel* oc, void* context) {
	LOG_DEBUG("BlockingEmulationDevice::write("<<oc<<","<<context<<")"<<endl);
	((BlockingEmulationDevice*)context)->writer_thread();
}

class ByPassArrivalProcess : public prime::ssf::Process {
public:
	EmulationByPassServer* dev;
	ByPassArrivalProcess(EmulationByPassServer* _dev) :
		prime::ssf::Process(_dev->getCommunity(), true),
		dev(_dev) {}
	void action(); //! SSF PROCEDURE SIMPLE
};


//! SSF PROCEDURE SIMPLE
void ByPassArrivalProcess::action()
{
	//! SSF CALL
	dev->arrival_process(this);

	LOG_DEBUG("ByPassArrivalProcess::action(): waitOn()\n");
	waitOn();
}

ConnectedSocket::ConnectedSocket(int fd_, uint32 buf_len):
					fd(fd_), start(0), end(0),len(buf_len), buf(new byte[buf_len]){

#ifdef PRIME_SSF_MACH_X86_DARWIN
	LOG_ERROR("Cannot use TAP Devices on DARWIN!");
#else
	/* Force the network socket into nonblocking mode */
	int flags = fcntl(fd,F_GETFL,0);
	if(flags == -1) {
		LOG_ERROR("ACK! flags=-1"<<endl);
	}
	fcntl(fd, F_SETFL, flags | O_NONBLOCK | SHUT_RD);
#endif /*PRIME_SSF_ARCH_X86_DARWIN*/
}

ConnectedSocket::~ConnectedSocket() {
	if(fd>=0)
		close(fd);
	if(buf)
		delete[] buf;
}

struct ByPassHeader {
	uint32_t com_id;
	uint32_t serial;
	uint32_t len;
};

bool ConnectedSocket::read(prime::ssf::inChannel* input_channel) {
	/**
	 * The format of a bypass packet is
	 *
	 * com_id (uint32)
	 * serial (uint32)
	 * virutal time (uint64)
	 * payload length (uint32)
	 * byte[] (the length above)
	 *
	 */
	bool rv=false;

	int ret = recv(fd, buf+end, len-end , 0);
	if(ret > 0) {
		//got something
		end+=ret;
		if(end == len) {
			//we filled the buffer....
			//more is probably waiting
			rv=true;
		}
	}
	//check for a header
	while(end-start >=sizeof(ByPassHeader)) {
		//we have a header
		ByPassHeader* hdr = (ByPassHeader*)(buf+start);
		if(end-start>=sizeof(ByPassHeader)+hdr->len) {
			//we have a whole payload...
			//XXX create the event and insert into the
			LOG_ERROR("Not done\n");
			start+=sizeof(ByPassHeader)+hdr->len;
		}
	}
	if(end==start)
		end=start=0;
	else if (start != 0) {
		//if we are near the end of the buffer lets move it front
		if(start > .75 * len) {
			memcpy(buf,buf+start,end-start);
			start=0;
			end-=start;
		}
	}
	return rv;
}

EmulationByPassServer::EmulationByPassServer(Community* community_, int port_):
						community(community_), stop(false), port(port_), listen_fd(-1), max_fd(-1)
{

}

EmulationByPassServer::~EmulationByPassServer() {

}

void EmulationByPassServer::init() {
#ifdef PRIME_SSF_MACH_X86_DARWIN
	LOG_ERROR("Cannot use TAP Devices on DARWIN!");
#else
	//setup channel to the server
	input_channel = new prime::ssf::inChannel
			(community, EmulationByPassServer::run, this);
	assert(input_channel);
	ByPassArrivalProcess* reader = new ByPassArrivalProcess(this);
	assert(reader);
	reader->waitsOn(input_channel);

	struct sockaddr_in servaddr;

	/* Set up to be a daemon listening on port BASE_BYPASS_PORT+community->getCommunityId() */
	listen_fd = socket(AF_INET, SOCK_STREAM, 0);
	memset(&servaddr, 0, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port   = htons(BASE_BYPASS_PORT+community->getCommunityId());
	bind(listen_fd, (struct sockaddr *) &servaddr, sizeof(servaddr));

	/* Force the network socket into nonblocking mode */
	int flags = fcntl(listen_fd,F_GETFL,0);
	if(flags == -1) {
		LOG_ERROR("ACK! flags=-1"<<endl);
	}
	fcntl(listen_fd, F_SETFL, flags | O_NONBLOCK | SHUT_RD);
#endif /*PRIME_SSF_ARCH_X86_DARWIN*/

	LOG_DEBUG("EmulationByPassServer::init(), getCommunity()="<<getCommunity()<<", listening on port "<<(BASE_BYPASS_PORT+community->getCommunityId())<<endl)
}

void EmulationByPassServer::arrival_process(prime::ssf::Process* p){

	EmulationEvent** evts = (EmulationEvent**)input_channel->activeEvents();
	for(int i=0; evts && evts[i]; i++) {
		//XXX insert into sim..
		LOG_ERROR("not done!\n");
	}
}

void EmulationByPassServer::run(prime::ssf::inChannel* ic, void* context) {
	EmulationByPassServer* svr = (EmulationByPassServer*)context;
	fd_set base_fds, fds;
	//setup base_fd set
	FD_ZERO(&base_fds);
	FD_SET(svr->listen_fd,&base_fds);
	ConnectedSocket::List::iterator it;
	svr->max_fd=svr->listen_fd+1;
	for(;!svr->stop;) {
		//FD_COPY isn't standard... a simple assignment should work though....
		//FD_ZERO(&fds);
		fds=base_fds;
		select(svr->max_fd, &fds, NULL, NULL, NULL);
		LOG_DEBUG("Got something..."<<endl);
		int max_loops=500;
		bool more_packets=false;
		do {
			//see if anyone is trying to connect
			int fd = -1;
			do {
				fd = accept(svr->listen_fd, NULL, NULL);
				if (fd != -1) {
					/* Someone connected.  */
					svr->max_fd = svr->listen_fd;
					ConnectedSocket* t = new ConnectedSocket(fd, CON_SOCKET_BUF_LEN);
					svr->sockets.push_back(t);
					FD_SET(svr->listen_fd,&base_fds);
					for(it = svr->sockets.begin();it!=svr->sockets.end();it++) {
						if(svr->max_fd < (*it)->getFD()) {
							svr->max_fd = (*it)->getFD();
						}
					}
					svr->max_fd++;
				}
			} while(fd!=-1);
			//see if anyone has sent data
			for(it = svr->sockets.begin();it!=svr->sockets.end();it++) {
				if(FD_ISSET((*it)->getFD(),&fds)) {
					more_packets=more_packets || (*it)->read(svr->input_channel);
				}
			}
			max_loops--;
		} while(more_packets && max_loops>0);
	}
	for(it = svr->sockets.begin();it!=svr->sockets.end();it++) {
		delete (*it);
	}
	svr->sockets.clear();
	close(svr->listen_fd);
}


} //namespace ssfnet
} //namespace prime

#endif /*SSFNET_EMULATION*/
