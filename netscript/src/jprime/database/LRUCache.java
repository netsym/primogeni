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

import java.util.LinkedList;

import jprime.PersistableObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class LRUCache extends FIFOCache {

	/**
	 * @param maxMemoryUsage
	 * @param thread
	 */
	public LRUCache(int capacity, DBThread thread) {
		super(capacity,thread);
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#modified(jprime.IPeristable)
	 */
	public void modified(PersistableObject obj) {
		PKey p = obj.getPKey();
		if(p!=null) {
			try {
				active_map_lock.lock();
				if(!__activate(p, obj.oldHashCode())) {
					try {
						active_queue_lock.lock();
						if(active_queue.addObj(obj)) {
							obj.cached=true;
						}
						else {
							drainCollectedQueue();
							clean_item(active_queue.poll());
							if(active_queue.addObj(obj)) {
								obj.cached=true;
							}
							else {
								throw new RuntimeException("WTF?");
							}
						}
					} finally {
						active_queue_lock.unlock();
					}
				}
			} finally {
				active_map_lock.unlock();
			}
		}
	}

	protected boolean __activate_add(PersistableObject obj) {
		PKey key = obj.getPKey();
		int hash = obj.oldHashCode();
		LinkedList<LoadedNode> l = activeNodes.get(key);
		LoadedNode rv = new LoadedNode(obj);
		if(l==null) {
			l=new LinkedList<LoadedNode>();
			l.add(rv);
			activeNodes.put(key, l);
		}
		else {
			for(LoadedNode n : l) {
				if(n.hash == hash) {
					boolean old_val=n.active;
					n.active=true;
					return old_val;
				}
			}
			l.add(rv);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#accessed(jprime.PersistableObject)
	 */
	public void accessed(PersistableObject obj) {
		try {
			active_map_lock.lock();
			if(!obj.cached) {
				//its not in the queue.....
				if(!__activate_add(obj)) {
					try {
						active_queue_lock.lock();
						if(active_queue.addObj(obj)) {
							obj.cached=true;
						}
						else {
							drainCollectedQueue();
							clean_item(active_queue.poll());
							if(active_queue.addObj(obj)) {
								obj.cached=true;
							}
							else {
								throw new RuntimeException("WTF?");
							}
						}
					} finally {
						active_queue_lock.unlock();
					}
				}
			}
			else {
				active_queue.remove(obj);
				if(!active_queue.addObj(obj)) {
					throw new RuntimeException("WTF?");
				}
			}
		} finally {
			active_map_lock.unlock();
		}
	}

	protected PersistableObject poll() {
		try {
			active_queue_lock.lock();
			return active_queue.poll();
		} finally {
			active_queue_lock.unlock();
		}
	}

}
