/**
 * utilities for the identification and transfer of ipframes
 * inside and between circular buffers.
 */

#ifndef IPFRAME_UTILS_H
#define IPFRAME_UTILS_H

#include <netinet/in.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <time.h>


#include <utility> // for std::pair
#include <string>
using std::string;

#include "circle_buffer.h"

struct ip_frame_info {
  ip_frame_info( const unsigned pre_padding, const unsigned post_padding )
    : PRE_PADDING(pre_padding), POST_PADDING(post_padding)
  {}
  struct in_addr leadaddr;
  struct in_addr saddr;
  struct in_addr daddr;
  uint16_t length;
  uint8_t proto;
  string describe( void ) const;
  const unsigned PRE_PADDING;
  const unsigned POST_PADDING;
};

// returns the number of bytes constituting the next ip frame, 0 if
// a whole ipframe does not exist.
unsigned ipframe_find( CircleBuffer& cb, ip_frame_info& frame, bool has_lead );

#if 0
// copy as many ipframes as possible from src to dst
void ipframe_copy( CircleBuffer& dst, CircleBuffer& src, ip_frame_info& d, bool has_lead );

// copy a pre-identified ipframe from src to dst 
void ipframe_copy( CircleBuffer& dst, CircleBuffer& src, const ip_frame_info& frame );
#endif

#endif

