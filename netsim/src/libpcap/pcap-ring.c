/*Copyright (c) 2006, Regents of the University of California All rights reserved.
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
*/
#ifndef lint
static const char rcsid[] _U_ = "@#( $Header: /n/CVS/sirt/libpcap/pcap-ring.c,v 0.9 2005/06/10 23:06:31 cpw Exp $ (CPW)";
#endif

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif
#include <unistd.h>
#include <string.h>
# include <stdlib.h>
# include <errno.h>
# include <sys/uio.h>
# include <net/if.h>
# include <sys/ioctl.h>
# include <sys/poll.h>
# include <sys/mman.h>
#ifdef  HAVE_64BIT_TIME_T
#define LOCK_PREFIX "lock ; "
#endif
# include <linux/if_ether.h>
# include <netinet/in.h>

#include "pcap-int.h"
#include "sll.h"

#include <sys/utsname.h>

#ifdef DO_RING

#undef RING_DEBUG
//#define RING_DEBUG

#define LOST_MY_HEAD 2    /* find iovhead if pollAgain == LOST_MY_HEAD */

#ifdef PACKET_RX_RING
#define MAX_SLAB_SIZE 131072  // valid for 2.4 and 2.6 kernels; /proc/slabinfo
#define MAX_24RING_MEMORY ((unsigned long)(275219456))  // max ring size
#define MAX_26RING_MEMORY ((unsigned long)(2147483648))  // max ring size
#define MAX_ORDER 8 /* #include kernel source <linux/mmzone.h> */
int
packet_tring_setup(pcap_t *p)
{
    int i, k, idx, frames_per_block;
    struct tpacket_req req;
    unsigned long mem;

    req.tp_frame_size = TPACKET_ALIGN(TPACKET_HDRLEN) + TPACKET_ALIGN(p->snapshot+p->md.offset);

    if (p->rg.mem > 0) { /* use user-defined max */
      mem = p->rg.mem * 1024;
    } else if (p->rg.mem == 0) {
      return -1; /* PCAP_MEMORY=0 negates mmaped mode */
    } else {
      struct utsname utsbuf;
      if (uname(&utsbuf)) { mem = 65536; }
      else {
        if (utsbuf.release[0] == '2' && utsbuf.release[2] == '4') { 
 	  mem=MAX_24RING_MEMORY;
        } else if (utsbuf.release[0] == '2' && utsbuf.release[2] == '6') {
          mem=MAX_26RING_MEMORY;
        } else {
          mem= 1048576;
        }
      }
    }

    /* Use as many blocks as possible */
    req.tp_block_nr = MAX_SLAB_SIZE / sizeof(void*);
    /* In case we compile 32-bit, but run on a 64-bit kernel, scale by 2 */
    req.tp_block_nr /= 2;
    /* Get lower-bound of block size */
    req.tp_block_size = mem / req.tp_block_nr;

    /* Now round up to nearest order of page size */
    int order;
    for (order = 0; order <= MAX_ORDER; order++) {
	if ( (getpagesize() << order) >= req.tp_block_size) {
		break;
	}
    }
    req.tp_block_size = getpagesize() << order;

    /* And then recompute number of blocks precisely */
    req.tp_block_nr = mem / req.tp_block_size;
    /* Round-up rather than down */
    if (req.tp_block_nr * req.tp_block_size < mem) req.tp_block_nr++;

    frames_per_block = req.tp_block_size / req.tp_frame_size;
    req.tp_frame_nr = frames_per_block * req.tp_block_nr;
    p->rg.ct = req.tp_frame_nr;

#ifdef RING_DEBUG
    fprintf (stderr, "DEBUG, tring setup:block_size = %d, block_nr = %d, frame_size = %d, frame_nr = %d, mem = %g\n", req.tp_block_size, req.tp_block_nr, req.tp_frame_size, req.tp_frame_nr, (double)req.tp_block_size * req.tp_block_nr);
#endif
    if (setsockopt(p->fd, SOL_PACKET, PACKET_RX_RING, (void*)&req,
                            sizeof(req))) {
        perror("Error: setsockopt(PACKET_RX_RING)");
        return -2;
    }

    struct iovec *ring;
    unsigned long int devstats[IFACE_MAXFIELDS];
    ring = (struct iovec *) malloc(p->rg.ct * sizeof(struct iovec));
    if (!ring)
    {
        perror ("Error: Ring setup failure, malloc");
        return -2;
    }

    p->rg.buf = (void*)mmap(0, req.tp_block_nr * req.tp_block_size, PROT_READ|PROT_WRITE,
                    MAP_SHARED, p->fd, 0);
    if ((long)p->rg.buf == -1L) {
        perror("Error: Could not allocate shared memory");
        memset(&req, 0, sizeof(req));
        p->rg.buf=NULL;
        if (setsockopt(p->fd, SOL_PACKET, PACKET_RX_RING, (void*)&req,
                                sizeof(req)))
        {
            perror("Error: Failed to destroy ring");
        }
        free (ring);
        return -2;
    }

    idx=0;
    for (i=0; i<req.tp_block_nr; i++) {
        for (k=0; k<frames_per_block; k++) {
            ring[idx].iov_base = p->rg.buf + req.tp_block_size*i + k*req.tp_frame_size;
            ring[idx].iov_len = req.tp_frame_size;
            *(unsigned long*)ring[idx].iov_base = 0;
            idx++;
        }
    }

    p->rg.bufsize = req.tp_block_nr * req.tp_block_size;
    p->rg.iovhead = 0;
    p->rg.iovmax = p->rg.ct - 1;
    p->rg.iovec = ring;
    p->rg.fname = NULL;
    p->rg.pollAgain = 0;
    START_SECS(p) = 0;
    if (packet_dev_stats(p, &devstats[0]))
    {
      p->rg.ifpkts = devstats[tx_packets] + devstats[rx_packets];
      p->rg.ifbytes = devstats[tx_bytes] + devstats[rx_bytes];
      p->rg.multi = devstats[rx_multicast];
      p->rg.rxerrs = devstats[rx_errors];
      p->rg.rxdrops = devstats[rx_dropped];
      p->rg.txerrs = devstats[tx_errors];
      p->rg.txdrops = devstats[tx_dropped];
      p->rg.have_ds = 1;
    }
    else
    {
      p->rg.have_ds = 0;
    }

    return 0;
}
#endif /* PACKET_RX_RING */

