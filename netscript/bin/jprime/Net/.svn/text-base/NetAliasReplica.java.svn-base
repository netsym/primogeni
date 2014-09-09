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

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.Interface.IInterface;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.partitioning.Partitioning;
import jprime.routing.AlgorithmicRouting;
import jprime.routing.BGP;
import jprime.routing.ShortestPath;
import jprime.routing.StaticRoutingProtocol;
import jprime.util.IPAddressUtil;

import org.python.core.PyObject;
/**
 * @author Nathanael Van Vorst
 *
 */


public class NetAliasReplica extends jprime.gen.NetAliasReplica implements jprime.Net.INetAlias {
	
	/**
	 * @param v
	 * @param n
	 */
	public NetAliasReplica(PyObject[] v, String[] n){super(v,n);}
	
	/**
	 * 
	 */
	public NetAliasReplica(ModelNodeRecord rec){ super(rec); }
	
	/**
	 * @param parent
	 * @param referencedNode
	 */
	public NetAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}


	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.NetAliasReplica;}
	
	/* (non-Javadoc)
	 * @see jprime.gen.NetAliasReplica#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#getStaticRoutingProtocols()
	 */
	public StaticRoutingProtocol getStaticRoutingProtocol() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#addStaticRoutingProtocol(jprime.routing.StaticRoutingProtocol)
	 */
	public void setStaticRoutingProtocol(StaticRoutingProtocol p) {
		throw new RuntimeException("Cant add StaticRoutingProtocols to net aliases!");
	}
	 
	/* (non-Javadoc)
	 * @see jprime.Net.INet#createBGP()
	 */
	public BGP createBGP() {
		throw new RuntimeException("Cant add StaticRoutingProtocols to net aliases!");
	}
	 
	/* (non-Javadoc)
	 * @see jprime.Net.INet#createShortestPath()
	 */
	public ShortestPath createShortestPath() {
		throw new RuntimeException("Cant add StaticRoutingProtocols to net aliases!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#createAlgorithmicRouting()
	 */
	public AlgorithmicRouting createAlgorithmicRouting() {
		throw new RuntimeException("Cant add StaticRoutingProtocols to net aliases!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#getRoutingSphere()
	 */
	public IRoutingSphere getRoutingSphere() {
		return ((INet)getReplicatedNode()).getRoutingSphere();
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#isRoutingSphere()
	 */
	public boolean isRoutingSphere() {
		return ((INet)getReplicatedNode()).getStaticRoutingProtocol()!=null;
	}

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
		((INet)deference()).resetAlignments();
	}

	/* (non-Javadoc)
	 * @see jprime.ModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		return ((INet)deference()).getAlignments(p);
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#haveSubSpheres()
	 */
	public boolean haveSubSpheres() {
		return ((INet)deference()).haveSubSpheres();
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#isEdgeIfaceOfChildSphere(jprime.Interface.IInterface)
	 */
	public boolean isEdgeIfaceOfChildSphere(jprime.Interface.IInterface iface) {
		return ((INet)deference()).isEdgeIfaceOfChildSphere(iface);
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#isEdgeIfaceOfChildSphere(long, long)
	 */
	public boolean isEdgeIfaceOfChildSphere(long iface, long host) {
		return ((INet)deference()).isEdgeIfaceOfChildSphere(iface, host);
	}

	/* (non-Javadoc)
	 * @see jprime.Net.INet#getSubEdgeInterfaces()
	 */
	public List<SubEdgeInterfaceHostPair> getSubEdgeInterfaces() {
		return  ((INet)deference()).getSubEdgeInterfaces();
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#setSubEdgeInterfaces()
	 */
	public void setSubEdgeInterfaces(List<SubEdgeInterfaceHostPair> sub_edge_ifaces){
		throw new RuntimeException("Don't set sub edge interfaces from an alias!");
	}
		
	public List<String> help() {
		List<String> rv=super.help();
		Net.help(rv);
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#setExtraVizInfo(java.lang.Object)
	 */
	public void setExtraVizInfo(Object o) {
		throw new RuntimeException("dont call on aliases!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.Net.INet#getExtraVizInfo()
	 */
	public Object getExtraVizInfo() {
		return ((INet)deference()).getExtraVizInfo();
	}
	
	public void addCNFContent(int content_id,long rid) {
		((INet)deference()).addCNFContent(content_id, rid);
	}

	public HashMap<Integer, Long> getCNFContent2RIDMap() {
		return ((INet)deference()).getCNFContent2RIDMap();
	}
	
	public IInterface findInterfaceWithIp(String ip) {
		return findInterfaceWithIp(IPAddressUtil.ip2Int(ip));
	}
	
	public IInterface findInterfaceWithIp(long ip) {
		return ((INet)deference()).findInterfaceWithIp(ip);
	}
}
