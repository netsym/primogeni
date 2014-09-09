/**
 * \file ssfexception.h
 * \brief SSF exception handler.
 *
 * This header file contains the definition of the SSF exception class
 * (prime::ssf::ssf_exception). The exception class can be used by the
 * modeler to handle errors occur during the execution.
 */
#ifndef __PRIME_SSF_SSFEXCEPTION_H__
#define __PRIME_SSF_SSFEXCEPTION_H__

#include <exception>

namespace prime {
namespace ssf {

/**
 * \brief SSF exceptions that may be thrown by the SSF kernel.
 *
 * The SSF kernel throws exceptions when errors are encountered. The
 * user is expected to catch these exceptions when invoking the
 * public function through the SSF API if the user wants to handle the
 * errors in a customed fashion, rather than simply printing the error
 * messages and quitting the program.
 */
class ssf_exception : public virtual std::exception {
public:
  /// All SSF execeptions are defined here.
  enum exception_type_t {
    no_exception,	///< no exception or uninitialized exceptino
    other_exception,	///< unknown derived exception

    ltime_size,		///< prime::ssf::ssf_ltime_serialize or prime::ssf::ssf_ltime_deserialize: unconventional ltime_t size
    ltime_nullbuf,	///< prime::ssf::ssf_ltime_deserialize: null buffer

    compact_convloss,	///< prime::ssf::ssf_compact: type conversion causing data loss
    compact_mpibuf,	///< prime::ssf::ssf_compact: packing data larger than MPI buffer size
    compact_mismatch, ///< prime::ssf::ssf_compact: unmatched data packing
    compact_fops, ///< prime::ssf::ssf_compact: file operation failed
    compact_mpiops, ///< prime::ssf::ssf_compact: MPI operation failed

    entity_nullent,	///< prime::ssf::Entity: alignto() null entity
    entity_align,	///< prime::ssf::Entity: misalignment
    entity_proctxt,	///< prime::ssf::Entity: outside procedure context
    entity_dumpdata,	///< prime::ssf::Entity: dumping data
    entity_register,	///< prime::ssf::Entity: SSF_REGISTER_ENTITY
    entity_startall,	///< prime::ssf::Entity: startAll()
    entity_joinall,	///< prime::ssf::Entity: joinAll() called before startAll()
    entity_publish,	///< prime::ssf::Entity: publishing channel
    //entity_throttle,	///< prime::ssf::Entity: throttle()

    event_sysevt,	///< prime::ssf::Event: deleting an event owned by the system
    event_alias,	///< prime::ssf::Event: deleting an aliased event
    event_release,	///< prime::ssf::Event: unmatched call to the release method
    event_reference,	///< prime::ssf::Event: illegal reference to an event submitted to system
    event_regname,	///< prime::ssf::Event: missing event name for SSF_REGISTER_EVENT
    event_regcall,	///< prime::ssf::Event: missing factory method for SSF_REGISTER_EVENT
    event_regdup,	///< prime::ssf::Event: duplicate event class for SSF_REGISTER_EVENT

    proc_align,		///< prime::ssf::Process: timeline misalignment
    proc_multiwait,	///< prime::ssf::Process: multiple wait statements in a simple process
    proc_pcall,		///< prime::ssf::Process: improper procedure call: 
    proc_simple,	///< prime::ssf::Process: simple procedure invoked by non-simple process
    proc_owner,		///< prime::ssf::Process: null owner pointer
    proc_nowait,	///< prime::ssf::Process: missing wait statement in a simple process
    proc_aligncall,	///< prime::ssf::Process: procedure invocation misalignment

    ic_owner,		///< prime::ssf::inChannel: null owner pointer
    ic_proctxt,		///< prime::ssf::inChannel: activeEvents() called outside procedure context
    ic_align,		///< prime::ssf::inChannel: activeEvents() misalignment
    ic_pubreal,		///< prime::ssf::inChannel: publish real-time in-channel
    ic_duppub,		///< prime::ssf::inChannel: duplicate in-channel publishing 
    ic_putreal,		///< prime::ssf::inChannel: putRealEvent()
    ic_putvirt,		///< prime::ssf::inChannel: putVirtualEvent()

    oc_owner,		///< prime::ssf::outChannel: null owner pointer
    oc_delay,		///< prime::ssf::outChannel: negative delay
    oc_align,		///< prime::ssf::outChannel: misalignment
    oc_real,		///< prime::ssf::outChannel: no real-time support
    oc_noreal,		///< prime::ssf::outChannel: getRealEvent() on non-real-time out-channel
    oc_pubreal,		///< prime::ssf::outChannel: publish real-time out-channel
    oc_duppub,		///< prime::ssf::outChannel: duplicate out-channel publishing 

