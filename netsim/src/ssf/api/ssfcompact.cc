/*
 * ssfcompact :- data serialization.
 *
 * Data serialization is needed for message passing between
 * heterogeneous machine architectures (with different endian types)
 * or for collecting statistics that are stored in a platform
 * independent file. We encourage the user to use the (boost)
 * serialization library (libserial.a) directly whenever
 * possible. This module serves as a simple (and more flexible)
 * interface to data serialization.
 */

#include <assert.h>
#include <stdio.h>
#include <string.h>
#ifndef PRIME_SSF_ARCH_WINDOWS
#include <unistd.h>
#endif

#include "api/ssfcompact.h"
#include "api/ssfexception.h"

namespace prime {
namespace ssf {

static int compact_make_alignment(int offset, int size) {
	int align;
	if (size == 1)
		align = 1;
	else if (size <= 2)
		align = 2;
	else if (size <= 4)
		align = 4;
	else if (size <= 8)
		align = 8;
	else if (size <= 16)
		align = 16;
	else if (size <= 32)
		align = 32;
	else if (size <= 64)
		align = 64;
	else
		align = 128;
	return ((offset + (align - 1)) & (~(align - 1)));
}

ssf_compact::ssf_compact() :
	compact_record(0), compact_data(0) {
	compact_reset();
}

ssf_compact::~ssf_compact() {
	compact_reset();
}

void ssf_compact::add_float(float val) {
	add_float_array(1, &val);
}

void ssf_compact::add_double(double val) {
	add_double_array(1, &val);
}

#ifdef HAVE_LONG_DOUBLE
void ssf_compact::add_long_double(long double val) {add_long_double_array(1, &val);}
#endif

void ssf_compact::add_char(char val) {
	add_char_array(1, &val);
}

void ssf_compact::add_unsigned_char(unsigned char val) {
	add_unsigned_char_array(1, &val);
}

void ssf_compact::add_short(short val) {
	add_short_array(1, &val);
}

void ssf_compact::add_unsigned_short(unsigned short val) {
	add_unsigned_short_array(1, &val);
}

void ssf_compact::add_int(int val) {
	add_int_array(1, &val);
}

void ssf_compact::add_unsigned_int(unsigned int val) {
	add_unsigned_int_array(1, &val);
}

void ssf_compact::add_long(long val) {
	add_long_array(1, &val);
}

void ssf_compact::add_unsigned_long(unsigned long val) {
	add_unsigned_long_array(1, &val);
}

void ssf_compact::add_long_long(SSF_LONG_LONG val) {
	add_long_long_array(1, &val);
}

void ssf_compact::add_unsigned_long_long(SSF_UNSIGNED_LONG_LONG val) {
	add_unsigned_long_long_array(1, &val);
}

void ssf_compact::add_ltime(ltime_t val) {
	add_ltime_array(1, &val);
}

void ssf_compact::add_float_array(int size, float* vals) {
	assert(sizeof(float) == 4); // FIXME: assuming IEEE 754 compliant; check should be done at configuration/compilation time
	compact_add_record(TYPE_FLOAT, size);
	compact_append_raw(size, sizeof(float), (unsigned char*) vals);
}

void ssf_compact::add_double_array(int size, double* vals) {
	assert(sizeof(double) == 8); // FIXME: assuming IEEE 754 compliant; check should be done at configuration/compilation time
	compact_add_record(TYPE_DOUBLE, size);
	compact_append_raw(size, sizeof(double), (unsigned char*) vals);
}

#ifdef HAVE_LONG_DOUBLE
void ssf_compact::add_long_double_array(int size, long double* vals) {
	assert(sizeof(long double) == 16); // FIXME: assuming 128-bit precision; check should be done at configuration/compilation time
	compact_add_record(TYPE_LONG_DOUBLE, size);
	compact_append_raw(size, sizeof(long double), (unsigned char*)vals);
}
#endif

void ssf_compact::add_char_array(int size, char* vals) {
	compact_add_record(TYPE_INT8, size);
	compact_append_raw(size, sizeof(char), (unsigned char*) vals);
}

void ssf_compact::add_unsigned_char_array(int size, unsigned char* vals) {
	compact_add_record(TYPE_UINT8, size);
	compact_append_raw(size, sizeof(unsigned char), (unsigned char*) vals);
}

void ssf_compact::add_short_array(int size, short* vals) {
	assert(sizeof(short) == sizeof(prime::ssf::int16)); // FIXME: assuming short is always 16-bit
	compact_add_record(TYPE_INT16, size);
	compact_append_raw(size, sizeof(short), (unsigned char*) vals);
}

void ssf_compact::add_unsigned_short_array(int size, unsigned short* vals) {
	assert(sizeof(unsigned short) == sizeof(prime::ssf::uint16)); // FIXME: assuming short is always 16-bit
	compact_add_record(TYPE_UINT16, size);
	compact_append_raw(size, sizeof(unsigned short), (unsigned char*) vals);
}

void ssf_compact::add_int_array(int size, int* vals) {
	if (sizeof(int) == sizeof(prime::ssf::int32))
		compact_add_record(TYPE_INT32, size);
	else if (sizeof(int) == sizeof(prime::ssf::int64))
		compact_add_record(TYPE_INT64, size);
else		assert(0); // FIXME: assuming int has either 32 or 64 bits
		compact_append_raw(size, sizeof(int), (unsigned char*)vals);
	}

