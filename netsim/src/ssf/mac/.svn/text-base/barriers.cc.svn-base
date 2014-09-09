/*
 * barrier.cc :- barriers in shared memory.
 */

#include <assert.h>
#include <stdio.h>
#include <string.h>

#include "mac/arena.h"
#include "mac/machines.h"
#include "mac/segments.h"
#include "mac/barriers.h"

#if SSF_USE_THREAD_BARRIER

/*
 * POSIX threads: barrier is implemented using mutex and conditional
 * variable. Could be more efficient in a thread environment.
 */

namespace prime {
namespace ssf {

static int ssf_thread_barrier_counter = 0;
static pthread_mutex_t ssf_thread_barrier_mutex   = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t  ssf_thread_barrier_release = PTHREAD_COND_INITIALIZER;

static void ssf_my_global_init_barrier() {}
static void ssf_my_local_init_barrier() {}
static void ssf_my_local_wrapup_barrier() {}
static void ssf_my_global_wrapup_barrier() {}

void ssf_barrier()
{
  if(SSF_NPROCS == 1) return;
  assert(SSF_SELF < SSF_NPROCS);

  pthread_mutex_lock(&ssf_thread_barrier_mutex);
  ssf_thread_barrier_counter++;
  if(ssf_thread_barrier_counter == SSF_NPROCS) {
    ssf_thread_barrier_counter = 0;
    pthread_cond_broadcast(&ssf_thread_barrier_release);
  } else {
    pthread_cond_wait(&ssf_thread_barrier_release,
		      &ssf_thread_barrier_mutex);
  }
  pthread_mutex_unlock(&ssf_thread_barrier_mutex);
}

}; // namespace ssf
}; // namespace prime

#else /*not SSF_USE_THREAD_BARRIER*/

/* choose one and only one of the following!!! */
#define SSF_USE_SENSE_REVERSE_BARRIER 0 // not using sense reverse barrier
#define SSF_USE_PHASE_COUNT_BARRIER   1 // using phase count barrier

SSF_DECLARE_SHARED(volatile int**, barrier_exceptions);
SSF_DECLARE_PRIVATE(volatile int*, my_barrier_exception);

namespace prime {
namespace ssf {

static void ssf_notify_barrier_exception(void* p)
{
  for(int i=0; i<SSF_NPROCS; i++)
    if(i != SSF_SELF) *SSF_USE_SHARED(barrier_exceptions)[i] = 1;
}

// We use a statically allocated array (ssf_atexit_callbacks) to hold
// at-exit callback routines and the user-defined data to be passed to
// the callbacks. That is, two entries are needed for each callback
// registered. Dynamic memory de/allocation may not be acceptable at
// this stage. The variable ssf_atexit_entry indicates the next free
// slot of this array. Since the data structure is global, therefore
// we must make sure the at-exit routines are registered before the
// child processes are forked. (see above).

// maximum ataxit callbacks
#define SSF_ATEXIT_ARRAYSIZ 1024

/*
 * One can set a callback function that will be invoked when there is
 * a fatal error and the program is about to be terminated. The
 * function is called with a parameter, which is a pointer to the
 * predefined user data.
 */
typedef void (*ssf_atexit_callback_t)(void* data);

static void* ssf_atexit_callbacks[SSF_ATEXIT_ARRAYSIZ];
static int ssf_atexit_entry = 0;

// process atexit callbacks, one at a time
static void ssf_atexit_handler()
{
  for(int i=0; i<ssf_atexit_entry; i+=2) {
    ssf_atexit_callback_t fct = (ssf_atexit_callback_t)ssf_atexit_callbacks[i];
    void* data = ssf_atexit_callbacks[i+1];
    (*fct)(data);
  }
}

static void ssf_set_atexit(ssf_atexit_callback_t fct, void* data = 0) {
  if(ssf_atexit_entry+1 < SSF_ATEXIT_ARRAYSIZ) {
    ssf_atexit_callbacks[ssf_atexit_entry++] = (void*)fct;
    ssf_atexit_callbacks[ssf_atexit_entry++] = data;
  } else {
    // we don't want use ssf_exception here or we have a loop
    fprintf(stderr, "ERROR: ssf::ssf_set_atexit() too many callbacks\n");
    exit(1);
  }
}

}; // namespace ssf
}; // namespace prime

