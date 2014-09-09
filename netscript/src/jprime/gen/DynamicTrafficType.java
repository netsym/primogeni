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
public abstract class DynamicTrafficType extends jprime.TrafficType.TrafficType implements jprime.gen.IDynamicTrafficType {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public DynamicTrafficType(PyObject[] v, String[] s){super(v,s);}
	public DynamicTrafficType(ModelNodeRecord rec){ super(rec); }
	public DynamicTrafficType(IModelNode parent){ super(parent); }
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
		jprime.DynamicTrafficType.DynamicTrafficTypeReplica c = new jprime.DynamicTrafficType.DynamicTrafficTypeReplica(this.getName(),(IModelNode)parent,(jprime.DynamicTrafficType.IDynamicTrafficType)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1020: //DynamicTrafficType
			case 1021: //DistributedTrafficType
			case 1022: //CentralizedTrafficType
			case 1023: //SwingTCPTraffic
			case 1076: //DynamicTrafficTypeAlias
			case 1077: //DistributedTrafficTypeAlias
			case 1078: //CentralizedTrafficTypeAlias
			case 1079: //SwingTCPTrafficAlias
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
