/*
 * arena :- shared heap memory management.
 * 
 * This file contains a number of global routines that are used to
 * manage the shared memory. The C++ new and delete operators in SSF
 * are replaced with these functions to enable shared access to the
 * heap. These routines are internal to SSF implementation; the users
 * are not supposed to use them directly.
 */

/*
 * SSF uses the Hoard memory allocator for shared memory management,
 * if it is chosen. The original Hoard source code was modified to fit
 * into SSF. The following is Hoard's copyright disclaimer.
 */

/*
 * The Hoard Multiprocessor Memory Allocator
 * www.hoard.org
 *
 * Author: Emery Berger, http://www.cs.umass.edu/~emery
 *
 * Copyright (c) 1998-2003, The University of Texas at Austin.
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as
 * published by the Free Software Foundation, http://www.fsf.org.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 */

/*
 * SSF uses Doug Lea's memory allocator for memory management either
 * on sequential machines or on multiprocessors, if chosen. Its
 * original source code was modified to fit into SSF. The following
 * is DL's copyright disclaimer.
 */

/*
 * This is a version (aka dlmalloc) of malloc/free/realloc written by
 * Doug Lea and released to the public domain.  Use, modify, and
 * redistribute this code without permission or acknowledgement in any
 * way you wish.  Send questions, comments, complaints, performance
 * data, etc to dl@cs.oswego.edu
 *
 * VERSION 2.7.0 Sun Mar 11 14:14:06 2001  Doug Lea  (dl at gee)
 */

#include <assert.h>
#include <stdlib.h>
#include <string.h>

#include "mac/machines.h"
#include "mac/segments.h"
#include "mac/arena.h"
#include "sim/debug.h"
#include "api/ssfexception.h"

#include <errno.h>
extern int errno;

#if !SSF_SHARED_HEAP_SEGMENT

/*
 * The following macros changes the way how this arena works. They are
 * defined at the configuration.
 */

/*
 * The following variables are used for maintaining the shared arena
 * and for statistic collection to assess the state of memory
 * management of the shared arena. They are meaningful only when the
 * arena is active.
 */

// Maximum memory usage. It's size of the memory ever allocated to the
// application (and kernel) during the entire simulation.
SSF_DECLARE_SHARED(unsigned long, mem_watermark);

// Instantaneous memory usage at each processor. This is a private
// variable (i.e., one for each processor).
SSF_DECLARE_PRIVATE(unsigned long, mem_waterlevel);

// Permanent (fixed) memory allocated. This memory is not reclaimable.
SSF_DECLARE_SHARED(unsigned long, mem_oilmark);

// The range of shared arena under management is [arena, arena_end),
// which is 'mmapped' to be shared and is fixed throughout the entire
// simulation. That's why the two variables are shared and read-only
// (once set no more changes). The variables arena_buf and
// arena_bufend mark different memory segments for different purposes
// inside this range. The segments can be viewed as
// [arena..arena_buf..arena_bufend..arena_end), where the range
// [arena, arena_buf) is for the allocated memory segment, the range
// [arena_buf, arena_bufend) is for the free memory segment, and
// [arena_bufend, arena_end) is for the permanently allocated memory
// blocks. When a dynamic memory block is allocated, the pointer
// arena_buf moves to the right. When a permanent memory block is
// allocated, the pointer arena_bufend moves to the left.
SSF_DECLARE_SHARED_RO(char*, arena);
SSF_DECLARE_SHARED_RO(char*, arena_end);
SSF_DECLARE_SHARED(char*, arena_buf);
SSF_DECLARE_SHARED(char*, arena_bufend);

// Lock used for mutual exclusion. The lock is used when using DL's
// sequential memory allocator in a multiprocessor environment, or
// when allocating permanent memory.
SSF_DECLARE_SHARED(ssf_mutex, arena_lock);

