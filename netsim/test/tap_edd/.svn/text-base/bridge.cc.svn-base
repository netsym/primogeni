#include "bridge.h"

#define DEBUG 0


int tun_open_old(char *dev)
{
  char tunname[14];
  int i, fd;
  if( *dev ) {
    sprintf(tunname, "/dev/%s", dev);
    return open(tunname, O_RDWR);
  }
  for(i=0; i < 255; i++) {
    sprintf(tunname, "/dev/tun%d", i);
    /* Open device */
    if( (fd=open(tunname, O_RDWR)) > 0 ){
      sprintf(dev, "tun%d", i);
      return fd;
    }
  }
  return -1;
}


int tun_open(char *dev)
{
  struct ifreq ifr;
  int fd;
  if ((fd = open("/dev/net/tun", O_RDWR)) < 0) {
    return tun_open_old(dev);
  }
  
  memset(&ifr, 0, sizeof(ifr));
  ifr.ifr_flags = IFF_TAP | IFF_NO_PI;
  /*ifr.ifr_flags = IFF_TUN | IFF_NO_PI;*/
  if (*dev) {
    strncpy(ifr.ifr_name, dev, IFNAMSIZ);
  }
  
  if (ioctl(fd, TUNSETIFF, (void *) &ifr) < 0) {
    if (errno == EBADFD) {
      /* Try old ioctl */
      if (ioctl(fd, OTUNSETIFF, (void *) &ifr) < 0) 
	goto failed;
    }
    else {
      goto failed;
    }
  }
  
  strcpy(dev, ifr.ifr_name);
  //printf("Created %s\n",dev);
  return fd;
  
 failed:
  close(fd);
  std::cerr<<"ACK!"<<std::endl;
  return -1;
}

void tokenize(const std::string& str,
	      std::vector<std::string>& tokens,
	      const std::string& delimiters)
{
  // Skip delimiters at beginning.
  std::string::size_type lastPos = str.find_first_not_of(delimiters, 0);
  // Find first "non-delimiter".
  std::string::size_type pos     = str.find_first_of(delimiters, lastPos);
  
  while (std::string::npos != pos || std::string::npos != lastPos)
    {
      // Found a token, add it to the vector.
      tokens.push_back(str.substr(lastPos, pos - lastPos));
      // Skip delimiters.  Note the "not_of"
      lastPos = str.find_first_not_of(delimiters, pos);
      // Find next "non-delimiter"
      pos = str.find_first_of(delimiters, lastPos);
    }
}

void ip_fromString(const char* str, uint8_t *ip) {
  std::vector<std::string> toks;
  tokenize(std::string(str), toks, ".");
  uint8_t i=0; 
  for(i=0;i<toks.size() && i<4;i++){
    ip[i]=(uint8_t)atoi(toks[i].c_str());
    //printf(" parssing %s to %u[%x]\n",toks[i].c_str(),ip[i],ip[i]);
  }
}

void mac_fromString(const char* str, uint8_t* mac) {
  std::vector<std::string> toks;
  tokenize(std::string(str), toks, ":");
  uint8_t i=0;
  for(i=0;i<toks.size()&&i<6;i++){
    mac[i]=(uint8_t)strtol(toks[i].c_str(),NULL,16);
    //printf(" parssing %s to %u[%x]\n",toks[i].c_str(),mac->mac[i],mac->mac[i]);
  }
}

char macdispbuf[50];

char* mac2txt(uint8_t mac[]) {
  sprintf(macdispbuf, "%02x:%02x:%02x:%02x:%02x:%02x", mac[0], mac[1],
	  mac[2], mac[3], mac[4], mac[5]);
  return macdispbuf;
}

void dump_buffer(char* b, int len)
{
  int idx=0;
  for(idx=0;idx<9;idx++){
    printf("|0%i",idx);
  }
  for(idx=10;idx<16;idx++){
    printf("|%i",idx);
  }
  printf("|(%p,%i)\n",b,len);
  for(idx=0;idx<45;idx++){
    printf("-");
  }
  printf("\n");
  for(idx=0;idx<len;idx++){	
    if (idx % 15 == 0 && idx>0) {
      printf("|\n");
    }
    printf("|%02X", (unsigned char)b[idx]);
  }
  printf("\n");
}

