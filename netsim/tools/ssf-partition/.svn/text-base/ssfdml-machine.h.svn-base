/*
 * ssfdml-machine :- collecting information of the hardware
 * platform for model execution.
 *
 * Information includes the total number of distributed machines to be
 * used for executing the model, and the type and the number of
 * processors for each distributed machine.
 */

#ifndef __SSFDML_MACHINE_H__
#define __SSFDML_MACHINE_H__

#include <string.h>
#include <vector>

#include "dml.h"
using namespace prime::dml;

// Each distributed machine is an ssfdml_partition object.
class ssfdml_partition {
public:
  int index;        // unique index
  char* ip_address; // ip address of this machine
  char* arch;       // machine type
  long multiplex;   // number of processors on board

public:
  ssfdml_partition(int idx, char* ip, char* ar, long n) :
    index(idx), ip_address(ip), arch(ar), multiplex(n) {}

  ~ssfdml_partition() { delete[] ip_address; delete[] arch; }
};

// The whole hardware environment is encapsulated here.
class ssfdml_machine {
public:
  ssfdml_machine(char* filename);
  ~ssfdml_machine();

  int operator !() { return !valid; }

  long processing_power;   // total processors (assuming identical)
  std::vector<ssfdml_partition*> partitions;

  void mergewith(ssfdml_machine*); // adding one by one
  void dump(); // for debug only

protected:
  int valid;

  int parse_dml_machine(dmlConfig*);
  int parse_dml_partition(dmlConfig*);
};

#endif /*__SSFDML_MACHINE_H__*/
