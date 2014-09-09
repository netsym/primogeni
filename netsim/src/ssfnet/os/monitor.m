/**
 * \file monitor.h
 * \brief Header file for the Monitor and Aggregate classes.
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

#ifndef __MONITOR_H__
#define __MONITOR_H__

#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/community.h"

namespace prime {
namespace ssfnet {

class Monitor;

/**
 * used for monitors which are polling
 */
class MonitorTimer : public Timer {
public:
	typedef SSFNET_VECTOR(BaseEntity*) BaseEntityVec;
	typedef SSFNET_MAP(Community*, MonitorTimer*) Com2Timer;
	MonitorTimer(Community* com);
	~MonitorTimer();
	virtual void callback();
	void add(BaseEntity* e);
private:
	BaseEntityVec monitored;
};

/**
 * \brief Used to collect statistics
 *
 */
class Monitor : public ConfigurableEntity<Monitor,BaseEntity> {
 public:
	friend class MonitorTimer;
	state_configuration {
	 		shared configurable int64_t period {
	 			type=INT;
	 			default_value="500";
	 			doc_string="the period of polling (i.e. how long between polls) in milliseconds.";
	 		};
	 		shared configurable ResourceIdentifier to_monitor {
	 			type=RESOURCE_ID;
	 			default_value="";
				doc_string= "a list of entity relative ids which will have their state monitored";
	 		};
 	};

  /** The constructor. */
  Monitor();
  /** The destructor. */
  virtual ~Monitor();

  //will register itself with the entities it will monitor
  virtual void init();

  //no op
  virtual void wrapup();

 protected:
  MonitorTimer::Com2Timer timers;
};

/**
 * \brief Used to collect visualization statistics
 *
 */
class VizMonitor: public Timer {
 public:
	typedef SSFNET_MAP(UID_t, BaseEntity*) VizMap;
	VizMonitor(Community* com);
	~VizMonitor();
	void add(BaseEntity* e);
	void remove(UID_t uid);
	int size();
	virtual void callback();
	void printDebug();
 protected:
	VizMap* entities;
};

class Aggregate;

class Values {
public:
	typedef SSFNET_MAP(Community*, Values*) Com2Values;
	Values(): min(0),max(0),sample_count(0),sum(0) { }
	double min,max,sample_count,sum;
	SSFNET_SET(BaseConfigVar*) vars;
	SSFNET_SET(Aggregate*) aggregates;
	virtual void add(BaseEntity* e, int var_id);
	void add(Aggregate* e);
};

class VizValues: public Values {
public:
	VizValues();
	SSFNET_SET(BaseEntity*) entities;
	VirtualTime last_sample_time;
	virtual void add(BaseEntity* e, int var_id);
};


/**
 * \brief Used to collect statistics
 *
 */
class Aggregate : public ConfigurableEntity<Aggregate,BaseEntity> {
 public:
	state_configuration {
			shared configurable uint32_t var_id {
	 			type=INT;
	 			default_value="0";
	 			doc_string="the id of the variable which will be aggregated.";
	 		};
	 		shared configurable ResourceIdentifier to_aggregate {
	 			type=RESOURCE_ID;
	 			default_value="";
				doc_string= "a list of entity relative ids which will have their state aggregated";
	 		};

	 		//aggregates are special and even though these values are saved per-instance
	 		//we actually shaddow them for each community since nets can exist in multiple locations
	 		shared configurable double min {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="min of all the values at the sample time.";
				visualized=true;
				statistic=true;
	 		};
	 		shared configurable double max {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="max of all the values at the sample time.";
				visualized=true;
				statistic=true;
	 		};
	 		shared configurable int sample_count {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="the number of samples.";
				visualized=true;
				statistic=true;
	 		};
	 		shared configurable double sum {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="total of all the values at the sample time.";
				visualized=true;
				statistic=true;
	 		};
	};

  /** The constructor. */
	Aggregate();
  /** The destructor. */
  virtual ~Aggregate();

  //create a list of the entity variable maps we will be fetching values from....
  virtual void init();

  //no op
  virtual void wrapup();

  virtual void exportState(StateLogger* state_logger, double sampleInterval);

  virtual void exportVizState(StateLogger* state_logger, double sampleInterval);

  int getVarID();

  Values::Com2Values* getValues();

  //find the values and call update(com, vals)
  //this will be overridden my viz aggregate
  virtual Values* update(Community* com);

  //for viz aggregate we override this
  virtual Values* newValues();

 protected:
  //update the min,max,mean,median,sum
  void _update(Community* com, Values* vals);

  //we group the values we aggregate per community
  Values::Com2Values* values;
};

class VizAggregate : public ConfigurableEntity<VizAggregate,Aggregate> {
public:
	  /** The constructor. */
	VizAggregate();
	/** The destructor. */
	virtual ~VizAggregate();
	virtual Values* newValues();
protected:
	virtual Values* update(Community* com);
};


} // namespace ssfnet
} // namespace prime

#endif /*__MONITOR_H__*/
