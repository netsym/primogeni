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
public interface ISwingTCPTraffic extends jprime.CentralizedTrafficType.ICentralizedTrafficType {

	/**
	 * @return The file that indicates the positions of all experical data of the parameters.
	 */
	public jprime.variable.StringVariable getTraceDescription();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTraceDescription(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTraceDescription(jprime.variable.SymbolVariable value);

	/**
	 * @return whether to stretch some parameters
	 */
	public jprime.variable.BooleanVariable getStretch();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStretch(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStretch(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStretch(jprime.variable.SymbolVariable value);

	/**
	 * @return timeout for session (in second)
	 */
	public jprime.variable.FloatingPointNumberVariable getSessionTimeout();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSessionTimeout(jprime.variable.SymbolVariable value);

	/**
	 * @return timeout for rre (in second)
	 */
	public jprime.variable.FloatingPointNumberVariable getRreTimeout();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRreTimeout(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRreTimeout(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRreTimeout(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
