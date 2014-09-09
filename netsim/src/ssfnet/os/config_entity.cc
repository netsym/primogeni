/**
 * \file config_entity.cc
 * \brief Source file for the BaseEntity (and ConfigEntity) class.
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

#include "os/config_entity.h"
#include "os/config_type.h"
#include "os/logger.h"
#include "net/net.h"
#include "os/alias.h"
#include "net/interface.h"
#include "os/partition.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(BaseEntity)

ConfigType* BaseEntity::getClassConfigType() {
	return BaseEntity::__config__;
}

int BaseEntity::getClassConfigTypeId() {
	return BaseEntity::__config__->type_id;
}

ConfigType* BaseEntity::__config__ = ConfigurableFactory::registerConfigType(
		new ConfigType("BaseEntity", "", NULL, 0, -1, -1, -1, -1));

BaseEntity::BaseEntity() :
	propmap(0), statemap(0) {

}

ConfigVarMap* BaseEntity::propertyMapInstance() {
	LOG_ERROR("Should never see this"<<endl)
	return NULL;
}

ConfigVarMap* BaseEntity::stateMapInstance() {
	LOG_ERROR("Should never see this"<<endl)
	return NULL;
}

BaseEntity::~BaseEntity() {
}

UID_t BaseEntity::getUID() const {
	return unshared.uid;
}

void BaseEntity::setOffset(UID_t new_offset) {
	unshared.offset.read()=new_offset;
}

void BaseEntity::setUID(UID_t id) {
	unshared.uid=id;
}

UID_t BaseEntity::getMinUID() {
	//since we want the topnet we can set the anchor to be null
	return getMinRank(NULL);
}

UID_t BaseEntity::getRank(BaseEntity* anchor) {
	if(anchor) {
		//we can do a fast calculation
		UID_t t=getUID();
		UID_t diff=anchor->getUID()-anchor->getSize();
		if(anchor->containsUID(t) && diff<t) {
			//LOG_DEBUG(getUName()<<".getRank("<<t<<","<<anchor->getUName()<<")="<<(t-diff)<<endl)
			return (t-diff);
		}
		ssfnet_throw_exception(SSFNetException::other_exception,"Tried to get the rank of a node using an anchor which was not an ancestor!");
	}
	return getUID();
}

UID_t BaseEntity::getMinRank(BaseEntity* anchor) {
	if(anchor) {
		return getRank(anchor)-getSize()+1;
	}
	return getUID()-getSize()+1;
}

bool BaseEntity::containsUID(UID_t rank) {
	//since we want the topnet we can set the anchor to be null
	return this->getMinUID() <= rank && rank <= this->getUID();
	//XXX return containsRank(rank, NULL);
}

/*
XXX this is very slow
bool BaseEntity::containsRank(UID_t rank, BaseEntity* anchor) {
	//LOG_DEBUG("dst_uid is "<<rank<<", this entity is "<<this->getUName()<<endl)
	uint64_t uid_offset = 0;
	BaseEntity* c = this;
	//find out the offset from the topnet
	while (c->getParent()!= NULL && c != anchor) {
		uid_offset += c->getOffset();
		c = c->getParent();
	}
	//LOG_DEBUG("uid_offset is "<<uid_offset<<", this's size is "<<this->getSize()<<endl)
	return rank >= (1 + uid_offset) && rank <= (uid_offset + this->getSize());
}
*/

uint64_t BaseEntity::getSize() {
	return SSFNET_DYNAMIC_CAST(__prop__ *,getPropertyMap())->size.read();
}

uint64_t BaseEntity::getOffset() {
	return SSFNET_DYNAMIC_CAST(__state__ *,getStateMap())->offset.read();
}

BaseEntity* BaseEntity::previousSibling() {
	if (statemap->parent != NULL) {
		ChildIterator<BaseEntity*> sibs=statemap->parent->getAllChildren();
		BaseEntity* prev=NULL,*cur=NULL;
		while(sibs.hasMoreElements()) {
			prev=cur;
			cur=sibs.nextElement();
			if(cur == this) {
				return prev;
			}
		}
	}
	//LOG_DEBUG("\tno prev sib"<<endl)
	return NULL;
}

