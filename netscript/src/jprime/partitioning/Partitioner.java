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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import jprime.JMetis;
import jprime.Net.Net;
import jprime.partitioning.Partition.RemotePartitionRelationship;
import jprime.partitioning.PartitioningVisitor.PartitioningNode;
import jprime.partitioning.PartitioningVisitor.PortalPartitioningNodePair;
import jprime.util.ComputeNode;
import jprime.util.Portal;

/**
 * 
 * 
 * @author Nathanael Van Vorst
 */
public final class Partitioner {
	private static class Range implements Comparable<Range> {
		int min,max;
		public Range(int min, int max) {
			super();
			this.min = min;
			this.max = max;
		}
		public int compareTo(Range o) {
			if(min<=o.min && max>=o.min) {
				//the min value is in this range
				return 0;
			}
			if(min<=o.max&& max>=o.max) {
				//the max value is in this range
				return 0;
			}
			//place it based on the min value
			if(min<o.min) return -1;
			if(min>o.min) return 1;
			throw new RuntimeException("didnt execpt this, this="+this+", o="+o);
		}
		public String toString(){
			return "["+min+","+max+"]";
		}
		public static void insert(Range r, ArrayList<Range> list) {
			int idx = Collections.binarySearch(list, r);
			if(idx>0) {
				//we found a range which we overlap
				Range m = list.remove(idx);
				if(m.min<=r.min && m.max>=r.min) {
					//the min value is in this range
					if(m.max<r.max)
						m.max=r.max;
				}
				else if(m.min<=r.max && m.max>=r.max) {
					//the max value is in this range
					if(m.min>r.min)
						m.min=r.min;
				}
				insert(m,list);
			}
			else {
				if(list.size()==0) {
					list.add(r);
				}
				else {
					idx=((-idx)-1);
					int left=idx-1;
					if(left>=0 && idx<list.size()) {
						//being inserted between left and idx
						Range m = list.get(left);
						if(m.max+1==r.min) {
							//they can be merged
							m=list.remove(left);
							m.max=r.max;
							insert(m,list);
						}
						else {
							m = list.get(idx);
							if(r.max+1==m.min) {
								//they can be merged
								m=list.remove(idx);
								m.min=r.min;
								insert(m,list);
							}
							else {
								//can't be merged with neigbors
								list.add(idx,r);
							}
						}
					}
					else if(left>=0) {
						//at the tail
						Range m = list.get(left);
						if(m.max+1==r.min) {
							//they can be merged
							m=list.remove(left);
							m.max=r.max;
							insert(m,list);
						}
						else {
							//can't be merged with neigbors
							list.add(idx,r);
						}
					}
					else {
						//at the head
						Range m = list.get(idx);
						if(r.max+1==m.min) {
							//they can be merged
							m=list.remove(idx);
							m.min=r.min;
							insert(m,list);
						}
						else {
							//can't be merged with neigbors
							list.add(idx,r);
						}
					}
				}
			}

		}
	}
	protected final Partitioning partitioning;
	protected boolean done;
	
	/**
	 * @param partitioning_string
	 * @return
	 */
	protected static void parsePartString(Partitioning parting, String partitioning_string) {
		String[] toks = partitioning_string.split("::");
		if(toks.length!=2) {
			throw new RuntimeException("Invalid partition string '"+partitioning_string+"' ");
		}
		int host_count = Integer.parseInt(toks[0]);
		if(toks[1].contains("*")) {
			String[] h = toks[1].split(":");
			int procs=0;
			if(h.length==1) {
				procs=1;
			}
			else if(h.length==2) {
				procs=Integer.parseInt(h[1]);
			}
			else {
				throw new RuntimeException("Invalid host def '"+toks[1]+"' in partition string '"+partitioning_string+"'");
			}
			for(int i=1;i<=host_count;i++) {
				parting.addProcessingNode(new Partition(i, procs));
			}
		}
		else {
			String hosts[] = toks[1].split(",");
			if(host_count != hosts.length) {
				throw new RuntimeException("Invalid partition string '"+partitioning_string+"'. Expected "+host_count+" host defs but found "+hosts.length);
			}
			//create partitions
			for(String hdef : hosts) {
				String[] h = hdef.split(":");
				if(h.length==1) {
					parting.addProcessingNode(new Partition(Integer.parseInt(h[0]), 1));
				}
				else if(h.length==2) {
					parting.addProcessingNode(new Partition(Integer.parseInt(h[0]), Integer.parseInt(h[1])));
				}
				else {
					throw new RuntimeException("Invalid host def '"+hdef+"' in partition string '"+partitioning_string+"'");
				}
			}
		}
	}
	
