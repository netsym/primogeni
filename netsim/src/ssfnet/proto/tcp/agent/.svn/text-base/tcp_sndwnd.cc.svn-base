#include "tcp_sndwnd.h"
#include "tcp_session.h"
#include "os/data_message.h"
#include "os/logger.h"
#include "../../../net/host.h"
#include "tcp_rcvwnd.h"
#include <math.h>

//#define LOG_WARN(X) std::cout<<"["<<__LINE__<<"]"<<X;
//#define LOG_DEBUG(X) std::cout<<"["<<__LINE__<<"]"<<X;
//#define LOG_DEBUG(X) std::cout << X;

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(TCPSendWindow);

TCPSendWindow::TCPSendWindow(TCPSession* sess, uint32 initseq, uint64 bufsiz,
        uint32 wndsiz, TCPSession* sess_) :
  TCPSeqWindow(initseq, wndsiz), ownerSession(sess), bufferSize(bufsiz),
  lengthInBuffer(0),lengthBuffered(0), headChunk(0), tailChunk(0),
  start_pkt(1), start_seqno(0), ISS_peer(0), tcpSession(sess_),
  bytesToSendSim(0)
{

    bufferSize = 10000000000;

    start_seqno = tcpSession->getMaster()->getISS() + 1;
    LOG_DEBUG("startseqno set to " << start_seqno << endl);

    // make sure window size must be less than buffer size
    if(wndsiz > bufsiz) {
        //printf("ERROR: TCP window size (%u) must be no larger than buffer size (%u).\n",
          //      wndsiz, bufsiz);
    }
}

TCPSendWindow::~TCPSendWindow()
{
    //let's traverse the data structure
    TCPSegment* chunk = headChunk;
    int counter = 1;

    if (chunk == NULL) {
        LOG_DEBUG("nothing to erase in send window" << endl);
        return;
    }

    while(chunk!=NULL){
        LOG_DEBUG("sndwnd TRAVERSING...[" << "][counter=" << counter << "] address=" << chunk << endl);
        chunk = chunk->next;
        counter++;
    }

    chunk = headChunk;
    // delete all window content if necessary
    while(chunk!=NULL){
        if(chunk->msg != NULL){
            TCPSegment* aux = chunk;
            chunk = chunk->next;
            delete aux;
        } else {
            delete chunk;
            chunk = NULL;
        }
    }
}

void TCPSendWindow::cleanWindow(uint32_t last_ack_){
    TCPSegment* chunk = headChunk;
    uint32 mss = ownerSession->getMaster()->getMSS();
    int starPkt = this->start_pkt;
    uint32 starSeqno = this->start_seqno;
    if(chunk != NULL){
        //std::cout << "[" << this->tcpSession->getMaster()->getNow().second() << "] " <<
        //	"cleaning send window, startseqno set to[before 1] " << start_seqno << " starSeqno=" << starSeqno <<
        //    " chunk->size()=" << chunk->length << endl;
    }
    byte* firstRawData;

    if(chunk !=NULL && (last_ack_ >= ( (int) starSeqno + chunk->length + 300000 ) )){ //300000 is added to that we do not clean the window prematurely
        starSeqno = starSeqno + chunk->length;
        starPkt = starPkt + (int)(chunk->length/mss) + (chunk->length%mss ? 1 : 0);

        if(chunk->next != NULL){
            //LOG_DEBUG("\tdele and tail is not NULL\n");
            headChunk = chunk->next;
            firstRawData = chunk->next->msg;
        }
        else{
            //LOG_DEBUG("\tdele and tail is NULL\n");
            headChunk = tailChunk = NULL;
            firstRawData = NULL;
        }


        this->start_seqno = starSeqno;//<<<<<<<<<<<<<--------XXX
        this->start_pkt = starPkt;

        delete chunk;
    }
    return;
}

