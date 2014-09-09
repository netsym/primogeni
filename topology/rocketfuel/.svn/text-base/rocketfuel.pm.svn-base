package rocketfuel;

use lib '/home/jiang/rocketfuel';

use strict;
require 5.0.1;

require NetGeoClient;
require city2latlong;
require latlong2distance;

#
# function: get_router_level_map()
# input: a list of AS numbers
# output: a list of hash tables: 
#   ({%neighbors}, {%routers}, {%ext_routers}, {%peering})
# see the data structures below for an explanation of each of the hash tables.
#
our @EXPORT = qw(get_router_level_map);

#
# This is a global data structure containing all information about the
# routers. The data structure is represented as follows:
#  $routers{$asnum}{$uid} = ( 
#     ipaddrs => list of ip addresses of the interfaces of this router
#     aliases => list of aliases (DNS names or ip addresses) corresponding 
#                to the ip addresses listed above
#     cutoff => distance from a named gateway or backbone router
#     location => the router's location (CITY,STATE,COUNTRY); '*' means 
#                 unknown or useless (like STATE for a foreign country)
#     latlong => the lattitude and longitude of the location
#  };
# where $uid starts from 0 and goes consecutively.
#
my %routers;

#
# This is a global data structure containing information of external
# routers, considered foreign from the perspective of an AS. The data
# structure is represented as follows:
#  $ext_routers{$asnum}{$extuid} = {
#    alias => alias of the router
#    cutoff => distance from a named gateway or backbone router
#    reference => "$asnum:$uid" if the router is identified from 
#                 cross-referencing with other requested ASes
#  };
# where $extuid starts from 1 and goes consecutively
#
my %ext_routers;

#
# This is the global data structure to hold the adjacency matrix. The
# information is organized as follows:
#  $neighbors{$from_asnum}{$from_uid}{$to_asnum}{$to_uid} = {
#    latency => in milliseconds
#    weight => link weight for routing
#    linktype => exterior, interior, or access links
#  };
# where $to_uid could be negative if the peer is not identified among
# the known ASes, in which case, $to_asnum is the same as $from_asum
# and ABS($to_uid) is the index to $ext_routers{$from_asnum}.
#
my %neighbors;

#
# This global data structure is used to record peering relationships
# between ASs. In particular, 
#  $peering{$fromas}{$toas}{$uid}{$touid} = {
#    relation => "p2c", "c2p", "p2p"
#  };
# 
#
my %peering;

# these ASes have both latency and weight measures available between
# the internal routers
my %as_latwgt = 
  (
   1221 => "Telstra",
   1239 => "Sprint",
   1755 => "Ebone",
   3257 => "Tiscali",
   3967 => "Exodus",
   6461 => "Abovenet",
  );

# these ASes have no latency and weight measures
my %as_derived = 
  (
    2914 => "Verio",
    3356 => "Level3",
    3561 => "CableWireless",
    4755 => "TATACOMM",
    7018 => "AT&T",
    701 => "UUNet701",
    703 => "UUNet703",
  );

# available ISPs supported by rocketfuel; a map from their AS numbers
# to the ISP names
my %asnum2ispname = (%as_latwgt, %as_derived);

# set the follow to be non-zero to print out detailed information
my $DEBUG = 0;

# used for obtaining locations
my $netgeo = new CAIDA::NetGeoClient();
$netgeo->useLocalCache(0);
my %netgeo_lookup = ();
my %latlong_lookup = ();

# this is _the_ function called from outside
sub get_router_level_map {
  my ($netgeo_cache_file, $latlong_cache_file, @aslist) = @_;

  foreach my $asnum (@aslist) {
    if(!defined $asnum2ispname{$asnum}) {
      die "ERROR: rocketfuel does not contain data for AS ${asnum}\n";
    }
    get_aliases($asnum, "data/${asnum}.al");
  }

  foreach my $asnum (@aslist)  {
    get_topology($asnum, "data/${asnum}.cch");

    my $loc_adjusted = 0;
    if(defined $as_latwgt{$asnum}) {
      $loc_adjusted = get_latencies($asnum, "data/${asnum}.lat");
      $loc_adjusted += get_weights($asnum, "data/${asnum}.wgt");
    }
    if($loc_adjusted > 0) {
      my $n = scalar(keys %{$routers{$asnum}});
      my $r = int(1000*$loc_adjusted/$n)/10.0;
      print STDERR "rocketfuel: location adjusted: $loc_adjusted ($r%)\n";
    }
  }

  mend_asymmetry();
  resolve_netgeo($netgeo_cache_file);
  remove_unknown();
  fix_latencies($latlong_cache_file);
  fix_linktypes();

  print_summary();
  if($DEBUG) { print_everything(); }
  return ({%neighbors}, {%routers}, {%ext_routers}, {%peering});
}

#
# This is a map from ip address to AS number and its uid. It is used
# for cross-referencing.
#
my %aliases = ();

my %known_distances = ();
my %known_distances_num = ();
my $num_known_distances = 0;
my $num_unknown_distances = 0;

my %touch = ();
my %ext_touch = ();

