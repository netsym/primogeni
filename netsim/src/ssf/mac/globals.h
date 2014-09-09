/*
 * This file has been automatically generated --- DO NOT EDIT
 */

#ifndef __PRIME_SSF_GLOBALS_H__
#define __PRIME_SSF_GLOBALS_H__

#include <pthread.h>
#include "api/ssftime.h"
#include "sim/ssfmodel.h"
#include "rng.h"
#include "sim/kernelevt.h"
#include <sys/times.h>
#include "mac/machines.h"

namespace prime {
namespace ssf {

  /******* shared data segment *******/

#if !SSF_SHARED_DATA_SEGMENT

  struct shared_segment {
    void** reduction_array;
    int ret;
    ltime_t startime;
    ltime_t endtime;
    ssf_mutex environ_mutex;
    void* environ;
    void** paruniv;
    ssf_mutex report_mutex;
    void* teleport;
    ltime_t epoch_length;
    void** unique_edges;
    ltime_t* sorted_edges;
    int num_sorted_edges;
    int best_edge_index;
    ltime_t training_length;
    void* root_lp;
    void** entity_list;
    void* entlist;
    ssf_mutex entlist_mutex;
    ssf_mutex logiproc_mutex;
    int unique_entity_id;
    int unique_logiproc_id;
    ssf_kernel_event*** events_on_fire;
    ssf_mutex align_mutex;
    void* align_req_head;
    void* align_req_tail;
    ssf_mutex icmap_mutex;
    void* local_icmap;
    ssf_mutex map_mutex;
    void* map_req_head;
    void* map_req_tail;
    struct tms clock_us;
    double sum_utime;
    prime::ssf::uint32 total_lps;
    prime::ssf::uint32 total_ent;
    prime::ssf::uint32 total_evt;
    prime::ssf::uint32 total_msg;
    prime::ssf::uint32 total_msg_xprc;
    prime::ssf::uint32 total_msg_xmem;
    prime::ssf::uint32 total_tcs;
    prime::ssf::uint32 total_pcs;
    prime::ssf::uint32 total_wmk;
    double max_exec;
    double init_ltime_wallclock_ratio;
    void* objhash;
    ssf_mutex objhash_mutex;
    ssf_mutex apt_mutex;
    long apt_handle;
    void* apt_hash;
    void* apt_queue;
    SSF_CACHE_LINE_PADDING;
  };

#endif /*!SSF_SHARED_DATA_SEGMENT*/

  extern void** ssf_shared_reduction_array;
  extern int ssf_shared_ret;
  extern ltime_t ssf_shared_startime;
  extern ltime_t ssf_shared_endtime;
  extern ssf_mutex ssf_shared_environ_mutex;
  extern void* ssf_shared_environ;
  extern void** ssf_shared_paruniv;
  extern ssf_mutex ssf_shared_report_mutex;
  extern void* ssf_shared_teleport;
  extern ltime_t ssf_shared_epoch_length;
  extern void** ssf_shared_unique_edges;
  extern ltime_t* ssf_shared_sorted_edges;
  extern int ssf_shared_num_sorted_edges;
  extern int ssf_shared_best_edge_index;
  extern ltime_t ssf_shared_training_length;
  extern void* ssf_shared_root_lp;
  extern void** ssf_shared_entity_list;
  extern void* ssf_shared_entlist;
  extern ssf_mutex ssf_shared_entlist_mutex;
  extern ssf_mutex ssf_shared_logiproc_mutex;
  extern int ssf_shared_unique_entity_id;
  extern int ssf_shared_unique_logiproc_id;
  extern ssf_kernel_event*** ssf_shared_events_on_fire;
  extern ssf_mutex ssf_shared_align_mutex;
  extern void* ssf_shared_align_req_head;
  extern void* ssf_shared_align_req_tail;
  extern ssf_mutex ssf_shared_icmap_mutex;
  extern void* ssf_shared_local_icmap;
  extern ssf_mutex ssf_shared_map_mutex;
  extern void* ssf_shared_map_req_head;
  extern void* ssf_shared_map_req_tail;
  extern struct tms ssf_shared_clock_us;
  extern double ssf_shared_sum_utime;
  extern prime::ssf::uint32 ssf_shared_total_lps;
  extern prime::ssf::uint32 ssf_shared_total_ent;
  extern prime::ssf::uint32 ssf_shared_total_evt;
  extern prime::ssf::uint32 ssf_shared_total_msg;
  extern prime::ssf::uint32 ssf_shared_total_msg_xprc;
  extern prime::ssf::uint32 ssf_shared_total_msg_xmem;
  extern prime::ssf::uint32 ssf_shared_total_tcs;
  extern prime::ssf::uint32 ssf_shared_total_pcs;
  extern prime::ssf::uint32 ssf_shared_total_wmk;
  extern double ssf_shared_max_exec;
  extern double ssf_shared_init_ltime_wallclock_ratio;
  extern void* ssf_shared_objhash;
  extern ssf_mutex ssf_shared_objhash_mutex;
  extern ssf_mutex ssf_shared_apt_mutex;
  extern long ssf_shared_apt_handle;
  extern void* ssf_shared_apt_hash;
  extern void* ssf_shared_apt_queue;

  /******* private data segment *******/

#if SSF_SHARED_DATA_SEGMENT

