/*
 * ssfenv :- functions for obtaining system configuration.
 */

#include "ssf.h"
#include "sim/composite.h"

SSF_DECLARE_SHARED(void*, objhash);
SSF_DECLARE_SHARED(ssf_mutex, objhash_mutex);

SSF_DECLARE_SHARED(ssf_mutex, apt_mutex);
SSF_DECLARE_SHARED(long, apt_handle);
SSF_DECLARE_SHARED(void*, apt_hash);
SSF_DECLARE_SHARED(void*, apt_queue);

namespace prime {
namespace ssf {

int ssf_num_machines() { return SSF_USE_SHARED_RO(nmachs); }

int ssf_machine_index() { return SSF_USE_SHARED_RO(whoami); }

int ssf_num_processors() { return SSF_NPROCS; }

int ssf_num_processors(int mach)
{
  if(0 <= mach && mach < SSF_USE_SHARED_RO(nmachs)) 
    return SSF_USE_SHARED_RO(mach_nprocs)[mach];
  else return 0;
}

int ssf_processor_index() { return SSF_SELF; }

int ssf_processor_range(int& startidx, int& endidx)
{
  int np = 0;
  for(int i=0; i<SSF_USE_SHARED_RO(whoami); i++)
    np += SSF_USE_SHARED_RO(mach_nprocs)[i];
  startidx = np;
  endidx = np+SSF_NPROCS-1;
  return np+SSF_SELF;
}

int ssf_total_num_processors()
{
  int np = 0;
  for(int i=0; i<SSF_USE_SHARED_RO(nmachs); i++)
    np += SSF_USE_SHARED_RO(mach_nprocs)[i];
  return np;
}

int ssf_total_processor_index()
{
  int np = 0;
  for(int i=0; i<SSF_USE_SHARED_RO(whoami); i++)
    np += SSF_USE_SHARED_RO(mach_nprocs)[i];
  return np+SSF_SELF;
}

void ssf_set_global_object(char* objname, void* obj)
{
  ssf_mutex_wait(&SSF_USE_SHARED(objhash_mutex));
  if(!SSF_USE_SHARED(objhash))
    SSF_USE_SHARED(objhash) = new SSF_UNIV_STRPTR_MAP;
  if(!((SSF_UNIV_STRPTR_MAP*)SSF_USE_SHARED(objhash))->
     insert(SSF_MAKE_PAIR(objname, obj)).second)
    ssf_throw_exception(ssf_exception::globalobj, objname);
  ssf_mutex_signal(&SSF_USE_SHARED(objhash_mutex));
}

void* ssf_get_global_object(char* objname)
{
  void* obj = 0;
  if(SSF_USE_SHARED(objhash)) {
    ssf_mutex_wait(&SSF_USE_SHARED(objhash_mutex));
    SSF_UNIV_STRPTR_MAP::iterator iter =
      ((SSF_UNIV_STRPTR_MAP*)
       SSF_USE_SHARED(objhash))->find(objname);
    if(iter != ((SSF_UNIV_STRPTR_MAP*)
		SSF_USE_SHARED(objhash))->end())
      obj = (*iter).second;
    ssf_mutex_signal(&SSF_USE_SHARED(objhash_mutex));
  }
  return obj;
}

long ssf_shmem_barrier_set(void (*fct)(long, ltime_t, void*), 
			   ltime_t tm, void* data, boolean recur)
{
  if(recur && tm == SSF_LTIME_ZERO ||
     (!recur && tm < SSF_USE_PRIVATE(now))) return 0;
  ssf_mutex_wait(&SSF_USE_SHARED(apt_mutex));
  long handle = ++SSF_USE_SHARED(apt_handle);
  ssf_shmem_barrier_appointment* apt;
  if(recur) apt = new ssf_shmem_barrier_appointment
	      (SSF_USE_PRIVATE(now)+tm, handle, fct, data, tm);
  else apt = new ssf_shmem_barrier_appointment(tm, handle, fct, data);
  ((ssf_shmem_barrier_queue*)SSF_USE_SHARED(apt_queue))->push(apt);
  ((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->
    insert(SSF_MAKE_PAIR(handle, (void*)apt));
  ssf_mutex_signal(&SSF_USE_SHARED(apt_mutex));
  return handle;
}

boolean ssf_shmem_barrier_cancel(long handle)
{
  ssf_mutex_wait(&SSF_USE_SHARED(apt_mutex));
  SSF_UNIV_INTPTR_MAP::iterator iter = 
    ((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->find(handle);
  if(iter != ((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->end()) {
    void* apt = (*iter).second;
    ((SSF_UNIV_INTPTR_MAP*)SSF_USE_SHARED(apt_hash))->erase(iter);
    ((ssf_shmem_barrier_appointment*)apt)->handle = 0;
    ssf_mutex_signal(&SSF_USE_SHARED(apt_mutex));
    return true;
  } else {
    ssf_mutex_signal(&SSF_USE_SHARED(apt_mutex));
    return false;
  }
}

}; // namespace ssf
}; // namespace prime

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