	void ssf_compact::add_unsigned_int_array(int size, unsigned int* vals) {
		if(sizeof(unsigned int) == sizeof(prime::ssf::uint32)) compact_add_record(TYPE_UINT32, size);
		else if(sizeof(unsigned int) == sizeof(prime::ssf::uint64)) compact_add_record(TYPE_UINT64, size);
		else assert(0); // FIXME: assuming int has either 32 or 64 bits
		compact_append_raw(size, sizeof(unsigned int), (unsigned char*)vals);
	}

	void ssf_compact::add_long_array(int size, long* vals) {
		if(sizeof(long) == sizeof(prime::ssf::int32)) compact_add_record(TYPE_INT32, size);
		else if(sizeof(long) == sizeof(prime::ssf::int64)) compact_add_record(TYPE_INT64, size);
		else assert(0); // FIXME: assuming long has either 32 or 64 bits
		compact_append_raw(size, sizeof(long), (unsigned char*)vals);
	}

	void ssf_compact::add_unsigned_long_array(int size, unsigned long* vals) {
		if(sizeof(unsigned long) == sizeof(prime::ssf::int32)) compact_add_record(TYPE_INT32, size);
		else if(sizeof(unsigned long) == sizeof(prime::ssf::int64)) compact_add_record(TYPE_INT64, size);
		else assert(0); // FIXME: assuming long has either 32 or 64 bits
		compact_append_raw(size, sizeof(unsigned long), (unsigned char*)vals);
	}

	void ssf_compact::add_long_long_array(int size, SSF_LONG_LONG* vals) {
		assert(sizeof(SSF_LONG_LONG) == sizeof(prime::ssf::int64)); // FIXME: assuming long long is 64 bit
		compact_add_record(TYPE_INT64, size);
		compact_append_raw(size, sizeof(SSF_LONG_LONG), (unsigned char*)vals);
	}

	void ssf_compact::add_unsigned_long_long_array(int size, SSF_UNSIGNED_LONG_LONG* vals) {
		assert(sizeof(SSF_UNSIGNED_LONG_LONG) == sizeof(prime::ssf::uint64)); // FIXME: assuming long long is 64 bit
		compact_add_record(TYPE_UINT64, size);
		compact_append_raw(size, sizeof(SSF_UNSIGNED_LONG_LONG), (unsigned char*)vals);
	}

	void ssf_compact::add_ltime_array(int size, ltime_t* vals) {
#if defined(PRIME_SSF_LTIME_FLOAT)
		compact_add_record(TYPE_LTIME_FLOAT, size);
#elif defined(PRIME_SSF_LTIME_DOUBLE)
		compact_add_record(TYPE_LTIME_DOUBLE, size);
#elif defined(PRIME_SSF_LTIME_LONG)
		if(sizeof(long) == sizeof(prime::ssf::int32)) compact_add_record(TYPE_LTIME_INT32, size);
		else if(sizeof(long) == sizeof(prime::ssf::int64)) compact_add_record(TYPE_LTIME_INT64, size);
		else assert(0); // FIXME: assuming long has either 32 or 64 bits
#elif defined(PRIME_SSF_LTIME_LONGLONG)
		assert(sizeof(SSF_LONG_LONG) == sizeof(prime::ssf::int64)); // FIXME: assuming long long is 64 bit
		compact_add_record(TYPE_LTIME_INT64, size);
#endif
		compact_append_raw(size, sizeof(ltime_t), (unsigned char*)vals);
	}

	void ssf_compact::add_string(const char* val) {
		int size = strlen(val)+1;
		compact_add_record(TYPE_STRING, size);
		compact_append_raw(size, sizeof(char), (unsigned char*)val);
	}

	int ssf_compact::get_float(float* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_FLOAT &&
					compact_record[compact_rec_get_offset+1] >= c) { // either everything or nothing
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(float), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_float()");
		return 0;
	}

	int ssf_compact::get_double(double* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_DOUBLE &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(double), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_double()");
		return 0;
	}

#ifdef HAVE_LONG_DOUBLE
	int ssf_compact::get_long_double(long double* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_LONG_DOUBLE &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(long double), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_long_double()");
		return 0;
	}
#endif

	int ssf_compact::get_char(char* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_INT8 &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(char), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_char()");
		return 0;
	}

	int ssf_compact::get_unsigned_char(unsigned char* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_UINT8 &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(unsigned char), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_unsigned_char()");
		return 0;
	}

	int ssf_compact::get_short(short* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_INT16 &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(short), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_short()");
		return 0;
	}

	int ssf_compact::get_unsigned_short(unsigned short* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_UINT16 &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(unsigned short), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_unsigned_short()");
		return 0;
	}

