/*
 * \file stcp_session.cc
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

#include "stcp_session.h"
#include "os/logger.h"
#include "net/host.h"
#include <math.h>
#include <algorithm>

#define MAX_SEND_INTV 0.001
#define UPDATE_PROBE_INTV 2 //in terms of send data interval
#define BYTES_PER_SEGMENT 1400
#define MAXIMUM_WINDOW 65535 //bytes
//#define MAXIMUM_WINDOW 1000000000 //bytes
#define B 2
#define TIMEOUT 0.2
#define SYN_TIMEOUT 3

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(STCPSession);

class STCPSessionSendDataTimer : public Timer {
 public:

	STCPSessionSendDataTimer(STCPSession* stcp_sess_, float intv_, uint32 length_, byte* msg_=0, int offset_=0) :
			Timer(stcp_sess_->getMaster()), stcp_sess(stcp_sess_), send_intv(intv_),
			bytes_to_send(length_), msg(0), offset(0), count(0){
	}

	inline void force_cancel() { cancel();  }
	void setBytesToSend(uint32 len) { bytes_to_send=len;}
	void setSendIntv(float intv_) {send_intv=intv_;}

 protected:
	virtual void callback() {
		//send data
		LOG_DEBUG("IN TIMER, CALL SEND DATA"<<endl)
		stcp_sess->sendVirtualData(bytes_to_send);
		stcp_sess->addTotalBytesSent(bytes_to_send);
		LOG_DEBUG("send_intv="<<send_intv<<endl)
		this->set(VirtualTime(send_intv, VirtualTime::SECOND));
		//count++;
		//if(count%UPDATE_PROBE_INTV==0){
			//LOG_DEBUG("Send probe at time "<<stcp_sess->getMaster()->inHost()->getNow().second()<<endl)
			//stcp_sess->sendProbe(STCPMessage::UPDATE,0,-1);
			//stcp_sess->setLastProbeSent(stcp_sess->getMaster()->inHost()->getNow());
		//}
		//XXX if now()-last_probe_sent > some value, reduce the send rate...
	}
 private:
	STCPSession* stcp_sess;
	float send_intv;
	uint32 bytes_to_send;
	byte* msg;
	int offset;
	int count;
};

STCPSession::STCPSession(STCPMaster* master_, SimpleSocket* sock_, STCPConnection* conn_) :
		stcp_master(master_), socket(sock_), conn(conn_), first_packet(false),
		bytes_to_send(0), bytes_to_send_per_update(0), bytes_received(0), bytes_received_per_update(0),
		state(START), timer(0), is_slow_start(true), is_end(false), last_probe_sent(0) {}

STCPSession::~STCPSession() {
	if(socket)
		delete socket;
	if(conn)
		delete conn;
	if(timer){
		timer->cancel();
		delete timer;
		timer=0;
	}
}

void STCPSession::send(uint32 length)
{
	LOG_DEBUG("STCPSession::send virtual data "<< length << " down the protocol stack, getMaxDatagramSize()=" <<
			stcp_master->getMaxDatagramSize() << endl);
	//This is called by socket to send virtual data
	if(state==START){
		sendProbe(STCPMessage::START, length, -1); //start probe does not carry payload
		setLastProbeSent(stcp_master->inHost()->getNow());
	}else if(state==UPDATE){
		sendProbe(STCPMessage::UPDATE, length, -1);
		setLastProbeSent(stcp_master->inHost()->getNow());
	}else{
		LOG_ERROR("Should not happen!"<<endl);
	}
}

void STCPSession::sendMsg(uint32 length, byte* msg, bool send_data_and_disconnect)
{
	//XXX
	LOG_ERROR("STCP does not support sendMsg() yet."<<endl)
}

void STCPSession::sendVirtualData(uint32 length){
	//send data using a timer with the current send rate.
	LOG_DEBUG("Send data, length="<<length<<endl)
	uint32 have_sent = 0;
	LOG_DEBUG("have_sent="<<have_sent<<", length="<<(int)length<<endl)
	while(have_sent < length) { // if there's anything left to be sent
		//LOG_DEBUG("in loop"<<endl)
		int to_send = (int) mymin(stcp_master->getMaxDatagramSize(), (int)length-have_sent);
		have_sent += to_send;
		to_send += sizeof(RawSTCPHeader); //stcp length includes header
		//LOG_DEBUG("TIME="<<index<<endl)
		//index++;

		STCPMessage* stcp_msg = new STCPMessage(conn->src_port, conn->dst_port, to_send, STCPMessage::UPDATE,
				stcp_master->inHost()->getNow().second(), 0, -1);
		stcp_msg->initLossCalculationMap();
		stcp_msg->carryPayload(new DataMessage(to_send));

		//LOG_DEBUG("STCPSession::send simu to_send:" << to_send <<
				//" length=" << length << " have_sent=" << have_sent << " stcp_msgsize=" <<
				//stcp_msg->getLength() << " payload=" << stcp_msg->getLength() << endl);

		IPPushOption ops(conn->src_ip, conn->dst_ip, SSFNET_PROTOCOL_TYPE_STCP, DEFAULT_IP_TIMETOLIVE);
		stcp_master->push(stcp_msg, 0, (void*)&ops, sizeof(IPPushOption));
	}

}

void STCPSession::sendProbe(STCPMessage::Type type, uint32 bytes_to_send, int32 bytes_received_per_intv){

	STCPMessage* stcp_prob = new STCPMessage(conn->src_port, conn->dst_port,
			sizeof(RawSTCPHeader), type,
			stcp_master->inHost()->getNow().second(),  //send time
			bytes_to_send, //bytes_to_send
			bytes_received_per_intv); //bytes_received_per_update)
	stcp_prob->initLossCalculationMap();
	// No payload to carry here

	LOG_DEBUG("host "<<getMaster()->inHost()->getUID()<<" sends a probe message, type="<<type<<", bytes to send = " << stcp_prob->getBytesToSend() <<
			", message type is "<< type << ", src ip="<<conn->src_ip<<", dst ip="<<conn->dst_ip<<endl);

	// Set TTL to be 0
	IPPushOption ops(conn->src_ip, conn->dst_ip, SSFNET_PROTOCOL_TYPE_STCP, 0);
	stcp_master->push(stcp_prob, 0, (void*)&ops, sizeof(IPPushOption));
}

void STCPSession::receive(STCPMessage* stcpmsg, void* extinfo)
{
	LOG_DEBUG("at time="<<getMaster()->inHost()->getNow().second()<<", host="<<getMaster()->inHost()->getUID()<<", receives a pkt! with size=" << stcpmsg->getLength() << endl);
	uint32 socket_status_mask_ = 0;

	socket_status_mask_ |= SimpleSocket::SOCKET_PSH_FLAG;
	socket_status_mask_ |= SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED;

	//if(stcpmsg->getLength()==sizeof(RawSTCPHeader)){
	//this is a probe
	LOG_DEBUG("bytes received per intv "<<stcpmsg->getBytesReceivedPerIntv()<<endl);
	if(stcpmsg->getBytesReceivedPerIntv()==-1){
		//this is a receiver
		LOG_DEBUG("This is a receiver, host = "<<getMaster()->inHost()->getUName()<<endl);
		if(first_packet == false){
			first_packet = true;
			socket->socketBusy();
		}
		//send back the probe
		STCPMessage* new_msg=NULL;
		LOG_DEBUG("This is a receiver, stcp msg type is "<<stcpmsg->getType()<<endl);
		switch (stcpmsg->getType()){
			case STCPMessage::START:
			{
				//No data carried in start probe
				LOG_DEBUG("receive a start probe "<<endl);
				//set bytes_received to be 0
				stcpmsg->setBytesReceivedPerIntv(0);
				//get bytes_to_send
				bytes_to_send += stcpmsg->getBytesToSend();
				LOG_DEBUG("Set bytes to send to be "<<bytes_to_send<<" for host "<<getMaster()->inHost()->getUID()<<endl)

				new_msg=new STCPMessage(conn->src_port, conn->dst_port,
						stcpmsg->getLength(), STCPMessage::START, stcpmsg->getSendTime(),
						stcpmsg->getBytesToSend(), 0, stcpmsg->getLossCalculationMap(), stcpmsg->getMinTimestep());
				STCPMessage::LossCalculationMap* m=new_msg->getLossCalculationMap();
				for(STCPMessage::LossCalculationMap::iterator it=m->begin(); it!=m->end(); it++){
					LOG_DEBUG("start, in loss map, uid="<<it->first<<", first="<<(it->second)[1]<<", second="<<(it->second)[2]<<endl)
				}
				break;
			}
			case STCPMessage::UPDATE:
			{
				//update received bytes in this interval
				stcpmsg->setBytesReceivedPerIntv(bytes_received_per_update);
				bytes_received_per_update = 0;
				//if there are more bytes to send, update bytes_to_send
				if(stcpmsg->getBytesToSend()>0){
					bytes_to_send += stcpmsg->getBytesToSend();
					LOG_DEBUG("Updates bytes to send to be "<<bytes_to_send<<" for host "<<getMaster()->inHost()->getUID()<<endl)
				}
				new_msg=new STCPMessage(conn->src_port, conn->dst_port,
					stcpmsg->getLength(), STCPMessage::UPDATE, stcpmsg->getSendTime(),
					bytes_to_send, stcpmsg->getBytesReceivedPerIntv(), stcpmsg->getLossCalculationMap(), stcpmsg->getMinTimestep());
				STCPMessage::LossCalculationMap* m=new_msg->getLossCalculationMap();
				for(STCPMessage::LossCalculationMap::iterator it=m->begin(); it!=m->end(); it++){
						LOG_DEBUG("update, in loss map, uid="<<it->first<<", in="<<(it->second)[0]
						                                                <<", out="<<(it->second)[1]
						                                                <<", queue="<<(it->second)[2]<<endl)
				}

				//pop the data up to the application
				DataMessage* dm = (DataMessage*)stcpmsg->payload();
				if(!dm) {
					LOG_WARN("The update probe does not carry data."<<endl);
				}else{
					LOG_DEBUG("host "<<getMaster()->inHost()->getUID()<<" receives data, bytes to send="<<bytes_to_send<<", is_end="<<is_end<<endl);
					if(!is_end){
						LOG_DEBUG("SOCKET receive size = "<<dm->size()<<endl);
						socket->recv(dm->size(), 0, socket_status_mask_);
						//update received bytes, if it is equal to bytes_to_send, send end probe
						bytes_received += dm->size();
						bytes_received_per_update += dm->size();
						std::cout<<"bytes to send="<<bytes_to_send<<", bytes received="<<bytes_received<<endl;
						if(bytes_received>=(int32)bytes_to_send){
							sendProbe(STCPMessage::END, 0, 0);
							std::cout<<"host "<<getMaster()->inHost()->getUID()
								<<" sends END probe at time "<<getMaster()->inHost()->getNow().second()<<endl;
							is_end=true;
							LOG_DEBUG("host "<<getMaster()->inHost()->getUID()<<" has received all data, is end is true"<<endl)
						}
					}
				}
				break;
			}
			default:
				LOG_ERROR("Should not happen."<<endl);
				break;
		}
		//send back the probe
		IPPushOption ops(conn->src_ip, conn->dst_ip, SSFNET_PROTOCOL_TYPE_STCP, 0);
		stcp_master->push(new_msg, 0, (void*)&ops, sizeof(IPPushOption));
	}else{
		//this is a sender
		LOG_DEBUG("This is a sender, host = "<<getMaster()->inHost()->getUName()<<endl);
		double rtt = 0;
		float forward_loss = 1;
		float ack_loss=1;
		double handshake_latency=0;
		int num_of_segments_to_send = 0;
		uint32_t bytes_sent_slow_start = 0;
		uint32_t send_window = 0;
		double duration_slow_start = 0;
		double send_rate = 0;
		uint32_t bytes_per_send_intv = 0;

		switch (stcpmsg->getType()){
			case STCPMessage::START:{
				LOG_DEBUG("Sender received START probe, start timer to send three handshake packets."<<endl)
				if(state==START){
					//This is the first start probe, send the send start to get the initial loss probability
					sendProbe(STCPMessage::START, 0, -1); //start probe does not carry payload
					setLastProbeSent(stcp_master->inHost()->getNow());
					// get the the loss_cal map for calculating the loss probability
					loss_cal = stcpmsg->getLossCalculationMap();
					for(STCPMessage::LossCalculationMap::iterator it=loss_cal->begin(); it!=loss_cal->end(); it++){
						LOG_DEBUG("sender update loss_cal map, uid="<<it->first<<", in="<<(it->second)[0]
											                            <<", out="<<(it->second)[1]
											                            <<", queue="<<(it->second)[2]<<endl)
					}
					state=UPDATE;
				}else if (state==UPDATE){
					//get RTT, set a timer, send data, send update probe
					rtt = stcp_master->inHost()->getNow().second()-stcpmsg->getSendTime();
					STCPMessage::LossCalculationMap* cur_map = stcpmsg->getLossCalculationMap();
					for(STCPMessage::LossCalculationMap::iterator it=cur_map ->begin(); it!=cur_map ->end(); it++){
						LOG_DEBUG("sender get cur loss map, uid="<<it->first<<", in="<<(it->second)[0]
											                            <<", out="<<(it->second)[1]
											                            <<", queue="<<(it->second)[2]<<endl)
					}
					for(STCPMessage::LossCalculationMap::iterator it=loss_cal->begin(); it!=loss_cal->end(); it++){
						LOG_DEBUG("sender prev loss_cal map, uid="<<it->first<<", in="<<(it->second)[0]
																               <<", out="<<(it->second)[1]
																               <<", queue="<<(it->second)[2]<<endl)
					}
					for(STCPMessage::LossCalculationMap::iterator cur=cur_map->begin(); cur!=cur_map->end(); cur++){
						UID_t iface=cur->first;
						STCPMessage::LossCalculationMap::iterator prev=loss_cal->find(iface);
						int bytes_in = (cur->second)[0]-(prev->second)[0];
						int bytes_out = (cur->second)[1]-(prev->second)[1];
						int queue = (cur->second)[2]-(prev->second)[2];
						if(queue<0) queue=0;
						float drop_rate = (bytes_in-bytes_out-queue)/bytes_in;
						LOG_DEBUG("******bytes_in="<<bytes_in<<", bytes_out"<<bytes_out<<", queue="<<queue<<endl)
						forward_loss=forward_loss*(1-drop_rate);
						ack_loss=ack_loss*(1-drop_rate);
					}
					forward_loss = 1-forward_loss;
					ack_loss = 1-ack_loss;
					loss_cal->clear();
					loss_cal = stcpmsg->getLossCalculationMap();
					//the expected handshake phase latency
					handshake_latency = rtt + SYN_TIMEOUT*((1-forward_loss)/(1-2*forward_loss)+(1-ack_loss)/(1-2*ack_loss)-2);
					//the expected number of bytes sent in three handshake phase
					bytes_per_send_intv = BYTES_PER_SEGMENT; //send 1 data packet in this phase
					LOG_DEBUG("rtt="<<rtt<<", forward_loss="<<forward_loss<<", ack loss="<<ack_loss<<", handshake_latency="<<handshake_latency<<endl);
					//set timer to send data
					timer = new STCPSessionSendDataTimer(this, handshake_latency, bytes_per_send_intv);
					timer->set(0);
				}
				break;
			}
			case STCPMessage::UPDATE:{
				//check how many bytes should be sent in slow-start, and how many bytes have been sent
				//to determine this is slow start or steady state.
				LOG_DEBUG("Sender received UPDATE probe, send data packets."<<endl)
				//update RTT
				rtt = stcp_master->inHost()->getNow().second()-stcpmsg->getSendTime();
				STCPMessage::LossCalculationMap* cur_map = stcpmsg->getLossCalculationMap();
				for(STCPMessage::LossCalculationMap::iterator it=cur_map ->begin(); it!=cur_map ->end(); it++){
					LOG_DEBUG("sender get cur loss map, uid="<<it->first<<", in="<<(it->second)[0]
														                <<", out="<<(it->second)[1]
														                <<", queue="<<(it->second)[2]<<endl)
				}
			    for(STCPMessage::LossCalculationMap::iterator it=loss_cal->begin(); it!=loss_cal->end(); it++){
			        LOG_DEBUG("sender prev loss_cal map, uid="<<it->first<<", in="<<(it->second)[0]
																		 <<", out="<<(it->second)[1]
																	     <<", queue="<<(it->second)[2]<<endl)
				}
				for(STCPMessage::LossCalculationMap::iterator cur=cur_map->begin(); cur!=cur_map->end(); cur++){
					UID_t iface=cur->first;
					STCPMessage::LossCalculationMap::iterator prev=loss_cal->find(iface);
					int bytes_in = (cur->second)[0]-(prev->second)[0];
					int bytes_out = (cur->second)[1]-(prev->second)[1];
					int queue = (cur->second)[2]-(prev->second)[2];
					if(queue<0) queue=0;
					float drop_rate = (bytes_in-bytes_out-queue)/bytes_in;
					LOG_DEBUG("******bytes_in="<<bytes_in<<", bytes_out"<<bytes_out<<", queue="<<queue<<endl)
					forward_loss=forward_loss*(1-drop_rate);
					ack_loss=ack_loss*(1-drop_rate);
				}
				//update loss rate
				forward_loss = 1-forward_loss;
				ack_loss = 1-ack_loss;
				LOG_DEBUG("forward loss="<<forward_loss<<", ack loss="<<ack_loss<<endl)
				//check if it is still slow start or steady state
				if(is_slow_start){
					num_of_segments_to_send = (int)ceil(stcpmsg->getBytesToSend()/BYTES_PER_SEGMENT);
					if(forward_loss==0){
						bytes_sent_slow_start=stcpmsg->getBytesToSend();
					}else{
						bytes_sent_slow_start = (uint32_t)(BYTES_PER_SEGMENT * (1-pow((1-forward_loss), num_of_segments_to_send))*(1-forward_loss)/forward_loss);
					}
					LOG_DEBUG("slow start, num of bytes to send in this stage ="<<bytes_sent_slow_start
							<<", num_of_segments_to_send="<<num_of_segments_to_send
							<<", sender has sent "<<total_bytes_sent<<endl)
					if(total_bytes_sent<bytes_sent_slow_start){
						//This will still be in slow start
						//the expected window size at the end of slow start phase
						send_window = (uint32_t)((bytes_sent_slow_start+2)/((3+sqrt(5))/2));
						LOG_DEBUG("in slow start, send window="<<send_window<<endl)
						//the expected slow-start latency
						if(send_window > MAXIMUM_WINDOW){
							duration_slow_start = ceil(log(MAXIMUM_WINDOW/((5+sqrt(5))/10))/log((1+sqrt(5))/2))
								+(bytes_sent_slow_start-MAXIMUM_WINDOW*((3+sqrt(5))/10)-2)/MAXIMUM_WINDOW;
						}else{
							duration_slow_start = ceil(log((bytes_sent_slow_start+2)/((5+sqrt(5))/10))/log((1+sqrt(5))/2))-2;
						}
						//average send rate during slow start phase
						send_rate = bytes_sent_slow_start/duration_slow_start; //bytes per second
						LOG_DEBUG("in slow start, duration of slow start ="<<duration_slow_start
													<<", send rate="<<send_rate<<endl)
					}else{
						//This should have entered steady-state phase
						is_slow_start=false;
						//An approximation of send rate during steady-state phase
						double a = MAXIMUM_WINDOW/rtt;
						double b = 1/(rtt*sqrt(2*B*forward_loss)
							+TIMEOUT* mymin((double)1,3*sqrt(3*B*forward_loss/8))*forward_loss*(1+32*pow(forward_loss,2)));
						send_rate = mymin(a,b);
						LOG_DEBUG("should have entered steady-state phase, send rate="<<send_rate<<endl)
					}
				}else{
					//An approximation of send rate during steady-state phase
					double a = MAXIMUM_WINDOW/rtt;
					double b = 1/(rtt*sqrt(2*B*forward_loss)
						+TIMEOUT* mymin((double)1,3*sqrt(3*B*forward_loss/8))*forward_loss*(1+32*pow(forward_loss,2)));
					send_rate = mymin(a,b);
					LOG_DEBUG("steady-state phase, send rate="<<send_rate<<endl)
				}
				//update the send interval according to the loss rate
				float send_intv=0;
				if(forward_loss>0){
					send_intv=stcpmsg->getMinTimestep();
				}else{
					send_intv=MAX_SEND_INTV;
				}
				LOG_DEBUG("SEND RATE="<<send_rate<<", send intv="<<send_intv<<endl)
				LOG_DEBUG("bytes_to_send="<<stcpmsg->getBytesToSend()<<", cal bytes_intv (uint32_t)="<<(uint32_t)send_rate*send_intv
						<<", cal bytes_intv="<<send_rate*send_intv
						<<", min="<<mymin(stcpmsg->getBytesToSend(), send_rate*send_intv)<<endl)
				bytes_per_send_intv = (uint32_t)mymin(stcpmsg->getBytesToSend(), send_rate*send_intv);
				bytes_to_send_per_update = bytes_per_send_intv*MAX_SEND_INTV*UPDATE_PROBE_INTV;
				LOG_DEBUG("rtt="<<rtt<<", forward_loss="<<forward_loss<<endl);

				//XXX, change the send interval for update probes according to the loss/send rate
				if(timer){
					LOG_DEBUG("reset bytes to send = "<<bytes_per_send_intv<<endl)
					timer->setBytesToSend(bytes_per_send_intv);
					timer->setSendIntv(send_intv);
				}
				break;
			}
			case STCPMessage::END:{
				//stop sending data, delete timer
				LOG_DEBUG("host "<<getMaster()->inHost()->getUID()
						<<" receives END probe at time "<<getMaster()->inHost()->getNow().second()<<endl);
				if(timer){
					timer->cancel();
					delete timer;
					timer=0;
				}
				//XXX, close the socket
				//socket->disconnect();
				break;
			}
			default:
				LOG_ERROR("Should not happen."<<endl);
				break;
			}
		}
}

void STCPSession::setLastProbeSent(VirtualTime t){
	last_probe_sent = t;
}

void STCPSession::addTotalBytesSent(uint32_t bytes_sent){
	total_bytes_sent += bytes_sent;
}
int STCPSession::getID() { return conn->dst_port*100000+conn->src_port;}


}; // namespace ssfnet
}; // namespace prime