#
# The following subroutine parses the rocketfuel file ${asnum}.al to
# obtain the list of aliases of all routers in an ISP. The subroutine
# takes two parameters: the AS number $asnum, and the name of the
# alias file $alfile. The information about router aliases is inserted
# into the global data structure %routers.
#
sub get_aliases {
  my ($asnum, $alfile) = @_;
  open(IN_AL, "$alfile") || die "can't open alias file: $alfile\n";
  while(<IN_AL>) {
    chop; 
    s/\#.*$//g; # get rid of comments
    if(/([0-9]+)\s+([0-9.]+)\s+(.*)/) {
      my $uid = $1;
      my $ipaddr = $2;
      my $alias = $3;
      #print "$asnum:$uid $ipaddr $alias\n";
      push @{$routers{$asnum}{$uid}->{ipaddrs}}, $ipaddr;
      push @{$routers{$asnum}{$uid}->{aliases}}, $alias;
      $aliases{$ipaddr} = "$asnum:$uid";
    }
  }
  close(IN_AL);
}

#
# The following subroutine parses the RockFuel file ${asnum}.cch for
# the topology description. The subroutine takes two parameters: the
# AS number $asnum, and the name of the rocketfuel file $cchfile that
# contains the connectivity information. The information is filled in
# the global data structures $routers and $neighbors.
#
sub get_topology {
  my ($asnum, $cchfile) = @_;
  open(IN_CCH, "$cchfile") || die "can't open topology file: $cchfile\n";

  # start with matching the description of an external router
  while(<IN_CCH>) {
    chop;
    s/\#.*$//g; # get rid of comments

    if(/-([0-9]+)\s*=\s*([0-9.]+)\s+r(.*)/) {
      my $uid = $1;
      my $alias = $2;
      my $cutoff = $3;
      #print "$uid = $alias r$cutoff\n";
      $ext_routers{$asnum}{$uid}->{alias} = $alias;
      $ext_routers{$asnum}{$uid}->{cutoff} = $cutoff;
      if(defined $aliases{$alias}) {
	#print "$asnum:-$uid <=> $aliases{$alias}\n";
	$ext_routers{$asnum}{$uid}->{reference} = $aliases{$alias};
	#my ($toasnum, $touid) = split /:/, $aliases{$alias};
      }
    }
  }

  # get all internal connectivities
  seek(IN_CCH, 0, 0);
  while(<IN_CCH>) {
    chop;
    s/\#.*$//g; # get rid of comments

    # internal router is listed with quite a bunch of things, including
    # its location, adjacency list
    if(/([0-9]+)\s+@(\S+)\s+(\+?\s+)?(bb\s+)?\((\d+)\)\s+(&(\d+)\s+)?->(.*)/) {
      my $uid = $1;
      my $loc = $2;
      my $bb = $4;
      my $n = $5;
      my $m = $7;
      my $rest = $8;
      unless(defined $m) { $m = 0; }
      $loc =~ s/\+/ /g;
      #print "$uid($loc) $n($m) ->";
      if(defined $bb) { $routers{$asnum}{$uid}->{cutoff} = -1; }

      #print "ip='$routers{$asnum}{$uid}->{ipaddrs}[0]'\n";
      if($loc eq 'T' || $loc eq '?') {
	$routers{$asnum}{$uid}->{location} = '*,*,*';
	$netgeo_lookup{$routers{$asnum}{$uid}->{ipaddrs}[0]} =
	  \$routers{$asnum}{$uid}->{location};
      } else {
	$routers{$asnum}{$uid}->{location} =
	  city2latlong::formalize_cityname($loc);
	$netgeo_lookup{$routers{$asnum}{$uid}->{ipaddrs}[0]} =
	  \$routers{$asnum}{$uid}->{location};
      }

      my $sum = 0;
      while($rest =~ /<(\d+)>/) {
	my $nextuid = $1;
	$rest =~ s/<$nextuid>//;
	$sum++;
	#print " $nextuid";
	if($nextuid == $uid) { next; } # there's a possibility it's a self-loop!!!
	$neighbors{$asnum}{$uid}{$asnum}{$nextuid}->{latency} = 0; # placeholder
	$neighbors{$asnum}{$uid}{$asnum}{$nextuid}->{weight} = 0; # placeholder
      }
      if($sum != $n) { die "ERROR: corrupted file: $cchfile, $uid (#nodes=$n)<>$sum\n"; }

      $sum = 0;
      while($rest =~ /\{-(\d+)\}/) {
	my $nextuid = $1;
	$rest =~ s/\{-$nextuid\}//;
	$sum++;
	#print " -$nextuid";
	if(defined $ext_routers{$asnum} && 
	   defined $ext_routers{$asnum}{$nextuid} &&
	   defined $ext_routers{$asnum}{$nextuid}->{reference}) {
	  my $ref = $ext_routers{$asnum}{$nextuid}->{reference};
	  my ($toasnum, $touid) = split /:/, $ref;
	  $neighbors{$asnum}{$uid}{$toasnum}{$touid}->{latency} = 0; # placeholder
	  $neighbors{$asnum}{$uid}{$toasnum}{$touid}->{weight} = 0; # placeholder
	  if($asnum == $toasnum) { die "unexpected!"; }
	  $peering{$asnum}{$toasnum}{$uid}{$touid}->{relation} = "p2p";
	} else {
	  # assume this is connecting to a router belonging to a customer
	  $neighbors{$asnum}{$uid}{$asnum}{-$nextuid}->{latency} = 1; 
	  $neighbors{$asnum}{$uid}{$asnum}{-$nextuid}->{weight} = 0; # placeholder
	  $peering{$asnum}{0}{$uid}{-$nextuid}->{relation} = "p2c";
	  $ext_touch{$asnum}{$nextuid} = 1;
	}
      }
      if($sum != $m) { die "ERROR: corrupted file: $cchfile, $uid (#links=$m)<>$sum\n"; }

      if($rest =~ /\s+=\s*([^\s!]+)\s*\!?\s+r([0-9]+)/) {
	my $alias = $1;
	my $cutoff = $2;
	#print " = $hostname (r$cutoff)\n";
	if(!defined $routers{$asnum} ||
	   !defined $routers{$asnum}{$uid} ||
	   !defined $routers{$asnum}{$uid}->{cutoff}) {
	  $routers{$asnum}{$uid}->{cutoff} = $cutoff;
	}
      } else { die "ERROR: corrupted file: $cchfile, unrecognized line: $rest\n"; }
    }
  }

  close(IN_CCH);
}

