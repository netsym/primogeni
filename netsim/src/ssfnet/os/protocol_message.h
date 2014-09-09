/**
 * \file protocol_message.h
 * \brief Header file for the ProtocolMessage and RawProtocolMessage class.
 * \author Nathanael Van Vorst
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

#ifndef __PROTOCOL_MESSAGE_H__
#define __PROTOCOL_MESSAGE_H__

#include "os/ssfnet.h"

namespace prime {
namespace ssfnet {

class Packet;

/**
 * \brief Protocol message implemented as a list of protocol headers.
 *
 * ProtocolMessage is the base class representing a message header
 * specific to a protocol. Protocol messages are organized as a double
 * linked list. The payload of a protocol message is expected to be
 * the protocol message for the protocol above on the protocol
 * stack. The carrier of a protocol message is the protocol message of
 * the protocol below on the protocol stack.
 *
 * The ProtocolMessage class provides methods for serialization of the
 * protocol message to and from a byte array. Serialization is needed
 * when packets need to be delivered across memory boundaries (in a
 * distributed simulation environment). To facilitate that, each class
 * derived from the ProtocolMessage class must register itself using
 * the SSFNET_REGISTER_MESSAGE macro.
 *
 * The ProtocolMessage class also provides methods for converting from
 * a simulated protocol message into a real protocol header and vice
 * versa. These methods are invoked for real-time simulation where a
 * simulation packet is imported from or exported to emulated hosts.
 */
class ProtocolMessage : public ssf::ssf_quickobj {
  friend class Packet;

 public:
  //@name Serialization Services
  //@{

  /**
   * The default constructor; do nothing other than initiating the
   * basic fields. Each derived protocol message class must have such
   * a constructor without parameters.  This method is used only for
   * serialization when a packet needs to be delivered across memory
   * boundaries (in the form of a packet event).
   */
  ProtocolMessage();

  /**
   * Return the number of bytes needed to pack just this protocol
   * message (excluding the payload). This method is used for
   * serialization, which is needed when the simulator delivers
   * packets across memory boundaries in a distributed simulation
   * environment. The derived protocol message class needs to override
   * this method.
   */
  virtual int packingSize() const = 0;

  /**
   * Serialize this protocol message into the given buffer at the
   * given offset.  The buffer offset should be updated accordingly
   * after serialization. The derived protocol message class needs to
   * override this method.
   */
  virtual void serialize(byte* buf, int& offset) = 0;
  virtual int pack(prime::ssf::ssf_compact* dp) = 0;

  /**
   * Deserialize this protocol message from the given buffer at the
   * given offset.  The buffer offset should be updated accordingly
   * after deserialization. The derived protocol message class needs to
   * override this method.
   */
  virtual void deserialize(byte* buf, int& offset) = 0;
  virtual int unpack(prime::ssf::ssf_compact* dp) = 0;


  //@}

  //@name Clone Services
  //@{

  /**
   * The copy constructor. A derived protocol message class must
   * supply a copy constructor.  Note that, in C++, the copy
   * constructor at the derived class does not implicitly invoke the
   * copy constructor at the base class unless it is instructed to do
   * so explicitly. The user must explicitly enforce it. It is
   * expected that the copy constructor at the base class will make a
   * copy of not only the current protocol message itself, but also
   * its payload as well.
   */
  ProtocolMessage(const ProtocolMessage& msg);

  /**
   * Return a copy of the current protocol message and its payload.
   * This method is required for each derived protocol message class.
   * The clone method is used, for example, when the packet is sent to
   * multiple recipients across processor boundaries. This method
   * should be defined in the derived class as something like:
   <pre>
   ProtocolMessage* MyMessage::clone()
     { return new MyMessage(*this); }
   </pre>,
   * where MyMessage is derived from ProtocolMessage.
   */
  virtual ProtocolMessage* clone() = 0;

  /**
   * A protocol message (along with its carrier and payload) moves
   * vertically on the protocol stack and is supposed to be read-only
   * to a protocol session. The reuse method creates a mutable copy of
   * the same packet and return the corresponding protocol message. If
   * the protocol message has not been packetized, meaning there is no
   * packet associated with it, the method simply returns this
   * protocol message.  Otherwise, if the protocol message is
   * associated with a packet, and if the system determines that the
   * packet is not shared, this call simply leaves the packet
   * untouched and returns the same protocol message. However, if the
   * packet is shared by others (e.g., multiple recipients of a
   * broadcast or multicast packet), the original packet, including
   * all its protocol messages, needs to be cloned and the
   * corresponding new protocol message (corresponding to the newly
   * create packet) returned.
   */
  ProtocolMessage* reuse();

