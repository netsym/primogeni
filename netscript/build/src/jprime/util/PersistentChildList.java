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
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jprime.Metadata;
import jprime.ModelNode;
import jprime.database.ChildId;
import jprime.database.ChildIdList;


/**
 * @author Nathanael Van Vorst
 *
 */
public class PersistentChildList implements List<ModelNode>{
	public static final class Key implements Comparable<Key>{
		final long dbid;
		final int dborder;
		final int type;

		public Key(long dbid, int dborder,int type) {
			super();
			this.dbid = dbid;
			this.dborder = dborder;
			this.type=type;
		}

		public Key(ModelNode m) {
			super();
			this.dbid = m.getDBID();
			this.dborder = m.getDBOrder();
			this.type=m.getTypeId();
		}

		public int compareTo(Key o) {
			return (int)(dbid - o.dbid); 
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Key) {
				if(((Key)obj).dbid == dbid)
					return true;
			}
			return false;
		}
		
	}
	private class ModelNodeIterator implements java.util.Iterator<ModelNode> {
		final long my_mod_count=mod_count; //used to detect concurrent modifications
		final Iterator<Key> i = keys.iterator();
		public boolean hasNext() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.hasNext();
		}

		public ModelNode next() {
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
	private class ModelNodeListIterator implements ListIterator<ModelNode> {
		final long my_mod_count=mod_count; //used to detect concurrent modifications
		final ListIterator<Key> i;
		public ModelNodeListIterator() {
			i = keys.listIterator();
		}
		public ModelNodeListIterator(int idx) {
			i = keys.listIterator(idx);
		}
		public void add(ModelNode e) {
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

		public ModelNode next() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return get(i.next());
		}

		public int nextIndex() {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			return i.nextIndex();
		}

		public ModelNode previous() {
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

		public void set(ModelNode e) {
			if(my_mod_count != mod_count)
				throw new ConcurrentModificationException();
			throw new UnsupportedOperationException();
		}		
	}
	private static final Comparator<Key> c = new Comparator<Key>() {
		public int compare(Key o1, Key o2) {
			if(o1.dborder==o2.dborder)
				return 0;
			if(o1.dborder<o2.dborder)
				return -1;
			return 1;
		}
	};
	private long mod_count; //used to detect concurrent modifications

	/** The internal HashMap that will hold the SoftReference. */
	private final HashMap<Long,SoftReference<ModelNode>> hash;
	
	/** used to order children & store dbid **/
	private final ArrayList<Key> keys;
	
	/** used to search children by name **/
	//NATE private final HashMap<String,Key> name2key;
	
	/** used to re-fetch objects that have been reclaimed **/
	private Metadata meta;

	public PersistentChildList(Metadata meta) {
		super();
		this.hash = new HashMap<Long, SoftReference<ModelNode>>();
		//NATE this.name2key = new HashMap<String, Key>();
		this.keys = new ArrayList<Key>();
		this.meta=meta;
		this.mod_count=0;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public void add(int arg0, ModelNode arg1) {
		mod_count++;
		throw new UnsupportedOperationException();
	}
	
	public void setMetadata(Metadata m) {
		mod_count++;
		this.meta=m;
	}

	public void addKey(long dbid, int dborder, int type) {
		mod_count++;
		this.keys.add(new Key(dbid,dborder,type));
		/* NATE
		if(this.name2key.size()==0) {
			this.keys.add(new Key(dbid,dborder,type));
		}
		else {
			throw new RuntimeException("can't add keys once you add a real child!");
		}*/
	}

	public boolean add(ModelNode m) {
		mod_count++;
		final Key k = new Key(m);
		if(hash.containsKey(k)) {
			throw new RuntimeException("what happended?");
			//return false;
		}
		if(null!=get(m.getName(),false)) { //this will load the names if they are not aleady loaded!
			throw new RuntimeException("what happended?");
			//return false;
		}
		hash.put(k.dbid,new SoftReference<ModelNode>(m));
		//NATE name2key.put(m.getName(),k);
		keys.add(k);
		//XXX could be more effiecient
		Collections.sort(keys, c);
		return true;
	}

	public boolean addAll(Collection<? extends ModelNode> arg0) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int arg0, Collection<? extends ModelNode> arg1) {
		mod_count++;
		throw new UnsupportedOperationException();
	}

	public void clear() {
		mod_count++;
		keys.clear();
		//NATE name2key.clear();
		hash.clear();
	}

	public boolean contains(Object m) {
		if(m instanceof ModelNode) {
			final Key k = new Key((ModelNode)m);
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

	public ModelNode get(String name, boolean except) {
		/* NATE if(name2key.size()==0 && keys.size()>0) {
			//need to load keys!
			for(int i=0;i<keys.size();i++) {
				name2key.put(get(i).getName(), keys.get(i));
			}
		} 
		ModelNode m=null;
		Key k = name2key.get(name);
		if(k != null) {
			SoftReference<ModelNode> rv = hash.get(k.dbid);
			m=(rv==null)?null:rv.get();
			if(m==null) {
				//it was reclaimed -- we need to reload from the DB
				m=meta.loadModelNode(k.dbid);
				rv=new SoftReference<ModelNode>(m);
				hash.put(k.dbid,rv);
				if(m==null) {
					throw new RuntimeException("what happened?");
				}
			}
		}
		*/
		ModelNode m=null;
		for(int i=0;i<keys.size();i++) {
			m=get(i);
			if(m.getName().equals(name)) {
				break;
			}
			else m=null;
		}

		if(except && m == null) {
			throw new RuntimeException("invalid child name "+name);
		}
    	/* $if DEBUG $
		if(m!=null)	{
			meta.logAccess(m);
		}
		$endif$ */
		return m;
	}
	
	public int getTypeAt(int index) {
		Key k = keys.get(index);
		return k.type;
	}

	public long getDbIdAt(int index) {
		Key k = keys.get(index);
		return k.dbid;
	}
	
	public ModelNode get(int index) {
		ModelNode rv = null;
		Key k = keys.get(index);
		SoftReference<ModelNode> srv = hash.get(k.dbid);
		if(srv==null || srv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=meta.loadModelNode(k.dbid);
			srv=new SoftReference<ModelNode>(rv);
			hash.put(k.dbid,srv);
		}
		else {
			rv = srv.get();
		}
    	/* $if DEBUG $
		if(rv!=null) {
			meta.logAccess(rv);
		}
		$endif$ */
		return rv;
	}
	
	public ModelNode get(Key k) {
		ModelNode rv = null;
		SoftReference<ModelNode> srv = hash.get(k.dbid);
		if(srv==null || srv.get()==null) {
			//it was reclaimed -- we need to reload from the DB
			rv=meta.loadModelNode(k.dbid);
			srv=new SoftReference<ModelNode>(rv);
			hash.put(k.dbid,srv);
		}
		else {
			rv = srv.get();
		}
    	/* $if DEBUG $
		if(rv!=null) {
			meta.logAccess(rv);
		}
		$endif$ */
		return rv;
	}
	
	public int indexOf(Object arg0) {
		Key k = new Key((ModelNode)arg0);
		return Collections.binarySearch(keys, k, c);
	}

	public boolean isEmpty() {
		return hash.isEmpty();
	}

	public Iterator<ModelNode> iterator() {
		return new ModelNodeIterator();
	}

	public int lastIndexOf(Object arg0) {
		return indexOf(arg0);
	}

	public ListIterator<ModelNode> listIterator() {
		return new ModelNodeListIterator();
	}

	public ListIterator<ModelNode> listIterator(int idx) {
		return new ModelNodeListIterator(idx);
	}

	public ModelNode remove(int idx) {
		mod_count++;
		final Key k = keys.remove(idx);
		if(k == null) throw new RuntimeException("How did this happen?");
		final ModelNode m = get(k);
		//NATE name2key.remove(m.getName());
		keys.remove(idx);
		hash.remove(k.dbid);
		return m;
	}

	public boolean remove(Object o) {
		mod_count++;
		if(o instanceof ModelNode) {
			final ModelNode m = (ModelNode)o;
			//NATE final Key k = name2key.remove(m.getName());
			Key k=null;
			for(int i=0;i<keys.size();i++) {
				k=keys.get(i);
				if(m.getDBID() == k.dbid) {
					break;
				}
				else k=null;
			}
			keys.remove(k);
			hash.remove(k.dbid);
			return true;
		}
		else if(o instanceof Key) {
			final Key k = (Key)o;
			//final ModelNode m = get(k);
			//NATE name2key.remove(m.getName());
			keys.remove(k);
			hash.remove(k.dbid);
			return true;
		}
		return false;
	}

	public boolean removeAll(Collection<?> arg0) {
		mod_count++;
		boolean rv = false;
		for(Object i : arg0) {
			rv = rv || remove(i);
		}
		return rv;
	}

	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public ModelNode set(int arg0, ModelNode arg1) {
		throw new UnsupportedOperationException();
	}
	
	public int size() {
		return keys.size();
	}
	
	public ChildIdList getChildIds() {
		ChildIdList rv = new ChildIdList();
		for(Key k :keys) {
			rv.add(new ChildId(k.dbid,k.type,k.dborder));
		}
		return rv;
	}

	public List<ModelNode> subList(int start, int end) {
		ArrayList<ModelNode> rv = new ArrayList<ModelNode>(end-start);
		for(;start<end;start++)
			rv.add(get(start));
		return rv;
	}

	public Object[] toArray() {
		ArrayList<ModelNode> rv = new ArrayList<ModelNode>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		ArrayList<ModelNode> rv = new ArrayList<ModelNode>(keys.size());
		for(int i=0;i<keys.size();i++)
			rv.add(get(i));
		return rv.toArray(arg0);
	}	
}