# retrieve latency information
sub get_latencies {
  my ($asnum, $latfile) = @_;
  my $loc_adjusted = 0;
  open(IN_LAT, "$latfile") || die "Can't open $latfile\n";
  while(<IN_LAT>) {
    chop;
    s/\#.*$//g;
    if(/([^\d]+)(\d+)\s+([^\d]+)(\d+)\s+(\d+)/) {
      my $rfrom = $1;
      my $fromuid = $2;
      my $rto = $3;
      my $touid = $4;
      my $lat = $5;
      $rfrom =~ s/\+/ /g; $rfrom = city2latlong::formalize_cityname($rfrom);
      $rto =~ s/\+/ /g; $rto = city2latlong::formalize_cityname($rto);
      #print "$rfrom($fromuid)=>$rto($touid): $lat\n";
      if(defined $known_distances{$rfrom} &&
	 defined $known_distances{$rfrom}{$rto}) {
	$known_distances_num{$rfrom}{$rto}++;
	$known_distances{$rfrom}{$rto} = 
	  ($known_distances{$rfrom}{$rto}*($known_distances_num{$rfrom}{$rto}-1)+$lat)/
	    $known_distances{$rfrom}{$rto};
      } else {
	$known_distances{$rfrom}{$rto} = $lat;
	$known_distances_num{$rfrom}{$rto} = 1;
      }

      if(!defined $routers{$asnum} ||
	 !defined $routers{$asnum}{$fromuid}) {
	die "Unexpected: $asnum:$fromuid defined in $latfile is not defined elsewhere\n";
      }
      if($routers{$asnum}{$fromuid}->{location} ne $rfrom) {
	if($DEBUG) { print STDERR "(latency:from) update $asnum:$fromuid location from $routers{$asnum}{$fromuid}->{location} to $rfrom\n"; }
	$routers{$asnum}{$fromuid}->{location} = $rfrom;
	$loc_adjusted++;
      }
      if(!defined $routers{$asnum} ||
	 !defined $routers{$asnum}{$touid}) {
	die "Unexpected: $asnum:$touid defined in $latfile is not defined elsewhere\n";
      }
      if($routers{$asnum}{$touid}->{location} ne $rto) {
	if($DEBUG) { print STDERR "(latency:to) update $asnum:$touid location from $routers{$asnum}{$touid}->{location} to $rto\n"; }
	$routers{$asnum}{$touid}->{location} = $rto;
	$loc_adjusted++;
      }

      $neighbors{$asnum}{$fromuid}{$asnum}{$touid}->{latency} = $lat;
      $num_known_distances++;
    }
  }
  close(IN_LAT);
  return $loc_adjusted;
}

# retrive link weight information
sub get_weights {
  my ($asnum, $wgtfile) = @_;
  my $loc_adjusted = 0;
  open(IN_WGT, "$wgtfile") || die "Can't open $wgtfile\n";
  while(<IN_WGT>) {
    chop;
    s/\#.*$//g;
    if(/([^\d]+)(\d+)\s+([^\d]+)(\d+)\s+(\d+)/) {
      my $rfrom = $1;
      my $fromuid = $2;
      my $rto = $3;
      my $touid = $4;
      my $wgt = $5;
      $rfrom =~ s/\+/ /g; $rfrom = city2latlong::formalize_cityname($rfrom);
      $rto =~ s/\+/ /g; $rto = city2latlong::formalize_cityname($rto);
      #print "$rfrom($fromuid)=>$rto($touid): $wgt\n";

      if(!defined $routers{$asnum} ||
	 !defined $routers{$asnum}{$fromuid}) {
	die "Unexpected: $asnum:$fromuid defined in $wgtfile is not defined elsewhere\n";
      }
      if($routers{$asnum}{$fromuid}->{location} ne $rfrom) {
	if($DEBUG) { print STDERR "(weight:from) update $asnum:$fromuid location from $routers{$asnum}{$fromuid}->{location} to $rfrom\n"; }
	$routers{$asnum}{$fromuid}->{location} = $rfrom;
	$loc_adjusted++;
      }
      if(!defined $routers{$asnum} ||
	 !defined $routers{$asnum}{$touid}) {
	die "Unexpected: $asnum:$touid defined in $wgtfile is not defined elsewhere\n";
      }
      if($routers{$asnum}{$touid}->{location} ne $rto) {
	if($DEBUG) { print STDERR "(weight:to) update $asnum:$touid location from $routers{$asnum}{$touid}->{location} to $rto\n"; }
	$routers{$asnum}{$touid}->{location} = $rto;
	$loc_adjusted++;
      }

      $neighbors{$asnum}{$fromuid}{$asnum}{$touid}->{weight} = $wgt;
    }
  }
  close(IN_WGT);
  return $loc_adjusted;
}

