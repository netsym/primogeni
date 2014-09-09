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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import slingshot.ModelViewPerspective;
import slingshot.Perspective;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.ProjectLoader.ProjectState;
import slingshot.experiment.structure.SlingshotExperimentParent;

/**
 * 
 * The base class for all experiment commands
 * 
 * @author Nathanael Van Vorst
 * @author Eduardo Pena
 * @author Eduardo Tibau
 */
public abstract class ExperimentCommandHandler extends AbstractHandler implements
IHandler {
	protected ProjectState curExp;
	
	
	/**
	 * 
	 */
	public ExperimentCommandHandler() {
		super();
		ProjectLoader.addExpCommand(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public abstract Object execute(ExecutionEvent event) throws ExecutionException;

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
	 */
	@Override
	public abstract boolean isEnabled();

	/**
	 * @return
	 */
	protected boolean updateCurrentExperiment() {
		curExp=null;
		if(null == PlatformUI.getWorkbench() ||
				null == PlatformUI.getWorkbench().getActiveWorkbenchWindow() ||
				null == PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() ||
				null == PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective()) {
			return false;
		}
		else {
			String expName = "";
			IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
			try {
				//System.out.println("The current perspective ID is: "+descriptor.getId());
				if(descriptor.getId().equalsIgnoreCase(ModelViewPerspective.ID)){
					if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart() != null) {
						//System.out.println("We are in the Model View Perspective");
						expName = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getTitle();
						curExp = ProjectLoader.getProjectState(expName);
					}
				}
				else if(descriptor.getId().equalsIgnoreCase(Perspective.ID)){
					//System.out.println("We are in the Project Perspective");
		
					IStructuredSelection selection = (IStructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
		
					//System.out.println("Current selection is: "+selection );
					if(null != selection && selection.getFirstElement() != null){
						Object o = selection.getFirstElement();
						//System.out.println("The first element of this selection is: "+selection );
						if(o instanceof SlingshotExperimentParent){
							SlingshotExperimentParent e = (SlingshotExperimentParent) o;
							//System.out.println("The slection is an Experiment with name: "+e.getText() );
							expName = e.getText();
		
							curExp = ProjectLoader.getProjectState(expName);
						}
					}
				}
			}
			catch(Exception e) {
				curExp=null;
			}
		}
		//System.out.println("Looking for Experiment "+expName+" in our open experiments list found: "+exp );
		return curExp != null && curExp.exp!=null;
	}

	/**
	 * @param state
	 */
	public void updateButton(State state) {
		//System.out.println("\tupdateButton "+this.toString());
		final boolean shouldBeEnabled=this.isEnabled();
		if(shouldBeEnabled) {
			this.setEnabled(null);
		}
		this.isEnabled();
	}
}
