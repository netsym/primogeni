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
public abstract class NicQueueReplica extends jprime.ModelNodeReplica implements jprime.gen.INicQueue {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public NicQueueReplica(String name, IModelNode parent, jprime.NicQueue.INicQueue referencedNode) {
		super(name,parent,referencedNode);
	}
	public NicQueueReplica(ModelNodeRecord rec){ super(rec); }
	public NicQueueReplica(PyObject[] v, String[] s){super(v,s);}
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
		doing_deep_copy=true;
		jprime.NicQueue.NicQueueReplica c = new jprime.NicQueue.NicQueueReplica(this.getName(), (IModelNode)parent,(jprime.NicQueue.INicQueue)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
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

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.NicQueue.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
