package monitor.commands;

import java.util.List;

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
 */
public class PrimeStateExchangeCmd {
	private final long uid;
	private final long time;
	private final int  type;
	private final boolean forViz,aggregate;
	private final List<VarUpdate> updates;
    
	public PrimeStateExchangeCmd(StateExchangeCmd cmd) {
		this.time=cmd.getTime();
		this.uid=cmd.getUid();
		this.forViz=false;
		this.aggregate=false;
		this.type=cmd.getCmdType();
		this.updates=cmd.getUpdates();
	}
	public PrimeStateExchangeCmd(long time, long uid, boolean forViz, boolean aggregate, int type, List<VarUpdate> updates) {
    	this.time=time;
		this.uid=uid;
		this.forViz=forViz;
		this.aggregate=aggregate;
		this.type=type;
		this.updates = updates;
	}
	
	
	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
	}
	
	public boolean forViz() {
		return this.forViz;
	}
	
	public boolean isAggregate() {
		return aggregate;
	}
	
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * @return the type
	 */
	public int getCmdType() {
		return type;
	}
	
	/**
	 * @return the updates
	 */
	public List<VarUpdate> getUpdates() {
		return updates;
	}
	
	@Override
	public String toString() {	
		String rv = null;
		for(VarUpdate u : updates) {
			if(rv == null)
				rv = "[PrimeStateUpdateCmd: uid="+uid+", forViz="+forViz+", agg="+aggregate+", time="+time+" (";
			else rv+=",";
			rv+=u.toString();
		}
		return rv+")]";
	}

	public int getBodyLength()
	{
		int rv = (Integer.SIZE*3 + Long.SIZE*2)/8;
		for(VarUpdate u : updates)
			rv+=u.size();
		return rv;
	}

}
