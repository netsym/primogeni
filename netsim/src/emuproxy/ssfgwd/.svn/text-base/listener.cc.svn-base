/**
 * tcp listener for the ssfgwd project
 */


#include <sys/types.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <netdb.h>
#include <inttypes.h>
#include <string.h>

#include "debug.h"
#include "fd.h"
#include "select_call.h"
#include "ssfgwd_error.h"
#include "listener.h"

Listener::Listener( const uint16_t port, const int backlog )
  : sock(-1)
{
  struct sockaddr_in addr;
  struct sockaddr* pa = (struct sockaddr*)( &addr );

  if(( sock = socket( PF_INET, SOCK_STREAM, IPPROTO_IP )) == -1 ) {
    /*  error */
    perrorf_exit( errno, "socket()" );
  }

  if( o_nonblock( sock ) < 0 ) {
    errorf_exit( "o_nonblock()" );
  }


  /* this call may fail for local addresses, so we ignore the error code */
  int b = 1;
  setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &b, sizeof(b));

  memset( &addr, 0, sizeof( struct sockaddr_in ));
  addr.sin_family = PF_INET;
  addr.sin_port = htons( (short)port );  
  addr.sin_addr.s_addr = htonl(INADDR_ANY);

  if( bind( sock, pa, sizeof( addr )) == -1 ) {
    close( sock );
    perrorf_exit( errno, "bind()" );
  }

  if(( listen( sock, backlog ) == -1 )) {
    close( sock );
    perrorf_exit( errno, "listen()" );
  }

  D( "listening with %d on %hu with %d backlog\n", sock, port, backlog );
}

Listener::~Listener( void )
{
  close( sock );
  sock = -1;
}

// setup to be watched via a select call
void Listener::select_set( SelectCall& sc ) const
{
  if( sock >= 0 ) {
    sc.select_on_r( sock );
  }
}

// returns <0 on error, 1 on success with the connected socket in c, 
// and 0 if there is no activity
int Listener::select_isset( const SelectCall& sc, struct connection& c )
{
  if( !sc.is_set_r( sock )) return 0;

  memset( &c.addr, 0, sizeof( c.addr ));
  socklen_t s = sizeof( c.addr );
  c.connsock = accept( sock, (sockaddr*)&( c.addr ), &s );
  if( c.connsock == - 1 ) {
    perror( "accept()" );
    return -1;
  }

  if( o_nonblock( c.connsock ) < 0 ) {
    errorf( "connection must be non-blocking - aborting connection %d\n", c.connsock );
    close( c.connsock );
    return -1;
  }

  inet_ntop( AF_INET, &c.addr.sin_addr, c.ipstr, sizeof(c.ipstr));

  return 1;
}

// for client mode testing
Listener::connection::connection( struct sockaddr_in& server )
{
  connsock = socket( PF_INET, SOCK_STREAM, IPPROTO_IP );
  if( connsock < 0 ) {
    perror( "socket()" );
    return;
  }
  
  int r = connect( connsock, reinterpret_cast<struct sockaddr*>(&server), sizeof(server));
  if( r < 0 ) {
    perror( "connect()" );
    return;
  }

  if( o_nonblock( connsock ) < 0 ) {
    errorf( "connection must be non-blocking - aborting connection %d\n", connsock );
    close( connsock );
    connsock = -1;
    return;
  }
  
  addr = server;
  inet_ntop( AF_INET, &addr.sin_addr, ipstr, sizeof(ipstr));
}

