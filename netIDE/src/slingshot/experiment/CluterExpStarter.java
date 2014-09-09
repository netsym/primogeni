package slingshot.experiment;

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


import jprime.EmulationCommand;
import jprime.Net.Net;
import jprime.partitioning.ResetAlingments;
import jprime.util.PartTlvPair;
import monitor.core.Deployer;
import monitor.core.Provisioner;
import monitor.deployers.ProtoGeniDeployer;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.configuration.ConfigurationHandler;
import slingshot.environment.configuration.EnvironmentConfiguration;

/**
 * 
 * This class is responsible for preparing the model and deploying to a remote or primo geni cluster.
 * 
 * This process can be quite lengthy so it is done as a separate thread. When the process is done it will
 * update the command availability in the console.
 * 
 * @author Nathanael Van Vorst
 *
 */
public final class CluterExpStarter implements Runnable {
	private final PyExperiment pyexp;
	private final String resultsPath;
	private final Provisioner p;
	private final EnvironmentConfiguration ec;
	
	/**
	 * @param pyexp
	 * @param ec
	 * @param p
	 * @param resultsPath
	 */
	public CluterExpStarter(final PyExperiment pyexp, final EnvironmentConfiguration ec, final Provisioner p,final String resultsPath) {
		this.pyexp=pyexp;
		this.resultsPath=resultsPath;
		this.p=p;
		this.ec=ec;
	}
	
	/**
	 * @param what
	 * @param e
	 */
	private void error(String what, Exception e) {
		//if(pyexp.logTab!=null) {
			Util.dialog(Type.ERROR, "Encountered unexpected exception while "+what+":", Util.getStackTraceAsString(e));
		/*}
		else {					
			pyexp.println("Encountered unexpected exception while "+what+":\n"+Util.getStackTraceAsString(e));
		}*/
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		PartTlvPair parting=null;
		try {
			new ResetAlingments(pyexp.getRootNode());
			parting = pyexp.partition(ec.getPartitioningString(), resultsPath, ec.portal_links, ec.getComputeNodes());
		}
		catch(Exception e) {
			pyexp.setState(jprime.State.COMPILED);
			parting=null;
			error("partitioning",e);
		}
		if(parting!=null) {
			try {
				//pre-pare emulation commands
				Net topnet = pyexp.exp.getRootNode();
				for(EmulationCommand cmd : pyexp.exp.getEmulationCommands()) {
					cmd.evaluateCmd(topnet, ec.runtime);
				}
			}
			catch(Exception e) {
				pyexp.setState(jprime.State.PARTITIONED);
				error("evaluating emulation commands",e);
			}
			pyexp.setState(jprime.State.SETTING_UP);
			ProjectLoader.updateCommandAvailablility(pyexp.getName());
			Deployer dep =null;
			try {
				dep = new ProtoGeniDeployer(pyexp.getRootNode().getIpPrefix().toString(),pyexp,pyexp.exp,parting, p, ec.runtimeSymbolMap);
				pyexp.setCompileInfo(parting.partitioning, ec.getComputeNodes());
				pyexp.setController(dep.deploy(ConfigurationHandler.getOpenVPNDirectory()));
			}
			catch(Exception e) {
				pyexp.setState(jprime.State.PARTITIONED);
				pyexp.setController(null);
				dep=null;
				error("deploying experiment",e);
			}
			if(dep!=null) {
				try {
					dep.setupExperiment();
					pyexp.setState(jprime.State.SETUP);
					if(pyexp.logTab!=null) {
						Util.dialog(Type.NOTICE,"","Finished setting up the enviornment for '"+pyexp.exp.getName()+"'.");
					}
					else {
						pyexp.println("Finished setting up the enviornemt for '"+pyexp.exp.getName()+"'.");
					}
				}
				catch(Exception e) {
					pyexp.setState(jprime.State.PARTITIONED);
					dep=null;
					error("setting up experiment",e);
				}
			}
		}
		ProjectLoader.updateCommandAvailablility(pyexp.getName());
	}
}
