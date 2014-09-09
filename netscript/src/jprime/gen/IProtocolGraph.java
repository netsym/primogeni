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
import org.python.core.PyObject;
public interface IProtocolGraph extends jprime.IModelNode {

	/**
	 * @return whether protocol sessions should be created on demand
	 */
	public jprime.variable.BooleanVariable getAutomaticProtocolSessionCreation();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();

	/**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession();

	/**
	  * jython method to create a a new child of type jprime.ProtocolSession.ProtocolSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession(String name);

	 /**
	  * Add a new child of type jprime.ProtocolSession.ProtocolSession.
	  */
	public void addProtocolSession(jprime.ProtocolSession.ProtocolSession kid);

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSessionReplica(jprime.ProtocolSession.IProtocolSession to_replicate);

	/**
	  * jython method to create replica new child of type jprime.ProtocolSession.ProtocolSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession replicateProtocolSession(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSessionReplica(String name, jprime.ProtocolSession.IProtocolSession to_replicate);

	/**
	  * return Protocol sessions defined in the protocol graph
	  */
	public jprime.util.ChildList<jprime.ProtocolSession.IProtocolSession> getCfg_sess();

	/**
	  * Create a new child of type jprime.CNFTransport.CNFTransport and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport();

	/**
	  * jython method to create a a new child of type jprime.CNFTransport.CNFTransport, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransport and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport(String name);

	 /**
	  * Add a new child of type jprime.CNFTransport.CNFTransport.
	  */
	public void addCNFTransport(jprime.CNFTransport.CNFTransport kid);

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransportReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransportReplica(jprime.CNFTransport.ICNFTransport to_replicate);

	/**
	  * jython method to create replica new child of type jprime.CNFTransport.CNFTransportReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport replicateCNFTransport(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransportReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransportReplica(String name, jprime.CNFTransport.ICNFTransport to_replicate);

	/**
	  * Create a new child of type jprime.STCPMaster.STCPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster();

	/**
	  * jython method to create a a new child of type jprime.STCPMaster.STCPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster(String name);

	 /**
	  * Add a new child of type jprime.STCPMaster.STCPMaster.
	  */
	public void addSTCPMaster(jprime.STCPMaster.STCPMaster kid);

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMasterReplica(jprime.STCPMaster.ISTCPMaster to_replicate);

	/**
	  * jython method to create replica new child of type jprime.STCPMaster.STCPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster replicateSTCPMaster(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMasterReplica(String name, jprime.STCPMaster.ISTCPMaster to_replicate);

	/**
	  * Create a new child of type jprime.UDPMaster.UDPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster();

	/**
	  * jython method to create a a new child of type jprime.UDPMaster.UDPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster(String name);

	 /**
	  * Add a new child of type jprime.UDPMaster.UDPMaster.
	  */
	public void addUDPMaster(jprime.UDPMaster.UDPMaster kid);

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMasterReplica(jprime.UDPMaster.IUDPMaster to_replicate);

	/**
	  * jython method to create replica new child of type jprime.UDPMaster.UDPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster replicateUDPMaster(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMasterReplica(String name, jprime.UDPMaster.IUDPMaster to_replicate);

	/**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt();

	/**
	  * jython method to create a a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt(String name);

	 /**
	  * Add a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt.
	  */
	public void addSymbioSimAppProt(jprime.SymbioSimAppProt.SymbioSimAppProt kid);

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProtReplica(jprime.SymbioSimAppProt.ISymbioSimAppProt to_replicate);

	/**
	  * jython method to create replica new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt replicateSymbioSimAppProt(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProtReplica(String name, jprime.SymbioSimAppProt.ISymbioSimAppProt to_replicate);

	/**
	  * Create a new child of type jprime.TCPMaster.TCPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster();

	/**
	  * jython method to create a a new child of type jprime.TCPMaster.TCPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster(String name);

	 /**
	  * Add a new child of type jprime.TCPMaster.TCPMaster.
	  */
	public void addTCPMaster(jprime.TCPMaster.TCPMaster kid);

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMasterReplica(jprime.TCPMaster.ITCPMaster to_replicate);

	/**
	  * jython method to create replica new child of type jprime.TCPMaster.TCPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster replicateTCPMaster(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMasterReplica(String name, jprime.TCPMaster.ITCPMaster to_replicate);

	/**
	  * Create a new child of type jprime.ProbeSession.ProbeSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession();

	/**
	  * jython method to create a a new child of type jprime.ProbeSession.ProbeSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession(String name);

	 /**
	  * Add a new child of type jprime.ProbeSession.ProbeSession.
	  */
	public void addProbeSession(jprime.ProbeSession.ProbeSession kid);

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSessionReplica(jprime.ProbeSession.IProbeSession to_replicate);

	/**
	  * jython method to create replica new child of type jprime.ProbeSession.ProbeSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession replicateProbeSession(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSessionReplica(String name, jprime.ProbeSession.IProbeSession to_replicate);

	/**
	  * Create a new child of type jprime.IPv4Session.IPv4Session and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session();

	/**
	  * jython method to create a a new child of type jprime.IPv4Session.IPv4Session, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4Session and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session(String name);

	 /**
	  * Add a new child of type jprime.IPv4Session.IPv4Session.
	  */
	public void addIPv4Session(jprime.IPv4Session.IPv4Session kid);

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4SessionReplica(jprime.IPv4Session.IIPv4Session to_replicate);

	/**
	  * jython method to create replica new child of type jprime.IPv4Session.IPv4SessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session replicateIPv4Session(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4SessionReplica(String name, jprime.IPv4Session.IIPv4Session to_replicate);

	/**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession();

	/**
	  * jython method to create a a new child of type jprime.ApplicationSession.ApplicationSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession(String name);

	 /**
	  * Add a new child of type jprime.ApplicationSession.ApplicationSession.
	  */
	public void addApplicationSession(jprime.ApplicationSession.ApplicationSession kid);

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSessionReplica(jprime.ApplicationSession.IApplicationSession to_replicate);