VETH::VETH(uint32_t ip_, uint8_t *mac_, char* veth_, TAP* tap_): 
  tap(tap_),sock(0),veth(veth_)
{
  bcopy(&ip_,ip,4);
  bcopy(mac_,mac,6);

  struct ifreq ifr;  
  int ifindex = 0;
  sock = socket(AF_PACKET, SOCK_RAW, htons(ETH_P_ALL));
  if (sock == -1) {
    perror("socket():");
    exit(1);
  }
  if(DEBUG)std::cout<<"Successfully opened socket: "<<sock<<std::endl;
  /*retrieve ethernet interface index*/
  strncpy(ifr.ifr_name, veth.c_str(), IFNAMSIZ);
  if (ioctl(sock, SIOCGIFINDEX, &ifr) == -1) {
    perror("SIOCGIFINDEX");
    exit(1);
  }
  ifindex = ifr.ifr_ifindex;
  if(DEBUG)std::cout<<"Successfully got interface index: "<<ifindex<<" of "<<veth<<std::endl;

  /*retrieve corresponding MAC*/
  if (ioctl(sock, SIOCGIFHWADDR, &ifr) == -1) {
    perror("SIOCGIFINDEX");
    exit(1);
  }
  
  /*prepare sockaddr_ll*/
  socket_address.sll_family   = PF_PACKET;
  socket_address.sll_protocol = htons(ETH_P_IP);
  socket_address.sll_ifindex  = ifindex;
  socket_address.sll_hatype   = ARPHRD_ETHER;
  socket_address.sll_pkttype  = PACKET_OTHERHOST;
  socket_address.sll_halen    = ETH_ALEN;
  socket_address.sll_addr[6]  = 0x00; 
  socket_address.sll_addr[7]  = 0x00; 

  /** set sock to be non blocking **/
  int flags = fcntl(sock,F_GETFL,0);
  if(flags == -1) {
    std::cerr << "ACK! flags=-1"<<std::endl;
    exit(1);
  }
  fcntl(sock, F_SETFL, flags | O_NONBLOCK | SHUT_RD);

}

void VETH::send(EthernetHeader* eth, int len) {
  /*prepare sockaddr_ll*/
  socket_address.sll_addr[0]  = eth->tgt_hw_addr[0];
  socket_address.sll_addr[1]  = eth->tgt_hw_addr[1];
  socket_address.sll_addr[2]  = eth->tgt_hw_addr[2];
  socket_address.sll_addr[3]  = eth->tgt_hw_addr[3];
  socket_address.sll_addr[4]  = eth->tgt_hw_addr[4];
  socket_address.sll_addr[5]  = eth->tgt_hw_addr[5];
  
  /*send answer*/
  int sent = sendto(sock, (char*)eth, len, 0, (struct sockaddr*)&socket_address, sizeof(socket_address));
  if (sent == -1) {
    perror("sendto():");
    exit(1);
  }
}


std::ostream& operator<< (std::ostream& o, VETH const& veth) {
  o << "{VETH"
    <<" "<<veth.veth
    <<", "<<(uint16_t)veth.ip[0]
    <<"."<<(uint16_t)veth.ip[1]
    <<"."<<(uint16_t)veth.ip[2]
    <<"."<<(uint16_t)veth.ip[3]<<", ";
  o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)veth.mac[0];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)veth.mac[1];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)veth.mac[2];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)veth.mac[3];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)veth.mac[4];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)veth.mac[5];
  return o <<"}"<<std::dec;
}

std::string EthernetHeader::getTypeString(uint16_t t) {
  std::string rv;
  switch(t) {
  case ETH_P_8021Q:
    rv="vlan";
    break;
  case ETH_P_ARP:
    rv="arp";
    break;
  case ETH_P_IP:
    rv="ipv4";
    break;
  case ETH_P_IPV6:
    rv="ipv6";
    break;
  default:
    rv="UNKNOWN";
    break;
  }
  return rv;
}