sub mend_asymmetry {
  my $asym = 0;
  my $self = 0;
  foreach my $asnum (sort keys %neighbors) {
    foreach my $uid (sort keys %{$neighbors{$asnum}}) {
      foreach my $nasnum (sort keys %{$neighbors{$asnum}{$uid}}) {
	foreach my $nuid (sort keys %{$neighbors{$asnum}{$uid}{$nasnum}}) {
	  # remove self references
	  if($asnum == $nasnum && $uid == $nuid) {
	    delete $neighbors{$nasnum}{$nuid}{$asnum}{$uid};
	    $self++;
	  } elsif($nuid >= 0) {
	    $touch{$asnum}{$uid} = 1;
	    if(!defined $neighbors{$nasnum} ||
	       !defined $neighbors{$nasnum}{$nuid} ||
	       !defined $neighbors{$nasnum}{$nuid}{$asnum} ||
	       !defined $neighbors{$nasnum}{$nuid}{$asnum}{$uid}) {
	      $neighbors{$nasnum}{$nuid}{$asnum}{$uid} = 
		$neighbors{$asnum}{$uid}{$nasnum}{$nuid};
	      if($asnum != $nasnum && 
		 defined $peering{$asnum} &&
		 defined $peering{$asnum}{$nasnum} &&
		 defined $peering{$asnum}{$nasnum}{$uid} &&
		 defined $peering{$asnum}{$nasnum}{$uid}{$nuid} &&
		 (!defined $peering{$nasnum} ||
		  !defined $peering{$nasnum}{$asnum} ||
		  !defined $peering{$nasnum}{$asnum}{$nuid} ||
		  !defined $peering{$nasnum}{$asnum}{$nuid}{$uid})) {
		$peering{$nasnum}{$asnum}{$nuid}{$uid} = 
		  $peering{$asnum}{$nasnum}{$uid}{$nuid};
	      }
	      $asym++;
	    }
	  }
	}
      }
    }
  }
  if($asym > 0) {
    print STDERR "rocketfuel: mend asymmetric links: $asym\n";
  }
  if($self > 0) {
    print STDERR "rocketfuel: remove self-loops: $self\n";
  }
}

sub remove_unknown {
  my $rmnodes = 0;
  my $rmlinks = 0;
  my $rmnodes_ext = 0;

  foreach my $asnum (sort {$a<=>$b} keys %routers) {
    my $rmn = 0; my $rml = 0; my $rmn_ext = 0;

    foreach my $uid (keys %{$neighbors{$asnum}}) {
      foreach my $toasnum (keys %{$neighbors{$asnum}{$uid}}) {
	foreach my $touid (keys %{$neighbors{$asnum}{$uid}{$toasnum}}) {
	  if(!defined $routers{$asnum} ||
	     !defined $routers{$asnum}{$uid} ||
	     !defined $routers{$asnum}{$uid}->{location} || 
	     $routers{$asnum}{$uid}->{location} =~ /\*,\s*\*/) {
	    delete $neighbors{$asnum}{$uid}{$toasnum}{$touid};
	    $rml++;
	  } elsif($touid >= 0 &&
		  (!defined $routers{$toasnum} ||
		   !defined $routers{$toasnum}{$touid} ||
		   !defined $routers{$toasnum}{$touid}->{location} ||
		   $routers{$toasnum}{$touid}->{location} =~ /\*,\s*\*/)) {
	    delete $neighbors{$asnum}{$uid}{$toasnum}{$touid};
	    $rml++;
	  }
	}
      }
    }

    foreach my $uid (keys %{$routers{$asnum}}) {
      if(!defined $touch{$asnum} ||
	 !defined $touch{$asnum}{$uid} ||
	 !defined $routers{$asnum} ||
	 !defined $routers{$asnum}{$uid} ||
	 !defined $routers{$asnum}{$uid}->{location} ||
	 $routers{$asnum}{$uid}->{location} =~ /\*,\s*\*/) {
	delete $routers{$asnum}{$uid};
	$rmn++;
      } #else { print STDERR "$routers{$asnum}{$uid}->{location}\n"; }
    }

    foreach my $uid (keys %{$ext_routers{$asnum}}) {
      if(!defined $ext_touch{$asnum} ||
	 !defined $ext_touch{$asnum}{$uid}) {
	delete $ext_routers{$asnum}{$uid};
	$rmn_ext++;
      }
    }

    $rmnodes += $rmn; $rmlinks += $rml; $rmnodes_ext += $rmn_ext;
    if($rmn>0 || $rml>0 || $rmn_ext>0) {
      print STDERR "rocketfuel: remove nodes isolated or without location: AS$asnum => $rmn nodes, $rmn_ext external nodes, and $rml links\n";
    }
  }
  if($rmnodes>0 || $rmlinks>0 || $rmnodes_ext>0) {
    print STDERR "rocketfuel: total removed: $rmnodes nodes, $rmnodes_ext external nodes, and $rmlinks links\n";
  }
}

my %netgeo_cache;

