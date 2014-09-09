/**
 * \file rng-exception.h
 * \brief Exception handler for the rng library.
 */

#ifndef __PRIME_RNG_EXCEPTION_H__
#define __PRIME_RNG_EXCEPTION_H__

#include <exception>

namespace prime {
namespace rng {


/**
 * \brief RNG exceptions that may be thrown by the RNG library.
 *
 * The RNG library throws exceptions when errors are encountered. The
 * user is expected to catch these exceptions when invoking the public
 * function through the standard API if the user wants to handle the
 * errors in a customed fashion, rather than simply printing the error
 * messages and quitting the program.
 */
class rng_exception : public virtual std::exception {
 public:
  /// All RNG execeptions are defined here.
  enum exception_type_t {
    no_exception,	///< no exception or uninitialized exception
    other_exception,	///< unknown derived exception
    illegal_argument,	///< illegal argument
    spawn_error,	///< error spawning random streams
    total_exceptions	///< total number of exceptions defined in this class
  };

  /**
   * \brief The constructor, typically called by the rng_throw_exception() method.
   * \param c is the error code.
   * \param s is a string, which will be appended to the standard error message.
   */
  rng_exception(int c, const char* s);

  /// Returns the error code.
  virtual int type() { return code; }

  /// Returns the error message of the exception.
  virtual const char* what() const throw() {
    return explanation;
  }

 protected:
  rng_exception() : code(no_exception) { explanation[0] = 0; }

  int code;
  char explanation[512];
}; /*class rng_exception*/

/**
 * \brief Throw an exception of the given type and with a customized error message.
 * \param c is the error code (an enumerated value of exception_type_t).
 * \param s is the customized message that will be appended to the standard error string.
 */
inline void rng_throw_exception(int c, const char* s = 0) { throw rng_exception(c, s); }

}; // namespace rng
}; // namespace prime

#endif /*__PRIME_RNG_EXCEPTION_H__*/

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
