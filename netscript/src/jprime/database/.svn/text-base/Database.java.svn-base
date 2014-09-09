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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jprime.EntityFactory;
import jprime.Experiment;
import jprime.Metadata;
import jprime.PersistableObject;
import jprime.StatusListener;
import jprime.database.IPersistableCache.CacheType;
import jprime.database.IPersistableCache.PhaseChange;
import jprime.database.Table.SQLStmt;
import jprime.util.GlobalProperties;

$else$ */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jprime.EntityFactory;
import jprime.Experiment;
import jprime.Metadata;
import jprime.PersistableObject;
import jprime.StatusListener;
import jprime.database.IPersistableCache.CacheType;
import jprime.database.IPersistableCache.PhaseChange;
import jprime.database.Table.SQLStmt;
import jprime.util.GlobalProperties;


/* $endif$ */


/**
 * @author Nathanael Van Vorst
 */
public class Database {
	private static Map<String, Database> dbReferences = new HashMap<String,Database>();
    public class ShutdownThread extends Thread {
    	ShutdownThread() {
        }
        public void run() {
        	/* $if DEBUG $
        	cache.logPhaseChange(PhaseChange.shutdown,"");
        	shutdown();
        	cache.logPhaseChange(PhaseChange.finished,"");
        	$else$ */
        	IPersistableCache.logPhaseChange(PhaseChange.shutdown,"");
        	shutdown();
        	IPersistableCache.logPhaseChange(PhaseChange.finished,"");
        	/* $endif$ */
        }
    }

    /* $if false == USE_FLAT_FILE_DB $ */
	private final Connection flush_conn, load_conn;
    /* $endif$ */
	private final DBThread db_thread;
	private final DBType dbType;
	
	//the tables
	/* $if SEPARATE_PROP_TABLE $
	public final Attrs Attrs;
	$endif$ */
	public final BGPLinkTypes BGPLinkTypes;
	public final EmulationCommands EmulationCommands;
	public final Experiments Experiments;
	public final Metadatas Metadatas;
	public final ModelNodes ModelNodes;
	public final RouteEntries RouteEntries;
	public final RoutingProtocols RoutingProtocols;
	public final SymbolTables SymbolTables;
	public final IPersistableCache cache;
	/* $if USE_FLAT_FILE_DB $
	private final ArrayList<Table> tables = new ArrayList<Table>();
	 $endif$ */

	/**
	 * 
	 * @param driver
	 * @param url
	 * @param user
	 * @param pass
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Database(final DBType dbType, final Map<String,String> params) throws SQLException {
		final int capacity=Integer.parseInt(params.remove("CACHE_SIZE"));
		if(capacity<=0) {
			try {
				throw new RuntimeException("Invalid cache size; it must be greater than 0!");
			}catch(Exception e) {
				try {
					jprime.Console.err.printStackTrace(e);
				} catch(Exception ee) {
					e.printStackTrace();
				}
				jprime.Console.halt(100);
			}
		}
		this.dbType=dbType;
		this.dbType.setup(params);
		
	    /* $if false == USE_FLAT_FILE_DB $ */
		jprime.Console.out.println("url="+dbType.getURL());
		
		this.load_conn = (Connection) DriverManager.getConnection(dbType.getURL()); 
		this.flush_conn = (Connection) DriverManager.getConnection(dbType.getURL());
	    /* $endif$ */
		
		/* $if false == USE_FLAT_FILE_DB $ */
        final ArrayList<Table> tables = new ArrayList<Table>();
		/* $endif$ */

		//create table objs
		jprime.Console.err.println("Creating database tables!");
		/* $if SEPARATE_PROP_TABLE $
		this.Attrs=new Attrs(this); tables.add(Attrs);
		$endif$ */
		this.BGPLinkTypes=new BGPLinkTypes(this); tables.add(BGPLinkTypes);
		this.EmulationCommands=new EmulationCommands(this); tables.add(EmulationCommands);
		this.Experiments=new Experiments(this); tables.add(Experiments);
		this.Metadatas=new Metadatas(this); tables.add(Metadatas);
		this.ModelNodes=new ModelNodes(this); tables.add(ModelNodes);
		this.RouteEntries=new RouteEntries(this); tables.add(RouteEntries);
		this.RoutingProtocols=new RoutingProtocols(this); tables.add(RoutingProtocols);
		this.SymbolTables=new SymbolTables(this); tables.add(SymbolTables);
        