#if SSF_USE_SENSE_REVERSE_BARRIER

SSF_DECLARE_SHARED(ssf_mutex, barrier_lock);
SSF_DECLARE_SHARED(volatile int, barrier_counter);
SSF_DECLARE_SHARED(volatile int, barrier_release);
SSF_DECLARE_PRIVATE(int, barrier_direction);

namespace prime {
namespace ssf {

static void ssf_my_global_init_barrier()
{
  if(SSF_NPROCS > 1) {
    SSF_USE_SHARED(barrier_exceptions) =
      (volatile int**)ssf_arena_malloc_fixed(SSF_NPROCS*sizeof(int*));
    assert(SSF_USE_SHARED(barrier_exceptions));
    prime::ssf::ssf_mutex_init(&SSF_USE_SHARED(barrier_lock));
    SSF_USE_SHARED(barrier_counter) = SSF_USE_SHARED(barrier_release);
  }
}

static void ssf_my_global_wrapup_barrier() {}

static void ssf_my_local_init_barrier()
{
  if(SSF_NPROCS > 1 && SSF_SELF < SSF_NPROCS) {
    SSF_USE_PRIVATE(my_barrier_exception) = SSF_USE_SHARED(barrier_exceptions)[SSF_SELF] = 
      (volatile int*)ssf_arena_malloc_fixed(sizeof(int));
    *SSF_USE_PRIVATE(my_barrier_exception) = 0;
    SSF_USE_PRIVATE(barrier_direction) = 0;
    ssf_barrier();
    ssf_set_atexit(&ssf_notify_barrier_exception);
  }
}

static void ssf_my_local_wrapup_barrier() {}

void ssf_barrier()
{
  if(SSF_NPROCS == 1) return;
  assert(SSF_SELF < SSF_NPROCS);

  int local_counter;
  SSF_USE_PRIVATE(barrier_direction) = !SSF_USE_PRIVATE(barrier_direction);
  
  prime::ssf::ssf_mutex_wait(&SSF_USE_SHARED(barrier_lock));
  local_counter = ++SSF_USE_SHARED(barrier_counter);
  if(local_counter == SSF_NPROCS) {
    SSF_USE_SHARED(barrier_counter) = 0;
    prime::ssf::ssf_mutex_signal(&SSF_USE_SHARED(barrier_lock));
    SSF_USE_SHARED(barrier_release) = SSF_USE_PRIVATE(barrier_direction);
  } else {
    prime::ssf::ssf_mutex_signal(&SSF_USE_SHARED(barrier_lock));
    while(SSF_USE_SHARED(barrier_release) != SSF_USE_PRIVATE(barrier_direction)) {
      ssf_yield_proc();
      if(*SSF_USE_PRIVATE(my_barrier_exception)) break;
    }
  }
}

}; // namespace ssf
}; // namespace prime

#elif SSF_USE_PHASE_COUNT_BARRIER

SSF_DECLARE_SHARED(volatile int *, barrier_phase_count);
SSF_DECLARE_PRIVATE(int, barrier_phase_num);

