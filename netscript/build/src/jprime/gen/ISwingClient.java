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
public interface ISwingClient extends jprime.ApplicationSession.IApplicationSession {

	/**
	 * @return This is the number of active downloading sessions
	 */
	public jprime.variable.IntegerVariable getActiveSessions();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setActiveSessions(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setActiveSessions(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setActiveSessions(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of bytes received from all sessions
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
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