  struct private_segment {
    void* my_reduction;
    char* qmem_chunklist;
    char* qmem_poolbegin;
    char* qmem_poolend;
    void** qmem_freelist;
    size_t qmem_objsize;
    void* kevt_pool;
    long event_watermark;
    int self;
    int simphase;
    ltime_t decade;
    ltime_t now;
    void* processing_timeline;
    void* running_process;
    ltime_t flowdelta;
    long kevt_watermark;
    int datafile;
    prime::ssf::uint32 timeline_contexts;
    prime::ssf::uint32 process_contexts;
    prime::ssf::uint32 kernel_events;
    prime::ssf::uint32 kernel_messages;
    prime::ssf::uint32 kernel_messages_xprc;
    prime::ssf::uint32 kernel_messages_xmem;
    prime::ssf::uint32 direct_callbacks;
    void* universe;
    prime::rng::Random* krng;
    double execution_time;
    ltime_t epoch;
    double utime;
    double stime;
    ltime_t appointment;
    ltime_t realnow;
    SSF_CACHE_LINE_PADDING;
  };

#endif /*SSF_SHARED_DATA_SEGMENT*/

  extern void* ssf_private_my_reduction;
  extern char* ssf_private_qmem_chunklist;
  extern char* ssf_private_qmem_poolbegin;
  extern char* ssf_private_qmem_poolend;
  extern void** ssf_private_qmem_freelist;
  extern size_t ssf_private_qmem_objsize;
  extern void* ssf_private_kevt_pool;
  extern long ssf_private_event_watermark;
  extern int ssf_private_self;
  extern int ssf_private_simphase;
  extern ltime_t ssf_private_decade;
  extern ltime_t ssf_private_now;
  extern void* ssf_private_processing_timeline;
  extern void* ssf_private_running_process;
  extern ltime_t ssf_private_flowdelta;
  extern long ssf_private_kevt_watermark;
  extern int ssf_private_datafile;
  extern prime::ssf::uint32 ssf_private_timeline_contexts;
  extern prime::ssf::uint32 ssf_private_process_contexts;
  extern prime::ssf::uint32 ssf_private_kernel_events;
  extern prime::ssf::uint32 ssf_private_kernel_messages;
  extern prime::ssf::uint32 ssf_private_kernel_messages_xprc;
  extern prime::ssf::uint32 ssf_private_kernel_messages_xmem;
  extern prime::ssf::uint32 ssf_private_direct_callbacks;
  extern void* ssf_private_universe;
  extern prime::rng::Random* ssf_private_krng;
  extern double ssf_private_execution_time;
  extern ltime_t ssf_private_epoch;
  extern double ssf_private_utime;
  extern double ssf_private_stime;
  extern ltime_t ssf_private_appointment;
  extern ltime_t ssf_private_realnow;

  /******* shared read-only data segment *******/

  extern char* ssf_shared_ro_backstore;
  extern pthread_key_t ssf_shared_ro_private_segment;
  extern int ssf_shared_ro_spinlock;
  extern int ssf_shared_ro_heap;
  extern ssf_dml_model* ssf_shared_ro_submodel;
  extern int* ssf_shared_ro_mach_nprocs;
  extern char** ssf_shared_ro_mach_names;
  extern int ssf_shared_ro_nmachs;
  extern int ssf_shared_ro_whoami;
  extern int ssf_shared_ro_diverse;
  extern int ssf_shared_ro_seed;
  extern int ssf_shared_ro_silent;
  extern FILE* ssf_shared_ro_outfile;
  extern ltime_t ssf_shared_ro_flowdelta;
  extern long ssf_shared_ro_flowmark;
  extern ltime_t ssf_shared_ro_synthresh;
  extern ltime_t ssf_shared_ro_ckpt;
  extern ltime_t ssf_shared_ro_progress;
  extern int ssf_shared_ro_argc;
  extern char** ssf_shared_ro_argv;
  extern const char* ssf_shared_ro_version;
  extern char* ssf_shared_ro_copyinfo;
  extern char* ssf_shared_ro_showcfg;
  extern int ssf_shared_ro_max_thresh_edges;
  extern int ssf_shared_ro_nprocs;

#define PRIME_SSF_CONFIG "-I/home/obaida/Desktop/pgc2vega/primex/netsim/src -I/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssf -I/home/obaida/Desktop/pgc2vega/primex/netsim/src -I/home/obaida/Desktop/pgc2vega/primex/netsim/src/rng -I/home/obaida/Desktop/pgc2vega/primex/netsim/src -I/home/obaida/Desktop/pgc2vega/primex/netsim/src/dml -DPRIME_SSF_ARCH_GENERIC=1 -DPRIME_SSF_MACH_X86_LINUX=1 -DPRIME_SSF_DISTSIM=0 -DPRIME_SSF_LTIME_LONGLONG -DPRIME_SSF_DEBUG=0 -DPRIME_SSF_SCRUTINY=0 -DPRIME_SSF_EMULATION=1 -DPRIME_SSF_QUICKMEM=1 -DPRIME_SSF_ARENA=0 -DPRIME_SSF_HOARD=0 -DPRIME_VERSION=\"x1.0\" -DPRIME_SSF_BACKWARD_COMPATIBILITY=1 -DPRIME_SSF_MANAGE_KERNEL_EVENTS=1 -DPRIME_SSF_SHARED_MANAGEMENT=0 -DPRIME_SSF_INSTRUMENT=0 -DPRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS=0 -DPRIME_SSF_HOMOGENEOUS=0 -DPRIME_SSF_ENABLE_WARNING=1 -DPRIME_SSF_REPORT_USER_TIMING=1 -DPRIME_SSF_EVTLIST_BINHEAP -DSSFNET_EMULATION=1 -DPRIME_RNG_DEBUG=0 -DPRIME_RNG_SPRNG=1 -DPRIME_RNG_BOOST=0 -DPRIME_DML_DEBUG=0 -DPRIME_DML_LOCINFO=1 -g -O2"


}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_GLOBALS_H__*/
