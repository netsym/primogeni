/*
 * kernelevt :- kernel event data structure.
 *
 * A kernel event is different from a user-level event, which is an
 * instance of a subclass of the Event class used by the user for
 * encapsulating a message sent through the channels. A kernel event
 * is an event scheduled onto the timeline's event list for various
 * time-related operations, such as initializing an entity or a
 * process upon creation, calling the user's callback function at
 * timeout, activating a user process upon a message arrival at an
 * in-channel, and so on.
 */

#ifndef __PRIME_SSF_KERNELEVT_H__
#define __PRIME_SSF_KERNELEVT_H__

#include <assert.h>
#include "primex_config.h"
#include "api/ssftime.h"
#include "api/ssfqobj.h"

namespace prime {
namespace ssf {

class Entity;
class Process;
class Event;
class outChannel;
class inChannel;

class ssf_timeline;
class ssf_local_tlt;
class ssf_map_node;
class ssf_timer;

class ssf_kernel_event /*: public ssf_quickobj*/ {

  /* Since kernel events are allocated and deallocated frequently
     during the course of a simulation, we make it a subclass of the
     ssf_quickobj class so that it can control its own memory
     allocation and deallocation in a quick manner. The quick memory
     layer has a fragmentation cost: the size of a memory block is
     rounded up to a power of two with the smallest block size being
     16 bytes. We should watch the size of the kernel event closely
     when tuning the simulator's memory performance. The memory used
     by kernel events could be a primary source of memory consumption
     in a simulation. */

 public:
  // All possible kernel event types are enumerated here:
  enum {
    /*
     * Invalid type: it's an error to process a kernel event of this
     * type. We skip 0 as a type ID for better error resistance.
     */
    EVTYPE_NONE = 0,

    /*
     * The event has been cancelled previously. It's possible a kernel
     * event is stored in a data structure that does not support
     * removal unless it's in the front of the event queue. In that
     * case, all we can do is simply mark the event as being
     * cancelled. The internal member data of the kernel event should
     * be reclaimed as necessary at the time when the event is
     * cancelled. See the ssf_kernel_event::cancel() method.
     */
    EVTYPE_CANCELLED,

    /*
     * The event is scheduled by the Entity constructor with the same
     * simulation time as when the Entity object is created. The
     * kernel event maintains a pointer to the entity object. When we
     * process the event (at the same simulation time), the init()
     * method of the Entity class is called. Since the constructor and
     * the init method are called in different contexts (separated by
     * the kernel event scheduler), it is possible the constructor and
     * the init method are invoked on different processors! The
     * purpose of this is to have a balanced workload for initiating
     * the model if running in a multi-processor environment. Unlike
     * the entity constructor, at the time when the init method is
     * called, the entity has already been properly aligned.
     */
    EVTYPE_NEWENTITY,

    /*
     * The event is scheduled by the Process constructor with the same
     * simulation time as when the process is created. The kernel
     * event maintains a pointer to the process object. The process
     * constructor and the init() method run in different
     * contexts. Same as the EVTYPE_NEWENTITY event, the purpose is to
     * give a chance for the simulator to balance the workload when
     * initiating the model. When processing this event, the init()
     * method of the process is called and the process body is then
     * activated for the first time.
     */
    EVTYPE_NEWPROCESS,

    /*
     * The event unblocks the process that is suspended for a
     * specified period of simulation time. There is one and only one
     * such event per process, and therefore we keep one such event in
     * each process object. That is, the event is owned by a process
     * and should not be deleted after being processed. The event
     * maintains a pointer to the process it belongs to.
     */
    EVTYPE_TIMEOUT,

    /*
     * The event is used to carry a user event (of Event class or its
     * offspring) that travels from an out-channel to a list of
     * in-channels that map to the out-channel and belong to the same
     * target timeline. This kernel event is actually created at the
     * target timeline and inserted into the timeline's event list
     * with the same simulation time as when the event arrives at the
     * first in-channel. The kernel event is reused as it "snakes"
     * through each mapped in-channel.  The kernel event contains a
     * pointer to the receptacle of the target timeline (a ssf_map_node
     * object) that represents the next in-channel, which expects to
     * receive the user event; it also contains a pointer to the user
     * event that is carried along.
     */
    EVTYPE_OUTCHANNEL,
    EVTYPE_CHANNEL,
    EVTYPE_INCHANNEL,

    /*
     * The kernel event of this type is used by an entity timer. The
     * timer fires when the prespecified simulation time has
     * elapsed. The timeline's micro scheduler processes this event
     * and invokes a user-defined callback function, which can be
     * either an entity method or a regular C++ static function. The
     * kernel event is owned by the timer and shall not be reclaimed
     * by the scheduler after processing the event. The event contains
     * a pointer to the timer.
     */
    EVTYPE_TIMER,

    /*
     * The event type is used to support direct-event scheduling by a
     * family of Entity::schedule() methods. The kernel event
     * maintains two data fields: a pointer to the entity that
     * schedules the event, an integer-type handle that identifies the
     * callback function and the associated data (a mapping from the
     * handle to the callback function and data is provided in the
     * Entity object).
     */
    EVTYPE_SCHEDULE_ENTFCT,
    EVTYPE_SCHEDULE_PRCFCT,

    /*
     * This event type is used by the Semaphore::semSignal() method to
     * unblock the process waiting on a semaphore. The kernel event
     * contains a pointer to the process that should be unblocked.
     */
    EVTYPE_SEMSIGNAL,

