/*
 * ssfmodel :- parse DML for partitioned ssf model.
 */

#ifndef __PRIME_SSF_SSFMODEL_H__
#define __PRIME_SSF_SSFMODEL_H__

#include "dml.h"
#include "api/ssftype.h"
#include "api/ssftime.h"

namespace prime {
namespace ssf {

class ssf_dml_timeline;
class ssf_dml_entity;
class ssf_dml_align;
class ssf_dml_map;
class ssf_dml_model;
class ssf_entity_dml_param;

typedef SSF_QVECTOR(ssf_dml_entity*) SSF_DML_ENT_VECTOR;
typedef SSF_QVECTOR(ssf_entity_dml_param*) SSF_DML_PARAM_VECTOR;
typedef SSF_QMAP(SSF_STRING,int) SSF_DML_STRINT_MAP;
typedef SSF_QMAP(SSF_STRING,SSF_STRING) SSF_DML_STRSTR_MAP;
typedef SSF_QVECTOR(int) SSF_DML_INT_VECTOR;
typedef SSF_QVECTOR(ssf_dml_timeline*) SSF_DML_TML_VECTOR;
typedef SSF_QVECTOR(ssf_dml_map*) SSF_DML_MAP_VECTOR;

class ssf_dml_timeline {
 public:
  int serialno;  // index in ssf_dml_model::timeline_list
  int location;  // location number (machine id)
  int processor; // processor number on machine id (-1 = anywhere)
  SSF_DML_ENT_VECTOR entities; // list of entities of the timeline

 public:
  ssf_dml_timeline(int seqno, int loc);
  ssf_dml_timeline(int seqno, int loc, int prc);

#if PRIME_DML_DEBUG
  void dump();
#endif
};

class ssf_entity_dml_param;

class ssf_dml_entity {
 public:
  int serialno;            // index in ssf_dml_model::entity_list
  SSF_STRING instanceof;  // name of the entity class
  prime::dml::dmlConfig* configs;      // undecoded DML node
  SSF_DML_PARAM_VECTOR params;  // list of ssf_entity_dml_param objects
  SSF_DML_STRINT_MAP ext_outc; // list of *external* outChannels defined
  SSF_DML_STRINT_MAP ext_inc;  // list of *external* inChannels defined
  ssf_dml_timeline* timeline;  // alignment

 public:
  ssf_dml_entity(int seqno);
  ~ssf_dml_entity();

#if PRIME_DML_DEBUG
  void dump();
#endif
};

class ssf_dml_map {
 public:
  ssf_dml_entity* from; // from entity
  int from_port;    // index of external outChannel of the entity
  ssf_dml_entity* to;   // to entity
  int to_port;      // index of external inChannel of the entity
  ltime_t delay;      // map delay

#if PRIME_DML_DEBUG
 public:
  void dump();
#endif
};

class ssf_dml_model {
 public:
  ltime_t startime;     // simulation start time
  ltime_t endtime;      // simulation end time
  int datafile_keep;    // whether to keep the data file after use
  SSF_STRING datafile; // name of the output data file
  SSF_DML_STRSTR_MAP envars;  // environment variables
  SSF_DML_INT_VECTOR partitions; // machine partitions for entire system
  SSF_DML_TML_VECTOR timeline_list;  // list of timelines
  SSF_DML_ENT_VECTOR entity_list;      // list of entities
  SSF_DML_MAP_VECTOR map_list;            // list of mappings

  ssf_dml_model(char* filename);
  ~ssf_dml_model();

  int operator !() { return !valid; }

#if PRIME_DML_DEBUG
  void dump();
#endif

 protected:
  int valid;

  int parse_dml_submodel(prime::dml::dmlConfig*);
  int parse_dml_entity(prime::dml::dmlConfig*, ssf_dml_entity*);
  int parse_dml_map(prime::dml::dmlConfig*);
};

}; // namespace ssf
}; // namespace prime

#endif /*__PRIME_SSF_SSFMODEL_H__*/

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
