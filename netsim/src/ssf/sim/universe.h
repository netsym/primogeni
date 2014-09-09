/*
 * universe :- parallel universe, one for each processor.
 */

#ifndef __PRIME_SSF_UNIVERSE_H__
#define __PRIME_SSF_UNIVERSE_H__

namespace prime {
namespace ssf {

  typedef SSF_SET(ssf_logical_process*) SSF_UNIV_LP_SET;
  typedef SSF_VECTOR(ssf_logical_process*) SSF_UNIV_LP_VECTOR;
  typedef SSF_DEQUE(ssf_logical_process*) SSF_UNIV_LP_DEQUE;
  typedef SSF_MAP(SSF_STRING,inChannel*) SSF_UNIV_STRIC_MAP;
  typedef SSF_MAP(SSF_STRING,int) SSF_UNIV_STRINT_MAP;
  typedef SSF_MAP(SSF_STRING,SSF_STRING) SSF_UNIV_STRSTR_MAP;
  typedef SSF_MAP(ltime_t,int) SSF_UNIV_TMINT_MAP;
  typedef SSF_MAP(long,void*) SSF_UNIV_INTPTR_MAP;
  typedef SSF_MAP(SSF_STRING,void*) SSF_UNIV_STRPTR_MAP;
  typedef SSF_VECTOR(Entity*) SSF_UNIV_ENT_VECTOR;
  typedef SSF_VECTOR(void*) SSF_UNIV_PTR_VECTOR;
  typedef SSF_VECTOR(inChannel*) SSF_UNIV_IC_VECTOR;

  extern void ssf_global_init_universe();
  extern void ssf_global_wrapup_universe();
  extern void ssf_local_init_universe();
  extern void ssf_local_wrapup_universe();
  extern void ssf_run_universe();

  class ssf_universe : public ssf_permanent_object {
  public:
    ssf_universe(int pid); // constructor
    ~ssf_universe();       // destructor

    void big_bang();   // initialization
    void spin();       // running simulation
    void big_boom();   // finalization

    static void run_setup();  // parallel init
    static void run_continuation(); // complete init and run simulation
#if PRIME_SSF_EMULATION
    static void set_simulation_interval(ltime_t t0, ltime_t t1, double r);
#else
    static void set_simulation_interval(ltime_t t0, ltime_t t1);
#endif

#if PRIME_SSF_EMULATION
    // support for emulation
    ltime_t wallclock_to_ltime();
    ltime_t wallclock_to_ltime(ssf_machine_time rtime);
    void insert_real_time_event(ssf_kernel_event* kevt,
				void (*cb)(ltime_t, void*), void* usrdat);
    void insert_virtual_time_event(ssf_kernel_event* kevt,
				   void (*cb)(ltime_t, void*), void* usrdat);
    void throttle(double ratio, int (*cond)
		  (double, double, ltime_t, ltime_t, void*), void* ctxt);
#endif /*PRIME_SSF_EMULATION*/

    int interrupt_requested(ltime_t nxtdue);

    // these functions are called by recurring global barrier.
/*#if PRIME_SSF_ENABLE_CHECKPOINTING
    static void checkpoint(long handle, ltime_t now, void* data);
#endif*/
    static void progress(long handle, ltime_t now, void* data);

#if PRIME_SSF_INSTRUMENT
  public:

  /**
   * \brief Sets the current action and time mark.
   *
   * This adds the period since the last time mark to the time accumulator 
   * for the previous action.  If the previous action was waiting on input,
   * timings are also accumulated for the timeline that this timeline was
   * waiting on input from.
   */
  void setAction(int action, ssf_machine_time now=0) {
    if (now==0 && currentAction != ACTION_INVALID) now = ssf_get_wall_time();
    if (now>0) {      // now is -1 if only counting not timing
      actionTiming[currentAction]+=now;
      actionTiming[currentAction]-=lastTimeMark;
      lastTimeMark = now;
      currentAction = action;
    } else {
      currentAction = ACTION_INVALID; // don't time this one
    }
    actionCount[action]+=1;
    
/*      if (currentAction == ACTION_SYNCHRONIZING_UPSTREAM) {
        for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
	  if (synchMachines[i]) {
            upSyncTimes[i]+=now;
            upSyncTimes[i]-=lastTimeMark;
          }
        }
      }
      if (currentAction == ACTION_SYNCHRONIZING_DOWNSTREAM) {
        for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
	  if (!synchMachines[i]) {
            downSyncTimes[i]+=now;
            downSyncTimes[i]-=lastTimeMark;
          }
        }
      }*/
//    }
  }
  
  void setSynchMachines(long* machs, int action) {
    synchMachines = machs;
    setAction(action);  
  }

  void dumpInstrumentationData();

  void messageSent(prime::ssf::uint32 toTimeline) {
    messagesSent[toTimeline]++;
  }
#endif /*PRIME_SSF_INSTRUMENT*/

  private:
    /* list of friendly classes for opening access */
    friend class ssf_logical_process;
    friend class ssf_timeline;
    friend class ssf_stargate;
    friend class ssf_teleport;
    friend class Entity;
    friend class inChannel;
    friend class outChannel;
    friend class ssf_entity_data_enumeration;
    friend void ssf_run_universe();

    // Runtime context to facilitate the SSF_USE_RTX_PRIVATE method.
    void* __rtx__;

    int whereami;  // processor id

    ssf_mutex lpset_lock; // serialize access to the LP list
    SSF_UNIV_LP_SET lpset; // list of LPs

