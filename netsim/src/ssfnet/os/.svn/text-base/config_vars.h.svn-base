/**
 * \file config_vars.h
 * \brief Header file for the ConfigVarType (and ConfigVarMap) class.
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

#ifndef __CONFIG_VARS_H__
#define __CONFIG_VARS_H__

#include "os/ssfnet_types.h"
#include "os/ssfnet_exception.h"

namespace prime {
namespace ssfnet {
class BaseEntity;
class ModelBuilder;
class BaseConfigVar; //defined below
class ConfigVarType;//defined below
class ConfigType;//defined in config_type.h
class LogLvl;
class UpdateTrafficTypeEvent;
namespace model_builder {
class ModelNode;
}
//*********************************
// MACROS
//*********************************
// see config_type.h

/**
 *\brief Describe a property or a state variable
 */
class ConfigVarType {
public:
	enum Type {
		INT, FLOAT, STRING, BOOL, OBJECT, RESOURCE_ID
	};
	typedef SSFNET_LIST(ConfigVarType*) List;
	typedef SSFNET_MAP(SSFNET_STRING,ConfigVarType*) StrMap;
	typedef SSFNET_MAP(SSFNET_STRING,uint32_t) Str2HashIdMap;

	/** The constructor. */
	ConfigVarType(const char* _name, ConfigVarType::Type _type, bool _required,
			bool _read_only, const char* _default_value, const char* _comment,
			ConfigType* owningType);

	/**
	 * XXX getName()
	 */
	SSFNET_STRING& getName();

	/**
	 * XXX getDefaultValue()
	 */
	SSFNET_STRING& getDefaultValue();

	/**
	 * XXX getComment()
	 */
	SSFNET_STRING& getComment();

	/**
	 * XXX isRequired()
	 */
	bool isRequired();

	/**
	 * isReadOnly()
	 */
	bool isReadOnly();

	/**
	 * XXX getAttrNameId()
	 */
	uint32_t getAttrNameId();

	/**
	 * getDataType()
	 */
	ConfigVarType::Type getDataType();

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, ConfigVarType& var);

	/**
	 * return the id of the attr_namne/string. On error 0 is returned, otherwise the id is returned
	 */
	static uint32_t getAttrNameId(SSFNET_STRING& attr);
private:

	/** XXX name, default_value, comment */
	SSFNET_STRING name, default_value, comment;

	/** XXX required, read_only */
	bool required, read_only;

	/** XXX type */
	ConfigVarType::Type type;

	/** XXX type */
	uint32_t id;
};

/**
 * \brief Base class of all properties, states, and child lists
 */
class BaseEntityMember {
public:
	enum Type {
		VAR_TYPE, CHILD_TYPE, START_TYPE, END_TYPE
	};
	typedef SSFNET_LIST(BaseEntityMember*) List;
	typedef SSFNET_MAP(SSFNET_STRING,BaseEntityMember*) StrMap;
	static BaseEntityMember* start_arg;
	static BaseEntityMember* end_arg;

	/** The constructor. */
	BaseEntityMember() {
	}

	/** The destructor. */
	virtual ~BaseEntityMember() {
	}

	/** XXX getArgType() */
	virtual BaseEntityMember::Type getArgType()=0;
};

/**
 * \brief Used fort the start/end types
 */
class ConcreteBaseEntityMember: public BaseEntityMember {
public:
	/** The constructor. */
	ConcreteBaseEntityMember(BaseEntityMember::Type _type) :
		type(_type) {
	}

	/** The destructor. */
	virtual ~ConcreteBaseEntityMember() {
	}

	/** XXX getArgType() */
	BaseEntityMember::Type getArgType() {
		return type;
	}
private:
	BaseEntityMember::Type type;
};

class Aggregate;

/**
 * \brief Base for all properties and states
 */
