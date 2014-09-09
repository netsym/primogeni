/*
 * dml-runtime :- simulation runtime information.
 */

#ifndef __SSFDML_RUNTIME_H__
#define __SSFDML_RUNTIME_H__

#include <map>

#include "dml.h"
#include "ssfdml-model.h"
#include "ssfdml-machine.h"

class ssfdml_runtime {
public:
  ssfdml_runtime(char* filename);
  ~ssfdml_runtime();

  int operator !() { return !valid; }

  char* executable;

  double startime;
  double endtime;

  std::map<std::string,std::string> envars;

  char* datafile;

  ssfdml_model* model;
  ssfdml_machine* machine;

  void dump();

protected:
  int valid;

  int parse_dml_runtime(dmlConfig*);
};

#endif /*__SSFDML_RUNTIME_H__*/
