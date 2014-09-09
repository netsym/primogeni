/**
 * \file process.h
 * \brief Standard SSF process interface.
 *
 * This header file contains definition of the SSF process class
 * (prime::ssf::Process) as well as the base class for a procedure
 * container (the prime::ssf::ssf_procedure_container class). The user
 * should not include this header file explicitly, as it has been
 * included by ssf.h automatically.
 */

#ifndef __PRIME_SSF_PROCESS_H__
#define __PRIME_SSF_PROCESS_H__

#ifndef __PRIME_SSF_H__
#error "process.h can only be included by ssf.h"
#endif

namespace prime {
namespace ssf {

typedef SSF_QVECTOR(inChannel*) SSF_PRC_IC_VECTOR;
typedef SSF_QMAP(inChannel*,bool) SSF_PRC_ICBOOL_MAP;

/** 
 * \brief Basic class for all classes that contain procedures.
 *
 * The ssf_procedure_container class is the base class for all those
 * classes whose methods can be used as procedures. A procedure is a
 * function that is invoked by a simulation process. Both Entity and
 * Process classes are derived from this class since their methods can
 * be used as procedures. User can define a separate class with
 * methods as procedures as long as it's an subclass of this class.
 */
class ssf_procedure_container {
protected:
  /** There're only three types of procedure containers. */
  enum {
    PROCTYPE_GENERIC,	///< a generic class
    PROCTYPE_ENTITY,	///< an entity
    PROCTYPE_PROCESS	///< a process
  };

  // what type of container is this?
  int _proc_classid;

  /*
   * The constructor is protected; no one should instantiate this base
   * class directly. It can only be inherited.
   */
  ssf_procedure_container(int classid = PROCTYPE_GENERIC) :
    _proc_classid(classid) {}

  friend class Process;
}; /*ssf_procedure_container*/

/**
 * \brief Standard SSF process class.
 *
 * An SSF process is part of an entity (Entity) and is used to specify
 * the entity's state evolution. One can think of an SSF process as a
 * regular Unix process; it's a thread of execution interspersed with
 * wait statements. A process can wait for an event to arrive or wait
 * for a specified simulation time to pass. Each process object
 * encapsulates an SSF process. A process is started by executing a
 * procedure, which can be a method of an entity, a process, or any
 * class derived from the ssf_procedure_container class. The users can
 * define their own process classes if per-process private data is
 * needed.
 *
 * In SSF, processes are created and destroyed frequently during the
 * simulation. Therefore, we make the Process class a subclass of
 * ssf_quickobj so that we can do faster memory allocation and
 * deallocation operations. Also, since methods of the Process class
 * can be used as procedures, we make Process a subclass of the
 * ssf_procedure_container class.
 */
class Process : public ssf_quickobj, public ssf_procedure_container {
 public: /* standard public interface */

  /**
   * \brief The constructor with starting procedure as action().
   * \param owner points to the entity owner of this process.
   * \param simple indicates whether the process is a simple process or not.
   *
   * The constructor uses the Process::action() method as the starting
   * procedure. When this constructor is called, a new SSF process is
   * created. The new process will become eligible to run at the
   * current simulation time by invoking the start procedure, which,
   * in this case, is the action() method. Each process must be owned
   * by an entity instance and the ownership is immutable. In
   * particular, a process cannot migrate from one entity to
   * another. The process, however, is free to access the state
   * variables of its owner entity and all other entities that are
   * co-aligned with its owner entity, including instances of
   * in-channels and out-channels owned by these entities. A simple
   * process is a process with its procedure containing only one wait
   * statement and the wait statement must be the last statement in
   * the procedure. This special arrangement allows efficient process
   * context switching using continuation.
   */
  Process(Entity* owner, boolean simple = false);

