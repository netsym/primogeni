package slingshot.commands;
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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import slingshot.Application;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;

/**
 * This class handles the opening of experiments.
 * 
 * @author Nathanael Van Vorst
 *
 */
public class OpenFileHandler extends AbstractHandler implements IHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final String expName=ProjectLoader.getCurrentExpName();
		if(ProjectLoader.GENERATED_MODELS.compareTo(expName)!=0) {
			if(Application.db.listExperiments().contains(expName)) {
				ProjectLoader.LoadExperiment(expName);
			}
			else {
				Util.dialog(Type.ERROR,
						"",
						"The database does not contain a model by the name '"+expName+"'.\nDid you save the experiment before you closed it?");
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		final String expName=ProjectLoader.getCurrentExpName();
		if(ProjectLoader.GENERATED_MODELS.compareTo(expName)!=0) {
			if(Application.db.listExperiments().contains(expName)) {
				this.setBaseEnabled(true);
				return true;
			}
		}
		this.setBaseEnabled(false);
		return false;
	}

}