DataMessage* TCPSendWindow::generate(uint32 seqno, uint32 mss_length, uint32& length_to_send)
{
    LOG_DEBUG("[" << this << "] in generate: seqno to send=" << seqno << " starSeqno=" << this->start_seqno << endl);

    int pktLength = 0;

    // calculate the offset from the start
    uint32 offset = this->start_seqno - 1;

    // find the right block in the list
    TCPSegment* node = headChunk;

    // Return is there is no data to send
    //if(node == NULL || seqno == 0)
    if(node == NULL){
        LOG_DEBUG("node is null, seqno=" << seqno << endl)
        return NULL;
    }

    LOG_DEBUG("before while" << " seqno=" << seqno << " start_seqno=" << start_seqno <<
                    " offset=" << offset << endl);

    while(node) {
        LOG_DEBUG("node is not empty, size=" << node->length << " seqno=" << seqno <<
                " offset=" << offset << endl);

        //If the seqno falls anywhere between this node then this is the one
        if(seqno < offset + node->length){
            uint64_t length_left_ = node->length - seqno + offset;
            LOG_DEBUG("computing length_left_=" << length_left_ << endl);
            if(length_left_ > mss_length){
            	length_to_send = mss_length;
                LOG_DEBUG("length_to_send 1=" << length_to_send << endl);
            } else {
            	length_to_send = length_left_;
                LOG_DEBUG("length_to_send 2=" << length_to_send << endl);
            }
            //LOG_DEBUG("in generate:THIS IS THE NODE!!!!!!! length_to_send=" << length_to_send << endl);
            break;
        }
        //LOG_DEBUG("in generate:GO TO OTHER NODE" << endl);
        //Increment offset
        offset += node->length;
        //Move to the next node
        node = node->next;
    }

    // Check whether this is simulation data, in which case this function immediately returns
    if(node == NULL) {
        LOG_WARN("No more data to send" << endl);
        return NULL;
    }
    if(node->msg == NULL){

    	LOG_DEBUG("msg GENERATED: length_to_send" << length_to_send << endl);
        assert(length_to_send>0);
        return new DataMessage(length_to_send, NULL);
    }

    assert(node);

    //LOG_DEBUG("IN GENERATE: before if nodesize=" << node->size() << " offset=" << offset
            //<< " seqno=" << seqno << endl);
    if(offset + node->length - seqno > 0) {
        byte* payload;
        // the current node can satisfy the data length requested.
        if(node->length) {
            if(node->msg != NULL){
                //emu
                LOG_DEBUG("sndwnd emu: node_size=" << node->length << " node->rawdata=" << node->msg << endl);
            } else {
                //simu
                LOG_DEBUG("sndwnd simu: node_size=" << node->length << endl);
            }
            //assert(length_to_send>0);
            payload = new byte[length_to_send];

            pktLength = length_to_send;
            memcpy((byte*)payload, node->msg + seqno - offset, pktLength);
            LOG_DEBUG("send packet of pktLength=" << pktLength <<
                    " offset from rawdata=" << offset << endl);
        } else {
            LOG_ERROR("not supposed to happen");
            payload = 0;
        }
        LOG_DEBUG("msg GENERATED: pktLength" << pktLength << endl);
        return new DataMessage(pktLength, payload);
    }

    LOG_ERROR("What happened?"<<endl)

    return NULL;
}

uint64 TCPSendWindow::freeInBuffer()
{
      LOG_DEBUG("in sndswnd, buffsize=" << bufferSize <<  "lengthbuff=" << lengthBuffered << endl);
      return bufferSize - lengthBuffered;
}

bool TCPSendWindow::requestToSend(byte* msg, uint64 msg_len)
{
    // Here we insert the bytes sent into the send window

    byte* firstRawData = 0;

    // If there is enough place in the window, store all bytes there
    if(freeInBuffer() >= msg_len){
        TCPSegment* datachk = new TCPSegment(0 , msg_len, msg);

        if(msg){
                //LOG_DEBUG("[" << this->tcpSession->sessionID << "] 6  msg=" << *msg << " msg_len=" << msg_len << " freeInBuffer()=" << freeInBuffer() << endl);
        }
        else {
                //LOG_DEBUG("7  msg_len=" << msg_len << " freeInBuffer()=" << freeInBuffer() << endl);
                if(msg==NULL){
                    assert(datachk->msg == NULL);
                }
        }

        //LOG_DEBUG("push: [" << this->tcpSession->getMaster()->inHost()->getDefaultIP()<< "] [" << this->tcpSession->getMaster()->getNow().second() <<
            //    "] adding a new chunk at address=" << datachk <<
                //" length=" << msg_len << endl);

        if(tailChunk) {
            LOG_DEBUG("TAIL CHUNK is full" << endl);
            assert(headChunk);
            tailChunk->next = datachk;
            tailChunk = datachk;
        } else {
            // The window is empty!
            LOG_DEBUG("TAIL CHUNK is not full" << endl);
            assert(!firstRawData && !headChunk);
            headChunk = tailChunk = datachk;
            assert(headChunk);
            assert(tailChunk);
            firstRawData = datachk->msg;
        }

        // Update variables to reflect the newly added bytes
        addToBuffer(msg_len);
        //LOG_DEBUG("in REQUEST TO SEND: real_length=%d\n" << datachk->real_length);
        return true;
    }
    return false;
}

