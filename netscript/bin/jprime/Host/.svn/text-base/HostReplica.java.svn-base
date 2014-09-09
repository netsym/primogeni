package jprime.Host;

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
import java.util.Set;
import java.util.TreeSet;

import jprime.EmulationCommand;
import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.CNFApplication.ICNFApplication;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Interface.IInterface;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.util.PersistentEmuCmdList;
import jprime.variable.Dataset;
import jprime.variable.FloatingPointNumberVariable;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class HostReplica extends jprime.gen.HostReplica implements
		jprime.Host.IHost {
	protected PersistentEmuCmdList cmds;
	/**
	 * @param v
	 * @param n
	 */
	public HostReplica(PyObject[] v, String[] n){
		super(v,n);
		cmds = new PersistentEmuCmdList(getMetadata(),this);
	}
	
	/**
	 * 
	 */
	public HostReplica(ModelNodeRecord rec) {
		super(rec);
		cmds = new PersistentEmuCmdList(getMetadata(),this);
		for(ChildId c : rec.kids) {
			if(c.type==EntityFactory.EmulationCommand) {
				this.cmds.addKey(c.child_id);
			}
		}
	}

	/**
	 * @param parent
	 * @param referencedNode
	 */
	public HostReplica(String name, IModelNode parent, jprime.Host.IHost referencedNode) {
		super(name, parent, (jprime.Host.IHost) referencedNode);
		cmds = new PersistentEmuCmdList(getMetadata(),this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId() { return jprime.EntityFactory.HostReplica; }

	/*
	 * (non-Javadoc)
	 * 
	 * @see jprime.gen.HostReplica#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#setAlignment(jprime.partitioning.Partitioning, int)
	 */
	public void setAlignment(Partitioning p, int alignment) {
		throw new RuntimeException("Can't do this.....");
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		Set<Integer> rv = new TreeSet<Integer>();
		rv.add(p.getHostAlignment(this));
		return rv;
	}
	
	
	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#resetAlignments()
	 */
	public void resetAlignments() {
		//no op
	}
	
	/* (non-Javadoc)
	 * @see jprime.Host.IHost#hasEmulationProtocol()
	 */
	public boolean hasEmulationProtocol() {
		for(ModelNode c : getAllChildren()) {
			if(c instanceof IInterface) {
				for(ModelNode cc : c.getAllChildren()) {
					if(cc instanceof IEmulationProtocol)
						return true;
				}
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#accept(jprime.partitioning.PartitioningVisitor)
	 */
	public void accept(PartitioningVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * XXX this is hack for the geni demo....
	 */
	public void enableEmulation(boolean useVPN) {
		if(isReplica())
			this.convertToReal();
		for(ModelNode c : getAllChildren()) {
			if(c instanceof IInterface) {
				IInterface i = (IInterface)c;
				if(useVPN)
					i.createOpenVPNEmulation();
				else
					i.createTAPEmulation();
			}
		}
	}
	
	public void enableEmulation() {
		enableEmulation(false);
	}
	
	public List<EmulationCommand> getEmulationCommands() {
		return cmds;
	}
	
	public synchronized void __addEmulationCommand(EmulationCommand cmd) {
		if(!hasEmulationProtocol()) {
			throw new RuntimeException("You are trying to add an emulation commands to a host which is NOT emulated!");
		}
		cmds.add(cmd);
		modified(Modified.CHILDREN);
	}
	
	public int countInterfaces() {
		if(!isReplica()) {
			return getNics().size();
		}
		return ((IHost)getReplicatedNode()).countInterfaces();
	}
	
	public void addCNFContent(int contentID, int size) {
		if(isReplica())
			this.convertToReal();
		ICNFApplication cnf = null;
		for(IModelNode c: getAllChildren() ) {
			if(c instanceof ICNFApplication) {
				cnf = (ICNFApplication)c;
				break;
			}
		}
		if(cnf == null) {
			cnf = this.createCNFApplication();
		}
		cnf.addContentId(contentID, size);
	}
	
	public HashMap<Integer,Integer> getCNFContents() {
		if(isReplica()) {
			jprime.Console.err.println("WARNING: in host replica we are becomming real because of cnf!");
			this.convertToReal();
		}
		ICNFApplication cnf = null;
		for(IModelNode c: getAllChildren() ) {
			if(c instanceof ICNFApplication) {
				cnf = (ICNFApplication)c;
				break;
			}
		}
		if(cnf == null) {
			cnf = this.createCNFApplication();
			if (cnf.getContentIds() == null)
				return null;
		}
		return cnf.getContentIds();
	}
	
	public void deleteEmulationCommand(EmulationCommand emulationCommand) {
		if(isReplica())	
			throw new RuntimeException("Cannot delete a node which is a replica!");
		cmds.remove(emulationCommand);
		modified(Modified.CHILDREN);
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#delete_extra()
	 */
	@Override
	protected void delete_extra() {
		if(!isReplica()) {
			for(EmulationCommand cmd : cmds) {
				cmd.delete();
			}
			cmds.clear();
		}
	}
	
	public List<String> help() {
		List<String> rv=super.help();
		Host.help(rv);
		return rv;
	}
	
	@Override
	public synchronized ChildIdList getChildIds() {
		ChildIdList rv = super.getChildIds();
		if(cmds!=null)
			for(ChildId c:cmds.getChildIds()) {
				rv.add(c);
			}
		return rv;
	}
	
	public double getTrafficIntensity(Dataset d) {
		FloatingPointNumberVariable f = this.getTrafficIntensity();
		if(f != null) {
			return f.getRuntimeValue(this,d);
		}
		return 0.0;
	}

}