  /**
   * \brief The constructor of a process with an entity method as the
   * starting procedure. 
   * \param owner points to the entity owner of this process.
   * \param starter points to the entity method as the starting procedure.
   * \param simple indicates whether the process is a simple process or not.
   *
   * This constructor is similar to the previous one except that a
   * pointer to the starting procedure must be provided as an
   * argument. Refer to the previous constructor for details. Note that
   * the starting procedure function is actually a pointer to the Entity
   * class method. For example, if \b myent is a class derived from
   * Entity and the starting procedure function is a method called \b foo in the
   * myent class, the process can be created using:
\verbatim
new Process(myent, (void (Entity::*)(Process*))&myent::foo), true);
\endverbatim
   *
   */
  Process(Entity* owner, void (Entity::*starter)(Process*), 
	  boolean simple = false);

  /**
   * \brief The default staring procedure of this process.
   * 
   * This method does nothing besides suspending the process
   * indefinitely, which is conceptually equavalent to killing the
   * process. The user must override this method in a subclass to
   * alter this behavior. The precedure is simple by default.
   */
  virtual void action(); //! SSF PROCEDURE SIMPLE

  ; /* this flushes out the undesirable doxygen comment */

  /**
   * \fn init
   * \brief Initializes the process after it is created.
   *
   * This method is called by the system to initialize the process at
   * the same simulation as the time when the process is created. When
   * the process is created, an event is inserted into the timeline's
   * event list with a timestamp ofs the current simulation time. When
   * the event is next processed, the system will then invoke the
   * process's init method. The method is virtual and does nothing by
   * default; the user may override this method in the derived process
   * class to allow some initialization actions.
   */
  virtual void init() {}

  /**
   * \brief Wraps up the process before terminating the process.
   *
   * This method is called by the system to wrapup the process when
   * the process is terminated (including the case where the process
   * relinquishes the right to ever resume execution, for example, by
   * calling the waitForever method). The method is called before the
   * process object is to be reclaimed. All processes in the system
   * will be terminated automatically at the end of the simulation. By
   * default, this method does nothing. The user needs to override
   * this method in the derived class if needed.
   */
  virtual void wrapup() {}

  /**
   * \brief Returns whether the process is simple.
   *
   * A process is declared simple or not at the time when the process
   * is created. A simple process requires that the procedure
   * effectively contains only one wait statement and the wait
   * statement is at the very end of the procedure. The kernel
   * implements fast ways to deal with continuation to save the cost
   * of context switch for a simple process.
   */
  boolean isSimple() { return _proc_simple; }

  /**
   * \brief Returns the owner entity of this process.
   *
   * Every process is owned by an entity. The ownership is specified
   * in the process constructor is created and it's immutable.
   */
  Entity* owner() { return _proc_owner; }

  /** @{ */
  /**
   * \brief Blocks the process for message arrival at any of the in-channels.
   * \param w is a null-terminated list of in-channels.
   *
   * This method is a wait statement and must be called within a
   * procedure context. The process will be suspended until an event
   * or events arrive at any one of the in-channels specified in the
   * argument. If the list is empty, the process will wait forever,
   * and therfore will be terminated and reclaimed by the system. The
   * array provided as the argument should be null-terminated. The
   * in-channels must be either owned by the owner entity of this
   * process or owned by entities co-aligned with the owner entity of
   * this process. The array is duplicated in the kernel and therefore
   * can be reclaimed or changed after the call even when the process
   * is being suspended.
   */
  void waitOn(inChannel** w);

  /**
   * \brief Blocks the process for message arrival at an in-channel.
   * \param w points to an in-channel.
   *
   * The method is the same as the previous one, except, instead of
   * providing a null-terminated array of in-channels to wait on, only
   * one in-channel is specified.
   */
  void waitOn(inChannel* w);

  /**
   * \brief Blocks (and terminates) the process forever.
   *
   * This method will cause the process to suspend forever without the
   * possibility of resumption. The method is a wait statement and
   * must be called within a procedure context. Semantically, this is
   * identical to having this process terminated. And the kernel is
   * doing just that: the wrapup method will be called before the
   * process is reclaimed.
   */
  void waitForever();

