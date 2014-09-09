/*
 * stargate :- connection between logical processes.
 *
 * ssf_stargate is the data structure used for communication between
 * timelines that potentially belong to different processors, or
 * different memory spaces.
 */

#ifndef __PRIME_SSF_STARGATE_H__
#define __PRIME_SSF_STARGATE_H__

#ifndef __PRIME_SSF_COMPOSITE_H__
#error "stargate.h must be included by composite.h"
#endif

namespace prime {
namespace ssf {

  // This data structure represents the mapping from an out-channel
  // of one timeline to one or more in-channels that belong to
  // another timeline. It's the portal local to the timeline in which
  // the out-channel belongs.
  class ssf_local_tlt : public ssf_permanent_object {
  public:
    outChannel* oc; // the out-channel that originates the mapping     
    ltime_t min_offset; // minimum delay (=channel+mapping)
    ssf_stargate* stargate; // the stargate between the two timelines
    ssf_remote_tlt* remote_tlt; // points to the remote port if in the same address space
  }; /*ssf_local_tlt*/

  // This data structure represents the mapping from an out-channel
  // of one timeline to one or more in-channels of another
  // timeline. This is the portal that belongs to the remote
  // timeline.
  class ssf_remote_tlt : public ssf_permanent_object {
  public:
    ssf_stargate* stargate; // the stargate between the two timelines
    long nics; // number of in-channels mapped in the receiving timeline
    ssf_map_node *ic_head; // points to the head of the list of in-channels
    ssf_map_node *ic_tail; // points to the tail of the list of in-channels
  }; /*ssf_remote_tlt*/

  // Each ssf_map_node object represents a mapped in-channel from an
  // out-channel. The object is organized by the ssf_remote_tlt object.
  class ssf_map_node : public ssf_permanent_object {
  public:
    inChannel* ic; // points to the in-channel
    ltime_t map_delay; // mapping delay from the out-channel to the in-channel
    ssf_map_node* prev; // previous mapped in-channel in the same timeline
    ssf_map_node* next; // next mapped in-channel in the same timeline
  }; /*ssf_map_node*/

  // The stargate is the data structure for connecting two timelines
  // that could potentially belong to different processors or
  // different memory spaces. It's an arc in the LP graph.
  class ssf_stargate : public ssf_permanent_object {
  public:
    ssf_logical_process* src; // source LP of the arc (null if on remote machine)
    ssf_logical_process* tgt; // target LP of the arc (null if on remote machine)
    int srcid; // the serial number of the source LP
    int tgtid; // the serial number of the target LP
    ltime_t delay; // minimum of all channels from src to tgt timeline
    int in_sync; // true if synchronous (composite synchronization)

    volatile ltime_t time; // time of this arc (updated by the source)
    volatile int busy;     // channel scanning: to avoid race condition
    volatile int critical; // channel scanning: contains minumum safe time

    ssf_mutex mbox_mutex; // protect simultaneous access to the mailbox
    ssf_kernel_event* mbox;      // mailbox for events from src to tgt

    // both src and tgt are timelines in the same address space
    ssf_stargate(ssf_logical_process* src, ssf_logical_process* tgt);

    // the tgt timeline is on a remote machine (this is a proxy)
    ssf_stargate(ssf_logical_process* src, int tgtid);

    // the src timeline is on a remote machine (this is a proxy)
    ssf_stargate(int srcid, ssf_logical_process* tgt);

    // this is a special stargate; it's private stargate for each LP
    // in the sync group
    ssf_stargate(ssf_logical_process* tgt);

    // the destructor
    virtual ~ssf_stargate();

    // do the common work for all constructors
    void constructor();

    // register a mapping delay for each channel between the two
    // timelines; we select the minimum
    void set_delay(ltime_t);

    void set_time(ltime_t t) { time = t+delay; }
    volatile ltime_t get_time() { return time; }
    
    void drop_message(ssf_kernel_event*);
    void take_messages();

#if PRIME_SSF_DISTSIM
    // for remote stargate only

    // mapping from entity id and (out-channel) port id to the remote
    // timeline target.
    SSF_MAP(SSF_PAIR(int,int),ssf_remote_tlt*) remote_tlt_map;

    // accessing the map.
    ssf_remote_tlt* map_remote_tlt(int entno, int portno);
    void map_remote_tlt(int entno, int portno, ssf_remote_tlt*);
#endif
  }; /*ssf_stargate*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_STARGATE_H__*/

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

/*
 * $Id$
 */

