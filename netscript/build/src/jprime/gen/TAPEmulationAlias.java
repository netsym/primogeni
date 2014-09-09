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
public abstract class TAPEmulationAlias extends jprime.EmulationProtocol.EmulationProtocolAlias implements jprime.gen.ITAPEmulationAlias {
	public TAPEmulationAlias(IModelNode parent, jprime.TAPEmulation.ITAPEmulation referencedNode) {
		super(parent,(jprime.TAPEmulation.ITAPEmulation)referencedNode);
	}
	public TAPEmulationAlias(ModelNodeRecord rec){ super(rec); }
	public TAPEmulationAlias(PyObject[] v, String[] s){super(v,s);}
	public TAPEmulationAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.TAPEmulation.ITAPEmulation.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.TAPEmulation.TAPEmulationAliasReplica c = new jprime.TAPEmulation.TAPEmulationAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1059: //TAPEmulationAlias
			case 1171: //TAPEmulationAliasReplica
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
		return jprime.gen.TAPEmulation.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
