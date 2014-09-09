/**
 * rtqueue :- priority queue for real-time logical processes.
 *
 * We use a binary heap to implement this priority queue. We sort the
 * logical processes in the queue according to the next due time,
 * which is the minimum between the safe time (calculated from the
 * input channels) and the timestamp of the next event. It is possible
 * that the next due time of a logical process suddenly changes,
 * because of the arrival of an external real-time event.
 */

#if PRIME_SSF_EMULATION

#include <assert.h>
#include <string.h>

#include "api/ssfqobj.h"
#include "api/ssfexception.h"
#include "sim/rtqueue.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

#define PRIME_SSF_RTQUEUE_STARTING_ARRAY_SIZE 64

ssf_realtime_queue::ssf_realtime_queue() :
  _rtq_capacity(PRIME_SSF_RTQUEUE_STARTING_ARRAY_SIZE),
  _rtq_nitems(0)
{
  _rtq_heap = (ssf_logical_process**)ssf_quickobj::quick_new
    (_rtq_capacity*sizeof(ssf_logical_process*));
}

ssf_realtime_queue::~ssf_realtime_queue()
{
  clear();
  ssf_quickobj::quick_delete
    (_rtq_heap, _rtq_capacity*sizeof(ssf_logical_process*));
}

int ssf_realtime_queue::size()
{
  return _rtq_nitems;
}

ssf_logical_process* ssf_realtime_queue::top()
{
  if(empty()) ssf_throw_exception(ssf_exception::kernel_rtqempty, "top()");

  assert(_rtq_nitems > 0);
  return _rtq_heap[1];
}

void ssf_realtime_queue::pop()
{
  if(empty()) ssf_throw_exception(ssf_exception::kernel_rtqempty, "pop()");

  assert(_rtq_nitems > 0);
  ssf_logical_process* lp = _rtq_remove_node(1);
  assert(lp);
}

void ssf_realtime_queue::push(ssf_logical_process* lp)
{
  assert(lp && !lp->rtidx);
  _rtq_insert_node(lp);
}

void ssf_realtime_queue::clear()
{
  for(int i=1; i<=_rtq_nitems; i++) {
    _rtq_heap[i]->rtidx = 0;
    delete _rtq_heap[i];
  }
  _rtq_nitems = 0;
}

void ssf_realtime_queue::remove(ssf_logical_process* lp)
{
  assert(lp);

  if(empty()) ssf_throw_exception(ssf_exception::kernel_rtqempty, "remove()");

  if(lp->rtidx > 0) { // if the event is in the heap
    assert(0 < lp->rtidx && lp->rtidx <= _rtq_nitems && 
	   _rtq_heap[lp->rtidx] == lp);
    _rtq_remove_node(lp->rtidx);
  }
}

void ssf_realtime_queue::adjust(ssf_logical_process* lp)
{
  assert(lp && lp->rtidx > 0);
  _rtq_adjust_downheap(lp->rtidx); // only gets smaller
}

int ssf_realtime_queue::update(ltime_t wct)
{
  int yes = 0;
  for(int i=1; i<=_rtq_nitems; i++) {
    yes += _rtq_heap[i]->update_next_due_time(wct);
  }
  return yes;
}

void ssf_realtime_queue::_rtq_insert_node(ssf_logical_process* lp)
{
  assert(lp && !lp->rtidx);
  _rtq_nitems++;

  // expand the size of the heap if no more room left
  if(_rtq_nitems == _rtq_capacity) {
    ssf_logical_process** newheap = (ssf_logical_process**)ssf_quickobj::quick_new
      ((_rtq_capacity<<1)*sizeof(ssf_logical_process*));
    memcpy(newheap, _rtq_heap, _rtq_capacity*sizeof(ssf_logical_process*));
    ssf_quickobj::quick_delete(_rtq_heap, _rtq_capacity*sizeof(ssf_logical_process*));
    _rtq_heap = newheap;
    _rtq_capacity <<= 1;
  }

  _rtq_heap[_rtq_nitems] = lp;
  lp->rtidx = _rtq_nitems;

  _rtq_adjust_upheap(_rtq_nitems);
}

ssf_logical_process* ssf_realtime_queue::_rtq_remove_node(int index)
{
  assert(0 < index && index <= _rtq_nitems);
  ssf_logical_process* lp = _rtq_heap[index];
  _rtq_nitems--;

  // plug the hole with the last item, if there is one
  if(index <= _rtq_nitems) {
    _rtq_heap[index] = _rtq_heap[_rtq_nitems+1];
    _rtq_heap[index]->rtidx = index;

    // move it in either direction
    if(index>1 && _rtq_heap[index]->next_due_time() < 
       _rtq_heap[index>>1]->next_due_time())
      _rtq_adjust_upheap(index);
    else _rtq_adjust_downheap(index);
  }

  lp->rtidx = 0;
  return lp;
}

int ssf_realtime_queue::_rtq_adjust_upheap(int index)
{
  register int p = (index>>1);
  while(p && _rtq_heap[index]->next_due_time() < 
	_rtq_heap[p]->next_due_time()) {
    ssf_logical_process *node = _rtq_heap[p];
    _rtq_heap[p] = _rtq_heap[index];
    _rtq_heap[p]->rtidx = p;
    _rtq_heap[index] = node;
    node->rtidx = index;
    index = p;
    p = (index>>1);
  }
  return index;
}

int ssf_realtime_queue::_rtq_adjust_downheap(int index)
{
  for(;;) {
    int left = index<<1;
    if(left>_rtq_nitems) break;
    int right = left+1;
    int minside = left;
    if(right <= _rtq_nitems && 
       _rtq_heap[right]->next_due_time() < 
       _rtq_heap[left]->next_due_time())
      minside = right;
    if(_rtq_heap[minside]->next_due_time() < 
       _rtq_heap[index]->next_due_time()) {
      ssf_logical_process *node = _rtq_heap[index];
      _rtq_heap[index] = _rtq_heap[minside];
      _rtq_heap[index]->rtidx = index;
      _rtq_heap[minside] = node;
      node->rtidx = minside;
      index = minside;
    } else break;
  }
  return index;
}

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_EMULATION*/

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