BaseEntity* BaseEntity::predecessor(BaseEntity* anchor) {
	BaseEntity* pred = NULL;
	if(shared.offsets.size() != 0) {
		pred = OffsetTypePair::getChild(OffsetTypePair::getPartitionInstance(),this,shared.offsets.back().id_offset);
	}
	if (NULL == pred) {
		pred = previousSibling();
	}
	if (NULL == pred) {
		BaseEntity* p = statemap->parent;
		if (anchor == p || this == anchor) {
			pred = NULL;
		} else {
			//find the left most ancestor...
			while (pred == NULL && p != NULL && p != anchor) {
				pred = p->previousSibling();
				p = p->getParent();
			}
		}
	}
	return pred;
}

SSFNET_STRING* BaseEntity::getName() {
	return &(SSFNET_DYNAMIC_CAST(__state__*,getStateMap())->name.read());
}

const UName BaseEntity::getUName() {
	return UName(this);
}

ChildIterator<BaseEntity*> BaseEntity::getAllChildren() {
	//LOG_DEBUG("Creating all child iterator, shared.offsets.size()="<<shared.offsets.size()<<endl)
	return ChildIterator<BaseEntity*>(OffsetTypePair::getPartitionInstance(), 0, shared.offsets, this, shared.offsets.size()-1, 0);
}


BaseConfigVar* BaseEntity::findVar(SSFNET_STRING& name) {
	BaseConfigVar* rv = getPropertyMap()->getVar(name);
	if (!rv) {
		rv = getStateMap()->getVar(name);
	}
	return rv;
}

BaseConfigVar* BaseEntity::findVar(uint32_t attr_id) {
	BaseConfigVar* rv = getPropertyMap()->getVar(attr_id);
	if (!rv) {
		rv = getStateMap()->getVar(attr_id);
	}
	return rv;
}

bool BaseEntity::readVar(uint32_t attr_id, SSFNET_STRING& dst) {
	BaseConfigVar* v = findVar(attr_id);
	if (NULL == v)
		return true;
	return v->ext_read(dst);
}

bool BaseEntity::writeVar(uint32_t attr_id, SSFNET_STRING& val) {
	BaseConfigVar* v = findVar(attr_id);
	if (NULL == v)
		return true;
	return v->ext_write(val);
}

bool BaseEntity::initVar(uint32_t attr_id, SSFNET_STRING& val) {
	BaseConfigVar* v = findVar(attr_id);
	if (!v) {
		return true;
	}
	//LOG_DEBUG("initVar("<<name<< ","<<val<<")"<<endl)
	return v->init(val, true);
}

bool BaseEntity::readVar(SSFNET_STRING& name, SSFNET_STRING& dst) {
	BaseConfigVar* v = findVar(name);
	if (NULL == v)
		return true;
	return v->ext_read(dst);
}

bool BaseEntity::writeVar(SSFNET_STRING& name, SSFNET_STRING& val) {
	BaseConfigVar* v = findVar(name);
	if (NULL == v)
		return true;
	return v->ext_write(val);
}

bool BaseEntity::initVar(SSFNET_STRING& name, SSFNET_STRING& val) {
	BaseConfigVar* v = findVar(name);
	if (!v) {
		LOG_DEBUG("Unknown var "<<name<<endl)
		return true;
	}
	//LOG_DEBUG("initVar("<<name<< ","<<val<<")"<<endl)
	return v->init(val, true);
}

bool BaseEntity::readVar(const char* name, SSFNET_STRING& dst) {
	SSFNET_STRING n(name);
	return readVar(n, dst);
}

bool BaseEntity::writeVar(const char* name, const char* val) {
	SSFNET_STRING n(name), v(val);
	return writeVar(n, v);
}

