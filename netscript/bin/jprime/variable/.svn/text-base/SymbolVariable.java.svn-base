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
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;

$else$ */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

import jprime.EntityFactory;
import jprime.ModelNode;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 */
public class SymbolVariable extends ModelNodeVariable implements Serializable {
	private static final long serialVersionUID = 2568459599792071940L;
	
	/* $if false == SEPARATE_PROP_TABLE $ */
	private String value;
	public SymbolVariable() {
		super(-1);
		this.value=null;
	}	
	/* $endif$ */
	
	
	/* $if SEPARATE_PROP_TABLE $
	public SymbolVariable(Metadata meta, long dbid, long db_owner_id, int db_type, int db_name, String value) {
		super(meta,dbid,db_owner_id,EntityFactory.StringVariable, db_name,value);
	}
	$else$ */
	public SymbolVariable(ModelNode owner, int db_name, String value) {
		super(owner,db_name);
		this.value=value;
	}
	/* $endif$ */
	
	/* $if SEPARATE_PROP_TABLE $
	public SymbolVariable(String value) {
		super(getClassTypeId(),-1,value);
	}	
	$else$ */
	public SymbolVariable(String value) {
		super(-1);
		this.value=value;
	}	
	/* $endif$ */
	
	/* $if false == SEPARATE_PROP_TABLE $ */
	public void setValueAsString(String val) {
		this.value=val;
		modified();
	}
    public boolean equals(Object obj) {
    	return (obj instanceof SymbolVariable && value.equals(((SymbolVariable)obj).value));
    }
    public String toString() {
    	return value;
    }
	/* $endif$ */
    
	/**
	 * @return
	 */
	public String getName() {
		return value;
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
		return EntityFactory.SymbolVariable;
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#getTLVType()
	 */
	@Override
	public TLVVisitor.TLVType getTLVType() {
		return TLVVisitor.TLVType.SYMBOL;
	}
	
	/* $if SEPARATE_PROP_TABLE $
	@Override
	public void orphan() {
		getMetadata().getSymbolTable().unregisterSymbol(getName());
		super.orphan();
	}
	$endif$ */

	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#encodeTLV()
	 */
	@Override
	public String encodeTLV() {
		final String v =TLVVisitor.makeTLV(TLVType.STRING, getName().length(), getName());
		final String dbame=TLVVisitor.makeTLV(TLVType.LONG,Integer.toString(this.getDBName()).length(),Integer.toString(this.getDBName()));
		final String attr = dbame+TLVVisitor.makeTLV(TLVType.SYMBOL,v.length(),v);
		return TLVVisitor.makeTLV(TLVType.PROPERTY,attr.length(),attr);
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#attachToNode(jprime.ModelNode, int)
	 */
	@Override
	public void attachToNode(ModelNode m, int _dbname) {
		if(this.getDBName() == -1) {
			this.setDBName(_dbname);
			m.getMetadata().getSymbolTable().registerSymbol(getName());
		}
		else {
			throw new RuntimeException("why was this callend? -- probably should not have been!!");
		}
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
