/**
 * \file outchannel.h
 * \brief Standard SSF out-channel interface.
 *
 * This header file contains the definition of the SSF out-channel
 * class (prime::ssf::outChannel). The user should not include this
 * header file explicitly, as it has been included by ssf.h
 * automatically.
 */

#ifndef __PRIME_SSF_OUTCHANNEL_H__
#define __PRIME_SSF_OUTCHANNEL_H__

#ifndef __PRIME_SSF_H__
#error "outchannel.h can only be included by ssf.h"
#endif

namespace prime {
namespace ssf {

typedef SSF_QVECTOR(ssf_local_tlt*) SSF_OC_TML_VECTOR;

/**
 * \brief Standard SSF out-channel class.
 *
 * An SSF out-channel is the sending end of the communication link
 * between entities. Messages, known as events, are traveling from an
 * out-channel to all in-channels that are mapped to the
 * out-channel. The events sent from an out-channel experience delays
 * before reaching a mapped in-channel. The delay is the sum of a
 * channel delay (specified when the out-channel is created), a
 * mapping delay (specified when the out-channel is mapped to an
 * in-channel), and a per-write delay (specified when the event is
 * written to the out-channel).
 */
class outChannel {

 public: /* standard public interface */

  /**
   * \brief The constructor of an anonymous out-channel.
   * \param owner points to the owner entity of this out-channel.
   * \param cd is the channel delay (default to zero).
   *
   * The constructor must be called by passing the owner entity as its
   * argument.  The ownership of an out-channel is immutable. An
   * out-channel has a channel delay associated with it (which
   * defaults to zero): any events written to the out-channel are
   * delayed by this value. Using this constructor, the out-channel is
   * unknown externally and therefore cannot be identified by the DML
   * model description, unless the channel is published using the
   * SSF_PUBLISH_DML_OUTCHANNEL macro. Note that publishing this
   * out-channel must be done at the simulation initialization phase
   * so that the in-channel object can be known globally using its
   * associated string name.
   */
  outChannel(Entity* owner, ltime_t cd = SSF_LTIME_ZERO);

  /**
   * \brief The constructor of a named out-channel.
   * \param dmlname is the string name of the out-channel.
   * \param owner points to the owner entity of this out-channel.
   * \param cd is the channel delay (default to zero).
   *
   * This constructor creates a named out-channel, which can be
   * referenced by the DML model description during the model
   * construction phase. This constructor is equivalent to creating an
   * anonymous out-channel using the previous constructor and then
   * publishing the out-channel using the SSF_PUBLISH_DML_OUTCHANNEL
   * macro. Note that, unlike in-channels, out-channels do not have a
   * global name space---the scope of the name of this out-channel is
   * restricted to its owner entity---and the name can be only used in
   * the DML model specification.
   */
  outChannel(char* dmlname, Entity* owner, ltime_t cd = SSF_LTIME_ZERO);

#if PRIME_SSF_EMULATION
  /**
   * \brief The constructor of a special out-channel for real-time
   * interactions with an external device.
   * \param owner points to the owner entity of this out-channel.
   * \param tf is the function for the writer thread.
   * \param ctxt is the context pointer for passing user data to the writer thread.
   * \param cd is the channel delay (default to zero).
   * 
   * The kernel will create a writer thread to write data to the
   * external device. The user must provide the function to do the
   * real job. In particular, the user is responsible for translating
   * simulation events (of Event type) into the real data. The
   * simulation events are presented to the user via the
   * outChannel::getRealEvent() method. There is a delay associated
   * with the event indicating the expect latency before the event is
   * delivered to the external device. The special out-channel cannot
   * be published or mapped to another in-channel.  A context pointer
   * is provided to pass any user-defined data to the thread
   * function. The data is owned by the thread function afterwards and
   * should not be altered by simulation once this method is called.
   */
  outChannel(Entity* owner, void (*tf)(outChannel*, void*),
	     void* ctxt = 0, ltime_t cd = SSF_LTIME_ZERO);
#endif /*PRIME_SSF_EMULATION*/

  /**
   * \brief The destructor of the out-channel object.
   *
   * Detroying an out-channel means to separate the links between the
   * out-channel and all its mapped in-channels. Note that the user
   * shall not reclaim an out-channel directly: all out-channels are
   * reclaimed by the system when the owner entity is destroyed at the
   * end of the simulation.
   */
  virtual ~outChannel();

