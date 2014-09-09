TIME=`expr $1 \* 1000000`
NAME="/usr/local/primo/vz/tmp/$2.tcp.out"
mkdir -p /usr/local/primo/vz/tmp
echo "<<<< START" > $NAME
while [ $TIME -gt 0 ]
do
	D=`date +"%s:%N"`
	echo "<<<< $D" >> $NAME
	cat /proc/net/tcp >> $NAME
	TIME=`expr $TIME - 100000`
	usleep 100000
done
