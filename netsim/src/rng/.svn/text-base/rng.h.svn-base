/**
 * \file rng.h
 * \brief Header file for the RNG library.
 * 
 * The RNG library contains several random number and random variate
 * generators, many of which were ported from sprng and boost. These
 * random number generators are encapsulated in the prime::rng::random
 * class.
 */

#ifndef __PRIME_RNG_H__
#define __PRIME_RNG_H__

#include "rng-exception.h"

namespace prime {

/// RNG objects are defined in this namespace.
namespace rng {

  /**
   * \brief RNG random number and random variate generators.
   *
   * <b>DaSSF Random Number Generators</b>
   *
   * For compatibility reasons, we continue to include the
   * Lehmer random number generator used by DaSSF as default. The
   * generator uses the multiplier a = 48271 and modulus m =
   * 2^31-1. The generator has 256 random streams (with the jump
   * multiplier being 22925). For details, consult the book
   * "Discrete-Event Simulation: a First Course", by Lawrence
   * Leemis and Steve Park, Prentice Hall 2005.
   * 
   * The Mersenne Twister random number generator was also used in
   * DaSSF. The generator has a period of 2^19937-1; it gives a
   * sequence that is 623-dimensionally equidistributed.  The code is
   * modified from that of Richard J. Wagner, which is based on code
   * by Makoto Matsumoto, Takuji Nishimura, and Shawn Cokus. For more
   * information, see the inventors' web page at
   * http://www.math.keio.ac.jp/~matumoto/emt.html
   *
   * <b>Random Number Generators in SPRNG Library</b>
   *
   * We include the random number generators of the SPRNG library
   * (version 2.0), developed by Michael Mascagni et al. at Florida
   * State University (http://sprng.cs.fsu.edu), which features 6
   * classes of random number generators:
   *
   * 1. Combined Multiple Recursive Generator (CMRG): the period
   * of this generator is around 2^219; the number of distinct
   * streams available is over 10^8.
   *
   * 2. 48 Bit Linear Congruential Generator with Prime Addend
   * (LCG): the period of this generator is 2^48; the number of
   * distinct streams available is of the order of 2^19.
   *
   * 3. 64 Bit Linear Congruential Generator with Prime Addend
   * (LCG64): the period of this generator is 2^64; the number of
   * distinct streams available is over 10^8.
   *
   * 4. Modified Lagged Fibonacci Generator (LFG): the period of
   * this generator is 2^31(2^l-1) where l is the lag; the number
   * of distinct streams available is 2^[31(l-1)-1].
   *
   * 5. Multiplicative Lagged Fibonacci Generator (MLFG): the
   * period of this generator is 2^61(2^l-1), where l is the lag;
   * the number of distinct streams available is 2^[63(l-1)-1].
   *
   * 6. Prime Modulus Linear Congruential Generator (PMLCG): the
   * period of this generator is 2^61-2; the number of distinct
   * streams available is roughly 258. This generator is not
   * included in our library as it requires the GNU multiprecision
   * arithmetic library (libgmp.a).
   *
   * For more information, consult the SPRNG 2.0 User's Guide.
   *
   * <b>Random Number Generators in BOOST Library</b>
   *
   * We also include the random number generators implemented in the
   * Boost library (http://www.boost.org/libs/random). The performance
   * of these generators are listed on the boost web site. The
   * important numbers are copied here for reference (for speed,
   * higher means faster):
\verbatim
	 generator 	period		memory			speed
	 ---------	------		------			-----
	 MINSTD_RAND0 	2^31-2		sizeof(int32_t)		40
	 MINSTD_RAND 	2^31-2		sizeof(int32_t)		40
	 RAND48 	2^48-1		sizeof(uint64_t) 	80
	 ECUYER1988 	~2^61		2*sizeof(int32_t) 	20
	 KREUTZER1986 	?		1368*sizeof(uint32_t) 	60
	 HELLEKALEK1995	2^31-1		sizeof(int32_t) 	3
	 MT11213B 	2^11213-1 	352*sizeof(uint32_t) 	100
	 MT19937 	2^19937-1 	625*sizeof(uint32_t) 	100
	 LF607	 	~2^32000 	607*sizeof(double) 	150
	 LF1279 	~2^67000 	1279*sizeof(double) 	150
	 LF2281 	~2^120000 	2281*sizeof(double) 	150
	 LF3217 	~2^170000 	3217*sizeof(double) 	150
	 LF4423 	~2^230000 	4423*sizeof(double) 	150
	 LF9689 	~2^510000 	9689*sizeof(double) 	150
	 LF19937 	~2^1050000 	19937*sizeof(double) 	150
	 LF23209 	~2^1200000 	23209*sizeof(double) 	140
	 LF44497 	~2^2300000 	44497*sizeof(double) 	60
\endverbatim
  *
  */
  class Random {
  public:
    /// List of random number generator implementations.
    enum rng_type_t {
      /// Lehmer random number generator originally used in DaSSF.
      RNG_DASSF_LEHMER,

