/**
 * \file ssfcompact.h
 * \brief SSF data serialization interface.
 *
 * Data serialization is needed for message passing between
 * heterogeneous machine architectures (with different endian types)
 * or for collecting statistics that are stored in a platform
 * independent file. We however encourage the user to use the (boost)
 * serialization library (libserial.a) directly whenever possible. The
 * prime::ssf::ssf_compact class serves as a simple (and more
 * flexible) interface to data serialization.
 */

#ifndef __PRIME_SSF_SSFCOMPACT_H__
#define __PRIME_SSF_SSFCOMPACT_H__

#include <fstream>
#ifdef PRIME_SSF_SYNC_MPI
#include <mpi.h>
#endif

#include "api/ssftime.h"

namespace prime {
namespace ssf {

// Endian conversions: we don't need to do anything if the execution
// environment is homogeneous. That is, all machines running the
// simulation are of the same architecture. Note that running on a
// shared-memory multiprocessor machine or running a sequential
// simulation is considered as homogeneous. We only need to change the
// layout of the primitive data types to the little-endian format
// (i.e., least significant bit first). We chose little endian as the
// default layout since Intel x86 machines are the most popular.

#ifdef SSF_LITTLE_ENDIAN
#undef SSF_LITTLE_ENDIAN
#endif

#ifdef SSF_BIG_ENDIAN
#undef SSF_BIG_ENDIAN
#endif

#if defined(PRIME_SSF_ARCH_WINDOWS)
// FIXME: we assume windows machines are all intel x86 based (little endian)
#define __BYTE_ORDER __LITTLE_ENDIAN
#else
#include <sys/param.h>
#endif

#ifdef __BYTE_ORDER
#if __BYTE_ORDER == __LITTLE_ENDIAN
#define SSF_LITTLE_ENDIAN
#elif __BYTE_ORDER == __BIG_ENDIAN
#define SSF_BIG_ENDIAN
#else
#error "unknown endian type!"
#endif
#else /*undef __BYTE_ORDER*/
// FIXME: endian type is not defined; how to find it out?
#define SSF_LITTLE_ENDIAN
#endif /*__BYTE_ORDER*/

#if defined(SSF_LITTLE_ENDIAN) || PRIME_SSF_HOMOGENEOUS
#define SSF_NO_ENDIAN_FORMATTING
#endif

#ifdef SSF_NO_ENDIAN_FORMATTING

#define SSF_BYTE_ORDER_16(x) (x)
#define SSF_BYTE_ORDER_32(x) (x)
#define SSF_BYTE_ORDER_64(x) (x)

#else

#define SSF_BYTE_ORDER_16(x) \
   ((((prime::ssf::uint16)(x) & 0xff00) >> 8) | \
    (((prime::ssf::uint16)(x) & 0x00ff) << 8))

#define SSF_BYTE_ORDER_32(x) \
   ((((prime::ssf::uint32)(x) & 0xff000000) >> 24) | \
    (((prime::ssf::uint32)(x) & 0x00ff0000) >> 8)  | \
    (((prime::ssf::uint32)(x) & 0x0000ff00) << 8)  | \
    (((prime::ssf::uint32)(x) & 0x000000ff) << 24))

#define SSF_BYTE_ORDER_64(x) \
   ((prime::ssf::uint64(SSF_BYTE_ORDER_32((((prime::ssf::uint64)(x))<<32)>>32))<<32) | \
    SSF_BYTE_ORDER_32(((prime::ssf::uint64)(x))>>32))

#endif /*SSF_NO_ENDIAN_FORMATTING*/

/**
 * \brief Compact data storage.
 *
 * This class is used by the user and the SSF kernel to store data in
 * a memory efficient manner while maintaining platform
 * independence. That is, the compact data stored in this object can
 * be used on any platform regardless of the endian type of the
 * machine. For this reason, this class has been used extensively by
 * the PRIME SSF API. For example, the Event class uses this data type
 * to create events (see prime::ssf::SSF_EVENT_FACTORY) and replicate
 * events across address space (via the pack() method).
 */
class ssf_compact {
public:
  /// The constructor.
  ssf_compact();

  /// The destructor.
  virtual ~ssf_compact();

  /** @{ */
  /* Methods for packing a single item of some primitive type */
  /// Packs a float.
  void add_float(float val); 
  /// Packs a double.
  void add_double(double val); 
#ifdef HAVE_LONG_DOUBLE
  /// Packs a long double.
  void add_long_double(long double val); 
#endif
  /// Packs a character.
  void add_char(char val); 
  /// Packs an unsigned character.
  void add_unsigned_char(unsigned char val); 
  /// Packs a short integer.
  void add_short(short val); 
  /// Packs an unsigned short integer.
  void add_unsigned_short(unsigned short val); 
  /// Packs an integer.
  void add_int(int val); 
  /// Packs an unsigned integer.
  void add_unsigned_int(unsigned int val); 
  /// Packs a long integer.
  void add_long(long val); 
  /// Packs an unsigned long integer.
  void add_unsigned_long(unsigned long val); 
  /// Packs a long long integer.
  void add_long_long(SSF_LONG_LONG val); 
  /// Packs an unsigned long long integer.
  void add_unsigned_long_long(SSF_UNSIGNED_LONG_LONG val); 
  /// Packs time value.
  void add_ltime(ltime_t val); 

