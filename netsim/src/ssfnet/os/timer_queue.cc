/**
 * \file timer_queue.cc
 * \brief Source file for the TimerQueue class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
 * 
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

#include <assert.h>
#include <stdio.h>

#include "os/logger.h"
#include "os/timer_queue.h"
#include "os/protocol_session.h"
#include "os/community.h"
#include "net/host.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT_REF(Timer);

#ifndef SSFNET_TIMERQUEUE_MULTIMAP
// these are internally-defined classes for sorting timer queue data
class TimerQueueSplayNode : public prime::ssf::ssf_quickobj {
 public:
  TimerQueueData* tqd;
  TimerQueueSplayNode* parent;
  TimerQueueSplayNode* left;
  TimerQueueSplayNode* right;

  TimerQueueSplayNode(TimerQueueData* ke) :
    tqd(ke), parent(0), left(0), right(0) {
      assert(tqd);
      tqd->context = this;
    }

  ~TimerQueueSplayNode() { assert(tqd); tqd->context = 0; }
};

class TimerQueueSplayTree {
 public:
  TimerQueueSplayTree();
  virtual ~TimerQueueSplayTree();

  int size() { return nevts; }
  int empty() { return size() == 0; }
  TimerQueueData* top();
  void pop();
  void push(TimerQueueData* tqd);
  void remove(TimerQueueData* tqd);
  void clear();

  int nevts;
  TimerQueueSplayNode* root;
  TimerQueueSplayNode* min;
  void insert_node(TimerQueueData* tqd);
  TimerQueueData* remove_node(TimerQueueData* tqd);
  TimerQueueData* remove_min();
  void reclaim_tree(TimerQueueSplayNode* node);
  void splay(TimerQueueSplayNode* node);
};

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

TimerQueueSplayTree::TimerQueueSplayTree() : nevts(0), root(0), min(0) {}

TimerQueueSplayTree::~TimerQueueSplayTree() { clear(); }

TimerQueueData* TimerQueueSplayTree::top() {
  assert(!empty() && min);
  return min->tqd;
}

void TimerQueueSplayTree::pop() {
  assert(!empty());
  nevts--;
  assert(root && min);
  TimerQueueData* tqd = remove_min();
  assert(tqd);
}

void TimerQueueSplayTree::push(TimerQueueData* tqd) {
  assert(tqd && !tqd->context);
  nevts++;
  insert_node(tqd);
}

void TimerQueueSplayTree::clear() {
  nevts = 0;
  if(root) reclaim_tree(root);
  root = min = 0;
}

void TimerQueueSplayTree::remove(TimerQueueData* tqd) {
  assert(tqd && !empty());
  nevts--;
  if(tqd->context) { // if the event is in the tree
    remove_node(tqd);
    return;
  }
  assert(0); // events should be all in the tree!
}

void TimerQueueSplayTree::insert_node(TimerQueueData* tqd) {
  assert(tqd && !tqd->context);
  TimerQueueSplayNode* e = new TimerQueueSplayNode(tqd);
  TimerQueueSplayNode* n = root;
  RIGHT(e) = LEFT(e) = 0;
  if(n) {
    for(;;) {
      assert(n->tqd && e->tqd);
      if(n->tqd->compare_with(e->tqd) <= 0) {
	if(RIGHT(n)) n = RIGHT(n);
	else {
	  RIGHT(n) = e;
	  UP(e) = n;
	  break;
	}
      } else {
	if(LEFT(n)) n = LEFT(n);
	else {
	  if(min == n) min = e;
	  LEFT(n) = e;
	  UP(e) = n;
	  break;
	}
      }
    }
    splay(e);
    root = e;
  } else {
    root = min = e;
    UP(e) = 0;
  }
}

TimerQueueData* TimerQueueSplayTree::remove_node(TimerQueueData* tqd) {
  assert(tqd && tqd->context);
  TimerQueueSplayNode* r = (TimerQueueSplayNode*)tqd->context;
  if(r == min) return remove_min();
  TimerQueueSplayNode* n; TimerQueueSplayNode* p;
  if((n = LEFT(r))) {
    TimerQueueSplayNode* tmp;
    if((tmp = RIGHT(r))) {
      UP(n) = 0;
      for(; RIGHT(n); n = RIGHT(n));
      splay(n);
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
      splay(p);
      root = p;
    }
  } else root = n;
  tqd = r->tqd;
  delete r;
  return tqd;
}

TimerQueueData* TimerQueueSplayTree::remove_min() {
  assert(root && min);
  TimerQueueSplayNode *r = min;
  TimerQueueSplayNode *tmp, *p;
  if((p = UP(min))) {
    if((tmp = RIGHT(min))) {
      LEFT(p) = tmp;
      UP(tmp) = p;
      for(; LEFT(tmp); tmp = LEFT(tmp));
      min = tmp;
    } else {
      min = UP(min);
      LEFT(min) = 0;
    }
  } else {
    if((root = RIGHT(min))) {
      UP(root) = 0;
      for(tmp = root; LEFT(tmp); tmp = LEFT(tmp));
      min = tmp;
    } else min = 0;
  }
  TimerQueueData* tqd = r->tqd;
  delete r;
  return tqd;
}

void TimerQueueSplayTree::splay(TimerQueueSplayNode* n) {
  register TimerQueueSplayNode *g, *p, *x, *z;
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

void TimerQueueSplayTree::reclaim_tree(TimerQueueSplayNode* n) {
  TimerQueueSplayNode *l = LEFT(n), *r = RIGHT(n);
  TimerQueueData* tqd = n->tqd;
  delete n;
  delete tqd;
  if(l) reclaim_tree(l);
  if(r) reclaim_tree(r);
}
#endif /*SSFNET_TIMERQUEUE_MULTIMAP*/

