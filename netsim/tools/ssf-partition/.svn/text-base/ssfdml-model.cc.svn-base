/*
 * ssfdml-model :- parse iSSF DML model.
 */

#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

#include "ssfdml-model.h"

// different than strdup, we use new[] instead of malloc.
static char* sstrdup(char* str)
{
  assert(str);
  int len = strlen(str);
  char* newstr = new char[len+1];
  strcpy(newstr, str);
  return newstr;
}

ssfdml_param::ssfdml_param(int x) : type(INT), value_int(x) {}
ssfdml_param::ssfdml_param(double x) : type(FLOAT), value_float(x) {}
ssfdml_param::ssfdml_param(char* x) : type(STRING), value_string(sstrdup(x)) {}
ssfdml_param::~ssfdml_param() { if(type==STRING && value_string) delete[] value_string; }

ssfdml_model::ssfdml_model(char* filename)
{
  valid = 0;

  id_lookup = 0;
  entity_lookup = 0;
  align_list = 0;

  dmlConfig* cfg = new dmlConfig(filename);
  int ret = parse_dml_model(cfg);
  delete cfg;
  if(ret) return;
  if(resolve_dml_model()) return;
  clusterize();

  valid = 1;
}

ssfdml_model::~ssfdml_model()
{
  for(std::vector<ssfdml_entity*>::iterator ent_iter = entity_list.begin();
      ent_iter != entity_list.end(); ent_iter++) delete (*ent_iter);

  for(std::vector<ssfdml_map*>::iterator map_iter = map_list.begin();
      map_iter != map_list.end(); map_iter++) delete (*map_iter);

  for(std::vector<ssfdml_timeline*>::iterator tln_iter = timeline_list.begin();
      tln_iter != timeline_list.end(); tln_iter++) delete (*tln_iter);
}

int ssfdml_model::parse_dml_model(dmlConfig* cfg)
{
  id_lookup = new std::set<std::string>();
  entity_lookup = new std::map<std::string,ssfdml_entity*>();
  align_list = new std::vector<ssfdml_align*>();

  dmlConfig* modcfg = (dmlConfig*)cfg->findSingle("MODEL");
  if(!modcfg) {
    fprintf(stderr, "ERROR: missing MODEL attribute\n");
    return 1;
  }
  if(!DML_ISCONF(modcfg)) {
    fprintf(stderr, "ERROR: MODEL attribute must be list\n");
    return 1;
  }
  int ret = parse_dml_cluster(const_cast<char*>(""), modcfg);

  delete id_lookup; // id_lookup is of no use any more after this point

  return ret;
}

