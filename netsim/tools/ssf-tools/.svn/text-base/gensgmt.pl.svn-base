#!/usr/bin/perl

#
# bin/gensgmt.pl :- generate data segments.
#
# This perl script processes all source files and lists global
# variables belonging to different data segments: shared, shared
# read-only, and private.
#

#
# Usage: perl gensgmt.pl $(CFILES) -C $(CPP) -F $(CFLAGS), where
#   $(CFILES) is the list of source files,
#   $(CPP) is the pre-processor (e.g., g++ -E)
#   $(CFLAGS) is compiler flags.
#

use strict;

# read in the file list (everything before -C)
my $CLIST = '';
while($ARGV[0] ne '-C') {
  my $file = shift @ARGV;
  $CLIST .= "$file ";
}
my @CFILES = split(/\s+/, $CLIST);
shift @ARGV; # pass -C

my $PREP = '';
while($ARGV[0] ne '-F') {
  my $prep = shift @ARGV;
  $PREP .= "$prep ";
}
shift @ARGV; # pass -F

# read in the preprocessor and its compiler flags
my $FLAGS = join(' ', @ARGV);
my $RUN_PREP = $PREP.$FLAGS;

# Filter out "SSF_DECLARE_*" from the preprocessed source file and
# dump them to the file .sseg. Note that GENSGMT is added, in
# which case 'SSF_DECLARE_*' macros will not be expanded by the
# preprocessor.
unlink ".sseg";
for(my $i=0; $i<scalar(@CFILES); $i++) {
  `${RUN_PREP} -DGENSGMT $CFILES[$i] | grep \"^SSF_DECLARE_\" >> .sseg`;
}

print <<EOF;
/*
 * This file has been automatically generated --- DO NOT EDIT
 */

#ifndef __PRIME_SSF_GLOBALS_H__
#define __PRIME_SSF_GLOBALS_H__

EOF

open(IN, ".sseg");
unlink ".sseg";

while(<IN>) {
  if(/SSF_DECLARE_INCLUDE\s*\((.*)\)/) {
    my $incfile = $1;
    $incfile =~ s/\s//g;
    print "#include ${incfile}\n";
  }
}

print <<EOF;

namespace prime {
namespace ssf {

  /******* shared data segment *******/

#if !SSF_SHARED_DATA_SEGMENT

  struct shared_segment {
EOF

seek(IN, 0, 0);
while(<IN>) {
  if(/SSF_DECLARE_SHARED\s*\(([^,]*),([^)]*)\)/) {
    my $type = $1; my $id = $2;
    $type =~ s/^\s+//g; $type =~ s/\s+$//g;
    $id =~ s/^\s+//g; $id =~ s/\s+$//g;
    if($type !~ /^extern/) {
      print "    $type $id;\n";
    }
  }
}
print "    SSF_CACHE_LINE_PADDING;\n";
print "  };\n";

print <<EOF;

#endif /*!SSF_SHARED_DATA_SEGMENT*/

EOF

seek(IN, 0, 0);
while(<IN>) {
  if(/SSF_DECLARE_SHARED\s*\(([^,]*),([^)]*)\)/) {
    my $type = $1; my $id = $2;
    $type =~ s/^\s+//g; $type =~ s/\s+$//g;
    $id =~ s/^\s+//g; $id =~ s/\s+$//g;
    if($type !~ /^extern/) {
      print "  extern $type ssf_shared_$id;\n";
    }
  }
}

print <<EOF;

  /******* private data segment *******/

#if SSF_SHARED_DATA_SEGMENT

  struct private_segment {
EOF

seek(IN, 0, 0);
while(<IN>) {
  if(/SSF_DECLARE_PRIVATE\s*\(([^,]*),([^)]*)\)/) {
    my $type = $1; my $id = $2;
    $type =~ s/^\s+//g; $type =~ s/\s+$//g;
    $id =~ s/^\s+//g; $id =~ s/\s+$//g;
    if($type !~ /^extern/) {
      print "    $type $id;\n";
    }
  }
}
print "    SSF_CACHE_LINE_PADDING;\n";
print "  };\n";

print <<EOF;

#endif /*SSF_SHARED_DATA_SEGMENT*/

EOF

seek(IN, 0, 0);
while(<IN>) {
  if(/SSF_DECLARE_PRIVATE\s*\(([^,]*),([^)]*)\)/) {
    my $type = $1; my $id = $2;
    $type =~ s/^\s+//g; $type =~ s/\s+$//g;
    $id =~ s/^\s+//g; $id =~ s/\s+$//g;
    if($type !~ /^extern/) {
      print "  extern $type ssf_private_$id;\n";
    }
  }
}

print <<EOF;

  /******* shared read-only data segment *******/

EOF

seek(IN, 0, 0);
while(<IN>) {
  if(/SSF_DECLARE_SHARED_RO\s*\(([^,]*),([^)]*)\)/) {
    my $type = $1; my $id = $2;
    $type =~ s/^\s+//g; $type =~ s/\s+$//g;
    $id =~ s/^\s+//g; $id =~ s/\s+$//g;
    if($type !~ /^extern/) {
      print "  extern $type ssf_shared_ro_$id;\n";
    }
  }
}

close(IN);

$FLAGS =~ s/\"/\\\"/g;
print "\n#define PRIME_SSF_CONFIG \"${FLAGS}\"\n\n";

print <<EOF;

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_GLOBALS_H__*/
EOF