  /* methods for packet an array of values of a given type. */
  /// Packs an array of floats.
  void add_float_array(int nitems, float* val_array); 
  /// Packs an array of doubles.
  void add_double_array(int nitems, double* val_array); 
#ifdef HAVE_LONG_DOUBLE
  /// Packs an array of long doubles.
  void add_long_double_array(int nitems, long double* val_array); 
#endif
  /// Packs an array of characters.
  void add_char_array(int nitems, char* val_array); 
  /// Packs an array of unsigned characters.
  void add_unsigned_char_array(int nitems, unsigned char* val_array); 
  /// Packs an array of short integers.
  void add_short_array(int nitems, short* val_array); 
  /// Packs an array of unsigned short integers.
  void add_unsigned_short_array(int nitems, unsigned short* val_array); 
  /// Packs an array of integers.
  void add_int_array(int nitems, int* val_array); 
  /// Packs an array of unsigned integers.
  void add_unsigned_int_array(int nitems, unsigned int* val_array); 
  /// Packs an array of long integers.
  void add_long_array(int nitems, long* val_array); 
  /// Packs an array of unsigned long integers.
  void add_unsigned_long_array(int nitems, unsigned long* val_array); 
  /// Packs an array of long long integers.
  void add_long_long_array(int nitems, SSF_LONG_LONG* val_array); 
  /// Packs an array of unsigned long long integers.
  void add_unsigned_long_long_array(int nitems, SSF_UNSIGNED_LONG_LONG* val_array); 
  /// Packs an array of time values.
  void add_ltime_array(int nitems, ltime_t* val_array); 

  /// Packs a (null-terminated) string.
  void add_string(const char* valstr);
  /** @} */

  /** @{ */
  /* methods for unpacking, returns the total number of items
     retrieved. 0 means error. */
  /// Unpacks a specified number of floats; returns the number of items retrieved.
  int get_float(float* addr, int nitems = 1); 
  /// Unpacks a specified number of doubles; returns the number of items retrieved.
  int get_double(double* addr, int nitems = 1); 
#ifdef HAVE_LONG_DOUBLE
  /// Unpacks a specified number of long doubles; returns the number of items retrieved.
  int get_long_double(long double* addr, int nitems = 1); 
#endif
  /// Unpacks a specified number of characters; returns the number of items retrieved.
  int get_char(char* addr, int nitems = 1); 
  /// Unpacks a specified number of unsigned characters; returns the number of items retrieved.
  int get_unsigned_char(unsigned char* addr, int nitems = 1); 
  /// Unpacks a specified number of short integers; returns the number of items retrieved.
  int get_short(short* addr, int nitems = 1); 
  /// Unpacks a specified number of unsigned short integers; returns the number of items retrieved.
  int get_unsigned_short(unsigned short* addr, int nitems = 1); 
  /// Unpacks a specified number of integers; returns the number of items retrieved.
  int get_int(int* addr, int nitems = 1); 
  /// Unpacks a specified number of unsigned integers; returns the number of items retrieved.
  int get_unsigned_int(unsigned int* addr, int nitems = 1);
  /// Unpacks a specified number of long integers; returns the number of items retrieved.
  int get_long(long* addr, int nitems = 1); 
  /// Unpacks a specified number of unsigned long integers; returns the number of items retrieved.
  int get_unsigned_long(unsigned long* addr, int nitems = 1); 
  /// Unpacks a specified number of long long integers; returns the number of items retrieved.
  int get_long_long(SSF_LONG_LONG* addr, int nitems = 1); 
  /// Unpacks a specified number of unsigned long long integers; returns the number of items retrieved.
  int get_unsigned_long_long(SSF_UNSIGNED_LONG_LONG* addr, int nitems = 1); 
  /// Unpacks a specified number of time values; returns the number of items retrieved.
  int get_ltime(ltime_t* addr, int nitems = 1); 

  /// Unpacks a (null-terminated) string; returns the number of items retrieved.
  char* get_string();
  /** @} */

