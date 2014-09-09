package jprime.variable;

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/* $if SEPARATE_PROP_TABLE $
import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.database.PKey;
import jprime.variable.Dataset.SimpleDatum;
import jprime.visitors.TLVVisitor.TLVType;
$else$ */
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.PersistableObject.Modified;
import jprime.variable.Dataset.SimpleDatum;
import jprime.visitors.TLVVisitor.TLVType;
/* $endif$ */


/**
 * @author Nathanael Van Vorst
 */
public abstract class ModelNodeVariable extends jprime.gen.ModelNodeVariable implements Cloneable, Serializable {
	private static final long serialVersionUID = -8390685073726040644L;

	public static class VizAttr {
		public final boolean isSym;
		public final String value;
		public final VarDesc desc;
		public VizAttr(String value, VarDesc desc,boolean isSym) {
			super();
			this.value = value;
			this.desc = desc;
			this.isSym=isSym;
		}
	}
	private final static ArrayList<Integer> __varids__=new ArrayList<Integer>();
	static {
		for(Integer i : jprime.gen.ModelNodeVariable.str2id.values()) {
			__varids__.add(i);
		}
		Collections.shuffle(__varids__);
	}
	public static Integer rs_edge_interfaces(){return -1;}
	public static boolean shouldEncodeAttr(int id) {
		if(id == -1)
			return false;
		return true;
	}
	
	/* $if SEPARATE_PROP_TABLE $
	//transient
	protected Metadata meta=null;
	
	//persisted
	protected long dbid;
	protected long db_metadata_id;
	protected long db_owner_id;
	protected int db_type;
	protected int db_name;
	protected String value;
	$else$ */
	transient protected ModelNode owner;
	protected int db_name;
	/* $endif$ */

	

	/* $if SEPARATE_PROP_TABLE $
	protected ModelNodeVariable(Metadata meta, long dbid, long db_owner_id,
			int db_type, int db_name, String value) {
		super();
		this.dbid=dbid;
		this.db_metadata_id = meta.getDBID();
		this.meta=meta;
		this.db_owner_id = db_owner_id;
		this.db_type = db_type;
		this.db_name = db_name;
		this.value = value;
		this.persistable_state=PersistableState.UNMODIFIED;
		meta.loaded(this);
	}
	$else$ */
	protected ModelNodeVariable(ModelNode owner, int db_name) {
		this.owner=owner;
		this.db_name=db_name;
	}
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	protected ModelNodeVariable(int db_type, int db_name) {
		this.db_name=db_name;
		this.db_type=db_type;
		this.value=null;
		this.db_metadata_id=0;
		this.db_owner_id=0;
		this.persistable_state=PersistableState.NEW;
	}
	$else$ */
	protected ModelNodeVariable(int db_name) {
		this.owner=null;
		this.db_name=db_name;
	}
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	protected ModelNodeVariable() {
		this.db_name=-1;
		this.db_type=-1;
		this.value=null;
		this.db_metadata_id=0;
		this.db_owner_id=0;
		this.persistable_state=PersistableState.NEW;
	}
	$else$ */
	protected ModelNodeVariable() {
		this.owner=null;
		this.db_name=-1;
	}
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	protected ModelNodeVariable(int db_type, int db_name, String value) {
		this.db_name=db_name;
		this.db_type=db_type;
		this.value=value;
		this.db_metadata_id=0;
		this.db_owner_id=0;
		this.persistable_state=PersistableState.NEW;
	}
	$endif$ */
	
	
	/* $if SEPARATE_PROP_TABLE $
	public boolean savable() {
		return db_metadata_id!=0 && db_owner_id!=0;
	}
	$endif$ */

	/* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    
	/* $if SEPARATE_PROP_TABLE $
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ModelNodeVariable) {
			return this.getDBMetaID() == ((ModelNodeVariable)obj).getDBMetaID() && getDBID() ==  ((ModelNodeVariable)obj).getDBID();
		}
		return false;
	}
	$else$ */
    abstract public boolean equals(Object obj);
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public Metadata getMetadata() {
		return meta;
	}
	$endif$ */
    
	/* $if SEPARATE_PROP_TABLE $
	public PKey getPKey() {
		return new PKey(db_metadata_id, dbid);
	}
	$endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public long getOwnerID() {
		if(db_owner_id==0)
			throw new RuntimeException("never set my owner!");
		return db_owner_id;
	}
	$endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	@Override
	protected void finalize() throws Throwable {
		if(savable())
			meta.collect(this);
		super.finalize();
	}
	$endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public synchronized void save() {
		if(savable())
			meta.save(this);
	}
	$endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public long getDBMetaID() {
		if(db_metadata_id==0)
			throw new RuntimeException("never set my owner!");
		return db_metadata_id;
	}
	$endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public long getDBID() {
		if(dbid==0)
			throw new RuntimeException("never set my owner!");
		return dbid;
	}
	$endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public String toString() {
		return value;
	}
	$else$ */
	abstract public String toString();
	/* $endif$ */
	
	/**
	 * @param db_name
	 */
	public void setDBName(int db_name) {
		this.db_name=db_name;
	}
	
	/**
	 * @return
	 */
	public int getDBName() {
		return this.db_name;
	}
	
