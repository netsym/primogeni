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
import java.util.concurrent.Semaphore;

import jprime.PersistableObject;
import jprime.database.Field.BoundValue;

$else$ */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import jprime.PersistableObject;
import jprime.database.Field.BoundValue;
import jprime.database.Field.ConstraintType;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */

/* $if USE_FLAT_FILE_DB $

public abstract class Table extends SimpleDBTable {
	public static NOPStmt STOPWORK = null;
	protected final List<Field> primaryKeys=new ArrayList<Field>();
	protected Field metadataid=null;
	protected boolean haveGeneratedValues=false;

$else$ */

public abstract class Table {
	
	public static NOPStmt STOPWORK = null;
	protected final String tableName;
	protected final List<Field> fields=new ArrayList<Field>();
	protected final List<Field> primaryKeys=new ArrayList<Field>();
	protected final Database db;
	protected boolean haveGeneratedValues=false;
	protected PreparedStatement update;
	protected PreparedStatement insert;
	protected PreparedStatement delete;
	protected PreparedStatement deleteByMetadata;
	protected PreparedStatement select;
	protected Field metadataid=null;

 /* $endif$ */


	/* $if USE_FLAT_FILE_DB $
	public class SQLStmt {
		public final ArrayList<BoundValue> vals;
		public final Stmt stmt;

		public SQLStmt(ArrayList<BoundValue> vals, Stmt stmt) {
			super();
			this.vals=vals;
			this.stmt = stmt;
		}

		public WaitingSqlStmt asWaiting() {
			return new WaitingSqlStmt(this);
		}
		
		public long exec() {
			return stmt.exec(vals);
		}
		
		public Table getTable() {
			return Table.this;
		}
		
		public boolean effects(SQLStmt s) {
			return !stmt.reading && s.getTable() == Table.this;
		}
		void realeaseLock() {
			stmt.releaseLock();
		}
	}
	
	public class NOPStmt extends SQLStmt {
		public NOPStmt() {
			super(null,new Select() {
				@Override
				public long _exec(List<BoundValue> vals) {
					filedb.releaseLock();
					return 0;
				}
			});
		}
		public WaitingSqlStmt asWaiting() {
			throw new RuntimeException("dont call this!");
		}
		public boolean effects(SQLStmt s) {
			return false;
		}
		void realeaseLock() {
		}
	}
	
	public class WaitingSqlStmt extends SQLStmt {
		private final Semaphore lock;
		public Long rv = null;
		public WaitingSqlStmt(SQLStmt s) {
			super(s.vals,s.stmt);
			this.lock = new Semaphore(1);
			waitForExec();
		}
		public final long exec() {
			try {
				this.rv = super.exec();
				return rv;
			} finally {
				lock.release();
			}
		}
		public final void waitForExec() {
			lock.acquireUninterruptibly();
		}
		public final WaitingSqlStmt asWaiting() {
			return this;
		}
	}

	
	public class LoadSQLStmt extends SQLStmt {
		private final Semaphore lock;
		public LoadSQLStmt(ArrayList<BoundValue> vals, Select stmt) {
			super(vals,stmt);
			this.lock = new Semaphore(1);
			waitForExec();
		}
		public final long exec() {
			try {
				return stmt.exec(vals);
			} finally {
				lock.release();
			}
		}
		public final void waitForExec() {
			lock.acquireUninterruptibly();
		}
		public RecordBrowser getBrowser() {
			return ((Select)stmt).browser;
		}
	}
	
	$else$ */
	public class SQLStmt {
		public final ArrayList<BoundValue> vals;

		public final PreparedStatement stmt;
		public final boolean reading;

		public SQLStmt(ArrayList<BoundValue> vals, PreparedStatement stmt) {
			super();
			this.vals=vals;
			this.stmt = stmt;
			this.reading = false;
		}
		
		public SQLStmt(ArrayList<BoundValue> vals, PreparedStatement stmt, boolean reading) {
			super();
			this.vals=vals;
			this.stmt = stmt;
			this.reading = reading;
		}
		
