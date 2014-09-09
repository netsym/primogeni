#!/usr/bin/perl

use strict;
#use city2latlong;
#use latlong2distance;

use rocketfuel;

#use parse_caida;
#use pacificlearc;
#use niprnet;

# these parameters are used for plotting the network in DML
use constant PI=>3.1415926;
use constant DML_SLANT=>(PI/2); # 60 degrees
use constant DML_YEXP=>(70*sin(DML_SLANT));
use constant VIEWPORT_XSIZE=>512;
use constant VIEWPORT_YSIZE=>512;
use constant VIEW_XSCALE=>(VIEWPORT_XSIZE/(180+180*cos(DML_SLANT)));
use constant VIEW_YSCALE=>(VIEWPORT_YSIZE/(2*70*sin(DML_SLANT)));

use constant PROP_SPEED=>1e8; # one third of light speed

sub usage {
  print "\nUsage: perl $0 [options] asnum0 [asnum1 ...]\n";
  print "Options:\n";
  print "  -help : this message\n";
  print "  -seed <seed> : set random seed (default: 6460676)\n";
  print "  -type [xml|gml|dml|html|uuf|txt] : output type (default: txt)\n";
  print "  -cut <cut-level> : cut level (defaul: -1 backbone routers only)\n";
  print "  -o <filename>: output file (default: standard output)\n";
  print "  -netgeo <filename>: cache file with IP's location info (default: netgeo.cache)\n";
  print "  -latlong <filename> : cache file with city's latitude and longitude (default: latlong.cache)\n";
  print "  -gml-no-label : no city labels in GML output\n";
  print "  -html-no-marker : no location marker in HTML output\n";
  print "  -dml-show-ip : assign IP address to interface in DML output\n";
  exit 1;
}

my $seed = 6460676;
my $output_type = 0; # output xml
my $cutoff = -1; # cut level
my $output_file = '-';
my $netgeo_cache_file = "netgeo.cache";
my $latlong_cache_file = "latlong.cache";
my $gml_no_label = 0;
my $html_no_marker = 0;
my $dml_show_ip = 0;

my @isplist = ();

print "perl $0";
foreach my $argv (@ARGV) { print " $argv"; }
print "\n";

my %isp2asnum =
  ( "AT&T" => 7018,
    "Cable&Wireless" => 3561,
    "Ebone" => 1755,
    "Exodus" => 3967,
    "Level3" => 3356,
    "Sprint" => 1239,
    "TATACOMM" => 4755,
    "Telstra" => 1221,
    "Tiscali" => 3257,
    "UUNET701" => 701,
    "UUNET703" => 703,
    "Verio" => 2914,
    "Abovenet" => 6461,
  );

my %asnum2isp;
foreach my $asname (keys %isp2asnum) {
  if($isp2asnum{$asname} > 0) {
    $asnum2isp{$isp2asnum{$asname}} = $asname;
  }
}

while(scalar(@ARGV) > 0) {
  my $argv = shift @ARGV;
  if($argv =~ /^-/) {
    if($argv eq '-help') { usage(); }
    elsif($argv eq '-seed') {
      my $t = shift @ARGV;
      if(defined $t) { $seed = $t; }
      else { print STDERR "invalid -seed option\n"; usage(); }
    } elsif($argv eq '-type') {
      my $t = shift @ARGV;
      if(defined $t) {
	if(lc($t) eq 'xml') { $output_type |= 1; }
	elsif(lc($t) eq 'dml') { $output_type |= 2; }
	elsif(lc($t) eq 'gml') { $output_type |= 4; }
	elsif(lc($t) eq 'html') { $output_type |= 8; }
	elsif(lc($t) eq 'uuf') { $output_type |= 16; }
	elsif(lc($t) eq 'txt') { $output_type |= 32; }
	else { print STDERR "unrecognized output type\n"; usage(); }
      } else { print STDERR "invalid -type option\n"; usage(); }
    } elsif($argv eq '-cut') {
      my $t = shift @ARGV;
      if(defined $t) { $cutoff = $t; }
      else { print STDERR "invalid -cut option\n"; usage(); }
    } elsif($argv eq '-o') {
      my $t = shift @ARGV;
      if(defined $t) { $output_file = $t; }
      else { print STDERR "invalid -o option\n"; usage(); }
    } elsif($argv eq '-netgeo') {
      my $t = shift @ARGV;
      if(defined $t) { $netgeo_cache_file = $t; }
      else { print STDERR "invalid -netgeo option\n"; usage(); }
    } elsif($argv eq '-latlong') {
      my $t = shift @ARGV;
      if(defined $t) { $latlong_cache_file = $t; }
      else { print STDERR "invalid -latlong option\n"; usage(); }
    } elsif($argv eq '-gml-no-label') {
      $gml_no_label = 1;
    } elsif($argv eq '-html-no-marker') {
      $html_no_marker = 1;
    } elsif($argv eq '-dml-show-ip') {
      $dml_show_ip = 1;
    } else { print STDERR "unrecognized option: $argv\n"; usage(); }
  } else {
    if($argv =~ /\d+/ || $argv eq "all") {
      push @isplist, $argv;
    } else {
      print STDERR "unrecognized option: $argv\n";
      usage();
    }
  }
}
if(scalar(@isplist) == 0) { usage(); }
elsif(scalar(@isplist) == 1) {
  my $myarg = shift @isplist;
  if($myarg eq "all") {
    @isplist = values %isp2asnum;
  } else { push @isplist, $myarg; }
}

