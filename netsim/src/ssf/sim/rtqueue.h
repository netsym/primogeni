/*
 * rtqueue :- priority queue for real-time logical processes.
 *
 * We use a binary heap to implement this priority queue. We sort the
 * logical processes in the queue according to the next due time,
 * which is the minimum between the safe time (calculated from the
 * input channels) and the timestamp of the next event. It is possible
 * that the next due time of a logical process suddenly changes,
 * because of the arrival of an external real-time event.
 */

#ifndef __PRIME_SSF_RTQUEUE_H__
#define __PRIME_SSF_RTQUEUE_H__

#if PRIME_SSF_EMULATION

#include "api/ssftime.h"

namespace prime {
namespace ssf {

class ssf_logical_process;

class ssf_realtime_queue {
 public:
  // Constructor.
  ssf_realtime_queue();

  // Destructor.
  virtual ~ssf_realtime_queue();

  // Returns the total number of events stored in the data structure.
  int size();
  
  // Returns true (non-zero) if there's no more event left.
  int empty() { return size() == 0; }

  /*
   * Returns the logical process with the highest prioirty (i.e., with
   * the smallest next due time). In order to conform to the standard
   * priority queue template interface, we make sure it reports an
   * error if this method is called when the heap is empty.
   */
  ssf_logical_process* top();

  /*
   * Remove the logical process with with the smallest
   * timestamp. It'll report an error if this method is called when
   * the heap is empty.
   */
  void pop();

  // Insert a new logical process into the heap.
  void push(ssf_logical_process* lp);

  /*
   * Remove a logical process from the heap. The LP must be already in
   * the heap when the method is called.
   */
  void remove(ssf_logical_process* lp);

  // Remove all logical processes from the heap.
  void clear();

  // Adjust the position of the logical process in the queue,
  // assuming its next due time has been changed.
  void adjust(ssf_logical_process*);

  // Update the simulation time of all logical processes in the queue
  // to the wall-clock time.
  int update(ltime_t wct);

 protected:
  // Size of the array; will increase when all bins are occupied.
  int _rtq_capacity;

  // Number of bins in the array have been utilized.
  int _rtq_nitems;

  // The heap is an array of pointers to logical processes.
  ssf_logical_process** _rtq_heap;

  // Insert a node into the heap tree.
  void _rtq_insert_node(ssf_logical_process* lp);

  // Remove the logical process at the given index out of the heap tree.
  ssf_logical_process* _rtq_remove_node(int index);

  // Adjust the node of given index towards heap tree root.
  int _rtq_adjust_upheap(int index);

  // Adjust the node of given index towards the leaves.
  int _rtq_adjust_downheap(int index);

}; /*ssf_realtime_queue*/

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_EMULATION*/

#endif /*__PRIME_SSF_RTQUEUE_H__*/

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