  /**
   * \brief Blocks the process forever, but keeps the object.
   *
   * This method is similar to the previous method in that the process
   * will be suspended forever. The difference between waitForever and
   * suspendForever is that the latter one does not immediately
   * reclaim the process. That is, the process data structure will
   * remain accessible until the end of the simulation or until the
   * user reclaims it explicity.  In some cases this is necessary, for
   * example, when a derived process class contains data fields that
   * need to remain persistent even after the process has been
   * terminated.
   */
  void suspendForever();

  /**
   * \brief Suspends the process for the given period of time.
   * \param d is the time interval for waiting.
   *
   * The method is a wait statement and must be called within a
   * procedure context. The process will be suspended until the given
   * wait interval has elapsed. That is, the process will remain
   * suspended until the simulation clock reaches the current
   * simulation time plus the wait interval. Note that there will be a
   * context switch even if the given interval is zero.
   */
  void waitFor(ltime_t d);

  /**
   * \brief Suspends the process until the specified time.
   * \param t is the time to wake up the process.
   *
   * The method is a wait statement and must be called within a
   * procedure context. The method will cause the process to be
   * suspended until the simulation clock reaches the given simulation
   * time. The specified simulation time must be larger than or equal
   * to the current simulation time.
   */
  void waitUntil(ltime_t t);

  /**
   * \brief Blocks the process for message arrival on any of the
   * in-channels, but only for the given time interval.
   * \param w is a null-terminated list of in-channels.
   * \param d is the maximum time interval for waiting.
   * 
   * The method suspends the process to wait for an event arrival on a
   * list of in-channels or a given amount of simulation time to
   * elapse, whichever happens first. The method is a wait statement
   * and must be called within a procedure context. The process will
   * be suspended until one of the two conditions becomes true. Either
   * there is an event or events delivered at any one of the
   * in-channels specified, or the given simulation time period has
   * elapsed. The method returns true if it's timed out, or false if
   * an event arrives first. The result is nondeterministic if the
   * both happen simultaneously. The array provided in the argument
   * should be null-terminated and the in-channels must be either
   * owned by the owner entity of this process or owned by entities
   * co-aligned with the owner entity of this process. The array is
   * duplicated inside the kernel. Therefore, it can be changed or
   * reclaimed even when the process is suspended. Note that the
   * return value of this method must be stored in a state variable
   * since it straddles across a process suspension.
   */
  boolean waitOnFor(inChannel** w, ltime_t d);

  /**
   * \brief Blocks the process for message arrival on an in-channel,
   * but only for the given time interval.
   * \param w points to the in-channel.
   * \param d is the maximum time interval for waiting.
   *
   * The method is similar to the previous one except, instead of
   * providing a null-terminated array of in-channels to wait on, only
   * one in-channel is specified.
   */
  boolean waitOnFor(inChannel* w, ltime_t d);

  /**
   * \brief Blocks the process for message arrival on any of the
   * in-channels, but only until the specified time.
   * \param w is a null-terminated list of in-channels.
   * \param t is the time to wake up the process.
   *
   * This method is an alternative to the waitOnFor methods where the
   * process will be suspended either when there is an event arrival
   * or until the given simulation time has been reached. The
   * specified simulation time must be larger than or at least equal
   * to the current simulation time.
   */
  boolean waitOnUntil(inChannel** w, ltime_t t);

  /**
   * \brief Blocks the process for message arrival on an
   * in-channel, but only until the specified time.
   * \param w points to an in-channel.
   * \param t is the time to wake up the process.
   *
   * The method is similar to the previous one except, instead of
   * providing a null-terminated array of in-channels to wait on, only
   * one in-channel is specified.
   */
  boolean waitOnUntil(inChannel* w, ltime_t t);

  /**
   * \brief Blocks on a set of in-channels set by the waitsOn() method
   * previously.
   *
   * The method is a wait statement and therefore must be called in a
   * procedure context. The process will suspend waiting for events to
   * arrive at the static set of in-channels.
   */
  void waitOn();

