#!/usr/bin/perl

print "PREDEFINED =";
foreach $f (@ARGV) {
  if($f =~ /-D([a-zA-Z\-_0-9]+)="(.+)"/) {
    print " \\\n\t\"$1=\\\"$2\\\"\"";
  } elsif($f =~ /-D([a-zA-Z\-_0-9]+)=(.+)/) {
    print " \\\n\t\"$1=$2\"";
  } elsif($f =~ /-D([a-zA-Z\-_0-9]+)/) {
    print " \\\n\t\"$1\"";
  }
}
print "\n"
