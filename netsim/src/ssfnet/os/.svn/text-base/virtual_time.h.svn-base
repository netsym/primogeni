/**
 * \file virtual_time.h
 * \brief Header file for the VirtualTime class.
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

#ifndef __VIRTUAL_TIME_H__
#define __VIRTUAL_TIME_H__

#include <iostream>
#include <math.h>
#include "ssf.h"
#include "os/ssfnet_types.h"
#include "os/config_vars.h"

// this following macro is defined in ssf.h to indicate the primitive
// data type chosen by SSF to represent the simulation time
#ifndef PRIME_SSF_LTIME_LONGLONG
#error "ERROR: SSFNET requires ltime_t be of long long type."
#endif

#if HAVE_ROUND
#define ROUNDUP(x) round(x)
#else
#define ROUNDUP(x) floor((x)+0.5)
#endif

namespace prime {
namespace ssfnet {

/**
 * \brief Simulation time with units.
 *
 * The VirtualTime class is a representation of simulation time. It's
 * implemented with an internal ltime_t value which is counting the
 * number of clock ticks from the beginning of a simulation run. The
 * VirtualTime class provides methods for converting simulation clock
 * ticks to and from time values given in seconds, milliseconds,
 * microseconds, and nanoseconds. Typically, a tick is set to be a
 * nanosecond. But it's not always so. Do not assume that a nanosecond
 * is one tick.
 */
class VirtualTime  {
 public:

  /**
   * This is for backward compatibility. Returns the number of ticks
   * in a second. Users may find other functions such as second() and
   * nanosecond() much more useful.
   */
  inline static prime::ssf::ltime_t getFrequency() { return SECOND; }

  /**
   * This function is expected to be called by top net. The frequency
   * has to be set greater than or equal to 10e9. Otherwise, there
   * will be a lost of precision. The user may only be able to choose
   * a higher frequency (specified in DML).
   */
  static void setFrequency(prime::ssf::ltime_t freq);

  /** @name Constants for simulation time. */
  //@{

  /** A simulation second. */
  static prime::ssf::ltime_t SECOND;

  /** A simulation millisecond. */
  static prime::ssf::ltime_t MILLISECOND;

  /** A simulation microsecond. */
  static prime::ssf::ltime_t MICROSECOND;

  /** A simulation nanosecond. */
  static prime::ssf::ltime_t NANOSECOND;

  //#}

  /**@name Constructors from different types.
   *
   * An optional time unit can be provided as the second argument.
   * Be it SECOND, MILLISECOND, MICROSECOND, or NANOSECOND. The
   * default is NANOSECOND. The newly created virtual time object
   * will be in clock ticks.
   */
  //@{

  /** Convert from float. */
  VirtualTime(const float f = 0, prime::ssf::ltime_t unit = NANOSECOND) :
    vtime((prime::ssf::ltime_t)(f*unit)) {}

  /** Convert from double. */
  VirtualTime(const double d, prime::ssf::ltime_t unit = NANOSECOND) :
    vtime((prime::ssf::ltime_t)(d*unit)) {}

  /** Convert from int. */
  VirtualTime(const int i, prime::ssf::ltime_t unit = NANOSECOND) :
    vtime((prime::ssf::ltime_t)(i*unit)) {}

  /** Convert from long. */
  VirtualTime(const long l, prime::ssf::ltime_t unit = NANOSECOND) :
    vtime((prime::ssf::ltime_t)(l*unit)) {}

  /** Convert from long long. */
  VirtualTime(const long long ll, prime::ssf::ltime_t unit = NANOSECOND) :
    vtime((prime::ssf::ltime_t)(ll*unit)) {}

  //@}

  /** Copy constructor. */
  VirtualTime(const VirtualTime& vt) : vtime(vt.vtime) {}

  /**@name Convert clock ticks to time units.
   *
   * Translate from clock ticks to different time units. The return
   * type is double.
   */
  //@{

  /** Convert to second. */
  double second() const { return double(vtime)/SECOND; }

  /** Convert to millisecond. */
  double millisecond() const { return double(vtime)/MILLISECOND; }

  /** Convert to microsecond. */
  double microsecond() const { return double(vtime)/MICROSECOND; }

  /** Convert to nanosecond. */
  double nanosecond() const { return double(vtime)/NANOSECOND; }

  //@}

  /**@name Type conversions.
   *
   * Returns the number of clock ticks.
   */
  //@{

  /** Cast to float. */
  operator float() const { return (float)vtime; }

