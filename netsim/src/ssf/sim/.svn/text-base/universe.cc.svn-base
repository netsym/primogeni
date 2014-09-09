/*
 * universe :- parallel universe, one for each processor.
 */

#include <new>
#include <fstream>

#include <errno.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <math.h>
#include <string.h>

#ifndef PRIME_SSF_ARCH_WINDOWS
#include <unistd.h>
#endif
#include <iostream>

#if PRIME_SSF_EMULATION
#include <time.h>
#include <signal.h>
#endif

#if PRIME_SSF_REPORT_USER_TIMING
#include <sys/times.h>
#include <time.h>
#endif

#include "ssf.h"
#include "sim/composite.h"

#ifdef PRIME_SSF_ARCH_WINDOWS
#define snprintf _snprintf
#endif

// since we are using pthreads, timing is for all threads
#define PRIME_SSF_SPLIT_CLOCK 1

namespace prime {
namespace ssf {

#if PRIME_SSF_EMULATION
static void alrm_handler(int signo) {}
#endif

int ssf_default_main(int argc, char** argv)
{
  if(SSF_USE_SHARED(startime) == SSF_LTIME_MINUS_INFINITY &&
     SSF_USE_SHARED(endtime) == SSF_LTIME_INFINITY) {
    // The simulation time interval has not been set, which means no
    // model has been built. Nothing to be done.
#if PRIME_SSF_EMULATION
    Entity::startAll(0, 0, 1.0);
#else
    Entity::startAll(0, 0);
#endif
  } else {
#if PRIME_SSF_EMULATION
    Entity::startAll(SSF_USE_SHARED(startime), SSF_USE_SHARED(endtime), 1.0);
#else
    Entity::startAll(SSF_USE_SHARED(startime), SSF_USE_SHARED(endtime));
#endif
  }
  Entity::joinAll();
  return 0;
}

/* This following global variable should be in the data segment
   containing initialized global variables and it should proceed the
   re-definition of the user main function. That is, the function call
   to ssf_register_main should go after the variable initialization.
   If not, we need to make it so. */
static int (*ssf_main_shared_ro)(int, char**) = ssf_default_main;

int ssf_register_main(int (*ssfmain)(int, char**))
{
  ssf_main_shared_ro = ssfmain;
  return 0;
}

}; // namespace ssf
}; // namespace prime

//#include "globalbarrier.h"
//#include "appointment.h"

// SSF_CONTEXT
SSF_DECLARE_PRIVATE(int, simphase);
SSF_DECLARE_PRIVATE(ltime_t, decade);
SSF_DECLARE_PRIVATE(ltime_t, now);
SSF_DECLARE_PRIVATE(void*, processing_timeline);
SSF_DECLARE_PRIVATE(void*, running_process);
SSF_DECLARE_SHARED(ltime_t, startime);
SSF_DECLARE_SHARED(ltime_t, endtime);
SSF_DECLARE_PRIVATE(ltime_t, flowdelta);
SSF_DECLARE_SHARED(ssf_mutex, environ_mutex);
SSF_DECLARE_SHARED(void*, environ);
SSF_DECLARE_PRIVATE(long, kevt_watermark);
#ifdef PRIME_SSF_ARCH_WINDOWS
SSF_DECLARE_INCLUDE(<fstream>);
SSF_DECLARE_PRIVATE(std::ofstream, datafile);
#else
SSF_DECLARE_PRIVATE(int, datafile);
#endif

// SSF_STATS
SSF_DECLARE_PRIVATE(prime::ssf::uint32, timeline_contexts);
SSF_DECLARE_PRIVATE(prime::ssf::uint32, process_contexts);
SSF_DECLARE_PRIVATE(prime::ssf::uint32, kernel_events);
SSF_DECLARE_PRIVATE(prime::ssf::uint32, kernel_messages);
SSF_DECLARE_PRIVATE(prime::ssf::uint32, kernel_messages_xprc);
SSF_DECLARE_PRIVATE(prime::ssf::uint32, kernel_messages_xmem);
SSF_DECLARE_PRIVATE(prime::ssf::uint32, direct_callbacks);

// parallel universes
SSF_DECLARE_SHARED(void**, paruniv);
SSF_DECLARE_PRIVATE(void*, universe);

// random number generator per processor used by the kernel only
// (e.g., auto-mapping)
SSF_DECLARE_INCLUDE("rng.h");
SSF_DECLARE_PRIVATE(prime::rng::Random*, krng);

// statistics report
SSF_DECLARE_PRIVATE(double, execution_time); // exec time per processor
SSF_DECLARE_SHARED(ssf_mutex, report_mutex); // exclusive report

// for composite synchronization use
//SSF_DECLARE_SHARED(int, sync_settled);
SSF_DECLARE_SHARED(void*, teleport);
SSF_DECLARE_SHARED(ltime_t, epoch_length);
SSF_DECLARE_PRIVATE(ltime_t, epoch);

SSF_DECLARE_SHARED(void**, unique_edges);
SSF_DECLARE_SHARED(ltime_t*, sorted_edges);
SSF_DECLARE_SHARED(int, num_sorted_edges);
SSF_DECLARE_SHARED(int, best_edge_index);
SSF_DECLARE_SHARED(ltime_t, training_length);

// used only by initialization
SSF_DECLARE_SHARED(void*, root_lp);
SSF_DECLARE_SHARED(void**, entity_list);

//SSF_DECLARE_PRIVATE(int, build_dml_entity);
SSF_DECLARE_SHARED(void*, entlist);
SSF_DECLARE_SHARED(ssf_mutex, entlist_mutex);
SSF_DECLARE_SHARED(ssf_mutex, logiproc_mutex);
SSF_DECLARE_SHARED(int, unique_entity_id);
SSF_DECLARE_SHARED(int, unique_logiproc_id);

// events in exchange
SSF_DECLARE_INCLUDE("sim/kernelevt.h");
SSF_DECLARE_SHARED(ssf_kernel_event***, events_on_fire);

// alignment queue
SSF_DECLARE_SHARED(ssf_mutex, align_mutex);
SSF_DECLARE_SHARED(void*, align_req_head);
SSF_DECLARE_SHARED(void*, align_req_tail);

// mapping from name to published inchannel (for mapping)
SSF_DECLARE_SHARED(ssf_mutex, icmap_mutex);
SSF_DECLARE_SHARED(void*, local_icmap);
#if PRIME_SSF_DISTSIM
SSF_DECLARE_SHARED(void*, new_iclist);
SSF_DECLARE_SHARED(void*, remote_icmap);
#endif

// map queue
SSF_DECLARE_SHARED(ssf_mutex, map_mutex);
SSF_DECLARE_SHARED(void*, map_req_head);
SSF_DECLARE_SHARED(void*, map_req_tail);

#if PRIME_SSF_REPORT_USER_TIMING
SSF_DECLARE_INCLUDE(<sys/times.h>);
SSF_DECLARE_SHARED(struct tms, clock_us);
SSF_DECLARE_PRIVATE(double, utime);
SSF_DECLARE_PRIVATE(double, stime);
SSF_DECLARE_SHARED(double, sum_utime);
#endif

SSF_DECLARE_INCLUDE("mac/machines.h");

SSF_DECLARE_SHARED(prime::ssf::uint32, total_lps);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_ent);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_evt);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_msg);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_msg_xprc);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_msg_xmem);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_tcs);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_pcs);
SSF_DECLARE_SHARED(prime::ssf::uint32, total_wmk);
SSF_DECLARE_SHARED(double, max_exec);

SSF_DECLARE_SHARED(double, init_ltime_wallclock_ratio);

SSF_DECLARE_PRIVATE(ltime_t, appointment);
SSF_DECLARE_PRIVATE(ltime_t, realnow);

//SSF_DECLARE_SHARED(ssf_mutex, apptmtlist_mutex);
//SSF_DECLARE_SHARED(SSF_UNIV_PTR_VECTOR, apptmtlist);

#define NUM_FIXED_ENV 2

namespace prime {
namespace ssf {

#if PRIME_SSF_INSTRUMENT
char* ssf_universe::actionDescriptions[ACTION_TOTAL] = {
"invalid action",
"initialized",
"determined next event",
"cancelled events happened",
"new entity events happened",
"new process events happened",
"time out events happened",
"outchannel events happened",
"inchannel events happened",
"timer events happened",
"ent schedule events happened",
"prc schedule events happened",
"semaphore signal event happened",
"postprocessed events",
"synchronized globally",
"synchronized locally",
"sent messages",
"received messages",
"yielded to other processes",
"shut down",
"performed other tasks"
};
#endif

#ifdef PRIME_SSF_SYNC_LIBSYNK
static unsigned int ssf_libsynk_fmhandler_num_maps;
static unsigned int ssf_libsynk_fmhandler_maps;
static unsigned int ssf_libsynk_fmhandler_num_icnames;
static unsigned int ssf_libsynk_fmhandler_icnames;

static int ssf_libsynk_got_num_maps = 0;
static int ssf_libsynk_maps_to_get = 0;
static int ssf_libsynk_got_maps = 0;
static int ssf_libsynk_maps_synchronized = 0;

static int ssf_libsynk_get_num_maps(FM_stream* strm, unsigned msgsiz)
{
  //assert(msgsiz == sizeof(int));
  int nmaps = 0;
  FM_receive(&nmaps, strm, sizeof(int));
  nmaps = SSF_BYTE_ORDER_32(nmaps);

  ssf_libsynk_got_num_maps++;
  ssf_libsynk_maps_to_get += nmaps;

#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] num_maps handler: "
	      "msgsiz=%u, msggot=%d, nmaps=%d, toget=%d\n", 
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, msgsiz, 
	      ssf_libsynk_got_num_maps, nmaps, ssf_libsynk_maps_to_get);
#endif

  if(ssf_libsynk_got_num_maps == SSF_USE_SHARED_RO(nmachs)-1 &&
     ssf_libsynk_maps_to_get == ssf_libsynk_got_maps)
    ssf_libsynk_maps_synchronized = 1;
  return FM_CONTINUE;
}

static int ssf_libsynk_get_maps(FM_stream* strm, unsigned msgsiz)
{
  //assert(msgsiz >= 4*sizeof(int)+sizeof(ltime_t));
  int mach, src_lp, src_ent, src_port, icname_len;
  ltime_t net_delay, delay;

  FM_receive(&mach, strm, sizeof(int));
  mach = SSF_BYTE_ORDER_32(mach);
  FM_receive(&src_lp, strm, sizeof(int));
  src_lp = SSF_BYTE_ORDER_32(src_lp);
  FM_receive(&src_ent, strm, sizeof(int));
  src_ent = SSF_BYTE_ORDER_32(src_ent);
  FM_receive(&src_port, strm, sizeof(int));
  src_port = SSF_BYTE_ORDER_32(src_port);
  FM_receive(&net_delay, strm, sizeof(ltime_t));
  ssf_ltime_deserialize(net_delay, (char*)&delay);
  FM_receive(&icname_len, strm, sizeof(int));
  icname_len = SSF_BYTE_ORDER_32(icname_len);
  assert(icname_len > 0);
  //assert(msgsiz == icname_len+4*sizeof(int)+sizeof(ltime_t));
  char* icname = new char[icname_len+1]; assert(icname);
  FM_receive(icname, strm, icname_len);
  icname[icname_len] = 0;

  SSF_UNIV_STRIC_MAP::iterator iter = 
    ((SSF_UNIV_STRIC_MAP*)
     SSF_USE_SHARED(local_icmap))->find(icname);
  if(iter != ((SSF_UNIV_STRIC_MAP*)
	      SSF_USE_SHARED(local_icmap))->end()) {
    ssf_logical_process::map_remote_local(src_lp, src_ent, src_port, 
				     (*iter).second, delay);
  }
  else {
    char msg[256];
    sprintf(msg, "inchannel \"%s\" not found", icname);
    ssf_throw_exception(ssf_exception::kernel_mapch, msg);
  }			   

  TM_TopoAddSender(mach);

#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] maps handler (from %d): "
	      "msgsiz=%u, src[lp=%d ent=%d port=%d] tgt[ic=%s]\n",
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, mach, msgsiz, 
	      src_lp, src_ent, src_port, icname);
#endif

  delete[] icname;

  ssf_libsynk_got_maps++;
  if(ssf_libsynk_got_num_maps == SSF_USE_SHARED_RO(nmachs)-1 &&
     ssf_libsynk_maps_to_get == ssf_libsynk_got_maps)
    ssf_libsynk_maps_synchronized = 1;
  return FM_CONTINUE;
}

static int ssf_libsynk_got_num_icnames = 0;
static int ssf_libsynk_icnames_to_get = 0;
static int ssf_libsynk_got_icnames = 0;
static int ssf_libsynk_icnames_synchronized = 0;

static int ssf_libsynk_get_num_icnames(FM_stream* strm, unsigned msgsiz)
{
  //assert(msgsiz == sizeof(int));
  int nics = 0;
  FM_receive(&nics, strm, sizeof(int));
  nics = SSF_BYTE_ORDER_32(nics);

  ssf_libsynk_got_num_icnames++;
  ssf_libsynk_icnames_to_get += nics;

#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] num_icnames handler: "
	      "msgsiz=%u, msggot=%d, nics=%d, toget=%d\n", 
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, msgsiz, 
	      ssf_libsynk_got_num_icnames, nics, ssf_libsynk_icnames_to_get);
#endif

  if(ssf_libsynk_got_num_icnames == SSF_USE_SHARED_RO(nmachs)-1 &&
     ssf_libsynk_icnames_to_get == ssf_libsynk_got_icnames)
    ssf_libsynk_icnames_synchronized = 1;
  return FM_CONTINUE;
}

