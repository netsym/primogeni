/*
 * timeline :- internal representation of an SSF timeline.
 *
 * Each timeline corresponds to a logical process or LP in a parallel
 * discrete-event simulation term. Each timeline maintains its own
 * event list and interacts with others only through message passing
 * using well-defined portals. We separate the event processing
 * functions, which are independent of the underlying synchronization
 * algorithm, with the scheduling-related functions, which depends on
 * the synchronization algorithm. The timeline class defined in this
 * module only deals with event processing (including scheduling
 * user-level processes).
 */

#ifndef __PRIME_SSF_TIMELINE_H__
#define __PRIME_SSF_TIMELINE_H__

#include "api/ssftime.h"
#include "api/ssftype.h"
#include "sim/eventlist.h"

using namespace std;

namespace prime {
namespace ssf {

typedef SSF_PAIR(size_t,char*) ssf_szname_pair;

typedef SSF_QSET(Entity*) SSF_TML_ENT_SET;
typedef SSF_QDEQUE(ssf_kernel_event*) SSF_TML_KEVT_DEQUE; 
typedef SSF_QVECTOR(ssf_szname_pair) SSF_TML_INTBUF_VECTOR; 
typedef SSF_QDEQUE(Process*) SSF_TML_PRC_DEQUE; 
typedef SSF_QVECTOR(inChannel*) SSF_TML_IC_VECTOR;

class ssf_timeline {

 private: /* private interface, accessible only by internal implementation */

  /* list of friendly classes to open access */

  friend class Entity;
  friend class Process;
  friend class inChannel;
  friend class outChannel;
  friend class Event;

  friend class ssf_logical_process;
  friend class ssf_less_logical_process;
  friend class ssf_stargate;
  friend class ssf_teleport;
  friend class ssf_universe;
  friend class ssf_realtime_queue;

  // The constructor.
  ssf_timeline(prime::ssf::uint32 sno);

  // The destructor.
  virtual ~ssf_timeline();

  // Add a new entity to the current timeline. We need to assign a
  // unique serial number to the newly created entity.
  void add_entity(Entity*);

  // Remove an entity from this timeline.
  void remove_entity(Entity*);

  // This method is called by the system to process local events on
  // the timeline's event list with timestamps strictly less than
  // the simulation time given by the argument. Basically, the
  // system's global scheduler (one for each SSF instance) computes
  // the lower bound on timestamp (LBTS) it can allow this timeline
  // to advance its simulation clock without the danger of straggling
  // events (events with timestamps smaller than the current
  // simulation clock) to arrive later from another timeline. If the
  // timeline is a real-time timeline, which means it must be paced
  // with the real time, LBTS should be the current wall-clock
  // time. The event processing could be interrupted, for example,
  // by the embedded flow control mechanism, or maybe some real-time
  // requirements. The method returns zero if all local events with
  // timestamps less than LBTS has been processed. Otherwise, it
  // returns nonzero indicating it's been interrupted.
  int micro_scheduling(ltime_t LBTS);

  // Each timeline in the system has a unique id and it is taken from
  // the corresponding logical process.
  prime::ssf::uint32 serialno() { return sno; }

  // Insert a kernel event into the timeline's event list. If it's
  // beyond the current decade, the event will be managed using the
  // bin-queue data structure by the universe.
  void insert_event(ssf_kernel_event* kevt, int force_in = 0);

  // Cancel or retract an event. If the event can be removed from the
  // event list, we will make it so. However, if the event is in a
  // data structure that doesn't support direct removal (in the
  // simevents list or in a bin-queue maintained by the universe), we
  // simply mark it as cancelled.
  void cancel_event(ssf_kernel_event* kevt);

  // Insert a process onto the ready list. The process can be
  // selected for execution when there's a context switch.
  void activate_process(Process* p);

  // Return the first process that has been activated.
  Process* get_active_process();

  // Remove the running process from the ready queue and give it a
  // new state. This is usually called by the process itself.
  void deactivate_process(int newstate);

  // Called by standard SSF interface methods (like
  // Entity::processes) to allocate a contextual memory block of
  // given size. Contextual memory is only available at the current
  // process context. It'll be removed once there's a context switch.
  char* new_contextual_memory(size_t size);

  // Reclaim all contextual memory blocks allocated for the current
  // context. This method is called within micro_scheduling.
  void clear_contextual_memory();

