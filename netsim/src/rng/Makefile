#get all of the env vars and user options
ifneq ($(INCLUDED_INC), true)
include /home/obaida/Desktop/pgc2vega/primex/netsim/Makefile.inc 
endif

RNG_PATH = /home/obaida/Desktop/pgc2vega/primex/netsim/src/rng

ifeq ("x$(ENABLE_SPRNG)", "xyes")
SPRNG_SRCFILES = \
	$(RNG_PATH)/sprng/checkid.c \
	$(RNG_PATH)/sprng/cmrg.c \
	$(RNG_PATH)/sprng/cmrg.h \
	$(RNG_PATH)/sprng/int64.h \
	$(RNG_PATH)/sprng/interface.h \
	$(RNG_PATH)/sprng/lcg.c \
	$(RNG_PATH)/sprng/lcg.h \
	$(RNG_PATH)/sprng/lcg64.c \
	$(RNG_PATH)/sprng/lcg64.h \
	$(RNG_PATH)/sprng/lfg.c \
	$(RNG_PATH)/sprng/lfg.h \
	$(RNG_PATH)/sprng/makeseed.c \
	$(RNG_PATH)/sprng/mlfg.c \
	$(RNG_PATH)/sprng/mlfg.h \
	$(RNG_PATH)/sprng/multiply.h \
	$(RNG_PATH)/sprng/primelist_32.h \
	$(RNG_PATH)/sprng/primelist_64.h \
	$(RNG_PATH)/sprng/primes_32.c \
	$(RNG_PATH)/sprng/primes_32.h \
	$(RNG_PATH)/sprng/primes_64.c \
	$(RNG_PATH)/sprng/primes_64.h \
	$(RNG_PATH)/sprng/simple_.h \
	$(RNG_PATH)/sprng/sprng.c \
	$(RNG_PATH)/sprng/sprng.h \
	$(RNG_PATH)/sprng/store.c \
	$(RNG_PATH)/sprng/store.h
SPRNG_CFLAGS  = -DPRIME_RNG_SPRNG=1
else
SPRNG_SRCFILES =
SPRNG_CFLAGS  = -DPRIME_RNG_SPRNG=0
endif

