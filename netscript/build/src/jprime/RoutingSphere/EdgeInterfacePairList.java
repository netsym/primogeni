package jprime.RoutingSphere;

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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jprime.variable.StringVariable;

/**
 * @author Nathanael Van Vorst
 *
 */
public class EdgeInterfacePairList implements List<EdgeInterfacePair>{
	protected final StringVariable var;
	protected final List<EdgeInterfacePair> edges;
	public EdgeInterfacePairList(StringVariable var,
			List<EdgeInterfacePair> edges) {
		super();
		this.var = var;
		this.edges = edges;
	}
	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(EdgeInterfacePair e) {
		boolean rv = edges.add(e);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, EdgeInterfacePair element) {
		edges.add(index, element);
		var.setValue(EdgeInterfacePair.toBytes(edges));
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends EdgeInterfacePair> c) {
		boolean rv = edges.addAll(c);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends EdgeInterfacePair> c) {
		boolean rv = edges.addAll(index, c);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		edges.clear();
		var.setValue(EdgeInterfacePair.toBytes(edges));
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return edges.contains(o);
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {
		return edges.containsAll(c);
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return edges.equals(o);
	}
	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public EdgeInterfacePair get(int index) {
		return edges.get(index);
	}
	
	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	public int hashCode() {
		throw new RuntimeException("Dont use this!");
	}
	
	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return edges.indexOf(o);
	}
	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return edges.isEmpty();
	}
	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<EdgeInterfacePair> iterator() {
		return edges.iterator();
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return edges.lastIndexOf(o);
	}
	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<EdgeInterfacePair> listIterator() {
		return edges.listIterator();
	}
	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<EdgeInterfacePair> listIterator(int index) {
		return edges.listIterator(index);
	}
	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public EdgeInterfacePair remove(int index) {
		EdgeInterfacePair rv = edges.remove(index);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		boolean rv = edges.remove(o);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		boolean rv = edges.removeAll(c);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c) {
		boolean rv = edges.retainAll(c);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public EdgeInterfacePair set(int index, EdgeInterfacePair element) {
		EdgeInterfacePair rv = edges.set(index, element);
		var.setValue(EdgeInterfacePair.toBytes(edges));
		return rv;
	}
	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return edges.size();
	}
	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List<EdgeInterfacePair> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("dont use this!");
	}
	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		throw new RuntimeException("dont use this!");
	}
	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException("dont use this!");
	}
}