sub read_netgeo_cache {
  my ($cachefile) = @_;

  open(IN_CACHE, "$cachefile") || return;
  while(<IN_CACHE>) {
    chop; s/\#.*$//g;
    if(/=>/) {
      my ($ip, $loc_rocketfuel, $loc_netgeo, $loc_geobytes) = split '=>';
      $netgeo_cache{$ip}->{rocketfuel} = $loc_rocketfuel;
      $netgeo_cache{$ip}->{netgeo} = $loc_netgeo;
      if(defined $loc_geobytes) {
	$netgeo_cache{$ip}->{geobytes} = $loc_geobytes;
      }
    }
  }
  close(IN_CACHE);
}

sub write_netgeo_cache {
  my ($cachefile) = @_;

  open(OUT_CACHE, ">$cachefile") || die "Can't open $cachefile\n";
  print OUT_CACHE "# THIS FILE IS GENERATED AUTOMATICALLY, DON'T CHANGE!!!\n";
  foreach my $ip (sort keys %netgeo_cache) {
    print OUT_CACHE "${ip}=>$netgeo_cache{$ip}->{rocketfuel}";
    print OUT_CACHE "=>$netgeo_cache{$ip}->{netgeo}";
    if(defined $netgeo_cache{$ip}->{geobytes}) {
      print OUT_CACHE "=>$netgeo_cache{$ip}->{geobytes}";
    }
    print OUT_CACHE "\n";
  }
  close(OUT_CACHE);
}

sub resolve_netgeo {
  my ($netgeo_cache_file) = @_;
  my @iplist = reverse (keys %netgeo_lookup);
  if(scalar(@iplist) == 0) { return; }

  # the following is used because there's a rate limiting mechanism
  # in the NetGeo server.

  read_netgeo_cache($netgeo_cache_file);
  while(scalar(@iplist) > 0) {
    my @mylist = ();
    my $cut=0;
    while($cut<100 && scalar(@iplist)>0) {
      my $ip = shift @iplist;
      if(defined $netgeo_cache{$ip}) {
	my $rloc = $netgeo_lookup{$ip};
	# only when rocketfuel is not providing the city name
	if($$rloc =~ /\*,\*/) {

	  # we prefer geobytes over netgeo; seems more precise
	  if(defined $netgeo_cache{$ip} &&
	     defined $netgeo_cache{$ip}->{geobytes} &&
	     $netgeo_cache{$ip}->{geobytes} ne '*,*,*') {
	    $$rloc = $netgeo_cache{$ip}->{geobytes};
	  } else { $$rloc = $netgeo_cache{$ip}->{netgeo}; }

	  #if(defined $netgeo_cache{$ip}->{netgeo} &&
	  #   $netgeo_cache{$ip}->{netgeo} ne '*,*,*') {
	  #  $$rloc = $netgeo_cache{$ip}->{netgeo};
	  #} else { $$rloc = $netgeo_cache{$ip}->{geobytes}; }
	}
	$latlong_lookup{$$rloc} = 0;
	next;
      }
      print "looking up $ip\n";
      push @mylist, $ip;
      $cut++;
    }
    if($cut == 0) { last; }

    my $t = scalar(@iplist); print STDERR "retrieving next $cut, $t left\n";
    my $rec = $netgeo->getRecordArray([@mylist]);
    my $count = 0;
    while(1) {
      my $founderror = 0;
      foreach my $r (@$rec) {
	my $status = $r->{STATUS};
	if(!defined $status ||
	   $status ne $CAIDA::NetGeoClient::OK &&
	   $status ne $CAIDA::NetGeoClient::NO_MATCH) {
	  $founderror++;
	  last;
	}
      }
      if($founderror > 0) {
	if($count > 5) { last; }
	print STDERR "retry (after 100 seconds)...\n";
	sleep 100;
        $netgeo->updateRecordArray($rec);
	$count++;
      } else { last; }
    }
    foreach my $r (@$rec) {
      my $ip = $r->{TARGET};
      if($r->{STATUS} eq $CAIDA::NetGeoClient::NO_MATCH ||
	 $r->{STATUS} eq $CAIDA::NetGeoClient::LOOKUP_IN_PROGRESS) {
	die "NETGEO[$ip] no match\n";
      }
      if(!defined $r->{CITY}) { $r->{CITY} = '*'; }
      if(!defined $r->{STATE}) { $r->{STATE} = '*'; }
      if(!defined $r->{COUNTRY}) { $r->{COUNTRY} = '*'; }
      $netgeo_cache{$ip}->{netgeo} = city2latlong::formalize_cityname
	("$r->{CITY},$r->{STATE},$r->{COUNTRY}");
      print STDERR "NETGEO[$ip]=\"$netgeo_cache{$ip}->{netgeo}\"\n";

      my $rloc = $netgeo_lookup{$ip};
      $netgeo_cache{$ip}->{rocketfuel} = $$rloc;
      # only when rocketfuel is not providing the city name
      if($$rloc =~ /\*,\*/) {

	# we prefer geobytes over netgeo; seems more precise
	if(defined $netgeo_cache{$ip}->{geobytes} &&
	   $netgeo_cache{$ip}->{geobytes} ne '*,*,*') {
	  $$rloc = $netgeo_cache{$ip}->{geobytes};
	} else { $$rloc = $netgeo_cache{$ip}->{netgeo}; }

	#if(defined $netgeo_cache{$ip}->{netgeo} &&
	#   $netgeo_cache{$ip}->{netgeo} ne '*,*,*') {
	#  $$rloc = $netgeo_cache{$ip}->{netgeo};
	#} else { $$rloc = $netgeo_cache{$ip}->{geobytes}; }
      }
      $latlong_lookup{$$rloc} = 0;
    }
    write_netgeo_cache($netgeo_cache_file);

    if(scalar(@iplist) > 0) {
      print STDERR "sleep for 15 seconds, because of rating limiting... ($t left)\n";
      sleep 15;
    }
  }
}

