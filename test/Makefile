CWD=$(realpath .)
ROOT=$(realpath ./../)
NETSIM_DIR=${ROOT}/netsim
NETSCRIPT_DIR=${ROOT}/netscript
DIRS=$(filter-out %Makefile,$(wildcard ${CWD}/*))
ALL_DEPS=$(addsuffix /.done,${DIRS})
CLEAN_DEPS=$(addsuffix /.clean,${DIRS})

.PHONY: all clean setup test

all: setup test
	
test: ${ALL_DEPS}
	@echo "finished all top level tests, running netscript tests"
#make -C ${ROOT}/netscript/test all

clean: ${CLEAN_DEPS}
	@echo "finished cleaing top level tests, cleaning netscript tests"
#make -C ${ROOT}/netscript/test clean
	
setup:
	@echo "compiling prime and jprime"
	make -C ${NETSIM_DIR} ssfnet
	make -C ${NETSIM_DIR} ssfnet
	cd ${NETSCRIPT_DIR} && ant jar
	
%/.done: %
	@env ROOT=${ROOT} make -C $< all

%/.clean: %
	@make -C $< clean
	