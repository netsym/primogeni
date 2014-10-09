#!/bin/sh

echo "clean up netsim..."
cd netsim > /dev/null
make distclean > /dev/null
echo "clean up jprime..."
cd ../netscript > /dev/null
ant clean > /dev/null
echo "clean up topology..."
cd ../topology > /dev/null
make clean > /dev/null
echo "Primex has been uninstalled successfully!"