sub fix_latencies {
  my ($latlong_cache_file) = @_;
  %latlong_lookup = city2latlong::get_city_latlong
    ($latlong_cache_file, keys %latlong_lookup);

  foreach my $asnum (sort { $a <=> $b } keys %routers) {
    foreach my $uid (keys %{$routers{$asnum}}) {
      $routers{$asnum}{$uid}->{latlong} =
	$latlong_lookup{$routers{$asnum}{$uid}->{location}};
    }
  }

  # until now, the following number counts both directions of a link;
  # now, we should have it for undirected
  $num_known_distances /= 2.0;

  foreach my $asnum (sort { $a <=> $b } keys %routers) {
    foreach my $uid (sort { $a <=> $b } keys %{$neighbors{$asnum}}) {
      foreach my $nasnum (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}}) {
	foreach my $nuid (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}{$nasnum}}) {
	  if($asnum < $nasnum || $asnum == $nasnum && $uid < $nuid) {
	    if($neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{latency} == 0) {
	      if(defined $known_distances{$routers{$asnum}{$uid}->{location}} &&
		 defined $known_distances{$routers{$asnum}{$uid}->{location}}
		 {$routers{$nasnum}{$nuid}->{location}}) {
		$neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{latency} = 
		  $neighbors{$nasnum}{$nuid}{$asnum}{$uid}->{latency} = 
		    int($known_distances{$routers{$asnum}{$uid}->{location}}
			{$routers{$nasnum}{$nuid}->{location}});
		$num_known_distances++;
	      } else {
		$neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{latency} = 
		  $neighbors{$nasnum}{$nuid}{$asnum}{$uid}->{latency} = 
		    compute_latency(latlong2distance::get_latlong_distance
				    ($routers{$asnum}{$uid}->{latlong},
				     $routers{$nasnum}{$nuid}->{latlong}));
		$num_unknown_distances++;
	      }
	    }
	  }
	}
      }
    }
  }

  my $t = $num_known_distances+$num_unknown_distances;
  my $r = int($num_known_distances*1000/$t)/10;

  print STDERR "rocketfuel: found $num_known_distances ($r% of $t) latency labels\n";
}

sub fix_linktypes {
  foreach my $asnum (sort { $a <=> $b } keys %routers) {
    foreach my $uid (sort { $a <=> $b } keys %{$neighbors{$asnum}}) {
      foreach my $nasnum (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}}) {
	foreach my $nuid (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}{$nasnum}}) {
	  if($asnum < $nasnum) {
	    $neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{linktype} = 
	      $neighbors{$nasnum}{$nuid}{$asnum}{$uid}->{linktype} = 
		compute_exterior_bandwidth($routers{$asnum}{$uid}->{cutoff},
					   $routers{$nasnum}{$nuid}->{cutoff});
	  } elsif($asnum == $nasnum && $uid < $nuid) {
	    $neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{linktype} = 
	      $neighbors{$nasnum}{$nuid}{$asnum}{$uid}->{linktype} = 
		compute_interior_bandwidth($routers{$asnum}{$uid}->{cutoff},
					   $routers{$nasnum}{$nuid}->{cutoff});
	  } elsif($nuid < 0) { # $asnum must be the same as $nasnum
	    $neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{linktype} = 
	      compute_access_bandwidth($routers{$asnum}{$uid}->{cutoff}, 
				       $ext_routers{$asnum}{-$nuid}->{cutoff});
	  }
	}
      }
    }
  }
}

use constant PROP_SPEED=>1e8; # one third of light speed

sub compute_latency {
  my ($d) = @_;
  my $lat = int($d*1000/PROP_SPEED); # in milliseconds
  if($lat <= 0) { $lat = 1; }
  return $lat;
}  

sub compute_exterior_bandwidth {
  my ($c1, $c2) = @_;
  if($c1 > $c2) { my $t = $c1; $c1 = $c2; $c2 = $t; }
  if($c1 < 0) { if($c2 < 0) { return "ext_bb_bb"; } else { return "ext_bb_${c2}"; }}
  else { return "ext_${c1}_${c2}"; }
}

sub compute_interior_bandwidth {
  my ($c1, $c2) = @_;
  if($c1 > $c2) { my $t = $c1; $c1 = $c2; $c2 = $t; }
  if($c1 < 0) { if($c2 < 0) { return "int_bb_bb"; } else { return "int_bb_${c2}"; }}
  else { return "int_${c1}_${c2}"; }
}

sub compute_access_bandwidth {
  my ($c1, $c2) = @_;
  #if($c1 > $c2) { my $t = $c1; $c1 = $c2; $c2 = $t; }
  if($c1 < 0) { return "acc_bb_${c2}"; }
  else { return "acc_${c1}_${c2}"; }
}

