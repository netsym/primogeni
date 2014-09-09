package jprime.util;

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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jprime.EmulationCommand;
import jprime.Experiment;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.Net;
import jprime.routing.StaticRoutingProtocol;
import jprime.variable.ModelNodeVariable;
import jprime.variable.SymbolVariable;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Nathanael Van Vorst

 * Build a JPRIME model from an xml document that conforms to the primex schema
 */
public class XMLModelBuilder {
	/**
	 * @author Nathanael Van Vorst
	 *
	 */
	private static class temp_emu_cmd {
		final String cmd;
		final String outputSuffix;
		final long delay;
		final long maxRuntime;
		final boolean block;
		final boolean checkReturnCode;
		public temp_emu_cmd(String cmd, String outputSuffix, long delay,
				long maxRuntime, boolean block, boolean checkReturnCode) {
			super();
			this.cmd = cmd;
			this.outputSuffix = outputSuffix;
			this.delay = delay;
			this.maxRuntime = maxRuntime;
			this.block = block;
			this.checkReturnCode=checkReturnCode;
		}
	}
	
	/**
	 * @author Nathanael Van Vorst
	 *
	 * The types of nodes in a primex xml model
	 */
	public static enum XMLModelNodeType {
		MODEL,
		NODE,
		REPLICA,
		REF,
		ATTR,
		EMUCMD;
	}
	
	/**
	 * @author Nathanael Van Vorst
	 *
	 * A wrapper for primex xml model nodes
	 */
	public static class XMLModelNode {
		public final XMLModelNodeType type;
		public final Map<String,String> normal_attrs;
		public final Map<String,String> symbol_attrs;
		public final List<XMLModelNode> kids;
		public String node_type,path,name;
		public IModelNode node;

		/**
		 * Create a XMLModelNode from an raw xml node
		 * @param n the raw xml node
		 */
		public XMLModelNode(Node n) {
			this.node_type=null;
			this.path=null;
			this.name=null;
			this.node = null;
			this.normal_attrs=new HashMap<String, String>();
			this.symbol_attrs=new HashMap<String, String>();
			this.kids = new ArrayList<XMLModelNode>();
			if(n.getNodeName().compareTo("model")==0) {
				this.type=XMLModelNodeType.MODEL;
			}
			else if(n.getNodeName().compareTo("node")==0) {
				this.type=XMLModelNodeType.NODE;
			}
			else if(n.getNodeName().compareTo("emucmd")==0) {
				this.type=XMLModelNodeType.EMUCMD;
			}
			else if(n.getNodeName().compareTo("replica")==0) {
				this.type=XMLModelNodeType.REPLICA;
			}
			else if(n.getNodeName().compareTo("attribute")==0) {
				this.type=XMLModelNodeType.ATTR;
			}
			else if(n.getNodeName().compareTo("ref")==0) {
				this.type=XMLModelNodeType.REF;
			}
			else throw new RuntimeException("Invalid node type "+n.getNodeName());
			List<XMLModelNode> temp = new ArrayList<XMLModelNode>();

			NamedNodeMap node_attrs = n.getAttributes();
			if(null != node_attrs) {
				for(int i=0;i<node_attrs.getLength();i++) {
					Node a = node_attrs.item(i);
					if(a.getNodeName().equals("type")) node_type=a.getNodeValue();
					else if(a.getNodeName().equals("path")) path=a.getNodeValue();
					else if(a.getNodeName().equals("name")) name=a.getNodeValue();
					//else if(a.getNodeName().equals("value")) attrs.put("value",a.getNodeValue());
					else normal_attrs.put(a.getNodeName(),a.getNodeValue());
				}
			}
			NodeList nodeLst = n.getChildNodes();
			if(nodeLst.getLength()>0) {
				for (int i = 0; i < nodeLst.getLength(); i++) {
					n = nodeLst.item(i);
					if(n.getNodeType()==Node.ELEMENT_NODE)
						temp.add(new XMLModelNode(n));
				}
			}
			for(XMLModelNode i : temp) {
				switch(i.type) {
				case ATTR: 
					if(Boolean.parseBoolean(i.normal_attrs.get("symbol"))) {
						symbol_attrs.put(i.name, i.normal_attrs.get("value"));
					}
					else {
						normal_attrs.put(i.name, i.normal_attrs.get("value"));
					}
					break;
				case NODE:
				case REPLICA:
				case REF:
				case EMUCMD:
					kids.add(i);
					break;
				case MODEL:
				default:
					throw new RuntimeException("Did not expect XMLModelNode type "+i.type);
				}
			}
		}

