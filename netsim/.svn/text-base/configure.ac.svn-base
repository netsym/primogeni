#
# configure.ac :- used by autoconf to produce a configure script.
#

AC_PREREQ([2.63])
AC_INIT([primex], [x1.0], [bugs@primessf.net])
AC_CONFIG_SRCDIR([src/ssf/ssf.h])
AC_CONFIG_HEADERS([src/primex_config.h])

# Checks for programs.
AC_PROG_CC
AC_PROG_CXX
AC_PROG_CPP
AC_PROG_CXXCPP
#AC_PROG_LD
AC_PROG_LEX
AC_PROG_YACC
AC_PROG_SED
AC_PATH_PROG(PERL, perl, "nonexist")
AC_PATH_PROG(PYTHON, python, "nonexist")
AC_PATH_PROG(AR, ar, "nonexist")
AC_PATH_PROG(DOXYGEN, doxygen, "echo")
#AC_PROG_INSTALL
AC_PROG_LN_S
AC_PROG_MAKE_SET
AC_PROG_RANLIB
AC_PROG_MKDIR_P

# Checks for libraries.

# Checks for header files.
AC_HEADER_ASSERT
AC_HEADER_STDBOOL
AC_HEADER_STDC
AC_HEADER_SYS_WAIT
AC_HEADER_TIME
AC_CHECK_HEADERS([arpa/inet.h fcntl.h float.h inttypes.h libintl.h limits.h malloc.h netdb.h netinet/in.h stddef.h stdint.h stdlib.h string.h strings.h sys/param.h sys/socket.h sys/time.h sys/timeb.h unistd.h wchar.h])

# Checks for typedefs, structures, and compiler characteristics.
AC_C_CONST
AC_C_INLINE
AC_C_BIGENDIAN
AC_TYPE_INT16_T
AC_TYPE_INT32_T
AC_TYPE_INT64_T
AC_TYPE_INT8_T
AC_TYPE_UINT16_T
AC_TYPE_UINT32_T
AC_TYPE_UINT64_T
AC_TYPE_UINT8_T
AC_TYPE_PID_T
AC_TYPE_SIZE_T
AC_TYPE_SSIZE_T
AC_C_VOLATILE
AC_STRUCT_TM
AC_CHECK_TYPES([ptrdiff_t])

# Checks for library functions.
AC_FUNC_ALLOCA
AC_FUNC_FORK
AC_FUNC_MALLOC
AC_FUNC_MMAP
AC_FUNC_REALLOC
AC_FUNC_SELECT_ARGTYPES
AC_TYPE_SIGNAL
AC_FUNC_STAT
AC_FUNC_VPRINTF
AC_CHECK_FUNCS([floor gethostbyname gethostname gethrtime getpagesize gettimeofday memset munmap pow putenv select socket sqrt strcasecmp strchr strdup strerror getopt getopt_long])

