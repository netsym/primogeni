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
public abstract class OpenVPNEmulationReplica extends jprime.EmulationProtocol.EmulationProtocolReplica implements jprime.gen.IOpenVPNEmulation {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public OpenVPNEmulationReplica(String name, IModelNode parent, jprime.OpenVPNEmulation.IOpenVPNEmulation referencedNode) {
		super(name,parent,referencedNode);
	}
	public OpenVPNEmulationReplica(ModelNodeRecord rec){ super(rec); }
	public OpenVPNEmulationReplica(PyObject[] v, String[] s){super(v,s);}
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
		doing_deep_copy=true;
		jprime.OpenVPNEmulation.OpenVPNEmulationReplica c = new jprime.OpenVPNEmulation.OpenVPNEmulationReplica(this.getName(), (IModelNode)parent,(jprime.OpenVPNEmulation.IOpenVPNEmulation)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
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

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.OpenVPNEmulation.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
