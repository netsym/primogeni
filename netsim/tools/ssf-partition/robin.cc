/*
 * robin :- partition ssf DML model in round-robin fashion.
 */

#ifndef _GNU_SOURCE
#define _GNU_SOURCE  // _GNU_SOURCE enables getopt_long()
#endif

#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

#include "getopt.h"
#include "ssfdml-runtime.h"

static void usage(char* progname)
{
  fprintf(stderr, "Usage: %s [-hmo] [-help] [-machprof output_machprof]\n"
	  "    [-submodel <output_submodel>] <runtime_dml>\n", progname);
  exit(1);
}


int main(int argc, char *argv[])
{
  int k;

  // parse command line options
  char* submodel_outfile = 0;
  char* machprof_outfile = 0;
  for(;;) {
    static struct option long_options[] = {
      {"help", 0, 0, 'h'},
      {"machprof", 1, 0, 'm'},
      {"submodel", 1, 0, 'o'},
      {0, 0, 0, 0}
    };
    int c = getopt_long(argc, argv, "hm:o:", long_options, 0);
    if(c == -1) break;
    switch (c) {
    case 'h':
      usage(argv[0]);
      break;
    case 'm':
      machprof_outfile = optarg;
      break;
    case 'o':
      submodel_outfile = optarg;
      break;
    default:
      break;
    }
  }
  if(optind+1 != argc) usage(argv[0]);
  char* runtime_filename = argv[optind++];

  // open and parse runtime dml file
  ssfdml_runtime* runtime = new ssfdml_runtime(runtime_filename);
  if(!*runtime) return 1;

  /*
   * sub-model
   */

  // open submodel file if specified
  FILE* subf;
  if(submodel_outfile) {
    subf = fopen(submodel_outfile, "w");
    if(!subf) {
      perror("Can't open sub-model output file");
      return 2;
    }
  } else subf = stdout;

  fprintf(subf, "# THIS FILE IS GENERATED AUTOMATICALLY BY ROBIN GRAPH PARTITIONER.\n"
	  "# DO NOT CHANGE UNLESS YOU KNOW WHAT YOU ARE DOING.\n\n");

  // STARTTIME and ENDTIME
  fprintf(subf, "STARTTIME %g\n", runtime->startime);
  fprintf(subf, "ENDTIME %g\n\n", runtime->endtime);

  // ENVIRONMENT
  fprintf(subf, "ENVIRONMENT [\n");
  for(std::map<std::string,std::string>::iterator iter = runtime->envars.begin();
      iter != runtime->envars.end(); iter++) {
    fprintf(subf, "  %s \"%s\"\n", (*iter).first.c_str(),
	    (*iter).second.c_str());
  }
  fprintf(subf, "]\n\n");

  // MACHINE
  if(runtime->machine) {
    k = 0;
    for(std::vector<ssfdml_partition*>::iterator iter = 
	  runtime->machine->partitions.begin();
	iter != runtime->machine->partitions.end(); iter++, k++)
      fprintf(subf, "MACHINE [ SNO %d NPROCS %ld ]\n", 
	      k, (*iter)->multiplex);
  } else fprintf(subf, "MACHINE [ SNO 0 NPROCS 1 ]\n");
  fprintf(subf, "\n");

  // DATAFILE
  if(runtime->datafile) {
    fprintf(subf, "DATAFILE_KEEP \"%s\"\n\n", runtime->datafile);
  } else {
    char df[512];
    sprintf(df, ".out.%s.XXXXXX", runtime->executable);
    mkstemp(df);
    fprintf(subf, "DATAFILE_TEMP \"%s\"\n\n", df);
  }

  if(runtime->model) {

    // TIMELINE
    ssfdml_partition** who = 0;
    int* whoproc = 0;
    if(runtime->machine) {
      who = new ssfdml_partition*[runtime->machine->processing_power];
      whoproc = new int[runtime->machine->partitions.size()];
      int idx = 0;
      int idxproc = 0;
      for(std::vector<ssfdml_partition*>::iterator iter = runtime->machine->partitions.begin();
	  iter != runtime->machine->partitions.end(); iter++) {
	for(k=0; k<(*iter)->multiplex; k++) who[idx++] = (*iter);
	whoproc[idxproc++] = 0;
      }
    }

    k = 0;
    for(std::vector<ssfdml_timeline*>::iterator iter = runtime->model->timeline_list.begin();
	iter != runtime->model->timeline_list.end(); iter++, k++) {
      int which = who ? who[k%runtime->machine->processing_power]->index : 0;
      int whichproc = whoproc[which]++;
      if(whoproc[which] == runtime->machine->partitions[which]->multiplex)
	whoproc[which] = 0;
      fprintf(subf, "TIMELINE [SNO %d MACH %d PROC %d]\n", k, which, whichproc);
    }

    if(who) delete[] who;
    if(whoproc) delete[] whoproc;
    fprintf(subf, "\n");

    // ENTITY
    int sno = 0;
    for(std::vector<ssfdml_entity*>::iterator iter = runtime->model->entity_list.begin();
	iter != runtime->model->entity_list.end(); iter++, sno++) {
      fprintf(subf, "ENTITY [ SNO %d ", sno);
      // INSTANCEOF
      fprintf(subf, "INSTANCEOF \"%s\"\n", (*iter)->instanceof);
      // PARAMS
      fprintf(subf, "  PARAMS [");
      for(std::vector<ssfdml_param*>::iterator piter = (*iter)->params.begin();
	  piter != (*iter)->params.end(); piter++) {
	if((*piter)->type == ssfdml_param::INT) 
	  fprintf(subf, " INT %d", (*piter)->value_int);
	else if((*piter)->type == ssfdml_param::FLOAT) 
	  fprintf(subf, " FLOAT %g", (*piter)->value_float);
	else /*if((*piter)->type == ssfdml_param::STRING)*/
	  fprintf(subf, " STRING \"%s\"", (*piter)->value_string);
      }
      fprintf(subf, " ]\n");
      // CONFIGURE
      if((*iter)->configs) {
	fprintf(subf, "  CONFIGURE ");
	(*iter)->configs->print(subf, 2);
	fprintf(subf, "\n");
      }
      // EXTOUT and EXTIN
      k = 0;
      for(std::vector<std::string>::iterator ociter = (*iter)->ext_outc.begin();
	  ociter != (*iter)->ext_outc.end(); ociter++, k++)
	fprintf(subf, "  EXTOUT [ name \"%s\" port %d ]\n", (*ociter).c_str(), k);
      k = 0;
      for(std::vector<std::string>::iterator iciter = (*iter)->ext_inc.begin();
	  iciter != (*iter)->ext_inc.end(); iciter++, k++)
	fprintf(subf, "  EXTIN [ name \"%s\" port %d ]\n", (*iciter).c_str(), k);
      // TIMELINE
      fprintf(subf, "  TIMELINE %d\n", (*iter)->timeline->serialno);
      fprintf(subf, "]\n");
    }
    fprintf(subf, "\n");

    // MAP
    for(std::vector<ssfdml_map*>::iterator iter = runtime->model->map_list.begin();
	iter != runtime->model->map_list.end(); iter++) {
      fprintf(subf, "MAP [ FROM %d(%d) TO %d(%d) DELAY %g ]\n", 
	      (*iter)->from->serialno, (*iter)->from_port, 
	      (*iter)->to->serialno, (*iter)->to_port, (*iter)->delay);
    }

  } // end of if(runtime->model)

  fclose(subf);

  /*
   * machine profile
   */

  if(machprof_outfile) {
    FILE* mpf = fopen(machprof_outfile, "w");
    if(!mpf) {
      perror("Can't open machine profile output file");
      return 2;
    }
  
    if(runtime->machine) {
      for(std::vector<ssfdml_partition*>::iterator iter = runtime->machine->partitions.begin();
	  iter != runtime->machine->partitions.end(); iter++)
	fprintf(mpf, "%s\n", (*iter)->ip_address);
    } else {
      fprintf(mpf, "localhost\n");
    }
  
    fclose(mpf);
  }


  delete runtime;
  return 0;
}
