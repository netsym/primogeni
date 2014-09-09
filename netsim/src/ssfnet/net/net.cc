/**
 * \file net.cc
 * \brief Source file for the Net class.
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

#include "os/logger.h"
#include "os/community.h"
#include "net/net.h"
#include <math.h>
#include "os/partition.h"
namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(Net);


Net::Net() {
}

Net::~Net() {
	ChildIterator<Net*> n = subnets();
	while(n.hasMoreElements())
		delete n.nextElement();

	ChildIterator<Host*> h = hosts();
	while(h.hasMoreElements())
		delete h.nextElement();

	ChildIterator<Link*> l = links();
	while(l.hasMoreElements())
		delete l.nextElement();

	ChildIterator<RoutingSphere*> r = rsphere();
	while(r.hasMoreElements())
		delete r.nextElement();

	ChildIterator<Monitor*> m = monitors();
	while(m.hasMoreElements())
		delete m.nextElement();

	ChildIterator<Aggregate*> a = aggregates();
	while(a.hasMoreElements())
		delete a.nextElement();


	//the traffic manager will delete the traffics
}

void Net::initStateMap() {
	BaseEntity::initStateMap();
}

void Net::init() {
	LOG_INFO("Net::init(), "<<getUName()<<endl);
	ChildIterator<Net*> n = subnets();
	while(n.hasMoreElements()) {
		//n.nextElement()->init();
		Net* hh = n.nextElement();
		LOG_INFO("Net::init() initing "<<hh->getUName()<<endl);
		hh->init();
	}

	ChildIterator<Host*> h = hosts();
	while(h.hasMoreElements()) {
		//h.nextElement()->init();
		Host* hh = h.nextElement();
		LOG_INFO("Net::init() initing "<<hh->getUName()<<endl);
		hh->init();
	}

	ChildIterator<Link*> l = links();
	while(l.hasMoreElements()) {
		//l.nextElement()->init();
		Link* hh = l.nextElement();
		LOG_INFO("Net::init() initing "<<hh->getUName()<<endl);
		hh->init();
	}

	ChildIterator<RoutingSphere*> r = rsphere();
	while(r.hasMoreElements()) {
		//r.nextElement()->init();
		RoutingSphere* hh = r.nextElement();
		LOG_INFO("Net::init() initing "<<hh->getUName()<<endl);
		hh->init();
	}
	//aggs must be inited BEFORE monitors!
	ChildIterator<Aggregate*> a = aggregates();
	while(a.hasMoreElements()) {
		//r.nextElement()->init();
		Aggregate* hh = a.nextElement();
		LOG_INFO("Net::init() initing "<<hh->getUName()<<endl);
		hh->init();
	}

	ChildIterator<Monitor*> m = monitors();
	while(m.hasMoreElements()) {
		//r.nextElement()->init();
		Monitor* hh = m.nextElement();
		LOG_INFO("Net::init() initing "<<hh->getUName()<<endl);
		hh->init();
	}
	
	//the traffic manager will init the traffics
}

void Net::wrapup()
{
	ChildIterator<Net*> n = subnets();
	while(n.hasMoreElements())
		n.nextElement()->wrapup();

	ChildIterator<Link*> l = links();
	while(l.hasMoreElements())
		l.nextElement()->wrapup();

	ChildIterator<Host*> h = hosts();
	while(h.hasMoreElements())
		h.nextElement()->wrapup();

	ChildIterator<RoutingSphere*> r = rsphere();
	while(r.hasMoreElements())
		r.nextElement()->wrapup();

	ChildIterator<Monitor*> m = monitors();
	while(m.hasMoreElements())
		m.nextElement()->wrapup();

	ChildIterator<Aggregate*> a = aggregates();
	while(a.hasMoreElements())
		a.nextElement()->wrapup();


	//the traffic manager will wrapup the traffics
}

const IPPrefix& Net::getIPPrefix() {
	return unshared.ip_prefix.read();
}

void Net::exportVizState(StateLogger* state_logger, double sampleInterval){
	ChildIterator<Aggregate*> aa = aggregates();
	while(aa.hasMoreElements()) {
		Aggregate* a = aa.nextElement();
		//std::cout<<"\tstaart export viz state for "<<a->getUName()<<", uid="<<a->getUID()<<", logger="<<((void*)state_logger)<<endl;
		a->exportVizState(state_logger, sampleInterval);
		//std::cout<<"\tdone viz state for "<<a->getUName()<<", uid="<<a->getUID()<<", logger="<<((void*)state_logger)<<endl;
	}
}

void Net::exportState(StateLogger* state_logger, double sampleInterval){
	ChildIterator<Aggregate*> aa = aggregates();
	while(aa.hasMoreElements()) {
		Aggregate* a = aa.nextElement();
		if(!a->getConfigType()->isSubtype(VizAggregate::getClassConfigType())) {
			a->exportState(state_logger, sampleInterval);
		}
	}
}


CNFContentOwnerMap* Net::getCNFContentOwnerMap() {
	return &(unshared.cnf_content_ids.read());
}

uint8_t Net::getIPPrefixLen() {
	return shared.ip_prefix_len.read();
}

Net* Net::getSuperNet() { return SSFNET_STATIC_CAST(Net*,getParent()); }

ChildIterator<Monitor*> Net::getMonitors(int* nmonitors)
{
	if(nmonitors) *nmonitors = monitorsSize();
	return monitors();
}

ChildIterator<Aggregate*> Net::getAggregates(int* naggregate)
{
	if(naggregate) *naggregate = aggregatesSize();
	return aggregates();
}

ChildIterator<Host*> Net::getHosts(int* nhosts)
{
	if(nhosts) *nhosts = hostsSize();
	return hosts();
}

ChildIterator<Net*> Net::getNets(int* nnets)
{
	if(nnets) *nnets = subnetsSize();
	return subnets();
}

ChildIterator<Link*> Net::getLinks(int* nlinks)
{
	if(nlinks) *nlinks = linksSize();
	return links();
}

IPPrefixRoute::List& Net::getExternalNetworkRoutes() {
	return shared.portal_rules.read();
}

RoutingSphere* Net::getRoutingSphere()
{
	switch(rsphereSize()) {
	case 0: return NULL;
	case 1: return rsphere().nextElement();
	default:
		LOG_ERROR("The net "<<getUName()<<" has "<<rsphereSize()<<" routing spheres!"<<endl);
	}
	return NULL;
}

UID_t Net::getControllerRid(){
	return shared.controller_rid.read();
}

SubEdgeList& Net::getSubEdgeInterfaces(){
	return shared.sub_edge_ifaces.read();
}

UID_t Net::getUID() {
	return BaseEntity::getUID();
}

UID_t Net::getMinRank(BaseEntity* anchor) {
	return BaseEntity::getMinRank(anchor);
}

UID_t Net::getMinUID() {
	return BaseEntity::getMinRank(NULL);
}

bool Net::isRoutingSphere() {
	if(getRoutingSphere()) {
		//LOG_DEBUG(getUName()<<"["<<getTypeName()<<"] --> "<<getRoutingSphere()->hasRoutes()<<endl);
		return getRoutingSphere()->hasRoutes();
	}
	return false;
}

ChildIterator<Traffic*> Net::getTraffics(int* ntraffic)
{
	if(ntraffic) *ntraffic=trafficsSize();
	return traffics();
}

RoutingSphere* Net::findOwningSphere(UID_t id) {
	if(!this->containsUID(id)) return NULL;
	ChildIterator<Net*> n = subnets();
	Net* sub=NULL;
	RoutingSphere * rv=NULL;
	while(n.hasMoreElements() && !rv) {
		sub=n.nextElement();
		if(sub->containsUID(id)) {
			if(sub->isRoutingSphere()) {
				return sub->getRoutingSphere();
			}
			rv = sub->findOwningSphere(id);
		}
	}
	if(!rv && this->isRoutingSphere()) rv=this->getRoutingSphere();
	return rv;
}


} // namespace ssfnet
} // namespace prime