bool BaseEntity::initVar(const char* name, const char* val) {
	SSFNET_STRING n(name), v(val);
	return initVar(n, v);
}

void BaseEntity::__setup__(BaseEntityMember** args) {
	//XXX i think this function is no longer needed!
	int i = 0;
	while (args[i]->getArgType() != BaseEntityMember::END_TYPE) {
		int type = args[i]->getArgType();
		switch (type) {
		case BaseEntityMember::CHILD_TYPE:
			//no op
			break;
		case BaseEntityMember::END_TYPE:
		case BaseEntityMember::START_TYPE:
			//no op
			break;
		case BaseEntityMember::VAR_TYPE:
			LOG_ERROR("Did not expect to a state or property to be passed in!"<<endl)
			break;
		default:
			LOG_ERROR("Unknown argument type("<< type<<")!"<<endl)
		}
		i++;
	}
}

void BaseEntity::copy_init(BaseEntity* o) {
	//XXX
	//LOG_WARN("Need to replace copy init!"<<endl)
	//LOG_DEBUG("copy_init dst='"<<this->getName()<<"', src='"<<o->getName()<<"'"<<endl);
	if (this->getConfigType()->isSubtype(o->getConfigType())) {
		if (this->getStateMap() == NULL || o->getStateMap() == NULL) {
			LOG_ERROR("SHOULD NEVER BE HERE..."<<endl)
		}
		BaseConfigVar::AttrMap& my_map = statemap->prop_map;
		BaseConfigVar::AttrMap& other_map = o->statemap->prop_map;
		BaseConfigVar::AttrMap::iterator i;
		SSFNET_STRING s;
		for (i = my_map.begin(); i != my_map.end(); i++) {
			BaseConfigVar* new_val = other_map.find(i->first)->second;
			if (i->second->is_virgin()) {
				//only overwrite default values
				new_val->ext_read(s);
				this->initVar(i->first, s);
			} else {
				//new_val->ext_read(s);
				//LOG_DEBUG("not overwriting "<<i->second->getVarType()->getName()<<", val="<<s<<"["<<i->second->getVarType()->getDefaultValue()<<"], new_name="<<s<<", type="<<getTypeName()<<endl)
			}
		}
	} else {
		LOG_ERROR("Cannot copy nodes which are not within the same type hierarchy!this="<<getTypeName()<<", o="<<o->getTypeName()<<endl)
	}
	//LOG_DEBUG("copy_init dst='"<<this->getUName()<<"', src='"<<o->getUName()<<"'"<<endl);
}

void BaseEntity::initPropertyMap(ConfigVarMap* map) {
	if (NULL == map) {
		propmap=SSFNET_DYNAMIC_CAST(__prop__*,propertyMapInstance());
		getConfigType()->__init_map_vars__(propmap, false);
	} else {
		if(propmap) {
			/*
			LOG_DEBUG("Old map of "<<getName()<<endl)
			BaseConfigVar::AttrMap& m = propmap->getBackingMap();
			for(BaseConfigVar::AttrMap::iterator i = m.begin();i!=m.end();i++) {
				SSFNET_STRING s;
				(*i).second->ext_read(s);
				LOG_DEBUG("\t"<<(*i).second->getVarType()->getName()<<"="<<s<<endl)
			}
			LOG_DEBUG("\tNew map:"<<endl)
			m = map->getBackingMap();
			for(BaseConfigVar::AttrMap::iterator i = m.begin();i!=m.end();i++) {
				SSFNET_STRING s;
				(*i).second->ext_read(s);
				LOG_DEBUG("\t\t"<<(*i).second->getVarType()->getName()<<"="<<s<<endl)
			}*/
			propmap->unreference();
		}
		propmap=SSFNET_DYNAMIC_CAST(__prop__*,map->getReference());
	}
}

