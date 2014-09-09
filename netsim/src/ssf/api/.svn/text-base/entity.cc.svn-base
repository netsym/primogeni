/**
 * \file entity.cc
 * \brief Source file for the Standard SSF entity interface.
 *
 * entity :- standard SSF entity interface.
 *
 * An entity is a container for state variables. For example, a router
 * or a host in a network simulation can be modeled as an entity. A
 * user-defined entity must derive from the base entity class defined
 * in this module.
 */

#include <errno.h>
#include <stdio.h>
#include <string.h>

#ifndef PRIME_SSF_ARCH_WINDOWS
#include <unistd.h>
#endif

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

SSF_ENT_STRFAC_MAP* Entity::_ent_registered_entities = 0;
void (*Entity::_ent_global_wrapup)() = 0;

boolean ssf_entity_dml_param::get_value(long& intval) const
{
  if(_param_type == TYPE_INTEGER) {
    intval = _param_intval;
    return true;
  } else return false;
}

boolean ssf_entity_dml_param::get_value(double& fltval) const
{
  if(_param_type == TYPE_FLOAT) {
    fltval = _param_fltval;
    return true;
  } else return false;
}

boolean ssf_entity_dml_param::get_value(const char*& strval) const
{
  if(_param_type == TYPE_STRING) {
    strval = _param_strval.c_str();
    return true;
  } else return false;
}

ssf_entity_dml_param::ssf_entity_dml_param(long intval) :
  _param_type(TYPE_INTEGER), _param_intval(intval) {}

ssf_entity_dml_param::ssf_entity_dml_param(double fltval) :
  _param_type(TYPE_FLOAT), _param_fltval(fltval) {}

ssf_entity_dml_param::ssf_entity_dml_param(const char* strval) :
  _param_type(TYPE_STRING), _param_strval(strval) {}

#if PRIME_SSF_EMULATION
Entity::Entity(boolean emulation) :
#else
Entity::Entity() :
#endif
  ssf_procedure_container(ssf_procedure_container::PROCTYPE_ENTITY),
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(0),
#endif
#if PRIME_SSF_EMULATION
  _ent_emuable(emulation),
#endif
  _ent_next_inchannel(0), _ent_next_outchannel(0),
  _ent_next_kevt_seqno(0), _ent_next_sched(1L),
  _ent_future(SSF_LTIME_MINUS_INFINITY), _ent_initevt(0),
  _ent_max_preset_inport(0), _ent_max_preset_outport(0)
{
  // this will settle the entity's timeline and serial number
  assert(SSF_CONTEXT_PROCESSING_TIMELINE);
  SSF_CONTEXT_PROCESSING_TIMELINE->add_entity(this);

#if PRIME_SSF_INSTRUMENT
  _ent_name = new SSF_STRING("(undefined entity)");
#endif

  // schedule the kernel event for the init() method
  ssf_kernel_event* kevt = new ssf_kernel_event
    (SSF_CONTEXT_NOW, ssf_kernel_event::EVTYPE_NEWENTITY);
  kevt->entity = this;
  _ent_insert_event(kevt);
}

#if 0
void Entity::dispose()
{
  assert(0); // not implemented yet.
  if(!disposed()) {
    Disposable::dispose(); // this is required
    // more to add here...
  }
}
#endif

Entity::~Entity()
{
#if PRIME_SSF_INSTRUMENT
  if (_ent_name) delete _ent_name;
#endif // instrumentation
}

ltime_t Entity::now() { return SSF_CONTEXT_NOW; }

#if PRIME_SSF_EMULATION
ltime_t Entity::real_now() { return SSF_CONTEXT_UNIVERSE->wallclock_to_ltime(); }
#endif

ltime_t Entity::alignto(Entity* s)
{
  if(!s) ssf_throw_exception(ssf_exception::entity_nullent);
  return SSF_CONTEXT_UNIVERSE->add_alignment(this, s);
}

ltime_t Entity::makeIndependent(int p)
{
  if(p < 0) return SSF_CONTEXT_UNIVERSE->add_alignment(this, p);
  return SSF_CONTEXT_UNIVERSE->add_alignment(this, p%SSF_NPROCS);
}

