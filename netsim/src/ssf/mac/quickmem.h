/**
 * \file quickmem.h
 * \brief Header file for quick memory management.
 *
 * Quick memory is a memory management layer that provides fast memory
 * allocation and deallocation services at each processor. The fast
 * speed comes at the cost of additional memory consumption due to
 * fragmentation. The memory blocks are chosen from free memory chunks
 * with sizes rounded up to the power of two. The quick memory layer
 * is only active when PRIME_SSF_QUICKMEM macro is defined.
 */

#ifndef __PRIME_SSF_QUICKMEM_H__
#define __PRIME_SSF_QUICKMEM_H__

#if PRIME_SSF_QUICKMEM

#include <stdio.h>

namespace prime {
namespace ssf {

  /**
   * Initialize quick memory layer at the beginning of
   * simulation. This function must be called by each processor.
   */
  extern void ssf_quickmem_init();

  /**
   * Wrap up the quick memory layer in the end of the program. The
   * function is expected to be called by each processor.
   */
  extern void ssf_quickmem_wrapup();

  /**
   * This function allocates a memory block of the given size and
   * returns the pointer to the new memory block. Quick memory finds
   * the smallest available memory chunk that fits the requirement.
   */
  extern void* ssf_quickmem_malloc(size_t size);

  /**
   * This function returns the memory block back to the quick memory
   * management. Note that previously the returned memory block must
   * be accompanied with size, which must be consistent with the size
   * of the memory block when allocated; it's deprecated. The size
   * argument is still here for compatibility reasons; but it is not
   * used and one can simply ignore it.
   */
  extern void ssf_quickmem_free(void* p, size_t size = 0);

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_QUICKMEM*/

#endif /*__PRIME_SSF_QUICKMEM_H__*/

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
