#include "tcp_rcvwnd.h"
#include "os/logger.h"

//#define LOG_WARN(X) std::cout<<"["<<__LINE__<<"]"<<X;
//#define LOG_DEBUG(X) std::cout<<"["<<__LINE__<<"]"<<X;


namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(TCPRecvWindow);

TCPRecvWindow::TCPRecvWindow(uint32 initseq, uint32 winsiz, TCPSession* sess_) :
		last_ack_sent_(0), start_seqno(initseq), win_size(winsiz),
		highest_seqno(initseq), tcpSession(sess_), seen_(0),
		left_edge(0), right_edge(0), next_(0), before_next_(0),
		maxseen_(0), ts_to_echo_(0), is_dup_(false), base_nblocks_(-1),
		dsacks_(0), sf_(0), appl_rcvbuf(0), appl_rcvbuf_size(0),
		appl_data_rcvd(0), used_size(0), syn_included(0), fin_included(0),
		firstPktReceived(false) {}

TCPRecvWindow::~TCPRecvWindow()
{
	while(seen_) {
		TCPSegment* seg = seen_->next;
		if(seen_->msg) delete[] seen_->msg;
		delete seen_;
		seen_ = seg;
	}
}

void TCPRecvWindow::setSYN(bool syn) {
	if(syn)
		assert(used_size == 0);
	else
		seq_shift(1);
	syn_included = syn ? 1 : 0;
}

void TCPRecvWindow::update_ts(uint32_t seqno, int ts, int rfc1323)
{
	// update timestamp if segment advances with ACK.
	// Code changed by Andrei Gurtov.
	if (rfc1323 && seqno == last_ack_sent_ + 1){
		ts_to_echo_ = ts;
	}
	else if (ts >= ts_to_echo_ && seqno <= last_ack_sent_ + 1){
		//rfc1323-bis, update timestamps from duplicate segments
		ts_to_echo_ = ts;
	}
}

