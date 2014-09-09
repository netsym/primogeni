/**
 * \file factory.h
 * \brief Header file for the ConfigurableFactory class.
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

#ifndef __CONFIG_FACTORY_H__
#define __CONFIG_FACTORY_H__

#include "os/ssfnet_types.h"
#include "os/config_type.h"
#include "os/ssfnet_exception.h"

namespace prime {
namespace ssfnet {

/**
 * \brief XXX ConfigurableFactory
 *
 * XXX
 */
class ConfigurableFactory {
	friend class ConfigType;
	friend class RuntimeVariables;
public:
	/**
	 * XXX registerConfigType(ConfigType* type)
	 */
	static ConfigType* registerConfigType(ConfigType* type, const char* alias1=NULL, const char* alias2=NULL);

	/**
	 * XXX getConfigType(int typeId)
	 */
	static ConfigType* getConfigType(int typeId);

	/**
	 * XXX getConfigType(int typeId)
	 */
	static ConfigType* getConfigType_jprime(int typeId);

	/**
	 * XXX getConfigType(const char* name)
	 */
	static ConfigType* getConfigType(const char* name);

	/**
	 * XXX getConfigType(const SSFNET_STRING& name)
	 */
	static ConfigType* getConfigType(const SSFNET_STRING& name);

	/**
	 * XXX template
	 */
	template<typename T>
	static T createInstance_jprime(int typeId) {
		ConfigType* ct = getConfigType_jprime(typeId);
		if(ct == NULL) {
			char tmp[512];
			snprintf(tmp,512,"The type id %i is invalid.",typeId);
			ssfnet_throw_exception(SSFNetException::config_type_err, tmp);
		}
		return SSFNET_STATIC_CAST(T,ct->instance());
	}

	/**
	 * XXX template
	 */
	template<typename T>
	static T createInstance(int typeId) {
		ConfigType* ct = getConfigType(typeId);
		if(ct == NULL) {
			char tmp[512];
			snprintf(tmp,512,"The type id %i is invalid.",typeId);
			ssfnet_throw_exception(SSFNetException::config_type_err, tmp);
		}
		return SSFNET_STATIC_CAST(T,ct->instance());
	}

	template<typename T>
	static T createInstance(const SSFNET_STRING& name) {
		ConfigType* ct = getConfigType(name);
		if(ct == NULL) {
			char tmp[512];
			snprintf(tmp,512,"The type %s is invalid.",name.c_str());
			ssfnet_throw_exception(SSFNetException::config_type_err, tmp);
		}
		return SSFNET_STATIC_CAST(T,ct->instance());
	}

	template<typename T>
	static T createInstance(const char* name) {
		ConfigType* ct = getConfigType(name);
		if(ct == NULL) {
			char tmp[512];
			snprintf(tmp,512,"The type %s is invalid.",name);
			ssfnet_throw_exception(SSFNetException::config_type_err, tmp);
		}
		return SSFNET_STATIC_CAST(T,ct->instance());
	}

	/**
	 * XXX generateSchema(PRIME_OSTREAM& schema)
	 */
	static void generateSchema(PRIME_OSTREAM& schema);
private:

	/** The constructor. */
	ConfigurableFactory(void);

	/** The destructor. */
	~ConfigurableFactory(void);

	/** XXX setup */
	static bool setup;

	/**
	 * XXX setupConfigTypes()
	 */
	static void setupConfigTypes();

	/** XXX str_types */
	static ConfigType::StrMap* str_types;

	/** XXX int_types */
	static ConfigType::IntMap* int_types;

	/** XXX int_types_jprime */
	static ConfigType::IntMap* int_types_jprime;

	/** alias_ctype **/
	static ConfigType* alias_ctype;

};

} // namespace ssfnet
} // namespace prime

#endif /*__CONFIG_FACTORY_H__*/
