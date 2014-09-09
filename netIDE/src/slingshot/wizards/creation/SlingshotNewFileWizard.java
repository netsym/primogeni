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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

/**
 * This is the Wizard handler class for the Configuration File Creation Wizard.
 * The purpose of this Wizard is to allow the user to create a new Slingshot Python Source File in the Workspace.
 *
 * @author Eduardo Pena
 *
 */
public class SlingshotNewFileWizard extends Wizard implements INewWizard {

	/** The id from the plugin.xml */
	public static final String ID = "slingshot.wizards.file";

	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "New File";

	/** The page for the Python source file creation */
	private WizardNewFileCreationPage filePage;

	/** The workbench in use when the wizard is called */
	private IWorkbench workbench;
	/** The user's selection when the wizard is called */
	private IStructuredSelection selection;

	/**
	 * The Constructor for this class.
	 * It only sets the Wizard Name.
	 */
	public SlingshotNewFileWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();
		filePage = new WizardNewFileCreationPage(selection, "New File", "Create a new File in the workspace.");
		addPage(filePage);
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
		this.workbench = workbench;
		this.selection = selection;
	}

	/**
	 * Method called as soon as the Wizard is finished.
	 * Finds the Project where the file must be created, creates a new Python Source File in the correct folder and opens the new file in the Editor.
	 *
	 * @return boolean 				TRUE if the Wizard finished correctly, FALSE otherwise
	 *///open the new file in the Editor
	@Override
	public boolean performFinish() {
		boolean result = false;
		//create the new Java Source file
	    IFile file = filePage.createNewFile();
	    result = file != null;
	    //check whether the file was correctly created
	    if (result) {
	        try {
	        	//set the correct Editor for this file
	        	IDE.setDefaultEditor(file, "slingshot.DefaultEditor");
	        	//open the new file in the Editor
	            IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file);
	        } catch (PartInitException e) {
	            e.printStackTrace();
	        }
	    } // else no file created...result == false
	    return result;
	}

}