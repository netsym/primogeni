/**
 * \file tcp_message.cc
 * \brief Source file for the NewTCPMessage class.
 */

#include <string.h>
#include "tcp_message.h"
#include "os/logger.h"
#include "os/data_message.h"
#include "proto/ipv4/ipv4_message.h"
#include "proto/tcp/test/http_server.h"
namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(TCPMessage);



/** \brief Pseudo-IP header for computing TCP checksum. */
typedef struct tcp_pseudo_iphdr {
        u_int32_t src;
        u_int32_t dst;
        char pad;
        char protocol;
        u_int16_t len;
        tcp_pseudo_iphdr(IPv4RawHeader* iph, RawTCPHeader* tcph, uint16 l_):
		src(myhtonl(iph->getSrc())), dst(myhtonl(iph->getDst())), pad(0),
		protocol(SSFNET_PROTOCOL_TYPE_TCP), len(htons(l_)) {}
} tcp_pseudo_iphdr_t;


TCPMessage::TCPMessage()
: RawProtocolMessage() {
}

TCPMessage::TCPMessage(uint32 hdrsize)
: RawProtocolMessage() {
	rawptr = new byte[hdrsize];
	setLength(hdrsize); //Set length in  bytes!
}

TCPMessage::TCPMessage(uint16 src_port_, uint16 dst_port_,
		uint32 seqno_, uint32 ackno_, byte flags_, uint16 wsize_,
		uint16 length_, uint16_t option_len, byte* options_)
: RawProtocolMessage() {
	/*LOG_DEBUG("CONSTRUCTOR src_port_=" << src_port_ << " dst_port_=" << dst_port_ <<
				" seqno_=" << seqno_ << " ackno_=" << ackno_ << " flags_" << (int)flags_ <<
				" length_" << (int)(length_) << " lengthinchar=" << length_ <<
				" op1=" << (int)(*options_) << " op2=" << (int)(*options_+1) <<
				" op3=" << (int)(*options_+2) << " op4=" << (int)(*options_+3) <<
				"\n");*/
	if(length_< 20 && length_ > 60) {
		LOG_ERROR("Invalid tcp hdr length! length_"<<length_<<endl);
	}
	rawptr = new byte[length_];
	setSrcPort(myhtons(src_port_));
	setDstPort(myhtons(dst_port_));
	setSeqno(myhtonl(seqno_));
	setAckno(myhtonl(ackno_));
	setLength(length_); //Set length in  bytes!
	setFlags(flags_);
	setWSize(wsize_);
	//setWSize(30);

	setChecksum(0);

	assert(length_ - option_len == SSFNET_TCPHDR_LENGTH);

	if(option_len>0) {
		memcpy(rawptr+20,options_,option_len);
	}

	/*LOG_DEBUG("+++ CONSTRUCTOR TCP: " <<
				" seqno=" << (this->getSeqno()) <<
				" ackno=" << (this->getAckno()) <<
				" srcPort=" << (this->getSrcPort()) <<
				" dstPort=" << (this->getDstPort()) <<
				" length=" << (int)(this->getLength()) <<
				" flags=" << (int)(this->getFlags()) <<
				" opt1=" << (int)*(this->getOptions()) <<
				" opt2=" << (int)*(this->getOptions() + 1) <<
				" opt3=" << (int)*(this->getOptions() + 2) <<
				" opt4=" << (int)*(this->getOptions() + 3) <<
				"\n");*/
}

TCPMessage::~TCPMessage(){}

TCPMessage::TCPMessage(TCPMessage const & msg):
						RawProtocolMessage(msg) { }

ProtocolMessage* TCPMessage::clone() {
	return new TCPMessage(*this);
}


int TCPMessage::size() const { return getLength(); }

bool TCPMessage::hasPayload() const{ return const_cast<TCPMessage*>(this)->payload() != NULL; }

void TCPMessage::serialize(byte* buf, int& offset) {
	if(buf) {
		if(rawptr) memcpy(&buf[offset], rawptr, getLength());
		else LOG_ERROR("protocol message empty for serialization.");
		offset += getLength();
		LOG_DEBUG("serialize raw: size="<<packingSize()<<", first byte=0x"<<std::hex<<(uint32)*rawptr<<endl);
	} else LOG_ERROR("empty serialization buffer.");
}

void TCPMessage::deserialize(byte* buf, int& offset) {
	if(buf) {
		if(rawptr) LOG_ERROR("protocol message not empty for deserialization");
		rawptr = &buf[offset]; // temporary so that we can get the header size
		uint16_t hdrlen = getLength();
		rawptr = new byte[hdrlen];
		memcpy(rawptr, &buf[offset], hdrlen);
		offset += hdrlen;
		LOG_DEBUG("deserialize raw: size="<<packingSize()<<", first byte=0x"<<std::hex<<(uint32)*rawptr<<endl);
	} else LOG_ERROR("empty serialization buffer.");
}

