/*
 *  OpenVPN -- An application to securely tunnel IP networks
 *             over a single TCP/UDP port, with support for SSL/TLS-based
 *             session authentication and key exchange,
 *             packet encryption, packet authentication, and
 *             packet compression.
 *
 *  Copyright (C) 2002-2005 OpenVPN Solutions LLC <info@openvpn.net>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2
 *  as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program (see the file COPYING included with this
 *  distribution); if not, write to the Free Software Foundation, Inc.,
 *  59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * This file implements a simple OpenVPN plugin module which
 * will examine the username/password provided by a client,
 * and make an accept/deny determination.  Will run
 * on Windows or *nix.
 *
 * See the README file for build instructions.
 */

/* Local defines (sdm) */
#define PRIME_CONNECT OPENVPN_PLUGIN_CLIENT_CONNECT
#define PRIME_DISCONNECT OPENVPN_PLUGIN_CLIENT_DISCONNECT
#define PRIME_DEFAULT_PIPE 11
#define PRIME_MAX_ERROR_MESSAGE_LENGTH 256
#define PRIME_MAX_MESSAGE_LENGTH (sizeof(struct ip) + sizeof(uint32_t))

/* Standard includes (sdm)*/
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <signal.h>
#include <syslog.h>
#include <ctype.h>
#include <unistd.h>
#include <errno.h>

/* includes for socket comms (sdm)*/
#include <sys/socket.h>
#include <sys/wait.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netinet/tcp.h>
#include <sys/types.h>
#include <arpa/inet.h>

/* Local includes */
#include "openvpn-plugin.h"

/*Local prototype */
uint32_t prime_sendMsg (const char *destIP, const char *srcIP, uint32_t pipeD,uint32_t primeType);
static uint16_t iphdr_cksum(uint16_t *buf, uint32_t length);

/*
 * Given an environmental variable name, search
 * the envp array for its value, returning it
 * if found or NULL otherwise.
 */
static const char *
get_env (const char *name, const char *envp[])
{
  if (envp)
    {
      int i;
      const int namelen = strlen (name);
      for (i = 0; envp[i]; ++i)
	{
	  if (!strncmp (envp[i], name, namelen))
	    {
	      const char *cp = envp[i] + namelen;
	      if (*cp == '=')
		return cp + 1;
	    }
	}
    }
  return NULL;
}

OPENVPN_PLUGIN_DEF openvpn_plugin_handle_t
OPENVPN_PLUGIN_FUNC(openvpn_plugin_open_v2) 
	(unsigned int *type_mask, 
		const char *argv[], 
		const char *envp[],
		struct openvpn_plugin_string_list **return_list)
{
	struct openvpn_plugin_string_list *context;
	uint32_t length = 0;

	/*
	 * Allocate the context
	*/
	context = (struct openvpn_plugin_string_list *) calloc(1, sizeof (struct openvpn_plugin_string_list));

	/* Persist input conn/discon IP which should be passed in as an argument
	 * to this plugin (openvpn-client-conn.so). You should see a line like:
	 * plugin "/etc/openvpn/plugin/openvpn-client-conn.so" "10.44.1.9"
	 * where the second quoted string is the IP addr of the server to which
	 * you want to send the connect/disconnect info
	 */

	if (argv[1] != NULL)
	{
		/* No other structs */
		context->next = NULL;

		/* This is the pipe for use in writing out the events */
		context->name = (char *) get_env("connection_plugin_pipe_fileno",envp);

		/* This is the input IP address - the address to which the
		 * the message is sent */
		length = strlen(argv[1]);
		context->value = (char *) calloc(1,length);
		strncpy(context->value,argv[1],length);
	} else {
		/* won't know who to send this to */
		fprintf(stderr,"NO server to send conn/disconn to!\n\tCheck server conf\n");
		context->next = NULL;
		context->name = NULL;
		context->value = NULL;
	}
	
	/*
	 * Only interested in intercepting the connect/disconnect
	 * callback.
	 */

	*type_mask = OPENVPN_PLUGIN_MASK(OPENVPN_PLUGIN_CLIENT_CONNECT)
				 | OPENVPN_PLUGIN_MASK(OPENVPN_PLUGIN_CLIENT_DISCONNECT);

	return((openvpn_plugin_handle_t) context);
}

OPENVPN_PLUGIN_DEF int
OPENVPN_PLUGIN_FUNC(openvpn_plugin_func_v2)
	(openvpn_plugin_handle_t handle, 
	 const int type,
	 const char *argv[],
	 const char *envp[],
	 void *per_client_context,
	 struct openvpn_plugin_string_list **return_list)
{
	uint32_t retVal = OPENVPN_PLUGIN_FUNC_SUCCESS;
	struct openvpn_plugin_string_list *context;
	uint32_t pipeDescriptor;
	const char *virtual_source_ip;
	const char *pipe;