sub print_summary {
  my $nrouters = 0; # number of routers internal to the AS
  my $nextrouters = 0; # number of external routers
  my $nlinks = 0; # number of internal links
  my $nlinks_wgt = 0; # number of internal links with weights
  my $nextlinks = 0; # number of external links
  my $maxcutoff = -1;
  my %cutoffs = ();
  print STDERR "-----------------------------------------------------\n";
  print STDERR "      \trouters\text_rt\tlinks\text_lnk\tl_wgt\n";
  print STDERR "-----------------------------------------------------\n";
  foreach my $asnum (sort { $a <=> $b } keys %routers) {
    my $n = scalar(keys %{$routers{$asnum}});
    $nrouters += $n;

    foreach my $uid (keys %{$routers{$asnum}}) {
      my $c = 1000;
      if(defined $routers{$asnum} &&
	 defined $routers{$asnum}{$uid} &&
	 defined $routers{$asnum}{$uid}->{cutoff}) {
	$c = $routers{$asnum}{$uid}->{cutoff};
	if($c > $maxcutoff) { $maxcutoff = $c; }
      }
      if(defined $cutoffs{$asnum} &&
	 defined $cutoffs{$asnum}{$c}) {
	$cutoffs{$asnum}{$c}++;
      } else { $cutoffs{$asnum}{$c} = 1; }
    }

    my $next = scalar(keys %{$ext_routers{$asnum}});
    $nextrouters += $next;

    my $m = 0; my $mwgt = 0; my $mext = 0;
    foreach my $uid (sort { $a <=> $b } keys %{$neighbors{$asnum}}) {
      foreach my $nasnum (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}}) {
	foreach my $nuid (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}{$nasnum}}) {
	  if($asnum == $nasnum && $nuid >= 0) { 
	    $m++; 
	    if($neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{weight} > 0) { $mwgt++; }
	  } else { $mext++; }
	}
      }
    }
    $m /= 2.0; $mwgt /= 2.0;
    $nlinks += $m;
    $nlinks_wgt += $mwgt;
    $nextlinks += $mext; # external links (not access links) are counted twice

    print STDERR "AS$asnum\t$n\t$next\t$m\t$mext\t$mwgt\n";
  }
  print STDERR " total\t$nrouters\t$nextrouters\t$nlinks\t$nextlinks\t$nlinks_wgt\n";
  print STDERR "-----------------------------------------------------\n";

  print STDERR "Number of internal routers at each cutoff:\n";
  print STDERR "---------------------------------------------------------------------\n";
  print STDERR "cutoff";
  for(my $cc=-1; $cc<=$maxcutoff; $cc++) { print STDERR "\t$cc"; }
  print STDERR "\tunknown\n";
  print STDERR "---------------------------------------------------------------------\n";
  my %allcuts = ();
  foreach my $asnum (sort { $a <=> $b } keys %routers) {
    print STDERR "AS$asnum";
    for(my $cc=-1; $cc<=$maxcutoff; $cc++) { 
      my $ncuts = 0;
      if(defined $cutoffs{$asnum} && defined $cutoffs{$asnum}{$cc}) 
	{ $ncuts = $cutoffs{$asnum}{$cc}; }
      print STDERR "\t$ncuts";
      if(defined $allcuts{$cc}) { $allcuts{$cc} += $ncuts; }
      else { $allcuts{$cc} = $ncuts; }
    }
    my $ncuts = 0;
    if(defined $cutoffs{$asnum} && defined $cutoffs{$asnum}{1000}) 
      { $ncuts = $cutoffs{$asnum}{1000}; }
    print STDERR "\t$ncuts\n";
    if(defined $allcuts{1000}) { $allcuts{1000} += $ncuts; }
    else { $allcuts{1000} = $ncuts; }

    print STDERR "  accu";
    my $accuts = 0;
    for(my $cc=-1; $cc<=$maxcutoff; $cc++) { 
      my $ncuts = 0;
      if(defined $cutoffs{$asnum} && defined $cutoffs{$asnum}{$cc}) 
	{ $ncuts = $cutoffs{$asnum}{$cc}; }
      $accuts += $ncuts;
      print STDERR "\t$accuts";
    }
    my $ncuts = 0;
    if(defined $cutoffs{$asnum} && defined $cutoffs{$asnum}{1000}) 
      { $ncuts = $cutoffs{$asnum}{1000}; }
    $accuts += $ncuts;
    print STDERR "\t$accuts\n";
    print STDERR "---------------------------------------------------------------------\n";
  }
  my $nas = scalar(keys %routers);
  if($nas > 1) {
    print STDERR " total";
    for(my $cc=0; $cc<=$maxcutoff; $cc++) { 
      print STDERR "\t$allcuts{$cc}";
    }
    print STDERR "\t$allcuts{1000}\n";

    print STDERR "  accu";
    my $accuts = 0;
    for(my $cc=0; $cc<=$maxcutoff; $cc++) {
      $accuts += $allcuts{$cc};
      print STDERR "\t$accuts";
    }
    $accuts += $allcuts{1000};
    print STDERR "\t$accuts\n";
    print STDERR "---------------------------------------------------------------------\n";
  }

  print STDERR "BGP peering: fromAS => toAS:links ...\n";
  foreach my $asnum (sort {$a<=>$b} keys %peering) {
    print STDERR "  $asnum =>";
    my $cc = 0;
    foreach my $toasnum (sort {$a<=>$b} keys %{$peering{$asnum}}) {
      my $c = 0;
      foreach my $uid (keys %{$peering{$asnum}{$toasnum}}) {
	$c += scalar(keys %{$peering{$asnum}{$toasnum}{$uid}});
      }
      if($c > 0) { print STDERR " $toasnum:$c"; }
      $cc += $c;
    }
    print STDERR " (total=$cc)\n";
  }
}

