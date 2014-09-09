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
public abstract class SwingServerAlias extends jprime.ApplicationSession.ApplicationSessionAlias implements jprime.gen.ISwingServerAlias {
	public SwingServerAlias(IModelNode parent, jprime.SwingServer.ISwingServer referencedNode) {
		super(parent,(jprime.SwingServer.ISwingServer)referencedNode);
	}
	public SwingServerAlias(ModelNodeRecord rec){ super(rec); }
	public SwingServerAlias(PyObject[] v, String[] s){super(v,s);}
	public SwingServerAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.SwingServer.ISwingServer.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.SwingServer.SwingServerAliasReplica c = new jprime.SwingServer.SwingServerAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1106: //SwingServerAlias
			case 1218: //SwingServerAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return Listening port for incoming connections.
	 */
	public jprime.variable.IntegerVariable getListeningPort() {
		return (jprime.variable.IntegerVariable)((ISwingServer)deference()).getListeningPort();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setListeningPort(String value) {
		((ISwingServer)deference()).setListeningPort(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setListeningPort(long value) {
		((ISwingServer)deference()).setListeningPort(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setListeningPort(jprime.variable.SymbolVariable value) {
		((ISwingServer)deference()).setListeningPort(value);
	}

	/**
	 * @return Number of bytes received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesReceived() {
		return (jprime.variable.IntegerVariable)((ISwingServer)deference()).getBytesReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(String value) {
		((ISwingServer)deference()).setBytesReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(long value) {
		((ISwingServer)deference()).setBytesReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesReceived(jprime.variable.SymbolVariable value) {
		((ISwingServer)deference()).setBytesReceived(value);
	}

	/**
	 * @return Number of requests received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getRequestsReceived() {
		return (jprime.variable.IntegerVariable)((ISwingServer)deference()).getRequestsReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(String value) {
		((ISwingServer)deference()).setRequestsReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(long value) {
		((ISwingServer)deference()).setRequestsReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRequestsReceived(jprime.variable.SymbolVariable value) {
		((ISwingServer)deference()).setRequestsReceived(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.SwingServer.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
