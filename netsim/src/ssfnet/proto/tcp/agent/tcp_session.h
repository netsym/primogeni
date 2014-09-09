#ifndef __TCP_SESSION_H__
#define __TCP_SESSION_H__

//Headers included in ns-2
#include "scoreboard_2_ns.h"
#include "linux/ns-linux-util.h"

#include "os/data_message.h"
#include "proto/ipv4/ipv4_message.h"
#include "proto/ipv4/ipv4_session.h"
#include "tcp_message.h"
#include "../tcp_master.h"
#include "tcp_sndwnd.h"
#include "tcp_rcvwnd.h"
#include "proto/transport_session.h"

#define TCP_TIMER_SACK1_DELACK 6
#define SACK1_DELACK

#define CWND_ACTION_DUPACK      1       // dup acks/fastretransmit
#define CWND_ACTION_TIMEOUT     2       // retransmission timeout
#define CWND_ACTION_ECN         3       // ECN bit [src quench if supporte$
#define CWND_ACTION_EXITED      4       // congestion recovery has ended

#define TCP_TIMER_RTX           0
#define TCP_TIMER_DELSND        1
#define TCP_TIMER_BURSTSND      2
#define TCP_TIMER_CONNTERM      3

#define TCP_REASON_TIMEOUT    	0x01
#define TCP_REASON_DUPACK       0x02

#define TIMER_IDLE 0
#define TIMER_PENDING 1

#ifndef COARSE_GRAINED_TIMERS
#define COARSE_GRAINED_TIMERS
#endif

namespace prime {
namespace ssfnet {

class TCPSamplingTimer;
class TCPTimer;
class TCPSession;
class TCPMaster;
class TCPConnection;
class SimpleSocket;
class TCPSinkTimer;
class TCPRecvWindow;

//for SINK
class NewSackStack;
class TCPSinkSession;
class TCPSegment;

class IPOptionToAbove;

#define CLOSE_SSTHRESH_HALF     0x00000001
#define CLOSE_CWND_HALF         0x00000002
#define CLOSE_CWND_RESTART      0x00000004
#define CLOSE_CWND_INIT         0x00000008
#define CLOSE_CWND_ONE          0x00000010
#define CLOSE_SSTHRESH_HALVE    0x00000020
#define CLOSE_CWND_HALVE        0x00000040
#define THREE_QUARTER_SSTHRESH  0x00000080
#define CLOSE_CWND_HALF_WAY     0x00000100
#define CWND_HALF_WITH_MIN      0x00000200
#define TCP_IDLE                0x00000400
#define NO_OUTSTANDING_DATA     0x00000800

#define TCP_DEFAULT_MSL_TIMEOUT_FACTOR 2  // as per RFC 793

// Struct for High speed TCP
struct hstcp {
	double low_p;  // low_p
	double dec1;    // for computing the decrease parameter
	double dec2;    // for computing the decrease parameter
	double p1;      // for computing p
	double p2;      // for computing p
	double cwnd_last_;
	double increase_last_;

	hstcp() : low_p(0.0), dec1(0.0), dec2(0.0), p1(0.0), p2(0.0),
			cwnd_last_(0.0), increase_last_(0.0) { }
};

// A list to store the parameters
class ParamList_ {
 private:
	struct paramNode {
		int* addr;
		int value;
		int default_value;
		struct paramNode* next;
	};
	struct paramNode* head;
 public:
	ParamList_():head(NULL) {}
	~ParamList_();
	// Add this parameter to the list and set the value
	void set_param(int* address, int value);
	bool get_param(int* address, int* valuep);

	// Refresh all the values in the list
	void refresh_default();

	// Restore the
	void restore_default();
	void load_local();
};

//This class provide C++ interface to access the Linux parameters for specific congestion control algorithm
// The manager of Linux parameters for each TCP
class LinuxParamManager_ {
	private:
	ParamList_ localValues;
	static struct cc_list* find_cc_by_proto(const char* proto);
	static struct cc_param_list* find_param_by_proto_name(const char* proto, const char* name);
public:
	LinuxParamManager_(){};
	static bool set_default_param(const char* proto, const char* param, const int value);
	static bool get_default_param(const char* proto, const char* param, int* valuep);
	static bool query_param(const char* proto);
	bool set_param(const char* proto, const char* param, const int value);
	bool get_param(const char* proto, const char* param, int* valuep);
	void load_local() {localValues.load_local();};
	void restore_default() {localValues.restore_default();};
};

enum {
	TCP_STATE_CLOSED       = 0,
	TCP_STATE_LISTEN       = 1,
	TCP_STATE_SYN_SENT     = 2,
	TCP_STATE_SYN_RECEIVED = 3,
	TCP_STATE_ESTABLISHED  = 4,
	TCP_STATE_CLOSE_WAIT   = 5,
	TCP_STATE_CLOSING      = 6,
	TCP_STATE_FIN_WAIT_1   = 7,
	TCP_STATE_FIN_WAIT_2   = 8,
	TCP_STATE_LAST_ACK     = 9,
	TCP_STATE_TIME_WAIT    = 10
};

class TCPSession : public TransportSession {

