package jprime.util;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject.PersistableState;
import jprime.ResourceIdentifier.EvalutedResourceID;
import jprime.ResourceIdentifier.ResourceID;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.routing.IRouteVisitor;
import jprime.routing.RouteCalculationVisitor;
import jprime.variable.Dataset;
import jprime.variable.Dataset.SimpleDatum;
import jprime.variable.ModelNodeVariable;
import jprime.variable.ModelNodeVariable.VizAttr;
import jprime.visitors.IGenericVisitor;
import jprime.visitors.IPAddressAssignment;
import jprime.visitors.TLVVisitor;
import jprime.visitors.UIDAssignmentVisitor;
import jprime.visitors.VerifyVisitor;
import jprime.visitors.XMLVisitor;


/**
 * @author Nathanael Van Vorst
 */
public class RankSearchNode implements IModelNode, Comparator<IModelNode> {
	public static class NodeIdxPair {
		public final int idx;
		public final IModelNode node;
		public NodeIdxPair(int idx, IModelNode node) {
			super();
			this.idx = idx;
			this.node = node;
		}
	}
	public static class NodeTripple {
		public IModelNode prev,cur,next;
		public NodeTripple(IModelNode prev, IModelNode cur, IModelNode next) {
			super();
			this.prev = prev;
			this.cur = cur;
			this.next = next;
		}
	}
	public final long rank;
	public final IModelNode anchor;
	public RankSearchNode(final long rank,final IModelNode anchor) {
		this.rank=rank;
		this.anchor=anchor;
	}
	public long getRank(IModelNode a) {
		if(anchor ==a) return rank;
		throw new RuntimeException("Used invalid anchor!");
	}
	public long getMaxRank(IModelNode a) {
		if(anchor ==a) return rank;
		throw new RuntimeException("Used invalid anchor!");
	}
	public long getMinRank(IModelNode a) {
		if(anchor ==a) return rank;
		throw new RuntimeException("Used invalid anchor!");
	}
	public int compare(IModelNode o1, IModelNode o2) {
		final long min_rank1 = o1.getMinRank(anchor);
		final long min_rank2 = o2.getMinRank(anchor);
		final long max_rank1 = o1.getMaxRank(anchor);
		final long max_rank2 = o2.getMaxRank(anchor);
		
		if(min_rank1<=min_rank2  && max_rank1>=max_rank2) {
			//o1 contains o2
			return 0;
		}
		else if(min_rank2<=min_rank1  && max_rank2>=max_rank1) {
			//o2 contains o1
			return 0;
		}
		else if(max_rank1<max_rank2)
			return -1;
		return 1;
	}
	public int compareTo(IModelNode o) {
		throw new RuntimeException("Dont call this!");
	}
	public long getDBID() {
		throw new RuntimeException("Dont call this!");
	}
	public long getParentId() {
		throw new RuntimeException("Dont call this!");
	}
	public long getUID() {
		throw new RuntimeException("Dont call this!");
	}

	public void setUID(long id) {
		throw new RuntimeException("Dont call this!");
	}

	public long getMinUID() {
		throw new RuntimeException("Dont call this!");
	}

	public boolean containsRank(IModelNode anchor, long rank) {
		throw new RuntimeException("Dont call this!");
	}

	public boolean containsUID(long uid) {
		throw new RuntimeException("Dont call this!");
	}
	
	public boolean hasBeenReplicated() {
		throw new RuntimeException("Dont call this!");
	}

	public String getName() {
		throw new RuntimeException("Dont call this!");
	}

	public long getSize() {
		throw new RuntimeException("Dont call this!");
	}

	public void setSize(long s) {
		throw new RuntimeException("Dont call this!");
	}

	public long getOffset() {
		throw new RuntimeException("Dont call this!");
	}

	public IModelNode copy(String name, IModelNode parent) {
		throw new RuntimeException("Dont call this!");
	}

	public void setOffset(long o) {
		throw new RuntimeException("Dont call this!");
	}

	public int getOrder() {
		throw new RuntimeException("Dont call this!");
	}

	public int getTypeId() {
		throw new RuntimeException("Dont call this!");
	}

	public IModelNode deference() {
		throw new RuntimeException("Dont call this!");
	}
	
	public void delete() {
		throw new RuntimeException("Dont call this!");
	}
	
	public ModelNodeVariable setAttribute(int varId, String value) throws InstantiationException, IllegalAccessException {
		throw new RuntimeException("Dont call this!");
	}
	
