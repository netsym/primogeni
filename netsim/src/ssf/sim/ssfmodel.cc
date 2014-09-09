/*
 * ssfmodel :- parse DML for partitioned ssf model.
 */

#include <ctype.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "ssf.h"
#include "sim/ssfmodel.h"

#if defined(PRIME_SSF_ARCH_WINDOWS)
#define STRCASECMP _stricmp
#else
#include <strings.h>
#define STRCASECMP strcasecmp
#endif

using namespace prime::dml;

namespace prime {
namespace ssf {

ssf_dml_model::ssf_dml_model(char* filename)
{
  dmlConfig* root = new dmlConfig(filename);
  valid = !parse_dml_submodel(root);
  delete root;
}

ssf_dml_model::~ssf_dml_model()
{
  if(!valid) return;

  envars.clear();
  partitions.clear();

  for(SSF_DML_TML_VECTOR::iterator tm_iter = timeline_list.begin();
      tm_iter != timeline_list.end(); tm_iter++)
    delete (*tm_iter);
  timeline_list.clear();

  for(SSF_DML_ENT_VECTOR::iterator ent_iter = entity_list.begin();
      ent_iter != entity_list.end(); ent_iter++)
    delete (*ent_iter);
  entity_list.clear();

  for(SSF_DML_MAP_VECTOR::iterator map_iter = map_list.begin();
      map_iter != map_list.end(); map_iter++)
    delete (*map_iter);
  map_list.clear();
}

int ssf_dml_model::parse_dml_submodel(dmlConfig* root)
{
  // STARTTIME
  char* str = (char*)root->findSingle("STARTTIME");
  if(!str || dmlConfig::isConf(str)) {
    char msg[256];
    sprintf(msg, "wrong STARTTIME in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
#if defined(PRIME_SSF_LTIME_LONG)
  startime = (ltime_t)atol(str);
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#if HAVE_ATOLL
  startime = (ltime_t)atoll(str);
#else
  startime = (ltime_t)atol(str);
#endif
#elif defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
  startime = (ltime_t)atof(str);
#else
#error "unknown ltime_t type"
#endif

  // ENDTIME
  str = (char*)root->findSingle("ENDTIME");
  if(!str || dmlConfig::isConf(str)) {
    char msg[256];
    sprintf(msg, "wrong ENDTIME in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
#if defined(PRIME_SSF_LTIME_LONG)
  endtime = (ltime_t)atol(str);
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#if HAVE_ATOLL
  endtime = (ltime_t)atoll(str);
#else
  endtime = (ltime_t)atol(str);
#endif
#elif defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
  endtime = (ltime_t)atof(str);
#else
#error "unknown ltime_t type"
#endif

  if(startime > endtime) {
    char msg[256];
    sprintf(msg, "ENDTIME(%g) smaller than STARTTIME(%g)",
	    (double)endtime, (double)startime);
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }

  // ENVIRONMENT
  dmlConfig* cfg = (dmlConfig*)root->findSingle("ENVIRONMENT");
  if(cfg) {
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong ENVIRONMENT in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    Enumeration* e = cfg->find("*"); // list dml attributes
    while(e->hasMoreElements()) {
      char* v = (char*)e->nextElement();
      char* k = &v[strlen(v)+1];
      envars.insert(std::make_pair(k,v));
    }
    delete e;
  }

  // MACHINE
  Enumeration* e = root->find("MACHINE");
  while(e->hasMoreElements()) {
    cfg = (dmlConfig*)e->nextElement();
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong MACHINE in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    str = (char*)cfg->findSingle("SNO");
    if(!str || dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong MACHINE.SNO in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int sno = atoi(str);
    str = (char*)cfg->findSingle("NPROCS");
    if(!str || dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong MACHINE.NPROCS in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int nprocs = atoi(str);
    assert(sno == (int)partitions.size()); // assume sno is increasing
    partitions.push_back(nprocs);
    //partitions[sno] = nprocs;
  }
  delete e;
  
  // DATAFILE
  str = (char*)root->findSingle("DATAFILE_KEEP");
  if(str) {
    if(dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong DATAFILE_KEEP in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    datafile_keep = 1;
    datafile = str;
  } else {
    str = (char*)root->findSingle("DATAFILE_TEMP");
    if(!str) {
      char msg[256];
      sprintf(msg, "missing DATAFILE_KEEP or DATAFILE_TEMP in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    if(dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong DATAFILE_TEMP in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    datafile_keep = 0;
    datafile = str;
  }    

  // TIMELINE
  e = root->find("TIMELINE");
  while(e->hasMoreElements()) {
    cfg = (dmlConfig*)e->nextElement();
    if(!cfg || !dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong TIMELINE in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    str = (char*)cfg->findSingle("SNO");
    if(!str || dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong TIMELINE.SNO in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int sno = atoi(str);
    str = (char*)cfg->findSingle("MACH");
    if(!str || dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong TIMELINE.MACH in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int mach = atoi(str);
    str = (char*)cfg->findSingle("PROC");
    if(!str || dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong TIMELINE.PROC in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int proc = atoi(str);
    assert(sno == (int)timeline_list.size());
    timeline_list.push_back(new ssf_dml_timeline(sno, mach, proc));
    //timeline_list[sno] = new ssf_dml_timeline(sno, mach, proc);
  }
  delete e;

  // ENTITY
  e = root->find("ENTITY");
  while(e->hasMoreElements()) {
    cfg = (dmlConfig*)e->nextElement();
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    str = (char*)cfg->findSingle("SNO");
    if(!str || dmlConfig::isConf(str)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.SNO in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int sno = atoi(str);
    ssf_dml_entity* ent = new ssf_dml_entity(sno);
    assert(sno == (int)entity_list.size());
    entity_list.push_back(ent);
    //ssf_dml_entity* ent = entity_list[sno] = new ssf_dml_entity(sno);
    if(parse_dml_entity(cfg, ent)) return 1;
  }
  delete e;

  // MAP
  e = root->find("MAP");
  while(e->hasMoreElements()) {
    cfg = (dmlConfig*)e->nextElement();
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong MAP in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    if(parse_dml_map(cfg)) return 1;
  }
  delete e;

  return 0;
}

int ssf_dml_model::parse_dml_entity(dmlConfig* entcfg, ssf_dml_entity* ent)
{
  // INSTANCEOF
  char* str = (char*)entcfg->findSingle("INSTANCEOF");
  if(!str || dmlConfig::isConf(str)) {
    char msg[256];
    sprintf(msg, "wrong ENTITY.INSTANCEOF in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  ent->instanceof = str; // string assignment

  // PARAMS
  dmlConfig* cfg = (dmlConfig*)entcfg->findSingle("PARAMS");
  if(cfg) {
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.PARAMS in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    Enumeration* e = cfg->find("*");
    while(e->hasMoreElements()) {
      str = (char*)e->nextElement();
      if(dmlConfig::isConf(str)) {
	char msg[256];
	sprintf(msg, "wrong ENTITY.PARAMS in sub-model DML");
	ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
	return 1;
      }
      char* k = &str[strlen(str)+1];
      if(!STRCASECMP(k, "INT")) {
	ent->params.push_back(new ssf_entity_dml_param(atol(str)));
      } else if(!STRCASECMP(k, "FLOAT")) {
	ent->params.push_back(new ssf_entity_dml_param(atof(str)));
      } else if(!STRCASECMP(k, "STRING")) {
	ent->params.push_back(new ssf_entity_dml_param(str));
      } else {
	char msg[256];
	sprintf(msg, "wrong ENTITY.PARAMS type in sub-model DML");
	ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
	return 1;
      }
    }
    delete e;
  }

  // CONFIGURE
  cfg = (dmlConfig*)entcfg->findSingle("CONFIGURE");
  if(cfg) {
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.CONFIGURE in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    ent->configs = (dmlConfig*)cfg->clone(); // deep copy
  }

  // EXTOUT [ NAME "name" PORT port ]
  Enumeration* e = entcfg->find("EXTOUT");
  while(e->hasMoreElements()) {
    cfg = (dmlConfig*)e->nextElement();
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.EXTOUT in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }

    char* port = (char*)cfg->findSingle("PORT");
    if(!port || dmlConfig::isConf(port)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.EXTOUT.PORT in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int portno = atoi(port);
    char* name = (char*)cfg->findSingle("NAME");
    if(!name || dmlConfig::isConf(name)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.EXTOUT.NAME in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    ent->ext_outc.insert(SSF_MAKE_PAIR(name, portno));
  }
  delete e;

  // EXTIN
  e = entcfg->find("EXTIN");
  while(e->hasMoreElements()) {
    cfg = (dmlConfig*)e->nextElement();
    if(!dmlConfig::isConf(cfg)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.EXTIN in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }

    char* port = (char*)cfg->findSingle("PORT");
    if(!port || dmlConfig::isConf(port)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.EXTIN.PORT in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    int portno = atoi(port);
    char* name = (char*)cfg->findSingle("NAME");
    if(!name || dmlConfig::isConf(name)) {
      char msg[256];
      sprintf(msg, "wrong ENTITY.EXTIN.NAME in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    ent->ext_inc.insert(SSF_MAKE_PAIR(name, portno));
  }
  delete e;

  // TIMELINE
  str = (char*)entcfg->findSingle("TIMELINE");
  if(!str || dmlConfig::isConf(str)) {
    char msg[256];
    sprintf(msg, "wrong ENTITY.TIMELINE in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  int tl = atoi(str);
  if(0>tl || tl>=(int)timeline_list.size()) {
    char msg[256];
    sprintf(msg, "ENTITY.TIMELINE out of range in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  ent->timeline = timeline_list[tl];
  ent->timeline->entities.push_back(ent);

  return 0;
}

int ssf_dml_model::parse_dml_map(dmlConfig* mapcfg)
{
  char *str, *p, *q, *r, *s; int n;
  ssf_dml_entity* ent;


  ssf_dml_map* m = new ssf_dml_map();

  // FROM
  str = (char*)mapcfg->findSingle("FROM");
  if(!str || dmlConfig::isConf(str)) {
    char msg[256];
    sprintf(msg, "wrong MAP.FROM in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  str = r = strdup(str); // make a copy for internal handling
  p = strchr(r, '(');
  s = p;
  if(!p || strchr(p+1, '(')) {
    char msg[256];
    sprintf(msg, "wrong MAP.FROM in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  *p++ = 0;
  q = strchr(p, ')');
  if(!q || strchr(q+1, ')') || *(q+1) || p==q) {
    char msg[256];
    sprintf(msg, "wrong MAP.FROM in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  n = 0;
  while(r < s) {
    if(!isdigit(*r)) {
      char msg[256];
      sprintf(msg, "wrong MAP.FROM in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    n = n*10+*r++-'0';
  }
  if(n >= (int)entity_list.size()) {
    char msg[256];
    sprintf(msg, "MAP.FROM out of range in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  ent = m->from = entity_list[n];
  n = 0;
  while(p < q) {
    if(!isdigit(*p)) {
      char msg[256];
      sprintf(msg, "wrong MAP.FROM in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    n = n*10+*p++-'0';
  }
  if(n >= (int)ent->ext_outc.size()) {
    char msg[256];
    sprintf(msg, "MAP.FROM port out of range in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  m->from_port = n;
  free(str);

  // TO
  str = (char*)mapcfg->findSingle("TO");
  if(!str || dmlConfig::isConf(str)) {
    char msg[256];
    sprintf(msg, "wrong MAP.TO in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  str = r = strdup(str); // make a copy for internal handling
  p = strchr(r, '(');
  s = p;
  if(!p || strchr(p+1, '(')) {
    char msg[256];
    sprintf(msg, "wrong MAP.TO in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  *p++ = 0;
  q = strchr(p, ')');
  if(!q || strchr(q+1, ')') || *(q+1) || p==q) {
    char msg[256];
    sprintf(msg, "wrong MAP.TO in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  n = 0;
  while(r < s) {
    if(!isdigit(*r)) {
      char msg[256];
      sprintf(msg, "wrong MAP.TO in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    n = n*10+*r++-'0';
  }
  if(n >= (int)entity_list.size()) {
    char msg[256];
    sprintf(msg, "MAP.TO out of range in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  ent = m->to = entity_list[n];
  n = 0;
  while(p < q) {
    if(!isdigit(*p)) {
      char msg[256];
      sprintf(msg, "wrong MAP.TO in sub-model DML");
      ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
      return 1;
    }
    n = n*10+*p++-'0';
  }
  if(n >= (int)ent->ext_inc.size()) {
    char msg[256];
    sprintf(msg, "MAP.TO port out of range in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
  m->to_port = n;
  free(str);

  // DELAY
  str = (char*)mapcfg->findSingle("DELAY");
  if(!str || dmlConfig::isConf(str)) {
    char msg[256];
    sprintf(msg, "wrong MAP.DELAY in sub-model DML");
    ssf_throw_exception(ssf_exception::kernel_ssfmodel, msg);
    return 1;
  }
#if defined(PRIME_SSF_LTIME_LONG)
  m->delay = (ltime_t)atol(str);
#elif defined(PRIME_SSF_LTIME_LONGLONG)
#if HAVE_ATOLL
  m->delay = (ltime_t)atoll(str);
#else
  m->delay = (ltime_t)atol(str);
#endif
#elif defined(PRIME_SSF_LTIME_FLOAT) || defined(PRIME_SSF_LTIME_DOUBLE)
  m->delay = (ltime_t)atof(str);
#else
#error "unknown ltime_t type"
#endif

  map_list.push_back(m);
  return 0;
}

#if PRIME_DML_DEBUG
void ssf_dml_model::dump()
{
  if(valid) {
    printf("STARTTIME = %g\n", (double)startime);
    printf("ENDTIME = %g\n", (double)endtime);
    for(SSF_DML_STRSTR_MAP::iterator env_iter = envars.begin();
	env_iter != envars.end(); env_iter++) {
      printf("ENV: %s=%s\n", (*env_iter).first.c_str(), 
	     (*env_iter).second.c_str());
    }
    printf("\n");

    for(SSF_DML_TML_VECTOR::iterator tm_iter = timeline_list.begin();
	tm_iter != timeline_list.end(); tm_iter++)
      (*tm_iter)->dump();

    for(SSF_DML_ENT_VECTOR::iterator ent_iter = entity_list.begin();
	ent_iter != entity_list.end(); ent_iter++)
      (*ent_iter)->dump();

    for(SSF_DML_MAP_VECTOR::iterator map_iter = map_list.begin();
	map_iter != map_list.end(); map_iter++)
      (*map_iter)->dump();
  }
}
#endif
 
ssf_dml_timeline::ssf_dml_timeline(int seqno, int loc) :
  serialno(seqno), location(loc), processor(-1) {}

ssf_dml_timeline::ssf_dml_timeline(int seqno, int loc, int prc) :
  serialno(seqno), location(loc), processor(prc) {}

#if PRIME_DML_DEBUG
void ssf_dml_timeline::dump()
{
  printf("%d: TIMELINE @ %d [", serialno, location);
  for(SSF_DML_ENT_VECTOR::iterator iter = entities.begin();
      iter != entities.end(); iter++)
    printf(" %d", (*iter)->serialno);
  printf(" ]\n");
}
#endif

ssf_dml_entity::ssf_dml_entity(int seqno) : 
  serialno(seqno), configs(0), timeline(0) {}

ssf_dml_entity::~ssf_dml_entity()
{
  for(SSF_DML_PARAM_VECTOR::iterator iter = params.begin();
      iter != params.end(); iter++)
    delete (*iter);
  params.clear();
  ext_outc.clear();
  ext_inc.clear();
}

#if PRIME_DML_DEBUG
void ssf_dml_entity::dump()
{
  printf("%d: \"%s\": PARAMS[", serialno, instanceof.c_str());
  for(SSF_DML_PARAM_VECTOR::iterator iter = params.begin();
      iter != params.end(); iter++) {
    if((*iter)->get_type() == ssf_entity_dml_param::TYPE_INTEGER) {
      long intval; (*iter)->get_value(intval);
      printf(" INT=%ld", intval);
    } else if((*iter)->get_type() == ssf_entity_dml_param::TYPE_FLOAT) {
      double floatval; (*iter)->get_value(floatval);
      printf(" FLOAT=%g", floatval);
    } else if((*iter)->get_type() == ssf_entity_dml_param::TYPE_STRING) {
      const char* strval; (*iter)->get_value(strval);
      printf(" STRING=\"%s\"", strval);
    }
  }
  printf(" ] EXTOUT [");
  for(SSF_DML_STRINT_MAP::iterator oc_iter = ext_outc.begin();
      oc_iter != ext_outc.end(); oc_iter++)
    printf(" %d=\"%s\"", (*oc_iter).second, (*oc_iter).first.c_str());
  printf(" ] EXTIN [");
  for(SSF_DML_STRINT_MAP::iterator ic_iter = ext_inc.begin();
      ic_iter != ext_inc.end(); ic_iter++)
    printf(" %d=\"%s\"", (*ic_iter).second, (*ic_iter).first.c_str());
  printf(" ]\n");  
}
#endif

#if PRIME_DML_DEBUG
void ssf_dml_map::dump()
{
  printf("MAP FROM %d(%d) TO %d(%d) DELAY %g\n",
	 from->serialno, from_port, to->serialno, to_port, 
	 (double)delay);
}
#endif

}; // namespace ssf
}; // namespace prime

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

/*
 * $Id$
 */