void TCPSendWindow::setISSPeer(int iss_){
    this->ISS_peer = iss_;
}

int TCPSendWindow::getBytesToSendSim()
{
    return bytesToSendSim;
}

void TCPSendWindow::setBytesToSendSim(int bytes)
{
    bytesToSendSim = bytes;
    LOG_DEBUG("BYTESTOSEND in sndwnd is set to" << bytesToSendSim);
}

uint32 TCPSendWindow::acknoToPktno(uint32_t seqNo){
    //Used when an ACK is received
	uint32_t pktno = 0;
    TCPSegment* chunk = headChunk;
    uint32_t mss = this->tcpSession->getMaster()->getMSS();
//    bool inflate = false; //for enabling large transfers, still in testing

    //test whether this seqNo should be 'inflated'
    LOG_DEBUG("[debug large] [" << this->tcpSession->getMaster()->getNow().second() << "] [" <<
    		this->tcpSession->getMaster()->inHost()->getUID() << "] " <<
    		"testing for inflation, t_seqno_=" << this->tcpSession->t_seqno_ <<
    		" seqNo=" << seqNo << " seqNo%mss=" << seqNo%mss <<
    		" max sent in first round=" << (((uint32_t)pow(2,32) - (this->start_seqno - 1))/mss) <<
    		" rhs=" << (mss - ((uint32_t)pow(2,32) - (this->start_seqno - 1))%mss) << endl);

    //for enabling large transfers, still in testing
/*    if( (this->tcpSession->t_seqno_ > (((uint32_t)pow(2,32) - (this->start_seqno - 1))/mss)) &&
    	(seqNo%mss == (mss - ((uint32_t)pow(2,32) - (this->start_seqno - 1))%mss)) )
    {
    	//this test means that we have already sent packet sequences beyond what a uint32_t type can support
    	cout << "\t[debug large] inflating...\n" << endl;
    	inflate = true ;
    }*/

    int startPkt = this->start_pkt;
    uint32 startSeqno = this->start_seqno;

    LOG_DEBUG("in processack, [acknoToPktno 1] [" << this->tcpSession->getMaster()->inHost()->getUID() <<
            "] in acknoToPktno, startPkt=" << startPkt << " startSeqno=" << startSeqno <<
            " received " << seqNo << " to translate to pktno" << endl);

    if(chunk == NULL && seqNo == 1)
        return 1;
    else {
        while(chunk != NULL){
            LOG_DEBUG("\t***[debug interactions] acknoToPktno: seqNo=" << seqNo << " startSeqno=" <<
                    startSeqno << " chunk->size()=" << chunk->length <<
                    " in " << this->tcpSession->getMaster()->inHost()->getDefaultIP() << endl);
            if(seqNo < (startSeqno + chunk->length)){
            	//for enabling large transfers, still in testing
 //           	if(inflate){
 //           		pktno = (seqNo - startSeqno)/mss + startPkt + ((uint32_t)pow(2,32) - (this->start_seqno - 1))/mss;
 //           		cout << "\t\t[debug large] after inflation, pktno=" << pktno << endl;
 //           	} else {
            		pktno = (seqNo - startSeqno)/mss + startPkt;
 //           	}
                LOG_DEBUG("in processack, [acknoToPktno 2] [" <<
                        "] translated to " << pktno << " seqNo=" << seqNo <<
                        " startSeqno" << startSeqno << " mss=" << mss << " startPkt=" << startPkt << endl);
                return pktno;
            } else if(seqNo == (startSeqno + chunk->length)) {
            	//for enabling large transfers, still in testing
//            	if(inflate){
//            		pktno = (seqNo - startSeqno)/mss + startPkt + 1;
//            	} else {
            		pktno = (seqNo - startSeqno)/mss + startPkt + 1;
//            	}
                LOG_DEBUG("in processack, [acknoToPktno 3]  [" <<
                    "] translated to " << pktno << " seqNo=" << seqNo <<
                    " chunk->size()=" << chunk->length << " startSeqno=" << startSeqno << endl);
                return pktno;
            } else {
                LOG_DEBUG("\t\t***[debug interactions]  [" <<
                        "] in acknoToPktno 3, translated to " << pktno << endl);
            }

            if(chunk->msg){
                uint32 div = (chunk->length)/mss;
                //xxx this caused problems before: startPkt = startPkt + div + (div%mss ? 1 : 0);
                startPkt = startPkt + div + (chunk->length%mss ? 1 : 0);
                startSeqno += chunk->length;

                // Let's got to the next block of data
                chunk = chunk->next;

                LOG_DEBUG("in processack, [acknoToPktno 4]  [" <<
                        "] in acknoToPktno 4, chunk->size()=" << chunk->length << " startPkt=" <<
                        startPkt << " div=" << div << " startSeqno=" << startSeqno << " div%mss=" << div%mss <<
                        " nextsize=" << chunk->length << endl);

            } else {
                LOG_WARN("1 acknowledging data never sent!" << endl);
                break;
            }
        }
    }
    LOG_DEBUG("[debug conversion] [" << this->tcpSession->getMaster()->inHost()->getUID() <<
            "] in acknoToPktno, seqno "<< seqNo << " translated to " << pktno << endl);
    return pktno;
}

