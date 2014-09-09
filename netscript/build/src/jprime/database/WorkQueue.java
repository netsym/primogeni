package jprime.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import jprime.database.Table.LoadSQLStmt;
import jprime.database.Table.SQLStmt;
import jprime.database.Table.WaitingSqlStmt;
import jprime.util.GlobalProperties;

class WorkQueue {
	private ArrayBlockingQueue<SQLStmt> work;
	private final double cap;
	private boolean stop=false;
	
	public WorkQueue() {
		this((GlobalProperties.DB_FLUSH_Q_SIZE>0)?(GlobalProperties.DB_FLUSH_Q_SIZE):(50*GlobalProperties.BATCH_SIZE));
	}
	public WorkQueue(int capacity) {
		this.cap = capacity;
		this.work = new ArrayBlockingQueue<SQLStmt>(capacity);
	}
	public void stop() {
		stop=true;
		addWork(Table.STOPWORK);
	}
	
	public double full() {
		return work.size()/cap;
	}

	public SQLStmt getSingleWork() {
		if(stop) return Table.STOPWORK;
		try {
			return work.take();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return getSingleWork();
		}
	}
	public void getWork(final ArrayList<SQLStmt> rv, int size) {
		if(0 == work.drainTo(rv, size)) {
			rv.add(getSingleWork());
		}
	}
	public LinkedList<SQLStmt> getAllWork() {
		LinkedList<SQLStmt> rv=new LinkedList<SQLStmt>();
		work.drainTo(rv);
		return rv;
	}
	
	public void addWork(List<SQLStmt> sql) {
		for(SQLStmt s : sql) {
			addWork(s);
		}
	}
	public void addWork(SQLStmt s) {
		while(true) {
			try {
				work.put(s);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void addWork(WaitingSqlStmt s) {
		while(true) {
			try {
				work.put(s);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void addWork(LoadSQLStmt s) {
		while(true) {
			try {
				work.put(s);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
	public int size() {
		return work.size();
	}
}