/**
 * \file packet.h
 * \brief Header file for the Packet class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
 * 
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

#ifndef __PACKET_H__
#define __PACKET_H__

#include "os/ssfnet.h"
#include "os/virtual_time.h"
#if TEST_ROUTING == 1
#include <time.h>
#include <sys/time.h>
#include <unistd.h>
#include <ctime>
#include <os/config_entity.h>
#endif

namespace prime {
namespace ssfnet {

class ProtocolMessage;
class PacketEvent;
class NixVector;
class RoutingSphere;

#if TEST_ROUTING == 1
class RouteHop {
public:
	typedef SSFNET_LIST(RouteHop) List;
	short send;
	UID_t host;
	VirtualTime vtime;
	uint64_t ts;
	RouteHop(short send_, UID_t host_, VirtualTime vtime_) {
		send=send_;
		host=host_;
		vtime=vtime_;
		ts=cur_timestamp();
	}
	RouteHop(prime::ssf::ssf_compact* dp) { unpack(dp); }
	RouteHop(const RouteHop& o): send(o.send), host(o.host), vtime(o.vtime), ts(o.ts) { }
	~RouteHop(){}
	void pack(prime::ssf::ssf_compact* dp) {
		dp->add_short(send);
		dp->add_unsigned_long_long(host);
		int64_t t=vtime;
		dp->add_long_long(t);
		dp->add_unsigned_long_long(ts);
	}
	void unpack(prime::ssf::ssf_compact* dp) {
		dp->get_short(&send);
		int64_t t;
		dp->get_unsigned_long_long(&ts);
		host=ts;
		dp->get_long_long(&t);
		dp->get_unsigned_long_long(&ts);
	}
	void save(ofstream& f) {
		f <<"["<<(send?"S":"R")<<","<<ts<<","<<vtime<<","<<host<<"]";
	}
	static int packingSize() { return 2*sizeof(uint64_t)+sizeof(int64_t)+sizeof(short);}
};
class RouteLookup {
public:
	enum CalcType {
		DST_FIND=1,
		DST_INSERT=2,
		DST_LOOKUP=3,
		NIX_INSERT=4,
		NIX_FIND=5,
		NIX_LOOKUP=6,
		TOTAL=7,
		ERROR=8
	};
	typedef SSFNET_LIST(RouteLookup) List;
	CalcType ct;
	UID_t net;
	uint64_t t;
	RouteLookup(RouteLookup::CalcType ct_, UID_t net_, uint64_t t_) {
		ct=ct_;
		net=net_;
		t=t_;
	}
	RouteLookup(prime::ssf::ssf_compact* dp) { unpack(dp); }
	RouteLookup(const RouteLookup& o): ct(o.ct), net(o.net), t(o.t) { }
	~RouteLookup() {}
	void pack(prime::ssf::ssf_compact* dp) {
		short s=ct;
		dp->add_short(s);
		dp->add_unsigned_long_long(net);
		dp->add_unsigned_long_long(t);
	}
	void unpack(prime::ssf::ssf_compact* dp) {
		short s=3;
		dp->get_short(&s);
		switch(s) {
		case 1:
			ct=DST_FIND;
			break;
		case 2:
			ct=DST_INSERT;
			break;
		case 3:
			ct=DST_LOOKUP;
			break;
		case 4:
			ct=NIX_INSERT;
			break;
		case 5:
			ct=NIX_FIND;
			break;
		case 6:
			ct=NIX_LOOKUP;
			break;
		case 7:
			ct=TOTAL;
			break;
		default:
			ct=ERROR;
			break;
		}
		dp->get_unsigned_long_long(&t);
		net=t;
		dp->get_unsigned_long_long(&t);
	}
	void save(ofstream& f) {
		switch(ct) {
		case DST_FIND:
			f <<"["<<"DST_FIND";
			break;
		case DST_INSERT:
			f <<"["<<"DST_INSERT";
			break;
		case DST_LOOKUP:
			f <<"["<<"DST_LOOKUP";
			break;
		case NIX_INSERT:
			f <<"["<<"NIX_INSERT";
			break;
		case NIX_FIND:
			f <<"["<<"NIX_FIND";
			break;
		case NIX_LOOKUP:
			f <<"["<<"NIX_LOOKUP";
			break;
		case TOTAL:
			f <<"["<<"TOTAL";
			break;
		default:
			f <<"["<<"ERROR";
			break;
		}

		f<<","<<t<<","<<net<<"]";
	}

	static int packingSize() 	{ return 2*sizeof(uint64_t)+sizeof(short);}
};
class RouteDebug {
public:
	RouteHop::List hops;
	RouteLookup::List lookups;
	RouteDebug(){ }
	RouteDebug(prime::ssf::ssf_compact* dp) { unpack(dp); }
	void pack(prime::ssf::ssf_compact* dp) {
		uint32_t l;
		l=hops.size();
		dp->add_unsigned_int(l);
		l=lookups.size();
		dp->add_unsigned_int(l);
		for(RouteHop::List::iterator it=hops.begin();it!=hops.end();it++) { (*it).pack(dp); }
		for(RouteLookup::List::iterator it=lookups.begin();it!=lookups.end();it++) { (*it).pack(dp); }
	}
	void unpack(prime::ssf::ssf_compact* dp) {
		uint32_t l1,l2;
		dp->get_unsigned_int(&l1);
		dp->get_unsigned_int(&l2);
		for(uint32_t i=0;i<l1;i++){ hops.push_back(RouteHop(dp));}
		for(uint32_t i=0;i<l2;i++){ lookups.push_back(RouteLookup(dp));}
	}
	int packingSize() const {return RouteHop::packingSize()*hops.size()+RouteLookup::packingSize()*lookups.size()+2*sizeof(uint32_t);}
	void save(UID_t dst, ofstream& lookup_file, ofstream& hop_file, void* droppedAt) {
		//nix_file << c_time <<" "<< ((uint64_t)(end-start))<<"\n";
		bool first=true;
		hop_file << dst<<", "<<hops.size()<<" {";
		for(RouteHop::List::iterator it=hops.begin();it!=hops.end();it++) {
			if(!first)hop_file<<",";
			(*it).save(hop_file);
			first=false;
		}
		if(droppedAt) {
			hop_file <<"[D,"<<((BaseEntity*)droppedAt)->getUName()<<","<<((BaseEntity*)droppedAt)->getUID()<<"]";
		}
		hop_file << "}\n";
		first=true;
		lookup_file <<dst<<lookups.size()<<" {";
		for(RouteLookup::List::iterator it=lookups.begin();it!=lookups.end();it++) {
			if(!first)lookup_file<<",";
			(*it).save(lookup_file);
			first=false;
		}
		lookup_file <<"}\n";
		hops.clear();
		lookups.clear();
	}
};
#endif

/**
 * \brief A packet is the unit of transmission through a link.
 *
 * The Packet class represents the data bytes sent as a unit in the
 * physical transmission medium. Structurally, a packet consists of a
 * sequence of protocol messages organized as a double-linked
 * list. The implementation of the Packet class uses reference
 * counters to facilitate fast processing as the packets are forwarded
 * in the network. In typical cases, the same packet can be reused as
 * it traverses through the network.
 *
 * The Packet class may contain the raw content of a packet in the
 * same format as what is in the real system (stored in a contiguous
 * buffer). This arrangement is made specifically for real-time
 * simulation and emulation. An emulation packet contains the raw
 * network packet received by the simulator as what has been captured
 * at the external emulated host. That is, all the content of the
 * protocol headers and payloads of the packet is stored in the same
 * continuous buffer (albeit at different offset). In this case, the
 * linked list of protocol messages in the Packet class is merely used
 * to describe the structure of the packet.
 */