std::ostream& operator<< (std::ostream& o, EthernetHeader const& hdr) {
  o <<"{Ethernet"
    <<"\n\tsrc_hw_addr=";
  o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[0];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[1];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[2];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[3];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[4];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_hw_addr[5];
  o <<"\n\ttgt_mac=";
  o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[0];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[1];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[2];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[3];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[4];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_hw_addr[5];
  return o <<"\n\tframe_type="<<hdr.getFrameType()
	   <<"["<<EthernetHeader::getTypeString(hdr.getFrameType())<<"]"<<"}"<<std::dec;
}

std::ostream& operator<< (std::ostream& o, VLANHeader const& hdr) {
  return o <<"{VLAN"
    <<"\n\tframe_type="<<hdr.getFrameType()<<"["<<EthernetHeader::getTypeString(hdr.getFrameType())<<"]"
    <<"\n\tvid="<<hdr.getVID()
    <<"}"<<std::dec;
}

std::string ARPHeader::getOpcodeString() const {
  std::string rv;
  switch (getOpcode()){
  case ARP_REQUEST:
    rv="ARP_REQUEST";
    break;
  case ARP_REPLY:
    rv="ARP_REPLY";
    break;
  case RARP_REQUEST:
    rv="RARP_REQUEST";
    break;
  case RARP_REPLY:
    rv="RARP_REPLY";
    break;
  default:
    rv="BAD OPCODE";
    break;
  }
  return rv;
}

std::string ARPHeader::getHString() const {
  std::string rv;
  switch(getHType()) {
  case ARP_TYPE_ETH:
    rv="ETH";
    break;
  default:
    rv="UNKNWON";
  }
  return rv;
}

std::string ARPHeader::getPString() const {
  std::string rv;
  switch(getHType()) {
  case ARP_PROTO_IP:
    rv="IP";
    break;
  default:
    rv="UNKNWON";
  }
  return rv;
}

std::ostream& operator<< (std::ostream& o, ARPHeader const& hdr) {
  o <<"{ARP"
    <<"\n\thtype=0x"<<std::hex<<(uint16_t)hdr.getHType()<<"["<<hdr.getHString()<<std::dec<<"], size="<<(uint16_t)hdr.getHSize()
    <<"\n\tproto=0x"<<std::hex<<(uint16_t)hdr.getProto()<<"["<<hdr.getPString()<<std::dec<<"], size="<<(uint16_t)hdr.getPSize()
    <<"\n\topcode=0x"<<std::hex<<(uint16_t)hdr.getOpcode()<<"["<<hdr.getOpcodeString()<<"]"
    <<"\n\tsrc_mac=";
  o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[0];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[1];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[2];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[3];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[4];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.src_mac[5]<<std::dec;
  o <<"\n\tsrc_ip="
    <<(uint16_t)hdr.src_ip[0]
    <<"."<<(uint16_t)hdr.src_ip[1]
    <<"."<<(uint16_t)hdr.src_ip[2]
    <<"."<<(uint16_t)hdr.src_ip[3]
    <<"\n\ttgt_mac=";
  o << std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[0];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[1];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[2];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[3];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[4];
  o <<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)hdr.tgt_mac[5]<<std::dec;
  return o <<"\n\ttgt_ip="
	   <<(uint16_t)hdr.tgt_ip[0]
	   <<"."<<(uint16_t)hdr.tgt_ip[1]
	   <<"."<<(uint16_t)hdr.tgt_ip[2]
	   <<"."<<(uint16_t)hdr.tgt_ip[3]
	   <<"}"<<std::dec;
}

std::string IPv4Header::getProtoString() const {
  std::string rv;
  switch(getProto()) {
  case IPPROTO_ICMP:
    rv="icmp";
    break;
  case IPPROTO_TCP:
    rv="tcp";
    break;
  case IPPROTO_UDP:
    rv="udp";
    break;
  case IPPROTO_IPIP: /*IP-IP tunneling*/
    rv="ip";
    break;
  default:
    rv="UNKNOWN";
  }
  return rv;
}