ifeq ("x$(ENABLE_BOOSTRNG)", "xyes")
BOOSTRNG_SRCFILES = \
	$(RNG_PATH)/boost/config.hpp \
	$(RNG_PATH)/boost/cstdint.hpp \
	$(RNG_PATH)/boost/generator_iterator.hpp \
	$(RNG_PATH)/boost/integer_traits.hpp \
	$(RNG_PATH)/boost/limits.hpp \
	$(RNG_PATH)/boost/random.hpp \
	$(RNG_PATH)/boost/static_assert.hpp \
	$(RNG_PATH)/boost/config/abi_prefix.hpp \
	$(RNG_PATH)/boost/config/abi_suffix.hpp \
	$(RNG_PATH)/boost/config/auto_link.hpp \
	$(RNG_PATH)/boost/config/posix_features.hpp \
	$(RNG_PATH)/boost/config/requires_threads.hpp \
	$(RNG_PATH)/boost/config/select_compiler_config.hpp \
	$(RNG_PATH)/boost/config/select_platform_config.hpp \
	$(RNG_PATH)/boost/config/select_stdlib_config.hpp \
	$(RNG_PATH)/boost/config/suffix.hpp \
	$(RNG_PATH)/boost/config/user.hpp \
	$(RNG_PATH)/boost/config/abi/borland_prefix.hpp \
	$(RNG_PATH)/boost/config/abi/borland_suffix.hpp \
	$(RNG_PATH)/boost/config/abi/msvc_prefix.hpp \
	$(RNG_PATH)/boost/config/abi/msvc_suffix.hpp \
	$(RNG_PATH)/boost/config/compiler/borland.hpp \
	$(RNG_PATH)/boost/config/compiler/comeau.hpp \
	$(RNG_PATH)/boost/config/compiler/common_edg.hpp \
	$(RNG_PATH)/boost/config/compiler/compaq_cxx.hpp \
	$(RNG_PATH)/boost/config/compiler/digitalmars.hpp \
	$(RNG_PATH)/boost/config/compiler/gcc.hpp \
	$(RNG_PATH)/boost/config/compiler/greenhills.hpp \
	$(RNG_PATH)/boost/config/compiler/hp_acc.hpp \
	$(RNG_PATH)/boost/config/compiler/intel.hpp \
	$(RNG_PATH)/boost/config/compiler/kai.hpp \
	$(RNG_PATH)/boost/config/compiler/metrowerks.hpp \
	$(RNG_PATH)/boost/config/compiler/mpw.hpp \
	$(RNG_PATH)/boost/config/compiler/sgi_mipspro.hpp \
	$(RNG_PATH)/boost/config/compiler/sunpro_cc.hpp \
	$(RNG_PATH)/boost/config/compiler/vacpp.hpp \
	$(RNG_PATH)/boost/config/compiler/visualc.hpp \
	$(RNG_PATH)/boost/config/platform/aix.hpp \
	$(RNG_PATH)/boost/config/platform/amigaos.hpp \
	$(RNG_PATH)/boost/config/platform/beos.hpp \
	$(RNG_PATH)/boost/config/platform/bsd.hpp \
	$(RNG_PATH)/boost/config/platform/cygwin.hpp \
	$(RNG_PATH)/boost/config/platform/hpux.hpp \
	$(RNG_PATH)/boost/config/platform/irix.hpp \
	$(RNG_PATH)/boost/config/platform/linux.hpp \
	$(RNG_PATH)/boost/config/platform/macos.hpp \
	$(RNG_PATH)/boost/config/platform/solaris.hpp \
	$(RNG_PATH)/boost/config/platform/win32.hpp \
	$(RNG_PATH)/boost/config/stdlib/dinkumware.hpp \
	$(RNG_PATH)/boost/config/stdlib/libcomo.hpp \
	$(RNG_PATH)/boost/config/stdlib/libstdcpp3.hpp \
	$(RNG_PATH)/boost/config/stdlib/modena.hpp \
	$(RNG_PATH)/boost/config/stdlib/msl.hpp \
	$(RNG_PATH)/boost/config/stdlib/roguewave.hpp \
	$(RNG_PATH)/boost/config/stdlib/sgi.hpp \
	$(RNG_PATH)/boost/config/stdlib/stlport.hpp \
	$(RNG_PATH)/boost/config/stdlib/vacpp.hpp \
	$(RNG_PATH)/boost/detail/workaround.hpp \
	$(RNG_PATH)/boost/random/additive_combine.hpp \
	$(RNG_PATH)/boost/random/bernoulli_distribution.hpp \
	$(RNG_PATH)/boost/random/binomial_distribution.hpp \
	$(RNG_PATH)/boost/random/cauchy_distribution.hpp \
	$(RNG_PATH)/boost/random/discard_block.hpp \
	$(RNG_PATH)/boost/random/exponential_distribution.hpp \
	$(RNG_PATH)/boost/random/gamma_distribution.hpp \
	$(RNG_PATH)/boost/random/geometric_distribution.hpp \
	$(RNG_PATH)/boost/random/inversive_congruential.hpp \
	$(RNG_PATH)/boost/random/lagged_fibonacci.hpp \
	$(RNG_PATH)/boost/random/linear_congruential.hpp \
	$(RNG_PATH)/boost/random/linear_feedback_shift.hpp \
	$(RNG_PATH)/boost/random/lognormal_distribution.hpp \
	$(RNG_PATH)/boost/random/mersenne_twister.hpp \
	$(RNG_PATH)/boost/random/normal_distribution.hpp \
	$(RNG_PATH)/boost/random/poisson_distribution.hpp \
	$(RNG_PATH)/boost/random/random_number_generator.hpp \
	$(RNG_PATH)/boost/random/ranlux.hpp \
	$(RNG_PATH)/boost/random/shuffle_output.hpp \
	$(RNG_PATH)/boost/random/subtract_with_carry.hpp \
	$(RNG_PATH)/boost/random/triangle_distribution.hpp \
	$(RNG_PATH)/boost/random/uniform_01.hpp \
	$(RNG_PATH)/boost/random/uniform_int.hpp \
	$(RNG_PATH)/boost/random/uniform_on_sphere.hpp \
	$(RNG_PATH)/boost/random/uniform_real.hpp \
	$(RNG_PATH)/boost/random/uniform_smallint.hpp \
	$(RNG_PATH)/boost/random/variate_generator.hpp \
	$(RNG_PATH)/boost/random/xor_combine.hpp \
	$(RNG_PATH)/boost/random/detail/const_mod.hpp \
	$(RNG_PATH)/boost/random/detail/iterator_mixin.hpp \
	$(RNG_PATH)/boost/random/detail/pass_through_engine.hpp \
	$(RNG_PATH)/boost/random/detail/ptr_helper.hpp \
	$(RNG_PATH)/boost/random/detail/signed_unsigned_compare.hpp \
	$(RNG_PATH)/boost/random/detail/uniform_int_float.hpp
