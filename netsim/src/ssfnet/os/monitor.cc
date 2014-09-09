/**
 * \file monitor.cc
 * \brief Source file for the Monitor and Aggregate classes.
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

#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include "os/logger.h"
#include "os/monitor.h"
#include "os/partition.h"
#include "os/config_vars.h"
#include "os/state_logger.h"

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(Monitor);
//#define LOG_DEBUG(X) LOG_WARN(X)
//#define LOG_DEBUG(X){PRIME_STRING_STREAM ss; ss<<"D["<<prime::ssfnet::Partition::getInstance()->getPartitionId()<<":monitor::"<<__LINE__<<"]"<<X; std::cout<<ss.str();}
//#define LOG_WARN(X){PRIME_STRING_STREAM ss; ss<<"W["<<prime::ssfnet::Partition::getInstance()->getPartitionId()<<":monitor::"<<__LINE__<<"]"<<X; std::cout<<ss.str();}
//#define DEBUG_CODE(X) {X}

MonitorTimer::MonitorTimer(Community* com): Timer(com) {
}

MonitorTimer::~MonitorTimer() {}

void MonitorTimer::add(BaseEntity* e) {
	monitored.push_back(e);
	LOG_DEBUG("\t monitor "<<e->getUName()<<endl);
}

void MonitorTimer::callback() {
	StateLogger* sl = getCommunity()->getStateLogger();
	if(sl) {
		for(BaseEntityVec::iterator i=monitored.begin(); i!=monitored.end(); i++) {
			(*i)->exportState(sl,getDelay().second());
		}
		set();
		return;
	}
	LOG_DEBUG("\tNO STATE LOGGER!"<<endl);
}

Monitor::Monitor() {
}

Monitor::~Monitor() {
	for(MonitorTimer::Com2Timer::iterator i = timers.begin(); i != timers.end();i++) {
		delete i->second;
		i->second=0;
	}
	timers.clear();
}

void Monitor::init() {
	LOG_DEBUG("start init "<<getUName()<<endl);
	VirtualTime period((int64_t)shared.period.read(),VirtualTime::MILLISECOND);
	Partition* p = Partition::getInstance();
	CompiledRID* rid = 0;
	if(shared.to_monitor.read().isCompiled()) {
		rid=shared.to_monitor.read().getCompiledRI();
	}
	if(!rid || rid->getUIDVec().size()==0) {
		LOG_DEBUG("finding nodes to monitor "<<getUName()<<endl);
		SSFNET_LIST(BaseEntity*) l;
		l.push_back(SSFNET_DYNAMIC_CAST(Net*,getParent()));
		while(l.size()>0) {
			BaseEntity* e = l.front();
			l.pop_front();
			if(e->getConfigType()->isSubtype(Net::getClassConfigType())) {
				int c=0;
				SSFNET_DYNAMIC_CAST(Net*,e)->getMonitors(&c);
				if(c==0 || getParent() == e) {
					ChildIterator<BaseEntity*> all = e->getAllChildren();
					while(all.hasMoreElements())
						l.push_back(all.nextElement());
				}
			}
			else if(e->getConfigType()->isSubtype(Host::getClassConfigType())) {
				Community* owner = SSFNET_DYNAMIC_CAST(Host*,e)->getCommunity();
				MonitorTimer::Com2Timer::iterator j = timers.find(owner);
				if(j == timers.end()) {
					MonitorTimer* t = new MonitorTimer(owner);
					t->add(e);
					timers.insert(SSFNET_MAKE_PAIR(owner,t));
				}
				else {
					j->second->add(e);
				}
			}
		}
		LOG_DEBUG("done finding nodes to monitor "<<getUName()<<endl);
	}
	else {
		UIDVec& uids = rid->getUIDVec();
		for(UIDVec::iterator i = uids.begin(); i!= uids.end(); i++) {
			LOG_DEBUG("init uid "<<*i<<endl);
			Context* c = p->lookupContext(rid->getUID(*i,getParent()),false);
			if(c) {
				LOG_DEBUG("\tinit "<<c->getObj()->getUName()<<endl);
				Community* owner = c->getCommunityId()>0?p->getCommunity(c->getCommunityId()):NULL;
				if(owner) {
					LOG_DEBUG("\t\t"<<owner->getCommunityId()<<endl);
					MonitorTimer::Com2Timer::iterator j = timers.find(owner);
					if(j == timers.end()) {
						MonitorTimer* t = new MonitorTimer(owner);
						t->add(c->getObj());
						timers.insert(SSFNET_MAKE_PAIR(owner,t));
					}
					else {
						j->second->add(c->getObj());
					}
				}
				else if(c->getObj()){
					LOG_DEBUG("\tinit "<<c->getObj()->getUName()<<endl);
					if(c->getObj()->getConfigType()->isSubtype(Aggregate::getClassConfigType())) {
						LOG_DEBUG("\tinit "<<c->getObj()->getUName()<<endl);
						Aggregate* a = SSFNET_DYNAMIC_CAST(Aggregate*, c->getObj());
						Values::Com2Values* c2v = a->getValues();
						LOG_DEBUG("\tinit "<<c2v->size()<<endl);
						for(Values::Com2Values::iterator j = c2v->begin(); j!= c2v->end(); j++) {
							Community* com = j->first;
							if(!com) {
								LOG_ERROR("wtf?\n");
							}
							LOG_DEBUG("mon agg "<<a->getUName()<<", com="<<com->getCommunityId()<<"("<<((void*)com) <<")"<<endl);
							MonitorTimer::Com2Timer::iterator j = timers.find(com);
							if(j == timers.end()) {
								MonitorTimer* t = new MonitorTimer(com);
								t->add(c->getObj());
								timers.insert(SSFNET_MAKE_PAIR(com,t));
							}
							else {
								j->second->add(c->getObj());
							}
						}
					}
					else {
						LOG_ERROR(c->getObj()->getUName()<<" was monitored but it has no owning community!\n");
					}
				}
				else {
					LOG_ERROR("Partition::lookupContext("<<rid->getUID(*i,getParent())<<") returned an invalid context!\n");
				}
			}
			else {
				LOG_DEBUG("["<<getUName()<<"]uid "<<rid->getUID(*i,getParent())<<" not in partition "<< p->getPartitionId()<<"\n");
			}
		}
	}
	for(MonitorTimer::Com2Timer::iterator i = timers.begin(); i != timers.end();i++) {
		Community* ack = i->first;
		if(!ack) {
			LOG_ERROR("wtf?");
		}
		i->second->setDelay(period);
		i->first->addMonitor(i->second);
		LOG_DEBUG("added monitor "<<getUName()<<" with period of "<<period<<endl);
	}
	LOG_DEBUG("end init "<<getUName()<<endl);
}

void Monitor::wrapup() {

}

//	typedef SSFNET_PAIR(BaseEntity*,void*) VizPair;
//	typedef SSFNET_MAP(UID_t, VizPair) VizMap;
VizMonitor::VizMonitor(Community* com): Timer(com), entities(new VizMap()) {
}

VizMonitor::~VizMonitor() {
	entities->clear();
	delete entities;
}

void VizMonitor::add(BaseEntity* e) {
	VizMap::iterator i = entities->find(e->getUID());
	if(i == entities->end()) {
		entities->insert(SSFNET_MAKE_PAIR(e->getUID(),e));
	}
	else {
		if(i->second != e) {
			LOG_ERROR("how did this happen?\n");
		}
	}
}

void VizMonitor::remove(UID_t uid) {
	entities->erase(uid);
}

void VizMonitor::printDebug() {
	PRIME_STRING_STREAM ss;
	bool f=true;
	for(VizMap::iterator i=entities->begin(); i!= entities->end(); i++) {
		if(f) {
			ss<<"[";
			f=false;
		}
		else {
			ss<<",";
		}
		ss<<"("<<i->first<<","<<i->second->getUName()<<")";
	}
	if(f) ss<<"[]";
	else ss<<"]";
	LOG_WARN("viz mon is monitoring:"<<ss.str()<<endl);
}


int VizMonitor::size() {
	return entities->size();
}

void VizMonitor::callback() {
	StateLogger* sl = getCommunity()->getStateLogger();
	if(sl) {
		for(VizMap::iterator i = entities->begin(); i!= entities->end(); i++) {
			//std::cout <<"start viz export for "<<i->second->getUName()<<endl;
			i->second->exportVizState(sl,getDelay().second());
			//std::cout <<"end viz export for "<<i->second->getUName()<<endl;
		}
		set(VirtualTime((int64)comm->getVizExportRate(),VirtualTime::MILLISECOND));
	}
}

void Values::add(BaseEntity* e, int var_id) {
	BaseConfigVar* v = e->getStateMap()->getVar(var_id);
	if(!v) {
		LOG_ERROR("what happened?\n");
	}
	LOG_DEBUG("\t agg "<<e->getUName()<<":"<<v->getVarType()->getName()<<endl);
	vars.insert(v);
}

void Values::add(Aggregate* e) {
	aggregates.insert(e);
	LOG_DEBUG("\t agg "<<e->getUName()<<endl);
}

void VizValues::add(BaseEntity* e, int var_id) {
	BaseConfigVar* v = e->getStateMap()->getVar(var_id);
	if(!v) {
		LOG_ERROR("what happened?\n");
	}
	vars.insert(v);
	entities.insert(e);
	LOG_DEBUG("\t agg "<<e->getUName()<<":"<<v->getVarType()->getName()<<endl);
}



Aggregate::Aggregate(): values(new Values::Com2Values()) {
}

Aggregate::~Aggregate() {
	delete values;
}

int Aggregate::getVarID() {
	return shared.var_id.read();
}
Values::Com2Values* Aggregate::getValues() {
	return values;
}
Values* Aggregate::newValues() {
	return new Values();
}

static void agg_init_recurse(Aggregate* agg, Values::Com2Values* values, int varid, BaseEntity* e) {
	if(Net::getClassConfigType()->isSubtype(e->getConfigType())) {
		ChildIterator<BaseEntity*> all = e->getAllChildren();
		while(all.hasMoreElements()) {
			BaseEntity* ee = all.nextElement();
			if(Link::getClassConfigType()->isSubtype(ee->getConfigType())) {
				//std::cout<<"\tlooking at "<<ee->getUName()<<endl;
				ChildIterator<BaseEntity*> nics = ee->getAllChildren();
				while(nics.hasMoreElements()) {
					BaseEntity* nic = nics.nextElement()->deference()->getParent();
					//std::cout<<"\t\tlooking at "<<nic->getUName()<<endl;
					agg_init_recurse(agg, values, varid, nic);
				}
			}
			else if(Host::getClassConfigType()->isSubtype(ee->getConfigType())) {
				agg_init_recurse(agg, values, varid, ee);
			}
		}
	}
	else if(Host::getClassConfigType()->isSubtype(e->getConfigType())) {
		if(e->getStateMap() && e->getStateMap()->getVar(varid)) {
			//std::cout<<"\tadding "<<e->getUName()<<endl;
			Community* owner = SSFNET_DYNAMIC_CAST(Host*,e)->getCommunity();
			Values::Com2Values::iterator j = values->find(owner);
			if(j == values->end()) {
				Values* t = agg->newValues();
				t->add(e,varid);
				values->insert(SSFNET_MAKE_PAIR(owner,t));
			}
			else {
				j->second->add(e,varid);
			}
		}
	}
}


void Aggregate::init() {
	LOG_DEBUG("start init "<<getUName()<<endl);
	Partition* p = Partition::getInstance();
	CompiledRID* rid = 0;
	if(shared.to_aggregate.read().isCompiled()) {
		rid=shared.to_aggregate.read().getCompiledRI();
	}
	if(!rid || rid->getUIDVec().size()==0) {
		LOG_DEBUG("finding nodes to agg "<<getUName()<<endl);
		//std::cout<<"start finding nodes to agg "<<getUName()<<" looking for varid "<<getVarID()<<endl;
		agg_init_recurse(this, values, getVarID(), SSFNET_DYNAMIC_CAST(Net*,getParent()));
		/*std::cout<<"end finding nodes to agg "<<getUName()<<" values:"<<endl;
		for(  Values::Com2Values::iterator i = values->begin(); i!=values->end(); i++) {
			std::cout<<"\tcom="<<i->first->getCommunityId()<<", size="<<i->second->vars.size()<<endl;
		}*/
		LOG_DEBUG("done finding nodes to agg "<<getUName()<<endl);
	}
	else {
		UIDVec& uids = rid->getUIDVec();
		for(UIDVec::iterator i = uids.begin(); i!= uids.end(); i++) {
			LOG_DEBUG("looking up uid "<<*i<<endl);
			Context* c = p->lookupContext(rid->getUID(*i,getParent()),false);
			if(c) {
				Community* owner = c->getCommunityId()>0?p->getCommunity(c->getCommunityId()):NULL;
				if(owner) {
					Values::Com2Values::iterator j = values->find(owner);
					if(j == values->end()) {
						Values* t = newValues();
						if(c->getObj()->getConfigType()->isSubtype(Aggregate::getClassConfigType())) {
							t->add(SSFNET_DYNAMIC_CAST(Aggregate*,c->getObj()));
						}
						else {
							t->add(c->getObj(),shared.var_id.read());
						}
						values->insert(SSFNET_MAKE_PAIR(owner,t));
					}
					else {
						if(c->getObj()->getConfigType()->isSubtype(Aggregate::getClassConfigType())) {
							j->second->add(SSFNET_DYNAMIC_CAST(Aggregate*,c->getObj()));
						}
						else {
							j->second->add(c->getObj(),shared.var_id.read());
						}
					}
					LOG_DEBUG("\t agg "<<c->getObj()->getUName()<<endl);
				}
				else if(c->getObj()){
					LOG_ERROR(c->getObj()->getUName()<<" was aggregated but it has no owning community!\n");
				}
				else {
					LOG_ERROR("Partition::lookupContext("<<rid->getUID(*i,getParent())<<") returned an invalid context!\n");
				}
				continue;
			}
			LOG_DEBUG("["<<getUName()<<"]uid "<<rid->getUID(*i,getParent())<<" not in partition "<< p->getPartitionId()<<"\n");
		}
	}
	LOG_DEBUG("finish init "<<getUName()<<endl);
}

