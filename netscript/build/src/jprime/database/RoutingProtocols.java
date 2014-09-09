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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.routing.StaticRoutingProtocol;


$if false == SEPARATE_PROP_TABLE $
import jprime.util.PersistentAttrMap;
$endif$

$else$ */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.routing.StaticRoutingProtocol;

/* $if false == SEPARATE_PROP_TABLE $ */
import jprime.util.PersistentAttrMap;
/* $endif$ */

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class RoutingProtocols extends Table {
	/* $if USE_FLAT_FILE_DB $
	private Delete deleteByOwner=null;
	$else$ */
	private PreparedStatement deleteByOwner=null;
	/* $endif$ */

	/**
	 * @param tableName
	 */
	public RoutingProtocols(Database db) {
		super("routing_protocols",db);
		Field dbid = new Field(this,"dbid", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((StaticRoutingProtocol)obj).getDBID());
			}
		};
		Field metadata = new Field(this,"metadata", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((StaticRoutingProtocol)obj).getMetadataId());
			}
		};
		Field parent = new Field(this,"parent", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL, ConstraintType.INDEX},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((StaticRoutingProtocol)obj).getParentId());
			}
		};
		Field type = new Field(this, "type", ColumnType.INTEGER, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((StaticRoutingProtocol)obj).getTypeId());
			}
		};
		Field kids = new Field(this, "kids", ColumnType.BLOB, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((StaticRoutingProtocol)obj).getChildIds());
				$else$ */
				return new BoundValue(ChildIdList.toBytes(((StaticRoutingProtocol)obj).getChildIds()));
				/* $endif$ */
			}
		};
		this.fields.add(dbid);
		this.fields.add(metadata);
		this.fields.add(parent);
		this.fields.add(type);
		this.fields.add(kids);
		this.primaryKeys.add(dbid);	
		this.primaryKeys.add(metadata);	
	}
	
	/**
	INSERT INTO routing_protocols(dbid,metadata,parent,type,kids)
	VALUES(?,?,?,?,?)
	
	UPDATE routing_protocols SET dbid = ? ,metadata = ? ,parent = ? ,type = ? ,kids = ?
	WHERE dbid = ?  AND metadata = ?
	
	DELETE FROM routing_protocols WHERE metadata = ? AND parent = ?
	 */
	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup() {
		setup_common();
		this.insert=new Insert() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				ModelNodeRecord value = new ModelNodeRecord(null,
						(Long)vals.get(0).value,
						(Long)vals.get(1).value,
						(Long)vals.get(2).value,
						(Integer)vals.get(3).value,
						(ChildIdList)vals.get(4).value
						$if false == SEPARATE_PROP_TABLE $
						, new PersistentAttrMap(null)
						$endif$
						);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				ModelNodeRecord value = new ModelNodeRecord(null,
						(Long)vals.get(0).value,
						(Long)vals.get(1).value,
						(Long)vals.get(2).value,
						(Integer)vals.get(3).value,
						(ChildIdList)vals.get(4).value
						$if false == SEPARATE_PROP_TABLE $
						, new PersistentAttrMap(null)
						$endif$
						);
				insert(key,value);
				return key.dbid;
			}
		};
		this.deleteByOwner=new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final TreeSet<Long> dbids = new TreeSet<Long>();
				final RecordKey key = new RecordKey((Long)vals.get(0).value,0);
				final long parent_dbid = (Long)vals.get(1).value;
				final Browser b = browse(key);
				final Tuple t=new Tuple();
				while(b.next(t)) {
					if(((ModelNodeRecord)t.getValue()).db_parent_id==parent_dbid)
						dbids.add(((RecordKey)t.getKey()).dbid);
				}
				while(dbids.size()>0) {
					key.dbid= dbids.pollFirst();
					delete(key);
				}
				return 0;
			}
		};
	}

	$else$ */
	public void setup() {
		super.setup();
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
	/* $endif$ */
	
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
			throw new RuntimeException("static routing protocols are only owned by model nodes and meta datas! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}	
	public StaticRoutingProtocol load(Metadata meta, long nodeid) {
		StaticRoutingProtocol rv = (StaticRoutingProtocol)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid));
		if(rv != null)
			return rv;

		//jprime.Console.err.println("Loading node "+new PKey(meta.getDBID(), nodeid));

		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();

		vals.add(this.primaryKeys.get(0).getPkeyVal(nodeid));
		vals.add(this.primaryKeys.get(1).getPkeyVal(meta.getDBID()));


		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		ModelNodeRecord rec=null; 
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			rec = (ModelNodeRecord)rs.value.value;
			rec.meta=meta;
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				rec = new ModelNodeRecord(meta,
						rs.getLong(1),
						rs.getLong(2),
						rs.getLong(3),
						rs.getInt(4),
						ChildIdList.fromBytes(rs.getBlob(5))
						/* $if false == SEPARATE_PROP_TABLE $ */
						, new PersistentAttrMap(null)
						/* $endif$ */
				);
						
				//jprime.Console.err.println("\tloaded record "+rec.dbid+", asked for "+nodeid+", type="+rec.db_type);
				break;
				//jprime.Console.err.println("\tloaded ["+rv.getClass().getSimpleName()+","+rv.getTypeId()+"]:"+rv.getDBID());
			}
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading model node, dbid="+nodeid+", metaid="+meta.getDBID());
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		db.getDBThread().closeRS(rs);
		/* $endif$ */
		if(rec != null)
			rv = EntityFactory.createStaticRoutingProtocol(rec.db_type, rec);
		if(rv==null) {
			try {
				throw new RuntimeException("What happened? node:"+new PKey(meta.getDBID(), nodeid));
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
}
