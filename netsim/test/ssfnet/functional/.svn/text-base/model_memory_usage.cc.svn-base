#include <stdio.h>
#include <string>
#include <iostream>
#include <fstream>
#include "os/model_builder.h"
#include "os/routing.h"
#include "os/partition.h"
#include "net/net.h"
#include "net/interface.h"

using namespace prime::ssfnet;
class mysize {
public:
	mysize(): total_static(0),props_static(0),states_static(0),count(0), share(0),total(0),props(0),states(0),total_no_share(0),props_no_share(0) { }
	long total_static;
	long props_static;
	long states_static;
	long count;
	long share;
	long total;
	long props;
	long states;
	long total_no_share;
	long props_no_share;
};
typedef SSFNET_MAP(SSFNET_STRING,mysize*) SizeMap;
typedef SSFNET_MAP(void*,long) PropMap;


void get_size(SizeMap& staticSize, PropMap& propMap, BaseEntity* e) {
	SizeMap::iterator s =staticSize.find(e->getTypeName());
	PropMap::iterator p = propMap.find((void*)e->getPropertyMap());
	mysize* t=NULL;
	if(s==staticSize.end()) {
		t=new mysize();
		t->total_static=e->getMemUsage_class();
		t->props_static=e->getPropertyMap()->getMemUsage_class();
		t->states_static=e->getStateMap()->getMemUsage_class();
		staticSize.insert(SSFNET_MAKE_PAIR(e->getTypeName(),t));
		s =staticSize.find(e->getTypeName());
	}
	else {
		t=(*s).second;
	}
	t->count++;
	t->total_no_share+=e->getMemUsage_object(true);
	t->props_no_share+=e->getMemUsage_object_properties();
	t->states+=e->getMemUsage_object_states();

	if(p==propMap.end()) {
		propMap.insert(SSFNET_MAKE_PAIR((void*)e->getPropertyMap(),1));
		p = propMap.find((void*)e->getPropertyMap());
	}
	else {
		(*p).second++;
	}

	if((*p).second==1) {
		t->total+=e->getMemUsage_object(true);
		t->props+=e->getMemUsage_object_properties();
	}
	else {
		t->total+=e->getMemUsage_object(false);
		t->share++;
	}
	ChildIterator<BaseEntity*> kids=e->getAllChildren();
	while(kids.hasMoreElements()) {
		get_size(staticSize,propMap,kids.nextElement());
	}
}
int main(int argc, char** argv) {
	if (argc != 3) {
		fprintf(stderr,"usage %s <tlv_file> <inlcude routing>\n", argv[0]);
		exit(0);
	}

	char* tlv_file;
	tlv_file=argv[1];
	int include_routing = atoi(argv[2]);

	ModelBuilder* mb=new ModelBuilder(tlv_file, false, false, new RuntimeVariables());
	Partition* partition=mb->buildModel();
	partition->init();
	SSFNET_STRING tab("");
	Net* t=partition->getTopnet();
	SizeMap staticSize;
	PropMap propMap;
	mysize total;
	get_size(staticSize,propMap,t);

	for(SizeMap::iterator i=staticSize.begin();i!=staticSize.end();i++) {
		if((*i).first.compare("RouteTable")==0 && !include_routing) continue;
		total.total_static+=(*i).second->total_static;
		total.props_static+=(*i).second->props_static;
		total.states_static+=(*i).second->states_static;
		total.total+=(*i).second->total;
		total.props+=(*i).second->props;
		total.states+=(*i).second->states;
		total.total_no_share+=(*i).second->total_no_share;
		total.props_no_share+=(*i).second->props_no_share;
		total.share+=(*i).second->share;
		total.count+=(*i).second->count;
	}

	cout <<"#Histogram:"<<endl;
	cout <<"#class, STATIC, property size, state size, other size, total size, OBJECT, # objs, #shared"<<endl;
	for(SizeMap::iterator i=staticSize.begin();i!=staticSize.end();i++) {
		if((*i).first.compare("RouteTable")==0 && !include_routing) continue;
		cout <<(*i).first;
		cout <<", STATIC";
		cout <<", "<<(*i).second->props_static;
		cout <<", "<<(*i).second->states_static;
		cout <<", "<<(*i).second->total_static-(*i).second->states_static-(*i).second->props_static;
		cout <<", "<<(*i).second->total_static;
		cout <<", OBJECT";
		cout <<", "<<(*i).second->count;
		cout <<", "<<(*i).second->share;
		cout <<"\n";
	}
	total.total+=partition->getMemUsage();
	total.total_no_share+=partition->getMemUsage();
	cout <<"Class Histogram: "<<endl;
	cout <<"\tTotal: "<<total.total_static<<" bytes"<<endl;
	cout <<"\tProperties: "<<total.props_static<<" bytes, Percent:"<<100*((double)total.props_static/(double)total.total_static)<<endl;
	cout <<"\tStates: "<<total.states_static<<" bytes, Percent:"<<100*((double)total.states_static/(double)total.total_static)<<endl;
	cout <<"\tOther: "<<(total.total_static-total.props_static-total.states_static)
			<<" bytes, Percent:"<<100*((total.total_static-(double)total.states_static-(double)total.props_static)/(double)total.total_static)<<endl;

	cout <<"Object Histogram: "<<endl;
	cout <<"\tTotal objects: "<<total.count<<endl;
	cout <<"\tTotal share: "<<total.share<<", duplication factor: "<<(double)total.share/(double)total.count<<endl;
	cout <<"\tContext Map Size: "<<partition->getMemUsage()<<" bytes"<<endl;
	cout <<"\tIf no Sharing: "<<total.total_no_share<<" total bytes"<<endl;
	cout <<"\t\tProperties: "<<total.props_no_share<<" bytes, Percent:"<<100*((double)total.props_no_share/(double)total.total_no_share)<<endl;
	cout <<"\t\tStates: "<<total.states<<" bytes, Percent:"<<100*((double)total.states/(double)total.total_no_share)<<endl;
	cout <<"\t\tOther: "<<(total.total_no_share-total.props_no_share-total.states)<<" bytes, Percent:"<<(double)(total.total_no_share-total.props_no_share-total.states)/(double)total.total_no_share<<endl;
	cout <<"\tActual Size: "<<total.total<<" total bytes"<<endl;
	cout <<"\t\tProperties: "<<total.props<<" bytes, Percent:"<<100*((double)total.props/(double)total.total)<<endl;
	cout <<"\t\tStates: "<<total.states<<" bytes, Percent:"<<100*((double)total.states/(double)total.total)<<endl;
	cout <<"\t\tOther: "<<(total.total-total.props-total.states)<<" bytes, Percent:"<<100*(double)(total.total-total.props-total.states)/(double)total.total<<endl;
	cout <<"\tReal Savings: "<<total.total_no_share-total.total<<" bytes, Percent: "<<100*((double)(total.total_no_share-total.total)/(double)total.total_no_share)<<endl;


#if 0
	//the old way
	for(SizeMap::iterator i=staticSize.begin();i!=staticSize.end();i++) {
		if((*i).first.compare("RouteTable")==0 && !include_routing) continue;
		cout <<"\t"<<(*i).first<<": "<<endl;
		cout <<"\t\tStatic:"<<endl;
		cout <<"\t\t\tProperies: "<<(*i).second->props_static<<endl;
		cout <<"\t\t\tStates: "<<(*i).second->states_static<<endl;
		cout <<"\t\t\tOther: "<<(*i).second->total_static-(*i).second->states_static-(*i).second->props_static<<endl;
		cout <<"\t\t\tTotal: "<<(*i).second->total_static<<endl;
		cout <<"\t\tObjects:"<<endl;
		cout <<"\t\t\tCount: "<<(*i).second->count<<endl;
		cout <<"\t\t\tshare: "<<(*i).second->share<<endl;
		cout <<"\t\t\tTotal: "<<(*i).second->total <<"("<< 100*((double)(*i).second->total/(double)total.total)<<"% of total)"<<endl;
		cout <<"\t\t\tTotal(w/o sharing): "<<(*i).second->total_no_share<<"("<< 100*((double)(*i).second->total/(double)total.total)<<"% of total)"<<endl;
		cout <<"\t\t\tStates: "<<(*i).second->states<<"("<< 100*((double)(*i).second->states/(double)total.states)<<"% of all state)"<<endl;
		cout <<"\t\t\tProperies: "<<(*i).second->props<<"("<< 100*((double)(*i).second->props/(double)total.props)<<"% of all properties)"<<endl;
		cout <<"\t\t\tProperies(w/o sharing): "<<(*i).second->props_no_share<<"("<< 100*((double)(*i).second->props_no_share/(double)total.props_no_share)<<"% of all props)"<<endl;
		double o1 = (*i).second->total-(*i).second->states-(*i).second->props;
		double o2 = total.total-total.states-total.props;
		cout <<"\t\t\tOther: "<<o1<<"("<<100*o1/o2<<"% of all overhead)"<<endl;
	}
	total.total+=partition->getMemUsage();
	total.total_no_share+=partition->getMemUsage();
	cout <<"Class Histogram: "<<endl;
	cout <<"\tTotal: "<<total.total_static<<" bytes"<<endl;
	cout <<"\tProperties: "<<total.props_static<<" bytes, Percent:"<<100*((double)total.props_static/(double)total.total_static)<<endl;
	cout <<"\tStates: "<<total.states_static<<" bytes, Percent:"<<100*((double)total.states_static/(double)total.total_static)<<endl;
	cout <<"\tOther: "<<(total.total_static-total.props_static-total.states_static)
			<<" bytes, Percent:"<<100*((total.total_static-(double)total.states_static-(double)total.props_static)/(double)total.total_static)<<endl;

	cout <<"Object Histogram: "<<endl;
	cout <<"\tTotal objects: "<<total.count<<endl;
	cout <<"\tTotal share: "<<total.share<<", duplication factor: "<<(double)total.share/(double)total.count<<endl;
	cout <<"\tContext Map Size: "<<partition->getMemUsage()<<" bytes"<<endl;
	cout <<"\tIf no Sharing: "<<total.total_no_share<<" total bytes"<<endl;
	cout <<"\t\tProperties: "<<total.props_no_share<<" bytes, Percent:"<<100*((double)total.props_no_share/(double)total.total_no_share)<<endl;
	cout <<"\t\tStates: "<<total.states<<" bytes, Percent:"<<100*((double)total.states/(double)total.total_no_share)<<endl;
	cout <<"\t\tOther: "<<(total.total_no_share-total.props_no_share-total.states)<<" bytes, Percent:"<<(double)(total.total_no_share-total.props_no_share-total.states)/(double)total.total_no_share<<endl;
	cout <<"\tActual Size: "<<total.total<<" total bytes"<<endl;
	cout <<"\t\tProperties: "<<total.props<<" bytes, Percent:"<<100*((double)total.props/(double)total.total)<<endl;
	cout <<"\t\tStates: "<<total.states<<" bytes, Percent:"<<100*((double)total.states/(double)total.total)<<endl;
	cout <<"\t\tOther: "<<(total.total-total.props-total.states)<<" bytes, Percent:"<<100*(double)(total.total-total.props-total.states)/(double)total.total<<endl;
	cout <<"\tReal Savings: "<<total.total_no_share-total.total<<" bytes, Percent: "<<100*((double)(total.total_no_share-total.total)/(double)total.total_no_share)<<endl;
#endif

	return -1;
}

