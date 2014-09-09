/*
 * arena :- shared heap memory management.
 * 
 * This file contains a number of global routines that are used to
 * manage the shared memory. The C++ new and delete operators in SSF
 * are replaced with these functions to enable shared access to the
 * heap. These routines are internal to SSF implementation; the users
 * are not supposed to use them directly.
 */

#ifndef __PRIME_SSF_ARENA_H__
#define __PRIME_SSF_ARENA_H__

#include <stdio.h>

namespace prime {
namespace ssf {

  /*
   * Each processor sharing this arena should initialize it in the
   * beginning. A processor id is passed as an argument.
   */
  extern void ssf_arena_init(int pid);

  /*
   * Similar to malloc, the function allocates a memory chunk of the
   * given size from the shared heap. A pointer to the allocated
   * memory is returned if the allocation is successful. Otherwise,
   * the execution is aborted and an error message is generated to
   * the standard error.
   */
  extern void* ssf_arena_malloc(size_t size);

  /*
   * Similar to calloc, the function allocates a memory chunk large
   * enough for hold an array of n elements of elmsz bytes each, and
   * returns a pointer to the allocated memory.  The memory is
   * initialized to zero. The execution will be aborted if the request
   * fails and an error message is generated to the standard error
   * stream.
   */
  extern void* ssf_arena_calloc(size_t n, size_t elmsz);

  /*
   * The function differs from ssf_arena_malloc: the memory block
   * allocated by ssf_arena_malloc can be reclaimed during the simulation,
   * while the memory block allocated by this function cannot. The
   * memory must persist throughout the entire simulation.  The reason
   * for having this function is that it entails less overhead,
   * suitable for data structures that have a life time spanning over
   * the entire simulation.
   */
  extern void* ssf_arena_malloc_fixed(size_t size);

  /*
   * Similar to the standard free system routine, this function
   * reclaims the memory space pointed to by s, which must have been
   * returned by a previous call to ssf_arena_malloc, or ssf_arena_calloc. If
   * the memory block pointed by s has already been freed before,
   * undefined behaviour occurs. If s is NULL, no operation is
   * performed. It's an error to reclaim memory allocated by
   * ssf_arena_malloc_fixed or from the standard system malloc routine.
   */
  extern void ssf_arena_free(void* s);

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_ARENA_H__*/

/*
 * Copyright (c) 2007-2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 * 
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * noninfringement. In no event shall Florida International University be
 * liable for any claim, damages or other liability, whether in an
 * action of contract, tort or otherwise, arising from, out of or in
 * connection with the software or the use or other dealings in the
 * software.
 * 
 * This software is developed and maintained by
 *
 *   The PRIME Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, FL 33199, USA
 *
 * Contact Jason Liu <liux@cis.fiu.edu> for questions regarding the use
 * of this software.
 */
