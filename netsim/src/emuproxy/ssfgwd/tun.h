#ifndef _TUN_H
#define _TUN_H

/* 
 * Allocate TUN device, returns opened fd. 
 * Stores dev name in the first arg(must be large enough).
 */  
extern int tun_open(char *dev);

#endif
