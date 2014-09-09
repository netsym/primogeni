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

/**
 * @author Nathanael Van Vorst
 *
 */
public class HostAlignment {
	protected Partitioning partitioning;
	protected long uid;
	protected int align_id;
	protected ArrayList<Long> ips;
	
	/**
	 * for jpa
	 */
	public HostAlignment() {
		
	}
	
	/**
	 * @param parent
	 */
	public HostAlignment(long uid, ArrayList<Long> ips, int align_id, Partitioning partitioning) {
		this.partitioning=partitioning;
		this.align_id=align_id;
		this.uid=uid;
		this.ips=ips;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getAlignId() {
		return align_id;
	}

	public void setAlignId(int align_id) {
		this.align_id = align_id;
	}
	
	public ArrayList<Long> getIPs() {
		return this.ips;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[HostAlignment "+uid+" --> "+align_id+"]";
	}

}