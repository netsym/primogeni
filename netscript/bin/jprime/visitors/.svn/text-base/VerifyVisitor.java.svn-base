package jprime.visitors;

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


import java.util.Stack;

import jprime.ModelNode;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Net.NetReplica;
import jprime.routing.StaticRoutingProtocol;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 * 
 * this does two things:
 * 
 * 1) verifies that each net has a proper routing strategy
 * 2) create a Traffic to hold dynamic traffics
 *
 */
public class VerifyVisitor {
	private Stack<StaticRoutingProtocol> previousRoutingProtocol;
	public VerifyVisitor(Net net) {
		this.previousRoutingProtocol = new Stack<StaticRoutingProtocol>();
		this.visit(net);
	}
	
	/** 
	 * @param node
	 */
	private void __visit__(INet node) {
		if(node == null) {
			throw new NullPointerException("node was null");
		}
		boolean found_dyn=false;
		try {
			if(null != node.getChildByName(jprime.Traffic.ITraffic.DYN_NAME)) {
				found_dyn=true;
			}
		} catch(Exception e) {
			found_dyn=false;
		}
		if(!found_dyn && !node.isReplica() && !node.hasBeenReplicated()) {
			node.createTraffic(jprime.Traffic.ITraffic.DYN_NAME);
		}
		
		boolean do_pop=false;
		int subnet_count=0;
		if(node.getParent()==null) {
			//the topnet must have a routing protocol
			if(node.getStaticRoutingProtocol()==null) {
				//lets add shortest path
				node.createShortestPath();
			}
			do_pop=true;
			previousRoutingProtocol.push(node.getStaticRoutingProtocol());			
		}
		else {
			StaticRoutingProtocol prev_proto = previousRoutingProtocol.peek();
			//its not the top
			if(node.getStaticRoutingProtocol()!=null) {
				do_pop=true;
				StaticRoutingProtocol this_proto=node.getStaticRoutingProtocol();
				previousRoutingProtocol.push(this_proto);
				//check that this_proto and prev_proto are compatible
				//We only allow algorithmic protocol to be applied to the topnet and 
				//we only do global routing based on algorithmic protocol, any protocols
				//assigned to the sub-sphere are not allowed
				if(this_proto.isAlgorithmicProtocol() || prev_proto.isAlgorithmicProtocol()){
					throw new RuntimeException("When applying Alg_routing to the networks, there can be only one routing sphere. Found second sphere:"+node.getUniqueName()+", type="+this_proto);	
				}
			}
			else {
				//check that its okay for this child to not have a routing protocol
				//and add one if necessary (if set do_pop to true and add it to previous proto)
				//If the prev_proto is AS level protocol, this protocol should not be null.
				//We must set this protocol to be shortest path if it is null.
				if(prev_proto.isASLevelProtocol()){
					node.createShortestPath();
					do_pop=true;
					previousRoutingProtocol.push(node.getStaticRoutingProtocol());
				}
			}
		}
		for(ModelNode c : node.getAllChildren()) {
			if(c instanceof INet){
				subnet_count+=1;
			}
			c.accept(this);
		}
		
		//remove this for testing as topologies
		if(node.getStaticRoutingProtocol()!=null && node.getStaticRoutingProtocol().isASLevelProtocol() && subnet_count<2){
			throw new RuntimeException("AS level routing protocols require 2 or more subnets to route between.");		
		}
		
		if(do_pop) {
			previousRoutingProtocol.pop();
		}
		
		//make sure everyone has a route table...
		node.getRoutingSphere().getRouteTable();
	}

	
	/** 
	 * @param node
	 */
	public void visit(Net node) {
		__visit__(node);
	}

	/** 
	 * @param node
	 */
	public void visit(NetReplica node) {
		if(!node.isReplica()) {
			__visit__(node);
        }
	}

	/** 
	 * @param node
	 */
	public void visit(ModelNode node) {
		//we dont care about nodes which are not nets
	}


}
