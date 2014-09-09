/*
 * timeline :- internal representation of an SSF timeline.
 *
 * Each timeline corresponds to a logical process or LP in a parallel
 * discrete-event simulation term. Each timeline maintains its own
 * event list and interacts with other only through message passing
 * using well-defined portals. We separate the event processing
 * functions, which are independent of the underlying synchronization
 * algorithm, with the scheduling-related functions, which depends on
 * the synchronization algorithm. The timeline class defined in this
 * module only deals with event processing (including scheduling
 * user-level processes).
 */

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

#if PRIME_SSF_INSTRUMENT
char* ssf_timeline::actionDescriptions[ACTION_TOTAL] = {
  "invalid action",
  "initialized",
  "determined next event",
  "cancelled events happened",
  "new entity events happened",
  "new process events happened",
  "time out events happened",
  "outchannel events happened",
  "inchannel events happened",
  "timer events happened",
  "ent schedule events happened",
  "prc schedule events happened",
  "semaphore signal events happened",
  "postprocessed events",
  "sent messages",
  "received messages",
  "waited to run",
  "interrupted",
  "performed unknown tasks"
};
#endif

ssf_timeline::ssf_timeline(prime::ssf::uint32 s) :
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(0),
#endif
  sno(s), emuable(0),
#if PRIME_SSF_EMULATION
  on_stage(0),
#endif
   safetime(SSF_LTIME_INFINITY),
   simclock(SSF_LTIME_MINUS_INFINITY) 
{
#if PRIME_SSF_INSTRUMENT
  actionCount = new long[ssf_timeline::ACTION_TOTAL];
  actionTiming = new ssf_machine_time[ssf_timeline::ACTION_TOTAL];
  for (int i=0; i<ssf_timeline::ACTION_TOTAL; i++) {
    actionCount[i]=0;
    actionTiming[i]=0;
  }
  lastTimeMark = ssf_get_wall_time();
  currentAction = ACTION_INVALID;
  numTimings = new prime::ssf::uint32[ssf_kernel_event::EVTYPE_TOTAL];
  for (int i=0; i<ssf_kernel_event::EVTYPE_TOTAL; i++) {
    numTimings[i]=0;
  }
  alignmentName = new SSF_STRING("(unknown)");
#endif
}

ssf_timeline::~ssf_timeline()
{
  //SSF_CONTEXT_PROCESSING_TIMELINE_ASSIGN(this);
  //SSF_CONTEXT_NOW = SSF_LTIME_INFINITY;
#if PRIME_SSF_INSTRUMENT
  delete [] actionCount;
  delete [] actionTiming;
  delete [] numTimings;
#endif

  // delete all entities of this timeline
  for(SSF_TML_ENT_SET::iterator iter = entities.begin();
      iter != entities.end(); iter++) {
    delete (*iter);
  }
  entities.clear();

  assert(evtlist.empty());
  assert(simevents.empty());
  assert(context_buffers.empty());
  assert(active_processes.empty());
  assert(active_inchannels.empty());
}

void ssf_timeline::add_entity(Entity* ent)
{
  assert(ent);
  ent->_ent_serialno = SSF_CONTEXT_UNIVERSE->new_entity(ent);
  ent->_ent_timeline = this;
  entities.insert(ent);
}

void ssf_timeline::remove_entity(Entity* ent)
{
  assert(ent);
  SSF_CONTEXT_UNIVERSE->delete_entity(ent);
  entities.erase(ent);
}

