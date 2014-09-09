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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Net.Net;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.database.Database;
/* $if false == DEBUG $ */
import jprime.database.IPersistableCache;
/* $endif$ */
import jprime.database.IPersistableCache.PhaseChange;
import jprime.database.PKey;
import jprime.partitioning.Partitioner;
import jprime.partitioning.Partitioning;
import jprime.routing.RouteCalculationVisitor;
import jprime.util.ComputeNode;
import jprime.util.PersistentChildList;
import jprime.util.PersistentEmuCmdList;
import jprime.util.PersistentModelNodeList;
import jprime.util.Portal;
import jprime.visitors.CNFVisitor;
import jprime.visitors.IPAddressAssignment;
import jprime.visitors.UIDAssignmentVisitor;
import jprime.visitors.VerifyVisitor;

/**
 * @author Nathanael Van Vorst
 */
public class Experiment extends PersistableObject {
	//transient
	private Metadata metadata;
	protected PersistentModelNodeList<IEmulationProtocol> emu_protos;
	protected PersistentEmuCmdList cmds;
	protected PersistentChildList roots;

	//persisted
	private String name;
	private long metadata_id;

	/**
	 * Create new experiment
	 * @param name
	 */
	public Experiment(Database db, String name) {
		this.name=name;
		this.metadata=Metadata.createNewMeta(db);
		this.metadata_id=metadata.getDBID();
		this.roots = new PersistentChildList(this.metadata);
		this.persistable_state=PersistableState.NEW;
		this.mods=Modified.ALL.id;
		if(this instanceof Experiment) {
			this.cmds=new PersistentEmuCmdList(this.metadata, (Experiment)this);
			this.emu_protos=new PersistentModelNodeList<IEmulationProtocol>(this.metadata);
		}
		else {
			this.cmds=null;
			this.emu_protos=null;
		}
		jprime.Console.out.println("Created new lib/exp, metadata_id="+this.metadata.getDBID()+", name="+this.name);
		metadata.loaded(this);
		metadata.getDatabase().addExperiment(this);
	}


	/**
	 * Load experiment from db
	 * 
	 * @param name
	 */
	public Experiment(String name, Metadata meta,ChildIdList kids) {
		if(name==null) {
			throw new RuntimeException("The name is null!");
		}
		else if(meta==null) {
			throw new RuntimeException("The metadata is null!");
		}
		this.name=name;
		this.metadata_id=meta.getDBID();
		this.metadata=meta;
		this.persistable_state=PersistableState.UNMODIFIED;
		this.mods=Modified.NOTHING.id;
		this.roots = new PersistentChildList(this.metadata);
		if(this instanceof Experiment) {
			this.cmds=new PersistentEmuCmdList(this.metadata, (Experiment)this);
			this.emu_protos=new PersistentModelNodeList<IEmulationProtocol>(this.metadata);
		}
		else {
			this.cmds=null;
			this.emu_protos=null;
		}

		for(ChildId kid:kids) {
			switch(kid.type) {
			case EntityFactory.EmulationCommand:
				cmds.addKey(kid.child_id);
				break;
			case EntityFactory.TrafficPortal:
			case EntityFactory.TrafficPortalAlias:
			case EntityFactory.TrafficPortalReplica:
			case EntityFactory.TrafficPortalAliasReplica:
			case EntityFactory.TAPEmulation:
			case EntityFactory.TAPEmulationAlias:
			case EntityFactory.TAPEmulationAliasReplica:
			case EntityFactory.TAPEmulationReplica:
			case EntityFactory.OpenVPNEmulation:
			case EntityFactory.OpenVPNEmulationAlias:
			case EntityFactory.OpenVPNEmulationAliasReplica:
			case EntityFactory.OpenVPNEmulationReplica:
				emu_protos.addKey(kid.child_id,kid.type);
				break;
			case EntityFactory.Net:
				roots.addKey(kid.child_id, kid.order,kid.type);
				break;
			default:
				throw new RuntimeException("Unknown child of lib/exp. type_id="+kid.type+", dbid="+kid.child_id);
			}
		}
		jprime.Console.out.println("Loaded new lib/exp, metadata_id="+this.metadata.getDBID()+", name="+this.name);
		metadata.loaded(this);
		metadata.getDatabase().addExperiment(this);
	}