sub print_everything {
  print STDERR "\n\nrocketfuel router-level map details:\n\n";

  # for each AS number requested
  foreach my $asnum (sort { $a <=> $b } keys %routers) {
    print STDERR "### AS ${asnum} ($asnum2ispname{$asnum}) ###\n\n";

    # print STDERR the list of internal routers
    my $n = scalar(keys %{$routers{$asnum}});
    print STDERR "-- INTERNAL ROUTERS: total=${n}\n\n";
    foreach my $uid (keys %{$routers{$asnum}}) {
      print STDERR "$asnum:$uid ";
      if(defined $routers{$asnum} &&
	 defined $routers{$asnum}{$uid} &&
	 defined $routers{$asnum}{$uid}->{location}) {
	print STDERR "$routers{$asnum}{$uid}->{location} $routers{$asnum}{$uid}->{latlong} ";
      } else { print STDERR "UNKNOWN LOCATION "; }
      my $nics = scalar(@{$routers{$asnum}{$uid}->{ipaddrs}});
      print STDERR "nics=${nics} ";
      if(defined $routers{$asnum} &&
	 defined $routers{$asnum}{$uid} &&
	 defined $routers{$asnum}{$uid}->{cutoff}) {
	if($routers{$asnum}{$uid}->{cutoff} < 0) { print STDERR "bb\n"; }
	else { print STDERR "r$routers{$asnum}{$uid}->{cutoff}\n"; }
      } else { print STDERR "UNKNOWN\n"; }
      for(my $x=0; $x<$nics; $x++) {
	print STDERR "  $routers{$asnum}{$uid}->{ipaddrs}[$x]";
	print STDERR "($routers{$asnum}{$uid}->{aliases}[$x])\n";
      }
      print STDERR "\n";
    }
    print STDERR "\n";

    # print STDERR the list of external routers (recall the list should be
    # enumerated from -1, -2, and so on).
    my $m = scalar(keys %{$ext_routers{$asnum}});
    print STDERR "-- EXTERNAL ROUTERS: total=$m\n\n";
    foreach my $extuid (keys %{$ext_routers{$asnum}}) {
      print STDERR "$asnum:-$extuid:";
      print STDERR " $ext_routers{$asnum}{$extuid}->{alias}";
      if(defined $ext_routers{$asnum} &&
	 defined $ext_routers{$asnum}{$extuid} &&
	 defined $ext_routers{$asnum}{$extuid}->{cutoff}) {
	if($ext_routers{$asnum}{$extuid}->{cutoff} < 0) { print STDERR " bb"; }
	else { print STDERR " r$ext_routers{$asnum}{$extuid}->{cutoff}"; }
      } else { print STDERR " UNKNOWN"; }	       
      if($ext_routers{$asnum}{$extuid}->{reference}) {
	print STDERR " <=> $ext_routers{$asnum}{$extuid}->{reference}";
      }
      print STDERR "\n";
    }
    print STDERR "\n";

    # print STDERR the links between the routers (sorted by $uid, peer AS
    # number, and peer router id)
    print STDERR "-- CONNECTIVITY:\n\n";
    foreach my $uid (sort { $a <=> $b } keys %{$neighbors{$asnum}}) {
      foreach my $nasnum (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}}) {
	foreach my $nuid (sort { $a <=> $b } keys %{$neighbors{$asnum}{$uid}{$nasnum}}) {
	  print STDERR "$asnum:$uid <=> $nasnum:$nuid ";
	  print STDERR "($neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{latency},";
	  print STDERR "$neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{weight},";
	  print STDERR "$neighbors{$asnum}{$uid}{$nasnum}{$nuid}->{linktype})\n";
	  #if($nuid >= 0 && !defined $neighbors{$nasnum}{$nuid}{$asnum}{$uid}) {
	  #  die "asymmetry: $asnum:$uid<>$nasnum:$nuid\n";
	  #}
	}
      }
      #print STDERR "\n";
    }
    print STDERR "\n";
  }

  # print STDERR peering among the requested ASes
  my $title_printed = 0;
  foreach my $asnum (sort {$a<=>$b} keys %peering) {
    foreach my $toasnum (sort {$a<=>$b} keys %{$peering{$asnum}}) {
      if(!$title_printed) {
	print STDERR "-- PEERING POINTS:\n";
	$title_printed = 1;
      }
      print STDERR "AS$asnum<=>AS$toasnum:\n";
      foreach my $uid (sort {$a<=>$b} keys %{$peering{$asnum}{$toasnum}}) {
	print STDERR " $asnum:$uid \[";
	foreach my $touid (sort {$a<=>$b} keys %{$peering{$asnum}{$toasnum}{$uid}}) {
	  print STDERR " $toasnum:$touid ($peering{$asnum}{$toasnum}{$uid}{$touid}->{relation})";
	}
	print STDERR " \]\n";
      }
      print STDERR "\n";
    }
  }
  print STDERR "\n";
}

1;
