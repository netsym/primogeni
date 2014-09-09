#!/usr/bin/perl

# use module
use XML::Simple;
use Data::Dumper;

# create object
$xml = new XML::Simple;

# read XML file
$data = $xml->XMLin("export.xml");

# print output
open(OUTFILE, ">outfile");
print OUTFILE Dumper($data);
close(OUTFILE);