Entity** Entity::coalignedEntities()
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "coalignedEntities()");

  Entity** ents = (Entity**)_ent_new_contextual_memory
    (sizeof(Entity*)*(_ent_timeline->entities.size()+1));
  int idx = 0;
  for(SSF_TML_ENT_SET::iterator iter = _ent_timeline->entities.begin();
      iter != _ent_timeline->entities.end(); iter++) {
    ents[idx++] = *iter;
  }
  ents[idx] = 0;
  return ents;
}

Process** Entity::processes()
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "processes()");

  Process** procs = (Process**)_ent_new_contextual_memory
    (sizeof(Process*)*(_ent_processes.size()+1));
  int idx = 0;
  for(SSF_ENT_PRC_SET::iterator iter = _ent_processes.begin();
      iter != _ent_processes.end(); iter++) {
    procs[idx++] = *iter;
  }
  procs[idx] = 0;
  return procs;
}

inChannel** Entity::inChannels()
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "inChannels()");

  inChannel** ics = (inChannel**)_ent_new_contextual_memory
    (sizeof(inChannel*)*(_ent_inchannels.size()+1));
  int idx = 0;
  for(SSF_ENT_IC_SET::iterator iter = _ent_inchannels.begin();
      iter != _ent_inchannels.end(); iter++) {
    ics[idx++] = *iter;
  }
  ics[idx] = 0;
  return ics;
}

outChannel** Entity::outChannels()
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "outChannels()");

  outChannel** ocs = (outChannel**)_ent_new_contextual_memory
    (sizeof(outChannel*)*(_ent_outchannels.size()+1));
  int idx = 0;
  for(SSF_ENT_OC_SET::iterator iter = _ent_outchannels.begin();
      iter != _ent_outchannels.end(); iter++) {
    ocs[idx++] = *iter;
  }
  ocs[idx] = 0;
  return ocs;
}

boolean Entity::isSimple()
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "isSimple()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "isSimple()");

  return SSF_CONTEXT_RUNNING_PROCESS->isSimple();
}

void Entity::waitOn(inChannel** waitchannels)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOn()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOn()");

  SSF_CONTEXT_RUNNING_PROCESS->waitOn(waitchannels);
}

void Entity::waitOn(inChannel* waitchannel)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOn()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOn()");

  SSF_CONTEXT_RUNNING_PROCESS->waitOn(waitchannel);
}

void Entity::waitForever()
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitForever()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitForever()");

  SSF_CONTEXT_RUNNING_PROCESS->waitForever();
}

void Entity::suspendForever()
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "suspendForever()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "suspendForever()");

  SSF_CONTEXT_RUNNING_PROCESS->suspendForever();
}

void Entity::waitFor(ltime_t waitinterval)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitFor()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitFor()");

  SSF_CONTEXT_RUNNING_PROCESS->waitFor(waitinterval);
}

void Entity::waitUntil(ltime_t when)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitUntil()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitUntil()");

  SSF_CONTEXT_RUNNING_PROCESS->waitUntil(when);
}

boolean Entity::waitOnFor(inChannel* waitchannel, ltime_t timeout)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOnFor()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOnFor()");

  return SSF_CONTEXT_RUNNING_PROCESS->waitOnFor(waitchannel, timeout);
}

boolean Entity::waitOnFor(inChannel** waitchannels, ltime_t timeout)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOnFor()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOnFor()");

  return SSF_CONTEXT_RUNNING_PROCESS->waitOnFor(waitchannels, timeout);
}

boolean Entity::waitOnUntil(inChannel* waitchannel, ltime_t when)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOnUntil()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOnUntil()");

  return SSF_CONTEXT_RUNNING_PROCESS->waitOnUntil(waitchannel, when);
}

boolean Entity::waitOnUntil(inChannel** waitchannels, ltime_t when)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOnUntil()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOnUntil()");

  return SSF_CONTEXT_RUNNING_PROCESS->waitOnUntil(waitchannels, when);
}

void Entity::waitsOn(inChannel** waitchannels)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitsOn()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitsOn()");

  SSF_CONTEXT_RUNNING_PROCESS->waitsOn(waitchannels);
}

void Entity::waitsOn(inChannel* waitchannel)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitsOn()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitsOn()");

  SSF_CONTEXT_RUNNING_PROCESS->waitsOn(waitchannel);
}

void Entity::waitOn()
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOn()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOn()");

  SSF_CONTEXT_RUNNING_PROCESS->waitOn();
}