    timer_entity,	///< prime::ssf::ssf_timer: null entity pointer
    timer_callback,	///< prime::ssf::ssf_timer: null callback function pointer
    timer_running,	///< prime::ssf::ssf_timer: deleting running timer
    timer_delay,	///< prime::ssf::ssf_timer: negative delay
    timer_align,	///< prime::ssf::ssf_timer: entity misalignment
    timer_resched,	///< prime::ssf::ssf_timer: cannot schedule running timer, use reschedule

    sem_delete,	///< prime::ssf::ssf_semaphore: deleting a semaphore with blocked processes
    sem_inproc,	///< prime::ssf::ssf_semaphore: semaphore must be used within an SSF process 
    sem_align,	///< prime::ssf::ssf_semaphore: semaphore must be used within the same timeline

    globalobj,		///< ssf_set_global_object: duplicate global object name

    cmdline_badarg,	///< command line: bad argument
    cmdline_diverse,	///< command line: assumed homogeneous; must reconfigure to allow diverse platforms
    cmdline_nmach,	///< command line: number of machines inconsistent between command-line and MPI context
    cmdline_rank,	///< command line: machine rank inconsistent between command-line and MPI context
    cmdline_async,	///< command line: SSF MPI implementation does not support -async
    cmdline_submodel,	///< command line: failed to read the DML model
    cmdline_modnmach,	///< command line: number of machines inconsistent with the DML model
    cmdline_modnproc,	///< command line: number of processors inconsistent between command-line and DML model
    cmdline_nodist,	///< command line: assumed standalone; must reconfigure to allow distributed simulation
    cmdline_windist,	///< command line: windows platform does not support distributed simulation
    cmdline_output,	///< command line: simulation output

    kernel_heapmem,	///< ssf kernel: memory overflow: increase heap size
    kernel_sdata,	///< ssf kernel: can't map shared data segment
    kernel_pdata,	///< ssf kernel: can't map private data segment
    kernel_sseg,	///< ssf kernel: can't create shared segment
    kernel_forklimit,	///< ssf kernel: number of child processes exceeds limit
    kernel_fork,	///< ssf kernel: failed to create child processes
    kernel_join,	///< ssf kernel: failed to join child processes
    kernel_spawn,	///< ssf kernel: failed to spawn thread
    kernel_binheap,	///< ssf kernel: prime::ssf::ssf_binary_heap
    kernel_splay,	///< ssf kernel: prime::ssf::ssf_splay_tree
    kernel_disapt,	///< ssf kernel: appointment channel unsupported for distributed simulation
    kernel_ssfmodel,	///< ssf kernel: parsing DML model
    kernel_mapch,	///< ssf kernel: channel mapping
    kernel_nojoin,	///< ssf kernel: missing Entity::joinAll() in main thread
    kernel_mkmodel,	///< ssf kernel: building simulation model
    kernel_dumpdata,	///< ssf kernel: dumping data
    kernel_rtqempty,	///< ssf kernel: ssf_realtime_queue empty
    kernel_event,	///< ssf kernel: ssf_kernel_event
    kernel_xdelay,	///< ssf kernel: cross-timeline channels must have positive delays
    kernel_mpi,		///< ssf kenrel: mpi call
    kernel_unpack,	///< ssf kenrel: failed to unpack msg
    kernel_lbts,	///< ssf kenrel: failed to initiate LBTS computation
    kernel_newevt,	///< ssf kenrel: failed to recognize user event
    kernel_pack,	///< ssf kenrel: failed to pack msg

    total_exceptions	///< total number of exceptions defined by this class
  };

  /**
   * \brief The constructor, typically called by the ssf_throw_exception() method.
   * \param c is the error code.
   * \param s is a string, which will be appended to the standard error message.
   */
  ssf_exception(int c, const char* s);

  /// Returns the error code.
  virtual int type() { return code; }

  /// Returns the error message of the exception.
  virtual const char* what() const throw() {
    return explanation;
  }

protected:
  ssf_exception() : code(no_exception) { explanation[0] = 0; }

  int code;
  char explanation[512];
}; /*class ssf_exception*/

/**
 * \brief Throws an exception of the given type and with a customized error message.
 * \param c is the error code (an enumerated value of exception_type_t).
 * \param s is the customized message that will be appended to the standard error string.
 */
inline void ssf_throw_exception(int c, const char* s = 0) { throw ssf_exception(c, s); }

/**
 * \brief Prints out a warning message if allowed to do so.
 * \param s points to the warning message.
 *
 * This function allows variable arguments like printf. If the system
 * is configured not to print out the warning messages, this function
 * is reduced a no-op.
 */
extern void ssf_throw_warning(const char* s, ...);

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFEXCEPTION_H__*/

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
