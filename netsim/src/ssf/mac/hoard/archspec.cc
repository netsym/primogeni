///-*-C++-*-//////////////////////////////////////////////////////////////////
//
// The Hoard Multiprocessor Memory Allocator
// www.hoard.org
//
// Author: Emery Berger, http://www.cs.utexas.edu/users/emery
//
// Copyright (c) 1998-2001, The University of Texas at Austin.
//
// This library is free software; you can redistribute it and/or modify
// it under the terms of the GNU Library General Public License as
// published by the Free Software Foundation, http://www.fsf.org.
//
// This library is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Library General Public License for more details.
//
//////////////////////////////////////////////////////////////////////////////

/*
 * The software was modified by Jason Liu to fit into SSF. There are
 * a few places that require special attention:
 *
 *   1. The names of the source files are changed to make it less
 *   likely to be in conflict with those used by SSF users. The
 *   source files are placed under SSF source tree.
 * 
 *   2. Macros, global variables, and public functions are renamed to
 *   make them separate from user name space.
 *
 *   3. The view of the shared memory and the processes is peculiar in
 *   SSF. This affects how shared and private variables are defined
 *   and used.
 *
 *   4. Hoard originally depends on system memory routines
 *   (malloc/free) to allocate and release memory blocks. We changed
 *   it so that hoard is now built on top of DL's memory allocator,
 *   which operates on the shared arena.
 *
 * We consider the hoard allocator as part of SSF and distributed as
 * such, with complete understanding of the terms and conditions of
 * its original copyright claims. We attach our copyright notice in
 * the following (not at all in conflict with the original claims by
 * Hoard's author), maintaining our rights to the entire SSF
 * software. We honor hoard's original copyright.
 */

#include "mac/segments.h"
#include "mac/hoard/archspec.h"

#if PRIME_SSF_HOARD

namespace prime {
namespace ssf {

int ssf_hoard_get_thread_id (void)
{
  return SSF_SELF;
}

void ssf_hoard_lock_init (ssf_hoard_lock_type& mutex)
{
  prime::ssf::ssf_mutex_init(&mutex);
}

void ssf_hoard_lock (ssf_hoard_lock_type& mutex)
{
  prime::ssf::ssf_mutex_wait(&mutex);
}

void ssf_hoard_unlock (ssf_hoard_lock_type& mutex)
{
  prime::ssf::ssf_mutex_signal(&mutex);
}

void ssf_hoard_yield (void)
{
  prime::ssf::ssf_yield_proc();
}

extern "C" void* ssf_dlmalloc (size_t);
extern "C" void  ssf_dlfree (void*);

void* ssf_hoard_get_memory (long size)
{
  prime::ssf::ssf_mutex_wait(&SSF_USE_SHARED(arena_lock));
  void* ptr = ssf_dlmalloc (size);
  prime::ssf::ssf_mutex_signal(&SSF_USE_SHARED(arena_lock));
  return ptr;
}

void ssf_hoard_free_memory (void* ptr)
{
  prime::ssf::ssf_mutex_wait(&SSF_USE_SHARED(arena_lock));
  ssf_dlfree (ptr);
  prime::ssf::ssf_mutex_signal(&SSF_USE_SHARED(arena_lock));
}

int ssf_hoard_get_page_size (void)
{
  return prime::ssf::ssf_get_page_size();
}

int ssf_hoard_get_nprocs (void)
{
  return SSF_NPROCS;
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/