	friend class TCPSamplingTimer;

public:

	// the constructor
	//TCPSession(TCPMaster* master, SimpleSocket* sock, TCPConnection* conn, NewSacker* acker_);

	// the constructor2
	TCPSession(TCPMaster* master, SimpleSocket* sock, TCPConnection* conn);

	//the destructor
	~TCPSession();

	TCPSendWindow* sndWnd;

	void getOptions(int& options_, struct OptionsEnabled* ops_en, TCPMessage* tcphdr);

	// initialize the agent
	void init();

	// Receive the message popped up from the ip session
	void receive(TCPMessage* tcphdr, void* extinfo);

	int getT_seqno() {return t_seqno_;}
	int getCurSeq() {return curSeq;}

	void processSYN(TCPMessage* tcphdr);
	bool processACK(TCPMessage* tcphdr, void* extinfo);
	void processFIN(TCPMessage* tcphdr);

	void sendData(uint32 seqno, uint32 datalen = 0, byte mask = 0,
			uint32 ackno = 0, bool need_calc_rtt = false,
			bool need_set_rextimeout = false, int flags_ = 0,
			struct OptionsEnabled* opts = NULL, uint32 data_length = 0, byte* final_data_to_send = NULL);
	virtual void sendMsg(uint64_t length, byte* msg, bool send_data_and_disconnect = false);
	virtual void send(uint64_t bytes_);
	void sendFirst();
	void sendFin(uint32 seqno, uint32 ackno, uint32 length=0, byte* msg = NULL);
	void sendSyn(uint32 seqno, uint32 ackno);
	void sendFinScheduleTimer(uint32 seqno, uint32 ackno, uint32 length = 0, byte* msg = NULL);
	void sendSynScheduleTimer(uint32 seqno, uint32 ackno);
	void sendBytes(int bytes_);
	void sendMuch(int force, int reason, int maxburst = 0);
	void output(int seqno, int reason = 0);
	void send_helper(int) { return; }
	void send_idle_helper() { return; }

	int window();
	void openCwnd();
	void touchCwnd();				// called whenever cwnd_ is changed, mark linux_.snd_cwnd_stamp
	void tcpModerateCwnd();
	void slowdown(int how);			// reduce cwnd/ssthresh
	double windowd();

	int packetsInFlight();
	void sndRecv(TCPMessage* tcphdr, struct OptionsEnabled& opt_en, int& options);
	bool installCongestionControl(const char* name);
	void removeCongestionControl();
	void save_from_linux();			// the variables that shall be saved from Linux every ack
	void loadToLinuxOnce();			// the variables that shall be loaded to Linux at boot or reset
	void loadToLinux();				// the variables that shall be loaded to Linux every acks
	bool isCongestion();			// whether the network is congested?
	void enterLoss();
	unsigned char ackProcessing(TCPMessage* tcph, unsigned char flag);  // process the ack: sequence#
	double limitedSlowStart(double cwnd, int max_ssthresh, double increment); // Limited slow-start for high windows
	double increaseParam();  	    // get increase parameter for current cwnd
	double decreaseParam();  	    // get decrease parameter for current cwnd
	void tcpFastRetransAlert(unsigned char flag);
	void sample();
	inline void tcp_set_ca_state(const u8 ca_state) {
		if ((linux_.icsk_ca_ops)&&(linux_.icsk_ca_ops->set_state))
			linux_.icsk_ca_ops->set_state(&linux_, ca_state);
		//printf("%lf: %d State: %d->%d cwnd:%d ssthresh:%d\n",tcpMaster->getNow().second(), this, linux_.icsk_ca_state, ca_state, linux_.snd_cwnd, linux_.snd_ssthresh);
		linux_.icsk_ca_state = ca_state;
	};
	void timeProcessing(TCPMessage* tcph, unsigned char flag, s32* seq_urtt_p, struct OptionsEnabled* ops_en_);

