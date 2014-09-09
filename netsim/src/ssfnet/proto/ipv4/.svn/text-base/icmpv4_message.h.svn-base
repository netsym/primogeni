/**
 * \file icmpv4_message.h
 * \brief Header file for the ICMPv4Message class.
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

#ifndef __ICMPV4_MESSAGE_H__
#define __ICMPV4_MESSAGE_H__

#include <string.h>
#include "os/ssfnet.h"
#include "os/protocol_message.h"

namespace prime {
namespace ssfnet {

/** \brief Raw ICMP header format; common portion. */
class RawICMPv4CommonHeader {
 private:
  uint8 icmp_type; /* message type */
  uint8 icmp_code; /* type sub-code */
  uint16 icmp_cksum; /* checksum of the entire icmp message */

 public:
  uint8 getType() const { return icmp_type; }
  uint8 getCode() const { return icmp_code; }
  uint16 getCheckSum() const { return icmp_cksum; } // no endian conversion

  void setType(uint8 x) { icmp_type = x; }
  void setCode(uint8 x) { icmp_code = x; }
  void setCheckSum(uint16 x) { icmp_cksum = x; } // no endian conversion
};

/**
 * \brief Raw ICMP header for Destination Unreachable (type=3),
 * Source Quench (type=4), Time Exceeded (type=11), Redirect
 * (type=5), Parameter Problem (type=12) messages; the data field
 * contains the full IP header and the first 8 bytes (64 bits) of the
 * payload of the datagram that prompted this error message to be
 * sent.
 */
class RawICMPv4IPDataHeader : public RawICMPv4CommonHeader {
 private:
  byte icmp_unused[4]; /* unused except when type=12 and code=0, 1st byte is the offset */
  byte icmp_data[1]; /* variable sized data (ip header and 8-byte payload) */

 public:
  byte* getData() const { return const_cast<byte*>(icmp_data); }
  void setData(byte* bf, int bfsz, int offset) { memcpy(&icmp_data[offset], bf, bfsz); }
};

/**
 * \brief Raw ICMP header for Echo Request (type=8) and Echo Reply
 * (type=0) messages; the data portion is optional (implementation
 * specific) and variable in size.
 */
class RawICMPv4EchoHeader : public RawICMPv4CommonHeader {
 private:
  uint16 icmp_id; /* identification (for matching request/reply) */
  uint16 icmp_seq; /* sequence number (for matching request/reply) */
  byte icmp_data[1]; /* variable sized data */

 public:
  uint16 getID() const { return myntohs(icmp_id); }
  uint16 getSeq() const { return myntohs(icmp_seq); }
  byte* getData() const { return const_cast<byte*>(icmp_data); }

  void setID(uint16 x) { icmp_id = myhtons(x); }
  void setSeq(uint16 x) { icmp_seq = myhtons(x); }
  void setData(byte* bf, int bfsz, int offset) { memcpy(&icmp_data[offset], bf, bfsz); }
};

/**
 * \brief Raw ICMP header for Timestamp Request (type=13) and
 * Timestamp Reply (type=14) messages.
 */
class RawICMPv4TimestampHeader : public RawICMPv4CommonHeader {
 private:
  uint16 icmp_id; /* identification (for matching request/reply) */
  uint16 icmp_seq; /* sequence number (for matching request/reply) */
  uint32 icmp_origtm; /* time when request is sent */
  uint32 icmp_recvtm; /* time when request is received */
  uint32 icmp_xmittm; /* time when reply is sent */

 public:
  uint16 getID() const { return myntohs(icmp_id); }
  uint16 getSeq() const { return myntohs(icmp_seq); }
  uint32 getOriginateTimestamp() const { return myntohl(icmp_origtm); }
  uint32 getReceiveTimestamp() const { return myntohl(icmp_recvtm); }
  uint32 getTransmitTimestamp() const { return myntohl(icmp_xmittm); }

  void setID(uint16 x) { icmp_id = myhtons(x); }
  void setSeq(uint16 x) { icmp_seq = myhtons(x); }
  void setOriginateTimestamp(uint32 x) { icmp_origtm = myhtonl(x); }
  void setReceiveTimestamp(uint32 x) { icmp_recvtm = myhtonl(x); }
  void setTransmitTimestamp(uint32 x) { icmp_xmittm = myhtonl(x); }
};

class IPv4Message;

