/*
 * logiproc :- definition of a logical process.
 */

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

ssf_logical_process::ssf_logical_process(prime::ssf::uint32 sno) :
  ssf_timeline(sno)
#if PRIME_SSF_EMULATION
  , rtidx(0) 
#endif
{}

ssf_logical_process::ssf_logical_process() :
  ssf_timeline(ssf_universe::new_logiproc())
#if PRIME_SSF_EMULATION
  , rtidx(0)
#endif
{}

ssf_logical_process::~ssf_logical_process()
{
  // reclaim outbound stargates only: we assume an LP owns its
  // outbound stargates (but not inbound stargates).
  for(SSF_LP_SG_VECTOR::iterator iter = outbound.begin();
      iter != outbound.end(); iter++) {
    delete (*iter);
  }
}

void ssf_logical_process::make_ready()
{
  if(simclock < SSF_USE_RTX_PRIVATE(now))
    simclock = SSF_USE_RTX_PRIVATE(now);

  for(SSF_LP_SG_VECTOR::iterator iter = async_inbound.begin();
      iter != async_inbound.end(); iter++) {
    assert((*iter)->src);
    // the following since it's possible the source time has not yet
    // been updated
    if(SSF_USE_RTX_PRIVATE(now)<(*iter)->src->simclock)
      (*iter)->time = (*iter)->src->simclock+(*iter)->delay;
    else (*iter)->time = SSF_USE_RTX_PRIVATE(now)+(*iter)->delay;
    (*iter)->critical = 0;
  }
  universe->enque_lp(this);
}

void ssf_logical_process::map_local_local(outChannel* oc, inChannel* ic, 
				     ltime_t delay)
{
  // create mapnode
  ssf_map_node* node = new ssf_map_node;
  node->ic = ic;
  node->map_delay = delay;

  // try match existing timeline target
  SSF_QVECTOR(ssf_local_tlt*)::iterator titer;
  for(titer = oc->_oc_timeline_list.begin();
      titer != oc->_oc_timeline_list.end(); titer++) {
    if(!(*titer)->stargate && oc->_oc_owner->_ent_timeline == 
       ic->_ic_owner->_ent_timeline) break;
    else if((*titer)->stargate && (*titer)->stargate->tgt &&
	    (*titer)->stargate->tgt == ic->_ic_owner->_ent_timeline) break;
  }
  ssf_local_tlt* found;
  if(titer != oc->_oc_timeline_list.end()) found = *titer;
  else found = 0;

  if(!found) {
    // if not exist, create a new one and add it to the list
    found = new ssf_local_tlt;
    found->oc = oc;
    found->min_offset = delay;

    ssf_remote_tlt* rtlt = new ssf_remote_tlt;
    rtlt->nics = 1;
    rtlt->ic_head = rtlt->ic_tail = node;
    node->prev = node->next = 0;
    found->remote_tlt = rtlt;

    found->stargate = rtlt->stargate = 
      ((ssf_teleport*)SSF_USE_SHARED(teleport))->map_stargate
      ((ssf_logical_process*)oc->_oc_owner->_ent_timeline, 
       (ssf_logical_process*)ic->_ic_owner->_ent_timeline);
    // ic and oc are not on the same timeline!
    // and oc is not appointment channel
    if(found->stargate && oc->_oc_in_timeline_graph) 
      found->stargate->set_delay(oc->_oc_channel_delay+delay);
    oc->_oc_timeline_list.push_back(found);
  } else { 
    // if found exist, adjust min delay and sort the mapnode to the right place
    if(delay < found->min_offset) {
      found->min_offset = delay;
      if(found->stargate && oc->_oc_in_timeline_graph)
	found->stargate->set_delay(oc->_oc_channel_delay+delay);
    }
    ssf_remote_tlt* rtlt = found->remote_tlt;
    rtlt->nics++;
    ssf_map_node* p = rtlt->ic_head;
    while(p && p->map_delay < delay) p = p->next;
    node->next = p;
    if(p) node->prev = p->prev;
    else node->prev = rtlt->ic_tail;
    if(node->prev) node->prev->next = node;
    else rtlt->ic_head = node;
    if(p) p->prev = node;
    else rtlt->ic_tail = node;
  }
}