class BaseConfigVar: public BaseEntityMember {
	friend class ConfigVarMap;
	friend class BaseEntity;
	friend class ConfigType;
	friend class UpdateTrafficTypeEvent;
	friend class model_builder::ModelNode;
	friend class Aggregate;
public:
	typedef SSFNET_MAP(uint32_t, BaseConfigVar*) AttrMap;
	typedef SSFNET_LIST(BaseConfigVar*) List;

	/** The constructor. */
	BaseConfigVar() {
	}

	/** The destructor. */
	virtual ~BaseConfigVar() {
	}

	/** XXX getArgType() */
	BaseEntityMember::Type getArgType() {
		return BaseEntityMember::VAR_TYPE;
	}
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseConfigVar& x);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseConfigVar* x);
	/**
	 * XXX getVarType()
	 */
	virtual ConfigVarType* getVarType()=0;

protected:
	/**
	 * XXX ext_read(SSFNET_STRING& dest)
	 * returns true on error
	 */
	virtual bool ext_read(SSFNET_STRING& dest)=0;

	/**
	 * XXX ext_write(SSFNET_STRING& new_val)
	 * returns true on error
	 */
	virtual bool ext_write(SSFNET_STRING& new_val)=0;

	/**
	 * XXX is_virgin()
	 */
	virtual bool is_virgin()=0;

	/**
	 * XXX init(SSFNET_STRING& new_val)
	 * returns true on error
	 */
	virtual bool
	init(SSFNET_STRING& new_val,bool defile)=0;

	/**
	 * get a pointer to the value...
	 */
	virtual void* getValuePtr() = 0;

	virtual double getValueAsDouble() = 0;
public:
	/**
	 * \brief calculates the amount of memory used by this var
	 *
	 */
	virtual long getMemUsage_object();
	/**
	 * \brief calculates the amount of memory used by this var
	 *
	 */
	virtual long getMemUsage_class() ;
};

/**
 * \brief XXX ConfigProperty
 *
 * XXX
 */
template<typename T, const ConfigVarType* getType(void)>
class ConfigProperty: public BaseConfigVar {
	friend class ConfigVarMap;
public:
	/** The constructor. */
	ConfigProperty() :
		BaseConfigVar(), value(T()),virgin(true) {
	}

	/** The destructor. */
	virtual ~ConfigProperty() {
	}

	/**
	 * XXX read(void)
	 */
	T& read(void) {
		return value;
	}

	/**
	 * XXX operator*()
	 */
	T& operator*() {
		return value;
	}

	/**
	 * XXX getVarType()
	 */
	ConfigVarType* getVarType() {
		return const_cast<ConfigVarType*> (getType());
	}

protected:

	void* getValuePtr()  {
		return &value;
	}

	virtual double getValueAsDouble() {
		double rv;
		rw_config_var<double, T> (rv, value);
		return rv;
	}


	/**
	 * XXX ext_read(SSFNET_STRING& dest)
	 * return true on error
	 */
	bool ext_read(SSFNET_STRING& dest) {
		return rw_config_var<SSFNET_STRING, T> (dest, value);
	}

	/**
	 * XXX ext_write(SSFNET_STRING& new_val)
	 * return true on error
	 */
	bool ext_write(SSFNET_STRING& new_val) {
		virgin=false;
		return rw_config_var<T, SSFNET_STRING> (value, new_val);
	}

	/**
	 * XXX init(SSFNET_STRING& new_val)
	 * return true on error
	 */
	bool init(SSFNET_STRING& new_val,bool defile) {
		if(defile)virgin=false;
		return rw_config_var<T, SSFNET_STRING> (value, new_val);
	}

	/**
	 * XXX is_virgin()
	 */
	bool is_virgin() {
		return virgin;
	}

	/**
	 * XXX __get_var_type__()
	 */
	static ConfigVarType* __get_var_type__() {
		return const_cast<ConfigVarType*> (getType());
	}

	/**
	 * XXX __ext_read__(BaseConfigVar* inst, SSFNET_STRING& dest)
	 * return true on error
	 */
	static bool __ext_read__(BaseConfigVar* inst, SSFNET_STRING& dest) {
		return inst->ext_read(dynamic_cast<ConfigProperty<T, getType>*> (dest));
	}

