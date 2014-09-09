/**
 * \file ssf.h
 * \brief Header file for the SSF library.
 *
 * This is the only header file that the user needs to include to use
 * all SSF functionalities. See individual files included by this file
 * for the detailed interface of each component.
 */

#ifndef __PRIME_SSF_H__
#define __PRIME_SSF_H__

#include <stdio.h>

#include "../dml/dml.h"
#include "../rng/rng.h"

#include "sim/internal.h"

// SSF types and constants.
#include "api/ssftype.h"
#include "api/ssftime.h"
#include "api/ssfqobj.h"
#include "api/ssfcompact.h"
#include "api/ssfexception.h"

#ifndef PRIME_SSFNET_VERSION
/**
 * \brief All PRIME objects are defined in this namespace.
 *
 * Currently PRIME contains three modules, each has its own namespace:
 *   - prime::rng for random number generators (RNG),
 *   - prime::dml for domain modeling language (DML), and
 *   - prime::ssf for scalable simulation framework (SSF).
 */
#endif
namespace prime {

/// SSF objects are defined in this namespace.
namespace ssf {

/// SSF boolean type.
typedef int boolean;

#ifdef true
#undef true
#undef false
#endif

/// Constant true value of the SSF boolean type.
#define true  1
/// Constant false value of the SSF boolean type.
#define false 0

#ifndef SSF_TRUE
#define SSF_TRUE  true
#define SSF_FALSE false
#endif

/// SSF opaque object type.
typedef void Object;

// forward declaration
class Entity;
class Process;
class Event;
class inChannel;
class outChannel;

/** print out helpful information about ssf command-line arguments. */
extern void print_command_line(FILE* outfd = stderr);

//disable to printing of the introductory copyright
extern bool do_not_print_copyright();

//disable to printing of the introductory stats
extern bool do_not_print_stats();

}; // namespace ssf
}; // namespace prime

// ssf core
#include "api/process.h"
#include "api/entity.h"
#include "api/event.h"
#include "api/inchannel.h"
#include "api/outchannel.h"

// ssf extensions
#include "api/ssfsem.h"
#include "api/ssftimer.h"
#include "api/ssfenv.h"

#ifdef PRIME_SSF_BACKWARD_COMPATIBILITY
using namespace prime::ssf;
using namespace prime::dml;
using namespace prime::rng;
#endif

#endif /*__PRIME_SSF_H__*/

#ifndef PRIME_SSFNET_VERSION
/**
 * \mainpage PRIME SSF Reference Manual
 *
 * This documentation is generated automatically from the source
 * code. This reference manual is designed to accompany the PRIME SSF
 * User's Manual distributed with the software. An online version of
 * this manual can be obtained from the PRIME web site:
 * <http://www.cis.fiu.edu/prime/index.html>.  Additional information
 * about this software can also be found at this web site.\n
 *
 * <b>Copyright (c) 2007-2011 Florida International University.</b>
 *
 * PERMISSION IS HEREBY GRANTED, FREE OF CHARGE, TO ANY INDIVIDUAL OR
 * INSTITUTION OBTAINING A COPY OF THIS SOFTWARE AND ASSOCIATED
 * DOCUMENTATION FILES (THE "SOFTWARE"), TO USE, COPY, MODIFY, AND
 * DISTRIBUTE WITHOUT RESTRICTION.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL FLORIDA INTERNATIONAL UNIVERSITY BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * This software is developed and maintained by
 *
 *   The PRIME Research Group\n
 *   School of Computing and Information Sciences\n
 *   Florida International University\n
 *   Miami, FL 33199, USA
 *
 * Contact Jason Liu <liux@cis.fiu.edu> for questions regarding the use
 * of this software.
 */
#endif
