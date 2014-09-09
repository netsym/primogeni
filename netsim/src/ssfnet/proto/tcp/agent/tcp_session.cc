/*
 * 
 * Copyright (c) 1990, 1997 Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms are permitted
 * 
 * Copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,s
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Lawrence Berkeley Laboratory,
 * Berkeley, CA.  The name of the University may not be used to
 * endorse or promote products derived from this software without
 * specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

/*
 * TCP-Linux module for PRIMEX
 *
 * September 2010
 *
 * Author: Miguel Erazo (miguel.erazo@gmail.com)
 *
 * The PRIME Research Group
 * School of Computing and Information Sciences
 * Florida International University
 * Miami, FL 33199, USA
 *
 */

/*
 * TCP-Linux module for NS2
 * May 2006
 * Author: Xiaoliang (David) Wei  (DavidWei@acm.org)
 * NetLab, the California Institute of Technology
 * http://netlab.caltech.edu
 *
 */

#include "tcp_session.h"
#include "proto/simple_socket.h"
#include "os/logger.h"
#include "../../../net/host.h"

namespace prime {
namespace ssfnet {

//#define LOG_DEBUG(X) std::cout<<X;

LOGGING_COMPONENT(TCPSession);

TCPSession::TCPSession(TCPMaster* master, SimpleSocket* sock, TCPConnection* conn)
{
	/*
	 * Initilialize all instance variables
	 */
	close_session_interval = 0;
	close_session_enabled = false;
	seqnoForClosing = 0;
	acknoForClosing = 0;

	open_session_interval = 0;
	open_session_enabled = false;
	seqnoForOpening = 0;
	acknoForOpening = 0;

	cached_options = 0;
	//cached_opts_en = null;

	tcpMaster = master;
	tcpSock = sock;
	tcpConn = conn;
	sndWnd = 0;
	changedContainer = false;

	//Enable timestamps
	ts_enabled = 1;

	reported = false;

	//Timers
	rtx_timer_ = new TCPTimer(TCP_TIMER_RTX, this);
	conn_termination_timer_ = new TCPTimer(TCP_TIMER_CONNTERM, this);
	smp_timer = new TCPSamplingTimer(this);

	agent_tracefile = 0;
	state = TCP_STATE_CLOSED;
	peer_wnd_scalable = true;

	//Initialize Linux TCP variables
	tss_size_ = 100;
	tss_ = (double*) calloc(tss_size_, sizeof(double));

	maxBurst = 0;
	nextPktsInFlight = 0;
	overhead_ = 0;
	curSeq = 0;
	ndatapack_ = 0;
	ndatabytes_ = 0;
	nackpack_ = 0;

	bugfix_ts_ = true;

	ssthresh_ = 0;
	last_ack_ = 0;
	recover_ = 0;
	congAction = false;
	necnresponses_ = 0;
	// Default changed on 2006/1/24
	precisionReduce = true;
	first_decrease_ = 1;
	ecn_backoff_ = 0;
	ect_ = 0;
	maxrto_ = 100000;
	// Default changed to 200ms on 2004/10/14, to match values used by many implementations
	minrto_ = 0.2;
	lastreset_ = 0;
	nrexmitpack_ = 0;
	nrexmitbytes_ = 0;
	nrexmit_ = 0;
	control_increase_ = 0;
	high_p_ = 0.0000001;
	QOption_ = 0;
	qs_cwnd_ = 0;
	wnd_init_option_ = 1;

	awnd_ = 0;
	wnd_init_ = 2;
	wnd_option_ = 1;
	wnd_th_ = 0.002;
	// cwnd_frac_ deleted on 6/6/04
	cwnd_range_ = 0;
	ncwndcuts_ = 0;
	ncwndcuts1_ = 0;
	count_ = 0;
	// Default set to "true" on 2002/03/07
	rfc2988_ = true;

	// Variable added on 2001/05/11
	timerfix_ = true;
	ts_peer_ = 0;
	// Added 2003/07/24
	ts_resetRTO_ = false;
	ts_option_ = false;
	ts_echo_ = 0;
	tss_size_ = 100;

	T_RTTVAR_BITS = 2;
	t_rtt_ = 0;
	T_SRTT_BITS = 3;
	t_srtt_ = 0;
	t_rttvar_ = 0;
	rttvar_exp_ = 2;
	rtt_seq_ = -1;
	rtt_active_ = 0;
	rtt_ts_ = 0;
	EnblRTTCtr_ = 0;
	srtt_init_ = 0;
	rttvar_init_ = 12;
	rtxcur_init_ = 3.0; // Default changed on 2006/01/21

	// These are all used for high-speed TCP.
	low_window_ = 38; // default changed on 2002/8/12.
	high_window_ = 83000;

	t_backoff_ = 0;
	initialized_ = false;
	scb_ = new ScoreBoard2();

	k_parameter_ = 0.0; // for binomial congestion control
	l_parameter_ = 1.0; // for binomial congestion control
	repeatedAck = 0;
	maxseq_ = 0;

	tcp_tick_ = 0.01; //default changed on 2002/03/07
	max_ssthresh_ = 0;
	fcnt_ = 0;
	maxcwnd_ = master->getSndWndSize();
	wnd_const_ = 4;
	decreaseNum = 0.5;
	increaseNum = 1.0;

	msl_timer_count = 0;
	slow_start_restart_ = true;
	restart_bugfix_ = true;
	frto_ = 0;
	bugfix_ss_ = 1; // Variable added on 2006/06/13
	frto_enabled_ = 0; // Added on 2004/10/26 for F-RTO
	sfrto_enabled_ = 0; // Added on 2004/10/26 for F-RTO
	// Added for congruence between ns-2 and prime initialization
	ts_option_ = true;
	rcv_next_ = 0;

	// The upper layer has not issued a close yet
	close_issued = false;
	simultaneous_closing = false;

	lastSeqno=-1;
	lastAck=-1;
	sessionNumber=-1;
	//LOG_DEBUG("TCPMASTER has been initialized"<<endl)
}

TCPSession::~TCPSession()
{
	LOG_DEBUG("debug[" << this->getMaster()->inHost()->getNow().second() << "] erasing this session" <<
			" srcport=" << this->getTcpConn()->srcPort << " dstport=" << this->getTcpConn()->dstPort <<
			" srcip=" << this->getTcpConn()->srcIP << " dstip=" << this->getTcpConn()->dstIP <<
			" myip=" << this->getMaster()->inHost()->getDefaultIP() << endl);

	delete scb_;
	removeCongestionControl();

	//erase rcv and snd windows, the socket, sink, and timers
	if(sink->rcvWnd) delete sink->rcvWnd;
	if(sndWnd) delete sndWnd;
	if(tcpSock) delete tcpSock;
	if(sink) delete sink;
	if(agent_tracefile){
		fclose(agent_tracefile);
	}
	if(rtx_timer_){
		rtx_timer_->cancel();
		delete rtx_timer_;
		rtx_timer_=0;
	}
	if(conn_termination_timer_){
		conn_termination_timer_->cancel();
		delete conn_termination_timer_;
		conn_termination_timer_=0;
	}
	if(smp_timer){
		smp_timer->cancel();
		delete smp_timer;
		smp_timer=0;
	}
//	if(filePointer)
//		fclose(filePointer);
}

void TCPSession::reset() {

	//LOG_DEBUG("TCPSession is being initialized..." << endl);
	scb_->ClearScoreBoard();

	// Initialize TCP side
	t_seqno_ = 0;
	highest_ack_ = -2;
	cwnd_ = 2;
	wnd_ = tcpMaster->getSndWndSize();

	// Initialize Linux side
	linux_.icsk_ca_state = TCP_CA_Open;
	linux_.snd_cwnd_stamp = 0;
	linux_.bytes_acked = 0;
	linux_.icsk_ca_ops = NULL;
	linux_.snd_cwnd_cnt = 0;
	linux_.snd_cwnd = 2;

	initialized_ = false;

	rttInit();
	rtt_seq_ = -1;
	curSeq = 0;
	setInitialWindow();
	maxseq_ = -1;
	last_ack_ = -1;
	ssthresh_ = int(wnd_);
	if (max_ssthresh_ > 0 && max_ssthresh_ < ssthresh_){
		ssthresh_ = max_ssthresh_;
	}
	wnd_restart_ = 1.;
	awnd_ = wnd_init_ / 2.0;
	recover_ = 0;
	last_cwnd_action_ = 0;
	first_decrease_ = 1;
	lastreset_ = 0;

	// Now these variables will be reset
  	ndatapack_ = 0;
	ndatabytes_ = 0;
	nackpack_ = 0;
	nrexmitbytes_ = 0;
	nrexmit_ = 0;
	nrexmitpack_ = 0;
	necnresponses_ = 0;
	ncwndcuts_ = 0;
	ncwndcuts1_ = 0;

	if (control_increase_) {
		prev_highest_ack_ = highest_ack_;
	}

	if (wnd_option_ == 8) {
		// HighSpeed TCP
		hstcp_.low_p = 1.5 / (low_window_ * low_window_);
		double highLowWin = log(high_window_) - log(low_window_);
		double highLowP = log(high_p_) - log(hstcp_.low_p);
		hstcp_.dec1 = 0.5 - log(low_window_) * (high_decrease_ - 0.5)
				/ highLowWin;
		hstcp_.dec2 = (high_decrease_ - 0.5) / highLowWin;
		hstcp_.p1 = log(hstcp_.low_p) - log(low_window_) * highLowP
				/ highLowWin;
		hstcp_.p2 = highLowP / highLowWin;
	}

	if (QOption_) {
		int now = (int) (tcpMaster->getNow().second() / tcp_tick_ + 0.5);
		T_last = now;
		T_prev = now;
		W_used = 0;
		if (EnblRTTCtr_) {
			resetQOption();
		}
	}

	// Initialize Linux side variables from instance variables of this class
	loadToLinuxOnce();
	loadToLinux();

    //LOG_DEBUG("TCPSession has been initialized...with " << tcpMaster->getTcpCA() << " IP=" << tcpMaster->inHost()->getDefaultIP() << "\n");
}

void TCPSession::rttInit() {
	t_rtt_ = 0;
	t_srtt_ = int(srtt_init_ / tcp_tick_) << T_SRTT_BITS;
	t_rttvar_ = int(rttvar_init_ / tcp_tick_) << T_RTTVAR_BITS;
	t_rtxcur_ = rtxcur_init_;
	t_backoff_ = 1;
}

void TCPSession::init()
{
	filePointer=0;
	smp_timer->resched(tcpMaster->getSamplingInterval());
	//****************************************

	// Initialize variables
	reset();

	// Install Linux TCP CA algorithm
	linuxTcpCA = new char[tcpMaster->getTcpCA().length() + 1];
	strcpy(linuxTcpCA, tcpMaster->getTcpCA().c_str());

	if (installCongestionControl(linuxTcpCA) == false){
		LOG_ERROR("\n ERROR: in INIT: selected CA algorithm not found");
	}
}

double TCPSession::initialWindow()
{
	// If Quick-Start Request was approved, use that as a basis for
	// initial window

	int size_ = tcpMaster->getMSS();
	LOG_DEBUG("MSS in Master =" << this->getMaster()->getMSS() << endl);

	if (qs_cwnd_) {
		return (qs_cwnd_);
	}
	//
	// init_option = 1: static iw of wnd_init_
	//
	if (wnd_init_option_ == 1) {
		return (wnd_init_);
	} else if (wnd_init_option_ == 2) {
		// do iw according to Internet draft
		if (size_ <= 1095) {
			return (4.0);
		} else if (size_ < 2190) {
			return (3.0);
		} else {
			return (2.0);
		}
	}
	// what should we return here???
	fprintf(stderr, "Wrong number of wnd_init_option_ %d\n", wnd_init_option_);
	abort();
	return (2.0); // make msvc happy.
}

void TCPSession::setInitialWindow()
{
	// Set the congestion window
	cwnd_ = initialWindow();
}

bool TCPSession::passiveOpen() {
	// Allocate buffers
	//LOG_DEBUG("PASSIVE OPEN!" << endl);
	allocate_buffers();

	// go to TCP_STATE_LISTEN state
	initStateListen();
	return true;
}

bool TCPSession::activeOpen() {
	// Allocate buffers
	//LOG_DEBUG("ACTIVE OPEN!" << endl);
	allocate_buffers();

	init_state_closed();
	return true;
}

int TCPSession::getID() {
	return tcpConn->dstPort*100000+tcpConn->srcPort;
}

void TCPSession::allocate_buffers() {
	// Allocate send buffer
	// On this implementation the window size can be as large as the buffer size
	sndWnd = new TCPSendWindow(this, 0, tcpMaster->getSndBufSize(),
			tcpMaster->getSndWndSize(), this);
	assert(sndWnd);

	// Allocate receive buffer and make the sink code visible to it
	sink->rcvWnd = new TCPRecvWindow(0, tcpMaster->getRcvWndSize(), this);
	// Configure receive window associating it to the sink session
	sink->rcvWnd->configure(this->sink);
	assert(sink->rcvWnd);
}

void TCPSession::resetQOption() {
	int now = (int) (tcpMaster->getNow().second() / tcp_tick_ + 0.5);

	T_start = now;
	W_timed = -1;
	Backoffs = 0;
}

void TCPSession::receive(TCPMessage* tcphdr, void* extinfo)
{
#if TEST_ROUTING == 1
	//miguel has some messed up logic where somehow we get misformed packets....
	//we are being lazy and not diving into the hell that is TCP to figure this out
	if(tcphdr->getPacket())  {
		saveRouteDebug(tcphdr->getPacket(),NULL);
	}
#endif
	LOG_DEBUG("---> TCP RECEIVED A PKT WITH state=" << state <<
				" seqno=" << tcphdr->getSeqno() << " ackno=" << tcphdr->getAckno() <<
				" length=" << tcphdr->getLength() <<
				" hastpayload: " << tcphdr->hasPayload() << "\n");

	if(tcphdr->hasPayload()){
	LOG_DEBUG("[" << this->getMaster()->inHost()->getNow().second() << "] " <<
			"TCPSession: received a packet with seqno=" << tcphdr->getSeqno() <<
			" ackno=" << tcphdr->getAckno() << " tcphdr->size=" << tcphdr->size() <<
			" tcphdr->getFlags()=" << (int)tcphdr->getFlags() <<
			" payload_size=" << tcphdr->payload()->size() <<
			" state=" << state << endl);
	} else {
		LOG_DEBUG("[" << this->getMaster()->inHost()->getNow().second() << "] " <<
				"TCPSession: received a packet with seqno=" << tcphdr->getSeqno() <<
				" ackno=" << tcphdr->getAckno() << " tcphdr->size=" << tcphdr->size() <<
				" tcphdr->getFlags()=" << (int)tcphdr->getFlags() <<
				" NO PAYLOAD" << endl);
	}
	
	if(state == TCP_STATE_LAST_ACK){
		LOG_DEBUG("this packet will make us close the session correctly flags:" << tcphdr->getFlags() << endl);
	}

	if (tcphdr->getFlags() & TCPMessage::TCP_FLAG_RST) {
		if (state != TCP_STATE_LISTEN) reset();
		    return;
	}

	// if it's a SYN segment, handle it
	if (tcphdr->getFlags() & TCPMessage::TCP_FLAG_SYN) {
		LOG_DEBUG("received a syn packet" << endl);
		processSYN(tcphdr);
	}

	// if it's a FIN segment, handle it
	if (tcphdr->getFlags() & TCPMessage::TCP_FLAG_FIN) {
		LOG_DEBUG("TCPSession: FIN pkt RX" << endl);
		processFIN(tcphdr);
		return;
	}

	// if it's an ACK segment, handle it
	if (tcphdr->getFlags() & TCPMessage::TCP_FLAG_ACK) {
		processACK(tcphdr, extinfo);
	}

	if (state == TCP_STATE_CLOSED){
		return;
	}
}

void TCPSession::processSYN(TCPMessage* tcphdr)
{
	LOG_DEBUG("just received a syn packet state=" << state << endl);

	switch (state) {
	case TCP_STATE_SYN_SENT: {
		LOG_DEBUG("[debug longdelays] [ " << this->getMaster()->inHost()->getNow().second() << " ] rx a syn being at TCP_STATE_SYN_SENT, " <<
				" srcport=" << this->tcpConn->srcPort <<
				" dstport=" << this->tcpConn->dstPort << endl);

		struct OptionsEnabled opt_en;
		int options = 0;

		//get Options
		getOptions(options, &opt_en, tcphdr);

		//LOG_DEBUG("\t\t OPTIONS GOT:" << options << "\n");

		if (options & TSOPT_OPT) {
			ts_enabled = 1;
			ts_peer_ = opt_en.ts_value;
		}

		LOG_DEBUG("debug[" << this->getMaster()->inHost()->getNow().second() << "] send_data 1" <<
				" srcport=" << this->getTcpConn()->srcPort << " dstport=" << this->getTcpConn()->dstPort <<
				" srcip=" << this->getTcpConn()->srcIP << " dstip=" << this->getTcpConn()->dstIP <<
				" myip=" << this->getMaster()->inHost()->getDefaultIP() << endl);

		// Send data including timestamps
		LOG_DEBUG("in processack, [processsyn] received a syn with seqno=" << tcphdr->getSeqno() << " and ackno=" << 
			tcphdr->getAckno() << " and sending the ack, seqno=" <<
				tcpMaster->getISS() << " ackno=" << tcphdr->getSeqno() + 1 << endl);

		// Initialize the receive window
		sink->rcvWnd->setStart(tcphdr->getSeqno());
		sendData(tcpMaster->getISS() + 1, 0, TCPMessage::TCP_FLAG_ACK, tcphdr->getSeqno() + 1, true, true, options, &opt_en);	

		break;
	}
	case TCP_STATE_SYN_RECEIVED:
		//handle this case
		return;
	case TCP_STATE_LISTEN:

		LOG_DEBUG("[debug longdelays] [ " << this->getMaster()->inHost()->getNow().second() << " ] rx a syn being at TCP_STATE_LISTEN, " <<
				" srcport=" << this->tcpConn->srcPort <<
				" dstport=" << this->tcpConn->dstPort << endl);

		// Initialize the receive window
		sink->rcvWnd->setStart(tcphdr->getSeqno());
		sink->rcvWnd->setSYN(true);

		// Get initial sequence no and initialize Send Window
		sndWnd->setStart(tcpMaster->getISS());
		sndWnd->setISSPeer(tcphdr->getSeqno());
		rcvWndSize = tcphdr->getWSize();
		initStateSynReceived(tcphdr);

		break;
	}
}

void TCPSession::processFIN(TCPMessage* tcphdr)
{
	LOG_DEBUG("-------[closing sessions] [" << this->getMaster()->inHost()->getNow().second() <<
		"] [" << this->getMaster()->getUID() << "] received a FIN in state=" << state << endl);

	switch (state) {
	case TCP_STATE_ESTABLISHED:
		 if(close_issued) simultaneous_closing = true;
		 initStateCloseWait(tcphdr);
		 break;
	case TCP_STATE_FIN_WAIT_1:
		initStateTimeWait(tcphdr);
		LOG_DEBUG("fin received in TCP_STATE_FIN_WAIT_1" << endl);
		break;
	case TCP_STATE_FIN_WAIT_2:
		initStateTimeWait(tcphdr);
		break;
	case TCP_STATE_LAST_ACK:
		initStateClosed();
		break;
	case TCP_STATE_TIME_WAIT:
		break;
	default:
		// other states, do nothing right now
		break;
	}
}

bool TCPSession::processACK(TCPMessage* tcphdr, void* extinfo)
{
	uint32 socket_status_mask_ = 0;
	uint32 bytes_to_deliver = 0;

	switch (state) {
	case TCP_STATE_CLOSED:
		return false;

	case TCP_STATE_LISTEN:
		// shouldn't be receiving ACK's yet
		LOG_DEBUG("PANIC: TCPSession::Should not be receiving ACK's yet" << endl);
		return false;

	case TCP_STATE_SYN_SENT: {
		//CLIENT
		// Handle this case when the session is acting as a client
		// SEND what we have on the buffer
		state = TCP_STATE_ESTABLISHED;

		if(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
				state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT) {
			return false;
		} else {
			sendMuch(0, 0, maxBurst);
		}

		return false;
	}
	case TCP_STATE_SYN_RECEIVED: {
		int options = 0;
		struct OptionsEnabled opt_en;
		byte* app_data = NULL;
		int data_size = 0;

		getOptions(options, &opt_en, tcphdr);

		// server
		initStateEstablished();

		sink->rcvRecv(tcphdr, socket_status_mask_, bytes_to_deliver, opt_en, options, app_data, data_size);
		LOG_DEBUG("id=" << this->getID() << "BYTES: TCP_STATE_SYN_RECEIVED bytes_to_deliver=" << bytes_to_deliver <<
				" size of msg=" << tcphdr->size() << " size of tcphdr=" << tcphdr->size() << endl);

		sndRecv(tcphdr, opt_en, options);

		if (tcphdr->hasPayload()) {
			rcv_next_ = tcphdr->getSeqno() + (tcphdr->payload())->size(); //check whether size() is returning a correct value
		} else {
			//Do nothing
		}

		DataMessage* dm = 0;
		//If this packet has payload, drop it to a pointer
		if(tcphdr->hasPayload()) {
			dm = (DataMessage*) tcphdr->payload();
		} else {
			dm = 0;
		}

		if (dm) {
			LOG_DEBUG("\tA pkt with a payload has arrived size=" << dm->size() << "\n");
		} else {
			LOG_DEBUG("\tthis packet does not have a payload associated\n");
		}

		LOG_DEBUG("\tBEFORE TESTING PSH TCPMessage::TCP_FLAG_PSH " <<
				" psh" << (int)tcphdr->getFlags() << endl);

		if (tcphdr->getFlags() & TCPMessage::TCP_FLAG_PSH) {
			// There exists a payload that must be conveyed to the application
			LOG_DEBUG("a packet with a psh flag received in state=TCP_STATE_SYN_RECEIVED" << endl);
			socket_status_mask_ |= SimpleSocket::SOCKET_PSH_FLAG;
			//LOG_DEBUG("	A ACK PKT WITH THE PUSH FLAG WAS SENT! IP=" << tcpMaster->inHost()
			//->getDefaultIP() << "\n");
			if (reported == false) {
				LOG_DEBUG("SENDING DATA TO APP 1" << endl);
				reported = true;
				if(dm){
					LOG_DEBUG("SENDING DATA TO EMU APP 1 mask=" << socket_status_mask_ <<
						" size=" << dm->size() << endl);
					//tcpSock->recv(dm->size(), (char*)(dm->getRawData()), socket_status_mask_);
					tcpSock->recv(bytes_to_deliver, (char*)(dm->getRawData()), socket_status_mask_);
				} else {
					LOG_DEBUG("SENDING DATA TO EMU APP 2 mask=" << socket_status_mask_ << endl);
					//tcpSock->recv(tcphdr->size(), 0 ,socket_status_mask_);
					tcpSock->recv(bytes_to_deliver, 0 ,socket_status_mask_);
				}
			}
		}
		tcphdr->erase();
		return true;
	}
	case TCP_STATE_ESTABLISHED:
	{
		int options = 0;
		struct OptionsEnabled opt_en;
 		byte* app_data = NULL;
		int data_size = 0;

		getOptions(options, &opt_en, tcphdr);

		if(this->sndWnd->acknoToPktno(tcphdr->getAckno()) > this->curSeq && curSeq != 0){
			//Signal the App that all the bytes sent previously were received
			LOG_DEBUG("in tcpsession, all bytes received by the receiver at curseq=" << curSeq << endl);
			tcpSock->allBytesReceived();
		}

		LOG_DEBUG("got an ack pkt with ack=" << this->sndWnd->acknoToPktno(tcphdr->getAckno()) << endl);
		//LOG_DEBUG("\tprocessACK: TCP_STATE_ESTABLISHED=" << (int)tcphdr->flags << "\n");
		//TCPMessage* newpkt = new TCPMessage(*(tcphdr));
		TCPMessage* newpkt = tcphdr;

		sink->rcvRecv(tcphdr, socket_status_mask_, bytes_to_deliver, opt_en, options, app_data, data_size);

		//LOG_DEBUG("[" << this->getMaster()->inHost()->getNow().second() << "]" <<
		//		"in processack: data_size=" << data_size << " bytes_to_deliver=" << bytes_to_deliver << endl);

		if(tcphdr->hasPayload()){
			if(tcphdr->payload()->getRawData()!=NULL){
				LOG_DEBUG("numtodeliver real from process ack=" << bytes_to_deliver << endl);
			}
		}

		//LOG_DEBUG("BYTES: TCP_STATE_ESTABLISHED bytes_to_deliver=" << bytes_to_deliver <<
			//	" size of tcphdr=" << tcphdr->size() << " size of tcphdr=" << tcphdr->size() << endl);

		//xxx: sndRecv(tcphdr);
		//if (this->t_seqno_ < this->curSeq) {
			sndRecv(tcphdr, opt_en, options);
		/*} else {
			//getOptions(options, &opt_en, tcphdr);
			// Updtate ts_peer_ even for old acks
			if (tcphdr->getOptions() && opt_en.ts_value > 0) {
				ts_peer_ = opt_en.ts_value; //record timestamp for echoing
			}
		}*/

		//LOG_DEBUG("BYTES: TCP_STATE_ESTABLISHED AFTER bytes_to_deliver=" << bytes_to_deliver <<
			//	" size of msg=" << tcphdr->size() << endl);

		if (tcphdr->hasPayload()) {
			//LOG_DEBUG("---********** Updating rcv_next_\n");
			rcv_next_ = tcphdr->getSeqno() + (tcphdr->payload())->size();
		} else {
			//Do nothing
		}

		//Send the data up the protocol stack
		DataMessage* dm = 0;
		if (tcphdr->hasPayload()) {
			dm = (DataMessage*) newpkt->payload();
			LOG_DEBUG("BYTES: TCP dmsize=" << dm->size());
		}
		/*app_data, data_size
		if (data_size > 0) {
			dm = new DataMessage();
			LOG_DEBUG("BYTES: TCP dmsize=" << dm->size());
		}*/

		LOG_DEBUG("BEFORE TESTING PSH 2," << " mask=" <<
				socket_status_mask_ << " flags=" << (int)(tcphdr->getFlags()) << endl);

		if (tcphdr->getFlags() & TCPMessage::TCP_FLAG_PSH && data_size > 0) {
			//There exists a payload that must be conveyed to the application
			socket_status_mask_ |= SimpleSocket::SOCKET_PSH_FLAG;

			LOG_DEBUG("a packet with a psh flag received in state=TCP_STATE_ESTABLISHED" << endl)

			LOG_DEBUG("about to send to app: data_size=" << data_size << " app_data" << app_data << endl);
			tcpSock->recv(data_size, (char*)app_data, socket_status_mask_);

			return true;
		}

		// Update the application the number of the received bytes even though the PSH flag was not set
		if((socket_status_mask_ & SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED) &&
				!(socket_status_mask_ & TCPMessage::TCP_FLAG_PSH) && bytes_to_deliver > 0){
			LOG_DEBUG("SENDING DATA TO EMU APP 5 mask=" << socket_status_mask_ <<
				" size=" << bytes_to_deliver << endl);
			tcpSock->recv(bytes_to_deliver, 0, socket_status_mask_);
		}
		tcphdr->erase();
		return true;
	}
	case TCP_STATE_CLOSE_WAIT:{}
	case TCP_STATE_LAST_ACK:{}
	case TCP_STATE_FIN_WAIT_1:{}
	case TCP_STATE_FIN_WAIT_2:{}
	case TCP_STATE_CLOSING:{}
	case TCP_STATE_TIME_WAIT:{
		return false;
	}
	}
	return false;
}

void TCPSession::tcpFastRetransAlert(unsigned char flag) {
	struct inet_connection_sock *icsk = &linux_;

	if (linux_.icsk_ca_state == TCP_CA_Open) {
		//no need to exit unless frto.
	} else {
		if (highest_ack_ >= recover_) {
			scb_->ClearScoreBoard();
			if (linux_.icsk_ca_ops == NULL)
				loadToLinux();
			if (linux_.snd_cwnd < linux_.snd_ssthresh) {
				linux_.snd_cwnd = linux_.snd_ssthresh;
				tcpModerateCwnd();
			}
			tcp_set_ca_state(TCP_CA_Open);
			nextPktsInFlight = 0; //stop rate halving
			return;
		}
	}

	// F. Process state
	switch (linux_.icsk_ca_state) {
	case TCP_CA_Recovery:
		break;
	case TCP_CA_Loss:
		//let it through
		break;
	default:
		//TCP_CA_Open and have loss or ECE
		if (flag & (FLAG_DATA_LOST | FLAG_ECE)) {
			recover_ = maxseq_;
			last_cwnd_action_ = CWND_ACTION_DUPACK;
			touchCwnd();
			nextPktsInFlight = linux_.snd_cwnd; //we do rate halving
			if (linux_.icsk_ca_ops == NULL) {
				slowdown(CLOSE_SSTHRESH_HALF | CLOSE_CWND_HALF);
				loadToLinux();
			} else {
				//ok this is the linux part
				//DEBUG(5, "check ssthresh\n");
				linux_.snd_ssthresh = icsk->icsk_ca_ops->ssthresh(&linux_);
				linux_.snd_cwnd_cnt = 0;
				linux_.bytes_acked = 0;
				//DEBUG(5, "check min_cwnd %p\n", icsk->icsk_ca_ops->min_cwnd);
				if (icsk->icsk_ca_ops->min_cwnd)
					linux_.snd_cwnd = icsk->icsk_ca_ops->min_cwnd(&linux_);//XXXXXXXXXXX<----------------
				else
					linux_.snd_cwnd = linux_.snd_ssthresh;
				ncwndcuts_++;
				congAction = true;
				// Linux uses a CWR process to halve rate, we have a simpler one.
			}
			if (flag & FLAG_ECE) {
				++necnresponses_;
			}
			tcp_set_ca_state(TCP_CA_Recovery);
		}
	}

}

void TCPSession::slowdown(int how) {
	//added for highspeed
	double decrease;
	double win, halfwin, decreasewin;
	int slowstart = 0;

	//double localCwnd_ = cwnd_;

	++ncwndcuts_;
	if (!(how & TCP_IDLE) && !(how & NO_OUTSTANDING_DATA)) {
		++ncwndcuts1_;
	}
	// we are in slowstart for sure if cwnd < ssthresh
	if (cwnd_ < ssthresh_)
		slowstart = 1;
	if (precisionReduce) {
		halfwin = windowd() / 2;
		if (wnd_option_ == 6) {
			/* binomial controls */
			decreasewin = windowd() - (1.0 - decreaseNum) * pow(windowd(),
					l_parameter_);
		} else if (wnd_option_ == 8 && (cwnd_ > low_window_)) {
			/* experimental highspeed TCP */
			decrease = decreaseParam();
			//if (decrease < 0.1)
			//      decrease = 0.1;
			decreaseNum = decrease;
			decreasewin = windowd() - (decrease * windowd());
		} else {
			decreasewin = decreaseNum * windowd();
		}
		win = windowd();
	} else {
		int temp;
		temp = (int) (window() / 2);
		halfwin = (double) temp;
		if (wnd_option_ == 6) {
			/* binomial controls */
			temp = (int) (window() - (1.0 - decreaseNum) * pow(window(),
					l_parameter_));
		} else if ((wnd_option_ == 8) && (cwnd_ > low_window_)) {
			/* experimental highspeed TCP */
			decrease = decreaseParam();
			//if (decrease < 0.1)
			//       decrease = 0.1;
			decreaseNum = decrease;
			temp = (int) (windowd() - (decrease * windowd()));
		} else {
			temp = (int) (decreaseNum * window());
		}
		decreasewin = (double) temp;
		win = (double) window();
	}
	if (how & CLOSE_SSTHRESH_HALF)
		// For the first decrease, decrease by half
		// even for non-standard values of decrease_num_.
		if (first_decrease_ == 1 || slowstart || last_cwnd_action_
				== CWND_ACTION_TIMEOUT) {
			// Do we really want halfwin instead of decreasewin
			// after a timeout?
			ssthresh_ = (int) halfwin;
		} else {
			ssthresh_ = (int) decreasewin;
		}
	else if (how & THREE_QUARTER_SSTHRESH)
		if (ssthresh_ < 3 * cwnd_ / 4)
			ssthresh_ = (int) (3 * cwnd_ / 4);
	if (how & CLOSE_CWND_HALF)
		// For the first decrease, decrease by half
		// even for non-standard values of decrease_num_.
		if (first_decrease_ == 1 || slowstart || decreaseNum == 0.5) {
			cwnd_ = (int) halfwin;
		} else
			cwnd_ = decreasewin;
	else if (how & CWND_HALF_WITH_MIN) {
		// We have not thought about how non-standard TCPs, with
		// non-standard values of decrease_num_, should respond
		// after quiescent periods.
		cwnd_ = decreasewin;
		if (cwnd_ < 1)
			cwnd_ = 1;
	} else if (how & CLOSE_CWND_RESTART) {
		cwnd_ = int(wnd_restart_);
	}
	else if (how & CLOSE_CWND_INIT) {
		cwnd_ = int(wnd_init_);
	}
	else if (how & CLOSE_CWND_ONE) {
		cwnd_ = 1;
	}
	else if (how & CLOSE_CWND_HALF_WAY) {
		// cwnd_ = win - (win - W_used)/2 ;
		cwnd_ = W_used + decreaseNum * (win - W_used);

		if (cwnd_ < 1)
			cwnd_ = 1;
	}
	if (ssthresh_ < 2)
		ssthresh_ = 2;
	if (how & (CLOSE_CWND_HALF | CLOSE_CWND_RESTART | CLOSE_CWND_INIT
			| CLOSE_CWND_ONE))
		congAction = true;

	fcnt_ = count_ = 0;
	if (first_decrease_ == 1)
		first_decrease_ = 0;
	// for event tracing slow start
	if (cwnd_ == 1 || slowstart) {
		// Not sure if this is best way to capture slow_start
		// This is probably tracing a superset of slowdowns of
		// which all may not be slow_start's --Padma, 07/'01.
		//trace_event("SLOW_START");
	}
}

/*
 * Limited Slow-Start for large congestion windows.
 * This should only be called when max_ssthresh_ is non-zero.
 */
double TCPSession::limitedSlowStart(double cwnd, int max_ssthresh,
		double increment) {
	if (max_ssthresh <= 0) {
		return increment;
	} else {
		double increment1 = 0.0;
		int round = int(cwnd / (double(max_ssthresh) / 2.0));
		if (round > 0) {
			increment1 = 1.0 / double(round);
		}
		if (increment < increment1) {
			return increment1;
		} else {
			return increment;
		}
	}
}

void TCPSession::sndRecv(TCPMessage* tcphdr, struct OptionsEnabled& opt_en, int& options)
{
	//int options = 0;
	//struct OptionsEnabled opt_en;
	int ack = 0;

	//getOptions(options, &opt_en, tcphdr);

	// Updtate ts_peer_ even for old acks
	if (tcphdr->getOptions() && opt_en.ts_value > 0) {
		ts_peer_ = opt_en.ts_value; //record timestamp for echoing
	}

	for (int i = 0; i < opt_en.sack_length; i = i + 2) {
		//printf("	***LEFT=%d RIGHT=%d [%s]\n", opt_en.sack[i], opt_en.sack[i+1], tcpMaster->myIP);
	}
	//LOG_DEBUG("\t sndRecv 2 \n")
	++nackpack_;

	//prior_snd_una: # of the next packet to be acked
	//ack : 		 number of the packet that is being acked
	//t_seqno_:		 # of the next packet to send

	struct sock* sk = &linux_;
	//uint32 prior_snd_una = highestAck + 1;
	int prior_snd_una = highest_ack_ + 1;
	//ack must be transformed to packet #

	LOG_DEBUG("testing emulation: " << " in " <<
			this->tcpMaster->inHost()->getDefaultIP() << " tcphdr->getAckno()" <<
			tcphdr->getAckno() << endl);

	ack = sndWnd->acknoToPktno(tcphdr->getAckno());

	LOG_DEBUG("[" << this->getMaster()->getUID() << "] at [" << this->tcpMaster->getNow().second() << "]" <<
			" original ackno=" << tcphdr->getAckno() << " translated to:" << ack << endl);

	uint32_t prior_in_flight;
	s32 seq_rtt;
	unsigned char flag = 0;

	//Get time this packet was received
	tcp_time_stamp = (unsigned long) (trunc(tcpMaster->getNow().second()
			* JIFFY_RATIO));
	ktime_get_real = (s64) trunc(tcpMaster->getNow().nanosecond());

	//Do not take into account this info in this cases
	if (ack > (int)t_seqno_) {
		LOG_DEBUG("[" << this->sessionNumber << "] at [" << this->tcpMaster->getNow().second() << "] uninteresting ack" << endl);
		return; // uninteresting_ack
	}
	if (ack < prior_snd_una) {
		LOG_DEBUG("[" << this->sessionNumber << "] at [" << this->tcpMaster->getNow().second() << "] old ACK!" << endl);
		// old_ack; only worth for D-SACK. but let's pass it.
		return;
	}

	if (linux_.icsk_ca_ops) {
		//This has to be done before the first call to linux_.icsk_ca_ops.
		//LOG_DEBUG("\t sndRecv 5 \n")
		paramManager.load_local();
		//After this call, all return must be done after a paramManager.restore_default, or save_to_linux (which inclues restore_default).
	}

	prior_in_flight = packetsInFlight();

	LOG_DEBUG("in processack, before update scb, ack=" << ack << "prior_snd_una=" << prior_snd_una << endl);
	if (ack > prior_snd_una) {
		//LOG_DEBUG("\t sndRecv 6 \n")
		linux_.bytes_acked += (ack - prior_snd_una) * linux_.mss_cache;
		flag |= (FLAG_DATA_ACKED);
		LOG_DEBUG("\t*** 1flag=" << (int)flag << "\n");
	} else {
		//LOG_DEBUG("\t sndRecv 7 \n")
		repeatedAck++;
	}
	//Update Sequence numbers in Linux side
	//if it is a repeated ACK then FLAG_UNSURE_TSTAMP is raised in flag
	//otherwise flag is kept untouched
	flag |= ackProcessing(tcphdr, flag);
	LOG_DEBUG("\t*** 2flag=" << (int)flag << "\n");

	//We update the SCB in case a SACK has been been sent in case non-contiguos
	//seqno numbers have been received
	if ((opt_en.sack_length > 0) || (FLAG_DATA_ACKED && (!scb_->IsEmpty()))) {
	 	LOG_DEBUG("in processack, updating scb with" << highest_ack_ << endl);	
		flag |= scb_->UpdateScoreBoard(highest_ack_, tcphdr, 3, &opt_en);
		LOG_DEBUG("\t*** 3flag=" << (int)flag << "\n");
	}
	timeProcessing(tcphdr, flag, &seq_rtt, &opt_en);
	LOG_DEBUG("\t*** 4flag=" << (int)flag << "\n");

	if (linux_.icsk_ca_ops) {
		if ((!initialized_)) {
			if (linux_.icsk_ca_ops->init)
				linux_.icsk_ca_ops->init(&linux_);
			initialized_ = true;
		}
		if ((flag & FLAG_NOT_DUP) && (linux_.icsk_ca_ops->pkts_acked)) {
			ktime_t last_ackt;
			if ((flag & FLAG_UNSURE_TSTAMP) || (!bugfix_ts_)) {
				last_ackt = 0;
			} else {
				a1_ = tcphdr->getAckno();
				a2_ = sndWnd->acknoToPktno(tcphdr->getAckno());
				a3_ = (sndWnd->acknoToPktno(tcphdr->getAckno())	% tss_size_);

				double then = tss_[sndWnd->acknoToPktno(tcphdr->getAckno()) % tss_size_];

				LOG_DEBUG("then=" << then << "\n");
				last_ackt = (s64) trunc(then * 1000000000);
			}
			linux_.icsk_ca_ops->pkts_acked(sk, ack - prior_snd_una, last_ackt);
		}
	}

#define tcp_ack_is_dubious(flag) (!(flag & FLAG_NOT_DUP) || \
	(flag & FLAG_CA_ALERT) || linux_.icsk_ca_state != TCP_CA_Open)

#define tcp_may_raise_cwnd(flag) (!(flag & FLAG_ECE) || linux_.snd_cwnd < linux_.snd_ssthresh) && \
    !((1 << linux_.icsk_ca_state) & (TCPF_CA_Recovery | TCPF_CA_CWR))

#define tcp_cong_avoid(ack, rtt, in_flight, good)										\
{																						\
	if (linux_.icsk_ca_ops) {															\
    	LOG_DEBUG("\tquerying specific algorithm with mss=" << linux_.mss_cache << " rtt=" << rtt << \
    		" in_flight=" << in_flight << " good=" << good << "\n"); 					\
    	linux_.icsk_ca_ops->cong_avoid(sk, ack*linux_.mss_cache, rtt/1000, in_flight, good); \
    } else {																			\
      openCwnd();																		\
      loadToLinux();																	\
    }																					\
    touchCwnd(); \
	if ( cwnd_ > maxcwnd_){ cwnd_ = maxcwnd_; } \
}

