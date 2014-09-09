#!/bin/bash

NETSCRIPT_PATH="../../netscript"
TRACES_PATH=$1

echo "traces_path=$TRACES_PATH"

#-----------------------------------------------------------------
#1) test TCP behavior in terms of cwnd trajectories and throughput
# i) Highspeed
echo "****************************************"
echo "testing TCP behavior"
echo "****************************************"
echo "  tcp tests for highspeed:"
rm ${TRACES_PATH}/trace* >& /tmp/out
cp test-tcp-behavior-highspeed.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-tcp-behavior-highspeed.xml &> /tmp/out)
res=`(cd ../ ; ./primex 63 ../netscript/tcptester_part_1.tlv | grep "complete")`
number=`echo $res | wc -l`
thru=`echo $res | awk 'BEGIN{} {split($3,array,"=") ; throughput=array[2]*8 ; print throughput} END{}'`
echo "\tThe number of flows completed is : ${number}"
if [ "$number" -ne 1 ]
then
	echo "\ttest 1 not passed: the number of flows completed is not correct, should be 1"
else
	echo "\tPASSED: correct number of flows completed for test 1"
fi
echo "\tThroughput achieved=${thru} Mbps"
thru=${thru/.*}
if [[ "$thru" -lt 9 ]]
then
	echo "\tthroughput test for highspeed not passed, should be > 90% of bandwidth"
else
	echo "\tPASSED: achieved a usage of more than 90%"
fi
mv ${TRACES_PATH}/trace* ${TRACES_PATH}/trace >& /tmp/out 
last=$?
if [ "$last" -ne 0 ]
then
        echo "\tFAILED: could not read the trace file"
else
	res=`cat ../results/trace | grep "59\.9" | awk 'BEGIN{} {print $7} END{}'` 
	echo "\tThe number of congestion window cuts is : ${res}"
	if [ "$res" -ne 8 ]
	then
        	echo "\tFAILED: the number of congestion window cuts is not correct, should be 8"
	else
		if [ "$last" -eq 0 ]
		then
        		echo "\tPASSED: correct number of congestion window cuts completed for test 1"
		fi
	fi
fi
# ii) BIC
echo "  tcp tests for bic:"
rm ${TRACES_PATH}/trace* >& /tmp/out
cp test-tcp-behavior-bic.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-tcp-behavior-bic.xml &> /tmp/out)
res=`(cd ../ ; ./primex 63 ../netscript/tcptester_part_1.tlv | grep "complete")`
number=`echo $res | wc -l`
thru=`echo $res | awk 'BEGIN{} {split($3,array,"=") ; throughput=array[2]*8 ; print throughput} END{}'`
echo "\tThe number of flows completed is : ${number}"
if [ "$number" -ne 1 ]
then
        echo "\ttest 1 not passed: the number of flows completed is not correct, should be 1"
else
	echo "\tPASSED: correct number of flows completed for test 1"
fi
echo "\tThroughput achieved=${thru} Mbps"
thru=${thru/.*}
if [[ "$thru" -lt 9 ]]
then
        echo "\tthroughput test for highspeed not passed, should be > 90% of bandwidth"
else
        echo "\tPASSED: achieved a usage of more than 90%"
fi
mv ${TRACES_PATH}/trace* ${TRACES_PATH}/trace >& /tmp/out
last=$?
if [ "$last" -ne 0 ]
then
        echo "\tFAILED: could not read the trace file"
else
	res=`cat ../results/trace | grep "59\.9" | awk 'BEGIN{} {print $7} END{}'`
	echo "\tThe number of congestion window cuts is : ${res}"
	if [ "$res" -ne 9 ]
	then
        	echo "\ttest 1 not passed: the number of congestion window cuts is not correct, should be 9"
	else
		if [ "$last" -eq 0 ]
        	then
       			echo "\tPASSED: correct number of congestion window cuts completed for test 1"
		fi
	fi
fi
#Scalable
echo "  tcp tests for scalable:"
rm ${TRACES_PATH}/trace* >& /tmp/out
cp test-tcp-behavior-scalable.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-tcp-behavior-scalable.xml &> /tmp/out)
res=`(cd ../ ; ./primex 63 ../netscript/tcptester_part_1.tlv | grep "complete")`
number=`echo $res | wc -l`
thru=`echo $res | awk 'BEGIN{} {split($3,array,"=") ; throughput=array[2]*8 ; print throughput} END{}'`
echo "\tThe number of flows completed is : ${number}"
if [ "$number" -ne 1 ]
then
        echo "\ttest 1 not passed: the number of flows completed is not correct, should be 1"
else
	echo "\tPASSED: correct number of flows completed for test 1"
fi
echo "\tThroughput achieved=${thru} Mbps"
thru=${thru/.*}
if [[ "$thru" -lt 9 ]]
then
        echo "\tthroughput test for highspeed not passed, should be > 90% of bandwidth"
else
        echo "\tPASSED: achieved a usage of more than 90%"
fi
mv ${TRACES_PATH}/trace* ${TRACES_PATH}/trace >& /tmp/out
last=$?
if [ "$last" -ne 0 ]
then
        echo "\tFAILED: could not read the trace file"
else
	res=`cat ../results/trace | grep "58\.9" | awk 'BEGIN{} {print $7} END{}'`
	echo "\tThe number of congestion window cuts is : ${res}"
	if [ "$res" -ne 24 ]
	then
        	echo "\ttest 1 not passed: the number of congestion window cuts is not correct, should be 24"
	else
		if [ "$last" -eq 0 ]
        	then
       			echo "\tPASSED: correct number of congestion window cuts completed for test 1"
		fi
	fi