	public Map<String, String> getAttributes(boolean includeDefaultValues) {
		throw new RuntimeException("Dont call this!");
	}
	
	public Vector<SimpleDatum> getTimeSeriesByName(Dataset dataset, String name) {
		throw new RuntimeException("Dont call this!");
	}

	public Vector<SimpleDatum> getTimeSeriesByName(Dataset dataset, int name) {
		throw new RuntimeException("Dont call this!");
	}
	
	public SimpleDatum getRuntimeValueByName(int name, Dataset ds) {
		throw new RuntimeException("Dont call this!");
	}
	
	public Map<String,VizAttr> getVizAttributes(Dataset d){
		throw new RuntimeException("Dont call this!");
	}

	public Map<String, String> getAttributes() {
		throw new RuntimeException("Dont call this!");
	}

	public Collection<ModelNodeVariable> getAttributeValues() {
		throw new RuntimeException("Dont call this!");
	}

	public Collection<jprime.variable.ModelNodeVariable> getAllAttributeValues() {
		throw new RuntimeException("Dont call this!");
	}
	
	public ModelNodeVariable getAttributeByName(String name) {
		throw new RuntimeException("Dont call this!");
	}

	public ModelNodeVariable getAttributeByName(int name) {
		throw new RuntimeException("Dont call this!");
	}

	public List<ModelNode> getAllChildren() {
		throw new RuntimeException("Dont call this!");
	}
	
	public PersistentChildList getPersistentChildList() {
		throw new RuntimeException("Dont call this!");
	}

	public Metadata getMetadata() {
		throw new RuntimeException("Dont call this!");
	}

	public Set<Integer> getAlignments(Partitioning p) {
		throw new RuntimeException("Dont call this!");
	}

	public IModelNode getParent() {
		throw new RuntimeException("Dont call this!");
	}

	public IModelNode getParent_nofetch() {
		throw new RuntimeException("Dont call this!");
	}
	
	public UniqueName getUniqueName() {
		throw new RuntimeException("Dont call this!");
	}

	public int getNumberOfChildren() {
		throw new RuntimeException("Dont call this!");
	}

	public IModelNode getChildByName(String name) {
		throw new RuntimeException("Dont call this!");
	}

	public int countChildrenWithName(String name) {
		throw new RuntimeException("Dont call this!");
	}

	public boolean isReplica() {
		throw new RuntimeException("Dont call this!");
	}

	public boolean isAlias() {
		throw new RuntimeException("Dont call this!");
	}

	public void enforceMinimumChildConstraints() {
		throw new RuntimeException("Dont call this!");
	}

	public IModelNode getChildByRank(long rank, IModelNode anchor) {
		throw new RuntimeException("Dont call this!");
	}

