/*
 * internal.h :- internal classes and definitions for SSF API.
 */

#ifndef __PRIME_SSF_INTERNAL_H__
#define __PRIME_SSF_INTERNAL_H__

#include "primex_config.h"
#include "mac/machines.h"
#include "mac/quickmem.h"
#include "mac/segments.h"
#include "sim/kernelevt.h"
#include "sim/timeline.h"
#include "sim/procedure.h"

namespace prime {
namespace ssf {

  class Entity; 
  class Event;
  class Process;
  class inChannel;
  class outChannel;

  //typedef idml::Enumeration Enumeration;

  class ssf_alignment_request {
  public:
    enum { INDEP, ALIGN };
    int type;
    Entity* ent1;   // which entity initiates the request
    union {
      int proc;     // INDEP: processor to assign to
      Entity* ent2; // ALIGN: entity to align to
    };
    ssf_alignment_request* next; // link to next request in queue
  }; /*ssf_alignment_request*/

  class ssf_map_request {
  public:
    enum { POINTER, STRING };
    int type;
    outChannel* oc;   // the out-channel that initiates the request
    union {
      inChannel* ic;  // POINTER: the in-channel that is mapped or unmapped to
      char* icname;   // STRING: name of the in-channel to be mapped or unmapped
    };
    ltime_t delay;    // mapping delay
    ssf_map_request* next; // link to next request in queue
  }; /*ssf_map_request*/

  /*
   * The association between an in-channel and a process waiting on
   * the in-channel is implemented as a cross-link of ssf_wait_node
   * objects. The wait node is a quick object, since the simulator
   * needs to quickly set up and tear down the association during run
   * time.
   */
  class ssf_wait_node : public ssf_quickobj {
  public:
    // Points to the process this node belongs.
    Process* p;

    // Points to the in-channel this node belongs.
    inChannel* ic;

    /*
     * Points to the next in-channel the process is waiting on. A
     * single linked list here will do since the in-channels are
     * removed from the process's dynamic sensitivity list after the
     * wait call returns.
     */
    ssf_wait_node* next_p;

    /* 
     * Points to the previous process that is also sensitive to the
     * in-channel. These processes need to be removed individually and
     * there a double-linked list is used here for faster access.
     */
    ssf_wait_node* prev_c;

    // Points to the next process sensitive to the in-channel.
    ssf_wait_node* next_c;
  }; /*ssf_wait_node*/

#if 0
  /*
   * The class is used as the base class for objects that can be
   * disposed explicitly by the user. It contains the dispose() method
   * for the user to get rid of the object. However, it is up the
   * system to determine when to reclaim it.
   */
  class ssf_disposable {
  private:
    // Mark whether the object has been disposed by the user.
    boolean _disp_disposed;

  protected:
    /*
     * The constructor initializes the data structure. It is protected
     * so that only a subclass can create instances.
     */
    ssf_disposable() : _disp_disposed(false) {}
    
    /*
     * Used by the subclasses to determine whether the object has been
     * disposed of by the user.
     */
    boolean disposed() { return _disp_disposed; }

  public:
    /*
     * The method is called by the user to dispose the object. Any
     * subclass should overload this method dealing with this
     * situation and the method in the subclass must call the this
     * method in the base class.
     */
    virtual void dispose() { _disp_disposed = true; }

  }; /*ssf_disposable*/
#endif

  enum {
    SSF_SIMPHASE_PREPARATION,
    SSF_SIMPHASE_INITIALIZATION,
    SSF_SIMPHASE_RUNNING,
    SSF_SIMPHASE_WRAPUP
  };

  typedef void (*ssf_shmem_barrier_callback)(long handle, ltime_t now, void* data);

  class ssf_shmem_barrier_appointment {
  public:
    ltime_t time_to_happen;
    long handle;
    ssf_shmem_barrier_callback callback;
    void* data;
    ltime_t period;
    
    ssf_shmem_barrier_appointment(ltime_t t, long h, ssf_shmem_barrier_callback f,
		       void* d, ltime_t p = SSF_LTIME_ZERO) :
      time_to_happen(t), handle(h), callback(f), 
      data(d), period(p) {}
  };

