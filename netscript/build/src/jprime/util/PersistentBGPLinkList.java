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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.routing.BGPLinkType;


/**
 * @author Nathanael Van Vorst
 *
 */
public class PersistentBGPLinkList implements List<BGPLinkType>{
	/** The internal HashMap that will hold the SoftReference. */
	private final HashMap<Long,SoftReference<BGPLinkType>> hash;
	private final ArrayList<Long> keys;
	
	/** used to re-fetch objects that have been reclaimed **/
	private Metadata meta;

	private class BGPLinkTypeIterator implements java.util.Iterator<BGPLinkType> {
		int idx=-1;
		public boolean hasNext() {
			return (idx+1)<keys.size();
		}

		public BGPLinkType next() {
			if(idx==keys.size()) throw new NoSuchElementException();
			return get(++idx);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	private class BGPLinkTypeListIterator implements ListIterator<BGPLinkType> {
		int idx=-1;

		public void add(BGPLinkType e) {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			return (idx+1)<keys.size();
		}

		public boolean hasPrevious() {
			return idx>0;
		}

		public BGPLinkType next() {
			if(idx==keys.size()) throw new NoSuchElementException();
			return get(++idx);
		}

		public int nextIndex() {
			if(idx>=keys.size()) return keys.size();
			return idx+1;
		}

		public BGPLinkType previous() {
			if(idx<=0) throw new NoSuchElementException();
			return get(--idx);
		}

		public int previousIndex() {
			if(idx<=0) return -1;
			return idx-1;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void set(BGPLinkType e) {
			throw new UnsupportedOperationException();
		}		
	}
	public PersistentBGPLinkList(Metadata meta) {
		super();
		this.hash = new HashMap<Long, SoftReference<BGPLinkType>>();
		this.keys = new ArrayList<Long>();
		this.meta=meta;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public void add(int arg0, BGPLinkType arg1) {
		throw new UnsupportedOperationException();
	}
	
	public void setMetadata(Metadata m) {
		this.meta=m;
	}

	public void addKey(long dbid) {
		this.keys.add(dbid);
	}

	public boolean add(BGPLinkType m) {
		Long k = m.getDBID();
		if(hash.containsKey(k))
			return false;
		hash.put(k,new SoftReference<BGPLinkType>(m));
		keys.add(k);
		//XXX could be more effiecient
		Collections.sort(keys);
		return true;
	}

	public boolean addAll(Collection<? extends BGPLinkType> arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int arg0, Collection<? extends BGPLinkType> arg1) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object m) {
		if(m instanceof BGPLinkType) {
			Long k = ((BGPLinkType)m).getDBID();
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

	public BGPLinkType get(int index) {
		Long k = keys.get(index);
		SoftReference<BGPLinkType> rv = hash.get(k);
		if(rv==null || rv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=new SoftReference<BGPLinkType>(meta.loadBGPLinkType(k));
			hash.put(k,rv);
		}
		return rv.get();
	}

	public int indexOf(Object k) {
		return Collections.binarySearch(keys, (Long)k);
	}

	public boolean isEmpty() {
		return hash.isEmpty();
	}

	public Iterator<BGPLinkType> iterator() {
		return new BGPLinkTypeIterator();
	}

	public int lastIndexOf(Object arg0) {
		return indexOf(arg0);
	}

	public ListIterator<BGPLinkType> listIterator() {
		return new BGPLinkTypeListIterator();
	}

	public ListIterator<BGPLinkType> listIterator(int idx) {
		BGPLinkTypeListIterator rv = new BGPLinkTypeListIterator();
		while(rv.idx<keys.size() && (rv.idx+1)<idx)
			rv.nextIndex();
		return rv;
	}

	public BGPLinkType remove(int arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public BGPLinkType set(int arg0, BGPLinkType arg1) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return keys.size();
	}

	public ChildIdList getChildIds() {
		ChildIdList rv = new ChildIdList();
		for(long k : keys) {
			rv.add(new ChildId(k, EntityFactory.BGPLinkType));
		}
		return rv;
	}

	public List<BGPLinkType> subList(int start, int end) {
		ArrayList<BGPLinkType> rv = new ArrayList<BGPLinkType>(end-start);
		for(;start<end;start++)
			rv.add(get(start));
		return rv;
	}

	public Object[] toArray() {
		ArrayList<BGPLinkType> rv = new ArrayList<BGPLinkType>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		ArrayList<BGPLinkType> rv = new ArrayList<BGPLinkType>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray(arg0);
	}	
}