# check for long long type
AC_CHECK_HEADERS([inttypes.h])
AC_CHECK_TYPE([unsigned long long])
AC_CHECK_TYPE([long long])
AC_CHECK_FUNCS([atoll])
AC_MSG_CHECKING(for u_int64_t)
AC_CACHE_VAL(ac_cv_u_int64_t,
  [AC_TRY_COMPILE([#include <stdio.h>
                   #include <sys/types.h>],
                  [u_int64_t foo = 0;],
                  [ac_cv_u_int64_t=true],
                  [ac_cv_u_int64_t=false])])
if test x"$ac_cv_u_int64_t" = x"true" ; then
  AC_DEFINE([HAVE_U_INT64_T], 1, "Define if u_int64_t represents long long type")
  AC_MSG_RESULT(yes)
else
  AC_DEFINE([HAVE_U_INT64_T], 0, "Define if u_int64_t represents long long type")
  AC_MSG_RESULT(no)
fi

# machine architecture

AC_CANONICAL_HOST
#AC_DEFUN([get_host], [AC_REQUIRE([AC_CANONICAL_HOST])])
#get_host

SSF_ARCH=GENERIC

case "${host_cpu}-${host_os}" in
  # linux on x86
  i?86-linux* | i?86-pc-linux* | i?86-gnu | i?86-pc-gnu)
    SSF_MACH=X86_LINUX
    ;;

  # cygwin on x86
  i?86-cygwin*)
    SSF_MACH=X86_CYGWIN
    ;;

  # linux on 64-bit x86
  *86_64-*linux*)
    SSF_MACH=X86_64_LINUX
    ;;

  # cygwin on 64-bit x86
  i?86_64-cygwin*)
    SSF_MACH=X86_64_CYGWIN
    ;;

  # linux on ia64
  ia64-*linux*)
    SSF_MACH=IA64_LINUX
    ;;

  # intel-based mac
  i?86-darwin*)
    SSF_MACH=X86_DARWIN
    ;;

  # powerpc-based mac
  powerpc-darwin*)
    SSF_MACH=POWERPC_DARWIN
    ;;

  # ibm's aix on powerpc
  powerpc-*aix*)
    SSF_MACH=POWERPC_AIX
    ;;

  # linux on powerpc64 (blue gene)
  powerpc64-*linux*)
    SSF_MACH=POWERPC64_LINUX
    ;;

  # sun's solaris on sparc
  sparc-solaris2*)
    SSF_MACH=SPARC_SOLARIS
    ;;

  # (old) osf on alpha machines
  alpha*-osf*)
    SSF_MACH=ALPHA_OSF
    ;;

  # (old) 64-bit irix on mips
  mips-irix6.4 | mips-irix6.5)
    SSF_MACH=MIPS_IRIX64
    ;;

  # (old) 32-bit irix on mips
  mips-irix*)
    SSF_MACH=MIPS_IRIX32
    ;;

  # generic is our safe bet
  *)
    SSF_MACH=GENERIC # it's not on our short list; mostly safe to treat it as generic
    ;;
esac

## if the user does not specify GENERIC, we go with the most specific
## architecture detected above
#if test x"$SSF_ARCH" = x"UNKNOWN"; then
#   SSF_ARCH="$SSF_MACH"
#fi

AC_SUBST(SSF_ARCH)
AC_SUBST(SSF_MACH)

#check for jni lib
if test "x$SSF_MACH" = "xX86_DARWIN" -o "x$SSF_MACH" = "xPOWERPC_DARWIN"; then
  JNI_LDFLAGS="-L/System/Library/Frameworks/JavaVM.framework/Libraries/" #/Library/Java/Home/lib
else
  if test "x$JAVA_HOME" != "x" ; then
    JNI_LDFLAGS="-L$JAVA_HOME/jre/lib/$OS_ARCH/server"
  else
    JNI_LDFLAGS=""
  fi
fi
AC_SUBST(JNI_LDFLAGS)

#check for jni headers
if test "x$SSF_MACH" = "xX86_DARWIN" -o "x$SSF_MACH" = "xPOWERPC_DARWIN"; then
  JNI_CPPFLAGS="-I/System/Library/Frameworks/JavaVM.framework/Headers" #/Library/Java/Home/include
else
  JNI_CPPFLAGS=""
  if test "x$JAVA_HOME" != "x" ; then
    for dir in `find $JAVA_HOME/include -follow -type d`
    do
      JNI_CPPFLAGS="$JNI_CPPFLAGS -I$dir"
    done
  fi
fi
cppflags_bak=$CPPFLAGS
CPPFLAGS="$CPPFLAGS $JNI_CPPFLAGS"
#echo "$CPPFLAGS"
if test "x$SSF_MACH" = "xX86_DARWIN" -o "x$SSF_MACH" = "xPOWERPC_DARWIN"; then
  AC_CHECK_HEADERS([jni.h], [], AC_MSG_ERROR([Native java headers not found. Is Java Developer package installed?]))
else
  AC_CHECK_HEADERS([jni.h], [], AC_MSG_ERROR([Native java headers not found. Is \$JAVA_HOME set correctly?]))
fi
CPPFLAGS=$cppflags_bak
AC_SUBST([JNI_CPPFLAGS])

# check flex
if test "x$LEX" != "xflex"; then
  HAVE_FLEX="no"
else
  HAVE_FLEX="yes"
fi
AC_SUBST(HAVE_FLEX)

# check bison
if test "$YACC" != "bison -y"; then
  HAVE_BISON="no"
else
  HAVE_BISON="yes"
