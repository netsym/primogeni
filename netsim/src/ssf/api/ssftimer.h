/**
 * \file ssftimer.h
 * \brief SSF timer for event-oriented simulation world-view.
 *
 * This header file contains the definition of the SSF timer class
 * (prime::ssf::ssf_timer) that is provided to the user for the
 * event-oriented world-view.
 */

#ifndef __PRIME_SSF_SSFTIMER_H__
#define __PRIME_SSF_SSFTIMER_H__

#ifndef __PRIME_SSF_H__
#error "ssftimer.h can only be included by ssf.h"
#endif

namespace prime {
namespace ssf {


/**
 * \brief The SSF timer for event-oriented simulation.
 *
 * An SSF timer encapsulates a callback function, which is associated
 * with an SSF Entity object. The callback function can either be a
 * member function of the entity object or a regular C++ function with
 * a designated signature. The timer can be scheduled to fire off at a
 * future simulation time. It can also be cancelled before the timer
 * goes off. If the timer goes off, the system will call the
 * associated callback function with a pointer to the timer object as
 * the function argument. The user can design a derived class of the
 * ssf_timer class to carry meaningful user data so that when the
 * timer goes off, the callback function will be able to extract the
 * data.
 */
class ssf_timer {
public:
  /**
   * \brief The constructor using an entity method as the callback function.
   * \param owner points to the owner entity of this timer.
   * \param callback points to the callback method of the owner entity.
   *
   * The constructor for the timer needs a pointer to the entity
   * object with which the timer will be permanently associated. The
   * callback function must take as argment a pointer to the ssf_timer
   * object. The callback function is actually a pointer to the Entity
   * class method. For example, if \b myent is a class derived from
   * Entity and the callback function is a method called \b foo in the
   * myent class, the timer can be created using:
\verbatim
new ssf_timer(myent, (void (Entity::*)(ssf_timer*))&myent::foo));
\endverbatim
   *
   */
  ssf_timer(Entity* owner, void (Entity::*callback)(ssf_timer*));

  /**
   * \brief The constructor using a regular callback function.
   * \param owner points to the owner entity of this timer.
   * \param callback points to the callback function.
   *
   * The constructor for the timer needs a pointer to the entity
   * object with which the timer will be permanently associated. The
   * callback function must take as argment a pointer to the ssf_timer
   * object.
   */
  ssf_timer(Entity* owner, void (*callback)(ssf_timer*));
  
  /**
   * \brief The constructor using the virtual callback() method.
   * \param owner points to the owner entity of this timer.
   *
   * The constructor for the timer needs only a pointer to the entity
   * object with which the timer will be permanently associated. Upon
   * firing, the timer will invoke the callback() method of this
   * object.
   */
  ssf_timer(Entity* owner);

  /**
   * \brief The destructor.
   *
   * Attmpting to delete a running timer is an error. The user should
   * first cancel the timer before deleting it, if the timer has been
   * scheduled.
   */
  virtual ~ssf_timer();

  /**
   * \brief Schedule the timer to go off after the specified delay.
   * \param delay is the delay time (from now) before the timer goes off
   *
   * This method schedules the timer to go off after the given delay
   * time. Attempting to schedule a timer that has already been
   * scheduled (i.e., the timer is running) is an error. In this case,
   * the user should call reschedule().
   */
  void schedule(ltime_t delay);

  /**
   * \brief Reschedule the timer to go off after the specified delay.
   * \param delay is the delay time (from now) before the timer goes off
   *
   * This method is similar to the schedule() method: it schedules the
   * timer to go off after 'delay' time. The only difference is that,
   * if the timer is running at the time this function is called, the
   * previously scheduled time will be cancelled.
   */
  void reschedule(ltime_t delay);

  /**
   * \brief Cancel the timer.
   *
   * The method cancels the timer that has been schedule to go off in
   * the future. If the timer is not running, the function is simply a
   * no-op.
   */
  void cancel();
  
  /// \brief Check whether the timer has just been created.
  boolean is_untouched() 
    { return (_tmr_state == TIMER_STATE_UNTOUCHED); }

  /// \brief Check whether the timer has just been cancelled.
  boolean is_cancelled()
    { return (_tmr_state == TIMER_STATE_CANCELLED); }

  /// \brief Check whether the timer has just gone off.
  boolean is_finished()
    { return (_tmr_state == TIMER_STATE_FINISHED);  }

  /// \brief Check whether the timer is being scheduled to go off.
  boolean is_running()
    { return (_tmr_state == TIMER_STATE_RUNNING);   }

  /** \brief Return the owner of the timer. */
  Entity* owner()  { return _tmr_focus; }

  /**
   * \brief Returns the scheduled fire time of the timer if the timer
   * is running; Otherwise, it's undefined.
   */
  ltime_t fire_time()  { return _tmr_firetime; }

  /**
   * \brief This is the callback method if no callback is appointed by
   * the constructor.
   *
   * This method does nothing. The derived class id expected to
   * override this method.
   */
  virtual void callback() {}

protected:
  enum { 
    TIMER_STATE_UNTOUCHED = 0, 
    TIMER_STATE_RUNNING   = 1, 
    TIMER_STATE_CANCELLED = 2, 
    TIMER_STATE_FINISHED  = 3 
  };

  Entity* _tmr_focus; // timer must be entity state
  void (Entity::*_tmr_entfct)(ssf_timer*); // Entity method upon schedule
  void (*_tmr_regfct)(ssf_timer*); // regular function upon schedule

  int _tmr_state;             // state of the timer
  ltime_t _tmr_firetime;      // scheduled fire time
  ssf_kernel_event* _tmr_schedevt; // used for cancellation

  // open access to these friend classes.
  friend class ssf_timeline;
  friend class ssf_kernel_event;
};

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef ssf_timer Timer;
typedef ssf_timer EventTimer;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFTIMER_H__*/

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
