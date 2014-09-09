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
  superblock.cpp
  ------------------------------------------------------------------------
  The superblock class controls a number of blocks (which are
  allocatable units of memory).
  ------------------------------------------------------------------------
  @(#) $Id: mac/hoard/superblock.cc,v 1.1.1.1 2004/07/21 21:01:32 jasonliu Exp $
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

#include <string.h>

#include "mac/hoard/heap.h"
#include "mac/hoard/processheap.h"
#include "mac/hoard/superblock.h"

#if PRIME_SSF_HOARD

namespace prime {
namespace ssf {

ssf_hoard_superblock::ssf_hoard_superblock (int numBlocks,	// The number of blocks in the sb.
			int szclass,	// The size class of the blocks.
			ssf_hoard_heap * o)	// The heap that "owns" this sb.
  :
#if PRIME_SSF_HOARD_HEAP_DEBUG
    _magic (SSF_HOARD_SUPERBLOCK_MAGIC),
#endif
    _sizeClass (szclass),
    _numBlocks (numBlocks),
    _numAvailable (0),
    _fullness (0),
    _freeList (NULL),
    _owner (o),
    _next (NULL),
    _prev (NULL),
    dirtyFullness (true)
{
  assert (_numBlocks >= 1);

  // Determine the size of each block.
  const int blksize =
    ssf_hoard_heap::align (sizeof(ssf_hoard_block) + ssf_hoard_heap::sizeFromClass(_sizeClass));

  // Make sure this size is in fact aligned.
  assert ((blksize & ssf_hoard_heap::SSF_HOARD_ALIGNMENT_MASK) == 0);

  // Set the first block to just past this superblock header.
  ssf_hoard_block * b
    = (ssf_hoard_block *) ssf_hoard_heap::align ((unsigned long) (this + 1));

  // Initialize all the blocks,
  // and insert the block pointers into the linked list.
  for (int i = 0; i < _numBlocks; i++) {
    // Make sure the block is on a double-word boundary.
    // (the following line offended SGI native compiler!)
    //assert (((unsigned int) b & ssf_hoard_heap::SSF_HOARD_ALIGNMENT_MASK) == 0);
    new (b) ssf_hoard_block (this);
    assert (b->getSuperblock() == this);
    b->setNext (_freeList);
    _freeList = b;
    b = (ssf_hoard_block *) ((char *) b + blksize);
  }
  _numAvailable = _numBlocks;
  //  computeFullness();
  assert ((unsigned long) b <= ssf_hoard_heap::align (sizeof(ssf_hoard_superblock) + blksize * _numBlocks) + (unsigned long) this);

  ssf_hoard_lock_init (_upLock);
}


ssf_hoard_superblock * ssf_hoard_superblock::makeSuperblock (int sizeclass,
					 ssf_hoard_process_heap * pHeap)
{
  // We need to get more memory.

  char * buf;
  int numBlocks = ssf_hoard_heap::numBlocks(sizeclass);

  // Compute how much memory we need.
  unsigned long moreMemory;
  size_t sz = ssf_hoard_heap::sizeFromClass(sizeclass);

  if (numBlocks > 1) {
	// ssf_hoard_heap::align(sizeof(ssf_hoard_superblock) + (ssf_hoard_heap::align (sizeof(ssf_hoard_block) + sz)) * numBlocks) <= ssf_hoard_heap::SSF_HOARD_SUPERBLOCK_SIZE) {

    moreMemory = ssf_hoard_heap::SSF_HOARD_SUPERBLOCK_SIZE;
    assert (moreMemory >= ssf_hoard_heap::align(sizeof(ssf_hoard_superblock) + (ssf_hoard_heap::align (sizeof(ssf_hoard_block) + sz)) * numBlocks));

    // Get some memory from the process heap.
    buf = (char *) ssf_hoard_get_memory (moreMemory);
  } else {
    // One object.
    assert (numBlocks == 1);

    size_t blksize = ssf_hoard_heap::align (sizeof(ssf_hoard_block) + sz);
    moreMemory = ssf_hoard_heap::align (sizeof(ssf_hoard_superblock) + blksize);

    // Get space from the system.
    buf = (char *) ssf_hoard_get_memory (moreMemory);
  }
 
  // Make sure that we actually got the memory.
  if (buf == NULL) {
    return 0;
  }
  buf = (char *) ssf_hoard_heap::align ((unsigned long) buf);

  // Make sure this buffer is double-word aligned.
  assert (buf == (char *) ssf_hoard_heap::align ((unsigned long) buf));
  assert ((((unsigned long) buf) & ssf_hoard_heap::SSF_HOARD_ALIGNMENT_MASK) == 0);

  // Instantiate the new superblock in the buffer.
  ssf_hoard_superblock * sb = new (buf) ssf_hoard_superblock (numBlocks, sizeclass, NULL);

  return sb;
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/
