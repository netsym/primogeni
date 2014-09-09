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
  heap.h
  ------------------------------------------------------------------------
  ssf_hoard_heap, the base class for ssf_hoard_thread_heap and ssf_hoard_process_heap.
  ------------------------------------------------------------------------
  @(#) $Id: mac/hoard/heap.h,v 1.1.1.1 2004/07/21 21:01:32 jasonliu Exp $
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

#ifndef _PRIME_SSF_HOARD_HEAP_H_
#define _PRIME_SSF_HOARD_HEAP_H_

#include "mac/hoard/config.h"

#if PRIME_SSF_HOARD

#include "mac/hoard/archspec.h"
#include "mac/hoard/superblock.h"
#include "mac/hoard/heapstats.h"

namespace prime {
namespace ssf {

class ssf_hoard_process_heap; // forward declaration

class ssf_hoard_heap {

public:

  ssf_hoard_heap (void);

  // A superblock that holds more than one object must hold at least
  // this many bytes.
  enum { SSF_HOARD_SUPERBLOCK_SIZE = 8192 };

  // A thread heap must be at least 1/EMPTY_FRACTION empty before we
  // start returning superblocks to the process heap.
  enum { SSF_HOARD_EMPTY_FRACTION = SSF_HOARD_SUPERBLOCK_FULLNESS_GROUP - 1 };

  // Reset value for the least-empty bin.  The last bin
  // (SUPERBLOCK_FULLNESS_GROUP-1) is for completely full superblocks,
  // so we use the next-to-last bin.
  enum { SSF_HOARD_RESET_LEAST_EMPTY_BIN = SSF_HOARD_SUPERBLOCK_FULLNESS_GROUP - 2 };

  // The number of empty superblocks that we allow any thread heap to
  // hold once the thread heap has fallen below 1/EMPTY_FRACTION
  // empty.
  enum { SSF_HOARD_MAX_EMPTY_SUPERBLOCKS = 1 } ; // EMPTY_FRACTION / 2 };

  // The maximum number of thread heaps we allow.  (NOT the maximum
  // number of threads -- Hoard imposes no such limit.)  This must be
  // a power of two! NB: This number is twice the maximum number of
  // PROCESSORS supported by Hoard.
  enum { SSF_HOARD_MAX_HEAPS = 64 };

  // ANDing with this rounds to MAX_HEAPS.
  enum { SSF_HOARD_MAX_HEAPS_MASK = SSF_HOARD_MAX_HEAPS - 1 };

  //
  // The number of size classes.
  //

  enum { SSF_HOARD_SIZE_CLASSES = 132 };

  // Every object is aligned so that it can always hold a double.
  //#ifdef MALLOC_ALIGNMENT
  //enum { ALIGNMENT = MALLOC_ALIGNMENT };
  //#else
  enum { SSF_HOARD_ALIGNMENT = sizeof(double) };
  //#endif

  // ANDing with this rounds to ALIGNMENT.
  enum { SSF_HOARD_ALIGNMENT_MASK = SSF_HOARD_ALIGNMENT - 1};

  // Used for sanity checking.
  enum { SSF_HOARD_HEAP_MAGIC = 0x0badcafe };

  // Get the usage and allocated statistics.
  inline void getStats (int sizeclass, int& U, int& A);


#if SSF_HOARD_HEAP_STATS
  // How much is the maximum ever in use for this size class?
  inline int maxInUse (int sizeclass);

  // How much is the maximum memory allocated for this size class?
  inline int maxAllocated (int sizeclass);
#endif

  // Insert a superblock into our list.
  void insertSuperblock (int sizeclass,
			 ssf_hoard_superblock * sb,
			 ssf_hoard_process_heap * pHeap);

  // Remove the superblock with the most free space.
  ssf_hoard_superblock * removeMaxSuperblock (int sizeclass);

  // Find an available superblock (i.e., with some space in it).
  ssf_hoard_superblock * findAvailableSuperblock (int sizeclass,
						  ssf_hoard_block *& b,
						  ssf_hoard_process_heap * pHeap);

  // Lock this heap.
  inline void lock (void);

  // Unlock this heap.
  inline void unlock (void);

  // Set our index number (which heap we are).
  inline void setIndex (int i);

  // Get our index number (which heap we are).
  inline int getIndex (void);

  // Free a block into a superblock.
  // This is used by ssf_hoard_process_heap::free().
  // Returns 1 iff the superblock was munmapped.
  int freeBlock (ssf_hoard_block *& b,
		 ssf_hoard_superblock *& sb,
		 int sizeclass,
		 ssf_hoard_process_heap * pHeap);

  //// Utility functions ////

  // Return the size class for a given size.
  inline static int sizeClass (const size_t sz);

  // Return the size corresponding to a given size class.
  inline static size_t sizeFromClass (const int sizeclass);

  // Return the release threshold corresponding to a given size class.
  inline static int getReleaseThreshold (const int sizeclass);

  // Return how many blocks of a given size class fit into a superblock.
  inline static int numBlocks (const int sizeclass);

  // Align a value.
  inline static size_t align (const size_t sz);

private:

  // Disable copying and assignment.

  ssf_hoard_heap (const ssf_hoard_heap&);
  const ssf_hoard_heap& operator= (const ssf_hoard_heap&);

  // Recycle a superblock.
  void recycle (ssf_hoard_superblock *);

  // Reuse a superblock (if one is available).
  ssf_hoard_superblock * reuse (int sizeclass);

  // Remove a particular superblock.
  void removeSuperblock (ssf_hoard_superblock *, int sizeclass);

public:
	// Move a particular superblock from one bin to another.
  void moveSuperblock (ssf_hoard_superblock *,
		       int sizeclass,
		       int fromBin,
		       int toBin);
private:

  // Update memory in-use and allocated statistics.
  // (*UStats = just update U.)
  inline void incStats (int sizeclass, int updateU, int updateA);

public:
  inline void incUStats (int sizeclass);
private:

  inline void decStats (int sizeclass, int updateU, int updateA);
  inline void decUStats (int sizeclass);

  //// Members ////

#if PRIME_SSF_HOARD_HEAP_DEBUG
  // For sanity checking.
  const unsigned long _magic;
#else
  #define _magic SSF_HOARD_HEAP_MAGIC
#endif

  // Heap statistics.
  ssf_hoard_heap_stats	_stats[SSF_HOARD_SIZE_CLASSES];

  // The per-heap lock.
  ssf_hoard_lock_type _lock;

  // Which heap this is (0 = the process (global) heap).
  int _index;

  // Reusable superblocks.
  ssf_hoard_superblock *	_reusableSuperblocks;
  int		_reusableSuperblocksCount;

  // Lists of superblocks.
  ssf_hoard_superblock *	_superblocks[SSF_HOARD_SUPERBLOCK_FULLNESS_GROUP][SSF_HOARD_SIZE_CLASSES];

  // The current least-empty superblock bin.
  int	_leastEmptyBin[SSF_HOARD_SIZE_CLASSES];

  // The lookup table for size classes.
  static size_t	_sizeTable[SSF_HOARD_SIZE_CLASSES];

  // The lookup table for release thresholds.
  static size_t	_threshold[SSF_HOARD_SIZE_CLASSES];

  /*
public:
  // A little helper class that we use to define some statics.
  class _initNumProcs {
  public:
  	_initNumProcs(void);
  };

  friend class _initNumProcs;
protected:
  // number of CPUs, cached
  static int _numProcessors;
  static int _numProcessorsMask;
  */
};



void ssf_hoard_heap::incStats (int sizeclass, int updateU, int updateA) {
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  assert (updateU >= 0);
  assert (updateA >= 0);
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  _stats[sizeclass].incStats (updateU, updateA);
}



void ssf_hoard_heap::incUStats (int sizeclass) {
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  _stats[sizeclass].incUStats ();
}


void ssf_hoard_heap::decStats (int sizeclass, int updateU, int updateA) {
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  assert (updateU >= 0);
  assert (updateA >= 0);
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  _stats[sizeclass].decStats (updateU, updateA);
}


void ssf_hoard_heap::decUStats (int sizeclass)
{
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  _stats[sizeclass].decUStats();
}


void ssf_hoard_heap::getStats (int sizeclass, int& U, int& A) {
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  _stats[sizeclass].getStats (U, A);
}


#if SSF_HOARD_HEAP_STATS
int ssf_hoard_heap::maxInUse (int sizeclass) {
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  return _stats[sizeclass].getUmax();
}


int ssf_hoard_heap::maxAllocated (int sizeclass) {
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  return _stats[sizeclass].getAmax(); 
}
#endif


int ssf_hoard_heap::sizeClass (const size_t sz) {
  // Find the size class for a given object size
  // (the smallest i such that _sizeTable[i] >= sz).
  int sizeclass = 0;
  while (_sizeTable[sizeclass] < sz) 
    {
      sizeclass++;
      assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
    }
  return sizeclass;
}


size_t ssf_hoard_heap::sizeFromClass (const int sizeclass) {
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  return _sizeTable[sizeclass];
}


int ssf_hoard_heap::getReleaseThreshold (const int sizeclass) {
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  return _threshold[sizeclass];
}


int ssf_hoard_heap::numBlocks (const int sizeclass) {
  assert (sizeclass >= 0);
  assert (sizeclass < SSF_HOARD_SIZE_CLASSES);
  const size_t s = sizeFromClass (sizeclass);
  assert (s > 0);
  const int blksize = align (sizeof(ssf_hoard_block) + s);
  // Compute the number of blocks that will go into this superblock.
  int nb = SSF_HOARD_MAX (1, ((SSF_HOARD_SUPERBLOCK_SIZE - sizeof(ssf_hoard_superblock)) / blksize));
  return nb;
}


void ssf_hoard_heap::lock (void) 
{
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  ssf_hoard_lock (_lock);
}


void ssf_hoard_heap::unlock (void) {
  assert (_magic == SSF_HOARD_HEAP_MAGIC);
  ssf_hoard_unlock (_lock);
}


size_t ssf_hoard_heap::align (const size_t sz)
{
  // Align sz up to the nearest multiple of ALIGNMENT.
  // This is much faster than using multiplication
  // and division.
  return (sz + SSF_HOARD_ALIGNMENT_MASK) & ~((size_t) SSF_HOARD_ALIGNMENT_MASK);
}


void ssf_hoard_heap::setIndex (int i) 
{
  _index = i; 
}


int ssf_hoard_heap::getIndex (void)
{
  return _index;
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_HEAP_H_*/
