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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
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
import jprime.RoutingSphere.EdgeInterfacePair;
import jprime.routing.IRouteEntry.RouteEntryComparator;
import jprime.util.ChildList;
import jprime.util.PersistentRouteEntryList;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 */
public class BGPAdjacencyMatrix extends BaseAdjacencyMatrix implements IAdjacencyMatrix {
	public static final class ModelNodeIdxPair {
		public final IModelNode node;
		public Map<Long, IAdjacencyEntry> adjList = new HashMap<Long, IAdjacencyEntry>();
		public ModelNodeIdxPair(IModelNode node) {
			this.node=node;
		}
	}
	
    /**
     * Some value to initialize the priority queue with.
     */
	public static class BGPAdjacencyEntry implements IAdjacencyEntry {
		
		public final BGPRelationShipType type;
		public int cost;
		public BGPAdjacencyEntry(BGPRelationShipType type, int cost) {
			super();
			this.type = type;
			this.cost = cost;
		}
		public String toString() {
			return "[type="+type+", cost="+cost+"]";
		}
		public int getCost() {
			return cost;
		}
		public BGPRelationShipType getRelationship(){
			return type;
		}   
	}
	
	private final IModelNode anchor;
	private Map<Long, ModelNodeIdxPair> matrix;
	private long[] P;
	private int[] D;
	private HashMap<Long,Integer> nodemap;
	private Vector<Long> nodes;

	private final Set<Long> settledNodes_; //the settled nodes set
	private final Map<Long, Integer> shortestDistances_; //the shortest distances list
	private final Map<Long, Long> predecessors_; //the predecessors
	private final PriorityQueue<Long> unsettledNodes; //the unsettled nodes set

	
	/**
     * This comparator orders nodes according to their shortest distances,
     * in ascending fashion. 
     */
	protected final Comparator<Long> shortestDistanceComparator = new Comparator<Long>(){
        public int compare(Long left, Long right){
            int shortestDistanceLeft = getShortestDistance(left);
            int shortestDistanceRight = getShortestDistance(right);
            if (shortestDistanceLeft > shortestDistanceRight){
                return 1;
            }
            else if (shortestDistanceLeft < shortestDistanceRight){
                return -1;
            }
            else {
                return left.compareTo(right);
            }
        }
    };

	/**
	 * 
	 */
	public BGPAdjacencyMatrix(IModelNode anchor) {
		super(anchor);
		this.anchor=anchor;
		this.matrix=new HashMap<Long, ModelNodeIdxPair>();
		this.P=null;
		this.D=null;
		this.nodemap = new HashMap<Long,Integer>();
		this.nodes = new Vector<Long>();	
		this.targets = new Vector<Long>();

		this.settledNodes_ = new HashSet<Long>();
		this.shortestDistances_  = new HashMap<Long, Integer>();
		this.predecessors_ = new HashMap<Long, Long>();
		this.unsettledNodes = new PriorityQueue<Long>(INITIAL_CAPACITY, shortestDistanceComparator);

	}
	
	protected final boolean isSettled(Long v){
	    return settledNodes_.contains(v);
	}

	protected final void setShortestDistance(Long node, int distance){
		/*
         * This crucial step ensures no duplicates are created in the queue
         * when an existing unsettled node is updated with a new shortest 
         * distance.
         */
        unsettledNodes.remove(node);

        /*
         * Update the shortest distance.
         */
        shortestDistances_.put(node, distance);
        
		/*
		 * Re-balance the queue according to the new shortest distance found
		 * (see the comparator the queue was initialized with).
		 */
		unsettledNodes.add(node);  
	}


	protected final void setPredecessor(Long a, Long b){
	    predecessors_.put(a, b);
	}

	public final int getShortestDistance(Long node){
	    Integer d = shortestDistances_.get(node);
	    return (d == null) ? Integer.MAX_VALUE : d;
	}	 

