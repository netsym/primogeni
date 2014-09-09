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
  processheap.h
  ------------------------------------------------------------------------
  We use one ssf_hoard_process_heap for the whole program.
  ------------------------------------------------------------------------
  @(#) $Id: mac/hoard/processheap.h,v 1.1.1.1 2004/07/21 21:01:32 jasonliu Exp $
  ------------------------------------------------------------------------
  Emery Berger                    | <http://www.cs.utexas.edu/users/emery>
  Department of Computer Sciences |             <http://www.cs.utexas.edu>
  University of Texas at Austin   |                <http://www.utexas.edu>
  ========================================================================
*/

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

#ifndef _PRIME_SSF_HOARD_PROCESSHEAP_H_
#define _PRIME_SSF_HOARD_PROCESSHEAP_H_

#include <stdio.h>

#include "mac/hoard/config.h"

#if PRIME_SSF_HOARD

#include "mac/hoard/archspec.h"
#include "mac/hoard/heap.h"
#include "mac/hoard/threadheap.h"

#if SSF_HOARD_HEAP_LOG
#include "memstat.h"
#include "log.h"
#endif

namespace prime {
namespace ssf {

class ssf_hoard_process_heap : public ssf_hoard_heap {

public:

  // Always grab at least this many superblocks' worth of memory which
  // we parcel out.
  enum { SSF_HOARD_REFILL_NUMBER_OF_SUPERBLOCKS = 16 };

  ssf_hoard_process_heap (void);

  ~ssf_hoard_process_heap (void) {
#if SSF_HOARD_HEAP_STATS
    stats();
#endif
  }

  // Memory deallocation routines.
  void free (void * ptr);

  // Print out statistics information.
  void stats (void);

  // Get a thread heap index.
  inline int getHeapIndex (void);

  // Get the thread heap with index i.
  inline ssf_hoard_thread_heap& getHeap (int i);

  // Extract a superblock.
  ssf_hoard_superblock * acquire (const int c, ssf_hoard_heap * dest);

  // Insert a superblock.
  void release (ssf_hoard_superblock * sb);

#if SSF_HOARD_HEAP_LOG
  // Get the log for index i.
  inline Log<MemoryRequest>& getLog (int i);
#endif

#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  // Declare that we have allocated an object.
  void setAllocated (int requestedSize,
		     int actualSize);

  // Declare that we have deallocated an object.
  void setDeallocated (int requestedSize,
		       int actualSize);

  // Return the number of wasted bytes at the high-water mark
  // (maxAllocated - maxRequested)
  inline int getFragmentation (void);

  int getMaxAllocated (void) {
    return _maxAllocated;
  }

  int getInUseAtMaxAllocated (void) {
    return _inUseAtMaxAllocated;
  }

  int getMaxRequested (void) {
    return _maxRequested;
  }
  
#endif

private:

  // Hide the lock & unlock methods.

  inline void lock (void) {
    ssf_hoard_heap::lock();
  }

  inline void unlock (void) {
    ssf_hoard_heap::unlock();
  }

  // Prevent copying and assignment.
  ssf_hoard_process_heap (const ssf_hoard_process_heap&);
  const ssf_hoard_process_heap& operator= (const ssf_hoard_process_heap&);

  // The per-thread heaps.
  ssf_hoard_thread_heap theap[SSF_HOARD_MAX_HEAPS];

#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  // Statistics required to compute fragmentation.  We cannot
  // unintrusively keep track of these on a multiprocessor, because
  // this would become a bottleneck.

  int _currentAllocated;
  int _currentRequested;
  int _maxAllocated;
  int _maxRequested;
  int _inUseAtMaxAllocated;
  int _fragmentation;

  // A lock to protect these statistics.
  ssf_hoard_lock_type _statsLock;
#endif

#if SSF_HOARD_HEAP_LOG
  Log<MemoryRequest> _log[SSF_HOARD_MAX_HEAPS + 1];
#endif

  // A lock for the superblock buffer.
  /*
  ssf_hoard_lock_type _bufferLock;
  char * 	_buffer;
  int 		_bufferCount;
  */
};


ssf_hoard_thread_heap& ssf_hoard_process_heap::getHeap (int i)
{
  assert (i >= 0);
  assert (i < SSF_HOARD_MAX_HEAPS);
  return theap[i];
}


#if SSF_HOARD_HEAP_LOG
Log<MemoryRequest>& ssf_hoard_process_heap::getLog (int i)
{
  assert (i >= 0);
  assert (i < SSF_HOARD_MAX_HEAPS + 1);
  return _log[i];
}
#endif


// Return ceil(log_2(num)).
// num must be positive.
inline int lg (int num)
{
  assert (num > 0);
  int power = 0;
  int n = 1;
  // Invariant: 2^power == n.
  while (n < num) {
    n <<= 1;
    power++;
  }
  return power;
}


// Hash out the thread id to a heap and return an index to that heap.
int ssf_hoard_process_heap::getHeapIndex (void) {
//  int tid = ssf_hoard_get_thread_id() & ssf_hoard_heap::_numProcessorsMask;
  int tid = ssf_hoard_get_thread_id() & SSF_HOARD_MAX_HEAPS_MASK;
  assert (tid < SSF_HOARD_MAX_HEAPS);
  return tid;
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_PROCESSHEAP_H_*/
