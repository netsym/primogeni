/**
 * \file entity.h
 * \brief Standard SSF entity interface.
 *
 * This header file contains definition of the SSF entity class
 * (prime::ssf::Entity), as well as a number of other classes designed
 * to interface with the entity class. The user should not include
 * this header file explicitly, as it has been included by ssf.h
 * automatically.
 */

#ifndef __PRIME_SSF_ENTITY_H__
#define __PRIME_SSF_ENTITY_H__

#ifndef __PRIME_SSF_H__
#error "entity.h can only be included by ssf.h"
#endif

namespace prime {
namespace ssf {

class ssf_entity_dml_param;

/**
 * \brief The factory method for an SSF entity.
 *
 * An entity factory is a callback function that takes a list of
 * parameters (of type ssf_entity_dml_param) and returns a newly
 * created entity object. Each registered entity must have a entity
 * factory so that the kernel will be able to create entity objects
 * from the DML model specification.
 */
typedef Entity* (*SSF_ENTITY_FACTORY)(const ssf_entity_dml_param**);

typedef SSF_QVECTOR(ssf_kernel_event*) SSF_ENT_KEVT_VECTOR;
typedef SSF_QSET(inChannel*) SSF_ENT_IC_SET;
typedef SSF_QSET(outChannel*) SSF_ENT_OC_SET;
typedef SSF_QSET(Process*) SSF_ENT_PRC_SET;
typedef SSF_QMAP(long,ssf_kernel_event*) SSF_ENT_INTKEVT_MAP;
typedef SSF_QMAP(SSF_STRING,inChannel*) SSF_ENT_STRIC_MAP;
typedef SSF_QMAP(SSF_STRING,outChannel*) SSF_ENT_STROC_MAP;
typedef SSF_QMAP(prime::ssf::uint32,inChannel*) SSF_ENT_INTIC_MAP;
typedef SSF_QMAP(prime::ssf::uint32,outChannel*) SSF_ENT_INTOC_MAP;
typedef SSF_MAP(SSF_STRING,SSF_ENTITY_FACTORY) SSF_ENT_STRFAC_MAP;

/**
 * \brief The parameter taken from the DML script for creating SSF
 * entities.
 *
 * This class is an extension to the standard SSF API. It is used for
 * building an SSF model from the DML model description. The kernel
 * creates an entity object for each ENTITY attribute defined in
 * DML. This is done through an entity factory callback function
 * registered by the user. A null-terminated list of
 * ssf_entity_dml_param objects is passed to the user-defined callback
 * function representing the parameter list specified in DML.
 */
class ssf_entity_dml_param {
public:
  /**
   * \brief There are only three valid parameter types that can be
   * decoded from the DML model description.
   */
  enum param_type_t {
    /// to avoid zero for better error resistance.
    TYPE_NONE = 0,

    /// long integer type.
    TYPE_INTEGER = 1,

    /// double floating-point number type.
    TYPE_FLOAT = 2,

    /// character string type.
    TYPE_STRING = 3,

#if PRIME_SSF_BACKWARD_COMPATIBILITY
    INTEGER = 1,
    FLOAT   = 2,
    STRING  = 3
#endif
  };

  /// Returns the type of this parameter.
  int get_type() const { return _param_type; }

  /**
   * \brief Retrieves the long integer type parameter.
   * \returns true if the value of the parameter is successfully retrieved.
   */
  boolean get_value(long& intval) const;

  /**
   * \brief Retrieves the double type parameter.
   * \returns true if the value of the parameter is successfully retrieved.
   */
  boolean get_value(double& fltval) const;

  /**
   * \brief Retrieves the string type parameter.
   * \returns true if the value of the parameter is successfully retrieved.
   *
   * The user must call the method with the right type. Note that the
   * user only gets a pointer to the string. The memory storing the
   * string belongs to the system. It is important to know that the
   * user is expected to make a copy of the string if it's to be used
   * later in a different context.
   */
  boolean get_value(const char*& strval) const;

#if PRIME_SSF_BACKWARD_COMPATIBILITY
  /*
   * The following methods are provided for backward
   * compatibility. They don't have protections against mistype
   * errors.
   */
  long intval() const { return _param_intval; }
  double floatval() const { return _param_fltval; }
  const char* strval() const { return _param_strval.c_str(); }
#endif

private:
  /*
   * The constructors are defined as private. The life cycle of an
   * ssf_entity_dml_param object is controlled by the system, although the
   * object is accessible to the user through the factory method when
   * creating entity objects.
   */
  ssf_entity_dml_param(long intval);
  ssf_entity_dml_param(double fltval);
  ssf_entity_dml_param(const char* strval);

  // Type of the parameter.
  int _param_type;

  // Value of the parameter. We use union here to save memory. But we
  // can't put string as member of the union.
  union {
    long   _param_intval;
    double _param_fltval;
  };
  SSF_STRING  _param_strval;

