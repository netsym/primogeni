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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import jprime.JMetis;
import jprime.partitioning.IUIDRange.UIDRangeListManip;
import jprime.partitioning.PartitioningVisitor.PartitioningNode;
import jprime.partitioning.PartitioningVisitor.PartitioningRelationship;
import jprime.util.Portal;

/**
 * @author Nathanael Van Vorst
 */
public class Partition implements Comparable<Partition>{
	public static class RemotePartitionRelationship {
		public final PartitioningVisitor.PartitioningRelationship relationship;
		public final TreeSet<Integer> nodes;
		public RemotePartitionRelationship(PartitioningVisitor.PartitioningRelationship r) {
			this.relationship = new PartitioningVisitor.PartitioningRelationship(r.getFreq(),null);
			this.nodes= new TreeSet<Integer>();
		}
	}
	public static class ProcessingNodeComparator implements Comparator<Partition>{
		public int compare(Partition n1, Partition n2) {
			if (n1.start_align_id > n2.start_align_id){
				return +1;
			}else if (n1.start_align_id < n2.start_align_id){
				return -1;
			}else{
				return 0;
			}
		}
	}

	protected final int id;
	protected Partitioning partitioning;
	protected int processorCount;
	protected int start_align_id;
	protected List<ProcessingNodeLink> left_links;
	protected List<ProcessingNodeLink> right_links;
	protected List<Alignment> alignments;
	protected ArrayList<PartitioningNode> nodes;
	protected int[] node_alignments;
	protected Set<Portal> portals = new HashSet<Portal>();
	
	//a map from local node ids to partitions which the local node has a connection to
	protected HashMap<Integer,HashMap<Integer,RemotePartitionRelationship>> links_to_remote_processing_nodes;
	protected boolean is_emulated;
	protected int edge_count;
	protected List<UIDRange> uidRanges;
	protected HashMap<Integer,Integer> global_to_local_id;
	private static final UIDRangeListManip<UIDRange> range_manip=new UIDRangeListManip<UIDRange>();

	protected Partition(int id, int processorCount) {
		this.id=id;
		this.processorCount=processorCount;
		this.alignments=new ArrayList<Alignment>();
		this.start_align_id=-1;
		this.nodes = new ArrayList<PartitioningNode>();
		this.links_to_remote_processing_nodes=new HashMap<Integer, HashMap<Integer,RemotePartitionRelationship>>();
		this.is_emulated=false;
		this.edge_count=0;
		this.uidRanges=null;
		this.global_to_local_id=new HashMap<Integer, Integer>();
		this.node_alignments=null;
		//Database.collect(this);
	}

	/**
	 * @return
	 */
	public LinkIterator getLinks() {
		return new LinkIterator(this);
	}

	/**
	 * @return
	 */
	public List<ProcessingNodeLink> getLeftLinks() {
		return left_links;
	}

	/**
	 * @return
	 */
	public List<ProcessingNodeLink> getRightLinks() {
		return right_links;
	}

	/**
	 * @return
	 */
	public List<Alignment> getAlginments() {
		return alignments;
	}

	/**
	 * @return
	 */
	public Partitioning getPartitioning() {
		return partitioning;
	}


	/**
	 * @param alignment
	 * @return
	 */
	public boolean hasAlignment(int alignment) {
		//jprime.Console.out.println("start_align_id["+start_align_id+"] <= alignment["+alignment+"] && alignment < (start_align_id["+start_align_id+"]+processorCount["+processorCount+"]) -->"+(start_align_id<= alignment && alignment < (start_align_id+processorCount)));
		return start_align_id<= alignment && alignment < (start_align_id+processorCount);
	}

