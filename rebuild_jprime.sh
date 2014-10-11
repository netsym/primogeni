#Comments
#[me@linuxbox me]$ chmod 755 my_script
#[me@linuxbox me]$ ./my_script


echo "--------------------------------------------------------"
echo "Updating jar"
echo "--------------------------------------------------------"
cd ./netscript
ant clean
ant jar 
cd dist
rm ../../netIDE/jprime/jprime.jar ../../netIDE/jprime/jprime.src.jar
cp *.jar ../../netIDE/jprime/
echo "--------------------------------------------------------"
echo "Successfully updated"
echo "--------------------------------------------------------"