		public WaitingSqlStmt asWaiting() {
			return new WaitingSqlStmt(this);
		}
		
		public ResultSet load() throws SQLException {
			for(int i=0;i<vals.size();i++) {
				vals.get(i).setValue(i+1, stmt);
			}
			return stmt.executeQuery();
		}
		
		public long exec() throws SQLException {
			long rv = 0;
			if(haveGeneratedValues && insert == stmt) {
				for(int i=0;i<vals.size();i++) {
					vals.get(i).setValue(i+1, stmt);
				}
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				while (rs.next()) {
					rv = rs.getLong(1);
				}
			}
			else {
				for(int i=0;i<vals.size();i++) {
					vals.get(i).setValue(i+1, stmt);
				}
				stmt.executeUpdate();
			}
			return rv;
		}
		
		public Table getTable() {
			return Table.this;
		}
		
		public boolean effects(SQLStmt s) {
			return !reading && s.getTable() == getTable();
		}
	}
	
	public class NOPStmt extends SQLStmt {
		public NOPStmt() {
			super(null,null);
		}
		public WaitingSqlStmt asWaiting() {
			throw new RuntimeException("dont call this!");
		}
		public ResultSet load() throws SQLException {
			throw new RuntimeException("dont call this!");
		}
		public long exec() throws SQLException {
			return -1;
		}
		public boolean effects(SQLStmt s) {
			return false;
		}
	}
	
	public class WaitingSqlStmt extends SQLStmt {
		private final Semaphore lock;
		public Long rv = null;
		public WaitingSqlStmt(SQLStmt s) {
			super(s.vals,s.stmt);
			this.lock = new Semaphore(1);
			waitForExec();
		}
		public final long exec() {
			try {
				this.rv = super.exec();
				return rv;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				lock.release();
			}
		}
		public final void waitForExec() {
			lock.acquireUninterruptibly();
		}
		public final WaitingSqlStmt asWaiting() {
			return this;
		}
	}
	
	public class LoadSQLStmt extends SQLStmt {
		private final Semaphore lock;
		public ResultSet rs=null;
		public LoadSQLStmt(ArrayList<BoundValue> vals, PreparedStatement stmt) {
			super(vals,stmt,true);
			this.lock = new Semaphore(1);
			waitForExec();
		}
		public final long exec() {
			try {
				//stmt.setMaxRows(GlobalProperties.FETCH_SIZE);
				for(int i=0;i<vals.size();i++) {
					vals.get(i).setValue(i+1, stmt);
				}
				rs= stmt.executeQuery();
				return 0;
			} catch (SQLException e) {
				rs=null;
				throw new RuntimeException(e);
			} finally {
				lock.release();
			}
		}
		public final void waitForExec() {
			lock.acquireUninterruptibly();
		}
	}
 	/* $endif$ */


	/* $if USE_FLAT_FILE_DB $
	protected Table(String tableName, Database db) {
		super(tableName, db);
		synchronized(Table.class) {
			if(STOPWORK == null) {
				STOPWORK = new NOPStmt();
			}
		}
	}
	$else$ */
	protected Table(String tableName, Database db) {
		this.tableName=tableName;
		this.db=db;
		this.update=null;
		this.insert=null;
		this.delete=null;
		synchronized(Table.class) {
			if(STOPWORK == null) {
				STOPWORK = new NOPStmt();
			}
		}
	}
	 /* $endif$ */

