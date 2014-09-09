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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.BaseInterface.IBaseInterface;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Link.Link;
import jprime.Link.LinkReplica;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Net.NetReplica;
import jprime.Router.IRouter;
import jprime.Router.Router;
import jprime.RoutingSphere.EdgeInterfacePair;
import jprime.RoutingSphere.EdgeInterfacePairList;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.util.ChildList;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 *
 */
public class ShortestPathVisitor implements IRouteVisitor {
	private final ShortestPathAdjacencyMatrix adjMatrix;
	private Stack<IRoutingSphere> prevSphere;
	private Map<Long,EdgeInterfacePair> allInterfaces;
	private Map<Long,EdgeInterfacePair> linkAttachedInterfaces;

	public ShortestPathVisitor(INet net) {
		this.adjMatrix = new ShortestPathAdjacencyMatrix(net);
		this.prevSphere = new Stack<IRoutingSphere>();
		this.allInterfaces=new HashMap<Long,EdgeInterfacePair>();
		this.linkAttachedInterfaces=new HashMap<Long,EdgeInterfacePair>();
		this.__visit__(net);
	}
	
	/** 
	 * @param node
	 */
	private void __visit__(INet node) {
		//jprime.Console.out.println("visiting net "+node.getUID()+" ,name="+node.getUniqueName());
		Map<Long,EdgeInterfacePair> prevAllInterfaces=null;
		Map<Long,EdgeInterfacePair> prevLinkAttachedInterfaces=null;

		if(node.isRoutingSphere()){
			prevSphere.push(node.getRoutingSphere());
			prevAllInterfaces=allInterfaces;
			prevLinkAttachedInterfaces=linkAttachedInterfaces;
			allInterfaces=new HashMap<Long,EdgeInterfacePair>();
			linkAttachedInterfaces=new HashMap<Long,EdgeInterfacePair>();
		}
		for(ModelNode c : node.getAllChildren()) {
			if(c instanceof INet){
				if(((INet)c).isRoutingSphere()){
					//add all the edge interfaces of the subsphere to allInterfaces list
					//jprime.Console.out.println("\tadd all the edge interfaces of the subsphere="+c.getUniqueName()+" to allInterfaces list");
					if (((INet)c).getRoutingSphere().getEdgeInterfaces() != null) {
						for (EdgeInterfacePair iface : ((INet)c).getRoutingSphere().getEdgeInterfaces()) {
							long iface_uid = iface.getIfaceUID(c);
							//jprime.Console.out.println("iface uid="+iface_uid);
							long iface_rank = iface_uid-node.getUID()+node.getSize();
							//jprime.Console.out.println("iface rank="+iface_rank);
							long diff = c.getUID()-c.getSize();
			    			if(!allInterfaces.containsKey(iface_rank)){
			    				allInterfaces.put(iface_rank,new EdgeInterfacePair(iface, node, diff));
			    			}
						}
					}
					//jprime.Console.out.println("Skipping visiting "+c.getUniqueName()+" because its a routing sphere ....");
					continue;
				}
			}
			c.accept(this);
		}
		//set the edge interfaces for this net if it is a routing sphere
		if(node.isRoutingSphere()){
			//jprime.Console.out.println("\tallInterfaces="+allInterfaces+" ,linkAttachedInterfaces="+linkAttachedInterfaces);
			//jprime.Console.out.println("\tbefore allInterfaces.size()="+allInterfaces.size());
			for(long key : linkAttachedInterfaces.keySet()) {
				allInterfaces.remove(key);
			}
			//jprime.Console.out.println("\tafter allInterfaces.size()="+allInterfaces.size());
			EdgeInterfacePairList l = node.getRoutingSphere().getEdgeInterfaces();
			l.addAll(allInterfaces.values());
			//add the edge interfaces to the targets
			Iterator<EdgeInterfacePair> it=l.iterator();
			while(it.hasNext()){
				EdgeInterfacePair e=it.next();
				if(!adjMatrix.targets.contains(e.iface_max_rank)){
					adjMatrix.targets.add(e.iface_max_rank);
				}
			}
		}
		//Add the edge interfaces of the direct child routing spheres into the adj_list;
        //calculate cost for the path of the special nodes pairs (if this node has such pairs) 
		//using the route_table that has been stored already
		for(ModelNode c : node.getAllChildren()) {	
			if(c instanceof INet) {
				if(((INet)c).isRoutingSphere()){
					List<EdgeInterfacePair> subEdgeIfaces=((INet)c).getRoutingSphere().getEdgeInterfaces();
					if(subEdgeIfaces.size()>0){
						//jprime.Console.out.println("    edge interface size="+subEdgeIfaces.size());
						for(EdgeInterfacePair ir : subEdgeIfaces){
							IModelNode node_i = c.getChildByRank(ir.iface_max_rank, c);
							if(node_i == null) {
								throw new RuntimeException("Cant find rank "+ir.iface_max_rank+", from anchor="+c.getUniqueName());
							}
							//jprime.Console.out.println("    edge interface ="+node_i.getUniqueName());
							//add node_i to the targets
							if(!adjMatrix.targets.contains(node_i.getRank(adjMatrix.anchor))){
								adjMatrix.targets.add(node_i.getRank(adjMatrix.anchor));
							}					
							//jprime.Console.out.println("Found rank "+ir.iface_max_rank+"("+c.getUniqueName()+")-->"+node_i.getUniqueName());
							for(EdgeInterfacePair jr : subEdgeIfaces){
								IModelNode node_j = c.getChildByRank(jr.iface_max_rank, c);
								if(node_j == null) {
									throw new RuntimeException("Cant find rank "+jr.iface_max_rank+", from anchor="+c.getUniqueName());
								}
								//jprime.Console.out.println("Found rank "+jr.iface_max_rank+"("+c.getUniqueName()+")-->"+node_j.getUniqueName());
								if(node_i != node_j && !adjMatrix.containAdjEntry(node_i,node_j)){
									RouteEntry re=((INet)c).getRoutingSphere().getRouteTable().lookupPermRoute(
											ir.iface_min_rank,
											ir.iface_max_rank,
											jr.iface_min_rank,
											jr.iface_max_rank);
									if(re!=null){
										adjMatrix.addEdge(node_i,node_j,re.getCost());
									    //jprime.Console.out.println("node i="+node_i.getUniqueName()+" , node j="+node_j.getUniqueName()+", cost="+re.getCost());
									}
									else if(node_i.getParent()==node_j.getParent()){
										adjMatrix.addEdge(node_i,node_j,0);
										//jprime.Console.out.println("node i="+node_i.getUniqueName()+" , node j="+node_j.getUniqueName()+", cost=0");
									}
								}
							}
						}
					}
				}
			}
		}
		if(node.isRoutingSphere()){
			//find the parent sphere	
			if(prevSphere.peek()==node.getRoutingSphere()){
				prevSphere.pop();
				allInterfaces=prevAllInterfaces;
				linkAttachedInterfaces=prevLinkAttachedInterfaces;
			}
			else {
				throw new RuntimeException("Should never see this!");
			}
		}
	}	
	/** 
	 * @param node
	 */
	private void __visit__(ILink node) {
		IRoutingSphere thisSphere=prevSphere.peek();
		ChildList<IBaseInterfaceAlias>.ChildIterator attachments1 = node.getAttachments().enumerate();
	    while(attachments1.hasNext()) {
	    	IInterface hostNic1=(IInterface)attachments1.next().deference();
	    	//check the parent of this interface, if it is a host or emulated router
	    	//add the interface to the targets list
	    	if(hostNic1.getParent() instanceof IRouter) {
				if(Router.isRoutable((IRouter)hostNic1.getParent())) { //is routable
					if(!adjMatrix.targets.contains(hostNic1.getRank(adjMatrix.anchor))){
						adjMatrix.targets.add(hostNic1.getRank(adjMatrix.anchor));
					}
				}
			} else { //host
				if(!adjMatrix.targets.contains(hostNic1.getRank(adjMatrix.anchor))){
					adjMatrix.targets.add(hostNic1.getRank(adjMatrix.anchor));
				}
			}	
	    	//add link attached interface into the linkAttachedInterfaces list for calculating the edge interface
	    	Long rank = hostNic1.getRank(thisSphere.getParent());
	    	if(!linkAttachedInterfaces.containsKey(rank)){
	    		linkAttachedInterfaces.put(rank, new EdgeInterfacePair(hostNic1, thisSphere.getParent()));
	    	}
	    	//add all interfaces on the attached interface's parent (host) to allInterfaces list
	    	if(getRoutingSphere(hostNic1) == getRoutingSphere(node) ){
	    		ChildList<IInterface>.ChildIterator hostIfaces = ((IHost)(hostNic1.getParent())).getNics().enumerate();
	    		while(hostIfaces.hasNext()){
	    			IInterface hostInterface=(IInterface)hostIfaces.next();
	    			rank = hostInterface.getRank(thisSphere.getParent());
	    			if(!allInterfaces.containsKey(rank)){
	    				allInterfaces.put(rank,new EdgeInterfacePair(hostInterface, thisSphere.getParent()));
	    			}
	    		}
    		}
	    	//Add direct links to the matrix
	    	ChildList<IBaseInterfaceAlias>.ChildIterator attachments2 = node.getAttachments().enumerate();
	    	while(attachments2.hasNext()){    		
	    		IBaseInterface hostNic2=(IBaseInterface)attachments2.next().deference();
	    		//jprime.Console.out.println("hostNic1="+hostNic1.getUniqueName()+", hostNic2="+hostNic2.getUniqueName());
	    		//jprime.Console.out.println("hostNic1.getUID()!=hostNic2.getUID() is: "+(hostNic1.getUID()!=hostNic2.getUID()) );
	    		//jprime.Console.out.println(adjMatrix.getAdjListForNode(hostNic1).containsKey(hostNic2.getRank(node.getParent())));
	    		if(hostNic1.getUID()!=hostNic2.getUID() 
	    				&& hostNic1.getParent().getUID()!=hostNic2.getParent().getUID() 
	    				&& !adjMatrix.containAdjEntry(hostNic1,hostNic2)){
	    			adjMatrix.addEdge(hostNic1, hostNic2, 1);
	    		}	
	    	}
	    	//Add links with cost 0 for the interfaces who have the same parent (host).
	    	//We only care about the node in this sphere, and don't care about the node in its sub-sphere.
	    	INet owningNet=(INet)hostNic1.getParent().getParent();
	    	IRoutingSphere owningSphere=owningNet.getRoutingSphere();
	    	while(!owningNet.isRoutingSphere()){
	    		owningNet=(INet)owningNet.getParent();
	    		owningSphere=owningNet.getRoutingSphere();		
	    	}
	    	if(owningSphere==thisSphere){
	    		ChildList<IInterface>.ChildIterator hostNics = ((IHost)(hostNic1.getParent())).getNics().enumerate();
		    	List<IInterface> ifaces = new ArrayList<IInterface>();   
	    		while(hostNics.hasNext()){
	    			ifaces.add(hostNics.next());
	    		}
	    		for(IBaseInterface i : ifaces){
	    			if(i!=hostNic1 && !adjMatrix.containAdjEntry(i,hostNic1)){
	    				adjMatrix.addEdge(i,hostNic1,0);
	    			}
	    			/* XXX old way
	    			for(IBaseInterface j : ifaces){
	    				if(i!=j && !adjMatrix.containAdjEntry(i,j)){
	    					adjMatrix.addEdge(i,j,0);
	    				}
	    			}*/
	    		}
	    	}
	    }
	}	
	private IRoutingSphere getRoutingSphere(IModelNode m) {
		if(m == null) return null;
		if(m instanceof INet) {
			if (((INet)m).isRoutingSphere()) return ((INet)m).getRoutingSphere();
		}
		return getRoutingSphere(m.getParent());
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteVisitor#visit(jprime.Net.Net)
	 */
	public void visit(Net node) {
		__visit__(node);
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteVisitor#visit(jprime.Net.NetReplica)
	 */
	public void visit(NetReplica node) {
		__visit__(node);
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteVisitor#visit(jprime.Link.Link)
	 */
	public void visit(Link node) {
		__visit__(node);
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteVisitor#visit(jprime.Link.LinkReplica)
	 */
	public void visit(LinkReplica node) {
		__visit__(node);
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteVisitor#visit(jprime.ModelNode)
	 */
	public void visit(ModelNode node) {		
		//we dont care about nodes which are not nets
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteVisitor#getAdjacenyMatrix()
	 */
	public ShortestPathAdjacencyMatrix getAdjacenyMatrix() {
		return adjMatrix;
	}
	
}

