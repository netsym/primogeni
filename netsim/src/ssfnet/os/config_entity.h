/**
 * \file config_entity.h
 * \brief Header file for the BaseEntity (and ConfigurableEntity) class.
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

#ifndef __CONFIG_ENTITY_H__
#define __CONFIG_ENTITY_H__

#include "os/ssfnet_types.h"
#include "os/config_type.h"
#include "os/config_factory.h"
namespace prime {
namespace ssfnet {

/*
 * ###################################################
 * ###################################################
 *  How to use the ConfigurableEntity Template and
 *            associated property & state macros
 * ###################################################
 * ###################################################
 *
 * Below are some details following a breif example:
 * (need to define property and state variables)
 * (need to make a difference between configurable and non-configurable variables)
 *
 * class A : public ConfigurableEntity<A, ST> {
 *   A() { ... } // a default constructor is required
 *   virtual ~A() { ... } // a *virtual* destructor is also required
 *   SSFNET_PROPMAP_DECL( // this is where property variables are declared
 *     SSFNET_CONFIG_PROPERTY_DECL(type, name); // one at a time
 *     ...
 *   );
 *   SSFNET_STATEMAP_DECL( // this is where state variables are declared
 *     SSFNET_CONFIG_STATE_DECL(type, name); // one at a time
 *     ...
 *   );
 *

 *
 * ###################################################
 * Details:
 * ###################################################
 *  1) If Have your class extend the template
 *        ConfigurableEntity<typename CT, typename ST, int>
 *     -- CT will the type of your class
 *     -- If your class is extending another class
 *        which derives from ConfigurableEntity<>, then
 *        ST will be that class. You can only
 *        extend __1__ ConfigurableEntity<> class at a time.
 *     -- Otherwise you can omit ST because
 *        defaults to BaseEntity - the base of all
 *        configurable types.
 *     -- The last integer is optional; if present, it must be a unique positive integer (greater than zero) to identify the type of this entity. This type id can be used to create instances of this entity type via ConfigurableEntityFactory.


 * ***************************************************
 *  2) The entity must have a default constructor.
 *     And must only be constructed using the
 *     ConfigurableFactory method:
 *         ConfigurableFactory::createInstance()
 *    The constructor should also be protected;
 *    however, this is not enforced.
 * ***************************************************
 *  3) Within the class you must specify three things:
 *     i) The properties of the class. This is done
 *        using the SSFNET_PROPMAP_DECL macro.
 *    ii) The states of the class. This is done
 *        using the SSFNET_STATEMAP_DECL macro.
 *   iii) The function used to 'setup' an instance of
 *        the class. This is used by the factory to
 *        initialize the instance class. This is done
 *        using the SSFNET_ENTITY_SETUP macro.
 *
 *     -- SSFNET_PROPMAP_DECL(...) takes a list of
 *       property declaration separated by
 *       semicolons. A property is declared using the
 *                  SSFNET_CONFIG_PROPERTY_DECL macro.
 *
 *     -- SSFNET_CONFIG_PROPERTY_DECL(TYPE,NAME) takes
 *       a type and a variable name. The type can be
 *       any valid c++ type which does not derive from
 *       another ConfigurableEntity<>
 *
 *
 *     -- SSFNET_STATEMAP_DECL(...) takes a list of
 *       state declarations separated by semicolons.
 *       A sate is declared using the
 *                  SSFNET_CONFIG_STATE_DECL macro.
 *
 *     -- SSFNET_CONFIG_STATE_DECL(TYPE,NAME) takes
 *       a type and a variable name. The type can be
 *       any valid c++ type which does not derive from
 *       another ConfigurableEntity<>
 *
 *     -- SSFNET_ENTITY_SETUP(TYPE, SUPER_TYPE)
 *      take the name of your class and the super type
 *      it is derived from. Unfortunately you must
 *      specify BaseEntity unlike the template decl.
 *
 * ***************************************************
 *  4) Where you place your class implementation
 *     (i.e., the .cc source file), you must:
 *     i) Register the configurable entity with the
 *        the ConfigurableFactory using the
 *        SSFNET_REGISTER_ENTITY macro.
 *    ii) You must define each property in the
 *        property map using the
 *        SSFNET_CONFIG_PROPERTY_DEF macro.
 *   iii) You must define each state in the state
 *        map using the SSFNET_CONFIG_STATE_DEF
 *        macro.
 *
 *     -- SSFNET_REGISTER_ENTITY XXX TODO
 *     -- SSFNET_CONFIG_PROPERTY_DEF XXX TODO
 *     -- SSFNET_CONFIG_STATE_DEF XXX TODO
 *
 *     See the macros (below) for details on them
 * ***************************************************
 *  5) The class must not contain any pure-virtual
 *     functions because the ConfigurableFactory will
 *     need to be able to instantiate this class if
 *     user requests it.
 *
 *     To force derived types to implement methods
 *     the typical approach is to throw an exception
 *     indicating the derived class must provide an
 *     implementation.
 *
 * ###################################################
 * Example:
 * ###################################################
 XXX TODO
 For example, if class Foo was going to be a
 configurable then class decl would look like:
 class Foo : public ConfigurableEntity<Foo>
 { ... }
 And if Foo extended Bar, the class decl
 would look like:
 class Foo : public ConfigurableEntity<Foo, Bar>
 { ... }


 */