	/* $if USE_FLAT_FILE_DB $
	abstract protected void setup();
	protected void setup_common() {
		super.setup_common();
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
	}
	$else$ */
	protected void setup() {
		jprime.Console.err.flush();
		jprime.Console.err.flush();
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


		String add=null, vals=null;
		for(Field f:fields) {
			if(add==null) {
				add="INSERT INTO "+tableName+"(";
				vals="VALUES(";
			}
			else {
				add+=",";
				vals+=",";
			}
			if(f.isGenerated) {
				add+=f.name;
				vals+="DEFAULT";
			}
			else {
				add+=f.name;
				vals+="?";
			}
		}
		add+=") "+vals+")";

		jprime.Console.out.println("INSERT SQL for "+tableName+" = "+add);
		try {
			//insert=db.getFlushConnection().prepareStatement(add);
			insert=db.getFlushConnection().prepareStatement(add,Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+add);
			throw new RuntimeException(e);
		}

		String up=null,where=null;
		for(Field f:fields) {
			if(f.isGenerated)continue;
			if(up==null)
				up="UPDATE "+tableName+" SET ";
			else
				up+=",";
			up+=f.name+" = ? ";
		}
		for(Field f:primaryKeys) {
			if(where==null)
				where=" WHERE ";
			else
				where+=" AND ";
			where+=f.name+" = ? ";
		}
		up+=where;

		jprime.Console.out.println("UPDATE SQL for "+tableName+" = "+up);
		try {
			update=db.getFlushConnection().prepareStatement(up);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+up);
			throw new RuntimeException(e);
		}


		where=null;
		for(Field f:primaryKeys) {
			if(where==null)
				where="DELETE FROM "+tableName+" WHERE ";
			else
				where+=" AND ";
			where+=f.name+" = ? ";
		}

		jprime.Console.out.println("DELETE SQL for "+tableName+" = "+where);
		try {
			delete=db.getFlushConnection().prepareStatement(where);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+where);
			throw new RuntimeException(e);
		}

		where=null;
		if(metadataid != null) {
			where="DELETE FROM "+tableName+" WHERE "+metadataid.name+" = ?";
		}

		if(where !=null) {
			jprime.Console.out.println("DELETE-BY-METADATA SQL for "+tableName+" = "+where);
			try {
				deleteByMetadata=db.getFlushConnection().prepareStatement(where);
			} catch (SQLException e) {
				jprime.Console.out.println("Error compiling "+where);
				throw new RuntimeException(e);
			}
		}
		else {
			throw new RuntimeException("Unable to find the metadata id field!");
		}
		
		
		add=vals=where=null;
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
		where=null;
		for(Field f:primaryKeys) {
			if(where==null)
				where=" WHERE ";
			else
				where+=" AND ";
			where+=f.name+" = ? ";
		}
		add+=vals+where;

		jprime.Console.out.println("SELECT SQL for "+tableName+" = "+add);
		try {
			select=db.getLoadConnection().prepareStatement(add);
			//select=db.getLoadConnection().prepareStatement(add);
		} catch (SQLException e) {
			jprime.Console.out.println("Error compiling "+add);
			throw new RuntimeException(e);
		}

	}
	/* $endif$ */

	protected abstract void deleteByOwner(PersistableObject owner, List<SQLStmt> stmts);
	
	protected void delete(PersistableObject obj, List<SQLStmt> stmts) {
		ArrayList<BoundValue> values=new ArrayList<Field.BoundValue>(2);
		for(Field f:primaryKeys) {
			values.add(f.getValue(obj));
		}
		stmts.add(new SQLStmt(values,delete));
	}
	
	protected void deleteByMetadata(long mid, List<SQLStmt> stmts) {
		ArrayList<BoundValue> values=new ArrayList<Field.BoundValue>(1);
		values.add(metadataid.getPkeyVal(mid));
		stmts.add(new SQLStmt(values,deleteByMetadata));
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
		}
		else {
			ArrayList<BoundValue> values=new ArrayList<Field.BoundValue>();
			for(Field f:fields) {
				if(f.isGenerated)continue;
				values.add(f.getValue(obj));
			}
			switch(obj.getPersistableState()) {
			case MODIFIED:
			{
				for(Field f : primaryKeys) {
					values.add(f.getValue(obj));
				}
				rv.add(new SQLStmt(values,update));
			}
			break;
			case NEW:
			{
				rv.add(new SQLStmt(values,insert));
			}
			break;
			case UNMODIFIED:
			case ORPHAN:
				//no op
				break;
			case DEAD:
				delete(obj,rv);
			}
		}
		return rv;
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
