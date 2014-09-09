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
public abstract class ApplicationSessionReplica extends jprime.ProtocolSession.ProtocolSessionReplica implements jprime.gen.IApplicationSession {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public ApplicationSessionReplica(String name, IModelNode parent, jprime.ApplicationSession.IApplicationSession referencedNode) {
		super(name,parent,referencedNode);
	}
	public ApplicationSessionReplica(ModelNodeRecord rec){ super(rec); }
	public ApplicationSessionReplica(PyObject[] v, String[] s){super(v,s);}
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
		jprime.ApplicationSession.ApplicationSessionReplica c = new jprime.ApplicationSession.ApplicationSessionReplica(this.getName(), (IModelNode)parent,(jprime.ApplicationSession.IApplicationSession)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1159: //ApplicationSessionReplica
			case 1160: //CNFApplicationReplica
			case 1161: //CBRReplica
			case 1162: //SwingServerReplica
			case 1163: //SwingClientReplica
			case 1164: //HTTPClientReplica
			case 1165: //HTTPServerReplica
			case 1166: //ICMPv4SessionReplica
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
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
