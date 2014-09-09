/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */


package jprime.gen;

import jprime.*;
import jprime.ModelNodeRecord;
import jprime.variable.*;
import org.python.core.PyObject;
import org.python.core.Py;
public abstract class DropTailQueueAlias extends jprime.NicQueue.NicQueueAlias implements jprime.gen.IDropTailQueueAlias {
	public DropTailQueueAlias(IModelNode parent, jprime.DropTailQueue.IDropTailQueue referencedNode) {
		super(parent,(jprime.DropTailQueue.IDropTailQueue)referencedNode);
	}
	public DropTailQueueAlias(ModelNodeRecord rec){ super(rec); }
	public DropTailQueueAlias(PyObject[] v, String[] s){super(v,s);}
	public DropTailQueueAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.DropTailQueue.IDropTailQueue.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.DropTailQueue.DropTailQueueAliasReplica c = new jprime.DropTailQueue.DropTailQueueAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1068: //DropTailQueueAlias
			case 1180: //DropTailQueueAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.DropTailQueue.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
