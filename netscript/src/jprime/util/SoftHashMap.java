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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


/**
 * @author Nathanael Van Vorst
 *
 * @param <K>
 * @param <V>
 */
public abstract class SoftHashMap<K, V> implements Map<K, V>{
	/** The internal HashMap that will hold the SoftReference. */
	protected final Map<K,SoftReference<V>> hash;

	/**
	 * 
	 */
	public SoftHashMap() {
		hash = new HashMap<K,SoftReference<V>>();		
	}

	/**
	 * @param initialCapacity
	 */
	public SoftHashMap(int initialCapacity) {
		hash = new HashMap<K,SoftReference<V>>(initialCapacity);
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public SoftHashMap(int initialCapacity, float loadFactor) {
		hash = new HashMap<K,SoftReference<V>>(initialCapacity,loadFactor);
	}

	public void clear() {
		hash.clear();
	}

	public boolean containsKey(Object key) {
		if(key == null) throw new RuntimeException("Do not support null keys!");
		return hash.containsKey(key);
	}

	public boolean containsValue(Object value) {
		if(value == null) throw new RuntimeException("Do not support null values!");
		return hash.containsValue(value);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new RuntimeException("Do not support entrySet() -- use keySet() or values()");
	}

	public Set<K> keySet() {
		Set<K> rv = new HashSet<K>();
		Set<K> dead = new HashSet<K>();
		for(java.util.Map.Entry<K, SoftReference<V>> e: hash.entrySet()) {
			if(e.getValue().get()!=null)
				rv.add(e.getKey());
			else
				dead.add(e.getKey());
		}
		for(K key : dead)
			reload(key);
		return rv;
	}

	@SuppressWarnings("unchecked")
	public V get(Object key) {
		SoftReference<V> sf = hash.get(key);
		V rv = null;
		if(sf==null) {
			rv = reload((K)key);
		}
		else {
			rv = sf.get();
			if(rv == null) {
				rv = reload((K)key);
			}
		}
		return rv;
	}

	public boolean isEmpty() {
		return hash.isEmpty();
	}

	public V put(K key, V value) {
		if(key == null) throw new RuntimeException("Do not support null keys!");
		if(value == null) throw new RuntimeException("Do not support null values!");
		SoftReference<V> rv = hash.put(key, new SoftReference<V>(value));
		if(rv!=null)  {
			return rv.get();
		}
		return null;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		for(java.util.Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(),e.getValue());
		}
	}

	public V remove(Object key) {
		SoftReference<V> rv = hash.remove(key);
		if(rv == null) {
			return null;
		}
		return rv.get();
	}

	public int size() {
		return hash.size();
	}

	public Collection<V> values() {
		Collection<V> rv = new LinkedList<V>();
		Set<K> dead = new HashSet<K>();
		for(java.util.Map.Entry<K, SoftReference<V>> e: hash.entrySet()) {
			
			if(e.getValue()!=null && e.getValue().get()!=null)
				rv.add(e.getValue().get());
			else
				dead.add(e.getKey());
		}
		for(K key : dead) {
			V t = reload(key);
			if(t!=null)
				rv.add(t);
		}
		return rv;
	}
	
	abstract protected V reload(K key);
}
