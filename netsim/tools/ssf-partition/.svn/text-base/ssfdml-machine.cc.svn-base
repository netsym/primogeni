/*
 * ssfdml-machine :- collecting information of the hardware
 * platform for model execution.
 *
 * Information includes the total number of distributed machines to be
 * used for executing the model, and the type and the number of
 * processors for each distributed machine.
 */

#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

#include "ssfdml-machine.h"

// different than strdup, we use new[] instead of malloc.
static char* sstrdup(char* str)
{
  assert(str);
  int len = strlen(str);
  char* newstr = new char[len+1];
  strcpy(newstr, str);
  return newstr;
}

ssfdml_machine::ssfdml_machine(char* filename)
{
  valid = 0;
  dmlConfig* root = new dmlConfig(filename);
  valid = !parse_dml_machine(root);
  delete root;
}

ssfdml_machine::~ssfdml_machine()
{
  for(std::vector<ssfdml_partition*>::iterator iter = partitions.begin();
      iter != partitions.end(); iter++) {
    delete (*iter);
  }
}

int ssfdml_machine::parse_dml_machine(dmlConfig* cfg)
{
  // machine
  Enumeration* machenum = cfg->find("MACHINE");
  while(machenum->hasMoreElements()) {
    dmlConfig* mcfg = (dmlConfig*)machenum->nextElement();
    if(!DML_ISCONF(mcfg)) {
      fprintf(stderr, "ERROR: MACHINE attribute must be list\n");
      return 1;
    }
    if(parse_dml_partition(mcfg)) return 1;
  }
  delete machenum;

  processing_power = 0;
  for(std::vector<ssfdml_partition*>::iterator iter = partitions.begin();
      iter != partitions.end(); iter++)
    processing_power += (*iter)->multiplex;
  return 0;
}

int ssfdml_machine::parse_dml_partition(dmlConfig* cfg)
{
  // ipaddr
  char* ip = (char*)cfg->findSingle("IPADDR");
  if(!ip) {
    fprintf(stderr, "ERROR: missing MACHINE.IPADDR attribute\n");
    return 1;
  }
  if(DML_ISCONF(ip)) {
    fprintf(stderr, "ERROR: IPADDR attribute must be singleton\n");
    return 1;
  }
  ip = sstrdup(ip);

  // architecture
  char* arch = (char*)cfg->findSingle("ARCHITECTURE");
  if(!arch) {
    fprintf(stderr, "ERROR: missing MACHINE.ARCHITECTURE attribute\n");
    return 1;
  }
  if(DML_ISCONF(arch)) {
    fprintf(stderr, "ERROR: ARCHITECTURE attribute must be singleton\n");
    return 1;
  }
  arch = sstrdup(arch);

  // partition
  char* npstr = (char*)cfg->findSingle("PARTITION");
  int np;
  if(npstr) {
    if(DML_ISCONF(npstr)) {
      fprintf(stderr, "ERROR: PARTITION attribute must be singleton\n");
      return 1;
    }
    np = atoi(npstr);
  } else np = 1;

  ssfdml_partition* part = new ssfdml_partition
    (partitions.size(), ip, arch, np);
  partitions.push_back(part);
  return 0;
}

void ssfdml_machine::mergewith(ssfdml_machine* mach)
{
  if(!mach) return;
  processing_power += mach->processing_power;
  for(std::vector<ssfdml_partition*>::iterator iter = mach->partitions.begin();
      iter != mach->partitions.end(); iter++)
    partitions.push_back(*iter);
  delete mach;
}

void ssfdml_machine::dump()
{
  printf("PROCESSING POWER = %ld\n", processing_power);
  register int i = 0;
  for(std::vector<ssfdml_partition*>::iterator iter = partitions.begin();
      iter != partitions.end(); iter++, i++) {
    printf("%d: IPADDR=%s, ARCH==%s, PARTITIONS=%ld\n", i,
	   (*iter)->ip_address, (*iter)->arch, (*iter)->multiplex);
  }
}
