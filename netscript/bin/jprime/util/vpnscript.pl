#!/usr/bin/perl
#
#
# ---------------------------
#         caveats
# ---------------------------
# 
# * Need to install perl Math::BaseCalc package.
#   Use: "sudo perl -MCPAN -e "install Math::BaseCalc"
# 
# * Need to install OpenVPN and OpenSSL.
#   Use: "sudo apt-get install openvpn openssl"
#
#
# ---------------------------
# Command-line of this script
# ---------------------------
#
# % perl vpnscript.pl [OPTIONS] <configfile>
# % perl vpnscript.pl -h | --help
# % perl vpnscript.pl -c | --clean
#
# OPTIONS include:
#   -d <vpndir> | --dir <vpndir>
#      Path to the source-code directory of OpenVPN (default: the
#      current directory)
#   -s <ipprefix> | --ipspace <ipprefix>
#      A block of network ip addresses which OpenVPN uses for setting
#      up connections (default 10.9.0.0)
#   -h | --help
#      Print of usage information
#   -c | --clean
#      Clean up all temporary files and directories the script uses to
#      generate the VPN configuration files
#
# The configfile is the configuration file containing the setup of the
# VPN servers and clients and is generated automatically from the
# model DML file by running dmlenv utility (with -e option).
#
# Format for the configuration file
# ---------------------------------
#
# The configuration file contains a list of statements. One statement
# per line in any order. The statements can be divided functionally
# into three parts: server specification, client specification, and
# network ip prefix.
#
# There can be as many servers as necessary. Each server runs an
# OpenVPN server and an emulation gateway process bridging traffic
# between clients and the simulator itself. Each server is specified
# using:
#
#   server <server_hostname_or_ip> [<openvpn_port> [<ssfgwd_port>]]
#
# The openvpn_port is the well-known port for the VPN server to accept
# client connections. The default is 1194. The ssfgwd_port is the port
# used by the emulation gateway to accept connections from the
# simulator. The default is 36685.

# There can be as many clients as there are hosts defined in the
# virtual network. Each client runs one or multiple OpenVPN clients
# that correspond to the network interfaces in the virtual host (each
# with its own virtual ip address). Each client is specified using:
#
#   client <virtual_ip_0> [<virtual_ip_1> ...]
#
# There must be a statement in the configuration file to specify the
# ip prefix of the virtual network.
#
#   subnet <virtual_ip> <virtual_ip_mask>
#
# For each server or client specified in the configuration file, this
# script will generate a compressed tar file containing all necessary
# configuration files for running an OpenVPN server or client.
#
# This script was first created by Nathan Van Vorst and later modified
# by Jason Liu.

use strict;

my $openvpn_prefix = "10.9.0.0";
#my $openvpn_mask = "255.255.255.0";
my $default_openvpn_port = 1194;
my $default_ssfgwd_port = 36685;
my $openvpn_dir = ".";
my $conf_file;
my %server_hash = ();
my %client_hash = ();
my $subnet_prefix;
my $subnet_mask;

sub usage {
  print << "ENDP";
Usage:
  % perl vpnscript.pl [OPTIONS] <configfile>
  % perl vpnscript.pl -h | --help
  % perl vpnscript.pl -c | --clean

  OPTIONS include:
    -d <vpndir> | --vpndir <vpndir> 
       Path to the source-code directory of OpenVPN (default: the
       current directory)
    -s <ipprefix> | --ipspace <ipprefix>
       A block of network ip addresses which OpenVPN uses for setting
       up connections (default 10.9.0.0)
    -h | --help
       Print of usage information
    -c | --clean
       Clean up all temporary files and directories the script uses to
       generate the VPN configuration files

  The configfile is the configuration file containing the setup of the
  VPN servers and clients and is generated automatically from the
  model DML file by running dmlenv utility (with -e option).
ENDP
  exit(1);
}

sub cleanup {
  `rm -rf easy-rsa`;
  `rm -rf servers`;
  `rm -rf clients`;
}

my $argc = @ARGV;
for(my $i=0; $i<$argc; $i++) {
  if($ARGV[$i] eq '-h' || $ARGV[$i] eq '--help') { usage(); }
  elsif($ARGV[$i] eq '-c' || $ARGV[$i] eq '--clean') { 
    cleanup(); 
    #print "Cleaning up!\n";
    exit(0);
  } elsif($ARGV[$i] eq '-d' || $ARGV[$i] eq '--vpndir') {
    if($i+1 < $argc) { $openvpn_dir = $ARGV[$i+1]; $i++; }
    else { die "WRONG ARGUMENT: -d <vpndir> | --vpndir <vpndir>" }
  } elsif($ARGV[$i] eq '-s' || $ARGV[$i] eq '--ipspace') {
    #if($i+2 < $argc) { $openvpn_prefix = $ARGV[$i+1]; $openvpn_mask = $ARGV[$i+2]; $i+=2; }
    #else { die "WRONG ARGUMENT: -s <ipprefix> <ipmask> | --ipspace <ipprefix> <ipmask>" }
    if($i+1 < $argc) { $openvpn_prefix = $ARGV[$i+1]; $i++; }
    else { die "WRONG ARGUMENT: -s <ipprefix> | --ipspace <ipprefix>" }
  } else { 
    if(!defined $conf_file) { 
      $conf_file = $ARGV[$i];
      if($conf_file =~ /^-/) {
	print "UNRECOGNIZED ARGUMENT: $conf_file\n";
	usage();
      }
    } else { print "WRONG ARGUMENT.\n"; usage(); }
  }
}
if(!defined $conf_file) { usage(); }

