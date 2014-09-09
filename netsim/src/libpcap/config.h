/* config.h.  Generated from config.h.in by configure.  */
/* config.h.in.  Generated from configure.ac by autoheader.  */

/* Enable optimizer debugging */
/* #undef BDEBUG */

/* defined if linux allows shared ring buffer */
#define DO_RING 1

/* defined if linux has 64 bit time_t */
#define HAVE_64BIT_TIME_T 1

/* define if you have the DAG API */
/* #undef HAVE_DAG_API */

/* Define to 1 if you have the declaration of `ether_hostton', and to 0 if you
   don't. */
#define HAVE_DECL_ETHER_HOSTTON 1

/* define if you have a /dev/dlpi */
/* #undef HAVE_DEV_DLPI */

/* Define to 1 if you have the <dirent.h> header file, and it defines `DIR'.
   */
#define HAVE_DIRENT_H 1

/* Define to 1 if you have the <dlfcn.h> header file. */
#define HAVE_DLFCN_H 1

/* Define to 1 if you have the `ether_hostton' function. */
#define HAVE_ETHER_HOSTTON 1

/* on HP-UX 10.20 */
/* #undef HAVE_HPUX10_20 */

/* on HP-UX 9.x */
/* #undef HAVE_HPUX9 */

/* if ppa_info_t_dl_module_id exists */
/* #undef HAVE_HP_PPA_INFO_T_DL_MODULE_ID_1 */

/* Define to 1 if you have the <ifaddrs.h> header file. */
#define HAVE_IFADDRS_H 1

/* Define to 1 if you have the <inttypes.h> header file. */
#define HAVE_INTTYPES_H 1

/* Define to 1 if you have the <limits.h> header file. */
#define HAVE_LIMITS_H 1

/* Define to 1 if you have the <memory.h> header file. */
#define HAVE_MEMORY_H 1

/* Define to 1 if you have the <ndir.h> header file, and it defines `DIR'. */
/* #undef HAVE_NDIR_H */

/* Define to 1 if you have the <netinet/ether.h> header file. */
#define HAVE_NETINET_ETHER_H 1

/* Define to 1 if you have the <netinet/if_ether.h> header file. */
#define HAVE_NETINET_IF_ETHER_H 1

/* if there's an os_proto.h */
/* #undef HAVE_OS_PROTO_H */

/* define if you have a /proc/net/dev */
#define HAVE_PROC_NET_DEV 1

/* define if you have a Septel API */
/* #undef HAVE_SEPTEL_API */

/* Define to 1 if you have the `snprintf' function. */
#define HAVE_SNPRINTF 1

/* if struct sockaddr has sa_len */
/* #undef HAVE_SOCKADDR_SA_LEN */

/* if struct sockaddr_storage exists */
#define HAVE_SOCKADDR_STORAGE 1

/* On solaris */
/* #undef HAVE_SOLARIS */

/* Define to 1 if you have the <stdint.h> header file. */
#define HAVE_STDINT_H 1

/* Define to 1 if you have the <stdlib.h> header file. */
#define HAVE_STDLIB_H 1

/* Define to 1 if you have the `strerror' function. */
#define HAVE_STRERROR 1

/* Define to 1 if you have the <strings.h> header file. */
#define HAVE_STRINGS_H 1

/* Define to 1 if you have the <string.h> header file. */
#define HAVE_STRING_H 1

/* Define to 1 if you have the `strlcpy' function. */
/* #undef HAVE_STRLCPY */

/* Define to 1 if the system has the type `struct ether_addr'. */
/* #undef HAVE_STRUCT_ETHER_ADDR */

/* Define to 1 if `st_rdev' is a member of `struct stat'. */
#define HAVE_STRUCT_STAT_ST_RDEV 1

/* Define to 1 if you have the <sys/bufmod.h> header file. */
/* #undef HAVE_SYS_BUFMOD_H */

/* Define to 1 if you have the <sys/dir.h> header file, and it defines `DIR'.
   */
/* #undef HAVE_SYS_DIR_H */

/* Define to 1 if you have the <sys/dlpi_ext.h> header file. */
/* #undef HAVE_SYS_DLPI_EXT_H */

