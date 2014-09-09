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
 *
 */

#ifdef SSFNET_EMULATION

#ifndef __EMUDEVICE_H__
#define	 __EMUDEVICE_H__

#include "ssf.h"
#include "os/ssfnet_types.h"
#include "os/emu/emulation_event.h"

namespace prime {
namespace ssfnet {


class EmulationDevice;
class Interface;
class Community;
class EmulationProtocol;
class Packet;
class Community;
class EmulationDeviceProxy;

#define EMU_PROXY_DEVICE 0
#define EMU_OPEN_VPN_DEVICE 1
#define EMU_TAP_DEVICE 3
#define EMU_VNICK_DEVICE 4
#define EMU_TRAFFIC_PORTAL 5
#define CON_SOCKET_BUF_LEN 1024*50 /* 50K */
#define BASE_BYPASS_PORT 8100

/**
 * \brief A base class for all emulation devices
 *
 */
class EmulationDevice {
public:
	typedef SSFNET_LIST(EmulationDevice*) List;
	typedef SSFNET_MAP(int,EmulationDevice::List*) ListMap;
	typedef SSFNET_MAP(uint32_t, EmulationProtocol*) EmulationProtocolMap;
	/**
	 * the constructor
	 */
	EmulationDevice();

	/**
	 * the deconstructor
	 */
	virtual ~EmulationDevice();

	/** Initialize the device.
	 * derived classes must implement this
	 */
	virtual void init();

	/**
	 * close the emulation device
	 */
	virtual void wrapup()=0;

	/** Called by the I/O manager to register an interface/protocol with this device.
	 * Once the device is setup it should call back to the EmulationProtocol with itself as
	 * the emulation device.
	 */
	virtual void registerEmulationProtocol(EmulationProtocol* emu_proto)=0;

	/**
	 * proxied devices need to be able to register the protocol connected to them.....
	 */
	virtual void registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto)=0;

	/**
	 * called by EmulationProtocols to export the pkt
	 */
	virtual void exportPacket(Interface* iface, Packet* pkt)=0;

	/**
	 * called by the i/o manager to handle proxied emulation evts
	 */
	virtual void handleProxiedEvent(EmulationEvent* evt)=0;

	/**
	 * returns whether this emulation device is ready
	 */
	virtual bool isActive()=0;

	/**
	 * If this type of protocol requires that only one instance exist on a host
	 * then only _1_ I/O manager can create one of these devices. All other
	 * I/O managers must proxy access to the owning I/OManager.
	 * then
	 */
	virtual bool requiresSingleInstancePerHost() = 0;

	/**
	 * Get the type of emulation device
	 */
	virtual int getDeviceType() = 0;

	/**
	 * If requiresSingleInstancePerHost() is true then I/O who need an instance of this device must
	 * create proxies of the original using this function.
	 */
	EmulationDeviceProxy* createProxyDevice(Community* com);

	/**
	 * get the community
	 */
	Community* getCommunity();

	/*
	 * set the iomanager
	 */
	void setCommunity(Community* _community);

	/**
	 * get the id of this device (its auto assigned durign construction
	 */
	int getDeviceId();

	/**
	 * whether this dev is a proxy
	 */
	virtual bool isEmulationDeviceProxy() { return false; }

	/**
	 * whether this dev is blocking
	 */
	virtual bool isBlockingEmulationDevice() { return false; }

	/**
	 * whether this dev is polling
	 */
	virtual bool isPollingEmulationDevice() { return false; }

	/**
	 * This method is called by the SSFNET_REGISTER_EMU_DEVICE macro to
	 * register a emulation device class. The method should not be
	 * publicly accessible. Yet, we make it public in order to make the
	 * macro to work properly.
	 */
	static int registerEmulationDevice(EmulationDevice* (*fct)(), int itype);

