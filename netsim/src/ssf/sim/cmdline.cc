/*
 * cmdline :- set up environment variables (global read-only
 *            variables) from command-line options.
 */

#include <fstream>

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#ifdef PRIME_SSF_ARCH_WINDOWS
#include <io.h>
#else
#include <unistd.h>
#endif

#ifdef PRIME_SSF_SYNC_MPI
#include <mpi.h>
#endif

#ifdef PRIME_SSF_SYNC_LIBSYNK
extern "C" {
#include "fm.h"
#include "tm.h"
}
#endif

#include "api/ssfexception.h"
#include "mac/machines.h"
#include "mac/segments.h"
#include "sim/debug.h"
#include "sim/ssfmodel.h"
#include "sim/cmdline.h"

#ifndef PRIME_VERSION
#define PRIME_VERSION "unknown"
#endif

SSF_DECLARE_INCLUDE("api/ssftime.h");
SSF_DECLARE_INCLUDE("sim/ssfmodel.h");
SSF_DECLARE_SHARED_RO(ssf_dml_model*, submodel);
SSF_DECLARE_SHARED_RO(int*, mach_nprocs);
SSF_DECLARE_SHARED_RO(char**, mach_names);
SSF_DECLARE_SHARED_RO(int, nmachs);
SSF_DECLARE_SHARED_RO(int, whoami);
SSF_DECLARE_SHARED_RO(int, diverse);
SSF_DECLARE_SHARED_RO(int, seed);
SSF_DECLARE_SHARED_RO(int, silent);
SSF_DECLARE_SHARED_RO(FILE*, outfile);
SSF_DECLARE_SHARED_RO(ltime_t, flowdelta);
SSF_DECLARE_SHARED_RO(long, flowmark);
#if PRIME_SSF_DISTSIM
SSF_DECLARE_SHARED_RO(int, globalasync);
#endif
SSF_DECLARE_SHARED_RO(ltime_t, synthresh);
/*#if PRIME_SSF_ENABLE_CHECKPOINTING
SSF_DECLARE_SHARED_RO(ltime_t, ckpt);
#endif*/
//SSF_DECLARE_SHARED_RO(int, spin);
SSF_DECLARE_SHARED_RO(ltime_t, progress);

SSF_DECLARE_SHARED_RO(int, argc);
SSF_DECLARE_SHARED_RO(char**, argv);

SSF_DECLARE_SHARED_RO(const char*, version);
SSF_DECLARE_SHARED_RO(char*, copyinfo);
SSF_DECLARE_SHARED_RO(char*, showcfg);

SSF_DECLARE_SHARED_RO(int, max_thresh_edges);

