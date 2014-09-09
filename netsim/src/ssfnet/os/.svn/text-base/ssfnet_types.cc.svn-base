/**
 * \file ssfnet_types.cc
 * \brief Source file for Abstract Data Types designed to be shared-memory safe.
 *
 * \author Nathanael Van Vorst
 * \author Ting Li
 * 
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

#include <ctype.h>
#include <stdio.h>
#include <string.h>
#include "os/ssfnet_types.h"
#include "os/virtual_time.h"
#include "os/logger.h"
#include "stdint.h"
#include <iomanip>
#include <sys/time.h>

#if TEST_ROUTING == 1
#include "os/packet.h"
#include "os/routing.h"
#endif

namespace prime {
namespace ssfnet {
LOGGING_COMPONENT(ssfnet_types)

unsigned long long nateorder__1=0;
unsigned long long nateorder__2=0;
unsigned long long nateorder_1()
{
return ++nateorder__1;
}
unsigned long long nateorder_2()
{
return ++nateorder__2;
}


byte* sstrdup(const byte* s) {
	if (!s)
		return 0;
	byte* x = new byte[strlen((const char*) s) + 1]; // overloaded new operator
	strcpy((char*) x, (const char*) s);
	return x;
}

// The following code is modified from the raw socket example provided
// by Thamer Al-Herbish <shadows@whitefang.com>, which considers the
// case when the buffer length (in bytes) is odd.
uint16 cksum(uint16* addr, int len, uint16 accu) {
	register int sum = 0;
	uint16 answer = 0;
	register uint16 *w = addr;
	register int nleft = len;

	/*
	 * Our algorithm is simple, using a 32 bit accumulator (sum), we add
	 * sequential 16 bit words to it, and at the end, fold back all the
	 * carry bits from the top 16 bits into the lower 16 bits.
	 */
	while (nleft > 1) {
		sum += *w++;
		nleft -= 2;
	}

	/* mop up an odd byte, if necessary */
	if (nleft == 1) {
		*(byte*) (&answer) = *(byte*) w;
		sum += answer;
	}

	/* add back carry outs from top 16 bits to low 16 bits */
	sum = (sum >> 16) + (sum & 0xffff); /* add hi 16 to low 16 */
	if (accu)
		sum += (~accu) & 0xffff; /* add accumulative result */
	sum += (sum >> 16); /* add carry */
	answer = ~sum; /* truncate to 16 bits */

	return answer;
}


int fast_cksum_a(uint16* addr, int len) {
	register int sum = 0;
	uint16 answer = 0;
	register uint16 *w = addr;
	register int nleft = len;

	/*
	 * Our algorithm is simple, using a 32 bit accumulator (sum), we add
	 * sequential 16 bit words to it, and at the end, fold back all the
	 * carry bits from the top 16 bits into the lower 16 bits.
	 */
	while (nleft > 1) {
		sum += *w++;
		nleft -= 2;
	}

	/* mop up an odd byte, if necessary */
	if (nleft == 1) {
		*(byte*) (&answer) = *(byte*) w;
		sum += answer;
	}

	return sum;
}

SSFNetEvent::SSFNetEvent(SSFNetEvent::Type _ssfnet_evt_type, int _target_com_id):
		ssf::Event(),
		ssfnet_evt_type(_ssfnet_evt_type), target_com_id(_target_com_id) {
}

SSFNetEvent::SSFNetEvent(const SSFNetEvent& evt):
		ssf::Event(evt),
		ssfnet_evt_type(evt.ssfnet_evt_type), target_com_id(evt.target_com_id)  {
}

SSFNetEvent::~SSFNetEvent() {
	//LOG_DEBUG("Deleting SSFNetEvent, ssfnet_evt_type="<<ssfnet_evt_type<<", target_com_id="<<target_com_id<<endl);
}

void SSFNetEvent::free() {
	//LOG_DEBUG("Freeing SSFNetEvent, ssfnet_evt_type="<<ssfnet_evt_type<<", target_com_id="<<target_com_id<<endl);
	if(!isOwnedBySystem()) {
		//LOG_DEBUG("It's an event owned by user." <<endl);
		delete this;
	}
}

SSFNetEvent::Type SSFNetEvent::getSSFNetEventType() const {
	return ssfnet_evt_type;
}

void SSFNetEvent::setSSFNetEventType(SSFNetEvent::Type  t) {
	this->ssfnet_evt_type=t;
}

void SSFNetEvent::setTargetCommunity(int _com_id) {
	this->target_com_id=_com_id;
}

int SSFNetEvent::getTargetCommunity() const {
	return target_com_id;
}

prime::ssf::ssf_compact* SSFNetEvent::pack() {
	prime::ssf::ssf_compact* dp = new prime::ssf::ssf_compact();
	dp->add_short(ssfnet_evt_type);
	dp->add_int(target_com_id);
	//LOG_DEBUG("Packing SSFNetEvent, ssfnet_evt_type="<<ssfnet_evt_type<<", target_com_id="<<target_com_id<<endl);
	return dp;
}