    /*
     * This is the total number of defined kernel event types. It's
     * used for sanity checking: all kernel events must have a valid
     * type value between 1 and EVTYPE_TOTAL-1.
     */
    EVTYPE_TOTAL
  };

  static const char* _event_type_descriptions[EVTYPE_TOTAL];


  // The simulation time of this kernel event.
  ltime_t time;

  // The serial number of the entity that generates this event.
  prime::ssf::uint32 entity_serialno;

  // The event sequence number increases by one for each consecutive
  // kernel event generated by the entity.
  prime::ssf::uint32 event_seqno;

  /*
   * Each kernel event maintains a pointer that is required by the
   * implementation of the priority queue to infer its context. A
   * context is needed if the kernel event is to be removed from the
   * data structure. For splay tree, this pointer points to the tree
   * node storing the kernel event; for binary heap, this pointer
   * points to the element in the heap array and therefore the index
   * of the element can be inferred. This pointer can also be used to
   * indicate whether the kernel event is in the priority queue data
   * structure or not; it's helpful for debugging purposes.
   */
  void* context;

  // The type of this kernel event.
  int type;

  /*
   * The following data fields are defined differently with different
   * kernel event types. We ought to be very careful in organizing
   * these fields into several union structures to save memory.
   */
  union {
    Entity* entity;          // NEWENTITY, SCHEDULE_ENTFCT
    Process* process;        // SCHEDULE_ENTFCT, NEWPROCESS, TIMEOUT, SEMSIGNAL
    outChannel* outchannel;  // OUTCHANNEL
    ssf_local_tlt* localtlt; // CHANNEL
    ssf_map_node* mapnode;   // INCHANNEL
    ssf_timer* timer;        // TIMER
  };

  union {
    void (Entity::*entfct)(Event*);  // SCHEDULE_ENTFCT
    void (Process::*prcfct)(Event*); // SCHEDULE_PRCFCT
  };

  union {
    long sched_handle; // SCHEDULE_ENTFCT, SCHEDULE_PRCFCT
  };

  // User event attached to the kernel event.
  Event* usrdat;

  /*
   * The constructor of the kernel event. Different kernel event types
   * expect different arguments. Each event is identified by a serial
   * number of the entity that creates this event, an event sequence
   * number (recorded by the entity and incremented by one for each
   * newly created kernel event), the simulation time the event is
   * scheduled to be processed, and the type of this kernel event.
   */
  ssf_kernel_event(ltime_t evtime, int evtype, Event* evt = 0);

  // Destructor.
  virtual ~ssf_kernel_event();

  /*
   * This method is used by the priority queue (a Splay tree, a binary
   * heap, or others) to sort all kernel events. There should be ways
   * to provide a total ordering of the events, especially for those
   * events with the same simulation time (i.e., simultaneous
   * events). Simultaneous events may cause unrepeatable results. We
   * maintain simulation repeatability by observing the following
   * rule:
   *
   *   Two runs using the same model---with the same entities and the
   *   same channel mappings---and using the same random seed should
   *   generate the same result, regardless of how the entities are
   *   aligned (for example, using 4 timelines instead of 1), and
   *   regardless of the underlying machine platform (for example,
   *   using 16 distributed-memory machines of two processors each,
   *   instead of sequential execution), provided that interactions
   *   between entities are carried out only through channels.
   *
   * To guarantee the above property, we use two fields in addition to
   * the simulation time to help sort events in a priority queue. One
   * is the a 32-bit unsigned integer that's the serial number of the
   * entity who created this kernel event, and the other is a 32-bit
   * unsigned integer that's the sequence number of the event, which
   * is assigned by the entity who created this event. The sequence
   * number is added by one for each consecutive kernel event
   * generated by the same entity.
   *
   * The method returns -1 if the rank of this kernel event is smaller
   * than that of the one provided the argement, or 1 if it's
   * larger. It should be impossible to have events with the same
   * rank, unless there's an integer wrap-around problem, which is
   * highly unlikely.
   */
  inline int compare_with(ssf_kernel_event* kevt) {
    assert(kevt && 0 < kevt->type && kevt->type < EVTYPE_TOTAL);
    if(time < kevt->time ||
    		(time == kevt->time &&
					(entity_serialno < kevt->entity_serialno ||
								(entity_serialno == kevt->entity_serialno &&
									event_seqno < kevt->event_seqno
								)
					)
			)
		) return -1;
    return 1;
  }
  /*
   * This method marks the current kernel event as "cancelled". During
   * simulation, a kernel event may be stored in various data
   * structures. It's possible the data structure does not support
   * efficient removal of events, unless it's at a particular
   * position, e.g., the front of a queue or the end of a list. When
   * we can't remove the event, we can only mark the event as
   * "cancelled". And later when we process the event, we simply skip
   * it. This method must reclaim internal data of this kernel event
   * and change the type of the event as EVTYPE_CANCELLED.
   */
  void cancel();

  /*
   * This method returns the associated timeline of this event, which
   * can be deduced from the event type and that data field.
   */
  ssf_timeline* owner_timeline();

#if PRIME_SSF_MANAGE_KERNEL_EVENTS
 public:
  /* The kernrel events are used often enough to warrant their own
     memory management. */
  ssf_kernel_event* kevt_nxtpool;
  static void* operator new(size_t sz);
  static void operator delete(void* p);
#endif /*PRIME_SSF_MANAGE_KERNEL_EVENTS*/

}; /*ssf_kernel_event*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_KERNELEVT_H__*/

/*
 * 
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
