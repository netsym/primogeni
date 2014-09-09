/**
 * \file ssfnet_types.h
 * \brief Header file for Abstract Data Types designed to be shared-memory safe.
 *
 * STL containers are _NOT_ by default thread safe:
 *   "The SGI implementation of STL is thread-safe only in the sense that simultaneous
 *    accesses to distinct containers are safe, and simultaneous read accesses to to
 *    shared containers are safe. If multiple threads access a single container,
 *    and at least one thread may potentially write, then the user is responsible for
 *    ensuring mutual exclusion between the threads during the container accesses."
 *  You have been warned.
 *
 * \author Nathanael Van Vorst
 * \author Jason Liu
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

#ifndef __SSFNET_TYPES_H__
#define __SSFNET_TYPES_H__


#include <stdint.h>
#include <iostream>
#include <sstream>
#include <algorithm>
#include <cstddef>
#include <cstdlib>
#include <deque>
#include <list>
#include <map>
#include <set>
#include <stack>
#include <vector>
#include "ssf.h"
#include "os/ssfnet_exception.h"

#include <sys/time.h>

namespace prime {
namespace ssfnet {

inline long natetime()
{
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec*1000 + t.tv_usec/1000.0;
}
unsigned long long nateorder_1();
unsigned long long nateorder_2();

//#define ACK_COUT(X, node) fprintf(stdout,"NATE[%s:%d][%ld][%llu][%llu]%s\n",__FILE__,__LINE__,natetime(),nateorder_1(),(node?node->getUID():0L),X );
//#define ACK_CERR(X, node) fprintf(stderr,"NATE[%s:%d][%ld][%llu][%llu]%s\n",__FILE__,__LINE__,natetime(),nateorder_2(),(node?node->getUID():0L),X );
#define ACK_COUT(X, Y) {}
#define ACK_CERR(X, Y) {}

#if SSFNET_DEBUG
#define SSFNET_DYNAMIC_CAST(a,b) dynamic_cast<a>(b)
#define SSFNET_STATIC_CAST(a,b) static_cast<a>(b)
#else
#define SSFNET_DYNAMIC_CAST(a,b) ((a)b)
#define SSFNET_STATIC_CAST(a,b) ((a)b)
#endif

/** min, max, and abs operators */
#define mymin(a, b) (((a) < (b)) ? (a) : (b))
#define mymax(a, b) (((a) > (b)) ? (a) : (b))
#define myabs(a)    (((a) > 0.0) ? (a) : -(a))

/** make a duplicate string in shared memory */
extern byte* sstrdup(const byte* s);

/** compute 16-bit checksum */
extern uint16 cksum(uint16* addr, int len, uint16 accu = 0);
extern int fast_cksum_a(uint16* addr, int len);


/** endian conversion */
#if defined(WORDS_BIGENDIAN)
#define myhtons(A) (A)
#define myhtonl(A) (A)
#define myntohs(A) (A)
#define myntohl(A) (A)
#define myntohlL(A) (A)
#else
#define myhtons(A) ((((uint16)(A) & 0xff00) >> 8) | \
		(((uint16)(A) & 0x00ff) << 8))
#define myhtonl(A) ((((uint32)(A) & 0xff000000) >> 24) | \
		(((uint32)(A) & 0x00ff0000) >> 8) |  \
		(((uint32)(A) & 0x0000ff00) << 8) |  \
		(((uint32)(A) & 0x000000ff) << 24))
#define myhtonll(A) (  ( ((uint64)myhtonl((uint32)(A & 0xFFFFFFFF)))  << 32) | \
                       ( myhtonl((uint32)(A >> 32))) )

#define myntohs myhtons
#define myntohl myhtonl
#define myntohll myhtonll
#endif

/* some primitive types */
typedef prime::ssf::uint64 UID_t;
typedef prime::ssf::int8 int8;
typedef prime::ssf::uint8 uint8;
typedef prime::ssf::int16 int16;
typedef prime::ssf::uint16 uint16;
typedef prime::ssf::int32 int32;
typedef prime::ssf::uint32 uint32;
typedef prime::ssf::int64 int64;
typedef prime::ssf::uint64 uint64;
typedef prime::ssf::int8 int8_t;
typedef prime::ssf::uint8 uint8_t;
typedef prime::ssf::int16 int16_t;
typedef prime::ssf::uint16 uint16_t;
typedef prime::ssf::int32 int32_t;
typedef prime::ssf::uint32 uint32_t;
typedef prime::ssf::int64 int64_t;
typedef prime::ssf::uint64 uint64_t;

