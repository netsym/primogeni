/**
 * \file fluid_hop.h
 * \brief Header file for the FluidHop class.
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

#ifndef __FLUID_HOP_H__
#define __FLUID_HOP_H__

#include "os/ssfnet_types.h"

#define SSFNET_FLUID_CACHING
//#define SSFNET_FLUID_DAMPENING
//#define SSFNET_FLUID_VARYING
//#define SSFNET_FLUID_DYNAMIC
//#define SSFNET_FLUID_ROUNDTRIP
//#define SSFNET_FLUID_ACK_PKTSIZ (40*8)
//#define SSFNET_FLUID_STOCHASTIC

//#define DEBUG

namespace prime {
namespace ssfnet {

class FluidQueue;
class TrafficManager;


/** A fluid class is identified by the source and destination uids of the flows. */
typedef SSFNET_PAIR(UID_t, UID_t) FLUID_CLASSID;

/**
 * \brief A series of fluid states indicating fluid flow rate changes.
 *
 * The fluid states are sorted in increasing order of time. If needed,
 * linear interpolation is applied to instances between the
 * states. Otherwise, it's treated as piece-wise constant.
 */
class FluidSeries {
 public:
  enum { PIECE_WISE_LINEAR, PIECE_WISE_CONSTANT };

  /** The constructor. */
  FluidSeries(int type = PIECE_WISE_LINEAR, float initvalue = 0, long initstep = 0);

  /** The destructor. */
  ~FluidSeries();

  /** Set the type of the time series: whether it's a piece-wise
      linear function or a piece-wise constant function. */
  void setType(int type);

  /** Insert a new fluid state at the given time (step). */
  void set(float value, long step);

  /** Get the value of a given time. Interpolation is needed if the
      time is between two states. */
  float get(long step);

 private:
  /**
   * \internal
   * \brief An internal representation of fluid states.
   */
  class FluidState /*: public ssf_quickobj*/ {
  public:
    FluidState(float v, long s) :
      step(s), value(v), prev(0), next(0) {}

    /** The time of the state indicated by the time step index. */
    long step;

    /** The value of the state. */
    float value;

    /** Point to the next state. */
    FluidState* prev;

    /** Point to the previous state. */
    FluidState* next;
  };

  /** Type of the time series, whether we interpolate the space
      between fluid states as piece-wise linear or piece-wise
      constant. */
  int type;

  /** Point to the earliest state of this series. */
  FluidState* earliest;

#ifdef SSFNET_FLUID_CACHING
  /** Point to the state last accessed; this is used for performance
      improvement. */
  FluidState* current;
#endif
};

/**
 * \brief Per-hop data of a tcp flow at a router.
 *
 * A fluid queue can maintain several fluid flows that traverse
 * through the router. State variables associated with a class of
 * fluid flows through a router is included in this FluidHop class.
 */
class FluidHop {
 public:
  /** The owner fluid queue in a router. If it's NULL, this hop node
      is a ghost node, except the last hop used to account for the
      reverse path which is not treated as a ghost node. */
  FluidQueue* fluid_queue;

  /** If the node is a ghost node, this is the uid of the node
      this ghost node mirrors. Otherwise, this is simply the uid
      of the network interface that contains the fluid
      queue. For the last hop, it's 0 (invalid uid). For the ghost node
      of the last hop, the source uid is used. */
  UID_t peer_uid;

  /** If the node is a ghost node, this is the uid of the community
      where the interface this ghost node mirrors is at. Otherwise, this is
      simply the uid of the community that contains the interface that
      contains the fluid queue. For the ghost node of the last hop, the uid of
      the src node's community is used (if not define ROUNDTRIP). */
  UID_t comm_uid;

  /** The id of the fluid class this fluid hop is belonging to. */
  FLUID_CLASSID class_id;

  /** Point to the traffic manager object that maintains all fluid
      classes in each timeline. */
  //FluidTraffic* fluid_traffic;
  TrafficManager* traffic_mgr;

  /** Point to the next router of this fluid flow. */
  FluidHop* next;

  /** True if next exists and is a ghost */
  bool next_is_ghost;

  /** Whether this hop is receiving fluid events. */
  bool is_receiving;

  /** The propagation connecting to the next hop. However, for the hop
      at the destination node, it's the end-to-end path delay. */
  float prop_delay;

  /** The fluid arrival rate at the router over time. */
  FluidSeries arrival;

  /** The fluid departure rate from the router over time. */
  FluidSeries departure;

  /** The accumulative delay experienced by the fluid up to this hop over time. */
  FluidSeries accu_delay;

  /** The accumulative loss rate experienced by the fluid up to this hop over time. */
  FluidSeries accu_loss;

#ifdef SSFNET_FLUID_DAMPENING
  float d_arrival;
  float d_departure;
  float d_accudelay;
  float d_acculoss;
#endif

 public:
  typedef SSFNET_PAIR(UID_t, UID_t) PathPair;
  typedef SSFNET_MAP(UID_t, PathPair) PathMap;
  /** The constructor for a local fluid hop. */
  FluidHop(FluidQueue* fq, FLUID_CLASSID class_id, TrafficManager* traffic_mgr);

  /** The constructor for a ghost or the last fluid hop. */
  FluidHop(UID_t id, UID_t comm_id, FLUID_CLASSID class_id, TrafficManager* traffic_mgr);

  /** The destructor. */
  ~FluidHop();

  /** The first hop behaves differently from others. In particular,
      the time series (for the arrival rate, the accumulative delay,
      and the accumulative loss) is a piece-wise constant function
      rather than a piece-wise linear function. */
  void setFirstHop();

  /** Return whether this is a ghost node. Note that the last node is
      not treated as a ghost node. */
  bool isGhost();

  /** Return the arrival rate at the given time step. */
  float getArrival(long step);

  /** Set the arrival rate at the given time step. */
  void setArrival(float my_arrival, long step, boolean init = false);

  /** Return the departure rate at the given time step. */
  float getDeparture(long step);

  /** Set the departure rate at the given time step. */
  void setDeparture(float my_departure, long step, boolean init = false);

  /** Return the accumulative delay at the given time step. */
  float getAccuDelay(long step);

  /** Set the accumulative delay at the given time step. */
  void setAccuDelay(float my_delay, long step, boolean init = false);

  /** Return the accumulative loss rate at the given time step. */
  float getAccuLoss(long step);

  /** Set the accumulative loss rate at the given time step. */
  void setAccuLoss(float my_loss, long step, boolean init = false);

  /** Only called for in the ghost node. A new fluid class
      registration message will be sent downstream for creating the
      subsequent fluid hops on remote processors in order to establish
      the entire fluid path. */
#ifdef SSFNET_FLUID_ROUNDTRIP
   void registerFluidClass(float accu_delay, long pktsiz, bool forward_path, PathMap f_nics, PathMap r_nics);
#endif
  void registerFluidClass(float accu_delay, long pktsiz, PathMap f_nics, PathMap r_nics);

  /** This method is called in the ghost node when the fluid class is
      to be reclaimed. A message will be sent to the downstream
      processor to tear down the fluid hops on the path. */
  void unregisterFluidClass();

  /** If this hop is the first in the community to receive fluid event
      (which means it has a ghost node), this function should be
      called to register this node with the TrafficManager class. */
  void prepareReceiving();

  void set_arrival(float my_arrival, long step);
  void set_departure(float my_departure, long step);
  void set_accudelay(float my_delay, long step);
  void set_acculoss(float my_loss, long step);
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__FLUID_HOP_H__*/