	int ssf_compact::get_int(int* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset &&
				compact_record[compact_rec_get_offset+1] >= c) {
			// FIXME: there's no conversion between signed and unsigned
			// integer types; this is OK, since it is a programming issue, not
			// a portability issue.
			if((compact_record[compact_rec_get_offset] == TYPE_INT32 &&
							sizeof(prime::ssf::int32) == sizeof(int)) ||
					(compact_record[compact_rec_get_offset] == TYPE_INT64 &&
							sizeof(prime::ssf::int64) == sizeof(int))) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(int), (unsigned char*)val);
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_INT32) {
				// we have a larger integer size than in the storage, which is OK
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::int32 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::int32), (unsigned char*)&tmp);
					val[i] = (int)tmp; // upward conversion here
				}
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_INT64) {
				// we have a smaller integer size than in the storage, which may become problematic
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::int64 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::int64), (unsigned char*)&tmp);
					val[i] = (int)tmp; // downward conversion here
					if((prime::ssf::int64)val[i] != tmp)
					ssf_throw_exception(ssf_exception::compact_convloss, "get_int()");
				}
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_int()");
		return 0;
	}

	int ssf_compact::get_unsigned_int(unsigned int* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset &&
				compact_record[compact_rec_get_offset+1] >= c) {
			// FIXME: there's no conversion between signed and unsigned
			// integer types; this is OK, since it is a programming issue, not
			// a portability issue.
			if((compact_record[compact_rec_get_offset] == TYPE_UINT32 &&
							sizeof(prime::ssf::uint32) == sizeof(unsigned int)) ||
					(compact_record[compact_rec_get_offset] == TYPE_UINT64 &&
							sizeof(prime::ssf::uint64) == sizeof(unsigned int))) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(unsigned int), (unsigned char*)val);
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_UINT32) {
				// we have a larger integer size than in the storage, which is OK
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::uint32 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::uint32), (unsigned char*)&tmp);
					val[i] = (unsigned int)tmp; // upward conversion here
				}
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_UINT64) {
				// we have a smaller integer size than in the storage, which may become problematic
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::uint64 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::uint64), (unsigned char*)&tmp);
					val[i] = (unsigned int)tmp; // downward conversion here
					if((prime::ssf::uint64)val[i] != tmp)
					ssf_throw_exception(ssf_exception::compact_convloss, "get_unsigned_int()");
				}
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_unsigned_int()");
		return 0;
	}

	int ssf_compact::get_long(long* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset &&
				compact_record[compact_rec_get_offset+1] >= c) {
			// FIXME: there's no conversion between signed and unsigned
			// integer types; this is OK, since it is a programming issue, not
			// a portability issue.
			if((compact_record[compact_rec_get_offset] == TYPE_INT32 &&
							sizeof(prime::ssf::int32) == sizeof(long)) ||
					(compact_record[compact_rec_get_offset] == TYPE_INT64 &&
							sizeof(prime::ssf::int64) == sizeof(long))) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(long), (unsigned char*)val);
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_INT32) {
				// we have a larger integer size than in the storage, which is OK
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::int32 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::int32), (unsigned char*)&tmp);
					val[i] = (long)tmp; // upward conversion here
				}
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_INT64) {
				// we have a smaller integer size than in the storage, which may become problematic
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::int64 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::int64), (unsigned char*)&tmp);
					val[i] = (long)tmp; // downward conversion here
					if((prime::ssf::int64)val[i] != tmp)
					ssf_throw_exception(ssf_exception::compact_convloss, "get_long()");
				}
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_long()");
		return 0;
	}

	int ssf_compact::get_unsigned_long(unsigned long* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset &&
				compact_record[compact_rec_get_offset+1] >= c) {
			// FIXME: there's no conversion between signed and unsigned
			// integer types; this is OK, since it is a programming issue, not
			// a portability issue.
			if((compact_record[compact_rec_get_offset] == TYPE_UINT32 &&
							sizeof(prime::ssf::uint32) == sizeof(unsigned long)) ||
					(compact_record[compact_rec_get_offset] == TYPE_UINT64 &&
							sizeof(prime::ssf::uint64) == sizeof(unsigned long))) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(unsigned long), (unsigned char*)val);
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_UINT32) {
				// we have a larger integer size than in the storage, which is OK
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::uint32 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::uint32), (unsigned char*)&tmp);
					val[i] = (unsigned long)tmp; // upward conversion here
				}
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_UINT64) {
				// we have a smaller integer size than in the storage, which may become problematic
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::uint64 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::uint64), (unsigned char*)&tmp);
					val[i] = (unsigned long)tmp; // downward conversion here
					if((prime::ssf::uint64)val[i] != tmp)
					ssf_throw_exception(ssf_exception::compact_convloss, "get_unsigned_long()");
				}
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_unsigned_long()");
		return 0;
	}

	int ssf_compact::get_long_long(SSF_LONG_LONG* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_INT64 &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(SSF_LONG_LONG), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_long_long()");
		return 0;
	}

	int ssf_compact::get_unsigned_long_long(SSF_UNSIGNED_LONG_LONG* val, int c) {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_UINT64 &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(SSF_UNSIGNED_LONG_LONG), (unsigned char*)val);
				return c;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_unsigned_long_long()");
		return 0;
	}

	int ssf_compact::get_ltime(ltime_t* val, int c) {
#if defined(PRIME_SSF_LTIME_FLOAT)
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_LTIME_FLOAT &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(ltime_t), (unsigned char*)val);
				return c;
			}
		}