	/**
	  * jython method to create replica new child of type jprime.ApplicationSession.ApplicationSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession replicateApplicationSession(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSessionReplica(String name, jprime.ApplicationSession.IApplicationSession to_replicate);

	/**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocol and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol();

	/**
	  * jython method to create a a new child of type jprime.RoutingProtocol.RoutingProtocol, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocol and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol(String name);

	 /**
	  * Add a new child of type jprime.RoutingProtocol.RoutingProtocol.
	  */
	public void addRoutingProtocol(jprime.RoutingProtocol.RoutingProtocol kid);

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocolReplica(jprime.RoutingProtocol.IRoutingProtocol to_replicate);

	/**
	  * jython method to create replica new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol replicateRoutingProtocol(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocolReplica(String name, jprime.RoutingProtocol.IRoutingProtocol to_replicate);

	/**
	  * Create a new child of type jprime.CNFApplication.CNFApplication and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication();

	/**
	  * jython method to create a a new child of type jprime.CNFApplication.CNFApplication, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplication and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication(String name);

	 /**
	  * Add a new child of type jprime.CNFApplication.CNFApplication.
	  */
	public void addCNFApplication(jprime.CNFApplication.CNFApplication kid);

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplicationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplicationReplica(jprime.CNFApplication.ICNFApplication to_replicate);

	/**
	  * jython method to create replica new child of type jprime.CNFApplication.CNFApplicationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication replicateCNFApplication(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplicationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplicationReplica(String name, jprime.CNFApplication.ICNFApplication to_replicate);

	/**
	  * Create a new child of type jprime.CBR.CBR and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR();

	/**
	  * jython method to create a a new child of type jprime.CBR.CBR, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.CBR.CBR and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR(String name);

	 /**
	  * Add a new child of type jprime.CBR.CBR.
	  */
	public void addCBR(jprime.CBR.CBR kid);

	 /**
	  * Create a new child of type jprime.CBR.CBRReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBRReplica(jprime.CBR.ICBR to_replicate);

	/**
	  * jython method to create replica new child of type jprime.CBR.CBRReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CBR.ICBR replicateCBR(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.CBR.CBRReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBRReplica(String name, jprime.CBR.ICBR to_replicate);

	/**
	  * Create a new child of type jprime.SwingServer.SwingServer and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer();

	/**
	  * jython method to create a a new child of type jprime.SwingServer.SwingServer, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServer and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer(String name);

	 /**
	  * Add a new child of type jprime.SwingServer.SwingServer.
	  */
	public void addSwingServer(jprime.SwingServer.SwingServer kid);

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServerReplica(jprime.SwingServer.ISwingServer to_replicate);

	/**
	  * jython method to create replica new child of type jprime.SwingServer.SwingServerReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer replicateSwingServer(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServerReplica(String name, jprime.SwingServer.ISwingServer to_replicate);

	/**
	  * Create a new child of type jprime.SwingClient.SwingClient and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient();

	/**
	  * jython method to create a a new child of type jprime.SwingClient.SwingClient, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClient and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient(String name);

	 /**
	  * Add a new child of type jprime.SwingClient.SwingClient.
	  */
	public void addSwingClient(jprime.SwingClient.SwingClient kid);

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClientReplica(jprime.SwingClient.ISwingClient to_replicate);

	/**
	  * jython method to create replica new child of type jprime.SwingClient.SwingClientReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient replicateSwingClient(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClientReplica(String name, jprime.SwingClient.ISwingClient to_replicate);

	/**
	  * Create a new child of type jprime.HTTPClient.HTTPClient and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient();

	/**
	  * jython method to create a a new child of type jprime.HTTPClient.HTTPClient, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClient and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient(String name);

	 /**
	  * Add a new child of type jprime.HTTPClient.HTTPClient.
	  */
	public void addHTTPClient(jprime.HTTPClient.HTTPClient kid);

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClientReplica(jprime.HTTPClient.IHTTPClient to_replicate);

	/**
	  * jython method to create replica new child of type jprime.HTTPClient.HTTPClientReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient replicateHTTPClient(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClientReplica(String name, jprime.HTTPClient.IHTTPClient to_replicate);

	/**
	  * Create a new child of type jprime.HTTPServer.HTTPServer and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer();

	/**
	  * jython method to create a a new child of type jprime.HTTPServer.HTTPServer, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServer and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer(String name);

	 /**
	  * Add a new child of type jprime.HTTPServer.HTTPServer.
	  */
	public void addHTTPServer(jprime.HTTPServer.HTTPServer kid);

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServerReplica(jprime.HTTPServer.IHTTPServer to_replicate);

	/**
	  * jython method to create replica new child of type jprime.HTTPServer.HTTPServerReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer replicateHTTPServer(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServerReplica(String name, jprime.HTTPServer.IHTTPServer to_replicate);

	/**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4Session and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session();

	/**
	  * jython method to create a a new child of type jprime.ICMPv4Session.ICMPv4Session, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4Session and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session(String name);

	 /**
	  * Add a new child of type jprime.ICMPv4Session.ICMPv4Session.
	  */
	public void addICMPv4Session(jprime.ICMPv4Session.ICMPv4Session kid);

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4SessionReplica(jprime.ICMPv4Session.IICMPv4Session to_replicate);

	/**
	  * jython method to create replica new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session replicateICMPv4Session(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4SessionReplica(String name, jprime.ICMPv4Session.IICMPv4Session to_replicate);
}
