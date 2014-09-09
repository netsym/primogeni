/*
 * binque :- bin queue, a calendar queue tailored for composite
 *           synchronization.
 *
 * There is one bin queue for each processor for storing future
 * events. The size of the bins is the same as the decade length,
 * which is the threshold used by composite synchronization algorithm
 * to partition cross-timeline channels into synchronous and
 * asynchronous channels.
 */

#ifndef __PRIME_SSF_BINQUE_H__
#define __PRIME_SSF_BINQUE_H__

#ifndef __PRIME_SSF_COMPOSITE_H__
#error "binque.h must be included by composite.h"
#endif

namespace prime {
namespace ssf {

  class ssf_bin_queue : public ssf_permanent_object {
  public:
    // constructor and destructor.
    ssf_bin_queue();
    ~ssf_bin_queue();

    // inserting events into and retrieving events from this data
    // structure.
    void insert_event(ssf_kernel_event* evt);
    ssf_kernel_event* retrieve_binful_events(ltime_t upper_time);

    // since composite synchronization involves a training period at
    // the beginning of the simulation, we don't create all the bins
    // until we know the size of the bins is fixed.
    void settle_bins(ltime_t startime, ltime_t binsize, int nbins);

  private:
    int settled; // is the bin size fixed by now?
    ltime_t startime; // used to calculate the bin index of an event
    ltime_t binsize; // size of the bin
    int nbins; // number of bins for the calendar queue

    ssf_kernel_event* big_bin; // used to hold events before the queue is settled
    ssf_kernel_event** bin_array; // the calendar queue
    ssf_splay_tree *splay; // events far into the future is stored here

    int curbin; // the current bin for processing
    ltime_t loweredge; // lower window edge of the current bin
  }; /*ssf_bin_queue*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_BINQUE_H__*/

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

/*
 * $Id$
 */
