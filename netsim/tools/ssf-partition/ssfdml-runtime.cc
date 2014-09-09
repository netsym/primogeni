/*
 * dml-runtime :- simulation runtime information.
 */

#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

#include "ssfdml-runtime.h"

// different than strdup, we use new[] instead of malloc.
static char* sstrdup(char* str)
{
  assert(str);
  int len = strlen(str);
  char* newstr = new char[len+1];
  strcpy(newstr, str);
  return newstr;
}

ssfdml_runtime::ssfdml_runtime(char* filename) :
  executable(0), model(0), machine(0)
{
  valid = 0;
  dmlConfig* root = new dmlConfig(filename);
  valid = !parse_dml_runtime(root);
  delete root;
}

ssfdml_runtime::~ssfdml_runtime()
{
  if(executable) delete executable;
  if(model) delete model;
  if(machine) delete machine;

  if(datafile) delete[] datafile;
}

int ssfdml_runtime::parse_dml_runtime(dmlConfig* cfg)
{
  // executable "name"
  /*
  char* execstr = (char*)cfg->findSingle("EXECUTABLE");
  if(!execstr) {
    fprintf(stderr, "ERROR: missing EXECUTABLE attribute\n");
    return 1;
  }
  if(DML_ISCONF(execstr)) {
    fprintf(stderr, "ERROR: EXECUTABLE attribute must be singleton\n");
    return 1;
  }
  executable = sstrdup(execstr);
  */

  // model "model.dml"
  char* modfile = (char*)cfg->findSingle("MODEL");
  if(modfile) {
    if(DML_ISCONF(modfile)) {
      fprintf(stderr, "ERROR: MODEL attribute must be singleton\n");
      return 1;
    }
    model = new ssfdml_model(modfile);
    if(!*model) return 1;
  } else model = 0;

  // machine "machine.dml"
  Enumeration* machenum = cfg->find("MACHINE");
  machine = 0;
  while(machenum->hasMoreElements()) {
    char* machfile = (char*)machenum->nextElement();
    if(DML_ISCONF(machfile)) {
      fprintf(stderr, "ERROR: MACHINE attribute must be singleton\n");
      return 1;
    }
    if(!machine) {
      machine = new ssfdml_machine(machfile);
      if(!*machine) return 1;
    } else {
      ssfdml_machine* m = new ssfdml_machine(machfile);
      if(!*m) return 1;
      machine->mergewith(m);
    }
  }
  delete machenum;

  // starttime
  char* startstr = (char*)cfg->findSingle("STARTTIME");
  if(!startstr) {
    fprintf(stderr, "ERROR: missing STARTTIME attribute\n");
    return 1;
  }
  if(DML_ISCONF(startstr)) {
    fprintf(stderr, "ERROR: STARTTIME attribute must be singleton\n");
    return 1;
  }
  startime = atof(startstr);

  // endtime
  char* endstr = (char*)cfg->findSingle("ENDTIME");
  if(!endstr) {
    fprintf(stderr, "ERROR: missing ENDTIME attribute\n");
    return 1;
  }
  if(DML_ISCONF(endstr)) {
    fprintf(stderr, "ERROR: ENDTIME attribute must be singleton\n");
    return 1;
  }
  endtime = atof(endstr);

  if(startime > endtime) {
    fprintf(stderr, "ERROR: ENDTIME (%g) must be no smaller than STARTTIME (%g)\n",
	    endtime, startime);
    return 1;
  }

  // environment
  dmlConfig* envcfg = (dmlConfig*)cfg->findSingle("ENVIRONMENT");
  if(envcfg) {
    if(!DML_ISCONF(envcfg)) {
      fprintf(stderr, "ERROR: ENVIRONMENT attribute must be list\n");
      return 1;
    }
    Enumeration* envenum = envcfg->find("*");
    while(envenum->hasMoreElements()) {
      char* valstr = (char*)envenum->nextElement();
      if(DML_ISCONF(valstr)) {
	fprintf(stderr, "ERROR: attributes in ENVIRONMENT must be singleton\n");
	return 1;
      }
      char* keystr = valstr+strlen(valstr)+1;
      if(!envars.insert(std::make_pair(keystr, valstr)).second) {
	fprintf(stderr, "ERROR: duplicated environment variable: %s\n", keystr);
	return 1;
      }
    }
    delete envenum;
  }

  // datafile "outfile"
  char* datstr = (char*)cfg->findSingle("DATAFILE");
  if(datstr) {
    if(DML_ISCONF(datstr)) {
      fprintf(stderr, "ERROR: DATAFILE attribute must be singleton\n");
      return 1;
    }
    datafile = sstrdup(datstr);
  } else datafile = 0;

  return 0;
}

void ssfdml_runtime::dump()
{
  /*
  printf("EXECUTABLE = %s\n", executable);
  */

  printf("STARTTIME = %g\n", startime);
  printf("ENDTIME = %g\n", endtime);

  for(std::map<std::string,std::string>::iterator iter = envars.begin();
      iter != envars.end(); iter++) {
    printf("ENVAR: %s=\"%s\"\n", (*iter).first.c_str(), 
	   (*iter).second.c_str());
  }
  printf("\n");

  printf("----- MODEL -----\n");
  if(model) model->dump();

  if(machine) {
    printf("----- MACHINE -----\n");
    machine->dump();
  }
}