int ssfdml_model::parse_dml_cluster(char* prefix, dmlConfig* cfg)
{
  // trace down sub-clusters within this cluster
  Enumeration* cltenum = cfg->find("CLUSTER");
  while(cltenum->hasMoreElements()) {
    dmlConfig* ccfg = (dmlConfig*)cltenum->nextElement();
    if(!DML_ISCONF(ccfg)) {
      fprintf(stderr, "ERROR: CLUSTER attribute must be list\n");
      return 1;
    }
    char* idstr = (char*)ccfg->findSingle("ID");
    if(!idstr) {
      fprintf(stderr, "ERROR: missing CLUSTER.ID attribute\n");
      return 1;
    }
    if(DML_ISCONF(idstr)) {
      fprintf(stderr, "ERROR: CLUSTER.ID attribute must be singleton\n");
      return 1;
    }
    char* p; // new prefix
    if(idstr[0] == '.') { // absolute id
      p = sstrdup(idstr);
    } else {
      p = new char[strlen(prefix)+strlen(idstr)+2];
      sprintf(p, "%s.%s", prefix, idstr);
    }
    // register the new absolute id
    if(!id_lookup->insert(p).second) {
      fprintf(stderr, "ERROR: duplicated ID: %s\n", p);
      return 1;
    }
    int ret = parse_dml_cluster(p, ccfg);
    delete[] p;
    if(ret) return 1;
  }
  delete cltenum;

  // trace down entities within this cluster
  Enumeration* entenum = cfg->find("ENTITY");
  while(entenum->hasMoreElements()) {
    dmlConfig* ccfg = (dmlConfig*)entenum->nextElement();
    if(!DML_ISCONF(ccfg)) {
      fprintf(stderr, "ERROR: ENTITY attribute must be list\n");
      return 1;
    }
    char* idstr = (char*)ccfg->findSingle("ID");
    if(!idstr) {
      fprintf(stderr, "ERROR: missing ENTITY.ID attribute\n");
      return 1;
    }
    if(DML_ISCONF(idstr)) {
      fprintf(stderr, "ERROR: ENTITY.ID attribute must be singleton\n");
      return 1;
    }
    char* p; // new prefix
    if(idstr[0] == '.') { // absolute id
      p = sstrdup(idstr);
    } else {
      p = new char[strlen(prefix)+strlen(idstr)+2];
      sprintf(p, "%s.%s", prefix, idstr);
    }
    // register the new absolute id
    if(!id_lookup->insert(p).second) {
      fprintf(stderr, "ERROR: duplicated ID: %s\n", p);
      return 1;
    }
    int ret = parse_dml_entity(p, ccfg);
    delete[] p;
    if(ret) return 1;
  }
  delete entenum;

  // take care of alignments defined within the cluster
  Enumeration* alnenum = cfg->find("ALIGN");
  while(alnenum->hasMoreElements()) {
    dmlConfig* ccfg = (dmlConfig*)alnenum->nextElement();
    if(!DML_ISCONF(ccfg)) {
      fprintf(stderr, "ERROR: ALIGN attribute must be list\n");
      return 1;
    }
    if(parse_dml_align(prefix, ccfg)) return 1; // same prefix
  }
  delete alnenum;
  
  // take care of mappings defined within the cluster
  Enumeration* mapenum = cfg->find("MAP");
  while(mapenum->hasMoreElements()) {
    dmlConfig* ccfg = (dmlConfig*)mapenum->nextElement();
    if(!DML_ISCONF(ccfg)) {
      fprintf(stderr, "ERROR: MAP attribute must be list\n");
      return 1;
    }
    if(parse_dml_map(prefix, ccfg)) return 1; // same prefix
  }
  delete mapenum;

  return 0;
}

int ssfdml_model::parse_dml_entity(char* prefix, dmlConfig* cfg)
{
  ssfdml_entity* ent = new ssfdml_entity();
  ent->id = sstrdup(prefix);
  
  // instanceof
  char* inst = (char*)cfg->findSingle("INSTANCEOF");
  if(!inst) {
    fprintf(stderr, "ERROR: missing ENTITY.INSTANCEOF attribute\n");
    return 1;
  }
  if(DML_ISCONF(inst)) {
    fprintf(stderr, "ERROR: ENTITY.INSTANCEOF attribute must be singleton\n");
    return 1;
  }
  ent->instanceof = sstrdup(inst);

  // params
  dmlConfig* pcfg = (dmlConfig*)cfg->findSingle("PARAMS");
  if(pcfg) {
    if(!DML_ISCONF(pcfg)) {
      fprintf(stderr, "ERROR: ENTITY.PARAMS attribute must be list\n");
      return 1;
    }
    Enumeration* penum = pcfg->find("*"); // order is important!
    while(penum->hasMoreElements()) {
      char* valstr = (char*)penum->nextElement();
      if(DML_ISCONF(valstr)) {
	fprintf(stderr, "ERROR: attributes in ENTITY.PARAMS must be singleton\n");
	return 1;
      }
      char* keystr = valstr+strlen(valstr)+1;

      if(!strcmp(keystr, "INT")) {
	if(!strcmp(valstr, "%N") || !strcmp(valstr, "%n")) // total entities & entity index
	  ent->params.push_back(new ssfdml_param(valstr)); // evaluation delayed
	else ent->params.push_back(new ssfdml_param(atoi(valstr)));
      } else if(!strcmp(keystr, "FLOAT")) {
	ent->params.push_back(new ssfdml_param(atof(valstr)));
      } else if(!strcmp(keystr, "STRING")) {
	if(!strcmp(valstr, "%I")) // global entity id = entity global id
	  ent->params.push_back(new ssfdml_param(prefix));
	else if(!strcmp(valstr, "%i")) { // local entity id
	  char* param = strrchr(prefix, '.');
	  if(!param) ent->params.push_back(new ssfdml_param(prefix));
	  else ent->params.push_back(new ssfdml_param(param+1));
	} else ent->params.push_back(new ssfdml_param(valstr));
      }
    }
    delete penum;
  }

  // configs
  dmlConfig* cfcfg = (dmlConfig*)cfg->findSingle("CONFIGURE");
  if(cfcfg) {
    if(!DML_ISCONF(cfcfg)) {
      fprintf(stderr, "ERROR: ENTITY.CONFIGURE attribute must be list\n");
      return 1;
    }
    ent->configs = (dmlConfig*)cfcfg->clone();
  }

  entity_list.push_back(ent);
  entity_lookup->insert(std::make_pair(ent->id, ent));

  return 0;
}

