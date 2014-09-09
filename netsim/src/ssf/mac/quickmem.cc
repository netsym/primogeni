/*
 * quickmem :- quick memory management.
 */

#if PRIME_SSF_QUICKMEM

#include "mac/quickmem.h"

#include <assert.h>
#include <stdlib.h>

#include "mac/segments.h"
#include "mac/memcpp.h"
#include "sim/debug.h"
#include "api/ssfexception.h"

#define SSF_QMEM_ALIGNMENT 8 // make sure the first bucket is larger than this!
#define SSF_QMEM_NUMBUCKETS 9
#define SSF_QMEM_POOLCHUNK 32768
#define SSF_QMEM_THRESHOLD 4096

SSF_DECLARE_PRIVATE(char*,  qmem_chunklist);
SSF_DECLARE_PRIVATE(char*,  qmem_poolbegin);
SSF_DECLARE_PRIVATE(char*,  qmem_poolend);
SSF_DECLARE_PRIVATE(void**, qmem_freelist);

SSF_DECLARE_PRIVATE(size_t, qmem_objsize);

#define SSF_QMEM_CANONICALIZE(s, n) if(s <= n) return n
#define SSF_QMEM_BUCKETIZE(s, n, b) if(s <= n) return b

namespace prime {
namespace ssf {

// round the size up to the power of two.
static int ssf_quickmem_canonicalize(size_t size)
{
  SSF_QMEM_CANONICALIZE(size, 16);
  SSF_QMEM_CANONICALIZE(size, 32);
  SSF_QMEM_CANONICALIZE(size, 64);
  SSF_QMEM_CANONICALIZE(size, 128);
  SSF_QMEM_CANONICALIZE(size, 256);
  SSF_QMEM_CANONICALIZE(size, 512);
  SSF_QMEM_CANONICALIZE(size, 1024);
  SSF_QMEM_CANONICALIZE(size, 2048);
  SSF_QMEM_CANONICALIZE(size, 4096);
  return -1;
}

// find the bucket index for the given size
static int ssf_quickmem_bucketize(size_t size)
{
  SSF_QMEM_BUCKETIZE(size, 16,   0);
  SSF_QMEM_BUCKETIZE(size, 32,   1);
  SSF_QMEM_BUCKETIZE(size, 64,   2);
  SSF_QMEM_BUCKETIZE(size, 128,  3);
  SSF_QMEM_BUCKETIZE(size, 256,  4);
  SSF_QMEM_BUCKETIZE(size, 512,  5);
  SSF_QMEM_BUCKETIZE(size, 1024, 6);
  SSF_QMEM_BUCKETIZE(size, 2048, 7);
  SSF_QMEM_BUCKETIZE(size, 4096, 8);
  return -1;
}

// allocate a new chunk of memory for the memory pool
static void ssf_quickmem_allocate_pool()
{
#if PRIME_SSF_DEBUG 
  prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_allocate_pool()\n", SSF_SELF);
#endif

  // allocate the memory chunk
  SSF_USE_PRIVATE(qmem_poolbegin) = new char[SSF_QMEM_POOLCHUNK];
  assert(SSF_USE_PRIVATE(qmem_poolbegin));
  SSF_USE_PRIVATE(qmem_poolend) = 
    SSF_USE_PRIVATE(qmem_poolbegin)+SSF_QMEM_POOLCHUNK;

  // link it with the existing chunks (using the first 16 bytes)
  *(char**)SSF_USE_PRIVATE(qmem_poolbegin) = 
    SSF_USE_PRIVATE(qmem_chunklist);
  SSF_USE_PRIVATE(qmem_chunklist) = SSF_USE_PRIVATE(qmem_poolbegin);
  SSF_USE_PRIVATE(qmem_poolbegin) += 16;

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_allocate_pool() "
	      "=> new memory pool: start=%p, end=%p\n",  SSF_SELF, 
	      SSF_USE_PRIVATE(qmem_poolbegin), SSF_USE_PRIVATE(qmem_poolend));
#endif
}

void ssf_quickmem_init()
{
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_init() init\n", SSF_SELF);
#endif

  // get the first chunk
  SSF_USE_PRIVATE(qmem_chunklist) = 0;
  ssf_quickmem_allocate_pool();

  // make a free list at the start of the chunk (next to the first 16 bytes)
  SSF_USE_PRIVATE(qmem_freelist) = 
    (void**)SSF_USE_PRIVATE(qmem_poolbegin);
  SSF_USE_PRIVATE(qmem_poolbegin) += 
    ssf_quickmem_canonicalize(sizeof(void*)*SSF_QMEM_NUMBUCKETS);
  for(int i=0; i<SSF_QMEM_NUMBUCKETS; i++)
    SSF_USE_PRIVATE(qmem_freelist)[i] = 0;
}

void ssf_quickmem_wrapup()
{
#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_wrapup()\n", SSF_SELF);
#endif

  // return the memory chunks one after another back to system
  while(SSF_USE_PRIVATE(qmem_chunklist)) {
    char* next = *(char**)SSF_USE_PRIVATE(qmem_chunklist);
    delete[] SSF_USE_PRIVATE(qmem_chunklist);
    SSF_USE_PRIVATE(qmem_chunklist) = next;
  }
}

void* ssf_quickmem_malloc(size_t size)
{
  size += SSF_QMEM_ALIGNMENT;

  if(size > SSF_QMEM_THRESHOLD) {
    char* mem = new char[size];

    // store the size in the first SSF_QMEM_ALIGNMENT bytes
    *(size_t*)mem = size;
    mem += SSF_QMEM_ALIGNMENT;

#if PRIME_SSF_DEBUG
    prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_malloc(%u) "
			  "=> %p (via prime::ssf::ssf_arena_malloc)\n", 
			  SSF_SELF, size-SSF_QMEM_ALIGNMENT, mem);
#endif
    return (void*)mem;
  }