  /** @{ */
  /* generic "static" method for serialization of primitives (of given size) */
  /// Pack data of the unsigned character type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::uint8 data,  void* buf = 0);
  /// Pack data of the 16-bit unsigned integer type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::uint16 data, void* buf = 0);
  /// Pack data of the 32-bit unsigned integer type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::uint32 data, void* buf = 0);
  /// Pack data of the 64-bit unsigned integer type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::uint64 data, void* buf = 0);
  /// Pack data of the character type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::int8 data,   void* buf = 0);
  /// Pack data of the 16-bit integer type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::int16 data,  void* buf = 0);
  /// Pack data of the 32-bit integer type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::int32 data,  void* buf = 0);
  /// Pack data of the 64-bit integer type into the buffer; returns the pointer to the buffer.
  static void* serialize(prime::ssf::int64 data,  void* buf = 0);
  // FIXME: we assume float is 32-bit and double is 64-bit
  // (independent of the architecture); long double is not included
  // since it's not a platform independent primitive type; right?
  /// Pack data of the float type into the buffer; returns the pointer to the buffer.
  static void* serialize(float data,  void* buf = 0);
  /// Pack data of the double type into the buffer; returns the pointer to the buffer.
  static void* serialize(double data, void* buf = 0);
  /** @} */

  /** @{ */
  /* generic "static" method for deserialization, 0 means success */
  /// Unpack data of the unsigned character type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::uint8& data,  void* buf);
  /// Unpack data of the 16-bit unsigned integer type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::uint16& data, void* buf);
  /// Unpack data of the 32-bit unsigned integer type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::uint32& data, void* buf);
  /// Unpack data of the 64-bit unsigned integer type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::uint64& data, void* buf);
  /// Unpack data of the character type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::int8& data,   void* buf);
  /// Unpack data of the 16-bit integer type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::int16& data,  void* buf);
  /// Unpack data of the 32-bit integer type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::int32& data,  void* buf);
  /// Unpack data of the 64-bit integer type from the buffer; returns zero if successful.
  static int deserialize(prime::ssf::int64& data,  void* buf);
  // FIXME: we assume float is 32-bit and double is 64-bit
  // (independent of the architecture); long double is not included
  // since it's not a platform independent primitive type; right?
  /// Unpack data of the float type from the buffer; returns zero if successful.
  static int deserialize(float& data,  void* buf);
  /// Unpack data of the double type from the buffer; returns zero if successful.
  static int deserialize(double& data, void* buf);
  /** @} */

  /* Use ssf_serialize_ltime() and ssf_deserialize_ltime() functions
     defined in ssftime.h to handle ltime_t data. However, one should
     keep in mind that these functions, like the serialize and
     deserialize functions defined above, do not handle integer type
     conversions. That is, they only handle endian translations, but
     they do not attempt to convert integer types (e.g., int and long)
     that may have a different size across machine architectures.  The
     only ltime_t type that may cause trouble is long. Float, double,
     and long long all have a fixed size. */

private:
  enum { 
    TYPE_FLOAT,  // assume IEEE standard 754 single-precision floating point number (32 bits)
    TYPE_DOUBLE, // assume IEEE standard 754 double-precision floating point number (64 bits)
    TYPE_LONG_DOUBLE, // long double floating point format is not IEEE 754 compliant (128 bits)
    TYPE_INT8,
    TYPE_UINT8,
    TYPE_INT16,
    TYPE_UINT16,
    TYPE_INT32,
    TYPE_UINT32,
    TYPE_INT64,
    TYPE_UINT64,
    TYPE_LTIME_FLOAT, // ltime_t must be fixed (choose one and stick with it); no type conversion is allowed
    TYPE_LTIME_DOUBLE,
    TYPE_LTIME_INT32,
    TYPE_LTIME_INT64,
    TYPE_STRING // null-terminated character string
  };

  int* compact_record; // struct { int type; int size; };
  int compact_rec_capacity;
  int compact_rec_add_offset;
  int compact_rec_get_offset;

  unsigned char* compact_data;
  int compact_capacity;
  int compact_add_offset;
  int compact_get_offset;
  
  void compact_reset();
  void compact_add_record(int type, int size);
  void compact_append_raw(int ch, int size, unsigned char* buf);
  void compact_retrieve_raw(int ch, int size, unsigned char* buf);

public:
  /* the following methods are only used by the ssf kernel. */

  // file I/O; zero means error.
  int read(std::ifstream& ifs);
  int write(std::ofstream& ofs);

#ifndef PRIME_SSF_ARCH_WINDOWS
  int read(int fd);  // read from file (specified by a file descriptor)
  int write(int fd); // write to file (specified by a file descriptor)
#endif

  // packing and unpacking this data structure to and from a byte
  // array. Note that these methods return non-zero if error occurs.
#ifdef PRIME_SSF_SYNC_MPI
  int pack(MPI_Comm comm, char* buf, int& offset, int bufsiz);
  int unpack(MPI_Comm comm, char* buf, int& offset, int bufsiz);
#endif

#ifdef PRIME_SSF_SYNC_LIBSYNK
  int pack(char* buf, int& offset, int bufsiz);
  int unpack(char* buf, int& offset, int bufsiz);
#endif
}; // class ssf_compact

#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef ssf_compact DataPacked;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFCOMPACT_H__*/

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
