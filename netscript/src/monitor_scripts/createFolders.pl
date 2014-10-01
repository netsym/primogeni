#!/usr/bin/perl
#Usage: perl /primex/scripts/createFolders.pl 237 237

use strict;

my $i;
my $from = $ARGV[0];
my $to   = $ARGV[1];

print "from $from to $to\n";


`modprobe fuse`;
print "Script run successfully!\n";
`echo "[11] [$to]" >> /tmp/pgc_copy_cmd_executed.out`;
exit 0;