int** TCPRecvWindow::append_ack(TCPMessage* ch, uint32_t old_seqno, int& size)
{
	// ch and h are the common and tcp headers of the ack being constructed
	// old_seqno is the sequence # of the packet we just got

	int** sack;
	int sack_index;
	TCPSegment* sack_left = NULL;
	TCPSegment* sack_right = NULL;
	uint32_t sack_left_int = 0;
	uint32_t sack_right_int = 0;
	uint32_t recent_sack_left, recent_sack_right;

	// 3 rows 2 columns
	sack = new int* [3];
	*(sack+0) = new int [2];
	*(sack+1) = new int [2];
	*(sack+2) = new int [2];

	// Initialization
	for(int i=0; i<3; i++){
		for(int j=0; j<2; j++){
			sack[i][j] = -1;
		}
	}

	// the last in-order packet seen (i.e. the cumulative ACK # - 1)
	uint32_t seqno = Seqno();

	// initialization; sack_index=0 and sack_{left,right}= -1
	sack_index = 0;

	if (old_seqno < 0) {
		//LOG_ERROR("Error: invalid packet number " << old_seqno);
	} else if (seqno >= maxseen_ && (sf_->cnt() != 0)){
		// if the Cumulative ACK seqno is at or beyond the right edge
		// of the window, and if the NewSackStack is not empty, reset it
		// (empty it)
		sf_->reset();
	} else if (( (seqno < maxseen_) || is_dup_ ) && (base_nblocks_ > 0)) {
		// Otherwise, if the received packet is to the left of
		// the right edge of the receive window (but not at
		// the right edge), OR if it is a duplicate, AND we
		// can have 1 or more Sack blocks, then execute the
		// following, which computes the most recent Sack
		// block

		if ((*dsacks_) && is_dup_) {

			// Record the DSACK Block
			sack[sack_index][0] = old_seqno;
			assert(ch->payload() != NULL);
			sack[sack_index][1] = old_seqno + ch->payload()->size();

			// record the block
			sack_index++;
		}

		if (sack_index >= base_nblocks_) {
			//LOG_ERROR("Error: can't use DSACK with less than 2 SACK blocks" << endl);
		} else {
			sack_right = NULL;

			// look rightward for first hole
			// start at the current packet
			bool found = false;

			sack_right = this->findFirstRightHole(old_seqno, found);

			if(found == true){
				//sack_right = this->findFirstRightHole(old_seqno, found);
				LOG_DEBUG("[debug sack] found the first right hole:" <<  endl);
			} else {
				// if there's no hole set the right edge of the sack
				// to be the next expected packet
				sack_right = right_edge;
				if(sack_right != NULL){
					LOG_DEBUG("[debug sack] did not find the first right hole, sack right is not null:" << endl);
				} else {
					LOG_DEBUG("[debug sack] did not find the first right hole, sack right is null" << endl);
				}
			}

						// if the current packet's seqno is smaller than the
			// left edge of the window, set the sack_left to 0
			if(sack_right != NULL){
				//printf("	*** BEFORE: sack_right=%d next=%d seqnoPKT=%d\n",
				//	(sack_right->seqno+960-1)/960, (seqno+960-1)/960, (ch->seqno+960-1)/960);
			}
			if (old_seqno <= seqno) {
				sack_left = NULL;
				// don't record/send the block
			} else {
				// look leftward from right edge for first hole

				TCPSegment* aux = NULL;

				if(sack_right != NULL){
					LOG_DEBUG("[debug sack] " << endl);
					for (aux = sack_right; aux->seqno > (uint32) seqno;){// aux = aux->previous) {
						if(aux->previous != NULL) {
							// if there is no node on my left, then break with no sack_left assigned
							if(aux->seqno == (uint32)(aux->previous->seqno + aux->previous->length)){
								//there is not hole so do not assign sack_left
								//aux = aux->previous;
								if(aux->previous != NULL){
									aux = aux->previous;
								} else {
									// if there is no node on my left, then break with no sack_left assigned
									break;
								}
							} else {
								sack_left = aux;
								break;
							}
						}
						else {
							break;
						}
					}
				} else {
					goto error;
				}

				if(sack_left != NULL && sack_right != NULL){
					sack[sack_index][0] = sack_left->seqno; //MSS
					sack[sack_index][1] = sack_right->seqno + sack_right->length;
				} else {
					//LOG_WARN("hit an strange error!" << endl);
					goto error;
				}

				// record the block
				sack_index++;
			}
			error:

			//these are number now
			if(sack_left != NULL)
				recent_sack_left = sack_left->seqno;
			else
				recent_sack_left = -1;
			if(sack_right != NULL)
				recent_sack_right = sack_right->seqno + sack_right->length;
			else
				recent_sack_right = -1;

			// first sack block is built, check the others
			// make sure that if max_sack_blocks has been made
			// large from tcl we don't over-run the stuff we
			// allocated in Sacker::Sacker()
			int k = 0;

			while (sack_index < base_nblocks_) {

				sack_left_int = sf_->head_left(k);
				sack_right_int = sf_->head_right(k);

				// no more history
				if (sack_left_int < 0 || sack_right < 0 ||
						sack_right_int > maxseen_ + 1)
					break;

				// newest ack "covers up" this one

				if (recent_sack_left <= sack_left_int &&
						recent_sack_right >= sack_right_int) {
					sf_->pop(k);
					continue;
				}

				// store the old sack (i.e. move it down one)
				sack[sack_index][0] = sack_left_int;
				sack[sack_index][1] = sack_right_int;

				sack_index++;
				k++;
			}
			if (old_seqno > seqno) {
				// put most recent block onto stack
				sf_->push();
				// this just moves things down 1 from the
				// beginning, but it doesn't push any values
				// on the stack
				sf_->head_left() = recent_sack_left;
				sf_->head_right() = recent_sack_right;
				// this part stores the left/right values at
				// the top of the stack (slot 0)
			}

		} // this '}' is for the DSACK base_nblocks_ >= test;
		// (didn't feel like re-inden all the code and
		// causing a large diff)*/
	}
	size = sack_index;
	return sack;
}