namespace prime {
namespace ssf {

static void sstrcat(char* str, const char* fmt, ...)
{
  char newstr[1024];
  va_list ap;
  va_start(ap, fmt);
  vsprintf(newstr, fmt, ap);
  va_end(ap);
  strcat(str, newstr);
}

// different than strdup, we use new[] instead of malloc.
static char* sstrdup(const char* str)
{
  assert(str);
  int len = strlen(str);
  char* newstr = new char[len+1];
  strcpy(newstr, str);
  return newstr;
}

ssf_command_line::ssf_cmdline_option ssf_command_line::opt_array[] = {
  /*{ ssf_command_line::OPTION_HELP, "-help",
    "-help              : print help information message" },*/
  { ssf_command_line::OPTION_SUBMODEL, "-submodel",
    "-submodel <fname>  : use ssf dml submodel" },
  { ssf_command_line::OPTION_S, "-s",
    "-s                 : same as -submodel" },
  { ssf_command_line::OPTION_NMACHS, "-nmachs",
    "-nmachs <m> [n_0,]p_0:[n_1,]p_1:..:[n_{m-1},]p_{m-1} : machine config (default: 1 localhost:1)" },
  { ssf_command_line::OPTION_M, "-m",
    "-m                 : same as -nmachs" },
  { ssf_command_line::OPTION_NPROCS, "-nprocs",
    "-nprocs <n>        : set number of processors (default: 1)" },
  { ssf_command_line::OPTION_N, "-n",
    "-n                 : same as -nprocs" },
  { ssf_command_line::OPTION_RANK, "-rank",
    "-rank <r>          : set the machine rank in the array (default: 0 or from MPI context)" },
  { ssf_command_line::OPTION_R, "-r",
    "-r                 : same as -rank" },
  { ssf_command_line::OPTION_SAME, "-same",
    "-same              : homogeneous machine cluster" },
  { ssf_command_line::OPTION_DIVERSE, "-diverse",
    "-diverse           : heterogeneous machine cluster (default)" },
#if !SSF_SHARED_HEAP_SEGMENT
  { ssf_command_line::OPTION_HEAP, "-heap",
    "-heap <mbytes>     : set size of the shared heap (default: 100 MB)" },
  { ssf_command_line::OPTION_BACKSTORE, "-backstore",
    "-backstore <dir>   : backstore for shared heap (default: /tmp)" },
#endif
  { ssf_command_line::OPTION_SEED, "-seed",
    "-seed <n>          : set initial random seed (default: 54321)" },
  { ssf_command_line::OPTION_SILENT, "-silent",
    "-silent            : suppress ssf messages (default: show messages)" },
  { ssf_command_line::OPTION_SHOWCFG, "-showcfg",
    "-showcfg           : show system configuration (default)" },
  { ssf_command_line::OPTION_NOSHOWCFG, "-noshowcfg",
    "-noshowcfg         : do not show system configuration" },
  { ssf_command_line::OPTION_OUTFILE, "-outfile",
    "-outfile <fname>   : output message to file (default: stdout)" },
  { ssf_command_line::OPTION_O, "-o",
    "-o                 : same as -outfile" },
  { ssf_command_line::OPTION_DEBUG, "-debug",
    "-debug <n>         : debug mask (default 0)" },
  { ssf_command_line::OPTION_D, "-d",
    "-d                 : same as -debug" },
  { ssf_command_line::OPTION_FLOWDELTA, "-flowdelta",
    "-flowdelta <ltime> : time increment per scheduling (default: infinite)" },
  { ssf_command_line::OPTION_FLOWMARK, "-flowmark",
    "-flowmark <mbytes> : max kernel events per scheduling (default: infinite)" },
#if PRIME_SSF_DISTSIM
  { ssf_command_line::OPTION_SYNC, "-sync",
    "-sync              : use barriers for global synchronization (default)" },
  { ssf_command_line::OPTION_ASYNC, "-async",
    "-async             : use CMB for global synchronization" },
#endif
  { ssf_command_line::OPTION_PROGRESS, "-progress",
    "-progress <intv>   : show progress of simulation (default: infinite)" },
  { ssf_command_line::OPTION_P, "-p",
    "-p                 : same as -progress" },
  { ssf_command_line::OPTION_SYNTHRESH, "-synthresh",
    "-synthresh <ltime> : local composite synchronization threshold (default: automatic)" },
  { ssf_command_line::OPTION_T, "-t",
    "-t                 : same as -synthresh" },
/*#if PRIME_SSF_ENABLE_CHECKPOINTING
  { ssf_command_line::OPTION_CKPT, "-ckpt",
    "-ckpt <ltime>      : set checkpointing interval (default: infinite)" },
  { ssf_command_line::OPTION_C, "-c",
    "-c                 : same as -ckpt" },
#endif*/
  { ssf_command_line::OPTION_SPIN, "-spin",
    "-spin              : spinning simulation processes instead of yielding" },
  { ssf_command_line::OPTION_YIELD, "-yield",
    "-yield             : yielding simulation processes (default)" },
  { ssf_command_line::OPTION_ENDOFOPT, "--",
    "--                 : end of parsing ssf command-line arguments" },
  { ssf_command_line::OPTION_NONE, 0, "" }
};

ssf_command_line::ssf_command_line()
{
  submodel  = 0;      // unset
  nmachs    = -1;     // unset
  mach_nprocs = 0;    // no processor deployment
  mach_names = 0;     // no machine name set
  rank      = -1;     // unset
  diverse   = 1;      // homogenous
#if !SSF_SHARED_HEAP_SEGMENT
  heap      = 1000;    // 1 GB -- increased from 100 MB by Jason on 10/14/2010
  backstore = sstrdup("/tmp"); // system temporary directory
#endif
  seed      = 54321;  // system random seed
  silent    = 0;      // show messages
  showcfg   = 0;      // system configuration printout on
  outfile   = 0;      // unset
  debug     = 0;      // no debug message
  flowdelta = SSF_LTIME_ZERO; // zero means infinity
  flowmark  = 0;      // zero means infinity
#if PRIME_SSF_DISTSIM
  globalasync = 0;    // barrier synchronization
#endif
  synthresh = -1;     // negative means unset
/*#if PRIME_SSF_ENABLE_CHECKPOINTING
  ckpt      = 0;      // infinite
#endif*/
  spin      = 0;      // use yield
  progress  = 0;      // infinite
}

ssf_command_line::~ssf_command_line()
{
  if(submodel) delete[] submodel;
  if(nmachs >= 0) {
    assert(mach_nprocs && mach_names);
    delete[] mach_nprocs;
    for(int i=0; i<nmachs; i++) delete[] mach_names[i];
    delete[] mach_names;
  }
#if !SSF_SHARED_HEAP_SEGMENT
  if(backstore) delete[] backstore;
#endif
  if(outfile) delete[] outfile;
}

void ssf_command_line::print_help(FILE* outfd)
{
  fprintf(outfd, "SSF command-line options:\n");
  for(ssf_cmdline_option* p = opt_array; p->str; p++)
    fprintf(outfd, "  %s\n", p->info);
}

void print_command_line(FILE* outfd)
{
  ssf_command_line::print_help(outfd);
}

#define OPTCHECK(cond, complaint)\
  if(!(cond)) { \
    char msg[256]; sprintf(msg, "%s: %s", p->str, complaint); \
    ssf_throw_exception(ssf_exception::cmdline_badarg, msg); \
    return 1; }

int ssf_command_line::parse(int& argc, char**& argv)
{
  int i;
  int offset = 1;

  for(i=1; i<argc; i++) {
    // match the command line option
    ssf_cmdline_option *p;
    for(p=opt_array; p->str; p++)
      if(strcmp(argv[i], p->str) == 0) break;

    switch(p->opt) {

    /*
    case OPTION_HELP: {
      print_help(argv[0]);
      return 1;
    }
    */

    case OPTION_SUBMODEL:
    case OPTION_S: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      if(submodel) delete[] submodel;
      submodel = sstrdup(argv[i]);
      break;
    }

    case OPTION_NMACHS:
    case OPTION_M: {
      OPTCHECK(nmachs==-1, "duplicate option: -nmachs or -nprocs");
      ++i;
      OPTCHECK(i<argc, "argument missing");
      nmachs = atoi(argv[i]);
      OPTCHECK(nmachs>0, "invalid number of distributed machines");
      mach_nprocs = new int[nmachs]; assert(mach_nprocs);
      mach_names = new char*[nmachs]; assert(mach_names);
      ++i;
      OPTCHECK(i<argc, "argument missing");
      char* str = argv[i];
      for(int k=0; k<nmachs; k++) {
	char* colon = strchr(str, ':');
	char* substr = str; 
	if(k==nmachs-1) {
	  OPTCHECK(colon==0, "ill format");
	} else { 
	  OPTCHECK(colon, "ill format");
	  *colon = 0; 
	  str = colon+1;
	}
	char* comma = strchr(substr, ',');
	if(comma) {
	  *comma = 0;
	  mach_names[k] = sstrdup(substr);
	  substr = comma+1;
	} else mach_names[k] = sstrdup("localhost");
	mach_nprocs[k] = atoi(substr);
	OPTCHECK(mach_nprocs[k]>0, "invalid number of processors");
      }
      break;
    }

    case OPTION_NPROCS:
    case OPTION_N: {
      OPTCHECK(nmachs==-1, "duplicate option: -nmachs or -nprocs");
      nmachs = 0; // to distinguish from -m
      mach_nprocs = new int[1]; assert(mach_nprocs);
      mach_names = new char*[1]; assert(mach_names);
      ++i;
      OPTCHECK(i<argc, "argument missing");
      mach_nprocs[0] = atoi(argv[i]);
      mach_names[0] = sstrdup("localhost");
      OPTCHECK(mach_nprocs[0]>0, "invalid number of processors");
      break;
    }

    case OPTION_RANK:
    case OPTION_R: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      rank = atoi(argv[i]);
      //OPTCHECK(nmachs>0, "rank must be set after -nmachs or -nprocs option");
      //OPTCHECK(0<=rank&&rank<nmachs, "rank out of range");
      OPTCHECK(0<=rank, "rank out of range");
      break;
    }

    case OPTION_SAME: {
      diverse = 0;
      break;
    }

    case OPTION_DIVERSE: {
      diverse = 1;
#if PRIME_SSF_HOMOGENEOUS
      ssf_throw_exception(ssf_exception::cmdline_diverse);
#endif
      break;
    }

#if !SSF_SHARED_HEAP_SEGMENT
    case OPTION_HEAP: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      heap = atoi(argv[i]);
      OPTCHECK(heap>64, "heap size must be no smaller than 64 (MB)");
      break;
    }

