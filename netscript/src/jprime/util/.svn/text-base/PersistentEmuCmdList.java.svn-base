package jprime.util;

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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jprime.EmulationCommand;
import jprime.EntityFactory;
import jprime.Experiment;
import jprime.Metadata;
import jprime.PersistableObject;
import jprime.Host.Host;
import jprime.Host.HostReplica;
import jprime.Host.IHost;
import jprime.database.ChildId;
import jprime.database.ChildIdList;


/**
 * @author Nathanael Van Vorst
 *
 */
public class PersistentEmuCmdList implements List<EmulationCommand>{
	private long mod_count; //used to detect concurrent modifications
	/** The internal HashMap that will hold the SoftReference. */
	private final HashMap<Long,SoftReference<EmulationCommand>> hash;
	private final ArrayList<Long> keys;

	/** used to re-fetch objects that have been reclaimed **/
	private Metadata meta;
	private PersistableObject parent;
	
	private class EmulationCommandIterator implements java.util.Iterator<EmulationCommand> {
		private final long my_mod_count; //used to detect concurrent modifications
		private final ArrayList<Long> mykeys;
		private final Iterator<Long> i;
		public EmulationCommandIterator() {
			my_mod_count=mod_count;
			mykeys=new ArrayList<Long>();
			mykeys.addAll(keys);
			i=mykeys.iterator();
		}
		public boolean hasNext() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.hasNext();
		}

