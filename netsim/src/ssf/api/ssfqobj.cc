/*
 * quickobj :- quick memory object.
 */

#include <stdlib.h> // for malloc and free

#include "api/ssfqobj.h"
#include "api/ssfexception.h"
#include "mac/memcpp.h"
#include "mac/quickmem.h"
#include "mac/segments.h"

//SSF_DECLARE_PRIVATE(size_t, quickobj_size);

namespace prime {
namespace ssf {

#if PRIME_SSF_QUICKMEM

void* ssf_quickobj::operator new(size_t sz)
{
  return ssf_quickmem_malloc(sz);
}

void ssf_quickobj::operator delete(void* p)
{
  ssf_quickmem_free(p);
}

#endif /*PRIME_SSF_QUICKMEM*/

void* ssf_quickobj::quick_new(size_t sz)
{
#if PRIME_SSF_QUICKMEM
  return ssf_quickmem_malloc(sz);
#else
  return new char[sz]; // invoke ssf_arena_malloc if the new operator is overloaded
#endif
}

// size is no longer needed here
void ssf_quickobj::quick_delete(void* p, size_t sz)
{
#if PRIME_SSF_QUICKMEM
  ssf_quickmem_free(p);
#else
  delete[] (char*)p; // invoke ssf_arena_free if the delete operator is overloaded
#endif
}

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
