package slingshot;

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

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import slingshot.navigator.CustomNavigator;

/**
 * This class was generated automatically by eclipse's RCP tool.
 * Here the perspective shown when the application starts is defined.
 *
 * @author Eduardo Pena
 *
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	/** The ID of the starting perspective */
	private static final String PERSPECTIVE_ID = "slingshot.perspective";

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public void postStartup() {
		super.postStartup();
		
		//Remove unnecessary menu items
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();		
		page.hideActionSet("org.eclipse.search.searchActionSet");

		// Used when the navigator selection changes
		ISelectionChangedListener navSelectionListener = new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event){

	        	//TODO: HANDLE THIS EXCEPTION CORRECTLY

	        	//get the categories that should be refreshed when the selection on the navigator changes
	        	ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
	        	Command[] commands = service.getDefinedCommands();

	        	for(Command c : commands){
	        		try{
	        			if(c.getCategory().getId().equalsIgnoreCase("slingshot.experiment.commands")){
	        				//System.out.println("Command found: "+c.getName());
	        				c.isEnabled();
	        			}
	        		}catch(Exception e){}
	        	}

			}
		};

		try{
			IViewReference ref = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference(CustomNavigator.ID);
			CommonNavigator cn = (CommonNavigator)ref.getPart(false);
			CommonViewer viewer = cn.getCommonViewer();
			viewer.addSelectionChangedListener(navSelectionListener);
		}catch(Exception e){
			System.err.println("Couldn't create the selection listener for the navigator.");
			e.printStackTrace();
		}
	}
}