#define PACKET_AVAILABLE (*(unsigned long*)ring[p->rg.iovhead].iov_base)
int
pcap_ring_recv(pcap_t *p, int cnt, pcap_handler callback, u_char *user)
{
  struct iovec *ring = p->rg.iovec;
  time_t	poll_timeout = 0; /* milliseconds */
  int		n = 0;
#ifdef RING_STATS
  /* p->md.timeout is in milliseconds */
  time_t	pdelta = 0; /* seconds */
  int		consec = 0;

    if (!p->rg.statperiod)
    {
      if ((p->md.timeout) || (cnt>0) ) START_SECS(p) = 0;
    }
    /*       else {
     *       START_SECS(p) = 0;
     *   }
2006 */
#else  /*RING_STATS not defined*/
    if ((p->md.timeout) || (cnt>0) ) START_SECS(p) = 0;
#endif /*RING_STATS*/

  if (!p->rg.iovec) return (-1); /* called inappropriately (no ring defined) */

  if (cnt<=0) cnt=-1; /* read foreaver */

  for (;;)
  {
    while (PACKET_AVAILABLE)
    {
      struct tpacket_hdr *h = ring[p->rg.iovhead].iov_base;
      struct sockaddr_ll *sll = (void*)h + TPACKET_ALIGN(sizeof(*h));
      unsigned char *bp = (unsigned char*)h + h->tp_mac;
      int delta; // recomputed for each packet
      struct pcap_pkthdr h1;
      struct sll_header       *hdrp;

      //p->md.stat.ps_recv++;
      p->md.packets_read++;
      p->rg.pollAgain = 0;

#ifdef RING_STATS
      p->rg.rs.r_recv++;
      p->rg.rs.r_bytes += h->tp_len; 
      consec++;
      if (consec > p->rg.rs.r_consec)
	      p->rg.rs.r_consec = consec;
#endif /*RING_STATS*/

      /*
      if (p->md.use_bpf == 2)
      {
        if (sll->sll_pkttype != PACKET_OUTGOING)
        {
           memset(bp-14, 0, 6);
           if (sll->sll_pkttype == PACKET_BROADCAST) memset(bp-14, 0xFF, 6);
           else if (sll->sll_pkttype == PACKET_MULTICAST) *(bp-14) = 1;
           else *(bp-14+5) = 1;
           memcpy(bp-14+6, sll->sll_addr, 6);
        }
        else
        {
           memcpy(bp-14, sll->sll_addr, 6);
           memset(bp-14+6, 0, 6);
        }
        *(unsigned short*)(bp-2) = sll->sll_protocol;
      }
      */
      if (p->md.cooked)
      {
	 hdrp = (struct sll_header *)((char *)bp - sizeof(struct sll_header));  
	 switch (sll->sll_pkttype) {

	    case PACKET_HOST:
		hdrp->sll_pkttype = htons(LINUX_SLL_HOST);
		break;

	    case PACKET_BROADCAST:
		hdrp->sll_pkttype = htons(LINUX_SLL_BROADCAST);
		break;

	    case PACKET_MULTICAST:
		hdrp->sll_pkttype = htons(LINUX_SLL_MULTICAST);
		break;

	    case PACKET_OTHERHOST:
		hdrp->sll_pkttype = htons(LINUX_SLL_OTHERHOST);
		break;

	    case PACKET_OUTGOING:
		hdrp->sll_pkttype = htons(LINUX_SLL_OUTGOING);
		break;

	    default:
		hdrp->sll_pkttype = -1;
		break;
	    }

	    hdrp->sll_hatype = htons(sll->sll_hatype);
	    hdrp->sll_halen = htons(sll->sll_halen);
	    memcpy(hdrp->sll_addr, sll->sll_addr,
		  (sll->sll_halen > SLL_ADDRLEN) ? SLL_ADDRLEN : sll->sll_halen);
	    hdrp->sll_protocol = sll->sll_protocol;
      }

      if ((p->fcode.bf_insns == NULL ||
        bpf_filter(p->fcode.bf_insns, bp, h->tp_len, h->tp_snaplen)) && 
	!(!p->md.sock_packet && sll->sll_ifindex == p->md.lo_ifindex &&
		sll->sll_pkttype == PACKET_OUTGOING)
	)
      {
        h1.ts.tv_sec = h->tp_sec;
        h1.ts.tv_usec = h->tp_usec;
        h1.caplen = h->tp_snaplen;
        h1.len = h->tp_len;
/*
        if (p->md.use_bpf == 2)
        {
            h1.caplen += 14;
            h1.len    += 14;
            bp    -= 14;
        }
*/
	if (p->md.cooked)
	{
	    h1.caplen += p->md.offset;
	    h1.len    += p->md.offset;
	    bp        -= p->md.offset;
	}
	//XXX START NATE
	switch(p->direction) {
	case PCAP_D_INOUT:
	        (*callback)(user, (void *)&h1, bp);
	        n++;
	        break;
	case PCAP_D_OUT:
		if(sll->sll_pkttype == PACKET_OUTGOING) {
	        (*callback)(user, (void *)&h1, bp);
	        n++;
		}
		break;
	case PCAP_D_IN:
		if(sll->sll_pkttype != PACKET_OUTGOING) {
	        (*callback)(user, (void *)&h1, bp);
	        n++;
		}
		break;
	}
	//XXX END NATE
    //XXX OLD (*callback)(user, (void *)&h1, bp);
    //XXX OLD  n++;
      }
#ifdef RING_STATS
      else
      {
          p->rg.rs.r_ignore++;
      }
#endif
      if (!(START_SECS(p)))
      {
          START_SECS(p) = h->tp_sec;
          START_USECS(p) = h->tp_usec;
	  if (!p->rg.tea) p->rg.tea = h->tp_sec + p->rg.statperiod;
      }
      STOP_SECS(p) = h->tp_sec;
      STOP_USECS(p) = h->tp_usec;

#ifdef RING_DEBUG
  fprintf (stderr, "\nstart_secs=%d, stop_secs=%d\n", START_SECS(p), STOP_SECS(p));
#endif

      h->tp_status = 0;
      /* mb(); */
      p->rg.iovhead = (p->rg.iovhead == p->rg.iovmax) ? 0 : p->rg.iovhead+1;

      /* check for user specified timeout (in seconds) PCAP_TIMEOUT > 0 */
      if (p->rg.timeout)
      {
        delta = (STOP_SECS(p)) - p->rg.timeout;
	if (delta >= 0 )
	{
#ifdef RING_STATS
          if (p->rg.statperiod)
          	(void) packet_ring_stats (p, p->rg.statbits);
                consec = 0;
#endif /*RING_STATS*/
	  snprintf(p->errbuf, sizeof(p->errbuf),
			  "User specified timeout occured");
          errno = ETIMEDOUT;
          return (-1);
	}
      }

#ifdef RING_STATS
      {
	//struct timeval tdelta = {0};

	//TV_SUB(p->rg.rs.r_stop, p->rg.rs.r_start, tdelta)
	if (p->rg.statperiod)
        if (STOP_SECS(p) >= p->rg.tea)
	{
		do {
			p->rg.tea += p->rg.statperiod;
			pdelta += p->rg.statperiod;
		} while ( p->rg.tea < STOP_SECS(p) );
#ifdef RING_DEBUG
	fprintf (stderr, "dump: p->rg.tea: %d, START_SECS: %d, STOP_SECS: %d, pdelta: %d, statperiod: %d\n",
				  p->rg.tea, START_SECS(p), STOP_SECS(p), pdelta, p->rg.statperiod);
#endif /*RING_DEBUG*/
		(void) packet_ring_stats (p, p->rg.statbits);
		fflush (stderr);
		consec = 0;
	}
      }
#endif /*RING_STATS*/

      /* check to see if need to return after some delta time */
      if (p->md.timeout > 0)
      {
#ifdef RING_DEBUG
	      fprintf (stderr, "in md.timeout check\n");
#endif /*RING_DEBUG*/
        /* should we use pdelta? */
#ifdef RING_STATS
	if (p->rg.statperiod)
	{
              if (pdelta > 0)
	      {
	          poll_timeout = (p->md.timeout - (pdelta * 1000));
#ifdef RING_DEBUG
	     fprintf (stderr, "stat: md.timeout: %d, pdelta: %d, poll_timeout: %d\n", p->md.timeout, pdelta, poll_timeout);
#endif /*RING_DEBUG*/
	         if ((poll_timeout) <= 0) return n;
	      }
	}
	/* nope, calculate the time gone by */
	else
#endif /*RING_STATS*/
	{	
		delta = ((STOP_SECS(p) - START_SECS(p))*1000000)
			  +(STOP_USECS(p) - START_USECS(p));
		
		if (delta > 0)
		{
		    poll_timeout = p->md.timeout - (delta/1000);
#ifdef RING_DEBUG
		    fprintf (stderr, "goneby: md.timeout: %d, delta: %d, poll_timeout: %d\n", p->md.timeout, delta, poll_timeout);
#endif
		    if ((poll_timeout) <= 0)
			return n;
		}
	}
      }
      /* check if need to return after some number of pkts */
      if (cnt > 0)
      {
        if (n == cnt)
	{
#ifdef RING_DEBUG
	    fprintf (stderr, "count satisfied: n is %d, ps_recv is %d\n",
			    n, p->md.stat.ps_recv);
#endif
#ifdef RING_STATS
	    if (p->rg.statperiod)
	    {
		    (void) packet_ring_stats (p, p->rg.statbits);
		    consec = 0;
	    }
#endif /*RING_STATS*/
	    return n;
	}
      }

    } /* end of packet collection loop, get here when no packets on ring */

    if (p->md.timeout == -1)
    {
	    return n;  /* calling program will poll, non-blocking IO */
    }
    /*  p->md.timeout == 0, wait forever for packets */
    /*  p->md.timeout > 0, will return once we have exhausted the timeout */

    while (!PACKET_AVAILABLE)
    {
      int pres;
      struct pollfd pfd;
      int delta;
#ifdef RING_TIME_STATS
      struct timeval tv1, tv2;
      struct timezone tz;
#endif
#ifdef RING_STATS
      consec = 0;
#endif
      pfd.fd = p->fd;
pollagain:
      /*
       * Has "pcap_breakloop()" been called?
       */
      if (p->break_loop) {
              /*
               * Yes - clear the flag that indicates that it
               * has, and return -2 as an indication that we
               * were told to break out of the loop.
               */
               p->break_loop = 0;
               return -2;
      }
      p->rg.pollAgain++;
      pfd.revents = 0;
      pfd.events = POLLIN;

#ifdef RING_STATS
      p->rg.rs.r_polls++;
#endif
#ifdef RING_TIME_STATS
      if (gettimeofday (&tv1, &tz))
      {
              fprintf (stderr, "ring_stats: gettimeofday1, %s\n",
                              pcap_strerror(errno));
      }
#endif
      if (p->md.timeout == 0)
      { /* wait forever or until a packet arrives */

#ifdef RING_DEBUG
	fprintf (stderr, "poll: p->md.timeout == 0, waiting for a packet.\n");
#endif
        pres = poll(&pfd, 1, -1);
      }
      else
      { /* check if we have polled enough to exhaust the timeout */
	delta = (p->md.timeout - poll_timeout);

#ifdef RING_DEBUG
	fprintf (stderr, "poll: p->md.timeout == %d, delta: %d, poll_timeout: %d.\n",
		p->md.timeout, delta, poll_timeout);
#endif
	if (delta < 1) return (n) ;
	/* else, wait for delta msecs on the clock */
        pres = poll(&pfd, 1, delta);
      }

#ifdef RING_TIME_STATS
      if (gettimeofday (&tv2, &tz))
      {
              fprintf (stderr, "ring_stats: gettimeofday2, %s\n",
                              pcap_strerror(errno));
              return (-1);
      }
      delta = ((tv2.tv_sec - tv1.tv_sec)*1000000) + tv2.tv_usec - tv1.tv_usec;
      p->rg.rs.r_waits.tv_usec += (delta-((delta/1000000)*1000000));
      if (p->rg.rs.r_waits.tv_usec >= 1000000)
      {
          delta = p->rg.rs.r_waits.tv_usec/1000000;
          p->rg.rs.r_waits.tv_sec += delta;
          p->rg.rs.r_waits.tv_usec -= (delta * 1000000);
      }
      STOP_SECS(p) = tv2.tv_sec;
      STOP_USECS(p) = tv2.tv_usec;
#endif

      if ((pres > 0) && (pfd.revents&POLLIN))
      { /* should mean that a packet is ready to be pulled off the ring */

        if (p->rg.pollAgain<LOST_MY_HEAD) continue;

        /*
         * why am I here? Cause just previously poll returned i went to check
         * the ring but there was no data!  Has not happened in a long time.
         */

#ifdef RING_STATS
        if (p->rg.rs.r_polls)
        {
            p->rg.rs.r_specious++;
        }
#endif
        if (packet_head_check(p) >= 0) continue;

        if (p->rg.pollAgain < 120) continue;

        pcap_pstats (p);

        fprintf (stderr,
            "pcap_ring_recv: Kernel not updating ring buffer, shutting down!  \n");
        return(-1);
      }
      else
      { /* something bad happened or we timed out*/
        if (pres == 0)
        { /* ms went to zero, timeout */
          p->rg.pollAgain = 0;
          return n;
        }
        if (pres < 0)
        { /* system call failed */
          if (errno == EINTR)
          {
            p->rg.pollAgain = 0;
            goto pollagain;
          }
          sprintf(p->errbuf, "poll: bad juju, %s", pcap_strerror(errno));
          return n ? : -1;
        }
        { /* we have a packet event, pres > 0 and bad news */
          int err;
          socklen_t elen = sizeof(err);

          if (getsockopt(p->fd, SOL_SOCKET, SO_ERROR, &err, &elen))
          {
            sprintf(p->errbuf, "getsockopt(SO_ERROR): %s", pcap_strerror(errno));
            return n ? : -1;
          }
          if (err)
          {
            struct ifreq ifr;

            if (err != ENODEV || p->md.device == NULL)
            {
              sprintf(p->errbuf, "read error: %s", pcap_strerror(err));
              return n ? : -1;
            }
            fprintf(stderr, "Device %s is down.\n", p->md.device);
            ifr.ifr_ifindex = 0;
            strcpy(ifr.ifr_name, p->md.device);
            if (ioctl(p->fd, SIOCGIFINDEX, &ifr) < 0)
            {
              if (errno != ENODEV)
              {
                sprintf(p->errbuf, "ioctl: %s", pcap_strerror(errno));
                return n ? : -1;
              }
              fprintf(stderr, "Device %s does not exist anymore.\n", p->md.device);
              return n ? : -1;
            }
          }
          else
          {
            fprintf(stderr,
            "Poll of device %s returns POLLERR, but SO_ERROR not set.\n",
              p->md.device);
          }
          return n ? : -1;
        } /* end of packet event                       */
      } /* end of something bad happened             */
    } /* end of while packet not available         */
  } /* back to the top of the routine            */
  /* end of forever, should never get that far */
}
        