fi

#-----------------------------------------------------------------
# 2) test TCP flow completion 
# i) Dummbell:1000Mbps - 10Mbps - 10Mbps; queuesize=64000; mss=1448 
echo "****************************************"
echo "testing flow completion using many file sizes in different bottleneck link capacities"
echo "****************************************"
echo "  bottleneck link capacity=10Mbps, filesize of each flow=10KB:"
cp test-flow-completion-10K-cubic.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-10K-cubic.xml &> /tmp/out)
#1000 seconds of experimentation
number=`(cd ../ ; ./primex 1000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 10000  ] 
then
	echo "\tflows completed:$number"
	echo "\tPASSED: all 10000 flows of 10K completed using cubic"
else
	let incomplete=10000-number
	num=`echo "scale=2;$number/100" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi

echo "  bottleneck link capacity=10Mbps, filesize of each flow=1MB:"
cp test-flow-completion-1M-cubic.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-1M-cubic.xml &> /tmp/out)
#10000 seconds of experimentation
number=`(cd ../ ; ./primex 10000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 10000  ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all 10000 flows of 1M completed using cubic"
else
        let incomplete=10000-number
	num=`echo "scale=2;$number/100" | bc`
        echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi

#mss=10000 in the following exp
echo "  bottleneck link capacity=10Mbps, filesize of each flow=4GB:"
cp test-flow-completion-4G-cubic.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-4G-cubic.xml &> /tmp/out)
#400000 seconds of experimentation
number=`(cd ../ ; ./primex 400000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 20  ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all 4G flows completed using cubic"
else
        let incomplete=10000-number
        num=`echo "scale=2;($number*10)/2" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi


# ii) Dumbbell:1000Mbps - 500Mbps - 10Mbps; queuesize=64000; mss=1448
echo "  bottleneck link capacity=500Mbps, filesize of each flow=10KB:"
cp test-flow-completion-10K-cubic-500Mbot.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-10K-cubic-500Mbot.xml &> /tmp/out)
#1000 seconds of experimentation
number=`(cd ../ ; ./primex 1000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 10000  ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all 10000 flows of 10K completed using cubic"
else
        let incomplete=10000-number
        num=`echo "scale=2;($number*10)/2" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi

echo "  bottleneck link capacity=500Mbps, filesize of each flow=1MB:"
cp test-flow-completion-1M-cubic-500Mbot.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-1M-cubic-500Mbot.xml &> /tmp/out)
#1000 seconds of experimentation
number=`(cd ../ ; ./primex 1000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 10000  ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all 10000 flows of 1M completed using cubic"
else
        let incomplete=10000-number
        num=`echo "scale=2;$number/100" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi

#mss=10000 in the following exp
echo "  bottleneck link capacity=500Mbps, filesize of each flow=4GB:"
cp test-flow-completion-4G-cubic-500Mbot.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-4G-cubic-500Mbot.xml &> /tmp/out)
#400000 seconds of experimentation
number=`(cd ../ ; ./primex 400000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 20  ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all 4G flows (20) completed using cubic"
else
        let incomplete=10000-number
        num=`echo "scale=2;($number*10)/2" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 20"
fi

# iii) Dumbbell:1000Mbps - 1Mbps - 10Mbps; queuesize=64000; mss=1448
echo "  bottleneck link capacity=1Mbps, filesize of each flow=10KB:"
cp test-flow-completion-10K-cubic-1Mbot.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-10K-cubic-1Mbot.xml &> /tmp/out)
#1200 seconds of experimentation
number=`(cd ../ ; ./primex 1200 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 10000 ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all flows (10000) of 10K completed using cubic"
else
	let incomplete=1000-number
        num=`echo "scale=2;$number/100" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi

echo "  bottleneck link capacity=1Mbps, filesize of each flow=1MB:"
cp test-flow-completion-1M-cubic-1Mbot.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-1M-cubic-1Mbot.xml &> /tmp/out)
#20000 seconds of experimentation
number=`(cd ../ ; ./primex 20000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 1000 ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all flows (1000) of 1M completed using cubic"
else
        let incomplete=1000-number
        num=`echo "scale=2;$number/100" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi

#mss=10000 in the following exp
echo "  bottleneck link capacity=1Mbps, filesize of each flow=1GB:"
cp test-flow-completion-1G-cubic-1Mbot.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-1G-cubic-1Mbot.xml &> /tmp/out)
#1000000 seconds of experimentation
number=`(cd ../ ; ./primex 1000000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 50  ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all 4G flows completed using cubic"
else
        let incomplete=50-number
        num=`echo "scale=2;($number*10)/2" | bc`
	echo "\tFAILED:percentage of flows completed: $num[%], $number out of 50"
fi

echo "****************************************"
echo "testing flow completion with a low bandwidth bottleneck link and when traffic is flowing both ways"
echo "****************************************"
cp test-flow-completion-fs1M-bot10Mbps-10000flows-bothways.xml ../../netscript/
(cd ${NETSCRIPT_PATH}; java -jar dist/jprime.jar create tcptester test-flow-completion-fs1M-bot10Mbps-10000flows-bothways.xml &> /tmp/out)
#10000 seconds of experimentation
number=`(cd ../ ; ./primex 10000 ../netscript/tcptester_part_1.tlv | grep "complete" | wc -l)`
#echo "number=$number"
if [ "$number" -eq 10000  ]
then
        echo "\tflows completed:$number"
        echo "\tPASSED: all 1M flows completed"
else
        let incomplete=10000-number
        num=`echo "scale=2;($number)/100" | bc`
        echo "\tFAILED:percentage of flows completed: $num[%], $number out of 10000"
fi