#define unshared ((__state__&)*statemap)
#define shared ((__prop__&)*propmap)

//Declare (for h files)
#define SSFNET_CONFIG_PROPERTY_DECL(type,name)\
  static const ConfigVarType* __static__##name; \
  static const ConfigVarType* __static__get_##name() { return __static__##name; }; \
  ConfigProperty<type,&__static__get_##name> name;

#define SSFNET_CONFIG_STATE_DECL(type,name)\
  static const ConfigVarType* __static__##name; \
  static const ConfigVarType* __static__get_##name() { return __static__##name; }; \
  ConfigState<type,&__static__get_##name> name;

#define SSFNET_CONFIG_CHILDREN_DECL_SHARED(type,name)\
  static const ConfigChildType* __static__##name; \
  static const ConfigChildType* __static__get_##name() { return __static__##name; };

#define SSFNET_CONFIG_CHILDREN_DECL_UNSHARED(cls,type,name)\
ChildList<type,&cls::__prop__::__static__get_##name> name;

#define SSFNET_CONFIG_CHILDREN_DECL(type,name)\
  ChildIterator<type*> name (); int name##Size();\

#define SSFNET_PROPMAP_DECL(props) \
    class __prop__ : public __super_type__::__prop__ {\
		public: __prop__(); virtual ~__prop__(){}\
		virtual long getMemUsage_object();\
		virtual long getMemUsage_class();\
		props };\
	virtual ConfigVarMap* propertyMapInstance(){ return SSFNET_DYNAMIC_CAST(ConfigVarMap*,new __prop__());};

#define SSFNET_STATEMAP_DECL(states) \
	class __state__: public __super_type__::__state__ {\
		public: __state__(); virtual ~__state__(){}\
		virtual long getMemUsage_object();\
		virtual long getMemUsage_class();\
		states };\
	virtual ConfigVarMap* stateMapInstance(){ return SSFNET_DYNAMIC_CAST(ConfigVarMap*,new __state__());};

#define SSFNET_ENTITY_SETUP(...) virtual void setup() {\
		__super_type__::setup();\
		BaseEntityMember* args[] = {BaseConfigVar::start_arg, ##__VA_ARGS__,BaseConfigVar::end_arg};\
		this->__setup__(args);}

