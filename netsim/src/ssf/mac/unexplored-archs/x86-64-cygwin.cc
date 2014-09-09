/*
 * x86-64-cygwin :- machine support for x86-64/cygwin machine configuration.
 */

#ifdef PRIME_SSF_ARCH_X86_64_CYGWIN

#include <assert.h>
#include <errno.h>
#include <sched.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/wait.h>

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

// Private segement is pointed to by a global public variable (which
// is shared in this configuration); therefore, the segments must all
// start at the same virtual address.
#if SSF_USE_THREADS
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

void ssf_map_private_data(int pid, size_t size)
{
#if SSF_USE_THREADS
  // this function must be called by child process 0 first!!!

  if(!SSF_SDATA_ACTIVE) {
    assert(pid == 0);
#if PRIME_SSF_DEBUG
    prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_map_private_data(%d): "
		"data is private in sequential environment\n", pid);
#endif
    SSF_INIT_SHARED_RO(private_segment, 0); // for sanity check
    return;
  }

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
  // we take memory from the program heap (instead of the shared
  // arena, if we use 'new' operator here), knowing that the memory is
  // private to each process
  pthread_setspecific(SSF_USE_SHARED_RO(private_segment), 
		      calloc(1, size));

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_map_private_data(%d): "
	      "size=%d, area=%p\n", pid, (int)size, SSF_USE_SHARED_RO(private_segment));
#endif
#endif
}

void* ssf_create_shared_segment(size_t size)
{
  if(size <= 0) return 0;

  // create backstore file for the shared segment
  char fname[SSF_MACHINE_MMAP_FNAME_MAX]; 
  snprintf(fname, SSF_MACHINE_MMAP_FNAME_MAX, "%s/ssf-shared-XXXXXX",
	   SSF_USE_SHARED_RO(backstore));
  fname[SSF_MACHINE_MMAP_FNAME_MAX-1] = 0; // safety
  int fd = mkstemp(fname);
  if(fd < 0) {
    char errmsg[256];
    int errcode = errno;
    sprintf(errmsg, "(size=%d) unable to create temporary file (%s): %s",
	    (int)size, fname, strerror(errcode));
    ssf_throw_exception(ssf_exception::kernel_sseg, errmsg);
  }
  unlink(fname); // the file will be deleted after use

  // append something to the end of file as sanity check
  int n = lseek(fd, size, SEEK_SET);
  if(n != (int)size) {
    char errmsg[256];
    int errcode = errno;
    sprintf(errmsg, "(size=%d) unable to seek to memory end: %s",
	    (int)size, strerror(errcode));
    ssf_throw_exception(ssf_exception::kernel_sseg, errmsg);
  }
  n = write(fd, &size, sizeof(size));
  if(n != sizeof(size)) {
    char errmsg[256];
    int errcode = errno;
    sprintf(errmsg, "(size=%d) unable to write to memory end: %s",
	    (int)size, strerror(errcode));
    ssf_throw_exception(ssf_exception::kernel_sseg, errmsg);
  }

  // map into the (shared) address space
  void* area = mmap(0, size, PROT_READ|PROT_WRITE, MAP_SHARED, fd, 0);
  if (area == (void*)-1) {
    char errmsg[256];
    int errcode = errno;
    sprintf(errmsg, "(size=%d) failed to map into address space: %s",
	    (int)size, strerror(errcode));
    ssf_throw_exception(ssf_exception::kernel_sseg, errmsg);
  }

  // file handle no long needed
  close(fd);

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_create_shared_segment(size=%d): "
	      "shared segment=%p\n", (int)size, area);
#endif
  return area;
}

/* The following macro provide the same functionality as xchg function
   from asm/system.h on linux machines. Actually it's taken from the
   linux source; Cygwin doesn't have such a function. */
struct xchg_dummy { unsigned long a[100]; };
#define XCHG_DUMMY(x) ((struct xchg_dummy*)(x))

static inline unsigned long xchg(unsigned long x, volatile void* ptr, int size)
{
  switch (size) {
  case 1:
    __asm__ __volatile__("xchgb %b0,%1"
			 :"=q" (x)
			 :"m" (*XCHG_DUMMY(ptr)), "0" (x)
			 :"memory");
    break;
  case 2:
    __asm__ __volatile__("xchgw %w0,%1"
			 :"=r" (x)
			 :"m" (*XCHG_DUMMY(ptr)), "0" (x)
			 :"memory");
    break;
  case 4:
    __asm__ __volatile__("xchgl %0,%1"
			 :"=r" (x)
			 :"m" (*XCHG_DUMMY(ptr)), "0" (x)
			 :"memory");
    break;
  }
  return x;
}

#define XCHG(ptr,v) ((__typeof__(*(ptr)))xchg((unsigned long)(v),(ptr),sizeof(*(ptr))))

#define SSF_ACQUIRE_LOCK(lock) XCHG((int*)lock, 1)
#define SSF_RELEASE_LOCK(lock) (*lock) = 0

void ssf_mutex_init(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  if(SSF_NPROCS == 1) return;

  *lock = 1;
  SSF_RELEASE_LOCK(lock);
}

void ssf_mutex_wait(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  if(SSF_NPROCS == 1) return;

  if(SSF_ACQUIRE_LOCK(lock) == 0) return;
  do {
    while(*lock != 0) ssf_yield_proc();
  } while(SSF_ACQUIRE_LOCK(lock) != 0);
}

void ssf_mutex_signal(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  if(SSF_NPROCS == 1) return;

  SSF_RELEASE_LOCK(lock);
}

