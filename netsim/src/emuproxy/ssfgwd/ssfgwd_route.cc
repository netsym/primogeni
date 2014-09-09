/**
 * one proxy connection
 */

#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <netdb.h>

#include "debug.h"
//#include "fd.h"
#include "ssfgwd_error.h"
#include "ssfgwd_route.h"

#define PRIME_MAX_MESSAGE_LENGTH (sizeof(struct ip) + sizeof(uint32_t))

// ip circle buffer pools sized at half a Meg and expanded in 64K byte chunks
#if 1
static CircleBuffer* new_buffer_pool(void) throw( std::bad_alloc ) { return new CircleBuffer( 1<<19, 1<<16 ); }
#else
// used for testing wrap at the end of circular buffers
static CircleBuffer* new_buffer_pool(void) throw( std::bad_alloc ) { return new CircleBuffer( 80, 10 ); }
#endif

NewRoute::NewRoute( const Listener::connection& conn_, FILE* const logfp_ )
  : ipaddr(mirror_ip.ip), peer_type(type), conn(conn_), 
  setup_type(type), setup_ip_list(ip_list), logfp(logfp_),
    setup(0), type(0)
{
}

string NewRoute::description(void) const
{
  const unsigned B(64);
  char b[B];
  string r( "new route " );

  r += (string)(ip_list);
  switch( type ) {
    case READER : r += "reader" ; break;
    case WRITER : r += "writer" ; break;
    case BOTH : r += "reader+writer" ; break;
  }
  snprintf( b, B, " socket descriptor %d", conn.connsock );
  r += b;
  return r;
}

void NewRoute::skip_setup( const NewRoute::PEER_TYPE type_, const uint32_t remote_address )
{
  setup = 5;
  type = type_;
  ip_list.clear();
  if( type != WRITER ) {
    setup_ip ip;
    ip.ip = remote_address;
    ip_list.push_back( ip );
  }
}

void NewRoute::select_set_r( SelectCall& sc ) const
{
  // we always want to read from the ioinjector
  
  /**
   * NOTE:  we don't need to check sock or tunfd for <0 values because
   * SelectCall is smart enough to ignore fd<0
   */
  sc.select_on_r( conn.connsock );
  sc.select_on_x( conn.connsock );
}

// select operations in setup mode - returns true when setup is complete
bool NewRoute::select_isset( const SelectCall& sc, bool& io_error )
{
  if( sc.is_set_x( conn.connsock )) {
    io_error = true;
    return false;
  }

  if( !sc.is_set_r( conn.connsock )) {
    return false;
  }

  while( setup < 5 ) {
    // read setup bytes
    uint8_t b;
    int r = read( conn.connsock, &b, 1 );
    if( r == 0 ) {
      // eof
      perrorf( errno, "setup route %p premature eof", this );
      io_error = true;
      return false;
    } else if( r < 0 ) {
      if( errno != EAGAIN ) {
        // error, remove myself from setup
        perrorf( errno, "setup route %p read()", this );
        io_error = true;
      }
      return false;
    }
    switch( setup ) {
      case 0 : type = b; break;
      default : mirror_ip.ipbytes[setup-1] = b; break;
    }

    setup++;

    switch( type ) {
      case NewRoute::WRITER :
        // writers do not send any ip addresses so we are done now
        return true;
      case NewRoute::READER :
        // readers send an arbitrary number of addresses
        if(( setup == 5 ) and mirror_ip.ip ) {
          // another ip address
          ip_list.push_back( mirror_ip );
          setup = 1;
        }
        break;
      case NewRoute::BOTH :
        // reader+writer (aka testing connections) may only provide one address
        if(( setup == 5 ) and mirror_ip.ip ) {
          // another ip address
          ip_list.push_back( mirror_ip );
          setup = 1;
        }
        if( ip_list.size() > 1 ) {
          errorf( "setup route %p reader+writer send too many addresses\n", this );
          io_error = true;
          return false;
        }
        break;
    }
  }

  return setup == 5;
}



