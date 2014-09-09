/**
 * \file ssftime.h
 * \brief Type of SSF simulation time.
 *
 * Currently supported simulation time types (prime::ssf::ltime_t)
 * include long, long long, float, and double. The type must be chosen
 * at the configuration time before one compiles SSF. The user should
 * not include this header file explicitly, as it has been included by
 * ssf.h automatically.
 */

#ifndef __PRIME_SSF_SSFTIME_H__
#define __PRIME_SSF_SSFTIME_H__

#include "api/ssftype.h"

namespace prime {
namespace ssf {

/**
 * \def SSF_LTIME_INFINITY
 * \brief The constant maximum value of ltime_t type.
 */

/**
 * \def SSF_LTIME_MINUS_INFINITY
 * \brief The constant minumum value of ltime_t type.
 */

/**
 * \def SSF_LTIME_ZERO
 * \brief The constant zero of ltime_t type.
 */

#ifdef SSF_LTIME_INFINITY
#undef SSF_LTIME_INFINITY
#endif

#ifdef SSF_LTIME_MINUS_INFINITY
#undef SSF_LTIME_MINUS_INFINITY
#endif

#ifdef SSF_LTIME_ZERO
#undef SSF_LTIME_ZERO
#endif

#if defined(PRIME_SSF_LTIME_FLOAT)

/**
 * \brief ltime_t is defined as float.
 *
 * One can use --with-ltime=(float|double|long|longlong) option at
 * configuration to redefine ltime_t type.
 */
typedef float ltime_t;

#define SSF_LTIME_INFINITY		((float)3.402823e+38)
#define SSF_LTIME_MINUS_INFINITY	((float)-3.402823e+38)
#define SSF_LTIME_ZERO			((float)0.0)

#elif defined(PRIME_SSF_LTIME_DOUBLE)

/**
 * \brief ltime_t is defined as double.
 *
 * One can use --with-ltime=(float|double|long|longlong) option at
 * configuration to redefine ltime_t type.
 */
typedef double ltime_t;

#define SSF_LTIME_INFINITY		((double)1.797693e+308)
#define SSF_LTIME_MINUS_INFINITY	((double)-1.797693e+308)
#define SSF_LTIME_ZERO			((double)0.0)

#elif defined(PRIME_SSF_LTIME_LONG)

/**
 * \brief ltime_t is defined as a 32-bit integer.
 *
 * One can use --with-ltime=(float|double|long|longlong) option at
 * configuration to redefine ltime_t type.
 */
typedef prime::ssf::int32 ltime_t;

#define SSF_LTIME_INFINITY		((prime::ssf::int32)(LONG_MAX/2))
#define SSF_LTIME_MINUS_INFINITY	((prime::ssf::int32)(LONG_MIN/2))
#define SSF_LTIME_ZERO			((prime::ssf::int32)0)

#elif defined(PRIME_SSF_LTIME_LONGLONG)

/**
 * \brief ltime_t is defined as long long.
 *
 * One can use --with-ltime=(float|double|long|longlong) option at
 * configuration to redefine ltime_t type.
 */
typedef SSF_LONG_LONG ltime_t;

#if defined(LLONG_MAX)
#define SSF_LTIME_INFINITY		((SSF_LONG_LONG)(LLONG_MAX/2))
#define SSF_LTIME_MINUS_INFINITY	((SSF_LONG_LONG)(LLONG_MIN/2))
#elif defined(LONG_LONG_MAX)
#define SSF_LTIME_INFINITY		((SSF_LONG_LONG)(LONG_LONG_MAX/2))
#define SSF_LTIME_MINUS_INFINITY	((SSF_LONG_LONG)(LONG_LONG_MIN/2))
#else
#define SSF_LTIME_INFINITY		((SSF_LONG_LONG)(((SSF_UNSIGNED_LONG_LONG)-1LL)>>2))
#define SSF_LTIME_MINUS_INFINITY	((SSF_LONG_LONG)(-SSF_LTIME_INFINITY))
#endif
#define SSF_LTIME_ZERO			((SSF_LONG_LONG)0)

#else
#error "undefined ltime_t type"
#endif

/**
 * \brief Serialize an ltime_t type variable.
 * \param tm is the time to be serialized
 * \param buf is the byte array to store the serialized result
 * \return the pointer to the byte array
 *
 * This function serializes the given simulation time value into a
 * byte array whose content is independent of the underlying machine
 * platform (i.e., the endian type). The function does not check the
 * size of the preallocated buffer. But if the buffer is NULL, the
 * function allocates a new buffer. In any case, the pointer to the
 * buffer with the serialized time value is returned.
 */
extern char* ssf_ltime_serialize(ltime_t tm, char* buf = 0);

/**
 * \brief Deserialize an ltime_t type variable.
 * \param tm is the time variable as the result of deserialization
 * \param buf is the byte array that stores the time to be deserialized
 *
 * This function deserializes the byte array into a simulation time of
 * appropriate type. It is expected that the byte array contains the
 * time value that is platform-independent (i.e., without any
 * assumption on the endian type of this machine).
 */
extern void ssf_ltime_deserialize(ltime_t& tm, char* buf);

#ifdef PRIME_SSF_SYNC_MPI
/**
 * \def SSF_LTIME_MPI_TYPE
 * \brief The corresponding MPI type for the ltime_t type.
 */
#if defined(PRIME_SSF_LTIME_FLOAT)
#define SSF_LTIME_MPI_TYPE MPI_FLOAT
#elif defined(PRIME_SSF_LTIME_DOUBLE)
#define SSF_LTIME_MPI_TYPE MPI_DOUBLE
#elif defined(PRIME_SSF_LTIME_LONG)
#define SSF_LTIME_MPI_TYPE MPI_LONG
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#define SSF_LTIME_MPI_TYPE MPI_LONG_LONG_INT
#else
#error "Unknow ltime_t type"
#endif
#endif /*PRIME_SSF_SYNC_MPI*/

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef ltime_t Time;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFTIME_H__*/

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
