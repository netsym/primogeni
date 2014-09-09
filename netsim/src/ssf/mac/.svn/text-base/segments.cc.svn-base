/*
 * segments :- data segment management.
 *
 * SSF may define a number of data segments: one for global variables
 * shared by all processors, one for global variables only accessible
 * privately by each processor, and one for the shared heap used for
 * dynamic memory allocations.
 */

#include <assert.h>
#include <stdlib.h>
#include <string.h>

#include "mac/machines.h"
#include "mac/segments.h"

/* Important definitions used here:
 *
 *   SSF_SHARED_DATA_SEGMENT: whether the data segment is made shared
 *      among processors; if not, we need to make sure those declared
 *      using SSF_DECLARE_SHARED are indeed shared among processors.
 *
 *   SSF_SHARED_HEAP_SEGMENT: whether the heap segment is made shared
 *      among processors; if not, we need to enable shared arena
 *      management, so that processors can communicate (e.g., sending
 *      SSF events) via shared memory.
 */

#if !SSF_SHARED_DATA_SEGMENT
SSF_DECLARE_SHARED_RO(struct shared_segment*, shared_segment);
#endif

// the total size of the shared heap (in MB)
SSF_DECLARE_SHARED_RO(int, heap);

namespace prime {
namespace ssf {

void ssf_map_shared_segments()
{
  // make data segment shared (if it's not) so that global shared
  // variables are shared among the processes; for threads, this is
  // no-op; but for unix processes, we may need to use ldscript and
  // mmap (so that data segment maps to the same physical memory);
  // it's possible on some platforms this is not possible, in which
  // case the global variables are simply not shared
  ssf_map_shared_data();

#if !SSF_SHARED_HEAP_SEGMENT || !SSF_SHARED_DATA_SEGMENT

  /* if the heap segment or the data shared segment is not shared, we
     are obliged to create a shared segment (using mmap). We let the
     shared data occupy the first part of the created segment. */

#if !SSF_SHARED_DATA_SEGMENT
  size_t pagesize = ssf_get_page_size();
  size_t sdata_size;
  if(SSF_SDATA_ACTIVE) {
    sdata_size = sizeof(struct shared_segment);
    sdata_size = (sdata_size+pagesize-1)/pagesize*pagesize;
  } else sdata_size = 0;
#else
  size_t sdata_size = 0;
#endif

#if !SSF_SHARED_HEAP_SEGMENT
  size_t arena_size;
  if(SSF_ARENA_ACTIVE) arena_size = SSF_USE_SHARED_RO(heap)<<20;
  else arena_size = 0;
#else
  size_t arena_size = 0;
#endif

  char* ptr = (char*)ssf_create_shared_segment(sdata_size+arena_size);
  if(!ptr && sdata_size+arena_size>0) {
    // error condition!!!
    fprintf(stderr, "ERROR: cannot allocate shared segment of 0x%x bytes!\n", 
	    (unsigned)(sdata_size+arena_size));
    exit(1);
  } 

#if !SSF_SHARED_DATA_SEGMENT
  if(SSF_SDATA_ACTIVE) {
    memset(ptr, 0, sizeof(struct shared_segment)); // zero out shared variables
    SSF_INIT_SHARED_RO(shared_segment, (struct shared_segment*)ptr);
    ptr += sdata_size;
  } else {
    SSF_INIT_SHARED_RO(shared_segment, 0); // for sanity check
  }
#endif

#if !SSF_SHARED_HEAP_SEGMENT
  if(SSF_ARENA_ACTIVE) {
    // mark the boundary of the shared arena
    SSF_INIT_SHARED_RO(arena, ptr);
    SSF_INIT_SHARED_RO(arena_end, ptr+arena_size);
  } else {
    SSF_INIT_SHARED_RO(arena, 0); // for sanity check
    SSF_INIT_SHARED_RO(arena_end, 0);
  }
#endif

#else /*SSF_SHARED_HEAP_SEGMENT && SSF_SHARED_DATA_SEGMENT*/

  // for sanity check
  //SSF_INIT_SHARED_RO(arena, 0); 
  //SSF_INIT_SHARED_RO(arena_end, 0);

#endif /*SSF_SHARED_HEAP_SEGMENT && SSF_SHARED_DATA_SEGMENT*/
}

void ssf_map_private_segments(int pid)
{
#if SSF_SHARED_DATA_SEGMENT
  size_t pagesize = ssf_get_page_size();
  size_t pvar_size = (sizeof(struct private_segment)+pagesize-1)/pagesize*pagesize;
  ssf_map_private_data(pid, pvar_size);
#endif

  //#if !SSF_SHARED_HEAP_SEGMENT && PRIME_SSF_EMULATION
  //if(SSF_ARENA_ACTIVE)
  //  prime::ssf::ssf_mutex_init(&SSF_USE_PRIVATE(arena_inner_lock));
  //#endif

  SSF_SELF = pid;
#if !SSF_SHARED_HEAP_SEGMENT
  if(SSF_ARENA_ACTIVE) 
    SSF_USE_PRIVATE(mem_waterlevel) = 0;
#endif
}

}; // namespace ssf
}; // namespace prime

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
