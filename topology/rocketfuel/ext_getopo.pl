#!/usr/bin/perl -w

use strict;
#use city2latlong;
#use latlong2distance;
use ext_rocketfuel;

#use parse_caida;
#use pacific;
#use niprnet;

#use xml module
#use XML::Generator::DOM;
#use XML::Twig;
use XML::Simple;
use Data::Dumper;

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
  print "  -type [xml|gml|dml|html|uuf|txt|denoise] : output type (default: txt)\n";
  print "  -cut <cut-level> : cut level (defaul: -1 backbone routers only)\n";
  print "  -o <filename>: output file (default: standard output)\n";
  print "  -netgeo <filename>: cache file with IP's location info (default: netgeo.cache)\n";
  print "  -latlong <filename> : cache file with city's latitude and longitude (default: latlong.cache)\n";
  print "  -gml-no-label : no city labels in GML output\n";
  print "  -html-no-marker : no location marker in HTML output\n";
  print "  -only-US: show only routers in HTML output\n";
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
my $onlyUS = 0;
my $dml_show_ip = 0;
my %alias_loc;
my $alias_loc_file = "alias_loc.in";

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
        elsif(lc($t) eq 'denoise') { $output_type |= 64; }
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
    } elsif($argv eq '-only-US') {
      $onlyUS = 1;
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
   ($output_type&32) != 0 && $output_file =~ /(.*)\.txt$/ ||
   ($output_type&64) != 0 && ($output_file =~ /(.*)\.html$/ || $output_file =~ /(.*)\.htm$/)) {
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
my @result = ext_rocketfuel::get_router_level_map
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
  #die "XML output disabled.";
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
#  die "UUF output disabled.";
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.uuf'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_uuf_undirected_withoutACC();
  close(OUTPUT);
}

if(($output_type&32) != 0) { # output txt
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.txt'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_everything();
  close(OUTPUT);
}