static int ssf_libsynk_get_icnames(FM_stream* strm, unsigned msgsiz)
{
  //assert(msgsiz >= sizeof(int));
  int icname_len;
  FM_receive(&icname_len, strm, sizeof(int));
  icname_len = SSF_BYTE_ORDER_32(icname_len);
  assert(icname_len > 0);
  //assert(msgsiz == icname_len+2*sizeof(int));
  char* icname = new char[icname_len+1]; assert(icname);
  FM_receive(icname, strm, icname_len);
  icname[icname_len] = 0;
  int lp_serialno;
  FM_receive(&lp_serialno, strm, sizeof(int));
  lp_serialno = SSF_BYTE_ORDER_32(lp_serialno);

  if(!((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->
     insert(std::make_pair(icname, lp_serialno)).second) {
    char msg[256];
    sprintf(msg, "duplicated in-channel name \"%s\"", icname);
    ssf_throw_exception(ssf_exception::kernel_mapch, msg);
  }

#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] icnames handler: "
	      "msgsiz=%u, icname=%s lp_serialno=%d\n",
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, 
	      msgsiz, icname, lp_serialno);
#endif

  ssf_libsynk_got_icnames++;
  if(ssf_libsynk_got_num_icnames == SSF_USE_SHARED_RO(nmachs)-1 &&
     ssf_libsynk_icnames_to_get == ssf_libsynk_got_icnames)
    ssf_libsynk_icnames_synchronized = 1;
  return FM_CONTINUE;
}
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

void ssf_universe::link_lp(ssf_logical_process* lp)
{
  assert(lp);
  lp->universe = this;
  //printf("link_lp: [%d] %d: wait\n", SSF_SELF, whereami); fflush(0);
  ssf_mutex_wait(&lpset_lock);
  lpset.insert(lp);
  ssf_mutex_signal(&lpset_lock);
  //printf("link_lp: [%d] %d: signal\n", SSF_SELF, whereami); fflush(0);
}

void ssf_universe::unlink_lp(ssf_logical_process *lp)
{
  assert(lp && this == lp->universe);
  //printf("unlink_lp: [%d] %d: wait\n", SSF_SELF, whereami); fflush(0);
  ssf_mutex_wait(&lpset_lock);
  lpset.erase(lp);
  ssf_mutex_signal(&lpset_lock);
  //printf("unlink_lp: [%d] %d: signal\n", SSF_SELF, whereami); fflush(0);
  lp->universe = 0;
}

void ssf_universe::enque_lp(ssf_logical_process* lp)
{
  assert(SSF_SELF == whereami);
  assert(lp);
#if PRIME_SSF_EMULATION
  lp->on_stage = true;
  if(lp->emuable) rt_trampoline.push(lp);
  else trampoline.push(lp);
#else
  trampoline.push(lp);
#endif
  /*
  printf("[%d] insert LP#%d (key=%g) into trampoline (size=%d)\n",
         SSF_SELF, lp->serialno(), lp->simclock, trampoline->size());
  */
}

ssf_logical_process* ssf_universe::deque_lp()
{
  assert(SSF_SELF == whereami);
  /// HEREHERE
  //printf("%d: realnow=%lld: deque_lp(): real=%d,sim=%d\n", SSF_USE_SHARED_RO(whoami), wallclock_to_ltime(), (int)rt_trampoline.size(), (int)trampoline.size()); fflush(0);
  while(rolling_lps > 0) {
#if PRIME_SSF_EMULATION
    if(rt_trampoline.size() > 0) {
      SSF_USE_RTX_PRIVATE(realnow) = wallclock_to_ltime();
      if(rt_trampoline.update(SSF_USE_RTX_PRIVATE(realnow))) {
	ssf_logical_process* lp = rt_trampoline.top();
	rt_trampoline.pop();
	lp->on_stage = false;
	return lp;
      }
    }
#endif /*PRIME_SSF_EMULATION*/
    if(trampoline.size() > 0) {
      ssf_logical_process* lp = trampoline.top();
      trampoline.pop();
#if PRIME_SSF_EMULATION
      lp->on_stage = false;
#endif
      return lp;
    } else upgrade_prospect();
  }

  if(reserves.size() > 0) {
    /*
    if(SSF_USE_RTX_PRIVATE(decade) <= SSF_USE_SHARED(endtime))
      SSF_USE_RTX_PRIVATE(decade) = SSF_USE_SHARED(endtime)+1;
    */
    ssf_logical_process* lp = reserves.front();
    reserves.pop_front();
    return lp;
  } else return 0;
}

void ssf_universe::submit_prospect(ssf_logical_process* lp)
{
  assert(lp);
  if(lp->universe->whereami == SSF_SELF) {
    enque_lp(lp);
  } else {
    ssf_mutex_wait(&lp->universe->prospect_mutex);
    lp->universe->prospect.push_back(lp);
    ssf_mutex_signal(&lp->universe->prospect_mutex);
  }
}

#include <sys/time.h>
#include <time.h>

void ssf_universe::upgrade_prospect()
{
  assert(SSF_SELF == whereami); 
#if 0
  if(emuable) interrupt_requested(); // we should check emulation input
#endif
  ssf_mutex_wait(&prospect_mutex);
  for(SSF_UNIV_LP_VECTOR::iterator iter = prospect.begin();
      iter != prospect.end(); iter++) {
#if PRIME_SSF_EMULATION
    if((*iter)->emuable) rt_trampoline.push(*iter);
    else trampoline.push(*iter);
#else
    trampoline.push(*iter);
#endif
  }
  prospect.clear();
  ssf_mutex_signal(&prospect_mutex);

#if PRIME_SSF_EMULATION
  // in case no LP added, we give others a chance...
  if(!trampoline.size() && rt_trampoline.size()) {
    ssf_logical_process* lp = rt_trampoline.top();
    interrupt_requested(lp->next_due_time()); 

#define SOMETHRESHOLD 1000000 // 1 msec
    SSF_USE_RTX_PRIVATE(realnow) = wallclock_to_ltime();
    ltime_t ldelay = lp->next_due_time()-SSF_USE_RTX_PRIVATE(realnow);
    if(ldelay > SOMETHRESHOLD) {
      double secs = ldelay/ltime_wallclock_ratio;
      if(secs > 10) secs = 10; // to avoid overflow

      struct timespec req;
      req.tv_sec = time_t(secs);
      req.tv_nsec = long((secs-req.tv_sec)*1e9);
      /// HEREHERE
      //printf("%d: realnow=%lld: upgrade_prospect->nanosleep(%.9f)\n", SSF_USE_SHARED_RO(whoami), SSF_USE_RTX_PRIVATE(realnow), secs); fflush(0);
      nanosleep(&req, 0);

#if 0
      struct itimerval rttimer;
      struct itimerval old_rttimer;
      rttimer.it_value.tv_sec     = long(secs); /* A signal will be sent one second*/
      rttimer.it_value.tv_usec    = long((secs-rttimer.it_value.tv_sec)*1000000); /*  from when this is called */ 
      rttimer.it_interval.tv_sec  = 0; /* If the timer is not reset, the */
      rttimer.it_interval.tv_usec = 0; /*  signal will be sent every second */

      setitimer (ITIMER_REAL, &rttimer, &old_rttimer);
      sleep();
#endif
    } else ssf_thread_yield();
  } else 
#endif
    ssf_thread_yield();
}

void ssf_universe::reserve_lp(ssf_logical_process* lp)
{
  assert(SSF_SELF == whereami);
  assert(lp);
  reserves.push_back(lp);
}

int* ssf_universe::automap_array = 0;
int  ssf_universe::automap_index = 0;

int ssf_universe::automap()
{
  assert(SSF_SELF == 0);
  int is_first = 0;
  if(!automap_array) {
    automap_array = (int*)ssf_arena_malloc_fixed(sizeof(int)*SSF_NPROCS);
    assert(automap_array);
    is_first = 1;
  }
  if(!automap_index) {
    // compute the permutation: for the very first time, we make the
    // first choice be zero.
    register int i;
    for(i=0; i<SSF_NPROCS; i++) automap_array[i] = i;
    for(i=0; i<SSF_NPROCS-1; i++) {
      if(i==0 && is_first) continue;
      int j = i+(long)floor((SSF_NPROCS-i)*
			    SSF_USE_PRIVATE(krng)->uniform());
      if(i != j) {
	int swap = automap_array[i];
	automap_array[i] = automap_array[j];
	automap_array[j] = swap;
      }
    }
  }
  int ret = automap_array[automap_index++];
  if(automap_index == SSF_NPROCS) automap_index = 0;
  return ret;
}

int ssf_universe::datafile_partition_idx;
int ssf_universe::datafile_processor_idx;

void ssf_universe::reset_datafiles() 
{ 
  /* In order for NFS to consolidate individual data files, we need to
     put a delay here, though I really don't know how long it would be
     enough: my guess is that NFS needs to synchronize at the second
     level. -jason. */
#ifndef PRIME_SSF_ARCH_WINDOWS
  if(SSF_USE_SHARED_RO(nmachs) > 1) sleep(1);
#endif
  datafile_partition_idx = datafile_processor_idx = 0; 
}

#ifdef PRIME_SSF_ARCH_WINDOWS
int ssf_universe::next_datafile(std::ifstream& fd)
{
  while(datafile_partition_idx < SSF_USE_SHARED_RO(nmachs)) {
    int np = SSF_USE_SHARED_RO(mach_nprocs)[datafile_partition_idx];
    if(datafile_processor_idx >= np) {
      datafile_partition_idx++;
      datafile_processor_idx = 0;
      continue;
    }
    char fname[512];
    snprintf(fname, 512, "%s-%d-%d", SSF_USE_SHARED_RO(submodel) ?
	     SSF_USE_SHARED_RO(submodel)->datafile.c_str() : ".tmpdat",
	     datafile_partition_idx, datafile_processor_idx++);
    fname[511] = 0; // sanity
    fd.open(fname, std::ios::out|std::ios::binary);
    if(!fd) ssf_throw_warning("global wrapup missing data file \"%s\"", fname);
    return 1;
  } 
  return 0;
}
#else
int ssf_universe::next_datafile()
{
  while(datafile_partition_idx < SSF_USE_SHARED_RO(nmachs)) {
    int np = SSF_USE_SHARED_RO(mach_nprocs)[datafile_partition_idx];
    if(datafile_processor_idx >= np) {
      datafile_partition_idx++;
      datafile_processor_idx = 0;
      continue;
    }
    char fname[512];
    snprintf(fname, 512, "%s-%d-%d", SSF_USE_SHARED_RO(submodel) ?
	     SSF_USE_SHARED_RO(submodel)->datafile.c_str() : ".tmpdat",
	     datafile_partition_idx, datafile_processor_idx++);
    fname[511] = 0; // sanity
    int fd = open(fname, O_RDONLY);
    if(fd < 0) ssf_throw_warning("global wrapup missing data file \"%s\"", fname);
    return fd;
  } 
  return -1;
}
#endif

int ssf_universe::new_entity(Entity* ent)
{
  assert(whereami == SSF_SELF);
  total_entities++;
  int sn;
  if(preset_entity_id >= 0) { 
    ssf_mutex_wait(&SSF_USE_SHARED(entlist_mutex));
    ((SSF_UNIV_PTR_VECTOR*)SSF_USE_SHARED(entlist))->push_back(ent);
    ssf_mutex_signal(&SSF_USE_SHARED(entlist_mutex));
    sn = preset_entity_id; 
    preset_entity_id = -1;
  } else {
    ssf_mutex_wait(&SSF_USE_SHARED(entlist_mutex));
    ((SSF_UNIV_PTR_VECTOR*)SSF_USE_SHARED(entlist))->push_back(ent);
    sn = SSF_USE_SHARED(unique_entity_id);
    SSF_USE_SHARED(unique_entity_id) += SSF_USE_SHARED_RO(nmachs);
    ssf_mutex_signal(&SSF_USE_SHARED(entlist_mutex));
  }
  return sn;
}

void ssf_universe::delete_entity(Entity* ent)
{
  assert(whereami == SSF_SELF);
  total_entities--;
  /*
  ssf_mutex_wait(&SSF_USE_SHARED(entlist_mutex));
  SSF_USE_SHARED(entlist)->erase(ent);
  ssf_mutex_signal(&SSF_USE_SHARED(entlist_mutex));
  */
}

int ssf_universe::new_logiproc()
{
  ssf_mutex_wait(&SSF_USE_SHARED(logiproc_mutex));
  int sn = SSF_USE_SHARED(unique_logiproc_id);
  SSF_USE_SHARED(unique_logiproc_id) += SSF_USE_SHARED_RO(nmachs);
  ssf_mutex_signal(&SSF_USE_SHARED(logiproc_mutex));
  return sn;
}

ltime_t ssf_universe::add_alignment(Entity* ent1, Entity* ent2)
{
  ssf_alignment_request* req = new ssf_alignment_request;
  if(ent2 == 0) { // it means independent
    req->type = ssf_alignment_request::INDEP;
    req->ent1 = ent1;
    req->proc = -1; 
  } else {
    req->type = ssf_alignment_request::ALIGN;
    req->ent1 = ent1;
    req->ent2 = ent2;
  }
  req->next = 0;

  // add the node to the end of the link
  ssf_mutex_wait(&SSF_USE_SHARED(align_mutex));
  if(!SSF_USE_SHARED(align_req_head)) {
    SSF_USE_SHARED(align_req_head) = 
      SSF_USE_SHARED(align_req_tail) = req;
  } else {
    ((ssf_alignment_request*)SSF_USE_SHARED(align_req_tail))->next = req;
    SSF_USE_SHARED(align_req_tail) = req;
  }
  ssf_mutex_signal(&SSF_USE_SHARED(align_mutex));

  return SSF_USE_RTX_PRIVATE(decade);
}

ltime_t ssf_universe::add_alignment(Entity* ent1, int pid)
{
  ssf_alignment_request* req = new ssf_alignment_request;
  req->type = ssf_alignment_request::INDEP;
  req->ent1 = ent1;
  req->proc = pid;
  req->next = 0;

  // add the node to the end of the link
  ssf_mutex_wait(&SSF_USE_SHARED(align_mutex));
  if(!SSF_USE_SHARED(align_req_head)) {
    SSF_USE_SHARED(align_req_head) = 
      SSF_USE_SHARED(align_req_tail) = req;
  } else {
    ((ssf_alignment_request*)SSF_USE_SHARED(align_req_tail))->next = req;
    SSF_USE_SHARED(align_req_tail) = req;
  }
  ssf_mutex_signal(&SSF_USE_SHARED(align_mutex));

  return SSF_USE_RTX_PRIVATE(decade);
}

void ssf_universe::now_do_alignment()
{
  assert(SSF_SELF == 0); // processor 0, no need for mutex
  ssf_alignment_request* node = 
    (ssf_alignment_request*)SSF_USE_SHARED(align_req_head);
  while(node) {
    ssf_alignment_request* req = node;
    node = req->next;
    if(req->type == ssf_alignment_request::INDEP) 
      really_makeindependent(req->ent1, req->proc);
    else really_alignto(req->ent1, req->ent2);
    delete req;
  }
  SSF_USE_SHARED(align_req_head) = 
    SSF_USE_SHARED(align_req_tail) = 0;
}

void ssf_universe::really_alignto(Entity* ent0, Entity* ent1)
{
  assert(SSF_SELF == 0);
  assert(ent0 && ent1);
  if(ent0->_ent_timeline == ent1->_ent_timeline) return;

  // take calling entity off of its timeline list
  ssf_logical_process* tmln = (ssf_logical_process*)ent0->_ent_timeline;
  tmln->entities.erase(ent0);

  // attach to the target timeline
  ent0->_ent_timeline = ent1->_ent_timeline;
  ent1->_ent_timeline->entities.insert(ent0);

  if(tmln->entities.size() == 0) {
    tmln->universe->unlink_lp(tmln);
    delete tmln;
  }
}

void ssf_universe::really_makeindependent(Entity* ent, int p)
{
  assert(SSF_SELF == 0);
  assert(ent);

  ssf_logical_process* tmln = (ssf_logical_process*)ent->_ent_timeline;
  if(tmln->entities.size() > 1) { // make an independent timeline
    tmln->entities.erase(ent);

    // create new timeline and copy some info from current timeline
    ssf_logical_process* lp = new ssf_logical_process();
    ent->_ent_timeline = lp;
    lp->entities.insert(ent);
#if PRIME_SSF_INSTRUMENT
    // set alignment name to name of first entity (the only entity if using ssfnet)
    lp->setAlignmentName(ent->entityName());
#endif    
    // assign processor to the new timeline
    if(p >= 0) ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->link_lp(lp);
    else ((ssf_universe*)SSF_USE_SHARED(paruniv)[automap()])->link_lp(lp);
  } else if(p >= 0 && SSF_USE_SHARED(paruniv)[p] != tmln->universe) {
    // the timeline is reassigned to the new processor
    tmln->universe->unlink_lp(tmln);
    ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->link_lp(tmln);
  }
}

void ssf_universe::new_public_inchannel(inChannel* ic)
{
  ssf_mutex_wait(&SSF_USE_SHARED(icmap_mutex));
  if(!((SSF_UNIV_STRIC_MAP*)SSF_USE_SHARED(local_icmap))->
     insert(std::make_pair(ic->_ic_name, ic)).second) {
    char msg[256];
    sprintf(msg, "duplicated public inchannel \"%s\"", ic->_ic_name);
    ssf_throw_exception(ssf_exception::kernel_mapch, msg);
  }
#if PRIME_SSF_DISTSIM
  ((SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))->push_back(ic);
#endif
  ssf_mutex_signal(&SSF_USE_SHARED(icmap_mutex));
}

ltime_t ssf_universe::add_mapping(outChannel* oc, inChannel* ic, ltime_t delay)
{
  ssf_map_request* req = new ssf_map_request;
  req->type = ssf_map_request::POINTER;
  req->oc = oc;
  req->ic = ic;
  req->delay = delay;
  req->next = 0;

  ssf_mutex_wait(&SSF_USE_SHARED(map_mutex));
  if(!SSF_USE_SHARED(map_req_head)) {
    SSF_USE_SHARED(map_req_head) = 
      SSF_USE_SHARED(map_req_tail) = req;
  } else {
    ((ssf_map_request*)SSF_USE_SHARED(map_req_tail))->next = req;
    SSF_USE_SHARED(map_req_tail) = req;
  }
  ssf_mutex_signal(&SSF_USE_SHARED(map_mutex));

  return SSF_USE_RTX_PRIVATE(epoch);
}

ltime_t ssf_universe::add_mapping(outChannel* oc, char* icname, ltime_t delay)
{
  ssf_map_request* req = new ssf_map_request;
  req->type = ssf_map_request::STRING;
  req->oc = oc;
  int len = strlen(icname);
  req->icname = new char[len+1];
  strcpy(req->icname, icname);
  req->delay = delay;
  req->next = 0;

  ssf_mutex_wait(&SSF_USE_SHARED(map_mutex));
  if(!SSF_USE_SHARED(map_req_head)) {
    SSF_USE_SHARED(map_req_head) = 
      SSF_USE_SHARED(map_req_tail) = req;
  } else {
    ((ssf_map_request*)SSF_USE_SHARED(map_req_tail))->next = req;
    SSF_USE_SHARED(map_req_tail) = req;
  }
  ssf_mutex_signal(&SSF_USE_SHARED(map_mutex));

  return SSF_USE_RTX_PRIVATE(epoch);
}

#ifdef PRIME_SSF_SYNC_MPI
#define MAX_BUFSIZ 4096
#endif

void ssf_universe::now_do_mapping()
{
  assert(SSF_SELF == 0);

#if PRIME_SSF_DISTSIM
  synchronize_inchannel_names();

  int rmap_cnt = 0;
  ssf_map_request* rmap_head = 0;
  ssf_map_request* rmap_tail = 0;
#endif

  ssf_map_request* node = (ssf_map_request*)SSF_USE_SHARED(map_req_head);
  while(node) {
    ssf_map_request* req = node;
    node = req->next;
    assert(req->delay >= 0); // unmap unimplemented
    if(req->type == ssf_map_request::POINTER) {
      //printf("%d: %p mapto %p\n", SSF_USE_SHARED_RO(whoami), req->oc, req->ic); fflush(0);
      ssf_logical_process::map_local_local(req->oc, req->ic, req->delay);
      delete req;
    } else {
      //printf("%d: %p mapto \"%s\"\n", SSF_USE_SHARED_RO(whoami), 
      //       req->oc, req->icname); fflush(0);
      SSF_UNIV_STRIC_MAP::iterator iter = 
	((SSF_UNIV_STRIC_MAP*)
	 SSF_USE_SHARED(local_icmap))->find(req->icname);
      if(iter != ((SSF_UNIV_STRIC_MAP*)
		  SSF_USE_SHARED(local_icmap))->end()) {
	ssf_logical_process::map_local_local(req->oc, (*iter).second, req->delay);
	delete[] req->icname;
	delete req;
      }
      else {
#if PRIME_SSF_DISTSIM
	// remote inchannel name
	int lp_serialno = 0;
	SSF_UNIV_STRINT_MAP::iterator iter = 
	  ((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->find(req->icname);
	if(iter == ((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->end()) {
	  char msg[256];
	  sprintf(msg, "mapto/unmap unknown in-channel \"%s\"", req->icname);
	  ssf_throw_exception(ssf_exception::kernel_mapch, msg);
	}
	lp_serialno = (*iter).second;

	/*
	  int mach = lpid_to_machid(lp_serialno);
	  assert(mach != SSF_USE_SHARED_RO(whoami));
	*/

	if(req->oc->_oc_channel_delay+req->delay < SSF_USE_SHARED(epoch_length))
	  SSF_USE_SHARED(epoch_length) = req->oc->_oc_channel_delay+req->delay;
	ssf_logical_process::map_local_remote(req->oc, lp_serialno, req->delay);
	  
	// add to the link for further operations
	rmap_cnt++;
	req->next = 0;
	if(!rmap_head) rmap_head = rmap_tail = req;
	else {
	  rmap_tail->next = req;
	  rmap_tail = req;
	}
#else
	char msg[256];
	sprintf(msg, "mapto/unmap unknown in-channel \"%s\"", req->icname);
	ssf_throw_exception(ssf_exception::kernel_mapch, msg);
#endif
      }
    }
  }
  SSF_USE_SHARED(map_req_head) = SSF_USE_SHARED(map_req_tail) = 0;

  if(SSF_USE_SHARED_RO(nmachs) == 1) return;

#ifdef PRIME_SSF_SYNC_MPI
  // The following code looks very much like the one in function
  // synchronize_inchannel_names(). Instead of distributing public
  // names of in-channels, we broadcast mapping information.
  
  register int i, k;

  int maxmaps;
  if(MPI_Allreduce(&rmap_cnt, &maxmaps, 1, MPI_INT, MPI_MAX, MPI_COMM_WORLD))
    ssf_throw_exception(ssf_exception::kernel_mapch, 
			"failed to find max number of channel mappings");
  if(maxmaps == 0) return;

  char* sendbuf = new char[SSF_USE_SHARED_RO(nmachs)*MAX_BUFSIZ]; assert(sendbuf);
  char** sbuf = new char*[SSF_USE_SHARED_RO(nmachs)]; assert(sbuf);
  int* sdisp = new int[SSF_USE_SHARED_RO(nmachs)]; assert(sdisp);
  for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
    sdisp[i] = i*MAX_BUFSIZ;
    sbuf[i] = &sendbuf[sdisp[i]];
  }
  int* sendcnt = new int[SSF_USE_SHARED_RO(nmachs)]; assert(sendcnt);
  int* recvcnt = new int[SSF_USE_SHARED_RO(nmachs)]; assert(recvcnt);
  int* rdisp = new int[SSF_USE_SHARED_RO(nmachs)]; assert(rdisp);

  // pack and send 100 mappings at a time
  for(k=0; k<maxmaps; k+=100) {
    memset(sendcnt, 0, SSF_USE_SHARED_RO(nmachs)*sizeof(int));

    int nn = 0;
    while(rmap_head && nn<100) {
      ssf_map_request* req = rmap_head;
      rmap_head = req->next;
      nn++;

      int lp_serialno = 0;
      SSF_UNIV_STRINT_MAP::iterator iter = 
	((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->find(req->icname);
      if(iter != ((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->end())
	lp_serialno = (*iter).second;
      else assert(0);

      int mach = lpid_to_machid(lp_serialno);

      int src_lp = req->oc->_oc_owner->_ent_timeline->serialno();
      int src_ent = req->oc->_oc_owner->_ent_serialno;
      int src_port = req->oc->portno();
      int icname_len = strlen(req->icname);
      /*
      printf("%d: map_local_remote: source [lp=%d ent=%d port=%d] target [ic=%s, lp=%d]\n",
	     SSF_USE_SHARED_RO(whoami), src_lp, src_ent, src_port, 
	     req->icname, (int)lp_serialno);
      */

      if(MPI_Pack(&src_lp, 1, MPI_INT, sbuf[mach], 
		  MAX_BUFSIZ, &sendcnt[mach], MPI_COMM_WORLD) ||
	 MPI_Pack(&src_ent, 1, MPI_INT, sbuf[mach],
		  MAX_BUFSIZ, &sendcnt[mach], MPI_COMM_WORLD) ||
	 MPI_Pack(&src_port, 1, MPI_INT, sbuf[mach], 
		  MAX_BUFSIZ, &sendcnt[mach], MPI_COMM_WORLD) ||	  
	 MPI_Pack(&req->delay, 1, SSF_LTIME_MPI_TYPE, sbuf[mach], 
		  MAX_BUFSIZ, &sendcnt[mach], MPI_COMM_WORLD) ||
	 MPI_Pack(&icname_len, 1, MPI_INT, sbuf[mach],
		  MAX_BUFSIZ, &sendcnt[mach], MPI_COMM_WORLD) ||	  
	 MPI_Pack(req->icname, icname_len, MPI_UNSIGNED_CHAR, sbuf[mach], 
		  MAX_BUFSIZ, &sendcnt[mach], MPI_COMM_WORLD)) {
	ssf_throw_exception(ssf_exception::kernel_mapch, 
			    "failed to pack channel mapping information");
      }
      delete[] req->icname;
      delete req;
    }

    if(MPI_Alltoall(sendcnt, 1, MPI_INT, recvcnt, 1, MPI_INT, MPI_COMM_WORLD))
      ssf_throw_exception(ssf_exception::kernel_mapch, 
			  "failed to synchronize mapping info size");
    rdisp[0] = 0;
    for(i=1; i<SSF_USE_SHARED_RO(nmachs); i++) {
      rdisp[i] = rdisp[i-1]+recvcnt[i-1];    
    }
    int rsize = rdisp[SSF_USE_SHARED_RO(nmachs)-1]+
      recvcnt[SSF_USE_SHARED_RO(nmachs)-1];
    char* recvbuf = new char[rsize]; assert(recvbuf);
    if(MPI_Alltoallv(sendbuf, sendcnt, sdisp, MPI_PACKED,
		     recvbuf, recvcnt, rdisp, MPI_PACKED, MPI_COMM_WORLD)) {
      ssf_throw_exception(ssf_exception::kernel_mapch, 
			  "failed to synchronize mapping info");
    }
    for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
      if(i == SSF_USE_SHARED_RO(whoami)) continue;

      char* rbuf = &recvbuf[rdisp[i]];
      int bufsiz = recvcnt[i];
      int pos = 0;

      while(pos < bufsiz) {
	int src_lp, src_ent, src_port, icname_len;
	ltime_t delay;

	if(MPI_Unpack(rbuf, bufsiz, &pos, &src_lp, 1, 
		      MPI_INT, MPI_COMM_WORLD) ||
	   MPI_Unpack(rbuf, bufsiz, &pos, &src_ent, 1, 
		      MPI_INT, MPI_COMM_WORLD) ||
	   MPI_Unpack(rbuf, bufsiz, &pos, &src_port, 1, 
		      MPI_INT, MPI_COMM_WORLD) ||
	   MPI_Unpack(rbuf, bufsiz, &pos, &delay, 1, 
		      SSF_LTIME_MPI_TYPE, MPI_COMM_WORLD) ||
	   MPI_Unpack(rbuf, bufsiz, &pos, &icname_len, 1, 
		      MPI_INT, MPI_COMM_WORLD))
	  ssf_throw_exception(ssf_exception::kernel_mapch, 
			      "failed to unpack mapping info");
	
	char* icname = new char[icname_len+1]; assert(icname);
	if(MPI_Unpack(rbuf, bufsiz, &pos, icname, icname_len, 
		      MPI_UNSIGNED_CHAR, MPI_COMM_WORLD))
	  ssf_throw_exception(ssf_exception::kernel_mapch, 
			      "failed to unpack mapping info");
	icname[icname_len] = 0;

	SSF_UNIV_STRIC_MAP::iterator iter = 
	  ((SSF_UNIV_STRIC_MAP*)SSF_USE_SHARED(local_icmap))->find(icname);
	if(iter != ((SSF_UNIV_STRIC_MAP*)SSF_USE_SHARED(local_icmap))->end()) {
	  ssf_logical_process::map_remote_local(src_lp, src_ent, src_port, 
					   (*iter).second, delay);
	}
	else {
	  char msg[256];
	  sprintf(msg, "inchannel \"%s\" not found", icname);
	  ssf_throw_exception(ssf_exception::kernel_mapch, msg);
	}
	delete[] icname;
      }
    }
    delete[] recvbuf;
  }

  delete[] sendbuf;
  delete[] sbuf;
  delete[] sdisp;
  delete[] rdisp;
  delete[] sendcnt;
  delete[] recvcnt;
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
  ltime_t* ssf_libsynk_sendmap = 0;

  int* sendcnt = new int[SSF_USE_SHARED_RO(nmachs)]; assert(sendcnt);
  memset(sendcnt, 0, SSF_USE_SHARED_RO(nmachs)*sizeof(int));
  while(rmap_head) {
    ssf_map_request* req = rmap_head;
    rmap_head = req->next;

    int lp_serialno = 0;
    SSF_UNIV_STRINT_MAP::iterator iter = 
      ((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->find(req->icname);
    if(iter != ((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->end())
      lp_serialno = (*iter).second;
    else assert(0);

    int mach = lpid_to_machid(lp_serialno);
    assert(mach != SSF_USE_SHARED_RO(whoami));
    int net_mach = SSF_BYTE_ORDER_32(SSF_USE_SHARED_RO(whoami));
    sendcnt[mach]++;
    int src_lp = req->oc->_oc_owner->_ent_timeline->serialno();
    src_lp = SSF_BYTE_ORDER_32(src_lp);
    int src_ent = req->oc->_oc_owner->_ent_serialno;
    src_ent = SSF_BYTE_ORDER_32(src_ent);
    int src_port = req->oc->portno();
    src_port = SSF_BYTE_ORDER_32(src_port);
    ltime_t net_delay; ssf_ltime_serialize(req->delay, (char*)&net_delay);
    int icname_len = strlen(req->icname);
    int net_icname_len = SSF_BYTE_ORDER_32(icname_len);

    FM_stream* strm = FM_begin_message
      (mach, icname_len+4*sizeof(int)+sizeof(ltime_t), ssf_libsynk_fmhandler_maps);
    assert(strm);
    FM_send_piece(strm, &net_mach, sizeof(int));
    FM_send_piece(strm, &src_lp, sizeof(int));
    FM_send_piece(strm, &src_ent, sizeof(int));
    FM_send_piece(strm, &src_port, sizeof(int));
    FM_send_piece(strm, &net_delay, sizeof(ltime_t));
    FM_send_piece(strm, &net_icname_len, sizeof(int));
    FM_send_piece(strm, req->icname, icname_len);
    FM_end_message(strm);

    if(!ssf_libsynk_sendmap) {
      ssf_libsynk_sendmap = new ltime_t[SSF_USE_SHARED_RO(nmachs)];
      for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) 
	ssf_libsynk_sendmap[i] = SSF_LTIME_INFINITY;
    }
    if(req->oc->_oc_channel_delay+req->delay < ssf_libsynk_sendmap[mach])
      ssf_libsynk_sendmap[mach] = req->oc->_oc_channel_delay+req->delay;

#if PRIME_SSF_DEBUG 
    ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] send_map=>%d: "
		"src[lp=%d ent=%d port=%d] tgt[ic=%s, lp=%d]\n",
		SSF_USE_SHARED_RO(whoami), SSF_SELF, mach,
		SSF_BYTE_ORDER_32(src_lp), SSF_BYTE_ORDER_32(src_ent), 
		SSF_BYTE_ORDER_32(src_port), req->icname, lp_serialno);
#endif

    FM_extract(~0); ssf_yield_proc();

    delete[] req->icname;
    delete req;
  }

  register int i;
  for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
    if(i == SSF_USE_SHARED_RO(whoami)) continue;
    FM_stream* strm = FM_begin_message(i, sizeof(int), ssf_libsynk_fmhandler_num_maps);
    assert(strm);
    sendcnt[i] = SSF_BYTE_ORDER_32(sendcnt[i]);
    FM_send_piece(strm, &sendcnt[i], sizeof(int));
    FM_end_message(strm);

#if PRIME_SSF_DEBUG 
    ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] send_num_maps: %d\n",
		SSF_USE_SHARED_RO(whoami), SSF_SELF, 
		SSF_BYTE_ORDER_32(sendcnt[i]));
#endif

    FM_extract(~0); ssf_yield_proc();
  }

  delete[] sendcnt;
  while(!ssf_libsynk_maps_synchronized) {
    FM_extract(~0); ssf_yield_proc();
  }

  if(ssf_libsynk_sendmap) {
    for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
      if(i == SSF_USE_SHARED_RO(whoami)) continue;
      if(ssf_libsynk_sendmap[i] < SSF_LTIME_INFINITY) {
	TM_TopoAddReceiver(i, ssf_libsynk_sendmap[i]);
      }
    }
    delete[] ssf_libsynk_sendmap;
    ssf_libsynk_sendmap = 0;
  }
#endif /*PRIME_SSF_SYNC_LIBSYNK*/
}

#if PRIME_SSF_DISTSIM
int ssf_universe::lpid_to_machid(int lpid)
{
  int n = SSF_USE_SHARED_RO(submodel) ? 
    SSF_USE_SHARED_RO(submodel)->timeline_list.size() : 0;
  if(lpid < n) {
    assert(SSF_USE_SHARED_RO(submodel));
    ssf_dml_timeline* dmltl = (ssf_dml_timeline*)
      SSF_USE_SHARED_RO(submodel)->timeline_list[lpid];
    return dmltl->location;
  } else {
    return lpid%SSF_USE_SHARED_RO(nmachs);
  }
}

/* FIXME: MPICH implementation has a bug. When connecting two linux
   machines, one 64-bit and the other 32-bit, somehow, MPI_Allgatherv
   does not work properly. We can fix that by using separate
   MPI_Gatherv calls (see the code in the following included in #if
   and #else). The other problem we have is that MPI_LONG_LONG_INT
   type is not supported for packing and unpacking for data to be sent
   between these two machines. We don't have an easy solution to this
   problem, other than breaking the long long values into two long
   values, which we would have to change all the outstanding MPI calls
   involving transfering ltime_t type values. There are too many such
   places in the code that require fixing in this case. */

//#define MYPRINT

void ssf_universe::synchronize_inchannel_names()
{
  if(SSF_USE_SHARED_RO(nmachs) == 1) return;

#ifdef PRIME_SSF_SYNC_MPI
  int nnames = ((SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))->size();
#ifdef MYPRINT
  printf("%d: number of public icnames = %d\n", SSF_USE_SHARED_RO(whoami), nnames); 
  for(int h=0; h<nnames; h++) 
    printf(" %d: %s\n", h, (*(SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))[h]->_ic_name);
  fflush(0);
#endif
  int maxnnames;
  if(MPI_Allreduce(&nnames, &maxnnames, 1, MPI_INT, MPI_MAX, MPI_COMM_WORLD))
    ssf_throw_exception(ssf_exception::kernel_mapch, 
			"failed to find max number of public in-channel names");
#ifdef MYPRINT
  printf("%d: maxnames = %d\n", SSF_USE_SHARED_RO(whoami), maxnnames); fflush(0);
#endif
  if(maxnnames == 0) return;

  char* sendbuf = new char[MAX_BUFSIZ]; assert(sendbuf);
  int* rcnts = new int[SSF_USE_SHARED_RO(nmachs)]; assert(rcnts);
  int* rdisp = new int[SSF_USE_SHARED_RO(nmachs)]; assert(rdisp);

  register int i, j, k;
  // pack and send 100 names at a time
  for(k=0; k<maxnnames; k+=100) {
    int sendpos = 0;
    int nn = k<nnames ? nnames-k : 0; if(nn > 100) nn = 100;
    if(MPI_Pack(&nn, 1, MPI_INT, sendbuf, MAX_BUFSIZ, &sendpos, MPI_COMM_WORLD))
      ssf_throw_exception(ssf_exception::kernel_mapch, 
			  "failed to pack in-channel names");
    if(nn > 0) {
      //inChannel** iclist = (inChannel**)SSF_USE_SHARED(new_iclist)->get_list();
      for(i=k; i<k+nn; i++) {
	inChannel* ic = (*(SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))[i];
	char* icname = ic->_ic_name; assert(icname);
	int icname_len = strlen(icname);
	int lp_serialno = ic->_ic_owner->_ent_timeline->serialno();
#ifdef MYPRINT
	printf("%d: packing public icname %s->%d\n", SSF_USE_SHARED_RO(whoami),
	       icname, lp_serialno); fflush(0);
#endif
	if(MPI_Pack(&icname_len, 1, MPI_INT, 
		    sendbuf, MAX_BUFSIZ, &sendpos, MPI_COMM_WORLD) ||
	   MPI_Pack(icname, icname_len, MPI_UNSIGNED_CHAR, 
		    sendbuf, MAX_BUFSIZ, &sendpos, MPI_COMM_WORLD) ||
	   MPI_Pack(&lp_serialno, 1, MPI_INT, 
		    sendbuf, MAX_BUFSIZ, &sendpos, MPI_COMM_WORLD)) 
	  ssf_throw_exception(ssf_exception::kernel_mapch, 
			      "failed to pack in-channel names");
      }
    }

    if(MPI_Allgather(&sendpos, 1, MPI_INT, rcnts, 1, MPI_INT, MPI_COMM_WORLD))
      ssf_throw_exception(ssf_exception::kernel_mapch, 
			  "failed to synchronize in-channel pack sizes");
    rdisp[0] = 0;
    for(i=1; i<SSF_USE_SHARED_RO(nmachs); i++)
      rdisp[i] = rdisp[i-1]+rcnts[i-1];
    int rsize = rdisp[SSF_USE_SHARED_RO(nmachs)-1]+rcnts[SSF_USE_SHARED_RO(nmachs)-1];
#ifdef MYPRINT
    printf("%d: sendpos=%d\n", SSF_USE_SHARED_RO(whoami), sendpos);
    for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++)
      printf("%d: rcnts[%d]=%d, rdisp[%d]=%d\n", SSF_USE_SHARED_RO(whoami), 
	     i, rcnts[i], i, rdisp[i]);
    printf("%d: send data:\n", SSF_USE_SHARED_RO(whoami));
    for(i=0; i<sendpos; i++) printf("%02x ", unsigned(sendbuf[i]&0xff));
    printf("\n");
#endif
    char* recvbuf = new char[rsize]; assert(recvbuf);
#if 1
    if(MPI_Allgatherv(sendbuf, sendpos, MPI_PACKED, 
		      recvbuf, rcnts, rdisp, MPI_PACKED, MPI_COMM_WORLD))
      ssf_throw_exception(ssf_exception::kernel_mapch, 
			  "failed to synchronize in-channel names");
#else
    for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
      if(MPI_Gatherv(sendbuf, sendpos, MPI_PACKED, 
		     ((i==SSF_USE_SHARED_RO(whoami))?recvbuf:0), 
		     rcnts, rdisp, MPI_PACKED, i, MPI_COMM_WORLD))
	ssf_throw_exception(ssf_exception::kernel_mapch, 
			    "failed to synchronize in-channel names");
    }
#endif

#ifdef MYPRINT
    printf("%d: recv data:\n", SSF_USE_SHARED_RO(whoami));
    for(i=0; i<rsize; i++) printf("%02x ", unsigned(recvbuf[i]&0xff));
    printf("\n");
#endif

    for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
      if(i == SSF_USE_SHARED_RO(whoami)) continue;
      char* buf = &recvbuf[rdisp[i]];
      int bufsiz = rcnts[i];
      int pos = 0;
      int nnames;
      if(MPI_Unpack(buf, bufsiz, &pos, &nnames, 1, MPI_INT, MPI_COMM_WORLD))
	ssf_throw_exception(ssf_exception::kernel_mapch, 
			    "failed to unpack in-channel names");
#ifdef MYPRINT
      printf("%d=>%d: got %d public names\n", i,
	     SSF_USE_SHARED_RO(whoami), nnames);
#endif
      for(j=0; j<nnames; j++) {
	int icname_len;
	if(MPI_Unpack(buf, bufsiz, &pos, &icname_len, 1, 
		      MPI_INT, MPI_COMM_WORLD))
	  ssf_throw_exception(ssf_exception::kernel_mapch, 
			      "failed to unpack in-channel names");
	char* icname = new char[icname_len+1]; assert(icname);
	int lp_serialno;
	if(MPI_Unpack(buf, bufsiz, &pos, icname, icname_len, 
		      MPI_UNSIGNED_CHAR, MPI_COMM_WORLD) ||
	   MPI_Unpack(buf, bufsiz, &pos, &lp_serialno, 1, 
		      MPI_INT, MPI_COMM_WORLD))
	  ssf_throw_exception(ssf_exception::kernel_mapch, 
			      "failed to unpack in-channel names");
	icname[icname_len] = 0;

	if(!((SSF_UNIV_STRINT_MAP*)SSF_USE_SHARED(remote_icmap))->
	   insert(std::make_pair(icname, lp_serialno)).second) {
	  char msg[256];
	  sprintf(msg, "duplicated in-channel name \"%s\"", icname);
	  ssf_throw_exception(ssf_exception::kernel_mapch, msg);
	}
	assert(lpid_to_machid(lp_serialno) == i);
#ifdef MYPRINT
	printf("%d=>%d: %d: %s => %d\n", i, 
	       SSF_USE_SHARED_RO(whoami), j, icname, lp_serialno);
#endif
      }
    }
    delete recvbuf;
  }
  ((SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))->clear();

  delete[] sendbuf;
  delete[] rcnts;
  delete[] rdisp;
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
  int nnames = ((SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))->size();
  int net_nnames = SSF_BYTE_ORDER_32(nnames);
  register int i, j;
  for(i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
    if(i == SSF_USE_SHARED_RO(whoami)) continue;
    FM_stream* strm = FM_begin_message(i, sizeof(int), ssf_libsynk_fmhandler_num_icnames);
    assert(strm);
    FM_send_piece(strm, &net_nnames, sizeof(int));
    FM_end_message(strm);

#if PRIME_SSF_DEBUG 
    ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] send_num_icnames: %d\n",
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, nnames);
#endif

    FM_extract(~0); ssf_yield_proc();
  }
  for(i=0; i<nnames; i++) {
    inChannel* ic = (*(SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))[i];
    char* icname = ic->_ic_name; assert(icname);
    int icname_len = strlen(icname);
    int net_icname_len = SSF_BYTE_ORDER_32(icname_len);
    int lp_serialno = ic->_ic_owner->_ent_timeline->serialno();
    lp_serialno = SSF_BYTE_ORDER_32(lp_serialno);
    for(j=0; j<SSF_USE_SHARED_RO(nmachs); j++) {
      if(j == SSF_USE_SHARED_RO(whoami)) continue;
      FM_stream* strm = FM_begin_message(j, icname_len+2*sizeof(int),
					 ssf_libsynk_fmhandler_icnames);
      assert(strm);
      FM_send_piece(strm, &net_icname_len, sizeof(int));
      FM_send_piece(strm, icname, icname_len);
      FM_send_piece(strm, &lp_serialno, sizeof(int));
      FM_end_message(strm);

#if PRIME_SSF_DEBUG 
      ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] send_icname=>%d: "
		"icname=%s lp_serialno=%d\n", SSF_USE_SHARED_RO(whoami), 
		SSF_SELF, j, icname, SSF_BYTE_ORDER_32(lp_serialno));
