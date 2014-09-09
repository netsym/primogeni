/*
 * random.cc :- the source file for random number/variate generator.
 */

#ifndef __PRIME_RNG_CONFIG_H__
#define __PRIME_RNG_CONFIG_H__
#include "primex_config.h"
#endif /*__PRIME_RNG_CONFIG_H__*/

#include <assert.h>
#include <stdio.h>
#include <time.h>

#if HAVE_STDLIB_H
#include <stdlib.h>
#else
#error "Missing system header file: stdlib.h"
#endif

#include "rng.h"

#include "dassf/lehmer.h"
#include "dassf/mersenne-twister.h"

#if PRIME_RNG_SPRNG
extern "C" { 
#include "sprng/sprng.h"
};
#endif

#if PRIME_RNG_BOOST
#include "boost/random.hpp"
#warning "Boost RNG library is NOT thread-safe; multithreading is not supported!"
#endif

namespace prime {
namespace rng {

int Random::seed_of_seeds = 0;

Random::Random(int type, int seed) : 
  rng_type(type)
{
  if(seed == 0) seed = (unsigned)time(0);
  else seed += seed_of_seeds;

  switch(type) {

  case RNG_DASSF_LEHMER:
    rng_kernel = new lehmer32(seed);
#if PRIME_RNG_DEBUG
    printf("\nLehmer generator: seed = %d\n\n", seed);
#endif
    break;

  case RNG_DASSF_MERSENNE_TWISTER:
    rng_kernel = new mersenne_twister(seed);
#if PRIME_RNG_DEBUG
    printf("\nMersenne twister generator: seed = %d\n\n", seed);
#endif
    break;

#if PRIME_RNG_SPRNG
  case RNG_SPRNG_CMRG_LECU1:
  case RNG_SPRNG_CMRG_LECU2:
  case RNG_SPRNG_CMRG_LECU3:
    rng_kernel = init_sprng(SPRNG_CMRG, 0, 1, seed, 
			    type-RNG_SPRNG_CMRG_LECU1);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;

  case RNG_SPRNG_LCG_CRAYLCG:
  case RNG_SPRNG_LCG_DRAND48:
  case RNG_SPRNG_LCG_FISH1:
  case RNG_SPRNG_LCG_FISH2:
  case RNG_SPRNG_LCG_FISH3:
  case RNG_SPRNG_LCG_FISH4:
  case RNG_SPRNG_LCG_FISH5:
    rng_kernel = init_sprng(SPRNG_LCG, 0, 1, seed, 
			    type-RNG_SPRNG_LCG_CRAYLCG);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;

  case RNG_SPRNG_LCG64_LECU1:
  case RNG_SPRNG_LCG64_LECU2:
  case RNG_SPRNG_LCG64_LECU3:
    rng_kernel = init_sprng(SPRNG_LCG64, 0, 1, seed, 
			    type-RNG_SPRNG_LCG64_LECU1);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;

  case RNG_SPRNG_LFG_LAG1279:
  case RNG_SPRNG_LFG_LAG17:
  case RNG_SPRNG_LFG_LAG31:
  case RNG_SPRNG_LFG_LAG55:
  case RNG_SPRNG_LFG_LAG63:
  case RNG_SPRNG_LFG_LAG127:
  case RNG_SPRNG_LFG_LAG521:
  case RNG_SPRNG_LFG_LAG521B:
  case RNG_SPRNG_LFG_LAG607:
  case RNG_SPRNG_LFG_LAG607B:
  case RNG_SPRNG_LFG_LAG1279B:
    rng_kernel = init_sprng(SPRNG_LFG, 0, 1, seed, 
			    type-RNG_SPRNG_LFG_LAG1279);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;
    
  /*
  case RNG_SPRNG_PMLCG:
    rng_kernel = init_sprng(SPRNG_PMLCG, 0, 1, seed, SPRNG_DEFAULT);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;
  */

  case RNG_SPRNG_MLFG_LAG1279:
  case RNG_SPRNG_MLFG_LAG17:
  case RNG_SPRNG_MLFG_LAG31:
  case RNG_SPRNG_MLFG_LAG55:
  case RNG_SPRNG_MLFG_LAG63:
  case RNG_SPRNG_MLFG_LAG127:
  case RNG_SPRNG_MLFG_LAG521:
  case RNG_SPRNG_MLFG_LAG521B:
  case RNG_SPRNG_MLFG_LAG607:
  case RNG_SPRNG_MLFG_LAG607B:
  case RNG_SPRNG_MLFG_LAG1279B:
    rng_kernel = init_sprng(SPRNG_MLFG, 0, 1, seed, 
			    type-RNG_SPRNG_MLFG_LAG1279);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;
#endif

#if PRIME_RNG_BOOST   
  case RNG_BOOST_MINSTD_RND0: {
    boost::minstd_rand0 rng(seed);
    rng_kernel = new boost::uniform_01<boost::minstd_rand0>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost minimum standard generator (a=16807): seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_MINSTD_RND: {
    boost::minstd_rand rng(seed);
    rng_kernel = new boost::uniform_01<boost::minstd_rand>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost minimum standard generator (a=48271): seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_RAND48: {
    boost::rand48 rng(seed);
    rng_kernel = new boost::uniform_01<boost::rand48>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost rand48 generator: seed = %d\n\n", seed);
#endif
    break;
  }
    
  case RNG_BOOST_ECUYER1988: {
    boost::ecuyer1988 rng(seed, seed);
    rng_kernel = new boost::uniform_01<boost::ecuyer1988>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Ecuyer1988 generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_KREUTZER1986: {
    boost::kreutzer1986 rng(seed);
    rng_kernel = new boost::uniform_01<boost::kreutzer1986>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Kreutzer1986 generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_HELLEKALEK1995: {
    boost::hellekalek1995 rng(seed);
    rng_kernel = new boost::uniform_01<boost::hellekalek1995>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Hellekalek1995 generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_MT11213B: {
    boost::mt11213b rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::mt11213b>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Mersenne Twister (11213b) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_MT19937: {
    boost::mt19937 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::mt19937>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Mersenne Twister (19937) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI607: {
    boost::lagged_fibonacci607 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci607>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (607) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI1279: {
    boost::lagged_fibonacci1279 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci1279>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (1279) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI2281:  {
    boost::lagged_fibonacci2281 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci2281>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (2281) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI3217: {
    boost::lagged_fibonacci3217 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci3217>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (3217) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI4423: {
    boost::lagged_fibonacci4423 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci4423>(rng); 
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (4423) generator: seed = %d\n\n", seed);
#endif
   break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI9689: {
    boost::lagged_fibonacci9689 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci9689>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (9689) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI19937: {
    boost::lagged_fibonacci19937 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci19937>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (19937) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI23209: {
    boost::lagged_fibonacci23209 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci23209>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (23209) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI44497: {
    boost::lagged_fibonacci44497 rng((unsigned)seed);
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci44497>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (44497) generator: seed = %d\n\n", seed);
#endif
    break;
  }
#endif

  default:
    fprintf(stderr, "ERROR: unknown random type %d\n", type);
    rng_kernel = 0;
    break;
  }
}

Random::Random(int type, int streamidx, int nstreams, int seed) :
  rng_type(type)
{
  if(seed == 0) seed = (unsigned)time(0);
  else seed += seed_of_seeds;

  switch(type) {

  case RNG_DASSF_LEHMER:
    rng_kernel = new lehmer32(seed, streamidx, nstreams);
#if PRIME_RNG_DEBUG
    printf("\nLehmer generator: seed = %d, stream_index = %d, num_streams = %d\n\n",
	   seed, streamidx, nstreams);
#endif
    break;

  case RNG_DASSF_MERSENNE_TWISTER:
    fprintf(stderr, "WARNING: Mersenne twister generator does not support "
	    "multiple streams, may not guarantee independence\n");
    rng_kernel = new mersenne_twister(seed*(streamidx+1));
#if PRIME_RNG_DEBUG
    printf("\nMersenne twister generator: seed = %d\n\n", seed);
#endif
    break;

#if PRIME_RNG_SPRNG
  case RNG_SPRNG_CMRG_LECU1:
  case RNG_SPRNG_CMRG_LECU2:
  case RNG_SPRNG_CMRG_LECU3:
    rng_kernel = init_sprng(SPRNG_CMRG, streamidx, nstreams, seed, 
			    type-RNG_SPRNG_CMRG_LECU1);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;

  case RNG_SPRNG_LCG_CRAYLCG:
  case RNG_SPRNG_LCG_DRAND48:
  case RNG_SPRNG_LCG_FISH1:
  case RNG_SPRNG_LCG_FISH2:
  case RNG_SPRNG_LCG_FISH3:
  case RNG_SPRNG_LCG_FISH4:
  case RNG_SPRNG_LCG_FISH5:
    rng_kernel = init_sprng(SPRNG_LCG, streamidx, nstreams, seed, 
			    type-RNG_SPRNG_LCG_CRAYLCG);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;

  case RNG_SPRNG_LCG64_LECU1:
  case RNG_SPRNG_LCG64_LECU2:
  case RNG_SPRNG_LCG64_LECU3:
    rng_kernel = init_sprng(SPRNG_LCG64, streamidx, nstreams, seed, 
			    type-RNG_SPRNG_LCG64_LECU1);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;

  case RNG_SPRNG_LFG_LAG1279:
  case RNG_SPRNG_LFG_LAG17:
  case RNG_SPRNG_LFG_LAG31:
  case RNG_SPRNG_LFG_LAG55:
  case RNG_SPRNG_LFG_LAG63:
  case RNG_SPRNG_LFG_LAG127:
  case RNG_SPRNG_LFG_LAG521:
  case RNG_SPRNG_LFG_LAG521B:
  case RNG_SPRNG_LFG_LAG607:
  case RNG_SPRNG_LFG_LAG607B:
  case RNG_SPRNG_LFG_LAG1279B:
    rng_kernel = init_sprng(SPRNG_LFG, streamidx, nstreams, seed, 
			    type-RNG_SPRNG_LFG_LAG1279);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;
    
  /*
  case RNG_SPRNG_PMLCG:
    rng_kernel = init_sprng(SPRNG_PMLCG, streamidx, nstreams, 
    seed, SPRNG_DEFAULT);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;
  */

  case RNG_SPRNG_MLFG_LAG1279:
  case RNG_SPRNG_MLFG_LAG17:
  case RNG_SPRNG_MLFG_LAG31:
  case RNG_SPRNG_MLFG_LAG55:
  case RNG_SPRNG_MLFG_LAG63:
  case RNG_SPRNG_MLFG_LAG127:
  case RNG_SPRNG_MLFG_LAG521:
  case RNG_SPRNG_MLFG_LAG521B:
  case RNG_SPRNG_MLFG_LAG607:
  case RNG_SPRNG_MLFG_LAG607B:
  case RNG_SPRNG_MLFG_LAG1279B:
    rng_kernel = init_sprng(SPRNG_MLFG, streamidx, nstreams, seed, 
			    type-RNG_SPRNG_MLFG_LAG1279);
#if PRIME_RNG_DEBUG
    print_sprng((int*)rng_kernel);
#endif
    break;
#endif
    
#if PRIME_RNG_BOOST
  case RNG_BOOST_MINSTD_RND0: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::minstd_rand0 rng(seed*(streamidx+1));
    rng_kernel = new boost::uniform_01<boost::minstd_rand0>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost minimum standard generator (a=16807): seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_MINSTD_RND: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::minstd_rand rng(seed*(streamidx+1));
    rng_kernel = new boost::uniform_01<boost::minstd_rand>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost minimum standard generator (a=48271): seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_RAND48: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::rand48 rng((int)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::rand48>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost rand48 generator: seed = %d\n\n", seed);
#endif
    break;
  }
    
  case RNG_BOOST_ECUYER1988: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::ecuyer1988 rng(seed, seed*(streamidx+1));
    rng_kernel = new boost::uniform_01<boost::ecuyer1988>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Ecuyer1988 generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_KREUTZER1986: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::kreutzer1986 rng(seed*(streamidx+1));
    rng_kernel = new boost::uniform_01<boost::kreutzer1986>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Kreutzer1986 generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_HELLEKALEK1995: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::hellekalek1995 rng(seed*(streamidx+1));
    rng_kernel = new boost::uniform_01<boost::hellekalek1995>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Hellekalek1995 generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_MT11213B: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::mt11213b rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::mt11213b>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Mersenne Twister (11213b) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_MT19937: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::mt19937 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::mt19937>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost Mersenne Twister (19937) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI607: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci607 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci607>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (607) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI1279: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci1279 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci1279>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (1279) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI2281:  {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci2281 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci2281>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (2281) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI3217: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci3217 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci3217>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (3217) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI4423: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci4423 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci4423>(rng); 
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (4423) generator: seed = %d\n\n", seed);
#endif
   break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI9689: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci9689 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci9689>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (9689) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI19937: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci19937 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci19937>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (19937) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI23209: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci23209 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci23209>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (23209) generator: seed = %d\n\n", seed);
#endif
    break;
  }

  case RNG_BOOST_LAGGED_FIBONACCI44497: {
    fprintf(stderr, "WARNING: Boost generator does not support "
	    "multiple streams, may not guarantee independence\n");
    boost::lagged_fibonacci44497 rng((unsigned)(seed*(streamidx+1)));
    rng_kernel = new boost::uniform_01<boost::lagged_fibonacci44497>(rng);
#if PRIME_RNG_DEBUG
    printf("\nBoost lagged fibonacci (44497) generator: seed = %d\n\n", seed);
#endif
    break;
  }
#endif

  default:
    fprintf(stderr, "ERROR: unknown random type %d\n", type);
    rng_kernel = 0;
    break;
  }
}

Random::~Random()
{
  switch(rng_type) {

  case RNG_DASSF_LEHMER:
    delete (lehmer32*)rng_kernel;
    break;

  case RNG_DASSF_MERSENNE_TWISTER:
    delete (mersenne_twister*)rng_kernel;
    break;

#if PRIME_RNG_SPRNG
  case RNG_SPRNG_CMRG_LECU1:
  case RNG_SPRNG_CMRG_LECU2:
  case RNG_SPRNG_CMRG_LECU3:
  case RNG_SPRNG_LCG_CRAYLCG:
  case RNG_SPRNG_LCG_DRAND48:
  case RNG_SPRNG_LCG_FISH1:
  case RNG_SPRNG_LCG_FISH2:
  case RNG_SPRNG_LCG_FISH3:
  case RNG_SPRNG_LCG_FISH4:
  case RNG_SPRNG_LCG_FISH5:
  case RNG_SPRNG_LCG64_LECU1:
  case RNG_SPRNG_LCG64_LECU2:
  case RNG_SPRNG_LCG64_LECU3:
  case RNG_SPRNG_LFG_LAG1279:
  case RNG_SPRNG_LFG_LAG17:
  case RNG_SPRNG_LFG_LAG31:
  case RNG_SPRNG_LFG_LAG55:
  case RNG_SPRNG_LFG_LAG63:
  case RNG_SPRNG_LFG_LAG127:
  case RNG_SPRNG_LFG_LAG521:
  case RNG_SPRNG_LFG_LAG521B:
  case RNG_SPRNG_LFG_LAG607:
  case RNG_SPRNG_LFG_LAG607B:
  case RNG_SPRNG_LFG_LAG1279B:
  //case RNG_SPRNG_PMLCG:
  case RNG_SPRNG_MLFG_LAG1279:
  case RNG_SPRNG_MLFG_LAG17:
  case RNG_SPRNG_MLFG_LAG31:
  case RNG_SPRNG_MLFG_LAG55:
  case RNG_SPRNG_MLFG_LAG63:
  case RNG_SPRNG_MLFG_LAG127:
  case RNG_SPRNG_MLFG_LAG521:
  case RNG_SPRNG_MLFG_LAG521B:
  case RNG_SPRNG_MLFG_LAG607:
  case RNG_SPRNG_MLFG_LAG607B:
  case RNG_SPRNG_MLFG_LAG1279B:
    free_sprng((int*)rng_kernel);
    break;
#endif

#if PRIME_RNG_BOOST
  case RNG_BOOST_MINSTD_RND0:
    delete (boost::uniform_01<boost::minstd_rand0>*)rng_kernel;
    break;

  case RNG_BOOST_MINSTD_RND:
    delete (boost::uniform_01<boost::minstd_rand>*)rng_kernel;
    break;

  case RNG_BOOST_RAND48:
    delete (boost::uniform_01<boost::rand48>*)rng_kernel;
    break;
    
  case RNG_BOOST_ECUYER1988:
    delete (boost::uniform_01<boost::ecuyer1988>*)rng_kernel;
    break;

  case RNG_BOOST_KREUTZER1986:
    delete (boost::uniform_01<boost::kreutzer1986>*)rng_kernel;
    break;

  case RNG_BOOST_HELLEKALEK1995:
    delete (boost::uniform_01<boost::hellekalek1995>*)rng_kernel;
    break;

  case RNG_BOOST_MT11213B:
    delete (boost::uniform_01<boost::mt11213b>*)rng_kernel;
    break;

  case RNG_BOOST_MT19937:
    delete (boost::uniform_01<boost::mt19937>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI607:
    delete (boost::uniform_01<boost::lagged_fibonacci607>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI1279:
    delete (boost::uniform_01<boost::lagged_fibonacci1279>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI2281:
    delete (boost::uniform_01<boost::lagged_fibonacci2281>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI3217:
    delete (boost::uniform_01<boost::lagged_fibonacci3217>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI4423:
    delete (boost::uniform_01<boost::lagged_fibonacci4423>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI9689:
    delete (boost::uniform_01<boost::lagged_fibonacci9689>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI19937:
    delete (boost::uniform_01<boost::lagged_fibonacci19937>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI23209:
    delete (boost::uniform_01<boost::lagged_fibonacci23209>*)rng_kernel;
    break;

  case RNG_BOOST_LAGGED_FIBONACCI44497:
    delete (boost::uniform_01<boost::lagged_fibonacci44497>*)rng_kernel;
    break;
#endif

  default: assert(0);
  }
}

const char* Random::type_string() const
{
  switch(rng_type) {
  case RNG_DASSF_LEHMER: return "DaSSF Lehmer generator";
  case RNG_DASSF_MERSENNE_TWISTER: return "DaSSF Mersenne Twister generator";
#if PRIME_RNG_SPRNG
  case RNG_SPRNG_CMRG_LECU1: return "SPRNG CMRG LECU1 generator";
  case RNG_SPRNG_CMRG_LECU2: return "SPRNG CMRG LECU2 generator";
  case RNG_SPRNG_CMRG_LECU3: return "SPRNG CMRG LECU3 generator";
  case RNG_SPRNG_LCG_CRAYLCG: return "SPRNG LCG CRAYLCG generator";
  case RNG_SPRNG_LCG_DRAND48: return "SPRNG LCG DRAND48 generator";
  case RNG_SPRNG_LCG_FISH1: return "SPRNG LCG FISH1 generator";
  case RNG_SPRNG_LCG_FISH2: return "SPRNG LCG FISH2 generator";
  case RNG_SPRNG_LCG_FISH3: return "SPRNG LCG FISH3 generator";
  case RNG_SPRNG_LCG_FISH4: return "SPRNG LCG FISH4 generator";
  case RNG_SPRNG_LCG_FISH5: return "SPRNG LCG FISH5 generator";
  case RNG_SPRNG_LCG64_LECU1: return "SPRNG LCG64 LECU1 generator";
  case RNG_SPRNG_LCG64_LECU2: return "SPRNG LCG64 LECU2 generator";
  case RNG_SPRNG_LCG64_LECU3: return "SPRNG LCG64 LECU3 generator";
  case RNG_SPRNG_LFG_LAG1279: return "SPRNG LFG LAG1279 generator";
  case RNG_SPRNG_LFG_LAG17: return "SPRNG LFG LAG17 generator";
  case RNG_SPRNG_LFG_LAG31: return "SPRNG LFG LAG31 generator";
  case RNG_SPRNG_LFG_LAG55: return "SPRNG LFG LAG55 generator";
  case RNG_SPRNG_LFG_LAG63: return "SPRNG LFG LAG63 generator";
  case RNG_SPRNG_LFG_LAG127: return "SPRNG LFG LAG127 generator";
  case RNG_SPRNG_LFG_LAG521: return "SPRNG LFG LAG521 generator";
  case RNG_SPRNG_LFG_LAG521B: return "SPRNG LFG LAG521B generator";
  case RNG_SPRNG_LFG_LAG607: return "SPRNG LFG LAG607 generator";
  case RNG_SPRNG_LFG_LAG607B: return "SPRNG LFG LAG607B generator";
  case RNG_SPRNG_LFG_LAG1279B: return "SPRNG LFG LAG1279B generator";
  /*case RNG_SPRNG_PMLCG: return "SPRNG PMLCG generator";*/
  case RNG_SPRNG_MLFG_LAG1279: return "SPRNG MLFG LAG1279 generator";
  case RNG_SPRNG_MLFG_LAG17: return "SPRNG MLFG LAG17 generator";
  case RNG_SPRNG_MLFG_LAG31: return "SPRNG MLFG LAG31 generator";
  case RNG_SPRNG_MLFG_LAG55: return "SPRNG MLFG LAG55 generator";
  case RNG_SPRNG_MLFG_LAG63: return "SPRNG MLFG LAG63 generator";
  case RNG_SPRNG_MLFG_LAG127: return "SPRNG MLFG LAG127 generator";
  case RNG_SPRNG_MLFG_LAG521: return "SPRNG MLFG LAG521 generator";
  case RNG_SPRNG_MLFG_LAG521B: return "SPRNG MLFG LAG521B generator";
  case RNG_SPRNG_MLFG_LAG607: return "SPRNG MLFG LAG607 generator";
  case RNG_SPRNG_MLFG_LAG607B: return "SPRNG MLFG LAG607B generator";
  case RNG_SPRNG_MLFG_LAG1279B: return "SPRNG MLFG LAG1279B generator";
#endif
#if PRIME_RNG_BOOST
  case RNG_BOOST_MINSTD_RND0: return "Boost minstd rnd0 generator";
  case RNG_BOOST_MINSTD_RND: return "Boost minstd rnd generator";
  case RNG_BOOST_RAND48: return "Boost rand48 generator";
  case RNG_BOOST_ECUYER1988: return "Boost Ecuyer 1988 generator";
  case RNG_BOOST_KREUTZER1986: return "Boost Kreutzer 1986 generator";
  case RNG_BOOST_HELLEKALEK1995: return "Boost Hellekalek 1995 generator";
  case RNG_BOOST_MT11213B: return "Boost MT 11213B generator";
  case RNG_BOOST_MT19937: return "Boost MT 19937 generator";
  case RNG_BOOST_LAGGED_FIBONACCI607: return "Boost Lagged Fibonacci 607 generator";
  case RNG_BOOST_LAGGED_FIBONACCI1279: return "Boost Lagged Fibonacci 1279 generator";
  case RNG_BOOST_LAGGED_FIBONACCI2281: return "Boost Lagged Fibonacci 2281 generator";
  case RNG_BOOST_LAGGED_FIBONACCI3217: return "Boost Lagged Fibonacci 3217 generator";
  case RNG_BOOST_LAGGED_FIBONACCI4423: return "Boost Lagged Fibonacci 4423 generator";
  case RNG_BOOST_LAGGED_FIBONACCI9689 :return "Boost Lagged Fibonacci 9698 generator";
  case RNG_BOOST_LAGGED_FIBONACCI19937: return "Boost Lagged Fibonacci 19937 generator";
  case RNG_BOOST_LAGGED_FIBONACCI23209: return "Boost Lagged Fibonacci 23209 generator";
  case RNG_BOOST_LAGGED_FIBONACCI44497: return "Boost Lagged Fibonacci 44497 generator";
#endif
  default: return "unknown random type";
  }
}

double Random::operator()()
{
  switch(rng_type) {

  case RNG_DASSF_LEHMER:
    return (*(lehmer32*)rng_kernel)();

  case RNG_DASSF_MERSENNE_TWISTER:
    return (*(mersenne_twister*)rng_kernel)();

#if PRIME_RNG_SPRNG
  case RNG_SPRNG_CMRG_LECU1:
  case RNG_SPRNG_CMRG_LECU2:
  case RNG_SPRNG_CMRG_LECU3:
  case RNG_SPRNG_LCG_CRAYLCG:
  case RNG_SPRNG_LCG_DRAND48:
  case RNG_SPRNG_LCG_FISH1:
  case RNG_SPRNG_LCG_FISH2:
  case RNG_SPRNG_LCG_FISH3:
  case RNG_SPRNG_LCG_FISH4:
  case RNG_SPRNG_LCG_FISH5:
  case RNG_SPRNG_LCG64_LECU1:
  case RNG_SPRNG_LCG64_LECU2:
  case RNG_SPRNG_LCG64_LECU3:
  case RNG_SPRNG_LFG_LAG1279:
  case RNG_SPRNG_LFG_LAG17:
  case RNG_SPRNG_LFG_LAG31:
  case RNG_SPRNG_LFG_LAG55:
  case RNG_SPRNG_LFG_LAG63:
  case RNG_SPRNG_LFG_LAG127:
  case RNG_SPRNG_LFG_LAG521:
  case RNG_SPRNG_LFG_LAG521B:
  case RNG_SPRNG_LFG_LAG607:
  case RNG_SPRNG_LFG_LAG607B:
  case RNG_SPRNG_LFG_LAG1279B:
  //case RNG_SPRNG_PMLCG:
  case RNG_SPRNG_MLFG_LAG1279:
  case RNG_SPRNG_MLFG_LAG17:
  case RNG_SPRNG_MLFG_LAG31:
  case RNG_SPRNG_MLFG_LAG55:
  case RNG_SPRNG_MLFG_LAG63:
  case RNG_SPRNG_MLFG_LAG127:
  case RNG_SPRNG_MLFG_LAG521:
  case RNG_SPRNG_MLFG_LAG521B:
  case RNG_SPRNG_MLFG_LAG607:
  case RNG_SPRNG_MLFG_LAG607B:
  case RNG_SPRNG_MLFG_LAG1279B:
    return sprng((int*)rng_kernel);
#endif

#if PRIME_RNG_BOOST
  case RNG_BOOST_MINSTD_RND0:
    return (*(boost::uniform_01<boost::minstd_rand0>*)rng_kernel)();

  case RNG_BOOST_MINSTD_RND:
    return (*(boost::uniform_01<boost::minstd_rand>*)rng_kernel)();

  case RNG_BOOST_RAND48:
    return (*(boost::uniform_01<boost::rand48>*)rng_kernel)();
    
  case RNG_BOOST_ECUYER1988:
    return (*(boost::uniform_01<boost::ecuyer1988>*)rng_kernel)();

  case RNG_BOOST_KREUTZER1986:
    return (*(boost::uniform_01<boost::kreutzer1986>*)rng_kernel)();

  case RNG_BOOST_HELLEKALEK1995:
    return (*(boost::uniform_01<boost::hellekalek1995>*)rng_kernel)();

  case RNG_BOOST_MT11213B:
    return (*(boost::uniform_01<boost::mt11213b>*)rng_kernel)();

  case RNG_BOOST_MT19937:
    return (*(boost::uniform_01<boost::mt19937>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI607:
    return (*(boost::uniform_01<boost::lagged_fibonacci607>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI1279:
    return (*(boost::uniform_01<boost::lagged_fibonacci1279>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI2281:
    return (*(boost::uniform_01<boost::lagged_fibonacci2281>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI3217:
    return (*(boost::uniform_01<boost::lagged_fibonacci3217>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI4423:
    return (*(boost::uniform_01<boost::lagged_fibonacci4423>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI9689:
    return (*(boost::uniform_01<boost::lagged_fibonacci9689>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI19937:
    return (*(boost::uniform_01<boost::lagged_fibonacci19937>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI23209:
    return (*(boost::uniform_01<boost::lagged_fibonacci23209>*)rng_kernel)();

  case RNG_BOOST_LAGGED_FIBONACCI44497:
    return (*(boost::uniform_01<boost::lagged_fibonacci44497>*)rng_kernel)();
#endif

  default: assert(0);
  }

  return 0;
}

void Random::set_seed(int seed)
{
  if(seed == 0) seed = (unsigned)time(0);
  else seed += seed_of_seeds;

  switch(rng_type) {

  case RNG_DASSF_LEHMER:
    ((lehmer32*)rng_kernel)->set_seed(seed);
    break;

  case RNG_DASSF_MERSENNE_TWISTER:
    ((mersenne_twister*)rng_kernel)->seed(seed);
    break;

#if PRIME_RNG_SPRNG
  case RNG_SPRNG_CMRG_LECU1:
  case RNG_SPRNG_CMRG_LECU2:
  case RNG_SPRNG_CMRG_LECU3:
  case RNG_SPRNG_LCG_CRAYLCG:
  case RNG_SPRNG_LCG_DRAND48:
  case RNG_SPRNG_LCG_FISH1:
  case RNG_SPRNG_LCG_FISH2:
  case RNG_SPRNG_LCG_FISH3:
  case RNG_SPRNG_LCG_FISH4:
  case RNG_SPRNG_LCG_FISH5:
  case RNG_SPRNG_LCG64_LECU1:
  case RNG_SPRNG_LCG64_LECU2:
  case RNG_SPRNG_LCG64_LECU3:
  case RNG_SPRNG_LFG_LAG1279:
  case RNG_SPRNG_LFG_LAG17:
  case RNG_SPRNG_LFG_LAG31:
  case RNG_SPRNG_LFG_LAG55:
  case RNG_SPRNG_LFG_LAG63:
  case RNG_SPRNG_LFG_LAG127:
  case RNG_SPRNG_LFG_LAG521:
  case RNG_SPRNG_LFG_LAG521B:
  case RNG_SPRNG_LFG_LAG607:
  case RNG_SPRNG_LFG_LAG607B:
  case RNG_SPRNG_LFG_LAG1279B:
  //case RNG_SPRNG_PMLCG:
  case RNG_SPRNG_MLFG_LAG1279:
  case RNG_SPRNG_MLFG_LAG17:
  case RNG_SPRNG_MLFG_LAG31:
  case RNG_SPRNG_MLFG_LAG55:
  case RNG_SPRNG_MLFG_LAG63:
  case RNG_SPRNG_MLFG_LAG127:
  case RNG_SPRNG_MLFG_LAG521:
  case RNG_SPRNG_MLFG_LAG521B:
  case RNG_SPRNG_MLFG_LAG607:
  case RNG_SPRNG_MLFG_LAG607B:
  case RNG_SPRNG_MLFG_LAG1279B:
    fprintf(stderr, "WARNING: SPRNG does not support reseeding "
	    "the random streams, ignored\n");
    break;
#endif

#if PRIME_RNG_BOOST
  case RNG_BOOST_MINSTD_RND0:
    ((boost::uniform_01<boost::minstd_rand0>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_MINSTD_RND:
    ((boost::uniform_01<boost::minstd_rand>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_RAND48:
    ((boost::uniform_01<boost::rand48>*)rng_kernel)->
      base().seed(seed);
    break;
    
  case RNG_BOOST_ECUYER1988:
    ((boost::uniform_01<boost::ecuyer1988>*)rng_kernel)->
      base().seed(seed, seed);
    break;

  case RNG_BOOST_KREUTZER1986:
    ((boost::uniform_01<boost::kreutzer1986>*)rng_kernel)->
      base().seed(seed);
    break;

  case RNG_BOOST_HELLEKALEK1995:
    ((boost::uniform_01<boost::hellekalek1995>*)rng_kernel)->
      base().seed(seed);
    break;

  case RNG_BOOST_MT11213B:
    ((boost::uniform_01<boost::mt11213b>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_MT19937:
    ((boost::uniform_01<boost::mt19937>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI607:
    ((boost::uniform_01<boost::lagged_fibonacci607>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI1279:
    ((boost::uniform_01<boost::lagged_fibonacci1279>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI2281:
    ((boost::uniform_01<boost::lagged_fibonacci2281>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI3217:
    ((boost::uniform_01<boost::lagged_fibonacci3217>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI4423:
    ((boost::uniform_01<boost::lagged_fibonacci4423>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI9689:
    ((boost::uniform_01<boost::lagged_fibonacci9689>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI19937:
    ((boost::uniform_01<boost::lagged_fibonacci19937>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI23209:
    ((boost::uniform_01<boost::lagged_fibonacci23209>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;

  case RNG_BOOST_LAGGED_FIBONACCI44497:
    ((boost::uniform_01<boost::lagged_fibonacci44497>*)rng_kernel)->
      base().seed((unsigned)seed);
    break;
#endif

  default: assert(0);
  }
}

Random** Random::spawn(int n)
{
  if(n <= 0) return 0;
  Random** rngs = new Random*[n]; assert(rngs);

  switch(rng_type) {

  case RNG_DASSF_LEHMER: {
    lehmer32** r = ((lehmer32*)rng_kernel)->spawn(n);
    if(!r) { delete[] rngs; return 0; }
    for(int i=0; i<n; i++)
      rngs[i] = new Random(rng_type, (void*)r[i]);
    delete[] r;
    return rngs;
  }

  case RNG_DASSF_MERSENNE_TWISTER: {
    fprintf(stderr, "WARNING: Mersenne twister does not support spawning, "
	    "may not guarantee independence\n");
    // may be this is stupid!
    for(int i=0; i<n; i++) {
      rngs[i] = new Random(rng_type, (long)((*this)()*0x07fff0000));
    }
    return rngs;
  }

#if PRIME_RNG_SPRNG
  case RNG_SPRNG_CMRG_LECU1:
  case RNG_SPRNG_CMRG_LECU2:
  case RNG_SPRNG_CMRG_LECU3:
  case RNG_SPRNG_LCG_CRAYLCG:
  case RNG_SPRNG_LCG_DRAND48:
  case RNG_SPRNG_LCG_FISH1:
  case RNG_SPRNG_LCG_FISH2:
  case RNG_SPRNG_LCG_FISH3:
  case RNG_SPRNG_LCG_FISH4:
  case RNG_SPRNG_LCG_FISH5:
  case RNG_SPRNG_LCG64_LECU1:
  case RNG_SPRNG_LCG64_LECU2:
  case RNG_SPRNG_LCG64_LECU3:
  case RNG_SPRNG_LFG_LAG1279:
  case RNG_SPRNG_LFG_LAG17:
  case RNG_SPRNG_LFG_LAG31:
  case RNG_SPRNG_LFG_LAG55:
  case RNG_SPRNG_LFG_LAG63:
  case RNG_SPRNG_LFG_LAG127:
  case RNG_SPRNG_LFG_LAG521:
  case RNG_SPRNG_LFG_LAG521B:
  case RNG_SPRNG_LFG_LAG607:
  case RNG_SPRNG_LFG_LAG607B:
  case RNG_SPRNG_LFG_LAG1279B:
  //case RNG_SPRNG_PMLCG:
  case RNG_SPRNG_MLFG_LAG1279:
  case RNG_SPRNG_MLFG_LAG17:
  case RNG_SPRNG_MLFG_LAG31:
  case RNG_SPRNG_MLFG_LAG55:
  case RNG_SPRNG_MLFG_LAG63:
  case RNG_SPRNG_MLFG_LAG127:
  case RNG_SPRNG_MLFG_LAG521:
  case RNG_SPRNG_MLFG_LAG521B:
  case RNG_SPRNG_MLFG_LAG607:
  case RNG_SPRNG_MLFG_LAG607B:
  case RNG_SPRNG_MLFG_LAG1279B: {
    int** r;
    int rn = spawn_sprng((int*)rng_kernel, n, &r);
    if(rn != n) {
      for(int i=0; i<rn; i++) free_sprng(r[i]);
      free(r);
      rng_throw_exception(rng_exception::spawn_error);
      return 0;
    }
    for(int i=0; i<n; i++) 
      rngs[i] = new Random(rng_type, r[i]);
    free(r);
    return rngs;
  }
#endif

#if PRIME_RNG_BOOST
  case RNG_BOOST_MINSTD_RND0:
  case RNG_BOOST_MINSTD_RND:
  case RNG_BOOST_RAND48:
  case RNG_BOOST_ECUYER1988:
  case RNG_BOOST_KREUTZER1986:
  case RNG_BOOST_HELLEKALEK1995:
  case RNG_BOOST_MT11213B:
  case RNG_BOOST_MT19937:
  case RNG_BOOST_LAGGED_FIBONACCI607:
  case RNG_BOOST_LAGGED_FIBONACCI1279:
  case RNG_BOOST_LAGGED_FIBONACCI2281:
  case RNG_BOOST_LAGGED_FIBONACCI3217:
  case RNG_BOOST_LAGGED_FIBONACCI4423:
  case RNG_BOOST_LAGGED_FIBONACCI9689:
  case RNG_BOOST_LAGGED_FIBONACCI19937:
  case RNG_BOOST_LAGGED_FIBONACCI23209:
  case RNG_BOOST_LAGGED_FIBONACCI44497: {
    fprintf(stderr, "WARNING: Boost does not support spawning, "
	    "may not guarantee independence\n");
    // may be this is stupid!
    for(int i=0; i<n; i++) {
      rngs[i] = new Random(rng_type, (long)((*this)()*0x07fff0000));
    }
    return rngs;
  }
#endif

  default: assert(0);
  }
  return 0;
}

double Random::uniform(double a, double b)
{
  if(a > b) {
    char errstr[256];
    sprintf(errstr, ": uniform(%lg,%lg)", a, b);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  if(a == b) return a;
  else return a+(b-a)*(*this)();
}

double Random::exponential(double x)
{
  if(x <= 0) {
    char errstr[256];
    sprintf(errstr, ": exponential(%lg)", x);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  double result = -1.0/x*log(1.0-(*this)());
  return result;
}

double Random::erlang(long n, double x)
{  
  if(n <= 0 || x <= 0) {
    char errstr[256];
    sprintf(errstr, ": erlang(%ld,%lg)", n, x);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  double y = 0.0;
  for(long i=0; i<n; i++) y += exponential(x);
  return y;
}

double Random::pareto(double k, double a)
{
  if(k <= 0 || a <= 0) {
    char errstr[256];
    sprintf(errstr, ": pareto(%lg,%lg)", k, a);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  return k*pow(1.0-(*this)(), -1.0/a);
}

double Random::normal(double m, double s)
{
  if(s <= 0) {
    char errstr[256];
    sprintf(errstr, ": normal(%lg,%lg)", m, s);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  /* Uses a very accurate approximation of the normal idf due to Odeh & Evans, */
  /* J. Applied Statistics, 1974, vol 23, pp 96-97.                            */
  double p0 = 0.322232431088;        double q0 = 0.099348462606;
  double p1 = 1.0;                   double q1 = 0.588581570495;
  double p2 = 0.342242088547;        double q2 = 0.531103462366;
  double p3 = 0.204231210245e-1;     double q3 = 0.103537752850;
  double p4 = 0.453642210148e-4;     double q4 = 0.385607006340e-2;
  double u, t, p, q, z;

  u = (*this)();
  if(u < 0.5) t = sqrt(-2.0*log(u));
  else t = sqrt(-2.0*log(1.0-u));
  p = p0+t*(p1+t*(p2+t*(p3+t*p4)));
  q = q0+t*(q1+t*(q2+t*(q3+t*q4)));
  if(u < 0.5) z = (p/q)-t;
  else z = t-(p/q);

  double result = m+s*z;
  return result;
}

double Random::lognormal(double a, double b)
{
  if(b <= 0) {
    char errstr[256];
    sprintf(errstr, ": lognormal(%lg,%lg)", a, b);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  double result = exp(a+b*normal(0.0, 1.0));
  return result;
}

double Random::chisquare(long n)
{ 
  if(n <= 0) {
    char errstr[256];
    sprintf(errstr, ": chisquare(%ld)", n);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  double y = 0.0;
  for(long i=0; i<n; i++) {
    double z = normal(0.0, 1.0);
    y += z*z;
  }
  return y;
}

double Random::student(long n)
{
  if(n <= 0) {
    char errstr[256];
    sprintf(errstr, ": student(%ld)", n);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  double result = normal(0.0, 1.0)/sqrt(chisquare(n)/n);
  return result;
}

long Random::bernoulli(double p)
{
  if(p < 0 || p > 1) {
    char errstr[256];
    sprintf(errstr, ": bernoulli(%lg)", p);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  long result = (*this)()<p ? 1 : 0;
  return result;
}

long Random::equilikely(long a, long b)
{
  if(a > b) {
    char errstr[256];
    sprintf(errstr, ": equiliely(%ld,%ld)", a, b);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  double x = (*this)();
  long v = a+(long)floor((b-a+1)*x);
  return v;
}

long Random::binomial(long n, double p)
{ 
  if(n <= 0 || p < 0 || p > 1) {
    char errstr[256];
    sprintf(errstr, ": binomial(%ld,%lg)", n, p);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  long y = 0;
  for(long i=0; i<n; i++) y += bernoulli(p);
  return y;
}

long Random::geometric(double p)
{
  if(p <= 0 || p >= 1) {
    char errstr[256];
    sprintf(errstr, ": geometric(%lg)", p);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  long result = 1+(long)(log(1.0-(*this)())/log(1.0-p));
  return result;
}

long Random::pascal(long n, double p)
{ 
  if(n <= 0 || p <= 0 || p >= 1) {
    char errstr[256];
    sprintf(errstr, ": pascal(%ld,%lg)", n, p);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  long y = 0;
  for(long i=0; i<n; i++) y += geometric(p);
  return y;
}

long Random::poisson(double m)
{ 
  if(m <= 0) {
    char errstr[256];
    sprintf(errstr, ": poisson(%lg)", m);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return -1;
  }

  long y = 0;
  double t = exp(m);
  do {
    y++;
    t *= (*this)();
  } while(t >= 1.0);
  y--;
  return y;
}

long* Random::permute(long n)
{
  if(n <= 0) {
    char errstr[256];
    sprintf(errstr, ": permute(%ld)", n);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return 0;
  }

  long *array = new long[n]; assert(array);
  permute(array, n);
  return array;
}

void Random::permute(long* array, long n)
{
  if(!array || n <= 0) {
    char errstr[256];
    sprintf(errstr, ": permute(%p,%ld)", array, n);
    rng_throw_exception(rng_exception::illegal_argument, errstr);
    return;
  }

  int i;
  for(i=0; i<n; i++) array[i] = i;
  for(i=0; i<n-1; i++) {
    long j = equilikely(i, n-1);
    if(i != j) {
      long tmp = array[i];
      array[i] = array[j];
      array[j] = tmp;
    }
  }
}

}; // namespace rng
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

/*
 * $Id$
 */
