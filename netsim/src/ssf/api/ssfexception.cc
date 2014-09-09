/*
 * ssfexception :- exception handler.
 */

#include <stdio.h>
#include <stdarg.h>
#include <string.h>
#include "api/ssfexception.h"

namespace prime {
namespace ssf {

ssf_exception::ssf_exception(int c, const char* s) : code(c) {
  explanation[0] = 0; strcat(explanation, "ERROR: ");
  switch(code) {
  case no_exception: strcat(explanation, "uninitialized exception"); break;
  case other_exception: break;

  case ltime_size: strcat(explanation, "unconventional ltime_t size"); break;
  case ltime_nullbuf: strcat(explanation, "ssf_ltime_deserialize: null buffer"); break;

  case compact_convloss: strcat(explanation, "ssf_compact: type conversion causing data loss"); break;
  case compact_mpibuf: strcat(explanation, "ssf_compact: packing data larger than MPI buffer size"); break;
  case compact_mismatch: strcat(explanation, "ssf_compact: mismatch packing data"); break;
  case compact_fops: strcat(explanation, "ssf_compact: file operation failed"); break;
  case compact_mpiops: strcat(explanation, "ssf_compact: MPI operation failed"); break;

  case entity_nullent: strcat(explanation, "prime::ssf::Entity: alignto() null entity"); break;
  case entity_align: strcat(explanation, "prime::ssf::Entity: misalignment"); break;
  case entity_proctxt: strcat(explanation, "prime::ssf::Entity: outside procedure context"); break;
  case entity_dumpdata: strcat(explanation, "prime::ssf::Entity: dumping data"); break;
  case entity_register: strcat(explanation, "prime::ssf::Entity: SSF_REGISTER_ENTITY"); break;
  case entity_startall: strcat(explanation, "prime::ssf::Entity: startAll()"); break;
  case entity_joinall: strcat(explanation, "prime::ssf::Entity: joinAll() called before startAll()"); break;
  case entity_publish: strcat(explanation, "prime::ssf::Entity: publishing channel"); break;
  //case entity_throttle: strcat(explanation, "prime::ssf::Entity: throttle()"); break;
    
  case event_sysevt: strcat(explanation, "Event: deleting an event owned by the system"); break;
  case event_alias: strcat(explanation, "Event: deleting an aliased event"); break;
  case event_release: strcat(explanation, "Event: unmatched call to the release method"); break;
  case event_reference: strcat(explanation, "Event: illegal reference to an event submitted to system"); break;
  case event_regname: strcat(explanation, "Event: missing event name for SSF_REGISTER_EVENT"); break;
  case event_regcall: strcat(explanation, "Event: missing factory method for SSF_REGISTER_EVENT"); break;
  case event_regdup: strcat(explanation, "Event: duplicate event class for SSF_REGISTER_EVENT"); break;

  case proc_align: strcat(explanation, "Process: timeline misalignment"); break;
  case proc_multiwait: strcat(explanation, "Process: multiple wait statements in a simple process"); break;
  case proc_pcall: strcat(explanation, "Process: improper procedure call"); break;
  case proc_simple: strcat(explanation, "Process: simple procedure invoked by non-simple process"); break;
  case proc_owner: strcat(explanation, "Process: null owner pointer"); break;
  case proc_nowait: strcat(explanation, "Process: missing wait statement in a simple process"); break;
  case proc_aligncall: strcat(explanation, "Process: procedure invocation misalignment"); break;

  case ic_owner: strcat(explanation, "prime::ssf::inChannel: null owner pointer"); break;
  case ic_proctxt: strcat(explanation, "prime::ssf::inChannel: activeEvents() called outside procedure context"); break;
  case ic_align: strcat(explanation, "prime::ssf::inChannel: activeEvents() misalignment"); break;
  case ic_pubreal: strcat(explanation, "prime::ssf::inChannel: publish real-time in-channel"); break;
  case ic_duppub: strcat(explanation, "prime::ssf::inChannel: duplicate in-channel publishing "); break;
  case ic_putreal: strcat(explanation, "prime::ssf::inChannel: putRealEvent()"); break;
  case ic_putvirt: strcat(explanation, "prime::ssf::inChannel: putVirtualEvent()"); break;

  case oc_owner: strcat(explanation, "prime::ssf::outChannel: null owner pointer"); break;
  case oc_delay: strcat(explanation, "prime::ssf::outChannel: negative delay"); break;
  case oc_align: strcat(explanation, "prime::ssf::outChannel: misalignment"); break;
  case oc_real: strcat(explanation, "prime::ssf::outChannel: no real-time support"); break;
  case oc_noreal: strcat(explanation, "prime::ssf::outChannel: getRealEvent() on non-real-time out-channel"); break;
  case oc_pubreal: strcat(explanation, "prime::ssf::outChannel: publish real-time out-channel"); break;
  case oc_duppub: strcat(explanation, "prime::ssf::outChannel: duplicate out-channel publishing "); break;

  case timer_entity: strcat(explanation, "ssf_timer: null entity pointer"); break;
  case timer_callback: strcat(explanation, "ssf_timer: null callback function pointer"); break;
  case timer_running: strcat(explanation, "ssf_timer: deleting running timer"); break;
  case timer_delay:  strcat(explanation, "ssf_timer: negative delay"); break;
  case timer_align: strcat(explanation, "ssf_timer: entity misalignment"); break;
  case timer_resched: strcat(explanation, "ssf_timer: cannot schedule running timer, use reschedule"); break;

  case sem_delete: strcat(explanation, "ssf_semaphore: deleting a semaphore with blocked processes"); break;
  case sem_inproc: strcat(explanation, "ssf_semaphore: semaphore must be used within an SSF process"); break;
  case sem_align: strcat(explanation, "ssf_semaphore: semaphore must be used within the same timeline"); break;

  case globalobj: strcat(explanation, "ssf_set_global_object: duplicate global object name"); break;

  case cmdline_badarg: strcat(explanation, "command line: bad argument"); break;
  case cmdline_diverse: strcat(explanation, "command line: assumed homogeneous; must reconfigure to allow diverse platforms"); break;
  case cmdline_nmach: strcat(explanation, "command line: number of machines inconsistent between command-line and MPI context"); break;
  case cmdline_rank: strcat(explanation, "command line: machine rank inconsistent between command-line and MPI context"); break;
  case cmdline_async: strcat(explanation, "command line: SSF MPI implementation does not support -async"); break;
  case cmdline_submodel: strcat(explanation, "command line: failed to read the DML model"); break;
  case cmdline_modnmach: strcat(explanation, "command line: number of machines inconsistent with the DML model"); break;
  case cmdline_modnproc: strcat(explanation, "command line: number of processors inconsistent between command-line and DML model"); break;
  case cmdline_nodist: strcat(explanation, "command line: assumed standalone; must reconfigure to allow distributed simulation"); break;
  case cmdline_windist: strcat(explanation, "command line: windows platform does not support distributed simulation"); break;
  case cmdline_output: strcat(explanation, "command line: simulation output"); break;

  case kernel_heapmem: strcat(explanation, "ssf kernel: memory overflow: increase heap size"); break;
  case kernel_sdata: strcat(explanation, "ssf kernel: can't map shared data segment"); break;
  case kernel_pdata: strcat(explanation, "ssf kernel: can't map private data segment"); break;
  case kernel_sseg: strcat(explanation, "ssf kernel: can't create shared segment"); break;
  case kernel_forklimit: strcat(explanation, "ssf kernel: number of child processes exceeds limit"); break;
  case kernel_fork: strcat(explanation, "ssf kernel: failed to create child processes"); break;
  case kernel_join: strcat(explanation, "ssf kernel: failed to join child processes"); break;
  case kernel_spawn: strcat(explanation, "ssf kernel: failed to spawn thread"); break;
  case kernel_binheap: strcat(explanation, "ssf kernel: prime::ssf::ssf_binary_heap"); break;
  case kernel_splay: strcat(explanation, "ssf kernel: prime::ssf::ssf_splay_tree"); break;
  case kernel_disapt: strcat(explanation, "ssf kernel: appointment channel unsupported for distributed simulation"); break;
  case kernel_ssfmodel: strcat(explanation, "ssf kernel: parsing DML model"); break;
  case kernel_mapch: strcat(explanation, "ssf kernel: channel mapping"); break;
  case kernel_nojoin: strcat(explanation, "ssf kernel: missing Entity::joinAll() in main thread"); break;
  case kernel_mkmodel: strcat(explanation, "ssf kernel: building simulation model"); break;
  case kernel_dumpdata: strcat(explanation, "ssf kernel: dumping data"); break;
  case kernel_rtqempty: strcat(explanation, "ssf kernel: ssf_realtime_queue empty"); break;
  case kernel_event: strcat(explanation, "ssf kernel: ssf_kernel_event"); break;
  case kernel_xdelay: strcat(explanation, "ssf kernel: cross-timeline channels must have positive delays"); break;
  case kernel_mpi: strcat(explanation, "ssf kenrel: mpi call"); break;
  case kernel_unpack: strcat(explanation, "ssf kenrel: failed to unpack msg"); break;
  case kernel_lbts: strcat(explanation, "ssf kenrel: failed to initiate LBTS computation"); break;
  case kernel_newevt: strcat(explanation, "ssf kenrel: failed to recognize user event"); break;
  case kernel_pack: strcat(explanation, "ssf kenrel: failed to pack msg"); break;

  default: strcat(explanation, "programming error");
  }
  if(s) { strcat(explanation, ": "); strcat(explanation, s); } // possible overflow
}

void ssf_throw_warning(const char* s, ...) {
#ifdef PRIME_SSF_ENABLE_WARNING
  va_list ap;
  va_start(ap, s);
  fprintf(stderr, "WARNING: ");
  vfprintf(stderr, s, ap);
  fprintf(stderr, "\n");
  va_end(ap);
#endif
}

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