#endif

      FM_extract(~0); ssf_yield_proc();
    }
  }
  while(!ssf_libsynk_icnames_synchronized) {
    FM_extract(~0); ssf_yield_proc();
  }
  ((SSF_UNIV_IC_VECTOR*)SSF_USE_SHARED(new_iclist))->clear();
#endif /*PRIME_SSF_SYNC_LIBSYNK*/
}
#endif /*PRIME_SSF_DISTSIM*/

#ifdef PRIME_SSF_SYNC_MPI
#undef MAX_BUFSIZ
#endif

void ssf_global_init_universe()
{
  // check start/end simulation time, valid only if taken from dml.
  if(SSF_USE_SHARED_RO(submodel)) {
    //SSF_USE_SHARED(sync_settled) = 1;
    SSF_USE_SHARED(startime) = SSF_USE_SHARED_RO(submodel)->startime;
    if(SSF_USE_SHARED(startime) < SSF_LTIME_MINUS_INFINITY) {
      ssf_throw_warning("simulation start time before SSF_LTIME_MINUS_INFINITY(%g), "
			"truncated to avoid overflow!", double(SSF_LTIME_MINUS_INFINITY));
      SSF_USE_SHARED(startime) = SSF_LTIME_MINUS_INFINITY;
    }
    SSF_USE_SHARED(endtime) = SSF_USE_SHARED_RO(submodel)->endtime;
    if(SSF_USE_SHARED(endtime) > SSF_LTIME_INFINITY) {
      ssf_throw_warning("simulation end time beyond SSF_LTIME_INFINITY(%g), "
			"truncated to avoid overflow!", double(SSF_LTIME_INFINITY));
      SSF_USE_SHARED(endtime) = SSF_LTIME_INFINITY;
    }
  } else {
    //SSF_USE_SHARED(sync_settled) = 0;
    SSF_USE_SHARED(startime) = SSF_LTIME_ZERO; //SSF_LTIME_MINUS_INFINITY;
    SSF_USE_SHARED(endtime) = SSF_LTIME_INFINITY;
  }

  // set environment variables:
  ssf_mutex_init(&SSF_USE_SHARED(environ_mutex));
  int sz = SSF_USE_SHARED_RO(submodel) ? 
    SSF_USE_SHARED_RO(submodel)->envars.size() : 0;
  sz += NUM_FIXED_ENV; // add the number of fixed environment variables
  char* buf = (char*)ssf_arena_malloc_fixed(sizeof(SSF_UNIV_STRSTR_MAP));
  SSF_USE_SHARED(environ) = new (buf) SSF_UNIV_STRSTR_MAP;

  // first, add the default environment variables
  char timebuf[32];
  sprintf(timebuf, "%g", (double)SSF_USE_SHARED(startime));
  SSF_CONTEXT_ENVIRONMENT->insert(SSF_MAKE_PAIR(SSF_STRING("start time"), 
						SSF_STRING(timebuf)));
  sprintf(timebuf, "%g", (double)SSF_USE_SHARED(endtime));
  SSF_CONTEXT_ENVIRONMENT->insert(SSF_MAKE_PAIR(SSF_STRING("end time"),
						SSF_STRING(timebuf)));
  
  if(SSF_USE_SHARED_RO(submodel)) {
    // second, add environment variables in DML
    for(SSF_UNIV_STRSTR_MAP::iterator iter = 
	  SSF_USE_SHARED_RO(submodel)->envars.begin();
	iter != SSF_USE_SHARED_RO(submodel)->envars.end(); iter++) {
      SSF_CONTEXT_ENVIRONMENT->insert(*iter);
    }
  }

  // init parallel universe
  SSF_USE_SHARED(paruniv) = (void**)
    ssf_arena_malloc_fixed(SSF_NPROCS*sizeof(ssf_universe*));
  assert(SSF_USE_SHARED(paruniv));

  ssf_mutex_init(&SSF_USE_SHARED(report_mutex));

  prime::rng::Random::set_seed_of_seeds(SSF_USE_SHARED_RO(seed));

  // prepare for the list the entities from DML
  if(SSF_USE_SHARED_RO(submodel)) {
    // plus one, in case it's zero
    SSF_USE_SHARED(entity_list) = 
      new void*[SSF_USE_SHARED_RO(submodel)->entity_list.size()+1];
    memset(SSF_USE_SHARED(entity_list), 0, 
	   sizeof(void*)*(SSF_USE_SHARED_RO(submodel)->entity_list.size()+1));
  }

  //buf = (char*)ssf_arena_malloc_fixed(sizeof(ssf_teleport));
  SSF_USE_SHARED(teleport) = new ssf_teleport; // permanent object

  /*initialize_globalbarrier();*/

  SSF_USE_SHARED(unique_edges) = new void*[SSF_NPROCS];
  SSF_USE_SHARED(entlist) = new SSF_UNIV_PTR_VECTOR;

  ssf_mutex_init(&SSF_USE_SHARED(entlist_mutex));
  ssf_mutex_init(&SSF_USE_SHARED(logiproc_mutex));

  /*
  buf = (char*)ssf_arena_malloc_fixed(sizeof(Vector_pVoid));
  SSF_USE_SHARED(apptmtlist) = new (buf) Vector_pVoid;
  assert(SSF_USE_SHARED(apptmtlist));
  ssf_mutex_init(&SSF_USE_SHARED(apptmtlist_mutex));
  */

  ssf_mutex_init(&SSF_USE_SHARED(align_mutex));
  SSF_USE_SHARED(align_req_head) = SSF_USE_SHARED(align_req_tail) = 0;

  ssf_mutex_init(&SSF_USE_SHARED(icmap_mutex));
  buf = (char*)ssf_arena_malloc_fixed(sizeof(SSF_UNIV_STRIC_MAP));
  SSF_USE_SHARED(local_icmap) = new (buf) SSF_UNIV_STRIC_MAP;

#if PRIME_SSF_DISTSIM
  buf = (char*)ssf_arena_malloc_fixed(sizeof(SSF_UNIV_IC_VECTOR));
  SSF_USE_SHARED(new_iclist) = new (buf) SSF_UNIV_IC_VECTOR;
  assert(SSF_USE_SHARED(new_iclist));

  buf = (char*)ssf_arena_malloc_fixed(sizeof(SSF_UNIV_STRINT_MAP));
  SSF_USE_SHARED(remote_icmap) = new (buf) SSF_UNIV_STRINT_MAP;
#endif

  ssf_mutex_init(&SSF_USE_SHARED(map_mutex));
  SSF_USE_SHARED(map_req_head) = SSF_USE_SHARED(map_req_tail) = 0;

  SSF_USE_SHARED(events_on_fire) = (ssf_kernel_event***)
    ssf_arena_malloc_fixed(SSF_NPROCS*sizeof(ssf_kernel_event**));

  ssf_mutex_init(&SSF_USE_SHARED(objhash_mutex));
  SSF_USE_SHARED(objhash) = 0;

  ssf_mutex_init(&SSF_USE_SHARED(apt_mutex));
  SSF_USE_SHARED(apt_handle) = 0;
  SSF_USE_SHARED(apt_hash) = new SSF_UNIV_INTPTR_MAP;
  SSF_USE_SHARED(apt_queue) = new ssf_shmem_barrier_queue();
}

