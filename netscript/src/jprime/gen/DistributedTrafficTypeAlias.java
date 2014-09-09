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
public abstract class DistributedTrafficTypeAlias extends jprime.DynamicTrafficType.DynamicTrafficTypeAlias implements jprime.gen.IDistributedTrafficTypeAlias {
	public DistributedTrafficTypeAlias(IModelNode parent, jprime.DistributedTrafficType.IDistributedTrafficType referencedNode) {
		super(parent,(jprime.DistributedTrafficType.IDistributedTrafficType)referencedNode);
	}
	public DistributedTrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public DistributedTrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public DistributedTrafficTypeAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.DistributedTrafficType.DistributedTrafficTypeAliasReplica c = new jprime.DistributedTrafficType.DistributedTrafficTypeAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1077: //DistributedTrafficTypeAlias
			case 1189: //DistributedTrafficTypeAliasReplica
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
		return jprime.gen.DistributedTrafficType.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