typedef prime::ssf::byte byte;
typedef prime::ssf::uint32 word;

typedef unsigned long ulong;
typedef unsigned int uint;

typedef void general_data;

/* FIXME: need wrappers for stringstream , ostream , and istream */
typedef std::ifstream PRIME_IFSTREAM;
typedef std::ofstream PRIME_OFSTREAM;
typedef std::istream PRIME_ISTREAM;
typedef std::ostream PRIME_OSTREAM;
typedef std::stringstream PRIME_STRING_STREAM;
typedef std::istringstream PRIME_ISTRING_STREAM;
/** std::string in shared memory. */
typedef SSF_STRING SSFNET_STRING;

/** std::multimap<x,y> in shared memory. */
#define SSFNET_MULTIMAP(x,y) SSF_MULTIMAP(x,y)

/** std::map<x,y> in shared memory. */
#define SSFNET_MAP(x,y) SSF_MAP(x,y)

/** std::set<x> in shared memory. */
#define SSFNET_SET(x) SSF_SET(x)

/** std::vector<x> in shared memory. */
#define SSFNET_VECTOR(x) SSF_VECTOR(x)

/** std::deque<x> in shared memory. */
#define SSFNET_DEQUE(x) SSF_DEQUE(x)

/** std::priority_queue<T,S,C > in shared memory. */
//XXX
//#define SSFNET_PRIORITY_QUEUE(T,S,C) SSF_PRIORITY_QUEUE(T,S,C)
#define SSFNET_PRIORITY_QUEUE(T,S,C) std::priority_queue<T,S,C >

/** std::make_pair<x,y> in shared memory. */
#define SSFNET_MAKE_PAIR(x,y) SSF_MAKE_PAIR(x,y)

/** std::pair<x,y> in shared memory. */
#define SSFNET_PAIR(x,y) SSF_PAIR(x,y)

/** std::stack<x, std::deque<x> > in shared memory. */
#define SSFNET_STACK(x) std::stack<x, SSF_DEQUE(x) >

// this following macro is defined from ssf.h to indicate whether the
// heap segment is shared by multiple processors. If it is, there's no
// shared-memory layer and therefore no need for a separate memory
// allocator; otherwise, we use the memory allocator SSF_ALLOCATOR
// that can deal with the shared memory.
#if SSF_SHARED_HEAP_SEGMENT

/** std::list<x> in shared memory. */
#define SSFNET_LIST(x) std::list<x >

/** std::hash_map<x,y> in shared memory. */
#define SSFNET_HASH_MAP(x,y) std::hash_map<x, y, std::hash<x >, std::equal_to<x > >

/** std::multimap<x,y,z> in shared memory (with redefinition of the
 compare function). */
#define SSFNET_MULTIMAP_CMP(x,y,z) std::multimap<x, y, z >

/** std::map<x,y,z> in shared memory (with redefinition of the compare
 function). */
#define SSFNET_MAP_CMP(x,y,z)      std::map<x, y, z >

/** std::set<x,z> in shared memory (with redefinition of the compare
 function). */
#define SSFNET_SET_CMP(x,z)        std::set<x, z >

#else /* !SSF_SHARED_HEAP_SEGMENT */

/** std::list<x> in shared memory. */
#define SSFNET_LIST(x) std::list<x, prime::ssf::SSF_ALLOCATOR<x > >

/** std::hash_map<x,y> in shared memory. */
#define SSFNET_HASH_MAP(x,y) std::hash_map<x, y, std::hash<x >, \
		std::equal_to<x >, prime::ssf::SSF_ALLOCATOR<std::pair<x,y > > >

/** std::multimap<x,y,z> in shared memory (with redefinition of the
 compare function). */
#define SSFNET_MULTIMAP_CMP(x,y,z) std::multimap<x, y, z, \
		prime::ssf::SSF_ALLOCATOR<std::pair<x,y > > >

/** std::map<x,y,z> in shared memory (with redefinition of the compare
 function). */
