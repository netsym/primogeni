/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */


package jprime.gen;

import jprime.*;
import jprime.variable.*;
import jprime.ModelNodeRecord;
import org.python.core.PyObject;
import org.python.core.Py;
public abstract class NicQueue extends jprime.ModelNode implements jprime.gen.INicQueue {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public NicQueue(PyObject[] v, String[] s){super(v,s);}
	public NicQueue(ModelNodeRecord rec){ super(rec); }
	public NicQueue(IModelNode parent){ super(parent); }
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
		jprime.NicQueue.NicQueueReplica c = new jprime.NicQueue.NicQueueReplica(this.getName(),(IModelNode)parent,(jprime.NicQueue.INicQueue)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1010: //NicQueue
			case 1011: //FluidQueue
			case 1012: //DropTailQueue
			case 1013: //RedQueue
			case 1066: //NicQueueAlias
			case 1067: //FluidQueueAlias
			case 1068: //DropTailQueueAlias
			case 1069: //RedQueueAlias
			case 1122: //NicQueueReplica
			case 1123: //FluidQueueReplica
			case 1124: //DropTailQueueReplica
			case 1125: //RedQueueReplica
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
	public final static java.util.ArrayList<Integer> attrIds=new java.util.ArrayList<Integer>();
	static {
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
