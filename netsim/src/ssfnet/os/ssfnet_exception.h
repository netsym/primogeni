/**
 * \file ssfnet_exception.h
 * \brief SSFNet exception handler.
 *
 * This header file contains the definition of the SSFNet exception
 * class (prime::ssfnet::SSFNetException). The exception class can be
 * used to handle errors occur during the execution.
 *
 * \author Nathanael Van Vorst
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

#ifndef __SSFNET_EXCEPTION_H__
#define __SSFNET_EXCEPTION_H__

#include <exception>

namespace prime {
namespace ssfnet {

class SSFNetException: public virtual std::exception {
public:
	/// All SSFNet execeptions are defined here.
	enum Type {
		no_exception = 0, ///< no exception or uninitialized exception
		other_exception, ///< unknown derived exception

		config_write_err, ///< if a user tries to write to a property variable
		config_varmap_err, ///< if the user includes a variable already defined as a property or state in a parent class in a derived class
		config_type_err, ///< No class/type by the name or id specified

		runtime_getnow, ///< getNow() should not be called before init and after wrapup

		duplicate_nix_vector, ///< tried to set a nix vector of a packet that already had one

		unknown_context_uid, ///< asked for a node with an unknown

		must_implement_exception, ///< user did not override a function in a derived class

		total_exceptions ///< total number of exceptions defined by this class
	};

	/**
	 * \brief The constructor, typically called by the ssfnet_throw_exception() method.
	 * \param _code is the error code.
	 * \param msg is a string, which will be appended to the standard error message.
	 */
	SSFNetException(SSFNetException::Type _code, const char* msg);

	/// Returns the error code.
	virtual SSFNetException::Type type() {
		return code;
	}

	/// Returns the error message of the exception.
	virtual const char* what() const throw () {
		return explanation;
	}

	void append(const char * msg);

protected:
	SSFNetException() :
		code(no_exception),explanation(0),len(0),pos(0) {
	}
	SSFNetException::Type code;
	char* explanation;
	int len,pos;
};

/**
 * \brief Throws an exception of the given type and with a customized error message.
 * \param code is the error code (an enumerated value of exception_type_t).
 * \param msg is the customized message that will be appended to the standard error string.
 */
inline void ssfnet_throw_exception(SSFNetException::Type code, const char* msg = 0) {
	throw SSFNetException(code,msg);
}

}
; // namespace ssfnet
}
; // namespace prime

#endif /*__SSFNET_EXCEPTION_H__*/