boolean Entity::waitOnFor(ltime_t timeout)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOnFor()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOnFor()");

  return SSF_CONTEXT_RUNNING_PROCESS->waitOnFor(timeout);
}

boolean Entity::waitOnUntil(ltime_t when)
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "waitOnUntil()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "waitOnUntil()");

  return SSF_CONTEXT_RUNNING_PROCESS->waitOnUntil(when);
}

inChannel** Entity::activeChannels()
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::entity_proctxt, "activeChannels()");
  if(!_ent_coaligned_with(SSF_CONTEXT_RUNNING_PROCESS->_proc_owner))
    ssf_throw_exception(ssf_exception::entity_align, "activeChannels()");

  return SSF_CONTEXT_RUNNING_PROCESS->activeChannels();
}

const char* Entity::getenv(char* envar)
{
  const char* p;
  assert(SSF_CONTEXT_ENVIRONMENT);
  ssf_mutex_wait(&SSF_CONTEXT_ENVIRON_MUTEX);
  SSF_MAP(SSF_STRING,SSF_STRING)::iterator iter =
    SSF_CONTEXT_ENVIRONMENT->find(envar);
  if(iter != SSF_CONTEXT_ENVIRONMENT->end())
    p = (*iter).second.c_str();
  else p = 0;
  ssf_mutex_signal(&SSF_CONTEXT_ENVIRON_MUTEX);
  return p;
}

long Entity::schedule(void (Entity::*entfct)(Event*),
		      Event* evt, ltime_t delay)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "schedule()");

  if(delay < SSF_LTIME_ZERO) {
    ssf_throw_warning("Entity::schedule() negative delay, corrected");
    delay = SSF_LTIME_ZERO;
  }

  ssf_kernel_event* kevt = new ssf_kernel_event
    (SSF_CONTEXT_NOW+delay, ssf_kernel_event::EVTYPE_SCHEDULE_ENTFCT, evt);
  kevt->entity = this;
  kevt->entfct = entfct;
  kevt = _ent_insert_event(kevt);
  if(kevt) return kevt->sched_handle = _ent_add_schedule(kevt);
  else return 0;
}

long Entity::schedule(Process* proc, void (Process::*prcfct)(Event*),
		      Event* evt, ltime_t delay)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "schedule()");
  if(!proc || proc->_proc_owner != this)
    ssf_throw_exception(ssf_exception::entity_align, "schedule()");

  if(delay < SSF_LTIME_ZERO) {
    ssf_throw_warning("Entity::schedule() negative delay, corrected");
    delay = SSF_LTIME_ZERO;
  }

  ssf_kernel_event* kevt = new ssf_kernel_event
    (SSF_CONTEXT_NOW+delay, ssf_kernel_event::EVTYPE_SCHEDULE_PRCFCT, evt);
  kevt->process = proc;
  kevt->prcfct = prcfct;
  kevt = proc->_proc_owner->_ent_insert_event(kevt);
  if(kevt) return kevt->sched_handle =
	     proc->_proc_owner->_ent_add_schedule(kevt);
  else return 0;
}

boolean Entity::unschedule(long handle)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "unschedule()");

  ssf_kernel_event* kevt = _ent_delete_schedule(handle);
  if(kevt) {
    // there's a kernel event mapped to the handle
    _ent_cancel_event(kevt);
    return true;
  } else return false;
}

void Entity::schedule_nonretractable(void (Entity::*entfct)(Event*),
				     Event* evt, ltime_t delay)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "schedule_nonretract()");

  if(delay < SSF_LTIME_ZERO) {
    ssf_throw_warning("Entity::schedule_nonretract() negative delay, corrected");
    delay = SSF_LTIME_ZERO;
  }

  ssf_kernel_event* kevt = new ssf_kernel_event
    (SSF_CONTEXT_NOW+delay, ssf_kernel_event::EVTYPE_SCHEDULE_ENTFCT, evt);
  kevt->entity = this;
  kevt->entfct = entfct;
  kevt->sched_handle = 0; // non-retractable
  _ent_insert_event(kevt);
}

