/*
 * eventlist :- data structure for event list.
 *
 * An event list is implemented as a priority queue. One can choose
 * from three alternatives: a binary heap, a splay tree, or a priority
 * queue implemented in the standard template library.
 */

#ifndef __PRIME_SSF_EVENTLIST_H__
#define __PRIME_SSF_EVENTLIST_H__

#include <queue>
#include <functional>

#include "api/ssftype.h"
#include "sim/binheap.h"
#include "sim/splaytree.h"
#include "sim/kernelevt.h"

#if !defined(PRIME_SSF_EVTLIST_BINHEAP) && !defined(PRIME_SSF_EVTLIST_SPLAYTREE) && !defined(PRIME_SSF_EVTLIST_PRIOQUE)
#define PRIME_SSF_EVTLIST_SPLAYTREE
#endif

namespace prime {
namespace ssf {

#if defined(PRIME_SSF_EVTLIST_BINHEAP)

// Use binary heap as the event list type.
typedef ssf_binary_heap ssf_event_list;

#elif defined(PRIME_SSF_EVTLIST_SPLAYTREE)

// Use splay tree as the event list type.
typedef ssf_splay_tree ssf_event_list;

#else

// Use default priority queue from STL as the event list type. In
// this case, we need to define the comparison function (the less
// predicate) for it to sort events in the heap.
struct ssf_less_kernel_event : public SSF_LESS(ssf_kernel_event*) {
  bool operator()(ssf_kernel_event* t1, ssf_kernel_event* t2) {
    return t1->time > t2->time; // smaller timestamp means higher priority
  }
};

typedef SSF_PRIORITY_QUEUE
  (ssf_kernel_event*, SSF_VECTOR(ssf_kernel_event*), ssf_less_kernel_event)
  ssf_priority_queue;

class ssf_event_list : public ssf_priority_queue {
 public:
  // add one method that's been missing in STL priority_queue.
  void remove(ssf_kernel_event* kevt) { kevt->cancel(); }
};

#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_EVENTLIST_H__*/

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
