package city2latlong;

require 5.001;
use strict;

require LWP;

#
# input: latlong cache file, list of city names (in the format: "city,state,country")
# output: a hash city_name=>"latitude,longitude"
#
our @EXPORT = qw(get_city_latlong formalize_cityname read_cache write_cache get_latlong);

my %state2long = (
  "AK" => "ALASKA",
  "AL" => "ALABAMA",
  "AR" => "ARKANSAS",
  "AZ" => "ARIZONA",
  "CA" => "CALIFORNIA",
  "CO" => "COLORADO",
  "CT" => "CONNECTICUT",
  "DC" => "DISTRICT OF COLUMBIA",
  "DE" => "DELAWARE",
  "FL" => "FLORIDA",
  "GA" => "GEORGIA",
  "HI" => "HAWAII",
  "IA" => "IOWA",
  "ID" => "IDAHO",
  "IL" => "ILLINOIS",
  "IN" => "INDIANA",
  "KS" => "KANSAS",
  "KY" => "KENTUCKY",
  "LA" => "LOUISIANA",
  "MA" => "MASSACHUSETTS",
  "MD" => "MARYLAND",
  "ME" => "MAINE",
  "MI" => "MICHIGAN",
  "MN" => "MINNESOTA",
  "MO" => "MISSOURI",
  "MS" => "MISSISSIPPI",
  "MT" => "MONTANA",
  "NC" => "NORTH CAROLINA",
  "ND" => "NORTH DAKOTA",
  "NE" => "NEBRASKA",
  "NH" => "NEW HAMPSHIRE",
  "NJ" => "NEW JERSEY",
  "NM" => "NEW MEXICO",
  "NV" => "NEVADA",
  "NY" => "NEW YORK",
  "OH" => "OHIO",
  "OK" => "OKLAHOMA",
  "OR" => "OREGON",
  "PA" => "PENNSYLVANIA",
  "RI" => "RHODE ISLAND",
  "SC" => "SOUTH CAROLINA",
  "SD" => "SOUTH DAKOTA",
  "TN" => "TENNESSEE",
  "TX" => "TEXAS",
  "UT" => "UTAH",
  "VA" => "VIRGINIA",
  "VT" => "VERMONT",
  "WA" => "WASHINGTON",
  "WI" => "WISCONSIN",
  "WV" => "WEST VIRGINIA",
  "WY" => "WYOMING",
);

my %state2short = (
  "ALABAMA" => "AL",
  "ALASKA" => "AK",
  "ARIZONA" => "AZ",
  "ARKANSAS" => "AR",
  "CALIFORNIA" => "CA",
  "COLORADO" => "CO",
  "CONNECTICUT" => "CT",
  "DELAWARE" => "DE",
  "DISTRICT OF COLUMBIA" => "DC",
  "FLORIDA" => "FL",
  "GEORGIA" => "GA",
  "HAWAII" => "HI",
  "IDAHO" => "ID",
  "ILLINOIS" => "IL",
  "INDIANA" => "IN",
  "IOWA" => "IA",
  "KANSAS" => "KS",
  "KENTUCKY" => "KY",
  "LOUISIANA" => "LA",
  "MAINE" => "ME",
  "MARYLAND" => "MD",
  "MASSACHUSETTS" => "MA",
  "MICHIGAN" => "MI",
  "MINNESOTA" => "MN",
  "MISSISSIPPI" => "MS",
  "MISSOURI" => "MO",
  "MONTANA" => "MT",
  "NEBRASKA" => "NE",
  "NEVADA" => "NV",
  "NEW HAMPSHIRE" => "NH",
  "NEW JERSEY" => "NJ",
  "NEW MEXICO" => "NM",
  "NEW YORK" => "NY",
  "NORTH CAROLINA" => "NC",
  "NORTH DAKOTA" => "ND",
  "OHIO" => "OH",
  "OKLAHOMA" => "OK",
  "OREGON" => "OR",
  "PENNSYLVANIA" => "PA",
  "RHODE ISLAND" => "RI",
  "SOUTH CAROLINA" => "SC",
  "SOUTH DAKOTA" => "SD",
  "TENNESSEE" => "TN",
  "TEXAS" => "TX",
  "UTAH" => "UT",
  "VERMONT" => "VT",
  "VIRGINIA" => "VA",
  "WASHINGTON" => "WA",
  "WEST VIRGINIA" => "WV",
  "WISCONSIN" => "WI",
  "WYOMING" => "WY",
);

