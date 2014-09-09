/**
 * select_cxx is the C select() call C++ ized
 */

#ifndef SELECT_CALL_H
#define SELECT_CALL_H

#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <inttypes.h>

class SelectCall {
  public:
    SelectCall( void ) { reinit(false); }
    SelectCall( const bool eintr_mode ) { reinit(eintr_mode); }
    void reinit( const bool eintr_mode );
    void select_on( const int fd, const uint32_t _msecs, const bool read, const bool write, const bool except );
    void select_on( const int fd, const bool read, const bool write, const bool except )
    { select_on( fd, 0, read, write, except ); }
    void select_on_r( const int fd, const uint32_t _msecs )
    { select_on( fd, _msecs, true, false, false ); }
    void select_on_w( const int fd, const uint32_t _msecs )
    { select_on( fd, _msecs, false, true , false ); }
    void select_on_x( const int fd, const uint32_t _msecs )
    { select_on( fd, _msecs, false, false, true ); }
    void select_on_r( const int fd )
    { select_on( fd, 0, true, false, false ); }
    void select_on_w( const int fd )
    { select_on( fd, 0, false, true , false ); }
    void select_on_x( const int fd )
    { select_on( fd, 0, false, false, true ); }
    bool is_set( const int fd, bool read, bool write, bool except ) const;
    bool is_set_r( const int fd ) const { return fd < 0 ? false : FD_ISSET( fd, &rset ); }
    bool is_set_w( const int fd ) const { return fd < 0 ? false : FD_ISSET( fd, &wset ); }
    bool is_set_x( const int fd ) const { return fd < 0 ? false : FD_ISSET( fd, &xset ); }
    // return value is the same as the select call
    int operator()( const uint32_t max_msecs=0 );
    int operator()( uint32_t& msecs_remaining, uint32_t& msecs_spent, const uint32_t max_msecs=0 );
  private:
    int maxfd;
    fd_set rset;
    fd_set wset;
    fd_set xset;
    unsigned int rfds : 1;  
    unsigned int wfds : 1;  
    unsigned int xfds : 1;  // exceptions
    unsigned int eintr : 1;  // recall on EINTR
    uint32_t msecs;
    void update_maxfd( const int fd )
    {
      if( maxfd < fd ) maxfd = fd;
    }
    void update_msecs( const uint32_t _msecs )
    {
      if( !_msecs ) return;
      if( !this->msecs || ( _msecs < this->msecs )) this->msecs = _msecs;
    }
};


#endif

