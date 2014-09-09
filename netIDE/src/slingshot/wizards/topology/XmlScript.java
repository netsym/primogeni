package slingshot.wizards.topology;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.JTextArea;

import slingshot.wizards.topology.XmlNode.XmlNodeType;

/**
 * This class is use to translate generator output file into xml models
 * @author Hao Jiang
 */

public class XmlScript 
{
	private String inputfile;
	//private String outputfile;
	
	public XmlTree tree;
	public JTextArea slog;
	
	static final String NODE = "node";
	static final String REPLICA = "replica";
	static final String REF = "ref";
	static final String ATTRIBUTE = "attribute";
	
	static final String NODE_TYPE_NET = "Net";
	static final String NODE_TYPE_HOST = "Host";
	static final String NODE_TYPE_ROUTER = "Router";
	static final String NODE_TYPE_INTERFACE = "Interface";
	static final String NODE_TYPE_LINK = "Link";
	
	static final String TYPE = "type";
	static final String NAME = "name";
	static final String PATH = "path";
	static final String VALUE = "value";
	
	static final String BITRATE = "bit_rate";
	static final String LATENCY = "latency";
	
	static final String BANDWIDTH = "bandwidth";
	static final String DELAY = "delay";
	
	static final String TOPNET = "topnet";
	static final String NET = "sub";
	static final String INTERFACE = "if";
	static final String HOST = "h";
	static final String ROUTER = "r";
	
	static final String ROUTERBORDER = "RT_BORDER";
	
	static final String ROUTING = "routing";
	static final String SHORTESTPATH = "ShortestPath";
	
	static final int HS = 1000;
	
	//topology type
	public static enum TopologyType 
	{
		AS_LEVEL,
		ROUTER_LEVEL,
		HIERARCHY;
	}
	//ways to assign bandwidth for links
	public static enum BWAssginType
	{
		CONSTANT,
		UNIFORM,
		HEAVYTAILED,
		EXPONENTIAL;
	}
	
	public XmlScript(String inputfile, JTextArea text)
	{
		File file = new File(inputfile);
		if (file.exists())
		{
			this.inputfile = inputfile;
			//this.outputfile = outputfile;
			this.slog = text;
			tree = new XmlTree();
		}
		else
		{
			inputfile = null;
			//outputfile = null;
			this.slog = null;
			tree = null;
		}
	}
	
	public void CreateTopNet()
	{	
		XmlNode top = new XmlNode(XmlNodeType.NODE, TOPNET);
		top.setAttribute(TYPE, NODE_TYPE_NET);
		top.level = 0;
		
		CreateRouting(top);
		
		this.tree.setRoot(top);
	}
	
	public void CreateRouting(XmlNode parent)
	{
		if (parent == null)
			return;
		
		XmlNode routing = new XmlNode(XmlNodeType.NODE, ROUTING);
		routing.setAttribute(TYPE, SHORTESTPATH);
		
		parent.addChild(routing);
	}
		
	public XmlNode CreateNet(XmlNode parent, String name)
	{
		if (parent == null || name == null)
			return null;
		
		XmlNode net = new XmlNode(XmlNodeType.NODE, name);
		net.setAttribute(TYPE, NODE_TYPE_NET);
		parent.addChild(net);
		tree.addNet(net);
		CreateRouting(net);	
		return net;
	}
	
	public XmlNode CreateHost(XmlNode parent, String name)
	{
		if (parent == null && name == null)
			return null;
		
		XmlNode host = new XmlNode(XmlNodeType.NODE, name);
		host.setAttribute(TYPE, NODE_TYPE_HOST);
		
		parent.addChild(host);
		return host;
	}
	
	public XmlNode CreateRouter(XmlNode parent, String name)
	{
		if (parent == null && name == null)
			return null;
		
		XmlNode router = new XmlNode(XmlNodeType.NODE, name);
		router.setAttribute(TYPE, NODE_TYPE_ROUTER);
		
		parent.addChild(router);
		tree.addRouter(router);
		
		return router;
	}
	
	public XmlNode CreateInterface(XmlNode parent, String name, 
								float latency, float bit_rate)
	{
		if (parent == null)
			return null;
		
		XmlNode ifelem = new XmlNode(XmlNodeType.NODE, name);
		ifelem.setAttribute(TYPE, NODE_TYPE_INTERFACE);
		parent.addChild(ifelem);
		
		DecimalFormat df = new DecimalFormat("########");
		XmlNode bitratelem = new XmlNode(XmlNodeType.ATTR, BITRATE);
		bitratelem.setAttribute(VALUE, df.format(bit_rate));
			
		ifelem.addChild(bitratelem);
	
		df = new DecimalFormat("###.#####");
		XmlNode latencyelem = new XmlNode(XmlNodeType.ATTR, LATENCY);
		latencyelem.setAttribute(VALUE, df.format(latency));
		
		ifelem.addChild(latencyelem);
		
		return ifelem;
		
	}
	