		/**
		 * recursively create jprime model nodes until an Alias (ref) or Replica is Encountered
		 */
		public void expandNodes() {
			if(this.node == null)
				throw new RuntimeException("Called expandNodes wth this.node == null !");
			for(Entry<String,String> e : this.normal_attrs.entrySet()) {
				int dbname = ModelNodeVariable.name2int(e.getKey());
				ModelNodeVariable v = createModelNodeVariable(dbname,e.getValue());
				this.node.addAttr(v);
			}
			for(Entry<String,String> e : this.symbol_attrs.entrySet()) {
				SymbolVariable sym =new SymbolVariable(e.getValue());
				int dbname = ModelNodeVariable.name2int(e.getKey());
				this.node.addAttr(sym);
				sym.attachToNode((ModelNode)this.node, dbname);
			}
			ArrayList<temp_emu_cmd> cmds = new ArrayList<temp_emu_cmd>();

			for(XMLModelNode c : kids) {
				if(c.type == XMLModelNodeType.NODE) {
					if(c.node_type == null)
						throw new RuntimeException("The node has no type!");
					c.node = ModelNode.createModelNode(c.node_type, this.node, c.name);
					if(c.node instanceof StaticRoutingProtocol) {
						((Net)this.node).setStaticRoutingProtocol((StaticRoutingProtocol)c.node);
					}
					else {
						((ModelNode)this.node).addChild((ModelNode)c.node);
					}
					c.expandNodes();
				}
				else if(c.type == XMLModelNodeType.EMUCMD) {
					final String cmd=c.normal_attrs.get("cmd");
					final String outputSuffix=c.normal_attrs.get("outputSuffix");
					final long delay=Long.parseLong(c.normal_attrs.get("delay"));
					final long maxRuntime=Long.parseLong(c.normal_attrs.get("maxRuntime"));
					final boolean block=Boolean.parseBoolean(c.normal_attrs.get("block"));
					final boolean checkReturnCode=Boolean.parseBoolean(c.normal_attrs.get("checkReturnCode"));
					cmds.add(new temp_emu_cmd(cmd,outputSuffix,delay,maxRuntime,block,checkReturnCode));
				}
			}
			if(cmds.size()>0) {
				if(this.node instanceof IHost) {
					IHost h = (IHost)this.node;
					for(temp_emu_cmd cmd : cmds) {
						jprime.Console.out.println("Adding emulation command! "+cmd);
						try {
							new EmulationCommand(h, cmd.cmd, cmd.outputSuffix, cmd.delay, cmd.maxRuntime, cmd.block, TimeUnit.MILLISECONDS, cmd.checkReturnCode);
						}
						catch(Exception e) {
							jprime.Console.out.println("Tried to add emulation commands to a node which is not emulated!");
						}
					}
				}
				else {
					jprime.Console.out.println("Tried to add emulation commands to a node which was not a host!");
				}
			}
		}