  /** Cast to double. */
  operator double() const { return (double)vtime; }

  /** Cast to int. */
  operator int() const { return (int)vtime; }

  /** Cast to long. */
  operator long() const { return (long)vtime; }

  /** Cast to long long. */
  operator long long() const { return (long long)vtime; }

  //@}

  /**@name Assignment operators.
   *
   * Assignment operations from different types. The variable at the
   * right-hand side of the assignment operator is assumed to be the
   * number of clock ticks.
   */
  //@{

  /** Assignment operator. */
  VirtualTime operator = (const VirtualTime& vt)
    { vtime = vt.vtime; return *this; }

  /** Assign from int. */
  VirtualTime operator = (const int i)
    { vtime = (prime::ssf::ltime_t)i; return *this; }

  /** Assign from long. */
  VirtualTime operator = (const long l)
    { vtime = (prime::ssf::ltime_t)l; return *this; }

  /** Assign from long long. */
  VirtualTime operator = (const long long ll)
    { vtime = (prime::ssf::ltime_t)ll; return *this; }

  /** Assign from float. */
  VirtualTime operator = (const float f)
    { vtime = (prime::ssf::ltime_t)f; return *this; }

  /** Assign from double. */
  VirtualTime operator = (const double d)
    { vtime = (prime::ssf::ltime_t)d; return *this; }

  /** Plus equal operator. */
  VirtualTime operator += (const VirtualTime& vt)
    { vtime += vt.vtime; return *this; }

  /** Plus equal an int. */
  VirtualTime operator += (const int i)
    { vtime += (prime::ssf::ltime_t)i; return *this; }

  /** Plus equal a long. */
  VirtualTime operator += (const long l)
    { vtime += (prime::ssf::ltime_t)l; return *this; }

  /** Plus equal a long long. */
  VirtualTime operator += (const long long ll)
    { vtime += (prime::ssf::ltime_t)ll; return *this; }

  /** Plus equal a float. */
  VirtualTime operator += (const float f)
    { vtime += (prime::ssf::ltime_t)f; return *this; }

  /** Plus equal a double. */
  VirtualTime operator += (const double d)
    { vtime += (prime::ssf::ltime_t)d; return *this; }

  /** Minus equal operator. */
  VirtualTime operator -= (const VirtualTime& vt)
    { vtime -= vt.vtime; return *this; }

  /** Minus equal an int. */
  VirtualTime operator -= (const int i)
    { vtime -= (prime::ssf::ltime_t)i; return *this; }

  /** Minus equal a long. */
  VirtualTime operator -= (const long l)
    { vtime -= (prime::ssf::ltime_t)l; return *this; }

  /** Minus equal a long long. */
  VirtualTime operator -= (const long long ll)
    { vtime -= (prime::ssf::ltime_t)ll; return *this; }

  /** Minus equal a float. */
  VirtualTime operator -= (const float f)
    { vtime -= (prime::ssf::ltime_t)f; return *this; }

  /** Minus equal a double. */
  VirtualTime operator -= (const double d)
    { vtime -= (prime::ssf::ltime_t)d; return *this; }

  /** Multiply equal operator. */
  VirtualTime operator *= (const VirtualTime& vt)
    { vtime *= vt.vtime; return *this; }

  /** Multiply equal an int. */
  VirtualTime operator *= (const int i)
    { vtime *= (prime::ssf::ltime_t)i; return *this; }

  /** Multiply equal a long. */
  VirtualTime operator *= (const long l)
    { vtime *= (prime::ssf::ltime_t)l; return *this; }

  /** Multiply equal a long long. */
  VirtualTime operator *= (const long long ll)
    { vtime *= (prime::ssf::ltime_t)ll; return *this; }

  /** Multiply equal a float. */
  VirtualTime operator *= (const float f)
    { vtime *= (prime::ssf::ltime_t)f; return *this; }

  /** Multiply equal a double. */
  VirtualTime operator *= (const double d)
    { vtime *= (prime::ssf::ltime_t)d; return *this; }

  /** Divide equal operator. */
  VirtualTime operator /= (const VirtualTime& vt)
    { vtime /= vt.vtime; return *this; }

  /** Divide equal an int. */
  VirtualTime operator /= (const int i)
    { vtime /= (prime::ssf::ltime_t)i; return *this; }

  /** Divide equal a long. */
  VirtualTime operator /= (const long l)
    { vtime /= (prime::ssf::ltime_t)l; return *this; }

  /** Divide equal a long long. */
  VirtualTime operator /= (const long long ll)
    { vtime /= (prime::ssf::ltime_t)ll; return *this; }

