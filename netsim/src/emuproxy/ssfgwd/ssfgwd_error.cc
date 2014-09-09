/**
 * ssfgwd error utilities
 */

#include <stdarg.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>

static void error_fmt( const char* const fmt, va_list& ap )
{
  vfprintf( stderr, fmt, ap );
}

void errorf( const char* const fmt, ... )
{
  va_list ap;
  va_start( ap, fmt );
  error_fmt( fmt, ap );
  va_end( ap );
}

void errorf_exit( const char* const fmt, ... )
{
  va_list ap;
  va_start( ap, fmt );
  error_fmt( fmt, ap );
  va_end( ap );

  exit( 1 );
}

void perrorf( const int err, const char* const fmt, ... )
{
  va_list ap;
  va_start( ap, fmt );
  error_fmt( fmt, ap );
  va_end( ap );
  errorf( ": %s\n", strerror( err ));
}

void perrorf_exit( const int err, const char* const fmt, ... )
{
  va_list ap;
  va_start( ap, fmt );
  error_fmt( fmt, ap );
  va_end( ap );
  errorf_exit( ": %s\n", strerror( err ));
}
