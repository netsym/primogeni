/*
 * teleport :- teleport for remote message passing.
 *
 * The data structure is need to send and receive messages to remote
 * ssf instances in a distributed memory environment.
 */

#ifndef __PRIME_SSF_TELEPORT_H__
#define __PRIME_SSF_TELEPORT_H__

#ifndef __PRIME_SSF_COMPOSITE_H__
#error "teleport.h must be included by composite.h"
#endif

// FIXME: libsynk seems having problems with transient messages when
// computing the next synchronization window for ssf: the temporary
// solution is to count all messages, both sent and received, and wait
// until all messages are delivered before LBTS calculation.
//#define SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES 1

namespace prime {
namespace ssf {

  // portal to cross-memory-space messages.
  class ssf_teleport : public ssf_permanent_object {
  public:
    static SSF_MAP(SSF_PAIR(int,int),ssf_stargate*) starmap;
    static ssf_stargate* map_stargate(ssf_logical_process* src, ssf_logical_process* tgt);

#if PRIME_SSF_DISTSIM
    long MSG_SNDBUF_THRESHOLD;
    long MSG_BUFSIZ;

#ifdef PRIME_SSF_SYNC_MPI
    int up_rank; // the upstream processor in the binomial tree
    int ndowns; // number of downstream processors in the binomial tree
    int* down_ranks; // ranks of the downstream processors

    // there's a separate buffer for each processor (including the
    // processor running the teleport, which is used for sending the
    // upsync and downsync messages)
    char** sendbuf;
    int* sendpos; // size of data already in the buffers

    int outstanding_recv; // whether the immediate receive is posted
    MPI_Request request; // if yes, here's the handle
    char* recvbuf; // and here's the buffer

    // all decoded messages during the window are queued up here (the
    // message received the latest being at the front of the list)
    ssf_messenger* recv_msgque;
    long msgrecvd; // number of messages received so far

    // how many processors that teleport needs to help send messages
    // during this window (including the processor running teleport)
    int num_to_send;

    // # upsync messages remain to be received from downstream
    int up_sync;

    // # downsync messages remain to be received from upstream
    int down_sync;

    // # messages to be received by all processors downstream and myself
    long* upsent;
    long* upsent_rcvd; // buffer for received upsent during up-sync phase

    // final # messages to be received by all processors (collected by
    // the root of the binomial tree and then broadcasted downstream)
    long* downsent;

    // an indicator that thread 0 (which is in charge of synchronizing
    // with other mpi processes) has reached the global barrier
    bool mainproc_hit_barrier;
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
    static long libsynk_last_transaction;
    static unsigned int fmhandler_msgs;
    static unsigned int fmhandler_results;
#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
    static unsigned int fmhandler_num_msgs;
    static int libsynk_got_num_msgs;
    static int libsynk_msgs_to_get;
    static int libsynk_got_msgs;
    static int libsynk_msgs_synchronized;
#endif
    static int libsynk_got_results;
    static int libsynk_results_synchronized;

    static long libsynk_lbtsstarted(long transaction, TM_Time* mintime,
				    TM_TimeQual* mintimequal, 
				    TM_LBTSDoneProc* donehandler);
    static void libsynk_lbtsdone(TM_Time result, TM_TimeQual resultqual, 
				 long transaction);
    static int libsynk_transaction_done(long transaction);
#if SSF_LIBSYNK_COUNT_TRANSIENT_MESSAGES
    static int libsynk_get_num_msgs(FM_stream* strm, unsigned msgsiz);
#endif
    static int libsynk_get_msgs(FM_stream* strm, unsigned msgsiz);
    static int libsynk_get_results(FM_stream* strm, unsigned msgsiz);
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

    static ssf_stargate* map_stargate(ssf_logical_process* src, int tgtid);
    static ssf_stargate* map_stargate(int srcid, ssf_logical_process* tgt);
    static ssf_stargate* map_stargate(int srcid, int tgtid);

    // use to communicate with other processes on the shared memory
    ssf_mutex msgque_mutex;
    ssf_messenger* msg_queue;

    void transport(ssf_stargate* stargate, ssf_kernel_event* evt);

#ifdef PRIME_SSF_SYNC_MPI
    void init_round();
    int test_receive();
    int global_messaging();
#endif /*PRIME_SSF_SYNC_MPI*/
    
    void global_message_barrier();
#endif /*PRIME_SSF_DISTSIM*/

    ssf_teleport();
    virtual ~ssf_teleport();

    void initialize();
    void finalize();

    void serialize_reporting_begin();
    void serialize_reporting_end();

    int  calculate_lbts();
  }; /*ssf_teleport*/

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_TELEPORT_H__*/

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