/*
 * Use ebuf to set parameters extended parameters for pcap_open_live
 * pcap_open_live checks
 * characteristics of ebuf and acts accordingly.
 * ebuf = pcap_ring_args(device, snaplen, !pflag?-1:-2, msecs, ebuf,
 *                                 rg_R, rg_P, NULL, rg_C);
 */
void
pcap_live_args (const char *device, int snaplen, int promisc, int to_ms, char *ebuf,                  int raw, u_int16_t proto, u_int8_t *maddr, int frame_ct)
{
    live_args *ra = (live_args *)ebuf;

    strncpy (ra->head, "R001", 4);
    ra->device = device;
    ra->snaplen = snaplen;
    ra->promisc = promisc;
    ra->to_ms = to_ms;
    ra->ebuf = ebuf;
    ra->raw = raw;
    ra->proto = proto;
    ra->maddr = maddr;
    ra->frame_ct = frame_ct;
    return;
}

void
packet_ring_close (pcap_t * p)
{
        
    if (p->rg.rs.r_stop.tv_sec) /* are we in a lookupdev loop */
    {
        (void) packet_ring_stats (p, p->rg.statbits); /* no */
    }

    if (p->rg.iovec != NULL)
    {
        free (p->rg.iovec);
    }
    p->rg.iovec = NULL;
    if (p->rg.buf != NULL)
    {
        munmap (p->rg.buf, p->rg.bufsize);
        p->rg.buf = NULL;
    }
}