void BaseEntity::initStateMap() {
	if(statemap) delete statemap;
	statemap=SSFNET_DYNAMIC_CAST(__state__*,stateMapInstance());
	getConfigType()->__init_map_vars__(statemap, true);
	statemap->parent=0;
	statemap->uid=0;
}

void BaseEntity::setup() {
	//no op
}

//BaseEntity* BaseEntity::getParent() const {
//	return statemap->parent;
//}

void BaseEntity::setParent(BaseEntity* p) {
	if(statemap->parent && propmap->offsets.size()!=0) {
		LOG_ERROR("Currently you cannot re-attach a sub-tree to another parent!"<<endl)
	}
	statemap->parent = p;
}

void BaseEntity::addChild(BaseEntity* e) {
	BaseEntity* prev=0;
	BaseEntity* cur=0;
	OffsetTypePair* cur_otp=NULL;
	Partition* part = Partition::getInstance();
	if (NULL == e) {
		ssfnet_throw_exception(SSFNetException::other_exception,"You cannot add a null child!");
	}
	e->setParent(this);
	if(e->getName()->compare("[no name]")==0) {
		e->getName()->clear();
		e->getName()->append(e->getTypeName());
		e->getName()->append("_");
		char temp[100];
		sprintf(temp,"%llu",e->getUID());
		e->getName()->append(temp);
		LOG_DEBUG("Added child "<<e->getUName()<<" to "<<getUName()<<" with an auto name"<<endl);
	}
	//find the place to add the child
	//look for the first child whose context isn't there and add use the ID
	//LOG_DEBUG("Adding child "<<e->getUName()<<", parent_uid=["<<getMinUID()<<","<<getUID()<<"]"<<", type_id="<<e->getTypeId()<<endl)
	//LOG_DEBUG("shared.offsets="<<(void*)&(shared.offsets)<<", size="<<shared.offsets.size()<<endl)
	for(uint idx=0;idx<shared.offsets.size();idx++) {
		prev=cur;
		cur = OffsetTypePair::getChild(part, this, idx, false);
		//std::cout<<"["<<idx<<"]type="<<shared.offsets[idx].type<<", e.type="<<e->getTypeId()<<", compare="<<(shared.offsets[idx].type==e->getTypeId())<<", cur==NULL?"<<(cur?"false":"true")<<std::endl;
		if(!cur) {
			//LOG_DEBUG("\t["<<idx<<"]prev sib=NULL, type_id="<<shared.offsets[idx].type<<endl);
			//we found a null child
			if(shared.offsets[idx].type==e->getTypeId()) {
				//it has the same type so we can use its offset
				cur_otp=&(shared.offsets[idx]);
				//LOG_DEBUG("Found an existing id_offset to use"<<endl);
				break;
			}
			else {
				//seems one of my replicated counter parts added a child I didn't...
				//I need to create one to match, and keep looking for
				//an available offset which has the same type as e
				cur = ConfigurableFactory::createInstance<BaseEntity*>(shared.offsets[idx].type);
				addChild(cur);
			}
		}
		//else  LOG_DEBUG("\t["<<idx<<"]prev sib="<<cur->getUName()<<", "<<cur->getUID()<<", type_id="<<shared.offsets[idx].type<<endl);
	}
	part->getStructuralModifcationLock();
	if(e->getUID()==0) {
		//LOG_DEBUG("Setting the UID"<<endl)
		if(e->getSize()==0) {
			//LOG_DEBUG("assuming size is 1"<<endl)
			//lets assume its 1.....
			//XXX check if there are kids...
			e->propmap->size.read()=1;
		}
		if(!prev && !cur) {
			//its the first
			//LOG_DEBUG("It was the first child"<<endl)
			if(getMinUID()==getUID()) {
				LOG_WARN("There are no more uids left for this node so you can't add more children! UName="<<getUName()<<endl);
				//std::cout<<"[1]There are no more uids left for this node so you can't add more children! UName="<<getUName()<<endl;
				ssfnet_throw_exception(SSFNetException::other_exception,"There are no more uids left for this node so you can't add more children!");
			}
			e->setUID(getMinUID());
			e->statemap->offset.read()=0;
		}
		else {
			//its has a prev sibling
			if(cur) {
				//LOG_DEBUG("It was _not_ the first child. cur="<<cur->getUName()<<endl)
				e->setUID(cur->getUID()+e->getSize());
				e->statemap->offset.read()=cur->getOffset()+cur->getSize();
			}
			else {
				//LOG_DEBUG("It was _not_ the first child. prev="<<prev->getUName()<<", "<<prev->getUID()<<endl)
				e->setUID(prev->getUID()+e->getSize());
				e->statemap->offset.read()=prev->getOffset()+prev->getSize();
			}
			if(e->getUID()==getUID()) {
				LOG_WARN("There are no more uids left for this node so you can't add more children! UName="<<getUName()<<endl);
				LOG_WARN("e->getUID()["<<e->getUID()<<"]==getUID()["<<getUID()<<"]"<<endl);

				//std::cout<<"[2]There are no more uids left for this node so you can't add more children! UName="<<getUName()<<endl;
				//std::cout<<"e->getUID()["<<e->getUID()<<"]==getUID()["<<getUID()<<"]"<<endl;
				ssfnet_throw_exception(SSFNetException::other_exception,"There are no more uids left for this node so you can't add more children!");

			}
		}
	}
	else if(e->getSize()==0) {
		LOG_DEBUG("the size was 0 but it had a uid of "<<e->getUID()<<endl)
		e->propmap->size.read()=e->getUID() - e->getMinUID();
	}
	//LOG_DEBUG("e->getUID()="<<e->getUID()<<", e->getSize()="<<e->getSize()<<endl)
	if(!cur_otp) {
		//need to add a new child
		//LOG_DEBUG("Creating an id_offset to use"<<endl)
		OffsetTypePair temp(e->getOffset()+e->getSize(),e->getTypeId());
		shared.offsets.push_back(temp);
		cur_otp=&(shared.offsets[shared.offsets.size()-1]);
	}
	if(getUID()-getSize()+cur_otp->id_offset != e->getUID()) {
		LOG_WARN("e->uid="<<e->getUID()<<", e->offset="<<e->getOffset()<<", e->size="<<e->getSize()<<", parent->uid="<<getUID()<<endl)
		ssfnet_throw_exception(SSFNetException::other_exception,"there is something wrong with the UID/OFFSET calculations!");
	}
	part->releaseStructuralModifcationLock();
	//std::cout<<"Add child to "<<getUName()<<"["<<getUID()<<"] child="<<e->getName()<<"["<<e->getUID()<<"]"<<endl;
	part->addContext(e);
}