	//LOG_DEBUG("\t sndRecv 17 \n")
	if (tcp_ack_is_dubious(flag)) {
		//LOG_DEBUG("\t sndRecv 18 \n")

		if ((flag & FLAG_DATA_ACKED) && tcp_may_raise_cwnd(flag)) {
			tcp_cong_avoid(ack, seq_rtt, prior_in_flight, 0);
		}
		tcpFastRetransAlert(flag);
	} else {
		//LOG_DEBUG("\t sndRecv 19 \n")
		if ((flag & FLAG_DATA_ACKED)) {
			//LOG_DEBUG("\t sndRecv 20 \n")
			prev_highest_ack_ = highest_ack_;
			//printf("\n\n[2][%g]\n", this->tcpMaster->getNow().second());
			tcp_cong_avoid(ack, seq_rtt, prior_in_flight, 1);
		}
	}

	if (linux_.icsk_ca_ops) {
		//LOG_DEBUG("\t sndRecv 21 \n")
		save_from_linux();
		paramManager.restore_default();
	}

	LOG_DEBUG("[" << this->sessionNumber << "] at [" << this->tcpMaster->getNow().second() << "] *** SEND_MUCH 1 ***" << endl);

	if(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
			state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT) {
		return;
	} else {
		sendMuch(0, 0, maxBurst);
	}
}

//now - ops_en_->ts_echo, 0 or ACK of pkt just received!
//time between 2 consecutive sends (SERVER),
void TCPSession::rttUpdate(double tao, unsigned long pkt_seq_no) {
	double now = tcpMaster->getNow().millisecond();

	if (ts_enabled) {
		t_rtt_ = int(tao + 0.5);
	} else {
		double sendtime = now - tao;
		sendtime += boot_time_;
		double tickoff = fmod(sendtime, tcp_tick_);
		t_rtt_ = int((tao + tickoff) / tcp_tick_);
	}

	// Record microsecond timestamp
	if ((linux_.icsk_ca_ops) && (linux_.icsk_ca_ops->rtt_sample)) {
		if (bugfix_ts_ && pkt_seq_no && tss_) {
			//if we use the tss value when we have the timestamp
			//unsigned long a = (unsigned long)(round((now-tss[pkt_seq_no % tss_size_])*US_RATIO));
			//unsigned long ha = highest_ack_;
			//if (a < 100)
			linux_.icsk_ca_ops->rtt_sample(&linux_, (unsigned long) (round((now
					- tss_[pkt_seq_no % tss_size_]) * US_RATIO)));
		} else {
			//otherwise, use an approximation
			linux_.icsk_ca_ops->rtt_sample(&linux_, (unsigned long) (round(tao
					* US_RATIO)));
		}
	}

	if (t_rtt_ < 1) {
		t_rtt_ = 1;
	}
	// t_srtt_ has 3 bits to the right of the binary point
	// t_rttvar_ has 2
	// Thus "t_srtt_ >> T_SRTT_BITS" is the actual srtt,
	//   and "t_srtt_" is 8*srtt.
	// Similarly, "t_rttvar_ >> T_RTTVAR_BITS" is the actual rttvar,
	//   and "t_rttvar_" is 4*rttvar.
	if (t_srtt_ != 0) {
		register short delta;
		delta = t_rtt_ - (t_srtt_ >> T_SRTT_BITS); // d = (m - a0)
		if ((t_srtt_ += delta) <= 0) // a1 = 7/8 a0 + 1/8 m
			t_srtt_ = 1;
		if (delta < 0) {
			delta = -delta;
			delta -= (t_rttvar_ >> T_RTTVAR_BITS);
			if (delta > 0)
				delta >>= T_SRTT_BITS;
		} else
			delta -= (t_rttvar_ >> T_RTTVAR_BITS);
		t_rttvar_ += delta;
		//		if ((t_rttvar_ += delta) <= 0)	// var1 = 3/4 var0 + 1/4 |d|
		//			t_rttvar_ = 1;
	} else {
		t_srtt_ = t_rtt_ << T_SRTT_BITS; // srtt = rtt
		t_rttvar_ = t_rtt_ << (T_RTTVAR_BITS - 1); // rttvar = rtt / 2
	}
	//
	// Current retransmit value is
	//    (unscaled) smoothed round trip estimate
	//    plus 2^rttvar_exp_ times (unscaled) rttvar.
	//
	t_rtxcur_ = (((t_rttvar_ << (rttvar_exp_ + (T_SRTT_BITS - T_RTTVAR_BITS)))
			+ t_srtt_) >> T_SRTT_BITS) * tcp_tick_;
	linux_.srtt = t_srtt_;
	return;
}

//header, flag,a variable to change
void TCPSession::timeProcessing(TCPMessage* tcph, unsigned char flag,
		s32* seq_urtt_p, struct OptionsEnabled* ops_en_)
{

#define renew_timer { if ((int)t_seqno_ > highest_ack_ ||    \
		highest_ack_ < maxseq_ || linux_.snd_cwnd < 1)  \
		setRtxTimer(); else cancelRtxTimer(); }

	if (!timerfix_) {
		renew_timer;
	}

	//update time:
	double now = tcpMaster->getNow().millisecond();

	//double tao;
	//if (tcph->getOptions() && ops_en_->ts_value > 0 && ops_en_->ts_echo > 0) {
	if (tcph->getOptions() && ops_en_->ts_value > 0) {
		ts_peer_ = ops_en_->ts_value; //record timestamp for echoing
	}

	//Update RTT only if it's OK to do so from info in the flags header.
	//This is needed for protocols in which intermediate agents
	//in the network intersperse acks (e.g., ack-reconstructors) for
	//various reasons (without violating e2e semantics).

	//TCPMessage* fh = tcph;
	linux_.rx_opt.saw_tstamp = 0;

	if (ts_enabled == 1) {
		//if (ts_option_) {
		ts_echo_ = ops_en_->ts_echo;
		linux_.rx_opt.saw_tstamp = 1;
		//Here we do not multiply by JIFFY_RATIO because the time is already in miliseconds
		linux_.rx_opt.rcv_tsecr = (__u32) (round(ts_echo_));
		linux_.rx_opt.rcv_tsval = (__u32) (round(ops_en_->ts_value
				* JIFFY_RATIO));
		if (flag & FLAG_UNSURE_TSTAMP) {
			// we are not sure which packet generates this ack
			/*if (now < ops_en_->ts_echo)
				printf(
						"	***before calling rttUpdate 1: now=%g ops_en_->ts_echo=%g\n",
						now, ops_en_->ts_echo);*/
			rttUpdate(now - ops_en_->ts_echo, 0);
		} else {
			//we are sure this packet generates this ack
			//ORIGINALrtt_update(now - tcph->ts_echo(), tcph->seqno());
			rttUpdate(now - ops_en_->ts_echo, sndWnd->acknoToPktno(tcph->getAckno()));
		}
		(*seq_urtt_p) = (s32) (round((now - ops_en_->ts_echo) * JIFFY_RATIO));
		//(*seq_usrtt_p) = (s32)(round((now - tcph->ts_echo())*US_RATIO));
		if (ts_resetRTO_) {
			t_backoff_ = 1;
		}
		//}
	}

	if (rtt_active_ && (int) sndWnd->acknoToPktno(tcph->getAckno()) >= rtt_seq_) {
		rtt_active_ = 0;
		if (!linux_.rx_opt.saw_tstamp){
			rttUpdate(now - rtt_ts_);
		}
	}

	if (timerfix_){
		renew_timer;
	}
	// update average window
	awnd_ *= 1.0 - wnd_th_;
	awnd_ += wnd_th_ * cwnd_;
}