int ssf_timeline::micro_scheduling(ltime_t LBTS)
{
  /*printf("timeline %d microscheduling: simclock=%lld, LBTS=%lld\n", 
    serialno(), simclock, LBTS);*/

  int interrupted = 0;
#if 0  
  int interrupted_called = 0;
#endif

  SSF_CONTEXT_PROCESSING_TIMELINE_ASSIGN(this);
  SSF_CONTEXT_KEVT_WATERMARK = 0;

  ltime_t last_realtime_check_io = SSF_USE_RTX_PRIVATE(realnow);

  for(;;) {

#if PRIME_SSF_INSTRUMENT
    setAction(ACTION_DETERMINING_NEXT_EVENT);
#endif

#if 0
    // it's possible someone raised the signal to interrupt the event
    // processing in the previous iteration; usually, when the signal
    // is raised the control breaks out immediately; it's possible
    // however one wants to finish processing all simultaneous events
    // first before breaking out the loop.
    if(interrupted) break;
#endif

    // check event memory usage; if it's above the limit, we interrupt
    // the execution session.
    if(!emuable && SSF_CONTEXT_FLOWMARK > 0 && // zero means infinity
       SSF_CONTEXT_KEVT_WATERMARK >= SSF_CONTEXT_FLOWMARK) {
      // we need to set the clock to the timestamp of the next event
      // or LBTS, whichever is smaller; if there're simultaneous
      // events left in the batch, the clock has already been set.
      if(simevents.empty()) {
	if(evtlist.empty()) simclock = LBTS;
	else {
	  simclock = evtlist.top()->time;
	  if(simclock > LBTS) simclock = LBTS;
	}
      }
      interrupted = 1;
      break; // break out immediately
    }

    if(!simevents.empty()) {
      // the batch is unfinished before it was interrupted last time
      SSF_CONTEXT_NOW = simclock;
    } else {
      // get a new batch of simultaneous events

      // if no event in the event list, we are through with this
      // execution session. we need to update the clock and break out
      // immediately.
      if(evtlist.empty()) {
	simclock = LBTS;
	break;
      }

      // next event's timestamp is no smaller than LBTS, meaning that
      // the current execution session has finished.

      // we process emulation events carrying the same timestamp as
      // LBTS (which is the current wall-clock time)!
      ssf_kernel_event* kevt = evtlist.top();
      if(kevt->time > LBTS || (!emuable && kevt->time == LBTS)) {
	simclock = LBTS;
	break;
      }

      // if the flow control mechanism is based on not allowing a
      // timeline to process events for too far ahead, we should break
      // out if the next event is beyond the allowance.
      if(!emuable && kevt->time >= SSF_CONTEXT_FLOWDELTA) {
	simclock = kevt->time;
	interrupted = 1;
	break;
      }

      SSF_CONTEXT_NOW = simclock = kevt->time;
      simevents.push_back(kevt);
      evtlist.pop();

      // retrieve all events with the same timestamp
      while(!evtlist.empty()) {
	kevt = evtlist.top(); // next event in queue
	if(kevt->time == simclock) {
	  simevents.push_back(kevt);
	  evtlist.pop();
	} else break;
      }
    }

    /*printf("timeline %d round: simclock=%lld\n", serialno(), simclock);*/

    // process all simultaneous events one by one
    while(!simevents.empty()) {

      ssf_kernel_event* current_event = simevents.front();
      simevents.pop_front();
      SSF_STATS_KERNEL_EVENTS++;

      switch(current_event->type) {

      case ssf_kernel_event::EVTYPE_CANCELLED: {
	// If the event has been cancelled somehow, nothing to be done
	// here except resetting the counter. We don't count cancelled
	// events.
#if PRIME_SSF_INSTRUMENT
      setAction(ACTION_CANCELLED_EVENT);
#endif
	SSF_STATS_KERNEL_EVENTS--;
	break;
      } // EVTYPE_CANCELLED
      
      case ssf_kernel_event::EVTYPE_NEWENTITY: {
	// This event is scheduled for each newly created entity at
	// the same simulation time when the entity is created. Simply
	// call the entity's init method.
#if PRIME_SSF_INSTRUMENT
      setAction(ACTION_NEWENTITY_EVENT);
#endif
	Entity* ent = current_event->entity;
	assert(ent && ent->_ent_timeline == this);
	ent->init();
	break;
      } // EVTYPE_NEWENTITY
    
      case ssf_kernel_event::EVTYPE_NEWPROCESS: {
	// This event is scheduled for each newly created process at
	// the same simulation time when the process is created. We
	// should i) call the process's init method for it to
	// initialize itself, and ii) activate the process by
	// scheduling it onto the ready queue for execution.
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_NEWPROCESS_EVENT);
#endif
	Process* p = current_event->process;
	assert(p && p->_proc_owner && 
	       p->_proc_owner->_ent_timeline == this);
	p->init();
	activate_process(p);

	break;
      } // EVTYPE_NEWPROCESS
    
      case ssf_kernel_event::EVTYPE_TIMER: {
	// One event-driven mechanism implemented in SSF is the
	// timer, with the goal of saving the cost of process context
	// switching, which is unavoidable in a process-oriented
	// simulation view. We should mark the timer as fired and
	// invoke the user callback routing.
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_TIMER_EVENT);
#endif
	ssf_timer* timer = current_event->timer;
	assert(timer && timer->_tmr_state == ssf_timer::TIMER_STATE_RUNNING);
      
	SSF_STATS_DIRECT_CALLBACKS++;
	timer->_tmr_state = ssf_timer::TIMER_STATE_FINISHED;
	if(timer->_tmr_entfct)
	  ((timer->_tmr_focus)->*(timer->_tmr_entfct))(timer);
	else if(timer->_tmr_regfct) 
	  (*(timer->_tmr_regfct))(timer);
	else timer->callback();
	break;
      } // EVTYPE_TIMER

      case ssf_kernel_event::EVTYPE_SCHEDULE_ENTFCT: {
	// This is for direct event scheduling implemented in the
	// entity class; we should i) remove the handle so that the
	// user won't be able to cancel the event anymore, and ii)
	// invoke the user-defined callback function.
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_SCHEDULE_ENTFCT_EVENT);
#endif
	assert(current_event->entity && current_event->entfct);

	SSF_STATS_DIRECT_CALLBACKS++;

	// if the event is retractable, remove the handle to disable it
	if(current_event->sched_handle) {
	  current_event->entity->_ent_delete_schedule
	    (current_event->sched_handle);
	}

	// invoke the callback function
	(current_event->entity->*(current_event->entfct))
	  (current_event->usrdat);
	break;
      } // EVTYPE_SCHEDULE_ENTFCT
    
      case ssf_kernel_event::EVTYPE_SCHEDULE_PRCFCT: {
	// This is for direct event scheduling implemented in the
	// entity calss; we should i) remove the handle so that the
	// user won't be able to cancel the event anymore, and ii)
	// invoke the user-defined callback function.
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_SCHEDULE_PRCFCT_EVENT);
#endif
	assert(current_event->process && current_event->prcfct);

	SSF_STATS_DIRECT_CALLBACKS++;

	// if the event is retractable, remove the handle to disable it
	if(current_event->sched_handle) {
	  current_event->process->_proc_owner->_ent_delete_schedule
	    (current_event->sched_handle);
	}

	// invoke the callback function
	(current_event->process->*(current_event->prcfct))
	  (current_event->usrdat);
	break;
      } // EVTYPE_SCHEDULE_PRCFCT

      case ssf_kernel_event::EVTYPE_TIMEOUT: {
	// This event means that a suspended process is timed out. The
	// process should be marked as being timed out and unblocked.
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_TIMEOUT_EVENT);
#endif
	Process* p = current_event->process; assert(p);
	assert(p->_proc_holdevt == current_event);
	assert(p->_proc_owner && p->_proc_owner->_ent_timeline == this); 

	// flag the process is timed out 
	p->_proc_timedout = true;

	// cut off the link to the current event to prevent
	// cancellation by the process.
	p->_proc_holdevt = 0;

	// if the process is also waiting on a set of static
	// in-channels, sign off; Or, if it's waiting on a set of
	// dynamic in-channels, insensitize it.
	if(p->_proc_static_expect) p->_proc_static_expect = false;
	else if(p->_proc_waiton) p->_proc_insensitize();

	// activate the process
	activate_process(p);
	break;
      } // EVTYPE_TIMEOUT 
      
      case ssf_kernel_event::EVTYPE_OUTCHANNEL: {
	// This event is scheduled at the timeline where a
	// outChannel::write() method is called. This event carries
	// the user event from the outchannel to the mapped
	// inchannels.
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_OUTCHANNEL_EVENT);
#endif
        outChannel* oc = current_event->outchannel; 
	assert(oc);

	// unlike before, we don't count this event
	SSF_STATS_KERNEL_EVENTS--;