my %nations = (
  "AFGHANISTAN"=>"AF",
  "ALBANIA"=>"AL",
  "ALGERIA"=>"DZ",
  "AMERICAN SAMOA"=>"AS",
  "ANDORRA"=>"AD",
  "ANGOLA"=>"AO",
  "ANGUILLA"=>"AI",
  "ANTARCTICA"=>"AQ",
  "ANTIGUA AND BARBUDA"=>"AG",
  "ARGENTINA"=>"AR",
  "ARMENIA"=>"AM",
  "ARUBA"=>"AW",
  "AUSTRALIA"=>"AU",
  "AUSTRIA"=>"AT",
  "AZERBAIJAN"=>"AZ",
  "BAHAMAS"=>"BS",
  "BAHRAIN"=>"BH",
  "BANGLADESH"=>"BD",
  "BARBADOS"=>"BB",
  "BELARUS"=>"BY",
  "BELGIUM"=>"BE",
  "BELIZE"=>"BZ",
  "BENIN"=>"BJ",
  "BERMUDA"=>"BM",
  "BHUTAN"=>"BT",
  "BOLIVIA"=>"BO",
  "BOSNIA & HERZEGOWINA"=>"BA",
  "BOTSWANA"=>"BW",
  "BOUVET ISLAND"=>"BV",
  "BRAZIL"=>"BR",
  "BRITISH INDIAN OCEAN TERRITORY"=>"IO",
  "BRUNEI DARUSSALAM"=>"BN",
  "BULGARIA"=>"BG",
  "BURKINA FASO"=>"BF",
  "BURMA"=>"MM",
  "BURUNDI"=>"BI",
  "BYELORUSSIA"=>"BY",
  "CAMBODIA"=>"KH",
  "CAMEROON"=>"CM",
  "CANADA"=>"CA",
  "CAPE VERDE"=>"CV",
  "CAYMAN ISLANDS"=>"KY",
  "CENTRAL AFRICAN REPUBLIC"=>"CF",
  "CHAD"=>"TD",
  "CHILI"=>"CL",
  "CHINA"=>"CN",
  "CHRISTMAS ISLAND"=>"CX",
  "COCOS (KEELING) ISLANDS"=>"CC",
  "COLOMBIA"=>"CO",
  "COMOROS"=>"KM",
  "CONGO"=>"CG",
  "COOK ISLANDS"=>"CK",
  "COSTA RICA"=>"CR",
  "COTE D'IVOIRE"=>"CI",
  "CROATIA"=>"HR",
  "CUBA"=>"CU",
  "CYPRUS"=>"CY",
  "CZECH REPUBLIC"=>"CZ",
  "DENMARK"=>"DK",
  "DJIBOUTI"=>"DJ",
  "DOMINICA"=>"DM",
  "DOMINICAN REPUBLIC"=>"DO",
  "EAST TIMOR"=>"TP",
  "ECUADOR"=>"EC",
  "EGYPT"=>"EG",
  "EL SALVADOR"=>"SV",
  "EQUATORIAL GUINEA"=>"GQ",
  "ERITREA"=>"ER",
  "ESTONIA"=>"EE",
  "ETHIOPIA"=>"ET",
  "FALKLAND ISLANDS"=>"FK",
  "FAROE ISLANDS"=>"FO",
  "FIJI ISLANDS"=>"FJ",
  "FINLAND"=>"FI",
  "FRANCE"=>"FR",
  "FRANCE, METROPOLITAN"=>"FX",
  "FRENCH GUIANA"=>"GF",
  "FRENCH POLYNESIA"=>"PF",
  "FRENCH SOUTHERN AND ANTARCTIC TERRITORIES"=>"TF",
  "GABON"=>"GA",
  "GAMBIA"=>"GM",
  "GEORGIA"=>"GE",
  "GERMANY"=>"DE",
  "GHANA"=>"GH",
  "GIBRALTAR"=>"GI",
  "GREECE"=>"GR",
  "GREENLAND"=>"GL",
  "GRENADA"=>"GD",
  "GUADELOUPE"=>"GP",
  "GUAM"=>"GU",
  "GUATEMALA"=>"GT",
  "GUINEA"=>"GN",
  "GUINEA-BISSAU"=>"GW",
  "GUYANA"=>"GY",
  "HAITI"=>"HT",
  "HEARD AND MCDONALD ISLANDS"=>"HM",
  "HOLY"=>"VA",
  "HONDURAS"=>"HN",
  "HONG KONG"=>"HK",
  "HRVATSKA"=>"HR",
  "HUNGARY"=>"HU",
  "ICELAND"=>"IS",
  "INDIA"=>"IN",
  "INDONESIA"=>"ID",
  "IRAN, ISLAMIC REPUBLIC OF"=>"IR",
  "IRAQ"=>"IQ",
  "IRELAND"=>"IE",
  "ISRAEL"=>"IL",
  "ITALY"=>"IT",
  "IVORY COAST"=>"CI",
  "JAMAICA"=>"JM",
  "JAPAN"=>"JP",
  "JORDAN"=>"JO",
  "KAMPUCHEA"=>"FORMER",
  "KAZAKHSTAN"=>"KZ",
  "KENYA"=>"KE",
  "KIRIBATI"=>"KI",
  "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF"=>"KP",
  "NORTH KOREA"=>"KP",
  "KOREA, REPUBLIC OF"=>"KR",
  "SOUTH KOREA"=>"KR",
  "KOREA"=>"KR",
  "KUWAIT"=>"KW",
  "KYRGYZSTAN"=>"KG",
  "LAO, PEOPLE'S DEMOCRATIC REPUBLIC"=>"LA",
  "LAO"=>"LA",
  "LATVIA"=>"LA",
  "LEBANON"=>"LB",
  "LESOTHO"=>"LS",
  "LIBERIA"=>"LR",
  "LIBYAN ARAB JAMAHIRIYA"=>"LY",
  "LIECHTENSTEIN"=>"LI",
  "LITHUANIA"=>"LT",
  "LUXEMBOURG"=>"LU",
  "MACAO"=>"MO",
  "MACAU"=>"MO",
  "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF"=>"MK",
  "MADAGASCAR"=>"MG",
  "MALAWI"=>"MW",
  "MALAYSIA"=>"MY",
  "MALDIVES"=>"MV",
  "MALI"=>"ML",
  "MALTA"=>"MT",
  "MALVINAS"=>"SEE",
  "MARSHALL ISLANDS"=>"MH",
  "MARTINIQUE"=>"MQ",
  "MAURITANIA"=>"MR",
  "MAURITIUS"=>"MU",
  "MAYOTTE"=>"YT",
  "MEXICO"=>"MX",
  "MICRONESIA, FEDERATED STATES OF"=>"FM",
  "MOLDOVA, REPUBLIC OF"=>"MD",
  "MONACO"=>"MC",
  "MONGOLIA"=>"MN",
  "MONTSERRAT"=>"MS",
  "MOROCCO"=>"MA",
  "MOZAMBIQUE"=>"MZ",
  "MYANMAR"=>"MM",
  "NAMIBIA"=>"NA",
  "NAURU"=>"NR",
  "NEPAL"=>"NP",
  "NETHERLANDS"=>"NL",
  "NETHERLANDS ANTILLES"=>"AN",
  "NEW CALEDONIA"=>"NC",
  "NEW ZEALAND"=>"NZ",
  "NICARAGUA"=>"NI",
  "NIGER"=>"NE",
  "NIGERIA"=>"NG",
  "NIUE"=>"NU",
  "NORFOLK ISLAND"=>"NF",
  "NORTHERN MARIANA ISLANDS"=>"MP",
  "NORWAY"=>"NO",
  "OMAN"=>"OM",
  "PAKISTAN"=>"PK",
  "PALAU"=>"PW",
  "PANAMA"=>"PA",
  "PAPUA NEW GUINEA"=>"PG",
  "PARAGUAY"=>"PY",
  "PERU"=>"PE",
  "PHILIPPINES"=>"PH",
  "PITCAIRN ISLAND"=>"PN",
  "POLAND"=>"PL",
  "PORTUGAL"=>"PT",
  "PUERTO RICO"=>"PR",
  "QATAR"=>"QA",
  "REUNION"=>"RE",
  "ROMANIA"=>"RO",
  "RUSSIAN FEDERATION"=>"RU",
  "RUSSIA"=>"RU",
  "ROSSIYA"=>"RU",
  "RWANDA"=>"RW",
  "SAINT"=>"SEE",
  "SAINT KITTS (CHRISTOPHER) AND NEVIS"=>"KN",
  "SAINT LUCIA"=>"LC",
  "SAINT VINCENT AND THE GRENADINES"=>"VC",
  "SAMOA"=>"WS",
  "SAN MARINO"=>"SM",
  "SAO TOME AND PRINCIPE"=>"ST",
  "SAUDI ARABIA"=>"SA",
  "SENEGAL"=>"SN",
  "SEYCHELLES"=>"SC",
  "SIERRA LEONE"=>"SL",
  "SINGAPORE"=>"SG",
  "SLOVAKIA"=>"SK",
  "SLOVENIA"=>"SI",
  "SOLOMON ISLANDS"=>"SB",
  "SOMALIA"=>"SO",
  "SOUTH AFRICA"=>"ZA",
  "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS"=>"GS",
  "SPAIN"=>"ES",
  "SRI LANKA"=>"LK",
  "ST HELENA"=>"SH",
  "ST PIERRE AND MIQUELON"=>"PM",
  "SUDAN"=>"SD",
  "SURINAME"=>"SR",
  "SVALBARD AND JAN MAYEN ISLANDS"=>"SJ",
  "SWAZILAND"=>"SZ",
  "SWEDEN"=>"SE",
  "SWITZERLAND"=>"CH",
  "SYRIAN ARAB REPUBLIC"=>"SY",
  "SYRIA"=>"SY",
  "TAIWAN"=>"TW",
  "TAJIKISTAN"=>"TJ",
  "TANZANIA"=>"TZ",
  "THAILAND"=>"TH",
  "TOGO"=>"TG",
  "TOKELAU"=>"TK",
  "TONGA"=>"TO",
  "TRINIDAD AND TOBAGO"=>"TT",
  "TUNISIA"=>"TN",
  "TURKEY"=>"TR",
  "TURKMENISTAN"=>"TM",
  "TURKS AND CAICOS ISLANDS"=>"TC",
  "TUVALU"=>"TV",
  "UGANDA"=>"UG",
  "UKRAINE"=>"UA",
  "UNION OF SOVIET SOCIALIST REPUBLICS"=>"SU",
  "UNITED ARAB EMIRATES"=>"AE",
  "UNITED KINGDOM"=>"GB",
  "UK"=>"GB",
  "ENGLAND"=>"GB",
  "UNITEDKINGDOM"=>"GB",
  "UNITED STATES OF AMERICA"=>"US",
  "UNITED STATES MINOR OUTLYING ISLANDS"=>"UM",
  "URUGUAY"=>"UY",
  "UZBEKISTAN"=>"UZ",
  "VANAUTU"=>"VU",
  "VATICAN CITY STATE"=>"VA",
  "VENEZUELA"=>"VE",
  "VIET NAM"=>"VN",
  "VIRGIN ISLANDS (BRITISH)"=>"VG",
  "VIRGIN ISLANDS (US)"=>"VI",
  "WALLIS AND FUTUNA ISLANDS"=>"WF",
  "WEST AFRICA"=>"XO",
  "WESTERN SAHARA"=>"EH",
  "YEMEN"=>"YE",
  "YUGOSLAVIA"=>"YU",
  "ZAIRE"=>"CD",
  "ZAMBIA"=>"ZM",
  "ZIMBABWE"=>"ZW"
);