Route::Route( const NewRoute& nr, bool attached_, FILE* const logfp_, const bool zero_frame_padding, enum Route::LOOP_AT loop_at_, Route::ReflectorSet& reflector_ips)
  : to_buf(NULL), ip_list(nr.setup_ip_list), tofd(-1), fromfd(-1),
    attached(attached_), logfp(logfp_), from_buf(NULL), 
    from_frame(0,zero_frame_padding?0:0), loopat(loop_at_), reflectorIps(reflector_ips)
{
  try {
    if( nr.peer_type == NewRoute::BOTH ) {
      tofd = dup(nr.conn.connsock);
      to_buf = new_buffer_pool();  
      fromfd = dup(nr.conn.connsock);
      from_buf = new_buffer_pool();  
    } else if( nr.peer_type == NewRoute::READER ) {
      tofd = dup( nr.conn.connsock );
      to_buf = new_buffer_pool();  
    } else if( nr.peer_type == NewRoute::WRITER ) {
      fromfd = dup( nr.conn.connsock );
      from_buf = new_buffer_pool();  
    }
  } catch( std::bad_alloc& e ) {
    errorf( "cannot allocate buffer for connection fd %d from %s\n", nr.conn.connsock, nr.conn.ipstr );
    reset();
    // reraise
    throw e;
  }
  if (nr.peer_type == NewRoute::READER  || nr.peer_type == NewRoute::BOTH) {
    if( Route::LOOP_THROUGH_INBOUND==loopat ) {
      // Need to send data to the reader like we were a client!!
      for( NewRoute::list::const_iterator q=ip_list.begin(); q!= ip_list.end(); q++ ) {
        const unsigned B(17);
        char b[B];
        snprintf( b, B, "%d.%d.%d.%d", q->ipbytes[0], q->ipbytes[1],
                  q->ipbytes[2], q->ipbytes[3] );
        string f(b);
        for(Route::ReflectorSet::iterator i = reflectorIps.begin(); i!=reflectorIps.end();i++) {
		  //printf(" '%s'=='%s'--->%i\n",i->c_str(), f.c_str(), *i == f);
          if(*i == f) {
	            fprintf(stderr, "Reflecting %s\n", i->c_str()); fflush(stderr);
	            send_prime_io_msg(b,b);
				break;
          }
        }
      }
    }
  }
  D( "connection fd %d from %s\n", nr.conn.connsock, nr.conn.ipstr );
}

void Route::reset( void )
{
  // we don't care about errors with these calls
  close(tofd);
  close(fromfd);
  delete from_buf;
  delete to_buf;
  from_buf = to_buf = NULL;
  tofd = fromfd = -1;
}

Route::~Route( void )
{
  reset();
}

string Route::description(void) const
{
  string r( static_cast<string>(ip_list) );  
  const unsigned B = 128;
  char b[B];
  snprintf( b, B, "(tofd,fromfd)=(%d,%d)", tofd, fromfd ); 
  r += b;
  return r;
}

void Route::select_set_r( SelectCall& sc ) const
{
  // we always want to read from the ioinjector
  
  /**
   * NOTE:  we don't need to check sock or tunfd for <0 values because
   * SelectCall is smart enough to ignore fd<0
   */
  sc.select_on_r( fromfd );
  sc.select_on_x( fromfd );
  from_read_complete = to_write_complete = false;
}

void Route::select_set_w( SelectCall& sc ) const
{
  /**
   * NOTE:  we don't need to check sock or tunfd for <0 values because
   * SelectCall is smart enough to ignore fd<0
   */
  if( to_buf->write_space() > 0 ) {
    sc.select_on_w( tofd );
  }
  from_read_complete = to_write_complete = false;
}


/**
 * this function is called when there is fd activity, a Route:: is an 
 * established connection to an EmuProxy.
 */
void Route::select_isset( const SelectCall& sc, CircleBuffer& outbound_buf, 
			  CircleBuffer& loopback_buf, bool& io_error )
{
  
  if( sc.is_set_x( fromfd )) {
    if(logfp) { fprintf( logfp, "%s fd exception\n", description().c_str() ); fflush(logfp); }
    io_error = true;
    return;
  }

  if( !from_read_complete && sc.is_set_r( fromfd )) {
    from_read_complete = true;
    // copy data arriving from the peer
    if( from_buf->read( fromfd ) < 0 ) {
      perrorf( errno, "%s read()", description().c_str() );
      io_error = true;
      return;
    }
    unsigned len;

    while(( len = ipframe_find( *from_buf, from_frame, attached )) > 0 ) { 
      if( from_frame.PRE_PADDING ) {
        from_buf->write_skip( from_frame.PRE_PADDING );
      }
      if(logfp) { fprintf( logfp, "> from_ssf: %s\n",from_frame.describe().c_str() ); fflush(stdout); }
      
      if(from_frame.proto == 254) {
        // Dr. Lius 254 protocol loopback implementation
        from_buf->write( loopback_buf, len, true );
      } else if( loopat == Route::LOOP_THROUGH_INBOUND ) {
        //find which iothread to reflect this back to
        //route_reflected_msg();
        from_buf->write( loopback_buf, len, true );
      } else {
        // otherwise it goes into the tunnel/openvpn outbound buffer  
        from_buf->write( outbound_buf, len, true );
      }

      if( from_frame.POST_PADDING ) {
        from_buf->write_skip( from_frame.POST_PADDING );
      }
    }
  }

  // copy data going to the peer
  if( !to_write_complete && sc.is_set_w( tofd )) {
    to_write_complete = true;
    if( to_buf->write( tofd ) < 0 ) {
      perrorf( errno, "%s write()", description().c_str() );
      io_error = true;
      return;
    }
  }

}

