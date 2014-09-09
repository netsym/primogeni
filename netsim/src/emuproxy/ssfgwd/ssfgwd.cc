/**
 * ssfgwd main() routine
 */


#include <sys/ioctl.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <limits.h>
#ifdef SSFNET_EMU_TIMESYNC
#include <sys/time.h>
#endif

#include <inttypes.h>
#include <fcntl.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <fstream>
using std::ifstream;
#include <iostream>
using std::cerr;
using std::endl;
#include <utility>
using std::pair;

//#include "extra/getopt.h"

#include "debug.h"
#include "fd.h"
#include "tun.h"
#include "ssfgwd_error.h"
#include "listener.h"
#include "ssfgwd_route.h"
#include "ipframe_utils.h"

#define SSFGWD_TUNUP_EXEC "SSFGWD_TUNUP_EXEC"
#define SSFGWD_TUNUP_REPLACE "tunX"

void usage( bool error=false ) __attribute__ (( noreturn ));
void usage( bool error )
{
  static const char* u = \
    "usage:  ssfgwd [-h|--help]\n"
    "        ssfgwd [-a] [-f] [--device|-d TUN_DEVICE] [--port|-p LISTEN_PORT] [--logfile|-l LOGFILE_PATH]\n" 
    "                 [--backlog|-b CONNECTION_BACKLOG ] [--tunup-exec CMD]\n"
    "          -a:  run in attached mode from VPN server (default=unattached)\n"
    "          -f:  run in foreground (default=daemonized, cannot use stdout for logging)\n"
    "          -d:  tunnel device (default=none)\n"
    "          -p:  server port to listen on (default=36685)\n"
    "          -l:  logfile (default=none)\n"
    "          -b:  backlog connections (default=12)\n"
    "          -c:  run server for client test mode connections (default=disabled)\n"
    "          --tunup-exec:  run this command with /bin/sh after the tunnel device has been opened\n"
    "                     and configured.  The command will have all occurances of '" SSFGWD_TUNUP_REPLACE "'\n"
    "                     replaced with the configured tunnel device name. (default=none)\n"
    "\n"
    "                     This parameter may also be set through the environment with " SSFGWD_TUNUP_EXEC "\n"
    "\n"
    "        ssfgwd --client SERVER_IP [any server options above]\n"
    "          --client:  be the client in test mode.  Connect to SERVER_IP as a default\n" 
    "                     gateway (report remote address 0.0.0.0).\n"
    "\n"
    "        ssfgwd {--loop,-L} [any server options above (-p, -b have no affect)]\n"
    "          --loop:    be a loopback device, any frame received from the tunnel is\n"
    "                     immediately shunted back through the tunnel.\n" 
    "\n"
    "        ssfgwd {--loop-through-inbound,-I} ROUTES_FILE [any server options above (-p, -b have no affect)]\n"
    "          --loop-through-inbound: reflect packets to the same EmuProxy connection as if they came from \n"
    "                     the OpenVPN connection or tunnel device using the routes file specified.\n"
    "\n"
    "                     Each line in the the route file contains a destination address followed by one or \n"
    "                     more address, delimited by spaces or tabs, that should be routed to the destination.\n"
    "                     Example:\n"
    "                       10.0.0.1 10.0.0.2\n"
    "                       10.0.0.2 10.0.0.1\n"
    "\n"
    ;

  cerr << u << endl;
  exit( !error );
}


struct runparams {
  runparams( void ) 
    // I'll buy anyone a beer that can say why 36685 is the obvious
    // port number
    : attached(false), foreground(false), client(false), 
      client_test_mode(false), loop_mode(false), loop_at(Route::NOLOOP),
      port(36685), backlog(12), logfile(), device(), tunup_exec()
    {
      char* ev = getenv( SSFGWD_TUNUP_EXEC );
      if( ev ) {
        tunup_exec = ev;
      }
    }
  unsigned attached :1;
  unsigned foreground :1;
  unsigned client:1;
  unsigned client_test_mode:1;
  unsigned loop_mode:1;
  enum Route::LOOP_AT loop_at;
  struct sockaddr_in server_addr;
  uint16_t port;
  uint16_t backlog;
  string logfile;
  Route::ReflectorSet routerIps;
  string device;
  string tunup_exec;
};

