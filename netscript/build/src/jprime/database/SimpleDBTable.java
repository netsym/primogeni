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

$if SEPARATE_PROP_TABLE $

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import jprime.ModelNodeRecord;
import jprime.database.Attrs.VarRecord;
import jprime.database.BGPLinkTypes.BgpLinkTypeRec;
import jprime.database.Datasets.DatasetValue;
import jprime.database.EmulationCommands.EmuCmdRec;
import jprime.database.Experiments.ExpRecord;
import jprime.database.Field.BoundValue;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.database.Metadatas.MetadataRecord;
import jprime.database.RouteEntries.RouteEntryRec;
import jprime.database.SymbolTables.SymbolTableRec;
import jprime.database.TimeSeries.TimeSeriesRecord;
import jprime.util.GlobalProperties;

$else$

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import jprime.ModelNodeRecord;
import jprime.database.BGPLinkTypes.BgpLinkTypeRec;
import jprime.database.EmulationCommands.EmuCmdRec;
import jprime.database.Experiments.ExpRecord;
import jprime.database.Field.BoundValue;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.database.Metadatas.MetadataRecord;
import jprime.database.ModelNodes.AttrsRecord;
import jprime.database.ModelNodes.KidsRecord;
import jprime.database.RouteEntries.RouteEntryRec;
import jprime.database.SymbolTables.SymbolTableRec;
import jprime.util.GlobalProperties;

$endif$


$else$ */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import jprime.database.Field.BoundValue;
import jprime.database.FileDB.Browser;
import jprime.database.FileDB.Tuple;
import jprime.util.GlobalProperties;


/* $endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */


