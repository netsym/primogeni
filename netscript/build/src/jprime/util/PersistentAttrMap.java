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
/* $if SEPARATE_PROP_TABLE $

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.variable.ModelNodeVariable;

$else$ */

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jprime.EntityFactory;
import jprime.ModelNode;
import jprime.PersistableObject.Modified;
import jprime.variable.BooleanVariable;
import jprime.variable.FloatingPointNumberVariable;
import jprime.variable.IntegerVariable;
import jprime.variable.ListVariable;
import jprime.variable.ModelNodeVariable;
import jprime.variable.OpaqueVariable;
import jprime.variable.ResourceIdentifierVariable;
import jprime.variable.StringVariable;
import jprime.variable.SymbolVariable;

/* $endif$ */
/**
 * @author Nathanael Van Vorst
 *
 */
/* $if SEPARATE_PROP_TABLE $

public class PersistentAttrMap implements Map<Integer, ModelNodeVariable> {
	private static final Comparator<ModelNodeVariable> sorter = new Comparator<ModelNodeVariable>() {
		public int compare(ModelNodeVariable o1, ModelNodeVariable o2) {
			if(o1.getDBName() == o2.getDBName())
				return 0;
			if(o1.getDBName() < o2.getDBName())
				return -1;
			return 1;
		}
	};
	private Metadata meta;
	private long owner_id;
	private final HashMap<Integer,Long> name2dbid;

	private class PA extends SoftHashMap<Long, ModelNodeVariable> {
		@Override
		protected ModelNodeVariable reload(Long key) {
			SoftReference<ModelNodeVariable> v = new SoftReference<ModelNodeVariable>(meta.loadModelNodeVariable(key,owner_id));
			hash.put(key, v);
			return v.get();
		}
	}

	private final PA pa;
	public PersistentAttrMap(Metadata meta, long owner_id) {
		super();
		this.meta = meta;
		this.owner_id=owner_id;
		this.pa=new PA();
		this.name2dbid=new HashMap<Integer, Long>();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public void setMetadata(Metadata m, long owner_id) {
		this.meta=m;
		this.owner_id=owner_id;
	}

	public void clear() {
		pa.clear();
		name2dbid.clear();
	}
	public void addKey(long dbid, long dborder) {
		this.name2dbid.put((int)dborder, dbid);
		this.pa.hash.put(dbid, null);
	}
	public boolean containsKey(Object key) {
		Object key1=name2dbid.get(key);
		if(key1==null) return false;
		return pa.containsKey(key1);
	}
	public boolean containsValue(Object value) {
		return pa.containsValue(value);
	}
	public Set<java.util.Map.Entry<Integer, ModelNodeVariable>> entrySet() {
		throw new RuntimeException("Do not support entrySet() -- use keySet() or values()");
	}
	public ModelNodeVariable get(Object key) {
		Object key1=name2dbid.get(key);
		if(key1==null) return null;
		ModelNodeVariable rv = pa.get(key1);
		if(rv!=null) {
	    	$if DEBUG $
			meta.logAccess(rv);
			$endif$
		}
		return rv;
	}
	public Set<Integer> keySet() {
		return name2dbid.keySet();
	}
	public boolean isEmpty() {
		return pa.isEmpty();
	}
	public ModelNodeVariable put(Integer key, ModelNodeVariable value) {
		Long key1=value.getDBID();
		name2dbid.put(key, key1);
		return pa.put(key1, value);
	}
	public void putAll(Map<? extends Integer, ? extends ModelNodeVariable> m) {
		for(java.util.Map.Entry<? extends Integer, ? extends ModelNodeVariable> e : m.entrySet()) {
			put(e.getKey(),e.getValue());
		}
	}
	public ModelNodeVariable remove(Object key) {
		Object key1=name2dbid.remove(key);
		if(key1==null) return null;
		return pa.remove(key1);
	}
	public int size() {
		return pa.size();
	}
	public Collection<ModelNodeVariable> values() {
		ArrayList<ModelNodeVariable> rv = new ArrayList<ModelNodeVariable>(this.size());
		rv.addAll(pa.values());
		Collections.sort(rv,sorter);
		return rv;
	}

	public ChildIdList getChildIds(ChildIdList rv) {
		for(java.util.Map.Entry<Integer, Long> e : name2dbid.entrySet())
			rv.add(new ChildId(e.getValue(),EntityFactory.ModelNodeVaribaleStart,e.getKey()));
		return rv;
	}
}
$else$ */
public class PersistentAttrMap implements Map<Integer, ModelNodeVariable>, Serializable {
	private static final long serialVersionUID = -7005543945805740244L;
	transient private HashMap<Integer, ModelNodeVariable> attrs = new HashMap<Integer, ModelNodeVariable>();
	transient private ModelNode owner;
	public PersistentAttrMap(ModelNode owner) {
		this.owner=owner;
	}
	private PersistentAttrMap() {
		this.owner=null;
	}
	public void setOwner(ModelNode owner) {
		this.owner=owner;
		for(ModelNodeVariable v : attrs.values())
			v.setOwner(owner);
	}

	public void clear() {
		if(attrs.size()>0) {
			for(ModelNodeVariable v : attrs.values()) {
				if(v instanceof SymbolVariable)
					owner.getMetadata().getSymbolTable().unregisterSymbol(v.toString());
			}
			attrs.clear();
			owner.modified(Modified.ATTRS);
		}
	}