void parseReflectorRoutes(const char* file, runparams& rp);

/*
static const struct option lopts[] = {
  { "attach", 0, 0, 'a' },
  { "port", 1, 0, 'p' },
  { "backlog", 1, 0, 'b' },
  { "logfile", 1, 0, 'l' },
  { "device", 1, 0, 'd' },
  { "help", 0, 0, 'h' },
  { "client", 1, 0, 'C' },
  { "tunup-exec", 1, 0, 'P' },
  { "loop", 0, 0, 'L' },
  { "loop-through-inbound", 1, 0, 'I' },
  { 0, 0, 0, 0 }};

extern char* optarg;
extern int optind;
*/

void parse_argv( struct runparams& rp, int argc, char* argv[] )
{
  for(int i=1; i<argc; i++) {
    if(!strcmp(argv[i], "-a") || !strcmp(argv[i], "-attach")) {
      rp.attached = true;
    } else if(!strcmp(argv[i], "-p") || !strcmp(argv[i], "-port")) {
      if(++i >= argc) usage();
      unsigned long u = strtoul( argv[i], (char**)NULL, 0 );
      if(( errno == ERANGE ) || !u || ( u > USHRT_MAX )) usage();
      rp.port = u & 0xffff;
    } else if(!strcmp(argv[i], "-b") || !strcmp(argv[i], "-backlog")) {
      if(++i >= argc) usage();
      unsigned long u = strtoul( argv[i], (char**)NULL, 0 );
      if(( errno == ERANGE ) || !u || ( u > INT_MAX )) usage();
      rp.backlog = u & 0xffff;
    } else if(!strcmp(argv[i], "-l") || !strcmp(argv[i], "-logfile")) {
      if(++i >= argc) usage();
      rp.logfile = string( argv[i] );
    } else if(!strcmp(argv[i], "-d") || !strcmp(argv[i], "-device")) {
      if(++i >= argc) usage();
      rp.device = string( argv[i] );
    } else if(!strcmp(argv[i], "-h") || !strcmp(argv[i], "-help")) {
      usage(true);
    } else if(!strcmp(argv[i], "-C") || !strcmp(argv[i], "-client")) {
      if(++i >= argc) usage();
      inet_pton( AF_INET, argv[i], &rp.server_addr.sin_addr );
      rp.loop_mode = false;
      rp.client = true;
      rp.client_test_mode = true;
    } else if(!strcmp(argv[i], "-P") || !strcmp(argv[i], "-tunup-exec")) {
      if(++i >= argc) usage();
      rp.tunup_exec = argv[i];
    } else if(!strcmp(argv[i], "-L") || !strcmp(argv[i], "-loop")) {
      rp.client = rp.client_test_mode = false;
      rp.loop_mode = true;
    } else if(!strcmp(argv[i], "-I") || !strcmp(argv[i], "-loop-through-inbound")) {
      if(++i >= argc) usage();
      rp.client = rp.client_test_mode = false;
      rp.loop_at = Route::LOOP_THROUGH_INBOUND;
      rp.attached = true;
      parseReflectorRoutes(argv[i], rp);
    } else if(!strcmp(argv[i], "-c")) {
      rp.loop_mode = false;
      rp.client_test_mode = true;
    } else usage();
  }

#if 0
  while(1) {
    int c, option_index = 0;
  
    c = getopt_long( argc, argv, "ap:b:l:d:hfcC:P:LI:", lopts, &option_index );
    switch( c ) {
      case -1 :
        if( optind == argc ) return;
        // fall through, too many cl params provided
      case '?' :
        usage(); // does not return
        break;
      case 'h' :
        usage( true );
        break;
      case 'a':
	rp.attached = true;
	break;
      case 'f' :
        rp.foreground = true;
        break;
      case 'p' :
        {
          unsigned long u = strtoul( optarg, (char**)NULL, 0 );
          if(( errno == ERANGE ) || !u || ( u > USHRT_MAX )) usage();
          rp.port = u & 0xffff;
        }
        break;
      case 'b' :
        {
          unsigned long u = strtoul( optarg, (char**)NULL, 0 );
          if(( errno == ERANGE ) || !u || ( u > INT_MAX )) usage();
          rp.backlog = u & 0xffff;
        }
        break;
      case 'l' :
        // logfile name, support '-' as stdout
        rp.logfile = string( optarg );
        break;
      case 'd' :
        // device
        rp.device = string( optarg );
        break;
      case 'P' :
        // post tunnel exec command
        rp.tunup_exec = optarg;
        break;

      // connect as client for testing
      case 'C' :
        // serverip
        inet_pton( AF_INET, optarg, &rp.server_addr.sin_addr );
        rp.loop_mode = false;
        rp.client = true;
        rp.client_test_mode = true;
        break;

      case 'c' :
        // run server in for client test connections
        rp.loop_mode = false;
        rp.client_test_mode = true;
        break;

      // be a loopback
      case 'L' :
        rp.client = rp.client_test_mode = false;
        rp.loop_mode = true;
        break;

      case 'I' :
        rp.client = rp.client_test_mode = false;
        rp.loop_at = Route::LOOP_THROUGH_INBOUND;
	rp.attached = true;
        parseReflectorRoutes(optarg, rp);
        break;
    }
  }
#endif
}


