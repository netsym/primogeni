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
public abstract class GhostInterfaceAlias extends jprime.BaseInterface.BaseInterfaceAlias implements jprime.gen.IGhostInterfaceAlias {
	public GhostInterfaceAlias(IModelNode parent, jprime.GhostInterface.IGhostInterface referencedNode) {
		super(parent,(jprime.GhostInterface.IGhostInterface)referencedNode);
	}
	public GhostInterfaceAlias(ModelNodeRecord rec){ super(rec); }
	public GhostInterfaceAlias(PyObject[] v, String[] s){super(v,s);}
	public GhostInterfaceAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.GhostInterface.IGhostInterface.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.GhostInterface.GhostInterfaceAliasReplica c = new jprime.GhostInterface.GhostInterfaceAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1064: //GhostInterfaceAlias
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
	 * @return the uid of the remote node
	 */
	public jprime.variable.IntegerVariable getRealUid() {
		return (jprime.variable.IntegerVariable)((IGhostInterface)deference()).getRealUid();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRealUid(String value) {
		((IGhostInterface)deference()).setRealUid(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRealUid(long value) {
		((IGhostInterface)deference()).setRealUid(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRealUid(jprime.variable.SymbolVariable value) {
		((IGhostInterface)deference()).setRealUid(value);
	}

	/**
	 * @return the id of the remote community
	 */
	public jprime.variable.IntegerVariable getCommunityId() {
		return (jprime.variable.IntegerVariable)((IGhostInterface)deference()).getCommunityId();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCommunityId(String value) {
		((IGhostInterface)deference()).setCommunityId(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCommunityId(long value) {
		((IGhostInterface)deference()).setCommunityId(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCommunityId(jprime.variable.SymbolVariable value) {
		((IGhostInterface)deference()).setCommunityId(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.GhostInterface.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