my $urlprefix_astro = "http://www.astro.com/atlas/horoscope?country_list=&expr=";
my $urlsuffix_astro = "&submit=Search";

my $urlprefix_mit = "http://www.mit.edu:8001/geo?location=";
my $urlsuffix_mit = "";

my %citylatlong;

sub formalize_cityname {
  my ($city) = @_;
#  print "### $city=>";
  $city =~ s/\([^)]*\)//g;

  my ($c,$s,$n) = split /,/, uc($city);
  $c =~ s/^\s+//g; $c =~ s/\s+$//g;
  if(defined $s) {
    $s =~ s/\([^)]+\)//g;
    $s =~ s/^\s+//g; $s =~ s/\s+$//g;
  } else { 
    #print "*,*,*\n";
    return "*,*,*";
  }
  if(defined $n) {
    $n =~ s/^\s+//g; $n =~ s/\s+$//g;
  }
  if(defined $state2long{$s} || defined $state2short{$s}) {
    if(defined $state2short{$s}) { $s = $state2short{$s}; }
    $n = 'US';
  } else {
    if(defined $n) {
      if($s ne '*') {
	$s = '*';
	#die "Hur? <$c,$s,$n>\n";
      }
    } else { $n = $s; $s = '*'; }
    if(defined $nations{$n}) { $n = $nations{$n}; }
  }