    case OPTION_BACKSTORE: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      if(backstore) delete[] backstore;
      backstore = sstrdup(argv[i]);
      break;
    }
#endif

    case OPTION_SEED: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      seed = atoi(argv[i]);
      break;
    }

    case OPTION_SILENT: {
      silent = 1;
      break;
    }

    case OPTION_SHOWCFG: {
      showcfg = 1;
      break;
    }

    case OPTION_NOSHOWCFG: {
      showcfg = 0;
      break;
    }

    case OPTION_OUTFILE:
    case OPTION_O: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      if(outfile) delete[] outfile;
      outfile = sstrdup(argv[i]);
      break;
    }

    case OPTION_DEBUG:
    case OPTION_D: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      OPTCHECK(argv[i][0]=='0' && argv[i][1]=='x', "need hex number (0x)");
      sscanf(argv[i], "0x%x", &debug);
      break;
    }

    case OPTION_FLOWDELTA: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
#if defined(PRIME_SSF_LTIME_LONG)
      flowdelta = (ltime_t)atol(argv[i]);
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#if HAVE_ATOLL
      flowdelta = (ltime_t)atoll(argv[i]);
#else
      flowdelta = (ltime_t)atol(argv[i]);
#endif
#elif defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
      flowdelta = (ltime_t)atof(argv[i]);
