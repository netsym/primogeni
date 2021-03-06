package jprime.routing;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.BaseInterface.IBaseInterface;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Host.IHost;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.PersistableObject.Modified;
import jprime.RouteTable.IRouteTable;
import jprime.RoutingSphere.EdgeInterfacePair;
import jprime.routing.IAdjacencyMatrix.NodeAdj;
import jprime.routing.IAdjacencyMatrix.RouteCounts;
import jprime.routing.IRouteEntry.RouteEntryComparator;
import jprime.util.ChildList;
import jprime.util.GlobalProperties;
import jprime.util.PersistentRouteEntryList;

/**
 * @author Nathanael Van Vorst, Ting Li
 */
public abstract class BaseAdjacencyMatrix {
	public static final boolean ENABLE_COMPRESSION = true ;
	public static final int INITIAL_CAPACITY = 20;
	    
	protected final IModelNode anchor;
	protected HashMap<Long,Integer> rank2idx;
	protected Vector<NodeAdj> matrix;
	protected Vector<Long> targets; //hosts, edge interfaces, and emulated routers.
	private int num_temp_routes_inserted=0;
	
	/**
	 * @param anchor
	 */
	public BaseAdjacencyMatrix(IModelNode anchor) {
		this.anchor=anchor;
		this.matrix=new Vector<NodeAdj>();
		this.rank2idx = new HashMap<Long,Integer>();
		this.targets = new Vector<Long>();	
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#computeShortestPath()
	 */
	abstract public RouteCounts computeShortestPath(INet net);

	protected NodeAdj getNodeAdj(IModelNode src) {
		Integer sidx = rank2idx.get(src.getRank(anchor));
		if(sidx == null) {
			sidx=matrix.size();
			rank2idx.put(src.getRank(anchor),sidx);
			matrix.add(new NodeAdj(src,anchor,sidx));
		}
		return matrix.get(sidx);
	}

	public boolean containAdjEntry(IModelNode src, IModelNode dst) {
		return getNodeAdj(src).adjList.containsKey(getNodeAdj(dst).idx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jprime.routing.IAdjacencyMatrix#addEdgeNodes(List<Long>,
	 * jprime.Net.INet)
	 */
	public final void addEdgeNodes(List<Long> enodes, INet net) {
		for (IModelNode m : net.getAllChildren()) {
			if (m instanceof INet) {
				if (((INet) m).getStaticRoutingProtocol() != null) {
					for (EdgeInterfacePair iface : ((INet) m).getRoutingSphere().getEdgeInterfaces()) {
						if(!enodes.contains(iface.getHostUID(m))){
							enodes.add(iface.getHostUID(m));
						}
					}
				} else {
					addEdgeNodes(enodes, ((INet) m));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jprime.routing.IAdjacencyMatrix#storeRoutes(jprime.RouteTable.IRouteTable
	 * , jprime.Net.INet)
	 */
	public final RouteCounts storeRoutes(IRouteTable tbl, INet net) {
		// set edge interfaces for the route table per routing sphere
		int merged=0;
		String edgeIfaces = "[";
		int s = 0;
		//XXX HACK BY NATE --- somehow we see topnet with edge interfaces.....
		//OLD if (net.getRoutingSphere().getEdgeInterfaces() != null) {
		if (net.getRoutingSphere().getEdgeInterfaces() != null && net.getParent() !=null) {
			for (EdgeInterfacePair iface : net.getRoutingSphere().getEdgeInterfaces()) {
				if (s != 0) {
					edgeIfaces += ", ";
				}
				// jprime.Console.out.println("Trying to store edge ifaces...net="+net.getUniqueName()+", anchor="+anchor.getUniqueName());
				edgeIfaces += Long.toString(iface.getIfaceMaxRank(anchor, net));
				s = 1;
			}
			edgeIfaces += "]";
			tbl.setEdgeIfaces(edgeIfaces);
		}else{
			edgeIfaces += "]";
		}
		jprime.Console.out.println("Storing routes for net=" + net.getUniqueName() + " edgeIfaces=" + edgeIfaces);
		PersistentRouteEntryList perm_route_entries = tbl.getPermRouteEntries();
		LinkedList<TempRouteEntry> temp_route_entries = tbl.getTempRouteEntries();
		if(temp_route_entries == null || temp_route_entries.size()==0) {
			throw new RuntimeException("what happend?");
		}
		RouteEntryComparator rc =new RouteEntryComparator(false);
		//add route entries for edge interface (self loop) to the route table
		//XXX HACK BY NATE --- somehow we see topnet with edge interfaces.....
		//OLD if (net.getRoutingSphere().getEdgeInterfaces() != null) {
		if (net.getRoutingSphere().getEdgeInterfaces() != null&& net.getParent() !=null) {
			for (EdgeInterfacePair iface : net.getRoutingSphere().getEdgeInterfaces()) {
				IBaseInterface srcIface = (IBaseInterface) matrix.get(rank2idx.get(iface.iface_max_rank)).node;
				IHost srcNode = (IHost) srcIface.getParent();
				long outboundIface = srcIface.getRank(srcNode);
				long owningHost = srcNode.getRank(net);
						
				// find attached link for the src interface
				ILink link = null;
				INet cur_net = (INet) srcIface.getParent().getParent();
				while (cur_net != null && link == null) {
					link = findAttachedLink(cur_net, srcIface);
					cur_net = (INet) cur_net.getParent();
				}
				IBaseInterface dstIface = srcIface;
				
				//nexthop interface is the src interface
				IBaseInterface nexthopIface = srcIface;
				IHost nexthopNode = srcNode;
				long nexthopId = nexthopNode.getRank(net);
				
				int cost = 0;
				
				// calculate number of bits
				int numOfBits = (int)srcNode.getSize();
				jprime.Console.out.println("srcIface="+srcIface.getUniqueName()+", numOfBits="+numOfBits);
				
				// the next hop node is edge node
				boolean edgeIface = true;
				
				// find the index of the next hop interface on bus and the number of bits of bus link
				long busIdx = 0;
				int numOfBitsBus = 0;
				IBaseInterface attachment = null;
				if (link != null) {
					ChildList<IBaseInterfaceAlias> attachments = ((ILink) link).getAttachments();
					if (attachments.size() > 2 && srcIface.getParent() != nexthopIface.getParent()) {
						// jprime.Console.out.println("srcIface="+srcIface.getUniqueName()+" dstIface="+dstIface.getUniqueName()+", and the link="+link.getUniqueName());
						// jprime.Console.out.println("size of ((ILink)link).getAttachments()="+((ILink)link).getAttachments().size());
						// we need a bus index
						ChildList<IBaseInterfaceAlias>.ChildIterator it = attachments.enumerate();
						// jprime.Console.out.println("Looking for attachment "+nexthopIface.getUniqueName()+" on link "+link.getUniqueName());
						while (it.hasNext()) {
							IBaseInterfaceAlias a = it.next();
							// jprime.Console.out.println("\tOn attachement "+a.getUniqueName()+"-->"+a.deference().getUniqueName());
							if (((IBaseInterface) a.deference()) == nexthopIface) {
								attachment = a;
								break;
							}
							busIdx++;
						}
						numOfBitsBus = (int) Math.ceil(Math.log(attachments.size()) / Math.log(2));
						if (attachment == null) {
							throw new RuntimeException("Something went terribly wrong. Couldn't find the attachment "
											+ nexthopIface.getUniqueName() + " on link " + link.getUniqueName());
						}
					}
				}
				RouteEntry re = new RouteEntry(tbl,srcNode.getMinRank(net), srcNode.getMaxRank(net), dstIface.getMinRank(net), dstIface.getMaxRank(net), 
						outboundIface, owningHost, numOfBits, nexthopId, edgeIface, busIdx, numOfBitsBus, cost);
				__insertPermRouteEntry(net,re,perm_route_entries,rc);
			}
		}
		LinkedList<TempRouteEntry> compressed =null;
		if(ENABLE_COMPRESSION) {
			//merge on srcs
			rc =new RouteEntryComparator(true);
			compressed = new LinkedList<TempRouteEntry>();
			while(temp_route_entries.size()>0) {
				TempRouteEntry re = temp_route_entries.remove(0);
				merged+=insertTempRouteEntry(net,re,compressed,rc);
			}
			//jprime.Console.out.println("srcMerged:"+merged+",route count:"+route_entries.size());
		}
		else {
			compressed=temp_route_entries;
		}
		while(compressed.size()>0) {
			perm_route_entries.add(new RouteEntry(net.getRoutingSphere().getRouteTable(), compressed.remove(0)));
		}
		((ModelNode)tbl).modified(Modified.ALL);
		return new RouteCounts(merged,perm_route_entries.size());
	}
	
	protected int insertPermRouteEntry(INet net, RouteEntry re, PersistentRouteEntryList route_entries, RouteEntryComparator rc) {
		ModelNode rt = (ModelNode)net.getRoutingSphere().getRouteTable();
		int rv = __insertPermRouteEntry(net,re,route_entries,rc);
		rt.modified(Modified.CHILDREN);
		return rv;
	}
	
	protected int __insertPermRouteEntry(INet net, RouteEntry re, PersistentRouteEntryList route_entries, RouteEntryComparator rc) {
		int merged=0;
		if(ENABLE_COMPRESSION) {
			int bindex = Collections.binarySearch(route_entries,re,rc);
			int idx = bindex<0?((-bindex)-1):bindex;
			boolean fail=false;
			//first try to merge with prev, then hit, then next
			for(;idx>=0 && idx<route_entries.size();idx--) {
				final RouteEntry hit=route_entries.get(idx);
				if(hit.merge(re, net,rc.srcFirst)) {
					//we need to remove hit and re-insert it
					re.orphan();
					re = hit;
					route_entries.remove(idx);
					merged++;
				}
				else if(!fail) {
					fail=true;
				}
				else {
					break;
				}
			}
			fail=false;
			//we shouldn't need to re-find the idx because we only
			//walk back until we cannot merge.
			//
			//bindex = Collections.binarySearch(route_entries,re,rc);
			//idx = bindex<0?((-bindex)-1):bindex;
			for(;idx>=0 && idx<route_entries.size();idx++) {
				final RouteEntry hit=route_entries.get(idx);
				if(hit.merge(re, net,rc.srcFirst)) {
					//we need to remove hit and re-insert it
					re.orphan();
					re = hit;
					route_entries.remove(idx);
					merged++;
				}
				else if(!fail) {
					fail=true;
				}
				else {
					break;
				}
			}
			//if we merged we need to find the new location to insert at
			if(merged>0) {
				bindex = Collections.binarySearch(route_entries,re,rc);
			}
			route_entries.add(bindex<0?((-bindex)-1):bindex,re);
		}
		else {
			//just insert it
			route_entries.add(re);
		}
		return merged;
	}
	
	protected int insertTempRouteEntry(INet net, TempRouteEntry re, LinkedList<TempRouteEntry> route_entries, RouteEntryComparator rc) {
		if(num_temp_routes_inserted>GlobalProperties.BATCH_SIZE) {
			num_temp_routes_inserted=0;
		}
		int merged=0;
		if(ENABLE_COMPRESSION) {
			int bindex = Collections.binarySearch(route_entries,re,rc);
			int idx = bindex<0?((-bindex)-1):bindex;
			boolean fail=false;
			//first try to merge with prev, then hit, then next
			for(;idx>=0 && idx<route_entries.size();idx--) {
				final TempRouteEntry hit=route_entries.get(idx);
				if(hit.merge(re, net,rc.srcFirst)) {
					//we need to remove hit and re-insert it
					re = hit;
					route_entries.remove(idx);
					merged++;
				}
				else if(!fail) {
					fail=true;
				}
				else {
					break;
				}
			}
			fail=false;
			//we shouldn't need to re-find the idx because we only
			//walk back until we cannot merge.
			//
			//bindex = Collections.binarySearch(route_entries,re,rc);
			//idx = bindex<0?((-bindex)-1):bindex;
			for(;idx>=0 && idx<route_entries.size();idx++) {
				final TempRouteEntry hit=route_entries.get(idx);
				if(hit.merge(re, net,rc.srcFirst)) {
					//we need to remove hit and re-insert it
					re = hit;
					route_entries.remove(idx);
					merged++;
				}
				else if(!fail) {
					fail=true;
				}
				else {
					break;
				}
			}
			//if we merged we need to find the new location to insert at
			if(merged>0) {
				bindex = Collections.binarySearch(route_entries,re,rc);
			}
			route_entries.add(bindex<0?((-bindex)-1):bindex,re);
		}
		else {
			//just insert it
			route_entries.add(re);
		}
		num_temp_routes_inserted++;
		return merged;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IAdjacencyMatrix#findAttachedLink(jprime.Net.INet, jprime.BaseInterface.IBaseInterface)
	 */
    public final ILink findAttachedLink(INet net, IBaseInterface iface){
    	ILink l=null;
    	for(ModelNode c : net.getAllChildren()){
    		if(c instanceof ILink){
    			ChildList<IBaseInterfaceAlias>.ChildIterator attachments = ((ILink)c).getAttachments().enumerate();
    		    while(attachments.hasNext()) {
    		    	final IBaseInterface t = attachments.next();
    		    	if(t.deference().getUID()==iface.getUID()){
    		    		l=(ILink)c;
    		    		break;
    		    	}
    		    }
    		}
    	}
    	return l;
    }

}