	public final  Long getPredecessor(Long node){
	    return predecessors_.get(node);
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
	public void addEdge(IModelNode left, IModelNode right) {
		throw new RuntimeException("Must specify a bgp relationship type!");
	}

	/**
	 * @param left
	 * @param right
	 * @param type
	 */
	public void addEdge(IModelNode left, IModelNode right, BGPRelationShipType type, int cost) {		
		getAdjListForNode(left).put(right.getRank(anchor),new BGPAdjacencyEntry(type,cost));
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#computeAlgPath()
	 */
	public void computeAlgPath(){
		throw new RuntimeException("Should call computeAlgPath on bgp!");
	}
		
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#computeShortestPath()
	 */
	public RouteCounts computeShortestPath(INet net) {
		IRouteTable tbl = net.getRoutingSphere().getRouteTable();
		PersistentRouteEntryList route_entries = tbl.getPermRouteEntries();
		RouteEntryComparator rc =new RouteEntryComparator(false);

		// jprime.Console.out.println("net=" + net.getUniqueName());
		// set edge nodes for the route table per routing sphere
		// Edge nodes include the edge nodes of this sphere and its direct children routing spheres
		List<Long> edgeNodes = new ArrayList<Long>();
		addEdgeNodes(edgeNodes, net);
		if (net.getRoutingSphere().getEdgeInterfaces() != null) {
			for (EdgeInterfacePair iface : net.getRoutingSphere().getEdgeInterfaces()) {
				if(!edgeNodes.contains(iface.getHostUID(net))){
					edgeNodes.add(iface.getHostUID(net));
				}
			}
		}
		jprime.Console.out.println("net=" + net.getUniqueName() + ", edgeNodes=" + edgeNodes);
		
		//this is a redux of the Dijkstra's algorithm from http://renaud.waldura.com/doc/java/dijkstra
		int merged = 0;
		if(P != null)
			return null;
		P = new long[matrix.size()];
		D = new int[matrix.size()];
		int node_idx=0;
		for(long l : matrix.keySet()) {
			nodes.add(l);
			nodemap.put(l, node_idx);
			node_idx++;
		}
	
		for(int i=0; i<nodes.size(); i++){//i is the src
			Arrays.fill(D, Integer.MAX_VALUE); //set the distance from i to all targets to be maximum
			//Arrays.fill(P, 0);
			Long src_node = nodes.elementAt(i); //node is the src
			if(matrix.containsKey(src_node)) {
				for(Entry<Long, IAdjacencyEntry> e : matrix.get(src_node).adjList.entrySet()) {
					D[nodemap.get(e.getKey())]=e.getValue().getCost();
				}
			}
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
	
			for(int j=0; j<nodes.size(); j++){
				Long start = nodes.elementAt(i);
				Long destination = nodes.elementAt(j);
				IBaseInterface dstIface = (IBaseInterface) matrix.get(destination).node; //route entry items 3,4: dstIface		
				//IHost dstNode = (IHost)dstIface.getParent();
				
				settledNodes_.clear();
				unsettledNodes.clear();       
				shortestDistances_.clear();
				predecessors_.clear();  
				
				for(int k=0;k<nodes.size();k++){
					for(int mark=10; mark<15; mark++){
						Long node=Long.parseLong((Integer.toString(mark)).concat(Long.toString(nodes.elementAt(k))));		  
						if(k==i){
							//add source
							setShortestDistance(node, 0);
							unsettledNodes.add(node);	
						}
					}
				}
				//the current node
				Long u;    
				//the node with the shortest distance
				while((u = unsettledNodes.poll())!=null){
					assert !isSettled(u);  
					Long uNode = Long.parseLong(Long.toString(u).substring(2));
					Long uType = Long.parseLong(Long.toString(u).substring(0,2));	
					// destination reached, stop
					if (uNode == destination) break;	            
					settledNodes_.add(u);	
					for (Long vNode : matrix.get(uNode).adjList.keySet()){
						BGPRelationShipType vRelationship = matrix.get(uNode).adjList.get(vNode).getRelationship();
						int vType=0;
						if(vRelationship==BGPRelationShipType.S2SDOWN){
							vType=10;		
						}else if(vRelationship==BGPRelationShipType.C2P){
							vType=11;
						}else if(vRelationship==BGPRelationShipType.P2P){
							vType=12;
						}else if(vRelationship==BGPRelationShipType.P2C){
							vType=13;
						}else if(vRelationship==BGPRelationShipType.S2SUP){
							vType=14;
						}else if(vRelationship==BGPRelationShipType.EXTRA){
							vType=15;
						}
						Long v = Long.parseLong((Integer.toString(vType)).concat(Long.toString(vNode)));
						// if v and u are on the same hosts, v has the same type as u.
						//if(vNode!=uNode && matrix.get(uNode).adjList.get(vNode).getCost()==0){
						if(vType==15){
							if(uType!=12){	
								v = Long.parseLong((Long.toString(uType)).concat(Long.toString(vNode)));
							}else{
								vType=13;
								v = Long.parseLong((Long.toString(vType)).concat(Long.toString(vNode)));
							}
							//jprime.Console.out.println("uNode is: "+uNode+", uType is: "+uType);
							//jprime.Console.out.println("vNode is: "+vNode+", vType is: "+Long.parseLong(Long.toString(v).substring(0,2)));
						}
						// skip node already settled
						if (isSettled(v)) continue;                
						int shortDist = getShortestDistance(u) + matrix.get(uNode).adjList.get(vNode).getCost();  
						if (uType==11 || uType==14){
							/*if(start==137&&destination==162){	
								jprime.Console.out.println("Case1");
								jprime.Console.out.println("dist(v): "+getShortestDistance(v));
							}*/
							if (shortDist < getShortestDistance(v)){
								// assign new shortest distance and mark unsettled
								setShortestDistance(v, shortDist);                             
								// assign predecessor in shortest path	
								setPredecessor(v, u);
							}
						}
						if ((uType==13 || uType==12 || uType==10)&&
								//(vRelationship==BGPRelationShipType.P2C||vRelationship==BGPRelationShipType.S2SDOWN)){
								(Long.parseLong(Long.toString(v).substring(0,2))==13||Long.parseLong(Long.toString(v).substring(0,2))==10)){
							/*if(start==137&&destination==162){
								jprime.Console.out.println("Case2");
								jprime.Console.out.println("dist(v): "+getShortestDistance(v));
							}*/
							if (shortDist < getShortestDistance(v)){
								// assign new shortest distance and mark unsettled
								setShortestDistance(v, shortDist);                             
								// assign predecessor in shortest path
								setPredecessor(v, u);
							}
						}
					}        
				}
				/*if(start==137&&destination==162){	
					for(Long key : predecessors.keySet()){
						jprime.Console.out.println("start="+start+" ,key="+key+" ,predecessor="+predecessors.get(key));
					}
					for(Long key : shortestDistances.keySet()){
						jprime.Console.out.println("start="+start+" ,key="+key+" ,distance="+shortestDistances.get(key));
					}
				}*/
				ArrayList<Long> C = new ArrayList<Long>();
				C.add(destination);
				//backtrack from the destination by P(previous), adding to the result list
				Long destPair=null;
				for(Long key: predecessors_.keySet()){
					Long destTemp = Long.parseLong((Long.toString(key)).substring(2));
					if(destTemp.compareTo(destination)==0){
						destPair=key;
						break;
					}
				}
				//Update cost
				if(i!=j){
					D[j]=getShortestDistance(destPair);
				}
				//Build the path and update the predecessor
				if(predecessors_.get(destPair)!=null){
					while(Long.parseLong((Long.toString(predecessors_.get(destPair))).substring(2))!=start){
						C.add(0,Long.parseLong((Long.toString(predecessors_.get(destPair))).substring(2)));
						destPair=predecessors_.get(destPair);
						if(predecessors_.get(destPair)==null){
							/*if(start==137&&destination==162){
								jprime.Console.out.println("ahaa, dstPair="+destPair);
								jprime.Console.out.println("i="+i+" ,j="+j);
							}*/
							//the path does not exist, the annotation may not be correct.
							if(Long.parseLong(Long.toString(destPair).substring(2))!=start){
								P[j]=0;
								D[j]=Integer.MAX_VALUE;
								jprime.Console.out.println("Destination("+destination+") is not reachable from the source("+start+").The annotation assignment may not be corret!");
							}
							break;
						}
					}
				}
				C.add(0,start);
				if(D[j]!=Integer.MAX_VALUE){//for those pairs have costs 0 or 1.
					P[j]=C.get(1);
				}
				// Shortest path calculation is done! 
				// Build the route entry for src i to dst j according to P (next hop) and D (distance) 
				
				// get cost from D
				int cost = 0;
				if (D != null) {
					cost = D[j]; //route entry item 12: cost
				}
					
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
							+ matrix.get(nodes.elementAt(j)).node.getUniqueName());
					continue;
				}

				// check if the next hop node is edge node
				boolean edgeIface = false;
				if (edgeNodes != null && nexthopNode != null) {
					if (edgeNodes.contains(nexthopNode.getUID())) {
						edgeIface = true;
					}
				}//route entry item 9: edgeIface
				
				// find the index of the next hop interface on bus and the number of bits of bus link
				long busIdx = 0;
				int numOfBitsBus = 0;
				IBaseInterface attachment = null;
				if (link != null) {
					ChildList<IBaseInterfaceAlias> attachments = ((ILink) link).getAttachments();
					if (attachments.size() > 2 && srcIface.getParent() != nexthopIface.getParent()) {
						// jprime.Console.out.println("srcIface="+srcIface.getUniqueName()+" dstIface="+dstIface.getUniqueName()+", and the link="+link.getUniqueName());
						// jprime.Console.out.println("size of ((ILink)link).getAttachments()="+((ILink)link).getAttachments().size());
						// we need a bus index
						ChildList<IBaseInterfaceAlias>.ChildIterator it = attachments.enumerate();
						// jprime.Console.out.println("Looking for attachment "+nexthopIface.getUniqueName()+" on link "+link.getUniqueName());
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
				
				//create a route entry and put it to the table, suppose they can insert in order here		
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
				o.println("\t-->"+matrix.get(e.getKey()).node.getUniqueName()+"[rank="+e.getKey()+"]"
						+" relationship "+e.getValue().getRelationship()+" costs "+e.getValue().getCost());
			}
		}
	}

}
