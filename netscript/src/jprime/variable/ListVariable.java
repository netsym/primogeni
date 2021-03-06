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
import java.util.List;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;
$else$ */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jprime.EntityFactory;
import jprime.ModelNode;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;
/* $endif$ */



/**
 * @author Nathanael Van Vorst
 */
public class ListVariable extends ModelNodeVariable implements Serializable {
	private static final long serialVersionUID = -1578668830064206175L;
	
	/* $if SEPARATE_PROP_TABLE $
	List<ModelNodeVariable> l;
	
	private static String serializeList(List<ModelNodeVariable> vars) {
		throw new RuntimeException("Not done");
	}
	
	private static List<ModelNodeVariable> deserializeList(String str) {
		throw new RuntimeException("Not done");
	}
	 $else$ */
	private List<ModelNodeVariable> value;
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public ListVariable(Metadata meta, long dbid, long db_owner_id, int db_type, int db_name, String value) {
		super(meta,dbid,db_owner_id,EntityFactory.StringVariable, db_name,value);
		l=null;
	}
	$else$ */
	public ListVariable(ModelNode owner, int db_name, List<ModelNodeVariable> value) {
		super(owner,db_name);
		this.value=value;
	}
	/* $endif$ */
		
	/* $if SEPARATE_PROP_TABLE $
	public ListVariable(int db_name, List<ModelNodeVariable> vars) {
		super(getClassTypeId(),db_name,serializeList(vars));
		l=vars;
	}
	public ListVariable(int db_name, String value) {
		super(getClassTypeId(),db_name, value);
		l=deserializeList(value);
	}
	$else$ */
	public ListVariable(int db_name, List<ModelNodeVariable> value) {
		super(db_name);
		this.value=value;
	}
	/* $endif$ */
	/* $if SEPARATE_PROP_TABLE $
	public ListVariable() {
		super();
	}
	$else$ */
	public ListVariable() {
		super();
		this.value=new LinkedList<ModelNodeVariable>();
	}
	/* $endif$ */

	/* $if false == SEPARATE_PROP_TABLE $ */
	public void setValueAsString(String val) {
		throw new RuntimeException("not done!");
	}
    public boolean equals(Object obj) {
    	if(obj instanceof ListVariable) {
    		List<ModelNodeVariable> o = ((ListVariable)obj).value;
    		if(o.size() == value.size()) {
    			for(int i=0;i<value.size();i++) {
    				if(!value.get(i).equals(o.get(i))) {
    					return false;
    				}
    			}
    			return true;
    		}
    	}
    	return false;
    }
    public String toString() {
		throw new RuntimeException("not done!");
    }
	/* $endif$ */

	/* $if SEPARATE_PROP_TABLE $
	public void setValue(String vars) {
		l=deserializeList(vars);
		value=vars;
		modified(Modified.ALL);
	}
	public void setValue(List<ModelNodeVariable> vars) {
		value=serializeList(vars);
		l=vars;
		modified(Modified.ALL);
	}
	public List<ModelNodeVariable> getValue() {
		if(l == null) {
			if(value != null)
				l=deserializeList(value);
		}
		return l;
	}
	$else$ */
	public void setValue(List<ModelNodeVariable> vars) {
		value=vars;
		modified();
	}
	public List<ModelNodeVariable> getValue() {
		return value;
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
		return EntityFactory.ListVariable;
	}
	
	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#getTLVType()
	 */
	@Override
	public TLVVisitor.TLVType getTLVType() {
		return TLVVisitor.TLVType.LIST;
	}

	/* (non-Javadoc)
	 * @see jprime.variable.ModelNodeVariable#encodeTLV()
	 */
	@Override
	public String encodeTLV() {
		String attr = TLVVisitor.makeTLV(TLVType.LONG,Integer.toString(this.getDBName()).length(),Integer.toString(this.getDBName()));
		String value = null;
		String l = "";
		for(ModelNodeVariable mv : this.getValue()) {
			value=mv.toString();
			l+= TLVVisitor.makeTLV(mv.getTLVType(),value.length(),value);
		}
		attr+= TLVVisitor.makeTLV(TLVType.LIST,l.length(),l);
		return TLVVisitor.makeTLV(TLVType.PROPERTY,attr.length(),attr);
	}
	
	public void flushObject(DataOutputStream out) throws Exception {
		/* $if SEPARATE_PROP_TABLE $
		throw new RuntimeException("not supported");
		$else$ */
		out.writeInt(value.size());
		for(ModelNodeVariable v : value) {
			out.writeInt(v.getTypeId());
			out.writeInt(v.getDBName());
			v.flushObject(out);
		}
		/* $endif$ */
	}
	public void initObject(DataInputStream in) throws Exception {
		/* $if SEPARATE_PROP_TABLE $
		throw new RuntimeException("not supported");
		$else$ */
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
			value.add(v);
		}
		/* $endif$ */
	}
	public int packingSize() {
		/* $if SEPARATE_PROP_TABLE $
		throw new RuntimeException("not supported");
		$else$ */
		int rv=(Integer.SIZE*(1+2*value.size()))/8;
		for(ModelNodeVariable v : value) {
			rv+=v.packingSize();
		}
		return rv;
		/* $endif$ */
	}	
}
