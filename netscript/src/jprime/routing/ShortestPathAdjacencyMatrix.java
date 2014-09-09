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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.BaseInterface.IBaseInterface;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.RouteTable.IRouteTable;
import jprime.Router.IRouter;
import jprime.Router.Router;
import jprime.RoutingSphere.EdgeInterfacePair;
import jprime.routing.IRouteEntry.RouteEntryComparator;
import jprime.util.ChildList;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 */
public class ShortestPathAdjacencyMatrix extends BaseAdjacencyMatrix implements IAdjacencyMatrix {
    public static final class SimpleAdjacencyEntry implements IAdjacencyEntry {
		public int cost;
		public SimpleAdjacencyEntry(int cost) {
			super();
			this.cost=cost;
		}
		public SimpleAdjacencyEntry() {
			super();
			this.cost=Integer.MAX_VALUE;
		}
		public int getCost() {
			return cost;
		}
		public BGPRelationShipType getRelationship() {
			return null;
		}
		public String toString() {
			return "[cost="+cost+"]";
		}
	}
    
	private int[] P;
	private int[] D;
	private boolean[] settledNodes_; //the settled nodes set
	private int[] shortestDistances_; //the shortest distances list
	private int[] predecessors_; //the predecessors
	private final PriorityQueue<Integer> unsettledNodes; //the unsettled nodes set  
	