void ssf_local_init_universe()
{
  SSF_USE_PRIVATE(processing_timeline) = 0;
  SSF_USE_PRIVATE(running_process) = 0;

  SSF_USE_PRIVATE(timeline_contexts) = 0;
  SSF_USE_PRIVATE(process_contexts) = 0;
  SSF_USE_PRIVATE(kernel_events) = 0;
  SSF_USE_PRIVATE(kernel_messages) = 0;
  SSF_USE_PRIVATE(kernel_messages_xprc) = 0;
  SSF_USE_PRIVATE(kernel_messages_xmem) = 0;
  SSF_USE_PRIVATE(direct_callbacks) = 0;

#if PRIME_SSF_MANAGE_KERNEL_EVENTS
  SSF_USE_PRIVATE(kevt_pool) = 0;
#endif

  SSF_USE_PRIVATE(universe) = SSF_USE_SHARED(paruniv)[SSF_SELF] 
    = new ssf_universe(SSF_SELF);
  assert(SSF_USE_PRIVATE(universe));

  SSF_USE_PRIVATE(krng) = new prime::rng::Random();
  assert(SSF_USE_PRIVATE(krng));

  SSF_USE_SHARED(unique_edges)[SSF_SELF] = new SSF_UNIV_TMINT_MAP;

  SSF_USE_SHARED(events_on_fire)[SSF_SELF] = (ssf_kernel_event**)
    ssf_arena_malloc_fixed(SSF_NPROCS*sizeof(ssf_kernel_event*));
  memset(SSF_USE_SHARED(events_on_fire)[SSF_SELF], 0, 
	 SSF_NPROCS*sizeof(ssf_kernel_event*));
}

static int compar(const void* a, const void *b) 
{
  ltime_t ta = *(ltime_t*)a;
  ltime_t tb = *(ltime_t*)b;
  if (ta<tb) return -1;
  if (ta>tb) return 1;
  return 0;
}

void ssf_run_universe()
{
  // settle down all the initializations
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->big_bang();

  if(SSF_SELF == 0) {
    extern int (*ssf_main_shared_ro)(int, char**);
    SSF_USE_SHARED(ret) = (*ssf_main_shared_ro)
      (SSF_USE_SHARED_RO(argc), SSF_USE_SHARED_RO(argv));
    if(SSF_USE_PRIVATE(simphase) != SSF_SIMPHASE_WRAPUP) {
      // the main has not invoked joinAll
      if(SSF_USE_SHARED(ret) == 0) {
	// throw exception only when the main returns 0; which will
	// prompt the error message "missing joinAll"
	ssf_throw_exception(ssf_exception::kernel_nojoin);
      } else {
	// otherwise, simply abort (ungraciously); supposedly _exit()
	// terminates all other processes
	_exit(SSF_USE_SHARED(ret));
      }
    }
  } else {
    ssf_universe::run_setup();
    SSF_USE_PRIVATE(simphase) = SSF_SIMPHASE_RUNNING;
    ssf_universe::run_continuation();
  }

  // if the output files are not marked to keep
#if PRIME_SSF_DISTSIM
  ssf_barrier();
  if(SSF_USE_SHARED_RO(nmachs) > 1 && SSF_SELF == 0) {
#ifdef PRIME_SSF_SYNC_MPI
    MPI_Barrier(MPI_COMM_WORLD);
#endif
#ifdef PRIME_SSF_SYNC_LIBSYNK
    FML_Barrier();
#endif
  }
  if(SSF_USE_SHARED_RO(whoami) == 0 && SSF_SELF == 0) {
    if(!SSF_USE_SHARED_RO(submodel) ||
       !SSF_USE_SHARED_RO(submodel)->datafile_keep) {
      int p = 0, q = 0;
      while(p < SSF_USE_SHARED_RO(nmachs)) {
	long np = SSF_USE_SHARED_RO(mach_nprocs)[p];
	if(q >= np) {
	  p++;
	  q = 0;
	  continue;
	}
	char fname[512];
	sprintf(fname, "%s-%d-%d", SSF_USE_SHARED_RO(submodel) ? 
		SSF_USE_SHARED_RO(submodel)->datafile.c_str() : ".tmpdat", p, q++);
	//printf("unlinking data file %s\n", fname); fflush(0);
	unlink(fname);
      } 
    }
  }
#else /*not PRIME_SSF_DISTSIM*/
  ssf_barrier();
  if(SSF_SELF == 0) {
    if(!SSF_USE_SHARED_RO(submodel) ||
       !SSF_USE_SHARED_RO(submodel)->datafile_keep) {
      assert(SSF_USE_SHARED_RO(nmachs) == 1);
      int q = 0;
      while(q < SSF_NPROCS) {
	char fname[512];
	sprintf(fname, "%s-0-%d", SSF_USE_SHARED_RO(submodel) ? 
		SSF_USE_SHARED_RO(submodel)->datafile.c_str() : ".tmpdat", q++);
	//printf("unlinking data file %s\n", fname); fflush(0);
	unlink(fname);
      } 
    }
  }
#endif /*not PRIME_SSF_DISTSIM*/
  ssf_barrier();
}

//double ssf_universe::ltime_wallclock_ratio;

#if PRIME_SSF_EMULATION
void ssf_universe::set_simulation_interval(ltime_t t0, ltime_t t1, double r)
#else
void ssf_universe::set_simulation_interval(ltime_t t0, ltime_t t1)
#endif
{
  if(t0 < SSF_LTIME_MINUS_INFINITY) {
    ssf_throw_warning("Entity::startAll start time before SSF_LTIME_MINUS_INFINITY(%g), "
		      "truncated to avoid overflow!", double(SSF_LTIME_MINUS_INFINITY));
    t0 = SSF_LTIME_MINUS_INFINITY;
  }
  if(t1 > SSF_LTIME_INFINITY) {
    ssf_throw_warning("Entity::startAll end time beyond SSF_LTIME_INFINITY(%g), "
		      "truncated to avoid overflow!", double(SSF_LTIME_INFINITY));
    t1 = SSF_LTIME_INFINITY;
  }

  if(SSF_USE_SHARED(startime) != t0) {
    SSF_USE_SHARED(startime) = t0;
    char timebuf[32];
    sprintf(timebuf, "%g", (double)SSF_USE_SHARED(startime));
    SSF_UNIV_STRSTR_MAP::iterator iter =
      SSF_CONTEXT_ENVIRONMENT->find("start time");
    if(iter != SSF_CONTEXT_ENVIRONMENT->end())
      (*iter).second = timebuf;
    else SSF_CONTEXT_ENVIRONMENT->insert
	   (SSF_MAKE_PAIR(SSF_STRING("start time"), SSF_STRING(timebuf)));
    SSF_USE_PRIVATE(now) = SSF_USE_PRIVATE(decade) = 
      SSF_USE_PRIVATE(epoch) = SSF_USE_SHARED(startime);
  }

  if(SSF_USE_SHARED(endtime) != t1) {
    SSF_USE_SHARED(endtime) = t1;
    char timebuf[32];
    sprintf(timebuf, "%g", (double)SSF_USE_SHARED(endtime));
    SSF_UNIV_STRSTR_MAP::iterator iter =
      SSF_CONTEXT_ENVIRONMENT->find("end time");
    if(iter != SSF_CONTEXT_ENVIRONMENT->end())
      (*iter).second = timebuf;
    else SSF_CONTEXT_ENVIRONMENT->insert
	   (SSF_MAKE_PAIR(SSF_STRING("end time"), SSF_STRING(timebuf)));
  }

  if(!SSF_USE_SHARED_RO(silent) && 
     SSF_USE_SHARED_RO(whoami) == 0 && SSF_SELF == 0) {
    fprintf(SSF_USE_SHARED_RO(outfile), "Simulation time [%g, %g]\n",
	    (double)SSF_USE_SHARED(startime), 
	    (double)SSF_USE_SHARED(endtime));
  }

#if PRIME_SSF_EMULATION
  assert(r >= 0);
  SSF_USE_SHARED(init_ltime_wallclock_ratio) = r;
#endif
}

void ssf_universe::run_setup()
{
  ssf_barrier();

  SSF_USE_PRIVATE(now) = SSF_USE_PRIVATE(decade) = 
    SSF_USE_PRIVATE(epoch) = SSF_USE_SHARED(startime);

#if PRIME_SSF_EMULATION
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->ltime_wallclock_ratio = 
    SSF_USE_SHARED(init_ltime_wallclock_ratio);
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->throttle_cond = 0;
#endif

  if(SSF_SELF == 0) {
/*#if PRIME_SSF_ENABLE_CHECKPOINTING
    if(SSF_USE_SHARED_RO(ckpt) > SSF_LTIME_ZERO) {
      ssf_shmem_barrier_set(&ssf_universe::checkpoint, SSF_USE_SHARED_RO(ckpt), 0, 1);
    }
#endif*/
    if(SSF_USE_SHARED_RO(progress) > SSF_LTIME_ZERO) {
      ssf_shmem_barrier_set(&ssf_universe::progress, SSF_USE_SHARED_RO(progress), 0, 1);
    }

    // check to see if new entities have been created by the main
    // function; if true, add the timeline, otherwise, delete it.
    if(((ssf_logical_process*)SSF_USE_SHARED(root_lp))->
       entities.size() > 0) {
      ((ssf_universe*)SSF_USE_PRIVATE(universe))->link_lp
	((ssf_logical_process*)SSF_USE_SHARED(root_lp));
      automap(); // pass the very first as it maps to processor 0
    } else delete (ssf_logical_process*)SSF_USE_SHARED(root_lp);
    SSF_USE_SHARED(root_lp) = 0; // no use any more
  }

#if PRIME_SSF_DEBUG
  ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] run_setup: "
	    "(recursively) consolidating alignment, init entities: "
	    "now=%g, decade=%g, epoch=%g, startime=%g, endtime=%g\n",
	    SSF_USE_SHARED_RO(whoami), SSF_SELF, (double)SSF_USE_PRIVATE(now), 
	    (double)SSF_USE_PRIVATE(decade), (double)SSF_USE_PRIVATE(epoch),
	    (double)SSF_USE_SHARED(startime), (double)SSF_USE_SHARED(endtime));
