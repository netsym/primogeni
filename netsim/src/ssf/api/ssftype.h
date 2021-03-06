/**
 * \file ssftype.h
 * \brief Platform-independent primitive types and standard
 * template management.
 *
 * This file contains definitions of system-independent primitive
 * types that are guaranteed to have the specified size and
 * format. This file also contains definition of new STL templates
 * using the memory allocator that is compatible with the SSF
 * shared-memory management layer (prime::ssf::SSF_ALLOCATOR). The
 * user should not include this header file explicitly, as it has been
 * included by ssf.h automatically.
 */

#ifndef __PRIME_SSF_SSFTYPE_H__
#define __PRIME_SSF_SSFTYPE_H__

#include <limits.h>
#include "mac/machines.h"

#if defined(PRIME_SSF_ARCH_WINDOWS)

namespace prime {
namespace ssf {

  /*
   * We define these primitive types because C/C++ varies the size of
   * the primitive types on different platforms (different machine
   * architecture, different operating system, or even different
   * compiler switches). The types defined here are explicit about
   * their sizes.
   */

  /** \name Platform-Independent Primitive Data Types */
  /** @{ */
  typedef char			int8;	///< 8-bit character type
  typedef unsigned char		uint8;	///< 8-bit unsigned character type
  typedef unsigned char		byte;	///< 8-bit unsigned character type
  typedef short			int16;	///< 16-bit integer type (short)
  typedef unsigned short	uint16;	///< 16-bit unsigned integer type (unsigned short)
  typedef __int32		int32;	///< 32-bit integer type
  typedef unsigned __int32	uint32;	///< 32-bit unsigned integer type
  typedef __int64		int64;	///< 64-bit integer type
  typedef unsigned _int64	uint64;	///< 64-bit unsigned integer type
  /** @} */

}; // namespace ssf
}; // namespace prime

#else // unix platforms

// we haven't found a good platform-independent way to specify
// primitive types; the definition here says if this is the case,
// let's revert to the original C/C++ primitive types to avoid 
// problems (see below why we don't want this if it's possible)
#define PRIME_SSF_USE_PRIMITIVE_TYPES 1

#if !PRIME_SSF_USE_PRIMITIVE_TYPES
// this header file defines basic types
#include <inttypes.h>
#include <sys/types.h>
#endif

namespace prime {
namespace ssf {

  /*
   * We define these primitive types because C/C++ varies the size of
   * the primitive types on different platforms (different machine
   * architecture, different operating system, or even different
   * compiler switches). The types defined here are explicit about
   * their sizes.
   */

  /** \name Platform-Independent Primitive Data Types */
  /** @{ */
#if PRIME_SSF_USE_PRIMITIVE_TYPES
  typedef char		int8;	///< 8-bit character type
  typedef unsigned char	uint8;	///< 8-bit unsigned character type
  typedef unsigned char	byte;	///< 8-bit unsigned character type
  typedef short		int16;	///< 16-bit integer type (short)
  typedef unsigned short uint16;	///< 16-bit unsigned integer type (unsigned short)
  typedef int		int32;	///< 32-bit integer type
  typedef unsigned int	uint32;	///< 32-bit unsigned integer type
  typedef long long int	int64;	///< 64-bit integer type
  typedef unsigned long long int uint64;	///< 64-bit unsigned integer type
#else
  typedef int8_t	int8;	///< 8-bit character type
  typedef u_int8_t	uint8;	///< 8-bit unsigned character type
  typedef u_int8_t	byte;	///< 8-bit unsigned character type
  typedef int16_t	int16;	///< 16-bit integer type (short)
  typedef u_int16_t	uint16;	///< 16-bit unsigned integer type (unsigned short)
  typedef int32_t	int32;	///< 32-bit integer type
  typedef u_int32_t	uint32;	///< 32-bit unsigned integer type
#if HAVE_U_INT64_T
  typedef int64_t	int64;	///< 64-bit integer type
  typedef u_int64_t	uint64;	///< 64-bit unsigned integer type
#else
  typedef long long int	int64;	///< 64-bit integer type
  typedef unsigned long long int uint64;	///< 64-bit unsigned integer type
#endif
#endif
  /** @} */

}; // namespace ssf
}; // namespace prime

#endif /*PRIME_SSF_ARCH_WINDOWS*/

