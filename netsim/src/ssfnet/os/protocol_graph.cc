/**
 * \file protocol_graph.cc
 * \brief Source file for the ProtocolGraph class.
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

#include <stdlib.h>
#include <string.h>
#include "os/logger.h"
#include "os/config_factory.h"
#include "os/protocol_graph.h"
#include "os/protocol_session.h"
#include "os/partition.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(ProtocolGraph)

		void ProtocolGraph::initStateMap() {
	BaseEntity::initStateMap();
	unshared.initialized=false;
}

void ProtocolGraph::init()
{
	LOG_INFO("ProtocolGraph::init(), "<<getUName()<<endl);
	DEBUG_CODE(
	if(unshared.initialized) {
		LOG_INFO("[init >1]"<<getUName()<<", part="<<Partition::getInstance()->getPartitionId()<<"\n");
	}
	);
	// add the configured protocol sessions
	assert(!unshared.initialized); // make sure init will not be called in insert_session()
	ChildIterator<ProtocolSession*> ss = cfg_sess();
	while (ss.hasMoreElements()) {
		insert_session(ss.nextElement());
	}

	unshared.initialized = true;
	PSESS_VECTOR mylist(unshared.protocol_list); // create a local copy because protocol_list might be altered!
	for (PSESS_VECTOR::iterator iter = mylist.begin();
			iter != mylist.end(); iter++) {
		(*iter)->init();
	}
}

void ProtocolGraph::wrapup()
{
	PSESS_VECTOR mylist(unshared.protocol_list); // create a local copy because protocol_list might be altered!
	for (PSESS_VECTOR::iterator iter = mylist.begin();
			iter != mylist.end(); iter++) {
		(*iter)->wrapup();
	}
}

ProtocolSession* ProtocolGraph::sessionForName(const SSFNET_STRING& pname)
{
	PROTONAME_MAP::iterator iter = unshared.pname_map.find(pname);
	if (iter != unshared.pname_map.end()) return (*iter).second;
	else if(automaticallyCreateProtocolSessions()){
		//std::cout<< "auto adding protocol, pname="<<pname<<", uname="<<getUName()<<endl;
		ProtocolSession* sess =
				ConfigurableFactory::createInstance<ProtocolSession*>(pname);
		addChild(sess);
		insert_session(sess);
		return sess;
	}
	return 0;
}

ProtocolSession* ProtocolGraph::sessionForNumber(int pno, bool create)
{
	PROTONUM_MAP::iterator iter = unshared.pno_map.find(pno);
	if (iter != unshared.pno_map.end()) return (*iter).second;
	else if(create && automaticallyCreateProtocolSessions()){
		ProtocolSession* sess=
				ConfigurableFactory::createInstance<ProtocolSession*>(convert_protonum_to_typeid(pno));
		//std::cout<< "auto adding protocol, pno="<<pno<<"["<<convert_protonum_to_typeid(pno)<<"]["<<sess->getConfigType()->getName()<<"] uname="<<getUName()<<endl;
		addChild(sess);
		insert_session(sess);
		return sess;
	}
	return 0;
}

ProtocolGraph::ProtocolGraph() {}

ProtocolGraph::~ProtocolGraph()
{
	PSESS_VECTOR mylist(unshared.protocol_list); // create a local copy because protocol_list might be altered!
	for (PSESS_VECTOR::reverse_iterator iter = mylist.rbegin();
			iter != mylist.rend(); iter++) {
		delete (*iter);
	}
}

void ProtocolGraph::insert_session(ProtocolSession* sess)
{
	if (!unshared.pno_map.insert(SSFNET_MAKE_PAIR(sess->getProtocolNumber(), sess)).second ||
			!unshared.pname_map.insert(SSFNET_MAKE_PAIR(sess->getProtocolName(), sess)).second){
		//std::cout<<"[didnt add to protocol_list]"<<getUName()<<", insert session: " << sess->getProtocolName() << " [" << sess->getProtocolNumber() << "]" << endl;
		return;
	}
	//LOG_DEBUG(getUName()<<", insert session: " << sess->getProtocolName() << " [" << sess->getProtocolNumber() << "]" << endl);
	//std::cout<<"[added to protocol_list]"<<getUName()<<", insert session: " << sess->getProtocolName() << " [" << sess->getProtocolNumber() << "]" << endl;
	unshared.protocol_list.push_back(sess);
	if (unshared.initialized) sess->init();
}

bool ProtocolGraph::automaticallyCreateProtocolSessions() {
	return shared.automatic_protocol_session_creation.read();
}


} // namespace ssfnet
} // namespace prime