		/**
		 * Recursively find all aliases and replicas, remembering the depth at which they were found.
		 *
		 * @param map a map which stores the depth of which nodes were found
		 * @param depth the current depth
		 */
		public void findLinksAndReplicas(Map<Integer, Set<XMLModelNode>> map, Integer depth) {
			//jprime.Console.out.println("findLinksAndReplicas "+this.node.getUniqueName());
			for(XMLModelNode c : kids) {
				if(c.type == XMLModelNodeType.REPLICA) {
					Set<XMLModelNode> l =null;
					if(!map.containsKey(depth+1)) {
						l = new HashSet<XMLModelNode>();
						map.put(depth+1, l);
					}
					else {
						l=map.get(depth+1);
					}
					l.add(this);
				}
				else if(c.type == XMLModelNodeType.REF) {
					Set<XMLModelNode> l =null;
					if(!map.containsKey(depth)) {
						l = new HashSet<XMLModelNode>();
						map.put(depth, l);
					}
					else {
						l=map.get(depth);
					}
					l.add(this);
				}
				else {
					c.findLinksAndReplicas(map,depth+1);
				}
			}
		}

		/**
		 * Try to expand the link by evaluating the references. If the references cannot be resolved then an error is thrown (if throwerror is true)
		 * @param throwerror whether to throw an error when a reference cannot be resolved
		 * @return the number of references that could _not_ be resolved
		 */
		public int expandLink(boolean throwerror) {
			if(throwerror) jprime.Console.out.println("ACK Expanding link "+this.node.getUniqueName());
			//jprime.Console.out.println("Expanding link "+this.node.getUniqueName());
			if(this.node == null)
				throw new RuntimeException("Called expandRefs wth this.node == null !");
			ILink l = null;
			int rv = 0;
			for(XMLModelNode c : kids) {
				if(c.type == XMLModelNodeType.REF) {
					if(c.node != null) continue;
					if(l == null) {
						if(this.node instanceof ILink) {
							l=(ILink)this.node;
						}
						else {
							if(throwerror)jprime.Console.out.println("\t\tACK 1");
							throw new RuntimeException("References are ONLY supported as children of links. Found "+this.node.getClass().getName());
						}
					}
					if(c.path == null) {
						if(throwerror)jprime.Console.out.println("\t\tACK 2");
						throw new RuntimeException("references must have a path attribute!");
					}
					try {
						c.node = (ModelNode) ModelNode.evaluatePath(this.node, c.path);
					} catch (Exception e) {
						if(throwerror) {
							if(throwerror)jprime.Console.out.println("\t\tACK 3");
							throw new RuntimeException(e);
						}
						//jprime.Console.out.println("ACK\t\t"+e.getMessage());
						rv++;
						continue;
					}
					if(c.node == null) {
						if(throwerror) {
							if(throwerror)jprime.Console.out.println("\t\tACK 4");
							throw new RuntimeException("The path '"+c.path+"' from "+c.node.getUniqueName()+" is invalid!");
						}
						//jprime.Console.out.println("\t\tThe path '"+c.path+"' from "+c.node.getUniqueName()+" is invalid!");
						rv++;
						continue;
					}
					if(!(c.node instanceof IInterface)) {
						if(throwerror)jprime.Console.out.println("\t\tACK 5");
						throw new RuntimeException("references should only refer to interfaces. Found "+c.node.getClass().getName());
					}
					c.node = (ModelNode)l.createInterface((IInterface)c.node);
					//jprime.Console.out.println("\t\tAttached iface "+c.node.getUniqueName());
				}
			}
			if(throwerror) jprime.Console.out.println("\t\tACK Expanded link "+this.node.getUniqueName()+" --> "+rv);
			//jprime.Console.out.println("\t\tExpanded link "+this.node.getUniqueName()+" --> "+rv);
			return rv;
		}

