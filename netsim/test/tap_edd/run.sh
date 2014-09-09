vzctl set 1002 --netif_add eth0,66:12:03:04:05:01,veth1002.0,00:12:03:04:05:01
vzctl exec 1002 ifconfig eth0 192.1.0.1 up

vzctl set 1003 --netif_add eth0,66:12:03:04:05:02,veth1003.0,00:12:03:04:05:02
vzctl exec 1003 ifconfig eth0 192.1.0.2 up

ifconfig veth1002.0 0.0.0.0 up
ifconfig veth1003.0 0.0.0.0 up

tunctl -t tap1002 
tunctl -t tap1003
ifconfig tap1002 0 up
ifconfig tap1003 0 up
ifconfig tap1002 txqueuelen 100
ifconfig tap1002 txqueuelen 100

brctl addbr br1002
brctl addbr br1003

ifconfig br1002 0 up
ifconfig br1003 0 up

brctl addif br1002 tap1002
brctl addif br1002 veth1002.0

brctl addif br1003 tap1003
brctl addif br1003 veth1003.0

for C in 1002 1003;
do
	A="vzctl exec ${C} ip route show"
	A=${A}" | awk '{print \"vzctl exec ${C} ip route del \"\$1}'"
	A=${A}" | sh"
	A=${A}" ; vzctl exec ${C} ip route add default dev eth0"
	A=${A}" ; vzctl exec ${C} ip route show"
	${A}
	done

./bridge BRIDGE tap1002 veth1002.0 66:12:03:04:05:01 192.1.0.1 BRIDGE tap1003 veth1003.0 66:12:03:04:05:02 192.1.0.2
