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

import java.util.Iterator;
import java.util.LinkedList;

import jprime.PersistableObject;
import jprime.StatusListener;
import jprime.util.GlobalProperties;

/**
 * @author Nathanael Van Vorst
 *
 */
public class FIFOCache extends IPersistableCache {
	protected final CacheQueue active_queue;
	
	public FIFOCache(int capacity, DBThread thread) {
		super(thread);
		this.active_queue=new CacheQueue(capacity);
	}
	
	protected PersistableObject __get(PKey key) {
		PersistableObject rv = null;
		LinkedList<LoadedNode> l = activeNodes.get(key);
		if(l!=null) {
			for(LoadedNode n : l) {
				if(n.active) {
					rv = n.get();
					if(rv == null) {
						try {
							throw new RuntimeException("How did this happen?"+key+", l.size()="+l.size());
						}
						catch(Exception e) {
							jprime.Console.err.printStackTrace(e);
							jprime.Console.halt(100);
						}
					}
					break;
				}
				else {
					rv = n.get();
					if(rv != null) {
						try {
							active_queue_lock.lock();
							if(!active_queue.addObj(rv)) {
								drainCollectedQueue();
								clean_item(active_queue.poll());
								if(!active_queue.addObj(rv)) {
									throw new RuntimeException("WTF?");
								}
							}
						} finally {
							active_queue_lock.unlock();
						}
						n.active=true;
						break;
					}
				}
			}
		}
		return rv;
	}
	
