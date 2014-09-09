
#include <limits.h>
#include <unistd.h>

#include <algorithm>

#include "debug.h"
#include "circle_buffer.h"
#include "ssfgwd_error.h"

void CircleBuffer::init_logic( const unsigned init_size, const unsigned expand_size_ )
{
  if( currsize <= 1 ) currsize = 0;
  if( expand_size_ == (unsigned)~0 ) 
    expand_size = init_size;
  if( expand_size <= 1 ) expand_size = 2;
  if( currsize ) buffer = new uint8_t[currsize];
  if( !buffer ) currsize = 0;
}

#define _INIT_CHAIN total_read(tread), total_written(twrite), \
  current_read_pos(read_pos), current_write_pos(write_pos), \
  read_pos(0), write_pos(0), \
  buffer(0), expand_size(expand_size_), currsize(init_size), \
  tread(0), twrite(0)
  

CircleBuffer::CircleBuffer( const unsigned init_size, const unsigned expand_size_ )
  : _INIT_CHAIN
{
  init_logic( init_size, expand_size_ );
}

CircleBuffer::~CircleBuffer( void )
{
  if( buffer ) delete[] buffer;
  buffer = 0;
  currsize = 0;
  reset();
}

// allocate a uint8_t[] and copy the write space into it, allocates currsize + xtra_size bytes 
// and places total into new_size
uint8_t* CircleBuffer::new_write_space( unsigned& new_size, const unsigned xtra_size )
{
  new_size = currsize + xtra_size;
  if( !new_size ) return 0;
  uint8_t* p = new uint8_t[new_size];
  if( !p ) {
    new_size = 0;
    return 0;
  }

  // copy the data into it
  CircleBuffer::vector_space vs;
  vs( this, write_pos, read_pos, false ); 
  uint8_t* cpy_to = p;
  for( size_t vc = 0; vc < vs.vcount; vc++ ) {
    memcpy( cpy_to, vs.v[vc].iov_base, vs.v[vc].iov_len );
    cpy_to += vs.v[vc].iov_len;
  }

  return p;
}

void CircleBuffer::reset( void )
{
  read_pos = write_pos = 0;
  tread = twrite = 0;
}

void CircleBuffer::reset( const uint8_t* const data, unsigned size )
{
  reset();
  append( data, size );
}

void CircleBuffer::reset( const string& strdata )
{
  reset();
  append( strdata );
}

unsigned CircleBuffer::space( unsigned from, unsigned to, bool read_space ) const
{
  if( !currsize ) return 0;
  unsigned cs;
  if( from == to ) {
    cs = currsize - 1;
  } else if( from < to ) {
    cs = to - from;
  } else {
    // from > to; the length to the end and the wrap around
    cs = currsize - from;
    cs += to;
  }

  if( read_space ) {
    if( cs == 1 ) {
      // we can never read just one byte because it would leave
      // read_pos == write_pos and we wouldn't be able to detect this
      // state from absolute startup
      cs = 0;
    }
  } else {
    if( from == to ) {
      // we cannot write ahead of the read_pos marker
      cs = 0;
    }
  }

  return cs;
}


int CircleBuffer::read( const int fd, unsigned* const original_read_pos )
{
  vector_space vs;
  vs( this, read_pos, write_pos, true );
  if( !vs.total ) {
    if( !expand() ) { 
      return 0;
    } 
    // recalc
    vs( this, read_pos, write_pos, true );
  }
  if( original_read_pos ) *original_read_pos = read_pos;
  int r = vs.read( fd );
  if( r > 0 ) {
    tread += r;
    read_pos = ( read_pos + r ) % currsize;
    D( "read %u bytes, read_pos = %u\n", r, read_pos );
  } 
  return r;
}

int CircleBuffer::write( const int fd )
{
  vector_space vs;
  vs( this, write_pos, read_pos, false );
  if( !vs.total ) return 0;
  int r = vs.write( fd );
  if( r > 0 ) {
    twrite += r;
    write_pos = ( write_pos + r ) % currsize;
    _D( "wrote %u bytes, write_pos = %u\n", r, write_pos );
  }
  return r;
}

