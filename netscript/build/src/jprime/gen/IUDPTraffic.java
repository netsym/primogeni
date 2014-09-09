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
public interface IUDPTraffic extends jprime.StaticTrafficType.IStaticTrafficType {

	/**
	 * @return The destination port for an CBR connection.
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
	 * @return The number of bytes to send each time interval.
	 */
	public jprime.variable.IntegerVariable getBytesToSendEachInterval();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendEachInterval(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendEachInterval(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesToSendEachInterval(jprime.variable.SymbolVariable value);

	/**
	 * @return Number of times the server will send 'bytesToSendEachInterval' to client.
	 */
	public jprime.variable.IntegerVariable getCount();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCount(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCount(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCount(jprime.variable.SymbolVariable value);

	/**
	 * @return Whether this host will send packets to a simulated or emulated entity).
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
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