#if PRIME_SSF_EMULATION
	if(oc->_oc_threadfct) {
	  // if the outchannel is a special one (e.g., attaching to a
	  // real-time device), we insert the event into the priority
	  // queue maintained by the channel. In case the user is
	  // writer thread is waiting for the event's arrival, we wake
	  // it up.
#if 1
	  ltime_t arise = simclock+oc->_oc_channel_delay;
	  ssf_kernel_event* ke = new ssf_kernel_event
	    (arise, ssf_kernel_event::EVTYPE_OUTCHANNEL);
	  ke->usrdat = current_event->usrdat->clone(); // a new copy to be thread-safe
	  ssf_thread_mutex_wait(&oc->_oc_realque_mutex);
	  oc->_oc_realque->push(ke);
	  if(oc->_oc_realque->size()==1) {
	    // we only send the signal to writer thread if this is the first event in queue
	    ssf_thread_cond_signal(&oc->_oc_realque_cond);
	  }
	  //printf("%u\n", oc->_oc_realque->size()); fflush(0);
	  ssf_thread_mutex_signal(&oc->_oc_realque_mutex);
#else // the experimental code below is problematic (can't use two queues)
	  ssf_kernel_event* ke = new ssf_kernel_event
	    (simclock, ssf_kernel_event::EVTYPE_OUTCHANNEL);
	  ke->usrdat = current_event->usrdat->clone(); // a new copy to be thread-safe
	  ssf_thread_mutex_wait(&oc->_oc_realque_mutex);
	  if(oc->_oc_realque) {
	    assert(oc->_oc_realque_tail && oc->_oc_realque_tail->context);
	    ssf_external_event* np = (ssf_external_event*)oc->_oc_realque_tail->context;
	    assert(np->next == 0);
	    np->next = oc->_oc_realque_tail = kevt;
	  } else {
	    assert(oc->_oc_realque_tail == 0);
	    oc->_oc_realque = oc->_oc_realque_tail = kevt;
	    ssf_thread_cond_signal(&oc->_oc_realque_cond);
	  }
	  ssf_thread_mutex_signal(&oc->_oc_realque_mutex);
#endif
	} else 