  friend class Entity;
  friend class ssf_dml_model;
}; /*ssf_entity_dml_param*/

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef ssf_entity_dml_param EntityParam;
#endif

/**
 * \brief The data entry used for user-level data collection.
 *
 * Each data entry includes a user-defined type, the simulation time
 * when the record is written, and the serial number of the entity
 * that generates the record. The user data is encapsulated in a
 * ssf_compact object, which could be NULL. The
 * Entity::retrieve_data() method returns an enumeration of this
 * ssf_entity_data objects. Therefore the data entry can inspected by
 * the user. The ssf_entity_data objects are maintained by the system;
 * the user cannot create or delete an ssf_entity_data object
 * directly.
 */
class ssf_entity_data {
public:
  /// Returns the serial number of the source entity.
  int entity() { return _entdat_entity; }

  /// Returns the user-defined type of the recorded data.
  int type() { return _entdat_type; }

  /// Returns the simulation time of this record.
  ltime_t time() { return _entdat_time; }

  /// Returns the data as a compact object.
  ssf_compact* data() { return _entdat_data; }

#if PRIME_SSF_BACKWARD_COMPATIBILITY
  ssf_compact* packed() { return data(); }
#endif

private:
  // Constructor: it's protected; the system's generating the object
  // for the users upon request.
  ssf_entity_data(int entity, int type, ltime_t time, ssf_compact* data = 0) :
    _entdat_entity(entity), _entdat_type(type), _entdat_time(time),
    _entdat_data(data) {}

  // Destructor: protected so that the user won't delete it
  // accidentally.
  ~ssf_entity_data() { if(_entdat_data) delete _entdat_data; }

  // The entity who dumped the data.
  int _entdat_entity;

  // User-defined type of this record.
  int _entdat_type;

  // Simulation time at which this record's been written.
  ltime_t _entdat_time;

  // User data is encapsulated into the ssf_compact object.
  ssf_compact* _entdat_data;

  friend class Entity;
  friend class ssf_entity_data_enumeration;
}; /*ssf_entity_data*/

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef ssf_entity_data DataDumped;
typedef ssf_entity_data DataLog;
#endif

/**
 * \brief Standard SSF entity class.
 *
 * An entity is a container for state variables. For example, a router
 * or a host in a network simulation can be modeled as an entity. A
 * user-defined entity must derive from the base entity class.
 *
 * Since a derived entity class can contain procedures (used by
 * Process), the entity class should be a subclass of the
 * ssf_procedure_container class.
 */
class Entity : public ssf_procedure_container, public prime::dml::Configurable {

 public: /* standard public interface */

#if PRIME_SSF_EMULATION
  /**
   * \brief The entity contructor.
   * \param emu indicates whether this entity should keep up with the
   * real time.
   *
   * In SSF, the user supposedly can create entities and delete them
   * dynamically during the simulation. It is highly recommended that
   * all entities are created at the beginning of the simulation and
   * one should leave it as such for the entire simulation. The newly
   * created entity belongs to the current processing timeline. (If
   * the entity is created from the main function, the processing
   * timeline is the default one created by the system known as the
   * root timeline). When the entity is created, the processing
   * timeline will schedule an internal event (at the same simulation
   * time) to invoke the entity's init() method.  Entities can also be
   * created implicitly at the start of the simulation from the DML
   * model specification provided as a command-line argument.
   *
   * <b>In PRIME SSF, entities are static objects during
   * simulation. One can only create entities during simulation
   * initialization. Once created, the entities should not be
   * reclaimed.</b>
   *
   * We added an extension to this entity constructor for real-time
   * simulation: the user can make this entity an emulation entity,
   * which means that the simulation time of this entity will be
   * aligned with the real wall-clock time.
   */
  Entity(boolean emu = false);
#else
  /**
   * \brief The entity contructor.
   *
   * In SSF, the user can create entities and delete them dynamically
   * during the simulation, although doing so is not
   * recommended. Preferably all entities are created at the beginning
   * of the simulation. The newly created entity belongs to the
   * current processing timeline. (If the entity is created from the
   * main function, the processing timeline is the default one created
   * by the system known as the root timeline). When the entity is
   * created, the processing timeline will schedule an internal event
   * (at the same simulation time) to invoke the entity's init()
   * method.  Entities can also be created implicitly at the start of
   * the simulation from the DML model specification provided as a
   * command-line argument.
   */
  Entity();
#endif /*PRIME_SSF_EMULATION*/

  /**
   * \brief Initializes the entity right after the entity is created.
   *
   * The init method is called by the system at the same simulation
   * time as the time when the entity is created. That is, when an
   * entity is created, a kernel event is inserted into its timeline's
   * event list with the current simulation time as its timestamp.
   * When the event is next processed, the system will then invoke the
   * entity's init() method. The method in this base class is virtual
   * and does nothing by default; the user is expected to override
   * this method in the derived entity class.
   */
  virtual void init() {}

  /**
   * \brief Wraps up the entity before the entity will be reclaimed.
   *
   * The wrapup method is called by the system as soon as the entity
   * is abouot to be detroyed (i.e., before the destructor is
   * invoked). The method in the base class is virtual and does
   * nothing by default; the user may override this method in the
   * derived entity class. Note that all living entities will be
   * destroyed automatically at the end of the simulation and their
   * wrapup methods will be called one by one.
   */
  virtual void wrapup() {}

