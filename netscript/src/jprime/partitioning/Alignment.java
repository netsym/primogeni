package jprime.partitioning;

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

import java.util.ArrayList;
import java.util.List;

import jprime.partitioning.IUIDRange.UIDRangeListManip;

/**
 * @author Nathanael Van Vorst
 *
 */
public class Alignment implements Comparable<Alignment> {
	protected int align_id;
	
	protected Partition processingNode;

	protected List<AlignedUIDRange> uidRanges;
	
	private static final UIDRangeListManip<AlignedUIDRange> range_manip=new UIDRangeListManip<AlignedUIDRange>();
	
	/**
	 * for jpa
	 */
	public Alignment() {
		
	}
	
	/**
	 * @param parent
	 */
	public Alignment(int align_id, Partition parent) {
		this.processingNode=parent;
		this.align_id=align_id;
		this.uidRanges=null;
	}

	public int compareTo(final Alignment argNode) {
		return align_id == argNode.align_id ? 0 : (align_id < argNode.align_id?-1:1);
	}

	/**
	 * @return
	 */
	public Partition getProcessingNode() {
		return processingNode;
	}
	
	/**
	 * @return
	 */
	public int getAlignId() {
		return align_id;
	}
	
	/**
	 * @return
	 */
	public int getPartId() {
		return processingNode.id;
	}
	
	/**
	 * @return
	 */
	public List<? extends IUIDRange> getUIDRanges() {
		return uidRanges;
	}
	
	/**
	 * @param r
	 */
	public void addUIDRanges(long low, long high) {
		if(uidRanges==null) {
			uidRanges = new ArrayList<AlignedUIDRange>();
		}
		range_manip.addUIDRange(low, high, uidRanges, this);
	}
	
	public String toString() {
		return "[Alignment "+align_id+"]";
	}
}