BaseEntity* BaseEntity::getChildByName(SSFNET_STRING& name) {
	//XXX could more efficient if we stored a map of children by name
	// we need to see how often this is called to see if wasting that memory
	// is warranted.
	//LOG_DEBUG(getUName()<<"->getChildByName("<<name<<")"<<endl)
	ChildIterator<BaseEntity*> kids=getAllChildren();
	BaseEntity* rv;
	while(kids.hasMoreElements()) {
		rv=kids.nextElement();
		//LOG_DEBUG("\tchild="<<rv->getName()<<endl);
		if (0 == name.compare(rv->getName()->c_str())) {
			return rv;
		}
	}
	return NULL;
}

BaseEntity* BaseEntity::deference() {
	return this;
}

/*
ConfigVarMap* BaseEntity::getPropertyMap() {
	return propmap;
}
ConfigVarMap* BaseEntity::getStateMap() {
	return statemap;
}

*/
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseEntity& e) {
	return os<<"("<<e.getTypeName()<<")"<<e.getUName();
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseEntity* e) {
	if(e)
		return os<<*e;
	return os<<"NULL";
}


BaseEntity::__prop__::__prop__(){
	BaseEntityMember* args[] = { BaseConfigVar::start_arg, &size,
			BaseConfigVar::end_arg };
	this->__setup__(args);
}

