/*---------------------------------------------------------------------------*/
/* Portable TCP FM-like interface.                                           */
/* Author(s): Kalyan Perumalla <http://www.cc.gatech.edu/~kalyan> 20July2001 */
/* $Revision: 1.1.1.1 $ $Name:  $ $Date: 2004/07/21 21:01:31 $ */
/*---------------------------------------------------------------------------*/
#ifndef __TCP_FM_H
#define __TCP_FM_H

/*---------------------------------------------------------------------------*/
#define TCPMAXPE 8 /*CUSTOMIZE*/
#define TCPMAXPIECES 16
#define TCPMAXPIECELEN 1000
#define TCPMAXDATALEN (TCPMAXPIECES*TCPMAXPIECELEN)

/*---------------------------------------------------------------------------*/
typedef void TCP_stream;

/*---------------------------------------------------------------------------*/
typedef int TCPCallback(int, TCP_stream *, int, int, int);

/*---------------------------------------------------------------------------*/
void TCP_initialize(char *[], int, int, TCPCallback *);
void TCP_finalize(void);
TCP_stream *TCP_begin_message(int, int, int, int, int);
void TCP_send_piece(TCP_stream *, void *, int);
void TCP_end_message(TCP_stream *);
int TCP_receive(void *, TCP_stream *, unsigned int);
int TCP_numpieces(TCP_stream *);
int TCP_piecelen(TCP_stream *, int);
int TCP_extract(unsigned int maxbytes);
int TCP_debug_level(int);

/*---------------------------------------------------------------------------*/
extern int TCP_nodeid;
extern int TCP_numnodes;

/*---------------------------------------------------------------------------*/
#endif /* __TCP_FM_H */
