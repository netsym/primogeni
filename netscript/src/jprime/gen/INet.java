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
public interface INet extends jprime.IModelNode {

	/**
	 * @return The n in W.X.Y.Z/n
	 */
	public jprime.variable.IntegerVariable getIpPrefixLen();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefixLen(jprime.variable.SymbolVariable value);

	/**
	 * @return ip prefix of this network
	 */
	public jprime.variable.OpaqueVariable getIpPrefix();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefix(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefix(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of content id and uid. i.e. [1,100,2,200] --> [ cid:1, uid:100],[ cid:2,uid:200]
	 */
	public jprime.variable.OpaqueVariable getCnfContentIds();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCnfContentIds(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCnfContentIds(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of rules in the form [ip_prefix->(router_uid,iface_uid,iface_ip)]. They will automatically be compiled into the topnet.
	 */
	public jprime.variable.OpaqueVariable getPortalRules();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPortalRules(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPortalRules(jprime.variable.SymbolVariable value);

	/**
	 * @return The RID of the cnf controller.
	 */
	public jprime.variable.IntegerVariable getControllerRid();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setControllerRid(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setControllerRid(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setControllerRid(jprime.variable.SymbolVariable value);

	/**
	 * @return A comma separated list of its child sphere's edge iface and owning_host rank pairs
	 */
	public jprime.variable.OpaqueVariable getSubEdgeIfaces();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSubEdgeIfaces(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSubEdgeIfaces(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();

	/**
	  * Create a new child of type jprime.Net.Net and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Net.INet createNet();

	/**
	  * jython method to create a a new child of type jprime.Net.Net, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Net.INet createNet(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Net.Net and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Net.INet createNet(String name);

	 /**
	  * Add a new child of type jprime.Net.Net.
	  */
	public void addNet(jprime.Net.Net kid);

	 /**
	  * Create a new child of type jprime.Net.NetReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Net.INet createNetReplica(jprime.Net.INet to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Net.NetReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Net.INet replicateNet(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Net.NetReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Net.INet createNetReplica(String name, jprime.Net.INet to_replicate);

	/**
	  * return sub-networks defined within this network
	  */
	public jprime.util.ChildList<jprime.Net.INet> getSubnets();

	/**
	  * Create a new child of type jprime.Host.Host and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Host.IHost createHost();

	/**
	  * jython method to create a a new child of type jprime.Host.Host, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Host.IHost createHost(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Host.Host and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Host.IHost createHost(String name);

	 /**
	  * Add a new child of type jprime.Host.Host.
	  */
	public void addHost(jprime.Host.Host kid);

	 /**
	  * Create a new child of type jprime.Host.HostReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Host.IHost createHostReplica(jprime.Host.IHost to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Host.HostReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Host.IHost replicateHost(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Host.HostReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Host.IHost createHostReplica(String name, jprime.Host.IHost to_replicate);

	/**
	  * return hosts defined within this network
	  */
	public jprime.util.ChildList<jprime.Host.IHost> getHosts();

	/**
	  * Create a new child of type jprime.Router.Router and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter();

	/**
	  * jython method to create a a new child of type jprime.Router.Router, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Router.Router and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter(String name);

	 /**
	  * Add a new child of type jprime.Router.Router.
	  */
	public void addRouter(jprime.Router.Router kid);

	 /**
	  * Create a new child of type jprime.Router.RouterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouterReplica(jprime.Router.IRouter to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Router.RouterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Router.IRouter replicateRouter(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Router.RouterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouterReplica(String name, jprime.Router.IRouter to_replicate);

	/**
	  * Create a new child of type jprime.Link.Link and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Link.ILink createLink();

	/**
	  * jython method to create a a new child of type jprime.Link.Link, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Link.ILink createLink(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Link.Link and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Link.ILink createLink(String name);

	 /**
	  * Add a new child of type jprime.Link.Link.
	  */
	public void addLink(jprime.Link.Link kid);

	 /**
	  * Create a new child of type jprime.Link.LinkReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Link.ILink createLinkReplica(jprime.Link.ILink to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Link.LinkReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Link.ILink replicateLink(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Link.LinkReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Link.ILink createLinkReplica(String name, jprime.Link.ILink to_replicate);

	/**
	  * return links defined within this network
	  */
	public jprime.util.ChildList<jprime.Link.ILink> getLinks();

	/**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolder and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder();

	/**
	  * jython method to create a a new child of type jprime.PlaceHolder.PlaceHolder, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolder and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder(String name);

	 /**
	  * Add a new child of type jprime.PlaceHolder.PlaceHolder.
	  */
	public void addPlaceHolder(jprime.PlaceHolder.PlaceHolder kid);

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolderReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolderReplica(jprime.PlaceHolder.IPlaceHolder to_replicate);

	/**
	  * jython method to create replica new child of type jprime.PlaceHolder.PlaceHolderReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder replicatePlaceHolder(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolderReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolderReplica(String name, jprime.PlaceHolder.IPlaceHolder to_replicate);

	/**
	  * return a node to hold the place of children which exist on remote partitions
	  */
	public jprime.util.ChildList<jprime.PlaceHolder.IPlaceHolder> getPlaceholders();

	/**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphere and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere();

	/**
	  * jython method to create a a new child of type jprime.RoutingSphere.RoutingSphere, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphere and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere(String name);

	 /**
	  * Add a new child of type jprime.RoutingSphere.RoutingSphere.
	  */
	public void addRoutingSphere(jprime.RoutingSphere.RoutingSphere kid);

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphereReplica(jprime.RoutingSphere.IRoutingSphere to_replicate);

	/**
	  * jython method to create replica new child of type jprime.RoutingSphere.RoutingSphereReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere replicateRoutingSphere(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphereReplica(String name, jprime.RoutingSphere.IRoutingSphere to_replicate);

	/**
	  * return routing sphere defined within this network
	  */
	public jprime.util.ChildList<jprime.RoutingSphere.IRoutingSphere> getRsphere();

	/**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere();

	/**
	  * jython method to create a a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere(String name);

	 /**
	  * Add a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere.
	  */
	public void addGhostRoutingSphere(jprime.GhostRoutingSphere.GhostRoutingSphere kid);

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphereReplica(jprime.GhostRoutingSphere.IGhostRoutingSphere to_replicate);

	/**
	  * jython method to create replica new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere replicateGhostRoutingSphere(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphereReplica(String name, jprime.GhostRoutingSphere.IGhostRoutingSphere to_replicate);

	/**
	  * Create a new child of type jprime.Monitor.Monitor and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor();

	/**
	  * jython method to create a a new child of type jprime.Monitor.Monitor, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Monitor.Monitor and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor(String name);

	 /**
	  * Add a new child of type jprime.Monitor.Monitor.
	  */
	public void addMonitor(jprime.Monitor.Monitor kid);

	 /**
	  * Create a new child of type jprime.Monitor.MonitorReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitorReplica(jprime.Monitor.IMonitor to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Monitor.MonitorReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor replicateMonitor(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Monitor.MonitorReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitorReplica(String name, jprime.Monitor.IMonitor to_replicate);

	/**
	  * return data monitors
	  */
	public jprime.util.ChildList<jprime.Monitor.IMonitor> getMonitors();

	/**
	  * Create a new child of type jprime.Aggregate.Aggregate and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate();

	/**
	  * jython method to create a a new child of type jprime.Aggregate.Aggregate, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Aggregate.Aggregate and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate(String name);

	 /**
	  * Add a new child of type jprime.Aggregate.Aggregate.
	  */
	public void addAggregate(jprime.Aggregate.Aggregate kid);

	 /**
	  * Create a new child of type jprime.Aggregate.AggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregateReplica(jprime.Aggregate.IAggregate to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Aggregate.AggregateReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate replicateAggregate(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Aggregate.AggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregateReplica(String name, jprime.Aggregate.IAggregate to_replicate);

	/**
	  * return aggregate statistics
	  */
	public jprime.util.ChildList<jprime.Aggregate.IAggregate> getAggregates();

	/**
	  * Create a new child of type jprime.VizAggregate.VizAggregate and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate();

	/**
	  * jython method to create a a new child of type jprime.VizAggregate.VizAggregate, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregate and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate(String name);

	 /**
	  * Add a new child of type jprime.VizAggregate.VizAggregate.
	  */
	public void addVizAggregate(jprime.VizAggregate.VizAggregate kid);

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregateReplica(jprime.VizAggregate.IVizAggregate to_replicate);

	/**
	  * jython method to create replica new child of type jprime.VizAggregate.VizAggregateReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate replicateVizAggregate(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregateReplica(String name, jprime.VizAggregate.IVizAggregate to_replicate);

	/**
	  * Create a new child of type jprime.Traffic.Traffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic();

	/**
	  * jython method to create a a new child of type jprime.Traffic.Traffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Traffic.Traffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic(String name);

	 /**
	  * Add a new child of type jprime.Traffic.Traffic.
	  */
	public void addTraffic(jprime.Traffic.Traffic kid);

	 /**
	  * Create a new child of type jprime.Traffic.TrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTrafficReplica(jprime.Traffic.ITraffic to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Traffic.TrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic replicateTraffic(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Traffic.TrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTrafficReplica(String name, jprime.Traffic.ITraffic to_replicate);

	/**
	  * return traffic defined within this network
	  */
	public jprime.util.ChildList<jprime.Traffic.ITraffic> getTraffics();
}
