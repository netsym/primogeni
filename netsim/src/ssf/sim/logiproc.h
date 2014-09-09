/*
 * logiproc :- definition of a logical process.
 */

#ifndef __PRIME_SSF_LOGIPROC_H__
#define __PRIME_SSF_LOGIPROC_H__

#ifndef __PRIME_SSF_COMPOSITE_H__
#error "logiproc.h must be included by composite.h"
#endif

#include "sim/timeline.h"

namespace prime {
namespace ssf {

  typedef SSF_QVECTOR(ssf_stargate*) SSF_LP_SG_VECTOR;

  /*
   * Each logical process (LP) contains a separate event list that can
   * be distributed to a different processor or even a different
   * machine in a parallel simulation environment.
   */
  class ssf_logical_process : public ssf_timeline {
  public:
    // Points to the universe (i.e., processor) to which this logical
    // process belongs.
    ssf_universe* universe;

#if PRIME_SSF_EMULATION
    // Used by emulation: index to the ssf_realtime_queue (rt_trampoline).
    int rtidx;
#endif

    // Incoming stargates through which this logical process receives
    // events from its predecessor logical processes.
    SSF_LP_SG_VECTOR inbound;

    // Outgoing stargates: events generated from this logical process
    // can be sent to the successor logical processes.
    SSF_LP_SG_VECTOR outbound;

    // A subset of incoming stargates, identified as asynchronous
    // channels by the composite synchronization algorithm.
    SSF_LP_SG_VECTOR async_inbound;

    // A subset of outgoing stargates, identified as asynchronous
    // channels by the composite synchronization algorithm.
    SSF_LP_SG_VECTOR async_outbound;

    /*
    int time_again;
    unsigned long event_load;
    */

    SSF_CACHE_LINE_PADDING;

    // The constructors and the destructor.
    ssf_logical_process(prime::ssf::uint32 serialno);
    ssf_logical_process();
    virtual ~ssf_logical_process();

    void make_ready();

    static void map_local_local(outChannel*, inChannel*, ltime_t);
#if PRIME_SSF_DISTSIM
    static void map_local_remote(outChannel*, int, ltime_t);
    static void map_remote_local(int, int, int, inChannel*, ltime_t);
#endif

    void run();

#if PRIME_SSF_EMULATION
    /* This method is used only if the logical process is marked as a
    real-time LP. It is used to update the simulation clock, making it
    keep up with the wall-clock time. The method returns true if there
    is an event due for processing. */
    int update_next_due_time(ltime_t realnow);
#endif

  }; /*ssf_logical_process*/

  // Use default priority queue from STL to sort logical
  // processes. All we need to do is to define the comparison
  // function (the less predicate) for it to sort events in the heap.
  class ssf_less_logical_process : public SSF_LESS(ssf_logical_process*) {
  public:
    bool operator()(ssf_logical_process* t1, ssf_logical_process* t2) {
      return t1->simclock > t2->simclock; // smallest time first
    }
  };

  typedef SSF_PRIORITY_QUEUE
    (ssf_logical_process*, SSF_QVECTOR(ssf_logical_process*),
     ssf_less_logical_process) ssf_lp_queue;

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_LOGIPROC_H__*/

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
