/**
 * \file new_tcp_rcvwnd.h
 * \brief Header file for TCPRecvWindow class.
 */

#ifndef __NEW_TCP_RCVWND_H__
#define __NEW_TCP_RCVWND_H__

#include "os/data_message.h"
#include "tcp_seqwnd.h"
#include "tcp_message.h"
#include "tcp_session.h"

namespace prime {
namespace ssfnet {

class TCPSinkSession;

// brief A TCP segment received by the TCP receiver.
// The received TCP segments are organized as a linked list in the
// receive window

class NewSackStack {
protected:
	int size_;
	int cnt_;
	struct Sf_Entry {
		int left_;
		int right_;
	} *SFE_;
public:
	NewSackStack(int);         // create a SackStack of size (int)
	~NewSackStack();
	int& head_right(int n = 0) { return SFE_[n].right_; }
	int& head_left(int n = 0) { return SFE_[n].left_; }
	int cnt() { return cnt_; }      // how big the stack is
	void reset() {
		register int i;
		for (i = 0; i < cnt_; i++)
			SFE_[i].left_ = SFE_[i].right_ = -1;
		cnt_ = 0;
	}

	inline void push(int n = 0) {
		if (cnt_ >= size_) cnt_ = size_ - 1;  // overflow check
		register int i;
		for (i = cnt_-1; i >= n; i--)
			SFE_[i+1] = SFE_[i];    // not efficient for big size
		cnt_++;
	}
	inline void pop(int n = 0) {
		register int i;
		for (i = n; i < cnt_-1; i++)
			SFE_[i] = SFE_[i+1];    // not efficient for big size
		SFE_[i].left_ = SFE_[i].right_ = -1;
		cnt_--;
	}
};

class TCPSegment {
 public:
	// The default constructor
	TCPSegment() {}

	// The constructor with given fields
	TCPSegment(uint32 sno, uint64_t len, byte* m, TCPSegment* nxt = 0) :
		seqno(sno), length(len), msg(m), next(nxt), previous(NULL), sentUp(false){}

	// Set the fields
	void set(uint32 sno, uint32 len, byte* m, TCPSegment* nxt = 0) {
		seqno = sno; length = len; msg = m; next = nxt;
	}
    
 public:
	uint32_t seqno; 		 // The sequence number of the first byte of the segment.
	uint64_t length; 		 // The length of the segment.
	byte* msg; 			 // Point to the data; NULL if it's fake data.
	TCPSegment* next;    // Point to the next segment.
	TCPSegment* previous;// Point to the next segment.
	bool sentUp;
};
  
 // brief The sliding window for TCP receiver.
 //   We use a linked list as the buffer to store received packets. When
 //   the upper layer requests to receive data, it will pop up data from
 //   the linked list

class TCPRecvWindow{
public:
	TCPRecvWindow(uint32 initseq, uint32 winsiz, TCPSession* sess_);

	~TCPRecvWindow();

	void setStart(uint32 seqno);

	void setSYN(bool syn);
  
	// Shift the window by the given offset permanently
	void seq_shift(uint32 offset) { start_seqno += offset; }

	// Like start method, but also uses SYN & FIN segments; the method
	//      is used for receiving window
	uint32 expect() {return start_seqno+syn_included+fin_included; }
	uint32_t Seqno();
	inline int seqnoBefore() const { return (before_next_); }

	void update_ts(uint32_t seqno, int ts, int rfc1323 = 0);
	uint32_t update(uint32_t seqno, uint32_t numBytes, DataMessage* dm, byte*& app_data, int& data_size);

	int** append_ack(TCPMessage*, uint32_t oldSeqno, int& size);
	void reset();
	double ts_to_echo() { return ts_to_echo_;}
	//int ecn_unacked() { return ecn_unacked_;}
	inline int Maxseen() const { return (maxseen_); }
	//void resize_buffers(int sz);  // resize the seen_ buffer
	void configure(TCPSinkSession*);
	void cleanWindow();

	//Linked list related methods
	int addToList(int seq, int numBytes, DataMessage* dm, byte*& app_data, int& data_size);
	bool searchSeq(int seq);
	TCPSegment* findFirstRightHole(int start_seqno, bool& found);

 //private:

	// For updating timestamps, from Andrei Gurtov.
	uint32_t last_ack_sent_;
	// Starting sequence number of the window
	uint32_t start_seqno;
	// Starting sequence number of the window
	//uint32 peer_ISN;
	// Size of the window in Bytes
	uint32 win_size;
	// The highest sequence number received //XXX is this really necessary?
	uint32 highest_seqno;
	TCPSession* tcpSession;

	// A buffer to put received segments
	TCPSegment* seen_; 		//this is the head
	// A pointer to the TcpSegment which is the left edge of the window
	TCPSegment* left_edge;   //-->next = next_
	// A pointer to the TcpSegment which is the right edge of the window
	TCPSegment* right_edge;  //-->maxseen_

	// next seqno expected
	uint32_t next_;
	// the seqno of one packet before next
	uint32_t before_next_;
	// max packet number seen
	uint32_t maxseen_;

	double ts_to_echo_;
	// A duplicate packet
	bool is_dup_;
	int base_nblocks_;
	int* dsacks_;           // Generate DSACK blocks.
	NewSackStack *sf_;

	// The buffer (owned by application) to receive data
	byte* appl_rcvbuf;

	// Total number of bytes expected to receive
	uint32 appl_rcvbuf_size;

	// The size of the received data (that has been put into the
    //  receive buffer)
	uint32 appl_data_rcvd;

	// How many bytes are used in window
	uint32 used_size;

	// Whether SYN is in window
	int syn_included;

	// Whether FIN is in window
	int fin_included;

 private:
	bool firstPktReceived;
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__TCP_RCVWND_H__*/
