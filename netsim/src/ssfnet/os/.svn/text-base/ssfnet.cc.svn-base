/**
 * \file ssfnet.cc
 * \brief Source file for System-wide definitions and configurations.
 * \author Nathanael Van Vorst
 * 
 * 
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

#include <string.h>
#include "os/logger.h"
#include "os/ssfnet.h"
#include "os/virtual_time.h"
#include "os/model_builder.h"
#include "os/partition.h"
#include "os/emu/vpn_device.h"
#include "os/state_logger.h"

#if SSFNET_EMULATION
#include <netinet/in.h>
#include <arpa/inet.h>
#include <pcap.h>
#endif

using namespace prime::ssf;
using namespace prime::ssfnet;

LOGGING_COMPONENT(ssfnet);

#if SSFNET_EMULATION
#ifndef PRIME_SSF_MACH_X86_DARWIN
static bool match_addr (SSFNET_STRING* to_match, pcap_addr_t *addresses) {
	bool rv =false;
	pcap_addr_t *addr = addresses;
	while (addr && !rv) {
		struct sockaddr_in *ip = (struct sockaddr_in *)addr->addr;
		if (ip) {
			char* t = inet_ntoa(ip->sin_addr);
			LOG_DEBUG("\t\tIP="<<t<<endl);
			if(!strcmp(to_match->c_str(),t)) {
				rv=true;
			}
		}
		addr = addr->next;
	}
	return rv;
}
#endif

static SSFNET_STRING* lookupDeviceByIP(SSFNET_STRING* ip) {
#ifndef PRIME_SSF_MACH_X86_DARWIN
	int r;
	char errbuf[PCAP_ERRBUF_SIZE];
	pcap_if_t *devs;
	SSFNET_STRING* rv = 0;
	r = pcap_findalldevs (&devs, errbuf);
	if (r) {
		LOG_ERROR("Could get list of interfaces. Error="<<SSFNET_STRING(errbuf)<<endl);
	}
	LOG_DEBUG("Looking for dev with IP "<<ip<<endl);
	pcap_if_t *dev = devs;
	while (dev && !rv) {
		LOG_DEBUG("\tLooking at Dev "<< SSFNET_STRING(dev->name)<<endl);
		if(match_addr(ip, dev->addresses)) {
			rv=new SSFNET_STRING(dev->name);
			break;
		}
		dev = dev->next;
	}
	pcap_freealldevs (devs);
	return rv;
#else
    LOG_ERROR("Not supported on OSX"<<endl);
    return 0;
#endif
}
#endif

static void show_usage(char* prognam) {
	fprintf(
			stderr,
			"USAGE: %s [ssf-options] [ssfnet-options] <sim-time> <model source>\n",
			prognam);
	prime::ssf::print_command_line(stderr);
	fprintf(stderr, "SSFNet command-line options:\n");
	fprintf(stderr, "  -help                : show this message.\n");
	fprintf(stderr, "  -h                   : same as -help.\n");
	fprintf(stderr,	"  -quiet               : quiet mode (no ssfnet runtime messages).\n");
	fprintf(stderr, "  -q                   : same as -quiet.\n");
	fprintf(stderr,	"  -emuratio <ratio>    : set emulation speed (i.e., the slow-down factor).\n");
	fprintf(stderr, "  -e                   : same as -emuratio.\n");
	fprintf(stderr,	"  -vars <file>         : load runtime variables/symbols\n");
	fprintf(stderr,	"                         and default values from <file>.\n");
	fprintf(stderr, "  -V                   : same as -vars.\n");
	fprintf(stderr,	"  -CCD <millisecs>     : the delay between event channels of collocated\n");
	fprintf(stderr, "                         communities (which carry control events) in milliseconds.\n");
#if SSFNET_DEBUG
	fprintf(stderr,	"  -DEBUG <lvl>         : Set the debug level of all streams to <lvl> (DEBUG, \n");
	fprintf(stderr,	"                         INFO, WARN, or OFF). Default is WARN. This is different\n");
	fprintf(stderr,	"                         from -debug, which is for ssf.\n");
	fprintf(stderr, "  -D <lvl>             : same as -DEBUG.\n");
	fprintf(stderr, "  -DEBUG_LIST          : List what debug streams can be controlled.\n");
	fprintf(stderr, "  -DL                  : same as -DEBUG_LIST.\n");
	fprintf(stderr, "  -DS <stream> <lvl>   : Set the debug stream <stream> to <lvl>.\n");
	fprintf(stderr, "            To only show debug logs of 'Interface' and 'Host', \n");
	fprintf(stderr, "            the following options would be used:\n");
	fprintf(stderr, "                           -D OFF 100 -DS Interface DEBUG -DS Host DEBUG\n");
#endif

#ifdef PRIME_SSF_SYNC_MPI
	fprintf(stderr, "  -enable_state_file   : save exported state to state.stats\n");
#else
	fprintf(stderr, "  -enable_state_file   : save exported state to state_part_X.stats\n");
#endif
	fprintf(stderr, "  -enable_state_stream : send exported state to a state server.\n");
	fprintf(stderr, "  -stream_server <ip>:<port>\n");
	fprintf(stderr, "                       : connect to the state sever at <ip>:port;\n");
	fprintf(stderr, "                         the default is 127.0.0.1:9992 ;\n");
	fprintf(stderr, "  -VEI <milliseconds>  : the number of milliseconds between\n");
	fprintf(stderr, "                       : visualization data exports (defaults to 500).\n");
#if SSFNET_EMULATION
	fprintf(stderr, "  -gateway <ip>:<port> : add an openvpn server to connect to\n");
	fprintf(stderr,	"  -tp <IP> <UID>       : attach the interface with <IP>\n");
	fprintf(stderr,	"                         to the traffic portal with UID <UID>.\n");
#endif
	fprintf(stderr, "  <sim-time>           : the simulation time in seconds.\n");
	fprintf(stderr, "  <model source>       : specify ONE of the following:\n");
	fprintf(stderr,	"     <server>:<port>   : The name of the server that hosts the\n");
	fprintf(stderr,	"                         model DB, and port which its runs on\n");
	fprintf(stderr,	"     <packed-file>     : the serialized model for this partition\n");
#if PRIME_SSF_DISTSIM
	fprintf(stderr,	"     <name> <dir>      : were name is the <name> of the model and\n");
	fprintf(stderr,	"                         <dir> is the location of the packed files\n");
	fprintf(stderr,	"                         for each partition.\n");
#endif
}

#if SSFNET_DEBUG
typedef SSFNET_PAIR(LogStream*,LogLvl*) LogConfig;
typedef SSFNET_LIST(LogConfig) LogConfigList;
#endif

#if SSFNET_EMULATION
typedef SSFNET_MAP(UID_t, SSFNET_STRING*) SSFNET_UID2STR_MAP;
SSFNET_UID2STR_MAP* portal_map;
#endif

int main
(int argc, char** argv) {
#if SSFNET_DEBUG
	LogConfigList log_cgs;
#endif
#if SSFNET_EMULATION
	portal_map = new SSFNET_UID2STR_MAP();
#endif
	bool showuse = false, useStateFile=false, useStateStream=false;
	double emuratio = 1.0;
	bool isThrottled=false;
	bool silent = false;
	char machid[16];
	RuntimeVariables* runtime_vars=0;
	sprintf(machid, "%d", prime::ssf::ssf_machine_index());
	int collocated_delay=-1;

	LOG_DEBUG("parsing command-line arguments."<<endl)

	int offset = 1;
	for (int i = 1; i < argc; i++) {
		if (!strcmp(argv[i], "-q") || !strcmp(argv[i], "-quiet")) {
			silent = true;
		} else
			argv[offset++] = argv[i];
	}
	argv[offset] = 0;
	argc = offset;

	if (!silent) {
		printf("============================================================\n");
		printf("Parallel Real-time Immersive Simulation Environment (PRIME)\n");
		printf("SSF Network Simulator (SSFNet) Version %s\n", PRIME_VERSION);
		printf("Copyright (c) 2007-2011 Florida International University.\n");
		printf("============================================================\n");
		printf("\n\n");
	}

	//while (DebugWait) ;


	offset = 1;
	for (int i = 1; i < argc; i++) {
		if (!strcmp(argv[i], "-h") || !strcmp(argv[i], "-help")) {
			showuse = true;
			break;
		}
		else if (!strcmp(argv[i], "-e") || !strcmp(argv[i], "-emuratio")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -e or -emuratio: argument missing!\n\n");
				showuse = true;
				break;
			}
			emuratio = atof(argv[i]);
			if (emuratio <= 0) {
				fprintf(stderr,
						"ERROR: -e or -emuratio: positive emulation throttle ratio is required!\n\n");
				showuse = true;
				break;
			}
			 Community::setIsThrottled(true);
			 isThrottled=true;
		}
		else if (!strcmp(argv[i], "-v") || !strcmp(argv[i], "-vars")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -v or -vars: argument missing!\n\n");
				showuse = true;
				break;
			}
			runtime_vars = new RuntimeVariables(argv[i]);
		}
		else if (!strcmp(argv[i], "-CCD")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -CCD: argument missing!\n\n");
				showuse = true;
				break;
			}
			collocated_delay = (int)atol(argv[i]);
		}
		else if (!strcmp(argv[i], "-VEI")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -VEI: argument missing!\n\n");
				showuse = true;
				break;
			}
			Partition::setVisualiationExportInterval((int)atol(argv[i]));
		}
#if SSFNET_EMULATION
		else if (!strcmp(argv[i], "-tp")) {
			SSFNET_STRING* nic;
			UID_t uid=0;
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -tp: missing interface ip name!\n\n");
				showuse = true;
				break;
			}
			nic=new SSFNET_STRING(argv[i]);
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -tp: missing traffic portal UID!\n\n");
				showuse = true;
				break;
			}
			uid = atoll(argv[i]);
			if(uid ==0) {
				fprintf(stderr, "ERROR: -tp: invalid traffic portal UID!\n\n");
				showuse = true;
				break;
			}
			portal_map->insert(SSFNET_MAKE_PAIR(uid,nic));
		}
		else if (!strcmp(argv[i], "-gateway")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -gateway: argument missing!\n\n");
				showuse = true;
				break;
			}
			char* pch = strtok(argv[i],":");
			SSFNET_STRING server;
			int port=0;
			while(pch!=NULL) {
				if(!server.length()) {
					server.append(pch);
				}
				else {
					port=atoi(pch);
				}
				pch = strtok(NULL,":");
			}
			if(!server.length() || port <=  0) {
				fprintf(stderr, "ERROR: -gateway: invalid gateway argument!\n\n");
				showuse = true;
				break;
			}
			OpenVPNDevice::addGateway(server,port);
		}
#endif
#if SSFNET_DEBUG
		else if (!strcmp(argv[i], "-DL") || !strcmp(argv[i], "-DEBUG_LIST")) {
			LogStreamFactory::STR_PAIR_MAP& lgs=LogStreamFactory::instance().getLoggers();
			fprintf(stderr,"The available logger streams available for configuration are:\n");
			for(LogStreamFactory::STR_PAIR_MAP::iterator i = lgs.begin(); i!=lgs.end();i++) {
				fprintf(stderr,"\t%s\n",i->first.c_str());
			}
			return -1;
		}
		else if (!strcmp(argv[i], "-DS")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -DS is missing the logger and log level!!\n\n");
				showuse = true;
				break;
			}
			//check the logger is okay
			LogStreamFactory::STR_PAIR_MAP& lgs=LogStreamFactory::instance().getLoggers();
			SSFNET_STRING str(argv[i]);
			LogStreamFactory::STR_PAIR_MAP::iterator rv = lgs.find(str);
			if(rv == lgs.end()) {
				fprintf(stderr, "ERROR: -DS: %s is not a valid logger!!!\n\n", argv[i]);
				showuse = true;
				break;
			}
			LogStream* logger = rv->second.first;
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -DS is missing the log level!!\n\n");
				showuse = true;
				break;
			}
			if(!strcmp("DEBUG",argv[i])) {
				log_cgs.push_back(SSFNET_MAKE_PAIR(logger,new LogLvl(LogLvl::DEBUG_LVL)));
			}
			else if(!strcmp("WARN",argv[i])) {
				log_cgs.push_back(SSFNET_MAKE_PAIR(logger,new LogLvl(LogLvl::WARN_LVL)));
			}
			else if(!strcmp("INFO",argv[i])) {
				log_cgs.push_back(SSFNET_MAKE_PAIR(logger,new LogLvl(LogLvl::INFO_LVL)));
			}
			else if(!strcmp("OFF",argv[i])) {
				log_cgs.push_back(SSFNET_MAKE_PAIR(logger,new LogLvl(LogLvl::NONE_LVL)));
			}
			else {
				fprintf(stderr, "ERROR: -DS: invalid level '%s'!\n\n",argv[i]);
				showuse = true;
				break;
			}
		}
		else if (!strcmp(argv[i], "-D") || !strcmp(argv[i], "-DEBUG")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -D or -DEBUG: argument missing!\n\n");
				showuse = true;
				break;
			}
			if(!strcmp("DEBUG",argv[i])) {
				LogStreamFactory::instance().setDefaultThreshold(new LogLvl(LogLvl::DEBUG_LVL));
			}
			else if(!strcmp("WARN",argv[i])) {
				LogStreamFactory::instance().setDefaultThreshold(new LogLvl(LogLvl::WARN_LVL));
			}
			else if(!strcmp("INFO",argv[i])) {
				LogStreamFactory::instance().setDefaultThreshold(new LogLvl(LogLvl::INFO_LVL));
			}
			else if(!strcmp("OFF",argv[i])) {
				LogStreamFactory::instance().setDefaultThreshold(new LogLvl(LogLvl::NONE_LVL));
			}
			else {
				fprintf(stderr, "ERROR: -D or -DEBUG: invalid level '%s'!\n\n",argv[i]);
				showuse = true;
				break;
			}
		}
#endif
		else if (!strcmp(argv[i], "-enable_state_file")) {
			useStateFile=true;
			if(useStateStream) {
				fprintf(stderr, "ERROR: Cannot enable both state_file and state_streams!\n\n");
				showuse = true;
				break;
			}
		}
		else if (!strcmp(argv[i], "-enable_state_stream")) {
			useStateStream=true;
			if(useStateFile) {
				fprintf(stderr, "ERROR: Cannot enable both state_file and state_streams!\n\n");
				showuse = true;
				break;
			}
		}
		else if (!strcmp(argv[i], "-stream_server")) {
			if (++i >= argc) {
				fprintf(stderr, "ERROR: -stream_server: argument missing!\n\n");
				showuse = true;
				break;
			}
			char* pch = strtok(argv[i],":");
			SSFNET_STRING server;
			int port=0;
			while(pch!=NULL) {
				if(!server.length()) {
					server.append(pch);
				}
				else {
					port=atoi(pch);
				}
				pch = strtok(NULL,":");
			}
			if(!server.length() || port <=  0) {
				fprintf(stderr, "ERROR: -set_state_stream_server: invalid gateway argument!\n\n");
				showuse = true;
				break;
			}
			TCPStateLogger::setTarget(server,port);
		}
		else {
			argv[offset++] = argv[i];
		}
	}
#if SSFNET_DEBUG
	//must happen before anything else!
	for(LogConfigList::iterator i = log_cgs.begin();i!=log_cgs.end();i++) {
		i->first->configureThreshold(i->second);
	}
	log_cgs.clear();
#endif

	argv[offset] = 0;
	argc = offset;

	if (showuse) {
		show_usage(argv[0]);
		return -1; // nonzero will silence ssf from warning the missing joinAll
	}

	if (argc != 3) {
#if PRIME_SSF_DISTSIM
		if (argc != 4) {
#endif
		fprintf(stderr, "ERROR: incorrect command-line arguments!\n\n");
		show_usage(argv[0]);
		return -1;
#if PRIME_SSF_DISTSIM
		}
#endif
	}

	double end_time = atof(argv[1]);
	if (end_time <= 0) {
		fprintf(stderr, "ERROR: invalid simulation time: %s.\n\n", argv[1]);
		show_usage(argv[0]);
		return -1;
	}
	Partition* partition = NULL;
	LOG_DEBUG("end time="<<end_time<<endl)
	if(!runtime_vars)
		runtime_vars = new RuntimeVariables();
	if(collocated_delay>0) {
		Community::setCollocatedCommunityDelay(VirtualTime(collocated_delay,VirtualTime::MILLISECOND));
	}

#if SSFNET_EMULATION
	{
		//register portals -- must be done before the partition is created!
		for(SSFNET_UID2STR_MAP::iterator it = portal_map->begin(); it!= portal_map->end();it++) {
			SSFNET_STRING* dev = lookupDeviceByIP(it->second);
			if(!dev) {
				LOG_ERROR("Unable to find an interface with IP "<<it->second<<endl);
			}
			Partition::addTrafficPortalMappping(it->first,dev);
		}
	}
#endif

#if PRIME_SSF_DISTSIM
	if(argc == 4) {
		char tlv[strlen(argv[2])+strlen(argv[3])+10];
		sprintf(tlv,"%s/%s_part_%i.tlv",argv[3],argv[2],(prime::ssf::ssf_machine_index()+1));
		LOG_DEBUG("Opening tlv file '"<< SSFNET_STRING(tlv) <<"'"<<endl);
		ModelBuilder* mb = new ModelBuilder(tlv,useStateFile, useStateStream, runtime_vars);
		partition = mb->buildModel();
		delete mb;
	}
	else
#endif
	{
		if (NULL != strchr(argv[2], ':')) {
			char* port = strchr(argv[2], ':');
			port--;
			*port = '\n';
			port++;
			LOG_DEBUG("Using a server as the model source [server="<<argv[2]<<", port="<<port<<"]."<<endl)
			LOG_ERROR("XXX don't support fetching the model from a model DB yet."<<endl);
		} else {
			LOG_DEBUG("Opening tlv file "<<argv[2]<<endl)
			//its a TLV file
			ModelBuilder* mb = new ModelBuilder(argv[2],useStateFile, useStateStream, runtime_vars);
			partition = mb->buildModel();
			delete mb;
		}
	}
#if TEST_ROUTING == 1
	routing_test_init();
#endif

	//call init on all the model entities
	partition->init();

	//setup routing spheres
	partition->bootstrap();
	LOG_INFO("DONE BOOTSTRAP"<<endl);

#ifdef SSFNET_EMULATION
	LOG_DEBUG("starting simulation: end_time="<<end_time<<", emulation_ratio="<<emuratio<<endl)
	Entity::startAll(0, (ltime_t) VirtualTime(end_time, VirtualTime::SECOND),
			emuratio * (double) VirtualTime::SECOND);
#else
	if(isThrottled || useStateStream) {
		// run simulation for the given number of seconds
		LOG_DEBUG("starting simulation: end_time="<<end_time<<", emulation_ratio="<<emuratio<<endl)
		Entity::startAll(0, (ltime_t) VirtualTime(end_time, VirtualTime::SECOND),
				emuratio * (double) VirtualTime::SECOND);
	}
	else {
		LOG_DEBUG("starting simulation: end_time="<<end_time<<endl)
		Entity::startAll(0, (ltime_t)VirtualTime(end_time, VirtualTime::SECOND));
	}
#endif
	Entity::joinAll();
	LOG_DEBUG("wrapping up simulation"<<endl)
	partition->wrapup();
#if TEST_ROUTING == 1
	routing_test_wrapup();
#endif
	delete partition;
	return 0;
}

