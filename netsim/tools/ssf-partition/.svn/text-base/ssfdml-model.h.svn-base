/*
 * ssfdml-model :- parse SSF DML model.
 */

#ifndef __SSFDML_MODEL_H__
#define __SSFDML_MODEL_H__

#include <map>
#include <set>
#include <string>
#include <vector>

#include "dml.h"
using namespace prime::dml;

class ssfdml_timeline;
class ssfdml_entity;
class ssfdml_align;
class ssfdml_map;
class ssfdml_model;

/*
 * A DML model contains a list of user-defined entities, mappings from
 * out-channels to in-channels, and a list of alignment relationships
 * between entities, which will be resolved with a list of timelines.
 */
class ssfdml_model {
 public:
  std::vector<ssfdml_entity*> entity_list;
  std::vector<ssfdml_map*> map_list;
  std::vector<ssfdml_timeline*> timeline_list;

  ssfdml_model(char* filename);
  ~ssfdml_model();

  int operator !() { return !valid; }

  void dump(); // for debug only

protected:
  int valid; // mark whether the model is parsed OK

  // the following data structure are ephemeral
  std::set<std::string>* id_lookup; // used to prevent duplicate (absolute) id names
  std::map<std::string,ssfdml_entity*>* entity_lookup; // map from entity id to the data structure
  std::vector<ssfdml_align*>* align_list; // temporarily store alignment definitions

  int parse_dml_model(dmlConfig*);
  int parse_dml_cluster(char*, dmlConfig*);
  int parse_dml_entity(char*, dmlConfig*);
  int parse_dml_align(char*, dmlConfig*);
  int parse_dml_map(char*, dmlConfig*);
  int resolve_dml_model();
  void clusterize();
};

// SSF entity definition.
class ssfdml_param {
 public:
  enum { INT, FLOAT, STRING };
  int type;
  union {
    int value_int;
    double value_float;
    char* value_string;
  };
  ssfdml_param(int x);
  ssfdml_param(double x);
  ssfdml_param(char* x);
  ~ssfdml_param();
};

class ssfdml_entity {
public:
  int serialno;

  char* id;
  char* instanceof;
  std::vector<ssfdml_param*> params;
  dmlConfig* configs;

  ssfdml_timeline* timeline;
  std::vector<std::string> ext_outc;
  std::vector<std::string> ext_inc;

public:
  ssfdml_entity() : id(0), instanceof(0), configs(0) {}
  ~ssfdml_entity() {
    if(id) delete[] id;
    if(instanceof) delete[] instanceof;
    for(std::vector<ssfdml_param*>::iterator piter = params.begin();
	piter != params.end(); piter++) delete (*piter);
    if(configs) delete configs;
  }

  void dump();
};

// Channel mapping information from the DML file.
class ssfdml_map {
public:
  union {
    char* unresolved_from;
    ssfdml_entity* from;
  };

  union {
    char* unresolved_from_port;
    int from_port;
  };

  union {
    char* unresolved_to;
    ssfdml_entity* to;
  };

  union {
    char* unresolved_to_port;
    int to_port;
  };

  double delay;

  void dump(); // for debug only
};

// Alignment definitions from the DML file.
class ssfdml_align {
public:
  union {
    char* unresolved_from;
    ssfdml_entity* from;
  };

  union {
    char* unresolved_to;
    ssfdml_entity* to;
  };
};

// Contains necessary information of an SSF timeline.
class ssfdml_timeline {
public:
  int serialno;
  std::set<ssfdml_entity*> entities;

  // used to partition timeline connectivity graph
  std::map<ssfdml_timeline*,double> neighbors;
  int intno, mach, proc;

  void dump();
};

#endif /*__SSFDML_MODEL_H__*/
