/*
 * process :- standard SSF process.
 *
 * An SSF process is part of an entity and is used to specify the
 * entity's state evolution. One can think of an SSF process as a
 * regular Unix process; it's a thread of execution interspersed with
 * wait statements. A process can wait for an event to arrive or wait
 * for a specified simulation time to pass. Each process object
 * encapsulates an SSF process. A process is started by executing a
 * procedure, which can be a method of an entity, a process, or any
 * class derived from the ssf_procedure_container class. The users can
 * define their own process classes if per-process private data is
 * needed.
 */

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

Process::Process(Entity* theowner, boolean simple) :
  ssf_procedure_container(ssf_procedure_container::PROCTYPE_PROCESS),	// this is a process
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _proc_simple(simple),			// is this simple process?
  _proc_starter(0),			// action
  _proc_state(PROCSTATE_CREATING),	// new born
  _proc_stack(0),			// empty stack
  _proc_holdevt(0),			// timeout event
  _proc_waiton(0),			// dynamic sensitivity
  _proc_static_expect(false),		// static sensitivity
  _proc_proper(0),			// reset the guard
  _proc_timedout(0)			// not timed out
{
  // common code portion for initializing the data structure
  _proc_constructor(theowner);
}

Process::Process(Entity* theowner, void (Entity::*starter)(Process*), 
		 boolean simple) : 
  ssf_procedure_container(ssf_procedure_container::PROCTYPE_PROCESS),	// this is a process
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _proc_simple(simple),			// is this simple process?
  _proc_starter(starter),		// an entity method
  _proc_state(PROCSTATE_CREATING),	// new born
  _proc_stack(0),			// empty stack
  _proc_holdevt(0),			// timeout event
  _proc_waiton(0),			// dynamic sensitivity
  _proc_static_expect(false),		// static sensitivity
  _proc_proper(0),			// reset the guard
  _proc_timedout(0)			// not timed out
{
  // common code portion for initializing the data structure
  _proc_constructor(theowner);
}

Process::~Process()
{
  assert(_proc_state != PROCSTATE_RUNNING);

  // remove this process from the entity
  _proc_owner->_ent_delete_process(this);

  // reclaim all stack frames
  while(_proc_stack) {
    ssf_procedure* p = _proc_pop_stack();
    delete p;
  }

  // remove dynamic sensitivity
  _proc_insensitize();

  // remove static sensitivity
  _proc_insensitize_static();
}

//! SSF PROCEDURE SIMPLE
void Process::action()
{
  // bootstrap this procedure...

  // prevent the user from calling this method directly
  _proc_is_proper("action");

  // suspend forever
  _proc_suspend();
}

void Process::waitOn(inChannel** waitchannels) 
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    SSF_CONTEXT_RUNNING_PROCESS->waitOn(waitchannels);
    return;
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOn");

  if(waitchannels) {
    for(int i=0; waitchannels[i]; i++) {
#if PRIME_SSF_SCRUTINY
      // check aligment: the channels must be accessible by the process
      if(!_proc_owner->_ent_coaligned_with(waitchannels[i]->_ic_owner))
	ssf_throw_exception(ssf_exception::proc_align, "waitOn()");

      // check duplications, ignore those who are
      register int j = i-1;
      while(j >= 0 && waitchannels[j] != waitchannels[i]) j--;
      if(j >= 0) continue; // there's a duplicate
#endif

      // make the process sensitive to the channel
      _proc_sensitize(waitchannels[i]);
    }
  }

  // if the list is empty, the process will suspend forever...
  _proc_suspend();
}

void Process::waitOn(inChannel* waitchannel)
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    SSF_CONTEXT_RUNNING_PROCESS->waitOn(waitchannel);
    return;
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOn");

#if PRIME_SSF_SCRUTINY
  // check aligment: the channels must be accessible by the process
  if(waitchannel && !_proc_owner->_ent_coaligned_with(waitchannel->_ic_owner))
    ssf_throw_exception(ssf_exception::proc_align, "waitOn()");
