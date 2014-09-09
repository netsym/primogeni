/**
 * \file tun.cc
 * \brief The source code for openning the tunnel device on various
 * machine platforms.
 */

#include "primex_config.h"

#include "ssf.h"

// to get rid of the warnings...
#undef PACKAGE_NAME
#undef PACKAGE_STRING
#undef PACKAGE_TARNAME
#undef PACKAGE_VERSION

#if defined(PRIME_SSF_MACH_X86_LINUX) || defined(PRIME_SSF_MACH_X86_64_LINUX) || defined(PRIME_SSF_MACH_X86_CYGWIN)
#define TUNDEF_LINUX
#elif defined(PRIME_SSF_MACH_POWERPC_DARWIN) || defined(PRIME_SSF_MACH_X86_DARWIN)
#define TUNDEF_GENERIC
#elif defined(PRIME_SSF_MACH_POWERPC_FREEBSD)
#define TUNDEF_FREEBSD
#elif defined(PRIME_SSF_MACH_X86_LINUX_OPENBSD)
#define TUNDEF_OPENBSD
#else
//#define TUNDEF_GENERIC 
//XXX this shouldn't get here but it is! I am forcing it to be linux for now
#define TUNDEF_LINUX
#error "what happened.. invalid arch"
#endif

#ifdef TUNDEF_LINUX

#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <syslog.h>
#include <errno.h>

#include <sys/ioctl.h>
#include <sys/socket.h>
#include <linux/if.h>

int tun_open_old(char *dev)
{
  char tunname[14];
  int i, fd;

  if( *dev ) {
    sprintf(tunname, "/dev/%s", dev);
    return open(tunname, O_RDWR);
  }

  for(i=0; i < 255; i++) {
    sprintf(tunname, "/dev/tun%d", i);
    /* Open device */
    if( (fd=open(tunname, O_RDWR)) > 0 ){
      sprintf(dev, "tun%d", i);
      return fd;
    }
  }
  return -1;
}

#ifdef HAVE_LINUX_IF_TUN_H /* New driver support */
#include <linux/if_tun.h>

/* pre 2.4.6 compatibility */
#define OTUNSETNOCSUM  (('T'<< 8) | 200) 
#define OTUNSETDEBUG   (('T'<< 8) | 201) 
#define OTUNSETIFF     (('T'<< 8) | 202) 
#define OTUNSETPERSIST (('T'<< 8) | 203) 
#define OTUNSETOWNER   (('T'<< 8) | 204)

int tun_open(char *dev)
{
  struct ifreq ifr;
  int fd;

  if ((fd = open("/dev/net/tun", O_RDWR)) < 0)
    return tun_open_old(dev);

  memset(&ifr, 0, sizeof(ifr));
  ifr.ifr_flags = IFF_TUN | IFF_NO_PI;
  if (*dev)
    strncpy(ifr.ifr_name, dev, IFNAMSIZ);

  if (ioctl(fd, TUNSETIFF, (void *) &ifr) < 0) {
    if (errno == EBADFD) {
      /* Try old ioctl */
      if (ioctl(fd, OTUNSETIFF, (void *) &ifr) < 0) 
	goto failed;
    } else
      goto failed;
  } 

  strcpy(dev, ifr.ifr_name);
  return fd;

 failed:
  close(fd);
  return -1;
}

#else
int tun_open(char *dev)
{
  return tun_open_old(dev);
}
#endif /* New driver support */

#endif /*TUNDEF_LINUX*/

#ifdef TUNDEF_FREEBSD

#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <syslog.h>

#include <sys/ioctl.h>
#include <net/if_tun.h>

int tun_open(char *dev)
{
  char tunname[14];
  int i, fd = -1;

  if( *dev ){
    sprintf(tunname, "/dev/%s", dev);
    fd = open(tunname, O_RDWR);
  } else {
    for(i=0; i < 255; i++){
      sprintf(tunname, "/dev/tun%d", i);
      /* Open device */
      if( (fd=open(tunname, O_RDWR)) > 0 ){
	sprintf(dev, "tun%d", i);
	break;
      }
    }
  }
  if( fd > -1 ){
    i=0;
    /* Disable extended modes */
    ioctl(fd, TUNSLMODE, &i);	
    ioctl(fd, TUNSIFHEAD, &i);
  }	
  return fd;
}

#endif /*TUNDEF_FREEBSD*/

#ifdef TUNDEF_OPENBSD

#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <syslog.h>

#include <sys/types.h>
#include <sys/uio.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <net/if_tun.h>

int tun_open(char *dev)
{
  char tunname[14];
  int i, fd = -1;

  if( *dev ){
    sprintf(tunname, "/dev/%s", dev);
    fd = open(tunname, O_RDWR);
  } else {
    for(i=0; i < 255; i++){
      sprintf(tunname, "/dev/tun%d", i);
      /* Open device */
      if( (fd=open(tunname, O_RDWR)) > 0 ){
	sprintf(dev, "tun%d", i);
	break;
      }
    }
  }
  return fd;
}

#endif /*TUNDEF_OPENBSD*/

#ifdef TUNDEF_GENERIC

#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <syslog.h>

int tun_open(char *dev)
{
  char tunname[14];
  int i, fd;

  if( *dev ) {
    sprintf(tunname, "/dev/%s", dev);
    return open(tunname, O_RDWR);
  }

  for(i=0; i < 255; i++){
    sprintf(tunname, "/dev/tun%d", i);
    /* Open device */
    if( (fd=open(tunname, O_RDWR)) > 0 ){
      sprintf(dev, "tun%d", i);
      return fd;
    }
  }
  return -1;
}

#endif /*TUNDEF_GENERIC*/
