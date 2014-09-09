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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import jprime.ModelNode;
import jprime.StatusListener;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Net.NetAlias;
import jprime.Net.NetAliasReplica;
import jprime.Net.NetReplica;
import jprime.RouteTable.IRouteTable;
import jprime.routing.IAdjacencyMatrix.RouteCounts;
import jprime.util.PersistentChildList;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 */
public class RouteCalculationVisitor {
	/**
	 * @author Nathanael Van Vorst
	 * @author Ting Li
	 *
	 */
	private static class IntComparator implements Comparator<Integer> {
		public int compare (Integer o1, Integer o2) {
			if(o1 == o2) return 0;
			if(o1 < o2) return 1;
			return -1;
		}
	
	}
	
	private HashMap<Integer, Vector<INet>> nets;
	private int depth;
	
	/**
	 * @param o
	 * @param n
	 */
	public void printRouteEntries(PrintStream o, INet n){
		Iterator<RouteEntry> it=n.getRoutingSphere().getRouteTable().getPermRouteEntries().iterator();
		while(it.hasNext()){
			IRouteEntry re=it.next();
			o.println(re.toString());
		}
	}
	
	/**
	 * @param net
	 */
	public RouteCalculationVisitor(StatusListener sl, int percent, Net net) {
		nets = new HashMap<Integer, Vector<INet>>();
		depth=0;
		int merged=0, total=0;
		this.__visit__(net);
		TreeSet<Integer> keys = new TreeSet<Integer>(new IntComparator());
		keys.addAll(nets.keySet());

		int s = 0;
		for(int d :  keys)
			s+=nets.get(d).size();

		int per = percent / s;
		if(per<=0) per=1;

		for(int d :  keys) {
			for(INet n : nets.get(d)) {
				//jprime.Console.out.println("net is:"+n.getUID()+", depth="+d+", name="+n.getName()+", unique name="+n.getUniqueName());
				StaticRoutingProtocol this_proto=n.getStaticRoutingProtocol();
				final IRouteTable rt = n.getRoutingSphere().getRouteTable();//we need to make sure we don't loose this reference!
				long b = System.currentTimeMillis();
				IRouteVisitor rv = this_proto.getRouteVisitor(n);
				long c = System.currentTimeMillis();
				jprime.Console.out.println("net="+n.getUniqueName()+", adj build time="+(c-b));
				b=c;
				IAdjacencyMatrix adj = rv.getAdjacenyMatrix();
				
				//if(d==2){
				/*if(n.getUID()==214){
					jprime.Console.out.println("Adj matrix for "+n.getUniqueName());
					adj.printAdjMatrix(jprime.Console.out);
					if(n.getRoutingSphere().getEdgeInterfaces()!=null){
						jprime.Console.out.println("The size of the edge interfaces is: "+n.getRoutingSphere().getEdgeInterfaces().size());
					}
					
				}*/
				RouteCounts rc = adj.computeShortestPath(n);
				c = System.currentTimeMillis();
				jprime.Console.out.println("net="+n.getUniqueName()+", route calc time="+(c-b));
				b=c;
				if(rc!=null){
					merged+=rc.merged;
					rc=adj.storeRoutes(rt, n);
					merged+=rc.merged;
					total+=rc.total;
					rt.clearTempRoutes();
					c = System.currentTimeMillis();
					jprime.Console.out.println("net="+n.getUniqueName()+", route store time="+(c-b)+", merged="+rc.merged);
				}
				else {
					c = System.currentTimeMillis();
					jprime.Console.out.println("net="+n.getUniqueName()+", route store time="+(c-b)+", merged=0");
				}
				//this.printRouteEntries(jprime.Console.out, n);	
				sl.finsihed(per);	
			}
		}
		jprime.Console.out.println("Total routes=[Compressed:"+total+", Uncompressed:"+(merged+total)+"]");
	}
	
	/** 
	 * @param node
	 */
	private void __visit__(INet node) {
		depth++;
		if(null!=node.getStaticRoutingProtocol()) {
			if(!nets.containsKey(depth)) {
				nets.put(depth, new Vector<INet>());
			}
			nets.get(depth).add(node);
		}
		PersistentChildList cl = node.getPersistentChildList();
		for(int i=0;i<cl.size();i++) {
			if(NetReplica.isSubType(cl.getTypeAt(i))) {
				if( ! NetAliasReplica.isSubType(cl.getTypeAt(i))) {
					((NetReplica)cl.get(i)).accept(this);
				}
			}
			else if(Net.isSubType(cl.getTypeAt(i))) {
				if( ! NetAlias.isSubType(cl.getTypeAt(i))) {
					((Net)cl.get(i)).accept(this);
				}
			}
		}
		/* old
		for(ModelNode c : node.getAllChildren()) {
			c.accept(this);
		}
		 * 
		 */
		depth--;
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
