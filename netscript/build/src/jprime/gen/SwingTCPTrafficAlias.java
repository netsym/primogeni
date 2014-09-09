/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */


package jprime.gen;

import jprime.*;
import jprime.ModelNodeRecord;
import jprime.variable.*;
import org.python.core.PyObject;
import org.python.core.Py;
public abstract class SwingTCPTrafficAlias extends jprime.CentralizedTrafficType.CentralizedTrafficTypeAlias implements jprime.gen.ISwingTCPTrafficAlias {
	public SwingTCPTrafficAlias(IModelNode parent, jprime.SwingTCPTraffic.ISwingTCPTraffic referencedNode) {
		super(parent,(jprime.SwingTCPTraffic.ISwingTCPTraffic)referencedNode);
	}
	public SwingTCPTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public SwingTCPTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public SwingTCPTrafficAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.SwingTCPTraffic.ISwingTCPTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.SwingTCPTraffic.SwingTCPTrafficAliasReplica c = new jprime.SwingTCPTraffic.SwingTCPTrafficAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1079: //SwingTCPTrafficAlias
			case 1191: //SwingTCPTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The file that indicates the positions of all experical data of the parameters.
	 */
	public jprime.variable.StringVariable getTraceDescription() {
		return (jprime.variable.StringVariable)((ISwingTCPTraffic)deference()).getTraceDescription();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTraceDescription(String value) {
		((ISwingTCPTraffic)deference()).setTraceDescription(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTraceDescription(jprime.variable.SymbolVariable value) {
		((ISwingTCPTraffic)deference()).setTraceDescription(value);
	}

	/**
	 * @return whether to stretch some parameters
	 */
	public jprime.variable.BooleanVariable getStretch() {
		return (jprime.variable.BooleanVariable)((ISwingTCPTraffic)deference()).getStretch();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStretch(String value) {
		((ISwingTCPTraffic)deference()).setStretch(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStretch(boolean value) {
		((ISwingTCPTraffic)deference()).setStretch(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStretch(jprime.variable.SymbolVariable value) {
		((ISwingTCPTraffic)deference()).setStretch(value);
	}

	/**
	 * @return timeout for session (in second)
	 */
	public jprime.variable.FloatingPointNumberVariable getSessionTimeout() {
		return (jprime.variable.FloatingPointNumberVariable)((ISwingTCPTraffic)deference()).getSessionTimeout();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(String value) {
		((ISwingTCPTraffic)deference()).setSessionTimeout(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSessionTimeout(float value) {
		((ISwingTCPTraffic)deference()).setSessionTimeout(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSessionTimeout(jprime.variable.SymbolVariable value) {
		((ISwingTCPTraffic)deference()).setSessionTimeout(value);
	}

	/**
	 * @return timeout for rre (in second)
	 */
	public jprime.variable.FloatingPointNumberVariable getRreTimeout() {
		return (jprime.variable.FloatingPointNumberVariable)((ISwingTCPTraffic)deference()).getRreTimeout();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRreTimeout(String value) {
		((ISwingTCPTraffic)deference()).setRreTimeout(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRreTimeout(float value) {
		((ISwingTCPTraffic)deference()).setRreTimeout(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRreTimeout(jprime.variable.SymbolVariable value) {
		((ISwingTCPTraffic)deference()).setRreTimeout(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.SwingTCPTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
