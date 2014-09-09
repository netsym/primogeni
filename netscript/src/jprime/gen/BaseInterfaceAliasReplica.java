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
public abstract class BaseInterfaceAliasReplica extends jprime.ModelNodeAliasReplica implements jprime.gen.IBaseInterfaceAlias {
	public BaseInterfaceAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name,parent,referencedNode);
	}
	public BaseInterfaceAliasReplica(ModelNodeRecord rec){ super(rec); }
	public BaseInterfaceAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.BaseInterface.IBaseInterface.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.BaseInterface.BaseInterfaceAliasReplica c = new jprime.BaseInterface.BaseInterfaceAliasReplica(this.getName(), (IModelNode)parent,(jprime.BaseInterface.BaseInterfaceAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1174: //BaseInterfaceAliasReplica
			case 1175: //InterfaceAliasReplica
			case 1176: //GhostInterfaceAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return ip address
	 */
	public jprime.variable.OpaqueVariable getIpAddress() {
		return (jprime.variable.OpaqueVariable)((IBaseInterface)deference()).getIpAddress();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpAddress(String value) {
		((IBaseInterface)deference()).setIpAddress(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpAddress(jprime.variable.SymbolVariable value) {
		((IBaseInterface)deference()).setIpAddress(value);
	}

	/**
	 * @return mac address
	 */
	public jprime.variable.OpaqueVariable getMacAddress() {
		return (jprime.variable.OpaqueVariable)((IBaseInterface)deference()).getMacAddress();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMacAddress(String value) {
		((IBaseInterface)deference()).setMacAddress(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMacAddress(jprime.variable.SymbolVariable value) {
		((IBaseInterface)deference()).setMacAddress(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.BaseInterface.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
