/*
Copyright (c) 2005, Regents of the University of California All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:
   
     * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of the Los Alamos National Laboratory nor the
       names of its contributors may be used to endorse or promote products
       derived from this software without specific prior written permission.
   
   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
   OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
   TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
   @(#) $Header: /n/CVS/sirt/libpcap/pcap-ring.h,v 0.10 2005/07/18 16:05:12 cpw Exp $ (CPW)
*/
#ifndef PCAP_RING_H
#define PCAP_RING_H 1
# ifdef linux

#  include <sys/socket.h>
#  include <linux/if_packet.h>
#  include <time.h>

#  ifndef SOL_PACKET /* */
#   define SOL_PACKET  263 /* */
#  endif /* SOL_PACKET */

#  ifdef DO_RING
#ifndef mb
# define X86_FEATURE_XMM2 (0*32+26)
# ifdef __x86_64__
#  define alternative(oldinstr, newinstr, feature)    \
    asm volatile ("661:\n\t" oldinstr "\n662:\n"             \
              ".section .altinstructions,\"a\"\n"            \
              "  .align 8\n"                       \
              "  .quad 661b\n"            /* label */          \
              "  .quad 663f\n"        /* new instruction */ \
              "  .byte %c0\n"             /* feature bit */    \
              "  .byte 662b-661b\n"       /* sourcelen */      \
              "  .byte 664f-663f\n"       /* replacementlen */ \
              ".previous\n"                 \
              ".section .altinstr_replacement,\"ax\"\n"     \
              "663:\n\t" newinstr "\n664:\n"   /* replacement */ \
              ".previous" :: "i" (feature) : "memory")
#  define mb()    asm volatile("mfence":::"memory")
# else
#  define alternative(oldinstr, newinstr, feature) 	                \
	asm volatile ("661:\n\t" oldinstr "\n662:\n" 		        \
		      ".section .altinstructions,\"a\"\n"     	        \
		      "  .align 4\n"				        \
		      "  .long 661b\n"            /* label */           \
		      "  .long 663f\n"		  /* new instruction */ \
		      "  .byte %c0\n"             /* feature bit */     \
		      "  .byte 662b-661b\n"       /* sourcelen */       \
		      "  .byte 664f-663f\n"       /* replacementlen */  \
		      ".previous\n"					\
		      ".section .altinstr_replacement,\"ax\"\n"		\
		      "663:\n\t" newinstr "\n664:\n"   /* replacement */\
		      ".previous" :: "i" (feature) : "memory")  

#  define mb() alternative("lock; addl $0,0(%%esp)", "mfence", X86_FEATURE_XMM2)
# endif

#endif

#define MAX_IOVEC_SIZE 32760 /* only applies to 2.2 - 2.4.25 kernels */
                              /* 2.4.26 has appropriate patch */

#define RSTAT_FIELD(bit) (1<<(bit))

#define RSTAT_TV_STARTDATE   RSTAT_FIELD(0)
#define RSTAT_PKTS_PROCESSED RSTAT_FIELD(1)
#define RSTAT_PKTS_DROPPED   RSTAT_FIELD(2)
#define RSTAT_PKTS_TOTAL     RSTAT_FIELD(3)
#define RSTAT_PKTS_IGNORED   RSTAT_FIELD(4)
#define RSTAT_DEV_PACKETS    RSTAT_FIELD(5)
#define RSTAT_DEV_BYTES      RSTAT_FIELD(6)
#define RSTAT_BYTES_RECEIVED RSTAT_FIELD(7)
#define RSTAT_POLL_CALLS     RSTAT_FIELD(8)
#define RSTAT_RING_INDEX     RSTAT_FIELD(9)
#define RSTAT_RING_CONSEC    RSTAT_FIELD(10)
#define RSTAT_SPECIOUS       RSTAT_FIELD(11)
#define RSTAT_TV_ELAPSED     RSTAT_FIELD(12)
#define RSTAT_RX_ERRORS      RSTAT_FIELD(13)
#define RSTAT_RX_DROPS       RSTAT_FIELD(14)
#define RSTAT_TX_ERRORS      RSTAT_FIELD(15)
#define RSTAT_TX_DROPS       RSTAT_FIELD(16)
#define RSTAT_MULTICAST      RSTAT_FIELD(17)
#define RSTAT_TV_ENDDATE     RSTAT_FIELD(18)
#define RSTAT_TV_WAIT        RSTAT_FIELD(19)
#define RSTAT_TV_PROCESSING  RSTAT_FIELD(20)

/* TV_ADD and TV_SUB lifted from tcpslice (Vern Paxson vern@ee.lbl.gov) */
 
