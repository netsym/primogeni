package slingshot.wizards.creation;

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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * This is the Wizard handler class for the Project Creation Wizard.
 * The purpose of this Wizard is to allow the user to create a new Slingshot Project in the Workspace.
 *
 * @author Eduardo Pena
 *
 */
public class SlingshotNewProjectWizard extends Wizard implements INewWizard {

	/** The id from the plugin.xml */
	public static final String ID = "slingshot.wizards.new.project";
	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "New Project";
	/** The name displayed on top of the Wizard's page */
	private static final String PAGE_NAME = "New Slingshot Project";

	/** The page for the Slingshot Project creation */
	private WizardNewProjectCreationPage projectPage;

	/**
	 * The Constructor for this class.
	 * It only sets the Wizard Name.
	 */
	public SlingshotNewProjectWizard() {
		super();
		setWindowTitle(WIZARD_NAME);
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();

		projectPage = new WizardNewProjectCreationPage(PAGE_NAME);
		projectPage.setTitle("New Slingshot Project");
		projectPage.setDescription("Create an empty Slingshot project.");

	    addPage(projectPage);
	}

	/**
	 * Method called when the Wizard is first created.
	 * Gets the workbench and the user's selection.
	 *
	 * @param workbench				The current Workbench
	 * @param selection				The user's selection
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	/**
	 * Method called as soon as the Wizard is finished.
	 * Creates a new project in the Workspace using the SlingshotProjectSupport class.
	 *
	 * @return boolean 				TRUE if the Wizard finished correctly, FALSE otherwise
	 */
	@Override
	public boolean performFinish() {
		//String name = projectPage.getProjectName();
	    //URI location = null;
	    if (!projectPage.useDefaults()) {
	    	//location = projectPage.getLocationURI();
	        projectPage.getLocationURI();
	    } // else location == null

	    //SlingshotProjectCreator.createProject();

		return true;
	}

}