std::ostream& operator<< (std::ostream& o, IPv4Header const& hdr) {
  uint8_t* src=(uint8_t*)&(hdr.ip_src);
  uint8_t* dst=(uint8_t*)&(hdr.ip_dst);
  return o <<"{IPv4"
	   <<"\n\tsrc="
	   <<(uint16_t)src[0]
	   <<"."<<(uint16_t)src[1]
	   <<"."<<(uint16_t)src[2]
	   <<"."<<(uint16_t)src[3]
	   <<"\n\tdst="
	   <<(uint16_t)dst[0]
	   <<"."<<(uint16_t)dst[1]
	   <<"."<<(uint16_t)dst[2]
	   <<"."<<(uint16_t)dst[3]
	   <<"\n\thdr_len="<<(uint16_t)hdr.getHdrLen()
	   <<"\n\ttos="<<(uint16_t)hdr.getTOS()
	   <<"\n\ttoal_len="<<(uint16_t)hdr.getLen()
	   <<"\n\tident="<<(uint16_t)hdr.getIdent()
	   <<"\n\toffset="<<(uint16_t)hdr.getOffset()
	   <<"\n\tttl="<<(uint16_t)hdr.getTTL()
	   <<"\n\tproto="<<(uint16_t)hdr.getProto()<<"["<<hdr.getProtoString()<<"]"
	   <<"\n\tchecksum="<<hdr.getCheckSum()
	   <<"}\n";

}

std::ostream& operator<< (std::ostream& o, IPv6Header const& hdr) {
  return o <<"{IPv6 payload_length="<<hdr.getPayloadLength()<<"}";
}


TAP::TAP(std::string name_, Bridge* bridge_): 
  name(name_),bridge(bridge_), head(0),tail(0),pos(0),state(ITS_GOOD) { 
  buf =(char*)malloc(BUFFSIZE);
  sprintf(buf,"%s",name.c_str());
  arppacketlen = sizeof(EthernetHeader)+sizeof(ARPHeader);
  arppacket = (char*)malloc(arppacketlen+1);
  //if(DEBUG)std::cout <<"Trying to open '"<<name<<"'\n"<<std::endl;
  fd = tun_open(buf);
  ioctl(fd, TUNSETNOCSUM, 1); 
  if( -1 == fd) {
    std::cerr <<"Unable to create TAP '"<<name<<"'!"<<std::endl;
    perror(0);
    exit(100);
  }
}

void TAP::myread(uint32_t len, bool passive) {
  //printf("[0]myread(fd=%s, buf=%p, pos=%u, tail=%u, len=%u,passive=%i)\n",name.c_str(),buf,pos,tail,len,passive);
  if(passive) {
    if(pos+len>tail) {
      //printf("[1]myread(fd=%s, buf=%p, pos=%u, tail=%u, len=%u,passive=%i)\n",name.c_str(),buf,pos,tail,len,passive);
      state=NO_MORE_BYTES;
      return;
    }
    //printf("[2]myread(fd=%s, buf=%p, pos=%u, tail=%u, len=%u,passive=%i)\n",name.c_str(),buf,pos,tail,len,passive);
    state=ITS_GOOD;
    return;
  }

  if(pos+len>tail) {
    len=pos+len-tail; //only ready the amount we have not yet read
    // printf("READING %u bytes\n",len);
    int rv = read(fd,(buf+tail),len);
    if(rv == 0) {
      //printf("[3]myread(fd=%s, buf=%p, pos=%u, tail=%u, len=%u,passive=%i)\n",name.c_str(),buf,pos,tail,len,passive);
      //dump_buffer(buf,tail);
      state = NO_MORE_BYTES;
      return;
    }
    else if(rv>0) {
      len-=rv;
      tail+=rv;
    }
    else {
      printf("error reading from tap!");
      perror(0);
      state= READ_ERROR;
      return;
    }
  }
  //printf("[4]myread(fd=%s, buf=%p, pos=%u, tail=%u, len=%u,passive=%i)\n",name.c_str(),buf,pos,tail,len,passive);
  //dump_buffer(buf,tail);
  state = ITS_GOOD;
}

void TAP::mywrite(char* buf, int len) {
  int rv = write(fd,buf,len);
  if( len != rv) {
    //write should never fail.....
    std::cerr << "ACK! write failed! Tried to write "<<len<<" but only wrote "<<rv<<std::endl;
    exit(100);
  }
}