//#if PRIME_SSF_EMULATION
// Lock used for mutual exclusion WITHIN a process. This will become
// necessary if emulation is enabled so that both read and writer
// threads are to shared the same private segment as the simulation
// process spawning them.
//SSF_DECLARE_PRIVATE(ssf_mutex, arena_inner_lock);
//#endif

// Used by DL memory allocator to hold the state of the memory
// allocator. It needs to be shared by multiple processors.
SSF_DECLARE_SHARED(void*, dlmalloc_state);

/*
 * Allocated memory blocks must be aligned correctly (we use
 * double-word boundary; it must be a power of two). The following
 * macros are used to adjust a memory pointer to point to a correctly
 * aligned memory block.
 */
#define SSF_ARENA_ALIGNMENT_SIZE sizeof(SSF_LONG_LONG)
#define SSF_ARENA_ALIGN(s) \
  ((size_t)(((size_t)(s) + (SSF_ARENA_ALIGNMENT_SIZE-1)) & \
	    (~(SSF_ARENA_ALIGNMENT_SIZE-1))))

namespace prime {
namespace ssf {

/*
 * Declarations of public routines from the hoard memory allocator,
 * which we use in this module. Only applicable if hoard is
 * chosen. These functions are declared here because hoard is
 * _implicitly_ linked with SSF.
 */
#if PRIME_SSF_HOARD
extern void   SSF_HOARD_INIT();
extern void*  SSF_HOARD_MALLOC(size_t);
extern void   SSF_HOARD_FREE(void*);
extern size_t SSF_HOARD_GET_USABLE_SIZE_WITH_OVERHEAD(void*);
#endif /*PRIME_SSF_HOARD*/

/*
 * The following function is used by DL allocator to replace the
 * standard system routine sbrk. ssf_sbrk increments the program's data
 * space by 'incr' bytes. This chunk of memory will be maintained by
 * DL's memory allocator internally.
 */
static void* ssf_sbrk(long incr)
{
  // We operate on the arena segment directly, assuming that 'incr' is
  // a power of two, which should be the case!
  assert((size_t)incr == SSF_ARENA_ALIGN(incr));

  if(SSF_USE_SHARED(arena_buf)+incr > SSF_USE_SHARED(arena_bufend)) {
    // if we don't have more memory available 
    // (incr must be positive in this case)
    errno = ENOMEM;
#if PRIME_SSF_DEBUG
    // By the time this function is first called, the private segment
    // has not been set up. We can't use SSF_SELF here.
    prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_sbrk(%ld): out of memory\n", incr);
#endif
    return (void*)-1; // -1 means error in ssf_sbrk
  } else {
    char* p;
    if(incr > 0) {
      // positive increment means asking for more memory
      p = SSF_USE_SHARED(arena_buf);
      SSF_USE_SHARED(arena_buf) += incr;

      // record memory watermark (the maximum memory usage)
      unsigned long diff = (unsigned long)
	(SSF_USE_SHARED(arena_buf)-SSF_USE_SHARED_RO(arena));
      if(diff > SSF_USE_SHARED(mem_watermark))
	SSF_USE_SHARED(mem_watermark) = diff;
    } else {
      // negative increment means returning memory to the pool
      SSF_USE_SHARED(arena_buf) += incr; // incr is negative
      p = SSF_USE_SHARED(arena_buf);
    }
    // we don't change the memory water level (instantaneous memory
    // usage) here. The memory block given to sbrk is used internally
    // by DL allocator. We record memory water level at each
    // individual calls to malloc and free.

#if PRIME_SSF_DEBUG
    // it's possible the private segment has not been established
    // before this function is first called; therefore, no USE_PRIVATE
    // is allowed here!
    prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_sbrk(%ld): %p (brk=%p)\n",
		incr, p, SSF_USE_SHARED(arena_buf));
#endif
    return (void*)p;
  }
}

}; // namespace ssf
}; // namespace prime

