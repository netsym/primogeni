#!/bin/bash
RUN=0
while true
do
	perl /primex/scripts/shutdown.pl
	java -cp /primex/jprime.jar monitor.core.Monitor > /primex/run_${RUN}.out
	RUN=`expr 1 + $RUN`
	sleep 1
done