fi
AC_SUBST(HAVE_BISON)

# Check pthreads
oldlibs=$LIBS
CHECK_PTHREAD([LIBS="$LIBS $PTHREAD_LIBS" CFLAGS="$CFLAGS $PTHREAD_CFLAGS $CONFIG_CFLAGS"])
if test x"$pthread_ok" = x"yes"; then
  AC_DEFINE([HAVE_PTHREAD], 1, "Define if you have POSIX threads are supported")
  HAVE_PTHREAD="yes"
else
  AC_DEFINE([HAVE_PTHREAD], 0, "Define if you have POSIX threads are supported")
  HAVE_PTHREAD="no"
fi
AC_SUBST(HAVE_PTHREAD)
#if test x"$HAVE_PTHREAD" = x"yes"; then
#  CHECK_PTHREAD_FUNC(pthread_mutex_init, [pthread_mutex_init(0, 0)])
#  CHECK_PTHREAD_FUNC(pthread_mutex_lock, [pthread_mutex_lock(0)])
#  CHECK_PTHREAD_FUNC(pthread_mutex_unlock, [pthread_mutex_unlock(0)])
#  CHECK_PTHREAD_FUNC(pthread_mutex_trylock, [pthread_mutex_trylock(0)])
#  CHECK_PTHREAD_FUNC(pthread_key_create, [pthread_key_create(0, 0)])
#  CHECK_PTHREAD_FUNC(pthread_setspecific, [pthread_setspecific(0, 0)])
#  CHECK_PTHREAD_FUNC(pthread_kill, [pthread_kill(0, 0)])
#  CHECK_PTHREAD_FUNC(pthread_cancel, [pthread_cancel(0)])
#  CHECK_PTHREAD_FUNC(pthread_setcancelstate, [pthread_setcancelstate(0, 0)])
#  CHECK_PTHREAD_FUNC(pthread_setcanceltype, [pthread_setcanceltype(0, 0)])
#fi
LIBS=$oldlibs

# User options.

FATAL="no"

## enable metis graph partitioner for partitioning DML models
#AC_ARG_ENABLE(metis,       [  --enable-metis	  graph partitioning support],
#  ENABLE_METIS=$enableval, ENABLE_METIS="yes")
#AM_CONDITIONAL([ENABLE_METIS], [test "$ENABLE_METIS" = "yes"])

# We got rid of the serialization support as it's becoming too
# cumbersome to be included in the distribution.
#AC_ARG_ENABLE(serial,      [  --enable-serial	serialization support],
#  ENABLE_SERIAL=$enableval, ENABLE_SERIAL="no")
#AM_CONDITIONAL([ENABLE_SERIAL], [test "$ENABLE_SERIAL" = "yes"])

## enable ssfnet
#AC_ARG_ENABLE(ssfnet,       [  --enable-ssfnet	enable ssfnet],
#  ENABLE_SSFNET=$enableval, ENABLE_SSFNET="yes")
#if ! test -d "ssfnet"; then
#  ENABLE_SSFNET="no"
#fi
#AM_CONDITIONAL([ENABLE_SSFNET], [test "$ENABLE_SSFNET" = "yes"])

## rng debugging information
#AC_ARG_ENABLE(rng-debug,    [  --enable-rng-debug      enable RNG debugging information],
#  ENABLE_RNG_DEBUG=$enableval, ENABLE_RNG_DEBUG="no")
#if test "$ENABLE_RNG_DEBUG" = yes; then
#  AC_DEFINE([PRIME_RNG_DEBUG], 1, [RNG debugging information])
#else
#  AC_DEFINE([PRIME_RNG_DEBUG], 0, [RNG debugging information])
#fi
#AM_CONDITIONAL([ENABLE_RNG_DEBUG], [test x"$ENABLE_RNG_DEBUG" = x"yes"])

#AC_ARG_ENABLE(rng-sprng,    [  --enable-rng-sprng      enable SPRNG random number generators],
#  ENABLE_RNG_SPRNG=$enableval, ENABLE_RNG_SPRNG="yes")
#AM_CONDITIONAL([ENABLE_RNG_SPRNG], [test "$ENABLE_RNG_SPRNG" = "yes"])

