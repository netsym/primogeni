/*
 * debug :- print out debugging information.
 *
 * This module contains global public functions that can be used to
 * print out debugging messages while running the program. One can
 * filter out relevant debugging print-outs by providing a bit mask at
 * the command line. The debugging output is only enabled when the
 * macro PRIME_SSF_DEBUG is true.
 */

#ifndef __PRIME_SSF_DEBUG_H__
#define __PRIME_SSF_DEBUG_H__

#if PRIME_SSF_DEBUG

namespace prime {
namespace ssf {

  // All debug levels should be listed here.
  enum {
    // no debug
    SSF_DEBUG_NONE		= 0x00000000U,

    // machine-dependent routines
    SSF_DEBUG_MACHINE		= 0x00000001U,

    // shared arena
    SSF_DEBUG_ARENA		= 0x00000010U,

    // quick memory
    SSF_DEBUG_QMEM		= 0x00000020U,

    // all memory-related
    SSF_DEBUG_MEMORY		= 0x000000f0U,

    // global synchronization: libsynk library
    SSF_DEBUG_LIBSYNK		= 0x00010000U,

    // global synchronization: mpi
    SSF_DEBUG_MPI		= 0x00020000U,

    // all global synchronization
    SSF_DEBUG_GLOBALSYNC	= 0x00030000U,

    // global synchronization: libsynk library, detailed
    SSF_DEBUG_LIBSYNK_ALL	= 0x00100000U,

    // global synchronization: mpi, detailed
    SSF_DEBUG_MPI_ALL		= 0x00200000U,

    // all global synchronization, detailed
    SSF_DEBUG_GLOBALSYNC_ALL	= 0x00300000U,

    // universe
    SSF_DEBUG_UNIVERSE		= 0x00400000U,

    // all debug messages
    SSF_DEBUG_EVERYTHING	= 0xffffffffU
  };

  /*
   * The function is called by the runtime system where debug messages
   * are needed to be printed out. The debug level is given as the
   * first parameter. There can be at most 32 distinct debug
   * levels. The user can choose only the debug messages at a certain
   * debug level to be printed out (to stdout) eventually by
   * proividing a debug mask at the command line. (We known it through
   * a global shared read-only variable). Those debug messages not
   * chosen are filtered out.
   */
  extern void ssf_debug(uint32 mask, const char* msg, ...);

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_DEBUG*/

#endif /*__PRIME_SSF_DEBUG_H__*/

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

/*
 * $Id$
 */
