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
CREATE_XML=false
CREATE_GRAPHVIZ=false
PART_STR="4::1:1,2:1,3:1,4:1"
OUT_DIR=${PATH}

JAR="${CWD}/../dist/jprime.jar"
OPS="-Xmx${MEM} -DDB_NAME=${DB_NAME} -DDB_PATH=${DB_PATH} -DDB_DEBUG=${DB_DEBUG} -DDB_TYPE=${DB_TYPE} -DCREATE_XML=${CREATE_XML} -DCREATE_GRAPHVIZ=${CREATE_GRAPHVIZ} -DPART_STR=${PART_STR} -DOUT_DIR=${OUT_DIR}"
CMD="${JAVA} ${OPS} -jar ${JAR} load ${NAME}.model"

echo "Comping ${x}"
${MKDIR} -p ${PATH}
${RM} -rf ${PATH}/*.{xml,tlv}
${CMD} &> ${MODEL}.part.out
#${CMD}
rv=$?
if [ "0" == "${rv}" ]
then
	echo "PASS" > ${MODEL}.part
else
	echo "FAIL" > ${MODEL}.part
fi
exit 0
