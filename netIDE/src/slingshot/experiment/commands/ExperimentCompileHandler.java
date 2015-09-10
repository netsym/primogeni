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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.PyExperiment;
import slingshot.experiment.compile.CompileWizard;

/**
 * @author Nathanael Van Vorst
 *
 */
public class ExperimentCompileHandler extends ExperimentCommandHandler implements
		IHandler {

	public static final String ID ="experiment.compile.handler";

	/* (non-Javadoc)
	 * @see slingshot.experiment.commands.ExperimentCommandHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return compileExperiment();
	}

	/**
	 * @return
	 */
	public PyExperiment compileExperiment(){
		if(updateCurrentExperiment()) {
			curExp.exp.println("Compiling experiment");
			final Shell shell = new Shell(Display.getDefault());
			CompileWizard cw = new CompileWizard();
			WizardDialog configdlg = new WizardDialog (shell, cw);
    		configdlg.open();//zzz_compile
    		String ipprefix = cw.getIPPrefix();
    		if(ipprefix != null) {
    			try {
    				if(curExp.view != null) {
    					curExp.view.showLogTab();
    				}
    				curExp.exp.compile(ipprefix);////zzz_compile
    			}
    			catch(Exception e ) {
    				Util.dialog(Type.ERROR,
    						"Unable to compile; encountered unexpected exception:",Util.getStackTraceAsString(e));
    			}
    			ProjectLoader.updateCommandAvailablility(curExp);
    			curExp.exp.showAttrs();
    		}
			return curExp.exp;
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
			if(curExp.getState().lte(State.PRE_COMPILED)) {
				setBaseEnabled(true);
				return true;
			}
		}
		setBaseEnabled(false);
		return false;
	}

}