#if PRIME_SSF_DISTSIM
void ssf_logical_process::map_local_remote(outChannel* oc, int tgt_lp_no,
				      ltime_t delay)
{
  // try match existing timeline target
  SSF_QVECTOR(ssf_local_tlt*)::iterator titer;
  for(titer = oc->_oc_timeline_list.begin();
      titer != oc->_oc_timeline_list.end(); titer++) {
    if((*titer)->stargate && (*titer)->stargate->tgt == 0 &&
       (*titer)->stargate->tgtid == tgt_lp_no) break;
  }
  ssf_local_tlt* found;
  if(titer != oc->_oc_timeline_list.end()) found = *titer;
  else found = 0;

  if(!found) {
    // if not exist, create a new one and add it to the list
    found = new ssf_local_tlt;
    found->oc = oc;
    found->min_offset = delay;

    found->remote_tlt = 0;

    found->stargate = ((ssf_teleport*)SSF_USE_SHARED(teleport))->map_stargate
      ((ssf_logical_process*)oc->_oc_owner->_ent_timeline, tgt_lp_no); 
    assert(found->stargate);
    if(oc->_oc_in_timeline_graph)
      found->stargate->set_delay(oc->_oc_channel_delay+delay);
    else ssf_throw_exception(ssf_exception::kernel_disapt);
    oc->_oc_timeline_list.push_back(found);
  } else { 
    assert(found->stargate);
    // if found exist, adjust min delay
    if(delay < found->min_offset) {
      found->min_offset = delay;
      if(oc->_oc_in_timeline_graph)
	found->stargate->set_delay(oc->_oc_channel_delay+delay);
      else ssf_throw_exception(ssf_exception::kernel_disapt);
    }
  }
}

void ssf_logical_process::map_remote_local(int src_lp_no, int src_ent_no, int src_port,
				      inChannel* ic, ltime_t delay)
{
  // create mapnode
  ssf_map_node* node = new ssf_map_node;
  node->ic = ic;
  node->map_delay = delay;

  // try find remote timeline target in stargate
  ssf_stargate* sg = ((ssf_teleport*)SSF_USE_SHARED(teleport))->map_stargate
    (src_lp_no, (ssf_logical_process*)ic->_ic_owner->_ent_timeline);
  assert(sg);
  ssf_remote_tlt* rtlt = sg->map_remote_tlt(src_ent_no, src_port);
  if(!rtlt) { 
    // if not exist, create remote timeline target
    rtlt = new ssf_remote_tlt;
    rtlt->nics = 1;
    rtlt->ic_head = rtlt->ic_tail = node;
    node->prev = node->next = 0;
    sg->map_remote_tlt(src_ent_no, src_port, rtlt);
  } else {
    // if found exist, sort the mapnode to the right place
    rtlt->nics++;
    ssf_map_node* p = rtlt->ic_head;
    while(p && p->map_delay < delay) p = p->next;
    node->next = p;
    if(p) node->prev = p->prev;
    else node->prev = rtlt->ic_tail;
    if(node->prev) node->prev->next = node;
    else rtlt->ic_head = node;
    if(p) p->prev = node;
    else rtlt->ic_tail = node;
  }
}
#endif /*PRIME_SSF_DISTSIM*/

