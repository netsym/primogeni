#get all of the env vars and user options
ifneq ($(INCLUDED_INC), true)
include /home/liux/primogeni/netsim/Makefile.inc 
endif

DML_PATH = /home/liux/primogeni/netsim/src/dml

DML_SRCFILES = \
	$(DML_PATH)/dml.h \
	$(DML_PATH)/dml.cc \
	$(DML_PATH)/dml-dictionary.h \
	$(DML_PATH)/dml-dictionary.cc \
	$(DML_PATH)/dml-locinfo.h \
	$(DML_PATH)/dml-locinfo.cc \
	$(DML_PATH)/dml-tree.h \
	$(DML_PATH)/dml-tree.cc \
	$(DML_PATH)/dml-exception.h \
	$(DML_PATH)/dml-exception.cc \
	$(DML_PATH)/dml-parse.h 

DML_LEXFILES  = \
	$(DML_PATH)/dml-lex.ll \
	$(DML_PATH)/pre-dml-lex.cc

DML_YACCFILES = \
	$(DML_PATH)/dml-syntax.yy \
	$(DML_PATH)/pre-dml-syntax.h \
	$(DML_PATH)/pre-dml-syntax.cc

DML_BUILTFILES = \
	$(DML_PATH)/dml-lex.cc \
	$(DML_PATH)/dml-syntax.h \
	$(DML_PATH)/dml-syntax.cc

DML_CCFILES = $(filter %.cc,$(DML_SRCFILES)) $(filter %.cc,$(DML_BUILTFILES))
DML_HFILES = $(filter %.h,$(DML_SRCFILES)) $(filter %.h,$(DML_BUILTFILES))
DML_OBJFILES = $(DML_CCFILES:.cc=.dml.oo)
DML_BUILTLIB = $(DML_PATH)/libdml.a
DML_CLEANUP = $(DML_BUILTFILES) $(DML_OBJFILES) $(DML_BUILTLIB) \
	lex.yy.c y.tab.h y.tab.cc y.output

DML_CFLAGS = -DPRIME_DML_DEBUG=0 -DPRIME_DML_LOCINFO=1
DML_CXXFLAGS = $(DML_CFLAGS)
DML_INCLUDES = -I$(NETSIM_DIR)/src -I$(DML_PATH)
DML_LDFLAGS = -L$(DML_PATH)
DML_LIBS = -ldml $(LEXLIB)

ifeq ("x$(HAVE_FLEX)", "xyes")
$(DML_PATH)/dml-lex.cc:	$(DML_PATH)/dml-lex.ll
	$(LEX) $(LFLAGS) $(DML_PATH)/dml-lex.ll
	$(MV) lex.yy.c $(DML_PATH)/dml-lex.cc
else
$(DML_PATH)/dml-lex.cc:	$(DML_PATH)/pre-dml-lex.cc
	$(CP) $(DML_PATH)/pre-dml-lex.cc $(DML_PATH)/dml-lex.cc
endif

ifeq ("x$(HAVE_BISON)", "xyes")
$(DML_PATH)/dml-syntax.h $(DML_PATH)/dml-syntax.cc:	$(DML_PATH)/dml-syntax.yy
	$(YACC) $(YFLAGS) $(DML_PATH)/dml-syntax.yy
	$(MV) y.tab.c $(DML_PATH)/dml-syntax.cc
	$(MV) y.tab.h $(DML_PATH)/dml-syntax.h
else
$(DML_PATH)/dml-syntax.h $(DML_PATH)/dml-syntax.cc:	$(DML_PATH)/pre-dml-syntax.h $(DML_PATH)/pre-dml-syntax.cc
	$(CP) $(DML_PATH)/pre-dml-syntax.h $(DML_PATH)/dml-syntax.h
	$(CP) $(DML_PATH)/pre-dml-syntax.cc $(DML_PATH)/dml-syntax.cc
endif

$(DML_PATH)/%.dml.oo:	$(DML_PATH)/%.cc #$(DML_HFILES) <<< XXX why not?
	$(CXX_ORIG) -c $(DML_INCLUDES) $(CXXFLAGS) $(DML_CXXFLAGS) -o $@ $<

$(DML_BUILTLIB):	$(DML_BUILTFILES) $(DML_OBJFILES)
	$(RMV) $(DML_BUILTLIB)
	$(AR) $(ARFLAGS) $(DML_BUILTLIB) $(DML_OBJFILES)
	$(RANLIB) $(DML_BUILTLIB)

.PHONY: test-dml test-dml-clean

dml-help:
	@ $(ECHO) "    dml               :  Build the DML (domain modeling language) library."
	@ $(ECHO) "    dml-clean         :  Clean DML."
	@ $(ECHO) "    test-dml          :  Build the test program for the DML library."
	@ $(ECHO) "    test-dml-clean    :  Clean DML test program."

dml:	$(DML_BUILTLIB)

dml-clean:	test-dml-clean
	$(RMV) $(DML_CLEANUP)

DMLTEST_PATH = $(DML_PATH)/test
LISTDML_SRCFILES = $(DMLTEST_PATH)/listdml.cc

$(DMLTEST_PATH)/%.o:	$(DMLTEST_PATH)/%.cc
	$(CXX_ORIG) -c $(INCLUDES) $(DML_INCLUDES) $(CXXFLAGS) $(DML_CXXFLAGS) -o $@ $<
$(DMLTEST_PATH)/listdml:	$(DMLTEST_PATH)/listdml.o $(DML_BUILTLIB)
	$(CXX_ORIG) $^ -o $@ $(DML_LDFLAGS) $(DML_LIBS)

test-dml:	$(DMLTEST_PATH)/listdml
test-dml-clean:
	$(RMV) $(DMLTEST_PATH)/listdml $(DMLTEST_PATH)/listdml.o

INCLUDED_DML=true
