package jprime.database;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jprime.PersistableObject;
import jprime.Net.INet;

/**
 * @author Nathanael Van Vorst
 *
 */
public class CacheQueue implements Iterable<PersistableObject>{
	private final List<PersistableObject> q1, q2;
	private final int cap1,cap2;

	protected CacheQueue(int cap1, int cap2) {
		this.q1=new LinkedList<PersistableObject>();
		this.q2=new LinkedList<PersistableObject>();
		this.cap1=cap1;
		this.cap2=cap2;
	}
	public CacheQueue(int cap) {
		this((int)Math.floor(cap*.75),cap-(int)Math.floor(cap*.75));
	}
	
	/*
	public PersistableObject poll() {
		if(q1.size()>0)
			return q1.remove(0);
		return q2.remove(0);
	}
	*/
	public PersistableObject poll() {
		while(q1.size()>0 && q2.size()<cap2) {
			final PersistableObject rv = (q1.size()>0)?q1.remove(0):q2.remove(0);
			if((rv.hasBeenReplicated() || rv instanceof INet) && q2.size() < cap2)
				q2.add(rv);
			else
				return rv;
		}
		return (q1.size()>0)?q1.remove(0):q2.remove(0);
	}
	public boolean addObj(PersistableObject e) {
		if(q1.size() <cap1) {
			q1.add(e);
			return true;
		}
		if(q2.size() <cap2) {
			q2.add(e);
			return true;
		}
		return false;
	}
	public void clear() {
		q1.clear();
		q2.clear();
	}
	public boolean isEmpty() {
		return q1.isEmpty() && q2.isEmpty();
	}
	public boolean remove(Object o) {
		if(q1.remove(o))
			return true;
		return q2.remove(o);
	}
	public int size() {
		return q1.size()+q2.size();
	}
	public Iterator<PersistableObject> iterator() {
		return new Iterator<PersistableObject>() {
			Iterator<PersistableObject> i1 = q1.iterator();
			Iterator<PersistableObject> i2 = q2.iterator();
			public boolean hasNext() {
				if(i1.hasNext()) return true;
				return i2.hasNext();
			}
			public PersistableObject next() {
				if(i1.hasNext()) return i1.next();
				return i2.next();
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