namespace prime {
namespace ssf {

void ssf_my_global_init_barrier()
{
  if(SSF_NPROCS > 1) {
    SSF_USE_SHARED(barrier_exceptions) =
      (volatile int**)ssf_arena_malloc_fixed(SSF_NPROCS*sizeof(int*));
    assert(SSF_USE_SHARED(barrier_exceptions));
    SSF_USE_SHARED(barrier_phase_count) = 
      (volatile int *)ssf_arena_malloc_fixed(SSF_NPROCS*sizeof(int));
    assert(SSF_USE_SHARED(barrier_phase_count));
    memset((void *)SSF_USE_SHARED(barrier_phase_count), 0, 
	   (SSF_NPROCS*sizeof(int)));
  }
}

static void ssf_my_global_wrapup_barrier() {}

static void ssf_my_local_init_barrier() 
{
  if(SSF_NPROCS > 1 && SSF_SELF < SSF_NPROCS) {
    SSF_USE_PRIVATE(my_barrier_exception) = SSF_USE_SHARED(barrier_exceptions)[SSF_SELF] = 
      (volatile int*)ssf_arena_malloc_fixed(sizeof(int));
    *SSF_USE_PRIVATE(my_barrier_exception) = 0;
    SSF_USE_PRIVATE(barrier_phase_num) = 0;
    ssf_barrier();
    ssf_set_atexit(&ssf_notify_barrier_exception);
  }
}

static void ssf_my_local_wrapup_barrier() {}

void ssf_barrier() 
{
  if(SSF_NPROCS == 1) return;
  assert(SSF_SELF < SSF_NPROCS);

  /* mark that we're here */
  SSF_USE_SHARED(barrier_phase_count)[SSF_SELF]++;
  
  /* wait until all PEs have gotten at least this far. Do this by
     computing the minimum barrier_phase_count among PEs until that minimum
     has reached barrier_phase_num. */
  SSF_USE_PRIVATE(barrier_phase_num)++;
  int j =  SSF_USE_SHARED(barrier_phase_count)[0];
  for(int i=1; i<SSF_NPROCS; i++)
    if(SSF_USE_SHARED(barrier_phase_count)[i] < j) j = SSF_USE_SHARED(barrier_phase_count)[i];
  while(j < SSF_USE_PRIVATE(barrier_phase_num)) {
    ssf_yield_proc();
    if(*SSF_USE_PRIVATE(my_barrier_exception)) break;
    j =  SSF_USE_SHARED(barrier_phase_count)[0];
    for(int i=1; i<SSF_NPROCS; i++)
      if(SSF_USE_SHARED(barrier_phase_count)[i] < j) j = SSF_USE_SHARED(barrier_phase_count)[i];
  }
}

}; // namespace ssf
}; // namespace prime

#else /*neither SSF_USE_SENSE_REVERSE_BARRIER nor SSF_USE_PHASE_COUNT_BARRIER*/
#error "must define either SSF_USE_SENSE_REVERSE_BARRIER or SSF_USE_PHASE_COUNT_BARRIER"
#endif

#endif /*not SSF_USE_THREAD_BARRIER*/

/*
 * global reduction is supported for all the following data types
 */

SSF_DECLARE_SHARED(void**, reduction_array);
SSF_DECLARE_PRIVATE(void*, my_reduction);