#endif /*PRIME_SSF_EMULATION*/  
        {
	  ltime_t arise = simclock+oc->_oc_channel_delay;
	  if(oc->_oc_timeline_list.size() > 0) {
	    // scan for all mapped timelines
	    for(SSF_OC_TML_VECTOR::iterator iter = 
		  oc->_oc_timeline_list.begin();
		iter != oc->_oc_timeline_list.end(); iter++) {
	      // discard the event if it's beyond the horizon
	      ltime_t first = arise+(*iter)->min_offset;
	      if(first > SSF_CONTEXT_ENDTIME) continue;

	      assert(first >= simclock); // obviously

	      // generate channel event (EVTYPE_CHANNEL for transferring
	      // between timelines, EVTYPE_CHANNEL for local delivery)
	      // and schedule it
	      if((*iter)->stargate) { // to a timeline not this
		ssf_kernel_event* ke = new ssf_kernel_event
		  (first, ssf_kernel_event::EVTYPE_CHANNEL,
		   current_event->usrdat);
		ke->entity_serialno = current_event->entity_serialno;
		ke->event_seqno = current_event->event_seqno;
		ke->localtlt = *iter;
		(*iter)->stargate->drop_message(ke);
	      } else { // to the same timeline
		ssf_kernel_event* ke = new ssf_kernel_event
		  (first, ssf_kernel_event::EVTYPE_INCHANNEL,
		   current_event->usrdat);
		assert((*iter)->remote_tlt);
		ke->entity_serialno = current_event->entity_serialno;
		ke->event_seqno = current_event->event_seqno;
		ke->mapnode = (*iter)->remote_tlt->ic_head;
		insert_event(ke);
	      }
	    }
	  }
	}
        break;
      } // EVTYPE_OUTCHANNEL

      case ssf_kernel_event::EVTYPE_INCHANNEL: {
	// This is an incoming message. Events that travel through
	// channels (from out-channels to in-channels) are only marked
	// by their arrival times at the destination timelines. For
	// all in-channels in a timeline that are mapped to the same
	// outchannel, there is only one such event in the event
	// list. Its timestamp is the simulation time at which the
	// user event first shows up at an in-channel. After
	// processing the arrival at the first in-channel, the same
	// event will be scheduled to represent the arrival at the
	// next in-channel. It saves the memory as we reuse the event
	// (i.e., user event, not kernel event), which "snakes"
	// through all the mapped in-channels at the same timeline.
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_INCHANNEL_EVENT);
#endif
	ssf_map_node* mapnode = current_event->mapnode;
	assert(mapnode);
	for(;;) { // process all simultaneous arrivals
	  inChannel* ic = mapnode->ic;
	  assert(ic && ic->_ic_owner &&
		 ic->_ic_owner->_ent_timeline == this);
	
	  // activate processes blocking on the in-channel if there's any
	  ic->_ic_schedule_arrival(current_event);

	  ssf_map_node* nxtnode = mapnode->next; // move on to the next map-node
	  if(nxtnode) {
	    ltime_t offset = nxtnode->map_delay-mapnode->map_delay;
	    if(offset > SSF_LTIME_ZERO) {
	      // if next inchannel in the list needs further delay, reschedule 
	      // another arrival and break out.
	      ssf_kernel_event* kevt = new ssf_kernel_event
		(simclock+offset, ssf_kernel_event::EVTYPE_INCHANNEL, 
		 current_event->usrdat);
	      kevt->mapnode = nxtnode;
	      ic->_ic_owner->_ent_insert_event(kevt);
	      break;
	    } else mapnode = nxtnode; // same mapping delay, continue the scan
	  } else break; // no more inchannels on the list, we are done
	}
	break;
      } // EVTYPE_INCHANNEL

      case ssf_kernel_event::EVTYPE_SEMSIGNAL: {
#if PRIME_SSF_INSTRUMENT
        setAction(ACTION_SEMSIGNAL_EVENT);
#endif
	// This event is thrown upon by a process trying to wake up
	// another one. Simply activate the process.
	Process* p = current_event->process;
	assert(p && p->_proc_owner && 
	       p->_proc_owner->_ent_timeline == this);
	assert(!p->_proc_static_expect);
	assert(!p->_proc_waiton);
	assert(!p->_proc_holdevt);

	// activate the process
	activate_process(p);
	break;
      } // EVTYPE_SEMSIGNAL

      default: {
	char msg[256];
	sprintf(msg, "unknown type=%d", current_event->type);
	ssf_throw_exception(ssf_exception::kernel_event, msg);
      }}

      // reclaim the kernel event
      delete current_event;

