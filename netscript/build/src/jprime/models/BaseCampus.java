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

import java.util.List;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Link.Link;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.routing.BGP;
import jprime.routing.BGPRelationShipType;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class BaseCampus extends ModelInterface{
	public static enum RoutingType {
		SHORTEST_PATH_L1("SHORTEST_PATH_L1"),
		SHORTEST_PATH_L12("SHORTEST_PATH_L12"),
		SHORTEST_PATH_L123("SHORTEST_PATH_L123"),
		HALF_SHORTEST_PATH("HALF_SHORTEST_PATH"),
		BGP_SHORTEST_PATH("BGP_SHORTEST_PATH"),
		ALGORITHMIC("ALGORITHMIC");
		
		private final String str;
		private RoutingType(String s) {
			str=s;
		}
		static RoutingType fromString(String s) {
			for(RoutingType rt : values()) {
				if(rt.str.compareToIgnoreCase(s)==0)
					return rt;
			}
			throw new RuntimeException("unknown rouitng type "+s);
		}
	}
	private static final List<ModelParam> getParameterDefinitions(List<ModelParam> parameterDefinitions) {
		parameterDefinitions.add(new ModelParam(ModelParamType.STRING, "Routing type", "routingType", "ROUTING_TYPE", "SHORTEST_PATH_L123"));
		return parameterDefinitions;
	}
	
	/**
	 * @param db
	 * @param exp
	 */
	public BaseCampus(Database db, Experiment exp, List<ModelParam> parameterDefinitions) {
		super(db,exp,getParameterDefinitions(parameterDefinitions));
	}

	/**
	 * @param db
	 * @param expName
	 */
	public BaseCampus(Database db, String expName, List<ModelParam> parameterDefinitions) {
		super(db, expName,getParameterDefinitions(parameterDefinitions));
	}
	
	private BaseCampus() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		createCampus(top, RoutingType.fromString(parameters.get("routingType").asString()));
		return top;
	}
	
	public IRouter createCampus(final INet top, final RoutingType routingType) {
		return createCampus(top,true, routingType);
	}
	
	public static void attachCampus(INet owningNet, IRouter toAttachTo) {		
		BaseCampus bc = new BaseCampus();
		IRouter gateway = bc.createCampus(owningNet, RoutingType.SHORTEST_PATH_L123);
		ILink l = ((INet)toAttachTo.getParent()).createLink();
		l.attachInterface(gateway.createInterface("outside_"+gateway.getParent().getAllChildren().size()));
		l.attachInterface(toAttachTo.createInterface("toCampus_"+toAttachTo.getParent().getAllChildren().size()));
	}
	
	public IRouter createCampus(final INet top, final boolean haveTopLevelRoutingProtocol, final RoutingType routingType) {
		BGP bgpRouting = null;
		switch(routingType) {
		case ALGORITHMIC:
			if(haveTopLevelRoutingProtocol)top.createAlgorithmicRouting();
			break;
		case SHORTEST_PATH_L123:
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			try {
				top.createShortestPath();
			} catch(Exception e)  { /* ignore it*/ }
			break;
		case BGP_SHORTEST_PATH:
			bgpRouting  = top.createBGP();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		
		//net01 includes net0, net1 and two routers
		INet net01 = null;
		switch(routingType) {
		case ALGORITHMIC:
		case SHORTEST_PATH_L1:
			net01 = top.createNet("net01");
			break;
		case BGP_SHORTEST_PATH:
			//no op
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
		case ALGORITHMIC:
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			net0 = net01.createNet("net0"); 
			break;
		case SHORTEST_PATH_L123:
			net0 = net01.createNet("net0"); 
			net0.createShortestPath();
			break;
		case BGP_SHORTEST_PATH:
			net0 = top.createNet("net0"); 
			net0.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		IRouter net0_r0 = net0.createRouter("r0");
		IInterface net0_r0_if0 = net0_r0.createInterface("if0");
		net0_r0_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if0.setLatency("0.0");
		net0_r0_if0.createFluidQueue();
		IInterface net0_r0_if1 = net0_r0.createInterface("if1");
		net0_r0_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if1.setLatency("0.0");
		net0_r0_if1.createFluidQueue();
		IInterface net0_r0_if2 = net0_r0.createInterface("if2");
		net0_r0_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if2.setLatency("0.0");
		net0_r0_if2.createFluidQueue();
		/*IInterface net0_r0_if3 = net0_r0.createInterface("if3");
		net0_r0_if3.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if3.setLatency("0.0");
		IInterface net0_r0_if4 = net0_r0.createInterface("if4");
		net0_r0_if4.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r0_if4.setLatency("0.0");
		IInterface net0_r0_if5 = net0_r0.createInterface("if5");
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
		net0_r1_if0.createFluidQueue();
		IInterface net0_r1_if1 = net0_r1.createInterface("if1");
		net0_r1_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r1_if1.setLatency("0.0");
		net0_r1_if1.createFluidQueue();
		IInterface net0_r1_if2 = net0_r1.createInterface("if2");
		net0_r1_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r1_if2.setLatency("0.0");
		net0_r1_if2.createFluidQueue();
		
		IRouter net0_r2 = net0.createRouter("r2");
		IInterface net0_r2_if0 = net0_r2.createInterface("if0");
		net0_r2_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r2_if0.setLatency("0.0");
		net0_r2_if0.createFluidQueue();
		IInterface net0_r2_if1 = net0_r2.createInterface("if1");
		net0_r2_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r2_if1.setLatency("0.0");
		net0_r2_if1.createFluidQueue();
		IInterface net0_r2_if2 = net0_r2.createInterface("if2");
		net0_r2_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net0_r2_if2.setLatency("0.0");
		net0_r2_if2.createFluidQueue();
		
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
		case ALGORITHMIC:
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			net1 = net01.createNet("net1"); 
			//no op
			break;
		case SHORTEST_PATH_L123:
			net1 = net01.createNet("net1"); 
			net1.createShortestPath();
			break;
		case BGP_SHORTEST_PATH:
			net1 = top.createNet("net1"); 
			net1.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		
		IRouter net1_r0 = net1.createRouter("r0");
		IInterface net1_r0_if0 = net1_r0.createInterface("if0");
		net1_r0_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if0.setLatency("0.0");
		net1_r0_if0.createFluidQueue();
		IInterface net1_r0_if1 = net1_r0.createInterface("if1");
		net1_r0_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if1.setLatency("0.0");
		net1_r0_if1.createFluidQueue();
		IInterface net1_r0_if2 = net1_r0.createInterface("if2");
		net1_r0_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if2.setLatency("0.0");
		net1_r0_if2.createFluidQueue();
		IInterface net1_r0_if3 = net1_r0.createInterface("if3");
		net1_r0_if3.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r0_if3.setLatency("0.0");
		net1_r0_if3.createFluidQueue();
		
		IRouter net1_r1 = net1.createRouter("r1");
		IInterface net1_r1_if0 = net1_r1.createInterface("if0");
		net1_r1_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r1_if0.setLatency("0.0");
		net1_r1_if0.createFluidQueue();
		IInterface net1_r1_if1 = net1_r1.createInterface("if1");
		net1_r1_if1.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r1_if1.setLatency("0.0");
		net1_r1_if1.createFluidQueue();
		IInterface net1_r1_if2 = net1_r1.createInterface("if2");
		net1_r1_if2.setBitRate("2000000000"); //as2as, 2Gbs
		net1_r1_if2.setLatency("0.0");
		net1_r1_if2.createFluidQueue();
		
		IHost net1_h2 = net1.createHost("h2");
		IInterface net1_h2_if0 = net1_h2.createInterface("if0");
		net1_h2_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h2_if0.setLatency("0.0");
		net1_h2_if0.createFluidQueue();
		
		IHost net1_h3 = net1.createHost("h3");
		IInterface net1_h3_if0 = net1_h3.createInterface("if0");
		net1_h3_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h3_if0.setLatency("0.0");
		net1_h3_if0.createFluidQueue();
		
		IHost net1_h4 = net1.createHost("h4");
		IInterface net1_h4_if0 = net1_h4.createInterface("if0");
		net1_h4_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h4_if0.setLatency("0.0");
		net1_h4_if0.createFluidQueue();
		
		IHost net1_h5 = net1.createHost("h5");
		IInterface net1_h5_if0 = net1_h5.createInterface("if0");
		net1_h5_if0.setBitRate("2000000000"); //as2as, 2Gbs
		net1_h5_if0.setLatency("0.0");
		net1_h5_if0.createFluidQueue();
		
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
		case ALGORITHMIC:
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
		case SHORTEST_PATH_L123:
		{
			//routers in net01
			IRouter net01_r4 = net01.createRouter("r4");
			net01_r4_if0 = net01_r4.createInterface("if0");
			net01_r4_if0.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r4_if0.setLatency("0.0");
			net01_r4_if0.createFluidQueue();
			net01_r4_if1 = net01_r4.createInterface("if1");
			net01_r4_if1.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r4_if1.setLatency("0.0");
			net01_r4_if1.createFluidQueue();
			net01_r4_if2 = net01_r4.createInterface("if2");
			net01_r4_if2.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r4_if2.setLatency("0.0");
			net01_r4_if2.createFluidQueue();
			net01_r4_if3 = net01_r4.createInterface("if3");
			net01_r4_if3.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r4_if3.setLatency("0.0");
			net01_r4_if3.createFluidQueue();
			
			IRouter net01_r5 = net01.createRouter("r5");	
			net01_r5_if0 = net01_r5.createInterface("if0");
			net01_r5_if0.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r5_if0.setLatency("0.0");
			net01_r5_if0.createFluidQueue();
			net01_r5_if1 = net01_r5.createInterface("if1");
			net01_r5_if1.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r5_if1.setLatency("0.0");
			net01_r5_if1.createFluidQueue();
			net01_r5_if2 = net01_r5.createInterface("if2");
			net01_r5_if2.setBitRate("1000000000"); //r2r, 1Gbs
			net01_r5_if2.setLatency("0.0");
			net01_r5_if2.createFluidQueue();
			net01_r5_if3 = net01_r5.createInterface("if3");
			net01_r5_if3.setBitRate("2000000000"); //as2as, 2Gbs
			net01_r5_if3.setLatency("0.0");
			net01_r5_if3.createFluidQueue();
			
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
		case BGP_SHORTEST_PATH:
			//routers in net01
			IRouter net01_r4 = top.createRouter("r4");	
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
			
			IRouter net01_r5 = top.createRouter("r5");	
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
			net01_r4_r5 = top.createLink("r4_r5");
			net01_r4_r5.createInterface(net01_r4_if1);
			net01_r4_r5.createInterface(net01_r5_if3);
			net01_r4_r5.setDelay((float)0.005);
			
			//links from backbone net 0 to routers 4, 5, net1_r0
			net01_net0_r4 = top.createLink("net0_r4");
			net01_net0_r4.createInterface(net0_r0_if2);
			net01_net0_r4.createInterface(net01_r4_if0);
			net01_net0_r4.setDelay((float)0.005);
			
			net01_net0_r5 = top.createLink("net0_r5");
			net01_net0_r5.createInterface(net0_r1_if1);
			net01_net0_r5.createInterface(net01_r5_if0);
			net01_net0_r5.setDelay((float)0.005);
			
			net01_net0_net1 = top.createLink("net0_net1");
			net01_net0_net1.createInterface(net0_r2_if0);
			net01_net0_net1.createInterface(net1_r0_if3);
			net01_net0_net1.setDelay((float)0.005);
			
			//net01:r4 --> net01:r5 have P2P link
			bgpRouting.createLinkType((Link)net01_r4_r5, BGPRelationShipType.P2P, true);
			
			//net01:net0:r0 <--> net01:r4 have C2P link
			bgpRouting.createLinkType((Link)net01_net0_r4, BGPRelationShipType.C2P, true);
			
			//net01:net0:r1 --> net01:r5 have P2C link
			bgpRouting.createLinkType((Link)net01_net0_r5, BGPRelationShipType.P2C, true);
			
			//net01:net1:r2 <--> net01:net0:r1 have P2C link
			bgpRouting.createLinkType((Link)net01_net0_net1, BGPRelationShipType.P2C, true);
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		
		//net2 includes seven routers and seven lans
		INet net2 = top.createNet("net2"); 
		switch(routingType) {
		case ALGORITHMIC:
		case SHORTEST_PATH_L1:
			//no op
			break;
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L123:
		case BGP_SHORTEST_PATH:
			net2.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		IRouter net2_r0 = net2.createRouter("r0");
		IInterface net2_r0_if0 = net2_r0.createInterface("if0");
		net2_r0_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r0_if0.setLatency("0.0");
		net2_r0_if0.createFluidQueue();
		IInterface net2_r0_if1 = net2_r0.createInterface("if1");
		net2_r0_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r0_if1.setLatency("0.0");
		net2_r0_if1.createFluidQueue();
		IInterface net2_r0_if2 = net2_r0.createInterface("if2");
		net2_r0_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r0_if2.setLatency("0.0");
		net2_r0_if2.createFluidQueue();
		
		IRouter net2_r1 = net2.createRouter("r1");
		IInterface net2_r1_if0 = net2_r1.createInterface("if0");
		net2_r1_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r1_if0.setLatency("0.0");
		net2_r1_if0.createFluidQueue();
		IInterface net2_r1_if1 = net2_r1.createInterface("if1");
		net2_r1_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r1_if1.setLatency("0.0");
		net2_r1_if1.createFluidQueue();
		IInterface net2_r1_if2 = net2_r1.createInterface("if2");
		net2_r1_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r1_if2.setLatency("0.0");
		net2_r1_if2.createFluidQueue();
		
		IRouter net2_r2 = net2.createRouter("r2");
		IInterface net2_r2_if0 = net2_r2.createInterface("if0");
		net2_r2_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if0.setLatency("0.0");
		net2_r2_if0.createFluidQueue();
		IInterface net2_r2_if1 = net2_r2.createInterface("if1");
		net2_r2_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if1.setLatency("0.0");
		net2_r2_if1.createFluidQueue();
		IInterface net2_r2_if2 = net2_r2.createInterface("if2");
		net2_r2_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if2.setLatency("0.0");
		net2_r2_if2.createFluidQueue();
		IInterface net2_r2_if3 = net2_r2.createInterface("if3");
		net2_r2_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r2_if3.setLatency("0.0");
		net2_r2_if3.createFluidQueue();
		
		IRouter net2_r3 = net2.createRouter("r3");
		IInterface net2_r3_if0 = net2_r3.createInterface("if0");
		net2_r3_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if0.setLatency("0.0");
		net2_r3_if0.createFluidQueue();
		IInterface net2_r3_if1 = net2_r3.createInterface("if1");
		net2_r3_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if1.setLatency("0.0");
		net2_r3_if1.createFluidQueue();
		IInterface net2_r3_if2 = net2_r3.createInterface("if2");
		net2_r3_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if2.setLatency("0.0");
		net2_r3_if2.createFluidQueue();
		IInterface net2_r3_if3 = net2_r3.createInterface("if3");
		net2_r3_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r3_if3.setLatency("0.0");
		net2_r3_if3.createFluidQueue();
		
		IRouter net2_r4 = net2.createRouter("r4");
		IInterface net2_r4_if0 = net2_r4.createInterface("if0");
		net2_r4_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r4_if0.setLatency("0.0");
		net2_r4_if0.createFluidQueue();
		IInterface net2_r4_if1 = net2_r4.createInterface("if1");
		net2_r4_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r4_if1.setLatency("0.0");
		net2_r4_if1.createFluidQueue();
		
		IRouter net2_r5 = net2.createRouter("r5");
		IInterface net2_r5_if0 = net2_r5.createInterface("if0");
		net2_r5_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r5_if0.setLatency("0.0");
		net2_r5_if0.createFluidQueue();
		IInterface net2_r5_if1 = net2_r5.createInterface("if1");
		net2_r5_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r5_if1.setLatency("0.0");
		net2_r5_if1.createFluidQueue();
		IInterface net2_r5_if2 = net2_r5.createInterface("if2");
		net2_r5_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r5_if2.setLatency("0.0");
		net2_r5_if2.createFluidQueue();
		
		IRouter net2_r6 = net2.createRouter("r6");
		IInterface net2_r6_if0 = net2_r6.createInterface("if0");
		net2_r6_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if0.setLatency("0.0");
		net2_r6_if0.createFluidQueue();
		IInterface net2_r6_if1 = net2_r6.createInterface("if1");
		net2_r6_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if1.setLatency("0.0");
		net2_r6_if1.createFluidQueue();
		IInterface net2_r6_if2 = net2_r6.createInterface("if2");
		net2_r6_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if2.setLatency("0.0");
		net2_r6_if2.createFluidQueue();
		IInterface net2_r6_if3 = net2_r6.createInterface("if3");
		net2_r6_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net2_r6_if3.setLatency("0.0");
		net2_r6_if3.createFluidQueue();
		
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
		
		//the 7 lans in net2
		INet lan_net2[] = new INet[7];
		for(int i=0; i<7; i++){
			lan_net2[i] = net2.createNet("net2_lan"+i);
			createLan(lan_net2[i], routingType);
		}
			
		//connect the 7 lans in net2 with the routers in net2
		ILink net2_lan0_r2 = net2.createLink("lan0_r2");
		net2_lan0_r2.setDelay((float)0.005); //r2r
		net2_lan0_r2.createInterface((IInterface)lan_net2[0].getChildByName("r0").getChildByName("if0"));
		net2_lan0_r2.createInterface(net2_r2_if3);
		
		ILink net2_lan1_r4 = net2.createLink("lan1_r4");
		net2_lan1_r4.setDelay((float)0.005); //r2r
		net2_lan1_r4.createInterface((IInterface)lan_net2[1].getChildByName("r0").getChildByName("if0"));
		net2_lan1_r4.createInterface(net2_r4_if1);

		ILink net2_lan2_r3 = net2.createLink("lan2_r3");
		net2_lan2_r3.setDelay((float)0.005); //r2r
		net2_lan2_r3.createInterface((IInterface)lan_net2[2].getChildByName("r0").getChildByName("if0"));
		net2_lan2_r3.createInterface(net2_r3_if1);

		ILink net2_lan3_r5 = net2.createLink("lan3_r5");
		net2_lan3_r5.setDelay((float)0.005); //r2r
		net2_lan3_r5.createInterface((IInterface)lan_net2[3].getChildByName("r0").getChildByName("if0"));
		net2_lan3_r5.createInterface(net2_r5_if1);		
		
		ILink net2_lan4_r6 = net2.createLink("lan4_r6");
		net2_lan4_r6.setDelay((float)0.005); //r2r
		net2_lan4_r6.createInterface((IInterface)lan_net2[4].getChildByName("r0").getChildByName("if0"));
		net2_lan4_r6.createInterface(net2_r6_if3);
		
		ILink net2_lan5_r6 = net2.createLink("lan5_r6");
		net2_lan5_r6.setDelay((float)0.005); //r2r
		net2_lan5_r6.createInterface((IInterface)lan_net2[5].getChildByName("r0").getChildByName("if0"));
		net2_lan5_r6.createInterface(net2_r6_if2);
		
		ILink net2_lan6_r6 = net2.createLink("lan6_r6");
		net2_lan6_r6.setDelay((float)0.005); //r2r
		net2_lan6_r6.createInterface((IInterface)lan_net2[6].getChildByName("r0").getChildByName("if0"));
		net2_lan6_r6.createInterface(net2_r6_if1);
		//end network2
		
		//net3 includes 4 routers and 5 lans
		INet net3 = top.createNet("net3");
		switch(routingType) {
		case ALGORITHMIC:
		case SHORTEST_PATH_L1:
			//no op
			break;
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L123:
		case BGP_SHORTEST_PATH:
			net3.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		IRouter net3_r0 = net3.createRouter("r0");
		IInterface net3_r0_if0 = net3_r0.createInterface("if0");
		net3_r0_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if0.setLatency("0.0");
		net3_r0_if0.createFluidQueue();
		IInterface net3_r0_if1 = net3_r0.createInterface("if1");
		net3_r0_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if1.setLatency("0.0");
		net3_r0_if1.createFluidQueue();
		IInterface net3_r0_if2 = net3_r0.createInterface("if2");
		net3_r0_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if2.setLatency("0.0");
		net3_r0_if2.createFluidQueue();
		IInterface net3_r0_if3 = net3_r0.createInterface("if3");
		net3_r0_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r0_if3.setLatency("0.0");
		net3_r0_if3.createFluidQueue();
		
		IRouter net3_r1 = net3.createRouter("r1");
		IInterface net3_r1_if0 = net3_r1.createInterface("if0");
		net3_r1_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if0.setLatency("0.0");
		net3_r1_if0.createFluidQueue();
		IInterface net3_r1_if1 = net3_r1.createInterface("if1");
		net3_r1_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if1.setLatency("0.0");
		net3_r1_if1.createFluidQueue();
		IInterface net3_r1_if2 = net3_r1.createInterface("if2");
		net3_r1_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if2.setLatency("0.0");
		net3_r1_if2.createFluidQueue();
		IInterface net3_r1_if3 = net3_r1.createInterface("if3");
		net3_r1_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r1_if3.setLatency("0.0");
		net3_r1_if3.createFluidQueue();
		
		IRouter net3_r2 = net3.createRouter("r2");
		IInterface net3_r2_if0 = net3_r2.createInterface("if0");
		net3_r2_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r2_if0.setLatency("0.0");
		net3_r2_if0.createFluidQueue();
		IInterface net3_r2_if1 = net3_r2.createInterface("if1");
		net3_r2_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r2_if1.setLatency("0.0");
		net3_r2_if1.createFluidQueue();
		IInterface net3_r2_if2 = net3_r2.createInterface("if2");
		net3_r2_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r2_if2.setLatency("0.0");
		net3_r2_if2.createFluidQueue();

		IRouter net3_r3 = net3.createRouter("r3");
		IInterface net3_r3_if0 = net3_r3.createInterface("if0");
		net3_r3_if0.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if0.setLatency("0.0");
		net3_r3_if0.createFluidQueue();
		IInterface net3_r3_if1 = net3_r3.createInterface("if1");
		net3_r3_if1.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if1.setLatency("0.0");
		net3_r3_if1.createFluidQueue();
		IInterface net3_r3_if2 = net3_r3.createInterface("if2");
		net3_r3_if2.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if2.setLatency("0.0");
		net3_r3_if2.createFluidQueue();
		IInterface net3_r3_if3 = net3_r3.createInterface("if3");
		net3_r3_if3.setBitRate("1000000000"); //r2r, 1Gbs
		net3_r3_if3.setLatency("0.0");
		net3_r3_if3.createFluidQueue();
		
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
		
		//the 5 lans in net2
		INet lan_net3[] = new INet[5];
		for(int i=0; i<5; i++){
			lan_net3[i] = net3.createNet("net3_lan"+i);
			createLan(lan_net3[i], routingType);
		}
		
		//connect the 5 lans in net3 with the routers in net3
		ILink net3_lan0_r0 = net3.createLink("lan0_r0");
		net3_lan0_r0.setDelay((float)0.005); //r2r
		net3_lan0_r0.createInterface((IInterface)lan_net3[0].getChildByName("r0").getChildByName("if0"));
		net3_lan0_r0.createInterface(net3_r0_if3);
		
		ILink net3_lan1_r0 = net3.createLink("lan1_r0");
		net3_lan1_r0.setDelay((float)0.005); //r2r
		net3_lan1_r0.createInterface((IInterface)lan_net3[1].getChildByName("r0").getChildByName("if0"));
		net3_lan1_r0.createInterface(net3_r0_if2);
		
		ILink net3_lan2_r2 = net3.createLink("lan2_r2");
		net3_lan2_r2.setDelay((float)0.005); //r2r
		net3_lan2_r2.createInterface((IInterface)lan_net3[2].getChildByName("r0").getChildByName("if0"));
		net3_lan2_r2.createInterface(net3_r2_if2);
		
		ILink net3_lan3_r3 = net3.createLink("lan3_r3");
		net3_lan3_r3.setDelay((float)0.005); //r2r
		net3_lan3_r3.createInterface((IInterface)lan_net3[3].getChildByName("r0").getChildByName("if0"));
		net3_lan3_r3.createInterface(net3_r3_if2);
		
		ILink net3_lan4_r3 = net3.createLink("lan4_r3");
		net3_lan4_r3.setDelay((float)0.005); //r2r
		net3_lan4_r3.createInterface((IInterface)lan_net3[4].getChildByName("r0").getChildByName("if0"));
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
		

		switch(routingType) {
		case ALGORITHMIC:
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
		case SHORTEST_PATH_L123:
			//no op
			break;
		case BGP_SHORTEST_PATH:
			//net01:r4 <--> net2:r0 have P2P link
			bgpRouting.createLinkType((Link)r4_net2_1, BGPRelationShipType.P2P, true);
			
			//net01:r4 <--> net2:r1 have C2P link
			bgpRouting.createLinkType((Link)r4_net2_2, BGPRelationShipType.C2P, true);
			
			//net01:r5 <--> net3:r0 have P2C link
			bgpRouting.createLinkType((Link)r5_net3_1, BGPRelationShipType.P2C, true);
			
			//net01:r5 <--> net3:r1 have C2P link
			bgpRouting.createLinkType((Link)r5_net3_2, BGPRelationShipType.C2P, true);
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}
		
		return (IRouter)top.get("net01.net0.r0");
	}
	
	public IRouter createLan(final INet lan, final RoutingType routingType) {
		IRouter rv = null;
		switch(routingType) {
		case ALGORITHMIC:
		case SHORTEST_PATH_L12:
		case SHORTEST_PATH_L1:
			//no op
			break;
		case SHORTEST_PATH_L123:
		case BGP_SHORTEST_PATH:
			lan.createShortestPath();
			break;
		default:
			throw new RuntimeException("Unknown routing type "+routingType);
		}

		IRouter lan_r0 = lan.createRouter("r0");
		IInterface lan_r0_if0 = lan_r0.createInterface("if0");
		lan_r0_if0.setBitRate("1000000000"); //r2r, 1Gbs
		lan_r0_if0.setLatency("0.0");
		lan_r0_if0.createFluidQueue();
		IInterface lan_r0_if3 = lan_r0.createInterface("if3");
		lan_r0_if3.setBitRate("100000000"); //lan, 100Mbs
		lan_r0_if3.setLatency("0.0");
		lan_r0_if3.createFluidQueue();
		IInterface lan_r0_if4 = lan_r0.createInterface("if4");
		lan_r0_if4.setBitRate("100000000"); //lan, 100Mbs
		lan_r0_if4.setLatency("0.0");
		lan_r0_if4.createFluidQueue();
		IInterface lan_r0_if5 = lan_r0.createInterface("if5");
		lan_r0_if5.setBitRate("100000000"); //lan, 100Mbs
		lan_r0_if5.setLatency("0.0");
		lan_r0_if5.createFluidQueue();
		IInterface lan_r0_if6 = lan_r0.createInterface("if6");
		lan_r0_if6.setBitRate("100000000"); //lan, 100Mbs
		lan_r0_if6.setLatency("0.0");
		lan_r0_if6.createFluidQueue();

		IHost h[] = new IHost[43];
		IInterface iface[] = new IInterface[43];
		
		for(int i=1 ; i<43 ; i++){
			h[i] = lan.createHost("h"+i);
			iface[i] = h[i].createInterface("if0");
			iface[i].setBitRate("100000000"); //lan, 100Mbs
			iface[i].setLatency("0.0");
			iface[i].createFluidQueue();
		}

		//links in lan 
		ILink lan_l1 = lan.createLink("l1");
		lan_l1.setDelay((float)0.001); //lan
		lan_l1.createInterface(lan_r0_if3);
		for(int i=1; i<11; i++){
			lan_l1.createInterface((IInterface)h[i].getChildByName("if0"));
		}
		
		ILink lan_l2 = lan.createLink("l2");
		lan_l2.setDelay((float)0.001); //lan
		lan_l2.createInterface(lan_r0_if4);
		for(int i=11; i<21; i++){
			lan_l2.createInterface((IInterface)h[i].getChildByName("if0"));
		}
		
		ILink lan_l3 = lan.createLink("l3");
		lan_l3.setDelay((float)0.001); //lan
		lan_l3.createInterface(lan_r0_if5);
		for(int i=21; i<31; i++){
			lan_l3.createInterface((IInterface)h[i].getChildByName("if0"));
		}
		
		ILink lan_l4 = lan.createLink("l4");
		lan_l4.setDelay((float)0.001); //lan
		lan_l4.createInterface(lan_r0_if6);	
		for(int i=31;i<43;i++) {
			lan_l4.createInterface((IInterface)h[i].getChildByName("if0"));
		}
       	return rv;
	}
}
