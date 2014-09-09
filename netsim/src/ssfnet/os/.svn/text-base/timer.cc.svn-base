/**
 * \file timer.cc
 * \brief Source file for the Timer class.
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

#include "os/logger.h"
#include "os/timer.h"
#include "os/protocol_session.h"
#include "os/community.h"
#include "net/host.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(Timer);

Timer::Timer(ProtocolSession* s) :
  sess(s), comm(s ? s->inHost()->getCommunity() : 0),
  delay(0), event_handle(0) {}

Timer::Timer(Community* comm) :
  sess(0), comm(comm), delay(0), event_handle(0) {}

Timer::Timer(ProtocolSession* s, VirtualTime d) :
  sess(s), comm(s ? s->inHost()->getCommunity() : 0),
  delay(d), event_handle(0)
{
  if(delay < 0) LOG_ERROR("negative delay used to create timer.");
}

Timer::~Timer()
{
  if(event_handle) {
    if(event_handle->is_running())
      LOG_ERROR("attempting to reclaim running timer.");
    delete event_handle;
  }
}

void Timer::set()
{
  if(event_handle && event_handle->is_running())
    LOG_ERROR("attempting to set running timer.");
  if(sess && !comm)
    comm = sess->inHost()->getCommunity();
  start_timer();
}

void Timer::set(VirtualTime d)
{
  // set delay and start the timer
  if(event_handle && event_handle->is_running())
    LOG_ERROR("attempting to set running timer.");
  if(d < 0){
	  LOG_WARN("set timer with negative delay: " << d.second() << " seconds.");
	  ssfnet_throw_exception(SSFNetException::other_exception,"...");
  }
  delay = d;
  if(sess && !comm)
    comm = sess->inHost()->getCommunity();
  start_timer();
}

void Timer::cancel()
{
  // effective only when it's running
  if(event_handle && event_handle->is_running())
    stop_timer();
}

void Timer::setDelay(VirtualTime d)
{
  if(event_handle && event_handle->is_running())
    LOG_ERROR("attempting to modify running timer.");
  if(d < 0) LOG_ERROR("set timer with negative delay: " << d.second() << " seconds.");
  delay = d;
}

bool Timer::isCancelled()
{
  return bool(!event_handle || !event_handle->is_running());
}

VirtualTime Timer::firetime()
{
  if(event_handle && event_handle->is_running())
    return start_time + delay;
  return VirtualTime(0);
}

void Timer::timer_expires(prime::ssf::ssf_timer* tmr)
{
  assert(tmr && ((TimerEvent*)tmr)->timer);
  ((TimerEvent*)tmr)->timer->callback();
}

void Timer::start_timer()
{
  if(!comm)
    LOG_ERROR("premature timer: timer has not been attached to a community.");

  // note down the current time and time for the timer to expire
  start_time = comm->now();

  if(!event_handle) {
    event_handle = new TimerEvent(comm, timer_expires, this);
    assert(event_handle);
  }
  event_handle->schedule(delay);
}

void Timer::stop_timer()
{
  assert(event_handle);
  event_handle->cancel();
}

} // namespace ssfnet
} // namespace prime