/* Define to 1 if you have the <sys/ioccom.h> header file. */
/* #undef HAVE_SYS_IOCCOM_H */

/* Define to 1 if you have the <sys/ndir.h> header file, and it defines `DIR'.
   */
/* #undef HAVE_SYS_NDIR_H */

/* Define to 1 if you have the <sys/sockio.h> header file. */
/* #undef HAVE_SYS_SOCKIO_H */

/* Define to 1 if you have the <sys/stat.h> header file. */
#define HAVE_SYS_STAT_H 1

/* Define to 1 if you have the <sys/types.h> header file. */
#define HAVE_SYS_TYPES_H 1

/* if if_packet.h has tpacket_stats defined */
#define HAVE_TPACKET_STATS 1

/* Define to 1 if you have the <unistd.h> header file. */
#define HAVE_UNISTD_H 1

/* Define to 1 if you have the `vsnprintf' function. */
#define HAVE_VSNPRINTF 1

/* define if your compiler has __attribute__ */
/* #undef HAVE___ATTRIBUTE__ */

/* IPv6 */
/* #undef INET6 */

/* if unaligned access fails */
/* #undef LBL_ALIGN */

/* Define to the sub-directory in which libtool stores uninstalled libraries.
   */
#define LT_OBJDIR ".libs/"

/* Define to 1 if `major', `minor', and `makedev' are declared in <mkdev.h>.
   */
/* #undef MAJOR_IN_MKDEV */

/* Define to 1 if `major', `minor', and `makedev' are declared in
   <sysmacros.h>. */
/* #undef MAJOR_IN_SYSMACROS */

/* Define to 1 if netinet/ether.h declares `ether_hostton' */
#define NETINET_ETHER_H_DECLARES_ETHER_HOSTTON /**/

/* Define to 1 if netinet/if_ether.h declares `ether_hostton' */
/* #undef NETINET_IF_ETHER_H_DECLARES_ETHER_HOSTTON */

/* do not use protochain */
/* #undef NO_PROTOCHAIN */

/* Name of package */
#define PACKAGE "libpcap"

/* Define to the address where bug reports for this package should be sent. */
#define PACKAGE_BUGREPORT "cpw@lanl.gov"

/* Define to the full name of this package. */
#define PACKAGE_NAME "libpcap"

/* Define to the full name and version of this package. */
#define PACKAGE_STRING "libpcap 1.9"

/* Define to the one symbol short name of this package. */
#define PACKAGE_TARNAME "libpcap"

/* Define to the home page for this package. */
#define PACKAGE_URL ""

/* Define to the version of this package. */
#define PACKAGE_VERSION "1.9"

/* /dev/dlpi directory */
/* #undef PCAP_DEV_PREFIX */

/* Define as the return type of signal handlers (`int' or `void'). */
#define RETSIGTYPE void

/* defined to collect packet capture statistics */
#define RING_STATS 1

/* Define to 1 if you have the ANSI C header files. */
#define STDC_HEADERS 1

/* Define to 1 if you can safely include both <sys/time.h> and <time.h>. */
#define TIME_WITH_SYS_TIME 1

/* Define to 1 if your <sys/time.h> declares `struct tm'. */
/* #undef TM_IN_SYS_TIME */

/* Version number of package */
#define VERSION "0.9.8.20081128"

/* Enable parser debugging */
/* #undef YYDEBUG */

/* define on AIX to get certain functions */
/* #undef _SUN */

/* Define to empty if `const' does not conform to ANSI C. */
/* #undef const */

/* Define to `__inline__' or `__inline' if that's what the C compiler
   calls it, or to nothing if 'inline' is not supported under any name.  */
#ifndef __cplusplus
#define inline inline
#endif

/* Define to `long int' if <sys/types.h> does not define. */
/* #undef off_t */

/* Define to `int' if <sys/types.h> does not define. */
/* #undef pid_t */

/* on sinix */
/* #undef sinix */

/* Define to `unsigned int' if <sys/types.h> does not define. */
/* #undef size_t */

/* if we have u_int16_t */
/* #undef u_int16_t */

/* if we have u_int32_t */
/* #undef u_int32_t */

/* if we have u_int8_t */
/* #undef u_int8_t */
