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
public abstract class HTTPClientAliasReplica extends jprime.ApplicationSession.ApplicationSessionAliasReplica implements jprime.gen.IHTTPClientAlias {
	public HTTPClientAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public HTTPClientAliasReplica(ModelNodeRecord rec){ super(rec); }
	public HTTPClientAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.HTTPClient.IHTTPClient.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.HTTPClient.HTTPClientAliasReplica c = new jprime.HTTPClient.HTTPClientAliasReplica(this.getName(), (IModelNode)parent,(jprime.HTTPClient.HTTPClientAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1220: //HTTPClientAliasReplica
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
		return (jprime.variable.IntegerVariable)((IHTTPClient)deference()).getActiveSessions();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setActiveSessions(String value) {
		((IHTTPClient)deference()).setActiveSessions(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setActiveSessions(long value) {
		((IHTTPClient)deference()).setActiveSessions(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setActiveSessions(jprime.variable.SymbolVariable value) {
		((IHTTPClient)deference()).setActiveSessions(value);
	}

	/**
	 * @return Number of bytes received from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesReceived() {
		return (jprime.variable.IntegerVariable)((IHTTPClient)deference()).getBytesReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(String value) {
		((IHTTPClient)deference()).setBytesReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(long value) {
		((IHTTPClient)deference()).setBytesReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesReceived(jprime.variable.SymbolVariable value) {
		((IHTTPClient)deference()).setBytesReceived(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.HTTPClient.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
