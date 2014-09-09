/**
 * \file tcp_init.h
 * \brief Default values for TCP.
 * \author Miguel Erazo
 * 
 * 
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

#ifndef __TCP_INIT_H__
#define __TCP_INIT_H__

/*
 * Default initialization constants
 */

#define TCP_DEFAULT_VERSION			"reno"
#define TCP_DEFAULT_ISS				0
#define TCP_DEFAULT_MSS				1024 // RFC uses 536
#define TCP_DEFAULT_SNDWNDSIZ		1280000 // in bytes
#define TCP_DEFAULT_SNDBUFSIZ		1280000 // in bytes
#define TCP_DEFAULT_SLOW_TIMEOUT	0.5
#define TCP_DEFAULT_FAST_TIMEOUT	0.2
#define TCP_DEFAULT_MSL				60.0

#define TCP_SACK_NO             0x0000
#define TCP_SACK_PERMITTED      0x0001
#define TCP_SACK_OPTION         0x0002

#define TCP_SACK_PERMITTED_KIND 4
#define TCP_SACK_OPTION_KIND    5

#define TCP_SACK_MAX_BLOCKS     4 // 8*N+2<=40 => N<=4.

#define TCP_DEFAULT_MSL_TIMEOUT_FACTOR 2  // as per RFC 793
#define TCP_DEFAULT_BACKOFF_LIMIT TCP_DEFAULT_MAXRXMIT*/

#define DEFAULT_IP_TIMETOLIVE 64
#ifndef IPADDR
typedef prime::ssf::uint32 IPADDR;
#endif

enum {
  IPADDR_INADDR_ANY = 0xFFFFFFFF
};

#endif /*__TCP_INIT_H__*/
