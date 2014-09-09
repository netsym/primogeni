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
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is class define a xml node
 * @author Hao Jiang
 */
public class XmlNode 
{	
	static final String NODE = "node";
	static final String REPLICA = "replica";
	static final String REF = "ref";
	static final String ATTRIBUTE = "attribute";
	
	public static enum XmlNodeType
	{
		NODE,
		REPLICA,
		REF,
		ATTR;
	}
	
	//node name attribute
	public String name;
	//node type
	public XmlNodeType nodetype;
	//path from root to this node
	public String path;
	//node depth in tree
	public int level;
	//parent node
	public XmlNode parent;
	//xml node attributes map
	private Map<String,String> attris;
	//children node list
	private List<XmlNode> children;
	
	public XmlNode(XmlNodeType type, String name)
	{
		this.name = name;
		this.nodetype = type;
		this.path = name;
		
		this.level = -1;
		this.parent = null;
		
		this.attris = new HashMap<String, String>();
		this.children = new ArrayList<XmlNode>();
		
		this.setAttribute("name", name);
	}
	
	public XmlNode(XmlNodeType type)
	{
		this.name = "";
		this.nodetype = type;
		this.path = "";
		
		this.level = -1;
		this.parent = null;
		
		this.attris = new HashMap<String, String>();
		this.children = new ArrayList<XmlNode>();
	}
	
	public XmlNode(Node n)
	{
		this.name = "";
		this.nodetype = null;
		this.path = "";
		
		this.level = -1;
		this.attris = new HashMap<String, String>();
		this.children = new ArrayList<XmlNode>();
		
		if(n.getNodeName().compareTo("node")==0) {
			this.nodetype = XmlNodeType.NODE;
		}
		else if(n.getNodeName().compareTo("replica")==0) {
			this.nodetype = XmlNodeType.REPLICA;
		}
		else if(n.getNodeName().compareTo("attribute")==0) {
			this.nodetype = XmlNodeType.ATTR;
		}
		else if(n.getNodeName().compareTo("ref")==0) {
			this.nodetype = XmlNodeType.REF;
		}
		
		NamedNodeMap node_attrs = n.getAttributes();
		if(null != node_attrs)
		{
			for(int i = 0; i < node_attrs.getLength();i++) 
			{
				Node a = node_attrs.item(i);
				if(a.getNodeName().equals("name"))
				{
					name = a.getNodeValue();
					path = "";
				}
				this.attris.put(a.getNodeName(), a.getNodeValue());
			}
		}
		
		NodeList nodeLst = n.getChildNodes();
		if(nodeLst.getLength()>0) 
		{
			for (int i = 0; i < nodeLst.getLength(); i++)
			{
				n = nodeLst.item(i);
				if(n.getNodeType()==Node.ELEMENT_NODE)
					this.add(new XmlNode(n));
			}
		}
	}
	
	public void setAttribute(String key, String value)
	{
		if (this.attris.containsKey(key))
			this.attris.remove(key);
		this.attris.put(key, value);
		
		if (key.equals("name"))
		{
			this.name = value;
			if (this.parent != null)
				this.path = this.parent.path + ":" + this.name;
			else
				this.path = this.name;
		}
	}
	
	public String getPath()
	{
		if (this.parent == null)
			return name;
		else
			return parent.getPath() + ":" + name;
	}
	
	public int getLevel()
	{
		if (this.parent == null)
			return 0;
		else 
			return parent.getLevel() + 1;
	}
	
	public String getAttribute(String key)
	{
		return this.attris.get(key);
	}
	
	public void RemoveAttri(String key)
	{
		this.attris.remove(key);
	}
		
	public List<XmlNode> getChildren()
	{
		return this.children;
	}
	
	public XmlNode getChild(int index)
	{
		
		if (children != null && 
				index >=0 &&
				index < children.size())
			return children.get(index);
		return null;
	}
	
	public XmlNode getChild(String name)
	{
		if (children != null)
		{
			XmlNode tmp;
			for (int i = 0; i < this.children.size(); i++)
			{
				tmp = children.get(i);
				if (tmp.attris.get("name").equals(name))
					return tmp;
			}
		}
		return null;
	}
	public void addChild(XmlNode node)
	{
		if (node != null)
		{
			this.children.add(node);
			node.level = this.level + 1;
			node.parent = this;
			node.path = this.path + ":" + node.name;
		}
	}
	
	public void add(XmlNode node)
	{
		if (node != null)
		{
			this.children.add(node);
			node.parent = this;
		}
	}
	
	public void removeChild(int index)
	{
		if (children != null && 
				index >=0 &&
				index < children.size())
			children.remove(index);
	}
		
	public boolean equals(XmlNode node)
	{
		if (node != null)
		{
			if (this.nodetype.equals(node.nodetype) &&
				this.attris.equals(node.attris) &&
				this.path.equals(node.path))
				return true;
		}
		
		return false;
	}
	
	public void Print()
	{
		String str = "";
		int lv = this.level;

		while(lv != 0)
		{
			str += "\t";
			lv--;
		}
	
		switch(this.nodetype)
		{
			case NODE: str += "<" + NODE;
				break;
			case REPLICA: str += "<" + REPLICA;
				break;
			case REF: str += "<" + REF;
				break;
			case ATTR: str += "<" + ATTRIBUTE;
				break;
		}
		
		Set<Entry<String, String>> attriset = attris.entrySet();
		Iterator<Entry<String, String>> iter = attriset.iterator();
		Entry<String, String> entry;
		while(iter.hasNext())
		{
			entry =iter.next();
			str += " " + entry.getKey() + "=" + "\"" + entry.getValue() + "\"";
		}
		str += "/>\n";
		
		System.out.println(str);
		
		XmlNode node;
		for (int i = 0; i < this.children.size(); i++)
		{
			node = this.children.get(i);
			node.Print();
		}
	}

	public void Writexml(BufferedWriter out) throws IOException
	{
		String str = "";
		int lv = this.level;

		while(lv != 0)
		{
			str += " ";
			lv--;
		}
		
		switch(this.nodetype)
		{
			case NODE: str += "<" + NODE;
				break;
			case REPLICA: str += "<" + REPLICA;
				break;
			case REF: str += "<" + REF;
				break;
			case ATTR: str += "<" + ATTRIBUTE;
				break;
		}
		
		Set<Entry<String, String>> attriset = attris.entrySet();
		Iterator<Entry<String, String>> iter = attriset.iterator();
		Entry<String, String> entry;
		while(iter.hasNext())
		{
			entry = iter.next();
			str += " " + entry.getKey() + "=" + "\"" + entry.getValue() + "\"";
		}
		
		if (this.children.size() == 0)
		{
			str += "/>\n";
			out.write(str);
		}
		else
		{
			str += ">\n";
			out.write(str);
			
			XmlNode node;
			for (int i = 0; i < this.children.size(); i++)
			{
				node = this.children.get(i);
				node.Writexml(out);
			}
			
			str = "";
			lv = this.level;
			while(lv != 0)
			{
				str += " ";
				lv--;
			}
			
			switch(this.nodetype)
			{
				case NODE: str += "</" + NODE + ">\n";
					break;
				case REPLICA: str += "</" + REPLICA + ">\n";
					break;
				case REF: str += "</" + REF + ">\n";
					break;
				case ATTR: str += "</" + ATTRIBUTE + ">\n";
					break;
			}
			
			out.write(str);
		}
	}
}