/*
 *  To send real packets
 */

uint32 TCPSendWindow::pktnoToSeqno(int pktNo, bool& found)
{
    //Used when sending a data packet
    uint32_t seqno = 0;
    TCPSegment* chunk = headChunk;
    uint32_t mss = ownerSession->getMaster()->getMSS();

    uint32_t starPkt = this->start_pkt;
    uint32_t starSeqno = this->start_seqno;

    // Traverse the list starting from the head
    while(chunk != NULL)
    {
    	LOG_DEBUG("\t[" << this->tcpSession->getMaster()->getNow().second() << "] " <<
        		"[debug pktnotoseqno] [pktnoToSeqno 2]  [" << this->tcpSession->getMaster()->getNow().second() << "]: pktNo=" <<
                pktNo << " starPkt=" <<
                starPkt << " chunk->size()=" << chunk->length << " mss=" << mss << " chunk->length/mss=" <<
                chunk->length/(uint64_t)mss << endl);

        if((uint64_t)pktNo < (uint64_t) starPkt + (uint64_t) (chunk->length/mss + (chunk->length%mss ? 1 : 0) ) ){
            seqno = starSeqno + mss * (pktNo - starPkt);
            found = true;
            LOG_DEBUG("[" << this->tcpSession->getMaster()->getNow().second() << "] " <<
            		"[debug pktnotoseqno] [pktnoToSeqno 4] : starSeqno=" << starSeqno << " starPkt=" << starPkt <<
                                " chunk->size()" << chunk->length << " mss=" << mss << " seqno=" << seqno <<
                                " pktNo=" << pktNo << endl);
            break;
        }

        if(chunk->msg){
            starSeqno += chunk->length;
            starPkt = starPkt + (uint32)((chunk->length)/mss) + (uint32)((chunk->length)%mss?1:0);
            chunk = chunk->next;
       //     LOG_DEBUG("[" << this->tcpSession->getMaster()->getNow().second() << "] " <<
         //   		"[debug pktnotoseqno] [pktnoToSeqno 3] : starSeqno=" << starSeqno << " starPkt=" << starPkt <<
           //         " chunk->size()" << chunk->length << " mss=" << mss << " pktNo=" << pktNo << endl);
            //LOG_DEBUG("8 : starSeqno=" << starSeqno << " starPkt=" << starPkt << endl);
        } else {
            fflush(stdout);
            starSeqno += chunk->length;
            starPkt = starPkt + (uint32)((chunk->length)/mss) + (uint32)((chunk->length)%mss?1:0);
            chunk = chunk->next;
            seqno = starSeqno;
        //    LOG_DEBUG("[" << this->tcpSession->getMaster()->getNow().second() << "] " <<
          //  		"[debug pktnotoseqno] [pktnoToSeqno 5] : starSeqno=" << starSeqno << " starPkt=" << starPkt <<
            //		" pktNo=" << pktNo << endl);
        }
    }

    //LOG_DEBUG("\t\tseqno to send=" << seqno << "\n");
    return seqno;
}

void TCPSendWindow::addToBuffer(uint32 length)
{
    assert(lengthInBuffer + length <= bufferSize);

    // update how many bytes in the buffer and being requested.
    lengthInBuffer += length;
    //length_in_request -= length;
    lengthBuffered += length;
}

}; // namespace ssfnet
}; // namespace prime