#define SSFNET_MAP_CMP(x,y,z)      std::map<x, y, z, \
		prime::ssf::SSF_ALLOCATOR<std::pair<x,y > > >

/** std::set<x,z> in shared memory (with redefinition of the compare
 function). */
#define SSFNET_SET_CMP(x,z)        std::set<x, z, \
		prime::ssf::SSF_ALLOCATOR<x > >

#endif

/** A pair of integers. */
typedef SSF_PAIR(int,int) SSFNET_INT_PAIR;

/** A vector of strings. */
typedef SSFNET_VECTOR(SSFNET_STRING) SSFNET_STRING_VECTOR;

/** A vector of pointers. */
typedef SSFNET_VECTOR(void*) SSFNET_POINTER_VECTOR;

/** A map from an integer to a pointer. */
typedef SSFNET_MAP(int, void*) SSFNET_INT2PTR_MAP;

/** A vector of long integers. */
typedef SSFNET_VECTOR(long) SSFNET_LONG_VECTOR;

/** A set of long integers. */
typedef SSFNET_SET(long) SSFNET_LONG_SET;

/** UIDVec. */
typedef SSFNET_VECTOR(UID_t) UIDVec;

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const SSFNET_STRING* x);
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, SSFNET_STRING* x);
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const char* x);
PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, char* x);

class SSFNetEvent: public prime::ssf::Event {
public:
	enum Type {
		SSFNET_EVT_MIN=100,
		SSFNET_PKT_EVT,
		SSFNET_EMU_NORMAL_EVT,
		SSFNET_EMU_PROXY_EVT,
		SSFNET_ARP_EVT,
		SSFNET_TRAFFIC_MIN_TYPE,
		//---- start traffic evts
		//generic traffic
		SSFNET_TRAFFIC_START_EVT,
		SSFNET_TRAFFIC_UPDATE_EVT,
		SSFNET_TRAFFIC_FINISH_EVT,
		//swing traffic
		SSFNET_TRAFFIC_SWING_START_EVT,
		SSFNET_TRAFFIC_SWING_FINISH_EVT,
		//ppbp traffic
		SSFNET_TRAFFIC_PPBP_START_EVT,
		//http traffic
		SSFNET_TRAFFIC_HTTP_START_EVT,
		SSFNET_TRAFFIC_HTTP_FINISH_EVT,
		//cnf traffic
		SSFNET_TRAFFIC_CNF_START_EVT,
		//---- end traffic evts
		SSFNET_TRAFFIC_MAX_TYPE,
	    SSFNET_FLUID_REGISTER_EVT,
		SSFNET_FLUID_REGISTER_REVERSE_EVT,
	    SSFNET_FLUID_UNREGISTER_EVT,
	    SSFNET_FLUID_ARRIVAL_EVT,
	    SSFNET_FLUID_ACCU_DELAY_EVT,
	    SSFNET_FLUID_ACCU_LOSS_EVT,
		SSFNET_NAME_SRV_EVT,
		SSFNET_ROUTING_IFACE_STATUS_REQUEST_EVT,
		SSFNET_ROUTING_IFACE_STATUS_RESPONSE_EVT,
		SSFNET_GHOST_ROUTING_SPHERE_REG_EVT,
		SSFNET_GHOST_ROUTING_SPHERE_REG_RESPONSE_EVT,
		SSFNET_STATE_UPDATE_EVT,
		SSFNET_STATE_SET_EVT,
		SSFNET_STATE_FETCH_EVT,
		SSFNET_CREATE_NODE_EVT,
		SSFNET_AREA_OF_INTEREST_UPDATE,
		SSFNET_VIZ_EXPORT_UPDATE,
		SSFNET_EVT_MAX
	};
	/** The destructor. */
	virtual ~SSFNetEvent();

	/** The constructor. */
	SSFNetEvent(SSFNetEvent::Type _ssfnet_evt_type, int _target_com_id=-1);

	/** The copy constructor. */
	SSFNetEvent(const SSFNetEvent& evt);

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone() = 0;

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine across the
	 * memory boundaries. It is actually used to serialize the event
	 * object into a byte stream represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/** Unpack the packet event, reverse process of the pack method. */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * Get the type of this event
	 */
	SSFNetEvent::Type getSSFNetEventType() const;

	/**
	 * Set the type of this event
	 */
	void setSSFNetEventType(SSFNetEvent::Type  t);


