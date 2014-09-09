/**
 * dumpdml :- read dml file(s) and then print it out. It is used as
 *            a test example for the dml library.
 *
 * Author: Jason Liu <liux@cis.fiu.edu>
 */

#include <stdio.h>

#include <stdlib.h>
#include <map>
#include <string>

#include "dml.h"
using namespace prime;
using namespace dml;

#ifdef ENABLE_SOFTPM
extern "C" {
#include "pdl.h"
}

struct spm_root {
  dmlConfig* config;
};
#endif

void strsub(std::string& cp, std::string oldstr, std::string newstr, 
	    int num_times = -1)
{
  int startpos = 0;
  for(int i=0; i!=num_times; i++) {
    int loc = cp.find(oldstr, startpos);
    if(loc >= 0) {
      cp.replace(loc, oldstr.length(), newstr);
      startpos = loc + newstr.length();
    } else break;
  }
}

const char* stringification(std::string str)
{
  strsub(str, "\\", "\\\\");
  strsub(str, "\"", "\\\"");
  strsub(str, "\n", "\\n");
  strsub(str, "\t", "\\t");
  return str.c_str();
}

void list_dml(Configuration* cfg, int sp = 0)
{
  Enumeration* ss = cfg->find("*");
  while(ss->hasMoreElements()) {
    for(int i=0; i<sp; i++) printf(" ");
    Configuration* kidcfg = (Configuration*)ss->nextElement();
    if(!DML_ISCONF(kidcfg)) {
      printf("%s\t\"%s\"\n", Configuration::getKey(kidcfg), 
	     stringification((char*)kidcfg));
    } else {
      printf("%s [\n", Configuration::getKey(kidcfg));
      list_dml((Configuration*)kidcfg, sp+2);
      for(int i=0; i<sp; i++) printf(" ");
      printf("]\n");
    }
  }
  delete ss;
}

int main(int argc, char** argv)
{
  dmlConfig* cfg;

  if(argc < 2) {
#ifdef ENABLE_SOFTPM
    // if no parameters are given, we restore from the container
    struct spm_root* root = (struct spm_root*)prestore(0);
    if(!root) {
      fprintf(stderr, "ERROR: can't restore container.\n");
      return 1;
    }
    cfg = root->config;
#else
    fprintf(stderr, "Usage: %s <dmlfile> ...\n", argv[0]);
    return 1;
#endif
  } else {
    int ndmls = argc-1;
    char** fnames = new char*[ndmls+1];
    for(int k=0; k<ndmls; k++) fnames[k] = argv[1+k];
    fnames[ndmls] = 0;

    try { cfg = new dmlConfig(fnames); }
    catch(dml_exception& e) { fprintf(stderr, "%s\n", e.what()); return 1; }
    delete[] fnames;

#ifdef ENABLE_SOFTPM
    struct spm_root* root = (struct spm_root*)pcalloc(0, sizeof(struct spm_root));
    if(!root) {
      fprintf(stderr, "ERROR: can't allocate container.\n");
      return 1;
    }
    root->config = cfg;
    flush(0);
#endif
  }

  list_dml(cfg);
    
  /* Alternatively (starting from version 2.1), an easier way is to
     do the following. It'll print out the dml file verbatim. */
  /*
    char vbuf[10240]; // assuming it's less than 10 KB
    FILE* mfile = fopen(cfg->location().filename(), "r");
    if(!mfile) { 
      fprintf(stderr, "ERROR: can't open file: %s\n", 
	      cfg->location().filename());
      exit(1);
    }
    fseek(mfile, cfg->location().startpos(), SEEK_SET);
    int sz = cfg->location().endpos() - cfg->location().startpos();
    if(sz > sizeof(vbuf)-1) sz = sizeof(vbuf)-1;
    fread(vbuf, 1, sz, mfile); 
    vbuf[sz] = 0;
    printf("%s\n", vbuf);
    fclose(mfile);
  */
  
  delete cfg;
  return 0;
}
