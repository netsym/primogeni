/**
 * a circular buffer for the ssfgwd i/o
 */

#ifndef CBUFFER_H
#define CBUFFER_H

#include <sys/uio.h>
#include <inttypes.h>

#include <string>
using std::string;
#include <queue>
using std::priority_queue;
#include <vector>
using std::vector;

class CircleBuffer {
  public:
    // memory allocation failed if read_space returns 0 after creation
    // default expand_size == init_size
    CircleBuffer( const unsigned init_size, const unsigned expand_size );
    ~CircleBuffer( void );

    // reads or writes at least one byte, may expand the buffer 
    int read( const int fd, unsigned& original_read_pos ) { return read( fd, &original_read_pos ); }
    int read( const int fd ) { return read( fd, NULL ); }
    // write should only be called if write_space() returns > 0
    int write( const int fd );
    int write( CircleBuffer& dest, unsigned length, bool transfer_data=false );

    // amount of available space
    unsigned read_space( void ) const { return space( read_pos, write_pos, true ); }
    unsigned write_space( void ) const { return space( write_pos, read_pos, false ); }
    
    // does not deallocate memory, simply resets i/o positions
    void reset( void );
    // resets i/o positions then sets provided data to the write space
    void reset( const uint8_t* const data, unsigned size );
    void reset( const string& strdata );

    // return the character at circular position
    unsigned char& operator[]( const unsigned pos )
    {
      return buffer[pos%currsize];
    }

    // need this for my priority queue
    unsigned memory_space( void ) const { return currsize; }

    // append ability
    CircleBuffer& append( const uint8_t* data, unsigned size );
    CircleBuffer& append( const char* data, unsigned size ) { return append( reinterpret_cast<const uint8_t*>(data), size ); }
    CircleBuffer& append( const string& strdata ) { return append( strdata.c_str(), strdata.size() ); }
    CircleBuffer& operator+=( const string& strdata ) { return append( strdata ); }

    // allocate a uint8_t[] and copy the write space into it, allocates write_space() + xtra_size bytes 
    // and places total into new_size
    uint8_t* new_write_space( unsigned& new_size, const unsigned xtra_size );

    // skip the write location ahead - returns old write position
    unsigned write_skip( unsigned count ) { unsigned old = write_pos; write_pos = (write_pos+count)%currsize; return old; }

    const unsigned& total_read;
    const unsigned& total_written;

    const unsigned& current_read_pos;
    const unsigned& current_write_pos;
  private:
    unsigned read_pos, write_pos;
    uint8_t* buffer;
    unsigned expand_size;
    unsigned currsize;

    unsigned tread;
    unsigned twrite;

    void init_logic( const unsigned init_size, const unsigned expand_size_ );

    bool expand( void );
    unsigned space( const unsigned from, const unsigned to, const bool read_space ) const ;
    struct vector_space {
      void operator()( const CircleBuffer* const cb, const unsigned from, const unsigned to,
          bool read_space );
      int read( const int fd ) { return ::readv( fd, v, vcount ); }
      int write( const int fd ) { return ::writev( fd, v, vcount ); }
      size_t vcount;
      struct iovec v[2];
      int total;
#if defined(SSFGWD_DEBUG)
      void debug( void ) const;
#endif
    }; 
    friend struct CircleBuffer::vector_space;

    int read( const int fd, unsigned* original_read_pos );
};



#endif

