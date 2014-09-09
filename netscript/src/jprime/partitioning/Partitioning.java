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
import java.util.List;
import java.util.Map;


import jprime.IModelNode;
import jprime.Net.Net;
import jprime.partitioning.Partition.ProcessingNodeComparator;
import jprime.util.ComputeNode;
import jprime.util.IPAddressUtil;

/**
 * 
 * @author Nathanael Van Vorst
 */
public class Partitioning {
	public static class IPAlignment implements Comparable<Long> {
		public long low, high;
		public final int com_id;

		public IPAlignment(long low, long high, int com_id) {
			super();
			this.low = low;
			this.high = high;
			this.com_id = com_id;
		}
		public int compareTo(Long o) {
			if (low == o || high == o) {
				return 0;
			} else if (low <= o && high >= o) {
				return 0;
			} else if (o < low) {
				return 1;
			}
			return -1;
		}
		@Override
		public String toString() {
			return "("+IPAddressUtil.int2IP(low)+"["+low+"],"+IPAddressUtil.int2IP(high)+"["+high+"])-->"+com_id;
		}
	}

	public static class AlignmentResult {
		public final int part_id;
		public final int com_id;

		public AlignmentResult(int partId, int comId) {
			this.part_id = partId;
			this.com_id = comId;
		}
	}

	protected long dbid;
	protected int max_align_id;
	protected String part_str;
	protected Net rootNode;
	protected Map<Long, HostAlignment> hostAlignmentMap;
	protected Map<Integer, Partition> processingNodes;
	private List<AlignmentForwardingEntry> alignmentForwardingEntries;
	protected ArrayList<Partition> sortedProcessingNodes;
	protected ArrayList<IPAlignment> ipAlignments;
	protected HashMap<ComputeNode, Partition> computeNode2PartitionMap;

	/**
	 * for javax.persistence
	 */
	public Partitioning() {
		super();
	}

	/**
	 * @param part_str
	 *            the string which determines the partitioning
	 */
	public Partitioning(String part_str, Net rootNode) {
		this.rootNode = rootNode;
		this.part_str = part_str;
		this.max_align_id = 0;
		this.sortedProcessingNodes = null;
		this.hostAlignmentMap = new HashMap<Long, HostAlignment>();
		this.alignmentForwardingEntries = new ArrayList<AlignmentForwardingEntry>();
		this.ipAlignments = null;
		this.processingNodes = new HashMap<Integer, Partition>();
		this.computeNode2PartitionMap=new HashMap<ComputeNode, Partition>();
	}

	/**
	 * @param p
	 *            add p to this partitioning
	 */
	public void addProcessingNode(Partition p) {
		if (this.processingNodes.containsKey(p.getId())) {
			throw new RuntimeException("Duplicate processing node id "
					+ p.getId());
		}
		this.processingNodes.put(p.getId(), p);
		p.partitioning = this;
	}

	/**
	 * 
	 */
	public void createAlignments(ArrayList<Partition> pnode) {
		if (max_align_id == 0) {
			this.sortedProcessingNodes = pnode;
			for (Partition p : pnode) {
				max_align_id = p.createAlignments(this, max_align_id);
			}
		}
	}

	/**
	 * @return the partitions
	 */
	public Map<Integer, Partition> getProcessingNodes() {
		return this.processingNodes;
	}

	/**
	 * @return
	 */
	public Net getTopnet() {
		return rootNode;
	}

	public int getTotalNumberOfAlignments() {
		return max_align_id;
	}

	/**
	 * @return the partitioning string
	 */
	public String getPartitioningString() {
		return part_str;
	}

