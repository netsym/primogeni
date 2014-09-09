To build this thing from absolute scratch:

  1. rm -rf config
  2. sh bootstrap
     y
  3. Peruse .warrantee
  4. ./configure ...
  5. make
  
To further understand:

Please read README.linux first.  This README describes an additional
mechanism, using shared memory ring buffers first implimented in the linux
kernel by Alexey Kuznetsov as a patch to the 2.2.x kernel and a kernel
configuration option (CONFIG_PACKET_MMAP) in the 2.4.x kernel.  The
original source for the modifications to the LBL libpcap package was made
available by Alexey sometime around March of 1999.

Note: This version of linux pcap supports the nonblock features and the "any"
      interface(s)

Note: Versions of libpcap based on tcpdump.org 0.8 have been changed in regard
      to the "to_ms" argument to pcap_open_live.  Here is how it is used:

      1. With a positive timeout (initialized by the to_ms value on each
         call to pcap_read), a "read" will return if either
         a. enough polls have been called to exhaust the timeout value,
            or
         b. the timeout expires even if no packets have been received. 
      2. With a zero timeout, a "pcap_read" will never return. The timeout is
         considered infinite. Of course callbacks will continue for each packet
         that arrives.  And the PCAP_TIMEOUT environment variable can be used
         to signal an error of ETIMEDOUT.
      3. With a value of -1, "pcap_read" will return if either
         a. there are no packets on the ring,
            or
         b. the packets that have been queued on the ring have all been
            processed. In otherwords, it is non-blocking. The recommended
            'non blocking' mechanism is to use 'pcap_setnonblock' and
            'pcap_getnonblock' to set/unset or retrieve the blocking/nonblocking
             state. In otherwords, calling pcap_setnonblock is equivalent to
             calling pcap_open_live with to_ms == -1. 

The current linux libpcap implimentation relies heavily on Alexey's code.
However, any problems should be reported to cornett@arpa.net who is responsible
for this linux library implimentation.  Every effort was made to make the
changes available to libpcap dependent applications without their having to
be recompiled.  If the application (tcpdump for example) was built with
shared libraries in mind, then there should be no problem installing the
mmap libpcap in the usual place (/usr/lib), and availing yourself of the
ring buffer option.  How could this be?

There are a number of ways a libpcap based application can be coerced into using
the ring buffer:

1. If shared libraries are being used by the application, then you can set
   an environment variable to clue pcap_open_live to go the ring route:

      # PCAP_MEMORY=2000 tcpdump -i eth1 ...

   Note: setting PCAP_MEMORY=max may not work unless you have a lot
      of memory.  Take a look at your free memory (top comes to mind).
      You could specify PCAP_FRAMES, but specifing memory is best way
      to maximize your number of ring entries.  In production, you
      set this in a startup script and it is yours for the duration.
      If you pcap program dies, not all the memory is necessarily 
      recovered by the system as free space.  As a consequence, using
      this interactively, can limit your resources.

   Other environment variables include:

     PCAP_SNAPLEN think tcpdump -s
     PCAP_PROMISC -1 = promiscous -2 = not promiscuous
     PCAP_TO_MS   variable meanings, think milliseconds(ms) to wait for a
                  packet, but read the note regarding to_ms above.
     PCAP_RAW     2 = cooked mode
     PCAP_PROTO   ip,ipv6,arp,rarp,802.2,802.3,lat,dec,atalk,aarp,ipx,x25
     PCAP_MADDR   requires hex string which will override PCAP_PROMISC 
     PCAP_FRAMES  greater than 0 up to n.  (n is found experimentally.)
     PCAP_MEMORY  set to how much free memory (in Kbytes) you are willing
                  to allocate.  (preferred method.)
     PCAP_VERBOSE print informative messages, since old app doesn't see them.
     PCAP_STATS   print pcap statistics to stderr every PCAP_PERIOD ms.
                  Stats will also be printed whenever pcap_read, pcap_dispatch,
                  and pcap_loop return to the calling program.
     PCAP_TIMEOUT return errno "ETIMEDOUT" when packet time is greater than
                  value provided (eg PCAP_TIMEOUT=1044406300 will cause
                  tcpdump to quit on Mon Feb 4 17:51:40 MST 2003).
     PCAP_PERIOD  milliseconds between stats (will not cause pcap_dispatch
                  to return, will generate stats).  

   Note: The format for PCAP_STATS is a 32 bit mask.  See pcap-ring.h for
      field definitions.  If a bit is set in this mask, then the corresonding
      value will be printed.  The values are separated by a space.

      bit 0 is defined to be 0x00000001 so If you wanted to print the
      "time" followed by number of packets dropped by the kernel and
      seen by the device, you would set PCAP_STATS=0x24

          0  Start date and time
          1  Packets processed
          2  Packets dropped
          3  Packets total
          4  Packets ignored
          5  Packets seen by device (in and out)
          6  Bytes seen by device (in and out)
          7  Bytes received 
          8  Number of times poll system call called
          9  Current ring buffer index
         10  Maximum number of frames pulled from ring before having to poll
         11  Specious signal to pull frames from ring
         12  Elapsed time between first and last packet seen during sample time
	 13  Received errors
         14  Received drops
         15  Transfer errors
         16  Transfer drops
         17  Multicast packet count (the Packets total includes this number)

      By setting PCAP_STATS=0x021fff, the ring statistics line would print 
      items  0 through 12 and item 17 in that order.  I've been known to
      use PCAP_STATS=0x1fff.

2. If static libraries are being used the the application will need to be
   re-loaded.

3. Or, one can write non-portable libpcap based applications by calling a
   linux specific pcap routine called pcap_ring_args which initializes
   ebuf prior to calling pcap_open_live as a way of passing in extra ring
   related arguments.  Not recommended.

4. Problems with make procedure:
   (See .warrantee, if you have not already)
   If you already have a libpcap from your favorite "vendor", these
   last steps may not provide you with what you want since "root" is required
   and various libpcap based applications on your system are expecting some
   specific pcap version.  I found that judicious symlink's in /usr/lib

   such as:

    ln -s libpcap.so.0.9.x libpcap.so.0.7.2
                         ^ is related to libpcap source used as a base.

   Also, if you put the shared library in /usr/lib, a ldconfig seems to help.

   One more item regarding compilation on 64bit linux:

   You may see something like the following output when running 'bootstrap':

     /usr/share/aclocal/g-wrap.m4:7: warning: underquoted definition of
       AC_GWRAP_CHECK_GUILE
     run info '(automake)Extending aclocal'
     or see http://sources.redhat.com/automake/automake.html#Extending-aclocal
     /usr/share/aclocal/g-wrap.m4:23: warning: underquoted definition of
       AM_PATH_GWRAP
     /usr/share/aclocal/aqhbci.m4:5: warning: underquoted definition of
       AC_AQHBCI
     /usr/share/aclocal/ao.m4:9: warning: underquoted definition of
       XIPH_PATH_AO
  
   Bringing the aclocal m4 macros up to date might help, or, try
   placing '[' and ']' around the first argument to AC_DEFUN in the respective
   m4 files, or ignore the problem.  It's your call.