int ssfdml_model::parse_dml_align(char* prefix, dmlConfig* cfg)
{
  ssfdml_align* al = new ssfdml_align();

  // from
  char* fromstr = (char*)cfg->findSingle("FROM");
  if(!fromstr) {
    fprintf(stderr, "ERROR: missing ALIGN.FROM attribute\n");
    return 1;
  }
  if(DML_ISCONF(fromstr)) {
    fprintf(stderr, "ERROR: ALIGN.FROM attribute must be singleton\n");
    return 1;
  }
  if(fromstr[0] == '.') al->unresolved_from = sstrdup(fromstr);
  else {
    al->unresolved_from = new char[strlen(prefix)+strlen(fromstr)+2];
    sprintf(al->unresolved_from, "%s.%s", prefix, fromstr);
  }

  // to
  char* tostr = (char*)cfg->findSingle("TO");
  if(!tostr) {
    fprintf(stderr, "ERROR: missing ALIGN.TO attribute\n");
    return 1;
  }
  if(DML_ISCONF(tostr)) {
    fprintf(stderr, "ERROR: ALIGN.TO attribute must be singleton\n");
    return 1;
  }
  if(tostr[0] == '.') al->unresolved_to = sstrdup(tostr);
  else {
    al->unresolved_to = new char[strlen(prefix)+strlen(tostr)+2];
    sprintf(al->unresolved_to, "%s.%s", prefix, tostr);
  }

  align_list->push_back(al);
  return 0;
}

int ssfdml_model::parse_dml_map(char* prefix, dmlConfig* cfg)
{
  ssfdml_map* map = new ssfdml_map();
  char *p, *q;

  // from
  char* fromstr = (char*)cfg->findSingle("FROM");
  if(!fromstr) {
    fprintf(stderr, "ERROR: missing MAP.FROM attribute\n");
    return 1;
  }
  if(DML_ISCONF(fromstr)) {
    fprintf(stderr, "ERROR: MAP.FROM attribute must be singleton\n");
    return 1;
  }
  if(fromstr[0] == '.') map->unresolved_from = sstrdup(fromstr);
  else {
    map->unresolved_from = new char[strlen(prefix)+strlen(fromstr)+2];
    sprintf(map->unresolved_from, "%s.%s", prefix, fromstr);
  }

  p = strchr(map->unresolved_from, '(');
  if(!p || strchr(p+1, '(')) {
    fprintf(stderr, "ERROR: malformed MAP.FROM attribute\n");
    return 1;
  }
  *p++ = 0;
  q = strchr(p, ')');
  if(!q || strchr(q+1, ')') || *(q+1) || p==q) {
    fprintf(stderr, "ERROR: malformed MAP.FROM attribute\n");
    return 1;
  }
  *q = 0; map->unresolved_from_port = sstrdup(p);
  
  // to
  char* tostr = (char*)cfg->findSingle("TO");
  if(!tostr) {
    fprintf(stderr, "ERROR: missing MAP.TO attribute\n");
    return 1;
  }
  if(DML_ISCONF(tostr)) {
    fprintf(stderr, "ERROR: MAP.TO attribute must be singleton\n");
    return 1;
  }
  if(tostr[0] == '.') map->unresolved_to = sstrdup(tostr);
  else {
    map->unresolved_to = new char[strlen(prefix)+strlen(tostr)+2];
    sprintf(map->unresolved_to, "%s.%s", prefix, tostr);
  }

  p = strchr(map->unresolved_to, '(');
  if(!p || strchr(p+1, '(')) {
    fprintf(stderr, "ERROR: malformed MAP.TO attribute\n");
    return 1;
  }
  *p++ = 0;
  q = strchr(p, ')');
  if(!q || strchr(q+1, ')') || *(q+1) || p==q) {
    fprintf(stderr, "ERROR: malformed MAP.TO attribute\n");
    return 1;
  }
  *q = 0; map->unresolved_to_port = sstrdup(p);

  // delay
  char* dlystr = (char*)cfg->findSingle("DELAY");
  if(!dlystr) {
    fprintf(stderr, "ERROR: missing MAP.DELAY attribute\n");
    return 1;
  }
  if(DML_ISCONF(dlystr)) {
    fprintf(stderr, "ERROR: MAP.DELAY attribute must be singleton\n");
    return 1;
  }
  map->delay = atof(dlystr);

  map_list.push_back(map);
  return 0;
}

