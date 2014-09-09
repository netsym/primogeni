#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>

#include <sys/socket.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/ioctl.h>

#include <linux/if_tun.h>
#include <linux/if_packet.h>
#include <net/if.h>  
#include <net/ethernet.h>  
#include <netinet/if_ether.h>
#include <netinet/ip.h>
#include <netinet/in.h>
#include <netinet/ip_icmp.h>

#include <map>
#include <vector>
#include <string>
#include <iostream>
#include <iomanip>

/* pre 2.4.6 compatibility */
#define OTUNSETNOCSUM  (('T'<< 8) | 200) 
#define OTUNSETDEBUG	(('T'<< 8) | 201) 
#define OTUNSETIFF	  (('T'<< 8) | 202) 
#define OTUNSETPERSIST (('T'<< 8) | 203) 
#define OTUNSETOWNER	(('T'<< 8) | 204)

#define BUFFSIZE 1600

/* ARP protocol constants */
#define ARP_PROTO_IP      0x0800
#define ARP_TYPE_ETH      0x0001
/* ARP opcode constants */
#define ARP_REQUEST   0x0001
#define ARP_REPLY     0x0002
#define RARP_REQUEST  0x0003
#define RARP_REPLY    0x0004

#define ETH_HW_ADDR_LEN 6

int tun_open_old(char *dev);
int tun_open(char *dev);
void tokenize(const std::string& str, 
	      std::vector<std::string>& tokens,
	      const std::string& delimiters);
void ip_fromString(const char* str, uint8_t *ip);
void mac_fromString(const char* str, uint8_t* mac);
char* mac2txt(uint8_t mac[]);
void dump_buffer(char* b, int len);

class TAP;
class Bridge;
class VETH;


class EthernetHeader {
  friend class TAP;
  friend class VETH;
  friend class Bridge;
 private:
  uint8_t tgt_hw_addr[ETH_HW_ADDR_LEN];
  uint8_t src_hw_addr[ETH_HW_ADDR_LEN];
  uint16_t frame_type;

 public: 
  uint8_t* getSrc() { return  src_hw_addr; }
  uint8_t* getDst() { return  tgt_hw_addr; }
  uint16_t getFrameType() const { return ntohs(frame_type); }
  void setFrameType(uint16_t ft) {frame_type = htons(ft);}
  void setSrc(uint8_t addr[]) { 
    for(int i=0;i<ETH_HW_ADDR_LEN;i++)
      src_hw_addr[i]=addr[i];
  }
  void setDst(uint8_t addr[]) { 
    for(int i=0;i<ETH_HW_ADDR_LEN;i++)
      tgt_hw_addr[i]=addr[i];
  }

  bool isVLAN() { return ETH_P_8021Q == ntohs(frame_type);}
  bool hasARP() { return ETH_P_ARP ==  ntohs(frame_type);}
  bool hasIPv4() { return ETH_P_IP ==  ntohs(frame_type);}
  bool hasIPv6() { return ETH_P_IPV6 ==  ntohs(frame_type);}

  static bool isVLAN(uint16_t frame_type) { return ETH_P_8021Q == frame_type; }
  static bool isARP(uint16_t frame_type) { return ETH_P_ARP ==  frame_type; }
  static bool isIPv4(uint16_t frame_type) { return ETH_P_IP ==  frame_type; }
  static bool isIPv6(uint16_t frame_type) { return ETH_P_IPV6 ==  frame_type; } 

  static std::string getTypeString(uint16_t t);

  friend std::ostream& operator<< (std::ostream& o, EthernetHeader const& hdr);
};

class VLANHeader {
 private:
  uint16_t vid;//lower 12 bits
  uint16_t frame_type;
 public:
  uint16_t getFrameType() const { return ntohs(frame_type); }
  uint16_t getVID() const { return ntohs(vid)*0x0FFF; }
  void setFrameType(uint16_t ft) { frame_type = htons(ft); }
  bool isVLAN() { return ETH_P_8021Q == ntohs(frame_type); }
  bool hasARP() { return ETH_P_ARP ==  ntohs(frame_type); }
  bool hasIPv4() { return ETH_P_IP ==  ntohs(frame_type); }
  bool hasIPv6() { return ETH_P_IPV6 ==  ntohs(frame_type); } 