#elif defined(PRIME_SSF_LTIME_DOUBLE)
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_LTIME_DOUBLE &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(ltime_t), (unsigned char*)val);
				return c;
			}
		}
#elif defined(PRIME_SSF_LTIME_LONG)
		if(compact_rec_get_offset < compact_rec_add_offset &&
				compact_record[compact_rec_get_offset+1] >= c) {
			// FIXME: there's no conversion between signed and unsigned
			// integer types; this is OK, since it is a programming issue, not
			// a portability issue.
			if(compact_record[compact_rec_get_offset] == TYPE_INT32 &&
					sizeof(prime::ssf::int32) == sizeof(ltime_t) ||
					compact_record[compact_rec_get_offset] == TYPE_INT64 &&
					sizeof(prime::ssf::int64) == sizeof(ltime_t)) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(ltime_t), (unsigned char*)val);
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_INT32) {
				// we have a larger integer size than in the storage, which is OK
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::int32 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::int32), (unsigned char*)&tmp);
					val[i] = (ltime_t)tmp; // upward conversion here
				}
				return c;
			} else if(compact_record[compact_rec_get_offset] == TYPE_INT64) {
				// we have a smaller integer size than in the storage, which may become problematic
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				for(int i=0; i<c; i++) {
					prime::ssf::int64 tmp;
					compact_retrieve_raw(1, sizeof(prime::ssf::int64), (unsigned char*)&tmp);
					val[i] = (ltime_t)tmp; // downward conversion here
					if((prime::ssf::int64)val[i] != tmp)
					ssf_throw_exception(ssf_exception::compact_convloss, "get_ltime()");
				}
				return c;
			}
		}
#elif defined(PRIME_SSF_LTIME_LONGLONG)
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_LTIME_INT64 &&
					compact_record[compact_rec_get_offset+1] >= c) {
				compact_record[compact_rec_get_offset+1] -= c;
				if(compact_record[compact_rec_get_offset+1] == 0)
				compact_rec_get_offset += 2;
				compact_retrieve_raw(c, sizeof(ltime_t), (unsigned char*)val);
				return c;
			}
		}
