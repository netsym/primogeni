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
public interface IMonitor extends jprime.IModelNode {

	/**
	 * @return the period of polling (i.e. how long between polls) in milliseconds.
	 */
	public jprime.variable.IntegerVariable getPeriod();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPeriod(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPeriod(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPeriod(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of entity relative ids which will have their state monitored
	 */
	public ResourceIdentifierVariable getToMonitor();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToMonitor(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToMonitor(jprime.ResourceIdentifier.ResourceID value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setToMonitor(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