double TCPSession::increaseParam()
{
	double increase, decrease, p, answer;
	/* extending the slow-start for high-speed TCP */

	/* for highspeed TCP -- from Sylvia Ratnasamy, */
	/* modifications by Sally Floyd and Evandro de Souza */
	// p ranges from 1.5/W^2 at congestion window low_window_, to
	//    high_p_ at congestion window high_window_, on a log-log scale.
	// The decrease factor ranges from 0.5 to high_decrease
	//  as the window ranges from low_window to high_window,
	//  as the log of the window.
	// For an efficient implementation, this would just be looked up
	//   in a table, with the increase and decrease being a function of the
	//   congestion window.

	if (cwnd_ <= low_window_) {
		answer = 1 / cwnd_;
		return answer;
	} else if (cwnd_ >= hstcp_.cwnd_last_ && cwnd_ < hstcp_.cwnd_last_
			+ cwnd_range_) {
		// cwnd_range_ can be set to 0 to be disabled,
		//  or can be set from 1 to 100
		answer = hstcp_.increase_last_ / cwnd_;
		return answer;
	} else {
		// OLD:
		// p = exp(linear(log(cwnd_), log(low_window_), log(hstcp_.low_p), log(hig$
		// NEW, but equivalent:
		p = exp(hstcp_.p1 + log(cwnd_) * hstcp_.p2);
		decrease = decreaseParam();
		// OLD:
		// increase = cwnd_*cwnd_*p *(2.0*decrease)/(2.0 - decrease);
		// NEW, but equivalent:
		increase = cwnd_ * cwnd_ * p / (1 / decrease - 0.5);
		//      if (increase > max_increase) {
		//              increase = max_increase;
		//      }
		answer = increase / cwnd_;
		hstcp_.cwnd_last_ = cwnd_;
		hstcp_.increase_last_ = increase;
		return answer;
	}
}