#define START_SECS(x) x->rg.rs.r_start.tv_sec
#define START_USECS(x) x->rg.rs.r_start.tv_usec
#define STOP_SECS(x) x->rg.rs.r_stop.tv_sec
#define STOP_USECS(x) x->rg.rs.r_stop.tv_usec

/* compute a + b, store in c */
#define	TV_ADD(a,b,c) \
	(c).tv_sec = (a).tv_sec + (b).tv_sec; \
	(c).tv_usec = (a).tv_usec + (b).tv_usec; \
	if ((c).tv_usec > 1000000) { \
		(c).tv_usec -= 1000000; (c).tv_sec += 1; \
	}

/* compute a - b, store in c */
#define	TV_SUB(a,b,c) \
	(c).tv_sec = (a).tv_sec - (b).tv_sec; \
	if ((a).tv_usec < (b).tv_usec) { \
		(c).tv_sec -= 1; \
		(c).tv_usec = ((a).tv_usec + 1000000) - (b).tv_usec; \
	} else \
		(c).tv_usec = (a).tv_usec - (b).tv_usec;

/* interface statistic indexes for stats from /proc/net/dev */
#define IFACE_MAXFIELDS 16
#define rx_bytes  0
#define rx_packets  1
#define rx_errors  2
#define rx_dropped  3
#define rx_fifo_errors  4
#define rx_frame_errors  5
#define rx_compressed  6
#define rx_multicast  7
#define tx_bytes  8
#define tx_packets  9
#define tx_errors 10
#define tx_dropped 11
#define tx_fifo_errors 12
#define tx_collisions 13
#define tx_carrier_errors 14
#define tx_compressed 15

#define DEFLT_RAW      1
#define DEFLT_PROTO    0
#define DEFLT_MADDR    NULL
#define DEFLT_FRAMES   1024 /* packets */
#define DEFLT_MEM      1536 /* kilobytes */

/*
 * used by packet_ring_stats()
 */
typedef struct ring_stat {
   u_long r_recv;    /* number of packets received rg.rs.r_recv */
   u_long r_drop;    /* number of packets dropped see pcap_stats */
   u_long r_bytes;   /* number of bytes received, rg.rs.r_bytes */
   u_long r_ignore;  /* number of packets ignored in ring_recv */
   u_long r_consec;  /* number of packets received consec, rg.rs.r_consec */
   u_long r_polls;   /* number calls to poll rg.rs.r_polls */
   u_long r_specious; /* poll says there is a packet, but there isn't */
   struct timeval r_waits; /* time waiting for a packet, rg.rs.r_waits */
   struct timeval r_start; /* start collecting rg.rs.r_start */
   struct timeval r_stop;  /* stop collecting rg.rs.r_stop */
} ring_stat;

typedef struct pcap_ring
{
    char         *buf;
    void         *iovec;     /* non zero implies ring use */
    u_int8_t     *maddr;
    char	 *fname;
    ring_stat     rs;
    unsigned long int ifpkts;
    unsigned long int ifbytes;
    unsigned long int rxerrs;
    unsigned long int rxdrops;
    unsigned long int multi;
    unsigned long int txerrs;
    unsigned long int txdrops;
    time_t timeout; /* return errno ETIMEOUT when timeout reached */
    unsigned int  bufsize;
    unsigned int  iovmax;
    unsigned int  iovhead;
    long	  mem; /* size of ring in kilobytes, PCAP_MEM*/
    long	  ct;  /* size of ring in packets, PCAP_FRAMES) */
    int		  have_ds;   /* non zero if /proc/net/dev can be read */
    int           soft_bpf;
    int           pollAgain; /* used to die if shared memory lost */
    int           promisc;
    int		  statperiod; /* replacement for to_ms in desision to print */
    int           statbits;  /* see STAT FIELDS */
    u_int16_t     proto;
    time_t	  tea;    /* used to calculate when to gen a stat line */

} pcap_ring;

typedef struct live_args
{
	char		head[4];
	time_t		timeout;
	time_t		statperiod;
	long		frame_ct;
	long		max_mem;
	int		version;
	const char	*device;
	char		*ebuf;
	u_int8_t	*maddr;
	int		snaplen;
	int		promisc;
	int		to_ms;
	int		raw;
	int		verbose;
	int		statbits;
	u_int16_t	proto;
} live_args;

void packet_ring_close (pcap_t *);
int packet_head_check (pcap_t *);
struct timeval *packet_ring_diff(struct timeval *, struct timeval *, struct timeval *);
int packet_tring_setup(pcap_t *);
int packet_dev_stats(pcap_t *, unsigned long int *);
int packet_discard(pcap_t *);

#  endif /* DO_RING */
# endif /* linux */
#endif /* !PCAP_RING_H */
