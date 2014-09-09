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

import java.util.Set;


/**
 * @author Nathanael Van Vorst
 * 
 * Nodes such as networks and links can be aligned. 
 * Since sub-nets can be separately aligned, some networks
 * can be "cross-aligned" meaning that they exist in
 * multiple alignments. Similarly for links, each interface
 * can also be separately aligned causing links to
 * be possibly "cross-aligned"
 */
public interface IAlignedNode {
	/**
	 * @param p
	 * @param alignment
	 */
	public void setAlignment(Partitioning p, int alignment);
	
	/**
	 * @return the node's alignments
	 */
	public Set<Integer> getAlignments(Partitioning p);
	
	
	/**
	 * nets and links have this called by child elements when they are modified
	 */
	public void resetAlignments();
}
