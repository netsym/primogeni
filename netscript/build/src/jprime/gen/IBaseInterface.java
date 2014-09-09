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
public interface IBaseInterface extends jprime.IModelNode {

	/**
	 * @return ip address
	 */
	public jprime.variable.OpaqueVariable getIpAddress();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpAddress(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpAddress(jprime.variable.SymbolVariable value);

	/**
	 * @return mac address
	 */
	public jprime.variable.OpaqueVariable getMacAddress();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMacAddress(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMacAddress(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
