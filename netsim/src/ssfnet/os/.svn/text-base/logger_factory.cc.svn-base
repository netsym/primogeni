/**
 * \file logger_factory.cc
 * \brief Source file for private implementations of logging facilities.
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

#include "logger_factory.h"
#include "os/ssfnet_types.h"
#include "os/logger.h"
#include "string.h"

namespace prime {
namespace ssfnet {

LogLvl::LogLvl() : val(LogLvl::NONE_LVL),  str(none_str) { }

LogLvl::LogLvl(LogLvl::Type _val) :
	val(_val) {
	init();
	switch (_val) {
	case ABORT_LVL:
		this->str = abort_str;
		break;
	case ERROR_LVL:
		this->str = error_str;
		break;
	case WARN_LVL:
		this->str = warn_str;
		break;
	case INFO_LVL:
		this->str = info_str;
		break;
	case DEBUG_LVL:
		this->str = debug_str;
		break;
	case NONE_LVL:
		this->str = none_str;
		break;
	default:
		fprintf(stderr, "(1)INVALID log level %i!", _val);
		exit(100);
	}
}

LogLvl::~LogLvl() {
}

LogLvl LogLvl::operator=(const LogLvl& x) {
	this->val = x.val;
	this->str = x.str;
	return *this;
}

LogLvl::operator int() const {
	return val;
}

const SSFNET_STRING& LogLvl::toString() const {
	return *this->str;
}

const LogLvl& LogLvl::fromString(SSFNET_STRING& lvl) {
	if (lvl.compare(*abort_str) == 0)
		return *abort();
	if (lvl.compare(*error_str) == 0)
		return *error();
	if (lvl.compare(*warn_str) == 0)
		return *warn();
	if (lvl.compare(*info_str) == 0)
		return *info();
	if (lvl.compare(*debug_str) == 0)
		return *debug();
	if (lvl.compare(*none_str) == 0)
		return *none();
	fprintf(stderr, "(2)INVALID log level %s!", lvl.c_str());
	exit(100);
}

PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const LogLvl& x) {
	os << x.toString();
	return os;
}

int operator==(const LogLvl& x, const LogLvl& y) {
	return x.val == y.val;
}
int operator==(const LogLvl& x, const LogLvl::Type y) {
	return x.val == y;
}
int operator==(const LogLvl::Type x, const LogLvl& y) {
	return x == y.val;
}
int operator!=(const LogLvl& x, const LogLvl& y) {
	return x.val != y.val;
}
int operator!=(const LogLvl& x, const LogLvl::Type y) {
	return x.val != y;
}
int operator!=(const LogLvl::Type x, const LogLvl& y) {
	return x != y.val;
}

int operator>(const LogLvl& x, const LogLvl& y) {
	return x.val > y.val;
}
int operator>(const LogLvl& x, const LogLvl::Type y) {
	return x.val > y;
}
int operator>(const LogLvl::Type x, const LogLvl& y) {
	return x > y.val;
}
int operator>=(const LogLvl& x, const LogLvl& y) {
	return x.val >= y.val;
}
int operator>=(const LogLvl& x, const LogLvl::Type y) {
	return x.val >= y;
}
int operator>=(const LogLvl::Type x, const LogLvl& y) {
	return x >= y.val;
}

int operator<(const LogLvl& x, const LogLvl& y) {
	return x.val < y.val;
}
int operator<(const LogLvl& x, const LogLvl::Type y) {
	return x.val < y;
}
int operator<(const LogLvl::Type x, const LogLvl& y) {
	return x < y.val;
}
int operator<=(const LogLvl& x, const LogLvl& y) {
	return x.val <= y.val;
}
int operator<=(const LogLvl& x, const LogLvl::Type y) {
	return x.val <= y;
}
int operator<=(const LogLvl::Type x, const LogLvl& y) {
	return x <= y.val;
}

LogLvl* LogLvl::none_obj = NULL;
LogLvl* LogLvl::abort_obj = NULL;
LogLvl* LogLvl::error_obj = NULL;
LogLvl* LogLvl::warn_obj = NULL;
LogLvl* LogLvl::info_obj = NULL;
LogLvl* LogLvl::debug_obj = NULL;
SSFNET_STRING* LogLvl::none_str=NULL;
SSFNET_STRING* LogLvl::abort_str=NULL;
SSFNET_STRING* LogLvl::error_str=NULL;
SSFNET_STRING* LogLvl::warn_str=NULL;
SSFNET_STRING* LogLvl::info_str=NULL;
SSFNET_STRING* LogLvl::debug_str=NULL;
LogLvl* LogLvl::none(){init(); return none_obj; }
LogLvl* LogLvl::abort(){init(); return abort_obj; }
LogLvl* LogLvl::error(){init(); return error_obj; }
LogLvl* LogLvl::warn(){init(); return warn_obj; }
LogLvl* LogLvl::info(){init(); return info_obj; }
LogLvl* LogLvl::debug(){init(); return debug_obj; }
void LogLvl::init() {
	if(NULL != none_str) return;
	none_str=new SSFNET_STRING("none");
	abort_str=new SSFNET_STRING("abort");
	error_str=new SSFNET_STRING("error");
	warn_str=new SSFNET_STRING("warn");
	info_str=new SSFNET_STRING("info");
	debug_str=new SSFNET_STRING("debug");
	none_obj = new LogLvl(LogLvl::NONE_LVL);
	abort_obj = new LogLvl(LogLvl::ABORT_LVL);
	error_obj = new LogLvl(LogLvl::ERROR_LVL);
	warn_obj = new LogLvl(LogLvl::WARN_LVL);
	info_obj = new LogLvl(LogLvl::INFO_LVL);
	debug_obj = new LogLvl(LogLvl::DEBUG_LVL);
}


LogMsg::LogMsg(const LogMsg& other) :
	os(other.os), f(other.f), ss(PRIME_STRING_STREAM::in | PRIME_STRING_STREAM::out) {
	ss<< other.ss.str();
}
LogMsg::~LogMsg() {
	if (os)
		*os << ss.str();
	if (f)
		(*f)();
}

LogMsg& LogMsg::operator<<(PRIME_OSTREAM& (*el)(PRIME_OSTREAM&)) {
	ss<<*el;
	return *this;
}

LogMsg::LogMsg(const char* fileName, int line, const LogLvl* lvl, LogStream* ls) :
	os(0), f(0), ss(PRIME_STRING_STREAM::in | PRIME_STRING_STREAM::out) {
	if (lvl != LogLvl::none()) {
		assert(fileName!=0);
		assert(line>0);
		assert(ls!=0);
		ls->preamble(fileName, line, lvl, this);
	}
}

LogMsg* LogStream::bitBucket = NULL;

LogStream::~LogStream() {
	LogStreamFactory& lsf = LogStreamFactory::instance();
	if (abort_stream) {
		lsf.destroy(componentName, LogLvl::abort(), abort_stream);
	}
	if (error_stream) {
		lsf.destroy(componentName, LogLvl::error(), error_stream);
	}
	if (warn_stream) {
		lsf.destroy(componentName, LogLvl::warn(), warn_stream);
	}
	if (info_stream) {
		lsf.destroy(componentName, LogLvl::info(), info_stream);
	}
	if (debug_stream) {
		lsf.destroy(componentName, LogLvl::debug(), debug_stream);
	}
}

LogStream::LogStream(const char* componentName, LogLvl* threshold) :
	threshold(threshold), abort_stream(&std::cerr), error_stream(
			&std::cerr), warn_stream(&std::cout), info_stream(&std::cout),
			debug_stream(&std::cout), componentName(componentName) {
}

void LogStream::configureThreshold(LogLvl* t) {
	threshold = t;
}

void LogStream::configureStream(LogLvl* lvl, PRIME_OSTREAM* stream) {
	assert(0!=stream);
	switch (lvl->val) {
	case LogLvl::ABORT_LVL:
		abort_stream = stream;
		break;
	case LogLvl::ERROR_LVL:
		error_stream = stream;
		break;
	case LogLvl::WARN_LVL:
		warn_stream = stream;
		break;
	case LogLvl::INFO_LVL:
		info_stream = stream;
		break;
	case LogLvl::DEBUG_LVL:
		debug_stream = stream;
		break;
	default:
		fprintf(stderr, "(3)INVALID LogLvl %i!", lvl->val);
		exit(100);
	}
}

LogMsg LogStream::log(const char* fileName, int line, const LogLvl* lvl) {
	if (lvl->val > threshold->val || line < 0) {
		//printf("[bit bucket] LogStream::log threshold=%i, lvl=%i\n",threshold->val,lvl->val);
		return *bitBucket;
	}
	//printf("LogStream::log threshold=%i, lvl=%i\n",threshold->val,lvl->val);
	return LogMsg(fileName, line, lvl, this);
}

const int path_len_prefix=strlen(__FILE__)-strlen("ssfnet/os/logger_factory.cc");

void LogStream::preamble(const char* fileName, int line, const LogLvl* lvl,
		LogMsg* lm) {
	switch (lvl->val) {
	case LogLvl::ABORT_LVL:
		lm->f = LogStreamFactory::do_abort;
		lm->os = abort_stream;
		lm->ss<< "ABORT";
		break;
	case LogLvl::ERROR_LVL:
		lm->f = LogStreamFactory::do_exit;
		lm->os = error_stream;
		lm->ss<< "ERROR";
		break;
	case LogLvl::WARN_LVL:
		lm->os = warn_stream;
		lm->ss<< "WARN";
		break;
	case LogLvl::INFO_LVL:
		lm->os = info_stream;
		lm->ss<< "INFO";
		break;
	case LogLvl::DEBUG_LVL:
		lm->os = debug_stream;
		lm->ss<< "DEBUG";
		break;
	default:
		fprintf(stderr, "(4)INVALID LogLvl %i!", lvl->val);
		exit(100);
	}
#if PRIME_SSF_DISTSIM
	lm->ss<<"["<<(prime::ssf::ssf_machine_index()+1)<<":"<< prime::ssf::ssf_processor_index()<<"[" << (fileName+path_len_prefix) << ":" << line << "]] ";
	//lm->ss<<"["<<(prime::ssf::ssf_machine_index()+1)<<":"<< prime::ssf::ssf_processor_index()<<"[" << componentName << "::" << (fileName+path_len_prefix) << ":" << line << "]] ";
#else
	//lm->ss<< "[" << componentName << "::" << (fileName+path_len_prefix) << ":" << line << "] ";
	lm->ss<< "["<< "::" << (fileName+path_len_prefix) << ":" << line << "] ";
#endif
}

LogStreamFactory* LogStreamFactory::factory = 0;

LogStreamFactory::LogStreamFactory() {
	default_threshold= new LogLvl(LogLvl::WARN_LVL);
}

LogStreamFactory::~LogStreamFactory() {
}

LogStreamFactory& LogStreamFactory::instance() {
	if (LogStreamFactory::factory == 0) {
		LogStreamFactory::factory = new LogStreamFactory();
		LogStreamFactory::factory->createLogger("default");
		LogStream::bitBucket = new LogMsg();
	}
	return *LogStreamFactory::factory;
}

LogStream* LogStreamFactory::createLogger(const char* componentName,
		void(*abortCleanupFunc)()) {
	SSFNET_STRING n(componentName);
	LogStreamFactory::STR_PAIR_MAP::iterator it = loggers.find(n);
	LogStream* ls = NULL;
	if (it != loggers.end()) {
		ls = it->second.first;
		if (NULL != abortCleanupFunc)
			it->second.second = abortCleanupFunc;
	} else {
		ls = new LogStream(componentName, new LogLvl(*default_threshold));
		this->loggers.insert(
				SSFNET_MAKE_PAIR(componentName,SSFNET_MAKE_PAIR(ls,abortCleanupFunc)));
	}
	return ls;
}

bool LogStreamFactory::setDefaultThreshold(LogLvl* threshold) {
	LogStreamFactory::STR_PAIR_MAP::iterator it;
	for (it = loggers.begin(); it != loggers.end(); it++) {
		LogStream* ls = (*it).second.first;
		ls->configureThreshold(threshold);
	}
	delete default_threshold;
	default_threshold=threshold;
	return false;
}

void LogStreamFactory::destroy(const SSFNET_STRING& componentName,
		const LogLvl* lvl, PRIME_OSTREAM*& stream) {
	//XXX TODO handle close the stream, etc
	stream->flush();
	stream = 0;
}

void LogStreamFactory::do_abort() {
	LogStreamFactory::STR_PAIR_MAP::iterator it;
	for (it = instance().loggers.begin(); it != instance().loggers.end(); it++) {
		//call cleanup
		if ((*it).second.second)
			(*it).second.second();
	}
	abort();
}

void LogStreamFactory::do_exit() {
	LogStreamFactory::STR_PAIR_MAP::iterator it;
	for (it = instance().loggers.begin(); it != instance().loggers.end(); it++) {
		//call cleanup
		if ((*it).second.second)
			(*it).second.second();
	}
	ssfnet_throw_exception(SSFNetException::other_exception,"see preceding error message!");
	//exit(100);
}

} //namespace ssfnet
} //namespace prime
