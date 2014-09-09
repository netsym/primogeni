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
public abstract class ProtocolSessionAliasReplica extends jprime.ModelNodeAliasReplica implements jprime.gen.IProtocolSessionAlias {
	public ProtocolSessionAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name,parent,referencedNode);
	}
	public ProtocolSessionAliasReplica(ModelNodeRecord rec){ super(rec); }
	public ProtocolSessionAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.ProtocolSession.IProtocolSession.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.ProtocolSession.ProtocolSessionAliasReplica c = new jprime.ProtocolSession.ProtocolSessionAliasReplica(this.getName(), (IModelNode)parent,(jprime.ProtocolSession.ProtocolSessionAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1207: //ProtocolSessionAliasReplica
			case 1208: //CNFTransportAliasReplica
			case 1209: //STCPMasterAliasReplica
			case 1210: //UDPMasterAliasReplica
			case 1211: //SymbioSimAppProtAliasReplica
			case 1212: //TCPMasterAliasReplica
			case 1213: //ProbeSessionAliasReplica
			case 1214: //IPv4SessionAliasReplica
			case 1215: //ApplicationSessionAliasReplica
			case 1216: //CNFApplicationAliasReplica
			case 1217: //CBRAliasReplica
			case 1218: //SwingServerAliasReplica
			case 1219: //SwingClientAliasReplica
			case 1220: //HTTPClientAliasReplica
			case 1221: //HTTPServerAliasReplica
			case 1222: //ICMPv4SessionAliasReplica
			case 1223: //RoutingProtocolAliasReplica
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
		return jprime.gen.ProtocolSession.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
