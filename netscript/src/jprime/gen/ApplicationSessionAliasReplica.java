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
public abstract class ApplicationSessionAliasReplica extends jprime.ProtocolSession.ProtocolSessionAliasReplica implements jprime.gen.IApplicationSessionAlias {
	public ApplicationSessionAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public ApplicationSessionAliasReplica(ModelNodeRecord rec){ super(rec); }
	public ApplicationSessionAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.ApplicationSession.IApplicationSession.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.ApplicationSession.ApplicationSessionAliasReplica c = new jprime.ApplicationSession.ApplicationSessionAliasReplica(this.getName(), (IModelNode)parent,(jprime.ApplicationSession.ApplicationSessionAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1215: //ApplicationSessionAliasReplica
			case 1216: //CNFApplicationAliasReplica
			case 1217: //CBRAliasReplica
			case 1218: //SwingServerAliasReplica
			case 1219: //SwingClientAliasReplica
			case 1220: //HTTPClientAliasReplica
			case 1221: //HTTPServerAliasReplica
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
		return jprime.gen.ApplicationSession.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