void Entity::schedule_nonretractable(Process* proc, void (Process::*prcfct)(Event*),
				     Event* evt, ltime_t delay)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _ent_timeline)
    ssf_throw_exception(ssf_exception::entity_align, "schedule_nonretractable()");
  if(!proc || proc->_proc_owner != this)
    ssf_throw_exception(ssf_exception::entity_align, "schedule_nonretractable()");

  if(delay < SSF_LTIME_ZERO) {
    ssf_throw_warning("Entity::schedule_nonretract() negative delay, corrected");
    delay = SSF_LTIME_ZERO;
  }

  ssf_kernel_event* kevt = new ssf_kernel_event
    (SSF_CONTEXT_NOW+delay, ssf_kernel_event::EVTYPE_SCHEDULE_PRCFCT, evt);
  kevt->process = proc;
  kevt->prcfct = prcfct;
  kevt->sched_handle = 0; // non-retractable
  _ent_insert_event(kevt);
}

void Entity::dumpData(int type, ssf_compact* dp)
{
  char buf[sizeof(ltime_t)+sizeof(int)];
  prime::ssf::int32 hasdata = dp ? 1 : 0;
#ifdef PRIME_SSF_ARCH_WINDOWS
  if(!SSF_CONTEXT_DATAFILE.write((char*)ssf_compact::serialize(_ent_serialno, buf), sizeof(int)) ||
     !SSF_CONTEXT_DATAFILE.write((char*)ssf_compact::serialize((prime::ssf::int32)type, buf), sizeof(int)) ||
     !SSF_CONTEXT_DATAFILE.write((char*)ssf_ltime_serialize(SSF_CONTEXT_NOW, buf), sizeof(ltime_t)) ||
     !SSF_CONTEXT_DATAFILE.write((char*)ssf_compact::serialize(hasdata, buf), sizeof(int)) ||
     (dp && dp->write(SSF_CONTEXT_DATAFILE) <= 0)) {
#else
  if(write(SSF_CONTEXT_DATAFILE, ssf_compact::serialize(_ent_serialno, buf), sizeof(int)) <= 0 ||
     write(SSF_CONTEXT_DATAFILE, ssf_compact::serialize((prime::ssf::int32)type, buf), sizeof(int)) <= 0 ||
     write(SSF_CONTEXT_DATAFILE, ssf_ltime_serialize(SSF_CONTEXT_NOW, buf), sizeof(ltime_t)) <= 0 ||
     write(SSF_CONTEXT_DATAFILE, ssf_compact::serialize(hasdata, buf), sizeof(int)) <= 0 ||
     (dp && dp->write(SSF_CONTEXT_DATAFILE) <= 0)) {
#endif
    char msg[256];
    int errcode = errno;
    sprintf(msg, "write error: %s", strerror(errcode));
    ssf_throw_exception(ssf_exception::entity_dumpdata, msg);
  }
}

/**
 * ssf_entity_data_enumeration implements the Enumeration class which is
 * abstract. It is used to retrieve data logs written by the user
 * during the simulation.
 */
#ifdef PRIME_SSF_ARCH_WINDOWS
class ssf_entity_data_enumeration : public prime::dml::Enumeration {
public:
  /// Constructor: reset the log files, get to the beginning.
  ssf_entity_data_enumeration() : invalid(0), /*cur_fd(-1),*/ cur_dd(0) {
    ssf_universe::next_datafile(cur_fd);
  }

  /// Destructor.
  virtual ~ssf_entity_data_enumeration() {
    if(cur_fd) cur_fd.close();
    //if(cur_fd >=0) close(cur_fd);
    if(cur_dd) delete cur_dd;
  }

  /// Move on to the next record, if the current record has been
  /// consumed and if there is a next one.
  virtual int hasMoreElements() {
    //register int i = 0;

    // no more elements if an error is found
    if(invalid) return 0;

    if(cur_dd) {
      if(retrieved) {
	// remove the previous retrieved record
	delete cur_dd;
	cur_dd = 0;
      } else {
	// the data has not been retrieved; we should not move the
	// pointer.
	return 1;
      }
    }

    int who, type, hasdata; ltime_t when;

    // can't read another record off the current file
    if(!cur_fd || !cur_fd.read((char*)&who, sizeof(int))) {
      //if(!cur_fd.eof()) { invalid = 1; return 0; } // read error
      //else { cur_fd.close(); }
      cur_fd.close();
      do {
	if(!ssf_universe::next_datafile(cur_fd)) {
	  invalid = 1; return 0;
	}
      } while(!cur_fd.read((char*)&who, sizeof(int)));
    }
    /*
    if(cur_fd < 0 || (i=read(cur_fd, &who, sizeof(int)))<=0) {
      if(cur_fd >= 0) { // file is opened
	if(i<0) { invalid = 1; return 0; } // read error
	else { close(cur_fd); } // no more record to read
      }
      do {
	cur_fd = ssf_universe::next_datafile();
	if(cur_fd < 0) { invalid = 1; return 0; }
      } while(read(cur_fd, &who, sizeof(int)) <= 0);
    }
    */

    // get all fields of the record
    if(!cur_fd.read((char*)&type, sizeof(int)) ||
       !cur_fd.read((char*)&when, sizeof(ltime_t)) ||
       !cur_fd.read((char*)&hasdata, sizeof(int))) {
    //if(read(cur_fd, &type, sizeof(int))<=0 ||
    //   read(cur_fd, &when, sizeof(ltime_t))<=0 ||
    //   read(cur_fd, &hasdata, sizeof(int))<=0) {
      invalid = 1;
      return 0;
    }
    ssf_compact* dp;
    if(hasdata) {
      dp = new ssf_compact;
      if(dp->read(cur_fd) <= 0) {
	invalid = 1;
	return 0;
      }
    } else dp = 0;

    cur_dd = new DataLog(who, type, when, dp);
    retrieved = 0;
    return 1;
  }

  // Returns the current data record. If the current one is consumed
  // already, move on to the next one. Note that the user should NOT
  // delete the record being returned.
  virtual void* nextElement() {
    if(invalid) return 0;
    if(!retrieved) {
      retrieved = 1;
      return (void*)cur_dd;
    } else {
      // the record has been retrieved; we should move on.
      if(hasMoreElements()) {
	assert(retrieved);
	//retrieved = 1;
	return (void*)cur_dd;
      } else return 0;
    }
  }

  /// No error has happened, yet.
  int invalid;

  /// The file descriptor points to the log file currently processed.
  std::ifstream cur_fd;
  //int cur_fd;

  /// The record now active in the current file.
  DataLog* cur_dd;

  /// Whether the record has been retrieved since last call to the
  /// hasMoreElements method.
  int retrieved;
}; /*ssf_entity_data_enumeration*/
#else
class ssf_entity_data_enumeration : public prime::dml::Enumeration {
public:
  int invalid;
  int cur_fd;
  ssf_entity_data* cur_dd;

  ssf_entity_data_enumeration() : invalid(0), cur_fd(-1), cur_dd(0) {}
  virtual ~ssf_entity_data_enumeration() {
    if(cur_fd >=0) close(cur_fd);
    if(cur_dd) delete cur_dd;
  }

  int hasMoreElements() {
    register int i = 0;

    prime::ssf::int32 who, type, thereisdp;
    ltime_t when;

    if(invalid) return 0;
    if(cur_dd) { delete cur_dd; cur_dd = 0; }
    if(cur_fd < 0 || (i=read(cur_fd, &who, sizeof(int)))<=0) {
      if(cur_fd >= 0) {
	if(i<0) { invalid = 1; return 0; }
	close(cur_fd);
      }
      do {
	cur_fd = ssf_universe::next_datafile();
	if(cur_fd < 0) { invalid = 1; return 0; }
      } while(read(cur_fd, &who, sizeof(int)) <= 0);
    }
    ssf_compact::deserialize(who, &who);

    if(read(cur_fd, &type, sizeof(int))<=0 ||
       read(cur_fd, &when, sizeof(ltime_t))<=0 ||
       read(cur_fd, &thereisdp, sizeof(int))<=0) {
      invalid = 1;
      return 0;
    } else {
      ssf_compact::deserialize(type, &type);
      ssf_compact::deserialize(when, &when);
      ssf_compact::deserialize(thereisdp, &thereisdp);
    }

    ssf_compact* dp;
    if(thereisdp) {
      dp = new ssf_compact;
      if(dp->read(cur_fd) <= 0) {
	invalid = 1;
	return 0;
      }
    } else dp = 0;

    cur_dd = new ssf_entity_data(who, type, when, dp);
    return 1;
  }

  virtual void* nextElement() { return (void*)cur_dd; }
}; /*ssf_entity_data_enumeration*/
#endif /*undef PRIME_SSF_ARCH_WINDOWS*/

prime::dml::Enumeration* Entity::retrieveDataDumped()
{
  ssf_universe::reset_datafiles();
  return new ssf_entity_data_enumeration();
}

char* Entity::_ent_new_contextual_memory(size_t size)
{
  assert(SSF_CONTEXT_PROCESSING_TIMELINE == _ent_timeline);
  assert(size>0);
  return _ent_timeline->new_contextual_memory(size);
}

ssf_kernel_event* Entity::_ent_insert_event(ssf_kernel_event* kevt)
{
  assert(kevt);

  if(kevt->time > SSF_CONTEXT_ENDTIME) {
    kevt->cancel();
    delete kevt;
    return 0;
  }

  kevt->entity_serialno = _ent_serialno;
  kevt->event_seqno = _ent_new_event();

  if(kevt->type == ssf_kernel_event::EVTYPE_NEWENTITY) {
    // special treatment for NEWENTITY event, we assume at the
    // creation the entity is not consolicated with the alignment.
    assert(!_ent_initevt);
    _ent_initevt = kevt;
  } else {
    if(kevt->time >= _ent_future) {
      // we need to temporarily store the event if its timestamp is no
      // less than what's given in _ent_future. In this case, we
      // store the event with timestamp equal to the difference
      // between the scheduled time and now. This is to avoid
      // situations where at the simulation initialization phase, we
      // don't know exactly what is the start time.
      kevt->time -= SSF_CONTEXT_NOW;
      _ent_evtque.push_back(kevt);
    } else {
      // safe to insert it into the timeline's event list.
      _ent_timeline->insert_event(kevt);
    }
  }
  return kevt;
}

void Entity::_ent_cancel_event(ssf_kernel_event* kevt)
{
  assert(kevt);
  // FIXME!
  kevt->cancel();
}

long Entity::_ent_add_schedule(ssf_kernel_event* kevt)
{
  assert(kevt);
  long handle = _ent_next_sched++;
  _ent_direct_sched.insert(SSF_MAKE_PAIR(handle, kevt));
  return handle;
}

ssf_kernel_event* Entity::_ent_delete_schedule(long handle)
{
  SSF_ENT_INTKEVT_MAP::iterator iter = _ent_direct_sched.find(handle);
  if(iter != _ent_direct_sched.end()) {
    ssf_kernel_event* kevt = (*iter).second;
    _ent_direct_sched.erase(iter);
    return kevt;
  } return 0;
}

Entity* Entity::_ent_create_registered_entity(const char* entname,
					       const ssf_entity_dml_param** params)
{
  if(!_ent_registered_entities) return 0;
  SSF_ENT_STRFAC_MAP::iterator iter =
    _ent_registered_entities->find(entname);
  if(iter != _ent_registered_entities->end())
    return (*(*iter).second)(params);
  else return 0;
}

boolean Entity::_ent_is_timedout()
{
  assert(SSF_CONTEXT_RUNNING_PROCESS);
  return SSF_CONTEXT_RUNNING_PROCESS->_proc_is_timedout();
}

int Entity::_ent_register_entity(char* entname, SSF_ENTITY_FACTORY factory)
{
  if(!entname) ssf_throw_exception(ssf_exception::entity_register, "empty name");
  if(!factory) ssf_throw_exception(ssf_exception::entity_register, "empty factory");
  if(!_ent_registered_entities) _ent_registered_entities = new SSF_ENT_STRFAC_MAP();
  if(!_ent_registered_entities->
     insert(SSF_MAKE_PAIR(entname, factory)).second) {
    char msg[256];
    sprintf(msg, "duplicate name: %s", entname);
    ssf_throw_exception(ssf_exception::entity_register, msg);
  }
  return 0; // return value doesn't matter
}

int Entity::_ent_set_global_wrapup(void (*wrapper)())
{
  _ent_global_wrapup = wrapper;
  return 0; // return value doesn't matter
}

void Entity::_ent_add_inchannel(inChannel* ic)
{
  assert(ic);
  _ent_inchannels.insert(ic);
  ic->_ic_serialno = -(++_ent_next_inchannel);
}

void Entity::_ent_delete_inchannel(inChannel* ic)
{
  assert(ic);
  _ent_inchannels.erase(ic);
}

void Entity::_ent_add_outchannel(outChannel* oc)
{
  assert(oc);
  _ent_outchannels.insert(oc);
  oc->_oc_serialno = -(++_ent_next_outchannel);
}

void Entity::_ent_delete_outchannel(outChannel* oc)
{
  assert(oc);
  _ent_outchannels.erase(oc);
}

#if PRIME_SSF_EMULATION
void Entity::startAll(ltime_t t1, double r)
{
  startAll(SSF_LTIME_ZERO, t1, r);
}
#else
void Entity::startAll(ltime_t t1)
{
  startAll(SSF_LTIME_ZERO, t1);
}
#endif
#if PRIME_SSF_EMULATION
void Entity::startAll(ltime_t t0, ltime_t t1, double r)
#else
void Entity::startAll(ltime_t t0, ltime_t t1)
#endif
{
  if(SSF_USE_PRIVATE(simphase) != SSF_SIMPHASE_INITIALIZATION)
    ssf_throw_exception(ssf_exception::entity_startall, "illegal call");
  //if(t0 < SSF_LTIME_ZERO || t0 > t1) {
  if(t0 > t1) {
    char msg[256];
    sprintf(msg, "invalid interval (%lf,%lf)", (double)t0, (double)t1);
    ssf_throw_exception(ssf_exception::entity_startall, msg);
  }
#if PRIME_SSF_EMULATION
  if(r < 0) {
    char msg[256];
    sprintf(msg, "invalid ltime_wallclock_ratio (=%lf)", r);
    ssf_throw_exception(ssf_exception::entity_startall, msg);
  }
#endif

  if(SSF_SELF > 0) return; // only processor 0 may call this function

  // adjust the start time if not set appropriately
#if PRIME_SSF_EMULATION
  ssf_universe::set_simulation_interval(t0, t1, r);
#else
  ssf_universe::set_simulation_interval(t0, t1);
#endif

  // calling Entity::init() methods (in parallel) and build the model.
  ssf_universe::run_setup();
  SSF_USE_PRIVATE(simphase) = SSF_SIMPHASE_RUNNING;
}

#if PRIME_SSF_EMULATION
void Entity::throttle(double ltime_wallclock_ratio,
		      boolean (*condition)(double, double, ltime_t, ltime_t, void*),
		      void* context)
{
  /*
  if(!_ent_emuable)
    ssf_throw_exception(ssf_exception::entity_throttle,
			"called for non-real-time entity");
  */
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->
    throttle(ltime_wallclock_ratio, condition, context);
}
#endif

void Entity::joinAll()
{
  if(SSF_USE_PRIVATE(simphase) != SSF_SIMPHASE_RUNNING)
    ssf_throw_exception(ssf_exception::entity_joinall);
  ssf_universe::run_continuation();
}

Process* current_process() { return (Process*)SSF_USE_PRIVATE(running_process); }

ltime_t now() { return SSF_USE_PRIVATE(now); }

void Entity::_ent_publish_inchannel(char* name, inChannel* ic, boolean global)
{
  // create a map entry from the name to published inchannel
  if(!_ent_icmap.insert(SSF_MAKE_PAIR(name, ic)).second) {
    char msg[256];
    sprintf(msg, "inChannel::publish() duplicated name %s", name);
    ssf_throw_exception(ssf_exception::entity_publish, msg);
  }
  if(global) SSF_CONTEXT_UNIVERSE->new_public_inchannel(ic);
}

void Entity::_ent_unpublish_inchannel(char* name, inChannel* ic)
{
  // the published name must be found in the map
  SSF_ENT_STRIC_MAP::iterator iter = _ent_icmap.find(name);
  if(iter != _ent_icmap.end()) // delete the entry, no longer published
    _ent_icmap.erase(iter);
  else {
    char msg[256];
    sprintf(msg, "inChannel::unpublish() %s not found", name);
    ssf_throw_exception(ssf_exception::entity_publish, msg);
  }
}

void Entity::_ent_publish_outchannel(char* name, outChannel* oc)
{
  // create a map entry from the name to published outchannel
  if(!_ent_ocmap.insert(SSF_MAKE_PAIR(name, oc)).second) {
    char msg[256];
    sprintf(msg, "outChannel::publish() duplicated name %s", name);
    ssf_throw_exception(ssf_exception::entity_publish, msg);
  }
}

void Entity::_ent_unpublish_outchannel(char* name, outChannel* oc)
{
  // the published name must be found in the map
  SSF_ENT_STROC_MAP::iterator iter = _ent_ocmap.find(name);
  if(iter != _ent_ocmap.end()) // delete the entry, no longer published
    _ent_ocmap.erase(iter);
  else {
    char msg[256];
    sprintf(msg, "outChannel::unpublish() %s not found", name);
    ssf_throw_exception(ssf_exception::entity_publish, msg);
  }
}

int Entity::_ent_preset_inchannel(const char* name, int portno)
{
  assert(portno >= 0);
  SSF_ENT_STRIC_MAP::iterator iter = _ent_icmap.find(name);
  if(iter != _ent_icmap.end()) {
    // the inchannel should have been already published
    inChannel* ic = (*iter).second;
    ic->_ic_serialno = (prime::ssf::uint32)portno; // preset by dml
    if(portno > _ent_max_preset_inport)
      _ent_max_preset_inport = portno; // recorded the largest portno
    if(!_ent_icport.insert(SSF_MAKE_PAIR(portno, ic)).second) {
      char msg[256];
      sprintf(msg, "duplicate inchannel port number: name=%s, portno=%d",
		 name, portno);
      ssf_throw_exception(ssf_exception::entity_publish, msg);
    }
    return 0; // no error
  } else return 1; // there's error, return non-zero
}

int Entity::_ent_preset_outchannel(const char* name, int portno)
{
  assert(portno >= 0);
  SSF_ENT_STROC_MAP::iterator iter = _ent_ocmap.find(name);
  if(iter != _ent_ocmap.end()) {
    // the outchannel should have been already published
    outChannel* oc = (*iter).second;
    oc->_oc_serialno = (prime::ssf::uint32)portno; // preset by dml
    if(portno > _ent_max_preset_outport)
      _ent_max_preset_outport = portno; // recorded the largest portno
    if(!_ent_ocport.insert(SSF_MAKE_PAIR(portno, oc)).second) {
      char msg[256];
      sprintf(msg, "duplicate outchannel port number: name=%s, portno=%d",
		 name, portno);
      ssf_throw_exception(ssf_exception::entity_publish, msg);
    }
    return 0; // no error
  } else return 1; // there's error, return non-zero
}

void Entity::_ent_strip_initevt()
{
  if(_ent_initevt) {
    delete _ent_initevt;
    _ent_initevt = 0;
    SSF_CONTEXT_PROCESSING_TIMELINE_ASSIGN(_ent_timeline);
    init();
  }
}

void Entity::_ent_settle_events()
{
#if SSF_SHARED_DATA_SEGMENT
  _ent_timeline->__rtx__ = __rtx__ = SSF_GET_PRIVATE_DATA;
  for(SSF_ENT_PRC_SET::iterator p_iter = _ent_processes.begin();
      p_iter != _ent_processes.end(); p_iter++)
    (*p_iter)->__rtx__ = __rtx__;
  for(SSF_ENT_IC_SET::iterator i_iter = _ent_inchannels.begin();
      i_iter != _ent_inchannels.end(); i_iter++)
    (*i_iter)->__rtx__ = __rtx__;
  for(SSF_ENT_OC_SET::iterator o_iter = _ent_outchannels.begin();
      o_iter != _ent_outchannels.end(); o_iter++)
    (*o_iter)->__rtx__ = __rtx__;
#endif

  if(!_ent_evtque.empty()) {
    for(SSF_ENT_KEVT_VECTOR::iterator iter = _ent_evtque.begin();
	iter != _ent_evtque.end(); iter++) {
      if((*iter)->type != ssf_kernel_event::EVTYPE_CANCELLED) {
	(*iter)->time += SSF_CONTEXT_NOW;
	if((*iter)->time > SSF_CONTEXT_ENDTIME) {
	  (*iter)->cancel();
	  delete (*iter);
	} else _ent_timeline->insert_event(*iter);
      } else delete (*iter);
    }
    _ent_evtque.clear();
  }
  _ent_future = SSF_LTIME_INFINITY;
}

}; // namespace ssf
}; // namespace prime

/*
 * Copyright (c) 2007-2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * noninfringement. In no event shall Florida International University be
 * liable for any claim, damages or other liability, whether in an
 * action of contract, tort or otherwise, arising from, out of or in
 * connection with the software or the use or other dealings in the
 * software.
 *
 * This software is developed and maintained by
 *
 *   The PRIME Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, FL 33199, USA
 *
 * Contact Jason Liu <liux@cis.fiu.edu> for questions regarding the use
 * of this software.
 */
