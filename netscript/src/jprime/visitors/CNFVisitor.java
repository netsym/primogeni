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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import jprime.IModelNode;
import jprime.CNFApplication.ICNFApplication;
import jprime.CNFTransport.ICNFTransport;
import jprime.Host.IHost;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;

/**
 * @author Nathanael Van Vorst
 * @author Rong Rong
 * 
 * 1) For each network will be find the CNF controller and tell it about other cnf enabled routers in that network
 * 2) For each each network we will find all cnf-contents and build a map to those resources
 */
public class CNFVisitor {
	private Stack<Set<IModelNode>> cnf_routers = new Stack<Set<IModelNode>>();
	/**
	 * @param topnet
	 */
	public CNFVisitor(Net topnet) {
		jprime.Console.out.println("skipping cnf visitor in compiliation phase....");
		//visit(topnet);
	}

	private void visit(IModelNode n) {
		if(n instanceof INet) {
			visitNet((INet)n);
		}
	}

	private void visitNet(INet net) {
		IRouter controller=null;
		ICNFTransport controller_cnf_trans=null;
		cnf_routers.push(new TreeSet<IModelNode>());
		HashMap<Integer, Long> contents = new HashMap<Integer, Long>();
		for(IModelNode c : net.getAllChildren()) {
			visit(c);
			if(c instanceof INet) {
				HashMap<Integer, Long> sub_contents = ((INet)c).getCNFContent2RIDMap();
				if(sub_contents==null) continue;
				long rank_c = c.getRank(net);
				long temp = rank_c-c.getSize();
				for(Entry<Integer, Long> e : sub_contents.entrySet()) {
					//recall that R_a^c = R_b^c+R_a_b-S_b
					Long prev = contents.put(e.getKey(), e.getValue()+temp); 
					if ( prev != null) {
						jprime.Console.err.println("WARNING: the CNF content "+e.getKey()+" is found in multiple locations: "+prev+", "+e.getValue()+temp);
					}
				}
			}
			else if(c instanceof IRouter) {
				//check if it in the controller
				if(((IRouter)c).isCNFController()) {
					if(controller != null) {
						jprime.Console.err.println("WARNING: Found more than one controller for net "+c.getUniqueName());
					}
					for(IModelNode cc : c.getAllChildren()) {
						if(cc instanceof ICNFTransport) {
							controller_cnf_trans=(ICNFTransport)cc;
							break;
						}
					}
					controller=(IRouter)c;
				}
				else {
					//check if it has the cnf transport -- i.e. is a cnf router
					for(IModelNode cc : c.getAllChildren()) {
						if(cc instanceof ICNFTransport) {
							cnf_routers.peek().add(c);
						}
					}
				}
			}
			else if(c instanceof IHost) {
				for(IModelNode cc : c.getAllChildren()) {
					if(cc instanceof ICNFApplication) {
						HashMap<Integer,Integer> cids = ((ICNFApplication)cc).getContentIds();
						if (cids == null) continue;
						if(cids != null) {
							long rank = c.getRank(net);
							for(Integer cid : cids.keySet()) {
								contents.put(cid,rank);
							}
						}
					}
				}
			}
		}
		if(contents.size()>0) {
			for(Entry<Integer,Long> e : contents.entrySet()) {
				net.addCNFContent(e.getKey(),e.getValue());
			}
		}
		Set<IModelNode> cur_cnf_routers = cnf_routers.pop();
		if(controller != null) {
			net.setControllerRid(controller.getRank(net));
			if(cur_cnf_routers.size()>0) {
				for(IModelNode cr : cur_cnf_routers) {
					controller_cnf_trans.addCNFRouter(cr.getRank(net));
				}
			}
		}
		else {
			Set<IModelNode> parent_cnf_routers = (cnf_routers.size()>0)?cnf_routers.peek():null;
			if(parent_cnf_routers != null) {
				parent_cnf_routers.addAll(cur_cnf_routers);
			}
			else if(cur_cnf_routers != null && cur_cnf_routers.size()>0) {
				//no controllers were set anywhere in this sub-tree
				String routers=null;
				for(IModelNode c : cur_cnf_routers) {
					if(routers==null)
						routers="[";
					else
						routers+=",";
					routers+=c.getUniqueName();
				}
				throw new RuntimeException("Unable to find the controller for the following cnf routers:"+routers+"]");
			}
		}
	}

}
