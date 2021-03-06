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
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ResourceIdentifier.ResourceID;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;
$else$ */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

import jprime.EntityFactory;
import jprime.ModelNode;
import jprime.ResourceIdentifier.ResourceID;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 */
public class ResourceIdentifierVariable extends ModelNodeVariable implements Serializable {
	private static final long serialVersionUID = -8483214059260350610L;
	
	private transient ResourceID rid;
	private String value;
	
	private static String serializeID(ResourceID id) {
		if(id.getCompiledStr()==null) {
			return "#1#"+id.toString();
		}
		return "#0#"+id.getCompiledStr();
	}
	
	private static ResourceID deserializeID(String str) {
		if(str.length()>3) {
			if(str.charAt(0)=='#' && str.charAt(0)=='0' && str.charAt(0)=='#') {
				return new ResourceID(str.substring(3),true);
			}
			else if(str.charAt(0)=='#' && str.charAt(0)=='1' && str.charAt(0)=='#') {
				return new ResourceID(str.substring(3),false);
			}
		}
		return new ResourceID(str,false);
	}
	
	public ResourceIdentifierVariable() {
		super();
		this.rid=null;
		this.value=null;
	}

	/* $if SEPARATE_PROP_TABLE $
	public ResourceIdentifierVariable(Metadata meta, long dbid, long db_owner_id, int db_type, int db_name, String value) {
		super(meta,dbid,db_owner_id,EntityFactory.StringVariable, db_name,value);
		rid=null;
	}
	$else$ */
	public ResourceIdentifierVariable(ModelNode owner, int db_name, ResourceID value) {
		super(owner,db_name);
		this.rid=value;
		this.value=serializeID(this.rid);
	}
	public ResourceIdentifierVariable(ModelNode owner, int db_name, String value) {
		super(owner,db_name);
		this.value=value;
		this.rid=deserializeID(value);
	}
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public ResourceIdentifierVariable(int db_name, ResourceID value) {
		super(getClassTypeId(),db_name, serializeID(value));
		rid=value;
	}
	$else$ */
	public ResourceIdentifierVariable(int db_name, ResourceID value) {
		super(db_name);
		this.rid=value;
		this.value=serializeID(this.rid);
	}
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public ResourceIdentifierVariable(int db_name, String value) {
		super(getClassTypeId(),db_name, value);
		rid=deserializeID(value);
	}
	$else$ */
	public ResourceIdentifierVariable(int db_name, String value) {
		super(db_name);
		this.value=value;
		this.rid=deserializeID(value);
	}
	/* $endif$ */

	/* $if false == SEPARATE_PROP_TABLE $ */
	public void setValueAsString(String val) {
		this.value=val;
		this.rid=deserializeID(value);
		modified();
	}
    public boolean equals(Object obj) {
    	return (obj instanceof ResourceIdentifierVariable && value.equals(((ResourceIdentifierVariable)obj).value));
    }
    public String toString() {
    	return this.rid.toString();
    }
	/* $endif$ */

    
	/* $if SEPARATE_PROP_TABLE $
	public void setValue(ResourceID v) {
		rid=v;
		value=serializeID(rid);
		modified(Modified.ALL);
	}
	public void setValue(String v) {
		rid=deserializeID(v);
		value=v;
		modified(Modified.ALL);
	}
	public ResourceID getValue() {
		if(rid == null) {
			if(value != null)
				rid=deserializeID(value);
		}
		return rid;
	}
	$else$ */
	public void setValue(ResourceID v) {
		this.value=serializeID(v);
		this.rid=v;
		modified();
	}
	public void setValue(String v) {
		this.value=v;
		this.rid=deserializeID(value);
		modified();
	}
	public ResourceID getValue() {
		if(rid == null) {
			if(value != null)
				rid=deserializeID(this.value);
		}
		return rid;
	}
	/* $endif$ */

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
		return EntityFactory.ResourceIdentifierVariable;
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#getTLVType()
	 */
	@Override
	public TLVVisitor.TLVType getTLVType() {
		return TLVVisitor.TLVType.STRING;
	}
	
	/**
	 * Evaluate the ResourceID using 'anchor' as the location
	 * in the model from which to start the evaluation
	 * 
	 * @param anchor the node from which to calculate relative ids
	 * @param path_to_anchor the path from where this resource id is embeded to the anchor
	 */
	public void evaluate(ModelNode anchor, String path_to_anchor) {
		getValue().evaluate(anchor,path_to_anchor);
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#encodeTLV()
	 */
	@Override
	public String encodeTLV() {
		String attr = TLVVisitor.makeTLV(TLVType.LONG,Integer.toString(this.getDBName()).length(),Integer.toString(this.getDBName()));
		attr+=getValue().packToTLV();
		return TLVVisitor.makeTLV(TLVType.PROPERTY,attr.length(),attr);
	}

	public String getCompiledString() {
		return getValue().getCompiledString();
	}

	public void flushObject(DataOutputStream out) throws Exception {
		out.writeInt(value.length());
		out.writeBytes(value);
	}
	public void initObject(DataInputStream in) throws Exception {
		final byte[] f = new byte[in.readInt()];
		in.read(f);
		value = new String(f);
		this.rid=null;
	}
	public int packingSize() {
		return Integer.SIZE/8+value.length();
	}

}
