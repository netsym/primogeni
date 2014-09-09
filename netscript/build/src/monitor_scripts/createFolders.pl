#!/usr/bin/perl

use strict;

my $i;
my $from = $ARGV[0];
my $to   = $ARGV[1];
my $funionfs = 0;

print "from $from to $to\n";

my $argc;
$argc = @ARGV;
if ($argc==3){
        $funionfs = 1;
} else {
        $funionfs = 0;
}
print "number of parameters $argc funionfs=$funionfs\n";

`modprobe fuse`;
`mkdir /usr/local/primo/vz/private`;
`echo "[1] [$to]" >> /tmp/copy_cmd_executed.out`;
if ( $? != 0 && $? != 256 )
{
  `echo "[2] [$to]" >> /tmp/copy_cmd_executed.out`;
  print "command failed: $!\n";
  exit 1;
}
`mkdir -p /usr/local/primo/vz/private-rw`;
if ( $? != 0 && $? != 256 )
{
  `echo "[3] [$to]" >> /tmp/copy_cmd_executed.out`;
  print "command failed: $!\n";
  exit 1;
}

#`rm -rf /usr/local/primo/vz/private`;
`echo "copying from $from to $to" >> /tmp/copy_cmd_executed.out`;
`echo "[4] [$to]" >> /tmp/copy_cmd_executed.out`;
for($i=$from; $i<=$to; $i++){
   `mkdir -p /usr/local/primo/vz/private/$i`;
   if ( $? != 0 )
   {
     `echo "[5] [$to]" >> /tmp/copy_cmd_executed.out`;
     print "command failed: $!\n";
     exit 1;
   }
   `echo "[6] [$to]" >> /tmp/copy_cmd_executed.out`;
   #`/bin/rm -rf /usr/local/primo/vz/private/$i`;
   #`/bin/rm -rf /usr/local/primo/vz/private-rw/$i`;
   `mkdir -p /usr/local/primo/vz/private-rw/$i`;
   if ( $? != 0 )
   {
     `echo "[7] [$to]" >> /tmp/copy_cmd_executed.out`;
     print "command failed: $!\n";
     exit 1;
   }
   if ($funionfs==1){
        `echo "[8] [$to]" >> /tmp/copy_cmd_executed.out`;
        `/usr/local/bin/funionfs -o nonempty -o dirs=/primex/current_template/=RO:/usr/local/primo/vz/private-rw/$i -o allow_other NONE /usr/local/primo/vz/private/$i`;
   } else {
        `echo "[9] [$to]" >> /tmp/copy_cmd_executed.out`;
	#`echo "cp -rf /primex/current_template/* /usr/local/primo/vz/private/$i" >> /tmp/copy_cmd_executed.out`;
	#`cp -rf /primex/current_template/* /usr/local/primo/vz/private/$i`;
	#`sleep 1`;
	`echo "cp -rf /primex/current_template/* /usr/local/primo/vz/private/$i" >> /tmp/copy_cmd_executed.out`;
	`cp -rf /primex/current_template/* /usr/local/primo/vz/private/$i`;
   }

   #if ( $? != 0 )
   #{
     #`echo "[10] [$to]" >> /tmp/copy_cmd_executed.out`;
     #print "command failed: $!\n";
     #exit 1;
   #}
}
print "Script run successfully!\n";
`echo "[11] [$to]" >> /tmp/copy_cmd_executed.out`;
exit 0;
