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
  superblock.h
  ------------------------------------------------------------------------
  The superblock class controls a number of blocks (which are
  allocatable units of memory).
  ------------------------------------------------------------------------
  @(#) $Id: mac/hoard/superblock.h,v 1.1.1.1 2004/07/21 21:01:32 jasonliu Exp $
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

#ifndef _PRIME_SSF_HOARD_SUPERBLOCK_H_
#define _PRIME_SSF_HOARD_SUPERBLOCK_H_

#include <new>

#include "mac/hoard/config.h"

#if PRIME_SSF_HOARD

#include "mac/hoard/archspec.h"
#include "mac/hoard/block.h"

namespace prime {
namespace ssf {

class ssf_hoard_heap; // forward declaration
class ssf_hoard_process_heap; // forward declaration

class ssf_hoard_superblock {

public:

  // Construct a superblock for a given size class and set the heap
  // owner.
  ssf_hoard_superblock (int numblocks,
	      int sizeclass,
	      ssf_hoard_heap * owner);

  ~ssf_hoard_superblock (void) {
    ssf_hoard_lock_destroy (_upLock);
  }

  // Make (allocate or re-use) a superblock for a given size class.
  static ssf_hoard_superblock * makeSuperblock (int sizeclass, ssf_hoard_process_heap * pHeap);

  // Find out who allocated this superblock.
  inline ssf_hoard_heap * getOwner (void);

  // Set the superblock's owner.
  inline void setOwner (ssf_hoard_heap * o);

  // Get a block from the superblock.
  inline ssf_hoard_block * getBlock (void);

  // Put a block back in the superblock.
  inline void putBlock (ssf_hoard_block * b);

  // How many blocks are available?
  inline int getNumAvailable (void);

  // How many blocks are there, in total?
  inline int getNumBlocks (void);

  // What size class are blocks in this superblock?
  inline int getBlockSizeClass (void);

  // Insert this superblock before the next one.
  inline void insertBefore (ssf_hoard_superblock * nextSb);

  // Return the next pointer (to the next superblock in the list).
  inline ssf_hoard_superblock * const getNext (void);

  // Return the prev pointer (to the previous superblock in the list).
  inline ssf_hoard_superblock * const getPrev (void);

  // Return the 'fullness' of this superblock.
  inline int getFullness (void);
  
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  // Return the amount of waste in every allocated block.
  int getMaxInternalFragmentation (void);
#endif

  // Remove this superblock from its linked list.
  inline void remove (void);

  // Is this superblock valid? (i.e.,
  // does it have the right magic number?)
  inline int isValid (void);

  inline void upLock (void) {
    ssf_hoard_lock (_upLock);
  }

  inline void upUnlock (void) {
    ssf_hoard_unlock (_upLock);
  }

  // Compute the 'fullness' of this superblock.
  inline void computeFullness (void);

private:


  // Disable copying and assignment.

  ssf_hoard_superblock (const ssf_hoard_superblock&);
  const ssf_hoard_superblock& operator= (const ssf_hoard_superblock&);

  // Used for sanity checking.
  enum { SSF_HOARD_SUPERBLOCK_MAGIC = 0xCAFEBABE };

#if PRIME_SSF_HOARD_HEAP_DEBUG
  unsigned long _magic;
#endif

  const int 	_sizeClass;	// The size class of blocks in the superblock.
  const int 	_numBlocks;	// The number of blocks in the superblock.
  int		_numAvailable;	// The number of blocks available.
  int		_fullness;	// How full is this superblock?
				// (which SUPERBLOCK_FULLNESS group is it in)
  ssf_hoard_block *	_freeList;	// A pointer to the first free block.
  ssf_hoard_heap * 	_owner;		// The heap who owns this superblock.
  ssf_hoard_superblock * 	_next;		// The next superblock in the list.
  ssf_hoard_superblock * 	_prev;		// The previous superblock in the list.

  bool dirtyFullness;

  ssf_hoard_lock_type _upLock;	// Lock this when moving a superblock to the global (process) heap.

  // We insert a cache pad here to prevent false sharing with the
  // first block (which immediately follows the superblock).

  double _pad[SSF_HOARD_CACHE_LINE / sizeof(double)];
};


ssf_hoard_heap * ssf_hoard_superblock::getOwner (void)
{
  assert (isValid());
  ssf_hoard_heap * o = _owner;
  return o;
}


void ssf_hoard_superblock::setOwner (ssf_hoard_heap * o) 
{
  assert (isValid());
  _owner = o;
}


ssf_hoard_block * ssf_hoard_superblock::getBlock (void)
{
  assert (isValid());
  // Pop off a block from this superblock's freelist,
  // if there is one available.
  if (_freeList == NULL) {
    // The freelist is empty.
    assert (getNumAvailable() == 0);
    return NULL;
  }
  assert (getNumAvailable() > 0);
  ssf_hoard_block * b = _freeList;
  _freeList = _freeList->getNext();
  _numAvailable--;

  b->setNext(NULL);

  dirtyFullness = true;
  //  computeFullness();

  return b;
}


void ssf_hoard_superblock::putBlock (ssf_hoard_block * b)
{
  assert (isValid());
  // Push a block onto the superblock's freelist.
  assert (b->isValid());
  assert (b->getSuperblock() == this);
  assert (getNumAvailable() < getNumBlocks());
  b->setNext (_freeList);
  _freeList = b;
  _numAvailable++;

  dirtyFullness = true;
  //  computeFullness();
}

int ssf_hoard_superblock::getNumAvailable (void)
{
  assert (isValid());
  return _numAvailable;
}


int ssf_hoard_superblock::getNumBlocks (void)
{
  assert (isValid());
  return _numBlocks;
}


int ssf_hoard_superblock::getBlockSizeClass (void)
{
  assert (isValid());
  return _sizeClass;
}


ssf_hoard_superblock * const ssf_hoard_superblock::getNext (void)
{
  assert (isValid());
  return _next; 
}

ssf_hoard_superblock * const ssf_hoard_superblock::getPrev (void)
{
  assert (isValid());
  return _prev; 
}


void ssf_hoard_superblock::insertBefore (ssf_hoard_superblock * nextSb) {
  assert (isValid());
  // Insert this superblock before the next one (nextSb).
  assert (nextSb != this);
  _next = nextSb;
  if (nextSb) {
    _prev = nextSb->_prev;
    nextSb->_prev = this;
  }
}


void ssf_hoard_superblock::remove (void) {
  // Remove this superblock from a doubly-linked list.
  if (_next) {
    _next->_prev = _prev;
  }
  if (_prev) {
    _prev->_next = _next;
  }
  _prev = NULL;
  _next = NULL;
}


int ssf_hoard_superblock::isValid (void)
{
  assert (_numBlocks > 0);
  assert (_numAvailable <= _numBlocks);
  assert (_sizeClass >= 0);
  return 1;
}


void ssf_hoard_superblock::computeFullness (void)
{
  assert (isValid());
  _fullness = (((SSF_HOARD_SUPERBLOCK_FULLNESS_GROUP - 1)
		* (getNumBlocks() - getNumAvailable())) / getNumBlocks());
}

int ssf_hoard_superblock::getFullness (void)
{
  assert (isValid());
  if (dirtyFullness) {
    computeFullness();
    dirtyFullness = false;
  }
  return _fullness;
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_SUPERBLOCK_H_*/
