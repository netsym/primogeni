/*
 * inchannel :- standard SSF in-channel interface.
 * 
 * An SSF in-channel is the receiving end of the communication link
 * between entities. Messages, known as events, are traveling from an
 * out-channel to all in-channels that are mapped to the
 * out-channel. Processes can wait on an in-channel or a list of
 * in-channels for events to arrive. Events that arrive at an
 * in-channel on which no process is waiting are discarded by the
 * kernel implicitly.
 */

#include <assert.h>
#include <string.h>

#if PRIME_SSF_EMULATION
#include <sys/types.h>
#include <pthread.h>
#include <signal.h>
#endif

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

inChannel::inChannel(Entity* theowner) :
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _ic_owner(theowner), // set the owner of the in-channel
  _ic_name(0),         // clear the name and remain anonymous
#if PRIME_SSF_EMULATION
  _ic_threadfct(0),    // not a special channel
  _ic_threadctx(0),    // no data to thread function if special
  _ic_thread(0),       // reader thread is not running
  _ic_mapnode(0),      // mapping from real device
#endif
  _ic_sched(0),        // initialize for _ic_sched_arrival()
  _ic_wait_head(0), _ic_wait_tail(0) // clean up waiting processes
{
  if(!theowner) ssf_throw_exception(ssf_exception::ic_owner);

  // register with the owner entity (which will assign my serial no.)
  _ic_owner->_ent_add_inchannel(this);
}

inChannel::inChannel(char* thename, Entity* theowner) :
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _ic_owner(theowner), // set the owner of the channel
  _ic_name(0),         // clear the name and remain anonymous
#if PRIME_SSF_EMULATION
  _ic_threadfct(0),    // not a special channel
  _ic_threadctx(0),    // no data to thread function if special
  _ic_thread(0),       // reader thread is not running
  _ic_mapnode(0),      // mapping from real device
#endif
  _ic_sched(0),        // initialize for _ic_sched_arrival()
  _ic_wait_head(0), _ic_wait_tail(0) // clean up waiting processes
{
  if(!theowner) ssf_throw_exception(ssf_exception::ic_owner);

  // register with the owner entity (which will assign my serial no.)
  _ic_owner->_ent_add_inchannel(this);

  // publish the channel with a name
  if(thename) publish(thename);
}

inChannel::~inChannel()
{
  assert(!_ic_sched);
  assert(!_ic_wait_head);
  assert(!_ic_wait_tail);
  assert(_ic_static_procs.empty());
  assert(_ic_active_events.empty());

  // terminate the reader thread if it's still running
  /*
  if(_ic_threadfct && _ic_thread) {
    thread_delete(_ic_thread);
    delete _ic_thread;
  }
  */
  //if(_ic_mapnode) delete _ic_mapnode; // it's a permanent object

  // if the channel is published, unpublish it
  if(_ic_name) {
    _ic_owner->_ent_unpublish_inchannel(_ic_name, this);
    delete[] _ic_name;
    _ic_name = 0;
  }

  // deregister with the owner entity
  _ic_owner->_ent_delete_inchannel(this);
}

Event** inChannel::activeEvents()
{
  if(!SSF_CONTEXT_RUNNING_PROCESS)
    ssf_throw_exception(ssf_exception::ic_proctxt);
  if(_ic_owner->_ent_timeline != SSF_CONTEXT_PROCESSING_TIMELINE)
    ssf_throw_exception(ssf_exception::ic_align);

  SSF_PRC_ICBOOL_MAP::iterator iter = 
    SSF_CONTEXT_RUNNING_PROCESS->_proc_active_inchannels.find(this);
  if(iter != SSF_CONTEXT_RUNNING_PROCESS->_proc_active_inchannels.end()) {
    if((*iter).second) return 0;
    else {
      (*iter).second = true;
      Event** e = (Event**)_ic_owner->_ent_new_contextual_memory
	((_ic_active_events.size()+1)*sizeof(Event*));
      int idx = 0;
      for(SSF_IC_EVT_VECTOR::iterator e_iter = _ic_active_events.begin();
	  e_iter != _ic_active_events.end(); e_iter++)
	e[idx++] = *e_iter;
      e[idx] = 0;
      return e;
    }
  } else return 0;
}

