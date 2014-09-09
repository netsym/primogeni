# TODO:
#   bugs@primessf.net
#   autoscan
# FIXME:
#   ENABLE_METIS,ENABLE_SPRNG,ENABLE_BOOSTRNG not in configure....
#

PACKAGE_NAME = primex
PACKAGE_VERSION = x1.0
PACKAGE_BUGREPORT = bugs@primessf.net

ENABLE_SSFNET_EMULATION = yes
ENABLE_SSFNET_OPENVPN = no
ENABLE_SSFNET_ROUTE_DEBUG = no
ENABLE_METIS = yes
ENABLE_SPRNG = yes
ENABLE_BOOSTRNG = no

NETSIM_DIR = /home/obaida/Desktop/pgc2vega/primex/netsim
DOC_DIR = /home/obaida/Desktop/pgc2vega/primex/netsim/../doc

CC_ORIG = gcc
CC = gcc
CXX_ORIG = g++
CXX = g++
CPP_ORIG = gcc -E
CPP = gcc -E -C
CXXCPP_ORIG = g++ -E
CXXCPP = g++ -E -C 
CFLAGS = -g -O2 
CXXFLAGS = -g -O2 
CPPFLAGS =  
#LDFLAGS = 
#LIBS = 
YACC = bison -y
YFLAGS = -dt -v
LEX = flex
LFLAGS = 
LEXLIB = -lfl
SED = /bin/sed
PERL = /usr/bin/perl
PYTHON = /usr/bin/python
AR = /usr/bin/ar
ARFLAGS = -cru
RANLIB = ranlib
LN_S = ln -s
#INSTALL = @INSTALL@
LN_S = ln -s
MKDIR_P = /bin/mkdir -p
EXEEXT = 

HAVE_BISON = yes
HAVE_FLEX = yes

PTHREAD_CFLAGS = 
PTHREAD_LIBS = -lpthread

SSF_SYNC = standalone
SSF_MACH = X86_LINUX
SSF_ARCH = GENERIC

JNI_LDFLAGS = -L/usr/lib/jvm/java-6-openjdk/jre/lib//server
JNI_CPPFLAGS =  -I/usr/lib/jvm/java-6-openjdk/include -I/usr/lib/jvm/java-6-openjdk/include/linux

JPRIME_SEPARATE_PROP_TABLE= false



MV = mv -f
CP = cp
RM = rm -f
RMR = rm -rf
ECHO = echo
CAT = cat

DOXYGEN = echo

# for some reason $(RM) changes to "echo [RM] | rm -f", we use another
# macro here to try to bypass this problem for the moment... -jason.
RMV = rm -f

INCLUDED_INC=true
