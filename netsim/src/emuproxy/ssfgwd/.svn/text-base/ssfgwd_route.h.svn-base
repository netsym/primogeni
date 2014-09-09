/**
 * a route object for the ssfgwd.
 * this object represents one connection between the ssfgwd daemon process and 
 * a remote ioinjector process.
 */

#ifndef SSFGWD_ROUTE_H 
#define SSFGWD_ROUTE_H 
#include <stdio.h>
#include <stdlib.h>
#include <vector>
using std::vector;
#include <map>
using std::map;
#include <set>
using std::set;
#include <string>
using std::string;

#include "select_call.h"
#include "circle_buffer.h"
#include "listener.h"
#include "ipframe_utils.h"


class NewRoute { 
  public:
    // type of connection
    enum PEER_TYPE { READER=0, WRITER=1, BOTH=255 };
    union setup_ip {
      setup_ip(void) : ip(0) {}
      uint8_t ipbytes[4];
      uint32_t ip;
    };
    class list : public std::vector<setup_ip> {
      public :
        operator string( void ) const
        {
          if( !size() ) {
            return "empty ip list ";
          }

          const unsigned B(17);
          char b[B];

          string r;
          for( const_iterator i=begin(); i!=end(); i++ ) {
            snprintf( b, B, "%d.%d.%d.%d ", i->ipbytes[0], i->ipbytes[1],
                i->ipbytes[2], i->ipbytes[3] );
            r += b;
          }
          return r;
        }
    };
    // container used for route objects - note that pointers are kept in the container,
    // NOT copies of the explicit object
    typedef std::set<NewRoute*> set;

    NewRoute( const Listener::connection& conn, FILE* const logfp );
    void select_set_r( SelectCall& sc ) const ;
    bool select_isset( const SelectCall& sc, bool& io_error );
    void skip_setup( const PEER_TYPE type_, const uint32_t remote_address );
    string description(void) const;

    const uint32_t& ipaddr;
    const uint8_t& peer_type;
    const Listener::connection conn; // connection to the ioinjector
    const uint8_t& setup_type; 
    const list& setup_ip_list;

  private:
    FILE* const logfp;
    // enough to signify the 5th startup byte
    unsigned setup : 3;
    uint8_t type;
    setup_ip mirror_ip;
    list ip_list;
};

class Route {
  public:
    // container used for route objects - note that pointers are kept in the container,
    // NOT copies of the explicit object
    class map : public std::map<uint32_t,Route*> {
      public :
        // removes all instances of route
        void erase( const Route* route )
        {
          for( iterator i=begin(); i!=end(); ) {
            iterator c(i);
            i++;
            if( c->second == route ) {
              std::map<uint32_t,Route*>::erase(c);
            }
          }
        }
    };
    typedef std::set<std::string> ReflectorSet;
    typedef std::set<Route*> set;

    enum STATS_TYPE { STATS_ALL=0, STATS_TO=1, STATS_FROM=2 };
    enum LOOP_AT { NOLOOP=0, LOOP_THROUGH_INBOUND=2 };

    Route( const NewRoute& ready_peer, bool attached, FILE* const logfp, const bool zero_frame_padding, enum Route::LOOP_AT loop_at_, Route::ReflectorSet& reflector_ips);
    ~Route( void );

    void select_set_r( SelectCall& sc ) const ;
    // returns true if it has outbound data for the tunnel device
    void select_set_w( SelectCall& sc ) const ;
    void select_isset( const SelectCall& sc, CircleBuffer& outbound_buf, 
		       CircleBuffer& loopback_buf, bool& io_error );
    string description(void) const;
    string stats( const STATS_TYPE st = STATS_ALL ) const ;

    // needs to be public for main()
    CircleBuffer* to_buf;    //data destined for the ioinjector
    const NewRoute::list ip_list;

    //for reflecting backing back to the iothreads
    uint16_t iphdr_cksum(uint16_t *buf, uint32_t length);
    void send_prime_io_msg(char* srcIP, char* dstIP);
    bool route_reflected_msg();

  private:
    int tofd, fromfd;  // descriptors for outbound and inbound socket
    bool attached;
    FILE* const logfp;
    CircleBuffer* from_buf;  //data read from the ioinjector

    void reset( void );

    ip_frame_info from_frame;

    // so we don't mangle our select call constness for a clr() method
    mutable bool from_read_complete, to_write_complete;

    // what type of loopback mode to place...
    enum LOOP_AT loopat;
    Route::ReflectorSet reflectorIps;
};

#endif