if($output_type == 0) { $output_type = 32; }
if(($output_type&1) != 0 && $output_file =~ /(.*)\.xml$/ ||
   ($output_type&2) != 0 && $output_file =~ /(.*)\.dml$/ ||
   ($output_type&4) != 0 && $output_file =~ /(.*)\.gml$/ ||
   ($output_type&8) != 0 && ($output_file =~ /(.*)\.html$/ || $output_file =~ /(.*)\.htm$/) ||
   ($output_type&16) != 0 && $output_file =~ /(.*)\.uuf$/ ||
   ($output_type&32) != 0 && $output_file =~ /(.*)\.txt$/) {
  $output_file = $1;
}

srand($seed);

#my $min_latency = 0.003;
#my $collocation_lat = 0.003;
#my $collocation_bdw = 2.488e9; # OC48
#my $peeringat_lat = 0.005;
#my $peeringat_bdw = 6.22e8; # OC12
#my $peeringbetween_bdw = 1.55e8; # OC3
#my $unmatch_bdw = 1.55e8; # OC3
#my %unmatch_bdw = ();
#my $mil_attach_bdw = 4.5e7; # T3

my $laninfo = {
  'ornl9177' => {
    'pdf' => 0.55,
    'filename' => 'ornl9177.xml',
    'nnodes' => 9177,
    'nlinks' => 1281,
    'cidrsize' => 65536,
    'attachid' => 430,
    'attachnhi' => '11(4)',
    'attachip' => ip2num('0.0.255.254')
  },

  'dartmouth3886' => {
    'pdf' => 0.3,
    'filename' => 'dartmouth3886.xml',
    'nnodes' => 3886,
    'nlinks' => 469,
    'cidrsize' => 16384,
    'attachid' => 7,
    'attachnhi' => '0(0)',
    'attachip' => ip2num('0.0.63.254')
  },

  'campus538' => {
    'pdf' => 0.15,
    'filename' => 'campus538.xml',
    'nnodes' => 538,
    'nlinks' => 87,
    'cidrsize' => 4096,
    'attachid' => 0,
    'attachnhi' => '2:0(3)',
    'attachip' => ip2num('0.0.15.254')
  },

  'campus610' => {
    'pdf' => 0.0,
    'filename' => 'campus610.xml',
    'nnodes' => 610,
    'nlinks' => 87,
    'cidrsize' => 4096,
    'attachid' => 0,
    'attachnhi' => '2:0(3)',
    'attachip' => ip2num('0.0.15.254')
  }
};

#my %peering_conn_routers = ( # peering connectivity test routers, cutlevel 0
#			     1239 => 3251, # TACOMA,WA
#			     2914 => 335, # VIENNA,VA
#			     3356 => 198, # DENVER,CO
#			     3967 => 163, # EL SEGUNDO,CA
#			     6461 => 399, # VIENNA,VA
#			     7018 => 12426, # ABINGDON,VA
#			     );
			     
my %isphashlist = cleanup_isplist(@isplist);