		/**
		 * Try to expand all children who are replicas. If the replicas cannot be resolved then an error is thrown (if throwerror is true)
		 * @param throwerror whether to throw an error when a replica cannot be resolved
		 * @return the number of replicas that could _not_ be resolved
		 */
		public int expandReplica(boolean throwerror) {
			if(this.node == null)
				throw new RuntimeException("Called expandReplicas wth this.node == null !");
			int rv = 0;
			for(XMLModelNode c : kids) {
				if(c.type == XMLModelNodeType.REPLICA) {
					if(c.node!=null) continue;
					if(c.path == null) {
						throw new RuntimeException("replicas must have a path attribute!");
					}
					try {
						c.node = (ModelNode) ModelNode.evaluatePath(this.node, c.path);
					} catch(Exception e) {
						if(throwerror)
							throw new RuntimeException(e);
						jprime.Console.out.println("\t\t"+e.getMessage());
						rv++;
						continue;
					}
					if(c.node == null) {
						if(throwerror)
							throw new RuntimeException("The path '"+c.path+"' from "+c.node.getUniqueName()+" is invalid!");
						jprime.Console.out.println("\t\tThe path '"+c.path+"' from "+c.node.getUniqueName()+" is invalid!");
						rv++;
						continue;
					}
					c.node = createReplica(this.node, c.node, c.name);
					//jprime.Console.out.println("\t\tExpanded replica "+ c.node.getUniqueName()+" --> "+rv);
				}
				//else jprime.Console.out.println("\t\tChild is not replica name="+ c.name+", type="+c.type);
			}
			return rv;
		}