#  print "$c,$s,$n\n";
  return "$c,$s,$n";
}

sub get_city_latlong {
  my ($cachefile, @citylist) = @_;
  my %latlonglist;

  read_cache($cachefile);
  foreach my $city (@citylist) {
    $city = formalize_cityname($city);
    my ($c,$s,$n) = split /,/, $city;
    my ($citylat, $citylong) = get_latlong($cachefile, $c, $s, $n);
    my $latlongstr;
    if(defined $citylat) { $latlongstr = "$citylat,$citylong"; }
    else { $latlongstr = "not available"; }
    $latlonglist{$city} = $latlongstr;
    #print "### $city => $latlonglist{$city}\n";
  }
  write_cache($cachefile);
  return %latlonglist;
}

my $latlong_lookup_idx;

sub read_cache {
  my ($cachefile) = @_;
  $latlong_lookup_idx = 0;

  open(IN_CACHE, "$cachefile") || return;
  while(<IN_CACHE>) {
    chop; s/\#.*$//g;
    if(/=>/) {
      my ($cityname, $cityloc) = split '=>';
      #print "$cityname => $cityloc\n";
      my ($citylat, $citylong) = split /,/, $cityloc;
      #$citylat =~ s/(\d+)(\w)(\d+)/\1:\3 \2/; $citylat = uc($citylat);
      #$citylong =~ s/(\d+)(\w)(\d+)/\1:\3 \2/; $citylong = uc($citylong);
      $citylatlong{uc($cityname)}->{latitude} = uc($citylat);
      $citylatlong{uc($cityname)}->{longitude} = uc($citylong);
    }
  }
  close(IN_CACHE);
}

