/*
 * x86-64-linux :- hardware support for x86_64/Linux
 * machine configuration.
 *
 * Supported machine configurations include:
 *   host_cpu={x86_64}, host_os={linux}
 *
 * Global variables are shared among processes. There are two ways to
 * achieve this. We can use GNU LD to mark the boundaries of the
 * global data segments (using ldscript). The shared memory heap is
 * obtained through mmap. Private variables are stored in a
 * self-managed private data segment, allocated using mmap for each
 * process. Note that this assumes the starting address of the private
 * data segments (returned from mmap) on all processors is identical;
 * the starting address is stored in a global shared variable, which
 * is marked as read-only. We create child processes using the CLONE
 * system call.
 *
 * The second way to achieve shared global variables is to use Posix
 * threads. In this case, the heap memory is shared automatically by
 * all threads. Private variables are defined as thread-specific data
 * (TSD), stored in a private memory block indexed by the TSD key. It
 * is questionable whether this method is a bit slow in access time.
 * 
 * Mutual exclusion is through the atomic exchange instruction,
 * implemented using embedded assembly language.
 */

#ifdef PRIME_SSF_ARCH_X86_64_LINUX

#ifndef __PRIME_SSF_MACHINE_X86_64_LINUX_H__
#define __PRIME_SSF_MACHINE_X86_64_LINUX_H__

#ifndef __PRIME_SSF_MACHINES_H__
#error "ERROR: x86-64-linux.h must not be included explicitly"
#endif

#include <stdio.h>

#if defined(HAVE_GNU_LD) && !PRIME_SSF_EMULATION && !PRIME_SSF_WITH_THREADS
#define SSF_USE_THREADS 0
#define SSF_SHARED_DATA_SEGMENT 1
#define SSF_SHARED_HEAP_SEGMENT 0
#else
#define SSF_USE_THREADS 1
#define SSF_SHARED_DATA_SEGMENT 1
#define SSF_SHARED_HEAP_SEGMENT 0 // use arena instead of the shared heap
#endif

#if SSF_USE_THREADS
#if defined(HAVE_PTHREAD)
#include <pthread.h>
#else
#error "ERROR: pthreads are required!"
#endif
#endif

#if SSF_USE_THREADS
#define SSF_USE_THREAD_BARRIER 1
#define SSF_GET_PRIVATE_DATA \
 ((struct prime::ssf::private_segment*) \
 pthread_getspecific(SSF_USE_SHARED_RO(private_segment)))
#define SSF_SET_PRIVATE_DATA(x)	\
 pthread_setspecific(SSF_USE_SHARED_RO(private_segment), x)
#else
#define SSF_USE_THREAD_BARRIER 0
#define SSF_GET_PRIVATE_DATA \
 ((struct prime::ssf::private_segment*) \
 SSF_USE_SHARED_RO(private_segment))
#endif

// padding to avoid false sharing
#define SSF_CACHE_LINE_SIZE 64
#define SSF_CACHE_LINE_PADDING \
  char _ssf_dummy_for_cache_line_padding[SSF_CACHE_LINE_SIZE]

namespace prime {
namespace ssf {

  /* Memory management. */

  /*
   * This function creates a data segment for shared global
   * variables. If we choose to use unix processes instead of threads,
   * global data is copied instead of shared. We need to use special
   * tricks (i.e. ldscript) to make the data segment shared among
   * processes. Otherwise, the global data is shared by default (when
   * we use threads).
   */
  extern void ssf_map_shared_data();

  /*
   * This function creates a data segment for private global
   * variables. Private means each process has its own copy of the
   * data. Usually we don't need to do anything as each process has
   * its own copy of the global variables when forked. However, since
   * we make the data segment shared for this architecture (see
   * above), we have to allocate a private segment for private
   * variables for each process. In case we use threads, all we need
   * to do is to create a thread-specific data.
   */
  extern void ssf_map_private_data(int pid, size_t size);

  /*
   * This function creates the shared segment for the heap (which we
   * call arena) and the global shared data section. A pointer to the
   * shared segment is returned.
   */
  extern void* ssf_create_shared_segment(size_t size);

  /* Mutual exclusion. */

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
  extern int ssf_mutex_try(ssf_mutex* lock);

  /* Process management. */

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

#if PRIME_SSF_EMULATION
  /* Threads for real-time support. */

  typedef pthread_t ssf_thread_type;
  typedef pthread_mutex_t ssf_thread_mutex;
  typedef pthread_cond_t ssf_thread_cond;

  extern ssf_thread_type ssf_thread_self();
  extern int ssf_thread_kill(ssf_thread_type tid, int sig);

  extern void ssf_thread_create(ssf_thread_type* tid, void (*child)(void*), void* data);
  extern void ssf_thread_delete(ssf_thread_type* tid);

  extern void ssf_thread_mutex_init(ssf_thread_mutex* lock);
  extern void ssf_thread_mutex_wait(ssf_thread_mutex* lock);
  extern void ssf_thread_mutex_signal(ssf_thread_mutex* lock);
  extern int  ssf_thread_mutex_try(ssf_thread_mutex* lock);

  extern void ssf_thread_cond_init(ssf_thread_cond* cond);
  extern void ssf_thread_cond_wait(ssf_thread_cond* cond, ssf_thread_mutex* lock);
  extern void ssf_thread_cond_signal(ssf_thread_cond* cond);
  extern void ssf_thread_cond_broadcast(ssf_thread_cond* cond);

  extern void ssf_thread_yield();
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_MACHINE_X86_64_LINUX_H__*/

#endif /*PRIME_SSF_ARCH_X86_64_LINUX*/

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