	public void evaluateResourceID(ResourceID resourceid, int list_idx,
			int term_idx, EvalutedResourceID result) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(IGenericVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(IPAddressAssignment visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(UIDAssignmentVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(TLVVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(IRouteVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(VerifyVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(RouteCalculationVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(PartitioningVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void accept(XMLVisitor visitor) {
		throw new RuntimeException("Dont call this!");
	}

	public void addAttr(ModelNodeVariable attr) {
		throw new RuntimeException("Dont call this!");
	}

	public Class<?> getNodeType() {
		throw new RuntimeException("Dont call this!");
	}

	public int getRow() {
		throw new RuntimeException("Dont call this!");
	}

	public void setRow(int row) {
		throw new RuntimeException("Dont call this!");
	}

	public boolean getHub() {
		throw new RuntimeException("Dont call this!");
	}
	
	public PrefuseLocation updateLocation(PrefuseLocation loc) {
		throw new RuntimeException("Dont call this!");
	}
	
	public String getAttributeValueByName(int name) {
		throw new RuntimeException("Dont call this!");
	}

	public void setHub(boolean h) {
		throw new RuntimeException("Dont call this!");
	}
	public IModelNode getChildByQuery(String query) {
		throw new RuntimeException("Dont call this!");
	}

	public IModelNode get(String query) {
		throw new RuntimeException("Dont call this!");
	}
	
	public PersistableState getPersistableState() {
		throw new RuntimeException("Dont call this!");
	}
	
	public IModelNode findNodeByUID(long uid) {
		throw new RuntimeException("Dont call this!");
	}
	
	public List<String> help() {
		return new ArrayList<String>();
	}

	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent) {
		throw new RuntimeException("Dont call this!");
	}
		
	private static boolean compatibleTypes(Class<?> t1,Class<?> t2) {
		return t1.isAssignableFrom(t2)?true:t2.isAssignableFrom(t1);
	}

	private static IModelNode predecessor_down(Class<?> type, int idx, List<ModelNode> kids) {
		if(idx>=0 && idx<kids.size()) {
			IModelNode prev = null;
			//first search left and down
			for(;idx>=0;idx--) {
				prev=kids.get(idx);
				if(compatibleTypes(type,prev.getClass())) {
					return prev;
				}
				else {
					//we need to start at the deep part of this net....
					final List<ModelNode> net_kids = prev.getAllChildren();
					prev= predecessor_down(type,net_kids.size()-1,net_kids);
					if(prev != null) return prev;
				}
			}
		}
		//the predecessor is not in this sub-tree!
		return null;
	}
	private static IModelNode predecessor(Class<?> type, int idx, List<ModelNode> siblings, IModelNode anchor) {
		IModelNode prev = predecessor_down(type,idx, siblings);
		if(prev == null) {
			final IModelNode p = siblings.get(0).getParent();
			//we can't recurse up pass the anchor!
			if(p != null && p.getUID() != anchor.getUID()) {
				final IModelNode gp = p.getParent();
				if(gp != null) {
					final List<ModelNode> kids = gp.getAllChildren();
					final RankSearchNode rsn = new RankSearchNode(p.getMaxRank(anchor), anchor);
					final int pidx = Collections.binarySearch(kids, rsn,rsn);
					prev = predecessor(type,pidx-1,kids,anchor);
				}
			}
		}
		return prev;
	}
	
	private static IModelNode successor_down(Class<?> type, int idx, List<ModelNode> kids) {
		if(idx>=0 && idx<kids.size()) {
			IModelNode next = null;
			//first search left and down
			for(;idx<kids.size();idx++) {
				next=kids.get(idx);
				if(compatibleTypes(type,next.getClass())) {
					return next;
				}
				else {
					//we need to start at the deep part of this net....
					final List<ModelNode> net_kids = next.getAllChildren();
					next= successor_down(type,0,net_kids);
					if(next != null) return next;
				}
			}
		}
		//the successor is not in this sub-tree!
		return null;
	}
	private static IModelNode successor(Class<?> type, int idx, List<ModelNode> siblings, IModelNode anchor) {
		IModelNode next = successor_down(type,idx, siblings);
		if(next == null) {
			final IModelNode p = siblings.get(0).getParent();
			//we can't recurse up pass the anchor!
			if(p != null && p.getUID() != anchor.getUID()) {
				final IModelNode gp = p.getParent();
				if(gp != null) {
					final List<ModelNode> kids = gp.getAllChildren();
					final RankSearchNode rsn = new RankSearchNode(p.getMaxRank(anchor), anchor);
					final int pidx = Collections.binarySearch(kids, rsn,rsn);
					next = successor(type,pidx+1,kids,anchor);
				}
			}
		}
		return next;
	}
	
	public static NodeIdxPair findNode(long rank, Class<?> target_type, IModelNode node, IModelNode anchor) {
		final List<ModelNode> kids = node.getAllChildren();
		final RankSearchNode rsn = new RankSearchNode(rank, anchor);
		int idx = Collections.binarySearch(kids, rsn,rsn);
		if(idx < 0 ) {
			throw new  RuntimeException("Did not find the node with rank "+rank);
		}
		IModelNode match = kids.get(idx);
		if(match.getMaxRank(anchor)==rank) {
			return new NodeIdxPair(idx,match);
		}
		else if(target_type != null  && target_type.isAssignableFrom(match.getClass())) {
			//this assumes the target type is not a net!
			return new NodeIdxPair(idx,match);
		}
		return findNode(rank,target_type,match,anchor);
	}
	public static NodeTripple findTripple(long rank, Class<?> target_type, IModelNode anchor) {
		final NodeIdxPair match = findNode(rank,target_type,anchor,anchor);
		if(match == null)
			throw new  RuntimeException("Did not find the node with rank "+rank);
		IModelNode prev = predecessor(match.node.getClass(), match.idx-1, match.node.getParent().getAllChildren(),anchor);
		IModelNode next = successor(match.node.getClass(), match.idx+1, match.node.getParent().getAllChildren(),anchor);
		return new NodeTripple(prev, match.node,next);
	}
}
