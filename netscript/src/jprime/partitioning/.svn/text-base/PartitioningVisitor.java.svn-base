package jprime.partitioning;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Host.Host;
import jprime.Host.HostReplica;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Link.Link;
import jprime.Link.LinkReplica;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Net.NetReplica;
import jprime.util.ChildList;
import jprime.util.ComputeNode;
import jprime.util.IPAddressUtil;
import jprime.util.Portal;

/**
 * 
 * @author Nathanael Van Vorst
 */
public class PartitioningVisitor {
	private final static int EMU_WEIGHT = 10;
	private static final double COLLCATED_PORTAL_LATENCY=0.0000001;
	public static class PortalPartitioningNodePair {
		public final Portal portal;
		public final PartitioningNode node;
		public PortalPartitioningNodePair(Portal portal, PartitioningNode node) {
			super();
			this.portal = portal;
			this.node = node;
		}
	}
	public static class PartitioningRelationship {
		/**
		 * metis seems to get very upset when the weights are too large. We use this to normalize all the weights.
		 */
		private static double freq_max=0;
		private double freq; //this is 1000/provided latency
		private final boolean is_protal_link;
		
		/**
		 * 
		 * @param latency its assumes this is in milliseconds (1/1000 secs)
		 * @param affinity
		 * @param pr
		 */
		public PartitioningRelationship(double latency,PartitioningVisitor pr) {
			this(latency,pr,false);
		}
		
		/**
		 * 
		 * @param latency its assumes this is in milliseconds (1/1000 secs)
		 * @param pr
		 */
		public PartitioningRelationship(double latency,PartitioningVisitor pr, boolean is_protal_link) {
			this.is_protal_link=is_protal_link;
			if(pr!=null)
				pr.incEdges();
			if(latency==0) {
				this.freq = Float.MAX_VALUE;
			}
			else {
				this.freq= 1000/latency;
			}
			if(freq > freq_max)
				freq_max=freq;
		}
		
		public boolean isPortalLink() {
			return is_protal_link;
		}
		/**
		 * used when we are aggregating links
		 * @param r
		 */
		public void mergeWeight(PartitioningRelationship r) {
			if(r.freq > this.freq) {
				this.freq=r.freq;
				if(this.freq > freq_max)
					freq_max=this.freq;
			}
		}
		
		/*
		 * This value will be on the interval [1001,1]
		 */
		public int getWeight() {
			//the +1 is so it is never zero
			return (int)((freq/freq_max)*10000.0)+1;
		}
		public double getFreq() {
			return freq;
		}
		public void setLatency(double latency) {
			if(latency==0) {
				this.freq = Float.MAX_VALUE;
			}
			else {
				this.freq= (float)(1000/latency);
			}
			if(freq > freq_max)
				freq_max=freq;
		}
		public String toString() {
			return "[PartitioningRelationship f="+freq+"("+freq_max+"),w="+getWeight()+"]";
		}
	}
	public static class PartitioningNode implements Comparable<PartitioningNode> {
		public final long low_uid, high_uid;
		public final boolean isNet;
		public HashMap<Integer,PartitioningRelationship> links;
		private int weight;
		public int id;
		public int part_id;
		public boolean is_emulated;
		public ArrayList<Long> ips;
		//public final IModelNode m;

		public PartitioningNode(IHost m, boolean is_emulated, int id, int w) {
			this.low_uid=m.getMinUID();
			this.high_uid=m.getUID();
			this.isNet=m instanceof INet;
			this.id=id;
			this.is_emulated=is_emulated;
			this.links = new HashMap<Integer, PartitioningVisitor.PartitioningRelationship>();
			this.part_id=-1;
			this.ips=new ArrayList<Long>();
			for(ModelNode c : m.getAllChildren()) {
				if(c instanceof IInterface) {
					if(((IInterface)c).getIpAddress() == null) {
						jprime.Console.out.println("WARNING: "+c.getUniqueName()+" has no ip address!");
					}
					else {
						ips.add(IPAddressUtil.ip2Int(((IInterface)c).getIpAddress().getValue()));
					}
				}
			}
			this.setWeight(w);
			//jprime.Console.out.println("PartitioningNode "+m.getUniqueName()+"("+high_uid+")=id("+id+")");
			//this.m=m;
		}

