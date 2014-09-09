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

#ifndef _PRIME_SSF_HOARD_CONFIG_H_
#define _PRIME_SSF_HOARD_CONFIG_H_

#if PRIME_SSF_HOARD

#include "mac/machines.h"

namespace prime {
namespace ssf {

#define SSF_HOARD_HEAP_LOG 0		// If non-zero, keep a log of heap accesses.

// The number of groups of superblocks we maintain based on what
// fraction of the superblock is empty. NB: This number must be at
// least 2, and is 1 greater than the EMPTY_FRACTION in heap.h.

enum { SSF_HOARD_SUPERBLOCK_FULLNESS_GROUP = 9 };

#if PRIME_SSF_DEBUG
#define PRIME_SSF_HOARD_HEAP_DEBUG 1
#define SSF_HOARD_HEAP_STATS 1
#define PRIME_SSF_HOARD_HEAP_FRAG_STATS 1
#else
#define PRIME_SSF_HOARD_HEAP_DEBUG 0		// If non-zero, keeps extra info for sanity checking.
#define SSF_HOARD_HEAP_STATS 0			// If non-zero, maintain blowup statistics.
#define PRIME_SSF_HOARD_HEAP_FRAG_STATS 0	// If non-zero, maintain fragmentation statistics.
#endif

// SSF_HOARD_CACHE_LINE = The number of bytes in a cache line.

#define SSF_HOARD_CACHE_LINE SSF_CACHE_LINE_SIZE

#define SSF_HOARD_MAX(a,b) (((a) > (b)) ? (a) : (b))

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_CONFIG_H_*/
