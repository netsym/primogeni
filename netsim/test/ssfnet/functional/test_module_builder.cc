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


void print_r(BaseEntity* e, BaseEntity* anchor, SSFNET_STRING& tab) {
	if(anchor->getConfigType()->isSubtype(e->getConfigType()))
		anchor=e;
	SSFNET_STRING tab1=tab;
	tab1.append("  ");
	SSFNET_STRING tab2=tab1;
	tab2.append("  ");
	cout <<tab<<"<"<< e->getTypeName() <<">\n";
	cout <<tab1<<"<uniqueName>"<<e->getUName()<<"</uniqueName>"<<"\n";
	if(e->getParent()!=NULL) {
		cout <<tab1<<"<parent>"<<e->getParent()->getUName()<<"</parent>"<<"\n";
	}
	cout <<tab1<<"<size>"<<e->getSize()<<"</size>"<<"\n";
	cout <<tab1<<"<offset>"<<e->getOffset()<<"</offset>"<<"\n";
	cout <<tab1<<"<min_uid>"<<e->getMinUID()<<"</min_uid>"<<"\n";
	cout <<tab1<<"<uid>"<<e->getUID()<<"</uid>"<<"\n";
	if(e->getParent() && e->getParent()->containsUID(e->getUID()))
	cout <<tab1<<"<rank anchor=\""<<e->getParent()->getUName()<<"\">"<<e->getRank(e->getParent())<<"</rank>"<<"\n";
			//<<", rank="<<e->getRank(anchor)
			//<<", min_rank="<<e->getMinRank(anchor)
	if(Net::getClassConfigType()->isSubtype(e->getConfigType())) {
		cout <<tab1<<"<ip_prefix>"<<dynamic_cast<Net*>(e)->getIPPrefix()<<"</ip_prefix>"<<"\n";
	}
	if(BaseInterface::getClassConfigType()->isSubtype(e->getConfigType())) {
		cout <<tab1<<"<ip_address>"<<dynamic_cast<BaseInterface*>(e)->getIP()<<"</ip_address>"<<"\n";
		cout <<tab1<<"<mac_address>"<<dynamic_cast<BaseInterface*>(e)->getMAC()<<"</mac_address>"<<"\n";
	}


	if(RouteTable::getClassConfigType()->isSubtype(e->getConfigType())) {
		cout <<tab1<<"<routes>\n";
		RTREE& t = dynamic_cast<RouteTable*>(e)->getRouteMap();
		cout <<tab1<<"<table_addr>"<<(void*)&t<<"</table_addr>\n";
		RTREE::RTree::Iterator i;
		t.GetFirst(i);
		while(!t.IsNull(i)) {
			RouteEntry* r = *i;
			cout << tab2 << *r<<endl;
			t.GetNext(i);
		}
		/*XXX
		cout << " EdgeIfaces [";
		EdgeInterface::List& el=*(dynamic_cast<RouteTable*>(e)->prop->edge_ifaces));
		for(EdgeInterface::List::iterator i=el.begin();i!=el.end();i++) {
			if(i!=el.begin())cout<<",";
			cout<<*i;
		}
		cout<< "]";
		*/
		cout <<tab1<<"</routes>\n";
	}
	cout <<tab1<<"<children>\n";
	ChildIterator<BaseEntity*> kids = e->getAllChildren();
	while(kids.hasMoreElements()) {
		print_r(kids.nextElement(),anchor, tab2);
	}
	cout <<tab1<<"</children>\n";
	cout <<tab<<"</"<< e->getTypeName()<<">"<<endl;
}

int main(int argc, char** argv) {
	if (argc != 2) {
		fprintf(stderr,"usage %s <tlv_file> \n", argv[0]);
		exit(0);
	}

	char* tlv_file;
	tlv_file=argv[1];

	ModelBuilder* mb=new ModelBuilder(tlv_file, false, false, new RuntimeVariables());
	Partition* partition=mb->buildModel();
	partition->init();
	SSFNET_STRING tab("");
	Net* t=partition->getTopnet();
	cout <<"***********************************"<<endl;
	print_r(dynamic_cast<BaseEntity*>(t), dynamic_cast<BaseEntity*>(t), tab);
	cout <<"***********************************"<<endl;

	return -1;
}

