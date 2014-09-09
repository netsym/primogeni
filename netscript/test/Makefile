CWD=$(realpath .)
COMPILE=sh ${CWD}/compile_test.sh ${CWD}
PARTITION=sh ${CWD}/part_test.sh ${CWD}
RV=sh ${CWD}/results.sh ${CWD}
JAVA=/export/home/tingli/workspace/primex/netscript/test/java_models/CNFBaseline.java
#JAVA=$(filter-out %.class,$(wildcard ${CWD}/java_models/*.java))
XML=$(wildcard ${CWD}/xml_models/*.xml)
PYTHON=$(wildcard ${CWD}/python_models/*.py)

MODELS=$(sort $(filter-out %SwingTest.java %CampusAlgorithmic.java %CampusGlobalShortestPath.java %CampusMostlyShortestPath.java %LargeModel.java,${JAVA} ${XML} ${PYTHON}))
SKIP=$(sort $(filter %SwingTest.java  %CampusAlgorithmic.java %CampusGlobalShortestPath.java %CampusMostlyShortestPath.java %LargeModel.java,${JAVA} ${XML} ${PYTHON}))

COMP=$(addsuffix .comp,$(basename ${MODELS})) $(addsuffix .comp.skip,$(basename ${SKIP}))
PART=$(addsuffix .part,$(basename ${MODELS})) $(addsuffix .part.skip,$(basename ${SKIP}))

#.PHONY:	all setup compile partition clean

all: compile partition

help:	
	@echo "Targets:"
	@echo "    all       :  Run all tests."
	@echo "    compile   :  Run tlv compilation tests."
	@echo "    partition :  Run partitioning tests."

clean:
	@find ${CWD} -name "derby.log" | xargs rm -rf
	@find ${CWD} -name "*.class" | xargs rm -rf
	@find ${CWD} -name "*.comp*" | xargs rm -rf
	@find ${CWD} -name "*.part*" | xargs rm -rf
	@find ${CWD} -name "testtmp" -type d | xargs rm -rf

compile: ${COMP}
	echo "------compile---@${RV} .comp Compilation\n"
	@${RV} .comp Compilation

partition: ${PART}
	echo "------partition---@${RV} .part Partitioning\n"
	@${RV} .part Partitioning

%.comp: %.java
	echo "------.comp java-----@${COMPILE} ${F} $<\n"
	@${COMPILE} ${F} $<

%.part: %.java
	echo "------.part java-----@${PARTITION} ${F} $<\n"
	@${PARTITION} ${F} $<

%.comp: %.xml
	echo "------.comp xml-----@${COMPILE} ${F} $<\n"
	@${COMPILE} ${F} $<

%.part: %.xml
	echo "------.part xml-----@${PARTITION} ${F} $<\n"
	@${PARTITION} ${F} $<
	
%.comp: %.py
	@echo "SKIP" > $<.comp
	
%.part: %.py
	@echo "SKIP" > $<.part

%.part.skip: %.py
	@echo "SKIP" > $<.part

%.part.skip: %.java
	@echo "SKIP" > $<.part

%.part.skip: %.xml
	@echo "SKIP" > $<.part

%.comp.skip: %.py
	@echo "SKIP" > $<.comp

%.comp.skip: %.java
	@echo "SKIP" > $<.comp

%.comp.skip: %.xml
	@echo "SKIP" > $<.comp