  /**
   * \brief Returns the current simulation time of this entity.
   *
   * In SSF, there is no global simulation time. Only co-aligned
   * entities share the same simulation clock. That is, entities in
   * the same alignment group (called a timeline) is guaranteed to
   * advance synchronously in simulation time. Entities not co-aligned
   * may experience different simulation times. Therefore, it is
   * incorrect for an entity to access the state variables of another
   * entity of a different timeline. Time advancement of the entities
   * is dictated by the underlying parallel simulation mechanism. The
   * return value of this method is the current simulation time of
   * this entity and its co-aligned entities. The return value is
   * undefined if the method is called before the simulation starts
   * (i.e., before joinAll is called in the main function) and also
   * after the simulation has finished (after joinAll returns).
   */
  ltime_t now();

#if PRIME_SSF_EMULATION
  /**
   * \brief Returns the current wall-clock time in simulation time
   * units.
   *
   * This method is an SSF extension to to support real-time
   * emulation. It returns the current wall-clock time in simulation
   * time units, which means that, if the simulation starts at real
   * time T0 and virtual time t0, and if the current real time is T
   * and the emulation ratio is r, then the returned simulation time
   * is (T-T0)*r+t0.
   */
  ltime_t real_now();
#endif

  /**
   * \brief Returns the timeline of this entity.
   *
   * The alignment method returns a pointer to a timeline object,
   * which is the data structure that represents the alignment group
   * containing this entity. The timeline object itself is internal to
   * the implementation; that's why the return value is a pointer to a
   * generic type. Entities with the same pointer value are co-aligned
   * and can directly access each other's state variables without
   * having to use the SSF message passing methods (through channels).
   */
  Object* alignment() { return (Object*)_ent_timeline; }

  /**
   * \brief Changes the alignment of this entity.
   * \param s points to the target entity that this entity is expected to co-align with.
   * \returns a timestamp indicating the simulation time when the alignment change can be accomplished.
   *
   * The alignto method is used to change the current alignment of the
   * entity and make itself align with an entity that is provided as
   * an argument. If both entities share the same timeline, nothing
   * will happen. Since we must use a pointer to refer to target
   * entity, both entities must be in the same address space. This
   * method returns a simulation time at which this alignment change
   * is expected to take effect. The alignment relationship of the
   * current entity remains unchanged until the simulation time
   * reaches the returned timestamp.
   */
  ltime_t alignto(Entity* s);

  /**
   * \brief Creates a new timeline only for this entity.
   * \param p is the id of the designated processor the timeline will be located.
   * \returns a timestamp indicating the simulation time when the alignment change can be accomplished.
   *
   * The makeIndependent method is used to change the current
   * alignment of the entity and creates a new timeline for itself. A
   * timestamp at which the alignment change takes effect is
   * returned. The alignment remains unchanged until the simulation
   * time reaches the returned timestamp. Note that alignment
   * relationship is not transitive: entities currently co-aligned
   * with the this entity will not change the alignment relationships
   * between themselves, unless they explicitly make alignment
   * changes. This method simply removes the calling entity from the
   * current timeline and creates a new one for itself. If the
   * designated processor id is given, the newly created timeline is
   * assigned to that processor. By default, the processor id is -1,
   * which means that the system assigns this new timeline
   * automatically to a processor (in a round-robin fashion).
   */
  ltime_t makeIndependent(int p = -1);

  /**
   * \brief Returns a list of entities co-aligned with this entity.
   *
   * This method returns a null-terminated list of pointers to all
   * entities that share the same timeline with this entity, including
   * this entity. That is, at least one entity will be returned by
   * this method. The returned array belongs to the system and is only
   * temporarily accessible to the user in the current context, which
   * lasts until the user event processing function returns or the
   * control of the process proceeds to the next wait statement. The
   * array could be reused by the system in a different context. This
   * means that the user must not change the array, reclaim the array,
   * or store the pointer to the array for later access.
   */
  Entity** coalignedEntities();

  /**
   * \brief Returns a list of processes owned by this entity.
   *
   * The processes method returns a null-terminated list of pointers
   * to all processes this entity owns, no matter whether they are
   * currently active or not. The array belongs to the system and is
   * only temporarily accessible to the user in the current context.
   */
  Process** processes();

  /**
   * \brief Returns a list of in-channels owned by this entity.
   *
   * The inChannels method returns a null-terminated array of pointers
   * to all in-channels this entity owns. The array belongs to the
   * system and is only temporarily accessible to the user in the
   * current context.
   */
  inChannel** inChannels();

  /**
   * \brief Returns a list of out-channels owned by this entity.
   *
   * The outChannels method returns a null-terminated array of
   * pointers of all out-channels this entity owns. The array belongs
   * to the system and is only temporarily accessible to the user in
   * the current context.
   */
  outChannel** outChannels();

#if PRIME_SSF_INSTRUMENT
  /* Returns the text name of the entity. */
  SSF_STRING* entityName() { return _ent_name; }

