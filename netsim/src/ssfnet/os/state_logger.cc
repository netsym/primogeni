/**
 * \file state_logger.h
 * \brief Header file for the logging state to files and tcp streams
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

#include "os/community.h"
#include "os/partition.h"
#include "os/state_logger.h"
#include "os/logger.h"
#include <netdb.h>
#include <sys/select.h>
#include <sys/types.h>
#include <sys/select.h>
#include <unistd.h>
#include <os/model_builder.h>

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(StateLogger);

//#define LOG_DEBUG(X){PRIME_STRING_STREAM ss; ss<<"D["<<prime::ssfnet::Partition::getInstance()->getPartitionId()<<":statelogger::"<<__LINE__<<"]"<<X; std::cout<<ss.str();}
//#define LOG_WARN(X){PRIME_STRING_STREAM ss; ss<<"W["<<prime::ssfnet::Partition::getInstance()->getPartitionId()<<":statelogger::"<<__LINE__<<"]"<<X; std::cout<<ss.str();}
//#define DEBUG_CODE(X) {X}

#if TEST_VIZ_THROUGHPUT
uint64_t currentTimeMillis ()
{
	struct timeval tv;
	gettimeofday(&tv,NULL);
	return (1000000ULL*(tv.tv_sec)+(tv.tv_usec))/1000;
}
#endif

VarUpdate::VarUpdate(int var_id_, int type_): var_id(var_id_), type(type_){}

LongUpdate::LongUpdate(int var_id, int64_t value_): VarUpdate(var_id, model_builder::TLV::LONG),value((uint64_t)value_){}
LongUpdate::LongUpdate(int var_id, int32_t value_): VarUpdate(var_id, model_builder::TLV::LONG),value((uint64_t)value_){}
LongUpdate::LongUpdate(int var_id, int16_t value_): VarUpdate(var_id, model_builder::TLV::LONG),value((uint64_t)value_){}
LongUpdate::LongUpdate(int var_id, uint64_t value_): VarUpdate(var_id, model_builder::TLV::LONG),value(value_){}
LongUpdate::LongUpdate(int var_id, uint32_t value_): VarUpdate(var_id, model_builder::TLV::LONG),value(value_){}
LongUpdate::LongUpdate(int var_id, uint16_t value_): VarUpdate(var_id, model_builder::TLV::LONG),value(value_){}
LongUpdate::LongUpdate(int var_id): VarUpdate(var_id, model_builder::TLV::LONG){}
LongUpdate::LongUpdate(const LongUpdate& o): VarUpdate((const VarUpdate&)o), value(o.value){}
void LongUpdate::pack(prime::ssf::ssf_compact* dp) {
	dp->add_int(var_id);
	dp->add_int(type);
	dp->add_long_long(value);
}
void LongUpdate::unpack(prime::ssf::ssf_compact* dp) {
	dp->get_unsigned_long_long(&value);
}
void LongUpdate::encode(int& offset, int size, char* buf) {
	if(size-offset < 16)
		LOG_ERROR("Buffer was too small!");
	*((int32_t*)(buf+offset)) = myhtonl((int32)var_id);
	*((int32_t*)(buf+offset+4)) = myhtonl((int32)type);
	*((uint64_t*)(buf+offset+8)) = myhtonll(value);
	offset+=16;
}
int LongUpdate::size(){
	return 20;
}
SSFNET_STRING LongUpdate::svalue() {
	char t[128];
	sprintf(t,"%llu",value);
	SSFNET_STRING rv;
	rv.append(t);
	return rv;
}


DoubleUpdate::DoubleUpdate(int var_id, double value_): VarUpdate(var_id, model_builder::TLV::FLOAT),value(value_){}
DoubleUpdate::DoubleUpdate(int var_id): VarUpdate(var_id, model_builder::TLV::FLOAT){}
DoubleUpdate::DoubleUpdate(const DoubleUpdate& o): VarUpdate((const VarUpdate&)o), value(o.value){}
void DoubleUpdate::pack(prime::ssf::ssf_compact* dp) {
	dp->add_int(var_id);
	dp->add_int(type);
	dp->add_double(value);
}
void DoubleUpdate::unpack(prime::ssf::ssf_compact* dp) {
	dp->get_double(&value);
}
void DoubleUpdate::encode(int& offset, int size, char* buf) {
	char t[128];
	sprintf(t,"%f",value);
	if(size-offset < (int)(12+strlen(t)))
		LOG_ERROR("Buffer was too small!");
	*((int32_t*)(buf+offset)) = myhtonl((int32)var_id);
	*((int32_t*)(buf+offset+4)) = myhtonl((int32)type);
	*((int32_t*)(buf+offset+8)) = myhtonl((int32)strlen(t));
	memcpy(buf+offset+12,t,strlen(t));
	offset+=12+strlen(t);
}
int DoubleUpdate::size(){
	char t[128];
	sprintf(t,"%f",value);
	return 12+strlen(t);
}
SSFNET_STRING DoubleUpdate::svalue() {
	char t[128];
	sprintf(t,"%f",value);
	SSFNET_STRING rv;
	rv.append(t);
	return rv;
}


BoolUpdate::BoolUpdate(int var_id, bool value_): VarUpdate(var_id, model_builder::TLV::BOOL),value(value_){}
BoolUpdate::BoolUpdate(int var_id): VarUpdate(var_id, model_builder::TLV::BOOL){}
BoolUpdate::BoolUpdate(const BoolUpdate& o): VarUpdate((const VarUpdate&)o), value(o.value){}
void BoolUpdate::pack(prime::ssf::ssf_compact* dp) {
	dp->add_int(var_id);
	dp->add_int(type);
	dp->add_short(value?1:0);
}
void BoolUpdate::unpack(prime::ssf::ssf_compact* dp) {
	short s;
	dp->get_short(&s);
	value=s?1:0;
}
void BoolUpdate::encode(int& offset, int size, char* buf) {
	if(size-offset < 9)
		LOG_ERROR("Buffer was too small!");
	*((int32_t*)(buf+offset)) = myhtonl((int32)var_id);
	*((int32_t*)(buf+offset+4)) = myhtonl((int32)type);
	*((byte*)(buf+offset+8)) = (byte)(value?0xff:0x00);
	offset+=9;
}
int BoolUpdate::size(){
	return 9;
}
SSFNET_STRING BoolUpdate::svalue() {
	SSFNET_STRING rv;
	rv.append(value?"true":"false");
	return rv;
}


StringUpdate::StringUpdate(int var_id, SSFNET_STRING value_): VarUpdate(var_id, model_builder::TLV::STRING),value(value_){}
StringUpdate::StringUpdate(int var_id): VarUpdate(var_id, model_builder::TLV::STRING){}
StringUpdate::StringUpdate(const StringUpdate& o): VarUpdate((const VarUpdate&)o), value(o.value){}
void StringUpdate::pack(prime::ssf::ssf_compact* dp) {
	dp->add_int(value.length());
	dp->add_char_array(value.length(),(char*)(value.c_str()));
}

void StringUpdate::unpack(prime::ssf::ssf_compact* dp) {
	int s;
	dp->get_int(&s);
	char* t = new char[s];
	dp->get_char(t,s);
	value.append(t);
	delete[] t;
}
void StringUpdate::encode(int& offset, int size, char* buf) {
	if(size-offset < (int)(12+value.length()))
		LOG_ERROR("Buffer was too small!");
	*((int32_t*)(buf+offset)) = myhtonl((int32)var_id);
	*((int32_t*)(buf+offset+4)) = myhtonl((int32)type);
	*((uint32_t*)(buf+offset+8)) = myhtonl((uint32_t)value.size());
	memcpy(buf+offset+12,value.c_str(),value.length());
	offset+=12+value.length();
}
int StringUpdate::size(){
	return 12+value.size();
}
SSFNET_STRING StringUpdate::svalue() {
	return value;
}


CreateNodeEvent::CreateNodeEvent() :
		SSFNetEvent(SSFNET_CREATE_NODE_EVT), length(0), tlv(0) {
}
CreateNodeEvent::CreateNodeEvent(int length_, char* tlv_) :
		SSFNetEvent(SSFNET_CREATE_NODE_EVT), length(length_), tlv(0) {
	if(length) {
		tlv = new char[length+1];
		memcpy(tlv,tlv_,length);
		tlv[length]='\0';
	}
}
CreateNodeEvent::CreateNodeEvent(const CreateNodeEvent& o):
		SSFNetEvent(SSFNET_CREATE_NODE_EVT), length(o.length), tlv(0) {
	if(length) {
		tlv = new char[length+1];
		memcpy(tlv,o.tlv,length);
		tlv[length]='\0';
	}
}

CreateNodeEvent::~CreateNodeEvent() {
	if(tlv) delete tlv;
}

Event* CreateNodeEvent::clone() {
	return new CreateNodeEvent(*this);
}

prime::ssf::ssf_compact* CreateNodeEvent::pack() {
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_int(length);
	dp->add_char_array(length,tlv);
	return dp;
}

void CreateNodeEvent::unpack(prime::ssf::ssf_compact* dp) {
	SSFNetEvent::unpack(dp);
	dp->get_int(&length);
	tlv=new char[length+1];
	dp->get_char(tlv,length);
	tlv[length]='\0';
}

prime::ssf::Event* CreateNodeEvent::createCreateNodeEvent(prime::ssf::ssf_compact* dp) {
	CreateNodeEvent * rv = new CreateNodeEvent();
	rv->unpack(dp);
	return rv;
}

// used to register an event class.
SSF_REGISTER_EVENT(CreateNodeEvent, CreateNodeEvent::createCreateNodeEvent);



AreaOfInterestUpdateEvent::AreaOfInterestUpdateEvent()
:SSFNetEvent(SSFNetEvent::SSFNET_AREA_OF_INTEREST_UPDATE), add(false), count(0), entities(0) {
}

AreaOfInterestUpdateEvent::AreaOfInterestUpdateEvent(bool add_, int count_, UID_t* entities_)
:SSFNetEvent(SSFNetEvent::SSFNET_AREA_OF_INTEREST_UPDATE), add(add_), count(count_), entities(entities_){
}

AreaOfInterestUpdateEvent::AreaOfInterestUpdateEvent(const AreaOfInterestUpdateEvent& o)
:SSFNetEvent(SSFNetEvent::SSFNET_AREA_OF_INTEREST_UPDATE), add(o.add), count(o.count), entities(new UID_t[o.count]){
	memcpy(entities,o.entities,sizeof(UID_t)*count);
}

AreaOfInterestUpdateEvent::~AreaOfInterestUpdateEvent() {
	if(entities)
		delete entities;
}

Event* AreaOfInterestUpdateEvent::clone() {
	return new AreaOfInterestUpdateEvent(*this);
}

prime::ssf::ssf_compact* AreaOfInterestUpdateEvent::pack() {
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_short(add?1:0);
	dp->add_int(count);
	for(int i=0; i< count; i++) {
		dp->add_unsigned_long_long(entities[i]);
	}
	return dp;
}

void AreaOfInterestUpdateEvent::unpack(prime::ssf::ssf_compact* dp) {
	SSFNetEvent::unpack(dp);
	short t;
	dp->get_short(&t);
	add=t?true:false;
	dp->get_int(&count);
	entities = new UID_t[count];
	for(int i=0;i<count;i++) {
		dp->get_unsigned_long_long(&(entities[i]));
	}
}

prime::ssf::Event* AreaOfInterestUpdateEvent::createAreaOfInterestUpdateEvent(prime::ssf::ssf_compact* dp) {
	AreaOfInterestUpdateEvent * rv = new AreaOfInterestUpdateEvent();
	rv->unpack(dp);
	return rv;
}

// used to register an event class.
SSF_REGISTER_EVENT(AreaOfInterestUpdateEvent, AreaOfInterestUpdateEvent::createAreaOfInterestUpdateEvent);

VizExportRateUpdateEvent::VizExportRateUpdateEvent::VizExportRateUpdateEvent()
:SSFNetEvent(SSFNetEvent::SSFNET_VIZ_EXPORT_UPDATE), rate(0) {
}
VizExportRateUpdateEvent::VizExportRateUpdateEvent(uint64 rate_)
:SSFNetEvent(SSFNetEvent::SSFNET_VIZ_EXPORT_UPDATE), rate(rate_) {
}

VizExportRateUpdateEvent::VizExportRateUpdateEvent(const VizExportRateUpdateEvent& o)
:SSFNetEvent(SSFNetEvent::SSFNET_VIZ_EXPORT_UPDATE), rate(o.rate) {
}

VizExportRateUpdateEvent::~VizExportRateUpdateEvent() { }
Event* VizExportRateUpdateEvent::VizExportRateUpdateEvent::clone() {
	return new VizExportRateUpdateEvent(rate);
}
prime::ssf::ssf_compact* VizExportRateUpdateEvent::pack() {
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_long_long(rate);
	return dp;
}
void VizExportRateUpdateEvent::VizExportRateUpdateEvent::unpack(prime::ssf::ssf_compact* dp) {
	SSFNetEvent::unpack(dp);
	dp->get_unsigned_long_long(&rate);
}
prime::ssf::Event* VizExportRateUpdateEvent::createVizExportRateUpdateEvent(prime::ssf::ssf_compact* dp) {
	VizExportRateUpdateEvent * rv = new VizExportRateUpdateEvent();
	rv->unpack(dp);
	return rv;
}

// used to register an event class.
SSF_REGISTER_EVENT(VizExportRateUpdateEvent, VizExportRateUpdateEvent::createVizExportRateUpdateEvent);



StateUpdateEvent::StateUpdateEvent(SSFNetEvent::Type type, VirtualTime time_, UID_t uid_, bool forViz_, bool aggregate_)
:SSFNetEvent(type),time((uint64_t)time_.millisecond()),uid(uid_), forViz(forViz_), aggregate(aggregate_){
}

StateUpdateEvent::StateUpdateEvent()
:SSFNetEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT) ,time(0), uid(0), forViz(false), aggregate(false){
}

StateUpdateEvent::~StateUpdateEvent(){
	for(VarUpdate::VarUpdateVector::iterator i = updates.begin(); i!=updates.end(); i++) {
		delete *i;
	}
	updates.clear();
}

StateUpdateEvent::StateUpdateEvent(const StateUpdateEvent& o)
:SSFNetEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT),time(o.time),uid(o.uid), forViz(o.forViz), aggregate(o.aggregate){
	union {
		BoolUpdate* b;
		StringUpdate* s;
		LongUpdate* l;
		DoubleUpdate* d;
	} v;
	for(VarUpdate::VarUpdateVector::const_iterator i = o.updates.begin(); i!=o.updates.end(); i++) {
		v.b= (BoolUpdate*)(*i);
		switch((*i)->type) {
		case model_builder::TLV::BOOL:
			updates.push_back(new BoolUpdate(*(v.b)));
			break;
		case model_builder::TLV::FLOAT:
			updates.push_back(new DoubleUpdate(*(v.d)));
			break;
		case model_builder::TLV::LONG:
			updates.push_back(new LongUpdate(*(v.l)));
			break;
		case model_builder::TLV::STRING:
			updates.push_back(new StringUpdate(*(v.s)));
			break;
		default:
			LOG_ERROR("unexpected var update type: "<<(*i)->type);
		};
	}
}


void StateUpdateEvent::addVarUpdate(int id, bool value) {
	updates.push_back(new BoolUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, double value) {
	updates.push_back(new DoubleUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, uint16 value) {
	updates.push_back(new LongUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, uint32 value) {
	updates.push_back(new LongUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, uint64 value) {
	updates.push_back(new LongUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, int16 value) {
	updates.push_back(new LongUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, int32 value) {
	updates.push_back(new LongUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, int64 value) {
	updates.push_back(new LongUpdate(id,value));
}

void StateUpdateEvent::addVarUpdate(int id, SSFNET_STRING value) {
	updates.push_back(new StringUpdate(id,value));
}

Event* StateUpdateEvent::clone() {
	return new StateUpdateEvent(*this);
}

prime::ssf::ssf_compact* StateUpdateEvent::pack() {
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_long_long(time);
	dp->add_unsigned_long_long(uid);
	dp->add_short(forViz?1:0);
	dp->add_short(aggregate?1:0);
	dp->add_int(updates.size());
	for(VarUpdate::VarUpdateVector::iterator i = updates.begin(); i!=updates.end(); i++) {
		(*i)->pack(dp);
	}
	return dp;
}

void StateUpdateEvent::unpack(prime::ssf::ssf_compact* dp) {
	SSFNetEvent::unpack(dp);
	dp->get_unsigned_long_long(&time);
	dp->get_unsigned_long_long(&uid);
	short bb;
	dp->get_short(&bb);
	forViz=bb?true:false;
	dp->get_short(&bb);
	aggregate=bb?true:false;
	int s, id, type;
	dp->get_int(&s);
	for(int i=0;i<s;i++) {
		dp->get_int(&id);
		dp->get_int(&type);
		switch(type) {
		case model_builder::TLV::BOOL: {
			BoolUpdate* s=new BoolUpdate(id);
			s->unpack(dp);
			updates.push_back(s);
		}
		break;
		case model_builder::TLV::FLOAT: {
			DoubleUpdate* s = new DoubleUpdate(id);
			s->unpack(dp);
			updates.push_back(s);
		}
		break;
		case model_builder::TLV::LONG: {
			LongUpdate* s = new LongUpdate(id);
			s->unpack(dp);
			updates.push_back(s);
		}
		break;
		case model_builder::TLV::STRING: {
			StringUpdate* s = new StringUpdate(id);
			s->unpack(dp);
			updates.push_back(s);
		}
		break;
		default: {
			LOG_ERROR("unexpected var update type: "<<type);
		}
		};
	}
}

prime::ssf::Event* StateUpdateEvent::createStateUpdateEvent(prime::ssf::ssf_compact* dp) {
	StateUpdateEvent* evt = new StateUpdateEvent();
	evt->unpack(dp);
	return evt;
}

// used to register an event class.
SSF_REGISTER_EVENT(StateUpdateEvent, StateUpdateEvent::createStateUpdateEvent);



class StateUpdateArrivalProcess : public prime::ssf::Process {
public:
	StateLogger* sl;
	StateUpdateArrivalProcess(StateLogger* _sl) :
		prime::ssf::Process(_sl->getCommunity(), true),
		sl(_sl) {}
	void action(); //! SSF PROCEDURE SIMPLE
};


//! SSF PROCEDURE SIMPLE
void StateUpdateArrivalProcess::action()
{
	//! SSF CALL
	sl->arrival_process(this);

	LOG_DEBUG("StateUpdateArrivalProcess::action(): waitOn()\n");
	waitOn();
}

StateLogger::StateLogger(Community* _community)
: community(_community),input_channel(0),output_channel(0), stop(false) {
}

StateLogger::~StateLogger() {

}

void StateLogger::init(){
	output_channel = new prime::ssf::outChannel
			(getCommunity(), StateLogger::write, this);
	assert(output_channel);

	input_channel = new prime::ssf::inChannel
			(getCommunity(), StateLogger::read, this);
	assert(input_channel);

	StateUpdateArrivalProcess* reader = new StateUpdateArrivalProcess(this);
	assert(reader);
	reader->waitsOn(input_channel);

	LOG_DEBUG("StateLogger::init(), getCommunity()="<<getCommunity()<<endl)
}

void StateLogger::wrapup() {
	stop=true;
}

//! SSF PROCEDURE SIMPLE
void StateLogger::arrival_process(prime::ssf::Process* p) {
	LOG_DEBUG("In arrival process"<<endl);
	SSFNetEvent** evts = (SSFNetEvent**)input_channel->activeEvents();
	for(int i=0; evts && evts[i]; i++) {
		switch(evts[i]->getSSFNetEventType()) {
		case SSFNetEvent::SSFNET_STATE_FETCH_EVT:
		{
			StateUpdateEvent* sevt = (StateUpdateEvent*)evts[i];
			BaseEntity* e= community->getObject(sevt->uid,true);
			if(NULL == e) {
				fprintf(stderr,"[part_id=%i, com_id=%i]Unknown uid %llu!\n",community->getPartition()->getPartitionId(),community->getCommunityId(), sevt->uid);
				continue;
			}
			LOG_DEBUG("In arrival process, got fetch"<<endl);
			StateUpdateEvent* update = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT, community->now(), e->getUID(),true,false);
			for(VarUpdate::VarUpdateVector::iterator j = sevt->updates.begin(); j!=sevt->updates.end(); j++) {
				SSFNET_STRING v;
				LOG_DEBUG("Fetching var "<<(*j)->var_id <<" uid="<<e->getUID()<<endl);
				if(!e->readVar((*j)->var_id,v)) {
					update->addVarUpdate((*j)->var_id, v);
				}
				else {
					LOG_WARN("Unable to find var with id "<<(*j)->var_id<<" in node "<<e->getUName()<<"!"<<endl);
				}
			}
			if(update->updates.size()>0) {
				community->exportState(update);
			}
			else {
				delete update;
			}
		}
		break;
		case SSFNetEvent::SSFNET_STATE_SET_EVT:
		{
			StateUpdateEvent* sevt = (StateUpdateEvent*)evts[i];
			BaseEntity* e= community->getObject(sevt->uid,true);
			if(NULL == e) {
				fprintf(stderr,"[part_id=%i, com_id=%i]Unknown uid %llu!\n",community->getPartition()->getPartitionId(),community->getCommunityId(), sevt->uid);
				continue;
			}
			LOG_DEBUG("In arrival process, got set"<<endl);
			StateUpdateEvent* update = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_UPDATE_EVT, community->now(), e->getUID(),true,false);
			for(VarUpdate::VarUpdateVector::iterator j = sevt->updates.begin(); j!=sevt->updates.end(); j++) {
				SSFNET_STRING v=(*j)->svalue();
				LOG_DEBUG("Setting var "<<(*j)->var_id <<" uid="<<e->getUID()<<endl);
				if(!e->writeVar((*j)->var_id,v)) {
					update->addVarUpdate((*j)->var_id, v);
				}
				else {
					LOG_WARN("Unable to find var with id "<<(*j)->var_id<<" in node "<<e->getUName()<<"!"<<endl);
				}
			}
			if(update->updates.size()>0) {
				community->exportState(update);
			}
			else {
				delete update;
			}
		}
		break;
		case SSFNetEvent::SSFNET_AREA_OF_INTEREST_UPDATE:
		{
			LOG_WARN("arrival processes got the aoi update"<<endl)
			AreaOfInterestUpdateEvent* sevt = (AreaOfInterestUpdateEvent*)evts[i];
			if(sevt->add) {
				for(int i = 0; i< sevt->count; i++)
					community->addToAreaOfInterest(sevt->entities[i]);
			}
			else {
				for(int i = 0; i< sevt->count; i++)
					community->removeFromAreaOfInterest(sevt->entities[i]);
			}
		}
		break;
		case SSFNetEvent::SSFNET_VIZ_EXPORT_UPDATE:
		{
			LOG_WARN("arrival processes got a viz export rate update"<<endl)
			VizExportRateUpdateEvent* sevt = (VizExportRateUpdateEvent*)evts[i];
			community->updateVizExportRate(sevt->rate);
		}
		break;
		case SSFNetEvent::SSFNET_CREATE_NODE_EVT:
		{
			PRIME_ISTRING_STREAM iss(((CreateNodeEvent*)evts[i])->tlv, PRIME_ISTRING_STREAM::in);
			model_builder::TLV t(&iss);
			model_builder::ModelNode m(t, 0);

			BaseEntity* p= community->getObject(m.parent_id,true);
			m.base_entity->setUID(0);
			m.base_entity->setOffset(0);

			LOG_DEBUG("adding context "<<m.base_entity->getUName()<<endl);

			p->addChild(m.base_entity);
			if(StaticTrafficType::getClassConfigType()->isSubtype(m.base_entity->getConfigType())) {
				m.base_entity->init();
				community->getTrafficManager()->addDynamicallyCreatedTraffic((StaticTrafficType*)m.base_entity);
				LOG_DEBUG("Created new traffic type of type "<<m.base_entity->getConfigType()->getName()<<endl);
			}
			else {
				LOG_ERROR("Unsupported type of externally created dynamic node "<<m.base_entity->getConfigType()->getName()<<endl);
			}
		}
		break;
		default:
			LOG_WARN("Invalid state exchange type "<<evts[i]->getSSFNetEventType()<<endl);
			break;
		}
	}
}

void StateLogger::read(prime::ssf::inChannel* ic, void* context) {
	((StateLogger*)context)->reader_thread();
}

void StateLogger::write(prime::ssf::outChannel* oc, void* context) {
	((StateLogger*)context)->writer_thread();
}

void StateLogger::export_state(StateUpdateEvent* update) {
	output_channel->write(update);
}

FileStateLogger::FileStateLogger(Community* _community, ofstream* file_):StateLogger(_community),file(file_) {
	LOG_INFO("Started FileStateLogger!"<<endl);
}
FileStateLogger::~FileStateLogger() {
	stop=true;
	ofstream* t = file;
	file=0;
	t->close();
	delete t;
}

void FileStateLogger::init() {
	StateLogger::init();
}

void FileStateLogger::wrapup() {
	StateLogger::wrapup();
}

void FileStateLogger::reader_thread(){
	LOG_INFO("File loggers do not support setting of state, quiting reader thread."<<endl);
};

void FileStateLogger::writer_thread(){
	int cid = getCommunity()->getCommunityId();
	double delay=0;
	*file<<"community, time, is_aggregate, uid, var_id, value\n";
	for(;!stop;) {
		// receive event through the out-channel from the simulator
		StateUpdateEvent* evt = (StateUpdateEvent*)output_channel->getRealEvent(delay);
		if(evt->forViz) {
			LOG_WARN("in file-logging mode yet we got viz info....\n");
		}
		else {
			for(VarUpdate::VarUpdateVector::iterator i = evt->updates.begin(); i!=evt->updates.end(); i++) {
				*file<<cid<<", "<<evt->time<<","<< evt->aggregate<<"," <<evt->uid<<","<<(*i)->var_id<<","<<(*i)->svalue()<<"\n";
			}
		}
		evt->free();
	}
	file->flush();
	file->close();
};

SSFNET_STRING TCPStateLogger::ip="127.0.0.1";
int TCPStateLogger::port=9992;

void TCPStateLogger::setTarget(SSFNET_STRING ip_,int port_) {
	TCPStateLogger::ip=ip_;
	TCPStateLogger::port=port_;
	LOG_DEBUG("Set the state stream server to "<<ip<<":"<<port<<endl);
}


TCPStateLogger::TCPStateLogger(Community* _community):
								StateLogger(_community),in_sock(0),out_sock(0),in_bs(0),out_bs(0),in_buf(0), out_buf(0){ }
TCPStateLogger::~TCPStateLogger() { }

void TCPStateLogger::init() {
	StateLogger::init();

	in_bs=TCP_STATE_BUF_SZ;
	out_bs=TCP_STATE_BUF_SZ;
	in_buf = new char[in_bs];
	out_buf = new char[out_bs];

	out_sock = socket(AF_INET, SOCK_STREAM, 0);
	if(out_sock == -1) {
		LOG_ERROR("TCPStateLogger cannot create socket, "<<ip<<":"<<port<<endl);
	}
	in_sock = socket(AF_INET, SOCK_STREAM, 0);
	if(in_sock == -1) {
		LOG_ERROR("TCPStateLogger cannot create socket, "<<ip<<":"<<port<<endl);
	}

	struct hostent* hent = gethostbyname(ip.c_str());
	if(!hent) {
		LOG_ERROR("TCPStateLogger cannot get the host info of "<<ip<<":"<<port<<endl);
	}

	sockaddr_in addr;
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	addr.sin_addr = *(struct in_addr*)*hent->h_addr_list;
	if(connect(out_sock, (struct sockaddr*)&addr, sizeof(addr)) != 0) {
		LOG_ERROR("TCPStateLogger cannot connect to "<<ip<<":"<<port<<endl);
	}
	if(connect(in_sock, (struct sockaddr*)&addr, sizeof(addr)) != 0) {
		LOG_ERROR("TCPStateLogger cannot connect to "<<ip<<":"<<port<<endl);
	}

	LOG_INFO("Started TCPStateLogger!"<<endl);
}

void TCPStateLogger::wrapup() {
	LOG_DEBUG("closing logger"<<endl);
	stop=true;
#if TEST_VIZ_THROUGHPUT
	writer_debug<< "#FINISH"<<endl;
	writer_debug.close();
#endif
	if(out_sock) {
		int i = out_sock;
		out_sock=0;
		close(i);
	}
	if(in_sock) {
		int i = in_sock;
		in_sock=0;
		close(i);
	}
	if(out_buf) {
		delete[] out_buf;
		out_buf=0;
	}
	if(in_buf) {
		delete[] in_buf;
		in_buf=0;
	}
	StateLogger::wrapup();
}

int TCPStateLogger::safe_send(int s, char* buf, unsigned int bufsiz)
{
	int sent_len = 0;
	while(sent_len < (int)bufsiz){
		int ret = send(s, buf+sent_len, bufsiz-sent_len, 0/*MSG_NOSIGNAL*/);
		if(ret <= 0) return -1;
		sent_len += ret;
	}
	return (int)sent_len;
}

