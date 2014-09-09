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
public abstract class CNFApplicationAlias extends jprime.ApplicationSession.ApplicationSessionAlias implements jprime.gen.ICNFApplicationAlias {
	public CNFApplicationAlias(IModelNode parent, jprime.CNFApplication.ICNFApplication referencedNode) {
		super(parent,(jprime.CNFApplication.ICNFApplication)referencedNode);
	}
	public CNFApplicationAlias(ModelNodeRecord rec){ super(rec); }
	public CNFApplicationAlias(PyObject[] v, String[] s){super(v,s);}
	public CNFApplicationAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.CNFApplication.ICNFApplication.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.CNFApplication.CNFApplicationAliasReplica c = new jprime.CNFApplication.CNFApplicationAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1104: //CNFApplicationAlias
			case 1216: //CNFApplicationAliasReplica
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
		return (jprime.variable.IntegerVariable)((ICNFApplication)deference()).getListeningPort();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setListeningPort(String value) {
		((ICNFApplication)deference()).setListeningPort(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setListeningPort(long value) {
		((ICNFApplication)deference()).setListeningPort(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setListeningPort(jprime.variable.SymbolVariable value) {
		((ICNFApplication)deference()).setListeningPort(value);
	}

	/**
	 * @return Number of bytes received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesReceived() {
		return (jprime.variable.IntegerVariable)((ICNFApplication)deference()).getBytesReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(String value) {
		((ICNFApplication)deference()).setBytesReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(long value) {
		((ICNFApplication)deference()).setBytesReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesReceived(jprime.variable.SymbolVariable value) {
		((ICNFApplication)deference()).setBytesReceived(value);
	}

	/**
	 * @return Number of bytes sent so far from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesSent() {
		return (jprime.variable.IntegerVariable)((ICNFApplication)deference()).getBytesSent();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesSent(String value) {
		((ICNFApplication)deference()).setBytesSent(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesSent(long value) {
		((ICNFApplication)deference()).setBytesSent(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesSent(jprime.variable.SymbolVariable value) {
		((ICNFApplication)deference()).setBytesSent(value);
	}

	/**
	 * @return Number of requests received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getRequestsReceived() {
		return (jprime.variable.IntegerVariable)((ICNFApplication)deference()).getRequestsReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(String value) {
		((ICNFApplication)deference()).setRequestsReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(long value) {
		((ICNFApplication)deference()).setRequestsReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRequestsReceived(jprime.variable.SymbolVariable value) {
		((ICNFApplication)deference()).setRequestsReceived(value);
	}

	/**
	 * @return a list of content id and their sizes. i.e. [100,1,200,2] --> [cid:100, size:1],[cid:200, size:2]
	 */
	public jprime.variable.OpaqueVariable getCnfContentSizes() {
		return (jprime.variable.OpaqueVariable)((ICNFApplication)deference()).getCnfContentSizes();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCnfContentSizes(String value) {
		((ICNFApplication)deference()).setCnfContentSizes(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCnfContentSizes(jprime.variable.SymbolVariable value) {
		((ICNFApplication)deference()).setCnfContentSizes(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.CNFApplication.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
