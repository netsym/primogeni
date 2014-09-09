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
public abstract class ProtocolSession extends jprime.ModelNode implements jprime.gen.IProtocolSession {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public ProtocolSession(PyObject[] v, String[] s){super(v,s);}
	public ProtocolSession(ModelNodeRecord rec){ super(rec); }
	public ProtocolSession(IModelNode parent){ super(parent); }
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
		jprime.ProtocolSession.ProtocolSessionReplica c = new jprime.ProtocolSession.ProtocolSessionReplica(this.getName(),(IModelNode)parent,(jprime.ProtocolSession.IProtocolSession)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1039: //ProtocolSession
			case 1040: //CNFTransport
			case 1041: //STCPMaster
			case 1042: //UDPMaster
			case 1043: //SymbioSimAppProt
			case 1044: //TCPMaster
			case 1045: //ProbeSession
			case 1046: //IPv4Session
			case 1047: //ApplicationSession
			case 1048: //CNFApplication
			case 1049: //CBR
			case 1050: //SwingServer
			case 1051: //SwingClient
			case 1052: //HTTPClient
			case 1053: //HTTPServer
			case 1054: //ICMPv4Session
			case 1055: //RoutingProtocol
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
			case 1151: //ProtocolSessionReplica
			case 1152: //CNFTransportReplica
			case 1153: //STCPMasterReplica
			case 1154: //UDPMasterReplica
			case 1155: //SymbioSimAppProtReplica
			case 1156: //TCPMasterReplica
			case 1157: //ProbeSessionReplica
			case 1158: //IPv4SessionReplica
			case 1159: //ApplicationSessionReplica
			case 1160: //CNFApplicationReplica
			case 1161: //CBRReplica
			case 1162: //SwingServerReplica
			case 1163: //SwingClientReplica
			case 1164: //HTTPClientReplica
			case 1165: //HTTPServerReplica
			case 1166: //ICMPv4SessionReplica
			case 1167: //RoutingProtocolReplica
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