#AC_ARG_ENABLE(rng-boost,    [  --enable-rng-boost      enable Boost random number generators],
#  ENABLE_RNG_BOOST=$enableval, ENABLE_RNG_BOOST="no")
#AM_CONDITIONAL([ENABLE_RNG_BOOST], [test "$ENABLE_RNG_BOOST" = "yes"])

## dml debugging information
#AC_ARG_ENABLE(dml-debug,    [  --enable-dml-debug      enable DML debugging information],
#  ENABLE_DML_DEBUG=$enableval, ENABLE_DML_DEBUG="no")
#if test "$ENABLE_DML_DEBUG" = yes; then
#  AC_DEFINE([PRIME_DML_DEBUG], 1, [DML debugging information])
#else
#  AC_DEFINE([PRIME_DML_DEBUG], 0, [DML debugging information])
#fi
#AM_CONDITIONAL([ENABLE_DML_DEBUG], [test x"$ENABLE_DML_DEBUG" = x"yes"])

# dml location information
#AC_ARG_ENABLE(dml-locinfo,  [  --enable-dml-locinfo    enable DML location information],
#  ENABLE_DML_LOCINFO=$enableval, ENABLE_DML_LOCINFO="yes")
#if test "$ENABLE_DML_LOCINFO" = yes; then
#  AC_DEFINE([PRIME_DML_LOCINFO], 1, [DML location information])
#else
#  AC_DEFINE([PRIME_DML_LOCINFO], 0, [DML location information])
#fi
#AM_CONDITIONAL([ENABLE_DML_LOCINFO], [test x"$ENABLE_DML_LOCINFO" = x"yes"])

#AC_ARG_ENABLE(ssf-debug,    [  --enable-ssf-debug	enable SSF debugging information],
#  ENABLE_SSF_DEBUG=$enableval, ENABLE_SSF_DEBUG="no")
#AM_CONDITIONAL([ENABLE_SSF_DEBUG], [test x"$ENABLE_SSF_DEBUG" = x"yes"])
##if test x"$ENABLE_SSF_DEBUG" = x"yes"; then
##  AC_DEFINE([PRIME_SSF_DEBUG], 1, [SSF debugging information])
##else
##  AC_DEFINE([PRIME_SSF_DEBUG], 0, [SSF debugging information])
##fi

#AC_ARG_ENABLE(ssf-scrutiny, [  --enable-ssf-scrutiny	enable SSF run-time sanity checking],
#  ENABLE_SSF_SCRUTINY=$enableval, ENABLE_SSF_SCRUTINY="no")
#AM_CONDITIONAL([ENABLE_SSF_SCRUTINY], [test x"$ENABLE_SSF_SCRUTINY" = x"yes"])
##if test x"$ENABLE_SSF_SCRUTINY" = x"yes"; then
##  AC_DEFINE([PRIME_SSF_SCRUTINY], 1, [SSF run-time sanity checking])
##else
##  AC_DEFINE([PRIME_SSF_SCRUTINY], 0, [SSF run-time sanity checking])
##fi

#AC_ARG_ENABLE(ssf-quickmem, [  --enable-ssf-quickmem	enable SSF quick memory support],
#  ENABLE_SSF_QUICKMEM=$enableval, ENABLE_SSF_QUICKMEM="yes")
#AM_CONDITIONAL([ENABLE_SSF_QUICKMEM], [test x"$ENABLE_SSF_QUICKMEM" = x"yes"])
##if test x"$ENABLE_SSF_QUICKMEM" = x"yes"; then
##  AC_DEFINE([PRIME_SSF_QUICKMEM], 1, [SSF quick memory service])
##else
##  AC_DEFINE([PRIME_SSF_QUICKMEM], 0, [SSF quick memory service])
##fi

#AC_ARG_ENABLE(ssf-hoard, [  --enable-ssf-hoard	SSF uses the Hoard memory allocator],
#  ENABLE_SSF_HOARD=$enableval, ENABLE_SSF_HOARD="yes")
#AM_CONDITIONAL([ENABLE_SSF_HOARD], [test x"$ENABLE_SSF_HOARD" = x"yes"])
##if test x"$ENABLE_SSF_HOARD" = x"yes"; then
##  AC_DEFINE([PRIME_SSF_HOARD], 1, [SSF hoard memory allocator])
##else
##  AC_DEFINE([PRIME_SSF_HOARD], 0, [SSF hoard memory allocator])
##fi

