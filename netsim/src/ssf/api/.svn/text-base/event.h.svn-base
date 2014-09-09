/**
 * \file event.h
 * \brief Standard SSF event interface.
 *
 * This header file contains definition of the SSF event class as well
 * as the macros for declaring and registering user-defined event
 * classes that are derived from the prime::ssf::Event class.
 */

#ifndef __PRIME_SSF_EVENT_H__
#define __PRIME_SSF_EVENT_H__

#ifndef __PRIME_SSF_H__
#error "event.h can only be included by ssf.h"
#endif

namespace prime {
namespace ssf {

/**
 * \brief The factory method for an SSF event.
 *
 * An event factory is a user-defined callback function that takes a
 * pointer to the ssf_compact object and returns a newly created event
 * object. A callback function of this type must be provided and
 * registered at the kernel for each user-defined event subclass,
 * using the SSF_DECLARE_EVENT and SSF_REGISTER_EVENT macros. The
 * kernel will map the name of the event class to its factory callback
 * method so that it can instantiate an event object whenever the
 * event is traveling across address spaces. The factory method should
 * take as parameter a pointer to the ssf_compact object; the user is
 * expected to restore the data fields of the derived event class,
 * which are stored in the ssf_compact object previously using the
 * pack() method.
 */
typedef Event* (*SSF_EVENT_FACTORY)(ssf_compact*);
    
typedef SSF_SET(SSF_STRING) SSF_EVT_STR_SET;
typedef SSF_VECTOR(SSF_EVENT_FACTORY) SSF_EVT_FAC_VECTOR;


/* The following macros should be used to register all user-defined
   event classes. The macros are defined differently due to the
   difference in the way C proprocessors handle stringification and
   concatenation. */

/**
 * \brief The macro is used to declare an event class.
 * \param x is the name of the event class (no quotes).
 *
 * The macro must be used \b inside every user-defined event
 * subclass. Internally, this macro creates an identifier for the
 * event class, which will be used to identify the event object when
 * the kernal needs to serialize and ship the event across the address
 * space. The identifier of the event is used at the receiving end to
 * construct the event object and deserialize the data fields. See
 * SSF_REGISTER_EVENT for an example of usage.
 */
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_DECLARE_EVENT(x) \
  static int _evt_evtcls_ident_##x; \
  virtual int _evt_evtcls_ident() { return _evt_evtcls_ident_##x; }
#else
#define SSF_DECLARE_EVENT(x) \
  static int _evt_evtcls_ident_/**/x; \
  virtual int _evt_evtcls_ident() { return _evt_evtcls_ident_/**/x; }
#endif

/**
 * \brief The macro is used to register an event class.
 * \param x is the name of the event class (no quotes).
 * \param y points to the factory callback function that creates event instances.
 *
 * The macro is expected to be used in the source code where the class
 * (in particular, the event factory method of the class) is
 * defined. Every user-defined event subclass must register itself
 * with the kernel using this macro. The factory method should be a
 * static member of the event subclass.
 *
 * The following example describes the use of the event factory method
 * and the two macros for declaring and registering an event subclass:
\verbatim
In myevent.h file:
  class MyEvent : public Event {
    ...
    static MyEvent* my_event_factory(ssf_compact* compact_data);
    SSF_DECLARE_EVENT(MyEvent);
  };

In myevent.cc file:
  ...
  MyEvent* MyEvent::my_event_factory(ssf_compact* compact_data) {
     MyEvent* newevt = new MyEvent;
     compact_data->get_int(&newevt->int_data);
     ...
     return newevt;
  }
  SSF_REGISTER_EVENT(MyEvent, MyEvent::my_event_factory);
\endverbatim
 *
 */
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_REGISTER_EVENT(x,y) \
  int x::_evt_evtcls_ident_##x = \
    prime::ssf::Event::_evt_register_event(#x, y)
#else
#define SSF_REGISTER_EVENT(x,y) \
  int x::_evt_evtcls_ident_/**/x = \
    prime::ssf::Event::_evt_register_event("x", y)
#endif

/**
 * \brief Standard SSF event class.
 *
 * An event is a message that is sent from one entity to another
 * through SSF channels. An event written to an out-channel will be
 * delivered by the system to all in-channels that are mapped to the
 * out-channel with a delay that is the sum of the delay defined by
 * the out-channel, the mapping delay, and the per-write delay
 * provided at the time when the write method is called. A
 * user-defined event that carries useful data must derive from this
 * event class.
 *
 * Since events are commonly used objects in SSF---many events are
 * created and deleted during a simulation run---we apply strict rules
 * for accessing events in order to achieve better efficiency. The
 * event class is thus derived from the ssf_quickobj class to take
 * advantage of faster memory operations.
 */
class Event : public ssf_quickobj {

 public: /* standard public interface */

  /**
   * \brief The evente constructor.
   *
   * A newly created event belongs to the user until it is submitted
   * to the system, for example, when the write() method is called.
   * After that, the system takes control of the event. Especially,
   * the user should not delete an event owned by the system. To keep
   * an event for later access, the user must either make an explicit
   * copy of the event (using the copy constructor), or make an
   * alias/reference to the event through the save() method.
   */
  Event();

  /**
   * \brief The copy constructor. 
   * \param evt is a reference to the event that needs to be copied.
   *
   * The copy constructor is used to make an explicit copy of the
   * event object. The method takes necessary steps to copy the
   * internal member data. All derived event classes must provide
   * their own copy constructor that overload this method, if any
   * member data in the subclass needs to be copied.
   *
   * IMPORTANT: in C++, since the copy constructor is just another
   * constructor of the derived event class, a constructor (regular
   * constructor or copy constructor) of this event base class must be
   * called, either explicitly or implicitly. If you don't specify the
   * constructor of the base class, the default constructor Event()
   * will be called and that may not be the kind of action you
   * expect. So, we recommend that one should always call the base
   * class's copy constructor \b explicitly.
   *
   * Note that, as specified in the event referencing rule, the newly
   * created event belongs to the user until the event is submitted to
   * the system. This applies to the newly created event using this
   * copy constructor. That is, the user can do anything to the newly
   * created event object---anything before the event is submitted to
   * the system (for example, by calling the outChannel::write()
   * method).
   */
  Event(const Event& evt);

  /**
   * \brief The event destructor. 
   *
   * The user can delete an event only when it is not owned by the
   * system. An event is owned by the user from the time it is created
   * to the time it is submitted to the system (e.g., when the
   * outChannel::write() method is called). Trying to delete an event
   * owned by the system will cause an error, which will likely be
   * caught by this destructor. The destructor is virtual, meaning
   * that the user can overload this method if there is extra data
   * field that is needed to be reclaimed in the derived event class.
   */
  virtual ~Event();

  /**
   * \brief To clone an event.
   *
   * The clone method is used to make an explicit copy of an event
   * object. The user is expected to provide such a method in the
   * derived event class. Whenever it is called, a new instance of the
   * event is returned.  Presumably, the overloaded method is
   * implemented as follows:
\verbatim
Event* myEvent::clone() { return new myEvent(*this); }
\endverbatim
   * The method is called by the kernel when the kernel decides it
   * needs to copy an event object, for example, when shipping an
   * event across the address space. Failing to provide such a method
   * will cause a derived event object to be cloned incorrectly.
   */
  virtual Event* clone();

  /**
   * \brief Increments the reference counter of this event.
   *
   * The save method is used to support the reference counter scheme
   * of the event objects. This method makes an additional reference
   * to the event. Therefore, the event will not be reclaimed when a
   * context switch happens in the simulation kernel.  This method is
   * used when the user wants to save an event, which has just been
   * created or received from an in-channel so that the user can
   * access it later in simulation time. In SSF, by default, a
   * received event is only temporarily accessible to the user, the
   * system can reclaim it implicitly after the current simulation
   * process becomes suspended. This method can be called more than
   * once on the same event object. The system will not release the
   * event's storage until a matching number of calls to the release
   * method have been encountered. Note that modification on a
   * referenced copy of an event will change all other copies
   * (maintained either by the user or the system). Therefore, one can
   * only change the content of an event when the event is not aliased
   * elsewhere (see the aliased() method). This method returns a
   * pointer of the same event object for convenience.
   */
  Event* save();

  /**
   * \brief Decrements the reference counter of this event.
   *
   * The release method is called when the user decides to delete a
   * reference to an event previously saved. If the number of calls
   * to the release method matches the number of calls to the save
   * method, the event can be reclaimed by the kernel. Releasing a
   * newly created event (not yet submitted to the system) causes the
   * event to be reclaimed at once.
   */
  void release();

  /**
   * \brief Returns true if the event has multiple aliases.
   *
   * An event is aliased if there is more than one place using this
   * event simultaneously. This is possible because there can be
   * multiple in-channels mapped to the same out-channel, or there can
   * be more than one process waiting on the same in-channel and
   * having access to the same incoming event. This aliasing condition
   * cautions the user to keep from modifying the content of the
   * event. Otherwise, others would see unexpected change. If the
   * method returns false, the caller can be sure that there is only
   * one reference to the event, which therefore can be modified
   * without danger.
   */
  boolean aliased();

  /**
   * \brief Packs this event into a byte stream before sending it to a
   * remote machine.
   *
   * This method is used to serialize the event object. The user is
   * responsible to pack the data fields in the derived event object
   * when the event is about to be shipped by the kernel to a remote
   * machine in a distributed simulation environment. The packed
   * (serialized) data will be unpacked (deserialized) at the
   * destination machine. This is done by the kernel calling the
   * factory method of the corresponding event class, which should
   * have been registered using SSF_DECLARE_EVENT and
   * SSF_REGISTER_EVENT macros. The pack method must return a pointer
   * to a ssf_compact object, which is created to encapsulate the
   * result of serialization.  At this base class, the method does
   * nothing other than returning a NULL pointer, which indicates to
   * the kernel that the base class does not need to serialize its
   * data fields.
   */
  virtual ssf_compact* pack() { return 0; }

  /**
   * \brief  returns whether this event is owned by the system.
   * If it is not owned by the system is can be freed directly.
   */
  bool isOwnedBySystem() { return _evt_sysown; }
    
 private: /* internal member data */

  // User reference counter.
  int _evt_usrref;

  // System reference counter.
  int _evt_sysref;

  // Indicate whether the event is owned by the system.
  boolean  _evt_sysown;
    
  /*
   * A set of event class names. Each simulation instance should have
   * an identical set of event classes declared by the program. We use
   * pointers here to avoid the potential problem of having a
   * different ordering in the initialization of static data objects
   * in C++. The ordering is important to guarantee consistency of the
   * event class identification across the memory space. When
   * initialized to zero, they are assigned before any other static
   * variables are to be initialized. This is important for us to
   * implement the SSF_REGISTER_EVENT macro.
   *
   * FIXME: this may not be a portable solution; the restriction can
   * be relaxed if we can derive the event class id from the event's
   * canonical string name.
   */
  static SSF_EVT_STR_SET* _evt_registered_event_names;

  /*
   * All event factory methods that have been registered in the
   * program are all listed here. All event classes are assigned with
   * an event type id, which is actually the index of this vector.
   */
  static SSF_EVT_FAC_VECTOR* _evt_registered_event_factories;

 private: /* internal helper methods */

  // Make a system reference to the event object.
  Event* _evt_sys_reference();

  // Remove a system reference to the event object.
  void _evt_sys_unreference();
    
 public: /* public methods used internally */

  /*
   * This method creates an event object from the given integer id and
   * the packed data. A NULL will be returned if no such event class
   * is registered or if the event object cannot be successfully
   * created from the packed data given.
   *
   * This needs to be public so it can be used during boot-straping by non-ssf code
   */
  static Event* _evt_create_registered_event(int id, ssf_compact* data);

  /*
   * The method is used by the SSF_REGISTER_EVENT macro to establish a
   * link between the name of an event class and its factory method.
   * The return value is not as important as tricking the C++ to call
   * this method during global initialization.
   */
  static int _evt_register_event(const char* evtname, SSF_EVENT_FACTORY factory);

  /*
   * The method is a factory method of the base event class. The
   * method takes a pointer to the ssf_compact object, which is the
   * data structure used for serialization and deserialization, and
   * returns a pointer to a newly created event object. The user
   * should define a similar method for each derived event class and
   * register it along with the name of the derived event class using
   * the SSF_REGISTER_EVENT macro.
   */
  static Event* _evt_create_instance(ssf_compact* data);
    
  // All event must be declared and registered including the base class.
  SSF_DECLARE_EVENT(Event);

  /* List of friendly classes that have access to the event's internal
     implementation. */

  friend class Entity;
  friend class Process;
  friend class inChannel;
  friend class outChannel;

  friend class ssf_kernel_event;
  friend class ssf_stargate;
  friend class ssf_teleport;
  friend class ssf_universe;
}; /*Event*/

#if PRIME_SSF_BACKWARD_COMPATIBILITY
#define DECLARE_EVENT SSF_DECLARE_EVENT
#define REGISTER_EVENT SSF_REGISTER_EVENT
typedef Event SSF_Event;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_EVENT_H__*/

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
