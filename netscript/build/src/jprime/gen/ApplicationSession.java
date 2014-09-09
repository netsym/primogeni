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
public abstract class ApplicationSession extends jprime.ProtocolSession.ProtocolSession implements jprime.gen.IApplicationSession {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public ApplicationSession(PyObject[] v, String[] s){super(v,s);}
	public ApplicationSession(ModelNodeRecord rec){ super(rec); }
	public ApplicationSession(IModelNode parent){ super(parent); }
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
		jprime.ApplicationSession.ApplicationSessionReplica c = new jprime.ApplicationSession.ApplicationSessionReplica(this.getName(),(IModelNode)parent,(jprime.ApplicationSession.IApplicationSession)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1047: //ApplicationSession
			case 1048: //CNFApplication
			case 1049: //CBR
			case 1050: //SwingServer
			case 1051: //SwingClient
			case 1052: //HTTPClient
			case 1053: //HTTPServer
			case 1054: //ICMPv4Session
			case 1103: //ApplicationSessionAlias
			case 1104: //CNFApplicationAlias
			case 1105: //CBRAlias
			case 1106: //SwingServerAlias
			case 1107: //SwingClientAlias
			case 1108: //HTTPClientAlias
			case 1109: //HTTPServerAlias
			case 1110: //ICMPv4SessionAlias
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
