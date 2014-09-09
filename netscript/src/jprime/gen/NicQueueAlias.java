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
public abstract class NicQueueAlias extends jprime.ModelNodeAlias implements jprime.gen.INicQueueAlias {
	public NicQueueAlias(IModelNode parent, jprime.NicQueue.INicQueue referencedNode) {
		super(parent,(IModelNode)referencedNode);
	}
	public NicQueueAlias(ModelNodeRecord rec){ super(rec); }
	public NicQueueAlias(PyObject[] v, String[] s){super(v,s);}
	public NicQueueAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.NicQueue.INicQueue.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.NicQueue.NicQueueAliasReplica c = new jprime.NicQueue.NicQueueAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1066: //NicQueueAlias
			case 1067: //FluidQueueAlias
			case 1068: //DropTailQueueAlias
			case 1069: //RedQueueAlias
			case 1178: //NicQueueAliasReplica
			case 1179: //FluidQueueAliasReplica
			case 1180: //DropTailQueueAliasReplica
			case 1181: //RedQueueAliasReplica
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
		return jprime.gen.NicQueue.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
