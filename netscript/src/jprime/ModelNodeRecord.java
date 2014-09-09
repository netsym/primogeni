package jprime;

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

$if SEPARATE_PROP_TABLE $
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import jprime.database.ChildIdList;
import jprime.database.SimpleDBTable.RecordValue;
import jprime.util.NoCopyByteArrayOutputStream;

$else$
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import jprime.database.ChildIdList;
import jprime.database.SimpleDBTable.RecordValue;
import jprime.util.NoCopyByteArrayOutputStream;
import jprime.util.PersistentAttrMap;

$endif$

$else$ */

/* $if SEPARATE_PROP_TABLE $
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import jprime.database.ChildIdList;
import jprime.util.NoCopyByteArrayOutputStream;

$else$ */
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import jprime.database.ChildIdList;
import jprime.util.NoCopyByteArrayOutputStream;
import jprime.util.PersistentAttrMap;

/* $endif$ */

/* $endif$ */


/**
 * @author Nathanael Van Vorst
 *
 */

/* $if USE_FLAT_FILE_DB $
public class ModelNodeRecord extends RecordValue implements Serializable {
	private final static long serialVersionUID = -6793754549503141029L;
	$else$ */
public class ModelNodeRecord {
/* $endif$ */
	public long dbid;
	public long db_metadata_id;
	public long db_parent_id;
	public long replica_meta_id;
	public long replica_id;
	public long attached_link_id;
	public int db_type;
	public int db_order;
	public long uid;
	public long offset;
	public long size;
	public String name;
	public boolean has_been_replicated;
	public transient Metadata meta;
	public transient ChildIdList kids;
	/* $if false == SEPARATE_PROP_TABLE $ */
	public transient PersistentAttrMap attrs;
	/* $endif$ */