BaseEntity::__prop__::~__prop__() {
}

const ConfigVarType * BaseEntity::__prop__::__static__size = new ConfigVarType(
		"size", ConfigVarType::INT, 1, 1, "0",
		"the # of children of this node +1 for this node.",
		getClassConfigType());

const ConfigVarType* BaseEntity::__prop__::__static__get_size() {
	return __static__size;
}

BaseEntity::__state__::__state__() {
	BaseEntityMember* args[] = { BaseConfigVar::start_arg, &name, &offset,
			BaseConfigVar::end_arg };
	this->__setup__(args);
}

BaseEntity::__state__::~__state__() {
}

const ConfigVarType* BaseEntity::__state__::__static__name = new ConfigVarType(
		"name", ConfigVarType::STRING, 1, 0, "[no name]",
		"the name of this node.", getClassConfigType());

const ConfigVarType
		* BaseEntity::__state__::__static__offset =
				new ConfigVarType(
						"offset",
						ConfigVarType::INT,
						1,
						0,
						"0",
						"the offset+size of your previous sibling. If there are no previous sibling this is 0.",
						getClassConfigType());

const ConfigVarType* BaseEntity::__state__::__static__get_name() {
	return __static__name;
}

const ConfigVarType* BaseEntity::__state__::__static__get_offset() {
	return __static__offset;
}

long BaseEntity::getMemUsage_object_properties() {
	return getPropertyMap()->getMemUsage_object()+shared.offsets.size()*sizeof(OffsetTypePair);
}

long BaseEntity::getMemUsage_object_states() {
	return getStateMap()->getMemUsage_object();
}

long BaseEntity::getMemUsage_object(bool count_shared_objs) {
	return sizeof(*this)+(count_shared_objs?getMemUsage_object_properties():0)+getMemUsage_object_states();
}

long BaseEntity::getMemUsage_class() {
	return sizeof(*this)+getPropertyMap()->getMemUsage_class()+getStateMap()->getMemUsage_class();
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const UName* x) {
	if(x)
		return os << *x;
	return os <<"[NULL(UName)]";
}
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const UName& x) {
	SSFNET_LIST(SSFNET_STRING*) l;
	SSFNET_LIST(SSFNET_STRING*)::iterator it;
	for(BaseEntity* i=x.node;NULL!=i; i=i->getParent()) {
		//if(i->getName().size()==0)
		//	LOG_WARN("Node with uid="<<i->getUID()<<" has no name!"<<endl);
		l.push_front(i->getName());
	}
	bool first=true;
	for(it=l.begin();it!=l.end();it++) {
		if(!first) os<<':';
		os<<*(*it);
		first=false;
	}
	return os;
}

UName::UName(BaseEntity* _node): node(_node) {
}

UName::UName(const UName &o) {
	this->node=o.node;
}

UName::~UName() {

}

bool UName::operator==(const UName &o) const {
	BaseEntity* i=node;
	BaseEntity* j=o.node;
	while(NULL!=i && NULL!=j) {
		if(i->getName()->compare(*j->getName())!=0) {
			return false;
		}
		i=i->getParent();
		j=j->getParent();
	}
	return NULL==i && NULL==j;
}

SSFNET_STRING* UName::operator[](const int idx) {
	BaseEntity* cur=node;
	for(int i=0;i<idx && NULL!=cur; i++) {
		cur=cur->getParent();
	}
	if(NULL==cur) {
		return NULL;
	}
	return cur->getName();
}

SSFNET_STRING* UName::toString() {
	PRIME_STRING_STREAM ss;
	ss << this;
	return new SSFNET_STRING(ss.str().c_str());
}

bool compare_uid(BaseEntity* first, BaseEntity* second)
{
	return first->getUID()<second->getUID();
}

bool OffsetTypePair::is_subtype(ConfigChildType* type, BaseEntity* c) {
	if(!type)return true;
	if (type->isAliased()) {
		return type->getConfigType()->isSubtype(c->deference()->getConfigType());
	}
	return type->getConfigType()->isSubtype(c->getConfigType());
}

