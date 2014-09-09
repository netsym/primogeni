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
public interface ITCPTraffic extends jprime.StaticTrafficType.IStaticTrafficType {

	/**
	 * @return The destination port for an HTTP connection.
	 */
	public jprime.variable.IntegerVariable getDstPort();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstPort(jprime.variable.SymbolVariable value);

	/**
	 * @return The size to request to the server.
	 */
	public jprime.variable.IntegerVariable getFileSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFileSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFileSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setFileSize(jprime.variable.SymbolVariable value);

	/**
	 * @return Whether the file size are modeled by a concatenation of bounded Weibull and Pareto distributions
	 */
	public jprime.variable.BooleanVariable getFilePareto();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFilePareto(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setFilePareto(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setFilePareto(jprime.variable.SymbolVariable value);

	/**
	 * @return Whether this host will request packets from a simulated or emulated entity.
	 */
	public jprime.variable.BooleanVariable getToEmulated();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToEmulated(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToEmulated(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setToEmulated(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of session.
	 */
	public jprime.variable.IntegerVariable getNumberOfSessions();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfSessions(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfSessions(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumberOfSessions(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of connections within a session.
	 */
	public jprime.variable.IntegerVariable getConnectionsPerSession();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setConnectionsPerSession(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setConnectionsPerSession(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setConnectionsPerSession(jprime.variable.SymbolVariable value);

	/**
	 * @return Session timeout in seconds.
	 */
	public jprime.variable.IntegerVariable getSessionTimeout();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSessionTimeout(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