void TCPRecvWindow::configure(TCPSinkSession* sink)
{
	if (sink == NULL) {
		fprintf(stderr, "warning: Sacker::configure(): no TCP sink!\n");
	    return;
	}

	int& nblocks = sink->max_sack_blocks_;
	if (int(nblocks) > NSA) {
		fprintf(stderr, "warning(Sacker::configure): TCP header limits number of SACK blocks to %d, not %d\n",
				NSA, int(nblocks));
	    nblocks = NSA;
	}
	sf_ = new NewSackStack(int(nblocks));
	base_nblocks_ = int(nblocks);
	dsacks_ = &(sink->generate_dsacks_);
}

TCPSegment* TCPRecvWindow::findFirstRightHole(int start_seqno, bool& found)
{
	TCPSegment* curr = seen_;
	if(curr == NULL){
		return curr;
	}

	while(curr->next != NULL) {
		if(curr->seqno == (uint32) start_seqno) {
			break;
		}
		curr = curr->next;
	}

	//return a pointer to the first block BEFORE the rightward hole
	while(curr->next != NULL){
		if(curr->seqno + curr->length == curr->next->seqno){
			curr = curr->next;
		}
		else {
			found = true;
			break;
		}
	}
	return curr;
}

void TCPRecvWindow::reset()
{
	sf_->reset();
	next_ = 0;
	maxseen_ = 0;
	memset(seen_, 0, (sizeof(int) * (win_size + 1)));
}

bool TCPRecvWindow::searchSeq(int seq){
	TCPSegment* current = seen_;
	if(current == NULL) return false;
	while(current->next != NULL){
		if(current->seqno == (uint32)seq){
			return true;
		}
		current = current->next;
	}
	if(current->seqno == (uint32)seq) {
		return true;
	}
	return false;
}

uint32_t TCPRecvWindow::Seqno()
{
	return (next_);
}

