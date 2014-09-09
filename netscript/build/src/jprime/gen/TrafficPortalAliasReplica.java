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
public abstract class TrafficPortalAliasReplica extends jprime.EmulationProtocol.EmulationProtocolAliasReplica implements jprime.gen.ITrafficPortalAlias {
	public TrafficPortalAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public TrafficPortalAliasReplica(ModelNodeRecord rec){ super(rec); }
	public TrafficPortalAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.TrafficPortal.ITrafficPortal.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.TrafficPortal.TrafficPortalAliasReplica c = new jprime.TrafficPortal.TrafficPortalAliasReplica(this.getName(), (IModelNode)parent,(jprime.TrafficPortal.TrafficPortalAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1170: //TrafficPortalAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return list of ip prefixes that are reachable via this portal
	 */
	public jprime.variable.OpaqueVariable getNetworks() {
		return (jprime.variable.OpaqueVariable)((ITrafficPortal)deference()).getNetworks();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNetworks(String value) {
		((ITrafficPortal)deference()).setNetworks(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNetworks(jprime.variable.SymbolVariable value) {
		((ITrafficPortal)deference()).setNetworks(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.TrafficPortal.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