		/* $if USE_FLAT_FILE_DB $
		for(Table tbl:tables) {
			tbl.createTable();
		}
		$else$ */
		Statement stmt;
		jprime.Console.err.flush();
		jprime.Console.err.flush();
		for(Table tbl:tables) {
			jprime.Console.err.println("Creating table "+tbl.tableName);
			for(String t: tbl.createTable()) {
				jprime.Console.err.println("\t"+t);
				try {
					stmt = (Statement) flush_conn.createStatement();
					stmt.execute(t);
					stmt.close();
					//jprime.Console.err.println("\tSUCCESS");
				} catch (SQLException e) {
					//jprime.Console.err.println("\tFailure:");
					//jprime.Console.err.printStackTrace(e);
				}
			}
		}
		/* $endif$ */
		for(Table tbl:tables) {
			tbl.setup();
		}
		this.db_thread=new DBThread(this);
		
		this.cache=CacheType.createCache(GlobalProperties.DB_CACHE_TYPE, capacity,db_thread);
		this.db_thread.start();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
	}

	@Override
	protected void finalize() throws Throwable {
		shutdown();
		super.finalize();
	}
	
	/* $if USE_FLAT_FILE_DB $
	protected void commit() {
		//System.out.println("datbase.commit()");
		for(Table tbl:tables) {
			tbl.commit();
		}
	}
	protected void close() {
		for(Table tbl:tables) {
			tbl.close();
		}
	}
	 $endif$ */

	/**
	 * @return
	 */
	public IPersistableCache getCache() {
		return cache;
	}
	
	/**
	 * @return
	 */
	public DBThread getDBThread() {
		return db_thread;
	}
	
	
	/**
	 * @return
	 */
	public DBType getDbType() {
		return dbType;
	}
	
	/* $if false == USE_FLAT_FILE_DB $ */
	protected Connection getFlushConnection() {
		return flush_conn;
	}
	
	protected Connection getLoadConnection() {
		return load_conn;
	}
	/* $endif$ */
	
	protected DBType getDBType() {
		return dbType;
	}
	
    public void shutdown()
    {
    	jprime.Console.out.println("Shutting down..."); jprime.Console.out.flush();
	   	jprime.Console.out.println("\tStart Flushing."); jprime.Console.out.flush();
    	cache.flush(true);
	   	jprime.Console.out.println("\tDone Flushing."); jprime.Console.out.flush();
    	jprime.Console.out.println("\tStopping DB thread");  jprime.Console.out.flush();
		db_thread.stopThread();
    	jprime.Console.out.println("\tStopped DB thread, waiting for it join()."); jprime.Console.out.flush();
		try {
			db_thread.join();
		} catch (InterruptedException e) {
		}
		/* $if USE_FLAT_FILE_DB $
		close();
		$else$ */
        try
        {
            if (flush_conn != null)
            {
                flush_conn.close();
            }
            if (load_conn != null)
            {
                load_conn.close();
            }
        }
        catch (SQLException e)
        {
        	jprime.Console.err.printStackTrace(e);
        }
		/* $endif$ */
    	jprime.Console.out.println("\tDone shutting down."); jprime.Console.out.flush();
    }

	public static Database createDatabase() {
		Map<String,String> params  = new HashMap<String, String>();
		params.put("CACHE_SIZE", ""+GlobalProperties.CACHE_SIZE);
		return Database.createDatabase(GlobalProperties.DB_TYPE,params);
	}

	public static Database createDatabase(Map<String,String> params) {
		if(!params.containsKey("CACHE_SIZE"))
			params.put("CACHE_SIZE", ""+GlobalProperties.CACHE_SIZE);
		return Database.createDatabase(GlobalProperties.DB_TYPE, params);
	}
	
	/**
	 * 
	 * @param driver
	 * @param url
	 * @param user
	 * @param pass
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Database createDatabase(DBType dbType, Map<String,String> params) {
		Database db = null;
		synchronized(dbReferences) {
    		db = dbReferences.get(dbType.getURL());
    		if(db==null) {
    			try {
	    			db=new Database(dbType,params);
	    			dbReferences.put(dbType.getURL(),db);
    			} catch (Exception e) {
    				try {
    					jprime.Console.err.printStackTrace(e);
    				} catch(Exception ee) {
    					e.printStackTrace();
    				}
    				jprime.Console.halt(100, "Could not open database. You probably have another instance of jprime or slingshot running!");
    			}
    		}
    	}
		return db;
	}
	
	/** 
	 * @param name
	 * @return
	 */
	public Experiment createExperiment(String name, boolean replace) {
		Experiment prev = Experiments.load(name,this.Metadatas);
		if(prev != null) {
			if(replace) {
				Experiments.delete(prev);
			}
			else {
				throw new RuntimeException("There is already a "+prev.getClass().getSimpleName()+" with the name '"+name+"'");
			}
		}
		Experiment rv = new Experiment(this,name);
		this.save(rv,null);
		return rv;
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public Experiment loadExperiment(String name) {
		return this.Experiments.loadExperiment(name,this.Metadatas);
	}

	public Metadata loadMetadata(long metaid) {
		return this.Metadatas.load(metaid);
	}
	
	public int countInCoreObjs() {
		return cache.size();
	}
	
	public void save(Experiment obj, StatusListener sl) {
		try {
			synchronized(obj) {
				db_thread.addWork(processObject(obj,false));
				db_thread.addWork(processObject(obj.getMetadata(),false));
				obj.saved();
			}
			if(sl!=null) {
				cache.saveExp(obj.getMetadata().getDBID(), sl);
			}
		}
		catch(Exception e) {
			try {
				jprime.Console.err.printStackTrace(e);
			} catch(Exception ee) {
				e.printStackTrace();
			}
			jprime.Console.halt(100);
		}
	}

	/**
	 * @param 
	 */
	public void remove(Experiment obj){
		//jprime.Console.err.println("delete obj:"+obj+", state="+obj.getPersistableState());jprime.Console.err.flush();
		db_thread.addWork(processObject(obj,true));
	}
	
	public List<String> listExperiments() {
		return this.Experiments.listExperiments();
	}

	public void addMetadata(Metadata m) {
		cache.addMetadata(m);
	}
	
	public void addExperiment(Experiment l) {
		cache.addExperiment(l);
	}

	public Metadata getMetadata(long id) {
		return cache.getMetadata(id);
	}
	
	public Experiment getExperiment(String name) {
		return cache.getExperiment(name);
	}
	
	public void removeMetadata(long id) {
		cache.removeMetadata(id);
	}

	public void removeLibExp(String name) {
		cache.removeLibExp(name);
	}
	
	public List<SQLStmt> processObject(PersistableObject obj, boolean delete) {
		switch(obj.getTypeId()) {
		case EntityFactory.Experiment:
			return this.Experiments.processObject(obj,delete);
		case EntityFactory.Metadata:
			return this.Metadatas.processObject(obj,delete);
		case EntityFactory.SymbolTable:
			return this.SymbolTables.processObject(obj,delete);
		case EntityFactory.BGPLinkType :
			return this.BGPLinkTypes.processObject(obj,delete);
		case EntityFactory.EmulationCommand :
			return this.EmulationCommands.processObject(obj,delete);
		case EntityFactory.StaticRoutingProtocolStart:
		case EntityFactory.StaticRoutingProtocolEND:
		case EntityFactory.BGP:
		case EntityFactory.ShortestPath:
		case EntityFactory.AlgorithmicRouting:
			return this.RoutingProtocols.processObject(obj,delete);
		case EntityFactory.RouteEntry:
			return this.RouteEntries.processObject(obj,delete);	
		case EntityFactory.BooleanVariable:
		case EntityFactory.FloatingPointNumberVariable:
		case EntityFactory.IntegerVariable:
		case EntityFactory.ListVariable:
		case EntityFactory.OpaqueVariable:
		case EntityFactory.ResourceIdentifierVariable:
		case EntityFactory.StringVariable:
		case EntityFactory.SymbolVariable:		
		case EntityFactory.ModelNodeVaribaleStart:
		case EntityFactory.ModelNodeVaribaleEnd:
			/* $if SEPARATE_PROP_TABLE $
			return this.Attrs.processObject(obj,delete);
			$else$ */
			throw new RuntimeException("Dont save model node variables when SEPARATE_PROP_TABLE is set to false!");
			/* $endif$ */
		default:
			if(obj.getTypeId()>EntityFactory.START_MODEL_NODE_TYPES) { 
				return this.ModelNodes.processObject(obj,delete);
			}
			throw new RuntimeException("The obj "+obj+" has an unknown type id!");
		}
	}
}
