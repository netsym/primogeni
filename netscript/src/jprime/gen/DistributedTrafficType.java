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
public abstract class DistributedTrafficType extends jprime.DynamicTrafficType.DynamicTrafficType implements jprime.gen.IDistributedTrafficType {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public DistributedTrafficType(PyObject[] v, String[] s){super(v,s);}
	public DistributedTrafficType(ModelNodeRecord rec){ super(rec); }
	public DistributedTrafficType(IModelNode parent){ super(parent); }
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.DistributedTrafficType.IDistributedTrafficType.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.DistributedTrafficType.DistributedTrafficTypeReplica c = new jprime.DistributedTrafficType.DistributedTrafficTypeReplica(this.getName(),(IModelNode)parent,(jprime.DistributedTrafficType.IDistributedTrafficType)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1021: //DistributedTrafficType
			case 1077: //DistributedTrafficTypeAlias
			case 1133: //DistributedTrafficTypeReplica
			case 1189: //DistributedTrafficTypeAliasReplica
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