int ssfdml_model::resolve_dml_model()
{
  register int i, j;

  // resolve %N and %n
  int total = entity_list.size();
  i = 0;
  for(std::vector<ssfdml_entity*>::iterator entiter = entity_list.begin();
      entiter != entity_list.end(); entiter++, i++) {
    (*entiter)->serialno = i;
    for(std::vector<ssfdml_param*>::iterator piter = (*entiter)->params.begin();
	piter != (*entiter)->params.end(); piter++) {
      if((*piter)->type == ssfdml_param::STRING &&
	 !strcmp((*piter)->value_string, "%N")) { // total entities
	delete[] (*piter)->value_string;
	(*piter)->type = ssfdml_param::INT;
	(*piter)->value_int = total;
      } else if((*piter)->type == ssfdml_param::STRING &&
		!strcmp((*piter)->value_string, "%n")) { // entity index
	delete[] (*piter)->value_string;
	(*piter)->type = ssfdml_param::INT;
	(*piter)->value_int = i;
      }
    }
  }

  // resolve alignments
  for(std::vector<ssfdml_align*>::iterator aligniter = align_list->begin();
      aligniter != align_list->end(); aligniter++) {
    std::map<std::string,ssfdml_entity*>::iterator iter_from =
      entity_lookup->find((*aligniter)->unresolved_from);
    if(iter_from != entity_lookup->end()) {
      delete[] (*aligniter)->unresolved_from;
      (*aligniter)->from = (*iter_from).second;
    } else {
      fprintf(stderr, "Error: ALIGN.FROM unresolved: %s\n",
	      (*aligniter)->unresolved_from);
      return 1;
    }
    std::map<std::string,ssfdml_entity*>::iterator iter_to = 
      entity_lookup->find((*aligniter)->unresolved_to);
    if(iter_to != entity_lookup->end()) {
      delete[] (*aligniter)->unresolved_to;
      (*aligniter)->to = (*iter_to).second;
    } else {
      fprintf(stderr, "Error: ALIGN.TO unresolved: %s\n",
	      (*aligniter)->unresolved_to);
      return 1;
    }
  }

  // resolve mappings
  for(std::vector<ssfdml_map*>::iterator mapiter = map_list.begin();
      mapiter != map_list.end(); mapiter++) {
    std::map<std::string,ssfdml_entity*>::iterator iter_from = 
      entity_lookup->find((*mapiter)->unresolved_from);
    if(iter_from != entity_lookup->end()) {
      delete[] (*mapiter)->unresolved_from;
      (*mapiter)->from = (*iter_from).second;

      int f = 0; j = 0;
      for(std::vector<std::string>::iterator piter = (*mapiter)->from->ext_outc.begin();
	  piter != (*mapiter)->from->ext_outc.end(); piter++, j++) {
	if(!(*piter).compare((*mapiter)->unresolved_from_port)) {
	  delete[] (*mapiter)->unresolved_from_port;
	  (*mapiter)->from_port = j;
	  f = 1;
	  break;
	}
      }
      if(!f) {
	(*mapiter)->from->ext_outc.push_back((*mapiter)->unresolved_from_port);
	delete[] (*mapiter)->unresolved_from_port;
	(*mapiter)->from_port = (*mapiter)->from->ext_outc.size()-1;
      }
    } else {
      fprintf(stderr, "Error: MAP.FROM unresolved: %s\n",
	      (*mapiter)->unresolved_from);
      return 1;
    }

    std::map<std::string,ssfdml_entity*>::iterator iter_to = 
      entity_lookup->find((*mapiter)->unresolved_to);
    if(iter_to != entity_lookup->end()) {
      delete[] (*mapiter)->unresolved_to;
      (*mapiter)->to = (*iter_to).second;

      int f = 0; j = 0;
      for(std::vector<std::string>::iterator piter = (*mapiter)->to->ext_inc.begin();
	  piter != (*mapiter)->to->ext_inc.end(); piter++, j++) {
	if(!(*piter).compare((*mapiter)->unresolved_to_port)) {
	  delete[] (*mapiter)->unresolved_to_port;
	  (*mapiter)->to_port = j;
	  f = 1;
	  break;
	}
      }
      if(!f) {
	(*mapiter)->to->ext_inc.push_back((*mapiter)->unresolved_to_port);
	delete[] (*mapiter)->unresolved_to_port;
	(*mapiter)->to_port = (*mapiter)->to->ext_inc.size()-1;
      }
    } else {
      fprintf(stderr, "Error: MAP.TO unresolved: %s\n",
	      (*mapiter)->unresolved_to);
      return 1;
    }
  }

  delete entity_lookup;
  return 0;
}

