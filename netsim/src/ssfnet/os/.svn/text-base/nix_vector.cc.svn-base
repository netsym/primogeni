/*
 * \file nix_vector.cc
 * \brief Source file for the nix-vector routing.
 */

#include "os/ssfnet_types.h"
#include "os/logger.h"
#include "os/nix_vector.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(NixVector)

NixVector::NixVector() :
	used(0), cur_vec_used(0), total(0)
{
	nix_vector.push_back(0);
}

NixVector::~NixVector() {}

NixVector* NixVector::copy() {
	return new NixVector(*this);
}

NixVector::NixVector(const NixVector& o) :
	nix_vector(o.nix_vector),
	used(o.used), cur_vec_used(o.cur_vec_used), total(o.total) {}

NixVector& NixVector::operator=(const NixVector& o)
{
	if (this == &o) {
		return *this;
	}
	nix_vector = o.nix_vector;
	used = o.used;
	cur_vec_used = o.cur_vec_used;
	total = o.total;
	return *this;
}

std::ostream& operator <<(std::ostream& os, const NixVector& nix)
{
	nix.dumpNixVector(os);
	return os;
}

void NixVector::addOutboundIfaceIdx (uint32_t outbound_iface_idx, uint32_t num_of_bits)
{
	assert(0 < num_of_bits && num_of_bits <= 32);

	// Check to see if the number of new bits forces the creation of
	// a new entry into the nix vector.
	if (cur_vec_used + num_of_bits > 32) {
		if (cur_vec_used == 32) {
			// can't add any more to this vector, so start a new one.
			nix_vector.push_back(outbound_iface_idx);
			// Reset number of bits in cur_vec_used to be num_of_bits,
			// because we are working with a new entry in the nix vector
			cur_vec_used = num_of_bits;
			total += num_of_bits;
		} else {
			// Fill in the remaining portion of the current vector entry
			//uint32_t temp_bits = outbound_iface_idx;
			uint32_t temp_bits = outbound_iface_idx << cur_vec_used;
			temp_bits |= nix_vector.back();
			nix_vector.back() = temp_bits;
			// Start a new vector entry and push the remaining bits there.
			outbound_iface_idx = outbound_iface_idx >> (32 - cur_vec_used);
			nix_vector.push_back(outbound_iface_idx);
			// Reset number of bits in cur_vec_used for the new vector.
			cur_vec_used = (num_of_bits-(32-cur_vec_used));
			total += num_of_bits;
		}
	} else {
		// Shift over the outbound_iface_idx by the number of current bits.
		// And then logically OR outbound_iface_idx with the present nix,
		// resulting in the new nix vector
		outbound_iface_idx = outbound_iface_idx << cur_vec_used;
		outbound_iface_idx |= nix_vector.back();

		// Insert the new nix vector and increment cur_vec_used and total accordingly
		nix_vector.back() = outbound_iface_idx;
		cur_vec_used += num_of_bits;
		total += num_of_bits;
	}
}

uint32_t NixVector::getOutboundIfaceIdx(uint32_t num_of_bits)
{
	assert(0 < num_of_bits && num_of_bits <= 32);
	assert(num_of_bits <= getRemainingBits());

	//LOG_DEBUG("num_of_bits=" << num_of_bits <<
	//					", total=" << total << ", used=" << used <<
	//					", remaining=" << getRemainingBits()<<
	//					", vector_idx="<<used/32<< endl);

	uint32_t vector_idx = used/32;

	uint32_t low_bit = used%32;
	uint32_t high_bit = (used+num_of_bits)%32;

	//LOG_DEBUG("low_bit=" << low_bit <<
	//			", high_bit=" << high_bit << endl);

	uint32_t extracted_bits = nix_vector.at(vector_idx);;
	if (low_bit < high_bit || (low_bit >= high_bit && high_bit == 0)) { // single vector entry
		if (low_bit >= high_bit)
			high_bit = 32;
		if (high_bit < 32)
			extracted_bits <<= (32 - high_bit);
		if (high_bit - low_bit < 32)
			extracted_bits >>= (32 - high_bit + low_bit);
	} else { // two vector entries
		//LOG_DEBUG("WE NEED TO EXTRACT BITS FROM TWO VECTOR ENTRIES"<<endl)
		assert(low_bit > 0);
		extracted_bits >>= low_bit;
		uint32_t h = nix_vector.at(vector_idx + 1);
		assert(high_bit < 32);
		h <<= (32 - high_bit);
		extracted_bits += (h >> (low_bit - high_bit));
	}

	used += num_of_bits;
	return extracted_bits;
}