  /**
   * \brief Blocks on a set of in-channels set by the waitsOn() method
   * previously, but only for the given period of time.
   * \param d is the maximum time interval for waiting.
   *
   * The method is similar to the other waitOnFor method except
   * there's no in-channel specified. The process will wait for events
   * to arrive on the static set of in-channels only, or until the
   * given amount of simulation time has elapsed.
   */
  boolean waitOnFor(ltime_t d);

  /**
   * \brief Blocks on a set of in-channels set by the waitsOn() method
   * previously, but only until the specified time.
   * \param t is the time to wake up the process.
   *
   * The method is similar to the other waitOnUntil method except
   * there's no in-channel specified. The process will wait for events
   * to arrive on the static set of in-channels only, or until the
   * given simulation time has been reached.
   */
  boolean waitOnUntil(ltime_t t);
  /** @} */

  /**
   * \brief Specifies a set of in-channels the process will be waiting
   * on later.
   * \param w is a null-terminated list of in-channels.
   *
   * This method specifies the static set of in-channels the process
   * will be waiting on. The waitOn methods, where the in-channels are
   * not specified, will wait on the static set of in-channels. Using
   * static set of in-channels can remove the overhead for setting up
   * and tearing down internal data structures needed for establishing
   * associations between processes and in-channels.  Since it is
   * common for a process in a simulation to always wait on a fixed
   * set of in-channels, the user can call this method to make the
   * process sensitive to arrivals on all channels in the static set
   * once for all. The method is not a wait statement and therefore it
   * can be called anywhere. Also, the method can be invoked multiple
   * times---the latest one determines the static in-channel set the
   * process is currently sensitive to. The array provided should be
   * null-terminated. The in-channels must be either owned by the
   * owner entity of this process or owned by entities co-aligned with
   * the owner entity of this process. The array is duplicated inside
   * the kernel. Therefore, the user can change or reclaim it
   * immediately after the call is returned.
   */
  void waitsOn(inChannel** w);

  /**
   * \brief Specifies a single in-channels the process will be waiting
   * on later.
   * \param w point to an in-channel.
   *
   * The method is similar to the previous one except, instead of
   * providing a null-terminated array of in-channels as the static
   * set, only one in-channel is specified.
   */
  void waitsOn(inChannel* w);

  /**
   * \brief Returns the list of in-channels containing arrival events.
   *
   * The method returns a list of in-channels currently holding
   * event(s) arriving at the in-channels waited by the process. The
   * method must be called in a procedure context. Specifically the
   * method returns a null-terminated array of pointers to in-channels
   * that contain events that activated the process. The set must be a
   * subset of the in-channels the process is sensitive to, either
   * statically or dynamically.  The returned array belongs to the
   * system and is only temporarily accessible to the user in the
   * current context.
   */
  inChannel** activeChannels();

  /// This method is an alias to owner entity's now() method.
  ltime_t now();

 public: /* public methods that should be used internally */

  /*
   * This method is used by the source-code translator for sanity
   * checking to make sure a procedure is called properly. When a
   * method is used as a procedure, it should not be invoked directly
   * by the user outside the process context. This method is embedded
   * into the procedure's preamble by the translator. It will report
   * any improper use of the procedure and abort the execution if it
   * happens. The argument is the name of the procedure, which will be
   * used in the error report.
   */
  void _proc_is_proper(const char* fctname);

  /*
   * This method is used by the source-code translator to find out
   * whether the running process is unblocked as a result of the
   * waitOnFor method timed out.
   */
  boolean _proc_is_timedout() { return _proc_timedout; }

  /*
   * This method is used by the source-code translator to retrieve the
   * procedure frame at the top of the stack.
   */
  ssf_procedure* _proc_top_stack() { return _proc_stack; }

  /*
   * This method is used by the source-code translator to add a
   * procedure frame to the top of the stack.
   */
  void _proc_push_stack(ssf_procedure* proc);

  // Pop a procedure frame from the top of the stack.
  ssf_procedure* _proc_pop_stack();