# get rocketfuel router map (strip those isps not in caida)
print STDERR "reading rocketfuel raw data...\n";
my @result = rocketfuel::get_router_level_map
  ($netgeo_cache_file, $latlong_cache_file, keys %isphashlist);
my %rocketfuel_neighbors = % { shift @result };
my %rocketfuel_routers = %{ shift @result };
my %rocketfuel_ext_routers = %{ shift @result };
my %rocketfuel_peering = %{ shift @result };

# this is the data structure that eventually contains everything
# needed for the topology;
#
#   $everything_as{$asnum}->{name} : the name of the AS
#   $everything_as{$asnum}->{int_routers} : number of internal routers
#   $everything_as{$asnum}->{acc_routers} : number of access routers
#   $everything_as{$asnum}->{int_links} : number of links between internal routers
#   $everything_as{$asnum}->{acc_links} : number of links between internal and access routers
#   $everything_as{$asnum}->{ext_links} : number of links to routers of other ASes
#
#   $everything_rt{$asnum}{0..n-1}->{ipaddrs}: list of ip addresses of this router
#   $everything_rt{$asnum}{0..n-1}->{aliases}: list of aliases (dns names or ip addresses)
#   $everything_rt{$asnum}{0..n-1}->{cutoff}: distance from a named gateway or backbone router
#   $everything_rt{$asnum}{0..n-1}->{location}: the router's location (CITY,STATE,COUNTRY)
#   $everything_rt{$asnum}{0..n-1}->{latlong}: the router's location in lattitude and longitude
#
#   $everything_rt{$asnum}{-1..-m}->{alias}: alias of the access router
#   $everything_rt{$asnum}{-1..-m}->{cutoff}: distance from a named gateway or backbone router
#
#   $everything_ln{$asnum}{0..n-1}{$toasnum}{0..n'-1}->{latency}: link latency in milliseconds
#   $everything_ln{$asnum}{0..n-1}{$toasnum}{0..n'-1}->{height}: link weight (for routing)
#   $everything_ln{$asnum}{0..n-1}{$toasnum}{0..n'-1}->{linktype}: link type (interior or exterior, between what cut levels)
#
#   $everything_ln{$asnum}{0..n-1}{$asnum}{-1..-m}->{latency}: link latency in milliseconds
#   $everything_ln{$asnum}{0..n-1}{$asnum}{-1..-m}->{height}: link weight (for routing)
#   $everything_ln{$asnum}{0..n-1}{$asnum}{-1..-m}->{linktype}: link type (access, between what cut levels)
#
my %everything_as;
my %everything_rt;
my %everything_ln;

my %rt_idx;
my %acc_rt_idx;
my %ln_idx;
my %ext_ln_idx;
my %acc_ln_idx;