BOOSTRNG_CFLAGS  = -DPRIME_RNG_BOOST=1
else
BOOSTRNG_SRCFILES =
BOOSTRNG_CFLAGS  = -DPRIME_RNG_BOOST=0
endif

RNG_SRCFILES = \
	$(SPRNG_SRCFILES) \
	$(BOOSTRNG_SRCFILES) \
	$(RNG_PATH)/rng.h \
	$(RNG_PATH)/rng.cc \
	$(RNG_PATH)/rng-exception.h \
	$(RNG_PATH)/rng-exception.cc \
	$(RNG_PATH)/dassf/lehmer.h \
	$(RNG_PATH)/dassf/mersenne-twister.h

RNG_CFILES = $(filter %.c,$(RNG_SRCFILES))
RNG_CXXFILES = $(filter %.cc,$(RNG_SRCFILES))
RNG_HFILES = $(filter %.h,$(RNG_SRCFILES)) $(filter %.hpp,$(RNG_SRCFILES))
RNG_OBJFILES = $(RNG_CFILES:.c=.rng.o) $(RNG_CXXFILES:.cc=.rng.oo)
RNG_BUILTLIB = $(RNG_PATH)/librng.a
RNG_CLEANUP = $(RNG_OBJFILES) $(RNG_BUILTLIB)

RNG_CFLAGS = -DPRIME_RNG_DEBUG=0 $(SPRNG_CFLAGS) $(BOOSTRNG_CFLAGS)
RNG_CXXFLAGS = $(RNG_CFLAGS)
RNG_INCLUDES = -I$(NETSIM_DIR)/src -I$(RNG_PATH)
RNG_LDFLAGS = -L$(RNG_PATH)
RNG_LIBS = -lrng

$(RNG_PATH)/%.rng.o:	$(RNG_PATH)/%.c $(RNG_HFILES)
	$(CC_ORIG) -c $(RNG_INCLUDES) $(CFLAGS) $(RNG_CFLAGS) -o $@ $<

$(RNG_PATH)/%.rng.oo:	$(RNG_PATH)/%.cc $(RNG_HFILES)
	$(CXX_ORIG) -c $(RNG_INCLUDES) $(CXXFLAGS) $(RNG_CXXFLAGS) -o $@ $<

$(RNG_BUILTLIB):	$(RNG_OBJFILES)
	$(RMV) $(RNG_BUILTLIB)
	$(AR) $(ARFLAGS) $(RNG_BUILTLIB) $(RNG_OBJFILES)
	$(RANLIB) $(RNG_BUILTLIB)

.PHONY: test-rng test-rng-clean

rng-help:
	@ $(ECHO) "    rng               :  Build the RNG (random number generator) library."
	@ $(ECHO) "    rng-clean         :  Clean RNG."
	@ $(ECHO) "    test-rng          :  Build the test program for the RNG library."
	@ $(ECHO) "    test-rng-clean    :  Clean RNG test program."

rng:	$(RNG_BUILTLIB)

rng-clean:	test-rng-clean
	$(RMV) $(RNG_CLEANUP)

RNGTEST_PATH = $(RNG_PATH)/test
TEST_DASSF_SRCFILES = $(RNGTEST_PATH)/test_dassf.cc

$(RNGTEST_PATH)/%.o:	$(RNGTEST_PATH)/%.cc
	$(CXX_ORIG) -c $(INCLUDES) $(RNG_INCLUDES) $(CXXFLAGS) $(RNG_CXXFLAGS) -o $@ $<

$(RNGTEST_PATH)/test_dassf:	$(RNGTEST_PATH)/test_dassf.o $(RNG_BUILTLIB)
	$(CXX_ORIG) $^ -o $@ $(RNG_LDFLAGS) $(RNG_LIBS)

test-rng:	$(RNGTEST_PATH)/test_dassf
test-rng-clean:
	$(RMV) $(RNGTEST_PATH)/test_dassf $(RNGTEST_PATH)/test_dassf.o

INCLUDED_RNG=true
