/*
 * debug :- print out debugging information.
 *
 * This module contains global public functions that can be used to
 * print out debugging messages while running the program. One can
 * filter out relevant debugging output by providing a bit mask at the
 * command line. The debugging output is only enabled when the macro
 * PRIME_SSF_DEBUG is true.
 */

#if PRIME_SSF_DEBUG

#include <stdio.h>
#include <stdarg.h>

#include "mac/segments.h"
#include "sim/debug.h"

// The following global read-only variable is set when the command
// line arguments are parsed.
SSF_DECLARE_SHARED_RO(uint32, debug_mask);

namespace prime {
namespace ssf {

void ssf_debug(uint32 mask, const char* msg, ...)
{
  if((SSF_USE_SHARED_RO(debug_mask)&mask) != 0) {
    va_list ap;
    va_start(ap, msg);
    vprintf(msg, ap);
    fflush(stdout);
  }
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_DEBUG*/

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
