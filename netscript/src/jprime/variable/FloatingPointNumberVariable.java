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
public class FloatingPointNumberVariable extends ModelNodeVariable implements Serializable {
	private static final long serialVersionUID = 5948403719580764216L;
	
	/* $if false == SEPARATE_PROP_TABLE $ */
	private double value;
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public FloatingPointNumberVariable(Metadata meta, long dbid, long db_owner_id, int db_type, int db_name, String value) {
		super(meta,dbid,db_owner_id,EntityFactory.FloatingPointNumberVariable, db_name,value);
	}
	$else$ */
	public FloatingPointNumberVariable(ModelNode owner, int db_name, float value) {
		super(owner,db_name);
		this.value=value;
	}
	public FloatingPointNumberVariable(ModelNode owner, int db_name, double value) {
		super(owner,db_name);
		this.value=value;
	}
	public FloatingPointNumberVariable(ModelNode owner, int db_name, String value) {
		super(owner,db_name);
		this.value=Double.parseDouble(value);
	}
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public FloatingPointNumberVariable(int db_name, float value) {
		super(getClassTypeId(), db_name, Double.toString(value));
	}
	public FloatingPointNumberVariable(int db_name, double value) {
		super(getClassTypeId(), db_name, Double.toString(value));
	}
	$else$ */
	public FloatingPointNumberVariable(int db_name, float value) {
		super(db_name);
		this.value=value;
	}
	public FloatingPointNumberVariable(int db_name, double value) {
		super(db_name);
		this.value=value;
	}
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public FloatingPointNumberVariable(int db_name, String value) {
		super(getClassTypeId(),db_name, value);
	}
	$else$ */
	public FloatingPointNumberVariable(int db_name, String value) {
		super(db_name);
		this.value=Double.parseDouble(value);
	}
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public FloatingPointNumberVariable() {
		super();
	}
	$else$ */
	public FloatingPointNumberVariable() {
		super();
		this.value=0;
	}
	/* $endif$ */
	
	/* $if false == SEPARATE_PROP_TABLE $ */
	public void setValueAsString(String val) {
		this.value=Double.parseDouble(val);
		modified();
	}
    public boolean equals(Object obj) {
    	return (obj instanceof FloatingPointNumberVariable && value == ((FloatingPointNumberVariable)obj).value);
    }
    public String toString() {
    	return Double.toString(value);
    }
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public void setValue(String v) {
		this.value=v;
		modified(Modified.ALL);
	}
	public void setValue(double v) {
		value=Double.toString(v);
		modified(Modified.ALL);
	}
	public void setValue(float v) {
		value=Float.toString(v);
		modified(Modified.ALL);
	}
	public float getValue() {
		return Float.parseFloat(value);
	}
	$else$ */
	public void setValue(String v) {
		value=Double.parseDouble(v);
		modified();
	}
	public void setValue(float v) {
		value=v;
		modified();
	}
	public void setValue(double v) {
		value=v;
		modified();
	}
	public double getValue() {
		return value;
	}
	/* $endif$ */

	/**
	 * @param ds
	 * @return
	 */
	public double getRuntimeValue(IModelNode owner, Dataset ds) {
		if(ds == null)
			return getValue();
		SimpleDatum rv = ds.getMostRecentDatum(this.getDBName(), owner.getUID());
		if(rv==null) {
			return getValue();
		}
		return Float.parseFloat(rv.value);
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
		return EntityFactory.FloatingPointNumberVariable;
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#getTLVType()
	 */
	@Override
	public TLVVisitor.TLVType getTLVType() {
		return TLVVisitor.TLVType.FLOAT;
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#encodeTLV()
	 */
	@Override
	public String encodeTLV() {
		String attr = TLVVisitor.makeTLV(TLVType.LONG,Integer.toString(this.getDBName()).length(),Integer.toString(this.getDBName()));
		String value = this.toString();
		attr+= TLVVisitor.makeTLV(TLVType.FLOAT,value.length(),value);
		return TLVVisitor.makeTLV(TLVType.PROPERTY,attr.length(),attr);
	}
	
	public void flushObject(DataOutputStream out) throws Exception {
		/* $if SEPARATE_PROP_TABLE $
		out.writeDouble(getValue());
		$else$ */
		out.writeDouble(value);
		/* $endif$ */
	}
	public void initObject(DataInputStream in) throws Exception {
		/* $if SEPARATE_PROP_TABLE $
		value=Double.toString(in.readDouble());
		$else$ */
		value=in.readDouble();
		/* $endif$ */
	}
	public int packingSize() {
		/* $if SEPARATE_PROP_TABLE $
		$else$ */
		/* $endif $ */
		return Double.SIZE/8;
	}

}
