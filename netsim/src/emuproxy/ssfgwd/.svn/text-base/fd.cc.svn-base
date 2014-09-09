/**
 * file descriptor utilities
 */

#include "fd.h"

// returns < 0 on error, 0 on success
int o_nonblock( const int fd )
{
  int fdflags;

  fdflags = fcntl( fd, F_GETFL );
  if( fdflags < 0 ) { 
    perrorf( errno, "o_nonblock()" );
    return -1;
  }

  fdflags |= O_NONBLOCK;  
  if( fcntl( fd, F_SETFL, fdflags ) < 0 ) {
    perrorf( errno, "o_nonblock()" );
    return -1;
  }

  return 0;
}