  // Clean up.
  void wrapup();

#if PRIME_SSF_EMULATION
  // Used by emulation for sorting RT timelines. Returns the
  // minimum between the simulation time of next event in queue and
  // the safe time calculated from all its input channels.
  ltime_t next_due_time();
#endif

#if PRIME_SSF_INSTRUMENT

  public:

  /**
   * \brief Returns the text name of the alignment.
   *
   */
  SSF_STRING* getAlignmentName() {
    return alignmentName;   
  }
    
  /**
   * \brief Sets the text name of the entity.
   *
   */
  void setAlignmentName(SSF_STRING* alignName) {
    if (alignmentName) delete alignmentName;
    alignmentName = new SSF_STRING(alignName->c_str());
  }

  /**
   * \brief Sets the current action and time mark.
   *
   * This adds the period since the last time mark to the time accumulator 
   * for the previous action.  If the previous action was waiting on input,
   * timings are also accumulated for the timeline that this timeline was
   * waiting on input from.
   */
  void setAction(int action);

  void dumpInstrumentationData(int whereami);

  void messageSent(prime::ssf::uint32 toTimeline) {
    messagesSent[toTimeline]++;
  }

#endif

 private: /* internal member data */

#if SSF_SHARED_DATA_SEGMENT
  // Runtime context to facilitate the SSF_USE_RTX_PRIVATE method.
  void* __rtx__;
#endif

  // Serial number of this timeline.
  prime::ssf::uint32 sno;

#if PRIME_SSF_INSTRUMENT
  enum {
    ACTION_INVALID = 0,
    ACTION_INITIALIZING,
    ACTION_DETERMINING_NEXT_EVENT,
    ACTION_CANCELLED_EVENT,
    ACTION_NEWENTITY_EVENT,
    ACTION_NEWPROCESS_EVENT,
    ACTION_TIMEOUT_EVENT,
    ACTION_OUTCHANNEL_EVENT,
    ACTION_INCHANNEL_EVENT,
    ACTION_TIMER_EVENT,
    ACTION_SCHEDULE_ENTFCT_EVENT,
    ACTION_SCHEDULE_PRCFCT_EVENT,
    ACTION_SEMSIGNAL_EVENT,
    ACTION_EVENT_POSTPROCESSING,
    ACTION_SENDING_MESSAGES,
    ACTION_RECIEVING_MESSAGES,
    ACTION_WAITING,
    ACTION_INTERRUPTED,
    ACTION_OTHER,
    ACTION_TOTAL
  };

  static char* actionDescriptions[ACTION_TOTAL];
  long* actionCount;
  ssf_machine_time* actionTiming;
  int currentAction;
  ssf_machine_time currentTime;
  ssf_machine_time lastTimeMark;

  map<prime::ssf::uint32, ssf_machine_time> inputWaitTimes;
  map<prime::ssf::uint32, int> messagesSent;
  prime::ssf::uint32 waitingOnInputFrom;
  prime::ssf::uint32* numTimings;
  SSF_STRING* alignmentName;
#endif

  // Does this timeline have to attain real-time deadlines?
  int emuable;

#if PRIME_SSF_EMULATION
  // Used by emulation: is this timeline waiting for next event (in
  // ssf_universe::rt_trampoline) or is it waiting for other timelines to
  // update itself?
  int on_stage;
#endif /*PRIME_SSF_EMULATION*/

  // The upper bound of simulation time this timeline can advance its
  // simulation clock. Also used by emulation to calculate the next due
  // time of this timeline.
  ltime_t safetime;

  // Simulation clock of this timeline. Will not go backwards.
  ltime_t simclock;

  // The event list is a priority queue. We can use either a splay
  // tree, or a binary heap, or even a standard priority queue
  // from STL (most likely implemented using a heap).
  ssf_event_list evtlist;

  // Assuming the alignment does not change frequently, we use a
  // vector to hold all entities in this alignment group.
  SSF_TML_ENT_SET entities;

  // Simultaneous events (with timestamps equal to the simulation
  // clock are stored here waiting to be processed in a batch mode.
  SSF_TML_KEVT_DEQUE simevents;

  // Memory blocks allocated during the current context should be
  // reclaimed when there's a context switch.
  SSF_TML_INTBUF_VECTOR context_buffers;

  // List of active processes ready to run.
  SSF_TML_PRC_DEQUE active_processes;

  // Incoming channels that have events arrived at the current
  // simulation time.
  SSF_TML_IC_VECTOR active_inchannels;
}; /*ssf_timeline*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_TIMELINE_H__*/

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