void TimerQueueData::setTime(VirtualTime t) {
  if(!timer_set) my_time = t;
  else LOG_ERROR("attempt to set timer in use.");
}

void TimerQueueData::addTime(VirtualTime t) {
  if(!timer_set) my_time += t;
  else LOG_ERROR("attempt to set timer in use.");
}

TimerQueueData::~TimerQueueData()
{
  if(timer_set) LOG_ERROR("attempt to reclaim a running timer queue data.\n");
}

void TimerQueue::timer_callback()
{
  if(is_sorted) {
    TDQueue* tqueue = (TDQueue*)queue;
    if (tqueue->empty()) LOG_ERROR("timeout with an empty queue.");

    TDQIterator begin_iter = tqueue->begin(), end_iter = tqueue->end();
    VirtualTime cur_time = (*begin_iter)->my_time;
    assert(cur_time == VirtualTime(comm->now()));

    // execute all available callbacks of the same time
    TDQIterator iter = begin_iter;
    while(iter != end_iter && cur_time == (*iter)->my_time) {
      TimerQueueData* cur_data = (*iter); assert(cur_data);

      tqueue->pop_front(); // remove it from the queue
      cur_data->timer_set = false; // turn off the switch

      // callback on current data (it is expected that the current data
      // should be reclaimed by the callback function)
      assert(!in_callback);
      in_callback = true; // must disable all timer ops invoked by user
      callback(cur_data);
      in_callback = false;

      iter = tqueue->begin();
    }

    if(!tqueue->empty()) {
      assert(qtimer->isCancelled());
      qtimer->set((*iter)->my_time - cur_time);
    }
  } else {
#ifdef SSFNET_TIMERQUEUE_MULTIMAP
    TMQueue* tqueue = (TMQueue*)queue;
    if (tqueue->empty()) LOG_ERROR("timeout with an empty queue.");

    TMQIterator begin_iter = tqueue->begin(), end_iter = tqueue->end();
    VirtualTime cur_time = begin_iter->first;
    assert(cur_time == begin_iter->second->my_time);
    assert(cur_time == VirtualTime(comm->now()));

    // execute all available callbacks of the same time
    TMQIterator iter = begin_iter;
    while (iter != end_iter && cur_time == iter->first) {
      TimerQueueData* cur_data = iter->second; assert(cur_data);

      tqueue->erase(iter); // remove it from the queue
      cur_data->timer_set = false; // turn off the switch

      // callback on current data (it is expected that the current data
      // should be reclaimed by the callback function)
      assert(!in_callback);
      in_callback = true; // must disable all timer ops invoked by user
      callback(cur_data);
      in_callback = false;

      iter = tqueue->begin();
    }

    // schedule the next timer
    if(!tqueue->empty()) {
      assert(qtimer->isCancelled());
      qtimer->set(iter->first - cur_time);
    }
#else
    TimerQueueSplayTree* splay = (TimerQueueSplayTree*)queue;
    if (splay->empty()) LOG_ERROR("timeout with an empty queue.");

    VirtualTime cur_time = splay->top()->my_time;
    while(!splay->empty() && cur_time == splay->top()->my_time) {
      TimerQueueData* cur_data = splay->top(); assert(cur_data);
      splay->pop();
      cur_data->timer_set = false;
      assert(!in_callback);
      in_callback = true;
      callback(cur_data);
      in_callback = false;
    }
    if(!splay->empty()) {
      assert(qtimer->isCancelled());
      qtimer->set(splay->top()->my_time - cur_time);
    }
#endif
  }
}

