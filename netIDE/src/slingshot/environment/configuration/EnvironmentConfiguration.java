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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jprime.util.ComputeNode;
import jprime.util.Portal;
import slingshot.environment.EnvType;

/**
 * 
 * After an enviornment is read from a file it is stored into an EnvironmentConfiguration. 
 * Each environment type will extend this class.
 * 
 * This is the object that is passed to the Meta-Controller for use during experiment deployment.
 * 
 * @author Nathanael Van Vorst
 *
 */
public abstract class EnvironmentConfiguration {
	private final String name;
	//Because these are one use, runtime can be stored here	private;
	public Integer runtime=null, numMachines=null, numProcs=null;
	public Double emuRatio=null;
	public String sliceName = null;
	public Set<Portal> portal_links=null;
	public Map<String,String> runtimeSymbolMap=new HashMap<String, String>();
	
	/**
	 * @param name
	 * @param runtime
	 * @param emuRatio
	 */
	protected EnvironmentConfiguration(String name) {
		super();
		this.name=name;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getPartitioningString() {
		if(numMachines == null)
			throw new RuntimeException("numMachines is null.");
		if(numProcs == null)
			throw new RuntimeException("numProcs is null.");
		String part_str=null;
		for(int i =1;i<=numMachines;i++) {
			if(part_str==null) part_str=numMachines+"::";
			else part_str+=",";
			part_str+=i+":"+numProcs;
		}
		return part_str;
	}
	
	/**
	 * @return
	 */
	public abstract EnvType getType();
	
	public abstract List<ComputeNode> getComputeNodes();
	
	public int portalCount() {
		return 0;
	}

}
