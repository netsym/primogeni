fails=0
echo ""
echo "Verifying that routes have not changed."
echo "***********************************************"
for f in $(ls *.1.hops)
do
	base=`echo "${f}" | awk '{print substr($0,0,length($0)-7)}'`
 	d=`diff -N -q -s ${base}.1.hops ${base}.hops.orig | awk '{print $5}'`
	if [ "differ" == "${d}" ]
	then
		echo "\tFAILED: ${base}.1.hops changed!"
		fails=$((fails+1))
	else
		echo "\tSUCESS: ${base}.1.hops did not change!"
	fi
 	d=`diff -N -q -s ${base}.3.hops ${base}.hops.orig | awk '{print $5}'`
	if [ "differ" == "${d}" ]
	then
		echo "\tFAILED: ${base}.3.hops changed!"
		fails=$((fails+1))
	else
		echo "\tSUCESS: ${base}.3.hops did not change!"
	fi
done
if [ "0" -ne "${fails}" ]
then
	echo "\t***************************************"
	echo "\tFailure! Found some routes that changed."
else
	echo "\tSuccess! No routes changed."
fi

exit $fails