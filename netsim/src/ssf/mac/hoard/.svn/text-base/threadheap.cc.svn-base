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

#include <limits.h>
#include <string.h>

#include "mac/hoard/config.h"
#include "mac/hoard/heap.h"
#include "mac/hoard/threadheap.h"
#include "mac/hoard/processheap.h"

#if PRIME_SSF_HOARD

namespace prime {
namespace ssf {

ssf_hoard_thread_heap::ssf_hoard_thread_heap (void)
  : _pHeap (0)
{}


// malloc (sz):
//   inputs: the size of the object to be allocated.
//   returns: a pointer to an object of the appropriate size.
//   side effects: allocates a block from a superblock;
//                 may call sbrk() (via makeSuperblock).

void * ssf_hoard_thread_heap::malloc (const size_t size)
{
  const int sizeclass = sizeClass (size);
  ssf_hoard_block * b = NULL;

  lock();

  // Look for a free block.
  // We usually have memory locally so we first look for space in the
  // superblock list.

  ssf_hoard_superblock * sb = findAvailableSuperblock (sizeclass, b, _pHeap);
  if (sb == NULL) {
    
    // We don't have memory locally.
    // Try to get more from the process heap.
    
    assert (_pHeap);
    sb = _pHeap->acquire ((int) sizeclass, this);

    // If we didn't get any memory from the process heap,
    // we'll have to allocate our own superblock.
    if (sb == NULL) {
      sb = ssf_hoard_superblock::makeSuperblock (sizeclass, _pHeap);
      if (sb == NULL) {
	// We're out of memory!
	unlock ();
	return NULL;
      }
#if SSF_HOARD_HEAP_LOG
      // Record the memory allocation.
      MemoryRequest m;
      m.allocate ((int) sb->getNumBlocks() * (int) sizeFromClass(sb->getBlockSizeClass()));
      _pHeap->getLog(getIndex()).append(m);
#endif
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
      _pHeap->setAllocated (0, sb->getNumBlocks() * sizeFromClass(sb->getBlockSizeClass()));
#endif
    }
    
    // Get a block from the superblock.
    b = sb->getBlock ();
    assert (b != NULL);

    // Insert the superblock into our list.
    insertSuperblock (sizeclass, sb, _pHeap);
  }

  assert (b != NULL);
  assert (b->isValid());
  assert (sb->isValid());

  b->markAllocated();

#if SSF_HOARD_HEAP_LOG
  MemoryRequest m;
  m.malloc ((void *) (b + 1), align(size));
  _pHeap->getLog(getIndex()).append(m);
#endif
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  b->setRequestedSize (align(size));
  _pHeap->setAllocated (align(size), 0);
#endif

  unlock();
  
  // Skip past the block header and return the pointer.
  return (void *) (b + 1);
}


void * ssf_hoard_thread_heap::memalign (size_t alignment,
			     size_t size)
{
  // Calculate the amount of space we need
  // to satisfy the alignment requirements.

  size_t newSize;

  // If the alignment is less than the required alignment,
  // just call malloc.
  if (alignment <= SSF_HOARD_ALIGNMENT) {
    return this->malloc (size);
  }

  if (alignment < sizeof(ssf_hoard_block)) {
    alignment = sizeof(ssf_hoard_block);
  }

  // Alignment must be a power of two!
  assert ((alignment & (alignment - 1)) == 0);

  // Leave enough room to align the block within the malloced space.
  newSize = size + sizeof(ssf_hoard_block) + alignment;

  // Now malloc the space up with a little extra (we'll put the block
  // pointer in right behind the allocated space).

  void * ptr = this->malloc (newSize);
  if ((((unsigned long) ptr) & -((long) alignment)) == 0) {
    // ptr is already aligned, so return it.
    assert (((unsigned long) ptr % alignment) == 0);
    return ptr;

  } else {

    // Align ptr.
    char * newptr = (char *)
      (((unsigned long) ptr + alignment - 1) & -((long) alignment));

    // If there's not enough room for the block header, skip to the
    // next aligned space within the block..
    if ((unsigned long) newptr - (unsigned long) ptr < sizeof(ssf_hoard_block)) {
      newptr += alignment;
    }
    assert (((unsigned long) newptr % alignment) == 0);

    // Copy the block from the start of the allocated memory.
    ssf_hoard_block * b = ((ssf_hoard_block *) ptr - 1);

    assert (b->isValid());
    assert (b->getSuperblock()->isValid());
    
    // Make sure there's enough room for the block header.
    assert (((unsigned long) newptr - (unsigned long) ptr) >= sizeof(ssf_hoard_block));

    ssf_hoard_block * p = ((ssf_hoard_block *) newptr - 1);

    // Make sure there's enough room allocated for size bytes.
    assert (((unsigned long) p - sizeof(ssf_hoard_block)) >= (unsigned long) b);

    if (p != b) {
      assert ((unsigned long) newptr > (unsigned long) ptr);
      // Copy the block header.
      *p = *b;
      assert (p->isValid());
      assert (p->getSuperblock()->isValid());
      
      // Set the next pointer to point to b with the 1 bit set.
      // When this block is freed, it will be treated specially.
      p->setNext ((ssf_hoard_block *) ((unsigned long) b | 1));

    } else {
      assert (ptr != newptr);
    }

    assert (((unsigned long) ptr + newSize) >= ((unsigned long) newptr + size));
    return newptr;
  }
}


size_t ssf_hoard_thread_heap::objectSize (void * ptr) 
{
  // Find the superblock pointer.
  
  ssf_hoard_block * b = ((ssf_hoard_block *) ptr - 1);
  assert (b->isValid());
  ssf_hoard_superblock * sb = b->getSuperblock ();
  assert (sb);
  
  // Return the size.
  return sizeFromClass (sb->getBlockSizeClass());
}


void ssf_hoard_thread_heap::setpHeap (ssf_hoard_process_heap * p) 
{
  _pHeap = p; 
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/