void SSFNetEvent::unpack(prime::ssf::ssf_compact* dp) {
	short t;
	dp->get_short(&t,1);
	ssfnet_evt_type=(SSFNetEvent::Type)t;
	if(ssfnet_evt_type<=SSFNET_EVT_MIN) {
		LOG_ERROR("The event has an invalid event type, ssfnet_evt_type="<<ssfnet_evt_type<<"!"<<endl);
	}
	else if(ssfnet_evt_type>=SSFNET_EVT_MAX) {
		LOG_ERROR("The event has an invalid event type, ssfnet_evt_type="<<ssfnet_evt_type<<"!"<<endl);
	}
	dp->get_int(&target_com_id);
	if(target_com_id<0) {
		LOG_ERROR("The event has an invalid community id, community id="<<target_com_id<<"!"<<endl);
	}
	//LOG_DEBUG("Unpacking SSFNetEvent, ssfnet_evt_type="<<ssfnet_evt_type<<", target_com_id="<<target_com_id<<endl);
}

SSFNET_STRING IPAddress::toString() const {
	char buf[50];
	char* s = buf;
	for (int i = 24; i >= 0; i -= 8) {
		sprintf(s, "%u", ((unsigned) ((ipaddr >> i) & 0xFF)));
		s += strlen(s);
		if (i > 0)
			sprintf(s++, ".");
	}
	return buf;
}

void IPAddress::fromString(const SSFNET_STRING& ipstr) {
	const char* str = ipstr.c_str();
	int dot_num = 0; // how many dot are met so far
	int base = 0, incr = 0;
	int i;
	for (i = 0; (isdigit(str[i]) || str[i] == '\0' || (str[i] == '.' && dot_num
			<= 2)); i++) {
		// we get to the end of a number
		if ((str[i] == '.') || (str[i] == '\0')) {
			base *= 256;
			base += incr;
			incr = 0;
			if (str[i] == '\0')
				break;
			else
				dot_num++;
		} else { // it is a digit
			incr *= 10;
			incr += (str[i] - '0');
			if (incr >= 256)
				break;
		}
	}
	if (str[i] == '\0' && dot_num == 3) {
		ipaddr = base;
	} else {
		ipaddr = IPADDR_INVALID;
	}
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPAddress& x) {
	os << x.toString();
	return os;
}
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPAddress* x) {
	if (x)
		return os << x->toString();
	return os << "[NULL(IPAddress)]";
}

uint32 IPPrefix::masks[33] = {
		0x0,        0x80000000, 0xC0000000, 0xE0000000, 0xF0000000, //0-4
		0xF8000000, 0xFC000000, 0xFE000000, 0xFF000000, 0xFF800000, //5-9
		0xFFC00000, 0xFFE00000, 0xFFF00000, 0xFFF80000, 0xFFFC0000, //10-14
		0xFFFE0000, 0xFFFF0000, 0xFFFF8000, 0xFFFFC000, 0xFFFFE000, //15-19
		0xFFFFF000, 0xFFFFF800, 0xFFFFFC00, 0xFFFFFE00, 0xFFFFFF00, //20-24
		0xFFFFFF80, 0xFFFFFFC0, 0xFFFFFFE0, 0xFFFFFFF0, 0xFFFFFFF8, //25-29
		0xFFFFFFFC, 0xFFFFFFFE, 0xFFFFFFFF }; //30-32

bool IPPrefix::contains(IPPrefix& ippref) {
	return (len <= ippref.len) && (((uint32(ippref.addr) ^ uint32(addr))
			& masks[len]) == 0);
}

bool IPPrefix::contains(IPAddress ipaddr) {
	return ((uint32(ipaddr) ^ uint32(addr)) & masks[len]) == 0;
}

bool operator==(const IPPrefix& ip1, const IPPrefix& ip2) {
	return (ip1.len == ip2.len) && ((uint32(ip1.addr)
			& IPPrefix::masks[ip1.len]) == (uint32(ip2.addr)
			& IPPrefix::masks[ip2.len]));
}

bool operator!=(const IPPrefix& ip1, const IPPrefix& ip2) {
	return (ip1.len != ip2.len) || ((uint32(ip1.addr)
			& IPPrefix::masks[ip1.len]) != (uint32(ip2.addr)
			& IPPrefix::masks[ip2.len]));
}

SSFNET_STRING IPPrefix::toString() const {
	char buf[50];
	char* s = buf;
	for (int i = 24; i >= 0; i -= 8) {
		sprintf(s, "%u", ((uint32(addr) >> i) & 0xFF));
		s += strlen(s);
		if (i > 0)
			sprintf(s++, ".");
	}
	if (len < 32)
		sprintf(s, "/%d", len);
	return buf;
}

