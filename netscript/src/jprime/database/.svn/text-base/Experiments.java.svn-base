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
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import jprime.EntityFactory;
import jprime.Experiment;
import jprime.Metadata;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;

$else$ */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jprime.EntityFactory;
import jprime.Experiment;
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
public class Experiments extends Table {
	/* $if USE_FLAT_FILE_DB $
	public static class ExpRecordKey extends RecordKey implements Serializable {
		private static final long serialVersionUID = 7128216083838382261L;
		String name;
		public ExpRecordKey(String name) {
			super(0,0);
			this.name=name;
		}
		public int compareTo(RecordKey o) {
			if(o.getClass() == getClass()) {
				return name.compareTo(((ExpRecordKey)o).name);
			}
			throw new RuntimeException("How did this happen?");
		}
		public String toString() {
			return "[ExpRecordKey "+name+"]";
		}
	}

	public static class ExpRecord extends RecordValue implements Serializable {
		private static final long serialVersionUID = 5954043982744465842L;
		long metadata_id;
		int type;
		ChildIdList kids;
		public ExpRecord(long metadata_id, int type, ChildIdList kids) {
			super();
			this.metadata_id = metadata_id;
			this.type = type;
			this.kids = kids;
		}
		public ExpRecord() { }
		public RecType getType() { return RecType.ExpRecordEnum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			out.writeLong(metadata_id);
			out.writeInt(type);
			kids.flushObject(out);
		}  
		public void initObject(DataInputStream in) throws Exception {  
			metadata_id=in.readLong();
			type=in.readInt();
			kids=ChildIdList.fromBytes(in);
		}
		public int packingsize() {
			return (Long.SIZE+Integer.SIZE)/8+kids.packingsize();
		}
		public String toString() {
			return "[ExpRecord metadata_id="+metadata_id+" kids.size()="+kids.size()+"]";
		}
	}
	$endif$ */
	
	/* $if USE_FLAT_FILE_DB $
	private Select list=null;
	$else$ */
	private PreparedStatement list=null;
	/* $endif$ */
	
