
#ifndef DEBUG_H
#define DEBUG_H

#include <stdio.h>
#include <string.h>

#define stddbg stderr
#define STDERR_FORMAT( fmt, args... )	fprintf( stddbg, fmt , ##args )

/* some convenience macros for debug statements
 */
#if defined(SSFGWD_DEBUG) 
#define DMSG( fmt, args... ) \
	STDERR_FORMAT( fmt , ##args )

#define DMSG_ERRNO( fmt, args... ) \
	STDERR_FORMAT( fmt ": %s\n", ##args, strerror(errno) )

#define DMSG_LINE( fmt, args... ) \
	STDERR_FORMAT( "[%d]: " fmt , __LINE__ , ##args )

#define DMSG_FUNC( fmt, args... ) \
	STDERR_FORMAT( "%s(): " fmt , __PRETTY_FUNCTION__ , ##args )

#define DMSG_FUNC_LINE( fmt, args... ) \
	STDERR_FORMAT( "%s()[%d]: " fmt , __PRETTY_FUNCTION__, __LINE__ , ##args )

#define DMSG_FILE_LINE( fmt, args... ) \
	STDERR_FORMAT( "%s[%d]: " fmt , __FILE__, __LINE__ , ##args )

// my favorite default
#define D( fmt, args... ) DMSG_FILE_LINE( fmt , ##args )

#else

#define DMSG( fmt, args... ) 
#define DMSG_ERRNO( fmt, args... )
#define DMSG_LINE( fmt, args... ) 
#define DMSG_FUNC( fmt, args... ) 
#define DMSG_FUNC_LINE( fmt, args... ) 
#define DMSG_FILE_LINE( fmt, args... ) 
#define D( fmt, args... ) 

#endif

// noop version
#define _DMSG( fmt, args... ) 
#define _DMSG_ERRNO( fmt, args... )
#define _DMSG_LINE( fmt, args... ) 
#define _DMSG_FUNC( fmt, args... ) 
#define _DMSG_FUNC_LINE( fmt, args... ) 
#define _DMSG_FILE_LINE( fmt, args... ) 
#define _D( fmt, args... ) 


#endif

