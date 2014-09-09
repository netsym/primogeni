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

/* $if USE_FLAT_FILE_DB $

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jprime.Metadata;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;

$else$ */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jprime.Metadata;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class Metadatas extends Table {
	/* $if USE_FLAT_FILE_DB $
	public static class MetadataRecordKey extends RecordKey implements Serializable {
		private static final long serialVersionUID = -4299262553485850431L;
		public MetadataRecordKey(long dbid) {
			super(-1, dbid);
		}
		@Override
		public int compareTo(RecordKey o) {
			if(o.getClass() == getClass()) {
				return (int)(dbid - o.dbid);
			}
			throw new RuntimeException("How did this happen?");
		}
		@Override
		public void setAutoId(long id) {
			if(dbid==0)
				dbid=id;
		}
		public String toString() {
			return "[MetadataRecordKey "+dbid+"]";
		}
	}

	public static class MetadataRecord extends RecordValue implements Serializable {
		private static final long serialVersionUID = -626315454441169179L;
		long max_node_id;
		int state;
		long symboltable_id;
		public MetadataRecord(long max_node_id, int state, long symboltable_id) {
			super();
			this.max_node_id = max_node_id;
			this.state = state;
			this.symboltable_id = symboltable_id;
		}
		public MetadataRecord() { }
		public RecType getType() { return RecType.MetadataRecordEnum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			out.writeLong(max_node_id);
			out.writeInt(state);
			out.writeLong(symboltable_id);
		}  
		public void initObject(DataInputStream in) throws Exception {  
			max_node_id=in.readLong();
			state=in.readInt();
			symboltable_id=in.readLong();
		}
		public int packingsize() {
			return (Long.SIZE+Long.SIZE+Integer.SIZE)/8;
		}
		public String toString() {
			return "[MetadataRecord max_node_id="+max_node_id+" state="+state+" symboltable_id="+symboltable_id+"]";
		}
	}
	$endif$ */

	public Metadatas(Database db) {
		super("metadatas",db);
		Field dbid = new Field(this, "dbid", ColumnType.INTEGER, null,  new ConstraintType[]{ConstraintType.NOT_NULL, ConstraintType.AUTO_INCREMENT},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				if(((Metadata)obj).getDBID()==-1)
					return new BoundValue("DEFAULT");
				return new BoundValue(((Metadata)obj).getDBID());
			}
		};
		Field max_node_id = new Field(this,"max_node_id", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((Metadata)obj).getDB_max_node_id());
			}
		};
		Field state = new Field(this,"state", ColumnType.INTEGER, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((Metadata)obj).getDB_state());
			}
		};
		Field symboltable_id = new Field(this,"symboltable_id", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((Metadata)obj).getSymboltableId());
			}
		};
		this.fields.add(dbid);
		this.fields.add(max_node_id);
		this.fields.add(state);
		this.fields.add(symboltable_id);
		this.primaryKeys.add(dbid);	
	}

	/**
	INSERT SQL for metadatas
	INSERT INTO metadatas(dbid,max_node_id,state,symboltable_id)
	VALUES(DEFAULT,?,?,?)
	
	UPDATE SQL for metadatas
	UPDATE metadatas SET max_node_id = ? ,state = ? ,symboltable_id = ?
	WHERE dbid = ?
	
	DELETE SQL for metadatas
	DELETE FROM metadatas
	WHERE dbid = ?
	
	DELETE-BY-METADATA SQL for metadatas
	DELETE FROM metadatas
	WHERE dbid = ?
	
	SELECT SQL for metadatas
	SELECT  dbid, max_node_id, state, symboltable_id FROM metadatas
	WHERE dbid = ?
	 */

	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup_common() {
		super.setup_common();
		this.select = new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser = new RecordBrowser(new MetadataRecordKey((Long)vals.get(0).value));
				return 0;
			}
		};
		this.delete = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				delete(new MetadataRecordKey((Long)vals.get(0).value));
				return 0;
			}
		};
		this.deleteByMetadata = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				delete(new MetadataRecordKey((Long)vals.get(0).value));
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
				final MetadataRecordKey key = new MetadataRecordKey(0);
				final MetadataRecord value = new MetadataRecord(
						(Long)vals.get(0).value,
						(Integer)vals.get(1).value,
						(Long)vals.get(2).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final MetadataRecordKey key = new MetadataRecordKey((Long)vals.get(3).value);
				final MetadataRecord value = new MetadataRecord(
						(Long)vals.get(0).value,
						(Integer)vals.get(1).value,
						(Long)vals.get(2).value);
				insert(key,value);
				return key.dbid;
			}
		};
	}
	$endif$ */
	
	public Metadata load(long metaid) {
		Metadata rv=db.getMetadata(metaid);
		if(rv != null) {
			return rv;
		}
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();

		vals.add(this.fields.get(0).getPkeyVal(metaid));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			final MetadataRecord r = (MetadataRecord)rs.value.value;
			rv = new Metadata(db,metaid, r.max_node_id, r.state, r.symboltable_id);
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				rv = new Metadata(db,metaid, rs.getLong(2), rs.getInt(3),rs.getLong(4));
				break;
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading meta data, metaid="+metaid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		if(rv == null) {
			throw new RuntimeException("Unable to load metadata with id="+metaid);
		}
		return rv;
	}

	protected void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts) {
		throw new RuntimeException("meta datas do not have owners!");
	}

	/* (non-Javadoc)
	 * @see jprime.database.Table#delete(jprime.PersistableObject, java.util.List)
	 */
	@Override
	protected void delete(PersistableObject obj, List<SQLStmt> stmts) {
		db.removeMetadata(((Metadata)obj).getDBID());
		super.delete(obj, stmts);
	}

	/* (non-Javadoc)
	 * @see jprime.database.Table#deleteByMetadata(long, java.util.List)
	 */
	@Override
	protected void deleteByMetadata(long mid, List<SQLStmt> stmts) {
		db.removeMetadata(mid);
		super.deleteByMetadata(mid, stmts);
	}
}