	private ArrayList<NodeAdj>[] compressed_nodes;

	
	/**
     * This comparator orders nodes according to their shortest distances,
     * in ascending fashion. 
     */
	protected final Comparator<Integer> shortestDistanceComparator = new Comparator<Integer>(){
        public int compare(Integer left, Integer right){
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
	public ShortestPathAdjacencyMatrix(IModelNode anchor) {
		super(anchor);
		this.P=null;
		this.D=null;
		this.settledNodes_=null;
		this.shortestDistances_=null;
		this.predecessors_=null;
		this.unsettledNodes = new PriorityQueue<Integer>(INITIAL_CAPACITY, shortestDistanceComparator);
		this.compressed_nodes=null;
	}
	
	protected final boolean isSettled(int v){
	    return settledNodes_[v];
	}

	protected final void setSettled(int v){
	    settledNodes_[v]=true;
	}
	
	protected final void setShortestDistance(int node, int distance){
		/*
         * This crucial step ensures no duplicates are created in the queue
         * when an existing unsettled node is updated with a new shortest 
         * distance.
         */
        unsettledNodes.remove(node);
        settledNodes_[node]=true;
        
        /*
         * Update the shortest distance.
         */
        shortestDistances_[node]=distance;
        
		/*
		 * Re-balance the queue according to the new shortest distance found
		 * (see the comparator the queue was initialized with).
		 */
		unsettledNodes.add(node); //XXX check this....
	}


	protected final void setPredecessor(int a, int b){
		predecessors_[a]=b;
	}

	public final  int getShortestDistance(int node){
		return shortestDistances_[node];
	}	 

	public final int getPredecessor(int node){
	    return predecessors_[node];
	}


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
		NodeAdj l = getNodeAdj(left);
		NodeAdj r = getNodeAdj(right);
		l.adjList.put(r.idx,new SimpleAdjacencyEntry(cost));
		r.adjList.put(l.idx,new SimpleAdjacencyEntry(cost));
		
		l.adjList.put(l.idx,new SimpleAdjacencyEntry(0));
		r.adjList.put(r.idx,new SimpleAdjacencyEntry(0));
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#computeShortestPath()
	 */
	@SuppressWarnings("unchecked")
	public RouteCounts computeShortestPath(INet net) {
		jprime.Console.out.println("\n\ncompute shortest path for net="+net.getUniqueName()+", uid="+net.getUID());
		Collections.sort(targets);
		/*for(Long r : targets){
			jprime.Console.out.println("original targets : "+r);
		}*/
		int num_router=0;
		int num_sub=0;
		for(ModelNode c : net.getAllChildren()) {	
			if(c instanceof IRouter) {
				num_router++;
			}else if(c instanceof INet){
				num_sub++;
			}
		}
		if(net.getParent()==null && num_router<2 && num_sub==0){
			jprime.Console.out.println("This topology has less than two routers, skipping routing optimization");
		}else{
			compressed_nodes=new ArrayList[matrix.size()];
			Arrays.fill(compressed_nodes, null);
		
			/*
			 * remove hosts from adj and only do calc on routers
			 */
			for(int i=0;i<matrix.size();i++){
				final NodeAdj i_node = matrix.get(i);
				if(((IHost)i_node.node.getParent()).countInterfaces()==1) {
					//we are a host!
					final int idx = Collections.binarySearch(targets,((Long)i_node.rank));
					if(idx>=0) {
						targets.remove(idx);
					}
					matrix.set(i,null);
					//remove me from others adj
					for( Entry<Integer, IAdjacencyEntry> e : i_node.adjList.entrySet()) {
						if(e.getKey() != i) {
							//jprime.Console.out.println("e.getKey() = " + e.getKey() + ", e.getValue()=" + e.getValue());
							final NodeAdj j_node = matrix.get(e.getKey());
							j_node.r_adjList.put(i,j_node.adjList.remove(i));
							if(e.getValue().getCost()>0) {
								//jprime.Console.out.println("Add compressed i node="+i_node.idx+" to j node="+j_node.idx);
								j_node.compressed.add(i_node);
							}
							else {
								throw new RuntimeException("How did we get here?");
							}
						}
					}
				}
			}
			/*if(net.getUID()==220){ //97: net0, 263: net1, 1600: net=topnet:net2:net2_lan0
				jprime.Console.out.println("after removing hosts:");
				jprime.Console.out.println("Adj matrix:");
			    printAdjMatrix(jprime.Console.out);
				for(Long r : targets){
					jprime.Console.out.println("targets: "+r);
				}	
			}*/
			/*
			 * find all interfaces of router which only have self loops
			 */
			for(int i=0;i<matrix.size();i++){
				final NodeAdj i_node = matrix.get(i);
				if(i_node == null) continue;
				int c =0;
				for(IAdjacencyEntry e : i_node.adjList.values()) {
					c+=e.getCost();
				}
	
				if(c==0){
					boolean isEdge=isEdgeIface(net, i_node);		
					if(!isEdge){
						//jprime.Console.out.println("costs=0, i_node="+i_node.node.getUniqueName());
						//we only link to ifaces on the same router!
						int idx = Collections.binarySearch(targets,((Long)i_node.rank));
						if(idx>=0) {
							//jprime.Console.out.println("remove target idx="+idx);
							targets.remove(idx);
						}
						matrix.set(i,null);
						//remove me from others adj
						for(Entry<Integer, IAdjacencyEntry> e : i_node.adjList.entrySet()) {
							if(e.getKey() != i) {
								final NodeAdj j_node = matrix.get(e.getKey());
								j_node.r_adjList.put(i,j_node.adjList.remove(i));
								idx = Collections.binarySearch(targets,((Long)j_node.rank));
								if(idx<0) {
									//jprime.Console.out.println("add node rank="+j_node.rank+" to target idx="+(-idx-1));
									targets.add(((-idx)-1),j_node.rank);
								}
								if(i_node.compressed.size()>0){
									if(compressed_nodes[e.getKey()] == null)
										compressed_nodes[e.getKey()]=new ArrayList<NodeAdj>();
									//jprime.Console.out.println("add index="+i_node.idx+" to compressed_nodes index="+e.getKey());
									compressed_nodes[e.getKey()].add(i_node);
								}
							}
						}
					}
				}
			}
			/*if(net.getUID()==220){
				jprime.Console.out.println("after removing routers:");
				jprime.Console.out.println("Adj matrix:");
				printAdjMatrix(jprime.Console.out);
				for(Long r : targets){
					jprime.Console.out.println("targets: "+r);
				}	
			}*/
		}
		
		IRouteTable tbl = net.getRoutingSphere().getRouteTable();
		LinkedList<TempRouteEntry> route_entries = tbl.getTempRouteEntries();
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
		//jprime.Console.out.println("net=" + net.getUniqueName() + ", edgeNodes=" + edgeNodes);	
		//jprime.Console.out.println("net=" + anchor.getUniqueName() + " targets=" + targets);
		
		//this is a redux of the Dijkstra's algorithm from http://renaud.waldura.com/doc/java/dijkstra
		if(P != null)
			return null;
		//jprime.Console.out.println("\nCompute shortest path with a matrix of "+matrix.size()+"x"+matrix.size()+", edge node size="+edgeNodes.size());
		int merged=0;
		P = new int[matrix.size()]; //P[j] is the next hop from src to j
		D = new int[matrix.size()]; //D[j] is the best estimate of the shortest distance from src to j
		settledNodes_=new boolean[matrix.size()];
		shortestDistances_=new int[matrix.size()];
		predecessors_=new int[matrix.size()];
		Integer[] target_idx =null;
		if(targets.isEmpty()){
			target_idx = new Integer[rank2idx.size()];
			for(int i=0;i<rank2idx.size();i++) {
				target_idx[i]=i;
			}
		}
		else {
			target_idx = new Integer[targets.size()];
			for(int i=0;i<targets.size();i++) {
				target_idx[i]=rank2idx.get(targets.get(i));;
			}
		}

		for(int i=0; i<matrix.size(); i++){ //i is the src
			NodeAdj i_node = matrix.get(i);
			if(null == i_node) continue; //the entry was compressed
			//init the arrays used to calc shortest paths to all dsts
			Arrays.fill(P, Integer.MAX_VALUE); 
			Arrays.fill(D, Integer.MAX_VALUE); 
			
			//init D with initial costs
			for(Entry<Integer, IAdjacencyEntry> e : matrix.get(i).adjList.entrySet()) {
				D[e.getKey()]=e.getValue().getCost();
			}
			
			/*
			 * Create route entries for:
			 * Src: 
			 * 	i(router), next hop is calculated
			 * 	for each h in i.compressed (hosts on bus), next hop is i, cost++
			 * 	for each h in compressed_node[i] (router), next hop is calculated
			 *  for each h in compressed_node[i], for each item in h.compressed (host), next hop is i, cost++
			 *  
			 * Dst:
			 *  j (if j's parent is emulated router or host),
			 *  for each d in j.compressed (hosts on bus), cost++
			 *  for each d in compressed_node[j] (if d's parent is emulated router)
			 *  for each d in compressed_node[j], for each item in d.compressed (host), cost++
			 */
			for(int j=0; j<target_idx.length; j++) {
				//make sure the target was reachable (i.e. had any links attached to it
				if(target_idx[j]==null)continue;

				//init the arrays used to calc shortest path to _this_ dst
				Arrays.fill(settledNodes_, false); 
				Arrays.fill(shortestDistances_, Integer.MAX_VALUE); 
				Arrays.fill(predecessors_, Integer.MAX_VALUE);
				unsettledNodes.clear();       
				
				int dest = target_idx[j];
				//jprime.Console.out.println("j="+j+", target_idx[j]="+target_idx[j]+", dest="+dest);
				final int tgt_idx=dest;
				
				NodeAdj j_node = matrix.get(dest);
				if(j_node==null){
					jprime.Console.out.println("Should never see this! The dest has been removed from the matrix!");
				}
				
				//jprime.Console.out.println("j_node="+j_node.node.getUniqueName()+", index="+j_node.idx);
				//jprime.Console.out.println("dstIface="+dstIface.getUniqueName()+", index="+matrix.get(dest).idx);
				
				//add source
				setShortestDistance(i, 0);
				unsettledNodes.add(i);		
				//the current node
				Integer u;    
				//the node with the shortest distance
				while((u = unsettledNodes.poll())!=null){
					assert !isSettled(u);  					
					// destination reached, stop
					if (u == dest) break;      
					setSettled(u);	
					for (Integer v : matrix.get(u).adjList.keySet()){
						// skip node already settled
						if (isSettled(v)) continue;                
						int shortDist = getShortestDistance(u) + matrix.get(u).adjList.get(v).getCost();  						
						if (shortDist < getShortestDistance(v)){
							// assign new shortest distance and mark unsettled
							setShortestDistance(v, shortDist);                             
							// assign predecessor in shortest path	
							setPredecessor(v, u);
						}
					}
				}
				
				ArrayList<Integer> C = new ArrayList<Integer>();
				C.add(dest);

				//Update cost
				if(i!=tgt_idx){
					D[tgt_idx]=getShortestDistance(dest);
				}
				//backtrack from the destination by P(previous),Build the path and update the predecessor
				if(getPredecessor(dest)!=Integer.MAX_VALUE){
					while(getPredecessor(dest)!=i){
						C.add(0,getPredecessor(dest));
						dest=getPredecessor(dest);
						if(dest==Integer.MAX_VALUE){
							if(dest!=i){
								P[tgt_idx]=0;
								D[tgt_idx]=Integer.MAX_VALUE;
								
								jprime.Console.out.println("WARNING: Destination("+dest+") is not reachable from the source("+i+").The annotation assignment may not be corret!");
							}
							break;
						}
					}
				}
				C.add(0,i);
				if(D[tgt_idx]!=Integer.MAX_VALUE){//for those pairs have costs 0 or 1.
					P[tgt_idx]=C.get(1);
				}	
			
				// Build the route entry for src i to dst j according to P (next hop) and D (distance) 
				// get cost from D
				int cost = 0;
				if (D != null) {
					cost = D[tgt_idx]; //route entry item 12: cost
				}
				
				IInterface nexthopIface = null;
				
				// get next hop from P	
				if (P[tgt_idx] < Integer.MAX_VALUE && matrix.get(P[tgt_idx]) != null) {
					nexthopIface = (IInterface) matrix.get(P[tgt_idx]).node;
					/*jprime.Console.out.println("base route: srcIface="+i_node.node.getUniqueName()
							+", dstIface="+j_node.node.getUniqueName()
							+", nexthopIface="+nexthopIface.getUniqueName()+", index="+matrix.get(P[tgt_idx]).idx);*/
					//base route entry from i to j
				} else {					
					jprime.Console.out.println("WARNING: matrix.get(P[" + tgt_idx + "])==null, i-->" 
							+ matrix.get(i).node.getUniqueName() + ", j-->"
							+ matrix.get(tgt_idx).node.getUniqueName());
					continue;
				}
				// Shortest path calculation is done!
				
				if(net.getParent()==null && num_router<2 && num_sub==0){
					TempRouteEntry re;
					if(targets.contains(i_node.rank)){ //This is host
						re = calRouteEntry(net, (IBaseInterface)i_node.node, (IBaseInterface)j_node.node, nexthopIface, i_node.node.getRank(i_node.node.getParent()),edgeNodes, cost);
					}else{
						re = calRouteEntry(net, (IBaseInterface)i_node.node, (IBaseInterface)j_node.node, nexthopIface, nexthopIface.getRank(nexthopIface.getParent()),  edgeNodes, cost);
					}
					merged+=insertTempRouteEntry(net,re,route_entries,rc);
				}else{
				
				{//start with i as source
					TempRouteEntry e = calcRoute_i(i_node,j_node,null,null,cost,nexthopIface,net,edgeNodes);
					if(e!=null) {
						merged+=insertTempRouteEntry(net,e,route_entries,rc);
					}
					if(compressed_nodes[j_node.idx]!=null && compressed_nodes[j_node.idx].size()>0){		
						for(NodeAdj jcn : compressed_nodes[j_node.idx]){		
							e = calcRoute_i(i_node,j_node,jcn,null,cost,nexthopIface,net,edgeNodes);
							if(e!=null) {
								merged+=insertTempRouteEntry(net,e,route_entries,rc);
							}
							if(jcn.compressed.size()>0){			
								for(NodeAdj jcnc : jcn.compressed){
									e = calcRoute_i(i_node,j_node,jcn,jcnc,cost,nexthopIface,net,edgeNodes);
									if(e!=null) {
										merged+=insertTempRouteEntry(net,e,route_entries,rc);
									}
								}
							}
						}
					}
					if(j_node.compressed.size()>0){			
						for(NodeAdj jc : j_node.compressed){
							e = calcRoute_i(i_node,j_node,jc,cost,nexthopIface,net,edgeNodes);
							if(e!=null) {
								merged+=insertTempRouteEntry(net,e,route_entries,rc);
							}
						}
					}
				}//end with i as source
				
				if(i_node.compressed.size()>0){//start with ic as source
					for(NodeAdj ic : i_node.compressed){
						TempRouteEntry e = calcRoute_ic(ic,i_node,j_node,null,null,cost,nexthopIface,net,edgeNodes);
						if(e!=null) {
							merged+=insertTempRouteEntry(net,e,route_entries,rc);
						}
						if(compressed_nodes[j_node.idx]!=null && compressed_nodes[j_node.idx].size()>0){		
							for(NodeAdj jcn : compressed_nodes[j_node.idx]){		
								e = calcRoute_ic(ic,i_node,j_node,jcn,null,cost,nexthopIface,net,edgeNodes);
								if(e!=null) {
									merged+=insertTempRouteEntry(net,e,route_entries,rc);
								}
								if(jcn.compressed.size()>0){			
									for(NodeAdj jcnc : jcn.compressed){
										e = calcRoute_ic(ic,i_node,j_node,jcn,jcnc,cost,nexthopIface,net,edgeNodes);
										if(e!=null) {
											merged+=insertTempRouteEntry(net,e,route_entries,rc);
										}
									}
								}
							}
						}
						if(j_node.compressed.size()>0){			
							for(NodeAdj jc : j_node.compressed){
								e = calcRoute_ic(ic,i_node,j_node,jc,cost,nexthopIface,net,edgeNodes);
								if(e!=null) {
									merged+=insertTempRouteEntry(net,e,route_entries,rc);
								}
							}
						}
					}
				}//end with ic as source
				
				{//start with icn as source
					if(compressed_nodes[i_node.idx]!=null && compressed_nodes[i_node.idx].size()>0){		
						for(NodeAdj icn : compressed_nodes[i_node.idx]){		
							TempRouteEntry e = calcRoute_icnc(null,icn,i_node,j_node,null,null,cost,nexthopIface,net,edgeNodes);
							if(e!=null) {
								merged+=insertTempRouteEntry(net,e,route_entries,rc);
							}
							if(compressed_nodes[j_node.idx]!=null && compressed_nodes[j_node.idx].size()>0){		
								for(NodeAdj jcn : compressed_nodes[j_node.idx]){		
									e = calcRoute_icnc(null,icn,i_node,j_node,jcn,null,cost,nexthopIface,net,edgeNodes);
									if(e!=null) {
										merged+=insertTempRouteEntry(net,e,route_entries,rc);
									}
									if(jcn.compressed.size()>0){			
										for(NodeAdj jcnc : jcn.compressed){
											e = calcRoute_icnc(null,icn,i_node,j_node,jcn,jcnc,cost,nexthopIface,net,edgeNodes);
											if(e!=null) {
												merged+=insertTempRouteEntry(net,e,route_entries,rc);
											}
										}
									}
								}
							}
							if(j_node.compressed.size()>0){			
								for(NodeAdj jc : j_node.compressed){
									e = calcRoute_icnc(null,icn,i_node,j_node,jc,cost,nexthopIface,net,edgeNodes);
									if(e!=null) {
										merged+=insertTempRouteEntry(net,e,route_entries,rc);
									}
								}
							}
						}
					}
				}//end with icn as source
				
				{//start with icnc as source
					if(compressed_nodes[i_node.idx]!=null && compressed_nodes[i_node.idx].size()>0){		
						for(NodeAdj icn : compressed_nodes[i_node.idx]){		
							if(icn.compressed.size()>0){			
								for(NodeAdj icnc : icn.compressed){
									TempRouteEntry e = calcRoute_icnc(icnc,icn,i_node,j_node,null,null,cost,nexthopIface,net,edgeNodes);
									if(e!=null) {
										merged+=insertTempRouteEntry(net,e,route_entries,rc);
									}
									if(compressed_nodes[j_node.idx]!=null && compressed_nodes[j_node.idx].size()>0){		
										for(NodeAdj jcn : compressed_nodes[j_node.idx]){		
											e = calcRoute_icnc(icnc,icn,i_node,j_node,jcn,null,cost,nexthopIface,net,edgeNodes);
											if(e!=null) {
												merged+=insertTempRouteEntry(net,e,route_entries,rc);
											}
											if(jcn.compressed.size()>0){			
												for(NodeAdj jcnc : jcn.compressed){
													e = calcRoute_icnc(icnc,icn,i_node,j_node,jcn,jcnc,cost,nexthopIface,net,edgeNodes);
													if(e!=null) {
														merged+=insertTempRouteEntry(net,e,route_entries,rc);
													}
												}
											}
										}
									}
									if(j_node.compressed.size()>0){			
										for(NodeAdj jc : j_node.compressed){
											e = calcRoute_icnc(icnc,icn,i_node,j_node,jc,cost,nexthopIface,net,edgeNodes);
											if(e!=null) {
												merged+=insertTempRouteEntry(net,e,route_entries,rc);
											}
										}
									}
								}
							}
						}
					}
				}//end with icnc as source
				}
			}
		}
		return new RouteCounts(merged,route_entries.size());
	}
			
	
	private TempRouteEntry calRouteEntry(INet net, IBaseInterface i, IBaseInterface j, IBaseInterface nexthop, long outbound, List<Long> edgeNodes, int cost){
		IBaseInterface srcIface = i;
		//check if the srcIface is the edge interface of my child spheres
		boolean srcIsEdgeIface = net.isEdgeIfaceOfChildSphere((IInterface)srcIface);
		IHost srcNode = (IHost) srcIface.getParent(); //route entry items 1,2: srcNode
		
		IBaseInterface dstIface = j; //route entry items 3,4: dstIface	
		
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
		
		//jprime.Console.out.println("link="+link.getUniqueName());
	
		IBaseInterface nexthopIface = nexthop;
		//jprime.Console.out.println("nexthopIface="+nexthopIface.getUniqueName());
		IHost nexthopNode = (IHost) nexthopIface.getParent();
		//jprime.Console.out.println("nexthopNode="+nexthopNode.getUniqueName());
		long nexthopId = nexthopNode.getRank(net); //route entry item 8: nexthopId						
		
		long outboundIface = outbound;
		long owningHost = 0;
		
		if(nexthopNode!=srcNode){
			if(srcIsEdgeIface){
				owningHost = srcNode.getRank(net);
				numOfBits = (int)srcNode.getSize();
			}		
		}else{
			jprime.Console.out.println("Can never happen: nexthopNode="+nexthopNode.getUniqueName()+" is equal to srcNode="+srcNode.getUniqueName()+", self loop!");
			jprime.Console.out.println("srcIface="+srcIface.getUniqueName()+", dstIface="+dstIface.getUniqueName());
		}//route entry item 5, 6, 7: outboundIface, owningHost, numOfBits
	

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
				//jprime.Console.out.println("srcIface="+srcIface.getUniqueName()+" dstIface="+dstIface.getUniqueName()+", and the link="+link.getUniqueName());
				//jprime.Console.out.println("size of ((ILink)link).getAttachments()="+((ILink)link).getAttachments().size());
				//we need a bus index
				ChildList<IBaseInterfaceAlias>.ChildIterator it = attachments.enumerate();							
				//jprime.Console.out.println("Looking for attachment "+nexthopIface.getUniqueName()+" on link "+link.getUniqueName());
				while (it.hasNext()) {
					IBaseInterfaceAlias a = it.next();
				    //jprime.Console.out.println("\tOn attachement "+a.getUniqueName()+"-->"+a.deference().getUniqueName());
					if (((IBaseInterface) a.deference()).getUID() == nexthopIface.getUID()) {
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
		}
		
		IRouteTable tbl = net.getRoutingSphere().getRouteTable();
		TempRouteEntry newRe = new TempRouteEntry(tbl,srcNode.getMinRank(net), srcNode.getMaxRank(net), dstIface.getMinRank(net), dstIface.getMaxRank(net), 
				outboundIface, owningHost, numOfBits, nexthopId, edgeIface, busIdx, numOfBitsBus, cost);
		return newRe;
	}

	/**
	 * @param i    never null
	 * @param j    never null
	 * @param jcn  can be null
	 * @param jcnc can be null
 	 * @param baseCost cost from i to j ONLY
	 * @param nhi_i_j the next hop on the way to j from i
	 * @param net  the net owns the current routing sphere, used for calRouteEntry() 
	 * @param edgeNodes  the list of edge nodes of the current sphere, used for calRouteEntry()
	 * @return
	 */
	private TempRouteEntry calcRoute_i(NodeAdj i, NodeAdj j, NodeAdj jcn, NodeAdj jcnc, int baseCost, IInterface nhi_i_j, INet net, List<Long> edgeNodes) {
		//jprime.Console.out.println("calc i 1");
		if(i==null || j==null){
			throw new RuntimeException("i="+i+", j="+j+"; i and j can never be null!");
		}
		IBaseInterface srcIface=(IBaseInterface)i.node;
		IBaseInterface dstIface=null;
		long outboundIface = i.node.getRank(i.node.getParent()); //outboundIface is always i 
		IBaseInterface nexthopIface = null;
		
		if(jcn==null && jcnc==null){ //1. i->j
			//jprime.Console.out.println("1. i to j, srcIface="+srcIface.getUniqueName()+", dstIface="+j.node.getUniqueName());
			if(isTarget(net, j)){	
				dstIface=(IBaseInterface)j.node;
				if(i.node.getParent().getUID()!=j.node.getParent().getUID()){
					nexthopIface = (IBaseInterface)nhi_i_j;
					if(i.node.getParent().getUID()!=nexthopIface.getParent().getUID()){
						TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost);
						//jprime.Console.out.println("re is not null"
								//+", nexthopIface"+nexthopIface.getUniqueName()+", outboundIface="+outboundIface+", cost="+baseCost);
						return re;
					}else{//|i->nexthopIface|->j, srcNode is nexthopNode, ignore
						return null;
					}
				}else{ //|i->j|, srcNode is nexthopNode, ignore
					//jprime.Console.out.println("i and j are on the same node!");
					return null;
				}
			}else{
				//jprime.Console.out.println("j is not target!");
				return null;
			}
		}else if(jcn!=null){//i->j->jcn/jcnc, jcn and j must be on the same node
			if(j.node.getParent().getUID()!=jcn.node.getParent().getUID()){
				throw new RuntimeException("j node="+j.node.getUniqueName()+" and jcn node="+jcn.node.getUniqueName()+" must be on the same node!");
			}
			if(jcnc==null){ //2. i->|j->jcn|
				//jprime.Console.out.println("1. i to jcn, srcIface="+srcIface.getUniqueName()+", dstIface="+jcn.node.getUniqueName());
				if(isTarget(net, jcn)){
					if(i.node.getParent().getUID()!=j.node.getParent().getUID()){ //2.a i->|j->jcn|, i and j are on the different nodes, cost doesn't change 
						dstIface = (IBaseInterface)jcn.node; //dstIface is jcn
						nexthopIface = (IBaseInterface)nhi_i_j; //nexthopIface is the next hop from i to j
						if(i.node.getParent().getUID()!=nexthopIface.getParent().getUID()){
							TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost);
							//jprime.Console.out.println("re is not null"
								//+", nexthopIface"+nexthopIface.getUniqueName()+", outboundIface="+outboundIface+", cost="+baseCost);
							return re;
						}else{
							return null;
						}
					}else{ //2.b |i->j->jcn|, srcNode is nexthopNode, ignore
						//jprime.Console.out.println("i and jcn are on the same node!");
						return null;	
					}
				}else{
					//jprime.Console.out.println("jcn is not target!");
					return null;
				}
			}else{ //3. i->|j->jcn|->jcnc, jcnc and jcn can never be on the different nodes
				
				if(jcn.node.getParent().getUID()==jcnc.node.getParent().getUID()){
					throw new RuntimeException("jcn node="+jcn.node.getUniqueName()+" and jcnc node="+jcnc.node.getUniqueName()+" can never be on the same node!");
				}
				if(i.node.getParent().getUID()!=j.node.getParent().getUID()){ //3.a i->|j->jcn|->jcnc, i and j are on the different nodes, cost doesn't change 
					dstIface = (IBaseInterface)jcnc.node; //dstIface is jcn
					nexthopIface = (IBaseInterface)nhi_i_j; //nexthopIface is the next hop from i to j	
					if(i.node.getParent().getUID()!=nexthopIface.getParent().getUID()){
						TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
						return re;
					}else{
						return null;
					}
				}else{ //3.b |i->j->jcn|->jcnc, srcNode is nexthopNode, ignore
					return null;
				}
			}	
		}else{ //jcn is null, jcnc is not null
			throw new RuntimeException("jcn="+jcn+", jcnc="+jcnc+"; jcn can not be null when jcnc is not null!");
		}
	}
	
	/**
	 * @param i  never null
	 * @param j  never null
	 * @param jc never null
 	 * @param baseCost cost from i to j ONLY
	 * @param nhi_i_j the next hop on the way to j from i
	 * @param net  the net owns the current routing sphere, used for calRouteEntry() 
	 * @param edgeNodes  the list of edge nodes of the current sphere, used for calRouteEntry()
	 * @return
	 */
	private TempRouteEntry calcRoute_i(NodeAdj i, NodeAdj j, NodeAdj jc, int baseCost, IInterface nhi_i_j, INet net, List<Long> edgeNodes) {
		//jprime.Console.out.println("calc i 2");
		if(i==null || j==null || jc==null){
			throw new RuntimeException("i="+i+", j="+j+", jc="+jc+"; i and j can never be null!");
		}
		
		IBaseInterface srcIface=(IBaseInterface)i.node;
		IBaseInterface dstIface=(IBaseInterface)jc.node;
		long outboundIface = i.node.getRank(i.node.getParent()); //outboundIface is always i
		IBaseInterface nexthopIface = null;
		
		// jc is the interface on a bus link, jc and j are on different nodes.	
		if(j.node.getParent().getUID()==jc.node.getParent().getUID()){
			throw new RuntimeException("j node="+j.node.getUniqueName()+" and jc node="+jc.node.getUniqueName()+" can never be on the same node!");
		}
		
		if(i.node.getParent().getUID()==j.node.getParent().getUID()){ //1. |i->j|->jc 
			if(i.node.getUID()==j.node.getUID()){ //i is j, nexthopIface is jc, cost+1
				nexthopIface = (IBaseInterface)jc.node;
				TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
				return re;
			}else{ //i and j are on the same node, i is not j, nexthopIface is j, srcNode is nexthopNode, ignore
				return null;
			}
		}else{ //2. i->j->jc
			if(i.bothExistInAdj(j)){ //i and j are directly connected by a bus link, nexthopIface is jc, cost doesn't change
				nexthopIface = (IBaseInterface)jc.node;
				TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost);
				return re;
			}else{ //i and j are not directly connected
				nexthopIface = (IBaseInterface)nhi_i_j;
				TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
				return re;
			}
		}	
	}