#endif

  // make the process sensitive to the channel
  if(waitchannel) _proc_sensitize(waitchannel);

  // if the argument is NULL, the process will suspend forever...
  _proc_suspend();
}

void Process::waitForever()
{ 
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    SSF_CONTEXT_RUNNING_PROCESS->waitForever();
    return;
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitForever");

  // if the state is not running, it must be a simple process and it
  // must be the case that the process has been suspended previously.
  if(_proc_state != PROCSTATE_RUNNING) {
    assert(_proc_simple);
    ssf_throw_exception(ssf_exception::proc_multiwait);
  }

  // kill the running process
  SSF_CONTEXT_PROCESSING_TIMELINE->deactivate_process
    (PROCSTATE_TERMINATING);
}

void Process::suspendForever()
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    SSF_CONTEXT_RUNNING_PROCESS->suspendForever();
    return;
  }

  // the method should not be called outside procedure context
  _proc_is_proper("suspendForever");

  // suspend the process forever...
  _proc_suspend();
}

void Process::waitFor(ltime_t waitinterval) 
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    SSF_CONTEXT_RUNNING_PROCESS->waitFor(waitinterval);
    return;
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitFor");

  // correct negative delay
  if(waitinterval < SSF_LTIME_ZERO) {
    ssf_throw_warning("prime::ssf::Process::waitFor() negative wait!!!");
    waitinterval = SSF_LTIME_ZERO;
  }

  // wait for timeout
  _proc_holdfor(waitinterval);
}

void Process::waitUntil(ltime_t when)
{
  // applies only to the current running process; it is possible
  // a method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    SSF_CONTEXT_RUNNING_PROCESS->waitUntil(when);
    return;
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitUntil");

  // correct negative wait
  if(when < SSF_CONTEXT_NOW) {
    ssf_throw_warning("prime::ssf::Process::waitUntil() negative wait!!!");
    when = SSF_CONTEXT_NOW;
  }

  // wait for timeout
  _proc_holduntil(when);
}

boolean Process::waitOnFor(inChannel** waitchannels, ltime_t timeout) 
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    return SSF_CONTEXT_RUNNING_PROCESS->waitOnFor(waitchannels, timeout);
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOnFor");

  // correct negative wait
  if(timeout < SSF_LTIME_ZERO) {
    ssf_throw_warning("prime::ssf::Process::waitOnFor() negative wait!!!");
    timeout = SSF_LTIME_ZERO;
  }

  if(waitchannels) {
    for(int i=0; waitchannels[i]; i++) {
#if PRIME_SSF_SCRUTINY
      // check aligment: the channels must be accessible by the process
      if(!_proc_owner->_ent_coaligned_with(waitchannels[i]->_ic_owner))
	ssf_throw_exception(ssf_exception::proc_align, "waitOnFor()");

      // check duplications, ignore those who are
      register int j = i-1;
      while(j >= 0 && waitchannels[j] != waitchannels[i]) j--;
      if(j >= 0) continue; // there's a duplicate
#endif

      // make the process sensitive to the channel
      _proc_sensitize(waitchannels[i]);
    }
  }

  // set wait for timeout
  _proc_holdfor(timeout);

  // the return value here doesn't matter; the embedded code will fold
  // up the program stack quickly.
  return true;
}

boolean Process::waitOnFor(inChannel* waitchannel, ltime_t timeout) 
{
  // applies only to the current running process; it is possible
  // a method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    return SSF_CONTEXT_RUNNING_PROCESS->waitOnFor(waitchannel, timeout);
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOnFor");

  // correct negative wait
  if(timeout < SSF_LTIME_ZERO) {
    ssf_throw_warning("prime::ssf::Process::waitOnFor() negative wait!!!");
    timeout = SSF_LTIME_ZERO;
  }

#if PRIME_SSF_SCRUTINY
  // check aligment: the channels must be accessible by the process
  if(waitchannel && !_proc_owner->_ent_coaligned_with(waitchannel->_ic_owner))
    ssf_throw_exception(ssf_exception::proc_align, "waitOnFor()");
#endif

  // make the process sensitive to the channel
  if(waitchannel) _proc_sensitize(waitchannel);

  // set wait for timeout
  _proc_holdfor(timeout);

  // the return value here doesn't matter; the embedded code will fold
  // up the program stack quickly.
  return true;
}