	protected LoadedNode __remove(PKey key, int hash) {
		LinkedList<LoadedNode> l = activeNodes.get(key);
		if(l==null) {
			try {
				throw new RuntimeException("How did this happen?"+key+",hash="+hash);
			}
			catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
			return null;
		}
		else {
			LoadedNode found=null;
			Iterator<LoadedNode> it = l.iterator();
			while(it.hasNext()) {
				found=it.next();
				if(found.hash==hash) {
					it.remove();
					break;
				}
				else found=null; //XXX ACK!
			}
			if(found == null) {
				try {
					throw new RuntimeException("How did this happen?"+key+", l.size()="+l.size()+", obj hash="+hash);
				}
				catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
					for(LoadedNode n : l) {
						jprime.Console.err.println("n.hash:"+n.hash+", n.active:"+n.active+", n..get():"+n.get());
					}
					jprime.Console.halt(100);
				}
			}
			return found;
		}
	}

	protected boolean __deactive(PKey key, int hash) {
		LinkedList<LoadedNode> l = activeNodes.get(key);
		if(l==null) {
			try {
				throw new RuntimeException("How did this happen?"+key);
			}
			catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		else {
			for(LoadedNode n : l) {
				if(n.hash == hash) {
					n.active=false;
					return true;
				}
			}
			try {
				throw new RuntimeException("How did this happen?"+key+", l.size()="+l.size());
			}
			catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		return false;
	}

	protected boolean __activate(PKey key, int hash) {
		LinkedList<LoadedNode> l = activeNodes.get(key);
		if(l==null) {
			try {
				throw new RuntimeException("How did this happen?"+key);
			}
			catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		else {
			for(LoadedNode n : l) {
				if(n.hash == hash) {
					boolean old_val=n.active;
					n.active=true;
					return old_val;
				}
			}
			try {
				throw new RuntimeException("How did this happen?"+key+", l.size()="+l.size());
			}
			catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		throw new RuntimeException("How did this happen?");
	}

	protected LoadedNode __add(PKey key,PersistableObject obj) {
		LinkedList<LoadedNode> l = activeNodes.get(key);
		LoadedNode rv = new LoadedNode(obj);
		if(l==null) {
			l=new LinkedList<LoadedNode>();
			l.add(rv);
			activeNodes.put(key, l);
		}
		else {
			for(LoadedNode n : l) {
				if(n.active) {
					try {
						throw new RuntimeException("How did this happen?"+key+", l.size()="+l.size());
					}
					catch(Exception e) {
						jprime.Console.err.printStackTrace(e);
						jprime.Console.halt(100);
					}
				}
			}
			l.add(rv);
		}
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#size()
	 */
	public int size() {
		return active_queue.size();
	}

	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#remove(jprime.IPeristable)
	 */
	public void remove(PersistableObject obj) {
		PKey p = obj.getPKey();
		if(p!=null) {
			LoadedNode n = null;
			try {
				active_map_lock.lock();
				n=__remove(obj.getPKey(), obj.oldHashCode());
				if(n==null) {
					try {
						throw new RuntimeException("How did this happen?"+p+" :: "+obj);
					}
					catch(Exception e) {
						jprime.Console.err.printStackTrace(e);
					}
					jprime.Console.halt(100);
				}
				//this was the most recently active node
				if(n.active) {
					active_queue_lock.lock();
					if(!active_queue.remove(obj)) {
						try {
							throw new RuntimeException("How did this happen?"+p+", obj="+obj+", state="+obj.getPersistableState());
						}
						catch(Exception e) {
							jprime.Console.err.printStackTrace(e);
						}
						jprime.Console.err.println("OBJ:"+p+", obj="+obj+", state="+obj.getPersistableState()+", hash="+obj.oldHashCode()+", n.hash="+n.hash);
						PersistableObject obj1=n.get();
						if(obj1 == null)
							jprime.Console.err.println("n.get()"+p+", obj="+obj1+", state=NULL");
						else
							jprime.Console.err.println("n.get()"+p+", obj="+obj1+", state="+obj1.getPersistableState());
						for(PersistableObject o : active_queue) {
							jprime.Console.err.println(o.getPKey()+" :: "+o+", state="+o.getPersistableState());
						}
						jprime.Console.halt(-1);
					}
					active_queue_lock.unlock();
					n.active=false;
				}
			} finally {
				active_map_lock.unlock();
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#flush()
	 */
	public void flush(boolean flushMetaExps) {
		super.flush(flushMetaExps);
		try {
			active_map_lock.lock();
	    	jprime.Console.out.println("\tDraining collected queue[0]."); jprime.Console.out.flush();
			drainCollectedQueue();
			active_queue_lock.lock();
	    	jprime.Console.out.println("\t[0]Cleaning "+active_queue.size()+" objects."); jprime.Console.out.flush();
		} finally {
			active_queue_lock.unlock();
			active_map_lock.unlock();
		}
		boolean stop=false;
		while(!stop) {
			int c =GlobalProperties.BATCH_SIZE;
	    	jprime.Console.out.println("\t[1]Cleaning "+c+" objects.");
	    	try {
	    		active_map_lock.lock();
				active_queue_lock.lock();
				while(active_queue.size()>0 && c >0) {
					clean_item(active_queue.poll());
					c--;
				}
				stop=active_queue.size()==0;
	    	} finally {
				active_queue_lock.unlock();
				active_map_lock.unlock();
	    	}
	    	jprime.Console.out.println("\t[2] "+active_queue.size()+" objects remain.");
	    	Thread.yield();
		}
		try {
			active_map_lock.lock();
	    	jprime.Console.out.println("\tDraining collected queue[1]."); jprime.Console.out.flush();
			drainCollectedQueue();
		} finally {
			active_map_lock.unlock();
		}
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
	
	/* (non-Javadoc)
	 * @see jprime.database.IPersistableCache#loaded(jprime.IPeristable)
	 */
	public void loaded(PersistableObject obj) {
		PKey p = obj.getPKey();
		//jprime.Console.err.println("1 ["+obj.getClass().getSimpleName()+"] loaded dbid:"+p+" :: "+obj);jprime.Console.err.flush();
		if(p!=null) {
			PersistableObject evicted = null;
			try {
				active_map_lock.lock();
				active_queue_lock.lock();
				if(active_queue.addObj(obj)) {
					__add(p,obj);
					obj.cached=true;
				}
				else {
					evicted = active_queue.poll();
					if(active_queue.addObj(obj)) {
						__add(p,obj);
						obj.cached=true;
					}
					else {
						throw new RuntimeException("WTF?");
					}
				}
				if(null != evicted) {
					drainCollectedQueue();
					clean_item(evicted);
				}
			} finally  {
				active_queue_lock.unlock();
				active_map_lock.unlock();
			}
		}
	}
	protected void saveExp(long metaid, StatusListener sl) {
		try {
			active_map_lock.lock();
			active_queue_lock.lock();
			for(PersistableObject cur : active_queue) {
				switch(cur.getPersistableState()) {
				case NEW:
				case MODIFIED:
					if(cur.getMetadata().getDBID() == metaid) {
						cur.save();
					}
					break;
				}
				sl.finsihed(1);
			}
		} finally {
			active_queue_lock.unlock();
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