		/**
		 * Create an attribute for a jprime model node
		 * @param dbid the id of the attribute name (see jprime.gen.ModelNodeVariable)
		 * @param value the value to assign the attribute
		 * @return the newly created ModelNodeVariable
		 */
		public static ModelNodeVariable createModelNodeVariable(int dbid, String value) {
			ModelNodeVariable v;
			Class<?> c= ModelNodeVariable.int2type(dbid);
			Class<?>[] argsClass = new Class<?>[] { int.class, String.class};
			Object[] args = new Object[] { dbid, value };
			try {
				Constructor<?> ctor = c.getConstructor(argsClass);
				v = (ModelNodeVariable)ctor.newInstance(args);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			return v;
		}

		/**
		 * Create a model node of type 'type' with name 'name'
		 * @param type the type of model node to create
		 * @param name the name of the model node
		 * @return the newly created model node
		 */
		public static IModelNode createModelNode(String type, String name) {
			IModelNode rv = null;
			try {
				Class<?> c= Class.forName("jprime."+type+"."+type);
				try {
					rv = (IModelNode)c.newInstance();
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				}
			} catch (ClassNotFoundException e) {
				try {
					Class<?> c = Class.forName("jprime.routing."+type);
					try {
						rv = (IModelNode)c.newInstance();
					} catch (IllegalAccessException e1) {
						throw new RuntimeException(e1);
					} catch (InstantiationException e1) {
						throw new RuntimeException(e1);
					}
				} catch (ClassNotFoundException e1) {
					throw new RuntimeException("Unknown node type "+type);
				}
			}
			if(rv instanceof ModelNode) {
				((ModelNode)rv).setName(name);
				//jprime.Console.out.println("\t\tCreated node "+rv.getUniqueName());
			}
			return rv;
		}

		/**
		 * Create a replica of node 'replicate' under the node 'parent' with name 'name'
		 * @param parent the parent node
		 * @param replicate the node to replicate
		 * @param name the name of the model node
		 * @return
		 */
		public static IModelNode createReplica(IModelNode parent, IModelNode replicate, String name) {
			//jprime.Console.out.println("\t\t\t\tExpanding child of "+parent.getUniqueName()+", replicating="+replicate.getUniqueName()+", name="+name);
			ModelNode rv = null;
			Class<? extends ModelNode> c= ((ModelNode)parent).getClass();
			Class<?>[] argsClass = new Class<?>[] { String.class,  findModelNodeInterface(replicate.getClass())};
			Object[] args = new Object[] { name, replicate };
			String mname = "create"+replicate.getClass().getSimpleName()+"Replica";
			//jprime.Console.out.println("Looking for "+parent.getClass().getName()+":"+mname);
			try {
				Method m = findMethod(c,mname, argsClass,true);
				rv = (ModelNode) m.invoke(parent, args);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
			rv.setName(name);
			//jprime.Console.out.println("\t\t\t\tCreated replica "+rv.getUniqueName());
			return rv;
		}

		/**
		 * Find the deepest interface extends IModelNode
		 *
		 * @param c the class where we should start looking
		 * @return the deepest interface extends IModelNode
		 */
		private static Class<?> findModelNodeInterface(Class<?> c) {
			if(c.isInterface())
				return c;
			for(Class<?> i : c.getInterfaces()) {
				if(i.getSimpleName().endsWith(c.getSimpleName()))
					return i;
			}
			return c;
		}

		/**
		 * Find the method 'methodName' of class 'c' with argument types 'argClass'. If the 'c' does not have it, the super classes are recursivly searched.
		 * @param c the class to search
		 * @param methodName the method to find
		 * @param argsClass the argument types of the method to find
		 * @param first whether this function has recursed on itself (if so, first == false)
		 * @return the method
		 * @throws NoSuchMethodException thrown if the method cannot be found.
		 */
		private static Method findMethod(Class<?> c, String methodName, Class<?>[] argsClass, boolean first) throws NoSuchMethodException {
			Method rv =null;
			if(c != null) {
				try {
					rv=c.getDeclaredMethod(methodName, argsClass);
				} catch (NoSuchMethodException e) {
					rv=findMethod(c.getSuperclass(), methodName, argsClass,false);
				}
			}
			if(rv == null && first) {
				String e = null;
				for(Class<?> t : argsClass) {
					if(e==null)
						e=methodName+"(";
					else
						e+=", ";
					e+=t.getSimpleName();
				}
				e+=") in class "+c.getName();
				throw new NoSuchMethodException(e);
			}
			return rv;
		}
	}

	private final Experiment exp;
	private XMLModelNode root;


	/**
	 * @param exp
	 * @param name
	 * @param file
	 */
	public XMLModelBuilder(Experiment exp, String file) {
		this(exp, new File(file));
	}
	
	/**
	 * @param exp
	 * @param file
	 */
	public XMLModelBuilder(Experiment exp, File file) {
		this.root = parseFile(file);
		if(root.kids.size()==0) {
			throw new RuntimeException("The model has no top-most network!");
		}
		else if(root.kids.size()>1) {
			throw new RuntimeException("this file contained an invalid experiment!");
		}
		else {
			this.exp=exp;
		}
	}


	/**
	 * Create the model. This can only be called once.
	 */
	public void createModel() {
		if(root == null) {
			throw new RuntimeException("The model has already been created!");
		}
		//create the top-most elements
		if(exp != null) {
			if(root.kids.size() != 1) {
				throw new RuntimeException("Experiments can only have 1 top-mos element!");
			}
		}
		for(XMLModelNode c : root.kids) {
			if(c.type != XMLModelNodeType.NODE) {
				throw new RuntimeException("top-most elements must be nodes!");
			}
			if(c.node_type == null) {
				throw new RuntimeException("top-most elements must have a node type!");
			}
			if(exp != null) {
				if(!c.node_type.equals("Net")) {
					throw new RuntimeException("Experiments only support top-most elements of type 'net'. Found "+c.node_type);
				}
				if(c.name == null) {
					c.node = exp.createTopNet("topnet");
				}
				else {
					c.node = exp.createTopNet(c.name);
				}
			}
			else {
				throw new RuntimeException("what happened?");
			}
		}

		//expand all regular nodes
		for(XMLModelNode c : root.kids) {
			c.expandNodes();
		}

		Map<Integer, Set<XMLModelNode>> map = new HashMap<Integer, Set<XMLModelNode>>();

		for(XMLModelNode c : root.kids) {
			c.findLinksAndReplicas(map, 0);
		}
		List<Integer> todel = new ArrayList<Integer>();
		int no_progress_count=0;
		int prev_remaining=-1;
		while(map.size()>0) {
			List<Integer> keys = new ArrayList<Integer>();
			keys.addAll(map.keySet());
			Collections.sort(keys);
			//jprime.Console.out.println("map.size()="+map.size()+", keys="+keys);
			int remaining=0;
			for(int idx = keys.size()-1; idx>=0; idx--) {
				Integer depth = keys.get(idx);
				Set<XMLModelNode> cur =map.get(depth);
				//jprime.Console.out.println("\tdepth:"+depth+", before:"+cur.size());
				Set<XMLModelNode> not_done = expandDepth(cur);
				//jprime.Console.out.println("\tdepth:"+depth+", after:"+not_done.size());
				remaining+=not_done.size();
				if(not_done.size()==0) {
					todel.add(depth);
					//jprime.Console.out.println("\tdeleteing depth "+depth+" because there are "+not_done.size()+" nodes left to expand");
				}
				//else jprime.Console.out.println("\tnot deleteing depth "+depth+" because there are "+not_done.size()+" nodes left to expand");
				map.put(depth, not_done);
			}
			for(Integer depth: todel) {
				map.remove(depth);
			}
			todel.clear();
			//jprime.Console.out.println("\tprev_remaining="+prev_remaining+", remaining="+remaining);
			if(prev_remaining != -1 && prev_remaining == remaining)
				no_progress_count++;
			prev_remaining = remaining;
			//jprime.Console.out.println("\tno_progress_count="+no_progress_count);
			if(no_progress_count>2) {
				break;
			}
		}
		//this is to print errors....
		for(Integer depth : map.keySet()) {
			for(XMLModelNode n : map.get(depth)) {
				try {
					n.expandLink(true);
				} catch(Exception e) {
					jprime.Console.err.println("1:"+e.getMessage());
				}
				try {
					n.expandReplica(true);
				} catch(Exception e) {
					jprime.Console.err.println("2:"+e.getMessage());
				}
			}
		}
		root=null;
		if(map.size()>0) {
			throw new RuntimeException("There were errors while constructing the model!");
		}
	}

	private Set<XMLModelNode> expandDepth(Set<XMLModelNode> cur) {
		Set<XMLModelNode> not_done = new HashSet<XMLModelBuilder.XMLModelNode>();
		//jprime.Console.out.println("\t\tExpanding replicas, cur="+cur.size()+", not_done="+not_done.size());
		int rv=0;
		for(XMLModelNode n:cur) {
			//String ack ="";
			//if(n.node != null) ack="["+n.node.getUniqueName()+"]";
			//jprime.Console.out.println("\t\t\tn="+n.name+"["+n.type+"]"+ack);
			rv=n.expandReplica(false);
			if(rv > 0) {
				not_done.add(n);
			}
			//jprime.Console.out.println("\t\t\t-- >"+rv);
		}
		//jprime.Console.out.println("\t\tExpanding links, cur="+cur.size()+", not_done="+not_done.size());
		for(XMLModelNode n:cur) {
			//String ack ="";
			//if(n.node != null) ack="["+n.node.getUniqueName()+"]";
			//jprime.Console.out.println("\t\t\tn="+n.name+"["+n.type+"]"+ack);
			rv=n.expandLink(false);
			if(rv > 0) {
				not_done.add(n);
			}
			//jprime.Console.out.println("\t\t\t-- >"+rv);
		}
		//jprime.Console.out.println("\t\tFindished, cur="+cur.size()+", not_done="+not_done.size());
		return not_done;
	}

	/**
	 * Parse the XML file into XMLModelNodes
	 * @param fileName the xml model
	 * @return the root(s) of the model
	 */
	private XMLModelNode parseFile(File file) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		NodeList nodeLst = doc.getChildNodes();
		List<XMLModelNode> roots=new ArrayList<XMLModelNode>();
		for (int s = 0; s < nodeLst.getLength(); s++) {
			Node n = nodeLst.item(s);
			if(n.getNodeType()==Node.ELEMENT_NODE) {
				roots.add(new XMLModelNode(n));
			}
		}
		if(roots.size()==0) {
			throw new RuntimeException("Found NO models in this file");
		}
		else if(roots.size()>1) {
			throw new RuntimeException("Found multiple models in this file");
		}
		return roots.get(0);
	}



	/**
	 * @return the exp
	 */
	public Experiment getExp() {
		return exp;
	}
}
