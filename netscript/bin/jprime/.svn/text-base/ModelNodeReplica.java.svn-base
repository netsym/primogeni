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
import java.util.ArrayList;
import java.util.List;

import jprime.variable.ModelNodeVariable;
import jprime.variable.SymbolVariable;

import org.python.core.PyObject;


/**
 * 
 * @author Nathanael Van Vorst
 */
public abstract class ModelNodeReplica extends ModelNode  implements IModelNode{
	protected final static List<Integer> non_replicated_attrs=new ArrayList<Integer>();
	static {
		non_replicated_attrs.add(ModelNodeVariable.name());
		non_replicated_attrs.add(ModelNodeVariable.uid());
		non_replicated_attrs.add(ModelNodeVariable.offset());
	}
	
	private SoftReference<ModelNode> __replica=null;
	protected boolean doing_deep_copy;

	/** constructor for jython 
	 * @param values
	 * @param names
	 */
	protected ModelNodeReplica(PyObject[] values, String[] names) {
		super(removeReferencedNode(values),names);
		ModelNode referencedNode = (ModelNode)values[1].__tojava__(ModelNode.class);
		if(referencedNode == null)
			throw new RuntimeException("referencedNode was null!");
		this.setReplicaMetaId(referencedNode.getMetadata().getDBID());
		this.setReplicaId(referencedNode.getDBID());
		this.__replica=new SoftReference<ModelNode>(referencedNode);
		//do a deep copy
		doing_deep_copy=true;
		for(ModelNode tocopy : referencedNode.getAllChildren()) {
			ModelNode copied = tocopy.deepCopy(this);
			super.addChild(copied);
		}
		doing_deep_copy=false;
		referencedNode.setHas_been_replicated(true);
	}
	
	private static PyObject[] removeReferencedNode(PyObject[] values){
		PyObject[] n = new PyObject[values.length-1];
		for(int i=0, j=0 ; i<values.length ; i++)
			if(i!=1) {
				n[j] = values[i];
				//jprime.Console.out.println("["+j+"]="+values[i]+"["+i+"]");
				j++;
			}
		return n;
	}
	
	/**
	 * 
	 * @param parent
	 * @param referencedNode
	 */
	protected ModelNodeReplica(String name, IModelNode parent, IModelNode referencedNode) {
		super(parent);
		this.setName(name);
		if(referencedNode == null)
			throw new RuntimeException("referencedNode was null!");
		this.setReplicaMetaId(referencedNode.getMetadata().getDBID());
		this.setReplicaId(((ModelNode)referencedNode).getDBID());
		this.__replica=new SoftReference<ModelNode>((ModelNode)referencedNode);
		//do a deep copy
		doing_deep_copy=true;
		for(ModelNode tocopy : referencedNode.getAllChildren()) {
			ModelNode copied = tocopy.deepCopy(this);
			super.addChild(copied);
		}
		doing_deep_copy=false;
		((ModelNode)referencedNode).setHas_been_replicated(true);
	}
	
	/**
	 * 
	 * 
	 */
	public ModelNodeReplica(ModelNodeRecord rec) {
		super(rec);
		doing_deep_copy=false;
	}
	
	
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isAlias()
	 */
	public boolean isAlias(){
		return false;
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
	public void addChild(ModelNode child){
		if(!doing_deep_copy) {
			//normal mode
			//since they added a child, we need to convert all parent nodes from replicas to real nodes then our self.
			convertToReal();
		}
		//else we need to just add the node...
		super.addChild(child);
	}
	
	/**
	 * @return
	 */
	public ModelNode getReplicatedNode() {
		ModelNode rv = (__replica == null)?null: __replica.get();
		if(rv==null) {
			if(getReplicaId() == getDBID() && getReplicaMetaId() == this.getDBMetaID()) {
				return this;
			}
			else {
				rv = getMetadata().loadModelNode(getReplicaMetaId(),getReplicaId());
				__replica = new SoftReference<ModelNode>(rv);
			}
		}
		if(rv == null) {
			throw new RuntimeException("The replicated node was null!");
		}
    	/* $if DEBUG $
		getMetadata().logAccess(rv);
		$endif$ */
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#deference()
	 */
	public IModelNode deference() {
		return this;
	}
	
	/**
	 * 
	 */
	protected void convertToReal() {
		if(!isReplica())
			return;
		ModelNode replica = getReplicatedNode();
		for(ModelNodeVariable v : replica.getAllAttributeValues()) {
			if(null == getAttributeByName(v.getDBName())) {
				if(!non_replicated_attrs.contains(v.getDBName())) {
					if(v instanceof SymbolVariable) {
						SymbolVariable vv = new SymbolVariable(((SymbolVariable)v).getName());
						vv.attachToNode(this, v.getDBName());
						vv.setOwner(this);
						this.attrs.put(v.getDBName(),vv);
					}
					else {
						try {
							Class<? extends jprime.gen.ModelNodeVariable > cls = v.getClass();
							ModelNodeVariable vv=(jprime.variable.ModelNodeVariable)cls.newInstance();
							vv.setDBName(v.getDBName());
							vv.setOwner(this);
							this.attrs.put(v.getDBName(),vv);
							vv.setValueAsString(v.toString());
						} catch (InstantiationException e) {
							throw new RuntimeException(e);
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
		this.__replica=null;
		this.setReplicaMetaId(getDBMetaID());
		this.setReplicaId(getDBID());
		if(this.getParent() instanceof ModelNodeReplica) {
			((ModelNodeReplica)getParent()).convertToReal();
		}
	}	
}