  /// Returns the owner entity of the out-channel.
  Entity* owner() { return _oc_owner; }

  /**
   * \brief Sends an event out from this out-channel to all mapped
   * in-channel.
   * \param evt points to the event to be sent out.
   * \param dly is the per-write-delay that will be added to the event's total transfer delay.
   *
   * A process may write an event to any out-channel as long as the
   * process' owner entity and the channel's owner entity are either
   * identical or coaligned with each other. The written event is
   * scheduled to arrive at its destination after the sum of channel
   * delay, mapping delay, and per-write delay (given by the second
   * argument). The event remains its accessibility to the user after
   * the write only in the current context, i.e., before the process
   * is suspended again.
   */
  virtual void write(Event* evt, ltime_t dly = SSF_LTIME_ZERO);

  /**
   * \brief Maps from this out-channel to an in-channel.
   * \param ic points to the in-channel.
   * \param dly is the mapping delay that will be added to the event's total transfer time.
   * \returns a simulation time when the mapping will be established.
   *
   * The method creates a new mapping between this out-channel and the
   * in-channel specified in the first argument. This method requires
   * a pointer to the in-channel, which means that the in-channel must
   * have been created in the same address space. A non-negative
   * mapping delay can be associated with each mapping (which defaults
   * to zero).
   *
   * The method returns a simulation time at which the mapping becomes
   * effective. If the mapping is done during the simulation
   * initialization phase, it is guaranteed that the returned
   * timestamp is the simulation start time (therefore, no waiting is
   * necessary). Otherwise, the user is required to wait until the
   * specified simulation time before sending an event out from this
   * out-channel so that the mapping can be established in time. It is
   * \b strongly recommended that the user establishes the mapping at
   * the simulation initialization phase, since such mapping will be
   * done in an implementation-dependent fashion, which means that SSF
   * implementations are not behaving consistently on the returned
   * timestamp.
   *
   * SSF requires that the sum of the channel delay and the mapping
   * delay must be strictly greater than zero if the entities at both
   * ends belong to different timelines, unless this is a mapping from
   * an appointment out-channel.
   */
  ltime_t mapto(inChannel* ic, ltime_t dly = SSF_LTIME_ZERO);

  /**
   * \brief Maps from this out-channel to a named in-channel.
   * \param icname is the name of the in-channel.
   * \param dly is the mapping delay that will be added to the event's total transfer time.
   * \returns a simulation time when the mapping will be established.
   *
   * This method is similar to the previous mapto method except that
   * we refer to the in-channel using a string name rather than a
   * pointer to the in-channel object. This allows one to establish a
   * mapping between channels at different address spaces in a
   * distributed memory environment.
   */
  ltime_t mapto(char* icname, ltime_t dly = SSF_LTIME_ZERO);

  /**
   * \brief Unmap this out-channel to the specified in-channel.
   * \param ic points to the in-channel.
   * \returns a simulation time when the unmapping operation is accomplished.
   *
   * This method is used to sever a connection from this out-channel
   * to an in-channel, pointed to by the argument. This method
   * requires a pointer to the in-channel, which means that the
   * in-channel must have been created in the same address space. 
   *
   * The operation will not take effect until the simulation clock
   * reaches the returned timestamp. That is, the user should wait
   * until the returned simulation before sending events out from this
   * out-channel. Otherwise, the behavior is non-deterministic. It is
   * \b strongly recommended that the user avoids using this method
   * since dynamic channel mapping is implementation dependent in
   * SSF. That is, the user cannot expect an SSF implementation to
   * honor dynamic channel mapping in a consistent manner: one cannot
   * be sure about the returned timestamp at all. It is for this
   * reason the user should simply map the channels at the beginning
   * of the simulation and not change the mapping during the
   * simulation.
   */
  ltime_t unmap(inChannel* ic);

  /**
   * \brief Unmaps this out-channel from a named in-channel.
   * \param icname is the name of the in-channel.
   * \returns a simulation time when the unmapping operation will be done.

   * This method is similar to the previous one except we use a string
   * name to refer to the in-channel.
   */
  ltime_t unmap(char* icname);

