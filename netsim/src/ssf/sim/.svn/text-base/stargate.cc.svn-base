/*
 * stargate :- connection between logical processes.
 *
 * ssf_stargate is the data structure used for communication between
 * timelines that potentially belong to different processors, or
 * different memory spaces.
 */

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

ssf_stargate::ssf_stargate(ssf_logical_process* lp1, ssf_logical_process* lp2) :
  src(lp1), tgt(lp2), srcid(lp1->serialno()), tgtid(lp2->serialno())
{
  constructor();
}

ssf_stargate::ssf_stargate(ssf_logical_process* lp1, int lp2id) :
  src(lp1), tgt(0), srcid(lp1->serialno()), tgtid(lp2id)
{
  constructor();
}

ssf_stargate::ssf_stargate(int lp1id, ssf_logical_process* lp2) :
  src(0), tgt(lp2), srcid(lp1id), tgtid(lp2->serialno())
{
  constructor();
}

ssf_stargate::ssf_stargate(ssf_logical_process* lp) :
  src(0), tgt(0) /*, srcid(0), tgtid(0)*/
{
  constructor();
  tgt = lp; // not assigned above! so that it's not added to tgt.inbound
}

void ssf_stargate::constructor()
{
  delay = SSF_LTIME_INFINITY;
  in_sync = 0;

  if(src) src->outbound.push_back(this);
  if(tgt) tgt->inbound.push_back(this);

  time = SSF_LTIME_MINUS_INFINITY;
  busy = critical = 0;

  ssf_mutex_init(&mbox_mutex);
  mbox = 0;
}

ssf_stargate::~ssf_stargate()
{
  assert(!mbox); // no left-over events
}

void ssf_stargate::set_delay(ltime_t t)
{
  if(t <= SSF_LTIME_ZERO) ssf_throw_exception(ssf_exception::kernel_xdelay);
  if(t < delay) delay = t;
}

void ssf_stargate::drop_message(ssf_kernel_event* evt)
{
  SSF_USE_PRIVATE(kernel_messages)++;
#if PRIME_SSF_INSTRUMENT
  src->messageSent(tgtid);
#endif
  
  if(!tgt) {
    // this is a remote memory message delivery
#if PRIME_SSF_DISTSIM
    ((ssf_teleport*)SSF_USE_SHARED(teleport))->transport(this, evt);
    SSF_USE_PRIVATE(kernel_messages_xmem)++;
#else
    assert(0); // impossible if running on shared-memory only
#endif
  } else {
    // this is local memory message delivery
    if(evt->type == ssf_kernel_event::EVTYPE_CHANNEL) {
      evt->type = ssf_kernel_event::EVTYPE_INCHANNEL;
      evt->mapnode = evt->localtlt->remote_tlt->ic_head;
    } else { assert(0); } // no other type accepted

    if(in_sync) { // via synchronous channel
      if(src->universe != tgt->universe) { // different universe
	SSF_USE_PRIVATE(kernel_messages_xprc)++;
#if 1 /*undef SSF_EVENT_NOCOPY*/
	if(evt->usrdat) { // an explicit copy is needed
	  Event* e = evt->usrdat->clone()->_evt_sys_reference();
	  evt->usrdat->_evt_sys_unreference();
	  evt->usrdat = e;
	}
#endif
      }
      // drop the event into the target universe's calendar queue
      /*
      printf("evtime=%g, decade=%g\n", (double)evt->time, 
	     (double)SSF_USE_PRIVATE(decade));
      */
      assert(evt->time >= SSF_USE_PRIVATE(decade));
      src->universe/*SSF_CONTEXT_UNIVERSE*/->binque.insert_event(evt);
    } else { // via asynchronous channel
      assert(src);
      if(src->universe == tgt->universe) { // same processor
	tgt->insert_event(evt);
      } else { // across processors
	SSF_USE_PRIVATE(kernel_messages_xprc)++;
#if 1 /*undef SSF_EVENT_NOCOPY*/
	if(evt->usrdat) { // an explicit copy is needed
	  Event* e = evt->usrdat->clone()->_evt_sys_reference();
	  evt->usrdat->_evt_sys_unreference();
	  evt->usrdat = e;
	}
#endif
	if(evt->time >= SSF_USE_PRIVATE(decade)) {
	  src->universe/*SSF_CONTEXT_UNIVERSE*/->binque.insert_event(evt);
	} else { // deliver through the mailbox
	  assert(!evt->context);
	  ssf_mutex_wait(&mbox_mutex);
	  evt->context = mbox;
	  mbox = evt;
	  ssf_mutex_signal(&mbox_mutex);
	}
      }
    }
  }
}

void ssf_stargate::take_messages()
{
  assert(tgt); // taken by the target timeline
  if(!src || src->universe != tgt->universe) {
    // this is a receiver proxy, or a cross-processor arc; otherwise,
    // the mailbox is not used
    ssf_mutex_wait(&mbox_mutex);
    ssf_kernel_event* evt = mbox;
    mbox = 0;
    ssf_mutex_signal(&mbox_mutex);
    while(evt) {
      ssf_kernel_event* nxt = (ssf_kernel_event*)evt->context;
      assert(evt->time < SSF_USE_PRIVATE(decade));
      evt->context = 0; // reset for sake of sanity checking
      tgt->insert_event(evt);
      evt = nxt;
    }
  }
}

#if PRIME_SSF_DISTSIM
ssf_remote_tlt* ssf_stargate::map_remote_tlt(int entno, int portno)
{
  //printf("map_remote_tlt: ent=%d, port=%d\n", entno, portno); fflush(0);
  SSF_MAP(SSF_PAIR(int,int),ssf_remote_tlt*)::iterator iter = 
    remote_tlt_map.find(SSF_MAKE_PAIR(entno, portno));
  if(iter != remote_tlt_map.end())
    return (*iter).second;
  else return 0;
}

void ssf_stargate::map_remote_tlt(int entno, int portno, ssf_remote_tlt* tlt)
{
  //printf("map_remote_tlt: ent=%d, port=%d => add\n", entno, portno); fflush(0);
  remote_tlt_map.insert
    (SSF_MAKE_PAIR(SSF_MAKE_PAIR(entno, portno), tlt));
}
#endif /*PRIME_SSF_DISTSIM*/

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

/*
 * $Id$
 */

