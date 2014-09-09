#!/bin/bash
CWD=$1
MODEL=$2
x=$(basename $2)
NAME=${x%.*}
JAVA=$(which java)
RM=$(which rm)
MKDIR=$(which mkdir)
MEM=2048m

PATH=${CWD}/testtmp
DB_NAME=testdb
DB_PATH=${PATH}
DB_DEBUG=false
DB_TYPE=DERBY
CREATE_XML=true
CREATE_GRAPHVIZ=false
PART_STR="1::1:1"
OUT_DIR=${PATH}

JAR="${CWD}/../dist/jprime.jar"
OPS="-Xmx${MEM} -DDB_NAME=${DB_NAME} -DDB_PATH=${DB_PATH} -DDB_DEBUG=${DB_DEBUG} -DDB_TYPE=${DB_TYPE} -DCREATE_XML=${CREATE_XML} -DCREATE_GRAPHVIZ=${CREATE_GRAPHVIZ} -DPART_STR=${PART_STR} -DOUT_DIR=${OUT_DIR}"
CMD="${JAVA} ${OPS} -jar ${JAR} create ${NAME}.model ${MODEL}"

echo "Comping ${x}"
${MKDIR} -p ${PATH}
${RM} -rf ${PATH}/*.{_1.xml,tlv}
${CMD} &> ${MODEL}.comp.out
rv=$?
if [ "0" == "${rv}" ]
then
	#lets see if we can load up the associated xml
	CMD="${JAVA} ${OPS} -jar ${JAR} create ${NAME}.model_xml ${OUT_DIR}/${NAME}.model.xml"
	echo "\tComping extacted version of ${x}"
	${CMD} &> ${MODEL}.xml.comp.out
	rv=$?
	if [ "0" == "${rv}" ]
	then
		echo "PASS" > ${MODEL}.comp
	else
		echo "FAIL" > ${MODEL}.comp
	fi
else
	echo "FAIL" > ${MODEL}.comp
fi
exit 0