	void cancelRtxTimer();
	void resetRtxTimer(int mild, int backoff = 1);
	void setRtxTimer();

	void rttBackoff();   			// double multiplier
	void timeoutNonRtx(int tno);
	double rttTimeout();
	void timeout(int tno);
	void rttUpdate(double tao, unsigned long pkt_seq_no=0);		//rewrite the tcp.cc functions

#ifdef COARSE_GRAINED_TIMERS
	void slowTimeout();
#endif

	void reset();
	void rttInit();
	void setInitialWindow();
	void resetQOption();
	double initialWindow();

	uint32 calcAdvertisedWnd();
	void allocate_buffers();

	// Session management functions
	void applClose();
	void timeoutResend();
	virtual bool passiveOpen();
	virtual bool activeOpen();
	virtual int getID();

	// Accessors
	TCPConnection* getTcpConn() { return tcpConn; }
	TCPMaster* getMaster() { return tcpMaster; }
	TCPSinkSession* getSink() { return sink; }
	TCPSendWindow* getSndWnd () { return sndWnd; }
	SimpleSocket* getSock() { return tcpSock; }
	bool& getReported(){ return reported; }
	double getTs_Peer(){ return ts_peer_; }
	int getTSEnabled() { return ts_enabled; }
	uint32 getCwnd() { return (uint32)cwnd_;}
	uint32 getRTT() { return (uint32)t_rtt_;}

	// Mutators
    void setSink(TCPSinkSession* sink_) { sink = sink_; };

private:

	// Pointers to other entities
	TCPMaster* tcpMaster;	//pointer to master protocol session
	SimpleSocket* tcpSock;  // pointer to the active socket (if there is)
	TCPSinkSession *sink;	//pointer to sink code
public:
	int sessionNumber;

	// Enable ts_exchange
	int ts_enabled;
	FILE* filePointer;

public:
	bool reported;
private:
	double cwnd_;           	// current window
	double wnd_;				// maximum window size
	int maxcwnd_;           	// max # cwnd can ever be
	int wnd_option_;
	double awnd_;           	// averaged window
	double wnd_const_;
	double wnd_th_;         	// window "threshold"
	int cwnd_range_;        	// for determining when to recompute params
	int qs_cwnd_; 				// Initial window for Quick-Start
	int low_window_;       		// window for turning on high-speed TCP
	int high_window_;       	// target window for new response function
	int ncwndcuts_;   			// number of times cwnd was reduced for any reason -- sylvia
	int ncwndcuts1_;     		// number of times cwnd was reduced due to congestion (as opposed to idle periods
	double wnd_restart_;
	double wnd_init_;
	int T_full ; 				// last time the window was full
	int T_last ;
	int T_prev ;
	int T_start ;
	int W_used ;
	int W_timed ;
	int count_;             	// used in window increment algorithms
	int ect_;               	// turn on ect bit now?
	int rfc2988_;
	int wnd_init_option_;   	// 1 for using wnd_init_ - 2 for using large initial windows */
	int Backoffs;				//unused?

	uint32_t lastAck;
	uint32_t lastSeqno;

	int timerfix_;          	// set to true to update timer after update the RTT, instead of before
	double ts_peer_;        	// the most recent timestamp the peer sent
	double maxrto_;				// max value of an RTO
	double minrto_;         	// min value of an RTO
	int ts_resetRTO_;
	int ts_option_;         	// use RFC1323-like timestamps?
	//double *tss;            	// To store sent timestamps, with bugfix_ts_
	//XXXdouble ts_echo_;        	// the most recent timestamp the peer echoed
	int ts_echo_;
	int t_rtt_;             	// round trip time
	int t_srtt_;      			// smoothed round-trip time
	int t_rttvar_;    			// variance in round-trip time
	int T_SRTT_BITS;        	// exponent of weight for updating t_srtt_
	int bugfix_ts_;         	// 1 to enable timestamp heuristic, to allow multiple-fast-retransmits in special cases. From Andrei Gurtov. Not implemented yet
	double *tss_;     	       	// To store sent timestamps, with bugfix_ts_
	int tss_size_;         		// Current capacity of tss
	double t_rtxcur_;       	// current retransmit value
	int rttvar_exp_;        	// exponent of multiple for t_rtxcur_
	int T_RTTVAR_BITS;      	// exponent of weight for updating t_rttva
	int rtt_seq_;           	// seq # of timed seg if rtt_active_ is 1
	int rtt_active_;        	// 1 if a rtt sample is pending
	double rtt_ts_;         	// time at which rtt_seq_ was sent
	int use_rtt_;          		// Use RTT for timeout, for ECN-marked SYN-ACK
	double high_p_;         	// target drop rate for new response function
	int QOption_;    	      	// TCP quiescence option
	int EnblRTTCtr_; 			// are we using a corase grained timer?
	int srtt_init_;         	// initial value for computing t_srtt_
	int rttvar_init_;       	// initial value for computing t_rttvar_
	double rtxcur_init_;   		// initial value for t_rtxcur_

