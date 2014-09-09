/*
 * procedure :- activation frame for a procedure in the heap.
 *
 * Each procedure has a stack frame created in the program heap so
 * that the procedure call chain will be persistent even after the
 * function returns all the way back to the thread scheduler.
 */

#include <assert.h>
#include <string.h>

#include "ssf.h"

namespace prime {
namespace ssf {

ssf_procedure::ssf_procedure(Process* focus) :
  _proc_owner(0), _proc_entry(0), _proc_parent(0),
  _proc_ptr(0), _proc_focus(focus),
  _proc_retaddr(0), _proc_retsize(0) {}

ssf_procedure::ssf_procedure(void (ssf_procedure_container::*proc)(Process*), 
		     ssf_procedure_container *focus, 
		     void *retaddr, int retsize) :
  _proc_owner(0), _proc_entry(0), _proc_parent(0),
  _proc_ptr(proc), _proc_focus(focus),
  _proc_retaddr(retaddr), _proc_retsize(retsize) {}

ssf_procedure::~ssf_procedure() {}

void ssf_procedure::call_procedure(ssf_procedure *proc)
{
  // put the procedure into the call chain
  proc->_proc_owner = _proc_owner;
  proc->_proc_parent = this;

  // push the procedure on top of the stack
  assert(_proc_owner->_proc_stack == this);
  _proc_owner->_proc_stack = proc;

  // call the function
  _proc_owner->_proc_activate();
}

int ssf_procedure::call_suspended()
{
  return _proc_owner->_proc_state != Process::PROCSTATE_RUNNING;
}

void ssf_procedure::call_return(void *retval)
{
  // copy the return value to parent's address space
  if(retval && _proc_retaddr) {
    assert(_proc_retsize > 0);
    memcpy(_proc_retaddr, retval, _proc_retsize);
  }

  if(_proc_parent) {
    // if it's in a sub-procedure, simply pop the stack and reclaim
    // the current procedure frame.
    _proc_owner->_proc_stack = _proc_parent;
    delete this;
  } else {
    // if it's the starting procedure of the process, just reset the
    // entry point so that the process would restart from the
    // beginning.
    _proc_entry = 0;
  }
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

/*
 * $Id$
 */
