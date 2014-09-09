/*---------------------------------------------------------------------------*/
/* Author(s): Kalyan Perumalla <http://www.cc.gatech.edu/~kalyan> 02Oct2003  */
/* $Revision: 1.1.1.1 $ $Name:  $ $Date: 2004/07/21 21:01:31 $ */
/*---------------------------------------------------------------------------*/
#ifndef __MYCOMPAT_H
#define __MYCOMPAT_H

/*---------------------------------------------------------------------------*/
#if defined(_WIN32) || defined(_WIN32_WCE)
    #define PLATFORM_WIN 1
#endif

#ifdef _WIN32_WCE
    #define PLATFORM_WINCE 1
#endif

/*---------------------------------------------------------------------------*/
#if PLATFORM_WIN
    #if !PLATFORM_WINCE
        #include <time.h>
        #define sleep(_s) \
	    do{clock_t c=clock()+(_s)*1000;while(c>clock()){}}while(0)
    #else
        #include <winbase.h>
        #define time(_pt)     (*(_pt)=0)
        #define sleep(_s)     Sleep((_s)*1000)
        #define strdup(_s)    strcpy(((char*)malloc(strlen(_s)+1)),_s)
        #define perror(_s)    ((void)0)
        #define system(_s)    ((int)-1)
        char *getenv( const char *s );
        int putenv( char *s );
    #endif

    #include <process.h>
    #define getpid() _getpid()

    #include <winsock.h>
    typedef time_t TIMER_TYPE;
    #define TIMER_NOW(_t) time(&_t)
    #define TIMER_SECONDS(_t) (_t)
    #define SIGPIPE SIGSEGV
    #define SIGHUP SIGSEGV
    #define SHM_AVAILABLE 0
#else
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #include <sys/time.h>
    #include <unistd.h>
    typedef struct timeval TIMER_TYPE;
    #define TIMER_NOW(_t) gettimeofday(&_t,NULL)
    #define TIMER_SECONDS(_t) ((double)(_t).tv_sec + (_t).tv_usec*1e-6)
    typedef char BOOLEAN;
    #define SHM_AVAILABLE 1
#endif

/*---------------------------------------------------------------------------*/
#if PLATFORM_WINCE
    /*Application should define the callback to handle the failed assertion*/
    void assert_callback( const TCHAR *fname, int line, const TCHAR *condition);
    #define MYASSERT( _cond, _act ) \
       do{if( !(_cond) ) { printf _act; printf("\n"); \
          assert_callback( TEXT(__FILE__), __LINE__, TEXT(#_cond) );}}while(0)
    #define assert( _cond ) MYASSERT( _cond, ("Failed assertion") )
#else
    #include <assert.h>
    #define MYASSERT( _cond, _act ) \
       do{if( !(_cond) ) { printf _act; printf("\n"); assert( _cond );}}while(0)
#endif

/*---------------------------------------------------------------------------*/
#endif /* __MYCOMPAT_H */