	/** set the community this event is destined for **/
	void setTargetCommunity(int com_id);

	/** get the community this event is destined for **/
	int getTargetCommunity() const;

	/**
	 * Instead of using delete to free SSFNet events we need to call
	 * this method. This method figures out if the event is owned by
	 * ssf or not and takes the appropriate action.
	 */
	void free();
private:

	SSFNetEvent::Type ssfnet_evt_type;
	int target_com_id;
};


/**
 * \brief An abstract class for enumeration type.
 *
 * This abstract class for enumerating or iterating through a list of
 * things. A similar definition is in prime::dml namespace.
 */
template<typename T>
class Enumeration {
public:
	/// The constructor.
	Enumeration() {
	}

	/// The destructor.
	virtual ~Enumeration() {
	}

	/// Returns true (non-zero) if there are more elements to be enumerated.
	virtual bool hasMoreElements() = 0;

	/// Returns the next elements in the enumeration.
	virtual T nextElement() = 0;
};

/**
 * \brief XXX IPAddress
 *
 * XXX
 */
class IPAddress : public ssf::ssf_quickobj {
public:
	typedef SSFNET_LIST(IPAddress*) List;
	typedef SSFNET_VECTOR(IPAddress*) Vector;

	/** Reserved IPv4 addresses. */
	enum {
		IPADDR_INVALID = 0, ///< invalid IP address (0.0.0.0)
				IPADDR_ANYDEST = 0xffffffff, ///< broadcast ip address (255.255.255.255)
				IPADDR_LOOPBACK = 0x7f000001,
				///< loopback ip address (127.0.0.1)
	};

	IPAddress(uint32 addr = IPADDR_INVALID) :
		ipaddr(addr) {
	}

	inline operator int() const {
		return ipaddr;
	}
	inline operator uint32() const {
		return ipaddr;
	}

	inline IPAddress operator=(const IPAddress& x) {
		ipaddr = x.ipaddr;
		return *this;
	}
	inline IPAddress operator=(const char* addr) {
		SSFNET_STRING str(addr);
		fromString(str);
		return *this;
	}
	inline 	IPAddress operator=(const SSFNET_STRING& addr) {
		fromString(addr);
		return *this;
	}
	inline IPAddress operator=(const int x) {
		ipaddr = x;
		return *this;
	}
	inline IPAddress operator=(const uint32 x) {
		ipaddr = x;
		return *this;
	}
	inline bool operator==(const IPAddress rhs) const {
		return ipaddr == rhs.ipaddr;
	}
	inline bool operator!=(const IPAddress rhs) const {
		return ipaddr != rhs.ipaddr;
	}
	inline bool operator<(const IPAddress rhs) const {
		return ipaddr < rhs.ipaddr;
	}
	inline bool operator>(const IPAddress rhs) const {
		return ipaddr > rhs.ipaddr;
	}
	inline bool operator<=(const IPAddress rhs) const {
		return ipaddr <= rhs.ipaddr;
	}
	inline bool operator>=(const IPAddress rhs) const {
		return ipaddr >= rhs.ipaddr;
	}

	friend int operator==(const IPAddress& x, const int y) {
		return x.ipaddr == (uint32) y;
	}
	friend int operator==(const int x, const IPAddress& y) {
		return (uint32) x == y.ipaddr;
	}
	friend int operator==(const IPAddress& x, const uint32 y) {
		return x.ipaddr == y;
	}
	friend int operator==(const uint32 x, const IPAddress& y) {
		return x == y.ipaddr;
	}

	friend int operator!=(const IPAddress& x, const int y) {
		return x.ipaddr != (uint32) y;
	}
	friend int operator!=(const int x, const IPAddress& y) {
		return (uint32) x != y.ipaddr;
	}
	friend int operator!=(const IPAddress& x, const uint32 y) {
		return x.ipaddr != y;
	}
	friend int operator!=(const uint32 x, const IPAddress& y) {
		return x != y.ipaddr;
	}

	SSFNET_STRING toString() const;
	void fromString(const SSFNET_STRING& ipstr);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPAddress& x);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPAddress* x);

protected:
	uint32 ipaddr;
};

/**
 * \brief IP prefix, such as 10.10.0.0/16.
 */