void Aggregate::wrapup() {
}

Values* Aggregate::update(Community* com) {
	if(com) {
		Values::Com2Values::iterator j = values->find(com);
		if(j != values->end()) {
			_update(com, j->second);
			return j->second;
		}
	}
	return 0;
}

void Aggregate::_update(Community* com, Values* vals) {
	bool first=true;
	for(SSFNET_SET(BaseConfigVar*)::iterator i = vals->vars.begin(); i!= vals->vars.end(); i++) {
		if(first) {
			vals->min=(*i)->getValueAsDouble();
			vals->max=(*i)->getValueAsDouble();
			vals->sum=(*i)->getValueAsDouble();
			vals->sample_count=vals->vars.size();
			first=false;
		}
		else {
			double vv =(*i)->getValueAsDouble();
			vals->sum+=vv;
			if(vv>vals->max) {
				vals->max=vv;
			}
			if(vv<vals->min) {
				vals->min=vv;
			}
		}
	}
	first=true;
	for(SSFNET_SET(Aggregate*)::iterator i = vals->aggregates.begin(); i!= vals->aggregates.end(); i++) {
		Aggregate* a = *i;
		Values* vv = a->update(com);
		if(first) {
			if(vv) {
				vals->sum=vv->sum;
				vals->sample_count=vv->sample_count;
				vals->max=vv->max;
				vals->min=vv->min;
			}
			first=false;
		}
		else {
			if(vv) {
				vals->sum+=vv->sum;
				vals->sample_count+=vv->sample_count;
				if(vv->max>vals->max) {
					vals->max=vv->max;
				}
				if(vv->min<vals->min) {
					vals->min=vv->min;
				}
			}
		}
	}
}