	public Object clone() {
		throw new RuntimeException("dont use this");
	}

	public boolean containsKey(Object arg0) {
		return attrs.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return attrs.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<Integer, ModelNodeVariable>> entrySet() {
		return attrs.entrySet();
	}

	public boolean equals(Object arg0) {
		return attrs.equals(arg0);
	}

	public ModelNodeVariable get(Object arg0) {
		return attrs.get(arg0);
	}

	public int hashCode() {
		return attrs.hashCode();
	}

	public boolean isEmpty() {
		return attrs.isEmpty();
	}

	public Set<Integer> keySet() {
		return attrs.keySet();
	}

	public ModelNodeVariable put(Integer arg0, ModelNodeVariable arg1) {
		arg1.setOwner(owner);
		ModelNodeVariable rv = null;
		synchronized(this) {
			rv = attrs.put(arg0, arg1);
		}
		if(rv!= null) {
			rv.setOwner(null);
			if(rv instanceof SymbolVariable)
				owner.getMetadata().getSymbolTable().unregisterSymbol(rv.toString());
		}
		owner.modified(Modified.ATTRS);
		return rv;
	}

	public void putAll(Map<? extends Integer, ? extends ModelNodeVariable> arg0) {
		for(java.util.Map.Entry<? extends Integer, ? extends ModelNodeVariable> e :arg0.entrySet()) {
			put(e.getKey(),e.getValue());
		}
		owner.modified(Modified.ATTRS);
	}

	public ModelNodeVariable remove(Object arg0) {
		ModelNodeVariable rv = null;
		synchronized(this) {
			rv = attrs.remove(arg0);
		}
		if(rv!= null) {
			rv.setOwner(null);
			if(rv instanceof SymbolVariable)
				owner.getMetadata().getSymbolTable().unregisterSymbol(rv.toString());
		}
		owner.modified(Modified.ATTRS);
		return rv;
	}

	public int size() {
		return attrs.size();
	}

	public String toString() {
		return attrs.toString();
	}

	public Collection<ModelNodeVariable> values() {
		return attrs.values();
	}
	public int packingsize() {
		int size=(Integer.SIZE*(1+2*attrs.size()))/8;
		synchronized(this) {
			for(ModelNodeVariable v :attrs.values()) {
				size+=v.packingSize();
			}
		}
		return size;
	}
	
	public void flushObject(DataOutputStream out) throws Exception { 
		//System.out.println("from PersistentAttrMap.writeObject: backinglist.size()=" + backinglist.size());
		synchronized(this) {
			out.writeInt(attrs.size());
			for(ModelNodeVariable v : attrs.values()) {
				out.writeInt(v.getTypeId());
				out.writeInt(v.getDBName());
				v.flushObject(out);
			}
		}
		//System.out.println("\twrote "+out.size()+" bytes");
	}  
	
	private void initObject(DataInputStream in) throws Exception {  
		//System.out.println("from PersistentAttrMap.readObject: available="+in.available());
		int n = in.readInt();
		for(int i=0;i<n;i++) {
			ModelNodeVariable v=null;
			switch(in.readInt()){
			case EntityFactory.BooleanVariable:
				v = new BooleanVariable();
				break;
			case EntityFactory.IntegerVariable:
				v = new IntegerVariable();
				break;
			case EntityFactory.FloatingPointNumberVariable:
				v = new FloatingPointNumberVariable();
				break;
			case EntityFactory.ListVariable:
				v = new ListVariable();
				break;
			case EntityFactory.OpaqueVariable:
				v = new OpaqueVariable();
				break;
			case EntityFactory.ResourceIdentifierVariable:
				v = new ResourceIdentifierVariable();
				break;
			case EntityFactory.StringVariable:
				v = new StringVariable();
				break;
			case EntityFactory.SymbolVariable:
				v = new SymbolVariable();
				break;
			default:
				throw new RuntimeException("Unknown var type!");
			}
			v.setDBName(in.readInt());
			v.initObject(in);
			this.attrs.put(v.getDBName(), v);
		}
	} 

	public static ByteArrayInputStream toBytes(PersistentAttrMap l) {
		try {
			final NoCopyByteArrayOutputStream bos =new NoCopyByteArrayOutputStream(l.packingsize());
			final DataOutputStream out = new DataOutputStream(bos);
			l.flushObject(out);
			return bos.getBufferAsByteArrayInputStream();
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
	}
	
	public static PersistentAttrMap fromBytes(DataInputStream in) {
		try {
			final PersistentAttrMap rv = new PersistentAttrMap();
			rv.initObject(in);
			return rv;
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
	}

	public static PersistentAttrMap fromBytes(Blob b) {
		try {
			if(b.length()>0) {
				//final NoCopyByteArrayInputStream in = new NoCopyByteArrayInputStream(b.getBytes(1, (int)b.length()));
				final DataInputStream oin = new DataInputStream(b.getBinaryStream(1, (int)b.length()));
				final PersistentAttrMap rv = new PersistentAttrMap();
				rv.initObject(oin);
				return rv;
			}
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
		return new PersistentAttrMap();
	}
}

/* $endif$ */
