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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import jprime.database.FileDB.Browser;
import jprime.database.FileDB.RecordPosition;
import jprime.database.SimpleDBTable.RecordKey;

/**
 * @author Nathanael Van Vorst
 *
 */
final class Index implements Serializable {
	private static final long serialVersionUID = 1321052603618006884L;
	private final TreeMap<RecordKey,RecordPosition> records;
	private final TreeSet<RecordPosition> freeset;
	private volatile long auto_id;
	private final Semaphore lock = new Semaphore(1);
	public Index() {
		super();
		this.records=new TreeMap<SimpleDBTable.RecordKey, FileDB.RecordPosition>();
		this.freeset=new TreeSet<FileDB.RecordPosition>();
		this.auto_id=10L;
	}
	protected void getLock() {
		lock.acquireUninterruptibly();
	}
	protected void releaseLock() {
		lock.release();
	}
	protected RecordPosition put(RecordKey key, RecordPosition value) {
		if(value.start<0 || value.size<=0){
			throw new RuntimeException("ACK!!!! start<0 (read)");
		}
		return records.put(key, value);
	}
	protected RecordPosition get(Object key) {
		return records.get(key);
	}
	protected RecordPosition remove(Object key) {
		return records.remove(key);
	}
	protected NavigableMap<RecordKey,RecordPosition> getSubmap(final RecordKey key, final Browser b) {
		return records.tailMap(key, true);
	}
	protected boolean addFreePosition(RecordPosition e) {
		return freeset.add(e);
	}
	protected int records() {
		return records.size();
	}
	protected long nextId() {
		return auto_id++;
	}
	
	public static void toFile(final File f, final Index idx) {
		try {
			System.out.println("[filedb]["+f.getAbsolutePath()+"] writing index contents(map:"
					+idx.records.size()+",free:"+idx.freeset.size()+", auto_id:"+idx.auto_id+"):");
			//for(Entry<RecordKey, RecordPosition> e : idx.map.entrySet()) {
			//	System.out.println("[filedb]\t"+e.getKey()+" --> "+e.getValue());
			//}
			final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(idx);
			oos.close();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static Index fromFile(final File f) {
		try {
			final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			final Index idx = (Index) ois.readObject();
			ois.close();
			System.out.println("[filedb]["+f.getAbsolutePath()+"] read index contents(map:"
					+idx.records.size()+",free:"+idx.freeset.size()+", auto_id:"+idx.auto_id+"):");
			//for(Entry<RecordKey, RecordPosition> e : idx.map.entrySet()) {
			//	System.out.println("[filedb]\t"+e.getKey()+" --> "+e.getValue());
			//}
			return idx;
		} catch(Exception e) {
		}
		return null;
	}
}