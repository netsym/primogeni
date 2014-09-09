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
import jprime.util.ChildList;
import jprime.util.PersistentEmuCmdList;
import jprime.variable.Dataset;
import jprime.variable.FloatingPointNumberVariable;
import jprime.visitors.IVizVisitor;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class Host extends jprime.gen.Host implements jprime.Host.IHost {
	protected PersistentEmuCmdList cmds;
	
	/**
	 * @param v
	 * @param n
	 */
	public Host(PyObject[] v, String[] n){
		super(v,n);
		cmds = new PersistentEmuCmdList(getMetadata(),this);
	}
	
	/**
	 * 
	 */
	public Host(ModelNodeRecord rec) {
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
	 */
	public Host(jprime.IModelNode parent) {
		super(parent);
		cmds = new PersistentEmuCmdList(getMetadata(),this);
	}

	@Override
	public synchronized ChildIdList getChildIds() {
		ChildIdList rv = super.getChildIds();
		if(cmds!=null) {
			for(ChildId c:cmds.getChildIds()) {
				rv.add(c);
			}
		}
		return rv;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId() { return jprime.EntityFactory.Host; }

	/*
	 * (non-Javadoc)
	 * 
	 * @see jprime.gen.Host#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jprime.IModelNode#accept(jprime.routing.VizVisitor)
	 */
	public void accept(IVizVisitor visitor) {
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
	 * XXX 
	 */
	public void enableEmulation(boolean useVPN) {
		for(ModelNode c : getAllChildren()) {
			if(c instanceof IInterface) {
				IInterface i = (IInterface)c;
				if(this.hasBeenReplicated()) {
					throw new RuntimeException("can't modify replicated nodes!");
				}
				else {
					if(useVPN) {
						i.createOpenVPNEmulation();
					}
					else {
						i.createTAPEmulation();
					}
				}
			}
		}
	}
	
	public void enableEmulation() {
		enableEmulation(false);
	}

	
	public List<EmulationCommand> getEmulationCommands() {
		if(cmds!=null)cmds.size();
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
		return getNics().size();
	}

	public static IInterface getDefaultInterface(IHost h) {
		ChildList<IInterface> nics = h.getNics();
		if(nics.size()==0)
			return null;
		return nics.enumerate().next();
	}
	
	public void deleteEmulationCommand(EmulationCommand emulationCommand) {
		if(hasBeenReplicated())	
			throw new RuntimeException("Cannot delete a node which has been replicated!");
		cmds.remove(emulationCommand);
		modified(Modified.CHILDREN);
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#delete_extra()
	 */
	@Override
	protected void delete_extra() {
		for(EmulationCommand cmd : cmds) {
			cmd.delete();
		}
		cmds.clear();
	}

	protected static void help(List<String> rv) {
		rv.add("Host/Router:");
		rv.add("   Interface creation:");
		rv.add("      createInterface()");
		rv.add("      createInterface(String)");
		rv.add("      createInterfaceReplica(IInterface)");
		rv.add("      createInterfaceReplica(String, IInterface)");
		rv.add("      newInterface(PyObject[], String[])");
		rv.add("      replicateInterface(PyObject[], String[])");
		rv.add("   getTrafficIntensity()\n"+
				  "      Get the maximum traffic intensity of the interfaces\n"+
				  "      attached to this node.");
		rv.add("   addEmulationCommand(EmulationCommand)\n"+
				  "      Add a new command to be run on the emulated machine.\n"+
				  "      The host must have enumlation enabled for this to succeed.");
		rv.add("   getEmulationCommands()\n"+
				  "      List the emulation commands currently attached to this host.");
		rv.add("   hasEmulationProtocol()\n"+
				  "      Determine if this host is emulated.");
		rv.add("   enableEmulation()\n"+
		  "      Enable OpenVZ emulation for this host.");
		rv.add("   enableEmulation(boolean useOpenVPN)\n"+
		  "      Enable emulation for this host.\n"+
		  "      useOpenVPN determines if OpenVZ or OpenVPN is used.");
	}
	
	public void addCNFContent(int contentID, int size) {
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
		cnf.addContentId(contentID,size);
	}
	
	public HashMap<Integer,Integer> getCNFContents() {
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


	
	public List<String> help() {
		List<String> rv=super.help();
		Host.help(rv);
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
