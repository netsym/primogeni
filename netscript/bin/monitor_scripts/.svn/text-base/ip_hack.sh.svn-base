geni=""
prime=""
ip=""
for i in 0 1 2 3
do
	ifconfig eth$i up
	link=`ethtool eth$i | grep Link | awk '{print $3}'`
	t=`ifconfig eth$i | grep Mask | grep 131`
	if [ "yes" == "$link" ]
		then
		if [ -z "$t" ]
		then
			echo "eth$i link=$link"
			prime="eth$i"
		else
			ip=`ifconfig eth$i | grep Mask | awk '{split(substr($2,6),a,".");print a[4]}'`
			echo "eth$i link=$link, ip='$ip'"
			geni="eth$i"
		fi
	else
		echo "eth$i is down"
		ifconfig eth$i down
	fi
done 
echo "geni=$geni, prime=$prime, ip=$ip"
cmd="ifconfig $prime 172.1.0.$ip up"
echo "$cmd"
$cmd
