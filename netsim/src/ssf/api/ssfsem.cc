/*
 * ssfsem :- ssf semaphore for inter-process communication.
 *
 * It is frequently the case that one simulation process needs to
 * signal another process about its progress without passing data. In
 * the standard SSF API, this can be only achieved using channels: two
 * channels---one out-channel and one in-channel---are mapped together
 * and events are sent as the signal from one process to the
 * other. This situation becomes more complicated when there are more
 * processes involved. SSF semaphores are created to ease this
 * situation. A semaphore must be an entity state (that is, it's a
 * member variable of the user-defined entity class). Processes of the
 * entity can operate on this semaphore in the same fashion as a
 * typical operating system semaphore for inter-process
 * communications.
 */

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

ssf_semaphore::ssf_semaphore(int count, Entity* owner) :
  _sem_count(count), _sem_owner(owner), 
  _sem_resident_timeline(0) {}

ssf_semaphore::~ssf_semaphore() {
  if(SSF_USE_PRIVATE(simphase) != SSF_SIMPHASE_WRAPUP && 
     _sem_waiting_queue.size() > 0)
    ssf_throw_exception(ssf_exception::sem_delete);
}

void ssf_semaphore::semWait() {
  if(!((Process*)SSF_USE_PRIVATE(running_process)))
    ssf_throw_exception(ssf_exception::sem_inproc, "semWait()");

  // the method should not be called outside procedure context
  ((Process*)SSF_USE_PRIVATE(running_process))->_proc_is_proper("semWait");

  if(!_sem_resident_timeline) {
    if(_sem_owner && _sem_owner->_ent_timeline != 
       ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)))
      ssf_throw_exception(ssf_exception::sem_align, "semWait()");
    _sem_resident_timeline = ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline));
  } else if(_sem_resident_timeline != ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)))
    ssf_throw_exception(ssf_exception::sem_align, "semWait()");

  _sem_count--; if(_sem_count >= 0) return;

  // add calling process to the end of the semaphore waiting list.
  // First get the process that will block, then call suspend to get
  // it off of the runtime deque.
  ((Process*)SSF_USE_PRIVATE(running_process))->_proc_suspend();
  _sem_waiting_queue.push_back(((Process*)SSF_USE_PRIVATE(running_process)));
}

void ssf_semaphore::semSignal() {
  if(!((Process*)SSF_USE_PRIVATE(running_process)))
    ssf_throw_exception(ssf_exception::sem_inproc, "semSignal()");

#if 0
  // the method should not be called outside procedure context
  ((Process*)SSF_USE_PRIVATE(running_process))->_proc_is_proper("semSignal");
#endif

  if(!_sem_resident_timeline) {
    if(_sem_owner && _sem_owner->_ent_timeline != 
       ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)))
      ssf_throw_exception(ssf_exception::sem_align, "semSignal()");
    _sem_resident_timeline = ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline));
  } else if(_sem_resident_timeline != ((ssf_timeline*)SSF_USE_PRIVATE(processing_timeline)))
    ssf_throw_exception(ssf_exception::sem_align, "semSignal()");

  _sem_count++;

  // if there is at least one process blocked, release the one at the
  // head of the blocked list
  if(_sem_waiting_queue.size() > 0) {
    Process *releaseProcess = _sem_waiting_queue.front();
    _sem_waiting_queue.pop_front();
    ssf_kernel_event* kevt = new ssf_kernel_event
      (SSF_USE_PRIVATE(now), ssf_kernel_event::EVTYPE_SEMSIGNAL);
    kevt->process = releaseProcess;
    releaseProcess->_proc_owner->_ent_insert_event(kevt);
  }
}

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef ssf_semaphore Semaphore;
#endif

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
