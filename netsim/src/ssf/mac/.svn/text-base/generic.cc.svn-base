/*
 * generic :- generic support for machines with pthreads.
 */

#ifdef PRIME_SSF_ARCH_GENERIC

#include <assert.h>
#include <errno.h>
#include <sched.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>

#include "mac/machines.h"
#include "mac/segments.h"
#include "sim/debug.h"
#include "api/ssfexception.h"

// max name length of the file used for mmap backstore
#define SSF_MACHINE_MMAP_FNAME_MAX 512

// max number of child processes that can be forked
#define SSF_MACHINE_FORK_MAX 256

// parent process should assume to be child process number 0
#define SSF_MACHINE_PARENT_AS_CHILD0 1

// the directory to generate mmap backstore files
SSF_DECLARE_SHARED_RO(char*, backstore);

#if !PRIME_SSF_FIXREG
// Private segement is pointed to by a global public variable (which
// is shared in this configuration); therefore, the segments must all
// start at the same virtual address.
SSF_DECLARE_INCLUDE(<pthread.h>);
SSF_DECLARE_SHARED_RO(pthread_key_t, private_segment);
#endif

// Should we implement lock with simple spinning, instead of given
// other processes a chance to compete for CPU cycles? On one hand,
// forsaking CPU allows extra overhead due to context switches. On the
// other hand, preempting other's opportunity to run may not be an
// ideal situation where SSF processes are to coexist with other
// Unix processes.
SSF_DECLARE_SHARED_RO(int, spinlock);

namespace prime {
namespace ssf {

void ssf_map_shared_data()
{
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_map_shared_data(): "
	      "data is shared for pthreads\n");
#endif
}

void ssf_map_private_data(int pid, size_t size)
{
  // this function must be called by child process 0 first!!!

  if(!SSF_SDATA_ACTIVE) {
    assert(pid == 0);
#if PRIME_SSF_DEBUG
    prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_map_private_data(%d): "
		"data is private in sequential environment\n", pid);
#endif
#if !PRIME_SSF_FIXREG
    SSF_INIT_SHARED_RO(private_segment, 0); // for sanity check
#endif
    return;
  }

#if !PRIME_SSF_FIXREG
  // All processes are required to call this method, but only
  // processor 0 (the parent) will create the private data
  // segment. Since the address space is marked as copy-on-write
  // (mmap), the region will be private to each child process.
  if(pid == 0) {
    pthread_key_t key;
    if(pthread_key_create(&key, 0))
      ssf_throw_exception(ssf_exception::kernel_pdata, 
			  "can't create key for the private segment");
    SSF_INIT_SHARED_RO(private_segment, key);
  }
#endif

  // we take memory from the program heap (instead of the shared
  // arena, if we use 'new' operator here), knowing that the memory is
  // private to each process
  SSF_SET_PRIVATE_DATA(calloc(1, size));

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_map_private_data(%d): "
			"size=%d, area=%p\n", pid, (int)size, SSF_GET_PRIVATE_DATA);
#endif
}

void* ssf_create_shared_segment(size_t size)
{
  // this function should not be called if we do not use arena (we use
  // shared heap), but in case we change our mind, we simply use the
  // malloc here to create the shared heap
  if(size <= 0) return 0;
  else return malloc(size);
}

struct pthread_child_struct {
  void (*child)(int);
  int pid;
};

static void* pthread_child_starter(void* data)
{
  pthread_child_struct* pcs = (pthread_child_struct*)data;
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(): "
	      "child process %d starts...\n", pcs->pid);
#endif
  (*pcs->child)(pcs->pid);
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(): "
	      "child process %d ends...\n", pcs->pid);
#endif
  return 0;
}

void ssf_fork_children(int num, void (*child)(int))
{
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d)\n", num);
#endif

  pthread_t tid[SSF_MACHINE_FORK_MAX];
  pthread_child_struct pcs[SSF_MACHINE_FORK_MAX];
  if(num >= SSF_MACHINE_FORK_MAX) {
    ssf_throw_exception(ssf_exception::kernel_forklimit);
  }

#if SSF_MACHINE_PARENT_AS_CHILD0
  int startpid = 1;
#else
  int startpid = 0;
#endif
  for(int i=startpid; i<num; i++) {
    pcs[i].child = child; pcs[i].pid = i;
    if(pthread_create(tid+i, 0, pthread_child_starter, pcs+i))
      ssf_throw_exception(ssf_exception::kernel_fork);
  }

#if SSF_MACHINE_PARENT_AS_CHILD0
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(): "
	      "child process 0 (assumed by parent) starts...\n");
#endif
  child(0);
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(): "
	      "child process 0 (assumed by parent) ends...\n");
#endif
#endif /*SSF_MACHINE_PARENT_AS_CHILD0*/

  // parent now waits for all children to complete
  for(int n=startpid; n<num; n++) {
    if(pthread_join(tid[n], 0))
      ssf_throw_exception(ssf_exception::kernel_join);
  }
}

void ssf_yield_proc() 
{
  // beware the danger of process starving
  if(SSF_USE_SHARED_RO(spinlock)) return;

#ifdef _POSIX_PRIORITY_SCHEDULING
  sched_yield();
#else
#warning "WARNING: can't use sched_yield()"
  usleep(0); // force it to sleep if possible
#endif
}

struct ssf_thread_child_struct {
  void (*child)(void*);
  void* data;
};

static void* ssf_thread_child(void* data)
{
  int tmp;
  pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, &tmp);
  pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, &tmp);
  ssf_thread_child_struct* cs = (ssf_thread_child_struct*)data;
  (*cs->child)(cs->data);
  delete cs;
  return 0;
}

ssf_thread_type ssf_thread_self() { return pthread_self(); }

int ssf_thread_kill(ssf_thread_type tid, int sig) { return pthread_kill(tid, sig); }

void ssf_thread_create(ssf_thread_type* tid, void (*child)(void*), void* data)
{
  ssf_thread_child_struct* cs = new ssf_thread_child_struct;
  cs->child = child; cs->data = data;
  if(pthread_create(tid, 0, ssf_thread_child, cs))
    ssf_throw_exception(ssf_exception::kernel_spawn);
}

void ssf_thread_yield()
{
#ifdef _POSIX_PRIORITY_SCHEDULING
  sched_yield();
#else
#warning "WARNING: can't use sched_yield()"
  usleep(0); // force it to sleep if possible
#endif
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_ARCH_GENERIC*/

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