  /* Sets the text name of the entity. */
  void setEntityName(SSF_STRING entName) {
    if (_ent_name) delete _ent_name;
    _ent_name = new SSF_STRING(entName.c_str());
  }
#endif

#if PRIME_SSF_EMULATION
  /**
   * \brief Runs the simulation from time zero to the given end time.
   * \param endtime is the end time of the simulation run.
   * \param r is the emulation ratio (default to 1).
   *
   * The startAll method provides the start time (zero in this case)
   * and the end time of the simulation run. The simulation will
   * execute over this time interval, \b inclusive of both
   * endpoints. This static method is expected to be called by the
   * user in the main function before the joinAll() method is called.
   *
   * As an extension to support real-time simulation, we add an extra
   * parameter to the startAll method to indicate the emulation speed,
   * which is defined by the number of simulation time ticks (ltime_t)
   * needed for each real-time second. The default is 1.0. That is,
   * the emulation runs in sync with the wall-clock time.
  */
  static void startAll(ltime_t endtime, double r = 1.0);

  /**
   * \brief Runs the simulation from the given start time to the given end time.
   * \param starttime is the start time of the simulation run.
   * \param endtime is the end time of the simulation run.
   * \param r is the emulation ratio (default to 1).
   *
   * The startAll method provides the start time and the end time of
   * the simulation run.It is expected that the start time is earlier
   * than the end time. The simulation will execute over this time
   * interval, \b inclusive of both endpoints. This static method is
   * expected to be called by the user in the main function before the
   * joinAll() method is called.
   *
   * As an extension to support real-time simulation, we add an extra
   * parameter to the startAll method to indicate the emulation speed,
   * which is defined by the number of simulation time ticks (ltime_t)
   * needed for each real-time second. The default is 1.0. That is,
   * the emulation runs in sync with the wall-clock time.
   */
  static void startAll(ltime_t starttime, ltime_t endtime, double r = 1.0);

  /**
   * \brief Changes the the emulation speed.
   * \param ltime_wallclock_ratio is the ratio of simulation time to real time.
   * \param condition points to the function that determines the throttling termination condition.
   * \param context is the context pointer for passing user data to the condition function.
   *
   * Throttling is done by changing the ratio of simulation time to
   * real time, maintained by the kernel for advancing the simulation
   * clock relative to the wall-clock time. The ratio was initially
   * set by the Entity's startAll() method. Presumably, if the ratio
   * is set to be "infinite" (using a large double-precision
   * floating-point number like 1e38), the emulation is equivalent to
   * pure simulation: events are processed in the system as fast as
   * possible. If the ratio is set to any constant, the simulation
   * clock advances proportionally to the real time clock by this
   * constant factor. Particularly, if the ratio is one, the
   * simulation progresses in sync with the wall-clock time. If the
   * ratio is zero, the simulation simply halts. No event will be
   * executed and the system will not advance the simulation
   * clock. It's an error to set a negative ratio. Once an emulation
   * execution ratio is set, the kernel will maintain this ratio until
   * the throttling termination condition becomes true. That is, if
   * the callback function "condition" is not NULL and when called
   * returns true, the kernel will reset the ratio back to the initial
   * value set by the startAll() method in the beginning of
   * simulation. The condition function has five parameters: the first
   * is the current wall-clock time, the second is the wall-clock time
   * when the throttle started, the third is the current simulation
   * time, the fourth is the simulation time when the throttle
   * started, and last one is a user-defined object.  Note that once
   * you set the ratio to be "infinite", there's no turning back. The
   * simulation kernel will treat all events (including both external
   * and internal events) as backlog. Therefore, if you want to "catch
   * up" a simulation, use a reasonable constant factor to speed up
   * the simulation rather than infinite. In this case, you can later
   * resume normal emulation speed once the simulation time catches up
   * with wall-clock time.
   */
  static void throttle(double ltime_wallclock_ratio, boolean (*condition)
		       (double, double, ltime_t, ltime_t, void*) = 0,
		       void* context = 0);
#else
  /**
   * \brief Runs the simulation from time zero to the given end time.
   * \param endtime is the end time of the simulation run.
   *
   * The startAll method provides the start time (zero in this case)
   * and the end time of the simulation run. The simulation will
   * execute over this time interval, \b inclusive of both
   * endpoints. This static method is expected to be called by the
   * user in the main function before the joinAll() method is called.
   */
  static void startAll(ltime_t endtime);

  /**
   * \brief Runs the simulation from the given start time to the given end time.
   * \param starttime is the start time of the simulation run.
   * \param endtime is the end time of the simulation run.
   *
   * The startAll method provides the start time and the end time of
   * the simulation run.It is expected that the start time is earlier
   * than the end time. The simulation will execute over this time
   * interval, \b inclusive of both endpoints. This static method is
   * expected to be called by the user in the main function before the
   * joinAll() method is called.
   */
  static void startAll(ltime_t starttime, ltime_t endtime);
#endif

  /**
   * \brief The main thread awaits the simulation to finish.
   *
   * This method is called from the main function (or in a subroutine
   * invoked by the main function) \b after the startAll() method has
   * been called. In this implementation, this is actually the method
   * that starts the simulation run from the simulation, where the
   * startAll() method only prepares the system for the simulation
   * run. The joinAll method returns only after the simulation
   * finishes.
   */
  static void joinAll();

 public: /* extensions to the standard public interface */

