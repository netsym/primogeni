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
public interface ICentralizedTrafficType extends jprime.DynamicTrafficType.IDynamicTrafficType {

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getSrcs();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(jprime.ResourceIdentifier.ResourceID value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSrcs(jprime.variable.SymbolVariable value);

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getDsts();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(jprime.ResourceIdentifier.ResourceID value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDsts(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
