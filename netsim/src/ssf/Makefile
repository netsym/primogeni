#get all of the env vars and user options
ifneq ($(INCLUDED_INC), true)
include /home/liux/primogeni/netsim/Makefile.inc 
endif

SSF_PATH = /home/liux/primogeni/netsim/src/ssf
SSF_TOOL_PATH = /home/liux/primogeni/netsim/tools/ssf-tools
SSF_XLATE_PATH = /home/liux/primogeni/netsim/tools/ssf-xlate
SSF_PART_PATH = /home/liux/primogeni/netsim/tools/ssf-partition
SSF_LIBSYNK_PATH = /home/liux/primogeni/netsim/src/ssf/libsynk

ssf-doc: /home/liux/primogeni/netsim/../doc/doxy_predef.cfg /home/liux/primogeni/netsim/../doc/ssfnet.doxyfile
	$(DOXYGEN) /home/liux/primogeni/netsim/../doc/ssf.doxyfile

SSF_MAC_HFILES = \
	$(SSF_PATH)/mac/arena.h \
	$(SSF_PATH)/mac/dlmalloc.h \
	$(SSF_PATH)/mac/barriers.h \
	$(SSF_PATH)/mac/generic.h \
	$(SSF_PATH)/mac/hoard/archspec.h \
	$(SSF_PATH)/mac/hoard/block.h \
	$(SSF_PATH)/mac/hoard/config.h \
	$(SSF_PATH)/mac/hoard/heap.h \
	$(SSF_PATH)/mac/hoard/heapstats.h \
	$(SSF_PATH)/mac/hoard/processheap.h \
	$(SSF_PATH)/mac/hoard/superblock.h \
	$(SSF_PATH)/mac/hoard/threadheap.h \
	$(SSF_PATH)/mac/machines.h \
	$(SSF_PATH)/mac/memcpp.h \
	$(SSF_PATH)/mac/quickmem.h \
	$(SSF_PATH)/mac/segments.h
SSF_MAC_CFILES = \
	$(SSF_PATH)/mac/arena.cc \
	$(SSF_PATH)/mac/barriers.cc \
	$(SSF_PATH)/mac/generic.cc \
	$(SSF_PATH)/mac/hoard/archspec.cc \
	$(SSF_PATH)/mac/hoard/heap.cc \
	$(SSF_PATH)/mac/hoard/processheap.cc \
	$(SSF_PATH)/mac/hoard/superblock.cc \
	$(SSF_PATH)/mac/hoard/threadheap.cc \
	$(SSF_PATH)/mac/hoard/wrapper.cc \
	$(SSF_PATH)/mac/machines.cc \
	$(SSF_PATH)/mac/memcpp.cc \
	$(SSF_PATH)/mac/quickmem.cc \
	$(SSF_PATH)/mac/segments.cc
#	$(SSF_PATH)/mac/machdep.c

SSF_SIM_HFILES = \
	$(SSF_PATH)/sim/binheap.h \
	$(SSF_PATH)/sim/binque.h \
	$(SSF_PATH)/sim/cmdline.h \
	$(SSF_PATH)/sim/composite.h \
	$(SSF_PATH)/sim/debug.h \
	$(SSF_PATH)/sim/eventlist.h \
	$(SSF_PATH)/sim/internal.h \
	$(SSF_PATH)/sim/kernelevt.h \
	$(SSF_PATH)/sim/logiproc.h \
	$(SSF_PATH)/sim/messenger.h \
	$(SSF_PATH)/sim/procedure.h \
	$(SSF_PATH)/sim/rtqueue.h \
	$(SSF_PATH)/sim/splaytree.h \
	$(SSF_PATH)/sim/ssfmodel.h \
	$(SSF_PATH)/sim/stargate.h \
	$(SSF_PATH)/sim/teleport.h \
	$(SSF_PATH)/sim/timeline.h \
	$(SSF_PATH)/sim/universe.h
SSF_SIM_CFILES = \
	$(SSF_PATH)/sim/binheap.cc \
	$(SSF_PATH)/sim/binque.cc \
	$(SSF_PATH)/sim/cmdline.cc \
	$(SSF_PATH)/sim/debug.cc \
	$(SSF_PATH)/sim/kernelevt.cc \
	$(SSF_PATH)/sim/logiproc.cc \
	$(SSF_PATH)/sim/messenger.cc \
	$(SSF_PATH)/sim/procedure.cc \
	$(SSF_PATH)/sim/rtqueue.cc \
	$(SSF_PATH)/sim/splaytree.cc \
	$(SSF_PATH)/sim/ssfmain.cc \
	$(SSF_PATH)/sim/ssfmodel.cc \
	$(SSF_PATH)/sim/stargate.cc \
	$(SSF_PATH)/sim/teleport.cc \
	$(SSF_PATH)/sim/timeline.cc \
	$(SSF_PATH)/sim/universe.cc