    int rolling_lps;        // number of LPs to handle in this epoch
    ssf_lp_queue trampoline;     // queue of LPs ready for execution
#if PRIME_SSF_EMULATION
    ssf_realtime_queue rt_trampoline;  // queue of real-time LPs
#endif
    ssf_mutex prospect_mutex; // lock the access to prospect LP list
    SSF_UNIV_LP_VECTOR prospect; // set of LPs set ready by other processors
    SSF_UNIV_LP_DEQUE reserves; // LPs that have reached the simulation end time

    int total_entities;   // number of entities assigned to this universe
    int preset_entity_id; // serial number of next entity to be created if >=0

    ssf_bin_queue binque; // bin-queue maintained by each processor (at each universe)

    int locked_down;
    int edge_index;
    ltime_t start_training_time;
    ltime_t end_training_time;
    ssf_machine_time start_timer;
    ssf_machine_time end_timer;
    double best_rate;
    ltime_t decade_length;

#if PRIME_SSF_INSTRUMENT
    // the states the universe can be in
    enum {
      ACTION_INVALID = 0,
      ACTION_INITIALIZING,
      ACTION_DETERMINING_NEXT_EVENT,
      ACTION_CANCELLED_EVENT,
      ACTION_NEWENTITY_EVENT,
      ACTION_NEWPROCESS_EVENT,
      ACTION_TIMEOUT_EVENT,
      ACTION_OUTCHANNEL_EVENT,
      ACTION_INCHANNEL_EVENT,
      ACTION_TIMER_EVENT,
      ACTION_SCHEDULE_ENTFCT_EVENT,
      ACTION_SCHEDULE_PRCFCT_EVENT,
      ACTION_SEMSIGNAL_EVENT,
      ACTION_EVENT_POSTPROCESSING,
      ACTION_SYNCHRONIZE_GLOBAL,
      ACTION_SYNCHRONIZE_LOCAL,
      ACTION_SENDING_MESSAGES,
      ACTION_RECEIVING_MESSAGES,
      ACTION_YIELDED,
      ACTION_ENDING,
      ACTION_OTHER,
      ACTION_TOTAL
    };
    static char* actionDescriptions[ACTION_TOTAL];
    ssf_machine_time* actionTiming;
    long* actionCount;
    int currentAction;
    ssf_machine_time currentTime;
    ssf_machine_time lastTimeMark;
    long* synchMachines;
    map<prime::ssf::uint32, ssf_machine_time> upSyncTimes;
    map<prime::ssf::uint32, ssf_machine_time> downSyncTimes;

    map<prime::ssf::uint32, int> messagesRecieved;
    map<prime::ssf::uint32, int> messagesSent;
    

#endif

    static int* automap_array; // random permutation of processor ids
    static int automap_index;  // current index to the permutation

    static int datafile_partition_idx; // indicate which data file we're prcessing ..
    static int datafile_processor_idx; // .. at the wrapup phase

    ssf_machine_time wallclock_startime;
#if PRIME_SSF_EMULATION
    // support for emulation
    double ltime_wallclock_ratio;
    ltime_t simclock_startime;
    ltime_t simclock_reftime;
    ssf_machine_time wallclock_reftime;
    int (*throttle_cond)(double, double, ltime_t, ltime_t, void*);
    void* throttle_context;
    int emuable;
    ssf_thread_mutex emutex;
    ssf_thread_cond econd;
    ssf_kernel_event* emuevt;
    ssf_kernel_event* emuevt_tail;
#endif /*PRIME_SSF_EMULATION*/

    //TimeStat* stats; // statistics
    //int tap_reserve;     // mark whether we're scheduling reserve LPs

  private:
    void link_lp(ssf_logical_process*);   // add an LP to the universe
    void unlink_lp(ssf_logical_process*); // remove an LP from the universe

    void enque_lp(ssf_logical_process*);   // put the LP onto trampoline
    ssf_logical_process* deque_lp();       // select the next ready LP for execution
    void reserve_lp(ssf_logical_process*); // make the LP as final
    void submit_prospect(ssf_logical_process*); // cross-processor scheduling of an LP
    void upgrade_prospect(); // collect LPs droped off by foreign processors

    int new_entity(Entity*);     // add the entity to the list, return a unique id
    void delete_entity(Entity*); // delete the entity from the list
    static int new_logiproc();   // return a globally unique timeline id
  
    static int automap(); // automatic processor assignment for LPs
    
    static void reset_datafiles(); // begin processing data files at system wrapup
#ifdef PRIME_SSF_ARCH_WINDOWS
    static int next_datafile(std::ifstream&); // move to the next data file
#else
    static int next_datafile();
#endif

    ltime_t add_alignment(Entity*, Entity*);
    ltime_t add_alignment(Entity*, int);
    static void now_do_alignment();
    static void really_alignto(Entity*, Entity*);
    static void really_makeindependent(Entity*, int);

    void new_public_inchannel(inChannel*);
    ltime_t add_mapping(outChannel*, inChannel*, ltime_t);
    ltime_t add_mapping(outChannel*, char*, ltime_t);
    static void now_do_mapping();
#if PRIME_SSF_DISTSIM
    static int lpid_to_machid(int);
    static void synchronize_inchannel_names();
#endif

    int expand_epoch();   // compute next global window (epoch)
    void process_epoch(); // process all LPs within the epoch

    void settle_binqueue();
    
    void insert_event(ssf_kernel_event*);
    void cancel_event(ssf_kernel_event*);

    void partition_channels(ltime_t threshold);

    static char* print_memory_usage(char* buf, prime::ssf::uint32 siz);

    static ltime_t first_simulation_barrier();
    static ltime_t redeem_simulation_barrier();

  }; /*ssf_universe*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_UNIVERSE_H__*/

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