int
packet_ring_stats (pcap_t * p, int opt)
{
    struct timeval ftv = {0};
    struct timeval stv = {0};
#ifdef RING_TIME_STATS
    struct timeval tv = {0};
#endif
#ifdef HAVE_TPACKET_STATS
    struct tpacket_stats ks = {0};
    socklen_t l;

    l = sizeof (struct tpacket_stats);

    if (getsockopt(p->fd, SOL_PACKET, PACKET_STATISTICS, &ks, &l) > -1)
    {
        p->md.kstats_total.tp_packets += ks.tp_packets;
        p->md.kstats_total.tp_drops += ks.tp_drops;
    }
#endif

    (void) packet_ring_diff (&ftv, &p->rg.rs.r_start, &p->rg.rs.r_stop);
    stv = p->rg.rs.r_stop;
#ifdef RING_STATS
#ifdef RING_TIME_STATS
    (void) packet_ring_diff (&tv, &p->rg.rs.r_waits, &ftv);
#endif

    if (p->rg.iovec != NULL && opt)
    {
        unsigned long int devstats[IFACE_MAXFIELDS];
        unsigned long int ifpkts;
        unsigned long int ifbytes;
        unsigned long int rxerrs;
        unsigned long int rxdrops;
        unsigned long int multi;
        unsigned long int txerrs;
        unsigned long int txdrops;
        if (packet_dev_stats(p, &devstats[0]))
        {
                unsigned long int packets = devstats[rx_packets]
                                + devstats[tx_packets]; 
                unsigned long int bytes = devstats[rx_bytes]
                                    + devstats[tx_bytes];
            
    	    ifpkts = 1L - p->rg.ifpkts + packets - 1;
                p->rg.ifpkts = packets;
    	    ifbytes = 1L - p->rg.ifbytes + bytes - 1;
                p->rg.ifbytes = bytes;
    	    multi = 1L - p->rg.multi + devstats[rx_multicast] - 1;
    	    p->rg.multi = devstats[rx_multicast];
	    rxerrs = 1L - p->rg.rxerrs + devstats[rx_errors] - 1;
	    p->rg.rxerrs = devstats[rx_errors];
	    rxdrops = 1L - p->rg.rxdrops + devstats[rx_dropped] - 1;
	    p->rg.rxdrops = devstats[rx_dropped];
	    txerrs = 1L - p->rg.txerrs + devstats[tx_errors] - 1;
	    p->rg.txerrs = devstats[tx_errors];
	    txdrops = 1L - p->rg.txdrops + devstats[tx_dropped] - 1;
	    p->rg.txdrops = devstats[tx_dropped];
	}
	else {
            ifpkts = 0;
            ifbytes = 0;
            rxerrs = 0;
            rxdrops = 0;
            multi = 0;
            txerrs = 0;
            txdrops = 0;
        }
        if (opt < 0)
        {
            fprintf (stderr, "                          Start time: %s",
                        ctime((time_t *) &START_SECS(p)));
            fprintf (stderr, "             Total packets processed: %ld\n",
                        p->rg.rs.r_recv);
#ifdef HAVE_TPACKET_STATS
            fprintf (stderr, "               Total number of drops: %d\n",
                        ks.tp_drops);
            fprintf (stderr, "             Total number of packets: %d\n",
                        ks.tp_packets);
#endif
	    if (p->rg.have_ds) {
			fprintf (stderr, "             Total Interface packets: %ld\n",
                        ifpkts);
			fprintf (stderr, "               Total Interface bytes: %ld\n",
                        ifbytes);
	    }
            fprintf (stderr, "               Total packets ignored: %ld\n",
                        p->rg.rs.r_ignore);
            fprintf (stderr, "                Total bytes received: %ld\n",
                        p->rg.rs.r_bytes);
            fprintf (stderr, "                Number of poll waits: %ld\n",
                        p->rg.rs.r_polls);
            fprintf (stderr, "                          Ring index: %d\n",
                        p->rg.iovhead);
            fprintf (stderr, "     Max consecutive packets on ring: %ld\n",
                        p->rg.rs.r_consec);
            fprintf (stderr, "          Total specious ring checks: %ld\n",
                        p->rg.rs.r_specious);
            fprintf (stderr, "       Total seconds start to finish: %09lu.%06lu\n",
                           ftv.tv_sec, ftv.tv_usec);
            fprintf (stderr, "                            End time: %s\n",
                        ctime((time_t *) &STOP_SECS(p)));
#ifdef RING_TIME_STATS
            fprintf (stderr, "  Total seconds waiting for a packet: %09lu.%06lu\n",
                           p->rg.rs.r_waits.tv_sec,
                           p->rg.rs.r_waits.tv_usec);
            fprintf (stderr, "    Total seconds processing packets: %09lu.%06lu\n",
                           tv.tv_sec, tv.tv_usec);
#endif
        }
        else
        {
            char *sp;
            char statbuf[192];
            char *statbufptr = &statbuf[0];
            int  nchars;
        
            sp = "S:";
            if (opt&RSTAT_TV_STARTDATE)
            {
                nchars = sprintf (statbufptr, "%s%lu.%06lu", sp,
                            START_SECS(p),
                            START_USECS(p));
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_PKTS_PROCESSED)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp, p->rg.rs.r_recv);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_PKTS_DROPPED)
            {
                nchars = sprintf (statbufptr, "%s%u", sp, ks.tp_drops);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_PKTS_TOTAL)
            {
                nchars = sprintf (statbufptr, "%s%u", sp, ks.tp_packets);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_PKTS_IGNORED)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp, p->rg.rs.r_ignore);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_DEV_PACKETS)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp,
				(p->rg.have_ds) ? ifpkts : 0);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_DEV_BYTES)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp,
				(p->rg.have_ds) ? ifbytes : 0);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_BYTES_RECEIVED)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp, p->rg.rs.r_bytes);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_POLL_CALLS)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp, p->rg.rs.r_polls);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_RING_INDEX)
            {
                nchars = sprintf (statbufptr, "%s%u", sp, p->rg.iovhead);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_RING_CONSEC)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp, p->rg.rs.r_consec);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_SPECIOUS)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp, p->rg.rs.r_specious);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_TV_ELAPSED)
            {
                nchars = sprintf (statbufptr, "%s%09lu.%06lu", sp,
                                ftv.tv_sec, ftv.tv_usec);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_RX_ERRORS)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp, 
				(p->rg.have_ds) ? rxerrs : 0);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_RX_DROPS)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp,
				(p->rg.have_ds) ? rxdrops : 0);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_TX_ERRORS)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp,
				(p->rg.have_ds) ? txerrs : 0);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_TX_DROPS)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp,
				(p->rg.have_ds) ? txdrops : 0);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_MULTICAST)
            {
                nchars = sprintf (statbufptr, "%s%lu", sp,
				(p->rg.have_ds) ? multi : 0);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_TV_ENDDATE)
            {
                nchars = sprintf (statbufptr, "%s%09lu.%06lu", sp,
                                STOP_SECS(p),
                                STOP_USECS(p));
                sp = " ";
                statbufptr += nchars;
            }