	public XmlNode CreateLan(XmlNode parent,                          //subnet to add the lan net work 
							 XmlNode router,                          //router to attach the lan net work
							 XmlNode replica,                         //replica AS node(null if there is no replica)
							 String name,                             //lan network name
						  	 int [] hnum,                             //host num for each router
						  	 float rtlatency, float rtbit_rate,      
						  	 float hlatency, float hbit_rate)
	{
		if (router == null || parent == null)
			return null;
		
		int i;
		int hostnum = 0;
		for (i = 0; i < hnum.length; i++)
		{
			hostnum += hnum[i];
			if (hnum[i] <= 0)
				return null;
		}
		
		List<XmlNode> routers = new ArrayList<XmlNode>();
		
		XmlNode lan = CreateNet(parent, name);
		
		//create hosts in lan net
		//XmlNode host0 = this.CreateHost(lan, HOST + "0");
		//XmlNode ifhost = CreateInterface(host0, INTERFACE + "_" + ROUTER, hlatency, hbit_rate);
		//for (i = 1; i < hostnum; i++)
			//this.CreateReplica(lan, host0, HOST + i);
		for (i = 0; i < hostnum; i++)
		{
			XmlNode host = CreateHost(lan, HOST + String.valueOf(i));
			CreateInterface(host, INTERFACE + "_" + ROUTER, hlatency, hbit_rate);
		}
		
		int hid = 0;
		//create routers in lan net
		XmlNode rt;
		for (i = 0; i < hnum.length; i++)
		{
			rt = new XmlNode(XmlNodeType.NODE, ROUTER + i);;
			rt.setAttribute(TYPE, NODE_TYPE_ROUTER);
			lan.addChild(rt);
			
			routers.add(rt);
			
			for (int j = 0; j < hnum[i]; j++)
			{
				//CreateReplica(rt, ifhost, INTERFACE + "_" + HOST + hid);
				CreateInterface(rt, INTERFACE + "_" + HOST + hid, hlatency, hbit_rate);
				
				//create link between hosts and router
				XmlNode link = new XmlNode(XmlNodeType.NODE, ROUTER + i + "_" + HOST + hid);
				link.setAttribute(TYPE, NODE_TYPE_LINK);
				lan.addChild(link);
				
				XmlNode bitratelem = new XmlNode(XmlNodeType.ATTR, BANDWIDTH);
				DecimalFormat df = new DecimalFormat("#########");
				bitratelem.setAttribute(VALUE, df.format(hbit_rate));
				link.addChild(bitratelem);
				
				XmlNode delayelem = new XmlNode(XmlNodeType.ATTR, DELAY);
				df = new DecimalFormat("#.######");
				delayelem.setAttribute(VALUE, df.format(hlatency));
				link.addChild(delayelem);
				
				String name1 = ROUTER + i;
				String name2 = HOST + hid;
				String path1 = "..:" + ROUTER + i + ":" + INTERFACE + "_" + HOST + hid;
				String path2 = "..:" + HOST + hid + ":" + INTERFACE + "_" + ROUTER;
				
				XmlNode ref1 = new XmlNode(XmlNodeType.REF);
				ref1.setAttribute(NAME, name1);
				ref1.setAttribute(PATH, path1);
					
				XmlNode ref2 = new XmlNode(XmlNodeType.REF);
				ref2.setAttribute(NAME, name2);
				ref2.setAttribute(PATH, path2);
				
				link.addChild(ref1);
				link.addChild(ref2);
				
				hid++;
			}
		}
		
		XmlNode r = new XmlNode(XmlNodeType.NODE, "r");
		r.setAttribute(TYPE, NODE_TYPE_ROUTER);
		lan.addChild(r);
		
		XmlNode ifr = CreateInterface(r, INTERFACE + "_" + ROUTER, rtlatency, rtbit_rate);
		XmlNode ifrouter = CreateInterface(router, INTERFACE + "_" + name + "_" + ROUTER, rtlatency, rtbit_rate);
	    //CreateReplica(router, ifr, INTERFACE + "_" + name + "_" + ROUTER);
        if (replica != null)
        	CreateReplicaLink(parent, replica ,ifr, ifrouter,
				              name + "_" + ROUTER + "_" + router.name,
				              name + "_" + ROUTER, router.name, rtlatency, rtbit_rate);
        else
		    this.CreateLink(parent, ifr, ifrouter,
						    name + "_" + ROUTER + "_" + router.name,
						    name + "_" + ROUTER, router.name, rtlatency, rtbit_rate);
		
		//connect each routers in lan
		for (i = 0; i < routers.size(); i++)
		{
			for (int j = i + 1; j < routers.size(); j ++)
			{
				XmlNode rt1 = routers.get(i);
				XmlNode rt2 = routers.get(j);
			
				//XmlNode if1 = CreateReplica(rt1, ifr, INTERFACE + "_" + ROUTER + j);
				XmlNode if1 = CreateInterface(rt1, INTERFACE + "_" + ROUTER + j, rtlatency, rtbit_rate);
				//XmlNode if2 = CreateReplica(rt2, ifr, INTERFACE + "_" + ROUTER + i);
				XmlNode if2 = CreateInterface(rt2, INTERFACE + "_" + ROUTER + i, rtlatency, rtbit_rate);
			
				this.CreateLink(lan, if1, if2, 
					        	ROUTER + i + "_" + ROUTER + j, 
					        	ROUTER + i, ROUTER + j, rtlatency, rtbit_rate);
			}
		}

		//connect lan router with parent router		
		for (i = 0; i < routers.size(); i++)
		{
			rt = routers.get(i);
			
			//XmlNode if1 = CreateReplica(rt, ifr, INTERFACE + "_" + ROUTER);
			XmlNode if1 = CreateInterface(rt, INTERFACE + "_" + ROUTER, rtlatency, rtbit_rate);
			//XmlNode if2 = CreateReplica(r, ifr, INTERFACE + "_" + ROUTER + i);
		    XmlNode if2 = CreateInterface(r, INTERFACE + "_" + ROUTER + i, rtlatency, rtbit_rate);
			this.CreateLink(lan, if1, if2, 
							ROUTER + i + "_" + ROUTER, 
							ROUTER + i, ROUTER, rtlatency, rtbit_rate);
			
		}
		
		return lan;
	}
	
	public void CreateReplicaLan(XmlNode parent, XmlNode router, XmlNode node, 
								 String name, float latency, float bit_rate)
	{
		if (parent == null || router == null || node == null)
			return;
		
		XmlNode replicalan = CreateReplica(parent, node, name);
		
		XmlNode if1 = CreateInterface(router, INTERFACE + "_" + name + "_" + ROUTER, 
								      latency, bit_rate);
		
		//create link between hosts and router
		XmlNode link = new XmlNode(XmlNodeType.NODE, router.name + "_" + name + "_" + ROUTER);
		link.setAttribute(TYPE, NODE_TYPE_LINK);
		parent.addChild(link);
		
		XmlNode bitratelem = new XmlNode(XmlNodeType.ATTR, BANDWIDTH);
		DecimalFormat df = new DecimalFormat("#########");
		bitratelem.setAttribute(VALUE, df.format(bit_rate));
		link.addChild(bitratelem);
		
		XmlNode delayelem = new XmlNode(XmlNodeType.ATTR, DELAY);
		df = new DecimalFormat("#.######");
		delayelem.setAttribute(VALUE, df.format(latency));
		link.addChild(delayelem);
		
		String name1 = router.name;
		String name2 = name + "_" + ROUTER;
		
		XmlNode ref1 = new XmlNode(XmlNodeType.REF, name1);
		XmlNode ref2 = new XmlNode(XmlNodeType.REF, name2);
		link.addChild(ref1);
		link.addChild(ref2);
		
		//String path1 = "..:" + router.name + ":" + INTERFACE + "_" + name + "_" + ROUTER;
		//String path2 = "..:" + name + ":" + ROUTER + ":"+ INTERFACE + "_" + ROUTER;
		
		String path1 = tree.getPath(ref1, if1);
		String path2 = tree.getPath(ref2, replicalan);
		path2 += ":" + ROUTER + ":"+ INTERFACE + "_" + ROUTER;

		ref1.setAttribute(PATH, path1);
		ref2.setAttribute(PATH, path2);
		
		//tree.addReplica(replicalan);
	}
	
	public void CreateReplicaNet(XmlNode parent, XmlNode node, String name)
	{
		XmlNode replicanode =  CreateReplica(parent, node, name);
		tree.addReplica(replicanode);
		tree.addASReplica(node, replicanode);
	}
	
	public XmlNode CreateReplica(XmlNode parent, XmlNode node, String name)
	{
		if (parent == null || node == null || name == null)
			return null;
		
		XmlNode replicanode = new XmlNode(XmlNodeType.REPLICA, name);
		parent.addChild(replicanode);
		
		String path = tree.getPath(replicanode, node);
		replicanode.setAttribute(PATH, path);
		
		//tree.addReplica(replicanode);
		
		return replicanode;
	}
	
	
	
