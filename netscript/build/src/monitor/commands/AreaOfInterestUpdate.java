package monitor.commands;


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


/**
 * @author Nathanael Van Vorst
 *
 * Create a container with id 'containerid' on the specified machine. Then
 * add the specified virtual interfaces with the specified names to the container
 * 
 */
public class AreaOfInterestUpdate extends AbstractCmd {
	public final long[] uids;
	public final boolean add;
	public AreaOfInterestUpdate(long uid, boolean add) {
		super(CommandType.AREA_OF_INTEREST_UPDATE);
		this.uids = new long[1];
		this.uids[0]=uid;
		this.add = add;
		//System.out.println("create new AOI: "+this);
	}
	public AreaOfInterestUpdate(long[] uids, boolean add) {
		super(CommandType.AREA_OF_INTEREST_UPDATE);
		this.uids = uids;
		this.add = add;
		//System.out.println("create new AOI: "+this);
	}
	public AreaOfInterestUpdate dup() {
		return new AreaOfInterestUpdate(uids,add);
	}

	/* (non-Javadoc)
	 * @see monitor.commands.AbstractCmd#getBodyLength()
	 */
	@Override
	public int getBodyLength() {
		return (Long.SIZE*uids.length+ Short.SIZE+Integer.SIZE)/8;
	}

	
	@Override
	public String toString() {
		return "[AreaOfInterestUpdate add="+add+", #uids=" + uids.length + "]";
	}
	
}