  friend std::ostream& operator<< (std::ostream& o, VLANHeader const& hdr);
};

class ARPHeader {
  friend class TAP;
 private:
  uint16_t htype;
  uint16_t proto;
  uint8_t  hsize;
  uint8_t  psize;
  uint16_t opcode;
  uint8_t  src_mac[ETH_HW_ADDR_LEN];
  uint8_t  src_ip[4];
  uint8_t  tgt_mac[ETH_HW_ADDR_LEN];
  uint8_t  tgt_ip[4];

 public:
  
  uint8_t* getSrcMac() { return  src_mac; }
  uint8_t* getDstMac() { return  tgt_mac; }
  uint16_t getHType()  const { return ntohs(htype); }
  uint16_t getProto()  const { return ntohs(proto); }
  uint8_t  getHSize()  const { return hsize; }
  uint8_t  getPSize()  const { return psize; }
  uint16_t getOpcode() const { return ntohs(opcode); }
  uint32_t getSrcIP() const { return ntohl(*((uint32_t*)src_ip)); }
  uint32_t getDstIP() const { return ntohl(*((uint32_t*)tgt_ip)); }

  std::string getOpcodeString() const;
  std::string getPString() const;
  std::string getHString() const;

  void setHType (uint16_t t) {htype = htons(t);}
  void setProto (uint16_t t) {proto = htons(t);}
  void setHSize (uint8_t  t) {hsize=t;}
  void setPSize (uint8_t  t) {psize=t;}
  void setOpcode(uint16_t t) {opcode = htons(t);}

  void setSrcMac(uint8_t addr[]) { 
    for(int i=0;i<ETH_HW_ADDR_LEN;i++)
      src_mac[i]=addr[i];
  }
  void setDstMac(uint8_t addr[]) { 
    for(int i=0;i<ETH_HW_ADDR_LEN;i++)
      tgt_mac[i]=addr[i];
  }
  void setSrcIP(uint32_t addr) { 
    *((uint32_t*)src_ip)=htonl(addr);
  }
  void setDstIP(uint32_t addr) {
    *((uint32_t*)tgt_ip)=htonl(addr);
  }


  bool isArpRequest() { return ntohs(opcode) == ARP_REQUEST;}
  bool isArpReply() { return ntohs(opcode) == ARP_REPLY;}
  bool isRArpRequest() { return ntohs(opcode) == RARP_REQUEST;}
  bool isRArpReplt() { return ntohs(opcode) == RARP_REPLY;}

  friend std::ostream& operator<< (std::ostream& o, ARPHeader const& hdr);
};

class IPv4Header {
  friend class TAP;
 private:
  uint8_t ip_vhl;  /* version and header length (0x4?) */
  uint8_t ip_tos;  /* type of service */
  uint16_t ip_len; /* total length of the datagram (including ip header) */
  uint16_t ip_ident; /* identification */
  uint16_t ip_off; /* flags and fragment offset field */
  uint8_t ip_ttl;  /* time to live */
  uint8_t ip_p;    /* protocol id */
  uint16_t ip_cksum; /* header checksum */
  uint8_t ip_src[4]; /*  soruce ip address */
  uint8_t ip_dst[4]; /* destination ip address */

 public:
  uint8_t getHdrLen() const { return (ip_vhl&0xf)*4; } // in bytes
  uint8_t  getTOS() const { return ip_tos; }
  uint16_t getLen() const { return ntohs(ip_len); }
  uint16_t getIdent() const { return ntohs(ip_ident); }
  uint16_t getOffset() const { return ntohs(ip_off); }
  uint8_t  getTTL() const { return ip_ttl; }
  uint8_t  getProto() const { return ip_p; }
  uint32_t getSrc() const { return ntohl(* ((uint32_t*)ip_src)); }
  uint32_t getDst() const { return ntohl(* ((uint32_t*)ip_dst)); }
  uint16_t getCheckSum() const { return ip_cksum; } // no endian conversion

