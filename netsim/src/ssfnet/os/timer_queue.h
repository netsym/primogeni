/**
 * \file timer_queue.h
 * \brief Header file for the TimerQueue class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
 * 
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

#ifndef __TIMER_QUEUE_H__
#define __TIMER_QUEUE_H__

#include "os/ssfnet_types.h"
#include "os/timer.h"

namespace prime {
namespace ssfnet {

class TimerQueue;

#ifndef SSFNET_TIMERQUEUE_MULTIMAP
class TimerQueueSplayTree;
class TimerQueueSplayNode;
#endif

/**
 * \brief Data to be inserted in the timer queue.
 *
 * This is the data structure used by the timer queue. Each instance
 * is associated with a scheduled timer and upon its expiration, the
 * timer's callback function will be invoked with this object passed
 * as the argument. Typically when some extra info is desired to
 * identify the timer, this class is extended to include the info.
 */
class TimerQueueData : public ssf::ssf_quickobj {
  friend class TimerQueue;

 private:
  /** Whether this timer is in the timer queue. */
  bool timer_set;

  /** Time until timeout. */
  VirtualTime my_time;

 public:
  /** The constructor. */
  TimerQueueData(VirtualTime t) : 
    timer_set(false), my_time(t)
#ifndef SSFNET_TIMERQUEUE_MULTIMAP
    , context(0)
#endif
  {}

  /** The destructor. */
  virtual ~TimerQueueData();

  /** Return the scheduled time of this data. */
  VirtualTime time() { return my_time; }

  /** Set the time so that the object can be used continuously. */
  void setTime(VirtualTime t);

  /** Add more time so that the object can be used continuously. */
  void addTime(VirtualTime t);

  /** Whether this timer is in use (running). */
  bool inUse() { return timer_set != 0; }

#ifndef SSFNET_TIMERQUEUE_MULTIMAP
 private:
  // the following is used internally
  friend class TimerQueueSplayTree;
  friend class TimerQueueSplayNode;
  void* context;
  inline int compare_with(TimerQueueData* tqd) {
    assert(tqd);
    if(my_time < tqd->my_time) return -1;
    else if(my_time > tqd->my_time) return 1;
    else return 0;
  }
#endif
};

/**
 * \brief A queue of timers.
 *
 * The TimerQueue class is used in the situation when many timers are
 * needed.  In fact, this class just makes use of the timer class. The
 * user insert data with timestamp to the queue, the data (of
 * TimerQueueData type) will be passed to the callback function at the
 * specified time. From the simulation kernel's point of view, only
 * one timer is actually used for the entire queue of timers on the
 * event list.
 *
 * Like the timer class, the user needs to provide a callback
 * function, which takes a TimerQueueData pointer as its parameter.
 * The TimerQueue class is an abstract class, i.e., it cannot be used
 * directly. One needs to overload the callback function in a derived
 * class.
 *  
 * It is important to note that a scheduled timer data can only get
 * out of the queue in two ways: timer expiration and explicit
 * removal. In any case, the callback function is responsible to
 * reclaim the TimerQueueData passed to it if necessary.
 *
 * There are two types of timer queues: RANDOM and SORTED. The type is
 * specified in the constructor. The RANDOM type is the default. The
 * RANDOM type allows the data to be inserted in any random order,
 * while the SORTED type assumes the time of the data inserted is
 * always non-decreasing. We use a faster data structure if we already
 * know the data is sorted.
 */
class TimerQueue {
  friend class QueueTimer; // defined in the following

 protected:
  /** 
   * \internal
   * \brief The internal timer used in TimerQueue.
   */
  class QueueTimer : public Timer {
  private:
    TimerQueue *tq;

  public:
    QueueTimer(ProtocolSession* sess, TimerQueue* timer_queue) :
      Timer(sess), tq(timer_queue) {}

    QueueTimer(Community* comm, TimerQueue* timer_queue) :
      Timer(comm), tq(timer_queue) {}

    virtual void callback() { tq->timer_callback(); }
  };

#ifdef SSFNET_TIMERQUEUE_MULTIMAP
  // for random queue type, we use a map (with no limit on the number
  // of items having the same key)
  typedef SSFNET_MULTIMAP(VirtualTime, TimerQueueData*) TMQueue;
  typedef TMQueue::iterator TMQIterator;
#endif

  // for sorted type, we use a double-ended queue
  typedef SSFNET_DEQUE(TimerQueueData*) TDQueue;
  typedef TDQueue::iterator TDQIterator;

 protected:
  /** whether the timer queue is sorted or not (random). */
  bool is_sorted;

  /** True if inside the user callback function where the timer
      operation should be disallowed. */
  bool in_callback;

  /** The queue of timer data; we use a generic pointer since it could
      be a map or a doubled-ended queue. */
  void* queue;

  /** The timer used internally by the timer queue. */
  QueueTimer *qtimer;

  /** Owner timeline. */
  Community* comm;

  /** The maximum time of data inserted into the queue. */
  VirtualTime max_time;

 public:
  /** The constructor. */
  TimerQueue(ProtocolSession *psess, bool is_sorted = false);

  /** The constructor that does not specify an owner protocol. */
  TimerQueue(Community *pcomm, bool is_sorted = false);

  /** The destructor. */
  virtual ~TimerQueue();

  /** 
   * The callback function to be overwritten in the derived class. The
   * function is responsible for reclaiming the timer data passed to
   * it when necessary.
   */
  virtual void callback(TimerQueueData *tqd) = 0;

  /** 
   * Insert data into the queue and schedule a timer event.
   * @param tqd points to the timer queue data.
   */
  void insert(TimerQueueData* tqd);

  /**
   * Append the timer data to the queue, assuming the data has time
   * greater than all others already in queue.
   */
  void append(TimerQueueData* tqd);

  /**
   * Remove a previously scheduled timer queue data from the
   * queue. Note that the user is responsible for reclaiming the data
   * afterwards.  @param tqd points to the data already scheduled in
   * queue.
   */
  void remove(TimerQueueData* tqd);

  /**
   * Cancel the timer. Remove all the scheduled timers and their
   * associated data in the queue. The argument specifies whether the
   * timer queue data is to be reclaimed or not.
   */
  void clearQueue(bool release_mem = false);

 protected:
  // called by QueueTimer callback function
  void timer_callback();
};

} // namespace ssfnet
} // namespace prime

#endif /*__TIMER_QUEUE_H__*/
