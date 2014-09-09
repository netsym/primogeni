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

/* $if SEPARATE_PROP_TABLE $
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.Metadata;
import jprime.variable.Dataset.SimpleDatum;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;
$else$ */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.variable.Dataset.SimpleDatum;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;
/* $endif$ */


/**
 * @author Nathanael Van Vorst
 */
public class OpaqueVariable extends ModelNodeVariable implements Serializable {
	private static final long serialVersionUID = -1849457914637813761L;
	
	/* $if false == SEPARATE_PROP_TABLE $ */
	private String value;
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public OpaqueVariable(Metadata meta, long dbid, long db_owner_id, int db_type, int db_name, String value) {
		super(meta,dbid,db_owner_id,EntityFactory.OpaqueVariable, db_name,value);
	}
	$else$ */
	public OpaqueVariable(ModelNode owner, int db_name, String value) {
		super(owner,db_name);
		this.value=value;
	}
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public OpaqueVariable(int db_name, String value) {
		super(getClassTypeId(),db_name, value);
	}
	$else$ */
	public OpaqueVariable(int db_name, String value) {
		super(db_name);
		this.value=value;
	}
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public OpaqueVariable() {
		super();
	}
	$else$ */
	public OpaqueVariable() {
		super();
		this.value="";
	}
	/* $endif$ */
	
	/* $if false == SEPARATE_PROP_TABLE $ */
	public void setValueAsString(String val) {
		this.value=val;
		modified();
	}
    public boolean equals(Object obj) {
    	return (obj instanceof OpaqueVariable && value.equals(((OpaqueVariable)obj).value));
    }
    public String toString() {
    	return value;
    }
	/* $endif$ */
	
	public void setValue(String v) {
		value=v;
		/* $if SEPARATE_PROP_TABLE $
		modified(Modified.ALL);
		$else$ */
		modified();
		/* $endif$ */
	}
	public String getValue() {
		return value;
	}
	
	
	/**
	 * @param ds
	 * @return
	 */
	public String getRuntimeValue(IModelNode owner, Dataset ds) {
		if(ds == null)
			return getValue();
		SimpleDatum rv = ds.getMostRecentDatum(this.getDBName(), owner.getUID());
		if(rv==null) {
			return getValue();
		}
		return rv.value;
	}

	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#getTypeId()
	 */
	public int getTypeId() {
		return getClassTypeId();
	}
	
	/**
	 * @return
	 */
	public static int getClassTypeId() {
		return EntityFactory.OpaqueVariable;
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#getTLVType()
	 */
	@Override
	public TLVVisitor.TLVType getTLVType() {
		return TLVType.OPAQUE;
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#encodeTLV()
	 */
	@Override
	public String encodeTLV() {
		String attr = TLVVisitor.makeTLV(TLVVisitor.TLVType.LONG,Integer.toString(this.getDBName()).length(),Integer.toString(this.getDBName()));
		String value = this.toString();
		attr+= TLVVisitor.makeTLV(TLVVisitor.TLVType.OPAQUE,value.length(),value);
		return TLVVisitor.makeTLV(TLVVisitor.TLVType.PROPERTY,attr.length(),attr);
	}
	public void flushObject(DataOutputStream out) throws Exception {
		out.writeInt(value.length());
		out.writeBytes(value);
	}
	public void initObject(DataInputStream in) throws Exception {
		final byte[] f = new byte[in.readInt()];
		in.read(f);
		value = new String(f);
	}
	public int packingSize() {
		return Integer.SIZE/8+value.length();
	}

}
