/*
 * cmdline :- set up environment variables (global read-only
 *            variables) from command-line options.
 */

#ifndef __PRIME_SSF_CMDLINE_H__
#define __PRIME_SSF_CMDLINE_H__

#include "ssf.h"

namespace prime {
namespace ssf {

  /*
   * ssf_command_line is for parsing command-line argments. The data
   * structure is used internally.
   */
  class ssf_command_line {
  public:
    // Enumeration of recognizable command-line options.
    enum Option {
      OPTION_NONE,	// avoid zero for better error resistance
      //OPTION_HELP,	// print help information: -help
      OPTION_ENDOFOPT,	// separator ending ssf options: --
      OPTION_SUBMODEL,	// dml sub-model: -submodel <filename>
      OPTION_S,		// same as above: -s
      OPTION_NMACHS,	// machine configuration: -nmachs <m> <n_{0}:p_{0},..,n_{m-1}:p_{m-1}>
      OPTION_M,		// same as above: -m
      OPTION_NPROCS,	// number of processors: -nprocs <n>
      OPTION_N,		// same as above: -n
      OPTION_RANK,	// machine rank in the array: -rank <r>
      OPTION_R,		// same as above: -r
      OPTION_SAME,	// assume homogeneous machine configuration: -same
      OPTION_DIVERSE,	// assume heterogeneous machine configuration: -diverse
#if !SSF_SHARED_HEAP_SEGMENT
      OPTION_HEAP,	// size of shared memory heap: -heap <mbytes>
      OPTION_BACKSTORE,	// backstore for shared heap: -backstore <dirname>
#endif
      OPTION_SEED,	// initial random seed: -seed <n>
      OPTION_SILENT,	// suppress messages: -silent
      OPTION_SHOWCFG,	// show system configuration: -showcfg
      OPTION_NOSHOWCFG,	// do not show system configuration: -noshowcfg
      OPTION_OUTFILE,	// redirect messages: -outfile <filename>
      OPTION_O,		// same as above: -o
      OPTION_DEBUG,	// debug mask: -debug <n>
      OPTION_D,		// same as above: -d
      OPTION_FLOWDELTA,	// flow control, time increment per scheduling: -flowdelta <ltime>
      OPTION_FLOWMARK,	// flow control, max events per scheduling: -flowmark <n>
#if PRIME_SSF_DISTSIM
      OPTION_SYNC,      // use barriers for global synchronization: -sync
      OPTION_ASYNC,     // use CMB for global synchronization: -async
#endif
      OPTION_SYNTHRESH,	// local composite synchronization threshold: -synthresh <ltime>
      OPTION_T,		// same as above: -t
      /*#if PRIME_SSF_ENABLE_CHECKPOINTING
      OPTION_CKPT,	// checkpointing interval: -ckpt <ltime>
      OPTION_C,		// same as above: -c
      #endif*/
      OPTION_SPIN,	// process spinning, no yield: -spin
      OPTION_YIELD,	// process yield: -yield
      OPTION_PROGRESS,  // show progress: -progress <interval>
      OPTION_P,		// same as above: -p
      OPTION_TOTAL	// total number of argument types
    };

    // Structure for storing command-line options.
    struct ssf_cmdline_option {
      Option opt;
      const char *str;
      const char *info;
    };
    static ssf_cmdline_option opt_array[];

    // Print out ssf usage information.
    static void print_help(FILE* outfd);

    char* submodel;
    int nmachs;
    int* mach_nprocs;
    char** mach_names;
    int rank;
    int diverse;
#if !SSF_SHARED_HEAP_SEGMENT
    int heap;
    char* backstore;
#endif
    int seed;
    int silent;
    int showcfg;
    char* outfile;
    unsigned debug;
    ltime_t flowdelta;
    long flowmark;
#if PRIME_SSF_DISTSIM
    int globalasync;
#endif
    ltime_t synthresh;
    /*#if PRIME_SSF_ENABLE_CHECKPOINTING
    ltime_t ckpt;
    #endif*/
    int spin;
    ltime_t progress;

  public:
    ssf_command_line();
    ~ssf_command_line();

    int parse(int& argc, char**& argv);
  }; /*ssf_command_line*/

  extern int  ssf_init_environment(int argc, char** argv);
  extern void ssf_wrapup_environment();

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_CMDLINE_H__*/

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