boolean Process::waitOnUntil(inChannel** waitchannels, ltime_t when) 
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    return SSF_CONTEXT_RUNNING_PROCESS->waitOnUntil(waitchannels, when);
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOnUntil");

  // correct negative wait
  if(when < SSF_CONTEXT_NOW) {
    ssf_throw_warning("prime::ssf::Process::waitOnUntil() negative wait!!!");
    when = SSF_CONTEXT_NOW;
  }

  if(waitchannels) {
    for(int i=0; waitchannels[i]; i++) {
#if PRIME_SSF_SCRUTINY
      // check aligment: the channels must be accessible by the process
      if(!_proc_owner->_ent_coaligned_with(waitchannels[i]->_ic_owner))
	ssf_throw_exception(ssf_exception::proc_align, "waitOnUntil()");

      // check duplications, ignore those who are
      register int j = i-1;
      while(j >= 0 && waitchannels[j] != waitchannels[i]) j--;
      if(j >= 0) continue; // there's a duplicate
#endif

      // make the process sensitive to the channel
      _proc_sensitize(waitchannels[i]);
    }
  }

  // set wait for timeout
  _proc_holduntil(when);

  // the return value here doesn't matter; the embedded code will wrap
  // up the program stack quickly.
  return true;
}

boolean Process::waitOnUntil(inChannel* waitchannel, ltime_t when) 
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    return SSF_CONTEXT_RUNNING_PROCESS->waitOnUntil(waitchannel, when);
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOnUntil");
 
  // correct negative wait
  if(when < SSF_CONTEXT_NOW) {
    ssf_throw_warning("prime::ssf::Process::waitOnUntil() negative wait!!!");
    when = SSF_CONTEXT_NOW;
  }

#if PRIME_SSF_SCRUTINY
  // check aligment: the channels must be accessible by the process
  if(waitchannel && !_proc_owner->_ent_coaligned_with(waitchannel->_ic_owner))
    ssf_throw_exception(ssf_exception::proc_align, "waitOnUntil()");
#endif

  // make the process sensitive to the channel
  if(waitchannel) _proc_sensitize(waitchannel);

  // set wait for timeout
  _proc_holduntil(when);

  // the return value here doesn't matter; the embedded code will fold
  // up the program stack quickly.
  return true;
}

void Process::waitsOn(inChannel** waitchannels)
{
  // clean up the previous static set of in-channels
  _proc_insensitize_static();

  if(waitchannels) {
    for(int i=0; waitchannels[i]; i++) {
#if PRIME_SSF_SCRUTINY
      // check aligment: the channels must be accessible by the process
      if(!_proc_owner->_ent_coaligned_with(waitchannels[i]->_ic_owner))
	ssf_throw_exception(ssf_exception::proc_align, "waitsOn()");

      // check duplications, ignore those who are
      register int j = i-1;
      while(j >= 0 && waitchannels[j] != waitchannels[i]) j--;
      if(j >= 0) continue; // there's a duplicate
#endif

      // make the process statically sensitive to the channel
      _proc_sensitize_static(waitchannels[i]);
    }
  }
}

void Process::waitsOn(inChannel* waitchannel)
{
  // clean up the previous static set of in-channels
  _proc_insensitize_static();

  // make the process statically sensitive to the channel
  if(waitchannel) _proc_sensitize_static(waitchannel);
}

void Process::waitOn() 
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    SSF_CONTEXT_RUNNING_PROCESS->waitOn();
    return;
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOn");

  // set to wait on static channels...
  _proc_static_expect = true;
  _proc_suspend();
}

boolean Process::waitOnFor(ltime_t timeout)
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    return SSF_CONTEXT_RUNNING_PROCESS->waitOnFor(timeout);
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOnFor");

  // correct negative wait
  if(timeout < SSF_LTIME_ZERO) {
    ssf_throw_warning("prime::ssf::Process::waitOnFor() negative wait!!!");
    timeout = SSF_LTIME_ZERO;
  }

  _proc_static_expect = true;
  _proc_holdfor(timeout);

  return true; // the return value here doesn't matter 
}

