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

import java.util.List;

import jprime.Host.IHost;
import jprime.database.DBThread;
import jprime.database.Database;
import jprime.database.IPersistableCache;
/* $if DEBUG $
import jprime.database.IPersistableCache.PhaseChange;
$endif$ */
import jprime.database.PKey;
import jprime.database.Table.SQLStmt;
import jprime.routing.BGPLinkType;
import jprime.routing.RouteEntry;
import jprime.routing.StaticRoutingProtocol;
import jprime.util.GlobalProperties;
/* $if SEPARATE_PROP_TABLE $
import jprime.variable.ModelNodeVariable;
$endif$ */

/**
 * @author Nathanael Van Vorst
 */
public class Metadata extends PersistableObject {
	//transient
	private SymbolTable symbolTable=null;
	private State state_enum=null;
	private final Database db;
    private final IPersistableCache cache;
	private final DBThread db_thread;
	
	//persisted
	private long dbid; //autoassigned
	private long max_node_id;
	private long symboltable_id;
	private int state;

	public Metadata(Database db,long dbid, long max_node_id, int state, long symboltable_id) {
		super();
		this.db=db;
		this.cache=db.getCache();
		this.db_thread=db.getDBThread();
		this.dbid = dbid;
		this.max_node_id = max_node_id;
		this.state = state;
		this.symboltable_id=symboltable_id;
		this.persistable_state=PersistableState.MODIFIED; //we should always save this!
		this.mods=Modified.ALL.id;
		db.addMetadata(this);
	}
	
	public static Metadata createNewMeta(Database db) {
		Metadata meta = new Metadata(db);
		meta.save();
		meta.symbolTable=new SymbolTable(meta);
		meta.symboltable_id=meta.symbolTable.getDBID();
		meta.symbolTable.save();
		db.addMetadata(meta);
		return meta;
	}

	/**
	 * 
	 */
	private Metadata(Database db) {
		this.state=State.PRE_COMPILED.val;
		this.state_enum=null;
		this.dbid=0;
		this.max_node_id=1;
		this.symboltable_id=-1;
		this.db=db;
		this.cache=db.getCache();
		this.db_thread=db.getDBThread();
		this.persistable_state=PersistableState.NEW;
		this.mods=Modified.ALL.id;
	}
	
	/* (non-Javadoc)
	 * @see jprime.PersistableObject#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.oldHashCode();
	}
	
	@Override
	protected void finalize() throws Throwable {
		save();
	}
	
	public Metadata getMetadata() {
		return this;
	}

	public void __setdbid(long id) {
		this.dbid=id;
	}

	public long getDB_max_node_id() {
		return max_node_id;
	}
	
	public int getDB_state() {
		return state;
	}

	public long getSymboltableId() {
		return symboltable_id;
	}
	
	/**
	 * 
	 * @param exp
	 */
	public synchronized void save() {
		List<SQLStmt> work = db.processObject(this,false);
		if(work.size()!=1)
			throw new RuntimeException("what hapened?");
		if(this.persistable_state== PersistableState.NEW)
			this.__setdbid(db_thread.exec(work.get(0)));
		else
			db_thread.exec(work.get(0));
		this.saved();
	}

	public long getNextModelNodeDBID() {
		return ++max_node_id;
	}
		
