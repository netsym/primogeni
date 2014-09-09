/**
 * \file inchannel.h
 * \brief Standard SSF in-channel interface.
 * 
 * This header file contains the definition of the SSF in-channel
 * class (prime::ssf::inChannel). The user should not include this
 * header file explicitly, as it has been included by ssf.h
 * automatically.
 */

#ifndef __PRIME_SSF_INCHANNEL_H__
#define __PRIME_SSF_INCHANNEL_H__

#ifndef __PRIME_SSF_H__
#error "inchannel.h can only be included by ssf.h"
#endif

#if PRIME_SSF_EMULATION
#include <sys/types.h>
#include <unistd.h>
#endif

namespace prime {
namespace ssf {

typedef SSF_QSET(Process*) SSF_IC_PRC_SET;
typedef SSF_QVECTOR(Event*) SSF_IC_EVT_VECTOR;

/**
 * \brief Standard SSF in-channel class.
 *
 * An SSF in-channel is the receiving end of the communication link
 * between entities. Messages, known as events, are traveling from an
 * out-channel to all in-channels that are mapped to the
 * out-channel. Processes can wait on an in-channel or a list of
 * in-channels for events to arrive. Events that arrive at an
 * in-channel on which no process is waiting are discarded by the
 * system implicitly.
 */
class inChannel {

 public: /* standard public interface */

  /**
   * \brief The constructor of an unnamed in-channel.
   * \param owner points to the owner entity of this in-channel.
   *
   * The constructor is called by passing an argument that points to
   * an entity object as its owner. The ownership is immutable during
   * the lifetime of the in-channel. Using this constructor, the
   * in-channel is unknown externally and therefore cannot be
   * referenced outside the current address space. In order to access
   * it externally (from another address space) in a distributed
   * environment, the channel must be published globally (by calling
   * the publish() method). This must be done at the initialization
   * phase so that the in-channel object can be referenced using its
   * associated string name elsewhere.
   */
  inChannel(Entity* owner);

  /**
   * \brief The constructor of a named in-channel.
   * \param name is the string name of this in-channel.
   * \param owner points to the owner entity of this in-channel.
   *
   * The constructor must be called with two arguments: one is the
   * name of the in-channel, and the other is a pointer to the owner
   * entity object. The ownership is immutable. The name of the
   * in-channel must be globally unique. A named in-channel can be
   * referenced from the outside of the address spaces in a
   * distrubuted environment (using the outChannel::mapto() method).
   * The name can also be used in the DML model description, which is
   * imported by the system during the model construction phase.
   *
   * An in-channel has two name spaces---one global and one
   * local---although the first one infers the second. Each in-channel
   * can have a global unique string name so that out-channels can
   * reference the in-channel using this string name, even from a
   * remote address space. The global unique string name is given
   * either calling this constructor or invoking the publish() method
   * after the in-channel has been created. The scope of the second
   * name space of the in-channel is local to the owner entity. That
   * is, in-channels can have the same name as long as they are not
   * belonging to the same entity. An in-channel will have a local
   * name if the in-channel is first created unnamed and then
   * published using the SSF_PUBLISH_DML_INCHANNEL macro. If the
   * channel is created with a global name, the local name will be the
   * same as the global name.
   */
  inChannel(char* name, Entity* owner);

#if PRIME_SSF_EMULATION
  /**
   * \brief The constructor of a special in-channel that supports
   * real-time interactions with an external device.
   * \param owner points to the owner entity of this in-channel.
   * \param tf is the function for the reader thread.
   * \param ctxt is the context pointer for passing user data to the reader thread.
   * \param dly is the mapping delay (default to zero).
   *
   * The kernel will create a reader thread to read data from the
   * external device. The user must provide the function that uses
   * system blocking calls (such as select) to do the real job. Once
   * the data is read from the real-time device, the user is also
   * responsible for translating the data into a simulation event (of
   * Event type) and pass it to the simulator using the
   * inChannel::putRealEvent() method. There is a mapping delay
   * associated with the in-channel: it's the time the user calls the
   * putRealEvent() method until the event shows up in the in-channel,
   * as if it's delivered from a regular out-channel. The default is
   * zero.  The special in-channel cannot be published or mapped from
   * another out-channel. A context pointer is provided to pass any
   * user-defined data to the thread function. The data becomes owned
   * by the thread function once this method is called and should not
   * be altered by the caller afterwards.
   */
  inChannel(Entity* owner, void (*tf)(inChannel*, void*),
	    void* ctxt = 0, ltime_t dly = SSF_LTIME_ZERO);
#endif

  /**
   * \brief The destructor of an in-channel object.
   *
   * Although an in-channel object can be explicitly deleted by the
   * user during the simulation, we \b strongly recommend the user not
   * to delete in-channel objects directly. Deleting an in-channel
   * object implies that the association between the in-channel object
   * and any processes that are sensitive to the in-channel object
   * must be first destroyed. Also, the link between all out-channels
   * and this in-channel object should be severed. It's a costly
   * operation.  Alternatively, the user can wait until the end of the
   * simulation where the system will reclaim all entities
   * automatically, which will in turn delete all in-channels and
   * out-channels that belong to these entities.
   */
  virtual ~inChannel();

