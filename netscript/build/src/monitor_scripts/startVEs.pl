#!/usr/bin/perl

use strict;
my $i;
my $from = $ARGV[0];
my $to   = $ARGV[1];

for($i=$from; $i<=$to; $i++){
   #Create configuration file before starting the container
   `cp /etc/vz/conf/template.conf /etc/vz/conf/$i.conf`;
   if ( $? != 0 )
   {
     print "command failed: failed to create configuration file for VE with id=$i\n";
     exit 1;
   }  

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