	public void CreateReplicaLink(XmlNode parent, XmlNode replica, 
			                      XmlNode from, XmlNode to, 				           //interface nodes
			                      String linkname, String ref1name, String ref2name,  
			                      float delay, float bandwidth)
	{
		if (parent == null || from == null || to == null)
		return;
		
		XmlNode link = new XmlNode(XmlNodeType.NODE, linkname);
		link.setAttribute(TYPE, NODE_TYPE_LINK);
		parent.addChild(link);
		
		XmlNode bitratelem = new XmlNode(XmlNodeType.ATTR, BANDWIDTH);
		DecimalFormat df = new DecimalFormat("########");
		bitratelem.setAttribute(VALUE, df.format(bandwidth));
		link.addChild(bitratelem);
		
		XmlNode delayelem = new XmlNode(XmlNodeType.ATTR, DELAY);
		df = new DecimalFormat("#.#####");
		delayelem.setAttribute(VALUE, df.format(delay));
		link.addChild(delayelem);
		
		XmlNode ref1 = new XmlNode(XmlNodeType.REF, ref1name);
		XmlNode ref2 = new XmlNode(XmlNodeType.REF, ref2name);
		link.addChild(ref1);
		link.addChild(ref2);
		
		String path1 = tree.getPath(ref1, from);		
		String path2 = tree.getPath(ref2, replica);
		path2 += ":" + to.parent.name + ":" + to.name;
		
		ref1.setAttribute(PATH, path1);
		ref2.setAttribute(PATH, path2);
		
		tree.addLink(link);
	}
	
	public void CreateLink(XmlNode parent, 
						   XmlNode from, XmlNode to, 				           //interface nodes
						   String linkname, String ref1name, String ref2name,  
						   float delay, float bandwidth)
	{
		if (parent == null || from == null || to == null)
			return;
		
		XmlNode link = new XmlNode(XmlNodeType.NODE, linkname);
		link.setAttribute(TYPE, NODE_TYPE_LINK);
		parent.addChild(link);
		
		XmlNode bitratelem = new XmlNode(XmlNodeType.ATTR, BANDWIDTH);
		DecimalFormat df = new DecimalFormat("########");
		bitratelem.setAttribute(VALUE, df.format(bandwidth));
		link.addChild(bitratelem);
		
		XmlNode delayelem = new XmlNode(XmlNodeType.ATTR, DELAY);
		df = new DecimalFormat("#.#####");
		delayelem.setAttribute(VALUE, df.format(delay));
		link.addChild(delayelem);
		
		XmlNode ref1 = new XmlNode(XmlNodeType.REF, ref1name);
		XmlNode ref2 = new XmlNode(XmlNodeType.REF, ref2name);
		link.addChild(ref1);
		link.addChild(ref2);
		
		String path1 = tree.getPath(ref1, from);
		String path2 = tree.getPath(ref2, to);
		
		ref1.setAttribute(PATH, path1);
		ref2.setAttribute(PATH, path2);
		
		tree.addLink(link);
	}
	
	public void CreateLink(String ASfrom, String ASto, 
						   String nodefrom, String nodeto,
						   float delay, float bandwidth,
						   String edgeid,
						   Map<String, String> borderRTMap)
	{
		XmlNode parent;
		if (ASfrom.equals(ASto))
			parent = this.tree.getNet(TOPNET + ":" + NET + ASfrom);
		else
			parent = this.tree.getRoot();
		
		if (parent != null)
		{
			XmlNode linkelem = new XmlNode(XmlNodeType.NODE);
			
			if (ASfrom.equals(ASto))
				linkelem.setAttribute(NAME, ROUTER + nodefrom + "_" + ROUTER + nodeto + "_" + edgeid);
			else
				linkelem.setAttribute(NAME, NET + ASfrom + "_" + NET + ASto + "_" + edgeid);
			linkelem.setAttribute(TYPE, NODE_TYPE_LINK);
			
			parent.addChild(linkelem);
			
			XmlNode bitratelem = new XmlNode(XmlNodeType.ATTR, BANDWIDTH);
			DecimalFormat df = new DecimalFormat("########");
			bitratelem.setAttribute(VALUE, df.format(bandwidth));
			linkelem.addChild(bitratelem);
			
			XmlNode delayelem = new XmlNode(XmlNodeType.ATTR, DELAY);
			df = new DecimalFormat("#.#####");
			delayelem.setAttribute(VALUE, df.format(delay));
			linkelem.addChild(delayelem);
		
			String rnodefrom, rnodeto;
			String tmpstr;
			String path1, path2;
			String name1, name2;
			
			if (ASfrom.equals(ASto))
			{
				path1 = "..:" + ROUTER + nodefrom + ":"
						+ INTERFACE + edgeid + "_" + ROUTER + nodeto;
				path2 = "..:" + ROUTER + nodeto + ":"
						+ INTERFACE + edgeid + "_" + ROUTER + nodefrom;
				
				name1 = ROUTER + nodefrom;
				name2 = ROUTER + nodeto;
			}
			else
			{
				tmpstr = borderRTMap.get(nodefrom);
				if (tmpstr != null)
				{
					//rASfrom = ASreplicaMap.get(ASfrom);
					rnodefrom = tmpstr;
					path1 = "..:" + NET + ASfrom + ":" + ROUTER + rnodefrom + ":"
						  + INTERFACE + edgeid + "_" + NET + ASto + "_" + ROUTER + nodeto;
				}
				else
					path1 = "..:" + NET + ASfrom + ":" + ROUTER + nodefrom + ":"
						  + INTERFACE + edgeid + "_" + NET + ASto + "_" + ROUTER + nodeto;
				
				tmpstr = borderRTMap.get(nodeto);
				if (tmpstr != null)
				{
					//rASto = ASreplicaMap.get(ASto);
					rnodeto = tmpstr;
					path2 = "..:" + NET + ASto + ":" + ROUTER + rnodeto + ":"
						+ INTERFACE + edgeid + "_" + NET + ASfrom + "_" + ROUTER + nodefrom;
				}
				else
					path2 = "..:" + NET + ASto + ":" + ROUTER + nodeto + ":"
						+ INTERFACE + edgeid + "_" + NET + ASfrom + "_" + ROUTER + nodefrom;
				
				name1 = NET + ASfrom;
				name2 = NET + ASto;
			}
			
			XmlNode ref1 = new XmlNode(XmlNodeType.REF);
			ref1.setAttribute(NAME, name1);
			ref1.setAttribute(PATH, path1);
				
			XmlNode ref2 = new XmlNode(XmlNodeType.REF);
			ref2.setAttribute(NAME, name2);
			ref2.setAttribute(PATH, path2);
			
			linkelem.addChild(ref1);
			linkelem.addChild(ref2);
			
			tree.addLink(linkelem);
		}
	}
	
