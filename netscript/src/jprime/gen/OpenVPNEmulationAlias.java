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
public abstract class OpenVPNEmulationAlias extends jprime.EmulationProtocol.EmulationProtocolAlias implements jprime.gen.IOpenVPNEmulationAlias {
	public OpenVPNEmulationAlias(IModelNode parent, jprime.OpenVPNEmulation.IOpenVPNEmulation referencedNode) {
		super(parent,(jprime.OpenVPNEmulation.IOpenVPNEmulation)referencedNode);
	}
	public OpenVPNEmulationAlias(ModelNodeRecord rec){ super(rec); }
	public OpenVPNEmulationAlias(PyObject[] v, String[] s){super(v,s);}
	public OpenVPNEmulationAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.OpenVPNEmulation.IOpenVPNEmulation.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.OpenVPNEmulation.OpenVPNEmulationAliasReplica c = new jprime.OpenVPNEmulation.OpenVPNEmulationAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1060: //OpenVPNEmulationAlias
			case 1172: //OpenVPNEmulationAliasReplica
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
		return jprime.gen.OpenVPNEmulation.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
