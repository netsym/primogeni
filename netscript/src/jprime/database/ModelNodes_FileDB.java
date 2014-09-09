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

/* $if UPDATE_CIDS_SEPARATE && USE_FLAT_FILE_DB $

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;

import jprime.ModelNodeRecord;
import jprime.database.ModelNodes.AttrsRecord;
import jprime.database.ModelNodes.KidsRecord;
import jprime.database.SimpleDBTable.RecordKey;
import jprime.database.SimpleDBTable.RecordValue;
import jprime.database.SimpleDBTable.RecordValue.RecType;
import jprime.util.NoCopyByteArrayOutputStream;

$else$ */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.channels.FileChannel;

import jprime.ModelNodeRecord;
import jprime.database.SimpleDBTable.RecordKey;
import jprime.database.SimpleDBTable.RecordValue;
import jprime.util.NoCopyByteArrayOutputStream;


/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class ModelNodes_FileDB extends FileDB {
	
	static final class ModelNodeRecordPosition extends RecordPosition implements Serializable, Comparable<RecordPosition>{
		private static final long serialVersionUID = -4750026079202377228L;
		long  kids_start;
		int kids_size;
		/* $if false == SEPARATE_PROP_TABLE $ */
		long attrs_start;
		int attrs_size;
		/* $endif$ */
		
		public ModelNodeRecordPosition(long local_start, int local_size,
				long kids_start, int kids_size
				/* $if false == SEPARATE_PROP_TABLE $ */
				, long attrs_start, int attrs_size
				/* $endif$ */
				) {
			super(local_start, local_size);
			this.kids_start = kids_start;
			this.kids_size = kids_size;
			/* $if false == SEPARATE_PROP_TABLE $ */
			this.attrs_start = attrs_start;
			this.attrs_size = attrs_size;
			/* $endif$ */
		}
		public String toString() {
			return "[local:"+size+"@"+start+", kids:"+kids_size+"@"+kids_start
			/* $if false == SEPARATE_PROP_TABLE $ */
			+", attrs:"+attrs_size+"@"+attrs_start
			/* $endif$ */
			+"]"
			;
		}
	}
	
	
	
	private final RandomAccessFile kids_data;
	private final File kids_data_file;
	private final FileChannel kids_data_channel;
	private final NoCopyByteArrayOutputStream byte_os_kids;
	/* $if false == SEPARATE_PROP_TABLE $ */
	private final RandomAccessFile attrs_data;
	private final File attrs_data_file;
	private final FileChannel attrs_data_channel;
	private final NoCopyByteArrayOutputStream byte_os_attrs;
	/* $endif$ */
	
	
	public ModelNodes_FileDB(String path) {
		super(path);
		this.kids_data_file=new File(path+"kids_data.dat");
		this.kids_data = getData(kids_data_file);
		try {
			total_used+=kids_data.length();
			kids_data.seek(kids_data.length());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.kids_data_channel = this.kids_data.getChannel();
		this.byte_os_kids = new NoCopyByteArrayOutputStream(1024*1024*5);

		/* $if false == SEPARATE_PROP_TABLE $ */
		this.attrs_data_file=new File(path+"attrs_data.dat");
		this.attrs_data = getData(attrs_data_file);
		try {
			total_used+=attrs_data.length();
			attrs_data.seek(attrs_data.length());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.attrs_data_channel = this.attrs_data.getChannel();
		this.byte_os_attrs = new NoCopyByteArrayOutputStream(1024*1024*5);
		/* $endif$ */
	}
	protected RecordValue read(final RecordPosition pos) {
		return read((ModelNodeRecordPosition)pos);
	}
	protected RecordValue read(final ModelNodeRecordPosition pos) {
		/* $if UPDATE_CIDS_SEPARATE && USE_FLAT_FILE_DB $
		ModelNodeRecord mnr=(ModelNodeRecord)super.read(pos);
		try {
			if(pos.kids_start>=0) {
					if(byte_os_kids.size()>0) {
						kids_data_channel.write(byte_os_kids.getByteBuffer());
						//data_channel.force(false);
						//System.out.println("[filedb]["+this.data_file.getParent()+"]flushed... (pos="+data_channel.position()+")");
						byte_os_kids.clear();
					}
					int read = kids_data_channel.read(byte_is.getByteBuffer(pos.kids_size), pos.kids_start);
					if(read <= 0) {
						throw new RuntimeException("How did this happen?");
					}
					else {
						if(read < pos.kids_size) {
							while(read < pos.kids_size) {
								read+=kids_data_channel.read(byte_is.getSlice(), pos.kids_start+read);
							}
						}
					}
					if(pos.kids_size != read) {
						throw new RuntimeException("ACK! asked for "+pos.kids_size+" but got "+read+" bytes!");
					}
					final DataInputStream in = new DataInputStream(byte_is.getInputStream());
					final RecordValue rv = RecType.instance(in.readInt());
					rv.initObject(in);
					mnr.kids=((KidsRecord)rv).kids;
			}
			$if false == SEPARATE_PROP_TABLE $
			if(pos.attrs_start>=0) {
				if(byte_os_attrs.size()>0) {
					attrs_data_channel.write(byte_os_attrs.getByteBuffer());
					//data_channel.force(false);
					//System.out.println("[filedb]["+this.data_file.getParent()+"]flushed... (pos="+data_channel.position()+")");
					byte_os_attrs.clear();
				}
				int read = attrs_data_channel.read(byte_is.getByteBuffer(pos.attrs_size), pos.attrs_start);
				if(read <= 0) {
					throw new RuntimeException("How did this happen?");
				}
				else {
					if(read < pos.attrs_size) {
						while(read < pos.attrs_size) {
							read+=attrs_data_channel.read(byte_is.getSlice(), pos.attrs_start+read);
						}
					}
				}
				if(pos.attrs_size != read) {
					throw new RuntimeException("ACK! asked for "+pos.attrs_size+" but got "+read+" bytes!");
				}
				final DataInputStream in = new DataInputStream(byte_is.getInputStream());
				final RecordValue rv = RecType.instance(in.readInt());
				rv.initObject(in);
				mnr.attrs=((AttrsRecord)rv).attrs;
			}
			$endif$
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return mnr;
		$else$ */
		return null;
		/* $endif$ */
	}
	/* $if UPDATE_CIDS_SEPARATE && USE_FLAT_FILE_DB $
	private ModelNodeRecordPosition encode(ModelNodeRecord value) {
		try {
			if(!byte_os.haveSpace(value.packingsize())) {
				data_channel.write(byte_os.getByteBuffer());
				byte_os.clear();
			}
			int b = byte_os.size();
			final long local_start = data_channel.position()+b;
			DataOutputStream ds = new DataOutputStream(byte_os);
			ds.writeInt(value.getType().id);
			value.flushObject(ds);
			ds.flush();
			final int local_size=byte_os.size()-b;
			
			int kids_size=-1;
			long kids_start=-1;
			$if false == SEPARATE_PROP_TABLE $
			long attrs_start=-1;
			int attrs_size=-1;
			$endif$
			
			if(value.kids != null) {
				if(!byte_os_kids.haveSpace(value.kids.packingsize())) {
					kids_data_channel.write(byte_os_kids.getByteBuffer());
					byte_os_kids.clear();
				}
				b = byte_os_kids.size();
				kids_start = kids_data_channel.position()+b;
				ds = new DataOutputStream(byte_os_kids);
				ds.writeInt(RecType.KidsRecordEnum.id);
				value.kids.flushObject(ds);
				ds.flush();
				kids_size=byte_os_kids.size()-b;
			}
			$if false == SEPARATE_PROP_TABLE $
			if(value.attrs != null) {
				if(!byte_os_attrs.haveSpace(value.attrs.packingsize())) {
					attrs_data_channel.write(byte_os_attrs.getByteBuffer());
					byte_os_attrs.clear();
				}
				b = byte_os_attrs.size();
				attrs_start = attrs_data_channel.position()+b;
				ds = new DataOutputStream(byte_os_attrs);
				ds.writeInt(RecType.AttrsRecordEnum.id);
				value.attrs.flushObject(ds);
				ds.flush();
				attrs_size=byte_os_attrs.size()-b;
			}
			$endif$
			return new ModelNodeRecordPosition(local_start, local_size, kids_start, kids_size
					$if false == SEPARATE_PROP_TABLE $
					, attrs_start, attrs_size
					$endif$
					);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private RecordPosition encode(KidsRecord value) {
		try {
			if(!byte_os_kids.haveSpace(value.packingsize())) {
				kids_data_channel.write(byte_os_kids.getByteBuffer());
				byte_os_kids.clear();
			}
			final int b = byte_os_kids.size();
			final long pos = kids_data_channel.position()+b;
			DataOutputStream ds = new DataOutputStream(byte_os_kids);
			ds.writeInt(value.getType().id);
			value.flushObject(ds);
			ds.flush();
			return new RecordPosition(pos, byte_os_kids.size()-b);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	$if false == SEPARATE_PROP_TABLE $
	private RecordPosition encode(AttrsRecord value) {
		try {
			if(!byte_os_attrs.haveSpace(value.packingsize())) {
				attrs_data_channel.write(byte_os_attrs.getByteBuffer());
				byte_os_attrs.clear();
			}
			final int b = byte_os_attrs.size();
			final long pos = attrs_data_channel.position()+b;
			DataOutputStream ds = new DataOutputStream(byte_os_attrs);
			ds.writeInt(value.getType().id);
			value.flushObject(ds);
			ds.flush();
			return new RecordPosition(pos, byte_os_attrs.size()-b);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	$endif$
	$endif$ */
	
	public long insert(RecordKey key, ModelNodeRecord value) {
		final long id =index.nextId();
		/* $if UPDATE_CIDS_SEPARATE && USE_FLAT_FILE_DB $
		key.setAutoId(id);
		final ModelNodeRecordPosition new_pos = encode(value);
		final ModelNodeRecordPosition old_pos = (ModelNodeRecordPosition)index.remove(key);
		if(old_pos != null) {
			if(new_pos.kids_start<0) {
				new_pos.kids_start=old_pos.kids_start;
				new_pos.kids_size=old_pos.kids_size;
				old_pos.kids_start=-1;
			}
			$if false == SEPARATE_PROP_TABLE $
			if(new_pos.attrs_start<0) {
				new_pos.attrs_start=old_pos.attrs_start;
				new_pos.attrs_size=old_pos.attrs_size;
				old_pos.attrs_start=-1;
			}
			$endif$
			index.addFreePosition(new RecordPosition(old_pos.start, old_pos.size));
			total_free+=old_pos.size;
			if(old_pos.kids_start>=0) {
				index.addFreePosition(new RecordPosition(old_pos.kids_start, old_pos.kids_size));
				total_free+=old_pos.size;
			}
			$if false == SEPARATE_PROP_TABLE $
			if(old_pos.attrs_start>=0) {
				index.addFreePosition(new RecordPosition(old_pos.attrs_start, old_pos.attrs_size));
				total_free+=old_pos.size;
			}
			$endif$
		}
		index.put(key,new_pos);

		total_used+=new_pos.size;
		if(new_pos.kids_start>=0) {
			total_used+=new_pos.kids_size;
		}
		$if false == SEPARATE_PROP_TABLE $
		if(new_pos.attrs_start>=0) {
			total_used+=new_pos.attrs_size;
		}
		$endif$
		$endif$ */
		return id;
	}
	/* $if UPDATE_CIDS_SEPARATE && USE_FLAT_FILE_DB $
	protected final void update(RecordKey key, ModelNodeRecord value) {
		final ModelNodeRecordPosition new_pos = encode(value);
		final ModelNodeRecordPosition old_pos = (ModelNodeRecordPosition)index.remove(key);
		if(old_pos == null) {
			throw new RuntimeException("how did this happen?");
		}
		else {
			if(new_pos.kids_start<0) {
				new_pos.kids_start=old_pos.kids_start;
				new_pos.kids_size=old_pos.kids_size;
				old_pos.kids_start=-1;
			}
			$if false == SEPARATE_PROP_TABLE $
			if(new_pos.attrs_start<0) {
				new_pos.attrs_start=old_pos.attrs_start;
				new_pos.attrs_size=old_pos.attrs_size;
				old_pos.attrs_start=-1;
			}
			$endif$
			index.addFreePosition(new RecordPosition(old_pos.start, old_pos.size));
			total_free+=old_pos.size;
			if(old_pos.kids_start>=0) {
				index.addFreePosition(new RecordPosition(old_pos.kids_start, old_pos.kids_size));
				total_free+=old_pos.size;
			}
			$if false == SEPARATE_PROP_TABLE $
			if(old_pos.attrs_start>=0) {
				index.addFreePosition(new RecordPosition(old_pos.attrs_start, old_pos.attrs_size));
				total_free+=old_pos.size;
			}
			$endif$
		}
		index.put(key,new_pos);		
		index.put(key,new_pos);

		total_used+=new_pos.size;
		if(new_pos.kids_start>=0) {
			total_used+=new_pos.kids_size;
		}
		$if false == SEPARATE_PROP_TABLE $
		if(new_pos.attrs_start>=0) {
			total_used+=new_pos.attrs_size;
		}
		$endif$
	}
	protected final void update(RecordKey key, RecordValue value) {
		throw new RuntimeException("how did this happen?");
	}
	
	protected final void update(RecordKey key, KidsRecord value) {
		final RecordPosition new_pos = encode(value);
		final ModelNodeRecordPosition old_pos = (ModelNodeRecordPosition)index.get(key);
		if(old_pos == null) {
			throw new RuntimeException("how did this happen?");
		}
		index.addFreePosition(new RecordPosition(old_pos.kids_start, old_pos.kids_size));
		total_free+=old_pos.kids_size;
		old_pos.kids_start=new_pos.start;
		old_pos.kids_size=new_pos.size;
		total_used+=new_pos.size;
	}
	$if false == SEPARATE_PROP_TABLE $
	protected final void update(RecordKey key, AttrsRecord value) {
		final RecordPosition new_pos = encode(value);
		final ModelNodeRecordPosition old_pos = (ModelNodeRecordPosition)index.get(key);
		if(old_pos == null) {
			throw new RuntimeException("how did this happen?");
		}
		index.addFreePosition(new RecordPosition(old_pos.attrs_start, old_pos.attrs_size));
		total_free+=old_pos.attrs_size;
		old_pos.attrs_start=new_pos.start;
		old_pos.attrs_size=new_pos.size;
		total_used+=new_pos.size;
	}
	$endif$
	$endif$ */

	
	protected final void delete(RecordKey key) {
		final ModelNodeRecordPosition old_pos = (ModelNodeRecordPosition)index.remove(key);
		index.addFreePosition(new RecordPosition(old_pos.start, old_pos.size));
		total_free+=old_pos.size;
		if(old_pos.kids_start>=0) {
			index.addFreePosition(new RecordPosition(old_pos.kids_start, old_pos.kids_size));
			total_free+=old_pos.size;
		}
		/* $if false == SEPARATE_PROP_TABLE $ */
		if(old_pos.attrs_start>=0) {
			index.addFreePosition(new RecordPosition(old_pos.attrs_start, old_pos.attrs_size));
			total_free+=old_pos.size;
		}
		/* $endif$ */
		//System.out.println("[filedb]["+this.data_file.getParent()+"]delete "+key +"["+old_pos+"]");
	}

	protected final void close() {
		super.close();
		try {
			kids_data_channel.write(byte_os_kids.getByteBuffer());
			kids_data_channel.force(true);
			kids_data_channel.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		/* $if false == SEPARATE_PROP_TABLE $ */
		try {
			attrs_data_channel.write(byte_os_attrs.getByteBuffer());
			attrs_data_channel.force(true);
			attrs_data_channel.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		/* $endif$ */
	}
}
