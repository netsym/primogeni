#!/usr/bin/perl -w
use strict;

my @files;
my $threshold = 20;
my $level = 2;
my $level_single = 7;
my $latcenter;
my $longcenter;

sub usage() {
  print "\nUsage: perl $0 [options] file0 [file1 ...]\n";
  print "Options:\n";
  print "  -h/-help/--help : this message\n";
  print "  -thrsh <threshold>   : threshold to determine whether the pairs match or not (defaul: 20 km)\n";
  exit 1;
}

print "perl $0";
foreach my $argv (@ARGV) { print " $argv"; }
print "\n";

while (scalar(@ARGV) > 0) {
  my $argv = shift @ARGV;
  if ($argv =~ /^-/) {
    if ($argv eq '-h' or $argv eq '--help' or $argv eq '-help') {
      usage();
    } elsif ($argv eq '-thrsh') {
      $argv = shift @ARGV;
      if (defined $argv and $argv =~ /\d+\.?\d*/) {
        $threshold = $argv;
      } else {
        print STDERR "invalid -thrsh option\n";
        usage();
      }
    } else {
      print STDERR "unrecognized option: $argv\n";
      usage();
    }
  } else { push @files, $argv; }
}

if ((scalar @files) == 0) { usage(); }

foreach my $file (@files) {
  my (%dismatch, %error, %match);
  &readDistance($file, \%dismatch, \%error, \%match);
  my $out_file = $file;
  if ($out_file =~ /(.+)\.distance$/) { $out_file = $1; }
  &print_denoise_gmap($out_file, \%dismatch, \%error, \%match);
}

sub readDistance {
  my ($file, $ref_dismatch, $ref_error, $ref_match) = @_;
  my $lat_min = 180;
  my $lat_max = -180;
  my $long_min = 120;
  my $long_max = -240;

  open IN, "$file" || die "Can't open file '$file' for reading: $!";
  while (<IN>) {
    chomp;
    s/\#.*$//;
    if (/^(.*)\t\((.*)\)\t\((.*),(.*)\)\t(.*)\t(.*)$/) {
      my $location = $1;
      my $latlong = $2;
      my ($lat, $long) = split /,/, $latlong;
      my $tolat = $3;
      my $tolong = $4;
      my $distance = $5;
      my $errorcode = $6;
      if ($distance eq '-') {
        $$ref_error{$location}->{tolat} = $tolat;
        $$ref_error{$location}->{tolong} = $tolong;
        $$ref_error{$location}->{errorcode} = $errorcode;
      } elsif($distance <= $threshold) {
        $$ref_match{$location}->{lat} = $lat;
        $$ref_match{$location}->{long} = $long;
        $$ref_match{$location}->{tolat} = $tolat;
        $$ref_match{$location}->{tolong} = $tolong;
        $$ref_match{$location}->{distance} = $distance;
      } else {
        $$ref_dismatch{$location}->{lat} = $lat;
        $$ref_dismatch{$location}->{long} = $long;
        $$ref_dismatch{$location}->{tolat} = $tolat;
        $$ref_dismatch{$location}->{tolong} = $tolong;
        $$ref_dismatch{$location}->{distance} = $distance;
        if($lat < $lat_min) { $lat_min = $lat; }
        if($lat > $lat_max) { $lat_max = $lat; }
        my $mylong = $long;
        if($long > 120) { $mylong = $long-360; }
        if($mylong < $long_min) { $long_min = $mylong; }
        if($mylong > $long_max) { $long_max = $mylong; }

        if($tolat < $lat_min) { $lat_min = $tolat; }
        if($tolat > $lat_max) { $lat_max = $tolat; }
        $mylong = $tolong;
        if($tolong > 120) { $mylong = $tolong-360; }
        if($mylong < $long_min) { $long_min = $mylong; }
        if($mylong > $long_max) { $long_max = $mylong; }

      }
    }
  }
  close IN;

  $latcenter = ($lat_min+$lat_max)/2;
  $longcenter = ($long_min+$long_max)/2;
}

