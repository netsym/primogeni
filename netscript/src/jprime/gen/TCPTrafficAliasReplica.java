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
public abstract class TCPTrafficAliasReplica extends jprime.StaticTrafficType.StaticTrafficTypeAliasReplica implements jprime.gen.ITCPTrafficAlias {
	public TCPTrafficAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public TCPTrafficAliasReplica(ModelNodeRecord rec){ super(rec); }
	public TCPTrafficAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.TCPTraffic.ITCPTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.TCPTraffic.TCPTrafficAliasReplica c = new jprime.TCPTraffic.TCPTrafficAliasReplica(this.getName(), (IModelNode)parent,(jprime.TCPTraffic.TCPTrafficAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1196: //TCPTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The destination port for an HTTP connection.
	 */
	public jprime.variable.IntegerVariable getDstPort() {
		return (jprime.variable.IntegerVariable)((ITCPTraffic)deference()).getDstPort();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(String value) {
		((ITCPTraffic)deference()).setDstPort(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(long value) {
		((ITCPTraffic)deference()).setDstPort(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstPort(jprime.variable.SymbolVariable value) {
		((ITCPTraffic)deference()).setDstPort(value);
	}

	/**
	 * @return The size to request to the server.
	 */
	public jprime.variable.IntegerVariable getFileSize() {
		return (jprime.variable.IntegerVariable)((ITCPTraffic)deference()).getFileSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFileSize(String value) {
		((ITCPTraffic)deference()).setFileSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFileSize(long value) {
		((ITCPTraffic)deference()).setFileSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setFileSize(jprime.variable.SymbolVariable value) {
		((ITCPTraffic)deference()).setFileSize(value);
	}

	/**
	 * @return Whether the file size are modeled by a concatenation of bounded Weibull and Pareto distributions
	 */
	public jprime.variable.BooleanVariable getFilePareto() {
		return (jprime.variable.BooleanVariable)((ITCPTraffic)deference()).getFilePareto();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFilePareto(String value) {
		((ITCPTraffic)deference()).setFilePareto(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFilePareto(boolean value) {
		((ITCPTraffic)deference()).setFilePareto(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setFilePareto(jprime.variable.SymbolVariable value) {
		((ITCPTraffic)deference()).setFilePareto(value);
	}

	/**
	 * @return Whether this host will request packets from a simulated or emulated entity.
	 */
	public jprime.variable.BooleanVariable getToEmulated() {
		return (jprime.variable.BooleanVariable)((ITCPTraffic)deference()).getToEmulated();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToEmulated(String value) {
		((ITCPTraffic)deference()).setToEmulated(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToEmulated(boolean value) {
		((ITCPTraffic)deference()).setToEmulated(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setToEmulated(jprime.variable.SymbolVariable value) {
		((ITCPTraffic)deference()).setToEmulated(value);
	}

	/**
	 * @return Number of session.
	 */
	public jprime.variable.IntegerVariable getNumberOfSessions() {
		return (jprime.variable.IntegerVariable)((ITCPTraffic)deference()).getNumberOfSessions();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfSessions(String value) {
		((ITCPTraffic)deference()).setNumberOfSessions(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfSessions(long value) {
		((ITCPTraffic)deference()).setNumberOfSessions(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumberOfSessions(jprime.variable.SymbolVariable value) {
		((ITCPTraffic)deference()).setNumberOfSessions(value);
	}

	/**
	 * @return Number of connections within a session.
	 */
	public jprime.variable.IntegerVariable getConnectionsPerSession() {
		return (jprime.variable.IntegerVariable)((ITCPTraffic)deference()).getConnectionsPerSession();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setConnectionsPerSession(String value) {
		((ITCPTraffic)deference()).setConnectionsPerSession(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setConnectionsPerSession(long value) {
		((ITCPTraffic)deference()).setConnectionsPerSession(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setConnectionsPerSession(jprime.variable.SymbolVariable value) {
		((ITCPTraffic)deference()).setConnectionsPerSession(value);
	}

	/**
	 * @return Session timeout in seconds.
	 */
	public jprime.variable.IntegerVariable getSessionTimeout() {
		return (jprime.variable.IntegerVariable)((ITCPTraffic)deference()).getSessionTimeout();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(String value) {
		((ITCPTraffic)deference()).setSessionTimeout(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(long value) {
		((ITCPTraffic)deference()).setSessionTimeout(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSessionTimeout(jprime.variable.SymbolVariable value) {
		((ITCPTraffic)deference()).setSessionTimeout(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.TCPTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