  struct ssf_less_shmem_barrier_appointment : public SSF_LESS(ssf_shmem_barrier_appointment*) {
    bool operator()(ssf_shmem_barrier_appointment* t1, ssf_shmem_barrier_appointment* t2) {
      // smaller timestamp means higher priority
      return t1->time_to_happen > t2->time_to_happen;
    }
  };

  // barrier appointment queue
  typedef SSF_PRIORITY_QUEUE
    (ssf_shmem_barrier_appointment*, SSF_VECTOR(ssf_shmem_barrier_appointment*), 
     ssf_less_shmem_barrier_appointment) ssf_shmem_barrier_queue;
  
  // main function is substituted.
  extern int ssf_default_main(int argc, char** argv);
  extern int ssf_register_main(int (*)(int, char**));

#ifndef main
#define main \
  ssf_main_tmp0 = 0; \
  extern int ssf_main(int argc, char** argv); \
  int ssf_main_tmp1 = prime::ssf::ssf_register_main(ssf_main); \
  int ssf_main
#endif

}; // namespace ssf
}; // namespace prime

/*
 * ssf execution context
 */

#define SSF_CONTEXT_SIMPHASE		SSF_USE_RTX_PRIVATE(simphase)
#define SSF_CONTEXT_DECADE		SSF_USE_RTX_PRIVATE(decade)
#define SSF_CONTEXT_NOW			SSF_USE_RTX_PRIVATE(now)
#define SSF_CONTEXT_PROCESSING_TIMELINE	((ssf_timeline*)SSF_USE_RTX_PRIVATE(processing_timeline))
#define SSF_CONTEXT_RUNNING_PROCESS	((Process*)SSF_USE_RTX_PRIVATE(running_process))
#define SSF_CONTEXT_ENVIRON_MUTEX	SSF_USE_SHARED(environ_mutex)
#define SSF_CONTEXT_ENVIRONMENT		((SSF_MAP(SSF_STRING,SSF_STRING)*)SSF_USE_SHARED(environ))
#define SSF_CONTEXT_UNIVERSE		((ssf_universe*)SSF_USE_RTX_PRIVATE(universe))
#define SSF_CONTEXT_FLOWMARK		SSF_USE_SHARED_RO(flowmark)
#define SSF_CONTEXT_FLOWDELTA		SSF_USE_RTX_PRIVATE(flowdelta)
#define SSF_CONTEXT_STARTIME		SSF_USE_SHARED(startime)
#define SSF_CONTEXT_ENDTIME		SSF_USE_SHARED(endtime)
#define SSF_CONTEXT_KEVT_WATERMARK	SSF_USE_RTX_PRIVATE(kevt_watermark)
#define SSF_CONTEXT_DATAFILE		SSF_USE_RTX_PRIVATE(datafile)

#define SSF_CONTEXT_PROCESSING_TIMELINE_ASSIGN(x) SSF_USE_RTX_PRIVATE(processing_timeline)=x
#define SSF_CONTEXT_RUNNING_PROCESS_ASSIGN(x) SSF_USE_RTX_PRIVATE(running_process)=x

#define SSF_STATS_TIMELINE_CONTEXTS	SSF_USE_RTX_PRIVATE(timeline_contexts)
#define SSF_STATS_PROCESS_CONTEXTS	SSF_USE_RTX_PRIVATE(process_contexts)
#define SSF_STATS_KERNEL_EVENTS		SSF_USE_RTX_PRIVATE(kernel_events)
#define SSF_STATS_KERNEL_MESSAGES	SSF_USE_RTX_PRIVATE(kernel_messages)
#define SSF_STATS_KERNEL_XMEM_MESSAGES	SSF_USE_RTX_PRIVATE(kernel_messages_xmem)
#define SSF_STATS_KERNEL_XPRC_MESSAGES	SSF_USE_RTX_PRIVATE(kernel_messages_xprc)
#define SSF_STATS_DIRECT_CALLBACKS	SSF_USE_RTX_PRIVATE(direct_callbacks)

#endif /*__PRIME_SSF_INTERNAL_H__*/

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