#AC_ARG_WITH(ssf-ltime,   [  --with-ssf-ltime	set the SSF simulation time type (float, double, long, or longlong)],
#  WITH_SSF_LTIME=$withval, WITH_SSF_LTIME="longlong")
#if test x"$WITH_SSF_LTIME" != x"float" -a x"$WITH_SSF_LTIME" != x"double" -a x"$WITH_SSF_LTIME" != x"long" -a x"$WITH_SSF_LTIME" != x"longlong"; then
#  AC_MSG_WARN([Unknown SSF simulation time type; I'm using longlong instead!])
#  WITH_SSF_LTIME=longlong
#fi
#AM_CONDITIONAL([SSF_LTIME_FLOAT], [test x"$WITH_SSF_LTIME" = x"float"])
#AM_CONDITIONAL([SSF_LTIME_DOUBLE], [test x"$WITH_SSF_LTIME" = x"double"])
#AM_CONDITIONAL([SSF_LTIME_LONG], [test x"$WITH_SSF_LTIME" = x"long"])
#AM_CONDITIONAL([SSF_LTIME_LONGLONG], [test x"$WITH_SSF_LTIME" = x"longlong"])
#if test x"$WITH_SSF_LTIME" = "longlong" -a x"$ac_cv_type_long_long" != x"yes"; then
#  AC_MSG_WARN([The long long type is not supported by this machine; you can't set it to be the simulation time type!])
#  FATAL="yes"
#fi

#AC_ARG_WITH(ssf-arena,  [  --with-ssf-arena	use arena for shared-memory management],
#  WITH_SSF_ARENA=$withval, WITH_SSF_ARENA="no")
#AM_CONDITIONAL([SSF_WITH_ARENA], [test x"$WITH_SSF_ARENA" = x"yes"])
##if test x"$WITH_SSF_ARENA" = x"yes"; then
##  AC_DEFINE([PRIME_WITH_ARENA], 1, [SSF uses arena for shared-memory management])
##else
##  AC_DEFINE([PRIME_WITH_ARENA], 0, [SSF uses arena for shared-memory management])
##fi

AC_ARG_WITH(ssf-sync,    [  --with-ssf-sync	SSF distributed-memory synchronization (standalone, mpi, or libsynk)],
  SSF_SYNC=$withval, SSF_SYNC="standalone")
if test x"$SSF_SYNC" != x"standalone" -a x"$SSF_SYNC" != x"mpi" -a x"$SSF_SYNC" != x"libsynk"; then
  AC_MSG_WARN([Unknown SSF synchronization method; use standalone as default!])
  SSF_SYNC="standalone"
fi
AC_SUBST(SSF_SYNC)
#AM_CONDITIONAL([SSF_SYNC_MPI], [test x"$SSF_SYNC" = x"mpi"])
#AM_CONDITIONAL([SSF_SYNC_LIBSYNK], [test x"$SSF_SYNC" = x"libsynk"])
#AM_CONDITIONAL([SSF_SYNC_STANDALONE], [test x"$SSF_SYNC" = x"standalone"])

AC_ARG_WITH(ssf-mpi-bufsiz,  [  --with-ssf-mpi-bufsiz	set the SSF MPI sender-side buffer size (in MB)],
  WITH_SSF_MPI_BUFSIZ=$withval, WITH_SSF_MPI_BUFSIZ="32")
