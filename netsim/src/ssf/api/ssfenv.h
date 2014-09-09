/**
 * \file ssfenv.h
 * \brief System runtime information and auxiliary functions.
 */

#ifndef __PRIME_SSF_SSFENV_H__
#define __PRIME_SSF_SSFENV_H__

namespace prime {
namespace ssf {

  /// \brief Returns the number of distributed machines for this
  /// simulation run.
  extern int ssf_num_machines();
  
  /// \brief Returns the caller's machine index (from 0 to m-1).
  extern int ssf_machine_index();

  /// \brief Returns the number of processors on this machine.
  extern int ssf_num_processors();

  /// \brief Returns the total number of processors on the the machine
  /// of the given index.
  extern int ssf_num_processors(int mach_idx);

  /// \brief Returns the total number of processors used for entire
  /// distributed simulation environment.
  extern int ssf_total_num_processors();

  /// \brief Returns the caller's processor index on this machine.
  extern int ssf_processor_index();

  /// \brief Returns the global index of this processor.
  extern int ssf_total_processor_index();

  /// \brief Gets the processor index range [start_idx, end_idx] of
  /// the current machine and returns the global index of this
  /// processor.
  extern int ssf_processor_range(int& start_idx, int& end_idx);

  /**
   * \brief Installs a global object.
   * \param objname is the name of the object.
   * \param object points to a generic data structure.
   *
   * This function Associates a global object with a name so that it
   * can be accessed from another processor on the \b same
   * machine. The global object should be located in the shared heap
   * so that other processors are able to access it. A reference to a
   * static global variable won't do it. To make an object global, it
   * must be created using the new operator (which is overloaded by
   * SSF).
   */
  extern void ssf_set_global_object(char* objname, void* object);

  /// \brief Returns the global object of the given name or NULL if
  /// not found.
  extern void* ssf_get_global_object(char* objname);

  /**
   * \brief Sets a barrier for processors sharing the same memory.
   * \param fct points to the callback function.
   * \param tm is the wakeup time of the barrier or the period if the barrier is recurring.
   * \param dat is the user-defined data to be passed to the callback function.
   * \param recur indicates whether this is a recurring barrier or not.
   * \returns a handle that can be used to cancel this barrier.
   *
   * Shared-memory barrier is a facility for user to synchronize
   * processors on the same shared memory. Note that only processor 0
   * on this machine calls the callback function. The callback
   * function takes three arguments: the handle that represents the
   * barrier, the simulation time of the barrier, and the user-defined
   * data.
   */  
  extern long ssf_shmem_barrier_set(void (*fct)(long, ltime_t, void*), 
				    ltime_t tm, void* dat,
				    boolean recur = false);

  /// \brief Cancels a shared-memory barrier.
  extern boolean ssf_shmem_barrier_cancel(long handle);

#if PRIME_SSF_BACKWARD_COMPATIBILITY
#define num_machines ssf_num_machines
#define machine_index ssf_machine_index
#define num_processors ssf_num_processors
#define total_num_processors ssf_total_num_processors
#define processor_index ssf_processor_index
#define total_processor_index ssf_total_processor_index
#define processor_range ssf_processor_range
#define set_global_object ssf_set_global_object
#define get_global_object ssf_get_global_object
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFENV_H__*/

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