void ssf_logical_process::run()
{
#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_OTHER);
#endif

  SSF_LP_SG_VECTOR::iterator iter;

  SSF_STATS_TIMELINE_CONTEXTS++;

  // set flow control
  if(!emuable && async_inbound.size() == 0 && 
     SSF_USE_SHARED_RO(flowdelta) > SSF_LTIME_ZERO)
    SSF_USE_RTX_PRIVATE(flowdelta) = simclock+SSF_USE_SHARED_RO(flowdelta);
  else SSF_USE_RTX_PRIVATE(flowdelta) = SSF_LTIME_INFINITY;

#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_RECIEVING_MESSAGES);
#endif
  // block inchannels (set busy), compute the event execution horizon,
  // and settle incoming events
  int keep_c_id = -1;
  safetime = SSF_USE_RTX_PRIVATE(decade);
  int idx = 0;
  for(iter = async_inbound.begin(); iter != async_inbound.end(); 
      iter++, idx++) {
    assert((*iter)->src); // local predecessor
    (*iter)->busy = 1;
    (*iter)->critical = 0;
    ltime_t t = (*iter)->get_time();
    if(t <= safetime) {
      safetime = t;
      keep_c_id = idx;
    }
    (*iter)->take_messages();
  }
#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_OTHER);
#endif

  // enter execution session
  //universe->stats->switch_state(TimeStat::STATE_WORK);
  SSF_USE_RTX_PRIVATE(kevt_watermark) = 0;
  //ltime_t pre_simclock = simclock;
  int interrupted;
  if(emuable) {
#if PRIME_SSF_EMULATION
    if(SSF_USE_RTX_PRIVATE(realnow) < safetime) {
      micro_scheduling(SSF_USE_RTX_PRIVATE(realnow));
      interrupted = (SSF_USE_RTX_PRIVATE(realnow)<SSF_USE_SHARED(endtime));
    } else {
      interrupted = micro_scheduling(safetime);
      //assert(!interrupted); <- it's possible to interrupt an emulation timeline!
      //interrupted = 0;
    }
#else
    assert(0);
#endif
  } else interrupted = micro_scheduling(safetime);
  /*
  printf("%d <= (roll=%d) LP[sno=%d<%s>] microscheduling %g=>%g (decade=%g), real=%g, int=%s\n",
	 SSF_SELF, universe->rolling_lps, serialno(), emuable?"real":"sim", 
	 (double)pre_simclock, (double)simclock, 
	 (double)SSF_USE_RTX_PRIVATE(decade), (double)SSF_USE_RTX_PRIVATE(realnow),
	 interrupted?"yes":"no");
  */
  //universe->stats->switch_state(TimeStat::STATE_SCAN);

  // unblock inchannels and mark critical inchannel
  if(keep_c_id >= 0 && !interrupted && 
     (simclock<SSF_USE_RTX_PRIVATE(decade) && 
      simclock<SSF_USE_SHARED(endtime))) {
    // if it's not going to reschedule itself, let someone else do it!
#if PRIME_SSF_INSTRUMENT
    setAction(ACTION_RECIEVING_MESSAGES);
    waitingOnInputFrom = async_inbound[keep_c_id]->srcid;
#endif
    async_inbound[keep_c_id]->critical = 1;
  }

  // no more busy
  for(iter = async_inbound.begin(); iter != async_inbound.end(); iter++) {
    assert((*iter)->src);
    (*iter)->busy = 0;
  }

#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_SENDING_MESSAGES);
#endif
  // update outgoing channels and possibly unblock subsequent LPs
  for(iter = async_outbound.begin(); iter != async_outbound.end(); iter++) {
    assert((*iter)->tgt);
    (*iter)->set_time(simclock);
    while((*iter)->busy) {
      ssf_yield_proc(); // spin lock
    }
    if((*iter)->critical) {
      //assert((*iter)->tgt);
      (*iter)->critical = 0;
      //universe->stats->switch_state(TimeStat::STATE_SORT);
      universe->submit_prospect((*iter)->tgt);
      //universe->stats->switch_state(TimeStat::STATE_SCAN);
    }
  }
#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_WAITING);
#endif

  if(interrupted) {
    // if it's interrupted in the middle, go reschedule itself
    universe->enque_lp(this);
    //ssf_yield_proc();
  } else {
    // if the time is up, simply relinquish the CPU
    if(simclock >= SSF_USE_SHARED(endtime) ||
       simclock >= SSF_USE_RTX_PRIVATE(decade)) {
      // if the simulaiton has reached the end time, we need to
      // schedule to run all timelines again just to make sure we can
      // process all events at the end time.
      if(SSF_USE_RTX_PRIVATE(decade) > SSF_USE_SHARED(endtime)) {
	if(universe->rolling_lps > 0) { // we didn't tap the reserve yet
	  // add it to the reserve
	  universe->reserve_lp(this);
	  universe->rolling_lps--;
	  if(!universe->rolling_lps) {
	    //printf("[%d] tapping reserve...\n", SSF_SELF);
	    ssf_barrier();
	  }
	} // otherwise, we are currently processing reserved timeline,
	// we do nothing here.
      } else {
	universe->rolling_lps--;
	assert(universe->rolling_lps >= 0);
      }
    }
  }
#if PRIME_SSF_INSTRUMENT
  setAction(ACTION_OTHER);
#endif
}

#if PRIME_SSF_EMULATION
int ssf_logical_process::update_next_due_time(ltime_t realnow)
{
  assert(emuable);
  if(realnow >= SSF_USE_SHARED(endtime)) return 1;

  ltime_t due = next_due_time();
  if(due <= realnow) return 1;

  if(simclock < realnow) {
    simclock = realnow;

    // update outgoing channels and possibly unblock subsequent LPs
    for(SSF_LP_SG_VECTOR::iterator iter = async_outbound.begin(); 
	iter != async_outbound.end(); iter++) {
      assert((*iter)->tgt);
      (*iter)->set_time(simclock);
      while((*iter)->busy) {
	ssf_yield_proc(); // spin lock
      }
      if((*iter)->critical) {
	(*iter)->critical = 0;
	universe->submit_prospect((*iter)->tgt);
      }
    }
  }

  return 0;
}
#endif /*PRIME_SSF_EMULATION*/

}; // namespace ssf
}; // namespace prime

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