if(($output_type&64) != 0) { # output denoise
  my $file = $output_file;
  if($file ne '-') { $file = $output_file.'.distance.html'; }
  open(OUTPUT, ">$file") || die "can't open $file\n";
  print_getDistance_gmap();
  close(OUTPUT);
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

#print xml function
sub print_xml
{
  my $net = 'sub';
  my $interface = 'if';
  my $router = 'r';

  my $type_net = 'Net';
  my $type_router = 'Router';
  my $type_if = 'Interface';
  my $type_link = 'Link'; 
   
  my $bit_rate = 'bit_rate';
  my $delay = 'delay';

  print STDERR "Start to create xml...\n";
  my $hash_ref = {};

  #create top net
  $hash_ref->{'name'} = 'topnet';
  $hash_ref->{'type'} = $type_net;
  
  #create AS
  foreach my $asnum (sort { $a <=> $b } keys %everything_as) 
  {
    $hash_ref->{'node'}->{$net . $asnum}
             ->{'type'} = $type_net;   
  }
  	
  print $hash_ref;
  #create router
  foreach my $asnum (sort { $a<=>$b } keys %everything_rt) 
  {
    foreach my $uid (sort { $a<=>$b } keys %{$everything_rt{$asnum}}) 
    {
      if($uid < 0) { next; }
      $hash_ref->{'node'}->{$net . $asnum}
                 ->{'node'}->{$router . $uid}
                 ->{'type'} = $type_router;
    }
  }  
  
  #create interface and links
  my $linknum = 0;
  foreach my $asnum (sort {$a<=>$b} keys %everything_ln) 
  {
    foreach my $uid (sort {$a<=>$b} keys %{$everything_ln{$asnum}}) 
    {
      foreach my $toasnum (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}}) 
      {
        foreach my $touid (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}{$toasnum}}) 
        {
          if ($touid >= 0) 
          {
	        $linknum++;
            my $lat = $everything_ln{$asnum}{$uid}{$toasnum}{$touid}->{latency}/1000;
            my $weight = $everything_ln{$asnum}{$uid}{$toasnum}{$touid}->{weight};
            
            if ($asnum < $toasnum or ( $asnum == $toasnum and $uid < $touid) or 
                !defined $everything_ln{$toasnum}{$touid}{$asnum}{$uid})
            {
              #create interface node
              my $ifname1;
	      my $ifname2;
	      if ($asnum eq $toasnum)
	      {
		$ifname1 = $interface . '_' . $router . $touid;
		$ifname2 = $interface . '_' . $router . $uid;
	      } 
	      else 
              {
		$ifname1 = $interface . '_' . $net . $toasnum . '_' . $router . $touid;
		$ifname2 = $interface . '_' . $net . $asnum . '_' . $router . $uid;
	      }      
              
              #create interface 1
              $hash_ref->{'node'}->{$net . $asnum}
                         ->{'node'}->{$router . $uid}
                           ->{'node'}->{$ifname1}
                           ->{'type'} = $type_if;

              #create bit_reate
              $hash_ref->{'node'}->{$net . $asnum}
                         ->{'node'}->{$router . $uid}
                           ->{'node'}->{$ifname1}
                             ->{'attribute'}->{$bit_rate}
                             ->{'value'} = $weight;
            
              #create latency 
              $hash_ref->{'node'}->{$net . $asnum}
                         ->{'node'}->{$router . $uid}
                           ->{'node'}->{$ifname1}
                             ->{'attribute'}->{$delay}
			     ->{'value'} = $lat;    

              #create interface 2
              $hash_ref->{'node'}->{$net . $toasnum}
                         ->{'node'}->{$router . $touid}
                           ->{'node'}->{$ifname2}
                           ->{'type'} = $type_if;
	      #create bit_reate
              $hash_ref->{'node'}->{$net . $toasnum}
                         ->{'node'}->{$router . $touid}
                           ->{'node'}->{$ifname2}
                             ->{'attribute'}->{$bit_rate}
                             ->{'value'} = $weight;

              #create latency 
	      $hash_ref->{'node'}->{$net . $toasnum}
                         ->{'node'}->{$router . $touid}
                           ->{'node'}->{$ifname2}
                             ->{'attribute'}->{$delay}
			     ->{'value'} = $lat;
 
              #create link node
	      if ($asnum eq $toasnum) 
	      {
		my $lname = $router . $uid . '_' . $router . $touid;
                my $ref1name = $router . $uid;
	        my $ref2name = $router . $touid;

                my $path1 = '..:' . $router . $uid . ':' . 
		                    $interface . '_' . $router . $touid;
                my $path2 = '..:' . $router . $touid . ':' . 
		                    $interface . '_' . $router . $uid;

                $hash_ref->{'node'}->{$net . $asnum}
                           ->{'node'}->{$lname}
                             ->{'attribute'}->{$bit_rate}
                             ->{'value'} = $weight;

                $hash_ref->{'node'}->{$net . $asnum}
                           ->{'node'}->{$lname}
                             ->{'attribute'}->{$delay}
                             ->{'value'} = $lat;

                $hash_ref->{'node'}->{$net . $asnum}
                           ->{'node'}->{$lname}
                             ->{'ref'}->{$ref1name}
                             ->{'path'} = $path1;

                $hash_ref->{'node'}->{$net . $asnum}
                           ->{'node'}->{$lname}
                             ->{'ref'}->{$ref2name}
                             ->{'path'} = $path2;

                $hash_ref->{'node'}->{$net . $asnum}
                           ->{'node'}->{$lname}
                           ->{'type'} = $type_link;
	      }
	      else
	      {
		my $lname = $net . $asnum . '_' . $net . $toasnum . '_' . $linknum;
		my $ref1name = $net . $asnum;
		my $ref2name = $net . $toasnum;
                
                my $path1 = '..:' . $net . $asnum . ':' . 
		                  $router . $uid . ':' . 
		                  $interface . '_' . $net . $toasnum . '_' . $router . $touid;
	        my $path2 = '..:' . $net . $toasnum . ':' . 
		                  $router . $touid . ':' . 
		                  $interface . '_' . $net . $asnum . '_' . $router . $uid;
                
                $hash_ref->{'node'}->{$lname}
                           ->{'attribute'}->{$bit_rate}
                           ->{'value'} = $weight;

                $hash_ref->{'node'}->{$lname}
                           ->{'attribute'}->{$delay}
                           ->{'value'} = $lat;
             
                $hash_ref->{'node'}->{$lname}
                           ->{'ref'}->{$ref1name}
                           ->{'path'} = $path1;

                $hash_ref->{'node'}->{$lname}
                           ->{'ref'}->{$ref2name}
                           ->{'path'} = $path2;

                $hash_ref->{'node'}->{$lname}
                         ->{'type'} = $type_link;
	      }
            }
          }
        }
      }
    }
  }
  
  my $data = XMLout($hash_ref, RootName => 'node');

  #print OUTPUT '<?xml version="1.0" ?>'."\n";
  #print OUTPUT '<model xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"'."\n";
  #print OUTPUT '	xsi:noNamespaceSchemaLocation="primex.xsd">'."\n";
  print OUTPUT $data;
  #print OUTPUT '</model>';
  
  if(0)
  {
  #create object
  my $gen  = XML::Generator::DOM->new(escape      => 'always',
				      conformance => 'strict',
				      pretty      => 2);

  #create topnet
  my $dom = $gen->xml($gen->node({'name' => 'topnet','type' =>'net'}));
  
  #create AS 
  foreach my $asnum (sort { $a <=> $b } keys %everything_as) 
  {
    my $asnode = $dom->createElement('node');
   
    $asnode->setAttribute("name", $net . $asnum);
    $asnode->setAttribute("type", $type_net);
    
    $dom->getDocumentElement->appendChild($asnode);
  }
  
  #create router
  foreach my $asnum (sort { $a<=>$b } keys %everything_rt) 
  {
    foreach my $uid (sort { $a<=>$b } keys %{$everything_rt{$asnum}}) 
    {
   
      if($uid < 0) { next; }

      my $rtnode = $dom->createElement("node");
      $rtnode->setAttribute("name", $router . $uid);
      $rtnode->setAttribute("type", $type_router);
      
      my @aslist = $dom->getDocumentElement->getChildNodes();
      foreach my $node (@aslist) 
      {
        if ($node->getNodeType() eq XML::DOM::ELEMENT_NODE) 
        {
          $node->getAttribute("name");
          if ($node->getAttribute("name") eq $net . $asnum)
          {
            $node->appendChild($rtnode);
          }
        }
      }
    }
  }
  
  print STDERR "create interfaces and links...\n";
  #create interface and links
  #interface number
  my $ifnum = 0;
  #link number
  my $lnum = 0;

  foreach my $asnum (sort {$a<=>$b} keys %everything_ln) 
  {
    foreach my $uid (sort {$a<=>$b} keys %{$everything_ln{$asnum}}) 
    {
      foreach my $toasnum (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}}) 
      {
        foreach my $touid (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}{$toasnum}}) 
        {

          my $lat = $everything_ln{$asnum}{$uid}{$toasnum}{$touid}->{latency}/1000;

          if ($touid >= 0) 
          {
            if ($asnum < $toasnum or ( $asnum == $toasnum and $uid < $touid) or 
                !defined $everything_ln{$toasnum}{$touid}{$asnum}{$uid})
            {
              #create interface node
	      my $if1node = $dom->createElement("node");
	      my $if2node = $dom->createElement("node");
	      my $ifname1;
	      my $ifname2;
	      if ($asnum eq $toasnum)
	      {
		$ifname1 = $interface . '_' . $router . $touid;
		$ifname2 = $interface . '_' . $router . $uid;
	      } 
	      else 
              {
		$ifname1 = $interface . '_' . $net . $toasnum . '_' . $router . $touid;
		$ifname2 = $interface . '_' . $net . $asnum . '_' . $router . $uid;
	      }      
	      $if1node->setAttribute("name", $ifname1);
	      $if1node->setAttribute("type", $type_if);
	      $if2node->setAttribute("name", $ifname2);
	      $if2node->setAttribute("type", $type_if); 
		
	      my $attri1node = $dom->createElement("attribute");
	      $attri1node->setAttribute("name", "latency");
	      $attri1node->setAttribute("value", $lat);

	      my $attri2node = $dom->createElement("attribute");
	      $attri2node->setAttribute("name", "latency");
	      $attri2node->setAttribute("value", $lat);		

	      $if1node->appendChild($attri1node);
	      $if2node->appendChild($attri2node);

	      my @aslist = $dom->getDocumentElement->getChildNodes();
	      my @rtlist;
              foreach my $node (@aslist) 
              {
		#find a element node
		if ($node->getNodeType() eq XML::DOM::ELEMENT_NODE)  
		{    
	  	  if ($node->getAttribute("name") eq $net . $asnum) 
		  {
	    	    @rtlist = $node->getElementsByTagName("node");
		    foreach my $rtnode (@rtlist) 
		    {
		      if ($rtnode->getAttribute("name") eq $router . $uid) 
		      {
	    		$rtnode->appendChild($if1node);
		        $ifnum++;  print STDERR "create interface " . $ifnum . "...\n";
		      }
		    } 
	  	  }
		    
		  if ($node->getAttribute("name") eq $net . $toasnum)  
		  {
		    @rtlist = $node->getElementsByTagName("node");
		    foreach my $rtnode (@rtlist) 
		    {
		      if ($rtnode->getAttribute("name") eq $router . $touid) 
		      {
	    	  	$rtnode->appendChild($if2node);
		        $ifnum++;  print STDERR "create interface " . $ifnum . "...\n";
		      }
		    } 
		  }
	        }
	      }
		
	      #create link node
	      my $lnode = $dom->createElement("node");   
	      my $lname;
	      if ($asnum eq $toasnum) 
	      {
		$lname = $router . $uid . '_' . $router . $touid;
	      }
	      else
	      {
                $lname = $net . $asnum . '_' . $net . $toasnum;
	      }
	      $lnode->setAttribute("name", $lname);
	      $lnode->setAttribute("type", $type_link); 
	
	      my $ref1node = $dom->createElement("ref");
	      my $ref2node = $dom->createElement("ref");
	      my $ref1name;
	      my $ref2name;
              my $path1;
              my $path2;    
	      if ($asnum eq $toasnum) 
	      {
		$ref1name = $router . $uid;
	        $ref2name = $router . $touid;
                
                $path1 = '..:' . $net . $asnum . ':' . 
		                  $router . $uid . ':' . 
		                  $interface . '_' . $router . $touid;
                $path2 = '..:' . $net . $toasnum . ':' . 
		                  $router . $touid . ':' . 
		                  $interface . '_' . $router . $uid;
	      }
	      else
	      {
		$ref1name = $net . $asnum;
		$ref2name = $net . $toasnum;
                
                $path1 = '..:' . $net . $asnum . ':' . 
		                  $router . $uid . ':' . 
		                  $interface . '_' . $net . $toasnum . '_' . $router . $touid;
	        $path2 = '..:' . $net . $toasnum . ':' . 
		                  $router . $touid . ':' . 
		                  $interface . '_' . $net . $asnum . '_' . $router . $uid;
	      }
	      
	      $ref1node->setAttribute("name", $ref1name);	
	      $ref1node->setAttribute("path", $path1);	      
	      $ref2node->setAttribute("name", $ref2name);	
	      $ref2node->setAttribute("path", $path2);
	
	      $lnode->appendChild($ref1node);	
	      $lnode->appendChild($ref2node);	
	
              if ($asnum eq $toasnum) 
              {
		@aslist = $dom->getDocumentElement->getChildNodes();
		foreach my $node (@aslist) 
		{
		  if ($node->getNodeType() eq XML::DOM::ELEMENT_NODE) 
		  {
		    $node->getAttribute("name");
		    if ($node->getAttribute("name") eq $net . $asnum) 
		    {
		      $node->appendChild($lnode);
		      $lnum++;  print STDERR "create link " . $lnum . "...\n";
		    }
		  }
	        }
	      }
	      else
              {
		$dom->getDocumentElement->appendChild($lnode);
	        $lnum++;  print STDERR "create link " . $lnum . "...\n";
              }
	    }
          }   
        }
      }
    }
  }

  my $twig = new XML::Twig;
  $twig->set_indent(" "x2);
  $twig->parse($dom->toString);
  $twig->set_pretty_print("indented");

  print OUTPUT $twig->sprint;;
  }
  print STDERR "End creating xml...\n";
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
      my ($lat, $long) = split(/,/, $everything_rt{$asnum}{$uid}->{latlong});
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
#  my $num_rt = 0;
#  my $num_link = 0;
my $icon_dir = "http://gmaps-samples.googlecode.com/svn/trunk/markers/";
my %colors = (  "10" => "pink", 
                "20" => "green",
                "30" => "blue",
                "50" => "orange",
                "500" => "red/blank.png",
                "1000" => "circular/bluecirclemarker.png",
                "2000" => "circular/greencirclemarker.png",
                "other" => "circular/yellowcirclemarker.png",
);
  
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
        ($lat, $long) = split(/,/, $everything_rt{$asnum}{$uid}->{latlong});
        my ($lat_degree, $lat_minute, $long_degree, $long_minute);
        ($lat_degree, $lat_minute) = split(/:/, $lat);
        ($long_degree, $long_minute) = split(/:/, $long);
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
#      $num_rt++;
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
  print OUTPUT '          function createMarker (mypos, myiconImage, mytitle, myinfo) {'."\n";
  print OUTPUT '            var myicon = new GIcon(G_DEFAULT_ICON);'."\n";
  print OUTPUT '            myicon.image = myiconImage;'."\n";
  print OUTPUT '            myicon.shadowSize = new GSize(0, 0);'."\n";
  print OUTPUT '            var marker = new GMarker(mypos, {title: mytitle, icon: myicon});'."\n";
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
#          $num_link++;
          my $loc2 = $everything_rt{$toasnum}{$touid}->{location};
          if($loc1 ne $loc2 &&
              ($locinfo{$loc1}->{latitude} != $locinfo{$loc2}->{latitude} ||
                $locinfo{$loc1}->{longitude} != $locinfo{$loc2}->{longitude}) &&
                  !defined $loc2loc{$loc1}{$loc2} &&
                    (!$onlyUS ||
                      ($loc1 =~ /,US$/ &&
                        $loc2 =~ /,US$/)) ) {
            $loc2loc{$loc1}{$loc2} = $loc2loc{$loc2}{$loc1} = 1;
            #print polyline
            print OUTPUT "          map.addOverlay(new GPolyline([new GLatLng($locinfo{$loc1}->{latitude}, $locinfo{$loc1}->{longitude}), ";
            print OUTPUT "new GLatLng($locinfo{$loc2}->{latitude}, $locinfo{$loc2}->{longitude})], \"#ffffff\", 3));\n";
          }
        }
      }
    }
  }

  if(!$html_no_marker) {
    foreach my $location (keys %locinfo) {
      if (!$onlyUS || $location =~ /,US$/) {
        my $num = scalar @{$locinfo{$location}->{routers}};
        my $color;
        my $image;
        if ($num <= 10) {
          $color = $colors{10};
        } elsif ($num <= 20) {
          $color = $colors{20};
        } elsif ($num <= 30) {
          $color = $colors{30};
        } elsif ($num <= 40) {
          $color = $colors{40};
        } elsif ($num <= 50) {
          $color = $colors{50};
        } elsif ($num <= 500) {
          $color = $colors{500};
        } elsif ($num <= 1000) {
          $color = $colors{1000};
        } elsif ($num <= 2000) {
          $color = $colors{2000};
        } else {
          $color = $colors{other};
        }
        if ($num <= 50) {
          $image = "$icon_dir$color/marker${num}.png";
        } else {
          $image = "$icon_dir$color";
        }
        
        
        #print marker
        print OUTPUT "          map.addOverlay(createMarker(new GLatLng($locinfo{$location}->{latitude}, $locinfo{$location}->{longitude}),\n";
        print OUTPUT "           \"$image\",\n";
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
  }
  
  foreach my $location (sort {(scalar @{$locinfo{$a}->{routers}}) <=> (scalar @{$locinfo{$b}->{routers}}) } keys %locinfo) {
    my $num = scalar @{$locinfo{$location}->{routers}};
    print "### $location: $num\n";
  }

  print OUTPUT '        }'."\n";
  print OUTPUT '      }'."\n";
  print OUTPUT '    </script>'."\n";
  print OUTPUT '  </head>'."\n";
  print OUTPUT '  <body onload="initialize()" onunload="GUnload()">'."\n";
  print OUTPUT '    <div id="map_canvas" style="width: 1200px; height: 750px"></div>'."\n";
  print OUTPUT '  </body>'."\n";
  print OUTPUT '</html>'."\n";
  
#  print "### Google map router: $num_rt, link: $num_link\n";
}

sub print_uuf_undirected_withoutACC {
  my $num_ln = 0;
  my $num_rt = 0;
  my %rt;
  my $count_acc_rt = 0;
  my $count_acc_ln = 0;

  print OUTPUT "# ASes\n";
  print OUTPUT %everything_as;
  print "\n";
  print OUTPUT %everything_rt;
  print "\n";
  print OUTPUT %everything_ln;
  print "\n";

  print OUTPUT "# Links\n";
  foreach my $asnum (sort {$a<=>$b} keys %everything_ln) {
    foreach my $uid (sort {$a<=>$b} keys %{$everything_ln{$asnum}}) {
      foreach my $toasnum (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}}) {
        foreach my $touid (sort {$a<=>$b} keys %{$everything_ln{$asnum}{$uid}{$toasnum}}) {
          my $lat = $everything_ln{$asnum}{$uid}{$toasnum}{$touid}->{latency}/1000;
          my $type = $everything_ln{$asnum}{$uid}{$toasnum}{$touid}->{linktype};
          if ($touid >= 0) {
            if ($asnum < $toasnum or ( $asnum == $toasnum and $uid < $touid) or !defined $everything_ln{$toasnum}{$touid}{$asnum}{$uid}) {
              print OUTPUT "$asnum:$uid\t$toasnum:$touid\t$lat\t$type\n";
              $num_ln++;
              if (!defined $rt{$asnum}{$uid}) {
                $rt{$asnum}{$uid}->{degree} = 1;
                $rt{$asnum}{$uid}->{access} = 0;
              } else { ($rt{$asnum}{$uid}->{degree})++; }
              if (!defined $rt{$toasnum}{$touid}) {
                $rt{$toasnum}{$touid}->{degree} = 1;
                $rt{$toasnum}{$touid}->{access} = 0;
              } else { ($rt{$toasnum}{$touid}->{degree})++; }
            }
          } else {
            # print OUTPUT "$asnum:$uid\t0:$touid\t$lat\t$type\n";
            if (!defined $rt{$asnum}{$uid}) {
              $rt{$asnum}{$uid}->{access} = 1;
              $rt{$asnum}{$uid}->{degree} = 0;
            } else { ($rt{$asnum}{$uid}->{access})++; }
#            $count_acc_ln++;
          }
        }
      }
    }
  }
  print OUTPUT "\n# Routers\n";
  foreach my $asnum (sort {$a<=>$b} keys %rt ) {
    foreach my $uid (sort {$a<=>$b} keys %{$rt{$asnum}} ) {
      if ($rt{$asnum}{$uid}->{degree} > 0) {
        my $cutoff = $everything_rt{$asnum}{$uid}->{cutoff};
        if ($cutoff == -1) { $cutoff = "bb"; }
        print OUTPUT "$asnum:$uid\t$everything_rt{$asnum}{$uid}->{location}\t$rt{$asnum}{$uid}->{degree}\t$rt{$asnum}{$uid}->{access}\t$cutoff\n";
        if ($rt{$asnum}{$uid}->{access} > 0) { 
          $count_acc_rt++;
          $count_acc_ln += $rt{$asnum}{$uid}->{access};  
        }
        $num_rt++;
      }
    }
  }

  print OUTPUT "\n# routers: $num_rt\n";
  print OUTPUT "# links: $num_ln\n";
  print OUTPUT "# Access routers: $count_acc_rt\n";
  print OUTPUT "# Access links: $count_acc_ln\n";
}



