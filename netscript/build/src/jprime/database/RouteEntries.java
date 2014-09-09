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
import jprime.routing.RouteEntry;

$else$ */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jprime.Metadata;
import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;
import jprime.routing.RouteEntry;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class RouteEntries extends Table {
	/* $if USE_FLAT_FILE_DB $

	public static class RouteEntryRec extends RecordValue implements Serializable { 
		private static final long serialVersionUID = -4899937609360232818L;
		long parent_dbid;
		long srcMin;
		long srcMax;
		long dstMin;
		long dstMax;
		long outboundIface;
		long owningHost;
		int numOfBits;
		long nextHopId;
		boolean edgeIface;
		long busIdx;
		int numOfBitsBus;
		int cost;
		public RouteEntryRec(long parent_dbid, long srcMin, long srcMax,
				long dstMin, long dstMax, long outboundIface, long owningHost,
				int numOfBits, long nextHopId, boolean edgeIface, long busIdx,
				int numOfBitsBus, int cost) {
			super();
			this.parent_dbid = parent_dbid;
			this.srcMin = srcMin;
			this.srcMax = srcMax;
			this.dstMin = dstMin;
			this.dstMax = dstMax;
			this.outboundIface = outboundIface;
			this.owningHost = owningHost;
			this.numOfBits = numOfBits;
			this.nextHopId = nextHopId;
			this.edgeIface = edgeIface;
			this.busIdx = busIdx;
			this.numOfBitsBus = numOfBitsBus;
			this.cost = cost;
		}
		public RouteEntryRec() { }
		public RecType getType() { return RecType.RouteEntryRecEnum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			out.writeLong(parent_dbid);
			out.writeLong(srcMin);
			out.writeLong(srcMax);
			out.writeLong(dstMin);
			out.writeLong(dstMax);
			out.writeLong(outboundIface);
			out.writeLong(owningHost);
			out.writeInt(numOfBits);
			out.writeLong(nextHopId);
			out.writeBoolean(edgeIface);
			out.writeLong(busIdx);
			out.writeInt(numOfBitsBus);
			out.writeInt(cost);
		}  
		public void initObject(DataInputStream in) throws Exception {  
			parent_dbid=in.readLong();
			srcMin=in.readLong();
			srcMax=in.readLong();
			dstMin=in.readLong();
			dstMax=in.readLong();
			outboundIface=in.readLong();
			owningHost=in.readLong();
			numOfBits=in.readInt();
			nextHopId=in.readLong();
			edgeIface=in.readBoolean();
			busIdx=in.readLong();
			numOfBitsBus=in.readInt();
			cost=in.readInt();
		}
		public int packingsize() {
			return (Long.SIZE*9+Integer.SIZE*3+Byte.SIZE)/8;
		}
	}

	protected class RouteEntryBrowser extends RecordBrowser {
		protected final long max;
		protected boolean done;
		public RouteEntryBrowser(RecordKey key, long max) {
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
	
	$endif$ */
	
	/* $if USE_FLAT_FILE_DB $
	private Select select_many=null;
	$else$ */
	private PreparedStatement select_many=null;
	/* $endif$ */
	
	/**
	 * @param tableName
	 */
	public RouteEntries(Database db) {
		super("route_entries",db);
		Field dbid = new Field(this,"dbid", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getDBID());
			}
		};
		Field metadata_id = new Field(this,"metadata_id", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getMetadata_id());
			}
		};
		Field outbound_iface = new Field(this,"outbound_iface", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getOutboundIface());
			}
		};
		Field src_max = new Field(this,"src_max", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getSrcMax());
			}
		};
		Field edge_iface = new Field(this,"edge_iface", ColumnType.SMALLINT, "0", null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((RouteEntry)obj).getEdgeIface());
				$else$ */
				return new BoundValue(((RouteEntry)obj).getEdgeIface()?1:0);
				/* $endif$ */
			}
		};
		Field dst_min = new Field(this,"dst_min", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getDstMin());
			}
		};
		Field cost = new Field(this,"cost", ColumnType.INTEGER, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getCost());
			}
		};
		Field dst_max = new Field(this,"dst_max", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getDstMax());
			}
		};
		Field busIdx = new Field(this,"busIdx", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getBusIdx());
			}
		};
		Field num_of_bits = new Field(this,"num_of_bits", ColumnType.INTEGER, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getNumOfBits());
			}
		};
		Field src_min = new Field(this,"src_min", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getSrcMin());
			}
		};
		Field num_of_bits_bus = new Field(this,"num_of_bits_bus", ColumnType.INTEGER, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getNumOfBitsBus());
			}
		};
		Field owning_host = new Field(this,"owning_host", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getOwningHost());
			}
		};
		Field next_hop_id = new Field(this,"next_hop_id", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getNextHopId());
			}
		};
		Field parent_id = new Field(this,"parent_id", ColumnType.BIGINT, null, null,false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((RouteEntry)obj).getParent_id());
			}
		};
		this.fields.add(dbid); //0
		this.fields.add(metadata_id);//1
		this.fields.add(owning_host);//2
		this.fields.add(parent_id);//3
		this.fields.add(outbound_iface);//4
		this.fields.add(next_hop_id);//5
		this.fields.add(src_min);//6
		this.fields.add(src_max);//7
		this.fields.add(dst_min);//8
		this.fields.add(dst_max);//9
		this.fields.add(edge_iface);//10
		this.fields.add(cost);//11
		this.fields.add(busIdx);//12
		this.fields.add(num_of_bits);//13
		this.fields.add(num_of_bits_bus);//14
		
		this.primaryKeys.add(dbid);
		this.primaryKeys.add(metadata_id);
	}
	
	/**
	INSERT INTO route_entries(dbid,metadata_id,owning_host,parent_id,outbound_iface,next_hop_id,src_min,src_max,dst_min,dst_max,edge_iface,cost,busIdx,num_of_bits,num_of_bits_bus)
	VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
	
	UPDATE route_entries SET dbid = ? ,metadata_id = ? ,owning_host = ? ,parent_id = ? ,outbound_iface = ? ,next_hop_id = ? ,src_min = ? ,src_max = ? ,dst_min = ? ,dst_max = ? ,edge_iface = ? ,cost = ? ,busIdx = ? ,num_of_bits = ? ,num_of_bits_bus = ?
	WHERE dbid = ?  AND metadata_id = ?
	
	SELECT  dbid, metadata_id, owning_host, parent_id, outbound_iface, next_hop_id, src_min, src_max, dst_min, dst_max, edge_iface, cost, busIdx, num_of_bits, num_of_bits_bus FROM route_entries
	WHERE metadata_id = ?  AND dbid BETWEEN ? AND ?  ORDER BY dbid ASC

	 */
	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup() {
		setup_common();
		this.insert=new Insert() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				final RouteEntryRec value = new RouteEntryRec(
						(Long)vals.get(3).value, //parent_dbid,
						(Long)vals.get(6).value, //srcMin,
						(Long)vals.get(7).value, //srcMax,
						(Long)vals.get(8).value, //dstMin,
						(Long)vals.get(9).value, //dstMax,
						(Long)vals.get(4).value, //outboundIface,
						(Long)vals.get(2).value, //owningHost,
						(Integer)vals.get(13).value, //numOfBits,
						(Long)vals.get(5).value, //nextHopId,
						(Boolean)vals.get(10).value, //edgeIface,
						(Long)vals.get(12).value, //busIdx,
						(Integer)vals.get(14).value, //numOfBitsBus,
						(Integer)vals.get(11).value //cost
				);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				final RouteEntryRec value = new RouteEntryRec(
						(Long)vals.get(3).value, //parent_dbid,
						(Long)vals.get(6).value, //srcMin,
						(Long)vals.get(7).value, //srcMax,
						(Long)vals.get(8).value, //dstMin,
						(Long)vals.get(9).value, //dstMax,
						(Long)vals.get(4).value, //outboundIface,
						(Long)vals.get(2).value, //owningHost,
						(Integer)vals.get(13).value, //numOfBits,
						(Long)vals.get(5).value, //nextHopId,
						(Boolean)vals.get(10).value, //edgeIface,
						(Long)vals.get(12).value, //busIdx,
						(Integer)vals.get(14).value, //numOfBitsBus,
						(Integer)vals.get(11).value //cost
				);
				insert(key,value);
				return key.dbid;
			}
		};
		this.select_many=new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser=new RouteEntryBrowser(new RecordKey((Long)vals.get(0).value, 0),(Long)vals.get(1).value);
				return 0;
			}
		};
	}

	$else$ */
	@Override
	protected void setup() {
		super.setup();
		String add=null,vals=null,where=null;
		for(Field f:fields) {
			if(add==null) {
				add="SELECT  ";
				vals=" FROM "+tableName+" ";
			}
			else {
				add+=", ";
			}
			add+=f.name;
		}
		where=" WHERE "+primaryKeys.get(1).name+" = ? "
		+" AND "+primaryKeys.get(0).name+" BETWEEN ? AND ? "
		+" ORDER BY "+primaryKeys.get(0).name+" ASC";
		add+=vals+where;

		jprime.Console.out.println("SELECT-MANY SQL for "+tableName+" = "+add);
		try {
			select_many=db.getLoadConnection().prepareStatement(add);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+add);
			throw new RuntimeException(e);
		}
	}
	/* $endif$ */
	
	protected void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts) {
		if(owner instanceof Metadata) {
			deleteByMetadata(owner.getDBID(),stmts);
		}
		else {
			throw new RuntimeException("static routing protocols are only owned by mmeta datas! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}
	
	public List<RouteEntry> loadMany(Metadata meta, long lowid, long highid) {
		//jprime.Console.err.println("Loading node(s) meta="+meta.getDBID()+", low="+lowid+", high="+highid);
		ArrayList<RouteEntry> rv = new ArrayList<RouteEntry>((int)(highid-lowid));
		
		RouteEntry t = (RouteEntry)meta.findLoadedObj(new PKey(meta.getDBID(), lowid));
		if(t != null) {
			//if we already have the low id lets just return it!
			rv.add(t);
			return rv;
		}


		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
		
		vals.add(this.primaryKeys.get(1).getPkeyVal(meta.getDBID()));
		vals.add(this.primaryKeys.get(1).getPkeyVal(lowid));
		vals.add(this.primaryKeys.get(1).getPkeyVal(highid));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select_many);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		RouteEntry low=null;
		while (rs.next()) {
			RouteEntry temp = (RouteEntry)meta.findLoadedObj(new PKey(meta.getDBID(), rs.value.key.dbid));
			if(null==temp) {
				final RouteEntryRec rec = (RouteEntryRec)rs.value.value;
				temp = new RouteEntry(meta,
						rs.value.key.dbid,
						rec.parent_dbid,
						rec.srcMin,
						rec.srcMax,
						rec.dstMin,
						rec.dstMax,
						rec.outboundIface,
						rec.owningHost,
						rec.numOfBits,
						rec.nextHopId,
						rec.edgeIface,
						rec.busIdx,
						rec.numOfBitsBus,
						rec.cost);
				if(temp.getDBID()==lowid)
					low=temp;
				rv.add(temp);
			}
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		RouteEntry low=null;
		try {
			while (rs.next()) {
				RouteEntry temp = (RouteEntry)meta.findLoadedObj(new PKey(rs.getLong(2), rs.getLong(1)));
				if(null==temp) {
					temp=new RouteEntry(meta,
							rs.getLong(1),
							rs.getLong(4),
							rs.getLong(7),
							rs.getLong(8),
							rs.getLong(9),
							rs.getLong(10),
							rs.getLong(5),
							rs.getLong(3),
							rs.getInt(14),
							rs.getLong(6),
							rs.getShort(11)==0?false:true,
							rs.getLong(13),
							rs.getInt(15),
							rs.getInt(12));
					if(temp.getDBID()==lowid)
						low=temp;
					rv.add(temp);
				}
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading route entries, meta="+meta.getDBID()+", low="+lowid+", high="+highid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
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
			try {
				throw new RuntimeException("What happened? rv[0]="+rv.get(0).getDBID()+" lowid="+lowid);
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
	
	public RouteEntry load(Metadata meta, long nodeid) {
		RouteEntry rv = (RouteEntry)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid));
		if(rv != null)
			return rv;
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
		
		vals.add(this.primaryKeys.get(1).getPkeyVal(nodeid));
		vals.add(this.primaryKeys.get(1).getPkeyVal(meta.getDBID()));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			final RouteEntryRec rec = (RouteEntryRec)rs.value.value;
			rv = new RouteEntry(meta,
					rs.value.key.dbid,
					rec.parent_dbid,
					rec.srcMin,
					rec.srcMax,
					rec.dstMin,
					rec.dstMax,
					rec.outboundIface,
					rec.owningHost,
					rec.numOfBits,
					rec.nextHopId,
					rec.edgeIface,
					rec.busIdx,
					rec.numOfBitsBus,
					rec.cost);
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
					rv=new RouteEntry(meta,
							rs.getLong(1),
							rs.getLong(4),
							rs.getLong(7),
							rs.getLong(8),
							rs.getLong(9),
							rs.getLong(10),
							rs.getLong(5),
							rs.getLong(3),
							rs.getInt(14),
							rs.getLong(6),
							rs.getShort(11)==0?false:true,
							rs.getLong(13),
							rs.getInt(15),
							rs.getInt(12));
					break;
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading route entries, meta="+meta.getDBID()+", nodeid="+nodeid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
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
