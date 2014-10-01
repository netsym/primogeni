#!/usr/bin/perl
#usage: perl /primex/scripts/createFolders.pl 237 237

use strict;
my $i;
my $from = $ARGV[0];
my $to   = $ARGV[1];

for($i=$from; $i<=$to; $i++){
   #kill a container: if there is a container with duplicate CT_ID
   `/usr/sbin/vzctl stop $i`;
   `/usr/sbin/vzctl destroy $i`;

   #Create configuration file before starting the container
   #container template should be palced in /vz/tempalte/cache
   `/usr/sbin/vzctl create $i --ostemplate centos-6-x86_64-minimal --config basic`;
   if ( $? != 0 )
   {
     print "command failed: failed to container VE with id=$i\n";
     exit 1;
   }
   #set onboot yes for container, that will be reflected to /etc/vz/conf/CT_ID.conf
   `/usr/sbin/vzctl set $i --onboot yes --save`;
   if ( $? != 0 )
   {
     print "command failed: failed to create configuration file for VE with id=$i\n";
     exit 1;
   }  
   #Setting hostname
   `/usr/sbin/vzctl set $i --hostname host_1001 --save`;
   if ( $? != 0 )
   {
     print "command failed: failed to set or save hostname for VE with id=$i\n";
     exit 1;
   }
   #Setting nameserver
   `/usr/sbin/vzctl set $i --nameserver 10.1.0.1 --save`;
   if ( $? != 0 )
   {
     print "command failed: failed to set nameserver VE with id=$i\n";
     exit 1;
   }
   #Setting origin_sample
   #`/usr/sbin/vzctl set $i --origin_sample vps.basic --save`;
   #if ( $? != 0 )
   #{
   #  print "command failed: failed to set origin_ORIGIN_SAMPLE="name" that is name of container sample configuration which the container is based on. VE with id=$i\n";
   #  exit 1;
   #}




   #Start the container
   `/usr/sbin/vzctl start $i`;
   if ( $? != 0 )
   {
     print "command failed: unable to start VE with id=$i\n";
     exit 1;
   }
}
print "VEs started sucessfully!\n";
exit 0;











