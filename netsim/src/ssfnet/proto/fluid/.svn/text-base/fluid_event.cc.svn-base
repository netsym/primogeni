/**
 * \file fluid_event.cc
 * \brief Source file for the FluidEvent class.
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

#include "fluid_event.h"

namespace prime {
namespace ssfnet {

FluidEvent::FluidEvent() :
				SSFNetEvent(SSFNetEvent::SSFNET_FLUID_ARRIVAL_EVT),
				class_id(0, 0), peer_uid(0), fluid_value(0), fluid_step(0) {}
#ifdef SSF_FLUID_ROUNDTRIP
FluidEvent::FluidEvent(const FluidEvent& evt) :
				SSFNetEvent(evt),
				class_id(evt.class_id), peer_uid(evt.peer_uid),
				fluid_value(evt.fluid_value), fluid_step(evt.fluid_step), path_value(evt.path_value),
				forward_nics(evt.forward_nics), reverse_nics(evt.reverse_nics){}
#else
FluidEvent::FluidEvent(const FluidEvent& evt) :
				SSFNetEvent(evt),
				class_id(evt.class_id), peer_uid(evt.peer_uid),
				fluid_value(evt.fluid_value), fluid_step(evt.fluid_step),
				forward_nics(evt.forward_nics), reverse_nics(evt.reverse_nics){}
#endif

prime::ssf::ssf_compact* FluidEvent::pack()
{
	prime::ssf::ssf_compact* dp = SSFNetEvent::pack();
	dp->add_unsigned_long_long(class_id.first);
	dp->add_unsigned_long_long(class_id.second);
	dp->add_unsigned_long_long(peer_uid);
	dp->add_float(fluid_value);
	dp->add_long(fluid_step);

	UID_t t = forward_nics.size();
	dp->add_unsigned_long_long(t);
	for(FluidHop::PathMap::iterator it=forward_nics.begin();it!=forward_nics.end();it++) {
		dp->add_unsigned_long_long(it->first);
		dp->add_unsigned_long_long(it->second.first);
		dp->add_unsigned_long_long(it->second.second);
	}
    t = reverse_nics.size();
	dp->add_unsigned_long_long(t);
	for(FluidHop::PathMap::iterator it=reverse_nics.begin();it!=reverse_nics.end();it++) {
		dp->add_unsigned_long_long(it->first);
		dp->add_unsigned_long_long(it->second.first);
		dp->add_unsigned_long_long(it->second.second);
	}
	return dp;
}

void FluidEvent::unpack(prime::ssf::ssf_compact* dp)
{
	SSFNetEvent::unpack(dp);
	dp->get_unsigned_long_long(&class_id.first);
	dp->get_unsigned_long_long(&class_id.second);
	dp->get_unsigned_long_long(&peer_uid);
	dp->get_float(&fluid_value);
	dp->get_long(&fluid_step);
	UID_t s=0,w=0,v=0,t=0;
	dp->get_unsigned_long_long(&s);
	for(int i=0;i<(int)s;i++){
		dp->get_unsigned_long_long(&w);
		dp->get_unsigned_long_long(&v);
		dp->get_unsigned_long_long(&t);
		forward_nics.insert(SSFNET_MAKE_PAIR(w,SSFNET_MAKE_PAIR(v,t)));
	}
	s=0,w=0,v=0, t=0;
	dp->get_unsigned_long_long(&s);
	for(int i=0;i<(int)s;i++){
		dp->get_unsigned_long_long(&w);
		dp->get_unsigned_long_long(&v);
		dp->get_unsigned_long_long(&t);
		reverse_nics.insert(SSFNET_MAKE_PAIR(w,SSFNET_MAKE_PAIR(v,t)));
	}
}

prime::ssf::Event* FluidEvent::createFluidEvent(prime::ssf::ssf_compact* dp)
{
	FluidEvent* pe = new FluidEvent();
	pe->unpack(dp);
	return pe;
}

// used to register an event class.
SSF_REGISTER_EVENT(FluidEvent, FluidEvent::createFluidEvent);

}; // namespace ssfnet
}; // namespace prime