sub write_cache {
  my ($cachefile) = @_;
  if($latlong_lookup_idx == 0) { return; }
  $latlong_lookup_idx = 0;

  open(OUT_CACHE, ">$cachefile") || die "Can't open $cachefile\n";
  print OUT_CACHE "# THIS FILE IS GENERATED AUTOMATICALLY; DON'T CHANGE!\n";
  foreach my $cityname (sort keys %citylatlong) {
    print OUT_CACHE "${cityname}=>$citylatlong{$cityname}->{latitude},";
    print OUT_CACHE "$citylatlong{$cityname}->{longitude}\n";
  }
  close(OUT_CACHE);
}

sub get_latlong {
  my ($cachefile, $city, $state, $country) = @_;
  my $citystr = "$city,$state,$country";
  if($citystr =~ /\*,\*/ ||
     !defined $city || !defined $state ||
     !defined $country) { 
    #die "malform: $citystr\n"; 
    return ("0:0W,0:0N");
  }

  if(!defined $citylatlong{$citystr}) {
    # 0=not found, 1=not sure, 2=definite
    print STDERR "getting location for \"$city,$state,$country\" from astro...\n";
    my ($level_1, $lat_1, $long_1) = get_latlong_astro($city, $state, $country);
    if($level_1 == 2) {
      $citylatlong{$citystr}->{latitude} = $lat_1;
      $citylatlong{$citystr}->{longitude} = $long_1;
    } else {
      print STDERR "getting location for \"$city,$state,$country\" from mit...\n";
      my ($level_2, $lat_2, $long_2) = get_latlong_mit($city, $state, $country);
      if($level_2 > $level_1) { 
	$citylatlong{$citystr}->{latitude} = $lat_2;
	$citylatlong{$citystr}->{longitude} = $long_2;
      } elsif($level_1 > 0 && $level_1 >= $level_2) {
	$citylatlong{$citystr}->{latitude} = $lat_1;
	$citylatlong{$citystr}->{longitude} = $long_1;
      } else {
	print STDERR "can't find location for $citystr\n";
	return ("0:0W,0:0N");
      }
    }
    $latlong_lookup_idx++; 
    if($latlong_lookup_idx == 10) {
      print STDERR "writing to cache...\n";
      write_cache($cachefile);
    }
  }
  return ($citylatlong{$citystr}->{latitude},
	  $citylatlong{$citystr}->{longitude});
}