  /**
   * \brief Returns the owner entity of the in-channel.
   *
   * Each in-channel object must be owned by an entity object. The
   * owner entity of the in-channel object is declared when the
   * in-channel is created. The ownership is immutable and cannot be
   * changed throughout the simulation.
   */
  Entity* owner() { return _ic_owner; }

  /**
   * \brief Returns a list of events arrived at the in-channel at
   * the current simulation time.
   *
   * Events arrived at the in-channel will not be stored; they must be
   * retrieved using this method or they will be discarded implicitly
   * by the system afterwards. This method is expected to be called by
   * the process waiting on the in-channel: when the process unblocks
   * and returns from the wait statement, it should get the arrival
   * events immediately. The owner entity of the caller process must
   * be identical to or co-aligned with the owner entity of this
   * in-channel. The returned array is null-terminated list of the
   * arrival events. If the list is empty, a NULL could be returned.
   * If an array is returned, the array belongs to the system and is
   * only temporarily accessible to the caller in the current context
   * (i.e., until the process is suspended again). In particular, the
   * user should not try to reclaim the array. The events in the array
   * can be either passed back to the system (by writing them out
   * through an out-channel), or explicitly stored for later use
   * (using the Event::save() or the Event::clone()
   * methods). Otherwise, the events will be reclaimed by the kernel
   * once there is a process context switch.
   */
  Event** activeEvents();

  /**
   * \brief Publishes an unnamed in-channel.
   * \param name is a globally unique string name of the in-channel.
   *
   * This method associates the in-channel object with a global string
   * name that can be used from another address space. It attaches a
   * string name to the in-channel, which is expected to be otherwise
   * unnamed when created. The name of the in-channel must be globally
   * unique. When published, the in-channel's name is exposed to other
   * address spaceses so that remote out-channels can map to this
   * in-channel using its string name. Also, if the in-channel is
   * published during the simulation initialization phase, its name
   * can be referenced in a DML model specification. To publish an
   * already named in-channel is an error.
   */
  void publish(char* name);

#if PRIME_SSF_EMULATION
  /**
   * \brief Inserts a real event with a real time delay into simulation.
   * \param evt is the event to be inserted into simulation.
   * \param d is the delay of the event in real seconds.
   * \param cb points to a callback functioon.
   * \param dat is the user data that will be passed to the callback function.
   *
   * This is the method that can only be called in the reader thread
   * once the reader thread gets the data from the real device and
   * transforms it into a simulation event. The simulation kernel
   * inserts this event into the timeline's event list as an event in
   * transit, which will soon emerge in user space as if it's a
   * regular user event delivered from a regular out-channel to this
   * in-channel after the prescribed delay. The delay will be the sum
   * of the mapping delay (in simulation time, set when the
   * constructor is called) and the write delay (in real time seconds,
   * which is provided as the second argument of this
   * method). Conforming to the normal SSF event referencing rule, the
   * event given as the argument is presented to the system and
   * therefore no longer belongs to the user. Since SSF's reference
   * counter scheme is not thread-safe, it is important that one does
   * not keep a reference to the event (e.g., via the Event::save()
   * method). If you want to maintain a copy of this event, make an
   * explicit clone of this event before calling this method. If a
   * callback function is not NULL, the callback function will be
   * invoked at the exact simulation time when this event is injected to
   * the system. Both the simulation time and the user data will be
   * passed to the callback function, which is responsible for
   * reclaiming the data afterwards.
   */
  void putRealEvent(Event* evt, double d = 0.0,
		    void (*cb)(ltime_t, void*) = 0, void* dat = 0);

  /**
   * \brief Inserts a real event with a virtual time delay into simulation.
   * \param evt is the event to be inserted into simulation.
   * \param d is the delay of the event in virtual time (default to zero).
   * \param cb points to a callback functioon.
   * \param dat is the user data that will be passed to the callback function.
   *
   * Similar to the putRealEvent() method, one can also schedule
   * simulation events from the reader thread. The user should
   * anticipate that there will be some real time delay before this
   * event can be truly inserted into the simulation system. This is
   * largely due to the context switching between the reader thread
   * and the simulation kernel process.
   */
  void putVirtualEvent(Event* evt, ltime_t d = SSF_LTIME_ZERO,
		       void (*cb)(ltime_t, void*) = 0, void* dat = 0);
#endif /*PRIME_SSF_EMULATION*/

 public:

  // The method is called implicitly by PUBLISH_INCHANNEL method. It
  // is publically accessible but the user shouldn't call it
  // directly.
  void dml_publish(char* thename);

 private: /* private methods for internal use */