TimerQueue::TimerQueue(ProtocolSession *psess, bool sorted) :
  is_sorted(sorted), in_callback(false),
  comm(psess ? psess->inHost()->getCommunity() : 0), max_time(0)
{
  if(is_sorted) queue = new TDQueue;
  else {
#ifdef SSFNET_TIMERQUEUE_MULTIMAP
    queue = new TMQueue;
#else
    queue = new TimerQueueSplayTree;
#endif
  }
  assert(queue);

  qtimer = new QueueTimer(psess, this);
  assert(qtimer);
}

TimerQueue::TimerQueue(Community* pcomm, bool sorted) :
  is_sorted(sorted), in_callback(false), comm(pcomm), max_time(0)
{
  if(is_sorted) queue = new TDQueue;
  else {
#ifdef SSFNET_TIMERQUEUE_MULTIMAP
    queue = new TMQueue;
#else
    queue = new TimerQueueSplayTree;
#endif
  }
  assert(queue);

  qtimer = new QueueTimer(pcomm, this);
  assert(qtimer);
}

TimerQueue::~TimerQueue()
{
  clearQueue(true); // reclaim remaining data in queue

  if(is_sorted) delete (TDQueue*)queue;
  else {
#ifdef SSFNET_TIMERQUEUE_MULTIMAP
    delete (TMQueue*)queue;
#else
    delete (TimerQueueSplayTree*)queue;
#endif
  }
  delete qtimer;
}

void TimerQueue::clearQueue(bool release_mem)
{
  if(is_sorted) {
    TDQueue* tqueue = (TDQueue*)queue;
    if(!tqueue->empty()) {
      if(!in_callback) qtimer->cancel();
      TDQIterator end_iter = tqueue->end();
      for(TDQIterator iter = tqueue->begin();
	  iter != end_iter; iter++) {
	(*iter)->timer_set = false;
	if(release_mem) delete (*iter);
      }
    }
    tqueue->clear();
  } else {
#ifdef SSFNET_TIMERQUEUE_MULTIMAP
    TMQueue* tqueue = (TMQueue*)queue;
    if(!tqueue->empty()) {
      if(!in_callback) qtimer->cancel();
      TMQIterator end_iter = tqueue->end();
      for(TMQIterator iter = tqueue->begin();
	  iter != end_iter; iter++) {
	assert(iter->second);
	iter->second->timer_set = false;
	if(release_mem) delete iter->second;
      }
      tqueue->clear();
    }
#else
    TimerQueueSplayTree* splay = (TimerQueueSplayTree*)queue;
    if(!splay->empty()) {
      if(!in_callback) qtimer->cancel();
      while(!splay->empty()) {
	TimerQueueData* tqd = splay->top();
	splay->pop();
	tqd->timer_set = false;
	if(release_mem) delete tqd;
      }
    }
#endif
  }
}
void TimerQueue::insert(TimerQueueData* tqd)
{
  if(!tqd) LOG_ERROR("empty timer data.");
  if(tqd->timer_set) LOG_ERROR("timer data has been used.");
  if(tqd->my_time < VirtualTime(comm->now()))
    //XXX LOG_ERROR("time's in the past: now=" << VirtualTime(comm->now()).second()
    LOG_WARN("time's in the past: now=" << VirtualTime(comm->now()).second()
	      << ", insert time=" << tqd->my_time.second() << ".");

  if(is_sorted || tqd->my_time > max_time) {
	  append(tqd);
	  return;
  }

#ifdef SSFNET_TIMERQUEUE_MULTIMAP
  bool reset_timer = false; // preempt the first timer?

  TMQueue* tqueue = (TMQueue*)queue;
  if(!tqueue->empty() && tqueue->begin()->first > tqd->my_time) {
    reset_timer = true;   // new event happens first
    if(!in_callback) qtimer->cancel();
  }

  tqd->timer_set = true;
  tqueue->insert(TMQueue::value_type(tqd->my_time, tqd));

  // set timer if needed.
  if(reset_timer || tqueue->size() == 1) {
    if(!in_callback) {
      assert(qtimer->isCancelled());
      qtimer->set(tqd->my_time - VirtualTime(comm->now())); // restart the timer
    }
  }
#else
  bool reset_timer = false; // preempt the first timer?
  TimerQueueSplayTree* splay = (TimerQueueSplayTree*)queue;
  if(!splay->empty() && splay->top()->my_time > tqd->my_time) {
    reset_timer = true;   // new event happens first
    if(!in_callback) {
    	qtimer->cancel();
    }
  }
  //LOG_DEBUG("tqd "<<tqd<<endl);
  tqd->timer_set = true;
  splay->push(tqd);

  // set timer if needed.
  if(reset_timer || splay->size() == 1) {
    if(!in_callback) {
      assert(qtimer->isCancelled());
      qtimer->set(tqd->my_time - VirtualTime(comm->now())); // restart the timer
    }
  }
#endif
}

