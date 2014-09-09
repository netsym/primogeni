/*
 * divider :- partition ssf DML model using metis.
 */

#ifndef _GNU_SOURCE
#define _GNU_SOURCE  // _GNU_SOURCE enables getopt_long()
#endif

#include <fcntl.h>
#include <math.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

#include "getopt.h"

#include "ssfdml-runtime.h"

// METIS can either do recursive or k-way. Doesn't really matter.
#define METIS_USE_RECURSIVE

extern "C" {
#include "metis.h"
}

// return an edge weight for the graph; 0 if all the same
idxtype fx(double min_delay, double max_delay, double delay)
{
  idxtype ret;
  if(max_delay == min_delay) ret = 0;
  else {
    double x = (max_delay-delay)/(max_delay-min_delay);
    x = pow(x, 0.25); // make it favorable to large delays
    ret = (idxtype)(x*16384);
  }
  return ret;
}

/*
double gx(double min_delay, double max_delay, idxtype edge)
{
  double ret;
  if(max_delay == min_delay) ret = min_delay;
  else ret = max_delay-(edge-1024)*(max_delay-min_delay)/15360;
  return ret;
}
*/

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

  fprintf(subf, "# THIS FILE IS GENERATED AUTOMATICALLY BY METIS GRAPH PARTITIONER.\n"
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
    if(-1==mkstemp(df)) {
        fprintf(stderr, "UNABLE CREATE A TEMP FILE \"%s\"\n\n", df);
        return 2;
    }
    fprintf(subf, "DATAFILE_TEMP \"%s\"\n\n", df);
  }

  if(runtime->model) {

    /* partition using METIS */
    
    int inter_timeline_links = 0;
    double max_delay = 0;
    double min_delay = 1e308;

    int i;
    
    // populate timeline neighbors data structure
    for(std::vector<ssfdml_map*>::iterator miter = runtime->model->map_list.begin();
	miter != runtime->model->map_list.end(); miter++) {
      ssfdml_timeline* from = (*miter)->from->timeline;
      ssfdml_timeline* to = (*miter)->to->timeline;
      if(from != to) { // inter-timeline channel mapping
	if(max_delay < (*miter)->delay) max_delay = (*miter)->delay;
	if((*miter)->delay < min_delay) min_delay = (*miter)->delay;
	std::map<ssfdml_timeline*,double>::iterator citer = from->neighbors.find(to);
	if(citer != from->neighbors.end()) {
	  if((*citer).second < (*miter)->delay)
	    (*citer).second = (*miter)->delay; // can I change the value directly?
	} else {
	  inter_timeline_links++;
	  from->neighbors.insert(std::make_pair(to, (*miter)->delay));
	}
      }
    }

    int n = runtime->model->timeline_list.size();
    idxtype *xadj = new idxtype[n+1];
    xadj[0] = 0;
    idxtype* adjncy = inter_timeline_links ? new idxtype[inter_timeline_links] : 0;
    idxtype* adjwgt = inter_timeline_links ? new idxtype[inter_timeline_links] : 0;
    int wgtflag = 3;
    int numflag = 0;
    int nparts = runtime->machine ? runtime->machine->partitions.size() : 1;
    int options[5]; options[0] = 0;
    int edgecut;
    idxtype* vwgt = new idxtype[n];
    idxtype* part = new idxtype[n];

    float* tpwgts = new float[nparts];
    float sumwgt = 0;
    std::vector<ssfdml_partition*>::iterator ppiter;
    for(i = 0, ppiter = runtime->machine->partitions.begin();
	ppiter != runtime->machine->partitions.end(); ppiter++, i++) {
      tpwgts[i] = (float)(*ppiter)->multiplex;
      sumwgt += tpwgts[i];
    }
    for(i=0; i<(int)runtime->machine->partitions.size(); i++) 
      tpwgts[i] /= sumwgt;

    // global partitioning
    if(nparts == 1) {
      for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	  titer != runtime->model->timeline_list.end(); titer++) 
	(*titer)->mach = 0;
    } else {
      int x = 0; i = 0; 
      for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	  titer != runtime->model->timeline_list.end(); titer++, i++) {
	vwgt[i] = (*titer)->entities.size()+1;
	for(std::map<ssfdml_timeline*,double>::iterator citer = (*titer)->neighbors.begin();
	    citer != (*titer)->neighbors.end(); citer++) {
	  idxtype edge = fx(min_delay, max_delay, (*citer).second);
	  if(edge > 0) { // no need to do that if all edges weigh the same
	    adjncy[x] = (*citer).first->serialno;
	    adjwgt[x] = edge;
	    x++;
	  }
	}
	xadj[i+1] = x;
      }
      assert(x == 0 || x == inter_timeline_links);

      if(x == 0) wgtflag = 2;
