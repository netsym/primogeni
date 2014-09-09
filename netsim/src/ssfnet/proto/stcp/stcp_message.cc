/*
 * \file stcp_message.cc
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
#include "stcp_message.h"
#include "os/logger.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {
//#define LOG_DEBUG(X) {std::cout<<"[stcp_message.cc:"<<__LINE__<<"]"<<X<<endl;}
//#define LOG_WARN(X) {std::cout<<"[stcp_message.cc:"<<__LINE__<<"]"<<X<<endl;}

LOGGING_COMPONENT(STCPMessage);

STCPMessage::STCPMessage(): RawProtocolMessage(){}


STCPMessage::STCPMessage(uint16 src_port_, uint16 dst_port_, uint32 length_,
		STCPMessage::Type type_, double send_time_, uint32 to_send_, int32 received_,
		STCPMessage::LossCalculationMap* m, float min_ts_, int seq_): RawProtocolMessage()
{
	LOG_DEBUG("creating a stcp message with srcport=" << src_port_ << " dstport=" << dst_port_ <<
			" length=" << length_ << "type=" << type_ << endl);
	LOG_DEBUG("msg->pkt is "<<this->pkt<<endl);

	//stcp length is the header length + the payload length
	assert(length_>=sizeof(RawSTCPHeader));
	rawptr=new byte[sizeof(RawSTCPHeader)];
	setSrcPort(src_port_);
	setDstPort(dst_port_);
	setLength(length_);
	setType(type_);
	setSendTime(send_time_);
	setBytesToSend(to_send_);
	setBytesReceivedPerIntv(received_);
	setLossCalculationMap(m);
	setMinTimestep(min_ts_);
	setSeq(seq_);
}

STCPMessage::STCPMessage(STCPMessage const & stcpmsg_): RawProtocolMessage(stcpmsg_) {}

ProtocolMessage* STCPMessage::clone()
{
	return new STCPMessage(*this);
}

int STCPMessage::size() const { return sizeof(RawSTCPHeader); }

int STCPMessage::packingSize() const { return sizeof(RawSTCPHeader); }

void STCPMessage::setSrcPort(uint16 src_port_){
	((RawSTCPHeader*)rawptr)->setSrcPort(src_port_);
}

void STCPMessage::setDstPort(uint16 dst_port_){
	((RawSTCPHeader*)rawptr)->setDstPort(dst_port_);
}

void STCPMessage::setLength(uint32 length_){
	((RawSTCPHeader*)rawptr)->setLength(length_);
}

void STCPMessage::setType(STCPMessage::Type type_){
	((RawSTCPHeader*)rawptr)->setType(type_);
}
void STCPMessage::setSendTime(double send_time_){
	((RawSTCPHeader*)rawptr)->setSendTime(send_time_);
}
void STCPMessage::setBytesToSend(uint32 to_send_){
	((RawSTCPHeader*)rawptr)->setBytesToSend(to_send_);
}
void STCPMessage::setBytesReceivedPerIntv(int32 received_){
	((RawSTCPHeader*)rawptr)->setBytesReceivedPerIntv(received_);
}

void STCPMessage::setLossCalculationMap(STCPMessage::LossCalculationMap* m){
	((RawSTCPHeader*)rawptr)->setLossCalculationMap(m);
}

void STCPMessage::insertLossCalculationMap(UID_t iface, int in, int out, int queue) {
	LOG_DEBUG("(RawSTCPHeader*)rawptr="<<(RawSTCPHeader*)rawptr<<endl)
	return ((RawSTCPHeader*)rawptr)->insertLossCalculationMap(iface,in, out, queue);
}
void STCPMessage::initLossCalculationMap() {
	return ((RawSTCPHeader*)rawptr)->initLossCalculationMap();
}
void STCPMessage::setMinTimestep(float min_ts_){
	((RawSTCPHeader*)rawptr)->setMinTimestep(min_ts_);
}
void STCPMessage::setSeq(int s){
	((RawSTCPHeader*)rawptr)->setSeq(s);
}

const uint16 STCPMessage::getSrcPort() const{
	return ((RawSTCPHeader*)rawptr)->getSrcPort();
}

const uint16 STCPMessage::getDstPort() const{
	return ((RawSTCPHeader*)rawptr)->getDstPort();
}

const uint32 STCPMessage::getLength() const{
	return ((RawSTCPHeader*)rawptr)->getLength();
}

const STCPMessage::Type STCPMessage::getType() const{
	return ((RawSTCPHeader*)rawptr)->getType();
}
const double STCPMessage::getSendTime() const{
	return ((RawSTCPHeader*)rawptr)->getSendTime();
}
const uint32 STCPMessage::getBytesToSend() const{
	return ((RawSTCPHeader*)rawptr)->getBytesToSend();
}
const int32 STCPMessage::getBytesReceivedPerIntv() const{
	return ((RawSTCPHeader*)rawptr)->getBytesReceivedPerIntv();
}

STCPMessage::LossCalculationMap* STCPMessage::getLossCalculationMap() {
	return ((RawSTCPHeader*)rawptr)->getLossCalculationMap();
}

const float STCPMessage::getMinTimestep() const {
	return ((RawSTCPHeader*)rawptr)->getMinTimestep();
}
const int STCPMessage::getSeq() const {
	return ((RawSTCPHeader*)rawptr)->getSeq();
}

void STCPMessage::serialize(byte* buf, int& offset) {
	LOG_ERROR("serialize() is not finished."<<endl);
}

void STCPMessage::deserialize(byte* buf, int& offset) {
	LOG_ERROR("deserialize() is not finished."<<endl);
}

void STCPMessage::toRawBytes(byte*& b, int& n){
	LOG_ERROR("toRawBytes() is not supported."<<endl);

}

void STCPMessage::fromRawBytes(Packet* p, byte*& b, int& n){
	LOG_ERROR("fromRawBytes() is not supported."<<endl);
}

SSFNET_REGISTER_MESSAGE(STCPMessage, SSFNET_PROTOCOL_TYPE_STCP);

} //namespace ssfnet
} //namespace prime
