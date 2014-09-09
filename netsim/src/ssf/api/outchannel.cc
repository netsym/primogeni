/*
 * outchannel :- standard SSF out-channel interface.
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

#include <assert.h>
#include <string.h>

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

outChannel::outChannel(Entity* theowner, ltime_t channel_delay) :
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _oc_owner(theowner), // owner entity
  _oc_name(0), // clear the name and remain anonymous 
#if PRIME_SSF_EMULATION
  _oc_threadfct(0), // not a special channel
  _oc_threadctx(0), // no data to thread function if special
  _oc_thread(0), // writer thread is not running
#endif
  _oc_in_timeline_graph(true), // default is not an appointment channel
  _oc_channel_delay(channel_delay) // channel delay
{
  if(!theowner) ssf_throw_exception(ssf_exception::oc_owner);
  if(channel_delay < SSF_LTIME_ZERO)
    ssf_throw_exception(ssf_exception::oc_delay, "outChannel()");

  // register with the owner entity (which will assign my serial no.)
  _oc_owner->_ent_add_outchannel(this);
}

outChannel::outChannel(char* thename, Entity* theowner, 
		       ltime_t channel_delay) :
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _oc_owner(theowner), // owner entity
  _oc_name(0), // clear the name and remain anonymous 
#if PRIME_SSF_EMULATION
  _oc_threadfct(0), // not a special channel
  _oc_threadctx(0), // no data to thread function if special
  _oc_thread(0), // writer thread is not running
#endif
  _oc_in_timeline_graph(true), // default is not an appointment channel
  _oc_channel_delay(channel_delay) // channel delay
{
  if(!theowner) ssf_throw_exception(ssf_exception::oc_owner);
  if(channel_delay < SSF_LTIME_ZERO)
    ssf_throw_exception(ssf_exception::oc_delay, "outChannel()");

  // register with the owner entity (which will assign my serial no.)
  _oc_owner->_ent_add_outchannel(this);

  // publish the channel with a name
  if(thename) dml_publish(thename);
}

void outChannel::write(Event* evt, ltime_t per_write_delay)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _oc_owner->_ent_timeline)
    ssf_throw_exception(ssf_exception::oc_align, "write()");
  if(per_write_delay < SSF_LTIME_ZERO) 
    ssf_throw_exception(ssf_exception::oc_delay, "write()");
  
  if(!evt) return;

  ltime_t arise = SSF_CONTEXT_NOW+per_write_delay;
  ssf_kernel_event* event = new ssf_kernel_event
    (arise, ssf_kernel_event::EVTYPE_OUTCHANNEL, evt);
  event->outchannel = this;
  _oc_owner->_ent_insert_event(event);
}

ltime_t outChannel::mapto(inChannel* tgt, ltime_t mapping_delay)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _oc_owner->_ent_timeline)
    ssf_throw_exception(ssf_exception::oc_align, "mapto()");
  if(mapping_delay < SSF_LTIME_ZERO) 
    ssf_throw_exception(ssf_exception::oc_delay, "mapto()");
#if PRIME_SSF_EMULATION
  if(_oc_threadfct)
    ssf_throw_exception(ssf_exception::oc_real, "mapto()");
#endif

  if(!tgt) return SSF_CONTEXT_NOW;

#if PRIME_SSF_EMULATION
  if(tgt->_ic_threadfct)
    ssf_throw_exception(ssf_exception::oc_real, "mapto() in-channel target");
#endif

  /*
  if(SSF_CONTEXT_SIMPHASE != SSF_SIMPHASE_PREPARATION && 
     SSF_CONTEXT_SIMPHASE != SSF_SIMPHASE_INITIALIZATION) {
    ssf_throw_warning("ineffective outChannel::mapto(\"%p\", %g)", 
	       tgt, (double)mapping_delay);
    return SSF_LTIME_INFINITY;
  }
  */

  return SSF_CONTEXT_UNIVERSE->add_mapping(this, tgt, mapping_delay);
}

ltime_t outChannel::mapto(char* icname, ltime_t mapping_delay)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _oc_owner->_ent_timeline)
    ssf_throw_exception(ssf_exception::oc_align, "mapto()");
  if(mapping_delay < SSF_LTIME_ZERO) 
    ssf_throw_exception(ssf_exception::oc_delay, "mapto()");
#if PRIME_SSF_EMULATION
  if(_oc_threadfct)
    ssf_throw_exception(ssf_exception::oc_real, "mapto()");
#endif

  if(!icname || strlen(icname) == 0) 
    return SSF_CONTEXT_NOW;

  /*
  if(SSF_CONTEXT_SIMPHASE != SSF_SIMPHASE_PREPARATION && 
     SSF_CONTEXT_SIMPHASE != SSF_SIMPHASE_INITIALIZATION) {
    ssf_throw_warning("ineffective ssf::outChannel::mapto(\"%p\", %g)", 
	       tgt, (double)mapping_delay);
    return SSF_LTIME_INFINITY;
  }
  */

  return SSF_CONTEXT_UNIVERSE->add_mapping(this, icname, mapping_delay);
}

ltime_t outChannel::unmap(inChannel* tgt)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _oc_owner->_ent_timeline)
    ssf_throw_exception(ssf_exception::oc_align, "unmap()");

  if(!tgt) return SSF_CONTEXT_NOW;

  ssf_throw_warning("ineffective ssf::outChannel::unmap(\"%p\")", tgt);
  return SSF_LTIME_INFINITY;
}

