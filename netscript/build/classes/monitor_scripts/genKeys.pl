my $dir = $ARGV[0];
#`rm -rf $dir`;
`rm -rf $dir/id_dsa*`;
`rm -rf $dir/known_hosts`;
`mkdir -p $dir`;
`chmod 700 $dir`;
`ssh-keygen -t dsa -f /$dir/id_dsa -P ""`;
if ( $? != 0)
{
  print "command failed: $!\n";
  exit 1;
}

`cp $dir/id_dsa.pub $dir/authorized_keys2`;
if ( $? != 0)
{
  print "command failed: $!\n";
  exit 1;
}
`chmod 640 $dir/id_dsa.pub`;
if ( $? != 0)
{
  print "command failed: $!\n";
  exit 1;
}
`chmod 640 $dir/authorized_keys2`;
if ( $? != 0)
{
  print "command failed: $!\n";
  exit 1;
}
exit 0;

