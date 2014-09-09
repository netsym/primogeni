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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.Net.INet;
import jprime.ResourceIdentifier.EvalutedResourceID;
import jprime.ResourceIdentifier.ResourceID;
import jprime.database.PKey;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.util.PersistentChildList;
import jprime.util.UniqueName;
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
 *
 */
public abstract class StaticRoutingProtocol extends PersistableObject implements IModelNode {
	//transient
	private SoftReference<INet> __parent=null;
	private Metadata meta;
	
	//persisted
	protected long dbid;
	protected int type;
	protected long parent_id;
	protected long metadata_id;

	/** 
	 * Used when loading form DB
	 * @param dbid
	 * @param type
	 * @param parent_id
	 */
	protected StaticRoutingProtocol(Metadata meta, long dbid, int type, long parent_id) {
		super();
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.dbid = dbid;
		this.type = type;
		this.parent_id = parent_id;
		this.__parent=null;
		this.persistable_state=PersistableState.UNMODIFIED;
		this.mods=Modified.NOTHING.id;
		meta.loaded(this);
	}

	protected StaticRoutingProtocol(INet parent, int type) {
		super();
		this.meta=parent.getMetadata();
		this.metadata_id=meta.getDBID();
		this.dbid=meta.getNextModelNodeDBID();
		this.type=type;
		this.persistable_state=PersistableState.NEW;
		this.mods=Modified.ALL.id;
		this.__parent=new SoftReference<INet>(parent);
		meta.loaded(this);
	}
	
	@Override
	protected void finalize() throws Throwable {
		meta.collect(this);
		super.finalize();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StaticRoutingProtocol) {
			return this.getMetadataId() == ((StaticRoutingProtocol)obj).getMetadataId() && getDBID() ==  ((StaticRoutingProtocol)obj).getDBID();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#save()
	 */
	public synchronized void save() {
		meta.save(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IModelNode o) {
		throw new RuntimeException("Dont call this!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.PeristableObject#getPKey()
	 */
	public PKey getPKey() {
		return new PKey(metadata_id, dbid);
	}
	
	/**
	 * @return
	 */
	public abstract boolean isASLevelProtocol();

	/**
	 * @return
	 */
	public abstract boolean isAlgorithmicProtocol();

	public abstract IModelNode __copy(String name, IModelNode toCopy, IModelNode parent);

	/**
	 * @param used
	 *            by replicas to do a deep copy of the node.
	 */
	abstract public StaticRoutingProtocol deepCopy(INet parent);

	/**
	 * @param net
	 * @return
	 */
	public abstract IRouteVisitor getRouteVisitor(INet net);

	/**
	 * @return
	 */
	public INet getParent() {
		if(__parent==null || __parent.get()==null) {
			__parent=new SoftReference<INet>((INet)meta.loadModelNode(parent_id));
		}
		return __parent.get();
	}
	
	public IModelNode getParent_nofetch() {
		if(__parent==null ) {
			return null;
		}
		return __parent.get();
	}
	
	public long getDBID() {
		return dbid;
	}
	
	public int getType() {
		return type;
	}

	public long getParentId() {
		return parent_id;
	}

	public long getMetadataId() {
		return metadata_id;
	}

	public Metadata getMetadata() {
		return meta;
	}
	
	public long getUID() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void setUID(long id) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public long getRank(IModelNode anchor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public long getMinRank(IModelNode anchor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public long getMinUID() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public long getMaxRank(IModelNode anchor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public boolean containsRank(IModelNode anchor, long rank) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public boolean containsUID(long uid) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public String getName() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}

	public IModelNode copy(String name, IModelNode parent) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}

	public long getSize() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void setSize(long s) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public long getOffset() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}
	
	public void setOffset(long o) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public int getOrder() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}

	public void delete() {
		throw new RuntimeException("Dont call this!");
	}

	abstract public int getTypeId();


	public IModelNode deference() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}
	
	public boolean hasBeenReplicated() {
		return false;
	}
	
	public Vector<SimpleDatum> getTimeSeriesByName(Dataset dataset, String name) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}

	public Vector<SimpleDatum> getTimeSeriesByName(Dataset dataset, int name) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}
	
	public SimpleDatum getRuntimeValueByName(int name, Dataset ds) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}

	public Collection<ModelNodeVariable> getAttributeValues() {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}

	public Collection<jprime.variable.ModelNodeVariable> getAllAttributeValues() {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}
	
	public Map<String, String> getAttributes(boolean includeDefaults) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}
	
	public Map<String,VizAttr> getVizAttributes(Dataset d) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}
	
	public Map<String, String> getAttributes() {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}

	public ModelNodeVariable getAttributeByName(String name) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public ModelNodeVariable getAttributeByName(int name) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}

	public String getAttributeValueByName(int name) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}

	public PersistentChildList getPersistentChildList() {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}
	
	public List<ModelNode> getAllChildren() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public Set<Integer> getAlignments(Partitioning p) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public UniqueName getUniqueName() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public int getNumberOfChildren() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public IModelNode getChildByName(String name) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}
	
	public IModelNode getChildByQuery(String query){
		throw new RuntimeException("Do not call this on StaticRouting Protocols!");
	}
	
	public IModelNode get(String query){
		throw new RuntimeException("Do not call this on StaticRouting Protocols!");
	}


	public int countChildrenWithName(String name) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public boolean isReplica() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public boolean isAlias() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void enforceMinimumChildConstraints() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public IModelNode getChildByRank(long rank, IModelNode anchor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void accept(IGenericVisitor visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void accept(IPAddressAssignment visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void accept(UIDAssignmentVisitor visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void accept(TLVVisitor visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void accept(IRouteVisitor visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void accept(VerifyVisitor visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void accept(RouteCalculationVisitor visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}

	public ModelNodeVariable setAttribute(int varId, String value) throws InstantiationException, IllegalAccessException {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}

	public void accept(PartitioningVisitor visitor) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void addAttr(ModelNodeVariable attr) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public int getRow() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public void setRow(int row) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}


	public boolean getHub() {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}

	public PrefuseLocation updateLocation(PrefuseLocation loc) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}


	public void setHub(boolean h) {
		throw new RuntimeException(
				"Do not call this on StaticRouting Protocols!");
	}
		
	public void accept(XMLVisitor visitor) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}
	
	public void evaluateResourceID(ResourceID rid, int list_idx, int term_idx,  EvalutedResourceID result) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");
	}
	
	public Class<?> getNodeType() {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");		
	}
	
	public IModelNode findNodeByUID(long uid) {
		throw new RuntimeException(
		"Do not call this on StaticRouting Protocols!");		
	}

	
	public List<String> help() {
		return new ArrayList<String>();
	}
}
