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
import jprime.ModelNodeRecord;
import org.python.core.PyObject;
import org.python.core.Py;
public abstract class AggregateAliasReplica extends jprime.ModelNodeAliasReplica implements jprime.gen.IAggregateAlias {
	public AggregateAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name,parent,referencedNode);
	}
	public AggregateAliasReplica(ModelNodeRecord rec){ super(rec); }
	public AggregateAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Aggregate.IAggregate.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.Aggregate.AggregateAliasReplica c = new jprime.Aggregate.AggregateAliasReplica(this.getName(), (IModelNode)parent,(jprime.Aggregate.AggregateAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1182: //AggregateAliasReplica
			case 1183: //VizAggregateAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return the id of the variable which will be aggregated.
	 */
	public jprime.variable.IntegerVariable getVarId() {
		return (jprime.variable.IntegerVariable)((IAggregate)deference()).getVarId();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setVarId(String value) {
		((IAggregate)deference()).setVarId(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setVarId(long value) {
		((IAggregate)deference()).setVarId(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setVarId(jprime.variable.SymbolVariable value) {
		((IAggregate)deference()).setVarId(value);
	}

	/**
	 * @return a list of entity relative ids which will have their state aggregated
	 */
	public ResourceIdentifierVariable getToAggregate() {
		return (ResourceIdentifierVariable)((IAggregate)deference()).getToAggregate();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToAggregate(String value) {
		((IAggregate)deference()).setToAggregate(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToAggregate(jprime.ResourceIdentifier.ResourceID value) {
		((IAggregate)deference()).setToAggregate(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setToAggregate(jprime.variable.SymbolVariable value) {
		((IAggregate)deference()).setToAggregate(value);
	}

	/**
	 * @return min of all the values at the sample time.
	 */
	public jprime.variable.FloatingPointNumberVariable getMin() {
		return (jprime.variable.FloatingPointNumberVariable)((IAggregate)deference()).getMin();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMin(String value) {
		((IAggregate)deference()).setMin(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMin(float value) {
		((IAggregate)deference()).setMin(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMin(jprime.variable.SymbolVariable value) {
		((IAggregate)deference()).setMin(value);
	}

	/**
	 * @return max of all the values at the sample time.
	 */
	public jprime.variable.FloatingPointNumberVariable getMax() {
		return (jprime.variable.FloatingPointNumberVariable)((IAggregate)deference()).getMax();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMax(String value) {
		((IAggregate)deference()).setMax(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMax(float value) {
		((IAggregate)deference()).setMax(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMax(jprime.variable.SymbolVariable value) {
		((IAggregate)deference()).setMax(value);
	}

	/**
	 * @return the number of samples.
	 */
	public jprime.variable.FloatingPointNumberVariable getSampleCount() {
		return (jprime.variable.FloatingPointNumberVariable)((IAggregate)deference()).getSampleCount();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSampleCount(String value) {
		((IAggregate)deference()).setSampleCount(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSampleCount(float value) {
		((IAggregate)deference()).setSampleCount(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSampleCount(jprime.variable.SymbolVariable value) {
		((IAggregate)deference()).setSampleCount(value);
	}

	/**
	 * @return total of all the values at the sample time.
	 */
	public jprime.variable.FloatingPointNumberVariable getSum() {
		return (jprime.variable.FloatingPointNumberVariable)((IAggregate)deference()).getSum();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSum(String value) {
		((IAggregate)deference()).setSum(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSum(float value) {
		((IAggregate)deference()).setSum(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSum(jprime.variable.SymbolVariable value) {
		((IAggregate)deference()).setSum(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Aggregate.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