	/* This function gets called for every event - it is the "callback"
	 * So, it calls functions that prep a struct and then send it off
	 * to a pipe...
	 */

	/* see the openvpn man page for ifconfig_pool_remote_ip.
	 * (the client's virtual ip). This is what we want to send to
	 * the simulation as the IP address being connected/disconnected.
	 * The pipe descriptor is passed through OpenVPN as a "custom"
	 * env variable...
	 * The openvpn man page is part of the API. The openvpn-plugin.h
	 * file constitutes the rest of the API.
	 */

	context = (struct openvpn_plugin_string_list *) handle;

	virtual_source_ip = get_env("ifconfig_pool_remote_ip", envp);

	/* This code is here in case we couldn't get the pipe descriptor in
	 * the open call.
	 * We have to be careful since get_env could return NULL
	 * NOTE: We use PRIME_DEFAULT_PIPE in the event that the value
	 * can't be retrieved from the envp array.
	 */
	if (context->name == NULL)
	{
		pipe = get_env("connection_plugin_pipe_fileno", envp);
		if (pipe == NULL)
		{
			pipeDescriptor = PRIME_DEFAULT_PIPE;
		} else {
			pipeDescriptor = atoi(pipe);
		}
	} else {
		pipeDescriptor = atoi(context->name);
	}

	retVal = prime_sendMsg(context->value, virtual_source_ip, pipeDescriptor, type);

    return(retVal); 
}

uint32_t prime_sendMsg (const char *destIP, const char *srcIP, uint32_t pipeD,uint32_t primeType)
{
	uint32_t retVal = OPENVPN_PLUGIN_FUNC_SUCCESS;
	uint32_t errnum;
	int32_t retCode;
	char errMsg[PRIME_MAX_ERROR_MESSAGE_LENGTH];
	char datagram[PRIME_MAX_MESSAGE_LENGTH]; /* hdrs & payload - big enough for any packet*/
	uint32_t *primePrefix = (uint32_t *) datagram; /* this is the virt source ip*/
	struct ip *primeIP = (struct ip *) (datagram + sizeof(uint32_t)); /*IP hdr follows the prime prefix*/

	memset(datagram,0,PRIME_MAX_MESSAGE_LENGTH);
	
	/* First build the headers */
	/* overload the TOS field for PRIME meaning */
	if (primeType == PRIME_CONNECT)
	{
		primeIP->ip_tos = 0x01;
	} else if (primeType == PRIME_DISCONNECT) {
		primeIP->ip_tos = 0x02;
	} else {
		retVal = OPENVPN_PLUGIN_FUNC_ERROR;
		fprintf(stderr,"[In prime_sendMsg]: FATAL incorrect primeType %i\n",primeType);
	}

	if (retVal != OPENVPN_PLUGIN_FUNC_ERROR)
	{
		primeIP->ip_len = htons(sizeof(struct ip)); /* no payload & no other hdrs*/
		primeIP->ip_hl = 5;
		primeIP->ip_v = 4;
		primeIP->ip_id = htons(54321);
		primeIP->ip_off = 0;
		primeIP->ip_ttl = 255;
		primeIP->ip_p = 255; //IPPROTO_TCP;
		primeIP->ip_sum = 0;
		primeIP->ip_dst.s_addr = inet_addr(destIP);
		primeIP->ip_src.s_addr = inet_addr(srcIP);
		primeIP->ip_sum = iphdr_cksum((uint16_t *) primeIP, (primeIP->ip_len >> 1));

		/* the prefix */
		*primePrefix = primeIP->ip_src.s_addr;

		/* write to the pipe */
		retCode = write(pipeD,datagram,PRIME_MAX_MESSAGE_LENGTH);	
		errnum = errno;
		if (retCode < 0)
		{
			retVal = OPENVPN_PLUGIN_FUNC_ERROR;
			strerror_r(errnum,errMsg,PRIME_MAX_ERROR_MESSAGE_LENGTH);
			/*Note that there may be no text with a given error
			 * see errno.h for more details.
			 */
			fprintf(stderr,"[In prime_sendMsg]Error from write:\n\t%i:%s\n",errnum,errMsg);
		} else {
			/* this is just a status message and probably should
			 * write somewhere else, but the API doesn't provide anything...
			 */
			fprintf(stderr,"Sent %i bytes with SRC=%s\n",retCode, srcIP);
		}
	
	} //if (retVal != OPENVPN_PLUGIN_FUNC_ERROR)

	return(retVal);
}

