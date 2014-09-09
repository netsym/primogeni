/**
 * \file ipv4_message.h
 * \brief Header file for the IPv4Message class.
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

#ifndef __IPV4_MESSAGE_H__
#define __IPV4_MESSAGE_H__

#include "os/ssfnet.h"
#include "os/protocol_message.h"

namespace prime {
namespace ssfnet {

#define DEFAULT_IPV4_HDRLEN 20 // sizeof(IPv4RawHeader)
#define DEFAULT_IPV4_TTL 64 // somehow this is the standard?

/** \brief Raw ip header format (stored in network order). */
class IPv4RawHeader {
 private:
  uint8 ip_vhl;  /* version and header length (0x4?) */
  uint8 ip_tos;  /* type of service */
  uint16 ip_len; /* total length of the datagram (including ip header) */
  uint16 ip_ident; /* identification */
  uint16 ip_off; /* flags and fragment offset field */
  uint8 ip_ttl;  /* time to live */
  uint8 ip_p;    /* protocol id */
  uint16 ip_cksum; /* header checksum */
  uint32 ip_src; /*  soruce ip address */
  uint32 ip_dst; /* destination ip address */

 public:
  uint8 getHdrLen() const { return (ip_vhl&0xf)*4; } // in bytes
  uint8 getTOS() const { return ip_tos; }
  uint16 getLen() const { return myntohs(ip_len); }
  uint16 getIdent() const { return myntohs(ip_ident); }
  uint16 getOffset() const { return myntohs(ip_off); }
  uint8 getTTL() const { return ip_ttl; }
  uint8 getProto() const { return ip_p; }
  uint32 getSrc() const { return myntohl(ip_src); }
  uint32 getDst() const { return myntohl(ip_dst); }
  uint16 getCheckSum() const { return ip_cksum; } // no endian conversion

  void setHdrLen(uint8 x) { ip_vhl = (0x40|((x>>2)&0xf)); } // in bytes
  void setTOS(uint8 x) { ip_tos = x; }
  void setLen(uint16 x) { ip_len = myhtons(x); }
  void setIdent(uint16 x) { ip_ident = myhtons(x); }
  void setOffset(uint16 x) { ip_off = myhtons(x); }
  void setTTL(uint8 x) { ip_ttl = x; }
  void setProto(uint8 x) { ip_p = x; }
  void setSrc(uint32 x) { ip_src = myhtonl(x); }
  void setDst(uint32 x) { ip_dst = myhtonl(x); }
  void setCheckSum(uint16 x) { ip_cksum = x; } // no endian conversion

  void decrementTTL(uint8 x) { ip_ttl -= x; }

  SSFNET_STRING getProtoString() const;

  friend PRIME_OSTREAM& operator<< (PRIME_OSTREAM& o, IPv4RawHeader const& hdr);
};

/** \brief IPv4 protocol message. */
class IPv4Message: public RawProtocolMessage {
 public:
  /** The default constructor (without any argument). */
  IPv4Message();

  /** The copy constructor. */
  IPv4Message(const IPv4Message& ipmsg);

  /** Clone the ip protocol message. */
  virtual ProtocolMessage* clone();

  /** The constructor with specific fields. */
  IPv4Message(IPAddress src, IPAddress dst, int proto, int hdrlen = DEFAULT_IPV4_HDRLEN,
	      int tos = 0, int ident=0, int offset=0, int ttl = DEFAULT_IPV4_TTL);

  /**
   * Return the number of bytes of this protocol message only
   * (excluding its payload) as in the raw packet format. Since we use
   * the real IP format, this method is the same as packingSize().
   */
  virtual int size() const { return (int)getHdrLen(); }

  /** Convert from real bytes. */
  virtual void fromRawBytes(Packet* rawpkt, byte*& rawbytes, int& nbytes);

  /** Convert to real bytes; we use this chance to compute datagram
      size and the checksum. */
  virtual void toRawBytes(byte*& rawbytes, int& nbytes);

  /** Return the ip protocol's archetype. */
  virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_IPV4; }

 public:
  uint8 getHdrLen() const; ///< Return the length of the ip header in bytes.
  uint8 getTOS() const; ///< Return the type of service (TOS) field.
  uint16 getLen() const; ///< Return the length of the entire ip packet in bytes (header plus payload).
  uint16 getIdent() const; ///< Return the identification field.
  uint16 getOffset() const; ///< Return the flags and fragment field.
  uint8 getTTL() const; ///< Return the time to live (TTL) field.
  uint8 getProto() const; ///< Return the protocol id of the payload.
  IPAddress getSrc() const; ///< Return the source ip address.
  IPAddress getDst() const; ///< Return the destination ip address.

  void setHdrLen(uint8 x); ///< Set the length of the ip header in bytes.
  void setTOS(uint8 x); ///< Set the type of service (TOS) field.
  void setLen(uint16 x); ///< Set the length of the entire ip packet in bytes (header plus payload).
  void setIdent(uint16 x); ///< Set the identification field.
  void setOffset(uint16 x); ///< Set the flags and fragment field.
  void setTTL(uint8 x); ///< Set the time to live (TTL) field.
  void setProto(uint8 x); ///< Set the protocol id of the payload.
  void setSrc(IPAddress x); ///< Set the source ip address.
  void setDst(IPAddress x); ///< Set the destination ip address.

  void decrementTTL(uint8 x=1); ///< decrement the ttl by x

};

} // namespace ssfnet
} // namespace prime

#endif /*__IPV4_MESSAGE_H__*/
