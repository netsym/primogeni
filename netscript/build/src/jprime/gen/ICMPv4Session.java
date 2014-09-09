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
public abstract class ICMPv4Session extends jprime.ApplicationSession.ApplicationSession implements jprime.gen.IICMPv4Session {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public ICMPv4Session(PyObject[] v, String[] s){super(v,s);}
	public ICMPv4Session(ModelNodeRecord rec){ super(rec); }
	public ICMPv4Session(IModelNode parent){ super(parent); }
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
		jprime.ICMPv4Session.ICMPv4SessionReplica c = new jprime.ICMPv4Session.ICMPv4SessionReplica(this.getName(),(IModelNode)parent,(jprime.ICMPv4Session.IICMPv4Session)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1054: //ICMPv4Session
			case 1110: //ICMPv4SessionAlias
			case 1166: //ICMPv4SessionReplica
			case 1222: //ICMPv4SessionAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();
	public final static java.util.ArrayList<Integer> attrIds=new java.util.ArrayList<Integer>();
	static {
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