/*
 * Calculating the decrease parameter for highspeed TCP.
 */
double TCPSession::decreaseParam() {
	double decrease;
	// OLD:
	// decrease = linear(log(cwnd_), log(low_window_), 0.5, log(high_window_), high_de$
	// NEW (but equivalent):
	decrease = hstcp_.dec1 + log(cwnd_) * hstcp_.dec2;
	return decrease;
}

void TCPSession::touchCwnd() {
	linux_.snd_cwnd_stamp = tcp_time_stamp; /* we touch the congestion window in this function */
}

void TCPSession::tcpModerateCwnd() {
	linux_.snd_cwnd = min((int) linux_.snd_cwnd, packetsInFlight()
			+ tcp_max_burst); //max
	touchCwnd();
}

void TCPSession::openCwnd()
{
	double increment;
	
	LOG_DEBUG("entering opencwnd...");

	if (cwnd_ < ssthresh_) {
		/* slow-start (exponential) */
		cwnd_ += 1;
		//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
	} else {
		/* linear */
		double f;
		switch (wnd_option_) {
		case 0:
			if (++count_ >= cwnd_) {
				count_ = 0;
				++cwnd_;
				//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
			}
			break;

		case 1:
			/* This is the standard algorithm. */
			increment = increaseNum / cwnd_;
			if ((last_cwnd_action_ == 0 || last_cwnd_action_
					== CWND_ACTION_TIMEOUT) && max_ssthresh_ > 0) {
				increment = limitedSlowStart(cwnd_, max_ssthresh_, increment);
			}
			cwnd_ += increment;
			//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ <<
					//" increment=" << increment << " max_ssthresh_" << max_ssthresh_ << "\n");
			break;

		case 2:
			/* These are window increase algorithms
			 * for experimental purposes only. */
			/* This is the Constant-Rate increase algorithm
			 *  from the 1991 paper by S. Floyd on "Connections
			 *  with Multiple Congested Gateways".
			 *  The window is increased by roughly
			 *  wnd_const_*RTT^2 packets per round-trip time.  */
			f = (t_srtt_ >> T_SRTT_BITS) * tcp_tick_;
			f *= f;
			f *= wnd_const_;
			/* f = wnd_const_ * RTT^2 */
			f += fcnt_;
			if (f > cwnd_) {
				fcnt_ = 0;
				++cwnd_;
				//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
			} else
				fcnt_ = f;
			break;
		case 3:
			/* The window is increased by roughly
			 *  awnd_^2 * wnd_const_ packets per RTT,
			 *  for awnd_ the average congestion window. */
			f = awnd_;
			f *= f;
			f *= wnd_const_;
			f += fcnt_;
			if (f > cwnd_) {
				fcnt_ = 0;
				++cwnd_;
				//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
			} else
				fcnt_ = f;
			break;

		case 4:
			/* The window is increased by roughly
			 *  awnd_ * wnd_const_ packets per RTT,
			 *  for awnd_ the average congestion window. */
			f = awnd_;
			f *= wnd_const_;
			f += fcnt_;
			if (f > cwnd_) {
				fcnt_ = 0;
				++cwnd_;
				//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
			} else
				fcnt_ = f;
			break;
		case 5:
			/* The window is increased by roughly wnd_const_*RTT
			 *  packets per round-trip time, as discussed in
			 *  the 1992 paper by S. Floyd on "On Traffic
			 *  Phase Effects in Packet-Switched Gateways". */
			f = (t_srtt_ >> T_SRTT_BITS) * tcp_tick_;
			f *= wnd_const_;
			f += fcnt_;
			if (f > cwnd_) {
				fcnt_ = 0;
				++cwnd_;
				//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
			} else
				fcnt_ = f;
			break;
		case 6:
			/* binomial controls */
			cwnd_ += increaseNum / (cwnd_ * pow(cwnd_, k_parameter_));
			//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
			break;
		case 8:
			/* high-speed TCP, RFC 3649 */
			increment = increaseParam();
			if ((last_cwnd_action_ == 0 || last_cwnd_action_
					== CWND_ACTION_TIMEOUT) && max_ssthresh_ > 0) {
				increment = limitedSlowStart(cwnd_, max_ssthresh_, increment);
			}
			cwnd_ += increment;
			//LOG_DEBUG("\t\t\t\tcwnd_ increased from " << localCwnd_ << " to" << cwnd_ << "\n");
			break;
		default:
			abort();
		}
	}

	// if maxcwnd_ is set (nonzero), make it the cwnd limit

	if (maxcwnd_ && (int(cwnd_) > maxcwnd_)){
		cwnd_ = maxcwnd_;
	}
	
	return;
}

unsigned char TCPSession::ackProcessing(TCPMessage* tcph, unsigned char flag) {
	//update sequence numbers
	if (flag & FLAG_DATA_ACKED) {
		//LOG_DEBUG("Entering ACK processing" << endl);
		//get the ack in packets
		//LOG_DEBUG("\tbefore highest_ack_=" << highest_ack_ << "\n");

		LOG_DEBUG("in processack, [ackProcessing] translating " << tcph->getAckno() << " to" << sndWnd->acknoToPktno(tcph->getAckno())
			<< endl); 
		highest_ack_ = sndWnd->acknoToPktno(tcph->getAckno()) - 1;

		//LOG_DEBUG("\tafter highest_ack_=" << highest_ack_ << "\n");
		linux_.snd_una = (highest_ack_ + 1) * linux_.mss_cache;
		maxseq_ = max(maxseq_, highest_ack_);

		if ((int)t_seqno_ < highest_ack_ + 1) {
			t_seqno_ = highest_ack_ + 1;
			linux_.snd_nxt = t_seqno_ * linux_.mss_cache;
		}
		this->sndWnd->cleanWindow(tcph->getAckno());
		return flag;
	} else {
		//LOG_DEBUG("\tskippedhighest_ack_=" << highest_ack_ << "\n");
		return flag | FLAG_UNSURE_TSTAMP;
	}
	//LOG_DEBUG("\tskipped highest_ack_=" << highest_ack_ << "\n");
}

int TCPSession::packetsInFlight() {
	return (scb_->packets_in_flight(highest_ack_ + 1, t_seqno_));
}

bool TCPSession::isCongestion() {
	return (packetsInFlight() >= (int)linux_.snd_cwnd);
}

void TCPSession::getOptions(int& options, struct OptionsEnabled* opt_en,
		TCPMessage* tcphdr) {
	// Traverse the whole options field to search for options conveyed
	int offset = 0;

	//LOG_DEBUG("getOptions" << endl);

	//Initialize structure
	opt_en->echo = 0;
	opt_en->echo_reply = 0;
	opt_en->mss = 0;
	opt_en->sack = NULL;
	opt_en->sack_length = 0;
	opt_en->sack_permitted = false;
	opt_en->ts_echo = 0;
	opt_en->ts_value = 0;
	opt_en->wsopt = 0;

	// Execute this block if indeed the packet has the options field
	if (tcphdr->getOptions()) {
		LOG_DEBUG("\t\t*** size=" << tcphdr->size() << " tcphdr->getLength()=" << tcphdr->getLength()
				<< " SSFNET_TCPHDR_LENGTH " << SSFNET_TCPHDR_LENGTH << "\n");
		while (offset < (tcphdr->getLength() - SSFNET_TCPHDR_LENGTH)) {
			//LOG_DEBUG("READING BYTE OF OPTIONS=" << (int)(tcphdr->getOptions()[offset]) << "\n");
			switch ((int)(tcphdr->getOptions()[offset])) {
			case 0:{
				// End of option list
				offset++;
				break;
			}
			case 1:
				//No operation
				offset++;
				break;
			case 2: {
				//MSS
				//LOG_DEBUG("mss=" << (tcphdr->getOptions()[offset + 2] << 8) +
					//	(tcphdr->getOptions()[offset + 3]) << endl);
				opt_en->mss = (tcphdr->getOptions()[offset + 2] << 8) +
						(tcphdr->getOptions()[offset + 3]);
				//LOG_DEBUG("mss=" << opt_en->mss << "\n");
				options |= MSS_OPT;
				offset += 4;
				break;
			}
			case 3:
				//LOG_DEBUG("\t\t\t\t***-----------***\n");
				options |= WSOPT_OPT;
				offset += 3;
				break;
			case 4:
				//SACK permitted
				opt_en->sack_permitted = true;
				//LOG_DEBUG("SACK PERMITTED OPTED" << endl);
				options |= SACK_PERMITTED_OPT;
				offset += 2;
				break;
			case 5: {
				//SACK (for server side)
				//LOG_DEBUG("SACK OPTIONS ENABLED" << endl);
				int array_index = 0;
				int starting_byte = offset + 2;

				options |= SACK_OPT;
				// Get the length of the SACK block, because it is variable
				opt_en->sack_length = (tcphdr->getOptions()[offset + 1] - 2) / 4;
				opt_en->sack = new int[opt_en->sack_length];

				//LOG_DEBUG("\nStarting byte=" <<  starting_byte << endl);

				while (array_index < opt_en->sack_length) {
					// Populate the newly created array
					opt_en->sack[array_index] = (tcphdr->getOptions()[starting_byte]
							<< 24) + (tcphdr->getOptions()[starting_byte + 1] << 16)
							+ (tcphdr->getOptions()[starting_byte + 2] << 8)
							+ (tcphdr->getOptions()[starting_byte + 3]);

					opt_en->sack[array_index] = this->sndWnd->acknoToPktno(opt_en->sack[array_index]);

					//LOG_DEBUG("seqno=%d pktno=%d" << seqno << opt_en->sack[array_index]);
					array_index++;
					starting_byte += 4;
				}
				offset += tcphdr->getOptions()[offset + 1];
				break;
			}
			case 6:
				// Echo //Obsolete
				options |= ECHO_OPT;
				offset += 6;
				break;
			case 7:
				// Echo reply //obsolete
				options |= ECHO_REPLY_OPT;
				offset += 6;
				break;
			case 8:
				// TSOPT
				options |= TSOPT_OPT;
				// Get the timestamp
				opt_en->ts_value = (tcphdr->getOptions()[offset + 2] << 24)
						+ (tcphdr->getOptions()[offset + 3] << 16)
						+ (tcphdr->getOptions()[offset + 4] << 8)
						+ tcphdr->getOptions()[offset + 5];
				opt_en->ts_echo = (tcphdr->getOptions()[offset + 6] << 24)
						+ (tcphdr->getOptions()[offset + 7] << 16)
						+ (tcphdr->getOptions()[offset + 8] << 8)
						+ tcphdr->getOptions()[offset + 9];
				offset += 10;

				break;
			case 9:
				//Partial order connection permitted
				options |= PART_ORD_CONN_PERM_OPT;
				offset += 2;
				break;
			case 10:
				//Partial oder service profile
				options |= PART_ORD_SERV_PROF_OPT;
				offset += 3;
				break;
			case 11:
				//CC, connetion count
				options |= CC_CONN_COUNT;
				offset += 6;
				break;
			case 12:
				//CC.NEW
				options |= CC_NEW;
				offset += 6;
				break;
			case 13:
				//CC.ECHO
				options |= CC_ECHO;
				offset += 6;
				break;
			case 14:
				//Alternate checksum request
				options |= ALT_CHECKSUM_REQ_OPT;
				offset += 3;
				break;
			case 15:
				//Alternate checksum data
				options |= ALT_CHECKSUM_DATA_OPT;
				offset += tcphdr->getOptions()[offset + 1];
				break;
			default:
				offset += tcphdr->getOptions()[offset + 1];
				//offset ++;
				break;
			}
		}
	}
}

void TCPSession::initStateListen()
{
	assert(state == TCP_STATE_CLOSED);

	// set the session to listen
	state = TCP_STATE_LISTEN;
	return;
}

int TCPSession::initStateClosing()
{
	state = TCP_STATE_CLOSING;
	//XXX Check rcvwnd and signal SOCK_EOF
	return 0;
}

int TCPSession::init_state_closed()
{
	assert(state == TCP_STATE_CLOSED);
	t_seqno_++;
	highest_ack_ = 0;
	return 1;
}

int TCPSession::initStateSynReceived(TCPMessage* tcphdr)
{
	assert(state != TCP_STATE_CLOSED);
	bool myListen = (state == TCP_STATE_LISTEN);
	struct OptionsEnabled opt_en;
	int options = 0;

	state = TCP_STATE_SYN_RECEIVED;

	//get Options
	getOptions(options, &opt_en, tcphdr);
	cached_options = options;
	cached_opts_en = opt_en;

	//LOG_DEBUG("\t\t OPTIONS GOT:" << options << "\n");

	if (options & TSOPT_OPT) {
		ts_enabled = 1;
		ts_peer_ = opt_en.ts_value;
	}

	//if my session was listening before getting SYN
	if (myListen) {
		if (changedContainer == false) {
			changedContainer = true;
			//LOG_DEBUG("calling socketbusy\n" << endl);
			tcpSock->socketBusy();
			tcpMaster->changeToConnected(this);
		}

		LOG_DEBUG("debug[" << this->getMaster()->inHost()->getNow().second() << "] send_data 2" <<
						" srcport=" << this->getTcpConn()->srcPort << " dstport=" << this->getTcpConn()->dstPort <<
						" srcip=" << this->getTcpConn()->srcIP << " dstip=" << this->getTcpConn()->dstIP <<
						" myip=" << this->getMaster()->inHost()->getDefaultIP() << endl);

		//***
		if(open_session_enabled == false){
			seqnoForOpening = this->lastSeqno;
			acknoForOpening = tcphdr->getSeqno() + 1;//supposing the FIN packet carries 1 byes of payload
			open_session_enabled = true;
			LOG_DEBUG("correct opening 3" << endl);
		} else {
			LOG_WARN("duplicate syn" << endl);
		}
		//***
		sendData(sndWnd->start(), 0, TCPMessage::TCP_FLAG_SYN
				| TCPMessage::TCP_FLAG_ACK, sink->rcvWnd->Seqno(), true, true,
				options, &opt_en);

		//***
		open_session_interval = 2*this->getMaster()->getSlowTimeout() + 0.1; //at least we will wait one slow timeout interval
		//***

		t_seqno_++;
	} else {
		LOG_ERROR("THIS SESSION SHOULD BE LISTENING, ERROR IN CODE\n");
	}
	return 0;
}