// open the tunnel
static int open_tunnel( const runparams& rp, FILE* const logfp )
{
  char devname[64]; // should be big enough
  if(rp.device.empty()) devname[0] = 0;
  else { strncpy(devname, rp.device.c_str(), 63); devname[63] = 0; }

  int r = tun_open(devname);
  if( r < 0 ) {
    perrorf( errno, "open tunnel device (%s)", devname );
    exit(1);
  }

  const char* dname = rindex(devname, '/');
  if(dname) dname++;
  else dname = devname;

  if( o_nonblock( r ) < 0 ) {
    errorf( "tunnel file descriptor cannot be made non-blocking.\n" );
    close(r);
    exit(1);
  }
  if(logfp) { fprintf( logfp, "using '%s' tunnel device fd %d\n", devname, r ); fflush(logfp); }
  if( rp.tunup_exec.size() ) {
    const string::size_type rl( sizeof( SSFGWD_TUNUP_REPLACE )-1 );
    string e(rp.tunup_exec);
    string::size_type p(0);
    while(( p=e.find( SSFGWD_TUNUP_REPLACE, p)) < e.size() ) {
      e.replace( p, rl, dname );
      p+=rl;
    }
    
    if(logfp) { fprintf( logfp, "tunnel up: %s\n", e.c_str() ); fflush(logfp); }
    int rv = system( e.c_str());
    if( rv ) {
      errorf( "system( tunup_exec ) returned exit code %d\n", rv );
      exit(1);
    }
  }

  return r;  
}

