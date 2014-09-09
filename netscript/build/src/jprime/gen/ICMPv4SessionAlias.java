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
public abstract class ICMPv4SessionAlias extends jprime.ApplicationSession.ApplicationSessionAlias implements jprime.gen.IICMPv4SessionAlias {
	public ICMPv4SessionAlias(IModelNode parent, jprime.ICMPv4Session.IICMPv4Session referencedNode) {
		super(parent,(jprime.ICMPv4Session.IICMPv4Session)referencedNode);
	}
	public ICMPv4SessionAlias(ModelNodeRecord rec){ super(rec); }
	public ICMPv4SessionAlias(PyObject[] v, String[] s){super(v,s);}
	public ICMPv4SessionAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.ICMPv4Session.IICMPv4Session.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.ICMPv4Session.ICMPv4SessionAliasReplica c = new jprime.ICMPv4Session.ICMPv4SessionAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1110: //ICMPv4SessionAlias
			case 1222: //ICMPv4SessionAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.ICMPv4Session.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