int TCPSession::initStateEstablished()
{
	state = TCP_STATE_ESTABLISHED;

	// disable the rtx mechanism for 3-way handshake
	open_session_enabled = false;

	return 0;
}

int TCPSession::initStateCloseWait(TCPMessage* tcphdr)
{
	state = TCP_STATE_CLOSE_WAIT;

	if(close_session_enabled == false){
		seqnoForClosing = this->lastSeqno;
		acknoForClosing = tcphdr->getSeqno() + 1;//supposing the FIN packet carries 1 byes of payload
		close_session_enabled = true;
		LOG_DEBUG("correct opening" << endl);
	} else {
		LOG_DEBUG("duplicate fin" << endl);
	}

	sendFinScheduleTimer(seqnoForClosing, acknoForClosing);

	state = TCP_STATE_LAST_ACK;

	LOG_DEBUG("[closing sessions] [" << this->getMaster()->inHost()->getNow().second() <<
			"] [" << this->getMaster()->getUID() << "] ***STATE=TCP_STATE_CLOSE_WAIT " <<
			" srcport=" << this->getTcpConn()->srcPort << " dstport=" << this->getTcpConn()->dstPort <<
			" srcip=" << this->getTcpConn()->srcIP << " dstip=" << this->getTcpConn()->dstIP <<
			" myip=" << this->getMaster()->inHost()->getDefaultIP() << endl);

	conn_termination_timer_->resched(2);

	return 0;
}

int TCPSession::initStateClosed()
{
	//servers
	state = TCP_STATE_CLOSED;

	LOG_DEBUG("[debug erase] [" << this->getMaster()->inHost()->getNow() << "] 	" <<
			"closing session srcport=" << this->getTcpConn()->srcPort << " dstport=" << this->getTcpConn()->dstPort <<
			" srcip=" << this->getTcpConn()->srcIP << " dstip=" << this->getTcpConn()->dstIP <<
			" myip=" << this->getMaster()->inHost()->getDefaultIP() << endl);

	close_session_enabled = false;
	//this->tcpMaster->eraseSession(this);

	return 0;
}

int TCPSession::initStateTimeWait(TCPMessage* tcphdr)
{
	LOG_DEBUG("[closing sessions] [" << this->getMaster()->inHost()->getNow().second() <<
	        "] [" << this->getMaster()->getUID() << "] ***STATE=TCP_STATE_TIME_WAIT" << endl);
	//Here we changed from FIN-WAIT-1 to TIME_WAIT DIRECTLY
	state = TCP_STATE_TIME_WAIT;
	// Launching a timer with 2MSL
	LOG_DEBUG("MSL: launching timer 2 with 2MSL at" << getMaster()->inHost()->getNow().second()
			<< endl);

	sendFinScheduleTimer(seqnoForClosing, acknoForClosing);
	LOG_DEBUG("close_session_interval set to " << close_session_interval << endl);

	//XXX real value for delay: conn_termination_timer_->sched(2*TCP_DEFAULT_MSL);
	conn_termination_timer_->resched(2);
	return 0;
}

void TCPSession::sendBytes(int bytes_)
{
	LOG_DEBUG("TCP got" << bytes_ << " to send\n");
}

void TCPSession::sendFinScheduleTimer(uint32 seqno, uint32 ackno, uint32 length, byte* msg)
{
	LOG_DEBUG("[closing sessions] [" << this->getMaster()->inHost()->getNow().second() <<
			"] [" << this->getMaster()->getUID() << "] seqno to send for FIN is:" <<
			seqno << " curSeq=" << curSeq << endl);

	sendFin(seqno, ackno, length, msg);
	//set this if the FIN packet gets lost again
	close_session_interval = 5*this->getMaster()->getSlowTimeout() + 0.1; //at least we will wait one slow timeout interval
}

void TCPSession::sendSynScheduleTimer(uint32 seqno, uint32 ackno)
{
	//use current seqno and ackno for retransmissions
	sendSyn(seqno, ackno);
	//set this if the FIN packet gets lost again
	open_session_interval = 10*this->getMaster()->getSlowTimeout() + 0.1; //at least we will wait one slow timeout interval
}

void TCPSession::sendSyn(uint32 seqno, uint32 ackno)
{
	//send SYN packet
	byte mask = 0;
	int option_len = 0;
	byte* options = NULL;
	//int flags_ = 0;
	int pos = 0;

	//preparing the mask
	mask = mask | TCPMessage::TCP_FLAG_SYN;

	/*
	 * constructing the options
	 */
	// Options used by most TCP implementations in Linux
	/*flags_ |= MSS_OPT;
	flags_ |= SACK_PERMITTED_OPT;
	flags_ |= TSOPT_OPT;
	flags_ |= WSOPT_OPT;*/

	//The header is going to be enlarged according to the options enabled
	option_len = 4 + 2 + 10 + 4; //MSS , SACK, TIMESTAMPS, WINDOW SCALE

	int old_option_len = option_len;
	if (option_len % 4)
		option_len = ((int) (option_len / 4)) * 4 + 4;
	options = new byte[option_len];

	// For MSS option
	options[pos++] = 2;
	options[pos++] = 4;
	options[pos++] = (tcpMaster->getMSS() >> 8) & 0xFF;
	options[pos++] = tcpMaster->getMSS() & 0xFF;
	// For SACK PERMITTED option
	options[pos++] = 4;
	options[pos++] = 2;
	// For TSOPT option
	double now = tcpMaster->getNow().millisecond();
	int nowInt = (int)now;
	options[pos++] = 8;
	options[pos++] = 10;
	options[pos++] = (nowInt & 0xFF000000) >> 24;
	options[pos++] = (nowInt & 0x00FF0000) >> 16;
	options[pos++] = (nowInt & 0x0000FF00) >> 8;
	options[pos++] = nowInt & 0x000000FF;
	options[pos++] = (0 & 0xFF000000) >> 24;
	options[pos++] = (0 & 0x00FF0000) >> 16;
	options[pos++] = (0 & 0x0000FF00) >> 8;
	options[pos++] = 0 & 0x000000FF;
	// For WSOPT option
	options[pos++] = 1;
	options[pos++] = 3;
	options[pos++] = 3;
	options[pos++] = 7;

	

	// Pad to a full word 
	for (int i = old_option_len + 1; i <= option_len; i++) {
		options[pos] = 1;
		pos++;
	}

	peer_wnd_scalable = false;

	//LOG_DEBUG("\nIN SEND DATA: sending pkt with seqno =" << seqno << endl);
	TCPMessage* tcp_msg = new TCPMessage(tcpConn->srcPort,
			tcpConn->dstPort, seqno, ackno, mask, this->calcAdvertisedWnd(),
			(SSFNET_TCPHDR_LENGTH + option_len), option_len, options);

	if(seqno > lastSeqno){
			lastSeqno = seqno;
	}
	if(ackno > lastAck){
		lastAck = ackno;
	}

	IPPushOption ops(tcpConn->srcIP, tcpConn->dstIP,
			SSFNET_PROTOCOL_TYPE_TCP, 0);

	//LOG_DEBUG("in processack, [sendSyn] sending syn packet with seqno=" << tcp_msg->getSeqno() << endl);
	tcpMaster->push(tcp_msg, NULL, (void*) &ops, sizeof(IPPushOption));
}

void TCPSession::sendFin(uint32 seqno, uint32 ackno, uint32 length, byte* msg){
	// Sending a packet with FIN flag
	byte mask = 0;
	int options = 0;

	// Preparing the options
	options |= TSOPT_OPT;
	//preparing the mask
	mask = mask | TCPMessage::TCP_FLAG_FIN;
	mask = mask | TCPMessage::TCP_FLAG_ACK;

	sendData(seqno, tcpMaster->getMSS(), mask, ackno, true, true, options, NULL, length, msg);
}

void TCPSession::sendFirst()
{
	if (state == TCP_STATE_CLOSED || state == TCP_STATE_SYN_SENT) { //< we rtx a syn when state == TCP_STATE_SYN_SENT
		seqnoForOpening = tcpMaster->getISS();
		acknoForOpening = 0;

		sendSynScheduleTimer(seqnoForOpening, acknoForClosing);

		state = TCP_STATE_SYN_SENT;
		open_session_enabled = true;
		//LOG_DEBUG("this packet has payload? " << tcp_msg->hasPayload() << endl);
	} else {
		//LOG_DEBUG("Send bytes without making the handshake again" << endl);

		if(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
				state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT) {
			return;
		} else {
			sendMuch(0, 0, maxBurst);
		}
	}
}

void TCPSession::sendMsg(uint64_t length, byte* msg, bool send_data_and_disconnect)
{
	int size_ = tcpMaster->getMSS();

	if((length == 0 && msg == 0) || (send_data_and_disconnect == true)){
		state = TCP_STATE_FIN_WAIT_1;

		bool found = false;
		seqnoForClosing = this->sndWnd->pktnoToSeqno(curSeq, found);
		acknoForClosing = sink->rcvWnd->Seqno();
		sendFinScheduleTimer(seqnoForClosing, acknoForClosing, length, msg);
		close_session_enabled = true;
		LOG_DEBUG("close_session_interval set to " << close_session_interval << endl);
		return;
	}

	if (msg) {
		//LOG_DEBUG("\tmsg sent to TCP is NOT NULL!\n");
		//byte* msgcpy = new byte[length + 1];
		//assert(msgcpy);
		//memcpy(msgcpy, msg, length);
		//delete[] msg;

		//Insert DataChunk into the linked list
		if (sndWnd->requestToSend(msg, length)) {
		//if (sndWnd->requestToSend(msgcpy, length)) {
			curSeq += (length / size_ + (length % size_ ? 1 : 0));
			LOG_DEBUG("in processack, [sendMsg] curSeq=" << curSeq << endl);
			//LOG_DEBUG("\tafter requesting to send length=" << length << " curSeq=" << curSeq << "\n");
		}
	} else {
		//LOG_DEBUG("\tmsg sent to TCP is NULL!\n");
		if (sndWnd->requestToSend(0, length)) {
			curSeq += (length / size_ + (length % size_ ? 1 : 0));
		}
	}

	LOG_DEBUG("push: [" << this->tcpMaster->inHost()->getDefaultIP() << "] [" << this->tcpMaster->getNow().second() <<
			"] CURSEQ EMU=" << curSeq << endl);

	sendFirst();

	LOG_DEBUG("[debug longdelays] [" << this->getMaster()->getUID() << "] [ " << this->getMaster()->inHost()->getNow().second() << " ], starting sendmsg:" <<
			" srcport=" << this->tcpConn->srcPort << " dstport=" << this->tcpConn->dstPort << " length=" << length <<
			" msg=" << msg <<
			" curSeq=" << curSeq << endl);

	return;
}

void TCPSession::send(uint64_t bytes)
{
	//Sending packets to a simulated entity
	LOG_DEBUG("[debug conversion][" << this->getMaster()->inHost()->getUID() << "][" <<
			this->getMaster()->inHost()->getNow().second() << "] starting send with length=" << bytes << endl);

	uint32_t size = (uint32_t)tcpMaster->getMSS();
	uint32_t div = 0;

	//Insert DataChunk into the linked list
	//LOG_DEBUG("	before requesting to send length=" << length << " size=" << size_ << "\n");
	if (sndWnd->requestToSend(NULL, bytes)) {
		div = bytes / size;
		curSeq += div;
		curSeq += (bytes % size) ? 1 : 0;
		//LOG_DEBUG("\tafter requesting to send length=" << length << " curSeq=" << curSeq << "\n");
	}

	LOG_DEBUG("[debug longdelays] [ " << this->getMaster()->inHost()->getNow().second() << " ], starting send:" <<
			" srcport=" << this->tcpConn->srcPort << " dstport=" << this->tcpConn->dstPort <<
			" bytes=" << bytes << " curSeq=" << curSeq << endl);
	
	sendFirst();
}

void TCPSession::sendMuch(int force, int reason, int maxburst)
{
	if(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
			state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT || state == TCP_STATE_CLOSED) {
		//Do not make more computation because we are closing the session...
		return;
	}

	int found = 0; int npacket = 0; int xmit_seqno = 0; int win = 0;
	send_idle_helper();
	
	//LOG_DEBUG("CURSEQ in sendmuch:" << curSeq << endl);

	// Get the value of congestion window from Linux
	win = window();
	// We suppose we found a packet to send beforehand
	found = 1;

	// As long as the pipe is open and there is app data to send...
	nextPktsInFlight = min(nextPktsInFlight, packetsInFlight() + 1);

	LOG_DEBUG("[" << sessionNumber << "] [" << this->tcpMaster->getNow().second() <<
			"] sendmuch before while loop, packetsInFlight()=" <<
			packetsInFlight() << " max(win, nextPktsInFlight)=" << max(win, nextPktsInFlight) << " t_seqno_" << t_seqno_ <<
			" cwnd_=" << cwnd_ << " curSeq" << curSeq << endl);

	LOG_DEBUG("in processack, [sendmuch] curSeq=" << curSeq << endl);	
	while (packetsInFlight() < max(win, nextPktsInFlight)) {
		//LOG_DEBUG("Entering WHILE in sendMuch... IP=" << (this->tcpMaster->inHost()->getDefaultIP()) << endl);
		
		if (overhead_ == 0 || force) {
			found = 0;
			//scb_ relates seqno to send to acks Rx
			xmit_seqno = scb_->GetNextRetran();

			if (xmit_seqno == -1) { // no retransmissions to send
				//LOG_DEBUG("\tNO RETRANSMISSIONS!!!\n" << endl);
				//if there is no more application data to send do nothing

				if (t_seqno_ > curSeq) {
					LOG_DEBUG("\tAll packets have been sent? t_seqno_=" << t_seqno_<< "  curSeq=" <<
							curSeq << "\n");
					return;
				}

				found = 1;
				//LOG_DEBUG("IN SEND MUCH t_seqno_=" << (int)t_seqno_ << endl);
				xmit_seqno = t_seqno_++;
			} else {
				found = 1;
				scb_->MarkRetran(xmit_seqno, t_seqno_);
				win = window();
			}

			if (found) {
				LOG_DEBUG("debug[" << this->getMaster()->inHost()->getNow().second() << "] ***got a packet to send" <<
						" srcport=" << this->getTcpConn()->srcPort << " dstport=" << this->getTcpConn()->dstPort <<
						" srcip=" << this->getTcpConn()->srcIP << " dstip=" << this->getTcpConn()->dstIP <<
						" myip=" << this->getMaster()->inHost()->getDefaultIP() << " xmit_seqno=" <<
						xmit_seqno << endl);

				// A packet to send was found, send it!
				output(xmit_seqno, reason);
				//LOG_DEBUG("in SEND_MUCH: SEQNO to transmit " << xmit_seqno << endl);

				nextPktsInFlight = min(nextPktsInFlight, max(packetsInFlight() - 1, 1));

				if ((int)t_seqno_ <= xmit_seqno) {
					t_seqno_ = xmit_seqno + 1;
				}

				linux_.snd_nxt = t_seqno_ * linux_.mss_cache;
				npacket++;
			} else {
				// Nothing to do by now
			}
		}

		if (maxburst && npacket >= maxburst){
			break;
		}
	}

	// call helper function
	send_helper(maxburst);
}