  /* The following methods are shortcuts to the corresponding methods
     in the Process class. These methods can only be called within
     procedures, that is, in a process context. Look for detailed
     descriptions of these methods in the Process class. */

  /** \name Shortcuts to the Corresponding Process Methods
   * @{
   */
  /// \brief Returns whether the current process is simple.
  boolean isSimple();

  /// \brief Blocks the current process for message arrival at any of
  /// the in-channels.
  void waitOn(inChannel**);

  /// \brief Blocks the current process for message arrival at an
  /// in-channel.
  void waitOn(inChannel*);

  /// \brief Blocks (and terminates) the current process forever.
  void waitForever();

  /// \brief Blocks the current process forever, but keeps the object.
  void suspendForever();

  /// \brief Suspends the current process for the given period of
  /// time.
  void waitFor(ltime_t);

  /// \brief Suspends the current process until the specified time.
  void waitUntil(ltime_t);

  /// \brief Blocks the current process for message arrival on any of
  /// the in-channels, but only for the given time interval.
  boolean waitOnFor(inChannel**, ltime_t);

  /// \brief Blocks the current process for message arrival on an
  /// in-channel, but only for the given time interval.
  boolean waitOnFor(inChannel*, ltime_t);

  /// \brief Blocks the current process for message arrival on any of
  /// the in-channels, but only until the specified time.
  boolean waitOnUntil(inChannel**, ltime_t);

  /// \brief Blocks the current process for message arrival on an
  /// in-channel, but only until the specified time.
  boolean waitOnUntil(inChannel*, ltime_t);

  /// \brief Blocks on a set of in-channels set by the waitsOn()
  /// method previously.
  void waitOn();

  /// \brief Blocks on a set of in-channels set by the waitsOn()
  /// method previously, but only for the given period of time.
  boolean waitOnFor(ltime_t);

  /// \brief Blocks on a set of in-channels set by the waitsOn()
  /// method previously, but only until the specified time.
  boolean waitOnUntil(ltime_t);

  /// \brief Specifies a set of in-channels the current process will
  /// be waiting on later.
  void waitsOn(inChannel**);

  /// \brief Specifies a single in-channels the current process will
  /// be waiting on later.
  void waitsOn(inChannel*);

  /// \brief Returns the list of in-channels containing arrival
  /// events.
  inChannel** activeChannels();
  /** @} */

  /* The following methods do not conform to the SSF Specification and
     therefore may not be portable among different SSF
     implementations. */

  /**
   * \brief Configures the entity from DML specification.
   * \param cfg is the DML configuration of this entity.
   *
   * The config method is called if the entity is created from the DML
   * specification. The DML configuration that corresponds to the
   * CONFIGURE attribute in an ENTITY attribute is passed in as an
   * argument (as of type prime::dml::Configuration). Note that even
   * when no such CONFIGURE attribute defined in the DML, the method
   * will be called nonetheless with a NULL pointer. The method in the
   * base class is virutal and does nothing. The user is expected
   * overload this method in the derived class.
   */
  virtual void config(prime::dml::Configuration* cfg) {}

  /**
   * \brief Gets the value of the specified environment variable.
   * \param envar is the name of the environment variable.
   * \returns the value of the environment variable or NULL if undefined.
   *
   * The getenv method is a static method that returns the value of
   * the environment variable specified in the argument. Do not
   * confuse the environment variables in SSF with the environment
   * variables in a Unix process. The environment variables we refer
   * here are those that are related to the simulation run. For
   * example, "starttime" is an environment variable that specifies
   * the starting time of this simulation run. The returned string is
   * owned by the system and the user should make an explicit copy if
   * the string is to be used in another context. Environment
   * variables are set by the kernel at the start of the
   * simulation. There are two types of environment variables. One is
   * system-defined variables, such as "starttime". Here we list all
   * environment variables pre-defined by the system. The other type
   * of environment variables is user-defined variables, which are
   * specified in the DML specification using the ENVIRONMENT
   * attribute. The method returns NULL if the variable is undefined.
   */
  static const char* getenv(char* envar);

  /* Direct-event scheduling is a mechanism for the event-oriented
     simulation world-view, which is expected to be faster than the
     process-oriented approach specified in the standard SSF API. The
     scheme is designed to save the process context-switch overhead to
     achieve better performance. */

  /** \name Event-Oriented Methods
   * @{
   */

  /**
   * \brief Schedules an event with the specified delay.
   * \param cb is an entity method that will be invoked to process the event.
   * \param evt is the scheduled event.
   * \param dly is the delay of the event.
   * \returns a handle that can be used to later retract the event.
   *
   * The schedule method inserts an event directly into the event-list
   * with a timestamp equalent to the current simulation time plus the
   * specified delay. When the simulator processes the event, the
   * callback function will be invoked. The callback function is a
   * method of the Entity class or its derive class. The schedule
   * method returns a handle that can be used to retract (i.e., cancel
   * or unschedule) the event. The event presented to the function
   * follows the regular SSF event referencing rules. In particular,
   * the event will no longer be owned by the user after this method
   * is called. Also, when the callback function is invoked, if the
   * event is needed in another context, the user must either make an
   * explicit copy of the event or call Event::save() to obtain a
   * reference of the event,
   */
  long schedule(void (Entity::*cb)(Event*), Event* evt,
		ltime_t dly= SSF_LTIME_ZERO);

