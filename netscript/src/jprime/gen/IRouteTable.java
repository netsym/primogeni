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
public interface IRouteTable extends jprime.IModelNode {

	/**
	 * @return A comma separated list of edge ifaces
	 */
	public jprime.variable.OpaqueVariable getEdgeIfaces();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setEdgeIfaces(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setEdgeIfaces(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
