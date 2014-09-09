/*
 * teleport :- teleport for remote message passing.
 *
 * The data structure is need to send and receive messages to remote
 * ssf instances in a distributed memory environment.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ssf.h"
#include "sim/composite.h"

#ifdef PRIME_SSF_SYNC_MPI
// run time check for mpi calls
#define MPICHECK(x) if(x) ssf_throw_exception(ssf_exception::kernel_mpi, #x);

//#define DBGPRINT

// for the barrier
#define MSGTAP_MESSAGE		1
#define MSGTAP_SYNCUP		2
#define MSGTAP_SYNCDOWN		3
#define MSGTAP_REPORTING	4 // for serializing final report
#endif /*PRIME_SSF_SYNC_MPI*/

#if PRIME_SSF_DISTSIM
#define BSEND_BUFSIZ	(atol(PRIME_SSF_MPI_BUFSIZ)<<20)
#define MAX_EVTSIZ	(atol(PRIME_SSF_MPI_MAXEVT)<<20)
#endif

namespace prime {
namespace ssf {

SSF_MAP(SSF_PAIR(int,int),ssf_stargate*) ssf_teleport::starmap;

#ifdef PRIME_SSF_SYNC_LIBSYNK
long ssf_teleport::libsynk_last_transaction = -1;

unsigned int ssf_teleport::fmhandler_msgs;
unsigned int ssf_teleport::fmhandler_results;

#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
unsigned int ssf_teleport::fmhandler_num_msgs;
int ssf_teleport::libsynk_got_num_msgs = 0;
int ssf_teleport::libsynk_msgs_to_get = 0;
int ssf_teleport::libsynk_got_msgs = 0;
int ssf_teleport::libsynk_msgs_synchronized = 0;
#endif

int ssf_teleport::libsynk_got_results = 0;
int ssf_teleport::libsynk_results_synchronized = 0;

long ssf_teleport::libsynk_lbtsstarted(long transaction, TM_Time* mintime,
				   TM_TimeQual* mintimequal, 
				   TM_LBTSDoneProc* donehandler)
{
#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] LBTS started: "
	      "transaction=%ld => %lf\n",  SSF_USE_SHARED_RO(whoami), 
	      SSF_SELF, transaction, (double)SSF_USE_PRIVATE(decade));
#endif
  //return TM_DEFER;
  *mintime = SSF_USE_PRIVATE(decade);
  *mintimequal = TM_TIME_QUAL_EXCL;
  *donehandler = &ssf_teleport::libsynk_lbtsdone;
  return TM_ACCEPT;
}

void ssf_teleport::libsynk_lbtsdone(TM_Time result, TM_TimeQual resultqual, 
				long transaction)
{
#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] LBTS done: "
	      "transaction=%ld, result=%lf\n", SSF_USE_SHARED_RO(whoami),
	      SSF_SELF, transaction, (double)result);
#endif
  if(libsynk_last_transaction<transaction)
    libsynk_last_transaction = transaction;

  SSF_USE_PRIVATE(epoch) = result+SSF_USE_SHARED(startime);
  SSF_USE_SHARED(epoch_length) = SSF_USE_PRIVATE(epoch)-SSF_USE_PRIVATE(now);
  ssf_min_reduction(SSF_USE_PRIVATE(epoch));
}

int ssf_teleport::libsynk_transaction_done(long transaction)
{
  if(transaction <= libsynk_last_transaction) return 1;
  return 0;
}

#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
int ssf_teleport::libsynk_get_num_msgs(FM_stream* strm, unsigned msgsiz)
{
  //assert(msgsiz == sizeof(int));
  int nmsgs = 0;
  FM_receive(&nmsgs, strm, sizeof(int));
  nmsgs = SSF_BYTE_ORDER_32(nmsgs);

  libsynk_got_num_msgs++;
  libsynk_msgs_to_get += nmsgs;

#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] num_msgs handler: "
	      "msgsiz=%u, msggot=%d, nmsgs=%d, toget=%d\n", 
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, msgsiz, 
	      libsynk_got_num_msgs, nmsgs, libsynk_msgs_to_get);
#endif

  if(libsynk_got_num_msgs == SSF_USE_SHARED_RO(nmachs)-1 &&
     libsynk_msgs_to_get == libsynk_got_msgs) {
    libsynk_got_num_msgs = libsynk_msgs_to_get = libsynk_got_msgs = 0;
    libsynk_msgs_synchronized = 1;
  }
  return FM_CONTINUE;
}
#endif

int ssf_teleport::libsynk_get_msgs(FM_stream* strm, unsigned msgsiz)
{
  char recvbuf[MSG_BUFSIZ];
  int rcvmsgsiz = 0;
  FM_receive(&rcvmsgsiz, strm, sizeof(int));
  rcvmsgsiz = SSF_BYTE_ORDER_32(rcvmsgsiz);
  assert(rcvmsgsiz >= (int)sizeof(TM_TagType));
  FM_receive(recvbuf, strm, rcvmsgsiz);

  ssf_messenger* msg = new ssf_messenger;
  int pos = sizeof(TM_TagType);
  int ret = msg->unpack(recvbuf, pos, MSG_BUFSIZ);
  if(ret) ssf_throw_exception(ssf_exception::kernel_unpack);
#ifndef SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
  TM_In(msg->time-SSF_USE_SHARED(startime)+TM_TopoGetMinExternalLA(),
	*(TM_TagType*)recvbuf);
#endif

#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] get_msgs: "
	      "src_lp=%d, src_ent=%d, src_port=%d, time=%lf\n",
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, msg->src_lp, 
	      msg->src_ent, msg->src_port, (double)msg->time);
#endif

  ssf_stargate* sg = ssf_teleport::map_stargate(msg->src_lp, msg->tgt_lp);
  assert(sg);
  ssf_remote_tlt* rtlt = sg->map_remote_tlt(msg->src_ent, msg->src_port);
  assert(rtlt);
  Event* usrevt = 0;
  if(msg->usrevt_ident >= 0) {
    usrevt = Event::_evt_create_registered_event
      (msg->usrevt_ident, msg->usrevt_packed);
    if(!usrevt) {
      char expmsg[256];
      sprintf(expmsg, "ident=%d", msg->usrevt_ident);
      ssf_throw_exception(ssf_exception::kernel_newevt, expmsg);
    }
  }
  ssf_kernel_event* kevt = new ssf_kernel_event
    (msg->time, ssf_kernel_event::EVTYPE_INCHANNEL, usrevt);
  kevt->entity_serialno = msg->src_ent;
  kevt->event_seqno = msg->serial_no;
  kevt->mapnode = rtlt->ic_head;
  sg->tgt->insert_event(kevt);
  delete msg;