#ifdef RING_TIME_STATS
            if (opt&RSTAT_TV_WAIT)
            {
                nchars = sprintf (statbufptr, "%s%09lu.%06lu", sp,
                                p->rg.rs.r_waits.tv_sec,
                                p->rg.rs.r_waits.tv_usec);
                sp = " ";
                statbufptr += nchars;
            }
            if (opt&RSTAT_TV_PROCESSING)
            {
                nchars = sprintf (statbufptr, "%s%09lu.%06lu", sp,
                                tv.tv_sec, tv.tv_usec);
                sp = " ";
                statbufptr += nchars;
            }
#endif
                fprintf (stderr, "%s\n", statbuf);
        }
        memset ((void *)&p->rg.rs, 0, sizeof (struct ring_stat));
        p->rg.rs.r_start = stv;
	return (1);
    }
#endif /* RING_STATS */
    p->rg.rs.r_start = stv;
    return (0);
}

struct timeval *
packet_ring_diff (struct timeval *d, struct timeval *s, struct timeval *e)
{
    long int delta;

    d->tv_usec = ((e->tv_sec - s->tv_sec)*1000000) + e->tv_usec - s->tv_usec;
    if (d->tv_usec >= 1000000)
    {
        delta = d->tv_usec/1000000;
        d->tv_sec += delta;
        d->tv_usec -= (delta * 1000000);
    }
    return d;
}

