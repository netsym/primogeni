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
public abstract class TrafficPortalAlias extends jprime.EmulationProtocol.EmulationProtocolAlias implements jprime.gen.ITrafficPortalAlias {
	public TrafficPortalAlias(IModelNode parent, jprime.TrafficPortal.ITrafficPortal referencedNode) {
		super(parent,(jprime.TrafficPortal.ITrafficPortal)referencedNode);
	}
	public TrafficPortalAlias(ModelNodeRecord rec){ super(rec); }
	public TrafficPortalAlias(PyObject[] v, String[] s){super(v,s);}
	public TrafficPortalAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.TrafficPortal.TrafficPortalAliasReplica c = new jprime.TrafficPortal.TrafficPortalAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1058: //TrafficPortalAlias
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