#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
  libsynk_got_msgs++;
  if(libsynk_got_num_msgs == SSF_USE_SHARED_RO(nmachs)-1 &&
     libsynk_msgs_to_get == libsynk_got_msgs) {
    libsynk_got_num_msgs = libsynk_msgs_to_get = libsynk_got_msgs = 0;
    libsynk_msgs_synchronized = 1;
  }
#endif
  return FM_CONTINUE;
}

int ssf_teleport::libsynk_get_results(FM_stream* strm, unsigned msgsiz)
{
  //assert(msgsiz >= 9*sizeof(unsigned long)+sizeof(double));
#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] get_results: msgsiz=%u\n",
	      SSF_USE_SHARED_RO(whoami), SSF_SELF, msgsiz);
#endif

#if PRIME_SSF_REPORT_USER_TIMING
  double ut;
#endif
  uint32 lps, ent, evt, msg, msg_xprc, msg_xmem, tcs, pcs, wmk;
  double exec;

  char recvbuf[256];
  int rcvmsgsiz = 0;
  FM_receive(&rcvmsgsiz, strm, sizeof(int));
  rcvmsgsiz = SSF_BYTE_ORDER_32(rcvmsgsiz);
  FM_receive(recvbuf, strm, rcvmsgsiz);

  int pos = 0;
#if PRIME_SSF_REPORT_USER_TIMING
  ssf_compact::deserialize(ut, &recvbuf[pos]); pos += sizeof(double);
