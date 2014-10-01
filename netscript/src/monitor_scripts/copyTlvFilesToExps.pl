#!/usr/bin/perl
#Usage: perl copyTlv.pl /primex/exps/AThirdJavaModel
use strict;
use warnings;

my $exptDir = $ARGV[0];
my $filename = '/tmp/pgc_debug_msg';

open(my $fh, '>>', $filename) or die "Could not open file '$filename' $!";
open (MYFILE, '/primex/machineFile');
while (<MYFILE>) {
chomp;
#print "$_\n";
my @answer = split(':', $_);
print "Copying tlv files to Machine:$answer[0] \n";
print $fh "Copying tlv files and primex-exps files to:$answer[0] \n";

`ssh $answer[0] 'mkdir -p $exptDir'`;
`scp $exptDir/* $answer[0]:$exptDir/`
}
close(MYFILE);
#print "1".$abc;
print "Script run successfully!\n";
exit 0;
