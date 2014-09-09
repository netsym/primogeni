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

import jprime.EmulationCommand;
import jprime.Experiment;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.Host.IHost;
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

import jprime.EmulationCommand;
import jprime.Experiment;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.Host.IHost;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ColumnType;
import jprime.database.Field.ConstraintType;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class EmulationCommands extends Table {
	/* $if USE_FLAT_FILE_DB $

	public static class EmuCmdRec extends RecordValue implements Serializable { 
		private static final long serialVersionUID = -5900348658155563128L;
		long parent_dbid;
		String raw_cmd;
		String output_suffix;
		long delay;
		long raw_max_runtime;
		boolean block;
		boolean checkReturnCode;
		public EmuCmdRec(long parent_dbid, String raw_cmd, String output_suffix,
				long delay, long raw_max_runtime, boolean block,
				boolean checkReturnCode) {
			super();
			this.parent_dbid = parent_dbid;
			this.raw_cmd = raw_cmd;
			this.output_suffix = output_suffix;
			this.delay = delay;
			this.raw_max_runtime = raw_max_runtime;
			this.block = block;
			this.checkReturnCode = checkReturnCode;
		}
		public EmuCmdRec() { }
		public RecType getType() { return RecType.EmuCmdRecEmum;}
		public void flushObject(DataOutputStream out) throws Exception { 
			out.writeLong(parent_dbid);
			out.writeLong(delay);
			out.writeLong(raw_max_runtime);
			out.writeBoolean(block);
			out.writeBoolean(checkReturnCode);
			out.writeInt(raw_cmd.length());
			out.writeBytes(raw_cmd);
			out.writeInt(output_suffix.length());
			out.writeBytes(output_suffix);
		}  
		public void initObject(DataInputStream in) throws Exception {  
			parent_dbid=in.readLong();
			delay=in.readLong();
			raw_max_runtime=in.readLong();
			block=in.readBoolean();
			checkReturnCode=in.readBoolean();
			byte[] b = new byte[in.readInt()];
			in.read(b);
			raw_cmd=new String(b);
			b = new byte[in.readInt()];
			in.read(b);
			output_suffix=new String(b);
		}
		public int packingsize() {
			return (Long.SIZE+Long.SIZE+Long.SIZE+Byte.SIZE+Byte.SIZE+Integer.SIZE+Integer.SIZE)/8+raw_cmd.length()+output_suffix.length();
		}
	}

	protected class EmuCmdBrowser extends RecordBrowser {
		protected final long pid;
		public EmuCmdBrowser(RecordKey key, long pid) {
			super(key);
			this.pid=pid;
		}
		@Override
		public boolean next() {
			while(super.next()) {
				if(((EmuCmdRec)value.value).parent_dbid==pid) {
					return true;
				}
			}
			return false;
		}		
	}
	
	$endif$ */
	
	/* $if USE_FLAT_FILE_DB $
	private Select select_many=null;
	private Delete deleteByOwner=null;
	$else$ */
	private PreparedStatement select_many=null,deleteByOwner=null;
	/* $endif$ */
	
	public EmulationCommands(Database db) {
		super("emulation_commands",db);
		Field dbid = new Field(this,"dbid", ColumnType.BIGINT, null, new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((EmulationCommand)obj).getDBID());
			}
		};
		Field metadata = new Field(this,"metadata", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},true) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((EmulationCommand)obj).getMetadataId());
			}
		};
		Field parent = new Field(this,"parent", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL, ConstraintType.INDEX},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((EmulationCommand)obj).getParentId());
			}
		};
		Field raw_cmd = new Field(this, "raw_cmd", ColumnType.VARCHAR_2048, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((EmulationCommand)obj).getRawCmd());
			}
		};
		Field outputSuffix = new Field(this, "output_suffix", ColumnType.VARCHAR_2048, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((EmulationCommand)obj).getOutputSuffix());
			}
		};		
		Field delay = new Field(this, "delay", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((EmulationCommand)obj).getDelay());
			}
		};
		Field raw_maxRuntime = new Field(this, "raw_max_runtime", ColumnType.BIGINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				return new BoundValue(((EmulationCommand)obj).getRawMaxRuntime());
			}
		};
		Field block = new Field(this, "block", ColumnType.SMALLINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((EmulationCommand)obj).isBlocking());
				$else$ */
				return new BoundValue(((EmulationCommand)obj).isBlocking()?1:0);
				/* $endif$ */
			}
		};
		Field checkReturnCode = new Field(this, "checkReturnCode", ColumnType.SMALLINT, null,  new ConstraintType[]{ConstraintType.NOT_NULL},false) {
			@Override
			public BoundValue getValue(PersistableObject obj) {
				/* $if USE_FLAT_FILE_DB $
				return new BoundValue(((EmulationCommand)obj).shouldCheckReturnCode());
				$else$ */
				return new BoundValue(((EmulationCommand)obj).shouldCheckReturnCode()?1:0);
				/* $endif$ */
			}
		};
		this.fields.add(dbid);
		this.fields.add(metadata);
		this.fields.add(parent);
		this.fields.add(raw_cmd);
		this.fields.add(outputSuffix);
		this.fields.add(delay);
		this.fields.add(raw_maxRuntime);
		this.fields.add(block);
		this.fields.add(checkReturnCode);
		this.primaryKeys.add(dbid);	
		this.primaryKeys.add(metadata);	
	}
	
	/**
	INSERT INTO emulation_commands(dbid,metadata,parent,raw_cmd,output_suffix,delay,raw_max_runtime,block,checkReturnCode)
	VALUES(?,?,?,?,?,?,?,?,?)

	UPDATE emulation_commands SET dbid = ? ,metadata = ? ,parent = ? ,raw_cmd = ? ,output_suffix = ? ,delay = ? ,raw_max_runtime = ? ,block = ? ,checkReturnCode = ?
	WHERE dbid = ?  AND metadata = ?

	SELECT  dbid, metadata, parent, raw_cmd, output_suffix, delay, raw_max_runtime, block, checkReturnCode FROM emulation_commands
	WHERE metadata = ? AND parent = ?

	DELETE FROM emulation_commands WHERE metadata = ? AND parent = ?

	 */
	/* $if USE_FLAT_FILE_DB $
	@Override
	protected void setup() {
		setup_common();
		this.insert=new Insert() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				final EmuCmdRec value = new EmuCmdRec(
						(Long)vals.get(2).value,
						(String)vals.get(3).value,
						(String)vals.get(4).value,
						(Long)vals.get(5).value,
						(Long)vals.get(6).value,
						(Boolean)vals.get(7).value,
						(Boolean)vals.get(8).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.update=new Update() {
			@Override
			public long _exec(List<BoundValue> vals) {
				final RecordKey key = new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value);
				final EmuCmdRec value = new EmuCmdRec(
						(Long)vals.get(2).value,
						(String)vals.get(3).value,
						(String)vals.get(4).value,
						(Long)vals.get(5).value,
						(Long)vals.get(6).value,
						(Boolean)vals.get(7).value,
						(Boolean)vals.get(8).value);
				insert(key,value);
				return key.dbid;
			}
		};
		this.select_many=new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {
				this.browser=new EmuCmdBrowser(new RecordKey((Long)vals.get(0).value, 0),(Long)vals.get(1).value);
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
					if(((EmuCmdRec)t.getValue()).parent_dbid==parent_dbid)
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
		else if(owner instanceof Experiment) {
			ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
			vals.add(metadataid.getPkeyVal(((Experiment)owner).getMetadata().getDBID()));
			vals.add(metadataid.getPkeyVal(0));
			stmts.add(new SQLStmt(vals, deleteByOwner));
		}
		else {
			throw new RuntimeException("emu comands are are only owned by model nodes, exps, and meta datas! passed in "+owner.getClass().getSimpleName()+"!");
		}
	}
	
	public List<EmulationCommand> loadMany(Metadata meta, IHost parent, long nodeid) {
		//jprime.Console.err.println("Loading node(s) meta="+meta.getDBID()+", low="+lowid+", high="+highid);
		LinkedList<EmulationCommand> rv = new LinkedList<EmulationCommand>();
		
		EmulationCommand t = (EmulationCommand)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid)), n=null;
		if(t != null) {
			//if we already have the low id lets just return it!
			rv.add(t);
			return rv;
		}
		
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();

		vals.add(this.fields.get(1).getPkeyVal(meta.getDBID()));
		vals.add(this.fields.get(2).getPkeyVal(parent.getDBID()));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select_many);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			final EmuCmdRec r = (EmuCmdRec)rs.value.value;
			t = new EmulationCommand(
					meta,
					rs.value.key.dbid,
					parent,
					r.raw_cmd,
					r.output_suffix,
					r.delay,
					r.raw_max_runtime,
					r.block,
					r.checkReturnCode);
			if(t.getDBID() == nodeid)
				n=t;
			rv.add(t);
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				t = (EmulationCommand)meta.findLoadedObj(new PKey(meta.getDBID(), rs.getLong(1)));
				if(t==null) {
					t = new EmulationCommand(meta, rs.getLong(1), parent, rs.getString(4),
							rs.getString(5),rs.getLong(6), rs.getLong(7), rs.getShort(8)==0?false:true, rs.getShort(9)==0?false:true); 
				}
				if(t.getDBID() == nodeid)
					n=t;
				rv.add(t);
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading emulation commands, parentid="+parent.getDBID()+", nodeid="+nodeid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */

		if(n == null) {
			jprime.Console.err.println("Loading n since it was null, it is probably new and not flushed out yet....");
			n=load(meta, parent, nodeid);
			rv.add(0,n);
		}
		if(n==null) {
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
		else if(rv.get(0)!=n) {
			try {
				throw new RuntimeException("What happened? rv[0]="+rv.get(0).getDBID()+" nodeid="+nodeid);
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
	
	public EmulationCommand load(Metadata meta, IHost parent, long nodeid) {
		EmulationCommand rv = (EmulationCommand)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid));
		if(rv != null) {
			//if we already have the low id lets just return it!
			return rv;
		}
		
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
		
		vals.add(this.primaryKeys.get(1).getPkeyVal(nodeid));
		vals.add(this.primaryKeys.get(1).getPkeyVal(meta.getDBID()));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		/* $if USE_FLAT_FILE_DB $
		RecordBrowser rs = db.getDBThread().load(ls);
		while (rs.next()) {
			final EmuCmdRec r = (EmuCmdRec)rs.value.value;
			rv = new EmulationCommand(
					meta,
					rs.value.key.dbid,
					parent,
					r.raw_cmd,
					r.output_suffix,
					r.delay,
					r.raw_max_runtime,
					r.block,
					r.checkReturnCode);
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				rv = new EmulationCommand(meta, rs.getLong(1), parent, rs.getString(4), 
						rs.getString(5),rs.getLong(6), rs.getLong(7), rs.getShort(8)==0?false:true, rs.getShort(9)==0?false:true); 
				break;
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading bgp link type, meta="+meta.getDBID()+", nodeid="+nodeid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		if(rv==null) {
			try {
				throw new RuntimeException("What happened? low==null!");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
	
	public List<EmulationCommand> loadMany(Metadata meta, Experiment parent, long nodeid) {
		//jprime.Console.err.println("Loading node(s) meta="+meta.getDBID()+", low="+lowid+", high="+highid);
		LinkedList<EmulationCommand> rv = new LinkedList<EmulationCommand>();
		
		EmulationCommand t = (EmulationCommand)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid)), n=null;
		if(t != null) {
			//if we already have the low id lets just return it!
			rv.add(t);
			return rv;
		}
		
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();

		vals.add(this.fields.get(1).getPkeyVal(meta.getDBID()));
		vals.add(this.fields.get(2).getPkeyVal(parent.getDBID()));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select_many);
		/* $if USE_FLAT_FILE_DB $
		EmuCmdBrowser rs = (EmuCmdBrowser)db.getDBThread().load(ls);
		while (rs.next()) {
			final EmuCmdRec r = (EmuCmdRec)rs.value.value;
			t = new EmulationCommand(
					meta,
					rs.value.key.dbid,
					parent,
					r.raw_cmd,
					r.output_suffix,
					r.delay,
					r.raw_max_runtime,
					r.block,
					r.checkReturnCode);
			if(t.getDBID() == nodeid)
				n=t;
			rv.add(t);
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				t = (EmulationCommand)meta.findLoadedObj(new PKey(meta.getDBID(), rs.getLong(1)));
				if(t==null){
					t = new EmulationCommand(meta, rs.getLong(1), parent, rs.getString(4),
							rs.getString(5),rs.getLong(6), rs.getLong(7), rs.getShort(8)==0?false:true, rs.getShort(9)==0?false:true); 
				}
				if(t.getDBID() == nodeid)
					n=t;
				rv.add(t);
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading emulation commands, parentid="+parent.getDBID()+", nodeid="+nodeid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		if(n == null) {
			jprime.Console.err.println("Loading n since it was null, it is probably new and not flushed out yet....");
			n=load(meta, parent, nodeid);
			rv.add(0,n);
		}
		if(n==null) {
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
		else if(rv.get(0)!=n) {
			try {
				throw new RuntimeException("What happened? rv[0]="+rv.get(0).getDBID()+" nodeid="+nodeid);
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
	
	public EmulationCommand load(Metadata meta, Experiment parent, long nodeid) {
		EmulationCommand rv = (EmulationCommand)meta.findLoadedObj(new PKey(meta.getDBID(), nodeid));
		if(rv != null) {
			//if we already have the low id lets just return it!
			return rv;
		}
		
		ArrayList<BoundValue> vals = new ArrayList<Field.BoundValue>();
		
		vals.add(this.primaryKeys.get(1).getPkeyVal(nodeid));
		vals.add(this.primaryKeys.get(1).getPkeyVal(meta.getDBID()));

		final LoadSQLStmt ls = new LoadSQLStmt(vals, select);
		/* $if USE_FLAT_FILE_DB $
		EmuCmdBrowser rs = (EmuCmdBrowser)db.getDBThread().load(ls);
		while (rs.next()) {
			final EmuCmdRec r = (EmuCmdRec)rs.value.value;
			rv = new EmulationCommand(
					meta,
					rs.value.key.dbid,
					parent,
					r.raw_cmd,
					r.output_suffix,
					r.delay,
					r.raw_max_runtime,
					r.block,
					r.checkReturnCode);
			break;
		}
		ls.realeaseLock();
		$else$ */
		ResultSet rs = db.getDBThread().load(ls);
		try {
			while (rs.next()) {
				rv = new EmulationCommand(meta, rs.getLong(1), parent, rs.getString(4),
						rs.getString(5),rs.getLong(6), rs.getLong(7), rs.getShort(8)==0?false:true, rs.getShort(9)==0?false:true); 
				break;
			}
			db.getDBThread().closeRS(rs);
		} catch (SQLException e1) {
			jprime.Console.err.println("Error loading bgp link type, meta="+meta.getDBID()+", nodeid="+nodeid);
			jprime.Console.err.printStackTrace(e1);
			jprime.Console.halt(-1);
		}
		/* $endif$ */
		if(rv==null) {
			try {
				throw new RuntimeException("What happened? low==null!");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
		}
		return rv;
	}
}
