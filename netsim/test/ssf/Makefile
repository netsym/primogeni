#get the ssf vars (which includes all of the env vars and user options)
ifneq ($(INCLUDED_INC), true)
include /Users/liux/Workspace/primogeni/netsim/Makefile.inc 
endif

# get the rules to build the ssf code
ifneq ($(INCLUDED_SSF), true)
include /Users/liux/Workspace/primogeni/netsim/src/ssf/Makefile 
endif

TEST_SSF_PATH = /Users/liux/Workspace/primogeni/netsim/test/ssf

TEST_SSF_HFILES = \
	$(SSF_HFILES) \
	$(TEST_SSF_PATH)/muxtree/muxtree.h
TEST_SSF_CFILES = \
	$(TEST_SSF_PATH)/muxtree/muxtree.cc
TEST_SSF_OFILES = $(TEST_SSF_CFILES:.cc=.oo)
TEST_SSF_EXES= $(TEST_SSF_PATH)/muxtree/muxtree

# handle source files that require source-code transformation:
#   i) run preprocessor on the source code using CXXCPP and generate _tmp0.cc;
#  ii) strip the comments from _tmp0 using SSF_CMTSTRIP and generate _tmp1.cc;
# iii) run source-to-source translation using SSF_XLATER and generate _tmp2.cc;
#  iv) compile _tmp2.cc and generate the object file
#   v) delete all temperary _tmp?.cc files
$(TEST_SSF_PATH)/%.oo:	$(TEST_SSF_PATH)/%.cc $(TEST_SSF_HFILES)
	$(CXXCPP) $(SSF_INCLUDES) $(SSF_CXXFLAGS) -o _tmp0.cc $<
	$(SSF_CMTSTRIP) < _tmp0.cc > _tmp1.cc
	$(SSF_XLATER) _tmp1.cc > _tmp2.cc
	$(CXX) $(SSF_CXXOPT) $(CXXFLAGS) -o $@ -c _tmp2.cc
	$(RMV) _tmp?.cc

$(TEST_SSF_PATH)/muxtree/muxtree: $(TEST_SSF_PATH)/muxtree/muxtree.oo
	$(CXX) $< -o $@ $(SSF_LDFLAGS) $(SSF_LIBS)

test-ssf-help:
	@ $(ECHO) "    test-ssf          :  Build SSF tests."
	@ $(ECHO) "    test-ssf-clean    :  Clean SSF tests."

test-ssf: ssf $(TEST_SSF_EXES)
test-ssf-clean:
	$(RMV) $(TEST_SSF_EXES) $(TEST_SSF_OFILES)
	$(RMV) _tmp?.cc .tmpdat* *~


INCLUDED_SSF_TEST=true
