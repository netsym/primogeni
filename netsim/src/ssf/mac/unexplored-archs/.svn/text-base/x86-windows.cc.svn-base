/*
 * x86-windows :- hardware support for windows configuration.
 */

#ifdef PRIME_SSF_ARCH_WINDOWS

#include "api/ssfexception.h"
#include "sim/debug.h"
#include "mac/machines.h"
#include "mac/segments.h"

// the directory to generate mmap backstore files
SSF_DECLARE_SHARED_RO(char*, backstore);

// Private segement is pointed to by a global public variable (which
// is shared in this configuration); therefore, the segments must all
// start at the same virtual address.
SSF_DECLARE_SHARED_RO(void*, private_segment);

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
  assert(!SSF_SDATA_ACTIVE);
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_map_shared_data(): "
	            "data is shared in sequential environment\n");
#endif
}

void ssf_map_private_data(int pid, size_t size)
{
  // this function must be called by child process 0 first!!!
  assert(!SSF_SDATA_ACTIVE);
  assert(pid == 0);
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> prime::ssf::ssf_map_private_data(%d): "
              "data is private in sequential environment\n", pid);
#endif
  SSF_INIT_SHARED_RO(private_segment, 0); // for sanity check
}

void* ssf_create_shared_segment(size_t size)
{
  assert(0);
  return 0;
}

void ssf_mutex_init(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  assert(SSF_NPROCS == 1);
}

void ssf_mutex_wait(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  assert(SSF_NPROCS == 1);
}

void ssf_mutex_signal(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  assert(SSF_NPROCS == 1);
}

int ssf_mutex_try(ssf_mutex* lock)
{
  // lock is of no use in sequential environment
  assert(SSF_NPROCS == 1);
  return 1;
}

void ssf_fork_children(int num, void (*child)(int))
{
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_MACHINE, "<MACH> ssf_fork_children(nkids=%d)\n", num);
#endif
  assert(num == 1);
  child(0);
}

void ssf_yield_proc() 
{
  // beware the danger of process starving
  if(SSF_USE_SHARED_RO(spinlock)) return;
}

}; // namespace ssf
}; // namespace prime

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
