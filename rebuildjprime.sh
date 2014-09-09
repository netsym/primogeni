#Comments
#[me@linuxbox me]$ chmod 755 my_script
#[me@linuxbox me]$ ./my_script


echo "--------------------------------------------------------"
echo "Updating jar"
echo "--------------------------------------------------------"
cd ~/Desktop/pgc2vega/primex/netscript
ant clean
ant jar 
cd dist
rm ~/Desktop/pgc2vega/primex/netIDE/jprime/jprime.jar ~/Desktop/pgc2vega/primex/netIDE/jprime/jprime.src.jar
cp *.jar ~/Desktop/pgc2vega/primex/netIDE/jprime/
echo "--------------------------------------------------------"
echo "Successfully updated"
echo "--------------------------------------------------------"