sub get_latlong_astro {
  my ($city, $state, $country) = @_;

  my $citystr;
  if($state eq '*') { $citystr = $city.', '.$country; }
  else { $citystr = $city.', '.$state.', '.$country; }
  #$citystr = uc($citystr);

  $citystr =~ s/\s/+/g; $citystr =~ s/\,/\%2C/g; # make it like "atlanta%2C+ga%2C+us"
  my $url = "$urlprefix_astro$citystr$urlsuffix_astro";
  #print "Request: ($city, $state, $country) $url\n";

  my $agent = LWP::UserAgent->new;
  my $request = HTTP::Request->new(GET => $url);
  my $response = $agent->request($request);
  $response->is_success or return (0, 0, 0);
  my $result = $response->content;
  #print "$result\n";

  my @lines = split /\n/, $result;

  my $myoldlat;
  my $myoldlong;
  my $myoldlevel = 0;

  my $myplace;
  my $mylat;
  my $mylong;
  my $mystate;

  my $lineidx = 0;
  while($lineidx<=$#lines) {
    my $line = $lines[$lineidx++];
    if($line =~ /<li><a href="[^"]+">([^<]+)<\/a>.*<b>([a-zA-Z0-9]+)<\/b>, <b>([a-zA-Z0-9]+)<\/b>, ([a-zA-Z ]+)/) {
      $myplace = $1;
      $mylat = $2;
      $mylong = $3;
      $mystate = $4;

      $myplace = uc($myplace);
      $mystate = uc($mystate);
      $mylat =~ s/(\d+)(\w)(\d+)/$1:$3$2/; $mylat = uc($mylat);
      $mylong =~ s/(\d+)(\w)(\d+)/$1:$3$2/; $mylong = uc($mylong);

      if($myoldlevel < 1) { $myoldlevel = 1; $myoldlat = $mylat; $myoldlong = $mylong; }
      #print "$myplace, $mystate => ($mylat, $mylong)\n";

      if($myplace eq $city) {
        if($myoldlevel < 2) { $myoldlevel = 2; $myoldlat = $mylat; $myoldlong = $mylong; }

        if(($country eq 'US') &&
           ($state eq $mystate ||
            defined $state2long{$state} && $state2long{$state} eq $mystate)) {
          return (2, $mylat, $mylong);
        } elsif($country ne 'US' && $country eq $mystate) {
          return (2, $mylat, $mylong);
        }
      }
    }
  }
  if($myoldlevel > 0) { return (1, $myoldlat, $myoldlong); }
  else { return (0, 0, 0); }
}