#endif
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_ltime()");
		return 0;
	}

	char* ssf_compact::get_string() {
		if(compact_rec_get_offset < compact_rec_add_offset) {
			if(compact_record[compact_rec_get_offset] == TYPE_STRING) {
				int len = compact_record[compact_rec_get_offset+1];
				assert(len > 0);
				char* strval = new char[len]; assert(strval);
				compact_rec_get_offset += 2;
				compact_retrieve_raw(len, sizeof(char), (unsigned char*)strval);
				assert(strval[len-1] == 0);
				return strval;
			}
		}
		ssf_throw_exception(ssf_exception::compact_mismatch, "get_string()");
		return 0;
	}

	void* ssf_compact::serialize(prime::ssf::uint8 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::uint8)];
		*(prime::ssf::uint8*)buf = data;
		return buf;
	}

	void* ssf_compact::serialize(prime::ssf::uint16 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::uint16)];
		*(prime::ssf::uint16*)buf = SSF_BYTE_ORDER_16(data);
		return buf;
	}

	void* ssf_compact::serialize(prime::ssf::uint32 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::uint32)];
		*(prime::ssf::uint32*)buf = SSF_BYTE_ORDER_32(data);
		return buf;
	}

	void* ssf_compact::serialize(prime::ssf::uint64 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::uint64)];
		*(prime::ssf::uint64*)buf = SSF_BYTE_ORDER_64(data);
		return buf;
	}

	void* ssf_compact::serialize(prime::ssf::int8 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::int8)];
		*(prime::ssf::int8*)buf = data;
		return buf;
	}

	void* ssf_compact::serialize(prime::ssf::int16 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::int16)];
		*(prime::ssf::int16*)buf = SSF_BYTE_ORDER_16(data);
		return buf;
	}

	void* ssf_compact::serialize(prime::ssf::int32 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::int32)];
		*(prime::ssf::int32*)buf = SSF_BYTE_ORDER_32(data);
		return buf;
	}

	void* ssf_compact::serialize(prime::ssf::int64 data, void* buf) {
		// we don't check buffer overflow
		if(!buf) buf = new char[sizeof(prime::ssf::int64)];
		*(prime::ssf::int64*)buf = SSF_BYTE_ORDER_64(data);
		return buf;
	}

	void* ssf_compact::serialize(float data, void* buf) {
		// we don't check buffer overflow
		assert(sizeof(prime::ssf::uint32) == sizeof(float)); // FIXME: assuming IEEE 754 compliant; check should be done at configuration/compilation time
		if(!buf) buf = new char[sizeof(float)];
		prime::ssf::uint32 tmp = SSF_BYTE_ORDER_32(*(prime::ssf::uint32*)&data);
		memcpy(buf, &tmp, sizeof(prime::ssf::uint32));
		return buf;
	}

	void* ssf_compact::serialize(double data, void* buf) {
		// we don't check buffer overflow
		assert(sizeof(prime::ssf::uint64) == sizeof(double)); // FIXME: assuming IEEE 754 compliant; check should be done at configuration/compilation time
		if(!buf) buf = new char[sizeof(double)];
		prime::ssf::uint64 tmp = SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&data);
		memcpy(buf, &tmp, sizeof(prime::ssf::uint64));
		return buf;
	}

	int ssf_compact::deserialize(prime::ssf::uint8& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = *(prime::ssf::uint8*)buf;
		return 0;
	}

	int ssf_compact::deserialize(prime::ssf::uint16& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = SSF_BYTE_ORDER_16(*(prime::ssf::uint16*)buf);
		return 0;
	}

	int ssf_compact::deserialize(prime::ssf::uint32& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = SSF_BYTE_ORDER_32(*(prime::ssf::uint32*)buf);
		return 0;
	}

	int ssf_compact::deserialize(prime::ssf::uint64& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)buf);
		return 0;
	}

	int ssf_compact::deserialize(prime::ssf::int8& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = *(prime::ssf::int8*)buf;
		return 0;
	}

	int ssf_compact::deserialize(prime::ssf::int16& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = SSF_BYTE_ORDER_16(*(prime::ssf::int16*)buf);
		return 0;
	}

	int ssf_compact::deserialize(prime::ssf::int32& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = SSF_BYTE_ORDER_32(*(prime::ssf::int32*)buf);
		return 0;
	}

	int ssf_compact::deserialize(prime::ssf::int64& data, void* buf) {
		if(!buf) return 1; // indicates error
		data = SSF_BYTE_ORDER_64(*(prime::ssf::int64*)buf);
		return 0;
	}

	int ssf_compact::deserialize(float& data, void* buf) {
		if(!buf) return 1; // indicates error
		assert(sizeof(prime::ssf::uint32) == sizeof(float)); // FIXME: assuming IEEE 754 compliant; check should be done at configuration/compilation time
		prime::ssf::uint32 tmp = SSF_BYTE_ORDER_32(*(prime::ssf::int32*)buf);
		memcpy(&data, &tmp, sizeof(prime::ssf::uint32));
		return 0;
	}

	int ssf_compact::deserialize(double& data, void* buf)
	{
		if(!buf) return 1; // indicates error
		assert(sizeof(prime::ssf::uint64) == sizeof(double)); // FIXME: assuming IEEE 754 compliant; check should be done at configuration/compilation time
		prime::ssf::uint64 tmp = SSF_BYTE_ORDER_64(*(prime::ssf::int64*)buf);
		memcpy(&data, &tmp, sizeof(prime::ssf::uint64));
		return 0;
	}

	void ssf_compact::compact_reset() {
		if(compact_record) {delete[] compact_record; compact_record = 0;}
		if(compact_data) {delete[] compact_data; compact_data = 0;}
		compact_rec_capacity = compact_rec_add_offset = compact_rec_get_offset = 0;
		compact_capacity = compact_add_offset = compact_get_offset = 0;
	}

	void ssf_compact::compact_add_record(int type, int size) {
		if(type != TYPE_STRING && // each string must maintain its own record
				compact_rec_add_offset > 0 && compact_record[compact_rec_add_offset-2] == type) {
			compact_record[compact_rec_add_offset-1] += size;
		} else {
			if(compact_rec_add_offset >= compact_rec_capacity) {
				int newsz = compact_rec_capacity ? 2*compact_rec_capacity : 16;
				int* newbuf = new int[newsz]; assert(newbuf);
				if(compact_record) {
					memcpy(newbuf, compact_record, compact_rec_add_offset*sizeof(int));
					delete[] compact_record;
				}
				compact_record = newbuf;
				compact_rec_capacity = newsz;
			}
			compact_record[compact_rec_add_offset] = type;
			compact_record[compact_rec_add_offset+1] = size;
			compact_rec_add_offset += 2;
		}
	}

	void ssf_compact::compact_append_raw(int c, int size, unsigned char* buf) {
		compact_add_offset = compact_make_alignment(compact_add_offset, size);
		int newsz = compact_capacity;
		while(size*c+compact_add_offset > newsz) {
			newsz = newsz ? 2*newsz : 32;
			if(newsz < size*c+compact_add_offset) continue;
			unsigned char* newbuf = new unsigned char[newsz];
			assert(newbuf);
			if(compact_data) {
				memcpy(newbuf, compact_data, compact_capacity*sizeof(unsigned char));
				delete[] compact_data;
			}
			compact_data = newbuf;
			compact_capacity = newsz;
			break;
		}
#ifdef SSF_NO_ENDIAN_FORMATTING
		memcpy(&compact_data[compact_add_offset], buf, size*c);
#else
		if(size == sizeof(prime::ssf::uint8)) {
			memcpy(&compact_data[compact_add_offset], buf, size*c);
		} else {
			for(int i=0; i<c; i+=size) {
				if(size == sizeof(prime::ssf::uint16))
				*(prime::ssf::uint16*)&compact_data[compact_add_offset+i] =
				SSF_BYTE_ORDER_16(*(prime::ssf::uint16*)&buf[i]);
				else if(size == sizeof(prime::ssf::uint32))
				*(prime::ssf::uint32*)&compact_data[compact_add_offset+i] =
				SSF_BYTE_ORDER_32(*(prime::ssf::uint32*)&buf[i]);
				else if(size == sizeof(prime::ssf::uint64))
				*(prime::ssf::uint64*)&compact_data[compact_add_offset+i] =
				SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&buf[i]);
#ifdef HAVE_LONG_DOUBLE
				else if(size == sizeof(long double)) {
					assert(size == 2*sizeof(prime::ssf::uint64)); // FIXME: assuming 16 bytes
					*(prime::ssf::uint64*)&compact_data[compact_add_offset+i] =
					SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&buf[i+sizeof(prime::ssf::uint64)]);
					*(prime::ssf::uint64*)&compact_data[compact_add_offset+i+sizeof(prime::ssf::uint64)] =
					SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&buf[i]);
				}
