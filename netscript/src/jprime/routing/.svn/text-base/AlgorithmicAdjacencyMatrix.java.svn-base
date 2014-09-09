package jprime.routing;

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

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import jprime.IModelNode;
import jprime.BaseInterface.IBaseInterface;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.RouteTable.IRouteTable;
import jprime.routing.BGPAdjacencyMatrix.ModelNodeIdxPair;
import jprime.routing.IRouteEntry.RouteEntryComparator;
import jprime.routing.ShortestPathAdjacencyMatrix.SimpleAdjacencyEntry;
import jprime.util.ChildList;
import jprime.util.PersistentRouteEntryList;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 *
 */
public class AlgorithmicAdjacencyMatrix extends BaseAdjacencyMatrix implements IAdjacencyMatrix {
	
	public static final class ParentNodePair {
		public final IModelNode parent, node;
		public ParentNodePair(IModelNode parent, IModelNode node) {
			this.parent=parent;
			this.node=node;
		}
	}

	private long[] P;
	private Vector<Long> nodes;
	private Map<Long, ModelNodeIdxPair> matrix;
	
	/**
	 * 
	 */
	public AlgorithmicAdjacencyMatrix(IModelNode anchor) {
		super(anchor);
		this.P=null;
		this.nodes = new Vector<Long>();	
		this.matrix=new HashMap<Long, ModelNodeIdxPair>();
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#getAdjListForNode(jprime.IModelNode)
	 */
	public Map<Long, IAdjacencyEntry> getAdjListForNode(IModelNode node) {
		if(!matrix.containsKey(node.getRank(anchor))) {
			matrix.put(node.getRank(anchor), new ModelNodeIdxPair(node));
		}
		return matrix.get(node.getRank(anchor)).adjList;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#addEdge(jprime.IModelNode, jprime.IModelNode)
	 */
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#addEdge(jprime.IModelNode, jprime.IModelNode)
	 */
	public void addEdge(IModelNode left, IModelNode right) {
		addEdge(left,right,1);
	}

	/**
	 * @param left
	 * @param right
	 * @param weight
	 */
	public void addEdge(IModelNode left, IModelNode right, int cost) {
		getAdjListForNode(right).put(left.getRank(anchor),new SimpleAdjacencyEntry(cost));
		getAdjListForNode(left).put(right.getRank(anchor),new SimpleAdjacencyEntry(cost));
		
		getAdjListForNode(right).put(right.getRank(anchor),new SimpleAdjacencyEntry(0));
		getAdjListForNode(left).put(left.getRank(anchor),new SimpleAdjacencyEntry(0));
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.AdjacencyMatrix#computeShortestPath()
	 */
	public RouteCounts computeShortestPath(INet net) {
		IRouteTable tbl = net.getRoutingSphere().getRouteTable();
		PersistentRouteEntryList route_entries = tbl.getPermRouteEntries();
		RouteEntryComparator rc =new RouteEntryComparator(false);
		int merged = 0;
		
		Map<Long, BigInteger> nodeAddressPair=new HashMap<Long,BigInteger>();
		Map<BigInteger, Long> nodeAddressPair_revmap=new HashMap<BigInteger, Long>();
			
		//tree mapping
		long k=0;
		Long root=null;
		for(long l : matrix.keySet()) {
			nodes.add(l);
			//choose the node that has the maximum degree as the root, and use the maximum degree as k.
			if(matrix.get(l).adjList.size()>k){
				k=matrix.get(l).adjList.size();
				root=l;
			}
		}
		k--;
		//jprime.Console.out.println("root is: "+matrix.get(root).node.getUniqueName()+", k="+k);
		Queue<ParentNodePair> queue= new LinkedList<ParentNodePair>();
		queue.add(new ParentNodePair(null, matrix.get(root).node));
		Set<Long> enqueued=new HashSet<Long>();
		Set<Long> unassigned=new HashSet<Long>();
		enqueued.add(root);
		nodeAddressPair.put(root, BigInteger.valueOf(0));
		nodeAddressPair_revmap.put(BigInteger.valueOf(0),root);
		//jprime.Console.out.println("nodeAddressPair::  "+root+"<-->"+0);

		while(queue.size()!=0){
			//pick one node x from the queue
			ParentNodePair pn = (ParentNodePair)queue.poll();
			long node=pn.node.getUID();
			//get all the nodes directly connected to the node x 
			for(Long n : matrix.get(node).adjList.keySet()){
				unassigned.add(n);
			}
			//Remove the nodes that have already been assigned addresses in the tree.
			unassigned.removeAll(enqueued);
			//Assign address for node's children
			long offset=1;
			for(Long c : unassigned){
				nodeAddressPair.put(c, nodeAddressPair.get(node).multiply(BigInteger.valueOf(k)).add(BigInteger.valueOf(offset)));
				nodeAddressPair_revmap.put(nodeAddressPair.get(node).multiply(BigInteger.valueOf(k)).add(BigInteger.valueOf(offset)),c);
				//jprime.Console.out.println("nodeAddressPair::  "+matrix.get(c).node.getUID()+"<-->"+nodeAddressPair.get(node).multiply(BigInteger.valueOf(k)).add(BigInteger.valueOf(offset)));

				offset++;
				queue.add(new ParentNodePair(matrix.get(node).node, matrix.get(c).node));
			}
			enqueued.addAll(unassigned);
		}
		
		//calculate the next hop for all pairs	
		
		for(int i=0; i<nodes.size(); i++){
			//Initialize the next hops on the paths matrix
			P = new long[matrix.size()];
			Arrays.fill(P, -1);
			Long start = nodes.elementAt(i);
			//jprime.Console.out.println("start = nodes["+i+"]="+start);
			
			Long src_node = nodes.elementAt(i); //node is the src
			IBaseInterface srcIface = (IBaseInterface) matrix.get(src_node).node; 
			//check if the srcIface is the edge interface of my child spheres
			final boolean srcIsEdgeIface = net.isEdgeIfaceOfChildSphere((IInterface)srcIface);
			IHost srcNode = (IHost) srcIface.getParent(); //route entry items 1,2: srcNode
			// calculate number of bits
			int numOfChildren = (int)srcNode.getSize();
			int numOfBits = 0;
			if (numOfChildren <= 2) {
				numOfBits = 1;
			} else {
				numOfBits = (int) Math.ceil(Math.log(numOfChildren) / Math.log(2));
			} //route entry item 7: numOfBits
			
			ILink link = null;
			INet cur_net = (INet) srcIface.getParent().getParent(); 
			// find attached link for the src interface
			while (cur_net != null && link == null) {
				link = findAttachedLink(cur_net, srcIface);
				cur_net = (INet) cur_net.getParent();
			}			
			
			//for(int j=0; j<nodes.size(); j++){
			for(int j=0; j<targets.size(); j++) {
				//Long destination = nodes.elementAt(j);
				Long destination = targets.elementAt(j);
				//jprime.Console.out.println("destination = targets["+j+"]="+destination);
				IBaseInterface dstIface = (IBaseInterface) matrix.get(destination).node; //route entry items 3,4: dstIface		
				
				boolean pathExist=false;
				if(start.compareTo(destination)!=0){
					if(nodeAddressPair.containsKey(destination)){
							pathExist=true;
					}
					if(!pathExist){
						P[j]=0; //there is no path from i to j.
					}else{
						//if the start and the destination are in the same branch, go backward from the destination to the start to find the next hop.	
						//jprime.Console.out.println("i="+matrix.get(nodes.elementAt(i)).node.getUID()+", j="+matrix.get(targets.elementAt(j)).node.getUID());
						
						while(nodeAddressPair.get(destination).compareTo(BigInteger.valueOf(0))==1){
							Long parent=null;
							BigInteger parentAddr=(nodeAddressPair.get(destination).subtract(BigInteger.valueOf(1))).divide(BigInteger.valueOf(k));
							//if(matrix.get(nodes.elementAt(i)).node.getParent().getUID()==8038 && matrix.get(targets.elementAt(j)).node.getParent().getUID()==7982){
								//jprime.Console.out.println("Looking for parent, parentAddr =("+nodeAddressPair.get(destination)+"-1)/"+k+"="+parentAddr);
								//jprime.Console.out.println("\tnodeAddressPair_revmap.containsKey("+parentAddr+")-->"+(nodeAddressPair_revmap.containsKey(parentAddr)));
							//}
							if(nodeAddressPair_revmap.containsKey(parentAddr)) {
	 							parent = nodeAddressPair_revmap.get(parentAddr);
							}
							else {
								throw new RuntimeException("Can't find parent!");
							}
							if(parent.compareTo(start)==0){
								P[j]=destination;
								break;
							}
							destination=parent;
							//jprime.Console.out.println("destination = parent = "+parent);
						}
						//if the start and the destination are not in one branch, the next hop is the start's parent.
						if(P[j]==-1){
							BigInteger lookingfor = (nodeAddressPair.get(start).subtract(BigInteger.valueOf(1))).divide(BigInteger.valueOf(k));
							if(nodeAddressPair_revmap.containsKey(lookingfor)) {
								P[j] = nodeAddressPair_revmap.get(lookingfor);
							}
						}
					}		
				}else{
					P[j]=destination;
				}
				// Shortest path calculation is done! 
				// Build the route entry for src i to dst j according to P (next hop) and D (distance) 
				
				// set cost to be 0
				int cost = 0;
					
				// get next hop from P
				IBaseInterface nexthopIface = null;
				IHost nexthopNode = null;			
				long nexthopId = -1;
				long outboundIface = 0;
				long owningHost = 0;
				if (matrix.get(P[j]) != null) {
					nexthopIface = (IBaseInterface) matrix.get(P[j]).node;
					nexthopNode = (IHost) nexthopIface.getParent();
					nexthopId = nexthopNode.getRank(net); //route entry item 8: nexthopId
					if(nexthopNode!=srcNode){
						outboundIface = srcIface.getRank(srcNode); 
						if(srcIsEdgeIface){
							owningHost = srcNode.getRank(net);
							numOfBits = (int)srcNode.getSize();
						}
					}else{
						outboundIface = nexthopIface.getRank(nexthopNode); 
						if(srcIsEdgeIface){ 
							owningHost = nexthopNode.getRank(net);
							numOfBits = (int)nexthopNode.getSize();
						}
					}//route entry item 5, 6, 7: outboundIface, owningHost, numOfBits
				} else {
					jprime.Console.out.println("WARNING: matrix.get(P[" + j + "])==null, i-->" 
							+ matrix.get(nodes.elementAt(i)).node.getUniqueName() + ", j-->"
							+ matrix.get(targets.elementAt(j)).node.getUniqueName());
					continue;
				}

				// algorithmic routing only allows one routing sphere, set edgeIface to be false
				boolean edgeIface = false;  //route entry item 9: edgeIface
				
				// find the index of the next hop interface on bus and the number of bits of bus link
				long busIdx = 0;
				int numOfBitsBus = 0;
				IBaseInterface attachment = null;
				if (link != null) {
					ChildList<IBaseInterfaceAlias> attachments = ((ILink) link).getAttachments();
					if (attachments.size() > 2 && srcIface.getParent() != nexthopIface.getParent()) {
						// we need a bus index
						ChildList<IBaseInterfaceAlias>.ChildIterator it = attachments.enumerate();
						while (it.hasNext()) {
							IBaseInterfaceAlias a = it.next();
							// jprime.Console.out.println("\tOn attachement "+a.getUniqueName()+"-->"+a.deference().getUniqueName());
							if (((IBaseInterface) a.deference()) == nexthopIface) {
								attachment = a;
								break;
							}
							busIdx++;
						}
						numOfBitsBus = (int) Math.ceil(Math.log(attachments.size()) / Math.log(2));
						if (attachment == null) {
							throw new RuntimeException("Something went terribly wrong. Couldn't find the attachment "
											+ nexthopIface.getUniqueName() + " on link " + link.getUniqueName());
						}
					}
				} //route entry item 10, 11: busIdx, numOfBitsBus
				
				//create a route entry and put it to the table			
				if (nexthopNode != null) {
					if (srcNode.getUID() != nexthopNode.getUID()) {
						RouteEntry re = new RouteEntry(tbl,srcNode.getMinRank(net), srcNode.getMaxRank(net), dstIface.getMinRank(net), dstIface.getMaxRank(net), 
								outboundIface, owningHost, numOfBits, nexthopId, edgeIface, busIdx, numOfBitsBus, cost);
						merged+=insertPermRouteEntry(net,re,route_entries,rc);
					}
				}			
			}			
		}	
		return new RouteCounts(merged,route_entries.size());
	}
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#printAdjMatrix(java.io.PrintStream)
	 */
	public void printAdjMatrix(PrintStream o) {
		if(matrix==null)
			throw new RuntimeException("Can call this after calling compute shortest path!");
		for(Entry<Long, ModelNodeIdxPair> es : matrix.entrySet()) {
			o.println(es.getValue().node.getUniqueName()+"[rank="+es.getKey()+"]:");
			for(Entry<Long, IAdjacencyEntry> e : es.getValue().adjList.entrySet()) {
				o.println("\t-->"+matrix.get(e.getKey()).node.getUniqueName()+"[rank="+e.getKey()+"]"+" costs "+e.getValue().getCost());
			}
		}
	}

}
