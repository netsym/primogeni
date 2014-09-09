/**
 * utilities for the identification and transfer of ipframes
 * inside and between circular buffers.
 */

#include "debug.h"
#include "ipframe_utils.h"


string ip_frame_info::describe( void ) const
{
  const unsigned B(128);
  char b[B];
  string r;

  if(leadaddr.s_addr != 0) {
    r += "frame.leadaddr ";
    r += inet_ntop(AF_INET, &leadaddr, b, B);
    r += " ";
  }
  r += "frame.saddr ";
  r += inet_ntop(AF_INET, &saddr, b, B);  
  r += " frame.daddr ";
  r += inet_ntop(AF_INET, &daddr, b, B);  
  snprintf( b, B, " frame.length %u", length );
  r += b;
  snprintf( b, B, " protocol %u", (int)proto );
  r += b;

  return r;
}

// return the byte boundaries of an ip frame within the circular
// buffer.  Returns <cb.size,cb.size> if none are found, returns
// <first_byte_index,last_byte_index> if one is found and fills 
// in the destination ipfield
unsigned ipframe_find( CircleBuffer& cb, ip_frame_info& frame, bool has_lead )
{
  if( !cb.write_space() ) {
    return 0;
  }

  // determine the length
  unsigned len = cb.write_space();
  unsigned extra = has_lead ? 4 : 0;

  // the length of the ipframe is the 3rd and 4th octet
  if( len < (frame.PRE_PADDING+extra+4) ) {
    D("IP length not within write_space()\n" );
    return 0;
  }

  // the beginning must be this index
  unsigned first_byte_pos = cb.current_write_pos + frame.PRE_PADDING;

  if(has_lead) {
    uint8_t* p = reinterpret_cast<uint8_t*>(&frame.leadaddr.s_addr);
    *p = cb[first_byte_pos]; p++;
    *p = cb[first_byte_pos+1]; p++;
    *p = cb[first_byte_pos+2]; p++; 
    *p = cb[first_byte_pos+3];
    first_byte_pos += 4;
  } else frame.leadaddr.s_addr = 0; // indicating no lead

  // ok, find the length of the frame at octet indices 2 and 3
  uint8_t hi, lo;
  hi = cb[first_byte_pos+2];
  lo = cb[first_byte_pos+3];
  frame.length = ( hi << 8 ) | lo;
  D("hibyte=%02x lobyte=%02x combined=%04x\n", hi, lo, frame.length );
  //D("f.length=%u f.PRE=%u f.POST=%u len=%u\n", frame.length, frame.PRE_PADDING, frame.POST_PADDING, len );
  if( (frame.length+extra+frame.PRE_PADDING+frame.POST_PADDING) > len ) {
    // don't have the whole frame yet
    D("still waiting for whole IP frame (%u bytes) in write_space() (%u bytes)\n", 
        frame.length+frame.PRE_PADDING+frame.POST_PADDING, len );
    return 0;
  }

  // find the protocol number of this ip frame
  frame.proto = cb[first_byte_pos+9];

  // fill in the source ip from the 4th 32 bit integer in the frame
  const unsigned saddr_msb_offset = 12;
  uint8_t* p = reinterpret_cast<uint8_t*>(&frame.saddr.s_addr);
  *p = cb[first_byte_pos+saddr_msb_offset]; p++;
  *p = cb[first_byte_pos+saddr_msb_offset+1]; p++;
  *p = cb[first_byte_pos+saddr_msb_offset+2]; p++; 
  *p = cb[first_byte_pos+saddr_msb_offset+3];
  
  // fill in the dest ip from the 5th 32 bit integer in the frame
  const unsigned daddr_msb_offset = 16;
  p = reinterpret_cast<uint8_t*>(&frame.daddr.s_addr);
  *p = cb[first_byte_pos+daddr_msb_offset]; p++;
  *p = cb[first_byte_pos+daddr_msb_offset+1]; p++;
  *p = cb[first_byte_pos+daddr_msb_offset+2]; p++; 
  *p = cb[first_byte_pos+daddr_msb_offset+3];
  
  return frame.length+extra+frame.PRE_PADDING+frame.POST_PADDING;
}

#if 0
// copy as many ipframes as possible from src to dst
void ipframe_copy( CircleBuffer& dst, CircleBuffer& src, ip_frame_info& d, bool has_lead )
{
  while( true ) {
    if( !ipframe_find( src, d, has_lead )) {
      // no more frames, return
      break;
    }

    if( d.PRE_PADDING ) {
      src.write_skip( d.PRE_PADDING );
    }

    D( "from_frame %s\n", d.describe().c_str() );

    ipframe_copy( dst, src, const_cast<const ip_frame_info&>(d) );

    if( d.POST_PADDING ) {
      src.write_skip( d.POST_PADDING );
    }
  }
}

// copy a pre-identified ipframe from src to dst 
void ipframe_copy( CircleBuffer& dst, CircleBuffer& src, const ip_frame_info& frame )
{
  // length is encoded in .second, the start position is implicitly the current write position
  if(frame.leadaddr.s_addr != 0) src.write( dst, frame.length+4, true );
  else src.write( dst, frame.length, true );
  D("copy %u sized ip frame from CircleBuffer %p to CircleBuffer %p\n", frame.length, &src, &dst );
}
#endif
