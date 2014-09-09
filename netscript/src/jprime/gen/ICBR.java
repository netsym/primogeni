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
public interface ICBR extends jprime.ApplicationSession.IApplicationSession {

	/**
	 * @return Listening port port UDP datagrams.
	 */
	public jprime.variable.IntegerVariable getListeningPort();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setListeningPort(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setListeningPort(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setListeningPort(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of bytes received from all sessions
	 */
	public jprime.variable.IntegerVariable getTotalBytesReceived();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTotalBytesReceived(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTotalBytesReceived(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTotalBytesReceived(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of bytes sent from all sessions
	 */
	public jprime.variable.IntegerVariable getTotalBytesSent();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTotalBytesSent(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTotalBytesSent(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTotalBytesSent(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
