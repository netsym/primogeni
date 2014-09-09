package slingshot.visualization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject.PersistableState;
import jprime.Aggregate.Aggregate;
import jprime.Aggregate.IAggregate;
import jprime.GhostRoutingSphere.GhostRoutingSphere;
import jprime.GhostRoutingSphere.IGhostRoutingSphere;
import jprime.Host.Host;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Link.Link;
import jprime.Monitor.IMonitor;
import jprime.Monitor.Monitor;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.PlaceHolder.IPlaceHolder;
import jprime.PlaceHolder.PlaceHolder;
import jprime.ResourceIdentifier.EvalutedResourceID;
import jprime.ResourceIdentifier.ResourceID;
import jprime.Router.IRouter;
import jprime.Router.Router;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.RoutingSphere.RoutingSphere;
import jprime.Traffic.ITraffic;
import jprime.Traffic.Traffic;
import jprime.VizAggregate.IVizAggregate;
import jprime.VizAggregate.VizAggregate;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.routing.AlgorithmicRouting;
import jprime.routing.BGP;
import jprime.routing.IRouteVisitor;
import jprime.routing.RouteCalculationVisitor;
import jprime.routing.ShortestPath;
import jprime.routing.StaticRoutingProtocol;
import jprime.util.ChildList;
import jprime.util.PersistentChildList;
import jprime.util.UniqueName;
import jprime.variable.Dataset;
import jprime.variable.Dataset.SimpleDatum;
import jprime.variable.IntegerVariable;
import jprime.variable.ModelNodeVariable;
import jprime.variable.ModelNodeVariable.VizAttr;
import jprime.variable.OpaqueVariable;
import jprime.variable.SymbolVariable;
import jprime.visitors.IGenericVisitor;
import jprime.visitors.IPAddressAssignment;
import jprime.visitors.TLVVisitor;
import jprime.visitors.UIDAssignmentVisitor;
import jprime.visitors.VerifyVisitor;
import jprime.visitors.XMLVisitor;

import org.python.core.PyObject;

public class WrappedNet implements INet{
	public final INet wrapped_net;
	public final boolean expanded;
	private IVizAggregate viz=null;
	public WrappedNet(INet net, boolean expanded) {
		this.wrapped_net=net;
		this.expanded=expanded;
		net.setExtraVizInfo(this);
	}

	public SimpleDatum getTrafficIntensity(Dataset d) {
		if(viz==null) {
			for(IAggregate a : wrapped_net.getAggregates()) {
				if(a instanceof IVizAggregate) {
					viz=(IVizAggregate)a;
				}
			}
		}
		if(viz!=null) {
			return viz.getRuntimeValueByName(ModelNodeVariable.max(), d);
		}
		return null;
	}
	public void handleExpandCollapse(final NetworkView view) {
		if(wrapped_net.getParent()==null) {
			wrapped_net.setExtraVizInfo(null);
			return;
		}
		if(expanded) {
			collapse(view);
		}
		else {
			expand(view);
		}
	}

