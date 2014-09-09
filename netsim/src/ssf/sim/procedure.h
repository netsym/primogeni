/*
 * procedure :- activation frame for a procedure in the heap.
 *
 * Each procedure has a stack frame created in the program heap so
 * that the procedure call chain will be persistent even after the
 * function returns all the way back to the thread scheduler.
 */

#ifndef __PRIME_SSF_PROCEDURE_H__
#define __PRIME_SSF_PROCEDURE_H__

#ifndef __PRIME_SSF_INTERNAL_H__
#error "procedure.h must be included by internal.h"
#endif

namespace prime {
namespace ssf {

class ssf_procedure_container;

/*
 * This class represents an activation frame in the program heap for
 * each procedure. We use quick object to make memory allocation and
 * deallocation operations faster since calling subroutines could be a
 * frequent operation in the model.
 */
class ssf_procedure : public ssf_quickobj {
 public:
  /*
   * The constructor is used specially for procedures, whose body is
   * the Process::action() method.
   */
  ssf_procedure(Process* focus);

  /*
   * The constructor is used for procedures whose body is a method of
   * either an entity, or a process, or a generic ssf_procedure_container
   * subclass.
   */
  ssf_procedure(void (ssf_procedure_container::*func)(Process*), 
	    ssf_procedure_container* focus, void* retaddr = 0, 
	    int retsize = 0);

  // The destructor.
  virtual ~ssf_procedure();

  /*
   * This method is called from the embedded code where we encounter a
   * procedure call. A new procedure frame has been created and passed
   * as an argument to this method. The procedure frame will be pushed
   * onto the heap stack and the procedure function will be invoked.
   */
  void call_procedure(ssf_procedure* proc);
  
  /*
   * This method returns true if the process is suspended on a wait
   * statement, in which case the embedded code will quickly return
   * the control back to the thread scheduler by unfolding the program
   * call stack.
   */
  int call_suspended();

  /*
   * The method installs the return value into the parent's procedure
   * frame.
   */
  void call_return(void* retval = 0);

 public:
  // The owner process of this procedure.
  Process* _proc_owner;

  /*
   * Similar to a program counter; this marks the current entry point
   * of the procedure (so that the user thread can resume execution
   * after suspension).
   */
  int _proc_entry;

  // Points to the parent procedure in the heap stack.
  ssf_procedure* _proc_parent;

  // Points to the method represented by this procedure frame.
  void (ssf_procedure_container::*_proc_ptr)(Process*);

  /*
   * The container object that owns the procedure method; it can be an
   * entity, a process, or a generic ssf_procedure_container subclass.
   */
  ssf_procedure_container* _proc_focus;

  /*
   * This is the address to hold the return value in the parent
   * procedure. NULL if the procedure is of type void.
   */
  void* _proc_retaddr;
  
  // Size of the return value.
  int _proc_retsize;

}; /*ssf_procedure*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_PROCEDURE_H__*/

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
