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
public interface ITrafficPortal extends jprime.EmulationProtocol.IEmulationProtocol {

	/**
	 * @return list of ip prefixes that are reachable via this portal
	 */
	public jprime.variable.OpaqueVariable getNetworks();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNetworks(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNetworks(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
