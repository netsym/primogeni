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
import java.util.Map.Entry;
import java.util.TreeMap;

import jprime.Metadata;
import jprime.PersistableObject;
import jprime.SymbolTable;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;

$else$ */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import jprime.Metadata;
import jprime.PersistableObject;
import jprime.SymbolTable;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class SymbolTables extends Table {
	/* $if USE_FLAT_FILE_DB $

	public static class SymbolTableRec extends RecordValue implements Serializable { 
		private static final long serialVersionUID = -8048058655551730321L;
		final TreeMap<String,Integer> symbols;

		public SymbolTableRec(TreeMap<String, Integer> symbols) {
			super();
			this.symbols = symbols;
		}
		public SymbolTableRec() {
			symbols = new TreeMap<String, Integer>();
		}
		public RecType getType() { return RecType.SymbolTableRecEmum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			out.writeInt(symbols.size());
			for(Entry<String, Integer> e : symbols.entrySet()) {
				out.writeInt(e.getKey().length());
				out.writeBytes(e.getKey());
				out.writeInt(e.getValue());
			}
		}  
		public void initObject(DataInputStream in) throws Exception {
			int n = in.readInt();
			for(int i=0;i<n;i++) {
				byte[] b = new byte[in.readInt()];
				in.read(b);
				symbols.put(new String(b),in.readInt());
			}
		}
		public int packingsize() {
			int rv= (Integer.SIZE*2*symbols.size())/8;
			for(String s : symbols.keySet())
				rv+=s.length();
			return rv;
		}
	}
	
	$endif$ */
	
	/**
	 * @param tableName
	 */
	public SymbolTables(Database db) {
		super("symbol_tables",db);
		Field dbid = new Field(this,"dbid", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((SymbolTable)obj).getDBID());
			}
		};
		Field metadata = new Field(this,"metadata", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((SymbolTable)obj).getMetadataID());
			}
		};
		Field symbol_map = new Field(this, "symbol_map", ColumnType.BLOB, null,  new ConstraintType[]{ConstraintType.NOT_NULL}, false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((SymbolTable)obj).getSymbolMap());
				$else$ */
				return new BoundValue(((SymbolTable)obj).symbolMapToBytes());
				/* $endif$ */
			}
		};

		this.fields.add(dbid);
		this.fields.add(metadata);
		this.fields.add(symbol_map);
		this.primaryKeys.add(dbid);	
		this.primaryKeys.add(metadata);	
	}
	
	/**
	INSERT INTO symbol_tables(dbid,metadata,symbol_map) VALUES(?,?,?)
	UPDATE symbol_tables SET dbid = ? ,metadata = ? ,symbol_map = ?  WHERE dbid = ?  AND metadata = ?
	 */
	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup() {
		setup_common();
		this.insert=new Insert() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				@SuppressWarnings("unchecked")
				final SymbolTableRec value = new SymbolTableRec((TreeMap<String,Integer>)vals.get(2).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				@SuppressWarnings("unchecked")
				final SymbolTableRec value = new SymbolTableRec((TreeMap<String,Integer>)vals.get(2).value);
				insert(key,value);
				return key.dbid;
			}
		};
	}
	$endif$ */
	
	public SymbolTable load(Metadata meta, long id) {
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();

		vals.add(this.fields.get(0).getPkeyVal(id));
		vals.add(this.fields.get(1).getPkeyVal(meta.getDBID()));
		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		Long nodeid=null;
		TreeMap<String,Integer> symbols=null;
		while (rs.next()) {
			nodeid=rs.value.key.dbid;
			symbols=((SymbolTableRec)rs.value.value).symbols;
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		Long nodeid=null;
		TreeMap<String,Integer> symbols=null;
		try {
			while (rs.next()) {
				nodeid=rs.getLong(1);
				symbols=SymbolTable.symbolMapFromBytes(rs.getBlob(3));
				break;
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading symbol table, metaid="+meta.getDBID());
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		if(symbols == null) {
			throw new RuntimeException("Unable to load symbol table, metaid="+meta.getDBID());
		}
		return new SymbolTable(meta, nodeid, symbols);
	}
	
	protected void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts) {
		if(owner instanceof Metadata) {
			deleteByMetadata(owner.getDBID(),stmts);
		}
		else {
			throw new RuntimeException("only meta datas own symbols tables! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}
	

}
