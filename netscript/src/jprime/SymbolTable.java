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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.util.TreeMap;

import jprime.database.PKey;

/**
 * 
 * @author Nathanael Van Vorst
 */
public class SymbolTable extends PersistableObject {
	//transient
	private Metadata meta;
	
	//persisted
	private long dbid;
	private long metadata_id;
	private TreeMap<String,Integer> symbols;
	
	/**
	 * used when loaded from DB
	 * 
	 * @param dbid
	 */
	public SymbolTable(Metadata meta, long dbid, TreeMap<String,Integer> symbols) {
		this.dbid=dbid;
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.symbols=symbols;
		this.persistable_state=PersistableState.UNMODIFIED;
		this.mods=Modified.NOTHING.id;
		meta.loaded(this);
	}
	
	/**
	 * 
	 */
	public SymbolTable(Metadata meta) {
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.dbid=meta.getNextModelNodeDBID();
		this.symbols=new TreeMap<String, Integer>();
		this.persistable_state=PersistableState.NEW;
		this.mods=Modified.ALL.id;
		meta.loaded(this);
	}
	
	@Override
	protected void finalize() throws Throwable {
		meta.collect(this);
		super.finalize();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SymbolTable) {
			return this.getMetadataID() == ((SymbolTable)obj).getMetadataID() && getDBID() ==  ((SymbolTable)obj).getDBID();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see jprime.IPeristable#save()
	 */
	public synchronized void save() {
		meta.save(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.PeristableObject#getPKey()
	 */
	public PKey getPKey() {
		return new PKey(metadata_id, dbid);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getMetadata()
	 */
	public Metadata getMetadata() {
		return meta;
	}

	/**
	 * @return the id
	 */
	public long getDBID() {
		return this.dbid;
	}
	/**
	 * @return the id
	 */
	public long getMetadataID() {
		return this.metadata_id;
	}

	/* (non-Javadoc)
	 * @see jprime.IPeristable#getTypeId()
	 */
	public int getTypeId() {
		return EntityFactory.SymbolTable;
	}

	public void registerSymbol(String name) {
		if(symbols.containsKey(name)) {
			symbols.put(name,symbols.get(name)+1);
		}
		else {
			symbols.put(name,1);
		}
		modified(Modified.ALL);
	}
	
	public void unregisterSymbol(String name) {
		if(symbols.containsKey(name)) {
			int c = symbols.get(name);
			if(c==1) {
				symbols.remove(name);
			}
			else {
				symbols.put(name,c-1);				
			}
		}
	}
	
	public TreeMap<String,Integer> getSymbolMap() {
		return symbols;
	}
	
	public ByteArrayInputStream symbolMapToBytes() {
        //use buffering
        ByteArrayOutputStream bos = new ByteArrayOutputStream(symbols.size()*4) ;
        ObjectOutput out;
		try {
			out = new ObjectOutputStream(bos);
	        out.writeObject(symbols);
	        out.close();
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
		return new ByteArrayInputStream(bos.toByteArray());
	}
	
	@SuppressWarnings("unchecked")
	public static TreeMap<String,Integer> symbolMapFromBytes(Blob b) {
		try {
			if(b.length()>0) {
				byte[] bytes = b.getBytes(1, (int)b.length());
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
				TreeMap<String,Integer> rv = (TreeMap<String,Integer>) in.readObject();
				in.close();
				b.free();
				return rv;
			}
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			//jprime.Console.halt(100);
		}
		return new TreeMap<String,Integer>();
	}
}
