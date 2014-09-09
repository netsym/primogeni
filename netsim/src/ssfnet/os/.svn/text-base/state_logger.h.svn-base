/**
 * \file state_logger.h
 * \brief Header file for the logging state to files and tcp streams
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

#ifndef __STATE_LOGGER_H__
#define __STATE_LOGGER_H__

#define TEST_VIZ_THROUGHPUT false

#include "os/ssfnet.h"
#include "os/virtual_time.h"
#include <stdio.h>
#include <string.h>

namespace prime {
namespace ssfnet {
#define TCP_STATE_BUF_SZ  1024

class Community;

class VarUpdate {
public:
	VarUpdate(int var_id, int type);
	virtual ~VarUpdate(){}
	typedef SSFNET_VECTOR(VarUpdate*) VarUpdateVector;
	virtual void pack(prime::ssf::ssf_compact* dp)=0;
	virtual void unpack(prime::ssf::ssf_compact* dp)=0;
	virtual void encode(int& offset, int size, char* buf)=0;
	virtual int size()=0;
	virtual SSFNET_STRING svalue()=0;
	int var_id;
	int type;
};

class LongUpdate : public VarUpdate {
public:
	LongUpdate(int var_id, int64_t value);
	LongUpdate(int var_id, uint64_t value);
	LongUpdate(int var_id, int32_t value);
	LongUpdate(int var_id, uint32_t value);
	LongUpdate(int var_id, int16_t value);
	LongUpdate(int var_id, uint16_t value);
	LongUpdate(int var_id);
	LongUpdate(const LongUpdate& o);
	virtual ~LongUpdate(){}
	virtual void pack(prime::ssf::ssf_compact* dp);
	virtual void unpack(prime::ssf::ssf_compact* dp);
	virtual void encode(int& offset, int size, char* buf);
	virtual int size();
	virtual SSFNET_STRING svalue();
protected:
	uint64_t value;
};

class StringUpdate : public VarUpdate {
public:
	StringUpdate(int var_id, SSFNET_STRING value);
	StringUpdate(int var_id);
	StringUpdate(const StringUpdate& o);
	virtual ~StringUpdate(){}
	virtual void pack(prime::ssf::ssf_compact* dp);
	virtual void unpack(prime::ssf::ssf_compact* dp);
	virtual void encode(int& offset, int size, char* buf);
	virtual int size();
	virtual SSFNET_STRING svalue();
protected:
	SSFNET_STRING value;
};

class DoubleUpdate : public VarUpdate {
public:
	DoubleUpdate(int var_id, double value);
	DoubleUpdate(int var_id);
	DoubleUpdate(const DoubleUpdate& o);
	virtual ~DoubleUpdate(){}
	virtual void pack(prime::ssf::ssf_compact* dp);
	virtual void unpack(prime::ssf::ssf_compact* dp);
	virtual void encode(int& offset, int size, char* buf);
	virtual int size();
	virtual SSFNET_STRING svalue();
protected:
	double value;
};

class BoolUpdate : public VarUpdate {
public:
	BoolUpdate(int var_id, bool value);
	BoolUpdate(int var_id);
	BoolUpdate(const BoolUpdate& o);
	virtual ~BoolUpdate(){}
	virtual void pack(prime::ssf::ssf_compact* dp);
	virtual void unpack(prime::ssf::ssf_compact* dp);
	virtual void encode(int& offset, int size, char* buf);
	virtual int size();
	virtual SSFNET_STRING svalue();
protected:
	bool value;
};

class StateUpdateEvent : public SSFNetEvent {
public:
	uint64_t time;
	UID_t uid;
	bool forViz, aggregate;
	VarUpdate::VarUpdateVector updates;
	StateUpdateEvent(SSFNetEvent::Type type, VirtualTime time, UID_t uid, bool forViz, bool aggregate);
	StateUpdateEvent(const StateUpdateEvent& o);
	~StateUpdateEvent();

	/**
	 * Add upate for a var which is a bool
	 */
	void addVarUpdate(int id, bool value);

	/**
	 * Add upate for a var which is a double
	 */
	void addVarUpdate(int id, double value);

	/**
	 * Add upate for a var which is an uint16
	 */
	void addVarUpdate(int id, uint16 value);

	/**
	 * Add upate for a var which is an uint32
	 */
	void addVarUpdate(int id, uint32 value);

	/**
	 * Add upate for a var which is an uint64
	 */
	void addVarUpdate(int id, uint64 value);

	/**
	 * Add upate for a var which is an int16
	 */
	void addVarUpdate(int id, int16 value);

	/**
	 * Add upate for a var which is an int32
	 */
	void addVarUpdate(int id, int32 value);

	/**
	 * Add upate for a var which is an int64
	 */
	void addVarUpdate(int id, int64 value);

	/**
	 * Add upate for a var which is a string
	 */
	void addVarUpdate(int id, SSFNET_STRING value);

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone();

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine. It is
	 * actually used to serialize the event object into a byte stream
	 * represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/**
	 * Unpack the emulation event, reverse process of the pack method.
	 */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * This method is the factor method for deserializing the event
	 * object. A new state update event object is created.
	 */
	static prime::ssf::Event* createStateUpdateEvent(prime::ssf::ssf_compact* dp);

	/** Used by SSF to declare an event class. */
	SSF_DECLARE_EVENT(StateUpdateEvent);
