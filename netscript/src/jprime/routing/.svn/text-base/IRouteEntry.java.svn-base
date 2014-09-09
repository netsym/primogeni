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

import java.util.Comparator;

import jprime.Metadata;
import jprime.Net.INet;
import jprime.database.PKey;

/**
 * @author Nathanael Van Vorst
 *
 */
public interface IRouteEntry {
	public static class RouteEntryComparator implements Comparator<IRouteEntry> {
		public final boolean srcFirst;
		public RouteEntryComparator(boolean srcFirst) {
			super();
			this.srcFirst = srcFirst;
		}
		public int compare (IRouteEntry o1, IRouteEntry o2) {
			final int t = o1.compareSimple(o2);
			if(t != 0) return t;
			return o1.compareRanges(o2,srcFirst,true);
		}
	}
	public abstract boolean equals(Object obj);

	/* (non-Javadoc)
	 * @see jprime.IPeristable#getMetadata()
	 */
	public abstract Metadata getMetadata();

	/* (non-Javadoc)
	 * @see jprime.IPeristable#save()
	 */
	public abstract void save();

	/* (non-Javadoc)
	 * @see jprime.PeristableObject#getPKey()
	 */
	public abstract PKey getPKey();

	/* (non-Javadoc)
	 * @see jprime.IPeristable#saved()
	 */
	public abstract void saved();

	/* (non-Javadoc)
	 * @see jprime.IPeristable#modified()
	 */
	public abstract void modified();

	/* (non-Javadoc)
	 * @see jprime.IPeristable#orphan()
	 */
	public abstract void orphan();

	/**
	 * @return
	 */
	public abstract void attach(long pid);

	public abstract String toString();

	/**
	 * @return the srcMin
	 */
	public abstract long getSrcMin();

	/**
	 * @param srcMin the srcMin to set
	 */
	public abstract void setSrcMin(long srcMin);

	/**
	 * @return the srcMax
	 */
	public abstract long getSrcMax();

	/**
	 * @param srcMax the srcMax to set
	 */
	public abstract void setSrcMax(long srcMax);

	/**
	 * @return the dstMin
	 */
	public abstract long getDstMin();

	/**
	 * @param dstMin the dstMin to set
	 */
	public abstract void setDstMin(long dstMin);

	/**
	 * @return the dstMax
	 */
	public abstract long getDstMax();

	/**
	 * @param dstMax the dstMax to set
	 */
	public abstract void setDstMax(long dstMax);

	/**
	 * @return the outboundIface
	 */
	public abstract long getOutboundIface();

	/**
	 * @param outboundIface the outboundIface to set
	 */
	public abstract void setOutboundIface(long outboundIface);

	/**
	 * @return the owningHost
	 */
	public abstract long getOwningHost();

	/**
	 * @param owningHost the owningHost to set
	 */
	public abstract void setOwningHost(long owningHost);

	/**
	 * @return the numOfBits
	 */
	public abstract int getNumOfBits();

	/**
	 * @param numOfBits the numOfBits to set
	 */
	public abstract void setNumOfBits(int numOfBits);

	/**
	 * @return the nextHopId
	 */
	public abstract long getNextHopId();

	/**
	 * @param nextHopId the nextHopId to set
	 */
	public abstract void setNextHopId(int nextHopId);

	/**
	 * @return the edgeIface
	 */
	public abstract boolean getEdgeIface();

	/**
	 * @param edgeIface the edgeIface to set
	 */
	public abstract void setEdgeIface(boolean edgeIface);

	/**
	 * @return the busIdx
	 */
	public abstract long getBusIdx();

	/**
	 * @param busIdx the busIdx to set
	 */
	public abstract void setBusIdx(int busIdx);

	/**
	 * @return the numOfBitsBus
	 */
	public abstract int getNumOfBitsBus();

	/**
	 * @param numOfBitsBus the numOfBitsBus to set
	 */
	public abstract void setNumOfBitsBus(int numOfBitsBus);

	/**
	 * @return the cost
	 */
	public abstract int getCost();

	/**
	 * @param cost the cost to set
	 */
	public abstract void setCost(int cost);

