/**
 * \file fluid_event.h
 * \brief Header file for the FluidEvent class.
 * \author Ting Li
 * 
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

#ifndef __FLUID_EVENT_H__
#define __FLUID_EVENT_H__

#include "ssf.h"
#include "fluid_hop.h"

namespace prime {
namespace ssfnet {

class FluidEvent : public SSFNetEvent {
 public:
  /** Fluid class id. */
  FLUID_CLASSID class_id;

  /** The uid of the peer network interface. */
  UID_t peer_uid;

  /** The value of the fluid event (depending on the type). */
  float fluid_value;

  /** The time of the event is scheduled to happen (Runge-Kutta steps). */
  long fluid_step;

  FluidHop::PathMap forward_nics;

  FluidHop::PathMap reverse_nics;

 public:
  /** The default constructor. */
  FluidEvent();

  /** The constructor with specified arguments. */
  FluidEvent(FLUID_CLASSID cid, UID_t peer, int community, SSFNetEvent::Type typ, float val, long step) :
	  SSFNetEvent(typ,community), class_id(cid), peer_uid(peer), fluid_value(val), fluid_step(step) {}
  FluidEvent(FLUID_CLASSID cid, UID_t peer, int community, SSFNetEvent::Type typ, float val, long step,
		  FluidHop::PathMap f_nics, FluidHop::PathMap r_nics) :
	  SSFNetEvent(typ,community), class_id(cid), peer_uid(peer), fluid_value(val), fluid_step(step),
	  forward_nics(f_nics), reverse_nics(r_nics)  {}

  /** The copy constructor. */
  FluidEvent(const FluidEvent& evt);

  /** The destructor. */
  virtual ~FluidEvent() {}

  /** This method is required by SSF for any simulation event to clone
      itself, which is necessary when the simulation kernel delivers
      the event across processor boundaries. */
  virtual Event* clone() { return new FluidEvent(*this); }

  /** This method is required by SSF to pack the event object before
      the event is about to be shipped to a remote machine. It is
      actually used to serialize the event object into a byte stream
      represented as an ssf_compact object. */
  virtual prime::ssf::ssf_compact* pack();

  /** Unpack the fluid event, reverse process of the pack method. */
  virtual void unpack(prime::ssf::ssf_compact* dp);

  /** This method is the factor method for deserializing the event
      object. A new fluid event object is created. */
  static prime::ssf::Event* createFluidEvent
    (prime::ssf::ssf_compact* dp);

 public:
  /** Returns a new registration event. */
  #ifdef SSFNET_FLUID_ROUNDTRIP
  static FluidEvent* new_register(FLUID_CLASSID cid, UID_t peer, int community, float pathlen, long step, bool forward_path,
		  FluidHop::PathMap f_nics, FluidHop::PathMap r_nics) {
	  return new FluidEvent(cid, peer, community, forward_path?SSFNetEvent::SSFNET_FLUID_REGISTER_EVT:SSFNetEvent::SSFNET_FLUID_REGISTER_REVERSE_EVT,
			  pathlen, step, f_nics, r_nics);
  }
  #else
  static FluidEvent* new_register(FLUID_CLASSID cid, UID_t peer, int community, float pathlen, long step, FluidHop::PathMap f_nics, FluidHop::PathMap r_nics) {
    return new FluidEvent(cid, peer, community, SSFNetEvent::SSFNET_FLUID_REGISTER_EVT, pathlen, step, f_nics, r_nics);
  }
  #endif

  /** Returns a new unregistration event. */
  static FluidEvent* new_unregister(FLUID_CLASSID cid, UID_t peer, int community) {
    return new FluidEvent(cid, peer, community, SSFNetEvent::SSFNET_FLUID_UNREGISTER_EVT, 0, 0);
  }

  /** Returns a new arrival update event. */
  static FluidEvent* new_arrival(FLUID_CLASSID cid, UID_t peer, int community, float arrival, long step) {
    return new FluidEvent(cid, peer, community, SSFNetEvent::SSFNET_FLUID_ARRIVAL_EVT, arrival, step);
  }

  /** Returns a new accumulative delay update event. */
  static FluidEvent* new_accu_delay(FLUID_CLASSID cid, UID_t peer, int community, float delay, long step) {
    return new FluidEvent(cid, peer, community, SSFNetEvent::SSFNET_FLUID_ACCU_DELAY_EVT, delay, step);
  }

  /** Returns a new accumulative loss update event. */
  static FluidEvent* new_accu_loss(FLUID_CLASSID cid, UID_t peer, int community, float loss, long step) {
    return new FluidEvent(cid, peer, community, SSFNetEvent::SSFNET_FLUID_ACCU_LOSS_EVT, loss, step);
  }

  /** Used by SSF to declare an event class. */
  SSF_DECLARE_EVENT(FluidEvent);
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__FLUID_EVENT_H__*/