	/**
	 * @param alignment
	 * @return
	 */
	public Alignment findAlignment(int alignment) {
		// jprime.Console.out.println("START findAlignment("+alignment+")");
		assert (alignment >= 0 && alignment < max_align_id);
		Alignment rv = null;
		if (sortedProcessingNodes == null) {
			sortedProcessingNodes = new ArrayList<Partition>();
			sortedProcessingNodes.addAll(processingNodes.values());
			Collections.sort(sortedProcessingNodes,
					new ProcessingNodeComparator());
		}

		int min = 0, max = sortedProcessingNodes.size() - 1;
		while (rv == null) {
			int idx = (max + min) / 2;
			Partition p = sortedProcessingNodes.get(idx);
			// jprime.Console.out.println("\t1sortedProcessingNodes.size()="+sortedProcessingNodes.size()
			// +", idx="+idx+", range=["+p.start_align_id+","+(p.start_align_id+p.processorCount)+"]");
			if (p.hasAlignment(alignment)) {
				// jprime.Console.out.println("\t\tlooking for alignment "+alignment+", found it");
				// its here
				for (Alignment a : p.getAlginments()) {
					if (a.getAlignId() == alignment) {
						rv = a;
						break;
					}
				}
				if (rv == null)
					throw new RuntimeException("Expected alignment "
							+ alignment + " to be in processing node "
							+ p.getId() + " but it wasnt!");
			} else if (alignment < p.getStartAlignId()) {
				// jprime.Console.out.println("\t\tlooking for alignment "+alignment+", its in the lower half["+min+","+max+"]");
				// its in the lower half
				if (idx + 1 == min) {
					max = min;
				} else if (idx <= min) {
					idx = 0;
					for (Partition a : sortedProcessingNodes) {
						jprime.Console.out.println(idx + ":" + " [" + a.start_align_id
								+ "," + (a.start_align_id + a.processorCount)
								+ "]");
					}
					throw new RuntimeException("Couldnt find it!");
				} else if (max - min == 1) {
					max = min;
				} else {
					max -= (max - min) / 2;
				}
				// jprime.Console.out.println("\t\t\t2sortedProcessingNodes.size()="+sortedProcessingNodes.size()+", idx="+idx+" ["+min+","+max+"]");
			} else {
				// jprime.Console.out.println("\t\tlooking for alignment "+alignment+", its in the upper half["+min+","+max+"]");
				// its in the upper half
				if (idx + 1 == max) {
					min = max;
				} else if (idx >= max) {
					idx = 0;
					for (Partition a : sortedProcessingNodes) {
						jprime.Console.out.println(idx + ":" + " [" + a.start_align_id
								+ "," + (a.start_align_id + a.processorCount)
								+ "]");
						idx++;
					}
					throw new RuntimeException("Couldnt find it!");
				} else if (max - min == 1) {
					min = max;
				} else {
					min += (max - min) / 2;
				}
				// jprime.Console.out.println("\t\t\t3sortedProcessingNodes.size()="+sortedProcessingNodes.size()+", idx="+idx+" ["+min+","+max+"]");
			}
		}
		// jprime.Console.out.println("END findAlignment("+alignment+")");
		return rv;
	}

	/**
	 * @param m
	 * @return
	 */
	public int getHostAlignment(IModelNode m) {
		if (max_align_id == 0) {
			throw new RuntimeException("You need to partition the model first!");
		}
		if (hostAlignmentMap.size() == 0) {
			return 0;
		}
		Integer rv = hostAlignmentMap.get(m.getUID()).getAlignId();
		if (rv == null)
			throw new RuntimeException("unknown host uid " + m.getUID()
					+ ", name=" + m.getUniqueName());
		return rv.intValue();
	}