void TCPSession::output(int seqno, int reason)
{
	int force_set_rtx_timer = 0;

	LOG_DEBUG("[debug seqno] output method with seqno=" << seqno << endl);
	
	uint32 seqNoBytes = 0;

	if (tss_ == 0) {
		tss_ = (double*) calloc(tss_size_, sizeof(double));
		if (tss_ == 0)
			exit(1);
	}

	// Dynamically grow the timestamp array if it's getting full
	if (bugfix_ts_ && ((seqno - highest_ack_) > tss_size_ * 0.9)) {
		double *ntss;
		ntss = (double*) calloc(tss_size_ * 2, sizeof(double));
		if (ntss == NULL)
			exit(1);
		for (int i = 0; i < tss_size_; i++)
			ntss[(highest_ack_ + i) % (tss_size_ * 2)] = tss_[(highest_ack_ + i)	% tss_size_];
		free(tss_);
		tss_size_ *= 2;
		tss_ = ntss;
	}

	if (highest_ack_ == maxseq_)
		force_set_rtx_timer = 1;

	ndatapack_++;

	bool found = false;
	seqNoBytes = sndWnd->pktnoToSeqno(seqno, found);
	LOG_DEBUG("in processack, [output] translating " << seqno << " to" << seqNoBytes << " found=" << found << endl);
	if(found == false)
		return;

	int options = 0;
	if (ts_enabled == 1) {
		options |= TSOPT_OPT;
	}

	//LOG_DEBUG("\tcalling senddata with sink->rcvWnd->Seqno()=" << sink->rcvWnd->Seqno() <<
		//		" tcpMaster->getMSS()=" << 1448 << " seqNoBytes=" << seqNoBytes << "\n");

	LOG_DEBUG("debug[" << this->getMaster()->inHost()->getNow().second() << "] send_data 4" <<
			" srcport=" << this->getTcpConn()->srcPort << " dstport=" << this->getTcpConn()->dstPort <<
			" srcip=" << this->getTcpConn()->srcIP << " dstip=" << this->getTcpConn()->dstIP <<
			" myip=" << this->getMaster()->inHost()->getDefaultIP() << endl);
	sendData(seqNoBytes, tcpMaster->getMSS(), TCPMessage::TCP_FLAG_ACK,
			sink->rcvWnd->Seqno(), true, true, options);

	if (seqno > maxseq_) {
		maxseq_ = seqno;
		if (!rtt_active_) {
			rtt_active_ = 1;
			if (seqno > rtt_seq_) {
				rtt_seq_ = seqno;
				rtt_ts_ = tcpMaster->getNow().second();
			}
		}
	} else {
		++nrexmitpack_;
	}
	if (!(rtx_timer_->status() == TIMER_PENDING) || force_set_rtx_timer) {
		// No timer pending, schedule one
		setRtxTimer();
	}
}

int TCPSession::window() {
	return (linux_.snd_cwnd);
}

double TCPSession::windowd() {
	return (linux_.snd_cwnd);
}

void TCPSession::applClose() {
	//we do not implement this function since we suppose the application is always ready to close
	if (1) {
		// if there is nothing to send
		switch (state) {
		case TCP_STATE_ESTABLISHED:
			return;
		case TCP_STATE_CLOSE_WAIT:
			return;
		case TCP_STATE_TIME_WAIT:
			return;
		}
	} else {
	}
}

void TCPSession::timeout(int tno)
{
	LOG_DEBUG("[" << this->sessionNumber << "] at [" << this->tcpMaster->getNow().second() << " MSL: tno=" << tno << endl);
	if(tno == TCP_TIMER_CONNTERM){
		LOG_DEBUG("[" << this->getMaster()->inHost()->getNow().second() << "] MSL: the connection termination timer has expired" << endl);
		if(state == TCP_STATE_TIME_WAIT || state == TCP_STATE_LAST_ACK){
			this->initStateClosed();
			return;
		}
		//clients
		//this->tcpMaster->eraseSession(this);
	} else if (tno == TCP_TIMER_RTX) {
		LOG_DEBUG("TCP_TIMER_RTX timer invoked 1" << endl);
		if (highest_ack_ == maxseq_ && !slow_start_restart_) {
			/*
			 * TCP option:
			 * If no outstanding data, then don't do anything.
			 */
			//LOG_DEBUG("[" << this->sessionNumber << "] at [" << this->tcpMaster->getNow().second() << "] no call to send_much" << endl);
			LOG_DEBUG("in TIMEOUT(): no outstanding data" << endl);
			return;
		};
		LOG_DEBUG("TCP_TIMER_RTX timer invoked" << endl);
		last_cwnd_action_ = CWND_ACTION_TIMEOUT;
		// if there is no outstanding data, don't cut down ssthresh_
		if (highest_ack_ == maxseq_ && restart_bugfix_) {
			if (linux_.icsk_ca_ops)
				save_from_linux();
			slowdown(CLOSE_CWND_INIT);
			loadToLinux();
		} else {
			// close down to 1 segment
			enterLoss();
			++nrexmit_;
		};

		// if there is no outstanding data, don't back off rtx timer
		if (highest_ack_ == maxseq_){
			resetRtxTimer(TCP_REASON_TIMEOUT, 0);
		}
		else{
			resetRtxTimer(TCP_REASON_TIMEOUT, 1);
		}

		nextPktsInFlight = 0;
		linux_.bytes_acked = 0;
		save_from_linux();

		if(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
					state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT) {
			return;
		} else {
			sendMuch(0, TCP_REASON_TIMEOUT);
		}
	} else {
		// we do not know what it is
		if (linux_.icsk_ca_ops)
			save_from_linux();

		// retransmit timer
		if (tno == TCP_TIMER_RTX) {
			// There has been a timeout - will trace this event
			//trace_event("TIMEOUT");

			frto_ = 0;
			// Set pipe_prev as per Eifel Response
			pipe_prev_ = (window() > ssthresh_) ? window() : (int) ssthresh_;

			if (cwnd_ < 1){
				cwnd_ = 1;
			}
			if (highest_ack_ == maxseq_ && !slow_start_restart_) {
				/*
				 * TCP option:
				 * If no outstanding data, then don't do anything.
				 */
				// Should this return be here?
				// What if CWND_ACTION_ECN and cwnd < 1?
				// return;
			} else {
				recover_ = maxseq_;
				if (highest_ack_ == -1 && wnd_init_option_ == 2)
					/*
					 * First packet dropped, so don't use larger
					 * initial windows.
					 */
					wnd_init_option_ = 1;
				else if ((highest_ack_ == -1) && (wnd_init_option_ == 1)
						&& (wnd_init_ > 1) && bugfix_ss_)
					/*
					 * First packet dropped, so don't use larger
					 * initial windows.  Bugfix from Mark Allman.
					 */
					wnd_init_ = 1;
				if (highest_ack_ == maxseq_ && restart_bugfix_)
					/*
					 * if there is no outstanding data, don't cut
					 * down ssthresh_.
					 */
					slowdown(CLOSE_CWND_ONE | NO_OUTSTANDING_DATA);
				else if (highest_ack_ < recover_ && last_cwnd_action_
						== CWND_ACTION_ECN) {
					/*
					 * if we are in recovery from a recent ECN,
					 * don't cut down ssthresh_.
					 */
					slowdown(CLOSE_CWND_ONE);
					if (frto_enabled_ || sfrto_enabled_) {
						frto_ = 1;
					}
				} else {
					++nrexmit_;
					last_cwnd_action_ = CWND_ACTION_TIMEOUT;
					slowdown(CLOSE_SSTHRESH_HALF | CLOSE_CWND_RESTART);
					if (frto_enabled_ || sfrto_enabled_) {
						frto_ = 1;
					}
				}
			}
			// if there is no outstanding data, don't back off rtx timer
			if (highest_ack_ == maxseq_ && restart_bugfix_) {
				resetRtxTimer(0, 0);
			} else {
				resetRtxTimer(0, 1);
			}
			last_cwnd_action_ = CWND_ACTION_TIMEOUT;

			if(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
					state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT) {
				return;
			} else {
				sendMuch(0, TCP_REASON_TIMEOUT, maxBurst);
			}
		} else {
			timeoutNonRtx(tno);
		}
		loadToLinux();
	}
}

void TCPSession::enterLoss() {
	paramManager.load_local();
	touchCwnd();
	if (linux_.icsk_ca_ops == NULL) {
		slowdown(CLOSE_SSTHRESH_HALF | CLOSE_CWND_RESTART);
		loadToLinux();
	} else {

		linux_.snd_ssthresh = linux_.icsk_ca_ops->ssthresh(&linux_);
		linux_.snd_cwnd = 1;
		linux_.snd_cwnd_cnt = 0;
		linux_.bytes_acked = 0;

		ncwndcuts_++;
		congAction = true;
	}
	recover_ = maxseq_;
	tcp_set_ca_state(TCP_CA_Loss);
	//scb_->ClearScoreBoard();
	scb_->MarkLoss(highest_ack_ + 1, t_seqno_);
	//In Linux, we don't clear scoreboard in timeout, unless it's SACK Renege. We don't consider SACK Renege here.
	paramManager.restore_default();
}

void TCPSession::timeoutNonRtx(int tno) {
	if (tno == TCP_TIMER_DELSND) {
		/*
		 * delayed-send timer, with random overhead
		 * to avoid phase effects
		 */
		if(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
				state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT) {
			return;
		} else {
			sendMuch(1, TCP_REASON_TIMEOUT, maxBurst);
		}
	}
}

void TCPSession::timeoutResend() {
	switch (state) {
	case TCP_STATE_SYN_SENT:
		// retransmit the syn segment
		//send_data(sndwnd->firstUnused(), 0, TCPMessage::TCP_FLAG_SYN,
		//  rcvwnd->expect(), false, true);
		break;

	case TCP_STATE_SYN_RECEIVED:
		// retransmit the syn and ack segment
		//send_data(sndwnd->firstUnused(), 0, TCPMessage::TCP_FLAG_SYN | TCPMessage::TCP_FLAG_ACK,
		//  rcvwnd->expect(), false, true);
		break;

	case TCP_STATE_ESTABLISHED:
	case TCP_STATE_CLOSE_WAIT:
		// retransmit data segment
		break;

	case TCP_STATE_CLOSING:
	case TCP_STATE_FIN_WAIT_1:
	case TCP_STATE_LAST_ACK:
		break;

	default:
		// TCP_STATE_LISTEN, TCP_STATE_CLOSED, TCP_STATE_TIME_WAIT,
		// or TCP_STATE_FIN_WAIT_2
		LOG_DEBUG("PANIC: TCPSession::timeout_resend() unexpected" << state);
	}
}

void TCPSession::sendData(uint32 seqno, uint32 length, byte mask, uint32 ackno,
		bool need_calc_rtt, bool need_set_rextimeout, int flags_,
		struct OptionsEnabled* opts, uint32 data_length, byte* final_data_to_send)
{
	if(state == TCP_STATE_CLOSED)
		return;

	int nowInt = 0;
	int ts_peer = 0;

	// they will be used when SACK enabled.
	int added_len = 0;
	// Length of data to send
	uint32_t data_len = 0;
	byte* options = NULL;
	DataMessage* data = NULL;
	TCPMessage* tcp_msg = NULL;

	LOG_DEBUG("SEND DATA ackno=" << ackno << " seqno=" << seqno << " length=" << length <<
			" flags=" << flags_ << " mask=" << (int)mask << " curSeq=" << curSeq << endl);

	// generate the payload for this TCP packet
	if(!(state == TCP_STATE_CLOSE_WAIT || state == TCP_STATE_CLOSING || state == TCP_STATE_FIN_WAIT_1 || state == TCP_STATE_FIN_WAIT_2 ||
					state == TCP_STATE_LAST_ACK || state == TCP_STATE_TIME_WAIT || state == TCP_STATE_SYN_SENT) && length>0)
	{
		data = sndWnd->generate(seqno - 1, length, data_len);
	} else {
		if(data_length != 0 && final_data_to_send != NULL) {
			LOG_DEBUG("creating data message from data sent from app" << endl);
			data_len = data_length;
			data = new DataMessage(data_length, final_data_to_send);
			LOG_DEBUG("creating data message from data sent from app, length=" << data_len << " data=" <<
					data << endl);
		}
	}

	LOG_DEBUG("DATA_LEN obtained from generate, data_len=" << data_len << endl);

	// except the SYN and FIN packets, we set ACK anyway.
	if (!(mask & TCPMessage::TCP_FLAG_SYN))
		mask |= TCPMessage::TCP_FLAG_ACK;

	if (flags_) {
		if (flags_ & MSS_OPT) {
			added_len += 4;
		}
		if (flags_ & WSOPT_OPT) {
			added_len += 3;
		}
		if (flags_ & SACK_PERMITTED_OPT) {
			added_len += 2;
		}
		if (flags_ & SACK_OPT) {
			added_len += sink->sacks_size * 4 * 2 + 2;
		}
		if (flags_ & TSOPT_OPT) {
			added_len += 10;
		}
		int len = added_len;
		if (added_len % 4)
			added_len = ((int) (added_len / 4)) * 4 + 4;


		options = new byte[added_len];

		int pos = 0;
		if (flags_ & MSS_OPT) {
			options[pos++] = 2;
			options[pos++] = 4;
			options[pos++] = (tcpMaster->getMSS() >> 8) & 0xFF;
			options[pos++] = tcpMaster->getMSS() & 0xFF;
		}
		if (flags_ & WSOPT_OPT) {
			options[pos++] = 3;
			options[pos++] = 3;
			options[pos++] = 7;
		}
		if (flags_ & SACK_PERMITTED_OPT) {
			options[pos++] = 4;
			options[pos++] = 2;
		}
		if (flags_ & SACK_OPT) {
			if (sink->sacks_available == true) {
				for (int i = 0; i < sink->sacks_size; i++) {
					options[pos++] = 5;
					options[pos++] = sink->sacks_size;
					options[pos++] = (*(*(sink->sacks_ + i)) & 0xFF000000)
							>> 24;
					options[pos++] = (*(*(sink->sacks_ + i)) & 0x00FF0000)
							>> 16;
					options[pos++] = (*(*(sink->sacks_ + i)) & 0x0000FF00) >> 8;
					options[pos++] = *(*(sink->sacks_ + i)) & 0x000000FF;
					options[pos++] = (*(*(sink->sacks_ + i) + 1) & 0xFF000000)
							>> 24;
					options[pos++] = (*(*(sink->sacks_ + i) + 1) & 0x00FF0000)
							>> 16;
					options[pos++] = (*(*(sink->sacks_ + i) + 1) & 0x0000FF00)
							>> 8;
					options[pos++] = *(*(sink->sacks_ + i) + 1) & 0x000000FF;
				}
				sink->sacks_available = false;
			}
		}

		if (flags_ & TSOPT_OPT) {
			double now = tcpMaster->getNow().millisecond();

			nowInt = (int) now;
			ts_peer = (int) ts_peer_;

			options[pos++] = 8;
			options[pos++] = 10;
			options[pos++] = (nowInt & 0xFF000000) >> 24;
			options[pos++] = (nowInt & 0x00FF0000) >> 16;
			options[pos++] = (nowInt & 0x0000FF00) >> 8;
			options[pos++] = nowInt & 0x000000FF;
			options[pos++] = (ts_peer & 0xFF000000) >> 24;
			options[pos++] = (ts_peer & 0x00FF0000) >> 16;
			options[pos++] = (ts_peer & 0x0000FF00) >> 8;
			options[pos] = ts_peer & 0x000000FF;
		}
		for (int i = len + 1; i <= added_len; i++) {
			pos++;
			options[pos] = 1;
		}
	}

	if ((mask & TCPMessage::TCP_FLAG_SYN) != 0){
		LOG_DEBUG("sending a syn pkt in senddata" << endl);
		peer_wnd_scalable = false;
	}

	if(data) {
		LOG_DEBUG("setting PSH flag data=" << data << " data->size()" << data->size() <<
			" length=" << data_len << endl);
	} else {
		LOG_DEBUG("setting PSH flag data=NULL data->size()=NULL" <<
			" length=" << data_len << endl);
	}

	// We set the PSH flag for every data packet which has payload
	if (data_len > 0) {
		//LOG_DEBUG("TCP_FLAG_PSH SET!!!! \n");
		//mask |= TCPMessage::TCP_FLAG_PSH;
	}