#endif
  // run init methods recursively
  int stripped_entidx = 0;
  for(;;) {
    ssf_barrier();
    if(SSF_SELF == 0) now_do_alignment();
    ssf_barrier();

    int entidx = stripped_entidx;
    stripped_entidx = ((SSF_UNIV_PTR_VECTOR*)SSF_USE_SHARED(entlist))->size();
    ssf_barrier();

    //printf("%d: #### %d -> %d\n", SSF_SELF, entidx, stripped_entidx); fflush(0);
    if(entidx < stripped_entidx) {
      // make an explicit copy of everything (suffice from entidx to stripped_entidx)
      ssf_mutex_wait(&SSF_USE_SHARED(entlist_mutex));
      SSF_UNIV_ENT_VECTOR entvec(*(SSF_UNIV_ENT_VECTOR*)SSF_USE_SHARED(entlist));
      ssf_mutex_signal(&SSF_USE_SHARED(entlist_mutex));

      for(int k=entidx; k<stripped_entidx; k++) {
	Entity* ent = entvec[k];
	if(((ssf_logical_process*)ent->_ent_timeline)->universe == 
	   ((ssf_universe*)SSF_USE_PRIVATE(universe))) {
	  ent->_ent_strip_initevt();
	}
      }
    } else break;
  } 
  ssf_barrier();

  /*
  SSF_USE_PRIVATE(now) = SSF_USE_PRIVATE(decade) = 
    SSF_USE_PRIVATE(epoch) = SSF_USE_SHARED(startime);
  */

#if PRIME_SSF_DEBUG
  ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] run_setup: "
	    "settling events in entities, consolidating channel mapping\n",
	    SSF_USE_SHARED_RO(whoami), SSF_SELF);
#endif

  // now the alignment is set; the events temporarily stored in
  // entities can now move to the timeline's event list.

  // make an explicit copy of everything
  ssf_mutex_wait(&SSF_USE_SHARED(entlist_mutex));
  SSF_UNIV_ENT_VECTOR entvec(*(SSF_UNIV_ENT_VECTOR*)SSF_USE_SHARED(entlist));
  ssf_mutex_signal(&SSF_USE_SHARED(entlist_mutex));

  for(int i=0; i<(int)entvec.size(); i++) {
    Entity* ent = entvec[i];
    if(((ssf_logical_process*)ent->_ent_timeline)->universe == 
       ((ssf_universe*)SSF_USE_PRIVATE(universe))) {
      ent->_ent_settle_events();
    }
  }
  ssf_barrier();

  if(SSF_SELF == 0) {
    //SSF_USE_SHARED(sync_settled) = 1;
    delete (SSF_UNIV_PTR_VECTOR*)SSF_USE_SHARED(entlist);
    SSF_USE_SHARED(entlist) = 0; // no use any more

    ssf_universe::now_do_mapping();
  } // SELF==0
  ssf_barrier();

  // if parallel start function is set, run it on every processor
  //if(__parallel_start != 0) (*__parallel_start)();
  SSF_USE_PRIVATE(appointment) = first_simulation_barrier();
}

struct ssf_edge_sort { ltime_t delay; ltime_t gap; int num; };
struct ssf_less_edge_sort : public SSF_LESS(ssf_edge_sort) {
  bool operator()(const ssf_edge_sort& t1, const ssf_edge_sort& t2) {
    return t1.num>t2.num || (t1.num==t2.num && t1.gap>t2.gap);
  }
};
typedef SSF_VECTOR(ssf_edge_sort) SSF_UNIV_EDGESORT_VECTOR;

static bool print_stats= true;
bool do_not_print_stats() {
	print_stats=false;
	return print_stats;
}

void ssf_universe::run_continuation()
{
  register int i;

#if PRIME_SSF_DEBUG
  ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] run_continuation: entering running phase\n",
	    SSF_USE_SHARED_RO(whoami), SSF_SELF);
#endif

  // important: after big_bang() !
  ((ssf_teleport*)SSF_USE_SHARED(teleport))->initialize();

#define SSF_UNIEDGE(p) ((SSF_UNIV_TMINT_MAP*)SSF_USE_SHARED(unique_edges)[p])

  // identify unique edges
  for(SSF_UNIV_LP_SET::iterator lpiter = 
	((ssf_universe*)SSF_USE_PRIVATE(universe))->lpset.begin();
      lpiter != ((ssf_universe*)SSF_USE_PRIVATE(universe))->lpset.end(); lpiter++) {
    for(SSF_LP_SG_VECTOR::iterator sgiter = (*lpiter)->outbound.begin();
	sgiter != (*lpiter)->outbound.end(); sgiter++) {
      ssf_stargate* sg = (*sgiter);
      if(sg->tgt && // local
	 sg->delay < SSF_USE_SHARED(epoch_length)) { // less than epoch length
	SSF_UNIV_TMINT_MAP::iterator iter = 
	  SSF_UNIEDGE(SSF_SELF)->find(sg->delay);
	if(iter != SSF_UNIEDGE(SSF_SELF)->end()) (*iter).second++;
	else SSF_UNIEDGE(SSF_SELF)->insert(SSF_MAKE_PAIR(sg->delay,1)); // unique
      }
    }
  }
  
  ssf_barrier();

  // now, gather all the edge weights into processor 0 and sort them
  if(SSF_SELF == 0) {
    SSF_UNIV_TMINT_MAP* base = SSF_UNIEDGE(0);
    for(i=1; i<SSF_NPROCS; i++) {
      SSF_UNIV_TMINT_MAP* other = SSF_UNIEDGE(i);
      for(SSF_UNIV_TMINT_MAP::iterator iter = other->begin();
	  iter != other->end(); iter++) {
	SSF_UNIV_TMINT_MAP::iterator myiter =
	  base->find((*iter).first);
	if(myiter != base->end()) (*myiter).second += (*iter).second;
	else base->insert(*iter);
      }
      delete other;
    }
    if(base->size() > 0) {
      if((int)base->size() <= SSF_USE_SHARED_RO(max_thresh_edges)) {
	SSF_USE_SHARED(sorted_edges) = (ltime_t*)
	  ssf_arena_malloc_fixed(sizeof(ltime_t)*(base->size()+1));
	i = 0;
	for(SSF_UNIV_TMINT_MAP::iterator iter = base->begin();
	    iter != base->end(); iter++, i++) {
	  SSF_USE_SHARED(sorted_edges)[i] = (*iter).first;
	}
	qsort(SSF_USE_SHARED(sorted_edges), base->size(), 
	      sizeof(ltime_t), &compar);
	SSF_USE_SHARED(num_sorted_edges) = base->size()+1;
	SSF_USE_SHARED(sorted_edges)[base->size()] = 
	  SSF_USE_SHARED(training_length) = 
	  3*SSF_USE_SHARED(sorted_edges)[base->size()-1];
      } else {
	SSF_UNIV_EDGESORT_VECTOR sortvec;
	for(SSF_UNIV_TMINT_MAP::iterator miter = base->begin();
	    miter != base->end(); miter++, i++) {
	  ssf_edge_sort item; 
	  item.delay = (*miter).first; 
	  item.gap = 0;
	  item.num = (*miter).second;
	  //printf(">> delay=%g num=%d\n", (double)item.delay, item.num);
	  sortvec.push_back(item);
	}
	std::sort(sortvec.begin(), sortvec.end(), ssf_less_edge_sort());
	ltime_t preval = SSF_LTIME_ZERO;
	for(SSF_UNIV_EDGESORT_VECTOR::iterator viter = sortvec.begin();
	    viter != sortvec.end(); viter++) {
	  (*viter).gap = (*viter).delay-preval;
	  preval = (*viter).delay;
	}
	std::sort(sortvec.begin(), sortvec.end(), ssf_less_edge_sort());
	SSF_USE_SHARED(sorted_edges) = (ltime_t*)
	  ssf_arena_malloc_fixed(sizeof(ltime_t)*(SSF_USE_SHARED_RO(max_thresh_edges)+1));
	for(i=0; i<SSF_USE_SHARED_RO(max_thresh_edges); i++) {
	  SSF_USE_SHARED(sorted_edges)[i] = sortvec[i].delay;
	  //printf("%d:%g\n", i, (double)sortvec[i].delay);
	}
	SSF_USE_SHARED(num_sorted_edges) = SSF_USE_SHARED_RO(max_thresh_edges)+1;
	SSF_USE_SHARED(sorted_edges)[SSF_USE_SHARED_RO(max_thresh_edges)] = 
	  SSF_USE_SHARED(training_length) = 
	  3*SSF_USE_SHARED(sorted_edges)[SSF_USE_SHARED_RO(max_thresh_edges)-1];
      }
    } else SSF_USE_SHARED(num_sorted_edges) = 0;
    delete base;
    delete[] SSF_USE_SHARED(unique_edges);

#if PRIME_SSF_DEBUG
    ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] run_continuation: "
	      "sorting unique edges (%d) for composite synchronization\n",
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, SSF_USE_SHARED(num_sorted_edges));
#endif
  }
  ssf_barrier();

#if PRIME_SSF_INSTRUMENT
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->setAction(ACTION_OTHER);
#endif


#undef SSF_UNIEDGE

  // running
  /*
  if(!SSF_USE_SHARED_RO(silent)) {
    fprintf(SSF_USE_SHARED_RO(outfile), "(%d,%d): running...\n", 
	    SSF_USE_SHARED_RO(whoami), SELF);
    fflush(0);
  }
  */

#if PRIME_SSF_EMULATION
#if PRIME_SSF_DEBUG
  ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] run_continuation: "
	    "spawning emulation reader/writer threads\n",
	    SSF_USE_SHARED_RO(whoami), SSF_SELF);
#endif
  // prepare for emulation, spawn off reader and writer threads
  ssf_thread_mutex_init(&((ssf_universe*)SSF_USE_PRIVATE(universe))->emutex);
  ssf_thread_cond_init(&((ssf_universe*)SSF_USE_PRIVATE(universe))->econd);
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->emuevt = 
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->emuevt_tail = 0;
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->emuable = 0;
  ssf_barrier();  
  if(SSF_SELF == 0) {
    int pid = SSF_NPROCS+10;
    for(int p=0; p<SSF_NPROCS; p++) {
      // for all timelines on the universe
      for(SSF_UNIV_LP_SET::iterator tmiter = 
	    ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->lpset.begin();
	  tmiter != ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->lpset.end(); tmiter++) {
	// for all entities in the timeline
	for(SSF_TML_ENT_SET::iterator entiter = (*tmiter)->entities.begin();
	    entiter != (*tmiter)->entities.end(); entiter++) {
	  if((*entiter)->_ent_emuable) 
	    ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->emuable =
	      (*tmiter)->emuable = 1;
	  // for all inchannels of the entity
	  for(SSF_ENT_IC_SET::iterator iciter = (*entiter)->_ent_inchannels.begin();
	      iciter != (*entiter)->_ent_inchannels.end(); iciter++) {
	    if((*iciter)->_ic_threadfct) {
	      ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->emuable = 
		(*tmiter)->emuable = (*entiter)->_ent_emuable = 1;
	      (*iciter)->_ic_thread = new ssf_thread_type;
	      (*iciter)->_ic_pid = pid++;
	      (*iciter)->_ic_parent_pid = ssf_thread_self();
	      ssf_thread_create((*iciter)->_ic_thread, (void (*)(void*))
				&(*iciter)->_ic_start_thread, (*iciter));
	    }
	  }
	  // for all outchannels of the entity
	  for(SSF_ENT_OC_SET::iterator ociter = (*entiter)->_ent_outchannels.begin();
	      ociter != (*entiter)->_ent_outchannels.end(); ociter++) {
	    if((*ociter)->_oc_threadfct) {
	      ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->emuable = 
		(*tmiter)->emuable = (*entiter)->_ent_emuable = 1;
	      (*ociter)->_oc_thread = new ssf_thread_type;
	      (*ociter)->_oc_pid = pid++;
	      ssf_thread_create((*ociter)->_oc_thread, (void (*)(void*))
				&(*ociter)->_oc_start_thread, (*ociter));
	    }
	  }
	}
      }
    }
  }
  ssf_barrier();
#endif /*PRIME_SSF_EMULATION*/

#if PRIME_SSF_REPORT_USER_TIMING
  long clk_tck = sysconf(_SC_CLK_TCK);
  struct tms start_clock_us;
  times(&start_clock_us);
  if(!SSF_USE_SHARED_RO(silent) && SSF_SELF == 0) {
    double ut = double(start_clock_us.tms_utime-SSF_USE_SHARED(clock_us).tms_utime)/clk_tck;
    double st = double(start_clock_us.tms_stime-SSF_USE_SHARED(clock_us).tms_stime)/clk_tck;
#if PRIME_SSF_SPLIT_CLOCK
    // for some reason, threaded versions (except LINUX) count overall running time,
    // instead per thread!
    ut /= SSF_NPROCS;
    st /= SSF_NPROCS;
#endif
    fprintf(SSF_USE_SHARED_RO(outfile), "[%d] setup time: user = %g, system = %g\n", 
	    SSF_USE_SHARED_RO(whoami), ut, st);
  }
#endif

#if !SSF_SHARED_HEAP_SEGMENT
  if(!SSF_USE_SHARED_RO(silent) && SSF_ARENA_ACTIVE) {
    prime::ssf::uint32 waterlevel = ssf_sum_reduction
      ((prime::ssf::uint32)SSF_USE_PRIVATE(mem_waterlevel));
    if(SSF_SELF == 0) {
      char s1[64], s2[64], s3[64];
		  fprintf(SSF_USE_SHARED_RO(outfile),
			  "[%d] memory usage before simulation:\n"
			  "[%d]   instant=%s, watermark=%s, oilmark=%s\n",
			  SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(whoami),
			  print_memory_usage(s1, waterlevel),
			  print_memory_usage(s2, SSF_USE_SHARED(mem_watermark)),
			  print_memory_usage(s3, SSF_USE_SHARED(mem_oilmark)));
    }
  }
#endif

  if(SSF_SELF == 0 && SSF_USE_SHARED_RO(progress) > SSF_LTIME_ZERO) {
    printf("[%d] progress mark: time=%g\n",
	   SSF_USE_SHARED_RO(whoami), (double)SSF_USE_PRIVATE(now));
    fflush(0);
  }

#if PRIME_SSF_EMULATION
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->simclock_startime =
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->simclock_reftime = SSF_USE_SHARED(startime);
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->wallclock_reftime =
#endif
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->wallclock_startime = ssf_get_wall_time();
  SSF_USE_PRIVATE(execution_time) = 
    -ssf_wall_time_to_sec(((ssf_universe*)SSF_USE_PRIVATE(universe))->wallclock_startime);
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->spin();
  SSF_USE_PRIVATE(execution_time) += ssf_wall_time_to_sec(ssf_get_wall_time());

#if PRIME_SSF_REPORT_USER_TIMING
  struct tms end_clock_us;
  times(&end_clock_us);
  SSF_USE_PRIVATE(utime) = double(end_clock_us.tms_utime-start_clock_us.tms_utime)/clk_tck;
  SSF_USE_PRIVATE(stime) = double(end_clock_us.tms_stime-start_clock_us.tms_stime)/clk_tck;
  /*
  printf("%d: user time = %g, system time = %g, total = %g\n", 
	 SELF, SSF_USE_PRIVATE(utime), SSF_USE_PRIVATE(stime), SSF_USE_PRIVATE(utime)+SSF_USE_PRIVATE(stime));
  fflush(0);
  */
#if PRIME_SSF_SPLIT_CLOCK
  // for some reason, threaded versions (except LINUX) count overall running time,
  // instead per thread!
  SSF_USE_PRIVATE(utime) /= SSF_NPROCS;
  SSF_USE_PRIVATE(stime) /= SSF_NPROCS;
#endif
#endif

#if !SSF_SHARED_HEAP_SEGMENT
  if(!SSF_USE_SHARED_RO(silent) && SSF_ARENA_ACTIVE) {
    prime::ssf::uint32 waterlevel = ssf_sum_reduction
      ((prime::ssf::uint32)SSF_USE_PRIVATE(mem_waterlevel));
    if(SSF_SELF == 0) {
      char s1[64], s2[64], s3[64];
      fprintf(SSF_USE_SHARED_RO(outfile), 
	      "[%d] memory usage after simulation:\n"
	      "[%d]   instant=%s, watermark=%s, oilmark=%s\n",
	      SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(whoami),
	      print_memory_usage(s1, waterlevel),
	      print_memory_usage(s2, SSF_USE_SHARED(mem_watermark)),
	      print_memory_usage(s3, SSF_USE_SHARED(mem_oilmark)));
    }
  }
#endif

  // finishing up everything
  /*
  if(!SSF_USE_SHARED_RO(silent)) {
    fprintf(SSF_USE_SHARED_RO(outfile), "(%d,%d): finalizing...\n",
	    SSF_USE_SHARED_RO(whoami), SELF);
    fflush(0);
  }
  */

