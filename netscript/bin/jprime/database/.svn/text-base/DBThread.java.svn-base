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

import jprime.database.SimpleDBTable.RecordBrowser;
import jprime.database.Table.LoadSQLStmt;
import jprime.database.Table.SQLStmt;
import jprime.database.Table.WaitingSqlStmt;
import jprime.util.GlobalProperties;

$else$ */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import jprime.database.Table.LoadSQLStmt;
import jprime.database.Table.SQLStmt;
import jprime.database.Table.WaitingSqlStmt;
import jprime.util.GlobalProperties;

/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class DBThread extends Thread {

	private final WorkQueue work_queue = new WorkQueue();
	/* $if USE_FLAT_FILE_DB $
	private final Database db;
	$else$ */
	private final Semaphore reading = new Semaphore(1);
	/* $endif$ */
	private boolean stop=false;
	/* $if false == USE_FLAT_FILE_DB $ */
	private final Connection flush_conn, load_conn;
	/* $endif$ */

	/* $if USE_FLAT_FILE_DB $
	public DBThread(Database db) {
		super("DBThread");
		this.db=db;
		this.setDaemon(true);
	}
	$else$ */
	public DBThread(Database db) throws SQLException {
		super("DBThread");
		this.load_conn=db.getLoadConnection();
		this.flush_conn=db.getFlushConnection();
		this.load_conn.setAutoCommit(false);
		this.flush_conn.setAutoCommit(false);
		this.load_conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		this.flush_conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		try {
			this.reading.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		this.setDaemon(true);
	}
	/* $endif$ */

	public void stopThread() {
		this.stop=true;
		work_queue.stop();
	}

	public void addWork(List<SQLStmt> sql) {
		if(stop) {
			throw new RuntimeException("The DB thread is stopped!");
		}
		work_queue.addWork(sql);
	}

	public void addWork(SQLStmt sql) {
		if(stop) {
			throw new RuntimeException("The DB thread is stopped!");
		}
		work_queue.addWork(sql);
	}

	public double full() {
		return work_queue.full();	
	}

	public int size() {
		return work_queue.size();
	}

	/* $if false == USE_FLAT_FILE_DB $ */
	public void closeRS(ResultSet rs) {
		if(stop) {
			throw new RuntimeException("The DB thread is stopped!");
		}
		try {
			rs.close();
			return;
		} catch (SQLException e1) {
			throw new RuntimeException(e1);
		}
		finally {
			reading.release();
		}
	}
	/* $endif$ */

	@Override
	public void run() {
		try {
			int i=0, count=0,c=0;
			ArrayList<SQLStmt> todo = new ArrayList<SQLStmt>(GlobalProperties.BATCH_SIZE);
			while(!stop) {
				long min=-1,max=-1,total=0,commit_time=0;
				todo.clear();
				work_queue.getWork(todo,GlobalProperties.BATCH_SIZE);
				while(todo.size()>0){
					final SQLStmt e = todo.remove(0);
					//jprime.Console.out.println("[dbthread]Start exec of "+(c)+" stmts!");jprime.Console.out.flush();
					try {
						count++;
						c++;
						final long start = System.currentTimeMillis();
						/* $if USE_FLAT_FILE_DB $
						if(e.stmt.reading) {
						$else$ */
						if(e.reading) {
							/* $endif$ */
							if(i>0) {
								i=0;
								//jprime.Console.out.println("[dbthread]Before commit[0]!");jprime.Console.out.flush();
								commit();
							}
							try {
								e.exec();
							} catch(Exception ee) {
								jprime.Console.err.printStackTrace(ee);
								jprime.Console.halt(100);
							}

							/* $if false == USE_FLAT_FILE_DB $ */
							this.reading.acquireUninterruptibly();
							commit();
							/* $endif$ */
						}
						else {
							try {
								i++;
								e.exec();
							} catch(Exception ee) {
								jprime.Console.err.printStackTrace(ee);
								jprime.Console.halt(100);
							}
						}
						final long time = System.currentTimeMillis()-start;
						total+=time;
						if(min==-1 || time < min)min=time;
						if(max==-1 || time > max){
							max=time;
						}
						if(i>=GlobalProperties.BATCH_SIZE ) {//|| (i>5 && Runtime.getRuntime().freeMemory() < GlobalProperties.MIN_FREE)) {
							final long cstart = System.currentTimeMillis();
							i=0;
							//jprime.Console.out.println("[dbthread]Before commit[1]!");jprime.Console.out.flush();
							commit();
							commit_time+=System.currentTimeMillis()-cstart;
						}
					} catch(Exception ee) {
						jprime.Console.err.printStackTrace(ee);
						jprime.Console.halt(100);
					}
					if(max>100 || commit_time>500 || count%(10*GlobalProperties.BATCH_SIZE)==0) {
						jprime.Console.out.println("[dbthread]Finished min="+min+", max="+max+", avg="+(total/(c==0?0.00001:c))+", total="+total+", commit_time="+commit_time+", count="+c+", total_stmts="+count+", outstanding="+work_queue.size());
						c=0;
						min=max=-1;
						total=commit_time=0;
					}
				}
				//XXXif(maxe != null)
				//XXX	jprime.Console.out.println("[dbthread]bad sql="+maxe.stmt);jprime.Console.out.flush();
			}

			jprime.Console.out.println("\tFlushing the DB thread."); jprime.Console.out.flush();
			List<SQLStmt> temp=work_queue.getAllWork();
			jprime.Console.out.println("\t\tWe have "+temp.size()+" batches to flush."); jprime.Console.out.flush();
			c=0;
			try {
				while(temp.size()>0) {
					if(c%50==0) {
						if(c>0)
							jprime.Console.out.print("["+c+"]\n");
						jprime.Console.out.print("\t\t");
						jprime.Console.out.flush();
					}
					SQLStmt e = temp.remove(0);
					/* $if USE_FLAT_FILE_DB $
					if(!e.stmt.reading) {
					$else$ */
					if(!e.reading) {
						/* $endif$ */
						try {
							e.exec();
							count++;
						} catch(Exception ee) {
							jprime.Console.err.printStackTrace(ee);
							jprime.Console.halt(100);
						}
					}
					jprime.Console.out.print(".");
					if(c > 0 && c % GlobalProperties.BATCH_SIZE==0)
						commit();
					c++;
				}
			} catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
			if(c%50!=0) {
				jprime.Console.out.print("["+c+"]\n");
				jprime.Console.out.flush();
				c=0;
			}
			commit();
			jprime.Console.out.println("\n\tExiting the DB thread. Executed "+count+" total statements.");jprime.Console.out.flush();
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
			jprime.Console.halt(100);
		}
	}	

	private void commit() {
		/* $if USE_FLAT_FILE_DB $
		this.db.commit();
		$else$ */
		try {
			load_conn.commit();
			flush_conn.commit();
		} catch(Exception ee) {
			jprime.Console.err.printStackTrace(ee);
			jprime.Console.halt(100);
		}
		/* $endif$ */
	}

	public long exec(SQLStmt s) {
		if(stop) {
			throw new RuntimeException("The DB thread is stopped!");
		}
		WaitingSqlStmt ws = s.asWaiting();
		addWork(ws);
		ws.waitForExec();
		return ws.rv;
	}
	public long exec(List<SQLStmt> stmts) {
		if(stop) {
			throw new RuntimeException("The DB thread is stopped!");
		}
		WaitingSqlStmt ws = stmts.remove(stmts.size()-1).asWaiting();
		stmts.add(ws);
		addWork(stmts);
		ws.waitForExec();
		return ws.rv;
	}


	/* $if USE_FLAT_FILE_DB $
	public RecordBrowser load(LoadSQLStmt s) {
		if(stop) {
			throw new RuntimeException("The DB thread is stopped!");
		}
		addWork(s);
		s.waitForExec();
		return s.getBrowser();
	}
	$else$ */
	public ResultSet load(LoadSQLStmt s) {
		if(stop) {
			throw new RuntimeException("The DB thread is stopped!");
		}
		addWork(s);
		s.waitForExec();
		return s.rs;
	}
	/* $endif$ */

}
