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
public abstract class ProtocolGraphAliasReplica extends jprime.ModelNodeAliasReplica implements jprime.gen.IProtocolGraphAlias {
	public ProtocolGraphAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name,parent,referencedNode);
	}
	public ProtocolGraphAliasReplica(ModelNodeRecord rec){ super(rec); }
	public ProtocolGraphAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.ProtocolGraph.IProtocolGraph.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.ProtocolGraph.ProtocolGraphAliasReplica c = new jprime.ProtocolGraph.ProtocolGraphAliasReplica(this.getName(), (IModelNode)parent,(jprime.ProtocolGraph.ProtocolGraphAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1199: //ProtocolGraphAliasReplica
			case 1200: //HostAliasReplica
			case 1201: //RouterAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return whether protocol sessions should be created on demand
	 */
	public jprime.variable.BooleanVariable getAutomaticProtocolSessionCreation() {
		return (jprime.variable.BooleanVariable)((IProtocolGraph)deference()).getAutomaticProtocolSessionCreation();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(String value) {
		((IProtocolGraph)deference()).setAutomaticProtocolSessionCreation(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(boolean value) {
		((IProtocolGraph)deference()).setAutomaticProtocolSessionCreation(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(jprime.variable.SymbolVariable value) {
		((IProtocolGraph)deference()).setAutomaticProtocolSessionCreation(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.ProtocolGraph.attrIds;
	}

	/**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.ProtocolSession.ProtocolSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.ProtocolSession.ProtocolSession.
	  */
	public void addProtocolSession(jprime.ProtocolSession.ProtocolSession kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSessionReplica(jprime.ProtocolSession.IProtocolSession to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.ProtocolSession.ProtocolSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession replicateProtocolSession(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSessionReplica(String name, jprime.ProtocolSession.IProtocolSession to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return Protocol sessions defined in the protocol graph
	  */
	public jprime.util.ChildList<jprime.ProtocolSession.IProtocolSession> getCfg_sess() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.CNFTransport.CNFTransport and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.CNFTransport.CNFTransport, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransport and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.CNFTransport.CNFTransport.
	  */
	public void addCNFTransport(jprime.CNFTransport.CNFTransport kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransportReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransportReplica(jprime.CNFTransport.ICNFTransport to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.CNFTransport.CNFTransportReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport replicateCNFTransport(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransportReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransportReplica(String name, jprime.CNFTransport.ICNFTransport to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.STCPMaster.STCPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.STCPMaster.STCPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.STCPMaster.STCPMaster.
	  */
	public void addSTCPMaster(jprime.STCPMaster.STCPMaster kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMasterReplica(jprime.STCPMaster.ISTCPMaster to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.STCPMaster.STCPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster replicateSTCPMaster(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMasterReplica(String name, jprime.STCPMaster.ISTCPMaster to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.UDPMaster.UDPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.UDPMaster.UDPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.UDPMaster.UDPMaster.
	  */
	public void addUDPMaster(jprime.UDPMaster.UDPMaster kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMasterReplica(jprime.UDPMaster.IUDPMaster to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.UDPMaster.UDPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster replicateUDPMaster(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMasterReplica(String name, jprime.UDPMaster.IUDPMaster to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt.
	  */
	public void addSymbioSimAppProt(jprime.SymbioSimAppProt.SymbioSimAppProt kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProtReplica(jprime.SymbioSimAppProt.ISymbioSimAppProt to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt replicateSymbioSimAppProt(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProtReplica(String name, jprime.SymbioSimAppProt.ISymbioSimAppProt to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.TCPMaster.TCPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.TCPMaster.TCPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.TCPMaster.TCPMaster.
	  */
	public void addTCPMaster(jprime.TCPMaster.TCPMaster kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMasterReplica(jprime.TCPMaster.ITCPMaster to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.TCPMaster.TCPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster replicateTCPMaster(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMasterReplica(String name, jprime.TCPMaster.ITCPMaster to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.ProbeSession.ProbeSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.ProbeSession.ProbeSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.ProbeSession.ProbeSession.
	  */
	public void addProbeSession(jprime.ProbeSession.ProbeSession kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSessionReplica(jprime.ProbeSession.IProbeSession to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.ProbeSession.ProbeSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession replicateProbeSession(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSessionReplica(String name, jprime.ProbeSession.IProbeSession to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.IPv4Session.IPv4Session and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.IPv4Session.IPv4Session, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4Session and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.IPv4Session.IPv4Session.
	  */
	public void addIPv4Session(jprime.IPv4Session.IPv4Session kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4SessionReplica(jprime.IPv4Session.IIPv4Session to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.IPv4Session.IPv4SessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session replicateIPv4Session(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4SessionReplica(String name, jprime.IPv4Session.IIPv4Session to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.ApplicationSession.ApplicationSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.ApplicationSession.ApplicationSession.
	  */
	public void addApplicationSession(jprime.ApplicationSession.ApplicationSession kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSessionReplica(jprime.ApplicationSession.IApplicationSession to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.ApplicationSession.ApplicationSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession replicateApplicationSession(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSessionReplica(String name, jprime.ApplicationSession.IApplicationSession to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocol and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.RoutingProtocol.RoutingProtocol, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocol and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.RoutingProtocol.RoutingProtocol.
	  */
	public void addRoutingProtocol(jprime.RoutingProtocol.RoutingProtocol kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocolReplica(jprime.RoutingProtocol.IRoutingProtocol to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol replicateRoutingProtocol(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocolReplica(String name, jprime.RoutingProtocol.IRoutingProtocol to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.CNFApplication.CNFApplication and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.CNFApplication.CNFApplication, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplication and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.CNFApplication.CNFApplication.
	  */
	public void addCNFApplication(jprime.CNFApplication.CNFApplication kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplicationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplicationReplica(jprime.CNFApplication.ICNFApplication to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.CNFApplication.CNFApplicationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication replicateCNFApplication(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplicationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplicationReplica(String name, jprime.CNFApplication.ICNFApplication to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.CBR.CBR and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.CBR.CBR, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CBR.CBR and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.CBR.CBR.
	  */
	public void addCBR(jprime.CBR.CBR kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CBR.CBRReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBRReplica(jprime.CBR.ICBR to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.CBR.CBRReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CBR.ICBR replicateCBR(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.CBR.CBRReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBRReplica(String name, jprime.CBR.ICBR to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.SwingServer.SwingServer and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.SwingServer.SwingServer, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServer and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.SwingServer.SwingServer.
	  */
	public void addSwingServer(jprime.SwingServer.SwingServer kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServerReplica(jprime.SwingServer.ISwingServer to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.SwingServer.SwingServerReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer replicateSwingServer(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServerReplica(String name, jprime.SwingServer.ISwingServer to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.SwingClient.SwingClient and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.SwingClient.SwingClient, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClient and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.SwingClient.SwingClient.
	  */
	public void addSwingClient(jprime.SwingClient.SwingClient kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClientReplica(jprime.SwingClient.ISwingClient to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.SwingClient.SwingClientReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient replicateSwingClient(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClientReplica(String name, jprime.SwingClient.ISwingClient to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.HTTPClient.HTTPClient and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.HTTPClient.HTTPClient, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClient and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.HTTPClient.HTTPClient.
	  */
	public void addHTTPClient(jprime.HTTPClient.HTTPClient kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClientReplica(jprime.HTTPClient.IHTTPClient to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.HTTPClient.HTTPClientReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient replicateHTTPClient(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClientReplica(String name, jprime.HTTPClient.IHTTPClient to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.HTTPServer.HTTPServer and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.HTTPServer.HTTPServer, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServer and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.HTTPServer.HTTPServer.
	  */
	public void addHTTPServer(jprime.HTTPServer.HTTPServer kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServerReplica(jprime.HTTPServer.IHTTPServer to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.HTTPServer.HTTPServerReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer replicateHTTPServer(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServerReplica(String name, jprime.HTTPServer.IHTTPServer to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4Session and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.ICMPv4Session.ICMPv4Session, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4Session and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.ICMPv4Session.ICMPv4Session.
	  */
	public void addICMPv4Session(jprime.ICMPv4Session.ICMPv4Session kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4SessionReplica(jprime.ICMPv4Session.IICMPv4Session to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session replicateICMPv4Session(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4SessionReplica(String name, jprime.ICMPv4Session.IICMPv4Session to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