#if 0
      // Someone may want to interrupt the session; if so, we stop
      // processing further events, but we still have to finish
      // processing current events: calling processes that have been
      // activated already. This means, if the user processes are
      // taking longer than expected, we'll see possible missed
      // real-time deadline here. We're only able to allow interrupts
      // at the event level.
      if(/*!emuable &&*/ !interrupted) {
	// because it's possible to change ltime_wallclock_ratio value
	// at run-time, not letting an "emuable" timeline to check
	// interrupt requests may cause the timeline not being able to
	// allow the ratio to be changed at all. this is
	// undesireable. For this reason, I commented out the first
	// condition, knowing that this may make emulation timeline
	// also interruptable. -jason.
	interrupted = SSF_CONTEXT_UNIVERSE->interrupt_requested();
	interrupted_called = 1;
      }
#endif
      if(interrupted) break;
    }

#if PRIME_SSF_INSTRUMENT
    setAction(ACTION_EVENT_POSTPROCESSING);
#endif

    // execute all activated processes one by one
    Process* p = get_active_process(); 
    Process* q = 0;
    while(p) {
      p->_proc_execute();
      if(p != q) {
	SSF_STATS_PROCESS_CONTEXTS++;
	clear_contextual_memory();
	q = p;
      }
      p = get_active_process();
    }

    // clean up arrival events from active in-channels
    if(!active_inchannels.empty()) {
      for(SSF_TML_IC_VECTOR::iterator iter = active_inchannels.begin();
	  iter != active_inchannels.end(); iter++) 
	(*iter)->_ic_cleanup_active_events();
      active_inchannels.clear();
    }

    #define SOMETHRESHOLD 1000000 // 1 msec
    if(!interrupted && emuable) {
      ltime_t nxt = next_due_time();
      if(nxt - last_realtime_check_io > SOMETHRESHOLD) {
	last_realtime_check_io = nxt;
	interrupted = SSF_CONTEXT_UNIVERSE->interrupt_requested(nxt);
	if(interrupted) break;
      }
    }
  }

