
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


