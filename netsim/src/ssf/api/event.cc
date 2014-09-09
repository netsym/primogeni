/*
 * event :- standard SSF event.
 *
 * An event is a message that is sent from one entity to another
 * through SSF channels. An event written to an out-channel will be
 * delivered by the system to all in-channels that are mapped to the
 * out-channel with a delay that is the sum of the delay defined by
 * the out-channel, the mapping delay, and the per-write delay
 * provided at the time when the write method is called. A
 * user-defined event that carries useful data must derive from this
 * event class.
 */

#include <assert.h>

#include "ssf.h"
#include "sim/composite.h"

namespace prime {
namespace ssf {

SSF_EVT_STR_SET* Event::_evt_registered_event_names = 0;
SSF_EVT_FAC_VECTOR* Event::_evt_registered_event_factories = 0;

SSF_REGISTER_EVENT(Event, Event::_evt_create_instance);

Event::Event() : _evt_usrref(1), _evt_sysref(0), _evt_sysown(false) {}

Event::Event(const Event& evt) :
  _evt_usrref(1), _evt_sysref(0), _evt_sysown(false) {}

Event::~Event() {
  if(_evt_sysref < 0) { // this branch is used specifically by the system to delete events
    assert(_evt_sysown && _evt_usrref == 0);
    return; 
  }
#if PRIME_SSF_SCRUTINY
  if(_evt_sysown) ssf_throw_exception(ssf_exception::event_sysevt);
  assert(_evt_sysref == 0);
  if(_evt_usrref != 1) ssf_throw_exception(ssf_exception::event_alias);
#endif
}

Event* Event::clone() { return new Event(*this); }

Event* Event::save() {
  if(!_evt_sysown) _evt_sysown = true;
  else _evt_usrref++; 
  return this; 
}

void Event::release() {
#if PRIME_SSF_SCRUTINY
  if(_evt_usrref <= 0 || _evt_sysref < 0)
    ssf_throw_exception(ssf_exception::event_release);
#endif
  _evt_usrref--;
  if(_evt_usrref == 0 && _evt_sysref == 0) {
    if(!_evt_sysown) 
      ssf_throw_exception(ssf_exception::event_reference);
    _evt_sysref--; // mark it special for sanity check
    delete this;
  }
}

boolean Event::aliased() { 
  assert(_evt_sysref >= 0 && _evt_usrref >= 0);
  if(_evt_usrref+_evt_sysref <= 1) return false;
  else return true;
}

Event* Event::_evt_sys_reference() {
  if(!_evt_sysown) {
    assert(_evt_sysref == 0);
    _evt_sysown = true;
    _evt_usrref--;
    if(_evt_usrref < 0)
      ssf_throw_exception(ssf_exception::event_reference);
  }
  _evt_sysref++;
  return this;
}

void Event::_evt_sys_unreference() {
  assert(_evt_sysown);
  assert(_evt_sysref > 0 && _evt_usrref >= 0);
  _evt_sysref--;
  if(_evt_sysref == 0 && _evt_usrref == 0) {
    _evt_sysref--; // mark for special sanity check
    delete this;
  }
}

int Event::_evt_register_event(const char* evtname, SSF_EVENT_FACTORY factory)
{
  if(!evtname) ssf_throw_exception(ssf_exception::event_regname);
  if(!factory) ssf_throw_exception(ssf_exception::event_regcall);

  if(!_evt_registered_event_names) {
    _evt_registered_event_names = new SSF_EVT_STR_SET();
    assert(_evt_registered_event_names);
    _evt_registered_event_factories = new SSF_EVT_FAC_VECTOR();
    assert(_evt_registered_event_factories);
  }
  if(!_evt_registered_event_names->insert(evtname).second)
    ssf_throw_exception(ssf_exception::event_regdup, evtname);
  _evt_registered_event_factories->push_back(factory);
  return _evt_registered_event_factories->size()-1;
}

Event* Event::_evt_create_instance(ssf_compact* data) {
  assert(data == 0);
  return new Event();
}

Event* Event::_evt_create_registered_event(int id, ssf_compact* data) {
  if(_evt_registered_event_factories &&
     0 <= id && id < (int)_evt_registered_event_factories->size()) {
    return (*(*_evt_registered_event_factories)[id])(data);
  } else return 0;
}

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
