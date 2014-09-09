/**
 * \file ssfsem.h
 * \brief SSF semaphore for inter-process communication.
 *
 * This file contains the definition of the SSF semaphore class
 * prime::ssf::ssf_semaphore, used for synchronizing simulation
 * processes within an entity. The user should not include this
 * header file explicitly, as it has been included by ssf.h
 * automatically.
 */

#ifndef __PRIME_SSF_SSFSEM_H__
#define __PRIME_SSF_SSFSEM_H__

#ifndef __PRIME_SSF_H__
#error "ssfsem.h can only be included by ssf.h"
#endif

namespace prime {
namespace ssf {

typedef SSF_QDEQUE(Process*) SSF_SEM_PRC_DEQUE;

/**
 * \brief SSF semaphore for synchronizing simulation processes within
 * an entity.
 *
 * It is frequently the case that one simulation process
 * (prime::ssf::Process) needs to signal another process about its
 * progress without passing data. In the standard SSF API, this can be
 * only achieved using channels: two channels---one out-channel
 * (prime::ssf::outChannel) and one in-channel
 * (prime::ssf::inChannel)---are mapped together and events are sent
 * as the signal from one process to the other. This situation becomes
 * more complicated when there are more processes involved. SSF
 * semaphores are created to ease this situation. A semaphore must be
 * an entity state (that is, it's a member variable of the
 * user-defined class derived from prime::ssf::Entity). Processes of
 * the same entity can operate on this semaphore in the same fashion
 * as a typical operating system semaphore for inter-process
 * communications.
 */
class ssf_semaphore {
public: 
  /**
   * \brief The constructor.
   *
   * \param initval is the initial value of the semaphore.
   * \param owner points to the owner entity.
   *
   * The constructor establishes the initial value of the semaphore
   * passed as an argument or to the default value of 1. A process
   * waiting on a semaphore with a negative value will cause the
   * process to suspend. Unlike previous SSF versions, to avoid
   * potential problems of using the semaphore, we require the
   * semaphore be defined with a pointer to the owner entity object,
   * although at this stage passing a NULL is acceptable. If the owner
   * entity is provided, only processes that belong to this entity or
   * an entity that is aligned with this entity are allowed to use
   * this semaphore.
   */
  ssf_semaphore(int initval = 1, Entity* owner = 0);

  /**
   * \brief The destructor.
   *
   * One cannot delete a semaphore on which a process is waiting,
   * unless the simulation enters the wrapup phase.
   */
  virtual ~ssf_semaphore();
  
  /**
   * \brief Called by a process to wait on a semaphore.
   *
   * A call to this method decrements the semaphore, blocks the caller
   * if the result is negative, and appends the blocking process to
   * the end of the semaphore's list of waiting processes. Because
   * this method can potentially block a process, semWait is a wait
   * statement (see prime::ssf::Process). You can only call this
   * method inside a procedure of a simulation process
   * (prime::ssf::ssf_procedure). In addition, although you can call
   * this method liberally in a non-simple process, you can only call
   * this method at the end of a simple process (again, see
   * prime::ssf::Process).
   */
  void semWait();

  /**
   * \brief To increment the semaphore, which may wake up a waiting process.
   *
   * A call to this method increments the semaphore, and if the result
   * is non-negative, unsuspends the first process in the waiting
   * list, if any.  Processes are thus released from the semaphore in
   * the order in which they were blocked upon it. We require that
   * semSignal must be called inside a procedure of a simulation
   * process (prime::ssf::ssf_procedure).
   */
  void semSignal();

  /// Returns the current value of the semaphore.
  inline int semValue() { return _sem_count; }

private:
  // Runtime context to facilitate the SSF_USE_RTX_PRIVATE method.
  //void* __rtx__; (not here please)

  int _sem_count;
  Entity* _sem_owner;
  ssf_timeline* _sem_resident_timeline;
  SSF_SEM_PRC_DEQUE _sem_waiting_queue;
}; /*ssf_semaphore*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFSEM_H__*/

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