class IPPrefix {
public:
	typedef SSFNET_LIST(IPPrefix) List;
	/** The constructor. */
	IPPrefix(IPAddress ipaddr = IPAddress::IPADDR_INVALID, int iplen = 32) :
		addr(ipaddr), len(iplen) {
	}

	/** The copy constructor. */
	IPPrefix(const IPPrefix& p) :
		addr(p.addr), len(p.len) {
	}

	/** Return the ip address of the prefix. */
	const IPAddress& getAddress() const {
		return addr;
	}

	/** Return the IP prefix length (the number of signficant bits). */
	int getLength() const {
		return len;
	}

	/** Set the ip address of this prefix. */
	void setAddress(const IPAddress& ipaddr) {
		addr = ipaddr;
	}

	/** Set the length of this IP prefix. */
	void setLength(int length) {
		len = length;
	}

	/** The assignment operators. */
	IPPrefix& operator=(const IPPrefix& rhs) {
		addr = rhs.addr;
		len = rhs.len;
		return *this;
	}
	IPPrefix& operator=(const SSFNET_STRING& ipstr) {
		fromString(ipstr);
		return *this;
	}

	/** Returns true if the given IP prefix is contained in the subnet
	 specified by this prefix. */
	bool contains(IPPrefix& ippref);

	/** Returns true if the particular IP address is contained in the
	 subnet specified by this prefix. */
	bool contains(IPAddress ipaddr);

	/** The comparison operators. */
	friend bool operator==(const IPPrefix& ip1, const IPPrefix& ip2);
	friend bool operator!=(const IPPrefix& ip1, const IPPrefix& ip2);

	SSFNET_STRING toString() const;
	void fromString(const SSFNET_STRING& ipstr);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefix& x);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefix* x);

	static uint32 getMask(int len) {
		return masks[len];
	}

private:
	/** The network address of the prefix. */
	IPAddress addr;

	/** The length of the mask. */
	int len;

	/** A static lookup table to calculate subnet masks. */
	static uint32 masks[33];
};

/**
 * \brief XXX MACAddress
 *
 * XXX
 */
class MACAddress : public ssf::ssf_quickobj {
public:
	typedef SSFNET_LIST(MACAddress*) List;
	typedef SSFNET_VECTOR(MACAddress*) Vector;

	static MACAddress MACADDR_INVALID; ///< invalid MAC address (00:00:00:00:00:00)
	static MACAddress MACADDR_BROADCAST_11; ///< braodcast MAC address (11:11:11:11:11:11)
	static MACAddress MACADDR_BROADCAST_FF; ///< braodcast MAC address (ff:ff:ff:ff:ff:ff)

	MACAddress();
	MACAddress(SSFNET_STRING addr);
	MACAddress(UID_t uid);
	MACAddress(uint8_t* addr);

	inline operator uint64() const {
		return macaddr;
	}

	MACAddress& operator=(const MACAddress& x);
	MACAddress& operator=(const char* addr);
	MACAddress& operator=(const SSFNET_STRING& addr);
	MACAddress& operator=(const uint8_t* addr);

	friend int operator==(const MACAddress& x, const MACAddress& y);
	friend int operator!=(const MACAddress& x, const MACAddress& y);

	SSFNET_STRING toString() const;
	void toRawMac(uint8* raw_mac) const;
	void fromString(const SSFNET_STRING& macstr);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const MACAddress& x);

	bool operator <(const MACAddress& mac) const;
	bool operator <(const MACAddress*& mac) const;

private:
	uint64_t macaddr;
};


/**
 * \brief A generic data type for data stored in trie.
 *
 * This class is used by the Trie class as a base class to store data. It is stored here so we can define the IPPrefixRoute here.
 */
class TrieData {
public:
	/** The virtual destructor does nothing here. */
	virtual ~TrieData() {};
};


/**
 * \brief binds an ip-prefix to an interface which is a traffic portal
 */