#if PRIME_SSF_EMULATION
  ssf_barrier();
  if(SSF_SELF == 0) {
    for(int p=0; p<SSF_NPROCS; p++) {
      if(!((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->emuable) continue;
      for(SSF_UNIV_LP_SET::iterator tmiter = 
	    ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->lpset.begin();
	  tmiter != ((ssf_universe*)SSF_USE_SHARED(paruniv)[p])->lpset.end(); tmiter++) {
	if(!(*tmiter)->emuable) continue;
	for(SSF_TML_ENT_SET::iterator entiter = (*tmiter)->entities.begin();
	    entiter != (*tmiter)->entities.end(); entiter++) {
	  if(!(*entiter)->_ent_emuable) continue;
	  for(SSF_ENT_IC_SET::iterator iciter = (*entiter)->_ent_inchannels.begin();
	      iciter != (*entiter)->_ent_inchannels.end(); iciter++) {
	    if((*iciter)->_ic_threadfct && (*iciter)->_ic_thread) {
	      ssf_thread_delete((*iciter)->_ic_thread);
	      delete (*iciter)->_ic_thread;
	    }
	  }
	  for(SSF_ENT_OC_SET::iterator ociter = (*entiter)->_ent_outchannels.begin();
	      ociter != (*entiter)->_ent_outchannels.end(); ociter++) {
	    if((*ociter)->_oc_threadfct && (*ociter)->_oc_thread) {
	      ssf_thread_delete((*ociter)->_oc_thread);
	      delete (*ociter)->_oc_thread;
	    }
	  }
	}
      }
    }
  }
#endif /*PRIME_SSF_EMULATION*/
  ssf_barrier();

  SSF_USE_PRIVATE(simphase) = SSF_SIMPHASE_WRAPUP;

  // if parallel end function is set, run it on every processor
  //if(__parallel_end != 0) (*__parallel_end)();

  ((ssf_universe*)SSF_USE_PRIVATE(universe))->big_boom();

  if(SSF_USE_SHARED_RO(whoami) == 0 && SSF_SELF == 0) {
    if(Entity::_ent_global_wrapup != 0) 
      (*Entity::_ent_global_wrapup)();
  }
}

void ssf_local_wrapup_universe()
{
  delete SSF_USE_PRIVATE(krng);
  delete ((ssf_universe*)SSF_USE_PRIVATE(universe));
}

void ssf_global_wrapup_universe()
{
  SSF_CONTEXT_ENVIRONMENT->clear();

  //delete SSF_USE_SHARED(teleport);
  //ssf_arena_free(SSF_USE_SHARED(paruniv));
  /*{
    char** envlist = (char**)SSF_USE_SHARED(environ)->get_list();
    for(int i=0; envlist[i]; i++) delete envlist[i];
    delete SSF_USE_SHARED(environ);
  }*/
  
  if(SSF_USE_SHARED(objhash)) {
    delete ((SSF_UNIV_STRPTR_MAP*)SSF_USE_SHARED(objhash));
  }

  for(SSF_UNIV_INTPTR_MAP::iterator iter =
	((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->begin();
      iter != ((SSF_UNIV_INTPTR_MAP*)
	       SSF_USE_SHARED(apt_hash))->end(); iter++) {
    delete (ssf_shmem_barrier_appointment*)(*iter).second;
  }
  delete (SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash);
  delete (ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue);
}

ssf_universe::ssf_universe(int which) : 
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(SSF_GET_PRIVATE_DATA),
#endif
  whereami(which)
{
  //printf("%d: lpset_lock init\n", SSF_SELF); fflush(0);
  ssf_mutex_init(&lpset_lock);
  ssf_mutex_init(&prospect_mutex);

  total_entities = 0;
  preset_entity_id = -1;

  locked_down = 0;
  edge_index = -1;
  best_rate = -1;

  //stats = new TimeStat;
  //tap_reserve = 0;

#ifdef PRIME_SSF_SYNC_LIBSYNK
  if(SSF_USE_SHARED_RO(nmachs) > 1 && which == 0) {
    FML_FMInit();
    FML_RegisterHandler(&ssf_libsynk_fmhandler_num_maps, ssf_libsynk_get_num_maps);
    FML_RegisterHandler(&ssf_libsynk_fmhandler_maps, ssf_libsynk_get_maps);
    FML_RegisterHandler(&ssf_libsynk_fmhandler_num_icnames, ssf_libsynk_get_num_icnames);
    FML_RegisterHandler(&ssf_libsynk_fmhandler_icnames, ssf_libsynk_get_icnames);
    /*printf("FM handlers: num_maps=%d, maps=%d, num_icnames=%d, icnames=%d\n",
      ssf_libsynk_fmhandler_num_maps, ssf_libsynk_fmhandler_maps, ssf_libsynk_fmhandler_num_icnames, ssf_libsynk_fmhandler_icnames);*/

    TM_Init(0);
    //sleep(1); // make sure that remote machines are ready
  }
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

#if PRIME_SSF_INSTRUMENT
  actionCount = new long[ssf_universe::ACTION_TOTAL];
  actionTiming = new ssf_machine_time[ssf_universe::ACTION_TOTAL];
  for(int i=0; i<ssf_universe::ACTION_TOTAL; i++) {
    actionCount[i]=0;
    actionTiming[i]=0;
  }
  lastTimeMark = ssf_get_wall_time();
  currentAction = ACTION_INITIALIZING;
#endif

#if PRIME_SSF_EMULATION
  signal(SIGALRM, alrm_handler);
#endif
}

ssf_universe::~ssf_universe()
{
#if PRIME_SSF_INSTRUMENT
  delete [] actionCount;
  delete [] actionTiming;
#endif

#ifdef PRIME_SSF_SYNC_LIBSYNK
  if(SSF_USE_SHARED_RO(nmachs) > 1 && whereami == 0)
    FM_finalize();
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

  assert(rolling_lps == 0);
  assert(trampoline.size() == 0);
#if PRIME_SSF_EMULATION
  assert(rt_trampoline.size() == 0);
#endif
  assert(prospect.size() == 0);
  assert(reserves.size() == 0);

  //delete stats;

  // remove all logical processes
  for(SSF_UNIV_LP_SET::iterator iter = lpset.begin();
      iter != lpset.end(); iter++) {
    delete (*iter);
  }
  lpset.clear();

  ssf_barrier();
}

void ssf_universe::big_bang()
{
  SSF_USE_RTX_PRIVATE(simphase) = SSF_SIMPHASE_PREPARATION;

  SSF_USE_RTX_PRIVATE(now) = SSF_USE_RTX_PRIVATE(decade) = 
    SSF_USE_RTX_PRIVATE(epoch) = SSF_USE_SHARED(startime);
#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] big_bang: "
	    "entering preparation phase: set now,decade,epoch to startime=%g, endtime=%g\n",
	    SSF_USE_SHARED_RO(whoami), SSF_SELF, 
	    (double)SSF_USE_SHARED(startime), (double)SSF_USE_SHARED(endtime));
#endif

  if(SSF_SELF == 0) 
    SSF_USE_SHARED(epoch_length) = SSF_LTIME_INFINITY;

#if PRIME_SSF_REPORT_USER_TIMING
  if(!SSF_USE_SHARED_RO(silent) && SSF_SELF == 0)
    times(&SSF_USE_SHARED(clock_us));
#endif

#if !SSF_SHARED_HEAP_SEGMENT
  if(!SSF_USE_SHARED_RO(silent) && SSF_ARENA_ACTIVE) {
    prime::ssf::uint32 waterlevel = ssf_sum_reduction
      ((prime::ssf::uint32)SSF_USE_RTX_PRIVATE(mem_waterlevel));
    if(SSF_SELF == 0) {
  	  if(print_stats) {
		  char s1[64], s2[64], s3[64];
		  fprintf(SSF_USE_SHARED_RO(outfile),
			  "[%d] memory usage at startup:\n"
			  "[%d]   instant=%s, watermark=%s, oilmark=%s\n",
			  SSF_USE_SHARED_RO(whoami), SSF_USE_SHARED_RO(whoami),
			  print_memory_usage(s1, waterlevel),
			  print_memory_usage(s2, SSF_USE_SHARED(mem_watermark)),
			  print_memory_usage(s3, SSF_USE_SHARED(mem_oilmark)));
  	  }
    }
  }
#endif
  ssf_barrier();

  if(SSF_USE_SHARED_RO(submodel)) {
    // create and distribute timelines (on processor 0 at each machine)
    if(SSF_SELF == 0) {
#if PRIME_SSF_DEBUG 
      ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] big_bang: "
		"creating timelines from DML submodel (processor 0 only)\n", 
		SSF_USE_SHARED_RO(whoami), SSF_SELF);
#endif
      // compute the unique ids of logical processes that will be
      // created on this machine.
      int n = SSF_USE_SHARED_RO(submodel)->timeline_list.size();
      SSF_USE_SHARED(unique_logiproc_id) = (n+SSF_USE_SHARED_RO(nmachs)-1)/
	SSF_USE_SHARED_RO(nmachs)*SSF_USE_SHARED_RO(nmachs)+
	SSF_USE_SHARED_RO(whoami);

      // compute the unique ids of entities that will be created on
      // this machine.
      n = SSF_USE_SHARED_RO(submodel)->entity_list.size();
      SSF_USE_SHARED(unique_entity_id) = (n+SSF_USE_SHARED_RO(nmachs)-1)/
	SSF_USE_SHARED_RO(nmachs)*SSF_USE_SHARED_RO(nmachs)+
	SSF_USE_SHARED_RO(whoami);

      int idx = 0;
      for(SSF_DML_TML_VECTOR::iterator tl_iter =
	    SSF_USE_SHARED_RO(submodel)->timeline_list.begin();
	  tl_iter != SSF_USE_SHARED_RO(submodel)->timeline_list.end(); 
	  tl_iter++, idx++) {
#if PRIME_SSF_DISTSIM
	if((*tl_iter)->location == SSF_USE_SHARED_RO(whoami)) {
#endif
	  // creates the logical process prescribed by dml
	  ssf_logical_process* lp = new ssf_logical_process(idx);

	  // if dml specifies the processor assignment, do it;
	  // otherwise, we map it randomly.
	  if((*tl_iter)->processor >= 0) {
	    assert((*tl_iter)->processor < SSF_NPROCS);
	    ((ssf_universe*)SSF_USE_SHARED(paruniv)[(*tl_iter)->processor])->link_lp(lp);
	  } else ((ssf_universe*)SSF_USE_SHARED(paruniv)[automap()])->link_lp(lp);
#if PRIME_SSF_DISTSIM
	}
#endif
      }
    }
    ssf_barrier();

    // build entities in parallel
#if PRIME_SSF_DEBUG 
    ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] big_bang: "
	      "creating entities in parallel from DML submodel\n", 
	      SSF_USE_SHARED_RO(whoami), SSF_SELF);
#endif
    for(SSF_UNIV_LP_SET::iterator lp_iter = lpset.begin();
	lp_iter != lpset.end(); lp_iter++) {
      SSF_USE_RTX_PRIVATE(processing_timeline) = (*lp_iter);
      ssf_dml_timeline* dmltl = (ssf_dml_timeline*)
	SSF_USE_SHARED_RO(submodel)->timeline_list[(*lp_iter)->serialno()];
      for(SSF_DML_ENT_VECTOR::iterator ent_iter = dmltl->entities.begin(); 
	  ent_iter != dmltl->entities.end(); ent_iter++) {
	// create the entity using factory method (with preset entity
	// serial number)
	ssf_entity_dml_param** params = new ssf_entity_dml_param*[(*ent_iter)->params.size()+1];
	int idx = 0;
	for(SSF_DML_PARAM_VECTOR::iterator piter =
	      (*ent_iter)->params.begin();
	    piter != (*ent_iter)->params.end(); piter++)
	  params[idx++] = (*piter);
	params[idx] = 0;
	preset_entity_id = (*ent_iter)->serialno;
	Entity* ent = Entity::_ent_create_registered_entity
	  ((*ent_iter)->instanceof.c_str(), (const ssf_entity_dml_param**)params);
	if(!ent) {
	  char msg[256];
	  sprintf(msg, "can't create entity \"%s\"", 
		  (*ent_iter)->instanceof.c_str());
	  ssf_throw_exception(ssf_exception::kernel_mkmodel, msg);
	}
	delete[] params;
	ent->init();
	ent->config((*ent_iter)->configs);
	SSF_USE_SHARED(entity_list)[(*ent_iter)->serialno] = ent;
	//total_entities++; <- will be counted in new_entity()

	// declare external inchannels and outchannels: this requires
	// that entity constructor has defined them!
	for(SSF_DML_STRINT_MAP::iterator ic_iter = 
	      (*ent_iter)->ext_inc.begin(); 
	    ic_iter != (*ent_iter)->ext_inc.end(); ic_iter++) {
	  if(ent->_ent_preset_inchannel((*ic_iter).first.c_str(), 
					(*ic_iter).second)) {
	    char msg[256];
	    sprintf(msg, "entity %s inchannel %s undefined",
		    (*ent_iter)->instanceof.c_str(), 
		    (*ic_iter).first.c_str());
	    ssf_throw_exception(ssf_exception::kernel_mkmodel, msg);
	  }
	}
	for(SSF_DML_STRINT_MAP::iterator oc_iter = 
	      (*ent_iter)->ext_outc.begin(); 
	    oc_iter != (*ent_iter)->ext_outc.end(); oc_iter++) {
	  if(ent->_ent_preset_outchannel((*oc_iter).first.c_str(), 
					 (*oc_iter).second)) {
	    char msg[256];
	    sprintf(msg, "entity %s outchannel %s undefined",
		    (*ent_iter)->instanceof.c_str(), 
		    (*oc_iter).first.c_str());
	    ssf_throw_exception(ssf_exception::kernel_mkmodel, msg);
	  }
	}
      }
    }
    ssf_barrier();

    // establish mapping (by processor 0 on each machine)
    if(SSF_SELF == 0) {
#if PRIME_SSF_DEBUG 
      ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] big_bang: "
		"creating channel mappings from DML submodel (processor 0 only)\n", 
		SSF_USE_SHARED_RO(whoami), SSF_SELF);
#endif
      for(SSF_DML_MAP_VECTOR::iterator miter = 
	    SSF_USE_SHARED_RO(submodel)->map_list.begin();
	  miter != SSF_USE_SHARED_RO(submodel)->map_list.end(); miter++) {
	Entity* from = (Entity*)SSF_USE_SHARED(entity_list)[(*miter)->from->serialno];
	Entity* to = (Entity*)SSF_USE_SHARED(entity_list)[(*miter)->to->serialno];
	outChannel* oc = 0;
	inChannel* ic = 0;
	if(from) {
	  SSF_ENT_INTOC_MAP::iterator iter =
	    from->_ent_ocport.find((*miter)->from_port);
	  if(iter != from->_ent_ocport.end()) oc = (*iter).second;
	  else {
	    char msg[256];
	    sprintf(msg, "unable to find outchannel (portno=%d) "
		    "for mapping", (*miter)->from_port);
	    ssf_throw_exception(ssf_exception::kernel_mapch, msg);
	  }
	}
	if(to) {
	  SSF_ENT_INTIC_MAP::iterator iter =
	    to->_ent_icport.find((*miter)->to_port);
	  if(iter != to->_ent_icport.end()) ic = (*iter).second;
	  else {
	    char msg[256];
	    sprintf(msg, "unable to find inchannel (portno=%d) "
			  "for mapping", (*miter)->to_port);
	    ssf_throw_exception(ssf_exception::kernel_mapch, msg);
	  }
	}
	if(from && to)
	  ssf_logical_process::map_local_local(oc, ic, (*miter)->delay);
	else {
#if PRIME_SSF_DISTSIM
	  if(from || to) { // the mapping starts from or ends at this machine
	    if(from) {
	      ssf_logical_process::map_local_remote
		(oc, (*miter)->to->timeline->serialno, (*miter)->delay);
	      // calculate the epoch length to be minimum length of
	      // cross machine channels
	      if(oc->_oc_channel_delay+(*miter)->delay < 
		 SSF_USE_SHARED(epoch_length))
		SSF_USE_SHARED(epoch_length) = 
		  oc->_oc_channel_delay+(*miter)->delay;
	    } else {
	      ssf_logical_process::map_remote_local
		((*miter)->from->timeline->serialno, (*miter)->from->serialno, 
		 (*miter)->from_port, ic, (*miter)->delay);
	    }
	  }
#else /*not PRIME_SSF_DISTSIM*/
	  char msg[256];
	  sprintf(msg, "unable to map entity %d(%d) to entity %d(%d) locally",
		  (*miter)->from->serialno, (*miter)->from_port,
		  (*miter)->to->serialno, (*miter)->to_port);
	  ssf_throw_exception(ssf_exception::kernel_mapch, msg);
#endif /*not PRIME_SSF_DISTSIM*/
	}
      }
      delete[] SSF_USE_SHARED(entity_list);
      SSF_USE_SHARED(entity_list) = 0; // no use any more.
    } 
  } 

  else { // if SSF_USE_SHARED_RO(submodel)==0
    SSF_USE_SHARED(unique_logiproc_id) = SSF_USE_SHARED_RO(whoami);
    SSF_USE_SHARED(unique_entity_id) = SSF_USE_SHARED_RO(whoami);
  }
  ssf_barrier();

  // create temporary data output file for each processor
  char fname[512];
  snprintf(fname, 512, "%s-%d-%d", SSF_USE_SHARED_RO(submodel) ? 
	   SSF_USE_SHARED_RO(submodel)->datafile.c_str() : ".tmpdat",
	   SSF_USE_SHARED_RO(whoami), SSF_SELF);
  fname[511] = 0; // sanity
  //printf("creating data file %s\n", fname); fflush(0);
#ifdef PRIME_SSF_ARCH_WINDOWS
  SSF_USE_RTX_PRIVATE(datafile).open(fname, std::ios::out|std::ios::binary);
  if(!SSF_USE_RTX_PRIVATE(datafile)) {
    char msg[256];
    int errcode = errno;
    sprintf(msg, "unable to open file for data recording: %s: %s\n", 
	    fname, strerror(errcode));
    ssf_throw_exception(ssf_exception::kernel_dumpdata, msg);
  }
#else
  SSF_USE_RTX_PRIVATE(datafile) = creat(fname, 0644);
  if(SSF_USE_RTX_PRIVATE(datafile) < 0) {
    char msg[256];
    int errcode = errno;
    sprintf(msg, "unable to open file for data recording: %s: %s\n", 
	    fname, strerror(errcode));
    ssf_throw_exception(ssf_exception::kernel_dumpdata, msg);
  }
#endif
#if PRIME_SSF_DEBUG
  ssf_debug(SSF_DEBUG_UNIVERSE, "<UNIV> [%d:%d] big_bang: "
	    "creating temporary file \"%s\" and entering init phase\n",
	    SSF_USE_SHARED_RO(whoami), SSF_SELF, fname);
#endif

  // create the root timeline!
  SSF_USE_RTX_PRIVATE(simphase) = SSF_SIMPHASE_INITIALIZATION;
  if(SSF_SELF == 0) {
    SSF_USE_RTX_PRIVATE(processing_timeline) = 
      SSF_USE_SHARED(root_lp) = new ssf_logical_process();
  }
  ssf_barrier();
}

void ssf_universe::spin() 
{
  //stats->start_collection();
  //stats->push_state(TimeStat::STATE_SYNC);

  for(;;) {
    int ret = expand_epoch();
    process_epoch();
    if(ret) break;
  }
#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_ENDING);
#endif

  if(0 == lpset.size()) {
    //printf("[%d] tapping reserve...\n", SSF_SELF);
    ssf_barrier();
  }

  SSF_USE_RTX_PRIVATE(now) = SSF_USE_SHARED(endtime);
  ssf_barrier();

  //stats->pop_state(); // must be TimeStat::STATE_SYNC
  //stats->finish_collection();
}

