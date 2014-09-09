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
public abstract class ICMPv4SessionReplica extends jprime.ApplicationSession.ApplicationSessionReplica implements jprime.gen.IICMPv4Session {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public ICMPv4SessionReplica(String name, IModelNode parent, jprime.ICMPv4Session.IICMPv4Session referencedNode) {
		super(name,parent,referencedNode);
	}
	public ICMPv4SessionReplica(ModelNodeRecord rec){ super(rec); }
	public ICMPv4SessionReplica(PyObject[] v, String[] s){super(v,s);}
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
		doing_deep_copy=true;
		jprime.ICMPv4Session.ICMPv4SessionReplica c = new jprime.ICMPv4Session.ICMPv4SessionReplica(this.getName(), (IModelNode)parent,(jprime.ICMPv4Session.IICMPv4Session)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
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

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.ICMPv4Session.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