		public void setWeight(int w) {
			weight=w;
			if(w <=0) {
				throw new RuntimeException("Weights must be larger than 0!");
			}
		}
		public int getWeight() {
			return weight;
		}
		public String toString() {
			return "[PartitioningNode id="+id+", w="+getWeight()+"]";
		}
		public int compareTo(PartitioningNode o) {
	        if (id > o.id)
	            return +1;
	        else if (id < o.id)
	            return -1;
	        return 0;
		}
	}
	private int nodeCount, edgeCount, emuCount;
	private final HashMap<Long, PartitioningNode> hosts;
	private final HashMap<Long, PartitioningNode> nets;
	private final HashMap<ComputeNode,Set<PortalPartitioningNodePair>> collocated_portals;

	protected void incEdges() {
		edgeCount++;
	}
	
	public PartitioningVisitor(INet topnet, Set<Portal> portals) {
		this.nodeCount=0;
		this.edgeCount=0;
		this.emuCount=0;
		this.hosts=new HashMap<Long, PartitioningVisitor.PartitioningNode>();
		this.nets=new HashMap<Long, PartitioningVisitor.PartitioningNode>();
		this.collocated_portals = new HashMap<ComputeNode, Set<PortalPartitioningNodePair>>();
		this.visitNet(topnet);
		this.addPortalLinks(portals);
		//fix weights based on portal counts
		//we want each set of collocated portals to have the same weight to make it less likely metis will seperate them
		if(collocated_portals.size()>1) {
			long emu_weight = emuCount*EMU_WEIGHT;
			long sim_weight = nodeCount - emuCount;
			long portal_weight = (emu_weight+sim_weight)*10;
			long per_part_w = portal_weight/collocated_portals.size();
			for(Set<PortalPartitioningNodePair> s : collocated_portals.values()) {
				int per_node_w = (int)(per_part_w / s.size());
				for(PortalPartitioningNodePair p : s) {
					p.node.setWeight(per_node_w);
				}
			}
		}
	}
	
	private void addPortalLinks(Set<Portal> portals) {
		for(Portal p : portals) {
			if(p.getNode() == null) {
				jprime.Console.err.println("The portal "+p.getName()+" did not have a compute node attached!");
				continue;
			}
			else if(p.getLinkedInterface() == null) {
				jprime.Console.err.println("The portal "+p.getName()+" did not have an associated interface!");
				continue;
			}
			final IHost host = (IHost)p.getLinkedInterface().getParent();
			final PartitioningNode pn = hosts.get(host.getUID());
			if(pn == null) {
				throw new RuntimeException("The portal "+p.getName()+" is on an interface which was not in the model or was not attached to any link in the network.");
			}
			if(!collocated_portals.containsKey(p.getNode())) {
				collocated_portals.put(p.getNode(), new HashSet<PortalPartitioningNodePair>());
			}
			collocated_portals.get(p.getNode()).add(new PortalPartitioningNodePair(p, pn));
		}
		System.out.println("Collocated portals:");
		for(Entry<ComputeNode, Set<PortalPartitioningNodePair>> e : collocated_portals.entrySet()) {
			System.out.println("\t ComputeNode:"+e.getKey().toString());
			for(PortalPartitioningNodePair p : e.getValue()) {
				System.out.println("\t\t"+p.portal.getLinkedInterface().getUniqueName());
			}
		}
		
		for(Set<PortalPartitioningNodePair> s : collocated_portals.values())
		{
			if(s.size()<=1) continue;
			ArrayList<PartitioningNode> ifaces = new ArrayList<PartitioningNode>();
			for(PortalPartitioningNodePair p : s) {
				ifaces.add(p.node);
			}
			//create fake links between all collocated traffic portals with a very small latency so they wont (shouldn't) be cut
			for(int i=0;i<ifaces.size();i++) {
				PartitioningNode p1 = ifaces.get(i);
				for(int j=i+1;j<ifaces.size();j++) {
					PartitioningNode p2 = ifaces.get(j);
					if(p1.links.containsKey(p2.id)) {
						PartitioningRelationship r = p1.links.get(p2.id);
						//decrease the latency....
						r.setLatency(COLLCATED_PORTAL_LATENCY);
					}
					else {
						PartitioningRelationship r = new PartitioningRelationship(
								COLLCATED_PORTAL_LATENCY,
								this,
								true);
						p1.links.put(p2.id, r);
						p2.links.put(p1.id, r);
					}
				}
			}
		}
	}


