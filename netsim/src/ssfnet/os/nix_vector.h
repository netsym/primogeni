/**
 * \file nix_vector.h
 * \brief Header file for the nix-vector routing.
 *
 * This code is modeled after the code found at:
 * http://code.nsnam.org/jpelkey3/ns-3-nix-vector-routing/file/2c6d94b0f60d/src/common/nix-vector.cc,
 * The found code was unfortunately error ridden to be usable as is;
 * we modified it for it to function properly for our use.
 */

#ifndef __NIX_VECTOR_H__
#define __NIX_VECTOR_H__

#include "ssf.h"
#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

/**
 * \brief The data structure for nix-vector routing.
 *
 * A nix vector uses a vector to store the outbound interface indices.
 * The index of a network interface is actually the rank of the
 * interface in terms of its host. Each element of the vector is
 * 32-bit long and may pack as many entries (i.e., indices) as
 * possible. The data structure is designed so that the nix vector can
 * grow arbitrarily long if so required.
 */

class NixVector : public ssf::ssf_quickobj {
public:
	typedef SSFNET_VECTOR(uint32_t) NixBits_t;

	/** The default constructor. */
	NixVector();

	/** The copy constructor. */
	NixVector(const NixVector&);

	/** The destructor. */
	~NixVector();

	/** The assignment operator. */
	NixVector& operator=(const NixVector &);

	/**
	 * This method creates and returns a copy of this nix vector. It is
	 * called when we reuse the nix vector in cache.
	 */
	NixVector* copy();

	/**
	 * This method adds an outbound interface index to the nix vector.
	 *
	 * \param outbound_iface_idx is rank of the outbound interface in
	 * terms of its host;

	 * \param num_of_bits is the maximum number of bits that the
	 * outbound_iface_idx may contain; it is determined by the size of
	 * the host.
	 *
	 * Note: This method assumes that the number of bits to be added is
	 * always no larger than 32, i.e., one can only add or substract at
	 * most one element to the vector at a time. This is reasonable,
	 * because 32 would allow 2^32 possible neighbors.
	 */
	void addOutboundIfaceIdx(uint32_t outbound_iface_idx, uint32_t num_of_bits);

	/**
	 * This method returns the next outbound interface index.
	 *
	 * \param num_of_bits is the number of bits to be extracted from the
	 * nix vector; one must know a priori the number of bits the
	 * outbound interface index uses in the vector.
	 *
	 * Note: This method assumes that the number of bits to be added is
	 * always no larger than 32, i.e., one can only add or substract at
	 * most one element to the vector at a time. This is reasonable,
	 * because 32 would allow 2^32 possible neighbors.
	 */
	uint32_t getOutboundIfaceIdx(uint32_t num_of_bits);

	/**
	 * This method returns the nix vector as a vector of 32-bit integers.
	 */
	NixBits_t getNixVector();

	/**
	 * This method returns the number of available bits remaining unused
	 * in the nix vector, which is the total number of bits minus the
	 * number of bits used.
	 */
	uint32_t getRemainingBits();

	/**
	 * This method is a static method, which returns the number of bits
	 * used to store the given number of outbound interfaces.
	 *
	 * \param num_of_ifaces is the number of interfaces at the host; we
	 * use size of the host the outbound interface attaches to.
	 */
	static uint32_t getNumOfBits(uint32_t num_of_ifaces);

	/** This method is used for printing of the nix vector for debugging purposes. */
	void dumpNixVector(PRIME_OSTREAM &os) const;

	/* these are auxiliary functions for printing of the nix vector */
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, NixVector& v);
	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, NixVector* v);

	/**
	 * This method packs the data structure into ssf compact format. It
	 * is called by the corresponding packet during serialization before
	 * the packet can be sent across memory boundaries.
	 *
	 * Note: NOT IMPLEMENTED.
	 */
	void pack(prime::ssf::ssf_compact* dp);

	/**
	 * This method unpacks the data structure from ssf compact
	 * format. It is called by the corresponding packet during
	 * de-serialization after the packet has been sent over memory
	 * boundaries.
	 *
	 * Note: NOT IMPLEMENTED.
	 */
	void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * This method returns the number of bytes needed for packing the
	 * data structure; It is used by the simulation program to serialize
	 * the corresponding packet in order to send the packet across
	 * memory boundaries in a distributed simulation environment.
	 *
	 * Note: NOT IMPLEMENTED.
	 */
	int packingSize() const;


private:
	/* The nix vector is stored as a vector of 32-bit integers. */
	NixBits_t nix_vector;

	/* The number of used bits, for tracking where we are in the nix vector. */
	uint32_t used;

	/* The number of bits used in the last element of the vector; it is
		 needed to detect when to expand the vector if the remaining bits
		 are not enough for the new entry. */
	uint32_t cur_vec_used;

	/* Counting the total number of bits in the nix vector. */
	uint32_t total;

	/* Used for pretty printing the nix vector for debugging purposes. */
	void printDec2BinNixFill(uint32_t, uint32_t, PRIME_OSTREAM &os) const;
	void printDec2BinNix(uint32_t, uint32_t, PRIME_OSTREAM &os) const;
};

} // namespace ssfnet
} // namespace prime

#endif /*__NIX_VECTOR_H__*/