	int control_increase_ ; 	// If true, don't increase cwnd if sender is not window-limited
	int nrexmitpack_; 			// number of retransmited packets
	int nrexmitbytes_; 			// number of retransmited bytes
	double lastreset_;      	// W.N. Last time connection was reset - for detecting pkts from previous incarnations
	int ecn_backoff_;       	// True when retransmit timer should begin to be backed off
	int t_backoff_;
	double boot_time_;      	// where between 'ticks' this sytem came up
	int first_decrease_;    		// First decrease of congestion window. Used for decrease_num_ != 0.5
	int precisionReduce;  		// non-integer reduction of cwnd
	int necnresponses_; 		// number of times cwnd was reduced
	double k_parameter_;    	// k parameter in binomial controls
	double l_parameter_;    	// l parameter in binomial controls
	int congAction;       		// Congestion Action.  True to indicate that the sender responded to congestion
	hstcp hstcp_;               // HighSpeed TCP variables
	int ssthresh_;      	    // slow start threshold

 public:
	uint32_t curSeq;        		    // highest seqno "produced by app" in packets
	uint32_t t_seqno_;          		// sequence number of the packet to send next
	ScoreBoard2 *scb_;
	TCPConnection* tcpConn;		//src ip, src port, dest ip, dest port
 private:
	int a1_; int a2_; double a3_;
	int maxBurst;          		// max # packets can send back-2-back
	int maxseq_;             	// used for Karn algorithm. highest seqno sent so far
	struct tcp_sock linux_;		// Main data structure of a Linux TCP flow
	int nextPktsInFlight;		//the # of packets in flight allowed, if we need rate halving
	int highest_ack_;		    // not frozen during Fast Recovery
	double overhead_;
	int ndatapack_;				// number of data packets sent
	int ndatabytes_;			// number of data bytes sent
	int nackpack_;          	// number of ack packets received
	char* linuxTcpCA;			// the Linux TCP CA configured
	bool initialized_;			// a flag to record if a congestion control algorithm is initialized or not
	int last_ack_; 	        	// largest consecutive ACK, frozen during Fast Recovery
	LinuxParamManager_ paramManager;
	int repeatedAck;
	double tcp_tick_;       	// clock granularity
	int last_cwnd_action_;  	// CWND_ACTION_{TIMEOUT,DUPACK,ECN}
	int max_ssthresh_;     	 	// max value for ssthresh_
	double fcnt_;           	// used in window increment algorithms
	int prev_highest_ack_ ; 		// Used to determine if sender is window-limited
	double increaseNum;   		// factor for additive increase
	double decreaseNum;	   		// factor for multiplicative decrease
	int recover_;           	// highest pkt sent before dup acks, timeout, or source quench/ecn
	double high_decrease_;  	// decrease rate at target window
	int nrexmit_;           	// number of retransmit timeouts when there was data outstanding

	// Variables used for timeout
	int slow_start_restart_;	// boolean: re-init cwnd after connection goes idle.  On by default
	int restart_bugfix_;    	// ssthresh is cut down because of
								//   timeouts during a connection's idle period.
								//   Setting this boolean fixes this problem.
								//   For now, it is off by default.
	int frto_;
	int pipe_prev_; 			// window size when timeout last occurred
	int bugfix_ss_;         	// 1 to use window of one when SYN
								//   packet is dropped
	int frto_enabled_;      	// != 0 to enable F-RTO
	int sfrto_enabled_;     	// != 0 to enabled SACK-based F-RTO

	bool simultaneous_closing;  // Whether it is a simultaneous closing
	bool close_issued;			// Whether the upper layer has issued close

	FILE* agent_tracefile;

