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
public abstract class EmulationProtocolAlias extends jprime.ModelNodeAlias implements jprime.gen.IEmulationProtocolAlias {
	public EmulationProtocolAlias(IModelNode parent, jprime.EmulationProtocol.IEmulationProtocol referencedNode) {
		super(parent,(IModelNode)referencedNode);
	}
	public EmulationProtocolAlias(ModelNodeRecord rec){ super(rec); }
	public EmulationProtocolAlias(PyObject[] v, String[] s){super(v,s);}
	public EmulationProtocolAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.EmulationProtocol.IEmulationProtocol.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.EmulationProtocol.EmulationProtocolAliasReplica c = new jprime.EmulationProtocol.EmulationProtocolAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1057: //EmulationProtocolAlias
			case 1058: //TrafficPortalAlias
			case 1059: //TAPEmulationAlias
			case 1060: //OpenVPNEmulationAlias
			case 1169: //EmulationProtocolAliasReplica
			case 1170: //TrafficPortalAliasReplica
			case 1171: //TAPEmulationAliasReplica
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
	 * @return If ip_forwarding is set to true the emulated interface will intercept all pkts it sees. If it is set to false it will only capture pkts targeted for it.
	 */
	public jprime.variable.BooleanVariable getIpForwarding() {
		return (jprime.variable.BooleanVariable)((IEmulationProtocol)deference()).getIpForwarding();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpForwarding(String value) {
		((IEmulationProtocol)deference()).setIpForwarding(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpForwarding(boolean value) {
		((IEmulationProtocol)deference()).setIpForwarding(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpForwarding(jprime.variable.SymbolVariable value) {
		((IEmulationProtocol)deference()).setIpForwarding(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.EmulationProtocol.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
