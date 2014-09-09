/*
 * ssf-main :- main function.
 *
 * The main function will parse the commandline arguments, initialize
 * the simulation, run the simulation, and eventually wrap things up.
 */

#include <stdio.h>

#ifndef PRIME_SSF_ARCH_WINDOWS
#include <unistd.h>
#endif

#include "ssf.h"
#include "sim/composite.h"
#include "sim/cmdline.h"

// keep the return value from the user main function
SSF_DECLARE_SHARED(int, ret);

// total number of processors available on this machine
SSF_DECLARE_SHARED_RO(int, nprocs);

// processor id, aliased for SSF_SELF
SSF_DECLARE_PRIVATE(int, self);

namespace prime {
namespace ssf {

static bool print_copyright = true;
bool do_not_print_copyright() {
	print_copyright=false;
	return print_copyright;
}

static void ssf_global_init() 
{
  // print out copyright/license information as well as system
  // configurations.
  if(!SSF_USE_SHARED_RO(silent)) {
    if(SSF_USE_SHARED_RO(whoami)==0) {
    	if(print_copyright) {
		  fprintf(SSF_USE_SHARED_RO(outfile),
			  "------------------------------------------------------------\n");
		  fprintf(SSF_USE_SHARED_RO(outfile),
			  "Parallel Real-time Immersive Simulation Environment (PRIME)\n"
			  "Scalable Simulation Framework (SSF) Version %s\n",
			  SSF_USE_SHARED_RO(version));
		  fputs(SSF_USE_SHARED_RO(copyinfo), SSF_USE_SHARED_RO(outfile));
		  fprintf(SSF_USE_SHARED_RO(outfile),
			  "------------------------------------------------------------\n\n");
		}
    }
    if(SSF_USE_SHARED_RO(nmachs) > 1) {
      char myhostname[256];
      if(!gethostname(myhostname, 255)) {
		fprintf(SSF_USE_SHARED_RO(outfile), "[%d/%d] => %s\n",
			SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(nmachs), myhostname);
      }
    }
    if(SSF_USE_SHARED_RO(whoami)==0) {
      if(SSF_USE_SHARED_RO(showcfg))
	fputs(SSF_USE_SHARED_RO(showcfg), SSF_USE_SHARED_RO(outfile));
    }
  }

  // settle shared and private memory for process 0
  ssf_map_shared_segments();
  ssf_arena_init(0);
  ssf_map_private_segments(0);
#if PRIME_SSF_QUICKMEM
  ssf_quickmem_init();
#endif

  ssf_global_init_barriers();
  //ssf_global_init_random();
  //ssf_global_init_kevents();
  ssf_global_init_universe();
}

static void ssf_global_wrapup() 
{
  ssf_global_wrapup_universe();
  //ssf_global_wrapup_kevents();
  //ssf_global_wrapup_random();
  ssf_global_wrapup_barriers();
  
#if PRIME_SSF_QUICKMEM
  ssf_quickmem_wrapup();
#endif
  
  // Reset the flag marking that the shared arena is no longer
  // accessible; further memory deallocation must go through the
  // system heap management routines.
  ssf_arena_ready = 0;
}

static void ssf_local_init()
{
  ssf_local_init_barriers();
  //ssf_local_init_random();
  //ssf_local_init_kevents();
  ssf_local_init_universe();
}

static void ssf_local_wrapup() 
{
 ssf_local_wrapup_universe();
 //ssf_local_wrapup_kevents();
 //ssf_local_wrapup_random();
 ssf_local_wrapup_barriers();
}

static void ssf_child_main(int myid)
{
  if(myid) {
    // settle with memory first (except process 0, whose private
    // memory has been settled already in ssf_global_init).
    ssf_arena_init(myid);
    ssf_map_private_segments(myid);
#if PRIME_SSF_QUICKMEM
    ssf_quickmem_init();
#endif
  }
  
  ssf_local_init();
  //printf("process %d running...\n", SSF_SELF); fflush(0);
  ssf_barrier();

  ssf_run_universe();
  ssf_barrier();

  //printf("process %d finish running...\n", SSF_SELF); fflush(0);
  ssf_local_wrapup();

#if PRIME_SSF_QUICKMEM
  if(myid) ssf_quickmem_wrapup();
#endif
}

}; // namespace ssf
}; // namespace prime

// The main function in user space is not the real main function of
// this program; it is defined as a macro. We should uncloak it.
#ifdef main
#undef main
#endif

int main(int argc, char* argv[])
{
  try {

  // set up the global run-time environment: initialize the shared
  // read-only variables from environment variables.
  if(prime::ssf::ssf_init_environment(argc, argv)) return 1;

  // global initialization
  prime::ssf::ssf_global_init();

  // create child processes; the function returns only when all of
  // them have finished.
  prime::ssf::ssf_fork_children(SSF_NPROCS, prime::ssf::ssf_child_main);

  // global wrapup.
  prime::ssf::ssf_global_wrapup();

  // wrap up the global run-time environment.
  prime::ssf::ssf_wrapup_environment();

  // user return value is returned from the true main function.
  fflush(0);
#ifdef PRIME_SSF_ARCH_WINDOWS
  return SSF_USE_SHARED(ret);
#else
  // FIXME: we have a problem if we make the main function to return
  // normally, especially in cases where we have multiple
  // processes/threads spawned; using the _exit function somehow fixed
  // the problem.

  // JASON (8/30/07): I can't remember why can't we use exit() rather
  // than the evil _exit() which will not close the open files
  // properly. I'll simply put exit here; if I find problems later,
  // I'll change it back!
  exit(SSF_USE_SHARED(ret));
  
  //_exit(SSF_USE_SHARED(ret));
#endif
  }
  catch(prime::ssf::ssf_exception& e) { fprintf(stderr, "%s\n", e.what()); }
  catch(prime::dml::dml_exception& e) { fprintf(stderr, "%s\n", e.what()); }
  catch(prime::rng::rng_exception& e) { fprintf(stderr, "%s\n", e.what()); }
  fflush(0);

  return 1;
}

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