void IPPrefix::fromString(const SSFNET_STRING& ipstr) {
	int pos = ipstr.find('/');
	if (pos > 0) {
		//we have a "/XX"
		addr.fromString(ipstr.substr(0, pos));
		len = atoi(ipstr.substr(pos + 1).c_str());
	} else {
		//its just and ip
		addr.fromString(ipstr);
		len = 32;
	}
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefix& x) {
	os << x.toString();
	return os;
}
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefix* x) {
	if (x)
		return os << x->toString();
	return os << "[NULL(IPPrefix)]";
}


bool operator==(const IPPrefixRoute& ip1, const IPPrefixRoute& ip2) {
	return ip1.portal_uid == ip2.portal_uid && ip1.prefix == ip2.prefix ;
}

bool operator!=(const IPPrefixRoute& ip1, const IPPrefixRoute& ip2) {
	return ip1.portal_uid != ip2.portal_uid || ip1.prefix != ip2.prefix ;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefixRoute& x) {
	return os << x.prefix.toString()<<"-->{host:"<<x.portal_uid<<",iface:("<<x.portal_iface_uid<<","<<x.portal_ip.toString()<<","<<x.portal_mac.toString()<<")}";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefixRoute* x) {
	if (x)
		return os << x->prefix.toString()<<"-->{host:"<<x->portal_uid<<",iface:("<<x->portal_iface_uid<<","<<x->portal_ip.toString()<<","<<x->portal_mac.toString()<<")}";
	return os << "[NULL(IPPrefixRoute)]";
}


MACAddress MACAddress::MACADDR_INVALID  =MACAddress(0x0000000000000000ULL);  //00:00:00:00:00:00
MACAddress MACAddress::MACADDR_BROADCAST_11=MACAddress(0x0b0b0b0b0b0b0b0bULL);//11:11:11::11:11:11
MACAddress MACAddress::MACADDR_BROADCAST_FF=MACAddress(0xffffffffffffffffULL);//11:11:11::11:11:11
//MACAddress MACAddress::MACADDR_BROADCAST=MACAddress(11111111);//XXX needs to be the above value

MACAddress::MACAddress() {
	macaddr=0;
}

MACAddress::MACAddress(UID_t uid) {
	macaddr=uid;
}

MACAddress::MACAddress(uint8_t* addr) {
	//XXX LOG_ERROR("this is broken!\n");
	macaddr=
			(((uint64_t)addr[5])     & 0xFFLL) +
			(((uint64_t)addr[4]<<8)  & 0xFF00LL) +
			(((uint64_t)addr[3]<<16) & 0xFF0000LL) +
			(((uint64_t)addr[2]<<24) & 0xFF000000LL) +
			(((uint64_t)addr[1]<<32) & 0xFF00000000LL) +
			(((uint64_t)addr[0]<<40) & 0xFF0000000000LL);
/*		LOG_DEBUG("macaddr="<<macaddr<<" "
				<< std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)addr[0]
				<<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)addr[1]
				<<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)addr[2]
				<<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)addr[3]
				<<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)addr[4]
				<<":"<<std::setw(2)<<std::setfill('0')<<std::hex<<(uint16_t)addr[5]<<endl);*/
}


MACAddress::MACAddress(SSFNET_STRING addr) {
	fromString(addr);
}

MACAddress& MACAddress::operator=(const MACAddress& x) {
	macaddr=x.macaddr;
	return *this;
}

MACAddress& MACAddress::operator=(const char* addr) {
	SSFNET_STRING str(addr);
	fromString(str);
	return *this;
}

MACAddress& MACAddress::operator=(const SSFNET_STRING& addr) {
	fromString(addr);
	return *this;
}
MACAddress& MACAddress::operator=(const uint8_t* addr) {
	//XXX LOG_ERROR("this is broken!\n");
	macaddr=
			(((uint64_t)addr[5])     & 0xFFLL) +
			(((uint64_t)addr[4]<<8)  & 0xFF00LL) +
			(((uint64_t)addr[3]<<16) & 0xFF0000LL) +
			(((uint64_t)addr[2]<<24) & 0xFF000000LL) +
			(((uint64_t)addr[1]<<32) & 0xFF00000000LL) +
			(((uint64_t)addr[0]<<40) & 0xFF0000000000LL);
	return *this;
}

int operator==(const MACAddress& x, const MACAddress& y) {
	if(x.macaddr==y.macaddr) {
		return 1;
	}
	return 0;
}

int operator!=(const MACAddress& x, const MACAddress& y) {
	if(x.macaddr==y.macaddr) {
		return 0;
	}
	return 1;
}

SSFNET_STRING MACAddress::toString() const {
	char buf[50];
	char* s = buf;
	for (int i = 40; i >= 0; i -= 8) {
		sprintf(s, "%02x", ((unsigned) ((macaddr >> i) & 0xFF)));
		s += strlen(s);
		if (i > 0)
			sprintf(s++, ":");
	}
	return buf;
}


void MACAddress::toRawMac(uint8* raw_mac) const {
	for (int i = 40; i >= 0; i -= 8) {
		raw_mac[5-i/8]=((unsigned) ((macaddr >> i) & 0xFF));
	}
}