SSF_API_HFILES = \
	$(SSF_PATH)/api/entity.h \
	$(SSF_PATH)/api/event.h \
	$(SSF_PATH)/api/inchannel.h \
	$(SSF_PATH)/api/outchannel.h \
	$(SSF_PATH)/api/process.h \
	$(SSF_PATH)/api/ssfcompact.h \
	$(SSF_PATH)/api/ssfenv.h \
	$(SSF_PATH)/api/ssfexception.h \
	$(SSF_PATH)/api/ssfqobj.h \
	$(SSF_PATH)/api/ssfsem.h \
	$(SSF_PATH)/api/ssftime.h \
	$(SSF_PATH)/api/ssftimer.h \
	$(SSF_PATH)/api/ssftype.h
SSF_API_CFILES = \
	$(SSF_PATH)/api/entity.cc \
	$(SSF_PATH)/api/event.cc \
	$(SSF_PATH)/api/inchannel.cc \
	$(SSF_PATH)/api/outchannel.cc \
	$(SSF_PATH)/api/process.cc \
	$(SSF_PATH)/api/ssfcompact.cc \
	$(SSF_PATH)/api/ssfenv.cc \
	$(SSF_PATH)/api/ssfexception.cc \
	$(SSF_PATH)/api/ssfqobj.cc \
	$(SSF_PATH)/api/ssfsem.cc \
	$(SSF_PATH)/api/ssftime.cc \
	$(SSF_PATH)/api/ssftimer.cc

ifeq ("x$(SSF_SYNC)", "xlibsynk")
SSF_SYNC_HFILES = \
	$(SSF_LIBSYNK_PATH)/mycompat.h \
	$(SSF_LIBSYNK_PATH)/fm.h \
	$(SSF_LIBSYNK_PATH)/fmgm.h \
	$(SSF_LIBSYNK_PATH)/fmshm.h \
	$(SSF_LIBSYNK_PATH)/fmtcp.h \
	$(SSF_LIBSYNK_PATH)/rm.h \
	$(SSF_LIBSYNK_PATH)/rmbar.h \
	$(SSF_LIBSYNK_PATH)/tm.h
SSF_SYNC_CFILES = \
	$(SSF_LIBSYNK_PATH)/mycompat.c \
	$(SSF_LIBSYNK_PATH)/fm.c \
	$(SSF_LIBSYNK_PATH)/fmgm.c \
	$(SSF_LIBSYNK_PATH)/fmshm.c \
	$(SSF_LIBSYNK_PATH)/fmtcp.c \
	$(SSF_LIBSYNK_PATH)/rm.c \
	$(SSF_LIBSYNK_PATH)/rmbar.c \
	$(SSF_LIBSYNK_PATH)/tm.c \
	$(SSF_LIBSYNK_PATH)/tmnull.c \
	$(SSF_LIBSYNK_PATH)/tmred.c
SSF_SYNC_CFLAGS = -DPRIME_SSF_DISTSIM=1 -DPRIME_SSF_SYNC_LIBSYNK=1
else
SSF_SYNC_HFILES = 
SSF_SYNC_CFILES = 
ifeq ("x$(SSF_SYNC)", "xmpi")
SSF_SYNC_CFLAGS = -DPRIME_SSF_DISTSIM=1 -DPRIME_SSF_SYNC_MPI=1 -DMPICH_IGNORE_CXX_SEEK=1
else
SSF_SYNC_CFLAGS = -DPRIME_SSF_DISTSIM=0
endif
endif

SSF_BUILTFILES = $(SSF_PATH)/mac/globals.h
SSF_HFILES = $(SSF_PATH)/ssf.h $(SSF_MAC_HFILES) $(SSF_SIM_HFILES) $(SSF_API_HFILES) \
	$(SSF_SYNC_HFILES) $(filter %.h,$(SSF_BUILTFILES))
SSF_CXXFILES = $(SSF_MAC_CFILES) $(SSF_SIM_CFILES) $(SSF_API_CFILES) \
	$(filter %.cc,$(SSF_BUILTFILES))
SSF_CFILES = $(SSF_SYNC_CFILES)
SSF_SRCFILES = $(SSF_HFILES) $(SSF_CXXFILES) $(SSF_CFILES)
SSF_OBJFILES = $(SSF_CXXFILES:.cc=.ssf.oo) $(SSF_CFILES:.c=.ssf.o)
SSF_BUILTLIB = $(SSF_PATH)/libssf.a
SSF_CLEANUP = $(SSF_BUILTFILES) $(SSF_OBJFILES) $(SSF_BUILTLIB) \
	$(SSF_PATH)/mac/globals.h.tmp .sseg