  /*
   * This method (de-)associates the out-channel object with a string
   * name that can be used from a DML script. The method can be called
   * directly by user or indirectly from the
   * SSF_PUBLISH_DML_OUTCHANNEL macro. It attaches a string name to
   * the out-channel, which is expected to be otherwise unnamed when
   * constructed. If the in-channel is published during the simulation
   * initialization phase, its name can be referenced in a DML script
   * to describe the channel mapping. To publish an already named
   * out-channel is an error.
   */
  void dml_publish(char* thename);
  void dml_unpublish();

#if PRIME_SSF_EMULATION
  /**
   * \brief Retrieves the next event for the writer thread.
   * \param rdly is the real time delay in seconds returned by this method.
   * \returns a pointer to the event.
   *
   * This is a method that can only be called in the writer thread,
   * when it is free to get the next event in queue waiting to be
   * transferred to the external device. The thread would suspend if
   * there is no event in the queue. Different from the normal SSF
   * event referencing rule, the returned event belongs to the user
   * and the user is responsible to reclaim it afterwards. We do this
   * because SSF's reference counter scheme is not thread-safe. A
   * real-time delay is also returned. The delay is in seconds and is
   * the expected delay before this event affects the external device.
   */
  Event* getRealEvent(double& rdly);
#endif /*PRIME_SSF_EMULATION*/

 private: /* private methods for internal use */

  // Port number of this out-channel; computed from the serialno and
  // the entity's preset port numbers by DML.
  prime::ssf::uint32 portno();

 private: /* internal member data */

#if SSF_SHARED_DATA_SEGMENT
  // Runtime context to facilitate the SSF_USE_RTX_PRIVATE method.
  void* __rtx__;
#endif
 
  // The owner entity of this out-channel.
  Entity* _oc_owner;

  // Name of the out-channel. NULL if the channel is not published.
  char* _oc_name;

#if PRIME_SSF_EMULATION
  /*
   * Used by real-time out-channel; points to the function that
   * becomes the writer thread interacting with the real-time
   * device. Null indicates this is not a special channel.
   */
  void (*_oc_threadfct)(outChannel*, void*);

  // User-defined data to be passed to the writer thread function.
  void* _oc_threadctx;

  // The starter function of the writer thread.
  static void _oc_start_thread(outChannel* oc);

  // Used by real-time out-channel; points to the writer thread.
  ssf_thread_type* _oc_thread;

  // The thread ID used by memory management.
  int _oc_pid;

  ssf_thread_mutex _oc_realque_mutex;
  ssf_thread_cond _oc_realque_cond;
  ssf_event_list* _oc_realque;

#endif /*PRIME_SSF_EMULATION*/

  // Each out-channel has a unique serial number in an entity. To
  // derive a unique number for the entire model, we must also
  // include the serial number of the owner entity.
  int _oc_serialno;
  
  // An appointment out-channel, which is a subclass of this, is not
  // in the timeline graph.
  boolean _oc_in_timeline_graph;

  // Channel delay assigned to this out-channel.
  ltime_t _oc_channel_delay;

  // Each out-channel maintains a list of timeline targets for each
  // timelines it has a mapping to one or more in-channels.
  SSF_OC_TML_VECTOR _oc_timeline_list;

  /* list of friendly classes for opening access */
  friend class Entity;
  friend class Process;
  friend class Event;
  friend class inChannel;

  friend class ssf_timeline;
  friend class ssf_logical_process;
  friend class ssf_kernel_event;
  friend class ssf_universe;
  friend class ssf_teleport;
}; // class outChannel

/**
 * \brief Publishes an unnamed out-channel for DML access.
 * \param oc points to an unnamed out-channel.
 * \param name is the character string name.
 *
 * If the user decides to use DML file to create model, the name of
 * the outchannel must be published so that it could be referenced by
 * the kernel to establish channel mapping from DML
 * specification. Unlike in-channel, out-channel names are not global.
 */
#define SSF_PUBLISH_DML_OUTCHANNEL(oc, name) (oc)->dml_publish(name)

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef outChannel SSF_OutChannel;
typedef outChannel SSF_outChannel;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_OUTCHANNEL_H__*/

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
