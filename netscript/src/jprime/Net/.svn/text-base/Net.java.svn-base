package jprime.Net;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.RoutingSphere.EdgeInterfacePair;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.RoutingSphere.RoutingSphere;
import jprime.VizAggregate.VizAggregate;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.routing.AlgorithmicRouting;
import jprime.routing.BGP;
import jprime.routing.IRouteVisitor;
import jprime.routing.RouteCalculationVisitor;
import jprime.routing.ShortestPath;
import jprime.routing.StaticRoutingProtocol;
import jprime.util.ChildList;
import jprime.util.IPAddressUtil;
import jprime.variable.ModelNodeVariable;
import jprime.visitors.IVizVisitor;
import jprime.visitors.VerifyVisitor;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class Net extends jprime.gen.Net implements jprime.Net.INet {
	private long __staticRoutingProtocol_id;
	private int __staticRoutingProtocol_type;
	private SoftReference<StaticRoutingProtocol> __staticRoutingProtocol=null;

	
	//transient 
	protected TreeSet<Integer> alignments;
	protected Boolean haveChildrenRoutingSpheres;
	protected List<SubEdgeInterfaceHostPair> subSphereEdges;
	protected Object extraVizInfo=null;
	protected HashMap<Integer, Long> cnf_contents=null;

	/**
	 * @param v
	 * @param n
	 */
	public Net(PyObject[] v, String[] n){
		super(v,n);
		this.alignments = null;
		this.__staticRoutingProtocol_id = 0;
		this.__staticRoutingProtocol=null;
		this.__staticRoutingProtocol_type=0;
		addRS();
		addVizAgg();
		this.haveChildrenRoutingSpheres=null;
		this.subSphereEdges=null;
	}

	/**
	 * for the jpa
	 */
	public Net(ModelNodeRecord rec){
		super(rec);
		this.__staticRoutingProtocol=null;
		this.__staticRoutingProtocol_id=0;
		this.__staticRoutingProtocol_type=0;
		for(ChildId c : rec.kids) {
			if(c.type>EntityFactory.StaticRoutingProtocolStart && c.type<EntityFactory.StaticRoutingProtocolEND) {
				this.__staticRoutingProtocol_id=c.child_id;
				this.__staticRoutingProtocol_type=c.type;
				break;
			}
		}
		this.haveChildrenRoutingSpheres=null;
		this.alignments = null;
		this.subSphereEdges=null;
	}

	/**
	 * this should _only_ be used by the lib/exp to create root node(s)
	 * 
	 * @param meta
	 * @param _name
	 * @param _db_order
	 */
	public Net(Metadata meta, String _name, int _db_order){
		super((IModelNode)null);
		this.setMetadata(meta);
		this.setOrder(_db_order);
		this.setName(_name);
		this.alignments = null;
		this.__staticRoutingProtocol=null;
		this.__staticRoutingProtocol_id=0;
		this.__staticRoutingProtocol_type=0;
		addRS();
		addVizAgg();
		this.haveChildrenRoutingSpheres=null;
		this.subSphereEdges=null;
	}

	/**
	 * @param parent
	 */
	public Net(jprime.IModelNode parent){
		super(parent);
		this.alignments = null;
		this.__staticRoutingProtocol=null;
		this.__staticRoutingProtocol_id=0;
		this.__staticRoutingProtocol_type=0;
		addRS();
		addVizAgg();
		this.haveChildrenRoutingSpheres=null;
		this.subSphereEdges=null;
	}
	private final void addRS() {
		RoutingSphere rs = new RoutingSphere(this);
		rs.setName(INet.ROUTING_SPHERE);
		this.addChild(rs);
	}	
	private final void addVizAgg() {
		VizAggregate va = new VizAggregate(this);
		va.setVarId(ModelNodeVariable.traffic_intensity());
		va.setName(INet.VIZ_AGG_NAME);
		this.addChild(va);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.Net;}

	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#setAlignment(jprime.partitioning.Partitioning, int)
	 */
	public void setAlignment(Partitioning p, int alignment) {
		throw new RuntimeException("Cannot call this on nodes of type "+this.getClass().getSimpleName());
	}

	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#resetAlignments()
	 */
	public void resetAlignments() {
		alignments=null;
	}

	/* (non-Javadoc)
	 * @see jprime.ModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		if(alignments == null) {
			alignments=new TreeSet<Integer>();
			for(ModelNode c : getAllChildren()) {
				alignments.addAll(c.getAlignments(p));
			}
		}
		return alignments;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#getStaticRoutingProtocols()
	 */
	public StaticRoutingProtocol getStaticRoutingProtocol() {
		StaticRoutingProtocol rv = __staticRoutingProtocol==null?null:__staticRoutingProtocol.get();
		if(rv==null) {
			if(__staticRoutingProtocol_id==0) {
				return null;
			}
			rv=getMetadata().loadStaticRoutingProtocol(__staticRoutingProtocol_id);
			__staticRoutingProtocol=new SoftReference<StaticRoutingProtocol>(rv);
		}
    	/* $if DEBUG $
		if(rv!=null) {
			getMetadata().logAccess(rv);
		}
		$endif$ */
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#addStaticRoutingProtocol(jprime.routing.StaticRoutingProtocol)
	 */
	public synchronized void setStaticRoutingProtocol(StaticRoutingProtocol p) {
		StaticRoutingProtocol pp = getStaticRoutingProtocol();
		if(pp!=null) {
			throw new RuntimeException("This network already has "+pp.getClass().getCanonicalName()+" as its static routing protocol!");
		}
		this.__staticRoutingProtocol=new SoftReference<StaticRoutingProtocol>(p);
		this.__staticRoutingProtocol_id=p.getDBID();
		this.__staticRoutingProtocol_type=p.getTypeId();
		if(p.getParent()!=this)
			throw new RuntimeException("How did this happen?");
		modified(Modified.CHILDREN);
	}
	
	@Override
	public synchronized ChildIdList getChildIds() {
		ChildIdList rv = super.getChildIds();
		if(__staticRoutingProtocol_id != 0) {
			rv.add(new ChildId(__staticRoutingProtocol_id, __staticRoutingProtocol_type));
		}
		return rv;
	}


	/* (non-Javadoc)
	 * @see jprime.Net.INet#createBGP()
	 */
	public BGP createBGP() {
		BGP rv = new BGP(this);
		setStaticRoutingProtocol(rv);
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#createShortestPath()
	 */
	public ShortestPath createShortestPath() {
		ShortestPath rv = new ShortestPath(this);
		setStaticRoutingProtocol(rv);
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#createAlgorithmicRouting()
	 */
	public AlgorithmicRouting createAlgorithmicRouting() {
		AlgorithmicRouting rv = new AlgorithmicRouting(this);
		setStaticRoutingProtocol(rv);
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#getRoutingSphere()
	 */
	public IRoutingSphere getRoutingSphere() {
		ChildList<IRoutingSphere> rs =  this.getRsphere();
		IRoutingSphere rv = null;
		if(rs.size()==1) {
			rv = rs.enumerate().next();
		}
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#isRoutingSphere()
	 */
	public boolean isRoutingSphere() {
		return this.getStaticRoutingProtocol()!=null;
	}

	protected static void getEdges(INet child, INet anchor, List<SubEdgeInterfaceHostPair> edges) {
		for(ModelNode c: child.getAllChildren()) {
			if(c instanceof INet) {
				if(((INet)c).isRoutingSphere()) {
					//get edges...
					IRoutingSphere rs = ((INet)c).getRoutingSphere();
					for(EdgeInterfacePair ep : rs.getEdgeInterfaces()) {
						edges.add(new SubEdgeInterfaceHostPair(ep.getRank(ep.getIfaceUID(c),anchor), ep.getRank(ep.getHostUID(c),anchor)));
					}
				}
				else if(((INet)c).haveSubSpheres()) {
					getEdges((INet)c, anchor,edges);
				}
			}
		}
	}

	protected static String makeString(List<SubEdgeInterfaceHostPair> sub_edge_interfaces) {
		String subEdgeIfaces = null;
		if (sub_edge_interfaces != null) {
			for (SubEdgeInterfaceHostPair iface : sub_edge_interfaces) {
				if (subEdgeIfaces!=null) {
					subEdgeIfaces += ", ";
				}
				else {
					subEdgeIfaces="[";
				}
				// jprime.Console.out.println("Trying to store edge ifaces...net="+net.getUniqueName()+", anchor="+anchor.getUniqueName());
				subEdgeIfaces += "[";
				subEdgeIfaces += Long.toString(iface.getIfaceRank());
				subEdgeIfaces += ", ";
				subEdgeIfaces += Long.toString(iface.getHostRank());
				subEdgeIfaces += "]";
			}
		}
		if (subEdgeIfaces!=null) {
			subEdgeIfaces += "]";
		}
		else {
			subEdgeIfaces="[]";
		}
		return subEdgeIfaces;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#haveSubSpheres()
	 */
	public boolean haveSubSpheres() {
		if(haveChildrenRoutingSpheres == null) {
			for(ModelNode c: getAllChildren()) {
				if(c instanceof INet) {
					if(((INet)c).isRoutingSphere()) {
						haveChildrenRoutingSpheres=true;
						break;
					}
					else if(((INet)c).haveSubSpheres()) {
						haveChildrenRoutingSpheres=true;
						break;
					}
				}
			}
			if(haveChildrenRoutingSpheres==null) {
				haveChildrenRoutingSpheres=false;
			}
			else {
				this.subSphereEdges=new ArrayList<SubEdgeInterfaceHostPair>();
				getEdges(this, this, this.subSphereEdges);
				//jprime.Console.out.println("subSphereEdges=" + makeString(subSphereEdges));
				this.setSubEdgeIfaces(makeString(subSphereEdges));
			}
		}
		return haveChildrenRoutingSpheres;
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Net#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	/* (non-Javadoc)
	 * @see jprime.ModelNode#accept(jprime.visitors.VerifyVisitor)
	 */
	public void accept(VerifyVisitor visitor) {
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.IRouteVisitor)
	 */
	public void accept(IRouteVisitor visitor) {
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.RouteCalculationVisitor)
	 */
	public void accept(RouteCalculationVisitor visitor){
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.VizVisitor)
	 */
	public void accept(IVizVisitor visitor){
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see jprime.ModelNode#accept(jprime.partitioning.PartitioningVisitor)
	 */
	public void accept(PartitioningVisitor visitor) {
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#isEdgeIfaceOfChildSphere(jprime.Interface.IInterface)
	 */
	public boolean isEdgeIfaceOfChildSphere(jprime.Interface.IInterface iface) {
		if(haveSubSpheres()) {
			Iterator<SubEdgeInterfaceHostPair> it=subSphereEdges.iterator();
			while(it.hasNext()){
				SubEdgeInterfaceHostPair s = it.next();
				if(s.getIfaceRank()==iface.getRank(this) && s.getHostRank()==iface.getParent().getRank(this)){
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#isEdgeIfaceOfChildSphere(long, long)
	 */
	public boolean isEdgeIfaceOfChildSphere(long iface, long host) {
		if(haveSubSpheres()) {
			Iterator<SubEdgeInterfaceHostPair> it=subSphereEdges.iterator();
			while(it.hasNext()){
				SubEdgeInterfaceHostPair s = it.next();
				if(s.getIfaceRank()==iface && s.getHostRank()==host){
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#getSubEdgeInterfaces()
	 */
	public List<SubEdgeInterfaceHostPair> getSubEdgeInterfaces() {
		return subSphereEdges;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#setSubEdgeInterfaces()
	 */
	public void setSubEdgeInterfaces(List<SubEdgeInterfaceHostPair> sub_edge_ifaces){
		this.subSphereEdges = sub_edge_ifaces;
	}

	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent) {
		final ModelNode rv = (ModelNode)super.__copy(name,toCopy,parent);
		if(((INet)toCopy).getStaticRoutingProtocol()!=null) {
			IModelNode p = ((INet)toCopy).getStaticRoutingProtocol().__copy(null, ((INet)toCopy).getStaticRoutingProtocol(), rv);
			((INet)rv).setStaticRoutingProtocol((StaticRoutingProtocol)p);
		}
		if(toCopy.getChildByName(INet.VIZ_AGG_NAME)==null) {
			throw new RuntimeException("what happened?");
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#setExtraVizInfo(java.lang.Object)
	 */
	public void setExtraVizInfo(Object o) {
		extraVizInfo=o;
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#getExtraVizInfo()
	 */
	public Object getExtraVizInfo() {
		return extraVizInfo;
	}
	
	protected static void help(List<String> rv) {
		rv.add("Net:");
		rv.add("   Subnet creation:");
		rv.add("      createNet()");
		rv.add("      createNet(String)");
		rv.add("      createNetReplica(INet)");
		rv.add("      createNetReplica(String, INet)");
		rv.add("      newNet(PyObject[], String[])");
		rv.add("      replicateNet(PyObject[], String[])");
		rv.add("   Host creation:");
		rv.add("      createHost()");
		rv.add("      createHost(String)");
		rv.add("      createHostReplica(IHost)");
		rv.add("      createHostReplica(String, IHost)");
		rv.add("      newHost(PyObject[], String[])");
		rv.add("      replicateHost(PyObject[], String[])");
		rv.add("   Router creation:");
		rv.add("      createRouter()");
		rv.add("      createRouter(String)");
		rv.add("      createRouterReplica(IRouter)");
		rv.add("      createRouterReplica(String, IRouter)");
		rv.add("      newRouter(PyObject[], String[])");
		rv.add("      replicateRouter(PyObject[], String[])");
		rv.add("   Link creation:");
		rv.add("      createLink()");
		rv.add("      createLink(String)");
		rv.add("      createLinkReplica(ILink)");
		rv.add("      createLinkReplica(String, ILink)");
		rv.add("      newLink(PyObject[], String[])");
		rv.add("      replicateLink(PyObject[], String[])");
		rv.add("   Simulated Traffic creation:");
		rv.add("      createTraffic()");
		rv.add("      createTraffic(String)");
		rv.add("      createTrafficReplica(ITraffic)");
		rv.add("      createTrafficReplica(String, ITraffic)");
		rv.add("      newTraffic(PyObject[], String[])");
		rv.add("      replicateTraffic(PyObject[], String[])");
		rv.add("   Child Iterators:");		
		rv.add("      getHosts()");
		rv.add("      getLinks()");
		rv.add("      getSubnets()");
		rv.add("      getTraffics()");
		rv.add("   getIpPrefix()\n"+
			   "      Get the ip prefix for this network");

	}

	public List<String> help() {
		List<String> rv=super.help();
		help(rv);
		return rv;
	}
	
	public void addCNFContent(int content_id,long rid) {
		if(cnf_contents == null) {
			if(this.getCnfContentIds() == null)
				cnf_contents = new HashMap<Integer, Long>();
			else
				cnf_contents = decodeCNFContent2RIDMap(this.getCnfContentIds().toString());
		}
		cnf_contents.put(content_id,rid);
		try {
			this.setAttribute(ModelNodeVariable.cnf_content_ids(), Net.encodeCNFContent2RIDMap(cnf_contents));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public HashMap<Integer, Long> getCNFContent2RIDMap(){
		if( cnf_contents == null) {
			if(this.getCnfContentIds() == null) 
				return null;
			cnf_contents = decodeCNFContent2RIDMap(this.getCnfContentIds().toString());
		}
		return cnf_contents;
	}

	/**
	 * Transform a string into a CNFContent2RIDMap
	 * @param s
	 * @return
	 */
	public static HashMap<Integer, Long> decodeCNFContent2RIDMap(String s) {
		HashMap<Integer, Long> rv = new HashMap<Integer, Long>();
		if( s == ""){
			rv = null;
		}
		String mydelimiter = "\\[|,|\\]";
		String[] temp=s.replaceFirst("^"+mydelimiter,"").split(mydelimiter);
		for(int i=0;i<temp.length;i=i+2) {
			rv.put(Integer.parseInt(temp[i]),Long.parseLong(temp[i+1]));
		}
		
		return rv;
	}

	/**
	 * Transform a CNFContent2RIDMap into a string
	 * @param c
	 * @return
	 */
	public static String encodeCNFContent2RIDMap(HashMap<Integer, Long> c) {
		String rv =null;
		for(Entry<Integer, Long> e : c.entrySet()) {
			if(rv == null) rv ="[";
			else rv+=",";
			rv+=e.getKey()+","+e.getValue();
		}
		return rv+"]";
	}
	
	/**
	 * find the host with the ip
	 * @param ip
	 * @return
	 */
	public IInterface findInterfaceWithIp(String ip) {
		return findInterfaceWithIp(IPAddressUtil.ip2Int(ip));
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#findInterfaceWithIp(long)
	 */
	public IInterface findInterfaceWithIp(long ip) {
		try {
			String myip = getIpPrefix().toString().split("/")[0];
			if(IPAddressUtil.contains(IPAddressUtil.ip2Int(myip), (int)this.getIpPrefixLen().getValue(), ip)) {
				for(ModelNode c : getAllChildren()) {
					if(c instanceof ILink)  {
						myip = ((ILink)c).getIpPrefix().toString().split("/")[0];
						if(IPAddressUtil.contains(IPAddressUtil.ip2Int(myip), (int)((ILink)c).getIpPrefixLen().getValue(), ip)) {
							for(ModelNode cc : c.getAllChildren()) {
								if(cc instanceof IInterface) {
									final long lip = IPAddressUtil.ip2Int(((IInterface) cc).getIpAddress().toString());
									if( ip == lip) {
										return (IInterface)cc;
									}
								}
							}
						}
					}
					else if(c instanceof INet) {
						IInterface rv = ((INet)c).findInterfaceWithIp(ip);
						if(null != rv )
							return rv;
					}
				}
			}
		}
		catch(Exception e) {
		}
		return null;
	}	
}
