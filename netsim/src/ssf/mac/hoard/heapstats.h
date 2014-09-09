///-*-C++-*-//////////////////////////////////////////////////////////////////
//
// Hoard: A Fast, Scalable, and Memory-Efficient Allocator
//        for Shared-Memory Multiprocessors
// Contact author: Emery Berger, http://www.cs.utexas.edu/users/emery
//
// Copyright (c) 1998-2000, The University of Texas at Austin.
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

#ifndef _PRIME_SSF_HOARD_HEAPSTATS_H_
#define _PRIME_SSF_HOARD_HEAPSTATS_H_

#include <stdio.h>
#include <assert.h>

#include "mac/hoard/config.h"

#if PRIME_SSF_HOARD

namespace prime {
namespace ssf {

class ssf_hoard_heap_stats {
public:

  ssf_hoard_heap_stats (void)
    : 
    U (0),
    A (0)
#if SSF_HOARD_HEAP_STATS
    ,Umax (0),
    Amax (0)
#endif
  {}

  inline const ssf_hoard_heap_stats& operator= (const ssf_hoard_heap_stats& p);

  inline void incStats (int updateU, int updateA);
  inline void incUStats (void);

  inline void decStats (int updateU, int updateA);
  inline void decUStats (void);
  inline void decUStats (int& Uout, int& Aout);

  inline void getStats (int& Uout, int& Aout);


#if SSF_HOARD_HEAP_STATS
  
  inline int getUmax (void);
  inline int getAmax (void);

#endif


private:

  // U and A *must* be the first items in this class --
  // we will depend on this to atomically update them.

  int U;	// Memory in use.
  int A;	// Memory allocated.

#if SSF_HOARD_HEAP_STATS
  int Umax;
  int Amax;
#endif
};


inline void ssf_hoard_heap_stats::incStats (int updateU, int updateA) 
{
  assert (updateU >= 0);
  assert (updateA >= 0);
  assert (U <= A);
  assert (U >= 0);
  assert (A >= 0);
  U += updateU;
  A += updateA;
#if SSF_HOARD_HEAP_STATS
  Amax = SSF_HOARD_MAX (Amax, A);
  Umax = SSF_HOARD_MAX (Umax, U);
#endif
  assert (U <= A);
  assert (U >= 0);
  assert (A >= 0);
}


inline void ssf_hoard_heap_stats::incUStats (void)
{
  assert (U < A);
  assert (U >= 0);
  assert (A >= 0);
  U++;
#if SSF_HOARD_HEAP_STATS
  Umax = SSF_HOARD_MAX (Umax, U);
#endif
  assert (U >= 0);
  assert (A >= 0);
}


inline void ssf_hoard_heap_stats::decStats (int updateU, int updateA)
{
  assert (updateU >= 0);
  assert (updateA >= 0);
  assert (U <= A);
  assert (U >= updateU);
  assert (A >= updateA);
  U -= updateU;
  A -= updateA;
  assert (U <= A);
  assert (U >= 0);
  assert (A >= 0);
}


inline void ssf_hoard_heap_stats::decUStats (int& Uout, int& Aout) 
{
  assert (U <= A);
  assert (U > 0);
  assert (A >= 0);
  U--;
  Uout = U;
  Aout = A;
  assert (U >= 0);
  assert (A >= 0);
}


inline void ssf_hoard_heap_stats::decUStats (void) 
{
  assert (U <= A);
  assert (U > 0);
  assert (A >= 0);
  U--;
}


inline void ssf_hoard_heap_stats::getStats (int& Uout, int& Aout) 
{
  assert (U >= 0);
  assert (A >= 0);
  Uout = U;
  Aout = A;
  assert (U <= A);
  assert (U >= 0);
  assert (A >= 0);
}


#if SSF_HOARD_HEAP_STATS
inline int ssf_hoard_heap_stats::getUmax (void) 
{
  return Umax;
}


inline int ssf_hoard_heap_stats::getAmax (void) 
{
  return Amax;
}
#endif // SSF_HOARD_HEAP_STATS

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_HEAPSTATS_H_*/
