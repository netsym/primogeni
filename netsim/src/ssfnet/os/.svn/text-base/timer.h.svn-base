/**
 * \file timer.h
 * \brief Header file for the Timer class.
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

#ifndef __TIMER_H__
#define __TIMER_H__

#include "ssf.h"
#include "os/virtual_time.h"

namespace prime {
namespace ssfnet {

class Community;
class ProtocolSession;
class Timer;

/**
 * \internal
 * \brief An SSF timer that implements direct-event scheduling.
 *
 * TimerEvent is an internal event used for direct-event scheduling to
 * implement the timer. It contains a reference to the SSF timer it
 * uses to implement direct-event scheduling.
 */
class TimerEvent : public prime::ssf::ssf_timer {
  friend class Timer;
 protected:
  Timer* timer;
  TimerEvent(prime::ssf::Entity* ent,
	     void (*cb)(prime::ssf::ssf_timer*), Timer* tmr) :
    prime::ssf::ssf_timer(ent, cb), timer(tmr) {}
};

/**
 * \brief A timer used by a protocol session for time advancement.
 *
 * It's a class with a callback method, which the user can
 * overload. The callback method will be called when it times
 * out. Each timer is associated with a protocol session. Or, we may
 * say that a timer is <i>owned</i> by a protocol session. Through the
 * owner protocol session, the timer registers itself with the host
 * which is in charge of managing all timers and prepares for the case
 * where the host moves about and changes its SSF entity owner (a
 * possibility in a wireless mobile network environment).  This Timer
 * class is an abstract class. That is, it cannot be used
 * directly. One has to derive the class and overload the callback
 * function. Internally, a timer is implemented via SSF direct event
 * scheduling mechanism.
 */
class Timer {
 public:
  /**
   * Constructor of a timer. A reference to the owner protocol session
   * is given as the first argument. This constructor does not set the
   * delay value. It should be understood that the delay is zero by
   * default. The timer is not running when it has just been
   * created. One needs to 'set' the timer for it to start ticking.
   */
  Timer(ProtocolSession* sess);

  /**
   * Constructor of a timer with preset delay value. A reference
   * to the owner protocol session is given as the first argument.
   * The delay value is passed as the second argument. Note that
   * the delay value is not counted down. It's a constant inside
   * the timer and can be reused, despite the timer's current
   * state. The timer is not running when it is just created. One
   * needs to 'set' the timer for it to start ticking.
   */
  Timer(ProtocolSession* sess, VirtualTime delay);

  /**
   *  Alternative constructor that does not specify an owner protocol.
   *  Thus, the timer can be put outside the protocol graph, but also
   *  note that one can no longer assume that there is an associated
   *  protocol session in this case. So, getSession() will return
   *  NULL.
   */
  Timer(Community* ent);

  /** Reclaim the timer. */
  virtual ~Timer();

  /**
   * This is the callback function; the method is called when the
   * time fires, i.e. when the specified delay time has elapse
   * since the timer is set. The user should overload this method
   * in the derived class.
   */
  virtual void callback() = 0;

  /**
   * This method sets the timer to start ticking. The timer uses
   * the delay value previous set. The callback method is expected
   * to be called once the timer expires.
   */
  void set();

  /**
   * This method sets the timer to start ticking with the given
   * delay. Note that the delay value is not counted down. It's a
   * constant inside the timer and can be reused despite the timer's
   * current state. The callback method is expected to be called once
   * the timer expires.
   */
  void set(VirtualTime delay);

  /**
   * Cancel the timer, if it's running. The method has no effect if
   * the timer is not running. A timer can be used over and over
   * again. The delay value of the timer will be treated as a constant
   * and will not change unless it is reset explicitly.
   */
  void cancel();

  /**
   * Set the delay value of the timer. The method will overwrite
   * the delay value set previously. It is an error to set the
   * delay value while the timer is running.
   */
  void setDelay(VirtualTime delay);

  /** Return the current delay value. */
  VirtualTime getDelay() { return delay; }

  /**
   * Return the owner protocol session. The return value could be NULL
   * if the timer is not associated with any protocol session. Note
   * that if it does, the ownership of a timer is irrevocable.
   */
  ProtocolSession* getSession() { return sess; }

  /* get teh community */
  Community* getCommunity() { return comm; }

  /**
   * Has the timer being canceled? A timer is either canceled
   * (i.e. not running) or running.
   */
  bool isCancelled();

  /**
   * Is the timer running? A timer is either canceled (i.e. not
   * running) or running.
   */
  bool isRunning() { return !isCancelled(); }

  /**
   * If the timer is running, return the time the timer is scheduled
   * to time out. If the timer is not running, return 0.
   */
  VirtualTime firetime();

 protected:
  /** Owner protocol session. Ownership is irrevocable. */
  ProtocolSession* sess;

  /** Owner timeline. */
  Community* comm;

  /** Delay value, the length of time before a timer expires. */
  VirtualTime delay;

  /** Time when the timer starts ticking. This value is remembered in
      case the timer migrates from one timeline to another, a new
      timer event can be rescheduled properly. */
  prime::ssf::ltime_t start_time;

  /** Internally, the timer is implemented as a TimerEvent. */
  TimerEvent* event_handle;

  /** Start running the timer. */
  void start_timer();

  /** Stop the timer. */
  void stop_timer();

  /** This is the callback function when the timer expires. */
  static void timer_expires(prime::ssf::ssf_timer*);
};

} // namespace ssfnet
} // namespace prime

#endif /*__TIMER_H__*/