      /// Mersenne Twister random number generator originally used in DaSSF.
      RNG_DASSF_MERSENNE_TWISTER,

#if PRIME_RNG_SPRNG
      /// Combined multiple recursive generator (CMRG), a = 0x27BB2EE687B0B0FD.
      RNG_SPRNG_CMRG_LECU1,

      /// Combined multiple recursive generator(CMRG), a = 0x2C6FE96EE78B6955.
      RNG_SPRNG_CMRG_LECU2,

      /// Combined multiple recursive generator(CMRG), a = 0x369DEA0F31A53F85.
      RNG_SPRNG_CMRG_LECU3,      

      /// 48-bit linear congruential generator with prime addend (LCG), a = 0x2875A2E7B175.
      RNG_SPRNG_LCG_CRAYLCG,

      /// 48-bit linear congruential generator with prime addend (LCG), a = 0x5DEECE66D.
      RNG_SPRNG_LCG_DRAND48,

      /// 48-bit linear congruential generator with prime addend (LCG), a = 0x3EAC44605265.
      RNG_SPRNG_LCG_FISH1,

      /// 48-bit linear congruential generator with prime addend (LCG), a = 0x1EE1429CC9F5.
      RNG_SPRNG_LCG_FISH2,      

      /// 48-bit linear congruential generator with prime addend (LCG), a = 0x275B38EB4BBD.
      RNG_SPRNG_LCG_FISH3,

      /// 48-bit linear congruential generator with prime addend (LCG), a = 0x739A9CB08605.
      RNG_SPRNG_LCG_FISH4,

      /// 48-bit linear congruential generator with prime addend (LCG), a = 0x3228D7CC25F5.
      RNG_SPRNG_LCG_FISH5,

      /// 64-bit linear congruential generator with prime addend (LCG64), a = 0x27BB2EE687B0B0FD.
      RNG_SPRNG_LCG64_LECU1,

      /// 64-bit linear congruential generator with prime addend (LCG64), a = 0x2C6FE96EE78B6955.
      RNG_SPRNG_LCG64_LECU2,

      /// 64-bit linear congruential generator with prime addend (LCG64), a = 0x369DEA0F31A53F85.
      RNG_SPRNG_LCG64_LECU3,

      /// Modified lagged fibonacci generator (LFG), l = 1279, k = 861.
      RNG_SPRNG_LFG_LAG1279,

      /// Modified lagged fibonacci generator (LFG), l = 17, k = 5.
      RNG_SPRNG_LFG_LAG17,

      /// Modified lagged fibonacci generator (LFG), l = 31, k = 6.
      RNG_SPRNG_LFG_LAG31,

      /// Modified lagged fibonacci generator (LFG), l = 55, k = 24.
      RNG_SPRNG_LFG_LAG55,

      /// Modified lagged fibonacci generator (LFG), l = 63, k = 31.
      RNG_SPRNG_LFG_LAG63,

      /// Modified lagged fibonacci generator (LFG), l = 127, k = 97.
      RNG_SPRNG_LFG_LAG127,

      /// Modified lagged fibonacci generator (LFG), l = 521, k = 353.
      RNG_SPRNG_LFG_LAG521,

      /// Modified lagged fibonacci generator (LFG), l = 521, k = 168.
      RNG_SPRNG_LFG_LAG521B,

      /// Modified lagged fibonacci generator (LFG), l = 607, k = 334.
      RNG_SPRNG_LFG_LAG607,

      /// Modified lagged fibonacci generator (LFG), l = 607, k = 273.
      RNG_SPRNG_LFG_LAG607B,