#else
#error "unknown ltime_t type"
#endif
      OPTCHECK(flowdelta>0, "flowdelta must be positive");
      break;
    }

    case OPTION_FLOWMARK: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
      flowmark = atol(argv[i]);
      OPTCHECK(flowmark>=1000, "flowmark must be no smaller than 1000");
      break;
    }

#if PRIME_SSF_DISTSIM
    case OPTION_SYNC: {
      globalasync = 0;
      break;
    }

    case OPTION_ASYNC: {
      globalasync = 1;
      break;
    }
#endif

    case OPTION_SYNTHRESH:
    case OPTION_T: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
#if defined(PRIME_SSF_LTIME_LONG)
      synthresh = (ltime_t)atol(argv[i]);
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#if HAVE_ATOLL
      synthresh = (ltime_t)atoll(argv[i]);
#else
      synthresh = (ltime_t)atol(argv[i]);
#endif
#elif defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
      synthresh = (ltime_t)atof(argv[i]);
#else
#error "unknown ltime_t type"
#endif
      OPTCHECK(synthresh>=0, "negative composite synchronization threshold");
      break;
    }

/*#if PRIME_SSF_ENABLE_CHECKPOINTING
    case OPTION_CKPT:
    case OPTION_C: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
#if defined(PRIME_SSF_LTIME_LONG)
      ckpt = (ltime_t)atol(argv[i]);
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#if HAVE_ATOLL
      ckpt = (ltime_t)atoll(argv[i]);
#else
      ckpt = (ltime_t)atol(argv[i]);
#endif
#elif defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
      ckpt = (ltime_t)atof(argv[i]);
#else
#error "unknown ltime_t type"
#endif
      OPTCHECK(ckpt>0, "invalid checkpoint interval");
      break;
    }
#endif*/

    case OPTION_SPIN: {
      spin = 1;
      break;
    }

    case OPTION_YIELD: {
      spin = 0;
      break;
    }

    case OPTION_PROGRESS:
    case OPTION_P: {
      ++i;
      OPTCHECK(i<argc, "argument missing");
#if defined(PRIME_SSF_LTIME_LONG)
      progress = (ltime_t)atol(argv[i]);
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#if HAVE_ATOLL
      progress = (ltime_t)atoll(argv[i]);
#else
      progress = (ltime_t)atol(argv[i]);
#endif
#elif defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
      progress = (ltime_t)atof(argv[i]);
#else
#error "unknown ltime_t type"
#endif
      OPTCHECK(progress>0, "invalid progress interval");
      break;
    }

    case OPTION_ENDOFOPT: {
      ++i;
      goto stop;
    }

    default: {
      argv[offset++] = argv[i];
      break;
    }}
  }
    
 stop:
  // arrange the rest (unparsed) arguments
  for(int j=i; j<argc; j++)
    argv[offset++] = argv[j];
  argv[offset] = 0;
  argc = offset;

  return 0;
}

