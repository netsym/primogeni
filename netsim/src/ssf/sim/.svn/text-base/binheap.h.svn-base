/*
 * binheap :- priority queue implemented as a binary heap.
 *
 * There are differences between this implementation and a standard
 * implementation of a binary heap: i) we limit access to only a small
 * core set of methods for simplicity; ii) optionally, we can expedite
 * the handling of events with the same timestamps as the current
 * simulation clock (expected to be the timestamp of the last event
 * retrieved from this data structure); doing so we must make sure
 * that events that are newly inserted into the heap have no smaller
 * timestamp value than the events that have been retrieved earlier;
 * iii) kernel events are augmented with an index to the heap array
 * for faster removal; here, we use the "context pointer" to point to
 * the element in the heap array; the index of the element can be
 * inferred from the offset of the pointer to the start of the array.
 */

#ifndef __PRIME_SSF_BINHEAP_H__
#define __PRIME_SSF_BINHEAP_H__

#include "api/ssftype.h"
#include "api/ssftime.h"
#include "sim/kernelevt.h"

namespace prime {
namespace ssf {

class ssf_binary_heap {
 public:
  /*
   * Constructor. Allocate an array of the default size for the heap;
   * the array will grow if the space becomes tight. The user can
   * decide whether to include the optimization of caching
   * simultaneous events in a double-ended queue (using the macro
   * PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS at compilation time). Doing so
   * we would introduce the risk of generating unrepeatable simulation
   * results if simultaneous events are not dealt with carefullly.
   */
  ssf_binary_heap();

  // Destructor.
  virtual ~ssf_binary_heap();

  // Returns the total number of events stored in the data structure.
  int size();
  
  // Returns true (non-zero) if there's no more event left.
  int empty() { return size() == 0; }

  /*
   * Returns the event with the highest prioirty (i.e., with the
   * smallest timestamp). In order to conform to the standard priority
   * queue template interface, we make sure it reports an error if
   * this method is called when the heap is empty.
   */
  ssf_kernel_event* top();

  /*
   * Remove the event with with the smallest timestamp. It'll report
   * an error if this method is called when the heap is empty.
   */
  void pop();

  // Insert a new event into the heap.
  void push(ssf_kernel_event* kevt);

  /*
   * Remove an event from the heap. The event must be already in the
   * heap when the method is called.
   */
  void remove(ssf_kernel_event* kevt);

  // Remove all events from the heap.
  void clear();

 protected:
  // Size of the array; will increase when all bins are occupied.
  int _bh_capacity;

  // Number of bins in the array have been utilized.
  int _bh_nitems;

  // The heap is an array of pointers to kernel events.
  ssf_kernel_event** _bh_heap;

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  /*
   * Remember the timestamp of the last event retrieved. Used only
   * when the optimization of cache simultaneous events is turned on.
   */
  ltime_t _bh_now;

  /*
   * This is a double-ended list that contains newly inserted events
   * with the same timestamp as the one retrieved last time. Used only
   * when the optimization of cache simultaneous events is turned on.
   */
  SSF_DEQUE(ssf_kernel_event*) _bh_nowevts;
#endif

  // Insert a node into the heap tree.
  void _bh_insert_node(ssf_kernel_event* kevt);

  // Remove the event at the given index out of the heap tree.
  ssf_kernel_event* _bh_remove_node(int index);

  // Adjust the node of given index towards heap tree root.
  int _bh_adjust_upheap(int index);

  // Adjust the node of given index towards the leaves.
  int _bh_adjust_downheap(int index);

}; /*ssf_binary_heap*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_BINHEAP_H__*/

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
