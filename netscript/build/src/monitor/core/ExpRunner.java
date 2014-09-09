package monitor.core;

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

import java.util.Collection;

import jprime.Experiment;
import jprime.IModelNode;

/**
 * @author Nathanael Van Vorst
 *  startVEs.pl
 */
public class ExpRunner extends Thread {
	private boolean forceStop=false, running=true;;
	private final Experiment exp;
	private final int runtime;
	private final IExpListenter listener;
	private final Runnable setupThread;
	private Collection<IModelNode> initialAOI;
	public ExpRunner(Experiment exp, int runtime, IExpListenter listener, Collection<IModelNode> initialAOI) {
		this(exp,runtime,listener,null,initialAOI);
	}
	public ExpRunner(Experiment exp, int runtime, IExpListenter listener, Runnable setupThread, Collection<IModelNode> initialAOI) {
		super();
		this.exp = exp;
		this.runtime = runtime;
		this.listener=listener;
		this.setupThread=setupThread;
		this.initialAOI=initialAOI;
	}

	public void forceStop() {
		if(running){
			forceStop=true;
		}
		else{
			listener.finishedExperiment(true);
		}
	}
	
	public void run() {
		try {
			int waittime = runtime;
			if(setupThread != null) {
				listener.println("\nSetting up exp ExpRunner!");
				boolean error_in_setup=false;
				try {
					setupThread.run();
				}
				catch(Exception e) {
					error_in_setup=true;
				}
				if(error_in_setup || exp.getState().lt(jprime.State.SETUP)) {
					listener.println("Error during setup!");
					if(null != listener.getController())
						listener.getController().shutdown(false,true);
					return;
				}
				else {
					exp.setState(jprime.State.STARTING);
					listener.println("Starting up exp ExpRunner!");
					listener.getController().addToAreaOfInterest(initialAOI);
					//obaida-sending command to master machine
					listener.getController().runExperiment(exp, runtime, this);
					
				}
			}
			exp.setState(jprime.State.RUNNING);
			while((waittime>0 || listener.getController().getNumOutstandingCommands()>0) && !forceStop) {
				if(waittime % 5 == 0)
					listener.println("ExpRUnner exp is running. There are "+waittime+" seconds remaining. outstanding commands="+listener.getController().getNumOutstandingCommands());
					//listener.getController().
					//listener.println("hllo"+listener.getController().)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				waittime--;
				if(exp.getState()!=jprime.State.RUNNING || listener.getController().getFailures()>0)
					forceStop=true;
			}
			if(listener.getController()!=null) {
				listener.println("ExpRunner Finished exp. runtime="+waittime+", remaining commands="+listener.getController().getNumOutstandingCommands());
			boolean failed=listener.getController().getFailures()==0?
					(listener.getController().getNumOutstandingCommands()>0?true:false):true;					
				if(!failed) {
					listener.getController().cleanupExperiment(exp,listener);
					failed=listener.getController().getFailures()==0?false:true;
				}
				listener.getController().shutdown(!failed,failed);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			listener.finishedExperiment(true);
		}
		finally {
			running=false;
		}

	}

}
