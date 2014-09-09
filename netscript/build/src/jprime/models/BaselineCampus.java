package jprime.models;

/*
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

import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.TCPMaster.ITCPMaster;
import jprime.UDPMaster.IUDPMaster;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Ting Li
 *
 */
public class BaselineCampus extends ModelInterface{
	public static enum RoutingType {
		SHORTEST_PATH_L1,
		SHORTEST_PATH_L12,
		SHORTEST_PATH_L123,
	}
	
	protected final RoutingType routingType;
	
	/**
	 * @param db
	 * @param exp
	 */
	protected BaselineCampus(Database db, Experiment exp, RoutingType routingType) {
		super(db, exp, new ArrayList<ModelParam>());
		this.routingType=routingType;
	}

	/**
	 * @param db
	 * @param expName
	 */
	protected BaselineCampus(Database db, String expName, RoutingType routingType) {
		super(db, expName, new ArrayList<ModelParam>());
		this.routingType=routingType;
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> values) {
		Net top = exp.createTopNet("topnet");
		createBaselineCampus(top);
		return top;
	}
	
	public IRouter createBaselineCampus(INet top) {
		IRouter rv = null;
		switch(routingType) {
		case SHORTEST_PATH_L123:
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			top.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		
		//net01 includes net0, net1 and two routers
		INet net01 = null;
		switch(routingType) {
		case SHORTEST_PATH_L1:
			net01 = top.createNet("net01");
			break;
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L123:
			net01 = top.createNet("net01");
			net01.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		//net0 includes three routers
		INet net0 = null;
		switch(routingType) {
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			net0 = net01.createNet("net0"); 
			break;
		case SHORTEST_PATH_L123:
			net0 = net01.createNet("net0"); 
			net0.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		IRouter net0_r0 = net0.createRouter("r0");
		rv = net0_r0;
		IInterface net0_r0_if0 = net0_r0.createInterface("if0");
		net0_r0_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if0.setLatency("0.0");
		IInterface net0_r0_if1 = net0_r0.createInterface("if1");
		net0_r0_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if1.setLatency("0.0");
		IInterface net0_r0_if2 = net0_r0.createInterface("if2");
		net0_r0_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if2.setLatency("0.0");
		IInterface net0_r0_if3 = net0_r0.createInterface("if3");
		net0_r0_if3.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if3.setLatency("0.0");
		IInterface net0_r0_if4 = net0_r0.createInterface("if4");
		net0_r0_if4.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if4.setLatency("0.0");
		/*IInterface net0_r0_if5 = net0_r0.createInterface("if5");
		net0_r0_if5.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if5.setLatency("0.0");
		IInterface net0_r0_if6 = net0_r0.createInterface("if6");
		net0_r0_if6.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if6.setLatency("0.0");
		IInterface net0_r0_if7 = net0_r0.createInterface("if7");
		net0_r0_if7.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if7.setLatency("0.0");*/

		IRouter net0_r1 = net0.createRouter("r1");
		IInterface net0_r1_if0 = net0_r1.createInterface("if0");
		net0_r1_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r1_if0.setLatency("0.0");
		IInterface net0_r1_if1 = net0_r1.createInterface("if1");
		net0_r1_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r1_if1.setLatency("0.0");
		IInterface net0_r1_if2 = net0_r1.createInterface("if2");
		net0_r1_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r1_if2.setLatency("0.0");
		
		IRouter net0_r2 = net0.createRouter("r2");
		IInterface net0_r2_if0 = net0_r2.createInterface("if0");
		net0_r2_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r2_if0.setLatency("0.0");
		IInterface net0_r2_if1 = net0_r2.createInterface("if1");
		net0_r2_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r2_if1.setLatency("0.0");
		IInterface net0_r2_if2 = net0_r2.createInterface("if2");
		net0_r2_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r2_if2.setLatency("0.0");
		
		//connect the routers in net0
		ILink net0_r0_r1 = net0.createLink("r0_r1");
		net0_r0_r1.createInterface(net0_r0_if1);
		net0_r0_r1.createInterface(net0_r1_if2);
		net0_r0_r1.setDelay((float)0.005); //r2r
		
		ILink net0_r1_r2 = net0.createLink("r1_r2");
		net0_r1_r2.createInterface(net0_r1_if0);
		net0_r1_r2.createInterface(net0_r2_if1);
		net0_r1_r2.setDelay((float)0.005); //r2r
		
		ILink net0_r0_r2 = net0.createLink("r0_r2");
		net0_r0_r2.createInterface(net0_r0_if0);
		net0_r0_r2.createInterface(net0_r2_if2);
		net0_r0_r2.setDelay((float)0.005); //r2r
		
		//net1 includes two routers and 4 hosts
		INet net1 = null;
		switch(routingType) {
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			net1 = net01.createNet("net1"); 
			//no op
			break;
		case SHORTEST_PATH_L123:
			net1 = net01.createNet("net1"); 
			net1.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		
		IRouter net1_r0 = net1.createRouter("r0");
		IInterface net1_r0_if0 = net1_r0.createInterface("if0");
		net1_r0_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if0.setLatency("0.0");
		IInterface net1_r0_if1 = net1_r0.createInterface("if1");
		net1_r0_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if1.setLatency("0.0");
		IInterface net1_r0_if2 = net1_r0.createInterface("if2");
		net1_r0_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if2.setLatency("0.0");
		IInterface net1_r0_if3 = net1_r0.createInterface("if3");
		net1_r0_if3.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if3.setLatency("0.0");
		
		IRouter net1_r1 = net1.createRouter("r1");
		IInterface net1_r1_if0 = net1_r1.createInterface("if0");
		net1_r1_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r1_if0.setLatency("0.0");
		IInterface net1_r1_if1 = net1_r1.createInterface("if1");
		net1_r1_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r1_if1.setLatency("0.0");
		IInterface net1_r1_if2 = net1_r1.createInterface("if2");
		net1_r1_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r1_if2.setLatency("0.0");
		
		ITCPMaster t_master[] = new ITCPMaster[4];
		IUDPMaster u_master[] = new IUDPMaster[4];
		
		IHost net1_h2 = net1.createHost("h2");
		IInterface net1_h2_if0 = net1_h2.createInterface("if0");
		net1_h2_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h2_if0.setLatency("0.0");
		//TCP Master
		t_master[0] = net1_h2.createTCPMaster();
		net1_h2.createHTTPServer();
		//UDP Master
		u_master[0] = net1_h2.createUDPMaster();
			
		IHost net1_h3 = net1.createHost("h3");
		IInterface net1_h3_if0 = net1_h3.createInterface("if0");
		net1_h3_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h3_if0.setLatency("0.0");
		t_master[1] = net1_h3.createTCPMaster();
		net1_h3.createHTTPServer();
		u_master[1] = net1_h3.createUDPMaster();
		
		IHost net1_h4 = net1.createHost("h4");
		IInterface net1_h4_if0 = net1_h4.createInterface("if0");
		net1_h4_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h4_if0.setLatency("0.0");
		t_master[2] = net1_h4.createTCPMaster();
		net1_h4.createHTTPServer();
		u_master[2] = net1_h4.createUDPMaster();
		
		IHost net1_h5 = net1.createHost("h5");
		IInterface net1_h5_if0 = net1_h5.createInterface("if0");
		net1_h5_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h5_if0.setLatency("0.0");
		t_master[3] = net1_h5.createTCPMaster();
		net1_h5.createHTTPServer();
		u_master[3] = net1_h5.createUDPMaster();
		
		for(int i=0; i<4; i++) {
			t_master[i].setIss("10000"); //initial sequence number
			t_master[i].setMss("1000"); //maximum segment size
			t_master[i].setRcvWndSize("512"); //receive buffer size
			t_master[i].setSndWndSize("512"); //maximum send window size
			u_master[i].setMaxDatagramSize("10000"); //max UDP datagram size (payload bytes)
		}
		
		//connect the routers and hosts in net1
		ILink net1_r0_r1 = net1.createLink("r0_r1");
		net1_r0_r1.createInterface(net1_r0_if2);
		net1_r0_r1.createInterface(net1_r1_if0);
		net1_r0_r1.setDelay((float)0.001); //lan
		
		ILink net1_r0_h2 = net1.createLink("r0_h2");
		net1_r0_h2.createInterface(net1_r0_if0);
		net1_r0_h2.createInterface(net1_h2_if0);
		net1_r0_h2.setDelay((float)0.001); //lan
		
		ILink net1_r0_h3 = net1.createLink("r0_h3");
		net1_r0_h3.createInterface(net1_r0_if1);
		net1_r0_h3.createInterface(net1_h3_if0);
		net1_r0_h3.setDelay((float)0.001); //lan
		
		ILink net1_r1_h4 = net1.createLink("r1_h4");
		net1_r1_h4.createInterface(net1_r1_if1);
		net1_r1_h4.createInterface(net1_h4_if0);
		net1_r1_h4.setDelay((float)0.001); //lan
		
		ILink net1_r1_h5 = net1.createLink("r1_h5");
		net1_r1_h5.createInterface(net1_r1_if2);
		net1_r1_h5.createInterface(net1_h5_if0);
		net1_r1_h5.setDelay((float)0.001); //lan
		

		ILink net01_r4_r5 = null;
		ILink net01_net0_r4 = null;
		ILink net01_net0_r5 = null;
		ILink net01_net0_net1 = null;
		IInterface net01_r4_if0 = null;
		IInterface net01_r4_if1 = null;
		IInterface net01_r4_if2 = null;
		IInterface net01_r4_if3 = null;
		IInterface net01_r5_if0 = null;
		IInterface net01_r5_if1 = null;
		IInterface net01_r5_if2 = null;
		IInterface net01_r5_if3 = null;

		
		switch(routingType) {
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
		case SHORTEST_PATH_L123:
		{
			//routers in net01
			IRouter net01_r4 = net01.createRouter("r4");
			//rv=net01_r4;
			net01_r4_if0 = net01_r4.createInterface("if0");
			net01_r4_if0.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r4_if0.setLatency("0.0");
			net01_r4_if1 = net01_r4.createInterface("if1");
			net01_r4_if1.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r4_if1.setLatency("0.0");
			net01_r4_if2 = net01_r4.createInterface("if2");
			net01_r4_if2.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r4_if2.setLatency("0.0");
			net01_r4_if3 = net01_r4.createInterface("if3");
			net01_r4_if3.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r4_if3.setLatency("0.0");
			
			IRouter net01_r5 = net01.createRouter("r5");	
			net01_r5_if0 = net01_r5.createInterface("if0");
			net01_r5_if0.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r5_if0.setLatency("0.0");
			net01_r5_if1 = net01_r5.createInterface("if1");
			net01_r5_if1.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r5_if1.setLatency("0.0");
			net01_r5_if2 = net01_r5.createInterface("if2");
			net01_r5_if2.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r5_if2.setLatency("0.0");
			net01_r5_if3 = net01_r5.createInterface("if3");
			net01_r5_if3.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r5_if3.setLatency("0.0");
			
			//link router 4&5
			net01_r4_r5 = net01.createLink("r4_r5");
			net01_r4_r5.createInterface(net01_r4_if1);
			net01_r4_r5.createInterface(net01_r5_if3);
			net01_r4_r5.setDelay((float)0.005); //r2r
			
			//links from backbone net 0 to routers 4, 5, net1_r0
			net01_net0_r4 = net01.createLink("net0_r4");
			net01_net0_r4.createInterface(net0_r0_if2);
			net01_net0_r4.createInterface(net01_r4_if0);
			net01_net0_r4.setDelay((float)0.005);//r2r
			
			net01_net0_r5 = net01.createLink("net0_r5");
			net01_net0_r5.createInterface(net0_r1_if1);
			net01_net0_r5.createInterface(net01_r5_if0);
			net01_net0_r5.setDelay((float)0.005);//r2r
			
			net01_net0_net1 = net01.createLink("net0_net1");
			net01_net0_net1.createInterface(net0_r2_if0);
			net01_net0_net1.createInterface(net1_r0_if3);
			net01_net0_net1.setDelay((float)0.005);//r2r
		}
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		
		//net2 includes seven routers and seven lans
		INet net2 = top.createNet("net2"); 
		switch(routingType) {
		case SHORTEST_PATH_L1:
			//no op
			break;
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L123:
			net2.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		IRouter net2_r0 = net2.createRouter("r0");
		IInterface net2_r0_if0 = net2_r0.createInterface("if0");
		net2_r0_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r0_if0.setLatency("0.0");
		IInterface net2_r0_if1 = net2_r0.createInterface("if1");
		net2_r0_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r0_if1.setLatency("0.0");
		IInterface net2_r0_if2 = net2_r0.createInterface("if2");
		net2_r0_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r0_if2.setLatency("0.0");
		
		IRouter net2_r1 = net2.createRouter("r1");
		IInterface net2_r1_if0 = net2_r1.createInterface("if0");
		net2_r1_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r1_if0.setLatency("0.0");
		IInterface net2_r1_if1 = net2_r1.createInterface("if1");
		net2_r1_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r1_if1.setLatency("0.0");
		IInterface net2_r1_if2 = net2_r1.createInterface("if2");
		net2_r1_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r1_if2.setLatency("0.0");
		
		IRouter net2_r2 = net2.createRouter("r2");
		IInterface net2_r2_if0 = net2_r2.createInterface("if0");
		net2_r2_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if0.setLatency("0.0");
		IInterface net2_r2_if1 = net2_r2.createInterface("if1");
		net2_r2_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if1.setLatency("0.0");
		IInterface net2_r2_if2 = net2_r2.createInterface("if2");
		net2_r2_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if2.setLatency("0.0");
		IInterface net2_r2_if3 = net2_r2.createInterface("if3");
		net2_r2_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if3.setLatency("0.0");
		
		IRouter net2_r3 = net2.createRouter("r3");
		IInterface net2_r3_if0 = net2_r3.createInterface("if0");
		net2_r3_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if0.setLatency("0.0");
		IInterface net2_r3_if1 = net2_r3.createInterface("if1");
		net2_r3_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if1.setLatency("0.0");
		IInterface net2_r3_if2 = net2_r3.createInterface("if2");
		net2_r3_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if2.setLatency("0.0");
		IInterface net2_r3_if3 = net2_r3.createInterface("if3");
		net2_r3_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if3.setLatency("0.0");
		
		IRouter net2_r4 = net2.createRouter("r4");
		IInterface net2_r4_if0 = net2_r4.createInterface("if0");
		net2_r4_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r4_if0.setLatency("0.0");
		IInterface net2_r4_if1 = net2_r4.createInterface("if1");
		net2_r4_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r4_if1.setLatency("0.0");
		
		IRouter net2_r5 = net2.createRouter("r5");
		IInterface net2_r5_if0 = net2_r5.createInterface("if0");
		net2_r5_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r5_if0.setLatency("0.0");
		IInterface net2_r5_if1 = net2_r5.createInterface("if1");
		net2_r5_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r5_if1.setLatency("0.0");
		IInterface net2_r5_if2 = net2_r5.createInterface("if2");
		net2_r5_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r5_if2.setLatency("0.0");
		
		IRouter net2_r6 = net2.createRouter("r6");
		IInterface net2_r6_if0 = net2_r6.createInterface("if0");
		net2_r6_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if0.setLatency("0.0");
		IInterface net2_r6_if1 = net2_r6.createInterface("if1");
		net2_r6_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if1.setLatency("0.0");
		IInterface net2_r6_if2 = net2_r6.createInterface("if2");
		net2_r6_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if2.setLatency("0.0");
		IInterface net2_r6_if3 = net2_r6.createInterface("if3");
		net2_r6_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if3.setLatency("0.0");
		
		//router to router links in net2
		ILink net2_r0_r1 = net2.createLink("r0_r1");
		net2_r0_r1.createInterface(net2_r0_if1);
		net2_r0_r1.createInterface(net2_r1_if2);
		net2_r0_r1.setDelay((float)0.005);
		
		ILink net2_r0_r2 = net2.createLink("r0_r2");
		net2_r0_r2.createInterface(net2_r0_if2);
		net2_r0_r2.createInterface(net2_r2_if0);
		net2_r0_r2.setDelay((float)0.005);
		
		ILink net2_r2_r3 = net2.createLink("r2_r3");
		net2_r2_r3.createInterface(net2_r2_if1);
		net2_r2_r3.createInterface(net2_r3_if3);
		net2_r2_r3.setDelay((float)0.005);
		
		ILink net2_r2_r4 = net2.createLink("r2_r4");
		net2_r2_r4.createInterface(net2_r2_if2);
		net2_r2_r4.createInterface(net2_r4_if0);
		net2_r2_r4.setDelay((float)0.005);
		
		ILink net2_r1_r3 = net2.createLink("r1_r3");
		net2_r1_r3.createInterface(net2_r1_if1);
		net2_r1_r3.createInterface(net2_r3_if0);
		net2_r1_r3.setDelay((float)0.005);
		
		ILink net2_r3_r5 = net2.createLink("r3_r5");
		net2_r3_r5.createInterface(net2_r3_if2);
		net2_r3_r5.createInterface(net2_r5_if0);
		net2_r3_r5.setDelay((float)0.005);
		
		ILink net2_r5_r6 = net2.createLink("r5_r6");
		net2_r5_r6.createInterface(net2_r5_if2);
		net2_r5_r6.createInterface(net2_r6_if0);
		net2_r5_r6.setDelay((float)0.005);
		
		//the lans in net2
		INet net23 = net2.createNet("lan0"); 
		switch(routingType) {
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			//no op
			break;
		case SHORTEST_PATH_L123:
			net23.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		IRouter net23_r0 = net23.createRouter("r0");
		IInterface net23_r0_if0 = net23_r0.createInterface("if0");
		net23_r0_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net23_r0_if0.setLatency("0.0");
		IInterface net23_r0_if3 = net23_r0.createInterface("if3");
		net23_r0_if0.setBitRate("100000000"); //lan, 100Mbs
		net23_r0_if0.setLatency("0.0");
		IInterface net23_r0_if4 = net23_r0.createInterface("if4");
		net23_r0_if0.setBitRate("100000000"); //lan, 100Mbs
		net23_r0_if0.setLatency("0.0");
		IInterface net23_r0_if5 = net23_r0.createInterface("if5");
		net23_r0_if0.setBitRate("100000000"); //lan, 100Mbs
		net23_r0_if0.setLatency("0.0");
		IInterface net23_r0_if6 = net23_r0.createInterface("if6");
		net23_r0_if0.setBitRate("100000000"); //lan, 100Mbs
		net23_r0_if0.setLatency("0.0");
		
		IHost replica = null;
		IHost h[] = new IHost[43];
		IInterface iface[] = new IInterface[43];
		ITCPMaster t[] = new ITCPMaster[43];
		IUDPMaster u[] = new IUDPMaster[43];
		
		for(int i=1 ; i<43 ; i++){
			if(replica == null){
				h[i] = net23.createHost("h"+i);
				iface[i] = h[i].createInterface("if0");
				iface[i].setBitRate("100000000"); //lan, 100Mbs
				iface[i].setLatency("0.0");
				//TCP Master
				t[i] = h[i].createTCPMaster();
				t[i].setIss("10000"); //initial sequence number
				t[i].setMss("1000"); //maximum segment size
				t[i].setRcvWndSize("512"); //receive buffer size
				t[i].setSndWndSize("512"); //maximum send window size
				//UDP Master
				u[i] = h[i].createUDPMaster();
				u[i].setMaxDatagramSize("10000"); //max UDP datagram size (payload bytes)
				replica = h[i];
			}else{
				h[i] = net23.createHostReplica("h"+i, replica);
			}
		}

		//links in lan 
		ILink net23_l1 = net23.createLink("l1");
		net23_l1.setDelay((float)0.001); //lan
		net23_l1.createInterface(net23_r0_if3);
		net23_l1.createInterface((IInterface)h[1].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[2].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[3].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[4].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[5].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[6].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[7].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[8].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[9].getChildByName("if0"));
		net23_l1.createInterface((IInterface)h[10].getChildByName("if0"));
		
		ILink net23_l2 = net23.createLink("l2");
		net23_l2.setDelay((float)0.001); //lan
		net23_l2.createInterface(net23_r0_if4);
		net23_l2.createInterface((IInterface)h[11].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[12].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[13].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[14].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[15].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[16].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[17].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[18].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[19].getChildByName("if0"));
		net23_l2.createInterface((IInterface)h[20].getChildByName("if0"));
		
		ILink net23_l3 = net23.createLink("l3");
		net23_l3.setDelay((float)0.001); //lan
		net23_l3.createInterface(net23_r0_if5);
		net23_l3.createInterface((IInterface)h[21].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[22].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[23].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[24].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[25].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[26].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[27].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[28].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[29].getChildByName("if0"));
		net23_l3.createInterface((IInterface)h[30].getChildByName("if0"));
		
		ILink net23_l4 = net23.createLink("l4");
		net23_l4.createInterface(net23_r0_if6);	
		net23_l4.setDelay((float)0.001); //lan
		for(int i=31;i<43;i++) {
			IInterface ii=(IInterface)h[i].getChildByName("if0");
			net23_l4.createInterface(ii);
		}
		
		//connect net23 with the router in net2
		ILink net2_lan0_r2 = net2.createLink("lan0_r2");
		net2_lan0_r2.setDelay((float)0.005); //r2r
		net2_lan0_r2.createInterface(net23_r0_if0);
		net2_lan0_r2.createInterface(net2_r2_if3);
		
		//the other 6 lans and links between the lan to the router in net2
		INet net41 = net2.createNetReplica("lan1", net23);
		ILink net2_lan1_r4 = net2.createLink("lan1_r4");
		net2_lan1_r4.setDelay((float)0.005); //r2r
		net2_lan1_r4.createInterface((IInterface)net41.getChildByName("r0").getChildByName("if0"));
		net2_lan1_r4.createInterface(net2_r4_if1);
		
		INet net31 = net2.createNetReplica("lan2", net23);
		ILink net2_lan2_r3 = net2.createLink("lan2_r3");
		net2_lan2_r3.setDelay((float)0.005); //r2r
		net2_lan2_r3.createInterface((IInterface)net31.getChildByName("r0").getChildByName("if0"));
		net2_lan2_r3.createInterface(net2_r3_if1);
		
		INet net51 = net2.createNetReplica("lan3", net23);
		ILink net2_lan3_r5 = net2.createLink("lan3_r5");
		net2_lan3_r5.setDelay((float)0.005); //r2r
		net2_lan3_r5.createInterface((IInterface)net51.getChildByName("r0").getChildByName("if0"));
		net2_lan3_r5.createInterface(net2_r5_if1);		
		
		INet net63 = net2.createNetReplica("lan4", net23);
		ILink net2_lan4_r6 = net2.createLink("lan4_r6");
		net2_lan4_r6.setDelay((float)0.005); //r2r
		net2_lan4_r6.createInterface((IInterface)net63.getChildByName("r0").getChildByName("if0"));
		net2_lan4_r6.createInterface(net2_r6_if3);
		
		INet net62 = net2.createNetReplica("lan5", net23);
		ILink net2_lan5_r6 = net2.createLink("lan5_r6");
		net2_lan5_r6.setDelay((float)0.005); //r2r
		net2_lan5_r6.createInterface((IInterface)net62.getChildByName("r0").getChildByName("if0"));
		net2_lan5_r6.createInterface(net2_r6_if2);
		
		INet net61 = net2.createNetReplica("lan6", net23);
		ILink net2_lan6_r6 = net2.createLink("lan6_r6");
		net2_lan6_r6.setDelay((float)0.005); //r2r
		net2_lan6_r6.createInterface((IInterface)net61.getChildByName("r0").getChildByName("if0"));
		net2_lan6_r6.createInterface(net2_r6_if1);
		//end network2
		
		//net3 includes 4 routers and 5 lans
		INet net3 = top.createNet("net3");
		switch(routingType) {
		case SHORTEST_PATH_L1:
			//no op
			break;
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L123:
			net3.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		IRouter net3_r0 = net3.createRouter("r0");
		IInterface net3_r0_if0 = net3_r0.createInterface("if0");
		net3_r0_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if0.setLatency("0.0");
		IInterface net3_r0_if1 = net3_r0.createInterface("if1");
		net3_r0_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if1.setLatency("0.0");
		IInterface net3_r0_if2 = net3_r0.createInterface("if2");
		net3_r0_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if2.setLatency("0.0");
		IInterface net3_r0_if3 = net3_r0.createInterface("if3");
		net3_r0_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if2.setLatency("0.0");
		
		IRouter net3_r1 = net3.createRouter("r1");
		IInterface net3_r1_if0 = net3_r1.createInterface("if0");
		net3_r1_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if0.setLatency("0.0");
		IInterface net3_r1_if1 = net3_r1.createInterface("if1");
		net3_r1_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if1.setLatency("0.0");
		IInterface net3_r1_if2 = net3_r1.createInterface("if2");
		net3_r1_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if2.setLatency("0.0");
		IInterface net3_r1_if3 = net3_r1.createInterface("if3");
		net3_r1_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if3.setLatency("0.0");
		
		IRouter net3_r2 = net3.createRouter("r2");
		IInterface net3_r2_if0 = net3_r2.createInterface("if0");
		net3_r2_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r2_if0.setLatency("0.0");
		IInterface net3_r2_if1 = net3_r2.createInterface("if1");
		net3_r2_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r2_if1.setLatency("0.0");
		IInterface net3_r2_if2 = net3_r2.createInterface("if2");
		net3_r2_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r2_if2.setLatency("0.0");

		IRouter net3_r3 = net3.createRouter("r3");
		IInterface net3_r3_if0 = net3_r3.createInterface("if0");
		net3_r3_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if0.setLatency("0.0");
		IInterface net3_r3_if1 = net3_r3.createInterface("if1");
		net3_r3_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if1.setLatency("0.0");
		IInterface net3_r3_if2 = net3_r3.createInterface("if2");
		net3_r3_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if2.setLatency("0.0");
		IInterface net3_r3_if3 = net3_r3.createInterface("if3");
		net3_r3_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if3.setLatency("0.0");
		
		//router to router links in net3
		ILink net3_r0_r1 = net3.createLink("r0_r1");
		net3_r0_r1.setDelay((float)0.005); //r2r
		net3_r0_r1.createInterface(net3_r0_if1);
		net3_r0_r1.createInterface(net3_r1_if3);
		
		ILink net3_r1_r2 = net3.createLink("r1_r2");
		net3_r1_r2.setDelay((float)0.005); //r2r
		net3_r1_r2.createInterface(net3_r1_if2);
		net3_r1_r2.createInterface(net3_r2_if0);
		
		ILink net3_r2_r3 = net3.createLink("r2_r3");
		net3_r2_r3.setDelay((float)0.005); //r2r
		net3_r2_r3.createInterface(net3_r2_if1);
		net3_r2_r3.createInterface(net3_r3_if3);
		
		ILink net3_r1_r3 = net3.createLink("r1_r3");
		net3_r1_r3.setDelay((float)0.005); //r2r
		net3_r1_r3.createInterface(net3_r1_if1);
		net3_r1_r3.createInterface(net3_r3_if0);
		
		//5 lans composed of a router with local area networks
		INet net3_lan21 = net3.createNetReplica("lan0", net23);
		ILink net3_lan0_r0 = net3.createLink("lan0_r0");
		net3_lan0_r0.setDelay((float)0.005); //r2r
		net3_lan0_r0.createInterface((IInterface)net3_lan21.getChildByName("r0").getChildByName("if0"));
		net3_lan0_r0.createInterface(net3_r0_if3);
		
		INet net3_lan22 = net3.createNetReplica("lan1", net23);
		ILink net3_lan1_r0 = net3.createLink("lan1_r0");
		net3_lan1_r0.setDelay((float)0.005); //r2r
		net3_lan1_r0.createInterface((IInterface)net3_lan22.getChildByName("r0").getChildByName("if0"));
		net3_lan1_r0.createInterface(net3_r0_if2);
		
		INet net3_lan23 = net3.createNetReplica("lan2", net23);
		ILink net3_lan2_r2 = net3.createLink("lan2_r2");
		net3_lan2_r2.setDelay((float)0.005); //r2r
		net3_lan2_r2.createInterface((IInterface)net3_lan23.getChildByName("r0").getChildByName("if0"));
		net3_lan2_r2.createInterface(net3_r2_if2);
		
		INet net3_lan24 = net3.createNetReplica("lan3", net23);
		ILink net3_lan3_r3 = net3.createLink("lan3_r3");
		net3_lan3_r3.setDelay((float)0.005); //r2r
		net3_lan3_r3.createInterface((IInterface)net3_lan24.getChildByName("r0").getChildByName("if0"));
		net3_lan3_r3.createInterface(net3_r3_if2);
		
		INet net3_lan25 = net3.createNetReplica("lan4", net23);
		ILink net3_lan4_r3 = net3.createLink("lan4_r3");
		net3_lan4_r3.setDelay((float)0.005); //r2r
		net3_lan4_r3.createInterface((IInterface)net3_lan25.getChildByName("r0").getChildByName("if0"));
		net3_lan4_r3.createInterface(net3_r3_if1);
		//end network3
		
		//links to connect router4 in net01 to net2
		ILink r4_net2_1 = top.createLink("r4_net2_1");
		r4_net2_1.setDelay((float)0.005); //r2r
		r4_net2_1.createInterface(net01_r4_if2);
		r4_net2_1.createInterface(net2_r1_if0);
		
		ILink r4_net2_2 = top.createLink("r4_net2_2");
		r4_net2_2.setDelay((float)0.005); //r2r
		r4_net2_2.createInterface(net01_r4_if3);
		r4_net2_2.createInterface(net2_r0_if0);
		
		//links to connect router5 in net01 to net3
		ILink r5_net3_1 = top.createLink("r5_net3_1");
		r5_net3_1.setDelay((float)0.005); //r2r
		r5_net3_1.createInterface(net01_r5_if2);
		r5_net3_1.createInterface(net3_r0_if0);
		
		ILink r5_net3_2 = top.createLink("r5_net3_2");
		r5_net3_2.setDelay((float)0.005); //r2r
		r5_net3_2.createInterface(net01_r5_if1);
		r5_net3_2.createInterface(net3_r1_if0);		
		
		return rv;
	}
}
