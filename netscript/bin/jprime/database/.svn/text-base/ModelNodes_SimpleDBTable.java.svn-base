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

/* $if USE_FLAT_FILE_DB && UPDATE_CIDS_SEPARATE $

$if SEPARATE_PROP_TABLE $

import java.io.File;

import jprime.ModelNodeRecord;
import jprime.database.ModelNodes.KidsRecord;

$else$

import java.io.File;

import jprime.ModelNodeRecord;
import jprime.database.ModelNodes.AttrsRecord;
import jprime.database.ModelNodes.KidsRecord;

$endif$


$endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
/* $if UPDATE_CIDS_SEPARATE && USE_FLAT_FILE_DB $
public abstract class ModelNodes_SimpleDBTable extends Table {

	private ModelNodes_FileDB myfiledb;
	
	protected ModelNodes_SimpleDBTable(String tableName, Database db) {
		super(tableName,db);
		this.myfiledb=null;
	}

	public void createTable() {
		jprime.Console.out.println("["+tableName+"] Creating table at "+path);
		final File p = new File(path);
		if(!p.exists()) {
			p.mkdirs();
		}
		if(!p.exists() || p.isFile()) {
			throw new RuntimeException("Unable to open/create directory "+path);
		}
		this.myfiledb = new ModelNodes_FileDB(path);
		this.filedb = myfiledb;
		commit();
	}
	
	public long insert(RecordKey key, RecordValue value) {
		return myfiledb.insert(key, (ModelNodeRecord)value);
	}
	protected void update(RecordKey key, RecordValue value) {
		switch(value.getType()) {
		case ModelNodeRecordEnum:
			myfiledb.update(key, value);
			break;
		case KidsRecordEnum:
			myfiledb.update(key, (KidsRecord)value);
			break;
			$if false == SEPARATE_PROP_TABLE $
		case AttrsRecordEnum:
			myfiledb.update(key, (AttrsRecord)value);
			break;
			$endif$
		default:
			throw new RuntimeException("wtf?");
		}
	}	
	protected final long insert(RecordKey key, ModelNodeRecord value) {
		return myfiledb.insert(key,value);
	}
	protected void update(RecordKey key, ModelNodeRecord value) {
		myfiledb.update(key, value);
	}
	protected void update(RecordKey key, KidsRecord value) {
		myfiledb.update(key, value);
	}
	$if false == SEPARATE_PROP_TABLE $
	protected final void update(RecordKey key, AttrsRecord value) {
		myfiledb.update(key, value);
	}
	$endif$

}

$else$ */
public abstract class ModelNodes_SimpleDBTable  { }
/* $endif$ */