int TCPStateLogger::safe_receive(int s, char* buf, unsigned int bufsiz)
{
	int rcvd_len = 0;
	while(rcvd_len < (int)bufsiz){
#if defined(PRIME_SSF_MACH_X86_CYGWIN) || defined(PRIME_SSF_MACH_X86_64_CYGWIN)
		// windows does not support MSG_WAITALL
		int ret = recv(s, buf+rcvd_len, bufsiz-rcvd_len, 0);
#else
		int ret = recv(s, buf+rcvd_len, bufsiz-rcvd_len, MSG_WAITALL);
#endif
		if(ret <= 0) return -1;
		rcvd_len += ret;
	}
	return rcvd_len;
}

#define STATE_EX_HEAD_SIZE_READ 28 // 4+8+8+4+4 (total_size, uid, time, type, #updates)
#define STATE_EX_HEAD_SIZE_WRITE 36 // 4+8+8+4+4+4+4 (total_size, uid, forViz, aggregate, time, type, #updates)

void TCPStateLogger::reader_thread(){
	int offset, max_fd=in_sock+1;
	int32 msg_type=0,msg_size=0,cmd_type=0,var_count=0,var_id=0,var_type=0, add=0, uid_count=0;
	uint64 uid,time;
	StateUpdateEvent* state_update_evt=0;
	//send initial string for writer thread
	*((int32*)(in_buf))=myhtonl((int32)1); //reader
	*((int32*)(in_buf+4))=myhtonl((int32)community->getPartition()->getPartitionId());
	*((int32*)(in_buf+8))=myhtonl((int32)community->getCommunityId());
	if(12 != safe_send(in_sock,in_buf,12)) {
		LOG_WARN("Error while sending initial string to to state server!"<<endl);
		close(in_sock);
		in_sock=0;
	}
	VirtualTime mintime(50,VirtualTime::MILLISECOND);
	for (;!stop;) {
		fd_set fdset;
		FD_ZERO(&fdset);
		FD_SET(in_sock,&fdset);
		int ret = select(max_fd+1, &fdset, NULL, NULL, NULL);
		if(ret <= 0){
			//all the sockets in the descriptor set were closed!
			LOG_WARN("Error while listening for state sets/fetches");
			close(in_sock);
			in_sock=0;
			return;
		}
		if(FD_ISSET(in_sock, &fdset)) {
			//we have something!
			LOG_WARN("Reading a message"<<endl);

			//is this a create node or state exchange cmd?
			if(4 != safe_receive(in_sock,in_buf,4)) {
				LOG_WARN("Error reading state updates");
				close(in_sock);
				in_sock=0;
				return;
			}
			msg_type = (int32)myntohl( *((int32*)(in_buf+0)));
			LOG_WARN("msg_type="<<msg_type<<endl);

			switch(msg_type) {
			case 1000:
			{
				//format: total_length(I),time(L),uid(L),type(I),num_vars(I),vars
				if(STATE_EX_HEAD_SIZE_READ != safe_receive(in_sock,in_buf,STATE_EX_HEAD_SIZE_READ)) {
					LOG_WARN("Error reading state updates");
					close(in_sock);
					in_sock=0;
					return;
				}
				msg_size = (int32)myntohl( *((int32*)(in_buf+0)));
				time = myntohll( *((uint64*)(in_buf+4)));
				uid = myntohll( *((uint64*)(in_buf+12)));
				cmd_type = (int32)myntohl( *((int32*)(in_buf+20)));
				var_count = (int32)myntohl( *((int32*)(in_buf+24)));
				msg_size -= STATE_EX_HEAD_SIZE_READ;

				VirtualTime t((long long)time,VirtualTime::MILLISECOND);
				//if(community->now()-t<= mintime)
				//	t=community->now()+mintime;
				state_update_evt=0;
				switch(cmd_type) {
				case 1://set
					LOG_DEBUG("Got set command!"<<endl)
					state_update_evt = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_SET_EVT,t,uid,false,false);
					break;
				case 2://update
					LOG_DEBUG("did not expect to see updates on the in sock!");
					close(in_sock);
					in_sock=0;
					return;
				case 3://fetch
					LOG_DEBUG("Got fetch command!"<<endl)
					state_update_evt = new StateUpdateEvent(SSFNetEvent::SSFNET_STATE_FETCH_EVT,t,uid,false,false);
					break;
				default:
					LOG_WARN("in countered invalid command type "<<cmd_type<<endl);
					close(in_sock);
					in_sock=0;
					stop=true;
					return;
				}
				if(state_update_evt==0) {
					LOG_ERROR("how did this happen?"<<endl);
				}

				//read in the var updates
				if(msg_size > (int32)in_bs) {
					in_bs = msg_size+256;
					delete[] in_buf;
					in_buf = new char[in_bs];
				}

				if(msg_size != safe_receive(in_sock,in_buf,msg_size)) {
					LOG_WARN("Error reading state updates");
					close(in_sock);
					in_sock=0;
					return;
				}

				offset=0;
				for(int i=0;i<var_count;i++) {
					if((offset+8)>=msg_size) {
						LOG_WARN("Error reading state updates");
						close(in_sock);
						in_sock=0;
						return;
					}
					var_id = (int32)myntohl( *((int32*)(in_buf+offset)));
					var_type = (int32)myntohl( *((int32*)(in_buf+offset+4)));
					offset+=8;
					switch(var_type) {
					case model_builder::TLV::LONG:
						state_update_evt->addVarUpdate(var_id,(uint64)myntohl( *((uint64*)(in_buf+offset))));
						LOG_DEBUG("\ttype=long, var_id="<<var_id<<endl)
						offset+=8;
						break;
					case model_builder::TLV::FLOAT:
					case model_builder::TLV::STRING: {
						LOG_DEBUG("\ttype=float/string, var_id="<<var_id<<endl)
												int l = (int32)myntohl( *((int32*)(in_buf+offset)));
						offset+=4;
						char* b = new char[l+1];
						SSFNET_STRING s;
						memcpy(b,in_buf+offset,l);
						b[l]='\0';
						switch(var_type) {
						case model_builder::TLV::FLOAT:
							state_update_evt->addVarUpdate(var_id,atof(b));
							break;
						case model_builder::TLV::STRING:
							s.append(b);
							state_update_evt->addVarUpdate(var_id,s);
							break;
						}
						offset=+l;
						delete b;
					}
					break;
					case model_builder::TLV::BOOL:
						LOG_DEBUG("\ttype=bool, var_id="<<var_id<<endl)
						state_update_evt->addVarUpdate(var_id,(bool)(in_buf[offset]?true:false));
						offset++;
						break;
					default:
						LOG_WARN("in countered invalid var type "+var_type);
						close(in_sock);
						in_sock=0;
						stop=true;
						return;
					}
				}
				LOG_DEBUG("\tsending evt to sim!"<<endl);
				input_channel->putRealEvent(state_update_evt);
			}
			break;
			case 1001:
			{
				LOG_WARN("reading dynamic traffic..."<<endl);
				//format: total_length(I),TLV
				if(4 != safe_receive(in_sock,in_buf,4)) {
					LOG_WARN("Error reading state updates");
					close(in_sock);
					in_sock=0;
					return;
				}
				msg_size = (int32)myntohl( *((int32*)(in_buf+0)));
				LOG_WARN("reading tlv of "<<msg_size<<" bytes "<<endl);
				if(msg_size != safe_receive(in_sock,in_buf,msg_size)) {
					LOG_WARN("Error reading state updates");
					close(in_sock);
					in_sock=0;
					return;
				}
				input_channel->putRealEvent(new CreateNodeEvent(msg_size, in_buf));
			}
			break;
			case 1002:
			{
				LOG_WARN("reading 8 bytes..."<<endl);

				//format: add/remove(I),#uids(I),uid(L),...,uid(L)
				if(8 != safe_receive(in_sock,in_buf,8)) {
					LOG_WARN("Error reading state updates");
					close(in_sock);
					in_sock=0;
					return;
				}
				offset=0;

				add = (int32)myntohl( *((int32*)(in_buf+offset)));
				offset+=4;

				uid_count = (int32)myntohl( *((int32*)(in_buf+offset)));
				offset+=4;

				LOG_WARN("add="<<((int)add)<<" uidcount="<<((int)uid_count)<<endl);

				offset=8*uid_count;

				if(offset > (int32)in_bs) {
					in_bs = offset+256;
					delete[] in_buf;
					in_buf = new char[in_bs];
				}

				if(offset != safe_receive(in_sock,in_buf,offset)) {
					LOG_WARN("Error reading state updates");
					close(in_sock);
					in_sock=0;
					return;
				}
				UID_t* entities = new UID_t[(int)uid_count];
				offset=0;
				for(int i=0;i<uid_count;i++) {
					entities[i] = myntohll( *((uint64*)(in_buf+offset)));
					offset+=8;
					LOG_WARN("\t["<<(int)i<<"]="<<(long long int)entities[i]<<endl);
				}
				LOG_WARN("sending area of interest command!"<<endl);
				input_channel->putRealEvent(new AreaOfInterestUpdateEvent(add?true:false,uid_count,entities));
			}
			break;
			case 1003:
			{
				LOG_WARN("reading rate update..."<<endl);
				//format: long
				if(8 != safe_receive(in_sock,in_buf,8)) {
					LOG_WARN("Error reading rate update");
					close(in_sock);
					in_sock=0;
					return;
				}
				VizExportRateUpdateEvent* rate = new VizExportRateUpdateEvent();
				rate->rate = myntohll( *((uint64*)(in_buf)));
				std::cout << "sending rate update...rate="<<rate->rate<<endl;
				//LOG_WARN("sending rate update...rate="<<rate->rate<<endl);
				input_channel->putRealEvent(rate);
			}
			break;


			default :
			{
				LOG_WARN("Error reading state updates, read in cmd type "<<msg_size+" which is unknown!");
				close(in_sock);
				in_sock=0;
			}
			break;
			}
		}
	}
};

