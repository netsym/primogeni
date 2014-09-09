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
import java.util.Set;
import java.util.TreeSet;

import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.RoutingSphere.RoutingSphereReplica;
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
public class NetReplica extends jprime.gen.NetReplica implements jprime.Net.INet {
	private long __staticRoutingProtocol_id;
	private int __staticRoutingProtocol_type;
	private SoftReference<StaticRoutingProtocol> __staticRoutingProtocol=null;
	protected TreeSet<Integer> alignments;
	protected Boolean haveChildrenRoutingSpheres;
	protected List<SubEdgeInterfaceHostPair> subSphereEdges;
	protected Object extraVizInfo = null;
	protected HashMap<Integer, Long> cnf_contents=null;

	
	/**
	 * @param v
	 * @param n
	 */
	public NetReplica(PyObject[] v, String[] n){
		super(v,n);
		this.alignments=null;
		this.__staticRoutingProtocol_type=0;
		this.__staticRoutingProtocol_id = 0;
		this.__staticRoutingProtocol=null;
		this.haveChildrenRoutingSpheres = null;
		this.subSphereEdges=null;
	}
	
	/**
	 * 
	 */
	public NetReplica(ModelNodeRecord rec){
		super(rec);
		this.__staticRoutingProtocol_type=0;
		this.__staticRoutingProtocol=null;
		this.__staticRoutingProtocol_id=0;
		for(ChildId c : rec.kids) {
			if(c.type>EntityFactory.StaticRoutingProtocolStart && c.type<EntityFactory.StaticRoutingProtocolEND) {
				this.__staticRoutingProtocol_id=c.child_id;
				this.__staticRoutingProtocol_type=c.type;
				break;
			}
		}
		this.alignments=null;
		this.subSphereEdges=null;
	}
	
