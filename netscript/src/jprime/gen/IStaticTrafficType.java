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
public interface IStaticTrafficType extends jprime.TrafficType.ITrafficType {

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
	 * @return a list of emu/real nodes ips
	 */
	public jprime.variable.OpaqueVariable getDstIps();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstIps(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstIps(jprime.variable.SymbolVariable value);

	/**
	 * @return Time before the traffic starts (default: 0).
	 */
	public jprime.variable.FloatingPointNumberVariable getStartTime();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStartTime(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStartTime(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStartTime(jprime.variable.SymbolVariable value);

	/**
	 * @return The time between sending each traffic (default: 0.1). 
	 */
	public jprime.variable.FloatingPointNumberVariable getInterval();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setInterval(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setInterval(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setInterval(jprime.variable.SymbolVariable value);

	/**
	 * @return Whether to use exponential interval (default: false).
	 */
	public jprime.variable.BooleanVariable getIntervalExponential();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIntervalExponential(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIntervalExponential(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIntervalExponential(jprime.variable.SymbolVariable value);

	/**
	 * @return The method to map the source and destination based on the src and dst lists
	 */
	public jprime.variable.StringVariable getMapping();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMapping(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMapping(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
