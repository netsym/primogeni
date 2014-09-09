/**
 * \file logger.h
 * \brief Header file for public logging facilities.
 * \author Nathanael Van Vorst
 * 
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

#ifndef __LOGGER_H__
#define __LOGGER_H__

#include "logger_factory.h"

namespace prime {
namespace ssfnet {

/*
 * Basically to use the logger, the function "loggerInstance()" needs
 * to resolve to a function with will call
 *             LogStreamFactory::instance().createLogger("logger name").
 *
 * To Make this more efficient we will cache the result and wrap the
 * call to the function. Also, since some loggers need a special 'cleanup'
 * function called before an exit or abort, we provide two macros:
 *
 *   1) LOGGING_COMPONENT
 *       Should be used once and if a cleanup function is needed, this is where
 *       its done.
 *   2) LOGGING_COMPONENT_REF
 *       Just specify the name of the logger you want
 *
 * The above macros MUST only be specified in a cc files. Only one of these
 * macros is allowed per file.
 *
 * The logger factory takes care of the fact that a references may try to
 * log before the main logger is created.
 *
 */

/**
* Register a component with the logging module. If you need some cleanup to
* be done on an abort you can pass along a function pointer with the following
*  prototype:void (*abortCleanupFunc)().
*/
#define LOGGING_COMPONENT(component,...) \
	static LogStream* __loggerInstance__##component = \
		LogStreamFactory::instance().createLogger(#component, ##__VA_ARGS__);\
	static LogStream* loggerInstance() { return __loggerInstance__##component; }

#define LOGGING_COMPONENT_REF(component) \
	static LogStream* __loggerInstance__##component = NULL;\
	static LogStream* loggerInstance() { \
		if(NULL==__loggerInstance__##component)\
		__loggerInstance__##component = LogStreamFactory::instance().createLogger(#component, NULL);\
		return __loggerInstance__##component;}
#if SSFNET_DEBUG
#define LOG_DEBUG(msg)  {(((LogStream*)loggerInstance())->log(__FILE__,__LINE__,LogLvl::debug())) << msg ; }
#define LOG_INFO(msg)   {(((LogStream*)loggerInstance())->log(__FILE__,__LINE__,LogLvl::info())) << msg ; }
#define LOG_WARN(msg)   {(((LogStream*)loggerInstance())->log(__FILE__,__LINE__,LogLvl::warn())) << msg ; }
#define DEBUG_CODE(...) { __VA_ARGS__ }
#else
#define LOG_DEBUG(msg) {/* no op*/}
#define LOG_INFO(msg) {/* no op*/}
#define LOG_WARN(msg) {/* no op*/}
#define DEBUG_CODE(...) {/* no op*/}
#endif

#define LOG_ERROR(msg)  {(((LogStream*)loggerInstance())->log(__FILE__,__LINE__,LogLvl::error())) << msg ; }

} //namespace ssfnet
} //namespace prime

#endif /*__LOGGER_H__*/