/*
 * The following are definitions needed before DL allocator source
 * code can be included. The code is included directly instead of
 * linked after compilation. The macros are only used here and will
 * not be seen outside this scope; we made sure there's no conflict.
 */

// Disable the use of mmap for servicing large requests: DL allocator
// must rely on sbrk for getting memory from the system.
#undef HAVE_MMAP
#define HAVE_MMAP 0

// DL allocator provides a number of assertion checks hoping to catch
// memory errors. As noted in its source code, the checking is fairly
// extensive, and will slow down execution considerably. We turn it on
// only when PRIME_SSF_NDEBUG is not defined.
#undef DEBUG
#if PRIME_SSF_DEBUG
#define DEBUG 1
#else
#define DEBUG 0
#endif

// sbrk is replace with our own.
#undef MORECORE
#define MORECORE prime::ssf::ssf_sbrk

// The get_malloc_state function is disabled in DL when included from
// this file. It is replaced here by this one, which is made
// compatible with SSF's shared segment rules.
#undef get_malloc_state
#define get_malloc_state() ((mstate)SSF_USE_SHARED(dlmalloc_state))

// This is a hack to avoid trouble from cxx on OSF. Don't ask me why.
#ifndef __STD_C
#define __STD_C 1
#endif

// All DL routines are preceded with 'ssf_dl', to distinguish them
// from regular malloc/free. The macro DL_PREFIX is used to add the
// prefix to DL function names.
#define USE_DL_PREFIX 1
#if defined(__STDC__) || defined(__DECCXX)
#define DL_PREFIX(x) ssf_dl##x
#else
#define DL_PREFIX(x) ssf_dl/**/x
#endif

// dlmalloc.c is DL's malloc.c and is here included directly.
#include "mac/dlmalloc.h"

#endif /*not SSF_SHARED_HEAP_SEGMENT*/

namespace prime {
namespace ssf {

extern int ssf_arena_ready;

void ssf_arena_init(int pid)
{
  // Set the flag marking that shared arena is ready to be used;
  // further memory allocation will be coming from the shared arena.
  ssf_arena_ready = 1;
  
#if !SSF_SHARED_HEAP_SEGMENT

  if(!SSF_ARENA_ACTIVE) {
#if PRIME_SSF_DEBUG
    prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_arena_init(%d): "
		"arena not needed for sequential environment\n", pid);
#endif
    return;
  }

  if(pid == 0) {
    SSF_USE_SHARED(mem_watermark) = 0;
    SSF_USE_SHARED(mem_oilmark) = 0;

    // Processor 0 initializes everything!
    // Before ssf_arena_init is called, the shared memory segment has
    // already been mmapped into the address space. The shared arena
    // is marked by global shared variables arena and arena_end.
    SSF_USE_SHARED(arena_buf) = SSF_USE_SHARED_RO(arena);
    SSF_USE_SHARED(arena_bufend) = SSF_USE_SHARED_RO(arena_end);

    prime::ssf::ssf_mutex_init(&SSF_USE_SHARED(arena_lock));
    SSF_USE_SHARED(dlmalloc_state) = 
      ssf_arena_malloc_fixed(sizeof(malloc_state));
#if PRIME_SSF_HOARD
    prime::ssf::SSF_HOARD_INIT();
#endif
  }

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_arena_init(%d) => "
	     "(arena=%p, arena_end=%p, arena_buf=%p, arena_bufend=%p)\n", 
	     pid, SSF_USE_SHARED_RO(arena), SSF_USE_SHARED_RO(arena_end),
	     SSF_USE_SHARED(arena_buf), SSF_USE_SHARED(arena_bufend));
#endif

#else /*SSF_SHARED_HEAP_SEGMENT*/

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_arena_init(%d): "
	      "shared arena is bypassed\n", pid);
#endif

#endif /*SSF_SHARED_HEAP_SEGMENT*/
}