int ssf_mutex_try(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  if(SSF_NPROCS == 1) return 1;

  if(*lock != 0) return 0;
  if(SSF_ACQUIRE_LOCK(lock) == 0) return 1;
  else return 0;
}

#if SSF_USE_THREADS
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
#else
void ssf_fork_children(int num, void (*child)(int))
{
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d)\n", num);
#endif

  pid_t children_pid[SSF_MACHINE_FORK_MAX];
  if(num >= SSF_MACHINE_FORK_MAX) {
    ssf_throw_exception(ssf_exception::kernel_forklimit);
  }

#if SSF_MACHINE_PARENT_AS_CHILD0
  int startpid = 1;
#else
  int startpid = 0;
#endif
  for(int i=startpid; i<num; i++) {
    pid_t pid = fork();
    if(pid < 0) {
      ssf_throw_exception(ssf_exception::kernel_fork);
    } else if(pid == 0) { // in child process
#if PRIME_SSF_DEBUG
      prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d): "
		  "child process %d starts...\n", num, i);
#endif
      child(i);
#if PRIME_SSF_DEBUG
      prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d): "
		  "child process %d ends...\n", num, i);
#endif
      _exit(0); // force child process to end here!
    } else { // in parent process
      children_pid[i] = pid;
    }
  }

#if SSF_MACHINE_PARENT_AS_CHILD0
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d): "
	      "child process 0 (assumed by parent) starts...\n", num);
#endif
  child(0); // prarent assume child process 0
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d): "
	      "child process 0 (assumed by parent) ends...\n", num);
#endif
#endif /*SSF_MACHINE_PARENT_AS_CHILD0*/

  // parent now waits for all children to complete
  for(int n=startpid; n<num; n++) {
    int i, stat;
    pid_t pid = waitpid((pid_t)-1, &stat, 0);
    for(i=startpid; i<num; i++)
      if(pid == children_pid[i]) break;
    assert(i < num);
#if PRIME_SSF_DEBUG
    prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d): "
		"child %d exits\n", num, i);
#endif
    children_pid[i] = 0;

    if(WIFEXITED(stat)) { 
      // the child process terminated due to an exit call
      if(WEXITSTATUS(stat) != 0) {
	ssf_throw_warning("ssf_fork_children(nkids=%d): child %d returns %d",
		   num, i, WEXITSTATUS(stat));
      } else continue;
    } else if(WIFSIGNALED(stat)) {
      // child process terminated due to a signal
      const char* core;
      if(WCOREDUMP(stat)) core = " (core dumped)";
      else core = "";
      switch (WTERMSIG(stat)) {
      case SIGILL:
	ssf_throw_warning("child %d: illegal instruction%s", i, core);
	break;
      case SIGABRT:
	ssf_throw_warning("child %d: abort or assertion failed%s", i, core);
	break;
      case SIGFPE:
	ssf_throw_warning("child %d: floating exception%s", i, core);
	break;
      case SIGKILL:
	ssf_throw_warning("child %d: terminated%s", i, core);
	break;
      case SIGBUS:
	ssf_throw_warning("child %d: bus error%s", i, core);
	break;
      case SIGSEGV:
	ssf_throw_warning("child %d: segmentation fault%s", i, core);
	break;
      default:
	ssf_throw_warning("child %d: signal %d (see /usr/include/sys/signal.h)%s",
		   i, WTERMSIG(stat), core);
	break;
      }
    }

    // kill all others since we found an error
    for(i=startpid; i<num; i++)
      if(children_pid[i]) kill(children_pid[i], SIGKILL);
  }
}
#endif

void ssf_yield_proc() 
{
  // beware the danger of process starving
  if(SSF_USE_SHARED_RO(spinlock)) return;

#ifdef _POSIX_PRIORITY_SCHEDULING
  sched_yield();
#else
#warning "WARNING: can't use sched_yield()"
  usleep(0);
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

void ssf_thread_create(ssf_thread_type* tid, void (*child)(void*), void* data)
{
  ssf_thread_child_struct* cs = new ssf_thread_child_struct;
  cs->child = child; cs->data = data;
  if(pthread_create(tid, 0, ssf_thread_child, cs))
    ssf_throw_exception(ssf_exception::kernel_spawn);
}

void ssf_thread_delete(ssf_thread_type* tid)
{
  pthread_cancel(*tid);
}

void ssf_thread_mutex_init(ssf_thread_mutex *lock)
{
  pthread_mutex_init(lock, NULL);
}

void ssf_thread_mutex_wait(ssf_thread_mutex *lock)
{
  pthread_mutex_lock(lock);
}

void ssf_thread_mutex_signal(ssf_thread_mutex *lock)
{
  pthread_mutex_unlock(lock);
}

int ssf_thread_mutex_try(ssf_thread_mutex *lock)
{
  return !pthread_mutex_trylock(lock);
}

void ssf_thread_cond_init(ssf_thread_cond* cond)
{
  pthread_cond_init(cond, NULL);
}

void ssf_thread_cond_wait(ssf_thread_cond* cond, ssf_thread_mutex* lock)
{
  pthread_cond_wait(cond, lock);
}

void ssf_thread_cond_signal(ssf_thread_cond* cond)
{
  pthread_cond_signal(cond);
}

void ssf_thread_cond_broadcast(ssf_thread_cond* cond)
{
  pthread_cond_broadcast(cond);
}

void ssf_thread_yield()
{
#ifdef _POSIX_PRIORITY_SCHEDULING
  sched_yield();
#else
  usleep(0);
#endif
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_ARCH_X86_64_CYGWIN*/

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
