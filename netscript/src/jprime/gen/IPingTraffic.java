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
public interface IPingTraffic extends jprime.ICMPTraffic.IICMPTraffic {

	/**
	 * @return The number of bytes in the payload to be sent (default: 56).
	 */
	public jprime.variable.IntegerVariable getPayloadSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPayloadSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPayloadSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPayloadSize(jprime.variable.SymbolVariable value);

	/**
	 * @return The number of requests to send (default: 1).
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
	 * @return The rtt calculated by the ping traffic
	 */
	public jprime.variable.FloatingPointNumberVariable getRtt();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRtt(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRtt(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRtt(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