  /*
   * This method is used by the source-code translator to create a
   * procedure frame for the action method, in case it is
   * chosen. Usually, the translator will embed this method
   * automatically into the source code in the derived process
   * class. Here we need to bootstrap itself in case the action method
   * in this base class is chosen to be the start procedure.
   */
  virtual ssf_procedure* _proc_create_procedure_action();

 protected:

  /*
   * This is the destructor of the process. The destructor is a
   * protected method; the user should not delete a process
   * explicitly.  One should call the destroy method instead. The
   * process object will be reclaimed by the system when the process
   * terminates.
   */
  virtual ~Process();

 private: /* internal member data */

  /** There are four possible states of a process. */
  enum {
    PROCSTATE_CREATING,   ///< new born
    PROCSTATE_RUNNING,    ///< running
    PROCSTATE_WAITING,    ///< waiting (for events or timeout)
    PROCSTATE_TERMINATING ///< about to be terminated
  };

#if SSF_SHARED_DATA_SEGMENT
  // Runtime context to facilitate the SSF_USE_RTX_PRIVATE method.
  void* __rtx__;
#endif

  // The owner entity; it's immutable.
  Entity* _proc_owner;
  
  // True if the process is simple.
  boolean _proc_simple;

  // The starting procedure can be either a method of an entity
  // class or the action method of the process class. In the latter
  // case, the following pointer is set to be NULL.
  void (Entity::*_proc_starter)(Process*);

  // The current state of the process.
  int _proc_state;

  // Points to the procedure frame at the top of the stack.
  ssf_procedure* _proc_stack;

  // Points to the timeout event on which the process is
  // awaiting. This pointer is used to hold the kernel event in case
  // it needs to be cancelled.
  ssf_kernel_event* _proc_holdevt;

  // List of in-channels the process is dynamically waiting
  // on. Thedynamic association between processes and in-channels is
  // implemented as a cross-link.
  ssf_wait_node* _proc_waiton;

  // List of in-channels the process is statically sensitive to.
  SSF_PRC_IC_VECTOR _proc_static_channels;

  // True if the process is blocked expecting events to be
  // delivered to static channels.
  boolean _proc_static_expect;

  // List of in-channels that activate the process.
  SSF_PRC_ICBOOL_MAP _proc_active_inchannels;

  // This variable is set and reset by the system to check whether
  // a procedure is call properly.
  int _proc_proper;

  // True if the process is activated due to time out.
  boolean _proc_timedout;

 private:
    
  // Called the contructor to initialize the data fields.
  void _proc_constructor(Entity* owner);
  
  // Make the process dynamically sensitive to an in-channel.
  void _proc_sensitize(inChannel* ic);

  // Remove dynamic sensitivities of the process to all in-channels.
  void _proc_insensitize();

  // Make the process statically sensitive to an in-channel.
  void _proc_sensitize_static(inChannel* ic);

  // Remove static sensitivities of the process to all in-channels.
  void _proc_insensitize_static();

  // Suspend the process for some time.
  void _proc_holdfor(ltime_t delay);

  // Suspend the process until the specified simulation time has been
  // reached.
  void _proc_holduntil(ltime_t when);

  // Continue running this process.
  void _proc_execute();
    
  // Activate the procedure on top of the stack or initiates the
  // starting procedure.
  void _proc_activate();

  // Block the process from execution. The unblocking condition
  // should have already been set before this method is invoked.
  void _proc_suspend();
    
  // Terminate the process.
  void _proc_terminate();

  // Clean up the association with in-channels when deactivated.
  void _proc_cleanup_active_inchannels();
    
  /* list of friendly classes for opening access */
  friend class Entity;
  friend class Event;
  friend class inChannel;
  friend class outChannel;

  friend class ssf_semaphore;
  friend class ssf_procedure;
  friend class ssf_timeline;
  friend class ssf_logical_process;
  friend class ssf_kernel_event;
  friend class ssf_universe;
}; /*Process*/

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef Process SSF_Process;
typedef Process process;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_PROCESS_H__*/

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
