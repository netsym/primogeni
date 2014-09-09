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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import slingshot.wizards.topology.XmlNode.XmlNodeType;

/**
 * This is class define a xml tree structure
 * @author Hao Jiang
 */
public class XmlTree
{	
	private XmlNode root;
	//private Map<String, XmlNode> nodes;
	private Map<XmlNode, Coordinate> coordinates;
	
	public Map<String, XmlNode> routers;
	public Map<String, XmlNode> nets;
	public Map<String, XmlNode> links;
	public Map<String, XmlNode> replicas;
	
	public Map<XmlNode, List<XmlNode>> ASreplicsMap;
	
	public int minrtlevel;
	public int maxrtlevel;
	
	public int rrtnum;
	public int rlinknum;
    
	public XmlTree()
	{
		root = null;
		
		routers = new HashMap<String, XmlNode>();
		nets = new HashMap<String, XmlNode>();
		links = new HashMap<String, XmlNode>();
		replicas = new HashMap<String, XmlNode>();
		
		ASreplicsMap = new HashMap<XmlNode, List<XmlNode>>();
		
		coordinates = new HashMap<XmlNode, Coordinate>();
		
		maxrtlevel = 0;
		minrtlevel = Integer.MAX_VALUE;
		
		rrtnum = 0;
		rlinknum = 0;
	}
	
	//load xml file to create tree struct
	public void Loadfile(String xmlfile)
	{
		File file = new File(xmlfile);
		if (!file.exists())
			return;
		
		Document doc = null;
		try 
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file);
			doc.getDocumentElement().normalize();
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		NodeList nodeLst = doc.getChildNodes();
	
		List<Node> roots = new ArrayList<Node>();
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node n = nodeLst.item(i);
			if(n.getNodeType()==Node.ELEMENT_NODE) {
				roots.add(n);
			}
		}
		
		if(roots.size()==0) {
			throw new RuntimeException("Found No top net in this file");
		}
		else if(roots.size()>1) {
			throw new RuntimeException("Found multiple top net in this file");
		}
		
		this.root = new XmlNode(roots.get(0));
		vistDFS(root);
	}
	
	private void vistDFS(XmlNode node)
	{
		node.level = node.getLevel();
		node.path = node.getPath();
		
		if (node.nodetype == XmlNodeType.NODE)
		{
			if (node.getAttribute("type").equals(XmlScript.NODE_TYPE_NET))
				this.addNet(node);
			else if (node.getAttribute("type").equals(XmlScript.NODE_TYPE_ROUTER))
				this.addRouter(node);
			else if (node.getAttribute("type").equals(XmlScript.NODE_TYPE_LINK))
				this.addLink(node);
		}	
		
		for (XmlNode child : node.getChildren())
		{
			vistDFS(child);
		}
	}
	
	
	//find the path from one node to another
	public String getPath(XmlNode nodefrom, XmlNode nodeto)
	{
		String path = "";
		
		String path1, path2;
		
		path1 = nodefrom.path;
		path2 = nodeto.path;
		
		if (path1.equals(path2))
			return null;
		
		String [] strarray1 = path1.split(":");
		String [] strarray2 = path2.split(":");
		
		int i;
		
		for (i = 0; i < strarray1.length && i < strarray2.length; i++)
		{
			if (!strarray1[i].equals(strarray2[i]))
				break;
		}
		
		int back = strarray1.length - i - 1;
		while(back != 0)
		{
			path += "..:";
			back--;
		}
		
		for (;i < strarray2.length; i++)
		{
			path += strarray2[i] + ":";
		}
	
		return path.substring(0, path.length() - 1);
	}
	
	public XmlNode getRoot()
	{
		return this.root;
	}
	
	public void setRoot(XmlNode root)
	{
		this.root = root;
	}
	
	public void addRouter(XmlNode rt)
	{
		this.routers.put(rt.path, rt);
		if (this.maxrtlevel < rt.level)
			maxrtlevel = rt.level;
		if (this.minrtlevel > rt.level)
			minrtlevel = rt.level;
	}
	
	public void addNet(XmlNode net)
	{
		this.nets.put(net.path, net);
	}
	
	public void addLink(XmlNode link)
	{
		this.links.put(link.path, link);
	}
	
	public void addReplica(XmlNode replica)
	{
		this.replicas.put(replica.path, replica);
	}
	
	public void addASReplica(XmlNode node, XmlNode replicanode) {
		if (node == null || replicanode == null)
			return;
		List<XmlNode> list = ASreplicsMap.get(node);
		if (list != null) {
			if (!list.contains(replicanode))
				list.add(replicanode);
		}
		else {
			list = new ArrayList<XmlNode>();
			list.add(replicanode);
			ASreplicsMap.put(node, list);
		}
	}
	
	public XmlNode getRouter(String key)
	{
		return this.routers.get(key);
	}
	
	public Map<String, XmlNode> getRouters()
	{
		return this.routers;
	}
	
	public XmlNode getNet(String key)
	{
		return this.nets.get(key);
	}
	
	public XmlNode getLink(String key)
	{
		return this.links.get(key);
	}
	
	public XmlNode getReplica(String key)
	{
		return this.replicas.get(key);
	}
	
	public List<XmlNode> getASRepplica(XmlNode node) {
		return this.ASreplicsMap.get(node);
	}
	
	public boolean ContainsCoordinate(Coordinate c)
	{
		if (this.coordinates.containsValue(c))
			return true;
		else 
			return false;
	}
	
	public Coordinate getCoordinate(XmlNode node)
	{
		return this.coordinates.get(node);
	}
	
	public void addCoordinate(XmlNode node, Coordinate c)
	{
		this.coordinates.put(node, c);
	}
	
	public int getRouterNum()
	{
		return this.routers.size();
	}
	
	public int getNetNum()
	{
		return this.nets.size();
	}
	
	public int getLinkNum()
	{
		return this.links.size();
	}
	
	public int getReplicaNum()
	{
		return this.replicas.size();
	}
	
	public void ClearTree()
	{
		this.nets.clear();
		this.routers.clear();
		this.links.clear();
		this.replicas.clear();
		
		this.root = null;
		
		maxrtlevel = 0;
		minrtlevel = Integer.MAX_VALUE;
	}
	
	public void WriteXml(String file)
	{
		if (this.root == null)
			return;
		
		try
		{
		    // Create file 
		    FileWriter fstream = new FileWriter(file);
		    BufferedWriter out = new BufferedWriter(fstream);
		    
		    out.write("<?xml version=\"1.0\" ?>\n");
		    out.write("<model xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		    out.write("\txsi:noNamespaceSchemaLocation=\"primex.xsd\">\n");
		    this.root.Writexml(out);
		    out.write("</model>\n");
		    out.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		}
	}
}