  /** Divide equal a float. */
  VirtualTime operator /= (const float f)
    { vtime /= (prime::ssf::ltime_t)f; return *this; }

  /** Divide equal a double. */
  VirtualTime operator /= (const double d)
    { vtime /= (prime::ssf::ltime_t)d; return *this; }

  //@}

  /**@name Other Mathematical operators. */
  //@{

  /** Unary negative operator. */
  VirtualTime operator - () const { return VirtualTime(-vtime); }

  /** Plus operator. */
  friend VirtualTime operator + (const VirtualTime& vt1, const VirtualTime& vt2)
    { return VirtualTime(vt1.vtime+vt2.vtime); }

  /** Plus VirtualTime and int. */
  friend VirtualTime operator + (const VirtualTime& vt1, const int i)
    { return VirtualTime(vt1.vtime+((prime::ssf::ltime_t)i)); }

  /** Plus int and VirtualTime. */
  friend VirtualTime operator + (const int i, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)i)+vt2.vtime); }

  /** Plus VirtualTime and long. */
  friend VirtualTime operator + (const VirtualTime& vt1, const long l)
    { return VirtualTime(vt1.vtime+((prime::ssf::ltime_t)l)); }

  /** Plus long and VirtualTime. */
  friend VirtualTime operator + (const long l, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)l)+vt2.vtime); }

  /** Plus VirtualTime and long long. */
  friend VirtualTime operator + (const VirtualTime& vt1, const long long ll)
    { return VirtualTime(vt1.vtime+((prime::ssf::ltime_t)ll)); }

  /** Plus long long and VirtualTime. */
  friend VirtualTime operator + (const long long ll, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)ll)+vt2.vtime); }

  /** Plus VirtualTime and float. */
  friend VirtualTime operator + (const VirtualTime& vt1, const float f)
    { return VirtualTime(vt1.vtime+((prime::ssf::ltime_t)f)); }

  /** Plus float and VirtualTime. */
  friend VirtualTime operator + (const float f, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)f)+vt2.vtime); }

  /** Plus VirtualTime and double. */
  friend VirtualTime operator + (const VirtualTime& vt1, const double d)
    { return VirtualTime(vt1.vtime+((prime::ssf::ltime_t)d)); }

  /** Plus double and VirtualTime. */
  friend VirtualTime operator + (const double d, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)d)+vt2.vtime); }

  /** Minus operator. */
  friend VirtualTime operator - (const VirtualTime& vt1, const VirtualTime& vt2)
    { return VirtualTime(vt1.vtime-vt2.vtime); }

  /** Minus VirtualTime and int. */
  friend VirtualTime operator - (const VirtualTime& vt1, const int i)
    { return VirtualTime(vt1.vtime-((prime::ssf::ltime_t)i)); }

  /** Minus int and VirtualTime. */
  friend VirtualTime operator - (const int i, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)i)-vt2.vtime); }

  /** Minus VirtualTime and long. */
  friend VirtualTime operator - (const VirtualTime& vt1, const long l)
    { return VirtualTime(vt1.vtime-((prime::ssf::ltime_t)l)); }

  /** Minus long and VirtualTime. */
  friend VirtualTime operator - (const long l, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)l)-vt2.vtime); }

  /** Minus VirtualTime and long long. */
  friend VirtualTime operator - (const VirtualTime& vt1, const long long ll)
    { return VirtualTime(vt1.vtime-((prime::ssf::ltime_t)ll)); }

  /** Minus long long and VirtualTime. */
  friend VirtualTime operator - (const long long ll, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)ll)-vt2.vtime); }

  /** Minus VirtualTime and float. */
  friend VirtualTime operator - (const VirtualTime& vt1, const float f)
    { return VirtualTime(vt1.vtime-((prime::ssf::ltime_t)f)); }

  /** Minus float and VirtualTime. */
  friend VirtualTime operator - (const float f, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)f)-vt2.vtime); }

  /** Minus VirtualTime and double. */
  friend VirtualTime operator - (const VirtualTime& vt1, const double d)
    { return VirtualTime(vt1.vtime-((prime::ssf::ltime_t)d)); }

  /** Minus double and VirtualTime. */
  friend VirtualTime operator - (const double d, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)d)-vt2.vtime); }

  /** Multiply operator. */
  friend VirtualTime operator * (const VirtualTime& vt1, const VirtualTime& vt2)
    { return VirtualTime(vt1.vtime*vt2.vtime); }

  /** Multiply VirtualTime and int. */
  friend VirtualTime operator * (const VirtualTime& vt1, const int i)
    { return VirtualTime(vt1.vtime*((prime::ssf::ltime_t)i)); }

  /** Multiply int and VirtualTime. */
  friend VirtualTime operator * (const int i, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)i)*vt2.vtime); }

  /** Multiply VirtualTime and long. */
  friend VirtualTime operator * (const VirtualTime& vt1, const long l)
    { return VirtualTime(vt1.vtime*((prime::ssf::ltime_t)l)); }

  /** Multiply long and VirtualTime. */
  friend VirtualTime operator * (const long l, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)l)*vt2.vtime); }

  /** Multiply VirtualTime and long long. */
  friend VirtualTime operator * (const VirtualTime& vt1, const long long ll)
    { return VirtualTime(vt1.vtime*((prime::ssf::ltime_t)ll)); }

  /** Multiply long long and VirtualTime. */
  friend VirtualTime operator * (const long long ll, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)ll)*vt2.vtime); }

  /** Multiply VirtualTime and float. */
  friend VirtualTime operator * (const VirtualTime& vt1, const float f)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(vt1.vtime*((double)f))); }

  /** Multiply float and VirtualTime. */
  friend VirtualTime operator * (const float f, const VirtualTime& vt2)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(((double)f)*vt2.vtime)); }

  /** Multiply VirtualTime and double. */
  friend VirtualTime operator * (const VirtualTime& vt1, const double d)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(vt1.vtime*((double)d))); }

  /** Multiply double and VirtualTime. */
  friend VirtualTime operator * (const double d, const VirtualTime& vt2)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(((double)d)*vt2.vtime)); }

  /** Divide operator. */
  friend VirtualTime operator / (const VirtualTime& vt1, const VirtualTime& vt2)
    { return VirtualTime(vt1.vtime/vt2.vtime); }

  /** Divide VirtualTime and int. */
  friend VirtualTime operator / (const VirtualTime& vt1, const int i)
    { return VirtualTime(vt1.vtime/((prime::ssf::ltime_t)i)); }

  /** Divide int and VirtualTime. */
  friend VirtualTime operator / (const int i, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)i)/vt2.vtime); }

  /** Divide VirtualTime and long. */
  friend VirtualTime operator / (const VirtualTime& vt1, const long l)
    { return VirtualTime(vt1.vtime/((prime::ssf::ltime_t)l)); }

  /** Divide long and VirtualTime. */
  friend VirtualTime operator / (const long l, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)l)/vt2.vtime); }

  /** Divide VirtualTime and long long. */
  friend VirtualTime operator / (const VirtualTime& vt1, const long long ll)
    { return VirtualTime(vt1.vtime/((prime::ssf::ltime_t)ll)); }

  /** Divide long long and VirtualTime. */
  friend VirtualTime operator / (const long long ll, const VirtualTime& vt2)
    { return VirtualTime(((prime::ssf::ltime_t)ll)/vt2.vtime); }

  /** Divide VirtualTime and float. */
  friend VirtualTime operator / (const VirtualTime& vt1, const float f)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(vt1.vtime/((double)f))); }

  /** Divide float and VirtualTime. */
  friend VirtualTime operator / (const float f, const VirtualTime& vt2)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(((double)f)/vt2.vtime)); }

  /** Divide VirtualTime and double. */
  friend VirtualTime operator / (const VirtualTime& vt1, const double d)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(vt1.vtime/((double)d))); }

  /** Divide double and VirtualTime. */
  friend VirtualTime operator / (const double d, const VirtualTime& vt2)
    { return VirtualTime((prime::ssf::ltime_t) ROUNDUP(((double)d)/vt2.vtime)); }

  //@}

  /**@name Comparison operators. */
  //@{

  /** The == operator. */
  friend int operator == (const VirtualTime& vt1, const VirtualTime& vt2)
    { return vt1.vtime == vt2.vtime; }

  /** The == operator. */
  friend int operator == (const VirtualTime& vt1, const int i)
    { return vt1.vtime == (prime::ssf::ltime_t)i; }

  /** The == operator. */
  friend int operator == (const int i, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)i == vt2.vtime; }

  /** The == operator. */
  friend int operator == (const VirtualTime& vt1, const long l)
    { return vt1.vtime == (prime::ssf::ltime_t)l; }

  /** The == operator. */
  friend int operator == (const long l, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)l == vt2.vtime; }

  /** The == operator. */
  friend int operator == (const VirtualTime& vt1, const long long ll)
    { return vt1.vtime == (prime::ssf::ltime_t)ll; }

  /** The == operator. */
  friend int operator == (const long long ll, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)ll == vt2.vtime; }

  /** The == operator. */
  friend int operator == (const VirtualTime& vt1, const float f)
    { return vt1.vtime == (prime::ssf::ltime_t)f; }

  /** The == operator. */
  friend int operator == (const float f, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)f == vt2.vtime; }

  /** The == operator. */
  friend int operator == (const VirtualTime& vt1, const double d)
    { return vt1.vtime == (prime::ssf::ltime_t)d; }

  /** The == operator. */
  friend int operator == (const double d, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)d == vt2.vtime; }

  /** The != operator. */
  friend int operator != (const VirtualTime& vt1, const VirtualTime& vt2)
    { return vt1.vtime != vt2.vtime; }

  /** The != operator. */
  friend int operator != (const VirtualTime& vt1, const int i)
    { return vt1.vtime != (prime::ssf::ltime_t)i; }

  /** The != operator. */
  friend int operator != (const int i, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)i != vt2.vtime; }

  /** The != operator. */
  friend int operator != (const VirtualTime& vt1, const long l)
    { return vt1.vtime != (prime::ssf::ltime_t)l; }

  /** The != operator. */
  friend int operator != (const long l, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)l != vt2.vtime; }

  /** The != operator. */
  friend int operator != (const VirtualTime& vt1, const long long ll)
    { return vt1.vtime != (prime::ssf::ltime_t)ll; }

  /** The != operator. */
  friend int operator != (const long long ll, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)ll != vt2.vtime; }

  /** The != operator. */
  friend int operator != (const VirtualTime& vt1, const float f)
    { return vt1.vtime != (prime::ssf::ltime_t)f; }

  /** The != operator. */
  friend int operator != (const float f, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)f != vt2.vtime; }

  /** The != operator. */
  friend int operator != (const VirtualTime& vt1, const double d)
    { return vt1.vtime != (prime::ssf::ltime_t)d; }

  /** The != operator. */
  friend int operator != (const double d, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)d != vt2.vtime; }

  /** The >= operator. */
  friend int operator >= (const VirtualTime& vt1, const VirtualTime& vt2)
    { return vt1.vtime >= vt2.vtime; }

  /** The >= operator. */
  friend int operator >= (const VirtualTime& vt1, const int i)
    { return vt1.vtime >= (prime::ssf::ltime_t)i; }

  /** The >= operator. */
  friend int operator >= (const int i, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)i >= vt2.vtime; }

  /** The >= operator. */
  friend int operator >= (const VirtualTime& vt1, const long l)
    { return vt1.vtime >= (prime::ssf::ltime_t)l; }

  /** The >= operator. */
  friend int operator >= (const long l, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)l >= vt2.vtime; }

  /** The >= operator. */
  friend int operator >= (const VirtualTime& vt1, const long long ll)
    { return vt1.vtime >= (prime::ssf::ltime_t)ll; }

  /** The >= operator. */
  friend int operator >= (const long long ll, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)ll >= vt2.vtime; }

  /** The >= operator. */
  friend int operator >= (const VirtualTime& vt1, const float f)
    { return vt1.vtime >= (prime::ssf::ltime_t)f; }

  /** The >= operator. */
  friend int operator >= (const float f, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)f >= vt2.vtime; }

  /** The >= operator. */
  friend int operator >= (const VirtualTime& vt1, const double d)
    { return vt1.vtime >= (prime::ssf::ltime_t)d; }

  /** The >= operator. */
  friend int operator >= (const double d, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)d >= vt2.vtime; }

  /** The <= operator. */
  friend int operator <= (const VirtualTime& vt1, const VirtualTime& vt2)
    { return vt1.vtime <= vt2.vtime; }

  /** The <= operator. */
  friend int operator <= (const VirtualTime& vt1, const int i)
    { return vt1.vtime <= (prime::ssf::ltime_t)i; }

  /** The <= operator. */
  friend int operator <= (const int i, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)i <= vt2.vtime; }

  /** The <= operator. */
  friend int operator <= (const VirtualTime& vt1, const long l)
    { return vt1.vtime <= (prime::ssf::ltime_t)l; }

  /** The <= operator. */
  friend int operator <= (const long l, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)l <= vt2.vtime; }

  /** The <= operator. */
  friend int operator <= (const VirtualTime& vt1, const long long ll)
    { return vt1.vtime <= (prime::ssf::ltime_t)ll; }

  /** The <= operator. */
  friend int operator <= (const long long ll, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)ll <= vt2.vtime; }

  /** The <= operator. */
  friend int operator <= (const VirtualTime& vt1, const float f)
    { return vt1.vtime <= (prime::ssf::ltime_t)f; }

  /** The <= operator. */
  friend int operator <= (const float f, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)f <= vt2.vtime; }

  /** The <= operator. */
  friend int operator <= (const VirtualTime& vt1, const double d)
    { return vt1.vtime <= (prime::ssf::ltime_t)d; }

  /** The <= operator. */
  friend int operator <= (const double d, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)d <= vt2.vtime; }

  /** The > operator. */
  friend int operator > (const VirtualTime& vt1, const VirtualTime& vt2)
    { return vt1.vtime > vt2.vtime; }

  /** The > operator. */
  friend int operator > (const VirtualTime& vt1, const int i)
    { return vt1.vtime > (prime::ssf::ltime_t)i; }

  /** The > operator. */
  friend int operator > (const int i, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)i > vt2.vtime; }

  /** The > operator. */
  friend int operator > (const VirtualTime& vt1, const long l)
    { return vt1.vtime > (prime::ssf::ltime_t)l; }

  /** The > operator. */
  friend int operator > (const long l, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)l > vt2.vtime; }

  /** The > operator. */
  friend int operator > (const VirtualTime& vt1, const long long ll)
    { return vt1.vtime > (prime::ssf::ltime_t)ll; }

  /** The > operator. */
  friend int operator > (const long long ll, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)ll > vt2.vtime; }

  /** The > operator. */
  friend int operator > (const VirtualTime& vt1, const float f)
    { return vt1.vtime > (prime::ssf::ltime_t)f; }

  /** The > operator. */
  friend int operator > (const float f, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)f > vt2.vtime; }

  /** The > operator. */
  friend int operator > (const VirtualTime& vt1, const double d)
    { return vt1.vtime > (prime::ssf::ltime_t)d; }

  /** The > operator. */
  friend int operator > (const double d, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)d > vt2.vtime; }

  /** The < operator. */
  friend int operator < (const VirtualTime& vt1, const VirtualTime& vt2)
    { return vt1.vtime < vt2.vtime; }

  /** The < operator. */
  friend int operator < (const VirtualTime& vt1, const int i)
    { return vt1.vtime < (prime::ssf::ltime_t)i; }

  /** The < operator. */
  friend int operator < (const int i, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)i < vt2.vtime; }

  /** The < operator. */
  friend int operator < (const VirtualTime& vt1, const long l)
    { return vt1.vtime < (prime::ssf::ltime_t)l; }

  /** The < operator. */
  friend int operator < (const long l, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)l < vt2.vtime; }

  /** The < operator. */
  friend int operator < (const VirtualTime& vt1, const long long ll)
    { return vt1.vtime < (prime::ssf::ltime_t)ll; }

  /** The < operator. */
  friend int operator < (const long long ll, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)ll < vt2.vtime; }

  /** The < operator. */
  friend int operator < (const VirtualTime& vt1, const float f)
    { return vt1.vtime < (prime::ssf::ltime_t)f; }

  /** The < operator. */
  friend int operator < (const float f, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)f < vt2.vtime; }

  /** The < operator. */
  friend int operator < (const VirtualTime& vt1, const double d)
    { return vt1.vtime < (prime::ssf::ltime_t)d; }

  /** The < operator. */
  friend int operator < (const double d, const VirtualTime& vt2)
    { return (prime::ssf::ltime_t)d < vt2.vtime; }

  //@}

  /** The assignment operator from string. */
  VirtualTime& operator=(const SSFNET_STRING& str) { fromString(str); return *this; }

  /** Convert to string. */
  SSFNET_STRING toString() const;

  /** Convert from string. */
  void fromString(const SSFNET_STRING& str);

  /** The << operator. */
  friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const VirtualTime& x);

 protected:
  /** Clock ticks at highest simulation time precision (in nanoseconds). */
  prime::ssf::ltime_t vtime;
};

template<> bool rw_config_var<SSFNET_STRING, VirtualTime>
  (SSFNET_STRING& dst, VirtualTime& src);

template<> bool rw_config_var<VirtualTime, SSFNET_STRING>
  (VirtualTime& dst, SSFNET_STRING& src);

} // namespace ssfnet
} // namespace prime

#endif /*__VIRTUAL_TIME_H__*/