	//if(this->curSeq > 0){
	if(data == NULL){
		LOG_DEBUG("acker" << endl);
	} else {
		if (data->getRawData() != NULL) {
		
			LOG_DEBUG("in processack, [senddata] setting PSH flag!" << endl);

			LOG_DEBUG("push: [" << this->tcpMaster->inHost()->getDefaultIP() << "] [" << this->tcpMaster->getNow().second() <<
			"] TCP_FLAG_PSH SET!!!! data=" << data->getRawData() << "-" << " ackno=" << ackno << " seqno=" << seqno << endl);
			mask |= TCPMessage::TCP_FLAG_PSH;
		}
	}
	//}

	LOG_DEBUG("miguel CREATE TCP MESSAGE IP=" << (this->tcpMaster->inHost()->getDefaultIP()) <<
		" dstport=" << tcpConn->dstPort << " srcport=" << tcpConn->srcPort <<
		" seqno=" << seqno << " ackno=" << ackno << " ADDED_LEN=" << added_len << "\n");

	tcp_msg = new TCPMessage(tcpConn->dstPort,
			tcpConn->srcPort, seqno, ackno, mask, 100, (SSFNET_TCPHDR_LENGTH
					+ added_len), added_len, options);

	delete[] options;

	if(seqno > lastSeqno){
		lastSeqno = seqno;
	}
	if(ackno > lastAck){
		lastAck = ackno;
	}

	// attach the payload to the tcp message header.
	LOG_DEBUG("in senddata length=" << data_len << endl);

	// If in simulation or if this a SYN packet, do not attach payload
	if(data != NULL && data_len > 0) {
		tcp_msg->carryPayload(data);
		assert(tcp_msg->hasPayload());
	}

	if(tcp_msg->payload()!=NULL)
		LOG_DEBUG("payload of tcpmessage=" << tcp_msg->payload()->size() << endl);

	// Physically Send the Data
	IPPushOption ops((*tcpConn).dstIP, (*tcpConn).srcIP,
			SSFNET_PROTOCOL_TYPE_TCP, 0);

	if (strcmp(this->tcpMaster->inHost()->getDefaultIP().toString().c_str(),
			(*tcpConn).srcIP.toString().c_str()) == 0) {
		//client side
		IPPushOption ops2((*tcpConn).srcIP, (*tcpConn).dstIP,
				SSFNET_PROTOCOL_TYPE_TCP, DEFAULT_IP_TIMETOLIVE);
		ops = ops2;
		tcp_msg->setSrcPort(myhtons((*tcpConn).srcPort));
		tcp_msg->setDstPort(myhtons((*tcpConn).dstPort));
	} else {
		//server side
		tcp_msg->setSrcPort(myhtons((*tcpConn).dstPort));
		tcp_msg->setDstPort(myhtons((*tcpConn).srcPort));
	}

	LOG_DEBUG("[debug longdelays] [ " << this->getMaster()->inHost()->getNow().second() << " ], sending data with" <<
			" seqno=" << tcp_msg->getSeqno() << " ackno=" << tcp_msg->getAckno() <<
			" srcport=" << this->tcpConn->srcPort <<
			" dstport=" << this->tcpConn->dstPort << endl);

	LOG_DEBUG("[" << this->tcpMaster->getNow().second() << "] [sendData] sending a pkt with: seqno=" << tcp_msg->getSeqno() << " ackno=" <<
                                tcp_msg->getAckno() << endl);

	tcpMaster->push(tcp_msg, NULL, (void*) &ops, sizeof(IPPushOption));
}

uint32 TCPSession::calcAdvertisedWnd() {
	// first check the free space in the buffer
	//XXX Compute window adequately
	return 65535;
}

const char* TCPSession::get_state_name(int state) {
	switch (state) {
	case TCP_STATE_CLOSED:
		return "CLOSED";
	case TCP_STATE_LISTEN:
		return "LISTEN";
	case TCP_STATE_SYN_SENT:
		return "SYN_SENT";
	case TCP_STATE_SYN_RECEIVED:
		return "SYN_RECEIVED";
	case TCP_STATE_ESTABLISHED:
		return "ESTABLISHED";
	case TCP_STATE_CLOSE_WAIT:
		return "CLOSE_WAIT";
	case TCP_STATE_CLOSING:
		return "CLOSING";
	case TCP_STATE_FIN_WAIT_1:
		return "FIN_WAIT_1";
	case TCP_STATE_FIN_WAIT_2:
		return "FIN_WAIT_2";
	case TCP_STATE_LAST_ACK:
		return "LAST_ACK";
	case TCP_STATE_TIME_WAIT:
		return "TIME_WAIT";
	default:
		return "UNKNOWN";
	}
}

bool TCPSession::installCongestionControl(const char* name)
{
	//LOG_DEBUG("in INSTALL_CA : CA configured is " << name << endl);

	CongestionControlManager cong_ops_manager;

	struct tcp_congestion_ops* newops = cong_ops_manager.get_ops(name);

	if (newops) {
		//LOG_DEBUG("INSTALLING " << name << "\n");
		if (linux_.icsk_ca_ops != newops) {
			//release any existing congestion control algorithm before install
			if (linux_.icsk_ca_ops != NULL) {
				//LOG_DEBUG(" icsk_ca_ops is NOT null\n");
				if (linux_.icsk_ca_ops->release)
					linux_.icsk_ca_ops->release(&linux_);
				save_from_linux();
			} else {
				//LOG_DEBUG(" icsk_ca_ops is null\n");
				loadToLinuxOnce();
				loadToLinux(); //it was controlled by NS2, load to Linux
				tcp_tick_ = 1.0 / (double) JIFFY_RATIO;
			}
			linux_.icsk_ca_ops = newops;

			assert(linux_.icsk_ca_ops);

			if (linux_.icsk_ca_ops != NULL) {
				//LOG_DEBUG(" AFTER icsk_ca_ops is NOT null\n");
			} else {
				//LOG_DEBUG(" AFTER icsk_ca_ops is null\n");
			}

			if ((initialized_) && (newops->init)) {
				//if the algorithm is changed in the middle, we need to intialize the module
				newops->init(&linux_);
			}

			if ((linux_.icsk_ca_ops->flags & TCP_CONG_RTT_STAMP)
					|| (linux_.icsk_ca_ops->rtt_sample)) {
				bugfix_ts_ = 1;
				// if rtt_sample exists, we must turn on the sender-side timestamp record
				//to provide accurate rtt in microsecond.
			}

			save_from_linux();
		}
		return true;
	} else {
		return false;
	}
}

void TCPSession::removeCongestionControl()
{
	if (linux_.icsk_ca_ops != NULL) {
		if (linux_.icsk_ca_ops->release)
			linux_.icsk_ca_ops->release(&linux_);
		save_from_linux();
		linux_.icsk_ca_ops = NULL;
	}
}

void TCPSession::cancelRtxTimer() {}

void TCPSession::setRtxTimer() {
#ifdef COARSE_GRAINED_TIMERS
	//rtx_timer_->setCounter(rttTimeout());
	rtx_timer_->setCounter(3);
	LOG_DEBUG("rtx_timer set to " << rttTimeout() << endl);
	rtx_timer_->is_running = true;
#else
	rtx_timer_->resched(rttTimeout());
#endif
}

void TCPSession::resetRtxTimer(int mild, int backoff) {
	if (backoff){
		rttBackoff();
	}
	setRtxTimer();
	if (!mild) {
		t_seqno_ = highest_ack_ + 1;
	}
	rtt_active_ = 0;
}

void TCPSession::rttBackoff() {
	if (t_backoff_ < 64)
		t_backoff_ <<= 1;

	if (t_backoff_ > 8) {
		/*
		 * If backed off this far, clobber the srtt
		 * value, storing it in the mean deviation
		 * instead.
		 */
		t_rttvar_ += (t_srtt_ >> T_SRTT_BITS);
		t_srtt_ = 0;
	}
}

double TCPSession::rttTimeout()
{
	double timeout;

	if (rfc2988_) {
		// Correction from Tom Kelly to be RFC2988-compliant, by
		// clamping minrto_ before applying t_backoff_.
		if (t_rtxcur_ < minrto_ && !use_rtt_) {
			timeout = minrto_ * t_backoff_;
		} else {
			timeout = t_rtxcur_ * t_backoff_;
		}
	} else {
		// only of interest for backwards compatibility
		timeout = t_rtxcur_ * t_backoff_;
		if (timeout < minrto_) {
			timeout = minrto_;
		}
	}

	if (timeout > maxrto_) {
		timeout = maxrto_;
	}

	if (timeout < 2.0 * tcp_tick_) {
		if (timeout < 0) {
			fprintf(stderr, "TcpAgent: negative RTO!  (%f)\n", timeout);
			exit(1);
		} else if (use_rtt_ && timeout < tcp_tick_) {
			timeout = tcp_tick_;
		} else {
			timeout = 2.0 * tcp_tick_;
		}
	}

	use_rtt_ = 0;
	return (timeout);
}

void TCPSession::save_from_linux() {
	//double before = cwnd_;
	
	if (nextPktsInFlight > (int) linux_.snd_cwnd){
		LOG_DEBUG("[debug conversion] in save_from_linux 1 , nextPktsInFlight" << nextPktsInFlight <<
				" linux_.snd_cwnd=" << linux_.snd_cwnd << endl);
		cwnd_ = nextPktsInFlight;
	}
	else {
		LOG_DEBUG("[debug conversion] in save_from_linux 2, nextPktsInFlight" << nextPktsInFlight <<
			" linux_.snd_cwnd=" << linux_.snd_cwnd << endl);
		cwnd_ = linux_.snd_cwnd;
	}

	ssthresh_ = linux_.snd_ssthresh;
	// for legacy variables:
	last_ack_ = highest_ack_;
}

void TCPSession::loadToLinuxOnce() {
	linux_.snd_cwnd_clamp = (long unsigned) round(wnd_);
	linux_.snd_ssthresh = linux_.snd_cwnd_clamp;
	linux_.mss_cache = this->getMaster()->getMSS();
	LOG_DEBUG("MSS in Master =" << this->getMaster()->getMSS() << endl);
}

void TCPSession::loadToLinux() {
	linux_.snd_ssthresh = (int) ssthresh_;

	if ((nextPktsInFlight > (int) linux_.snd_cwnd) && ((int) cwnd_ >= (int) linux_.snd_cwnd)) {
		//We are in the process of rate-halving and the traditional ns-2 does not ask for further reduction
		nextPktsInFlight = (int) (trunc(cwnd_));
	} else {
		//We are not in the process of rate-halving -- safe to load
		linux_.snd_cwnd = (int) (trunc(cwnd_));
	}
	//read only:
	linux_.snd_cwnd_clamp = (int) wnd_;
}

bool ParamList_::get_param(int* address, int* valuep) {
	struct paramNode *p = head;
	while (p && (p->addr != address))
		p = p->next;
	if (p) {
		*valuep = p->value;
		return true;
	};
	return false;
}

// Set a local value for a TCP
void ParamList_::set_param(int* address, int value) {
	struct paramNode *p = head;
	while ((p) && (p->addr != address)) {
		p = p->next;
	};
	if (p) {
		//we find one.
		p->value = value;
		return;
	};
	//Cannot find any
	//Create a new entry
	p = new struct paramNode;
	p->addr = address;
	p->value = value;
	p->default_value = *address;
	p->next = head;
	head = p;
}

/** Refresh all the values in the list */
void ParamList_::refresh_default() {
	struct paramNode *p = head;
	while (p) {
		p->default_value = *(p->addr);
		p = p->next;
	};
}

void ParamList_::restore_default() {
	struct paramNode *p = head;
	while (p) {
		p->value = *(p->addr);
		*(p->addr) = p->default_value;
		p = p->next;
	};
}

void ParamList_::load_local() {
	struct paramNode *p = head;
	while (p) {
		p->default_value = *(p->addr);
		*(p->addr) = p->value;
		p = p->next;
	};
}

ParamList_::~ParamList_() {
	while (head) {
		struct paramNode *p = head;
		head = head->next;
		delete p;
	};
}

void TCPSession::sample()
{
	if(!tcpMaster) LOG_ERROR("How did I get here");
	return;
	if (curSeq > 1){
		if(!filePointer) {
			char f[1000];
			sprintf(f, "/Users/Erazo/Documents/workspace/primex/netsim/results/trace_%llu_session_%d.%p.tcp.out",
					tcpMaster->inHost()->getUID(), getID(),this);
			filePointer = fopen(f,"a");
		}
		bool found = false;
		fprintf(filePointer,"%g %g %d %d %d %d %d %d %d\n",
				tcpMaster->getNow().second(), cwnd_, maxseq_, ndatapack_,
				nackpack_, nrexmitpack_, ncwndcuts_, t_rtt_, this->sndWnd->pktnoToSeqno(t_seqno_, found));
		fflush(filePointer);
		//fclose(filePointer);
	}
	// reset the timer to wait for another interval time
	if (tcpMaster->getSamplingInterval() > 0){
		smp_timer->resched(0.1);
	}
//	}
}

/*
 * SLOW and FAST timers code
 */
#ifdef COARSE_GRAINED_TIMERS
void TCPSession::slowTimeout()
{
	LOG_DEBUG("[" << this->sessionNumber << "] at [" << this->tcpMaster->getNow().second() << "] slow timeout" <<
			" rtx_timer_->isRunning()" << rtx_timer_->isRunning() << " rtx_timer_->getCounter()" <<
			rtx_timer_->getCounter() << endl);

	//handle rxmit timer
	rtx_timer_->setCounter(rtx_timer_->getCounter()
			- tcpMaster->getSlowTimeout());

	if(open_session_enabled == true){
		LOG_DEBUG("[debug longdelays] [ " << this->getMaster()->inHost()->getNow().second() << " ] exe 3-way handshake rtx, "
				"TCP_STATE_LISTEN, " <<
				" srcport=" << this->tcpConn->srcPort <<
				" dstport=" << this->tcpConn->dstPort << endl);

		open_session_interval = open_session_interval - DEFAULT_SLOW_TIMER;
		if(open_session_interval <= 0){
			if(state == TCP_STATE_SYN_SENT){
				// rtx syn to start 3-way handshake
				sendFirst();
			}
			else if(state == TCP_STATE_SYN_RECEIVED){
				sendData(sndWnd->start(), 0, TCPMessage::TCP_FLAG_SYN
						| TCPMessage::TCP_FLAG_ACK, sink->rcvWnd->Seqno(), true, true,
						cached_options, &cached_opts_en);

				open_session_interval = 10*this->getMaster()->getSlowTimeout() + 0.1; //at least we will wait one slow timeout interval
			}
			//if(state == TCP_STATE_SYN_SENT) {
				//sendFinScheduleTimer(seqnoForClosing, acknoForClosing);
			//}
		}
	}

	if(close_session_enabled == true){
		LOG_DEBUG("close_session_interval=" << close_session_interval << " this->getMaster()->getSlowTimeout()="
				<< DEFAULT_SLOW_TIMER << endl);

		close_session_interval = close_session_interval - DEFAULT_SLOW_TIMER;
		if(close_session_interval <= 0){
			sendFinScheduleTimer(seqnoForClosing, acknoForClosing);

			if(state == TCP_STATE_TIME_WAIT || state == TCP_STATE_LAST_ACK){
				this->initStateClosed();
				return;
			}
		}
	}

	if (rtx_timer_->getCounter() <= 0 && rtx_timer_->isRunning()) {
		 timeout(TCP_TIMER_RTX);
		 LOG_DEBUG("\ttimeout in sender\n");
	}
}
#endif