void MACAddress::fromString(const SSFNET_STRING& macstr) {
		//XXX this logic looks VERY WRONG
	const char* str = macstr.c_str();
	int dot_num = 0; // how many dot are met so far
	uint64_t base = 0, incr = 0;
	int i;

	for (i = 0; (isdigit(str[i]) || str[i] == '\0' || (str[i] == ':' && dot_num
			<= 4)); i++) {
		// we get to the end of a number
		if ((str[i] == ':') || (str[i] == '\0')) {
			base *= 256;
			base += incr;
			incr = 0;
			if (str[i] == '\0')
				break;
			else
				dot_num++;
		} else { // it is a digit
			incr *= 10;
			incr += (str[i] - '0');
			if (incr >= 256)
				break;
		}
	}
	if (dot_num == 0) {
		macaddr=base;
	} else {
		macaddr=0;
	}
}

bool MACAddress::operator <(const MACAddress& mac) const {
	if(macaddr < mac.macaddr) {
		return true;
	}
	return false;
}

bool MACAddress::operator <(const MACAddress*& mac) const {
	if(macaddr < mac->macaddr) {
		return true;
	}
	return false;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const MACAddress& x) {
	os << x.toString();
	return os;
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const SSFNET_STRING* x) {
	if (x)
		return os << *x;
	return os << "[NULL(SSFNET_STRING)]";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, SSFNET_STRING* x) {
	if (x)
		return os << *x;
	return os << "[NULL(SSFNET_STRING)]";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const char* x) {
	if (x) {
		SSFNET_STRING s;
		s.append(x);
		return os << s;
	}
	return os << "[NULL(char*)]";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, char* x) {
	if (x) {
		SSFNET_STRING s;
		s.append(x);
		return os << s;
	}
	return os << "[NULL(char*)]";
}

int UIDRange::compare(const UIDRange lhs, const UIDRange rhs) {
	// 1 if lhs > rhs
	// 0 if lhs == rhs
	//-1 if lhs< rhs
	if (lhs.lower == rhs.lower) {
		if (rhs.upper == lhs.upper) {
			return 0;
		}
		if (lhs.upper < rhs.upper) {
			return -1;
		}
		return 1;
	}
	//we know lower != rhs.lower
	if (lhs.lower < rhs.lower) {
		return -1;
	}
	return 1;
}

void warn_default_rw_config_var(SSFNET_STRING dt, SSFNET_STRING st) {
	if (dt.compare(st) != 0)
		LOG_WARN("The template function bool rw_config_var<"<<dt<<", "<<st<<">\n\t was not specialized so the default was used!"<<endl)
}


