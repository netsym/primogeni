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
public abstract class PingTrafficAlias extends jprime.ICMPTraffic.ICMPTrafficAlias implements jprime.gen.IPingTrafficAlias {
	public PingTrafficAlias(IModelNode parent, jprime.PingTraffic.IPingTraffic referencedNode) {
		super(parent,(jprime.PingTraffic.IPingTraffic)referencedNode);
	}
	public PingTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public PingTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public PingTrafficAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.PingTraffic.IPingTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.PingTraffic.PingTrafficAliasReplica c = new jprime.PingTraffic.PingTrafficAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1086: //PingTrafficAlias
			case 1198: //PingTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The number of bytes in the payload to be sent (default: 56).
	 */
	public jprime.variable.IntegerVariable getPayloadSize() {
		return (jprime.variable.IntegerVariable)((IPingTraffic)deference()).getPayloadSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPayloadSize(String value) {
		((IPingTraffic)deference()).setPayloadSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPayloadSize(long value) {
		((IPingTraffic)deference()).setPayloadSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPayloadSize(jprime.variable.SymbolVariable value) {
		((IPingTraffic)deference()).setPayloadSize(value);
	}

	/**
	 * @return The number of requests to send (default: 1).
	 */
	public jprime.variable.IntegerVariable getCount() {
		return (jprime.variable.IntegerVariable)((IPingTraffic)deference()).getCount();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCount(String value) {
		((IPingTraffic)deference()).setCount(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCount(long value) {
		((IPingTraffic)deference()).setCount(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCount(jprime.variable.SymbolVariable value) {
		((IPingTraffic)deference()).setCount(value);
	}

	/**
	 * @return The rtt calculated by the ping traffic
	 */
	public jprime.variable.FloatingPointNumberVariable getRtt() {
		return (jprime.variable.FloatingPointNumberVariable)((IPingTraffic)deference()).getRtt();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRtt(String value) {
		((IPingTraffic)deference()).setRtt(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRtt(float value) {
		((IPingTraffic)deference()).setRtt(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRtt(jprime.variable.SymbolVariable value) {
		((IPingTraffic)deference()).setRtt(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.PingTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
