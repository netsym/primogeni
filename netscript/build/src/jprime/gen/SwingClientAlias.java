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
public abstract class SwingClientAlias extends jprime.ApplicationSession.ApplicationSessionAlias implements jprime.gen.ISwingClientAlias {
	public SwingClientAlias(IModelNode parent, jprime.SwingClient.ISwingClient referencedNode) {
		super(parent,(jprime.SwingClient.ISwingClient)referencedNode);
	}
	public SwingClientAlias(ModelNodeRecord rec){ super(rec); }
	public SwingClientAlias(PyObject[] v, String[] s){super(v,s);}
	public SwingClientAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.SwingClient.ISwingClient.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.SwingClient.SwingClientAliasReplica c = new jprime.SwingClient.SwingClientAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1107: //SwingClientAlias
			case 1219: //SwingClientAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return This is the number of active downloading sessions
	 */
	public jprime.variable.IntegerVariable getActiveSessions() {
		return (jprime.variable.IntegerVariable)((ISwingClient)deference()).getActiveSessions();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setActiveSessions(String value) {
		((ISwingClient)deference()).setActiveSessions(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setActiveSessions(long value) {
		((ISwingClient)deference()).setActiveSessions(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setActiveSessions(jprime.variable.SymbolVariable value) {
		((ISwingClient)deference()).setActiveSessions(value);
	}

	/**
	 * @return Number of bytes received from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesReceived() {
		return (jprime.variable.IntegerVariable)((ISwingClient)deference()).getBytesReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(String value) {
		((ISwingClient)deference()).setBytesReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(long value) {
		((ISwingClient)deference()).setBytesReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesReceived(jprime.variable.SymbolVariable value) {
		((ISwingClient)deference()).setBytesReceived(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.SwingClient.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