bool OffsetTypePair::is_subtype(ConfigChildType* type, int type_id) {
	if(!type)return true;
	if (type->isAliased()) {
		return true; //we are not sure if this is a sub-type
	}
	if(PlaceHolder::getClassConfigType()->type_id == type_id)return true;
	return type->getConfigType()->isSubtype(type_id);
}

BaseEntity* OffsetTypePair::deference(BaseEntity* c) {
	if(c) {
		/*LOG_DEBUG("deference, c="<<c->getUName()<<endl)
		if(c->deference()) {
			LOG_DEBUG("\t-->"<<c->deference()->getUName()<<endl)
		}
		else {
			LOG_WARN("\t-->NULL!")
		}*/
		return c->deference();
	}
	//LOG_WARN("deference, c was null!"<<endl)
	return c;
}

Partition* OffsetTypePair::getPartitionInstance() {
	return Partition::getInstance();
}

BaseEntity* OffsetTypePair::getChild(Partition* part, BaseEntity* parent, uint offset, bool autoAddChild) {
	OffsetTypePair::Vector& offsets=SSFNET_DYNAMIC_CAST(BaseEntity::__prop__*,parent->getPropertyMap())->offsets;
	//LOG_DEBUG("GetChild()::  Parent uid="<<parent->getUID()<<", name="<<parent->getUName()<<"; child idx="<<offset<<", #kids="<<offsets.size()<<endl)
	//std::cout<<"GetChild()::  Parent uid="<<parent->getUID()<<", name="<<parent->getUName()<<"; child idx="<<offset<<", #kids="<<offsets.size()<<endl;
	UID_t uid=parent->getUID()-parent->getSize()+offsets.at(offset).id_offset;
	//LOG_DEBUG("\tuid="<<uid<<",parent_uid="<<parent->getUID()<<", parent_size="<<parent->getSize()<<", offsets.at("<<offset<<").id_offset="<<offsets.at(offset).id_offset<<endl);
	//std::cout<<"\tuid="<<uid<<",parent_uid="<<parent->getUID()<<", parent_size="<<parent->getSize()<<", offsets.at("<<offset<<").id_offset="<<offsets.at(offset).id_offset<<endl;
	Context* context = part->lookupContext(uid, false);
	BaseEntity* rv = context?context->getObj():NULL;
	if(!rv && autoAddChild) {
		LOG_DEBUG("Auto adding the child, uid="<<uid<<endl);
		//this node must have replicas and either the base replicate or one of the replicas have added a this node
		//so we should add it here as well
		//XXX if we knew the original we could copy_init, but how to find it?
		rv = ConfigurableFactory::createInstance<BaseEntity*>(offsets.at(offset).type);
		rv->setParent(parent);
		rv->propmap->size.read()=1;
		if(!offset) {
			rv->statemap->offset.read()=0;
		}
		else {
			BaseEntity* prev = getChild(part, parent, offset-1);
			rv->statemap->offset.read()=prev->getOffset()+prev->getSize();
		}
		rv->setUID(uid);
		rv->getName()->clear();
		rv->getName()->append(rv->getTypeName());
		rv->getName()->append("_");
		char temp[100];
		sprintf(temp,"%llu",rv->getUID());
		rv->getName()->append(temp);
		LOG_DEBUG("Added child "<<rv->getUName()<<" to "<<parent->getUName()<<" with an auto name"<<endl);
		part->addContext(rv);
	}
	//LOG_DEBUG("\trv="<<part->lookupContext(uid)->getObj()->getUName()<<endl);
	return rv;
}

bool OffsetTypePair::checkIfPlaceHolder(BaseEntity* e) {
	if(!e) return false;
	return PlaceHolder::getClassConfigType()->isSubtype(e->getTypeId());
}

} // namespace ssfnet
} // namespace prime