sub print_denoise_gmap {
  my ($out_file, $ref_dismatch, $ref_error, $ref_match) = @_;
  my $file = $out_file."_thrsh".$threshold.".denoise.html";
  my %reasons =
    ( "200" => "Success",
      "400" => "Bad Request: A directions request could not be successfully parsed.",
      "403" => "Error 403: Probably an incorrect error caused by a bug in the handling of invalid JSON.",
      "500" => "Server Error: The geocoding request could not be successfully processed.",
      "601" => "Missing Address: The address was either missing or had no value.",
      "602" => "Unknown Address:  No corresponding geographic location could be found for the specified address.",
      "603" => "Unavailable Address:  The geocode for the given address cannot be returned due to legal or contractual reasons.",
      "604" => "Unknown Directions: The GDirections object could not compute directions between the points mentioned in the query.",
      "610" => "Bad Key: The API key is either invalid or does not match the domain for which it was given",
      "620" => "Too Many Queries: The daily geocoding quota for this site has been exceeded.",
    );

  open OUT, ">$file" || die "Can't open file '$file' for writing: $!";
  print OUT '<!DOCTYPE html "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">'."\n".
            '<html xmlns="http://www.w3.org/1999/xhtml">'."\n".
            '  <head>'."\n".
            '    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>'."\n".
            '    <title>Denoise - RocketFuel Router Level Topology</title>'."\n".
            '    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=abcdefg&sensor=false"'."\n".
            '            type="text/javascript"></script>'."\n".
            '    <script type="text/javascript">'."\n".
            "\n".
            '    var map;'."\n".
#            '    var info_match = "Matched \'location\' and \'latlong\' pairs(unit: km):<br>";'."\n".
            '    var info_dismatch = "Dismatched \'location\' and \'latlong\' pairs(unit: km):<br>";'."\n".
            '    var info_error = "Error locations:<br>";'."\n".
            '    var level = '.$level.';'."\n".
            '    var level_single = '.$level_single.';'."\n".
            '    var latcenter = '.$latcenter.';'."\n".
            '    var longcenter = '.$longcenter.';'."\n".
            "\n".
            '    function createMarker (mypos, mytitle, myinfo) {'."\n".
            '      var marker = new GMarker(mypos, {title: mytitle});'."\n".
            '      GEvent.addListener(marker, "click", function() {'."\n".
            '        marker.openInfoWindowHtml(myinfo);'."\n".
            '      });'."\n".
            '      return marker;'."\n".
            '    }'."\n".
            "\n".
            '    function drawMap(location, lat, long, tolat, tolong, ifClear) {'."\n".
            '      var point = new GLatLng(lat, long);'."\n".
            '      var topoint = new GLatLng(tolat, tolong);'."\n".
            '      if (ifClear) {'."\n".
            '        map.clearOverlays();'."\n".
            '        map.setCenter(new GLatLng((lat+tolat)/2, (long+tolong)/2), level_single);'."\n".
            '      }'."\n".
            '      map.addOverlay(createMarker(point, location, \'Location: <b>\'+location+\'</b><br>LatLong : (<b>\'+lat.toFixed(4)+\',\'+long.toFixed(4)+\'</b>)\'));'."\n".
            '      map.addOverlay(createMarker(topoint, \'(\'+tolat.toFixed(4)+\',\'+tolong.toFixed(4)+\')\', \'LatLong : (<b>\'+tolat.toFixed(4)+\',\'+tolong.toFixed(4)+\'</b>)\'));'."\n".
            '      map.addOverlay(new GPolyline([point, topoint],"ff0000",3));'."\n".
            '    }'."\n".
            "\n".
            '    function initialize() {'."\n".
            '      if (GBrowserIsCompatible()) {'."\n".
            '        map = new GMap2(document.getElementById("map_canvas"));'."\n".
            '        map.setCenter(new GLatLng(latcenter, longcenter), level);'."\n".
            '        var mapControl = new GMapTypeControl();'."\n".
            '        map.addControl(mapControl);'."\n".
            '        map.addControl(new GLargeMapControl());'."\n".
            "\n".
            '        document.getElementById("info_dismatch").innerHTML = info_dismatch;'."\n\n";

  foreach my $location (sort keys %{$ref_dismatch}) {
    my $lat = $$ref_dismatch{$location}->{lat};
    my $long = $$ref_dismatch{$location}->{long};
    my $tolat = $$ref_dismatch{$location}->{tolat};
    my $tolong = $$ref_dismatch{$location}->{tolong};
    my $distance = $$ref_dismatch{$location}->{distance};
    print OUT '        drawMap(\''.$location.'\', '.$lat.', '.$long.', '.$tolat.', '.$tolong.', false);'."\n".
              '        document.getElementById("info_dismatch").innerHTML += "<a href=\"javascript:void(0)\" onclick=\"drawMap(\''.$location.'\', '.$lat.', '.$long.', '.$tolat.', '.$tolong.', true);return false;\">\'<b>'.$location.'</b>\'('.$lat.','.$long.')--(<b>'.$tolat.','.$tolong.'</b>): '.$distance.'</a><br>";'."\n";
  }

  print OUT "\n";
  print OUT '        document.getElementById("info_error").innerHTML += "<br>"+info_error;'."\n\n";

  foreach my $location (sort keys %{$ref_error}) {
    my $tolat = $$ref_error{$location}->{tolat};
    my $tolong = $$ref_error{$location}->{tolong};
    my $errorcode = $$ref_error{$location}->{errorcode};
    my $reason = $reasons{$errorcode};
    print OUT '        document.getElementById("info_error").innerHTML += "\'<b>'.$location.'</b>\'(?)--(<b>'.$tolat.','.$tolong.'</b>): <b>'.$errorcode.'('.$reason.')</b><br>";'."\n";
  }

  print OUT "\n";

#  print OUT '        document.getElementById("info_match").innerHTML = info_match;'."\n\n";

#  foreach my $location (sort keys %{$ref_match}) {
#    my $tolat = $$ref_match{$location}->{tolat};
#    my $tolong = $$ref_match{$location}->{tolong};
#    my $distance = $$ref_match{$location}->{distance};
#    print OUT '        document.getElementById("info_match").innerHTML += "\'<b>'.$location.'</b>\'--(<b>'.$tolat.','.$tolong.'</b>): '.$distance.'<br>";'."\n";
#  }

#  print OUT "\n";
  my $dismatch_num = scalar keys %{$ref_dismatch};
  my $error_num = scalar keys %{$ref_error};
  my $match_num = scalar keys %{$ref_match};

  print OUT '        document.getElementById("info_error").innerHTML += \'<br>\';'."\n".
            '        document.getElementById("info_error").innerHTML += \'<table><tr><td width="150"><b># Dismatch</b></td><td width="100"><b>'.$dismatch_num.'</b></td></tr><tr><td width="150"><b># Error</b></td><td width="100"><b>'.$error_num.'</b></td></tr><tr><td width="150"><b># Match</b></td><td width="100"><b>'.$match_num.'</b></td></tr></table>\';'."\n".
            '        document.getElementById("info_error").innerHTML += \'<br>\';'."\n".
            '        document.getElementById("info_error").innerHTML += \'<table><tr><td width="150"><b># Threshold</b></td><td width="100"><b>'.$threshold.'</b></td></tr></table>\';'."\n".
            '      } else {'."\n".
            '        alert("Sorry, the Google Maps API is not compatible with this browser");'."\n".
            '      }'."\n".
            '    }'."\n".
            "\n".
            '    </script>'."\n".
            '  </head>'."\n".
            '  <body onload="initialize()" onunload="GUnload()">'."\n".
            '    <div id="map_canvas" style="width: 50%; height: 480px; float: left; border; 1px solid black"></div>'."\n".
            '    <div id="info_dismatch" style="width:49%; float: right"></div>'."\n".
            '    <div id="info_error" style="width:50%; float; bottom"></div>'."\n".
            '  </body>'."\n".
            '</html>'."\n";

  close OUT;
}
