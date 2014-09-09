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

#ifndef _PRIME_SSF_HOARD_ARCHSPEC_H_
#define _PRIME_SSF_HOARD_ARCHSPEC_H_

#include "mac/hoard/config.h"

#if PRIME_SSF_HOARD

// Wrap architecture-specific functions.

namespace prime {
namespace ssf {

  typedef ssf_mutex ssf_hoard_lock_type;

  int	ssf_hoard_get_thread_id (void);

  void	ssf_hoard_lock_init (ssf_hoard_lock_type& lock);
  void	ssf_hoard_lock (ssf_hoard_lock_type& lock);
  void	ssf_hoard_unlock (ssf_hoard_lock_type& lock);
  inline void  ssf_hoard_lock_destroy (ssf_hoard_lock_type&) {}

  int	ssf_hoard_get_page_size (void);
  void*	ssf_hoard_get_memory (long size);
  void	ssf_hoard_free_memory (void* ptr);

  void  ssf_hoard_yield (void);

  int	ssf_hoard_get_nprocs (void);

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/

#endif /*_PRIME_SSF_HOARD_ARCHSPEC_H_*/