	protected void finalize() throws Throwable {
		if(this.persistable_state != PersistableState.UNMODIFIED) {
			try {
				throw new RuntimeException("what happened?");
			} catch(Exception e){
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		super.finalize();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Experiment) {
			return this.getMetadataID() == ((Experiment)obj).getMetadataID() && getName().equals(((Experiment)obj).getName());
		}
		return false;
	}
	
	/**
	 * @param _name
	 * @return
	 */
	public synchronized Net createNewRoot(String _name) {
		Net rv = new jprime.Net.Net(this.metadata,_name,roots.size());
		this.roots.add(rv);
		modified(Modified.CHILDREN);
		return rv;
	}
	
	/**
	 * @return
	 */
	public State getState() {
		return metadata.getState();
	}
	
	/**
	 * @param state
	 */
	public void setState(State state) {
		metadata.setState(state);
	}
	
	/**
	 * @return
	 */
	public Metadata getMetadata() {
		return metadata;
	}
	
	/**
	 * @return
	 */
	public long getMetadataID() {
		return metadata_id;
	}

	/* (non-Javadoc)
	 * @see jprime.IPeristable#getChildIds()
	 */
	public synchronized ChildIdList getChildIds() {
		ChildIdList rv = null;
		if(cmds == null)
			rv = new ChildIdList();
		else
			rv = cmds.getChildIds();
		if(emu_protos != null) rv.addAll(emu_protos.getChildIds());
		rv.addAll(roots.getChildIds());
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getTypeId()
	 */
	public int getTypeId() {
		return EntityFactory.Experiment;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getDBID()
	 */
	public long getDBID() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getPKey()
	 */
	public PKey getPKey() {
		return null;
	}
	
	/**
	 * @return
	 */
	public int countInCoreObjs() {
		return getMetadata().getDatabase().countInCoreObjs();
	}

	/**
	 * Create the topmost network
	 * @param node
	 */
	public Net createTopNet(String netName){
		if(roots.size()!=0)
			throw new RuntimeException("The experiment already has a root!");
		return this.createNewRoot(netName);
	}

	
	/**
	 * Get the top  most network
	 * @param node
	 */
	public Net getRootNode(){
		if(roots.size()==0)
			return null;
		return (Net)roots.get(0);
	}
	
	/**
	 * Compile the model using default parameters
	 */
	public void compile(StatusListener sl) {
		compile(sl, new HashMap<String, String>());
	}
	
	/**
	 * Compile the model using the given parameters. Known parameters:
	 *  -- base_ip_address     : An ip address such as 192.0.0.0 which is 
	 *  						 used as the base ip during ip address assignment
	 *  -- protos_per_host : the number of protocol session uids to pre-assign to hosts
	 *  
	 * @param params parameters to be used during compilation.
	 */
	public void compile(StatusListener sl, Map<String,String> params) {
		try {
			/**
			 * Compilation consists of the following steps:
			 * 1) route policy verification
			 * 2) uid assignment
			 * 3) ip_address assignment
			 * 4) route calculation
			 */
			if(getState().val >= State.COMPILED.val) {
				jprime.Console.out.println("Exp is already compiled...skipping.");
				return;
			}
			else if(getState() != State.PRE_COMPILED) {
				throw new RuntimeException("Invalid state! The experiment is in state '"+getState()+"' and it MUST be in state '"+State.PRE_COMPILED+"' to be compiled!");
			}
	    	/* $if DEBUG $
			getMetadata().logPhaseChange(PhaseChange.verify_routes);
			$else$ */
			IPersistableCache.logPhaseChange(PhaseChange.verify_routes, "");
			/* $endif$ */
			jprime.Console.out.println("\t***************************************");
			jprime.Console.out.println("\tverifiy routes");
			jprime.Console.out.println("\t***************************************");
			jprime.Console.err.flush();
			jprime.Console.err.flush();
			//verify polices -- must be before we set this to the compile state since we may add nodes...
			new VerifyVisitor(getRootNode());
			sl.finsihed(5);
			setState(State.COMPILING);
			
	    	/* $if DEBUG $
			getMetadata().logPhaseChange(PhaseChange.assign_uids);
			$else$ */
			IPersistableCache.logPhaseChange(PhaseChange.assign_uids, "");
			/* $endif$ */
			jprime.Console.out.println("\t***************************************");
			jprime.Console.out.println("\t1) assign uids");
			jprime.Console.out.println("\t2) verify that all the child constraints are meet");
			jprime.Console.out.println("\t***************************************");
			jprime.Console.err.flush();
			jprime.Console.err.flush();
			//set uid,size,offset
			new UIDAssignmentVisitor(this, getRootNode(), params);
			sl.finsihed(10);
	
	    	/* $if DEBUG $
			getMetadata().logPhaseChange(PhaseChange.assign_ips);
			$else$ */
			IPersistableCache.logPhaseChange(PhaseChange.assign_ips, "");
			/* $endif$ */
			jprime.Console.out.println("\t***************************************");
			jprime.Console.out.println("\tassign ips");
			jprime.Console.out.println("\t***************************************");
			jprime.Console.err.flush();
			jprime.Console.err.flush();
			//set ip addrs
			new IPAddressAssignment(getRootNode(), params);
			sl.finsihed(10);
			
	    	/* $if DEBUG $
			getMetadata().logPhaseChange(PhaseChange.calc_routes);
			$else$ */
			IPersistableCache.logPhaseChange(PhaseChange.calc_routes, "");
			/* $endif$ */
			jprime.Console.out.println("\t***************************************");
			jprime.Console.out.println("\tcalc routes");
			jprime.Console.out.println("\t***************************************");
			jprime.Console.err.flush();
			jprime.Console.err.flush();
			//create forwarding tables
			new RouteCalculationVisitor(sl,75,getRootNode());
			//jprime.Console.out.println("Skipping routing");
			
	    	/* $if DEBUG $
			getMetadata().logPhaseChange(PhaseChange.cnf_crap);
			$else$ */
			IPersistableCache.logPhaseChange(PhaseChange.cnf_crap, "");
			/* $endif$ */
			jprime.Console.out.println("\t***************************************");
			jprime.Console.out.println("\tcnf-meta-processing");
			jprime.Console.out.println("\t***************************************");
			jprime.Console.err.flush();
			jprime.Console.err.flush();
			new CNFVisitor(getRootNode());

			
			//we are done compiling
			setState(State.COMPILED);
			
			//we should have used all the params
			if(params.size()!=0) {
				jprime.Console.err.println("WARNING: The following parameters where not used:");
				for(Entry<String, String> s : params.entrySet()) {
					jprime.Console.err.println("\t"+s.getKey()+" : "+s.getValue());				
				}
			}
			modified(Modified.ALL);
		} catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
			setState(State.PRE_COMPILED);
		}
	}
	
	/**
	 *  
	 * @param partitioning_string
	 * @return
	 */
	public Partitioning partition(String partitioning_string, Set<Portal> portals, List<ComputeNode> computeNodes) {
		try {
			if(getState().lt(State.COMPILED)) {
				throw new RuntimeException("Invalid state! The experiment is in state '"+getState()+"' and it MUST be in state '"+State.COMPILED+"' or higher to be partitioned!");
			}
			setState(State.PARTITIONING);
			Partitioner p = new Partitioner(getRootNode(), partitioning_string);
			Partitioning rv = p.partition(portals, computeNodes);
			setState(State.PARTITIONED);
			return rv;
		} catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
			return null;
		}
	}
	
	
	public void save(StatusListener sl){
		getMetadata().getDatabase().save(this, sl);
	}
	
	public synchronized void save(){
		getMetadata().save(this);
		getMetadata().save();
	}
	
	public List<EmulationCommand> getEmulationCommands() {
		return cmds;
	}
	
	public synchronized void __addEmulationCommand(EmulationCommand cmd) {
		cmds.add(cmd);
		modified(Modified.CHILDREN);
	}
	
	public void addEmuProtocol(IEmulationProtocol proto) {
		emu_protos.add(proto);
		modified(Modified.CHILDREN);
	}
	
	public PersistentModelNodeList<IEmulationProtocol> getEmuProtocols() {
		return emu_protos;
	}
}
