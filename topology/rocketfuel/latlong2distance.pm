#!/usr/bin/perl -w
# dist -- find great-circle distance between two points on earth's surface
#                                                              -*- perl -*-
#
# This code was written in 1998 by Darrell Kindred <dkindred@cmu.edu>.
# I have released it into the public domain.  Do what you like with it,
# but if you make any modifications, please either list your name and
# describe the changes here, or remove my name and address above.
#
# This code is distributed without any warranty, express or implied. 
# 
# Calculate the great-circle distance and initial heading from one point on
# the Earth to another, given latitude & longitude.
#   usage: dist <location1> <location2>
#
# Calculations assume a spherical Earth with radius 6367 km.  
# (I think this should cause no more than 0.2% error.)
#
# Here are some examples of acceptable location formats:
#
#   40:26:46N,79:56:55W
#   40:26:46.302N 79:56:55.903W
#   40°26'21"N 79d58'36"W
#   40d 26' 21" N 79d 58' 36" W
#   40.446195N 79.948862W
#   40.446195N -79.948862E
#
# Be sure to quote arguments from the shell as necessary.
#
# For a good discussion of the formula used here for calculating distances,
# as well as several more and less accurate techniques, see
#    http://www.census.gov/cgi-bin/geo/gisfaq?Q5.1
# and
#    http://www.best.com/~williams/avform.htm

# Jason Liu: I made this script a separate module. 3/13/2003.

package latlong2distance;

require 5.001;
use strict;

#
# input: (city1_latlong, city2_latlong), latlong="40:26N 79:56W"
# output: distance (in meters)
#
our @EXPORT = qw(get_latlong_distance);

sub get_latlong_distance {
  my ($city1, $city2) = @_;
  if(!defined $city1 || !defined $city2) {
    my ($p, $f, $l) = caller();
    die "undefined arguments to get_latlong_distance: package $p file $f line $l\n";
  }
  my($lat1,$long1) = &parse_location($city1);
  my($lat2,$long2) = &parse_location($city2);

  my $dist = &great_circle_distance($lat1,$long1,$lat2,$long2);

#  my $heading = &radians_to_degrees(&initial_heading($lat1,$long1,
#						     $lat2,$long2));
#
#  my $meters_per_mile = 1609.344;
#  my $nautical_miles_per_mile = (5280/6076);
#
#  print "great-circle distance from ", &loc_to_string($lat1,$long1), " to ",
#    &loc_to_string($lat2,$long2), " is\n";
#
#  # round to 3 significant digits since that's all we are justified
#  # in printing given the spherical earth assumption.
#  printf("%8g km\n", &round_to_3($dist / 1000));
#  printf("%8g miles\n", &round_to_3($dist / $meters_per_mile));
#  printf("%8g nautical miles\n",
#	 &round_to_3($dist / $meters_per_mile * $nautical_miles_per_mile));
#
#  printf("initial heading: %.0f degrees (%s)\n",
#	 $heading, &heading_string($heading));

  return $dist;
}

# given coordinates of two places in radians, compute distance in meters
sub great_circle_distance {
    my ($lat1,$long1,$lat2,$long2) = @_;

    # approx radius of Earth in meters.  True radius varies from
    # 6357km (polar) to 6378km (equatorial).
    my $earth_radius = 6367000;

    my $dlon = $long2 - $long1;
    my $dlat = $lat2 - $lat1;
    my $a = (sin($dlat / 2)) ** 2 
	    + cos($lat1) * cos($lat2) * (sin($dlon / 2)) ** 2;
    my $d = 2 * atan2(sqrt($a), sqrt(1 - $a));

    # This is a simpler formula, but it's subject to rounding errors
    # for small distances.  See http://www.census.gov/cgi-bin/geo/gisfaq?Q5.1
    # my $d = &acos(sin($lat1) * sin($lat2)
    #               + cos($lat1) * cos($lat2) * cos($long1-$long2));

    return $earth_radius * $d;
}

# compute the initial bearing (in radians) to get from lat1/long1 to lat2/long2
sub initial_heading {
    my ($lat1,$long1,$lat2,$long2) = @_;

    BEGIN {
	$::pi = 4 * atan2(1,1);
    }

    # note that this is the same $d calculation as above.  
    # duplicated for clarity.
    my $dlon = $long2 - $long1;
    my $dlat = $lat2 - $lat1;
    my $a = (sin($dlat / 2)) ** 2 
	    + cos($lat1) * cos($lat2) * (sin($dlon / 2)) ** 2;
    my $d = 2 * atan2(sqrt($a), sqrt(1 - $a));
    
    my $heading = acos((sin($lat2) - sin($lat1) * cos($d))
                   / (sin($d) * cos($lat1)));
    if (sin($long2 - $long1) < 0) {
	$heading = 2 * $::pi - $heading;
    }
    return $heading;
}