SSF_OTHER_CFLAGS = \
	-DPRIME_SSF_ARCH_$(SSF_ARCH)=1 \
	-DPRIME_SSF_MACH_$(SSF_MACH)=1 \
	$(SSF_SYNC_CFLAGS) \
	-DPRIME_SSF_LTIME_LONGLONG \
	-DPRIME_SSF_DEBUG=0 \
	-DPRIME_SSF_SCRUTINY=0 \
	-DPRIME_SSF_EMULATION=1 \
	-DPRIME_SSF_QUICKMEM=1 \
	-DPRIME_SSF_ARENA=0 \
	-DPRIME_SSF_HOARD=0 \
	-DPRIME_VERSION=\"$(PACKAGE_VERSION)\" \
	-DPRIME_SSF_BACKWARD_COMPATIBILITY=1 \
	-DPRIME_SSF_MANAGE_KERNEL_EVENTS=1 \
	-DPRIME_SSF_SHARED_MANAGEMENT=0 \
	-DPRIME_SSF_INSTRUMENT=0 \
	-DPRIME_SSF_EVTLIST_SIMULTANEOUS_EVENTS=0 \
	-DPRIME_SSF_HOMOGENEOUS=0 \
	-DPRIME_SSF_ENABLE_WARNING=1 \
	-DPRIME_SSF_REPORT_USER_TIMING=1 \
	-DPRIME_SSF_EVTLIST_BINHEAP

ifeq ("x$(ENABLE_SSFNET_EMULATION)", "xyes")
SSF_OTHER_CFLAGS += -DSSFNET_EMULATION=1
endif

SSF_CFLAGS = $(SSF_OTHER_CFLAGS) $(RNG_CFLAGS) $(DML_CFLAGS) $(PTHREAD_CFLAGS)
SSF_CXXFLAGS = $(SSF_OTHER_CFLAGS) $(RNG_CXXFLAGS) $(DML_CXXFLAGS) $(PTHREAD_CFLAGS)
SSF_INCLUDES = -I$(NETSIM_DIR)/src -I$(SSF_PATH) $(RNG_INCLUDES) $(DML_INCLUDES)
SSF_LDFLAGS = -L$(SSF_PATH) $(RNG_LDFLAGS) $(DML_LDFLAGS)
SSF_LIBS = -lssf $(RNG_LIBS) $(DML_LIBS) $(PTHREAD_LIBS)

$(SSF_PATH)/mac/globals.h:	$(SSF_CXXFILES)
	$(PERL) $(SSF_TOOL_PATH)/gensgmt.pl $(SSF_CXXFILES) -C $(CXXCPP) -F $(SSF_INCLUDES) $(SSF_CXXFLAGS) $(CXXFLAGS) > $@.tmp
	$(SSF_TOOL_PATH)/touchup $@.tmp $@

$(SSF_PATH)/%.ssf.oo:	$(SSF_PATH)/%.cc $(SSF_HFILES)
	$(CXX) -c $(SSF_INCLUDES) $(SSF_CXXFLAGS) $(CXXFLAGS) -o $@ $<

$(SSF_LIBSYNK_PATH)/%.ssf.o:	$(SSF_LIBSYNK_PATH)/%.c $(SSF_HFILES)
	$(CC_ORIG) -c -I$(SSF_LIBSYNK_PATH) -DPRIME_SSF_LTIME_LONGLONG -DGM_AVAILABLE=0 $(CFLAGS) -o $@ $<

$(SSF_BUILTLIB):	$(SSF_OBJFILES)
	$(RMV) $(SSF_BUILTLIB)
	$(AR) $(ARFLAGS) $(SSF_BUILTLIB) $(SSF_OBJFILES)
	$(RANLIB) $(SSF_BUILTLIB)

SSF_XLATE_SRCFILES = \
	$(SSF_XLATE_PATH)/cmtstrip.l \
	$(SSF_XLATE_PATH)/pre-cmtstrip.c
SSF_XLATE_BUILTFILES = $(SSF_XLATE_PATH)/cmtstrip.c
SSF_XLATE_OBJFILES = $(SSF_XLATE_BUILTFILES:.ssf.c=.o)
SSF_CMTSTRIP = $(SSF_XLATE_PATH)/cmtstrip$(EXEEXT)
SSF_XLATER = $(PERL) $(SSF_XLATE_PATH)/p4.pl
SSF_XLATE_CLEANUP = $(SSF_XLATE_BUILTFILES) $(SSF_XLATE_OBJFILES) $(SSF_CMTSTRIP) lex.yy.c

ifeq ("x$(HAVE_FLEX)", "xyes")
$(SSF_XLATE_PATH)/cmtstrip.c:	$(SSF_XLATE_PATH)/cmtstrip.l
	$(LEX) $(LFLAGS) $(SSF_XLATE_PATH)/cmtstrip.l
	mv lex.yy.c $(SSF_XLATE_PATH)/cmtstrip.c
