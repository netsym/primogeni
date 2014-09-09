/*
 * composite.h :- core header file for the composite synchronization.
 */

#ifndef __PRIME_SSF_COMPOSITE_H__
#define __PRIME_SSF_COMPOSITE_H__

#include <fstream>

#if PRIME_SSF_SYNC_MPI
#include <mpi.h>
#endif

#if PRIME_SSF_SYNC_LIBSYNK
extern "C" {
#include "fm.h"
#include "rm.h"
#include "tm.h"
}
#endif

#include "mac/arena.h"
#include "mac/barriers.h"
#include "mac/machines.h"
#include "mac/segments.h"
#include "mac/memcpp.h"
#include "sim/debug.h"
#include "sim/splaytree.h"
#include "api/ssfexception.h"
#include "api/ssftype.h"
#include "api/ssftime.h"
#include "api/ssfcompact.h"

// forward declarations
namespace prime {
namespace ssf {

class ssf_bin_queue;
class ssf_universe;
class ssf_map_node;
class ssf_local_tlt;
class ssf_remote_tlt;
class ssf_stargate;
class ssf_teleport;
class ssf_logical_process;

}; // namespace ssf
}; // namespace prime
 
#include "sim/binque.h"
#include "sim/rtqueue.h"
#include "sim/logiproc.h"
#include "sim/messenger.h"
#include "sim/stargate.h"
#include "sim/teleport.h"
#include "sim/universe.h"

#endif /*__PRIME_SSF_COMPOSITE_H__*/

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
