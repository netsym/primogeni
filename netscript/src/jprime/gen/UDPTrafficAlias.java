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
public abstract class UDPTrafficAlias extends jprime.StaticTrafficType.StaticTrafficTypeAlias implements jprime.gen.IUDPTrafficAlias {
	public UDPTrafficAlias(IModelNode parent, jprime.UDPTraffic.IUDPTraffic referencedNode) {
		super(parent,(jprime.UDPTraffic.IUDPTraffic)referencedNode);
	}
	public UDPTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public UDPTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public UDPTrafficAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.UDPTraffic.IUDPTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.UDPTraffic.UDPTrafficAliasReplica c = new jprime.UDPTraffic.UDPTrafficAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1082: //UDPTrafficAlias
			case 1194: //UDPTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The destination port for an CBR connection.
	 */
	public jprime.variable.IntegerVariable getDstPort() {
		return (jprime.variable.IntegerVariable)((IUDPTraffic)deference()).getDstPort();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(String value) {
		((IUDPTraffic)deference()).setDstPort(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(long value) {
		((IUDPTraffic)deference()).setDstPort(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstPort(jprime.variable.SymbolVariable value) {
		((IUDPTraffic)deference()).setDstPort(value);
	}

	/**
	 * @return The number of bytes to send each time interval.
	 */
	public jprime.variable.IntegerVariable getBytesToSendEachInterval() {
		return (jprime.variable.IntegerVariable)((IUDPTraffic)deference()).getBytesToSendEachInterval();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendEachInterval(String value) {
		((IUDPTraffic)deference()).setBytesToSendEachInterval(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendEachInterval(long value) {
		((IUDPTraffic)deference()).setBytesToSendEachInterval(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesToSendEachInterval(jprime.variable.SymbolVariable value) {
		((IUDPTraffic)deference()).setBytesToSendEachInterval(value);
	}

	/**
	 * @return Number of times the server will send 'bytesToSendEachInterval' to client.
	 */
	public jprime.variable.IntegerVariable getCount() {
		return (jprime.variable.IntegerVariable)((IUDPTraffic)deference()).getCount();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCount(String value) {
		((IUDPTraffic)deference()).setCount(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCount(long value) {
		((IUDPTraffic)deference()).setCount(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCount(jprime.variable.SymbolVariable value) {
		((IUDPTraffic)deference()).setCount(value);
	}

	/**
	 * @return Whether this host will send packets to a simulated or emulated entity).
	 */
	public jprime.variable.BooleanVariable getToEmulated() {
		return (jprime.variable.BooleanVariable)((IUDPTraffic)deference()).getToEmulated();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToEmulated(String value) {
		((IUDPTraffic)deference()).setToEmulated(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToEmulated(boolean value) {
		((IUDPTraffic)deference()).setToEmulated(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setToEmulated(jprime.variable.SymbolVariable value) {
		((IUDPTraffic)deference()).setToEmulated(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.UDPTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
