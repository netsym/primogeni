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

import jprime.Interface.IInterface;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.partitioning.IAlignedNode;
import jprime.routing.AlgorithmicRouting;
import jprime.routing.BGP;
import jprime.routing.ShortestPath;
import jprime.routing.StaticRoutingProtocol;

/**
 * @author Nathanael Van Vorst
 *
 */
public interface INet extends jprime.gen.INet, IAlignedNode {
	public static String ROUTING_SPHERE = "_rsphere_";
	public static String VIZ_AGG_NAME = "_viz_agg_";
	
	
	public static class SubEdgeInterfaceHostPair {
		/*
		 * For the below calculations, recall that:
		 * 
		 * rank(N,A) = uid(N)-uid(A)+size(A)
		 * 
		 * which implies
		 *
		 * uid(N) = rank(N,A)+uid(A)-size(A)
		 * 
		 */
		public final long interface_rank;
		public final long owning_host_rank;
		public SubEdgeInterfaceHostPair(long iface_rank, long host_rank) {
			interface_rank = iface_rank; 
			owning_host_rank = host_rank;
		}
		public long getIfaceRank() {
			return interface_rank;
		}
		public long getHostRank() {
			return owning_host_rank;
		}
	};
	
	/**
	 * @param o
	 */
	public void setExtraVizInfo(Object o);
	
	/**
	 * @return
	 */
	public Object getExtraVizInfo();
	
	/**
	 * @return
	 */
	public IRoutingSphere getRoutingSphere();
	
	/**
	 * @return
	 */
	public StaticRoutingProtocol getStaticRoutingProtocol();
	
	/**
	 * @param p
	 */
	public void setStaticRoutingProtocol(StaticRoutingProtocol p);
	 
	/**
	 * @return
	 */
	public BGP createBGP();
	 
	/**
	 * @return
	 */
	public ShortestPath createShortestPath();
	
	/**
	 * @return
	 */
	public AlgorithmicRouting createAlgorithmicRouting();
	
	/**
	 * 
	 * @return
	 */
	public boolean isRoutingSphere();
	
	/**
	 * @return
	 */
	public boolean haveSubSpheres();
	
	/**
	 * @param iface
	 * @return
	 */
	public boolean isEdgeIfaceOfChildSphere(jprime.Interface.IInterface iface);

	/**
	 * @param iface, host
	 * @return
	 */
	public boolean isEdgeIfaceOfChildSphere(long iface, long host);

	/**
	 * @return
	 */
	public List<SubEdgeInterfaceHostPair> getSubEdgeInterfaces();
	
	/**
	 * @param sub_edge_ifaces
	 */
	public void setSubEdgeInterfaces(List<SubEdgeInterfaceHostPair> sub_edge_ifaces);
	
	/**
	 * @return
	 */
	public HashMap<Integer,Long> getCNFContent2RIDMap();
	
	/**
	 * add an item of cnf_router/host rid and content cid to the CNFContent2RIDMap
	 * @param rid
	 * @param content_id
	 */
	public void addCNFContent(int content_id, long rid);

	/**
	 * find the interface with the ip
	 * @param ip
	 * @return
	 */
	public IInterface findInterfaceWithIp(String ip);
	
	/**
	 * find the interface with the ip
	 * @param ip
	 * @return
	 */
	public IInterface findInterfaceWithIp(long ip);
	
}