  // Make a process sensitive to this in-channel. This is done by
  // adding the wait-node to the cross-link maintained by the
  // process.
  void _ic_set_sensitive(ssf_wait_node* waitnode);

  // Remove the association between a process and this
  // in-channel. The corresponding wait-node is removed from the
  // cross-link.
  void _ic_set_insensitive(ssf_wait_node* waitnode);

  // Set a process to be statically sensitive to this in-channel.
  void _ic_set_sensitive_static(Process* proc);

  // Remove the in-channel from the process's static set. This method
  // is called by the waitsOn method to get rid of previously
  // committed sensitivities. This should be done infrequently since
  // it has a linear cost from removing an item from vector data
  // structure.
  void _ic_set_insensitive_static(Process* proc);

  // The method is called when an event is delivered to this
  // in-channel.
  virtual void _ic_schedule_arrival(ssf_kernel_event*);

  // Clean up the arrival events after processing.
  void _ic_cleanup_active_events();

  // Port number of this in-channel; computed from the serialno and
  // the entity's preset port numbers by DML.
  prime::ssf::uint32 portno();

 private: /* internal member data */

#if SSF_SHARED_DATA_SEGMENT
  // Runtime context to facilitate the SSF_USE_RTX_PRIVATE method.
  void* __rtx__;
#endif

  // The owner entity of the in-channel.
  Entity* _ic_owner;

  // Name of the in-channel. NULL if the channel is not published.
  char* _ic_name;

#if PRIME_SSF_EMULATION
  /*
   * Used by real-time in-channel; points to the function that becomes
   * the reader thread interacting with the real-time device. Null
   * indicates this is not a special channel.
   */
  void (*_ic_threadfct)(inChannel*, void*);

  // User-defined data to be passed to the reader thread function.
  void* _ic_threadctx;

  // The starter function of the reader thread.
  static void _ic_start_thread(inChannel* ic);

  // Used by real-time in-channel; points to the reader thread.
  ssf_thread_type* _ic_thread;

  // PID of the simulation process; used for signal handling.
  ssf_thread_type _ic_parent_pid;

  // The thread ID used by memory management.
  int _ic_pid;

  /*
   * A real-time in-channel is mapped from a real-time device. The
   * user can associate it with a mapping delay.
   */
  ssf_map_node* _ic_mapnode;

#endif /*PRIME_SSF_EMULATION*/

  // Each in-channel has a unique serial number in an entity. To
  // derive a unique number for the entire system, we must also
  // include the serial number of the owner entity.
  int _ic_serialno;

  /*
   * The number of times the _ic_schedule_arrival method has been
   * called so far by the Timeline::micro_scheduling method for
   * handling a batch of simultaneous events. The _ic_schedule_arrival
   * method is called once for every event in the batch. Each call
   * increments this variable by one. The variable is used to make
   * sure that only the first one can unblock the processes waiting on
   * the in-channel. The variable is reset by
   * Timeline::micro_scheduling after the batch of simultaneous events
   * has been processed.
   */
  int _ic_sched;

  // Points to the head of the linked list of processes sensitive to
  // the in-channel. Blocked processes are cross-linked with
  // in-channels.
  ssf_wait_node* _ic_wait_head;
    
  // Points to the tail of the linked list of sensitive processes.
  ssf_wait_node* _ic_wait_tail;

  // List of processes regarding this in-channel as part of their
  // static set of in-channels. Here we use a vector, since setting
  // and resetting static sensitivity are expected to be infrequent
  // operations.
  SSF_IC_PRC_SET _ic_static_procs;

  // The events delivered to this in-channel at the current
  // simulation time are stored in this vector waiting to be
  // retrieved using the activeEvents method by the user.
  SSF_IC_EVT_VECTOR _ic_active_events;

  /* friendly classes that can access private data */
  friend class Entity;
  friend class Process;
  friend class Event;
  friend class outChannel;

  friend class ssf_timeline;
  friend class ssf_logical_process;
  friend class ssf_kernel_event;
  friend class ssf_universe;
}; // class inChannel

/**
 * \brief Publishes an unnamed in-channel for DML access.
 * \param ic points to an unnamed in-channel.
 * \param name is the character string name.
 *
 * This macro is used to attach a name to an in-channel. The macro is
 * designed solely for the purpose of letting the kernel map channels
 * as described in the dml script. Specifically, the name provided
 * here is NOT published globally, therefore it does not have to be
 * unique. And the in-channel is not visible from other address
 * spaces. If however you want to map an in-channel from an
 * out-channel in another address space, you should either create a
 * named in-channel using the proper constructor or invoke the
 * publish() method after creating an unnamed in-inchannel. This macro
 * takes two parameters: a pointer to the in-channel object and a
 * string name to be associated with the in-channel.
 */
#define SSF_PUBLISH_DML_INCHANNEL(ic, name) (ic)->dml_publish(name)

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef inChannel SSF_InChannel;
typedef inChannel SSF_inChannel;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_INCHANNEL_H__*/

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