	/**
	 * XXX __ext_write__(BaseConfigVar* inst, SSFNET_STRING& new_val)
	 * return true on error
	 */
	static bool __ext_write__(BaseConfigVar* inst, SSFNET_STRING& new_val) {
		return inst->ext_write(
				dynamic_cast<ConfigProperty<T, getType>*> (new_val));
	}

	/**
	 * XXX __init__(BaseConfigVar* inst, SSFNET_STRING& new_val)
	 * return true on error
	 */
	static bool __init__(BaseConfigVar* inst, SSFNET_STRING& new_val)  {
		return inst->init(dynamic_cast<ConfigProperty<T, getType>*> (new_val));
	}

	/** XXX value */
	T value;
	bool virgin;
public:
	virtual long getMemUsage_object() { return sizeof(T); }
	virtual long getMemUsage_class()  { return sizeof(T); }
};

/**
 * \brief XXX ConfigState
 *
 * XXX
 */
template<typename T, const ConfigVarType* getType(void)>
class ConfigState: public ConfigProperty<T, getType> {
public:
	/** The constructor. */
	ConfigState() :
		ConfigProperty<T, getType> () {
	}
	/** The destructor. */
	virtual ~ConfigState() {
	}

	//XXX when the monitor stuff is added the * operator and write func
	//will notify the monitor of a change
	/**
	 * XXX
	 */
	T& operator*() {
		return this->value;
	}

	/**
	 * XXX write(const T& new_val)
	 */
	bool write(const T& new_val) {
		return rw_config_var<T, const T> (this->value, new_val);
	}

	/**
	 * XXX write(T& new_val)
	 */
	bool write(T& new_val) {
		return rw_config_var<T, T> (this->value, new_val);
	}

	/**
	 * write(const char* str)
	 */
	bool write(const char* str) {
		SSFNET_STRING _str(str);
		return write(_str);
	}
public:
	long getMemUsage_object() { return sizeof(T); }
	long getMemUsage_class()  { return sizeof(T); }
};

/**
 * \brief Each BaseEntity will have a ConfigVarMap for properties and state
 */
class ConfigVarMap {
	friend class ConfigurableFactory;
	friend class BaseEntity;
	//friend class ModelBuilder;
	friend class model_builder::ModelNode;
public:
	/** The constructor. */
	ConfigVarMap();

	/** decrement the reference count and if its 0 delete it */
	void unreference();

	/**
	 * XXX getVar(SSFNET_STRING& name)
	 */
	BaseConfigVar* getVar(SSFNET_STRING& name);

	/**
	 * XXX getVar(uint32_t attr_id)
	 */
	BaseConfigVar* getVar(uint32_t attr_id);

	/**
	 * XXX ConfigVarMap* getReference();
	 */
	ConfigVarMap* getReference();

	/**
	 * uint32_t getReferenceCount();
	 */
	uint32_t getReferenceCount();

	/**
	 * XXX count()
	 */
	uint32_t count();

	/** Get the map which stores the properties **/
	BaseConfigVar::AttrMap& getBackingMap();

	/**
	 * \brief calculates the amount of memory used by this entity
	 *
	 */
	virtual long getMemUsage_object()=0;

	/**
	 * \brief calculates the amount of memory used by this entity
	 *
	 */
	virtual long getMemUsage_class()=0;

protected:

	/** The destructor. */
	virtual ~ConfigVarMap();

	/**
	 * XXX __setup__(BaseEntityMember** args)
	 */
	virtual long __get_size__(bool do_static) ;

	/**
	 * XXX __setup__(BaseEntityMember** args)
	 */
	void __setup__(BaseEntityMember** args) ;

	/** XXX prop_map */
	BaseConfigVar::AttrMap prop_map;

	/**
	 * number of references
	 */
	uint32_t ref_count;
};

} // namespace ssfnet
} // namespace prime

#endif /*__CONFIG_VARS_H__*/
