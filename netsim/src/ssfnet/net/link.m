/**
 * \file link.h
 * \brief Header file for the Link and LinkInfo classes.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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

#ifndef __LINK_H__
#define __LINK_H__

#include "os/ssfnet_types.h"
#include "os/virtual_time.h"
#include "net/interface.h"

namespace prime {
namespace ssfnet {

class ModelBuilder;
class Net;
class Community;

/**
 * \brief Each community that has interfaces on this link will build one of these for the link.
 */
class LinkInfo {
public:
	friend class IOManager;
	typedef SSFNET_LIST(LinkInfo*) List;
	typedef SSFNET_MAP(UID_t, LinkInfo*) Map;
	class RemoteIface {
	public:
		typedef SSFNET_LIST(LinkInfo::RemoteIface*) List;
		RemoteIface(BaseInterface* _iface, int _remote_com_id, VirtualTime _delay):
		iface(_iface), remote_com_id(_remote_com_id), delay(_delay), oc(0)
		{
		}
		~RemoteIface(){}
		BaseInterface* iface;
		int remote_com_id;
		VirtualTime delay;
		prime::ssf::outChannel* oc;
	};
	LinkInfo();
	~LinkInfo();
	uint32_t getRemoteInterfaceCount();
	uint32_t getLocalInterfaceCount();
	RemoteIface** getRemoteInterfaces();
	Interface** getLocalInterfaces();
	uint* getIfaceIdxList();
	bool* getIfaceIdxListType();

private:
	/** array of Interfaces _NOT_ within this community. */
	RemoteIface** remote_ifaces;
	/** number of remote ifaces in above array **/
	uint32_t remote_iface_size;

	/** list of Interfaces within this community. */
	Interface** local_ifaces;
	/** number of local ifaces in above array **/
	uint32_t local_iface_size;

	//the idx into either remote_ifaces or local_ifaces depending on
	//wether idx_type is true(remote) or false(local).
	//this is used for looking up ifaces by index
	uint* iface_idx;
	bool* idx_type;
};

/**
 * \brief A link connects two or more network interfaces.
 */
class Link: public ConfigurableEntity<Link, BaseEntity> {
	typedef SSFNET_MAP(uint32, BaseInterface*) IP2IFACE_MAP;
	typedef SSFNET_MAP(MACAddress, BaseInterface*) MAC2IFACE_MAP;
public:
	state_configuration {
		shared configurable float delay  {
			type= FLOAT;
			default_value="0.001";
			doc_string="link delay";
			visualized=true;
		};
		shared configurable float bandwidth {
			type= FLOAT;
			default_value="1e8";
			doc_string="link bandwidth";
			visualized=true;
		};
		shared configurable int8_t ip_prefix_len {
			type= INT;
			default_value="-1";
			doc_string="The n in W.X.Y.Z/n";
		};
		configurable IPPrefix ip_prefix {
			type= OBJECT;
			default_value="0.0.0.0/0";
			doc_string="ip prefix of this Link";
		};

		/**
		 * each community has a different view of links via a linkinfo.
		 * This is a map from community id to its linkinfo of this link
		 */
		LinkInfo::Map com_id2link_info;

		/** A map from IP address to interface. */
		IP2IFACE_MAP ip2iface;

		/** A map from UID to interface. */
		MAC2IFACE_MAP mac2iface;

		child_type<BaseInterface> attachments {
			min_count = 0;
			max_count = 0; // infinity
			is_aliased = true; // by default it is false
			doc_string = "network interfaces attached to this link";
		};
	};
public:
	/** The constructor. */
	Link();

	/** The destructor. */
	virtual ~Link();

	/** The init method is used to initialize the link after the link
	 has been configured. */
	virtual void init();

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/** The wrapup method is used to collect statistics about the link
	 at the end of the simulation. */
	virtual void wrapup();

	/** Return the network containing this link. */
	Net* inNet();

	/**
	 * Return a list (an enumeration) of network interfaces attached to
	 * this link. The number of interfaces is returned if an argument (a
	 * pointer to an integer) is provided.
	 */
	ChildIterator<BaseInterface*> getAttachments(int* nattached = 0);

	/**
	 * Return the network interface attached to this link that has the
	 * given IP address. The method returns NULL if no such interface
	 * exists.
	 */
	BaseInterface* getInterfaceByIP(IPAddress ip);

	/**
	 * Return the network interface attached to this link that has the
	 * given MAC address. The method returns NULL if no such interface
	 * exists.
	 */
	BaseInterface* getInterfaceByMAC(MACAddress mac);

	/** Return the IP address length. (the n in W.X.Y.Z/n) */
	uint8_t getIPPrefixLen();

	/** Return the IP prefix **/
	IPPrefix  getIPPrefix();

	/** Return the delay of this link. */
	VirtualTime getDelay();

	/** Return the bandwidth of this link. */
	float getBandwidth();

	/*
	 * get the link info for the specified community
	 * If a link info for the community exists it is returned.
	 * NULL is returned otherwise
	 */
	LinkInfo* getLinkInfo(Community* com);

	/*
	 * register link info for the specified community
	 */
	void registerLinkInfo(Community* com, LinkInfo* link_info);

	/**
	 * Returns the peer interface of src_iface as next_hop_iface if the peer interface is local,
	 * otherwise returns the peer interface as remote_idx.
	 */
	int16_t getNextHop(BaseInterface* src_iface,
					Packet* pkt,
					Community* src_com,
					BaseInterface*& next_hop_iface,
					int& remote_idx,
					int16_t _bus_idx=-1);
public:
	virtual long getMemUsage_object_properties();
	virtual long getMemUsage_object_states();
	virtual long getMemUsage_class();
};

} // namespace ssfnet
} // namespace prime

#endif /*__LINK_H__*/
