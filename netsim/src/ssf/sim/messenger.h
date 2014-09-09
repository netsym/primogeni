/*
 * messenger.h :- message data structure for remote message passing.
 */

#ifndef __PRIME_SSF_MESSENGER_H__
#define __PRIME_SSF_MESSENGER_H__

#ifndef __PRIME_SSF_COMPOSITE_H__
#error "messenger.h must be included by composite.h"
#endif

#if PRIME_SSF_DISTSIM

namespace prime {
namespace ssf {

  class ssf_messenger {
  public:
    ssf_messenger(ltime_t t, prime::ssf::int32 src_lp, 
		  prime::ssf::int32 tgt_lp, prime::ssf::int32 src_ent, 
		  prime::ssf::int32 src_port, prime::ssf::int32 serial_no, 
		  Event* evt);
    ssf_messenger();
    virtual ~ssf_messenger();

    ltime_t time;
    prime::ssf::int32 src_lp;
    prime::ssf::int32 tgt_lp;
    prime::ssf::int32 src_ent;
    prime::ssf::int32 src_port;
    prime::ssf::int32 serial_no;
    prime::ssf::int32 usrevt_ident;
    ssf_compact* usrevt_packed;
    ssf_messenger* next;

#ifdef PRIME_SSF_SYNC_MPI
    int pack(MPI_Comm, char*, int&, int);
    int unpack(MPI_Comm, char*, int&, int);
#endif

#ifdef PRIME_SSF_SYNC_LIBSYNK
    int pack(char*, int&, int);
    int unpack(char*, int&, int);
#endif
  }; /*ssf_messenger*/

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_DISTSIM*/

#endif /*__PRIME_SSF_MESSENGER_H__*/

/*
 * 
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
