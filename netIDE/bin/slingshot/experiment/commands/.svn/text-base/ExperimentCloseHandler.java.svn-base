package slingshot.experiment.commands;

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

import jprime.State;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;

/**
 * @author Nathanael Van Vorst
 *
 */
public class ExperimentCloseHandler extends ExperimentCommandHandler implements IHandler {

	public static final String ID ="experiment.close.handler";
	/* (non-Javadoc)
	 * @see slingshot.experiment.commands.ExperimentCommandHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(updateCurrentExperiment()) {
			try {
				if(curExp.getState().gt(State.PARTITIONED)) {
					String title = "The experiment is still running!";
					String message = "Are you sure want to close "+curExp.exp.getName()+"? It appears to be running!";
					if(!Util.question(title, message)) {
						return null;
					}
				}
				//System.out.println("Closing curExp.exp.getName()");
				ProjectLoader.closeExp(curExp.exp.getName(), true);
				//System.out.println("\tCLOSED IT!");
				curExp=null;
				ProjectLoader.updateCommandAvailablility(curExp);
			}
			catch(Exception e) {
				Util.dialog(Type.ERROR,"Encountered unexpected exception while closing:",Util.getStackTraceAsString(e));
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see slingshot.experiment.commands.ExperimentCommandHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		//System.out.println(ID+" notified!");
		if(updateCurrentExperiment()) {
			setBaseEnabled(true);
			return true;
		}
		setBaseEnabled(false);
		return false;
	}
}