void TimerQueue::append(TimerQueueData* tqd)
{
  if(!tqd) LOG_ERROR("empty timer data.");
  if(tqd->timer_set) LOG_ERROR("timer data has been used.");
  if(tqd->my_time < max_time) {
    LOG_ERROR("append requires non-decreasing time.");
    //tqd->my_time = max_time; // clock may change during the simulation run?
  }
  max_time = tqd->my_time;

  if(is_sorted) {
	LOG_DEBUG("is_sorted."<<endl);
    TDQueue* tqueue = (TDQueue*)queue;
    tqd->timer_set = true;
    tqueue->push_back(tqd);
    if(tqueue->size() == 1) {
      if(!in_callback) {
	assert(qtimer->isCancelled());
	qtimer->set(tqd->my_time - VirtualTime(comm->now()));
      }
    }
  } else {
#ifdef SSFNET_TIMERQUEUE_MULTIMAP
    TMQueue* tqueue = (TMQueue*)queue;
    tqd->timer_set = true;
    if(tqueue->empty()) {
      tqueue->insert(TMQueue::value_type(tqd->my_time, tqd));
      if(!in_callback) {
	assert(qtimer->isCancelled());
	qtimer->set(tqd->my_time - VirtualTime(comm->now()));
      }
    } else {
      tqueue->insert(tqueue->end(),
		     TMQueue::value_type(tqd->my_time, tqd));
    }
#else
    TimerQueueSplayTree* splay = (TimerQueueSplayTree*)queue;
    tqd->timer_set = true;
    splay->push(tqd);
    if(splay->size() == 1) {
      if(!in_callback) {
    	  assert(qtimer->isCancelled());
    	  qtimer->set(tqd->my_time - VirtualTime(comm->now()));
      }
    }
#endif
  }
}

void TimerQueue::remove(TimerQueueData *tqd)
{
  if(is_sorted) LOG_ERROR("remove not supported for sorted timer queueu.");
  if(!tqd || !tqd->timer_set) return;  // data not in use

#ifdef SSFNET_TIMERQUEUE_MULTIMAP
  TMQueue* tqueue = (TMQueue*)queue;
  if(tqueue->empty()) return;

  SSFNET_PAIR(TMQIterator, TMQIterator) range
    = tqueue->equal_range(tqd->my_time);

  if(distance(range.first, range.second) <= 0)
    return; // doesn't exist

  for(TMQIterator iter = range.first;
      iter != range.second; iter++) {
    if(iter->second == tqd) {
      assert(tqd->timer_set);

      tqd->timer_set = false; // turn off the switch
      tqueue->erase(iter); // remove it from the queue

      // try to see whether we need to readjust the timer
      if(!in_callback) {
	if (tqueue->empty()) qtimer->cancel();
	else {
	  if(tqueue->begin()->first > tqd->my_time) {
	    qtimer->cancel();
	    qtimer->set(tqueue->begin()->first - VirtualTime(comm->now()));
	  }
	}
      }
      break;
    }
  }
#else
  TimerQueueSplayTree* splay = (TimerQueueSplayTree*)queue;
  if(splay->empty()) return;

  bool reset_timer = (tqd == splay->top());
  if(reset_timer && !in_callback) qtimer->cancel();
  tqd->timer_set = false;
  splay->remove(tqd);
  if(reset_timer && !splay->empty() && !in_callback) {
    assert(qtimer->isCancelled());
    qtimer->set(splay->top()->my_time - VirtualTime(comm->now())); // restart the timer
  }
#endif
}

} // namespace ssfnet
} // namespace prime