if test "$WITH_SSF_MPI_BUFSIZ" -lt "1"; then
  AC_MSG_WARN([SSF MPI sender-size buffer size out of range; I'm using the default 32 MB instead!])
  WITH_SSF_MPI_BUFSIZ="32"
fi
AC_ARG_WITH(ssf-mpi-maxevt,  [  --with-ssf-mpi-maxevt	set the maximum size of an SSF event (in MB)],
  WITH_SSF_MPI_MAXEVT=$withval, WITH_SSF_MPI_MAXEVT="1")
if test "$WITH_SSF_MPI_MAXEVT" -lt "1"; then
  AC_MSG_WARN([SSF MPI max event size out of range; I'm using the default 1 MB instead!])
  WITH_SSF_MPI_MAXEVT="1"
fi
if test "$WITH_SSF_MPI_MAXEVT" -gt "$WITH_SSF_MPI_BUFSIZ"; then
  AC_MSG_WARN([SSF MPI sender-size buffer size must not be smaller than the max event size; I'm readjusting the buffer size!])
  WITH_SSF_MPI_BUFSIZ="$WITH_SSF_MPI_MAXEVT";
fi
AC_DEFINE_UNQUOTED([PRIME_SSF_MPI_BUFSIZ], "$WITH_SSF_MPI_BUFSIZ", [SSF MPI sender-side buffer size (in MB)])
AC_DEFINE_UNQUOTED([PRIME_SSF_MPI_MAXEVT], "$WITH_SSF_MPI_MAXEVT", [The maximum size of an SSF event (in MB)])

# find the mpi compilers
AC_ARG_WITH(mpicxx,   [  --with-mpicxx	set the MPI C++ compiler],
  WITH_MPICXX=$withval, WITH_MPICXX="nonexist")
AC_ARG_WITH(mpicc,    [  --with-mpicc	set the MPI C compiler],
  WITH_MPICC=$withval, WITH_MPICC="nonexist")

AC_ARG_ENABLE(ssfnet-emulation,             [  --disable-ssfnet-emulation            enable SSFNet emulation],
  ENABLE_SSFNET_EMULATION=$enableval, ENABLE_SSFNET_EMULATION="yes")
AC_SUBST(ENABLE_SSFNET_EMULATION)

AC_ARG_ENABLE(ssfnet-openvpn,               [  --enable-ssfnet-openvpn              enable SSFNet emulation via OpenVPN],
  ENABLE_SSFNET_OPENVPN=$enableval, ENABLE_SSFNET_OPENVPN="no")
AC_SUBST(ENABLE_SSFNET_OPENVPN)

if test x"$ENABLE_SSFNET_EMULATION" = x"no" -a x"$ENABLE_SSFNET_OPENVPN" = x"yes"; then
  AC_MSG_ERROR([OpenVPN support requires network emulation. Abort!])
fi

AC_ARG_ENABLE(ssfnet-debug,                 [  --enable-ssfnet-debug                enable SSFNet debugging information],
  ENABLE_SSFNET_DEBUG=$enableval, ENABLE_SSFNET_DEBUG="no")
if test x"$ENABLE_SSFNET_DEBUG" = x"yes" ; then
  AC_DEFINE([SSFNET_DEBUG], 1, "SSFNet debugging information")
else
  AC_DEFINE([SSFNET_DEBUG], 0, "SSFNet debugging information")
fi

AC_ARG_ENABLE(ssfnet-route-debug,           [  --enable-ssfnet-route-debug          enable SSFNet route debugging],
  ENABLE_SSFNET_ROUTE_DEBUG=$enableval, ENABLE_SSFNET_ROUTE_DEBUG="no")
if test x"$ENABLE_SSFNET_ROUTE_DEBUG" = x"yes" ; then
  AC_DEFINE([TEST_ROUTING], 1, "SSFNet route debugging")
else
  AC_DEFINE([TEST_ROUTING], 0, "SSFNet route debugging")
fi
AC_SUBST(ENABLE_SSFNET_ROUTE_DEBUG)


AC_ARG_ENABLE(jprime-separate-prop-table,   [  --enable-jprime-separate-prop-table  use a separate table to store node properties],
  ENABLE_JPRIME_SEPARATE_PROP_TABLE=$enableval, ENABLE_JPRIME_SEPARATE_PROP_TABLE="no")
if test x"$ENABLE_JPRIME_SEPARATE_PROP_TABLE" = x"yes" ; then
  JPRIME_SEPARATE_PROP_TABLE="true"
  AC_DEFINE([JPRIME_SEPARATE_PROP_TABLE], true, "jPRIME properties are stored in a separate table.")
else
  JPRIME_SEPARATE_PROP_TABLE="false"
  AC_DEFINE([JPRIME_SEPARATE_PROP_TABLE], false, "jPRIME properties are _NOT_ stored in a separate table.")
fi

AC_ARG_ENABLE(jprime-use-file-flat-db,      [  --enable-jprime-use-file-flat-db     use flat files to store models instead of RDBMS],
  ENABLE_JPRIME_USE_FLAT_FILE_DB=$enableval, ENABLE_JPRIME_USE_FLAT_FILE_DB="no")
if test x"$ENABLE_JPRIME_USE_FLAT_FILE_DB" = x"yes" ; then
  JPRIME_USE_FLAT_FILE_DB="true"
  AC_DEFINE([JPRIME_USE_FLAT_FILE_DB], true, "jPRIME uses flat files to store models instead of RDBMS.")
else
  JPRIME_USE_FLAT_FILE_DB="false"
  AC_DEFINE([JPRIME_USE_FLAT_FILE_DB], false, "jPRIME uses flat files to store models instead of RDBMS.")
fi


AC_ARG_ENABLE(jprime-debug,   [  --enable-jprime-debug  enable debugging of jprime],
  ENABLE_JPRIME_DEBUG=$enableval, ENABLE_JPRIME_DEBUG="no")
if test x"$ENABLE_JPRIME_DEBUG" = x"yes" ; then
  JPRIME_DEBUG="true"
  AC_DEFINE([JPRIME_DEBUG], true, "jPRIME debugging.")
else
  JPRIME_DEBUG="false"
  AC_DEFINE([JPRIME_DEBUG], false, "jPRIME debugging.")
fi

AC_SUBST(JPRIME_SEPARATE_PROP_TABLE)
AC_SUBST(JPRIME_USE_FLAT_FILE_DB)
AC_SUBST(JPRIME_DEBUG)


# we need to keep the original compilers
CXX_ORIG="$CXX"
CXXCPP_ORIG="$CXXCPP"
CC_ORIG="$CC"
CPP_ORIG="$CPP"

if test x"$SSF_SYNC" = x"mpi"; then
  if test x"$WITH_MPICXX" = x"nonexist"; then # if the user didn't specify
    AC_PATH_PROG(MPICXX, "mpicxx", "nonexist") # first try mpicxx
    if test x"$MPICXX" = x"nonexist"; then # if mpicxx is not present
      AC_PATH_PROG(MPICXX1, "mpiCC", "nonexist") # then try mpiCC
      MPICXX="$MPICXX1"
      if test x"$MPICXX" = x"nonexist"; then # if mpiCC is also not present, warn the user
        MPICXX="mpicxx"
	AC_MSG_WARN([Cannot locate MPI C++ compiler; MPI may not work!])
      fi
    fi
    # MPICXX must have been set by now
  else # if the user specified the compiler, use it
    MPICXX="$WITH_MPICXX"
  fi

  if test x"$WITH_MPICC" = x"nonexist"; then # if the user didn't specify
    AC_PATH_PROG(MPICC, "mpicc", "nonexist") # only try mpicc
    if test x"$MPICC" = x"nonexist"; then # if mpicc is not present, warn the user
      MPICC="mpicc"
      AC_MSG_WARN([Cannot locate MPI C compiler; MPI may not work!])
    fi
  else # if the user specified the compiler, use it
    MPICC="$WITH_MPICC"
  fi

  # replace the compilers
  CXX="$MPICXX"
  CXXCPP="$MPICXX -E"
  CC="$MPICC"
  CPP="$MPICC -E"
fi

# unless we support native threads (maybe later), pthread is a must!
if test x"$HAVE_PTHREAD" != x"yes" ; then
  AC_MSG_WARN([Missing pthreads; SSF requires pthreads to run!])
  FATAL="yes"
fi

# See if the compiler is gcc; if not, warn the user
# ... gcc makes the preprocessor preserve the comments (-C option), which
# ... is important for us to do source-code transformation.
if test x"$GCC" = x"yes"; then
  CPP="$CPP -C"
  CXXCPP="$CXXCPP -C"
else
  AC_MSG_WARN([Compiler is not GCC. SSF may not work!])
  CPP="$CPP -C"
  CXXCPP="$CXXCPP -C"
fi

# check perl
if test x"$PERL" = x"nonexist"; then
  AC_MSG_WARN([Cannot find perl. Perl is required for SSF to work!])
  FATAL="yes"
fi
AC_SUBST(PERL)

# check python
if test x"$PYTHON" = x"nonexist"; then
  AC_MSG_WARN([Cannot find python. Python is required for SSF to work!])
  FATAL="yes"
fi
AC_SUBST(PYTHON)

# check ar
if test x"$AR" = x"nonexist"; then
  AC_MSG_WARN([Cannot find ar. Ar is required for SSF to work!])
  FATAL="yes"
fi
AC_SUBST(AR)

AC_SUBST(DOXYGEN)

NETSIM_DIR=`pwd`
AC_SUBST(NETSIM_DIR)

DOC_DIR=`pwd`/../doc
AC_SUBST(DOC_DIR)

NETSCRIPT_DIR=`pwd`/../netscript
AC_SUBST(NETSCRIPT_DIR)

NETIDE_DIR=`pwd`/../netIDE
AC_SUBST(NETIDE_DIR)


# can't continue if there's a fatal error.
if test x"$FATAL" = x"yes"; then
  AC_MSG_ERROR([Fatal error(s) found during configuration; Abort!])
  exit 1
fi

if test x"$ENABLE_SSFNET_OPENVPN" = x"yes"; then
  AC_CONFIG_SUBDIRS([src/emuproxy/openvpn])
fi

AC_SUBST(CXX_ORIG)
AC_SUBST(CXXCPP_ORIG)
AC_SUBST(CC_ORIG)
AC_SUBST(CPP_ORIG)
AC_SUBST(CXX)
AC_SUBST(CXXCPP)
AC_SUBST(CC)
AC_SUBST(CPP)
AC_SUBST(CXX_FLAGS)
AC_SUBST(CXXCPP_FLAGS)
AC_SUBST(CC_FLAGS)
AC_SUBST(CPP_FLAGS)

AC_CONFIG_FILES([
	Makefile.inc
	Makefile
	../doc/Makefile
	../doc/controller.doxyfile
	../doc/jprime.doxyfile
	../doc/slingshot.doxyfile
	../doc/ssf.doxyfile
	../doc/ssfnet.doxyfile
	../netscript/build.properties
	src/dml/Makefile
	src/emuproxy/Makefile
	src/rng/Makefile
	src/ssf/Makefile
	src/ssfnet/Makefile
	test/ssf/Makefile
	test/ssfnet/Makefile
	test/ssfnet/Makefile.inc
	tools/metis/Makefile])
AC_OUTPUT

# configure.ac ends here

echo " "
echo "-------------------------------------------------------------------";
echo "PRIME SSFNET is configured successfully with the following options:";
echo " "
echo "network emulation support ............. $ENABLE_SSFNET_EMULATION";
echo "emulation with openvpn ................ $ENABLE_SSFNET_OPENVPN";
echo "debugging information ................. $ENABLE_SSFNET_DEBUG";
echo "route debugging ....................... $ENABLE_SSFNET_ROUTE_DEBUG";
echo "synchronization method ................ $SSF_SYNC";
echo "jprime debugging ...................... $JPRIME_DEBUG";
echo "jprime properties stored seperately ... $JPRIME_SEPARATE_PROP_TABLE";
echo "use flat file database for jprime ..... $JPRIME_USE_FLAT_FILE_DB";
echo "-------------------------------------------------------------------";
echo " "

#
# Copyright (c) 2007-2011 Florida International University.
#
# Permission is hereby granted, free of charge, to any individual or
# institution obtaining a copy of this software and associated
# documentation files (the "software"), to use, copy, modify, and
# distribute without restriction.
# 
# The software is provided "as is", without warranty of any kind,
# express or implied, including but not limited to the warranties of
# merchantability, fitness for a particular purpose and
# noninfringement. In no event shall Florida International University be
# liable for any claim, damages or other liability, whether in an
# action of contract, tort or otherwise, arising from, out of or in
# connection with the software or the use or other dealings in the
# software.
# 
# This software is developed and maintained by
#
#   The PRIME Research Group
#   School of Computing and Information Sciences
#   Florida International University
#   Miami, FL 33199, USA
#
# Contact Jason Liu <liux@cis.fiu.edu> for questions regarding the use
# of this software.
#

