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

$if false == SEPARATE_PROP_TABLE $

$if UPDATE_CIDS_SEPARATE$

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
import jprime.ModelNodeRecord;
import jprime.PersistableObject;
import jprime.PersistableObject.Modified;
import jprime.State;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.util.PersistentAttrMap;

$else$

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
import jprime.ModelNodeRecord;
import jprime.PersistableObject;
import jprime.State;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.util.PersistentAttrMap;

$endif$

$else$

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
import jprime.ModelNodeRecord;
import jprime.PersistableObject;
import jprime.PersistableObject.Modified;
import jprime.State;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.database.FileDB.Browser;

$endif$

$else$ */

/* $if false == SEPARATE_PROP_TABLE $ */


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.PersistableObject;
import jprime.State;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.util.PersistentAttrMap;

/* $else$

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.PersistableObject;
import jprime.State;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;

$endif$ */

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */

/* $if UPDATE_CIDS_SEPARATE && USE_FLAT_FILE_DB $
public class ModelNodes extends ModelNodes_SimpleDBTable {
$else$ */
public class ModelNodes extends Table {
/* $endif$ */
	
	/* $if USE_FLAT_FILE_DB $

	public static class KidsRecord extends RecordValue implements Serializable { 
		private static final long serialVersionUID = -4677858674190911342L;
		public transient ChildIdList kids;
		public KidsRecord( ChildIdList kids) {
			super();
			this.kids=kids;
		}
		public KidsRecord() { }
		public RecType getType() { return RecType.KidsRecordEnum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			kids.flushObject(out);
		}
		public void initObject(DataInputStream in) throws Exception {  
			kids=ChildIdList.fromBytes(in);
		}
		public int packingsize() {
			return kids.packingsize();
		}
	}
	
	$if false == SEPARATE_PROP_TABLE $
	public static class AttrsRecord extends RecordValue implements Serializable { 
		private static final long serialVersionUID = -2405761172762315162L;
		public transient PersistentAttrMap attrs;
		public AttrsRecord( PersistentAttrMap attrs) {
			super();
			this.attrs=attrs;
		}
		public AttrsRecord() { }
		public RecType getType() { return RecType.AttrsRecordEnum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			attrs.flushObject(out);
		}
		public void initObject(DataInputStream in) throws Exception {  
			attrs=PersistentAttrMap.fromBytes(in);
		}
		public int packingsize() {
			return attrs.packingsize();
		}
	}
	$endif$
	
	protected class ModelNodeBrowser extends RecordBrowser {
		private int count=0;
		public ModelNodeBrowser(RecordKey key) {
			super(key);
		}
		@Override
		public boolean next() {
			if(count >jprime.util.GlobalProperties.FETCH_SIZE)
				return false;
			if(super.next()) {
				while(((ModelNodeRecord)value.value).db_type <= EntityFactory.START_MODEL_NODE_TYPES) {
					if(!super.next()) {
						//empty
						return false;
					}
				}
				return true;
				
				
			}
			return false;
		}
	}

	$endif$ */

	/* $if USE_FLAT_FILE_DB $
	private Select select_many=null;
	private Delete deleteByOwner=null;
	private Update update_local=null;
	private Update update_kids=null;
	private Update update_kids_and_local=null;
	$if false == SEPARATE_PROP_TABLE $
	private Update update_attrs=null;
	private Update update_attrs_and_kids=null;
	private Update update_attrs_and_local=null;
	$endif$
	
	
	private final Field dbid;
	private final Field metadata;
	private final Field parent;
	private final Field replica_metaid;
	private final Field replica;
	private final Field attached_link;
	private final Field type;
	private final Field db_order;
	private final Field uid;
	private final Field offset;
	private final Field size;
	private final Field name;
	private final Field has_been_replicated;
	private final Field kids;
	$if false == SEPARATE_PROP_TABLE $
	private final Field attrs;
	$endif$

	
	$else$ */
	private PreparedStatement select_many=null;
	private PreparedStatement deleteByOwner=null;
	private PreparedStatement update_local=null;
	private PreparedStatement update_kids=null;
	private PreparedStatement update_kids_and_local=null;
	/* $if false == SEPARATE_PROP_TABLE $ */
	private PreparedStatement update_attrs=null;
	private PreparedStatement update_attrs_and_kids=null;
	private PreparedStatement update_attrs_and_local=null;
	/* $endif$ */
	
	
	private final Field dbid;
	private final Field metadata;
	private final Field parent;
	private final Field replica_metaid;
	private final Field replica;
	private final Field attached_link;
	private final Field type;
	private final Field db_order;
	private final Field uid;
	private final Field offset;
	private final Field size;
	private final Field name;
	private final Field has_been_replicated;
	private final Field kids;
	/* $if false == SEPARATE_PROP_TABLE $ */
	private final Field attrs;
	/* $endif$ */

	/* $endif$ */

