/**
 * tcp listener for the ssfgwd project
 */

#ifndef LISTENER_H
#define LISTENER_H

#include <inttypes.h>
#include <netinet/in.h>

#include "select_call.h"

class Listener {
  public:
    // this is filled in by select_isset() if a connection is
    // accepted
    struct connection {
      // for use in client mode testing
      connection( struct sockaddr_in& server );
      // the usual constructor
      connection( void )
        : connsock(-1)
      {
        memset( &addr, 0, sizeof( addr ));
        memset( ipstr, 0, sizeof( ipstr ));
      }
      // copy constructor
      connection( const struct connection& c )
      {
        memcpy( this, &c, sizeof(c) );
        connsock = dup(c.connsock);
      }
      ~connection() { close(connsock); }
      int connsock;
      struct sockaddr_in addr;
      char ipstr[INET_ADDRSTRLEN];
    };

    Listener( const uint16_t port, const int backlog );
    ~Listener( void );

    // setup to be watched via a select call
    void select_set( SelectCall& sc ) const;
    // returns <0 on error, 1 on success with the connected socket in c, 
    // and 0 if there is no activity
    int select_isset( const SelectCall& sc, struct connection& c );

  private:
    int sock;
};

#endif

