#ifndef __PRIME_RANDOM_LEHMER_H__
#define __PRIME_RANDOM_LEHMER_H__

#include <assert.h>
#include <stdio.h>
#include <time.h>

#include "rng-exception.h"

#define STATIC_CONSTANT(type, assignment) static const type assignment

namespace prime {
namespace rng {

  template<class IntType, IntType a, IntType m, int ms, IntType js>
  class LehmerRNG {
  public:
    typedef IntType ResultType;

    STATIC_CONSTANT(IntType, multiplier = a);
    STATIC_CONSTANT(IntType, modulus = m);
    STATIC_CONSTANT(int, max_streams = ms);
    STATIC_CONSTANT(IntType, jump_multiplier = js);

    STATIC_CONSTANT(IntType, quotient = m/a);
    STATIC_CONSTANT(IntType, remainder = m%a);
    STATIC_CONSTANT(IntType, jump_quotient = m/js);
    STATIC_CONSTANT(IntType, jump_remainder = m%js);

    //STATIC_CONSTANT(double, one_over_modulus = 1.0/m);

    LehmerRNG(IntType myseed, int idx = 0, int n = 1) {
      assert(multiplier < modulus);
      assert(remainder < quotient); // must be modulus compatible

      set_seed(myseed);

      streamidx = idx; nstreams = n;
      if(streamidx < 0 || streamidx >= nstreams) {
	char errstr[256];
	sprintf(errstr, ": LehmerRNG(streamidx = %d, nstreams=%d)\n", 
		streamidx, nstreams);
	rng_throw_exception(rng_exception::illegal_argument, errstr);
	streamidx = 0; nstreams = 1;
      }
      if(nstreams > max_streams) {
	char errstr[256];
	sprintf(errstr, ": LehmerRNG too many streams (maximum=%d)\n", 
		max_streams);
	rng_throw_exception(rng_exception::illegal_argument, errstr);
	nstreams = max_streams;
      }
      
      for (int j=0; j<streamidx; j++) {
	seed = jump_multiplier*(seed%jump_quotient)-
	  jump_remainder*(seed/jump_quotient);
	if(seed <= 0) seed += modulus;
      }
    }

    double operator()() {
      seed = multiplier*(seed%quotient)-remainder*(seed/quotient);
      if(seed <= 0) seed += modulus;
      return double(seed)/modulus;
    }

    LehmerRNG** spawn(int n) {
      if(n <= 0) {
	char errstr[256];
	sprintf(errstr, ": LehmerRNG::spawn() nstreams = %d\n", n);
	rng_throw_exception(rng_exception::illegal_argument, errstr);
	return 0;
      }

      LehmerRNG** newrngs = new LehmerRNG*[n];
      assert(newrngs);
      for(int i=0; i<n; i++) {
	newrngs[i] = new LehmerRNG();
	assert(newrngs[i]);
	newrngs[i]->init_seed = newrngs[i]->seed = init_seed;
	newrngs[i]->streamidx = nstreams + nstreams*(i+1);
	if(newrngs[i]->streamidx > max_streams) {
	  fprintf(stderr, "WARNING: LehmerRNG::spawn requires more than %d streams, "
		  "cannot guarantee independent streams!\n",
		  newrngs[i]->streamidx);
	  newrngs[i]->streamidx %= max_streams;
	}
	newrngs[i]->nstreams = nstreams*(n+1); // skip a lot!!!

	for (int j=0; j<newrngs[i]->streamidx; j++) {
	  newrngs[i]->seed = jump_multiplier*(newrngs[i]->seed%jump_quotient)-
	    jump_remainder*(newrngs[i]->seed/jump_quotient);
	  if(newrngs[i]->seed <= 0) newrngs[i]->seed += modulus;
	}
      }
      return newrngs;
    } 

    void set_seed(IntType myseed) {
      if(myseed == 0) seed = (IntType)make_new_seed();
      else seed = myseed;
      if(seed <= 0) seed += modulus;
      init_seed = seed;
    }

  private:
    IntType seed;
    IntType init_seed;
    int streamidx;
    int nstreams;
    
    LehmerRNG() {} // called by spawn

    // copied from SPRNG; generate seed from local clock.
    int make_new_seed() {
      time_t tp;
      struct tm *temp;
      unsigned int temp2, temp3;
      static unsigned int temp4 = 0xe0e1;
  
      time(&tp);
      temp = localtime(&tp);
      
      temp2 = (temp->tm_sec<<26)+(temp->tm_min<<20)+(temp->tm_hour<<15)+
	(temp->tm_mday<<10)+(temp->tm_mon<<6);
      temp3 = (temp->tm_year<<13)+(temp->tm_wday<<10)+(temp->tm_yday<<1)+
	temp->tm_isdst;
      temp2 ^= clock()^temp3;
      temp4 = (temp4*0xeeee)%0xffff;
      temp2 ^= temp4<<16;
      temp4 = (temp4*0xaeee)%0xffff;
      temp2 ^= temp4;
      temp2 &= 0x7fffffff;
      return temp2;
    }
  }; // class LehmerRNG


  // a = 48271, m = 2^31-1, 256 streams (jump multiplier = 22925)
  typedef LehmerRNG<int, 48271, 2147483647, 256, 22925> lehmer32;

}; // namespace rng
}; // namespace prime

#endif /*__PRIME_RANDOM_LEHMER_H__*/