else
$(SSF_XLATE_PATH)/cmtstrip.c:	$(SSF_XLATE_PATH)/pre-cmtstrip.c
	$(CP) $(SSF_XLATE_PATH)/pre-cmtstrip.c $(SSF_XLATE_PATH)/cmtstrip.c
endif

$(SSF_XLATE_PATH)/%.ssf.o:	$(SSF_XLATE_PATH)/%.c
	$(CC_ORIG) -c -I$(SSF_XLATE_PATH) $(CFLAGS) -o $@ $<

$(SSF_CMTSTRIP):	$(SSF_XLATE_OBJFILES)
	$(CC_ORIG) $(LDFLAGS) -o $@ $<

SSF_PART_SRCFILES = \
	$(SSF_PART_PATH)/ssfdml-machine.h \
	$(SSF_PART_PATH)/ssfdml-machine.cc \
	$(SSF_PART_PATH)/ssfdml-model.h \
	$(SSF_PART_PATH)/ssfdml-model.cc \
	$(SSF_PART_PATH)/ssfdml-runtime.h \
	$(SSF_PART_PATH)/ssfdml-runtime.cc \
	$(SSF_PART_PATH)/getopt.h \
	$(SSF_PART_PATH)/getopt.c \
	$(SSF_PART_PATH)/getopt1.c
SSF_PART_DIVIDER_SRCFILES = $(SSF_PART_PATH)/divider.cc
SSF_PART_ROBIN_SRCFILES = $(SSF_PART_PATH)/robin.cc
SSF_PART_HFILES = $(filter %.h,$(SSF_PART_SRCFILES))
SSF_PART_CXXFILES = $(filter %.cc,$(SSF_PART_SRCFILES))
SSF_PART_CFILES = $(filter %.c,$(SSF_PART_SRCFILES))
SSF_PART_OBJFILES = $(SSF_PART_CXXFILES:.cc=.ssf.oo) $(SSF_PART_CFILES:.c=.ssf.o)
SSF_PART_DIVIDER_OBJFILES = $(SSF_PART_OBJFILES) $(SSF_PART_DIVIDER_SRCFILES:.cc=.ssf.oo)
SSF_PART_ROBIN_OBJFILES = $(SSF_PART_OBJFILES) $(SSF_PART_ROBIN_SRCFILES:.cc=.ssf.oo)
ifeq ("x$(ENABLE_METIS)", "xyes")
SSF_PARTITION = $(SSF_PART_PATH)/divider$(EXEEXT)
else
SSF_PARTITION = $(SSF_PART_PATH)/robin$(EXEEXT)
endif
SSF_PART_CLEANUP = $(SSF_PART_DIVIDER_OBJFILES) $(SSF_PART_ROBIN_OBJFILES) $(SSF_PARTITION)

$(SSF_PART_PATH)/%.ssf.oo:	$(SSF_PART_PATH)/%.cc
	$(CXX_ORIG) -c -I. -I$(SSF_PART_PATH) $(METIS_INCLUDES) $(DML_INCLUDES) $(CXXFLAGS) $(METIS_CXXFLAGS) $(DML_CXXFLAGS) -o $@ $<

$(SSF_PART_PATH)/%.ssf.o:	$(SSF_PART_PATH)/%.c
	$(CC_ORIG) -c -I. -I$(SSF_PART_PATH) $(DML_INCLUDES) $(CFLAGS) $(METIS_CFLAGS) -o $@ $<

$(SSF_PART_PATH)/divider$(EXEEXT):	$(SSF_PART_DIVIDER_OBJFILES)
	$(CXX_ORIG) $(LDFLAGS) $(METIS_LDFLAGS) $(DML_LDFLAGS) $(CXXFLAGS) -o $@ $(SSF_PART_DIVIDER_OBJFILES) $(METIS_LIBS) $(DML_LIBS)

$(SSF_PART_PATH)/robin$(EXEEXT):	$(SSF_PART_ROBIN_OBJFILES)
	$(CXX_ORIG) $(LDFLAGS) $(DML_LDFLAGS) $(CXXFLAGS) -o $@ $(SSF_PART_ROBIN_OBJFILES) $(DML_LIBS)

ssf-help:
	@ $(ECHO) "    ssf               :  Build the SSF (scalable simulation framework) simulator."
	@ $(ECHO) "    ssf-clean         :  Clean SSF."

ssf:	dml rng metis $(SSF_CMTSTRIP) $(SSF_PARTITION) $(SSF_BUILTLIB)

ssf-clean:
	$(RMV) $(SSF_XLATE_CLEANUP)
	$(RMV) $(SSF_PART_CLEANUP)
	$(RMV) $(SSF_CLEANUP)

INCLUDED_SSF=true
