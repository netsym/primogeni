/**
 * \file packet_event.h
 * \brief Header file for the PacketEvent class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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

#ifndef __PACKET_EVENT_H__
#define __PACKET_EVENT_H__

#include "ssf.h"
#include "os/packet.h"

namespace prime {
namespace ssfnet {

/**
 * \brief A packet event is the ssf event shipped across timelines.
 *
 * The PacketEvent class is derived from the Event class in SSF
 * representing network packets passing between SSF entities.  Each
 * community in ssfnet is implemented as an SSF entity with its own
 * timeline. The alignment is predetermined by the model. The
 * PacketEvent class is solely used for this purpose; for
 * communications between simulated network hosts in the same
 * community, we simply use timers to reduce the serialization
 * overhead.
 */
class PacketEvent: public SSFNetEvent {
 public:
  /** The default constructor. */
  PacketEvent();

  /** The constructor. */
  PacketEvent(Packet* packet, int dest_align_id, UID_t tuid);

  /** The copy constructor. */
  PacketEvent(const PacketEvent& pktevt);

  /** The destructor. */
  virtual ~PacketEvent();

  /**
   * This method is required by SSF for any simulation event to clone
   * itself, which is used when the simulation kernel delivers the
   * event across processor boundaries.
   */
  virtual Event* clone() { return new PacketEvent(*this); }

  /**
   * Retrieve the packet contained by this packet event. Since the
   * simulator uses point-to-point connections to facilitate
   * communications among the communities (i.e., SSF
   * entities/timelines), that is, one output channel is mapped to
   * only one input channel, we do not need to worry too much about
   * the special SSF event memory referencing rules (in this case,
   * events are not shared among multiple entities). We can make
   * modifications to the events as we want; no sharing happens.
   */
  Packet* dropPacket();
  
  /*
   * Get the uid of the iface this event should be delivered to
   */
  UID_t target() { return tuid; }

 public:
  /**
   * This method is required by SSF to pack the event object before
   * the event is about to be shipped to a remote machine across the
   * memory boundaries. It is actually used to serialize the event
   * object into a byte stream represented as an ssf_compact object.
   */
  virtual prime::ssf::ssf_compact* pack();

  /** Unpack the packet event, reverse process of the pack method. */
  virtual void unpack(prime::ssf::ssf_compact* dp);

  /**
   * This method is the factory method for deserializing the event
   * object. A new packet event object is created.
   */
  static prime::ssf::Event* createPacketEvent
    (prime::ssf::ssf_compact* dp);

 private:
  /** 
   * A packet event wraps around a packet (or an emulation
   * packet). SSF would make sure that packets going across timelines
   * are copied explicitly (via serialization).
   */
  Packet* packet;

  /*
   * the uid of the iface this event should be delivered to
   */
  UID_t tuid;

  /* This macros is used by SSF to declare an event class. */
  SSF_DECLARE_EVENT(PacketEvent);
};

} // namespace ssfnet
} // namespace prime

#endif /*__PACKET_EVENT_H__*/
