#!/usr/bin/perl
#usage: perl /primex/scripts/confVENetwork.pl 237 veth216 192.1.0.41 00:00:00:00:00:d8 66:00:00:00:00:d8
#usage: perl confVENetwork.pl 237 veth216 192.1.0.41 00:00:00:00:00:d8 66:00:00:00:00:d8

use strict;
my $i;
my $cont_id = $ARGV[0];
my $nic_id = $ARGV[1];
my $nic_ip = $ARGV[2];
my $nic_mac = $ARGV[3];
my $veth_mac = $ARGV[4]; 

print "args to conf VE: cont_id=$cont_id nic_id=$nic_id nic_ip=$nic_ip nic_mac=$nic_mac veth_mac=$veth_mac \n";

`/usr/sbin/vzctl set $cont_id --netif_add eth0,$nic_mac,$nic_id,$veth_mac`;
if ( $? != 0 )
{
  print "command failed: /usr/sbin/vzctl set $cont_id --netif_add eth0,$nic_mac,$nic_id,$veth_mac\n";
  exit 1;
}

# TODO: LIU: the netmask is fixed here; should be passed in as an argument from the compiled model from slingshot
`/usr/sbin/vzctl exec $cont_id ifconfig eth0 $nic_ip netmask 255.255.0.0 up`;
if ( $? != 0 )
{
  print "command failed: /usr/sbin/vzctl exec $cont_id ifconfig eth0 $nic_ip netmask 255.255.0.0 up\n";
  exit 1;
}

`/usr/sbin/vzctl exec $cont_id "echo \"nameserver 8.8.8.8\" > /etc/resolv.conf"`;

print "VEs network configuration sucessfully!\n";
exit 0;



