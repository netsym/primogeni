/*
 * memcpp :- memory management for C++ code.
 *
 * The global new and delete operators are replaced with SSF
 * subroutines that allocated and deallcated memory from the shared
 * arena. Similary, the memory used by the Standard Template Library
 * (STL) should also come from the shared arena to enable shared
 * access by multiple processors.
 */

#ifndef __PRIME_SSF_MEMCPP_H__
#define __PRIME_SSF_MEMCPP_H__

#include <algorithm>
#include <cstddef>
#include <cstdlib>
#include <climits>

#include "mac/machines.h"

namespace prime {
namespace ssf {

  /*
   * Global new operator and delete operator are changed to allocate
   * and deallocate memory from the shared arena. This can only happen
   * after the shared arena has been set up correctly. Therefore, the
   * time before the arena is created and after the arena is
   * destroyed, only the default system heap memory (which may or may
   * not be shared) can be used. This flag is set by the system to
   * indicate whether the arena is ready to be used.
   */
  extern int ssf_arena_ready;

  /*
   * ssf_permanent_object is the base class for permanent objects used in
   * the simulation, such as Entity. A permanent object stays alive
   * throughout the entire simulation. No deletion operation is
   * permitted.
   */
  class ssf_permanent_object {
  public:
#if !SSF_SHARED_HEAP_SEGMENT
    static void* operator new(size_t sz);
    static void operator delete(void* p);
#endif
  }; /*ssf_permanent_object*/


}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_MEMCPP_H__*/

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
