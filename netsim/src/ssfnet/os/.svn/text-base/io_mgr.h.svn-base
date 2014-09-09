/**
 * \file io_mgr.h
 * \brief Header file for the IOManager class.
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

#ifndef __IO_MGR_H__
#define __IO_MGR_H__

#include "ssf.h"
#include "os/ssfnet_types.h"
#include "os/virtual_time.h"
#include "os/community.h"
#include "os/ssfnet.h"
#include "net/link.h"

namespace prime {
namespace ssfnet {

class Interface;
class EmulationDevice;

/**
 * \brief Container for communication with external entities
 *
 * This class provides communication support between communities and emulation devices.
 * There is one such object for each community.
 */

class IOManager: public prime::ssf::Process {
	friend class Community;
public:
	typedef SSFNET_MAP(int, LinkInfo::RemoteIface::List*) ComId2RemoteIfaceList;
	typedef SSFNET_PAIR(prime::ssf::outChannel*,VirtualTime) ChannelDelayPair;
	typedef SSFNET_MAP(int, ChannelDelayPair) ComId2ChannelDelayPairMap;
#ifdef SSFNET_EMULATION
	typedef SSFNET_MAP(int, EmulationDevice*) EmuDevMap;
#else
	typedef void* EmuDevMap;
#endif

	/** The constructor. */
	IOManager(Community* p);

	/** The destructor. */
	virtual ~IOManager();

	/**
	 * The function that will be invoked when an event arrives at the
	 * input channel. Here it distributes the packets to the interfaces
	 * that should receive the packet in this alignment.
	 */
	virtual void action(); //! SSF PROCEDURE SIMPLE

protected:

	/**
	 * Called by its owning community to initialize any emulation devices it may own
	 */
	void setup_devices();

	/**
	 * Called by its owning community to wrapup any emulation devices it may own
	 */
	void wrapup();

	/**
	 * When a host/router is registered with a community it in turn
	 * registers the interfaces of the host/router with its
	 * associated IOManager using this function
	 */
	void addInterface(BaseInterface* iface);

	/**
	 * When an interface is registered it will create a link info in the link
	 * it is attached to for this community (if one does not exist).
	 */
	LinkInfo* createLinkInfo(Link* link);

	/**
	 * called by the community to setup the channels to the remote communities
	 */
	void createChannels();

	/**
	 * register the emulation device
	 */
#ifdef SSFNET_EMULATION
	void registerEmulationDevice(EmulationDevice* dev);
#else
	void registerEmulationDevice(void* dev);
#endif

	/** send the evt to the community with id=communityId **/
	void writeEvent(SSFNetEvent* evt, VirtualTime delay);

private:
	/*
	 * a map from the remote community ids to the a pair of out channels to that community.
	 * the first of the pair is the pkt channel and the second is the ctl channel
	 */
	ComId2ChannelDelayPairMap out_channels;

	/**
	 * A list of all of the emulation devices
	 */
	EmuDevMap emu_devs;

	/** The input channel to receive remote packets. */
	prime::ssf::inChannel* ic;

	/** a list of remote ifaces. createChannels() uses this to construct the channels. */
	ComId2RemoteIfaceList channel_info;

	/** a cache of the owner with the correct type.... */
	Community* mycomm;

	/** a cache of the partition ptr */
	Partition* mypart;
};

}// namespace ssfnet
}// namespace prime

#endif /*__IO_MGR_H__*/
