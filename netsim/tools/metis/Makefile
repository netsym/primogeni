#get all of the env vars and user options
ifneq ($(INCLUDED_INC), true)
include /home/liux/primogeni/netsim/Makefile.inc 
endif 

METIS_PATH = /home/liux/primogeni/netsim/tools/metis

METIS_SRCFILES = \
	$(METIS_PATH)/balance.c \
	$(METIS_PATH)/bucketsort.c \
	$(METIS_PATH)/ccgraph.c \
	$(METIS_PATH)/coarsen.c \
	$(METIS_PATH)/compress.c \
	$(METIS_PATH)/debug.c \
	$(METIS_PATH)/defs.h \
	$(METIS_PATH)/estmem.c \
	$(METIS_PATH)/fm.c \
	$(METIS_PATH)/fortran.c \
	$(METIS_PATH)/frename.c \
	$(METIS_PATH)/graph.c \
	$(METIS_PATH)/initpart.c \
	$(METIS_PATH)/kmetis.c \
	$(METIS_PATH)/kvmetis.c \
	$(METIS_PATH)/kwayfm.c \
	$(METIS_PATH)/kwayrefine.c \
	$(METIS_PATH)/kwayvolfm.c \
	$(METIS_PATH)/kwayvolrefine.c \
	$(METIS_PATH)/macros.h \
	$(METIS_PATH)/match.c \
	$(METIS_PATH)/mbalance2.c \
	$(METIS_PATH)/mbalance.c \
	$(METIS_PATH)/mcoarsen.c \
	$(METIS_PATH)/memory.c \
	$(METIS_PATH)/mesh.c \
	$(METIS_PATH)/meshpart.c \
	$(METIS_PATH)/metis.h \
	$(METIS_PATH)/mfm2.c \
	$(METIS_PATH)/mfm.c \
	$(METIS_PATH)/mincover.c \
	$(METIS_PATH)/minitpart2.c \
	$(METIS_PATH)/minitpart.c \
	$(METIS_PATH)/mkmetis.c \
	$(METIS_PATH)/mkwayfmh.c \
	$(METIS_PATH)/mkwayrefine.c \
	$(METIS_PATH)/mmatch.c \
	$(METIS_PATH)/mmd.c \
	$(METIS_PATH)/mpmetis.c \
	$(METIS_PATH)/mrefine2.c \
	$(METIS_PATH)/mrefine.c \
	$(METIS_PATH)/mutil.c \
	$(METIS_PATH)/myqsort.c \
	$(METIS_PATH)/ometis.c \
	$(METIS_PATH)/parmetis.c \
	$(METIS_PATH)/pmetis.c \
	$(METIS_PATH)/pqueue.c \
	$(METIS_PATH)/proto.h \
	$(METIS_PATH)/refine.c \
	$(METIS_PATH)/rename.h \
	$(METIS_PATH)/separator.c \
	$(METIS_PATH)/sfm.c \
	$(METIS_PATH)/srefine.c \
	$(METIS_PATH)/stat.c \
	$(METIS_PATH)/struct.h \
	$(METIS_PATH)/subdomains.c \
	$(METIS_PATH)/timing.c \
	$(METIS_PATH)/util.c \
	$(METIS_PATH)/myrng.c \
	$(METIS_PATH)/jmetis.c

METIS_CFILES = $(filter %.c,$(METIS_SRCFILES))
METIS_HFILES = $(filter %.h,$(METIS_SRCFILES))
METIS_OBJFILES = $(METIS_CFILES:.c=.metis.o)
METIS_BUILTLIB = $(METIS_PATH)/libmetis.a
METIS_BUILTLIB_SO = $(METIS_PATH)/libmetis.so
METIS_BUILTLIB_JNILIB = $(METIS_PATH)/libmetis.jnilib
METIS_CLEANUP = $(METIS_OBJFILES) $(METIS_BUILTLIB) $(METIS_BUILTLIB_SO) $(METIS_BUILTLIB_JNILIB) 

METIS_CFLAGS = $(JNI_CPPFLAGS) $(JNI_LDFLAGS) -fpic
METIS_INCLUDES = -I$(METIS_PATH)
METIS_LDFLAGS = -L$(METIS_PATH) 
METIS_LIBS = -lmetis

$(METIS_PATH)/%.metis.o:	$(METIS_PATH)/%.c $(METIS_HFILES)
	$(CC_ORIG) -c $(METIS_INCLUDES) $(CFLAGS) $(METIS_CFLAGS) -o $@ $<

$(METIS_BUILTLIB):	$(METIS_OBJFILES)
	$(RMV) $(METIS_BUILTLIB)
	$(AR) $(ARFLAGS) $(METIS_BUILTLIB) $(METIS_OBJFILES)
	$(RANLIB) $(METIS_BUILTLIB)

$(METIS_BUILTLIB_SO):	$(METIS_OBJFILES)
	$(CC_ORIG) -shared -fpic -o $(METIS_BUILTLIB_SO) $(METIS_INCLUDES) $(CFLAGS) $(METIS_CFLAGS) $(METIS_OBJFILES)
	ln -s $(METIS_BUILTLIB_SO) $(METIS_BUILTLIB_JNILIB) 

metis-help:
	@ $(ECHO) "    metis             :  Build the METIS (graph partitioner) library."
	@ $(ECHO) "    metis-clean       :  Clean METIS."

ifeq ("x$(ENABLE_METIS)", "xyes")
metis:	$(METIS_BUILTLIB) $(METIS_BUILTLIB_SO)
metis-clean:
	$(RMV) $(METIS_CLEANUP)
else
metis:
metis-clean:
endif

INCLUDED_METIS=true
