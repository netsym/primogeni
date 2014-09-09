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

#include "mac/hoard/config.h"
#include "mac/hoard/threadheap.h"
#include "mac/hoard/processheap.h"

#if PRIME_SSF_HOARD

namespace prime {
namespace ssf {

ssf_hoard_process_heap::ssf_hoard_process_heap (void)
  /* _buffer (NULL), _bufferCount (0)*/
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  : _currentAllocated (0),
    _currentRequested (0),
    _maxAllocated (0),
    _maxRequested (0),
    _inUseAtMaxAllocated (0)
#endif
{
  int i;
  // The process heap is heap 0.
  setIndex (0);
  for (i = 0; i < SSF_HOARD_MAX_HEAPS; i++) {
    // Set every thread's process heap to this one.
    theap[i].setpHeap (this);
    // Set every thread heap's index.
    theap[i].setIndex (i + 1);
  }
#if SSF_HOARD_HEAP_LOG
  for (i = 0; i < SSF_HOARD_MAX_HEAPS + 1; i++) {
    char fname[255];
    sprintf (fname, "log%d", i);
    unlink (fname);
    _log[i].open (fname);
  }
#endif
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  ssf_hoard_lock_init (_statsLock);
#endif
  /*ssf_hoard_lock_init (_bufferLock);*/
}


// Print out statistics information.
void ssf_hoard_process_heap::stats (void) {
#if SSF_HOARD_HEAP_STATS
  int umax = 0;
  int amax = 0;
  for (int j = 0; j < SSF_HOARD_MAX_HEAPS; j++) {
    for (int i = 0; i < SSF_HOARD_SIZE_CLASSES; i++) {
      amax += theap[j].maxAllocated(i) * sizeFromClass (i);
      umax += theap[j].maxInUse(i) * sizeFromClass (i);
    }
  }
  printf ("Amax <= %d, Umax <= %d\n", amax, umax);
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  amax = getMaxAllocated();
  umax = getMaxRequested();
  printf ("Maximum allocated = %d\nMaximum in use = %d\nIn use at max allocated = %d\n", amax, umax, getInUseAtMaxAllocated());
  printf ("Still in use = %d\n", _currentRequested);
  printf ("Fragmentation (3) = %f\n", (float) amax / (float) getInUseAtMaxAllocated());
  printf ("Fragmentation (4) = %f\n", (float) amax / (float) umax);
#endif

#endif // HEAP_STATS  
#if SSF_HOARD_HEAP_LOG
  printf ("closing logs.\n");
  fflush (stdout);
  for (int i = 0; i < SSF_HOARD_MAX_HEAPS + 1; i++) {
    _log[i].close();
  }
#endif
}



#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
void ssf_hoard_process_heap::setAllocated (int requestedSize,
				int actualSize)
{
  ssf_hoard_lock (_statsLock);
  _currentRequested += requestedSize;
  _currentAllocated += actualSize;
  if (_currentRequested > _maxRequested) {
    _maxRequested = _currentRequested;
  }
  if (_currentAllocated > _maxAllocated) {
    _maxAllocated = _currentAllocated;
    _inUseAtMaxAllocated = _currentRequested;
  }
  ssf_hoard_unlock (_statsLock);
}


void ssf_hoard_process_heap::setDeallocated (int requestedSize,
				  int actualSize)
{
  ssf_hoard_lock (_statsLock);
  _currentRequested -= requestedSize;
  _currentAllocated -= actualSize;
  ssf_hoard_unlock (_statsLock);
}
#endif


// free (ptr, pheap):
//   inputs: a pointer to an object allocated by malloc().
//   side effects: returns the block to the object's superblock;
//                 updates the thread heap's statistics;
//                 may release the superblock to the process heap.

void ssf_hoard_process_heap::free (void * ptr)
{
  // Return if ptr is 0.
  // This is the behavior prescribed by the standard.
  if (ptr == 0) {
    return;
  }

  // Find the block and superblock corresponding to this ptr.

  ssf_hoard_block * b = (ssf_hoard_block *) ptr - 1;
  assert (b->isValid());

  // Check to see if this block came from a memalign() call.
  if ((unsigned long) b->getNext() & 1) {
    // It did. Set the block to the actual block header.
    b = (ssf_hoard_block *) ((unsigned long) b->getNext() & ~1);
    assert (b->isValid());
  }    

  b->markFree();

  ssf_hoard_superblock * sb = b->getSuperblock();
  assert (sb);
  assert (sb->isValid());

  const int sizeclass = sb->getBlockSizeClass();

  //
  // Return the block to the superblock,
  // find the heap that owns this superblock
  // and update its statistics.
  //

  ssf_hoard_heap * owner;

  // By acquiring the up lock on the superblock,
  // we prevent it from moving to the global heap.
  // This eventually pins it down in one heap,
  // so this loop is guaranteed to terminate.
  // (It should generally take no more than two iterations.)
  sb->upLock();
  for (;;) {
    owner = sb->getOwner();
    owner->lock();
    if (owner == sb->getOwner()) {
      break;
    } else {
      owner->unlock();
    }
    // Suspend to allow ownership to quiesce.
    ssf_hoard_yield();
  }

#if SSF_HOARD_HEAP_LOG
  MemoryRequest m;
  m.free (ptr);
  getLog (owner->getIndex()).append(m);
#endif
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  setDeallocated (b->getRequestedSize(), 0);
#endif

  int sbUnmapped = owner->freeBlock (b, sb, sizeclass, this);

  owner->unlock();
  if (!sbUnmapped) {
    sb->upUnlock();
  }
}


ssf_hoard_superblock * ssf_hoard_process_heap::acquire (const int sizeclass,
				   ssf_hoard_heap * dest)
{
  lock ();

  // Remove the superblock with the most free space.
  ssf_hoard_superblock * maxSb = removeMaxSuperblock (sizeclass);
  if (maxSb) {
    maxSb->setOwner (dest);
  }

  unlock ();

  return maxSb;
}


// Put a superblock back into our list of superblocks.
void ssf_hoard_process_heap::release (ssf_hoard_superblock * sb)
{
  assert (SSF_HOARD_EMPTY_FRACTION * sb->getNumAvailable() > sb->getNumBlocks());

  lock();

  // Insert the superblock.
  insertSuperblock (sb->getBlockSizeClass(), sb, this);

  unlock();
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/