template<> bool rw_config_var<double, float> (double& dst, float& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, int8_t> (double& dst, int8_t& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, int16_t> (double& dst, int16_t& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, int32_t> (double& dst, int32_t& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, int64_t> (double& dst, int64_t& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, uint8_t> (double& dst, uint8_t& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, uint16_t> (double& dst, uint16_t& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, uint32_t> (double& dst, uint32_t& src) {
	dst=(double)src;
	return false;
}
template<> bool rw_config_var<double, uint64_t> (double& dst, uint64_t& src) {
	dst=(double)src;
	return false;
}


template<>
bool rw_config_var<SSFNET_STRING, SSFNET_STRING> (SSFNET_STRING& dst,
		SSFNET_STRING& src) {
	dst = src;
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, void*> (SSFNET_STRING& dst,
		void*& src) {
	char t[50];
	snprintf(t, 50, "%p", src);
	dst.clear();
	dst.append(t);
	return false;
}

template<>
bool rw_config_var<SSFNET_STRING, float> (SSFNET_STRING& dst, float& src) {
	std::stringstream ss;
	ss << src;
	ss >> dst;
	return false;
}
template<>
bool rw_config_var<SSFNET_STRING, double> (SSFNET_STRING& dst, double& src) {
	std::stringstream ss;
	ss << src;
	ss >> dst;
	return false;
}
template<>
bool rw_config_var<SSFNET_STRING, bool> (SSFNET_STRING& dst, bool& src) {
	dst.clear();
	if(src) {
		dst.append("true");
	}
	else {
		dst.append("false");
	}
	return false;
}

template<>
bool rw_config_var<bool, SSFNET_STRING> (bool& dst, SSFNET_STRING& src) {
	if(!strcasecmp("true",src.c_str())) {
		dst=1;
	}
	else if(!strcasecmp("false",src.c_str())) {
		dst=0;
	}
	else {
		if(atoi(src.c_str()))
			dst=1;
		else
			dst=0;
	}
	return false;
}

template<>
bool rw_config_var<float, SSFNET_STRING> (float& dst, SSFNET_STRING& src) {
	std::stringstream ss;
	ss << src;
	ss >> dst;
	return false;
}

template<>
bool rw_config_var<double, SSFNET_STRING> (double& dst, SSFNET_STRING& src) {
	std::stringstream ss;
	ss << src;
	ss >> dst;
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, LogLvl> (SSFNET_STRING& dst,
		LogLvl& src) {
	dst = src.toString();
	return false;
}

template<> bool rw_config_var<LogLvl, SSFNET_STRING> (LogLvl& dst,
		SSFNET_STRING& src) {
	dst = LogLvl::fromString(src);
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, IPAddress> (SSFNET_STRING& dst,
		IPAddress& src) {
	dst = src.toString();
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, IPPrefix> (SSFNET_STRING& dst,
		IPPrefix& src) {
	dst = src.toString();
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, MACAddress> (SSFNET_STRING& dst,
		MACAddress& src) {
	dst = src.toString();
	return false;
}

template<> bool rw_config_var<IPAddress, SSFNET_STRING> (IPAddress& dst,
		SSFNET_STRING& src) {
	dst.fromString(src.c_str());
	return false;
}
template<> bool rw_config_var<IPPrefix, SSFNET_STRING> (IPPrefix& dst,
		SSFNET_STRING& src) {
	dst.fromString(src.c_str());
	return false;
}
template<> bool rw_config_var<MACAddress, SSFNET_STRING> (MACAddress& dst,
		SSFNET_STRING& src) {
	dst.fromString(src.c_str());
	return false;
}

#ifndef INT8_MIN

/* Minimum of signed integral types.  */
# define INT8_MIN		(-128)
# define INT16_MIN		(-32767-1)
# define INT32_MIN		(-2147483647-1)
# define INT64_MIN		(-__INT64_C(9223372036854775807)-1)
/* Maximum of signed integral types.  */
# define INT8_MAX		(127)
# define INT16_MAX		(32767)
# define INT32_MAX		(2147483647)
# define INT64_MAX		(__INT64_C(9223372036854775807))

/* Maximum of unsigned integral types.  */
# define UINT8_MAX		(255)
# define UINT16_MAX		(65535)
# define UINT32_MAX		(4294967295U)
# define UINT64_MAX		(__UINT64_C(18446744073709551615))

#endif

template<> bool rw_config_var<int8_t, SSFNET_STRING> (int8_t& dst,
		SSFNET_STRING& src) {
	int a = atoi(src.c_str());
	if (a >= INT8_MIN && a <= INT8_MAX) {
		dst = (int8_t) a;
	} else {
		char tmp[512];
		snprintf(tmp, 512, "%i is not in the range [%i,%i].", a, INT8_MIN,
				INT8_MAX);
		ssfnet_throw_exception(SSFNetException::other_exception, tmp);
	}
	return false;
}

template<> bool rw_config_var<int16_t, SSFNET_STRING> (int16_t& dst,
		SSFNET_STRING& src) {
	int a = atoi(src.c_str());
	if (a >= INT16_MIN && a <= INT16_MAX) {
		dst = (int16_t) a;
	} else {
		char tmp[512];
		snprintf(tmp, 512, "%i is not in the range [%i,%i].", a, INT16_MIN,
				INT16_MAX);
		ssfnet_throw_exception(SSFNetException::other_exception, tmp);
	}
	return false;
}

template<> bool rw_config_var<int32_t, SSFNET_STRING> (int32_t& dst,
		SSFNET_STRING& src) {
	int a=atoi(src.c_str());
	if (a >= INT32_MIN && a <= INT32_MAX) {
		dst = (int32_t) a;
	} else {
		char tmp[512];
		snprintf(tmp, 512, "%i is not in the range [%i,%i].", a, INT32_MIN,
				INT32_MAX);
		ssfnet_throw_exception(SSFNetException::other_exception, tmp);
	}
	return false;
}

template<> bool rw_config_var<int64_t, SSFNET_STRING> (int64_t& dst, SSFNET_STRING& src) {
	dst = atol(src.c_str());
	return false;
}


template<> bool rw_config_var<uint8_t, SSFNET_STRING> (uint8_t& dst,
		SSFNET_STRING& src) {
	int a = atoi(src.c_str());
	if (a >= 0 && a <= UINT8_MAX) {
		dst = (uint8_t) a;
	} else {
		char tmp[512];
		snprintf(tmp, 512, "%i is not in the range [%i,%i].", a, 0, UINT8_MAX);
		ssfnet_throw_exception(SSFNetException::other_exception, tmp);
	}
	return false;
}

template<> bool rw_config_var<uint16_t, SSFNET_STRING> (uint16_t& dst,
		SSFNET_STRING& src) {
	int a = atoi(src.c_str());
	if (a >= 0 && a <= UINT16_MAX) {
		dst = (uint16_t) a;
	} else {
		char tmp[512];
		snprintf(tmp, 512, "%i is not in the range [%i,%i].", a, 0, UINT16_MAX);
		ssfnet_throw_exception(SSFNetException::other_exception, tmp);
	}
	return false;
}
template<> bool rw_config_var<uint32_t, SSFNET_STRING> (uint32_t& dst,
		SSFNET_STRING& src) {
	uint64_t a = atol(src.c_str());
	if (a >= 0 && a <= UINT32_MAX) {
		dst = (uint32_t) a;
	} else {
		char tmp[512];
		snprintf(tmp, 512, "%llu is not in the range [%i,%ui].", a, 0, UINT32_MAX);
		ssfnet_throw_exception(SSFNetException::other_exception, tmp);
	}
	return false;
}

template<> bool rw_config_var<uint64_t, SSFNET_STRING> (uint64_t& dst, SSFNET_STRING& src) {
	dst = atoll(src.c_str());
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, int8_t> (SSFNET_STRING& dst,
		int8_t& src) {
	char c[34];
	snprintf(c, 33, "%i", src);
	dst.clear();
	dst.append(c);
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, int16_t> (SSFNET_STRING& dst,
		int16_t& src) {
	char c[34];
	snprintf(c, 33, "%i", src);
	dst.clear();
	dst.append(c);
	return false;
}
template<> bool rw_config_var<SSFNET_STRING, int32_t> (SSFNET_STRING& dst,
		int32_t& src) {
	char c[34];
	snprintf(c, 33, "%i", src);
	dst.clear();
	dst.append(c);
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, int64_t> (SSFNET_STRING& dst, int64_t& src) {
	char c[68];
	snprintf(c, 67, "%lli", src);
	dst.clear();
	dst.append(c);
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, uint8_t> (SSFNET_STRING& dst,
		uint8_t& src) {
	char c[34];
	snprintf(c, 33, "%ui", src);
	dst.clear();
	dst.append(c);
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, uint16_t> (SSFNET_STRING& dst,
		uint16_t& src) {
	char c[34];
	snprintf(c, 33, "%ui", src);
	dst.clear();
	dst.append(c);
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, uint32_t> (SSFNET_STRING& dst,
		uint32_t& src) {
	char c[34];
	snprintf(c, 33, "%ui", src);
	dst.clear();
	dst.append(c);
	return false;
}


template<> bool rw_config_var<SSFNET_STRING, uint64_t> (SSFNET_STRING& dst, uint64_t& src) {
	char c[68];
	snprintf(c, 67, "%llu", src);
	dst.append(c);
	return false;
}

template<> bool rw_config_var<SSFNET_STRING, UIDVec> (
                SSFNET_STRING& dst, UIDVec& src) {
        PRIME_STRING_STREAM s;
        bool first=true;
        s << "[";
        for (UIDVec::iterator i = src.begin(); i != src.end(); i++) {
        	if(!first)s<<",";
                s << *i;
                first=false;
        }
        s << "]";
        dst.append(s.str().c_str());
        return false;
}

template<> bool rw_config_var<UIDVec, SSFNET_STRING> (
                UIDVec& dst, SSFNET_STRING& src) {
        if (src.length() <= 1) {
                LOG_ERROR("Invalid UIDVec "<<src<<endl)
        }
        unsigned long long ll;
        PRIME_STRING_STREAM s(src.c_str());

        if (s.get() != '[') {
                LOG_ERROR("Invalid UIDVec '"<<src<<"'"<<endl)
        }
        while (s.good()) {
                switch (s.peek()) {
                case ']':
                        return false;
                case ',':
                        s.get();
                default:
                        s >> ll;
                        dst.push_back(ll);
                }
        }
        LOG_ERROR("Invalid UIDVec"<<src<<endl)
        return true;
}

template<> bool rw_config_var<SSFNET_STRING, IPPrefixRoute::List> (SSFNET_STRING& dst, IPPrefixRoute::List& src) {
    PRIME_STRING_STREAM s;
    bool first=true;
    s << "[";
    for (IPPrefixRoute::List::iterator i = src.begin(); i != src.end(); i++) {
    	if(!first)s<<",";
    	s << (*i).getPrefix().toString()
    	    	<< "," << (*i).getPortalUID()
    	    	<< "," << (*i).getPortalIfaceUID()
    	    	<< "," << (*i).getPortalIfaceIP();
    	first=false;
    }
    s << "]";
    dst.append(s.str().c_str());
    return false;
}

template<> bool rw_config_var<IPPrefixRoute::List, SSFNET_STRING> (IPPrefixRoute::List& dst, SSFNET_STRING& src) {
    if (src.length() <= 1) {
            LOG_ERROR("Invalid IPPrefixRoute::List "<<src<<endl)
    }
    IPPrefix prefix;
    IPAddress ip_addr=IPAddress::IPADDR_INVALID;
    unsigned long long portal_uid=0, portal_iface_uid=0;

    int count=0;

    char* str = strdup(src.c_str());
    char * pch;
    pch = strtok (str,"][,");
    while (pch != NULL) {
		SSFNET_STRING t;
		t.append(pch);
    	switch(count % 4 ) {
    	case 0:
    		prefix.fromString(t);
    		break;
    	case 1:
    		portal_uid = atoll(t.c_str());
    		break;
    	case 2:
    		portal_iface_uid = atoll(t.c_str());
    		break;
    	case 3:
    		ip_addr.fromString(t);
    		dst.push_back(IPPrefixRoute(prefix,portal_uid,portal_iface_uid,ip_addr,MACAddress(portal_iface_uid)));
    		break;
    	}
    	count++;
        pch = strtok (NULL, "][,");
    }
    delete str;
    return false;
}

template<> bool rw_config_var<SSFNET_STRING, IPPrefix::List> (SSFNET_STRING& dst, IPPrefix::List& src) {
    PRIME_STRING_STREAM s;
    bool first=true;
    s << "[";
    for (IPPrefix::List::iterator i = src.begin(); i != src.end(); i++) {
    	if(!first)s<<",";
    	s << (*i).toString();
    	first=false;
    }
    s << "]";
    dst.append(s.str().c_str());
    return false;
}

template<> bool rw_config_var<IPPrefix::List, SSFNET_STRING> (IPPrefix::List& dst, SSFNET_STRING& src) {
    if (src.length() <= 1) {
            LOG_ERROR("Invalid IPPrefix::List "<<src<<endl)
    }
    IPPrefix prefix;
	SSFNET_STRING t;

    char* str = strdup(src.c_str());
    char * pch;
    pch = strtok (str,"][,");
    while (pch != NULL) {
    	t.clear();
    	t.append(pch);
    	prefix.fromString(t);
    	dst.push_back(prefix);
        pch = strtok (NULL, " ,.-");
    }
    delete str;
    return false;
}

RuntimeVariables::RuntimeVariables() {}

RuntimeVariables::RuntimeVariables(const char* filename) {
	bool in_defaults=false, in_symbols=false;
	int state=0;
	SSFNET_STRING line;
	ifstream myfile (filename);
	if (myfile.is_open()) {
		while ( myfile.good() ) {
			getline (myfile,line);
			std::stringstream sl(line);
			SSFNET_STRING t="";
			SSFNET_STRING cls="",attr="",value="";
			size_t found;
			state=in_symbols?1:0;
			while(!sl.eof()) {
				sl >> t;
				if(t.compare(".defaults")==0) {
					in_defaults=true;
					in_symbols=false;
					line="";
					break;
				}
				else if(t.compare(".symbols")==0) {
					in_defaults=false;
					in_symbols=true;
					line="";
					break;
				}
				if(state==0) {
					found=t.find("::");
					if (found==SSFNET_STRING::npos) {
						cls.append(t);
					}
					else {
						cls.append(t.substr(0,found));
						if(found<t.length()-2) {
							SSFNET_STRING tt=t.substr(found+2);
							found=tt.find("=");
							if (found==SSFNET_STRING::npos) {
								attr.append(tt);
							}
							else {
								attr.append(tt.substr(0,found));
								if(found<tt.length()-1)
									value.append(tt.substr(found+1));
								state=2;
							}
						}
						state=1;
					}
				}
				else if (state == 1) {
					found=t.find("=");
					if (found==SSFNET_STRING::npos) {
						attr.append(t);
					}
					else {
						attr.append(t.substr(0,found));
						if(found<t.length()-1)
							value.append(t.substr(found+1));
						state=2;
					}
				}
				else {
					value.append(t);
				}
			}
			if(in_defaults) {
				if(line.length()> 0 &&(cls.length()==0 || attr.length()==0 || value.length()==0)) {
					LOG_WARN("Skipping '"<<line<<"' from the variable file!"<<endl);
				}
				else if(line.length()>0){
					Str2StrMap* m = 0;
					Class2VarMap::iterator i = defaults.find(cls);
					if(i == defaults.end()) {
						m = new Str2StrMap();
						defaults.insert(SSFNET_MAKE_PAIR(cls,m));
					}
					else {
						m=i->second;
					}
					m->insert(SSFNET_MAKE_PAIR(attr,value));
					//LOG_WARN("CREATED "<<cls<<"::"<<attr<<"="<<value<<"["<<m->size()<<"]"<<((void*)m)<<endl);
				}
			}
			else if(in_symbols) {
				if(line.length()> 0 &&(attr.length()==0 || value.length()==0)) {
					LOG_WARN("Skipping '"<<line<<"' from the variable file!"<<endl);
				}
				else if(line.length()>0){
					symbols.insert(SSFNET_MAKE_PAIR(attr,value));
				}
			}
			else if(line.length()>0){
				LOG_WARN("Skipping '"<<line<<"' from the variable file!"<<endl);
			}
		}
	    myfile.close();
	  }
}

SSFNET_STRING* RuntimeVariables::getSymbolValue(const SSFNET_STRING& name) {
	Str2StrMap::iterator i = symbols.find(name);
	if(i == symbols.end()) {
		return 0;
	}
	SSFNET_STRING * rv = new SSFNET_STRING();
	rv->append(i->second);
	symbols.erase(name);
	return rv;
}

SSFNET_STRING* RuntimeVariables::getDefaultValue(const SSFNET_STRING& cls, const SSFNET_STRING& attr) {
	Class2VarMap::iterator i = defaults.find(cls);
	if(i==defaults.end()) {
		return 0;
	}
	Str2StrMap* m = i->second;
	Str2StrMap::iterator j = m->find(attr);
	if(j==m->end()) {
		return 0;
	}
	SSFNET_STRING * rv = new SSFNET_STRING();
	rv->append(j->second);
	i->second->erase(attr);
	if(i->second->size()==0) {
		delete i->second;
		i->second=0;
		defaults.erase(cls);
	}
	return rv;
}

void RuntimeVariables::initDefaults() {
	SSFNET_SET(SSFNET_STRING) done;
	ConfigType::List todo;
	ConfigType::List to_write;
	ConfigurableFactory::setupConfigTypes();
	todo.push_back(BaseEntity::getClassConfigType());

	while (todo.size() > 0) {
		ConfigType* t = todo.front();
		todo.pop_front();
		if (done.count(t->getName()) > 0) {
			continue;
		} else if (!t->isBase(false)) {
			to_write.push_back(t);
			done.insert(t->getName());
		}
		for (ConfigType::IntMap::iterator i = t->getDerivedTypes().begin(); i
				!= t->getDerivedTypes().end(); i++) {
			todo.push_back(i->second);
		}
	}

	for (ConfigType::List::iterator i = to_write.begin(); i != to_write.end(); i++) {
		ConfigVarType::StrMap& vars=(*i)->vars;
		for(ConfigVarType::StrMap::iterator j=vars.begin();j!=vars.end();j++) {
			SSFNET_STRING* v = getDefaultValue((*i)->getName(), j->second->getName());
			if(v){
				SSFNET_STRING old = j->second->getDefaultValue();
				j->second->getDefaultValue().clear();
				j->second->getDefaultValue().append(*v);
				LOG_INFO("Resetting default value of "<<(*i)->getName()<<"::"<<j->second->getName()<<" from '"<<old<<"' to '"<<j->second->getDefaultValue()<<"'"<<endl);
			}
		}
	}
}

void RuntimeVariables::issueSymbolWarnings() {
	for(Str2StrMap::iterator i = symbols.begin(); i!=symbols.end();i++) {
		if(i->first.size()>0 && i->second.size()>0)
			LOG_WARN("Did not use "<<i->first<<"="<<i->second<<endl);
	}
}


RuntimeVariables::~RuntimeVariables() {
	for(Class2VarMap::iterator i = defaults.begin(); i!=defaults.end();i++) {
		i->second->clear();
		delete i->second;
	}
	defaults.clear();
	symbols.clear();
}



#if TEST_ROUTING == 1

ofstream lookups, hops;
pthread_mutex_t routing_mutex = PTHREAD_MUTEX_INITIALIZER;
bool routing_test_inited=false;

void saveRouteDebug(Packet* pkt, void* droppedAt) {
	if(!routing_test_inited) routing_test_init();
	if(!pkt || !(pkt->route_debug)) LOG_ERROR("WHAT HAPPENED?\n");
	if(pthread_mutex_lock(&routing_mutex)) {
		LOG_ERROR("error with routing_mutex!\n");
	}
	pkt->route_debug->save(pkt->getFinalDestinationUID(),lookups,hops,droppedAt);
	if(pthread_mutex_unlock(&routing_mutex)) {
		LOG_ERROR("error with routing_mutex!\n");
	}
}

void routing_test_init() {
	if(pthread_mutex_lock(&routing_mutex)) {
		LOG_ERROR("error with routing_mutex!\n");
	}

	if(routing_test_inited) return;
#ifdef PRIME_SSF_SYNC_MPI
	char foo[256];
	sprintf(foo,"lookups_part_%i.stats",prime::ssf::ssf_machine_index());
	lookups.open(foo);
	sprintf(foo,"hops_part_%i.stats",prime::ssf::ssf_machine_index());
	hops.open(foo);
#else
	lookups.open("lookups.stats");
	hops.open("hops.stats");
#endif
	routing_test_inited=true;
	if(pthread_mutex_unlock(&routing_mutex)) {
		LOG_ERROR("error with routing_mutex!\n");
	}

}

void routing_test_wrapup() {
	if(pthread_mutex_lock(&routing_mutex)) {
		LOG_ERROR("error with routing_mutex!\n");
	}
	if(!routing_test_inited) return;
	lookups.flush();
	hops.flush();

	lookups.close();
	hops.close();
	routing_test_inited=false;
	if(pthread_mutex_unlock(&routing_mutex)) {
		LOG_ERROR("error with routing_mutex!\n");
	}
}

uint64_t cur_timestamp ()
{
	struct timeval tv;
	gettimeofday(&tv,NULL);
	return 1000000ULL*(tv.tv_sec)+(tv.tv_usec);
}

#endif


} // namespace ssfnet
} // namespace prime