	/**
	 * @param topnet
	 */
	public Partitioner(Net topnet, String partitioning_string) {
		this.done=false;
		if(null == partitioning_string || partitioning_string.length()==0) {
			partitioning_string="1::1:1";
		}
		partitioning=new Partitioning(partitioning_string,topnet);
		parsePartString(partitioning,partitioning_string);
	}
	
	/** 
	 * 
	 * @return
	 */
	public final Partitioning partition(Set<Portal> portals, List<ComputeNode> computeNodes) {
		if(!this.done) {
			this.done=true;
			ArrayList<Partition> pnodes = this.partitionModel(portals);
			HashMap<Integer, ComputeNode> p2c = new HashMap<Integer, ComputeNode>();

			if(pnodes.size()>0) {
				if(portals.size()>0) {
					for(Partition part : pnodes) {
						if(part.portals.size()>0) {
							ComputeNode cn = null;
							//make sure the portals all have the same compute node.....
							for(Portal p : part.portals) {
								if(cn == null) {
									cn = p.getNode();
									if(cn == null) {
										throw new RuntimeException("How did this happen?");
									}
								}
								else if(cn != p.getNode()) {
									throw new RuntimeException("How did this happen?");
								}
							}
							if(cn == null) {
								throw new RuntimeException("How did this happen?");
							}
							if(null != partitioning.computeNode2PartitionMap.put(cn, part)) {
								throw new RuntimeException("How did this happen?");
							}
							else {
								cn.setPartitionId(part.getId());
								p2c.put(part.getId(), cn);
							}
						}
					}
				}
				if(computeNodes.size()>0) {
					for(Partition p : pnodes) {
						if(!p2c.containsKey(p.getId())) {
							//System.out.println("partition "+p.getId()+"("+p+") does not have a compute node assigned.");
							//find a compute node without a parting
							for(ComputeNode c : computeNodes) {
								if(c.getPartitionId() == -1) {
									//System.out.println("\tassigned compute node "+c+"("+c.hashCode()+") to part "+p.getId());
									c.setPartitionId(p.getId());
									p2c.put(p.getId(),c);
									break;
								}
							}
						}
					}
					for(ComputeNode c : computeNodes) {
						if(c.getPartitionId()==-1) {
							throw new RuntimeException("Couldn't compute node "+c+" to a partiton!");
						}
					}
					for(Partition p : pnodes) {
						if(!p2c.containsKey(p.getId())) {
							throw new RuntimeException("Couldn't map partition "+p.getId()+" to a compute node!");
						}
						else {
							jprime.Console.out.println("Assigned partition "+p.getId()+" to compute node "+p2c.get(p.getId())+".");
						}
					}
				}				
				
				
				//Calculate shortest paths
				this.layoutProcessingNodePartitioning(pnodes);
				this.partitioning.createAlignments(pnodes);
				FloydWarshall fw = new FloydWarshall(this.partitioning.max_align_id);

				for(Partition p : pnodes) {
					//jprime.Console.out.println("The partition "+p.id+" has the following links to other nodes: (aligns=:"+p.alignments+")");
					HashMap<Integer,HashMap<Integer,List<String[]>>> linksto = new HashMap<Integer, HashMap<Integer,List<String[]>>>();
					for(Entry<Integer, HashMap<Integer, RemotePartitionRelationship>> e : p.links_to_remote_processing_nodes.entrySet()) {
						int local_align=-1;
						if(p.node_alignments.length==0)
							local_align=p.alignments.get(0).align_id;
						else
							local_align=p.node_alignments[e.getKey()];
						if(local_align!=-1) {
							for(Entry<Integer, RemotePartitionRelationship> f : e.getValue().entrySet()) {
								Partition remote = pnodes.get(f.getKey()-1);
								for(Integer i : f.getValue().nodes) {
									int remote_align = remote.getAlignId(i);
									if(remote_align != -1) {
										fw.addEdge(local_align, remote_align, f.getValue().relationship.getWeight());
										if(!linksto.containsKey(local_align)) {
											linksto.put(local_align, new HashMap<Integer,List<String[]>>());
										}
										if(!linksto.get(local_align).containsKey(remote_align))
											linksto.get(local_align).put(remote_align, new LinkedList<String[]>());
										linksto.get(local_align).get(remote_align).add(new String[]{""+remote.id,""+remote_align,f.getValue().relationship.toString()});
									}
									//else its a net
								}
							}
						}
						//else its a net
					}
					if(p.alignments.size()>0) {
						//add conns to commns within partition
						for(Alignment a : p.alignments) {
							if(!linksto.containsKey(a.align_id))
								linksto.put(a.align_id, new HashMap<Integer,List<String[]>>());
								
							for(Alignment b : p.alignments) {
								if(a==b)continue;
								if(!linksto.get(a.align_id).containsKey(b.align_id)) {
									linksto.get(a.align_id).put(b.align_id, new LinkedList<String[]>());
									linksto.get(a.align_id).get(b.align_id).add(new String[]{""+p.id,""+b.align_id,"<direct>"});
									fw.addEdge(a.align_id, b.align_id, 1);
								}
							}
						}
					}
					/*
					//DEBUG
					for(Entry<Integer, HashMap<Integer, List<String[]>>> v : linksto.entrySet()) {
						for(Entry<Integer, List<String[]>> vv : v.getValue().entrySet()) {
							for(String vvv[] : vv.getValue()) {
								jprime.Console.out.println("\t{part="+p.id+",com="+v.getKey()+"} --> {part="+vvv[0]+",com="+vvv[1]+"] link="+vvv[2]+"}");
							}
						}
					}
					*/
				}
				
				fw.calcShortestPaths();
				HashMap<Integer,HashMap<Integer,ArrayList<Range>>> hops = new HashMap<Integer, HashMap<Integer,ArrayList<Range>>>();
				for(int i =0;i<this.partitioning.max_align_id;i++) {
					for(int j =0;j<this.partitioning.max_align_id;j++) {
						if(i != j) {
							int nh=fw.getNextHop(i, j);
							HashMap<Integer,ArrayList<Range>> src2dst=null;
							if(hops.containsKey(nh)) {
								src2dst=hops.get(nh);
							}
							else {
								src2dst=new HashMap<Integer, ArrayList<Range>>();
								hops.put(nh, src2dst);
							}
							if(src2dst.containsKey(i)) {
								Range r = new Range(j, j);
								Range.insert(r,src2dst.get(i));
							}
							else {
								ArrayList<Range> t = new ArrayList<Range>();
								t.add(new Range(j, j));
								src2dst.put(i, t);
							}
						}
					}
				}
				for(Entry<Integer, HashMap<Integer, ArrayList<Range>>> nh : hops.entrySet()) {
					//jprime.Console.out.println("\tnext hop="+nh.getKey());
					for(Entry<Integer, ArrayList<Range>> src2dst : nh.getValue().entrySet()) {
						//jprime.Console.out.println("\t\tAlignment "+src2dst.getKey());
						for(Range r : src2dst.getValue()) {
							//jprime.Console.out.println("\t\t\tDst Range:"+r);
							//for(int j=r.min;j<=r.max;j++) jprime.Console.out.println("\t\t\t\tAlignment "+j+": "+fw.getShortestPath(src2dst.getKey(), j));
							if(r.min == nh.getKey() && r.max==nh.getKey()) {
								//we don't save trivial hops
								continue;
							}
							else {
								this.partitioning.getAlignmentForwardingEntries().add(
										new AlignmentForwardingEntry(
												src2dst.getKey().intValue(),
												r.min,
												r.max,
												nh.getKey(),
												this.partitioning));
							}
						}
					}
				}
			}
			else {
				throw new RuntimeException("The partitioning returned "+pnodes.size()+" partitions!");
			}
		}
		return partitioning;
	}
	
