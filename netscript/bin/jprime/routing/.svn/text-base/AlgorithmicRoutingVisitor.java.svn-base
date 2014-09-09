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

import jprime.util.ChildList;
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



/**
 * @author Ting Li
 *
 */
public class AlgorithmicRoutingVisitor implements IRouteVisitor {
	private final AlgorithmicAdjacencyMatrix adjMatrix;
	
	public AlgorithmicRoutingVisitor(INet net) {
		this.adjMatrix = new AlgorithmicAdjacencyMatrix(net);
		this.__visit__(net);
	}
	
	/** 
	 * @param node
	 */
	private void __visit__(INet node) {
		for(ModelNode c : node.getAllChildren()) {
			c.accept(this);
		}	
	}
	
	/** 
	 * @param node
	 */
	private void __visit__(ILink node) {
		//Add direct links to the matrix
		ChildList<IBaseInterfaceAlias>.ChildIterator attachments1 = node.getAttachments().enumerate();
	    while(attachments1.hasNext()) {
	    	IBaseInterface hostNic1=(IBaseInterface)attachments1.next().deference();
	    	//check the parent of this interface, if it is a host or emulated router
	    	//add the interface to the targets list
	    	//we only allow one sphere algorithmic routing, so we don't have edge interfaces here.
	    	if(hostNic1.getParent() instanceof IRouter) {
				if(((IRouter)hostNic1.getParent()).hasEmulationProtocol()) { //emulated router
					if(!adjMatrix.targets.contains(hostNic1.getRank(adjMatrix.anchor))){
						adjMatrix.targets.add(hostNic1.getRank(adjMatrix.anchor));
					}
				}
			} else { //host
				if(!adjMatrix.targets.contains(hostNic1.getRank(adjMatrix.anchor))){
					jprime.Console.out.println("add targets");
					adjMatrix.targets.add(hostNic1.getRank(adjMatrix.anchor));
				}
			}	
	    	ChildList<IBaseInterfaceAlias>.ChildIterator attachments2 = node.getAttachments().enumerate();
	    	while(attachments2.hasNext()){
	    		IBaseInterface hostNic2=(IBaseInterface)attachments2.next().deference();
	    		if(hostNic1.getUID()!=hostNic2.getUID() && !adjMatrix.containAdjEntry(hostNic1,hostNic2)){
	    			adjMatrix.addEdge(hostNic1, hostNic2,1);
	    		}
	    	}
	    	//Add links with cost 0 for the interfaces who have the same parent (host).
	    	List<IInterface> ifaces = new ArrayList<IInterface>();
	    	ChildList<IInterface>.ChildIterator host_nics = ((IHost)(hostNic1.getParent())).getNics().enumerate();
	    	while(host_nics.hasNext()){
	    		ifaces.add(host_nics.next());
	    	}
	    	for(IBaseInterface i : ifaces){
	    		for(IBaseInterface j : ifaces){
	    			if(i!=j && !adjMatrix.containAdjEntry(i,j)){
	    				adjMatrix.addEdge(i,j,0);
	    			}
	    		}
	    	}
	    }   	    
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
	public IAdjacencyMatrix getAdjacenyMatrix() {
		return adjMatrix;
	}

}