void TAP::processPkt(fd_set *fds) {
  if( fds && !FD_ISSET(fd, fds)  && tail == 0) {
    //there was nothing to read and our buffer is empty
    state = ITS_GOOD;
    return;
  }
  else if(fds && FD_ISSET(fd, fds) ) {
    //read something...
    myread(BUFFSIZE-tail,false);
    if(state == READ_ERROR || state == BAD_PROTO) {
      return;
    }
  }
  uint16_t frame_type;
  if(DEBUG)std::cout <<" Reading Ethernet[pos="<<pos<<",size="<<sizeof(EthernetHeader)<<",tail="<<tail<<"]\n";
  EthernetHeader* eth=(EthernetHeader*)(buf+pos);
  myread(sizeof(EthernetHeader),true);
  if(state !=  ITS_GOOD) {
    return;
  }
  pos+=sizeof(EthernetHeader);
  VLANHeader* vlan=(VLANHeader*)(buf+pos);
  if(eth->isVLAN()) {
    if(DEBUG)std::cout <<" Reading a 8021Q header [pos="<<pos<<",size="<<sizeof(VLANHeader)<<",tail="<<tail<<"]\n";
    myread(sizeof(VLANHeader),true);
    if(state !=  ITS_GOOD) {
      return;
    }
    pos+=sizeof(VLANHeader);
    frame_type=vlan->getFrameType();
  }
  else {
    vlan=NULL;
    frame_type=eth->getFrameType();
  }
  
  if(EthernetHeader::isARP(frame_type)) {
    if(DEBUG)std::cout <<" Reading ARP[pos="<<pos<<",size="<<sizeof(ARPHeader)<<",tail="<<tail<<"]\n";
    ARPHeader* arp=(ARPHeader*)(buf+pos);
    myread(sizeof(ARPHeader),true);
    if(state !=  ITS_GOOD) {
      pos=head;
      return;
    }
    pos+=sizeof(ARPHeader);
    if(DEBUG)std::cout << *arp<<std::endl;
    if(arp->isArpRequest()) {
      createArpResponse(eth,arp);
    }
  }
  else if(EthernetHeader::isIPv4(frame_type)) {
    if(DEBUG)std::cout <<" Reading IPv4[pos="<<pos<<",size="<<sizeof(IPv4Header)<<",tail="<<tail<<"]\n";
    IPv4Header* ip=(IPv4Header*)(buf+pos);
    myread(sizeof(IPv4Header),true);
    if(state !=  ITS_GOOD) {
      pos=head;
      return;
    }
    pos+=sizeof(IPv4Header);
   
    if(DEBUG)std::cout << *ip <<std::endl;
    uint32_t len = ip->getLen()-ip->getHdrLen(); 
    //uint32_t len = ip->getLen()-sizeof(IPv4Header);
    if(DEBUG)std::cout <<" Reading IPv4 Payload[pos="<<pos<<",size="<<len<<",tail="<<tail<<"]\n";
    myread(len,true);
    if(state !=  ITS_GOOD) {
      pos=head;
      return;
    }
    pos+=len;
    routePacket(eth,ip,(uint32_t)(pos-head));
  }
  else if(EthernetHeader::isIPv6(frame_type)) {
    if(DEBUG)std::cout <<" Reading IPv6[pos="<<pos<<",size="<<sizeof(IPv6Header)<<",tail="<<tail<<"]\n";
    IPv6Header* ip=(IPv6Header*)(buf+pos);
    myread(sizeof(IPv6Header),true);
    if(state !=  ITS_GOOD) {
      pos=head;
      return;
    }
    pos+=sizeof(IPv6Header);
    if(DEBUG)std::cout << *ip <<std::endl;
    if(DEBUG)std::cout <<" Reading IPv^ Payload[pos="<<pos<<",size="<<ip->getPayloadLength()<<",tail="<<tail<<"]\n";
    myread(ip->getPayloadLength(),true);
    if(state !=  ITS_GOOD) {
      pos=head;
      return;
    }
    pos+=ip->getPayloadLength();
  }
  else {
    std::cerr << "Unknown protocol following Ethernet header!"<<std::endl;
    std::cerr << *eth <<std::endl;
    exit(100);
  }

  //if(DEBUG)std::cout << "[1]FINISHED had="<<head<<", tail="<<tail<<", pos="<<pos<<"\n";

  //XXX could be more effiecint
  head=pos;
  if(tail==head) {
    tail=0;
    head=0;
    pos=0;
  }
  else {
    const char* src = buf+head;
    int len = tail-head;
    if(DEBUG)std::cout << "Moving ["<<head<<","<<tail<<"] to << [0,"<<len<<"]\n";
    memcpy(buf,src,len);
    head=0;
    pos=0;
    tail=len;
    bzero(buf+len,BUFFSIZE-len);
  }
  //if(DEBUG)std::cout << "[2]FINISHED head="<<head<<", tail="<<tail<<", pos="<<pos<<"\n";
  state = ITS_GOOD;
}