  /**
   * This method is expected to be called by users to reclaim the
   * protocol message and its payload. One must avoid deleting a
   * protocol message object directly (by using the delete operator);
   * that's why the destructor of this protocol message class should
   * be set as protected. This extra level of complexity is necessary
   * to support performance optimization schemes, such as using
   * reference counters. If the protocol message has been packetized
   * (that is, there is a packet associated with this protocol
   * message), the packet needs to be erased; otherwise, we just
   * reclaim this message and its payload.
   */
  void erase();

 protected:
  /**
   * The destructor will reclaim this packet header. This method is
   * intentionally set as protected so that users may not accidentally
   * reclaim the object using the delete operator. In its place, the
   * erase method should be used to reclaim the protocol message. We
   * make sure that only stand-alone messages get to call the
   * destructor to be reclaimed.
   */
  virtual ~ProtocolMessage();

  //@}

 public:
  //@name Emulation Services
  //@{

  /**
   * Return the number of bytes of this protocol message only
   * (excluding its payload). Note that the size might be different
   * from the packing size. In simulation, not all data fields are
   * needed to be accounted for; the packing size of the protocol
   * message can be reduced both for the sake of simplicity and for
   * efficiency of the model. The size here is the real size of the
   * protocol message in the target system.
   */
  virtual int size() const = 0;

  /**
   * This method is used for protocols to deal with real network
   * packets in real-time simulation/emulation scenarios.  The method
   * is provided with the raw content of the protocol message (as in
   * the real network), starting from the given offset pointed to by
   * 'rawbytes' and the size indicated by 'nbytes' (which also
   * includes the payload of this protocol message if exist).  If
   * 'rawpkt' is provided, the raw content of all protocol messages is
   * stored contiguously in a buffer maintained by the packet;
   * therefore, no memory copying is really needed here. Otherwise,
   * since this protocol message is not packetized, explicit copying
   * is necessary to create a separate store for the raw content.
   * Presumably this method will call the corresponding constructor of
   * the protocol message for its payload if exist. The default method
   * here does nothing; The derived class may want to override this
   * method to hook things up correctly.
   */
  virtual void fromRawBytes(Packet* rawpkt, byte*& rawbytes, int& nbytes) = 0;

  /**
   * This method is used for real-time simulation/emulation to convert
   * the simulated protocol message to a byte array containing the
   * protocol header in the real standard format (in network order).
   * If the protocol message is not stand-alone (see method below),
   * the raw content of all protocol messages is stored contiguously
   * in a buffer maintained by the packet; therefore, no memory
   * copying is really needed here. Otherwise, explicit copying is
   * necessary to create the raw packet.  Presumably this method will
   * call the corresponding constructor of the protocol message for
   * its payload if it exists. The default method here does nothing; The
   * derived class may want to override this method to hook things up
   * correctly.
   */
  virtual void toRawBytes(byte*& rawbytes, int& nbytes) = 0;

  /** Return the raw content of this protocol message. */
  byte* getRawData() { return rawptr; }

  /**
   * Return true if the protocol message has not been packetized or
   * the associated packet does not provide the buffer to contiguously
   * store the the raw content of the protocol messages. In other
   * words, the raw content of this protocol message, if exist, must
   * be stored in a stand-alone buffer.
   */
  bool isStandalone();

  //@}

  //@name Messaging Services
  //@{

  /**
   * Return the packet if the message has already been packetized
   * (i.e., not stand-alone); Otherwise, return NULL.
   */
  Packet* getPacket() { return pkt; }

  /**
   * Each protocol message (and the corresponding protocol) has two
   * types: an archetype and an implementation type. The archetype
   * identifies the protocol (such as MAC, IP, ICMP, TCP). The
   * implementation type identifies the specific implementation of the
   * protocol. The archetype is returned by this method. A derived
   * protocol message must override this method. Existing protocol
   * archetypes are defined in the header file "ssfnet.h".
   */
  virtual int archetype() const = 0;

  /**
   * The same protocol could have different implementations.
   * Accordingly, a protocol message could have different
   * representations.  The implementation type is used to uniquely
   * identify a protocol message (and the corresponding protocol
   * implementation). The implementation type is also used for
   * serialization of protocol messages. By default, the
   * implementation type is the same as the type, meaning that the
   * default acknowledges only one implementation of a protocol. In
   * cases where there exist multiple implementations, the derived
   * protocol message class need to override this method to return
   * different (and unique) value.
   */
  virtual int implementationType() const { return archetype(); }

  /**
   * Given a protocol message's archetype, this method traverses the
   * chain starting from this protocol message to find the protocol
   * message that has the same archetype.  If not found, the method
   * returns NULL.
   */
  ProtocolMessage* getMessageByArchetype(int atype);