//CODE FOR SINK
TCPSinkSession::TCPSinkSession(TCPMaster* master,
		SimpleSocket* sock, TCPConnection* conn, TCPSession* sess_)
{
	tcp_master = master;
	tcp_sock = sock;
	tcp_conn = conn;
	snd_session = sess_;
	save_ = 0;
	lastreset_ = 0.0;
	rcvWnd = 0;
	sacks_ = NULL;
	sacks_available = false;
	sacks_size = 0;

#ifdef COARSE_GRAINED_TIMERS
	delayed_ack = false;
#endif

	//Timers
#ifdef SACK1_DELACK
	delay_timer_ = new TCPSinkTimer(TCP_TIMER_SACK1_DELACK, this);
	interval_ = 0.2;
#else
	delay_timer_ = 0;
#endif

	// Initialize variables
	ts_echo_rfc1323_ = true;
	tcp_master = master;
	bytes_ = 0;

	// Values extracted from ns-default.tcl
	max_sack_blocks_ = 3;
	ts_echo_bugfix_ = true; // default changed, 2003/8/13
	ts_echo_rfc1323_ = false; // default added, 2003/8/13
	generate_dsacks_ = false;
	qs_enabled_ = false;
	RFC2581_immediate_ack_ = false;
	bytes_ = 0;
	ecn_syn_ = false; // Added 2005/11/21 for SYN/ACK pkts.
}

void TCPSinkSession::reset() {
#ifdef SACK1_DELACK
	if (delay_timer_->status() == TIMER_PENDING){
		delay_timer_->cancel();
	}
#endif
	rcvWnd->reset();
	save_ = NULL;
	lastreset_ = tcp_master->getNow().second();
}

void TCPSinkSession::ack(TCPMessage* opkt)
{
	if(this->snd_session->state == TCP_STATE_CLOSED)
                return;

	int options = 0;
	// New packet to be transmitted as ACK

	// Timestamp the packet
	if (snd_session->getTSEnabled() == 1) {
		//LOG_DEBUG("TS ENABLED 2" << endl);
		options |= TSOPT_OPT;
	}

	int size_ = 0;

	// Get the block of SACKS that explicitly indicates the blocks of packets received
	sacks_ = rcvWnd->append_ack(opkt, opkt->getSeqno(), size_);

	// Get the size of the SACK block, from 0 to 3
	if (size_ > 0) {
		sacks_available = true;
		sacks_size = size_;
	} else {
		sacks_available = false;
		sacks_size = 0;
	}

	// Record the last ACK seen extracting it from the packet
	rcvWnd->last_ack_sent_ = opkt->getAckno();

	// Send it if no other data packet is left to send
	int options_ = 0;
	if (snd_session->getT_seqno() >= snd_session->getCurSeq()) {
		options_ |= SACK_OPT;
	}

	// Send ACK if no more DATA packets are on the buffer
	//LOG_DEBUG("IN ACK: t_seqno_=" << this->snd_session->t_seqno_ <<
		//	" curseq=" << this->snd_session->curSeq << "\n");

	if (this->snd_session->t_seqno_ > this->snd_session->curSeq) {
		byte mask = 0;
		int added_len = 0;
		byte* options = NULL;
		int flags_ = 0;
		int pos = 0;

		// Preparing this packet to be an ACK packet
		mask = mask | TCPMessage::TCP_FLAG_ACK;

		// Constructing the options
		flags_ |= TSOPT_OPT;
		flags_ |= SACK_OPT;

		//The header is going to be enlarged according to the options enabled
		added_len = 10; //TIMESTAMPS
		added_len += size_ * 4 * 2 + 2; //SACKS

		// Enlarge length so that the header is a multiple of a window (32 bits)
		if (added_len % 4){
			added_len = ((int) (added_len / 4)) * 4 + 4;
		}

		options = new byte[added_len];

		/*
		 * For TSOPT option
		 */
		// Get the time
		double now = this->snd_session->getMaster()->getNow().millisecond();
		int nowInt = (int)now;
		// My timestamp
		options[pos++] = 8;  //code for TSOPT
		options[pos++] = 10; //code for TSOPT
		options[pos++] = (nowInt & 0xFF000000) >> 24;
		options[pos++] = (nowInt & 0x00FF0000) >> 16;
		options[pos++] = (nowInt & 0x0000FF00) >> 8;
		options[pos++] = nowInt & 0x000000FF;

		// The timestamp of the peer
		options[pos++] = ((int)(this->snd_session->getTs_Peer()) & 0xFF000000) >> 24;
		options[pos++] = ((int)(this->snd_session->getTs_Peer()) & 0x00FF0000) >> 16;
		options[pos++] = ((int)(this->snd_session->getTs_Peer()) & 0x0000FF00) >> 8;
		options[pos++] = ((int)(this->snd_session->getTs_Peer()) & 0x000000FF);

		// Execute this block is there there a generated SACK block
		if ((flags_ & SACK_OPT) && sacks_available == true && size_ > 0) {
			LOG_DEBUG("BUILDING SACKS \n");
			options[pos++] = 5;
			options[pos++] = size_ * 4 * 2 + 2;
			for (int i = 0; i < size_; i++) {
				//LOG_DEBUG("adding the " << i << " SACK LEFT=" << *(*(sacks_+i)+0) <<
				  //" RIGHT=" << *(*(sacks_+i)+1) << endl);
				options[pos++] = (*(*(this->sacks_ + i)) & 0xFF000000) >> 24;
				options[pos++] = (*(*(this->sacks_ + i)) & 0x00FF0000) >> 16;
				options[pos++] = (*(*(this->sacks_ + i)) & 0x0000FF00) >> 8;
				options[pos++] = *(*(this->sacks_ + i)) & 0x000000FF;
				options[pos++] = (*(*(this->sacks_ + i) + 1) & 0xFF000000) >> 24;
				options[pos++] = (*(*(this->sacks_ + i) + 1) & 0x00FF0000) >> 16;
				options[pos++] = (*(*(this->sacks_ + i) + 1) & 0x0000FF00) >> 8;
				options[pos++] = *(*(this->sacks_ + i) + 1) & 0x000000FF;
			}
			sacks_available = false;
			for (int i = 0; i < size_; i++) {
				delete[]sacks_[i];
			}
			delete[] sacks_;
		}

		//LOG_DEBUG("BEFORE PADDING len+1=" << len+1 << " added_len=" << added_len <<
		  //" pos=" << pos << "\n");
		// Pad the packet if needed
		for (int i = pos; i < added_len; i++) {
			//LOG_DEBUG("PADDING THE PKT \n");
			options[pos++] = 1;
		}

		// Get the next pktno to transmit
		int xmit_seqno_ = this->snd_session->scb_->GetNextRetran();
		if (xmit_seqno_ == -1){
			// If there is not sacks then extract the seqno from the send_session
			xmit_seqno_ = this->snd_session->t_seqno_ - 1;
			//LOG_DEBUG("xmit_seqno_=" << xmit_seqno_ << endl);
		}

		// Transform this pktno to seqno
		bool found = false;
		uint32 seqNoBytes = this->snd_session->sndWnd->pktnoToSeqno(xmit_seqno_, found);
		LOG_DEBUG("in processack, [ack] translating xmit_seqno_ " << xmit_seqno_ << " to " << seqNoBytes << " found=" << found << endl);
		LOG_DEBUG("in processack, [ack] translating xmit_seqno_ 2 to " << 
			this->snd_session->sndWnd->pktnoToSeqno(2, found) << " found=" << found << endl);

		//LOG_DEBUG("ack seqNoBytes=" << seqNoBytes <<
			//	" this->rcvWnd->next_" << this->rcvWnd->next_ << endl);

		// If the sender size has no more packets to send, then send acks with the seqno sent + 1
		if (this->snd_session->t_seqno_ > this->snd_session->curSeq) {
			seqNoBytes =  this->snd_session->sndWnd->pktnoToSeqno(xmit_seqno_+1, found);
		}

		TCPMessage* tcp_msg = new TCPMessage(
			this->snd_session->tcpConn->srcPort,
			this->snd_session->tcpConn->dstPort, seqNoBytes,
			this->rcvWnd->next_, mask, this->snd_session->calcAdvertisedWnd(), (SSFNET_TCPHDR_LENGTH
		+ added_len), added_len, options);

		LOG_DEBUG("in processack, [ack] sending an ack with: seqno=" << tcp_msg->getSeqno() << " ackno=" <<
				tcp_msg->getAckno() << " this->rcvWnd->next_=" << this->rcvWnd->next_ << endl);

		delete[] options;

		LOG_DEBUG("ACKNO constructing the message ackno=" << this->rcvWnd->next_ << endl);

		//Do not carry payload now
		//LOG_DEBUG("ACK 13 src=" << this->snd_session->tcpConn->srcIP << " dst=" <<
			//this->snd_session->tcpConn->dstIP << "\n");
		IPPushOption ops(this->snd_session->tcpConn->srcIP,
				this->snd_session->tcpConn->dstIP, SSFNET_PROTOCOL_TYPE_TCP,
				DEFAULT_IP_TIMETOLIVE);

		// Correct the dstIP and srcIP if necessary
		if ((uint32)this->tcp_master->inHost()->getDefaultIP() !=
				(uint32)this->snd_session->tcpConn->srcIP) {
			//server side
			IPPushOption ops2(this->snd_session->tcpConn->dstIP,
					this->snd_session->tcpConn->srcIP,
					SSFNET_PROTOCOL_TYPE_TCP, DEFAULT_IP_TIMETOLIVE);
			ops = ops2;

			tcp_msg->setSrcPort(myhtons(this->snd_session->tcpConn->dstPort));
			tcp_msg->setDstPort(myhtons(this->snd_session->tcpConn->srcPort));
			//LOG_DEBUG("	AFTER srcIP=" << ops.src_ip << " dstIP=" << ops.dst_ip << "\n");
		}

		//LOG_DEBUG("srcIP=" << ops.src_ip << " dstIP=" << ops.dst_ip <<
			//	" srcport=" << tcp_msg->getSrcPort() << " dstport=" << tcp_msg->getDstPort() <<
			//	" seqno=" << seqNoBytes << " ack=" << this->rcvWnd->next_ << "\n" << endl);

		// Send ACK
		LOG_DEBUG("2nd push ACKNO=" << tcp_msg->getAckno() << " seqno=" << tcp_msg->getSeqno() <<
				" src IP=" << ops.src_ip << endl);

		this->snd_session->getMaster()->push(tcp_msg, NULL, (void*) &ops,
				sizeof(IPPushOption));
	} else {
		// Do not send any packet because there are no more packets to ACK
	}
}

//client
void TCPSinkSession::rcvRecv(TCPMessage* pkt, uint32& mask, uint32& bytes_to_deliver, struct OptionsEnabled& opt_en,
		int& options, byte*& app_data, int& data_size)
{
	int numBytes = 0;
	//struct OptionsEnabled opt_en;
	//int options = 0;
	int payload_length = pkt->hasPayload()?pkt->payload()->size():0; //good for both sim and emu
	LOG_DEBUG(" payload_length from tcpsession=" << payload_length << endl);

	//Number of packets to deliver to upper layer
	uint32 numToDeliver = 0;

	DataMessage* dm = 0;
	//If this packet has payload, drop it to a pointer
	if(pkt->hasPayload()) {
		//for emulation
		dm = (DataMessage*) pkt->payload();
	}

	//if (pkt->getFlags() & TCPMessage::TCP_FLAG_PSH) {
		//numBytes = pkt->totalPackingSize();
		numBytes = payload_length;

		if(pkt->hasPayload()){
		if(pkt->payload()->getRawData()!=NULL) {
			LOG_DEBUG("this pkt has a message" << endl);
		}
		}

		if(pkt->payload()){
			LOG_DEBUG("numbytesgot=" << numBytes << " payloadsize=" << pkt->payload()->size() << endl);
		}
		/*else {
			LOG_DEBUG("numbytesgot=" << numBytes << " payload=0" << endl);
		}*/
	//}

	TCPMessage *th = pkt;
	// W.N. Check if packet is from previous incarnation
	//snd_session->getOptions(options, &opt_en, pkt);
	if (opt_en.ts_value < lastreset_) {
		// Remove packet and do nothing
		LOG_DEBUG("prematurely returning...\n");
		return;
	}
	rcvWnd->update_ts(pkt->getSeqno(), opt_en.ts_value, ts_echo_rfc1323_);

	if (numBytes) {
		//if there is data to be delivered update the rcv window
		LOG_DEBUG("updating rcvwnd from tcpsession, numBytes=" << numBytes << endl);
		numToDeliver = rcvWnd->update(th->getSeqno(), numBytes, dm, app_data, data_size);

		if(pkt->hasPayload()){
			if(pkt->payload()->getRawData()!=NULL){
				LOG_DEBUG("numtodeliver real=" << numToDeliver << endl);
			}
		}
	}

	if (numToDeliver) {
		//There exists packets to deliver to the application
		bytes_ += numToDeliver;
		mask |= SimpleSocket::SOCKET_UPDATE_BYTES_RECEIVED;
		if(pkt->hasPayload()){
			if(pkt->payload()->getRawData()!=NULL){
				LOG_DEBUG("numtodeliver=" << numToDeliver << " mask=" << mask << endl);
			}
		}
		bytes_to_deliver = numToDeliver;
	} else {
		//this happens when there exists packets lost so that the receiver window has holes that has to fill
		if(pkt->hasPayload()){
			if(pkt->payload()->getRawData()!=NULL){
				LOG_DEBUG("there is nothing to deliver now=" << numToDeliver << endl);
			}
		}
	}
	if(pkt->hasPayload()){
		if(pkt->payload()->getRawData()!=NULL){
			LOG_DEBUG("bytestoapp=" << bytes_to_deliver << endl);
		}
	}


	// If there's no timer and the packet is in sequence, set a timer.
	// Otherwise, send the ack and update the timer.
	DEBUG_CODE(if(!rcvWnd) LOG_ERROR("SHOULD NEVER SEE THIS!"););
	//if ((delay_timer_->status() != TIMER_PENDING && (int) th->getSeqno() == rcvWnd->Seqno()) || save_ == NULL) {
	if (save_ == NULL) {
		if (RFC2581_immediate_ack_ && ((int)th->getSeqno() < rcvWnd->Maxseen())) {
			// don't delay the ACK since
			// we're filling in a gap
		} else {
			// delay the ACK and start the timer.
			save_ = pkt;
#ifndef COARSE_GRAINED_TIMERS
			//delay_timer_->resched(interval_);
#else
			//enableFastTimeout
			delayed_ack = true;
			tcp_master->enableFastTimeout(this);
#endif
			delay_timer_->resched(interval_);
			return;
		}
	}
	// If there was a timer, turn it off.
	if (delay_timer_->status() == TIMER_PENDING) {
#ifdef COARSE_GRAINED_TIMERS
		delayed_ack = false;
//XXX debug longdelays:	tcp_master->disableFastTimeout(this);
#endif
		delay_timer_->cancel();
	}

	// Acknowledge data right away
	ack(pkt);//, src_ip)
	if (save_ != NULL) {
		save_ = NULL;
	}
}

void TCPSinkSession::timeout(int) {
	//LOG_DEBUG("Timer expired in SINK" << endl);
	// The timer expired so we ACK the last packet seen
	if (save_ != NULL) {
		TCPMessage* pkt = save_;
		ack(pkt);
		save_ = NULL;
	}
}

#ifdef COARSE_GRAINED_TIMERS
void TCPSinkSession::fastTimeout() {
	timeout(0);
}
#endif

CongestionControlManager::CongestionControlManager()
{
	scan();
}

void CongestionControlManager::scan()
{
	struct tcp_congestion_ops *e;
	num_ = 1;
	list_for_each_entry_rcu(e, &ns_tcp_cong_list, list) {
		num_++;
	};
	ops_list = new struct tcp_congestion_ops*[num_];

	ops_list[0] = &tcp_reno;
	int i = 1;
	list_for_each_entry_rcu(e, &tcp_cong_list, list) {
		ops_list[i] = e;
		i++;
	}
	cc_list_changed = 0;
}

struct tcp_congestion_ops* CongestionControlManager::get_ops(const char* name)
{
	if (cc_list_changed)
		scan();
	for (int i = 0; i < num_; i++) {
		if (strcmp(name, ops_list[i]->name) == 0)
			return ops_list[i];
	}
	return 0;
}

void CongestionControlManager::dump()
{
	for (int i = 0; i < num_; i++) {
		//printf(" %s", ops_list[i]->name);
	}
}

//XXX Although all options are the same on all tested programs, they should be extracted from the
       //first packet got (for server)

}; // namespace ssfnet
}; // namespace prime