class IPPrefixRoute : public TrieData {
public:
	typedef SSFNET_LIST(IPPrefixRoute) List;
	/** The constructor. */
	IPPrefixRoute(IPAddress prefix_ = IPAddress::IPADDR_INVALID, int prefix_len = 32, UID_t portal_uid_=0, UID_t portal_iface_uid_=0, IPAddress portal_ip_= IPAddress::IPADDR_INVALID, MACAddress portal_mac_ = MACAddress::MACADDR_INVALID) :
		prefix(IPPrefix(prefix_,prefix_len)), portal_uid(portal_uid_), portal_iface_uid(portal_iface_uid_), portal_ip(portal_ip_), portal_mac(portal_mac_) {
	}
	IPPrefixRoute(IPPrefix prefix_, UID_t portal_uid_, UID_t portal_iface_uid_, IPAddress portal_ip_, MACAddress portal_mac_) :
		prefix(prefix_), portal_uid(portal_uid_), portal_iface_uid(portal_iface_uid_), portal_ip(portal_ip_), portal_mac(portal_mac_) {
	}
	/** The copy constructor. */
	IPPrefixRoute(const IPPrefixRoute& p) :
		prefix(p.prefix), portal_uid(p.portal_uid), portal_iface_uid(p.portal_iface_uid), portal_ip(p.portal_ip), portal_mac(p.portal_mac) {
	}

	virtual ~IPPrefixRoute() { }

	/** Return the ip address of the prefix. */
	const IPAddress& getAddress() const {
		return prefix.getAddress();
	}

	/** Return the IP prefix length (the number of signficant bits). */
	int getLength() const {
		return prefix.getLength();
	}

	/** Return the prefix. */
	const IPPrefix& getPrefix() const {
		return prefix;
	}

	/** return the uid of the host/router with the portal iface */
	const UID_t getPortalUID() const {
		return portal_uid;
	}

	/** return the uid of the iface which is the portal */
	const UID_t getPortalIfaceUID() const {
		return portal_iface_uid;
	}

	/** return the IP of the iface which is the portal */
	const IPAddress getPortalIfaceIP() const {
		return portal_ip;
	}

	/** return the MAC of the iface which is the portal */
	const MACAddress getPortalIfaceMAC() const {
		return portal_mac;
	}

	/** Set the prefix. */
	void setPrefix(const IPPrefix& prefix_) {
		prefix = prefix_;
	}

	/** Set the portal uid. */
	void setPortalUID(const UID_t& uid) {
		portal_uid = uid;
	}

	/** Set the portal_iface_uid. */
	void setPortalIfaceUID(const UID_t& uid) {
		portal_iface_uid = uid;
	}

	/** Set the portal_ip. */
	void setPortalIP(const IPAddress& ip) {
		portal_ip = ip;
	}

	/** Set the portal_mac. */
	void setPortalMAC(const MACAddress& mac) {
		portal_mac = mac;
	}

	/** The assignment operators. */
	IPPrefixRoute& operator=(const IPPrefixRoute& rhs) {
		prefix=rhs.prefix;
		portal_uid=rhs.portal_uid;
		portal_iface_uid=rhs.portal_iface_uid;
		portal_ip=rhs.portal_ip;
		return *this;
	}

	/** The comparison operators. */
	friend bool operator==(const IPPrefixRoute& ip1, const IPPrefixRoute& ip2);
	friend bool operator!=(const IPPrefixRoute& ip1, const IPPrefixRoute& ip2);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefixRoute& x);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const IPPrefixRoute* x);

private:
	IPPrefix prefix;
	UID_t portal_uid;
	UID_t portal_iface_uid;
	IPAddress portal_ip;
	MACAddress portal_mac;
};

class UIDRange {
public:
	typedef SSFNET_LIST(UIDRange) List;
	UIDRange(UID_t _lower, UID_t _upper) :
		lower(_lower), upper(_upper) {
	}
	UIDRange(const UIDRange& o) :
		lower(o.lower), upper(o.upper) {
	}
	const UID_t lower, upper;
	static int compare(const UIDRange lhs, const UIDRange rhs);

	inline bool operator==(const UIDRange rhs) const {
		return compare(*this, rhs) == 0;
	}
	inline bool operator==(const UIDRange rhs) {
		return compare(*this, rhs) == 0;
	}

	inline bool operator!=(const UIDRange rhs) const {
		return compare(*this, rhs) != 0;
	}
	inline bool operator!=(const UIDRange rhs) {
		return compare(*this, rhs) != 0;
	}

	inline bool operator<(const UIDRange rhs) const {
		return compare(*this, rhs) < 0;
	}
	inline bool operator<(const UIDRange rhs) {
		return compare(*this, rhs) < 0;
	}

