/**
 * \file resource_identifier.h
 * \brief Header file for the ResourceIdentifier class.
 * \author Nathanael Van Vorst
 * 
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

#ifndef __RESOURCE_IDENTIFIER_H__
#define __RESOURCE_IDENTIFIER_H__

#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

class ResourceIdentifier;
class ConfigType;
class BaseEntity;

/** Used to serialize a ResourceIdentifier into a string **/
template<> bool rw_config_var<SSFNET_STRING, ResourceIdentifier> (
                SSFNET_STRING& dst, ResourceIdentifier& src);

/** Used to parse a string into a ResourceIdentifier **/
template<> bool rw_config_var<ResourceIdentifier, SSFNET_STRING> (
		ResourceIdentifier& dst, SSFNET_STRING& src);

class ResourceTerm {
public:
	typedef SSFNET_LIST(ResourceTerm) List;
	enum Scope {
		UP,
		DOWN,
		RECURSE_DOWN,
		SUB_ATTR,
		ATTR,
		NONE
	};
	ResourceTerm(ResourceTerm::Scope _scope, SSFNET_STRING _value): scope(_scope), value(_value)  {}
	ResourceTerm(const ResourceTerm& o): scope(o.scope), value(o.value)  {}
	ResourceTerm::Scope getScope() { return scope; }
	SSFNET_STRING& getValue() { return value; }
private:
	ResourceTerm::Scope scope;
	SSFNET_STRING value;
};

class ResourceFilter {
public:
	typedef SSFNET_LIST(ResourceTerm) List;
	enum Op {
		AND,
		OR,
		SET,
		GT,
		LT,
		GTE,
		LTE,
		EQ,
		NE,
		VALUE
	};
	ResourceFilter(ResourceFilter::Op _op, ResourceFilter* _left, ResourceFilter* _right) {
		op=_op;
		left.filter=_left;
		right=_right;
	}
	ResourceFilter(ResourceIdentifier* value) {
		op=VALUE;
		left.rid=value;
		right=0;
	}
	ResourceFilter(const ResourceFilter& o)  {
		op=o.op;
		left=o.left;
		right=o.right;
	}

	ResourceIdentifier* getValue() const {
		if(op==VALUE)
			return left.rid;
		return 0;
	}
	ResourceFilter* getLeft() const {
		if(op!=VALUE)
			return left.filter;
		return 0;
	}
	ResourceFilter* getRight() const {
		return right;
	}
private:
	ResourceFilter::Op op;
	union {
		ResourceFilter* filter;
		ResourceIdentifier* rid;
	} left;
	ResourceFilter* right;
};

class UncompiledRID {
public:
	typedef SSFNET_LIST(ResourceTerm::List) TermSet;

	UncompiledRID(): filter(0) {}
	UncompiledRID(const UncompiledRID& o){
		filter=o.filter;
		for(TermSet::const_iterator i = o.terms.begin();i!=o.terms.end();i++) {
			terms.push_back(*i);
		}
	}
	TermSet& getTerms() {
		return terms;
	}
	ResourceFilter* getFilter() const {
		return filter;
	}
	void setFilter(ResourceFilter* f) {
		filter=f;
	}

private:
	TermSet terms;
	ResourceFilter* filter;
};

class CompiledRID {
public:
	CompiledRID(ConfigType* _type): type(_type) {}
	CompiledRID(const CompiledRID& o): type(o.type) {
		for(UIDVec::const_iterator i=o.relativeIds.begin();i!=o.relativeIds.end();i++) {
			relativeIds.push_back(*i);
		}
	}
	UIDVec& getUIDVec() {
		return relativeIds;
	}
	ConfigType* getConfigType() const {
		return type;
	}
	static UID_t getUID(UID_t rid, BaseEntity* anchor);

private:
	UIDVec relativeIds;
	ConfigType* type;
};

/**
 * \brief An object which specifies a list of relative IDs to objects and/or properties.
 *
 * This class allows for sets of resources to be filtered and fetched
 */
class ResourceIdentifier {
public:
	/** The constructor. */
	ResourceIdentifier() {
		is_compiled=false;
		value.uncompiled=0;
	}
	ResourceIdentifier(const ResourceIdentifier& o) {
		is_compiled=o.is_compiled;
		if(is_compiled) {
			value.compiled=new CompiledRID(*o.getCompiledRI());
		}
		else {
			value.uncompiled=new UncompiledRID(*o.getUncompiledRI());
		}
	}

	/** The destructor. */
	virtual ~ResourceIdentifier() {
		if(is_compiled && value.compiled)
			delete value.compiled;
		else if(value.uncompiled)
			delete value.uncompiled;
	}

	UncompiledRID* getUncompiledRI() const{
		if(is_compiled)
			return 0;
		return value.uncompiled;
	}
	CompiledRID* getCompiledRI() const {
		if(is_compiled)
			return value.compiled;
		return 0;
	}
	bool isCompiled() const {
		return is_compiled;
	}

	void evaluate(BaseEntity* anchor);

	void init(UncompiledRID* v);

	void init(CompiledRID* v);

private:
	bool is_compiled;
	union {
		UncompiledRID* uncompiled;
		CompiledRID* compiled;
	} value;
};



}// namespace ssfnet
}// namespace prime

#endif /*__RESOURCE_IDENTIFIER_H__*/
