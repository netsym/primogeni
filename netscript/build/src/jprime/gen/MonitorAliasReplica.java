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
public abstract class MonitorAliasReplica extends jprime.ModelNodeAliasReplica implements jprime.gen.IMonitorAlias {
	public MonitorAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name,parent,referencedNode);
	}
	public MonitorAliasReplica(ModelNodeRecord rec){ super(rec); }
	public MonitorAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Monitor.IMonitor.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.Monitor.MonitorAliasReplica c = new jprime.Monitor.MonitorAliasReplica(this.getName(), (IModelNode)parent,(jprime.Monitor.MonitorAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1184: //MonitorAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return the period of polling (i.e. how long between polls) in milliseconds.
	 */
	public jprime.variable.IntegerVariable getPeriod() {
		return (jprime.variable.IntegerVariable)((IMonitor)deference()).getPeriod();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPeriod(String value) {
		((IMonitor)deference()).setPeriod(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPeriod(long value) {
		((IMonitor)deference()).setPeriod(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPeriod(jprime.variable.SymbolVariable value) {
		((IMonitor)deference()).setPeriod(value);
	}

	/**
	 * @return a list of entity relative ids which will have their state monitored
	 */
	public ResourceIdentifierVariable getToMonitor() {
		return (ResourceIdentifierVariable)((IMonitor)deference()).getToMonitor();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToMonitor(String value) {
		((IMonitor)deference()).setToMonitor(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setToMonitor(jprime.ResourceIdentifier.ResourceID value) {
		((IMonitor)deference()).setToMonitor(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setToMonitor(jprime.variable.SymbolVariable value) {
		((IMonitor)deference()).setToMonitor(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Monitor.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