int
pcap_status ( pcap_t * p )
{
    if ( p && p->fd)
    {
        if (p->sf.rfile)
            return PCAP_FILE;
        if (p->rg.iovec)
            return PCAP_RING;
        else
            return PCAP_NET;
    }
    else
    {
        return PCAP_CLOSED;
    }
}

int
packet_head_check (pcap_t * p)
{
    struct iovec *ring = p->rg.iovec;
    int    i, head;

    for (head = p->rg.iovhead, i = 0; i < p->rg.ct; i++ )
    {

        if (*(unsigned long *)ring[head].iov_base)
        {
          p->rg.iovhead = head;
/*          fprintf (stderr, "Set rg.iovhead to %d!\n", head); */
          return head;
        }
        head = (head == p->rg.iovmax) ? 0 : head + 1;
    }
    return -1;
}

/*
 * packet_discard (pcap_t *)
 * marks any entries in the ring buffer as read
 * presumably called after setting a new filter.
 */
int
packet_discard (pcap_t * p)
{
    struct iovec *ring = p->rg.iovec;
    struct tpacket_stats tps = {0};
    socklen_t olen = sizeof (tps);
    int    i, head;
    int    discarded = 0;

    if (!p->rg.iovec) return 0;
    for (head = p->rg.iovhead, i = 0; i < p->rg.ct; i++ )
    {

        if (*(unsigned long *)ring[head].iov_base)
        {
		*(unsigned long *)ring[head].iov_base = 0;
		discarded++;
        }
        head = (head == p->rg.iovmax) ? 0 : head + 1;
    }
    p->rg.iovhead = head;
    /* clear packet stats */
    (void) getsockopt(p->fd, SOL_PACKET, PACKET_STATISTICS, (void*)&tps, &olen);
#ifdef RING_DEBUG
    if (p->md.verbose)
      fprintf (stderr, "discarded: %d, tp_packets: %d, tp_drops: %d\n", discarded, tps.tp_packets, tps.tp_drops);
#endif
    p->md.kstats_total.tp_packets = 0;
    p->md.kstats_total.tp_drops = 0;
    return discarded;
}

