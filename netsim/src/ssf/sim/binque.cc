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

#include <math.h>
#include <string.h>

#include "sim/composite.h"

namespace prime {
namespace ssf {

ssf_bin_queue::ssf_bin_queue() : 
  settled(0), big_bin(0) {}

ssf_bin_queue::~ssf_bin_queue()
{
  // no events in the big bin
  //assert(big_bin == 0);
  if(settled) {
    // no events in the calendar queue (hope so...)
    // for(int i=0; i<nbins; i++) { assert(bin_array[i] == 0); }
    delete[] bin_array;
    // no events in the splay tree
    //assert(splay->size() == 0);
    delete splay;
  }
}

void ssf_bin_queue::insert_event(ssf_kernel_event* evt)
{
  assert(evt);
  if(settled) {
    ltime_t key = evt->time;
    if(key < loweredge+binsize*nbins) { // not too far into the future
      int n;
      if(key < loweredge+binsize) n = curbin;
      else {
#if defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
	// floating point operations, if ltime_t is a floating point number
	n = (int)fmod((double)floor(double(key-startime)/binsize),
		      (double)nbins);
#else
	// fixed point operations, much simpler
	n = (key-startime)/binsize%nbins;
#endif
      }
      assert(n >= 0);
      assert(!evt->context);
      evt->context = bin_array[n];
      bin_array[n] = evt;
    } else {
      splay->push(evt);
    }
  } else { // far into the future
    assert(!evt->context);
    evt->context = big_bin;
    big_bin = evt;
  }
}

ssf_kernel_event* ssf_bin_queue::retrieve_binful_events(ltime_t upper_time)
{
  ssf_kernel_event* evtlist = 0;
  if(settled) {
    while(loweredge < upper_time+binsize) { // the current bin is to be retrieved
      evtlist = bin_array[curbin];
      bin_array[curbin] = 0;
      if(evtlist == 0) { // should move to the next bin-ful of events
	curbin = (curbin+1)%nbins;
	loweredge += binsize; // could introduce cumulative errors
      } else break;
    }

    // look for events on splay tree as well
    while(splay->size() > 0) {
      ssf_kernel_event* evt = splay->top();
      if(evt->time < upper_time+binsize) {
	splay->pop();
	evt->context = evtlist;
	evtlist = evt;
      } else break;
    }
  } else {
    evtlist = big_bin;
    big_bin = 0;
  }
  return evtlist;
}

void ssf_bin_queue::settle_bins(ltime_t st, ltime_t bs, int nb)
{
  assert(!settled); // don't settle it twice
  settled  = 1;
  startime = st;
  binsize = bs;
  nbins = nb;
  
  bin_array = new ssf_kernel_event*[nb];
  memset(bin_array, 0, nb*sizeof(ssf_kernel_event*));
  splay = new ssf_splay_tree;

  curbin = 0;
  loweredge = startime;

  while(big_bin) {
    ssf_kernel_event* kevt = big_bin;
    big_bin = (ssf_kernel_event*)kevt->context;
    kevt->context = 0; // for sanity checking
    insert_event(kevt);
  }
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