uint32_t NixVector::getNumOfBits(uint32_t num_of_ifaces) {
	assert(num_of_ifaces > 0);
	num_of_ifaces--;
	uint32_t num_of_bits = 0;
	do {
		num_of_bits++;
		num_of_ifaces >>= 1;
	} while(num_of_ifaces > 0);
	return num_of_bits;
}

NixVector::NixBits_t NixVector::getNixVector()
{
	return nix_vector;
}

uint32_t NixVector::getRemainingBits()
{
	return (total - used);
}

void NixVector::pack(prime::ssf::ssf_compact* dp)
{
	dp->add_unsigned_int(used);
	dp->add_unsigned_int(cur_vec_used);
	dp->add_unsigned_int(total);
	dp->add_unsigned_int(nix_vector.size());
	/*LOG_DEBUG("PACKED used="<<used<<endl);
	LOG_DEBUG("PACKED cur_vec_used="<<cur_vec_used<<endl);
	LOG_DEBUG("PACKED total="<<total<<endl);
	LOG_DEBUG("PACKED nix_vector.size()="<<nix_vector.size()<<endl);*/
	for(NixBits_t::iterator i=nix_vector.begin();i!=nix_vector.end();i++) {
		//LOG_DEBUG("\tPACKED "<<*i<<endl);
		dp->add_unsigned_int(*i);
	}
}

void NixVector::unpack(prime::ssf::ssf_compact* dp)
{
	dp->get_unsigned_int(&used);
	dp->get_unsigned_int(&cur_vec_used);
	dp->get_unsigned_int(&total);
	uint32_t num=0,tmp=0;
	dp->get_unsigned_int(&num);
	/*LOG_DEBUG("UNPACKED used="<<used<<endl);
	LOG_DEBUG("UNPACKED cur_vec_used="<<cur_vec_used<<endl);
	LOG_DEBUG("UNPACKED total="<<total<<endl);
	LOG_DEBUG("UNPACKED tmp size="<<num<<endl);*/
	nix_vector.clear();
	for(;num>0;num--) {
		dp->get_unsigned_int(&tmp);
		//LOG_DEBUG("\tUNPACKED "<<tmp<<endl);
		nix_vector.push_back(tmp);
	}
	//LOG_DEBUG("PACKED nix_vector.size()="<<nix_vector.size()<<endl);
}

int NixVector::packingSize() const
{
	return (4+nix_vector.size())*sizeof(uint32);
}

void NixVector::dumpNixVector(PRIME_OSTREAM& os) const
{
	uint32_t i = nix_vector.size();
	SSFNET_VECTOR(uint32_t)::const_iterator iter = nix_vector.end();
	do {
		iter--;
		// all this work just to get the nix
		// vector to print out neat
		if (total == 32)
			printDec2BinNixFill(*iter, 32, os);
		else if (total > ((sizeof(uint32_t) * 8) * i))
			printDec2BinNix(*iter, getNumOfBits(*iter), os);
		else
			printDec2BinNixFill(*iter, total % 32, os);
		i--;
		if (iter != nix_vector.begin())
			os << "--";
	} while (iter != nix_vector.begin());
}

void NixVector::printDec2BinNix(uint32_t decimalNum, uint32_t num_of_bits,
		std::ostream &os) const {
	if (decimalNum == 0) {
		os << 0;
		return;
	}
	if (decimalNum == 1) {
		for (; num_of_bits > 1; num_of_bits--)
			os << 0;
		os << 1;
	} else {
		printDec2BinNix(decimalNum / 2, num_of_bits - 1, os);
		os << decimalNum % 2;
	}
}

void NixVector::printDec2BinNixFill(uint32_t decimalNum, uint32_t num_of_bits,
		std::ostream &os) const {
	if (decimalNum == 0) {
		os << 0;
		return;
	}
	if (decimalNum == 1) {
		// check to see if we need to
		// print out some zeros at the
		// beginning of the nix vector
		if ((uint32_t) (sizeof(uint32_t) * 8) > num_of_bits) {
			for (uint32_t i = ((sizeof(uint32_t) * 8) - num_of_bits); i > 0; i--)
				os << 0;
		}
		os << 1;
	} else {
		printDec2BinNixFill(decimalNum / 2, num_of_bits, os);
		os << decimalNum % 2;
	}
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, NixVector& v)
{
	os<<"[";
	v.dumpNixVector(os);
	return os<<", pos="<<v.used<<",total_remaining_bits="<<v.getRemainingBits()<<"]";
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, NixVector* v)
{
	if(v)	return os<<*v;
	return os<<"[NULL]";
}

} // namespace ssfnet
} // namespace prime