void TAP::routePacket(EthernetHeader* eth, IPv4Header* ip, uint32_t len) {
  //find which its sending to... 
  VETH* veth = bridge->find(ntohl(ip->getDst()));
  if(veth==NULL) {
    std::cerr << "Targeted unknown Host!\n"<<*eth<<"\n"<<*ip<<std::endl;
    return;
  }  
  for(int i=0;i<ETH_HW_ADDR_LEN; i++) {
    eth->src_hw_addr[i]=eth->tgt_hw_addr[i];
    eth->tgt_hw_addr[i]=veth->mac[i];
  }
  uint8_t* src=(uint8_t*)&(ip->ip_dst);
  if(DEBUG)std::cout << "\tRouted "
	    <<(uint16_t)src[0]
	    <<"."<<(uint16_t)src[1]
	    <<"."<<(uint16_t)src[2]
	    <<"."<<(uint16_t)src[3]
	    <<" to " << *veth <<"\n";
  if(DEBUG)std::cout <<"\tSending packet, size="<<pos<<"\n";
  //veth->tap->mywrite((char*)eth,len);
  veth->send(eth,len);
}


void TAP::createArpRequest(VETH* veth) {
  int idx;
  
  EthernetHeader * eth = (EthernetHeader*)arppacket;
  ARPHeader *arp = (ARPHeader*)(arppacket+sizeof(EthernetHeader));

  for(idx=0;idx<8;idx++) {
    eth->tgt_hw_addr[idx]=veth->mac[idx];
    arp->tgt_mac[idx]=veth->mac[idx];
    eth->src_hw_addr[idx]=3;
    arp->src_mac[idx]=3;
  }
  for(idx=0;idx<4;idx++) {
    arp->src_ip[idx]=0;
    arp->tgt_ip[idx]=veth->ip[idx];
  }
  eth->frame_type=htons(ETH_P_ARP);
  arp->htype = htons(ARP_TYPE_ETH); 
  arp->proto = htons(ARP_PROTO_IP);
  arp->hsize = 6;
  arp->psize = 4;
  arp->opcode= htons(ARP_REQUEST);

  //dump_buffer((char*)&(r.eth), sizeof(myethhdr_t));
  //dump_buffer((char*)&(r.arp), sizeof(myarphdr_t));
  if(DEBUG)std::cout<< "SENDING ARP REQUEST"<<arppacketlen<<"\n";
  mywrite(arppacket,arppacketlen);

}


void TAP::createArpResponse(EthernetHeader* ethr, ARPHeader* request) { 
  int idx;
  
  EthernetHeader * eth = (EthernetHeader*)arppacket;
  ARPHeader *arp = (ARPHeader*)(arppacket+sizeof(EthernetHeader));

  for(idx=0;idx<8;idx++) {
    eth->tgt_hw_addr[idx]=request->src_mac[idx];
    eth->src_hw_addr[idx]=3;
    arp->tgt_mac[idx]=3;
    arp->src_mac[idx]=3;
  }
  for(idx=0;idx<4;idx++) {
    arp->src_ip[idx]=request->tgt_ip[idx];
    arp->tgt_ip[idx]=request->src_ip[idx];
  }
  eth->frame_type=ethr->frame_type;
  arp->htype = request->htype;
  arp->proto = request->proto;
  arp->hsize = request->hsize;  
  arp->psize = request->psize;
  arp->opcode= htons(ARP_REPLY);

  //dump_buffer((char*)&(r.eth), sizeof(myethhdr_t));
  //dump_buffer((char*)&(r.arp), sizeof(myarphdr_t));
  //if(DEBUG)
  std::cout<< "SENDING ARP REPLY"<<arppacketlen<<"\n";
  mywrite(arppacket,arppacketlen);
}