#endif
  ssf_compact::deserialize(lps, (void*)&recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(ent, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(evt, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(msg, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(msg_xprc, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(msg_xmem, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(tcs, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(pcs, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(wmk, &recvbuf[pos]); pos += sizeof(uint32);
  ssf_compact::deserialize(exec, &recvbuf[pos]); pos += sizeof(double);

#if PRIME_SSF_REPORT_USER_TIMING
  SSF_USE_SHARED(sum_utime) += ut;
#endif
  SSF_USE_SHARED(total_lps) += lps;
  SSF_USE_SHARED(total_ent) += ent;
  SSF_USE_SHARED(total_evt) += evt;
  SSF_USE_SHARED(total_msg) += msg;
  SSF_USE_SHARED(total_msg_xprc) += msg_xprc;
  SSF_USE_SHARED(total_msg_xmem) += msg_xmem;
  SSF_USE_SHARED(total_tcs) += tcs;
  SSF_USE_SHARED(total_pcs) += pcs;
  SSF_USE_SHARED(total_wmk) += wmk;
  if(SF_USE_SHARED(max_exec) < exec)
    SSF_USE_SHARED(max_exec) = exec;

  libsynk_got_results++;
  if(libsynk_got_results == SSF_USE_SHARED_RO(nmachs)-1)
    libsynk_results_synchronized = 1;
  return FM_CONTINUE;
}
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

ssf_teleport::ssf_teleport() 
{
#if PRIME_SSF_DISTSIM
  MSG_SNDBUF_THRESHOLD = MAX_EVTSIZ;
  MSG_BUFSIZ = 2*MAX_EVTSIZ;

  ssf_mutex_init(&msgque_mutex);
  msg_queue = 0;

#ifdef PRIME_SSF_SYNC_MPI
  // get the ranks of the upstream processor and the downstream
  // processors in the binomial tree
  up_rank = 0;
  ndowns = 0;
  down_ranks = 0;
  if(SSF_USE_SHARED_RO(nmachs) > 1) {
    // compute up rank and down ranks
    int x = SSF_USE_SHARED_RO(whoami);
    int y = 2;
    int z = SSF_USE_SHARED_RO(nmachs);
    while((x&1) == 0 && z) {
      x >>= 1;
      y <<= 1;
      z >>= 1;
      ndowns++;
    }
    if(SSF_USE_SHARED_RO(whoami))
      up_rank = ~(y-1)&SSF_USE_SHARED_RO(whoami);
    if(ndowns > 0) {
      down_ranks = new int[ndowns];
      y = 1;
      for(int j=0; j<ndowns; j++) {
	if(SSF_USE_SHARED_RO(whoami)+y >= SSF_USE_SHARED_RO(nmachs)) { 
	  ndowns = j; 
	  break; 
	}
	down_ranks[j] = SSF_USE_SHARED_RO(whoami) | y;
	y <<= 1;
      }
    }
  }

#ifdef DBGPRINT
  printf("[%d] uprank=%d, downrank=%d", SSF_USE_SHARED_RO(whoami), up_rank, ndowns);
  for(int k=0; k<ndowns; k++) printf(" (%d)", down_ranks[k]);
  printf("\n"); //fflush(0);
#endif

  // It's important to use malloc to get memory blocks for MPI from
  // the heap instead of from the shared memory segment managed by
  // PRIME SSF.

  sendbuf = (char**)malloc(SSF_USE_SHARED_RO(nmachs)*sizeof(char*));
  assert(sendbuf);
  for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
    sendbuf[i] = (char*)malloc(MSG_BUFSIZ*sizeof(char));
    assert(sendbuf[i]);
  }

  sendpos = (int*)malloc(SSF_USE_SHARED_RO(nmachs)*sizeof(int));
  assert(sendpos);
  memset(sendpos, 0, sizeof(int)*SSF_USE_SHARED_RO(nmachs));
  
  outstanding_recv = 0;

  // there is only one receive buffer
  recvbuf = (char*)malloc(MSG_BUFSIZ*sizeof(char)); assert(recvbuf);
  
  // # messages to be received by all processors downstream and myself
  upsent = (long*)malloc(SSF_USE_SHARED_RO(nmachs)*sizeof(long));
  assert(upsent);
  upsent_rcvd = (long*)malloc(SSF_USE_SHARED_RO(nmachs)*sizeof(long));
  assert(upsent_rcvd);
    
  // final # messages to be received by all processors (collected by
  // the root of the binomial tree and then broadcasted downstream)
  downsent = (long*)malloc(SSF_USE_SHARED_RO(nmachs)*sizeof(long)); 
  assert(downsent);

  char* bsend_buf = (char*)malloc(BSEND_BUFSIZ);
  assert(bsend_buf);
  MPICHECK(MPI_Buffer_attach(bsend_buf, BSEND_BUFSIZ));

  mainproc_hit_barrier = false;
#endif /*PRIME_SSF_SYNC_MPI*/
#endif /*PRIME_SSF_DISTSIM*/
}

ssf_teleport::~ssf_teleport() 
{
#if PRIME_SSF_DISTSIM
  assert(msg_queue == 0);

#ifdef PRIME_SSF_SYNC_MPI
  for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
    free(sendbuf[i]);
  }
  free(sendbuf);
  free(sendpos);

  if(down_ranks) delete[] down_ranks;
#endif

  /*
  delete[] msg_queues;
  delete[] msgque_lengths;
  */
#endif /*PRIME_SSF_DISTSIM*/
}

void ssf_teleport::initialize()
{
#ifdef PRIME_SSF_SYNC_MPI
  ssf_barrier();
  if(SSF_SELF == 0) {
    ltime_t my_epoch_length = SSF_USE_SHARED(epoch_length);

    ltime_t our_epoch_length;
    MPICHECK(MPI_Allreduce(&my_epoch_length, &our_epoch_length,
			   1, SSF_LTIME_MPI_TYPE, MPI_MIN, MPI_COMM_WORLD));
    SSF_USE_SHARED(epoch_length) = our_epoch_length;

    init_round(); // get ready for the next window
  }
  ssf_barrier();
#if PRIME_SSF_DEBUG
  if(SSF_SELF == 0) {
    ssf_debug(SSF_DEBUG_MPI, "<MPI> %d starts... epoch length = %g\n",
	      SSF_USE_SHARED_RO(whoami), (double)SSF_USE_SHARED(epoch_length)); 
  }
#endif
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
  ssf_barrier();
  if(SSF_USE_SHARED_RO(nmachs) > 1 && SSF_SELF == 0) {
#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
    FML_RegisterHandler(&fmhandler_num_msgs, &libsynk_get_num_msgs);
#endif
    FML_RegisterHandler(&fmhandler_msgs, &libsynk_get_msgs);
    FML_RegisterHandler(&fmhandler_results, &libsynk_get_results);
    TM_TopoSetMinExternalLA(SSF_USE_SHARED(epoch_length));
#if PRIME_SSF_DEBUG
    if((SSF_USE_SHARED_RO(debug_mask)&SSF_DEBUG_LIBSYNK) != 0 ||
       (SSF_USE_SHARED_RO(debug_mask)&SSF_DEBUG_LIBSYNK_ALL) != 0)
      TM_TopoPrint();
#endif
    TM_SetLBTSStartProc(libsynk_lbtsstarted);

    ltime_t lbts = SSF_USE_PRIVATE(now)+TM_TopoGetMinExternalLA();
#if PRIME_SSF_DEBUG 
    ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] ssf_teleport::init() "
		"initiate LBTS: %lf\n", SSF_USE_SHARED_RO(whoami), 
		SSF_SELF, (double)lbts);
#endif
    long transaction;
    if(TM_StartLBTS(lbts-SSF_USE_SHARED(startime), TM_TIME_QUAL_EXCL, 
		    libsynk_lbtsdone, &transaction) != TM_SUCCESS)
      ssf_throw_exception(ssf_exception::kernel_lbts);

    while(!libsynk_transaction_done(transaction)) { 
      FM_extract(~0); TM_Tick(); ssf_yield_proc();
    }

    if(!SSF_USE_SHARED_RO(silent) && SSF_SELF == 0) {
      fprintf(SSF_USE_SHARED_RO(outfile), "libsynk %d starts...\n",
	      SSF_USE_SHARED_RO(whoami));
    }
#if PRIME_SSF_DEBUG 
    ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] epoch length = %g\n",
		SSF_USE_SHARED_RO(whoami), SSF_SELF,
		(double)SSF_USE_SHARED(epoch_length));
#endif
  } else {
    SSF_USE_PRIVATE(epoch) = ssf_min_reduction(SSF_LTIME_INFINITY);
  }
#endif /*PRIME_SSF_SYNC_LIBSYNK*/
}

void ssf_teleport::serialize_reporting_begin()
{
#if PRIME_SSF_DISTSIM
  ssf_barrier();
  if(SSF_SELF == 0) {
#ifdef PRIME_SSF_SYNC_MPI
    fflush(0); // clear up previous printf's 
    MPICHECK(MPI_Barrier(MPI_COMM_WORLD));

    if(SSF_USE_SHARED_RO(whoami) > 0) {
      MPI_Status status;
      int notice;
      MPICHECK(MPI_Recv(&notice, 1, MPI_INT, SSF_USE_SHARED_RO(whoami)-1,
			MSGTAP_REPORTING, MPI_COMM_WORLD, &status)); 
    }
#endif

#ifdef PRIME_SSF_SYNC_LIBSYNK
    /* not implemented */
#endif
  }
  ssf_barrier();
#endif
}

void ssf_teleport::serialize_reporting_end()
{
#if PRIME_SSF_DISTSIM
  ssf_barrier();
  if(SSF_SELF == 0) {
#ifdef PRIME_SSF_SYNC_MPI
    if(SSF_USE_SHARED_RO(whoami) < SSF_USE_SHARED_RO(nmachs)-1) {
      int notice = 1; // dummy
      MPICHECK(MPI_Send(&notice, 1, MPI_INT, SSF_USE_SHARED_RO(whoami)+1,
			MSGTAP_REPORTING, MPI_COMM_WORLD)); 
    }
#endif

#ifdef PRIME_SSF_SYNC_LIBSYNK
    /* not implemented */
#endif
  }
  ssf_barrier();
#endif
}

void ssf_teleport::finalize()
{
#if PRIME_SSF_DISTSIM
#if PRIME_SSF_REPORT_USER_TIMING
  double ut = ssf_sum_reduction((double)SSF_USE_PRIVATE(utime));
#endif
  uint32 lps = ssf_sum_reduction((uint32)((ssf_universe*)SSF_USE_PRIVATE(universe))->lpset.size());
  uint32 ent = ssf_sum_reduction((uint32)((ssf_universe*)SSF_USE_PRIVATE(universe))->total_entities);
  uint32 evt = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_events));
  uint32 msg = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_messages));
  uint32 msg_xprc = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_messages_xprc));
  uint32 msg_xmem = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_messages_xmem));
  uint32 tcs = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(timeline_contexts));
  uint32 pcs = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(process_contexts));
  double exec = ssf_min_reduction((double)SSF_USE_PRIVATE(execution_time));

  if(SSF_SELF == 0) {
#ifdef PRIME_SSF_SYNC_MPI
    // temporary variables must be used instead of using memory from
    // the shared arena.
    uint32 longtmp;
    double doubletmp;

#if PRIME_SSF_REPORT_USER_TIMING
    MPICHECK(MPI_Reduce(&ut, &doubletmp, 1, MPI_DOUBLE, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(sum_utime) = doubletmp;
#endif
    MPICHECK(MPI_Reduce(&lps, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_lps) = longtmp;
    MPICHECK(MPI_Reduce(&ent, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_ent) = longtmp;
    MPICHECK(MPI_Reduce(&evt, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_evt) = longtmp;
    MPICHECK(MPI_Reduce(&msg, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_msg) = longtmp;
    MPICHECK(MPI_Reduce(&msg_xprc, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_msg_xprc) = longtmp;
    MPICHECK(MPI_Reduce(&msg_xmem, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_msg_xmem) = longtmp;
    MPICHECK(MPI_Reduce(&tcs, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_tcs) = longtmp;
    MPICHECK(MPI_Reduce(&pcs, &longtmp, 1, MPI_UNSIGNED, 
			MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_pcs) = longtmp;
    uint32 wmk = 0;
#if !SSF_SHARED_HEAP_SEGMENT
    if(SSF_ARENA_ACTIVE) {
      wmk = SSF_USE_SHARED(mem_watermark)+SSF_USE_SHARED(mem_oilmark);
    }
#endif
    MPICHECK(MPI_Reduce(&wmk, &longtmp, 1, MPI_UNSIGNED, 
		       MPI_SUM, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(total_wmk) = longtmp;
    MPICHECK(MPI_Reduce(&exec, &doubletmp, 1, MPI_DOUBLE, 
			MPI_MAX, 0, MPI_COMM_WORLD));
    SSF_USE_SHARED(max_exec) = doubletmp;
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
    uint32 wmk = 0;
#if !SSF_SHARED_HEAP_SEGMENT
    if(SSF_ARENA_ACTIVE) {
      wmk = SSF_USE_SHARED(mem_watermark)+SSF_USE_SHARED(mem_oilmark);
    }
#endif

#if PRIME_SSF_REPORT_USER_TIMING
    SSF_USE_SHARED(sum_utime) = ut;
#endif
    SSF_USE_SHARED(total_lps) = lps;
    SSF_USE_SHARED(total_ent) = ent;
    SSF_USE_SHARED(total_evt) = evt;
    SSF_USE_SHARED(total_msg) = msg;
    SSF_USE_SHARED(total_msg_xprc) = msg_xprc;
    SSF_USE_SHARED(total_msg_xmem) = msg_xmem;
    SSF_USE_SHARED(total_tcs) = tcs;
    SSF_USE_SHARED(total_pcs) = pcs;
    SSF_USE_SHARED(total_wmk) = wmk;
    SSF_USE_SHARED(max_exec) = exec;

    if(SSF_USE_SHARED_RO(nmachs) > 1) {
      char mybuf[256/*9*sizeof(uint32)+2*sizeof(double)*/];
      int mypos = 0;
#if PRIME_SSF_REPORT_USER_TIMING
      ssf_compact::serialize(ut, &mybuf[mypos]); mypos += sizeof(double);
#endif
      ssf_compact::serialize(lps, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(ent, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(evt, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(msg, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(msg_xprc, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(msg_xmem, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(tcs, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(pcs, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(wmk, &mybuf[mypos]); mypos += sizeof(uint32);
      ssf_compact::serialize(exec, &mybuf[mypos]); mypos += sizeof(double);

      for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
	if(i == SSF_USE_SHARED_RO(whoami)) continue;

	FM_stream* strm = FM_begin_message(i, mypos, fmhandler_results);
	assert(strm);
	int net_mypos = SSF_BYTE_ORDER_32(mypos);
	FM_send_piece(strm, &net_mypos, sizeof(int));
	FM_send_piece(strm, mybuf, mypos);
	FM_end_message(strm);
      }

      while(!libsynk_results_synchronized) {
	FM_extract(~0); TM_Tick(); ssf_yield_proc();
      }
    }
#endif /*PRIME_SSF_SYNC_LIBSYNK*/
  }
  ssf_barrier();
#else /*not PRIME_SSF_DISTSIM*/
#if PRIME_SSF_REPORT_USER_TIMING
  double ut = ssf_sum_reduction((double)SSF_USE_PRIVATE(utime));
#endif
  uint32 lps = ssf_sum_reduction((uint32)((ssf_universe*)SSF_USE_PRIVATE(universe))->lpset.size());
  uint32 ent = ssf_sum_reduction((uint32)((ssf_universe*)SSF_USE_PRIVATE(universe))->total_entities);
  uint32 evt = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_events));
  uint32 msg = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_messages));
  uint32 msg_xprc = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_messages_xprc));
  uint32 msg_xmem = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(kernel_messages_xmem));
  uint32 tcs = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(timeline_contexts));
  uint32 pcs = ssf_sum_reduction((uint32)SSF_USE_PRIVATE(process_contexts));
  double exec = ssf_min_reduction((double)SSF_USE_PRIVATE(execution_time));

  ssf_barrier();
  if(SSF_SELF == 0) {
#if PRIME_SSF_REPORT_USER_TIMING
    SSF_USE_SHARED(sum_utime) = ut;
#endif
    SSF_USE_SHARED(total_lps) = lps;
    SSF_USE_SHARED(total_ent) = ent;
    SSF_USE_SHARED(total_evt) = evt;
    SSF_USE_SHARED(total_msg) = msg;
    SSF_USE_SHARED(total_msg_xprc) = msg_xprc;
    SSF_USE_SHARED(total_msg_xmem) = msg_xmem;
    SSF_USE_SHARED(total_tcs) = tcs;
    SSF_USE_SHARED(total_pcs) = pcs;
    SSF_USE_SHARED(total_wmk) = 0;
#if !SSF_SHARED_HEAP_SEGMENT
    if(SSF_ARENA_ACTIVE) {
      SSF_USE_SHARED(total_wmk) = SSF_USE_SHARED(mem_watermark)+
	SSF_USE_SHARED(mem_oilmark);
    }
#endif
    SSF_USE_SHARED(max_exec) = exec;
  }
  ssf_barrier();
#endif /*not PRIME_SSF_DISTSIM*/
}

int ssf_teleport::calculate_lbts()
{
#if PRIME_SSF_DISTSIM
  if(SSF_SELF == 0)
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->start_timer -= ssf_get_wall_time();

#if PRIME_SSF_INSTRUMENT
  if(SSF_SELF == 0)
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->setAction(ssf_universe::ACTION_SYNCHRONIZE_GLOBAL);
#endif

  if(SSF_SELF) {
    ssf_messenger* msg = new ssf_messenger
      (SSF_LTIME_MINUS_INFINITY, -1, 0, 0, 0, 0, 0);
    ssf_mutex_wait(&msgque_mutex);
    msg->next = msg_queue;
    msg_queue = msg;
    ssf_mutex_signal(&msgque_mutex);

    SSF_USE_PRIVATE(epoch) = ssf_min_reduction(SSF_LTIME_INFINITY);
  } else {
    mainproc_hit_barrier = true;
    global_message_barrier();
    mainproc_hit_barrier = false;
  }

#if PRIME_SSF_INSTRUMENT
  ((ssf_universe*)SSF_USE_PRIVATE(universe))->setAction(ssf_universe::ACTION_OTHER);
#endif

#ifdef DBGPRINT
  printf("%d<%d>: EPOCH = %g\n", SSF_USE_SHARED_RO(whoami), SSF_SELF,
	 (double)SSF_USE_PRIVATE(epoch)); fflush(0);
#endif

  if(SSF_SELF == 0)
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->start_timer += ssf_get_wall_time();

  return (SSF_USE_PRIVATE(epoch)>SSF_USE_SHARED(endtime));
#else /*not PRIME_SSF_DISTSIM*/
  SSF_USE_PRIVATE(epoch) = SSF_LTIME_INFINITY;
  return 1;
#endif /*not PRIME_SSF_DISTSIM*/
}

ssf_stargate* ssf_teleport::map_stargate(ssf_logical_process* src, ssf_logical_process* tgt)
{
  if(src == tgt) return 0;
  /*
  // Here, I assume that size of long is at least 32 bits and the model
  // will never have more than 2^16 timelines! -jason.
  long hash = src->serialno;
  hash <<= 16;
  hash |= tgt->serialno;
  */
  SSF_MAP(SSF_PAIR(int,int),ssf_stargate*)::iterator iter =
    starmap.find(SSF_MAKE_PAIR(src->serialno(), tgt->serialno()));
  if(iter != starmap.end()) return (*iter).second;
  else {
    ssf_stargate* sg = new ssf_stargate(src, tgt);
    starmap.insert(SSF_MAKE_PAIR(SSF_MAKE_PAIR(src->serialno(), 
						 tgt->serialno()), sg));
    return sg;
  }
}
  
#if PRIME_SSF_DISTSIM
ssf_stargate* ssf_teleport::map_stargate(ssf_logical_process* src, int tgt_id)
{
  SSF_MAP(SSF_PAIR(int,int),ssf_stargate*)::iterator iter =
    starmap.find(SSF_MAKE_PAIR(src->serialno(), tgt_id));
  if(iter != starmap.end()) return (*iter).second;
  else {
    ssf_stargate* sg = new ssf_stargate(src, tgt_id);
    starmap.insert
      (SSF_MAKE_PAIR(SSF_MAKE_PAIR(src->serialno(), tgt_id), sg));
    return sg;
  }
}
  
ssf_stargate* ssf_teleport::map_stargate(int src_id, ssf_logical_process* tgt)
{
  SSF_MAP(SSF_PAIR(int,int),ssf_stargate*)::iterator iter =
    starmap.find(SSF_MAKE_PAIR(src_id, tgt->serialno()));
  if(iter != starmap.end()) return (*iter).second;
  else {
    ssf_stargate* sg = new ssf_stargate(src_id, tgt);
    starmap.insert
      (SSF_MAKE_PAIR(SSF_MAKE_PAIR(src_id, tgt->serialno()), sg));
    return sg;
  }
}
  
ssf_stargate* ssf_teleport::map_stargate(int src_id, int tgt_id)
{
  SSF_MAP(SSF_PAIR(int,int),ssf_stargate*)::iterator iter =
    starmap.find(SSF_MAKE_PAIR(src_id, tgt_id));
  assert(iter != starmap.end());
  return (*iter).second;
}
  
void ssf_teleport::transport(ssf_stargate* sg, ssf_kernel_event* evt)
{
  assert(sg->tgt == 0);
  assert(evt && evt->type == ssf_kernel_event::EVTYPE_CHANNEL);
  
  outChannel* oc = evt->localtlt->oc;
  ssf_messenger* msg = new ssf_messenger
    (evt->time, sg->srcid, sg->tgtid, evt->entity_serialno,
     oc->portno(), evt->event_seqno, evt->usrdat);
  delete evt;

  /*printf("[%d] preparing to transport msg dated %g\n", 
    SSF_USE_SHARED_RO(whoami), double(msg->time));*/
  ssf_mutex_wait(&msgque_mutex);
  msg->next = msg_queue;
  msg_queue = msg;
  ssf_mutex_signal(&msgque_mutex);

  if(!SSF_SELF) global_messaging();
}

/*
void ssf_teleport::run_server()
{
  do {
    global_message_barrier();
    //ssf_kernel_event::receive_balance();
  } while(SSF_USE_PRIVATE(epoch) < SSF_LTIME_INFINITY);
  if(!SSF_USE_SHARED_RO(silent)) finalize();
  MPICHECK(MPI_Barrier(MPI_COMM_WORLD));
  ssf_barrier(); // sync with the end of ssf_universe::big_boom
}
*/

#ifdef PRIME_SSF_SYNC_MPI
void ssf_teleport::init_round()
{
  assert(!SSF_SELF);
  if(SSF_USE_PRIVATE(epoch)>SSF_USE_SHARED(endtime)) return;

#ifdef DBGPRINT
  printf("[%d] initialize for the next round\n", SSF_USE_SHARED_RO(whoami));
  //fflush(0);
#endif

  assert(!outstanding_recv);
  outstanding_recv = 1;
  MPICHECK(MPI_Irecv(recvbuf, MSG_BUFSIZ, MPI_PACKED, MPI_ANY_SOURCE, 
		     MPI_ANY_TAG, MPI_COMM_WORLD, &request));

  recv_msgque = 0;
  msgrecvd = 0;

  num_to_send = SSF_NPROCS;
  up_sync = ndowns;
  down_sync = SSF_USE_SHARED_RO(whoami) ? 1 : 0;

  memset(upsent, 0, sizeof(long)*SSF_USE_SHARED_RO(nmachs));
  memset(downsent, 0, sizeof(long)*SSF_USE_SHARED_RO(nmachs));
}

int ssf_teleport::test_receive()
{
  if(!outstanding_recv) return 0;

  MPI_Status status;
  int gotmsg; 
  MPICHECK(MPI_Test(&request, &gotmsg, &status));
  if(gotmsg) { // there is an incoming message
    outstanding_recv = 0;

    int rbfsz;
    MPICHECK(MPI_Get_count(&status, MPI_PACKED, &rbfsz));
    assert(rbfsz > 0);

    if(status.MPI_TAG == MSGTAP_MESSAGE) {
      msgrecvd++;
#ifdef DBGPRINT
      printf("[%d] recv msg [size=%d] from %d (nrcvd=%ld)\n",
	     SSF_USE_SHARED_RO(whoami), rbfsz, status.MPI_SOURCE, msgrecvd);
      //fflush(0);
#endif

      // decode the message one by one and put onto the list
      int pos = 0;
      while(pos < rbfsz) {
	ssf_messenger* msg = new ssf_messenger;
	MPICHECK(msg->unpack(MPI_COMM_WORLD, recvbuf, pos, rbfsz));
#ifdef DBGPRINT
	printf("[%d] unpack an event [dated %g] from %d\n",
	       SSF_USE_SHARED_RO(whoami), double(msg->time), status.MPI_SOURCE);
	//fflush(0);
#endif
	msg->next = recv_msgque;
	recv_msgque = msg;
      }
    }

    // it's up-sync message from downstream
    else if(status.MPI_TAG == MSGTAP_SYNCUP) {
      assert(up_sync > 0);
      up_sync--;
      
      int pos = 0;
      MPICHECK(MPI_Unpack(recvbuf, rbfsz, &pos, (char*)upsent_rcvd, 
			  SSF_USE_SHARED_RO(nmachs), MPI_LONG, MPI_COMM_WORLD));
      for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
	upsent[i] += upsent_rcvd[i];
      }
#ifdef DBGPRINT
      printf("[%d] recv UP-SYNC from %d upsent=[",
	     SSF_USE_SHARED_RO(whoami), status.MPI_SOURCE);
      for(int j=0; j<SSF_USE_SHARED_RO(nmachs); j++)
	printf(" %d(%ld=>%ld)", j, upsent[j]-upsent_rcvd[j], upsent_rcvd[j]);
      printf(" ]\n"); //fflush(0);
#endif
    }

    // it's downsync message from upstream
    else {
      assert(status.MPI_TAG == MSGTAP_SYNCDOWN);
      assert(SSF_USE_SHARED_RO(whoami)>0);
      
      down_sync--;
      assert(down_sync == 0);
      
      int pos = 0;
      MPICHECK(MPI_Unpack(recvbuf, rbfsz, &pos, (char*)downsent,
			  SSF_USE_SHARED_RO(nmachs), MPI_LONG, MPI_COMM_WORLD));
#ifdef DBGPRINT
      printf("[%d] recv DOWN-SYNC from %d downsent=[",
	     SSF_USE_SHARED_RO(whoami), status.MPI_SOURCE);
      for(int j=0; j<SSF_USE_SHARED_RO(nmachs); j++)
	printf(" %d(%ld)", j, downsent[j]);
      printf(" ]\n"); //fflush(0);
#endif
    }

    // if more to come, initiate another asynchronous receive
    if(down_sync+up_sync > 0) {
      assert(outstanding_recv == 0);
      MPICHECK(MPI_Irecv(recvbuf, MSG_BUFSIZ, MPI_PACKED, MPI_ANY_SOURCE, 
			 MPI_ANY_TAG, MPI_COMM_WORLD, &request));
      outstanding_recv = 1;
    }

    return 1;
  } else return 0; // no more incoming
}

int ssf_teleport::global_messaging()
{
#ifdef DBGPRINT
  printf("[%d] global_messaging() begins (num_to_send=%d)\n", 
	 SSF_USE_SHARED_RO(whoami), num_to_send);
  //fflush(0);
#endif

  if(num_to_send == -2) return 0;

  MPI_Status status;

  //assert(outstanding_recv);
  for(;;) {
    // if at the end of this loop the value remains to be true, it
    // means that there's no action been taken by this iteration, and
    // therefore it's time to go to sleep
    int vain = 1;

    // if the synchronization phase (upstream or downstream) has not
    // finished
    while(down_sync+up_sync > 0) {
      if(test_receive()) vain = 0;
      else {
        break;
      }
    }

    // if there are more messages to be sent
    if(num_to_send>0 && ssf_mutex_try(&msgque_mutex)) {
      // take them all
      ssf_messenger* msg = msg_queue;
      msg_queue = 0;
      ssf_mutex_signal(&msgque_mutex);
      
      while(msg) {
	vain = 0;

	ssf_messenger* nxt = msg->next;
	if(msg->src_lp < 0) { 
	  // fake message is used to signal the end of the round
	  num_to_send--; // just counting here!!!
	  assert(num_to_send >= 0);
#ifdef DBGPRINT
	  printf("[%d] decrement num_to_send=%d [up_sync=%d, down_sync=%d]\n",
		 SSF_USE_SHARED_RO(whoami), num_to_send, up_sync, down_sync); fflush(0);
#endif
	} else {
	  // send the message
	  int whereto = ssf_universe::lpid_to_machid(msg->tgt_lp);
	  MPICHECK(msg->pack(MPI_COMM_WORLD, sendbuf[whereto], sendpos[whereto], MSG_BUFSIZ));
#ifdef DBGPRINT
	  printf("[%d] pack msg [dated %g] to send to %d (sendpos=%d) [up_sync=%d, down_sync=%d, num_to_send=%d]\n",
		 SSF_USE_SHARED_RO(whoami), double(msg->time), whereto, sendpos[whereto],
		 up_sync, down_sync, num_to_send); //fflush(0);
#endif

	  if(sendpos[whereto] >= MSG_SNDBUF_THRESHOLD) {
	    test_receive();

	    upsent[whereto]++;
#ifdef DBGPRINT
	    printf("[%d] send msg [size=%d] to %d, upsent=[",
		   SSF_USE_SHARED_RO(whoami), sendpos[whereto], whereto);
	    for(int j=0; j<SSF_USE_SHARED_RO(nmachs); j++)
	      printf(" %d(%ld)", j, upsent[j]);
	    printf(" ] [up_sync=%d, down_sync=%d, num_to_send=%d]\n",
		   up_sync, down_sync, num_to_send); //fflush(0);
#endif
	    MPICHECK(MPI_Bsend(sendbuf[whereto], sendpos[whereto], MPI_PACKED, whereto, 
			       MSGTAP_MESSAGE, MPI_COMM_WORLD));
#ifdef DBGPRINT
	    printf("[%d] done send msg\n", SSF_USE_SHARED_RO(whoami)); fflush(0);
#endif
	    sendpos[whereto] = 0;
	  }
	}

	delete msg;
	msg = nxt;
      }

      // it's only me left, so no more to send
      if(num_to_send == 1 && mainproc_hit_barrier) num_to_send = 0;
    }

    // if no more to send, and we received all syncs from downstream,
    // we'll sync upstream
    if(num_to_send == 0 && up_sync == 0) {
      vain = 0;

      // time to flush all the send buffers
      for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
	if(i == SSF_USE_SHARED_RO(whoami)) continue;
	if(sendpos[i] > 0) { // residual packed messages
	  test_receive();

	  upsent[i]++;
#ifdef DBGPRINT
	  printf("[%d] flush msg [size=%d] to %d, upsent=[",
		 SSF_USE_SHARED_RO(whoami), sendpos[i], i);
	  for(int j=0; j<SSF_USE_SHARED_RO(nmachs); j++)
	    printf(" %d(%ld)", j, upsent[j]);
	  printf(" ] [up_sync=%d, down_sync=%d, num_to_send=%d]\n",
		 up_sync, down_sync, num_to_send); //fflush(0);
#endif
	  MPICHECK(MPI_Bsend(sendbuf[i], sendpos[i], MPI_PACKED, i,
			     MSGTAP_MESSAGE, MPI_COMM_WORLD));
#ifdef DBGPRINT
	  printf("[%d] done flush msg\n", SSF_USE_SHARED_RO(whoami)); fflush(0);
#endif
	  sendpos[i] = 0;
	}
      }

      // sync with upstream if it's not the root
      if(SSF_USE_SHARED_RO(whoami) > 0) {
	test_receive();

	sendpos[SSF_USE_SHARED_RO(whoami)] = 0;
	MPICHECK(MPI_Pack((char*)upsent, SSF_USE_SHARED_RO(nmachs), MPI_LONG, 
			  sendbuf[SSF_USE_SHARED_RO(whoami)], MSG_BUFSIZ, 
			  &sendpos[SSF_USE_SHARED_RO(whoami)], MPI_COMM_WORLD));
#ifdef DBGPRINT
	printf("[%d] send UP-SYNC to %d, upsent=[", 
	       SSF_USE_SHARED_RO(whoami), up_rank);
	for(int j=0; j<SSF_USE_SHARED_RO(nmachs); j++)
	  printf(" %d(%ld)", j, upsent[j]);
	printf(" ] [up_sync=%d, down_sync=%d, num_to_send=%d]\n",
		   up_sync, down_sync, num_to_send); //fflush(0);
#endif
	MPICHECK(MPI_Bsend(sendbuf[SSF_USE_SHARED_RO(whoami)], sendpos[SSF_USE_SHARED_RO(whoami)], 
			   MPI_PACKED, up_rank, MSGTAP_SYNCUP, MPI_COMM_WORLD));	
#ifdef DBGPRINT
	printf("[%d] done send UP-SYNC\n", SSF_USE_SHARED_RO(whoami)); fflush(0);
#endif
      } else { // if this is the root, turn around
	memcpy((char*)downsent, (char*)upsent, 
	       sizeof(long)*SSF_USE_SHARED_RO(nmachs));
      }

      // this is used to indicate we have done with the up-sync
      num_to_send--;
    }

    // time for us to do down-sync
    if(num_to_send == -1 && down_sync == 0) {
      vain = 0;

      for(int i=0; i<ndowns; i++) {
	test_receive();

	sendpos[SSF_USE_SHARED_RO(whoami)] = 0;
	MPICHECK(MPI_Pack((char*)downsent, SSF_USE_SHARED_RO(nmachs), MPI_LONG,
			  sendbuf[SSF_USE_SHARED_RO(whoami)], MSG_BUFSIZ,
			  &sendpos[SSF_USE_SHARED_RO(whoami)], MPI_COMM_WORLD));
#ifdef DBGPRINT
	printf("[%d] send DOWN-SYNC to %d, downsent=[",
	       SSF_USE_SHARED_RO(whoami), down_ranks[i]);
	for(int j=0; j<SSF_USE_SHARED_RO(nmachs); j++)
	  printf(" %d(%ld)", j, downsent[j]);
	printf(" ] [up_sync=%d, down_sync=%d, num_to_send=%d]\n",
		   up_sync, down_sync, num_to_send); //fflush(0);
#endif
	MPICHECK(MPI_Bsend(sendbuf[SSF_USE_SHARED_RO(whoami)], sendpos[SSF_USE_SHARED_RO(whoami)], 
			   MPI_PACKED, down_ranks[i], MSGTAP_SYNCDOWN, MPI_COMM_WORLD));
#ifdef DBGPRINT
	printf("[%d] done send DOWN-SYNC\n", SSF_USE_SHARED_RO(whoami)); fflush(0);
#endif
      }
      
      // what's left is that we need to receive all messages targetting me
      while(msgrecvd < downsent[SSF_USE_SHARED_RO(whoami)]) {
#ifdef DBGPRINT
	printf("[%d] blocked for %ld msgs\n", SSF_USE_SHARED_RO(whoami),
	       downsent[SSF_USE_SHARED_RO(whoami)]-msgrecvd); //fflush(0);
#endif

	if(outstanding_recv) {
	  MPICHECK(MPI_Wait(&request, &status));
	  outstanding_recv = 0; // no more immediate receives
	} else {
	  MPICHECK(MPI_Recv(recvbuf, MSG_BUFSIZ, MPI_PACKED, MPI_ANY_SOURCE, 
			    MPI_ANY_TAG, MPI_COMM_WORLD, &status));
	}
	assert(status.MPI_TAG == MSGTAP_MESSAGE);
	msgrecvd++;

	int rbfsz;
	MPICHECK(MPI_Get_count(&status, MPI_PACKED, &rbfsz));
	assert(rbfsz > 0);

#ifdef DBGPRINT
	printf("[%d] recv msg [size=%d] from %d (nrcvd=%ld)\n",
	       SSF_USE_SHARED_RO(whoami), rbfsz, status.MPI_SOURCE, msgrecvd);
	//fflush(0);
#endif

	// decode the message and put onto the list
	int pos = 0;
	while(pos < rbfsz) {
	  ssf_messenger* msg = new ssf_messenger;
	  MPICHECK(msg->unpack(MPI_COMM_WORLD, recvbuf, pos, rbfsz));
#ifdef DBGPRINT
	  printf("[%d] unpack msg [dated %g] from %d\n", 
		 SSF_USE_SHARED_RO(whoami), double(msg->time), status.MPI_SOURCE);
	  //fflush(0);
#endif
	  msg->next = recv_msgque;
	  recv_msgque = msg;
	}
      }
      assert(msgrecvd == downsent[SSF_USE_SHARED_RO(whoami)]);

      // Deliver the received messages
      while(recv_msgque) {
	ssf_messenger* msg = recv_msgque;
	recv_msgque = msg->next;

	ssf_stargate* sg = map_stargate(msg->src_lp, msg->tgt_lp);
	assert(sg);
	ssf_remote_tlt* rtlt = sg->map_remote_tlt(msg->src_ent, msg->src_port);
	assert(rtlt);
	Event* usrevt = 0;
	if(msg->usrevt_ident >= 0) {
	  usrevt = Event::_evt_create_registered_event
	    (msg->usrevt_ident, msg->usrevt_packed);
	  if(!usrevt) {
	    char m[256];
	    sprintf(m, "ident=%d", msg->usrevt_ident);
	    ssf_throw_exception(ssf_exception::kernel_newevt, m);
	  }
	}
	ssf_kernel_event* kevt = new ssf_kernel_event
	  (msg->time, ssf_kernel_event::EVTYPE_INCHANNEL, usrevt);
	kevt->entity_serialno = msg->src_ent;
	kevt->event_seqno = msg->serial_no;
	kevt->mapnode = rtlt->ic_head;
	sg->tgt->insert_event(kevt);
	delete msg;
      }

      num_to_send--;
      break;
    }

    if(vain) {
#ifdef DBGPRINT
      printf("[%d] global_messaging() ends in vain: "
	     "up_sync=%d, down_sync=%d, num_to_send=%d, msgrecvd=%ld (expected=%ld)\n",
	     SSF_USE_SHARED_RO(whoami), up_sync, down_sync, num_to_send, msgrecvd,
	     downsent[SSF_USE_SHARED_RO(whoami)]); //fflush(0);
#endif
#if PRIME_SSF_INSTRUMENT
/*  if (up_sync) {
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->setSynchMachines(upsent,ssf_universe::ACTION_SYNCHRONIZING_UPSTREAM);
  }
  else if (down_sync) {
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->setSynchMachines(downsent,ssf_universe::ACTION_SYNCHRONIZING_DOWNSTREAM);
  }
  else if (num_to_send) {
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->setAction(ssf_universe::ACTION_SENDING_MESSAGES);
  }
  else {
    ((ssf_universe*)SSF_USE_PRIVATE(universe))->setAction(ssf_universe::ACTION_RECIEVING_MESSAGES);
  }*/
#endif
      return 1; // it's not done
    }
  }

#ifdef DBGPRINT
  printf("[%d] global_messaging() ends successful\n", SSF_USE_SHARED_RO(whoami)); //fflush(0);
#endif
  return 0;
}
#endif /*PRIME_SSF_SYNC_MPI*/

void ssf_teleport::global_message_barrier()
{
#ifdef PRIME_SSF_SYNC_MPI
  // we have no alternative but to wait until it's done
  while(global_messaging()) ssf_yield_proc();

  // settle next window
  SSF_USE_PRIVATE(epoch) += SSF_USE_SHARED(epoch_length);

#ifdef DBGPRINT
  printf("[%d] entering barrier: epoch=%g\n", 
	 SSF_USE_SHARED_RO(whoami), double(SSF_USE_PRIVATE(epoch)));
  //fflush(0);
#endif
  MPICHECK(MPI_Barrier(MPI_COMM_WORLD));

  init_round(); // prepare for the next round

  SSF_USE_PRIVATE(epoch) = ssf_min_reduction(SSF_USE_PRIVATE(epoch));
#if PRIME_SSF_DEBUG
  ssf_debug(SSF_DEBUG_MPI, "<MPI> [%d] new epoch = %g\n", 
	    SSF_USE_SHARED_RO(whoami), (double)SSF_USE_PRIVATE(epoch));
#endif
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
  if(SSF_USE_SHARED_RO(nmachs) == 1) {
    SSF_USE_PRIVATE(epoch) = ssf_min_reduction(SSF_LTIME_INFINITY);
    return;
  }

  char sendbuf[MSG_BUFSIZ];
  int sendpos = 0;

  int num_to_send = SSF_NPROCS;
#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
  int* sendcnt = new int[SSF_USE_SHARED_RO(nmachs)]; assert(sendcnt);
  memset(sendcnt, 0, SSF_USE_SHARED_RO(nmachs)*sizeof(int));
#endif

  for(;;) {
    int vain = 1;

    if(num_to_send>0 && ssf_mutex_try(&msgque_mutex)) {
      ssf_messenger* msg = msg_queue;
      msg_queue = 0;
      ssf_mutex_signal(&msgque_mutex);
      
      while(msg) {
	vain = 0;
	ssf_messenger* nxt = msg->next;
	if(msg->src_lp < 0) { // fake message, signaling the end of the round
	  num_to_send--; // just counting here!!!
	  assert(num_to_send >= 0);
	} else {
	  // send the message
	  int whereto = ssf_universe::lpid_to_machid(msg->tgt_lp);
	  TM_PutTag((TM_TagType*)sendbuf);
	  sendpos = sizeof(TM_TagType);
	  int ret = msg->pack(sendbuf, sendpos, MSG_BUFSIZ);
	  if(ret) {
	    char msg[256];
	    sprintf(msg, "destination=%d", whereto);
	    ssf_throw_exception(ssf_exception::kernel_pack, msg);
	  }
	  assert(sendpos <= MSG_BUFSIZ);
	  FM_stream* strm = FM_begin_message
	    (whereto, sendpos, fmhandler_msgs);
	  assert(strm);
	  int net_sendpos = SSF_BYTE_ORDER_32(sendpos);
	  FM_send_piece(strm, &net_sendpos, sizeof(int));
	  FM_send_piece(strm, sendbuf, sendpos);
	  FM_end_message(strm);
#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
	  sendcnt[whereto]++;
#else
	  TM_Out(msg->time-SSF_USE_SHARED(startime), 1);
#endif

#if PRIME_SSF_DEBUG 
	  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] global_message_barrier() "
		      "send_msg=>%d: src_lp=%d, src_ent=%d, src_port=%d, time=%lf\n",
		      SSF_USE_SHARED_RO(whoami), SSF_SELF, whereto, msg->src_lp,
		      msg->src_ent, msg->src_port, (double)msg->time);
#endif

	  FM_extract(~0); ssf_yield_proc();
	}
	delete msg;
	msg = nxt;
      }
    
      if(num_to_send == 1 && mainproc_hit_barrier) {
	num_to_send = 0;
#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
	for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++) {
	  if(i == SSF_USE_SHARED_RO(whoami)) continue;
	  FM_stream* strm = FM_begin_message
	    (i, sizeof(int), fmhandler_num_msgs);
	  assert(strm);
	  sendcnt[i] = SSF_BYTE_ORDER_32(sendcnt[i]);
	  FM_send_piece(strm, &sendcnt[i], sizeof(int));
	  FM_end_message(strm);

	  FM_extract(~0); ssf_yield_proc();
	}
#endif
	break;
      }
    }
    if(vain) ssf_yield_proc();
  }

#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
  while(!libsynk_msgs_synchronized) { FM_extract(~0); ssf_yield_proc(); }
  libsynk_msgs_synchronized = 0;
  delete[] sendcnt;
#endif

  ltime_t lbts = SSF_USE_PRIVATE(now)+TM_TopoGetMinExternalLA();
#if PRIME_SSF_DEBUG 
  ssf_debug(SSF_DEBUG_LIBSYNK, "<LIBSYNK> [%d:%d] global_message_barrier() "
	      "initiate LBTS: %lf\n", SSF_USE_SHARED_RO(whoami), 
	      SSF_SELF, (double)lbts);
#endif
  long transaction;
  if(TM_StartLBTS(lbts-SSF_USE_SHARED(startime), TM_TIME_QUAL_EXCL,
		  libsynk_lbtsdone, &transaction) != TM_SUCCESS)
    ssf_throw_exception(ssf_exception::kernel_lbts);
  
  while(!libsynk_transaction_done(transaction)) { 
    FM_extract(~0); TM_Tick(); ssf_yield_proc();
  }
  //SSF_USE_PRIVATE(epoch) = ssf_min_reduction(SSF_USE_PRIVATE(epoch));
#endif /*PRIME_SSF_SYNC_LIBSYNK*/
}
#endif /*PRIME_SSF_DISTSIM*/

}; // namespace ssf
}; // namespace prime

/*
 * Copyright (c) 2007-2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 * 
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * noninfringement. In no event shall Florida International University be
 * liable for any claim, damages or other liability, whether in an
 * action of contract, tort or otherwise, arising from, out of or in
 * connection with the software or the use or other dealings in the
 * software.
 * 
 * This software is developed and maintained by
 *
 *   The PRIME Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, FL 33199, USA
 *
 * Contact Jason Liu <liux@cis.fiu.edu> for questions regarding the use
 * of this software.
 */

/*
 * $Id$
 */
