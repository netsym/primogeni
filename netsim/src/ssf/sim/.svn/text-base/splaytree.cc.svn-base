/*
 * splaytree :- priority queue implemented as a splay tree.
 *
 * Splay tree, or self-adjusting search tree, is a simple but
 * efficient data structure for storing an ordered set. The key
 * feature of this data structure---basically a binary tree---is that
 * it allows insertion, delete-min, and many other operations, all
 * with amortized logarithmic performance. We made an important change
 * to this data structure in the way how simultaneous events are
 * handled. In particular, events with the same timestamp value as the
 * current simulation clock (which is expected to be the timestamp of
 * the last event retrieved from this data structure) are store
 * separately in a doubled ended linked list. Since these events
 * frequently happen in simulation, we can cut the cost of insertion
 * and deletion of these events to a mere constant cost. In order to
 * allow such optimization, we require events that are newly inserted
 * into the data structure to have no smaller timestamp value than the
 * events that have been retrieved earlier.
 */

#include <assert.h>
#include <stdio.h>

#include "api/ssfexception.h"
#include "sim/splaytree.h"

namespace prime {
namespace ssf {

/* The following macros are used internally. */

#define UP(t)    ((t)->parent)
#define UPUP(t)  ((t)->parent->parent)
#define LEFT(t)  ((t)->left)
#define RIGHT(t) ((t)->right)

#define ROTATE_R(n,p,g) \
  if((LEFT(p) = RIGHT(n))) UP(RIGHT(n)) = p; \
  RIGHT(n) = p; UP(n) = g;  UP(p) = n;

#define ROTATE_L(n,p,g) \
  if((RIGHT(p) = LEFT(n))) UP(LEFT(n)) = p; \
  LEFT(n) = p; UP(n) = g;  UP(p) = n;

ssf_splay_tree::ssf_splay_tree() :
  _spt_nevts(0), _spt_root(0), _spt_min(0)
#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  , _spt_now(SSF_LTIME_MINUS_INFINITY)
#endif
{}

ssf_splay_tree::~ssf_splay_tree()
{ 
  clear();
}

ssf_kernel_event* ssf_splay_tree::top()
{
  if(empty())
    ssf_throw_exception(ssf_exception::kernel_splay, "top() empty splay tree");

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(!_spt_nowevts.empty())
    return _spt_nowevts.front();
#endif

  assert(_spt_min);
  return _spt_min->kevt;
}

void ssf_splay_tree::pop()
{
  if(empty())
    ssf_throw_exception(ssf_exception::kernel_splay, "pop() empty splay tree");

  _spt_nevts--;

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(!_spt_nowevts.empty()) {
    _spt_nowevts.pop_front();
    // now the user's responsible for the event, and we don't need to
    // update _spt_now.
    return;
  }
#endif

  assert(_spt_root && _spt_min);
  ssf_kernel_event* kevt = _spt_remove_min();
  assert(kevt);

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  _spt_now = kevt->time;
  // we need to download all simulataneous events into double-ended
  // linked list so that newly added events are actually added after
  // these events.
  while(_spt_min) {
    assert(_spt_min->kevt);
    if(_spt_min->kevt->time == _spt_now) {
      ssf_kernel_event* kevt = _spt_remove_min();
      assert(kevt);
      _spt_nowevts.push_back(kevt);
    } else break;
  }
#endif
}

void ssf_splay_tree::push(ssf_kernel_event* kevt)
{
  assert(kevt && !kevt->context);
  _spt_nevts++;

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(kevt->time < _spt_now) {
    char msg[256];
    sprintf(msg, "push() earlier event (%g) than last retrieved (%g)",
	    (double)kevt->time, (double)_bh_now);
    ssf_throw_exception(ssf_exception::kernel_splay, msg);
  }
  if(kevt->time == _spt_now) {
    _spt_nowevts.push_back(kevt);
    return;
  }
#endif

  _spt_insert_node(kevt);
}

void ssf_splay_tree::clear()
{
  _spt_nevts = 0;
#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  if(_spt_nowevts.size() > 0) {
    for(SSF_DEQUE(ssf_kernel_event*)::iterator iter = _spt_nowevts.begin();
	iter != _spt_nowevts.end(); iter++)
      delete (*iter);
    _spt_nowevts.clear();
  }
  _spt_now = SSF_LTIME_MINUS_INFINITY;
#endif
  if(_spt_root) _spt_reclaim_tree(_spt_root);
  _spt_root = _spt_min = 0;
}

void ssf_splay_tree::remove(ssf_kernel_event* kevt)
{
  assert(kevt);

  if(empty())
    ssf_throw_exception(ssf_exception::kernel_splay, "remove() empty splay tree");

  _spt_nevts--;
  if(kevt->context) { // if the event is in the tree
    _spt_remove_node(kevt);
    return;
  }

#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  _spt_nowevts.remove(kevt);
#else
  assert(0); // events should be all in the tree!
#endif
}

void ssf_splay_tree::_spt_insert_node(ssf_kernel_event* kevt)
{
  assert(kevt && !kevt->context);
  ssf_splay_node* e = new ssf_splay_node(kevt);

  ssf_splay_node* n = _spt_root;
  RIGHT(e) = LEFT(e) = 0;
  if(n) {
    for(;;) {
      assert(n->kevt && e->kevt);
      if(n->kevt->compare_with(e->kevt) <= 0) {
	if(RIGHT(n)) n = RIGHT(n);
	else {
	  RIGHT(n) = e;
	  UP(e) = n;
	  break;
	}
      } else {
	if(LEFT(n)) n = LEFT(n);
	else {
	  if(_spt_min == n) _spt_min = e;
	  LEFT(n) = e; 
	  UP(e) = n;
	  break;
	}
      }
    }
    _spt_splay(e);
    _spt_root = e;
  } else {
    _spt_root = _spt_min = e;
    UP(e) = 0;
  }
}

ssf_kernel_event* ssf_splay_tree::_spt_remove_node(ssf_kernel_event* kevt)
{
  assert(kevt && kevt->context);
  ssf_splay_node* r = (ssf_splay_node*)kevt->context;

  if(r == _spt_min) return _spt_remove_min();

  ssf_splay_node* n; ssf_splay_node* p;
  if((n = LEFT(r))) {
    ssf_splay_node* tmp;
    if((tmp = RIGHT(r))) {
      UP(n) = 0;
      for(; RIGHT(n); n = RIGHT(n));
      _spt_splay(n);
      RIGHT(n) = tmp;
      UP(tmp) = n;
    }
    UP(n) = UP(r);
  } else if((n = RIGHT(r))) {
    UP(n) = UP(r);
  }
  if((p = UP(r))) {
    if(r == LEFT(p)) LEFT(p) = n;
    else RIGHT(p) = n;
    if(n) {
      _spt_splay(p);
      _spt_root = p;
    }
  } else _spt_root = n;

  kevt = r->kevt;
  delete r;
  return kevt;
}

ssf_kernel_event* ssf_splay_tree::_spt_remove_min()
{
  assert(_spt_root && _spt_min);

  ssf_splay_node *r = _spt_min;
  ssf_splay_node *tmp, *p;
  if((p = UP(_spt_min))) {
    if((tmp = RIGHT(_spt_min))) {
      LEFT(p) = tmp;
      UP(tmp) = p;
      for(; LEFT(tmp); tmp = LEFT(tmp));
      _spt_min = tmp;
    } else {
      _spt_min = UP(_spt_min);
      LEFT(_spt_min) = 0;
    }
  } else {
    if((_spt_root = RIGHT(_spt_min))) {
      UP(_spt_root) = 0;
      for(tmp = _spt_root; LEFT(tmp); tmp = LEFT(tmp));
      _spt_min = tmp;
    } else _spt_min = 0;
  }

  ssf_kernel_event* kevt = r->kevt;
  delete r;
  return kevt;
}

void ssf_splay_tree::_spt_splay(ssf_splay_node* n)
{
  register ssf_splay_node *g, *p, *x, *z;

  for(;(p = UP(n));) {
    if(n == LEFT(p)) {
      if(!((g = UPUP(n)))) {
	ROTATE_R(n,p,g);
      } else if(p == LEFT(g)) {
	if((z = UP(g))) {
	  if(g == LEFT(z))
	    LEFT(z) = n;
	  else
	    RIGHT(z) = n;
	}
	UP(n) = z;
	if((x = LEFT(p) = RIGHT(n)))
	  UP(x) = p;
	RIGHT(n) = p;
	UP(p) = n;
	if((x = LEFT(g) = RIGHT(p)))
	  UP(x) = g;
	RIGHT(p) = g;
	UP(g) = p;
      } else {
	if((z = UP(g))) {
	  if(g == LEFT(z))
	    LEFT(z) = n;
	  else
	    RIGHT(z) = n;
	}
	UP(n) = z;
	if((x = LEFT(p) = RIGHT(n)))
	  RIGHT(n) = UP(x) = p;
	else
	  RIGHT(n) = p;
	if((x = RIGHT(g) = LEFT(n)))
	  LEFT(n) = UP(x) = g;
	else
	  LEFT(n) = g;
	UP(g) = UP(p) = n;
      }
    } else {
      if(!((g = UPUP(n)))) {
	ROTATE_L(n,p,g);
      } else if(p == RIGHT(g)) {
	if((z = UP(g))) {
	  if(g == RIGHT(z))
	    RIGHT(z) = n;
	  else
	    LEFT(z) = n;
	}
	UP(n) = z;
	if((x = RIGHT(p) = LEFT(n)))
	  UP(x) = p;
	LEFT(n) = p;
	UP(p) = n;
	if((x = RIGHT(g) = LEFT(p)))
	  UP(x) = g;
	LEFT(p) = g;
	UP(g) = p;
      } else {
	if((z = UP(g))) {
	  if(g == RIGHT(z))
	    RIGHT(z) = n;
	  else
	    LEFT(z) = n;
	}
	UP(n) = z;
	if((x = RIGHT(p) = LEFT(n)))
	  LEFT(n) = UP(x) = p;
	else
	  LEFT(n) = p;
	if((x = LEFT(g) = RIGHT(n)))
	  RIGHT(n) = UP(x) = g;
	else
	  RIGHT(n) = g;
	UP(g) = UP(p) = n;
      }
    }
  }
}

void ssf_splay_tree::_spt_reclaim_tree(ssf_splay_node* n)
{
  ssf_splay_node *l = LEFT(n), *r = RIGHT(n);
  ssf_kernel_event* kevt = n->kevt; 
  delete n;
  delete kevt;
  if(l) _spt_reclaim_tree(l);
  if(r) _spt_reclaim_tree(r);
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