      /// Modified lagged fibonacci generator (LFG), l = 1279, k = 419.
      RNG_SPRNG_LFG_LAG1279B,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 1279, k = 861.
      RNG_SPRNG_MLFG_LAG1279,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 1279, k = 419.
      RNG_SPRNG_MLFG_LAG1279B,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 17, k = 5.
      RNG_SPRNG_MLFG_LAG17,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 31, k = 6.
      RNG_SPRNG_MLFG_LAG31,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 55, k = 24.
      RNG_SPRNG_MLFG_LAG55,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 63, k = 31.
      RNG_SPRNG_MLFG_LAG63,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 127, k = 97.
      RNG_SPRNG_MLFG_LAG127,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 521, k = 353.
      RNG_SPRNG_MLFG_LAG521,
      
      /// Multiplicative lagged fibonacci generator (MLFG), l = 521, k = 168.
      RNG_SPRNG_MLFG_LAG521B,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 607, k = 334.
      RNG_SPRNG_MLFG_LAG607,

      /// Multiplicative lagged fibonacci generator (MLFG), l = 607, k = 273.
      RNG_SPRNG_MLFG_LAG607B,

      //RNG_SPRNG_PMLCG,
#endif

#if PRIME_RNG_BOOST
      /// Linear congruential (Lehmer) RNG, a = 16807.
      RNG_BOOST_MINSTD_RND0,

      /// Linear congruential (Lehmer) RNG, a = 48271.
      RNG_BOOST_MINSTD_RND,

      /// 48-bit linear congruential RNG, same as lrand48(), a = 0x5DEECE66D.
      RNG_BOOST_RAND48,

      /// Additive combine of two MLCGs.
      RNG_BOOST_ECUYER1988,

      /// A specialization of shuffle output.
      RNG_BOOST_KREUTZER1986,

      /// A specialization of inversive PRNG.
      RNG_BOOST_HELLEKALEK1995,

      /// Mersenne Twister.
      RNG_BOOST_MT11213B,

      /// Mersenne Twister
      RNG_BOOST_MT19937,

      /// Lagged fibonacci RNG, l = 607, k = 273.
      RNG_BOOST_LAGGED_FIBONACCI607,

      /// Lagged fibonacci RNG, l = 1279, k = 418.
      RNG_BOOST_LAGGED_FIBONACCI1279,

      /// Lagged fibonacci RNG, l = 2281, k = 1252.
      RNG_BOOST_LAGGED_FIBONACCI2281,

      /// Lagged fibonacci RNG, l = 3217, k = 576.
      RNG_BOOST_LAGGED_FIBONACCI3217,

      /// Lagged fibonacci RNG, l = 4423, k = 2098.
      RNG_BOOST_LAGGED_FIBONACCI4423,

      /// Lagged fibonacci RNG, l = 9689, k = 5502.
      RNG_BOOST_LAGGED_FIBONACCI9689,

      /// Lagged fibonacci RNG, l = 19937, k = 9842.
      RNG_BOOST_LAGGED_FIBONACCI19937,

      /// Lagged fibonacci RNG, l = 23209, k = 13470.
      RNG_BOOST_LAGGED_FIBONACCI23209,

      /// Lagged fibonacci RNG, l = 44497, k = 21034.
      RNG_BOOST_LAGGED_FIBONACCI44497,

      /* The following macros are used to help define range. */
#endif

      RNG_TOTAL,
      RNG_DASSF_LOW  = RNG_DASSF_LEHMER,
      RNG_DASSF_HIGH = RNG_DASSF_MERSENNE_TWISTER,
#if PRIME_RNG_SPRNG
      RNG_SPRNG_LOW  = RNG_SPRNG_CMRG_LECU1,
      RNG_SPRNG_HIGH = RNG_SPRNG_MLFG_LAG607B,
#endif
#if PRIME_RNG_BOOST
      RNG_BOOST_LOW  = RNG_BOOST_MINSTD_RND0,
      RNG_BOOST_HIGH = RNG_BOOST_LAGGED_FIBONACCI44497,
#endif

      /// For back compatibility.
      USE_RNG_LEHMER = RNG_DASSF_LEHMER,

      /// For back compatibility.
      USE_RNG_MT = RNG_DASSF_MERSENNE_TWISTER,

#if PRIME_RNG_SPRNG
      /// Default combined multiple recursive generator.
      RNG_SPRNG_CMRG = RNG_SPRNG_CMRG_LECU1,

      /// Default 48-bit linear congruential generator with prime addend (LCG).
      RNG_SPRNG_LCG = RNG_SPRNG_LCG_CRAYLCG,

      /// Default 64-bit linear congruential generator with prime addend (LCG64).
      RNG_SPRNG_LCG64 = RNG_SPRNG_LCG64_LECU1,

