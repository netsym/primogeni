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

#ifndef _PRIME_SSF_HOARD_THREADHEAP_H_
#define _PRIME_SSF_HOARD_THREADHEAP_H_

#include "mac/hoard/config.h"

#if PRIME_SSF_HOARD

#include "mac/hoard/heap.h"

namespace prime {
namespace ssf {

class ssf_hoard_process_heap; // forward declaration

//
// We use one ssf_hoard_thread_heap for each thread (processor).
//

class ssf_hoard_thread_heap : public ssf_hoard_heap {

public:

  ssf_hoard_thread_heap (void);

  // Memory allocation routines.
  void * malloc (const size_t sz);
  void * memalign (size_t alignment, size_t sz);

  // Find out how large an allocated object is.
  static size_t objectSize (void * ptr);

  // Set our process heap.
  void setpHeap (ssf_hoard_process_heap * p);

private:

  // Prevent copying and assignment.
  ssf_hoard_thread_heap (const ssf_hoard_thread_heap&);
  const ssf_hoard_thread_heap& operator= (const ssf_hoard_thread_heap&);

  // Our process heap.
  ssf_hoard_process_heap *	_pHeap;

  // We insert a cache pad here to avoid false sharing (the
  // ssf_hoard_process_heap holds an array of ssf_hoard_thread_heaps, and we don't want
  // these to share any cache lines).
  double _pad[SSF_HOARD_CACHE_LINE / sizeof(double)];
};

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_THREADHEAP_H_*/