void ssf_universe::big_boom() 
{
  //SSF_USE_RTX_PRIVATE(now) = SSF_USE_SHARED(endtime);
  int nents = 0;
  for(SSF_UNIV_LP_SET::iterator lpiter = lpset.begin();
      lpiter != lpset.end(); lpiter++) {
    nents += (*lpiter)->entities.size();
    (*lpiter)->wrapup();
  }

#ifdef PRIME_SSF_ARCH_WINDOWS
  SSF_USE_RTX_PRIVATE(datafile).close();
#else
  close(SSF_USE_RTX_PRIVATE(datafile));
#endif
  ssf_barrier();

  if(SSF_USE_SHARED_RO(silent)) {
    ((ssf_teleport*)SSF_USE_SHARED(teleport))->serialize_reporting_begin();
    ((ssf_teleport*)SSF_USE_SHARED(teleport))->serialize_reporting_end();
    ((ssf_teleport*)SSF_USE_SHARED(teleport))->finalize();
    return;
  }

  ((ssf_teleport*)SSF_USE_SHARED(teleport))->serialize_reporting_begin();

  ssf_mutex_wait(&SSF_USE_SHARED(report_mutex));
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d]-------------- processor %d --------------\n", 
	  SSF_USE_SHARED_RO(whoami), SSF_SELF);
#if PRIME_SSF_REPORT_USER_TIMING
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  CPU time: user = %g, system = %g\n", 
	  SSF_USE_SHARED_RO(whoami), SSF_SELF, SSF_USE_RTX_PRIVATE(utime), SSF_USE_RTX_PRIVATE(stime));
#endif
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  timelines = %d\n", 
	  SSF_USE_SHARED_RO(whoami), SSF_SELF, (int)lpset.size());
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  entities = %d (%d created here)\n",
	  SSF_USE_SHARED_RO(whoami), SSF_SELF, nents, total_entities);
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  kernel events = %d\n", 
	  SSF_USE_SHARED_RO(whoami), SSF_SELF, SSF_USE_RTX_PRIVATE(kernel_events));
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  messages = %d [xprc=%d, xmem=%d]\n", 
	  SSF_USE_SHARED_RO(whoami), SSF_SELF, SSF_USE_RTX_PRIVATE(kernel_messages),
	  SSF_USE_RTX_PRIVATE(kernel_messages_xprc), SSF_USE_RTX_PRIVATE(kernel_messages_xmem));
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  timeline context switches = %d\n", 
	  SSF_USE_SHARED_RO(whoami), SSF_SELF, SSF_USE_RTX_PRIVATE(timeline_contexts));
  fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  process context switches = %d\n", 
	  SSF_USE_SHARED_RO(whoami), SSF_SELF, SSF_USE_RTX_PRIVATE(process_contexts));
#if !SSF_SHARED_HEAP_SEGMENT
  if(SSF_ARENA_ACTIVE && SSF_SELF == 0) {
    char s1[64], s2[64];
    fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d)  memory watermark = %s, oilmark = %s\n",
	    SSF_USE_SHARED_RO(whoami), SSF_SELF,
	    print_memory_usage(s1, SSF_USE_SHARED(mem_watermark)),
	    print_memory_usage(s2, SSF_USE_SHARED(mem_oilmark)));
  }
#endif

#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_INVALID);
  dumpInstrumentationData();
#endif

  fflush(0);
  ssf_mutex_signal(&SSF_USE_SHARED(report_mutex));

  ((ssf_teleport*)SSF_USE_SHARED(teleport))->serialize_reporting_end();

  ((ssf_teleport*)SSF_USE_SHARED(teleport))->finalize();

  if(SSF_SELF == 0) {
    if(SSF_USE_SHARED_RO(whoami) == 0) {
      fprintf(SSF_USE_SHARED_RO(outfile), "========================= summary ==========================\n");
      fprintf(SSF_USE_SHARED_RO(outfile), "  execution time (max wall-clock time) = %g\n", 
	      SSF_USE_SHARED(max_exec));
#if PRIME_SSF_REPORT_USER_TIMING
      fprintf(SSF_USE_SHARED_RO(outfile), "  CPU user time: total = %g, avg = %g\n", 
	      SSF_USE_SHARED(sum_utime), SSF_USE_SHARED(sum_utime)/ssf_total_num_processors());
#endif
      fprintf(SSF_USE_SHARED_RO(outfile), "  total timelines = %d\n", SSF_USE_SHARED(total_lps));
      fprintf(SSF_USE_SHARED_RO(outfile), "  total entities = %d\n", SSF_USE_SHARED(total_ent));
      fprintf(SSF_USE_SHARED_RO(outfile), "  total kernel events = %d ", SSF_USE_SHARED(total_evt));
      if(SSF_USE_SHARED(max_exec) > 0) 
	fprintf(SSF_USE_SHARED_RO(outfile), "(rate = %g/sec)\n", 
		SSF_USE_SHARED(total_evt)/SSF_USE_SHARED(max_exec));
      else fprintf(SSF_USE_SHARED_RO(outfile), "\n");
      fprintf(SSF_USE_SHARED_RO(outfile), "  total messages = %d (xprc=%d, xmem=%d)\n", 
	      SSF_USE_SHARED(total_msg), SSF_USE_SHARED(total_msg_xprc), SSF_USE_SHARED(total_msg_xmem));
      fprintf(SSF_USE_SHARED_RO(outfile), "  total timeline context switches = %d ", 
	      SSF_USE_SHARED(total_tcs));
      if(SSF_USE_SHARED(max_exec) > 0) 
	fprintf(SSF_USE_SHARED_RO(outfile), "(rate = %g/sec)\n", 
		SSF_USE_SHARED(total_tcs)/SSF_USE_SHARED(max_exec));
      else fprintf(SSF_USE_SHARED_RO(outfile), "\n");
      fprintf(SSF_USE_SHARED_RO(outfile), "  total process context switches = %d ", 
	      SSF_USE_SHARED(total_pcs));
      if(SSF_USE_SHARED(max_exec) > 0) 
	fprintf(SSF_USE_SHARED_RO(outfile), "(rate = %g/sec)\n", 
		SSF_USE_SHARED(total_pcs)/SSF_USE_SHARED(max_exec));
      else fprintf(SSF_USE_SHARED_RO(outfile), "\n");
      if(SSF_USE_SHARED(total_wmk) > 0) {
	char s1[64];
	fprintf(SSF_USE_SHARED_RO(outfile), "  total memory usage = %s\n", 
		print_memory_usage(s1, SSF_USE_SHARED(total_wmk)));
      }
      fflush(0);
    }
    
    
    /*
    {
      register int i, j;
      double *tt = new double[TimeStat::TOTAL_STATES]; assert(tt);
      fprintf(SSF_USE_SHARED_RO(outfile), "\n[%d]***** execution timing statistics *****\n",
	      SSF_USE_SHARED_RO(whoami));
      fprintf(SSF_USE_SHARED_RO(outfile), "[%d]PN\t", SSF_USE_SHARED_RO(whoami));
      for(j=0; j<TimeStat::TOTAL_STATES; j++) {
	fprintf(SSF_USE_SHARED_RO(outfile), "%s\t", TimeStat::state_desc[j]);
	tt[j] = 0;
      }
      fprintf(SSF_USE_SHARED_RO(outfile), "\n");
      for(i=0; i<SSF_NPROCS; i++) {
	fprintf(SSF_USE_SHARED_RO(outfile), "[%d]%d", SSF_USE_SHARED_RO(whoami), i);
	for(j=0; j<TimeStat::TOTAL_STATES; j++) {
	  double t;
	  fprintf(SSF_USE_SHARED_RO(outfile), "\t%-7.2f", 
		  t=ssf_wall_time_to_sec(SSF_USE_SHARED(paruniv)[i]->stats->elapse[j]));
	  tt[j] += t;
	}
	fprintf(SSF_USE_SHARED_RO(outfile), "\n");
      }
      fprintf(SSF_USE_SHARED_RO(outfile), "[%d]SUM", SSF_USE_SHARED_RO(whoami));
      for(j=0; j<TimeStat::TOTAL_STATES; j++) {
	fprintf(SSF_USE_SHARED_RO(outfile), "\t%-7.2f", tt[j]);
      }
      fprintf(SSF_USE_SHARED_RO(outfile), "\n");
      delete[] tt;
    }
    fflush(0);
    */

    fprintf(SSF_USE_SHARED_RO(outfile), "\n"); fflush(0);
  }


  ssf_barrier();
}

void ssf_universe::partition_channels(ltime_t threshold)
{
  for(SSF_UNIV_LP_SET::iterator lpiter = lpset.begin();
      lpiter != lpset.end(); lpiter++) {
    ssf_logical_process* lp = (*lpiter);

    //lp->pvt_sg->take_messages();
    lp->async_inbound.clear();
    lp->async_outbound.clear();

    for(SSF_LP_SG_VECTOR::iterator iiter = lp->inbound.begin();
	iiter != lp->inbound.end(); iiter++) {
      //(*iiter)->take_messages();
      assert((*iiter)->mbox == 0);
      if((*iiter)->delay < threshold && (*iiter)->src) {
	(*iiter)->in_sync = 0;
	lp->async_inbound.push_back(*iiter);
      } else (*iiter)->in_sync = 1;
    }

    for(SSF_LP_SG_VECTOR::iterator oiter = lp->outbound.begin();
	oiter != lp->outbound.end(); oiter++) {
      if((*oiter)->delay < threshold && (*oiter)->tgt) {
	lp->async_outbound.push_back(*oiter);
      }
    }
  }
}

int ssf_universe::expand_epoch()
{

#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_SYNCHRONIZE_LOCAL);
#endif

#if PRIME_SSF_EMULATION
  wallclock_reftime = ssf_get_wall_time();
  simclock_reftime = 
#endif
  SSF_USE_RTX_PRIVATE(now) = SSF_USE_RTX_PRIVATE(decade);
  /// HEREHERE
  //printf("%d: realnow=%lld: expand_epoch(): now=%lld\n", SSF_USE_SHARED_RO(whoami), wallclock_to_ltime(), SSF_USE_RTX_PRIVATE(now)); fflush(0);

  if(SSF_USE_RTX_PRIVATE(appointment) == SSF_USE_RTX_PRIVATE(decade))
    SSF_USE_RTX_PRIVATE(appointment) = redeem_simulation_barrier();

  // in parallel 
  if(!locked_down) {
    if(SSF_USE_SHARED_RO(synthresh) >= 0) {
      if(SSF_USE_SHARED(num_sorted_edges) == 0) 
	decade_length = SSF_USE_SHARED(epoch_length);
      else {
	for(edge_index=0; edge_index<SSF_USE_SHARED(num_sorted_edges);
	    edge_index++)
	  if(SSF_USE_SHARED_RO(synthresh) <= 
	     SSF_USE_SHARED(sorted_edges)[edge_index]) break;
	if(edge_index >= SSF_USE_SHARED(num_sorted_edges)-1)
	  decade_length = SSF_USE_SHARED(epoch_length);
	else decade_length = SSF_USE_SHARED(sorted_edges)[edge_index];
      }
      partition_channels(decade_length);
      if(!SSF_USE_SHARED_RO(silent) && SSF_SELF==0) {
	printf("[%d] DECADE LENGTH = %g\n", SSF_USE_SHARED_RO(whoami),
	       (double)decade_length);
	fflush(0);
      }
      locked_down = 1;
      settle_binqueue();
    } else {
      if(edge_index < 0 || end_training_time <= SSF_USE_RTX_PRIVATE(now)) {
	if(edge_index < 0) edge_index = 0; // training not started
	else {
	  if(SSF_SELF == 0) { // only p0
	    end_timer = ssf_get_wall_time();
	    double burst_rate = (end_timer-start_timer)/
	      (SSF_USE_RTX_PRIVATE(now)-start_training_time);
	    /*
	    printf("-%d- %g: burst rate [decade=%g] = %g\n", 
		   SSF_USE_SHARED_RO(whoami), SSF_USE_RTX_PRIVATE(now),
		   SSF_USE_SHARED(sorted_edges)[edge_index], burst_rate);
	    */
	    if(best_rate<0 || burst_rate < best_rate) {
	      best_rate = burst_rate;
	      SSF_USE_SHARED(best_edge_index) = edge_index;
	    }
	  }
	  edge_index++;
	}
	if(edge_index < SSF_USE_SHARED(num_sorted_edges)) { // more training
	  start_training_time = SSF_USE_RTX_PRIVATE(now);
	  end_training_time = SSF_USE_RTX_PRIVATE(now)+
	    SSF_USE_SHARED(training_length);
	  /*
	  printf("decade=%g: %g -> %g\n", SSF_USE_SHARED(sorted_edges)[edge_index], 
		 start_training_time, end_training_time);
	  */
	  decade_length = SSF_USE_SHARED(sorted_edges)[edge_index];
	  partition_channels(decade_length);
	  if(SSF_SELF == 0) start_timer = ssf_get_wall_time();
	} else {
	  if(SSF_USE_SHARED(num_sorted_edges) == 0) 
	    decade_length = SSF_USE_SHARED(epoch_length);
	  else {
	    ssf_barrier(); // so that shared value is published
	    if(SSF_USE_SHARED(best_edge_index) == 
	       SSF_USE_SHARED(num_sorted_edges)-1) 
	      decade_length = SSF_USE_SHARED(epoch_length);
	    else decade_length = SSF_USE_SHARED(sorted_edges)
		   [SSF_USE_SHARED(best_edge_index)];
	  }
	  partition_channels(decade_length);
	  if(!SSF_USE_SHARED_RO(silent) && SSF_SELF==0) {
	    fprintf(SSF_USE_SHARED_RO(outfile), "[%d] DECADE LENGTH = %g\n", 
		    SSF_USE_SHARED_RO(whoami), (double)decade_length);
	  }
	  locked_down = 1;
	  settle_binqueue();
	}
      }
    }
  }
  ssf_barrier();

  //int ret = 0;
  if(SSF_USE_RTX_PRIVATE(now) == SSF_USE_RTX_PRIVATE(epoch)) {
    /*ret = */
    ((ssf_teleport*)SSF_USE_SHARED(teleport))->calculate_lbts();
  }

  SSF_USE_RTX_PRIVATE(decade) += decade_length;
  if(SSF_USE_RTX_PRIVATE(decade) > SSF_USE_RTX_PRIVATE(appointment))
    SSF_USE_RTX_PRIVATE(decade) = SSF_USE_RTX_PRIVATE(appointment);
  if(SSF_USE_RTX_PRIVATE(decade) > SSF_USE_RTX_PRIVATE(epoch))
    SSF_USE_RTX_PRIVATE(decade) = SSF_USE_RTX_PRIVATE(epoch);
  /*
  if(SSF_USE_RTX_PRIVATE(decade) > SSF_USE_SHARED(endtime))
    SSF_USE_RTX_PRIVATE(decade) = SSF_USE_SHARED(endtime);
  */

  /// HEREHERE
  /*printf("%d: realnow=%lld: NEW EPOCH: now=%lld, decade=%lld, epoch=%lld, decade-length=%lld, epoch_length=%lld\n", 
	 SSF_USE_SHARED_RO(whoami), wallclock_to_ltime(), SSF_USE_RTX_PRIVATE(now), SSF_USE_RTX_PRIVATE(decade), SSF_USE_RTX_PRIVATE(epoch), 
	 decade_length, SSF_USE_SHARED(epoch_length)); fflush(0);*/

  /*
  int np = (SSF_SELF+1)%SSF_NPROCS;
  ssf_logical_process* migrate_lp = SSF_USE_SHARED(paruniv)[np]->lp_first;
  unlink_lp(migrate_lp);
  link_lp(migrate_lp);
  ssf_barrier();
  */

#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_SENDING_MESSAGES);
#endif

  // redistribute events stored in the bin queue, possibly to other
  // processors.
  ssf_kernel_event* future_event = binque.retrieve_binful_events
    (SSF_USE_RTX_PRIVATE(decade));
  while(future_event) {
    while(future_event) {
      ssf_kernel_event* kevt = future_event;
      future_event = (ssf_kernel_event*)kevt->context;
      kevt->context = 0;

      ssf_logical_process* tl = (ssf_logical_process*)kevt->owner_timeline();
      if(tl) { // if not cancelled
	if(SSF_NPROCS > 1) {
	  int idx = tl->universe->whereami;
	  kevt->context = SSF_USE_SHARED(events_on_fire)[SSF_SELF][idx];
	  SSF_USE_SHARED(events_on_fire)[SSF_SELF][idx] = kevt;
	} else tl->insert_event(kevt, 1);
      }
    }
    future_event = binque.retrieve_binful_events
      (SSF_USE_RTX_PRIVATE(decade));
  }
  ssf_barrier();

  if(SSF_NPROCS > 1) {
    for(int i=0; i<SSF_NPROCS; i++) {
      future_event = SSF_USE_SHARED(events_on_fire)[i][SSF_SELF];
      SSF_USE_SHARED(events_on_fire)[i][SSF_SELF] = 0;
      while(future_event) {
	ssf_kernel_event* kevt = future_event;
	future_event = (ssf_kernel_event*)kevt->context;
	kevt->context = 0;
	ssf_logical_process* tl = (ssf_logical_process*)kevt->owner_timeline();
	assert(tl);
	tl->insert_event(kevt, 1);
      }
    }
  }

#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_OTHER);
#endif
  // make all LPs ready for execution
  //stats->switch_state(TimeStat::STATE_SCHED);
  for(SSF_UNIV_LP_SET::iterator iter = lpset.begin();
      iter != lpset.end(); iter++) {
    (*iter)->make_ready();
    //if(SSF_SELF==0) printf("%g: %d ready", (double)now(), (*iter)->serialno);
  }
  //if(SSF_SELF==0) { printf("\n"); fflush(0); }
  rolling_lps = lpset.size();
  ssf_barrier();
  //stats->switch_state(TimeStat::STATE_SYNC);

  //return ret;
  /*printf("-- expanding epoch: %g --\n", (double)SSF_USE_RTX_PRIVATE(decade));*/
  return SSF_USE_SHARED(endtime) < SSF_USE_RTX_PRIVATE(decade);
}

void ssf_universe::process_epoch()
{
  //stats->switch_state(TimeStat::STATE_SORT);
  /// HEREHERE
  //printf("%d: realnow=%lld: process_epoch() start ----->\n", SSF_USE_SHARED_RO(whoami), wallclock_to_ltime()); fflush(0);
  ssf_logical_process* lp = deque_lp();
  while(lp) {
    //stats->switch_state(TimeStat::STATE_SCAN);
    /// HEREHERE
    //printf("%d: realnow=%lld: process_epoch(): running LP %u\n", SSF_USE_SHARED_RO(whoami), wallclock_to_ltime(), lp->serialno()); fflush(0);
    lp->run();
    //stats->switch_state(TimeStat::STATE_SORT);
    lp = deque_lp();
  }
  /// HEREHERE
  //printf("%d: realnow=%lld: process_epoch() <----- stop\n", SSF_USE_SHARED_RO(whoami), wallclock_to_ltime()); fflush(0);
  //stats->switch_state(TimeStat::STATE_SYNC);
}