void* ssf_arena_malloc(size_t size)
{
#if !SSF_SHARED_HEAP_SEGMENT

  void* p;

  if(!SSF_ARENA_ACTIVE) {
    p = malloc(size);
#if PRIME_SSF_DEBUG
    prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> [%d] prime::ssf::ssf_arena_malloc(%lu) => "
		"%p (via malloc)\n", SSF_SELF, size, p);
#endif
    return p;
  }

  //#if PRIME_SSF_EMULATION && PRIME_SSF_HOARD
  //prime::ssf::ssf_mutex_wait(&SSF_USE_PRIVATE(arena_inner_lock));
  //#endif

#if PRIME_SSF_HOARD
  // DL is used for sequential execution.
  p = prime::ssf::SSF_HOARD_MALLOC(size);
#else /*not PRIME_SSF_HOARD*/
  // DL memory allocator must have mutual exclusion
  prime::ssf::ssf_mutex_wait(&SSF_USE_SHARED(arena_lock));
  p = ssf_dlmalloc(size);
  prime::ssf::ssf_mutex_signal(&SSF_USE_SHARED(arena_lock));
#endif /*not PRIME_SSF_HOARD*/

  if(!p) ssf_throw_exception(ssf_exception::kernel_heapmem);

#if PRIME_SSF_HOARD
  // Get the true size of the memory block allocated (including overhead).
  size_t real_size = prime::ssf::SSF_HOARD_GET_USABLE_SIZE_WITH_OVERHEAD(p);
#else /*not PRIME_SSF_HOARD*/
  size_t real_size = ssf_dlmalloc_usable_size(p)+SIZE_SZ;
#endif /*not PRIME_SSF_HOARD*/
  SSF_USE_PRIVATE(mem_waterlevel) += real_size;

  //#if PRIME_SSF_EMULATION && PRIME_SSF_HOARD
  //prime::ssf::ssf_mutex_signal(&SSF_USE_PRIVATE(arena_inner_lock));
  //#endif

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> [%d] prime::ssf::ssf_arena_malloc(%lu) => %p "
	      "(real_size=%lu) [waterlevel=%lu, watermark=%lu, oilmark=%lu]\n",
	      SSF_SELF, size, p, real_size, SSF_USE_PRIVATE(mem_waterlevel), 
	      SSF_USE_SHARED(mem_watermark), SSF_USE_SHARED(mem_oilmark));
#endif

  return p;

#else /*SSF_SHARED_HEAP_SEGMENT*/

  void* p = malloc(size);
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> [%d] prime::ssf::ssf_arena_malloc(%lu) => "
	      "%p (via malloc)\n", SSF_SELF, size, p);
#endif
  return p;

#endif /*SSF_SHARED_HEAP_SEGMENT*/
}

void ssf_arena_free(void* p)
{
#if !SSF_SHARED_HEAP_SEGMENT

  if(!SSF_ARENA_ACTIVE) {
#if PRIME_SSF_DEBUG 
    prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> [%d] prime::ssf::ssf_arena_free(%p) (via free)\n",
		SSF_SELF, p);
#endif
    free(p);
    return;
  }

  // Reclaiming NULL pointer should have no effect.
  if(!p) return;
  
  //#if PRIME_SSF_EMULATION && PRIME_SSF_HOARD
  //prime::ssf::ssf_mutex_wait(&SSF_USE_PRIVATE(arena_inner_lock));
  //#endif

#if PRIME_SSF_HOARD
  // Get the true size of the memory block allocated (including overhead).
  size_t real_size = prime::ssf::SSF_HOARD_GET_USABLE_SIZE_WITH_OVERHEAD(p);
#else /*not PRIME_SSF_HOARD*/
  size_t real_size = ssf_dlmalloc_usable_size(p)+SIZE_SZ;
#endif /*not PRIME_SSF_HOARD*/
  SSF_USE_PRIVATE(mem_waterlevel) -= real_size;