#endif
				else assert(0);
			}
		}
#endif /*SSF_NO_ENDIAN_FORMATTING*/
		compact_add_offset += size*c;
	}

	void ssf_compact::compact_retrieve_raw(int c, int size, unsigned char* buf)
	{
		compact_get_offset = compact_make_alignment(compact_get_offset, size);
		assert(size*c+compact_get_offset <= compact_capacity);
#ifdef SSF_NO_ENDIAN_FORMATTING
		memcpy(buf, &compact_data[compact_get_offset], size*c);
#else
		if(size == sizeof(prime::ssf::uint8)) {
			memcpy(buf, &compact_data[compact_get_offset], size*c);
		} else {
			for(int i=0; i<c; i+=size) {
				if(size == sizeof(prime::ssf::uint16))
				*(prime::ssf::uint16*)&buf[i] =
				SSF_BYTE_ORDER_16(*(prime::ssf::uint16*)&compact_data[compact_get_offset+i]);
				else if(size == sizeof(prime::ssf::uint32))
				*(prime::ssf::uint32*)&buf[i] =
				SSF_BYTE_ORDER_32(*(prime::ssf::uint32*)&compact_data[compact_get_offset+i]);
				else if(size == sizeof(prime::ssf::uint64))
				*(prime::ssf::uint64*)&buf[i] =
				SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&compact_data[compact_get_offset+i]);
#ifdef HAVE_LONG_DOUBLE
				else if(size == sizeof(long double)) {
					assert(size == 2*sizeof(prime::ssf::uint64)); // FIXME: assuming 16 bytes
					*(prime::ssf::uint64*)&buf[i+sizeof(prime::ssf::uint64)] =
					SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&compact_data[compact_get_offset+i]);
					*(prime::ssf::uint64*)&buf[i] =
					SSF_BYTE_ORDER_64(*(prime::ssf::uint64*)&compact_data[compact_get_offset+i+sizeof(prime::ssf::uint64)]);
				}
#endif
				else assert(0);
			}
		}
#endif /*SSF_NO_ENDIAN_FORMATTING*/
		compact_get_offset += size*c;
	}

	int ssf_compact::read(std::ifstream& ifs) {
		compact_reset();
		int rao = 0; // "record add offset" init to zero
		if(!ifs.read((char*)&rao, sizeof(int))) {
			ssf_throw_exception(ssf_exception::compact_fops, "read()");
			return 0;
		}
		compact_rec_add_offset = SSF_BYTE_ORDER_32(rao);
		if(compact_rec_add_offset > 0) {
			compact_rec_capacity = compact_rec_add_offset;
			compact_record = new int[compact_rec_capacity]; assert(compact_record);
			if(!ifs.read((char*)compact_record, compact_rec_add_offset*sizeof(int))) {
				ssf_throw_exception(ssf_exception::compact_fops, "read()");
				return 0;
			}
#ifndef SSF_NO_ENDIAN_FORMATTING
			for(int i=0; i<compact_rec_add_offset; i++)
			compact_record[i] = SSF_BYTE_ORDER_32(compact_record[i]);
#endif
			int ao = 0; // "add offset" init to zero
			if(!ifs.read((char*)&ao, sizeof(int))) {
				ssf_throw_exception(ssf_exception::compact_fops, "read()");
				return 0;
			}
			compact_add_offset = compact_capacity = SSF_BYTE_ORDER_32(ao);
			assert(compact_capacity > 0);
			compact_data = new unsigned char[compact_capacity]; assert(compact_data);
			if(!ifs.read((char*)compact_data, compact_add_offset*sizeof(unsigned char))) {
				ssf_throw_exception(ssf_exception::compact_fops, "read()");
				return 0;
			}
		}
		return 1;
	}

	int ssf_compact::write(std::ofstream& ofs) {
		int rao = SSF_BYTE_ORDER_32(compact_rec_add_offset);
		if(!ofs.write((char*)&rao, sizeof(int))) {
			ssf_throw_exception(ssf_exception::compact_fops, "write()");
			return 0;
		}
		if(compact_rec_add_offset > 0) {
#ifdef SSF_NO_ENDIAN_FORMATTING
			if(!ofs.write((char*)compact_record, compact_rec_add_offset*sizeof(int))) {
				ssf_throw_exception(ssf_exception::compact_fops, "write()");
				return 0;
			}
#else
			int* tmpmem = new int[compact_rec_add_offset];
			for(int i=0; i<compact_rec_add_offset; i++)
			tmpmem[i] = SSF_BYTE_ORDER_32(compact_record[i]);
			if(!ofs.write((char*)tmpmem, compact_rec_add_offset*sizeof(int))) {
				delete[] tmpmem;
				ssf_throw_exception(ssf_exception::compact_fops, "write()");
				return 0;
			}
			delete[] tmpmem;
#endif
			int ao = SSF_BYTE_ORDER_32(compact_add_offset);
			if(!ofs.write((char*)&ao, sizeof(int)) || !ofs.write((char*)compact_data, compact_add_offset*sizeof(unsigned char))) {
				ssf_throw_exception(ssf_exception::compact_fops, "write()");
				return 0;
			}
		}
		return 1;
	}

