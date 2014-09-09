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
public abstract class NetAlias extends jprime.ModelNodeAlias implements jprime.gen.INetAlias {
	public NetAlias(IModelNode parent, jprime.Net.INet referencedNode) {
		super(parent,(IModelNode)referencedNode);
	}
	public NetAlias(ModelNodeRecord rec){ super(rec); }
	public NetAlias(PyObject[] v, String[] s){super(v,s);}
	public NetAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Net.INet.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.Net.NetAliasReplica c = new jprime.Net.NetAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1065: //NetAlias
			case 1177: //NetAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The n in W.X.Y.Z/n
	 */
	public jprime.variable.IntegerVariable getIpPrefixLen() {
		return (jprime.variable.IntegerVariable)((INet)deference()).getIpPrefixLen();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(String value) {
		((INet)deference()).setIpPrefixLen(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(long value) {
		((INet)deference()).setIpPrefixLen(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefixLen(jprime.variable.SymbolVariable value) {
		((INet)deference()).setIpPrefixLen(value);
	}

	/**
	 * @return ip prefix of this network
	 */
	public jprime.variable.OpaqueVariable getIpPrefix() {
		return (jprime.variable.OpaqueVariable)((INet)deference()).getIpPrefix();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefix(String value) {
		((INet)deference()).setIpPrefix(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefix(jprime.variable.SymbolVariable value) {
		((INet)deference()).setIpPrefix(value);
	}

	/**
	 * @return a list of content id and uid. i.e. [1,100,2,200] --> [ cid:1, uid:100],[ cid:2,uid:200]
	 */
	public jprime.variable.OpaqueVariable getCnfContentIds() {
		return (jprime.variable.OpaqueVariable)((INet)deference()).getCnfContentIds();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCnfContentIds(String value) {
		((INet)deference()).setCnfContentIds(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCnfContentIds(jprime.variable.SymbolVariable value) {
		((INet)deference()).setCnfContentIds(value);
	}

	/**
	 * @return a list of rules in the form [ip_prefix->(router_uid,iface_uid,iface_ip)]. They will automatically be compiled into the topnet.
	 */
	public jprime.variable.OpaqueVariable getPortalRules() {
		return (jprime.variable.OpaqueVariable)((INet)deference()).getPortalRules();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPortalRules(String value) {
		((INet)deference()).setPortalRules(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPortalRules(jprime.variable.SymbolVariable value) {
		((INet)deference()).setPortalRules(value);
	}

	/**
	 * @return The RID of the cnf controller.
	 */
	public jprime.variable.IntegerVariable getControllerRid() {
		return (jprime.variable.IntegerVariable)((INet)deference()).getControllerRid();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setControllerRid(String value) {
		((INet)deference()).setControllerRid(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setControllerRid(long value) {
		((INet)deference()).setControllerRid(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setControllerRid(jprime.variable.SymbolVariable value) {
		((INet)deference()).setControllerRid(value);
	}

	/**
	 * @return A comma separated list of its child sphere's edge iface and owning_host rank pairs
	 */
	public jprime.variable.OpaqueVariable getSubEdgeIfaces() {
		return (jprime.variable.OpaqueVariable)((INet)deference()).getSubEdgeIfaces();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSubEdgeIfaces(String value) {
		((INet)deference()).setSubEdgeIfaces(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSubEdgeIfaces(jprime.variable.SymbolVariable value) {
		((INet)deference()).setSubEdgeIfaces(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Net.attrIds;
	}

	/**
	  * Create a new child of type jprime.Net.Net and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Net.INet createNet() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Net.Net, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Net.INet createNet(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Net.Net and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Net.INet createNet(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Net.Net.
	  */
	public void addNet(jprime.Net.Net kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Net.NetReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Net.INet createNetReplica(jprime.Net.INet to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Net.NetReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Net.INet replicateNet(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Net.NetReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Net.INet createNetReplica(String name, jprime.Net.INet to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return sub-networks defined within this network
	  */
	public jprime.util.ChildList<jprime.Net.INet> getSubnets() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.Host.Host and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Host.IHost createHost() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Host.Host, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Host.IHost createHost(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Host.Host and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Host.IHost createHost(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Host.Host.
	  */
	public void addHost(jprime.Host.Host kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Host.HostReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Host.IHost createHostReplica(jprime.Host.IHost to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Host.HostReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Host.IHost replicateHost(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Host.HostReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Host.IHost createHostReplica(String name, jprime.Host.IHost to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return hosts defined within this network
	  */
	public jprime.util.ChildList<jprime.Host.IHost> getHosts() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.Router.Router and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Router.Router, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Router.Router and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Router.Router.
	  */
	public void addRouter(jprime.Router.Router kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Router.RouterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouterReplica(jprime.Router.IRouter to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Router.RouterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Router.IRouter replicateRouter(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Router.RouterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouterReplica(String name, jprime.Router.IRouter to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.Link.Link and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Link.ILink createLink() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Link.Link, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Link.ILink createLink(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Link.Link and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Link.ILink createLink(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Link.Link.
	  */
	public void addLink(jprime.Link.Link kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Link.LinkReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Link.ILink createLinkReplica(jprime.Link.ILink to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Link.LinkReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Link.ILink replicateLink(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Link.LinkReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Link.ILink createLinkReplica(String name, jprime.Link.ILink to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return links defined within this network
	  */
	public jprime.util.ChildList<jprime.Link.ILink> getLinks() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolder and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.PlaceHolder.PlaceHolder, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolder and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.PlaceHolder.PlaceHolder.
	  */
	public void addPlaceHolder(jprime.PlaceHolder.PlaceHolder kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolderReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolderReplica(jprime.PlaceHolder.IPlaceHolder to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.PlaceHolder.PlaceHolderReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder replicatePlaceHolder(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolderReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolderReplica(String name, jprime.PlaceHolder.IPlaceHolder to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return a node to hold the place of children which exist on remote partitions
	  */
	public jprime.util.ChildList<jprime.PlaceHolder.IPlaceHolder> getPlaceholders() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphere and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.RoutingSphere.RoutingSphere, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphere and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.RoutingSphere.RoutingSphere.
	  */
	public void addRoutingSphere(jprime.RoutingSphere.RoutingSphere kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphereReplica(jprime.RoutingSphere.IRoutingSphere to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.RoutingSphere.RoutingSphereReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere replicateRoutingSphere(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphereReplica(String name, jprime.RoutingSphere.IRoutingSphere to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return routing sphere defined within this network
	  */
	public jprime.util.ChildList<jprime.RoutingSphere.IRoutingSphere> getRsphere() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere.
	  */
	public void addGhostRoutingSphere(jprime.GhostRoutingSphere.GhostRoutingSphere kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphereReplica(jprime.GhostRoutingSphere.IGhostRoutingSphere to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere replicateGhostRoutingSphere(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphereReplica(String name, jprime.GhostRoutingSphere.IGhostRoutingSphere to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.Monitor.Monitor and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Monitor.Monitor, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Monitor.Monitor and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Monitor.Monitor.
	  */
	public void addMonitor(jprime.Monitor.Monitor kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Monitor.MonitorReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitorReplica(jprime.Monitor.IMonitor to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Monitor.MonitorReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor replicateMonitor(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Monitor.MonitorReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitorReplica(String name, jprime.Monitor.IMonitor to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return data monitors
	  */
	public jprime.util.ChildList<jprime.Monitor.IMonitor> getMonitors() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.Aggregate.Aggregate and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Aggregate.Aggregate, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Aggregate.Aggregate and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Aggregate.Aggregate.
	  */
	public void addAggregate(jprime.Aggregate.Aggregate kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Aggregate.AggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregateReplica(jprime.Aggregate.IAggregate to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Aggregate.AggregateReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate replicateAggregate(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Aggregate.AggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregateReplica(String name, jprime.Aggregate.IAggregate to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return aggregate statistics
	  */
	public jprime.util.ChildList<jprime.Aggregate.IAggregate> getAggregates() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.VizAggregate.VizAggregate and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.VizAggregate.VizAggregate, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregate and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.VizAggregate.VizAggregate.
	  */
	public void addVizAggregate(jprime.VizAggregate.VizAggregate kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregateReplica(jprime.VizAggregate.IVizAggregate to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.VizAggregate.VizAggregateReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate replicateVizAggregate(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregateReplica(String name, jprime.VizAggregate.IVizAggregate to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.Traffic.Traffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Traffic.Traffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Traffic.Traffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Traffic.Traffic.
	  */
	public void addTraffic(jprime.Traffic.Traffic kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Traffic.TrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTrafficReplica(jprime.Traffic.ITraffic to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Traffic.TrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic replicateTraffic(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Traffic.TrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTrafficReplica(String name, jprime.Traffic.ITraffic to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return traffic defined within this network
	  */
	public jprime.util.ChildList<jprime.Traffic.ITraffic> getTraffics() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
