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
import java.util.List;

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
import jprime.RoutingSphere.EdgeInterfacePair;
import jprime.util.ChildList;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 */
public class BGPVisitor implements IRouteVisitor {
	private final BGPAdjacencyMatrix adjMatrix;
	private final List<BGPRelationShipType> bgpRelationships;
	
	public BGPVisitor(INet net) {
		this.adjMatrix = new BGPAdjacencyMatrix(net);	
		bgpRelationships=new ArrayList<BGPRelationShipType>();
		for(BGPRelationShipType i : BGPRelationShipType.values()){
			bgpRelationships.add(i);
		}
		this.__visit__(net);
	}
	
	/** 
	 * @param node
	 */
	private void __visit__(INet node) {
		for(BGPLinkType i : ((BGP)node.getStaticRoutingProtocol()).getLinkTypes()){
			ChildList<IBaseInterfaceAlias>.ChildIterator linkAttach1 = i.getLink().getAttachments().enumerate();
			while(linkAttach1.hasNext()) {
				IBaseInterface hostNic1=(IBaseInterface)linkAttach1.next().deference();
				ChildList<IBaseInterfaceAlias>.ChildIterator linkAttach2 = i.getLink().getAttachments().enumerate();
				//add direct links to the adjMatrix
				while(linkAttach2.hasNext()){		
					IBaseInterface hostNic2=(IBaseInterface)linkAttach2.next().deference();
				
					if(!adjMatrix.getAdjListForNode(hostNic2).containsKey(hostNic1.getRank(node))){
						if(hostNic1.getParent().getUID()!=hostNic2.getParent().getUID() && !adjMatrix.getAdjListForNode(hostNic1).containsKey(hostNic2.getRank(node))){
							adjMatrix.addEdge(hostNic1, hostNic2, i.getLinkType(), 1);
							adjMatrix.addEdge(hostNic2, hostNic1, 
								bgpRelationships.get(bgpRelationships.size()-bgpRelationships.indexOf(i.getLinkType())), 1);
						}else if(hostNic1.getParent().getUID()==hostNic2.getParent().getUID()){
							adjMatrix.addEdge(hostNic1, hostNic2, BGPRelationShipType.EXTRA, 0);
						}
					}
				}
				//Add links with cost 0 for the interfaces who have the same parent (host).
		    	//We only care about the node in this sphere, and don't care about the node in its sub-sphere.
		    	List<IInterface> ifaces = new ArrayList<IInterface>();
		    	if(hostNic1.getParent().getParent().getUID()==node.getUID()){
		    		ChildList<IInterface>.ChildIterator hostNics = ((IHost)(hostNic1.getParent())).getNics().enumerate();
		    		while(hostNics.hasNext()){
		    			ifaces.add(hostNics.next());
		    		}
		   
		    		for(IBaseInterface j : ifaces){
		    			for(IBaseInterface k : ifaces){
		    				if(!adjMatrix.getAdjListForNode(j).containsKey(k.getRank(node)) ){
		    					if(j==k){ //for the same nodes
		    						adjMatrix.addEdge(j,k,BGPRelationShipType.EXTRA,0);
		    					}else{ //for the different nodes on one host
		    						adjMatrix.addEdge(j,k,BGPRelationShipType.EXTRA,0);
		    					}
		    				}
		    			}
		    		}
		    	}
	    	}
		}
		
    	//then we add special interfaces of the child super nodes in the adj_list;
        //calculate cost for the path of the special nodes pairs (if this node has such pairs) using the 
        //route_table that has been stored already
		for(ModelNode c : node.getAllChildren()){
			if(c instanceof INet && ((INet)c).getRoutingSphere()!=null){
				List<EdgeInterfacePair> subEdgeIfaces=((INet)c).getRoutingSphere().getEdgeInterfaces();
				if(subEdgeIfaces.size()>1){
					for(EdgeInterfacePair ir : subEdgeIfaces){
						IModelNode node_i = c.getChildByRank(ir.iface_max_rank, c);
						if(node == null) {
							throw new RuntimeException("Cant find rank "+ir.iface_max_rank+", from anchor="+c.getUniqueName());
						}
						for(EdgeInterfacePair jr : subEdgeIfaces){
							IModelNode node_j = c.getChildByRank(jr.iface_max_rank, c);
							if(node == null) {
								throw new RuntimeException("Cant find rank "+jr.iface_max_rank+", from anchor="+c.getUniqueName());
							}
					
							if(node_i != node_j && !adjMatrix.getAdjListForNode(node_i).containsKey(node_j.getRank(node))){
								RouteEntry re=((INet)c).getRoutingSphere().getRouteTable().lookupPermRoute(
										ir.iface_min_rank,
										ir.iface_max_rank,
										jr.iface_min_rank,
										jr.iface_max_rank);
								
								if(re!=null){
									adjMatrix.addEdge(node_i,node_j,BGPRelationShipType.EXTRA,re.getCost());
									adjMatrix.addEdge(node_j,node_i,BGPRelationShipType.EXTRA,re.getCost());
								}
							}
							if(node_i.getParent() == node_j.getParent() && !adjMatrix.getAdjListForNode(node_i).containsKey(node_j.getRank(node))){
								adjMatrix.addEdge(node_i,node_j,BGPRelationShipType.EXTRA,0);
							}
						}
					}
				}	
			}
			//if the sub_net is not a routing sphere, it should be applied BGP and part of the top sphere.
			if(c instanceof INet && ((INet)c).getRoutingSphere()==null){
				c.accept(this);
			}
		}
	}
	/** 
	 * @param node
	 */
	private void __visit__(ILink node) {
		
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
		if(!node.isReplica()) {
			__visit__(node);
        }
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
	public IAdjacencyMatrix getAdjacenyMatrix() {
		return adjMatrix;
	}
}