typedef prime::ssf::int64  SSF_LONG_LONG;
typedef prime::ssf::uint64 SSF_UNSIGNED_LONG_LONG;

#include <deque>
#include <map>
#include <set>
#include <string>
#include <vector>

#include "mac/quickmem.h"

namespace prime {
namespace ssf {

  /**
 * \brief Template for STL allocator for shared memory.
 *
 * The difference between the C++ templates that are managed by SSF
 * and those from the standard template library (STL) is the memory
 * allocator used internally and implicitly by these template data
 * structures. In SSF, the template data structure may be used on
 * multiple processors via the shared memory. To facilitate that, the
 * simulation kernel replaces the default new and delete operators
 * with those that allocate and deallocate memory from the shared
 * memory (called arena) rather than the program heap, which by
 * default is not shared by multiprocessors. For templates, the memory
 * allocator used by template library must be aware of such
 * replacement. This is why we have to define new templates. Of
 * course, one can still use the original templates from STL (with
 * namespace std) as long as the data structure is not to be shared
 * among multiple processors (for example, a vector used only by an
 * SSF entity).
 *
 * This template is supposed to be used by the STL classes as the
 * memory allocator. Only by doing so will those STL objects be able
 * to be accessed by multiple processors. In SSF, we defined a few
 * standard STL container templates for convenience, including:
<pre>
    #define SSF_LESS(T) std::less<T > 
    #define SSF_MAKE_PAIR(A,B) std::make_pair(A,B) 
    #define SSF_PAIR(A,B) std::pair<A,B > 
    #define SSF_PRIORITY_QUEUE(T,S,C) std::priority_queue<T,S,C > 
    #define SSF_DEQUE(T) std::deque<T > 
    #define SSF_MAP(S,T) std::map<S,T > 
    #define SSF_MULTIMAP(S,T) std::multimap<S,T > 
    #define SSF_SET(T) std::set<T > 
    #define SSF_VECTOR(T) std::vector<T > 
    #define SSF_STRING std::string 
</pre>
 * The real definitions of the above templates vary and may include
 * the memory allocator if SSF shared memory management layer is
 * engaged.
 */
template<class T> class SSF_ALLOCATOR {
public:
  typedef T value_type;
  typedef size_t size_type;
  typedef std::ptrdiff_t difference_type;

  typedef T* pointer;
  typedef const T* const_pointer;

  typedef T& reference;
  typedef const T& const_reference;

  pointer address(reference r) const { return &r; }
  const_pointer address(const_reference r) const { return &r; }

  SSF_ALLOCATOR() throw() {}
  SSF_ALLOCATOR(const SSF_ALLOCATOR&) throw() {}
  template<class U> SSF_ALLOCATOR(const SSF_ALLOCATOR<U>&) throw() {}
  ~SSF_ALLOCATOR() throw() {}
  
  static pointer allocate(size_type n, const void* = 0) {
    //cerr << "allocate " << n << " element(s)" 
    //  << " of size " << sizeof(T) << endl;
    pointer ret = (pointer)(::operator new (n*sizeof(T)));
    //cerr << " allocated at: " << (void*)ret << std::endl;
    return ret;
  }

  static void deallocate(void* p, size_type n) {
    //cerr << "deallocate " << n << " element(s)" << " of size "
    //  << sizeof(T) << " at: " << (void*)p << endl;
    ::operator delete((void*)p);
  }

  void construct(pointer p, const T& val) { new ((void*)p) T(val); }
  void destroy(pointer p) { p->~T(); }

  //size_type max_size() const throw() { return LONG_MAX/sizeof(T); }
  size_type max_size() const throw() {
    return std::max(size_type(1), size_type(UINT_MAX/sizeof(T)));
  }

  /*
    friend bool operator==<>(const SSF_ALLOCATOR<T>&, const SSF_ALLOCATOR<T>&);
    friend bool operator!=<>(const SSF_ALLOCATOR<T>&, const SSF_ALLOCATOR<T>&);
  */

  template <class U> struct rebind { typedef SSF_ALLOCATOR<U> other; };
}; /*SSF_ALLOCATOR<T>*/

template<> class SSF_ALLOCATOR<void> {
  typedef size_t      size_type;
  typedef std::ptrdiff_t   difference_type;
  typedef void*       pointer;
  typedef const void* const_pointer;
  typedef void        value_type;
    
  template<class U> struct rebind { typedef SSF_ALLOCATOR<U> other; };
}; /*SSF_ALLOCATOR<>*/

template<class T> bool operator == 
  (const SSF_ALLOCATOR<T>&, const SSF_ALLOCATOR<T>&) throw() { return true; }

template<class T> bool operator != 
  (const SSF_ALLOCATOR<T>&, const SSF_ALLOCATOR<T>&) throw() { return false; }

/* The only different between SSF_ALLOCATOR and SSF_QUICK_ALLOCATOR is
   that the latter uses quick memory. */

#if PRIME_SSF_QUICKMEM
/**
 * \brief Template for STL allocator for shared memory using quick memory.
 *
 * The only difference from SSF_ALLOCATOR is that we use quick memory
 * allocation and deallocation services (see ssf_quickobj).
 */
template<class T> class SSF_QUICK_ALLOCATOR {
public:
  typedef T value_type;
  typedef size_t size_type;
  typedef std::ptrdiff_t difference_type;

  typedef T* pointer;
  typedef const T* const_pointer;

  typedef T& reference;
  typedef const T& const_reference;

  pointer address(reference r) const { return &r; }
  const_pointer address(const_reference r) const { return &r; }

  SSF_QUICK_ALLOCATOR() throw() {}
  SSF_QUICK_ALLOCATOR(const SSF_QUICK_ALLOCATOR&) throw() {}
  template<class U> SSF_QUICK_ALLOCATOR(const SSF_QUICK_ALLOCATOR<U>&) throw() {}
  ~SSF_QUICK_ALLOCATOR() throw() {}
  
  static pointer allocate(size_type n, const void* = 0) {
    //cerr << "allocate " << n << " element(s)" 
    //  << " of size " << sizeof(T) << endl;
    pointer ret = (pointer)ssf_quickmem_malloc(n*sizeof(T));
    //cerr << " allocated at: " << (void*)ret << std::endl;
    return ret;
  }

  static void deallocate(void* p, size_type n) {
    //cerr << "deallocate " << n << " element(s)" << " of size "
    //  << sizeof(T) << " at: " << (void*)p << endl;
    ssf_quickmem_free((void*)p, n*sizeof(T));
  }

  void construct(pointer p, const T& val) { new ((void*)p) T(val); }
  void destroy(pointer p) { p->~T(); }

  //size_type max_size() const throw() { return LONG_MAX/sizeof(T); }
  size_type max_size() const throw() {
    return std::max(size_type(1), size_type(UINT_MAX/sizeof(T)));
  }

  /*
    friend bool operator==<>(const SSF_QUICK_ALLOCATOR<T>&, const SSF_QUICK_ALLOCATOR<T>&);
    friend bool operator!=<>(const SSF_QUICK_ALLOCATOR<T>&, const SSF_QUICK_ALLOCATOR<T>&);
  */

  template <class U> struct rebind { typedef SSF_QUICK_ALLOCATOR<U> other; };
}; /*SSF_QUICK_ALLOCATOR<T>*/

template<> class SSF_QUICK_ALLOCATOR<void> {
  typedef size_t      size_type;
//  typedef ptrdiff_t   difference_type;
  typedef void*       pointer;
  typedef const void* const_pointer;
  typedef void        value_type;
    
  template<class U> struct rebind { typedef SSF_QUICK_ALLOCATOR<U> other; };
}; /*SSF_QUICK_ALLOCATOR<>*/

template<class T> bool operator == 
  (const SSF_QUICK_ALLOCATOR<T>&, const SSF_QUICK_ALLOCATOR<T>&) throw() { return true; }

template<class T> bool operator != 
  (const SSF_QUICK_ALLOCATOR<T>&, const SSF_QUICK_ALLOCATOR<T>&) throw() { return false; }
#endif /*PRIME_SSF_QUICKMEM*/

/*
 * Standard STL containers must be redefined for them to use the SSF
 * shared-memory layer on multiprocessors.  Here we only define STL
 * types that are used by the SSF interface and the SSF kernel. For
 * those STL types not defined in the following, you should follow
 * suit.
 */

// for certain installations, the heap segment is actually shared
// among multiprocessors, in which case there's no need to replace the
// default memeory allocator.

#define SSF_LESS(T) std::less<T > 
#define SSF_MAKE_PAIR(A,B) std::make_pair(A,B) 
#define SSF_PAIR(A,B) std::pair<A,B > 
#define SSF_PRIORITY_QUEUE(T,S,C) std::priority_queue<T,S,C > 

#if SSF_SHARED_HEAP_SEGMENT
#define SSF_DEQUE(T) std::deque<T > 
#define SSF_MAP(S,T) std::map<S,T > 
#define SSF_MULTIMAP(S,T) std::multimap<S,T > 
#define SSF_SET(T) std::set<T > 
#define SSF_VECTOR(T) std::vector<T > 
#define SSF_STRING std::string 
#else
#define SSF_DEQUE(T) std::deque<T,prime::ssf::SSF_ALLOCATOR<T > > 
template <typename S, typename T>
class ssf_map : public std::map<S,T,std::less<S>,prime::ssf::SSF_ALLOCATOR<std::pair<const S,T> > > {};
#define SSF_MAP(S,T) ssf_map<S,T >
//std::map<S,T,std::less<S >,prime::ssf::SSF_ALLOCATOR<std::pair<const S,T > > > 
template <typename S, typename T> 
class ssf_multimap : public std::multimap<S,T,std::less<S>,prime::ssf::SSF_ALLOCATOR<std::pair<const S,T> > > {};
#define SSF_MULTIMAP(S,T) ssf_multimap<S,T >
//#define SSF_MULTIMAP(S,T) std::multimap<S,T,std::less<S >,prime::ssf::SSF_ALLOCATOR<std::pair<const S,T > > > 
#define SSF_SET(T) std::set<T,std::less<T >,prime::ssf::SSF_ALLOCATOR<T > > 
#define SSF_VECTOR(T) std::vector<T,prime::ssf::SSF_ALLOCATOR<T > > 
#if !defined(__GNUC__) || (__GNUC__ >= 3)
typedef std::basic_string<char,std::char_traits<char>,prime::ssf::SSF_ALLOCATOR<char> > SSF_STRING;
#else // some quirks with the early gcc version 
typedef std::basic_string<char,std::string_char_traits<char>,prime::ssf::SSF_ALLOCATOR<char> > SSF_STRING;
#endif
#endif /*SSF_SHARED_HEAP_SEGMENT*/

#if PRIME_SSF_QUICKMEM
#define SSF_QDEQUE(T) std::deque<T,prime::ssf::SSF_QUICK_ALLOCATOR<T > > 
template <typename S, typename T> 
class ssf_qmap : public std::map<S,T,std::less<S>,prime::ssf::SSF_QUICK_ALLOCATOR<std::pair<const S,T> > > {};
#define SSF_QMAP(S,T) ssf_qmap<S,T >
template <typename S, typename T> 
class ssf_qmultimap : public std::multimap<S,T,std::less<S>,prime::ssf::SSF_QUICK_ALLOCATOR<std::pair<const S,T> > > {};
#define SSF_QMULTIMAP(S,T) ssf_qmultimap<S,T >
#define SSF_QSET(T) std::set<T,std::less<T >,prime::ssf::SSF_QUICK_ALLOCATOR<T > > 
#define SSF_QVECTOR(T) std::vector<T,prime::ssf::SSF_QUICK_ALLOCATOR<T > > 
#if !defined(__GNUC__) || (__GNUC__ >= 3)
typedef std::basic_string<char,std::char_traits<char>,prime::ssf::SSF_QUICK_ALLOCATOR<char> > SSF_QSTRING;
#else // some quirks with the early gcc version 
typedef std::basic_string<char,std::string_char_traits<char>,prime::ssf::SSF_QUICK_ALLOCATOR<char> > SSF_QSTRING;
#endif
#else
#define SSF_QDEQUE(T) SSF_DEQUE(T)
#define SSF_QMAP(S,T) SSF_MAP(S,T)
#define SSF_QMULTIMAP(S,T) SSF_MULTIMAP(S,T)
#define SSF_QSET(T) SSF_SET(T)
#define SSF_QVECTOR(T) SSF_VECTOR(T)
#define SSF_QSTRING SSF_STRING
#endif

#if PRIME_SSF_BACKWARD_COMPATIBILITY
#define ArenaAllocator prime::ssf::SSF_ALLOCATOR
#endif

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFTYPE_H__*/

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
