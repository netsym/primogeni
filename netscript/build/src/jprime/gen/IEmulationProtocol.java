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
public interface IEmulationProtocol extends jprime.IModelNode {

	/**
	 * @return If ip_forwarding is set to true the emulated interface will intercept all pkts it sees. If it is set to false it will only capture pkts targeted for it.
	 */
	public jprime.variable.BooleanVariable getIpForwarding();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpForwarding(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpForwarding(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpForwarding(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
