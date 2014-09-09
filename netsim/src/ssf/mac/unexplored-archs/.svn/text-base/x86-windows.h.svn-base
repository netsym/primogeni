/*
 * x86-windows :- hardware support for windows configuration.
 */

#ifdef PRIME_SSF_ARCH_WINDOWS

#ifndef __PRIME_SSF_MACHINE_WINDOWS_H__
#define __PRIME_SSF_MACHINE_WINDOWS_H__

#ifndef __PRIME_SSF_MACHINES_H__
#error "ERROR: x86-windows.h must not be included explicitly"
#endif

#include <stdio.h>

#define SSF_SHARED_DATA_SEGMENT 1
#define SSF_SHARED_HEAP_SEGMENT 1

#define SSF_GET_PRIVATE_DATA \
  ((struct prime::ssf::private_segment*)SSF_USE_SHARED_RO(private_segment))

// padding to avoid false sharing
#define SSF_CACHE_LINE_SIZE 64
#define SSF_CACHE_LINE_PADDING \
  char __ssf_dummy_for_cache_line_padding[SSF_CACHE_LINE_SIZE]

namespace prime {
namespace ssf {

  /*
   * Memory management.
   */

  /*
   * This function creates a data segment for shared global
   * variables. Nothing to be done here.
   */
  extern void ssf_map_shared_data();

  /*
   * This function creates a data segment for private global
   * variables. Private means each process has its own copy of the
   * data. Nothing to be done here.
   */
  extern void ssf_map_private_data(int pid, size_t size);

  /*
   * This function creates the shared segment for the heap (which we
   * call arena) and the global shared data section. A pointer to the
   * shared segment is returned.
   */
  extern void* ssf_create_shared_segment(size_t size);

  /*
   * Mutual exclusion.
   */

  /*
   * An integer is used as the lock variable.
   */
  typedef volatile int ssf_mutex;

  /*
   * Each lock variable must be initialized. It has to be in a
   * unlocked state.
   */
  extern void ssf_mutex_init(ssf_mutex* lock);

  /*
   * Aquire the lock. The process will suspend until the lock is
   * acquired.
   */
  extern void ssf_mutex_wait(ssf_mutex* lock);

  /*
   * Release the lock. This may unblock another process waiting on the
   * same lock.
   */
  extern void ssf_mutex_signal(ssf_mutex* lock);

  /*
   * Try to acquire the lock. If it's unlocked, the process acquires
   * the lock and the function returns 1. If it's already locked, the
   * functions returns 0; the process is not blocked.
   */
  extern int  ssf_mutex_try(ssf_mutex* lock);

  /*
   * Process management.
   */

  /*
   * Create child processes of the given number 'num'. Each child
   * process will use 'child' as its starting function. The parameter
   * to the child function is the process id, which ranges from 0 to
   * num-1. The parent process (the one invoking this function) will
   * be suspended until all child processes return from the child
   * function. Note that it is possible the parent process assumes
   * one of the child processes within this function.
   */
  extern void ssf_fork_children(int num, void (*child)(int));

  /*
   * A process can relinquish the processor voluntarily without
   * blocking by calling this function. This will give a chance to
   * another process to get some CPU cycles, instead of starving. This
   * is important when there are more processes are created than the
   * number of available physical processors.
   */
  extern void ssf_yield_proc();

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_MACHINE_WINDOWS_H__*/

#endif /*PRIME_SSF_ARCH_WINDOWS*/

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