boolean Process::waitOnUntil(ltime_t when) 
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    return SSF_CONTEXT_RUNNING_PROCESS->waitOnUntil(when);
  }

  // the method should not be called outside procedure context
  _proc_is_proper("waitOnUntil");
 
  // correct negative wait
  if(when < SSF_CONTEXT_NOW) {
    ssf_throw_warning("prime::ssf::Process::waitOnUntil() negative wait!!!");
    when = SSF_CONTEXT_NOW;
  }

  _proc_static_expect = true; 
  _proc_holduntil(when);

  return true; // the return value here doesn't matter 
}

inChannel** Process::activeChannels()
{
  // applies only to the current running process; it is possible a
  // method of the process object is used by another process.
  if(SSF_CONTEXT_RUNNING_PROCESS != this) {
    return SSF_CONTEXT_RUNNING_PROCESS->activeChannels();
  }

  // the method should not be called outside procedure context
  _proc_is_proper("activeChannels");
 
  inChannel** ics = (inChannel**)_proc_owner->_ent_new_contextual_memory
    (sizeof(inChannel*)*(_proc_active_inchannels.size()+1));
  int idx = 0;
  for(SSF_PRC_ICBOOL_MAP::iterator iter = _proc_active_inchannels.begin();
      iter != _proc_active_inchannels.end(); iter++)
    ics[idx++] = (*iter).first;
  ics[idx] = 0;
  return ics;
}

ltime_t Process::now() { return SSF_CONTEXT_NOW; }

void Process::_proc_is_proper(const char* name)
{
  if(!_proc_proper) ssf_throw_exception(ssf_exception::proc_pcall, name);
  if(!_proc_simple && !_proc_stack) ssf_throw_exception(ssf_exception::proc_simple);
}

void Process::_proc_push_stack(ssf_procedure* proc)
{
  assert(proc);
  proc->_proc_owner = this;
  proc->_proc_parent = _proc_stack;
  _proc_stack = proc;
}

ssf_procedure* Process::_proc_pop_stack()
{
  assert(_proc_stack);
  ssf_procedure* p = _proc_stack;
  _proc_stack = p->_proc_parent;
  return p;
}

ssf_procedure* Process::_proc_create_procedure_action()
{ 
  return new ssf_procedure(this);
}

void Process::_proc_constructor(Entity* theowner)
{
  if(!theowner) ssf_throw_exception(ssf_exception::proc_owner);

  // connect to the owner entity
  _proc_owner = theowner;
  _proc_owner->_ent_add_process(this);

  // schedule an event for the init method
  ssf_kernel_event* kevt = new ssf_kernel_event
    (SSF_CONTEXT_NOW, ssf_kernel_event::EVTYPE_NEWPROCESS);
  kevt->process = this;
  _proc_owner->_ent_insert_event(kevt);
}

void Process::_proc_sensitize(inChannel* ic)
{
  // add a new wait-node into the cross-link
  ssf_wait_node* wnode = new ssf_wait_node;
  wnode->p = this;
  wnode->ic = ic;
  wnode->next_p = _proc_waiton;
  _proc_waiton = wnode;
  ic->_ic_set_sensitive(wnode);
}

void Process::_proc_insensitize()
{
  // delete the wait-nodes of this process one by one
  while(_proc_waiton) {
    ssf_wait_node* n = _proc_waiton->next_p;
    assert(_proc_waiton->p == this);
    _proc_waiton->ic->_ic_set_insensitive(_proc_waiton);
    delete _proc_waiton;
    _proc_waiton = n;
  }
}

void Process::_proc_sensitize_static(inChannel* ic)
{
  assert(ic);
  ic->_ic_set_sensitive_static(this);
  _proc_static_channels.push_back(ic);
}