string Route::stats( const STATS_TYPE st ) const 
{
  const unsigned B(1024);
  char b[B];

  string r;

  switch( st ) {
    case STATS_ALL :
    case STATS_TO :
      if( to_buf ) {
        snprintf( b, B, "to ssf: %u buf_size; %u tot_bytes; %u write_space",
            to_buf->memory_space(), to_buf->total_written, to_buf->write_space() );
        r += b;
      } else {
        r += "to ssf: none";
      }
      if( st == STATS_ALL ) {
        r+= ";; ";
      }
      break;
    case STATS_FROM : // shut up g++
      break;
  }

  switch( st ) {
    case STATS_ALL :
    case STATS_FROM :
      if( from_buf ) {
        snprintf( b, B, "from ssf: %u buf_size; %u tot_bytes; %u write_space",
            from_buf->memory_space(), from_buf->total_written, from_buf->write_space() );
        r += b;
      } else {
        r += "from ssf: none";
      }
      break;
    case STATS_TO : // be quiet g++
      break;
  }

  return r;
}


// Stuff for the reflector
uint16_t Route::iphdr_cksum(uint16_t *buf, uint32_t length)
{
  uint32_t sum;
  
  for (sum=0; length > 0; length--)
    {    	
	//fprintf(stderr,"sum=%i, buf(0x%x), length=%i\n",sum,buf,length);
	//fflush(stderr);
      sum += *buf++;
    }
  sum = (sum >> 16) + (sum & 0xffff);
  sum += (sum >> 16);
  return (~sum);
};


    
void Route::send_prime_io_msg(char* srcIP, char* dstIP) { //always send to tofd/write to to_buff
  //much of this was stolen from ssfnet/src/emuproxy/openvpn-2.1_beta14/plugin/client-conn/client-conn.c
  fprintf(stderr, " sending init msg to %s\n", srcIP);fflush(stderr);
  char datagram[PRIME_MAX_MESSAGE_LENGTH]; /* hdrs & payload - big enough for any packet*/
  uint32_t *primePrefix = (uint32_t *) datagram; /* this is the virt source ip*/
  struct ip *primeIP = (struct ip *) (datagram + sizeof(uint32_t)); /*IP hdr follows the prime prefix*/
  memset(datagram,0,PRIME_MAX_MESSAGE_LENGTH);
  /* First build the headers */
  /* overload the TOS field for PRIME meaning */
  primeIP->ip_tos = 0x01; // this means we are connecting this client to this gateway  
  primeIP->ip_len = htons(sizeof(struct ip)); /* no payload & no other hdrs*/
  primeIP->ip_hl = 5;
  primeIP->ip_v = 4;
  primeIP->ip_id = htonl(54321);
  primeIP->ip_off = 0;
  primeIP->ip_ttl = 255;
  primeIP->ip_p = IPPROTO_TCP;
  primeIP->ip_sum = 0;
  primeIP->ip_dst.s_addr = inet_addr(dstIP); //XXX what else?
  primeIP->ip_src.s_addr = inet_addr(srcIP);
  primeIP->ip_sum = iphdr_cksum((uint16_t *) primeIP, (primeIP->ip_len >> 1));
  
  /* the prefix */
  *primePrefix = primeIP->ip_src.s_addr;
  int cc = write(tofd,datagram,PRIME_MAX_MESSAGE_LENGTH);
  /* write to the reader thread */
  if ( cc < 0) {
    fprintf(stderr,"[In prime_sendMsg] Unable to send ip msg!!\n");
    exit(1);
  } else if(logfp) {
   	 fprintf(logfp,"Sent %i bytes with SRC=%s\n",cc, srcIP);
  }
}

bool Route::route_reflected_msg() {
  //rmap::iterator dest = reflectorRoutes->find(from_frame.leadaddr);
  //  if( dest !=  reflectorRoutes->end() ) {
  string s(inet_ntoa(from_frame.leadaddr));
  //if(reflectorRoutes.count(s) > 0 ) {
  if(1) {
    if(logfp) {
      fprintf(logfp,"Routing reflected msg, changing from %s ",  s.c_str());
    }
    if(1) {
      fprintf( stdout, " DOING REFLECTION from_ssf: %s\n",from_frame.describe().c_str() ); 
      fflush(stdout);
    }
    /*    from_frame.leadaddr.s_addr = reflectorRoutes[from_frame.leadaddr.s_addr];
    const char* cp = reflectorRoutes[s].c_str();
    if(0 == inet_aton(cp, &from_frame.leadaddr)) {
      fprintf(stderr," BAD ADDRESS: %s\n", cp);
    }*/
    if(logfp) {
      fprintf(logfp," to %s\n", inet_ntoa(from_frame.leadaddr));
    }
    return true;
  } else {
    fprintf(stderr, "Unable to route reflected msg,reflector_routes[%s]=NULL\nfrom_ssf: %s\n", inet_ntoa(from_frame.leadaddr),from_frame.describe().c_str());
	exit(1);
  }
  return false;
}



