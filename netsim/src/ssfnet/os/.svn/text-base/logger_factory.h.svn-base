/**
 * \file logger_factory.h
 * \brief Header file for private implementations of logging facilities.
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

#ifndef __LOGGER_FACTORY_H__
#define __LOGGER_FACTORY_H__

#include <assert.h>
#include "os/ssfnet_types.h"
#include "os/config_entity.h"

namespace prime {
namespace ssfnet {

/**
 * \brief XXX LogLvl
 *
 * XXX
 */
class LogLvl {
	friend class LogStream;
public:
	enum Type {
		NONE_LVL = 1,
		ABORT_LVL = 2,
		ERROR_LVL = 3,
		WARN_LVL = 4,
		INFO_LVL = 5,
		DEBUG_LVL = 6,
	};
	LogLvl();
	LogLvl(LogLvl::Type _val);
	~LogLvl();

	LogLvl operator=(const LogLvl& x);

	operator int() const;


	const SSFNET_STRING& toString() const;
	static const LogLvl& fromString(SSFNET_STRING& lvl);

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const LogLvl& x);
	friend int operator==(const LogLvl& x, const LogLvl& y);
	friend int operator==(const LogLvl& x, const LogLvl::Type y);
	friend int operator==(const LogLvl::Type x, const LogLvl& y);
	friend int operator!=(const LogLvl& x, const LogLvl& y);
	friend int operator!=(const LogLvl& x, const LogLvl::Type y);
	friend int operator!=(const LogLvl::Type x, const LogLvl& y);

	friend int operator>(const LogLvl& x, const LogLvl& y);
	friend int operator>(const LogLvl& x, const LogLvl::Type y);
	friend int operator>(const LogLvl::Type x, const LogLvl& y);
	friend int operator>=(const LogLvl& x, const LogLvl& y);
	friend int operator>=(const LogLvl& x, const LogLvl::Type y);
	friend int operator>=(const LogLvl::Type x, const LogLvl& y);

	friend int operator<(const LogLvl& x, const LogLvl& y);
	friend int operator<(const LogLvl& x, const LogLvl::Type y);
	friend int operator<(const LogLvl::Type x, const LogLvl& y);
	friend int operator<=(const LogLvl& x, const LogLvl& y);
	friend int operator<=(const LogLvl& x, const LogLvl::Type y);
	friend int operator<=(const LogLvl::Type x, const LogLvl& y);
	static LogLvl* none();
	static LogLvl* abort();
	static LogLvl* error();
	static LogLvl* warn();
	static LogLvl* info();
	static LogLvl* debug();
private:
	static LogLvl* none_obj;
	static LogLvl* abort_obj;
	static LogLvl* error_obj;
	static LogLvl* warn_obj;
	static LogLvl* info_obj;
	static LogLvl* debug_obj;
	static SSFNET_STRING* none_str;
	static SSFNET_STRING* abort_str;
	static SSFNET_STRING* error_str;
	static SSFNET_STRING* warn_str;
	static SSFNET_STRING* info_str;
	static SSFNET_STRING* debug_str;
	static void init();
	LogLvl::Type val;
	const SSFNET_STRING* str;
};

class LogMsg;
/**
 * \brief XXX LogStream
 *
 * XXX
 */
class LogStream {
	friend class LogStreamFactory;
	friend class LogMsg;
public:

	~LogStream();

	LogMsg log(const char* fileName, int line, const LogLvl* lvl);

	void configureThreshold(LogLvl* threshold);

protected:
	LogStream(const char* componentName, LogLvl* threshold);
	void configureStream(LogLvl* lvl, PRIME_OSTREAM* stream);
	void preamble(const char* fileName, int line, const LogLvl* lvl,
			LogMsg* lm);

private:
	LogLvl const* threshold;
	PRIME_OSTREAM* abort_stream;
	PRIME_OSTREAM* error_stream;
	PRIME_OSTREAM* warn_stream;
	PRIME_OSTREAM* info_stream;
	PRIME_OSTREAM* debug_stream;
	const SSFNET_STRING componentName;
	static LogMsg* bitBucket;
};

/**
 * \brief XXX LogMsg
 *
 * XXX
 */
class LogMsg {
	friend class LogStream;
	friend class LogStreamFactory;
public:
	LogMsg(const LogMsg& other);
	~LogMsg();
	LogMsg& operator<<(PRIME_OSTREAM& (*el)(PRIME_OSTREAM&));
	template<class T>
	LogMsg& operator<<(T& val) {
		ss << val;
		return *this;
	}
	template<class T>
	LogMsg& operator<<(const T& val) {
		ss << val;
		return *this;
	}
	//XXX anything special for manipulators that take args like setw() ?
private:
	LogMsg(const char* fileName = 0, int line = -1, const LogLvl* lvl =
			LogLvl::none(), LogStream* ls = 0);
	PRIME_OSTREAM* os;
	void(*f)();
	PRIME_STRING_STREAM ss;
};

/**
 * \brief XXX LogStreamFactory
 *
 * XXX
 */
class LogStreamFactory {
	friend class LogStream;
	friend class LogMsg;
public:
	typedef SSFNET_PAIR(LogStream*,void (*)()) PAIR;
	typedef SSFNET_MAP(SSFNET_STRING,LogStreamFactory::PAIR) STR_PAIR_MAP;
	static LogStreamFactory& instance();

	~LogStreamFactory();
	LogStream* createLogger(const char* componentName,
			void(*abortCleanupFunc)() = 0);
	bool setDefaultThreshold(LogLvl* threshold);
	LogStreamFactory::STR_PAIR_MAP& getLoggers() { return loggers; }

private:
	static LogStreamFactory* factory;
	static void do_abort();
	static void do_exit();

	void destroy(const SSFNET_STRING& componentName, const LogLvl* lvl,
			PRIME_OSTREAM*& stream);
	LogStreamFactory::STR_PAIR_MAP loggers;
	LogLvl* default_threshold;
	LogStreamFactory();
};

} //namespace ssfnet
} //namespace prime

#endif /*__LOGGER_FACTORY_H__*/