void Aggregate::exportState(StateLogger* state_logger, double sampleInterval) {
	Values* vals = update(state_logger->getCommunity());
	if(vals) {
		StateUpdateEvent * evt = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT,state_logger->getCommunity()->now(),getUID(), false, true);
		evt->addVarUpdate(shared.min.getVarType()->getAttrNameId(),vals->min);
		evt->addVarUpdate(shared.max.getVarType()->getAttrNameId(),vals->max);
		evt->addVarUpdate(shared.sample_count.getVarType()->getAttrNameId(),vals->sample_count);
		evt->addVarUpdate(shared.sum.getVarType()->getAttrNameId(),vals->sum);
		state_logger->export_state(evt);
	}
}

void Aggregate::exportVizState(StateLogger* state_logger, double sampleInterval){
	Values* vals = update(state_logger->getCommunity());
	if(vals) {
		StateUpdateEvent * evt = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT,state_logger->getCommunity()->now(),getUID(), true, true);
		evt->addVarUpdate(shared.min.getVarType()->getAttrNameId(),vals->min);
		evt->addVarUpdate(shared.max.getVarType()->getAttrNameId(),vals->max);
		evt->addVarUpdate(shared.sample_count.getVarType()->getAttrNameId(),vals->sample_count);
		evt->addVarUpdate(shared.sum.getVarType()->getAttrNameId(),vals->sum);
		LOG_WARN("exporting viz state for "<<getUName()<<"("<<getUID()<<")\n");
		state_logger->export_state(evt);
	}
};

VizValues::VizValues() : Values(), last_sample_time(VirtualTime(0)) {

}

VizAggregate::VizAggregate() {}
VizAggregate::~VizAggregate() {}
Values* VizAggregate::update(Community* com) {
	if(com) {
		Values::Com2Values::iterator j = values->find(com);
		if(j != values->end()) {
			//std::cout<<"[start]export viz state for "<<getUName()<<", uid="<<getUID()<<endl;
			VizValues* vals = SSFNET_DYNAMIC_CAST(VizValues*, j->second);
			VirtualTime d = com->now() - vals->last_sample_time;
			vals->last_sample_time = com->now();
			for(SSFNET_SET(BaseEntity*)::iterator i = vals->entities.begin(); i!= vals->entities.end();i++) {
				(*i)->exportVizState(0,d.second());
			}
			_update(com, vals);
			//std::cout<<"[end]export viz state for "<<getUName()<<", uid="<<getUID()<<endl;
			return vals;
		}
	}
	return 0;
}

Values* VizAggregate::newValues() {
	return new VizValues();
}



} // namespace ssfnet
} // namespace prime