private:
	StateUpdateEvent();
};

class CreateNodeEvent : public SSFNetEvent {
public:
	int length;
	char* tlv;
	CreateNodeEvent();
	CreateNodeEvent(int length, char* tlv_);
	CreateNodeEvent(const CreateNodeEvent& o);
	~CreateNodeEvent();

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone();

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine. It is
	 * actually used to serialize the event object into a byte stream
	 * represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/**
	 * Unpack the emulation event, reverse process of the pack method.
	 */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * This method is the factor method for deserializing the event
	 * object. A new state update event object is created.
	 */
	static prime::ssf::Event* createCreateNodeEvent(prime::ssf::ssf_compact* dp);

	/** Used by SSF to declare an event class. */
	SSF_DECLARE_EVENT(CreateNodeEvent);
};

class AreaOfInterestUpdateEvent : public SSFNetEvent {
public:
	bool add;
	int count;
	UID_t* entities;
	AreaOfInterestUpdateEvent();
	AreaOfInterestUpdateEvent(bool add, int count, UID_t* entities);
	AreaOfInterestUpdateEvent(const AreaOfInterestUpdateEvent& o);
	~AreaOfInterestUpdateEvent();

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone();

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine. It is
	 * actually used to serialize the event object into a byte stream
	 * represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/**
	 * Unpack the emulation event, reverse process of the pack method.
	 */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * This method is the factor method for deserializing the event
	 * object. A new state update event object is created.
	 */
	static prime::ssf::Event* createAreaOfInterestUpdateEvent(prime::ssf::ssf_compact* dp);

	/** Used by SSF to declare an event class. */
	SSF_DECLARE_EVENT(AreaOfInterestUpdateEvent);
};


class VizExportRateUpdateEvent : public SSFNetEvent {
public:
	uint64 rate;
	VizExportRateUpdateEvent();
	VizExportRateUpdateEvent(uint64 rate);
	VizExportRateUpdateEvent(const VizExportRateUpdateEvent& o);
	~VizExportRateUpdateEvent();

	/**
	 * This method is required by SSF for any simulation event to clone
	 * itself, which is used when the simulation kernel delivers the
	 * event across processor boundaries.
	 */
	virtual Event* clone();

	/**
	 * This method is required by SSF to pack the event object before
	 * the event is about to be shipped to a remote machine. It is
	 * actually used to serialize the event object into a byte stream
	 * represented as an ssf_compact object.
	 */
	virtual prime::ssf::ssf_compact* pack();