open(FILE, $conf_file) || die "Cannot open config file: $conf_file";
while(<FILE>){
  chomp; 
  my $line = $_;
  s/^\s+//; s/\s+$//;
  if($_ eq '') { next; }
  if(/^server/) {
    my @svrlist = split(/\s+/, $_);
    my $cnt = @svrlist;
    if($cnt < 2 || $cnt > 4) { die "WRONG CONFIG FILE FORMAT: server"; }
    my $server_ip = $svrlist[1];
    my $server_vpnport;
    if($cnt > 2) { $server_vpnport = $svrlist[2]; }
    else { $server_vpnport = $default_openvpn_port; }
    my $server_gwdport;
    if($cnt > 3) { $server_gwdport = $svrlist[3]; }
    else { $server_gwdport = $default_ssfgwd_port; }
    $server_hash{$server_ip} = [$server_ip, $server_vpnport, $server_gwdport];
  } elsif(/^client/) {
    my @cltlist = split(/\s+/, $_);
    my $cnt = @cltlist;
    my $start = 1;
    if($cltlist[1] =~ /\[.*\]/) { $start++; }
    if($cnt < $start+1) { die "WRONG CONFIG FORMAT: client"; }
    my $client_ip = $cltlist[$start];
    shift @cltlist; if($start>1) { shift @cltlist; }
    $client_hash{$client_ip} = [@cltlist];
  } elsif(/^subnet/) {
    my @subnet = split(/\s+/, $_);
    my $cnt = @subnet;
    if($cnt != 3) { die "WRONG CONFIG FORMAT: subnet"; }
    $subnet_prefix = $subnet[1];
    $subnet_mask = $subnet[2];
  } elsif(/^#/) { next; }
  else { die "WRONG CONFIG FORMAT: $line"; }
}
close(FILE);

# set the environment for generating keys, etc.
print "@@@@@ STEP 1: cleaning up.\n";
cleanup();

print "@@@@@ STEP 2: copying easy-rsa.\n";
if(-d "$openvpn_dir/easy-rsa") {
  `cp -R $openvpn_dir/easy-rsa/2.0 easy-rsa`;
} else { die "Cannot find OpenVPN. Use -d option."; }

chdir "easy-rsa";
`. ./vars && ./clean-all`;

print "@@@@@ STEP 3: building dh\n";
`. ./vars && ./build-dh`;

print "@@@@@ STEP 4: building certificate authority.\n";
#print "  - generating ca.dft.\n";
#open(FILE, ">ca.dft") || die "Can't create file: ca.dft";
#print(FILE "US\nCO\nGolden\nColorado-School-of-Mines\nPRIME\nssfgwd\nprime\@mines.edu\n");
#close(FILE);
#print "  - creating keys.\n";
#`. ./vars && ./build-ca < ca.dft`;
`. ./vars && ./pkitool --initca`;

for my $server (keys %server_hash) {
  print "\n@@@@@ STEP 5: creating keys for server \"$server\".\n";
  #print "  - generating $server.default.\n";
  #open(FILE, ">$server.default");
  #print(FILE "US\nCO\nGolden\nColorado-School-of-Mines\nPRIME\n$server\nprime\@mines.edu\n\n\ny\ny\n");
  #close FILE;
  #print "  - creating keys.\n";
  #`. ./vars && ./pkitool --server $server < $server.default`;
  `. ./vars && ./pkitool --server $server`;
}

for my $client (keys %client_hash) {
  print "@@@@@ STEP 6: creating keys for client \"$client\".\n";
  for my $ip (@{$client_hash{$client}}) {
    #print "  - generating $ip.default.\n";
    #open(FILE, ">$ip.default");
    #print(FILE "US\nCO\nGolden\nColorado-School-of-Mines\nPRIME\n$ip\nprime\@mines.edu\n\n\ny\ny\n");
    #close FILE;
    #print "  - creating keys.\n";
    #`. ./vars && ./pkitool $ip < $ip.default`;
    print "  - creating keys for interface $ip.\n";
    `. ./vars && ./pkitool $ip`;
  }
}

chdir "..";

#create configs for the servers
print "@@@@@ STEP 7: building server distribution\n";
`mkdir servers`;
for my $server (keys %server_hash) {
  `mkdir servers/$server`;
  `mkdir servers/$server/ccd`;
  `mkdir servers/$server/keys`;

  print "  - creating servers/$server/$server.conf\n";
  open(FILE, ">servers/$server/$server.conf");
  print(FILE "port $server_hash{$server}[1]\n");
  print(FILE "proto udp\n");
  print(FILE "dev tun\n");
  print(FILE "ca keys/ca.crt\n");
  print(FILE "cert keys/$server.crt\n");
  print(FILE "key keys/$server.key\n");
  print(FILE "dh keys/dh1024.pem\n");
  #print(FILE "server $openvpn_prefix $openvpn_mask\n");
  print(FILE "server $openvpn_prefix $subnet_mask\n");
  print(FILE "push \"route $subnet_prefix $subnet_mask\"\n");
  print(FILE "route $subnet_prefix $subnet_mask\n");
  print(FILE "status openvpn-status_$server.log\n");
  print(FILE "client-config-dir ccd\n");
  print(FILE "keepalive 10 120\n");
  print(FILE "comp-lzo\n");
  print(FILE "persist-key\n");
  print(FILE "persist-tun\n");
  print(FILE "ccd-exclusive\n");
  print(FILE "verb 3\n");
  close FILE;

  print "  - creating servers/$server/runserver.sh\n";
  open(FILE, ">servers/$server/runserver.sh");
  print(FILE "SSFGWD_PATHNAME=\${PRIME_HOME?}/ssfgwd SSFGWD_ARGLIST=\"-p $server_hash{$server}[2]\" \${PRIME_HOME?}/openvpn --daemon --cd `pwd` --log openvpn-$server.log --plugin \${PRIME_HOME?}/openvpn-client-conn.so 192.168.1.1 --config $server.conf\n");
  close FILE;

  for my $client (keys %client_hash) {
    my $idx = 1;
    for my $ip (@{$client_hash{$client}}) {
      my @prefix_octets = split(/\./, $openvpn_prefix);
      my $last_octet = pop @prefix_octets;
      if($idx > 256) { # assume less than 65536
	my $next_octet = pop @prefix_octets;
	$next_octet += ($idx>>8);
	push @prefix_octets, $next_octet;
      }
      $last_octet += ($idx&0xff);
      push @prefix_octets, $last_octet;
      my $openvpn_prefix_client = join '.', @prefix_octets;
      $idx++;

      print "  - building servers/$server/ccd/$ip\n";
      open(FILE, ">servers/$server/ccd/$ip");
      print(FILE "ifconfig-push $ip $openvpn_prefix_client\n");
      close FILE;
      `cp easy-rsa/keys/$client.* servers/$server/keys/`;
    }
  }
  `cp easy-rsa/keys/ca.crt servers/$server/keys/`;
  `cp easy-rsa/keys/dh1024.pem servers/$server/keys/`;
  `cp easy-rsa/keys/$server.* servers/$server/keys/`;
}

#create configs for the clients
print "@@@@@ STEP 8: building client distribution\n";
`mkdir clients`;
for my $client (keys %client_hash) {
  `mkdir clients/$client`;
  print "  - creating clients/$client/runclient.sh\n";
  open(RCFILE, ">clients/$client/runclient.sh");
  for my $ip (@{$client_hash{$client}}) {
    `mkdir -p clients/$ip`;
    `mkdir clients/$ip/keys`;
    `cp easy-rsa/keys/$ip.* clients/$ip/keys/`;
    `cp easy-rsa/keys/ca.crt clients/$ip/keys/`;
    print "  - creating clients/$ip/$ip.conf\n";
    open(FILE, ">clients/$ip/$ip.conf");
    print(FILE "client\n");
    for my $server (keys %server_hash) {
      print(FILE "remote $server $server_hash{$server}[1]\n");
    }
    print(FILE "remote-random\n");
    print(FILE "dev tun\n");
    print(FILE "proto udp\n");
    print(FILE "resolv-retry infinite\n");
    print(FILE "nobind\n");
    print(FILE "persist-key\n");
    print(FILE "persist-tun\n");
    print(FILE "ca keys/ca.crt\n");
    print(FILE "cert keys/$ip.crt\n");
    print(FILE "key keys/$ip.key\n");
    print(FILE "comp-lzo\n");
    print(FILE "verb 3\n");
    close FILE;
#    print(RCFILE "(cd $ip; \${PRIME_HOME?}/ssfnet/openvpn --daemon --cd `pwd` --log ../openvpn-$ip.log --config $ip.conf)\n");
    print(RCFILE "(cd $ip; openvpn --daemon --cd `pwd` --log ../openvpn-$ip.log --config $ip.conf)\n");
    print(RCFILE "sleep 1\n");
  }
  close RCFILE;
}

# package up server components
for my $server (keys %server_hash) {
  `mkdir openvpn_server_$server`;
  `cp -R servers/$server/* openvpn_server_$server`;
  `tar -czvf $server.tgz openvpn_server_$server`;
  `rm -rf openvpn_server_$server`;
}

# package up client components
for my $client (keys %client_hash) {
  `mkdir openvpn_client_$client`;
  for my $ip (@{$client_hash{$client}}) {
    `mkdir openvpn_client_$client/$ip`;
    `cp -R clients/$ip/* openvpn_client_$client/$ip/`;
  }
  `mv openvpn_client_$client/$client/runclient.sh openvpn_client_$client`;
  `tar -czvf $client.tgz openvpn_client_$client`;
  `rm -rf openvpn_client_$client`;
}