	/**
	 * @return the collacted_portals
	 */
	public HashMap<ComputeNode, Set<PortalPartitioningNodePair>> getCollocated_portals() {
		return collocated_portals;
	}

	/**
	 * @param n
	 */
	public void visit(Host n) {
		visitHost(n);
	}

	/**
	 * @param n
	 */
	public void visit(HostReplica n) {
		visitHost(n);
	}
	/**
	 * @param n
	 */
	public void visit(Link n) {
		visitLink(n);
	}

	/**
	 * @param n
	 */
	public void visit(LinkReplica n) {
		visitLink(n);
	}

	/**
	 * @param n
	 */
	public void visit(Net n) {
		visitNet(n);
	}

	/**
	 * @param n
	 */
	public void visit(NetReplica n) {
		visitNet(n);
	}

	/**
	 * @param n
	 */
	private void visitHost(IHost n) {
		final boolean is_emulated=n.hasEmulationProtocol();
		
		PartitioningNode pn=null;
		if(hosts.containsKey(n.getUID())) {
			pn = hosts.get(n.getUID());			
			pn.setWeight(EMU_WEIGHT);
			pn.is_emulated=is_emulated;
		}
		else {
			pn =new PartitioningNode(n, is_emulated, nodeCount++, is_emulated?EMU_WEIGHT:1);
			if(null != hosts.put(n.getUID(),pn)) {
				throw new RuntimeException("hosts already had a node for uid "+n.getUID()+"!");
			}
		}
		if(is_emulated) {
			emuCount++;
		}
	}


	/**
	 * @param n
	 */
	private void visitLink(ILink n) {
		//jprime.Console.out.println("On link:"+n.getUniqueName());
		ArrayList<PartitioningNode> ifaces = new ArrayList<PartitioningNode>();
		ArrayList<IModelNode> ack = new ArrayList<IModelNode>();
		{
			ChildList<IBaseInterfaceAlias>.ChildIterator nics = n.getAttachments().enumerate();
			while(nics.hasNext()) {
				IModelNode m = nics.next().deference().getParent();
				//jprime.Console.out.println("\ton iface "+m.getUniqueName());
				ack.add(m);
				if(hosts.containsKey(m.getUID())) {
					ifaces.add(hosts.get(m.getUID()));
				}
				else {
					final boolean is_emu = ((IHost)m).hasEmulationProtocol();
					PartitioningNode pn = new PartitioningNode((IHost)m,is_emu,nodeCount++, is_emu?EMU_WEIGHT:1);
					ifaces.add(pn);
					if(null != hosts.put(m.getUID(),pn)) {
						throw new RuntimeException("hosts already had a node for uid "+m.getUID()+"!");
					}
				}
			}
		}
		for(int i=0;i<ifaces.size();i++) {
			PartitioningNode p1 = ifaces.get(i);
			//jprime.Console.out.println("\tp1="+p1.m.getUniqueName());
			for(int j=i+1;j<ifaces.size();j++) {
				PartitioningNode p2 = ifaces.get(j);
				if(p1.links.containsKey(p2.id)) {
					PartitioningRelationship r = p1.links.get(p2.id);
					jprime.Console.out.println("There is already a partitioning relationship between "+p1+" and "+p2+", p1==p2?"+(p1==p2)+", r="+r+", i="+ack.get(i).getUniqueName()+", j="+ack.get(j).getUniqueName());
					//throw new RuntimeException("didn't expect this....");
				}
				else {
					//XXX currently I don't handle default values so we can see nulls!
					PartitioningRelationship r = new PartitioningRelationship(
							(n.getDelay()==null?1:n.getDelay().getValue()),
							this);
					//jprime.Console.out.println("PartitioningRelationship ["+p1.id+","+p2.id+"]->("+r.getFreq()+","+r.getAffinity()+","+r.getWeight()+")");
					p1.links.put(p2.id, r);
					p2.links.put(p1.id, r);
				}
			}
		}
	}

	/**
	 * @param n
	 */
	private void visitNet(INet n) {
		//visit kids
		for(ModelNode c : n.getAllChildren())
			c.accept(this);
	}

	/**
	 * @param n
	 */
	public void visit(ModelNode n) {
		//no op
	}

	public HashMap<Long, PartitioningNode> getHosts() {
		return hosts;
	}

	public HashMap<Long, PartitioningNode> getNets() {
		return nets;
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
	public int getEdgeCount() {
		return edgeCount;
	}
}
