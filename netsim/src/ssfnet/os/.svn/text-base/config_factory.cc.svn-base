/**
 * \file config_factory.cc
 * \brief Source file for the ConfigurableFactory class.
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

#include "os/ssfnet_types.h"
#include "os/config_type.h"
#include "os/config_factory.h"
#include "os/logger.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(ConfigurableFactory)

ConfigType::StrMap* ConfigurableFactory::str_types = NULL;
ConfigType::IntMap* ConfigurableFactory::int_types = NULL;
ConfigType* ConfigurableFactory::alias_ctype = NULL;

ConfigType::IntMap* ConfigurableFactory::int_types_jprime = NULL;

bool ConfigurableFactory::setup = false;

void ConfigurableFactory::setupConfigTypes() {
	if (setup)
		return;
	setup = true;
	if (NULL == str_types)
		//nothing to do...
		return;
	ConfigType::StrMap::iterator it;
	for (it = str_types->begin(); it != str_types->end(); it++) {
		if (it->first.compare(it->second->getName()) == 0
				&& !it->second->getSuperName().empty()) {
			getConfigType(it->second->getSuperName())->registerDerivedType(
					it->second);
		}
	}
}

ConfigType* ConfigurableFactory::registerConfigType(ConfigType* type,
		const char* alias1, const char* alias2) {
	if (NULL == str_types) {
		str_types = new ConfigType::StrMap();
		int_types = new ConfigType::IntMap();
		int_types_jprime = new ConfigType::IntMap();
	}
	if (!str_types->insert(SSFNET_MAKE_PAIR(type->getName(),type)).second)
		LOG_ERROR("'"<<type->getName()<<"' is a duplicate ConfigType!"<<endl)
	if (alias1 != NULL) {
		if (!str_types->insert(SSFNET_MAKE_PAIR(alias1,type)).second)
			LOG_ERROR("'"<<type->getName()<<"' is a duplicate ConfigType!"<<endl)
	}
	if (alias2 != NULL) {
		if (!str_types->insert(SSFNET_MAKE_PAIR(alias2,type)).second)
			LOG_ERROR("'"<<type->getName()<<"' is a duplicate ConfigType!"<<endl)
	}

	if (!int_types->insert(SSFNET_MAKE_PAIR(type->type_id,type)).second)
		LOG_ERROR("'"<<type->getName()<<"' had a duplicate type_id("<<type->type_id<<"'!"<<endl);
	if (alias2 != NULL) {
		//LOG_INFO("Assigned "<<type->getName()
		//		<<"("<<SSFNET_STRING(alias1)<<","<<SSFNET_STRING(alias2)<<")"<<" type_id "<<type->type_id<<endl)
	}
	if (alias1 != NULL) {
		//LOG_INFO("Assigned "<<type->getName()<<"("<<SSFNET_STRING(alias1)<<")"<<" type_id "<<type->type_id<<endl)
	}
	/*std::cout<<"Assigned "<<type->getName()<<" type_id "<<type->type_id<<std::endl<<
			"\tjprime_alias_id:"<<type->jprime_alias_id<<std::endl<<
			"\tjprime_alias_replica_id:"<<type->jprime_alias_replica_id<<std::endl<<
			"\tjprime_impl_id:"<<type->jprime_impl_id<<std::endl<<
			"\tjprime_replica_id:"<<type->jprime_replica_id<<std::endl;*/

	int_types_jprime->insert(SSFNET_MAKE_PAIR(type->jprime_alias_id,type));
	int_types_jprime->insert(SSFNET_MAKE_PAIR(type->jprime_alias_replica_id,type));
	int_types_jprime->insert(SSFNET_MAKE_PAIR(type->jprime_impl_id,type));
	int_types_jprime->insert(SSFNET_MAKE_PAIR(type->jprime_replica_id,type));

	if(alias_ctype==NULL) {
		if(!type->name.compare("Alias")) {
			alias_ctype=type;
		}
	}
	return type;
}

ConfigType* ConfigurableFactory::getConfigType(const char* name) {
	SSFNET_STRING s(name);
	return ConfigurableFactory::getConfigType(s);
}

ConfigType* ConfigurableFactory::getConfigType(const SSFNET_STRING& name) {
	if (NULL == str_types)
		LOG_ERROR("Uknown ConfigType "<<name<<endl);
	ConfigType::StrMap::iterator rv = str_types->find(name);
	if (rv == str_types->end())
		LOG_ERROR("Uknown ConfigType "<<name<<endl);
	return rv->second;
}

ConfigType* ConfigurableFactory::getConfigType(int typeId) {
	if (NULL == int_types)
		LOG_ERROR("int_types was not setup!"<<endl);
	//LOG_DEBUG("ConfigurableFactory::getConfigType("<<typeId<<")"<<endl)
	ConfigType::IntMap::iterator rv = int_types->find(typeId);
	if (rv == int_types->end())
		LOG_ERROR("Uknown ConfigType "<<typeId<<endl);
	return rv->second;
}

ConfigType* ConfigurableFactory::getConfigType_jprime(int typeId) {
	if (NULL == int_types_jprime)
		LOG_ERROR("Uknown ConfigType "<<typeId<<endl);
	ConfigType::IntMap::iterator rv = int_types_jprime->find(typeId);
	if (rv == int_types_jprime->end())
		LOG_ERROR("Uknown ConfigType "<<typeId<<endl);

	if(typeId == rv->second->jprime_alias_id ||
		typeId == rv->second->jprime_alias_replica_id) {
		//its an alias....
		//LOG_DEBUG("Created an alias!"<<endl);
		return alias_ctype;
	}
	return rv->second;
}


ConfigurableFactory::ConfigurableFactory(void) {
}

ConfigurableFactory::~ConfigurableFactory(void) {
}

void ConfigurableFactory::generateSchema(PRIME_OSTREAM& schema) {
	if (NULL == str_types)
		LOG_ERROR("There are no types from which to build a schema!"<<endl);
	SSFNET_SET(SSFNET_STRING) done;
	ConfigType::List todo;
	ConfigType::List to_write;
	ConfigurableFactory::setupConfigTypes();
	todo.push_back(BaseEntity::getClassConfigType());

	while (todo.size() > 0) {
		ConfigType* t = todo.front();
		todo.pop_front();
		if (done.count(t->getName()) > 0) {
			continue;
		} else if (!t->isBase(false)) {
			to_write.push_back(t);
			done.insert(t->getName());
		}
		for (ConfigType::IntMap::iterator i = t->getDerivedTypes().begin(); i
				!= t->getDerivedTypes().end(); i++) {
			todo.push_back(i->second);
		}
	}

	schema << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" << endl;
	schema << "<PrimeModelSchema name='default schema'>" << endl;
	for (ConfigType::List::iterator i = to_write.begin(); i != to_write.end(); i++) {
		schema << **i << endl;
	}
	schema << "</PrimeModelSchema>" << endl;
}

} // namespace ssfnet
} // namespace prime
