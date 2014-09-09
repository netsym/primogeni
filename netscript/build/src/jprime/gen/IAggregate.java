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
public interface IAggregate extends jprime.IModelNode {

	/**
	 * @return the id of the variable which will be aggregated.
	 */
	public jprime.variable.IntegerVariable getVarId();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setVarId(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setVarId(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setVarId(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of entity relative ids which will have their state aggregated
	 */
	public ResourceIdentifierVariable getToAggregate();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToAggregate(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToAggregate(jprime.ResourceIdentifier.ResourceID value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setToAggregate(jprime.variable.SymbolVariable value);

	/**
	 * @return min of all the values at the sample time.
	 */
	public jprime.variable.FloatingPointNumberVariable getMin();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMin(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMin(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMin(jprime.variable.SymbolVariable value);

	/**
	 * @return max of all the values at the sample time.
	 */
	public jprime.variable.FloatingPointNumberVariable getMax();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMax(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMax(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMax(jprime.variable.SymbolVariable value);

	/**
	 * @return the number of samples.
	 */
	public jprime.variable.FloatingPointNumberVariable getSampleCount();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSampleCount(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSampleCount(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSampleCount(jprime.variable.SymbolVariable value);

	/**
	 * @return total of all the values at the sample time.
	 */
	public jprime.variable.FloatingPointNumberVariable getSum();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSum(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSum(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSum(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
