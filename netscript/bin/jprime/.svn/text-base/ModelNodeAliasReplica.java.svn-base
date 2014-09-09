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
 * XXX
 * 
 * @author Nathanael Van Vorst
 */
abstract public class ModelNodeAliasReplica extends ModelNodeReplica implements IModelNode{
	protected SoftReference<ModelNode> __referencedNode=null;
	
	
	/** constructor for jython 
	 * @param values
	 * @param names
	 */
	protected ModelNodeAliasReplica(PyObject[] values, String[] names) {
		super(values,names);
	}
	
	/**
	 * XXX
	 * @param parent
	 * @param referencedNode
	 */
	protected ModelNodeAliasReplica(String name, IModelNode parent, ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/**
	 * XXX
	 */
	protected ModelNodeAliasReplica(ModelNodeRecord rec) {
		super(rec);
	}
	
	/**
	 * @return
	 */
	public String getAliasPath() {
		StringVariable n = (StringVariable)getAttributeByName(ModelNodeVariable.alias_path());
		if(null!=n)
			return n.getValue();
		return ((ModelNodeAlias)getReplicatedNode()).getAliasPath();
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isReplica()
	 */
	public boolean isReplica() {
		return true;
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
	 * XXX
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
		IModelNode rv = null;
		if(null==__referencedNode) {
			rv=ModelNode.evaluatePath(this, getAliasPath());
			__referencedNode=new SoftReference<ModelNode>((ModelNode)rv);
		}
		else {
			rv = __referencedNode.get();
		}
		if(rv == null) {
			rv=ModelNode.evaluatePath(this, getAliasPath());
			__referencedNode=new SoftReference<ModelNode>((ModelNode)rv);
		}
		if(rv==null) {
			throw new RuntimeException("The alias path is in valid!"+getAliasPath()+" node="+getUniqueName());
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		return deference().getAlignments(p);
	}
}