class Packet : public ssf::ssf_quickobj {
  friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, Packet& pkt);
  friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, Packet* pkt);

 public:
  /** The default constructor; do nothing. */
  Packet();

  /**
   * Construct the packet from a list of protocol messages. In other
   * words, the protocol messages are to be packetized.
   */
  Packet(ProtocolMessage* msg,UID_t _dst_uid=0);

  /**
   * Construct the packet from a list of protocol messages. In other
   * words, the protocol messages are to be packetized.
   *
   * Also set the mac layer src/dst.
   */
  Packet(MACAddress src, MACAddress dst, ProtocolMessage* msg,UID_t _dst_uid=0);

  /**
   * Construct the packet from the real content of a packet. The
   * constructor will create the list of protocol messages as a
   * result. Note that the buffer will be taken as is; in particular,
   * no copying will be performed. The first 'reserved' bytes are
   * reserved by the emulation device and should not be touched by the
   * simulator, but rather just passed along as the packet is being
   * forwarded on the virtual network.
   */
  Packet(int ptype, byte* pktbuf, int pktsiz, int reserved = 0,UID_t _dst_uid=0);


  /**
   * Construct the packet from the real content of a packet. The
   * constructor will create the list of protocol messages as a
   * result. Note that the buffer will be taken as is; in particular,
   * no copying will be performed. The first 'reserved' bytes are
   * reserved by the emulation device and should not be touched by the
   * simulator, but rather just passed along as the packet is being
   * forwarded on the virtual network.
   *
   * Also set the mac layer src/dst.
   */
  Packet(MACAddress src, MACAddress dst, int ptype, byte* pktbuf, int pktsiz, int reserved = 0,UID_t _dst_uid=0);


  /** The copy constructor. */
  Packet(const Packet& pkt);

 protected:
  /**
   * The destructor is protected intentionally; the users should call
   * the erase method to reclaim the packet.
   */
  ~Packet();

 public:
  /**
   * This method conceptually creates a copy of the packet so that it
   * can be modified. It has the same semantic of destroying a packet
   * and then creating a new one with the same message headers. We
   * apply performance optimization by first inspecting whether the
   * packet is only used by one entity; if true, we reuse the current
   * instance and allow mutation; if not, we need to decrement the
   * reference counter, and then create and return a copy of the
   * current packet (including copying all protocol messages).
   */
  Packet* reuse();

  /**
   * Reclaim this packet. The user should call this method to reclaim
   * this packet rather than directly calling the destructor (using
   * the delete operator). This is because we use a reference counter
   * scheme to manage shared packets (e.g., broadcast or multicast
   * packets can be delivered to more than one network interfaces).
   * The erase method is made aware of reference counter scheme.
   */
  void erase();

  /** Return true if the packet is shared by multiple entities. */
  bool isShared() { return refcnt > 1; }

  /** Return a copy of the packet. */
  Packet* clone() { return new Packet(*this); }

  /**
   * Pack the packet in ssf compact format. This method is called by
   * the corresponding packet event for serialization before the
   * packet can be sent across memory boundaries.
   */
  void pack(prime::ssf::ssf_compact* dp);

  /**
   * Unpack the packet from ssf compact format. This method is called
   * by the corresponding packet event for deserialization after the
   * packet has been sent over memory boundaries.
   */
  void unpack(prime::ssf::ssf_compact* dp);

  /**
   * Return the packet length for packing, which is the the number of
   * bytes used by the simulation program to serialize the packet in
   * order to send the packet across memory boundaries in a distributed
   * simulation environment.
   */
  int packingSize() const;

  /**
   * Return the total number of bytes of this packet. Note that this
   * size here might be different from the packing size. In
   * simulation, not all data fields of protocol messages are needed
   * to be accounted for; the packing size of the protocol messages
   * can be reduced both for the sake of simplicity and efficiency of
   * the model. The size here is the real size of the packet (which is
   * the sum of the real size of all protocol messages) in the target
   * system.
   */
  int size() const;

  /** Return the address of the sending interface. */
  MACAddress getSrc() const { return src; }

  /** Set the address of the sending interface. */
  void setSrc(MACAddress s) { src = s; }

  /** Return the address of the receiving interface. */
  MACAddress getDst() const { return dst; }

  /** Set the address of the receiving interface. */
  void setDst(MACAddress d) { dst = d; }

  /** Return the type of the protocol message at the head of the chain. */
  int getMessageType() const { return prototype; }

  /** Set the type of the protocol message at the head of the chain. */
  void setMessageType(int t) { prototype = t; }

  /** Return the protocol message at the head of the chain. */
  ProtocolMessage* getMessage() const { return protomsg; }

  /**
   * Return the buffer storing the raw packet, along with the size of
   * the buffer and the size of the reserved bytes at the beginning of
   * the buffer.
   */
  inline byte* getRawBuffer(int* buffer_size, int* reserved_bytes) const {
    if(buffer_size) *buffer_size = rawbfsz;
    if(reserved_bytes) *reserved_bytes = rawrsv;
    return rawbuf;
  }

  /** get the RawBuffer **/
  inline byte* getRawBuffer() const { return rawbuf; }

  /** get the RawBuffer size **/
  inline int getBufferSize() const { return rawbfsz; }

  /** get the # bytes that are reserved in the buffer  **/
  inline int getReservedBytes() const { return rawrsv; }

  /**
   * Given a protocol message's archetype, this method traverses the
   * chain starting from the first protocol message to find the
   * protocol message that has the same archetype.  If not found, the
   * method returns NULL.
   */
  ProtocolMessage* getMessageByArchetype(int atype);

  /**
   * Given a protocol message's implementation type, this method
   * traverses the chain starting from the first protocol message to
   * find the protocol message that has the same implementation type.
   * If not found, the method returns NULL.
   */
  ProtocolMessage* getMessageByImplementationType(int itype);

  /**
   * Get the UID of the final destination of this packet
   */
  UID_t getFinalDestinationUID();

  /**
   * Get the  UID of the  final destination of this packet
   */
  void setFinalDestinationUID(UID_t new_dst_uid);

  /**
   * Get the UID of the previous routing sphere
   */
  UID_t getPrevRoutingSphereUID();

  /**
   * Get the UID of the previous routing sphere
   */
  void setPrevRoutingSphereUID(UID_t rs_uid);

  /**
   * Get the destination UID of this packet with the
   * sphere the pkt is currently traversing
   */
  UID_t getLocalDestinationUID();

  /**
   * Set the destination UID of this packet with the
   * sphere the pkt is currently traversing
   */
  void setLocalDestinationUID(UID_t new_dst_rank);

  /*
   * Get a pointer to the nix vector
   */
  NixVector* getNixVector();

  /**
   * Set the nix vector for this packet. It is an error to set a nix vector if the pkt already has one!
   */
  NixVector* setNixVector(NixVector* nix);

  /**
   * Remove the nixvector from this packet.
   */
  NixVector*  dropNixVector();


 private:

  /**
   * The reference counter that indicates the number of entities
   * sharing this packet; the packet cannot be reclaimed or modified
   * if this reference counter is larger than one. To modify the
   * packet, one needs to make an explicit call to the reuse method.
   */
  int refcnt;

  /** The address of the sending interface. */
  MACAddress src;

  /** The address of the receiving interface. */
  MACAddress dst;

  /** The archetype of the first protocol message. */
  int prototype;

  /**
   * The protocol messages are organized as a double linked list of
   * protocol messages starting from mac layer.
   */
  ProtocolMessage* protomsg;

  /**
   * The contiguous buffer storing the raw packet. This field is used
   * only if this is an emulation packet.
   */
  byte* rawbuf;

  /**
   * The size of the buffer containing the raw packet in standard
   * format. This field is used only if this packet is an emulation
   * packet. The size of the buffer can be larger than the packet size
   * (which is the return value of the size method); also, the size
   * includes the reserved bytes.
   */
  int rawbfsz;

  /**
   * The number of bytes at the beginning of the raw packet buffer
   * that have been reserved by the emulation device. These reserved
   * bytes are opaque to the simulator, which should not touch them
   * other than just pass them around (as part of the contiguous
   * raw buffer).
   */
  int rawrsv;

  /**
   * If static routing is being used, the path through a sphere is pre-computed and carried by a pkt
   * using a nix vector. This is a pointer to the nix vector (which may be null).
   */
  NixVector* nixvector;

  /*
   * the uid of the final destination of the packet. If this is unknown it set to 0.
   */
  UID_t dst_uid;

  /*
   * the uid of the routing sphere that created the nix vector previously
   */
  UID_t prev_rs_uid;

  /*
   * the uid of the destination within the sphere the pkt is currently traversing.
   * This will differ from the dst_uid in all sphere but the final sphere which
   * contains the final destination. The sphere which do not contain the final
   * destination, this is the uid of the edge_interface the packet targeted
   * at with this sphere
   */
  UID_t local_dst_uid;
#if TEST_ROUTING == 1
 public:
  RouteDebug* route_debug;
#endif
};

} // namespace ssfnet
} // namespace prime

#endif /*__PACKET_H__*/