	//assign bandwidth for link
	private float AssignBW(float bwmin, float bwmax, BWAssginType type) 
	{
		if (bwmax < 0 || 
			bwmin < 0 ||
			bwmax < bwmin)
			throw new RuntimeException("bwmax bwmin must be greater than 1");
		
		Random BWRandom = new Random(System.currentTimeMillis());
		float bw;
		
		if (type == BWAssginType.CONSTANT)
		{
			bw =  bwmin;
		} 
		else if (type == BWAssginType.UNIFORM) 
		{
			
			bw = (bwmin + bwmax * Distribution.getUniformRandom(BWRandom));
		} 
		else if (type == BWAssginType.HEAVYTAILED) 
		{
			bw = (Distribution.getParetoRandom(BWRandom, bwmin, bwmax, 1.2));
		} 
		else if (type == BWAssginType.EXPONENTIAL) 
		{
			bw = (Distribution.getExponentialRandom(BWRandom, bwmin));
		} 
		else 
		{ // default case
			bw = -1;
		}
		
		return bw;
	}
	
	private float AssignDL(XmlNode n1, XmlNode n2)
	{
		if (n1 == null && n2 == null)
			throw new RuntimeException("Xml node n1 and n2 could not be null");
		
		Coordinate c1 = this.tree.getCoordinate(n1);
		Coordinate c2 = this.tree.getCoordinate(n2);
		
		if (c1 == null && c2 == null)
			throw new RuntimeException("c1 and c2 can not be null");
		
		return (float)c1.distance(c2) / 300000000 * 1000;
	}
	/**
	 * Place nodes of nodeType  onto the plane Does collision checking to 
	 * ensure that two nodes in the plane can never have the same (x,y) cords.
	 */
	private void PlaceNodes(int HS, XmlNode node) 
	{
		if (HS < 1)
			throw new RuntimeException("HS must be greater than 1");
		if (node == null)
			throw new RuntimeException("node can not be null");
		
		Random PlaceRandom = new Random(System.currentTimeMillis());
		
		int x = (int) (Distribution.getUniformRandom(PlaceRandom) * HS);
		int y = (int) (Distribution.getUniformRandom(PlaceRandom) * HS);
		// System.out.print("("+x+", "+y+") ");
        Coordinate c = new Coordinate(x, y);
		/* check for collisions */
		while (true) 
		{
			if (this.tree.ContainsCoordinate(c)) 
			{
				x = (int) (Distribution.getUniformRandom(PlaceRandom) * HS);
				y = (int) (Distribution.getUniformRandom(PlaceRandom) * HS);
				
				c = new Coordinate(x, y);
			} 
			else 
			{
				this.tree.addCoordinate(node, c);
				break;
			}
		}
	}
	
