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

import java.util.List;

/**
 * @author Nathanael Van Vorst
 *
 */
public class StateExchangeCmd extends AbstractCmd{
	public static final int SET=1;
	public static final int UPDATE=2;
	public static final int FETCH=3;
	
	private final long uid;
	private final long time;
	private final int type;
	private final int community_id;
	private final boolean forViz, aggregate;
	private final List<VarUpdate> updates;
    
	public StateExchangeCmd(long time, long uid, boolean forViz, boolean aggregate, int type, int community_id, List<VarUpdate> updates) {
    	super(CommandType.STATE_EXCHANGE_CMD);
    	this.time=time;
		this.uid=uid;
		this.forViz=forViz;
		this.aggregate=aggregate;
		this.type=type;
		this.community_id=community_id;
		this.updates = updates;
	}
	
	public StateExchangeCmd(PrimeStateExchangeCmd p, int com_id) {
    	super(CommandType.STATE_EXCHANGE_CMD);
    	this.time = p.getTime();
    	this.uid=p.getUid();
    	this.forViz=p.forViz();
    	this.aggregate=p.isAggregate();
    	this.type=p.getCmdType();
    	this.updates = p.getUpdates();
    	this.community_id=com_id;
	}
	
	public boolean aggregate() {
		return aggregate;
	}
	public boolean forViz() {
		return forViz;
	}
	
	/**
	 * @return the community_id
	 */
	public int getCommunity_id() {
		return community_id;
	}

	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
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
				rv = "[StateUpdateCmd: serialNumber="+getSerialNumber()+", forViz="+forViz+", aggregate="+aggregate+", uid="+uid+", time="+time+" (";
			else rv+=",";
			rv+=u.toString();
		}
		return rv+")]";
	}

	@Override
	public int getBodyLength()
	{
		int rv = (Integer.SIZE*3 +Short.SIZE*2 + Long.SIZE*2)/8;
		for(VarUpdate u : updates)
			rv+=u.size();
		return rv;
	}
}
