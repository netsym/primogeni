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

import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.database.ChildId;
import jprime.database.ChildIdList;


/**
 * @author Nathanael Van Vorst
 *
 */
public class PersistentModelNodeList<T extends IModelNode> implements List<T>{
	/** The internal HashMap that will hold the SoftReference. */
	private final HashMap<Long,SoftReference<ModelNode>> hash;
	private final ArrayList<ID> keys;
	private long mod_count; //used to detect concurrent modifications

	/** used to re-fetch objects that have been reclaimed **/
	private Metadata meta;
	
	public static class ID {
		public final long key;
		public final int type;
		public ID(long key, int type) {
			this.key = key;
			this.type = type;
		}
	}
	
	private class MyIterator implements java.util.Iterator<T> {
		final long my_mod_count=mod_count; //used to detect concurrent modifications
		private final ArrayList<Long> mykeys;
		private final Iterator<Long> i;
		public MyIterator() {
			mykeys=new ArrayList<Long>();
			for(ID id : keys)
				mykeys.add(id.key);
			i=mykeys.iterator();
		}
		public boolean hasNext() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.hasNext();
		}

		public T next() {
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
	private class MyListIterator implements ListIterator<T> {
		final long my_mod_count=mod_count; //used to detect concurrent modifications
		private final ArrayList<Long> mykeys;
		private final ListIterator<Long> i;
		
		public MyListIterator() {
			mykeys=new ArrayList<Long>();
			for(ID id : keys)
				mykeys.add(id.key);
			i = mykeys.listIterator();
		}

		public MyListIterator(int idx) {
			mykeys=new ArrayList<Long>();
			for(ID id : keys)
				mykeys.add(id.key);
			i = mykeys.listIterator(idx);
		}
		
		public void add(T e) {
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

		public T next() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return get(i.next());
		}

		public int nextIndex() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.nextIndex();
		}

		public T previous() {
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

		public void set(T e) {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			throw new UnsupportedOperationException();
		}		
	}
	public PersistentModelNodeList(Metadata meta) {
		super();
		this.hash = new HashMap<Long, SoftReference<ModelNode>>();
		this.keys = new ArrayList<ID>();
		this.meta=meta;
		this.mod_count=0;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public void add(int arg0, T arg1) {
		mod_count++;
		throw new UnsupportedOperationException();
	}
	
	public void setMetadata(Metadata m) {
		mod_count++;
		this.meta=m;
	}

	public void addKey(long dbid, int type) {
		mod_count++;
		this.keys.add(new ID(dbid,type));
	}

	public boolean add(T m) {
		mod_count++;
		if(hash.containsKey(m.getDBID()))
			return false;
		hash.put(m.getDBID(),new SoftReference<ModelNode>((ModelNode)m));
		keys.add(new ID(m.getDBID(),m.getTypeId()));
		return true;
	}

	public boolean addAll(Collection<? extends T> arg0) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public void clear() {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object m) {
		if(m instanceof ModelNode) {
			Long k = ((ModelNode)m).getDBID();
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

	@SuppressWarnings("unchecked")
	public T get(int index) {
		Long k = keys.get(index).key;
		SoftReference<ModelNode> rv = hash.get(k);
		if(rv==null || rv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=new SoftReference<ModelNode>(meta.loadModelNode(k));
			hash.put(k,rv);
		}
		return (T)rv.get();
	}

	@SuppressWarnings("unchecked")
	public T get(Long k) {
		SoftReference<ModelNode> rv = hash.get(k);
		if(rv==null || rv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=new SoftReference<ModelNode>(meta.loadModelNode(k));
			hash.put(k,rv);
		}
		return (T)rv.get();
	}
	
	public int indexOf(Object k) {
		for(int i=0;i<keys.size();i++)
			if(keys.get(i).key==(Long)k)
				return i;
		return -1;
	}

	public boolean isEmpty() {
		return hash.isEmpty();
	}

	public Iterator<T> iterator() {
		return new MyIterator();
	}

	public int lastIndexOf(Object arg0) {
		return indexOf(arg0);
	}

	public ListIterator<T> listIterator() {
		return new MyListIterator();
	}

	public ListIterator<T> listIterator(int idx) {
		return new MyListIterator(idx);
	}

	public T remove(int arg0) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object arg0) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> arg0) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> arg0) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public T set(int arg0, T arg1) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public int size() {
		return keys.size();
	}

	public ChildIdList getChildIds() {
		ChildIdList rv = new ChildIdList();
		for(ID id : keys) {
			rv.add(new ChildId(id.key, id.type));
		}
		return rv;
	}

	public List<T> subList(int start, int end) {
		ArrayList<T> rv = new ArrayList<T>(end-start);
		for(;start<end;start++)
			rv.add(get(start));
		return rv;
	}

	public Object[] toArray() {
		ArrayList<T> rv = new ArrayList<T>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray();
	}

	@SuppressWarnings("unchecked")
	public <TT> TT[] toArray(TT[] arg0) {
		ArrayList<TT> rv = new ArrayList<TT>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add((TT)get(i));
		return rv.toArray(arg0);
	}	
}