	/**
	 * @param owner
	 */
	/* $if SEPARATE_PROP_TABLE $
	public void setOwner(ModelNode owner) {
		if(dbid != 0) {
			if(this.db_owner_id!=owner.getDBID()) {
				try {
					throw new RuntimeException("How did this happen?");
				}
				catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
				}
				jprime.Console.halt(100);
			}
			if(this.meta!=owner.getMetadata()) {
				try {
					throw new RuntimeException("How did this happen?");
				}
				catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
				}
				jprime.Console.halt(100);
			}
			if(this.db_metadata_id!=meta.getDBID()){
				try {
					throw new RuntimeException("How did this happen?");
				}
				catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
				}
				jprime.Console.halt(100);
			}
		}
		else {
			this.db_owner_id=owner.getDBID();
			this.meta=owner.getMetadata();
			this.db_metadata_id=meta.getDBID();
			this.dbid=owner.getMetadata().getNextModelNodeDBID();
			meta.loaded(this);
		}
	}
	$else$ */
	public void setOwner(ModelNode owner) {
		this.owner=owner;
	}
	public ModelNode getOwner() {
		return this.owner;
	}
	/* $endif$ */

	
	/* $if false == SEPARATE_PROP_TABLE $ */
	protected void modified() {
		if(null != owner) owner.modified(Modified.ATTRS);
	}
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public void setValueAsString(String val) {
		this.value=val;
		modified(Modified.ALL);
	}
	$else$ */
	abstract public void setValueAsString(String val);
	/* $endif$ */
	
	/**
	 * @param ds
	 * @return
	 */
	public String getRuntimeValueAsString(IModelNode owner, Dataset ds) {
		if(ds == null)
			return toString();
		SimpleDatum rv = ds.getMostRecentDatum(this.getDBName(), owner.getUID());
		if(rv==null) {
			return toString();
		}
		return rv.value;
	}
	
	/* $if SEPARATE_PROP_TABLE $
	public void orphan() {
		switch(this.persistable_state) {
		case UNMODIFIED:
		case MODIFIED:
			this.persistable_state=PersistableState.DEAD;
			modified(Modified.ALL);
			break;
		case NEW:
			this.persistable_state=PersistableState.ORPHAN;
			modified(Modified.ALL);
			break;
		case DEAD:
			throw new RuntimeException("does this happen? nate things this should be a no-op like orphan,  but he is not sure yet....");
			//this.persistableState=PersistableState.ORPHAN;
			//break;
		case ORPHAN:
			//no op
		}
	}
	$endif$ */
	
	/**
	 * @return
	 */
	public abstract int getTypeId();

	/**
	 * Turn this into a TLV
	 */
	public abstract String encodeTLV();
	
	/**
	 * @return
	 */
	public abstract TLVType getTLVType();
	
	public abstract void flushObject(DataOutputStream out) throws Exception;
	public abstract void initObject(DataInputStream in) throws Exception;
	public abstract int packingSize();

	
	/**
	 * @param m
	 * @param attr_dbname
	 * @return whether this is a default value by the name 'attr_dbname' for the model node m
	 */
	public static boolean hasDefaultValue(ModelNode m,int attr_dbname) {
		return getClsDesc(m).vars.containsKey(attr_dbname);
	}
	
	public static TreeSet<ClsDesc> getAllClsDesc() {
		TreeSet<ClsDesc> rv= new TreeSet<ModelNodeVariable.ClsDesc>(new Comparator<ModelNodeVariable.ClsDesc>() {
			public int compare(ClsDesc o1, ClsDesc o2) {
				if(o1.name.compareTo(o2.name)==0) return 0;
				if(o1.super_desc==null && o1.super_desc==null) {
					return o1.name.compareTo(o2.name);
				}
				else if(o1.super_desc == null) {
					return -1;
				}
				else if(o2.super_desc==null) {
					return 1;
				}
				final int c = o1.super_desc.name.compareTo(o2.super_desc.name);
				if(c==0) {
					return o1.name.compareTo(o2.name);
				}
				return c;
			}
		});
		rv.addAll(clsDescs.values());
		return rv;
	}
	
	public static ClsDesc getClsDesc(Class<?> cls) {
		return clsDescs.get(cls);
	}
	
	/**
	 * @param rand
	 * @return
	 */
	public static int randomVarId(Random rand) {
		return __varids__.get(rand.nextInt(__varids__.size()));
	}
	
	private static final Set<Integer> __auto_or_static_vars__;
	static {
		__auto_or_static_vars__ = new TreeSet<Integer>();
		__auto_or_static_vars__.add(ip_address());
		__auto_or_static_vars__.add(ip_prefix());
		__auto_or_static_vars__.add(ip_prefix_len());
		__auto_or_static_vars__.add(uid());
		__auto_or_static_vars__.add(size());
		__auto_or_static_vars__.add(offset());
		__auto_or_static_vars__.add(mac_address());
		__auto_or_static_vars__.add(name());
	}
	
	/**
	 * @param attr_dbname
	 * @return whether this variable by the name 'attr_dbname' can be set by the user
	 */
	public static boolean userModifiable(int attr_dbname) {
		return ! (__auto_or_static_vars__.contains(attr_dbname)); 
	}
	
	/**
	 * @param m
	 * @param _dbname
	 */
	public void attachToNode(ModelNode m, int _dbname) {
		//only used for copying symbols
	}

}