      /// Default modified lagged fibonacci generator (LFG).
      RNG_SPRNG_LFG = RNG_SPRNG_LFG_LAG1279,

      /// Default multiplicative lagged fibonacci generator (MLFG),
      RNG_SPRNG_MLFG = RNG_SPRNG_MLFG_LAG17
#endif
    };

    /**
     * \brief The default constructor.
     * \param type is the random number generator type (defined in rng_type_t).
     * \param seed is the initial random seed.
     *
     * This constructor creates a random number stream started from
     * the given initial seed. The user can choose any type of the
     * random number generator defined in rng_type_t. The default is
     * the Lehmer random number generator originally used by DaSSF. If
     * the seed is omitted or zero, the random stream is seeded by the
     * system clock, in which case the user may not be able to
     * reproduce the random stream in a separate run.
     */
    Random(int type = USE_RNG_LEHMER, int seed = 0);

    /**
     * \brief The constructor that creates a random stream among multiple random streams.
     * \param type is the random number generator type (defined in rng_type_t).
     * \param idx is the index of the random stream.
     * \param n is the total number of random streams.
     * \param seed is the initial random seed.
     *
     * This constructor chooses a random stream among multiple random
     * streams. For a given random number generator type, it is
     * expected that multiple random streams provide sufficient
     * separation (i.e., small correlation) between random numbers
     * drawn from separate random streams. The user should consult the
     * particular random number generator for the maximum number of
     * random streams that can be used simultaneous and still generate
     * acceptable result. If the random seed is omitted or zero, the
     * random stream is seeded by the system clock, in which case the
     * user may not be able to reproduce the random stream in a
     * separate run. The user is expected to provide the same random
     * seed (other than zero) to start multiple random streams.
     */
    Random(int type, int idx, int n, int seed =  0);

    /// The destructor.
    ~Random();

    /// Returns the type of this random number generator.
    int type() { return rng_type; }

    /// Returns a character string name for the random number generator type.
    const char* type_string() const;

    /// Returns a random number, which is uniformly distributed between 0 and 1.
    double operator()();

    /// Set the random seed (0 means that the system will pick one using the system clock).
    void set_seed(int seed);

    /**
     * \brief Spawns more random streams from the current stream.
     * \param n is the number of random streams to be spawned.
     * \returns a list of Random objects each corresponding to a new random stream.
     *
     * The returned type is a pointer array of size n. The array shall
     * be reclaimed by the user afterwards.
     */
    Random** spawn(int n);

    /**
     * \name Continuous Random Distributions.
     * @{
     */

    /**
     * \brief Returns a random number from a uniform(a,b) distribution.
     * \param a is the lower limit.
     * \param b is the upper limit.
     *
     * The random variable has a range of a < y < b, a mean of (a+b)/2,
     * and a variance of (b-a)*(b-a)/12.
     */
    double uniform(double a = 0, double b = 1.0);

    /**
     * \brief Returns a random number from an exponential distribution.
     * \param x is the rate (i.e., 1/x is the mean).
     *
     * The range of the random variable is y > 0. The mean is
     * 1/x and the variance is 1/x^2.
     */
    double exponential(double x);

    /**
     * \brief Returns a random number from an Erlang distribution.
     * \param n is the number of stages.
     * \param x is the rate of each exponential stage (i.e., 1/x is the mean).
     *
     * Recall that Erlang distribution is the sum of iid exponential
     * distributions.  The range of the random variable is y > 0. The
     * mean is n/x and the variance is n/x^2.
     */
    double erlang(long n, double x);

    /**
     * \brief Returns a random number from a Pareto distribution.
     * \param k is the scale parameter.
     * \param a is the shape parameter.
     *
     * The probability density function is f(x) = a*k^a/x^(a+1) for x
     * >= k. The range of the random variable is y > k. The mean is
     * a*k/(a-1) a>1 and the variance a*k^2/((a-1)^2*(a-2)) for a>2.
     */
    double pareto(double k, double a);

    /**
     * \brief Returns a random number from a Normal(m, s) distribution.
     * \param m is the mean of the normal distribution.
     * \param s is the standard deviation.
     *
     * The range of the random variable is all real numbers.
     */
    double normal(double m = 0, double s = 1.0);
  
