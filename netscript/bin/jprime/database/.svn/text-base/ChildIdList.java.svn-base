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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.Blob;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import jprime.util.NoCopyByteArrayOutputStream;

/**
 * @author Nathanael Van Vorst
 *
 */
public class ChildIdList  implements List<ChildId>{
	transient private final LinkedList<ChildId> backinglist;
	public ChildIdList() {
		this.backinglist = new LinkedList<ChildId>();
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.LinkedList#add(java.lang.Object)
	 */
	public boolean add(ChildId e) {
		return backinglist.add(e);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.LinkedList#add(int, java.lang.Object)
	 */
	public void add(int index, ChildId element) {
		backinglist.add(index, element);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.LinkedList#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends ChildId> c) {
		return backinglist.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.LinkedList#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends ChildId> c) {
		return backinglist.addAll(index, c);
	}

	/**
	 * @param e
	 * @see java.util.LinkedList#addFirst(java.lang.Object)
	 */
	public void addFirst(ChildId e) {
		backinglist.addFirst(e);
	}

	/**
	 * @param e
	 * @see java.util.LinkedList#addLast(java.lang.Object)
	 */
	public void addLast(ChildId e) {
		backinglist.addLast(e);
	}

	/**
	 * 
	 * @see java.util.LinkedList#clear()
	 */
	public void clear() {
		backinglist.clear();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#clone()
	 */
	public Object clone() {
		return backinglist.clone();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.LinkedList#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return backinglist.contains(o);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.AbstractCollection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> arg0) {
		return backinglist.containsAll(arg0);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#descendingIterator()
	 */
	public Iterator<ChildId> descendingIterator() {
		return backinglist.descendingIterator();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#element()
	 */
	public ChildId element() {
		return backinglist.element();
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.AbstractList#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		return backinglist.equals(arg0);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.LinkedList#get(int)
	 */
	public ChildId get(int index) {
		return backinglist.get(index);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#getFirst()
	 */
	public ChildId getFirst() {
		return backinglist.getFirst();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#getLast()
	 */
	public ChildId getLast() {
		return backinglist.getLast();
	}

	/**
	 * @return
	 * @see java.util.AbstractList#hashCode()
	 */
	public int hashCode() {
		return backinglist.hashCode();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.LinkedList#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return backinglist.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	public boolean isEmpty() {
		return backinglist.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.AbstractSequentialList#iterator()
	 */
	public Iterator<ChildId> iterator() {
		return backinglist.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.LinkedList#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return backinglist.lastIndexOf(o);
	}

	/**
	 * @return
	 * @see java.util.AbstractList#listIterator()
	 */
	public ListIterator<ChildId> listIterator() {
		return backinglist.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.LinkedList#listIterator(int)
	 */
	public ListIterator<ChildId> listIterator(int index) {
		return backinglist.listIterator(index);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.LinkedList#offer(java.lang.Object)
	 */
	public boolean offer(ChildId e) {
		return backinglist.offer(e);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.LinkedList#offerFirst(java.lang.Object)
	 */
	public boolean offerFirst(ChildId e) {
		return backinglist.offerFirst(e);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.LinkedList#offerLast(java.lang.Object)
	 */
	public boolean offerLast(ChildId e) {
		return backinglist.offerLast(e);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#peek()
	 */
	public ChildId peek() {
		return backinglist.peek();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#peekFirst()
	 */
	public ChildId peekFirst() {
		return backinglist.peekFirst();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#peekLast()
	 */
	public ChildId peekLast() {
		return backinglist.peekLast();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#poll()
	 */
	public ChildId poll() {
		return backinglist.poll();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#pollFirst()
	 */
	public ChildId pollFirst() {
		return backinglist.pollFirst();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#pollLast()
	 */
	public ChildId pollLast() {
		return backinglist.pollLast();
	}

	/**
	 * @return
	 * @see java.util.LinkedList#pop()
	 */
	public ChildId pop() {
		return backinglist.pop();
	}

	/**
	 * @param e
	 * @see java.util.LinkedList#push(java.lang.Object)
	 */
	public void push(ChildId e) {
		backinglist.push(e);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#remove()
	 */
	public ChildId remove() {
		return backinglist.remove();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.LinkedList#remove(int)
	 */
	public ChildId remove(int index) {
		return backinglist.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.LinkedList#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return backinglist.remove(o);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.AbstractCollection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> arg0) {
		return backinglist.removeAll(arg0);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#removeFirst()
	 */
	public ChildId removeFirst() {
		return backinglist.removeFirst();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.LinkedList#removeFirstOccurrence(java.lang.Object)
	 */
	public boolean removeFirstOccurrence(Object o) {
		return backinglist.removeFirstOccurrence(o);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#removeLast()
	 */
	public ChildId removeLast() {
		return backinglist.removeLast();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.LinkedList#removeLastOccurrence(java.lang.Object)
	 */
	public boolean removeLastOccurrence(Object o) {
		return backinglist.removeLastOccurrence(o);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.AbstractCollection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> arg0) {
		return backinglist.retainAll(arg0);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.LinkedList#set(int, java.lang.Object)
	 */
	public ChildId set(int index, ChildId element) {
		return backinglist.set(index, element);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#size()
	 */
	public int size() {
		return backinglist.size();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see java.util.AbstractList#subList(int, int)
	 */
	public List<ChildId> subList(int arg0, int arg1) {
		return backinglist.subList(arg0, arg1);
	}

	/**
	 * @return
	 * @see java.util.LinkedList#toArray()
	 */
	public Object[] toArray() {
		return backinglist.toArray();
	}

	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.LinkedList#toArray(T[])
	 */
	public <T> T[] toArray(T[] a) {
		return backinglist.toArray(a);
	}

	/**
	 * @return
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		return backinglist.toString();
	}

	public void flushObject(DataOutputStream out) throws Exception { 
		//System.out.println("from ChildIdList.writeObject: backinglist.size()=" + backinglist.size());
		out.writeInt(backinglist.size());
		for(ChildId c : backinglist) {
			out.writeLong(c.child_id);
			out.writeInt(c.type);
			out.writeInt(c.order);
		}
		//System.out.println("\twrote "+out.size()+" bytes");
	}  

	public void initObject(DataInputStream in) throws Exception {  
		//System.out.println("from ChildIdList.readObject: available="+in.available());
		final int n = in.readInt();  
		//System.out.println("\texpect "+n+" child ids, ");
		for(int i=0;i<n;i++) {
			//System.out.println("\t\tchild "+i);
			backinglist.add(new ChildId(in.readLong(), in.readInt(), in.readInt()));
		}
	} 

	private static int byteperchildid = (Integer.SIZE*2+Long.SIZE)/8;
	private static int isize = Integer.SIZE/8;
	public int packingsize() {
		return isize+byteperchildid*size();
	}
	public static ByteArrayInputStream toBytes(ChildIdList l) {
		//use buffering
		try {
			final NoCopyByteArrayOutputStream bos =new NoCopyByteArrayOutputStream(isize+byteperchildid*l.size());
			final DataOutputStream out = new DataOutputStream(bos);
			l.flushObject(out);
			return bos.getBufferAsByteArrayInputStream();
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
	}

	
	public static ChildIdList fromBytes(DataInputStream in) {
		try {
			final ChildIdList rv = new ChildIdList();
			rv.initObject(in);
			return rv;
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
	}
	public static ChildIdList fromBytes(Blob b) {
		try {
			if(b.length()>0) {
				//final NoCopyByteArrayInputStream in = new NoCopyByteArrayInputStream(b.getBytes(1, (int)b.length()));
				final DataInputStream oin = new DataInputStream(b.getBinaryStream(1, (int)b.length()));
				final ChildIdList rv = new ChildIdList();
				rv.initObject(oin);
				return rv;
			}
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
		return new ChildIdList();
	}

}
