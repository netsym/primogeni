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

#include <assert.h>
#include <stdio.h>
#include <string.h>

#include "api/ssfexception.h"
#include "api/ssfqobj.h"
#include "sim/binheap.h"


namespace prime {
namespace ssf {

#define SSF_BINHEAP_STARTING_ARRAY_SIZE 64

ssf_binary_heap::ssf_binary_heap() :
  _bh_capacity(SSF_BINHEAP_STARTING_ARRAY_SIZE),
  _bh_nitems(0)
#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  , _bh_now(SSF_LTIME_MINUS_INFINITY)
#endif
{
  _bh_heap = (ssf_kernel_event**)ssf_quickobj::quick_new
    (_bh_capacity*sizeof(ssf_kernel_event*));
}

ssf_binary_heap::~ssf_binary_heap()
{
  clear();
  ssf_quickobj::quick_delete
    (_bh_heap, _bh_capacity*sizeof(ssf_kernel_event*));
}

int ssf_binary_heap::size()
{
#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  return _bh_nitems+_bh_nowevts.size();
#else
  return _bh_nitems;
#endif
}


ssf_kernel_event* ssf_binary_heap::top()
{
  if(empty()) 
    ssf_throw_exception(ssf_exception::kernel_binheap, "top() empty heap");

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(!_bh_nowevts.empty())
    return _bh_nowevts.front();
#endif

  assert(_bh_nitems > 0);
  return _bh_heap[1];
}

void ssf_binary_heap::pop()
{
  if(empty())
    ssf_throw_exception(ssf_exception::kernel_binheap, "pop() empty heap");

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(!_bh_nowevts.empty()) {
    _bh_nowevts.pop_front();
    // now the user's responsible for the event, and we don't need to
    // update _bh_now.
    return;
  }
#endif

  assert(_bh_nitems > 0);
  ssf_kernel_event* kevt = _bh_remove_node(1);
  assert(kevt);

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  _bh_now = kevt->time;
  // we need to download all simulataneous events into double-ended
  // linked list so that newly added events are actually added after
  // these events.
  while(_bh_nitems > 0) {
    assert(_bh_heap[1]);
    if(_bh_heap[1]->time == _bh_now) {
      _bh_nowevts.push_back(_bh_remove_node(1));
    } else break;
  }
#endif
}

void ssf_binary_heap::push(ssf_kernel_event* kevt)
{
  assert(kevt && !kevt->context);

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(kevt->time < _bh_now) {
    char msg[256];
    sprintf(msg, "push() earlier event (%g) than last retrieved (%g)",
	    (double)kevt->time, (double)_bh_now);
    ssf_throw_exception(ssf_exception::kernel_binheap, msg);
  }
  if(kevt->time == _bh_now) {
    _bh_nowevts.push_back(kevt);
    return;
  }
#endif

  _bh_insert_node(kevt);
}

void ssf_binary_heap::clear()
{
#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(_bh_nowevts.size() > 0) {
    for(SSF_DEQUE(ssf_kernel_event*)::iterator iter = _bh_nowevts.begin();
	iter != _bh_nowevts.end(); iter++)
      delete (*iter);
    _bh_nowevts.clear();
  }
  _bh_now = SSF_LTIME_MINUS_INFINITY;
#endif

  for(int i=1; i<=_bh_nitems; i++) {
    _bh_heap[i]->context = 0;
    delete _bh_heap[i];
  }
  _bh_nitems = 0;
}

void ssf_binary_heap::remove(ssf_kernel_event* kevt)
{
  assert(kevt);

  if(empty())
    ssf_throw_exception(ssf_exception::kernel_binheap, "remove() empty heap");

  if(kevt->context) { // if the event is in the heap
    int index = ((ssf_kernel_event**)kevt->context)-_bh_heap;
    assert(0 < index && index <= _bh_nitems && 
	   _bh_heap[index] == kevt);
    _bh_remove_node(index);
    return;
  }

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  _bh_nowevts.remove(kevt);
#else
  assert(0); // events should be all in the heap array!
#endif
}

void ssf_binary_heap::_bh_insert_node(ssf_kernel_event* kevt)
{
  assert(kevt && !kevt->context);
  _bh_nitems++;

  // expand the size of the heap if no more room left
  if(_bh_nitems == _bh_capacity) {
    ssf_kernel_event** newheap = (ssf_kernel_event**)ssf_quickobj::quick_new
      ((_bh_capacity<<1)*sizeof(ssf_kernel_event*));
    memcpy(newheap, _bh_heap, _bh_capacity*sizeof(ssf_kernel_event*));
    for(int i=1; i<_bh_nitems; i++) newheap[i]->context = &newheap[i];
    ssf_quickobj::quick_delete(_bh_heap, _bh_capacity*sizeof(ssf_kernel_event*));
    _bh_heap = newheap;
    _bh_capacity <<= 1;
  }

  _bh_heap[_bh_nitems] = kevt;
  kevt->context = &_bh_heap[_bh_nitems];

  _bh_adjust_upheap(_bh_nitems);
}

ssf_kernel_event* ssf_binary_heap::_bh_remove_node(int index)
{
  assert(0 < index && index <= _bh_nitems);
  ssf_kernel_event* kevt = _bh_heap[index];
  _bh_nitems--;

  // plug the hole with the last item, if there is one
  if(index <= _bh_nitems) {
    _bh_heap[index] = _bh_heap[_bh_nitems+1];
    _bh_heap[index]->context = &_bh_heap[index];

    // move it in either direction
    if(index>1 && _bh_heap[index]->compare_with
       (_bh_heap[index>>1]) < 0)
      _bh_adjust_upheap(index);
    else _bh_adjust_downheap(index);
  }

  kevt->context = 0;
  return kevt;
}

int ssf_binary_heap::_bh_adjust_upheap(int index)
{
  register int p = (index>>1);
  while(p && _bh_heap[index]->compare_with(_bh_heap[p]) < 0) {
    ssf_kernel_event *node = _bh_heap[p];
    _bh_heap[p] = _bh_heap[index];
    _bh_heap[p]->context = &_bh_heap[p];
    _bh_heap[index] = node;
    node->context = &_bh_heap[index];
    index = p;
    p = (index>>1);
  }
  return index;
}

int ssf_binary_heap::_bh_adjust_downheap(int index)
{
  for(;;) {
    int left = index<<1;
    if(left>_bh_nitems) break;
    int right = left+1;
    int minside = left;
    if(right <= _bh_nitems && 
       _bh_heap[right]->compare_with(_bh_heap[left]) < 0)
      minside = right;
    if(_bh_heap[minside]->compare_with(_bh_heap[index]) < 0) {
      ssf_kernel_event *node = _bh_heap[index];
      _bh_heap[index] = _bh_heap[minside];
      _bh_heap[index]->context = &_bh_heap[index];
      _bh_heap[minside] = node;
      node->context = &_bh_heap[minside];
      index = minside;
    } else break;
  }
  return index;
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