#ifdef METIS_USE_RECURSIVE
      if(nparts <= 8) {
	METIS_WPartGraphRecursive(&n, xadj, x>0?adjncy:0, vwgt, x>0?adjwgt:0, &wgtflag,
				  &numflag, &nparts, tpwgts, options, &edgecut, part);
      } else {
#endif
	METIS_WPartGraphKway(&n, xadj, x>0?adjncy:0, vwgt, x>0?adjwgt:0, &wgtflag,
			     &numflag, &nparts, tpwgts, options, &edgecut, part);
#ifdef METIS_USE_RECURSIVE
      }
#endif

      i = 0;
      for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	  titer != runtime->model->timeline_list.end(); titer++, i++) {
	(*titer)->mach = part[i];
      }
    }

    // partitioning within each node
    i = 0;
    for(std::vector<ssfdml_partition*>::iterator piter = runtime->machine->partitions.begin();
	piter != runtime->machine->partitions.end(); piter++, i++) {
      nparts = (*piter)->multiplex;
      if(nparts == 1) {
	for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	    titer != runtime->model->timeline_list.end(); titer++) {
	  if((*titer)->mach == i) (*titer)->proc = 0;
	}
      } else {
	int x = 0;
	for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	    titer != runtime->model->timeline_list.end(); titer++)
	  if((*titer)->mach == i) (*titer)->intno = x++;

	wgtflag = 3;
	numflag = 0;
	options[0] = 0;
	n = 0; xadj[0] = 0; x = 0;
	for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	    titer != runtime->model->timeline_list.end(); titer++) {
	  if((*titer)->mach == i) {
	    vwgt[n] = (*titer)->entities.size()+1;
	    for(std::map<ssfdml_timeline*,double>::iterator citer = (*titer)->neighbors.begin();
		citer != (*titer)->neighbors.end(); citer++) {
	      if((*citer).first->mach != i) continue;
	      idxtype edge = fx(min_delay, max_delay, (*citer).second);
	      if(edge > 0) {
		adjncy[x] = (*citer).first->intno;
		adjwgt[x] = edge;
		x++;
	      }
	    }
	    xadj[++n] = x;
	  }
	}
	if(n == 0) continue;

	if(x == 0) wgtflag = 2;
#ifdef METIS_USE_RECURSIVE
	if(nparts <= 8) {
	  METIS_PartGraphRecursive(&n, xadj, x>0?adjncy:0, vwgt, x>0?adjwgt:0, &wgtflag,
				   &numflag, &nparts, options, &edgecut, part);
	} else {
#endif
	  METIS_PartGraphKway(&n, xadj, x>0?adjncy:0, vwgt, x>0?adjwgt:0, &wgtflag,
			      &numflag, &nparts, options, &edgecut, part);
#ifdef METIS_USE_RECURSIVE
	}
#endif
	x = 0;
	for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	    titer != runtime->model->timeline_list.end(); titer++) {
	  if((*titer)->mach == i) {
	    (*titer)->proc = part[x++];
	  }

	}
      } // nparts > 1
    } // piter iteration

    delete[] xadj;
    delete[] adjncy;
    delete[] adjwgt;
    delete[] vwgt;
    delete[] part;

    k = 0;
    for(std::vector<ssfdml_timeline*>::iterator titer = runtime->model->timeline_list.begin();
	titer != runtime->model->timeline_list.end(); titer++, k++)
      fprintf(subf, "TIMELINE [SNO %d MACH %d PROC %d]\n", 
	      k, (*titer)->mach, (*titer)->proc);
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

#if 0
  /*
   * MPI script
   */

  int fd = creat(mpiscp_filename, 0777);
  if(fd < 0) {
    perror("Can't open mp script output file");
    return 2;
  }
  FILE* mpif = fdopen(fd, "w");
  if(!mpif) {
    perror("Can't open mp script output file");
    return 2;
  }
  fprintf(mpif, 
	  "#!/bin/sh\n\n"
	  "mpirun -np %d -machinefile %s %s/OBJ-%s/%s -submodel %s $*\n",
	  runtime->machine->partitions.size(), machprof_filename, cwd, local_arch, 
	  runtime->executable, submodel_filename);
  fclose(mpif);
#endif
  
  delete runtime;
  return 0;
}
