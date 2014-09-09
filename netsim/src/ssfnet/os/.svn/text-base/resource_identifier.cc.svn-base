/**
 * \file resource_identifier.cc
 * \brief Source file for the ResourceIdentifier class.
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
#include "os/resource_identifier.h"
#include "os/logger.h"
#include "os/config_entity.h"
namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(ResourceIdentifier)

void ResourceIdentifier::evaluate(BaseEntity* anchor) {
	if(	!is_compiled and !(value.uncompiled)) {
		//it is empty!
		is_compiled=true;
		value.compiled=0;
	}
	else if(is_compiled and !(value.compiled)) {
		//it is empty!
	}
	else {
		LOG_ERROR("NOT DONE"<<endl);
	}
}

void ResourceIdentifier::init(UncompiledRID* v) {
	LOG_DEBUG("init UncompiledRID("<<v<<") resource identifier"<<endl);
	is_compiled=false;
	value.uncompiled=v;
}
void ResourceIdentifier::init(CompiledRID* v) {
	LOG_DEBUG("init CompiledRID("<<v<<")resource identifier"<<endl);
	is_compiled=true;
	value.compiled=v;
	//for(UIDVec::iterator i = v->getUIDVec().begin(); i!= v->getUIDVec().end();i++) {
	//	LOG_DEBUG("\trel id="<<*i<<endl);
	//}
}

UID_t CompiledRID::getUID(UID_t rid, BaseEntity* anchor) {
	//LOG_DEBUG("rid="<<rid<<endl);
	//LOG_DEBUG("\tanchor="<<anchor->getUName()<<"(uid="<<anchor->getUID()<<", size="<<anchor->getSize()<<")"<<endl);
	//LOG_DEBUG("\tUID="<< (rid + (anchor->getUID()-anchor->getSize())) <<endl);
	return (rid + (anchor->getUID()-anchor->getSize()));
}


template<> bool rw_config_var<SSFNET_STRING, ResourceIdentifier> (
                SSFNET_STRING& dst, ResourceIdentifier& src) {
	dst.clear();
	//XXX
	dst.append("don't support serializing ResourceIdentifiers yet!");
   return false;
}

template<> bool rw_config_var<ResourceIdentifier, SSFNET_STRING> (
		ResourceIdentifier& dst, SSFNET_STRING& src) {
	if(!src.length()) {
		dst.init((UncompiledRID*)0);
	}
	else {
		LOG_ERROR("NOT DONE, src="<<src<<endl);
	}
   return false;
}




}// namespace ssfnet
}// namespace prime

