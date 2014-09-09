package jprime.variable;

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


/**
 * @author Nathanael Van Vorst
 */
public class Dataset {
	private double cur_time = 0;
	public static class SimpleDatum {
		public final double time;
		public final String value;
		public SimpleDatum prev;
		public SimpleDatum(double time, String value,SimpleDatum prev) {
			super();
			this.time = time;
			this.value = value;
			this.prev=prev;
		}
		@Override
		public String toString() {
			return "["+time+":"+value+"]";
		}

	}
	private final HashMap<Long,HashMap<Integer,SimpleDatum>> cache;
	public Dataset() {
		super();
		this.cache=new HashMap<Long, HashMap<Integer,SimpleDatum>>();
	}

	public SimpleDatum getMostRecentDatum(int attr_name, long node_id) {
		HashMap<Integer,SimpleDatum> h = cache.get(node_id);
		if(h == null) return null;
		return h.get(attr_name);
	}
	public double getCurTime() {
		return cur_time;
	}

	public void addDatum(ModelNodeVariable v, long time, String value) {
		final double d = time / 1000.0;
		if(d > cur_time)cur_time=d;
		/* $if SEPARATE_PROP_TABLE $
		HashMap<Integer,SimpleDatum> h = cache.get(v.getOwnerID());
		$else$ */
		HashMap<Integer,SimpleDatum> h = cache.get(v.getOwner().getUID());
		/* $endif$ */
		if(h==null) {
			h=new HashMap<Integer, Dataset.SimpleDatum>();
			/* $if SEPARATE_PROP_TABLE $
			cache.put(v.getOwnerID(),h);
			$else$ */
			cache.put(v.getOwner().getUID(),h);
			/* $endif$ */
		}
		h.put(v.getDBName(), new SimpleDatum(d, value,h.get(v.getDBName())));
	}	

	/**
	 * @param ds
	 * @param val
	 */
	public void addDatum(long node_id, int var_name, double time, String value) {
		time=time/1000;
		if(time > cur_time)cur_time=time;
		HashMap<Integer,SimpleDatum> h = cache.get(node_id);
		if(h==null) {
			h=new HashMap<Integer, Dataset.SimpleDatum>();
			cache.put(node_id,h);
		}
		h.put(var_name, new SimpleDatum(time, value,h.get(var_name)));
	}

}
