/*
 * segments :- data segment management.
 *
 * SSF may define a number of data segments: one for global variables
 * shared by all processors, one for global variables accessible only
 * at each processor, and one for the shared heap used for dynamic
 * memory allocations.
 */

#ifndef __PRIME_SSF_SEGMENTS_H__
#define __PRIME_SSF_SEGMENTS_H__

#include "mac/machines.h"

namespace prime {
namespace ssf {

  /*
   * This function creates shared data segments, including the one
   * used for global shared variables and the one for the shared heap
   * (which we call arena).
   */
  extern void ssf_map_shared_segments();

  /*
   * This function creates private data segment for global variables,
   * which are only accessible by each process. The process id must be
   * given as a parameter.
   */
  extern void ssf_map_private_segments(int pid);

}; // namespace ssf
}; // namespace prime

#ifndef GENSGMT

// The macros listed in the following (SSF_DECLARE_*, SSF_USE_*,
// etc.) should not be defined if we're trying to filter the source
// code to come up with a list of global variables. This step is done
// with the help of the preprocessor. By not defining them, the
// preprocessor will not instantiate these macros and we can then
// filter out needed text (using grep).

// This file is automatically generated by script; it contains all
// global variables defined by SSF kernel (including shared
// variables, shared read-only variables, and private variables).
#ifdef PRIME_SSF_ARCH_WINDOWS
#include "mac/globals-windows.h"
#else
#include "mac/globals.h"
#endif

#ifdef PRIME_SSF_ARCH_WINDOWS
#define __STDC__ 1
#endif

// include necessary header files
#define SSF_DECLARE_INCLUDE(x)

// global shared read-only variables
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_DECLARE_SHARED_RO(type,id) namespace prime { namespace ssf { type ssf_shared_ro_##id; }}
#define SSF_INIT_SHARED_RO(id,val) prime::ssf::ssf_shared_ro_##id = val
#define SSF_USE_SHARED_RO(id) prime::ssf::ssf_shared_ro_##id
#else
#define SSF_DECLARE_SHARED_RO(type,id) namespace prime { namespace ssf { type ssf_shared_ro_/**/id; }}
#define SSF_INIT_SHARED_RO(id,val) prime::ssf::ssf_shared_ro_/**/id = val
#define SSF_USE_SHARED_RO(id) prime::ssf::ssf_shared_ro_/**/id
#endif

// global shared variables (full access)
#if SSF_SHARED_DATA_SEGMENT
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_DECLARE_SHARED(type,id) namespace prime { namespace ssf { type ssf_shared_##id; }}
#define SSF_USE_SHARED(id) prime::ssf::ssf_shared_##id
#else
#define SSF_DECLARE_SHARED(type,id) namespace prime { namespace ssf { type prime::ssf::ssf_shared_/**/id; }}
#define SSF_USE_SHARED(id) prime::ssf::ssf_shared_/**/id
#endif
#else /*not SSF_SHARED_DATA_SEGMENT*/
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_DECLARE_SHARED(type,id) namespace prime { namespace ssf { type ssf_shared_##id; }}
#define SSF_USE_SHARED(id) (SSF_SDATA_ACTIVE?SSF_SHARED_DATA->id:prime::ssf::ssf_shared_##id)
#else
#define SSF_DECLARE_SHARED(type,id) namespace prime { namespace ssf { type prime::ssf::ssf_shared_/**/id; }}
#define SSF_USE_SHARED(id) (SSF_SDATA_ACTIVE?SSF_SHARED_DATA->id:prime::ssf::ssf_shared_/**/id)
#endif
#endif /*not SSF_SHARED_DATA_SEGMENT*/

// global private variables
#if SSF_SHARED_DATA_SEGMENT
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_DECLARE_PRIVATE(type,id) namespace prime { namespace ssf { type ssf_private_##id; }}
#define SSF_USE_PRIVATE(id) (SSF_SDATA_ACTIVE?(SSF_GET_PRIVATE_DATA->id):(prime::ssf::ssf_private_##id))
#define SSF_USE_RTX_PRIVATE(id) (SSF_SDATA_ACTIVE?(__rtx__?(((struct prime::ssf::private_segment*)__rtx__)->id):(SSF_GET_PRIVATE_DATA->id)):(prime::ssf::ssf_private_##id))
#else
#define SSF_DECLARE_PRIVATE(type,id) namespace prime { namespace ssf { type ssf_private_/**/id; }}
#define SSF_USE_PRIVATE(id) (SSF_SDATA_ACTIVE?(SSF_GET_PRIVATE_DATA->id):(prime::ssf::ssf_private_/**/id))
#define SSF_USE_RTX_PRIVATE(id) (SSF_SDATA_ACTIVE?(__rtx__?(((struct prime::ssf::private_segment*)__rtx__)->id):(SSF_GET_PRIVATE_DATA->id)):(prime::ssf::ssf_private_/**/id))
#endif
//#define SSF_USE_RTX_PRIVATE(id) SSF_USE_PRIVATE(id)
#else /*not SSF_SHARED_DATA_SEGMENT*/
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_DECLARE_PRIVATE(type,id) namespace prime { namespace ssf { type ssf_private_##id; }}
#define SSF_USE_PRIVATE(id) prime::ssf::ssf_private_##id
#define SSF_USE_RTX_PRIVATE(id) prime::ssf::ssf_private_##id
#else
#define SSF_DECLARE_PRIVATE(type,id) namespace prime { namespace ssf { type ssf_private_/**/id; }}
#define SSF_USE_PRIVATE(id) prime::ssf::ssf_private_/**/id
#define SSF_USE_RTX_PRIVATE(id) prime::ssf::ssf_private_/**/id
#endif
#endif /*not SSF_SHARED_DATA_SEGMENT*/

// aliases for frequently used variables
#define SSF_SELF	SSF_USE_PRIVATE(self)
#define SSF_NPROCS	SSF_USE_SHARED_RO(nprocs)

#if !SSF_SHARED_DATA_SEGMENT
#define SSF_SHARED_DATA \
 ((struct prime::ssf::shared_segment*) \
 SSF_USE_SHARED_RO(shared_segment))
#endif

#endif /*GENSGMT*/

// The following macros are used to indicate whether in a sequential
// situation we should use the shared data and heap segments, instead
// of using the default data segment and the default malloc/free from
// the standard C library. In general cases, the arena is only useful
// in a multiprocessor environment, that is, when we use multiple
// processes and expect the heap to be shared by all these
// processes. Sequential execution does not need the arena. However,
// if one wants to debug the arena management (by defining the macro
// PRIME_SSF_SHARED_MANAGEMENT), the data segment is mapped
// shared and the arena is used regardless of the number of processors
// used.

#if PRIME_SSF_SHARED_MANAGEMENT || PRIME_SSF_EMULATION
#define SSF_ARENA_ACTIVE 1
#define SSF_SDATA_ACTIVE 1
#else
#define SSF_ARENA_ACTIVE (SSF_NPROCS>1)
#define SSF_SDATA_ACTIVE (SSF_NPROCS>1)
#endif

#endif /*__PRIME_SSF_SEGMENTS_H__*/

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
