/**
 * \file application_session.cc
 * \brief Source file for the ApplicationSession class.
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

#include "proto/application_session.h"
#include "os/logger.h"
#include "os/community.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(ApplicationSession);

/**
 * Used to preform async lookups
 *
 */
class ApplicationNameResolutionCallBack: public NameResolutionCallBackWrapper {
public:
	ApplicationNameResolutionCallBack(ApplicationSession* _session, StartTrafficEvent* _evt)
		: session(_session), evt(_evt) {
	}

	virtual ~ApplicationNameResolutionCallBack() {
	}

	/**
	 * called when a mac or ip is searched for
	 */
	virtual void call_back(UID_t uid) {
		LOG_ERROR("ApplicationNameResolutionCallBack::call_back(UID_t uid) should never be called!"<<endl)
	}

	/**
	 * called when a uid is searched for
	 */
	virtual void call_back(IPAddress ip, MACAddress mac) {
		LOG_DEBUG("[async]found ip/mac "<<ip<<","<<mac<<endl)
		session->startTraffic(evt, ip, mac);
	}

	/*
	 * called when the uid/mac/ip can't be found
	 */
	virtual void invalid_query() {
		LOG_WARN("The UID "<<evt->getDstUID()<<" does not map to a valid IP!"<<endl);
		evt->free();
	}

private:
	ApplicationSession* session;
	StartTrafficEvent* evt;
};

ApplicationSession::ApplicationSession() {
}


ApplicationSession::~ApplicationSession() {

}

void ApplicationSession::startTraffic(StartTrafficEvent* evt) {
	UID_t dst_uid=evt->getDstUID();
	IPAddress ip;
	if(dst_uid!=0){
		inHost()->getCommunity()->synchronousNameResolution(dst_uid, ip);
		if(IPAddress::IPADDR_INVALID == ip) {
			//do async lookup
			inHost()->getCommunity()->asynchronousNameResolution(dst_uid, new ApplicationNameResolutionCallBack(this, evt));
		}
		else {
			LOG_DEBUG("[sync]found ip/mac "<<ip<<endl)
			this->startTraffic(evt, ip, MACAddress::MACADDR_INVALID);
		}
	}else{
		uint32_t ipaddr = evt->getDstIP();
		if(ipaddr==0){
			LOG_ERROR("Both dst uid and dst ip have not been set!"<<endl)
		}
		ip=IPAddress(ipaddr);
		this->startTraffic(evt, ip, MACAddress::MACADDR_INVALID);
	}
}

void ApplicationSession::startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac) {
	LOG_ERROR("An application did not override startTraffic(StartTrafficEvent* evt, IPAddress ipaddr, MACAddress mac)"<<endl)
}

ApplicationSession::PortAppMap* ApplicationSession::port_map=0;
RegisteredApp::AppList* ApplicationSession::apps_to_reg=0;

int ApplicationSession::registerApplicationServer(int port, ConfigType* (*getClassConfigType)()) {
	if(!apps_to_reg) {
		port_map= new PortAppMap();
		apps_to_reg=new RegisteredApp::AppList();
	}
	apps_to_reg->push_back(RegisteredApp(port,getClassConfigType));
	return port;
}

void ApplicationSession::init_applications() {
	if(apps_to_reg) {
		for(RegisteredApp::AppList::iterator i = apps_to_reg->begin();i!= apps_to_reg->end();i++) {
			ConfigType* ct = (*i).getClassConfigType();
			if(!ct) LOG_ERROR("Should not happen!"<<endl);
			PortAppMap::iterator j = port_map->find((*i).port);
			if (j != port_map->end()) {
				LOG_ERROR("The port number "<<(*i).port<<" is already mapped to "<<j->second->getName()<<" but you tried to map "<<ct->getName()<<" to it!"<<endl)
			}
			port_map->insert(SSFNET_MAKE_PAIR((*i).port,ct));
			LOG_INFO("registered app "<<ct->getName()<<"["<<ct->type_id<<"] on port "<<(*i).port<<endl);
		}
	}
	else {
		LOG_ERROR("No apps!"<<endl);
	}
}

int ApplicationSession::getApplicationProtocolType(int port) {
	if(!port_map) {
                LOG_ERROR("The port map was not created!"<<endl);
                //port_map= new PortAppMap();
                //return 0;
        }
        PortAppMap::iterator i = port_map->find(port);
        if (i == port_map->end()) {
                LOG_WARN("Unknown port "<<port<<endl);
                return -1;
        }
        //LOG_WARN("found port "<<port<<" to run app "<<i->second->getName()<<"["<<i->second->type_id<<"]"<<port<<endl);
        return convert_typeid_to_protonum(i->second->type_id);
}

} // namespace ssfnet
} // namespace prime