	public Experiments(Database db) {
		super("lib_exp",db);
		Field name = new Field(this,"name", ColumnType.VARCHAR_256, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((Experiment)obj).getName());
			}
		};
		Field metadata = new Field(this,"metadata", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL, ConstraintType.INDEX},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((Experiment)obj).getMetadataID());
			}
		};
		Field type = new Field(this, "type", ColumnType.INTEGER, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((Experiment)obj).getTypeId());
			}
		};
		Field kids = new Field(this, "kids", ColumnType.BLOB, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((Experiment)obj).getChildIds());
				$else$ */
				return new BoundValue(ChildIdList.toBytes(((Experiment)obj).getChildIds()));
				/* $endif$ */
			}
		};
		this.fields.add(name);
		this.fields.add(metadata);
		this.fields.add(type);
		this.fields.add(kids);
		this.primaryKeys.add(name);	
	}
	
	/**
INSERT SQL for lib_exp
INSERT INTO lib_exp(name,metadata,type,kids) VALUES(?,?,?,?)

UPDATE SQL for lib_exp
UPDATE lib_exp SET name = ? ,metadata = ? ,type = ? ,kids = ?
WHERE name = ?

DELETE SQL for lib_exp
DELETE FROM lib_exp
WHERE name = ?

DELETE-BY-METADATA SQL for lib_exp
DELETE FROM lib_exp
WHERE metadata = ?

SELECT SQL for lib_exp
SELECT  name, metadata, type, kids FROM lib_exp
WHERE name = ?

LIST SQL for lib_exp
SELECT name, type FROM lib_exp ORDER BY name ASC
	 */

	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup_common() {
		super.setup_common();
		this.select = new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser = new RecordBrowser(new ExpRecordKey((String)vals.get(0).value));
				return 0;
			}
		};
		this.delete = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				delete(new ExpRecordKey((String)vals.get(0).value));
				return 0;
			}
		};
		this.deleteByMetadata = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final TreeSet<String> names = new TreeSet<String>();
				final ExpRecordKey key = new ExpRecordKey("");
				final long mid = (Long)vals.get(0).value;
				final Browser b = browse(key);
				final Tuple t=new Tuple();
				while(b.next(t)) {
					if(((ExpRecord)t.getValue()).metadata_id==mid)
						names.add(((ExpRecordKey)t.getKey()).name);
				}
				while(names.size()>0) {
					key.name= names.pollFirst();
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
				final ExpRecordKey key = new ExpRecordKey((String)vals.get(0).value);
				final ExpRecord value = new ExpRecord(
						(Long)vals.get(1).value,
						(Integer)vals.get(2).value,
						(ChildIdList)vals.get(3).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final ExpRecordKey key = new ExpRecordKey((String)vals.get(0).value);
				final ExpRecord value = new ExpRecord(
						(Long)vals.get(1).value,
						(Integer)vals.get(2).value,
						(ChildIdList)vals.get(3).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.list=new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser=new RecordBrowser(new ExpRecordKey(""));
				return 0;
			}
		};
	}

	$else$ */
	@Override
	protected void setup() {
		super.setup();
		String l="SELECT "+fields.get(0).name+", "+fields.get(2).name+" FROM "+tableName+" ORDER BY "+fields.get(0).name+" ASC";
		jprime.Console.out.println("LIST SQL for "+tableName+" = "+l);
		try {
			list=db.getLoadConnection().prepareStatement(l);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+l);
			throw new RuntimeException(e);
		}
	}
	/* $endif$ */
	
	public Experiment loadExperiment(String name, Metadatas metatable) {
		Experiment rv = load(name, metatable);
		if(rv == null)
			return null;
		if(rv instanceof Experiment)
			return (Experiment)rv;
		throw new RuntimeException("The lib/exp by the name "+name+" is a Library and NOT an Experiment!");
	}
	public Experiment load(String name, Metadatas metatable) {
		Experiment rv=db.getExperiment(name);
		if(rv != null)
			return rv;
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();

		vals.add(this.fields.get(1).getPkeyVal(name));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		String n=null;
		ChildIdList kids=null;
		int type=0;
		long mid=0;
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			n=((ExpRecordKey)rs.value.key).name;
			mid=((ExpRecord)rs.value.value).metadata_id;
			type=((ExpRecord)rs.value.value).type;
			kids=((ExpRecord)rs.value.value).kids;
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				n=rs.getString(1);
				mid=rs.getLong(2);
				type=rs.getInt(3);
				kids=ChildIdList.fromBytes(rs.getBlob(4));
				break;
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading lib/exp, name="+name);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		if(n != null) {
			Metadata meta = metatable.load(mid);
			if(type == EntityFactory.Experiment) {
				rv = new Experiment(name, meta,kids);
			}
			else {
				throw new RuntimeException("what happened?");
			}
		}
		return rv;
	}
	protected void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts) {
		if(owner instanceof Metadata) {
			deleteByMetadata(owner.getDBID(),stmts);
		}
		else {
			throw new RuntimeException("only meta datas own exps/libs! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}
	protected void delete(PersistableObject obj, List<SQLStmt> stmts) {
		db.removeLibExp(((Experiment)obj).getName());
		Metadata m = ((Experiment)obj).getMetadata();
		db.removeMetadata(m.getDBID());
		/* $if SEPARATE_PROP_TABLE $
		db.Attrs.deleteByOwner(m, stmts);
		$endif$ */
		db.BGPLinkTypes.deleteByOwner(m, stmts);
		db.EmulationCommands.deleteByOwner(m, stmts);
		db.Experiments.deleteByOwner(m, stmts);
		db.ModelNodes.deleteByOwner(m, stmts);
		
		db.RouteEntries.deleteByOwner(m, stmts);
		db.RoutingProtocols.deleteByOwner(m, stmts);
		db.SymbolTables.deleteByOwner(m, stmts);
		db.Metadatas.delete(m, stmts);
	}
	public void delete(Experiment toDel) {
		jprime.Console.out.println("Deleting "+toDel.getName());
		ArrayList<SQLStmt> stmts = new ArrayList<Table.SQLStmt>(100);
		long m = toDel.getMetadataID();
		db.removeLibExp(toDel.getName());
		db.removeMetadata(m);
		/* $if SEPARATE_PROP_TABLE $
		db.Attrs.deleteByMetadata(m, stmts);
		$endif$ */
		db.BGPLinkTypes.deleteByMetadata(m, stmts);
		db.EmulationCommands.deleteByMetadata(m, stmts);
		db.Experiments.deleteByMetadata(m, stmts);
		db.ModelNodes.deleteByMetadata(m, stmts);
		db.RouteEntries.deleteByMetadata(m, stmts);
		db.RoutingProtocols.deleteByMetadata(m, stmts);
		db.SymbolTables.deleteByMetadata(m, stmts);
		db.Metadatas.deleteByMetadata(m, stmts);
		db.getDBThread().exec(stmts);
		jprime.Console.out.println("Finished deleting "+toDel.getName());
	}
	public List<String> listExperiments() {
		LinkedList<String> rv = new LinkedList<String>();
		
		final LoadSQLStmt ls = new LoadSQLStmt(new ArrayList<Field.BoundValue>(), list);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			rv.add(((ExpRecordKey)rs.value.key).name);
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);

		try {
			while (rs.next()) {
				if(rs.getInt(2) == EntityFactory.Experiment) {
					rv.add(rs.getString(1));
				}
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error listing lib/exps");
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		return rv;
	}
}
