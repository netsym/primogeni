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
public abstract class RedQueueAliasReplica extends jprime.NicQueue.NicQueueAliasReplica implements jprime.gen.IRedQueueAlias {
	public RedQueueAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public RedQueueAliasReplica(ModelNodeRecord rec){ super(rec); }
	public RedQueueAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.RedQueue.IRedQueue.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.RedQueue.RedQueueAliasReplica c = new jprime.RedQueue.RedQueueAliasReplica(this.getName(), (IModelNode)parent,(jprime.RedQueue.RedQueueAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1181: //RedQueueAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return weight used by the AQM policy to calculate average queue length for each packet arrival
	 */
	public jprime.variable.FloatingPointNumberVariable getWeight() {
		return (jprime.variable.FloatingPointNumberVariable)((IRedQueue)deference()).getWeight();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWeight(String value) {
		((IRedQueue)deference()).setWeight(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWeight(float value) {
		((IRedQueue)deference()).setWeight(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setWeight(jprime.variable.SymbolVariable value) {
		((IRedQueue)deference()).setWeight(value);
	}

	/**
	 * @return the min threshold (in bytes) for calculating packet drop probability
	 */
	public jprime.variable.FloatingPointNumberVariable getQmin() {
		return (jprime.variable.FloatingPointNumberVariable)((IRedQueue)deference()).getQmin();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmin(String value) {
		((IRedQueue)deference()).setQmin(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmin(float value) {
		((IRedQueue)deference()).setQmin(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQmin(jprime.variable.SymbolVariable value) {
		((IRedQueue)deference()).setQmin(value);
	}

	/**
	 * @return the max threshold (in bytes) for calculating packet drop probability
	 */
	public jprime.variable.FloatingPointNumberVariable getQmax() {
		return (jprime.variable.FloatingPointNumberVariable)((IRedQueue)deference()).getQmax();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmax(String value) {
		((IRedQueue)deference()).setQmax(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQmax(float value) {
		((IRedQueue)deference()).setQmax(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQmax(jprime.variable.SymbolVariable value) {
		((IRedQueue)deference()).setQmax(value);
	}

	/**
	 * @return qcap for calculating packet drop probability
	 */
	public jprime.variable.FloatingPointNumberVariable getQcap() {
		return (jprime.variable.FloatingPointNumberVariable)((IRedQueue)deference()).getQcap();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQcap(String value) {
		((IRedQueue)deference()).setQcap(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQcap(float value) {
		((IRedQueue)deference()).setQcap(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQcap(jprime.variable.SymbolVariable value) {
		((IRedQueue)deference()).setQcap(value);
	}

	/**
	 * @return pmax for calculating packet drop probability
	 */
	public jprime.variable.FloatingPointNumberVariable getPmax() {
		return (jprime.variable.FloatingPointNumberVariable)((IRedQueue)deference()).getPmax();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPmax(String value) {
		((IRedQueue)deference()).setPmax(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPmax(float value) {
		((IRedQueue)deference()).setPmax(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPmax(jprime.variable.SymbolVariable value) {
		((IRedQueue)deference()).setPmax(value);
	}

	/**
	 * @return an option to avoid marking/dropping two packets in a row
	 */
	public jprime.variable.BooleanVariable getWaitOpt() {
		return (jprime.variable.BooleanVariable)((IRedQueue)deference()).getWaitOpt();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWaitOpt(String value) {
		((IRedQueue)deference()).setWaitOpt(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWaitOpt(boolean value) {
		((IRedQueue)deference()).setWaitOpt(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setWaitOpt(jprime.variable.SymbolVariable value) {
		((IRedQueue)deference()).setWaitOpt(value);
	}

	/**
	 * @return mean packet size in bytes
	 */
	public jprime.variable.IntegerVariable getMeanPktsiz() {
		return (jprime.variable.IntegerVariable)((IRedQueue)deference()).getMeanPktsiz();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMeanPktsiz(String value) {
		((IRedQueue)deference()).setMeanPktsiz(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMeanPktsiz(long value) {
		((IRedQueue)deference()).setMeanPktsiz(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMeanPktsiz(jprime.variable.SymbolVariable value) {
		((IRedQueue)deference()).setMeanPktsiz(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.RedQueue.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