void inChannel::_ic_set_sensitive(ssf_wait_node* wnode)
{
  // put the node into the double-linked list
  wnode->prev_c = 0;
  wnode->next_c = _ic_wait_head;
  if(_ic_wait_head) {
    _ic_wait_head->prev_c = wnode;
    _ic_wait_head = wnode;
  } else _ic_wait_head = _ic_wait_tail = wnode;
}

void inChannel::_ic_set_insensitive(ssf_wait_node* wnode)
{
  // get the node off the double-linked list
  if(wnode->prev_c) wnode->prev_c->next_c = wnode->next_c;
  else _ic_wait_head = wnode->next_c;
  if(wnode->next_c) wnode->next_c->prev_c = wnode->prev_c;
  else _ic_wait_tail = wnode->prev_c;
}

void inChannel::_ic_set_sensitive_static(Process* proc)
{
  _ic_static_procs.insert(proc);
}

void inChannel::_ic_set_insensitive_static(Process* proc)
{
  _ic_static_procs.erase(proc);
}

void inChannel::_ic_schedule_arrival(ssf_kernel_event* kevt)
{
  ssf_timeline* tl = _ic_owner->_ent_timeline;
  assert(tl == SSF_CONTEXT_PROCESSING_TIMELINE);

  _ic_sched++;
  if(_ic_sched == 1) { // only when this method is called for the first time
    // check permanent sensitivity first
    for(SSF_IC_PRC_SET::iterator iter = _ic_static_procs.begin();
	iter != _ic_static_procs.end(); iter++) {
      Process* p = *iter;
      if(p->_proc_static_expect) { // if the process is expecting event arrivals
	assert(!p->_proc_waiton);
	// unblock the process, cancel the timeout event if there is
	p->_proc_active_inchannels.insert(SSF_MAKE_PAIR(this,false));
	p->_proc_timedout = 0;
	if(p->_proc_holdevt) {
	  p->_proc_owner->_ent_cancel_event(p->_proc_holdevt);
	  p->_proc_holdevt = 0;
	}
	tl->activate_process(p);
      }
    }

    // scan through the temporary sensitive list
    ssf_wait_node* wnode = _ic_wait_head;
    while(wnode) {
      assert(wnode->ic == this);
      ssf_wait_node* wnode_next = wnode->next_c; // remember the next one
      Process* p = wnode->p;
      
      // unblock the process, cancel the timeout event if there is
      p->_proc_active_inchannels.insert(SSF_MAKE_PAIR(this,false));
      p->_proc_timedout = 0;
      if(p->_proc_holdevt) {
	p->_proc_owner->_ent_cancel_event(p->_proc_holdevt);
	p->_proc_holdevt = 0;
      }
      tl->activate_process(p);

      wnode = wnode_next; // go to the next waiting process
    }

    tl->active_inchannels.push_back(this);
  }

  if(kevt->usrdat)
    _ic_active_events.push_back(kevt->usrdat->_evt_sys_reference());
}

void inChannel::_ic_cleanup_active_events()
{
  if(!_ic_active_events.empty()) {
    for(SSF_IC_EVT_VECTOR::iterator e_iter = _ic_active_events.begin();
	e_iter != _ic_active_events.end(); e_iter++) {
      (*e_iter)->_evt_sys_unreference();
    }
    _ic_active_events.clear();
  }
  _ic_sched = 0;
}

prime::ssf::uint32 inChannel::portno()
{
  if(_ic_serialno >= 0) return _ic_serialno;
  else return _ic_owner->_ent_max_preset_inport-_ic_serialno;
}

void inChannel::publish(char* extname)
{
  if(extname) {
#if PRIME_SSF_EMULATION
    if(_ic_threadfct) ssf_throw_exception(ssf_exception::ic_pubreal);
#endif
    if(_ic_name) {
      char msg[256];
      sprintf(msg, "[\"%s\"->\"%s\"]", _ic_name, extname);
      ssf_throw_exception(ssf_exception::ic_duppub, msg);
    }
    int len = strlen(extname);
    _ic_name = new char[len+1];
    strcpy(_ic_name, extname);
    _ic_owner->_ent_publish_inchannel(_ic_name, this);
  }
}

