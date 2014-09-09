#!/usr/bin/perl

use strict;

my $rv;

`killall mpiexec`;
`pidof ssfgwd | xargs kill -9 `;
`pidof openvpn | xargs kill -9 `;
`pidof mpiexec | xargs kill -9 `;

for(my $counter = 1; $counter <= 10; $counter++){
	my $bridges = `ifconfig | grep br`;
	foreach my $temp (split(/\n/,$bridges)) {
		$temp =~ s/^\s+//;
		$temp = (split(/\s/,$temp))[0];
		$rv=`/sbin/ifconfig $temp down`;
		print $rv;
		$rv=`/usr/sbin/brctl delbr $temp`;
		print $rv;
	}
}

for(my $counter = 1; $counter <= 10; $counter++){
	my $taps = `ifconfig | grep tap`;
	foreach my $temp  (split(/\n/,$taps)) {
		$temp =~ s/^\s+//;
		$temp = (split(/\s/,$temp))[0];
		$rv=`/usr/sbin/tunctl -d $temp`;
		print $rv;
	}
}

for(my $counter = 1; $counter <= 10; $counter++){
	my $containers = `vzlist | grep -v "CTID"`;
	foreach my $temp (split(/\n/,$containers)) {
		$temp =~ s/^\s+//;
		$temp = (split(/\s/,$temp))[0];
		$rv=`/usr/sbin/vzctl stop $temp`;
		print $rv;
		$rv=`/bin/umount /usr/local/primo/vz/private/$temp`;
		print $rv;
	}
}
