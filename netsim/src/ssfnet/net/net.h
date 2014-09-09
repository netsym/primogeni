/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

/**
 * \file net.h
 * \brief Header file for the Net class.
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

#ifndef __NET_H__
#define __NET_H__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"
#include "os/routing.h"
#include "proto/cnf/cnf_session.h"
#include "os/traffic.h"
#include "net/host.h"
#include "net/link.h"
#include "os/placeholder.h"
namespace prime {
namespace ssfnet {

/**
 * \brief A network.
 *
 * The Net class is the container for hosts (routers), links, and
 * other sub-networks. Also, traffic and routing strategy can be
 * specified for a network.
 */
class Net: public ConfigurableEntity<Net, BaseEntity> {
public:
	friend class Partition;

	
public:
	SSFNET_PROPMAP_DECL(
		SSFNET_CONFIG_PROPERTY_DECL(int8_t, ip_prefix_len)
		SSFNET_CONFIG_PROPERTY_DECL(IPPrefixRoute::List, portal_rules)
		SSFNET_CONFIG_PROPERTY_DECL(UID_t, controller_rid)
		SSFNET_CONFIG_PROPERTY_DECL(SubEdgeList, sub_edge_ifaces)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(Net, subnets)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(Host, hosts)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(Link, links)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(PlaceHolder, placeholders)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(RoutingSphere, rsphere)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(Monitor, monitors)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(Aggregate, aggregates)
		SSFNET_CONFIG_CHILDREN_DECL_SHARED(Traffic, traffics)
	)
	SSFNET_STATEMAP_DECL(
		SSFNET_CONFIG_STATE_DECL(IPPrefix, ip_prefix)
		SSFNET_CONFIG_STATE_DECL(CNFContentOwnerMap, cnf_content_ids)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,Net,subnets)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,Host,hosts)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,Link,links)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,PlaceHolder,placeholders)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,RoutingSphere,rsphere)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,Monitor,monitors)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,Aggregate,aggregates)
		SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(Net,Traffic,traffics)
	)
	SSFNET_CONFIG_CHILDREN_DECL(Net, subnets)
	SSFNET_CONFIG_CHILDREN_DECL(Host, hosts)
	SSFNET_CONFIG_CHILDREN_DECL(Link, links)
	SSFNET_CONFIG_CHILDREN_DECL(PlaceHolder, placeholders)
	SSFNET_CONFIG_CHILDREN_DECL(RoutingSphere, rsphere)
	SSFNET_CONFIG_CHILDREN_DECL(Monitor, monitors)
	SSFNET_CONFIG_CHILDREN_DECL(Aggregate, aggregates)
	SSFNET_CONFIG_CHILDREN_DECL(Traffic, traffics)
	SSFNET_ENTITY_SETUP(&(unshared.subnets), &(unshared.hosts), &(unshared.links), &(unshared.placeholders), &(unshared.rsphere), &(unshared.monitors), &(unshared.aggregates), &(unshared.traffics) )
;

public:
	/** The constructor. */
	Net();

	/** The destructor. */
	virtual ~Net();

	/**
	 * The init method is used to initialize the network once it has
	 * been created and configured by model builder. The init method
	 * will initialize all sub-networks, hosts, and links therein.
	 */
	virtual void init();

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * used to collect statistical information of the simulation run.
	 * The wrapup method will call the wrapup method of all
	 * sub-networks, hosts, and links of this network.
	 */
	virtual void wrapup();

	/**
	 * Gets the uid of the net.
	 */
	UID_t getUID();

	/**
	 * Gets the rank of the left most child child in this subtree in reference to anchor
	 */
	virtual UID_t getMinRank(BaseEntity* anchor);

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	virtual void exportVizState(StateLogger* state_logger, double sampleInterval);
	virtual void exportState(StateLogger* state_logger, double sampleInterval);

	/**
	 * Gets the uid of the left most child child in this subtree
	 */
	UID_t getMinUID();

	/** Return the IP address length. (the n in W.X.Y.Z/n) */
	uint8_t getIPPrefixLen();

	/** Get the IP prefix of this network. */
	const IPPrefix& getIPPrefix();

	/** Get the CNF content owner map of this network. */
	CNFContentOwnerMap* getCNFContentOwnerMap();

	/** Return the parent network, or NULL if this is top-level
	 network. */
	Net* getSuperNet();

	/** Return all subnets defined in this network level. */
	ChildIterator<Net*> getNets(int* nnets = 0);

	/** Return all hosts defined in this network level.*/
	ChildIterator<Host*> getHosts(int* nhosts = 0);

	/** Return all links defined in this network level. */
	ChildIterator<Link*> getLinks(int* nlinks = 0);

	/** Return the routing sphere for this network if defined. */
	RoutingSphere* getRoutingSphere();

	/** Return all monitors defined in this network level.*/
	ChildIterator<Monitor*> getMonitors(int* nmonitors);

	/** Return all aggregates defined in this network level.*/
	ChildIterator<Aggregate*> getAggregates(int* naggregate);

	/**
	* Get the controller rid of this network.
	*/
	UID_t getControllerRid();

	/**
	 * Get the pairs of edge interface rank and owning host rank of sub routing spheres
	 */
	SubEdgeList& getSubEdgeInterfaces();

	/** Return all traffics defined in this network level. */
	ChildIterator<Traffic*> getTraffics(int* ntraffic = 0);

	/**
	 *  returns true if this network contains a routingsphere
	 */
	virtual bool isRoutingSphere();

	/**
	 * \breif find the sphere which owns the node with UID id. If the node is not owned by
	 * a sub-sphere, or no sub-spheres exists, this network routing sphere is considered the
	 * owning sphere. If this network is not a routing sphere, NULL is returned..
	 */
	RoutingSphere* findOwningSphere(UID_t id);

	IPPrefixRoute::List& getExternalNetworkRoutes();

};

} // namespace ssfnet
} // namespace prime

#endif /*__NET_H__*/