/*
void inChannel::unpublish()
{
  if(_ic_name) {
    _ic_owner->_ent_unpublish_inchannel(_ic_name, this);
    delete[] _ic_name;
    _ic_name = 0;
  }
}
*/

void inChannel::dml_publish(char* extname)
{
  if(extname) {
#if PRIME_SSF_EMULATION
    if(_ic_threadfct) ssf_throw_exception(ssf_exception::ic_pubreal);
#endif
    if(_ic_name) {
      char msg[256];
      sprintf(msg, "[\"%s\"->\"%s\"]", _ic_name, extname);
      ssf_throw_exception(ssf_exception::ic_duppub, msg);
    }
    int len = strlen(extname);
    _ic_name = new char[len+1];
    strcpy(_ic_name, extname);
    _ic_owner->_ent_publish_inchannel(_ic_name, this, false);
  }
}

#if PRIME_SSF_EMULATION
inChannel::inChannel(Entity* theowner, void (*tf)(inChannel*, void*),
		     void* ctx, ltime_t mapdelay) :
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _ic_owner(theowner), // set the owner of the channel
  _ic_name(0),         // clear the name and remain anonymous
  _ic_threadfct(tf),   // this is a special channel
  _ic_threadctx(ctx),  // data to the thread function
  _ic_thread(0),       // reader thread is not running for now
  _ic_sched(0),        // initialize for _ic_sched_arrival()
  _ic_wait_head(0), _ic_wait_tail(0) // clean up waiting processes
{
  if(!theowner) ssf_throw_exception(ssf_exception::ic_owner);

  // register with the owner entity (which will assign my serial no.)
  _ic_owner->_ent_add_inchannel(this);

  // create the map node for connecting the real device
  _ic_mapnode = new ssf_map_node;
  _ic_mapnode->ic = this;
  _ic_mapnode->map_delay = mapdelay;
  _ic_mapnode->prev = _ic_mapnode->next = 0;
}

void inChannel::putRealEvent(Event* evt, double write_delay_in_real_sec,
			     void (*cb)(ltime_t, void*), void* usrdat)
{
  if(!_ic_threadfct) 
    ssf_throw_exception(ssf_exception::ic_putreal,
			"called for non-real-time in-channel");
  if(write_delay_in_real_sec < 0)
    ssf_throw_exception(ssf_exception::ic_putreal, "negative write delay");

  if(evt) {
    ssf_universe* myuniv = ((ssf_logical_process*)_ic_owner->_ent_timeline)->universe;
    ssf_kernel_event* kevt = new ssf_kernel_event
      (_ic_mapnode->map_delay+ltime_t
       (write_delay_in_real_sec*myuniv->ltime_wallclock_ratio),
       ssf_kernel_event::EVTYPE_INCHANNEL, evt);
    kevt->mapnode = _ic_mapnode;
    myuniv->insert_real_time_event(kevt, cb, usrdat);
    ssf_thread_kill(_ic_parent_pid, SIGALRM);
  }
}

void inChannel::putVirtualEvent(Event* evt, ltime_t when,
				void (*cb)(ltime_t, void*), void* usrdat)
{
  if(!_ic_threadfct)
    ssf_throw_exception(ssf_exception::ic_putvirt,
			"called for non-real-time in-channel");
  if(evt) {
    ssf_universe* myuniv = ((ssf_logical_process*)_ic_owner->_ent_timeline)->universe;
    ssf_kernel_event* kevt = new ssf_kernel_event
      (when, ssf_kernel_event::EVTYPE_INCHANNEL, evt);
    kevt->mapnode = _ic_mapnode;
    myuniv->insert_virtual_time_event(kevt, cb, usrdat);
    ssf_thread_kill(_ic_parent_pid, SIGALRM);
  }
}

void inChannel::_ic_start_thread(inChannel* ic)
{
  //#if SSF_USE_THREADS
  //SSF_SET_PRIVATE_DATA(ic->__rtx__);
  //#endif

  ssf_arena_init(ic->_ic_pid);
  ssf_map_private_segments(ic->_ic_pid);
#if PRIME_SSF_QUICKMEM
  ssf_quickmem_init();
#endif

  (*ic->_ic_threadfct)(ic, ic->_ic_threadctx);
}

#endif /*PRIME_SSF_EMULATION*/

}; // namespace ssf
}; // namespace prime

/*
 * 
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
