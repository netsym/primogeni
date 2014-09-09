#include <stdio.h>
#include <stdlib.h>

#include "rng.h"
using namespace prime;
using namespace rng;

#ifdef ENABLE_SOFTPM
extern "C" {
#include "pdl.h"
}

struct spm_root {
  Random** rng;
};
#endif

int main(int argc, char** argv)
{
  Random** rng = new Random*[2*(Random::RNG_DASSF_HIGH-Random::RNG_DASSF_LOW+1)];

  if(argc == 3) { // the user provides the two random seeds; we create the container
    int s1 = atoi(argv[1]);
    int s2 = atoi(argv[2]);

    for(int i=Random::RNG_DASSF_LOW; i<=Random::RNG_DASSF_HIGH; i++) {
      try {
	Random* rng1 = new Random(i, s1);
	Random* rng2 = new Random(i, s2);
	rng[(i-Random::RNG_DASSF_LOW)*2] = rng1;
	rng[(i-Random::RNG_DASSF_LOW)*2+1] = rng2;
      } catch(rng_exception& e) { fprintf(stderr, "%s\n", e.what()); return 1; }
    }

#ifdef ENABLE_SOFTPM
    struct spm_root* root = (struct spm_root*)pcalloc(0, sizeof(struct spm_root));
    if(!root) {
      fprintf(stderr, "ERROR: can't allocate container.\n");
      return 1;
    }
    root->rng = rng;
    flush(0);
#endif
  } else {
#ifdef ENABLE_SOFTPM
    if(argc != 1) {
#endif
      fprintf(stderr, "Usage: %s seed1 seed2\n", argv[0]);
      return 1;
#ifdef ENABLE_SOFTPM
    }

    // without parameters, restore from container
    struct spm_root* root = (struct spm_root*)prestore(0);
    if(!root) {
      fprintf(stderr, "ERROR: can't restore container.\n");
      return 1;
    }
    rng = root->rng;
#endif
  } 

  for(int i=Random::RNG_DASSF_LOW; i<=Random::RNG_DASSF_HIGH; i++) {
    Random& rng1 = *rng[(i-Random::RNG_DASSF_LOW)*2];
    Random& rng2 = *rng[(i-Random::RNG_DASSF_LOW)*2+1];
    printf("random[%d]: %s\n", i, rng1.type_string());
    for(int j=0; j<3; j++) {
      double x1 = rng1();
      double x2 = rng2();
      double x3 = rng1.uniform(0,10);
      printf("u(0)=%lf, u(1)=%lf, unform(0,10)=%lf\n",
	     x1, x2, x3);
    }
    printf("\n");

    delete rng[(i-Random::RNG_DASSF_LOW)*2];
    delete rng[(i-Random::RNG_DASSF_LOW)*2+1];
  }
  delete[] rng;
  return 0;
}