#ifndef PRIME_SSF_ARCH_WINDOWS

	int ssf_compact::read(int fd) {
		compact_reset();
		int rao = 0; // "record add offset" init to zero
		if(::read(fd, &rao, sizeof(int)) <= 0) {
			ssf_throw_exception(ssf_exception::compact_fops, "read()");
			return 0;
		}
		compact_rec_add_offset = SSF_BYTE_ORDER_32(rao);
		if(compact_rec_add_offset > 0) {
			compact_rec_capacity = compact_rec_add_offset;
			compact_record = new int[compact_rec_capacity]; assert(compact_record);
			if(::read(fd, compact_record, compact_rec_add_offset*sizeof(int)) <= 0) {
				ssf_throw_exception(ssf_exception::compact_fops, "read()");
				return 0;
			}
#ifndef SSF_NO_ENDIAN_FORMATTING
			for(int i=0; i<compact_rec_add_offset; i++)
			compact_record[i] = SSF_BYTE_ORDER_32(compact_record[i]);
#endif
			int ao = 0; // "add offset" init to zero
			if(::read(fd, &ao, sizeof(int)) <= 0) {
				ssf_throw_exception(ssf_exception::compact_fops, "read()");
				return 0;
			}
			compact_add_offset = compact_capacity = SSF_BYTE_ORDER_32(ao);
			assert(compact_capacity > 0);
			compact_data = new unsigned char[compact_capacity]; assert(compact_data);
			if(::read(fd, compact_data, compact_add_offset*sizeof(unsigned char)) <= 0) {
				ssf_throw_exception(ssf_exception::compact_fops, "read()");
				return 0;
			}
		}
		return 1;
	}

	int ssf_compact::write(int fd) {
		int rao = SSF_BYTE_ORDER_32(compact_rec_add_offset);
		if(::write(fd, &rao, sizeof(int)) <= 0) {
			ssf_throw_exception(ssf_exception::compact_fops, "write()");
			return 0;
		}
		if(compact_rec_add_offset > 0) {
#ifdef SSF_NO_ENDIAN_FORMATTING
			if(::write(fd, compact_record, compact_rec_add_offset*sizeof(int)) <= 0) {
				ssf_throw_exception(ssf_exception::compact_fops, "write()");
				return 0;
			}
#else
			int* tmpmem = new int[compact_rec_add_offset];
			for(int i=0; i<compact_rec_add_offset; i++)
			tmpmem[i] = SSF_BYTE_ORDER_32(compact_record[i]);
			if(::write(fd, tmpmem, compact_rec_add_offset*sizeof(int)) <= 0) {
				delete[] tmpmem;
				ssf_throw_exception(ssf_exception::compact_fops, "write()");
				return 0;
			}
			delete[] tmpmem;
#endif
			int ao = SSF_BYTE_ORDER_32(compact_add_offset);
			if(::write(fd, &ao, sizeof(int)) <= 0 || ::write(fd, compact_data, compact_add_offset*sizeof(unsigned char)) <= 0) {
				ssf_throw_exception(ssf_exception::compact_fops, "write()");
				return 0;
			}
		}
		return 1;
	}

#endif /*undef PRIME_SSF_ARCH_WINDOWS*/