//seq=sequence number of the packet, numbytes is the number of REAL pkts and dm is the payload
int TCPRecvWindow::addToList(int seq, int numBytes, DataMessage* dm, byte*& app_data, int& data_size)
{
	byte* message = 0;
	TCPSegment* current = seen_;
	TCPSegment* previous = NULL;
	TCPSegment* aux = NULL;
	int segment_counter = 0;

	if(dm!=NULL){
		LOG_DEBUG("[" << this << "] addtolist 1: dm is NOT  null, seq=" << seq << " numbytes=" << numBytes << " dm" << dm << endl);
	} else {
		LOG_DEBUG("[" << this << "] addtolist 2: dm is NULL, seq=" << seq << " numbytes=" << numBytes << " dm" << dm << endl);
	}

	if(dm != NULL) {
		message = (byte*)dm->getRawData();
	}
	TCPSegment* node = new TCPSegment(seq, numBytes, message);

	int numToDeliver = 0;

	if(!current){

		LOG_DEBUG("\t[debug rx app], in addToList, addtohead, " <<
				" -srcport=" << this->tcpSession->tcpConn->srcPort <<
				" dstport=" << this->tcpSession->tcpConn->dstPort <<
				" , seq= " << seq << endl);

		// This list is empty
		seen_ = node;
		node->next = NULL;
		node->previous = NULL;

		node->sentUp = false;

		left_edge = node;
		right_edge = node;

		if(seq != (int)start_seqno+1) {
			//This first packet is not the first in sequence
			before_next_ = next_;
		} else {
			next_ = seq + numBytes;
			before_next_ = seq;
			firstPktReceived = true;
		}

		maxseen_ = before_next_;
		LOG_DEBUG("\t-rcvwnd 1: add to head, leftseqno=" << left_edge->seqno << endl);

	} else if((uint32)seq < current->seqno) {

		LOG_DEBUG("\t[debug rx app], in addToList, addbefore current, " <<
				" srcport=" << this->tcpSession->tcpConn->srcPort <<
				" dstport=" << this->tcpSession->tcpConn->dstPort <<
				" , seq= " << seq << " current->seqno=" << current->seqno << endl);

		// Insert to the left of the head
		if(firstPktReceived == false) {
			next_ = seq + numBytes;
			before_next_ = seq;
			firstPktReceived = true;
		}

		seen_ = node;
		node->next = current;
		current->previous = node;
		node->previous = NULL;

		left_edge = node;
		LOG_DEBUG("\t-rcvwnd 2: add before head, leftseqno=" << left_edge->seqno << endl);

	} else {

		//Search for possible correct position
		while(current->next != NULL){

			LOG_DEBUG("\t[debug rx app], in addToList, search for location, " <<
					" -srcport=" << this->tcpSession->tcpConn->srcPort <<
					" dstport=" << this->tcpSession->tcpConn->dstPort <<
					" , seq= " << seq << " current->seqno=" << current->seqno << endl);

			if(seq == (int) current->seqno){
				//duplicate packet, do not insert anything
				return 0;
			}
			if((current->seqno + current->length <= (uint32) seq && current->next->seqno >=
					(uint32)(seq+numBytes)))
			{
				// if there is a hole between current and current's next packet the the
				// position was found
				break;
			}
			//let's examine next segment
			previous = current;
			current = current->next;
		}

		LOG_DEBUG("\t[debug rx app], in addToList, after while, " <<
				" -srcport=" << this->tcpSession->tcpConn->srcPort <<
				" dstport=" << this->tcpSession->tcpConn->dstPort <<
				" , seq= " << seq << " current->seqno=" << current->seqno << endl);

		//Insert segment
		if(current->seqno + current->length <= (uint32) seq && current->next == NULL) {
			LOG_DEBUG("\t-rcvwnd 3: add after head " << endl);
			current->next = node;
			node->next = NULL;
			node->previous = current;
			node->sentUp = false;

			maxseen_ = seq;

			//update edges
			right_edge = node;
			if(node->previous->seqno + node->previous->length == node->seqno && (left_edge == node->previous)){
				LOG_DEBUG("\t\t-rcvwnd 4: " << endl);
				left_edge = node;
			}
		} else if(current->seqno + current->length <= (uint32) seq &&
				((uint32) seq + numBytes) <= current->next->seqno &&
				current->next != NULL) {
			LOG_DEBUG("\t-rcvwnd 5: insert in between " << " currentseq=" << current->seqno << " nexts=" <<
					current->next->seqno << " this seqno=" << node->seqno << endl);
			TCPSegment* aux = current->next;
			current->next = node;
			node->next = aux;
			node->previous = current;
			node->sentUp = false;

			//update edges
			if(node->previous->seqno + node->previous->length == node->seqno && (left_edge == node->previous)){
				left_edge = node;
				LOG_DEBUG("\t\t-rcvwnd 5 6: " << "leftseqno=" << left_edge->seqno << endl);
			}
		}
		else if(current->seqno >= (uint32)seq + numBytes) {
			LOG_DEBUG("\t-rcvwnd 7: should not be here " << endl);

			if(previous != NULL){
				previous->next = node;
				node->next = current;
				node->previous = previous;
				node->sentUp = false;
			} else {
				seen_ = node;
				node->next = current;
				node->sentUp = false;
			}
		}
	}
/*	if(node->previous==NULL && node->next==NULL) {
		printf("\t[%d] TCPRecvWindow: AFTER added segment to the END of the head prev=%d this=%d next=%d\n",
			this->tcpSession->getID(),-1, node->seqno,-1);
	} else if(node->previous==NULL && node->next!=NULL){
		printf("\t[%d] TCPRecvWindow: AFTER added segment to the END of the head prev=%d this=%d next=%d\n",
			this->tcpSession->getID(), -1, node->seqno, node->next->seqno);
	} else if(node->previous!=NULL && node->next==NULL) {
		printf("\t[%d] TCPRecvWindow: AFTER added segment to the END of the head prev=%d this=%d next=%d\n",
			this->tcpSession->getID(), node->previous->seqno, node->seqno, -1);
	} else {
		printf("\t[%d] TCPRecvWindow: AFTER added segment to the END of the head prev=%d this=%d next=%d\n",
			this->tcpSession->getID(), node->previous->seqno, node->seqno, node->next->seqno);
	}*/

	TCPSegment* current_for_test = seen_;
	while(true){
		if(current_for_test != NULL){
			LOG_DEBUG("\t[debug rx app], in addToList " <<
					" srcport=" << this->tcpSession->tcpConn->srcPort <<
					" dstport=" << this->tcpSession->tcpConn->dstPort <<
					" , seq= " << seq << "\t\t ---current->seqno=" << current_for_test->seqno << endl);
			current_for_test = current_for_test->next;
		} else {
			break;
		}
	}
	LOG_DEBUG(endl);

	if(firstPktReceived == false){
		//Do not update anything until we receive the first packet in the sequence
		return 0;
	}

	//Update edges and get the number of bytes to deliver
	TCPSegment* left = left_edge;
	data_size = 0;
	aux = left;

	if(left_edge != NULL) {
		LOG_DEBUG("\t\t-rcvwnd 5 6 5: " << "leftseqno=" << left_edge->seqno << " this->start_seqno=" <<
				this->start_seqno << endl);
	}

	LOG_DEBUG("\t\t[debug rx app], before updating edges " <<
			" srcport=" << this->tcpSession->tcpConn->srcPort <<
			" dstport=" << this->tcpSession->tcpConn->dstPort <<
			" , seq= " << seq << " left->seqno=" << left->seqno << " start_seqno=" << start_seqno <<
			" numToDeliver=" << numToDeliver << endl);

	if(left!=NULL && (left->seqno == (this->start_seqno +1))) {
		if (left->sentUp == false) {
			left->sentUp = true;
			numToDeliver += left->length;
			this->start_seqno += numToDeliver;
			LOG_DEBUG("\t\t\t-rcvwnd 5 7: sending:" << left->seqno << " to " << (left->seqno + left->length - 1) << endl);

			LOG_DEBUG("\t\t[debug rx app], this node is sent upwards " <<
					" srcport=" << this->tcpSession->tcpConn->srcPort <<
					" dstport=" << this->tcpSession->tcpConn->dstPort <<
					" left->seqno=" << left->seqno <<
					" numToDeliver=" << numToDeliver << endl);

			if(left->msg != NULL){
				data_size += left->length;
				segment_counter++;
				LOG_DEBUG("\t[" << this << "]  addtolist 9.5 data_size=" << data_size << " segment_counter=" << segment_counter << endl);
			}
		} else {
			//Do not count this segment
			LOG_DEBUG("2 numbytes_=" << numToDeliver << " seqno=" << left->seqno << " start_seqno=" << start_seqno << endl);
		}
	} else {
		LOG_DEBUG("\t[" << this << "]  addtolist 9.9" << endl);
		//Do nothing because the pkts are not in sequence
		//LOG_DEBUG("1 final numbytes_=" << numToDeliver << " seqno=" << left->seqno << " start_seqno=" << start_seqno << endl);
	}

	LOG_DEBUG("[" << this->tcpSession->getMaster()->getNow() << "] sending up to http--------------------" << endl);
	while(left->next != NULL){
		LOG_DEBUG("\t\t[debug rx app], testing new node to send upwards " <<
				" srcport=" << this->tcpSession->tcpConn->srcPort <<
				" dstport=" << this->tcpSession->tcpConn->dstPort <<
				" left->seqno=" << left->seqno <<
				" left->next->seqno=" << left->next->seqno << endl);
		if(left->seqno + left->length == left->next->seqno){
			LOG_DEBUG("\t\t[debug rx app], this node is going to be sent upwards " <<
					" srcport=" << this->tcpSession->tcpConn->srcPort <<
					" dstport=" << this->tcpSession->tcpConn->dstPort <<
					" left->seqno=" << left->seqno <<
					" left->next->seqno=" << left->next->seqno <<
					" left->next->sentUp=" << left->next->sentUp << endl);
			//XXX if (left->next->sentUp == false && left->next->msg != NULL) { //< is the second check necessary?
			if (left->next->sentUp == false) {
				left->next->sentUp = true;

				if(left->next->msg != NULL){
					LOG_DEBUG("[" << this->tcpSession->getMaster()->getNow() << "], rcvwindow , before, sending up to http, "
						" bytes_to_deliver=" << numToDeliver << " data_size=" << data_size << " next message is"
								"not null" << endl);
				} else {
					LOG_DEBUG("[" << this->tcpSession->getMaster()->getNow() << "], rcvwindow , before, sending up to http, "
						" bytes_to_deliver=" << numToDeliver << " data_size=" << data_size << " next message is"
						" null!!!" << endl);
				}

				numToDeliver += left->next->length;

				//Update the start seqno because this block is in sequence
				if(left->next->seqno == this->start_seqno +1) {
					this->start_seqno += left->next->length;
				}

				LOG_DEBUG("\t\t\t-rcvwnd 5 8: sending:" << left->next->seqno << " to " << (left->next->seqno + left->next->length -1) << endl);
				if(left->msg != NULL){
					data_size += left->next->length;
					segment_counter++;
				}

				LOG_DEBUG("[" << this->tcpSession->getMaster()->getNow() << "], rcvwindow, after, sending up to http, "
						" bytes_to_deliver=" << numToDeliver << " data_size=" << data_size<< endl);

			} else{
				LOG_DEBUG("\t[" << this << "]  addtolist 12" << endl);
				LOG_DEBUG("4 numbytes_=" << numToDeliver << " left->seqno" << left->seqno << " left->length" << left->length <<
						" left->next->seqno=" << left->next->seqno << endl);
				//Do nothing
			}
		} else {
			LOG_DEBUG("\t[" << this << "]  addtolist 13" << endl);
			LOG_DEBUG("5 numbytes_=" << numToDeliver << " left->seqno" << left->seqno << " left->length" << left->length <<
									" left->next->seqno=" << left->next->seqno << endl);
			//Blocks not in sequence
			break;
		}
		left = left->next;
	}
	if(data_size > 0){
		app_data = new byte[data_size];
	}
	int offset = 0;
	LOG_DEBUG("\t\t[" << this << "]  addtolist 14, segment_counter= " << segment_counter << endl);
	for(int i=0; i<segment_counter; i++) {
	//while(counter<segment_counter){
		LOG_DEBUG("\t[" << this << "]  addtolist 15" << endl);
		if(aux->msg != NULL){
			LOG_DEBUG("\t[" << this << "]  addtolist 16" << endl);
			LOG_DEBUG("copying..., msg= " << aux->msg << " offset=" << offset << " length=" << aux->length << endl);
			memcpy(app_data + offset, aux->msg, aux->length);
		}
		offset += aux->length;
		aux = aux->next;
	}
	//make sure we copied all data
	LOG_DEBUG("\t[" << this << "] addtolist 17 offset=" << offset << " data_size=" << data_size << endl);
	assert(offset == data_size);

	left_edge = left;

	next_ = left_edge->seqno + left_edge->length;
	before_next_ = left_edge->seqno;

	cleanWindow();

	LOG_DEBUG("\t\t\t\t-rcvwnd 5 9: numToDeliver= " << numToDeliver << endl);

	return numToDeliver;
}