void TCPMessage::fromRawBytes(Packet* p, byte*& b, int& n) {

	if(pkt) LOG_ERROR("protocol message already packetized.");
	if(rawptr) LOG_ERROR("protocol message not empty.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	RawTCPHeader* tcphdr = (RawTCPHeader*)b;

	/*LOG_DEBUG("initiating TCP: " << " seqno=" << (tcphdr->getSeqno()) <<
				" ackno=" << (tcphdr->getAckno()) <<
				" srcPort=" << (tcphdr->getSrcPort()) <<
				" dstPort=" << (tcphdr->getDstPort()) <<
				" length=" << (tcphdr->getLength()) <<
				" flags=" << (int)(tcphdr->getFlags()) <<
				"\n");*/
	uint32_t hdrlen = tcphdr->getLength();
	pkt = p;
	if(!isStandalone()) {
		//LOG_DEBUG(" TCP no copying!!!! hdrlen=" << hdrlen << "\n");
		rawptr = b; // no copying here!!!

		/*LOG_DEBUG("during assignment TCP: " <<
					" seqno=" << (this->getSeqno()) <<
					" ackno=" << (this->getAckno()) <<
					" srcPort=" << (this->getSrcPort()) <<
					" dstPort=" << (this->getDstPort()) <<
					" length=" << (this->getLength()) <<
					" flags=" << (int)(this->getFlags()) <<
					" opt1=" << (int)*(this->getOptions()) <<
					" opt2=" << (int)*(this->getOptions() + 1) <<
					" opt3=" << (int)*(this->getOptions() + 2) <<
					"\n");*/
		n -= hdrlen;
	} else {
		//LOG_DEBUG(" TCP copying hdrlen=" << hdrlen << "\n");
		rawptr = new byte[hdrlen];
		memcpy(rawptr, b, hdrlen);
		n -= hdrlen;
	}
	b += hdrlen;
	if(n > 0) {
		DataMessage* msg = new DataMessage();
		msg->fromRawBytes(pkt, b, n);
		link_payload(msg);
	}
	//LOG_DEBUG("nbytes="<< n<<endl);
}

void TCPMessage::toRawBytes(byte*& b, int& n) {
	//if(n != msglen) LOG_ERROR("ill-formed real packet content.");
	if(!pkt) LOG_ERROR("protocol message not packetized.");
	if(!rawptr) LOG_ERROR("protocol message empty.");
	if(!b) LOG_ERROR("empty raw content buffer.");

	//LOG_DEBUG("\t\t\t\t TCP: to rawbytes! n=" << n << "\n");
	/*LOG_DEBUG("TCP: " << " seqno=" << (this->getSeqno()) <<
				" ackno=" << (this->getAckno()) <<
				" srcPort=" << (this->getSrcPort()) <<
				" dstPort=" << (this->getDstPort()) <<
				" length_=" << (this->getLength()) <<
				" flags=" << (int)(this->getFlags()) <<
				" opt1=" << (int)*(this->getOptions()) <<
				" opt2=" << (int)*(this->getOptions() + 1) <<
				" opt3=" << (int)*(this->getOptions() + 2) <<
				"\n");*/
	/*RawTCPHeader* tcphdr = (RawTCPHeader*)rawptr;
		LOG_DEBUG("TCP 333 : " << " seqno=" << (tcphdr->getSeqno()) <<
				" ackno=" << (tcphdr->getAckno()) <<
				" srcPort=" << (tcphdr->getSrcPort()) <<
				" dstPort=" << (tcphdr->getDstPort()) <<
				" length=" << (tcphdr->getLength()) <<
				" flags=" << (int)(tcphdr->getFlags()) <<
				" opt1=" << int(*(b+20)) <<
				" opt2=" << int(*(b+21)) <<
				" opt3=" << int(*(b+22)) <<
				"\n");*/

	if(getChecksum()) {
		//we will assume that it was calculated correctly and does not need to be updated!
		RawProtocolMessage::toRawBytes(b, n); //will side effect b and n
	}
	else {
		//we need these below
		byte* bb=b;
		int nn=n;

		//will side effect b and n
		RawProtocolMessage::toRawBytes(b, n); //will side effect b and n

		//compute checksum
		IPv4RawHeader* iph = (IPv4RawHeader*)(bb-sizeof(IPv4RawHeader));
		RawTCPHeader* tcph = (RawTCPHeader*)bb;
		//LOG_DEBUG("checksum before "<<tcph->getChecksum()<<"\n");
		tcp_pseudo_iphdr_t ph(iph,tcph,(uint16)nn);
		tcph->setChecksum(0);
		//tcph->setChecksum(cksum((uint16*)bb, nn, cksum((uint16*)&ph, sizeof(tcp_pseudo_iphdr_t))));
		//std::cout<<"-------------\n";
		//std::cout<<"slow chksum:"<<cksum((uint16*)bb, nn, cksum((uint16*)&ph, sizeof(tcp_pseudo_iphdr_t)))<<"\n";
		//std::cout<<"fast chksum:"<<fast_cksum(&ph,tcph,nn)<<"\n";
		//HACK XXX
		tcph->setChecksum(fast_cksum(&ph,tcph,nn));
		//LOG_DEBUG("checksum after  "<<tcph->getChecksum()<<"\n");
	}
}

void RawTCPHeader::setOptions(byte* options_, int size_) {
	if(size_ && options_) {
		assert(size_ > 0);
		if(getLength()-SSFNET_TCPHDR_LENGTH >= size_) {
			memcpy(options_, getOptions(), size_);
		}
		else {
			LOG_ERROR("need to handle case when new options are larger than old options!"<<endl)
		}
	} else {
		//what to do? set options to be zero?
		memset(getOptions(),0,getLength()-SSFNET_TCPHDR_LENGTH);
	}
}

const uint16 TCPMessage::getSrcPort() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getSrcPort();
}