void TCPStateLogger::writer_thread(){
#if TEST_VIZ_THROUGHPUT
	uint64_t ack_count=0;
	writer_debug.open("/tmp/writer_timming.csv");
	writer_debug << "#real_time_start, real_time_end, sim_time_start, sim_time_end, obj_count, state_count\n";
	uint64_t real_start=-1,sim_start=-1,objs=0, states=0;
	bool first_evt=true;
#endif

	int offset;
	double delay=0;
	//send initial string for writer thread
	*((int32*)(out_buf))=myhtonl((int32)0); //writer
	*((int32*)(out_buf+4))=myhtonl((int32)community->getPartition()->getPartitionId());
	*((int32*)(out_buf+8))=myhtonl((int32)community->getCommunityId());

	if(12 != safe_send(out_sock,out_buf,12)) {
		LOG_WARN("Error while sending initial string to to state server!"<<endl);
		close(out_sock);
		out_sock=0;
	}

	for(;!stop;) {
		// receive event through the out-channel from the simulator
		StateUpdateEvent* evt = (StateUpdateEvent*)output_channel->getRealEvent(delay);
		if(out_sock)  {
#if TEST_VIZ_THROUGHPUT
			if(first_evt) {
				//first
				first_evt=false;
				sim_start=evt->time;
				real_start=currentTimeMillis();
				objs=1;
				states=evt->updates.size();
			}
			else if(sim_start < evt->time) {
				//start new batch....
				//#real_time_start, real_time_end, sim_time_start, sim_time_end, obj_count, state_count\n
				objs++;
				states+=evt->updates.size();
				writer_debug << real_start<<","<<currentTimeMillis()<<","<<sim_start<<","<<evt->time<<","<<objs<<","<<states<<"\n";
				sim_start=evt->time;
				real_start=currentTimeMillis();
				objs=1;
				states=evt->updates.size();
			}
			else {
				objs++;
				states+=evt->updates.size();
			}
			if(evt->time-ack_count > 500) {
				writer_debug.flush();
				ack_count=evt->time;
			}
#endif
			int s = STATE_EX_HEAD_SIZE_WRITE;
			for(VarUpdate::VarUpdateVector::iterator i = evt->updates.begin(); i!=evt->updates.end(); i++) {
				s+=(*i)->size();
			}
			if((int32)out_bs < s) {
				out_bs=s+256;
				delete[] out_buf;
				out_buf = new char[out_bs];
			}
			offset=0;

			*((int32*)(out_buf+offset))=myhtonl(s);
			offset=4;

			*((int64*)(out_buf+offset))=myhtonll((long long)evt->time);
			offset+=8;

			*((uint64*)(out_buf+offset))=myhtonll(evt->uid);
			offset+=8;

			*((int32*)(out_buf+offset))=myhtonl((int32)(evt->forViz?1:0));
			offset+=4;

			*((int32*)(out_buf+offset))=myhtonl((int32)(evt->aggregate?1:0));
			offset+=4;

			if(evt->aggregate && evt->forViz) {
				LOG_DEBUG("sending agg update for uid="<<evt->uid<<"\n");
			}

			switch(evt->getSSFNetEventType()) {
			case SSFNetEvent::SSFNET_STATE_SET_EVT:
				*((int32*)(out_buf+offset))=myhtonl((int32)1);
				break;
			case SSFNetEvent::SSFNET_STATE_UPDATE_EVT:
				*((int32*)(out_buf+offset))=myhtonl((int32)2);
				break;
			case SSFNetEvent::SSFNET_STATE_FETCH_EVT:
				*((int32*)(out_buf+offset))=myhtonl((int32)3);
				break;
			default:
				LOG_ERROR("invalid event type!");
			}
			offset+=4;
			*((int32*)(out_buf+offset))=myhtonl((int32)(evt->updates.size()));
			offset+=4;
			for(VarUpdate::VarUpdateVector::iterator i = evt->updates.begin(); i!=evt->updates.end(); i++) {
				//LOG_DEBUG("\t"<<i->var_id<<","<<i->svalue()<<"\n");
				(*i)->encode(offset,out_bs,out_buf);
			}
			if(offset > 20) {
				if(offset!=safe_send(out_sock,out_buf,offset)) {
					LOG_WARN("Error while sending to state server!"<<endl);
					close(out_sock);
					out_sock=0;
					break;
				}
			}
		}
		evt->free();
	}
#if TEST_VIZ_THROUGHPUT
	writer_debug<< "#FINISHED"<<endl;
	writer_debug.close();
#endif
};

}; // namespace ssfnet
}; // namespace prime

