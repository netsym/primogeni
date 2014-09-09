/*
 * splaytree :- priority queue implemented as a splay tree.
 *
 * Splay tree, or self-adjusting search tree, is a simple but
 * efficient data structure for storing an ordered set. The key
 * feature of this data structure---basically a binary tree---is that
 * it allows insertion, delete-min, and many other operations, all
 * with amortized logarithmic performance. We made an important change
 * to this data structure so that simultaneous events are handled
 * differently. Optionally, events with the same timestamp value as
 * the current simulation clock (which is expected to be the timestamp
 * of the last event retrieved from this data structure) are store
 * separately in a doubled ended linked list. Since these events
 * frequently happen in simulation, we can cut the cost of insertion
 * and deletion of these events to a mere constant cost. In order to
 * allow such optimization, we require newly inserted events into the
 * data structure must have no smaller timestamp value than the events
 * that have been retrieved earlier.
 */

#ifndef __PRIME_SSF_SPLAYTREE_H__
#define __PRIME_SSF_SPLAYTREE_H__

#include <assert.h>

#include "api/ssftype.h"
#include "api/ssfqobj.h"
#include "sim/kernelevt.h"

namespace prime {
namespace ssf {

class ssf_splay_tree {

 public:
  /*
   * Constructor. The user can decide whether to use the optimization
   * of caching simultaneous events in a double-ended queue. Doing so
   * we run the risk of generating unrepeatable simulation results if
   * simultaneous events are not dealt with carefullly.
   */
  ssf_splay_tree();

  // Destructor.
  virtual ~ssf_splay_tree();

  // Returns the total number of events stored in the data structure.
  int size() { return _spt_nevts; }
  
  // Returns true (non-zero) if there's no more event left.
  int empty() { return size() == 0; }

  /*
   * Returns the event with the highest prioirty (i.e., with the
   * smallest timestamp). In order to conform to the standard priority
   * queue template interface, we make sure it reports an error if
   * this method is called when there's no event.
   */
  ssf_kernel_event* top();

  /*
   * Remove the event with with the smallest timestamp. It'll report
   * an error if this method is called when there's no event left.
   */
  void pop();

  // Insert a new event into the data structure.
  void push(ssf_kernel_event* kevt);

  /*
   * Remove an event from the splay tree. The event must be in the
   * splay tree when the method is called.
   */
  void remove(ssf_kernel_event* kevt);

  // Remove all events.
  void clear();

 protected:
  /*
   * The ssf_splay_node class is used only internally to represent a node
   * of the splay tree. Each tree node has a pointer to the kernel
   * event and three other pointers to indicate its position in the
   * tree: one to its parent and two to its children on the left and
   * on the right. Splay tree is a typical binary tree. The class is
   * derived from the ssf_quickobj class for fast memory operations.
   */
  class ssf_splay_node : public ssf_quickobj {
  public:
    ssf_kernel_event* kevt;

    ssf_splay_node* parent;
    ssf_splay_node* left;
    ssf_splay_node* right;

    ssf_splay_node(ssf_kernel_event* ke) : 
      kevt(ke), parent(0), left(0), right(0) {
      assert(kevt);
      kevt->context = this;
    }

    ~ssf_splay_node() { assert(kevt); kevt->context = 0; }
  }; /*ssf_splay_tree::ssf_splay_node*/
  
  // Number of events (including simultaneous events).
  int _spt_nevts;

  // Root of the tree. The tree is supposed to be balanced.
  ssf_splay_node* _spt_root;

  // The tree node with the highest priority (least timestamp).
  ssf_splay_node* _spt_min;
 
#if PRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS
  /*
   * Remember the timestamp of the last event retrieved. Used on when
   * we turn on the optimization for simultaneous events.
   */
  ltime_t _spt_now;

  /*
   * This is a double-ended list that contains newly inserted events
   * with the same timestamp as the one retrieved last time. This is
   * used by the optimization that can be turned on by the user.
   */
  SSF_DEQUE(ssf_kernel_event*) _spt_nowevts;
#endif

  // Insert a node into the splay tree.
  void _spt_insert_node(ssf_kernel_event* kevt);

  // Remove the splay tree node.
  ssf_kernel_event* _spt_remove_node(ssf_kernel_event* kevt);

  // Remove the tree node with least timestamp.
  ssf_kernel_event* _spt_remove_min();

  // Reclaim sub-tree rooted by the given node.
  void _spt_reclaim_tree(ssf_splay_node* node);

  // Splay operation.
  void _spt_splay(ssf_splay_node* node);

}; /*ssf_splay_tree*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SPLAYTREE_H__*/

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
