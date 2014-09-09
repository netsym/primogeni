/*
 * \file config_type.h
 * \brief Header file for the ConfigType (and ConfigChildType) class.
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

#ifndef __CONFIG_TYPE_H__
#define __CONFIG_TYPE_H__

#include "os/ssfnet_types.h"
#include "os/config_vars.h"

namespace prime {
namespace ssfnet {

class ConfigType;//defined below
class ConfigChildType;//defined below
class BaseEntity; //defined in config_entity.h
class ConfigurableFactory;//defined in config_factory.h
class Partition; //defined in partition.h
/**
 * \brief XXX ConfigChildType
 *
 * XXX
 */
class ConfigChildType {
public:
	typedef SSFNET_LIST(ConfigChildType*) List;
	typedef SSFNET_MAP(SSFNET_STRING,ConfigChildType*) StrMap;

	/**
	 * The constructor. XXX ConfigChildType()
	 */
	ConfigChildType(const char* _name, const char* _type, bool _is_aliased,
			int _min, int _max, const char* _comment, ConfigType* owningType);

	/**
	 * XXX getName()
	 */
	SSFNET_STRING& getName();

	/**
	 * XXX getType()
	 */
	SSFNET_STRING& getType();

	/**
	 * XXX getComment()
	 */
	SSFNET_STRING& getComment();

	/** XXX getMin() */
	int getMin();

	/** XXX getMax() */
	int getMax();

	/** XXX isAliased() */
	bool isAliased();

	/** XXX getConfigType() */
	ConfigType* getConfigType();

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, ConfigChildType& c);
private:

	/**
	 * XXX name, type, comment;
	 */
	SSFNET_STRING name, type, comment;

	/** XXX is_aliased */
	bool is_aliased;

	/** XXX min, max */
	int min, max;

	/** XXX configType */
	ConfigType* configType;
};

/**
 * \brief XXX ConfigType
 *
 * XXX
 */
class ConfigType {
	friend class ConfigurableFactory;
	friend class BaseEntity;
	friend class ModelBuilder;
	friend class RuntimeVariables;
public:
	typedef SSFNET_LIST(ConfigType*) List;
	typedef SSFNET_MAP(SSFNET_STRING,ConfigType*) StrMap;
	typedef SSFNET_MAP(int,ConfigType*) IntMap;
	typedef SSFNET_SET(int) Set;

	/**
	 * The constructor. XXX ConfigType()
	 */
	ConfigType(const char* _name, const char* _super_name,
			BaseEntity* constructor(), int _type_id,
			int jprime_impl_id, int jprime_alias_id, int jprime_replica_id, int jprime_alias_replica_id);

	/**
	 * XXX register_property(ConfigVarType* p)
	 */
	void register_property(ConfigVarType* p);

	/**
	 * XXX register_childtype(ConfigChildType* p)
	 */
	void register_childtype(ConfigChildType* p);

	/**
	 * XXX getName()
	 */
	const SSFNET_STRING& getName();

	/**
	 * XXX getSuperName()
	 */
	const SSFNET_STRING& getSuperName();

	/**
	 * XXX ConfigType::IntMap& getDerivedTypes()
	 */
	ConfigType::IntMap& getDerivedTypes();

	/**
	 * XXX ConfigType* getSuperType();
	 */
	ConfigType* getSuperType();

	/**
	 * XXX instance(ConfigVarMap* pmap=0)
	 */
	BaseEntity* instance(ConfigVarMap* pmap = 0);

	/**
	 * XXX getVarMap()
	 */
	ConfigVarMap* getVarMap();

	/** XXX type_id */
	const int type_id;

	/** XXX*/
	const int jprime_impl_id;
	const int jprime_alias_id;
	const int jprime_replica_id;
	const int jprime_alias_replica_id;

	/**
	 * XXX registerDerivedType(ConfigType* c)
	 */
	void registerDerivedType(ConfigType* c);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, ConfigType& c);

	/**
	 * XXX isBase(bool checkSuper=true)
	 */
	bool isBase(bool checkSuper = true);

	/** XXX is_subtype(ConfigType* ct) **/
	bool isSubtype(ConfigType* ct);

	/** XXX is_subtype(BaseEntity* e) **/
	bool isSubtype(BaseEntity* e, bool deference=false);

	/** XXX is_subtype(int type_id) **/
	bool isSubtype(int type_id);

private:
	/** XXX name, super_name */
	SSFNET_STRING name, super_name;

	/**
	 * XXX super_type
	 */
	ConfigType* super_type;

	/** XXX vars */
	ConfigVarType::StrMap vars;

	/** XXX child_types */
	ConfigChildType::StrMap child_types;

	/** XXX derived_types */
	ConfigType::IntMap derived_types;

	/** XXX var_map */
	ConfigVarMap* var_map;

	/** XXX */
	BaseEntity* (*constructor)(void);

	/**
	 * XXX __init_map_vars__(ConfigVarMap* map,ConfigType* type);
	 */
	void __init_map_vars__(ConfigVarMap* map, bool is_state);

	/** XXX nextTypeId(int type_id) */
	static int nextTypeId(int type_id);

	/** XXX __type_id__ */
	static int __type_id__;
};



} // namespace ssfnet
} // namespace prime

#endif /*__CONFIG_TYPE_H__*/