	/**
	 * @return
	 */
	public abstract long getDBID();

	/**
	 * @return
	 */
	public abstract long getParent_id();

	/**
	 * @return
	 */
	public abstract long getMetadata_id();

	/* (non-Javadoc)
	 * @see jprime.IPeristable#getTypeId()
	 */
	public abstract int getTypeId();

	public abstract int compareSimple(IRouteEntry o);

	public abstract int compareRanges(IRouteEntry o, boolean srcFirst,
			boolean do_adj);

	public abstract int compareRangesDstFirst(IRouteEntry o, boolean do_adj);

	public abstract int compareRangesSrcFirst(IRouteEntry o, boolean do_adj);

	/**
	 * see if we can update ourselves to encompus re
	 * @param re
	 * @return
	 */
	public abstract boolean merge(IRouteEntry re, INet net, boolean srcFirst);

	
	public static class RECompareFuncs {
		public static int compareSimple(IRouteEntry one, IRouteEntry two) {
			if(one.getCost() < two.getCost()){
				return -1;
			}
			else if(one.getCost() > two.getCost()){
				return 1;
			}

			if(one.getNumOfBitsBus() < two.getNumOfBitsBus()){
				return -1;
			}
			else if(one.getNumOfBitsBus() > two.getNumOfBitsBus()){
				return 1;
			}

			if(one.getBusIdx() < two.getBusIdx()){
				return -1;
			}else if(one.getBusIdx() > two.getBusIdx()){
				return 1;
			}

			if(one.getEdgeIface() != two.getEdgeIface()){
				if(one.getEdgeIface()) {
					return 1;
				}
				return -1;
			}

			if(one.getNextHopId() < two.getNextHopId()) {
				return -1;
			}
			else if(one.getNextHopId() > two.getNextHopId()) {
				return 1;
			}

			if(one.getNumOfBits() < two.getNumOfBits()){
				return -1;
			}else if(one.getNumOfBits() > two.getNumOfBits()){
				return 1;
			}

			if(one.getOwningHost() < two.getOwningHost()){
				return -1;
			}
			else if(one.getOwningHost() > two.getOwningHost()){
				return 1;
			}		

			if(one.getOutboundIface() < two.getOutboundIface()){
				return -1;
			}
			else if(one.getOutboundIface() > two.getOutboundIface()){
				return 1;
			}

			return 0;
		}
		
		public static int compareRangesDstFirst(IRouteEntry one, IRouteEntry two, boolean do_adj) {
			if(one.getDstMin() == two.getDstMin() && one.getDstMax() == two.getDstMax()) {
				//o Dst ranges are equal
				if(one.getSrcMin() <= two.getSrcMin() && one.getSrcMax() >= two.getSrcMax()) {
					//o Src is contained in this Src
					return 0;
				}
				else if(do_adj && (one.getSrcMin()-1 == two.getSrcMax() || one.getSrcMax() == two.getSrcMin()-1)) {
					//they are adjacent
					return 0;
				}
				else {
					//Src ranges are not contained in each other
					if(one.getSrcMax() < two.getSrcMax()) {
						return -1;
					}
					else if(one.getSrcMax() > two.getSrcMax()) {
						return 1;
					}
					else {
						if(one.getSrcMin() < two.getSrcMin()) {
							return -1;
						}
						return 1;
					}
				}
			}
			else if(one.getDstMin() <= two.getDstMin() && one.getDstMax() >= two.getDstMax()) {
				//o Dst is contained in this Dst
				if(one.getSrcMin() <= two.getSrcMin() && one.getSrcMax() >= two.getSrcMax()) {
					//o Src is contained in this Src
					return 0;
				}
				else {
					//Src ranges are not contained in each other
					if(one.getSrcMax() < two.getSrcMax()) {
						return -1;
					}
					else if(one.getSrcMax() > two.getSrcMax()) {
						return 1;
					}
					else {
						if(one.getSrcMin() < two.getSrcMin()) {
							return -1;
						}
						return 1;
					}
				}
			}
			else if(two.getDstMin() <= one.getDstMin() && two.getDstMax() >= one.getDstMax()) {
				//this Dst is contained in o Dst
				if(two.getSrcMin() <= one.getSrcMin() && two.getSrcMax() >= one.getSrcMax()) {
					//this Src is contained in o Src
					return 0;
				}
				else {
					//Src ranges are not contained in each other
					if(one.getSrcMax() < two.getSrcMax()) {
						return -1;
					}
					else if(one.getSrcMax() > two.getSrcMax()) {
						return 1;
					}
					else {
						if(one.getSrcMin() < two.getSrcMin()) {
							return -1;
						}
						return 1;
					}
				}
			}
			else if(do_adj && (one.getDstMin()-1 == two.getDstMax() || one.getDstMax() == two.getDstMin()-1)) {
				//the Dsts are adjacent
				if(one.getSrcMin() == two.getSrcMin() && one.getSrcMax() == two.getSrcMax()) {
					//o Src are equal
					return 0;
				}
			}
			//Dst ranges are not contained in each other
			if(one.getDstMax() < two.getDstMax()) {
				return -1;
			}
			else if(one.getDstMax() > two.getDstMax()) {
				return 1;
			}
			else {
				if(one.getDstMin() < two.getDstMin()) {
					return -1;
				}
				return 1;
			}
		}

