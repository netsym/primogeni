#include "bridge.h"

int main(int argc, char *argv[])
{
  std::cout<<"STARTING"<<std::endl;
  Bridge b;
  if(!b.parseArgs(argc,argv)) {
    std::cout <<"Usage: bridge {bridge spec} {bridge spec} [<bridge spec>]\n";
    std::cout <<"  <bridge spec>: BRIDGE <tap name> <veth name> [<mac> <ip>]+}\n";
    std::cout <<"Example:  ./bridge BRIDGE tap1002 veth1002.0 66:12:03:04:05:01 192.1.0.1 ";
    std::cout <<"BRIDGE tap1003 veth1003.0 66:12:03:04:05:02 192.1.0.2 \n";
    exit(1);
  }
  b.run();
}