OPENVPN_PLUGIN_DEF void
OPENVPN_PLUGIN_FUNC(openvpn_plugin_close_v1) 
	(openvpn_plugin_handle_t handle)
{
	struct openvpn_plugin_string_list *context;

	context = (struct openvpn_plugin_string_list *) handle;
	free(context);
}
static uint16_t iphdr_cksum(uint16_t *buf, uint32_t length)
{
	uint32_t sum;

	for (sum=0; length > 0; length--)
 	{
		sum += *buf++;
	}
    sum = (sum >> 16) + (sum & 0xffff);
    sum += (sum >> 16);
	return (~sum);
}
#if 0
uint32_t prime_sendMsg (char *destIP, char *srcIP, uint32_t primeType)
{
	uint32_t optval = SO_DEBUG;
	uint32_t retVal = OPENVPN_PLUGIN_FUNC_SUCCESS;
	uint32_t errnum;
	uint32_t retCode;
	uint32_t SocketDescriptor;
	char errMsg[PRIME_MAX_ERROR_MESSAGE_LENGTH];
	char datagram[4096]; /* hdrs & payload - big enough for any packet*/
	struct ip *primeIP = (struct ip *) datagram; /*IP hdr at the beginning*/
	struct tcphdr *primeTCP = (((struct tcphdr *) datagram) + sizeof(struct ip)); /* starts just after the ip hdr*/
	struct sockaddr_in sin;

	memset(datagram,0,4096);
	
	/* First build the headers */
	primeIP->ip_hl = 5;
	primeIP->ip_v = 4;
	/* overload the TOS field for PRIME meaning */
	if (primeType == PRIME_CONNECT)
	{
		primeIP->ip_tos = 0x01;
	} else if (primeType == PRIME_DISCONNECT) {
		primeIP->ip_tos = 0x02;
	} else {
		retVal = OPENVPN_PLUGIN_FUNC_ERROR;
	}

	primeIP->ip_len = sizeof(struct ip) + sizeof(struct tcphdr); /* no payload */
	primeIP->ip_id = htonl(54321);
	primeIP->ip_off = 0;
	primeIP->ip_ttl = 255;
	primeIP->ip_p = IPPROTO_TCP;
	primeIP->ip_sum = 0;
	primeIP->ip_dst.s_addr = inet_addr(destIP);
	primeIP->ip_src.s_addr = inet_addr(srcIP);

	/* Just send a syn packet */
	primeTCP->source = htons(3456);
	primeTCP->dest = htons(36685); /* the PRIME port */
	primeTCP->seq = random();
	primeTCP->ack_seq = 0;
	primeTCP->res1 = 0;
	primeTCP->doff = 0;
	primeTCP->fin = 0;
	primeTCP->syn = 1;
	primeTCP->rst = 0;
	primeTCP->psh = 0;
	primeTCP->ack = 0;
	primeTCP->urg = 0;
	primeTCP->res2 = 0;
	primeTCP->window = htons(65535);
	primeTCP->check = 0;
	primeTCP->urg_ptr = 0;

	primeIP->ip_sum = iphdr_cksum((uint16_t *) datagram, (primeIP->ip_len >> 1));

	/*Set up the sockaddr struct*/
	sin.sin_family = AF_INET;
	sin.sin_port = primeTCP->dest;
	sin.sin_addr.s_addr = inet_addr("127.0.0.1"); /*play it safe?*/

	/* Now open the socket, set the options, and send the packet */
	SocketDescriptor = socket(AF_INET,SOCK_RAW,IPPROTO_TCP);

	/*Set the include header (IP_HDRINCL) and debug opts */
	retCode = setsockopt(SocketDescriptor,IPPROTO_IP,IP_HDRINCL,&optval,sizeof(optval));
	errnum = errno;
	if (retCode == 0)
	{
		/* send */
		retCode = sendto(SocketDescriptor,
						 datagram,
						 primeIP->ip_len, /*datagram length*/
						 0,				  /*routing flags*/
						 (struct sockaddr *) &sin,
						 sizeof(sin));
		errnum = errno;
		if (retCode < 0)
		{
			retVal = OPENVPN_PLUGIN_FUNC_ERROR;
			strerror_r(errnum,errMsg,PRIME_MAX_ERROR_MESSAGE_LENGTH);
			fprintf(stderr,"[In prime_sendMsg]Error from sendto:\n\t%s",errMsg);
		} else {
			fprintf(stderr,"Sent %i bytes\n",retCode);
		}
	} else {
		retVal = OPENVPN_PLUGIN_FUNC_ERROR;
		strerror_r(errnum,errMsg,PRIME_MAX_ERROR_MESSAGE_LENGTH);
		fprintf(stderr,"[In prime_sendMsg]Unable to setsockopt:\n\t%s",errMsg);
	}
		
	return(retVal);
}
#endif