	/**
	 * @param icnc can be null
	 * @param icn  never null
	 * @param i    never null
	 * @param j    never null
	 * @param jcn  can be null
	 * @param jcnc can be null
 	 * @param baseCost cost from i to j ONLY
	 * @param nhi_i_j the next hop on the way to j from i
	 * @param net  the net owns the current routing sphere, used for calRouteEntry() 
	 * @param edgeNodes  the list of edge nodes of the current sphere, used for calRouteEntry()
	 * @return
	 */
	private TempRouteEntry calcRoute_icnc(NodeAdj icnc, NodeAdj icn, NodeAdj i, NodeAdj j, NodeAdj jcn, NodeAdj jcnc, int baseCost, IInterface nhi_i_j, INet net, List<Long> edgeNodes) {
		//jprime.Console.out.println("calc icnc 1");
		if(icn==null || i==null || j==null){
			throw new RuntimeException("icn="+icn+", i="+i+", j="+j+"; icn, i and j can never be null!");
		}
		//icn and i must be on the same node
		if(i.node.getParent().getUID()!=icn.node.getParent().getUID()){
			throw new RuntimeException("i node="+i.node.getUniqueName()+" and icn node="+icn.node.getUniqueName()+" must be on the same node!");
		}
		
		IBaseInterface srcIface = null; 
		IBaseInterface dstIface = null; 
		long outboundIface = 0;
		IBaseInterface nexthopIface = null; 
		
		if(icnc == null){ //1. from icn to j, jcn or jcnc
			srcIface = (IBaseInterface)icn.node;
			if(jcn == null && jcnc == null){ //icn to j
				//1. |icn->i|->j, nexthopIface is i, srcNode is nexthopNode
				//2. |icn->i->j|, nexthopIface is j, srcNode is nexthopNode
				//2.1 i==j, |icn->j|, ignore
				return null;
			}else if (jcn!=null){ //icn to jcn or jcnc
				//jcn and j must be on the same node
				if(j.node.getParent().getUID()!=jcn.node.getParent().getUID()){
					throw new RuntimeException("j node="+j.node.getUniqueName()+" and jcn node="+jcn.node.getUniqueName()+" must be on the same node!");
				}
				if(jcnc==null){//icn to jcn
					//1. |icn->i|->|j->jcn|, nexthopIface is i, srcNode is nexthopNode
					//2. |icn->i->j->jcn|, self loop, ignore
					return null;	
				}else{//icn to jcnc
					if(jcn.node.getParent().getUID()==jcnc.node.getParent().getUID()){
						throw new RuntimeException("jcn node="+jcn.node.getUniqueName()+" and jcnc node="+jcnc.node.getUniqueName()+" can never be on the same node!");
					}
					
					//1. |icn->i|->|j->jcn|->jcnc, nexthopIface is i, srcNode is nexthopNode
					//2. |icn->i->j->jcn|->jcnc, self loop, ignore
					//2.1 i==j |icn->i->icn|->jcnc == icn->jcnc
					if(icn.node.getUID()==jcn.node.getUID()){
						dstIface=(IBaseInterface)jcnc.node;
						outboundIface = icn.node.getRank(icn.node.getParent());
						nexthopIface = (IBaseInterface)jcnc.node;
						TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
						return re;
					}
					
					return null;	
				}
			}else{ //jcn is null, jcnc is not null
				throw new RuntimeException("jcn="+jcn+", jcnc="+jcnc+"; jcn can not be null when jcnc is not null!");				
			}		
		}else{ //2. from icnc to j, jcn or jcnc
			if(icn.node.getParent().getUID()==icnc.node.getParent().getUID()){
				throw new RuntimeException("icn node="+icn.node.getUniqueName()+" and icnc node="+icnc.node.getUniqueName()+" can never be on the same node!");
			}
			srcIface = (IBaseInterface)icnc.node;
			outboundIface = icnc.node.getRank(icnc.node.getParent()); //outboundIface is always icnc
			nexthopIface = (IBaseInterface)icn.node; //nexthopIface is always icn
			
			if(jcn == null && jcnc == null){ //icnc to j
				if(isTarget(net, j)){
					dstIface = (IBaseInterface)j.node;
					//1. icnc->|icn->i|->j, nexthopIface is icn, cost+1
					//2. icnc->|icn->i->j|, nexthopIface is icn, cost+1
					TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
					return re;
				}
			}else if (jcn!=null){ //icn to jcn or jcnc
				//jcn and j must be on the same node
				if(j.node.getParent().getUID()!=jcn.node.getParent().getUID()){
					throw new RuntimeException("j node="+j.node.getUniqueName()+" and jcn node="+jcn.node.getUniqueName()+" must be on the same node!");
				}
				if(jcnc==null){//icnc to jcn
					if(isTarget(net, jcn)){
						dstIface = (IBaseInterface)jcn.node;
						//1. icnc->|icn->i|->|j->jcn|, nexthopIface is icn, cost+1
						//2. icnc->|icn->i->j->jcn|, nexthopIface is icn, cost+1
						TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
						return re;	
					}
				}else{//icnc to jcnc	
					dstIface = (IBaseInterface)jcnc.node;
					if(jcn.node.getParent().getUID()==jcnc.node.getParent().getUID()){
						throw new RuntimeException("jcn node="+jcn.node.getUniqueName()+" and jcnc node="+jcnc.node.getUniqueName()+" can never be on the same node!");
					}
					//1. icnc->|icn->i|->|j->jcn|->jcnc, nexthopIface is icn, cost+1
					//2. icnc->|icn->i->j->jcn|->jcnc, nexthopIface is icn, cost+1
					//2.1. if i is j, icnc->|icn->i|->icnc, self loop
					if(icnc.node.getUID()!=jcnc.node.getUID()){
						TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+2);
						return re;	
					}else{
						return null;
					}
				}
			}else{ //jcn is null, jcnc is not null
				throw new RuntimeException("jcn="+jcn+", jcnc="+jcnc+"; jcn can not be null when jcnc is not null!");				
			}		
		}
		return null;
	}
	
	/**
	 * @param icnc can be null
	 * @param icn  never null
	 * @param i    never null
	 * @param j    never null
	 * @param jc   never null
 	 * @param baseCost cost from i to j ONLY
	 * @param nhi_i_j the next hop on the way to j from i
	 * @param net  the net owns the current routing sphere, used for calRouteEntry() 
	 * @param edgeNodes  the list of edge nodes of the current sphere, used for calRouteEntry()
	 * @return
	 */
	private TempRouteEntry calcRoute_icnc(NodeAdj icnc, NodeAdj icn, NodeAdj i, NodeAdj j, NodeAdj jc, int baseCost, IInterface nhi_i_j, INet net, List<Long> edgeNodes) {
		//jprime.Console.out.println("calc icnc 2");
		if(icn==null || i==null || j==null || jc==null){
			throw new RuntimeException("icn="+icn+", i="+i+", j="+j+", jc="+jc+"; icn, i, j and jc can never be null!");
		}
		//icn and i must be on the same node
		if(i.node.getParent().getUID()!=icn.node.getParent().getUID()){
			throw new RuntimeException("i node="+i.node.getUniqueName()+" and icn node="+icn.node.getUniqueName()+" must be on the same node!");
		}
		// jc is the interface on a bus link, jc and j must be on different nodes.
		if(j.node.getParent().getUID()==jc.node.getParent().getUID()){
			throw new RuntimeException("j node="+j.node.getUniqueName()+" and jc node="+jc.node.getUniqueName()+" can never be on the same node!");
		}	
		IBaseInterface srcIface = null; 
		IBaseInterface dstIface = (IBaseInterface)jc.node; //jc
		long outboundIface = 0;
		IBaseInterface nexthopIface = null; 
		
		if(icnc == null){ //1. |icn->i|->j->jc
			//a) |icn->i|->j->jc
			//b) |icn->i->j|->ic, if i is j, |icn->i|->jc
			return null;
		}else{//2. icnc->|icn->i|->j->jc
			srcIface = (IBaseInterface)icnc.node;
			if(icn.node.getParent().getUID()==icnc.node.getParent().getUID()){
				throw new RuntimeException("icn node="+icn.node.getUniqueName()+" and icnc node="+icnc.node.getUniqueName()+" can never be on the same node!");
			}		
			//a) icnc->|icn->i|->j->jc
			//b) icnc->|icn->i->j|->jc, if i is j, icnc->|icn->i|->jc
			nexthopIface = (IBaseInterface)icn.node; //icn
			outboundIface = icnc.node.getRank(icnc.node.getParent()); //icnc
			TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+2);
			return re;	
		
		}
	}
	
	/**
	 * @param ic   never null
	 * @param i    never null
	 * @param j    never null
	 * @param jcn  can be null
	 * @param jcnc can be null
 	 * @param baseCost cost from i to j ONLY
	 * @param nhi_i_j the next hop on the way to j from i
	 * @param net  the net owns the current routing sphere, used for calRouteEntry() 
	 * @param edgeNodes  the list of edge nodes of the current sphere, used for calRouteEntry()
	 * @return
	 */
	private TempRouteEntry calcRoute_ic(NodeAdj ic, NodeAdj i, NodeAdj j, NodeAdj jcn, NodeAdj jcnc, int baseCost, IInterface nhi_i_j, INet net, List<Long> edgeNodes) {
		//jprime.Console.out.println("calc ic 1");
		if(ic==null || i==null || j==null){
			throw new RuntimeException("ic="+ic+", i="+i+", j="+j+"; ic, i and j can never be null!");
		}
				
		IBaseInterface srcIface = (IBaseInterface)ic.node; //ic
		IBaseInterface dstIface = null;
		long outboundIface = ic.node.getRank(ic.node.getParent()); //outboundIface is always ic
		IBaseInterface nexthopIface = (IBaseInterface)i.node; //nexthopIface is always i
		
		// ic is the interface on a bus link, ic and i are on different nodes.
		if(i.node.getParent().getUID()==ic.node.getParent().getUID()){
			throw new RuntimeException("i node="+i.node.getUniqueName()+" and ic node="+ic.node.getUniqueName()+" can never be on the same node!");
		}
		
		if(jcn==null && jcnc==null){ //1. ic->i->j
			if(isTarget(net, j)){
				dstIface=(IBaseInterface)j.node;
				TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
				return re;
			}else{
				return null;
			}
		}else if(jcn!=null){//ic->i->j->jcn/jcnc, jcn and j must be on the same node
			if(j.node.getParent().getUID()!=jcn.node.getParent().getUID()){
				throw new RuntimeException("j node="+j.node.getUniqueName()+" and jcn node="+jcn.node.getUniqueName()+" must be on the same node!");
			}
			if(jcnc==null){ //2.ic->i->|j->jcn|
				if(isTarget(net, jcn)){
					dstIface=(IBaseInterface)jcn.node;
					TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+1);
					return re;
				}else{
					return null;
				}
			}else{ //3. ic->i->|j->jcn|->jcnc, jcnc and jcn can never be on the different nodes
				if(jcn.node.getParent().getUID()==jcnc.node.getParent().getUID()){
					throw new RuntimeException("jcn node="+jcn.node.getUniqueName()+" and jcnc node="+jcnc.node.getUniqueName()+" can never be on the same node!");
				}
				dstIface=(IBaseInterface)jcnc.node;
				TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+2);
				return re;
			}	
		}else{ //jcn is null, jcnc is not null
			throw new RuntimeException("jcn="+jcn+", jcnc="+jcnc+"; jcn can not be null when jcnc is not null!");
		}
	}
	
	/**
	 * @param ic never null
	 * @param i  never null
	 * @param j  never null
	 * @param jc never null
 	 * @param baseCost cost from i to j ONLY
	 * @param nhi_i_j the next hop on the way to j from i
	 * @param net  the net owns the current routing sphere, used for calRouteEntry() 
	 * @param edgeNodes  the list of edge nodes of the current sphere, used for calRouteEntry()
	 * @return
	 */
	private TempRouteEntry calcRoute_ic(NodeAdj ic, NodeAdj i, NodeAdj j, NodeAdj jc, int baseCost, IInterface nhi_i_j, INet net, List<Long> edgeNodes) {
		//jprime.Console.out.println("calc ic 2");
		if(ic==null || i==null || j==null || jc==null){
			throw new RuntimeException("ic="+ic+", i="+i+", j="+j+", jc="+jc+"; ic, i, j and jc can never be null!");
		}
		IBaseInterface srcIface = (IBaseInterface)ic.node; //ic
		IBaseInterface dstIface = (IBaseInterface)jc.node; //jc
		long outboundIface = ic.node.getRank(ic.node.getParent()); //outboundIface is always ic
		IBaseInterface nexthopIface = null; 
		
		// ic is the interface on a bus link, ic and i are on different nodes.
		if(i.node.getParent().getUID()==ic.node.getParent().getUID()){
			throw new RuntimeException("i node="+i.node.getUniqueName()+" and ic node="+ic.node.getUniqueName()+" can never be on the same node!");
		}
		// jc is the interface on a bus link, jc and j are on different nodes.
		if(j.node.getParent().getUID()==jc.node.getParent().getUID()){
			throw new RuntimeException("j node="+j.node.getUniqueName()+" and jc node="+jc.node.getUniqueName()+" can never be on the same node!");
		}
		
		if(i.node.getUID()==j.node.getUID()){ //i is j, ic and jc are on the same bus link, cost is 1
			if(ic.node.getUID()!=jc.node.getUID()){
				nexthopIface = (IBaseInterface)jc.node;
				TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, 1);
				return re;
			}else{ //self loop, ignore
				return null;
			}
		}else{
			nexthopIface = (IBaseInterface)i.node;
			TempRouteEntry re = calRouteEntry(net, srcIface, dstIface, nexthopIface, outboundIface, edgeNodes, baseCost+2);
			return re;
		}	
	}

	public boolean isEdgeIface(INet net, NodeAdj i){
		boolean isEdge=false;
		for (EdgeInterfacePair iface : net.getRoutingSphere().getEdgeInterfaces()) {
			if(iface.getIfaceUID(net)==i.node.getUID()){
				isEdge=true;
				//jprime.Console.out.println("c=0, interface "+i_node.node.getUniqueName()+" is edge interface");
				break;
			}		
		}
		return isEdge;
	}
	
	private boolean isTarget(INet net, NodeAdj i){
		if(isEdgeIface(net, i)
				|| ((INet)net).isEdgeIfaceOfChildSphere((IInterface)i.node)
				|| ((i.node.getParent() instanceof IRouter) && Router.isRoutable((IRouter)i.node.getParent()))
				|| !(i.node.getParent() instanceof IRouter)){
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#printAdjMatrix(java.io.PrintStream)
	 */
	public void printAdjMatrix(PrintStream o) {
		if(matrix==null)
			throw new RuntimeException("Can call this after calling compute shortest path!");
		for(NodeAdj es : matrix) {
			if(es!=null){
				o.println(es.node.getUniqueName()+"[rank="+es.rank+"]"+" [index]="+es.idx+":");
				for(Entry<Integer, IAdjacencyEntry> e : es.adjList.entrySet()) {
					o.println("\t-->"+matrix.get(e.getKey()).node.getUniqueName()+"[index="+e.getKey()+"]"+" costs "+e.getValue().getCost());
				}
				if(es.compressed.size()>0){
					for(int i=0; i<es.compressed.size(); i++){
						o.println("\t-->compressed: "+es.compressed.get(i).idx);
					}
				}
			}
		}
	}
}