namespace prime {
namespace ssf {

static void ssf_my_global_init_reduction()
{
  if(SSF_NPROCS > 1) {
    SSF_USE_SHARED(reduction_array) = 
      (void**)ssf_arena_malloc_fixed(SSF_NPROCS*sizeof(void*));
    assert(SSF_USE_SHARED(reduction_array));
  }
}

static void ssf_my_global_wrapup_reduction() {}

static void ssf_my_local_init_reduction()
{
  if(SSF_NPROCS > 1) {
    SSF_USE_PRIVATE(my_reduction) = SSF_USE_SHARED(reduction_array)[SSF_SELF] = 
      (void*)ssf_arena_malloc_fixed(16); // big enough for all basic data types
    assert(SSF_USE_PRIVATE(my_reduction));
  }
}

static void ssf_my_local_wrapup_reduction() {}
 
int8 ssf_min_reduction(int8 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  int8 min = mymin;
  *(int8*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    int8 tmp = *(int8*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

int8 ssf_sum_reduction(int8 myval)
{
  if(SSF_NPROCS == 1) return myval;
  int8 sum = 0;
  *(int8*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(int8*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

int16 ssf_min_reduction(int16 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  int16 min = mymin;
  *(int16*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    int16 tmp = *(int16*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

int16 ssf_sum_reduction(int16 myval)
{
  if(SSF_NPROCS == 1) return myval;
  int16 sum = 0;
  *(int16*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(int16*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

int32 ssf_min_reduction(int32 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  int32 min = mymin;
  *(int32*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    int32 tmp = *(int32*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

int32 ssf_sum_reduction(int32 myval)
{
  if(SSF_NPROCS == 1) return myval;
  int32 sum = 0;
  *(int32*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(int32*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

int64 ssf_min_reduction(int64 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  int64 min = mymin;
  *(int64*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    int64 tmp = *(int64*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

int64 ssf_sum_reduction(int64 myval)
{
  if(SSF_NPROCS == 1) return myval;
  int64 sum = 0;
  *(int64*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(int64*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

uint8 ssf_min_reduction(uint8 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  uint8 min = mymin;
  *(uint8*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    uint8 tmp = *(uint8*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

uint8 ssf_sum_reduction(uint8 myval)
{
  if(SSF_NPROCS == 1) return myval;
  uint8 sum = 0;
  *(uint8*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(uint8*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

uint16 ssf_min_reduction(uint16 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  uint16 min = mymin;
  *(uint16*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    uint16 tmp = *(uint16*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

uint16 ssf_sum_reduction(uint16 myval)
{
  if(SSF_NPROCS == 1) return myval;
  uint16 sum = 0;
  *(uint16*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(uint16*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

uint32 ssf_min_reduction(uint32 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  uint32 min = mymin;
  *(uint32*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    uint32 tmp = *(uint32*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

uint32 ssf_sum_reduction(uint32 myval)
{
  if(SSF_NPROCS == 1) return myval;
  uint32 sum = 0;
  *(uint32*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(uint32*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

uint64 ssf_min_reduction(uint64 mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  uint64 min = mymin;
  *(uint64*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    uint64 tmp = *(uint64*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

uint64 ssf_sum_reduction(uint64 myval)
{
  if(SSF_NPROCS == 1) return myval;
  uint64 sum = 0;
  *(uint64*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(uint64*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

float ssf_min_reduction(float mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  float min = mymin;
  *(float*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    float tmp = *(float*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

float ssf_sum_reduction(float myval)
{
  if(SSF_NPROCS == 1) return myval;
  float sum = 0;
  *(float*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(float*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

double ssf_min_reduction(double mymin)
{
  if(SSF_NPROCS == 1) return mymin;
  double min = mymin;
  *(double*)SSF_USE_PRIVATE(my_reduction) = mymin;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) {
    double tmp = *(double*)SSF_USE_SHARED(reduction_array)[i];
    if(tmp < min) min = tmp;
  }
  ssf_barrier();
  return min;
}

double ssf_sum_reduction(double myval)
{
  if(SSF_NPROCS == 1) return myval;
  double sum = 0;
  *(double*)SSF_USE_PRIVATE(my_reduction) = myval;
  ssf_barrier();
  for(int i=0; i<SSF_NPROCS; i++) 
    sum += *(double*)SSF_USE_SHARED(reduction_array)[i];
  ssf_barrier();
  return sum;
}

void ssf_global_init_barriers()
{
  ssf_my_global_init_barrier();
  ssf_my_global_init_reduction();
}

void ssf_global_wrapup_barriers()
{
  ssf_my_global_wrapup_barrier();
  ssf_my_global_wrapup_reduction();
}

void ssf_local_init_barriers()
{
  ssf_my_local_init_barrier();
  ssf_my_local_init_reduction();
}

void ssf_local_wrapup_barriers()
{
  ssf_my_local_wrapup_barrier();
  ssf_my_local_wrapup_reduction();
}

}; // namespace ssf
}; // namespace prime

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
