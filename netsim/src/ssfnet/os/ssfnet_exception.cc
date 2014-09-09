/**
 * \file ssfnet_exception.cc
 * \brief SSFNet exception handler.
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
#include <stdio.h>
#include <stdarg.h>
#include <string.h>
#include "os/ssfnet_exception.h"

namespace prime {
namespace ssfnet {

SSFNetException::SSFNetException(SSFNetException::Type _code, const char* msg) :
	code(_code),explanation(new char[128]),len(128),pos(0) {

	append("ERROR: ");

	switch (code) {
	case no_exception:
		append("uninitialized exception");
		break;
	case other_exception:
		break;
	case config_write_err:
		append("Tried to write to a configurable property. Only configurable states are mutable");
		break;
	case config_varmap_err:
		break;
	case config_type_err:
		append("Invalid argument type.");
		break;
	case duplicate_nix_vector:
		append("Tried to set a nix vector of a packet that already had one");
		break;
	case unknown_context_uid:
		append("Asked for a node with an unknown context. ");
		break;
	case runtime_getnow:
		append("getNow() should not be called before init and after wrapup");
		break;
	case must_implement_exception:
		append("You must override/implement this function in the derived class!");
		break;
	default:
		append("programming error");
	}
	if (msg) {
		append(": ");
		append(msg);
	}
}

void SSFNetException::append(const char * msg) {
	if(len-pos < (int)strlen(msg)) {
		if(len==0) {
			//it was empty before
			len = strlen(msg)+100;
			explanation=new char[len];
			//append the string
			pos=sprintf(explanation,"%s",msg);
		}
		else {
			//need to add more space
			char* t = explanation;
			len += strlen(msg)+100;
			explanation=new char[len];
			//append the string
			pos=sprintf(explanation,"%s%s",t,msg);
			delete[] t;
		}
	}
	else {
		//append the string
		pos+=sprintf(explanation+pos,"%s",msg);
	}
}

} // namespace ssfnet
} // namespace prime
