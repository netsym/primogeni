#!/bin/sh
#
if [ "$JAVA_HOME" = "" ]; then
    export JAVA_HOME=/usr/local/jdk1.6.0_23/; 
    #please set JAVA_HOME with appropriate JAVA installation directory
fi
#change JAVA_HOME for other java installation, this is just to make sure that the
cd netsim
./configure
make distclean
cd ../netscript
ant clean
cd ../topology
make clean
cd ../netsim
./configure
make ssfnet-jprime
make ssfnet
cd ../netscript
ant jar
cd ../topology
make all
echo FINISHED_INSTALLATION