Bridge::Bridge():maxfd(0) {
  
}

VETH* Bridge::find(uint32_t ip) {
  std::map<uint32_t, VETH*>::iterator it = mac2ip.find(ip);
  if(it==mac2ip.end()) return NULL;
  return it->second;
}


bool Bridge::parseBridge(int* i, int argc, char** argv) { 
  uint8_t mac[ETH_HW_ADDR_LEN];
  uint32_t ip;
  char *macstr, *ipstr, *vethstr, *tapstr;
  TAP* tap=0;
  VETH* veth=0;
  if((*i+4)>=argc)return false;

  if(strcmp(argv[*i],"BRIDGE"))return false;
  *i=*i+1;

  tapstr=argv[*i];
  *i=*i+1;

  //  if(DEBUG)
  std::cout<<"Parsing bridge config, tap="<<tapstr<<std::endl;

  tap = new TAP(std::string(tapstr),this);
  taps.push_back(tap);

  while(*i<argc-2) {
    if(!strcmp(argv[*i],"BRIDGE"))
      break;
    vethstr=argv[*i];
    *i=*i+1;
    macstr=argv[*i];
    *i=*i+1;
    ipstr=argv[*i];
    *i=*i+1;
    mac_fromString(macstr,mac);
    ip_fromString(ipstr,(uint8_t*)(&ip));
    //ip=ntohl(ip);
    veth=new VETH(ip,mac,vethstr,tap);

    mac2ip.insert(std::pair<uint32_t, VETH*>(ip,veth));
    //if(DEBUG)
    std::cout<<" Parsed ["<<macstr<<","<<ipstr<<", "<<vethstr<<"] --> "<<*veth<<std::endl;
  }
  return true;
}


bool Bridge::parseArgs(int argc, char** argv) {
  if(argc<3)return false;
  int i=1;
  while(i<argc) {
    if(!parseBridge(&i,argc,argv))
      break;
  }

  if(i!=argc) {
    std::cerr<<"Error parsing args. The following were left over:\n\t";
    for(;i<argc;i++) {
      std::cerr<<argv[i];
    }
    std::cerr<<std::endl;
    return false;
  }

  for(std::vector<TAP*>::iterator it=taps.begin();it!=taps.end();it++) {
    if((*it)->getFD()>maxfd)
      maxfd=(*it)->getFD();
  }
  maxfd++;
  return true;
}


void Bridge::run() {
  fd_set fds;
  std::vector<TAP*>::iterator it;
  std::map<uint32_t, VETH*>::iterator m;
  
  for(m=mac2ip.begin();m!=mac2ip.end();m++) {
    m->second->tap->createArpRequest(m->second);
  }

  while(1){
    FD_ZERO(&fds);
    for(it=taps.begin();it!=taps.end();it++) {
      FD_SET((*it)->getFD(),&fds);
    }
    select(maxfd, &fds, NULL, NULL, NULL);
    for(it=taps.begin();it!=taps.end();it++) {
      do {
	(*it)->processPkt(&fds);
	switch((*it)->getState()) {
	case TAP::NO_MORE_BYTES:
	  std::cout << " **** INCOMPLETE PACKET! ****"<<std::endl;
	  break;
	case TAP::ITS_GOOD:
	  //its good
	  break;
	case TAP::READ_ERROR:
	  std::cerr << "READ ERROR! ABORTING"<<std::endl;
	  exit(100);
	  break;
	case TAP::BAD_PROTO:
	  std::cerr << "BAD PROTO! ABORTING"<<std::endl;
	  exit(100);
	}	
      } while((*it)->morePkts());
    }
  }
}