void
pcap_pstats (pcap_t *p)
{
#ifdef PACKET_STATISTICS
  struct tpacket_stats tps = {0};
  socklen_t olen = sizeof(tps);
  struct timeval tv;
  struct timezone tz;

  (void) gettimeofday (&tv, &tz);
  if (getsockopt(p->fd, SOL_PACKET, PACKET_STATISTICS, (void*)&tps, &olen) == 0)
  {
     fprintf (stderr, "E:%lu.%lu: packets = %u, dropped = %u\n",
                     tv.tv_sec, tv.tv_usec, tps.tp_packets, tps.tp_drops);
  }
#else
  fprintf (stderr, "E:No kernel packet statistics!\n");
#endif
}

/*
 * int packet_dev_stats (pcap_t *p, unsigned long int *uli)
 *   opens /proc/net/dev and looks for the interface equal to p->md.device.
 *   If found the statistics defined in pcap-ring.h are returned in the
 *   unsigned long int array uli.
 *
 * NOTE: this has XXX written all over it.  See linux net-tools package
 *         lib/interface.c
 *         lib/proc.c
 *       for a more robust solution.
 *
 * KNOWN TO WORK ON: 2.2.x and 2.4.x
 */

int
packet_dev_stats (pcap_t *p, unsigned long int *uli)
{
    static char buf[BUFSIZ];
    char *bufptr;
    char *device;
    char *s;
    FILE *dev;
    int fields = 0;
    int i;
    unsigned long int *saveuli;
    unsigned long int field;
    unsigned long int tfield;
    char *endptr[2];
    int notany = strncmp("any", p->md.device, strlen(p->md.device));

    saveuli = uli;
    for (i=0;i<IFACE_MAXFIELDS;i++) *uli++ = 0;
    uli = saveuli;
    
    dev = fopen ("/proc/net/dev", "r");
    if (!dev) return (0);

    (void) fgets (buf, BUFSIZ, dev);
    (void) fgets (buf, BUFSIZ, dev);

    while ((s = fgets (buf, BUFSIZ, dev)))
    {
        fields = 0;
        bufptr = &buf[0];
        device = bufptr;
        bufptr = strchr (buf, ':');
        while (*device == ' ') device ++;

        if (strncmp(device, p->md.device, strlen(p->md.device)) && notany)
		continue;
        *bufptr = ' ';
        while (fields++ < IFACE_MAXFIELDS)
        {
	    field = *uli;
	    tfield = strtoul(bufptr, endptr, 10);
            *uli++ = field + tfield;
            bufptr = *endptr;
        }
	if (notany)
	{
	  fclose (dev);
          return (fields);
	}
        uli = saveuli;
    }
    fclose (dev);
    return (fields);
}