	/**
	 * Create a new instance of the emulation device identified by the
	 * given implementation type. Return NULL if the implementation type
	 * cannot be found (has not been registered earlier).
	 */
	static EmulationDevice* createInstance(int itype);

protected:
	Community* community;
private:
	int id;
	static SSFNET_INT2PTR_MAP* registered_devices;
	static int total_devs;


};

/**
 * Each emulation device defined in the system must declare itself
 * using the macro SSFNET_REGISTER_EMU_DEVICE. The macro has two
 * arguments: the name of the emulation device class, and the unique
 * implementation type of the protocol message.
 */
#define SSFNET_REGISTER_EMU_DEVICE(name, itype) \
		static prime::ssfnet::EmulationDevice* prime_ssfnet_new_emu_device_##name() \
		{ return new name; } \
		int prime_ssfnet_register_emu_device_##name = \
		EmulationDevice::registerEmulationDevice(prime_ssfnet_new_emu_device_##name, itype);



class EmulationDeviceProxy : public EmulationDevice {
public:
	/**
	 * the constructor
	 */
	EmulationDeviceProxy();

	/**
	 * the deconstructor
	 */
	virtual ~EmulationDeviceProxy();

	/** Initialize the device.
	 * derived classes must implement this
	 */
	virtual void init();

	/**
	 * close the emulation device
	 */
	virtual void wrapup();

	/**
	 * get the dev
	 */
	EmulationDevice* getEmulationDevice();

	/**
	 * set the dev
	 */
	void setEmulationDevice(EmulationDevice* _dev);

	/** Called by the I/O manager to register an interface/protocol with this device.
	 * Once the device is setup it should call back to the EmulationProtocol with itself as
	 * the emulation device.
	 */
	virtual void registerEmulationProtocol(EmulationProtocol* emu_proto);

	/**
	 *
	 */
	virtual void registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto);

	/** called by EmulationProtocols to send the the community to export messages */

	/** called by Emulation Protocols to export messages */
	virtual void exportPacket(Interface* iface, Packet* pkt);

	/**
	 * called by the i/o manager to handle proxied emulation evts
	 */
	virtual void handleProxiedEvent(EmulationEvent* evt);

	/**
	 * returns whether this emulation device is ready
	 */
	virtual bool isActive();

	/**
	 * If this type of protocol requires that only one instance exist on a host
	 * then only _1_ I/O manager can create one of these devices. All other
	 * I/O managers must proxy access to the owning I/OManager.
	 * then
	 */
	virtual bool requiresSingleInstancePerHost();

	/**
	 * Get the type of emulation device
	 */
	virtual int getDeviceType();

	bool isEmulationDeviceProxy() { return true; }

protected:
	EmulationDevice* dev;
	EmulationDevice::EmulationProtocolMap protomap;
};


class BlockingEmulationDevice : public prime::ssf::ssf_procedure_container , public EmulationDevice {
public:
	/**
	 * the constructor
	 */
	BlockingEmulationDevice();

	/**
	 * the deconstructor
	 */
	virtual ~BlockingEmulationDevice();

	/** Initialize the device.
	 * derived class MUST implement this
	 */
	virtual void init();

	/**
	 * close the emulation device
	 */
	virtual void wrapup();

	/** Called by the I/O manager to register an interface/protocol with this device.
	 * Once the device is setup it should call back to the EmulationProtocol with itself as
	 * the emulation device.
	 */
	virtual void registerEmulationProtocol(EmulationProtocol* emu_proto) = 0;

	/**
	 *
	 */
	virtual void registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto)=0;

	/**
	 * called by EmulationProtocols to export the pkt
	 *
	 * Should create the emulation evt and write it to the output_channel
	 * */
	virtual void exportPacket(Interface* iface, Packet* pkt)=0;

	/**
	 * called by the i/o manager to handle proxied emulation evts
	 */
	virtual void handleProxiedEvent(EmulationEvent* evt)=0;

	/**
	 * returns whether this emulation device is ready
	 */
	virtual bool isActive()=0;

	/**
	 * If this type of protocol requires that only one instance exist on a host
	 * then only _1_ I/O manager can create one of these devices. All other
	 * I/O managers must proxy access to the owning I/OManager.
	 * then
	 */
	virtual bool requiresSingleInstancePerHost()=0;

	/**
	 * Get the type of emulation device
	 */
	virtual int getDeviceType()=0;

	/**
	 * return the emulation protocol that corresponds to the ip
	 */
	virtual EmulationProtocol* ip2EmulationProtocol(IPAddress& ip)=0;

	/**
	 * Called by read to implement the reader thread
	 */
	virtual void reader_thread(){};

	/**
	 * Called by write to implement the writer thread
	 */
	virtual void writer_thread(){};

	bool isBlockingEmulationDevice() { return true; }

	/**
	 * return the input channel
	 */
	prime::ssf::inChannel* getInputChannel();

	/**
	 * return the output channel
	 */
	prime::ssf::outChannel* getOutputChannel();

	/** This is the procedure (inside the simulator) for getting new packets from the blocking device **/
	void arrival_process(prime::ssf::Process* p); //! SSF PROCEDURE SIMPLE

