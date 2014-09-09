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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.routing.RouteEntry;


/**
 * @author Nathanael Van Vorst
 *
 */
public class PersistentRouteEntryList implements List<RouteEntry>{
	/** The internal HashMap that will hold the SoftReference. */
	private final HashMap<Long,SoftReference<RouteEntry>> hash;
	private final ArrayList<Long> keys;

	/** used to re-fetch objects that have been reclaimed **/
	private final Metadata meta;
	private final long parent_id;
	private class RouteEntryIterator implements java.util.Iterator<RouteEntry> {
		Iterator<Long> i = keys.iterator();
		public boolean hasNext() {
			return i.hasNext();
		}

		public RouteEntry next() {
			return get(i.next());
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	private class RouteEntryListIterator implements ListIterator<RouteEntry> {
		ListIterator<Long> i;

		public RouteEntryListIterator() {
			i = keys.listIterator();			
		}
		public RouteEntryListIterator(int idx) {
			i = keys.listIterator(idx);
		}
		public void add(RouteEntry e) {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public boolean hasPrevious() {
			return i.hasPrevious();
		}

		public RouteEntry next() {
			return get(i.next());
		}

		public int nextIndex() {
			return i.nextIndex();
		}

		public RouteEntry previous() {
			return get(i.previous());
		}

		public int previousIndex() {
			return i.previousIndex();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void set(RouteEntry e) {
			throw new UnsupportedOperationException();
		}
	}
	public PersistentRouteEntryList(Metadata meta, long parent_id) {
		super();
		this.hash = new HashMap<Long, SoftReference<RouteEntry>>();
		this.keys = new ArrayList<Long>();
		this.meta=meta;
		this.parent_id=parent_id;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	
	public void addKey(long dbid,int order) {
		while(order>=keys.size()) {
			keys.add(null);
		}
		this.keys.set(order, dbid);
	}

	public void add(int idx, RouteEntry m) {
		Long k = m.getDBID();
		keys.add(idx,k);
		hash.put(k,new SoftReference<RouteEntry>(m));
		switch(m.getPersistableState()) {
		case DEAD:
		case ORPHAN:
			m.attach(parent_id);
			break;
		case MODIFIED:
		case NEW:
		case UNMODIFIED:
			if(m.getParent_id()!=parent_id) {
				m.attach(parent_id);
			}
		}
	}
	
	public boolean add(RouteEntry m) {
		Long k = m.getDBID();
		if(hash.containsKey(k))
			return false;
		hash.put(k,new SoftReference<RouteEntry>(m));
		keys.add(k);
		switch(m.getPersistableState()) {
		case DEAD:
		case ORPHAN:
			m.attach(parent_id);
			break;
		case MODIFIED:
		case NEW:
		case UNMODIFIED:
			if(m.getParent_id()!=parent_id) {
				m.attach(parent_id);
			}
		}
		return true;
	}

	public boolean addAll(Collection<? extends RouteEntry> arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int arg0, Collection<? extends RouteEntry> arg1) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		hash.clear();
		keys.clear();
	}

	public boolean contains(Object m) {
		if(m instanceof RouteEntry) {
			Long k = ((RouteEntry)m).getDBID();
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

	public RouteEntry get(int index) {
		if(index<0 || index>=keys.size()) throw new IndexOutOfBoundsException();
		RouteEntry rv = null;
		Long k = keys.get(index);
		if(k==null) {
			throw new RuntimeException("What happended?");
		}
		SoftReference<RouteEntry> srv = hash.get(k);
		if(srv==null || srv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=meta.loadRouteEntry(k);
			srv=new SoftReference<RouteEntry>(rv);
			hash.put(k,srv);
		}
		else {
			rv=srv.get();
		}
    	/* $if DEBUG $
		if(rv!=null) {
			meta.logAccess(rv,parent_id);
		}
		$endif$ */
		return rv;
	}

	public RouteEntry get(Long k) {
		RouteEntry rv = null;
		SoftReference<RouteEntry> srv = hash.get(k);
		if(srv==null || srv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=meta.loadRouteEntry(k);
			srv=new SoftReference<RouteEntry>(rv);
			hash.put(k,srv);
		}
		else {
			rv=srv.get();
		}
    	/* $if DEBUG $
		if(rv!=null) {
			meta.logAccess(rv,parent_id);
		}
		$endif$ */
		return rv;
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

	public Iterator<RouteEntry> iterator() {
		return new RouteEntryIterator();
	}

	public int lastIndexOf(Object arg0) {
		return indexOf(arg0);
	}

	public ListIterator<RouteEntry> listIterator() {
		return new RouteEntryListIterator();
	}

	public ListIterator<RouteEntry> listIterator(int idx) {
		return new RouteEntryListIterator(idx);
	}

	public RouteEntry remove(int index) {
		if(index<0 || index>=keys.size()) throw new IndexOutOfBoundsException();
		Long k = keys.remove(index);
		if(k==null) {
			throw new RuntimeException("What happended?");
		}
		RouteEntry rv=null;
		SoftReference<RouteEntry> srv = hash.remove(k);
		if(srv==null || srv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=meta.loadRouteEntry(k);
		}
		else {
			rv = srv.get();
		}
		return rv;
	}

	public boolean remove(Object m) {
		Long k = ((RouteEntry)m).getDBID();
		if(keys.remove(k)) {
			SoftReference<RouteEntry> srv = hash.remove(k);
			if(srv == null) {
				throw new RuntimeException("how did this happen?");
			}
			return true;
		}
		return false;
	}

	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public RouteEntry set(int arg0, RouteEntry arg1) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return keys.size();
	}

	public ChildIdList getChildIds() {
		ChildIdList rv = new ChildIdList();
		for(int i =0;i<keys.size();i++) {
			Long k = keys.get(i);
			if(k==null) {
				throw new RuntimeException("What happended?");
			}
			rv.add(new ChildId(k, EntityFactory.RouteEntry,i));
		}
		return rv;
	}

	public List<RouteEntry> subList(int start, int end) {
		ArrayList<RouteEntry> rv = new ArrayList<RouteEntry>(end-start);
		for(;start<end;start++)
			rv.add(get(start));
		return rv;
	}

	public Object[] toArray() {
		ArrayList<RouteEntry> rv = new ArrayList<RouteEntry>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		ArrayList<RouteEntry> rv = new ArrayList<RouteEntry>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray(arg0);
	}
	
	public RouteEntry lookupRoute(long srcMin, long srcMax, long dstMin, long dstMax) {
		//XXX how to make faster?
		for(int i=0;i< keys.size();i++) {
			RouteEntry re = get(i);
			
			if(srcMin <= re.getSrcMin() && srcMax>=re.getSrcMax()) {
				if(dstMin <= re.getDstMin() && dstMax>=re.getDstMax())
				{
					return re;
				}
			}
			if(re.getSrcMin()<=srcMin && re.getSrcMax()>=srcMax) {
				if(re.getDstMin()<=dstMin && re.getDstMax()>=dstMax) {
					return re;
				}
			}
		}
		return null;
	}
}