sub read_alias_loc {
  my ($file) = @_;
  open IN_ALIAS, "$file" || die "Can't open file '$file' for reading: $!";
  while (<IN_ALIAS>) {
    s/\#.*$//g;
    if (/^(.+,.+,.+)=>(.+)$/) {
      my $loc = $1;
      my $alias = $2;
      $alias_loc{$loc} = $alias;
    }
  }

  close IN_ALIAS;

}

sub print_getDistance_gmap {
  my $delay_ini = 100;
  my $failure620_step = 100;
  my $failure500_step = 200;
  my $suc_step = 1;

  read_alias_loc($alias_loc_file);

  my %locinfo; # so that only one marker is used for each location
  foreach my $asnum ( sort {$a <=> $b} keys %everything_rt) {
    foreach my $uid ( sort {$a <=> $b} keys %{$everything_rt{$asnum}}) {
      if($uid < 0) { next; }
      my $location = $everything_rt{$asnum}{$uid}->{location};
      if(!defined $locinfo{$location}) {
	#get latitude and longtitude
	my ($lat, $long);
	($lat, $long) = split(/,/, $everything_rt{$asnum}{$uid}->{latlong});
	my ($lat_degree, $lat_minute, $long_degree, $long_minute);
	($lat_degree, $lat_minute) = split(/:/, $lat);
	($long_degree, $long_minute) = split(/:/, $long);
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
      }
    }
  }

  print OUTPUT '<!DOCTYPE html "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">'."\n".
               '<html xmlns="http://www.w3.org/1999/xhtml">'."\n".
               '  <head>'."\n".
               '    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>'."\n".
               '    <title>Get Distance - RocketFuel Router Level Topology</title>'."\n".
               '    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=abcdefg&sensor=false"'."\n".
               '            type="text/javascript"></script>'."\n".
               '    <script type="text/javascript">'."\n".
               "\n".
               '    var addresses;'."\n".
               '    var addLength;'."\n".
               '    var nextAddress = 0;'."\n".
               '    var delay_ini = '.$delay_ini.';'."\n".
               '    var delay;'."\n".
               '    var suc_response = 0;'."\n".
               '    var error_response = 0;'."\n".
               '    var request;'."\n".
               '    var failure620 = 0;'."\n".
               '    var failure500 = 0;'."\n".
               '    var failure620_step = '.$failure620_step.';'."\n".
               '    var failure500_step = '.$failure500_step.';'."\n".
               '    var suc_step = '.$suc_step.';'."\n".
               '    var query = 0;'."\n".
               '    var date;'."\n".
               '    var startTime;'."\n".
               '    var output_file = "'.$output_file.'";'."\n".
               '    var geocoder;'."\n".
               "\n".
               '    function getDistance(location, location_alias, lat, long, next) {'."\n".
               '      var latlong = new GLatLng(lat, long);'."\n".
               '      query++;'."\n".
               '      geocoder.getLocations(location_alias, function(response) {'."\n".
               '        if (!response || response.Status.code != G_GEO_SUCCESS ) {'."\n".
               '          var code=response.Status.code;'."\n".
               '          if (response.Status.code == G_GEO_TOO_MANY_QUERIES ) {'."\n".
               '            delay += failure620_step;'."\n".
               '            failure620++;'."\n".
               '          } else if (response.Status.code == G_GEO_SERVER_ERROR) {'."\n".
               '            delay += failure500_step;'."\n".
               '            failure500++;'."\n".
               '          } else {'."\n".
               '            nextAddress += 4;'."\n".
               '            document.getElementById("info").innerHTML += \'<table><tr><td width="350"><b>\'+location+\'</b></td><td width="250">(-)</td><td width="250">(<b>\'+lat.toFixed(4)+\',\'+long.toFixed(4)+\'</b>)</td><td width="150">-</td><td width="100"><b>\'+code+\'</b></td></tr></table>\';'."\n".
               '            error_response++;'."\n".
               '          }'."\n".
               '        } else {'."\n".
               '          nextAddress += 4;'."\n".
               '          var place = response.Placemark[0];'."\n".
               '          var point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);'."\n".
               '          var point_str = point.lat().toFixed(4)+","+point.lng().toFixed(4);'."\n".
               '          var latlong_str = latlong.lat().toFixed(4)+","+latlong.lng().toFixed(4);'."\n".
               '          var distance = point.distanceFrom(latlong)/1000;'."\n".
               '          document.getElementById("info").innerHTML += \'<table><tr><td width="350"><b>\'+location+\'</b></td><td width="250">(\'+point_str+\')</td><td width="250">(<b>\'+latlong_str+\'</b>)</td><td width="150"><b>\'+distance.toFixed(4)+\'</b></td><td width="100">-</td></tr></table>\';'."\n".
               '          delay -= suc_step;'."\n".
               '          suc_response++;'."\n".
               '        }'."\n".
               '        next();'."\n".
               '      });'."\n".
               '    }'."\n".
               "\n".
               '    function getDate() {'."\n".
               '      var d = new Date();'."\n".
               '      var year = d.getFullYear();'."\n".
               '      var month = d.getMonth();'."\n".
               '      var date = d.getDate();'."\n".
               '      month += 1;'."\n".
               '      month = month + "";'."\n".
               '      date = date + "";'."\n".
               '      if (month.length == 1) {'."\n".
               '        month = "0"+month;'."\n".
               '      }'."\n".
               '      if (date.length == 1) {'."\n".
               '        date = "0"+date;'."\n".
               '      }'."\n".
               '      var date = ""+year+""+month+""+date;'."\n".
               '      return date;'."\n".
               '    }'."\n".
               "\n".
               '    function getTime() {'."\n".
               '      var d = new Date();'."\n".
               '      var hour = d.getHours();'."\n".
               '      var minute = d.getMinutes();'."\n".
               '      hour = hour + "";'."\n".
               '      minute = minute + "";'."\n".
               '      if (hour.length == 1) {'."\n".
               '        hour = "0"+hour;'."\n".
               '      }'."\n".
               '      if (minute.length == 1) {'."\n".
               '        minute = "0"+minute;'."\n".
               '      }'."\n".
               '      var time = ""+hour+""+minute;'."\n".
               '      return time;'."\n".
               '    }'."\n".
               "\n".
               '    function theNext() {'."\n".
               '      if (nextAddress < addLength) {'."\n".
               '        setTimeout(\'getDistance("\'+addresses[nextAddress]+\'", "\'+addresses[nextAddress+1]+\'", \'+addresses[nextAddress+2]+\', \'+addresses[nextAddress+3]+\', theNext)\', delay);'."\n".
               '      } else {'."\n".
               '        document.getElementById("info").innerHTML += \'<br>\';'."\n".
               '        document.getElementById("info").innerHTML += \'<table><tr><td width="300"><b># Succesful response</b></td><td><b>\'+suc_response+\'</b></td></tr><tr><td width="300"><b># Error response</b></td><td><b>\'+error_response+\'</b></td></tr><tr><td width="300"><b># Request number</b></td><td><b>\'+request+\'</b></td></tr><tr><td width="300"><b># Failure_too_many</b></td><td><b>\'+failure620+\'</b></td></tr><tr><td width="300"><b># Failure_server_error</b></td><td><b>\'+failure500+\'</b></td></tr><tr><td width="300"><b># Actual query</b></td><td><b>\'+query+\'</b></td></tr><br><tr><td width="300"><b># Initial delay</b></td><td><b>\'+delay_ini+\'</b></td></tr><tr><td width="300"><b># Delay</b></td><td><b>\'+delay+\'</b></td></tr><tr><td width="300"><b># Failure_too_many step</b></td><td><b>\'+failure620_step+\'</b></td></tr><tr><td width="300"><b># Failure_server_error step</b></td><td><b>\'+failure500_step+\'</b></td></tr><tr><td width="300"><b># Successful response step</b></td><td><b>\'+suc_step+\'</b></td></tr></table>\';'."\n".
               '        var endTime = getTime();'."\n".
               '        document.getElementById("info").innerHTML += \'<br>\';'."\n".
               '        document.getElementById("info").innerHTML += "# Please copy and paste the content of this page, and save as \'<b>"+output_file+"_"+date+"_"+startTime+"_"+endTime+".distance</b>\', for further denoise process<br>";'."\n".
               '        alert("Finish");'."\n".
               '      }'."\n".
               '    }'."\n".
               "\n".
               '    function initialize() {'."\n".
               '      if (GBrowserIsCompatible()) {'."\n".
               '        date = getDate();'."\n".
               '        startTime = getTime();'."\n".
               '        addresses = ['."\n";

  foreach my $location (keys %locinfo) {
    #print marker
#    print OUTPUT "          map.addOverlay(createMarker(new GLatLng($locinfo{$location}->{latitude}, $locinfo{$location}->{longitude}),\n";
#    print OUTPUT "           \"$location ($locinfo{$location}->{latitude}, $locinfo{$location}->{longitude})\",\n";
#    my $str = '';
#    my $nrts = scalar(@{$locinfo{$location}->{routers}});
#    for(my $i=0; $i<$nrts; $i++) {
#      $str .=  $locinfo{$location}->{routers}[$i];
#      if(($i+1)%3 == 0) { $str .= "<br>"; }
#      elsif($i < $nrts-1) { $str .= ", "; }
#    }
#    print OUTPUT "           \"$location ($locinfo{$location}->{latitude}, $locinfo{$location}->{longitude})<br>$str\"));\n";
    my $alias;
    if (defined $alias_loc{$location}) {
      $alias = $alias_loc{$location};
    } else { $alias = $location; }
    print OUTPUT '                      "'.$location.'", "'.$alias.'", '.$locinfo{$location}->{latitude}.', '.$locinfo{$location}->{longitude}.','."\n";
  }

  print OUTPUT '                    ];'."\n".
               "\n".
               '        addLength = addresses.length;'."\n".
               '        request = addLength/4;'."\n".
               '        geocoder = new GClientGeocoder();'."\n".
               '        delay = delay_ini;'."\n".
               '        theNext();'."\n".
               '      } else {'."\n".
               '        alert("Sorry, the Google Maps API is not compatible with this browser");'."\n".
               '      }'."\n".
               '    }'."\n".
               "\n".
               '    </script>'."\n".
               '  </head>'."\n".
               '  <body onload="initialize()" onunload="GUnload()">'."\n".
               '    <div id="info"></div>'."\n".
               '  </body>'."\n".
               '</html>'."\n";

}

sub ip2num {
  my ($str) = @_;
  my ($a1, $a2, $a3, $a4) = split(/\./, $str);
  if(!defined $a1 || !defined $a2 || !defined $a3 || !defined $a4) {
    die "ill-formed ip address!\n";
  }
  return ($a1<<24)+($a2<<16)+($a3<<8)+$a4;
}
