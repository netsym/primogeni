#!/bin/sh
#Usage: perl createEmuHostIf.pl 237 veth216 192.1.0.41 00:00:00:00:00:d8 66:00:00:00:00:d8
#args                            1     2         3            4                 5
#                            cont_id  host_if  cont_ip     cont_mac        host_mac

ttl_args=$#
req_args=5

echo "Total args="${ttl_args}" required args="${req_args}
if [ $ttl_args -lt $req_args ]
then
	echo "Total args is less than $req_args"
	exit 1
else
	echo 'Received ' $# ' args. Listing args;'
	for i; do 
		echo $i 
	done
fi

cont_id=h$1
cont_if=$cont_id"-eth0"
cont_ip=$3
cont_mac=$4

host_if=$2
host_mac=$5

echo "args to configure emulated host: cont_id=$cont_id cont_if=$cont_if cont_ip=$cont_ip cont_mac=$cont_mac and host: host_if=$host_if host_mac=$host_mac\n"
echo "Status: 10 steps total"

ip link add $cont_if type veth peer name $host_if
if ( $? != 0 )
then
  echo "command failed: ip link add $cont_if type veth peer name $host_if\n"
  exit 1;
else 
	echo "1>"
fi

ip netns add $cont_id
if ( $? != 0 )
then
  echo "command failed: ip netns add $cont_id\n"
  exit 1
else 
	echo "2>"
fi

ip link set $cont_if netns $cont_id
if ( $? != 0 )
then
  echo "command failed: ip link set $cont_if netns $cont_id\n"
  exit 1
else 
	echo "3>"
fi

ip netns exec $cont_id ifconfig $cont_if down
if ( $? != 0 )
then
  echo "ip netns exec $cont_id ifconfig $cont_if down\n"
  exit 1
else 
	echo "4>"
fi

ip netns exec $cont_id ifconfig $cont_if $cont_ip
if ( $? != 0 )
then
  echo "command failed: ip netns exec $cont_id ifconfig $cont_if $cont_ip\n"
  exit 1
else 
	echo "5>"
fi

ip netns exec $cont_id ifconfig $cont_if hw ether $cont_mac
if ( $? != 0 )
then
  echo "command failed: ip netns exec $cont_id ifconfig $cont_if hw enter $cont_mac\n"
  exit 1
else 
	echo "6>"
fi

ip netns exec $cont_id ifconfig $cont_if up
if ( $? != 0 )
then
  echo "command failed: sudo ip netns add $cont_id\n"
  exit 1
else 
	echo "7>"
fi

ifconfig $host_if down
if ( $? != 0 )
then
  echo "command failed: ifconfig $host_if down\n"
  exit 1;
else 
	echo "8>"
fi

ifconfig $host_if hw ether $host_mac
if ( $? != 0 )
then
  echo "command failed: ifconfig $host_if hw enter $host_mac\n"
  exit 1;
else 
	echo "9>"
fi

ifconfig $host_if up
if ( $? != 0 )
then
  echo "command failed: ifconfig $host_if up\n"
  exit 1
else 
	echo "10>>>"
fi

echo "Complete!"