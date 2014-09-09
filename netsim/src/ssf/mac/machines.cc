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

#ifndef PRIME_SSF_ARCH_WINDOWS
#include <sys/time.h>
#include <unistd.h>
#endif

#include "mac/machines.h"

namespace prime {
namespace ssf {

int ssf_get_page_size() {
#ifdef PRIME_SSF_ARCH_WINDOWS
  return 8192;
#else
#if defined(_SC_PAGESIZE)
  return sysconf(_SC_PAGESIZE);
#elif defined(HAVE_GETPAGESIZE)
  return getpagesize();
#else
#warning "WARNING: missing information regarding hardware page size!"
  return 8192; // assuming 8KB page size, as large as it can be.
#endif
#endif
}

int ssf_get_partition_size() {
#ifdef PRIME_SSF_ARCH_WINDOWS
  return 1; // assuming single processor
#else
#if defined(_SC_NPROCESSORS_ONLN)
  return sysconf(_SC_NPROCESSORS_ONLN);
#elif defined(_SC_NPROC_ONLN)
  return sysconf(_SC_NPROC_ONLN);
#else
  //#warning "WARNING: missing information regarding hardware partition size!"
  return 1; // assuming single processor
#endif
#endif
}

#ifdef PRIME_SSF_ARCH_WINDOWS
#include <sys/timeb.h>
#include <sys/types.h>
#include <winsock.h>

static void gettimeofday(struct timeval* t,void* timezone) {
  struct _timeb timebuffer;
  _ftime( &timebuffer );
  t->tv_sec=timebuffer.time;
  t->tv_usec=1000*timebuffer.millitm;
}
#define HAVE_GETTIMEOFDAY 1
#endif

#if defined(HAVE_GETHRTIME)

// Wall time clock is available in nanosecond resolution. It's as good
// as it gets for general UNIX systems.

ssf_machine_time ssf_get_wall_time() { 
  return (ssf_machine_time)gethrtime();
}

double ssf_wall_time_to_sec(ssf_machine_time t) {
  return t/1e9; 
}

#elif defined(HAVE_GETTIMEOFDAY)

// Wall time clock is only in microseconds. It's the best precision we
// can get from the gettimeofday function.

ssf_machine_time ssf_get_wall_time() {
  struct timeval tv;
  gettimeofday(&tv, 0);
  return tv.tv_sec*1e6+tv.tv_usec;
}

double ssf_wall_time_to_sec(ssf_machine_time t) {
  return t/1e6; 
}

#else
#error "ERROR: missing system timing support!"
#endif

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