  /**
   * \brief Schedules an event with the specified delay.
   * \param p points to a process owned by this entity.
   * \param cb is a method of the proces, which will be invoked to handle the event.
   * \param evt is the scheduled event.
   * \param dly is the delay of the event.
   * \returns a handle that can be used to later retract the event.
   *
   * This method is the same as the previous one except that the
   * callback function is a method of the Process class or its derived
   * class. In this case, the process object, which must owned by this
   * entity or an entity coaligned with this entity, should be
   * provided as the first argument.
   */
  long schedule(Process* p, void (Process::*cb)(Event*),
		Event* evt, ltime_t dly = SSF_LTIME_ZERO);

  /**
   * \brief Retracts a scheduled event.
   * \param h is the handle of the scheduled event.
   * \returns true if the event is cancelled.
   *
   * The unschedule method retracts (or cancels) the event scheduled
   * earlier. The handle to the scheduled event, which was returned
   * from the schedule() method, must be provided as an argument. The
   * method returns true if the event has really been
   * cancelled. Otherwise, it returns false, which means the event has
   * already been processed.
   */
  boolean unschedule(long h);

  /**
   * \brief Schedules an event that will not be retracted.
   * \param cb is an entity method that will be invoked to process the event.
   * \param evt is the scheduled event.
   * \param dly is the delay of the event.
   *
   * The schedule_nonretractable method works similar to the
   * corresponding schedule() method except that no handle to the
   * event is returned. Therefore, the event cannot be canceled in the
   * future. This saves the cost of internal bookkeeping.
   */
  void schedule_nonretractable(void (Entity::*cb)(Event*), Event* evt,
			       ltime_t dly = SSF_LTIME_ZERO);

  /**
   * \param p points to a process owned by this entity.
   * \param cb is a method of the proces, which will be invoked to handle the event.
   * \param evt is the scheduled event.
   * \param dly is the delay of the event.
   *
   * This method is the same as the previous except that the callback
   * function is a Process method.
   */
  void schedule_nonretractable(Process* p, void (Process::*cb)(Event*),
			       Event* evt, ltime_t dly= SSF_LTIME_ZERO);
  /** @} */

  /* Data collection: each processor maintains a data output file used
     for possible post-simulation analysis. Within each output file,
     the data generated by the user are indexed by the entity's serial
     number. Each record contains a user-defined type and a timestamp
     at which the record is written to the file. The output files,
     generated by SSF instances (potentially in a distributed memory
     environment), can be analyzed after the simulation has finished.
     If the model is simple, the user can define a global wrapup
     function and use SSF extended methods (defined later) to
     retrieve the simulation results at the very end of the simulation
     run. */

  /**
   * \brief Creates a record for the entity.
   * \param t is the type of the record.
   * \param d is the data to be recorded.
   *
   * This method creates a record. The first argument specifies the
   * user-defined type of the record; the second one encapsulates the
   * dumped data in a data structure called ssf_compact, which is
   * defined to serialize platform-independent data.  This method is
   * mainly for statistical collection during the simulation. The data
   * recorded will be output to a file, which can be used later for
   * analysis. Alternatively, the recorded data can also be retrieved
   * at the end of the simulation using the retrieveDataDumped()
   * method.
  */
  void dumpData(int t, ssf_compact* d);

  /**
   * \brief Returns an enumeration of the recorded data.
   *
   * This static method is used for collecting statistics after the
   * simulation run. The method returns an enumeration of recorded
   * data (of type ssf_compact) output during the simulation. This
   * method should be called in the global wrapup method (see
   * SSF_SET_GLOBAL_WRAPUP), which is invoked when the simulation
   * has finished (i.e., after the joinAll() method returns).
   */
  static prime::dml::Enumeration* retrieveDataDumped();

  /* The following functions are specified in the SSF API, but are
     not implemented here in SSF. */

  static ltime_t pauseAll() { return SSF_LTIME_INFINITY; }
  static void resumeAll() {}

  /* List of friendly classes that have access to the entity's
     internal implementation. */
  friend class Event;
  friend class Process;
  friend class inChannel;
  friend class outChannel;

  friend class ssf_semaphore;
  friend class ssf_timeline;
  friend class ssf_logical_process;
  friend class ssf_kernel_event;
  friend class ssf_timer;
  friend class ssf_universe;

 protected:

  /*
   * This is entity destructor. It is protected: the user should not
   * delete an entity object directly. Instead, one should use the
   * dispose() method (not implemented yet). Note that the entity
   * destructor is virtual which means that the derived class can
   * provide its own (virtual) destructor that will be invoked before
   * the destructor at the base class; this can be used to clean up
   * the state variables maintained in the subclasses.
   */
  virtual ~Entity();

 private: /* internal member data */

#if SSF_SHARED_DATA_SEGMENT
  // Runtime context to facilitate the SSF_USE_RTX_PRIVATE method.
  void* __rtx__;
#endif

  // Alignment group this entity belongs to.
  ssf_timeline* _ent_timeline;