const uint16 TCPMessage::getDstPort() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getDstPort();
}

const uint32 TCPMessage::getSeqno() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getSeqno();
}

const uint32 TCPMessage::getAckno() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getAckno();
}

const uint16 TCPMessage::getLength() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getLength();
}

const byte TCPMessage::getFlags() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getFlags();
}

const uint16 TCPMessage::getWSize() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getWSize();
}

const uint16 TCPMessage::getChecksum() const {
	if(!rawptr) LOG_ERROR("empty protocol message");
	return ((RawTCPHeader*)rawptr)->getChecksum();
}


byte* TCPMessage::getOptions() {
	if(!rawptr) LOG_ERROR("empty protocol message");
	//return const_cast<byte*>(((RawTCPHeader*)rawptr)->getOptions(rawptr));
	return rawptr+20;
}

void TCPMessage::setSrcPort(uint16 src_port_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setSrcPort(src_port_);
}

void TCPMessage::setDstPort(uint16 dst_port_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setDstPort(dst_port_);
}

void TCPMessage::setSeqno(uint32 seq_no_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setSeqno(seq_no_);
}

void TCPMessage::setAckno(uint32 ackno_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setAckno(ackno_);
}

void TCPMessage::setLength(uint16 length_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	//LOG_DEBUG("set LENGTH to" << length_ << "-\n");
	((RawTCPHeader*)rawptr)->setLength(length_);
}

void TCPMessage::setFlags(byte flags_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setFlags(flags_);
}

void TCPMessage::setWSize(uint16 wsize_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setWSize(wsize_);
}

void TCPMessage::setOptions(byte* options_, int size_) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setOptions(options_, size_);
}

void TCPMessage::setChecksum(uint16 chksum) {
	if(!rawptr) LOG_ERROR("empty protocol message");
	((RawTCPHeader*)rawptr)->setChecksum(chksum);
}

uint16 TCPMessage::fast_cksum(struct tcp_pseudo_iphdr* phdr, RawTCPHeader* hdr, int length) {
	uint16 pcksum = cksum((uint16*)phdr, sizeof(tcp_pseudo_iphdr_t));
	if(hdr->getLength() % 2 == 0 && length>= 800) {
		//lets assume it is a http packet for now
		register int sum = 0;
		uint16 answer = 0;
		register uint16 *w = (uint16 *)hdr;
		register int nleft = hdr->getLength();
		int plength = length-nleft;
		/*std::cout<<" length="<<length<<", hdrlength="<<nleft<<", plength="<<plength<<"\n";
		for(int i=0;i<64;i++) {
			printf("[%c:%c],",((char*)hdr)[nleft+i],HTTPServer::global_buf[i]);
		}
		printf("\n");*/

		while (nleft > 1) {
			sum += *w++;
			nleft -= 2;
		}

		sum+=HTTPServer::precalc_cksums[plength];

		/* add back carry outs from top 16 bits to low 16 bits */
		sum = (sum >> 16) + (sum & 0xffff); /* add hi 16 to low 16 */
		sum += (~pcksum) & 0xffff; /* add accumulative result */
		sum += (sum >> 16); /* add carry */
		answer = ~sum; /* truncate to 16 bits */
		return answer;
	}
	return cksum((uint16*)hdr, length, pcksum);
}

SSFNET_REGISTER_MESSAGE(TCPMessage, SSFNET_PROTOCOL_TYPE_TCP);
}; // namespace ssfnet
}; // namespace prime