int main( int argc, char* argv[] )
{
  runparams rp;
  // parse commandline
  parse_argv( rp, argc, argv );

  // output log - forgive me for using plain vanilla C flavor FILE
  FILE* logfp( NULL );
  if( !rp.logfile.empty()) {
    if ( rp.logfile == "-" ) {
      if(rp.attached) 
	errorf_exit( "attached mode does not support logging to stdout\n" );
      // stdout
      D( "logging to stdout\n" );  
      logfp = stdout;
    } else {
      logfp = fopen( rp.logfile.c_str(), "a+" ); // append
      if( !logfp ) {
	errorf_exit( "could not open logfile '%s'\n", rp.logfile.c_str() );
      }
      D( "logging to '%s'\n", rp.logfile.c_str() );  
    }
  }

  // open the tunnel
  int outfd, infd;
  if(rp.attached) {
    if(logfp) { fprintf( logfp, "running attached mode\n" ); fflush(logfp); }
    outfd = 1; infd = 0; 
  } else { 
    if(logfp) { fprintf( logfp, "running unattached mode\n" ); fflush(logfp); }
    outfd = infd = open_tunnel( rp, logfp ); 
  }

  // ignore all SIGPIPEs, they will kill our daemon but if we ignore them, they
  // simply reset one connection
  if( signal( SIGPIPE, SIG_IGN ) == SIG_ERR ) {
    errorf( "unable to ignore SIGPIPE, ssfgwd may not be stable under heavy strain\n" );
  }

  if( !rp.attached && !rp.foreground ) {
    daemon(0,0);
  }

#if 0
  // redirect stdout and stderr to logfp
  if( fileno(logfp) != fileno(stdout) ) {
    dup2( fileno(logfp), fileno(stdout) );
  }
  if( fileno(logfp) != fileno(stderr) ) {
    dup2( fileno(logfp), fileno(stderr) );
  }
#endif

  // a set of clients who have not finished setting up yet
  NewRoute::set setup_clients;  
  // a mapping of established reader or reader+writer clients who have finished setting up
  // - indexed by network byte order sim address
  Route::map est_clients;  
  // a set of writer clients
  Route::set est_writers;

  // a buffer for outbound ip packets (from ioinjectors to tunnel device)
  // initmem = 512K  expand=64K
  CircleBuffer outbound_buf( 1<<19, 1<<16 );
  // from tunnel device to ioinjectors
  CircleBuffer inbound_buf( 1<<19, 1<<16 );

  // open a tcp listener on the port
  Listener jeeves( rp.port, rp.backlog );

  // tracks whether stats should be logged
  bool log_stats = true;
  unsigned client_connections = 0;
  unsigned ip_frames_routed = 0;
  unsigned ip_frames_dropped = 0;

#ifndef LOOP_TEST_MODE
  if( rp.loop_mode  ) {
    errorf( "loop mode support is not compiled into this vesion.\n" );
    exit(1);
  }  
#endif
#ifndef CLIENT_TEST_MODE
  if( rp.client || rp.client_test_mode ) {
    errorf( "client test mode support is not compiled into this vesion.\n" );
    exit(1);
  }
#else
  // sneak a connection in for client mode
  if( rp.client ) {
    // client mode
    rp.client_test_mode = true;

    rp.server_addr.sin_family = AF_INET;
    rp.server_addr.sin_port = htons( rp.port );

    // connect to server and tell the server we are a gateway that
    // uses only one i/o socket
    Listener::connection c(rp.server_addr);
    uint8_t both = NewRoute::BOTH;
    if( write( c.connsock, &both, sizeof(both)) != sizeof(both) ) {
      perror( "write(BOTH) to server" );
      exit(1);
    }
    
    // send two addresses down the line for testing purposes
    uint32_t client_ip = htonl(0xc0a80002); 
    if( write( c.connsock, &client_ip, sizeof(client_ip)) != sizeof(client_ip) ) {
      perror( "write(192.168.0.2) to server" );
      exit(1);
    }
#if 0
    // FOR TESTING - a reader+writer can't send two different addresses
    client_ip = htonl(0xc0a80003);
    if( write( c.connsock, &client_ip, sizeof(client_ip)) != sizeof(client_ip) ) {
      perror( "write(192.168.0.3) to server" );
      exit(1);
    }
#endif
    // and terminate...
    client_ip = htonl(0x0);
    if( write( c.connsock, &client_ip, sizeof(client_ip)) != sizeof(client_ip) ) {
      perror( "write(0.0.0.0) to server" );
      exit(1);
    }

    NewRoute sr(c, logfp);
    // tell it the peer (the server) is 192.168.0.1
    uint32_t server_ip = htonl(0xc0a80001);
    sr.skip_setup(NewRoute::BOTH, server_ip );  
  
    // a new route
    Route* r = new Route( sr, rp.attached, logfp, rp.client_test_mode, rp.loop_at );

    // remember this route
    est_clients[r->ip_list[0].ip] = r;
    client_connections++;
    if(logfp) { fprintf( logfp, "client mode to server established\n" ); fflush(logfp); }
  }
#endif

  SelectCall sc( true ); 
  while( true ) {
    sc.reinit( true );

    jeeves.select_set( sc );

    // we always want ipframes from the tunnel device
    sc.select_on_r( infd );

    for( NewRoute::set::iterator i=setup_clients.begin(); i!=setup_clients.end(); i++ ) {
      // every connection still being setup probably wants to read
      (*i)->select_set_r( sc );
    }

    // do we have ipframes that are writeable to the tunnel?
    if( outbound_buf.write_space() ) {
      sc.select_on_w( outfd );
    }

    for( Route::map::iterator i=est_clients.begin(); i!=est_clients.end(); i++ ) {
      Route* r((*i).second);
      // established connections want to read and write
      r->select_set_r( sc );
      r->select_set_w( sc );
    }

    for( Route::set::iterator i=est_writers.begin(); i!=est_writers.end(); i++ ) {
      // established writer connections only want to read
      (*i)->select_set_r( sc );
    }

    // inside of this is the select() call, return after 10 seconds if there is no 
    // activity and we need to log statistics
    int scr = sc( log_stats ? 10*1000 : 0 );
    log_stats = true;  
    switch( scr ) {
      case -1 :
        // error
        perror( "select()" );
        break;
      case 0 :
        // timeout
        
        // don't show anymore till we see activity
        log_stats = false;

        if(logfp) {
	  fprintf( logfp, "stats: client connections %u  frames routed %u  frames dropped %u\n",
		   client_connections, ip_frames_routed, ip_frames_dropped );
	  fprintf( logfp, "stats: %u clients setting up\n", (unsigned)setup_clients.size() );
	  fprintf( logfp, "stats: %u clients established\n", (unsigned)est_clients.size() );
	  fflush(logfp);
	}

        {
          std::set<Route*> stat_done;  // print the stats for a route out only one time
          for( Route::map::iterator i=est_clients.begin(); i!=est_clients.end(); i++ ) {
            Route* r((*i).second);
            if( stat_done.find(r) != stat_done.end() ) {
              continue;
            }
            // established connections want to read and write
            if(logfp) {
	      fprintf( logfp, " stats: %s\n", r->description().c_str() );
	      fprintf( logfp, "  stats: %s\n", r->stats(Route::STATS_TO).c_str() );
	      fprintf( logfp, "  stats: %s\n", r->stats(Route::STATS_FROM).c_str() );
	      fflush(logfp);
	    }
            stat_done.insert( r );
          }
        }
        // my outbound buffer
	if(logfp) {
	  fprintf( logfp, "stats: outbound buffer %u total_bytes, %u bytes from ssf, %u bytes to tunnel, %u write_space\n", 
		   outbound_buf.memory_space(), outbound_buf.total_read, outbound_buf.total_written, outbound_buf.write_space() );
	  fprintf( logfp, "stats: inbound buffer %u total_bytes, %u bytes from tunnel, %u bytes to ssf, %u write_space\n", 
		   inbound_buf.memory_space(), inbound_buf.total_read, inbound_buf.total_written, inbound_buf.write_space() );
	  fflush(logfp);
	}
        break; //end -1
      default :
        // fd activity

        // iterate through all the established reader or reader+writer connections 
        // since one call to erase() may remove multiple elements from the map and we
        // are currently iterating, we maintain a set of vector of pairs that should be removed
        // when all the iteration is finished.
        std::set<void*> to_be_removed;
	std::set<void*>::iterator p;
        bool route_error;
        for( Route::map::iterator miter=est_clients.begin(); miter!=est_clients.end(); miter++ ) {
          bool route_error = false;
          (*miter).second->select_isset( sc, outbound_buf, inbound_buf, route_error );

          if( route_error ) {
            // route had an io error - remember to remove it
            to_be_removed.insert( (void*)(*miter).second );
          }
        }

        // now that we are done iterating, it is safe to remove the elements that had io errors
        for( p=to_be_removed.begin(); p!=to_be_removed.end(); p++ ) {
	  est_clients.erase( (Route*)(*p) );
          delete (Route*)(*p);
        }
	to_be_removed.clear();

        // iterate through all the writer connections
        for( Route::set::iterator siter=est_writers.begin(); siter!=est_writers.end(); siter++) {
          bool route_error = false;
          (*siter)->select_isset( sc, outbound_buf, inbound_buf, route_error );

          if( route_error ) {
            // route had an io error - remember to remove it
            to_be_removed.insert( (void*)(*siter) );
	  }
	}

        // now that we are done iterating, it is safe to remove the elements that had io errors
        for( p=to_be_removed.begin(); p!=to_be_removed.end(); p++ ) {
          est_writers.erase( (Route*)(*p) );
          delete (Route*)(*p);
        }
	to_be_removed.clear();

        // write as much as we can from the outbound_buf to the tunnel
        if( sc.is_set_w( outfd ) && outbound_buf.write_space()) {
          outbound_buf.write( outfd );
        }

        // iterate through the clients that are setting up
        for( NewRoute::set::iterator niter=setup_clients.begin(); niter!=setup_clients.end(); niter++) {
          route_error = false;
          if( (*niter)->select_isset( sc, route_error )) {
            // allocate a new route
            try {

              Route* p( new Route( **niter, rp.attached, logfp, rp.client_test_mode, rp.loop_at, rp.routerIps));

              // what type is it?
              switch( (*niter)->setup_type ) {
                case NewRoute::WRITER :
                  // simply add to our writer set
                  est_writers.insert( p );
                  if(logfp) { fprintf( logfp, "%s inserted into established writers set with %s\n", p->description().c_str(),
                      (*niter)->description().c_str() ); fflush(logfp); }
                  break;

                case NewRoute::BOTH :
                case NewRoute::READER :
                  // remove pre-existing entries
                  for( NewRoute::list::const_iterator a=p->ip_list.begin(); a!=p->ip_list.end(); a++ ) {
                    Route::map::iterator f( est_clients.find( a->ip ));
                    if( f != est_clients.end() ) {
                      Route* to_delete = f->second;
                      if(logfp) { fprintf( logfp, "%s removed from established client set due to %s\n", to_delete->description().c_str(),
                           (*niter)->description().c_str() ); fflush(logfp); }
                      est_clients.erase( to_delete );
                      delete to_delete;
                    }
                  }
                  // install the new route against all of its ip addresses
                  for( NewRoute::list::const_iterator j=p->ip_list.begin(); j!= p->ip_list.end(); j++ ) {
                    est_clients[j->ip] = p;
                  }
                  if(logfp) { fprintf( logfp, "%s inserted into established client set from %s\n", p->description().c_str(), 
                      (*niter)->description().c_str() ); fflush(logfp); }
                  break;
              }

            } catch( std::bad_alloc e ) {
              errorf( "allocation error handling new route insertion\n" );
            }

            // remove the setup connection object
            to_be_removed.insert( (void*)(*niter) );

          } else if( route_error ) {
            // route had an io error, remove from set
            to_be_removed.insert( (void*)(*niter) );
          }
        }

        // now that we are done iterating, it is safe to remove the elements that had io errors
        for( p=to_be_removed.begin(); p!=to_be_removed.end(); p++ ) {
          setup_clients.erase( (NewRoute*)(*p) );
          delete (NewRoute*)(*p);
        }
	to_be_removed.clear();

        // read packets from the tunnel device
        if( sc.is_set_r( infd ) && inbound_buf.read( infd ) < 0 ) {
          perrorf( errno, "read( %s )", rp.device.c_str() );
        }

        // for each ipframe we find, place it into the proper outbound
        // route buffer
        {
          ip_frame_info  to_frame(0,0);
          unsigned len;
          while(( len = ipframe_find( inbound_buf, to_frame, rp.attached )) > 0 ) {

#ifdef LOOP_TEST_MODE
            if( rp.loop_mode ) {
              // shove it right back out (after changing the lead addr to dst addr if attached)
	      if(rp.attached) {
		unsigned first_byte_pos = inbound_buf.current_write_pos + to_frame.PRE_PADDING;
		uint8_t* p = reinterpret_cast<uint8_t*>(&to_frame.daddr.s_addr);
		inbound_buf[first_byte_pos]= *p++;
		inbound_buf[first_byte_pos+1] = *p++;
		inbound_buf[first_byte_pos+2] = *p++;
		inbound_buf[first_byte_pos+3] = *p;
	      } 
              inbound_buf.write( outbound_buf, len, true );
	      if(logfp) { fprintf( logfp, "loop: %s\n", to_frame.describe().c_str() ); fflush(logfp); }
	      ip_frames_routed++;
            } else 
#endif  
            {
              // found a to_frame, do we have an ioinjector with the same source ip?
              Route::map::iterator i;
	      if(rp.attached) i = est_clients.find( to_frame.leadaddr.s_addr );
	      else i = est_clients.find( to_frame.saddr.s_addr );
#ifdef CLIENT_TEST_MODE
              if( (i == est_clients.end()) && rp.client_test_mode ) {
                // force the packet out the first connection
                i = est_clients.begin();
              }
#endif
              if( i == est_clients.end() ) {
                if(logfp) { fprintf( logfp, "drop %s\n", to_frame.describe().c_str() ); fflush(logfp); }
                inbound_buf.write_skip( len );
                ip_frames_dropped++;
              } else {
                // found an outbound route

                // write the length, no more!
		/*
                if( !rp.client_test_mode ) {
                  // in ctm we don't write a length
                  uint16_t nbo_len = htons(len);
                  (*i).second->to_buf->append( reinterpret_cast<uint8_t*>(&nbo_len), sizeof( nbo_len ));
                }
		*/

                // shove it into the correct buffer
                inbound_buf.write( *(*i).second->to_buf, len, true );
                if(logfp) { fprintf( logfp, "to_ssf for %s: %s\n",
                    (*i).second->description().c_str(), to_frame.describe().c_str() ); fflush(logfp); }

#ifdef SSFNET_EMU_TIMESYNC
		// write the timestamp
                if( !rp.client_test_mode ) {
		  struct timeval t;
		  gettimeofday(&t, 0);
		  int tv[2];
		  tv[0] = htonl((int)t.tv_sec);
		  tv[1] = htonl((int)t.tv_usec);
                  (*i).second->to_buf->append( (char*)tv, sizeof(tv));
		}
#endif

                ip_frames_routed++;
              }
            }
          }
        }

        // and finally, handle new connections
        Listener::connection c;
        int e = jeeves.select_isset( sc, c );
        if( e < 0 ) {
          // error
          perror( "accept()" );
        } else if( e > 0 ) {
          // place the new connection into the setup set
          NewRoute* p(0);
          try {
#ifdef LOOP_TEST_MODE
            if( rp.loop_mode ) {
              if(logfp) { fprintf( logfp, "loop: ignore connection on descriptor %d\n", c.connsock ); fflush(logfp); }
            } else 
#endif
            {
              p = new NewRoute( c, logfp );
              setup_clients.insert( p );
              client_connections++;
            }
          } catch( std::bad_alloc e ) {
            errorf( "allocation error for new connection on %d\n", c.connsock);
            close(c.connsock);
            delete(p);
          }
        }
    } // end of switch
  }  
}

void parseReflectorRoutes(const char* file, runparams& rp) {
  ifstream in ( file );
  if ( in.is_open() ) {
    struct in_addr test;
    string dst;
    string line;
    int line_count = 1;
    string delims = " \t\n";
    while ( getline ( in, line ) ) {
      int count = 0;
	  bzero(&test, sizeof(struct in_addr));      
      const char* addr = line.c_str();
 	  if(0==inet_aton(addr,&test)) {
            fprintf(stderr, "\nErrors while parsing route file:\nFrom '%s' on line %i, found an invalid address '%s'!\n", file, line_count, addr);
            exit(1);
      }
      rp.routerIps.insert(string(addr));
	  count++;
      line_count++;
    }
    for(Route::ReflectorSet::iterator i = rp.routerIps.begin(); i!=rp.routerIps.end();i++) {
      printf(" Found Ip: %s\n",i->c_str());
    }

  } else {
    fprintf(stderr, "Errors while parsing route file:\nUnable to open '%s'\n", file);
    exit(1);
  }
  in.close();
}