		public static int compareRangesSrcFirst(IRouteEntry one, IRouteEntry two, boolean do_adj) {
			if(one.getSrcMin() == two.getSrcMin() && one.getSrcMax() == two.getSrcMax()) {
				//o src ranges are equal
				if(one.getDstMin() <= two.getDstMin() && one.getDstMax() >= two.getDstMax()) {
					//o dst is contained in this dst
					return 0;
				}
				else if(do_adj&& (one.getDstMin()-1 == two.getDstMax() || one.getDstMax() == two.getDstMin()-1)) {
					//they are adjacent
					return 0;
				}
				else {
					//dst ranges are not contained in each other
					if(one.getDstMax() < two.getDstMax()) {
						return -1;
					}
					else if(one.getDstMax() > two.getDstMax()) {
						return 1;
					}
					else {
						if(one.getDstMin() < two.getDstMin()) {
							return -1;
						}
						return 1;
					}
				}
			}
			else if(one.getSrcMin() <= two.getSrcMin() && one.getSrcMax() >= two.getSrcMax()) {
				//o src is contained in this src
				if(one.getDstMin() <= two.getDstMin() && one.getDstMax() >= two.getDstMax()) {
					//o dst is contained in this dst
					return 0;
				}
				else {
					//dst ranges are not contained in each other
					if(one.getDstMax() < two.getDstMax()) {
						return -1;
					}
					else if(one.getDstMax() > two.getDstMax()) {
						return 1;
					}
					else {
						if(one.getDstMin() < two.getDstMin()) {
							return -1;
						}
						return 1;
					}
				}
			}
			else if(two.getSrcMin() <= one.getSrcMin() && two.getSrcMax() >= one.getSrcMax()) {
				//this src is contained in o src
				if(two.getDstMin() <= one.getDstMin() && two.getDstMax() >= one.getDstMax()) {
					//this dst is contained in o dst
					return 0;
				}
				else {
					//dst ranges are not contained in each other
					if(one.getDstMax() < two.getDstMax()) {
						return -1;
					}
					else if(one.getDstMax() > two.getDstMax()) {
						return 1;
					}
					else {
						if(one.getDstMin() < two.getDstMin()) {
							return -1;
						}
						return 1;
					}
				}
			}
			else if(do_adj && (one.getSrcMin()-1 == two.getSrcMax() || one.getSrcMax() == two.getSrcMin()-1)) {
				//the srcs are adjacent
				if(one.getDstMin() == two.getDstMin() && one.getDstMax() == two.getDstMax()) {
					//o dst are equal
					return 0;
				}
			}
			//src ranges are not contained in each other
			if(one.getSrcMax() < two.getSrcMax()) {
				return -1;
			}
			else if(one.getSrcMax() > two.getSrcMax()) {
				return 1;
			}
			else {
				if(one.getSrcMin() < two.getSrcMin()) {
					return -1;
				}
				return 1;
			}
		}
	}
}