	inline bool operator>(const UIDRange rhs) const {
		return compare(*this, rhs) > 0;
	}
	inline bool operator>(const UIDRange rhs) {
		return compare(*this, rhs) > 0;
	}

	inline bool operator<=(const UIDRange rhs) const {
		return compare(*this, rhs) <= 0;
	}
	inline bool operator<=(const UIDRange rhs) {
		return compare(*this, rhs) <= 0;
	}

	inline bool operator>=(const UIDRange rhs) const {
		return compare(*this, rhs) >= 0;
	}
	inline bool operator>=(const UIDRange rhs) {
		return compare(*this, rhs) >= 0;
	}
};

static const SSFNET_STRING __GetTypeNameStr__("_Get_TypeName =");
/**
 * This is a hack which only works for GCC which allows to get a pretty string name of
 * a template type
 */
template<typename _Get_TypeName>
const SSFNET_STRING getTemplateTypeAsString() {
	SSFNET_STRING name = __PRETTY_FUNCTION__;
	size_t begin = name.find(__GetTypeNameStr__.c_str())
					+ __GetTypeNameStr__.size() + 1;
	size_t length = name.find("]", begin) - begin;
	name = name.substr(begin, length);
	return name;
}

void warn_default_rw_config_var(SSFNET_STRING dt, SSFNET_STRING st);

/** these functions are used by configvars to set/get values using strings **/
/**
 * XXX
 */
template<typename DT, typename ST> bool rw_config_var(DT& dst, ST& src) {
	warn_default_rw_config_var(getTemplateTypeAsString<DT> (), getTemplateTypeAsString<ST> ());
	dst = src;
	return false;
}

template<typename DT, typename ST> bool rw_config_var(double& dst, double& src) {
	dst = src;
	return false;
}

template<typename DT, typename ST> bool rw_config_var(double& dst, ST& src) {
	warn_default_rw_config_var(SSFNET_STRING("double"), getTemplateTypeAsString<ST> ());
	dst = 0;
	return true;
}

//when the src and dst types are the same
template<typename DT, typename ST> bool rw_config_var(DT& dst, DT& src) {
	dst = src;
	return false;
}

//when the src and dst types are the same (but the src is const)
template<typename DT, typename ST> bool rw_config_var(DT& dst, const DT& src) {
	dst = src;
	return false;
}

class LogLvl;

template<> bool rw_config_var<double, float> (double& dst, float& src);
template<> bool rw_config_var<double, int8_t> (double& dst, int8_t& src);
template<> bool rw_config_var<double, int16_t> (double& dst, int16_t& src);
template<> bool rw_config_var<double, int32_t> (double& dst, int32_t& src);
template<> bool rw_config_var<double, int64_t> (double& dst, int64_t& src);
template<> bool rw_config_var<double, uint8_t> (double& dst, uint8_t& src);
template<> bool rw_config_var<double, uint16_t> (double& dst, uint16_t& src);
template<> bool rw_config_var<double, uint32_t> (double& dst, uint32_t& src);
template<> bool rw_config_var<double, uint64_t> (double& dst, uint64_t& src);


template<> bool rw_config_var<SSFNET_STRING, SSFNET_STRING> (
		SSFNET_STRING& dst, SSFNET_STRING& src);

template<> bool rw_config_var<SSFNET_STRING, void*> (SSFNET_STRING& dst,
		void*& src);

template<> bool rw_config_var<SSFNET_STRING, float> (SSFNET_STRING& dst,
		float& src);
template<> bool rw_config_var<float, SSFNET_STRING> (float& dst,
		SSFNET_STRING& src);

template<> bool rw_config_var<SSFNET_STRING, double> (SSFNET_STRING& dst,
		double& src);
template<> bool rw_config_var<double, SSFNET_STRING> (double& dst,
		SSFNET_STRING& src);

template<> bool rw_config_var<SSFNET_STRING, bool> (SSFNET_STRING& dst,
		bool& src);
template<> bool rw_config_var<bool, SSFNET_STRING> (bool& dst,
		SSFNET_STRING& src);

template<> bool rw_config_var<SSFNET_STRING, LogLvl> (SSFNET_STRING& dst,
		LogLvl& src);
template<> bool rw_config_var<LogLvl, SSFNET_STRING> (LogLvl& dst,
		SSFNET_STRING& src);

