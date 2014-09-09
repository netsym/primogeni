/**
 * \file vpn_device.h
 * \brief This is an implementation of an emulation device for VPN
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

#ifndef __EMUVPNDEVICE_H__
#define	 __EMUVPNDEVICE_H__

#include "os/emu/emulation_device.h"

namespace prime {
namespace ssfnet {

/**
 * XXX
 */
class OpenVPNDevice : public BlockingEmulationDevice {
public:
	enum ThreadType {
		READER=0,
		WRITER=1
	};
	/**
	 * the constructor
	 */
	OpenVPNDevice();

	/**
	 * the deconstructor
	 */
	virtual ~OpenVPNDevice();

	/** Initialize the device.
	 * derived classes must implement this
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
	virtual void registerEmulationProtocol(EmulationProtocol* emu_proto);

	/**
	 *
	 */
	virtual void registerProxiedEmulationProtocol(EmulationDeviceProxy* dev, EmulationProtocol* emu_proto);

	/** called by EmulationProtocols to export messages */
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

	/**
	 * return the emulation protocl that corresponds to the ip
	 */
	virtual EmulationProtocol* ip2EmulationProtocol(IPAddress& ip);

	/**
	 * Called by read to implement the reader thread
	 */
	virtual void reader_thread();

	/**
	 * Called by write to implement the writer thread
	 */
	virtual void writer_thread();

	/**
	 * XXX hack for geni demo
	 */
	static void addGateway(SSFNET_STRING& server, int port);

protected:
	  /** Send a list of ip addresses of all host interfaces that accept
	      external packets to the simulation gateway connected by the
	      given socket. Returning non-zero means error. */
	  int send_ip_addresses(int sock);

	  /** Send a message of given size out from the socket. */
	  static int safe_send(int sock, char* buf, unsigned int bufsiz);

	  /** Receive a message of given size from the socket. */
	  static int safe_receive(int sock, char* buf, unsigned int bufsiz);

	class OpenVpnServer {
	public:
		OpenVpnServer(SSFNET_STRING _host, int _port): host(_host),port(_port),in(-1),out(-1){}
		SSFNET_STRING host;
		int port,in,out;
	};
	class EmuProtoVpnServerPair {
	public:
		EmuProtoVpnServerPair(EmulationProtocol* _proto): proto(_proto),server(0){}
		EmuProtoVpnServerPair(const EmuProtoVpnServerPair& o): proto(o.proto),server(o.server){}
		EmulationProtocol* proto;
		OpenVpnServer* server;
	};
	typedef SSFNET_VECTOR(OpenVpnServer*) OpenVpnServerVector;
	typedef SSFNET_MAP(uint32_t, EmuProtoVpnServerPair*) EmuProtoVpnServerPairMap;

	OpenVpnServerVector gateways;
	EmuProtoVpnServerPairMap ip2emuproto;

	char* recv_buf;
	char* send_buf;
	int recv_buf_size;
	int send_buf_size;

	//XXX start hack for geni demo
	typedef SSFNET_PAIR(SSFNET_STRING,int) ServerPortPair;
	typedef SSFNET_VECTOR(ServerPortPair) ServerPortPairList;
	static ServerPortPairList* static_gateways;
	//XXX end hack for geni demo


};

} //namespace ssfnet
} //namespace prime
#endif /*__EMUVPNDEVICE_H__*/
#endif /*SSFNET_EMULATION*/