  // Every entity must have a unique serial number.
  prime::ssf::uint32 _ent_serialno;

#if PRIME_SSF_EMULATION
  // Is this entity a real-time entity?
  int _ent_emuable;
#endif

#if PRIME_SSF_INSTRUMENT
  SSF_STRING* _ent_name;
#endif

  /*
   * These are the next serial numbers to be assigned to an in-channel
   * or an out-channel registered to this entity.
   */
  prime::ssf::uint32 _ent_next_inchannel;
  prime::ssf::uint32 _ent_next_outchannel;

  // The sequence number for the next kernel event.
  prime::ssf::uint32 _ent_next_kevt_seqno;

  // The next handle to be used for direct-event scheduling.
  long _ent_next_sched;

  /*
   * Hold the largest timestamp of events that can be inserted into
   * the timeline's event list. This value can be as large as
   * infinite. Note that events with timestamps beyond the current
   * synchronization window will be inserted into the processor's
   * bin-queue. The value may be changed to a smaller value to
   * temporarily hold events generated by the entity in the local
   * storage (_ent_evtque).
   */
  ltime_t _ent_future;

  // EVTYPE_NEWENTITY event is temporarily stored here.
  ssf_kernel_event* _ent_initevt;

  /*
   * Events are stored here temporarily until the entity joins a
   * timeline and the events can be inserted directly into the
   * timeline's event-list.
   */
  SSF_ENT_KEVT_VECTOR _ent_evtque;

  // Set of incoming channels of this entity.
  SSF_ENT_IC_SET _ent_inchannels;

  // Set of outgoing channels of this entity.
  SSF_ENT_OC_SET _ent_outchannels;

  // Set of processes of this entity.
  SSF_ENT_PRC_SET _ent_processes;

  // Map from the handle to the directly scheduled event.
  SSF_ENT_INTKEVT_MAP _ent_direct_sched;

  // The maximum port number of all inchannels preset by DML. The
  // number is used to compute port numbers of all inchannels owned
  // by this entity, published and unpublished.
  int _ent_max_preset_inport;

  // The maximum port number of all outchannels preset by DML. The
  // number is used to compute port numbers of all outchannels owned
  // by this entity, published and unpublished.
  int _ent_max_preset_outport;

  // Map from string names to a published inchannel object.
  SSF_ENT_STRIC_MAP _ent_icmap;

  // Map from string names to a published outchannel object.
  SSF_ENT_STROC_MAP _ent_ocmap;

  // Map from a port number to a published inchannel object.
  SSF_ENT_INTIC_MAP _ent_icport;

  // Map from a port number to a published outchannel object.
  SSF_ENT_INTOC_MAP _ent_ocport;

  /*
   * Map from the string names to the registered factory methods,
   * which will be invoked by the system to construct the named
   * entities.
   */
  static SSF_ENT_STRFAC_MAP* _ent_registered_entities;

  /*
   * This points to the user-defined global wrapup function to be
   * called at the end of the simulation.
   */
  static void (*_ent_global_wrapup)();

 protected: /* internal helper methods */

  /*
   * Allocate a chunk of memory for the current context. The memory
   * will be reclaimed whenever there's a context switch.
   */
  char* _ent_new_contextual_memory(size_t size);

  // Add a new process to this entity.
  void _ent_add_process(Process* proc) {
    _ent_processes.insert(proc);
  }

  // Remove a process from this entity.
  void _ent_delete_process(Process* proc) {
    _ent_processes.erase(proc);
  }

  // Add a new inchannel to this entity.
  void _ent_add_inchannel(inChannel* ic);

  // Remove an inchannel from this entity.
  void _ent_delete_inchannel(inChannel* ic);

  // Add a new outchannel to this entity.
  void _ent_add_outchannel(outChannel* oc);

  // Remove an outchannel from this entity.
  void _ent_delete_outchannel(outChannel* oc);

  // Publish an inchannel with the given name; an association from
  // the name to the inchannel object is established. Set global to
  // be true if one wants to publish the in-channel's name globally.
  void _ent_publish_inchannel(char* name, inChannel* ic,
			      boolean global = true);

  // Unpublish an inchannel of the given name; destroy the
  // association from the name to the inchannel.
  void _ent_unpublish_inchannel(char* name, inChannel* ic);

  // Publish an outchannel with the given name; an association from
  // the name to the outchannel object is established.
  void _ent_publish_outchannel(char* name, outChannel* oc);

  // Unpublish an outchannel of the given name; destroy the
  // association from the name to the outchannel.
  void _ent_unpublish_outchannel(char* name, outChannel* oc);

  // Preset the port number of the inchannel of the given name. This
  // method is called by the kernel after parsing the submodel DML.
  int _ent_preset_inchannel(const char* name, int portno);

  // Preset the port number of the outchannel of the given name. This
  // method is called by the kernel after parsing the submodel DML.
  int _ent_preset_outchannel(const char* name, int portno);

  // Return the sequence number assigned for the new kernel event.
  prime::ssf::uint32 _ent_new_event() {
    return _ent_next_kevt_seqno++;
  }

