/**
 * \file ssfqobj.h
 * \brief SSF quick-memory object.
 *
 * This header file contains the definition of the quick-memory object
 * class (prime::ssf::ssf_quickobj) for fast memory allocation and
 * deallocation services.
 */

#ifndef __PRIME_SSF_SSFQOBJ_H__
#define __PRIME_SSF_SSFQOBJ_H__

#include <sys/types.h>

namespace prime {
namespace ssf {

/**
 * \brief Fast memory allocation and deallocation services.
 *
 * Quick memory is a memory management layer that provides faster
 * memory allocation and deallocation services with some additional
 * memory overhead due to fragmentation.
 *
 * The ssf_quickobj class is the base class of all objects that
 * require fast memory allocation and deallocation (such as the Event
 * class). The user can simply derive the ssf_quickobj class to enjoy
 * the quick memory service.
 */
class ssf_quickobj {

#if PRIME_SSF_QUICKMEM

 public:
  /**
   * \brief Overloads the default new operator.
   * \param sz is the size of the object to be created.
   * \returns a pointer to the allocated memory.
   *
   * The new operator is called whenever a quick object is to be
   * created dynamically. The memory block is allocated from the
   * quick memory.
   */
  static void* operator new(size_t sz);

  /**
   * \brief Overloads the default delete operator.
   * \param p points to the memory block to be reclaimed.
   *
   * The delete operator is called whenever a quick object is
   * reclaimed. The memory block is returned to the quick memory.
   */
  static void operator delete(void* p);

#endif /*PRIME_SSF_QUICKMEM*/

 public:
  /**
   * \brief Allocates a memory block of the given size.
   * \param sz is the size of the memory block.
   * \return a pointer to the allocated memory.
   *
   * This static method is provided to the user to obtain a quick
   * memory block of the given size. The quick memory block can only
   * be reclaimed using the correponding quick_delete method.
   */
  static void* quick_new(size_t sz);

  /**
   * \brief Deallocates a memory block of the given size.
   * \param p points to the memory block to be reclaimed.
   * \param sz is the size of the memory block.
   *
   * This static method is provided to the user to reclaim a quick
   * memory block of the given size. The memory block must be from the
   * quick memory management. Previously, the memory block size must
   * be provided as the parameter; this is no longer necessary. The
   * argument is still here for only compatibility.
   */
  static void quick_delete(void* p, size_t sz = 0);
}; /*ssf_quickobj*/


#if PRIME_SSF_BACKWARD_COMPATIBILITY
typedef ssf_quickobj QuickObject;
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFQOBJ_H__*/

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
