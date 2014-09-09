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

#ifndef _PRIME_SSF_HOARD_BLOCK_H_
#define _PRIME_SSF_HOARD_BLOCK_H_

#include <assert.h>
#include "mac/hoard/config.h"

#if PRIME_SSF_HOARD

namespace prime {
namespace ssf {

class ssf_hoard_superblock;

class ssf_hoard_block {
public:

  ssf_hoard_block (ssf_hoard_superblock * sb) : 
#if PRIME_SSF_HOARD_HEAP_DEBUG
    _magic (SSF_HOARD_FREE_BLOCK_MAGIC),
#endif
    _next (NULL),
    _mySuperblock (sb)
  {}

  ssf_hoard_block& operator= (const ssf_hoard_block& b) {
#if PRIME_SSF_HOARD_HEAP_DEBUG
    _magic = b._magic;
#endif
    _next = b._next;
    _mySuperblock = b._mySuperblock;
#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
    _requestedSize = b._requestedSize;
#endif
    return *this;
  }

  enum { SSF_HOARD_ALLOCATED_BLOCK_MAGIC = 0xcafecafe,
	 SSF_HOARD_FREE_BLOCK_MAGIC = 0xbabebabe };

  // Mark this block as free.
  inline void markFree (void);

  // Mark this block as allocated.
  inline void markAllocated (void);

  // Is this block valid? (i.e.,
  // does it have the right magic number?)
  inline const int isValid (void) const;

  // Return the block's superblock pointer.
  inline ssf_hoard_superblock * getSuperblock (void);

#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  void setRequestedSize (size_t s) {   
    _requestedSize = s;
  }

  size_t getRequestedSize (void) { return _requestedSize; }
#endif

  void setNext (ssf_hoard_block * b) { _next = b; }
  ssf_hoard_block * getNext (void) { return _next; }

private:

#if PRIME_SSF_HOARD_HEAP_DEBUG
  union {
    unsigned long _magic;
    double _d3; // For alignment.
  };
#endif

  ssf_hoard_block* _next;	// The next block in a linked-list of blocks.
  ssf_hoard_superblock*	_mySuperblock;	// A pointer to my superblock.

#if PRIME_SSF_HOARD_HEAP_FRAG_STATS
  union {
    double _d4; // This is just for alignment purposes.
    size_t _requestedSize;	// The amount of space requested (vs. allocated).
  };
#endif

  // Disable copying.

  ssf_hoard_block (const ssf_hoard_block&);
};

ssf_hoard_superblock * ssf_hoard_block::getSuperblock (void)
{
#if PRIME_SSF_HOARD_HEAP_DEBUG
  assert (isValid());
#endif
  return _mySuperblock;
}


void ssf_hoard_block::markFree (void)
{
#if PRIME_SSF_HOARD_HEAP_DEBUG
  assert (_magic == SSF_HOARD_ALLOCATED_BLOCK_MAGIC);
  _magic = SSF_HOARD_FREE_BLOCK_MAGIC;
#endif
}


void ssf_hoard_block::markAllocated (void)
{
#if PRIME_SSF_HOARD_HEAP_DEBUG
  assert (_magic == SSF_HOARD_FREE_BLOCK_MAGIC);
  _magic = SSF_HOARD_ALLOCATED_BLOCK_MAGIC;
#endif
}


const int ssf_hoard_block::isValid (void) const 
{
#if PRIME_SSF_HOARD_HEAP_DEBUG
  return ((_magic == SSF_HOARD_FREE_BLOCK_MAGIC)
	  || (_magic == SSF_HOARD_ALLOCATED_BLOCK_MAGIC));
#else
  return 1;
#endif
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_BLOCK_H_*/
