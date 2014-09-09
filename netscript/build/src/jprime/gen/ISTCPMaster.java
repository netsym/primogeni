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
public interface ISTCPMaster extends jprime.ProtocolSession.IProtocolSession {

	/**
	 * @return maximum datagram size.
	 */
	public jprime.variable.IntegerVariable getMaxDatagramSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMaxDatagramSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMaxDatagramSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMaxDatagramSize(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
