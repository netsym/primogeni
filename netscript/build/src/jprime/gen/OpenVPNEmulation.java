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
public abstract class OpenVPNEmulation extends jprime.EmulationProtocol.EmulationProtocol implements jprime.gen.IOpenVPNEmulation {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public OpenVPNEmulation(PyObject[] v, String[] s){super(v,s);}
	public OpenVPNEmulation(ModelNodeRecord rec){ super(rec); }
	public OpenVPNEmulation(IModelNode parent){ super(parent); }
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
		jprime.OpenVPNEmulation.OpenVPNEmulationReplica c = new jprime.OpenVPNEmulation.OpenVPNEmulationReplica(this.getName(),(IModelNode)parent,(jprime.OpenVPNEmulation.IOpenVPNEmulation)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1004: //OpenVPNEmulation
			case 1060: //OpenVPNEmulationAlias
			case 1116: //OpenVPNEmulationReplica
			case 1172: //OpenVPNEmulationAliasReplica
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