#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_OTHER);
  SSF_CONTEXT_UNIVERSE->setAction(prime::ssf::ssf_universe::ACTION_OTHER);
#endif

  // in case there's no explicit context switch during the session
  clear_contextual_memory();

#if 0
  // make sure this is called at least once for this session
  if(!interrupted_called) SSF_CONTEXT_UNIVERSE->interrupt_requested();
#endif

  SSF_CONTEXT_PROCESSING_TIMELINE_ASSIGN(0);

  return interrupted;
}

char* ssf_timeline::new_contextual_memory(size_t size)
{
  char* p = (char*)ssf_quickobj::quick_new(size);
  context_buffers.push_back(SSF_MAKE_PAIR(size, p));
  return p;
}

void ssf_timeline::clear_contextual_memory()
{
  if(!context_buffers.empty()) {
    for(SSF_TML_INTBUF_VECTOR::iterator iter = context_buffers.begin();
	iter != context_buffers.end(); iter++)
      ssf_quickobj::quick_delete((*iter).second, (*iter).first);
    context_buffers.clear();
  }
}

void ssf_timeline::insert_event(ssf_kernel_event* kevt, int force_in)
{
  // event with timestamp beyond the current decade is managed by the
  // universe; depending on how decade is defined, one can use the
  // bin-queue data structure to hold future events for the sake of
  // scalability.

  // KNOWN PROBLEM: it's possible that, before the universe's binque
  // is settled, the redistribution of events (in
  // ssf_universe::expand_epoch) from the universe's binque (all in
  // ssf_bin_queue::big_bin) to timeline event lists may cause infinite
  // loops. We added the second argument so that the event is inserted
  // into the timeline's event list no matter whether its timestamp is
  // beyond the current decade. A problem with this is that we can no
  // longer guarantee that future events are kept away from the
  // timeline's event list. This could cause problems if we want do
  // dynamic realignment of entities.

  assert(kevt);
  if(force_in || kevt->time < SSF_CONTEXT_DECADE) {
    if(kevt->time == simclock) {
      simevents.push_back(kevt);
      /*printf("%lld: timeline %d insert simultaneous event\n", simclock, serialno());*/
    } else {
      evtlist.push(kevt);
      /*printf("%lld: timeline %d insert event of time %lld\n", simclock, serialno(), kevt->time);*/
    }
  } else {
    SSF_CONTEXT_UNIVERSE->insert_event(kevt);
    /*printf("%lld: timeline %d UNIVERSE insert event of time %lld\n", simclock, serialno(), kevt->time);*/
  }
}

