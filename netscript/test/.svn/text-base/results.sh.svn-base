#!/bin/bash
CWD=$1
SUFFIX=$2
FUNC=$3
fails=0
skips=0
passes=0
files=$(find $1 -name "*$2")
echo " "
echo " "
echo "Summary of ${FUNC}s:"
echo "-----------------------------"
for f in ${files}; do
	a=$(basename $f)
	b=${a/${SUFFIX}/""}
	c=$(cat ${f})
	if [ "$c" != "PASS" ]
	then
		if [ "$c" == "SKIP" ]
		then
			echo "\tSkipped ${FUNC} of ${b}"
			skips=$(($skips + 1))
		else
			echo "\tFailure in ${FUNC} of ${b}"
			fails=$(($fails + 1))
		fi
	else
		passes=$(($passes + 1))
	fi
done
echo " "
echo "\t---------------------"
echo "\tTotal passed :${passes}"
echo "\tTotal skipped:${skips}"
echo "\tTotal failed :${fails}"
echo "\t---------------------"

exit ${fails}