void TCPRecvWindow::cleanWindow()
{
	TCPSegment* temp=0;
	for(TCPSegment* aux=seen_; aux!=left_edge && aux!=NULL; ){
		if(aux->next != NULL){
			seen_ = aux->next;
			temp=aux->next;
		}
		else {
			temp=0;
		}
		LOG_DEBUG("CLEANING RCV WINDOW" << endl);
		delete aux;
		aux=temp;
	}
}

void TCPRecvWindow::setStart(uint32 seqno) {
	start_seqno = seqno;
	next_ = ++seqno;
	LOG_DEBUG("SEQNO was initialized to" << start_seqno << endl);
}

// Returns number of bytes that can be "delivered" to application
// also updates the receive window (i.e. next_, maxseen, and seen_ array)
// numBytes is now dm!
uint32_t TCPRecvWindow::update(uint32_t seq, uint32_t numBytes, DataMessage* dm, byte*& app_data, int& data_size)
{
	bool just_marked_as_seen = false;
	is_dup_ = false;

	if(seq && numBytes <= 0)
		LOG_ERROR("ERROR: received TCP packet of size %d" << numBytes << "<= 0" << endl);

	if(dm->getRawData()!=NULL){
		LOG_DEBUG("rcvwnd [update 1][""] , seq=" << seq << " numbytes=" << numBytes <<
				" maxseen_ = " << maxseen_ << " next_=" << next_ << " start_seqno=" << start_seqno << endl);
	}

	LOG_DEBUG("[debug rx app], beginning of 'update', " <<
			" srcport=" << this->tcpSession->tcpConn->srcPort <<
			" dstport=" << this->tcpSession->tcpConn->dstPort <<
			" , got seq= " << seq << " numBytes=" << numBytes << endl);


	int numToDeliver = 0;
	//There is no resizing of buffers here!

	if (seq > maxseen_) {
		// the packet is the highest one we've seen so far
		// we record the packets between the old maximum and
		// the new max as being "unseen" i.e. 0 bytes of each
		// packet have been received

		// We do now have to tag slots not used because we are
		// using a single linked list instead of an array

		maxseen_ = seq;

		if(dm->getRawData()!=NULL){
			LOG_DEBUG("rcvwnd [update 2][" << this->tcpSession->getID() << "] , seq=" << seq << " numbytes=" << numBytes <<
					" maxseen_ = " << maxseen_ << endl);
		}

		numToDeliver = addToList(seq, numBytes, dm, app_data, data_size);
		if(app_data) {
			LOG_DEBUG("in update: app_data=" << app_data << " size=" << data_size << endl);
		}
		if(dm->getRawData()!=NULL){
			LOG_DEBUG("rcvwnd [update 2.5][" << this->tcpSession->getID() << "] , numtodeliver=" << numToDeliver << endl);
		}
		just_marked_as_seen = true;
		// necessary so this packet isn't confused as being a duplicate
	}

	uint32_t next = next_;

	//XXX THIS WILL ALWAYS BE TRUE?
	if (seq < next) {
		// Duplicate packet case 1: the packet is to the left edge of
		// the receive window; therefore we must have seen it
		// before
		if(dm->getRawData()!=NULL){
			LOG_DEBUG("rcvwnd [update is duplicate], seq=" << seq << " next=" << next << endl);
		}
		is_dup_ = true;
	}

	if (seq >= next && seq <= maxseen_) {
		// next is the left edge of the recv window; maxseen_
		// is the right edge; execute this block if there are
		// missing packets in the recv window AND if current
		// packet falls within those gaps

		if (searchSeq(seq) && !just_marked_as_seen) {
			// Duplicate case 2: the segment has already been
			// recorded as being received (AND not because we just
			// marked it as such)
			is_dup_ = true;
		}
		// record the packet as being seen
		if(dm->getRawData()!=NULL){
			LOG_DEBUG("rcvwnd [update 3][" << "] , seq=" << seq << " numbytes=" << numBytes <<
				" maxseen_ = " << maxseen_ << endl);
		}
		numToDeliver = addToList(seq, numBytes, dm, app_data, data_size);
	}

	if(dm->getRawData()!=NULL){
		LOG_DEBUG("rcvwnd [update final][" << this->tcpSession->getID() << "] , numtodeliver=" << numToDeliver << endl);
	}

	LOG_DEBUG("[debug rx app], at the end of update, numToDeliver= " << numToDeliver <<
			" srcport=" << this->tcpSession->tcpConn->srcPort <<
			" dstport=" << this->tcpSession->tcpConn->dstPort <<
			" , got seq= " << seq << " numBytes=" << numBytes << endl);

	return numToDeliver;
}

NewSackStack::NewSackStack(int sz)
{
	register int i;
	size_ = sz;
	SFE_ = new Sf_Entry[sz];
	for (i = 0; i < sz; i++)
		SFE_[i].left_ = SFE_[i].right_ = -1;
	cnt_ = 0;
}

NewSackStack::~NewSackStack()
{
	delete SFE_;
}


}; // namespace ssfnet
}; // namespace prime