int CircleBuffer::write( CircleBuffer& dest, unsigned length, bool transfer_data )
{
  // create a vector space
  vector_space vs;
  vs( this, write_pos, (write_pos+length)%currsize, true );
  
  for(unsigned i=0;i<vs.vcount;i++ ) {
    // transfer block
    dest.append( static_cast<uint8_t*>(vs.v[i].iov_base), vs.v[i].iov_len );
  }
  if( transfer_data ) {
    // skip up the source
    write_pos = ( write_pos + vs.total ) % currsize;  
    twrite += vs.total;
  }
  return vs.total;
}

bool CircleBuffer::expand( void )
{
  if( expand_size == 0 ) return false;

  unsigned wspace = write_space();
  unsigned new_size;
  uint8_t* new_buffer = new_write_space( new_size, expand_size );
  if( !new_buffer ) return false;

  if( currsize ) {
    delete[] buffer;
  }

  currsize = new_size;
  buffer = new_buffer;
  write_pos = 0;
  read_pos = wspace;

  D( "expanded to %u, read_pos=%u, write_pos=%u\n", currsize, read_pos, write_pos );

  return true;
}

#if defined(SSFGWD_DEBUG)
void CircleBuffer::vector_space::debug( void ) const
{
  _D( "vector_space %p %p:%u %p:%u\n", this, v[0].iov_base, v[0].iov_len,
    v[1].iov_base, v[1].iov_len );
}
#endif

void CircleBuffer::vector_space::operator()( const CircleBuffer* const cb,
    const unsigned from, const unsigned to, bool read_space )
{
  // init
  vcount = total = 0;
  v[0].iov_base = v[1].iov_base = this;
  v[0].iov_len = v[1].iov_len = 0;

  unsigned s = cb->space( from, to, read_space );
  if( !s )  return;

  // readv
  vcount = 1;
  total = s;
  if( from < to ) {
    v[0].iov_base = &cb->buffer[from];
    v[0].iov_len = s;
  } else if( from >= to ) {
    // from from to end of buffer
    v[0].iov_base = &cb->buffer[from];
    v[0].iov_len = std::min(cb->currsize-from, s);
    if( v[0].iov_len < s ) {
      s -= v[0].iov_len;
      v[1].iov_base = &cb->buffer[0];
      v[1].iov_len = s;
      vcount = 2;
    }
  }
#if defined(SSFGWD_DEBUG)
  debug();
#endif
}

// place the data into the read space, expand the circular buffer if needed
CircleBuffer& CircleBuffer::append( const uint8_t* data, unsigned size )
{
  if( !size ) return *this;

  if( read_space() >= size ) {
    // we can simply append it now without acquiring new memory
    CircleBuffer::vector_space vs;
    vs( this, read_pos, write_pos, true ); 

    // truncate vector space to only span our data size
    if( vs.v[0].iov_len >= size ) {
      vs.vcount = 1;
      vs.v[0].iov_len = size;
    } else {
      vs.v[1].iov_len = size - vs.v[0].iov_len;
    }

    const uint8_t* cpy_from = data;
    for( size_t vc = 0; vc < vs.vcount; vc++ ) {
      memcpy( vs.v[vc].iov_base, cpy_from, vs.v[vc].iov_len );
      cpy_from += vs.v[vc].iov_len;
      read_pos = ( read_pos + vs.v[vc].iov_len ) % currsize;
    }
    tread += size;
    return *this;
  }


  // we must allocate some new space
  unsigned wspace = write_space();
  unsigned new_size;
  uint8_t* new_buffer = new_write_space( new_size, std::max(size,expand_size) );
  if( new_size == 0 ) {
    errorf( "circle buffer append failure - out of memory?\n" );
    return *this;
  }

  // append the data
  memcpy( &new_buffer[wspace], data, size );
  wspace += size;

  if( currsize ) {
    delete[] buffer;
  }

  buffer = new_buffer;
  write_pos = 0;
  read_pos = wspace;
  currsize = new_size;
  tread += size;

  _D( "expanded to %u, read_pos=%u, write_pos=%u\n", currsize, read_pos, write_pos );

  return *this;
}



