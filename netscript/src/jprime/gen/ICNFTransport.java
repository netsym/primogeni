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
import org.python.core.PyObject;
public interface ICNFTransport extends jprime.ProtocolSession.IProtocolSession {

	/**
	 * @return Number of bytes received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesReceived();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesReceived(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesReceived(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of requests received so far from all sessions
	 */
	public jprime.variable.IntegerVariable getRequestsReceived();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRequestsReceived(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRequestsReceived(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of bytes sent so far from all sessions
	 */
	public jprime.variable.IntegerVariable getBytesSent();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesSent(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesSent(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesSent(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of RIDS of other routers with cnf transports installed
	 */
	public jprime.variable.OpaqueVariable getCnfRouters();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCnfRouters(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCnfRouters(jprime.variable.SymbolVariable value);

	/**
	 * @return Whether this is a controller
	 */
	public jprime.variable.BooleanVariable getIsController();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsController(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsController(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIsController(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
