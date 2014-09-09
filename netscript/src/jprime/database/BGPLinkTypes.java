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
import java.util.TreeSet;

import jprime.Metadata;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.routing.BGPLinkType;
import jprime.routing.StaticRoutingProtocol;

$else$ */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jprime.Metadata;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.routing.BGPLinkType;
import jprime.routing.StaticRoutingProtocol;

/* $endif$ */


/**
 * @author Nathanael Van Vorst
 *
 */
public class BGPLinkTypes extends Table {
	/* $if USE_FLAT_FILE_DB $

	public static class BgpLinkTypeRec extends RecordValue implements Serializable { 
		private static final long serialVersionUID = 8396323513096615241L;
		long parent_dbid;
		int link_type;
		boolean first_child_is_src;
		String alias_path;
		public BgpLinkTypeRec(long parent_dbid, int link_type, boolean first_child_is_src,
				String alias_path) {
			super();
			this.parent_dbid = parent_dbid;
			this.link_type = link_type;
			this.first_child_is_src = first_child_is_src;
			this.alias_path = alias_path;
		}
		public BgpLinkTypeRec() {}
		public RecType getType() { return RecType.BGPLinkRecEnum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			out.writeLong(parent_dbid);
			out.writeInt(link_type);
			out.writeBoolean(first_child_is_src);
			out.writeInt(alias_path.length());
			out.writeBytes(alias_path);
		}  
		public void initObject(DataInputStream in) throws Exception {  
			parent_dbid=in.readLong();
			link_type=in.readInt();
			first_child_is_src=in.readBoolean();
			byte[] b = new byte[in.readInt()];
			in.read(b);
			alias_path=new String(b);
		}
		public int packingsize() {
			return (Integer.SIZE+Integer.SIZE+Long.SIZE+Byte.SIZE)/8+alias_path.length();
		}
	}

	protected class BLTDBIDBrowser extends RecordBrowser {
		protected final long max;
		protected boolean done;
		public BLTDBIDBrowser(RecordKey key, long max) {
			super(key);
			this.max=max;
			this.done=false;
		}
		@Override
		public boolean next() {
			if(!done && super.next()) {
				if(value.key.dbid>max) {
					done=true;
				}
				else {
					return true;
				}
			}
			return false;
		}
		
	}
	
	$else$ */

	private static class BLT {
		final long l1,l2;
		final int i1;
		final boolean b1;
		final String s1;
		public BLT(long l1, long l2, int i1, boolean b1, String s1) {
			super();
			this.l1 = l1;
			this.l2 = l2;
			this.i1 = i1;
			this.b1 = b1;
			this.s1 = s1;
		}
	}
	
	/* $endif$ */

	
	/* $if USE_FLAT_FILE_DB $
	private Select select_many=null;
	private Delete deleteByOwner=null;
	$else$ */
	private PreparedStatement select_many=null,deleteByOwner=null;
	/* $endif$ */
	