	// Timers
	TCPTimer* rtx_timer_;
	TCPTimer* conn_termination_timer_;
	TCPSamplingTimer* smp_timer;

	// The current state of the tcp session
public:
	int state;
private:
	bool changedContainer;
	//uint32 	start_seqno;
	bool peer_wnd_scalable;
	// If large window option is enabled, this is the shift counter to be used at the sender end
	uint8 sndwnd_scale;
	uint32 win_size;
	// Remote receive window size
	uint32 rcvWndSize;
	int rcv_next_;
	//XXX is this necessary, test before erasing!
	bool active;

	//used for counting the number of duplicate acks received when in the
	// the TCP closing stage
	double close_session_interval;
	bool close_session_enabled;
	uint32 seqnoForClosing;
	uint32 acknoForClosing;

	//used when a packet is lost during the 3-way handshake
	double open_session_interval;
	bool open_session_enabled;
	uint32 seqnoForOpening;
	uint32 acknoForOpening;
	int cached_options;
	struct OptionsEnabled cached_opts_en;

	// The timer count that lasts 2*msl
	VirtualTime msl_timer_count;

	// Translate the tcp state into a string
	static const char* get_state_name(int state);

	/*
	 * TCP state change
	 */
	// Change to CLOSED state
	int init_state_closed();
	// Change to SYN_SENT state
	//int init_state_syn_sent();
	// Change to SYN_RECEIVED state
	int initStateSynReceived(TCPMessage* tcphdr);
	// Change to LISTEN state
	void initStateListen();
	// Change to ESTABLISHED state
	int initStateEstablished();
	// Change to CLOSE_WAIT state
	int initStateCloseWait(TCPMessage* tcphdr);
	// Change to CLOSED state
	int initStateClosed();
	// Change to CLOSING state
	int initStateClosing();
	// Change to TIME_WAIT state
	int initStateTimeWait(TCPMessage* tcphdr);
};

class TCPSinkSession {
	friend class TCPMaster;

 public:

	TCPSinkSession(TCPMaster* master, SimpleSocket* sock,
		       TCPConnection* conn, TCPSession* sess_);

	// The destructor
	virtual ~TCPSinkSession() {}

	TCPMaster* tcp_master;	//pointer to master protocol session
	SimpleSocket* tcp_sock; // pointer to the active socket (if there is)
	TCPConnection* tcp_conn;	//src ip, src port, dest ip, dest port
	TCPRecvWindow* rcvWnd;
	TCPSession* snd_session;

	int** sacks_;
	int sacks_size;
	bool sacks_available;
	int max_sack_blocks_;   // used only by sack sinks
	int generate_dsacks_;   // used only by sack sinks

	void reset();
	int& maxsackblocks() { return max_sack_blocks_; }
	void rcvRecv(TCPMessage* pkt, uint32& mask, uint32& bytes_to_deliver, struct OptionsEnabled& opt_en, int& options,
			byte*& app_data, int& data_size);

	//members for DelAckSink
#ifdef SACK1_DELACK
	virtual void timeout(int tno);
	double interval_;
	TCPSinkTimer* delay_timer_;
#endif

	//virtual void recvBytes(TCPSegment* segment);

#ifdef COARSE_GRAINED_TIMERS
	void fastTimeout();
#endif

protected:

	void ack(TCPMessage*);//, IPADDR);

	int ts_echo_bugfix_;
	int ts_echo_rfc1323_;   // conforms to rfc1323 for timestamps echo
							// Added by Andrei Gurtov

	IPOptionToAbove* IPopt;

	//friend void NewSacker::configure(TCPSinkSession*);
	TCPMessage* save_;   // place to stash saved packet while delaying
	// used by DelAckSink
	int qs_enabled_; 		// to enable QuickStart
	int RFC2581_immediate_ack_;     // Used to generate ACKs immediately
	int bytes_;     		// for RFC2581-compliant gap-filling.
	double lastreset_;      // W.N. used for detecting packets
							// from previous incarnations
	int ecn_syn_;           // allow SYN/ACK packets to be ECN-capable

private:

#ifdef COARSE_GRAINED_TIMERS
	bool delayed_ack;
#endif
};

class CongestionControlManager {
 public:
	CongestionControlManager();
	//	int Register(struct tcp_congestion_ops* new_ops);
	struct tcp_congestion_ops* get_ops(const char* name);
	void dump();
	void scan();

 private:
	int num_;
	struct tcp_congestion_ops** ops_list;
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__TCP_AGENT_H__*/
