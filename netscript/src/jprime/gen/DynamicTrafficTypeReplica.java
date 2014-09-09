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
public abstract class DynamicTrafficTypeReplica extends jprime.TrafficType.TrafficTypeReplica implements jprime.gen.IDynamicTrafficType {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public DynamicTrafficTypeReplica(String name, IModelNode parent, jprime.DynamicTrafficType.IDynamicTrafficType referencedNode) {
		super(name,parent,referencedNode);
	}
	public DynamicTrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public DynamicTrafficTypeReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.DynamicTrafficType.IDynamicTrafficType.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.DynamicTrafficType.DynamicTrafficTypeReplica c = new jprime.DynamicTrafficType.DynamicTrafficTypeReplica(this.getName(), (IModelNode)parent,(jprime.DynamicTrafficType.IDynamicTrafficType)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1132: //DynamicTrafficTypeReplica
			case 1133: //DistributedTrafficTypeReplica
			case 1134: //CentralizedTrafficTypeReplica
			case 1135: //SwingTCPTrafficReplica
			case 1188: //DynamicTrafficTypeAliasReplica
			case 1189: //DistributedTrafficTypeAliasReplica
			case 1190: //CentralizedTrafficTypeAliasReplica
			case 1191: //SwingTCPTrafficAliasReplica
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
		return jprime.gen.DynamicTrafficType.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
