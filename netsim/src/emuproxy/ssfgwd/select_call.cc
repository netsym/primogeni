/**
 * implementation of a C++ ized select() object
 */

#include <inttypes.h>
#include <errno.h>

#include "debug.h"
#include "select_call.h"

void SelectCall::reinit( const bool eintr_mode )
{
  rfds = wfds = xfds = 0;
  maxfd = -1;
  msecs = 0;
  FD_ZERO( &rset );
  FD_ZERO( &wset );
  FD_ZERO( &xset );

  eintr = eintr_mode;
}

void SelectCall::select_on( const int fd, const uint32_t _msecs, const bool read, const bool write, const bool except )
{
  if( fd < 0 ) return;
  update_maxfd( fd );
  update_msecs( _msecs );
  if( read ) {  FD_SET( fd, &rset ); rfds = true; }
  if( write ) { FD_SET( fd, &wset ); wfds = true; }
  if( except ) { FD_SET( fd, &xset ); xfds = true; }
}

bool SelectCall::is_set( const int fd, bool read, bool write, bool except ) const
{
  if( fd < 0 ) return false;
  read = read && FD_ISSET( fd, &rset );
  write = write && FD_ISSET( fd, &wset );
  except = except && FD_ISSET( fd, &xset );
  return read||write||except;
}

// return values for operator()s are the same as the select() call
int SelectCall::operator()( const uint32_t max_msecs )
{
  uint32_t r, s;
  return this->operator()( r, s, max_msecs );
}

int SelectCall::operator()( uint32_t& msecs_remaining, uint32_t& msecs_spent, const uint32_t max_msecs )
{
  update_msecs( max_msecs );
  uint32_t ms = this->msecs;

  struct timeval tv = { 0, 0 }, *ptv=NULL;
  if( ms ) {
    ptv = &tv;
    if( ms ) {
      tv.tv_sec = ms / 1000;
      tv.tv_usec = ( ms % 1000 ) * 1000;
    }
  }

eintr_again:
  int r = ::select( maxfd>=0?maxfd+1:0, rfds?&rset:NULL, wfds?&wset:NULL,xfds?&xset:NULL, ptv ); 
  if(( r == -1 ) && this->eintr && ( errno == EINTR )) goto eintr_again;

  if( ms ) {
    // update msecs
    msecs_remaining = ptv->tv_sec * 1000 + ptv->tv_usec / 1000;
    msecs_spent = ms - msecs_remaining;
  }
  return r;
}