void ssf_timeline::cancel_event(ssf_kernel_event* kevt)
{
  // event with timestamp beyond the current decade is managed by the
  // universe; on the other hand, if the event's timestamp is the same
  // as the simulation clock, we don't know whether the event has been
  // offloaded to the list of simulataneous event or not. Best we can
  // do is to mark it as cancelled.
  assert(kevt);
  if(kevt->time >= SSF_CONTEXT_DECADE)
    SSF_CONTEXT_UNIVERSE->cancel_event(kevt);
  else evtlist.remove(kevt);
}

void ssf_timeline::activate_process(Process* p)
{
  // we should ignore processes that has already been put onto the
  // ready list; they were marked as running.
  if(p->_proc_state != Process::PROCSTATE_RUNNING) {
    p->_proc_state = Process::PROCSTATE_RUNNING;
    active_processes.push_back(p);
  }
}

Process* ssf_timeline::get_active_process()
{
  if(active_processes.empty()) return 0;
  else {
    Process* p = active_processes.front();
    // clean up the waiting state of the process
    if(p->_proc_static_expect) // if it's static blocking, mark it off
      p->_proc_static_expect = 0;
    else if(p->_proc_waiton) // if it's dynamic blocking, insensitize it 
      p->_proc_insensitize();
    return p;
  }
}

void ssf_timeline::deactivate_process(int newstate)
{
  assert(!active_processes.empty());

  // clean up the state of the process
  Process* p = active_processes.front();
  p->_proc_cleanup_active_inchannels();
  p->_proc_timedout = false;
  p->_proc_state = newstate;

  // remove the process from the ready queue
  active_processes.pop_front();
}

void ssf_timeline::wrapup()
{
  for(SSF_TML_ENT_SET::iterator iter = entities.begin();
      iter != entities.end(); iter++) {
    // copy is needed
    SSF_ENT_PRC_SET pset((*iter)->_ent_processes);
    for(SSF_ENT_PRC_SET::iterator piter = pset.begin();
	piter != pset.end(); piter++) {
      (*piter)->_proc_terminate();
    }
    (*iter)->wrapup();
  }
}

#if PRIME_SSF_EMULATION
ltime_t ssf_timeline::next_due_time()
{
  if(!emuable || evtlist.empty()) return safetime;
  else return evtlist.top()->time<safetime ? evtlist.top()->time : safetime;
}
#endif

#if PRIME_SSF_INSTRUMENT
/**
 * \brief Dumps instrumentation data to the output stream.
 *
 * This includes amount of time spent performing each type of action and, when
 * waiting on input, the amount of time spent waiting on input from each other
 * timeline.
 */
