/**
 * \file host.cc
 * \brief Source file for the Host class.
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

#include "os/logger.h"
#include "os/alias.h"
#include <string.h>

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(Alias)


Alias::Alias() {
}

Alias::~Alias() {
}

void Alias::initStateMap() {
	BaseEntity::initStateMap();
	unshared.shortcut_to=0;
}

void Alias::init() {
	resolveAliasPath();
}

void Alias::wrapup() {
}

BaseEntity* Alias::deference() {
	return unshared.shortcut_to;
}

void Alias::resolveAliasPath() {
	//LOG_DEBUG("Alias::resolveAliasPath()\n\tpath="<<unshared.alias_path.read()<<"\n\tfrom="<<getUName()<<endl)
	if(unshared.shortcut_to==NULL && !unshared.alias_path.read().isCleared()) {
		//starting here move up everytime '..' is seen, other we go down into the node using the str as the child name
		unshared.shortcut_to=this;
		RelativePath& p=unshared.alias_path.read();
		for(int idx=0;unshared.shortcut_to!=NULL && idx<p.len;idx++) {
			//LOG_DEBUG("At '"<<unshared.shortcut_to->getUName()<<"' uid="<<unshared.shortcut_to->getUID()<<endl)
			if(0==p[idx]->compare("..")) {
				//LOG_DEBUG("\tmoved up"<<endl)
				unshared.shortcut_to=unshared.shortcut_to->getParent();
			}
			else {
				//LOG_DEBUG("\tmoved down '"<<*(p[idx])<<"', size="<<unshared.shortcut_to->getSize()<<endl)
				unshared.shortcut_to=unshared.shortcut_to->getChildByName(*(p[idx]));
			}
		}
		if(unshared.shortcut_to==NULL) {
			char temp[1024];
			UName n = getUName();
			snprintf(temp,1024,"Could not resolve alias to %s from %s",unshared.alias_path.read().toString()->c_str(), n.toString()->c_str());
			ssfnet_throw_exception(SSFNetException::other_exception,temp);
		}
		p.clear();
	}
	else if(unshared.shortcut_to != NULL){
		return;//its already been resolved
	}
	else {
		LOG_ERROR("Should never see this."<<endl)
	}
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const RelativePath* x) {
	if(x)
		return os << *x;
	return os <<"[NULL(RelativePath)]";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const RelativePath& x) {
	for (int i = 0; i < x.len; i++) {
		if (i != 0)
			os << ":";
		os << x.value.path[i];
	}
	return os;
}
RelativePath::RelativePath() {
	len=0;
	value.path=0;
}

RelativePath::RelativePath(SSFNET_STRING& _path) {
	len=0;
	value.path=0;
	setPath(_path);
}
RelativePath::RelativePath(const RelativePath &o) {
	this->len=o.len;
	if(len<0) {
		value.cleared=true;
	}
	else if(len==0) {
		LOG_ERROR("You cannot copy a RelativePath that has not has its path set!"<<endl)
	}
	else {
		value.path=new SSFNET_STRING*[this->len];
		for(int i=0;i<len;i++) {
			value.path[i]=new SSFNET_STRING(o.value.path[i]->c_str());
		}
	}
}

RelativePath::RelativePath(RelativePath* o) {
	this->len=o->len;
	if(len<0) {
		value.cleared=true;
	}
	else if(len==0) {
		LOG_ERROR("You cannot copy a RelativePath that has not has its path set!"<<endl)
	}
	else {
		value.path=new SSFNET_STRING*[this->len];
		for(int i=0;i<len;i++) {
			value.path[i]=new SSFNET_STRING(o->value.path[i]->c_str());
		}
	}
}

RelativePath::~RelativePath() {
	if(len>0) {
		for(int i=0;i<this->len;i++) {
			delete this->value.path[i];
		}
		delete this->value.path;
	}
}

int RelativePath::size() {
	if(len>=0) return len;
	return 0;
}

void RelativePath::clear() {
	if(len>0) {
		for(int i=0;i<this->len;i++) {
			delete this->value.path[i];
		}
		delete this->value.path;
	}
	len=-1;
	value.cleared=true;
}
bool RelativePath::isCleared() {
	if(len<=0) {
		return value.cleared;
	}
	return false;
}

void RelativePath::setPath(SSFNET_STRING& _path) {
	if(len!=0) {
		LOG_ERROR("Path was already set! len="<<len<<endl)
	}
	SSFNET_VECTOR(SSFNET_STRING*) l;
	char str[_path.size() + 1];
	strcpy(str, _path.c_str());
	char * pch;
	pch = strtok(str, ":");
	while (pch != NULL) {
		SSFNET_STRING* s = new SSFNET_STRING(pch);
		l.push_back(s);
		pch = strtok(NULL, ":");
	}
	len=l.size();
	if(len>0) {
		value.path = new SSFNET_STRING*[len];
		SSFNET_VECTOR(SSFNET_STRING*)::iterator i;
		for(uint16_t i=0;i<l.size();i++) {
			value.path[i]=l[i];
			l[i]=NULL;
		}
		l.clear();
	}
}

SSFNET_STRING* RelativePath::operator[](const int idx) {
	if(idx<0) {
		LOG_ERROR("Invalid index "<<idx<<endl)
	}
	else if(idx>=this->len) {
		LOG_ERROR("Index, "<<idx<<" is out of bounds. Range=[0,"<<len<<")"<<endl)
	}
	return value.path[idx];
}

SSFNET_STRING* RelativePath::toString() {
	PRIME_STRING_STREAM ss;
	ss << this;
	return new SSFNET_STRING(ss.str().c_str());
}

template<> bool rw_config_var<SSFNET_STRING, RelativePath> (SSFNET_STRING& dst, RelativePath& src) {
	PRIME_STRING_STREAM ss;
	dst.clear();
	ss << src;
	dst.append(ss.str().c_str());
	return false;
}

template<> bool rw_config_var<RelativePath, SSFNET_STRING> (RelativePath& dst, SSFNET_STRING& src) {
	dst.setPath(src);
	return false;
}


} // namespace ssfnet
} // namespace prime