/** \brief ICMP protocol message. */
class ICMPv4Message : public RawProtocolMessage {
 public:
  enum {
    // ICMP message types
    ICMP_TYPE_ECHO_REPLY	= 0,	/* echo reply */
    ICMP_TYPE_DEST_UNREACH	= 3,	/* destination unreachable */
    ICMP_TYPE_SOURCE_QUENCH	= 4,	/* source quench */
    ICMP_TYPE_REDIRECT		= 5,	/* redirect (change route) */
    ICMP_TYPE_ECHO		= 8,	/* echo request */
    ICMP_TYPE_RT_ADVERTISEMENT	= 9,	/* router advertisement */
    ICMP_TYPE_RT_SOLICITATION	= 10,	/* router solicitation */
    ICMP_TYPE_TIME_EXCEEDED	= 11,	/* time exceeded */
    ICMP_TYPE_PARAMETER_PROB	= 12,	/* parameter problem */
    ICMP_TYPE_TIMESTAMP		= 13,	/* timestamp request */
    ICMP_TYPE_TIMESTAMP_REPLY	= 14,	/* timestamp reply */
    ICMP_TYPE_INFO_REQUEST	= 15,	/* information request */
    ICMP_TYPE_INFO_REPLY	= 16,	/* information reply */
    ICMP_TYPE_ADDRESS		= 17,	/* address mask request */
    ICMP_TYPE_ADDRESS_REPLY	= 18,	/* address mask reply */
  };

  enum {
    // ICMP code (subtype) for destination unreachable message
    ICMP_CODE_NET_UNREACH	= 0,	/* network unreachable */
    ICMP_CODE_HOST_UNREACH	= 1,	/* host unreachable */
    ICMP_CODE_PROT_UNREACH	= 2,	/* protocol unreachable */
    ICMP_CODE_PORT_UNREACH	= 3,	/* port unreachable */
    ICMP_CODE_FRAG_NEEDED	= 4,	/* fragmentation needed and DF (don't fragment) set	*/
    ICMP_CODE_SR_FAILED		= 5,	/* source route failed */
    ICMP_CODE_NET_UNKNOWN	= 6,	/* destination network unknown; not used */
    ICMP_CODE_HOST_UNKNOWN	= 7,	/* destination host unknown	*/
    ICMP_CODE_HOST_ISOLATED	= 8,	/* source host isolated; obsolete, not used */
    ICMP_CODE_NET_ANO		= 9,	/* destination network is administratively prohibited */
    ICMP_CODE_HOST_ANO		= 10,	/* destination host is administratively prohibited */
    ICMP_CODE_NET_UNR_TOS	= 11,	/* destination network unreachable for type of service */
    ICMP_CODE_HOST_UNR_TOS	= 12,	/* destination host unreachable for type of service */
    ICMP_CODE_PKT_FILTERED	= 13,	/* packet filtered */
    ICMP_CODE_PREC_VIOLATION	= 14,	/* host precedence violation */
    ICMP_CODE_PREC_CUTOFF	= 15,	/* precedence cut off */

    // ICMP code (subtype) for time exceeded message
    ICMP_CODE_EXC_TTL		= 0,	/* TTL count exceeded */
    ICMP_CODE_EXC_FRAGTIME	= 1,	/* Fragment Reassembly time exceeded */

    // ICMP code (subtype) for redirect message
    ICMP_CODE_REDIR_NETWORK	= 0,	/* redirect for network error */
    ICMP_CODE_REDIR_HOST	= 1,	/* redirect for host error */
    ICMP_CODE_REDIR_TOSNET	= 2,	/* redirect for type of service and network error */
    ICMP_CODE_REDIR_TOSHOST	= 3,	/* redirect for type of service and host error */

    // ICMP code (subtype) for parameter problem message (bad ip header)
    ICMP_CODE_PARAMPROB_POINTER	= 0,	/* pointer indicates the error */
    ICMP_CODE_PARAMPROB_OPTION	= 1,	/* missing a required option */
    ICMP_CODE_PARAMPROB_LENGTH	= 2,	/* bad length */
  };

  /** The default constructor. */
  ICMPv4Message();

  /** The copy constructor. */
  ICMPv4Message(const ICMPv4Message& icmpmsg);

  /** Clone this protocol message. */
  virtual ProtocolMessage* clone();

  /**
   * The constructor of icmp echo request messages. The data portion
   * is optional (implementation specific) and variable in size.
   */
  ICMPv4Message(uint16 id, uint16 seq, int datalen, byte* data = 0);

