package jprime;

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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jprime.PersistableObject.PersistableState;
import jprime.ResourceIdentifier.EvalutedResourceID;
import jprime.ResourceIdentifier.ResourceID;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.routing.IRouteVisitor;
import jprime.routing.RouteCalculationVisitor;
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
 */
public interface IModelNode extends Comparable<IModelNode>, Cloneable {
	/**
	 * @return
	 */
	public long getDBID();
	
	/**
	 * @return
	 */
	public long getParentId();
	
	/**
	 * @return XXX
	 */
	public long getUID();

	/**
	 * @param id the new uid
	 */
	public void setUID(long id);
	
	/**
	 * @param anchor
	 * @return
	 */
	public long getRank(IModelNode anchor);

	/**
	 * @param anchor
	 * @return
	 */
	public long getMinRank(IModelNode anchor);
	
	/**
	 * @return
	 */
	public long getMinUID();

	/**
	 * @param anchor
	 * @return
	 */
	public long getMaxRank(IModelNode anchor);
	
	/**
	 * @return
	 */
	public boolean hasBeenReplicated();

	/**
	 * @param anchor
	 * @param rank
	 * @return
	 */
	public boolean containsRank(IModelNode anchor, long rank);
	
	
	/**
	 * @param name
	 * @param parent
	 * @return
	 */
	public IModelNode copy(String name, IModelNode parent);

	/**
	 * @param uid
	 * @return
	 */
	public boolean containsUID(long uid);
	
	/**
	 * @return XXX
	 */ 
	public String getName();
	
	/**
	 * @return
	 */
	public long getSize();

	/**
	 * @param s
	 */
	public void setSize(long s);

	/**
	 * @return
	 */
	public long getOffset();
	
	/**
	 * @param o
	 */
	public void setOffset(long o);
		
	/**
	 * @return XXX
	 */ 
	public int getOrder();
		
	/**
	 * @return XXX
	 */
	public int getTypeId();

	/**
	 * @return XXX
	 */
	public IModelNode deference();
	
	/**
	 * Get all attributes for a model node... including the default value
	 * @return
	 */
	public Map<String,String> getAttributes(boolean includeDefaultValues);
	
	/**
	 * @param d
	 * @return
	 */
	public Map<String,VizAttr> getVizAttributes(Dataset d);
	
	/**
	 * Get all attributes for a model node... including the default value
	 * @return
	 */
	public Map<String,String> getAttributes();

	/**
	 * XXX
	 * @return
	 */
	public Collection<jprime.variable.ModelNodeVariable> getAttributeValues();
	
	/**
	 * XXX
	 * @return
	 */
	public Collection<jprime.variable.ModelNodeVariable> getAllAttributeValues();
	
	/**
	 * @param name
	 * @return
	 */
	public ModelNodeVariable getAttributeByName(String name);	
			
	/**
	 * @param name
	 * @return
	 */
	public ModelNodeVariable getAttributeByName(int name);	
	
	/**
	 * @param name
	 * @return
	 */
	public String getAttributeValueByName(int name);

	/**
	 * @param varId
	 * @param value
	 */
	public ModelNodeVariable setAttribute(int varId, String value) throws InstantiationException, IllegalAccessException;
	
	/**
	 * @param name
	 * @param ds
	 * @return
	 */
	public SimpleDatum getRuntimeValueByName(int name, Dataset ds);

	/**
	 * XXX
	 * @return
	 */
	public List<ModelNode> getAllChildren();
	
	/**
	 * XXX
	 * @return
	 */
	public PersistentChildList getPersistentChildList();
	
	/**
	 * XXX 
	 * @return
	 */
	public Metadata getMetadata();
	
	/**
	 * @return a list of id of alignments to which this node belongs
	 */
	public Set<Integer> getAlignments(Partitioning p);
	
	/**
	 * @return the parent of this node
	 */
	public IModelNode getParent_nofetch();
	
	/**
	 * @return the parent of this node
	 */
	public IModelNode getParent();
	
	/**
	 * @return the fully qualified name of this node
	 */
	public UniqueName getUniqueName();
	
	/**
	 * @return the number of *direct* children this node currently has
	 */
	public int getNumberOfChildren();

	/**
	 * @param name
	 * @return
	 */
	public IModelNode getChildByName(String name);
	
	/**
	 * @return
	 */
	public boolean isReplica();
	
	/**
	 * @return
	 */
	public boolean isAlias();
	
	/**
	 * 
	 */
	public void enforceMinimumChildConstraints();
	
	/**
	 * @param rank
	 * @param anchor
	 * @return
	 */
	public IModelNode getChildByRank(long rank, IModelNode anchor);
	
	/**
	 * @param rid
	 * @param term_idx
	 * @param results
	 */
	public void evaluateResourceID(ResourceID resourceid, int list_idx, int term_idx, EvalutedResourceID result);
	
	/**
	 * Accept a generic visitor
	 * @param visitor
	 */
	public void accept(IGenericVisitor visitor);

	/**
	 * @param visitor
	 */
	public void accept(IPAddressAssignment visitor);
	
	/**
	 * @param visitor
	 */
	public void accept(UIDAssignmentVisitor visitor);
	
	/**
	 * @param visitor
	 */
	public void accept(TLVVisitor visitor);
	
	/**
	 * @param visitor
	 */
	public void accept(IRouteVisitor visitor);
	
	
	/**
	 * @param visitor
	 */
	public void accept(VerifyVisitor visitor);

	
	/**
	 * @param visitor
	 */
	public void accept(RouteCalculationVisitor visitor);
	
	/**
	 * @param visitor
	 */
	public void accept(PartitioningVisitor visitor);
		
	/**
	 * @param visitor
	 */
	public void accept(XMLVisitor visitor);
	
	/**
	 * @param attr
	 */
	public void addAttr(ModelNodeVariable attr);
	
	/**
	 * @return
	 */
	public PersistableState getPersistableState();
	
	/**
	 * @param uid
	 * @return
	 */
	public IModelNode findNodeByUID(long uid);
	
	/**
	 * delete this node including all its children
	 */
	public void delete();	
	
	/**
	 * @param name
	 * @param toCopy
	 * @param parent
	 * @return
	 */
	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent);

	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType();
	
	public IModelNode getChildByQuery(String query);
	public IModelNode get(String query);
	
	public List<String> help();
	
	//-------------- 
	// for prefuse
	//-------------- 
	public int getRow();
	public void setRow(int row);
	public boolean getHub();	
	public void setHub(boolean h);
	public static class PrefuseLocation {
		public final double x, y;
		public PrefuseLocation(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	public PrefuseLocation updateLocation(PrefuseLocation loc);


}