# make the cuts and remove isolated routers
foreach my $asnum (sort {$a<=>$b} keys %rocketfuel_neighbors) {
  my $asname = $asnum2isp{$asnum};
  foreach my $uid (sort {$a<=>$b} keys %{$rocketfuel_neighbors{$asnum}}) {
    if(!defined $rocketfuel_routers{$asnum}{$uid}->{cutoff} ||
       $rocketfuel_routers{$asnum}{$uid}->{cutoff}>$cutoff) { next; }
    foreach my $toasnum (sort {$a<=>$b} keys %{$rocketfuel_neighbors{$asnum}{$uid}}) {
      my $toasname;
      if($toasnum > 0) { $toasname = $asnum2isp{$toasnum}; }
      else { $toasname = "anonymous"; }
      foreach my $touid (sort {$a<=>$b} keys %{$rocketfuel_neighbors{$asnum}{$uid}{$toasnum}}) {
	if($touid >= 0 && 
	   (!defined $rocketfuel_routers{$toasnum}{$touid}->{cutoff} ||
	    $rocketfuel_routers{$toasnum}{$touid}->{cutoff}>$cutoff)) { next; }

	# source node first

	if(!defined $everything_as{$asnum}->{name}) 
	  { $everything_as{$asnum}->{name} = $asname; }

	my $newuid;
	if(defined $rocketfuel_routers{$asnum}{$uid}->{newuid}) {
	  $newuid = $rocketfuel_routers{$asnum}{$uid}->{newuid};
	} else {
	  if(!defined $rt_idx{$asnum}) { $rt_idx{$asnum} = 0; }
	  $newuid = $rocketfuel_routers{$asnum}{$uid}->{newuid} = $rt_idx{$asnum};
	  $rt_idx{$asnum}++;
	}

	if(!defined $everything_rt{$asnum}{$newuid}) {
	  #print "new router $asnum:$newuid (was $asnum:$uid)\n";
	  $everything_rt{$asnum}{$newuid}->{ipaddrs} = $rocketfuel_routers{$asnum}{$uid}->{ipaddrs};
	  $everything_rt{$asnum}{$newuid}->{aliases} = $rocketfuel_routers{$asnum}{$uid}->{aliases};
	  $everything_rt{$asnum}{$newuid}->{cutoff} = $rocketfuel_routers{$asnum}{$uid}->{cutoff};
	  $everything_rt{$asnum}{$newuid}->{location} = $rocketfuel_routers{$asnum}{$uid}->{location};
	  $everything_rt{$asnum}{$newuid}->{latlong} = $rocketfuel_routers{$asnum}{$uid}->{latlong};
	}

	# destination node second

	if($touid < 0) {
	  if($asnum != $toasnum) { die "unexpected"; }
	  my $accuid;
	  if(defined $rocketfuel_ext_routers{$asnum}{-$touid}->{newuid}) {
	    $accuid = $rocketfuel_ext_routers{$asnum}{-$touid}->{newuid};
	  } else {
	    if(!defined $acc_rt_idx{$asnum}) { $acc_rt_idx{$asnum} = -1; }
	    $accuid = $rocketfuel_ext_routers{$asnum}{-$touid}->{newuid} = $acc_rt_idx{$asnum};
	    $acc_rt_idx{$asnum}--;
	  }

	  if(!defined $everything_rt{$asnum}{$accuid}) {
	    #print "new router $asnum:$accuid (was $touid)\n";
	    $everything_rt{$asnum}{$accuid}->{alias} = $rocketfuel_ext_routers{$asnum}{-$touid}->{alias};
	    $everything_rt{$asnum}{$accuid}->{cutoff} = $rocketfuel_ext_routers{$asnum}{-$touid}->{cutoff};
	  }

	  #print "new link $asnum:$newuid ($uid) <-> $asnum:$accuid ($touid)\n";
	  $everything_ln{$asnum}{$newuid}{$asnum}{$accuid}->{latency} = 
	    $rocketfuel_neighbors{$asnum}{$uid}{$asnum}{$touid}->{latency};
	  $everything_ln{$asnum}{$newuid}{$asnum}{$accuid}->{weight} = 
	    $rocketfuel_neighbors{$asnum}{$uid}{$asnum}{$touid}->{weight};
	  $everything_ln{$asnum}{$newuid}{$asnum}{$accuid}->{linktype} = 
	    $rocketfuel_neighbors{$asnum}{$uid}{$asnum}{$touid}->{linktype};
	  if(!defined $acc_ln_idx{$asnum}) { $acc_ln_idx{$asnum} = 1; }
	  else { $acc_ln_idx{$asnum}++; }	    
	} else {
	  if(!defined $everything_as{$toasnum}->{name}) 
	    { $everything_as{$toasnum}->{name} = $toasname; }
	  
	  my $extuid;
	  if(defined $rocketfuel_routers{$toasnum}{$touid}->{newuid}) {
	    $extuid = $rocketfuel_routers{$toasnum}{$touid}->{newuid};
	  } else {
	    if(!defined $rt_idx{$toasnum}) { $rt_idx{$toasnum} = 0; }
	    $extuid = $rocketfuel_routers{$toasnum}{$touid}->{newuid} = $rt_idx{$toasnum};
	    $rt_idx{$toasnum}++;
	  }
	    
	  if(!defined $everything_rt{$toasnum}{$extuid}) {
	    #print "new router $toasnum:$extuid (was $touid)\n";
	    $everything_rt{$toasnum}{$extuid}->{ipaddrs} = $rocketfuel_routers{$toasnum}{$touid}->{ipaddrs};
	    $everything_rt{$toasnum}{$extuid}->{aliases} = $rocketfuel_routers{$toasnum}{$touid}->{aliases};
	    $everything_rt{$toasnum}{$extuid}->{cutoff} = $rocketfuel_routers{$toasnum}{$touid}->{cutoff};
	    $everything_rt{$toasnum}{$extuid}->{location} = $rocketfuel_routers{$toasnum}{$touid}->{location};
	    $everything_rt{$toasnum}{$extuid}->{latlong} = $rocketfuel_routers{$toasnum}{$touid}->{latlong};
	  }

	  #print "new link $asnum:$newuid ($uid) <-> $toasnum:$extuid ($touid)\n";
	  $everything_ln{$asnum}{$newuid}{$toasnum}{$extuid}->{latency} = 
	    $rocketfuel_neighbors{$asnum}{$uid}{$toasnum}{$touid}->{latency};
	  $everything_ln{$asnum}{$newuid}{$toasnum}{$extuid}->{weight} = 
	    $rocketfuel_neighbors{$asnum}{$uid}{$toasnum}{$touid}->{weight};
	  $everything_ln{$asnum}{$newuid}{$toasnum}{$extuid}->{linktype} = 
	    $rocketfuel_neighbors{$asnum}{$uid}{$toasnum}{$touid}->{linktype};
	  if($asnum == $toasnum) {
	    if(!defined $ln_idx{$asnum}) { $ln_idx{$asnum} = 1; }
	    else { $ln_idx{$asnum}++; }
	  } else {
	    if(!defined $ext_ln_idx{$asnum}) { $ext_ln_idx{$asnum} = 1; }
	    else { $ext_ln_idx{$asnum}++; }
	  }
	}
      }
    }
  }
  if(defined $rt_idx{$asnum}) { $everything_as{$asnum}->{int_routers} = $rt_idx{$asnum}; } 
  else { $everything_as{$asnum}->{int_routers} = 0; }
  if(defined $acc_rt_idx{$asnum}) { $everything_as{$asnum}->{acc_routers} = -$acc_rt_idx{$asnum}-1; }
  else { $everything_as{$asnum}->{acc_routers} = 0; }
  if(defined $ln_idx{$asnum}) { $everything_as{$asnum}->{int_links} = $ln_idx{$asnum}/2; }
  else { $everything_as{$asnum}->{int_links} = 0; }
  if(defined $acc_ln_idx{$asnum}) { $everything_as{$asnum}->{acc_links} = $acc_ln_idx{$asnum}; }
  else { $everything_as{$asnum}->{acc_links} = 0; }
  if(defined $ext_ln_idx{$asnum}) { $everything_as{$asnum}->{ext_links} = $ext_ln_idx{$asnum}; }
  else { $everything_as{$asnum}->{ext_links} = 0; }
}