  /**
   * The constructor of icmp messages of the following types:
   * destination unreachable (type=3), source quench (type=4), time
   * exceeded (type=11), redirect (type=5), parameter problem
   * (type=12) messages. These messages contain the data field with
   * the full IP header and the first 8 bytes (64 bits) of the payload
   * of the datagram that prompted this error message to be sent.
   */
  ICMPv4Message(uint8 type, uint8 code, IPv4Message* iphdr);

  /** The constructor of icmp timestamp request messages. */
  ICMPv4Message(uint16 id, uint16 seq);

  /**
   * Return the number of bytes of this protocol message as in the raw
   * header format (as in real network).
   */
  virtual int size() const { return msglen; }

  /**
   * Return the number of bytes needed to packet this protocol message
   * (excluding the payload). It includes the raw header plus one integer
   * we use to pack the size of the raw header.
   */
  virtual int packingSize() const { return msglen+sizeof(int32); }

  /** Serialize data into a byte array. */
  virtual void serialize(byte* buf, int& offset);
  virtual int pack(prime::ssf::ssf_compact* dp);

  /** Deserialize data from a byte array. */
  virtual void deserialize(byte* buf, int& offset);
  virtual int unpack(prime::ssf::ssf_compact* dp);

  /** Convert from real bytes. */
  virtual void fromRawBytes(Packet* rawpkt, byte*& rawbytes, int& nbytes);

  /** Convert to real bytes and compute the checksum. */
  virtual void toRawBytes(byte*& rawbytes, int& nbytes);

  /** Return the protocol number that the message is representing. */
  virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_ICMPV4; }

 public:
  uint8 getType() const;
  uint8 getCode() const;
  uint16 getCheckSum() const;
  byte* getDataMsgData() const;
  uint16 getEchoMsgID() const;
  uint16 getEchoMsgSeq() const;
  byte* getEchoMsgData() const;
  uint16 getTimeMsgID() const;
  uint16 getTimeMsgSeq() const;
  uint32 getTimeMsgOriginateTimestamp() const;
  uint32 getTimeMsgReceiveTimestamp() const;
  uint32 getTimeMsgTransmitTimestamp() const;

  void setType(uint8 x);
  void setCode(uint8 x);
  void setCheckSum(uint16 x);
  void setDataMsgData(byte* bf, int bfsz, int offset = 0);
  void setEchoMsgID(uint16 x);
  void setEchoMsgSeq(uint16 x);
  void setEchoMsgData(byte* bf, int bfsz, int offset = 0);
  void setTimeMsgID(uint16 x);
  void setTimeMsgSeq(uint16 x);
  void setTimeMsgOriginateTimestamp(uint32 x);
  void setTimeMsgReceiveTimestamp(uint32 x);
  void setTimeMsgTransmitTimestamp(uint32 x);

 public:
  /** Create an echo request message. */
  static ICMPv4Message* makeEchoRequestMsg(uint16 id, uint16 seq,
					   int datalen, byte* data);

  /** Create an echo reply message in place of a given echo
      request. Note that the changes are applied in place; the same
      ICMP message is returned. */
  static ICMPv4Message* makeEchoReplyMsg(ICMPv4Message* echo);

  /** Create a destination unreachable message. */
  static ICMPv4Message* makeDestUnreachMsg(uint8 code, IPv4Message* iphdr);

  /** Create a source quench message. */
  static ICMPv4Message* makeSourceQuenchMsg(IPv4Message* iphdr);

  /** Create a time-exceeded message. */
  static ICMPv4Message* makeTimeExceededMsg(uint8 code, IPv4Message* iphdr);

  /** Create a redirect message. */
  static ICMPv4Message* makeRedirectMsg(uint8 code, IPv4Message* iphdr);

  /** Create a parameter problem (bad ip header) message. */
  static ICMPv4Message* makeParamProbMsg(uint8 code, IPv4Message* iphdr);

  /** Create an timestamp request message. */
  static ICMPv4Message* makeTimeRequestMsg(uint16 id, uint16 seq);

  /** Create a timestamp reply message in place of a given timestamp
      request. Note that the changes are applied in place; the same
      ICMP message is returned. */
  static ICMPv4Message* makeTimeReplyMsg(ICMPv4Message* req);

 private:
  /** Caching the size of this icmp message. */
  int msglen;
};

} // namespace ssfnet
} // namespace prime

#endif /*__ICMPV4_MESSAGE_H__*/
