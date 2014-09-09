/*
 * ssftime :- simulation time can defined as different types.
 *
 * Currently supported simulation time types include long, long long,
 * float, and double. The type must be chosen at the configuration
 * time before one compiles SSF.
 */

#include "api/ssftime.h"
#include "api/ssfcompact.h"
#include "api/ssfexception.h"

namespace prime {
namespace ssf {

char* ssf_ltime_serialize(ltime_t tm, char* buf)
{
  if(!buf) buf = new char[sizeof(ltime_t)];
  // FIXME: how to make this check static rather than dynamic?
  if(sizeof(ltime_t) == sizeof(prime::ssf::uint64))
    *(prime::ssf::uint64*)buf = SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&tm);
  else if(sizeof(ltime_t) == sizeof(prime::ssf::uint32))
    *(prime::ssf::uint32*)buf = SSF_BYTE_ORDER_32(*(prime::ssf::uint32*)&tm);
  else if(sizeof(ltime_t) == sizeof(prime::ssf::uint16))
    *(prime::ssf::uint16*)buf = SSF_BYTE_ORDER_16(*(prime::ssf::uint16*)&tm);
  else prime::ssf::ssf_throw_exception(ssf_exception::ltime_size, 
				   "ssf_ltime_serialize()");
  return buf;
}

void ssf_ltime_deserialize(ltime_t& tm, char* buf)
{
  if(!buf) prime::ssf::ssf_throw_exception(ssf_exception::ltime_nullbuf);
  // FIXME: how to make this check static rather than dynamic?
  if(sizeof(ltime_t) == sizeof(prime::ssf::uint64))
    *(prime::ssf::uint64*)&tm = SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)buf);
  else if(sizeof(ltime_t) == sizeof(prime::ssf::uint32))
    *(prime::ssf::uint32*)&tm = SSF_BYTE_ORDER_32(*(prime::ssf::uint32*)buf);
  else if(sizeof(ltime_t) == sizeof(prime::ssf::uint16))
    *(prime::ssf::uint16*)&tm = SSF_BYTE_ORDER_16(*(prime::ssf::uint16*)buf);
  else prime::ssf::ssf_throw_exception(ssf_exception::ltime_size, 
				   "ssf_ltime_deserialize()");
}

}; // namespace ssf
}; // namespace prime

/*
#include <stdio.h>
using namespace prime::ssf;

void printout(ltime_t x) {
  union { ltime_t xx; unsigned char yy[sizeof(ltime_t)]; } val;
  val.xx = x;
  printf("0x");
  for(int i=0; i<sizeof(ltime_t); i++)
    printf("%02x", val.yy[i]);
}

int main(int argc, char* argv[]) {
  ltime_t x = 0x12345678;
  ltime_t y, z;
  SSF_LTIME_SERIALIZE(x, (char*)&y);
  SSF_LTIME_DESERIALIZE(z, (char*)&y);
  printf("x="); printout(x); printf("\n");
  printf("y="); printout(y); printf("\n");
  printf("z="); printout(z); printf("\n");
  printf("inf=%g, -inf=%g\n", (double)SSF_LTIME_INFINITY,
	 (double)SSF_LTIME_MINUS_INFINITY);
  printf("inf="); printout(SSF_LTIME_INFINITY); printf("\n");
  printf("-inf="); printout(SSF_LTIME_MINUS_INFINITY); printf("\n");
  return 0;
}
*/

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
