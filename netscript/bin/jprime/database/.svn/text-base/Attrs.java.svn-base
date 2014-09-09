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

/* $if SEPARATE_PROP_TABLE $

$if USE_FLAT_FILE_DB $

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.variable.BooleanVariable;
import jprime.variable.FloatingPointNumberVariable;
import jprime.variable.IntegerVariable;
import jprime.variable.ListVariable;
import jprime.variable.ModelNodeVariable;
import jprime.variable.OpaqueVariable;
import jprime.variable.ResourceIdentifierVariable;
import jprime.variable.StringVariable;
import jprime.variable.SymbolVariable;

$else$

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.variable.BooleanVariable;
import jprime.variable.FloatingPointNumberVariable;
import jprime.variable.IntegerVariable;
import jprime.variable.ListVariable;
import jprime.variable.ModelNodeVariable;
import jprime.variable.OpaqueVariable;
import jprime.variable.ResourceIdentifierVariable;
import jprime.variable.StringVariable;
import jprime.variable.SymbolVariable;

$endif$


$endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
/* $if false == SEPARATE_PROP_TABLE $ */
public class Attrs {
	private Attrs() {}
}
/* $else$
public class Attrs extends Table {
	$if USE_FLAT_FILE_DB $

	public static class VarRecordKey extends RecordKey implements Serializable {
		private static final long serialVersionUID = 46274693414730289L;
		final long parent_dbid;
		public VarRecordKey(long metadata_id, long parent_dbid, long dbid) {
			super(metadata_id,dbid);
			this.parent_dbid=parent_dbid;
		}
		public int compareTo(RecordKey o) {
			if(o.getClass() == getClass()) {
				final VarRecordKey oo = (VarRecordKey)o;
				if(metadata_id == oo.metadata_id) {
					if(parent_dbid == oo.parent_dbid) {
						return (int)(dbid - o.dbid);
					}
					return (int)(parent_dbid - oo.parent_dbid);
				}
				return (int)(metadata_id - oo.metadata_id);
			}
			throw new RuntimeException("How did this happen?");
		}
		
	}

	public static class VarRecord extends RecordValue implements Serializable { 
		private static final long serialVersionUID = -6937315114270538365L;
		int name;
		int type;
		String value;
		public VarRecord(int name, int type, String value) {
			super();
			this.name = name;
			this.type = type;
			this.value = value;
		}
		public VarRecord() { }
		public RecType getType() { return RecType.VarRecordEnum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			out.writeInt(name);
			out.writeInt(type);
			out.writeInt(value.length());
			out.writeBytes(value);
		}
		public void initObject(DataInputStream in) throws Exception {  
			name=in.readInt();
			type=in.readInt();
			byte[] b = new byte[in.readInt()];
			in.read(b);
			value=new String(b);
		}
		public int packingsize() {
			return (Integer.SIZE+Integer.SIZE+Integer.SIZE)/8+value.length();
		}
	}

	$endif$
	
	$if USE_FLAT_FILE_DB $
	private Select select_many=null;
	private Delete deleteByOwner=null;
	$else$
	private PreparedStatement select_many=null,deleteByOwner=null;
	$endif$
	
	public Attrs(Database db) {
		super("attrs",db);
		
		Field dbid = new Field(this,"dbid", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNodeVariable)obj).getDBID());
			}
		};
		Field metadata = new Field(this,"metadata", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNodeVariable)obj).getDBMetaID());
			}
		};
		Field owner = new Field(this,"owner", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL, ConstraintType.INDEX},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNodeVariable)obj).getOwnerID());
			}
		};
		Field name = new Field(this, "name", ColumnType.INTEGER, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNodeVariable)obj).getDBName());
			}
		};
		Field type = new Field(this, "type", ColumnType.INTEGER, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNodeVariable)obj).getTypeId());
			}
		};
		Field value = new Field(this, "value", ColumnType.CLOB, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNodeVariable)obj).toString());
			}
		};
		this.fields.add(dbid);
		this.fields.add(metadata);
		this.fields.add(owner);
		this.fields.add(name);
		this.fields.add(type);
		this.fields.add(value);
		this.primaryKeys.add(dbid);	
		this.primaryKeys.add(metadata);	
		$if USE_FLAT_FILE_DB $
		this.primaryKeys.add(owner);	
		$endif$
	}
	
	$if false$
	DELETE SQL for attrs
	DELETE FROM attrs WHERE dbid = ?  AND metadata = ? AND owner = ?
	
	DELETE-BY-METADATA SQL for attrs
	DELETE FROM attrs WHERE metadata = ?
	
	SELECT SQL for attrs = SELECT  dbid, metadata, owner, name, type, value FROM attrs  WHERE dbid = ?  AND metadata = ? AND owner = ?
	
	INSERT SQL for attrs
	INSERT INTO attrs(dbid,metadata,owner,name,type,value) VALUES(?,?,?,?,?,?)
	
	UPDATE SQL for attrs
	UPDATE attrs SET dbid = ? ,metadata = ? ,owner = ? ,name = ? ,type = ? ,value = ?  WHERE dbid = ?  AND metadata = ? AND owner = ?
	
	SELECT-MANY SQL for attrs
	SELECT  dbid, metadata, owner, name, type, value FROM attrs WHERE metadata = ? AND owner = ?
	
	DELETE BY OWNER SQL for attrs
	DELETE FROM attrs WHERE metadata = ? AND owner = ?

	$endif$
	
	$if USE_FLAT_FILE_DB $
	@Override
	protected void setup_common() {
		super.setup_common();
		this.select = new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser = new RecordBrowser(new VarRecordKey((Long)vals.get(1).value, (Long)vals.get(2).value, (Long)vals.get(0).value));
				return 0;
			}
		};
		this.delete = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				delete(new VarRecordKey((Long)vals.get(1).value, (Long)vals.get(2).value, (Long)vals.get(0).value));
				return 0;
			}
		};
		this.deleteByMetadata = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final TreeSet<Long> dbids = new TreeSet<Long>();
				final VarRecordKey key = new VarRecordKey((Long)vals.get(0).value,0,0);
				final Browser b = browse(key);
				final Tuple t=new Tuple();
				while(b.next(t)) {
					dbids.add(((VarRecordKey)t.getKey()).dbid);
				}
				while(dbids.size()>0) {
					key.dbid= dbids.pollFirst();
					delete(key);
				}
				return 0;
			}
		};
	}
	@Override
	protected void setup() {
		setup_common();
		this.insert=new Insert() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final VarRecordKey key = new VarRecordKey((Long)vals.get(1).value, (Long)vals.get(2).value, (Long)vals.get(0).value);
				final VarRecord value = new VarRecord(
						(Integer)vals.get(3).value,
						(Integer)vals.get(4).value,
						(String)vals.get(5).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final VarRecordKey key = new VarRecordKey((Long)vals.get(1).value, (Long)vals.get(2).value, (Long)vals.get(0).value);
				final VarRecord value = new VarRecord(
						(Integer)vals.get(3).value,
						(Integer)vals.get(4).value,
						(String)vals.get(5).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.select_many=new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser=new RecordBrowser(new VarRecordKey((Long)vals.get(0).value, (Long)vals.get(1).value, 0));
				return 0;
			}
		};
		this.deleteByOwner=new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final TreeSet<Long> dbids = new TreeSet<Long>();
				final VarRecordKey key = new VarRecordKey((Long)vals.get(0).value,(Long)vals.get(1).value,0);
				final Browser b = browse(key);
				final Tuple t=new Tuple();
				while(b.next(t)) {
					dbids.add(((VarRecordKey)t.getKey()).dbid);
				}
				while(dbids.size()>0) {
					key.dbid= dbids.pollFirst();
					delete(key);
				}
				return 0;
			}
		};
	}

	$else$
	@Override
	protected void setup() {
		super.setup();
		String add=null;
		for(Field f:fields) {
			if(add==null) {
				add="SELECT  ";
			}
			else {
				add+=", ";
			}
			add+=f.name;
		}
		add+=" FROM "+tableName+" WHERE "+fields.get(1).name+" = ? AND "+fields.get(2).name+" = ?";

		jprime.Console.out.println("SELECT-MANY SQL for "+tableName+" = "+add);
		try {
			select_many=db.getLoadConnection().prepareStatement(add);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+add);
			throw new RuntimeException(e);
		}

		String where=null;
		if(metadataid != null) {
			where="DELETE FROM "+tableName+" WHERE "+metadataid.name+" = ? AND "+fields.get(2).name+" = ?" ;
		}
		jprime.Console.out.println("DELETE BY OWNER SQL for "+tableName+" = "+where);
		try {
			deleteByOwner=db.getFlushConnection().prepareStatement(where);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+where);
			throw new RuntimeException(e);
		}
		
	}
	$endif$
	
	protected void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts) {
		if(owner instanceof Metadata) {
			deleteByMetadata(owner.getDBID(),stmts);
		}
		else if(owner instanceof ModelNode) {
			ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
			vals.add(metadataid.getPkeyVal(((ModelNode)owner).getMetadata().getDBID()));
			vals.add(metadataid.getPkeyVal(owner.getDBID()));
			stmts.add(new SQLStmt(vals, deleteByOwner));
		}
		else {
			throw new RuntimeException("model node attrs are only owned by model node and meta datas! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}
	
	public ModelNodeVariable load_single(Metadata meta, long dbid, long owner_id) {
		ModelNodeVariable rv=null;
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
		
		vals.add(this.fields.get(0).getPkeyVal(dbid));
		vals.add(this.fields.get(1).getPkeyVal(meta.getDBID()));
		vals.add(this.fields.get(2).getPkeyVal(owner_id));
		
		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		$if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			rv = (ModelNodeVariable)meta.findLoadedObj(new PKey(meta.getDBID(), rs.value.key.dbid));
			if(rv==null) {
				rv = createVar(meta,(VarRecordKey)rs.value.key,(VarRecord)rs.value.value);
			}
			break;
		}
		ls.realeaseLock();
		$else$
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				rv = (ModelNodeVariable)meta.findLoadedObj(new PKey(meta.getDBID(), rs.getLong(1)));
				if(rv==null) {
					rv = createVar(meta,rs);
				}
				break;
			}
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading attrs for model node, dbid="+owner_id+", metaid="+meta.getDBID());
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		db.getDBThread().closeRS(rs);
		$endif$
		//jprime.Console.err.println("loaded "+attrs.size()+ "kids!");
		if(rv==null) {
			try {
				throw new RuntimeException("What happened? rv==null!");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
	
	public ModelNodeVariable load(Metadata meta, long dbid, long owner_id) {
		ModelNodeVariable rv=null;
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>(2);
		
		vals.add(this.fields.get(1).getPkeyVal(meta.getDBID()));
		vals.add(this.fields.get(2).getPkeyVal(owner_id));
		
		final LoadSQLStmt ls = new LoadSQLStmt(vals, select_many);
		$if USE_FLAT_FILE_DB $
		LinkedList<Record> recs = new LinkedList<Record>();
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			ModelNodeVariable a = (ModelNodeVariable)meta.findLoadedObj(new PKey(meta.getDBID(), rs.value.key.dbid));
			if(a==null) {
				recs.add(rs.value);
			}
			else {
				if(a.getDBID()==dbid)
				rv=a;
			}
		}
		ls.realeaseLock();
		while(recs.size()>0) {
			final Record rec = recs.poll();
			ModelNodeVariable a = createVar(meta,(VarRecordKey)rec.key,(VarRecord)rec.value);
			if(a.getDBID()==dbid)
				rv=a;
		}
		$else$
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				ModelNodeVariable a = (ModelNodeVariable)meta.findLoadedObj(new PKey(meta.getDBID(), rs.getLong(1)));
				if(a==null) {
					a = createVar(meta,rs);
				}
				if(a.getDBID()==dbid)
					rv=a;
			}
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading attrs for model node, dbid="+owner_id+", metaid="+meta.getDBID());
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		db.getDBThread().closeRS(rs);
		$endif$
		//jprime.Console.err.println("loaded "+attrs.size()+ "kids!");
		if(rv==null) {
			//it may have been new and not flushed out
			rv = (ModelNodeVariable)meta.findLoadedObj(new PKey(meta.getDBID(), dbid));
		}
		if(rv==null) {
			try {
				throw new RuntimeException("What happened? rv==null!");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
	
	$if USE_FLAT_FILE_DB $
	public ModelNodeVariable createVar(Metadata meta, VarRecordKey key, VarRecord rec) {
		final long dbid=key.dbid;
		final long owner=key.parent_dbid;
		final int name=rec.name;
		final int type=rec.type;
		final String value = rec.value;
		switch(type) {
			case EntityFactory.BooleanVariable:
				return new BooleanVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.FloatingPointNumberVariable:
				return new FloatingPointNumberVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.IntegerVariable:
				return new IntegerVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.ListVariable:
				return new ListVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.OpaqueVariable:
				return new OpaqueVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.ResourceIdentifierVariable:
				return new ResourceIdentifierVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.StringVariable:
				return new StringVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.SymbolVariable:
				return new SymbolVariable(meta,dbid,owner,type,name,value);
			default:
				try {
					throw new RuntimeException("What happened? type=="+type+"!");
				}catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
					jprime.Console.halt(-1);
				}
		}
		
		throw new RuntimeException("What happened? type=="+type+"!");
	}
	$else$
	public ModelNodeVariable createVar(Metadata meta,ResultSet rs) throws SQLException {
		final long dbid=rs.getLong(1);
		//final long metadata=rs.getLong(2);
		final long owner=rs.getLong(3);
		final int name=rs.getInt(4);
		final int type=rs.getInt(5);
		final String value = rs.getString(6);
		switch(type) {
			case EntityFactory.BooleanVariable:
				return new BooleanVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.FloatingPointNumberVariable:
				return new FloatingPointNumberVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.IntegerVariable:
				return new IntegerVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.ListVariable:
				return new ListVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.OpaqueVariable:
				return new OpaqueVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.ResourceIdentifierVariable:
				return new ResourceIdentifierVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.StringVariable:
				return new StringVariable(meta,dbid,owner,type,name,value);
			case EntityFactory.SymbolVariable:
				return new SymbolVariable(meta,dbid,owner,type,name,value);
			default:
				try {
					throw new RuntimeException("What happened? type=="+type+"!");
				}catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
					jprime.Console.halt(-1);
				}
		}
		
		throw new RuntimeException("What happened? type=="+type+"!");
	}
	$endif$
}
$endif$ */