sub get_latlong_mit {
  my ($city, $state, $country) = @_;

  my $citystr;
  if($state eq '*') { $citystr = $city.', '.$country; }
  else { $citystr = $city.', '.$state.', '.$country; }
  #$citystr = uc($citystr);

  $citystr =~ s/\s/+/g; $citystr =~ s/\,/\%2C/g;
  my $url = "$urlprefix_mit$citystr$urlsuffix_mit";

  #print "Request: $url\n";

  my $agent = LWP::UserAgent->new;
  my $request = HTTP::Request->new(GET => $url);

  my $response = $agent->request($request);
  $response->is_success or return (0, 0, 0);

  my $result = $response->content;
  #print "$result\n";

  my @lines = split /\n/, $result;

  my $myoldlat = 0;
  my $myoldlong = 0;
  my $myoldlevel = 0;

  my $myplace;
  my $mystate;
  my $mylatlong;
  my $mynation;

  my $lineidx = 0;
  while($lineidx<=$#lines) {
    my $line = $lines[$lineidx++];
    if($line =~ /<tr>/) {
      $myplace = $mystate = $mylatlong = $mynation = '';
      $line = $lines[$lineidx++]; if($line =~ /<td>([^<]+)<\/td>/) { $myplace = uc($1); }
      $line = $lines[$lineidx++]; if($line =~ /<td>([^<]+)<\/td>/) { $mystate = uc($1); }
      $lineidx++;
      $line = $lines[$lineidx++]; if($line =~ /<td>([^<]+)<\/td>/) { $mylatlong = $1; }
      $lineidx += 4;
      $line = $lines[$lineidx++]; if($line =~ /<td>([^<]+)<\/td>/) { $mynation = uc($1); }
      #print "### $myplace, $mystate, $mynation: $mylatlong\n";

      if($mylatlong =~ /(\d+):(\d+):\d+ ([A-Z]) (\d+):(\d+):\d+ ([A-Z])/) {
	my $mylat = lc("$1:$2$3");
	my $mylong = lc("$4:$5$6");
	$mylat = uc($mylat); $mylong = uc($mylong);
	if($myoldlevel < 1) { $myoldlevel = 1; $myoldlat = $mylat; $myoldlong = $mylong; }
	
	if($myplace eq $city) {
	  if($myoldlevel < 2) { $myoldlevel = 2; $myoldlat = $mylat; $myoldlong = $mylong; }

	  if(($mynation eq 'US') &&
	     ($state eq $mystate ||
	      defined $state2long{$state} && $state2long{$state} eq $mystate)) {
	    return (2, $mylat, $mylong);
	  } elsif($mynation ne 'US' && $country eq $mynation) {
	    return (2, $mylat, $mylong);
	  }
	}
      }
    }
  }
  if($myoldlevel > 0) { return (1, $myoldlat, $myoldlong); }
  else { return (0, 0, 0); }
}

1;
