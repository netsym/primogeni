/**
 * \file tcp_message.h
 * \brief Header file for the TCPMessage class.
 */

#ifndef __TCP_MESSAGE_H__
#define __TCP_MESSAGE_H__

#include <arpa/inet.h>
#include <netinet/in.h>
#include "os/ssfnet.h"
#include "os/protocol_message.h"
#include <stdio.h>
#include <string.h>

#define NSA 3

namespace prime {
namespace ssfnet {

#ifndef IPADDR
typedef prime::ssf::uint32 IPADDR;
#endif

class TCPSession;
class TCPMessage;

struct OptionsEnabled{
	int mss;
	int wsopt;
	bool sack_permitted;
	int sack_length; 	//total - 2 bytes (5-length)
	int* sack; 			//pointer to SACK #s sent
	int echo;
	int echo_reply;
	int ts_value; //TSval
	int ts_echo;  //TSecr
	//int partial_ord_conn_permitted;
	//int partial_ord_srv_profile;
	//int cc_new;
	//int cc_echo;
	//int alt_chksum_req;
	//int alt_chksum_data;
};
//OPTIONS
#define END_OPTIONS_LIST_OPT	0x00000001
#define NO_OPERATION_OPT		0x00000002
#define MSS_OPT					0x00000004
#define WSOPT_OPT				0x00000008
#define SACK_PERMITTED_OPT		0x00000010
#define SACK_OPT				0x00000020
#define ECHO_OPT				0x00000040
#define ECHO_REPLY_OPT			0x00000080
#define TSOPT_OPT				0x00000100
#define PART_ORD_CONN_PERM_OPT	0x00000200
#define PART_ORD_SERV_PROF_OPT	0X00000400
#define	CC_CONN_COUNT			0x00000800
#define CC_NEW					0x00001000
#define CC_ECHO					0x00002000
#define ALT_CHECKSUM_REQ_OPT	0x00004000
#define ALT_CHECKSUM_DATA_OPT	0x00008000

#define SSFNET_TCPHDR_LENGTH 20


struct tcp_pseudo_iphdr;
class RawTCPHeader;

class TCPMessage: public RawProtocolMessage {
 public:

	enum {
		TCP_FLAG_FIN = 0x01, // final segment from sender
		TCP_FLAG_SYN = 0x02, // start of a new connection
		TCP_FLAG_RST = 0x04, // connection to be reset
		TCP_FLAG_PSH = 0x08, // push operation invoked
		TCP_FLAG_ACK = 0x10, // acknowledgment field valid
		TCP_FLAG_URG = 0x20  // urgent pointer field valid
	};

	TCPMessage();

	TCPMessage(uint32 hdrsize);

	TCPMessage(uint16 src_port_, uint16 dst_port_,
			uint32 seqno_, uint32 ackno_, byte flags_, uint16 wsize_,
			uint16 length_ = SSFNET_TCPHDR_LENGTH, uint16_t option_len=0,
			byte* options_ = NULL);

	TCPMessage(TCPMessage const & icmpmsg);

	~TCPMessage();

	//virtual methods
	virtual ProtocolMessage* clone();
	virtual int size() const;
	virtual void fromRawBytes(Packet* p, byte*& b, int& n);
	virtual void toRawBytes(byte*& b, int& n);
	virtual int archetype() const { return SSFNET_PROTOCOL_TYPE_TCP; }

	virtual void serialize(byte* buf, int& offset);
	virtual void deserialize(byte* buf, int& offset);

	const uint16 getSrcPort() const;
	const uint16 getDstPort() const;
	const uint32 getSeqno() const;
	const uint32 getAckno() const;
	const uint16 getLength() const;
	const byte getFlags() const;
	const uint16 getWSize() const;
	const uint16 getChecksum() const;
	byte* getOptions();

	void setSrcPort(uint16 src_port_);
	void setDstPort(uint16 dst_port_);
	void setSeqno(uint32 seq_no_);
	void setAckno(uint32 ackno_);
	void setLength(uint16 length_);
	void setFlags(byte flags_);
	void setWSize(uint16 wsize_);
	void setOptions(byte* options_, int size_);
	void setChecksum(uint16 chksum);

	bool hasPayload() const;
	static uint16 fast_cksum(struct tcp_pseudo_iphdr* phdr, RawTCPHeader* hdr, int length);
};

class RawTCPHeader {
 private:
	// The source port number
	uint16 src_port;
	// The destination port number
	uint16 dst_port;
	// The sequence number
	uint32 seqno;
	// The acknowledgment sequence number
	uint32 ackno;
	// TCP header length in words
	byte   length;
	// Flags indicating packet type
	byte   flags;
	// Receive window size
	uint16 wsize;
	// Checksum
	uint16 checkSum;
	// Urgent Pointer
	uint16 urgentPointer;
	// TCP options, e.g., in SACK
	byte*  options;

 public:
	const uint16 getSrcPort() const { return myntohs(src_port); }
	const uint16 getDstPort() const { return myntohs(dst_port); }
	const uint32 getSeqno() const { return myntohl(seqno); }
	const uint32 getAckno() const { return myntohl(ackno); }
	const uint16 getLength() const { return ((uint8)length >> 2); } //in bytes!
	const byte getFlags() const { return flags; }
	const uint16 getWSize() const { return myntohs(wsize); }
	const uint16 getChecksum() const { return checkSum; }
	byte* getOptions() { return options; }

	void setSrcPort(uint16 src_port_) { src_port = src_port_; }
	void setDstPort(uint16 dst_port_) { dst_port = dst_port_; }
	void setSeqno(uint32 seqno_) { seqno = seqno_; }
	void setAckno(uint32 ackno_) { ackno = ackno_; }
	void setLength(uint16 length_) { length = length_<<2; } // in bytes!
	void setFlags(byte flags_) { flags = flags_; }
	void setWSize(uint16 wsize_) { wsize = wsize_; }
	void setOptions(byte* options_, int size_);
	void setChecksum(uint16 checkSum_) { checkSum = checkSum_; };
};

//#endif
}; // namespace ssfnet
}; // namespace prime

#endif /*__TCP_MESSAGE_H__*/