# return an angle in radians, between 0 and pi, whose cosine is x
sub acos {
    my($x) = @_;
    die "bad acos argument ($x)\n" if (abs($x) > 1.0);
    return atan2(sqrt(1 - $x * $x), $x);
}

# round to 3 significant digits
sub round_to_3 {
    my ($num) = @_;
    my ($lg,$round);
    if ($num == 0) {
	return 0;
    }
    $lg = int(log(abs($num)) / log(10.0));	# log base 10 of num
    $round = 10 ** ($lg - 2);
    return int($num / $round + 0.5) * $round;
}

# round to nearest integer
sub round_to_int {
    return int(abs($_[0]) + 0.5) * ($_[0] < 0 ? -1 : 1);
}

# print a location in a canonical form
sub loc_to_string {
    my($lat,$long) = @_;
    
    $lat  = &radians_to_degrees($lat);
    $long  = &radians_to_degrees($long);

    my $ns = "N";
    my $ew = "E";

    if ($lat < 0) {
	$lat = -$lat;
	$ns = "S";
    }
    if ($long < 0) {
	$long = -$long;
	$ew = "W";
    }
    $lat = int($lat * 3600 + 0.5);
    my $lat_string = sprintf("%d:%02d:%02d%s",
			     int($lat/3600), 
			     int(($lat - 3600*int($lat / 3600))/60),
			     $lat - 60*int($lat / 60),
			     $ns);
    $long = int($long * 3600 + 0.5);
    my $long_string = sprintf("%d:%02d:%02d%s",
			      int($long/3600), 
			      int(($long - 3600*int($long / 3600))/60),
			      $long - 60*int($long / 60),
			      $ew);

    return "$lat_string $long_string";
}

# convert a string which looks like "34:45:12N,15:34:10W" into a pair
# of degrees.  Also accepts "34.233N,90.134E" etc.
sub parse_location {
    my($str) = @_;
    my($lat,$long);
    
    if ($str =~ /^([0-9:.\260'"d -]*)([NS])[, ]+([0-9:.\260'"d -]*)([EW])$/i) {
	return undef if (!defined($lat = &parse_degrees($1)));
	$lat *= (($2 eq "N" || $2 eq "n") ? 1.0 : -1.0);
	return undef if (!defined($long = &parse_degrees($3)));
	$long *= (($4 eq "E" || $4 eq "e") ? 1.0 : -1.0);
	return(&degrees_to_radians($lat), &degrees_to_radians($long));
    } else {
	return undef;
    }
}

# given a bearing in degrees, return a string "north", "southwest", 
# "east-southeast", etc.
sub heading_string {
    my($deg) = @_;
    my($rounded,$s); 
    my(@dirs) = ("north","east","south","west"); 
    $rounded = &round_to_int($deg / 22.5) % 16; 
    if (($rounded % 4) == 0) { 
        $s = $dirs[$rounded/4]; 
    } else { 
        $s = $dirs[2 * int(((int($rounded / 4) + 1) % 4) / 2)]; 
        $s .= $dirs[1 + 2 * int($rounded / 8)]; 
        if ($rounded % 2 == 1) { 
	    $s = $dirs[&round_to_int($rounded/4) % 4] . "-" . $s;
        } 
    } 
    return $s; 
} 

BEGIN { $::pi = 4 * atan2(1,1); }
sub degrees_to_radians {
    return $_[0] * $::pi / 180.0;
}
sub radians_to_degrees {
    return $_[0] * 180.0 / $::pi;
}

# convert a string like 34:45:12.34 or 38:40 or 34.124 or 34d45'12.34"
# or 25° 02' 30" to degrees
# (also handles a leading `-')
sub parse_degrees {
    my($str) = @_;
    my($d,$m,$s,$sign);

    # yeah, this could probably be done with one regexp.
    if ($str =~ /^\s*(-?)([\d.]+)\s*(:|d|\260)\s*([\d.]+)\s*(:|\')\s*([\d.]+)\s*\"?\s*$/) {
	$sign = ($1 eq "-") ? -1.0 : 1.0;
	$d = $2 + 0.0;
	$m = $4 + 0.0;
	$s = $6 + 0.0;
    } elsif ($str =~ /^\s*(-?)([\d.]+)\s*(:|d|\260)\s*([\d.]+)(\')?\s*$/) {
	$sign = ($1 eq "-") ? -1.0 : 1.0;
	$d = $2 + 0.0;
	$m = $4 + 0.0;
	$s = 0.0;
    } elsif ($str =~ /^\s*(-?)([\d.]+)(d|\260)?\s*$/) {
	$sign = ($1 eq "-") ? -1.0 : 1.0;
	$d = $2 + 0.0;
	$m = 0.0;
	$s = 0.0;
    } else {
	die "parse_degrees: can't parse $str\n";
    }
    return ($sign * ($d + ($m / 60.0) + ($s / 3600.0)));
}

1;
