/**
 * \file config_type.cc
 * \brief Source file for the ConfigType (and ConfigChildType) class.
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

#include "os/logger.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/config_factory.h"
#include "os/ssfnet_types.h"
#include "os/partition.h"
namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(ConfigType)

int ConfigType::__type_id__ = 0;

int ConfigType::nextTypeId(int type_id) {
	if (type_id > 0)
		return type_id;
	else if (type_id < 0)
		LOG_ERROR("Specified an include type_id when declaring a configurable entity. type_id="<<type_id<<endl);
	return --__type_id__;
}

ConfigType::ConfigType(const char* _name, const char* _super_name,
		BaseEntity* _constructor(), int _type_id,
		int _jprime_impl_id, int _jprime_alias_id, int _jprime_replica_id, int _jprime_alias_replica_id) :
	type_id(ConfigType::nextTypeId(_type_id)),
	jprime_impl_id(_jprime_impl_id), jprime_alias_id(_jprime_alias_id),
	jprime_replica_id(_jprime_replica_id), jprime_alias_replica_id(_jprime_alias_replica_id),
	name(_name), super_name(_super_name), super_type(0) {
	this->constructor = _constructor;
}

void ConfigType::register_property(ConfigVarType* p) {
	//LOG_DEBUG("register property "<<p->getName()<<" in "<<name<<"(super="<<super_name<<")"<<endl);
	if (!vars.insert(SSFNET_MAKE_PAIR(p->getName(),p)).second)
		LOG_ERROR("Duplicate property or state variable '"<<p->getName()<<"'"<<endl);
}

void ConfigType::register_childtype(ConfigChildType* p) {
	//LOG_DEBUG("register child_type "<<p->getName()<<" in "<<name<<"(super="<<super_name<<")"<<endl);
	if (!child_types.insert(SSFNET_MAKE_PAIR(p->getName(),p)).second)
		LOG_ERROR("Duplicate child list '"<<p->getName()<<"'"<<endl);
}

const SSFNET_STRING& ConfigType::getName() {
	return name;
}
const SSFNET_STRING& ConfigType::getSuperName() {
	return super_name;
}

ConfigType::IntMap& ConfigType::getDerivedTypes() {
	return derived_types;
}

ConfigType* ConfigType::getSuperType() {
	if (super_type == NULL && super_name.size() > 0) {
		super_type = ConfigurableFactory::getConfigType(super_name);
	}
	return super_type;
}

ConfigVarMap* ConfigType::getVarMap() {
	return var_map;
}
bool ConfigType::isBase(bool checkSuper) {
	if (checkSuper)
		return getSuperName().size() == 0 || getSuperName().compare(
				BaseEntity::getClassConfigType()->getName()) == 0;
	return getName().compare(BaseEntity::getClassConfigType()->getName()) == 0;
}

BaseEntity* ConfigType::instance(ConfigVarMap* pmap) {
	ConfigurableFactory::setupConfigTypes();
	BaseEntity* rv = this->constructor();
	rv->initPropertyMap(pmap);
	rv->initStateMap();
	rv->setup();
	return rv;
}

void ConfigType::__init_map_vars__(ConfigVarMap* map, bool is_state) {
	ConfigType* type=this;
	while (type != NULL) {
		for (ConfigVarType::StrMap::iterator i = type->vars.begin(); i
				!= type->vars.end(); i++) {
			if (i->second->isReadOnly()) {
				//this is a property var but we are initing a state map
				if (is_state)
					continue;
			} else {
				//this is a state var but we are initing a property map
				if (!is_state)
					continue;
			}
			BaseConfigVar* v = map->getVar(i->second->getName());
			if (v == NULL) {
				for(BaseConfigVar::AttrMap::iterator q =map->getBackingMap().begin();q!=map->getBackingMap().end();q++) {
					LOG_DEBUG("var "<< (*q).first<<", "<<(*q).second<<endl)
				}
				char tmp[100];
				snprintf(tmp,100,"The variable '%s' was NULL! Should never see this",i->second->getName().c_str());
				ssfnet_throw_exception(SSFNetException::other_exception,tmp);
			}
			v->init(i->second->getDefaultValue(), false);
			SSFNET_STRING str;
			v->ext_read(str);
			//LOG_DEBUG("\t\tpre-init var:"<<i->first<<", default value="<<i->second->getDefaultValue()<<", value="<<str<<endl)
		}
		type = type->getSuperType();
	}
}

void ConfigType::registerDerivedType(ConfigType* c) {
	//LOG_DEBUG("Registering "<<c->getName()<<" as a derived type of "<< getName()<<"(super="<<getSuperName()<<")"<<endl);
	if (!derived_types.insert(SSFNET_MAKE_PAIR(c->type_id,c)).second)
		LOG_ERROR("'"<<c->getName()<<"' was already registered as a derived type of '"<<name<<"'"<<endl);
}

bool ConfigType::isSubtype(BaseEntity* e, bool deference) {
	if(deference)
		return isSubtype(e->deference()->getTypeId());
	return isSubtype(e->getTypeId());
}

bool ConfigType::isSubtype(ConfigType* ct) {
	//LOG_DEBUG("isSubtype "<<this->getName()<<"["<<this->type_id<<"]"<<" == "<<ct->getName()<<"["<<ct->type_id<<"]"<<endl)
	if (ct->type_id == this->type_id) {
		//LOG_DEBUG("\tTRUE"<<endl)
		return true;
	}
	ConfigType::IntMap::iterator it;
	for (it = derived_types.begin(); it != derived_types.end(); it++) {
		if ((*it).second->isSubtype(ct)) {
			//LOG_DEBUG("\tTRUE"<<endl)
			return true;
		}
	}
	//LOG_DEBUG("\tFALSE"<<endl)
	return false;
}

bool ConfigType::isSubtype(int tid) {
	//LOG_DEBUG("isSubtype "<<this->getName()<<"["<<this->type_id<<"]"<<" == "<<"["<<tid<<"]"<<endl)
	if (tid == this->type_id) {
		//LOG_DEBUG("\tTRUE"<<endl)
		return true;
	}
	ConfigType::IntMap::iterator it;
	//LOG_DEBUG("\tSTART looking at subs "<<getName()<<endl)
	for (it = derived_types.begin(); it != derived_types.end(); it++) {
		//LOG_DEBUG("\tlooking at sub "<<(*it).second->getName()<<endl)
		if ((*it).second->isSubtype(tid)) {
			//LOG_DEBUG("\tTRUE"<<endl)
			return true;
		}
	}
	//LOG_DEBUG("\tEND looking at subs "<<getName()<<endl)
	//LOG_DEBUG("\tFALSE "<<getName()<<endl)
	return false;
}


PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, ConfigType& c) {
	os << "<" << c.getName();
	if (!c.isBase())
		os << " extends=\"" << c.getSuperName() << "\" ";
	os << ">" << endl;
	ConfigType* cur = &c;
	while (true) {
		os << "   <!-- properties & children from " << cur->getName() << " -->"
				<< endl;
		for (ConfigVarType::StrMap::iterator i = cur->vars.begin(); i
				!= cur->vars.end(); i++)
			os << "   " << *(i->second) << endl;
		for (ConfigChildType::StrMap::iterator i = cur->child_types.begin(); i
				!= cur->child_types.end(); i++)
			os << "   " << *(i->second) << endl;
		if (cur->isBase(false)) {
			break;
		} else {
			cur = ConfigurableFactory::getConfigType(cur->getSuperName());
			if (cur == NULL)
				cur = BaseEntity::getClassConfigType();
		}
	}
	os << "</" << c.getName() << ">";
	return os;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, ConfigChildType& c) {
	os << "<childType name=\"" << c.getName();
	os << "\" type=\"" << c.getType();
	os << "\" min=\"" << c.getMin();
	os << "\" max=\"";
	if (c.getMax() == 0)
		os << "unbounded";
	else
		os << c.getMax();
	os << "\" isAliased=\"" << c.isAliased() << "\" />";
	return os;
}

ConfigChildType::ConfigChildType(const char* _name, const char* _type,
		bool _is_aliased, int _min, int _max, const char* _comment,
		ConfigType* owningType) :
	name(_name), type(_type), comment(_comment), is_aliased(_is_aliased), min(
			_min), max(_max), configType(NULL) {
	owningType->register_childtype(this);
}
SSFNET_STRING& ConfigChildType::getName() {
	return name;
}
SSFNET_STRING& ConfigChildType::getType() {
	return type;
}
SSFNET_STRING& ConfigChildType::getComment() {
	return comment;
}
int ConfigChildType::getMin() {
	return min;
}
int ConfigChildType::getMax() {
	return max;
}
bool ConfigChildType::isAliased() {
	return is_aliased;
}
ConfigType* ConfigChildType::getConfigType() {
	if (NULL == configType)
		configType = ConfigurableFactory::getConfigType(this->type);
	return configType;
}

} // namespace ssfnet
} // namespace prime
