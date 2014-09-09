/**
 * \file alias.h
 * \brief Header file for the Alias class.
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

#ifndef __ALIAS_H__
#define __ALIAS_H__

#include "os/ssfnet_types.h"
#include "os/config_entity.h"

namespace prime {
namespace ssfnet {

namespace model_builder{
class PartitionWrapper;
}
class Alias;
class RelativePath {
public:
	friend class Alias;
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const RelativePath* x);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const RelativePath& x);
	RelativePath();
	RelativePath(SSFNET_STRING& _path);
	RelativePath(RelativePath* o);
	RelativePath(const RelativePath &o);
	~RelativePath();

	/** XXX  operator[] **/
	SSFNET_STRING* operator[](const int idx);

	/** XXX  toString **/
	SSFNET_STRING* toString();

	void setPath(SSFNET_STRING& _path);

	int size();
protected:
	void clear();
	bool isCleared();
	struct {
		SSFNET_STRING** path;
		bool cleared;
	} value;
	int len;
};

template<> bool rw_config_var<SSFNET_STRING, RelativePath> (SSFNET_STRING& dst, RelativePath& src);
template<> bool rw_config_var<RelativePath, SSFNET_STRING> (RelativePath& dst, SSFNET_STRING& src);

/**
 * \brief A node with acts like a shortcut to another node
 *
 * XXX
 */
class Alias: public ConfigurableEntity<Alias, BaseEntity, 0, "NodeAlias"> {
	friend class model_builder::PartitionWrapper;
	friend class Community;
public:
	state_configuration {
		configurable RelativePath alias_path {
			type=OBJECT;
			default_value="";
			doc_string="the relative path to the node to which this alias points";
			visualized=true;
		};
		/**
		 * This is the node which this alias is a shortcut to.
		 */
		BaseEntity* shortcut_to;
	};

public:
	/** The constructor. */
	Alias();

	/** The destructor. */
	virtual ~Alias();

	/**
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/**
	 * The init method is used to initialize the alias once it has been
	 * created and configured by model builder.
	 * XXX
	 */
	virtual void init();

	/**
	 * The wrapup method is called at the end of the simulation. It is
	 * used to collect statistical information of the simulation run.
	 */
	virtual void wrapup();

	/**
	 * Get the node which this alias is a shortcut to.
	 */
	virtual BaseEntity* deference();

private:
	/**
	 * resolves alias_path to get the node this alias is a shortcut to
	 */
	void resolveAliasPath();

};

} // namespace ssfnet
} // namespace prime

#endif /*__ALIAS_H__*/