#undef OPTCHECK



int ssf_init_environment(int argc, char** argv)
{
#ifdef PRIME_SSF_SYNC_MPI
  MPI_Init(&argc, &argv);
#endif

  ssf_command_line cmdline;
  if(cmdline.parse(argc, argv)) return 1;

  SSF_INIT_SHARED_RO(diverse, cmdline.diverse);
#if !SSF_SHARED_HEAP_SEGMENT
  SSF_INIT_SHARED_RO(heap, cmdline.heap);
  SSF_INIT_SHARED_RO(backstore, cmdline.backstore); cmdline.backstore = 0;
#endif
  SSF_INIT_SHARED_RO(seed, cmdline.seed);
  SSF_INIT_SHARED_RO(silent, cmdline.silent);
#if PRIME_SSF_DEBUG
  SSF_INIT_SHARED_RO(debug_mask, cmdline.debug);
#endif
  SSF_INIT_SHARED_RO(flowdelta, cmdline.flowdelta);
  SSF_INIT_SHARED_RO(flowmark, cmdline.flowmark);
#if PRIME_SSF_DISTSIM
  SSF_INIT_SHARED_RO(globalasync, cmdline.globalasync);
#endif
  SSF_INIT_SHARED_RO(synthresh, cmdline.synthresh);
/*#if PRIME_SSF_ENABLE_CHECKPOINTING
  SSF_INIT_SHARED_RO(ckpt, cmdline.ckpt);
#endif*/
  SSF_INIT_SHARED_RO(spinlock, cmdline.spin);
  SSF_INIT_SHARED_RO(progress, cmdline.progress);

  SSF_INIT_SHARED_RO(argc, argc);
  SSF_INIT_SHARED_RO(argv, argv);

  int nmachs = cmdline.nmachs;
  int rank = cmdline.rank;

#ifdef PRIME_SSF_SYNC_MPI
  int mpi_nmachs, mpi_rank;
  MPI_Comm_size(MPI_COMM_WORLD, &mpi_nmachs);
  MPI_Comm_rank(MPI_COMM_WORLD, &mpi_rank);

  if(nmachs>0) { // if set by -m, it must be consistent!
    if(nmachs != mpi_nmachs)
      ssf_throw_exception(ssf_exception::cmdline_nmach);
  } else if(nmachs==0) { // if set by -n, we adopt the mpi version, but keep #procs
    nmachs = mpi_nmachs;
    if(mpi_nmachs > 1) { // we need to reshuffle cmdline.mach_nprocs and cmdline.mach_names
      int* np = cmdline.mach_nprocs;
      char** names = cmdline.mach_names;
      cmdline.mach_nprocs = new int[nmachs]; assert(cmdline.mach_nprocs);
      cmdline.mach_names = new char*[nmachs]; assert(cmdline.mach_names);
      for(int k=0; k<nmachs; k++) {
	cmdline.mach_nprocs[k] = np[0];
	cmdline.mach_names[k] = sstrdup(names[0]);
      }
      delete[] np;
      delete[] names[0]; delete[] names;
    }
  } else {
    nmachs = mpi_nmachs;
    // we reproduce cmdline.mach_nprocs and cmdline.mach_names
    cmdline.mach_nprocs = new int[nmachs]; assert(cmdline.mach_nprocs);
    cmdline.mach_names = new char*[nmachs]; assert(cmdline.mach_names);
    for(int k=0; k<nmachs; k++) {
      cmdline.mach_nprocs[k] = 1;
      cmdline.mach_names[k] = sstrdup("localhost");
    }
  }

  if(rank >= 0) { // if set by command line, must be consistent!
    if(rank != mpi_rank)
      ssf_throw_exception(ssf_exception::cmdline_rank);
  } else rank = mpi_rank;
  if(cmdline.globalasync)
    ssf_throw_exception(ssf_exception::cmdline_async);
#endif
  // Note that here it is possible no mpi is enabled and nmachs
  // remains uncommitted (-1 or 0)

  if(!cmdline.submodel) {
    // if -submodel option is not set and we found '.submodel.dml' file
    // in the current directory, use it!
    struct stat st;
    if(stat(".submodel.dml", &st) == 0 && 
       (st.st_mode&S_IFREG) != 0)
      cmdline.submodel = sstrdup(".submodel.dml");
  }
  if(cmdline.submodel) {
    ssf_dml_model* model = new ssf_dml_model(cmdline.submodel);
    if(!model || !*model) 
      ssf_throw_exception(ssf_exception::cmdline_submodel);
    SSF_INIT_SHARED_RO(submodel, model);

    // we must use nmachs and #procs/mach defined in the submodel
    SSF_INIT_SHARED_RO(nmachs, SSF_USE_SHARED_RO(submodel)->partitions.size());
    if(nmachs > 0 && // if nmachs has been confirmed by mpi or command-line,
       nmachs != SSF_USE_SHARED_RO(nmachs)) { // then, we have to make sure...
      ssf_throw_exception(ssf_exception::cmdline_modnmach);
    }
    int* np = new int[SSF_USE_SHARED_RO(nmachs)]; assert(np);
    char** names = new char*[SSF_USE_SHARED_RO(nmachs)]; assert(names);
    for(int k=0; k<SSF_USE_SHARED_RO(nmachs); k++) {
      np[k] = (int)SSF_USE_SHARED_RO(submodel)->partitions[k];
      if(nmachs > 0) { // if nmachs is set by -m or mpi, it must be consistent
	if(np[k] != cmdline.mach_nprocs[k])
	  ssf_throw_exception(ssf_exception::cmdline_modnproc);
	names[k] = sstrdup(cmdline.mach_names[k]);
      } else if(nmachs == 0) { // if nmachs is set by -n, it must also be consistent
	if(np[k] != cmdline.mach_nprocs[0])
	  ssf_throw_exception(ssf_exception::cmdline_modnproc);
	names[k] = sstrdup(cmdline.mach_names[0]);
      } else { // if nmachs is unset
	names[k] = sstrdup("localhost");
      }
    }
    nmachs = SSF_USE_SHARED_RO(nmachs);

    // if the rank is not set by command-line, or set incorrectly, nor
    // is it set by the MPI context, it defaults to zero, i.e., the
    // first machine in the array.
    if(rank < 0 || rank >= nmachs) rank = 0;
    SSF_INIT_SHARED_RO(whoami, rank); // rank is finally settled by now

    SSF_INIT_SHARED_RO(mach_nprocs, np);
    SSF_INIT_SHARED_RO(mach_names, names);
  } else { // no dml submodel defined
    SSF_INIT_SHARED_RO(submodel, 0);

    // if nmachs is still uncommited (-1 or 0), set it to one
    if(nmachs <= 0) SSF_INIT_SHARED_RO(nmachs, 1);
    else SSF_INIT_SHARED_RO(nmachs, nmachs);
    int* np = new int[SSF_USE_SHARED_RO(nmachs)]; assert(np);
    char** names = new char*[SSF_USE_SHARED_RO(nmachs)]; assert(names);
    for(int k=0; k<SSF_USE_SHARED_RO(nmachs); k++) {
      if(nmachs > 0) { // if nmachs is set by -m, take it from there
	np[k] = cmdline.mach_nprocs[k];
	names[k] = sstrdup(cmdline.mach_names[k]);
      } else if(nmachs == 0) { // if nmachs is set by -n, take it from there also
	np[k] = cmdline.mach_nprocs[0];
	names[k] = sstrdup(cmdline.mach_names[0]);
      } else { // if nmachs is unset
	np[k] = 1; 
	names[k] = sstrdup("localhost");
      }
    }
    nmachs = SSF_USE_SHARED_RO(nmachs);

    // if the rank is not set by command-line, or set incorrectly, nor
    // is it set by the MPI context, it defaults to zero, i.e., the
    // first machine in the array.
    if(rank < 0 || rank >= nmachs) rank = 0;
    SSF_INIT_SHARED_RO(whoami, rank); // rank is finally settled by now

    SSF_INIT_SHARED_RO(mach_nprocs, np);
    SSF_INIT_SHARED_RO(mach_names, names);
  }

  int nprocs = SSF_USE_SHARED_RO(mach_nprocs)[rank];
  SSF_INIT_SHARED_RO(nprocs, nprocs);

#if !PRIME_SSF_DISTSIM
  if(SSF_USE_SHARED_RO(nmachs) > 1) {
    ssf_throw_exception(ssf_exception::cmdline_nodist);
  }
#endif

/*#if PRIME_SSF_ENABLE_CHECKPOINTING
  if(SSF_USE_SHARED_RO(ckpt) > 0 && SSF_NPROCS > 1) {
    error_quit("ERROR: checkpoint not supported for multiprocessors");
  }
#endif*/

#ifdef PRIME_SSF_ARCH_WINDOWS
  if(SSF_NPROCS > 1) {
    ssf_throw_exception(ssf_exception::cmdline_windist);
  }
#endif

  if(cmdline.outfile) {
    char fname[1024];
    if(strlen(cmdline.outfile) > 1000)
      cmdline.outfile[1000] = 0; // cut down length
    sprintf(fname, "%s-%d", cmdline.outfile, rank);
#ifdef PRIME_SSF_ARCH_WINDOWS
    FILE* outf = fopen(fname, "w");
    if(!outf) {
      char msg[256];
      sprintf(msg, "can't open output file %s", fname);
      ssf_throw_exception(ssf_exception::cmdline_output, msg);
    }
    _dup2(_fileno(outf), 1);
#else
    int outf = creat(fname, 0600);
    if(outf < 0) {
      char msg[256];
      sprintf(msg, "can't open output file %s", fname);
      ssf_throw_exception(ssf_exception::cmdline_output, msg);
    }
    close(STDOUT_FILENO);
    if(-1==dup(outf))
        ssf_throw_exception(ssf_exception::cmdline_output, "Error duplicating file descriptor");
    close(outf);
#endif
  }
  SSF_INIT_SHARED_RO(outfile, stdout);

  SSF_INIT_SHARED_RO(version, PRIME_VERSION);
  char* copyright = new char[1024];
  strcpy(copyright, "Copyright (c) 2007-2011 Florida International University.\n"
	 "PRIME SSF is open source.\n"
	 "See COPYRIGHT for detailed licence information.\n");
  SSF_INIT_SHARED_RO(copyinfo, copyright);

  if(cmdline.showcfg) {
    char* machcfg = new char[4096]; 
    machcfg[0] = 0;
    sstrcat(machcfg, "[%d] ****************** MACHINE CONFIGURATIONS ******************\n",
	    SSF_USE_SHARED_RO(whoami));
    sstrcat(machcfg, "[%d] COMPILATION OPTIONS: %s\n", 
	    SSF_USE_SHARED_RO(whoami), PRIME_SSF_CONFIG);
    sstrcat(machcfg, "[%d] RUNTIME OPTIONS:\n", 
	    SSF_USE_SHARED_RO(whoami));
#if PRIME_SSF_DISTSIM
    sstrcat(machcfg, "  [%d] Distributed-Memory Simulation: nmachs=%d\n", 
	    SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(nmachs));
    if(SSF_USE_SHARED_RO(diverse))
      sstrcat(machcfg, "  [%d] Heterogeneous Environment\n", 
	      SSF_USE_SHARED_RO(whoami));
    else sstrcat(machcfg, "  [%d] Homogeneous Environment\n", 
		 SSF_USE_SHARED_RO(whoami));
#else
    sstrcat(machcfg, "  [%d] Shared-Memory Simulation\n", 
	    SSF_USE_SHARED_RO(whoami));  
#endif
    sstrcat(machcfg, "  [%d] Number of Processors: %d\n", 
	    SSF_USE_SHARED_RO(whoami), SSF_NPROCS);
#if !SSF_SHARED_HEAP_SEGMENT
    sstrcat(machcfg, "  [%d] Heap Size: %d MB\n", 
	    SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(heap));
    sstrcat(machcfg, "  [%d] Backstore Directory: %s\n", 
	    SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(backstore));
#endif
    sstrcat(machcfg, "  [%d] Random Seed: %d\n", 
	    SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(seed));
#if PRIME_SSF_DEBUG
    sstrcat(machcfg, "  [%d] Debug Mask: 0x%08x\n", 
	    SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(debug_mask));
#endif
    sstrcat(machcfg, "  [%d] Flow Control: delta=%lg, mark=%ld\n",
	    SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(flowdelta), 
	    SSF_USE_SHARED_RO(flowmark));
    if(SSF_USE_SHARED_RO(synthresh) >= 0)
      sstrcat(machcfg, "  [%d] Composite Synchronization: threshold=%lg\n",
	      SSF_USE_SHARED_RO(whoami), (double)SSF_USE_SHARED_RO(synthresh));
    else sstrcat(machcfg, "  [%d] Composite Synchronization: automatic\n", 
		 SSF_USE_SHARED_RO(whoami));
    if(SSF_USE_SHARED_RO(spinlock))
      sstrcat(machcfg, "  [%d] Spinning: SET\n", SSF_USE_SHARED_RO(whoami));
    else sstrcat(machcfg, "  [%d] Spinning: UNSET\n", SSF_USE_SHARED_RO(whoami));
    sstrcat(machcfg, "************************************************************\n\n");
    SSF_INIT_SHARED_RO(showcfg, machcfg);
  } else SSF_INIT_SHARED_RO(showcfg, 0);

  // FIXME: temporarily put here; should be user configurable.
  SSF_INIT_SHARED_RO(max_thresh_edges, 20);

#ifdef PRIME_SSF_SYNC_LIBSYNK
  // set the environment variables required by libsynk
  char* fm_numnodes = new char[20];
  sprintf(fm_numnodes, "FM_NUMNODES=%d", SSF_USE_SHARED_RO(nmachs)); 
  putenv(fm_numnodes);
  char* fm_nodeid = new char[20];
  sprintf(fm_nodeid, "FM_NODEID=%d", SSF_USE_SHARED_RO(whoami));
  putenv(fm_nodeid);
  for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
    char* nodestr = new char[20+strlen(SSF_USE_SHARED_RO(mach_names[i]))];
    sprintf(nodestr, "FM_NODENAME_%d=%s", i, SSF_USE_SHARED_RO(mach_names[i]));
    putenv(nodestr);
  }
  if(cmdline.globalasync) { putenv("TM_DONULL=1"); unsetenv("TM_NONULL"); putenv("TM_NORED=1"); }
  else { unsetenv("TM_DONULL"); putenv("TM_NONULL=1"); unsetenv("TM_NORED"); }