  // find the free list of the right size
  size_t canon_size = ssf_quickmem_canonicalize(size);
  int bucket = ssf_quickmem_bucketize(canon_size);
  char* mem = (char*)SSF_USE_PRIVATE(qmem_freelist)[bucket];
  if(mem) {
    // if there's a memory block in the free list, use it
    SSF_USE_PRIVATE(qmem_freelist)[bucket] = *((void**)mem);
  } else {
    // if the memory chunk exhausted
    if(SSF_USE_PRIVATE(qmem_poolbegin)+canon_size > 
       SSF_USE_PRIVATE(qmem_poolend))
      ssf_quickmem_allocate_pool();

    // carve out a memory block
    mem = SSF_USE_PRIVATE(qmem_poolbegin);
    SSF_USE_PRIVATE(qmem_poolbegin) += canon_size;
  }

  // store the size in the first SSF_QMEM_ALIGNMENT bytes
  *(size_t*)mem = canon_size;
  mem += SSF_QMEM_ALIGNMENT;

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_malloc(%u) "
			"=> %p (from bucket %d)\n", 
			SSF_SELF, size-SSF_QMEM_ALIGNMENT, mem, bucket);
#endif
  return (char*)mem;
}
    
// the size in the argument is no longer used!
void ssf_quickmem_free(void *p, size_t size)
{
  if(!p) return;

  // retrieve the size from the preceding SSF_QMEM_ALIGNMENT bytes
  p = (char*)p-SSF_QMEM_ALIGNMENT;
  size = *(size_t*)p;

  if(size > SSF_QMEM_THRESHOLD) {
#if PRIME_SSF_DEBUG
    prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_free(%p,%u) "
		"(via ssf_arena_free)\n", SSF_SELF, p, size-SSF_QMEM_ALIGNMENT);
#endif
    delete[] (char*)p;
    return;
  }

  // return the memory block to the free list of the right size
  int bucket = ssf_quickmem_bucketize(size);
  assert(bucket >= 0);
  *((void**)p) = SSF_USE_PRIVATE(qmem_freelist)[bucket];
  SSF_USE_PRIVATE(qmem_freelist)[bucket] = p;

#if PRIME_SSF_DEBUG
  prime::ssf::ssf_debug(SSF_DEBUG_QMEM, "<QMEM> self=%d: prime::ssf::ssf_quickmem_free(%p,%u) "
	      "(returning to bucket=%d)\n", SSF_SELF, p, size-SSF_QMEM_ALIGNMENT, bucket);
#endif
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_QUICKMEM*/

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
