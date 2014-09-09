package jprime.database;

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
import java.util.Iterator;
import java.util.Map.Entry;

import jprime.PersistableObject;
import jprime.StatusListener;

/**
 * @author Nathanael Van Vorst
 *
 */
public class CompleteCache extends IPersistableCache {
	private final HashMap<Long,HashMap<Long,PersistableObject>> nodes=new HashMap<Long, HashMap<Long,PersistableObject>>();
	private Iterator<Entry<Long, HashMap<Long, PersistableObject>>> iter = null;
	private Entry<Long, HashMap<Long, PersistableObject>> entry=null;
	private int count=0;
	public CompleteCache(DBThread thread) {
		super(thread);
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#size()
	 */
	@Override
	public int size() {
		return count;
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#remove(jprime.PersistableObject)
	 */
	@Override
	public void remove(PersistableObject obj) {
		try {
			active_map_lock.lock();
			HashMap<Long,PersistableObject> t = nodes.get(obj.getMetadata().getDBID());
			if(t!=null) {
				t.remove(obj.getDBID());
				count--;
			}
		} finally {
			active_map_lock.unlock();
		}
		
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#flush()
	 */
	@Override
	public void flush(boolean flushMetaExps) {
		super.flush(flushMetaExps);
		try {
			active_map_lock.lock();
			for(HashMap<Long, PersistableObject> h : nodes.values()) {
				for(PersistableObject o : h.values()) {
					o.save();
				}
			}
			nodes.clear();
			count=0;
		} finally {
			active_map_lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#modified(jprime.PersistableObject)
	 */
	@Override
	public void modified(PersistableObject obj) {
		//no op
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#loaded(jprime.PersistableObject)
	 */
	@Override
	public void loaded(PersistableObject obj) {
		try {
			active_map_lock.lock();
			HashMap<Long,PersistableObject> t = nodes.get(obj.getMetadata().getDBID());
			if(t!=null) {
				t.put(obj.getDBID(),obj);
			}
			else {
				t=new HashMap<Long, PersistableObject>();
				t.put(obj.getDBID(),obj);
				nodes.put(obj.getMetadata().getDBID(),t);
			}
			count++;
		} finally {
			active_map_lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#collect(jprime.PersistableObject)
	 */
	@Override
	public void collect(PersistableObject obj) {
		//no op
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#findLoadedObj(jprime.database.PKey)
	 */
	@Override
	public PersistableObject findLoadedObj(PKey p) {
		try {
			PersistableObject rv = null;
			active_map_lock.lock();
			HashMap<Long,PersistableObject> t = nodes.get(p.metaid);
			if(t!=null) {
				rv=t.get(p.dbid);
			}
			return rv;
		} finally {
			active_map_lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#__deactive(jprime.database.PKey, int)
	 */
	@Override
	protected boolean __deactive(PKey key, int hash) {
		throw new RuntimeException("Dont call!");
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#__get(jprime.database.PKey)
	 */
	@Override
	protected PersistableObject __get(PKey key) {
		throw new RuntimeException("Dont call!");
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#__add(jprime.database.PKey, jprime.PersistableObject)
	 */
	@Override
	protected LoadedNode __add(PKey key, PersistableObject obj) {
		throw new RuntimeException("Dont call!");
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#__remove(jprime.database.PKey, int)
	 */
	@Override
	protected LoadedNode __remove(PKey key, int hash) {
		throw new RuntimeException("Dont call!");
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#drainCollectedQueue()
	 */
	@Override
	protected void drainCollectedQueue() {
		throw new RuntimeException("Dont call!");
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#clean_item(jprime.PersistableObject)
	 */
	@Override
	protected void clean_item(PersistableObject o) {
		throw new RuntimeException("Dont call!");
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#saveExp(long, jprime.StatusListener)
	 */
	@Override
	protected void saveExp(long metaid, StatusListener sl) {
		try {
			active_map_lock.lock();
			if(nodes.containsKey(metaid)) {
				for(PersistableObject o : nodes.get(metaid).values()) {
					sl.finsihed(1);
					/* $if DEBUG $
					logFlush(o);
					$endif$ */
					o.save();
				}
				count-=nodes.get(metaid).size();
				nodes.remove(metaid);
			}
		} finally {
			active_map_lock.unlock();
		}
	}
	
	@Override
	protected PersistableObject poll() {
		if(iter == null) {
			iter = nodes.entrySet().iterator();
		}
		if(iter.hasNext()) {
			entry = iter.next();
		}
		else {
			entry=null;
		}
		while(entry != null) {
			if(entry.getValue().size()>0) {
				return entry.getValue().remove(0);
			}
			iter.remove();
			entry = iter.next();
		}
		return null;
	}
}