template<> bool rw_config_var<SSFNET_STRING, IPAddress> (SSFNET_STRING& dst,
		IPAddress& src);
template<> bool rw_config_var<SSFNET_STRING, IPPrefix> (SSFNET_STRING& dst,
		IPPrefix& src);
template<> bool rw_config_var<SSFNET_STRING, MACAddress> (SSFNET_STRING& dst,
		MACAddress& src);

template<> bool rw_config_var<IPAddress, SSFNET_STRING> (IPAddress& dst,
		SSFNET_STRING& src);
template<> bool rw_config_var<IPPrefix, SSFNET_STRING> (IPPrefix& dst,
		SSFNET_STRING& src);
template<> bool rw_config_var<MACAddress, SSFNET_STRING> (MACAddress& dst,
		SSFNET_STRING& src);

template<> bool
rw_config_var<SSFNET_STRING, int8_t> (SSFNET_STRING& dst, int8_t& src);

template<> bool
rw_config_var<int8_t, SSFNET_STRING> (int8_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<SSFNET_STRING, int16_t> (SSFNET_STRING& dst, int16_t& src);

template<> bool
rw_config_var<int16_t, SSFNET_STRING> (int16_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<int32_t, SSFNET_STRING> (int32_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<int32_t, SSFNET_STRING> (int32_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<SSFNET_STRING, uint8_t> (SSFNET_STRING& dst, uint8_t& src);

template<> bool
rw_config_var<SSFNET_STRING, int64_t> (SSFNET_STRING& dst, int64_t& src);

template<> bool
rw_config_var<int64_t, SSFNET_STRING> (int64_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<uint8_t, SSFNET_STRING> (uint8_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<SSFNET_STRING, uint16_t> (SSFNET_STRING& dst, uint16_t& src);

template<> bool
rw_config_var<uint16_t, SSFNET_STRING> (uint16_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<SSFNET_STRING, uint32_t> (SSFNET_STRING& dst, uint32_t& src);

template<> bool
rw_config_var<uint32_t, SSFNET_STRING> (uint32_t& dst, SSFNET_STRING& src);

template<> bool
rw_config_var<SSFNET_STRING, uint64_t> (SSFNET_STRING& dst, uint64_t& src);

template<> bool
rw_config_var<uint64_t, SSFNET_STRING> (uint64_t& dst, SSFNET_STRING& src);

template<> bool rw_config_var<SSFNET_STRING, UIDVec> (SSFNET_STRING& dst, UIDVec& src);

template<> bool rw_config_var<UIDVec, SSFNET_STRING> (UIDVec& dst, SSFNET_STRING& src);



template<> bool
rw_config_var<SSFNET_STRING, IPPrefix::List> (SSFNET_STRING& dst, IPPrefix::List& src);

template<> bool
rw_config_var<IPPrefix::List, SSFNET_STRING> (IPPrefix::List& dst, SSFNET_STRING& src);


template<> bool
rw_config_var<SSFNET_STRING, IPPrefixRoute::List> (SSFNET_STRING& dst, IPPrefixRoute::List& src);

template<> bool
rw_config_var<IPPrefixRoute::List, SSFNET_STRING> (IPPrefixRoute::List& dst, SSFNET_STRING& src);


class RuntimeVariables {
public:
	typedef SSFNET_MAP(SSFNET_STRING, SSFNET_STRING) Str2StrMap;
	typedef SSFNET_MAP(SSFNET_STRING, Str2StrMap*) Class2VarMap;
	RuntimeVariables(const char* filename);
	RuntimeVariables();
	~RuntimeVariables();
	SSFNET_STRING* getSymbolValue(const SSFNET_STRING& name);
	SSFNET_STRING* getDefaultValue(const SSFNET_STRING& cls, const SSFNET_STRING& attr);
	void initDefaults();
	void issueSymbolWarnings();
private:
	Class2VarMap defaults;
	Str2StrMap symbols;
};


#if TEST_ROUTING == 1
class Packet;
void saveRouteDebug(Packet* pkt, void* droppedAt);
void routing_test_init();
void routing_test_wrapup();
uint64_t cur_timestamp();
#endif


} // namespace ssfnet
} // namespace prime

#endif /*__SSFNET_TYPES_H__*/
