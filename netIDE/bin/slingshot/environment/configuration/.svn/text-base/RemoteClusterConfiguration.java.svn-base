package slingshot.environment.configuration;

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


import java.util.LinkedList;
import java.util.List;

import jprime.util.ComputeNode;
import slingshot.environment.EnvType;
import slingshot.environment.EnvironmentFileModel;

/**
 * 
 * A remote environment configuration.
 * 
 * @author Nathanael Van Vorst
 *
 */
public class RemoteClusterConfiguration extends EnvironmentConfiguration {
	
	protected ComputeNode master;
	protected LinkedList<ComputeNode> slaves;
	protected String slice_name;
	protected String linked_env;
	protected EnvType linked_env_type;
	
	/**
	 * @param name
	 * @param runtime
	 * @param model
	 */
	public RemoteClusterConfiguration(String name, EnvironmentFileModel model) {
		super(name);
		this.linked_env=model.linked_env_name;
		this.linked_env_type=model.linked_env_type;
		this.slice_name=model.slice_name;
		this.master = model.nodes.get(0);
		this.slaves = new LinkedList<ComputeNode>();
		this.slaves.addAll(model.nodes.subList(1, model.nodes.size()));
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.configuration.EnvironmentConfiguration#getType()
	 */
	public EnvType getType() { return EnvType.REMOTE_CLUSTER;}
	
	/**
	 * @return
	 */
	public ComputeNode getMaster() {
		return master;
	}
	
	/**
	 * @param master
	 */
	public void setMaster(ComputeNode master) {
		this.master = master;
	}
	
	/**
	 * @return
	 */
	public LinkedList<ComputeNode> getSlaves() {
		return slaves;
	}
	
	/**
	 * @param slaves
	 */
	public void setSlaves(LinkedList<ComputeNode> slaves) {
		this.slaves = slaves;
	}
	
	/**
	 * @return
	 */
	public String getLinked_env() {
		return linked_env;
	}

	/**
	 * @return
	 */
	public EnvType getLinked_env_type() {
		return linked_env_type;
	}
	
	/**
	 * @return
	 */
	public String getSliceName() {
		return slice_name;
	}
	
	public int portalCount() {
		int rv =0;
		for(ComputeNode c : slaves) {
			rv+=c.getPortals().size();
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.configuration.EnvironmentConfiguration#getComputeNodes()
	 */
	public List<ComputeNode> getComputeNodes() {
		return getSlaves(); 
	}
	
}