public abstract class SimpleDBTable {
	public static class MyComparator implements Serializable, Comparator<RecordKey> {
		private static final long serialVersionUID = -1508687106227581673L;
		public int compare(RecordKey o1, RecordKey o2) {
			return o1.compareTo(o2);
		}
	}
	public static class RecordKey implements Serializable, Comparable<RecordKey> {
		private static final long serialVersionUID = 1529469554043335596L;
		final long metadata_id;
		long dbid;
		public RecordKey(long metadata_id, long dbid) {
			super();
			this.metadata_id = metadata_id;
			this.dbid = dbid;
		}
		public void setAutoId(long id) {
		}
		public int compareTo(RecordKey o) {
			if(o.getClass() == getClass()) {
				if(metadata_id == o.metadata_id) {
					return (int)(dbid - o.dbid);
				}
				return (int)(metadata_id - o.metadata_id);
			}
			throw new RuntimeException("How did this happen?");
		}
		public String toString() {
			return "[RecordKey "+metadata_id+" "+dbid+"]";
		}
	}
	public static abstract class RecordValue {
		abstract public void flushObject(DataOutputStream out) throws Exception;
		abstract public void initObject(DataInputStream in) throws Exception;
		abstract public int packingsize();
		abstract public RecType getType();
		public static abstract class RecordValueMaker {
			public abstract RecordValue instance();
		}
		public static enum RecType {
			/* $if USE_FLAT_FILE_DB $
			BGPLinkRecEnum(0, new RecordValueMaker() {
				public RecordValue instance() {
					return new BgpLinkTypeRec();
				}
			}),
			EmuCmdRecEmum(1, new RecordValueMaker() {
				public RecordValue instance() {
					return new EmuCmdRec();
				}
			}),
			ExpRecordEnum(2, new RecordValueMaker() {
				public RecordValue instance() {
					return new ExpRecord();
				}
			}),
			SymbolTableRecEmum(3, new RecordValueMaker() {
				public RecordValue instance() {
					return new SymbolTableRec();
				}
			}),
			MetadataRecordEnum(4, new RecordValueMaker() {
				public RecordValue instance() {
					return new MetadataRecord();
				}
			}),
			RouteEntryRecEnum(5, new RecordValueMaker() {
				public RecordValue instance() {
					return new RouteEntryRec();
				}
			}),
			ModelNodeRecordEnum(6, new RecordValueMaker() {
				public RecordValue instance() {
					return new ModelNodeRecord();
				}
			}),
			KidsRecordEnum(7, new RecordValueMaker() {
				public RecordValue instance() {
					return new KidsRecord();
				}
			}),
			$if SEPARATE_PROP_TABLE $
			VarRecordEnum(8, new RecordValueMaker() {
				public RecordValue instance() {
					return new VarRecord();
				}
			}),
			$else$
			AttrsRecordEnum(8, new RecordValueMaker() {
				public RecordValue instance() {
					return new AttrsRecord();
				}
			}),
			$endif$
			
			;
			public final int id;
			public final RecordValueMaker maker;
			RecType(int i, RecordValueMaker m) {
				id=i;
				maker=m;
			}
			static final RecType[] types = new RecType[] {
				BGPLinkRecEnum,      EmuCmdRecEmum,      ExpRecordEnum,
				SymbolTableRecEmum,  MetadataRecordEnum, RouteEntryRecEnum,
				ModelNodeRecordEnum, KidsRecordEnum,
				$if SEPARATE_PROP_TABLE $
				VarRecordEnum
				$else$
				AttrsRecordEnum
				$endif$

			};
			static RecordValue instance(int type) {
				return types[type].maker.instance();
			}
			$else$ */
			NOOP;
			final int id=0;
			static RecordValue instance(int type) {
				return null;
			}
			/* $endif$ */
		}
	}
	public static final class Record {
		final RecordKey key;
		final RecordValue value;
		public Record(RecordKey key, RecordValue value) {
			super();
			this.key = key;
			this.value = value;
		}
	}
	protected class RecordBrowser {
		private final Browser b;
		private final Tuple t;
		public Record value;
		public RecordBrowser(RecordKey key) {
			this.b = browse(key);
			this.value = null;
			t=new Tuple(null,null);
		}
		public Browser getBrowser() {
			return b;
		}
		public boolean next() {
			if(b.next(t)) {
				value=new Record(t.key,t.value);
				return true;
			}
			return false;
		}
	}
	public abstract class Stmt {
		final boolean reading;
		private Stmt(boolean reading) {
			this.reading = reading;
		}
		final public void releaseLock() {
			filedb.releaseLock();
		}
		public abstract long exec(List<BoundValue> vals);
		public abstract long _exec(List<BoundValue> vals);
	}
	public abstract class Select extends Stmt {
		public volatile RecordBrowser browser;
		public Select() {
			super(true);
			this.browser=null;
		}
		@Override
		final public long exec(List<BoundValue> vals) {
			filedb.getLock();
			return _exec(vals);
		}
		@Override
		public abstract long _exec(List<BoundValue> vals);
	}
	public abstract class Delete extends Stmt {
		public Delete() {
			super(false);
		}
		@Override
		final public long exec(List<BoundValue> vals) {
			filedb.getLock();
			final long rv = _exec(vals);
			filedb.releaseLock();
			return rv;
		}
		@Override
		public abstract long _exec(List<BoundValue> vals);
	}
	public abstract class Insert extends Stmt {
		public Insert() {
			super(false);
		}
		@Override
		final public long exec(List<BoundValue> vals) {
			filedb.getLock();
			final long rv = _exec(vals);
			filedb.releaseLock();
			return rv;
		}
		@Override
		public abstract long _exec(List<BoundValue> vals);
	}
	public abstract class Update extends Stmt {
		public Update() {
			super(false);
		}
		@Override
		final public long exec(List<BoundValue> vals) {
			filedb.getLock();
			final long rv = _exec(vals);
			filedb.releaseLock();
			return rv;
		}
		@Override
		public abstract long _exec(List<BoundValue> vals);
	}
	