	public Database getDatabase() {
		if(db == null) {
			try {
				throw new RuntimeException("What happened?");
			} catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		return this.db;
	}
	
	/**
	 * 
	 * @return the id
	 */
	public long getDBID() {
		return dbid;
	}

	/**
	 * 
	 * @return the symbolTable
	 */
	public SymbolTable getSymbolTable() {
		if(symbolTable==null) {
			this.symbolTable=loadSymbolTable();
		}
		return symbolTable;
	}

	/**
	 * 
	 * @return the state
	 */
	public State getState() {
		if(state_enum == null)
			state_enum=State.fromInt(this.state);
		return state_enum;
	}
	
	/**
	 * 
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
		this.state_enum=State.fromInt(this.state);
	}
	
	/**
	 * 
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state_enum=state;
		this.state = state_enum.val;
	}

	/* (non-Javadoc)
	 * @see jprime.IPeristable#getTypeId()
	 */
	public int getTypeId() {
		return EntityFactory.Metadata;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#saved()
	 */
	public void saved() {
		switch(this.persistable_state) {
		case UNMODIFIED:
		case MODIFIED:
			//no up;
			break;
		case NEW:
			this.persistable_state=PersistableState.MODIFIED; //we should always save this!
			break;
		case DEAD:
		case ORPHAN:
			throw new UnsupportedOperationException();
		default:
		{
			try {
				throw new RuntimeException("How did this happen?");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
			}
			throw new UnsupportedOperationException();
		}
		}
	}
	
	public PersistableObject findLoadedObj(PKey p) {
		return cache.findLoadedObj(p);
	}
	
	public ModelNode getPreloadedModelNode(long id) {
		return (ModelNode)cache.findLoadedObj(new PKey(this.getDBID(), id));
	}
	
	public ModelNode loadModelNode(long metaid, long id) {
		return db.ModelNodes.load(db.getMetadata(metaid),id);
	}
		
	public ModelNode loadModelNode(long id, int batchsize) {
		if(batchsize==1) {
			return db.ModelNodes.load(this, id);
		}
		return (ModelNode)(db.ModelNodes.loadMany(this, id).get(0));
	}
	
	public ModelNode loadModelNode(long id) {
		return loadModelNode(id,GlobalProperties.FETCH_SIZE);
	}

	/* $if SEPARATE_PROP_TABLE $
	public ModelNodeVariable loadModelNodeVariable(long id, long owner) {
		return db.Attrs.load(this, id, owner);
	}
	$endif$ */
	
	public SymbolTable loadSymbolTable() {
		return db.SymbolTables.load(this, symboltable_id);
	}

	public StaticRoutingProtocol loadStaticRoutingProtocol(long id) {
		return db.RoutingProtocols.load(this, id);
	}
	
	public BGPLinkType loadBGPLinkType(long id, int batchsize) {
		if(batchsize==1)
			return db.BGPLinkTypes.load(this, id);
		return db.BGPLinkTypes.loadMany(this, id, id+batchsize).get(0);
	}

	public BGPLinkType loadBGPLinkType(long id) {
		return loadBGPLinkType(id,GlobalProperties.FETCH_SIZE);
	}
	
	public RouteEntry loadRouteEntry(long id, int batchsize) {
		if(batchsize==1)
			return db.RouteEntries.load(this, id);
		return db.RouteEntries.loadMany(this, id, id+batchsize).get(0);
	}
	
	public RouteEntry loadRouteEntry(long id) {
		return loadRouteEntry(id, GlobalProperties.FETCH_SIZE);
	}
	
	public EmulationCommand loadEmulationCommand(long id, IHost parent) {
		return db.EmulationCommands.loadMany(this, parent, id).get(0);
	}

	public EmulationCommand loadEmulationCommand(long id, Experiment parent) {
		return db.EmulationCommands.loadMany(this, parent, id).get(0);
	}
	
	public void loaded(PersistableObject obj) {
		cache.loaded(obj);
    	/* $if DEBUG $
		logLoad(obj);
		$endif$ */
	}


	public void modified(PersistableObject obj) {
		cache.modified(obj);
    	/* $if DEBUG $
		logAccess(obj);
		$endif$ */
	}
	
	
	/**
	 * 
	 * @param 
	 */
	public void save(PersistableObject obj) {
		while(true) {
			try {
				synchronized(obj) {
					try {
						db_thread.addWork(db.processObject(obj,false));
					}
					catch(Exception e) {
						jprime.Console.err.printStackTrace(e);
						jprime.Console.halt(100);
					}
					obj.saved();
				}
				break;
			} catch(java.lang.OutOfMemoryError e) {
				e.printStackTrace();
				if(Runtime.getRuntime().freeMemory() < 1024*1024) {
					jprime.Console.halt(100, "out of memory!");
				}
				else {
					Thread.yield();
					System.gc();
					System.runFinalization();
					System.gc();
				}
			}
		}
	}
	
	public void collect(PersistableObject obj) {
		db_thread.addWork(db.processObject(obj,false));
		__remove_finalizer(obj);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getPKey()
	 */
	public PKey getPKey() {
		return null;
	}
	
	/* $if SEPARATE_PROP_TABLE $
	public void remove(ModelNode owner, ModelNodeVariable attr){
		owner.getPersistentAttrMap().remove(attr.getDBName());
		db_thread.addWork(db.processObject(attr,true));
		cache.remove(attr);
	}
	$endif$ */
	
	/**
	 * @param 
	 */
	public void remove(PersistableObject obj,boolean delete){
		db_thread.addWork(db.processObject(obj,delete));
		cache.remove(obj);
	}
	
	/* $if DEBUG $
	public void logAccess(IModelNode obj) {
		try {
			ModelNode rv = ((ModelNode)obj);
			cache.accessed(rv);
			cache.logAccess(rv.getTypeId(), rv.getDBID(), rv.getParentId());
		}
		catch(Exception e1) {
			jprime.Console.err.printStackTrace(e1);
			try {
				throw new RuntimeException("How did this happen? obj.class="+obj.getClass().getSimpleName());
			}
			catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
	}
	
	public void logAccess(ModelNode obj) {
		cache.accessed(obj);
		cache.logAccess(obj.getTypeId(), obj.getDBID(), obj.getParentId());
	}
	
	public void logAccess(StaticRoutingProtocol obj) {
		cache.accessed(obj);
		cache.logAccess(obj.getTypeId(), obj.getDBID(), obj.getParentId());
	}
	
	$if SEPARATE_PROP_TABLE $
	public void logAccess(ModelNodeVariable obj) {
		cache.accessed(obj);
		cache.logAccess(obj.getTypeId(), obj.getDBID(), obj.getOwnerID());
	}
	$endif$
	
	public void logAccess(RouteEntry obj, long parent_id) {
		cache.accessed(obj);
		cache.logAccess(obj.getTypeId(), obj.getDBID(), parent_id);
	}
	
	final public void logAccess(PersistableObject obj) {
		if(obj instanceof ModelNode) {
			cache.accessed(obj);
			ModelNode rv = ((ModelNode)obj);
			cache.logAccess(rv.getTypeId(), rv.getDBID(), rv.getParentId());
		}
		else if(obj instanceof StaticRoutingProtocol) {
			cache.accessed(obj);
			StaticRoutingProtocol rv = ((StaticRoutingProtocol)obj);
			cache.logAccess(rv.getTypeId(), rv.getDBID(), rv.getParentId());
		}
		$if SEPARATE_PROP_TABLE $
		else if(obj instanceof ModelNodeVariable) {
			cache.accessed(obj);
			ModelNodeVariable rv = ((ModelNodeVariable)obj);
			cache.logAccess(rv.getTypeId(), rv.getDBID(), rv.getOwnerID());
		}
		$endif$
		else if(obj instanceof RouteEntry) {
			cache.accessed(obj);
			cache.logAccess(obj.getTypeId(), obj.getDBID(), -1);
		}
		else {
			cache.logAccess(obj.getTypeId(), obj.getDBID(), -1);
		}
	}
	
	public void logLoad(PersistableObject obj) {
		if(obj instanceof ModelNode) {
			ModelNode rv = ((ModelNode)obj);
			if(rv.getPersistableState()==PersistableState.NEW)
				cache.logCreate(rv.getTypeId(), rv.getDBID(), rv.getParentId());
			else 
				cache.logLoad(rv, rv.getParentId());
		}
		else if(obj instanceof StaticRoutingProtocol) {
			StaticRoutingProtocol rv = ((StaticRoutingProtocol)obj);
			if(rv.getPersistableState()==PersistableState.NEW)
				cache.logCreate(rv.getTypeId(), rv.getDBID(), rv.getParentId());
			else 
				cache.logLoad(rv, rv.getParentId());
		}
		$if SEPARATE_PROP_TABLE $
		else if(obj instanceof ModelNodeVariable) {
			ModelNodeVariable rv = ((ModelNodeVariable)obj);
			if(rv.getPersistableState()==PersistableState.NEW)
				cache.logCreate(rv.getTypeId(), rv.getDBID(), rv.getOwnerID());
			else 
				cache.logLoad(rv, rv.getOwnerID());
		}
		$endif$
		else {
			if(obj.getPersistableState()==PersistableState.NEW)
				cache.logCreate(obj.getTypeId(), obj.getDBID(), -1);
			else 
				cache.logLoad(obj, -1);
		}
	}
	
	public void logPhaseChange(PhaseChange phase) {
		cache.logPhaseChange(phase,"");
	}
	$endif$ */

	/**
	 * @param 
	 */
	private void __remove_finalizer(PersistableObject obj){
		switch(obj.getPersistableState()) {
		case DEAD:
			synchronized(obj) {
				db_thread.addWork(db.processObject(obj,false));
				obj.saved();
			}
			cache.remove(obj);
			break;
		case MODIFIED:
			try {
				throw new RuntimeException("how did this happen?");
			} catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
			}
			jprime.Console.halt(100);
		break;
		case ORPHAN:
		default:
			cache.collect(obj);
		}
	}
}
