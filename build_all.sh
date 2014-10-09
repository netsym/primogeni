#!/bin/sh

if [ "$JAVA_HOME" == "" ] 
then
  echo "JAVA_HOME is not set. Please install Java and set JAVA_HOME accordingly."
  exit 1
fi

echo "configuring primex netsim..."
cd netsim > /dev/null
output=`./configure`
if [[ $output =~ .*successfully.* ]]
then
  echo "primex configured successfully..."
else
  echo "$output"
  echo "Please fix the issues before installing primex"
  exit 1
fi

make clean
make ssfnet
result=$?
if [ "$result" != "0" ]; then
  echo "error found when compiling ssfnet"
fi
make ssfnet-jprime
result=$?
if [ "$result" != "0" ]; then
  echo "error found when compiling ssfnet"
fi
echo "ssfnet compiled successfully..."

cd ../netscript > /dev/null
ant clean
ant jar
result=$?
if [ "$result" != "0" ]; then
  echo "error found when compiling jprime"
fi
echo "jprime compiled successfully..."

cd ../topology > /dev/null
make clean
make all
result=$?
if [ "$result" != "0" ]; then
  echo "error found when compiling topology generator"
fi
echo "topology compiled successfully..."

echo "Congratulations. Primex has been built successfully!"
