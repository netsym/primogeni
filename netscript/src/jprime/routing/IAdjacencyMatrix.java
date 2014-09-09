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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import jprime.IModelNode;
import jprime.Net.INet;
import jprime.RouteTable.IRouteTable;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 *
 */
public interface IAdjacencyMatrix {
	
	/**
	 * @author Nathanael Van Vorst
	 * @author Ting Li
	 */
	public static class RouteCounts {
		public final int merged, total;

		public RouteCounts(int merged, int total) {
			super();
			this.merged = merged;
			this.total = total;
		}
	}
	/**
	 * @author Nathanael Van Vorst
	 * @author Ting Li
	 */
	public static class NodeAdj {
		public final int idx;
		public final long rank;
		public final IModelNode node;
		public Map<Integer, IAdjacencyEntry> adjList = new HashMap<Integer, IAdjacencyEntry>();
		public Map<Integer, IAdjacencyEntry> r_adjList = new HashMap<Integer, IAdjacencyEntry>();
		public boolean bothExistInAdj(NodeAdj j) {
			if(adjList.containsKey(j.idx)) {
				return true;
			}
			else if(r_adjList.containsKey(j.idx)) {
				return true;
			}
			return false;
		}
		public Vector<NodeAdj> compressed = new Vector<NodeAdj>();
		public NodeAdj(IModelNode node, IModelNode anchor,int idx) {
			this.node=node;
			this.rank = node.getRank(anchor);
			this.idx=idx;
		}
	}
	/**
	 * @author Nathanael Van Vorst
	 * @author Ting Li
	 */
	public static final class BGPAdjEntry {
		public final Long node;
		public BGPRelationShipType relationship;
		public int cost;
		public BGPAdjEntry(Long node, BGPRelationShipType r, int cost) {
			this.node=node;
			this.relationship=r;
			this.cost=cost;
		}
	}
	
	/**
	 * @author Nathanael Van Vorst
	 * @author Ting Li
	 */
	public static interface IAdjacencyEntry {
		/**
		 * @return
		 */
		public int getCost();
		/**
		 * @return
		 */
		public BGPRelationShipType getRelationship();
		
	}

	/**
	 * @param src
	 * @param dst
	 * @return
	 */
	public abstract boolean containAdjEntry(IModelNode src, IModelNode dst);
	
	/**
	 * @param left
	 * @param right
	 */
	public abstract void addEdge(IModelNode left, IModelNode right);

	/**
	 * populate the adj matrix
	 */
	public abstract RouteCounts computeShortestPath(INet net);

	/**
	 * @param tbl
	 * @param net
	 */
	public abstract RouteCounts storeRoutes(IRouteTable tbl, INet net);
	
	/**
	 * @param o
	 */
	public void printAdjMatrix(PrintStream o);
	
}