	/**
	 * @param parent
	 * @param referencedNode
	 */
	public NetReplica(String name, IModelNode parent, jprime.Net.INet referencedNode) {
		super(name, parent,referencedNode);
		this.alignments=null;
		this.haveChildrenRoutingSpheres = null;
		this.__staticRoutingProtocol_type=0;
		this.__staticRoutingProtocol_id = 0;
		this.__staticRoutingProtocol=null;
		this.subSphereEdges=null;
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
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.NetReplica;}

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
	 * @see jprime.Net.INet#getStaticRoutingProtocol()
	 */
	public StaticRoutingProtocol getStaticRoutingProtocol() {
		if(!isReplica()) {
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
		return ((INet)getReplicatedNode()).getStaticRoutingProtocol();
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#addStaticRoutingProtocol(jprime.routing.StaticRoutingProtocol)
	 */
	public synchronized void setStaticRoutingProtocol(StaticRoutingProtocol p) {
		if(isReplica()) {
			this.convertToReal();
		}
		StaticRoutingProtocol pp = getStaticRoutingProtocol();
		if(pp!=null) {
			throw new RuntimeException("This network already has "+pp.getClass().getCanonicalName()+" as its static routing protocol!");
		}
		this.__staticRoutingProtocol=new SoftReference<StaticRoutingProtocol>(p);
		this.__staticRoutingProtocol_id=p.getDBID();
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
		if(isReplica()) {
			this.convertToReal();
		}
		BGP rv = new BGP(this);
		setStaticRoutingProtocol(rv);
		return rv;
	}
	 
	/* (non-Javadoc)
	 * @see jprime.Net.INet#createShortestPath()
	 */
	public ShortestPath createShortestPath() {
		if(isReplica()) {
			this.convertToReal();
		}
		ShortestPath rv = new ShortestPath(this);
		setStaticRoutingProtocol(rv);
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#createAlgorithmicRouting()
	 */
	public AlgorithmicRouting createAlgorithmicRouting() {
		if(isReplica()) {
			this.convertToReal();
		}
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
		if(rv==null) {
			throw new RuntimeException("what happened?");
		}
		return rv;
	}
		
	/* (non-Javadoc)
	 * @see jprime.ModelNodeReplica#convertToReal()
	 */
	protected void convertToReal() {
		if(!isReplica())
			return;
		if(null == this.getStaticRoutingProtocol()) {
			StaticRoutingProtocol s = ((INet)getReplicatedNode()).getStaticRoutingProtocol();
			if(null != s) {
				this.setStaticRoutingProtocol(s.deepCopy(this));
			}
		}
		super.convertToReal();
		//copy 
		//Need to make my routing spheres real as well
		IRoutingSphere s = getRoutingSphere(); 
		if(s != null && s.isReplica()) {
			((RoutingSphereReplica)s).makeReal();
		}
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#isRoutingSphere()
	 */
	public boolean isRoutingSphere() {
		return this.getStaticRoutingProtocol()!=null;
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#haveSubSpheres()
	 */
	public boolean haveSubSpheres() {
		if(!isReplica()) {
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
				} else {
					this.subSphereEdges=new ArrayList<SubEdgeInterfaceHostPair>();
					Net.getEdges(this, this,this.subSphereEdges);
					this.setSubEdgeIfaces(Net.makeString(subSphereEdges));
				}
			}
			return haveChildrenRoutingSpheres;
		}
		return ((INet)getReplicatedNode()).haveSubSpheres();
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
		return isEdgeIfaceOfChildSphere(iface.getRank(this), iface.getParent().getRank(this));
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#isEdgeIfaceOfChildSphere(long, long)
	 */
	public boolean isEdgeIfaceOfChildSphere(long iface, long host) {
		if(!isReplica()) {
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
		return ((INet)getReplicatedNode()).isEdgeIfaceOfChildSphere(iface, host);
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#getSubEdgeInterfaces()
	 */
	public List<SubEdgeInterfaceHostPair> getSubEdgeInterfaces() {
		if(!isReplica())
			return subSphereEdges;
		return ((INet)getReplicatedNode()).getSubEdgeInterfaces();
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#setSubEdgeInterfaces()
	 */
	public void setSubEdgeInterfaces(List<SubEdgeInterfaceHostPair> sub_edge_ifaces){
		if(isReplica()) {
			throw new RuntimeException("Tried to set sub edge ifaces on a replicated node!");
		}
		this.subSphereEdges=sub_edge_ifaces;
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
	
	public List<String> help() {
		List<String> rv=super.help();
		Net.help(rv);
		return rv;
	}
	
	public void addCNFContent(int content_id,long rid) {
		if(isReplica())
			convertToReal();
		if(cnf_contents == null) {
			if(this.getCnfContentIds() == null)
				cnf_contents = new HashMap<Integer, Long>();
			else
				cnf_contents = Net.decodeCNFContent2RIDMap(this.getCnfContentIds().toString());
		}
		cnf_contents.put(content_id,rid);
		try {
			this.setAttribute(ModelNodeVariable.cnf_content_ids(), Net.encodeCNFContent2RIDMap(cnf_contents));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public HashMap<Integer, Long> getCNFContent2RIDMap(){
		if(cnf_contents == null && isReplica()) {
			//check if our replica has this information.... if it does, we need to become real.
			HashMap<Integer, Long> temp = ((INet)getReplicatedNode()).getCNFContent2RIDMap();
			if(temp == null)
				return null;
			if(temp.size()>0)
				convertToReal();
			else
				cnf_contents = temp;
		}
		if( cnf_contents== null) {
			if(this.getCnfContentIds() == null) 
				return null;
			cnf_contents = Net.decodeCNFContent2RIDMap(this.getCnfContentIds().toString());
		}
		return cnf_contents;
	}
	
	/**
	 * find the host with the ip
	 * @param ip
	 * @return
	 */
	public IInterface findInterfaceWithIp(String ip) {
		return findInterfaceWithIp(IPAddressUtil.ip2Int(ip));
	}
	
	/**
	 * find the host with the ip
	 * @param ip
	 * @return
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