	private void expand(final NetworkView view) {
		try {
			view.expandedNets.put(wrapped_net.getDBID(),true);
			view.cancel("force");
			view.pyexp.startVisualization();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private void collapse( NetworkView view, IModelNode n) {
		for(IModelNode c : n.getAllChildren()) {
			if(c.getRow() != -1) {
				if(c instanceof INet) {
					collapse(view,(INet)c);
				}
			}
		}
		view.expandedNets.put(wrapped_net.getDBID(),false);
	}

	private void collapse(final NetworkView view) {
		try {
			collapse(view, wrapped_net);
			view.cancel("force");
			view.pyexp.startVisualization();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * @return
	 * @see jprime.gen.INet#getIpPrefixLen()
	 */
	public IntegerVariable getIpPrefixLen() {
		return wrapped_net.getIpPrefixLen();
	}

	/**
	 * @param value
	 * @see jprime.gen.INet#setIpPrefixLen(java.lang.String)
	 */
	public void setIpPrefixLen(String value) {
		wrapped_net.setIpPrefixLen(value);
	}

	/**
	 * @param value
	 * @see jprime.gen.INet#setIpPrefixLen(long)
	 */
	public void setIpPrefixLen(long value) {
		wrapped_net.setIpPrefixLen(value);
	}

	/**
	 * @param value
	 * @see jprime.gen.INet#setIpPrefixLen(jprime.variable.SymbolVariable)
	 */
	public void setIpPrefixLen(SymbolVariable value) {
		wrapped_net.setIpPrefixLen(value);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getIpPrefix()
	 */
	public OpaqueVariable getIpPrefix() {
		return wrapped_net.getIpPrefix();
	}

	/**
	 * @param value
	 * @see jprime.gen.INet#setIpPrefix(java.lang.String)
	 */
	public void setIpPrefix(String value) {
		wrapped_net.setIpPrefix(value);
	}

	/**
	 * @param value
	 * @see jprime.gen.INet#setIpPrefix(jprime.variable.SymbolVariable)
	 */
	public void setIpPrefix(SymbolVariable value) {
		wrapped_net.setIpPrefix(value);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getSubEdgeIfaces()
	 */
	public OpaqueVariable getSubEdgeIfaces() {
		return wrapped_net.getSubEdgeIfaces();
	}

	/**
	 * @param value
	 * @see jprime.gen.INet#setSubEdgeIfaces(java.lang.String)
	 */
	public void setSubEdgeIfaces(String value) {
		wrapped_net.setSubEdgeIfaces(value);
	}

	/**
	 * @param p
	 * @param alignment
	 * @see jprime.partitioning.IAlignedNode#setAlignment(jprime.partitioning.Partitioning, int)
	 */
	public void setAlignment(Partitioning p, int alignment) {
		wrapped_net.setAlignment(p, alignment);
	}

	/**
	 * @param value
	 * @see jprime.gen.INet#setSubEdgeIfaces(jprime.variable.SymbolVariable)
	 */
	public void setSubEdgeIfaces(SymbolVariable value) {
		wrapped_net.setSubEdgeIfaces(value);
	}

	/**
	 * 
	 * @see jprime.partitioning.IAlignedNode#resetAlignments()
	 */
	public void resetAlignments() {
		wrapped_net.resetAlignments();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getAttrIds()
	 */
	public ArrayList<Integer> getAttrIds() {
		return wrapped_net.getAttrIds();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createNet()
	 */
	public INet createNet() {
		return wrapped_net.createNet();
	}

	/**
	 * @param o
	 * @see jprime.Net.INet#setExtraVizInfo(java.lang.Object)
	 */
	public void setExtraVizInfo(Object o) {
		wrapped_net.setExtraVizInfo(o);
	}

	/**
	 * @return
	 * @see jprime.Net.INet#getExtraVizInfo()
	 */
	public Object getExtraVizInfo() {
		return wrapped_net.getExtraVizInfo();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getDBID()
	 */
	public long getDBID() {
		return wrapped_net.getDBID();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newNet(org.python.core.PyObject[], java.lang.String[])
	 */
	public INet createNet(PyObject[] v, String[] n) {
		return wrapped_net.createNet(v, n);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getParentId()
	 */
	public long getParentId() {
		return wrapped_net.getParentId();
	}

	/**
	 * @return
	 * @see jprime.Net.INet#getRoutingSphere()
	 */
	public IRoutingSphere getRoutingSphere() {
		return wrapped_net.getRoutingSphere();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getUID()
	 */
	public long getUID() {
		return wrapped_net.getUID();
	}

	/**
	 * @return
	 * @see jprime.Net.INet#getStaticRoutingProtocol()
	 */
	public StaticRoutingProtocol getStaticRoutingProtocol() {
		return wrapped_net.getStaticRoutingProtocol();
	}

	/**
	 * @param id
	 * @see jprime.IModelNode#setUID(long)
	 */
	public void setUID(long id) {
		wrapped_net.setUID(id);
	}

	/**
	 * @param p
	 * @see jprime.Net.INet#setStaticRoutingProtocol(jprime.routing.StaticRoutingProtocol)
	 */
	public void setStaticRoutingProtocol(StaticRoutingProtocol p) {
		wrapped_net.setStaticRoutingProtocol(p);
	}

	/**
	 * @param anchor
	 * @return
	 * @see jprime.IModelNode#getRank(jprime.IModelNode)
	 */
	public long getRank(IModelNode anchor) {
		return wrapped_net.getRank(anchor);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createNet(java.lang.String)
	 */
	public INet createNet(String name) {
		return wrapped_net.createNet(name);
	}

	/**
	 * @return
	 * @see jprime.Net.INet#createBGP()
	 */
	public BGP createBGP() {
		return wrapped_net.createBGP();
	}

	/**
	 * @param anchor
	 * @return
	 * @see jprime.IModelNode#getMinRank(jprime.IModelNode)
	 */
	public long getMinRank(IModelNode anchor) {
		return wrapped_net.getMinRank(anchor);
	}

	/**
	 * @return
	 * @see jprime.Net.INet#createShortestPath()
	 */
	public ShortestPath createShortestPath() {
		return wrapped_net.createShortestPath();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getMinUID()
	 */
	public long getMinUID() {
		return wrapped_net.getMinUID();
	}

	/**
	 * @return
	 * @see jprime.Net.INet#createAlgorithmicRouting()
	 */
	public AlgorithmicRouting createAlgorithmicRouting() {
		return wrapped_net.createAlgorithmicRouting();
	}

	/**
	 * @param anchor
	 * @return
	 * @see jprime.IModelNode#getMaxRank(jprime.IModelNode)
	 */
	public long getMaxRank(IModelNode anchor) {
		return wrapped_net.getMaxRank(anchor);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addNet(jprime.Net.Net)
	 */
	public void addNet(Net kid) {
		wrapped_net.addNet(kid);
	}

	/**
	 * @return
	 * @see jprime.Net.INet#isRoutingSphere()
	 */
	public boolean isRoutingSphere() {
		return wrapped_net.isRoutingSphere();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#hasBeenReplicated()
	 */
	public boolean hasBeenReplicated() {
		return wrapped_net.hasBeenReplicated();
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createNetReplica(jprime.Net.INet)
	 */
	public INet createNetReplica(INet to_replicate) {
		return wrapped_net.createNetReplica(to_replicate);
	}

	/**
	 * @return
	 * @see jprime.Net.INet#haveSubSpheres()
	 */
	public boolean haveSubSpheres() {
		return wrapped_net.haveSubSpheres();
	}

	/**
	 * @param anchor
	 * @param rank
	 * @return
	 * @see jprime.IModelNode#containsRank(jprime.IModelNode, long)
	 */
	public boolean containsRank(IModelNode anchor, long rank) {
		return wrapped_net.containsRank(anchor, rank);
	}

	/**
	 * @param iface
	 * @return
	 * @see jprime.Net.INet#isEdgeIfaceOfChildSphere(jprime.Interface.IInterface)
	 */
	public boolean isEdgeIfaceOfChildSphere(IInterface iface) {
		return wrapped_net.isEdgeIfaceOfChildSphere(iface);
	}

	/**
	 * @param name
	 * @param parent
	 * @return
	 * @see jprime.IModelNode#copy(java.lang.String, jprime.IModelNode)
	 */
	public IModelNode copy(String name, IModelNode parent) {
		return wrapped_net.copy(name, parent);
	}

	/**
	 * @param iface
	 * @param host
	 * @return
	 * @see jprime.Net.INet#isEdgeIfaceOfChildSphere(long, long)
	 */
	public boolean isEdgeIfaceOfChildSphere(long iface, long host) {
		return wrapped_net.isEdgeIfaceOfChildSphere(iface, host);
	}

	/**
	 * @param uid
	 * @return
	 * @see jprime.IModelNode#containsUID(long)
	 */
	public boolean containsUID(long uid) {
		return wrapped_net.containsUID(uid);
	}

	/**
	 * @return
	 * @see jprime.Net.INet#getSubEdgeInterfaces()
	 */
	public List<SubEdgeInterfaceHostPair> getSubEdgeInterfaces() {
		return wrapped_net.getSubEdgeInterfaces();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicateNet(org.python.core.PyObject[], java.lang.String[])
	 */
	public INet replicateNet(PyObject[] v, String[] n) {
		return wrapped_net.replicateNet(v, n);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getName()
	 */
	public String getName() {
		return wrapped_net.getName();
	}

	/**
	 * @param sub_edge_ifaces
	 * @see jprime.Net.INet#setSubEdgeInterfaces(java.util.List)
	 */
	public void setSubEdgeInterfaces(
			List<SubEdgeInterfaceHostPair> sub_edge_ifaces) {
		wrapped_net.setSubEdgeInterfaces(sub_edge_ifaces);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getSize()
	 */
	public long getSize() {
		return wrapped_net.getSize();
	}

	/**
	 * @param s
	 * @see jprime.IModelNode#setSize(long)
	 */
	public void setSize(long s) {
		wrapped_net.setSize(s);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getOffset()
	 */
	public long getOffset() {
		return wrapped_net.getOffset();
	}

	/**
	 * @param o
	 * @see jprime.IModelNode#setOffset(long)
	 */
	public void setOffset(long o) {
		wrapped_net.setOffset(o);
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createNetReplica(java.lang.String, jprime.Net.INet)
	 */
	public INet createNetReplica(String name, INet to_replicate) {
		return wrapped_net.createNetReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getOrder()
	 */
	public int getOrder() {
		return wrapped_net.getOrder();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId() {
		return wrapped_net.getTypeId();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#deference()
	 */
	public IModelNode deference() {
		return wrapped_net.deference();
	}

	/**
	 * @param includeDefaultValues
	 * @return
	 * @see jprime.IModelNode#getAttributes(boolean)
	 */
	public Map<String, String> getAttributes(boolean includeDefaultValues) {
		return wrapped_net.getAttributes(includeDefaultValues);
	}

	/**
	 * @param d
	 * @return
	 * @see jprime.IModelNode#getVizAttributes(jprime.variable.Dataset)
	 */
	public Map<String, VizAttr> getVizAttributes(Dataset d) {
		return wrapped_net.getVizAttributes(d);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getSubnets()
	 */
	public ChildList<INet> getSubnets() {
		return wrapped_net.getSubnets();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getAttributes()
	 */
	public Map<String, String> getAttributes() {
		return wrapped_net.getAttributes();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createHost()
	 */
	public IHost createHost() {
		return wrapped_net.createHost();
	}

	/**
	 * @param tlv_encode
	 * @return
	 * @see jprime.IModelNode#getAttributeValues(boolean)
	 */
	
	public Collection<ModelNodeVariable> getAttributeValues() {
		return wrapped_net.getAllAttributeValues();
	}
	
	public Collection<ModelNodeVariable> getAllAttributeValues() {
		return wrapped_net.getAllAttributeValues();
	}
	
	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newHost(org.python.core.PyObject[], java.lang.String[])
	 */
	public IHost createHost(PyObject[] v, String[] n) {
		return wrapped_net.createHost(v, n);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.IModelNode#getAttributeByName(java.lang.String)
	 */
	public ModelNodeVariable getAttributeByName(String name) {
		return wrapped_net.getAttributeByName(name);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.IModelNode#getAttributeByName(int)
	 */
	public ModelNodeVariable getAttributeByName(int name) {
		return wrapped_net.getAttributeByName(name);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createHost(java.lang.String)
	 */
	public IHost createHost(String name) {
		return wrapped_net.createHost(name);
	}

	/**
	 * @param varId
	 * @param value
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @see jprime.IModelNode#setAttribute(int, java.lang.String)
	 */
	public ModelNodeVariable setAttribute(int varId, String value)
	throws InstantiationException, IllegalAccessException {
		return wrapped_net.setAttribute(varId, value);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addHost(jprime.Host.Host)
	 */
	public void addHost(Host kid) {
		wrapped_net.addHost(kid);
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createHostReplica(jprime.Host.IHost)
	 */
	public IHost createHostReplica(IHost to_replicate) {
		return wrapped_net.createHostReplica(to_replicate);
	}

	/**
	 * @param name
	 * @param ds
	 * @return
	 * @see jprime.IModelNode#getRuntimeValueByName(int, jprime.variable.Dataset)
	 */
	public SimpleDatum getRuntimeValueByName(int name, Dataset ds) {
		return wrapped_net.getRuntimeValueByName(name, ds);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getAllChildren()
	 */
	public List<ModelNode> getAllChildren() {
		return wrapped_net.getAllChildren();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getPersistentChildList()
	 */
	public PersistentChildList getPersistentChildList() {
		return wrapped_net.getPersistentChildList();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicateHost(org.python.core.PyObject[], java.lang.String[])
	 */
	public IHost replicateHost(PyObject[] v, String[] n) {
		return wrapped_net.replicateHost(v, n);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getMetadata()
	 */
	public Metadata getMetadata() {
		return wrapped_net.getMetadata();
	}

	/**
	 * @param p
	 * @return
	 * @see jprime.IModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		return wrapped_net.getAlignments(p);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getParent_nofetch()
	 */
	public IModelNode getParent_nofetch() {
		return wrapped_net.getParent_nofetch();
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createHostReplica(java.lang.String, jprime.Host.IHost)
	 */
	public IHost createHostReplica(String name, IHost to_replicate) {
		return wrapped_net.createHostReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getParent()
	 */
	public IModelNode getParent() {
		return wrapped_net.getParent();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getUniqueName()
	 */
	public UniqueName getUniqueName() {
		return wrapped_net.getUniqueName();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getNumberOfChildren()
	 */
	public int getNumberOfChildren() {
		return wrapped_net.getNumberOfChildren();
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.IModelNode#getChildByName(java.lang.String)
	 */
	public IModelNode getChildByName(String name) {
		return wrapped_net.getChildByName(name);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getHosts()
	 */
	public ChildList<IHost> getHosts() {
		return wrapped_net.getHosts();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#isReplica()
	 */
	public boolean isReplica() {
		return wrapped_net.isReplica();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#isAlias()
	 */
	public boolean isAlias() {
		return wrapped_net.isAlias();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createRouter()
	 */
	public IRouter createRouter() {
		return wrapped_net.createRouter();
	}

	/**
	 * 
	 * @see jprime.IModelNode#enforceMinimumChildConstraints()
	 */
	public void enforceMinimumChildConstraints() {
		wrapped_net.enforceMinimumChildConstraints();
	}

	/**
	 * @param rank
	 * @param anchor
	 * @return
	 * @see jprime.IModelNode#getChildByRank(long, jprime.IModelNode)
	 */
	public IModelNode getChildByRank(long rank, IModelNode anchor) {
		return wrapped_net.getChildByRank(rank, anchor);
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newRouter(org.python.core.PyObject[], java.lang.String[])
	 */
	public IRouter createRouter(PyObject[] v, String[] n) {
		return wrapped_net.createRouter(v, n);
	}

	/**
	 * @param resourceid
	 * @param list_idx
	 * @param term_idx
	 * @param result
	 * @see jprime.IModelNode#evaluateResourceID(jprime.ResourceIdentifier.ResourceID, int, int, jprime.ResourceIdentifier.ResourceID.EvalutedResourceID)
	 */
	public void evaluateResourceID(ResourceID resourceid, int list_idx,
			int term_idx, EvalutedResourceID result) {
		wrapped_net.evaluateResourceID(resourceid, list_idx, term_idx, result);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createRouter(java.lang.String)
	 */
	public IRouter createRouter(String name) {
		return wrapped_net.createRouter(name);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(IGenericVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.visitors.IPAddressAssignment)
	 */
	public void accept(IPAddressAssignment visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.visitors.UIDAssignmentVisitor)
	 */
	public void accept(UIDAssignmentVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addRouter(jprime.Router.Router)
	 */
	public void addRouter(Router kid) {
		wrapped_net.addRouter(kid);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.visitors.TLVVisitor)
	 */
	public void accept(TLVVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createRouterReplica(jprime.Router.IRouter)
	 */
	public IRouter createRouterReplica(IRouter to_replicate) {
		return wrapped_net.createRouterReplica(to_replicate);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.routing.IRouteVisitor)
	 */
	public void accept(IRouteVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.VerifyVisitor)
	 */
	public void accept(VerifyVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.routing.RouteCalculationVisitor)
	 */
	public void accept(RouteCalculationVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.partitioning.PartitioningVisitor)
	 */
	public void accept(PartitioningVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param visitor
	 * @see jprime.IModelNode#accept(jprime.visitors.XMLVisitor)
	 */
	public void accept(XMLVisitor visitor) {
		wrapped_net.accept(visitor);
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicateRouter(org.python.core.PyObject[], java.lang.String[])
	 */
	public IRouter replicateRouter(PyObject[] v, String[] n) {
		return wrapped_net.replicateRouter(v, n);
	}

	/**
	 * @param attr
	 * @see jprime.IModelNode#addAttr(jprime.variable.ModelNodeVariable)
	 */
	public void addAttr(ModelNodeVariable attr) {
		wrapped_net.addAttr(attr);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getPersistableState()
	 */
	public PersistableState getPersistableState() {
		return wrapped_net.getPersistableState();
	}

	/**
	 * @param name
	 * @param toCopy
	 * @param parent
	 * @return
	 * @see jprime.IModelNode#__copy(java.lang.String, jprime.IModelNode, jprime.IModelNode)
	 */
	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent) {
		return wrapped_net.__copy(name, toCopy, parent);
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createRouterReplica(java.lang.String, jprime.Router.IRouter)
	 */
	public IRouter createRouterReplica(String name, IRouter to_replicate) {
		return wrapped_net.createRouterReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getNodeType()
	 */
	public Class<?> getNodeType() {
		return wrapped_net.getNodeType();
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getRow()
	 */
	public int getRow() {
		return wrapped_net.getRow();
	}

	/**
	 * @param row
	 * @see jprime.IModelNode#setRow(int)
	 */
	public void setRow(int row) {
		wrapped_net.setRow(row);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#getHub()
	 */
	public boolean getHub() {
		return wrapped_net.getHub();
	}

	/**
	 * @param h
	 * @see jprime.IModelNode#setHub(boolean)
	 */
	public void setHub(boolean h) {
		wrapped_net.setHub(h);
	}

	/**
	 * @param query
	 * @return
	 * @see jprime.IModelNode#getChildByQuery(java.lang.String)
	 */
	public IModelNode getChildByQuery(String query) {
		return wrapped_net.getChildByQuery(query);
	}

	/**
	 * @param query
	 * @return
	 * @see jprime.IModelNode#get(java.lang.String)
	 */
	public IModelNode get(String query) {
		return wrapped_net.get(query);
	}

	/**
	 * @return
	 * @see jprime.IModelNode#help()
	 */
	public List<String> help() {
		return wrapped_net.help();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createLink()
	 */
	public ILink createLink() {
		return wrapped_net.createLink();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newLink(org.python.core.PyObject[], java.lang.String[])
	 */
	public ILink createLink(PyObject[] v, String[] n) {
		return wrapped_net.createLink(v, n);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createLink(java.lang.String)
	 */
	public ILink createLink(String name) {
		return wrapped_net.createLink(name);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addLink(jprime.Link.Link)
	 */
	public void addLink(Link kid) {
		wrapped_net.addLink(kid);
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createLinkReplica(jprime.Link.ILink)
	 */
	public ILink createLinkReplica(ILink to_replicate) {
		return wrapped_net.createLinkReplica(to_replicate);
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicateLink(org.python.core.PyObject[], java.lang.String[])
	 */
	public ILink replicateLink(PyObject[] v, String[] n) {
		return wrapped_net.replicateLink(v, n);
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createLinkReplica(java.lang.String, jprime.Link.ILink)
	 */
	public ILink createLinkReplica(String name, ILink to_replicate) {
		return wrapped_net.createLinkReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getLinks()
	 */
	public ChildList<ILink> getLinks() {
		return wrapped_net.getLinks();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createPlaceHolder()
	 */
	public IPlaceHolder createPlaceHolder() {
		return wrapped_net.createPlaceHolder();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newPlaceHolder(org.python.core.PyObject[], java.lang.String[])
	 */
	public IPlaceHolder createPlaceHolder(PyObject[] v, String[] n) {
		return wrapped_net.createPlaceHolder(v, n);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createPlaceHolder(java.lang.String)
	 */
	public IPlaceHolder createPlaceHolder(String name) {
		return wrapped_net.createPlaceHolder(name);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addPlaceHolder(jprime.PlaceHolder.PlaceHolder)
	 */
	public void addPlaceHolder(PlaceHolder kid) {
		wrapped_net.addPlaceHolder(kid);
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createPlaceHolderReplica(jprime.PlaceHolder.IPlaceHolder)
	 */
	public IPlaceHolder createPlaceHolderReplica(IPlaceHolder to_replicate) {
		return wrapped_net.createPlaceHolderReplica(to_replicate);
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicatePlaceHolder(org.python.core.PyObject[], java.lang.String[])
	 */
	public IPlaceHolder replicatePlaceHolder(PyObject[] v, String[] n) {
		return wrapped_net.replicatePlaceHolder(v, n);
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createPlaceHolderReplica(java.lang.String, jprime.PlaceHolder.IPlaceHolder)
	 */
	public IPlaceHolder createPlaceHolderReplica(String name,
			IPlaceHolder to_replicate) {
		return wrapped_net.createPlaceHolderReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getPlaceholders()
	 */
	public ChildList<IPlaceHolder> getPlaceholders() {
		return wrapped_net.getPlaceholders();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createRoutingSphere()
	 */
	public IRoutingSphere createRoutingSphere() {
		return wrapped_net.createRoutingSphere();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newRoutingSphere(org.python.core.PyObject[], java.lang.String[])
	 */
	public IRoutingSphere createRoutingSphere(PyObject[] v, String[] n) {
		return wrapped_net.createRoutingSphere(v, n);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createRoutingSphere(java.lang.String)
	 */
	public IRoutingSphere createRoutingSphere(String name) {
		return wrapped_net.createRoutingSphere(name);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addRoutingSphere(jprime.RoutingSphere.RoutingSphere)
	 */
	public void addRoutingSphere(RoutingSphere kid) {
		wrapped_net.addRoutingSphere(kid);
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createRoutingSphereReplica(jprime.RoutingSphere.IRoutingSphere)
	 */
	public IRoutingSphere createRoutingSphereReplica(IRoutingSphere to_replicate) {
		return wrapped_net.createRoutingSphereReplica(to_replicate);
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicateRoutingSphere(org.python.core.PyObject[], java.lang.String[])
	 */
	public IRoutingSphere replicateRoutingSphere(PyObject[] v, String[] n) {
		return wrapped_net.replicateRoutingSphere(v, n);
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createRoutingSphereReplica(java.lang.String, jprime.RoutingSphere.IRoutingSphere)
	 */
	public IRoutingSphere createRoutingSphereReplica(String name,
			IRoutingSphere to_replicate) {
		return wrapped_net.createRoutingSphereReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getRsphere()
	 */
	public ChildList<IRoutingSphere> getRsphere() {
		return wrapped_net.getRsphere();
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createGhostRoutingSphere()
	 */
	public IGhostRoutingSphere createGhostRoutingSphere() {
		return wrapped_net.createGhostRoutingSphere();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newGhostRoutingSphere(org.python.core.PyObject[], java.lang.String[])
	 */
	public IGhostRoutingSphere createGhostRoutingSphere(PyObject[] v, String[] n) {
		return wrapped_net.createGhostRoutingSphere(v, n);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createGhostRoutingSphere(java.lang.String)
	 */
	public IGhostRoutingSphere createGhostRoutingSphere(String name) {
		return wrapped_net.createGhostRoutingSphere(name);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addGhostRoutingSphere(jprime.GhostRoutingSphere.GhostRoutingSphere)
	 */
	public void addGhostRoutingSphere(GhostRoutingSphere kid) {
		wrapped_net.addGhostRoutingSphere(kid);
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createGhostRoutingSphereReplica(jprime.GhostRoutingSphere.IGhostRoutingSphere)
	 */
	public IGhostRoutingSphere createGhostRoutingSphereReplica(
			IGhostRoutingSphere to_replicate) {
		return wrapped_net.createGhostRoutingSphereReplica(to_replicate);
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicateGhostRoutingSphere(org.python.core.PyObject[], java.lang.String[])
	 */
	public IGhostRoutingSphere replicateGhostRoutingSphere(PyObject[] v,
			String[] n) {
		return wrapped_net.replicateGhostRoutingSphere(v, n);
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createGhostRoutingSphereReplica(java.lang.String, jprime.GhostRoutingSphere.IGhostRoutingSphere)
	 */
	public IGhostRoutingSphere createGhostRoutingSphereReplica(String name,
			IGhostRoutingSphere to_replicate) {
		return wrapped_net.createGhostRoutingSphereReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#createTraffic()
	 */
	public ITraffic createTraffic() {
		return wrapped_net.createTraffic();
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#newTraffic(org.python.core.PyObject[], java.lang.String[])
	 */
	public ITraffic createTraffic(PyObject[] v, String[] n) {
		return wrapped_net.createTraffic(v, n);
	}

	/**
	 * @param name
	 * @return
	 * @see jprime.gen.INet#createTraffic(java.lang.String)
	 */
	public ITraffic createTraffic(String name) {
		return wrapped_net.createTraffic(name);
	}

	/**
	 * @param kid
	 * @see jprime.gen.INet#addTraffic(jprime.Traffic.Traffic)
	 */
	public void addTraffic(Traffic kid) {
		wrapped_net.addTraffic(kid);
	}

	/**
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createTrafficReplica(jprime.Traffic.ITraffic)
	 */
	public ITraffic createTrafficReplica(ITraffic to_replicate) {
		return wrapped_net.createTrafficReplica(to_replicate);
	}

	/**
	 * @param v
	 * @param n
	 * @return
	 * @see jprime.gen.INet#replicateTraffic(org.python.core.PyObject[], java.lang.String[])
	 */
	public ITraffic replicateTraffic(PyObject[] v, String[] n) {
		return wrapped_net.replicateTraffic(v, n);
	}

	/**
	 * @param name
	 * @param to_replicate
	 * @return
	 * @see jprime.gen.INet#createTrafficReplica(java.lang.String, jprime.Traffic.ITraffic)
	 */
	public ITraffic createTrafficReplica(String name, ITraffic to_replicate) {
		return wrapped_net.createTrafficReplica(name, to_replicate);
	}

	/**
	 * @return
	 * @see jprime.gen.INet#getTraffics()
	 */
	public ChildList<ITraffic> getTraffics() {
		return wrapped_net.getTraffics();
	}

	@Override
	public OpaqueVariable getCnfContentIds() {
		return wrapped_net.getCnfContentIds();
	}

	@Override
	public void setCnfContentIds(String value) {
		wrapped_net.setCnfContentIds(value);
	}

	@Override
	public void setCnfContentIds(SymbolVariable value) {
		wrapped_net.setCnfContentIds(value);
	}

	@Override
	public IntegerVariable getControllerRid() {
		return wrapped_net.getControllerRid();
	}

	@Override
	public void setControllerRid(String value) {
		wrapped_net.setControllerRid(value);
	}

	@Override
	public void setControllerRid(long value) {
		wrapped_net.setControllerRid(value);
	}

	@Override
	public void setControllerRid(SymbolVariable value) {
		wrapped_net.setControllerRid(value);
	}

	@Override
	public HashMap<Integer, Long> getCNFContent2RIDMap() {
		return wrapped_net.getCNFContent2RIDMap();
	}


	/* (non-Javadoc)
	 * @see jprime.gen.INet#getPortalRules()
	 */
	@Override
	public OpaqueVariable getPortalRules() {
		return wrapped_net.getPortalRules();
	}

	/* (non-Javadoc)
	 * @see jprime.gen.INet#setPortalRules(java.lang.String)
	 */
	@Override
	public void setPortalRules(String value) {
		wrapped_net.setPortalRules(value);
	}

	/* (non-Javadoc)
	 * @see jprime.gen.INet#setPortalRules(jprime.variable.SymbolVariable)
	 */
	@Override
	public void setPortalRules(SymbolVariable value) {
		wrapped_net.setPortalRules(value);
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#addCNFContent(int, long)
	 */
	@Override
	public void addCNFContent(int content_id, long rid) {
		wrapped_net.addCNFContent(content_id, rid);
	}

	@Override
	public String getAttributeValueByName(int name) {
		return wrapped_net.getAttributeValueByName(name);
	}

	@Override
	public int compareTo(IModelNode arg0) {
		return wrapped_net.compareTo(arg0);
	}

	@Override
	public IInterface findInterfaceWithIp(String ip) {
		return wrapped_net.findInterfaceWithIp(ip);
	}

	@Override
	public IInterface findInterfaceWithIp(long ip) {
		return wrapped_net.findInterfaceWithIp(ip);
	}

	@Override
	public IModelNode findNodeByUID(long uid) {
		return wrapped_net.findNodeByUID(uid);
	}

	@Override
	public void delete() {
		wrapped_net.delete();
	}

	@Override
	public PrefuseLocation updateLocation(PrefuseLocation loc) {
		return wrapped_net.updateLocation(loc);
	}

	@Override
	public IMonitor createMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMonitor createMonitor(PyObject[] v, String[] n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMonitor createMonitor(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMonitor(Monitor kid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IMonitor createMonitorReplica(IMonitor to_replicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMonitor replicateMonitor(PyObject[] v, String[] n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMonitor createMonitorReplica(String name, IMonitor to_replicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChildList<IMonitor> getMonitors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAggregate createAggregate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAggregate createAggregate(PyObject[] v, String[] n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAggregate createAggregate(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAggregate(Aggregate kid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IAggregate createAggregateReplica(IAggregate to_replicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAggregate replicateAggregate(PyObject[] v, String[] n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAggregate createAggregateReplica(String name,
			IAggregate to_replicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChildList<IAggregate> getAggregates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVizAggregate createVizAggregate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVizAggregate createVizAggregate(PyObject[] v, String[] n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVizAggregate createVizAggregate(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addVizAggregate(VizAggregate kid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IVizAggregate createVizAggregateReplica(IVizAggregate to_replicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVizAggregate replicateVizAggregate(PyObject[] v, String[] n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVizAggregate createVizAggregateReplica(String name,
			IVizAggregate to_replicate) {
		// TODO Auto-generated method stub
		return null;
	}

}
