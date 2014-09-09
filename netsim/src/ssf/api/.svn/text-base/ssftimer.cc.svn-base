/*
 * ssftimer :- timer for event-oriented simulation.
 *
 * A timer encapsulates a callback function, which is associated with
 * an entity object. The callback function can either be a member
 * function of the entity object or a regular C++ function with a
 * designated signature. The timer can be scheduled to fire off at a
 * future simulation time. It can also be cancelled before the timer
 * goes off. If the timer goes off, the system will call the
 * associated callback function with an argument being the pointer to
 * the timer object. The user can design a derived class of the
 * ssf_timer class to carry user data so that when the timer goes off,
 * the callback function will be able to extract the data.
 */

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

ssf_timer::ssf_timer(Entity *focus, void (Entity::*entfct)(ssf_timer*)) :
  _tmr_focus(focus), _tmr_entfct(entfct), _tmr_regfct(0),
  _tmr_state(TIMER_STATE_UNTOUCHED), _tmr_firetime(SSF_LTIME_MINUS_INFINITY),
  _tmr_schedevt(0) {
  if(!focus) ssf_throw_exception(ssf_exception::timer_entity);
  if(!entfct) ssf_throw_exception(ssf_exception::timer_callback);
}

ssf_timer::ssf_timer(Entity *focus, void (*regfct)(ssf_timer*)) :
  _tmr_focus(focus), _tmr_entfct(0), _tmr_regfct(regfct),
  _tmr_state(TIMER_STATE_UNTOUCHED), _tmr_firetime(SSF_LTIME_MINUS_INFINITY),
  _tmr_schedevt(0) {
  if(!focus) ssf_throw_exception(ssf_exception::timer_entity);
  if(!regfct) ssf_throw_exception(ssf_exception::timer_callback);
}

ssf_timer::ssf_timer(Entity *focus) :
  _tmr_focus(focus), _tmr_entfct(0), _tmr_regfct(0),
  _tmr_state(TIMER_STATE_UNTOUCHED), _tmr_firetime(SSF_LTIME_MINUS_INFINITY),
  _tmr_schedevt(0) {
  if(!focus) ssf_throw_exception(ssf_exception::timer_entity);
}

ssf_timer::~ssf_timer() {
  if(_tmr_state == TIMER_STATE_RUNNING && _tmr_schedevt)
    ssf_throw_exception(ssf_exception::timer_running);
}

void ssf_timer::schedule(ltime_t delay) {

  if(delay < SSF_LTIME_ZERO)
    ssf_throw_exception(ssf_exception::timer_delay, "schedule()");
  // It's possible the timer is scheduled out of context. For example,
  // one can schedule a timer in the callback function of a global
  // barrier (e.g., in ssf_set_recurring_barrier function), during
  // which the processing time is set to be NULL.
  if(((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)) &&
     ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)) != _tmr_focus->_ent_timeline)
    ssf_throw_exception(ssf_exception::timer_align, "schedule()");
  if(_tmr_state == TIMER_STATE_RUNNING)
    ssf_throw_exception(ssf_exception::timer_resched);

  _tmr_firetime = delay+SSF_USE_PRIVATE(now);
  _tmr_state = TIMER_STATE_RUNNING;
  /*printf("%lld: timer sched for %lld\n", SSF_USE_PRIVATE(now), _tmr_firetime);*/

  ssf_kernel_event* kevt = new ssf_kernel_event
    (_tmr_firetime, ssf_kernel_event::EVTYPE_TIMER);
  kevt->timer = this;
  _tmr_schedevt = _tmr_focus->_ent_insert_event(kevt);
}

void ssf_timer::reschedule(ltime_t delay)
{
  if(delay < SSF_LTIME_ZERO)
    ssf_throw_exception(ssf_exception::timer_delay, "reschedule()");
  if(((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)) &&
     ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)) != _tmr_focus->_ent_timeline)
    ssf_throw_exception(ssf_exception::timer_align, "reschedule()");

  if(_tmr_state == TIMER_STATE_RUNNING) cancel();

  _tmr_firetime = delay+SSF_USE_PRIVATE(now);
  _tmr_state = TIMER_STATE_RUNNING;
  /*printf("%lld: timer resched for %lld\n", SSF_USE_PRIVATE(now), _tmr_firetime);*/

  ssf_kernel_event* kevt = new ssf_kernel_event
    (_tmr_firetime, ssf_kernel_event::EVTYPE_TIMER);
  kevt->timer = this;
  _tmr_schedevt = _tmr_focus->_ent_insert_event(kevt);
}

void ssf_timer::cancel()
{
  if(((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)) &&
     ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)) != _tmr_focus->_ent_timeline)
    ssf_throw_exception(ssf_exception::timer_align, "cancel()");

  if(_tmr_state == TIMER_STATE_RUNNING) {
    if(_tmr_schedevt)
      _tmr_focus->_ent_cancel_event(_tmr_schedevt);
    _tmr_state = TIMER_STATE_CANCELLED;
  }
}

}; // namespace ssf
}; // namespace prime

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
