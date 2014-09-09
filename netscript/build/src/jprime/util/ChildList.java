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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jprime.IModelNode;
import jprime.PersistableObject;
import jprime.database.PKey;
import jprime.gen.EntityFactory;


/**
 * @author Nathanael Van Vorst track of how many children are of a specific sub_type and allows them to be iterated over
 *
 * @param <T> the type of children this child list has
 */
public class ChildList<T extends IModelNode> implements List<T> {
	private final IModelNode owner;
	private final int min_child_count, max_child_count;
	
	private final ArrayList<Integer> indexes;
	
	/*
	 * @author Nathanael Van Vorst
	 * The iterator for this child list
	 */
	public class ChildIterator implements java.util.Iterator<T> {
		final PersistentChildList children=owner.getPersistentChildList();
		private final Iterator<Integer> i = indexes.iterator();
		/**
		 * the constructor
		 */
		protected ChildIterator() {
		}
		
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return i.hasNext();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			throw new RuntimeException("Not allowed to remove children via an iterator!");
		}
		
		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@SuppressWarnings("unchecked")
		public T next() {
			Object c = children.get(i.next());
			try {
				return (T)c;
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
			return null;
		}
	}
	/*
	 * @author Nathanael Van Vorst
	 * The iterator for this child list
	 */
	public class ChildListIterator implements java.util.ListIterator<T> {
		final PersistentChildList children=owner.getPersistentChildList();
		private final ListIterator<Integer> i;
		/**
		 * the constructor
		 */
		protected ChildListIterator() {
			i = indexes.listIterator();
		}
		protected ChildListIterator(int idx) {
			i = indexes.listIterator(idx);
		}
		public void add(T arg0) {
			throw new RuntimeException("Not allowed to remove children via an iterator!");
		}
		public boolean hasNext() {
			return i.hasNext();
		}
		public boolean hasPrevious() {
			return i.hasPrevious();
		}
		@SuppressWarnings("unchecked")
		public T next() {
			return (T)children.get(i.next());
		}
		public int nextIndex() {
			return i.nextIndex();
		}
		@SuppressWarnings("unchecked")
		public T previous() {
			return (T)children.get(i.previous());
		}
		public int previousIndex() {
			return i.previousIndex();
		}
		public void remove() {
			throw new RuntimeException("Not allowed to remove children via an iterator!");
		}
		public void set(T arg0) {
			throw new RuntimeException("Not allowed to remove children via an iterator!");
		}
	}

	
	/**
	 * the constructor
	 * 
	 * @param children the main list which stores all children
	 * @param impl_id the type id of the type of child in this list
	 */
	public ChildList(IModelNode owner, int impl_id, int min_child_count, int max_child_count) {
		final PersistentChildList children=owner.getPersistentChildList();
		this.owner=owner;
		this.max_child_count=max_child_count;
		this.min_child_count=min_child_count;
		this.indexes = new ArrayList<Integer>(children.size());
		for(int i=0;i<children.size();i++) {
			if(EntityFactory.isSubType(impl_id, children.getTypeAt(i))) {
				indexes.add(i);
			}
		}
	}
	
	/**
	 * @return an iterator to walk through the children of this type
	 */
	public ChildIterator enumerate() {
		return new ChildIterator();
	}
	
	/**
	 * @return the number of children of this type
	 */
	public int size() {
		return indexes.size();
	}
	
	/**
	 * If the min/max isn't meet it should throw an error!
	 */
	public void enforceChildConstraints(){
		if(min_child_count>0 && indexes.size() < min_child_count) {
			//XXX need to get the child list name somehow....
			throw new RuntimeException("min child count violated....");
		}
		if(max_child_count>0 && indexes.size() > max_child_count) {
			//XXX need to get the child list name somehow....
			throw new RuntimeException("max child count violated....");
		}
	}

	public boolean add(T e) {
		throw new RuntimeException("Not allowed to add children via a child sub-list!");
	}

	public void add(int index, T element) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public boolean addAll(Collection<? extends T> c) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public void clear() {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public boolean contains(Object o) {
		if(o instanceof PersistableObject) {
			final PersistentChildList children=owner.getPersistentChildList();
			PersistableObject oo = (PersistableObject)o;
			PKey p = oo.getPKey();
			if(p.metaid == owner.getMetadata().getDBID()) {
				for(int i :indexes) {
					if(children.getDbIdAt(i)==p.dbid)
						return true;
				}
			}
			return false;
		}
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		for(Object o:c) {
			if(!contains(o))
				return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		return (T)owner.getPersistentChildList().get(indexes.get(index));
	}

	public int indexOf(Object o) {
		if(o instanceof PersistableObject) {
			final PersistentChildList children=owner.getPersistentChildList();
			PersistableObject oo = (PersistableObject)o;
			PKey p = oo.getPKey();
			if(p.metaid == owner.getMetadata().getDBID()) {
				for(int i =0;i<indexes.size();i++) {
					if(children.getDbIdAt(indexes.get(i))==p.dbid)
						return i;
				}
			}
			return -1;
		}
		return -1;
	}

	public boolean isEmpty() {
		return indexes.size()==0;
	}

	public Iterator<T> iterator() {
		return new ChildIterator();
	}

	public int lastIndexOf(Object o) {
		if(o instanceof PersistableObject) {
			final PersistentChildList children=owner.getPersistentChildList();
			PersistableObject oo = (PersistableObject)o;
			PKey p = oo.getPKey();
			if(p.metaid == owner.getMetadata().getDBID()) {
				for(int i =indexes.size()-1;i>=0;i--) {
					if(children.getDbIdAt(indexes.get(i))==p.dbid)
						return i;
				}
			}
			return -1;
		}
		return -1;
	}

	public ListIterator<T> listIterator() {
		return new ChildListIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return new ChildListIterator(index);
	}

	public boolean remove(Object o) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public T remove(int index) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public T set(int index, T element) {
		throw new RuntimeException("Not allowed to modify a child sub-list!");
	}

	public List<T> subList(int fromIndex, int toIndex) {
		List<T> rv = new ArrayList<T>(1+toIndex-fromIndex);
		for(int i=fromIndex;i<toIndex;i++) {
			rv.add(get(i));
		}
		return rv;
	}

	public Object[] toArray() {
		Object[] rv = new Object[indexes.size()];
		for(int i=0;i<indexes.size();i++) {
			rv[i]=get(i);
		}
		return rv;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T[] toArray(T[] a) {
		if(a.length!=indexes.size()) {
			a=(T[])Array.newInstance(a.getClass(), indexes.size());
		}
		for(int i=0;i<indexes.size();i++) {
			a[i]=(T)get(i);
		}
		return a;
	}
}