	/**
	 * @return
	 */
	public List<AlignmentForwardingEntry> getAlignmentForwardingEntries() {
		return alignmentForwardingEntries;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Partitioning.IPAlignment> getIPAlignments() {
		if (null == ipAlignments) {
			this.ipAlignments = new ArrayList<Partitioning.IPAlignment>();
			for (HostAlignment h : hostAlignmentMap.values()) {
				if (null != h.ips) {
					for (long l : h.ips) {
						//jprime.Console.out.println("***********************:");
						if (ipAlignments.size() == 0) {
							//jprime.Console.out.println("1ADDING(0)["+IPAddressAssignment.int2IP(l)+"["+l+"],"+IPAddressAssignment.int2IP(l)+"["+l+"]]");
							ipAlignments.add(new IPAlignment(l, l, h.align_id));
						}
						else {
							int f_idx = Collections.binarySearch(ipAlignments, l);
							final int idx = (f_idx<0)?((-f_idx)-1):f_idx;
							IPAlignment hit = (idx<ipAlignments.size())?ipAlignments.get(idx):null;
							IPAlignment right = (idx+1<ipAlignments.size())?ipAlignments.get(idx+1):null;
							
							/*{
								jprime.Console.out.println("size="+ipAlignments.size()+",f_idx="+f_idx+", idx="+idx+", hit="+hit+", right="+right);
								if(hit!=null)
									jprime.Console.out.println("\t("+idx+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]");
								if(right!=null)
									jprime.Console.out.println("\t("+(idx+1)+")["+IPAddressAssignment.int2IP(right.low)+"["+right.low+"],"+IPAddressAssignment.int2IP(right.high)+"["+right.high+"]]");
							}*/
							if(hit==null && right==null) {
								//jprime.Console.out.println("2ADDING("+ipAlignments.size()+")["+IPAddressAssignment.int2IP(l)+"["+l+"],"+IPAddressAssignment.int2IP(l)+"["+l+"]]");
								ipAlignments.add(new IPAlignment(l, l, h.align_id));
							}
							else if(hit != null && hit.compareTo(l)==0) {
								//its contained in the hit
								if(hit.com_id != h.align_id) {
									if(l == hit.low && l==hit.high) {
										//we can get messed up with the com ids while merging and splitting...
										IPAlignment n = new IPAlignment(l, l, h.align_id);
										ipAlignments.add(idx+1,n);
										ipAlignments.remove(idx);
									}
									else if(l == hit.low && l!=hit.high) {
										//jprime.Console.out.println("1SPLITTING("+idx+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]], l="+IPAddressAssignment.int2IP(l)+"["+l+"]");
										IPAlignment n = new IPAlignment(l, l, h.align_id);
										hit.low=l+1;
										ipAlignments.add(idx,n);
										/*{
											jprime.Console.out.println("\t("+(idx)+")["+IPAddressAssignment.int2IP(n.low)+","+IPAddressAssignment.int2IP(n.high)+"]");
											jprime.Console.out.println("\t("+(idx+1)+")["+IPAddressAssignment.int2IP(hit.low)+","+IPAddressAssignment.int2IP(hit.high)+"]");
										}*/
									}
									else if(l ==  hit.high) {
										//jprime.Console.out.println("2SPLITTING("+idx+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]], l="+IPAddressAssignment.int2IP(l)+"["+l+"]");
										IPAlignment n = new IPAlignment(l, l, h.align_id);
										hit.high=l-1;
										ipAlignments.add((idx+1),n);
										/*{
											jprime.Console.out.println("\t("+(idx)+")["+IPAddressAssignment.int2IP(hit.low)+","+IPAddressAssignment.int2IP(hit.high)+"]");
											jprime.Console.out.println("\t("+(idx+1)+")["+IPAddressAssignment.int2IP(n.low)+","+IPAddressAssignment.int2IP(n.high)+"]");
										}*/
									}
									else {
										//jprime.Console.out.println("3SPLITTING("+idx+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]], l="+IPAddressAssignment.int2IP(l)+"["+l+"]");
										IPAlignment nleft = new IPAlignment(hit.low, l-1, hit.com_id);
										IPAlignment new_hit= new IPAlignment(l, l, h.align_id);
										hit.low=l+1;
										ipAlignments.add(idx,new_hit);
										ipAlignments.add(idx,nleft);
										/*{
											jprime.Console.out.println("\t("+(idx)+")["+IPAddressAssignment.int2IP(nleft.low)+","+IPAddressAssignment.int2IP(nleft.high)+"]");
											jprime.Console.out.println("\t("+(idx+1)+")["+IPAddressAssignment.int2IP(new_hit.low)+","+IPAddressAssignment.int2IP(new_hit.high)+"]");
											jprime.Console.out.println("\t("+(idx+2)+")["+IPAddressAssignment.int2IP(hit.low)+","+IPAddressAssignment.int2IP(hit.high)+"]");
										}*/
									}
								}
							}
							else if(hit != null && hit.com_id == h.align_id) {
								//can merge
								if(hit.low > l && idx ==0) {
									//jprime.Console.out.println("1MERGERING("+(idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]], low="+IPAddressAssignment.int2IP(l)+"["+l+"]--"+h.align_id);
									hit.low=l;
								}
								else if(hit.high<l) {
									//jprime.Console.out.println("2MERGERING("+(idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]], high="+IPAddressAssignment.int2IP(l)+"["+l+"]--"+h.align_id);
									hit.high=l;
								}
								else {
									//jprime.Console.out.println("3ADDING("+ipAlignments.size()+")["+IPAddressAssignment.int2IP(l)+"["+l+"],"+IPAddressAssignment.int2IP(l)+"["+l+"]]");
									ipAlignments.add(idx,new IPAlignment(l, l, h.align_id));
								}
							}
							else if(right != null && right.compareTo(l)==0) {
								throw new RuntimeException("Should not happen!");
							}
							else if(right != null && right.com_id == h.align_id) {
								//can merge
								if(right.low > l && hit.high<l) {
									//jprime.Console.out.println("3MERGERING("+(idx+1)+")["+IPAddressAssignment.int2IP(right.low)+"["+right.low+"],"+IPAddressAssignment.int2IP(right.high)+"["+right.high+"]], low="+IPAddressAssignment.int2IP(l)+"["+l+"]--"+h.align_id);
									right.low=l;
								}
								else if(right.high<l) {
									//jprime.Console.out.println("4MERGERING("+(idx+1)+")["+IPAddressAssignment.int2IP(right.low)+"["+right.low+"],"+IPAddressAssignment.int2IP(right.high)+"["+right.high+"]], high="+IPAddressAssignment.int2IP(l)+"["+l+"]--"+h.align_id);
									right.high=l;
								}
								else {
									//jprime.Console.out.println("4ADDING("+ipAlignments.size()+")["+IPAddressAssignment.int2IP(l)+"["+l+"],"+IPAddressAssignment.int2IP(l)+"["+l+"]]");
									ipAlignments.add(idx,new IPAlignment(l, l, h.align_id));
								}
							}
							else {
								//just add it
								//jprime.Console.out.println("5ADDING("+(idx)+")["+IPAddressAssignment.int2IP(l)+"["+l+"],"+IPAddressAssignment.int2IP(l)+"["+l+"]]--"+h.align_id);
								ipAlignments.add(idx,new IPAlignment(l, l, h.align_id));
							}
							//merge resulting alignments....
							for(int i=-1;i<2;i++) { //-1 becase we can extend to the left, and +2 because we can split and add two more....
								f_idx=idx+i;
								if(f_idx<0) continue;
								while(f_idx<ipAlignments.size()-1) {
									hit=ipAlignments.get(f_idx);
									right=ipAlignments.get(f_idx+1);
									if(hit.com_id==right.com_id) {
										/*{
											jprime.Console.out.println("5MERGERING("+(f_idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]");
											jprime.Console.out.println("\twith("+(f_idx+1)+")["+IPAddressAssignment.int2IP(right.low)+"["+right.low+"],"+IPAddressAssignment.int2IP(right.high)+"["+right.high+"]]");
										}*/
										hit.high=right.high;
										//jprime.Console.out.println("\tto("+(f_idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]");
										ipAlignments.remove(f_idx+1);
									}
									else {
										/*{
											jprime.Console.out.println("CANT MERGE("+(f_idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]--"+hit.com_id);
											jprime.Console.out.println("\twith("+(f_idx+1)+")["+IPAddressAssignment.int2IP(right.low)+"["+right.low+"],"+IPAddressAssignment.int2IP(right.high)+"["+right.high+"]]--"+right.com_id);
										}*/
										break;
									}
									
								}
							}
							/*{
								jprime.Console.err.flush();
								jprime.Console.err.flush();
								for(f_idx=0;f_idx<ipAlignments.size();f_idx++) {
									hit = ipAlignments.get(f_idx);
									right = (f_idx+1<ipAlignments.size())?ipAlignments.get(f_idx+1):null;
									if(hit.low > hit.high){
										jprime.Console.out.println("ACK("+(f_idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]--"+hit.com_id);
										throw new RuntimeException("ACK");
									}
									if(right != null ) {
										if(hit.high > right.low){
											jprime.Console.out.println("ACK("+(f_idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]--"+hit.com_id);
											jprime.Console.out.println("ACK("+(f_idx+1)+")["+IPAddressAssignment.int2IP(right.low)+"["+right.low+"],"+IPAddressAssignment.int2IP(right.high)+"["+right.high+"]]--"+right.com_id);
											throw new RuntimeException("ACK");
										}
										if(hit.com_id == right.com_id){
											jprime.Console.out.println("ACK("+(f_idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]--"+hit.com_id);
											jprime.Console.out.println("ACK("+(f_idx+1)+")["+IPAddressAssignment.int2IP(right.low)+"["+right.low+"],"+IPAddressAssignment.int2IP(right.high)+"["+right.high+"]]--"+right.com_id);
											throw new RuntimeException("ACK");
										}
									}
								}
							}*/
						}
						/*{
							jprime.Console.out.println("***********IPS*********:");
							for(int f_idx=0;f_idx<ipAlignments.size();f_idx++) {
								IPAlignment hit = ipAlignments.get(f_idx);
								jprime.Console.out.println("\t("+(f_idx)+")["+IPAddressAssignment.int2IP(hit.low)+"["+hit.low+"],"+IPAddressAssignment.int2IP(hit.high)+"["+hit.high+"]]--"+hit.com_id);
							}
						}*/
					}
				}
			}
		}
		return ipAlignments;
	}

	public HashMap<ComputeNode, Partition> getComputeNode2PartitionMap() {
		return computeNode2PartitionMap;
	}
}
