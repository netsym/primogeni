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
public abstract class UDPMasterAliasReplica extends jprime.ProtocolSession.ProtocolSessionAliasReplica implements jprime.gen.IUDPMasterAlias {
	public UDPMasterAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public UDPMasterAliasReplica(ModelNodeRecord rec){ super(rec); }
	public UDPMasterAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.UDPMaster.IUDPMaster.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.UDPMaster.UDPMasterAliasReplica c = new jprime.UDPMaster.UDPMasterAliasReplica(this.getName(), (IModelNode)parent,(jprime.UDPMaster.UDPMasterAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1210: //UDPMasterAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return maximum datagram size.
	 */
	public jprime.variable.IntegerVariable getMaxDatagramSize() {
		return (jprime.variable.IntegerVariable)((IUDPMaster)deference()).getMaxDatagramSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMaxDatagramSize(String value) {
		((IUDPMaster)deference()).setMaxDatagramSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMaxDatagramSize(long value) {
		((IUDPMaster)deference()).setMaxDatagramSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMaxDatagramSize(jprime.variable.SymbolVariable value) {
		((IUDPMaster)deference()).setMaxDatagramSize(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.UDPMaster.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
