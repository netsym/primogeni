/*
 * memcpp :- memory management for C++ code.
 *
 * The global new and delete operators are replaced with SSF
 * subroutines that allocated and deallcated memory from the shared
 * arena. Similary, the memory used by the Standard Template Library
 * (STL) should also come from the shared arena to enable shared
 * access by multiple processors.
 */

#include <assert.h>
#include <stdlib.h>

#include "mac/segments.h"
#include "mac/arena.h"
#include "mac/memcpp.h"
#include "api/ssfexception.h"

namespace prime {
namespace ssf {

// We initialize the global variable here; the system will set the
// variable true when the arena has been created. This is done from
// the main function.
int ssf_arena_ready = 0;

#if !SSF_SHARED_HEAP_SEGMENT

void* ssf_permanent_object::operator new(size_t sz)
{
  assert(ssf_arena_ready);
  return ssf_arena_malloc_fixed(sz);
}

void ssf_permanent_object::operator delete(void* p)
{
  // permanent objects are not to be reclaimed. We do nothing here.
}

#endif /*SSF_SHARED_HEAP_SEGMENT*/

}; // namespace ssf
}; // namespace prime

#if !SSF_SHARED_HEAP_SEGMENT

#include <new>
 
using namespace prime;
using namespace ssf;

/*
 * We replace the global new and delete operators with ssf_arena_malloc
 * and ssf_arena_free, so that memory blocks are coming from and returning
 * to the shared arena.
 */

void* operator new(size_t sz) throw(std::bad_alloc)
{
  // It's entirely possible that the new operator is called before the
  // shared arena is set up. In this case, we fall back to the default
  // system malloc routine. This, however, has significant
  // ramifications that the memory block so obtained may not be shared
  // by multiple processors. This could potentially result in hideous
  // bugs in the user code.
  char* mem;
  if(ssf_arena_ready) {
    mem = (char*)ssf_arena_malloc(sz);
    assert(!SSF_ARENA_ACTIVE || (SSF_USE_SHARED_RO(arena) <= mem &&
	   mem < SSF_USE_SHARED(arena_buf)));
  } else mem = (char*)malloc(sz);
  if(!mem) throw std::bad_alloc();
  return (void*)mem;
}

void* operator new(size_t sz, const std::nothrow_t&) throw()
{
  char* mem;
  if(ssf_arena_ready) {
    mem = (char*)ssf_arena_malloc(sz);
    assert(!SSF_ARENA_ACTIVE || (SSF_USE_SHARED_RO(arena) <= mem &&
	   mem < SSF_USE_SHARED(arena_buf)));
  } else mem = (char*)malloc(sz);
  return (void*)mem; 
}

void operator delete(void* p) throw()
{
  // no effect on a NULL pointer
  if(!p) return;

  if(SSF_USE_SHARED_RO(arena) <= p && 
     p < SSF_USE_SHARED_RO(arena_end)) {
    // if the memory block is from the shared arena
    if(p < SSF_USE_SHARED(arena_buf)) {
      // if the memory block is from allocated memory segment
      ssf_arena_free(p);
    } else if(SSF_USE_SHARED(arena_bufend) <= p) {
      // if the memory block is from permanent memory segment
      ssf_throw_warning("attempting to delete permanent objects, ignored!!!");
    } else {
      // if the memory block is from free memory segment
      throw std::bad_alloc();
    }
  } else free(p);
}

void operator delete(void* p, const std::nothrow_t&) throw()
{
  // no effect on a NULL pointer
  if(!p) return;

  if(SSF_USE_SHARED_RO(arena) <= p && 
     p < SSF_USE_SHARED_RO(arena_end)) {
    // if the memory block is from the shared arena
    if(p < SSF_USE_SHARED(arena_buf)) {
      // if the memory block is from allocated memory segment
      ssf_arena_free(p);
    } else if(SSF_USE_SHARED(arena_bufend) <= p) {
      // if the memory block is from permanent memory segment
      ssf_throw_warning("attempting to delete permanent objects, ignored!!!");
    } else {
      // if the memory block is from free memory segment
      ssf_throw_warning("attempting to delete free memory block, ignored!!!");
      //throw std::bad_alloc();
    }
  } else free(p);
}

void* operator new[](size_t sz) throw(std::bad_alloc)
{
  char* mem;
  if(ssf_arena_ready) {
    mem = (char*)ssf_arena_malloc(sz);
    assert(!SSF_ARENA_ACTIVE || (SSF_USE_SHARED_RO(arena) <= mem &&
	   mem < SSF_USE_SHARED(arena_buf)));
  } else mem = (char*)malloc(sz);
  if(!mem) throw std::bad_alloc();
  return (void*)mem;
}

void* operator new[](size_t sz, const std::nothrow_t&) throw()
{
  char* mem;
  if(ssf_arena_ready) {
    mem = (char*)ssf_arena_malloc(sz);
    assert(!SSF_ARENA_ACTIVE || (SSF_USE_SHARED_RO(arena) <= mem &&
	   mem < SSF_USE_SHARED(arena_buf)));
  } else mem = (char*)malloc(sz);
  return (void*)mem;
}

void operator delete[](void* p) throw()
{
  // no effect on a NULL pointer
  if(!p) return;

  if(SSF_USE_SHARED_RO(arena) <= p && 
     p < SSF_USE_SHARED_RO(arena_end)) {
    // if the memory block is from the shared arena
    if(p < SSF_USE_SHARED(arena_buf)) {
      // if the memory block is from allocated memory segment
      ssf_arena_free(p);
    } else if(SSF_USE_SHARED(arena_bufend) <= p) {
      // if the memory block is from permanent memory segment
      ssf_throw_warning("attempting to delete permanent objects, ignored!!!");
    } else {
      // if the memory block is from free memory segment
      throw std::bad_alloc();
    }
  } else free(p);
}

void operator delete[](void* p, const std::nothrow_t&) throw()
{
  // no effect on a NULL pointer
  if(!p) return;

  if(SSF_USE_SHARED_RO(arena) <= p && 
     p < SSF_USE_SHARED_RO(arena_end)) {
    // if the memory block is from the shared arena
    if(p < SSF_USE_SHARED(arena_buf)) {
      // if the memory block is from allocated memory segment
      ssf_arena_free(p);
    } else if(SSF_USE_SHARED(arena_bufend) <= p) {
      // if the memory block is from permanent memory segment
      ssf_throw_warning("attempting to delete permanent objects, ignored!!!");
    } else {
      // if the memory block is from free memory segment
      ssf_throw_warning("attempting to delete free memory block, ignored!!!");
      //throw std::bad_alloc();
    }
  } else free(p);
}

#endif /*SSF_SHARED_HEAP_SEGMENT*/

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
