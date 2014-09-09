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

/* $if DEBUG $

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import jprime.Experiment;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.StatusListener;
import jprime.routing.StaticRoutingProtocol;
import jprime.util.GlobalProperties;
$if SEPARATE_PROP_TABLE $
import jprime.variable.ModelNodeVariable;
$endif$

$else$ */

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import jprime.Experiment;
import jprime.Metadata;
import jprime.PersistableObject;
import jprime.StatusListener;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public abstract class IPersistableCache {
	public static enum CacheType {
		COMPLETE("COMPLETE"),
		FIFO("FIFO"),
		LRU("LRU");
		CacheType(String str) { this.str=str;}
		public final String str;
		public static  CacheType fromString(String s) {
			for(CacheType c : values())
				if(c.str.equalsIgnoreCase(s))
					return c;
			throw new RuntimeException("Invalid cache type "+s);
		}
		public static IPersistableCache createCache(CacheType ct, int capacity, DBThread thread) {
			switch(ct) {
			case FIFO:
				return new FIFOCache(capacity, thread);
			case COMPLETE:
				return new CompleteCache(thread);
			case LRU:
				/* $if DEBUG $
				return new LRUCache(capacity, thread);
				$else$ */
				throw new RuntimeException("LRU cache only works with accessing logging on (i.e. DEBUG)...");
				/* $endif$ */
			default:
				throw new RuntimeException("Unknown cache type "+ct.str);
			}
		}
	}
	
	public static class Collected {
		final PKey p;
		final int hash;
		public Collected(PKey p, int hash) {
			this.p = p;
			this.hash = hash;
		}
	}
	public static class LoadedNode extends SoftReference<PersistableObject>{
		public boolean active;
		public int hash;
		public LoadedNode(PersistableObject referent) {
			super(referent);
			this.active=true;
			this.hash=referent.oldHashCode();
		}
	}
	public static enum PhaseChange {
		build_model,
		calc_routes,
		assign_ips,
		assign_uids,
		verify_routes,
		cnf_crap,
		start_tlv_encode,
		start_xml,
		parted_xml,
		shutdown,
		finished;
	}
	
	protected final TreeMap<PKey, LinkedList<LoadedNode>> activeNodes=new TreeMap<PKey, LinkedList<LoadedNode>>();
	protected LinkedList<Collected> collected_queue = new LinkedList<Collected>();
	/* $if DEBUG $
	protected final AccessLogger accessLog=new AccessLogger(GlobalProperties.LOG_DIR+"/"+GlobalProperties.LOG_PREFIX+"access.log");
	$endif$ */
	protected final java.util.concurrent.locks.Lock active_map_lock = new java.util.concurrent.locks.ReentrantLock();
	protected final java.util.concurrent.locks.Lock active_queue_lock = new java.util.concurrent.locks.ReentrantLock();
	protected final java.util.concurrent.locks.Lock collected_queue_lock = new java.util.concurrent.locks.ReentrantLock();
	protected final DBThread thread;
	protected HashMap<Long,Metadata> metas = new HashMap<Long, Metadata>();
	protected HashMap<String,Experiment> exps = new HashMap<String, Experiment>();
	protected Integer targetSize = null;
	protected int over=0;
	public static double onemb = 1024*1024.0;

	public IPersistableCache(DBThread thread) {
		this.thread=thread;
	}

	
	public abstract int size();

	public abstract void remove(PersistableObject obj);

	public abstract void modified(PersistableObject obj);

	public abstract void loaded(PersistableObject obj);
	
	protected abstract boolean __deactive(PKey key, int hash);
	protected abstract PersistableObject __get(PKey key);
	protected abstract LoadedNode __add(PKey key,PersistableObject obj);
	protected abstract LoadedNode __remove(PKey key, int hash);
	protected abstract PersistableObject poll();
	protected abstract void saveExp(long metaid, StatusListener sl);

	public void flush(boolean flushMetaExps) {
    	jprime.Console.out.println("\tFlushing cache."); jprime.Console.out.flush();
		if(flushMetaExps) {
			LinkedList<Metadata> ms = new LinkedList<Metadata>();
			LinkedList<Experiment> es = new LinkedList<Experiment>();
			try {
				active_map_lock.lock();
				active_queue_lock.lock();
				ms.addAll(metas.values());
				es.addAll(exps.values());
				metas.clear();
				exps.clear();
			} finally {
				active_queue_lock.unlock();
				active_map_lock.unlock();
			}
	    	jprime.Console.out.println("\tCleaning "+ms.size()+" metas."); jprime.Console.out.flush();
			while(ms.size()>0) {
				try {
					active_map_lock.lock();
					active_queue_lock.lock();
					clean_item(ms.pop());
				} finally {
					active_queue_lock.unlock();
					active_map_lock.unlock();
				}
			}
	    	jprime.Console.out.println("\tCleaning "+es.size()+" exps."); jprime.Console.out.flush();
			while(es.size()>0) {
				try {
					active_map_lock.lock();
					active_queue_lock.lock();
					clean_item(es.pop());
				} finally {
					active_queue_lock.unlock();
					active_map_lock.unlock();
				}
			}
		}
	}

	public void collect(PersistableObject obj) {
		if(active_map_lock.tryLock()) {
			try {
				collected_queue_lock.lock();
				collected_queue.add(new Collected(obj.getPKey(), obj.oldHashCode()));
				for(Collected c : collected_queue) {
					LoadedNode n=__remove(c.p, c.hash);
					if(n.active) {
						try {
							throw new RuntimeException("How did this happen?"+c.p+" :: hash="+c.hash);
						}
						catch(Exception e) {
							jprime.Console.err.printStackTrace(e);
						}
						jprime.Console.halt(100);
					}
				}
				collected_queue.clear();
			} finally {
				collected_queue_lock.unlock();
				active_map_lock.unlock();
			}

		}
		else {
			collected_queue_lock.lock();
			collected_queue.add(new Collected(obj.getPKey(), obj.oldHashCode()));
			collected_queue_lock.unlock();
		}
	}

	public PersistableObject findLoadedObj(PKey p) {
		if(p==null)return null;
		PersistableObject rv = null;
		try {
			active_map_lock.lock();
			rv=__get(p);
		} finally {
			active_map_lock.unlock();
		}
		return rv;
	}
	
	public void accessed(PersistableObject o) {
		
	}
	
	/* $if DEBUG $

	public void logAccess(int type, long dbid, long pid) {
		try {
			accessLog.logAccess(type,dbid,pid);
		}catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
		}
	}

	public void logCreate(int type, long dbid, long pid) {
		try {
			accessLog.logCreate(type,dbid,pid);
		}catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
		}
	}

	public void logLoad(PersistableObject o, long pid) {
		try {
			accessLog.logLoad(o.getTypeId(),o.getDBID(),pid);
		}catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
		}
	}
	
	public void logFlush(PersistableObject o) {
		try {
			if(o instanceof ModelNode) {
				ModelNode rv = ((ModelNode)o);
				accessLog.logFlush(rv.getTypeId(), rv.getDBID(), rv.getParentId());
			}
			$if SEPARATE_PROP_TABLE $
			else if(o instanceof ModelNodeVariable) {
				ModelNodeVariable rv = ((ModelNodeVariable)o);
				accessLog.logFlush(rv.getTypeId(), rv.getDBID(), rv.getOwnerID());
			}
			$endif$
			else if(o instanceof StaticRoutingProtocol) {
				StaticRoutingProtocol rv = ((StaticRoutingProtocol)o);
				accessLog.logFlush(rv.getTypeId(), rv.getDBID(), rv.getParentId());
			}
			else {
				accessLog.logFlush(o.getTypeId(), o.getDBID(), -1);
			}
		}catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
		}
	}
	public void logPhaseChange(PhaseChange phase, String msg) {
		try {
			accessLog.logPhaseChange(phase.toString()+" "+msg);
			if(phase == PhaseChange.finished) {
				accessLog.close();
			}
		}catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
		}
	}
	$else$ */
	public static long START_TIME=System.currentTimeMillis();
	public static long PREV_TIME=System.currentTimeMillis();
	
	public static void logPhaseChange(PhaseChange phase, String msg) {
		final long time =System.currentTimeMillis();
		if(phase == PhaseChange.build_model) {
			START_TIME=time;
			PREV_TIME=time;
		}
		System.out.println("PHASE_CHANGE, phase="+phase.toString()+", time_from_start="+(time-START_TIME)+", time_in_phase="+(time-PREV_TIME)+", msg="+msg);
		PREV_TIME=time;
	}
	/* $endif$ */

	protected void drainCollectedQueue() {
		collected_queue_lock.lock();
		LinkedList<Collected> temp = collected_queue;
		collected_queue = new LinkedList<Collected>();
		collected_queue_lock.unlock();
		for(Collected c : temp) {
			LoadedNode n=__remove(c.p, c.hash);
			if(n==null) {
				try {
					throw new RuntimeException("How did this happen?"+c.p+" :: hash="+c.hash);
				}
				catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
				}
				jprime.Console.halt(100);
			}
			if(n.active) {
				try {
					throw new RuntimeException("How did this happen?"+c.p+" :: hash="+c.hash);
				}
				catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
				}
				jprime.Console.halt(100);
			}
		}
	}
	
	protected void clean_item(PersistableObject o) {
		/* $if DEBUG $
		accessLog.logEvict(o.getTypeId(), o.getDBID());
		$endif$ */
		try {
			if(o==null || o.getPersistableState()==null) {
				try {
					throw new RuntimeException("what happened?");
				} catch (Exception e) {
					jprime.Console.err.printStackTrace(e);
					jprime.Console.halt(-1);
				}
			}
			/* $if DEBUG $
			logFlush(o);
			$endif$ */
			o.save();
			PKey p = o.getPKey();
			if(p!=null) {
				o.cached=false;
				__deactive(p,o.oldHashCode());
			}
		}
		catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(-1);
		}
	}
	
	protected void addMetadata(Metadata m) {
		try {
			active_map_lock.lock();
			active_queue_lock.lock();
			if(metas.containsKey(m.getDBID())) {
				try {
					throw new RuntimeException("what happened?");
				} catch (Exception e) {
					jprime.Console.err.printStackTrace(e);
					jprime.Console.halt(-1);
				}
			}
			metas.put(m.getDBID(),m);
		} finally {
			active_queue_lock.unlock();
			active_map_lock.unlock();
		}
	}

	protected void addExperiment(Experiment m) {
		try {
			active_map_lock.lock();
			active_queue_lock.lock();
			if(exps.containsKey(m.getName())) {
				try {
					throw new RuntimeException("what happened?");
				} catch (Exception e) {
					jprime.Console.err.printStackTrace(e);
					jprime.Console.halt(-1);
				}
			}
			exps.put(m.getName(),m);
		} finally {
			active_queue_lock.unlock();
			active_map_lock.unlock();
		}
	}

	protected Metadata getMetadata(long id) {
		try {
			Metadata rv = null;
			active_map_lock.lock();
			active_queue_lock.lock();
			if(metas.containsKey(id)) {
				rv=metas.get(id);
			}
			return rv;
		} finally {
			active_queue_lock.unlock();
			active_map_lock.unlock();
		}
	}

	protected Experiment getExperiment(String name) {
		try {
			Experiment rv = null;
			active_map_lock.lock();
			active_queue_lock.lock();
			if(exps.containsKey(name)) {
				rv=exps.get(name);
			}
			return rv;
		} finally {
			active_queue_lock.unlock();
			active_map_lock.unlock();
		}
	}

	protected void removeMetadata(long id) {
		try {
			active_map_lock.lock();
			active_queue_lock.lock();
			metas.remove(id);
		} finally {
			active_queue_lock.unlock();
			active_map_lock.unlock();
		}
	}

	protected void removeLibExp(String name) {
		try {
			active_map_lock.lock();
			active_queue_lock.lock();
			exps.remove(name);
		} finally {
			active_queue_lock.unlock();
			active_map_lock.unlock();
		}
	}
}
