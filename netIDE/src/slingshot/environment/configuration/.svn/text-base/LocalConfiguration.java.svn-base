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


import java.util.List;

import jprime.util.ComputeNode;
import slingshot.environment.EnvType;

/**
 * 
 * A local environment configuration.
 * 
 * @author Nathanael Van Vorst
 *
 */
public class LocalConfiguration extends EnvironmentConfiguration {

	/**
	 * @param name
	 * @param runtime
	 * @param emuRatio
	 * @param model
	 * @param viz
	 */
	public LocalConfiguration(Integer runtime, Integer numProcs, Double emuRatio) {
		super("-- no name --");
		this.runtime = runtime;
		this.emuRatio = emuRatio;
		this.numMachines = 1;
		this.numProcs = numProcs;
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.configuration.EnvironmentConfiguration#getType()
	 */
	public EnvType getType() {
		return EnvType.LOCAL;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.configuration.EnvironmentConfiguration#getComputeNodes()
	 */
	public List<ComputeNode> getComputeNodes() {
		return null; 
	}

}
