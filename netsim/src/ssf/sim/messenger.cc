/*
 * messenger.cc :- data structure for remote message passing.
 */

#include "ssf.h"
#include "sim/composite.h"

#if PRIME_SSF_DISTSIM

namespace prime {
namespace ssf {

ssf_messenger::ssf_messenger(ltime_t t, prime::ssf::int32 lp, 
			     prime::ssf::int32 tlp, prime::ssf::int32 ent, 
			     prime::ssf::int32 port, prime::ssf::int32 sno, 
			     Event* evt) :
  time(t), src_lp(lp), tgt_lp(tlp), src_ent(ent), src_port(port), serial_no(sno)
{
  if(evt) {
    usrevt_ident = evt->_evt_evtcls_ident();
    usrevt_packed = evt->pack();
  } else {
    usrevt_ident = -1;
    usrevt_packed = 0;
  }
}

ssf_messenger::ssf_messenger() { /* do nothing, expect to receive */ }

ssf_messenger::~ssf_messenger() 
{
  if(usrevt_packed) delete usrevt_packed;
}

#ifdef PRIME_SSF_SYNC_MPI
int ssf_messenger::pack(MPI_Comm comm, char* buffer, int& pos, int bufsiz)
{
  /*printf("messenger::pack: srclp=%d, tgtlp=%d, srcent=%d, srcport=%d, sno=%d\n",
    src_lp, tgt_lp, src_ent, src_port, serial_no);*/

  //int pos = 0;
  if(MPI_Pack(&time, 1, SSF_LTIME_MPI_TYPE, buffer, bufsiz, &pos, comm))
    return 1;

  if(MPI_Pack(&src_lp, 1, MPI_INT, buffer, bufsiz, &pos, comm) ||
     MPI_Pack(&tgt_lp, 1, MPI_INT, buffer, bufsiz, &pos, comm) ||
     MPI_Pack(&src_ent, 1, MPI_INT, buffer, bufsiz, &pos, comm) ||
     MPI_Pack(&src_port, 1, MPI_INT, buffer, bufsiz, &pos, comm) ||
     MPI_Pack(&serial_no, 1, MPI_INT, buffer, bufsiz, &pos, comm) ||
     MPI_Pack(&usrevt_ident, 1, MPI_INT, buffer, bufsiz, &pos, comm))
    return 1;

  /*
  int len = 0;
  if(usrevt_packed) {
    char buf[1024]; // a maximum size for user data
    len = usrevt_packed->send(comm, buf, 1024);
    if(len <= 0) return 0;
    if(MPI_Pack(&len, 1, MPI_INT, buffer, bufsiz, &pos, comm) ||
       MPI_Pack(buf, len, MPI_PACKED, buffer, bufsiz, &pos, comm))
      return 0;
  } else {
    if(MPI_Pack(&len, 1, MPI_INT, buffer, bufsiz, &pos, comm)) return 0;
  }
  */
  prime::ssf::int32 thereispd = usrevt_packed ? 1 : 0;
  if(MPI_Pack(&thereispd, 1, MPI_INT, buffer, bufsiz, &pos, comm)) return 1;
  if(usrevt_packed && usrevt_packed->pack(comm, buffer, pos, bufsiz)) return 1;

  return 0;
}

int ssf_messenger::unpack(MPI_Comm comm, char* buffer, int& pos, int bufsiz)
{
  //int pos = 0;
  if(MPI_Unpack(buffer, bufsiz, &pos, &time, 1, SSF_LTIME_MPI_TYPE, comm)) 
    return 1;

  prime::ssf::int32 thereispd;
  if(MPI_Unpack(buffer, bufsiz, &pos, &src_lp, 1, MPI_INT, comm) ||
     MPI_Unpack(buffer, bufsiz, &pos, &tgt_lp, 1, MPI_INT, comm) ||
     MPI_Unpack(buffer, bufsiz, &pos, &src_ent, 1, MPI_INT, comm) ||
     MPI_Unpack(buffer, bufsiz, &pos, &src_port, 1, MPI_INT, comm) ||
     MPI_Unpack(buffer, bufsiz, &pos, &serial_no, 1, MPI_INT, comm) ||
     MPI_Unpack(buffer, bufsiz, &pos, &usrevt_ident, 1, MPI_INT, comm) ||
     MPI_Unpack(buffer, bufsiz, &pos, &thereispd, 1, MPI_INT, comm))
    return 1;

  /*
  if(thereispd) {
    char buf[1024]; // a maximum size for user data
    if(MPI_Unpack(buffer, bufsiz, &pos, buf, thereispd, MPI_PACKED, comm))
      return 1;
    usrevt_packed = new ssf_compact;
    if(usrevt_packed->recv(comm, buf, 1024) <= 0) return 1;
  } else usrevt_packed = 0;
  */
  if(thereispd) {
    usrevt_packed = new ssf_compact;
    if(usrevt_packed->unpack(comm, buffer, pos, bufsiz)) return 1;
  } else usrevt_packed = 0;

  /*printf("messenger::unpack: srclp=%d, tgtlp=%d, srcent=%d, srcport=%d, sno=%d\n",
    src_lp, tgt_lp, src_ent, src_port, serial_no);*/

  return 0;
}
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
int ssf_messenger::pack(char* buffer, int& pos, int bufsiz)
{
  if(pos+(int)sizeof(ltime_t) > bufsiz) return 1;
  ssf_ltime_serialize(time, &buffer[pos]); pos += sizeof(ltime_t);

  if(pos+(int)sizeof(int)*6 > bufsiz) return 1;
  ssf_compact::serialize(src_lp, &buffer[pos]); pos += sizeof(int);
  ssf_compact::serialize(tgt_lp, &buffer[pos]); pos += sizeof(int);
  ssf_compact::serialize(src_ent, &buffer[pos]); pos += sizeof(int);
  ssf_compact::serialize(src_port, &buffer[pos]); pos += sizeof(int);
  ssf_compact::serialize(serial_no, &buffer[pos]); pos += sizeof(int);
  ssf_compact::serialize(usrevt_ident, &buffer[pos]); pos += sizeof(int);

  prime::ssf::int32 thereispd = usrevt_packed ? 1 : 0;
  if(pos+(int)sizeof(int) > bufsiz) return 1;
  ssf_compact::serialize(thereispd, &buffer[pos]); pos += sizeof(int);
  if(usrevt_packed && usrevt_packed->pack(buffer, pos, bufsiz)) return 1;

  return 0;
}

int ssf_messenger::unpack(char* buffer, int& pos, int bufsiz)
{
  if(pos+(int)sizeof(ltime_t) > bufsiz) return 1;
  ssf_ltime_deserialize(time, &buffer[pos]); pos += sizeof(ltime_t);

  prime::ssf::int32 thereispd;
  if(pos+(int)sizeof(int)*7 > bufsiz) return 1;
  ssf_compact::deserialize(src_lp, &buffer[pos]); pos += sizeof(int);
  ssf_compact::deserialize(tgt_lp, &buffer[pos]); pos += sizeof(int);
  ssf_compact::deserialize(src_ent, &buffer[pos]); pos += sizeof(int);
  ssf_compact::deserialize(src_port, &buffer[pos]); pos += sizeof(int);
  ssf_compact::deserialize(serial_no, &buffer[pos]); pos += sizeof(int);
  ssf_compact::deserialize(usrevt_ident, &buffer[pos]); pos += sizeof(int);
  ssf_compact::deserialize(thereispd, &buffer[pos]); pos += sizeof(int);

  if(thereispd) {
    usrevt_packed = new ssf_compact;
    if(usrevt_packed->unpack(buffer, pos, bufsiz)) return 1;
  } else usrevt_packed = 0;

  return 0;
}
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_DISTSIM*/

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