    /**
     * \brief Returns a random number from a log normal distribution.
     * \param m is the mean of the normal distribution.
     * \param s is the standard deviation of the normal distribution.
     *
     * The logarithm of a log normal random variable has a normal
     * distribution. Log normal distribution can be thought of as the
     * product of a large number of iid variables (in the same way
     * that a normal distribution is the sum of a large number of iid
     * variables).  The range of the random variable is y>0. The mean
     * is exp(m+0.5*s*s) and the variance is
     * (exp(s*s)-1)*exp(2*m+s*s).
     */
    double lognormal(double m, double s);

    /**
     * \brief Returns a random number from a chi-square distribution.
     * \param n is the degree of freedom.
     *
     * Recall that a chi-square distribution is the sum of independent
     * Normal(0,1) distributions squared. The range of this random
     * variable is y>0, with a mean n and a variance 2*n.
     */
    double chisquare(long n);

    /**
     * \brief Returns a random number from a student's t-distribution.
     * \param n is the degree of freedom.
     *
     * The range of the random variable is all real numbers. The mean
     * is 0 (n>1) and the variance n/(n-2) (n > 2).
     */
    double student(long n);

    /** @} */

    /**
     * \name Discrete Random Distributions.
     * @{
     */

    /**
     * \brief Returns a random number from a Bernoulli distribution.
     * \param p is the probability of getting a head from a coin toss.
     *
     * The range is {0,1}. The mean is p and the variance is p*(1-p).
     */
    long bernoulli(double p = 0.5);

    /**
     * \brief Chooses a random number equally likely from a set of
     * integers ranging from a to b.
     *
     * The range is {a, a+1, ..., b}. The mean is (a+b)/2 and the
     * variance is variance ((b-a+1)*(b-a+1)-1)/12.
     */
    long equilikely(long a, long b);

    /**
     * \brief Returns a random number from a Binomial distribution.
     * \param n is the number of coin tosses.
     * \param p is the probability of getting a head for a coin toss.
     *
     * The Binomial distribution is the sum of n Bernoulli
     * distributions. The range of the random variable is {0, 1, ...,
     * n}. The mean is n*p and the variance is n*p*(1-p).
     */
    long binomial(long n, double p);

    /**
     * \brief Returns a random number from a geometric distribution.
     * \param p is the probability of getting a head for a coin toss.
     *
     * The geometric distribution is the number of coin tosses before
     * a head shows up.  The range is all natural numbers {1, 2,
     * ...}. The mean is 1/p and the variance is (1-p)/(p*p).
     */
    long geometric(double p);

    /**
     * \brief Returns a random number from a Pascal distribution.
     * \param n is the number of heads we'd like to see.
     * \param p is the probability of getting a head for a coin toss.
     *
     * The Pascal distribution, also known as negative binomial
     * distribution, is the total number of tails until n heads show
     * up. That is, the probability of y=k means we give the
     * probability of n-1 heads and k failures in k+n-1 trials, and
     * success on the (k+n)th trial. The range is all natural numbers
     * {1, 2, ...}. The mean is n/p and the variance is
     * n*(1-p)/(p*p). Note that Pascal(1, p) is identical to
     * geometric(p).
     */
    long pascal(long n, double p);

    /**
     * \brief Returns a random number from a Poisson distribution.
     * \param m is the mean and the variance.
     *
     * The Poisson distribution has a range of all non-negative
     * integers {0, 1, ...}. The mean is m and the variance is also m.
     */
    long poisson(double m);

    /** @} */

    /**
     * \name Random Permutations.
     * @{
     */

    /// Returns a random permutation of n integers (from 1 to n) in a long integer array.
    long* permute(long n);

    /// Returns a random permutation of n integers (from 1 to n) using the provided long integer array.
    void permute(long* array, long n);
  
    /** @} */

    /**
     * \brief Sets the seed of all seeds.
     *
     * The seed of seeds is a number that is added to all seeds that
     * the user provides to initiate random streams. That is, one can
     * change just a single seed to effectively alter all random seeds
     * to the random number generators.
     */
    static void set_seed_of_seeds(int s) { seed_of_seeds = s; }

  private:
    Random(int t, void* k) : rng_type(t), rng_kernel(k) {}

    // type of the random number generator
    int rng_type; 

    // points to the data structure of the RNG
    void* rng_kernel;

    // a hook so that one can directly set up the seed of all seeds
    static int seed_of_seeds;

  }; // class Random

}; // namespace rng
}; // namespace prime

#endif /*__PRIME_RNG_H__*/

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