  void setHdrLen(uint8_t x) { ip_vhl = (0x40|((x>>2)&0xf)); } // in bytes
  void setTOS(uint8_t x) { ip_tos = x; }
  void setLen(uint16_t x) { ip_len = htons(x); }
  void setIdent(uint16_t x) { ip_ident = htons(x); }
  void setOffset(uint16_t x) { ip_off = htons(x); }
  void setTTL(uint8_t x) { ip_ttl = x; }
  void setProto(uint8_t x) { ip_p = x; }
  void setSrc(uint32_t x) { *((uint32_t*)ip_src) = htonl(x); }
  void setDst(uint32_t x) { *((uint32_t*)ip_dst) = htonl(x); }
  void setCheckSum(uint16_t x) { ip_cksum = x; } // no endian conversion
  void decrementTTL(uint8_t x) { ip_ttl -= x; }

  std::string getProtoString() const;

  friend std::ostream& operator<< (std::ostream& o, IPv4Header const& hdr);
};


class IPv6Header {
 private:
  uint8_t ver_cls_label[4];
  uint16_t payload_len;
  uint8_t  next_hdr;
  uint8_t  hop_limit;
  uint8_t  src_addr[16];
  uint8_t  dst_addr[16];
 public:
  
  uint8_t* getSrc() { return  src_addr; }
  uint8_t* getDst() { return  dst_addr; }
  uint16_t getPayloadLength() const { return ntohs(payload_len); }
  /*  uint8_t  getNextHdr() const { return ntohs(next_hdr); }
  uint8_t  getHopLimit() const { return ntohs(hop_limit); }
  uint8_t  getVersion() const { return (ver_cls_label&0xF0000000)>>28; }
  uint8_t  getTrafficClass() const { return (ver_cls_label&0x0FF000000)>>20; }
  uint8_t  getFlowLabel() const { return (ver_cls_label&0x000FFFFFF); }

  void setVersion(uint8_t t) {  ver_cls_label=((t&0x0f)<<28) & (ver_cls_label&0x0FFFFFFFF); }
  void setTrafficClass(uint8_t t) {  ver_cls_label=(t<<20) & (ver_cls_label&0x000FFFFFF); }
  void setFlowLabel(uint32_t t) { ver_cls_label=(ver_cls_label&0xFFF000000)&(t*0x000FFFFFF);}  
  */
  void setPayloadLength(uint16_t t) {payload_len = htons(t);}
  /*
  void setNextHdr(uint8_t t) {next_hdr = t;}
  void setHopLimit(uint8_t t) {hop_limit = t;}
  void setSrc(uint8_t addr[]) { 
    for(int i=0;i<16;i++)
      src_addr[i]=addr[i];
  }
  void setDst(uint8_t addr[]) { 
    for(int i=0;i<16;i++)
      dst_addr[i]=addr[i];
  }
  */
  friend std::ostream& operator<< (std::ostream& o, IPv6Header const& hdr);
};

class TAP {
 public:
  enum State {
    NO_MORE_BYTES=100,
    READ_ERROR,
    ITS_GOOD,
    BAD_PROTO
  };
  TAP(std::string name_, Bridge* bridge_);
  void myread(uint32_t len, bool passive);
  void mywrite(char* buf, int len);
  void processPkt(fd_set *fds);
  void createArpResponse(EthernetHeader* eth, ARPHeader* request);
  void createArpRequest(VETH* veth);
  void routePacket(EthernetHeader* eth, IPv4Header* ip, uint32_t len);
  std::string& getName(){return name;}

  TAP::State getState(){ return state; }
  inline int getFD() const {return fd; }
  inline bool morePkts(){return state == ITS_GOOD && head < pos;}
 private:
  std::string name;
  Bridge* bridge;
  char* buf;
  char* arppacket;
  int arppacketlen;
  uint32_t head,tail,pos;
  TAP::State state;
  int fd;
};

class VETH {
 public:
  VETH(uint32_t ip_, uint8_t *mac_, char* veth, TAP* tap_);
  void send(EthernetHeader* eth, int len);
  friend std::ostream& operator<< (std::ostream& o, VETH const& veth);

  TAP* tap;
  uint8_t ip[4];
  uint8_t mac[6]; 
  int sock;
  std::string veth;
  struct sockaddr_ll socket_address;
};

class Bridge {
 public:
  Bridge();
  VETH* find(uint32_t ip);
  bool parseArgs(int argc, char** argv);
  void run();
 private:
  bool parseBridge(int *i, int argc, char** argv);
  int maxfd;
  std::vector<TAP*> taps;
  std::map<uint32_t, VETH*> mac2ip;
};