#if PRIME_SSF_HOARD
  // DL is used for sequential execution.
  prime::ssf::SSF_HOARD_FREE(p);
#else /*not PRIME_SSF_HOARD*/
  // using DL must be coordinated
  prime::ssf::ssf_mutex_wait(&SSF_USE_SHARED(arena_lock));
  ssf_dlfree(p);
  prime::ssf::ssf_mutex_signal(&SSF_USE_SHARED(arena_lock));
#endif /*not PRIME_SSF_HOARD*/

  //#if PRIME_SSF_EMULATION && PRIME_SSF_HOARD
  //prime::ssf::ssf_mutex_signal(&SSF_USE_PRIVATE(arena_inner_lock));
  //#endif

#if PRIME_SSF_DEBUG 
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> [%d] prime::ssf::ssf_arena_free(%p) => "
	      "(real_size=%lu) [waterlevel=%lu, watermark=%lu, oilmark=%lu]\n",
	      SSF_SELF, p, real_size, SSF_USE_PRIVATE(mem_waterlevel),
	      SSF_USE_SHARED(mem_watermark), SSF_USE_SHARED(mem_oilmark));
#endif

#else /*SSF_SHARED_HEAP_SEGMENT*/

#if PRIME_SSF_DEBUG 
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> [%d] prime::ssf::ssf_arena_free(%p) (via free)\n",
	      SSF_SELF, p);
#endif
  free(p);

#endif /*SSF_SHARED_HEAP_SEGMENT*/
}

void* ssf_arena_malloc_fixed(size_t size)
{
#if !SSF_SHARED_HEAP_SEGMENT

  void* p;

  if(!SSF_ARENA_ACTIVE) {
    p = malloc(size);
#if PRIME_SSF_DEBUG
    // By the time this function is first called, private segment has not
    // been setup. Therefore, we can't use SELF.
    prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_arena_malloc_fixed(%lu) => "
		"%p (via malloc)\n", size, p);
#endif
    return p;
  }

  // The memory allocated this way is permanent! It shall never be
  // reclaimed until the end of the simulation.

  size_t real_size = SSF_ARENA_ALIGN(size);
  prime::ssf::ssf_mutex_wait(&SSF_USE_SHARED(arena_lock));
  SSF_USE_SHARED(mem_oilmark) += real_size;
  if(SSF_USE_SHARED(arena_buf)+real_size > SSF_USE_SHARED(arena_bufend)) {
    ssf_throw_exception(ssf_exception::kernel_heapmem);
  }
  SSF_USE_SHARED(arena_bufend) -= real_size;
  p = SSF_USE_SHARED(arena_bufend);
  assert((size_t)p == SSF_ARENA_ALIGN(p));
  prime::ssf::ssf_mutex_signal(&SSF_USE_SHARED(arena_lock));

#if PRIME_SSF_DEBUG
  // By the time this function is first called, private segment has not
  // been setup. Therefore, we can't use SELF.
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_arena_malloc_fixed(%lu) => %p "
	     "(real_size=%lu)\n", size, p, real_size);
#endif

  return (void*)p;

#else /*SSF_SHARED_HEAP_SEGMENT*/

  void* p = malloc(size);
#if PRIME_SSF_DEBUG
  // By the time this function is first called, private segment has not
  // been setup. Therefore, we can't use SELF.
  prime::ssf::ssf_debug(SSF_DEBUG_ARENA, "<ARENA> prime::ssf::ssf_arena_malloc_fixed(%lu) => "
	      "%p (via malloc)\n", size, p);
#endif
  return p;

#endif /*SSF_SHARED_HEAP_SEGMENT*/
}

void* ssf_arena_calloc(size_t n, size_t elmsz)
{
  size_t size = n*elmsz;
  void* q = ssf_arena_malloc(size);
  if(q) memset(q, 0, size);
  return q;
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
