package monitor.provisoners;

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
import java.util.LinkedList;
import java.util.List;

import jprime.util.ComputeNode;
import jprime.util.Portal;

import monitor.core.Provisioner;



/**
 * @author Nathanael Van Vorst
 *
 */
public class PreAllocatedProvisioner extends Provisioner {
	private LinkedList<ComputeNode> computeNodes;
	public PreAllocatedProvisioner(List<String> ips) {
		super(ips.size());
		this.computeNodes = new LinkedList<ComputeNode>();
		for(String s : ips) {
			this.computeNodes.add(new ComputeNode(s, s, new ArrayList<Portal>()));
		}
		this.computeNodes.get(0).setPartitionId(null);
		for(int i =1;i<this.computeNodes.size();i++) {
			this.computeNodes.get(0).setPartitionId(i);
		}
	}
	public PreAllocatedProvisioner(ComputeNode master, List<ComputeNode> slaves) {
		super(slaves.size()+1);
		this.computeNodes=new LinkedList<ComputeNode>();
		this.computeNodes.add(master);
		this.computeNodes.addAll(slaves);
		if(master.getPartitionId() == null || master.getPartitionId()==-1) {
			master.setPartitionId(null);
		}
	}
	
	public void allocate() {
		//no op
	}
	
	public List<ComputeNode> getComputeNodes() {
		return computeNodes;
	}	
	
	public String toString() {
		return "[CannedProvisioner ips="+computeNodes+"]";
	}
}