	public ModelNodeRecord(DataInputStream in) {
		super();
		try {
			this.meta = null;
			this.dbid = in.readLong();
			this.db_metadata_id = in.readLong();
			this.db_parent_id = in.readLong();
			this.db_type=in.readInt();
			this.db_order = in.readInt();
			this.replica_meta_id=in.readLong();
			this.replica_id=in.readLong();
			this.attached_link_id = in.readLong();
			this.uid = in.readLong();
			this.offset = in.readLong();
			this.size = in.readLong();
			byte[] b = new byte[in.readInt()];
			this.name = new String(b);
			this.has_been_replicated = in.readBoolean();

			/* $if false == UPDATE_CIDS_SEPARATE $ */
			if(in.readBoolean())
				this.kids=ChildIdList.fromBytes(in);
			else
				this.kids=new ChildIdList();
			/* $if false == SEPARATE_PROP_TABLE $ */
			if(in.readBoolean())
				this.attrs=PersistentAttrMap.fromBytes(in);
			else
				this.attrs=new PersistentAttrMap(null);
			/* $endif$ */
			/* $else$
			this.kids=null;
			$endif$ */
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	/* $if USE_FLAT_FILE_DB $
	public ModelNodeRecord() { }
	public RecType getType() { return RecType.ModelNodeRecordEnum;}
	$endif$ */

	/** 
	 * 
	 * for model nodes 
	 * 
	 * 
	 * @param meta
	 * @param dbid
	 * @param db_metadata_id
	 * @param db_parent_id
	 * @param replica_id
	 * @param attached_link_id
	 * @param db_type
	 * @param db_order
	 * @param uid
	 * @param offset
	 * @param size
	 * @param name
	 * @param has_been_replicated
	 */
	public ModelNodeRecord(
			Metadata meta,
			long dbid,
			long db_metadata_id,
			long db_parent_id,
			long replica_meta_id,
			long replica_id,
			long attached_link_id,
			int db_type,
			int db_order,
			long uid,
			long offset,
			long size,
			String name,
			boolean has_been_replicated,ChildIdList kids
			/* $if false == SEPARATE_PROP_TABLE $ */
			, PersistentAttrMap attrs
			/* $endif$ */
	) {
		super();
		this.meta=meta;
		this.dbid = dbid;
		this.db_metadata_id = db_metadata_id;
		this.db_parent_id = db_parent_id;
		this.db_type=db_type;
		this.db_order = db_order;
		this.replica_meta_id=replica_meta_id;
		this.replica_id=replica_id;
		this.attached_link_id = attached_link_id;
		this.uid = uid;
		this.offset = offset;
		this.size = size;
		this.name = name;
		this.has_been_replicated = has_been_replicated;
		this.kids=kids;
		/* $if false == SEPARATE_PROP_TABLE $ */
		this.attrs=attrs;
		/* $endif$ */
	}

	/**
	 * 
	 * for model node records
	 * 
	 * @param meta
	 * @param dbid
	 * @param db_metadata_id
	 * @param db_parent_id
	 * @param db_type
	 */
	public ModelNodeRecord(
			Metadata meta,
			long dbid,
			long db_metadata_id,
			long db_parent_id,
			int db_type,
			ChildIdList kids
			/* $if false == SEPARATE_PROP_TABLE $ */
			, PersistentAttrMap attrs
			/* $endif$ */
	) {
		super();
		this.meta=meta;
		this.dbid = dbid;
		this.db_metadata_id = db_metadata_id;
		this.db_parent_id = db_parent_id;
		this.db_type=db_type;
		this.db_order = -1;
		this.replica_meta_id=-1;
		this.replica_id=-1;
		this.attached_link_id = -1;
		this.uid = -1;
		this.offset = -1;
		this.size = -1;
		this.name = "";
		this.has_been_replicated = false;
		this.kids=kids;
		/* $if false == SEPARATE_PROP_TABLE $ */
		this.attrs=attrs;
		/* $endif$ */
	}

	public void initObject(DataInputStream in) throws Exception {
		try {
			this.meta = null;
			this.dbid = in.readLong();
			this.db_metadata_id = in.readLong();
			this.db_parent_id = in.readLong();
			this.replica_meta_id=in.readLong();
			this.replica_id=in.readLong();
			this.attached_link_id = in.readLong();

			this.db_type=in.readInt();
			this.db_order = in.readInt();
			
			this.uid = in.readLong();
			this.offset = in.readLong();
			this.size = in.readLong();
			
			final int s =in.readInt();
			if(s>0) {
				byte[] b = new byte[s];
				in.read(b);
				this.name = new String(b);
			}
			else {
				name="";
			}
			this.has_been_replicated = in.readBoolean();
			
			/* $if false == UPDATE_CIDS_SEPARATE $ */
			if(in.readBoolean())
				this.kids=ChildIdList.fromBytes(in);
			else
				this.kids=new ChildIdList();
			/* $if false == SEPARATE_PROP_TABLE $ */
			if(in.readBoolean())
				this.attrs=PersistentAttrMap.fromBytes(in);
			else
				this.attrs=new PersistentAttrMap(null);
			/* $endif$ */
			/* $else$
			this.kids=null;
			$endif$ */
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	public void flushObject(DataOutputStream out) throws Exception { 
		out.writeLong(dbid);
		out.writeLong(db_metadata_id);
		out.writeLong(db_parent_id);
		out.writeLong(replica_meta_id);
		out.writeLong(replica_id);
		out.writeLong(attached_link_id);
		
		out.writeInt(db_type);
		out.writeInt(db_order);
		
		out.writeLong(uid);
		out.writeLong(offset);
		out.writeLong(size);
		
		if(name.length()>0) {
			out.writeInt(name.length());
			out.writeBytes(name);
		}
		else {
			out.writeInt(0);
		}
		
		out.writeBoolean(has_been_replicated);

		/* $if false == UPDATE_CIDS_SEPARATE $ */
		if(kids!=null){
			out.writeBoolean(true);
			kids.flushObject(out);
		}
		else {
			out.writeBoolean(false);
		}
		/* $if false == SEPARATE_PROP_TABLE $ */
		if(attrs!=null){
			out.writeBoolean(true);
			attrs.flushObject(out);
		}
		else {
			out.writeBoolean(false);
		}
		/* $endif$ */
		/* $endif$ */
		//System.out.println("serialized modelnode record from flushObject");
	}  

	public int packingsize() {
		/* $if UPDATE_CIDS_SEPARATE $
		return (Long.SIZE*9+Integer.SIZE*3+Byte.SIZE)/8+name.length();
		$else$ */
		return (Long.SIZE*9+Integer.SIZE*3+Byte.SIZE)/8
		+name.length()
		+(kids==null?0:kids.packingsize())
		/* $if false == SEPARATE_PROP_TABLE $ */
		+(attrs==null?0:attrs.packingsize())
		/* $endif$ */
		;
		/* $endif$ */
	}
	public static ByteArrayInputStream toBytes(ModelNodeRecord l) {
		int size=l.packingsize();
		try {
			NoCopyByteArrayOutputStream bos =new NoCopyByteArrayOutputStream(size);
			DataOutputStream out = new DataOutputStream(bos);
			l.flushObject(out);
			return bos.getBufferAsByteArrayInputStream();
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
	}

	public static ModelNodeRecord fromBytes(DataInputStream in) {
		try {
			return new ModelNodeRecord(in);
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			throw new RuntimeException(e);
		}
	}	
}