	public void Orbis2Xml(TopologyType Topotype,                               //topology type(AS level or router level)
						  int HS,											   //size of the plane
			              BWAssginType BWtype, float BWmin, float BWmax)       //how to assign capacity for link   
	{
		if (this.inputfile == null)
			throw new RuntimeException("input file could not be null");
		
		try
		{
			if (this.slog != null)
				this.slog.append("Begin translate Inet output file to xml file\n");
			System.out.println("Begin translate Inet output file to xml file");
			
			long startTime = System.currentTimeMillis();
			
			this.CreateTopNet();
			
			FileInputStream fs= new FileInputStream(this.inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		
			String from, to;
			String [] strarray = null;
			String data = null;
			
			int i = 0;
			while((data = br.readLine()) != null)
			{
				i++;
				if (i % 10000 == 0)
					System.out.println("reading line: " + i + " Heap size = " 
							           + (Runtime.getRuntime().totalMemory() 
							           - Runtime.getRuntime().freeMemory()) / 1048576 + "m");
				data.trim();
		
				strarray = data.split(" ");
				from = strarray[0];
				to = strarray[1];
				
				XmlNode node;
				
				XmlNode nodefrom = null, nodeto = null;
				//first time meet the node create AS/Router place the node on the plane
				if (Topotype == TopologyType.AS_LEVEL)
				{
					nodefrom = tree.getRouter(TOPNET + ":" + NET + from + ":" + ROUTER + from);
					//first time meet the AS node
					if (nodefrom == null)
					{
						node = this.CreateNet(this.tree.getRoot(), NET + from);
						nodefrom = this.CreateRouter(node, ROUTER + from);
						
						//place the node on the plane
						if (nodefrom != null)
							this.PlaceNodes(HS, nodefrom);
					}
					
					nodeto = tree.getRouter(TOPNET + ":" + NET + to + ":" + ROUTER + to);
					if (nodeto == null)
					{
						node = this.CreateNet(this.tree.getRoot(), NET + to);
						nodeto = this.CreateRouter(node, ROUTER + to);
						
						//place the node on the plane
						if (nodeto != null)
							this.PlaceNodes(HS, nodeto);
					}
				}
				else if (Topotype == TopologyType.ROUTER_LEVEL)
				{
					nodefrom = tree.getRouter(TOPNET + ":" + ROUTER + from);
					//first time meet the router node
					if (nodefrom == null)
					{
						nodefrom = this.CreateRouter(this.tree.getRoot(), ROUTER + from);
						
						if (nodefrom != null)
							this.PlaceNodes(HS, nodefrom);
					}
					
					nodeto = tree.getRouter(TOPNET + ":" + ROUTER + to);
					if (nodeto == null)
					{
						nodeto = this.CreateRouter(this.tree.getRoot(), ROUTER + to);
						
						if (nodeto != null)
							this.PlaceNodes(HS, nodeto);
					}
				}
							
				//create the interface and link
				if (nodefrom != null && nodeto != null)
				{
					float delay,  bandwidth;
					delay = this.AssignDL(nodefrom, nodeto);
					bandwidth = AssignBW(BWmin, BWmax, BWtype);
					
					XmlNode if1 = null, if2= null;
					if (Topotype == TopologyType.AS_LEVEL)
					{
						if1 = this.CreateInterface(nodefrom, INTERFACE + i + "_" + NET + to, delay, bandwidth);
						if2 = this.CreateInterface(nodeto, INTERFACE + i + "_" + NET + from, delay, bandwidth);
					}
					else if (Topotype == TopologyType.ROUTER_LEVEL)
					{	
						if1 = this.CreateInterface(nodefrom, INTERFACE + i + "_" + ROUTER + to, delay, bandwidth);
						if2 = this.CreateInterface(nodeto, INTERFACE + i +"_" + ROUTER + from, delay, bandwidth);
					}
					
					XmlNode parent = this.tree.getRoot();
					String linkname = "", ref1name = "", ref2name = "";
					if (Topotype == TopologyType.AS_LEVEL)
					{
						linkname = NET + from + "_" + NET + to + "_" + i;
						ref1name = NET + from;
						ref2name = NET + to;
					}
					else if (Topotype == TopologyType.ROUTER_LEVEL)
					{
						linkname = ROUTER + from + "_" + ROUTER + to + "_" + i;
						ref1name = ROUTER + from;
						ref2name = ROUTER + to;
					}
					CreateLink(parent, if1, if2, 
							   linkname, ref1name, ref2name, 
							   delay, bandwidth);
				}			
			}
			
			fs.close();
			br.close();
			/*
			this.tree.WriteXml(this.outputfile);
			this.tree.setRoot(null);
			*/
			System.out.println("End translate Inet file to xml file");
			
			long endTime = System.currentTimeMillis();
			this.slog.append("Totel net nodes: " + tree.getRouterNum() + "\n"); 
			this.slog.append("Totel links: " + i + "\n");
			this.slog.append("Totel translation cost time: " + (endTime-startTime) / 1000 + "s\n");   
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		}
	}

	public void Inet2Xml(BWAssginType BWtype, float BWmin, float BWmax)
	{
		if (this.inputfile == null)
			throw new RuntimeException("input file could not be null");
		
		try
		{
			if (this.slog != null)
				this.slog.append("Begin translate Inet output file to xml file\n");
			System.out.println("Begin translate Inet output file to xml file");
			
			long startTime = System.currentTimeMillis();
			
			this.CreateTopNet();
			
			FileInputStream fs = new FileInputStream(this.inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			
			String ASid, x, y;
			String ASfrom, ASto;
			
			boolean nodes = true;
			
			String [] strarray = null;
			String data = null;
			
			//read first line (node number and line number)
			data = br.readLine();
			data.trim();
			strarray = data.split(" ");
			
			int nodenum = Integer.parseInt(strarray[0]);
			int linknum = Integer.parseInt(strarray[1]);
					
			int i = 0;
			int edgeid = 0;
			
			while((data = br.readLine()) != null)
			{
				i++;
				if (i % 10000 == 0)
					System.out.println("reading line: " + i + " Heap size = " 
							           + (Runtime.getRuntime().totalMemory() 
							           - Runtime.getRuntime().freeMemory()) / 1048576 + "m");
				if (i == nodenum + 1)
				{
					nodes = false;
				}
				
				data.trim();
				
				XmlNode node;
				//reading nodes information
				if (nodes)
				{
					strarray = data.split("\t");
					ASid = strarray[0];
					x = strarray[1];
					y = strarray[0];
					
					node = this.CreateNet(this.tree.getRoot(), NET + ASid);
					this.CreateRouter(node, ROUTER + ASid);
					Coordinate c = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
					
					this.tree.addCoordinate(node, c);
				}
				
				//reading edges information
				if (!nodes)
				{
					edgeid = i - nodenum;
					strarray = data.split("\t");
					ASfrom = strarray[0];
					ASto = strarray[1];
					
					XmlNode nodefrom, nodeto, asfrom, asto;
					asfrom = this.tree.getNet(TOPNET + ":" + NET + ASfrom);
					asto = this.tree.getNet(TOPNET + ":" + NET + ASto);
					nodefrom = this.tree.getRouter(TOPNET + ":" + NET + ASfrom + ":" + ROUTER + ASfrom);
					nodeto = this.tree.getRouter(TOPNET + ":" + NET + ASto + ":" + ROUTER + ASto);
					
					if (nodefrom != null && nodeto != null)
					{
						float delay, bitrate;
						
						delay = AssignDL(asfrom, asto);
						bitrate = AssignBW(BWmin, BWmax, BWtype);
						
						XmlNode if1 = CreateInterface(nodefrom, INTERFACE + edgeid + "_" + NET + ASto, delay, bitrate);
						XmlNode if2 = CreateInterface(nodeto, INTERFACE + edgeid + "_" + NET + ASfrom, delay, bitrate);
						
						XmlNode parent = this.tree.getRoot();
						CreateLink(parent, if1, if2, 
								   NET + ASto + "_" + NET + ASfrom, 
								   NET + ASfrom + "_" + ROUTER + ASfrom, 
								   NET + ASto + "_" + ROUTER + ASto,
								   delay, bitrate);
					}	
				}
			}
			
			fs.close();
			br.close();
			/*
			this.tree.WriteXml(this.outputfile);
			this.tree.setRoot(null);
			*/
			System.out.println("End translate Inet file to xml file");
			
			long endTime=System.currentTimeMillis(); 
			this.slog.append("Totel net nodes: " + nodenum + "\n"); 
			this.slog.append("Totel links: " + linknum + "\n");
			this.slog.append("Totel translation cost time: " + (endTime-startTime) / 1000 + "s\n");
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		}
	}
	
	//translate brite output to xml format
	public void Brite2XmlFlat()
	{
		if (this.inputfile == null || !this.inputfile.endsWith(".brite"))
			throw new RuntimeException("Could not find the input file");
		
		try
		{
			if (this.slog != null)
				slog.append("Begin translate brite file to xml file\n");
			System.out.println("Begin translate brite file to xml file");
			
			long startTime=System.currentTimeMillis();
			
			this.CreateTopNet();
			
			FileInputStream fs= new FileInputStream(this.inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
					
			String data = null;
			String [] strarray = null;
			
			String nodeid, ASid, x, y;
			String edgeid, nodefrom, nodeto, ASfrom, ASto;
			
			float bandwidth, delay;
			
			//0 AS 1 router
			TopologyType type = null;
			boolean first = true;
			
			boolean nodes = false;
			boolean edges = false;
			
			int i = 0;
			int nodenum = 0;
			int linknum = 0;
			
			while((data = br.readLine()) != null)
			{
				i++;
				if (i % 10000 == 0)
					System.out.println("reading line: " + i + " Heap size = " 
							           + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576 + "m");
				data.trim();
				
				if (data.startsWith("Nodes"))
				{
					nodes = true;
					edges = false;
					continue;
				}
				
				if (data.startsWith("Edges"))
				{	
					edges = true;
					nodes = false;
					continue;
				}
				
				if (nodes && !data.equals(""))
				{		
					strarray = data.split("\t");
					
					nodeid = strarray[0];
					ASid = strarray[5];
				    x = strarray[1];
				    y = strarray[2];
				    
					//first time read node check type of graph(AS or router)
					if (first)
					{
						//router
						if (ASid.equals("-1"))
							type = TopologyType.ROUTER_LEVEL;
						//AS
						else
							type = TopologyType.AS_LEVEL;
						first = false;
					}
					
					XmlNode node;
					Coordinate c = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
					
					if (type == TopologyType.AS_LEVEL)
					{
						node = this.CreateNet(this.tree.getRoot(), NET + ASid);
						node = this.CreateRouter(node, ROUTER + ASid);
						
						this.tree.addCoordinate(node, c);
					}
					else if (type == TopologyType.ROUTER_LEVEL)
					{
						node = this.CreateRouter(this.tree.getRoot(), ROUTER + nodeid);
						
						this.tree.addCoordinate(node, c);
					}
					nodenum++;
				}
				
				//read edge information
				if (edges && !data.equals(""))
				{
					
					strarray = data.split("\t");
					
					edgeid = strarray[0];
					nodefrom = strarray[1];
					nodeto = strarray[2];
					//brite output delay is not correct(-1 for AS and 0 for router)
					//delay = strarray[4];
					bandwidth = Float.parseFloat(strarray[5]);
					ASfrom = strarray[6];
					ASto = strarray[7];
					
					XmlNode from = null, to = null;
					
					if (type == TopologyType.AS_LEVEL)
					{
						from = this.tree.getRouter(TOPNET + ":" + NET + nodefrom + ":" + ROUTER + nodefrom);
						to  = this.tree.getRouter(TOPNET + ":" + NET + nodeto + ":" + ROUTER + nodeto);
					}
					else if (type == TopologyType.ROUTER_LEVEL)
					{
						from = this.tree.getRouter(TOPNET + ":" + ROUTER + nodefrom);
						to  = this.tree.getRouter(TOPNET + ":" + ROUTER + nodeto);
					}
					
					if (from != null && to != null)
					{
						delay = this.AssignDL(from, to);
						XmlNode if1 = null, if2 = null;
						
						if (type == TopologyType.AS_LEVEL)
						{
							if1 = this.CreateInterface(from, INTERFACE + edgeid + "_" + NET + ASto,
									 			 delay, bandwidth);
							if2 = this.CreateInterface(to, INTERFACE + edgeid + "_" + NET + ASfrom,
									             delay, bandwidth);
						}
						else if (type == TopologyType.ROUTER_LEVEL)
						{
							if1 = this.CreateInterface(from, INTERFACE + edgeid + "_" + ROUTER + nodeto,
												delay, bandwidth);
							if2 = this.CreateInterface(to, INTERFACE + edgeid + "_"  + ROUTER + nodefrom,
												delay, bandwidth);
						}
						
						XmlNode parent = this.tree.getRoot();
						String linkname = "", ref1name = "", ref2name = "";
						if (type == TopologyType.AS_LEVEL)
						{
							linkname = NET + ASfrom + "_" + NET + ASto + "_" + i;
							ref1name = NET + ASfrom;
							ref2name = NET + ASto;
						}
						else if (type == TopologyType.ROUTER_LEVEL)
						{
							linkname = ROUTER + nodefrom + "_" + ROUTER + nodeto + "_" + i;
							ref1name = ROUTER + nodefrom;
							ref2name = ROUTER + nodeto;
						}
						this.CreateLink(parent, 
								        if1, if2, 
								        linkname, ref1name, ref2name, delay, bandwidth);
						
						linknum++;	
					}
				}
			}
			fs.close();
			br.close();

			if (this.slog != null)
				this.slog.append("End translate brite file to xml file\n");
			System.out.println("End translate brite file to xml file");
			
			long endTime=System.currentTimeMillis(); 
			this.slog.append("Totel net nodes: " + nodenum + "\n"); 
			this.slog.append("Totel links: " + linknum + "\n");
			this.slog.append("Totel translation cost time锛�" + (endTime-startTime) / 1000 + "s\n");
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		}
	}

	public void Brite2XmlHierarchical()
	{
		if (this.inputfile == null || !this.inputfile.endsWith(".brite"))
			throw new RuntimeException("could not find input file");
		
		try
		{
			if (this.slog != null)
				slog.append("Begin translate brite file to xml file\n");
			System.out.println("Begin translate brite file to xml file");
			
			long startTime=System.currentTimeMillis();
			
			this.CreateTopNet();
			
			FileInputStream fs= new FileInputStream(this.inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			
			Map<String, String> pathMap = new HashMap<String, String>();
			Map<String, String> borderRTMap = new HashMap<String, String>();
		
			String data = null;
			String [] strarray = null;
			
			String nodeid, ASid, nodereplica, type, x, y;
			String edgeid, nodefrom, nodeto, ASfrom, ASto, edgereplica;
			
			float delay, bandwidth;
			
			boolean nodes = false;
			boolean edges = false;
			
			XmlNode asnode, rtnode, replicanode;
			
			int i = 0;
			int nodenum = 0;
			int linknum = 0;
			
			while((data = br.readLine()) != null)
			{
				i++;
				if (i % 10000 == 0)
					System.out.println("reading line: " + i + " Heap size = " 
							           + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576 + "m");
				data.trim();
				
				if (data.startsWith("Nodes"))
				{
					nodes = true;
					edges = false;
					continue;
				}
				
				if (data.startsWith("Edges"))
				{	
					edges = true;
					nodes = false;
					continue;
				}
				
				if (nodes && !data.equals(""))
				{				
					strarray = data.split("\t");
					
					nodeid = strarray[0];
					ASid = strarray[5];
					nodereplica = strarray[6].substring(1, strarray[6].length() - 1);
					type = strarray[7];
					x = strarray[1];
				    y = strarray[2];
				    
				    Coordinate c = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
					//find a node with no replica
					if (nodereplica.equals("null"))
					{
						asnode = tree.getNet(TOPNET + ":" + NET + ASid);
						//first time find a none replica AS create the net object
						if (asnode == null)
						{
							asnode = this.CreateNet(this.tree.getRoot(), NET + ASid);
							nodenum++;
						}
					
						rtnode = this.CreateRouter(asnode, ROUTER + nodeid);
						this.tree.addCoordinate(rtnode, c);
						nodenum++;
		
						pathMap.put(nodeid, rtnode.path);
					}
					//find a node with replica
					else
					{
						replicanode = tree.getReplica(TOPNET + ":" + NET + ASid);
						if (replicanode == null)
						{
							String path = pathMap.get(nodereplica);
							asnode = tree.getRouter(path).parent;
							this.CreateReplicaNet(this.tree.getRoot(), asnode, NET + ASid);
						}
						
						//storage the border router information for later use
						if (type.equals(ROUTERBORDER))
							borderRTMap.put(nodeid, nodereplica);
						
						tree.rrtnum++;
					}
				}
				//read edge information
				if (edges && !data.equals(""))
				{
					strarray = data.split("\t");
					
					edgeid = strarray[0];
					nodefrom = strarray[1];
					nodeto = strarray[2];
					//delay = strarray[4];
					bandwidth = Float.parseFloat(strarray[5]);
					ASfrom = strarray[6];
					ASto = strarray[7];
					edgereplica = strarray[8];
					
					if (edgereplica.equals("<null>"))
					{
						XmlNode rtfrom, rtto;
					 	
						String rnodefrom = borderRTMap.get(nodefrom);
						String rnodeto = borderRTMap.get(nodeto);
				
						if (rnodefrom != null)
							rtfrom = tree.getRouter(pathMap.get(rnodefrom));
						else 
							rtfrom = tree.getRouter(pathMap.get(nodefrom));
						if (rnodeto != null)
							rtto = this.tree.getRouter(pathMap.get(rnodeto));
						else
							rtto = this.tree.getRouter(pathMap.get(nodeto));
					
						if (rtfrom != null && rtto != null)
						{
							delay = this.AssignDL(rtfrom, rtto);
							if (ASfrom.equals(ASto))
							{
								this.CreateInterface(rtfrom, INTERFACE + edgeid + "_" + ROUTER + nodeto,
										 			 delay, bandwidth);
								this.CreateInterface(rtto, INTERFACE + edgeid + "_" + ROUTER + nodefrom,
										             delay, bandwidth);
							}
							else
							{
								this.CreateInterface(rtfrom, INTERFACE + edgeid + "_" + NET + ASto + "_" + ROUTER + nodeto,
													delay, bandwidth);
								this.CreateInterface(rtto, INTERFACE + edgeid + "_" + NET + ASfrom + "_" + ROUTER + nodefrom,
													delay, bandwidth);
							}
							
							this.CreateLink(ASfrom, ASto, nodefrom, nodeto, 
									        delay, bandwidth, edgeid, 
											borderRTMap);
							linknum++;
						}
					}
					else
						tree.rlinknum++;
				}
			}
			
			fs.close();
			br.close();

			if (this.slog != null)
				this.slog.append("End translate brite file to xml file\n");
			System.out.println("End translate brite file to xml file");
			
			long endTime=System.currentTimeMillis(); 
			this.slog.append("Totel net nodes: " + nodenum + "\n"); 
			this.slog.append("Totel links: " + linknum + "\n");
			this.slog.append("Totel translation cost time锛�" + (endTime-startTime) / 1000 + "s\n");   
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		}
	}

	public void gtitm2XmlFlat(TopologyType Topotype, 
							  BWAssginType BWtype, float BWmin, float BWmax)
	{
		if (this.inputfile == null)
			throw new RuntimeException("input file could not be null");
		
		try
		{
			if (this.slog != null)
				this.slog.append("Begin translate Inet output file to xml file\n");
			System.out.println("Begin translate Inet output file to xml file");
			
			long startTime = System.currentTimeMillis();
			
			this.CreateTopNet();
			
			FileInputStream fs= new FileInputStream(this.inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			
			String nodeid, x, y;
			String from, to;
			String [] strarray = null;
			String data = null;
			
			boolean nodes = false;
			boolean edges = false;
			
			int i = 0;
			
			while((data = br.readLine()) != null)
			{
				i++;
				if (i % 10000 == 0)
					System.out.println("reading line: " + i + " Heap size = " 
							           + (Runtime.getRuntime().totalMemory() 
							           - Runtime.getRuntime().freeMemory()) / 1048576 + "m");
				data.trim();
		
				if (data.startsWith("VERTICES"))
				{
					nodes = true;
					edges = false;
					continue;
				}
				
				if (data.startsWith("EDGES"))
				{
					edges = true;
					nodes = false;
					continue;
				}
				
				XmlNode node;
				
				if (nodes && !data.equals(""))
				{
					strarray = data.split(" ");
					nodeid = strarray[0];
					x = strarray[2];
					y = strarray[3];
					
					if (Topotype == TopologyType.AS_LEVEL)
					{
						node = CreateNet(tree.getRoot(), NET + nodeid);
						node = CreateRouter(node, ROUTER + nodeid);
						
						Coordinate c = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
						tree.addCoordinate(node, c);
					}
					else if (Topotype == TopologyType.ROUTER_LEVEL)
					{
						node = this.CreateRouter(tree.getRoot(), ROUTER + nodeid);
						
						Coordinate c = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
						tree.addCoordinate(node, c);
					}
				}
				else if (edges && !data.equals(""))
				{
					strarray = data.split(" ");
					from = strarray[0];
					to = strarray[1];
					
					XmlNode nodefrom = null, nodeto = null;
					//first time meet the node create AS/Router place the node on the plane
					if (Topotype == TopologyType.AS_LEVEL)
					{
						nodefrom = tree.getRouter(TOPNET + ":" + NET + from + ":" + ROUTER + from);
						nodeto = tree.getRouter(TOPNET + ":" + NET + to + ":" + ROUTER + to);
					}
					else if (Topotype == TopologyType.ROUTER_LEVEL)
					{
						nodefrom = tree.getRouter(TOPNET + ":" + ROUTER + from);
						nodeto = tree.getRouter(TOPNET + ":" + ROUTER + to);
					}
								
					//create the interface and link
					if (nodefrom != null && nodeto != null)
					{
						float delay,  bandwidth;
						delay = this.AssignDL(nodefrom, nodeto);
						bandwidth = AssignBW(BWmin, BWmax, BWtype);
						
						XmlNode if1 = null, if2= null;
						if (Topotype == TopologyType.AS_LEVEL)
						{
							if1 = this.CreateInterface(nodefrom, INTERFACE + i + "_" + NET + to, delay, bandwidth);
							if2 = this.CreateInterface(nodeto, INTERFACE + i + "_" + NET + from, delay, bandwidth);
						}
						else if (Topotype == TopologyType.ROUTER_LEVEL)
						{	
							if1 = this.CreateInterface(nodefrom, INTERFACE + i + "_" + ROUTER + to, delay, bandwidth);
							if2 = this.CreateInterface(nodeto, INTERFACE + i +"_" + ROUTER + from, delay, bandwidth);
						}
						
						XmlNode parent = this.tree.getRoot();
						String linkname = "", ref1name = "", ref2name = "";
						if (Topotype == TopologyType.AS_LEVEL)
						{
							linkname = NET + from + "_" + NET + to + "_" + i;
							ref1name = NET + from;
							ref2name = NET + to;
						}
						else if (Topotype == TopologyType.ROUTER_LEVEL)
						{
							linkname = ROUTER + from + "_" + ROUTER + to + "_" + i;
							ref1name = ROUTER + from;
							ref2name = ROUTER + to;
						}
						CreateLink(parent, if1, if2, 
								   linkname, ref1name, ref2name, 
								   delay, bandwidth);
					}
				}			
			}
			
			fs.close();
			br.close();
			/*
			this.tree.WriteXml(this.outputfile);
			this.tree.setRoot(null);
			*/
			System.out.println("End translate Inet file to xml file");
			
			long endTime = System.currentTimeMillis();
			this.slog.append("Totel net nodes: " + tree.getRouterNum() + "\n"); 
			this.slog.append("Totel links: " + i + "\n");
			this.slog.append("Totel translation cost time锛�" + (endTime-startTime) / 1000 + "s\n");   
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		}
	}
	
	public void gtitm2XmlHierarchical(BWAssginType BWtype, float BWmin, float BWmax)
	{
		if (this.inputfile == null)
			throw new RuntimeException("input file could not be null");
		
		try
		{
			if (this.slog != null)
				this.slog.append("Begin translate Inet output file to xml file\n");
			System.out.println("Begin translate Inet output file to xml file");
			
			long startTime = System.currentTimeMillis();
			
			this.CreateTopNet();
			
			FileInputStream fs= new FileInputStream(this.inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			
			Map<String, XmlNode> routerMap = new HashMap<String, XmlNode>();
			Map<String, XmlNode> TMap = new HashMap<String, XmlNode>();
			
			String nodeid, nodeinfo, x, y;
			String from, to;
			
			String [] strarray = null;
			String data = null;
			
			boolean nodes = false;
			boolean edges = false;
			
			int linknum = 0;
			int i = 0;
			
			XmlNode tmpnode1, tmpnode2;
			
			while((data = br.readLine()) != null)
			{
				i++;
				
				if (i % 10000 == 0)
					System.out.println("reading line: " + i + " Heap size = " 
							           + (Runtime.getRuntime().totalMemory() 
							           - Runtime.getRuntime().freeMemory()) / 1048576 + "m");
				data.trim();
		
				if (data.startsWith("VERTICES"))
				{
					nodes = true;
					edges = false;
					continue;
				}
				
				if (data.startsWith("EDGES"))
				{
					edges = true;
					nodes = false;
					continue;
				}
				
				if (nodes && !data.equals(""))
				{
					strarray = data.split(" ");
					nodeid = strarray[0];
					nodeinfo = strarray[1];
					x = strarray[2];
					y = strarray[3];
					
					//T node
					if (nodeinfo.startsWith("T"))
					{
						strarray = nodeinfo.split(":");
						
						String id = strarray[1];
						
						tmpnode1 = CreateNet(tree.getRoot(), NET + "T" + id);
						tmpnode2 = this.CreateRouter(tmpnode1, ROUTER + "T" + id);
							
						Coordinate c = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));	
						tree.addCoordinate(tmpnode1, c);
						tree.addCoordinate(tmpnode2, c);
						
						TMap.put(id, tmpnode1);
						routerMap.put(nodeid, tmpnode2);
					}
					//S node
					else if (nodeinfo.startsWith("S")) 
					{
						int n = nodeinfo.indexOf(":");
						int m = nodeinfo.indexOf("/");
						
						String Tid = nodeinfo.substring(n + 1, m);
						String Sid = nodeinfo.substring(m + 1);
						
						tmpnode1 = TMap.get(Tid);
						tmpnode1 = CreateNet(tmpnode1, NET + "T" + Tid + "_" + "S" + Sid);
						tmpnode2 = CreateRouter(tmpnode1, ROUTER + "T" + Tid + "_" + "S" + Sid);
						
						Coordinate c = new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
						tree.addCoordinate(tmpnode1, c);
						tree.addCoordinate(tmpnode2, c);
						
						routerMap.put(nodeid, tmpnode2);
					}
				}
				else if (edges && !data.equals(""))
				{
					linknum++;
					
					strarray = data.split(" ");
					from = strarray[0];
					to = strarray[1];
					
					XmlNode nodefrom = null, nodeto = null;
					nodefrom = routerMap.get(from);
					nodeto = routerMap.get(to);
								
					//create the interface and link
					if (nodefrom != null && nodeto != null)
					{
						float delay,  bandwidth;
						delay = this.AssignDL(nodefrom, nodeto);
						bandwidth = AssignBW(BWmin, BWmax, BWtype);
						
						XmlNode if1 = null, if2= null;
			
						if1 = this.CreateInterface(nodefrom, INTERFACE  + "_" + nodeto.parent.name + linknum, delay, bandwidth);
						if2 = this.CreateInterface(nodeto, INTERFACE  +"_" + nodefrom.parent.name + linknum, delay, bandwidth);
						
						XmlNode parent;
						if (nodefrom.parent == nodeto.parent)
							parent = nodefrom.parent;
						else
							parent = tree.getRoot();
						
						String linkname = "", ref1name = "", ref2name = "";
						
						linkname = nodefrom.parent.name + "_" + nodeto.parent.name + "_" + linknum;
						ref1name = nodefrom.parent.name;
						ref2name = nodeto.parent.name;
						
						CreateLink(parent, if1, if2, 
								   linkname, ref1name, ref2name, 
								   delay, bandwidth);
					}
				}			
			}
			
			fs.close();
			br.close();
			/*
			this.tree.WriteXml(this.outputfile);
			this.tree.setRoot(null);
			*/
			System.out.println("End translate Inet file to xml file");
			
			long endTime = System.currentTimeMillis();
			this.slog.append("Totel net nodes: " + tree.getNetNum() + "\n"); 
			this.slog.append("Totel links: " + linknum + "\n");
			this.slog.append("Totel translation cost time锛�" + (endTime-startTime) / 1000 + "s\n");   
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		}
	}
	
	public void WriteXml(String file)
	{
		this.tree.WriteXml(file);
	}
	
	public XmlTree getXmlTree()
	{
		return this.tree;
	}
	
	public void AttachLanNet(int minsubnetnum, int maxsubnetnum, boolean duplicate,
							 int minrtnum, int maxrtnum,
							 int minhostnum, int maxhostnum,
							 float minrtbandwidth, float maxrtbandwidth,  BWAssginType rtbwdist,
							 float minrtlatency, float maxrtlatency, BWAssginType rtltdist,
							 float minhbandwidth, float maxhbandwidth, BWAssginType hbwdist,
							 float minhlatency, float maxhlatency, BWAssginType hltdist)
	{
		Random random = new Random(System.currentTimeMillis());
		int subnetnum = Distribution.getUniformRandom(random, minsubnetnum, maxsubnetnum);
		
		Map<String, XmlNode> map = tree.getRouters();
		List<XmlNode> routers = new ArrayList<XmlNode>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			routers.add(map.get(key));
		}
		
		//traverse all routers create lan network
		int gener_lan = 0;
		int index = 0;
		
		Map<String, XmlNode> lanMap = new HashMap<String, XmlNode>();
		
	    while (gener_lan < subnetnum) {
	        if (index >= routers.size())
	        	index %= routers.size();
	        
	    	XmlNode rt = routers.get(index++);
	    	
			random = new Random(System.currentTimeMillis());
			int rtnum = Distribution.getUniformRandom(random, minrtnum, maxrtnum);
			int [] hnum = new int[rtnum];
			for (int i = 0; i < hnum.length; i++)
				hnum[i] = Distribution.getUniformRandom(random, minhostnum, maxhostnum);
			String key = "";
			for (int i = 0; i < hnum.length; i++)
				key += hnum[i] + " ";
			
			float rtlatency = AssignBW(minrtlatency, maxrtlatency, rtltdist);
			float rtbit_rate = AssignBW(minrtbandwidth, maxrtbandwidth, rtbwdist);
			float hlatency = AssignBW(minhlatency, maxhlatency, hltdist);
			float hbit_rate = AssignBW(minhbandwidth, maxhbandwidth, hbwdist);
			
			//auto create duplicate LAN 
			if (duplicate)
			{
				XmlNode lan = lanMap.get(key);
				if (lan == null)
				{
					lan = CreateLan(this.tree.getRoot(), rt, null, "lan" + gener_lan++, 
						      		hnum, rtlatency, rtbit_rate, hlatency, hbit_rate);
					lanMap.put(key, lan);
				}
				else
					CreateReplicaLan(tree.getRoot(), rt, lan, "lan" + gener_lan++,
									 rtlatency, rtbit_rate);
				
				List<XmlNode> list = tree.getASRepplica(rt.parent);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						CreateLan(this.tree.getRoot(), rt, list.get(i), "lan" + gener_lan++, 
					      		  hnum, rtlatency, rtbit_rate, hlatency, hbit_rate);
					}
						
				}
			}
			else
			{
				CreateLan(this.tree.getRoot(), rt, null, "lan" + gener_lan++, 
					       hnum, rtlatency, rtbit_rate, hlatency, hbit_rate);
				
				List<XmlNode> list = tree.getASRepplica(rt.parent);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						CreateLan(this.tree.getRoot(), rt, list.get(i), "lan" + gener_lan++, 
					      		        hnum, rtlatency, rtbit_rate, hlatency, hbit_rate);
					}
						
				}
			}
		}
		
	}
}