		public EmulationCommand next() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return get(i.next());
		}

		public void remove() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			throw new UnsupportedOperationException();
		}
		
	}
	private class EmulationCommandListIterator implements ListIterator<EmulationCommand> {
		private final long my_mod_count; //used to detect concurrent modifications
		private final ArrayList<Long> mykeys;
		private final ListIterator<Long> i;
		
		public EmulationCommandListIterator() {
			my_mod_count=mod_count;
			mykeys=new ArrayList<Long>();
			mykeys.addAll(keys);
			i = mykeys.listIterator();
		}

		public EmulationCommandListIterator(int idx) {
			my_mod_count=mod_count;
			mykeys=new ArrayList<Long>();
			mykeys.addAll(keys);
			i = mykeys.listIterator(idx);
		}
		
		public void add(EmulationCommand e) {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.hasNext();
		}

		public boolean hasPrevious() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.hasPrevious();
		}

		public EmulationCommand next() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return get(i.next());
		}

		public int nextIndex() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.nextIndex();
		}

		public EmulationCommand previous() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return get(i.previous());
		}

		public int previousIndex() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.previousIndex();
		}

		public void remove() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			throw new UnsupportedOperationException();
		}

		public void set(EmulationCommand e) {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			throw new UnsupportedOperationException();
		}		
	}
	public PersistentEmuCmdList(Metadata meta, HostReplica parent) {
		super();
		this.hash = new HashMap<Long, SoftReference<EmulationCommand>>();
		this.keys = new ArrayList<Long>();
		this.meta=meta;
		this.parent=parent;
		this.mod_count=0;
	}
	
	public PersistentEmuCmdList(Metadata meta, Host parent) {
		super();
		this.hash = new HashMap<Long, SoftReference<EmulationCommand>>();
		this.keys = new ArrayList<Long>();
		this.meta=meta;
		this.parent=parent;
		this.mod_count=0;
	}
	
	public PersistentEmuCmdList(Metadata meta, Experiment parent) {
		super();
		this.hash = new HashMap<Long, SoftReference<EmulationCommand>>();
		this.keys = new ArrayList<Long>();
		this.meta=meta;
		this.parent=parent;
		this.mod_count=0;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public void add(int arg0, EmulationCommand arg1) {
		this.mod_count++;
		throw new UnsupportedOperationException();
	}
	
	public void setMetadata(Metadata m) {
		this.mod_count++;
		this.meta=m;
	}

	public void addKey(long dbid) {
		this.mod_count++;
		this.keys.add(dbid);
	}

	public boolean add(EmulationCommand m) {
		this.mod_count++;
		Long k = m.getDBID();
		if(hash.containsKey(k))
			return false;
		hash.put(k,new SoftReference<EmulationCommand>(m));
		keys.add(k);
		return true;
	}

	public boolean addAll(Collection<? extends EmulationCommand> arg0) {
		this.mod_count++;
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int arg0, Collection<? extends EmulationCommand> arg1) {
		this.mod_count++;
		throw new UnsupportedOperationException();
	}

	public void clear() {
		this.mod_count++;
		hash.clear();
		keys.clear();
	}

	public boolean contains(Object m) {
		if(m instanceof EmulationCommand) {
			Long k = ((EmulationCommand)m).getDBID();
			if(hash.containsKey(k))
				return true;
		}
		return false;
	}

	public boolean containsAll(Collection<?> arg0) {
		for(Object o : arg0)
			if(!contains(o)) return false;
		return true;
	}

	public EmulationCommand get(int index) {
		Long k = keys.get(index);
		SoftReference<EmulationCommand> rv = hash.get(k);
		if(rv==null || rv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			if(parent instanceof IHost) {
				rv=new SoftReference<EmulationCommand>(meta.loadEmulationCommand(k,(IHost)parent));
			}
			else {
				rv=new SoftReference<EmulationCommand>(meta.loadEmulationCommand(k,(Experiment)parent));
			}
			hash.put(k,rv);
		}
		return rv.get();
	}

	public EmulationCommand get(Long k) {
		SoftReference<EmulationCommand> rv = hash.get(k);
		if(rv==null || rv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			if(parent instanceof IHost) {
				rv=new SoftReference<EmulationCommand>(meta.loadEmulationCommand(k,(IHost)parent));
			}
			else {
				rv=new SoftReference<EmulationCommand>(meta.loadEmulationCommand(k,(Experiment)parent));
			}
			hash.put(k,rv);
		}
		return rv.get();
	}
	
	public int indexOf(Object k) {
		for(int i=0;i<keys.size();i++)
			if(keys.get(i)==(Long)k)
				return i;
		return -1;
	}

	public boolean isEmpty() {
		return hash.isEmpty();
	}

	public Iterator<EmulationCommand> iterator() {
		return new EmulationCommandIterator();
	}

	public int lastIndexOf(Object arg0) {
		return indexOf(arg0);
	}

	public ListIterator<EmulationCommand> listIterator() {
		return new EmulationCommandListIterator();
	}

	public ListIterator<EmulationCommand> listIterator(int idx) {
		return new EmulationCommandListIterator(idx);
	}

	public EmulationCommand remove(int index) {
		this.mod_count++;
		final Long k = keys.remove(index);
		SoftReference<EmulationCommand> rv = hash.remove(k);
		if(rv==null || rv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			if(parent instanceof IHost) {
				rv=new SoftReference<EmulationCommand>(meta.loadEmulationCommand(k,(IHost)parent));
			}
			else {
				rv=new SoftReference<EmulationCommand>(meta.loadEmulationCommand(k,(Experiment)parent));
			}
		}
		return rv.get();
	}

	public boolean remove(Object o) {
		this.mod_count++;
		Integer idx= null;
		final Long k = (o instanceof Long)?((Long)o):((o instanceof EmulationCommand)?((EmulationCommand)o).getDBID():null);
		if(k != null) {
			for(int i =0; i< keys.size(); i++) {
				if(keys.get(i) == k) {
					idx= i;
					break;
				}
			}
		}
		if(idx != null) {
			keys.remove(idx);
			hash.remove(k);
			return true;
		}
		return false;
	}

	public boolean removeAll(Collection<?> os) {
		boolean rv=false;
		for(Object o : os)
			rv = rv || remove(o);
		return rv;
	}

	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public EmulationCommand set(int arg0, EmulationCommand arg1) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return keys.size();
	}

	public ChildIdList getChildIds() {
		ChildIdList rv = new ChildIdList();
		for(long k : keys) {
			rv.add(new ChildId(k, EntityFactory.EmulationCommand));
		}
		return rv;
	}

	public List<EmulationCommand> subList(int start, int end) {
		ArrayList<EmulationCommand> rv = new ArrayList<EmulationCommand>(end-start);
		for(;start<end;start++)
			rv.add(get(start));
		return rv;
	}

	public Object[] toArray() {
		ArrayList<EmulationCommand> rv = new ArrayList<EmulationCommand>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		ArrayList<EmulationCommand> rv = new ArrayList<EmulationCommand>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray(arg0);
	}	
}