if(($output_type&1) != 0) { # output xml
  die "XML output disabled.";
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.xml'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_xml();
  close(OUTPUT);
}

if(($output_type&2) != 0) { # output dml
  die "DML output disabled.";
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.dml'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_dml();
  close(OUTPUT);
}

if(($output_type&4) != 0) { # output gml
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.gml'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_gml();
  close(OUTPUT);
}

if(($output_type&8) != 0) { # output html
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.html'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_gmap();
  close(OUTPUT);
}

if(($output_type&16) != 0) { # output uuf
  die "UUF output disabled.";
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.uuf'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_uuf();
  close(OUTPUT);
}

if(($output_type&32) != 0) { # output txt
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.txt'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_everything();
  close(OUTPUT);
}



sub ip2num {
  my ($str) = @_;
  my ($a1, $a2, $a3, $a4) = split /\./, $str;
  if(!defined $a1 || !defined $a2 || !defined $a3 || !defined $a4) {
    die "ill-formed ip address!\n";
  }
  return ($a1<<24)+($a2<<16)+($a3<<8)+$a4;
}

sub cleanup_isplist {
  my @isplist = @_;
  my %ret;
  foreach my $isp (@isplist) {
    if(defined $asnum2isp{$isp} &&
       defined $isp2asnum{$asnum2isp{$isp}} &&
       $isp2asnum{$asnum2isp{$isp}} > 0) {
      $ret{$isp} = $asnum2isp{$isp};
    } else {
      print STDERR "unknown AS number: $isp, ignored...\n";
    }
  }
  if(scalar(keys %ret) == 0) { die "ERROR: empty ISP list!\n"; }
  print STDERR "processing ISP:";
  foreach my $isp (sort keys %ret) {
    print STDERR " $isp($asnum2isp{$isp})";
  }
  print STDERR "\n";
  return %ret;
}