#if PRIME_SSF_DEBUG
  if((SSF_USE_SHARED_RO(debug_mask)&SSF_DEBUG_LIBSYNK_ALL) != 0) {
    putenv("FM_DEBUG=100"); putenv("FMTCP_DEBUG=100"); 
    putenv("FMSHM_DEBUG=100"); putenv("FMGM_DEBUG=100");
    putenv("TM_DEBUG=100"); putenv("TMRED_DEBUG=100"); putenv("TMNULL_DEBUG=100");
    putenv("RM_DEBUG=100"); putenv("RMB_DEBUG=100");
  } else if((SSF_USE_SHARED_RO(debug_mask)&SSF_DEBUG_LIBSYNK) != 0) {
    putenv("FM_DEBUG=1"); putenv("FMTCP_DEBUG=1"); 
    putenv("FMSHM_DEBUG=1"); putenv("FMGM_DEBUG=1");
    putenv("TM_DEBUG=1"); putenv("TMRED_DEBUG=1"); putenv("TMNULL_DEBUG=1");
    putenv("RM_DEBUG=1"); putenv("RMB_DEBUG=1");
  } else
#endif
  {
    putenv("FM_DEBUG=0"); putenv("FMTCP_DEBUG=0"); 
    putenv("FMSHM_DEBUG=0"); putenv("FMGM_DEBUG=0");
    putenv("TM_DEBUG=0"); putenv("TMRED_DEBUG=0"); putenv("TMNULL_DEBUG=0");
    putenv("RM_DEBUG=0"); putenv("RMB_DEBUG=0");
  }
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

  return 0;
}

void ssf_wrapup_environment()
{
  fflush(0); 
  fclose(stdout); // in case it's redirection!

  if(SSF_USE_SHARED_RO(submodel))
    delete SSF_USE_SHARED_RO(submodel);

  delete[] SSF_USE_SHARED_RO(mach_nprocs);

  delete[] SSF_USE_SHARED_RO(copyinfo);
  if(SSF_USE_SHARED_RO(showcfg))
    delete[] SSF_USE_SHARED_RO(showcfg);

#ifdef PRIME_SSF_SYNC_MPI
  MPI_Finalize();
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

/*
 * $Id$
 */
