CWD=$(realpath .)
ifeq ($(strip $(ROOT)),)
ROOT=${CWD}/../../
endif
NETSIM_DIR=${ROOT}/netsim
NETSCRIPT_DIR=${ROOT}/netscript
JAR=${NETSCRIPT_DIR}/dist/jprime.jar
JAVA1=java -Xmx${NC}24m -DDB_NAME=testdb -DDB_PATH=${CWD}
JAVA3=java -Xmx${NC}24m -DDB_NAME=testdb -DDB_PATH=${CWD} -DPART_STR=3::1:1,2:1,3:1
MODEL=${NETSCRIPT_DIR}/test/java_models/TwentyCampus.java
SIM=${NETSIM_DIR}/primex
MPI=mpirun -n 3 ${SIM} 
RUNTIME=1000
NC=10
A=inter_campus intra_campus 1_campus 2_campus 3_campus 4_campus 5_campus 6_campus 7_campus 8_campus 9_campus
TARGETS=$(sort $(addsuffix .1.hops,${A}) $(addsuffix .3.hops,${A}))
B=$(addsuffix .1,${A}) $(addsuffix .3,${A})
CLEAN=$(addprefix ${CWD}/,${B})
include ${NETSIM_DIR}/Makefile.inc

ifneq ($(ENABLE_SSFNET_ROUTE_DEBUG), yes)
all:
	$(error you must enable ssfnet route debugging during configuration using '--enable-ssfnet-route-debug')
else
.PHONY: all clean

all: ${TARGETS}
	@sh process_results.sh
	@echo "Finished routing tests"

clean:
	@rm -rf ${CWD}/*.tlv ${CWD}/*.out ${CWD}/*.xml ${CWD}/testdb ${CWD}/derby.log ${CWD}/*.hops ${CLEAN} ${CWD}/*.stats ${CWD}/temp
	@echo "Finished routing cleaning"


${JAR}:
	$(error cant find ${JAR})

${SIM}:
	$(error cant find ${SIM})

${MODEL}:
	$(error cant find ${MODEL})

inter_campus.1.stats inter_campus.3.stats: ${MODEL} ${JAR} ${SIM}
	@rm -rf ${CWD}/testdb
	rm -f hops_part_*.stats
	${JAVA1} -DOUT_DIR=${CWD} -DNUM_CAMPUS=${NC} -DINTRA_CAMPUS_TRAFFIC=false -DINTER_CAMPUS_TRAFFIC=true -DTRAFFIC_TYPE=-1 -jar ${JAR} create inter ${MODEL} &> inter_campus.1
	${SIM} ${RUNTIME} inter_part_1.tlv &> inter_campus.1.out
	cat hops_part_*.stats > inter_campus.1.stats	 
	rm -f hops_part_*.stats
	${JAVA3} -jar ${JAR} load inter &> inter_campus.3
	${MPI} ${RUNTIME} inter ./ &>inter_campus.3.out
	cat hops_part_*.stats > inter_campus.3.stats 

intra_campus.1.stats intra_campus.3.stats: ${MODEL} ${JAR} ${SIM}
	@rm -rf ${CWD}/testdb
	rm -f hops_part_*.stats
	${JAVA1} -DOUT_DIR=${CWD} -DNUM_CAMPUS=${NC} -DINTRA_CAMPUS_TRAFFIC=true -DINTER_CAMPUS_TRAFFIC=false -DTRAFFIC_TYPE=-1 -jar ${JAR} create intra ${MODEL} &> intra_campus.1
	${SIM} ${RUNTIME} intra_part_1.tlv &> intra_campus.1.out
	cat hops_part_*.stats > intra_campus.1.stats
	rm -f hops_part_*.stats
	${JAVA3} -DOUT_DIR=${CWD} -jar ${JAR} load intra ${MODEL} &> intra_campus.3
	${MPI} ${RUNTIME} intra ./ &> intra_campus.3.out
	cat hops_part_*.stats > intra_campus.3.stats 

%_campus: ${MODEL} ${JAR} ${SIM}
	@touch $@
	
%_campus_part_1.tlv: %_campus
	@rm -rf ${CWD}/testdb
	${JAVA1} -DOUT_DIR=${CWD} -DNUM_CAMPUS=${NC} -DINTRA_CAMPUS_TRAFFIC=false -DINTER_CAMPUS_TRAFFIC=false -DTRAFFIC_TYPE=$< -jar ${JAR} create $< ${MODEL} &> $<

%_campus_part_3.tlv:
			
%_campus.1.stats: %_campus_part_1.tlv
	rm -f hops_part_*.stats
	${SIM} ${RUNTIME} $< &> temp
	cat hops_part_*.stats > $@
	rm -f hops_part_*.stats

%_campus.3.stats: %_campus
	rm -f hops_part_*.stats
	${JAVA3} -jar ${JAR} load $< ${MODEL} &> $<
	${MPI} ${RUNTIME} $< ./ &> temp
	cat hops_part_*.stats > $@
	rm -f hops_part_*.stats

%.hops: %.stats
	python process_hops.py $< temp 
	sort temp > $@
	rm -f *.stats temp
	
endif