  /**
   * Given a protocol message's implementation type, this method
   * traverses the chain starting from this protocol message to find
   * the protocol message that has the same implementation type.  If
   * not found, the method returns NULL.
   */
  ProtocolMessage* getMessageByImplementationType(int itype);

  /* Returns the previous protocol message. */
  ProtocolMessage* carrier() { return prev; }

  /** Return the next protocol message. */
  ProtocolMessage* payload()  { return next; }

  /**
   * Set the given protocol message as the payload of this protocol
   * message. The payload of a protocol message is expected to be the
   * protocol message for the protocol above on the protocol stack.
   * Protocol messages are organized as a double linked list inside a
   * packet.  This protocol message must be a stand-alone message and
   * the given payload message must have not been packetized.
   */
  void carryPayload(ProtocolMessage* payload);

  /**
   * Drop the payload of this protocol message. This method will sever
   * the link between this protocol message and the next protocol
   * message and returns the next protocol message. This protocol
   * message must be a stand-alone message. If the payload does not
   * exist, return NULL.
   */
  ProtocolMessage* dropPayload();

  //@}

 protected:
  /** Points to the protocol message before this node (which we call
      the carrier). */
  ProtocolMessage* prev;

  /** Points to the protocol message after this node (which we call
      the payload). */
  ProtocolMessage* next;

  /**
   * Points to the packet that this protocol message is part of, if
   * there is one. It's possible a packet has not been created, in
   * which case we call this protocol message as a stand-alone
   * protocol message.
   */
  Packet* pkt;

  /**
   * If this is not NULL, it points to the raw content of this
   * protocol message; if 'pkt' is not NULL, the raw content is stored
   * (contiguously) in the 'rawbuf' maintained by the packet; if this
   * protocol message is stand-alone, the raw content is stored in a
   * separate buffer.
   */
  byte* rawptr;

  /** Same as carryPayload(), but without the excessive error checking
      and the fixing up of the payload for correct packetizing. */
  void link_payload(ProtocolMessage* payload);

  /** Same as dropPayload(), but without the excessive error checking
      and the fixing up of the payload for correct packetizing. */
  ProtocolMessage* unlink_payload();

 public:
  //@name Registration Service
  //@{

  /**
   * A mapping between the protocol message's implementation type and
   * the protocol message's factory method. This mapping is used to to
   * deserialize packet when it moves across memory boundaries.
   */
  static SSFNET_INT2PTR_MAP* registered_messages;

  /**
   * This method is called by the SSFNET_REGISTER_MESSAGE macro to
   * register a protocol message class. The method should not be
   * publicly accessible. Yet, we make it public in order to make the
   * macro to work properly.
   */
  static int registerMessage(ProtocolMessage* (*fct)(), int itype);

  /**
   * Create a new instance of the protocol message identified by the
   * given implementation type. Return NULL if the implementation type
   * cannot be found (has not been registered earlier).
   */
  static ProtocolMessage* newMessage(int itype);

  //@}
};

/**
 * Each protocol message defined in the system must declare itself
 * using the macro SSFNET_REGISTER_MESSAGE. The macro has two
 * arguments: the name of the protocol message class, and the unique
 * implementation type of the protocol message.
 */
#define SSFNET_REGISTER_MESSAGE(name, itype) \
  static prime::ssfnet::ProtocolMessage* prime_ssfnet_new_message_##name() \
    { return new name; } \
  int prime_ssfnet_register_message_##name = \
    ProtocolMessage::registerMessage(prime_ssfnet_new_message_##name, itype)

/**
 * \brief XXX RawProtocolMessage
 *
 * XXX
 */
class RawProtocolMessage : public ProtocolMessage {
 public:
  /** Default constructor. */
  RawProtocolMessage(): ProtocolMessage() {}

  /**
   * Return the number of bytes needed to pack this protocol message
   * (excluding the payload).
   */
  virtual int packingSize() const { return size()+sizeof(int); }

  /** Serialize data into a byte array. */
  virtual void serialize(byte* buf, int& offset);
  virtual int pack(prime::ssf::ssf_compact* dp);

  /** Deserialize data from a byte array. */
  virtual void deserialize(byte* buf, int& offset);
  virtual int unpack(prime::ssf::ssf_compact* dp);

  /** Copy constructor. */
  RawProtocolMessage(const RawProtocolMessage& msg) : ProtocolMessage(msg) {}

  /** Convert from real bytes. */
  virtual void fromRawBytes(Packet* rawpkt, byte*& rawbytes, int& nbytes);

  /** Convert to real types. */
  virtual void toRawBytes(byte*& rawbytes, int& nbytes);
};

} // namespace ssfnet
} // namespace prime

#endif /*__PROTOCOL_MESSAGE_H__*/
