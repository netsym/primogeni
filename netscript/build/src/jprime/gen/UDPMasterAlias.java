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
public abstract class UDPMasterAlias extends jprime.ProtocolSession.ProtocolSessionAlias implements jprime.gen.IUDPMasterAlias {
	public UDPMasterAlias(IModelNode parent, jprime.UDPMaster.IUDPMaster referencedNode) {
		super(parent,(jprime.UDPMaster.IUDPMaster)referencedNode);
	}
	public UDPMasterAlias(ModelNodeRecord rec){ super(rec); }
	public UDPMasterAlias(PyObject[] v, String[] s){super(v,s);}
	public UDPMasterAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.UDPMaster.UDPMasterAliasReplica c = new jprime.UDPMaster.UDPMasterAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1098: //UDPMasterAlias
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