	/** This is the reader thread for receiving packets from the
	 * blocking device and importing them to the simulator.
	 *
	 * */
	static void read(prime::ssf::inChannel* ic, void* context);

	/** This is the writer thread for handling exported simulation
	 * packets and sending them to the blocking device. */
	static void write(prime::ssf::outChannel* oc, void* context);

protected:
	/** whether the threads should stop **/
	bool stop;

	/** The special input channel used to import external events. */
	prime::ssf::inChannel* input_channel;

	/** The special output channel used to export simulation events. */
	prime::ssf::outChannel* output_channel;
};

class PollingEmulationDevice : public EmulationDevice {
public:
	/**
	 * the constructor
	 */
	PollingEmulationDevice();

	/**
	 * the deconstructor
	 */
	virtual ~PollingEmulationDevice();

	/** Initialize the device.
	 * derived class MUST implement this
	 */
	virtual void init();

	/**
	 * close the emulation device
	 */
	virtual void wrapup();

	/** Called by the I/O manager to register an interface/protocol with this device.
	 * Once the device is setup it should call back to the EmulationProtocol with itself as
	 * the emulation device.
	 */
	virtual void registerEmulationProtocol(EmulationProtocol* emu_proto) = 0;

	/**
	 *
	 */
	virtual void registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto)=0;

	/**
	 * called by EmulationProtocols to export the pkt
	 *
	 * Should create the emulation evt and write it to the output_channel
	 * */
	virtual void exportPacket(Interface* iface, Packet* pkt)=0;

	/**
	 * called by the i/o manager to handle proxied emulation evts
	 */
	virtual void handleProxiedEvent(EmulationEvent* evt)=0;

	/**
	 * returns whether this emulation device is ready
	 */
	virtual bool isActive()=0;

	/**
	 * If this type of protocol requires that only one instance exist on a host
	 * then only _1_ I/O manager can create one of these devices. All other
	 * I/O managers must proxy access to the owning I/OManager.
	 * then
	 */
	virtual bool requiresSingleInstancePerHost()=0;

	/**
	 * Get the type of emulation device
	 */
	virtual int getDeviceType()=0;

	/**
	 * return the emulation protocl that corresponds to the ip
	 */
	virtual EmulationProtocol* ip2EmulationProtocol(IPAddress& ip)=0;


	//XXX add polling based emu functions
};


class ConnectedSocket {
public:
	typedef SSFNET_VECTOR(ConnectedSocket *) List;

	ConnectedSocket(int fd_, uint32 buf_len);
	~ConnectedSocket();

	/**
	 * if date is available, try to read an emulation event from the socket
	 *
	 * when an entire packet is available write it to the channel....
	 *
	 * if more data is available then we return true
	 */
	bool read(prime::ssf::inChannel* input_channel);
	int getFD() { return fd; }
private:
	/** the socket fd */
	int fd;
	/** the current start of valid data in the buf **/
	uint32 start;
	/** the current start of valid data in the buf **/
	uint32 end;
	/** the max size of buf */
	uint32 len;
	/** the recv buf */
	byte* buf;
};


class EmulationByPassServer: public prime::ssf::ssf_procedure_container {
public:
	typedef SSFNET_VECTOR(int) SocketList;
	EmulationByPassServer(Community* community_, int port_);
	virtual ~EmulationByPassServer();

	/** setup the listening socket **/
	void init();

	/** stop the thread */
	void wrapup(){ stop = true; };

	/** This is the procedure (inside the simulator) for getting new packets from this device **/
	void arrival_process(prime::ssf::Process* p); //! SSF PROCEDURE SIMPLE

	/** get the owning community */
	Community* getCommunity() { return community; }
	/**
	 * This is the thread for receiving bypass packets from other
	 * communities
	 **/
	static void run(prime::ssf::inChannel* ic, void* context);

private:
	/** my owning community */
	Community* community;

	/** whether the threads should stop **/
	bool stop;

	/** the port to listen on **/
	int port;

	/* the listening socket - aysnc */
	int listen_fd;

	/* max socket fd */
	int max_fd;

	/** list of sockets which we waiting for packets on */
	ConnectedSocket::List sockets; //when we first connect we need to read the com id

	/** The special input channel used to import external events. */
	prime::ssf::inChannel* input_channel;
};


} //namespace ssfnet
} //namespace prime

#endif /*__EMUDEVICE_H__*/
#endif /*SSFNET_EMULATION*/
