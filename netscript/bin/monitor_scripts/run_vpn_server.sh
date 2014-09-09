PRIME_HOME=$1
EXP_DIR=$2
EXP_NAME=$3
SERVER_NAME=$4
SERVER_DIR=openvpn_server_${SERVER_NAME}

echo "PRIME_HOME=${PRIME_HOME}"
echo "EXP_DIR=${EXP_DIR}"
echo "EXP_NAME=${EXP_NAME}"
echo "SERVER_NAME=${SERVER_NAME}"
echo "SERVER_DIR=${SERVER_DIR}"

echo "cd ${EXP_DIR}/${EXP_NAME}"
cd ${EXP_DIR}/${EXP_NAME}
echo "tar -zxf ${SERVER_NAME}.tgz"
tar -zxf ${SERVER_NAME}.tgz
echo "cd ${EXP_DIR}/${EXP_NAME}/${SERVER_DIR}"
cd ${EXP_DIR}/${EXP_NAME}/${SERVER_DIR}
echo "sh PRIME_HOME=${PRIME_HOME} PWD=${EXP_DIR}/${EXP_NAME}/${SERVER_DIR} runserver.sh"
PRIME_HOME=${PRIME_HOME} PWD=${EXP_DIR}/${EXP_NAME}/${SERVER_DIR} bash runserver.sh
exit 0