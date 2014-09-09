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

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Net.INet;
import jprime.RouteTable.IRouteTable;
import jprime.database.PKey;
import jprime.util.RankSearchNode;
import jprime.util.RankSearchNode.NodeTripple;

/**
 * @author Nathanael Van Vorst
 *
 */
public class TempRouteEntry implements IRouteEntry {
	//transient
	private Metadata meta;
	
	//peristed
	protected long dbid;
	protected long parent_id;
	protected long metadata_id;
	protected long srcMin;
	protected long srcMax;
	protected long dstMin;
	protected long dstMax;
	protected long outboundIface;
	protected long owningHost;
	protected int numOfBits;
	protected long nextHopId;
	protected boolean edgeIface;
	protected long busIdx;
	protected int numOfBitsBus;
	protected int cost;
	
	protected TempRouteEntry(long srcMin, long srcMax, long dstMin, long dstMax) {
		super();
		this.srcMin = srcMin;
		this.srcMax = srcMax;
		this.dstMin = dstMin;
		this.dstMax = dstMax;
		
		this.parent_id = -1;
		this.dbid=-1;
		this.meta=null;
		this.metadata_id=-1;
		this.outboundIface = -1;
		this.owningHost = -1;
		this.numOfBits = -1;
		this.nextHopId = -1;
		this.edgeIface = false;
		this.busIdx = -1;
		this.numOfBitsBus = -1;
		this.cost = -1;
	}

	public TempRouteEntry(Metadata meta, long dbid, long parent_id, long srcMin, long srcMax, long dstMin,
			long dstMax, long outboundIface, long owningHost, int numOfBits, long nextHopId,
			boolean edgeIface, long busIdx, int numOfBitsBus, int cost) {
		super();
		this.parent_id = parent_id;
		this.dbid=dbid;
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.srcMin = srcMin;
		this.srcMax = srcMax;
		this.dstMin = dstMin;
		this.dstMax = dstMax;
		this.outboundIface = outboundIface;
		this.owningHost = owningHost;
		this.numOfBits = numOfBits;
		this.nextHopId = nextHopId;
		this.edgeIface = edgeIface;
		this.busIdx = busIdx;
		this.numOfBitsBus = numOfBitsBus;
		this.cost = cost;
	}