void ssfdml_model::clusterize()
{
  for(std::vector<ssfdml_entity*>::iterator eiter = entity_list.begin();
      eiter != entity_list.end(); eiter++) {
    ssfdml_timeline* tl = (*eiter)->timeline = new ssfdml_timeline;
    tl->entities.insert(*eiter);
  }

  for(std::vector<ssfdml_align*>::iterator aiter = align_list->begin();
      aiter != align_list->end(); aiter++) {
    ssfdml_entity* entfrom = (*aiter)->from;
    ssfdml_timeline* tlfrom = entfrom->timeline;
    ssfdml_timeline* tlto = (*aiter)->to->timeline;
    if(tlfrom != tlto) {
      tlfrom->entities.erase(entfrom);
      if(tlfrom->entities.size() == 0) delete tlfrom;
      entfrom->timeline = tlto;
      tlto->entities.insert(entfrom);
    }
    delete (*aiter);
  }

  std::set<ssfdml_timeline*> timeline_set;
  for(std::vector<ssfdml_entity*>::iterator eiter = entity_list.begin();
      eiter != entity_list.end(); eiter++) {
    timeline_set.insert((*eiter)->timeline);
  }
  int i = 0;
  for(std::set<ssfdml_timeline*>::iterator titer = timeline_set.begin();
      titer != timeline_set.end(); titer++, i++) {
    (*titer)->serialno = i;
    timeline_list.push_back(*titer);
  }

  delete align_list;
}

void ssfdml_model::dump()
{
  for(std::vector<ssfdml_entity*>::iterator eiter = entity_list.begin();
      eiter != entity_list.end(); eiter++)
    (*eiter)->dump();
  printf("\n");

  for(std::vector<ssfdml_map*>::iterator miter = map_list.begin();
      miter != map_list.end(); miter++)
    (*miter)->dump();
  printf("\n");

  for(std::vector<ssfdml_timeline*>::iterator titer = timeline_list.begin();
      titer != timeline_list.end(); titer++)
    (*titer)->dump();
  printf("\n");
}
 
void ssfdml_timeline::dump()
{
  printf("%d: TIMELINE (size=%d) [", serialno, (int)entities.size());
  for(std::set<ssfdml_entity*>::iterator eiter = entities.begin();
      eiter != entities.end(); eiter++)
    printf(" %d", (*eiter)->serialno);
  printf(" ]\n");
}

void ssfdml_entity::dump()
{
  printf("%d: id=%s, instanceof=\"%s\": PARAMS=[", serialno, id, instanceof);
  for(std::vector<ssfdml_param*>::iterator piter = params.begin();
      piter != params.end(); piter++) {
    if((*piter)->type == ssfdml_param::INT)
      printf(" INT=>%d", (*piter)->value_int);
    else if((*piter)->type == ssfdml_param::FLOAT) 
      printf(" FLOAT=>%g", (*piter)->value_float);
    else /*if((*piter)->type == ssfdml_param::STRING)*/
      printf(" STRING=>\"%s\"", (*piter)->value_string);
  }
  printf(" ] EXTOUT=[");
  register int i = 0;
  for(std::vector<std::string>::iterator oiter = ext_outc.begin();
      oiter != ext_outc.end(); oiter++, i++)
    printf(" %d=>\"%s\"", i, (*oiter).c_str());
  printf(" ] EXTIN=[");
  i = 0;
  for(std::vector<std::string>::iterator iiter = ext_inc.begin();
      iiter != ext_inc.end(); iiter++, i++)
    printf(" %d=>\"%s\"", i, (*iiter).c_str());
  printf(" ]\n");  
}

void ssfdml_map::dump()
{
  printf("MAP FROM %d(%d) TO %d(%d) DELAY %g\n", 
	 from->serialno, from_port, to->serialno, to_port, delay);
}