	/**
	 * @param alignments
	 * @return
	 */
	public int countContainedAlignments(Set<Integer> alignments) {
		if(getAlginments().size()==0)
			return alignments.size();
		int rv =0;
		for(int i : alignments)
			if(hasAlignment(i)) rv++;
		return rv;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public int getStartAlignId() {
		return start_align_id;
	}

	/**
	 * @return
	 */
	public int getProcessorCount() {
		return processorCount;
	}

	/**
	 * @param start_id
	 */
	public int createAlignments(Partitioning p, int start_id) {
		if(alignments.size()>0) {
			throw new RuntimeException("the alignments have already been created!");
		}
		this.start_align_id=start_id;
		this.partition(p);
		return start_align_id+processorCount;
	}

	/**
	 * @return
	 */
	public List<UIDRange> getUIDRanges() {
		if(uidRanges==null) {
			uidRanges = new ArrayList<UIDRange>();
			for(Alignment a : alignments) {
				for(IUIDRange u : a.getUIDRanges())
					range_manip.addUIDRange(u.getLow(), u.getHigh(), uidRanges, null);
			}
		}
		return uidRanges;
	}

	/**
	 * @param node2ProcessingId
	 */
	public void setupAlignProcessingNodes(int id) {
		//jprime.Console.out.println("setupAlignProcessingNodes for partition "+this.id+", total nodes:"+nodes.size());
		Collections.sort(nodes);
		for(int i=0; i<nodes.size();i++) {
			PartitioningNode p=nodes.get(i);
			global_to_local_id.put(p.id, i);
			p.id=id;
			if(p.is_emulated) {
				is_emulated=true;
			}
		}
		{
			Set<PartitioningRelationship> links=new HashSet<PartitioningVisitor.PartitioningRelationship>();
			for(PartitioningNode p : nodes) {
				HashMap<Integer,PartitioningRelationship> old_links=p.links;
				p.links=new HashMap<Integer,PartitioningRelationship>();
				for(Entry<Integer, PartitioningRelationship> e : old_links.entrySet()) {
					if(e.getValue().isPortalLink()) continue;
					if(global_to_local_id.containsKey(e.getKey())) {
						//jprime.Console.out.println("\tfound link to node within this partition, node_id="+e.getKey());
						p.links.put(global_to_local_id.get(e.getKey()), e.getValue());
						links.add(e.getValue());
					}
					else {
						throw new RuntimeException("Should happen");
					}
				}
				this.edge_count=links.size();
			}
		}
	}

	
	/**
	 * @param node2ProcessingId
	 */
	public void setupAlignProcessingNodes(int[] node2PartitionId) {
		//jprime.Console.out.println("setupAlignProcessingNodes for partition "+this.id+", total nodes:"+nodes.size());
		Collections.sort(nodes);
		//int[] local2global = new int[nodes.size()];
		for(int i=0; i<nodes.size();i++) {
			PartitioningNode p=nodes.get(i);
			global_to_local_id.put(p.id, i);
			//jprime.Console.out.println("\t["+p.id+"]="+i+", "+p.m.getUniqueName());
			//local2global[i]=p.id;
			p.id=i;
			if(p.is_emulated) {
				is_emulated=true;
			}
		}
		//jprime.Console.out.println("Moving links to remote partitions to links_to_remote_processing_nodes");
		{ //remove links to remote processing nodes and add them to links_to_remote_processing_nodes
			Set<PartitioningRelationship> links=new HashSet<PartitioningVisitor.PartitioningRelationship>();
			for(PartitioningNode p : nodes) {
				//jprime.Console.out.println("On Node "+p.id+", gid="+local2global[p.id]);
				HashMap<Integer,PartitioningRelationship> old_links=p.links;
				p.links=new HashMap<Integer,PartitioningRelationship>();
				for(Entry<Integer, PartitioningRelationship> e : old_links.entrySet()) {
					if(e.getValue().isPortalLink()) continue;
					if(global_to_local_id.containsKey(e.getKey())) {
						//jprime.Console.out.println("\tfound link to node within this partition, node_id="+e.getKey());
						p.links.put(global_to_local_id.get(e.getKey()), e.getValue());
						links.add(e.getValue());
					}
					else {
						HashMap<Integer,RemotePartitionRelationship> t1 = null;
						Integer remote_id = node2PartitionId[e.getKey().intValue()]+1;
						if(remote_id == this.id) {
							//jprime.Console.out.println("\tfound link to a net, node_id="+e.getKey()+", remote_id="+remote_id+", this.id="+this.id);
							continue; //this must be a net since it links to me
						}
						/*jprime.Console.out.println("\tfound link to a remote node, node_id="+e.getKey()+", remote_id="+remote_id+", this.id="+this.id);
						jprime.Console.out.println("\t\t[before]links to remote parts=");
						for(Entry<Integer, HashMap<Integer, RemotePartitionRelationship>> pid : links_to_remote_processing_nodes.entrySet()) {
							jprime.Console.out.println("\t\t\tnode "+pid.getKey()+"["+local2global[pid.getKey().intValue()]+"]-->");
							for(Entry<Integer, RemotePartitionRelationship> r : pid.getValue().entrySet()) {
								jprime.Console.out.println("\t\t\t\tpartition "+r.getKey()+"-->"+r.getValue().nodes);
							}
						}*/
						if(!links_to_remote_processing_nodes.containsKey(p.id)) {
							t1 = new HashMap<Integer, Partition.RemotePartitionRelationship>();
							links_to_remote_processing_nodes.put(p.id,t1);
						}
						else {
							t1=links_to_remote_processing_nodes.get(p.id);
						}
						if(!t1.containsKey(remote_id)) {
							RemotePartitionRelationship temp = new RemotePartitionRelationship(e.getValue());
							t1.put(remote_id,temp);
							temp.nodes.add(e.getKey());
						}
						else {
							RemotePartitionRelationship temp = t1.get(remote_id);
							temp.relationship.mergeWeight(e.getValue());
							temp.nodes.add(e.getKey());
						}
						/*jprime.Console.out.println("\t\t[after]links to remote parts=");
						for(Entry<Integer, HashMap<Integer, RemotePartitionRelationship>> pid : links_to_remote_processing_nodes.entrySet()) {
							jprime.Console.out.println("\t\t\tnode "+pid.getKey()+"-->");
							for(Entry<Integer, RemotePartitionRelationship> r : pid.getValue().entrySet()) {
								jprime.Console.out.println("\t\t\t\tpartition "+r.getKey()+"-->"+r.getValue().nodes);
							}
						}*/
						
					}
				}
				this.edge_count=links.size();
			}
		}
	}

	protected int getAlignId(int node_global_id) {
		if(node_alignments==null)
			throw new RuntimeException("need to partition this partition first!");
		Integer local_id = this.global_to_local_id.get(node_global_id);
		if(local_id == null)
			throw new RuntimeException("Partition "+this.id+" does have node with id "+node_global_id+"!\nNode ids="+global_to_local_id.keySet());
		if(node_alignments.length==0) {
			return alignments.get(0).align_id;
		}
		return node_alignments[local_id.intValue()];
	}

	/**
	 * partition the nodes assigned to this processing node
	 * @return the number of alignments
	 */
	public void partition(Partitioning partitioning) {
		if(node_alignments==null) {
			node_alignments=new int[nodes.size()];
			Arrays.fill(node_alignments, -1);
			if(is_emulated) {
				this.processorCount=1;
			}
			for(int i=start_align_id;i<start_align_id+processorCount;i++) {
				alignments.add(new Alignment(i, this));
				//jprime.Console.out.println("Created alignment "+i+" on processing node "+id);
			}
			JMetis.Partitioning rv=null;
			if(nodes.size()>0 && alignments.size()>1) {
				if(this.edge_count==0) {
					throw new RuntimeException("Should _NOT_ happen!");
				}
				else {
					//jprime.Console.out.println("Using metis! this.edge_count="+this.edge_count+", nodes="+nodes.size());
					rv=(new JMetis(nodes.size(),edge_count,nodes)).partitionGraph(processorCount);
				}
				//jprime.Console.out.println("Cut "+rv.edges_cut+" edges, rv.parting.length="+rv.parting.length);
				for(int i=0;i<rv.parting.length;i++) {
					//jprime.Console.out.println("\t["+(i+1)+"]"+rv.parting[i]);
					PartitioningNode p = nodes.get(i);
					p.links.clear();
					if(!p.isNet) {
						Alignment a = alignments.get(rv.parting[i]);
						a.addUIDRanges(p.low_uid,p.high_uid);
						partitioning.hostAlignmentMap.put(p.high_uid, new HostAlignment(p.high_uid,p.ips,a.align_id,partitioning));
						node_alignments[i]=a.align_id;
					}
				}
			}
			else if(nodes.size()>0) {
				//jprime.Console.out.println("There is only one processor on this node...!");
				//there is only one processor here
				Alignment a = alignments.get(0);
				for(int i=0;i<nodes.size();i++) {
					PartitioningNode p = nodes.get(i);
					p.links.clear();
					if(!p.isNet) {
						a.addUIDRanges(p.low_uid,p.high_uid);
						partitioning.hostAlignmentMap.put(p.high_uid, new HostAlignment(p.high_uid,p.ips,a.align_id,partitioning));
						node_alignments[i]=a.align_id;
					}
				}
			}
			else {
				//jprime.Console.out.println("We are assuming there is only one alignment! nodes.size()="+nodes.size()+", alignments.size()="+alignments.size());
				Alignment a = alignments.get(0);
				a.addUIDRanges(partitioning.getTopnet().getMinUID(),partitioning.getTopnet().getUID());
			}
			nodes.clear();
		}
	}

	public int compareTo(Partition o) {
		if(this.id < o.id) return -1;
		if(this.id > o.id) return 1;
		return 0;
	}
}