	/**
	 * @param tableName
	 */
	public ModelNodes(Database db) {
		super("model_nodes",db);
		this.dbid = new Field(this,"dbid", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getDBID());
			}
		};
		this.metadata = new Field(this,"metadata", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL}, true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getDBMetaID());
			}
		};
		this.parent = new Field(this,"parent", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getParentId());
			}
		};
		this.name = new Field(this, "name", ColumnType.VARCHAR_256, null,  new ConstraintType[]{ConstraintType.NOT_NULL}, false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				String n = ((ModelNode)obj).getName();
				if(n==null) {
					n="<un assigned>";
				}
				return new BoundValue(n);
			}
		};
		this.type = new Field(this, "type", ColumnType.INTEGER, null, new ConstraintType[]{ConstraintType.NOT_NULL}, false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getTypeId());
			}
		};
		this.replica_metaid = new Field(this, "replica_metaid ", ColumnType.BIGINT, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getReplicaMetaId());
			}
		};
		this.replica = new Field(this, "replica", ColumnType.BIGINT, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getReplicaId());
			}
		};
		this.attached_link = new Field(this, "attached_link", ColumnType.BIGINT, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getAttachedLinkId());
			}
		};
		this.db_order = new Field(this, "db_order", ColumnType.SMALLINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL}, false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((ModelNode)obj).getDBOrder());
			}
		};
		this.has_been_replicated = new Field(this, "has_been_replicated", ColumnType.SMALLINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL}, false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((ModelNode)obj).hasBeenReplicated());
				$else$ */
				return new BoundValue(((ModelNode)obj).hasBeenReplicated()?1:0);
				/* $endif$ */
			}
		};
		this.uid = new Field(this, "uid", ColumnType.BIGINT, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				final ModelNode o = ((ModelNode)obj);
				if(o.getMetadata().getState().lte(State.PRE_COMPILED)) {
					return new BoundValue(0L);
				}
				return new BoundValue(o.getUID());
			}
		};
		this.offset = new Field(this, "offset", ColumnType.INTEGER, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				final ModelNode o = ((ModelNode)obj);
				if(o.getMetadata().getState().lte(State.PRE_COMPILED)) {
					return new BoundValue(0);
				}
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue((int)o.getOffset());
				$else$ */
				return new BoundValue(o.getOffset());
				/* $endif$ */
			}
		};
		this.size = new Field(this, "size", ColumnType.INTEGER, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				final ModelNode o = ((ModelNode)obj);
				if(o.getMetadata().getState().lte(State.PRE_COMPILED)) {
					return new BoundValue(0);
				}
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue((int)o.getSize());
				$else$ */
				return new BoundValue(o.getSize());
				/* $endif$ */
			}
		};
		this.kids = new Field(this, "kids", ColumnType.BLOB, null,  new ConstraintType[]{ConstraintType.NOT_NULL}, false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((ModelNode)obj).getChildIds());
				$else$ */
				return new BoundValue(ChildIdList.toBytes(((ModelNode)obj).getChildIds()));
				/* $endif$ */
			}
		};
		/* $if false == SEPARATE_PROP_TABLE $ */
		this.attrs = new Field(this, "attrs", ColumnType.BLOB, null,  new ConstraintType[]{ConstraintType.NOT_NULL}, false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((ModelNode)obj).getPersistentAttrMap());
				$else$ */
				return new BoundValue(PersistentAttrMap.toBytes(((ModelNode)obj).getPersistentAttrMap()));
				/* $endif$ */
			}
		};
		/* $endif$ */

		this.fields.add(dbid);
		this.fields.add(metadata);
		this.fields.add(parent);

		this.fields.add(replica_metaid);
		this.fields.add(replica);
		this.fields.add(attached_link);
		this.fields.add(type);
		this.fields.add(db_order);
		this.fields.add(uid);
		this.fields.add(offset);
		this.fields.add(size);
		this.fields.add(name);
		this.fields.add(has_been_replicated);
		this.fields.add(kids);
		/* $if false == SEPARATE_PROP_TABLE $ */
		this.fields.add(attrs);
		/* $endif$ */

		this.primaryKeys.add(dbid);	
		this.primaryKeys.add(metadata);	
	}

	/**
	INSERT INTO model_nodes(dbid,metadata,parent,replica_metaid ,replica,attached_link,type,db_order,uid,offset,size,name,has_been_replicated,kids,attrs)
	VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)

	UPDATE model_nodes SET dbid = ? ,metadata = ? ,parent = ? ,replica_metaid  = ? ,replica = ? ,attached_link = ? ,type = ? ,db_order = ? ,uid = ? ,offset = ? ,size = ? ,name = ? ,has_been_replicated = ? ,kids = ?, attrs = ? 
	WHERE dbid = ?  AND metadata = ?

	SELECT  dbid, metadata, parent, replica_metaid , replica, attached_link, type, db_order, uid, offset, size, name, has_been_replicated, kids, attrs FROM model_nodes
	WHERE metadata = ?  AND dbid BETWEEN ? AND ?  AND types > 999 ORDER BY dbid ASC

	DELETE FROM model_nodes
	WHERE metadata = ? AND parent = ?
	 */
	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup() {
		setup_common();

		this.select = new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser = new RecordBrowser(new RecordKey((Long)vals.get(0).value, (Long)vals.get(1).value));
				return 0;
			}
		};
		this.select_many=new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser=new ModelNodeBrowser(
						new RecordKey((Long)vals.get(0).value, (Long)vals.get(1).value));
				return 0;
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
		this.delete = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				delete(new RecordKey((Long)vals.get(0).value, (Long)vals.get(1).value));
				return 0;
			}
		};
		this.deleteByMetadata = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final TreeSet<Long> dbids = new TreeSet<Long>();
				final RecordKey key = new RecordKey((Long)vals.get(0).value,0);
				final Browser b = browse(key);
				final Tuple t=new Tuple();
				while(b.next(t)) {
					dbids.add(((RecordKey)t.getKey()).dbid);
				}
				while(dbids.size()>0) {
					key.dbid= dbids.pollFirst();
					delete(key);
				}
				return 0;
			}
		};
		
		this.insert=new Insert() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(0).value, (Long)vals.get(1).value);
				final ModelNodeRecord value = new ModelNodeRecord(null,
						(Long)vals.get(1).value,
						(Long)vals.get(0).value,
						(Long)vals.get(2).value,
						(Long)vals.get(3).value,
						(Long)vals.get(4).value,
						(Long)vals.get(5).value,
						(Integer)vals.get(6).value,
						(Integer)vals.get(7).value,
						(Long)vals.get(8).value,
						(Integer)vals.get(9).value,
						(Integer)vals.get(10).value,
						(String)vals.get(11).value,
						(Boolean)vals.get(12).value,
						(ChildIdList)vals.get(13).value
						$if false == SEPARATE_PROP_TABLE $
						, (PersistentAttrMap)vals.get(14).value
						$endif$
				);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				$if false == SEPARATE_PROP_TABLE $
				final RecordKey key = new RecordKey((Long)vals.get(13).value, (Long)vals.get(14).value);
				$else$
				final RecordKey key = new RecordKey((Long)vals.get(12).value, (Long)vals.get(13).value);
				$endif$
				
				final ModelNodeRecord value = new ModelNodeRecord(null,
						$if false == SEPARATE_PROP_TABLE $
						(Long)vals.get(14).value,
						(Long)vals.get(13).value,
						$else$
						(Long)vals.get(13).value,
						(Long)vals.get(12).value,
						$endif$
						(Long)vals.get(0).value,
						(Long)vals.get(1).value,
						(Long)vals.get(2).value,
						(Long)vals.get(3).value,
						(Integer)vals.get(4).value,
						(Integer)vals.get(5).value,
						(Long)vals.get(6).value,
						(Integer)vals.get(7).value,
						(Integer)vals.get(8).value,
						(String)vals.get(9).value,
						(Boolean)vals.get(10).value,
						(ChildIdList)vals.get(11).value
						$if false == SEPARATE_PROP_TABLE $
						, (PersistentAttrMap)vals.get(12).value
						$endif$
				);
				update(key,value);
				return key.dbid;
			}
		};
		
		update_local = new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(11).value, (Long)vals.get(12).value);
				final ModelNodeRecord value = new ModelNodeRecord(null,
						(Long)vals.get(12).value,
						(Long)vals.get(11).value,
						(Long)vals.get(0).value,
						(Long)vals.get(1).value,
						(Long)vals.get(2).value,
						(Long)vals.get(3).value,
						(Integer)vals.get(4).value,
						(Integer)vals.get(5).value,
						(Long)vals.get(6).value,
						(Integer)vals.get(7).value,
						(Integer)vals.get(8).value,
						(String)vals.get(9).value,
						(Boolean)vals.get(10).value,
						null
						$if false == SEPARATE_PROP_TABLE $
						, null
						$endif$
				);
				update(key,value);
				return key.dbid;
			}
		};
		
		update_kids = new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				$if UPDATE_CIDS_SEPARATE  $
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(2).value);
				final KidsRecord value = new KidsRecord((ChildIdList)vals.get(0).value);
				update(key,value);
				return key.dbid;
				$else$
				throw new RuntimeException("wtf?");
				$endif                                                                
			}
		};
		
		update_kids_and_local = new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(12).value, (Long)vals.get(13).value);
				final ModelNodeRecord value = new ModelNodeRecord(null,
						(Long)vals.get(13).value,
						(Long)vals.get(12).value,
						(Long)vals.get(0).value,
						(Long)vals.get(1).value,
						(Long)vals.get(2).value,
						(Long)vals.get(3).value,
						(Integer)vals.get(4).value,
						(Integer)vals.get(5).value,
						(Long)vals.get(6).value,
						(Integer)vals.get(7).value,
						(Integer)vals.get(8).value,
						(String)vals.get(9).value,
						(Boolean)vals.get(10).value,
						(ChildIdList)vals.get(11).value
						$if false == SEPARATE_PROP_TABLE $
						, null
						$endif$
				);
				update(key,value);
				return key.dbid;
			}
		};
		
		$if false == SEPARATE_PROP_TABLE $
		update_attrs = new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				$if UPDATE_CIDS_SEPARATE  $
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(2).value);
				final AttrsRecord value = new AttrsRecord((PersistentAttrMap)vals.get(0).value);
				update(key,value);
				return key.dbid;
				$else$
				throw new RuntimeException("wtf?");
				$endif                                                                
			}
		};
		update_attrs_and_kids = new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				$if UPDATE_CIDS_SEPARATE  $
				final RecordKey key = new RecordKey((Long)vals.get(2).value, (Long)vals.get(3).value);
				final KidsRecord k = new KidsRecord((ChildIdList)vals.get(0).value);
				final AttrsRecord a = new AttrsRecord((PersistentAttrMap)vals.get(1).value);
				update(key,k);
				update(key,a);
				return key.dbid;
				$else$
				throw new RuntimeException("wtf?");
				$endif                                                                
			}
		};
		update_attrs_and_local = new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(12).value, (Long)vals.get(13).value);
				final ModelNodeRecord value = new ModelNodeRecord(null,
						(Long)vals.get(13).value,
						(Long)vals.get(12).value,
						(Long)vals.get(0).value,
						(Long)vals.get(1).value,
						(Long)vals.get(2).value,
						(Long)vals.get(3).value,
						(Integer)vals.get(4).value,
						(Integer)vals.get(5).value,
						(Long)vals.get(6).value,
						(Integer)vals.get(7).value,
						(Integer)vals.get(8).value,
						(String)vals.get(9).value,
						(Boolean)vals.get(10).value,
						null,
						(PersistentAttrMap)vals.get(11).value
				);
				update(key,value);
				return key.dbid;
			}
		};
		$endif$
		
	}

	$else$ */
	@Override
	protected void setup() {
		for(Field f:primaryKeys)
			f.isPKEY=true;
		for(Field f:fields) {
			if(!f.isPKEY && f.isGenerated) {
				throw new RuntimeException("the field "+f.name+" in table "+tableName+" is not a pkey but its generated!");
			}
			if(f.isGenerated)
				this.haveGeneratedValues=true;
			if(f.isMetadataId)
				metadataid=f;
		}

		String s = null;
		try {
			s="DELETE FROM "+tableName+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("DELETE SQL for "+tableName+" = "+s);
			
			delete=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		try {
			s="DELETE FROM "+tableName+" WHERE "
			+" "+metadata.name+" = ? ";
			
			jprime.Console.out.println("DELETE-BY-METADATA SQL for "+tableName+" = "+s);
			
			deleteByMetadata=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		try {
			s="DELETE FROM "+tableName+" WHERE "
			+" "+metadata.name+" = ? AND "+parent.name+" = ? ";

			jprime.Console.out.println("DELETE BY OWNER SQL for "+tableName+" = "+s);
			
			deleteByOwner=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		try {
			s="SELECT "
			+" "+metadata.name+", "
			+" "+dbid.name+", "
			+" "+parent.name+", "
			+" "+replica_metaid.name+", "
			+" "+replica.name+", "
			+" "+attached_link.name+", "
			+" "+type.name+", "
			+" "+db_order.name+", "
			+" "+uid.name+", "
			+" "+offset.name+", "
			+" "+size.name+", "
			+" "+name.name+", "
			+" "+has_been_replicated.name+", "
			+" "+kids.name+" "
			/* $if false == SEPARATE_PROP_TABLE $ */
			+", "+attrs.name+" "
			/* $endif$ */
			+" FROM "+tableName
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("SELECT SQL for "+tableName+" = "+s);
			
			select=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}
		/* $if false$
		//select many old
		try {
			s="SELECT "
				+" "+metadata.name+", "
				+" "+dbid.name+", "
				+" "+parent.name+", "
				+" "+replica_metaid.name+", "
				+" "+replica.name+", "
				+" "+attached_link.name+", "
				+" "+type.name+", "
				+" "+db_order.name+", "
				+" "+uid.name+", "
				+" "+offset.name+", "
				+" "+size.name+", "
				+" "+name.name+", "
				+" "+has_been_replicated.name+", "
				+" "+kids.name+" "
				$if false == SEPARATE_PROP_TABLE $
				+", "+attrs.name+" "
				$endif$
				+" FROM "+tableName
				+" WHERE "
				+" "+metadata.name+" = ? AND "+dbid.name+" BETWEEN ? AND ? "
				+" AND "+type.name+" > "+EntityFactory.START_MODEL_NODE_TYPES
				+" ORDER BY "+dbid.name+" ASC";
			
			jprime.Console.out.println("SELECT-MANY SQL for "+tableName+" = "+s);
			
			select_many=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}
		$endif$ */
		try {
			s="SELECT DISTINCT "
				+" C."+metadata.name+", "
				+" C."+dbid.name+", "
				+" C."+parent.name+", "
				+" C."+replica_metaid.name+", "
				+" C."+replica.name+", "
				+" C."+attached_link.name+", "
				+" C."+type.name+", "
				+" C."+db_order.name+", "
				+" C."+uid.name+", "
				+" C."+offset.name+", "
				+" C."+size.name+", "
				+" C."+name.name+", "
				+" C."+has_been_replicated.name+", "
				+" C."+kids.name+" "
				/* $if false == SEPARATE_PROP_TABLE $ */
				+", C."+attrs.name
				/* $endif$ */
				+" FROM "+tableName+" P JOIN "+tableName+" C ON C."+parent.name+" = P."+parent.name+" "
				+" WHERE "
				+" P."+metadata.name+" = ? AND P."+dbid.name+" = ? AND"
				+" C."+metadata.name+" = ? AND C."+dbid.name+" >= P."+dbid.name+" AND"
				+" C."+type.name+" > "+EntityFactory.START_MODEL_NODE_TYPES
				+" ORDER BY C."+dbid.name+" ASC";
			jprime.Console.out.println("SELECT-MANY SQL for "+tableName+" = "+s);
			
			select_many=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}
		
		try {
			s="INSERT INTO "+tableName+"("
			+metadata.name+","
			+dbid.name+","
			+parent.name+","
			+replica_metaid.name+","
			+replica.name+","
			+attached_link.name+","
			+type.name+","
			+db_order.name+","
			+uid.name+","
			+offset.name+","
			+size.name+","
			+name.name+","
			+has_been_replicated.name+","
			+kids.name
			/* $if false == SEPARATE_PROP_TABLE $ */
			+","+attrs.name
			/* $endif$ */
			+") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?"
			/* $if false == SEPARATE_PROP_TABLE $ */
			+",?)"
			/* $else$
			+")"
			$endif$ */
			;
			
			jprime.Console.out.println("INSERT SQL for "+tableName+" = "+s);
			
			insert=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}
		
		try {
			s="UPDATE "+tableName+" SET "
			+" "+parent.name+" = ? ,"
			+" "+replica_metaid.name+" = ? ,"
			+" "+replica.name+" = ? ,"
			+" "+attached_link.name+" = ? ,"
			+" "+type.name+" = ? ,"
			+" "+db_order.name+" = ? ,"
			+" "+uid.name+" = ? ,"
			+" "+offset.name+" = ? ,"
			+" "+size.name+" = ? ,"
			+" "+name.name+" = ? ,"
			+" "+has_been_replicated.name+" = ? ,"
			+" "+kids.name+" = ? "
			/* $if false == SEPARATE_PROP_TABLE $ */
			+", "+attrs.name+" = ? "
			/* $endif$ */
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("UPDATE SQL for "+tableName+" = "+s);
			
			update=db.getFlushConnection().prepareStatement(s,Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}
		
		try {
			s="UPDATE "+tableName+" SET "
			+" "+parent.name+" = ? ,"
			+" "+replica_metaid.name+" = ? ,"
			+" "+replica.name+" = ? ,"
			+" "+attached_link.name+" = ? ,"
			+" "+type.name+" = ? ,"
			+" "+db_order.name+" = ? ,"
			+" "+uid.name+" = ? ,"
			+" "+offset.name+" = ? ,"
			+" "+size.name+" = ? ,"
			+" "+name.name+" = ? ,"
			+" "+has_been_replicated.name+" = ? "
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("update_local SQL for "+tableName+" = "+s);
			
			update_local=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		try {
			s="UPDATE "+tableName+" SET "
			+" "+kids.name+" = ? "
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("update_kids SQL for "+tableName+" = "+s);
			
			update_kids=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		try {
			s="UPDATE "+tableName+" SET "
			+" "+parent.name+" = ? ,"
			+" "+replica_metaid.name+" = ? ,"
			+" "+replica.name+" = ? ,"
			+" "+attached_link.name+" = ? ,"
			+" "+type.name+" = ? ,"
			+" "+db_order.name+" = ? ,"
			+" "+uid.name+" = ? ,"
			+" "+offset.name+" = ? ,"
			+" "+size.name+" = ? ,"
			+" "+name.name+" = ? ,"
			+" "+has_been_replicated.name+" = ? ,"
			+" "+kids.name+" = ? "
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("update_kids_and_local SQL for "+tableName+" = "+s);
			
			update_kids_and_local=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		/* $if false == SEPARATE_PROP_TABLE $ */
		try {
			s="UPDATE "+tableName+" SET "
			+" "+attrs.name+" = ? "
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("update_attrs SQL for "+tableName+" = "+s);
			
			update_attrs=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		try {
			s="UPDATE "+tableName+" SET "
			+" "+kids.name+" = ? ,"
			+" "+attrs.name+" = ? "
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("update_attrs_and_kids SQL for "+tableName+" = "+s);
			
			update_attrs_and_kids=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}

		try {
			s="UPDATE "+tableName+" SET "
			+" "+parent.name+" = ? ,"
			+" "+replica_metaid.name+" = ? ,"
			+" "+replica.name+" = ? ,"
			+" "+attached_link.name+" = ? ,"
			+" "+type.name+" = ? ,"
			+" "+db_order.name+" = ? ,"
			+" "+uid.name+" = ? ,"
			+" "+offset.name+" = ? ,"
			+" "+size.name+" = ? ,"
			+" "+name.name+" = ? ,"
			+" "+has_been_replicated.name+" = ? ,"
			+" "+attrs.name+" = ? "
			+" WHERE "
			+" "+metadata.name+" = ? AND "+dbid.name+" = ? ";
			
			jprime.Console.out.println("update_attrs_and_local SQL for "+tableName+" = "+s);
			
			update_attrs_and_local=db.getFlushConnection().prepareStatement(s);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+s);
			throw new RuntimeException(e);
		}
		/* $endif$ */
	}
	/* $endif$ */

	protected void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts) {
		if(owner instanceof Metadata) {
			deleteByMetadata(owner.getDBID(),stmts);
		}
		else if(owner instanceof ModelNode) {
			/* $if SEPARATE_PROP_TABLE $
			db.Attrs.deleteByOwner(owner, stmts);
			$endif$ */
			ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>(2);
			vals.add(metadata.getPkeyVal(((ModelNode)owner).getMetadata().getDBID()));
			vals.add(parent.getPkeyVal(owner.getDBID()));
			stmts.add(new SQLStmt(vals, deleteByOwner));
		}
		else {
			throw new RuntimeException("model nodes are only owned by model node and meta datas! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}
	public List<ModelNode> loadMany(Metadata meta, long id) {
		//jprime.Console.err.println("Loading node(s) meta="+meta.getDBID()+", low="+lowid+", high="+highid);
		ArrayList<ModelNode> rv = null;
		
		ModelNode t = (ModelNode)meta.findLoadedObj(new PKey(meta.getDBID(), id));
		if(t != null) {
			//if we already have the low id lets just return it!
			rv = new ArrayList<ModelNode>(1);
			rv.add(t);
			return rv;
		}
		rv = new ArrayList<ModelNode>(jprime.util.GlobalProperties.FETCH_SIZE);


		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>(3);

		vals.add(metadata.getPkeyVal(meta.getDBID()));
		vals.add(dbid.getPkeyVal(id));
		/* $if false == USE_FLAT_FILE_DB $ */
		vals.add(metadata.getPkeyVal(meta.getDBID()));
		/* $endif$ */

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select_many);
		ModelNode low=null;
		LinkedList<ModelNodeRecord> recs=new LinkedList<ModelNodeRecord>();
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			ModelNode temp = (ModelNode)meta.findLoadedObj(new PKey(meta.getDBID(), rs.value.key.dbid));
			if(temp == null) {
				final ModelNodeRecord rec = (ModelNodeRecord)rs.value.value;
				rec.meta = meta;
				recs.add(rec);
			}
			else {
				if(temp.getDBID()==id) {
					low=temp;
				}
				rv.add(temp);
			}
		}
		ls.realeaseLock();
		for(ModelNodeRecord rec : recs) {
			ModelNode temp = EntityFactory.createNode(rec.db_type, rec);
			if(low == null && temp.getDBID()==id) {
				low=temp;
			}
			rv.add(temp);
		}
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				final PKey pkey = new PKey(meta.getDBID(), rs.getLong(2));
				ModelNode temp = (ModelNode)meta.findLoadedObj(pkey);
				if(temp == null) {
					recs.add(new ModelNodeRecord(meta,
							rs.getLong(2),
							rs.getLong(1),
							rs.getLong(3),
							rs.getLong(4),
							rs.getLong(5),
							rs.getLong(6),
							rs.getInt(7),
							rs.getInt(8),
							rs.getLong(9),
							rs.getInt(10),
							rs.getInt(11),
							rs.getString(12),
							(rs.getShort(13)==0?false:true),
							ChildIdList.fromBytes(rs.getBlob(14))
							/* $if false == SEPARATE_PROP_TABLE $ */
							, PersistentAttrMap.fromBytes(rs.getBlob(15))
							/* $endif$ */
					));
				}
				else if(temp.getDBID()==id) {
					low=temp;
				}
			}
			db.getDBThread().closeRS(rs);
			for(ModelNodeRecord rec: recs) {
				ModelNode m = EntityFactory.createNode(rec.db_type, rec);
				rv.add(m);
				if(rec.dbid==id)
					low=m;
			}
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading model nodes, meta="+meta.getDBID()+", dbid="+id);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */

		if(low == null) {
			//jprime.Console.err.println("Loading the lowid since it was null, it is probably new and not flushed out yet....");
			low=load(meta, id);
			rv.add(0,low);
		}
		if(low==null) {
			try {
				throw new RuntimeException("What happened? low==null!");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		else if(rv.size()==0 ) {
			try {
				throw new RuntimeException("What happened? rv.size()==0");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		else if(rv.get(0)!=low) {
			try {
				throw new RuntimeException("What happened? rv[0]="+rv.get(0).getDBID()+" dbid="+id);
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}

	public ModelNode load(Metadata meta, long nodeid) {
		ModelNode rv = (ModelNode)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid));
		if(rv != null)
			return rv;

		//jprime.Console.err.println("Loading node "+new PKey(meta.getDBID(), nodeid));

		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>(2);

		vals.add(metadata.getPkeyVal(meta.getDBID()));
		vals.add(dbid.getPkeyVal(nodeid));

		//System.out.println("[0]Loading model node.");System.out.flush();
		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		ModelNodeRecord rec=null; 
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		try {
				while (rs.next()) {
					rec = (ModelNodeRecord)rs.value.value;
					rec.meta = meta;
					break;
				}
				ls.realeaseLock();
		} catch (Exception e1) {
			jprime.Console.err.println("Error loading model node, dbid="+nodeid+", metaid="+meta.getDBID());
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
//System.out.println("[1]Done loading model node.");System.out.flush();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				rec = new ModelNodeRecord(meta,
						rs.getLong(2),
						rs.getLong(1),
						rs.getLong(3),
						rs.getLong(4),
						rs.getLong(5),
						rs.getLong(6),
						rs.getInt(7),
						rs.getInt(8),
						rs.getLong(9),
						rs.getInt(10),
						rs.getInt(11),
						rs.getString(12),
						(rs.getShort(13)==0?false:true),
						ChildIdList.fromBytes(rs.getBlob(14))
						/* $if false == SEPARATE_PROP_TABLE $ */
						, PersistentAttrMap.fromBytes(rs.getBlob(15))
						/* $endif$ */
				);
				//jprime.Console.err.println("\tloaded record "+rec.dbid+", asked for "+nodeid+", type="+rec.db_type);
				break;
				//jprime.Console.err.println("\tloaded ["+rv.getClass().getSimpleName()+","+rv.getTypeId()+"]:"+rv.getDBID());
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading model node, dbid="+nodeid+", metaid="+meta.getDBID());
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		if(rec==null) {
			try {
				throw new RuntimeException("What happened? node:"+new PKey(meta.getDBID(), nodeid));
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		rv = EntityFactory.createNode(rec.db_type, rec);
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

	protected void delete(PersistableObject obj, List<SQLStmt> stmts) {
		ArrayList<BoundValue> vals=new ArrayList<Field.BoundValue>(2);
		vals.add(metadata.getValue(obj));
		vals.add(dbid.getValue(obj));
		stmts.add(new SQLStmt(vals,delete));
	}
	
	protected void deleteByMetadata(long mid, List<SQLStmt> stmts) {
		ArrayList<BoundValue> vals=new ArrayList<Field.BoundValue>(1);
		vals.add(metadata.getPkeyVal(mid));
		stmts.add(new SQLStmt(vals,deleteByMetadata));
	}
	
	public List<SQLStmt> processObject(PersistableObject obj, boolean delete) {
		List<SQLStmt> rv=new ArrayList<SQLStmt>();
		if(delete) {
			switch(obj.getPersistableState()) {
			case UNMODIFIED:
			case MODIFIED:
			case DEAD:
				delete(obj,rv);
				break;
			case NEW:
			case ORPHAN:
				//no op
				break;
			}
			return rv;
		}
		else {
			int todo=-1;
			switch(obj.getPersistableState()) {
			case MODIFIED:
			{
				/* $if UPDATE_CIDS_SEPARATE $
				final int mods = obj.whatsModified();
				if( (mods & Modified.ALL.id) == Modified.ALL.id) {
					todo=7;
				}
				else {
					final boolean attrs = (mods & Modified.ATTRS.id) == Modified.ATTRS.id;
					final boolean kids = (mods & Modified.CHILDREN.id) == Modified.CHILDREN.id;
					final boolean local = (mods & Modified.LOCAL_PROPS.id) == Modified.LOCAL_PROPS.id;
					if(attrs) {
						if(kids) {
							if(local) {
								todo=7;
							}
							else {
								$if SEPARATE_PROP_TABLE $
								todo=2;
								$else                                                                                                                
								todo=6;
								$endif$
							}
						}
						else {
							if(local) {
								$if SEPARATE_PROP_TABLE $
								todo=3;
								$else                                                                                                                
								todo=5;
								$endif$
							}
							else {
								$if SEPARATE_PROP_TABLE $
								todo=2;
								$else                                                                                                                
								todo=4;
								$endif$
							}
						}					
					}
					else {
						if(kids) {
							if(local) {
								$if SEPARATE_PROP_TABLE $
								todo=7;
								$else                                                                                                                
								todo=3;
								$endif$
							}
							else {
								todo=2;
							}
						}
						else {
							if(local) {
								todo=1;
							}
							else {
								todo=0;
							}
						}
					}
				}
				$else$ */
				todo=7;
				/* $endif$ */
			}
			break;
			case NEW:
				todo=8;
			break;
			case UNMODIFIED:
			case ORPHAN:
				return rv;
			case DEAD:
				throw new RuntimeException("how did we get here?");
			}
			ArrayList<BoundValue> values=null;
			/* $if false $
			case	attrs	kids	local
			0		0		0		0		-->	shouldn't happen
			1		0		0		1		-->	update_local
			2		0		1		0		-->	update_kids
			$if SEPARATE_PROP_TABLE $
			8		0		1		1		-->	update
			2		1		0		0		-->	update_kids
			3		1		0		1		-->	update_kids_and_local
			2		1		1		0		-->	update_kids
			$else$
			3		0		1		1		-->	update_kids_and_local
			4		1		0		0		-->	update_attrs
			5		1		0		1		-->	update_attrs_and_local
			6		1		1		0		-->	update_attrs_and_kids
			$endif$
			7		1		1		1		-->	update
			---
			8								--> insert
			$endif$ */
			switch(todo) {
			case 0:
				throw new RuntimeException("the node says it is modified but its mods say it is not!");
			case 1:
				values=new ArrayList<Field.BoundValue>(13);
				values.add(parent.getValue(obj));
				values.add(replica_metaid.getValue(obj));
				values.add(replica.getValue(obj));
				values.add(attached_link.getValue(obj));
				values.add(type.getValue(obj));
				values.add(db_order.getValue(obj));
				values.add(uid.getValue(obj));
				values.add(offset.getValue(obj));
				values.add(size.getValue(obj));
				values.add(name.getValue(obj));
				values.add(has_been_replicated.getValue(obj));
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				rv.add(new SQLStmt(values,update_local));
				break;
			case 2:
				values=new ArrayList<Field.BoundValue>(3);
				values.add(kids.getValue(obj));
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				rv.add(new SQLStmt(values,update_kids));
				break;
			case 3:
				values=new ArrayList<Field.BoundValue>(14);
				values.add(parent.getValue(obj));
				values.add(replica_metaid.getValue(obj));
				values.add(replica.getValue(obj));
				values.add(attached_link.getValue(obj));
				values.add(type.getValue(obj));
				values.add(db_order.getValue(obj));
				values.add(uid.getValue(obj));
				values.add(offset.getValue(obj));
				values.add(size.getValue(obj));
				values.add(name.getValue(obj));
				values.add(has_been_replicated.getValue(obj));
				values.add(kids.getValue(obj));
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				rv.add(new SQLStmt(values,update_kids_and_local));
				break;
			case 4:
				/* $if false == SEPARATE_PROP_TABLE $ */
				values=new ArrayList<Field.BoundValue>(3);
				values.add(attrs.getValue(obj));
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				rv.add(new SQLStmt(values,update_attrs));
				break;
				/* $else$
				throw new RuntimeException("how did this happen?");
				$endif$ */
			case 5:
				/* $if false == SEPARATE_PROP_TABLE $ */
				values=new ArrayList<Field.BoundValue>(14);
				values.add(parent.getValue(obj));
				values.add(replica_metaid.getValue(obj));
				values.add(replica.getValue(obj));
				values.add(attached_link.getValue(obj));
				values.add(type.getValue(obj));
				values.add(db_order.getValue(obj));
				values.add(uid.getValue(obj));
				values.add(offset.getValue(obj));
				values.add(size.getValue(obj));
				values.add(name.getValue(obj));
				values.add(has_been_replicated.getValue(obj));
				values.add(attrs.getValue(obj));
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				rv.add(new SQLStmt(values,update_attrs_and_local));

				break;
				/* $else$
				throw new RuntimeException("how did this happen?");
				$endif$ */
			case 6:
				/* $if false == SEPARATE_PROP_TABLE $ */
				values=new ArrayList<Field.BoundValue>(4);
				values.add(kids.getValue(obj));
				values.add(attrs.getValue(obj));
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				rv.add(new SQLStmt(values,update_attrs_and_kids));
				break;
				/* $else$
				throw new RuntimeException("how did this happen?");
				$endif$ */
			case 7:
				values=new ArrayList<Field.BoundValue>(15);
				values.add(parent.getValue(obj));
				values.add(replica_metaid.getValue(obj));
				values.add(replica.getValue(obj));
				values.add(attached_link.getValue(obj));
				values.add(type.getValue(obj));
				values.add(db_order.getValue(obj));
				values.add(uid.getValue(obj));
				values.add(offset.getValue(obj));
				values.add(size.getValue(obj));
				values.add(name.getValue(obj));
				values.add(has_been_replicated.getValue(obj));
				values.add(kids.getValue(obj));
				/* $if false == SEPARATE_PROP_TABLE $ */
				values.add(attrs.getValue(obj));
				/* $endif$ */
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				rv.add(new SQLStmt(values,update));
				break;
			case 8:
				values=new ArrayList<Field.BoundValue>(15);
				values.add(metadata.getValue(obj));
				values.add(dbid.getValue(obj));
				values.add(parent.getValue(obj));
				values.add(replica_metaid.getValue(obj));
				values.add(replica.getValue(obj));
				values.add(attached_link.getValue(obj));
				values.add(type.getValue(obj));
				values.add(db_order.getValue(obj));
				values.add(uid.getValue(obj));
				values.add(offset.getValue(obj));
				values.add(size.getValue(obj));
				values.add(name.getValue(obj));
				values.add(has_been_replicated.getValue(obj));
				values.add(kids.getValue(obj));
				/* $if false == SEPARATE_PROP_TABLE $ */
				values.add(attrs.getValue(obj));
				/* $endif$ */
				rv.add(new SQLStmt(values,insert));
				break;
			default:
				throw new RuntimeException("what happened?");
			}
			return rv;
		}
	}

	
	/* $if false == USE_FLAT_FILE_DB $ */
	public List<String> createTable() {
		LinkedList<String> l = new LinkedList<String>();
		String rv =null;
		String unique=null;
		
		
		for(Field f : fields) {
			if(rv ==  null) {
				if(db.getDBType()==DBType.MYSQL)
					rv ="CREATE TABLE IF NOT EXISTS "+tableName+"(";
				else
					rv ="CREATE TABLE "+tableName+"(";
			}
			else
				rv+=",";
			rv+=f.name+" "+f.type.getString(db.getDBType());
			if(f.defaultValue!=null)
				rv+=" DEFAULT "+f.defaultValue;
			for(ConstraintType c : f.constraints) {
				switch(c) {
				case AUTO_INCREMENT:
					if(db.getDBType()==DBType.MYSQL) {
						rv+=" AUTO_INCREMENT";
					}
					else {
						if(f.isGenerated==false) {
							try {
								throw new RuntimeException("The field "+f.name+" in table "+tableName
										+" is not a primary key but is generated! This is not supported by DERBY");
							} catch (Exception e) {
								jprime.Console.err.printStackTrace(e);
								jprime.Console.halt(100);
							}
						}
						else {
							rv+=" GENERATED ALWAYS AS IDENTITY (START WITH 5, INCREMENT BY 1)";
						}
					}
					break;
				case NOT_NULL:
					rv+=" NOT NULL";
					break;
				case UNIQUE:
					if(unique==null) {
						if(db.getDBType()==DBType.MYSQL)
							unique=", UNIQUE KEY(";
						else
							unique=", UNIQUE (";
					}
					else unique+=", ";
					unique+=f.name;
					break;
				case INDEX:
					//no op
					break;
				default:
					try {
						throw new RuntimeException("invalid contraint type:"+c);
					} catch (Exception e) {
						jprime.Console.err.printStackTrace(e);
						jprime.Console.halt(100);
					}
				}
			}
		}
		
		String p=null;
		for(Field f : primaryKeys) {
			if(p==null)
				p=", PRIMARY KEY (";
			else p+=",";
			p+=f.name;
		}
		if(p!=null)
			rv+=p+")";
		if(unique!=null)
			rv+=unique+")";
		if(db.getDBType()==DBType.MYSQL) {
			for(Field f : fields) {
				for(ConstraintType c : f.constraints) {
					switch(c) {
					case INDEX:
						rv+=", INDEX "+f.name+"_index ("+f.name+")";
						break;
					default:
						//no op
					}
				}
			}
			rv+=") ENGINE = INNODB";
			l.add(rv);
		}
		else {
			rv+=")";
			l.add(rv);
			for(Field f : fields) {
				for(ConstraintType c : f.constraints) {
					switch(c) {
					case INDEX:
						rv="CREATE INDEX "+tableName+"_"+f.name+"_index ON "+tableName+"("+f.name+")";
						l.add(rv);
						break;
					default:
						//no op
					}
				}
			}
			
			rv+=") ENGINE = INNODB";
			
		}
		
		return l;
	}	
	 /* $endif$ */

}