	/**
	 * Unpack the emulation event, reverse process of the pack method.
	 */
	virtual void unpack(prime::ssf::ssf_compact* dp);

	/**
	 * This method is the factor method for deserializing the event
	 * object. A new state update event object is created.
	 */
	static prime::ssf::Event* createVizExportRateUpdateEvent(prime::ssf::ssf_compact* dp);

	/** Used by SSF to declare an event class. */
	SSF_DECLARE_EVENT(VizExportRateUpdateEvent);
};


class StateLogger : public prime::ssf::ssf_procedure_container {
public:
	virtual ~StateLogger();

	/**
	 * Initialize the logger;
	 * if the derived class  overrides this it must call this, i.e. StateLogger::init();
	 */
	virtual void init();

	/**
	 * close the logger
	 */
	virtual void wrapup();

	/**
	 * Called by read to implement the reader thread
	 */
	virtual void reader_thread(){};

	/**
	 * Called by write to implement the writer thread
	 */
	virtual void writer_thread(){};

	/*
	 * Send out an update
	 */
	void export_state(StateUpdateEvent* update);

	/**
	 * get the community
	 */
	Community* getCommunity() { return community; };

	/** This is the procedure (inside the simulator) for getting new state updates from the logger **/
	void arrival_process(prime::ssf::Process* p); //! SSF PROCEDURE SIMPLE

	/**
	 * return the input channel
	 */
	prime::ssf::inChannel* getInputChannel() { return input_channel; }

	/**
	 * return the output channel
	 */
	prime::ssf::outChannel* getOutputChannel() { return output_channel; }

	/** This is the reader thread for receiving update from the
	 * logger and importing them to the simulator.
	 *
	 * */
	static void read(prime::ssf::inChannel* ic, void* context);

	/** This is the writer thread for handling exported updates
	 * and sending them to the logger. */
	static void write(prime::ssf::outChannel* oc, void* context);

protected:
	StateLogger(Community* _community);
	Community* community;
	prime::ssf::inChannel* input_channel;
	prime::ssf::outChannel* output_channel;
	bool stop;
};


/**
 * \brief A thread which logs state to a file
 *
 */
class FileStateLogger : public StateLogger {
public:
	/**
	 * The constructor
	 */
	FileStateLogger(Community* _community, ofstream* file);

	/**
	 * The destructor.
	 */
	virtual ~FileStateLogger();

	/**
	 * Initialize the logger;
	 */
	virtual void init();

	/**
	 * close the logger
	 */
	virtual void wrapup();

	/**
	 * Called by read to implement the reader thread
	 */
	virtual void reader_thread();

	/**
	 * Called by write to implement the writer thread
	 */
	virtual void writer_thread();
private:
	ofstream* file;
};


/**
 * \brief A thread which logs state to a tcp stream
 *
 */
class TCPStateLogger : public StateLogger {
public:
	/**
	 * The constructor
	 */
	TCPStateLogger(Community* _community);

	/**
	 * The destructor.
	 */
	virtual ~TCPStateLogger();

	/**
	 * Initialize the logger;
	 */
	virtual void init();

	/**
	 * close the logger
	 */
	virtual void wrapup();

	/**
	 * Called by read to implement the reader thread
	 */
	virtual void reader_thread();

	/**
	 * Called by write to implement the writer thread
	 */
	virtual void writer_thread();

	static void setTarget(SSFNET_STRING ip, int port);
	static int safe_send(int s, char* buf, unsigned int bufsiz);
	static int safe_receive(int s, char* buf, unsigned int bufsiz);

private:
	int in_sock, out_sock;
	uint32 in_bs, out_bs;
	char* in_buf, * out_buf;
	static SSFNET_STRING ip;
	static int port;
#if TEST_VIZ_THROUGHPUT
public:
	ofstream writer_debug;
#endif
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__STATE_LOGGER_H__*/
