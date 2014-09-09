package slingshot.navigator;

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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

import slingshot.Application;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;

/**
 * This class handles the double click action inside the project explorer.
 *
 * @author Eduardo Pena
 * @author Nathanael Van Vorst
 */
public class DoubleClickActionProvider extends CommonActionProvider {

	/** The action class that opens a file in the workbench */
	private OpenFAction openAction;

	/**
	 * The empty constructor
	 */
	public DoubleClickActionProvider() {
	}

	/**
	 * This method handles a double click inside the project explorer.
	 */
	@Override
	public void init(ICommonActionExtensionSite aSite){
		super.init(aSite);
		
		ICommonViewerSite viewSite = aSite.getViewSite();
		if(viewSite instanceof ICommonViewerWorkbenchSite){
			//New DoubleClickListener
			IDoubleClickListener dblistener = new IDoubleClickListener() {
				@Override
				public void doubleClick(DoubleClickEvent event) {
					//System.out.println("Opening project from navigator.");
					doubleClickAction(event);
				}
			};
			
			//add the listener
			aSite.getStructuredViewer().addDoubleClickListener(dblistener);
			
			//continue the rest of init
			ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite) viewSite;
			openAction = new OpenFAction(workbenchSite.getPage(), workbenchSite.getSelectionProvider());
		}
	}

	@Override
	public void fillActionBars(IActionBars actionBars){
		if(openAction.isEnabled())
			actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
		
	}

	public void fillContextMenu(IMenuManager menu){
		if(openAction.isEnabled())
			menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, openAction);
	}
	
	protected void doubleClickAction(DoubleClickEvent event) {
		final String expName=ProjectLoader.getCurrentExpName();
		if(expName!=null && ProjectLoader.GENERATED_MODELS.compareTo(expName)!=0) {
			if(ProjectLoader.getProjectState(expName)!=null) {
				Util.dialog(Type.NOTICE,"",
						"The experiment "+expName+" is already open.");
			}
			else {
				if(Application.db.listExperiments().contains(expName)) {
					ProjectLoader.LoadExperiment(expName);
				}
				else {
					Util.dialog(Type.ERROR,"",
							"The database does not contain a model by the name '"+expName+"'.\nDid you save the experiment before you closed it?");
				}
			}
		}
	}

}
