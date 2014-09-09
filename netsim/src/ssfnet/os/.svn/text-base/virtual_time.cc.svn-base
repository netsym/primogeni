/**
 * \file virtual_time.cc
 * \brief Source file for the VirtualTime class.
 * \author Nathanael Van Vorst
 * \author Jason Liu
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

#include <stdio.h>
#include <stdlib.h>
#include "os/logger.h"
#include "os/virtual_time.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT_REF(ssfnet);

prime::ssf::ltime_t VirtualTime::SECOND        = 1000000000LL;
prime::ssf::ltime_t VirtualTime::MILLISECOND   = 1000000LL;
prime::ssf::ltime_t VirtualTime::MICROSECOND   = 1000LL;
prime::ssf::ltime_t VirtualTime::NANOSECOND    = 1LL;

void VirtualTime::setFrequency(prime::ssf::ltime_t freq)
{
  if (freq < 1000000000LL) 
    LOG_ERROR("virtual time resolution too low: frequency=" << freq << ".");

  SECOND = freq;
  MILLISECOND = freq/1000LL;
  MICROSECOND = freq/1000000LL;
  NANOSECOND  = freq/1000000000LL;
}


SSFNET_STRING VirtualTime::toString() const
{
  char buf[50];
  sprintf(buf, "%.9lfs", second());
  return buf;
}

void VirtualTime::fromString(const SSFNET_STRING& str)
{
  prime::ssf::ltime_t unit = SECOND;
  size_t found = str.find_first_not_of("0123456789.");
  if (found != string::npos) {
    SSFNET_STRING u = str.substr(found);
    if(!u.compare("ns")) unit = NANOSECOND;
    else if(!u.compare("us")) unit = MICROSECOND;
    else if(!u.compare("ms")) unit = MILLISECOND;
    else if(!u.compare("s")) unit = SECOND;
    else LOG_ERROR("invalid time format: " << str << ".");
  }
  double f = atof(str.c_str());
  vtime = (prime::ssf::ltime_t)(f*unit);
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const VirtualTime& vt)
{
  os << vt.toString();
  return os;
}

template<> bool rw_config_var<SSFNET_STRING, VirtualTime> 
  (SSFNET_STRING& dst, VirtualTime& src)
{
  dst = src.toString();
  return false;
}

template<> bool rw_config_var<VirtualTime, SSFNET_STRING>
  (VirtualTime& dst, SSFNET_STRING& src) {
	dst.fromString(src);
	return false;
}


} // namespace ssfnet
} // namespace prime