	/**
	 * partition network model into N partitions, where N is the number of processing nodes
	 */
	protected final ArrayList<Partition> partitionModel(Set<Portal> portals) {
		ArrayList<Partition> pnodes = new ArrayList<Partition>();
		pnodes.addAll(this.partitioning.processingNodes.values());
		Collections.sort(pnodes);
		if(pnodes.size()>1) {
			float[] partition_weights = new float[pnodes.size()];
			int pcount=0;
			boolean diff=false;
			int pcount_prev=-1;
			for(int i=0;i<pnodes.size();i++) {
				Partition p = pnodes.get(i);
				partition_weights[i]=p.processorCount;
				pcount+=p.processorCount;
				if(pcount_prev >0 )
					diff=diff||p.processorCount != pcount_prev;
				pcount_prev=p.processorCount;
			}
			for(int i=0;i<pnodes.size();i++) {
				partition_weights[i]/=pcount;
			}		
			PartitioningVisitor pv=new PartitioningVisitor(this.partitioning.getTopnet(), portals);
			
			JMetis.Partitioning rv=null;
			ArrayList<PartitioningNode> nodes = new ArrayList<PartitioningNode>();
			HashMap<Long, PartitioningNode> hosts = pv.getHosts();
			HashMap<Long, PartitioningNode> nets = pv.getNets();
			nodes.addAll(hosts.values());
			nodes.addAll(nets.values());
			Collections.sort(nodes);
			final JMetis graph = new JMetis(pv.getNodeCount(),pv.getEdgeCount(),nodes);
			if(diff) {
				rv = graph.partitionGraph(pnodes.size(),partition_weights);
			}
			else {
				rv = graph.partitionGraph(pnodes.size());
			}
			HashMap<ComputeNode, Set<PortalPartitioningNodePair>> collocated_portals = pv.getCollocated_portals();
			HashMap<ComputeNode, Integer> compute_node_parts = new HashMap<ComputeNode, Integer>();
			Set<Integer> used_compute_node_parts = new HashSet<Integer>();
			HashMap<Long,Set<PortalPartitioningNodePair>> traffic_portal_sets=new HashMap<Long, Set<PortalPartitioningNodePair>>();

			for(Set<PortalPartitioningNodePair> s : collocated_portals.values()) {
				for(PortalPartitioningNodePair p : s) {
					if(p.portal.getLinkedInterface() != null) {
						traffic_portal_sets.put(p.portal.getLinkedInterface().getParent().getUID(), s);
					}
					else {
						throw new RuntimeException("The linked interface is null!");
					}
				}
			}
			jprime.Console.out.println("Cut "+rv.edges_cut+" edges");
			for(int i=0;i<rv.parting.length;i++) {
				PartitioningNode p = nodes.get(i);
				if(traffic_portal_sets.containsKey(p.high_uid)) {
					//this is a traffic portal node...
					//we need to check if any node in its set already has a partition id
					//if it does, we need to override it to be the same
					Set<PortalPartitioningNodePair> s = traffic_portal_sets.get(p.high_uid);
					Portal portal=null;
					int collcated_part_id = -1;
					//find portal
					for(PortalPartitioningNodePair pp:s) {
						if(portal == null && pp.node == p) {
							portal=pp.portal;
							break;
						}
					}
					if(portal == null) {
						throw new RuntimeException("How did this happen?");
					}
					if(compute_node_parts.containsKey(portal.getNode())) {
						collcated_part_id=compute_node_parts.get(portal.getNode());
					}
					if(collcated_part_id == -1) {
						//we need to assign it
						//lets try to use what metis suggests
						collcated_part_id = rv.parting[i];
						if(used_compute_node_parts.contains(collcated_part_id)) {
							//metis tried to collcated portals which need to be on seperate compute nodes
							collcated_part_id = -1;
							for(int j=0; j< pnodes.size();j++) {
								if(!used_compute_node_parts.contains(j)) {
									collcated_part_id=j;
									break;
								}
							}
							if(collcated_part_id == -1) {
								throw new RuntimeException("How did this happen?");
							}
							jprime.Console.err.println("Warning: re-assigned partition of host uid="+
									p.high_uid+" from partition "+rv.parting[i]+
									" to "+collcated_part_id+" because metis tried to collocate portals that need to be on separte nodes.");
						}
						//else its free so we can use it;
						compute_node_parts.put(portal.getNode(),collcated_part_id);
						used_compute_node_parts.add(collcated_part_id);
						p.part_id=collcated_part_id;
					}
					else {
						//the portal must use collcated_part_id.
						if(collcated_part_id != rv.parting[i]) {
							jprime.Console.err.println("Warning: re-assigned partition of host uid="+
									p.high_uid+" from partition "+rv.parting[i]+
									" to "+collcated_part_id+" because metis tried to separate collocated nodes.");
						}
						p.part_id=collcated_part_id;
					}
					//jprime.Console.out.println("\t["+p.id+"]"+p.part_id+"("+p.high_uid+") -- (is traffic portal)");
					Partition part = pnodes.get(collcated_part_id);
					part.nodes.add(p);
					part.portals.add(portal);
				}
				else {
					p.part_id=rv.parting[i];
					//jprime.Console.out.println("\t["+p.id+"]"+p.part_id+"("+p.high_uid+")");
					pnodes.get(rv.parting[i]).nodes.add(p);
				}
			}
			for(Partition p: pnodes) {
				p.setupAlignProcessingNodes(rv.parting);
			}
		}
		else if(pnodes.size()==1 && pnodes.get(0).getProcessorCount()>1) {
			Partition p = pnodes.get(0);
			//don't need to consider portal links in this case.....
			PartitioningVisitor pv=new PartitioningVisitor(this.partitioning.getTopnet(), new HashSet<Portal>());
			for(PartitioningNode n : pv.getHosts().values()) {
				p.nodes.add(n);
			}
			for(PartitioningNode n : pv.getNets().values()) {
				p.nodes.add(n);
			}
			p.setupAlignProcessingNodes(1);
		}
		else if(pnodes.size()==1 && pnodes.get(0).getProcessorCount()==1) {
			Partition p = pnodes.get(0);
			Set<ComputeNode> ns = new HashSet<ComputeNode>();
			for(Portal pp :  portals) {
				ns.add(pp.getNode());
				p.portals.add(pp);
			}
			if(ns.size()!=1 && portals.size()>0) {
				throw new RuntimeException("How did this happen?");
			}
		}
		else {
			throw new RuntimeException("How did this happen?");
		}
		return pnodes;
	}

	/**
	 * 
	 * Depending on how the processing nodes are connected we may need to move 
	 * around the which nodes are assigned where to minimize the number of hops
	 * between pnodes which have nodes that communicate
	 * 
	 */
	protected final void layoutProcessingNodePartitioning(ArrayList<Partition> pnodes) {
		//jprime.Console.out.println("layoutProcessingNodePartitioning is currently a no-op!");
	}
	
	
}
