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


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map.Entry;

import jprime.database.SimpleDBTable.RecordKey;
import jprime.database.SimpleDBTable.RecordValue;
import jprime.database.SimpleDBTable.RecordValue.RecType;
import jprime.util.NoCopyByteArrayInputStream;
import jprime.util.NoCopyByteArrayOutputStream;



/**
 * @author Nathanael Van Vorst
 *
 */
public class FileDB {
	
	public static final class Tuple {
		RecordKey key;
		RecordValue value;
		public Tuple(RecordKey key, RecordValue value) {
			super();
			this.key = key;
			this.value = value;
		}
		public Tuple() {
			super();
			this.key = null;
			this.value = null;
		}
		public RecordKey getKey() {
			return key;
		}
		public RecordValue getValue() {
			return value;
		}
	}

	public final class Browser {
		private Iterator<Entry<RecordKey, RecordPosition>> iter;
		public Browser(RecordKey key) {
			this.iter = index.getSubmap(key,this).entrySet().iterator();
		}
		public boolean next(Tuple t) {
			if(iter != null) {
				if(iter.hasNext()) {
					final Entry<RecordKey, RecordPosition> u=iter.next();
					t.key=u.getKey();
					t.value=read(u.getValue());
					return true;
				}
				iter=null;
			}
			return false;
		}
	}
	static class RecordPosition implements Serializable, Comparable<RecordPosition>{
		private static final long serialVersionUID = -4750026079202377228L;
		long start;
		int size;
		public RecordPosition(long start, int size) {
			super();
			this.start = start;
			this.size = size;
		}
		public int compareTo(RecordPosition o) {
			return size-o.size;
		}
		public String toString() {
			return size+"@"+start;
		}
	}
	protected final File index_file,data_file;
	protected final Index index;
	protected final RandomAccessFile data;
	protected final FileChannel data_channel;
	protected final NoCopyByteArrayOutputStream byte_os;
	protected final NoCopyByteArrayInputStream byte_is;
	protected long total_used=0,total_free=0;
	public FileDB(String path) {
		super();
		this.index_file=new File(path+"index.dat");
		this.data_file=new File(path+"data.dat");
		Index t = Index.fromFile(index_file);
		if(t==null) {
			t = new Index();
			Index.toFile(index_file, t);
		}
		this.index=t;
		this.data = getData(data_file);
		try {
			total_used=data.length();
			data.seek(total_used);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.data_channel = this.data.getChannel();
		this.byte_os = new NoCopyByteArrayOutputStream(1024*1024*5);
		this.byte_is = new NoCopyByteArrayInputStream(1024*1024);
	}
	protected static RandomAccessFile getData(File f) {
		try {
			return new RandomAccessFile(f, "rw");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	protected RecordValue read(final RecordPosition pos) {
		try {
			if(pos.start<0 || pos.size<=0){
				throw new RuntimeException("ACK!!!! start<0 (read)");
			}
			/*
			if(pos.start > data_channel.position()) {
				//its in the buffer
				final DataInputStream in = new DataInputStream(byte_os.slice((int)(pos.start-data_channel.position()), pos.size));
				final RecordValue rv = RecType.instance(in.readInt());
				rv.initObject(in);
				return rv;
			}
			else */
			//XXX
			if(byte_os.size()>0) {
				data_channel.write(byte_os.getByteBuffer());
				//data_channel.force(false);
				//System.out.println("[filedb]["+this.data_file.getParent()+"]flushed... (pos="+data_channel.position()+")");
				byte_os.clear();
			}//XXX
			{
				int read = data_channel.read(byte_is.getByteBuffer(pos.size), pos.start);
				if(read <= 0) {
					throw new RuntimeException("How did this happen?");
				}
				else {
					if(read < pos.size) {
						while(read < pos.size) {
							read+=data_channel.read(byte_is.getSlice(), pos.start+read);
						}
					}
				}
				if(pos.size != read) {
					throw new RuntimeException("ACK! asked for "+pos.size+" but got "+read+" bytes!");
				}
				final DataInputStream in = new DataInputStream(byte_is.getInputStream());
				final RecordValue rv = RecType.instance(in.readInt());
				rv.initObject(in);
				return rv;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	private RecordPosition encode(RecordValue value) {
		try {
			if(!byte_os.haveSpace(value.packingsize())) {
				data_channel.write(byte_os.getByteBuffer());
				byte_os.clear();
			}
			final int b = byte_os.size();
			final long pos = data_channel.position()+b;
			DataOutputStream ds = new DataOutputStream(byte_os);
			ds.writeInt(value.getType().id);
			value.flushObject(ds);
			ds.flush();
			return new RecordPosition(pos, byte_os.size()-b);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public long insert(final RecordKey key, final RecordValue value) {
		final long id =index.nextId();
		key.setAutoId(id);
		final RecordPosition new_pos = encode(value);
		final RecordPosition old_pos = index.put(key,new_pos);
		if(old_pos != null) {
			index.addFreePosition(old_pos);
			total_free+=old_pos.size;
		}
		total_used+=new_pos.size;
		return id;
	}
	protected void update(final RecordKey key, final RecordValue value) {
		final RecordPosition new_pos = encode(value);
		final RecordPosition old_pos = index.get(key);
		if(old_pos == null) {
			jprime.Console.err.println("Warning: [filedb]["+this.data_file.getParent()+"] expected to find "+key+" in the index but did not!");
			//throw new RuntimeException("how did this happen? key="+key);
			index.put(key, new_pos);
		}
		else {
			final long st= old_pos.start;
			final int si=old_pos.size;
			total_free+=old_pos.size;
			total_used+=new_pos.size;
			old_pos.size=new_pos.size;
			old_pos.start=new_pos.start;
			new_pos.size=si;
			new_pos.start=st;
			index.addFreePosition(new_pos);
		}
	}

	protected void delete(RecordKey key) {
		final RecordPosition old_pos = index.remove(key);
		if(old_pos == null) {
			throw new RuntimeException("ACK!");
		}
		//System.out.println("[filedb]["+this.data_file.getParent()+"]delete "+key +"["+old_pos+"]");
		index.addFreePosition(old_pos);
	}

	protected Browser browse(RecordKey key) {
		//System.out.println("[filedb]["+this.data_file.getParent()+"]browse "+key);
		return new Browser(key);
	}

	protected void close() {
		System.out.println("[filedb]["+this.data_file.getParent()+"] total_free = "+total_free+", total_used="+total_used+", idx size:"+index.records());
		Index.toFile(index_file, index);
		try {
			//System.out.println("[filedb]["+this.data_file.getParent()+"]flushing out "+byte_os.size()+" bytes. (pos="+data_channel.position()+")");
			//System.out.flush();
			data_channel.write(byte_os.getByteBuffer());
			data_channel.force(true);
			//System.out.println("[filedb]["+this.data_file.getParent()+"]flushed... (pos="+data_channel.position()+")");
			data_channel.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	protected final void commit() {
	}
	protected void getLock() {
		index.getLock();
	}
	protected void releaseLock() {
		index.releaseLock();
	}

}