	protected final String tableName;
	protected final List<Field> fields=new ArrayList<Field>();
	protected final Database db;
	protected Update update;
	protected Insert insert;
	protected Select select;
	protected Delete delete;
	protected Delete deleteByMetadata;

	
	protected FileDB filedb;
	protected final String path;

	/**
	 * @param tableName
	 */
	protected SimpleDBTable(String tableName, Database db) {
		this.tableName=tableName;
		this.db=db;
		this.path=GlobalProperties.DB_PATH+"/"+GlobalProperties.DB_NAME+"/"+tableName+"/";
		this.select=null;
		this.update=null;
		this.insert=null;
		this.delete=null;
		this.deleteByMetadata=null;
		this.filedb=null;
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
		this.filedb = new FileDB(path);
		commit();
	}
	protected abstract void setup();
	protected void setup_common() {
		this.select = new Select() {
			@Override
			public long _exec(List<BoundValue> vals) {

				/*
				 * these all pass in dbid then metaid:
				SELECT SQL for attrs = SELECT  dbid, metadata, owner, name, type, value FROM attrs  WHERE dbid = ?  AND metadata = ?
				SELECT SQL for bgp_link_types = SELECT  dbid, metadata, parent, link_type, first_child_is_src, alias_path FROM bgp_link_types  WHERE dbid = ?  AND metadata = ?
				SELECT SQL for emulation_commands = SELECT  dbid, metadata, parent, raw_cmd, output_suffix, delay, raw_max_runtime, block, checkReturnCode FROM emulation_commands  WHERE dbid = ?  AND metadata = ?
				SELECT SQL for model_nodes = SELECT  dbid, metadata, parent, replica_metaid , replica, attached_link, type, db_order, uid, offset, size, name, has_been_replicated, kids FROM model_nodes  WHERE dbid = ?  AND metadata = ?
				SELECT SQL for route_entries = SELECT  dbid, metadata_id, owning_host, parent_id, outbound_iface, next_hop_id, src_min, src_max, dst_min, dst_max, edge_iface, cost, busIdx, num_of_bits, num_of_bits_bus FROM route_entries  WHERE dbid = ?  AND metadata_id = ?
				SELECT SQL for routing_protocols = SELECT  dbid, metadata, parent, type, kids FROM routing_protocols  WHERE dbid = ?  AND metadata = ?
				SELECT SQL for symbol_tables = SELECT  dbid, metadata, symbol_map FROM symbol_tables  WHERE dbid = ?  AND metadata = ?
				SELECT SQL for dataset = SELECT  dbid, metadata, date FROM dataset  WHERE dbid = ?  AND metadata = ?
				 */
				/*
				 * these are special and should be overridden:
				SELECT SQL for timeseries = SELECT  metadata, datasetid, nodeid, dbid, name, time, value FROM timeseries  WHERE metadata = ?  AND datasetid = ?  AND nodeid = ?  AND dbid = ?			
				SELECT SQL for lib_exp = SELECT  name, metadata, type, kids FROM lib_exp  WHERE name = ?
				SELECT SQL for metadatas = SELECT  dbid, max_node_id, state, symboltable_id FROM metadatas  WHERE dbid = ?
				SELECT SQL for attrs = SELECT  dbid, metadata, owner, name, type, value FROM attrs  WHERE dbid = ?  AND metadata = ?
				 */

				this.browser = new RecordBrowser(new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value));
				return 0;
			}
		};
		this.delete = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				/*
				 * these all pass in dbid then metaid:
				DELETE SQL for attrs = DELETE FROM attrs WHERE dbid = ?  AND metadata = ?
				DELETE SQL for bgp_link_types = DELETE FROM bgp_link_types WHERE dbid = ?  AND metadata = ?
				DELETE SQL for emulation_commands = DELETE FROM emulation_commands WHERE dbid = ?  AND metadata = ?
				DELETE SQL for model_nodes = DELETE FROM model_nodes WHERE dbid = ?  AND metadata = ?
				DELETE SQL for route_entries = DELETE FROM route_entries WHERE dbid = ?  AND metadata_id = ?
				DELETE SQL for routing_protocols = DELETE FROM routing_protocols WHERE dbid = ?  AND metadata = ?
				DELETE SQL for symbol_tables = DELETE FROM symbol_tables WHERE dbid = ?  AND metadata = ?
				DELETE SQL for dataset = DELETE FROM dataset WHERE dbid = ?  AND metadata = ?
				 */
				/*
				 * these are special and should be overridden:
				DELETE SQL for lib_exp = DELETE FROM lib_exp WHERE name = ?
				DELETE SQL for metadatas = DELETE FROM metadatas WHERE dbid = ?
				DELETE SQL for timeseries = DELETE FROM timeseries WHERE metadata = ?  AND datasetid = ?  AND nodeid = ?  AND dbid = ?
				DELETE SQL for attrs = DELETE FROM attrs WHERE dbid = ?  AND metadata = ?
				 */
				delete(new RecordKey((Long)vals.get(1).value, (Long)vals.get(0).value));
				return 0;
			}
		};
		this.deleteByMetadata = new Delete() {
			@Override
			public long _exec(List<BoundValue> vals) {
				/*
				 * these all pass in dbid then metaid:
				DELETE-BY-METADATA SQL for attrs = DELETE FROM attrs WHERE metadata = ?
				DELETE-BY-METADATA SQL for bgp_link_types = DELETE FROM bgp_link_types WHERE metadata = ?
				DELETE-BY-METADATA SQL for emulation_commands = DELETE FROM emulation_commands WHERE metadata = ?
				DELETE-BY-METADATA SQL for lib_exp = DELETE FROM lib_exp WHERE metadata = ?
				DELETE-BY-METADATA SQL for metadatas = DELETE FROM metadatas WHERE dbid = ?
				DELETE-BY-METADATA SQL for model_nodes = DELETE FROM model_nodes WHERE metadata = ?
				DELETE-BY-METADATA SQL for route_entries = DELETE FROM route_entries WHERE metadata_id = ?
				DELETE-BY-METADATA SQL for routing_protocols = DELETE FROM routing_protocols WHERE metadata = ?
				DELETE-BY-METADATA SQL for symbol_tables = DELETE FROM symbol_tables WHERE metadata = ?
				DELETE-BY-METADATA SQL for dataset = DELETE FROM dataset WHERE metadata = ?
				DELETE-BY-METADATA SQL for timeseries = DELETE FROM timeseries WHERE metadata = ?
				 */
				/*
				 * these are special and should be overridden:
				DELETE SQL for lib_exp = DELETE FROM lib_exp WHERE name = ?
				DELETE SQL for metadatas = DELETE FROM metadatas WHERE dbid = ?
				DELETE SQL for timeseries = DELETE FROM timeseries WHERE metadata = ?  AND datasetid = ?  AND nodeid = ?  AND dbid = ?
				DELETE-BY-METADATA SQL for attrs = DELETE FROM attrs WHERE metadata = ?
				 */
				
				final TreeSet<Long> dbids = new TreeSet<Long>();
				final RecordKey key = new RecordKey((Long)vals.get(0).value,0);
				final Browser b = browse(key);
				final Tuple t=new Tuple();
				while(b.next(t)) {
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
	
	protected long insert(RecordKey key, RecordValue value) {
		return filedb.insert(key,value);
	}

	protected void update(RecordKey key, RecordValue value) {
		filedb.update(key, value);
	}

	protected final void delete(RecordKey key) {
		filedb.delete(key);
	}

	protected final Browser browse(RecordKey key) {
		return filedb.browse(key);
	}

	protected final void close() {
		filedb.close();
	}
	
	protected final void commit() {
		filedb.commit();
	}
}
