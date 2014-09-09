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
public abstract class ProtocolSessionAlias extends jprime.ModelNodeAlias implements jprime.gen.IProtocolSessionAlias {
	public ProtocolSessionAlias(IModelNode parent, jprime.ProtocolSession.IProtocolSession referencedNode) {
		super(parent,(IModelNode)referencedNode);
	}
	public ProtocolSessionAlias(ModelNodeRecord rec){ super(rec); }
	public ProtocolSessionAlias(PyObject[] v, String[] s){super(v,s);}
	public ProtocolSessionAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.ProtocolSession.ProtocolSessionAliasReplica c = new jprime.ProtocolSession.ProtocolSessionAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1095: //ProtocolSessionAlias
			case 1096: //CNFTransportAlias
			case 1097: //STCPMasterAlias
			case 1098: //UDPMasterAlias
			case 1099: //SymbioSimAppProtAlias
			case 1100: //TCPMasterAlias
			case 1101: //ProbeSessionAlias
			case 1102: //IPv4SessionAlias
			case 1103: //ApplicationSessionAlias
			case 1104: //CNFApplicationAlias
			case 1105: //CBRAlias
			case 1106: //SwingServerAlias
			case 1107: //SwingClientAlias
			case 1108: //HTTPClientAlias
			case 1109: //HTTPServerAlias
			case 1110: //ICMPv4SessionAlias
			case 1111: //RoutingProtocolAlias
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
