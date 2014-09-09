/*
 * machines :- general hardware support.
 *
 * The module contains public functions common to all hardware
 * environments. The header file also provides architecture-specific
 * support by including the header files of specific machine
 * configurations.
 *
 * Hardware support includes memory management, process management,
 * mutual exclusion, hardware timing support, and query information
 * regarding hardware configurations.
 */

#ifndef __PRIME_SSF_MACHINES_H__
#define __PRIME_SSF_MACHINES_H__

#ifndef PRIME_SSF_CONFIG_H
#define PRIME_SSF_CONFIG_H
#include "primex_config.h"
#endif

/*
 * Look for specific machine support. We include a header file for
 * each specific machine configuration.
 */

#if defined(PRIME_SSF_ARCH_X86_CYGWIN)
#include "mac/x86-cygwin.h"

#elif defined(PRIME_SSF_ARCH_X86_DARWIN)
#include "mac/x86-darwin.h"

#elif defined(PRIME_SSF_ARCH_X86_LINUX)
#include "mac/x86-linux.h"

#elif defined(PRIME_SSF_ARCH_X86_64_CYGWIN)
#include "mac/x86-64-cygwin.h"

#elif defined(PRIME_SSF_ARCH_X86_64_LINUX)
#include "mac/x86-64-linux.h"

#elif defined(PRIME_SSF_ARCH_POWERPC_DARWIN)
#include "mac/powerpc-darwin.h"

#elif defined(PRIME_SSF_ARCH_POWERPC_AIX)
#include "mac/powerpc-aix.h"

/*
#elif defined(PRIME_SSF_ARCH_WINDOWS)
#include "mac/x86-windows.h"

#elif defined(PRIME_SSF_ARCH_MIPS_IRIX)
#include "mac/mips-irix.h"

#elif defined(PRIME_SSF_ARCH_SPARC_SOLARIS)
#include "mac/sparc-solaris.h"

#elif defined(PRIME_SSF_ARCH_ALPHA_OSF)
#include "mac/alpha-osf.h"
*/

#elif defined(PRIME_SSF_ARCH_GENERIC)
#include "mac/generic.h"

#else
#error "ERROR: architecture not supported!"
#endif

// there's no need to use hoard allocator if the memory heap is
// already shared; we force it here.
#if SSF_SHARED_HEAP_SEGMENT && PRIME_SSF_HOARD
#undef PRIME_SSF_HOARD
#define PRIME_SSF_HOARD 0
#endif

namespace prime {
namespace ssf {

  /* Query hardware configuration. */

  /*
   * This function returns the number of bytes in a page. A page is a
   * unit for basic memory operations (such as mmap).
   */
  extern int ssf_get_page_size();

  /*
   * This function returns the number of physical processors (sharing
   * the same address space) on this machine. Note this will not
   * provide the total number of distributed-memory machines running
   * the SSF model.
   */
  extern int ssf_get_partition_size();

  /* Timing support. */

  /*
   * We define machine time as a double float point value since
   * different platforms have different clock resolutions.
   */
  typedef double ssf_machine_time;

  /*
   * Get the clock value. The clock value does not make any sense
   * until it is translated into seconds.
   */
  extern ssf_machine_time ssf_get_wall_time();

  /*
   * Translate the clock value into seconds.
   */
  extern double ssf_wall_time_to_sec(ssf_machine_time t);

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_MACHINES_H__*/

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
