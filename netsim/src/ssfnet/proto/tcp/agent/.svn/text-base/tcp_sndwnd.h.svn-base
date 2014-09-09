#ifndef __TCP_SNDWND_H__
#define __TCP_SNDWND_H__

#include "os/ssfnet.h"
#include "os/data_message.h"
#include "tcp_seqwnd.h"

namespace prime {
namespace ssfnet {

class TCPSession;
class TCPSegment;

/**
 * \brief The sliding window for TCP sender.
 */
class TCPSendWindow: public TCPSeqWindow {
 public:
  // The constructor
  TCPSendWindow(TCPSession* sess, uint32 initseq, uint64 bufsiz, uint32 wndsiz, \
          TCPSession* sess_);

  // The destructor
  ~TCPSendWindow();

  // Generate a message with designated length and sequence number
  DataMessage* generate(uint32 seqno, uint32 mss_length, uint32& length_to_send);

  // Request to send new data. Returns true if the data was added to the buffer*/
  bool requestToSend(byte* msg, uint64 length);

 public:

  // Returns how much of the buffer is free
  uint64 freeInBuffer();

  // Translate from packet to sequence number
  uint32 pktnoToSeqno(int pktNo, bool& found);

  // Translate from seqno to pkt number
  uint32 acknoToPktno(uint32_t seqNo);

  // Erase packets already acknowledged
  void cleanWindow(uint32_t highest_ack_);

  // Accessors
  void setISSPeer(int iss_);
  int getBytesToSendSim();

  // Mutators
  void setBytesToSendSim(int bytes);

 protected:

    void addToBuffer(uint32 length);

 protected:
    TCPSession* ownerSession;

    // Buffer size
    uint64 bufferSize;

    // How many bytes are in the buffer waiting to be sent out
    uint64 lengthInBuffer;

    // How many bytes have been put into the buffer
    uint64 lengthBuffered;

    // Pointer to the head of the message list
    TCPSegment* headChunk;

    // Point to the tail of the message list
    TCPSegment* tailChunk;

    // pktno of the first packet stored in linked list
    uint32_t start_pkt;

    // seqno of the first packet stored in linked list
    uint32_t start_seqno;

    // ISS of peer //XXX needed?
    uint32_t ISS_peer;

    // Pointer to TCP session
    TCPSession* tcpSession;

    // Points to rawdata of a DataMessage
    //byte* firstRawData;

    // Bytes remaining to send in simulation mode
    int bytesToSendSim;

    //Time to delete the head
    //double timeToDeleteHead;
    //bool deletionTimeSet;
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__TCP_SNDWND_H__*/