sub print_everything {
  # for each AS number requested
  foreach my $asnum (sort { $a <=> $b } keys %everything_ln) {
    print OUTPUT "### AS ${asnum} ($everything_as{$asnum}->{name}) ###\n\n";

    my $n = $everything_as{$asnum}->{int_routers};
    my $m = $everything_as{$asnum}->{acc_routers};
    my $l = $everything_as{$asnum}->{int_links};
    my $la = $everything_as{$asnum}->{acc_links};
    my $le = $everything_as{$asnum}->{ext_links};
    my $lt = $l+$la+$le;

    print OUTPUT "-- INTERNAL ROUTERS: total=$n\n\n";
    foreach my $uid (sort { $a <=> $b } keys %{$everything_rt{$asnum}}) {
      if($uid < 0) { next; }
      print OUTPUT "$asnum:$uid ";
      print OUTPUT "$everything_rt{$asnum}{$uid}->{location} $everything_rt{$asnum}{$uid}->{latlong} ";
      my $nics = scalar(@{$everything_rt{$asnum}{$uid}->{ipaddrs}});
      print OUTPUT "i${nics} ";
      if($everything_rt{$asnum}{$uid}->{cutoff} < 0) { print OUTPUT "bb\n"; }
      else { print OUTPUT "r$everything_rt{$asnum}{$uid}->{cutoff}\n"; }
      for(my $x=0; $x<$nics; $x++) {
	my $ip = $everything_rt{$asnum}{$uid}->{ipaddrs}[$x];
	my $al = $everything_rt{$asnum}{$uid}->{aliases}[$x]; 
	print OUTPUT " $ip ($al)\n";
      }
      print OUTPUT "\n";
    }
    print OUTPUT "\n";

    print OUTPUT "-- EXTERNAL ROUTERS: total=$m\n\n";
    foreach my $uid (sort { $a <=> $b } keys %{$everything_rt{$asnum}}) {
      if($uid >= 0) { next; }
      print OUTPUT "$asnum:$uid ";
      print OUTPUT "$everything_rt{$asnum}{$uid}->{alias}";
      if($everything_rt{$asnum}{$uid}->{cutoff} < 0) { print OUTPUT " bb\n"; }
      else { print OUTPUT " r$everything_rt{$asnum}{$uid}->{cutoff}\n"; }
    }
    print OUTPUT "\n";

    print OUTPUT "-- CONNECTIVITY: int=$l, acc=$la, ext=$le, total=$lt\n\n";
    foreach my $uid (sort { $a <=> $b } keys %{$everything_ln{$asnum}}) {
      foreach my $nasnum (sort { $a <=> $b } keys %{$everything_ln{$asnum}{$uid}}) {
	foreach my $nuid (sort { $a <=> $b } keys %{$everything_ln{$asnum}{$uid}{$nasnum}}) {
	  print OUTPUT "$asnum:$uid <=> $nasnum:$nuid ";
	  print OUTPUT "($everything_ln{$asnum}{$uid}{$nasnum}{$nuid}->{latency},";
	  print OUTPUT "$everything_ln{$asnum}{$uid}{$nasnum}{$nuid}->{weight},";
	  print OUTPUT "$everything_ln{$asnum}{$uid}{$nasnum}{$nuid}->{linktype})\n";
	}
      }
    }
    print OUTPUT "\n";
  }
}

sub gml_latlong2pos {
  my ($lat, $long) = @_;
  my ($latcenter, $longcenter) = (35.0, 90.0);

  my $x; my $y;
  if($lat =~ /(\d+):(\d+)([NS])/) {
    my $d = $1;
    my $m = $2;
    if($3 eq 'S') { $d = -$d; $m = -$m; }
    $y = ($d-$latcenter)*60+$m;
  }
  if($long =~ /(\d+):(\d+)([EW])/) {
    my $d = $1;
    my $m = $2;
    if($3 eq 'W') { $d = -$d; $m = -$m; }
    $x = ($d-$longcenter)*60+$m;
  }

  return ($x, $y);
}