void Process::_proc_insensitize_static()
{
  if(!_proc_static_channels.empty()) {
    for(SSF_PRC_IC_VECTOR::iterator iter = _proc_static_channels.begin();
	iter != _proc_static_channels.end(); iter++)
      (*iter)->_ic_set_sensitive_static(this);
    _proc_static_channels.clear();
  }
}

void Process::_proc_holdfor(ltime_t delay)
{
  _proc_holduntil(SSF_CONTEXT_NOW+delay);
}

void Process::_proc_holduntil(ltime_t arise)
{
  assert(SSF_CONTEXT_NOW <= arise);
  ssf_kernel_event* kevt = new ssf_kernel_event
    (arise, ssf_kernel_event::EVTYPE_TIMEOUT);
  kevt->process = this;
  _proc_holdevt = _proc_owner->_ent_insert_event(kevt);
  _proc_suspend();
}

void Process::_proc_execute()
{
  SSF_CONTEXT_RUNNING_PROCESS_ASSIGN(this);
  _proc_activate();

  if(_proc_state == PROCSTATE_TERMINATING) {
    wrapup();
    delete this;
  } else {
    if(_proc_simple) {
      assert(!_proc_stack);
#if PRIME_SSF_SCRUTINY
      if(_proc_state != PROCSTATE_WAITING)
	ssf_throw_exception(ssf_exception::proc_nowait);
#endif
      // returning from simple process means suspension
      SSF_CONTEXT_PROCESSING_TIMELINE->
	deactivate_process(PROCSTATE_WAITING);
    }
  }
}

void Process::_proc_activate()
{
  /* Used to enforce error checking, so that procedures can be called
     only from this function. The translated procedure will call
     _proc_is_proper() function at its beginning to see if
     _proc_proper has been set, or error will be reported. Simple
     processes do not have this capability because we can't embed code
     for checking this condition. However, the wait methods are all
     embedded with this error checking step to be sure that they are
     called in a prcedure context. */
  _proc_proper++;

  if(!_proc_stack) {
    if(_proc_starter) (_proc_owner->*_proc_starter)(this);
    else action();
  } else {
    assert(_proc_stack->_proc_owner == this);
    if(!_proc_stack->_proc_ptr) {
#if PRIME_SSF_SCRUTINY
      if(!_proc_owner->_ent_coaligned_with
	 (((Process*)_proc_stack->_proc_focus)->_proc_owner))
	ssf_throw_exception(ssf_exception::proc_aligncall);
#endif
      ((Process*)_proc_stack->_proc_focus)->action();
    } else {
#if PRIME_SSF_SCRUTINY
      if(_proc_stack->_proc_focus->_proc_classid ==
	 ssf_procedure_container::PROCTYPE_ENTITY &&
	 !_proc_owner->_ent_coaligned_with((Entity*)_proc_stack->_proc_focus) ||
	 _proc_stack->_proc_focus->_proc_classid == 
	 ssf_procedure_container::PROCTYPE_PROCESS &&
	 !_proc_owner->_ent_coaligned_with(((Process*)_proc_stack->_proc_focus)->_proc_owner)) {
	ssf_throw_exception(ssf_exception::proc_aligncall);
      }
#endif
      (_proc_stack->_proc_focus->*(_proc_stack->_proc_ptr))(this);
    }
  }

  _proc_proper--;
}

void Process::_proc_suspend()
{
  assert(SSF_CONTEXT_RUNNING_PROCESS == this);

#if PRIME_SSF_SCRUTINY
  if(_proc_state != PROCSTATE_RUNNING) {
    assert(_proc_simple);
    ssf_throw_exception(ssf_exception::proc_multiwait);
  }
#endif

  if(_proc_simple) _proc_state = PROCSTATE_WAITING;
  else {
    SSF_CONTEXT_PROCESSING_TIMELINE->
      deactivate_process(PROCSTATE_WAITING);
  }
}

void Process::_proc_terminate()
{
  wrapup();
  delete this;
}

void Process::_proc_cleanup_active_inchannels()
{
  if(!_proc_active_inchannels.empty()) 
    _proc_active_inchannels.clear();
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
