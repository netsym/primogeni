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
public abstract class DropTailQueueReplica extends jprime.NicQueue.NicQueueReplica implements jprime.gen.IDropTailQueue {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public DropTailQueueReplica(String name, IModelNode parent, jprime.DropTailQueue.IDropTailQueue referencedNode) {
		super(name,parent,referencedNode);
	}
	public DropTailQueueReplica(ModelNodeRecord rec){ super(rec); }
	public DropTailQueueReplica(PyObject[] v, String[] s){super(v,s);}
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
		doing_deep_copy=true;
		jprime.DropTailQueue.DropTailQueueReplica c = new jprime.DropTailQueue.DropTailQueueReplica(this.getName(), (IModelNode)parent,(jprime.DropTailQueue.IDropTailQueue)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1124: //DropTailQueueReplica
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
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