//Definitions (for cc files)
#define SSFNET_REGISTER_ENTITY(entity_type,entity_type_tpl,entity_super_type,entity_super_type_tpl,impl_id,alias_id,replica_id,alias_replica_id,...)\
	static BaseEntity* __static_make_##entity_type() { return new entity_type##entity_type_tpl();}\
	template<> ConfigType* ConfigurableEntity< entity_type##entity_type_tpl, entity_super_type##entity_super_type_tpl, entity_type::__type_id__ >::__config__ = \
	ConfigurableFactory::registerConfigType(new ConfigType( #entity_type, #entity_super_type ,\
		&__static_make_##entity_type, entity_type::__type_id__, impl_id, alias_id, replica_id, alias_replica_id),##__VA_ARGS__);\
	template<> ConfigType::StrMap* ConfigurableEntity< entity_type##entity_type_tpl, entity_super_type##entity_super_type_tpl, entity_type::__type_id__ >::__derived_types__=NULL;\
    template<> ConfigType* ConfigurableEntity< entity_type##entity_type_tpl, entity_super_type##entity_super_type_tpl, entity_type::__type_id__ >::getClassConfigType() {\
		return ConfigurableEntity< entity_type##entity_type_tpl, entity_super_type##entity_super_type_tpl, entity_type::__type_id__ >::__config__; }

#define SSFNET_PROPMAP_DEF(entity_type,...) \
	entity_type::__prop__::__prop__(): __super_type__::__prop__::__prop__(){\
		BaseEntityMember* args[] = {BaseConfigVar::start_arg,##__VA_ARGS__,BaseConfigVar::end_arg};\
		this->__setup__(args);}\
	long entity_type::__prop__::getMemUsage_object() { return sizeof(entity_type::__prop__)+__get_size__(false);}\
	long entity_type::__prop__::getMemUsage_class() { return sizeof(entity_type::__prop__)+__get_size__(true);}

#define SSFNET_STATEMAP_DEF(entity_type,...) \
	entity_type::__state__::__state__(): __super_type__::__state__::__state__() {\
		BaseEntityMember* args[] = {BaseConfigVar::start_arg,##__VA_ARGS__,BaseConfigVar::end_arg};\
		this->__setup__(args);}\
	long entity_type::__state__::getMemUsage_object() { return sizeof(entity_type::__state__)+__get_size__(false);}\
	long entity_type::__state__::getMemUsage_class() { return sizeof(entity_type::__state__)+__get_size__(true);}

#define SSFNET_CONFIG_VAR_DEF(entity_type,var_type,var_name,required,readonly,default_value,comment) \
	const ConfigVarType* entity_type::__static__##var_name = \
		new ConfigVarType(#var_name,ConfigVarType::var_type,required,\
			readonly,default_value,comment,getClassConfigType());

#define SSFNET_CONFIG_PROPERTY_DEF(entity_type,var_type,var_name,required,default_value,comment) \
		const ConfigVarType* entity_type::__prop__::__static__##var_name = \
			new ConfigVarType(#var_name,ConfigVarType::var_type,required,\
				true,default_value,comment,getClassConfigType());

#define SSFNET_CONFIG_STATE_DEF(entity_type,var_type,var_name,required,default_value,comment) \
		const ConfigVarType* entity_type::__state__::__static__##var_name = \
			new ConfigVarType(#var_name,ConfigVarType::var_type,required,\
				false,default_value,comment,getClassConfigType());

#define SSFNET_CONFIG_CHILDREN_DEF(entity_type,list_type,var_name,is_aliased,min,max,comment) \
	const ConfigChildType* entity_type::__prop__::__static__##var_name = \
		new ConfigChildType(#var_name, #list_type,is_aliased,min,max,comment,getClassConfigType());\
  ChildIterator<list_type*> entity_type::var_name(){return unshared.var_name.enumerate(this,shared.offsets);}\
  int entity_type::var_name##Size(){return unshared.var_name.size(this,shared.offsets);}


class ModelBuilder;
class ConfigurableFactory;
class UName;
namespace model_builder {
class CommunityWrapper;
class ModelNode;
}

/**
 * Used by the child lists to store the offsets of children (and the children types)
 */
class OffsetTypePair {
public:
	typedef SSFNET_VECTOR(OffsetTypePair) Vector;
	OffsetTypePair(UID_t _offset, int _type): id_offset(_offset), type(_type) { }
	OffsetTypePair(OffsetTypePair& o): id_offset(o.id_offset), type(o.type) {}
	OffsetTypePair(const OffsetTypePair& o): id_offset(o.id_offset), type(o.type) {}
	UID_t id_offset;
	int type;

	/**
	 * find the uid of the parent child at the offset and use part to find the child
	 */
	static BaseEntity* getChild(Partition* part, BaseEntity* parent, uint offset, bool autoAddChild=true);

	/**
	 * deference the baseentity c
	 */
	static BaseEntity* deference(BaseEntity* c);


	/**
	 * get the partition instance
	 */
	static Partition* getPartitionInstance();


	/**
	 * return whether c is a subtype of type
	 */
	static bool is_subtype(ConfigChildType* type, BaseEntity* c);

	/**
	 * return whether the type indicated by type_id is a subtype of type
	 */
	static bool is_subtype(ConfigChildType* type, int type_id);

	/**
	 * return whether entity is a place holder
	 */
	static bool checkIfPlaceHolder(BaseEntity* e);
};


/**
 * Used to store sub child lists
 */
template<typename CHILD_CLASS>
class ChildIterator: public Enumeration<CHILD_CLASS> {
private:
	Partition* part;
	ConfigChildType* type;
	OffsetTypePair::Vector& offsets;
	BaseEntity* owner;
	int max, cur;
public:
	ChildIterator(Partition* part_, ConfigChildType* _type, OffsetTypePair::Vector& _offsets, BaseEntity* _owner, int _max, int _min) :
		part(part_), type(_type),offsets(_offsets),owner(_owner),max(_max),cur(_min) {
		assert(max<(int)offsets.size() && cur>=0);
		//assert(max<(int)offsets.size() && cur>=0 && (max<0 || cur<=max));
	}
	ChildIterator(ChildIterator<CHILD_CLASS>& p) :
		part(p.part), type(p.type),offsets(p.offsets),owner(p.owner),max(p.max),cur(p.cur) {
		assert(max<(int)offsets.size() && cur>=0);
		//assert(max<(int)offsets.size() && cur>=0 && (max<0 || cur<=max));
	}
	ChildIterator(const ChildIterator<CHILD_CLASS>& p) :
		part(p.part), type(p.type),offsets(p.offsets),owner(p.owner),max(p.max),cur(p.cur) {
		assert(max<(int)offsets.size() && cur>=0);
		//assert(max<(int)offsets.size() && cur>=0 && (max<0 || cur<=max));
	}

	bool hasMoreElements() {
		//printf("hasMoreElements, cur=%i, max=%i-->%s\n", cur, max,(cur<=max)?"true":"false");
		return cur<=max;
	}
	CHILD_CLASS nextElement() {
		//printf("here1 is_aliased=%s\n",(type)?type->isAliased()?"true":"false":"false");fflush(stdout);
		if(cur<=max) {
			//printf("here 2 cur=%i, max=%i\n", cur, max);fflush(stdout);
			BaseEntity* rv;
			//printf("here2a, rv=%p\n",OffsetTypePair::getChild(part,owner,cur));fflush(stdout);
			if(type&&type->isAliased()) {
				rv=OffsetTypePair::deference(OffsetTypePair::getChild(part,owner,cur,false));
				//rv=SSFNET_STATIC_CAST(CHILD_CLASS,OffsetTypePair::deference(OffsetTypePair::getChild(part,owner,cur)));
				//printf("here2b, rv=%p\n",rv);fflush(stdout);
			}
			else{
				rv=OffsetTypePair::getChild(part,owner,cur,false);
				//printf("here2c, rv=%p\n",rv);fflush(stdout);
			}
			for (cur=cur+1;cur<=max;cur++) {
				if (OffsetTypePair::is_subtype(type, offsets.at(cur).type)) {
					//found next
					break;
				}
			}
			if(type) {
				if(type->isAliased()) {
					//we need to make sure the child is of the correct type, since we expect the children to be aliased, we need to deference
					while(!type->getConfigType()->isSubtype(rv,true)) {
						//printf("here3, rv=%p\n",rv);fflush(stdout);
						rv=nextElement(); //skip it
					}
				}
				else {
					//we need to make sure the child is of the correct type
					while(!type->getConfigType()->isSubtype(rv)) {
						//printf("here3, rv=%p\n",rv);fflush(stdout);
						rv=nextElement(); //skip it
					}
				}
				while(OffsetTypePair::checkIfPlaceHolder(rv)) {
					rv=nextElement(); //skip it
				}
			}
			//printf("here4, rv=%p\n",rv);fflush(stdout);
			return SSFNET_STATIC_CAST(CHILD_CLASS,rv);
		}
		//printf("here5\n");fflush(stdout);
		return 0;
	}
};

/**
 * Used to store sub child lists
 */
template<typename CHILD_CLASS, const ConfigChildType* getType(void)>
class ChildList : public BaseEntityMember {
private:
	friend class BaseEntity;
	int cur_len,min, max;
	uint count;
public:
	typedef ChildIterator<CHILD_CLASS*> iterator;

	/** The constructor. */
	ChildList(): cur_len(-1),min(0),max(-1),count(0) {
	}

	/** The destructor. */
	~ChildList() {}

	//XXX -- debug
	/**
	 * Calculate the min/max indexes within the offset vector for this type of child
	 */
	void updateMinMax(Partition* part, BaseEntity* owner, OffsetTypePair::Vector& offsets) {
		ConfigChildType* ct = getChildType();
		min=offsets.size();
		max=-1;
		count=0;
		for(int i=0;i<(int)offsets.size();i++) {
			BaseEntity* kid=NULL;
			if(ct&&ct->isAliased()) {
				kid=OffsetTypePair::deference(OffsetTypePair::getChild(part,owner,i,false));
			}
			else{
				kid=OffsetTypePair::getChild(part,owner,i,false	);
			}
			if(ct) {
				if(kid && ct->getConfigType()->isSubtype(kid) && !OffsetTypePair::checkIfPlaceHolder(kid)) {
					if(min>i)min=i;
					if(max<i)max=i;
					count++;
				}
			}
			else {
				if(kid && !OffsetTypePair::checkIfPlaceHolder(kid)) {
					if(min>i)min=i;
					if(max<i)max=i;
					count++;
				}
			}
		}
		cur_len=offsets.size();
	}

	/**
	 * enumerate over all child nodes which are a subtype of getChildType()
	 */
	iterator enumerate(BaseEntity* owner, OffsetTypePair::Vector& offsets) {
		assert(owner);
		Partition* part = OffsetTypePair::getPartitionInstance();
		if(cur_len != (int)offsets.size()) {
			//there have been new children added
			updateMinMax(part,owner,offsets);
		}
		//printf("Get iterator, offsets.size()=%i, count=%i, min=%i, max=%i, type=%p\n",(int)offsets.size(),count,min,max,getChildType());
		return iterator(part,getChildType(), offsets, owner, max, min);
	}

	/**
	 * return the size of the list
	 */
	int size(BaseEntity* owner, OffsetTypePair::Vector& offsets) {
		if(cur_len != (int)offsets.size()) {
			//there have been new children added
			updateMinMax(OffsetTypePair::getPartitionInstance(),owner,offsets);
		}
		return count;
	}

	BaseEntityMember::Type getArgType() { return BaseEntityMember::CHILD_TYPE;};

	/**
	 *  return the ConfigChildType for this list.
	 */
	ConfigChildType* getChildType(void) {return const_cast<ConfigChildType*>(getType());}
};

class StateLogger;
class Monitor;

/**
 * \brief The base of all model entities.
 *
 * XXX add some text about the structure/property/state sharing
 */
class BaseEntity {
	friend int get_entity_config_type(BaseEntity* c);
	friend class Monitor;
	friend class StateLogger;
	friend class ModelBuilder;
	friend class model_builder::CommunityWrapper;
	friend class model_builder::ModelNode;
	friend class ConfigurableFactory;
	friend class ConfigType;
	friend class OffsetTypePair;

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, ConfigType& c);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseEntity& e);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, BaseEntity* e);
public:
	typedef SSFNET_LIST(BaseEntity*) List;
	typedef SSFNET_VECTOR(BaseEntity*) Vector;
	typedef SSFNET_MAP(SSFNET_STRING&,BaseEntity*) StrMap;
	typedef SSFNET_MAP(UID_t,BaseEntity*) UIDMap;
	typedef SSFNET_MAP(BaseEntity*,UID_t) RankMap;

	/**
	 * The init method is used to initialize the entity once it
	 * has been configured. The init method will call init methods
	 * of all children of this entity. <b>All derived classes that
	 * override this method must explicitly call this method in
	 * class which they derive from.</b>
	 */
	virtual void init() {}

	/**
	 * The wrapup method is called at the end of the simulation.
	 * The wrapup method will call wrapup methods of all children
	 * of this entity. <b>All derived classes that override this
	 * method must explicitly call this method in class which they
	 * derive from.</b>
	 */
	virtual void wrapup(){}

	/**
	 * Get the UID (unique identifier) of this entity
	 */
	UID_t getUID() const;

	/**
	 * Set the UID (unique identifier) of this entity
	 */
	void setUID(UID_t id);

	/**
	 * Get the rank (which is the relative identifier) of the
	 * entity in terms of the anchor
	 */
	virtual UID_t getRank(BaseEntity* anchor);

	/**
	 * Get the starting rank of this entity and its offspring in
	 * terms of the anchor
	 */
	virtual UID_t getMinRank(BaseEntity* anchor);

	/**
	 * Get the starting UID of this entity and its offspring
	 */
	virtual UID_t getMinUID();

	/**
	 * Return true of this entity (or one of its children) has the
	 * rank r in terms of the anchor.  Otherwise false is
	 * returned.
	 */
	//virtual bool containsRank(UID_t r, BaseEntity* anchor);

	/**
	 * Return true of this entity (or one of its children) has the
	 * UID uid.  Otherwise false is returned.
	 */
	bool containsUID(UID_t uid);

	/**
	 * Get the name of this entity
	 */
	SSFNET_STRING* getName();

	/**
	 * Get the unique (full qualified) name for this entity
	 */
	const UName getUName();

	/**
	 * Initialize the property/state identified by attr_id using
	 * the string val.  If an eror occurs true is returned;
	 * otherwise false is returned
	 */
	bool initVar(uint32_t attr_id, SSFNET_STRING& val);

	/**
	 * Initialize the property/state identified by the string name
	 * using the string val.  If an eror occurs true is returned;
	 * otherwise false is returned
	 */
	bool initVar(SSFNET_STRING& name, SSFNET_STRING& val);

	/**
	 * Initialize the property/state identified by the character
	 * string name using the character string val. If an eror
	 * occurs true is returned; otherwise false is returned
	 */
	bool initVar(const char* name, const char* val);

	/**
	 * Read the property/state identified by attr_id into the
	 * string dst. If an eror occurs true is returned; otherwise
	 * false is returned
	 */
	bool readVar(uint32_t attr_id, SSFNET_STRING& dst);

	/**
	 * Read the property/state identified by the the string name
	 * into the string dst. If an eror occurs true is returned;
	 * otherwise false is returned
	 */
	bool readVar(SSFNET_STRING& name, SSFNET_STRING& dst);

	/**
	 * Read the property/state identified by the character string
	 * name into the string dst. If an eror occurs true is
	 * returned; otherwise false is returned
	 */
	bool readVar(const char* name, SSFNET_STRING& dst);

	/**
	 * Convert the value val into the correct type, then set the
	 * property/state identified by attr_id.  If an eror occurs
	 * true is returned; otherwise false is returned
	 */
	bool writeVar(uint32_t attr_id, SSFNET_STRING& val);

	/**
	 * Convert the value val into the correct type, then set the
	 * property/state identified by the string name.  If an eror
	 * occurs true is returned; otherwise false is returned
	 */
	bool writeVar(SSFNET_STRING& name, SSFNET_STRING& val);

	/**
	 * Convert the value val into the correct type, then set the
	 * property/state identified by the character string name.  If
	 * an eror occurs true is returned; otherwise false is
	 * returned
	 */
	bool writeVar(const char* name, const char* val);

	/**
	 * Get the property map for this entity
	 */
	inline ConfigVarMap* getPropertyMap() const { return propmap; };

	/**
	 * Get the property map for this entity
	 */
	inline ConfigVarMap* getStateMap()  const { return statemap; };

	/**
	 * Initialize the property map
	 *
	 * XXX initPropertyMap(ConfigVarMap* map=NULL)
	 */
	virtual void initPropertyMap(ConfigVarMap* map = NULL);

	/**
	 * Initialize the state map
	 *
	 * XXX initStateMap()
	 */
	virtual void initStateMap();

	/**
	 * Get the parent entity
	 */
	inline BaseEntity* getParent() const { return statemap->parent; }

	/**
	 * Add the child newChild to this entity. If this entity's
	 * template is shared with other entities, all entities will
	 * see a new child template. However, only this entity will
	 * have a context associated with the template
	 */
	void addChild(BaseEntity* newChild);

	/**
	 * If this entity has an (immediate) child entity with the
	 * name n it will return the child entity. Otherwise null will
	 * be returned
	 */
	virtual BaseEntity* getChildByName(SSFNET_STRING& n);

	/**
	 * Get all children of this entity via iteration
	 */
	 ChildIterator<BaseEntity*> getAllChildren();

	/**
	 * Get the UID offset of this entity
	 */
	uint64_t getOffset();

	/**
	 * Get the number of descendents of this entity (including
	 * itself)
	 */
	uint64_t getSize();

	/**
	 *  Return whether this entity is considered a routing sphere;
	 *  routing sphere is a special entity (derived from the
	 *  BaseEntity class)
	 */
	virtual bool isRoutingSphere() {
		return false;
	}

	/**
	 * If this entity is an alias (or derivation of one), this
	 * will return the entity it is aliased from.  Otherwise this
	 * entity (i.e., 'this') is returned
	 */
	virtual BaseEntity* deference();

	/**
	 * Return the name of this entity type (i.e. Host, Net,
	 * Interface, ...); this is defined by the ConfigurableEntity
	 * template class
	 */
	virtual const SSFNET_STRING& getTypeName()=0;

	/**
	 * Return an integer associated with the type of this entity;
	 * this is defined by the ConfigurableEntity template class
	 */
	virtual int getTypeId()=0;

	/**
	 * Return the ConfigType for this entity. A ConfigType
	 * describes the properties/state/structure of this entity
	 * type.  This is defined by the ConfigurableEntity template
	 * class
	 */
	virtual ConfigType* getConfigType()=0;

	/**
	 * If an entity wants to export state they should implement this function,
	 * which will be invoked by the monitor
	 */
	virtual void exportState(StateLogger* state_logger, double sampleInterval){};

	/**
	 * When an entity is in view -- separate from saving state -- we will export
	 * state to slingshot for vizualization purposes
	 */
	virtual void exportVizState(StateLogger* state_logger, double sampleInterval){};

protected:
	/** The constructor */
	BaseEntity();

	/** The destructor. */
	virtual ~BaseEntity();

	/**
	 * Set the UID (unique identifier) of this entity
	 */
	void setOffset(UID_t new_offset);

	/**
	 *  Set the parent of this entity.  It is an error to set the
	 *  parent of an entity that already has a parent
	 */
	void setParent(BaseEntity* p);

	/**
	 * Used by setup() to setup the entity. The setup() method
	 * registers all the child types and configures the sub-child
	 * list (i.e., net.hosts, host.interfaces, etc)
	 */
	void __setup__(BaseEntityMember** args);

	/**
	 * Find the variable (either in the state or property map) of
	 * the given name.  If no variable by the name is found, null
	 * is returned
	 */
	BaseConfigVar* findVar(SSFNET_STRING& name);

	/**
	 * Find the variable (either in the state or property map)
	 * with attr_id.  If no variable with attr_id is found, null
	 * is returned
	 */
	BaseConfigVar* findVar(uint32_t attr_id);

	/**
	 * Return the entity which precedes this entity in the
	 * post-order traversal from the anchor.  If this is the first
	 * entity in the post-order traversal, null is returned
	 */
	BaseEntity* predecessor(BaseEntity* anchor);

	/**
	 * Return the previous sibling to this entity.  If this entity
	 * is the first child, null is returned
	 */
	BaseEntity* previousSibling();

	/**
	 * This is overridden by the ConfigurableEntity template to
	 * call __setup__ with child type of this entity
	 */
	virtual void setup();

	/**
	 * Used by the model_builder to initialized a copy of the node.
	 *
	 * If your node special setup done during a copy it should be done here. You must also call your
	 * base clases version of this function. See Alias or RouteTable.
	 *
	 * XXX copy_init(BaseEntity* o)
	 */
	virtual void copy_init(BaseEntity* o);

	/**
	 * This is overridden by the ConfigurableEntity template to
	 * create a property map instance associated with this type of
	 * entity
	 */
	virtual ConfigVarMap* propertyMapInstance()=0;

	/**
	 * This is overridden by the ConfigurableEntity template to
	 * create a state map instance associated with this type of
	 * entity
	 */
	virtual ConfigVarMap* stateMapInstance()=0;

	/**
	 * return whether this entity is assigned a rank/uid (XXX remove this later...)
	 */
	virtual bool isAssignedRank() {
		return true;
	}

public:
	/**
	 * Return the ConfigType for this entity type. A ConfigType
	 * describes the properties/state/structure of this entity
	 * type. This is defined by the ConfigurableEntity template
	 * class
	 */
	static ConfigType* getClassConfigType();

	/**
	 * Return the id of this entity type.  This is defined by the
	 * ConfigurableEntity template class
	 */
	static int getClassConfigTypeId();

	/**
	 * The base property map for all entities
	 */
	class __prop__: public ConfigVarMap {
	public:
		__prop__();
		virtual ~__prop__();

		/* The number of children under this entity +1 (for this entity) */
		static const ConfigVarType* __static__size;
		static const ConfigVarType* __static__get_size();
		ConfigProperty<UID_t, &__static__get_size> size;

		/** The children */
		OffsetTypePair::Vector offsets;
	};

	/**
	 * The base state map for all entities
	 */
	class __state__: public ConfigVarMap {
	public:
		__state__();
		virtual ~__state__();

		virtual bool hasNameOverride() { return false; }

		/** the offset+size of your previous sibling. If there are no previous sibling this is 0 **/
		static const ConfigVarType* __static__offset;
		static const ConfigVarType* __static__get_offset();
		ConfigProperty<UID_t, &__static__get_offset> offset;

		/** the name of this entity **/
		static const ConfigVarType* __static__name;
		static const ConfigVarType* __static__get_name();
		ConfigProperty<SSFNET_STRING, &__static__get_name> name;

		/** The parent template */
		BaseEntity* parent;

		/** the uid */
		UID_t uid;
	};

protected:
	/** The properties*/
	__prop__* propmap;

	/** The states*/
	__state__* statemap;

	/** The configtype for this entity */
	static ConfigType* __config__;

public:
	/**
	 * calculate the amount of memory used by this entity
	 *
	 */
	virtual long getMemUsage_object(bool count_shared_objs);

	/**
	 * calculate the amount of memory used by class
	 */
	virtual long getMemUsage_class();

	/**
	 * calculate the amount of memory used by this entity' properties
	 *
	 */
	virtual long getMemUsage_object_properties();

	/**
	 * calculate the amount of memory used by this entity' states
	 */
	virtual long getMemUsage_object_states();

};

/**
 * \brief A ConfigurableEntity is a template that is used to automatically
 * extend the parent BaseEntity and BaseEntity for a derived type.
 */
template<typename T, typename ST = BaseEntity, int type_id = 0>
class ConfigurableEntity: public ST {
public:
	typedef ST __super_type__;

	/** The constructor. */
	ConfigurableEntity() :
		ST() {
	}

	/** The destructor. */
	virtual ~ConfigurableEntity() {
	}

	/**
	 * This is overridden by the ConfigurableEntity template to create a
	 * property map instance associated with this type of entity
	 *
	 * This function will be defined in the ST class via a macro
	 */
	virtual ConfigVarMap* propertyMapInstance()=0;

	/**
	 * This is overridden by the ConfigurableEntity template to create a
	 * state map instance associated with this type of entity
	 *
	 * This function will be defined in the ST class via a macro
	 */
	virtual ConfigVarMap* stateMapInstance()=0;

	/**
	 * The init method is used to initialize the entity once it
	 * has been configured. The init method will call init methods of
	 * all children of this entity. All derived classes that override this
	 * method must explicitly call this method in class which they derive from.
	 */
	virtual void init() {
		ST::init();
	}

	/**
	 * The wrapup method is called at the end of the simulation.
	 * The wrapup  method will call wrapup  methods of
	 * all children of this entity. All derived classes that override this
	 * method must explicitly call this method in class which they derive from.
	 */
	virtual void wrapup(){
		ST::wrapup();
	}

	/**
	 * This is overridden by the ConfigurableEntity template to call __setup__ with child type of this
	 * entity
	 */
	virtual void setup() {
		ST::setup();
	}

	/**
	 * Return the integer if associated with the type of this entity.
	 */
	int getTypeId() {
		return getClassConfigType()->type_id;
	}

	/**
	 * Return the name of this entity type (i.e. Host, Net, Interface, ...)
	 */
	const SSFNET_STRING& getTypeName() {
		return getClassConfigType()->getName();
	}

	/**
	 * Return the ConfigType for this entity; A ConfigType describes the properties/state/structure of entities of this type
	 */
	ConfigType* getConfigType() {
		return getClassConfigType();
	}

	/**
	 * Return the ConfigType for this entity; A ConfigType describes the properties/state/structure of entities of this type
	 */
	static ConfigType* getSuperClassConfigType() {
		return ST::getClassConfigType();
	}

	/**
	 * Get the config type of the class
	 */
	static ConfigType* getClassConfigType(); //defined by macros....

	/**
	 * the config type id of the class
	 */
	static const int __type_id__ = type_id;

private:

	/** the config type of this class  */
	static ConfigType* __config__;

	/** types that derive this type */
	static ConfigType::StrMap* __derived_types__;

};

class UName {
public:
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const UName* x);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const UName& x);
	UName(BaseEntity* _node);
	UName(const UName &o);
	~UName();

	/** XXX  operator== **/
	bool operator==(const UName &o) const;

	/** XXX  operator[] **/
	SSFNET_STRING* operator[](const int idx);

	/** XXX  toString **/
	SSFNET_STRING* toString();

protected:
	BaseEntity* node;
};

/**
 * Used for sorting keys in a map
 */
//bool compare_uid(BaseEntity* first, BaseEntity* second);

} // namespace ssfnet
} // namespace prime

#endif /*__CONFIG_ENTITY_H__*/