	/**
	 * The constructor that should be used...
	 * @param parent
	 * @param srcMin
	 * @param srcMax
	 * @param dstMin
	 * @param dstMax
	 * @param outboundIface
	 * @param owningHost
	 * @param numOfBits
	 * @param nextHopId
	 * @param edgeIface
	 * @param busIdx
	 * @param numOfBitsBus
	 * @param cost
	 */
	public TempRouteEntry(IRouteTable parent, long srcMin, long srcMax, long dstMin,
			long dstMax, long outboundIface, long owningHost, int numOfBits, long nextHopId,
			boolean edgeIface, long busIdx, int numOfBitsBus, int cost) {
		super();
		this.parent_id = parent.getDBID();
		this.dbid=parent.getMetadata().getNextModelNodeDBID();
		this.meta=parent.getMetadata();
		this.metadata_id=meta.getDBID();
		this.srcMin = srcMin;
		this.srcMax = srcMax;
		this.dstMin = dstMin;
		this.dstMax = dstMax;
		this.outboundIface = outboundIface;
		this.owningHost = owningHost;
		this.numOfBits = numOfBits;
		this.nextHopId = nextHopId;
		this.edgeIface = edgeIface;
		this.busIdx = busIdx;
		this.numOfBitsBus = numOfBitsBus;
		this.cost = cost;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TempRouteEntry) {
			return this.metadata_id == ((TempRouteEntry)obj).metadata_id && dbid ==  ((TempRouteEntry)obj).dbid;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getMetadata()
	 */
	public Metadata getMetadata() {
		return meta;
	}
	
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#toString()
	 */
	public String toString() {
		String rv="[TRE srcMin="+srcMin;
		rv+=", srcMax="+srcMax;
		rv+=", dstMin="+dstMin;
		rv+=", dstMax="+dstMax;
		rv+=", outboundIface="+outboundIface;
		rv+=", owningHost="+owningHost;
		rv+=", numOfBits="+numOfBits;
		rv+=", nextHopId="+nextHopId;
		rv+=", edgeIface="+edgeIface;
		rv+=", busIdx="+busIdx;
		rv+=", numOfBitsBus="+numOfBitsBus;
		rv+=", cost="+cost+"]";
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getSrcMin()
	 */
	public long getSrcMin() {
		return srcMin;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setSrcMin(long)
	 */
	public void setSrcMin(long srcMin) {
		this.srcMin = srcMin;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getSrcMax()
	 */
	public long getSrcMax() {
		return srcMax;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setSrcMax(long)
	 */
	public void setSrcMax(long srcMax) {
		this.srcMax = srcMax;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getDstMin()
	 */
	public long getDstMin() {
		return dstMin;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setDstMin(long)
	 */
	public void setDstMin(long dstMin) {
		this.dstMin = dstMin;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getDstMax()
	 */
	public long getDstMax() {
		return dstMax;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setDstMax(long)
	 */
	public void setDstMax(long dstMax) {
		this.dstMax = dstMax;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getOutboundIface()
	 */
	public long getOutboundIface() {
		return outboundIface;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setOutboundIface(long)
	 */
	public void setOutboundIface(long outboundIface) {
		this.outboundIface = outboundIface;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getOwningHost()
	 */
	public long getOwningHost() {
		return owningHost;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setOwningHost(long)
	 */
	public void setOwningHost(long owningHost) {
		this.owningHost = owningHost;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getNumOfBits()
	 */
	public int getNumOfBits() {
		return numOfBits;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setNumOfBits(int)
	 */
	public void setNumOfBits(int numOfBits) {
		this.numOfBits = numOfBits;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getNextHopId()
	 */
	public long getNextHopId() {
		return nextHopId;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setNextHopId(int)
	 */
	public void setNextHopId(int nextHopId) {
		this.nextHopId = nextHopId;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getEdgeIface()
	 */
	public boolean getEdgeIface() {
		return edgeIface;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setEdgeIface(boolean)
	 */
	public void setEdgeIface(boolean edgeIface) {
		this.edgeIface = edgeIface;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getBusIdx()
	 */
	public long getBusIdx() {
		return busIdx;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setBusIdx(int)
	 */
	public void setBusIdx(int busIdx) {
		this.busIdx = busIdx;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getNumOfBitsBus()
	 */
	public int getNumOfBitsBus() {
		return numOfBitsBus;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setNumOfBitsBus(int)
	 */
	public void setNumOfBitsBus(int numOfBitsBus) {
		this.numOfBitsBus = numOfBitsBus;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getCost()
	 */
	public int getCost() {
		return cost;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#setCost(int)
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getDBID()
	 */
	public long getDBID() {
		return dbid;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getParent_id()
	 */
	public long getParent_id() {
		return parent_id;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getMetadata_id()
	 */
	public long getMetadata_id() {
		return metadata_id;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#getTypeId()
	 */
	public int getTypeId() {
		return EntityFactory.RouteEntry;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#compareSimple(jprime.routing.IRouteEntry)
	 */
	public int compareSimple(IRouteEntry o) {
		return RECompareFuncs.compareSimple(this, o);
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#compareRanges(jprime.routing.IRouteEntry, boolean, boolean)
	 */
	public int compareRanges(IRouteEntry o, boolean srcFirst, boolean do_adj) {
		return srcFirst?compareRangesSrcFirst(o,do_adj):compareRangesDstFirst(o,do_adj);
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#compareRangesDstFirst(jprime.routing.IRouteEntry, boolean)
	 */
	public int compareRangesDstFirst(IRouteEntry o, boolean do_adj) {
		return RECompareFuncs.compareRangesDstFirst(this, o, do_adj);
	}

	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#compareRangesSrcFirst(jprime.routing.IRouteEntry, boolean)
	 */
	public int compareRangesSrcFirst(IRouteEntry o, boolean do_adj) {
		return RECompareFuncs.compareRangesSrcFirst(this, o, do_adj);
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.IRouteEntry#merge(jprime.routing.IRouteEntry, jprime.Net.INet, boolean)
	 */
	public boolean merge(IRouteEntry re, INet net, boolean srcFirst) {
		if(compareSimple(re)==0) {
			boolean can_merge=false;
			IRouteEntry prev=null, next=null;
			final int r = compareRanges(re, srcFirst,true);
			if(r==0)  {
				can_merge=true;
			}
			else if(r<0) {
				prev=this;
				next=re;
			}
			else {
				prev=re;
				next=this;
			}
			if(!can_merge) {
				NodeTripple srcMinHost = RankSearchNode.findTripple(next.getSrcMin(),IHost.class,net);
				NodeTripple srcMaxHost = RankSearchNode.findTripple(prev.getSrcMax(),IHost.class,net);

				NodeTripple dstMinIface = RankSearchNode.findTripple(next.getDstMin(),IInterface.class,net);
				NodeTripple dstMaxIface = RankSearchNode.findTripple(prev.getDstMax(),IInterface.class,net);


				/*
				 * If srcMinHost and srcMaxHost are equal or overlap the hosts are compatible.
				 * If dstMinIface and dstMaxIface are equal or overlap the interfaces are compatible.
				 * 
				 * Only if both are compatible can we merge these entries
				 */
				boolean si=false,di=false;
				if(srcMinHost.cur == srcMaxHost.prev || srcMinHost.cur == srcMaxHost.cur || srcMinHost.cur == srcMaxHost.next) {
					//they intersect
					si=true;
				}
				else if(srcMaxHost.cur == srcMinHost.prev || srcMaxHost.cur == srcMinHost.cur || srcMaxHost.cur == srcMinHost.next) {
					si=true;
				}
				if(si) {
					if(dstMinIface.cur == dstMaxIface.prev || dstMinIface.cur == dstMaxIface.cur || dstMinIface.cur == dstMaxIface.next) {
						//they intersect, we can merge them
						di=true;
					}
					else if(dstMaxIface.cur == dstMinIface.prev ||dstMaxIface.cur == dstMinIface.cur || dstMaxIface.cur == dstMinIface.next) {
						//they intersect, we can merge them
						di=true;
					}
					if(di) {
						/*if(net.getUniqueName().toString().compareTo("topnet:campus_0:net01:net1")==0){
							jprime.Console.out.println("\tsrcMinHost: ["+
									(srcMinHost.prev==null?"NULL":srcMinHost.prev.getUniqueName())+","+
									(srcMinHost.cur==null?"NULL":srcMinHost.cur.getUniqueName())+","+
									(srcMinHost.next==null?"NULL":srcMinHost.next.getUniqueName())+"]");
							jprime.Console.out.println("\tsrcMaxHost: ["+
									(srcMaxHost.prev==null?"NULL":srcMaxHost.prev.getUniqueName())+","+
									(srcMaxHost.cur==null?"NULL":srcMaxHost.cur.getUniqueName())+","+
									(srcMaxHost.next==null?"NULL":srcMaxHost.next.getUniqueName())+"]");
							jprime.Console.out.println("\tdstMinIface: ["+
									(dstMinIface.prev==null?"NULL":dstMinIface.prev.getUniqueName())+","+
									(dstMinIface.cur==null?"NULL":dstMinIface.cur.getUniqueName())+","+
									(dstMinIface.next==null?"NULL":dstMinIface.next.getUniqueName())+"]");
							jprime.Console.out.println("\tdstMaxIface: ["+
									(dstMaxIface.prev==null?"NULL":dstMaxIface.prev.getUniqueName())+","+
									(dstMaxIface.cur==null?"NULL":dstMaxIface.cur.getUniqueName())+","+
									(dstMaxIface.next==null?"NULL":dstMaxIface.next.getUniqueName())+"]");
						}*/
						can_merge=true;
					}
				}
			}
			if(can_merge) {
				long  smin,smax,dmin,dmax;
				smin=this.getSrcMin()>re.getSrcMin()?re.getSrcMin():this.getSrcMin();
				dmin=this.getDstMin()>re.getDstMin()?re.getDstMin():this.getDstMin();
				smax=this.getSrcMax()<re.getSrcMax()?re.getSrcMax():this.getSrcMax();
				dmax=this.getDstMax()<re.getDstMax()?re.getDstMax():this.getDstMax();
				
				if(smax < dmin || dmax<smin) {
					/*if(net.getUniqueName().toString().compareTo("topnet:campus_0:net01:net1")==0){
						jprime.Console.out.println("*******************");
						jprime.Console.out.println("\tBEFORE[prev]:"+prev);
						jprime.Console.out.println("\tBEFORE[next]:"+next);
					}*/
					//we can safely merge
					this.setSrcMin(smin);
					this.setSrcMax(smax);
					this.setDstMin(dmin);
					this.setDstMax(dmax);
					//if(net.getUniqueName().toString().compareTo("topnet:campus_0:net01:net1")==0)jprime.Console.out.println("\t\tAFTER:"+this);
					return true;
				}
				else {
					//since there is overlap we could cause a SELF LOOP which is death!
					return false;
				}
			}
		}
		//if we get here that means we could not merge them
		return false;
	}

	public void save() {
		throw new RuntimeException("dont call this on TempRouteEntry");
	}

	public PKey getPKey() {
		throw new RuntimeException("dont call this on TempRouteEntry");
	}

	public void saved() {
		throw new RuntimeException("dont call this on TempRouteEntry");
	}

	public void modified() {
		throw new RuntimeException("dont call this on TempRouteEntry");
	}

	public void orphan() {
		throw new RuntimeException("dont call this on TempRouteEntry");
	}

	public void attach(long pid) {
		throw new RuntimeException("dont call this on TempRouteEntry");
	}
}
