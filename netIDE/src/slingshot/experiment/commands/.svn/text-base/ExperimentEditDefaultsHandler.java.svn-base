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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import slingshot.wizards.configuration.EditDefaults;


/**
 * @author Nathanael Van Vorst
 *
 */
public class ExperimentEditDefaultsHandler extends ExperimentCommandHandler {
	/* (non-Javadoc)
	 * @see slingshot.experiment.commands.ExperimentCommandHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(updateCurrentExperiment()) {
			final Shell shell = new Shell(Display.getDefault());
			final String resultsPath =  ResourcesPlugin.getWorkspace().getRoot().getProject(curExp.exp.getName()).getLocation().toOSString();

    		WizardDialog configdlg = new WizardDialog (shell, new EditDefaults(resultsPath));
    		configdlg.open();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see slingshot.experiment.commands.ExperimentCommandHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		if(updateCurrentExperiment()) {
			setBaseEnabled(true);
			return true;
		}
		setBaseEnabled(false);
		return false;
	}
}