  /*
   * Schedule an event of this entity. If the event is indeed
   * scheduled, it is returned. Otherwise, the method reclaims the
   * event and returns NULL.
   */
  ssf_kernel_event* _ent_insert_event(ssf_kernel_event*);

  // Cancel an event of this entity.
  void _ent_cancel_event(ssf_kernel_event*);

  /*
   * Allocate a new handle and establish a mapping from the handle to
   * the schedule event. The new handle is returned.
   */
  long _ent_add_schedule(ssf_kernel_event* kevt);

  /*
   * Delete the mapping from the handle to the kernel event. The
   * kernel event is returned if it is found. The method returns NULL
   * otherwise.
   */
  ssf_kernel_event* _ent_delete_schedule(long handle);

  // Return true if the given entity is coaligned with this entity.
  boolean _ent_coaligned_with(Entity* ent) {
    return ent && _ent_timeline == ent->_ent_timeline;
  }

  void _ent_strip_initevt();
  void _ent_settle_events();

  // Create an entity object of the given name with parameters parsed
  // from the DML model description.
  static Entity* _ent_create_registered_entity
    (const char* entname, const ssf_entity_dml_param**);

 public: /* public methods used internally */

  /*
   * This method is used by the embedded code. The method is used to
   * test whether the running process that just resumed execution from
   * a blocked state is due to time-out or a message arrival. The
   * method redirects the call to the method with the same name in the
   * Process class.
   */
  boolean _ent_is_timedout();

  /*
   * The _ent_register_entity method must be called by every derived
   * entity class (implicitly) that is to be referenced in the DML
   * model file. A mapping will be established as a result so that the
   * kernel can create the entity object from the DML description. The
   * mapping is established by using the REGISTER_ENTITY macro, which
   * informs the kernel to map the name of the entity class to the
   * factory callback function so that the kernel knows how to
   * instantiate the entity when parsing the DML file. The callback
   * function takes a null-terminated list of parameters (each of type
   * ssf_entity_dml_param) decoded from the PARAMS attribute of the ENTITY
   * attribute in the DML model file. The callback function is
   * supposed to return a newly constructed entity object.
   */
  static int _ent_register_entity(char* entname, SSF_ENTITY_FACTORY factory);

  /*
   * The _ent_set_global_wrapup method is used to set a global
   * (callback) function that will be called at the end of the
   * simulation (after the joinAll method returns). There can be only
   * one such function if defined. Calling to this method replaces the
   * previous defined global wrapup function, which is then
   * returned. A null method means to disable the global wrapup
   * function. The method is used by SET_GLOBAL_WRAPUP macro.
   */
  static int _ent_set_global_wrapup(void (*wrapper)());

}; /*Entity*/

/**
 * \brief This macro is used to register an entity.
 * \param x is the name of the entity (no quotes).
 * \param y is the entity factor method, which will be used to
 * create an entity instance.
 *
 * This macro should be used to register a user-defined entity class,
 * which is referenced by the DML model description. It's expected
 * that the mapping will be established between the name of the
 * derived entity class and the callback entity factory method, which
 * will be used by the kernel to construct the named entity from a DML
 * model description.
 */
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_REGISTER_ENTITY(x,y) \
  static int _ent_register_##x##__ = \
  prime::ssf::Entity::_ent_register_entity(#x,y)
#else
#define SSF_REGISTER_ENTITY(x,y) \
  static int _ent_register_/**/x/**/__ = \
  prime::ssf::Entity::_ent_register_entity("x",y)
#endif

/**
 * \brief Sets up a global wrapup function to be called at the end of
 * simulation.
 * \param x points to a function that takes no parameter and returns void.
 *
 * The macro is used to declare a global wrapup function, which is
 * called by the kernel when simulation finishes (after the main
 * function returns). Thus, the user will have a chance to produce a
 * summary the simulation run. The global wrapup function will be
 * called once and only once, and it's only at processor 0 of the
 * machine 0. If the macro is used in more than one places, only
 * one---the one that happens to be the latest one during simulation
 * initialization---will be the global wrapup function. This macro
 * must be used as a global variable declaration, outside the scope of
 * any functions.
 */
#if defined(__STDC__) || defined(__DECCXX)
#define SSF_SET_GLOBAL_WRAPUP(x) \
  static int _ent_global_wrapup__##x##__ = \
  prime::ssf::Entity::_ent_set_global_wrapup(x)
#else
#define SSF_SET_GLOBAL_WRAPUP(x) \
  static int _ent_global_wrapup__/**/x/**/__ = \
  prime::ssf::Entity::_ent_set_global_wrapup(x)
#endif

/**
 * \brief Returns the current running process.
 *
 * This function is undefined outside the process context. That is,
 * the function can only be used within procedures. The function is an
 * SSF extension.
 */
extern Process* current_process();

/**
 * \brief Returns the current simulation time.
 *
 * This global function is an alias of the Entity::now() method. It is
 * therefore undefined when the function is called before the
 * simulation starts (i.e., before Entity::joinAll() is invoked) or
 * after the simulation has finished (i.e., after Entity::joinAll()
 * returns).
 */
extern ltime_t now();

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef Entity SSF_Entity;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_ENTITY_H__*/

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
