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
public abstract class DynamicTrafficTypeAlias extends jprime.TrafficType.TrafficTypeAlias implements jprime.gen.IDynamicTrafficTypeAlias {
	public DynamicTrafficTypeAlias(IModelNode parent, jprime.DynamicTrafficType.IDynamicTrafficType referencedNode) {
		super(parent,(jprime.DynamicTrafficType.IDynamicTrafficType)referencedNode);
	}
	public DynamicTrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public DynamicTrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public DynamicTrafficTypeAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.DynamicTrafficType.DynamicTrafficTypeAliasReplica c = new jprime.DynamicTrafficType.DynamicTrafficTypeAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1076: //DynamicTrafficTypeAlias
			case 1077: //DistributedTrafficTypeAlias
			case 1078: //CentralizedTrafficTypeAlias
			case 1079: //SwingTCPTrafficAlias
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
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