void ssf_timeline::dumpInstrumentationData(int whereami) {

  fprintf(SSF_USE_SHARED_RO(outfile), "[%-2d]{%-2d}(%-2d) alignment                          : %s\n", 
          SSF_USE_SHARED_RO(whoami), serialno(), whereami, 
          alignmentName->c_str());

  ssf_machine_time total = 0;
  for (int i=1; i<ssf_timeline::ACTION_TOTAL; i++) {
    if (actionCount[i]>0) {

      // timings for event loops are sampled, have to adjust for that
      if (i <= ACTION_EVENT_POSTPROCESSING &&
          i >= ACTION_DETERMINING_NEXT_EVENT) {
        actionTiming[i]*=16;
      }
      
      fprintf(SSF_USE_SHARED_RO(outfile), "[%-2d]{%-2d}(%-2d) %-35s:  ", 
	      SSF_USE_SHARED_RO(whoami), serialno(), whereami,
              ssf_timeline::actionDescriptions[i]);
#if defined(HAVE_GETHRTIME)
      fprintf(SSF_USE_SHARED_RO(outfile), "%10d times (%.3f seconds)\n",
              actionCount[i], actionTiming[i]/1e9);
#else
      fprintf(SSF_USE_SHARED_RO(outfile), "%10d times (%.3f seconds)\n",
              actionCount[i], actionTiming[i]/1e6);
#endif
    }
    total+=actionTiming[i];

    // if displaying timing for "waiting on input", break it down
/*    if (i==ACTION_WAITING_ON_INPUT) {
      for (map<prime::ssf::uint32, ssf_machine_time>::iterator iter = inputWaitTimes.begin();
           iter != inputWaitTimes.end(); iter++) {
        fprintf(SSF_USE_SHARED_RO(outfile), "[%d]        from timeline %-30d:  ", 
	        SSF_USE_SHARED_RO(whoami), (*iter).first);
#if defined(HAVE_GETHRTIME)
        if ((*iter).second>1e9) fprintf(SSF_USE_SHARED_RO(outfile), "%.2f seconds\n",
                                (*iter).second/1e9);
        else fprintf(SSF_USE_SHARED_RO(outfile), "%.0f nanoseconds\n",
             (*iter).second);
#else
        if ((*iter).second>1e6) fprintf(SSF_USE_SHARED_RO(outfile), "%.2f seconds\n",
                                (*iter).second/1e6);
        else fprintf(SSF_USE_SHARED_RO(outfile), "%.0f microseconds\n",
             (*iter).second);
#endif
      }
    }*/
  }

  for (map<prime::ssf::uint32, int>::iterator iter = messagesSent.begin();
       iter != messagesSent.end(); iter++) {
#if PRIME_SSF_DISTSIM
    int machid = ssf_universe::lpid_to_machid((*iter).first);
    fprintf(SSF_USE_SHARED_RO(outfile), "[%-2d]{%-2d}(%-2d) messages sent to [%-2d]{%-2d}        :  %-10d\n", 
	  SSF_USE_SHARED_RO(whoami), serialno(), whereami, 
          machid, (*iter).first, (*iter).second);
#else
    fprintf(SSF_USE_SHARED_RO(outfile), "[%-2d]{%-2d}(%-2d) messages sent to {%-2d}              :  %-10d\n", 
	  SSF_USE_SHARED_RO(whoami), serialno(), whereami,
          (*iter).first, (*iter).second);
#endif
  }
#if 0
  for(SSF_TML_ENT_SET::iterator iter = entities.begin();
      iter != entities.end(); iter++) {
    (*iter)->dumpInstrumentationData();
  }
/*    std::cerr << "    total : \t\t\t";
#if defined(HAVE_GETHRTIME)
  if (total>1e9) std::cerr << (total/1e9) << " seconds";
  else std::cerr << total << " nanoseconds";
#else
  if (total>1e6) std::cerr << (total/1e6) << " seconds";
  else std::cerr << total << " microseconds";
#endif
  std::cerr << std::endl;*/
#endif
}
  
  
void ssf_timeline::setAction(int action) {
  ssf_machine_time now = -1;

  // time last action if it was valid
  if (currentAction != ACTION_INVALID) {
    now = ssf_get_wall_time();
    actionTiming[currentAction]+=now;
    actionTiming[currentAction]-=lastTimeMark;
/*    if (currentAction == ACTION_RECIEVING_MESSAGES) {
      inputWaitTimes[waitingOnInputFrom]+=now;
      inputWaitTimes[waitingOnInputFrom]-=lastTimeMark;
    }*/
  }

  // count new action
  actionCount[action]+=1;
  
  // For actions in the event loop we can't time every action without
  // overtaxing the system clock.  Therefore we skip timing all but a
  // sixteenth of the event loop actions.
  if (((actionCount[action]>>4)<<4)==actionCount[action] ||
      action > ACTION_EVENT_POSTPROCESSING ||
      action < ACTION_DETERMINING_NEXT_EVENT) {
    if (now == -1) now = ssf_get_wall_time();
    lastTimeMark = now;
    currentAction = action;
  } else {
    currentAction = ACTION_INVALID; // not timing this one
  }
  
  // set action for processor as well
  if (action <= ACTION_EVENT_POSTPROCESSING) {
    SSF_CONTEXT_UNIVERSE->setAction(action, now);
  }
}
#endif // instrumentation

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
