#!/usr/bin/perl

use strict;

my $hostname = `hostname`;
my @t = split(/\./,$hostname);
my $slice_name=$t[1];
my $slice_dir="/proj/emulab-net/exp/$slice_name/exps";
print "$slice_dir\n";
`mkdir -p $slice_dir`;
`rm -f /primex/exps`;
`ln -s $slice_dir /primex/exps`;
`ls -la /primex/exps`;
