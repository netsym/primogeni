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
public abstract class PPBPTrafficAliasReplica extends jprime.StaticTrafficType.StaticTrafficTypeAliasReplica implements jprime.gen.IPPBPTrafficAlias {
	public PPBPTrafficAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public PPBPTrafficAliasReplica(ModelNodeRecord rec){ super(rec); }
	public PPBPTrafficAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.PPBPTraffic.IPPBPTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.PPBPTraffic.PPBPTrafficAliasReplica c = new jprime.PPBPTraffic.PPBPTrafficAliasReplica(this.getName(), (IModelNode)parent,(jprime.PPBPTraffic.PPBPTrafficAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1195: //PPBPTrafficAliasReplica
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
		return (jprime.variable.IntegerVariable)((IPPBPTraffic)deference()).getDstPort();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(String value) {
		((IPPBPTraffic)deference()).setDstPort(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(long value) {
		((IPPBPTraffic)deference()).setDstPort(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstPort(jprime.variable.SymbolVariable value) {
		((IPPBPTraffic)deference()).setDstPort(value);
	}

	/**
	 * @return The number of bytes to send each time interval.
	 */
	public jprime.variable.IntegerVariable getBytesToSendPerInterval() {
		return (jprime.variable.IntegerVariable)((IPPBPTraffic)deference()).getBytesToSendPerInterval();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendPerInterval(String value) {
		((IPPBPTraffic)deference()).setBytesToSendPerInterval(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendPerInterval(long value) {
		((IPPBPTraffic)deference()).setBytesToSendPerInterval(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesToSendPerInterval(jprime.variable.SymbolVariable value) {
		((IPPBPTraffic)deference()).setBytesToSendPerInterval(value);
	}

	/**
	 * @return Average number of sessions (default: 0 means unset).
	 */
	public jprime.variable.IntegerVariable getAvgSessions() {
		return (jprime.variable.IntegerVariable)((IPPBPTraffic)deference()).getAvgSessions();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAvgSessions(String value) {
		((IPPBPTraffic)deference()).setAvgSessions(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAvgSessions(long value) {
		((IPPBPTraffic)deference()).setAvgSessions(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setAvgSessions(jprime.variable.SymbolVariable value) {
		((IPPBPTraffic)deference()).setAvgSessions(value);
	}

	/**
	 * @return The hurst parameter indicates the presence of LRD (Long-Range Dependence).
	 */
	public jprime.variable.FloatingPointNumberVariable getHurst() {
		return (jprime.variable.FloatingPointNumberVariable)((IPPBPTraffic)deference()).getHurst();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(String value) {
		((IPPBPTraffic)deference()).setHurst(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(float value) {
		((IPPBPTraffic)deference()).setHurst(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setHurst(jprime.variable.SymbolVariable value) {
		((IPPBPTraffic)deference()).setHurst(value);
	}

	/**
	 * @return The traffic stops starting new traffic flows after stop time.
	 */
	public jprime.variable.FloatingPointNumberVariable getStop() {
		return (jprime.variable.FloatingPointNumberVariable)((IPPBPTraffic)deference()).getStop();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(String value) {
		((IPPBPTraffic)deference()).setStop(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(float value) {
		((IPPBPTraffic)deference()).setStop(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStop(jprime.variable.SymbolVariable value) {
		((IPPBPTraffic)deference()).setStop(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.PPBPTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
