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

import java.lang.ref.SoftReference;
import java.util.Set;

import jprime.gen.ModelNodeVariable;
import jprime.partitioning.Partitioning;
import jprime.variable.StringVariable;

import org.python.core.PyObject;


/**
 * 
 * 
 * @author Nathanael Van Vorst
 */
abstract public class ModelNodeAlias extends ModelNode implements IModelNode {
	private SoftReference<ModelNode> __referenced_node=null;
	private long ref_dbid=0;
	/**
	 * 
	 */
	protected ModelNodeAlias(ModelNodeRecord rec) {
		super(rec);
	}
	
	/**
	 * 
	 * @param parent
	 * @param referencedNode
	 */
	protected ModelNodeAlias(IModelNode parent, IModelNode referencedNode) {
		super(parent);
		this.__referenced_node=new SoftReference<ModelNode>((ModelNode)referencedNode);
		if(referencedNode == null)
			throw new NullPointerException("referencedNode was null!");
		ref_dbid=referencedNode.getDBID();
		StringVariable p = new StringVariable(ModelNodeVariable.alias_path(),ModelNode.relativePath(this,referencedNode));
		p.setOwner(this);
		this.attrs.put(ModelNodeVariable.alias_path(), p);
	}
	
	protected ModelNodeAlias(IModelNode parent) {
		super(parent);
	}

	
	/** constructor for jython 
	 * @param values
	 * @param names
	 */
	protected ModelNodeAlias(PyObject[] values, String[] names) {
		super(removeReferencedNode(values),names);
		IModelNode referencedNode=(ModelNode)values[1].__tojava__(ModelNode.class);
		this.__referenced_node=new SoftReference<ModelNode>((ModelNode)referencedNode);
		if(referencedNode == null)
			throw new NullPointerException("referencedNode was null!");
		ref_dbid=referencedNode.getDBID();
		StringVariable p = new StringVariable(ModelNodeVariable.alias_path(),ModelNode.relativePath(this,deference()));
		p.setOwner(this);
		this.attrs.put(ModelNodeVariable.alias_path(), p);
	}
	private static PyObject[] removeReferencedNode(PyObject[] values){
		PyObject[] n = new PyObject[values.length-1];
		for(int i=0, j=0 ; i<values.length-1 ; i++)
			if(i!=1)
				n[j++] = values[i];
		return n;
	}
		
	/**
	 * @return
	 */
	public String getAliasPath() {
		StringVariable n = (StringVariable)getAttributeByName(ModelNodeVariable.alias_path());
		if(null==n){
			return null;
		}
		return n.getValue();
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isReplica()
	 */
	public boolean isReplica() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isAlias()
	 */
	public boolean isAlias(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getTypeId()
	 */
	abstract public int getTypeId();

	/**
	 * 
	 * @param child
	 * @return
	 */
	final public void addChild(ModelNode child){
		throw new RuntimeException("Aliases can not have children!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#deference()
	 */
	public IModelNode deference() {
		ModelNode rv = (null==__referenced_node)?null:__referenced_node.get();
		if(rv == null) {
			if(ref_dbid==0) {
				rv=(ModelNode)ModelNode.evaluatePath(this, getAliasPath());
			}
			else {
				rv=getMetadata().loadModelNode(ref_dbid);
				if(rv != null) {
					ref_dbid=rv.getDBID();
				}
			}
			__referenced_node=new SoftReference<ModelNode>((ModelNode)rv);
			if(rv==null) {
				throw new RuntimeException("The alias path is in valid!"+getAliasPath()+" node="+getUniqueName());
			}
		}
    	/* $if DEBUG $
		if(rv!=null) {
			getMetadata().logAccess(rv);
		}
		$endif$ */

		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		return deference().getAlignments(p);
	}
	
}