void ssf_universe::settle_binqueue()
{
  int slots = int(((double)SSF_USE_SHARED(endtime)-
		   (double)SSF_USE_RTX_PRIVATE(now))/
		  (double)decade_length);
  if(slots < 1) return;
  if(slots > 1024) slots = 1024;
  binque.settle_bins(SSF_USE_RTX_PRIVATE(now), decade_length, slots);
}

void ssf_universe::insert_event(ssf_kernel_event* kevt)
{
  assert(kevt);
  binque.insert_event(kevt);
}

void ssf_universe::cancel_event(ssf_kernel_event* kevt)
{
  assert(kevt);
  kevt->cancel();
}

#if PRIME_SSF_EMULATION
ltime_t ssf_universe::wallclock_to_ltime()
{
  return wallclock_to_ltime(ssf_get_wall_time());
}

ltime_t ssf_universe::wallclock_to_ltime(ssf_machine_time rtime)
{
  double sec = ssf_wall_time_to_sec(rtime-wallclock_reftime)*ltime_wallclock_ratio;
  if(sec+simclock_startime > SSF_LTIME_INFINITY) return SSF_LTIME_INFINITY;
  else return simclock_reftime+ltime_t(sec);
}

struct ssf_external_event {
  int type; // 0=real time, 1=virtual time, 2=throttle
  double ratio; // used by throttle type
  union {
    void (*put_callback)(ltime_t, void*);
    int (*cond_callback)(double, double, ltime_t, ltime_t, void*);
  };
  void* usrdat;
  ssf_kernel_event* next;
};

void ssf_universe::insert_real_time_event(ssf_kernel_event* kevt, void (*cb)(ltime_t, void*),
					  void* usrdat)
{
  assert(emuable);
  assert(kevt && kevt->context == 0);
  ssf_external_event* p = new ssf_external_event;
  p->type = 0;
  p->put_callback = cb;
  p->usrdat = usrdat;
  p->next = 0;
  kevt->context = p;
  ssf_thread_mutex_wait(&emutex);
  if(emuevt) {
    assert(emuevt_tail && emuevt_tail->context);
    ssf_external_event* np = (ssf_external_event*)emuevt_tail->context;
    assert(np->next == 0);
    np->next = emuevt_tail = kevt;
  } else {
    assert(emuevt_tail == 0);
    emuevt = emuevt_tail = kevt;
    ssf_thread_cond_signal(&econd);
  }
  ssf_thread_mutex_signal(&emutex);
}

void ssf_universe::insert_virtual_time_event(ssf_kernel_event* kevt, void (*cb)(ltime_t, void*),
					     void* usrdat)
{
  assert(emuable);
  assert(kevt && kevt->context == 0);
  ssf_external_event* p = new ssf_external_event;
  p->type = 1;
  p->put_callback = cb;
  p->usrdat = usrdat;
  p->next = 0;
  kevt->context = p;
  ssf_thread_mutex_wait(&emutex);
  if(emuevt) {
    assert(emuevt_tail && emuevt_tail->context);
    ssf_external_event* np = (ssf_external_event*)emuevt_tail->context;
    assert(np->next == 0);
    np->next = emuevt_tail = kevt;
  } else {
    assert(emuevt_tail == 0);
    emuevt = emuevt_tail = kevt;
    ssf_thread_cond_signal(&econd);
  }
  ssf_thread_mutex_signal(&emutex);
}

void ssf_universe::throttle(double ratio, int (*cond)
			    (double, double, ltime_t, ltime_t, void*), void* ctxt)
{
  ssf_kernel_event* kevt = new ssf_kernel_event
    (SSF_LTIME_ZERO, ssf_kernel_event::EVTYPE_INCHANNEL, 0);
  ssf_external_event* p = new ssf_external_event;
  p->type = 2;
  p->ratio = ratio;
  p->cond_callback = cond;
  p->usrdat = ctxt;
  p->next = 0;
  kevt->context = p;
  ssf_thread_mutex_wait(&emutex);
  if(emuevt) {
    assert(emuevt_tail && emuevt_tail->context);
    ssf_external_event* np = (ssf_external_event*)emuevt_tail->context;
    assert(np->next == 0);
    np->next = emuevt_tail = kevt;
  } else {
    assert(emuevt_tail == 0);
    emuevt = emuevt_tail = kevt;
    ssf_thread_cond_signal(&econd);
  }
  ssf_thread_mutex_signal(&emutex);
}
#endif /*PRIME_SSF_EMULATION*/

int ssf_universe::interrupt_requested(ltime_t nxtdue)
{
#if PRIME_SSF_EMULATION
  if(emuable) {
    int intr = 0;
    if(throttle_cond) {
      ssf_machine_time wt = ssf_get_wall_time();
      SSF_USE_RTX_PRIVATE(realnow) = wallclock_to_ltime(wt);
      if((*throttle_cond)(ssf_wall_time_to_sec(wt),  ssf_wall_time_to_sec(wallclock_reftime),
			  SSF_USE_RTX_PRIVATE(realnow), simclock_reftime, throttle_context)) {
	ltime_wallclock_ratio = SSF_USE_SHARED(init_ltime_wallclock_ratio);
	wallclock_reftime = wt;
	simclock_reftime = SSF_USE_RTX_PRIVATE(realnow);
	throttle_cond = 0;
	intr = 1;
      }
    }

    struct timespec req;
    SSF_USE_RTX_PRIVATE(realnow) = wallclock_to_ltime();
    double secs = (nxtdue-SSF_USE_RTX_PRIVATE(realnow))/ltime_wallclock_ratio;
    if(secs <= 0.001) secs = 0.001; // 0.1 ms is the minimum duration seems small enough?
    else if(secs > 10) secs = 10; // to avoid overflow
    req.tv_sec = time_t(secs);
    req.tv_nsec = long((secs-req.tv_sec)*1e9);

    ssf_kernel_event* myemuevt = 0;
    ssf_thread_mutex_wait(&emutex);
    ssf_thread_cond_timedwait(&econd, &emutex, &req);
    if(emuevt) {
      myemuevt = emuevt;
      emuevt = emuevt_tail = 0;
    }
    ssf_thread_mutex_signal(&emutex);

    if(myemuevt) {
      ssf_machine_time wt = ssf_get_wall_time();
      SSF_USE_RTX_PRIVATE(realnow) = wallclock_to_ltime(wt);
      while(myemuevt) {
	ssf_kernel_event *kevt = myemuevt;
	ssf_external_event* p = (ssf_external_event*)kevt->context;
	kevt->context = 0;
	myemuevt = p->next;
	assert(kevt->type == ssf_kernel_event::EVTYPE_INCHANNEL);

	switch(p->type) {
	case 0: {
	  kevt->time += SSF_USE_RTX_PRIVATE(realnow);
	  /// HEREHERE
	  //printf("%d: realnow=%lld: interrupt_requested(%lld) got event time=%lld\n", SSF_USE_SHARED_RO(whoami), SSF_USE_RTX_PRIVATE(realnow), nxtdue, kevt->time); fflush(0);
	  if(p->put_callback) (*p->put_callback)(kevt->time, p->usrdat);
	  Entity* ent = kevt->mapnode->ic->_ic_owner;
	  assert(ent->_ent_emuable);
	  ssf_timeline* tmln = ent->_ent_timeline;
	  ent->_ent_insert_event(kevt);
	  if(tmln->on_stage) { // if the timeline is in the ready list
	    rt_trampoline.adjust((ssf_logical_process*)tmln);
	    intr = 1;
	  }
	  break;
	}
	case 1: {
	  if(kevt->time < SSF_USE_RTX_PRIVATE(realnow)) {
	    ssf_throw_warning("inChannel::putVirtualEvent() stale event: t=%g, now=%g",
			      double(kevt->time), double(SSF_USE_RTX_PRIVATE(realnow)));
	    kevt->time = SSF_USE_RTX_PRIVATE(realnow);
	  }
	  if(p->put_callback) (*p->put_callback)(kevt->time, p->usrdat);
	  Entity* ent = kevt->mapnode->ic->_ic_owner;
	  assert(ent->_ent_emuable);
	  ssf_timeline* tmln = ent->_ent_timeline;
	  ent->_ent_insert_event(kevt);
	  if(tmln->on_stage) { // if the timeline is in the ready list
	    rt_trampoline.adjust((ssf_logical_process*)tmln);
	    intr = 1;
	  }
	  break;
	}
	case 2: {
	  if(throttle_cond) {
	    ssf_throw_warning("inChannel::throttle() overlap");
	  } else {
	    throttle_cond = p->cond_callback;
	    throttle_context = p->usrdat;
	    wallclock_reftime = wt;
	    simclock_reftime = SSF_USE_RTX_PRIVATE(realnow);
	    ltime_wallclock_ratio = p->ratio;
	    intr = 1;
	    delete kevt;
	  }
	  break;
	}
	default: assert(0);
	}
	delete p;
      }
    }
   
    if(intr) return intr;
    else if(rt_trampoline.size() > 0) {
      ssf_logical_process* lp = rt_trampoline.top();
      SSF_USE_RTX_PRIVATE(realnow) = wallclock_to_ltime();
      return (lp->next_due_time() <= SSF_USE_RTX_PRIVATE(realnow));
    } else return intr;
  } else return 0;
#else /*PRIME_SSF_EMULATION*/
  return 0;
#endif /*PRIME_SSF_EMULATION*/
}

char* ssf_universe::print_memory_usage(char* buf, prime::ssf::uint32 siz)
{
  static double one_gb = double(1<<30);
  static double one_mb = double(1<<20);
  static double one_kb = double(1<<10);
  if(siz > one_gb) sprintf(buf, "%.1lfGB", siz/one_gb);
  else if(siz > one_mb) sprintf(buf, "%.1lfMB", siz/one_mb);
  else if(siz > one_kb) sprintf(buf, "%.1lfKB", siz/one_kb);
  else sprintf(buf, "%g", double(siz));
  return buf;
}

ltime_t ssf_universe::first_simulation_barrier()
{
  if(!SSF_SELF) {
    ssf_shmem_barrier_appointment* apt = 0;
    while(!((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->empty()) {
      apt = (ssf_shmem_barrier_appointment*)
	((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->top();
      if(apt->handle) break;
      else {
	((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->pop();
	delete apt; apt = 0;
      }
    }
    if(apt) return ssf_min_reduction(apt->time_to_happen);
    else return ssf_min_reduction(SSF_LTIME_INFINITY);
  } else return ssf_min_reduction(SSF_LTIME_INFINITY);
}

ltime_t ssf_universe::redeem_simulation_barrier()
{
  ltime_t tm;
  if(!SSF_SELF) {
    ssf_shmem_barrier_appointment* apt = 0;
    while(!((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->empty()) {
      apt = (ssf_shmem_barrier_appointment*)
	((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->top();
      assert(apt);
      if(apt->handle) {
	SSF_UNIV_INTPTR_MAP::iterator iter = 
	  ((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->find(apt->handle);
	assert(iter != ((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->end());
	if(apt->period > SSF_LTIME_ZERO) {
	  ssf_shmem_barrier_appointment* newapt =
	    new ssf_shmem_barrier_appointment(SSF_USE_PRIVATE(now)+apt->period,
				   apt->handle, apt->callback,
				   apt->data, apt->period);
	  ((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->push(newapt);
	  (*iter).second = newapt;
	} else {
	  ((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->erase(iter);
	}
	break;
      } else {
	((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->pop();
	delete apt; apt = 0;
      }
    }
    if(apt) tm = ssf_min_reduction(apt->time_to_happen);
    else tm = ssf_min_reduction(SSF_LTIME_INFINITY);
  } else tm = ssf_min_reduction(SSF_LTIME_INFINITY);

  if(tm < SSF_LTIME_INFINITY) {
    ssf_shmem_barrier_appointment* apt = (ssf_shmem_barrier_appointment*)
      ((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->top();
    assert(apt && apt->handle);
    (*apt->callback)(apt->handle, SSF_USE_PRIVATE(now), apt->data);
    ssf_barrier();

    if(!SSF_SELF) {
      ((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->pop();
      delete apt; apt = 0;
      while(!((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->empty()) {
	apt = (ssf_shmem_barrier_appointment*)
	  ((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->top();
	if(apt->handle) break;
	else {
	  ((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->pop();
	  delete apt; apt = 0;
	}
      }
      if(apt) return ssf_min_reduction(apt->time_to_happen);
      else return ssf_min_reduction(SSF_LTIME_INFINITY);
    } else return ssf_min_reduction(SSF_LTIME_INFINITY);
  } else return SSF_LTIME_INFINITY;
}

/*#if PRIME_SSF_ENABLE_CHECKPOINTING
extern "C" {
  void ckpt_set_preckpt(void (*f)(void*), void* arg);
  void ckpt_set_postckpt(void (*f)(void*), void* arg);
  void ckpt_set_restart(void (*f)(void*), void* arg);
  int  ckpt_checkpoint(char* ckptdir);
}

void ssf_universe::checkpoint(long handle, ltime_t now, void* data)
{
  printf("checkpointing at %g ...\n", (double)now); fflush(0);
  char core_dir[128];
#if PRIME_SSF_DISTSIM
  sprintf(core_dir, "ckpt-%d@%g",
	  SSF_USE_SHARED_RO(whoami), (double)now);
#else
  sprintf(core_dir, "ckpt@%g", (double)now);
#endif
  struct stat mystat;
  if(stat(core_dir, &mystat)) {
    if(mkdir(core_dir, 0755)) {
      char msg[256];
      sprintf(msg, "can't create checkpoint directory: %s", core_dir);
      ssf_throw_exception(ssf_exception::kernel_ckpt, msg);
  } else {
    if(!(mystat.st_mode & S_IFDIR)) {
      char msg[256];
      sprintf(msg, "%s is not a checkpoint directory", core_dir);
      ssf_throw_exception(ssf_exception::kernel_ckpt, msg);
    }
  }
  ckpt_checkpoint(core_dir);
}
#endif*/

void ssf_universe::progress(long handle, ltime_t now, void* data)
{
  if(SSF_SELF == 0) {
    printf("[%d] advancing simulation time to %g\n",
	   SSF_USE_SHARED_RO(whoami), (double)now);
    fflush(0);
  }
}
#if PRIME_SSF_INSTRUMENT
/**
 * \brief Dumps instrumentation data to the output stream.
 *
 * This includes amount of time spent performing each type of action and, when
 * waiting on input, the amount of time spent waiting on input from each other
 * timeline.
 */
void ssf_universe::dumpInstrumentationData() {
  ssf_machine_time total = 0;
  for (int i=1; i<ssf_universe::ACTION_TOTAL; i++) {
    if (actionCount[i]>0) {
      // timings for event loops are sampled, have to adjust for that
      if (i <= ACTION_EVENT_POSTPROCESSING &&
          i >= ACTION_DETERMINING_NEXT_EVENT) {
        actionTiming[i]*=16;
      }
      fprintf(SSF_USE_SHARED_RO(outfile), "[%d](%d) %-35s:  ", 
	      SSF_USE_SHARED_RO(whoami), whereami, ssf_universe::actionDescriptions[i]);
#if defined(HAVE_GETHRTIME)
      fprintf(SSF_USE_SHARED_RO(outfile), "%10d times (%.3f seconds)\n",
              actionCount[i], actionTiming[i]/1e9);
#else
      fprintf(SSF_USE_SHARED_RO(outfile), "%10d times (%.3f seconds)\n",
              actionCount[i], actionTiming[i]/1e6);
#endif
    }
    total+=actionTiming[i];
      
      // if displaying timing for "synchronizing upstream", break it down
/*      if (i==ACTION_SYNCHRONIZING_UPSTREAM) {
        for (map<prime::ssf::uint32, ssf_machine_time>::iterator iter = upSyncTimes.begin();
             iter != upSyncTimes.end(); iter++) {
          fprintf(SSF_USE_SHARED_RO(outfile), "[%d-%d]        from machine %-30d:  ", 
	          SSF_USE_SHARED_RO(whoami), whereami, (*iter).first);
#if defined(HAVE_GETHRTIME)
          if ((*iter).second>1e9) fprintf(SSF_USE_SHARED_RO(outfile), "%.2f seconds\n",
                                  (*iter).second/1e9);
          else fprintf(SSF_USE_SHARED_RO(outfile), "%.0f nanoseconds\n",
               (*iter).second);
#else
          if ((*iter).second>1e6) fprintf(SSF_USE_SHARED_RO(outfile), "%.2f seconds\n",
                                  (*iter).second/1e6);
          else fprintf(SSF_USE_SHARED_RO(outfile), "%.0f microseconds\n",
               (*iter).second);
#endif
        }
      }
      // if displaying timing for "synchronizing downstream", break it down
      if (i==ACTION_SYNCHRONIZING_DOWNSTREAM) {
        for (map<prime::ssf::uint32, ssf_machine_time>::iterator iter = downSyncTimes.begin();
             iter != downSyncTimes.end(); iter++) {
          fprintf(SSF_USE_SHARED_RO(outfile), "[%d-%d]        from machine %-30d:  ", 
	          SSF_USE_SHARED_RO(whoami), whereami, (*iter).first);
#if defined(HAVE_GETHRTIME)
          if ((*iter).second>1e9) fprintf(SSF_USE_SHARED_RO(outfile), "%.2f seconds\n",
                                  (*iter).second/1e9);
          else fprintf(SSF_USE_SHARED_RO(outfile), "%.0f nanoseconds\n",
               (*iter).second);
#else
          if ((*iter).second>1e6) fprintf(SSF_USE_SHARED_RO(outfile), "%.2f seconds\n",
                                  (*iter).second/1e6);
          else fprintf(SSF_USE_SHARED_RO(outfile), "%.0f microseconds\n",
               (*iter).second);
#endif
        }
      }*/
  }

  for(SSF_UNIV_LP_SET::iterator lpiter = lpset.begin();
      lpiter != lpset.end(); lpiter++) {
    ssf_logical_process* lp = (*lpiter);
    lp->dumpInstrumentationData(whereami);
  }
/*    std::cerr << "    total : \t\t\t";
#if defined(HAVE_GETHRTIME)
  if (total>1e9) std::cerr << (total/1e9) << " seconds";
  else std::cerr << total << " nanoseconds";
#else
  if (total>1e6) std::cerr << (total/1e6) << " seconds";
  else std::cerr << total << " microseconds";
#endif
  std::cerr << std::endl;*/
}
#endif // instrumentation

}; // namespace ssf
}; // namespace prime

/*
 * 
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