/*
 * convert various frame types to an index for modified tcpdump code
 * and other applications that might want to bind to a specific protocol
 */
int
pcap_convert_proto( char *id )
{
    if (!strcmp(id, "help"))
    {
        fprintf (stderr, "ip, ipv4, ipv6, arp, rarp, 802.2, 802.3, lat,\ndec, atalk, aarp, ipx, x25, or an integer\nSee: /usr/include/linux/if_ether.h\n");
        return 0;
    }
    if (!strcmp(id, "ip") || !strcmp(id, "ipv4"))
        return htons(ETH_P_IP);
    if (!strcmp(id, "ipv6"))
        return htons(ETH_P_IPV6);
    if (!strcmp(id, "arp"))
        return htons(ETH_P_ARP);
    if (!strcmp(id, "rarp"))
        return htons(ETH_P_RARP);
    if (!strcmp(id, "802.2"))
        return htons(ETH_P_802_2);
    if (!strcmp(id, "802.3"))
        return htons(ETH_P_802_3);
    if (!strcmp(id, "lat"))
        return htons(ETH_P_LAT);
    if (!strcmp(id, "dec"))
        return htons(ETH_P_DEC);
    if (!strcmp(id, "atalk"))
        return htons(ETH_P_ATALK);
    if (!strcmp(id, "aarp"))
        return htons(ETH_P_AARP);
    if (!strcmp(id, "ipx"))
        return htons(ETH_P_IPX);
    if (!strcmp(id, "x25"))
        return htons(ETH_P_X25);
    return htons(atoi(id));
}

#endif /* DO_RING */