sub print_gml {
  my $dim = 5.0;
  my %asnuid2id;

  print OUTPUT "graph [\n";
  my $z = 0.0;
  my $id = 0;
  foreach my $asnum (sort {$a<=>$b} keys %everything_rt) {
    foreach my $uid (sort {$a<=>$b} keys %{$everything_rt{$asnum}}) {
      if($uid < 0) { next; }
      my $label = "$everything_rt{$asnum}{$uid}->{location}($asnum:$uid)";
      if($gml_no_label) { $label = ' '; }
      my ($lat, $long) = split /,/, $everything_rt{$asnum}{$uid}->{latlong};
      my ($x, $y) = gml_latlong2pos($lat, $long);

      print OUTPUT "  node [\n";
      print OUTPUT "    id $id label \"$label\"\n";
      print OUTPUT "    graphics [\n";
      print OUTPUT "      center [ x $x y $y z $z ]\n";
      print OUTPUT "      width $dim height $dim depth $dim\n";
      print OUTPUT "    ]\n";
      print OUTPUT "    vgj [ labelPosition \"Below\" shape \"Oval\" ]\n";
      print OUTPUT "  ]\n";

      $asnuid2id{$asnum}{$uid} = $id++;
    }
    $z += 1000.0;
  }

  foreach my $asnum (sort {$a<=>$b} keys %everything_ln) {
    foreach my $uid (sort {$a<=>$b} keys %{$everything_ln{$asnum}}) {
      foreach my $toasnum (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}}) {
	foreach my $touid (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}{$toasnum}}) {
	  if($touid < 0) { next; }
	  print OUTPUT "  edge [ source $asnuid2id{$asnum}{$uid} target $asnuid2id{$toasnum}{$touid} ]\n";
	}
      }
    }
  }

  print OUTPUT "]\n";
}

