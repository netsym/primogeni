/**
 * \file config_vars.cc
 * \brief Source file for the ConfigVarType (and ConfigVarMap) class.
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

#include <sstream>

#include "os/config_vars.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/config_factory.h"
#include "os/ssfnet_types.h"
#include "os/logger.h"
#include "os/ssfnet_exception.h"
#include "os/configurable_types.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT_REF(ConfigType)

uint32_t ConfigVarType::getAttrNameId(SSFNET_STRING& attr) {
	return getJPRIMEAttrID(attr);
}

ConfigVarType::ConfigVarType(const char* _name, ConfigVarType::Type _type,
		bool _required, bool _read_only, const char* _default_value,
		const char* _comment, ConfigType* owningType) :
	name(_name), default_value(_default_value), comment(_comment), required(
			_required), read_only(_read_only), type(_type) {
	owningType->register_property(this);
	id=getAttrNameId(name);
	if(0==id) {
		LOG_ERROR("Unknown attribute, name='"<<SSFNET_STRING(name)<<"'"<<endl);
	}
}
SSFNET_STRING& ConfigVarType::getName() {
	return name;
}
SSFNET_STRING& ConfigVarType::getDefaultValue() {
	return default_value;
}
SSFNET_STRING& ConfigVarType::getComment() {
	return comment;
}
bool ConfigVarType::isRequired() {
	return required;
}
bool ConfigVarType::isReadOnly() {
	return read_only;
}
uint32_t ConfigVarType::getAttrNameId() {
	return id;
}
ConfigVarType::Type ConfigVarType::getDataType() {
	return type;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, ConfigVarType& var) {
	os << "<attr name=\"" << var.getName();
	os << "\" type=\"";
	switch (var.getDataType()) {
	case ConfigVarType::INT:
		os << "int";
		break;
	case ConfigVarType::FLOAT:
		os << "float";
		break;
	case ConfigVarType::STRING:
		os << "string";
		break;
	case ConfigVarType::BOOL:
		os << "boolean";
		break;
	case ConfigVarType::OBJECT:
		os << "object";
		break;
	case ConfigVarType::RESOURCE_ID:
		os << "resource_id";
		break;
	default:
		LOG_ERROR("Unknown ConfigVarType "<<((int)var.getDataType())<<endl;)
	}
	os << "\" required=\"" << var.isReadOnly();
	os << "\" defaultValue=\"" << var.getDefaultValue();
	os << "\" isState=\"" << !var.isReadOnly();
	os << "\" comment=\"" << var.getComment() << "\" />";
	return os;
}

BaseEntityMember* BaseEntityMember::start_arg = new ConcreteBaseEntityMember(
		BaseEntityMember::START_TYPE);

BaseEntityMember* BaseEntityMember::end_arg = new ConcreteBaseEntityMember(
		BaseEntityMember::END_TYPE);

long BaseConfigVar::getMemUsage_object() {
	LOG_ERROR("Should never see this"<<endl)
	return -1;
}

long BaseConfigVar::getMemUsage_class() {
	LOG_ERROR("Should never see this"<<endl)
	return -1;
}

ConfigVarMap::ConfigVarMap() :
	ref_count(0) {
}

/** decrement the reference count and if its 0 delete it */
void ConfigVarMap::unreference() {
	if (ref_count == 0) {
		delete this;
	} else {
		ref_count--;
	}
}


ConfigVarMap::~ConfigVarMap() {
	if (ref_count != 0) {
		LOG_ERROR("Should Delete ConfigVarMap yet..."<<endl);
	}
}

BaseConfigVar::AttrMap& ConfigVarMap::getBackingMap() {
	return prop_map;
}

ConfigVarMap* ConfigVarMap::getReference() {
	ref_count++;
	return this;
}

uint32_t ConfigVarMap::getReferenceCount() {
	return ref_count;
}

BaseConfigVar*  ConfigVarMap::getVar(uint32_t attr_id) {
	BaseConfigVar::AttrMap::iterator it = prop_map.find(attr_id);
	if (it == prop_map.end()) {
		return NULL;
	}
	return it->second;
}

BaseConfigVar* ConfigVarMap::getVar(SSFNET_STRING& name) {
	uint32_t id=ConfigVarType::getAttrNameId(name);
	if(id==0) {
		return NULL;
	}
	return getVar(id);
}

uint32_t ConfigVarMap::count() {
	return prop_map.size();
}

void ConfigVarMap::__setup__(BaseEntityMember** args) {
	int i = 0;
	while (args[i]->getArgType() != BaseEntityMember::END_TYPE) {
		int type = args[i]->getArgType();
		switch (type) {
		case BaseEntityMember::VAR_TYPE: {
			BaseConfigVar* p = (BaseConfigVar*) args[i];
			if (p->getArgType() != BaseConfigVar::VAR_TYPE) {
				ssfnet_throw_exception(SSFNetException::config_type_err);
			}
			ConfigVarType* vt = ((ConfigVarType*) p->getVarType());
			if (!prop_map.insert(SSFNET_MAKE_PAIR(vt->getAttrNameId(),p)).second) {
				char tmp[512];
				snprintf(tmp,512,"%s was already in the variable map!",vt->getName().c_str());
				ssfnet_throw_exception(SSFNetException::config_varmap_err, tmp);
			}
		}
			break;
		case BaseEntityMember::END_TYPE:
		case BaseEntityMember::START_TYPE:
			//no op
			break;
		case BaseEntityMember::CHILD_TYPE:
		default:
			LOG_ERROR("Did not expect did get a childlist as an agurment!"<<endl)
			;
		}
		i++;
	}
}

long ConfigVarMap::getMemUsage_object() {
	return __get_size__(false);
}

long ConfigVarMap::getMemUsage_class(){
	return __get_size__(true);
}


long ConfigVarMap::__get_size__(bool do_static) {
	long rv=0;
	for(BaseConfigVar::AttrMap::iterator i=prop_map.begin();i!=prop_map.end();i++) {
		//ConfigVarType* vt = ((ConfigVarType*) (*i).second->getVarType());
		rv+=sizeof(int)+sizeof(void*);
	}
	return rv;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseConfigVar& x) {
	SSFNET_STRING s;
	x.ext_read(s);
	os<<s;
	return os;
}
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseConfigVar* x) {
	if(x)
		return os<<*x;
	return os <<"[NULL(BaseConfigVar)]";
}



} // namespace ssfnet
} // namespace prime
