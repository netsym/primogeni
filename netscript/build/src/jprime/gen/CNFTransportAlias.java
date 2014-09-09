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
public abstract class CNFTransportAlias extends jprime.ProtocolSession.ProtocolSessionAlias implements jprime.gen.ICNFTransportAlias {
	public CNFTransportAlias(IModelNode parent, jprime.CNFTransport.ICNFTransport referencedNode) {
		super(parent,(jprime.CNFTransport.ICNFTransport)referencedNode);
	}
	public CNFTransportAlias(ModelNodeRecord rec){ super(rec); }
	public CNFTransportAlias(PyObject[] v, String[] s){super(v,s);}
	public CNFTransportAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.CNFTransport.ICNFTransport.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.CNFTransport.CNFTransportAliasReplica c = new jprime.CNFTransport.CNFTransportAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1096: //CNFTransportAlias
			case 1208: //CNFTransportAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return Number of bytes received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesReceived() {
		return (jprime.variable.IntegerVariable)((ICNFTransport)deference()).getBytesReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(String value) {
		((ICNFTransport)deference()).setBytesReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(long value) {
		((ICNFTransport)deference()).setBytesReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesReceived(jprime.variable.SymbolVariable value) {
		((ICNFTransport)deference()).setBytesReceived(value);
	}

	/**
	 * @return Number of requests received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getRequestsReceived() {
		return (jprime.variable.IntegerVariable)((ICNFTransport)deference()).getRequestsReceived();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(String value) {
		((ICNFTransport)deference()).setRequestsReceived(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(long value) {
		((ICNFTransport)deference()).setRequestsReceived(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRequestsReceived(jprime.variable.SymbolVariable value) {
		((ICNFTransport)deference()).setRequestsReceived(value);
	}

	/**
	 * @return Number of bytes sent so far from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesSent() {
		return (jprime.variable.IntegerVariable)((ICNFTransport)deference()).getBytesSent();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesSent(String value) {
		((ICNFTransport)deference()).setBytesSent(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesSent(long value) {
		((ICNFTransport)deference()).setBytesSent(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesSent(jprime.variable.SymbolVariable value) {
		((ICNFTransport)deference()).setBytesSent(value);
	}

	/**
	 * @return a list of RIDS of other routers with cnf transports installed
	 */
	public jprime.variable.OpaqueVariable getCnfRouters() {
		return (jprime.variable.OpaqueVariable)((ICNFTransport)deference()).getCnfRouters();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCnfRouters(String value) {
		((ICNFTransport)deference()).setCnfRouters(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCnfRouters(jprime.variable.SymbolVariable value) {
		((ICNFTransport)deference()).setCnfRouters(value);
	}

	/**
	 * @return Whether this is a controller
	 */
	public jprime.variable.BooleanVariable getIsController() {
		return (jprime.variable.BooleanVariable)((ICNFTransport)deference()).getIsController();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsController(String value) {
		((ICNFTransport)deference()).setIsController(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsController(boolean value) {
		((ICNFTransport)deference()).setIsController(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIsController(jprime.variable.SymbolVariable value) {
		((ICNFTransport)deference()).setIsController(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.CNFTransport.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