sub print_gmap {
  my $level = 2;
  my $lat_min = 180; my $lat_max = -180;
  my $long_min = 120; my $long_max = -240;
  my %locinfo; # so that only one marker is used for each location
  foreach my $asnum ( sort {$a <=> $b} keys %everything_rt) {
    foreach my $uid ( sort {$a <=> $b} keys %{$everything_rt{$asnum}}) {
      if($uid < 0) { next; }
      my $location = $everything_rt{$asnum}{$uid}->{location};
      if(!defined $locinfo{$location}) {
	#get latitude and longtitude
	my ($lat, $long);
	($lat, $long) = split /,/, $everything_rt{$asnum}{$uid}->{latlong};
	my ($lat_degree, $lat_minute, $long_degree, $long_minute);
	($lat_degree, $lat_minute) = split /:/, $lat;
	($long_degree, $long_minute) = split /:/, $long;
	if (uc($lat_minute) =~ /S/) {
	  $lat_degree = 0 - $lat_degree;
	  $lat_minute =~ s/[Ss]//;
	  $lat_minute = 0 - $lat_minute;
	} elsif (uc($lat_minute) =~ /N/) { $lat_minute =~ s/[Nn]//; }
	else { die "ill-formed latitude: $lat"; }
	if (uc($long_minute) =~ /W/) {
	  $long_degree = 0 - $long_degree;
	  $long_minute =~ s/[Ww]//;
	  $long_minute = 0 - $long_minute;
	} elsif (uc($long_minute) =~ /E/) { $long_minute =~ s/[Ee]//; }
	else { die "ill-formed longitude: $long"; }
	$lat = sprintf "%.4f",  $lat_degree+$lat_minute/60;
	$long = sprintf "%.4f", $long_degree+$long_minute/60;
	$locinfo{$location}->{latitude} = $lat;
	$locinfo{$location}->{longitude} = $long;
	if($lat < $lat_min) { $lat_min = $lat; }
	if($lat > $lat_max) { $lat_max = $lat; }
	my $mylong = $long; if($long > 120) { $mylong = $long-360; }
	if($mylong < $long_min) { $long_min = $mylong; }
	if($mylong > $long_max) { $long_max = $mylong; }
      }
      push @{$locinfo{$location}->{routers}}, "$asnum:$uid ($everything_rt{$asnum}{$uid}->{cutoff})";
    }
  }
  my $latcenter = ($lat_min+$lat_max)/2;
  my $longcenter = ($long_min+$long_max)/2;

  #print OUTPUT "\n==========Start to output Google map script============\n";

  print OUTPUT '<!DOCTYPE html "-//W3C//DTD XHTML 1.0 Strict//EN" '."\n";
  print OUTPUT '  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">'."\n";
  print OUTPUT '<html xmlns="http://www.w3.org/1999/xhtml">'."\n";
  print OUTPUT '  <head>'."\n";
  print OUTPUT '    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>'."\n";
  print OUTPUT '    <title>RocketFuel Router Level Topology</title>'."\n";
  print OUTPUT '    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=abcdefg&sensor=false"'."\n";
  print OUTPUT '            type="text/javascript"></script>'."\n";
  print OUTPUT '    <script type="text/javascript">'."\n";
  print OUTPUT '      function initialize() {'."\n";
  print OUTPUT '        if (GBrowserIsCompatible()) {'."\n";
  print OUTPUT '          var map = new GMap2(document.getElementById("map_canvas"));'."\n";
  print OUTPUT '          map.setCenter(new GLatLng('.$latcenter.', '.$longcenter.'), '.$level.');'."\n";
  print OUTPUT '          var mapControl = new GMapTypeControl();'."\n";
  print OUTPUT '          map.addControl(mapControl);'."\n";
  print OUTPUT '          map.addControl(new GLargeMapControl());'."\n";
  print OUTPUT '          function createMarker (mypos, mytitle,myinfo) {'."\n";
  print OUTPUT '            var marker = new GMarker(mypos, {title: mytitle});'."\n";
  print OUTPUT '            GEvent.addListener(marker, "click", function() {'."\n";
  print OUTPUT '              marker.openInfoWindowHtml(myinfo);'."\n";
  print OUTPUT '            });'."\n";
  print OUTPUT '            return marker;'."\n";
  print OUTPUT '          }'."\n";

  my %loc2loc; # so that only one line between two distinct locations
  foreach my $asnum (sort {$a<=>$b} keys %everything_ln) {
    foreach my $uid (sort {$a<=>$b} keys %{$everything_ln{$asnum}}) {
      my $loc1 = $everything_rt{$asnum}{$uid}->{location};
      foreach my $toasnum (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}}) {
	foreach my $touid (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}{$toasnum}}) {
	  if($touid < 0) { next; }
	  my $loc2 = $everything_rt{$toasnum}{$touid}->{location};
	  if($loc1 ne $loc2 &&
	     ($locinfo{$loc1}->{latitude} != $locinfo{$loc2}->{latitude} ||
	      $locinfo{$loc1}->{longitude} != $locinfo{$loc2}->{longitude}) &&
	     !defined $loc2loc{$loc1}{$loc2}) {
	    $loc2loc{$loc1}{$loc2} = $loc2loc{$loc2}{$loc1} = 1;
	    #print polyline
	    print OUTPUT "          map.addOverlay(new GPolyline([new GLatLng($locinfo{$loc1}->{latitude}, $locinfo{$loc1}->{longitude}), ";
	    print OUTPUT "new GLatLng($locinfo{$loc2}->{latitude}, $locinfo{$loc2}->{longitude})], \"#ff0000\", 1));\n";
	  }
        }
      }
    }
  }

  if(!$html_no_marker) {
    foreach my $location (keys %locinfo) {
      #print marker
      print OUTPUT "          map.addOverlay(createMarker(new GLatLng($locinfo{$location}->{latitude}, $locinfo{$location}->{longitude}),\n";
      print OUTPUT "           \"$location ($locinfo{$location}->{latitude}, $locinfo{$location}->{longitude})\",\n";
      my $str = '';
      my $nrts = scalar(@{$locinfo{$location}->{routers}});
      for(my $i=0; $i<$nrts; $i++) {
	$str .=  $locinfo{$location}->{routers}[$i];
	if(($i+1)%3 == 0) { $str .= "<br>"; }
	elsif($i < $nrts-1) { $str .= ", "; }
      }
      print OUTPUT "           \"$location ($locinfo{$location}->{latitude}, $locinfo{$location}->{longitude})<br>$str\"));\n";
    }
  }

  print OUTPUT '        }'."\n";
  print OUTPUT '      }'."\n";
  print OUTPUT '    </script>'."\n";
  print OUTPUT '  </head>'."\n";
  print OUTPUT '  <body onload="initialize()" onunload="GUnload()">'."\n";
  print OUTPUT '    <div id="map_canvas" style="width: 1200px; height: 750px"></div>'."\n";
  print OUTPUT '  </body>'."\n";
  print OUTPUT '</html>'."\n";
}

