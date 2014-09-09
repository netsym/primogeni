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

import jprime.EmulationCommand;
import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.partitioning.Partitioning;
import jprime.variable.Dataset;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class HostAliasReplica extends jprime.gen.HostAliasReplica implements
		jprime.Host.IHostAlias {
	/**
	 * @param v
	 * @param n
	 */
	public HostAliasReplica(PyObject[] v, String[] n){super(v,n);}
	
	/**
	 * 
	 */
	public HostAliasReplica(ModelNodeRecord rec) {
		super(rec);
	}
	
	/**
	 * @param parent
	 * @param referencedNode
	 */
	public HostAliasReplica(String name, IModelNode parent,
			jprime.ModelNodeAlias referencedNode) {
		super(name, parent, referencedNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId() { return jprime.EntityFactory.HostAliasReplica; }

	/*
	 * (non-Javadoc)
	 * 
	 * @see jprime.gen.HostAliasReplica#accept(jprime.visitors.IGenericVisitor)
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
		return ((IHost)deference()).getAlignments(p);
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
		return ((IHost)deference()).hasEmulationProtocol();
	}

	public void enableEmulation(boolean useVPN) {
		((IHost)deference()).enableEmulation(useVPN);
	}
	
	public void enableEmulation() {
		enableEmulation(false);
	}
	
	public List<EmulationCommand> getEmulationCommands() {
		return ((IHost)deference()).getEmulationCommands();
	}
	
	public void __addEmulationCommand(EmulationCommand cmd) {
		 ((IHost)deference()).__addEmulationCommand(cmd);
	}
	
	public void addCNFContent(int contentID, int size) {
		((IHost)deference()).addCNFContent(contentID, size);
	}
	
	public HashMap<Integer,Integer> getCNFContents() {
		return ((IHost)deference()).getCNFContents();
	}

	public int countInterfaces() {
		return ((IHost)deference()).countInterfaces();
	}
	
	public void deleteEmulationCommand(EmulationCommand emulationCommand) {
		throw new RuntimeException("Dont call this on a host alias!");
	}

	public List<String> help() {
		List<String> rv=super.help();
		Host.help(rv);
		return rv;
	}
	
	public double getTrafficIntensity(Dataset d) {
		return ((IHost)deference()).getTrafficIntensity(d);
	}

}