ltime_t outChannel::unmap(char* icname)
{
  if(SSF_CONTEXT_PROCESSING_TIMELINE != _oc_owner->_ent_timeline)
    ssf_throw_exception(ssf_exception::oc_align, "unmap()");

  if(!icname || strlen(icname) == 0) 
    return SSF_CONTEXT_NOW;

  ssf_throw_warning("WARNING: ineffective ssf::outChannel::unmap(\"%s\")", icname);
  return SSF_LTIME_INFINITY;
}

outChannel::~outChannel()
{
#if PRIME_SSF_EMULATION
  if(_oc_threadfct) {
    assert(_oc_realque);
    while(_oc_realque->size() > 0) {
      ssf_kernel_event* evt = _oc_realque->top();
      _oc_realque->pop();
      delete evt;
    }
    delete _oc_realque;
  }
#endif

  // clean up the timeline targets
  if(!_oc_timeline_list.empty()) {
    for(SSF_OC_TML_VECTOR::iterator iter = _oc_timeline_list.begin();
	iter != _oc_timeline_list.end(); iter++)
      delete (*iter);
    _oc_timeline_list.clear();
  }

  // if the channel is published, unpublish it
  if(_oc_name) dml_unpublish();

  // deregister with the owner entity
  _oc_owner->_ent_delete_outchannel(this);
}

prime::ssf::uint32 outChannel::portno()
{
  if(_oc_serialno >= 0) return _oc_serialno;
  else return _oc_owner->_ent_max_preset_outport-_oc_serialno;
}

void outChannel::dml_publish(char* extname)
{
  if(extname) {
#if PRIME_SSF_EMULATION
    if(_oc_threadfct)
      ssf_throw_exception(ssf_exception::oc_pubreal);
#endif
    if(_oc_name) {
      char msg[256];
      sprintf(msg, "[\"%s\"->\"%s\"]", _oc_name, extname);
      ssf_throw_exception(ssf_exception::oc_duppub, msg);
    }
    int len = strlen(extname);
    _oc_name = new char[len+1];
    strcpy(_oc_name, extname);
    _oc_owner->_ent_publish_outchannel(_oc_name, this);
  }
}

void outChannel::dml_unpublish()
{
  if(_oc_name) {
    _oc_owner->_ent_unpublish_outchannel(_oc_name, this);
    delete[] _oc_name;
    _oc_name = 0;
  }
}

#if PRIME_SSF_EMULATION
outChannel::outChannel(Entity* theowner, void (*threadfct)(outChannel*, void*),
		       void* ctx, ltime_t channel_delay) :
#if SSF_SHARED_DATA_SEGMENT
  __rtx__(theowner?theowner->__rtx__:0),
#endif
  _oc_owner(theowner), // owner entity
  _oc_name(0), // clear the name and remain anonymous 
  _oc_threadfct(threadfct), // it's a special channel
  _oc_threadctx(ctx), // data to the thread function
  _oc_thread(0), // writer thread is not running
  _oc_in_timeline_graph(true), // default is not an appointment channel
  _oc_channel_delay(channel_delay) // channel delay
{
  if(!theowner) ssf_throw_exception(ssf_exception::oc_owner);
  if(channel_delay < SSF_LTIME_ZERO)
    ssf_throw_exception(ssf_exception::oc_delay, "outChannel()");

  // register with the owner entity (which will assign my serial no.)
  _oc_owner->_ent_add_outchannel(this);

  ssf_thread_mutex_init(&_oc_realque_mutex);
  ssf_thread_cond_init(&_oc_realque_cond);
  _oc_realque = new ssf_event_list;
}

Event* outChannel::getRealEvent(double& delay_in_real_sec)
{
  // IMPORTANT: this function will be called by another thread
  // (presumably writer thread). SSF's memory management does not
  // apply to this thread. 1. SSF_USE_PRIVATE will not work. We make
  // oc->__rtx__ to point to the private segment so that we can access
  // the CONTEXT (only in case we use threads in SSF for
  // multiprocessing; otherwise, we're still in the same OS process,
  // albeit in another thread). 2. The ssf_kernel_event objects from
  // _oc_realque are specially arranged so that they are NOT created
  // from quick memory. They will be soly owned by the writer thread
  // and should be removed with care. 3. The Event object is also
  // special and owned by the writer thread; in particular, no
  // reference counter scheme shall be used.

  if(!_oc_threadfct)
    ssf_throw_exception(ssf_exception::oc_noreal);
  ssf_thread_mutex_wait(&_oc_realque_mutex);
  if(_oc_realque->size() <= 0) {
    ssf_thread_cond_wait(&_oc_realque_cond, &_oc_realque_mutex);
  }

  ssf_kernel_event* kevt = _oc_realque->top(); assert(kevt);
  _oc_realque->pop();
  ssf_thread_mutex_signal(&_oc_realque_mutex);

  ssf_universe* myuniv = ((ssf_logical_process*)_oc_owner->_ent_timeline)->universe;
  if(myuniv->ltime_wallclock_ratio > 0)
    delay_in_real_sec = (kevt->time-myuniv->wallclock_to_ltime())/
      myuniv->ltime_wallclock_ratio;
  else delay_in_real_sec = 0; // COLLECT DEADLINE MISS STATISTICS HERE!!!

  Event* evt = kevt->usrdat; assert(evt);
  kevt->usrdat = 0;
  delete kevt;
  return evt;
}

void outChannel::_oc_start_thread(outChannel* oc)
{
  //#if SSF_USE_THREADS
  // this is so that getRealEvent will be able to access CONTEXT
  //SSF_SET_PRIVATE_DATA(oc->__rtx__);
  //#endif

  ssf_arena_init(oc->_oc_pid);
  ssf_map_private_segments(oc->_oc_pid);
#if PRIME_SSF_QUICKMEM
  ssf_quickmem_init();
#endif

  (*oc->_oc_threadfct)(oc, oc->_oc_threadctx);
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
