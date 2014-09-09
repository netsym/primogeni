package monitor.commands;

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

import java.util.ArrayList;

import jprime.util.ComputeNode;

import monitor.core.NodeConfig;

/**
 * @author Nathanael Van Vorst
 *
 * The command setups the roles of all of the slaves and
 * instructs the master to connect to the slaves
 *
 */
public class ConnectToSlavesCmd extends AbstractCmd{
	private ArrayList<NodeConfig> slaveConfigs;
    private NodeConfig masterConfig;
    
    public ConnectToSlavesCmd() {
    	super(CommandType.CONNNECT_SLAVES);
    	this.slaveConfigs= new ArrayList<NodeConfig>();
    	this.masterConfig=null;
    }
    
 
	@Override
	public String toString() {
		String rv="[ConnectToSlavesCmd: serialNumber="+getSerialNumber()+", slaveConfigs=<";
		for(NodeConfig nc : slaveConfigs) rv+=nc;
		rv+=">, masterConfig="+masterConfig+"]";
		return rv;
    }

	public ArrayList<NodeConfig> getSlaveConfigs() {
		return slaveConfigs;
	}

	public void addSlaveConfig(int nodeType, int machineId, ComputeNode cn) {
		addSlaveConfig(new NodeConfig(nodeType, machineId, cn));
	}
	
	public void addSlaveConfig(NodeConfig slaveConfig) {
		if(slaveConfig.partition_id == -1) {
			throw new RuntimeException("Slaves must have their partition id set!");
		}
		this.slaveConfigs.add(slaveConfig);
	}

	public NodeConfig getMasterConfig() {
		return masterConfig;
	}

	public void setMasterConfig(int nodeType, int machineId, ComputeNode ip) {
		setMasterConfig(new NodeConfig(nodeType, machineId, ip));
	}

	public void setMasterConfig(NodeConfig masterConfig) {
		if(this.masterConfig!=null)
			throw new RuntimeException("The master config was already set!");
		if(masterConfig.partition_id != -1) {
			throw new RuntimeException("The master cannot have its partition id set!");
		}
		this.masterConfig = masterConfig;
	}

	@Override
	public int getBodyLength()
	{
		int rv = Integer.SIZE/8+masterConfig.size();
		for(NodeConfig nc : slaveConfigs)
			rv+=nc.size();
		return rv;
	}
}
