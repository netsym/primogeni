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

#include <stdio.h>
#include "ssf.h"
#include "sim/composite.h"
#include "sim/kernelevt.h"

#if PRIME_SSF_MANAGE_KERNEL_EVENTS
#include "mac/segments.h"

SSF_DECLARE_PRIVATE(void*, kevt_pool);

#define SSF_KEVT_UPSIZE ((sizeof(ssf_kernel_event)+SSF_CACHE_LINE_SIZE-1)/SSF_CACHE_LINE_SIZE*SSF_CACHE_LINE_SIZE)
#define SSF_KEVT_POOLSZ 128
#endif

// kernel events are counted for flow control.
SSF_DECLARE_PRIVATE(long, event_watermark);

namespace prime {
namespace ssf {

const char* ssf_kernel_event::_event_type_descriptions[EVTYPE_TOTAL] = {
"invalid event type",
"cancelled event",
"new entity event",
"new process event",
"time out event",
"outchannel event",
"channel event",
"inchannel event",
"timer event",
"ent schedule event",
"prc schedule event",
"semaphore signal event"
};

ssf_kernel_event::ssf_kernel_event(ltime_t evtime, int evtype, Event* evt) :
  time(evtime), context(0), type(evtype)
{
  SSF_USE_PRIVATE(kevt_watermark)++;
  if(evt) usrdat = evt->_evt_sys_reference();
  else usrdat = 0;
}

ssf_kernel_event::~ssf_kernel_event()
{
  assert(0 < type && type < EVTYPE_TOTAL);
  assert(context == 0);
  SSF_USE_PRIVATE(kevt_watermark)--;
  if(usrdat) usrdat->_evt_sys_unreference();
  type = EVTYPE_NONE; // for sanity check
}

void ssf_kernel_event::cancel()
{
  assert(0 < type && type < EVTYPE_TOTAL);
  switch(type) {
  case EVTYPE_TIMEOUT:
    assert(process);
    process->_proc_holdevt = 0;
    break;
  case EVTYPE_SCHEDULE_ENTFCT:
    assert(entity);
    if(sched_handle > 0) // is retractable
      entity->_ent_delete_schedule(sched_handle);
    break;
  case EVTYPE_SCHEDULE_PRCFCT:
    assert(process);
    if(sched_handle > 0) // is retractable
      process->_proc_owner->_ent_delete_schedule(sched_handle);
    break;
  case EVTYPE_TIMER:
    assert(timer);
    timer->_tmr_schedevt = 0;
    break;
  }
  if(usrdat) { usrdat->_evt_sys_unreference(); usrdat = 0; }
  type = EVTYPE_CANCELLED;
}

ssf_timeline* ssf_kernel_event::owner_timeline()
{
  switch(type) {
  case EVTYPE_CANCELLED: break;
  case EVTYPE_NEWENTITY: return entity->_ent_timeline;
  case EVTYPE_NEWPROCESS: return process->_proc_owner->_ent_timeline;
  case EVTYPE_OUTCHANNEL: return outchannel->_oc_owner->_ent_timeline;
  case EVTYPE_CHANNEL: return localtlt->oc->_oc_owner->_ent_timeline;
  case EVTYPE_INCHANNEL: return mapnode->ic->_ic_owner->_ent_timeline;
  case EVTYPE_TIMEOUT: return process->_proc_owner->_ent_timeline;
  case EVTYPE_SCHEDULE_ENTFCT: return entity->_ent_timeline;
  case EVTYPE_SCHEDULE_PRCFCT: return process->_proc_owner->_ent_timeline;
  case EVTYPE_TIMER: return timer->_tmr_focus->_ent_timeline;
  case EVTYPE_SEMSIGNAL: return process->_proc_owner->_ent_timeline;
  //case EVTYPE_APPTMT: return __apptmt->oc->__owner->__timeline;
  //case EVTYPE_APPTMT_COND: return __mapnode->ic->__owner->__timeline;
  default: {
    char msg[256];
    sprintf(msg, "type=0x%x", type);
    ssf_throw_exception(ssf_exception::kernel_event, msg);
  }}
  return 0;
}

#if PRIME_SSF_MANAGE_KERNEL_EVENTS

// NOTE: this simple scheme here could cause memory imbalance among
// the threads

void* ssf_kernel_event::operator new(size_t sz)
{
  assert(sz == sizeof(ssf_kernel_event));
  if(!SSF_USE_PRIVATE(kevt_pool)) {
    char* p = new char[SSF_KEVT_UPSIZE*SSF_KEVT_POOLSZ];
    SSF_USE_PRIVATE(kevt_pool) = p;
    char* q = p + SSF_KEVT_UPSIZE;
    for(int i=0; i<SSF_KEVT_POOLSZ-1; i++) {
      ((ssf_kernel_event*)p)->kevt_nxtpool = (ssf_kernel_event*)q;
      p = q;
      q += SSF_KEVT_UPSIZE;
    }
    ((ssf_kernel_event*)p)->kevt_nxtpool = 0;
  }

  ssf_kernel_event* ptr = (ssf_kernel_event*)SSF_USE_PRIVATE(kevt_pool);
  SSF_USE_PRIVATE(kevt_pool) = ptr->kevt_nxtpool;
  return ptr;
}

void ssf_kernel_event::operator delete(void* p)
{
  if(p) {
    ((ssf_kernel_event*)p)->kevt_nxtpool = (ssf_kernel_event*)SSF_USE_PRIVATE(kevt_pool);
    SSF_USE_PRIVATE(kevt_pool) = p;
  }
}

#endif /*PRIME_SSF_MANAGE_KERNEL_EVENTS*/

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
