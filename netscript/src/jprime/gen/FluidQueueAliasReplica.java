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
public abstract class FluidQueueAliasReplica extends jprime.NicQueue.NicQueueAliasReplica implements jprime.gen.IFluidQueueAlias {
	public FluidQueueAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public FluidQueueAliasReplica(ModelNodeRecord rec){ super(rec); }
	public FluidQueueAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.FluidQueue.IFluidQueue.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.FluidQueue.FluidQueueAliasReplica c = new jprime.FluidQueue.FluidQueueAliasReplica(this.getName(), (IModelNode)parent,(jprime.FluidQueue.FluidQueueAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1179: //FluidQueueAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The type of the queue, must be red or droptail
	 */
	public jprime.variable.StringVariable getQueueType() {
		return (jprime.variable.StringVariable)((IFluidQueue)deference()).getQueueType();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueType(String value) {
		((IFluidQueue)deference()).setQueueType(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQueueType(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setQueueType(value);
	}

	/**
	 * @return Whether this queue is a RED queue, rather than a drop-tail
	 */
	public jprime.variable.BooleanVariable getIsRed() {
		return (jprime.variable.BooleanVariable)((IFluidQueue)deference()).getIsRed();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsRed(String value) {
		((IFluidQueue)deference()).setIsRed(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsRed(boolean value) {
		((IFluidQueue)deference()).setIsRed(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIsRed(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setIsRed(value);
	}

	/**
	 * @return The parameter used by RED queue to estimate the average queue length, must be between 0 and 1
	 */
	public jprime.variable.FloatingPointNumberVariable getWeight() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidQueue)deference()).getWeight();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWeight(String value) {
		((IFluidQueue)deference()).setWeight(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWeight(float value) {
		((IFluidQueue)deference()).setWeight(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setWeight(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setWeight(value);
	}

	/**
	 * @return the min threshold (in bytes) for calculating packet drop probability.
	 */
	public jprime.variable.FloatingPointNumberVariable getQmin() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidQueue)deference()).getQmin();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmin(String value) {
		((IFluidQueue)deference()).setQmin(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmin(float value) {
		((IFluidQueue)deference()).setQmin(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQmin(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setQmin(value);
	}

	/**
	 * @return the max threshold (in bytes) for calculating packet drop probability.
	 */
	public jprime.variable.FloatingPointNumberVariable getQmax() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidQueue)deference()).getQmax();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmax(String value) {
		((IFluidQueue)deference()).setQmax(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmax(float value) {
		((IFluidQueue)deference()).setQmax(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQmax(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setQmax(value);
	}

	/**
	 * @return Parameter to calculate packet drop probability, must be in the range (0,1].
	 */
	public jprime.variable.FloatingPointNumberVariable getPmax() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidQueue)deference()).getPmax();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPmax(String value) {
		((IFluidQueue)deference()).setPmax(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPmax(float value) {
		((IFluidQueue)deference()).setPmax(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPmax(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setPmax(value);
	}

	/**
	 * @return This RED option is used to avoid marking/dropping two packets in a row
	 */
	public jprime.variable.BooleanVariable getWaitOpt() {
		return (jprime.variable.BooleanVariable)((IFluidQueue)deference()).getWaitOpt();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWaitOpt(String value) {
		((IFluidQueue)deference()).setWaitOpt(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWaitOpt(boolean value) {
		((IFluidQueue)deference()).setWaitOpt(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setWaitOpt(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setWaitOpt(value);
	}

	/**
	 * @return Mean packet size in bytes used by RED to compute the average queue length, must be positive.
	 */
	public jprime.variable.FloatingPointNumberVariable getMeanPktsiz() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidQueue)deference()).getMeanPktsiz();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMeanPktsiz(String value) {
		((IFluidQueue)deference()).setMeanPktsiz(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMeanPktsiz(float value) {
		((IFluidQueue)deference()).setMeanPktsiz(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMeanPktsiz(jprime.variable.SymbolVariable value) {
		((IFluidQueue)deference()).setMeanPktsiz(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.FluidQueue.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
