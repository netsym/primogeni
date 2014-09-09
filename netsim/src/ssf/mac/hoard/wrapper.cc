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
  wrapper.cpp
  ------------------------------------------------------------------------
  Implementations of malloc(), free(), etc. in terms of hoard.
  This lets us link in hoard in place of the stock malloc.
  ------------------------------------------------------------------------
  @(#) $Id: mac/hoard/wrapper.cc,v 1.1.1.1 2004/07/21 21:01:32 jasonliu Exp $
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

#include "mac/segments.h"
#include "mac/hoard/config.h"
#include "mac/hoard/archspec.h"
#include "mac/hoard/threadheap.h"
#include "mac/hoard/processheap.h"

#if PRIME_SSF_HOARD

#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <new>

//
// Access exactly one instance of the process heap
// (which contains the thread heaps).
// We create this object dynamically to avoid bloating the object code.
//

SSF_DECLARE_SHARED(void*, hoard_allocator);

namespace prime {
namespace ssf {

void SSF_HOARD_INIT()
{
  char* buf = (char*)ssf_hoard_get_memory(sizeof(ssf_hoard_process_heap));
  SSF_USE_SHARED(hoard_allocator) = new (buf) ssf_hoard_process_heap;
}

void* SSF_HOARD_MALLOC(size_t sz)
{
  if (sz == 0) {
    sz = 1;
  }
  void* addr = ((ssf_hoard_process_heap*)SSF_USE_SHARED(hoard_allocator))->
    getHeap(((ssf_hoard_process_heap*)SSF_USE_SHARED(hoard_allocator))->
	    getHeapIndex()).malloc(sz);
  return addr;
}

void* SSF_HOARD_CALLOC (size_t nelem, size_t elsize)
{
  size_t sz = nelem * elsize;
  if (sz == 0) {
    sz = 1;
  }
  void* ptr = ((ssf_hoard_process_heap*)SSF_USE_SHARED(hoard_allocator))->
    getHeap(((ssf_hoard_process_heap*)SSF_USE_SHARED(hoard_allocator))->
	    getHeapIndex()).malloc(sz);
  memset(ptr, 0, sz);
  return ptr;
}

void SSF_HOARD_FREE(void* ptr)
{
  ((ssf_hoard_process_heap*)SSF_USE_SHARED(hoard_allocator))->free(ptr);
}

void* SSF_HOARD_MEMALIGN(size_t alignment, size_t size)
{
  void* addr = ((ssf_hoard_process_heap*)SSF_USE_SHARED(hoard_allocator))->
    getHeap(((ssf_hoard_process_heap*)SSF_USE_SHARED(hoard_allocator))->
	    getHeapIndex()).
    memalign(alignment, size);
  return addr;
}

void* SSF_HOARD_VALLOC(size_t size)
{
  return SSF_HOARD_MEMALIGN(ssf_hoard_get_page_size(), size);
}

void* SSF_HOARD_REALLOC(void* ptr, size_t sz)
{
  if (ptr == NULL) {
    return SSF_HOARD_MALLOC(sz);
  }

  if (sz == 0) {
    SSF_HOARD_FREE(ptr);
    return NULL;
  }

  // If the existing object can hold the new size,
  // just return it.

  size_t objSize = ssf_hoard_thread_heap::objectSize(ptr);
  if (objSize >= sz) {
    return ptr;
  }

  // Allocate a new block of size sz.

  void* buf = SSF_HOARD_MALLOC(sz);

  // Copy the contents of the original object
  // up to the size of the new block.

  size_t minSize = (objSize < sz) ? objSize : sz;
  memcpy(buf, ptr, minSize);

  // Free the old block.

  SSF_HOARD_FREE(ptr);

  // Return a pointer to the new one.

  return buf;
}

size_t SSF_HOARD_GET_USABLE_SIZE(void* ptr)
{
  return ssf_hoard_thread_heap::objectSize(ptr);
}

size_t SSF_HOARD_GET_USABLE_SIZE_WITH_OVERHEAD(void* ptr)
{
  return ssf_hoard_thread_heap::objectSize(ptr) + sizeof(ssf_hoard_block);
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_HOARD*/
