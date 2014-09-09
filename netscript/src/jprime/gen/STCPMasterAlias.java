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
public abstract class STCPMasterAlias extends jprime.ProtocolSession.ProtocolSessionAlias implements jprime.gen.ISTCPMasterAlias {
	public STCPMasterAlias(IModelNode parent, jprime.STCPMaster.ISTCPMaster referencedNode) {
		super(parent,(jprime.STCPMaster.ISTCPMaster)referencedNode);
	}
	public STCPMasterAlias(ModelNodeRecord rec){ super(rec); }
	public STCPMasterAlias(PyObject[] v, String[] s){super(v,s);}
	public STCPMasterAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.STCPMaster.ISTCPMaster.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.STCPMaster.STCPMasterAliasReplica c = new jprime.STCPMaster.STCPMasterAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1097: //STCPMasterAlias
			case 1209: //STCPMasterAliasReplica
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
		return (jprime.variable.IntegerVariable)((ISTCPMaster)deference()).getMaxDatagramSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMaxDatagramSize(String value) {
		((ISTCPMaster)deference()).setMaxDatagramSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMaxDatagramSize(long value) {
		((ISTCPMaster)deference()).setMaxDatagramSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMaxDatagramSize(jprime.variable.SymbolVariable value) {
		((ISTCPMaster)deference()).setMaxDatagramSize(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.STCPMaster.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