	public BGPLinkTypes(Database db) {
		super("bgp_link_types",db);

		Field dbid = new Field(this,"dbid", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((BGPLinkType)obj).getDBID());
			}
		};
		Field metadata = new Field(this,"metadata", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((BGPLinkType)obj).getMetadataId());
			}
		};
		Field parent = new Field(this,"parent", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL, ConstraintType.INDEX},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((BGPLinkType)obj).getParentId());
			}
		};
		Field link_type = new Field(this, "link_type", ColumnType.INTEGER, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((BGPLinkType)obj).getLinkTypeId());
			}
		};
		Field first_child_is_src = new Field(this,"first_child_is_src", ColumnType.SMALLINT, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((BGPLinkType)obj).firstChildIsSrc());
				$else$ */
				return new BoundValue(((BGPLinkType)obj).firstChildIsSrc()?1:0);
				/* $endif$ */
			}
		};
		Field alias_path = new Field(this, "alias_path", ColumnType.CLOB, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((BGPLinkType)obj).getAliasPath());
			}
		};
		this.fields.add(dbid);
		this.fields.add(metadata);
		this.fields.add(parent);
		this.fields.add(link_type);
		this.fields.add(first_child_is_src);
		this.fields.add(alias_path);
		this.primaryKeys.add(dbid);	
		this.primaryKeys.add(metadata);	
	}

	/**
	INSERT INTO bgp_link_types(dbid,metadata,parent,link_type,first_child_is_src,alias_path)
	VALUES(?,?,?,?,?,?)
	
	UPDATE bgp_link_types SET dbid = ? ,metadata = ? ,parent = ? ,link_type = ? ,first_child_is_src = ? ,alias_path = ?
	WHERE dbid = ? AND metadata = ?
	
	SELECT  dbid, metadata, parent, link_type, first_child_is_src, alias_path FROM bgp_link_types
	WHERE metadata = ? AND dbid BETWEEN ? and ?
	ORDER BY dbid ASC
	
	DELETE FROM bgp_link_types
	WHERE metadata = ? AND parent = ?
	 */
	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup() {
		setup_common();
		this.insert=new Insert() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				final BgpLinkTypeRec value = new BgpLinkTypeRec(
						(Long)vals.get(2).value,
						(Integer)vals.get(3).value,
						(Boolean)vals.get(4).value,
						(String)vals.get(5).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				final BgpLinkTypeRec value = new BgpLinkTypeRec(
						(Long)vals.get(2).value,
						(Integer)vals.get(3).value,
						(Boolean)vals.get(4).value,
						(String)vals.get(5).value);
				
				insert(key,value);
				return key.dbid;
			}
		};
		this.select_many=new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser=new BLTDBIDBrowser(new RecordKey((Long)vals.get(0).value, (Long)vals.get(1).value),(Long)vals.get(2).value);
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
					if(((BgpLinkTypeRec)t.getValue()).parent_dbid==parent_dbid)
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
	@Override
	protected void setup() {
		super.setup();
		String add=null;
		
		add=null;
		for(Field f:fields) {
			if(add==null) {
				add="SELECT  ";
			}
			else {
				add+=", ";
			}
			add+=f.name;
		}
		add+=" FROM "+tableName+" WHERE "+fields.get(1).name+" = ? AND "+fields.get(0).name+" BETWEEN ? and ?"
		+" ORDER BY "+fields.get(0).name+" ASC";

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
	/* $endif$ */
	
	protected void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts) {
		if(owner instanceof Metadata) {
			deleteByMetadata(owner.getDBID(),stmts);
		}
		else if(owner instanceof StaticRoutingProtocol) {
			ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
			vals.add(metadataid.getPkeyVal(((StaticRoutingProtocol)owner).getMetadata().getDBID()));
			vals.add(metadataid.getPkeyVal(owner.getDBID()));
			stmts.add(new SQLStmt(vals, deleteByOwner));
		}
		else {
			throw new RuntimeException("bgp link types only owned by static routing protocols and meta datas! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}	
	
	public List<BGPLinkType> loadMany(Metadata meta, long lowid, long highid) {
		//jprime.Console.err.println("Loading node(s) meta="+meta.getDBID()+", low="+lowid+", high="+highid);
		ArrayList<BGPLinkType> rv = new ArrayList<BGPLinkType>((int)(highid-lowid));

		BGPLinkType t = (BGPLinkType)meta.findLoadedObj(new PKey(meta.getDBID(), lowid));
		if(t != null) {
			//if we already have the low id lets just return it!
			rv.add(t);
			return rv;
		}
		
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();

		vals.add(this.fields.get(1).getPkeyVal(meta.getDBID()));
		vals.add(this.fields.get(2).getPkeyVal(lowid));
		vals.add(this.fields.get(2).getPkeyVal(highid));

		BGPLinkType low=null;
		final LoadSQLStmt ls = new LoadSQLStmt(vals, select_many);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			t = (BGPLinkType)meta.findLoadedObj(new PKey(meta.getDBID(), rs.value.key.dbid));
			if(t==null) {
				final BgpLinkTypeRec v = (BgpLinkTypeRec)rs.value.value;
				t=new BGPLinkType(meta,rs.value.key.dbid,v.parent_dbid,v.link_type,v.first_child_is_src,v.alias_path);
			}
			if(t.getDBID() == lowid)
				low=t;
			rv.add(t);
		}
		ls.realeaseLock();
		$else$ */
		LinkedList<BLT> temp = new LinkedList<BLT>();
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				t = (BGPLinkType)meta.findLoadedObj(new PKey(meta.getDBID(), rs.getLong(1)));
				if(t==null) {
					temp.add(new BLT(rs.getLong(1), rs.getLong(3), rs.getInt(4), rs.getShort(5)==0?false:true, rs.getString(6)));
				}
				else {
					rv.add(t);
				}
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading bgp link types, meta="+meta.getDBID()+", low="+lowid+", high="+highid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		
		for(BLT b : temp) {
			t = new BGPLinkType(meta, b.l1,b.l2,b.i1,b.b1,b.s1);
			rv.add(t);
			if(t.getDBID() == lowid)
				low=t;
		}
		temp.clear();
		/* $endif$ */

		if(low == null) {
			//jprime.Console.err.println("Loading the lowid since it was null, it is probably new and not flushed out yet....");
			low=load(meta, lowid);
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
			rv.remove(low);
			rv.add(0,low);
		}
		return rv;
	}
	
	public BGPLinkType load(Metadata meta, long nodeid) {
		BGPLinkType rv = (BGPLinkType)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid));
		if(rv != null) {
			//if we already have the low id lets just return it!
			return rv;
		}
		
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
		vals.add(this.primaryKeys.get(0).getPkeyVal(nodeid));
		vals.add(this.primaryKeys.get(1).getPkeyVal(meta.getDBID()));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			final BgpLinkTypeRec v = (BgpLinkTypeRec)rs.value.value;
			rv=new BGPLinkType(meta,rs.value.key.dbid,v.parent_dbid,v.link_type,v.first_child_is_src,v.alias_path);
			break;
		}
		ls.realeaseLock();
		$else$ */
		LinkedList<BLT> temp = new LinkedList<BLT>();
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				temp.add(new BLT(rs.getLong(1), rs.getLong(3), rs.getInt(4), rs.getShort(5)==0?false:true, rs.getString(6)));
				break;
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading bgp link type, meta="+meta.getDBID()+", nodeid="+nodeid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		for(BLT b : temp)
			if(rv != null) rv = new BGPLinkType(meta, b.l1,b.l2,b.i1,b.b1,b.s1);
			else new BGPLinkType(meta, b.l1,b.l2,b.i1,b.b1,b.s1);
		temp.clear();
		/* $endif$ */
		
		if(rv==null) {
			try {
				throw new RuntimeException("What happened?");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
}