#ifdef PRIME_SSF_SYNC_MPI
	int ssf_compact::pack(MPI_Comm comm, char* buffer, int& pos, int bufsiz) {
		if(bufsiz-pos <= compact_add_offset) {
			ssf_throw_exception(ssf_exception::compact_mpibuf);
			return 1;
		}

		if(MPI_Pack(&compact_rec_add_offset, 1, MPI_INT, buffer, bufsiz, &pos, comm)) {
			ssf_throw_exception(ssf_exception::compact_mpiops, "pack()");
			return 1;
		}

		if(compact_rec_add_offset > 0) {
			if(MPI_Pack(compact_record, compact_rec_add_offset, MPI_INT, buffer, bufsiz, &pos, comm) ||
				MPI_Pack(&compact_add_offset, 1, MPI_INT, buffer, bufsiz, &pos, comm)) {
				ssf_throw_exception(ssf_exception::compact_mpiops, "pack()");
				return 1;
			}

			if(MPI_Pack(compact_data, compact_add_offset, MPI_UNSIGNED_CHAR, buffer, bufsiz, &pos, comm)) {
				ssf_throw_exception(ssf_exception::compact_mpiops, "pack()");
				return 1;
			}
		}
		return 0;
	}

	int ssf_compact::unpack(MPI_Comm comm, char* buffer, int& pos, int bufsiz) {
		compact_reset();
		if(MPI_Unpack(buffer, bufsiz, &pos, &compact_rec_add_offset, 1, MPI_INT, comm)) {
			ssf_throw_exception(ssf_exception::compact_mpiops, "unpack()");
			return 1;
		}
		if(compact_rec_add_offset > 0) {
			compact_rec_capacity = compact_rec_add_offset;
			compact_record = new int[compact_rec_capacity]; assert(compact_record);
			if(MPI_Unpack(buffer, bufsiz, &pos, compact_record,	compact_rec_add_offset, MPI_INT, comm) ||
				MPI_Unpack(buffer, bufsiz, &pos, &compact_add_offset, 1, MPI_INT, comm)) {
				ssf_throw_exception(ssf_exception::compact_mpiops, "unpack()");
				return 1;
			}
			compact_capacity = compact_add_offset;
			compact_data = new unsigned char[compact_capacity]; assert(compact_data);
			if(MPI_Unpack(buffer, bufsiz, &pos, compact_data, compact_add_offset, MPI_UNSIGNED_CHAR, comm)) {
				ssf_throw_exception(ssf_exception::compact_mpiops, "unpack()");
				return 1;
			}
		}
		return 0;
	}
#endif /*PRIME_SSF_SYNC_MPI*/

#ifdef PRIME_SSF_SYNC_LIBSYNK
	int ssf_compact::pack(char* buffer, int& pos, int bufsiz) {
		pos = compact_make_alignment(pos, sizeof(int));
		if(pos+(int)sizeof(int) > bufsiz) return 1;
		serialize((prime::ssf::int32)compact_rec_add_offset, &buffer[pos]); pos += sizeof(int);

		if(compact_rec_add_offset > 0) {
			if(pos+compact_rec_add_offset*(int)sizeof(int) > bufsiz) return 1;
#ifdef SSF_NO_ENIAN_FORMATTING
			memcpy(&buffer[pos], (char*)compact_record, compact_rec_add_offset*sizeof(int));
#else
			for(int i=0; i<compact_rec_add_offset; i++)
			*(int*)&buffer[pos+i*sizeof(int)] = SSF_BYTE_ORDER_32(compact_record[i]);
#endif
			pos += compact_rec_add_offset*sizeof(int);

			if(pos+(int)sizeof(int) > bufsiz) return 1;
			serialize((prime::ssf::int32)compact_add_offset, &buffer[pos]); pos += sizeof(int);

			if(pos+compact_add_offset > bufsiz) return 1;
			memcpy(&buffer[pos], (char*)compact_data, compact_add_offset);
			pos += compact_add_offset;

			// extra careful
			pos = compact_make_alignment(pos, sizeof(int));
			if(pos > bufsiz) return 1;
		}

		return 0;
	}

	int ssf_compact::unpack(char* buffer, int& pos, int bufsiz)	{
		compact_reset();
		pos = compact_make_alignment(pos, sizeof(int));
		if(pos+(int)sizeof(int) > bufsiz) return 1;
		deserialize((prime::ssf::int32&)compact_rec_add_offset, &buffer[pos]); pos += sizeof(int);

		if(compact_rec_add_offset > 0) {
			compact_rec_capacity = compact_rec_add_offset;
			compact_record = new int[compact_rec_capacity]; assert(compact_record);
			if(pos+compact_rec_add_offset*(int)sizeof(int) > bufsiz) return 1;
#ifdef SSF_NO_ENIAN_FORMATTING
			memcpy((char*)compact_record, &buffer[pos], compact_rec_add_offset*sizeof(int));
#else
			for(int i=0; i<compact_rec_add_offset; i++)
			compact_record[i] = SSF_BYTE_ORDER_32(*(int*)&buffer[pos+i*sizeof(int)]);
#endif
			pos += compact_rec_add_offset*sizeof(int);

			if(pos+(int)sizeof(int) > bufsiz) return 1;
			deserialize((prime::ssf::int32&)compact_add_offset, &buffer[pos]); pos += sizeof(int);
			compact_capacity = compact_add_offset;
			compact_data = new unsigned char[compact_capacity]; assert(compact_data);

			if(pos+compact_add_offset > bufsiz) return 1;
			memcpy(&buffer[pos], (char*)compact_data, compact_add_offset);
			pos += compact_add_offset;

			// extra careful
			pos = compact_make_alignment(pos, sizeof(int));
			if(pos > bufsiz) return 1;
		}

		return 0;
	}
#endif /*PRIME_SSF_SYNC_LIBSYNK*/

}; // namespace ssf
}; // namespace prime

/*
 * Copyright (c) 2007-2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 * 
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * noninfringement. In no event shall Florida International University be
 * liable for any claim, damages or other liability, whether in an
 * action of contract, tort or otherwise, arising from, out of or in
 * connection with the software or the use or other dealings in the
 * software.
 * 
 * This software is developed and maintained by
 *
 *   The PRIME Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, FL 33199, USA
 *
 * Contact Jason Liu <liux@cis.fiu.edu> for questions regarding the use
 * of this software.
 */
